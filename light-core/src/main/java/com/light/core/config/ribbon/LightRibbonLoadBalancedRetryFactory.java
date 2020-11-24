package com.light.core.config.ribbon;

import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancedRetryFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.retry.RetryListener;

public class LightRibbonLoadBalancedRetryFactory extends RibbonLoadBalancedRetryFactory {

    private RetryListener[] retryListener = {new RetryLogListener()};

    public LightRibbonLoadBalancedRetryFactory(SpringClientFactory clientFactory) {
        super(clientFactory);
    }

    @Override
    public RetryListener[] createRetryListeners(String service) {
        return retryListener;
    }
}
