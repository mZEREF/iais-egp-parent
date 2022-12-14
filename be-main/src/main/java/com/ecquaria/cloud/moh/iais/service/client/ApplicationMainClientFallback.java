package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * ApplicationMainClientFallback
 *
 * @author suocheng
 * @date 11/26/2019
 */
public class ApplicationMainClientFallback implements ApplicationMainClient{

    @Override
    public FeignResponseEntity<ApplicationViewDto> getAppViewByCorrelationId(String correlationId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppViewByCorrelationId",correlationId);
    }

    @Override
    public FeignResponseEntity<ApplicationDto> getAppByNo(String appNo) {
        return IaisEGPHelper.getFeignResponseEntity("getAppByNo",appNo);
    }

    @Override
    public FeignResponseEntity<ApplicationDto> updateApplication(ApplicationDto applicationDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateApplication",applicationDto);
    }

    @Override
    public FeignResponseEntity<AppPremisesCorrelationDto> getLastAppPremisesCorrelationDtoByCorreId(String appCorreId) {
        return IaisEGPHelper.getFeignResponseEntity("getLastAppPremisesCorrelationDtoByCorreId",appCorreId);
    }

    FeignResponseEntity<ApplicationGroupDto> updateApplication( ApplicationGroupDto applicationGroupDto){
        return IaisEGPHelper.getFeignResponseEntity("updateApplication",applicationGroupDto);
    }


    @Override
    public FeignResponseEntity<List<ApplicationDto>> getGroupAppsByNo(String appGropId) {
        return IaisEGPHelper.getFeignResponseEntity("getGroupAppsByNo",appGropId);
    }

    @Override
    public FeignResponseEntity<ApplicationGroupDto> getAppById(String appGroupId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppById",appGroupId);
    }

    @Override
    public FeignResponseEntity<AppReturnFeeDto> saveAppReturnFee(AppReturnFeeDto appReturnFeeDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppReturnFee",appReturnFeeDto);
    }

    @Override
    public FeignResponseEntity<ProcessFileTrackDto> isFileExistence(Map<String, String> map) {
        return IaisEGPHelper.getFeignResponseEntity("isFileExistence",map);
    }

    @Override
    public FeignResponseEntity<List<AppPremiseMiscDto>> getAppPremiseMiscDtoListByAppId(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremiseMiscDtoListByAppId",appId);
    }

    @Override
    public FeignResponseEntity<AppPremisesCorrelationDto> getAppPremisesCorrelationDtosByAppId(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesCorrelationDtosByAppId",appId);
    }

    @Override
    public FeignResponseEntity<AppPremiseMiscDto> getAppPremisesMisc(String correId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesMisc",correId);
    }

    @Override
    public FeignResponseEntity<ApplicationDto> getApplicationById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationById",id);
    }

    @Override
    public FeignResponseEntity<List<String>> clearHclcodeByAppIds(List<ApplicationDto> applicationDtos) {
        return IaisEGPHelper.getFeignResponseEntity("clearHclcodeByAppIds",applicationDtos);
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> getAppsByLicId(String licId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppsByLicId",licId);
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> updateCessationApplications(List<ApplicationDto> applicationDtos) {
        return IaisEGPHelper.getFeignResponseEntity("updateCessationApplications",applicationDtos);
    }

    @Override
    public FeignResponseEntity<ApplicationGroupDto> getAppGrpByNo(String appGroupNo) {
        return IaisEGPHelper.getFeignResponseEntity("getAppGrpByNo",appGroupNo);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesCorrelationDto>> getPremCorrDtoByAppGroupId(String appGroupId) {
        return IaisEGPHelper.getFeignResponseEntity("getPremCorrDtoByAppGroupId",appGroupId);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> getAppSubmissionByAppId(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppSubmissionByAppId",appId);
    }

    @Override
    public FeignResponseEntity<List<AppEditSelectDto>> getAppEditSelectDto(String appId, String changeType) {
        return IaisEGPHelper.getFeignResponseEntity("getAppEditSelectDto",appId,changeType);
    }

    @Override
    public FeignResponseEntity<String> saveReportResult(ReportResultDto reportResultDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveReportResult",reportResultDto);
    }

    @Override
    public FeignResponseEntity<AppPremisesRecommendationDto> getAppPremRecordByIdAndType(String appPremId, String recomType) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremRecordByIdAndType",appPremId,recomType);
    }

    @Override
    public FeignResponseEntity<AppPremPreInspectionNcDto> getAppNcByAppCorrId(String appCorrId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppNcByAppCorrId",appCorrId);
    }

    @Override
    public FeignResponseEntity<HcsaTaskAssignDto> getUnitNoAndAddressByAppGrpIds(List<String> appGroupIds) {
        return IaisEGPHelper.getFeignResponseEntity("getUnitNoAndAddressByAppGrpIds",appGroupIds);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesOperationalUnitDto>> getUnitNoAndFloorByPremisesId(String premisesId) {
        return IaisEGPHelper.getFeignResponseEntity("getUnitNoAndFloorByPremisesId",premisesId);
    }

    @Override
    public FeignResponseEntity<List<ApplicationGroupDto>> getGroupsByNos(List<String> appGrpNums) {
        return IaisEGPHelper.getFeignResponseEntity("getGroupsByNos",appGrpNums);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesCorrelationDto>> getPremCorrDtoByAppGroupIds(List<String> appGroupIds) {
        return IaisEGPHelper.getFeignResponseEntity("getPremCorrDtoByAppGroupIds",appGroupIds);
    }

    @Override
    public FeignResponseEntity<AppPremisesCorrelationDto> getAppPremCorrByAppNo(String appNo) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremCorrByAppNo",appNo);
    }
}
