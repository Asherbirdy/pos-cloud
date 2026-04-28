package com.app.security.controller;

import com.app.security.dto.ProductCategory.ProductCategoryCreateRequest;
import com.app.security.dto.ProductCategory.ProductCategoryUpdateRequest;
import com.app.security.dto.Response;
import com.app.security.model.ProductCategory;
import com.app.security.service.ProductCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enterprise/{enterpriseId}/store/{storeId}/product-category")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("/")
    public Response<List<ProductCategory>> getAll(@PathVariable String storeId) {
        List<ProductCategory> list = productCategoryService.getAllByStoreId(storeId);
        return new Response<>("Success", list, HttpStatus.OK);
    }

    @GetMapping("/{productCategoryId}")
    public Response<ProductCategory> getById(@PathVariable String productCategoryId) {
        ProductCategory productCategory = productCategoryService.getById(productCategoryId);
        return new Response<>("Success", productCategory, HttpStatus.OK);
    }

    @PostMapping("/")
    public Response<Void> create(@PathVariable String storeId,
                                 @Valid @RequestBody ProductCategoryCreateRequest request) {
        productCategoryService.create(storeId, request.getName());
        return new Response<>("ProductCategory Create", null, HttpStatus.CREATED);
    }

    @PatchMapping("/{productCategoryId}")
    public Response<Void> update(@PathVariable String productCategoryId,
                                 @Valid @RequestBody ProductCategoryUpdateRequest request) {
        productCategoryService.update(productCategoryId, request.getName());
        return new Response<>("ProductCategory Update", null, HttpStatus.OK);
    }

    @DeleteMapping("/{productCategoryId}")
    public Response<Void> delete(@PathVariable String productCategoryId) {
        productCategoryService.delete(productCategoryId);
        return new Response<>("ProductCategory Delete", null, HttpStatus.OK);
    }
}
