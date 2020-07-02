package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.WithOutRenewalDto;

import java.util.List;

/**
 * WithOutRenewalService
 *
 * @author caijing
 * @date 2020/1/7
 */

public interface WithOutRenewalService {
    WithOutRenewalDto getRenewalViewByLicNo(String licenceNo);

    List<AppSubmissionDto> getAppSubmissionDtos(List<String> licenceIds);

    String getAppGrpNoByAppType(String appType);

    String getLicenceNumberByLicenceId(String licenceId);

    Boolean isChange(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) throws Exception;
}
