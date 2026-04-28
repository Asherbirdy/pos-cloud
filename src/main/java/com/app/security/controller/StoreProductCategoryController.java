package com.app.security.controller;

import com.app.security.dto.Response;
import com.app.security.dto.StoreProductCategory.StoreProductCategoryCreateRequest;
import com.app.security.dto.StoreProductCategory.StoreProductCategoryUpdateRequest;
import com.app.security.model.StoreProductCategory;
import com.app.security.service.StoreProductCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enterprise/{enterpriseId}/store/{storeId}/product-category")
public class StoreProductCategoryController {

    private final StoreProductCategoryService storeProductCategoryService;

    public StoreProductCategoryController(StoreProductCategoryService storeProductCategoryService) {
        this.storeProductCategoryService = storeProductCategoryService;
    }

    @GetMapping("/")
    public Response<List<StoreProductCategory>> getAll(@PathVariable String storeId) {
        List<StoreProductCategory> list = storeProductCategoryService.getAllByStoreId(storeId);
        return new Response<>("Success", list, HttpStatus.OK);
    }

    @GetMapping("/{productCategoryId}")
    public Response<StoreProductCategory> getById(@PathVariable String productCategoryId) {
        StoreProductCategory storeProductCategory = storeProductCategoryService.getById(productCategoryId);
        return new Response<>("Success", storeProductCategory, HttpStatus.OK);
    }

    @PostMapping("/")
    public Response<Void> create(@PathVariable String storeId,
                                 @Valid @RequestBody StoreProductCategoryCreateRequest request) {
        storeProductCategoryService.create(storeId, request.getName());
        return new Response<>("StoreProductCategory Create", null, HttpStatus.CREATED);
    }

    @PatchMapping("/{productCategoryId}")
    public Response<Void> update(@PathVariable String productCategoryId,
                                 @Valid @RequestBody StoreProductCategoryUpdateRequest request) {
        storeProductCategoryService.update(productCategoryId, request.getName());
        return new Response<>("StoreProductCategory Update", null, HttpStatus.OK);
    }

    @DeleteMapping("/{productCategoryId}")
    public Response<Void> delete(@PathVariable String productCategoryId) {
        storeProductCategoryService.delete(productCategoryId);
        return new Response<>("StoreProductCategory Delete", null, HttpStatus.OK);
    }
}
