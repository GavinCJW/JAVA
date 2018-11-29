package com.test.demo.configuration.redisson;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class RedissonConfiguration {
    @Bean
    public Redisson redisson() throws IOException {
        return (Redisson) Redisson.create(Config.fromYAML(new ClassPathResource("redisson.properties").getInputStream()));
    }

}
