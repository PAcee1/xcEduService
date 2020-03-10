package com.xuecheng.auth.client;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: Pace
 * @Data: 2020/3/10 16:54
 * @Version: v1.0
 */
@FeignClient(XcServiceList.XC_SERVICE_UCENTER)
public interface UserClient {

    @GetMapping("/ucenter/getUserExt")
    public XcUserExt getUserExt(@RequestParam("username") String username);
}
