package com.example.bucket4jratelimiter.controllers;

import com.example.bucket4jratelimiter.services.CustomService;
import com.example.bucket4jratelimiter.model.ClientRequest;
import com.example.bucket4jratelimiter.model.Constants;
import com.example.bucket4jratelimiter.components.TokenBucketComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/custom")
public class CustomController {
    private final TokenBucketComponent tokenBucketComponent;
    private final CustomService customService;
    private final String instanceId;

    public CustomController(
            TokenBucketComponent tokenBucketComponent,
            CustomService customService,
            @Value("${app.instance-id}") String instanceId) {
        this.tokenBucketComponent = tokenBucketComponent;
        this.customService = customService;
        this.instanceId = instanceId;
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> processCustomRequest(
            @RequestHeader(value = Constants.CLIENT_HEADER) String apiClient,
            @PathVariable String id,
            @RequestBody String value) {

        HttpStatus status;

        if (tokenBucketComponent.tryConsumeToken(apiClient)) {
            customService.doSomething(new ClientRequest(id, value));
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.TOO_MANY_REQUESTS;
        }

        return ResponseEntity
                .status(status)
                .header(Constants.INSTANCE_HEADER, instanceId)
                .build();
    }
}
