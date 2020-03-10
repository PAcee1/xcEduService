package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.dao.XcCompanyUserRepository;
import com.xuecheng.ucenter.dao.XcUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Pace
 * @Data: 2020/3/10 16:44
 * @Version: v1.0
 */
@Service
public class UcenterService {

    @Autowired
    private XcUserRepository userRepository;
    @Autowired
    private XcCompanyUserRepository companyUserRepository;


    /**
     * 查询用户及扩展信息
     * @param username
     * @return
     */
    public XcUserExt getUserExt(String username) {
        // 查询基础信息
        XcUser xcUser = userRepository.findByUsername(username);
        if(xcUser == null){
            return null;
        }

        // 查询公司信息
        XcCompanyUser xcCompanyUser = companyUserRepository.findByUserId(xcUser.getId());

        // 封装
        XcUserExt userExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser,userExt);
        if(xcCompanyUser != null){
            userExt.setCompanyId(xcCompanyUser.getCompanyId());
        }
        return userExt;
    }
}
