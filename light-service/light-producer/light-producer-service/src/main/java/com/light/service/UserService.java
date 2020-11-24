package com.light.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.light.dto.BasePageReq;
import com.light.dto.BaseResp;
import com.light.entity.DemoUser;
import com.light.consumer.dto.User;
import com.light.consumer.dto.UserListResp;

public interface UserService extends IService<DemoUser> {

    UserListResp list(BasePageReq<User> user);

    BaseResp add(User ser);
}
