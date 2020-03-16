package com.xuecheng.learning.dao;

import com.xuecheng.framework.domain.learning.XcLearningCourse;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Pace
 * @Data: 2020/3/16 16:21
 * @Version: v1.0
 */
public interface XcLearningCourseRepository extends JpaRepository<XcLearningCourse,String> {

    // 根据课程id和用户查询
    XcLearningCourse findByCourseIdAndUserId(String courseId,String userId);
}
