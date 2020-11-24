package com.light.core.config.feign;

import com.google.common.base.Charsets;
import com.light.core.config.LightProperties;
import feign.Request;
import feign.Response;
import feign.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static feign.Util.decodeOrDefault;

/**
 * Feign请求和相应日志处理
 *
 * @author lihaipeng
 * @date 2020-09-29
 */
public class LightFeignLogger extends feign.Logger {
    private static final Logger logger = LoggerFactory.getLogger("FeignLog");
    //    private final Logger logger;
    private LightProperties lightProperties;

    public LightFeignLogger(Logger logger, LightProperties lightProperties) {
//        this.logger = logger;
        this.lightProperties = lightProperties;
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        LightProperties.Setting setting = lightProperties.getLogger().getFeign();
        if (logger.isInfoEnabled() && setting.isEnabled()) {
            if (!setting.getSkipMethodSigns().contains(configKey)) {
                logger.info("FEIGN-REQ-[{}] {}-->{} {}", configKey, request.body() != null ? new String(request.body(), Charsets.UTF_8) : "", request.httpMethod(), request.url());
            }
        }
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        LightProperties.Setting setting = lightProperties.getLogger().getFeign();
        if (response.body() != null) {
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());
            if (logger.isInfoEnabled() && setting.isEnabled()) {
                if (!setting.getSkipMethodSigns().contains(configKey)) {
                    if (setting.getIgnoreResponseDataMethodSigns().contains(configKey)) {
                        logger.info("FEIGN-RESP-[{}] IGNORE_DATA size:{} spend:{}ms", configKey, (bodyData != null ? bodyData.length : 0), elapsedTime);
                    } else {
                        logger.info("FEIGN-RESP-[{}] {} size:{} spend:{}ms", configKey, decodeOrDefault(bodyData, Charsets.UTF_8, ""), (bodyData != null ? bodyData.length : 0), elapsedTime);
                    }
                }
            }
            return response.toBuilder().body(bodyData).build();
        } else {
            if (logger.isInfoEnabled() && setting.isEnabled()) {
                if (!setting.getSkipMethodSigns().contains(configKey)) {
                    logger.info("FEIGN-RESP-[{}] size:0 spend:{}ms", configKey, elapsedTime);
                }
            }
            return response;
        }
    }

    @Override
    protected IOException logIOException(String configKey, Level logLevel, IOException ioe, long elapsedTime) {
        if (logger.isErrorEnabled() && lightProperties.getLogger().getFeign().isEnabled()) {
            logger.error("FEIGN-RESP-[{}] {}: {}", configKey, ioe.getClass().getSimpleName(), ioe.getMessage());
        }
        return ioe;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format(methodTag(configKey) + format, args));
        }
    }
}
