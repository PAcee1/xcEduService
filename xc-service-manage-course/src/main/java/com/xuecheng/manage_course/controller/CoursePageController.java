package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CoursePageControllerApi;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.service.CoursePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Pace
 * @Data: 2020/3/1 17:19
 * @Version: v1.0
 */
@RestController
@RequestMapping("/coursepage")
public class CoursePageController implements CoursePageControllerApi {

    @Autowired
    private CoursePageService coursePageService;

    @Override
    @PostMapping("/preview/{id}")
    public CoursePublishResult coursePreview(@PathVariable("id") String courseId) {
        String previewUrl = coursePageService.coursePreview(courseId);
        return new CoursePublishResult(CommonCode.SUCCESS,previewUrl);
    }
}
