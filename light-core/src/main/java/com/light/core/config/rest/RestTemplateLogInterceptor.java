package com.light.core.config.rest;

import com.google.common.base.Charsets;
import com.light.core.config.LightProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * RestTemplate请求和相应日志拦截器
 *
 * @author lihaipeng
 * @date 2020-09-29
 */
public class RestTemplateLogInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger("RestTemplateLog");
    private LightProperties lightProperties;

    public RestTemplateLogInterceptor(LightProperties lightProperties) {
        this.lightProperties = lightProperties;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String methodTag = getMethodTag(request.getURI());
        LightProperties.Setting setting = lightProperties.getLogger().getRest();
        if (logger.isInfoEnabled() && setting.isEnabled()) {
            if (!setting.getSkipMethodSigns().contains(methodTag)) {
                logger.info("REST-REQ-[{}] {}-->{} {}", methodTag, body != null ? new String(body, Charsets.UTF_8) : "", request.getMethod(), request.getURI());
            }
        }
        long start = System.nanoTime();
        ClientHttpResponse response = null;
        try {
            response = execution.execute(request, body);
            long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            if (logger.isInfoEnabled()) {
                StringBuilder inputStringBuilder = new StringBuilder();
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), Charsets.UTF_8))) {
                    String line = bufferedReader.readLine();
                    while (line != null) {
                        inputStringBuilder.append(line);
                        line = bufferedReader.readLine();
                        if (line != null) {
                            inputStringBuilder.append('\n');
                        }
                    }
                }

                if (logger.isInfoEnabled() && setting.isEnabled()) {
                    if (!setting.getSkipMethodSigns().contains(methodTag)) {
                        if (setting.getIgnoreResponseDataMethodSigns().contains(methodTag)) {
                            logger.info("REST-RESP-[{}] IGNORE_DATA size:{} spend:{}ms", methodTag, inputStringBuilder.length(), elapsedTime);
                        } else {
                            logger.info("REST-RESP-[{}] {} size:{} spend:{}ms", methodTag, inputStringBuilder.toString(), inputStringBuilder.length(), elapsedTime);
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            if (logger.isErrorEnabled() && setting.isEnabled()) {
                logger.error("REST-RESP-[{}] {}: {}", methodTag, ioe.getClass().getSimpleName(), ioe.getMessage());
            }
            throw ioe;
        }
        return response;
    }

    private String getMethodTag(URI uri) {
        try {
            if (uri.getPath().indexOf('?') > -1) {
                return uri.getHost() + uri.getPath().substring(0, uri.getPath().indexOf('?'));
            } else {
                return uri.getHost() + uri.getPath();
            }
        } catch (Exception e) {
            return uri.getHost() + uri.getPath();
        }
    }

}
