package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;

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

    void saveAllSelfAssessment(List<SelfAssessment> selfAssessmentList, String applicationNumber);

    Boolean hasSubmittedSelfAssMtByGroupId(String groupId);

    Boolean hasSubmittedSelfAssMtRfiByCorrId(String corrId);

    void changePendingSelfAssMtStatus(String value, Boolean isGroupId);

    AppPremisesCorrelationDto getCorrelationByAppNo(String appNo);
}
