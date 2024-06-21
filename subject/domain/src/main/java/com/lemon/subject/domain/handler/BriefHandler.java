package com.lemon.subject.domain.handler;

import com.alibaba.fastjson.JSON;
import com.lemon.subject.common.enums.IsDeletedFlagEnum;
import com.lemon.subject.common.enums.SubjectInfoTypeEnum;
import com.lemon.subject.domain.convert.BriefSubjectConverter;
import com.lemon.subject.domain.convert.SubjectBOConverter;
import com.lemon.subject.domain.entity.SubjectAnswerBO;
import com.lemon.subject.domain.entity.SubjectInfoBO;
import com.lemon.subject.domain.entity.SubjectOptionBO;
import com.lemon.subject.infra.basic.entity.SubjectBrief;
import com.lemon.subject.infra.basic.service.SubjectBriefService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.auth.Subject;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class BriefHandler implements SubjectTypeHandler{
    @Autowired
    SubjectBriefService subjectBriefService;

    @Override
    public SubjectInfoTypeEnum getHandlerType() {
        return SubjectInfoTypeEnum.BRIEF;
    }

    @Override
    public void add(SubjectInfoBO subjectInfoBO) {
        log.info("BriefHandler:{}"+ JSON.toJSONString(subjectInfoBO));

        SubjectBrief subjectBrief = new SubjectBrief();
        BeanUtils.copyProperties(subjectInfoBO,subjectBrief);
        log.info("BriefHandler:SubjectBrief{}"+JSON.toJSONString(subjectBrief));
        if(subjectInfoBO.getId()!=null)
        subjectBrief.setSubjectId(subjectInfoBO.getId().intValue());
        subjectBrief.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        subjectBriefService.insert(subjectBrief);
    }

    @Override
    public SubjectOptionBO query(int subjectId) {
        SubjectBrief subjectBrief = new SubjectBrief();
        subjectBrief.setSubjectId(subjectId);
        SubjectBrief result = subjectBriefService.queryByCondition(subjectBrief);
        SubjectOptionBO subjectOptionBO = new SubjectOptionBO();
        subjectOptionBO.setSubjectAnswer(result.getSubjectAnswer());
        return subjectOptionBO;
    }
}
