package com.xuecheng.manage_cms_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: Pace
 * @Data: 2020/2/23 15:03
 * @Version: v1.0
 */
@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.cms") // 扫描实体类
@ComponentScan(basePackages = {"com.xuecheng.framework"}) // 扫描common接口
@ComponentScan(basePackages = {"com.xuecheng.manage_cms_client"}) // 扫描本接口
public class ManageCmsClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageCmsClientApplication.class);
    }
}
