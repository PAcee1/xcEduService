package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author: Pace
 * @Data: 2020/2/4 13:34
 * @Version: v1.0
 */
@Service
public class CmsPageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    /**
     * 分页查询页面列表
     * @param page 当前页数，前端传从1开始，我们需要减1处理
     * @param size 每页数量
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page,int size,
                                        QueryPageRequest queryPageRequest){
        // 查询条件判空
        if(queryPageRequest == null){
            queryPageRequest = new QueryPageRequest();
        }

        // 添加查询条件 对于别名和名称需要模糊查询，而ID使用精准查询就不用设置
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("pageName",ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        if(StringUtils.isNotBlank(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if(StringUtils.isNotBlank(queryPageRequest.getPageId())){
            cmsPage.setPageId(queryPageRequest.getPageId());
        }
        if(StringUtils.isNotBlank(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        if(StringUtils.isNotBlank(queryPageRequest.getPageName())){
            cmsPage.setPageName(queryPageRequest.getPageName());
        }
        if(StringUtils.isNotBlank(queryPageRequest.getPageType())){
            cmsPage.setPageType(queryPageRequest.getPageType());
        }
        // 创建Example
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);

        // 设置分页条件
        if(page < 1){
            page = 1;
        }
        page = page - 1; // 从0开始
        if(size <= 0){
            size = 10;
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);

        // 查询成功后，处理结果
        QueryResult queryResult = new QueryResult();
        queryResult.setTotal(all.getTotalElements());
        queryResult.setList(all.getContent());
        QueryResponseResult responseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return responseResult;
    }

    /**
     * 添加新cms页面
     * @param cmsPage cms页面信息
     * @return
     */
    public CmsPageResult add(CmsPage cmsPage) {
        // 添加前需要判断是否违法唯一性约束
        // 唯一性约束：pageName，siteId，pageWebPath
        CmsPage cmsPage1 = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(
                cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());
        if(cmsPage1 != null){
            // 如果存在，则抛出自定义异常，页面已存在异常
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        // 不存在，才进行添加
        // 并且设置pageId自动生成
        cmsPage.setPageId(null);
        CmsPage cmsPageResult = cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS,cmsPageResult);
    }

    /**
     * 根据ID查询CmsPage
     * @param pageId
     * @return
     */
    public CmsPage findById(String pageId){
        Optional<CmsPage> cmsPage = cmsPageRepository.findById(pageId);
        if(cmsPage.isPresent()){
            CmsPage cmsPage1 = cmsPage.get();
            return cmsPage1;
        }
        return null;
    }

    /**
     * 修改cmsPage
     * @param pageId
     * @param cmsPage
     * @return
     */
    public CmsPageResult update(String pageId,CmsPage cmsPage){
        // 首先判断此id是否存在
        CmsPage page = this.findById(pageId);
        // 存在才修改
        if(page == null){
            // 不存在抛出异常
            ExceptionCast.cast(CmsCode.CMS_FINDPAGE_NOTEXIST);
        }
        page.setTemplateId(cmsPage.getTemplateId());
        page.setSiteId(cmsPage.getSiteId());
        page.setPageAliase(cmsPage.getPageAliase());
        page.setPageName(cmsPage.getPageName());
        page.setPageWebPath(cmsPage.getPageWebPath());
        page.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
        // 更新
        CmsPage save = cmsPageRepository.save(page);
        if(save == null){
            // 保存失败抛出异常
            ExceptionCast.cast(CmsCode.CMS_SAVEPAGE_ERROR);
        }
        return new CmsPageResult(CommonCode.SUCCESS,save);
    }

    /**
     * 根据id删除页面
     * @param id
     * @return
     */
    public ResponseResult delete(String id) {
        // 首先需要判断此id是否存在
        CmsPage cmsPage = this.findById(id);
        if(cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_FINDPAGE_NOTEXIST);
        }
        cmsPageRepository.deleteById(id);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
