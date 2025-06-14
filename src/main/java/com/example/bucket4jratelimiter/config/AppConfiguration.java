package com.example.bucket4jratelimiter.config;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
public class AppConfiguration {

    @Bean
    public RedisClient appRedisClient(RedisProperties redisProperties) {
        return RedisClient.create(RedisURI.builder()
                .withSsl(false)
                .withHost(redisProperties.getHost())
                .withPort(redisProperties.getPort())
                .build()
        );
    }

    @Bean
    public ProxyManager<String> appRedisProxyManager(RedisClient redisClient) {
        var redisConnection = redisClient
                .connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

        return LettuceBasedProxyManager.builderFor(redisConnection)
                .withExpirationStrategy(
                        ExpirationAfterWriteStrategy.fixedTimeToLive(Duration.ofMinutes(1L)))
                .build();
    }

    @Bean
    public WebMvcConfigurer appWebMvcConfigurer(ClientFilteredInterceptor clientFilteredInterceptor,
                                                @Value("${app.interceptor-routes}") String[] routes) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry
                        .addInterceptor(clientFilteredInterceptor)
                        .addPathPatterns(routes);
            }
        };
    }
}
