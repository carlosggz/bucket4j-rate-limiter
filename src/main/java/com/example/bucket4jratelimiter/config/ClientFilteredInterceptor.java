package com.example.bucket4jratelimiter.config;

import com.example.bucket4jratelimiter.components.TokenBucketComponent;
import com.example.bucket4jratelimiter.model.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class ClientFilteredInterceptor implements HandlerInterceptor {

    private final TokenBucketComponent tokenBucketComponent;
    private final String instanceId;

    public ClientFilteredInterceptor(
            TokenBucketComponent tokenBucketComponent,
            @Value("${app.instance-id}") String instanceId) {
        this.tokenBucketComponent = tokenBucketComponent;
        this.instanceId = instanceId;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        response.setHeader(Constants.INSTANCE_HEADER, instanceId);
        val apiClient = request.getHeader(Constants.CLIENT_HEADER);

        if (tokenBucketComponent.tryConsumeToken(apiClient)) {
            log.info("API client request allowed");
            return true;
        } else {
            log.info("API client request NOT allowed");
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "Limit reached");
            return false;
        }
    }
}
