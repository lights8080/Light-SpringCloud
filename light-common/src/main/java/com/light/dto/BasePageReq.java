package com.light.dto;

import lombok.Data;

/**
 * 分页请求基础类
 *
 * @author lihaipeng
 * @date 2020-09-28
 */
@Data
public class BasePageReq<T extends Object> extends BaseReq<T> {
    /**
     * 当前页面
     */
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;

    /**
     * 排序
     */
    private String orderBy;
}
