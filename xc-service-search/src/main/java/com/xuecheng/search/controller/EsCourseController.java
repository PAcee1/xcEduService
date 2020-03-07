package com.xuecheng.search.controller;

import com.xuecheng.api.search.EsCourseControllerApi;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Pace
 * @Data: 2020/3/3 17:53
 * @Version: v1.0
 */
@RestController
@RequestMapping("/search/course")
public class EsCourseController implements EsCourseControllerApi {

    @Autowired
    private EsCourseService esCourseService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult<CoursePub> esList(@PathVariable int page,
                                                 @PathVariable int size,
                                                 CourseSearchParam courseSearchParam) {
        QueryResult<CoursePub> queryResult = esCourseService.esList(page, size, courseSearchParam);
        return new QueryResponseResult<CoursePub>(CommonCode.SUCCESS,queryResult);
    }

    @Override
    @GetMapping("/getall/{id}")
    public CoursePub getDetail(@PathVariable("id") String courseId) {
        return esCourseService.getDetail(courseId);
    }

    @Override
    @GetMapping("/getmedia/{teachplanId}")
    public TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId) {
        return esCourseService.getmedia(teachplanId);
    }
}
