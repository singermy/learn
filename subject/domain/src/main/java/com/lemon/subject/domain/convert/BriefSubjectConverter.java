package com.lemon.subject.domain.convert;

import com.lemon.subject.domain.entity.SubjectInfoBO;
import com.lemon.subject.infra.basic.entity.SubjectBrief;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BriefSubjectConverter {
    BriefSubjectConverter INSTANCE = Mappers.getMapper(BriefSubjectConverter.class);

    SubjectBrief convertBoToEntity(SubjectInfoBO subjectInfoBO);

    SubjectInfoBO convertToBO(SubjectBrief subjectBrief);
}
