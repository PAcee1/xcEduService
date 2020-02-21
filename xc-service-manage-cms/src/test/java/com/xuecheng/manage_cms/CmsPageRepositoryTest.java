package com.xuecheng.manage_cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: Pace
 * @Data: 2020/2/4 12:02
 * @Version: v1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsPageService cmsPageService;

    @Test
    public void testFindPage(){
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(0,10);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    @Test
    public void testFindPageByParams(){
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase("轮播");
        // 设置匹配规则
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                // 意思是pageAliase这个字段，包含，即模糊查询
                .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(0,10);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);
        System.out.println(all);
    }

    @Test
    public void getPageHtml(){
        String pageHtml = cmsPageService.getPageHtml("5e4fd031392c022514c5122e");
        System.out.println(pageHtml);
    }
}
