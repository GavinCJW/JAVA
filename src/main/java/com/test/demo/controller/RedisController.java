package com.test.demo.controller;

import com.test.demo.utils.RedisUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import static org.springframework.util.ObjectUtils.isEmpty;


@RestController
@EnableAutoConfiguration
@Api(value = "RedisController", description = "redis接口")
@RequestMapping("redis")
public class RedisController {

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private JedisCluster jedisCluster;

    @GetMapping("test")
    private String test(){
        jedisCluster.set("str", "test");
        return jedisCluster.get("str");
    }

    @PostMapping("setString")
    private boolean setString(@RequestParam String key , @RequestParam String value , @RequestParam long time){
        try {
            if(!isEmpty(time) && time>0){
                redisUtil.ValueOperations().set(key, value, time, TimeUnit.SECONDS);
            }else{
                redisUtil.ValueOperations().set(key,value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("getString")
    private Object getString(@RequestParam String key){
        return redisUtil.ValueOperations().get(key);
    }

    @PostMapping("setMap")
    private boolean setMap(){
        try {
            redisUtil.HashOperations().putAll("map", new HashMap<String,String>(){
                 {
                     put("1","a");
                     put("2","b");
                     put("q","c");
                };
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("getMap")
    private Object getMap(@RequestParam String key){

        return redisUtil.HashOperations().entries(key);
    }

}
