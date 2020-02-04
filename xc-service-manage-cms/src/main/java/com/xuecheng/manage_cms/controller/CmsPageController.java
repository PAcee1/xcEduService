package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Pace
 * @Data: 2020/2/4 11:36
 * @Version: v1.0
 */
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private CmsPageService cmsPageService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page,
                                        @PathVariable("size") int size,
                                        QueryPageRequest queryPageRequest) {
       /* List<CmsPage> cmsPageList = new ArrayList<>();
        cmsPageList.add(new CmsPage());
        QueryResult<CmsPage> queryResult = new QueryResult<>();
        queryResult.setList(cmsPageList);
        queryResult.setTotal(1);
        QueryResponseResult responseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);*/
        return cmsPageService.findList(page,size,queryPageRequest);
    }
}
