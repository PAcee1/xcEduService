package com.xuecheng.govern.gateway.service;

import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: Pace
 * @Data: 2020/3/13 15:22
 * @Version: v1.0
 */
@Service
public class AuthService {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 从Cookie取身份令牌
     * @param request
     * @return
     */
    public String getJwtFromCookie(HttpServletRequest request){
        Map<String, String> cookieMap = CookieUtil.readCookie(request, "uid");
        String jti = cookieMap.get("uid");
        if(StringUtils.isEmpty(jti)){
            return null;
        }
        return jti;
    }

    //查询令牌的有效期
    public long getExpire(String jti) {
        //token在redis中的key
        String key = "user_token:"+jti;
        Long expire = stringRedisTemplate.getExpire(key);
        return expire;
    }

    //从header中查询jwt令牌
    public String getJwtFromHeader(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        if(StringUtils.isEmpty(authorization)){
            //拒绝访问
            return null;
        }
        if(!authorization.startsWith("Bearer ")){
            //拒绝访问
            return null;
        }
        return authorization;
    }

}
