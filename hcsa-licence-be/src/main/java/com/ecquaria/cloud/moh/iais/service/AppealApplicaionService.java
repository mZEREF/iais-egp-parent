package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApplicationDto;

/**
 * AppealApplicaionService
 *
 * @author suocheng
 * @date 2/11/2020
 */

public interface AppealApplicaionService {
    AppealApplicationDto updateFEAppealApplicationDto(String eventRefNum,String submissionId);

    EicRequestTrackingDto getAppEicRequestTrackingDtoByRefNo(String refNo);
}
