package com.app.security.exception;

import com.app.security.model.StoreShift;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * 開班數量已達 store.running_devices_limit 時丟出，
 * 攜帶當下仍在 OPEN 狀態的班別列表，讓前端顯示「哪些人正在開班」。
 */
public class ShiftLimitReachedException extends ResponseStatusException {

    private final List<StoreShift> openShifts;

    public ShiftLimitReachedException(List<StoreShift> openShifts) {
        super(HttpStatus.CONFLICT, "SHIFT_LIMIT_REACHED");
        this.openShifts = openShifts;
    }

    public List<StoreShift> getOpenShifts() {
        return openShifts;
    }
}
