package com.xuecheng.framework.domain.cms.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Pace
 * @Data: 2020/3/2 15:13
 * @Version: v1.0
 */
@Data
@NoArgsConstructor
public class CmsPublishPageResult extends ResponseResult {

    String pageUrl;

    public CmsPublishPageResult(ResultCode resultCode,String pageUrl){
        super(resultCode);
        this.pageUrl = pageUrl;
    }
}
