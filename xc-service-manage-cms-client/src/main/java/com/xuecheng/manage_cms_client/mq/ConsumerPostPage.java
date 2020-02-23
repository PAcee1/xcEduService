package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.manage_cms_client.service.PageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Rabbitmq消费者，监听消息
 * @Author: Pace
 * @Data: 2020/2/23 16:42
 * @Version: v1.0
 */
@Component
public class ConsumerPostPage {

    @Autowired
    private PageService pageService;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg) throws IOException {
        // 将消息解析
        Map map = JSON.parseObject(msg, Map.class);
        // 获取pageId
        String pageId= (String) map.get("pageId");

        // 执行Service方法
        pageService.savePageToServerPath(pageId);
    }
}
