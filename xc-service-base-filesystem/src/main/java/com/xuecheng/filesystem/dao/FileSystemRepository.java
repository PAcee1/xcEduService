package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author: Pace
 * @Data: 2020/2/28 16:35
 * @Version: v1.0
 */
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {
}
