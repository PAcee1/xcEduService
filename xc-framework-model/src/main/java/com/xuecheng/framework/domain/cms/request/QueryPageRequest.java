package com.xuecheng.framework.domain.cms.request;

import com.xuecheng.framework.model.request.RequestData;
import lombok.Data;

/**
 * @Author: Pace
 * @Data: 2020/2/4 11:24
 * @Version: v1.0
 */
@Data
public class QueryPageRequest extends RequestData {
    //站点id
    private String siteId;
    // 页面ID
    private String pageId;
    // 页面名称
    private String pageName;
    // 别名
    private String pageAliase;
    // 模板Id
    private String templateId;
    // ...
}
