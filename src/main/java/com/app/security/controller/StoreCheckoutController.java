package com.app.security.controller;

import com.app.security.dto.Response;
import com.app.security.dto.StoreCheckout.StoreCheckoutCreateRequest;
import com.app.security.model.StoreCheckout;
import com.app.security.service.StoreCheckoutService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store/{storeId}/checkout")
public class StoreCheckoutController {

    private final StoreCheckoutService storeCheckoutService;

    public StoreCheckoutController(StoreCheckoutService storeCheckoutService) {
        this.storeCheckoutService = storeCheckoutService;
    }

    /**
     * 取得指定 store 的所有結帳單（依結帳時間新到舊排序）。
     * 用於後台檢視銷售紀錄、日結對帳。
     */
    @GetMapping("/")
    public Response<List<StoreCheckout>> getAll(@PathVariable String storeId) {
        List<StoreCheckout> list = storeCheckoutService.getAllByStoreId(storeId);
        return new Response<>("Success", list, HttpStatus.OK);
    }

    /**
     * 依班別查詢該班期間的所有結帳單。
     * 用於關班結算、班別現金核對。
     */
    @GetMapping("/by-shift/{storeShiftId}")
    public Response<List<StoreCheckout>> getAllByShift(@PathVariable String storeShiftId) {
        List<StoreCheckout> list = storeCheckoutService.getAllByShiftId(storeShiftId);
        return new Response<>("Success", list, HttpStatus.OK);
    }

    /**
     * 查詢單筆結帳單詳細資料。
     */
    @GetMapping("/{storeCheckoutId}")
    public Response<StoreCheckout> getById(@PathVariable String storeCheckoutId) {
        StoreCheckout checkout = storeCheckoutService.getById(storeCheckoutId);
        return new Response<>("Success", checkout, HttpStatus.OK);
    }

    /**
     * 建立結帳單（出單結帳），同筆交易內一併寫入所有明細。
     * 須帶入目前 OPEN 狀態的 store_shift_id 與至少一筆 checkoutItem。
     * 每筆明細以該商品當下 currentPrice 寫入 unit_price，status 預設 COMPLETED。
     * 結帳人取自登入 member。任何明細不存在或班別異常會整筆 rollback。
     */
    @PostMapping("/")
    public Response<String> create(@PathVariable String storeId,
                                   @Valid @RequestBody StoreCheckoutCreateRequest request) {
        String storeCheckoutId = storeCheckoutService.create(storeId, request.getStoreShiftId(), request.getCheckoutItem());
        return new Response<>("StoreCheckout Create", storeCheckoutId, HttpStatus.CREATED);
    }

    /**
     * 取消結帳單：將 order_status 改為 CANCELLED。
     * 用於退單、誤結。已 CANCELLED 的單會回傳 409 CHECKOUT_ALREADY_CANCELLED。
     */
    @PostMapping("/{storeCheckoutId}/cancel")
    public Response<Void> cancel(@PathVariable String storeCheckoutId, @PathVariable String storeId) {
        storeCheckoutService.cancel(storeCheckoutId);
        return new Response<>("StoreCheckout Cancel", null, HttpStatus.OK);
    }
}
