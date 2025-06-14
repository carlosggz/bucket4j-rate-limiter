package com.example.bucket4jratelimiter.services;

import com.example.bucket4jratelimiter.model.ClientRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomService {

    public void doSomething(ClientRequest clientRequest) {
        log.info("Processing request with id {}", clientRequest.id());
    }
}
