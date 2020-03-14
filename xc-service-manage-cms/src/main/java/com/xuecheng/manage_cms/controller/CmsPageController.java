package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPublishPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Pace
 * @Data: 2020/2/4 11:36
 * @Version: v1.0
 */
@RestController
@RequestMapping("/cms/page")
public class CmsPageController extends BaseController implements CmsPageControllerApi {

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

    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return cmsPageService.add(cmsPage);
    }

    @Override
    @GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable String id) {
        return cmsPageService.findById(id);
    }

    @Override
    @PutMapping("/update/{id}")
    public CmsPageResult update(@PathVariable String id,
                                @RequestBody CmsPage cmsPage) {
        return cmsPageService.update(id,cmsPage);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable String id) {
        return cmsPageService.delete(id);
    }

    @Override
    @PostMapping("/publishPage/{pageId}")
    public ResponseResult publishPage(@PathVariable String pageId) {
        // 获取Header中的JWT
        String jwt = getJwtFromHeader();
        cmsPageService.publishPage(pageId,jwt);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    @PostMapping("/save")
    public CmsPageResult save(@RequestBody CmsPage cmsPage) {
        return cmsPageService.save(cmsPage);
    }

    @Override
    @PostMapping("/publishPageQuick")
    public CmsPublishPageResult publishPageQuick(@RequestBody CmsPage cmsPage) {
        // 获取Header中的JWT
        String jwt = getJwtFromHeader();
        String pageUrl = cmsPageService.publishPageQuick(cmsPage,jwt);
        return new CmsPublishPageResult(CommonCode.SUCCESS,pageUrl);
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
