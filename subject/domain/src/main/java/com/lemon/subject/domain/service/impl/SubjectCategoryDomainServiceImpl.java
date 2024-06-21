package com.lemon.subject.domain.service.impl;

import com.lemon.subject.common.enums.IsDeletedFlagEnum;
import com.lemon.subject.domain.convert.SubjectCategoryConverter;
import com.lemon.subject.domain.entity.SubjectCategoryBO;
import com.lemon.subject.domain.service.SubjectCategoryDomainService;
import com.lemon.subject.infra.basic.entity.SubjectCategory;
import com.lemon.subject.infra.basic.service.SubjectCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SubjectCategoryDomainServiceImpl implements SubjectCategoryDomainService {
    @Resource
    private SubjectCategoryService subjectCategoryService;

    @Override
    public void insert(SubjectCategoryBO subjectCategoryBO) {
        SubjectCategory subjectCategory= SubjectCategoryConverter.INSTANCE.convertBoToCategory(subjectCategoryBO);
        subjectCategoryService.insert(subjectCategory);
    }

    @Override
    public List<SubjectCategoryBO> queryCategory(SubjectCategoryBO subjectCategoryBO) {
        SubjectCategory subjectCategory=SubjectCategoryConverter.INSTANCE.convertBoToCategory(subjectCategoryBO);
        subjectCategory.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
       List<SubjectCategory> subjectCategoryList=subjectCategoryService.queryPrimaryCategory(subjectCategory);
        List<SubjectCategoryBO> subjectCategoryBOList= SubjectCategoryConverter.INSTANCE.convertBoToCategory(subjectCategoryList);
        return subjectCategoryBOList;
    }

    @Override
    public Boolean update(SubjectCategoryBO subjectCategoryBO) {
        SubjectCategory subjectCategory=SubjectCategoryConverter.INSTANCE.convertBoToCategory(subjectCategoryBO);
        int count=subjectCategoryService.update(subjectCategory);
        return count>0;
    }

    @Override
    public Boolean delete(SubjectCategoryBO subjectCategoryBO) {
        SubjectCategory subjectCategory=SubjectCategoryConverter.INSTANCE.convertBoToCategory(subjectCategoryBO);
        subjectCategory.setIsDeleted(IsDeletedFlagEnum.DELETED.code);
        int count=subjectCategoryService.update(subjectCategory);
        return count>0;
    }
}
