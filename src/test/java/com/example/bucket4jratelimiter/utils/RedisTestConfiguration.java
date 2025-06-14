package com.example.bucket4jratelimiter.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.embedded.RedisServer;

@TestConfiguration
@Slf4j
public
class RedisTestConfiguration {

    @Bean
    public RedisServer redisServer(RedisProperties redisProperties) {
        log.info("Starting Embedded Redis Server on port {}...", redisProperties.getPort());
        RedisServer redisServer = new RedisServer(redisProperties.getPort());
        redisServer.start();
        log.info("Embedded Redis Server started");
        return redisServer;
    }

    @Bean
    @DependsOn("redisServer")
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
        log.info("Creating template...");
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setDefaultSerializer(new StringRedisSerializer());
        return template;
    }
}
