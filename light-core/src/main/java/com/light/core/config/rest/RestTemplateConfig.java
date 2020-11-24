package com.light.core.config.rest;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.base.Charsets;
import com.light.core.config.LightProperties;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.List;

@Configuration(proxyBeanMethods = false)
public class RestTemplateConfig {

    @Value("${ribbon.ConnectTimeout:5000}")
    private Integer connectTimeout;
    @Value("${ribbon.ReadTimeout:60000}")
    private Integer readTimeout;

    @Bean
    @ConditionalOnBean(HttpClient.class)
    public ClientHttpRequestFactory httpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        httpComponentsClientHttpRequestFactory.setConnectTimeout(this.connectTimeout);
        httpComponentsClientHttpRequestFactory.setReadTimeout(this.readTimeout);
        return httpComponentsClientHttpRequestFactory;
    }

    @LoadBalanced
    @Bean(value = "restTemplateRibbon")
    @ConditionalOnClass(LoadBalanced.class)
    @ConditionalOnBean(ClientHttpRequestFactory.class)
    public RestTemplate restTemplateRibbon(ClientHttpRequestFactory httpRequestFactory, LightProperties lightProperties, FastJsonHttpMessageConverter fastJsonHttpMessageConverter) {
        BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(httpRequestFactory);
        RestTemplate restTemplate = new RestTemplate(bufferingClientHttpRequestFactory);
        restTemplate.getInterceptors().add(new RestTemplateHeaderInterceptor(lightProperties));
        restTemplate.getInterceptors().add(new RestTemplateLogInterceptor(lightProperties));

        //fastjson 替换 jackson
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof StringHttpMessageConverter) {
                iterator.remove();
            }
            if (converter instanceof GsonHttpMessageConverter || converter instanceof MappingJackson2HttpMessageConverter) {
                iterator.remove();
            }
        }
        messageConverters.add(new StringHttpMessageConverter(Charsets.UTF_8));
        messageConverters.add(fastJsonHttpMessageConverter);
        return restTemplate;
    }

    @Bean(value = "restTemplate")
    @ConditionalOnBean(ClientHttpRequestFactory.class)
    public RestTemplate restTemplate(ClientHttpRequestFactory httpRequestFactory, LightProperties lightProperties, FastJsonHttpMessageConverter fastJsonHttpMessageConverter) {
        BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(httpRequestFactory);
        RestTemplate restTemplate = new RestTemplate(bufferingClientHttpRequestFactory);
        restTemplate.getInterceptors().add(new RestTemplateLogInterceptor(lightProperties));

        //fastjson 替换 jackson
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof StringHttpMessageConverter) {
                iterator.remove();
            }
            if (converter instanceof GsonHttpMessageConverter || converter instanceof MappingJackson2HttpMessageConverter) {
                iterator.remove();
            }
        }
        messageConverters.add(new StringHttpMessageConverter(Charsets.UTF_8));
        messageConverters.add(fastJsonHttpMessageConverter);
        return restTemplate;
    }
}
