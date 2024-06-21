package com.lemon.subject.domain.service;

import com.lemon.subject.common.entity.PageResult;
import com.lemon.subject.domain.entity.SubjectInfoBO;

public interface SubjectDomainService {
    void add(SubjectInfoBO subjectInfoBo);

    PageResult<SubjectInfoBO> getSubjectPage(SubjectInfoBO subjectInfoBO);

    SubjectInfoBO querySubjectInfo(SubjectInfoBO subjectInfoBO);
}
