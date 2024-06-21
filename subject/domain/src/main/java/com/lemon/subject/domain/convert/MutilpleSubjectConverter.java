package com.lemon.subject.domain.convert;

import com.lemon.subject.domain.entity.SubjectAnswerBO;
import com.lemon.subject.infra.basic.entity.SubjectMultiple;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MutilpleSubjectConverter {
    MutilpleSubjectConverter INSTANCE= Mappers.getMapper(MutilpleSubjectConverter.class);

    SubjectMultiple convertBoToEntity(SubjectAnswerBO subjectAnswerBO);

    List<SubjectAnswerBO> convertEntityToBoList(List<SubjectMultiple> subjectMultipleList);
}
