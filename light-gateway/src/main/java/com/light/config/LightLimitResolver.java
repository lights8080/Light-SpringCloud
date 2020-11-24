package com.light.config;

import com.light.constant.LightConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

/**
 * 限流解析器，支持多网关服务名称配置
 *
 * @author lihaipeng
 * @date 2020-09-24
 */
@Configuration
public class LightLimitResolver {


    @Value("${spring.application.name:light-gateway}")
    private String applicationName;

    @Primary
    @Bean
    public KeyResolver ipKeyResolver() {
        return (exchange) -> {
            String hostname = exchange.getRequest().getRemoteAddress().getHostName();
            if (StringUtils.isEmpty(hostname)) {
                return Mono.empty();
            } else {
                return Mono.just(applicationName + LightConstants.Symbol.COLON + hostname);
            }
        };
    }

    @Bean
    public KeyResolver userKeyResolver() {
        return (exchange) -> {
            String username = exchange.getRequest().getHeaders().getFirst(LightConstants.X_LIGHT_USERNAME);
            if (StringUtils.isEmpty(username)) {
                return Mono.empty();
            } else {
                return Mono.just(applicationName + LightConstants.Symbol.COLON + username);
            }
        };
    }

    @Bean
    KeyResolver apiKeyResolver() {
        return (exchange) -> {
            String path = exchange.getRequest().getPath().value();
            if (StringUtils.isEmpty(path)) {
                return Mono.empty();
            } else {
                return Mono.just(applicationName + LightConstants.Symbol.COLON + path);
            }
        };
    }
}
