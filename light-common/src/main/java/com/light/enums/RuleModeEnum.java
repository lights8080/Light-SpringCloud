package com.light.enums;

/**
 * 负载均衡策略
 *
 * @author lihaipeng
 * @date 2020-09-22
 */
public enum RuleModeEnum {

    /**
     * 权重
     */
    RANDOM_WEIGHT("randomWeight"),
    /**
     * 轮训
     */
    ROUND_ROBIN("roundRobin");

    private String name;

    private RuleModeEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
