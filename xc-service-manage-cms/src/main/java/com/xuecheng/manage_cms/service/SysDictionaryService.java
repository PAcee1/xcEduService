package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.dao.SysDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Pace
 * @Data: 2020/2/26 21:00
 * @Version: v1.0
 */
@Service
public class SysDictionaryService {

    @Autowired
    private SysDictionaryRepository sysDictionaryRepository;

    public SysDictionary getSysDictionaryByType(String type) {
        SysDictionary sysDictionary = sysDictionaryRepository.findByDType(type);
        return sysDictionary;
    }
}
