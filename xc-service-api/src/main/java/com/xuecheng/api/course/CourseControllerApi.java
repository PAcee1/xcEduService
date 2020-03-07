package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: Pace
 * @Data: 2020/2/25 21:53
 * @Version: v1.0
 */
@Api(value = "课程管理接口",description = "课程管理，查询接口")
public interface CourseControllerApi {
    @ApiOperation("课程计划查询")
    TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("查询课程列表")
    QueryResponseResult<CourseInfo> findCourseList(int page,
                                                   int size,
                                                   CourseListRequest courseListRequest);

    @ApiOperation("添加课程")
    AddCourseResult addCourse(CourseBase courseBase);

    @ApiOperation("根据id查询课程")
    CourseBase getCourseBaseById(String courseId);

    @ApiOperation("更新课程")
    ResponseResult updateCourseBase(CourseBase courseBase);

    @ApiOperation("根据id查询课程营销信息")
    CourseMarket getCourseMarketById(String courseId);

    @ApiOperation("修改课程营销信息")
    ResponseResult updateCourseMarket(CourseMarket courseMarket);

    @ApiOperation("添加课程图片")
    ResponseResult addCoursePic(String courseId,
                                String pic);

    @ApiOperation("查询课程图片")
    CoursePic findCoursePic(String courseId);

    @ApiOperation("删除课程图片")
    ResponseResult deleteCoursePic(String courseId);

    @ApiOperation("课程视图查询")
    CourseView findCourseView(String id);

    @ApiOperation("添加课程计划绑定的视频信息")
    ResponseResult saveTeachplanMedia(TeachplanMedia teachplanMedia);
}
