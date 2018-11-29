package com.test.demo.configuration.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.*;

@Configuration
public class RedisClusterConfig {

    @Value("${spring.redis.cluster.nodes}")
    private String redisNode;

    @Bean
    public JedisCluster getJedisCluster() {
        Set<HostAndPort> nodes = new HashSet<>();
        for(String node: redisNode.split(",")) {
            String[] arr=node.split(":");
            nodes.add(new HostAndPort(arr[0], Integer.parseInt(arr[1])));
        }
        return new  JedisCluster(nodes);
    }
}
