package com.light.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页响应基础类
 *
 * @author lihaipeng
 * @date 2020-09-28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasePageResp<T extends Object> extends BaseResp<T> {
    /**
     * 总条数
     */
    private Long totalRow;
    /**
     * 总页数
     */
    private Integer totalPage;
}
