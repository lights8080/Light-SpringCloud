package com.light;


import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class Application {
    public static void main(String[] args) {
        ApplicationContext ac = SpringApplication.run(Application.class, args);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                NacosAutoServiceRegistration nacosAutoServiceRegistration = ac.getBean(NacosAutoServiceRegistration.class);
                if (nacosAutoServiceRegistration != null) {
                    nacosAutoServiceRegistration.stop();
                }
            }
        });
    }
}

