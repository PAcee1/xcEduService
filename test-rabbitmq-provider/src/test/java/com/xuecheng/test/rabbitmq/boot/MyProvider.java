package com.xuecheng.test.rabbitmq.boot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: Pace
 * @Data: 2020/2/22 19:43
 * @Version: v1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MyProvider {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendMsg(){
        // 发送消息
        String exchangeName = "exchange_topics_boot";
        String msg = "send message with springboot";
        rabbitTemplate.convertAndSend(exchangeName,"boot.msg",msg);
    }
}
