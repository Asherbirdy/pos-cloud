package com.app.security.dao;

import com.app.security.model.StoreCheckoutItem;

import java.math.BigDecimal;
import java.util.List;

public interface StoreCheckoutItemDao {

    List<StoreCheckoutItem> getAllByCheckoutId(String storeCheckoutId);

    StoreCheckoutItem getById(String storeCheckoutItemId);

    String create(String storeCheckoutId, String storeProductItemId, Integer quantity, BigDecimal unitPrice, BigDecimal subtotal);

    void delete(String storeCheckoutItemId);
}
