package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Pace
 * @Data: 2020/3/10 16:45
 * @Version: v1.0
 */
public interface XcUserRepository extends JpaRepository<XcUser,String> {
    XcUser findByUsername(String username);
}
