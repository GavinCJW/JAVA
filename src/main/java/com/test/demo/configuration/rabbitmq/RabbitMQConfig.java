package com.test.demo.configuration.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue createFanoutQueue1() {
        return new Queue("FANOUT_QUEUE_NAME1");
    }

    @Bean
    public Queue createFanoutQueue2() {
        return new Queue("FANOUT_QUEUE_NAME2");
    }

    @Bean
    public FanoutExchange defFanoutExchange() {
        return new FanoutExchange("FANOUT_EXCHANGE");
    }

    @Bean
    Binding bindingFanout1() {
        return BindingBuilder.bind(createFanoutQueue1()).
                to(defFanoutExchange());
    }

    @Bean
    Binding bindingFanout2() {
        return BindingBuilder.bind(createFanoutQueue2()).
                to(defFanoutExchange());
    }

    @Bean
    public Queue createDirectQueue() {
        return new Queue("DIRECT_QUEUE_NAME");
    }

    @Bean
    DirectExchange directExchange(){
        return new DirectExchange("DIRECT_EXCHANGE");
    }

    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(createDirectQueue()).
                to(directExchange()).
                with("key");
    }

    @Bean
    public Queue createTopicQueue() {
        return new Queue("TOPIC_QUEUE_NAME");
    }

    @Bean
    TopicExchange defTopicExchange(){
        return new TopicExchange("TOPIC_EXCHANGE");
    }

    @Bean
    Binding bindingTopic() {
        return BindingBuilder.bind(createTopicQueue()).
                to(defTopicExchange()).
                with("key.*");
    }
}
