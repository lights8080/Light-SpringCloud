package com.light.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 请求基础类
 *
 * @author lihaipeng
 * @date 2020-09-28
 */
@Data
public class BaseReq<T extends Object> implements Serializable {
    private static final long serialVersionUID = 1;
    private T data;

    public BaseReq() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
