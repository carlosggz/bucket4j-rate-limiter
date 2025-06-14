package com.example.bucket4jratelimiter.components;

import com.example.bucket4jratelimiter.utils.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
}