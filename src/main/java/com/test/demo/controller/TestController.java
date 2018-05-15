package com.test.demo.controller;

import com.test.demo.repository.UserRepository;
import com.test.demo.mapper.UserMapper;
import com.test.demo.model.User;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@EnableAutoConfiguration
public class TestController extends HttpServlet {

    @Resource
    UserRepository _repository;

    @Resource
    UserMapper _mapper;

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
        HttpSession session=request.getSession();;
        session.invalidate();

    }
}
