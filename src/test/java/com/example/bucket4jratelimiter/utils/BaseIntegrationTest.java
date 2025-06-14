package com.example.bucket4jratelimiter.utils;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisCommandsProvider;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisAccessor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RedisTest
@AutoConfigureMockMvc
public abstract class BaseIntegrationTest {
    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    void clearResources() {
        Optional
            .of(redisTemplate)
            .map(RedisAccessor::getConnectionFactory)
            .map(RedisConnectionFactory::getConnection)
            .map(RedisCommandsProvider::serverCommands)
            .ifPresent(RedisServerCommands::flushAll);
    }
}
