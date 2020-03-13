package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.govern.gateway.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Pace
 * @Data: 2020/3/13 14:51
 * @Version: v1.0
 */
@Component
public class LoginFilter extends ZuulFilter {

    @Autowired
    private AuthService authService;

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
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        // 从Cookie取jti令牌
        String jti = authService.getJwtFromCookie(request);
        // 不存在拒绝访问
        if(jti == null){
            accessDenied();
            return null;
        }

        // 从Redis取过期时间
        long expire = authService.getExpire(jti);
        // 不存在拒绝访问
        if(expire < 0){
            accessDenied();
            return null;
        }

        // 从请求头取令牌
        String authorization = authService.getJwtFromHeader(request);
        // 不存在拒绝访问
        if(authorization == null){
            accessDenied();
            return null;
        }

        return null;
    }

    /**
     * 拒绝访问
     */
    private void accessDenied(){
        RequestContext currentContext = RequestContext.getCurrentContext();
        // 返回错误信息
        currentContext.setSendZuulResponse(false);//Zuul拒绝访问
        currentContext.setResponseStatusCode(200); // 响应状态码
        ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
        // 转成Json响应
        String jsonString = JSON.toJSONString(responseResult);
        currentContext.setResponseBody(jsonString);
        currentContext.getResponse().setContentType("application/json;charset=UTF-8");
    }
}
