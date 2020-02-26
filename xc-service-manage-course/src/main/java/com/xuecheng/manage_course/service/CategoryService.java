package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Pace
 * @Data: 2020/2/25 22:04
 * @Version: v1.0
 */
@Service
public class CategoryService {

    @Autowired(required = false)
    private CategoryMapper categoryMapper;

    public CategoryNode findCategoryList() {
        CategoryNode categoryNode = categoryMapper.findCategoryList();
        return categoryNode;
    }
}
