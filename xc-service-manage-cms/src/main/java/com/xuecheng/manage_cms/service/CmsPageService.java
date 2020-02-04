package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
     * @param queryPageRequest 参数
     * @return
     */
    public QueryResponseResult findList(int page,int size,
                                        QueryPageRequest queryPageRequest){
        if(page < 1){
            page = 1;
        }
        page = page - 1; // 从0开始
        if(size <= 0){
            size = 10;
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        QueryResult queryResult = new QueryResult();
        queryResult.setTotal(all.getTotalElements());
        queryResult.setList(all.getContent());
        QueryResponseResult responseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return responseResult;
    }
}
