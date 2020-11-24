package com.light.core.config.feign;

import com.light.core.config.LightProperties;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignLoggerFactory;

public class LightFeignLoggerFactory implements FeignLoggerFactory {

    private LightProperties lightProperties;

    public LightFeignLoggerFactory(LightProperties lightProperties) {
        this.lightProperties = lightProperties;
    }

    @Override
    public feign.Logger create(Class<?> type) {
        return new LightFeignLogger(LoggerFactory.getLogger(type), this.lightProperties);
    }
}
