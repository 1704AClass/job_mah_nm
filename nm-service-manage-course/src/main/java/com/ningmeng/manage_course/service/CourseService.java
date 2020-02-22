package com.ningmeng.manage_course.service;

import com.github.pagehelper.PageHelper;
import com.ningmeng.framework.domain.course.CourseBase;
import com.ningmeng.framework.domain.course.CoursePic;
import com.ningmeng.framework.domain.course.Teachplan;
import com.ningmeng.framework.domain.course.ext.CourseInfo;
import com.ningmeng.framework.domain.course.ext.TeachplanNode;
import com.ningmeng.framework.domain.course.request.CourseListRequest;
import com.ningmeng.framework.domain.course.response.AddCourseResult;
import com.ningmeng.framework.exception.ExceptionCast;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.QueryResult;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_course.dao.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Resource
    private TeachplanMapper teachplanMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private TeachplanRepository teachplanRepository;

    @Resource
    private CourseBaseRepository courseBaseRepository;

    @Resource
    private CoursePicRepositroy coursePicRepository;

    //查询课程计划
    public TeachplanNode findTeachplanList(String courseId){
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        return teachplanNode;
    }


    //获取课程根结点，如果没有则添加根结点
    public String getTeachplanRoot(String courseId){
        //校验课程id
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if(!optional.isPresent()){
            return null;
        }
        CourseBase courseBase = optional.get();
        //取出课程计划根结点
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId,"0");
        if(teachplanList == null || teachplanList.size() == 0){
            //新增一个根结点
            Teachplan teachplanRoot = new Teachplan();
            teachplanRoot.setCourseid(courseId);
            teachplanRoot.setPname(courseBase.getName());
            teachplanRoot.setParentid("0");
            teachplanRoot.setGrade("1");
            teachplanRoot.setStatus("0");
            teachplanRepository.save(teachplanRoot);
            return teachplanRoot.getId();
        }
        Teachplan teachplan = teachplanList.get(0);
        return teachplan.getId();
    }


    //添加课程计划
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan){
        //效验课程id和课程计划名称
        if(teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //取出课程id
        String courseid = teachplan.getCourseid();
        //取出父节点id
        String parentid = teachplan.getParentid();
        if(StringUtils.isEmpty(parentid)){
            //如果父节点为空则获取根结点
            parentid = getTeachplanRoot(courseid);
        }
        //取出父节点信息
        Optional<Teachplan> teachplanOptional = teachplanRepository.findById(parentid);
        if(!teachplanOptional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //父节点
        Teachplan teachplanParent = teachplanOptional.get();
        //父节点级别
        String parentGrade = teachplanParent.getGrade();
        //设置父节点
        teachplan.setParentid(parentid);
        teachplan.setStatus("0");//未发布
        //子节点的级别，根据父节点来判断
        if(parentGrade.equals("1")){
            teachplan.setGrade("2");
        }else if(parentGrade.equals("2")){
            teachplan.setGrade("3");
        }
        //设置课程id
        teachplan.setCourseid(teachplanParent.getCourseid());
        teachplanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }


    @Transactional
    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest) {
        if(courseListRequest == null){
            courseListRequest = new CourseListRequest();
        }
        if(page <= 0){
            page = 0;
        }
        if(size <= 0){
            size = 20;
        }
        //设置分页参数
        PageHelper.startPage(page,size);
        //分页查询s
        Page<CourseInfo> courseInfoPage = courseMapper.findCourseListPage(courseListRequest);
        QueryResult<CourseInfo> queryResult = new QueryResult();
        queryResult.setList(courseInfoPage.getContent());
        queryResult.setTotal(courseInfoPage.getTotalElements());
        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }


    @Transactional
    public AddCourseResult addCourseBase(CourseBase courseBase) {
        //课程状态默认为未发布
        courseBase.setStatus("202001");
        courseBaseRepository.save(courseBase);
        return new AddCourseResult(CommonCode.SUCCESS,courseBase.getId());
    }

    @Transactional
    public ResponseResult saveCoursePic(String courseId, String pic) {
        //查询课程图片
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        CoursePic coursePic = null;
        if(picOptional.isPresent()){
            coursePic = picOptional.get();
        }
        //没有课程图片则新建对象
        if(coursePic == null){
            coursePic = new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        //保存课程图片
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }


    public CoursePic findCoursePic(String courseId) {
        Optional<CoursePic> one = coursePicRepository.findById(courseId);
        if(one.isPresent()){
            return one.get();
        }
        return null;
    }


    //删除课程图片
    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        //执行删除，返回1表示删除成功，返回0表示删除失败
        long result = coursePicRepository.deleteByCourseid(courseId);
        if(result > 0){
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }



}
