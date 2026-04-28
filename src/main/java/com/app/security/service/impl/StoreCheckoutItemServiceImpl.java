package com.app.security.service.impl;

import com.app.security.dao.StoreCheckoutDao;
import com.app.security.dao.StoreCheckoutItemDao;
import com.app.security.model.StoreCheckoutItem;
import com.app.security.service.StoreCheckoutItemService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Component
public class StoreCheckoutItemServiceImpl implements StoreCheckoutItemService {

    private final StoreCheckoutItemDao storeCheckoutItemDao;

    private final StoreCheckoutDao storeCheckoutDao;

    public StoreCheckoutItemServiceImpl(StoreCheckoutItemDao storeCheckoutItemDao,
                                        StoreCheckoutDao storeCheckoutDao) {
        this.storeCheckoutItemDao = storeCheckoutItemDao;
        this.storeCheckoutDao = storeCheckoutDao;
    }

    @Override
    public List<StoreCheckoutItem> getAllByCheckoutId(String storeCheckoutId) {
        return storeCheckoutItemDao.getAllByCheckoutId(storeCheckoutId);
    }

    @Override
    public StoreCheckoutItem getById(String storeCheckoutItemId) {
        StoreCheckoutItem item = storeCheckoutItemDao.getById(storeCheckoutItemId);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_CHECKOUT_ITEM_NOT_FOUND");
        }
        return item;
    }

    @Override
    public String create(String storeCheckoutId, String storeProductItemId, Integer quantity, BigDecimal unitPrice) {
        if (storeCheckoutDao.getById(storeCheckoutId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_CHECKOUT_NOT_FOUND");
        }
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        return storeCheckoutItemDao.create(storeCheckoutId, storeProductItemId, quantity, unitPrice, subtotal);
    }

    @Override
    public void delete(String storeCheckoutItemId) {
        if (storeCheckoutItemDao.getById(storeCheckoutItemId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_CHECKOUT_ITEM_NOT_FOUND");
        }
        storeCheckoutItemDao.delete(storeCheckoutItemId);
    }
}
