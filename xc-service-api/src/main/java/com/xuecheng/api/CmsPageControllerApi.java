package com.xuecheng.api;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * @Author: Pace
 * @Data: 2020/2/4 11:22
 * @Version: v1.0
 */
public interface CmsPageControllerApi {
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
}
