package com.light.core.config.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.xml.ws.Response;

/**
 * Swagger2配置类
 *
 * @author lihaipeng
 * @date 2020-09-29
 */
@Configuration(proxyBeanMethods = false)
@EnableSwagger2
@ConditionalOnClass(EnableSwagger2.class)
@ConditionalOnProperty(prefix = "swagger", value = "enabled", havingValue = "true")
public class SwaggerConfig {

    @Value("${spring.cloud.nacos.discovery.metadata.light.version:latest}")
    private String appVersion;

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
//                .enable(enable)
                .apiInfo(apiInfo())
                .genericModelSubstitutes(Response.class)
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(appName)
                .version(appVersion)
                .build();
    }
}