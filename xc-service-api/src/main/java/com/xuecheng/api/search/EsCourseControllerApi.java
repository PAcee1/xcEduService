package com.xuecheng.api.search;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: Pace
 * @Data: 2020/3/3 17:54
 * @Version: v1.0
 */
@Api(value = "课程查询接口",description = "课程查询接口，ElasticSearch")
public interface EsCourseControllerApi {

    @ApiOperation("分页查询课程")
    QueryResponseResult<CoursePub> esList(int page,
                                          int size,
                                          CourseSearchParam courseSearchParam);

    @ApiOperation("根据课程ID，查询课程信息")
    CoursePub getDetail(String courseId);

    @ApiOperation("根据课程计划ID，查询播放地址")
    TeachplanMediaPub getmedia(String teachplanId);
}
