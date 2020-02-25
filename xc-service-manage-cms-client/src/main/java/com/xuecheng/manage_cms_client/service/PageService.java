package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

/**
 * @Author: Pace
 * @Data: 2020/2/23 16:22
 * @Version: v1.0
 */
@Slf4j
@Service
public class PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;

    /**
     * 根据pageId，从GridFS中获取html保存到服务器指定路径上
     * @param pageId
     */
    public void savePageToServerPath(String pageId) throws IOException {
        // 获取CmsPage
        CmsPage cmsPage = getCmsPageById(pageId);
        // 校验页面
        if(cmsPage == null){
            log.error("PageService.getCmsPageById CmsPage is null, pageId:{}",pageId);
            return ;
        }

        // 获取CmsSite
        String siteId = cmsPage.getSiteId();
        CmsSite cmsSite = getCmsSiteById(siteId);

        // 从GridFS获取静态文件
        String htmlFileId = cmsPage.getHtmlFileId();
        InputStream inputStream = getFileById(htmlFileId);
        //判断流是否成功获取
        if(inputStream == null){
            log.error("PageService.getFileById InputStream is null, htmlFileId:{}",htmlFileId);
            return ;
        }

        // 将静态文件保存到服务器上
        // 首先拼接服务器路径 = 站点物理路径 + 文件路径 + 文件名
        String savePath = cmsSite.getSitePhysicalPath() + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        // 使用IOUtils工具类保存
        FileOutputStream fileOutputStream = new FileOutputStream(new File(savePath));
        IOUtils.copy(inputStream,fileOutputStream);
        fileOutputStream.close();
        inputStream.close();
    }

    /**
     * 根据fileId向GridFS查询文件，获取此文件的输入流
     * 为后面将文件拷贝到服务器上做准备
     * @param fileId
     * @return
     */
    public InputStream getFileById(String fileId) {
        // 首先根据id获取File
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        // 然后打开下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        // 然后获取GridFSResources
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);

        try {
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据id获取CmsSIte
     * @param siteId
     * @return
     */
    public CmsSite getCmsSiteById(String siteId) {
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    /**
     * 根据id获取CmsPage
     * @param pageId
     * @return
     */
    public CmsPage getCmsPageById(String pageId){
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }
}
