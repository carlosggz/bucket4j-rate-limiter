package com.example.bucket4jratelimiter.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public record ClientRequest(String id, String value) {
    public ClientRequest {
        Assert.isTrue(StringUtils.isNotBlank(id), "Client request id cannot be empty");
        Assert.isTrue(StringUtils.isNotBlank(value), "Client request value cannot be empty");
    }
}
