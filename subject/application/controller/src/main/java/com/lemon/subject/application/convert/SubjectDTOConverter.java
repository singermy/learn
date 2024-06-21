package com.lemon.subject.application.convert;

import com.lemon.subject.application.dto.SubjectAnswerDTO;
import com.lemon.subject.application.dto.SubjectInfoDTO;
import com.lemon.subject.domain.entity.SubjectAnswerBO;
import com.lemon.subject.domain.entity.SubjectInfoBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubjectDTOConverter {
    SubjectDTOConverter INSTANCE= Mappers.getMapper(SubjectDTOConverter.class);

    SubjectInfoBO convertTOBO(SubjectInfoDTO subjectInfoDTO);

    List<SubjectAnswerBO> convertTOBOList(List<SubjectAnswerDTO> optionList);

    SubjectInfoDTO convertTODTO(SubjectInfoBO boResult);
}
