package com.test.demo.controller;

import com.test.demo.utils.ZookeeperUtil;
import io.swagger.annotations.Api;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.function.Function;

@RestController
@EnableAutoConfiguration
@Api(value = "ZookeeperController", description = "zookeeper接口")
@RequestMapping("zookeeper")
public class ZookeeperController {

    @Resource
    private ZookeeperUtil zookeeperUtil;

    @GetMapping("create")
    @ResponseBody
    public void create(){
        zookeeperUtil.createPersistentNode("/sss","aaaaaaa");
    }

    @GetMapping("set")
    @ResponseBody
    public void set(){
        zookeeperUtil.updateNode("/sss","123456");
    }

    @GetMapping("get")
    @ResponseBody
    public void get(){
        System.out.println(zookeeperUtil.getData("/sss", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent.getPath());
                System.out.println(watchedEvent.getState());
                System.out.println(watchedEvent.getType());
            }
        }));
    }

    @GetMapping("del")
    @ResponseBody
    public void del(){
        zookeeperUtil.deleteNode("/str");
    }

    @GetMapping("lock")
    @ResponseBody
    public void lock(){
        zookeeperUtil.lock(5,1);
    }

    @GetMapping("unlock")
    @ResponseBody
    public void unlock(){
        zookeeperUtil.unlock();
    }

    @GetMapping("DistributedLock")
    @ResponseBody
    public void DistributedLock(){
        zookeeperUtil.getDistributedLock(10, 10, new Function<Object, Object>() {
            @Override
            public Object apply(Object o) {
                System.out.println(o);
                return o;
            }
        });
    }

}
