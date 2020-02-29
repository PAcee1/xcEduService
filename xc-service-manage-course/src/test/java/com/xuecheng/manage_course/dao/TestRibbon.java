package com.xuecheng.manage_course.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: Pace
 * @Data: 2020/2/29 16:56
 * @Version: v1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRibbon {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void test(){
        String serverId = "XC-SERVICE-MANAGE-CMS";
        ResponseEntity<String> forEntity = restTemplate.getForEntity(
                "http://" + serverId + "/hello", String.class);
        System.out.println(forEntity.getBody());
    }

}
