package com.light.consumer.api;

import com.light.dto.BasePageReq;
import com.light.dto.BaseResp;
import com.light.consumer.dto.User;
import com.light.consumer.dto.UserListResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "light-producer-service", contextId = "producerFeignApi")
@Api(tags = "提供方")
public interface ProducerFeignApi {

    @ApiOperation(value = "hello")
    @GetMapping(value = "/hello")
    String hello(@RequestParam("name") String name);

    @ApiOperation(value = "用户列表")
    @ResponseBody
    @PostMapping(value = "/user/list", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    UserListResp list(@RequestBody BasePageReq<User> req);

    @ApiOperation(value = "添加用户")
    @ResponseBody
    @PostMapping(value = "/user/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResp add(@RequestBody User user);

}
