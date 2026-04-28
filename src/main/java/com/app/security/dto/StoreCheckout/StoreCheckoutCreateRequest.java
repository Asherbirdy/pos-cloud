package com.app.security.dto.StoreCheckout;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class StoreCheckoutCreateRequest {

    @NotNull
    private String storeShiftId;

    @NotNull
    @PositiveOrZero
    private BigDecimal settlePrice;

    public String getStoreShiftId() {
        return storeShiftId;
    }

    public void setStoreShiftId(String storeShiftId) {
        this.storeShiftId = storeShiftId;
    }

    public BigDecimal getSettlePrice() {
        return settlePrice;
    }

    public void setSettlePrice(BigDecimal settlePrice) {
        this.settlePrice = settlePrice;
    }
}
