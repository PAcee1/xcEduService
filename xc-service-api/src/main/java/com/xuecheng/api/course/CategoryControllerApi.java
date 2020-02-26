package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: Pace
 * @Data: 2020/2/25 21:53
 * @Version: v1.0
 */
@Api(value = "课程分类管理接口",description = "课程分类，查询接口")
public interface CategoryControllerApi {

    @ApiOperation("查询课程分类列表")
    CategoryNode findCategoryList();
}
