package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author: Pace
 * @Data: 2020/2/29 21:33
 * @Version: v1.0
 */
@Service
public class CmsSiteService {

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    /**
     * 查询站点列表
     * @return
     */
    public List<CmsSite> getCmsSiteList() {
        List<CmsSite> cmsSiteList = cmsSiteRepository.findAll();
        return cmsSiteList;
    }

    /**
     * 根据id查询站点
     * @param siteId
     * @return
     */
    public CmsSite getCmsSiteById(String siteId) {
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        if(optional.isPresent())
            return optional.get();
        return null;
    }
}
