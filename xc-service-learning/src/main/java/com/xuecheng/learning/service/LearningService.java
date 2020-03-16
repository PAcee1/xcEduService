package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.learning.XcLearningCourse;
import com.xuecheng.framework.domain.learning.response.LearningCode;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.client.CourseSearchClient;
import com.xuecheng.learning.dao.XcLearningCourseRepository;
import com.xuecheng.learning.dao.XcTaskHisRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @Author: Pace
 * @Data: 2020/3/8 14:58
 * @Version: v1.0
 */
@Service
public class LearningService {

    @Autowired
    private CourseSearchClient courseSearchClient;
    @Autowired
    private XcLearningCourseRepository xcLearningCourseRepository;
    @Autowired
    private XcTaskHisRepository xcTaskHisRepository;

    /**
     * 获取课程播放地址
     * @param courseId
     * @param teachplanId
     * @return
     */
    public String getMedia(String courseId, String teachplanId) {
        // 校验学生是否有权限观看

        // 查询课程播放地址，通过Feign调用查询服务接口
        TeachplanMediaPub mediaPub = courseSearchClient.getmedia(teachplanId);
        if(mediaPub == null || StringUtils.isEmpty(mediaPub.getMediaUrl())){
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_FAIL);
        }

        return mediaPub.getMediaUrl();
    }

    public ResponseResult addCourse(String userId,
                                    String courseId,
                                    String valid,
                                    Date startTime,
                                    Date endTime,
                                    XcTask xcTask){
        // 参数判断
        if (StringUtils.isEmpty(courseId)) {
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_ERROR);
        }
        if (StringUtils.isEmpty(userId)) {
            ExceptionCast.cast(LearningCode.CHOOSECOURSE_USERISNULL);
        }
        if(xcTask == null || StringUtils.isEmpty(xcTask.getId())){
            ExceptionCast.cast(LearningCode.CHOOSECOURSE_TASKISNULL);
        }

        // 查询
        XcLearningCourse xcLearningCourse = xcLearningCourseRepository.findByCourseIdAndUserId(courseId, userId);
        // 幂等判断，如果有则修改，没有新增
        if(xcLearningCourse != null){
            xcLearningCourse.setValid(valid);
            xcLearningCourse.setStartTime(startTime);
            xcLearningCourse.setEndTime(endTime);
            xcLearningCourse.setStatus("501001");
            xcLearningCourseRepository.save(xcLearningCourse);
        }else {
            xcLearningCourse = new XcLearningCourse();
            xcLearningCourse.setUserId(userId);
            xcLearningCourse.setCourseId(courseId);
            xcLearningCourse.setValid(valid);
            xcLearningCourse.setStartTime(startTime);
            xcLearningCourse.setEndTime(endTime);
            xcLearningCourse.setStatus("501001");
            xcLearningCourseRepository.save(xcLearningCourse);
        }

        // 插入历史表
        Optional<XcTaskHis> optional = xcTaskHisRepository.findById(xcTask.getId());
        if(!optional.isPresent()){
            // 如果不存在，添加，如果存在就不操作了
            XcTaskHis xcTaskHis = new XcTaskHis();
            BeanUtils.copyProperties(xcTask,xcTaskHis);
            xcTaskHisRepository.save(xcTaskHis);
        }

        return new ResponseResult(CommonCode.SUCCESS);
    }
}
