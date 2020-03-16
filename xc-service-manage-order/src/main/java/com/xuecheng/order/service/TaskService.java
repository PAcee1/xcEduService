package com.xuecheng.order.service;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.order.dao.XcTaskHisRepository;
import com.xuecheng.order.dao.XcTaskRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author: Pace
 * @Data: 2020/3/16 14:57
 * @Version: v1.0
 */
@Service
public class TaskService {

    @Autowired
    private XcTaskRepository xcTaskRepository;
    @Autowired
    private XcTaskHisRepository xcTaskHisRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 分页查询任务表任务
     * @param size
     * @param updateTime
     * @return
     */
    public List<XcTask> findXcTask(int size, Date updateTime){
        // 设置分页
        Pageable pageable = new PageRequest(0,size);
        Page<XcTask> all = xcTaskRepository.findByUpdateTimeBefore(pageable, updateTime);
        return all.getResult();
    }

    /**
     * 发送消息
     * @param exchangeName
     * @param routingKey
     * @param xcTask
     */
    public void sendMessage(String exchangeName,
                            String routingKey,
                            XcTask xcTask){
        // 取xcTask
        Optional<XcTask> op = xcTaskRepository.findById(xcTask.getId());
        if(op.isPresent()){
            // 存在才发消息
            rabbitTemplate.convertAndSend(exchangeName,routingKey,xcTask);
            // 发完修改updateTime
            XcTask task = op.get();
            task.setUpdateTime(new Date());
            xcTaskRepository.save(task);
        }
    }

    /**
     * 乐观锁控制，防止发送多条重复消息
     * @param id
     * @param version
     * @return
     */
    @Transactional
    public int versionControl(String id,int version){
        return xcTaskRepository.updateTaskVersion(id,version);
    }

    /**
     * 保存到历史任务表，并从任务表删除
     * @param taskId
     */
    public void saveHisAndDeleteTask(String taskId){
        Optional<XcTask> optional = xcTaskRepository.findById(taskId);
        if(optional.isPresent()){
            XcTaskHis xcTaskHis = new XcTaskHis();
            XcTask xcTask = optional.get();
            BeanUtils.copyProperties(xcTask,xcTaskHis);

            // 设置删除时间
            xcTaskHis.setDeleteTime(new Date());

            // 保存删除
            xcTaskHisRepository.save(xcTaskHis);
            xcTaskRepository.delete(xcTask);
        }
    }
}
