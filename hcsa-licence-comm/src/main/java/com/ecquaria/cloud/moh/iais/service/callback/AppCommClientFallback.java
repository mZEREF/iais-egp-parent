package com.ecquaria.cloud.moh.iais.service.callback;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesDoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppAlignAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpSecondAddrDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.client.AppCommClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

/**
 * @Auther chenlei on 5/3/2022.
 */
public class AppCommClientFallback implements AppCommClient {

    @Override
    public FeignResponseEntity<List<ApplicationDto>> getAppByLicIdAndExcludeNew(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity(licenceId);
    }

    @Override
    public FeignResponseEntity<Boolean> isLiscenceAppealOrCessation(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity(licenceId);
    }

    @Override
    public FeignResponseEntity<AppGrpPremisesDto> getAppGrpPremisesById(String appPreId) {
        return IaisEGPHelper.getFeignResponseEntity(appPreId);
    }

    @Override
    public FeignResponseEntity<List<AppGrpPremisesDto>> getActivePendingPremises(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity(licenseeId);
    }

    @Override
    public FeignResponseEntity<List<AppSvcDocDto>> getMaxSeqNumSvcDocList(String appGrpId) {
        return IaisEGPHelper.getFeignResponseEntity(appGrpId);
    }

    @Override
    public FeignResponseEntity<List<AppGrpPremisesDto>> getPendAppPremises(AppPremisesDoQueryDto appPremisesDoQueryDto) {
        return IaisEGPHelper.getFeignResponseEntity(JsonUtil.parseToJson(appPremisesDoQueryDto));
    }

    @Override
    public FeignResponseEntity<AppGrpPremisesDto> getActivePremisesByAppNo(String appNo) {
        return IaisEGPHelper.getFeignResponseEntity(appNo);
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> getApplicationsByGroupNo(String groupNo) {
        return IaisEGPHelper.getFeignResponseEntity(groupNo);
    }

    @Override
    public FeignResponseEntity<ApplicationDto> getApplicationById(String appId) {
        return IaisEGPHelper.getFeignResponseEntity(appId);
    }

    @Override
    public FeignResponseEntity<ApplicationDto> getApplicationDtoByAppNo(String appNo) {
        return IaisEGPHelper.getFeignResponseEntity(appNo);
    }

    @Override
    public FeignResponseEntity<List<AppSvcVehicleDto>> getActiveVehicles() {
        return IaisEGPHelper.getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<List<AppSvcVehicleDto>> getActivePendingVehicles() {
        return IaisEGPHelper.getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<List<AppGrpPremisesDto>> getActivePendingPremisesByPremType(String premType) {
        return IaisEGPHelper.getFeignResponseEntity(premType);
    }

    @Override
    public FeignResponseEntity<List<AppPremiseMiscDto>> getAppPremiseMiscsByConds(String type, String appId,
            List<String> excludeStatus) {
        return IaisEGPHelper.getFeignResponseEntity(type, appId, excludeStatus);
    }

    @Override
    public FeignResponseEntity<Void> saveAppPremiseMiscDto(List<AppPremiseMiscDto> appPremiseMiscDtoList) {
        return IaisEGPHelper.getFeignResponseEntity(appPremiseMiscDtoList);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> getRfiAppSubmissionDtoByAppNo(String appNo) {
        return IaisEGPHelper.getFeignResponseEntity(appNo);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> getAppSubmissionDtoByAppNo(String appNo, Boolean onlyItself) {
        return IaisEGPHelper.getFeignResponseEntity(appNo, onlyItself);
    }

    @Override
    public FeignResponseEntity<AppGroupMiscDto> saveAppGroupMiscDto(AppGroupMiscDto appGroupMiscDto) {
        return IaisEGPHelper.getFeignResponseEntity(JsonUtil.parseToJson(appGroupMiscDto));
    }

    @Override
    public FeignResponseEntity<AppSvcDocDto> getMaxVersionSvcComDoc(String appGrpId, String configDocId, String seqNum) {
        return IaisEGPHelper.getFeignResponseEntity(appGrpId, configDocId, seqNum);
    }

    @Override
    public FeignResponseEntity<AppSvcDocDto> getMaxVersionSvcSpecDoc(AppSvcDocDto appSvcDocDto, String appNo) {
        return IaisEGPHelper.getFeignResponseEntity(appSvcDocDto, appNo);
    }

    @Override
    public FeignResponseEntity<List<AppPremSpecialisedDto>> getAppPremSpecialisedDtoList(List<String> appPremCorreIds) {
        return IaisEGPHelper.getFeignResponseEntity(appPremCorreIds);
    }

    @Override
    public FeignResponseEntity<List<AppGrpSecondAddrDto>> saveSecondAddress(List<AppGrpSecondAddrDto> addrDtos) {
        return null;
    }

    @Override
    public FeignResponseEntity<List<AppAlignAppQueryDto>> getActiveApplicationsAddress(String licenseeId, List<String> svcIdList) {
        return IaisEGPHelper.getFeignResponseEntity(licenseeId);
    }
}
