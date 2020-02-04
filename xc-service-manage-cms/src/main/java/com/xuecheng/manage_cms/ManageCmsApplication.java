package com.xuecheng.manage_cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: Pace
 * @Data: 2020/2/4 11:19
 * @Version: v1.0
 */
@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.cms") // 扫描实体类
@ComponentScan(basePackages = {"com.xuecheng.api","com.xuecheng.manage_cms"}) // 扫描api接口
public class ManageCmsApplication{
    public static void main(String[] args) {
        SpringApplication.run(ManageCmsApplication.class);
    }
}
