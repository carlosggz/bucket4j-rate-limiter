package com.example.bucket4jratelimiter.components;

import com.example.bucket4jratelimiter.utils.BaseIntegrationTest;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TokenBucketComponentTest extends BaseIntegrationTest {

    @Autowired
    private TokenBucketComponent tokenBucketComponent;

    @Test
    void tokenBucketLimitRequests() {
        assertTrue(tokenBucketComponent.tryConsumeToken("client1"));
        assertTrue(tokenBucketComponent.tryConsumeToken("client1"));
        assertFalse(tokenBucketComponent.tryConsumeToken("client1"));
    }

    @Test
    void whenSendInvalidBucketItUsesDefault() {
        assertTrue(tokenBucketComponent.tryConsumeToken("unknown1"));
        assertFalse(tokenBucketComponent.tryConsumeToken("unknown2"));
    }

    @Test
    @SneakyThrows
    void testConsumeToken() {
        //given
        val start = LocalDateTime.now();

        //when
        tokenBucketComponent.consumeToken("unknown1");
        tokenBucketComponent.consumeToken("unknown2");
        tokenBucketComponent.consumeToken("unknown3");

        //then
        val duration = Duration.between(start, LocalDateTime.now());
        assertTrue(duration.getSeconds() >= 2 && duration.getSeconds() <= 3);
    }
}