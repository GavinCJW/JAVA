package com.test.demo.service;

import com.test.demo.configuration.datasource.DataSource;
import com.test.demo.configuration.datasource.DataSourceKey;
import com.test.demo.configuration.Watch.Watch;
import com.test.demo.mapper.UserMapper;
import com.test.demo.model.User;
import com.test.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Resource
    private UserMapper _mapper;

    @Resource
    private UserRepository _repository;
    @Watch
    @DataSource(DataSourceKey.DB1)
    public List<Map<String,Object>> select(){
        return _mapper.select();
    }
    @Watch
    @DataSource(DataSourceKey.DB1)
    public List<User> get() {
        return _mapper.get();
    }
    @Watch
    public List<User> findAllBy(){
        return _repository.findAll();
    }

    public void insert(User user){
        _mapper.insert(user);
    }

    public void delete(String id){ _repository.deleteById(id);}
}
