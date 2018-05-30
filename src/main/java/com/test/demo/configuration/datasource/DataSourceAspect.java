package com.test.demo.configuration.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DataSourceAspect {
    @Before("@annotation(DataSource)")
    public void switchDataSource(JoinPoint point) {
        try {
            Method method = point.getTarget().getClass().getMethod
                    (point.getSignature().getName(), ((MethodSignature)point.getSignature()).getParameterTypes());
            if (method.isAnnotationPresent(DataSource.class)) {
                DataSource annotation = method.getAnnotation(DataSource.class);
                DataSourceContextHolder.setDB(annotation.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After("@annotation(DataSource)")
    public void restoreDataSource(JoinPoint point) {
        DataSourceContextHolder.clearDB();
    }
}
