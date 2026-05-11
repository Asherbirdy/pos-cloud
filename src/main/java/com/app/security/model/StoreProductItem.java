package com.app.security.model;

import java.math.BigDecimal;
import java.util.Date;

public class StoreProductItem {
    private String storeProductItemId;
    private String storeProductCategoryId;
    private String name;
    private BigDecimal currentPrice;
    private Date createdAt;
    private Date updatedAt;

    public String getStoreProductItemId() {
        return storeProductItemId;
    }

    public void setStoreProductItemId(String storeProductItemId) {
        this.storeProductItemId = storeProductItemId;
    }

    public String getStoreProductCategoryId() {
        return storeProductCategoryId;
    }

    public void setStoreProductCategoryId(String storeProductCategoryId) {
        this.storeProductCategoryId = storeProductCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
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
