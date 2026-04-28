package com.app.security.dao;

import com.app.security.model.StoreShift;

import java.util.List;

public interface StoreShiftDao {

    List<StoreShift> getAllByStoreId(String storeId);

    StoreShift getById(String storeShiftId);

    int countOpenByStoreId(String storeId);

    String openShift(String storeId, String memberId);

    void closeShift(String storeShiftId);
}
