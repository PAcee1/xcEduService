package com.xuecheng.manage_cms_client.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Pace
 * @Data: 2020/2/23 15:01
 * @Version: v1.0
 */
@Configuration
public class RabbitmqConfig {

    //交换机的名称
    public static final String EX_ROUTING_CMS_POSTPAGE="ex_routing_cms_postpage";

    // 门户队列的名称
    @Value("${xuecheng.mq.queue1}")
    public  String queue_cms_postpage_name1;
    // 门户routingKey 即站点Id
    @Value("${xuecheng.mq.routingKey1}")
    public  String routingKey1;
    // 课程详情队列的名称
    @Value("${xuecheng.mq.queue3}")
    public  String queue_cms_postpage_name3;
    // 课程详情routingKey 即站点Id
    @Value("${xuecheng.mq.routingKey3}")
    public  String routingKey3;
    /**
     * 交换机配置使用direct类型
     * @return the exchange
     */
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange EXCHANGE_TOPICS_INFORM() {
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }

    //声明门户队列
    @Bean("queue_cms_postpage1")
    public Queue QUEUE_CMS_POSTPAGE1() {
        Queue queue = new Queue(queue_cms_postpage_name1);
        return queue;
    }

    /**
     * 绑定门户队列到交换机
     *
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding BINDING_QUEUE_INFORM_SMS1(@Qualifier("queue_cms_postpage1") Queue queue,
                                            @Qualifier(EX_ROUTING_CMS_POSTPAGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey1).noargs();
    }

    //声明课程详情队列
    @Bean("queue_cms_postpage3")
    public Queue QUEUE_CMS_POSTPAGE3() {
        Queue queue = new Queue(queue_cms_postpage_name3);
        return queue;
    }

    /**
     * 绑定课程详情队列到交换机
     *
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding BINDING_QUEUE_INFORM_SMS3(@Qualifier("queue_cms_postpage3") Queue queue,
                                            @Qualifier(EX_ROUTING_CMS_POSTPAGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey3).noargs();
    }
}
