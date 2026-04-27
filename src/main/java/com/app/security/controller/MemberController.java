package com.app.security.controller;

import com.app.security.dto.Member.MemberInfoResponse;
import com.app.security.dto.Response;
import com.app.security.service.AuthService;
import com.app.security.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    private final AuthService authService;

    public MemberController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @GetMapping("/showMe")
    public Response<MemberInfoResponse> showMe() {
        MemberInfoResponse memberInfo = memberService.showMemberInfo();
        return new Response<>("Success", memberInfo, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public Response<Void> logout() {
        authService.logout();
        return new Response<>("LOGOUT_SUCCESS", null, HttpStatus.OK);
    }
}
