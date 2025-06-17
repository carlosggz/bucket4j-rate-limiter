package com.example.bucket4jratelimiter.schedule;

import com.example.bucket4jratelimiter.components.TokenBucketComponent;
import com.example.bucket4jratelimiter.utils.BaseIntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        "app.seconds-to-expire=1"
})
class RemoveExpiredBucketsTest extends BaseIntegrationTest {

    @Autowired
    private RemoveExpiredBuckets removeExpiredBuckets;

    @Autowired
    private TokenBucketComponent tokenBucketComponent;

    @Test
    @SneakyThrows
    void whenExecutesHouseKeepingBucketsAreRemoved() {
        //given
        tokenBucketComponent.tryConsumeToken("client1");
        tokenBucketComponent.tryConsumeToken("client2");
        tokenBucketComponent.tryConsumeToken("others");
        Thread.sleep(2000);

        //when
        removeExpiredBuckets.doHousekeeping();

        //then
        assertEquals(3, removeExpiredBuckets.getRemovedOnLastExecution());
    }

    @Test
    @SneakyThrows
    void whenExecutesHouseKeepingWithoutBucketsItRemovedNothing() {
        //when
        removeExpiredBuckets.doHousekeeping();

        //then
        assertEquals(0, removeExpiredBuckets.getRemovedOnLastExecution());
    }
}