package com.lemon.subject.domain.service;

import com.lemon.subject.domain.entity.SubjectCategoryBO;
import com.lemon.subject.infra.basic.entity.SubjectCategory;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SubjectCategoryDomainService{
    void insert(SubjectCategoryBO subjectCategory);

    List<SubjectCategoryBO> queryCategory(SubjectCategoryBO categoryBO);


    Boolean update(SubjectCategoryBO subjectCategoryBO);

    Boolean delete(SubjectCategoryBO subjectCategoryBO);
}
