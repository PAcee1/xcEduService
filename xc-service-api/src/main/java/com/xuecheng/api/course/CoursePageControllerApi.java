package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: Pace
 * @Data: 2020/2/25 21:53
 * @Version: v1.0
 */
@Api(value = "课程页面管理接口",description = "课程页面管理，查询接口")
public interface CoursePageControllerApi {

    @ApiOperation("课程预览")
    CoursePublishResult coursePreview(String courseId);
}
