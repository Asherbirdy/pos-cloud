package com.app.security.controller;

import com.app.security.dto.Response;
import com.app.security.dto.Store.StoreCreateRequest;
import com.app.security.dto.Store.StoreEditRequest;
import com.app.security.model.Store;
import com.app.security.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enterprise/{enterpriseId}/store")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    /**
     * 取得指定企業底下的所有門市。
     * 用於後台 / 前台切換門市時的清單顯示。
     */
    @GetMapping("/")
    public Response<List<Store>> getAll(@PathVariable String enterpriseId) {
        List<Store> stores = storeService.getAllByEnterpriseId(enterpriseId);
        return new Response<>("Success", stores, HttpStatus.OK);
    }

    /**
     * 在指定企業下建立新門市。
     * 用於企業展店、新增分店時。
     */
    @PostMapping("/")
    public Response<Void> create(@PathVariable String enterpriseId,
                                 @Valid @RequestBody StoreCreateRequest request) {
        storeService.create(enterpriseId, request.getName());
        return new Response<>("Store Create", null, HttpStatus.CREATED);
    }

    /**
     * 編輯門市資料（名稱、啟用狀態、同時可開班的裝置上限）。
     * 用於門市改名、暫停營運、調整可同時開班的 POS 數。
     */
    @PatchMapping("/{store_id}")
    public Response<Void> edit(@PathVariable String store_id,
                               @Valid @RequestBody StoreEditRequest request,
                               @PathVariable String enterpriseId) {
        storeService.edit(store_id, request.getName(), request.getIsActive(), request.getRunning_devices_limit());
        return new Response<>("Store Edit", null, HttpStatus.OK);
    }
}
