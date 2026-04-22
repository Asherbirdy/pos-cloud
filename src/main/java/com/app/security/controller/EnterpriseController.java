package com.app.security.controller;

import com.app.security.dto.Enterprise.EnterpriseCreateRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {

    @GetMapping("/")
    public ResponseEntity<String> getAll() {
        return ResponseEntity.ok("Enterprise GetAll");
    }


    @PostMapping("/")
    public ResponseEntity<Map<Object,String>> create(@Valid @RequestBody EnterpriseCreateRequest request) {
        System.out.println(request.getName());
        return ResponseEntity.ok(Map.of("message", "Enterprise Create"));
    }



    @PatchMapping("/")
    public ResponseEntity<String> edit() {
        return ResponseEntity.ok("Enterprise Edit");
    }



}
