package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Pace
 * @Data: 2020/2/29 20:47
 * @Version: v1.0
 */
@Data
@NoArgsConstructor
public class CourseView implements Serializable {

    CourseBase courseBase; // 课程基础信息
    CourseMarket courseMarket; // 课程营销信息
    CoursePic coursePic; // 课程图片信息
    TeachplanNode teachplanNode; // 教学计划

}
