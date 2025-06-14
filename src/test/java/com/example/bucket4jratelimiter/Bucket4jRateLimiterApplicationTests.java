package com.example.bucket4jratelimiter;

import com.example.bucket4jratelimiter.model.Constants;
import com.example.bucket4jratelimiter.config.BucketsProperties;
import com.example.bucket4jratelimiter.utils.BaseIntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class Bucket4jRateLimiterApplicationTests extends BaseIntegrationTest {

    @Autowired
    private BucketsProperties bucketsProperties;

    @Test
    void contextLoads() {
        assertNotNull(bucketsProperties);
        assertFalse(CollectionUtils.isEmpty(bucketsProperties.getBuckets()));
        assertEquals(3, bucketsProperties.getBuckets().size());

        Stream
                .of("client1", "client2", "others")
                .forEach(bucket -> assertTrue(bucketsProperties
                        .getBuckets()
                        .stream()
                        .anyMatch(b -> b.name().equals(bucket))));

    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/v1/custom/{id}", "/api/v1/filtered/{id}"})
    @SneakyThrows
    void callProcessUsingTokenBucket(String url) {
        //given
        var executor = Executors.newFixedThreadPool(3);
        var requests = IntStream
                .rangeClosed(1, 4)
                .mapToObj(i -> getCall(url, "req" + i, "value" + i))
                .toList();
        Thread.sleep(1000); //wait for refill

        //when
        var results = requests
                .stream()
                .parallel()
                .map(executor::submit)
                .toList();

        //then
        var responseOk = 0;
        var responseKo = 0;

        for (var result : results) {
            var response = result.get();
            if (response == HttpStatus.OK.value()) {
                responseOk++;
            } else if (response == HttpStatus.TOO_MANY_REQUESTS.value()) {
                responseKo++;
            }
        }

        assertEquals(2, responseOk);
        assertEquals(2, responseKo);
    }

    private Callable<Integer> getCall(String url, String id, String value) {
        return () -> mockMvc
                .perform(
                        post(url, id)
                                .header(Constants.CLIENT_HEADER, "client1")
                                .content(value)
                )
                .andReturn()
                .getResponse()
                .getStatus();
    }
}
