package com.test.demo.mapper;

import com.test.demo.model.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    List<User> get();

    List<Map<String,Object>> select();

    Long insert(User user);
}
