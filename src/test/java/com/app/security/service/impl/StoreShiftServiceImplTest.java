package com.app.security.service.impl;

import com.app.security.dao.StoreDao;
import com.app.security.dao.StoreShiftDao;
import com.app.security.enums.ShiftStatus;
import com.app.security.exception.ShiftLimitReachedException;
import com.app.security.model.Store;
import com.app.security.model.StoreShift;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoreShiftServiceImplTest {

    private static final String STORE_ID = "store-1";
    private static final String CURRENT_MEMBER_ID = "member-current";
    private static final String OWNER_MEMBER_ID = "member-owner";
    private static final String SHIFT_ID = "shift-1";

    @Mock
    private StoreShiftDao storeShiftDao;

    @Mock
    private StoreDao storeDao;

    @InjectMocks
    private StoreShiftServiceImpl storeShiftService;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    /**
     * 設定當前登入者：把 memberId 放在 credentials，對應 JwtAuthenticationFilter 的寫入方式。
     */
    private void setAuthentication(String memberId) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("email", memberId, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private Store store(int limit) {
        Store store = new Store();
        store.setStoreId(STORE_ID);
        store.setRunning_devices_limit(limit);
        return store;
    }

    private StoreShift shift(String memberId, ShiftStatus status) {
        StoreShift shift = new StoreShift();
        shift.setStoreShiftId(SHIFT_ID);
        shift.setStoreId(STORE_ID);
        shift.setMemberId(memberId);
        shift.setStatus(status);
        return shift;
    }

    // ========== openShift ==========

    /**
     * 驗證：store 不存在 → 404 STORE_NOT_FOUND，且不應再去查 / 寫 store_shift。
     */
    @Test
    @DisplayName("openShift: store 不存在應丟 404")
    public void openShift_storeNotFound_throws() {
        when(storeDao.getStoreById(STORE_ID)).thenReturn(null);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> storeShiftService.openShift(STORE_ID)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("STORE_NOT_FOUND", ex.getReason());
        verify(storeShiftDao, never()).openShift(anyString(), anyString());
    }

    /**
     * 驗證：openShifts 數量未達 running_devices_limit → 由 dao 開班並回傳新 id，
     * 開班 member 取自當前登入者。
     */
    @Test
    @DisplayName("openShift: 未達上限應成功開班並回傳 shiftId")
    public void openShift_underLimit_success() {
        setAuthentication(CURRENT_MEMBER_ID);
        when(storeDao.getStoreById(STORE_ID)).thenReturn(store(2));
        when(storeShiftDao.getOpenByStoreId(STORE_ID))
                .thenReturn(List.of(shift(OWNER_MEMBER_ID, ShiftStatus.OPEN)));
        when(storeShiftDao.openShift(STORE_ID, CURRENT_MEMBER_ID)).thenReturn(SHIFT_ID);

        String result = storeShiftService.openShift(STORE_ID);

        assertEquals(SHIFT_ID, result);
        verify(storeShiftDao).openShift(STORE_ID, CURRENT_MEMBER_ID);
    }

    /**
     * 驗證：openShifts 數量已達 running_devices_limit → 丟 ShiftLimitReachedException，
     * exception 內須帶當下所有 OPEN shift（前端可顯示「已超過能開的班，目前由 X / Y 開班中」）。
     */
    @Test
    @DisplayName("openShift: 達到上限應丟 SHIFT_LIMIT_REACHED 並附上目前 OPEN shifts")
    public void openShift_limitReached_throwsWithOpenShifts() {
        setAuthentication(CURRENT_MEMBER_ID);
        List<StoreShift> openShifts = List.of(
                shift(OWNER_MEMBER_ID, ShiftStatus.OPEN),
                shift("member-x", ShiftStatus.OPEN)
        );
        when(storeDao.getStoreById(STORE_ID)).thenReturn(store(2));
        when(storeShiftDao.getOpenByStoreId(STORE_ID)).thenReturn(openShifts);

        ShiftLimitReachedException ex = assertThrows(
                ShiftLimitReachedException.class,
                () -> storeShiftService.openShift(STORE_ID)
        );

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertEquals("SHIFT_LIMIT_REACHED", ex.getReason());
        assertSame(openShifts, ex.getOpenShifts());
        verify(storeShiftDao, never()).openShift(anyString(), anyString());
    }

    // ========== closeShift ==========

    /**
     * 驗證：shift 不存在 → 404 STORE_SHIFT_NOT_FOUND，不應呼叫 dao.closeShift。
     */
    @Test
    @DisplayName("closeShift: shift 不存在應丟 404")
    public void closeShift_shiftNotFound_throws() {
        when(storeShiftDao.getById(SHIFT_ID)).thenReturn(null);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> storeShiftService.closeShift(SHIFT_ID)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("STORE_SHIFT_NOT_FOUND", ex.getReason());
        verify(storeShiftDao, never()).closeShift(anyString());
    }

    /**
     * 驗證：shift 已 CLOSED → 409 SHIFT_ALREADY_CLOSED，不可再關。
     */
    @Test
    @DisplayName("closeShift: 已 CLOSED 應丟 409")
    public void closeShift_alreadyClosed_throws() {
        when(storeShiftDao.getById(SHIFT_ID)).thenReturn(shift(OWNER_MEMBER_ID, ShiftStatus.CLOSED));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> storeShiftService.closeShift(SHIFT_ID)
        );

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertEquals("SHIFT_ALREADY_CLOSED", ex.getReason());
        verify(storeShiftDao, never()).closeShift(anyString());
    }

    /**
     * 驗證：當前登入者是該班別的 member 本人 → 可以關自己的班。
     */
    @Test
    @DisplayName("closeShift: 當前 member 是 shift owner 應成功關班")
    public void closeShift_byOwner_success() {
        setAuthentication(OWNER_MEMBER_ID);
        when(storeShiftDao.getById(SHIFT_ID)).thenReturn(shift(OWNER_MEMBER_ID, ShiftStatus.OPEN));

        storeShiftService.closeShift(SHIFT_ID);

        verify(storeShiftDao).closeShift(SHIFT_ID);
    }

    /**
     * 驗證：當前登入者非本人 → 403 CANNOT_CLOSE_OTHERS_SHIFT，不可代他人關班。
     */
    @Test
    @DisplayName("closeShift: 非本人應丟 403")
    public void closeShift_nonOwner_forbidden() {
        setAuthentication(CURRENT_MEMBER_ID);
        when(storeShiftDao.getById(SHIFT_ID)).thenReturn(shift(OWNER_MEMBER_ID, ShiftStatus.OPEN));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> storeShiftService.closeShift(SHIFT_ID)
        );

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        assertEquals("CANNOT_CLOSE_OTHERS_SHIFT", ex.getReason());
        verify(storeShiftDao, never()).closeShift(anyString());
    }
}
