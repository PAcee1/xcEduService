package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: Pace
 * @Data: 2020/2/4 13:34
 * @Version: v1.0
 */
@Service
public class CmsPageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 分页查询页面列表
     * @param page 当前页数，前端传从1开始，我们需要减1处理
     * @param size 每页数量
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page,int size,
                                        QueryPageRequest queryPageRequest){
        // 查询条件判空
        if(queryPageRequest == null){
            queryPageRequest = new QueryPageRequest();
        }

        // 添加查询条件 对于别名和名称需要模糊查询，而ID使用精准查询就不用设置
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("pageName",ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        if(StringUtils.isNotBlank(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if(StringUtils.isNotBlank(queryPageRequest.getPageId())){
            cmsPage.setPageId(queryPageRequest.getPageId());
        }
        if(StringUtils.isNotBlank(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        if(StringUtils.isNotBlank(queryPageRequest.getPageName())){
            cmsPage.setPageName(queryPageRequest.getPageName());
        }
        if(StringUtils.isNotBlank(queryPageRequest.getPageType())){
            cmsPage.setPageType(queryPageRequest.getPageType());
        }
        // 创建Example
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);

        // 设置分页条件
        if(page < 1){
            page = 1;
        }
        page = page - 1; // 从0开始
        if(size <= 0){
            size = 10;
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);

        // 查询成功后，处理结果
        QueryResult queryResult = new QueryResult();
        queryResult.setTotal(all.getTotalElements());
        queryResult.setList(all.getContent());
        QueryResponseResult responseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return responseResult;
    }

    /**
     * 添加新cms页面
     * @param cmsPage cms页面信息
     * @return
     */
    public CmsPageResult add(CmsPage cmsPage) {
        // 添加前需要判断是否违法唯一性约束
        // 唯一性约束：pageName，siteId，pageWebPath
        CmsPage cmsPage1 = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(
                cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());
        if(cmsPage1 != null){
            // 如果存在，则抛出自定义异常，页面已存在异常
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        // 不存在，才进行添加
        // 并且设置pageId自动生成
        cmsPage.setPageId(null);
        CmsPage cmsPageResult = cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS,cmsPageResult);
    }

    /**
     * 根据ID查询CmsPage
     * @param pageId
     * @return
     */
    public CmsPage findById(String pageId){
        Optional<CmsPage> cmsPage = cmsPageRepository.findById(pageId);
        if(cmsPage.isPresent()){
            CmsPage cmsPage1 = cmsPage.get();
            return cmsPage1;
        }
        return null;
    }

    /**
     * 修改cmsPage
     * @param pageId
     * @param cmsPage
     * @return
     */
    public CmsPageResult update(String pageId,CmsPage cmsPage){
        // 首先判断此id是否存在
        CmsPage page = this.findById(pageId);
        // 存在才修改
        if(page == null){
            // 不存在抛出异常
            ExceptionCast.cast(CmsCode.CMS_FINDPAGE_NOTEXIST);
        }
        page.setTemplateId(cmsPage.getTemplateId());
        page.setSiteId(cmsPage.getSiteId());
        page.setPageAliase(cmsPage.getPageAliase());
        page.setPageName(cmsPage.getPageName());
        page.setPageWebPath(cmsPage.getPageWebPath());
        page.setDataUrl(cmsPage.getDataUrl());
        page.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
        // 更新
        CmsPage save = cmsPageRepository.save(page);
        if(save == null){
            // 保存失败抛出异常
            ExceptionCast.cast(CmsCode.CMS_SAVEPAGE_ERROR);
        }
        return new CmsPageResult(CommonCode.SUCCESS,save);
    }

    /**
     * 根据id删除页面
     * @param id
     * @return
     */
    public ResponseResult delete(String id) {
        // 首先需要判断此id是否存在
        CmsPage cmsPage = this.findById(id);
        if(cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_FINDPAGE_NOTEXIST);
        }
        cmsPageRepository.deleteById(id);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 页面静态化
     * @param pageId
     * @return
     */
    public String getPageHtml(String pageId){
        // 首先获取数据模型
        Map model = getModelByPageId(pageId);
        if(model == null){
            // model不存在，抛异常
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }

        // 然后获取模板
        String templateContent = getTemplateByPageId(pageId);
        if(StringUtils.isEmpty(templateContent)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        // 最后执行静态化
        String html = generateHtml(templateContent,model);
        if(StringUtils.isEmpty(html)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return html;
    }

    /**
     * 发布页面
     * @param pageId
     */
    public void publishPage(String pageId){
        // 1、使页面静态化
        String pageHtml = getPageHtml(pageId);
        if(StringUtils.isEmpty(pageHtml)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }

        // 2、保存静态化页面到GridFS
        try {
            CmsPage cmsPage = saveHtml(pageId, pageHtml);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3、发送消息到MQ
        sendPublishPageMsg(pageId);
    }

    /**
     * 发送消息到MQ
     * @param pageId
     */
    private void sendPublishPageMsg(String pageId) {
        // 需要将消息json化
        Map<String ,Object> map = new HashMap<>();
        map.put("pageId",pageId);
        String jsonString = JSON.toJSONString(map);

        // 发布消息,routingKey为siteId
        CmsPage cmsPage = findById(pageId);
        String siteId = cmsPage.getSiteId();
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,siteId,jsonString);
    }

    /**
     * 存储静态页面到GridFS中
     * @param pageId
     * @param pageHtml
     * @throws IOException
     */
    private CmsPage saveHtml(String pageId, String pageHtml) throws IOException {
        // 1、根据pageId获取CmsPage
        CmsPage cmsPage = findById(pageId);
        if(cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        // 2、获取FileID
        String htmlFileId = cmsPage.getHtmlFileId();
        // 3、删除原来的File
        if(StringUtils.isNotEmpty(htmlFileId)){
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(htmlFileId)));
        }
        // 4、保存html到GridFS中
        InputStream inputStream = IOUtils.toInputStream(pageHtml, "utf-8");
        ObjectId objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());

        // 5、更新CmsPage的FileID
        cmsPage.setHtmlFileId(objectId.toHexString());
        cmsPageRepository.save(cmsPage);

        inputStream.close();
        return cmsPage;
    }

    /**
     * 静态化
     * @param templateContent
     * @param model
     * @return
     */
    private String generateHtml(String templateContent, Map model) {
        // 定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 使用String模板加载器使之成为模板
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateContent);
        // 将加载器设置到配置类中
        configuration.setTemplateLoader(stringTemplateLoader);
        // 然后就可以生成模板
        Template stringTemp = null;
        try {
            stringTemp = configuration.getTemplate("template", "utf-8");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(stringTemp, model);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取模板
     * @param pageId
     * @return
     */
    private String getTemplateByPageId(String pageId) {
        // 首先查询页面
        CmsPage cmsPage = this.findById(pageId);
        if(cmsPage == null){
            // 页面不存在，抛出异常
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        // 获取模板id
        String templateId = cmsPage.getTemplateId();
        if(StringUtils.isEmpty(templateId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        // 根据模板id获取模板对象
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if(optional.isPresent()){
            CmsTemplate cmsTemplate = optional.get();
            // 获取模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            // 取文件
            // 首先查询此文件
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));

            // 然后获取下载流
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            // 创建GridFSResources，获取文件对象
            GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
            // 使用IOUtils输出
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取数据模型
     * @param pageId
     * @return
     */
    private Map getModelByPageId(String pageId) {
        // 获取CmsPage
        CmsPage cmsPage = this.findById(pageId);
        if(cmsPage == null){
            // 页面不存在，抛出异常
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        // 然后获取DataUrl
        String dataUrl = cmsPage.getDataUrl();
        if(StringUtils.isEmpty(dataUrl)){
            // dataUrl不存在，抛异常
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }

        // 根据dataUrl获取模型
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }
}
