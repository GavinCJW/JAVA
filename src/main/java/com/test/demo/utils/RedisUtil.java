package com.test.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //指定缓存失效时间
    public boolean expire(String key,long time){
        try {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //获取过期时间
    public long getExpire(String key){
        return redisTemplate.getExpire(key,TimeUnit.SECONDS);
    }

    //判断key是否存在
    public boolean hasKey(String key){
        if(key.isEmpty())
            return false;
        return redisTemplate.hasKey(key);
    }

    //删除缓存
    public void del(String ... keys){
        for (String key : keys)
            redisTemplate.delete(key);
    }

    /********************************************************/
    //string
    public ValueOperations<String, Object> ValueOperations(){
        return redisTemplate.opsForValue();
    }

    //map
    public HashOperations<String, String, Object> HashOperations(){
        return redisTemplate.opsForHash();
    }

    //set
    public SetOperations<String, Object> SetOperations(){
        return redisTemplate.opsForSet();
    }

    //zset
    public ZSetOperations<String, Object> ZSetOperations(){
        return redisTemplate.opsForZSet();
    }

    //list
    public ListOperations<String, Object> ListOperations(){
        return redisTemplate.opsForList();
    }


}
