package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPublishPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: Pace
 * @Data: 2020/2/29 17:13
 * @Version: v1.0
 */
@FeignClient(value = "XC-SERVICE-MANAGE-CMS")
public interface CmsPageClient {

    // 根据id查询页面，Feign远程调用
    @GetMapping("/cms/page/get/{id}")
    CmsPage findCmsPageById(@PathVariable("id") String id);

    // 保存课程到CMSPage
    @PostMapping("/cms/page/save")
    CmsPageResult saveCmsPage(@RequestBody CmsPage cmsPage);

    // 一键发布课程
    @PostMapping("/cms/page/publishPageQuick")
    CmsPublishPageResult publishPageQuick(@RequestBody CmsPage cmsPage);
}
