package com.example.bucket4jratelimiter.controllers;

import com.example.bucket4jratelimiter.services.CustomService;
import com.example.bucket4jratelimiter.model.ClientRequest;
import com.example.bucket4jratelimiter.model.Constants;
import com.example.bucket4jratelimiter.components.TokenBucketComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/custom")
@RequiredArgsConstructor
public class CustomController {
    private final TokenBucketComponent tokenBucketComponent;
    private final CustomService customService;

    @PostMapping("/{id}")
    public ResponseEntity<?> processCustomRequest(
            @RequestHeader(value = Constants.CLIENT_HEADER) String apiClient,
            @PathVariable String id,
            @RequestBody String value) {

        if (tokenBucketComponent.tryConsumeToken(apiClient)) {
            customService.doSomething(new ClientRequest(id, value));
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
    }
}
