package com.app.security.service;

import com.app.security.model.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    List<ProductCategory> getAllByStoreId(String storeId);

    ProductCategory getById(String productCategoryId);

    void create(String storeId, String name);

    void update(String productCategoryId, String name);

    void delete(String productCategoryId);
}
