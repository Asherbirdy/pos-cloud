package com.app.security.controller;

import com.app.security.aspect.RequireStoreRole;
import com.app.security.dto.Response;
import com.app.security.dto.StoreProductCategory.StoreProductCategoryCreateRequest;
import com.app.security.dto.StoreProductCategory.StoreProductCategoryUpdateRequest;
import com.app.security.enums.StoreRole;
import com.app.security.model.StoreProductCategory;
import com.app.security.service.StoreProductCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store/{storeId}/product-category")
public class StoreProductCategoryController {

    private final StoreProductCategoryService storeProductCategoryService;

    public StoreProductCategoryController(StoreProductCategoryService storeProductCategoryService) {
        this.storeProductCategoryService = storeProductCategoryService;
    }

    /**
     * 取得指定門市的所有商品分類。
     * 用於 POS 點餐畫面分類列、後台分類管理。
     */
    @GetMapping("/")
    @RequireStoreRole({StoreRole.STORE_MANAGER})
    public Response<List<StoreProductCategory>> getAll(@PathVariable String storeId) {
        List<StoreProductCategory> list = storeProductCategoryService.getAllByStoreId(storeId);
        return new Response<>("Success", list, HttpStatus.OK);
    }

    /**
     * 查詢單一商品分類詳細資料。
     */
    @GetMapping("/{productCategoryId}")
    @RequireStoreRole({StoreRole.STORE_MANAGER})
    public Response<StoreProductCategory> getById(@PathVariable String storeId,
                                                  @PathVariable String productCategoryId) {
        StoreProductCategory storeProductCategory = storeProductCategoryService.getById(productCategoryId);
        return new Response<>("Success", storeProductCategory, HttpStatus.OK);
    }

    /**
     * 為指定門市新增商品分類。
     * 用於後台建立菜單分類（飲料、餐點…）。
     */
    @PostMapping("/")
    @RequireStoreRole({StoreRole.STORE_MANAGER})
    public Response<Void> create(@PathVariable String storeId,
                                 @Valid @RequestBody StoreProductCategoryCreateRequest request) {
        storeProductCategoryService.create(storeId, request.getName());
        return new Response<>("StoreProductCategory Create", null, HttpStatus.CREATED);
    }

    /**
     * 更新商品分類名稱。
     * 用於後台改名分類。
     */
    @PatchMapping("/{productCategoryId}")
    @RequireStoreRole({StoreRole.STORE_MANAGER})
    public Response<Void> update(@PathVariable String storeId,
                                 @PathVariable String productCategoryId,
                                 @Valid @RequestBody StoreProductCategoryUpdateRequest request) {
        storeProductCategoryService.update(productCategoryId, request.getName());
        return new Response<>("StoreProductCategory Update", null, HttpStatus.OK);
    }

    /**
     * 刪除商品分類。
     * 用於後台清除不再使用的分類。
     */
    @DeleteMapping("/{productCategoryId}")
    @RequireStoreRole({StoreRole.STORE_MANAGER})
    public Response<Void> delete(@PathVariable String storeId,
                                 @PathVariable String productCategoryId) {
        storeProductCategoryService.delete(productCategoryId);
        return new Response<>("StoreProductCategory Delete", null, HttpStatus.OK);
    }
}
