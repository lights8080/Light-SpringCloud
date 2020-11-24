package com.light.message;

import com.alibaba.fastjson.JSON;
import com.light.dto.BaseResp;
import com.light.consumer.dto.User;
import com.light.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "light-producer-service", topic = "light-user", selectorExpression = "add")
public class UserConsumer implements RocketMQListener<User> {
    @Autowired
    private UserService UserService;

    @Override
    public void onMessage(User user) {
        log.info("onMessage. user:add req:{}", JSON.toJSONString(user));
        BaseResp add = UserService.add(user);
        log.info("onMessage. user:add resp:{}", JSON.toJSONString(add));
    }
}
