package com.lemon.subject.domain.convert;

import com.lemon.subject.domain.entity.SubjectAnswerBO;
import com.lemon.subject.infra.basic.entity.SubjectRadio;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubjectRadioConverter {
    SubjectRadioConverter INSTANCE= Mappers.getMapper(SubjectRadioConverter.class);

    SubjectRadio convertBoToEntity(SubjectAnswerBO subjectAnswerBO);

    List<SubjectRadio> convertBOToEntityList(List<SubjectAnswerBO> optionList);

    List<SubjectAnswerBO> convertEntityToBoList(List<SubjectRadio> result);
}
