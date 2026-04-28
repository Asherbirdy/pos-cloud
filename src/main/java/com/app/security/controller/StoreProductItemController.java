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

    @GetMapping("/")
    public Response<List<StoreProductItem>> getAll(@PathVariable String productCategoryId) {
        List<StoreProductItem> list = storeProductItemService.getAllByCategoryId(productCategoryId);
        return new Response<>("Success", list, HttpStatus.OK);
    }

    @GetMapping("/{storeProductItemId}")
    public Response<StoreProductItem> getById(@PathVariable String storeProductItemId) {
        StoreProductItem item = storeProductItemService.getById(storeProductItemId);
        return new Response<>("Success", item, HttpStatus.OK);
    }

    @PostMapping("/")
    public Response<Void> create(@PathVariable String productCategoryId,
                                 @Valid @RequestBody StoreProductItemCreateRequest request) {
        storeProductItemService.create(productCategoryId, request.getName(), request.getCurrentPrice());
        return new Response<>("StoreProductItem Create", null, HttpStatus.CREATED);
    }

    @PatchMapping("/{storeProductItemId}")
    public Response<Void> update(@PathVariable String storeProductItemId,
                                 @Valid @RequestBody StoreProductItemUpdateRequest request) {
        storeProductItemService.update(storeProductItemId, request.getName(), request.getCurrentPrice());
        return new Response<>("StoreProductItem Update", null, HttpStatus.OK);
    }

    @DeleteMapping("/{storeProductItemId}")
    public Response<Void> delete(@PathVariable String storeProductItemId) {
        storeProductItemService.delete(storeProductItemId);
        return new Response<>("StoreProductItem Delete", null, HttpStatus.OK);
    }
}
