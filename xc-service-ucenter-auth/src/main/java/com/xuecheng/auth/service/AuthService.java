package com.xuecheng.auth.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.exception.ExceptionCast;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Pace
 * @Data: 2020/3/10 15:16
 * @Version: v1.0
 */
@Service
public class AuthService {

    @Value("${auth.tokenValiditySeconds}")
    private long ttl;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 发放令牌
     * @param username
     * @param password
     * @param clientId
     * @param clientSecert
     * @return
     */
    public AuthToken login(String username,
                           String password,
                           String clientId,
                           String clientSecert){
        /**
         * 请求令牌
         */
        AuthToken authToken = appleyToken(username,password,clientId,clientSecert);
        // 判断令牌是否存在
        if (authToken == null){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }

        /**
         * 保存令牌到Redis中
         */
        String key = authToken.getJti_token();
        String content = JSON.toJSONString(authToken);
        boolean saveResult = saveAuthToken(key,content,ttl);
        if(!saveResult){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);
        }
        return authToken;
    }

    /**
     * 根据jti 向Redis读jwt数据
     * @param jti
     */
    public AuthToken getUserJwt(String jti) {
        String key = "user_token:" + jti;
        String json = stringRedisTemplate.opsForValue().get(key);
        AuthToken authToken = JSON.parseObject(json, AuthToken.class);
        return authToken;
    }

    /**
     * 删除token
     * @param token
     */
    public Boolean deleteToken(String token) {
        String key = "user_token:" + token;
        Boolean delete = stringRedisTemplate.delete(key);
        return delete;
    }

    /**
     * 保存令牌到Redis中
     * @param key
     * @param content
     * @param ttl 过期时间
     * @return
     */
    private boolean saveAuthToken(String key, String content, long ttl) {
        String name = "user_token:" + key;
        stringRedisTemplate.boundValueOps(name).set(content,ttl,TimeUnit.SECONDS);
        // 通过过期时间判断是否存放成功
        Long expire = stringRedisTemplate.getExpire(name);
        return expire > 0;
    }

    /**
     * 请求SpringSecurity 通过密码授权模式获取令牌
     * @param username
     * @param password
     * @param clientId
     * @param clientSecert
     * @return
     */
    private AuthToken appleyToken(String username, String password, String clientId, String clientSecert) {
        // 获取认证服务地址
        ServiceInstance instance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        String authUrl = instance.getUri() + "/auth/oauth/token";

        // 配置请求头
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        // 加密应用名密码
        String httpbasic = httpbasic(clientId,clientSecert);
        header.add("Authorization","Basic " + httpbasic);

        // 配置body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username",username);
        body.add("password",password);

        // 拼装HttpEntity
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(body,header);

        // 指定RestTemplate遇到400或401错误时，不报错，而是返回结果
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                // 400和401不操作
                if(response.getRawStatusCode() != 400 && response.getRawStatusCode() !=401){
                    super.handleError(response);
                }
            }
        });
        // 执行认证
        ResponseEntity<Map> responseEntity = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map.class);
        // 令牌信息
        Map map = responseEntity.getBody();

        // 判断令牌
        if(map == null ||
                map.get("access_token") == null ||
                map.get("refresh_token") == null ||
                map.get("jti") == null){
            // 根据返回信息，返回正确错误信息
            String errorMsg = (String) map.get("error_description");
            if(StringUtils.isNotEmpty(errorMsg)){
                if(errorMsg.equals("坏的凭证")){
                    ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
                }else {
                    ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
                }
            }
        }
        // 封装令牌信息
        AuthToken authToken = new AuthToken();
        authToken.setJti_token((String) map.get("jti"));
        authToken.setJwt_token((String) map.get("access_token"));
        authToken.setRefresh_token((String) map.get("refresh_token"));
        return authToken;
    }

    /**
     * 封装请求Header
     * @param name
     * @param pwd
     * @return
     */
    private String httpbasic(String name, String pwd) {
        String code = name + ":" + pwd;
        byte[] encode = Base64Utils.encode(code.getBytes());
        return new String(encode);
    }


}
