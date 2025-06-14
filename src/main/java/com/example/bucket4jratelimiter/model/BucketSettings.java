package com.example.bucket4jratelimiter.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;


public record BucketSettings(String name, int maxRequests, int secondsRefill) {
    public BucketSettings {
        Assert.isTrue(StringUtils.isNotBlank(name), "Bucket name cannot be empty");
        Assert.isTrue(maxRequests > 0, "Max requests must be greater than 0");
        Assert.isTrue(secondsRefill > 0, "Seconds refill must be greater than 0");
    }
}
