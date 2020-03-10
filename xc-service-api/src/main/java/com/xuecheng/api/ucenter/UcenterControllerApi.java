package com.xuecheng.api.ucenter;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: Pace
 * @Data: 2020/3/10 16:41
 * @Version: v1.0
 */
@Api(value = "用户中心服务",description = "用户增删改查接口")
public interface UcenterControllerApi {

    @ApiOperation("获取用户及扩展信息")
    XcUserExt getUserExt(String username);
}
