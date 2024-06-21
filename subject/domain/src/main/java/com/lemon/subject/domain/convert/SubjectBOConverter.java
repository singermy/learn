package com.lemon.subject.domain.convert;

import com.lemon.subject.domain.entity.SubjectInfoBO;
import com.lemon.subject.domain.entity.SubjectOptionBO;
import com.lemon.subject.infra.basic.entity.SubjectInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubjectBOConverter {
    SubjectBOConverter INSTANCE= Mappers.getMapper(SubjectBOConverter.class);

    SubjectInfo convertBO(SubjectInfoBO subjectInfoBo);

    List<SubjectInfoBO> convertBOList(List<SubjectInfo> subjectInfoList);

    SubjectInfoBO convertOptionAndInfoToBo(SubjectOptionBO optionBO, SubjectInfo subjectInfo);
}
