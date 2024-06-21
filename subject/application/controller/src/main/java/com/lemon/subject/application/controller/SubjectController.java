package com.lemon.subject.application.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.lemon.subject.application.convert.SubjectDTOConverter;
import com.lemon.subject.application.dto.SubjectInfoDTO;
import com.lemon.subject.common.entity.PageResult;
import com.lemon.subject.common.entity.Result;
import com.lemon.subject.domain.entity.SubjectAnswerBO;
import com.lemon.subject.domain.entity.SubjectInfoBO;
import com.lemon.subject.domain.service.SubjectDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.Subject;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/subject")
public class SubjectController {
    @Autowired
    SubjectDomainService subjectDomainService;
    //新增题目
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody SubjectInfoDTO subjectInfoDTO){
        try{
            if(log.isDebugEnabled()){
                log.info("SubjectController.add.dto:{}", JSON.toJSONString(subjectInfoDTO));
            }
            //
            Preconditions.checkNotNull(subjectInfoDTO.getSubjectName(),"题目名称不能为空");
            Preconditions.checkNotNull(subjectInfoDTO.getSubjectDifficult(),"题目难度不能为空");
            Preconditions.checkNotNull(subjectInfoDTO.getSubjectType(), "题目类型不能为空");
            Preconditions.checkNotNull(subjectInfoDTO.getSubjectScore(), "题目分数不能为空");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(subjectInfoDTO.getCategoryIds()), "分类id不能为空");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(subjectInfoDTO.getLabelIds()), "标签id不能为空");
            //
            SubjectInfoBO subjectInfoBo= SubjectDTOConverter.INSTANCE.convertTOBO(subjectInfoDTO);
            List<SubjectAnswerBO> subjectAnswerBOList=SubjectDTOConverter.INSTANCE.convertTOBOList(subjectInfoDTO.getOptionList());
            subjectInfoBo.setOptionList(subjectAnswerBOList);
            log.info("SubjectController.add.bo:{}", JSON.toJSONString(subjectInfoBo));
            subjectDomainService.add(subjectInfoBo);
            return Result.ok(true);
        }catch (Exception e){
            log.error("SubjectCategoryController.add.error:{}", e.getMessage(), e);
            return Result.fail("新增题目失败");
        }
    }

    @PostMapping("/getSubjectPage")
    public Result<PageResult<SubjectInfoDTO>> getSubjectPage(@RequestBody SubjectInfoDTO subjectInfoDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("SubjectController.getSubjectPage.dto:{}", JSON.toJSONString(subjectInfoDTO));
            }
            Preconditions.checkNotNull(subjectInfoDTO.getCategoryId(), "分类id不能为空");
            Preconditions.checkNotNull(subjectInfoDTO.getLabelId(), "标签id不能为空");
            SubjectInfoBO subjectInfoBO = SubjectDTOConverter.INSTANCE.convertTOBO(subjectInfoDTO);
            subjectInfoBO.setPageNo(subjectInfoDTO.getPageNo());
            subjectInfoBO.setPageSize(subjectInfoDTO.getPageSize());
            PageResult<SubjectInfoBO> boPageResult = subjectDomainService.getSubjectPage(subjectInfoBO);
            return Result.ok(boPageResult);
        } catch (Exception e) {
            log.error("SubjectCategoryController.add.error:{}", e.getMessage(), e);
            return Result.fail("分页查询题目失败");
        }
    }

    @PostMapping("querySubjectInfo")
    public Result<SubjectInfoDTO> getSubjectDetail(@RequestBody SubjectInfoDTO subjectInfoDTO){
        try {
            if (log.isInfoEnabled()) {
                log.info("SubjectController.querySubjectInfo.dto:{}", JSON.toJSONString(subjectInfoDTO));
            }
            Preconditions.checkNotNull(subjectInfoDTO.getId(), "题目id不能为空");
            SubjectInfoBO subjectInfoBO = SubjectDTOConverter.INSTANCE.convertTOBO(subjectInfoDTO);
            SubjectInfoBO boResult = subjectDomainService.querySubjectInfo(subjectInfoBO);
            SubjectInfoDTO dto = SubjectDTOConverter.INSTANCE.convertTODTO(boResult);
            return Result.ok(dto);
        } catch (Exception e) {
            log.error("SubjectCategoryController.query.error:{}", e.getMessage(), e);
            return Result.fail("查询题目详情失败");
        }
    }
}
