package com.xuecheng.manage_cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @Author: Pace
 * @Data: 2020/2/4 12:02
 * @Version: v1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RestTemplateTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void test(){
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(
                "http://localhost:31001/cms/config/getModel/5a791725dd573c3574ee333f", Map.class);
        Map map = forEntity.getBody();
        System.out.println(map);
    }


}
