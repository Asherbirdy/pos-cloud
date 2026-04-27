package com.app.security.controller;

import com.app.security.dto.MemberStoreAccess.MemberStoreAccessCreateRequest;
import com.app.security.dto.MemberStoreAccess.MemberStoreAccessUpdateRequest;
import com.app.security.dto.Response;
import com.app.security.model.MemberStoreAccess;
import com.app.security.service.MemberStoreAccessService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member-store-access")
public class MemberStoreAccessController {

    private final MemberStoreAccessService memberStoreAccessService;

    public MemberStoreAccessController(MemberStoreAccessService memberStoreAccessService) {
        this.memberStoreAccessService = memberStoreAccessService;
    }

    @PostMapping("/")
    public Response<Void> create(@Valid @RequestBody MemberStoreAccessCreateRequest request) {
        memberStoreAccessService.create(request.getMemberId(), request.getStoreId(), request.getRole());
        return new Response<>("Create Member Store Access", null, HttpStatus.CREATED);
    }

    @GetMapping("/{storeId}")
    public Response<List<MemberStoreAccess>> getAccessByStoreId(@PathVariable String storeId) {
        List<MemberStoreAccess> list = memberStoreAccessService.getByStoreId(storeId);
        return new Response<>("Get Member Store Access", list, HttpStatus.OK);
    }

    @PatchMapping("/{memberStoreAccessId}")
    public Response<Void> update(@PathVariable String memberStoreAccessId,
                                 @Valid @RequestBody MemberStoreAccessUpdateRequest request) {
        System.out.println("memberStoreAccessId" + memberStoreAccessId);
        memberStoreAccessService.update(memberStoreAccessId, request.getRole(), request.getStatus());
        return new Response<>("Update Member Store Access", null, HttpStatus.OK);
    }
}
