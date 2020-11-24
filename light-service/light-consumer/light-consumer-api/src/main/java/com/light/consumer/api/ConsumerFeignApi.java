package com.light.consumer.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "light-consumer-service", contextId = "consumerFeignApi")
@Api(tags = "消费方")
public interface ConsumerFeignApi {

    @ApiOperation(value = "hello")
    @GetMapping(value = "/hello")
    String hello(@RequestParam("name") String name);
}
