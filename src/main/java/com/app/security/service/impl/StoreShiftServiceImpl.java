package com.app.security.service.impl;

import com.app.security.dao.StoreDao;
import com.app.security.dao.StoreShiftDao;
import com.app.security.enums.ShiftStatus;
import com.app.security.exception.ShiftLimitReachedException;
import com.app.security.model.Store;
import com.app.security.model.StoreShift;
import com.app.security.service.StoreShiftService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class StoreShiftServiceImpl implements StoreShiftService {

    private final StoreShiftDao storeShiftDao;

    private final StoreDao storeDao;

    public StoreShiftServiceImpl(StoreShiftDao storeShiftDao, StoreDao storeDao) {
        this.storeShiftDao = storeShiftDao;
        this.storeDao = storeDao;
    }

    @Override
    public List<StoreShift> getAllByStoreId(String storeId) {
        return storeShiftDao.getAllByStoreId(storeId);
    }

    @Override
    public StoreShift getById(String storeShiftId) {
        StoreShift shift = storeShiftDao.getById(storeShiftId);
        if (shift == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_SHIFT_NOT_FOUND");
        }
        return shift;
    }

    @Override
    public String openShift(String storeId) {
        Store store = storeDao.getStoreById(storeId);
        if (store == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_NOT_FOUND");
        }

        List<StoreShift> openShifts = storeShiftDao.getOpenByStoreId(storeId);
        if (openShifts.size() >= store.getRunning_devices_limit()) {
            throw new ShiftLimitReachedException(openShifts);
        }

        String memberId = currentMemberId();
        return storeShiftDao.openShift(storeId, memberId);
    }

    @Override
    public void closeShift(String storeShiftId) {
        StoreShift shift = storeShiftDao.getById(storeShiftId);
        if (shift == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_SHIFT_NOT_FOUND");
        }
        if (shift.getStatus() == ShiftStatus.CLOSED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SHIFT_ALREADY_CLOSED");
        }

        // 只有開班本人可以關班
        if (!currentMemberId().equals(shift.getMemberId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "CANNOT_CLOSE_OTHERS_SHIFT");
        }

        storeShiftDao.closeShift(storeShiftId);
    }

    private String currentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getCredentials();
    }
}
