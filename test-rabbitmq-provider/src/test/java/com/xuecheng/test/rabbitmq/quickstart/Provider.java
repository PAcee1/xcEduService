package com.xuecheng.test.rabbitmq.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Pace
 * @Data: 2020/2/22 15:53
 * @Version: v1.0
 */
public class Provider {

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

        // 4.使用通道声明队列
        /**
         * 参数：
         * 1、queue ： 队列名称
         * 2、durable：是否持久化，即重启后队列还在
         * 3、exclusive：独占连接，即该队列只能被此连接访问，如果连接关闭队列自动删除，如果设置为true即临时队列
         * 4、autoDelete：自动删除，队列不使用时mq自动删除，可与exclusive配合创建临时队列
         * 5、arguments：其他参数，一些扩展参数，比如：队列存活时间，死信队列等
         */
        channel.queueDeclare("helloworld",true,false,false,null);

        // 5.发送消息
        /**
         * 参数：
         * 1、exchange：交换机名称
         * 2、routingKey：路由key，交换机根据路由键转发到指定队列，如果使用默认交换机，路由键为队列名称
         * 3、props：消息属性
         * 4、body：消息内容
         */
        String msg = "hello rabbitmq";
        channel.basicPublish("","helloworld",null,msg.getBytes());

        // 6.关闭连接
        channel.close();
        connection.close();
    }
}
