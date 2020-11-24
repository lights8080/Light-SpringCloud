package com.light.core.config.converter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.base.Charsets;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

@ConditionalOnClass(FastJsonHttpMessageConverter.class)
@Configuration(proxyBeanMethods = false)
public class HttpMessageConverterConfig {

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters(FastJsonHttpMessageConverter fastJsonHttpMessageConverter) {
        return new HttpMessageConverters(fastJsonHttpMessageConverter);
    }

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        return newFastJsonHttpMessageConverter();
    }

    private FastJsonHttpMessageConverter newFastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        SerializerFeature[] serializerFeatures = new SerializerFeature[]{
                // Date的日期转换器
                SerializerFeature.WriteDateUseDateFormat,
                // 循环引用
                SerializerFeature.DisableCircularReferenceDetect
        };
        fastJsonConfig.setSerializerFeatures(serializerFeatures);
        fastJsonConfig.setCharset(Charsets.UTF_8);
        fastConverter.setFastJsonConfig(fastJsonConfig);

        List<MediaType> fastjsonSupportedMediaTypes = new ArrayList<>();
        fastjsonSupportedMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(fastjsonSupportedMediaTypes);
        return fastConverter;
    }
}
