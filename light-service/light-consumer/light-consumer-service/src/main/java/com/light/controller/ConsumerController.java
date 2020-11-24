package com.light.controller;

import com.light.consumer.api.ConsumerFeignApi;
import com.light.consumer.api.ProducerFeignApi;
import com.light.consumer.dto.User;
import com.light.dto.BaseResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class ConsumerController implements ConsumerFeignApi {

    @Autowired
    private ProducerFeignApi producerFeignApi;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    @ResponseBody
    @GetMapping(value = "/hello")
    public String hello(@RequestParam(value = "name", required = false) String name) {
        return "hello: light-consumer. name:" + name + " -> " + producerFeignApi.hello(name);
    }

    @ResponseBody
    @PostMapping(value = "/user/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResp add(@RequestBody User user) {
        rocketMQTemplate.convertAndSend("light-user:add", user);
        return new BaseResp(0,"success");
    }

}
