package com.app.security.controller;

import com.app.security.dto.Enterprise.EnterpriseCreateRequest;
import com.app.security.dto.Response;
import com.app.security.model.Enterprise;
import com.app.security.service.EnterpriseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    public EnterpriseController(EnterpriseService enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    @GetMapping("/")
    public Response<List<Enterprise>> getAll() {
        List<Enterprise> enterprises = enterpriseService.getAll();
        return new Response<>("Success", enterprises, HttpStatus.OK);
    }

    @PostMapping("/")
    public Response<Void> create(@Valid @RequestBody EnterpriseCreateRequest request) {
        enterpriseService.create(request.getName());
        return new Response<>("Enterprise Create", null, HttpStatus.CREATED);
    }

    @PatchMapping("/{enterpriseId}")
    public Response<Void> edit(
            @PathVariable String enterpriseId,
            @Valid @RequestBody EnterpriseCreateRequest request) {
        enterpriseService.edit(enterpriseId, request.getName());
        return new Response<>("Enterprise Edit", null, HttpStatus.OK);
    }
}
