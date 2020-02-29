package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsSite;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @Author: Pace
 * @Data: 2020/2/4 11:22
 * @Version: v1.0
 */
@Api(value="cms站点管理接口",description = "cms站点管理接口，提供数据模型的管理、查询接口")
public interface CmsSiteControllerApi {
    @ApiOperation("查询CMS站点列表")
    List<CmsSite> getCmsSiteList();
}
