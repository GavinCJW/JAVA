package com.test.demo.utils;

import java.util.*;

public class StopWatchUtil {
    private static Map<String,Long> stopWatchPool = new HashMap<>();

    public static void start(){
        start(null);
    }

    public static void start(String str){
        stopWatchPool.put(str,new Date().getTime());
    }

    public static void stop(){
        stop(null);
    }

    public static void stop(String str){
        stopWatchPool.put(str,new Date().getTime() - stopWatchPool.get(str));
    }

    public static Map<String,Long> getAll(){
        return stopWatchPool;
    }

    public static Long getTimeByName(){
        return getTimeByName(null);
    }

    public static Long getTimeByName(String str){
        return stopWatchPool.get(str);
    }

    public static Set<String> getAllName(){
        return stopWatchPool.keySet();
    }

    public static void clear(){
        clear(null);
    }

    public static void clear(String str){
        stopWatchPool.remove(str);
    }

    public static void clearAll(){
        stopWatchPool.clear();
    }
}
