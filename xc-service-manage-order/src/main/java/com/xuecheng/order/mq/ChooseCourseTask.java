package com.xuecheng.order.mq;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.config.RabbitMQConfig;
import com.xuecheng.order.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @Author: Pace
 * @Data: 2020/3/16 15:03
 * @Version: v1.0
 */
@Component
@Slf4j
public class ChooseCourseTask {

    @Autowired
    private TaskService taskService;

    /**
     * 监听完成选课队列
     * @param xcTask
     */
    @RabbitListener(queues = RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE)
    public void receiveFinish(XcTask xcTask){
        if(xcTask != null && StringUtils.isNotEmpty(xcTask.getId())){
            // 保存到历史表，删除任务表
            taskService.saveHisAndDeleteTask(xcTask.getId());
        }
    }

    /**
     * 定时扫描任务表，并发布消息
     */
    // @Scheduled(cron = "0 0/1 * * * *") // 正式环境 1分钟
    @Scheduled(cron = "0/3 * * * * *") // 测试 3秒
    public void sendChooseCourseTask(){
        // 取一分钟之前的任务
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(GregorianCalendar.MINUTE,-1);// 减一分钟
        Date time = calendar.getTime();

        // 取任务
        List<XcTask> xcTaskList = taskService.findXcTask(100, time);// 取100条

        // 循环发消息
        for(XcTask xcTask: xcTaskList){
            // 先进行乐观锁控制，如果修改成功，说明获取到资源，进行发送
            // 修改失败，说明此任务已经被控制
            if(taskService.versionControl(xcTask.getId(),xcTask.getVersion()) > 0) {
                // 交换机名称和路由键从对象中取
                String exchange = xcTask.getMqExchange();
                String routingkey = xcTask.getMqRoutingkey();
                taskService.sendMessage(exchange, routingkey, xcTask);
                log.info("ChooseCourseTask send message taskId :{}", xcTask.getId());
            }
        }
        System.out.println(xcTaskList);
    }

}
