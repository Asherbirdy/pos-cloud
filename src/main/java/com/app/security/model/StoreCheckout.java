package com.app.security.model;

import com.app.security.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;

public class StoreCheckout {
    private String storeCheckoutId;
    private String storeId;
    private String storeShiftId;
    private String memberId;
    private BigDecimal settlePrice;
    private OrderStatus orderStatus;
    private Date checkoutAt;
    private Date createdAt;
    private Date updatedAt;

    public String getStoreCheckoutId() {
        return storeCheckoutId;
    }

    public void setStoreCheckoutId(String storeCheckoutId) {
        this.storeCheckoutId = storeCheckoutId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreShiftId() {
        return storeShiftId;
    }

    public void setStoreShiftId(String storeShiftId) {
        this.storeShiftId = storeShiftId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public BigDecimal getSettlePrice() {
        return settlePrice;
    }

    public void setSettlePrice(BigDecimal settlePrice) {
        this.settlePrice = settlePrice;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getCheckoutAt() {
        return checkoutAt;
    }

    public void setCheckoutAt(Date checkoutAt) {
        this.checkoutAt = checkoutAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
