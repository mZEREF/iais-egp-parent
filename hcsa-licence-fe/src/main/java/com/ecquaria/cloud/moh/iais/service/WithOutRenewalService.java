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

    boolean isReplace(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) ;

    boolean isUpdate(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) ;

    boolean isEditDoc(AppSubmissionDto newAppSubmissionDto,AppSubmissionDto oldAppSubmissionDto);

    List<String> isUpdateCgo(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) ;

    List<String> isUpdatePo(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) ;

    List<String> isUpdateDpo(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) ;

    List<String> isUpdateMat(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) ;



}
