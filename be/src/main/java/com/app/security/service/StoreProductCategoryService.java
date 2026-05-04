package com.app.security.service;

import com.app.security.model.StoreProductCategory;

import java.util.List;

public interface StoreProductCategoryService {

    List<StoreProductCategory> getAllByStoreId(String storeId);

    StoreProductCategory getById(String productCategoryId);

    void create(String storeId, String name);

    void update(String productCategoryId, String name);

    void delete(String productCategoryId);
}
