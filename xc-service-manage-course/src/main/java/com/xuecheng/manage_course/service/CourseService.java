package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Pace
 * @Data: 2020/2/25 22:04
 * @Version: v1.0
 */
@Service
public class CourseService {

    @Autowired
    private TeachplanMapper teachplanMapper;

    /**
     * 根据id查询课程计划树
     * @param courseId
     * @return
     */
    public TeachplanNode findTeachListById(String courseId){
        return teachplanMapper.findTeachListById(courseId);
    }
}
