package com.app.security.service.impl;

import com.app.security.dao.StoreCheckoutDao;
import com.app.security.dao.StoreCheckoutItemDao;
import com.app.security.dao.StoreProductItemDao;
import com.app.security.dao.StoreShiftDao;
import com.app.security.dto.StoreCheckout.StoreCheckoutCreateRequest;
import com.app.security.enums.ShiftStatus;
import com.app.security.model.StoreProductItem;
import com.app.security.model.StoreShift;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoreCheckoutServiceImplTest {

    private static final String STORE_ID = "store-1";
    private static final String SHIFT_ID = "shift-1";
    private static final String MEMBER_ID = "member-1";
    private static final String CHECKOUT_ID = "checkout-1";
    private static final String PRODUCT_A = "product-a";
    private static final String PRODUCT_B = "product-b";

    @Mock
    private StoreCheckoutDao storeCheckoutDao;

    @Mock
    private StoreCheckoutItemDao storeCheckoutItemDao;

    @Mock
    private StoreShiftDao storeShiftDao;

    @Mock
    private StoreProductItemDao storeProductItemDao;

    @InjectMocks
    private StoreCheckoutServiceImpl storeCheckoutService;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    private void setAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("email", MEMBER_ID, Collections.emptyList()));
    }

    private StoreShift openShift() {
        StoreShift shift = new StoreShift();
        shift.setStoreShiftId(SHIFT_ID);
        shift.setStoreId(STORE_ID);
        shift.setStatus(ShiftStatus.OPEN);
        return shift;
    }

    private StoreProductItem product(String id, String price) {
        StoreProductItem p = new StoreProductItem();
        p.setStoreProductItemId(id);
        p.setCurrentPrice(new BigDecimal(price));
        return p;
    }

    private StoreCheckoutCreateRequest.CheckoutItemLine line(String productId, int quantity) {
        StoreCheckoutCreateRequest.CheckoutItemLine line = new StoreCheckoutCreateRequest.CheckoutItemLine();
        line.setStoreProductItemId(productId);
        line.setQuantity(quantity);
        return line;
    }

    /**
     * 驗證：建立結帳單時，會在同一個流程內先寫入 store_checkout，再依序寫入每一筆 checkout_item，
     * 且每筆 item 的 unitPrice 採後端查到的 currentPrice（不信任前端），數量取自 request。
     */
    @Test
    @DisplayName("create: 建立 checkout 後依序寫入所有 checkout_item")
    public void create_writesCheckoutThenItemsInOrder() {
        setAuthentication();
        when(storeShiftDao.getById(SHIFT_ID)).thenReturn(openShift());
        when(storeProductItemDao.getById(PRODUCT_A)).thenReturn(product(PRODUCT_A, "100.00"));
        when(storeProductItemDao.getById(PRODUCT_B)).thenReturn(product(PRODUCT_B, "55.50"));
        when(storeCheckoutDao.create(STORE_ID, SHIFT_ID, MEMBER_ID)).thenReturn(CHECKOUT_ID);

        List<StoreCheckoutCreateRequest.CheckoutItemLine> items = List.of(
                line(PRODUCT_A, 2),
                line(PRODUCT_B, 1)
        );

        String result = storeCheckoutService.create(STORE_ID, SHIFT_ID, items);

        assertEquals(CHECKOUT_ID, result);

        InOrder inOrder = inOrder(storeCheckoutDao, storeCheckoutItemDao);
        inOrder.verify(storeCheckoutDao).create(STORE_ID, SHIFT_ID, MEMBER_ID);
        inOrder.verify(storeCheckoutItemDao).create(CHECKOUT_ID, PRODUCT_A, 2, new BigDecimal("100.00"));
        inOrder.verify(storeCheckoutItemDao).create(CHECKOUT_ID, PRODUCT_B, 1, new BigDecimal("55.50"));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * 驗證：items 中只要有任一個 storeProductItemId 查不到，整筆 create 應丟 404
     * STORE_PRODUCT_ITEM_NOT_FOUND，且 store_checkout / store_checkout_item 都不應寫入
     * （由 @Transactional 保證真實 DB rollback；這裡驗證 service 在發現失敗後不會再呼叫 dao.create）。
     */
    @Test
    @DisplayName("create: 任一 item id 錯誤應丟 404 且不建立 checkout")
    public void create_invalidItemId_rollsBack() {
        setAuthentication();
        when(storeShiftDao.getById(SHIFT_ID)).thenReturn(openShift());
        when(storeProductItemDao.getById(PRODUCT_A)).thenReturn(product(PRODUCT_A, "100.00"));
        when(storeProductItemDao.getById(PRODUCT_B)).thenReturn(null);

        List<StoreCheckoutCreateRequest.CheckoutItemLine> items = List.of(
                line(PRODUCT_A, 2),
                line(PRODUCT_B, 1)
        );

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> storeCheckoutService.create(STORE_ID, SHIFT_ID, items)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("STORE_PRODUCT_ITEM_NOT_FOUND", ex.getReason());
        verify(storeCheckoutDao, never()).create(anyString(), anyString(), anyString());
        verify(storeCheckoutItemDao, never()).create(anyString(), anyString(), anyInt(), any());
    }
}
