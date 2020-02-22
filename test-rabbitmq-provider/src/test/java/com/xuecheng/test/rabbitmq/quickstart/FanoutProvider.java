package com.xuecheng.test.rabbitmq.quickstart;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Fanout交换机
 * @Author: Pace
 * @Data: 2020/2/22 17:01
 * @Version: v1.0
 */
public class FanoutProvider {

    // Fanout交换机，一个交换机可以绑定多个队列，发布消息时所有队列都会接收到，和RoutingKey无关

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

        // 4.建立交换机
        String exchangeName = "exchange_fanout";
        channel.exchangeDeclare(exchangeName,BuiltinExchangeType.FANOUT);
        // 5.建立两个队列
        String queue01Name = "queue_fanout_email";
        String queue02Name = "queue_fanout_sms";
        channel.queueDeclare(queue01Name,true,false,false,null);
        channel.queueDeclare(queue02Name,true,false,false,null);

        // 6.建立交换机与队列绑定关系
        channel.queueBind(queue01Name,exchangeName,""); // 与RoutingKey无关，所以设置为空
        channel.queueBind(queue02Name,exchangeName,""); // 与RoutingKey无关，所以设置为空

        // 7.发送消息
        for (int i = 0; i < 5; i++) {
            String msg = "fanout message " + i;
            // 这里routingKey设置为123，故意设置的不一样
            channel.basicPublish(exchangeName,"123",null,msg.getBytes());
        }

        channel.close();
        connection.close();
    }
}
