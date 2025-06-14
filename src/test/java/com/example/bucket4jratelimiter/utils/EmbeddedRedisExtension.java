package com.example.bucket4jratelimiter.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.embedded.RedisServer;

@Slf4j
public class EmbeddedRedisExtension implements BeforeAllCallback, AfterAllCallback {
    private RedisServer redisServer;

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        redisServer.stop();
        log.info("Embedded Redis Server stopped");
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        var appContext = SpringExtension.getApplicationContext(extensionContext);
        redisServer = appContext.getBean(RedisServer.class);

        if (!redisServer.isActive()) {
            redisServer.start();
        }

        log.info("Embedded Redis Server started: {}", redisServer.isActive());
    }
}