package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.constant.CourseConstant;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @Author: Pace
 * @Data: 2020/2/25 22:04
 * @Version: v1.0
 */
@Service
public class CourseService {

    @Autowired(required = false)
    private TeachplanMapper teachplanMapper;
    @Autowired(required = false)
    private CourseMapper courseMapper;
    @Autowired
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private TeachplanRepository teachplanRepository;
    @Autowired
    private CourseMarketRepository courseMarketRepository;
    @Autowired
    private CoursePicRepository coursePicRepository;


    /**
     * 查询课程列表，分页查询
     * @param page
     * @param size
     * @param courseListRequest
     * @return
     */
    public QueryResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest) {
        // 设置分页
        PageHelper.startPage(page,size);

        // 查询列表
        Page<CourseInfo> pageList = courseMapper.findCourseList(courseListRequest);

        // 封装QueryResult
        List<CourseInfo> result = pageList.getResult();
        long total = pageList.getTotal();
        QueryResult<CourseInfo> queryResult = new QueryResult<>();
        queryResult.setList(result);
        queryResult.setTotal(total);

        return queryResult;
    }

    /**
     * 添加课程
     * @param courseBase
     * @return
     */
    public String addCourse(CourseBase courseBase) {
        courseBase.setStatus(CourseConstant.COURSEBASE_STATUS_NO); // 设置课程状态为未发布
        courseBaseRepository.save(courseBase);
        return courseBase.getId();
    }

    /**
     * 根据id查询课程基础信息
     * @param courseId
     * @return
     */
    public CourseBase getCourseBaseById(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        return optional.get();
    }

    /**
     * 更新课程基础信息
     * @param courseBase
     */
    public void updateCourseBase(CourseBase courseBase) {
        courseBaseRepository.save(courseBase);
    }

    /**
     * 根据id查询课程营销信息
     * @param courseId
     * @return
     */
    public CourseMarket getCourseMarketById(String courseId) {
        Optional<CourseMarket> optional = courseMarketRepository.findById(courseId);
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        return optional.get();
    }

    /**
     * 更新课程营销信息
     * @param courseMarket
     */
    public void updateCourseMarket(CourseMarket courseMarket) {
        courseMarketRepository.save(courseMarket);
    }

    /**
     * 保存课程图片
     * @param courseId
     * @param pic
     */
    public void addCoursePic(String courseId, String pic) {
        CoursePic coursePic = new CoursePic();
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
    }

    /**
     * 查询图片
     * @param courseId
     * @return
     */
    public CoursePic findCoursePic(String courseId) {
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if(!optional.isPresent()){
            return  null;
        }
        return optional.get();
    }

    /**
     * 删除课程图片
     * @param courseId
     * @return
     */
    @Transactional
    public long deleteCoursePic(String courseId) {
        long result = coursePicRepository.deleteByCourseid(courseId);
        return result;
    }

    /**
     * 根据id查询课程计划树
     * @param courseId
     * @return
     */
    public TeachplanNode findTeachListById(String courseId){
        return teachplanMapper.findTeachListById(courseId);
    }

    /**
     * 添加课程计划
     * @param teachplan
     */
    public void addTeachplan(Teachplan teachplan) {
        // 0.判断参数是否都存在
        if(teachplan == null ||
                StringUtils.isEmpty(teachplan.getCourseid()) ||
                StringUtils.isEmpty(teachplan.getPname())){
            // 抛异常
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        String courseid = teachplan.getCourseid();
        // 1.获取父节点ID
        String parentid = teachplan.getParentid();
        if(StringUtils.isEmpty(parentid)){
            // 如果父节点id为空，说明父节点是根节点，需要取出该课程的根节点 Id
            parentid = getTeachplanRoot(courseid);
        }

        // 2.取出父节点信息
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Teachplan teachplanParent = optional.get();
        // 计算当前课程计划的等级
        String gradeParent = teachplanParent.getGrade();
        String grade = String.valueOf(Integer.parseInt(gradeParent) + 1);

        //  3.添加当前节点课程计划
        teachplan.setParentid(parentid);
        teachplan.setCourseid(courseid);
        teachplan.setGrade(grade);
        teachplanRepository.save(teachplan);
    }

    /**
     * 查询课程视图
     * @param id
     * @return
     */
    public CourseView findCourseView(String id) {
        // 根据id查询课程基础信息
        Optional<CourseBase> optional = courseBaseRepository.findById(id);
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        CourseBase courseBase = optional.get();

        // 查询课程图片
        CoursePic coursePic = findCoursePic(id);

        // 查询课程计划
        TeachplanNode teachplanNode = findTeachListById(id);

        // 查询课程营销
        CourseMarket courseMarket = getCourseMarketById(id);

        CourseView courseView = new CourseView();
        courseView.setCourseBase(courseBase);
        courseView.setCourseMarket(courseMarket);
        courseView.setCoursePic(coursePic);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }


    /**
     * 获取根节点，如果是新课，那么根节点不存在，需要创建根节点
     * @param courseid
     * @return
     */
    private String getTeachplanRoot(String courseid) {
        // 首先查询根节点
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseid, "0");
        if(teachplanList == null || teachplanList.size() == 0){// 不存在需要添加根节点
            // 查询此课程，获取名称
            Optional<CourseBase> optional = courseBaseRepository.findById(courseid);
            if(!optional.isPresent()){
                return null;
            }
            CourseBase courseBase = optional.get();

            // 创建根节点
            Teachplan root = new Teachplan();
            root.setCourseid(courseid);
            root.setPname(courseBase.getName());
            root.setParentid(CourseConstant.TEACHPLAN_ROOT_ID);
            root.setGrade(CourseConstant.TEACHPLAN_GRADE_ONE); // 一级节点
            root.setStatus(CourseConstant.TEACHPLAN_STATUS_NO); // 未发布
            teachplanRepository.save(root);
            return root.getId();
        }

        // 存在课程，直接返回
        Teachplan teachplan = teachplanList.get(0);
        return teachplan.getId();
    }

}
