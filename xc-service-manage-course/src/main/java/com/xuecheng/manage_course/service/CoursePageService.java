package com.xuecheng.manage_course.service;

import com.xuecheng.framework.constant.CourseConstant;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPublishPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @Author: Pace
 * @Data: 2020/3/1 17:05
 * @Version: v1.0
 */
@Service
public class CoursePageService {

    @Value("${course-publish.siteId}")
    private String publishSiteId;
    @Value("${course-publish.templateId}")
    private String publishTemplateId;
    @Value("${course-publish.previewUrl}")
    private String publishPreviewUrl;
    @Value("${course-publish.pageWebPath}")
    private String publishPageWebPath;
    @Value("${course-publish.pagePhysicalPath}")
    private String publishPagePhysicalPath;
    @Value("${course-publish.dataUrlPre}")
    private String publishDataUrlPre;

    @Autowired(required = false)
    private CmsPageClient cmsPageClient;
    @Autowired
    private CourseBaseRepository courseBaseRepository;

    /**
     * 课程预览
     * @param courseId
     * @return
     */
    public String coursePreview(String courseId){
        /** 一、根据课程信息创建CmsPage对象 **/
        CmsPage cmsPage = toCmsPage(courseId);

        /** 二、使用Feign调用CMS方法保存对象 **/
        CmsPageResult cmsPageResult = cmsPageClient.saveCmsPage(cmsPage);

        /** 三、拼接URL返回 **/
        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        String pageId = cmsPage1.getPageId();
        String previewUrl = publishPreviewUrl + pageId;
        return previewUrl;

    }

    /**
     * 课程一键发布
     * @param courseId
     * @return
     */
    @Transactional
    public String publishPageQuick(String courseId) {
        // 一、封装CmsPage对象
        CmsPage cmsPage = toCmsPage(courseId);

        // 二、远程调用CMS的一键发布接口
        CmsPublishPageResult cmsPublishPageResult = cmsPageClient.publishPageQuick(cmsPage);
        if(!cmsPublishPageResult.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        String pageUrl = cmsPublishPageResult.getPageUrl();

        // 三、更改课程状态为已发布
        updateCourseState(courseId);

        return pageUrl;
    }

    private void updateCourseState(String courseId){
        // 查询课程信息
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        CourseBase courseBase = optional.get();
        courseBase.setStatus(CourseConstant.COURSEBASE_STATUS_YES);
        courseBaseRepository.save(courseBase);
    }

    private CmsPage toCmsPage(String courseId){
        // 查询课程信息
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        CourseBase courseBase = optional.get();

        /** 根据课程信息创建CmsPage对象 **/
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publishSiteId);
        cmsPage.setTemplateId(publishTemplateId);
        cmsPage.setPageName(courseId + ".html");
        cmsPage.setPageAliase(courseBase.getName());
        cmsPage.setPageWebPath(publishPageWebPath);
        cmsPage.setPagePhysicalPath(publishPagePhysicalPath);
        cmsPage.setDataUrl(publishDataUrlPre + courseId);
        return cmsPage;
    }
}
