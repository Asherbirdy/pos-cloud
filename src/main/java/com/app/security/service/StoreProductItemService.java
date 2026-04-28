package com.app.security.service;

import com.app.security.model.StoreProductItem;

import java.math.BigDecimal;
import java.util.List;

public interface StoreProductItemService {

    List<StoreProductItem> getAllByCategoryId(String storeProductCategoryId);

    StoreProductItem getById(String storeProductItemId);

    void create(String storeProductCategoryId, String name, BigDecimal currentPrice);

    void update(String storeProductItemId, String name, BigDecimal currentPrice);

    void delete(String storeProductItemId);
}
