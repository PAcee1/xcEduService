package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: Pace
 * @Data: 2020/3/5 21:45
 * @Version: v1.0
 */
@Api(value = "媒资管理接口",description = "媒资管理接口，增删改查接口")
public interface MediaFileControllerApi {

    @ApiOperation("查询媒资列表")
    QueryResponseResult<MediaFile> list(int page,
                                              int size,
                                              QueryMediaFileRequest queryMediaFileRequest);
}
