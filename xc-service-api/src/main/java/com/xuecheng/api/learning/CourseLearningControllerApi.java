package com.xuecheng.api.learning;

import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: Pace
 * @Data: 2020/3/8 14:54
 * @Version: v1.0
 */
@Api("录播课程学习管理")
public interface CourseLearningControllerApi {

    @ApiOperation("查询录播课程学习地址")
    GetMediaResult getMedia(String courseId,String teachplanId);
}
