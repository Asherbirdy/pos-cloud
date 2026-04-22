package com.app.security.controller;

import com.app.security.dto.Response;
import com.app.security.dto.Store.StoreCreateRequest;
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

    @GetMapping("/")
    public Response<List<Store>> getAll(@PathVariable String enterpriseId) {
        List<Store> stores = storeService.getAllByEnterpriseId(enterpriseId);
        return new Response<>("Success", stores, HttpStatus.OK);
    }

    @PostMapping("/")
    public Response<Void> create(@PathVariable String enterpriseId,
                                 @Valid @RequestBody StoreCreateRequest request) {
        storeService.create(enterpriseId, request.getName());
        return new Response<>("Store Create", null, HttpStatus.CREATED);
    }

    @PatchMapping("/{store_id}")
    public Response<Void> edit(@PathVariable String store_id,
                               @Valid @RequestBody StoreCreateRequest request,
                               @PathVariable String enterpriseId
    ) {
        storeService.edit(store_id, request.getName());
        return new Response<>("Store Edit", null, HttpStatus.OK);
    }
}
