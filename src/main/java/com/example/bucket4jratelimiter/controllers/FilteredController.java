package com.example.bucket4jratelimiter.controllers;

import com.example.bucket4jratelimiter.services.CustomService;
import com.example.bucket4jratelimiter.model.ClientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/filtered")
@RequiredArgsConstructor
public class FilteredController {
    private final CustomService customService;

    @PostMapping("/{id}")
    public ResponseEntity<?> processCustomRequest(
            @PathVariable String id,
            @RequestBody String value) {

        customService.doSomething(new ClientRequest(id, value));
        return ResponseEntity.ok().build();
    }
}
