package com.test.demo.controller;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.test.demo.repository.UserRepository;
import com.test.demo.mapper.UserMapper;
import com.test.demo.model.User;
import com.test.demo.utils.CglibUtil;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@EnableAutoConfiguration
public class TestController extends HttpServlet {

    @Resource
    private UserRepository _repository;

    @Resource
    private UserMapper _mapper;

    @RequestMapping("/mybatis")
    private List<User> mybatis(){
        return _mapper.get();
    }

    @RequestMapping("/jpa")
    private List<User> jpa(){
        return _repository.findAllBy();
    }

    @RequestMapping("/test")
    private void test(HttpServletRequest request){
        HttpSession session=request.getSession();
        session.invalidate();

    }

    @RequestMapping("/ttt")
    private List<Map<String,Object>> ttt(){
        return _mapper.get_test();
    }

    @RequestMapping("/aaa")
    private Object aaa()throws ClassNotFoundException{
        HashMap propertyMap = new HashMap();

        propertyMap.put("id", Class.forName("java.lang.Object"));

        propertyMap.put("name", Class.forName("java.lang.Object"));

        propertyMap.put("address", Class.forName("java.lang.Object"));

        CglibUtil bean = new CglibUtil(propertyMap);
        bean.setValue("id", new Integer(123));

        bean.setValue("name", "454");

        bean.setValue("address", "789");

        Class clazz = bean.getObject().getClass();
        Method[] mm = clazz.getDeclaredMethods();
        for (Method m : mm) {
            System.out.println(m.getName());
        }

        Field[] ff = clazz.getDeclaredFields();
        for (Field f : ff) {
            System.out.println(f.getName());
        }

        try{
            Field field = User.class.getDeclaredField("name");
            Excel foo = field.getAnnotation(Excel.class);
            InvocationHandler h = Proxy.getInvocationHandler(foo);
            Field hField = h.getClass().getDeclaredField("memberValues");
            hField.setAccessible(true);
            // 获取 memberValues
            Map memberValues = (Map) hField.get(h);
            // 修改 value 属性值
            memberValues.put("name", "ddd");
            // 获取 foo 的 value 属性值
            String value = foo.name();
            System.out.println(value); // ddd
        }catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(UUID.randomUUID().toString().split("-")[0].trim());

        return bean.getObject();
    }
}
