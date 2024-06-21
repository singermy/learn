package com.lemon.subject.domain.handler;

import com.lemon.subject.common.enums.SubjectInfoTypeEnum;
import com.lemon.subject.domain.entity.SubjectInfoBO;
import com.lemon.subject.domain.entity.SubjectOptionBO;
import com.lemon.subject.infra.basic.entity.SubjectInfo;

public interface SubjectTypeHandler {
    SubjectInfoTypeEnum getHandlerType();

    void add(SubjectInfoBO subjectInfoBO);

    SubjectOptionBO query(int subjectId);


}
