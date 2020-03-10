package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Pace
 * @Data: 2020/3/10 16:45
 * @Version: v1.0
 */
public interface XcCompanyUserRepository extends JpaRepository<XcCompanyUser,String> {

    XcCompanyUser findByUserId(String userId);
}
