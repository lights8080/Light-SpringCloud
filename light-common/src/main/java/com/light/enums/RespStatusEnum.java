package com.light.enums;

/**
 * API response status
 *
 * @author lihaipeng
 * @date 2020-09-22
 */
public enum RespStatusEnum {
    /**
     * (200)请求成功
     */
    SUCCESS(200),

    /**
     * (400)请求失败
     */
    BAD_REQUEST(400),

    /**
     * (401)没有通过身份验证
     */
    UNAUTHORIZED(401),

    /**
     * (403)用户通过了身份验证，但是不具有访问资源所需的权限
     */
    FORBIDDEN(403),

    /**
     * (405)HTTP方法不支持
     */
    METHOD_NOT_ALLOWED(405),

    /**
     * (412)客户端请求信息的先决条件错误
     */
    PRECONDITION_FAILED(412),

    /**
     * (415)客户端要求的返回格式不支持
     */
    UNSUPPORTED_MEDIA_TYPE(415),

    /**
     * (429)客户端的请求次数超过限额
     */
    TOO_MANY_REQUESTS(429),

    /**
     * (500)客户端请求有效，服务器处理时发生了意外
     */
    SERVER_ERROR(500);

    private final Integer value;

    RespStatusEnum(Integer v) {
        value = v;
    }

    public Integer value() {
        return value;
    }

    public static RespStatusEnum fromValue(Integer v) {
        for (RespStatusEnum c : RespStatusEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return null;
    }
}
