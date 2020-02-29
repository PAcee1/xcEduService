package com.xuecheng.manage_cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: Pace
 * @Data: 2020/2/4 11:19
 * @Version: v1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("com.xuecheng.framework.domain.cms") // 扫描实体类
@ComponentScan(basePackages = {"com.xuecheng.api"}) // 扫描api接口
@ComponentScan(basePackages = {"com.xuecheng.framework"}) // 扫描common接口
@ComponentScan(basePackages = {"com.xuecheng.manage_cms"}) // 扫描本接口
public class ManageCmsApplication{
    public static void main(String[] args) {
        SpringApplication.run(ManageCmsApplication.class);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
