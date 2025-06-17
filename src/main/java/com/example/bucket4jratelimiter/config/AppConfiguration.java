package com.example.bucket4jratelimiter.config;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.jdbc.PrimaryKeyMapper;
import io.github.bucket4j.postgresql.Bucket4jPostgreSQL;
import io.github.bucket4j.postgresql.PostgreSQLadvisoryLockBasedProxyManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.time.Duration;

@Configuration
@EnableScheduling
public class AppConfiguration {

    @Bean
    public PostgreSQLadvisoryLockBasedProxyManager<String> appPostgresProxyManager(
            DataSource dataSource,
            @Value("${app.scheme}") String scheme,
            @Value("${app.seconds-to-expire}") int secondsToExpire) {
        return Bucket4jPostgreSQL
                .advisoryLockBasedBuilder(dataSource)
                .primaryKeyMapper(PrimaryKeyMapper.STRING)
                .table((StringUtils.isBlank(scheme) ? "" : scheme + ".") + "bucket")
                .idColumn("id")
                .stateColumn("state")
                .lockColumn("lock")
                .expiresAtColumn("expires_at")
                .expirationAfterWrite(ExpirationAfterWriteStrategy
                        .basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(secondsToExpire))
                )
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
