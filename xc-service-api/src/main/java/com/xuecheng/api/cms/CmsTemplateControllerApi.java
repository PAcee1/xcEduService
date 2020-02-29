package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @Author: Pace
 * @Data: 2020/2/4 11:22
 * @Version: v1.0
 */
@Api(value="cms模板管理接口",description = "cms模板管理接口，提供数据模型的管理、查询接口")
public interface CmsTemplateControllerApi {
    @ApiOperation("查询CMS模板列表")
    List<CmsTemplate> getCmsTemplateList();
}
