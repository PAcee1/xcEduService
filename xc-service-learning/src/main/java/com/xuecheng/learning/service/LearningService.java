package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.learning.response.LearningCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.learning.client.CourseSearchClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Pace
 * @Data: 2020/3/8 14:58
 * @Version: v1.0
 */
@Service
public class LearningService {

    @Autowired
    private CourseSearchClient courseSearchClient;

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
}
