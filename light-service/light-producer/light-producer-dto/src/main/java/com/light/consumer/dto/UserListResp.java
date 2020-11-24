package com.light.consumer.dto;

import com.light.dto.BasePageResp;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel(value = "订单列表请求信息")
public class UserListResp extends BasePageResp {
    private List<User> users;
}
