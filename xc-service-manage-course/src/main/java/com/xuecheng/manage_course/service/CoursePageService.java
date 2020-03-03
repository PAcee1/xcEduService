package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.constant.CourseConstant;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPublishPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    @Autowired(required = false)
    private TeachplanMapper teachplanMapper;
    @Autowired
    private CourseMarketRepository courseMarketRepository;
    @Autowired
    private CoursePicRepository coursePicRepository;
    @Autowired
    private CoursePubRepository coursePubRepository;

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

        // 四、保存课程索引到数据库，由Logstash自动更新索引
        CoursePub coursePub = createCoursePub(courseId);
        CoursePub newcoursePub = saveCoursePub(courseId,coursePub);

        return pageUrl;
    }

    /**
     * 保存CoursePub
     * @param courseId
     * @param coursePub
     * @return
     */
    private CoursePub saveCoursePub(String courseId,CoursePub coursePub) {
        CoursePub newCoursePub = new CoursePub();
        // 首先根据id查询，看看是否存在
        Optional<CoursePub> optional = coursePubRepository.findById(courseId);
        if (optional.isPresent()){
            // 存在，更新对象
            newCoursePub = optional.get();
        }
        // 将新的对象覆盖到旧的上
        BeanUtils.copyProperties(coursePub,newCoursePub);
        // 设置时间戳
        newCoursePub.setTimestamp(new Date());
        // 设置发布时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        newCoursePub.setPubTime(date);

        // 保存
        coursePubRepository.save(newCoursePub);
        return newCoursePub;
    }

    /**
     * 创建CoursePub对象
     * @param courseId
     * @return
     */
    private CoursePub createCoursePub(String courseId){
        CoursePub coursePub = new CoursePub();
        // 查询课程基本信息
        Optional<CourseBase> cboptional = courseBaseRepository.findById(courseId);
        if(cboptional.isPresent()){
            CourseBase courseBase = cboptional.get();
            BeanUtils.copyProperties(courseBase,coursePub);
        }

        // 课程图片信息
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        if(picOptional.isPresent()){
            CoursePic coursePic = picOptional.get();
            BeanUtils.copyProperties(coursePic,coursePub);
        }

        // 课程营销信息
        Optional<CourseMarket> cmOptional = courseMarketRepository.findById(courseId);
        if(cmOptional.isPresent()){
            CourseMarket courseMarket = cmOptional.get();
            BeanUtils.copyProperties(courseMarket,coursePub);
        }

        // 课程计划信息
        TeachplanNode teachplanNode = teachplanMapper.findTeachListById(courseId);
        String teachJson = JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(teachJson);
        return coursePub;
    }

    /**
     * 更新课程信息
     * @param courseId
     */
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

    /**
     * 将课程数据转为Cms页面对象
     * @param courseId
     * @return
     */
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
