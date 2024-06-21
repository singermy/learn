package com.lemon.subject.domain.handler;

import com.google.common.base.Preconditions;
import com.lemon.subject.common.enums.IsDeletedFlagEnum;
import com.lemon.subject.common.enums.SubjectInfoTypeEnum;
import com.lemon.subject.domain.convert.SubjectBOConverter;
import com.lemon.subject.domain.convert.SubjectRadioConverter;
import com.lemon.subject.domain.entity.SubjectAnswerBO;
import com.lemon.subject.domain.entity.SubjectInfoBO;
import com.lemon.subject.domain.entity.SubjectOptionBO;
import com.lemon.subject.infra.basic.entity.SubjectRadio;
import com.lemon.subject.infra.basic.service.SubjectRadioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Component
@Slf4j
public class RadioTypeHandler implements SubjectTypeHandler{
    @Autowired
    SubjectRadioService radioService;
    @Override
    public SubjectInfoTypeEnum getHandlerType() {
        return SubjectInfoTypeEnum.RADIO;
    }

    @Override
    public void add(SubjectInfoBO subjectInfoBO) {
        Preconditions.checkNotNull(subjectInfoBO.getOptionList());
        try{
            List<SubjectRadio> subjectRadioList= SubjectRadioConverter.INSTANCE.convertBOToEntityList(subjectInfoBO.getOptionList());
            subjectRadioList.forEach(subjectRadio -> {
            subjectRadio.setSubjectId(subjectInfoBO.getId());
            subjectRadio.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        });
       radioService.batchInsert(subjectRadioList);
        }catch (Exception e)
        {
            log.error("插入失败",e.getMessage());
        }

    }

    @Override
    public SubjectOptionBO query(int subjectId) {
        SubjectRadio subjectRadio = new SubjectRadio();
        subjectRadio.setSubjectId(Long.valueOf(subjectId));
        List<SubjectRadio> result = radioService.queryByCondition(subjectRadio);
        List<SubjectAnswerBO> subjectAnswerBOList = SubjectRadioConverter.INSTANCE.convertEntityToBoList(result);
        SubjectOptionBO subjectOptionBO = new SubjectOptionBO();
        subjectOptionBO.setOptionList(subjectAnswerBOList);
        return subjectOptionBO;
    }
}
