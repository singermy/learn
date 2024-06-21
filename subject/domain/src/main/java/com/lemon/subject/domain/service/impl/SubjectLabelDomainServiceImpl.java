package com.lemon.subject.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.lemon.subject.common.enums.CategoryTypeEnum;
import com.lemon.subject.common.enums.IsDeletedFlagEnum;
import com.lemon.subject.domain.convert.SubjectLabelConverter;
import com.lemon.subject.domain.entity.SubjectLabelBO;
import com.lemon.subject.domain.service.SubjectLabelDomainService;
import com.lemon.subject.infra.basic.entity.SubjectCategory;
import com.lemon.subject.infra.basic.entity.SubjectLabel;
import com.lemon.subject.infra.basic.entity.SubjectMapping;
import com.lemon.subject.infra.basic.service.SubjectCategoryService;
import com.lemon.subject.infra.basic.service.SubjectLabelService;
import com.lemon.subject.infra.basic.service.SubjectMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.security.auth.Subject;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubjectLabelDomainServiceImpl implements SubjectLabelDomainService {
    @Autowired
    SubjectLabelService subjectLabelService;

    @Autowired
    SubjectCategoryService subjectCategoryService;

    @Autowired
    SubjectMappingService subjectMappingService;
    @Override
    public Boolean add(SubjectLabelBO subjectLabelBO) {
        if (log.isInfoEnabled()) {
            log.info("SubjectLabelDomainServiceImpl.add.bo:{}", JSON.toJSONString(subjectLabelBO));
        }
        SubjectLabel subjectLabel = SubjectLabelConverter.INSTANCE
                .convertBoToLabel(subjectLabelBO);
        subjectLabel.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        int count = subjectLabelService.insert(subjectLabel);
        return count > 0;
    }

    @Override
    public Boolean update(SubjectLabelBO subjectLabelBO) {
        if (log.isInfoEnabled()) {
            log.info("SubjectLabelDomainServiceImpl.update.bo:{}", JSON.toJSONString(subjectLabelBO));
        }
        SubjectLabel subjectLabel = SubjectLabelConverter.INSTANCE
                .convertBoToLabel(subjectLabelBO);
        int count = subjectLabelService.update(subjectLabel);
        return count > 0;
    }


    @Override
    public List<SubjectLabelBO> queryLabelByCategoryId(SubjectLabelBO subjectLabelBO) {
        if (log.isInfoEnabled()) {
            log.info("SubjectLabelDomainServiceImpl.query.bo:{}", JSON.toJSONString(subjectLabelBO));
        }

        SubjectCategory subjectCategory = subjectCategoryService.queryById(subjectLabelBO.getCategoryId());
        //如果是1级分类
        if (CategoryTypeEnum.PRIMARY.getCode() == subjectCategory.getCategoryType())
        {
            SubjectLabel subjectLabel = new SubjectLabel();
            subjectLabel.setCategoryId(subjectLabelBO.getCategoryId());
            List<SubjectLabel> labelList = subjectLabelService.queryByCondition(subjectLabel);
            List<SubjectLabelBO> labelBOList = SubjectLabelConverter.INSTANCE.convertLabelToBoList(labelList);
            return labelBOList;
        }else{ //如果是n级分类
            SubjectMapping subjectMapping=new SubjectMapping();
            subjectMapping.setCategoryId(subjectLabelBO.getCategoryId());
            subjectMapping.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.code);
            List<SubjectMapping> subjectMappingList=subjectMappingService.queryByCategoryId(subjectMapping);
            if(CollectionUtils.isEmpty(subjectMappingList)){
                return Collections.emptyList();
            }
            List<Long> labelIdList=subjectMappingList.stream().map(SubjectMapping::getLabelId).collect(Collectors.toList());
            List<SubjectLabel> labelList=subjectLabelService.batchQueryById(labelIdList);
            List<SubjectLabelBO> subjectLabelBOList=SubjectLabelConverter.INSTANCE.convertLabelToBoList(labelList);
            log.info("subjectLabelBOList={}",JSON.toJSONString(subjectLabelBOList));
            return subjectLabelBOList;
        }


    }

    @Override
    public Boolean delete(SubjectLabelBO subjectLabelBO) {
        if (log.isInfoEnabled()) {
            log.info("SubjectLabelDomainServiceImpl.delete.bo:{}", JSON.toJSONString(subjectLabelBO));
        }
        SubjectLabel subjectLabel = SubjectLabelConverter.INSTANCE
                .convertBoToLabel(subjectLabelBO);
        subjectLabel.setIsDeleted(IsDeletedFlagEnum.DELETED.code);
        int count = subjectLabelService.update(subjectLabel);
        return count > 0;
    }


}
