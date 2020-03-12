package com.xuecheng.auth.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Pace
 * @Data: 2020/3/10 11:11
 * @Version: v1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestJWT {

    @Test
    public void test(){
        Map<String,Double> map = new HashMap<>();
        map.put("A",0.0);
        Double a = map.get("A");
        a = 2.2;
        System.out.println(map);
    }
    @Test
    public void createJWT(){
        // 秘钥库名称
        String keystoreName = "xc.keystore";
        // 秘钥库密码
        String keystorePwd = "xuechengkeystore";
        ClassPathResource classPathResource = new ClassPathResource(keystoreName);


        // 秘钥工厂创建
        // 需要秘钥库路径和密码
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource,keystorePwd.toCharArray());

        // 获取秘钥对
        // 需要别名和秘钥访问密码
        String alias = "xcKey";
        String keyPwd = "xuecheng";
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias,keyPwd.toCharArray());

        // 获取私钥
        RSAPrivateKey aPrivate = (RSAPrivateKey) keyPair.getPrivate();
        // jwtPayload内容
        Map map = new HashMap();
        map.put("name","pace");
        String jsonString = JSON.toJSONString(map);
        // 生成JWT
        Jwt jwt = JwtHelper.encode(jsonString, new RsaSigner(aPrivate));
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }

    @Test
    public void verifyJWT(){
        // 公钥
        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnASXh9oSvLRLxk901HANYM6KcYMzX8vFPnH/To2R+SrUVw1O9rEX6m1+rIaMzrEKPm12qPjVq3HMXDbRdUaJEXsB7NgGrAhepYAdJnYMizdltLdGsbfyjITUCOvzZ/QgM1M4INPMD+Ce859xse06jnOkCUzinZmasxrmgNV3Db1GtpyHIiGVUY0lSO1Frr9m5dpemylaT0BV3UwTQWVW9ljm6yR3dBncOdDENumT5tGbaDVyClV0FEB1XdSKd7VjiDCDbUAUbDTG1fm3K9sx7kO1uMGElbXLgMfboJ963HEJcU01km7BmFntqI5liyKheX+HBUCD4zbYNPw236U+7QIDAQAB-----END PUBLIC KEY-----";
        // jwt
        String jwtoken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoicGFjZSJ9.AnIPm6tLg8Ntyb9FmXkzsH3lm4hR0tp_FTnVXjXziLIIPGZscda7rCTGqRBOOs-Dr9I-EpODFPwSQ5UYks1LDl1WORJ5ggZgEZNKq7EguhfGqFO2naeJesN8Bhk2gpKE9O64E53wFl_VIA0Qu2Euk5GILRbhZvWY4qYGKoIp9k9IvQ2QxJl2nyL2aVxGvQXo29JlOffCqINew3vUwbbhz_q0FMdQ31OmXaUXASCfWndON4_UDgBJOF-LKEU1nvCIuaXygbfBoHU6tS-KLZcX67jZ5ADg396lwM9zCAsT-g7sw0jMzxnm0o47sl-9QRmusVpM7ai3oHhEUu9wAMAfPg";
        // 校验JWT
        Jwt jwt = JwtHelper.decodeAndVerify(jwtoken, new RsaVerifier(publicKey));
        // 拿到jwt中的自定义内容
        String claims = jwt.getClaims();
        System.out.println(claims);
    }
}
