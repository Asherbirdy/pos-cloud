package com.app.security.service.impl;

import com.app.security.dao.StoreProductItemDao;
import com.app.security.model.StoreProductItem;
import com.app.security.service.StoreProductItemService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Component
public class StoreProductItemServiceImpl implements StoreProductItemService {

    private final StoreProductItemDao storeProductItemDao;

    public StoreProductItemServiceImpl(StoreProductItemDao storeProductItemDao) {
        this.storeProductItemDao = storeProductItemDao;
    }

    @Override
    public List<StoreProductItem> getAllByCategoryId(String storeProductCategoryId) {
        return storeProductItemDao.getAllByCategoryId(storeProductCategoryId);
    }

    @Override
    public StoreProductItem getById(String storeProductItemId) {
        StoreProductItem item = storeProductItemDao.getById(storeProductItemId);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_PRODUCT_ITEM_NOT_FOUND");
        }
        return item;
    }

    @Override
    public void create(String storeProductCategoryId, String name, BigDecimal currentPrice) {
        storeProductItemDao.create(storeProductCategoryId, name, currentPrice);
    }

    @Override
    public void update(String storeProductItemId, String name, BigDecimal currentPrice) {
        StoreProductItem item = storeProductItemDao.getById(storeProductItemId);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_PRODUCT_ITEM_NOT_FOUND");
        }
        storeProductItemDao.update(storeProductItemId, name, currentPrice);
    }

    @Override
    public void delete(String storeProductItemId) {
        StoreProductItem item = storeProductItemDao.getById(storeProductItemId);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_PRODUCT_ITEM_NOT_FOUND");
        }
        storeProductItemDao.delete(storeProductItemId);
    }
}
