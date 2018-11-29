package com.test.demo.controller;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.test.demo.model.User;
import com.test.demo.service.TestService;
import com.test.demo.service.UserService;
import com.test.demo.utils.CglibUtil;
import com.test.demo.utils.HttpUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

@RestController
@EnableAutoConfiguration
@Api(value = "TestController", description = "测试专用接口")
@RequestMapping("test")
public class TestController extends HttpServlet {

    @Resource
    private UserService user_service;

    @Value("${jwt.key}")
    private String jwt_key;

    @ApiOperation(value="查询mybatis", notes="查询mybatis接口")
    @ApiImplicitParams({})
    @GetMapping("mybatis")
    private List<User> mybatis(){
        return user_service.get();
    }

    @GetMapping("jpa")
    private List<User> jpa(){
        return user_service.findAllBy();
    }

    @GetMapping("test")
    private void test(HttpServletRequest request){
        /*HttpSession session=request.getSession();
        session.invalidate();*/
        System.out.println(HttpUtil.get("http://www.baidu.com"));
    }

    @DeleteMapping("{id}")
    private void delete(@PathVariable int id){
        System.out.println(id);
        //_service.delete(id);
    }

    @PostMapping
    private void insert(@RequestParam String id){
        System.out.println(id);
        //_service.insert(user);
    }

    @PutMapping
    private void update(@RequestParam int id){
        System.out.println(id);
    }

    @GetMapping("ttt")
    private List<Map<String,Object>> ttt(){
        return user_service.select();
    }

    @GetMapping("aaa")
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


    @PostMapping("login")
    public String login(@RequestParam String name , @RequestParam String password) throws Exception {
        // Check if username and password is null
        if (name.equals("") || password.equals(""))
            throw new Exception("Please fill in username and password");

        // Check if the username is used
        if(name.equals("123") || password.equals("123")){
            throw new Exception("Please fill in username and password");
        }

        // Create Twt token
        String jwtToken = Jwts.builder().setSubject(name).claim("roles", "member").setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, jwt_key).compact();

        return jwtToken;
    }

    @Autowired
    TestService testService;

    @GetMapping(value = "/hi")
    public String sayHi(@RequestParam String name){
        return testService.sayHiFromClientOne(name);
    }
}
