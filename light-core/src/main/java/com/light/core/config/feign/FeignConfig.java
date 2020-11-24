package com.light.core.config.feign;

import com.light.core.config.LightProperties;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignLoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(FeignLoggerFactory.class)
public class FeignConfig {

    @Autowired
    private LightProperties lightProperties;

    @Bean
    Logger.Level feignLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    FeignLoggerFactory infoFeignLoggerFactory(LightProperties lightProperties) {
        return new LightFeignLoggerFactory(lightProperties);
    }

    @Bean
    public RequestInterceptor headerInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return;
            }
            HttpServletRequest request = attributes.getRequest();
            for (String headerName : lightProperties.getHeader().getThroughNames()) {
                String headerValue = request.getHeader(headerName);
                if (!StringUtils.isEmpty(headerValue)) {
                    requestTemplate.header(headerName, headerValue);
                }
            }

        };
    }

}
