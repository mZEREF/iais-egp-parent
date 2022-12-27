package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesDoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppAlignAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppLicBundleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageSearchDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-11-29 09:59
 **/
@Component
public class AppInboxFallback implements AppInboxClient {
    @Override
    public FeignResponseEntity<SearchResult<InboxAppQueryDto>> searchResultFromApp(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchResultFromApp", searchParam);
    }

    @Override
    public FeignResponseEntity<List<String>> getAppGrpIdsByLicenseeIs(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppGrpIdsByLicenseeIs", licenseeId);
    }

    @Override
    public FeignResponseEntity<String> getDraftNumber(String appNo) {
        return IaisEGPHelper.getFeignResponseEntity("getDraftNumber", appNo);
    }

    @Override
    public FeignResponseEntity<List<ApplicationDraftDto>> getDraftList() {
        return IaisEGPHelper.getFeignResponseEntity("getDraftList");
    }

    @Override
    public FeignResponseEntity<ApplicationDraftDto> getDraftInfo(String draftId) {
        return IaisEGPHelper.getFeignResponseEntity("getDraftInfo",draftId);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> saveAppsForRequestForChange(AppSubmissionDto appSubmissionDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppsForRequestForChange",appSubmissionDto);
    }

    @Override
    public FeignResponseEntity<String> getSubmissionId() {
        return IaisEGPHelper.getFeignResponseEntity("getSubmissionId");
    }

    @Override
    public FeignResponseEntity<Integer> getAppDraftNum(InterMessageSearchDto interMessageSearchDto) {
        return IaisEGPHelper.getFeignResponseEntity("getAppDraftNum",interMessageSearchDto);
    }

    @Override
    public FeignResponseEntity<String> updateDraftStatus(String draftNo, String status) {
        return IaisEGPHelper.getFeignResponseEntity("updateDraftStatus",draftNo,status);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesCorrelationDto>> listAppPremisesCorrelation(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("listAppPremisesCorrelation",appId);
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> getAppByLicIdAndExcludeNew(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppByLicIdAndExcludeNew",licenceId);
    }

    @Override
    public FeignResponseEntity<Boolean> isAppealEligibility(String id) {
        return IaisEGPHelper.getFeignResponseEntity("isAppealEligibility",id);
    }

    @Override
    public FeignResponseEntity<Void> updateFeAppStatus(String appId, String appStatus) {
        return IaisEGPHelper.getFeignResponseEntity("updateFeAppStatus",appId);
    }

    @Override
    public FeignResponseEntity<Map<String, Boolean>> listCanCeased(List<String> licIds) {
        return IaisEGPHelper.getFeignResponseEntity("listCanCeased",licIds);
    }

    @Override
    public FeignResponseEntity<ApplicationDto> getApplicarionById(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicarionById",appId);
    }

    @Override
    public FeignResponseEntity<String> selectDarft(Map<String, Object> serviceCodes) {
        return IaisEGPHelper.getFeignResponseEntity("selectDarft",serviceCodes);
    }

    @Override
    public FeignResponseEntity<Boolean> isLiscenceAppealOrCessation(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("isLiscenceAppealOrCessation",licenceId);
    }

    @Override
    public FeignResponseEntity<Boolean> isApplicationWithdrawal(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("isApplicationWithdrawal",appId);
    }

    @Override
    public FeignResponseEntity<List<ApplicationSubDraftDto>> getDraftByLicAppId(String licAppId) {
        return IaisEGPHelper.getFeignResponseEntity("getDraftByLicAppId",licAppId);
    }

    @Override
    public FeignResponseEntity deleteDraftByNo(String draftNo) {
        return IaisEGPHelper.getFeignResponseEntity("deleteDraftByNo",draftNo);
    }

    @Override
    public FeignResponseEntity<ApplicationDraftDto> getDraftInfoByAppNo(String appNo) {
        return IaisEGPHelper.getFeignResponseEntity("getDraftInfoByAppNo",appNo);
    }

    @Override
    public FeignResponseEntity<List<AppGrpPremisesDto>> getPendAppPremises(AppPremisesDoQueryDto appPremisesDoQueryDto){
        return IaisEGPHelper.getFeignResponseEntity("getPendAppPremises",appPremisesDoQueryDto);
    }

    @Override
    public FeignResponseEntity<String> deleteDraftNUmber(List<String> draftNumbers) {
        return IaisEGPHelper.getFeignResponseEntity("deleteDraftNUmber",draftNumbers);
    }

    @Override
    public FeignResponseEntity<List<AppPremiseMiscDto>> getAppPremiseMiscDtoRelateId(String relateId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremiseMiscDtoRelateId",relateId);
    }

    @Override
    public FeignResponseEntity<List<ApplicationGroupDto>> getAppGrpsByLicenseeId(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppGrpsByLicenseeId",licenseeId);
    }

    @Override
    public FeignResponseEntity<ApplicationGroupDto> getApplicationGroup(String groupId) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationGroup",groupId);
    }

    @Override
    public FeignResponseEntity<List<ApplicationGroupDto>> getApplicationGroupsByIds(List<String> appGrpIds) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationGroupsByIds",appGrpIds);
    }

    @Override
    public FeignResponseEntity<ApplicationDto> getApplicationById(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationById",appId);
    }

    @Override
    public FeignResponseEntity<List<ApplicationSubDraftDto>> getDraftByLicAppIdAndStatus(String licAppId, String status) {
        return IaisEGPHelper.getFeignResponseEntity("getDraftByLicAppIdAndStatus",licAppId);
    }

    @Override
    public FeignResponseEntity<List<ApplicationSubDraftDto>> getDraftListBySvcCodeAndStatus(List<String> svcCodeList, String licenseeId, String status, String appType) {
        return IaisEGPHelper.getFeignResponseEntity("getDraftListBySvcCodeAndStatus",svcCodeList);
    }

    @Override
    public FeignResponseEntity<ApplicationDto> getApplicationByCorreId(String appPremCorrId) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationByCorreId",appPremCorrId);
    }

    @Override
    public FeignResponseEntity<List<AppLicBundleDto>> getBundleListByAppNo(String appNo) {
        return IaisEGPHelper.getFeignResponseEntity("getBundleListByAppNo",appNo);
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> getApplicationsByLicenseeId(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationsByLicenseeId",licenseeId);
    }

    @Override
    public FeignResponseEntity<List<AppAlignAppQueryDto>> getActiveApplicationsAddress(String licenseeId, List<String> svcIdList) {
        return IaisEGPHelper.getFeignResponseEntity("getActiveApplicationsAddress",licenseeId,svcIdList);
    }
}