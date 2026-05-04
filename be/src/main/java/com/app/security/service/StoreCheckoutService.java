package com.app.security.service;

import com.app.security.dto.StoreCheckout.StoreCheckoutCreateRequest;
import com.app.security.model.StoreCheckout;

import java.util.List;

public interface StoreCheckoutService {

    List<StoreCheckout> getAllByStoreId(String storeId);

    List<StoreCheckout> getAllByShiftId(String storeShiftId);

    StoreCheckout getById(String storeCheckoutId);

    String create(String storeId, String storeShiftId, List<StoreCheckoutCreateRequest.CheckoutItemLine> items);

    void cancel(String storeCheckoutId);
}
