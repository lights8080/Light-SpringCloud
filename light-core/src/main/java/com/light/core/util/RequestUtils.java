/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.light.core.util;

import com.light.constant.LightConstants;
import com.light.dto.common.LoginUserInfo;
import com.light.enums.UserModeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Servlet请求工具类
 *
 * @author lihaipeng
 * @date 2020-09-28
 */
@Slf4j
public class RequestUtils {

    /**
     * get login user information.
     *
     * @return
     */
    public static LoginUserInfo getLoginUserInfo() {
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        HttpServletRequest request = getRequest();
        if (request == null) {
            loginUserInfo.setLoginName(LightConstants.UNKNOWN);
            loginUserInfo.setUserMode(UserModeEnum.NORMAL);
            return loginUserInfo;
        }
        loginUserInfo.setLoginName(request.getHeader(LightConstants.X_LIGHT_USERNAME));
        loginUserInfo.setUserMode(UserModeEnum.valueOf(request.getHeader(LightConstants.X_LIGHT_USERMODE)));
        return loginUserInfo;
    }

    /**
     * get request.
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = getRequestAttributes();
        if (requestAttributes != null) {
            return requestAttributes.getRequest();
        } else {
            return null;
        }
    }


    /**
     * get response.
     *
     * @return {HttpServletResponse}
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = getRequestAttributes();
        if (requestAttributes != null) {
            return requestAttributes.getResponse();
        } else {
            return null;
        }
    }

    public static ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * get remote ip.
     *
     * @param request
     * @return
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader(LightConstants.X_FORWARDED_FOR);
        if (StringUtils.isEmpty(ipAddress) || LightConstants.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(LightConstants.PROXY_CLIENT_IP);
        }
        if (StringUtils.isEmpty(ipAddress) || LightConstants.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(LightConstants.WL_PROXY_CLIENT_IP);
        }
        if (StringUtils.isEmpty(ipAddress) || LightConstants.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(LightConstants.HTTP_CLIENT_IP);
        }
        if (StringUtils.isEmpty(ipAddress) || LightConstants.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(LightConstants.X_REAL_IP);
        }
        if (StringUtils.isEmpty(ipAddress) || LightConstants.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        if (StringUtils.isEmpty(ipAddress)) {
            ipAddress = LightConstants.UNKNOWN;
        }
        return ipAddress;
    }


    /**
     * URL Decoder
     */
    public static String URLDecoderString(String v) {
        if (!StringUtils.isEmpty(v)) {
            try {
                return URLDecoder.decode(v, LightConstants.UTF8);
            } catch (UnsupportedEncodingException e) {
                log.error("UnsupportedEncodingException:", e);
            }
        }
        return "";
    }
}
