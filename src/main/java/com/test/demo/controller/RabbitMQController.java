package com.test.demo.controller;

import com.test.demo.rabbitmq.RabbitMQProducer;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@Api(value = "RabbitMQController", description = "消息队列接口")
@RequestMapping("rabbitmq")
public class RabbitMQController {

    @Autowired
    private RabbitMQProducer msgProducer;

    @GetMapping(value = "/sendFanout")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void sendMsg(){
        msgProducer.sendFanoutTestQueue("this is a test fanout message!");
    }

    @GetMapping(value = "/sendDirect")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void sendDirectMsg(){
        msgProducer.sendDirectTestQueue("this is a test direct message!");
    }

    @GetMapping(value = "/sendTopicA")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void sendTopicAMsg(){
        msgProducer.sendTopicTestQueue("this is a test topic aaa message!","key.aaa");
    }

    @GetMapping(value = "/sendTopicB")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void sendTopicBMsg(){
        msgProducer.sendTopicTestQueue("this is a test topic bbb message!","bbb");
    }

    @GetMapping(value = "/sendDelay")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void sendDelayMsg(){
        msgProducer.sendDelayQueue("this is a test delay message!","3000");
    }
}
