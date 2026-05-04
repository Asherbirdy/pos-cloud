package com.app.security.dto.StoreCheckout;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class StoreCheckoutCreateRequest {

    @NotBlank
    private String storeShiftId;

    @NotEmpty
    @Valid
    private List<CheckoutItemLine> checkoutItem;

    public String getStoreShiftId() {
        return storeShiftId;
    }

    public void setStoreShiftId(String storeShiftId) {
        this.storeShiftId = storeShiftId;
    }

    public List<CheckoutItemLine> getCheckoutItem() {
        return checkoutItem;
    }

    public void setCheckoutItem(List<CheckoutItemLine> checkoutItem) {
        this.checkoutItem = checkoutItem;
    }

    public static class CheckoutItemLine {

        @NotBlank
        private String storeProductItemId;

        @NotNull
        @Positive
        private Integer quantity;

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
    }
}
