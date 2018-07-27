package com.test.demo.utils;

import org.springframework.web.client.RestTemplate;

public class HttpUtil {

    public static String post(String url, Object data){
        return new RestTemplate().postForEntity(url,data, String.class).getBody();
    }

    public static String get(String url){
        return new RestTemplate().getForEntity(url, String.class).getBody();
    }

    public static void put(String url, Object data,Object placeholder){
        new RestTemplate().put(url,data,placeholder);
    }

    public static void delete(String url,Object placeholder){
        new RestTemplate().delete(url,placeholder);
    }
}

