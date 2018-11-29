package com.test.demo.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class RabbitMQConsumer {

    @RabbitListener(
            bindings = {
                    @QueueBinding(value = @Queue(value = "FANOUT_QUEUE_NAME1", durable = "true"),
                            exchange = @Exchange(value = "FANOUT_EXCHANGE", type = "fanout"))
            })
    @RabbitHandler
    public void processFanoutMsg(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        System.out.println("received Fanout1 message : " + msg);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            try {//最后一个参数是：是否重回队列
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @RabbitListener(
            bindings = {
                    @QueueBinding(value = @Queue(value = "FANOUT_QUEUE_NAME2", durable = "true"),
                            exchange = @Exchange(value = "FANOUT_EXCHANGE", type = "fanout"))
            })
    @RabbitHandler
    public void processFanout1Msg(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        System.out.println("received Fanout2 message : " + msg);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            try {//最后一个参数是：是否重回队列
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @RabbitListener(
            bindings = {
                    @QueueBinding(value = @Queue(value = "DIRECT_QUEUE_NAME", durable = "true"),
                            exchange = @Exchange(value = "DIRECT_EXCHANGE"),
                            key = "key")
            })
    @RabbitHandler
    public void processDirectMsg(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        System.out.println("received DIRECT message : " + msg);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            try {//最后一个参数是：是否重回队列
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @RabbitListener(
            bindings = {
                    @QueueBinding(value = @Queue(value = "DIRECT_QUEUE_NAME", durable = "true"),
                            exchange = @Exchange(value = "DIRECT_EXCHANGE"),
                            key = "key")
            })
    @RabbitHandler
    public void processDirectMsg2(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        System.out.println("received DIRECT2 message : " + msg);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            try {//最后一个参数是：是否重回队列
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @RabbitListener(
            bindings = {
                    @QueueBinding(value = @Queue(value = "TOPIC_QUEUE_NAME", durable = "true"),
                            exchange = @Exchange(value = "TOPIC_EXCHANGE", type = "topic"),
                            key = "key.*")
            })
    @RabbitHandler
    public void processTopicMsg(Message message, Channel channel) throws IOException{
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        System.out.println(LocalDateTime.now() + "received TOPIC message : " + msg);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            //最后一个参数是：是否重回队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,true);
        }
    }

    @RabbitListener(
            bindings = {
                    @QueueBinding(value = @Queue(value = "DEAD_LETTER_QUEUE", durable = "true"),
                            exchange = @Exchange(value = "DEAD_LETTER_EXCHANGE"),
                            key = "leeter")
            })
    @RabbitHandler
    public void listenerDeadLetterQueue(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        System.out.println("[listenerDelayQueue 监听的消息] - [消费时间] - [{"+ LocalDateTime.now()+"}] - [{"+msg+"}]");
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            try {//最后一个参数是：是否重回队列
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

}