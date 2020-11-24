package com.light.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.light.dto.BasePageReq;
import com.light.dto.BaseResp;
import com.light.entity.DemoUser;
import com.light.enums.RespStatusEnum;
import com.light.mapper.DemoUserMapper;
import com.light.consumer.dto.User;
import com.light.consumer.dto.UserListResp;
import com.light.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<DemoUserMapper, DemoUser> implements UserService {

    @Autowired
    private DemoUserMapper demoUserMapper;

    @Override
    public UserListResp list(BasePageReq<User> u) {
        UserListResp resp = new UserListResp();
        resp.setStatus(RespStatusEnum.SUCCESS.value());
        resp.setMessage(RespStatusEnum.SUCCESS.name());
        IPage<DemoUser> userPage = new Page<>(u.getPageNum(), u.getPageSize());
        List<User> list = new ArrayList<>();
        LambdaQueryWrapper<DemoUser> queryWrapper = new LambdaQueryWrapper<>();
        if (u.getData() != null && StringUtils.isNotEmpty(u.getData().getName())) {
            queryWrapper.eq(DemoUser::getName, u.getData().getName());
        }
        userPage = demoUserMapper.selectPage(userPage, queryWrapper);
        log.info(JSON.toJSONString(userPage));
        for (DemoUser userEntity : userPage.getRecords()) {
            User user = new User();
            BeanUtils.copyProperties(userEntity, user);
            list.add(user);
        }
        resp.setTotalPage(new Long(userPage.getPages()).intValue());
        resp.setTotalRow(userPage.getTotal());
        resp.setUsers(list);
        return resp;
    }

    @Override
    public BaseResp add(User user) {
        DemoUser demoUserEntity = new DemoUser();
        demoUserEntity.setName(user.getName());
        demoUserEntity.setAge(user.getAge());
        demoUserMapper.insert(demoUserEntity);
        return BaseResp.success();
    }
}
