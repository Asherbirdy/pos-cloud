package com.app.security.dto.StoreCheckoutItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class StoreCheckoutItemCreateRequest {

    @NotBlank
    private String storeProductItemId;

    @NotNull
    @Positive
    private Integer quantity;

    @NotNull
    @PositiveOrZero
    private BigDecimal unitPrice;

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
}
