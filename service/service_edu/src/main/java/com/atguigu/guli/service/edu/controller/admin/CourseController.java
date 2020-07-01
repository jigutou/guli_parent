package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.CourseQueryVo;
import com.atguigu.guli.service.edu.entity.vo.CourseVo;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author han
 * @since 2020-06-13
 */
@RequestMapping("admin/edu/course")
@Api(tags = "课程管理")
@CrossOrigin//允许跨域访问
@Slf4j
@RestController
public class CourseController {

    @Autowired
    CourseService courseService;

    @ApiOperation("新增课程")
    @PostMapping("save-course-info")
    public R saveCourseInfo(@ApiParam(value = "课程基本信息", required = true)
                            @RequestBody
                                    CourseInfoForm courseInfoForm) {

        String courseId = courseService.saveCourseInfo(courseInfoForm);
        return R.ok().data("courseId", courseId).message("保存成功");

    }

    @ApiOperation("根据id查询课程")
    @GetMapping("course-info/{id}")
    public R getCourseInfoById(@ApiParam(value = "课程id", required = true)
                               @PathVariable
                                       String id) {
        CourseInfoForm courseInfoForm = courseService.getCourseInfoById(id);
        if (courseInfoForm != null) {
            return R.ok().data("item", courseInfoForm);
        } else {
            return R.ok().message("数据不存在");
        }
    }

    @ApiOperation("更新课程")
    @PutMapping("update-course-info")
    public R updateCourseInfo(@ApiParam(value = "课程 form", required = true)
                              @RequestBody
                                      CourseInfoForm courseInfoForm) {
        courseService.updateCourseInfoById(courseInfoForm);
        return R.ok().message("修改成功");
    }

    @ApiOperation("课程分页列表")
    @GetMapping("/list/{page}/{limit}")
    public R listPage(@ApiParam(value = "当前页码", required = true) @PathVariable Long page,
                      @ApiParam(value = "每页记录数", required = true) @PathVariable Long limit,
                      @ApiParam("课程列表查询对象") CourseQueryVo courseQueryVo) {

        Page<CourseVo> pageParam = new Page<>(page, limit);
        IPage<CourseVo> pageModel = courseService.selectPage(pageParam, courseQueryVo);
        List<CourseVo> records = pageModel.getRecords();
        long total = pageModel.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation(value = "据ID删除课程")
    @DeleteMapping("/remove/{id}")
    public R removeById(@ApiParam(value = "课程id", required = true)
                        @PathVariable String id) {

        //TODO 删除视频：VOD
        //在此处调用vod中的删除视频文件的接口

        //删除封面：OSS
        courseService.removeCoverById(id);

        //删除课程
        boolean result = courseService.removeCourseById(id);

        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message(("数据不存在"));
        }
    }
}

