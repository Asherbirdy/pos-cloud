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

    /**
     * 驗證：當前 OPEN shift 數量已達 store.running_devices_limit → 丟 ShiftLimitReachedException
     * （HTTP 409 SHIFT_LIMIT_REACHED）並夾帶當下所有 OPEN shifts，且不應呼叫 dao.openShift。
     */
    @Test
    @DisplayName("openShift: 達到 running_devices_limit 應丟 SHIFT_LIMIT_REACHED")
    public void openShift_limitReached_throws() {
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

    /**
     * 驗證：當前登入者非該 shift 的 owner（開班本人） → 丟 403 CANNOT_CLOSE_OTHERS_SHIFT，
     * 不應呼叫 dao.closeShift。只有開班本人能關掉自己的班。
     */
    @Test
    @DisplayName("closeShift: 非本人應丟 403 CANNOT_CLOSE_OTHERS_SHIFT")
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
