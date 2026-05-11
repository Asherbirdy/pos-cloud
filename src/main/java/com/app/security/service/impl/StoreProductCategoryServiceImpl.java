package com.app.security.service.impl;

import com.app.security.dao.StoreProductCategoryDao;
import com.app.security.model.StoreProductCategory;
import com.app.security.service.StoreProductCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class StoreProductCategoryServiceImpl implements StoreProductCategoryService {

    private final StoreProductCategoryDao storeProductCategoryDao;

    public StoreProductCategoryServiceImpl(StoreProductCategoryDao storeProductCategoryDao) {
        this.storeProductCategoryDao = storeProductCategoryDao;
    }

    @Override
    public List<StoreProductCategory> getAllByStoreId(String storeId) {
        return storeProductCategoryDao.getAllByStoreId(storeId);
    }

    @Override
    public StoreProductCategory getById(String productCategoryId) {
        StoreProductCategory storeProductCategory = storeProductCategoryDao.getById(productCategoryId);
        if (storeProductCategory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_PRODUCT_CATEGORY_NOT_FOUND");
        }
        return storeProductCategory;
    }

    @Override
    public void create(String storeId, String name) {
        storeProductCategoryDao.create(storeId, name);
    }

    @Override
    public void update(String productCategoryId, String name) {
        StoreProductCategory storeProductCategory = storeProductCategoryDao.getById(productCategoryId);
        if (storeProductCategory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_PRODUCT_CATEGORY_NOT_FOUND");
        }
        storeProductCategoryDao.update(productCategoryId, name);
    }

    @Override
    public void delete(String productCategoryId) {
        StoreProductCategory storeProductCategory = storeProductCategoryDao.getById(productCategoryId);
        if (storeProductCategory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_PRODUCT_CATEGORY_NOT_FOUND");
        }
        storeProductCategoryDao.delete(productCategoryId);
    }
}
