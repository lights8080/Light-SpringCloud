package com.light.core.config.advice;

import com.light.dto.BaseResp;
import com.light.enums.RespStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller异常处理
 *
 * @author lihaipeng
 * @date 2020-09-29
 */
@Slf4j
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice
@ConditionalOnClass(RestControllerAdvice.class)
@ConditionalOnProperty(value = "light.advice.controller-exception-advice-enabled", havingValue = "true", matchIfMissing = true)
public class ControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        log.error("Exception:", e);
        return BaseResp.error();
    }

    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException:", e);
        return new BaseResp(RespStatusEnum.METHOD_NOT_ALLOWED.value(), RespStatusEnum.METHOD_NOT_ALLOWED.name());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: ", e);
        return new BaseResp<>(RespStatusEnum.PRECONDITION_FAILED.value(), "request parameter error");
    }
}



