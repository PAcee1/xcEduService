package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: Pace
 * @Data: 2020/2/4 11:22
 * @Version: v1.0
 */
@Api(value="数据字典接口",description = "提供数据字典的管理、查询接口")
public interface SysDictionaryControllerApi {

    @ApiOperation("根据数据字典Type查询字典")
    SysDictionary getSysDictionaryByType(String type);
}
