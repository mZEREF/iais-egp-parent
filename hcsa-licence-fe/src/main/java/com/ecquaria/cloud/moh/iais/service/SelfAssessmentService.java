package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;

import java.util.List;

/**
 * @Author: yichen
 * @Description:
 * @Date:2020/5/6
 **/

public interface SelfAssessmentService {
    List<SelfAssessment> receiveSelfAssessmentByGroupId(String groupId);

    List<SelfAssessment> receiveSubmittedSelfAssessmentDataByGroupId(String groupId);

    List<SelfAssessment> receiveSelfAssessmentRfiByCorrId(String corrId);

    Boolean saveAllSelfAssessment(List<SelfAssessment> selfAssessmentList);

    Boolean hasSubmittedSelfAssMt(String groupId);

    void changePendingSelfAssMtStatus(String groupId);
}
