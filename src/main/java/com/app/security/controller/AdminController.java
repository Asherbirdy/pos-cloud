package com.app.security.controller;

import com.app.security.dto.Admin.AdminCreateMemberRequest;
import com.app.security.dto.Admin.AdminMemberItem;
import com.app.security.dto.Response;
import com.app.security.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * 取得所有會員清單。
     * 用於 admin 後台管理使用者帳號。
     */
    @GetMapping("/member/")
    public Response<List<AdminMemberItem>> getAllMembers() {
        List<AdminMemberItem> members = adminService.getAllMembers();
        return new Response<>("Success", members, HttpStatus.OK);
    }

    /**
     * 由 admin 直接建立會員帳號（含 user / admin 角色）。
     * 用於後台批次建立帳號，不走自助註冊流程。
     */
    @PostMapping("/member/")
    public Response<Void> createMember(@Valid @RequestBody AdminCreateMemberRequest request) {
        adminService.createMember(request);
        return new Response<>("Member Create", null, HttpStatus.CREATED);
    }
}
