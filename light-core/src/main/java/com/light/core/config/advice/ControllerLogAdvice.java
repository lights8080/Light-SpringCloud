package com.light.core.config.advice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.light.core.config.LightProperties;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Controller请求和相应日志拦截器
 *
 * @author lihaipeng
 * @date 2020-09-29
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ControllerLogAdvice implements MethodInterceptor {
    private final static String METHOD_GET = "GET";
    private final static String METHOD_POST = "POST";
    private final static String METHOD_PUT = "PUT";
    private final static Logger logger = LoggerFactory.getLogger("ControllerLog");

    private LightProperties lightProperties;

    public ControllerLogAdvice(LightProperties lightProperties) {
        this.lightProperties = lightProperties;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String sortMethodName = getSortMethodName(invocation.getMethod());
        LightProperties.Setting setting = lightProperties.getLogger().getController();
        try {
            long start = System.nanoTime();
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();
            String requestData = null;
            if (METHOD_POST.equalsIgnoreCase(request.getMethod()) || METHOD_PUT.equalsIgnoreCase(request.getMethod())) {
                if (invocation.getArguments() != null) {
                    try {
                        requestData = JSONArray.toJSONString(invocation.getArguments(), SerializerFeature.IgnoreNonFieldGetter);
                    } catch (Exception e) {
                        requestData = "Exception[JSONArray.toJSONString]";
                    }
                }
            } else if (METHOD_GET.equalsIgnoreCase(request.getMethod())) {
                requestData = request.getQueryString();
            }

            if (logger.isInfoEnabled() && setting.isEnabled()) {
                if (!setting.getSkipMethodSigns().contains(sortMethodName)) {
                    logger.info("CONTROLLER-REQ-[{}] {}", sortMethodName, (requestData != null ? requestData : ""));
                }
            }
            Object result = invocation.proceed();
            long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            String responseData = null;
            if (result != null) {
                responseData = JSON.toJSONString(result);
            }
            if (logger.isInfoEnabled() && setting.isEnabled()) {
                if (!setting.getSkipMethodSigns().contains(sortMethodName)) {
                    if (setting.getIgnoreResponseDataMethodSigns().contains(sortMethodName)) {
                        logger.info("CONTROLLER-RESP-[{}] IGNORE_DATA size:{} spend:{}ms", sortMethodName, (responseData != null ? responseData.length() : 0), elapsedTime);
                    } else {
                        logger.info("CONTROLLER-RESP-[{}] {} size:{} spend:{}ms", sortMethodName, (responseData != null ? responseData : ""), (responseData != null ? responseData.length() : 0), elapsedTime);
                    }
                }
            }
            return result;
        } catch (Throwable throwable) {
            if (logger.isErrorEnabled() && setting.isEnabled()) {
                logger.error("CONTROLLER-RESP-[{}] {}: {}", sortMethodName, throwable.getClass().getSimpleName(), throwable.getMessage());
            }
            throw throwable;
        }
    }


    private String getSortMethodName(Method method) {
        StringBuilder sb = new StringBuilder();
        appendType(sb, method.getDeclaringClass(), false);
        sb.append(".");
        sb.append(method.getName());
        sb.append("(");
        Class<?>[] parametersTypes = method.getParameterTypes();
        appendTypes(sb, parametersTypes, false, false);
        sb.append(")");
        return sb.toString();
    }


    private void appendTypes(StringBuilder sb, Class<?>[] types, boolean includeArgs,
                             boolean useLongReturnAndArgumentTypeName) {
        if (includeArgs) {
            for (int size = types.length, i = 0; i < size; i++) {
                appendType(sb, types[i], useLongReturnAndArgumentTypeName);
                if (i < size - 1) {
                    sb.append(",");
                }
            }
        } else {
            if (types.length != 0) {
                sb.append("..");
            }
        }
    }

    private void appendType(StringBuilder sb, Class<?> type, boolean useLongTypeName) {
        if (type.isArray()) {
            appendType(sb, type.getComponentType(), useLongTypeName);
            sb.append("[]");
        } else {
            sb.append(useLongTypeName ? type.getName() : type.getSimpleName());
        }
    }
}

