package com.example.bucket4jratelimiter.config;

import com.example.bucket4jratelimiter.model.BucketSettings;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "app", ignoreInvalidFields = true)
@NoArgsConstructor
@Slf4j
public class BucketsProperties {
    private List<BucketSettings> buckets;
    private String defaultBucket;

    @PostConstruct
    public void init() {
        Assert.isTrue(!CollectionUtils.isEmpty(buckets), "No buckets configured");
        Assert.isTrue(StringUtils.isNotBlank(defaultBucket), "No default bucket configured");
        Assert.isTrue(buckets.stream().anyMatch(x -> x.name().equals(defaultBucket)), "Invalid default bucket");
        Assert.isTrue(buckets.stream().map(BucketSettings::name).distinct().count() == buckets.size(), "Repeated buckets");
    }
}
