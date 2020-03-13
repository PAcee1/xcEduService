package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Pace
 * @Data: 2020/3/13 14:51
 * @Version: v1.0
 */
//@Component
public class LoginFilterTest extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 测试 从请求头中获取Authorization
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletResponse response = currentContext.getResponse();
        HttpServletRequest request = currentContext.getRequest();
        String authorization = request.getHeader("Authorization");
        if(StringUtils.isEmpty(authorization)){
            // 返回错误信息
            currentContext.setSendZuulResponse(false);//Zuul拒绝访问
            currentContext.setResponseStatusCode(200); // 响应状态码
            ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
            // 转成Json响应
            String jsonString = JSON.toJSONString(responseResult);
            currentContext.setResponseBody(jsonString);
            response.setContentType("application/json;charset=UTF-8");
            return null;
        }
        return null;
    }
}
