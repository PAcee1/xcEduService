package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * @Author: Pace
 * @Data: 2020/2/21 20:56
 * @Version: v1.0
 */
@Controller
public class CmsPagePreviewController extends BaseController {

    @Autowired
    private CmsPageService cmsPageService;

    @GetMapping("/cms/preview/{pageId}")
    public void preview(@PathVariable String pageId) throws IOException {
        // 获取Header中的JWT
        String jwt = getJwtFromHeader();
        String pageHtml = cmsPageService.getPageHtml(pageId,jwt);
        // 输出页面
        // 必须设置请求头为html, nginx才能解析ssi，否则nginx不解析
        response.setHeader("Content-type","text/html;charset=utf-8");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(pageHtml.getBytes("utf-8"));
    }

    private String getJwtFromHeader(){
        String authorization = request.getHeader("Authorization");
        if(StringUtils.isEmpty(authorization)){
            //拒绝访问
            return null;
        }
        if(!authorization.startsWith("Bearer ")){
            //拒绝访问
            return null;
        }
        return authorization;
    }
}
