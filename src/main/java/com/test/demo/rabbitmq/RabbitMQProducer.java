package com.test.demo.rabbitmq;

import com.test.demo.configuration.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendFanoutTestQueue(String str){
        rabbitTemplate.convertAndSend("FANOUT_EXCHANGE",
                "", str);
    }

    public void sendDirectTestQueue(String str){
        rabbitTemplate.convertAndSend("DIRECT_EXCHANGE",
                "key", str);
    }

    public void sendTopicTestQueue(String str , String key){
        rabbitTemplate.convertAndSend("TOPIC_EXCHANGE",
                key, str);
    }

    public void sendDelayQueue(String str , String time){
        rabbitTemplate.convertAndSend("DELAY_EXCHANGE","",str,message->{
            message.getMessageProperties().setExpiration(time);
            return message;
        });
    }
}
