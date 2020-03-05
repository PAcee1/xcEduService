package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Pace
 * @Data: 2020/3/3 17:30
 * @Version: v1.0
 */
@Service
public class EsCourseService {

    @Value("${xuecheng.course.index}")
    private String index;
    @Value("${xuecheng.course.type}")
    private String type;
    @Value("${xuecheng.course.source_field}")
    private String sourceField;

    @Autowired
    private RestHighLevelClient client;

    public QueryResult<CoursePub> esList(int page,
                                         int size,
                                         CourseSearchParam courseSearchParam){
        // 创建搜索请求，设置索引
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        // 设置查询条件
        SearchSourceBuilder searchSourceBuilder = setSearchBuilder(page,size,courseSearchParam);
        searchRequest.source(searchSourceBuilder);

        QueryResult<CoursePub> queryResult = new QueryResult<>();
        try {
            // 查询
            SearchResponse response = client.search(searchRequest);
            // 处理返回结果
            queryResult = resolveResult(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return queryResult;
    }

    /**
     * 设置查询条件
     * @param courseSearchParam
     * @return
     */
    private SearchSourceBuilder  setSearchBuilder(int page,
                                                  int size,
                                                  CourseSearchParam courseSearchParam){
        // 设置查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String[] fields = sourceField.split(",");
        searchSourceBuilder.fetchSource(fields,new String[]{});// 设置搜索的字段
        // 聚合查询用
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if(StringUtils.isNotEmpty(courseSearchParam.getKeyword())){
            // 匹配关键字
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(
                    courseSearchParam.getKeyword(), "name","description","teachplan");
            // 设置匹配占比和字段权重
            multiMatchQueryBuilder.minimumShouldMatch("70%");
            multiMatchQueryBuilder.field("name",10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }

        // 过滤器过滤分类
        if(StringUtils.isNotEmpty(courseSearchParam.getMt())){
            // 一级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt",courseSearchParam.getMt()));
        }
        if(StringUtils.isNotEmpty(courseSearchParam.getSt())){
            // 二级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("st",courseSearchParam.getSt()));
        }

        // 过滤难度等级
        if(StringUtils.isNotEmpty(courseSearchParam.getGrade())){
            // 二级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade",courseSearchParam.getGrade()));
        }

        // 查询请求设置
        searchSourceBuilder.query(boolQueryBuilder);

        // 分页设置
        // 设置起始下标
        int from = (page < 1 ? 0 : page-1) * size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size < 0 ? 20 : size);

        // 设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);

        return searchSourceBuilder;
    }

    /**
     * 处理返回结果，封装成QueryResult对象
     * @param response
     * @return
     */
    private QueryResult<CoursePub> resolveResult(SearchResponse response){
        // 创建对象保存数据
        QueryResult<CoursePub> queryResult = new QueryResult<>();
        List<CoursePub> list = new ArrayList<>();

        // 处理结果
        SearchHits hits = response.getHits();
        long totalHits = hits.getTotalHits();
        queryResult.setTotal(totalHits);
        for(SearchHit hit : hits){
            CoursePub coursePub = new CoursePub();
            //取出source
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            // 取出id
            String id = (String) sourceAsMap.get("id");
            coursePub.setId(id);
            //取出名称
            String name = (String) sourceAsMap.get("name");
            //取出高亮字段内容
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if(highlightFields!=null){
                HighlightField nameField = highlightFields.get("name");
                if(nameField!=null){
                    Text[] fragments = nameField.getFragments();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Text str : fragments) {
                        stringBuffer.append(str.string());
                    }
                    name = stringBuffer.toString();
                }
            }
            coursePub.setName(name);
            //图片
            String pic = (String) sourceAsMap.get("pic");
            coursePub.setPic(pic);
            //价格
            Double price = null;
            if(sourceAsMap.get("price")!=null ){
                price = (Double) sourceAsMap.get("price");
            }
            coursePub.setPrice(price);
            Double priceOld = null;
            if(sourceAsMap.get("price_old")!=null ){
                priceOld = (Double) sourceAsMap.get("price_old");
            }
            coursePub.setPrice_old(priceOld);
            list.add(coursePub);
        }

        // 最后设置list
        queryResult.setList(list);
        return queryResult;
    }
}
