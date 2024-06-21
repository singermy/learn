package com.lemon.subject.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.lemon.subject.common.entity.PageResult;
import com.lemon.subject.common.entity.Result;
import com.lemon.subject.common.enums.IsDeletedFlagEnum;
import com.lemon.subject.common.enums.SubjectInfoTypeEnum;
import com.lemon.subject.domain.convert.SubjectBOConverter;
import com.lemon.subject.domain.entity.SubjectInfoBO;
import com.lemon.subject.domain.entity.SubjectOptionBO;
import com.lemon.subject.domain.handler.SubjectTypeHandler;
import com.lemon.subject.domain.handler.SubjectTypeHandlerFactory;
import com.lemon.subject.domain.service.SubjectDomainService;
import com.lemon.subject.infra.basic.entity.SubjectInfo;
import com.lemon.subject.infra.basic.entity.SubjectLabel;
import com.lemon.subject.infra.basic.entity.SubjectMapping;
import com.lemon.subject.infra.basic.service.SubjectInfoService;
import com.lemon.subject.infra.basic.service.SubjectLabelService;
import com.lemon.subject.infra.basic.service.SubjectMappingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyAccessorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubjectDomainServiceImpl implements SubjectDomainService {
    @Autowired
    SubjectInfoService subjectInfoService;

    @Autowired
    private SubjectTypeHandlerFactory subjectTypeHandlerFactory;

    @Autowired
    SubjectMappingService subjectMappingService;

    @Autowired
    SubjectLabelService subjectLabelService;

    @Override
    @Transactional
    public void add(SubjectInfoBO subjectInfoBo) {
        if (log.isInfoEnabled()) {
            log.info("SubjectInfoDomainServiceImpl.add.bo:{}", JSON.toJSONString(subjectInfoBo));
        }
        SubjectInfo subjectInfo= SubjectBOConverter.INSTANCE.convertBO(subjectInfoBo);
        subjectInfo.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.code);
        subjectInfoService.insert(subjectInfo);
        SubjectTypeHandler handler= subjectTypeHandlerFactory.getHandler(subjectInfo.getSubjectType());
        handler.add(subjectInfoBo);

        //
        List<Integer> categoryIds = subjectInfoBo.getCategoryIds();
        List<Integer> labelIds = subjectInfoBo.getLabelIds();
        List<SubjectMapping> mappingList = categoryIds.stream()
                .flatMap(categoryId -> labelIds.stream()
                        .map(labelId -> {
                            SubjectMapping subjectMapping = new SubjectMapping();
                            subjectMapping.setSubjectId(subjectInfo.getId());
                            subjectMapping.setCategoryId(Long.valueOf(categoryId));
                            subjectMapping.setLabelId(Long.valueOf(labelId));
                            subjectMapping.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
                            return subjectMapping;
                        }))
                .collect(Collectors.toList());
       subjectMappingService.batchInsert(mappingList);
    }

    @Override
    public PageResult<SubjectInfoBO> getSubjectPage(SubjectInfoBO subjectInfoBO) {
        PageResult<SubjectInfoBO> subjectInfoBOResult=new PageResult<>();
        subjectInfoBOResult.setPageNo(subjectInfoBO.getPageNo());
        subjectInfoBOResult.setPageSize(subjectInfoBO.getPageSize());
        int start=(subjectInfoBO.getPageNo()-1)*subjectInfoBO.getPageSize();
        SubjectInfo subjectInfo=SubjectBOConverter.INSTANCE.convertBO(subjectInfoBO);
        int count=subjectInfoService.countByCondition(subjectInfo,subjectInfoBO.getLabelId(),subjectInfoBO.getCategoryId());
        if(count==0){
            return subjectInfoBOResult;
        }
        List<SubjectInfo> subjectInfoList = subjectInfoService.queryPage(subjectInfo, subjectInfoBO.getCategoryId()
                , subjectInfoBO.getLabelId(), start, subjectInfoBO.getPageSize());
        List<SubjectInfoBO> subjectInfoBOS = SubjectBOConverter.INSTANCE.convertBOList(subjectInfoList);
        subjectInfoBOResult.setResult(subjectInfoBOS);
       subjectInfoBOResult.setTotal(count);
        return subjectInfoBOResult;

    }

    @Override
    public SubjectInfoBO querySubjectInfo(SubjectInfoBO subjectInfoBO) {
        SubjectInfo subjectInfo=SubjectBOConverter.INSTANCE.convertBO(subjectInfoBO);
        subjectInfo=subjectInfoService.queryById(subjectInfo.getId());
        if(subjectInfo==null){
            return new SubjectInfoBO();
        }
        SubjectTypeHandler handler= subjectTypeHandlerFactory.getHandler(subjectInfo.getSubjectType());
        SubjectOptionBO optionBO = handler.query(subjectInfoBO.getId().intValue());
        SubjectInfoBO bo = SubjectBOConverter.INSTANCE.convertOptionAndInfoToBo(optionBO, subjectInfo);
        SubjectMapping subjectMapping = new SubjectMapping();
        subjectMapping.setSubjectId(subjectInfo.getId());
        subjectMapping.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        List<SubjectMapping> mappingList = subjectMappingService.queryLabelId(subjectMapping);
        List<Long> labelIdList = mappingList.stream().map(SubjectMapping::getLabelId).collect(Collectors.toList());
        List<SubjectLabel> labelList = subjectLabelService.batchQueryById(labelIdList);
        List<String> labelNameList = labelList.stream().map(SubjectLabel::getLabelName).collect(Collectors.toList());
        bo.setLabelName(labelNameList);
        return bo;



    }
}
