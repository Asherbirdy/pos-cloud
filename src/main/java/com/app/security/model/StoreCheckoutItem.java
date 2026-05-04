package com.app.security.model;

import com.app.security.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;

public class StoreCheckoutItem {
    private String storeCheckoutItemId;
    private String storeCheckoutId;
    private String storeProductItemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private OrderStatus status;
    private Date createdAt;

    public String getStoreCheckoutItemId() {
        return storeCheckoutItemId;
    }

    public void setStoreCheckoutItemId(String storeCheckoutItemId) {
        this.storeCheckoutItemId = storeCheckoutItemId;
    }

    public String getStoreCheckoutId() {
        return storeCheckoutId;
    }

    public void setStoreCheckoutId(String storeCheckoutId) {
        this.storeCheckoutId = storeCheckoutId;
    }

    public String getStoreProductItemId() {
        return storeProductItemId;
    }

    public void setStoreProductItemId(String storeProductItemId) {
        this.storeProductItemId = storeProductItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
