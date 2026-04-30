package com.app.security.service.impl;

import com.app.security.dao.StoreCheckoutDao;
import com.app.security.dao.StoreCheckoutItemDao;
import com.app.security.dao.StoreProductItemDao;
import com.app.security.dao.StoreShiftDao;
import com.app.security.dto.StoreCheckout.StoreCheckoutCreateRequest;
import com.app.security.enums.OrderStatus;
import com.app.security.enums.ShiftStatus;
import com.app.security.model.StoreCheckout;
import com.app.security.model.StoreProductItem;
import com.app.security.model.StoreShift;
import com.app.security.service.StoreCheckoutService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Component
public class StoreCheckoutServiceImpl implements StoreCheckoutService {

    private final StoreCheckoutDao storeCheckoutDao;

    private final StoreCheckoutItemDao storeCheckoutItemDao;

    private final StoreShiftDao storeShiftDao;

    private final StoreProductItemDao storeProductItemDao;

    public StoreCheckoutServiceImpl(StoreCheckoutDao storeCheckoutDao,
                                    StoreCheckoutItemDao storeCheckoutItemDao,
                                    StoreShiftDao storeShiftDao,
                                    StoreProductItemDao storeProductItemDao) {
        this.storeCheckoutDao = storeCheckoutDao;
        this.storeCheckoutItemDao = storeCheckoutItemDao;
        this.storeShiftDao = storeShiftDao;
        this.storeProductItemDao = storeProductItemDao;
    }

    @Override
    public List<StoreCheckout> getAllByStoreId(String storeId) {
        return storeCheckoutDao.getAllByStoreId(storeId);
    }

    @Override
    public List<StoreCheckout> getAllByShiftId(String storeShiftId) {
        return storeCheckoutDao.getAllByShiftId(storeShiftId);
    }

    @Override
    public StoreCheckout getById(String storeCheckoutId) {
        StoreCheckout checkout = storeCheckoutDao.getById(storeCheckoutId);
        if (checkout == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_CHECKOUT_NOT_FOUND");
        }
        return checkout;
    }

    @Override
    @Transactional
    public String create(String storeId, String storeShiftId, List<StoreCheckoutCreateRequest.CheckoutItemLine> items) {
        StoreShift shift = storeShiftDao.getById(storeShiftId);

        if (shift == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_SHIFT_NOT_FOUND");
        }

        if (!shift.getStoreId().equals(storeId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SHIFT_STORE_MISMATCH");
        }

        if (shift.getStatus() != ShiftStatus.OPEN) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SHIFT_NOT_OPEN");
        }

        BigDecimal settlePrice = BigDecimal.ZERO;
        StoreProductItem[] products = new StoreProductItem[items.size()];
        BigDecimal[] subtotals = new BigDecimal[items.size()];

        for (int i = 0; i < items.size(); i++) {
            StoreCheckoutCreateRequest.CheckoutItemLine line = items.get(i);
            StoreProductItem product = storeProductItemDao.getById(line.getStoreProductItemId());
            if (product == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_PRODUCT_ITEM_NOT_FOUND");
            }
            BigDecimal subtotal = product.getCurrentPrice().multiply(BigDecimal.valueOf(line.getQuantity()));
            products[i] = product;
            subtotals[i] = subtotal;
            settlePrice = settlePrice.add(subtotal);
        }

        String memberId = currentMemberId();
        String storeCheckoutId = storeCheckoutDao.create(storeId, storeShiftId, memberId, settlePrice);

        for (int i = 0; i < items.size(); i++) {
            StoreCheckoutCreateRequest.CheckoutItemLine line = items.get(i);
            storeCheckoutItemDao.create(
                    storeCheckoutId,
                    products[i].getStoreProductItemId(),
                    line.getQuantity(),
                    products[i].getCurrentPrice(),
                    subtotals[i]);
        }

        return storeCheckoutId;
    }

    @Override
    public void cancel(String storeCheckoutId) {
        StoreCheckout checkout = storeCheckoutDao.getById(storeCheckoutId);
        if (checkout == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_CHECKOUT_NOT_FOUND");
        }
        if (checkout.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CHECKOUT_ALREADY_CANCELLED");
        }
        storeCheckoutDao.cancel(storeCheckoutId);
    }

    private String currentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getCredentials();
    }
}
