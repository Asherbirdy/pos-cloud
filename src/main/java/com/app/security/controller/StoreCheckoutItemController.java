package com.app.security.controller;

import com.app.security.dto.Response;
import com.app.security.dto.StoreCheckoutItem.StoreCheckoutItemCreateRequest;
import com.app.security.model.StoreCheckoutItem;
import com.app.security.service.StoreCheckoutItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkout/{storeCheckoutId}/item")
public class StoreCheckoutItemController {

    private final StoreCheckoutItemService storeCheckoutItemService;

    public StoreCheckoutItemController(StoreCheckoutItemService storeCheckoutItemService) {
        this.storeCheckoutItemService = storeCheckoutItemService;
    }

    /**
     * 取得指定結帳單下的所有商品明細（依加入順序）。
     * 用於顯示訂單品項、列印發票明細。
     */
    @GetMapping("/")
    public Response<List<StoreCheckoutItem>> getAll(@PathVariable String storeCheckoutId) {
        List<StoreCheckoutItem> list = storeCheckoutItemService.getAllByCheckoutId(storeCheckoutId);
        return new Response<>("Success", list, HttpStatus.OK);
    }

    /**
     * 查詢單筆結帳明細。
     */
    @GetMapping("/{storeCheckoutItemId}")
    public Response<StoreCheckoutItem> getById(@PathVariable String storeCheckoutItemId) {
        StoreCheckoutItem item = storeCheckoutItemService.getById(storeCheckoutItemId);
        return new Response<>("Success", item, HttpStatus.OK);
    }

    /**
     * 新增一筆結帳明細到指定結帳單。
     * unitPrice 為當下交易單價（保留歷史價格），subtotal 由 service 以 unitPrice * quantity 計算。
     * 若結帳單不存在回傳 404 STORE_CHECKOUT_NOT_FOUND。
     */
    @PostMapping("/")
    public Response<String> create(@PathVariable String storeCheckoutId,
                                   @Valid @RequestBody StoreCheckoutItemCreateRequest request) {
        String id = storeCheckoutItemService.create(
                storeCheckoutId,
                request.getStoreProductItemId(),
                request.getQuantity(),
                request.getUnitPrice());
        return new Response<>("StoreCheckoutItem Create", id, HttpStatus.CREATED);
    }

    /**
     * 刪除一筆結帳明細（誤打、退單前的品項調整）。
     */
    @DeleteMapping("/{storeCheckoutItemId}")
    public Response<Void> delete(@PathVariable String storeCheckoutItemId) {
        storeCheckoutItemService.delete(storeCheckoutItemId);
        return new Response<>("StoreCheckoutItem Delete", null, HttpStatus.OK);
    }
}
