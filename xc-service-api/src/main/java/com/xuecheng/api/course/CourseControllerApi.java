package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
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
}
