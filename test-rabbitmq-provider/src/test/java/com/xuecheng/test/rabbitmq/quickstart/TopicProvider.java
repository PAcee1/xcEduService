package com.xuecheng.test.rabbitmq.quickstart;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Topic交换机
 * @Author: Pace
 * @Data: 2020/2/22 17:01
 * @Version: v1.0
 */
public class TopicProvider {

    // Topic交换机，一个交换机可以绑定多个队列，发布消息时 交换机会根据RoutingKey转发消息到对应的队列上
    // 与Routing，Direct交换机不同的是，这个RoutingKey可以使用通配符

    public static void main(String[] args) throws IOException, TimeoutException {
        // 1.创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        // 2.建立连接
        Connection connection = connectionFactory.newConnection();

        // 3.建立通道
        Channel channel = connection.createChannel();

        // 声明交换机
        String exchangeName = "exchange_topic";
        channel.exchangeDeclare(exchangeName,BuiltinExchangeType.TOPIC);
        // 声明需要监听的队列
        String queueName = "queue_topic";
        channel.queueDeclare(queueName,true,false,false,null);
        // 交换机与队列绑定
        String routingKey = "xc.#";
        channel.queueBind(queueName,exchangeName,routingKey);

        // 5.发送消息
        for (int i = 0; i < 5; i++) {
            String msg = "topic message " + i;
            if(i % 2 == 0){
                channel.basicPublish(exchangeName,"xc.email",null,msg.getBytes());
            }else {
                channel.basicPublish(exchangeName,"xc.sms",null,msg.getBytes());
            }
        }

        channel.close();
        connection.close();
    }
}
