package com.example.bucket4jratelimiter.config;

import io.github.bucket4j.distributed.jdbc.BucketTableSettings;
import io.github.bucket4j.distributed.jdbc.PrimaryKeyMapper;
import io.github.bucket4j.distributed.jdbc.SQLProxyConfiguration;
import io.github.bucket4j.distributed.proxy.ClientSideConfig;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.postgresql.PostgreSQLadvisoryLockBasedProxyManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@Configuration
public class AppConfiguration {

    @Bean
    public ProxyManager<String> appPostgresProxyManager(DataSource dataSource, @Value("${app.scheme}") String scheme) {
        var tableName = (StringUtils.isBlank(scheme) ? "" : scheme + ".") + "bucket";
        return new PostgreSQLadvisoryLockBasedProxyManager<String>(SQLProxyConfiguration.builder()
                .withPrimaryKeyMapper(PrimaryKeyMapper.STRING)
                .withTableSettings(BucketTableSettings
                        .customSettings(tableName, "id", "state"))
                .withClientSideConfig(ClientSideConfig.getDefault())
                .build(dataSource));
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
