package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApproveGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealLicenceDto;
import java.util.List;

/**
 * AppealService
 *
 * @author suocheng
 * @date 2/6/2020
 */

public interface AppealService {
    public List<AppealApproveGroupDto> getAppealApproveDtos();
    public AppealLicenceDto createAppealLicenceDto(AppealLicenceDto appealLicenceDto);
    public AppealApplicationDto createAppealApplicationDto(AppealApplicationDto appealApplicationDto);
    //public AppealLicenceDto updateFEAppealLicenceDto(String eventRefNum,String submissionId);

}
