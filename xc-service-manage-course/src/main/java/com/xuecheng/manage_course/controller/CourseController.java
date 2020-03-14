package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Pace
 * @Data: 2020/2/25 22:06
 * @Version: v1.0
 */
@RestController
@RequestMapping("/course")
public class CourseController extends BaseController implements CourseControllerApi {

    @Autowired
    private CourseService courseService;

    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable String courseId) {
        return courseService.findTeachListById(courseId);
    }

    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        courseService.addTeachplan(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult<CourseInfo> findCourseList(@PathVariable int page,
                                                          @PathVariable int size,
                                                          CourseListRequest courseListRequest) {
        // 先从请求头的JWT中获取id
        XcOauth2Util oauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwtFromHeader = oauth2Util.getUserJwtFromHeader(request);
        String companyId = userJwtFromHeader.getCompanyId();
        QueryResult<CourseInfo> queryResult = courseService.findCourseList(page,size,companyId,courseListRequest);
        QueryResponseResult<CourseInfo> queryResponseResult = new QueryResponseResult<>(
                CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    @Override
    @PostMapping("/coursebase/add")
    public AddCourseResult addCourse(@RequestBody CourseBase courseBase) {
        String courseId = courseService.addCourse(courseBase);
        return new AddCourseResult(CommonCode.SUCCESS,courseId);
    }

    @Override
    @GetMapping("/coursebase/get/{courseId}")
    public CourseBase getCourseBaseById(@PathVariable String courseId) {
        return courseService.getCourseBaseById(courseId);
    }

    @Override
    @PutMapping("/coursebase/update")
    public ResponseResult updateCourseBase(@RequestBody CourseBase courseBase) {
        courseService.updateCourseBase(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    @GetMapping("/coursemarket/get/{courseId}")
    public CourseMarket getCourseMarketById(@PathVariable String courseId) {
        return courseService.getCourseMarketById(courseId);
    }

    @Override
    @PutMapping("/coursemarket/update")
    public ResponseResult updateCourseMarket(@RequestBody CourseMarket courseMarket) {
        courseService.updateCourseMarket(courseMarket);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    @PostMapping("/coursepic/add")
    public ResponseResult addCoursePic(String courseId, String pic) {
        courseService.addCoursePic(courseId,pic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @PreAuthorize("hasAuthority('course_find_pic')")
    @Override
    @GetMapping("/coursepic/get/{courseId}")
    public CoursePic findCoursePic(@PathVariable String courseId) {
        return courseService.findCoursePic(courseId);
    }

    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteCoursePic(String courseId) {
        long result = courseService.deleteCoursePic(courseId);
        if(result > 0){
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    @Override
    @GetMapping("/courseview/{id}")
    public CourseView findCourseView(@PathVariable String id) {
        return courseService.findCourseView(id);
    }

    @Override
    @PostMapping("/savemedia")
    public ResponseResult saveTeachplanMedia(@RequestBody TeachplanMedia teachplanMedia) {
        courseService.saveTeachplanMedia(teachplanMedia);
        return new ResponseResult(CommonCode.SUCCESS);
    }

}
