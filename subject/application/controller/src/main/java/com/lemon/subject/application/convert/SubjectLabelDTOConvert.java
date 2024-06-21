package com.lemon.subject.application.convert;

import com.lemon.subject.application.dto.SubjectCategoryDTO;
import com.lemon.subject.application.dto.SubjectLabelDTO;
import com.lemon.subject.domain.entity.SubjectCategoryBO;
import com.lemon.subject.domain.entity.SubjectLabelBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubjectLabelDTOConvert {
    SubjectLabelDTOConvert INSTANCE= Mappers.getMapper(SubjectLabelDTOConvert.class);

    SubjectLabelBO convertToLabelBo(SubjectLabelDTO subjectLabelDTO);

    List<SubjectLabelDTO> convertBOToLabelDTOList(List<SubjectLabelBO> resultList);
}
