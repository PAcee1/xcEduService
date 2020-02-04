package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author: Pace
 * @Data: 2020/2/4 11:56
 * @Version: v1.0
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
}
