package com.light.consumer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "用户信息")
public class User {

    @NotNull
    @ApiModelProperty(value = "用户名", required = true)
    private String name;

    @ApiModelProperty(value = "年龄")
    private Integer age;
}
