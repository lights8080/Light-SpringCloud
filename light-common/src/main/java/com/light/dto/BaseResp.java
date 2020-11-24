package com.light.dto;

import com.light.enums.RespStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应基础类
 *
 * @author lihaipeng
 * @date 2020-09-28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BaseResp<T extends Object> implements Serializable {
    private static final long serialVersionUID = 1;
    private Integer status;
    private String message;
    private T result;

    public BaseResp(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static BaseResp success() {
        return new BaseResp(RespStatusEnum.SUCCESS.value(), RespStatusEnum.SUCCESS.name());
    }

    public static BaseResp success(String message) {
        return new BaseResp(RespStatusEnum.SUCCESS.value(), message);
    }

    public static BaseResp success(String message, Object data) {
        return new BaseResp(RespStatusEnum.SUCCESS.value(), message, data);
    }

    public static BaseResp error() {
        return new BaseResp(RespStatusEnum.SERVER_ERROR.value(), RespStatusEnum.SERVER_ERROR.name());
    }

    public static BaseResp error(String message) {
        return new BaseResp(RespStatusEnum.SERVER_ERROR.value(), message);
    }

    public static BaseResp error(String message, Object data) {
        return new BaseResp(RespStatusEnum.SERVER_ERROR.value(), message, data);
    }
}
