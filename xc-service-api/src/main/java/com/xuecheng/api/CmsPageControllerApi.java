package com.xuecheng.api;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: Pace
 * @Data: 2020/2/4 11:22
 * @Version: v1.0
 */
@Api(value = "cms页面管理接口",description = "cms页面管理，查询接口")
public interface CmsPageControllerApi {

    /**
     * 查询cms页面
     * @param page 当前页
     * @param size 每页数量
     * @param queryPageRequest 查询参数
     * @return
     */
    @ApiOperation("查询cms页面")
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    /**
     * 添加新cms页面
     * @param cmsPage cms页面信息
     * @return
     */
    @ApiOperation("添加新cms页面")
    CmsPageResult add(CmsPage cmsPage);

    /**
     * 根据ID查询cmsPage
     * @param id
     * @return
     */
    @ApiOperation("根据ID查询cmsPage")
    CmsPage findById(String id);

    /**
     * 修改cmsPage
     * @param id
     * @param cmsPage
     * @return
     */
    @ApiOperation("修改cmsPage")
    CmsPageResult update(String id,CmsPage cmsPage);

    /**
     * 根据ID删除CMSPage
     * @param id
     * @return
     */
    @ApiOperation("根据ID删除CmsPage")
    ResponseResult delete(String id);

    /**
     * 页面发布
     * @param pageId
     * @return
     */
    @ApiOperation("页面发布")
    ResponseResult publishPage(String pageId);
}
