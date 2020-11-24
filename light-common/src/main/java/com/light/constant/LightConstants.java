package com.light.constant;

/**
 * The Light Global constant.
 *
 * @author lihaipeng
 * @date 2020-09-28
 */
public class LightConstants {

    public static final String UTF8 = "UTF-8";
    public static final String UNKNOWN = "unknown";

    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String X_REAL_IP = "X-Real-IP";
    public static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    public static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    public static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";

    public static final String AUTHORIZATION = "Authorization";
    public static final String X_LIGHT_REQUESTID = "X-Light-RequestId";
    public static final String X_LIGHT_USERNAME = "X-Light-userName";
    public static final String X_LIGHT_USERMODE = "X-Light-userMode";

    /**
     * 配置监听TOPIC
     */
    public static final String CONFIG_LISTENER_TOPIC = "config_listener";

    public static final class Symbol {
        private Symbol() {
        }

        public static final String COMMA = ",";
        public static final String SPOT = ".";
        public final static String UNDER_LINE = "_";
        public final static String PER_CENT = "%";
        public final static String AT = "@";
        public final static String LINE = "-";
        public static final String SLASH = "/";
        public static final String COLON = ":";
    }
}
