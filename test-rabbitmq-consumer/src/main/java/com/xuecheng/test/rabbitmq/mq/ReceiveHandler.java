package com.xuecheng.test.rabbitmq.mq;

import com.rabbitmq.client.Channel;
import com.xuecheng.test.rabbitmq.config.RabbitmqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: Pace
 * @Data: 2020/2/22 19:55
 * @Version: v1.0
 */
@Component
public class ReceiveHandler {

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_TOPICS_BOOT})
    public void receiveMsg(String msg, Message message, Channel channel){
        System.out.println(msg);
    }
}
