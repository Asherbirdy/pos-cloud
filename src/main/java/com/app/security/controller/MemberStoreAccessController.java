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

    @GetMapping("/{id}")
    public Response<String> getById(@PathVariable String id) {
        return new Response<>("Get Member Store Access", null, HttpStatus.OK);
    }

    @GetMapping("/member/{memberId}")
    public Response<String> getByMemberId(@PathVariable String memberId) {
        return new Response<>("Get Member Store Access By MemberId", null, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public Response<String> update(@PathVariable String id) {
        return new Response<>("Update Member Store Access", null, HttpStatus.OK);
    }
}
