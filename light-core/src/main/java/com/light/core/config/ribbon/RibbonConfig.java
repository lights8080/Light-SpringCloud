package com.light.core.config.ribbon;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(LoadBalancedRetryFactory.class)
public class RibbonConfig {
    @Bean
    @ConditionalOnClass(name = "org.springframework.retry.support.RetryTemplate")
    public LoadBalancedRetryFactory loadBalancedRetryPolicyFactory(
            final SpringClientFactory clientFactory) {
        return new LightRibbonLoadBalancedRetryFactory(clientFactory);
    }
}
