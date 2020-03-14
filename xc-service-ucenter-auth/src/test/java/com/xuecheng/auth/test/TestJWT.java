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
        String jwtoken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOiIxIiwidXNlcnBpYyI6bnVsbCwidXNlcl9uYW1lIjoiaXRjYXN0Iiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiJ0ZXN0MDIiLCJ1dHlwZSI6IjEwMTAwMiIsImlkIjoiNDkiLCJleHAiOjE1ODQyMjEzODYsImF1dGhvcml0aWVzIjpbInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYmFzZSIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfZGVsIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9saXN0IiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9wbGFuIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZSIsImNvdXJzZV9maW5kX2xpc3QiLCJ4Y190ZWFjaG1hbmFnZXIiLCJ4Y190ZWFjaG1hbmFnZXJfY291cnNlX21hcmtldCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfcHVibGlzaCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYWRkIl0sImp0aSI6IjdhMmRmOWFjLTdiYjEtNDcxZi1hNGYzLTc3MTlkMGYwYmE3MCIsImNsaWVudF9pZCI6IlhjV2ViQXBwIn0.fUqQ-2_n0iCEpgPW8qBmUp9XmwSgsxZ7wnZ496dzoD8sMnX8WJOyO-BkxgZre5QoyVr6GhL_bPwGkpKI8YamUurzgBg0WuDP6LIetX1f291gCCBPq23NwGpG_oBNxVnJHWVP-k4H5K-kBVGPP0qj4XAHdFW1uPd-gxbuzpCjjtF5zhUs8y7lCAXM8WG8oTv1ZjOnYeNV8J4RSuVakF1UHmgaq0N7nUn2Q0lcK9aI6RcHzMhI3MyA852eTq54UxuGXDeI4AMBrQf70zySlJzv-rNDRYNkNF-3wP3vh00-iQg8f2iOG16MdEy3X_ZZgiDWE12Iu90DZyxCU5oKCT1XOA";
        // 校验JWT
        Jwt jwt = JwtHelper.decodeAndVerify(jwtoken, new RsaVerifier(publicKey));
        // 拿到jwt中的自定义内容
        String claims = jwt.getClaims();
        System.out.println(claims);
    }
}
