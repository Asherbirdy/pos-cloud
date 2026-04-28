package com.app.security.service.impl;

import com.app.security.dao.ProductCategoryDao;
import com.app.security.model.ProductCategory;
import com.app.security.service.ProductCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryDao productCategoryDao;

    public ProductCategoryServiceImpl(ProductCategoryDao productCategoryDao) {
        this.productCategoryDao = productCategoryDao;
    }

    @Override
    public List<ProductCategory> getAllByStoreId(String storeId) {
        return productCategoryDao.getAllByStoreId(storeId);
    }

    @Override
    public ProductCategory getById(String productCategoryId) {
        ProductCategory productCategory = productCategoryDao.getById(productCategoryId);
        if (productCategory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PRODUCT_CATEGORY_NOT_FOUND");
        }
        return productCategory;
    }

    @Override
    public void create(String storeId, String name) {
        productCategoryDao.create(storeId, name);
    }

    @Override
    public void update(String productCategoryId, String name) {
        ProductCategory productCategory = productCategoryDao.getById(productCategoryId);
        if (productCategory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PRODUCT_CATEGORY_NOT_FOUND");
        }
        productCategoryDao.update(productCategoryId, name);
    }

    @Override
    public void delete(String productCategoryId) {
        ProductCategory productCategory = productCategoryDao.getById(productCategoryId);
        if (productCategory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PRODUCT_CATEGORY_NOT_FOUND");
        }
        productCategoryDao.delete(productCategoryId);
    }
}
