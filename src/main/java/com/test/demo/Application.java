package com.test.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
@MapperScan("com.test.demo.mapper")
@RestController
@EnableEurekaClient
//@EnableEurekaServer
@EnableFeignClients
public class Application {
    private static ConfigurableApplicationContext context;
    public static void main(String[] args) {
        context = SpringApplication.run(Application.class, args);
    }

    @RequestMapping("/close")
    private void close(){
        Application.context.close();
    }
}
