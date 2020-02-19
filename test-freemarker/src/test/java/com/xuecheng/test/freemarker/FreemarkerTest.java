package com.xuecheng.test.freemarker;

import com.xuecheng.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @Author: Pace
 * @Data: 2020/2/19 17:15
 * @Version: v1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FreemarkerTest {

    /**
     * 使用FreeMarker生成静态html
     */
    @Test
    public void testGenerateHTML() throws IOException, TemplateException {
        // 定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 定义模板路径
        // 获取当前路径
        String path = this.getClass().getResource("/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(path+"/templates/"));
        // 获取模板内容
        Template template = configuration.getTemplate("test1.ftl");
        // 获取数据
        Map data = getData();
        // 生成静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);

        // 输出到文件中
        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream outputStream = new FileOutputStream(new File("d:/tmp/test1.html"));
        IOUtils.copy(inputStream,outputStream);
        inputStream.close();
        outputStream.close();

    }

    @Test
    /**
     * 基于字符串生成模板然后生成HTML静态文件
     */
    public void testGenerateHTMLByString() throws IOException, TemplateException {
        // 定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 定义String类型模板
        String templateString="" +
                "<html>\n" +
                "    <head></head>\n" +
                "    <body>\n" +
                "    名称：${name}\n" +
                "    </body>\n" +
                "</html>";
        // 使用String模板加载器使之成为模板
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("stringTemp",templateString);
        // 将加载器设置到配置类中
        configuration.setTemplateLoader(stringTemplateLoader);
        // 然后就可以生成模板
        Template stringTemp = configuration.getTemplate("stringTemp", "utf-8");
        Map data = getData();
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(stringTemp, data);

        // 输出到文件中
        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream outputStream = new FileOutputStream(new File("d:/tmp/string.html"));
        IOUtils.copy(inputStream,outputStream);
        inputStream.close();
        outputStream.close();
    }

    public Map getData(){
        Map<String,Object> map = new HashMap<>();
        // map存放freemarker使用的数据
        map.put("name","Pace");

        Student stu1 = new Student();
        stu1.setName("小明");
        stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());
        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMoney(200.1f);
        stu2.setAge(19);
        stu2.setBirthday(new Date());
        List<Student> friends = new ArrayList<>();
        friends.add(stu1);
        stu2.setFriends(friends);
        stu2.setBestFriend(stu1);
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);
        //向数据模型放数据
        map.put("stus",stus);

        //准备map数据
        HashMap<String,Student> stuMap = new HashMap<>();
        stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);
        //向数据模型放数据
        map.put("stu1",stu1);
        //向数据模型放数据
        map.put("stuMap",stuMap);
        return map;
    }
}
