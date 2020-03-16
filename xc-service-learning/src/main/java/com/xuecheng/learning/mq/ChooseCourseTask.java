package com.xuecheng.learning.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.config.RabbitMQConfig;
import com.xuecheng.learning.service.LearningService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: Pace
 * @Data: 2020/3/16 16:38
 * @Version: v1.0
 */
@Component
public class ChooseCourseTask {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private LearningService learningService;

    @RabbitListener(queues = RabbitMQConfig.XC_LEARNING_ADDCHOOSECOURSE)
    public void receiveCourseTask(XcTask xcTask){
        // 取requestBody信息
        String requestBody = xcTask.getRequestBody();
        Map map = JSON.parseObject(requestBody, Map.class);
        String userId = (String) map.get("userId");
        String courseId = (String) map.get("courseId");
        String valid = (String) map.get("valid");
        // TODO 取startTime，endTime等

        // 保存课程
        ResponseResult responseResult = learningService.addCourse(userId, courseId, valid, null, null, xcTask);

        if(responseResult.isSuccess()){
            // 保存成功 发消息
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE,
                    RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE_KEY,xcTask);
        }
    }
}
