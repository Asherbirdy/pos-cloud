package com.app.security.model;

import java.math.BigDecimal;
import java.util.Date;

public class StoreCheckoutItem {
    private String storeCheckoutItemId;
    private String storeCheckoutId;
    private String storeProductItemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
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

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
