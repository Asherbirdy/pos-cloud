package com.app.security.dao;

import com.app.security.model.StoreCheckout;

import java.math.BigDecimal;
import java.util.List;

public interface StoreCheckoutDao {

    List<StoreCheckout> getAllByStoreId(String storeId);

    List<StoreCheckout> getAllByShiftId(String storeShiftId);

    StoreCheckout getById(String storeCheckoutId);

    String create(String storeId, String storeShiftId, String memberId, BigDecimal settlePrice);

    void cancel(String storeCheckoutId);
}
