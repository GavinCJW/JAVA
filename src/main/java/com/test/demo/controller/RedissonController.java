package com.test.demo.controller;

import io.swagger.annotations.Api;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@RestController
@EnableAutoConfiguration
@Api(value = "RedissonController", description = "redisson接口")
@RequestMapping("redisson")
public class RedissonController {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("lockAdd")
    public void lockAdd() throws Exception
    {
        RLock lock = redissonClient.getLock("key");
        try {
            if (lock.tryLock(0, 10, TimeUnit.SECONDS)) {
                System.out.println("拿到锁了: ");
                //校验分布式锁时不手动释放，等待10秒超时后自动释放
                //lock.unlock();
            }else{
                System.out.println("Redisson分布式锁没有获得锁:"+lock +",ThreadName:"+Thread.currentThread().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
