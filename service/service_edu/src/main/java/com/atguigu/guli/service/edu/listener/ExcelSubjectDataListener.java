package com.atguigu.guli.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.ExcelSubjectData;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor//全参构造函数
@NoArgsConstructor//无参构造函数
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {

    private SubjectMapper subjectMapper;

    /**
     * 遍历每一条记录
     *
     * @param excelSubjectData
     * @param analysisContext
     */
    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        log.info("解析到一条记录：{}", excelSubjectData);
        // 读取数据
        String levelOneTitle = excelSubjectData.getLevelOneTitle();
        String levelTwoTitle = excelSubjectData.getLevelTwoTitle();

        log.info("解析到一级标题：{}", levelOneTitle);
        log.info("解析到二级标题：{}", levelTwoTitle);

        String parentId = null;
        // 去重 一级类别
        Subject subjectLevelOne = this.getByTile(levelOneTitle);
        if (subjectLevelOne == null) {
            // 组装一级类别
            Subject subject = new Subject();
            subject.setParentId("0");
            subject.setTitle(levelOneTitle);

            //存入数据库
            subjectMapper.insert(subject);
            parentId = subject.getId();
        } else {
            parentId = subjectLevelOne.getId();
        }

        // 去重 二级类别
        Subject subjectLevleTwo = this.getSubByTile(levelTwoTitle, parentId);
        if (subjectLevleTwo == null) {
            Subject subject = new Subject();
            subject.setTitle(levelTwoTitle);
            subject.setParentId(parentId);

            subjectMapper.insert(subject);
        }

    }


    /**
     * 数据的收尾工作
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("全部数据解析完成");
    }

    /**
     * 根据一级分类名称 去重
     *
     * @param title
     * @return
     */
    private Subject getByTile(String title) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", "0");
        return subjectMapper.selectOne(queryWrapper);
    }

    /**
     * 根据分类名称和父id 去重
     *
     * @param title
     * @return
     */
    private Subject getSubByTile(String title, String parentId) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", parentId);//二级分类
        return subjectMapper.selectOne(queryWrapper);
    }
}
