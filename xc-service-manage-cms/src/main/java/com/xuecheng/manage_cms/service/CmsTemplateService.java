package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Pace
 * @Data: 2020/2/29 21:33
 * @Version: v1.0
 */
@Service
public class CmsTemplateService {

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    /**
     * 查询模板列表
     * @return
     */
    public List<CmsTemplate> getCmsTemplateList() {
        return cmsTemplateRepository.findAll();
    }
}
