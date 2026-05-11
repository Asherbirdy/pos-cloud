package com.app.security.service;

import com.app.security.model.StoreCheckoutItem;

import java.math.BigDecimal;
import java.util.List;

public interface StoreCheckoutItemService {

    List<StoreCheckoutItem> getAllByCheckoutId(String storeCheckoutId);

    StoreCheckoutItem getById(String storeCheckoutItemId);

    String create(String storeCheckoutId, String storeProductItemId, Integer quantity, BigDecimal unitPrice);

    void delete(String storeCheckoutItemId);
}
