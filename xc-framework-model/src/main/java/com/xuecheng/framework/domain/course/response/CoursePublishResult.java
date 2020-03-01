package com.xuecheng.framework.domain.course.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Pace
 * @Data: 2020/3/1 17:21
 * @Version: v1.0
 */
@Data
@NoArgsConstructor
public class CoursePublishResult extends ResponseResult {
    String previewUrl;

    public CoursePublishResult(ResultCode resultCode, String previewUrl){
        super(resultCode);
        this.previewUrl = previewUrl;
    }
}
