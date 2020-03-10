package com.xuecheng.auth.test;

import com.xuecheng.framework.client.XcServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: Pace
 * @Data: 2020/3/10 14:47
 * @Version: v1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestAuth {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Test
    public void testAuth(){
        // 获取认证服务地址
        ServiceInstance instance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        String authUrl = instance.getUri() + "/auth/oauth/token";

        // 配置请求头
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        // 加密应用名密码
        String httpbasic = httpbasic("XcWebApp","XcWebApp");
        header.add("Authorization","Basic " + httpbasic);

        // 配置body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username","itcast");
        body.add("password","1223");

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
        System.out.println(map);
    }

    private String httpbasic(String name, String pwd) {
        String code = name + ":" + pwd;
        byte[] encode = Base64Utils.encode(code.getBytes());
        return new String(encode);
    }
}
