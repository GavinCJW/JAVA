package com.test.demo.configuration.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(-10)//保证该AOP在@Transactional之前执行
@Component
public class DynamicDataSourceAspect {
    @Before("@annotation(tds)")
    public void changeDataSource(JoinPoint point, TargetDataSource tds) throws Throwable {
        //获取当前的指定的数据源;
        String dsId = tds.value();
        //如果不在我们注入的所有的数据源范围之内，那么输出警告信息，系统自动使用默认的数据源。
        if (!DynamicDataSourceContextHolder.containsDataSource(dsId)) {
            System.err.println("数据源[{}]不存在，使用默认数据源 > {}"+tds.value()+point.getSignature());
        } else {
            System.out.println("Use DataSource : {} > {}"+tds.value()+point.getSignature());
            //找到的话，那么设置到动态数据源上下文中。
            DynamicDataSourceContextHolder.setDataSourceType(tds.value());
        }
    }



    @After("@annotation(tds)")
    public void restoreDataSource(JoinPoint point, TargetDataSource tds) {
        System.out.println("Revert DataSource : {} > {}"+tds.value()+point.getSignature());
        //方法执行完毕之后，销毁当前数据源信息，进行垃圾回收。
        DynamicDataSourceContextHolder.clearDataSourceType();

    }
}
