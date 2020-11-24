package com.light.controller;


import com.light.dto.BasePageReq;
import com.light.dto.BaseResp;
import com.light.consumer.api.ProducerFeignApi;
import com.light.consumer.dto.User;
import com.light.consumer.dto.UserListResp;
import com.light.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class ProducerController implements ProducerFeignApi {

    @Autowired
    private UserService userService;

    @Override
    @ResponseBody
    @GetMapping(value = "/hello")
    public String hello(@RequestParam(value = "name", required = false) String name) {
        return "hello: light-producer. name:" + name;
    }

    @Override
    @ResponseBody
    @PostMapping(value = "/user/list", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserListResp list(@RequestBody BasePageReq<User> req) {
        return userService.list(req);
    }

    @Override
    @ResponseBody
    @PostMapping(value = "/user/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResp add(@RequestBody User user) {
        return userService.add(user);
    }

}
