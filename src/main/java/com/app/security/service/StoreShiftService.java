package com.app.security.service;

import com.app.security.model.StoreShift;

import java.util.List;

public interface StoreShiftService {

    List<StoreShift> getAllByStoreId(String storeId);

    StoreShift getById(String storeShiftId);

    String openShift(String storeId);

    void closeShift(String storeShiftId);
}
