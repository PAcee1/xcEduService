package com.xuecheng.framework.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @Author: Pace
 * @Data: 2020/3/14 22:55
 * @Version: v1.0
 */
public class FeignClientInterceptor implements RequestInterceptor {

    /**
     * 实现Feign的拦截接口
     * 将请求头信息向下传递，即JWT令牌信息向被调用方传递
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
        // 获取request变量
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes != null){
            // 取出请求头
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            // 循环请求头进行传递
            if(headerNames != null){
                while (headerNames.hasMoreElements()){
                    String name = headerNames.nextElement();
                    String value = request.getHeader(name);
                    if(name.equals("authorization")){
                        // 传递
                        template.header(name,value);
                    }
                }
            }
        }
    }
}
