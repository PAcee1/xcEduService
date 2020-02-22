package com.xuecheng.test.rabbitmq.quickstart;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Pace
 * @Data: 2020/2/22 16:09
 * @Version: v1.0
 */
public class RoutingSmsConsumer {

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

        // 4.声明需要监听的队列
        String queueName = "queue_routing_sms";
        channel.queueDeclare(queueName,true,false,false,null);
        // 5.创建消费者
        Consumer callback = new DefaultConsumer(channel){
            /**
             * 接收到消息后执行的方法，即回调方法
             * @param consumerTag 消费者标签，用来标识消费者
             * @param envelope  信封，通过envelope可以获取
             * @param properties 消息的属性，发送消息时设置的那些属性
             * @param body 消息内容
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 获取交换机
                String exchange = envelope.getExchange();
                // 获取消息ID，可以用于手工确认消息
                long deliveryTag = envelope.getDeliveryTag();
                // 消息内容
                String msg = new String(body,"utf-8");
                System.out.println("收取消息：" + msg);
            }
        };

        // 5.监听队列
        /**
         * 参数
         * 1、queue：监听队列名
         * 2、autoAck：是否自动回复Ack，即告诉mq你已经成功收到消息了，如果设置false，必须手动返回ack
         * 3、callback：回调方法
         */
        channel.basicConsume(queueName,true,callback);
    }
}
