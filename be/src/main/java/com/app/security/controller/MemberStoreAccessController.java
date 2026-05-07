package com.app.security.controller;

import com.app.security.dto.MemberStoreAccess.MemberStoreAccessCreateRequest;
import com.app.security.dto.MemberStoreAccess.MemberStoreAccessUpdateRequest;
import com.app.security.dto.MemberStoreAccess.StoreMemberAccessItem;
import com.app.security.dto.Response;
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

    /**
     * 指派 member 到某個 store，並給予門市層級角色（store_manager / store_staff）。
     * 用於門市新增員工、加入店長時。
     */
    @PostMapping("/")
    public Response<Void> create(@Valid @RequestBody MemberStoreAccessCreateRequest request) {
        memberStoreAccessService.create(request.getMemberId(), request.getStoreId(), request.getRole());
        return new Response<>("Create Member Store Access", null, HttpStatus.CREATED);
    }

    /**
     * 取得指定門市目前所有的成員授權清單（含角色與啟用狀態）。
     * 用於門市後台檢視員工名單、權限管理。
     */
    @GetMapping("/{storeId}")
    public Response<List<StoreMemberAccessItem>> getAccessByStoreId(@PathVariable String storeId) {
        List<StoreMemberAccessItem> list = memberStoreAccessService.getByStoreId(storeId);
        return new Response<>("Get Member Store Access", list, HttpStatus.OK);
    }

    /**
     * 更新成員在某門市的角色或啟用狀態。
     * 用於員工升店長、停權離職員工。
     */
    @PatchMapping("/{memberStoreAccessId}")
    public Response<Void> update(@PathVariable String memberStoreAccessId,
                                 @Valid @RequestBody MemberStoreAccessUpdateRequest request) {
        System.out.println("memberStoreAccessId" + memberStoreAccessId);
        memberStoreAccessService.update(memberStoreAccessId, request.getRole(), request.getIsActive());
        return new Response<>("Update Member Store Access", null, HttpStatus.OK);
    }
}
