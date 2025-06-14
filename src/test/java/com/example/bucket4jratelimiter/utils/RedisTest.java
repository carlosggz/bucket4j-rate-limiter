package com.example.bucket4jratelimiter.utils;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RedisTestConfiguration.class)
@ExtendWith(EmbeddedRedisExtension.class)
public @interface RedisTest {
}
