package com.light.filter;

import com.light.constant.LightConstants;
import com.light.enums.UserModeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

/**
 * 权限
 *
 * @author lihaipeng
 * @date 2020-09-24
 */
@Component
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            exchange.getResponse().getHeaders().add(LightConstants.X_LIGHT_REQUESTID, exchange.getRequest().getId());
            String token = exchange.getRequest().getHeaders().getFirst(LightConstants.AUTHORIZATION);
            if (StringUtils.isEmpty(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // TODO 权限校验

            // 增加特定请求头
            Consumer<HttpHeaders> httpHeaders = httpHeader -> {
                httpHeader.set(LightConstants.X_LIGHT_REQUESTID, exchange.getRequest().getId());
                httpHeader.set(LightConstants.X_LIGHT_USERNAME, "zhangsan");
                httpHeader.set(LightConstants.X_LIGHT_USERMODE, UserModeEnum.NORMAL.name());
                httpHeader.setContentType(MediaType.APPLICATION_JSON);
            };

            ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate().headers(httpHeaders).build();
            return chain.filter(exchange.mutate().request(serverHttpRequest).build());
        } catch (Exception e) {
            log.error("Exception", e);
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }

}
