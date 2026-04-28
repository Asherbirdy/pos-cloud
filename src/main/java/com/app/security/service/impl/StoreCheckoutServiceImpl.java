package com.app.security.service.impl;

import com.app.security.dao.StoreCheckoutDao;
import com.app.security.dao.StoreShiftDao;
import com.app.security.enums.OrderStatus;
import com.app.security.enums.ShiftStatus;
import com.app.security.model.StoreCheckout;
import com.app.security.model.StoreShift;
import com.app.security.service.StoreCheckoutService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Component
public class StoreCheckoutServiceImpl implements StoreCheckoutService {

    private final StoreCheckoutDao storeCheckoutDao;

    private final StoreShiftDao storeShiftDao;

    public StoreCheckoutServiceImpl(StoreCheckoutDao storeCheckoutDao, StoreShiftDao storeShiftDao) {
        this.storeCheckoutDao = storeCheckoutDao;
        this.storeShiftDao = storeShiftDao;
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
    public String create(String storeId, String storeShiftId, BigDecimal settlePrice) {
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

        String memberId = currentMemberId();
        return storeCheckoutDao.create(storeId, storeShiftId, memberId, settlePrice);
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
