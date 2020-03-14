package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Pace
 * @Data: 2020/3/14 17:12
 * @Version: v1.0
 */
@Mapper
public interface XcMenuMapper {
    List<XcMenu> selectPermissionByUserId(String userId);
}
