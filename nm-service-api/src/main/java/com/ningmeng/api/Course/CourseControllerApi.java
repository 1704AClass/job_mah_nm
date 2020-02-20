package com.ningmeng.api.Course;

import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.domain.course.Teachplan;
import com.ningmeng.framework.domain.course.ext.TeachplanNode;
import com.ningmeng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "",description = "")
public interface CourseControllerApi {



    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String couseId);


    @ApiOperation("添加课程计划")
    public ResponseResult addTeachplan(Teachplan teachplan);
}
