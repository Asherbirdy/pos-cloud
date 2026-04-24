package com.app.security.controller;

import com.app.security.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member-store-access")
public class MemberStoreAccessController {

    @PostMapping("/")
    public Response<String> create() {
        return new Response<>("Create Member Store Access", null, HttpStatus.CREATED);
    }

    @GetMapping("/{storeId}")
    public Response<String> getAccessByStoreId(@PathVariable String storeId) {
        return new Response<>("Get Member Store Access", null, HttpStatus.OK);
    }

    @PatchMapping("/{memberStoreAccessId}")
    public Response<String> update(@PathVariable String memberStoreAccessId) {
        return new Response<>("Update Member Store Access", null, HttpStatus.OK);
    }
}
