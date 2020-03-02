package com.xuecheng.search;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @Author: Pace
 * @Data: 2020/3/2 21:39
 * @Version: v1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestIndex {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void addIndex() throws IOException {
        // 创建索引请求
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
        // 设置索引参数
        createIndexRequest.settings(Settings.builder().put("number_of_shards","1")
                .put("number_of_replicas","0").build());
        // 设置映射
        String mapping = "{\n" +
                "\t\"properties\": {\n" +
                "\t\t\"name\": {\n" +
                "\t\t\t\"type\": \"text\",\n" +
                "\t\t\t\"analyzer\": \"ik_max_word\",\n" +
                "\t\t\t\"search_analyzer\": \"ik_smart\"\n" +
                "\t\t},\n" +
                "\t\t\"description\": {\n" +
                "\t\t\t\"type\": \"text\",\n" +
                "\t\t\t\"analyzer\":\"ik_max_word\",\n" +
                "\t\t\t\"search_analyzer\":\"ik_smart\"\n" +
                "\t\t},\n" +
                "\t\t\"studymodel\": {\n" +
                "\t\t\t\"type\": \"keyword\"\n" +
                "\t\t},\n" +
                "\t\t\"price\": {\n" +
                "\t\t\t\"type\": \"float\"\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
        createIndexRequest.mapping("doc",mapping,XContentType.JSON);
        // 创建操作索引的客户端
        IndicesClient indices = restHighLevelClient.indices();
        // 创建索引
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest);
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }

    @Test
    public void deleteIndex() throws IOException {
        DeleteIndexResponse response = restHighLevelClient.indices().delete(
                new DeleteIndexRequest("xc_course"));
        System.out.println(response.isAcknowledged());
    }
}
