package com.example.bucket4jratelimiter.components;

import com.example.bucket4jratelimiter.config.BucketsProperties;
import com.example.bucket4jratelimiter.model.BucketSettings;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Component
@Slf4j
public class TokenBucketComponent {

    private final Map<String, BucketConfiguration> buckets;
    private final String defaultBucket;
    private final ProxyManager<String> proxyManager;

    public TokenBucketComponent(@Lazy ProxyManager<String> proxyManager, BucketsProperties  bucketsProperties) {
        this.proxyManager = proxyManager;
        this.defaultBucket = bucketsProperties.getDefaultBucket();
        this.buckets = new HashMap<>();

        log.info("Initializing TokenBucketComponent...");

        for (BucketSettings settings : bucketsProperties.getBuckets()) {
            var bucket = BucketConfiguration.builder()
                    .addLimit(l -> l
                            .capacity(settings.maxRequests())
                            .refillIntervally(settings.maxRequests(), Duration.ofSeconds(settings.secondsRefill()))
                            .initialTokens(settings.maxRequests()))
                    .build();
            this.buckets.put(settings.name(), bucket);

            log.info("Created bucket {} with max request {} and seconds {}",
                    settings.name(), settings.maxRequests(), settings.secondsRefill());
        }

        log.info("TokenBucketComponent initialized!");
    }

    public boolean tryConsumeToken(String bucketName) {
        var configuration = Optional
                .ofNullable(bucketName)
                .filter(buckets::containsKey)
                .orElse(defaultBucket);

        return proxyManager.builder()
                .build(configuration, () -> this.buckets.get(configuration))
                .tryConsume(1);
    }
}
