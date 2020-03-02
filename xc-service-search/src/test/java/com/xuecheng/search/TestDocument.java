package com.xuecheng.search;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Pace
 * @Data: 2020/3/2 22:02
 * @Version: v1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDocument {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void addDoc() throws IOException {
        // 数据
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "spring cloud实战");
        jsonMap.put("description", "本课程主要从四个章节进行讲解：1.微服务架构入门 2.spring cloud 基础入门" +
                " 3.实战Spring Boot 4.注册中心eureka。");
        jsonMap.put("studymodel", "201001");
        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy‐MM‐dd HH:mm:ss");
        jsonMap.put("timestamp", dateFormat.format(new Date()));
        jsonMap.put("price", 5.6f);

        // 创建索引
        IndexRequest indexRequest = new IndexRequest("xc_course","doc");
        indexRequest.source(jsonMap);
        // 创建文档
        DocWriteResponse.Result result = restHighLevelClient.index(indexRequest).getResult();
        System.out.println(result);
    }

    @Test
    public void searchDoc() throws IOException {
        GetRequest getRequest = new GetRequest("xc_course","doc","9vuXm3ABSX29AG6KeQHI                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  ");
        GetResponse documentFields = restHighLevelClient.get(getRequest);
        Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
        System.out.println(sourceAsMap);

    }
}
