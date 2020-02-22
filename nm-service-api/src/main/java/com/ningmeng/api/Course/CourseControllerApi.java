package com.ningmeng.api.course;

import com.ningmeng.framework.domain.course.CourseBase;
import com.ningmeng.framework.domain.course.CoursePic;
import com.ningmeng.framework.domain.course.Teachplan;
import com.ningmeng.framework.domain.course.ext.TeachplanNode;
import com.ningmeng.framework.domain.course.request.CourseListRequest;
import com.ningmeng.framework.domain.course.response.AddCourseResult;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api(value = "课程管理Api" ,description = "提供课程管理的增删改查")
public interface CourseControllerApi {

    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    public ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("查询我的课程列表")
    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest);

    @ApiOperation("添加课程基础信息")
    public AddCourseResult addCourseBase(CourseBase courseBase);

    @ApiOperation("添加课程图片")
    public ResponseResult addCoursePic(String courseId,String pic);

    @ApiOperation("获取课程基础信息")
    public CoursePic findCoursePic(String courseId);

    @ApiOperation("删除课程信息")
    public ResponseResult deleteCoursePic(String courseId);

}
