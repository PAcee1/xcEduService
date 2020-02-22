package com.xuecheng.test.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Pace
 * @Data: 2020/2/22 19:30
 * @Version: v1.0
 */
@Configuration
public class RabbitmqConfig {

    public static final String EXCHANGE_TOPICS_BOOT = "exchange_topics_boot";
    public static final String QUEUE_TOPICS_BOOT = "queue_topics_boot";
    public static final String ROUTINGKEY_BOOT = "boot.#";

    // 声明交换机
    @Bean(EXCHANGE_TOPICS_BOOT)
    public Exchange exchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_BOOT).durable(true).build();
    }

    // 声明队列
    @Bean(QUEUE_TOPICS_BOOT)
    public Queue queue(){
        return new Queue(QUEUE_TOPICS_BOOT);
    }

    // 声明绑定关系
    @Bean
    public Binding binding(@Qualifier(EXCHANGE_TOPICS_BOOT) Exchange exchange,
                           @Qualifier(QUEUE_TOPICS_BOOT) Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_BOOT).noargs();
    }
}
