package com.light.core.config.rest;

import com.light.core.config.LightProperties;
import com.light.core.context.LightContextHolder;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class RestTemplateHeaderInterceptor implements ClientHttpRequestInterceptor {

    private LightProperties lightProperties;

    public RestTemplateHeaderInterceptor(LightProperties lightProperties) {
        this.lightProperties = lightProperties;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        for (String headerName : lightProperties.getHeader().getThroughNames()) {
            String headerValue = LightContextHolder.getLightContext().get(headerName);
            if (!StringUtils.isEmpty(headerValue)) {
                request.getHeaders().add(headerName, headerValue);
            }
        }
        return execution.execute(request, body);
    }
}
