package com.test.demo.configuration.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQDelayConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 当交换机无法正确投递消息的时候，RabbitMQ会调用Basic.Return命令将消息返回给生产者
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause)
                -> System.out.println("消息发送成功:correlationData({"+correlationData+"}),ack({"+ack+"}),cause({"+cause+"})"));
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey)
                ->System.out.println("消息丢失:exchange({"+exchange+"}),route({"+routingKey+"}),replyCode({"+replyCode+"}),replyText({"+replyText+"}),message:{"+message+"}"));
        return rabbitTemplate;
    }

    @Bean
    public Queue delayQueue() {
        return QueueBuilder
                .durable("DELAY_QUEUE")
                .withArgument("x-dead-letter-exchange", "DEAD_LETTER_EXCHANGE") //到期后转发的交换机
                .withArgument("x-dead-letter-routing-key", "leeter") // 到期后转发的路由键
                .build();
    }

    @Bean
    public FanoutExchange delayExchange() {
        return new FanoutExchange("DELAY_EXCHANGE");
    }

    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange());
    }

    @Bean
    public Queue dlxQueue() {
        return new Queue("DEAD_LETTER_QUEUE", true);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange("DEAD_LETTER_EXCHANGE");
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with("leeter");
    }

}
