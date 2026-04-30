package com.app.security.controller;

import com.app.security.dto.Response;
import com.app.security.dto.StoreProductItem.StoreProductItemCreateRequest;
import com.app.security.dto.StoreProductItem.StoreProductItemUpdateRequest;
import com.app.security.model.StoreProductItem;
import com.app.security.service.StoreProductItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-item")
public class StoreProductItemController {

    private final StoreProductItemService storeProductItemService;

    public StoreProductItemController(StoreProductItemService storeProductItemService) {
        this.storeProductItemService = storeProductItemService;
    }

    /**
     * 取得指定分類下的所有商品。
     * 用於 POS 點餐畫面顯示商品列、後台商品管理。
     */
    @GetMapping("/")
    public Response<List<StoreProductItem>> getAll(@PathVariable String productCategoryId) {
        List<StoreProductItem> list = storeProductItemService.getAllByCategoryId(productCategoryId);
        return new Response<>("Success", list, HttpStatus.OK);
    }

    /**
     * 查詢單一商品詳細資料。
     */
    @GetMapping("/{storeProductItemId}")
    public Response<StoreProductItem> getById(@PathVariable String storeProductItemId) {
        StoreProductItem item = storeProductItemService.getById(storeProductItemId);
        return new Response<>("Success", item, HttpStatus.OK);
    }

    /**
     * 在指定分類下新增商品（含目前售價）。
     * 用於後台建立菜單品項。
     */
    @PostMapping("/")
    public Response<Void> create(@PathVariable String productCategoryId,
                                 @Valid @RequestBody StoreProductItemCreateRequest request) {
        storeProductItemService.create(productCategoryId, request.getName(), request.getCurrentPrice());
        return new Response<>("StoreProductItem Create", null, HttpStatus.CREATED);
    }

    /**
     * 更新商品資料（名稱、目前售價）。
     * 用於改名、調整售價；歷史結帳單會保留當下單價，不受影響。
     */
    @PatchMapping("/{storeProductItemId}")
    public Response<Void> update(@PathVariable String storeProductItemId,
                                 @Valid @RequestBody StoreProductItemUpdateRequest request) {
        storeProductItemService.update(storeProductItemId, request.getName(), request.getCurrentPrice());
        return new Response<>("StoreProductItem Update", null, HttpStatus.OK);
    }

    /**
     * 刪除商品。
     * 用於下架不再販售的商品。
     */
    @DeleteMapping("/{storeProductItemId}")
    public Response<Void> delete(@PathVariable String storeProductItemId) {
        storeProductItemService.delete(storeProductItemId);
        return new Response<>("StoreProductItem Delete", null, HttpStatus.OK);
    }
}
