package com.test.demo.configuration.Watch;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import com.test.demo.utils.StopWatchUtil;

import java.util.Date;

@Aspect
@Component
public class WatchAspect {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    StopWatch watch = new StopWatch();
    @Pointcut("@annotation(Watch)")
    public void doPointCut(){
    }


    @Before("doPointCut()")
    public void doBeforeLog(JoinPoint point) {
        System.out.println("class_method={}"+ point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());

        //param
        System.out.println("args={}"+ point.getArgs());
        watch.start(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
        StopWatchUtil.start();
    }

    @After("doPointCut()")
    public void doAfterLog(JoinPoint point) {
        watch.stop();
        StopWatchUtil.stop();
        System.out.println(StopWatchUtil.getTimeByName());
        StopWatchUtil.clear();
        System.out.println(StopWatchUtil.getTimeByName());
        System.out.println(JSON.toJSON(StopWatchUtil.getAll()));
        System.out.println(watch.prettyPrint());
        System.out.println(watch.getLastTaskName());
        System.out.println(watch.getLastTaskTimeMillis());
        System.out.println(watch.getTotalTimeMillis());
        System.out.println(watch.getTotalTimeSeconds());
    }

    @AfterReturning(returning="ret", pointcut = "doPointCut()")
    public void doAfterReturnLog(Object ret){
        System.out.println(new Date().getTime());
        System.out.println(JSON.toJSONString(ret));
    }
}
