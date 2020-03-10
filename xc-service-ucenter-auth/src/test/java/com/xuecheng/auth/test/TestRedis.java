package com.xuecheng.auth.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Pace
 * @Data: 2020/3/10 13:51
 * @Version: v1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis(){
        /**
         * 存储JWT
         */
        String key = "user_token:6081830d-d9e7-448d-b8d9-bd1ac20899b2";
        // value
        Map valueMap = new HashMap();
        valueMap.put("jwt","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoicGFjZSJ9.AnIPm6tLg8Ntyb9FmXkzsH3lm4hR0tp_FTnVXjXziLIIPGZscda7rCTGqRBOOs-Dr9I-EpODFPwSQ5UYks1LDl1WORJ5ggZgEZNKq7EguhfGqFO2naeJesN8Bhk2gpKE9O64E53wFl_VIA0Qu2Euk5GILRbhZvWY4qYGKoIp9k9IvQ2QxJl2nyL2aVxGvQXo29JlOffCqINew3vUwbbhz_q0FMdQ31OmXaUXASCfWndON4_UDgBJOF-LKEU1nvCIuaXygbfBoHU6tS-KLZcX67jZ5ADg396lwM9zCAsT-g7sw0jMzxnm0o47sl-9QRmusVpM7ai3oHhEUu9wAMAfPg");
        valueMap.put("refresh_token","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoicGFjZSJ9.AnIPm6tLg8Ntyb9FmXkzsH3lm4hR0tp_FTnVXjXziLIIPGZscda7rCTGqRBOOs-Dr9I-EpODFPwSQ5UYks1LDl1WORJ5ggZgEZNKq7EguhfGqFO2naeJesN8Bhk2gpKE9O64E53wFl_VIA0Qu2Euk5GILRbhZvWY4qYGKoIp9k9IvQ2QxJl2nyL2aVxGvQXo29JlOffCqINew3vUwbbhz_q0FMdQ31OmXaUXASCfWndON4_UDgBJOF-LKEU1nvCIuaXygbfBoHU6tS-KLZcX67jZ5ADg396lwM9zCAsT-g7sw0jMzxnm0o47sl-9QRmusVpM7ai3oHhEUu9wAMAfPg");
        String jsonString = JSON.toJSONString(valueMap);
        // 存 过期时间30秒
        stringRedisTemplate.boundValueOps(key).set(jsonString,30,TimeUnit.SECONDS);

        /**
         * 取出JWT
         */
        String value = stringRedisTemplate.opsForValue().get(key);
        System.out.println(value);

        /**
         * 校验JWT
         * 如果存活会返回过期时间，如果不存在，返回 -2
         */
        Long expire = stringRedisTemplate.getExpire(key);
        System.out.println(expire);

    }
}
