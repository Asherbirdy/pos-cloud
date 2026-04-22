package com.app.security.controller;

import com.app.security.dto.Enterprise.EnterpriseCreateRequest;
import com.app.security.service.EnterpriseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    public EnterpriseController(EnterpriseService enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    @GetMapping("/")
    public ResponseEntity<String> getAll() {
        enterpriseService.getAll();
        return ResponseEntity.ok("Enterprise GetAll");
    }


    @PostMapping("/")
    public ResponseEntity<Map<Object,String>> create(@Valid @RequestBody EnterpriseCreateRequest request) {
        System.out.println(request.getName());

        enterpriseService.create("asdasdas");
        return ResponseEntity.ok(Map.of("message", "Enterprise Create"));
    }



    @PatchMapping("/")
    public ResponseEntity<String> edit() {
        enterpriseService.edit("asdasd");
        return ResponseEntity.ok("Enterprise Edit");
    }



}
