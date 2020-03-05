package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEicRequestTrackingDto;

/**
 * AppealApplicaionService
 *
 * @author suocheng
 * @date 2/11/2020
 */

public interface AppealApplicaionService {
    public AppealApplicationDto updateFEAppealApplicationDto(String eventRefNum);

    public AppEicRequestTrackingDto getAppEicRequestTrackingDtoByRefNo(String refNo);
}
