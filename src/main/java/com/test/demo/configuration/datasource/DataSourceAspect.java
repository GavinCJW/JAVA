package com.test.demo.configuration.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DataSourceAspect {
    @Pointcut("@annotation(DataSource)")
    public void doPointCut(){

    }

    @Before("doPointCut()")
    public void doBeforeDataSource(JoinPoint point) {
        try {
            Method method = point.getTarget().getClass().getMethod
                    (point.getSignature().getName(), ((MethodSignature)point.getSignature()).getParameterTypes());
            if (method.isAnnotationPresent(DataSource.class)) {
                DataSourceContextHolder.setDB(method.getAnnotation(DataSource.class).value());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After("doPointCut()")
    public void doAfterDataSource(JoinPoint point) {
        DataSourceContextHolder.clearDB();
    }
}
