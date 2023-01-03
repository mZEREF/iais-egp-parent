package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppliSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.WithdrawApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoring.excel.MonitoringSheetsDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2019/11/26 14:29
 */
@Slf4j
public class ApplicationFeClientFallback implements ApplicationFeClient {

    @Override
    public FeignResponseEntity<List<ApplicationGroupDto>> updateFeApplicationGroupStatus(
            List<ApplicationGroupDto> applicationGroupDtos) {
        return IaisEGPHelper.getFeignResponseEntity("updateFeApplicationGroupStatus", applicationGroupDtos);
    }

    @Override
    public FeignResponseEntity<AppPremisesCorrelationDto> getCorrelationByAppNo(String appNo) {
        return IaisEGPHelper.getFeignResponseEntity("getCorrelationByAppNo", appNo);
    }

    @Override
    public FeignResponseEntity<String> fileAll(List<String> grpIds) {
        return IaisEGPHelper.getFeignResponseEntity("fileAll", grpIds);
    }

    @Override
    public FeignResponseEntity<String> recDatesToString() {
        return IaisEGPHelper.getFeignResponseEntity("recDatesToString");
    }

    @Override
    public FeignResponseEntity<ApplicationDto> getApplicationById(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationById", appId);
    }

    @Override
    public FeignResponseEntity<Map<String, List<AppPremPreInspectionNcDocDto>>> recFileId() {
        return IaisEGPHelper.getFeignResponseEntity("recFileId");
    }


    @Override
    public FeignResponseEntity<ApplicationDto> updateApplication(ApplicationDto applicationDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateApplication", applicationDto);
    }

    @Override
    public FeignResponseEntity<String> savedFileName(String fileName) {
        return IaisEGPHelper.getFeignResponseEntity("savedFileName", fileName);
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> listApplicationByGroupId(String groupId) {
        return IaisEGPHelper.getFeignResponseEntity("listApplicationByGroupId", groupId);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> draftNumberGet(String draftNumber) {
        return IaisEGPHelper.getFeignResponseEntity("draftNumberGet", draftNumber);
    }

    @Override
    public FeignResponseEntity<List<AppSubmissionDto>> getAppSubmissionDtoDrafts(String draftNumber) {
        return IaisEGPHelper.getFeignResponseEntity("getAppSubmissionDtoDrafts", draftNumber);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> saveSubmision(AppSubmissionDto appSubmissionDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveSubmision", appSubmissionDto);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> saveApps(AppSubmissionDto appSubmissionDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveApps", appSubmissionDto);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> saveWithdrawnApps(Map<String, Object> map) {
        return IaisEGPHelper.getFeignResponseEntity("saveWithdrawnApps", map);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> saveReqeustInformationSubmision(
            AppSubmissionRequestInformationDto appSubmissionRequestInformationDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveReqeustInformationSubmision", appSubmissionRequestInformationDto);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> saveRFCOrRenewRequestInformation(
            AppSubmissionRequestInformationDto appSubmissionRequestInformationDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveRFCOrRenewRequestInformation", appSubmissionRequestInformationDto);
    }


    @Override
    public FeignResponseEntity<List<AppPremisesCorrelationDto>> listAppPremisesCorrelation(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("listAppPremisesCorrelation", appId);
    }

    @Override
    public FeignResponseEntity<String> doUpDate(ApplicationGroupDto applicationGroupDto) {
        return IaisEGPHelper.getFeignResponseEntity("doUpDate", applicationGroupDto);
    }

    @Override
    public FeignResponseEntity<String> doPaymentUpDate(ApplicationGroupDto applicationGroupDto) {
        return IaisEGPHelper.getFeignResponseEntity("doPaymentUpDate", applicationGroupDto);
    }

    @Override
    public FeignResponseEntity<String> paymentUpDateByGrpNo(ApplicationGroupDto applicationGroup) {
        return IaisEGPHelper.getFeignResponseEntity("paymentUpDateByGrpNo", applicationGroup);
    }

    @Override
    public FeignResponseEntity<String> getSubmissionId() {
        return IaisEGPHelper.getFeignResponseEntity("getSubmissionId");
    }

    @Override
    public FeignResponseEntity<List<AppPremisesSelfDeclChklDto>> getAppPremisesSelfDeclByCorrelationId(String correlationId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesSelfDeclByCorrelationId", correlationId);
    }

    @Override
    public FeignResponseEntity<String> inActiveLastVersionByGroupId(List<String> lastVersionId) {
        return IaisEGPHelper.getFeignResponseEntity("inActiveLastVersionByGroupId", lastVersionId);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesSelfDeclChklDto>> getAppPremisesSelfDeclChklListByGroupId(String groupId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesSelfDeclChklListByGroupId", groupId);
    }

    @Override
    public FeignResponseEntity<List<String>> getItemIdsByAppNo(String appPremCorrId) {
        return IaisEGPHelper.getFeignResponseEntity("getItemIdsByAppNo", appPremCorrId);
    }

    @Override
    public FeignResponseEntity<List<AppPremPreInspectionNcDocDto>> saveAppNcDoc(List<AppPremPreInspectionNcDocDto> dtoList) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppNcDoc", dtoList);
    }

    @Override
    public FeignResponseEntity<AppPremPreInspectionNcDocDto> updateAppNcDoc(
            AppPremPreInspectionNcDocDto appPremPreInspectionNcDocDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateAppNcDoc", appPremPreInspectionNcDocDto);
    }

    @Override
    public FeignResponseEntity<List<AppPremPreInspectionNcDocDto>> getNcDocListByItemId(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getNcDocListByItemId", id);
    }

    @Override
    public FeignResponseEntity<AppPremisesPreInspectionNcItemDto> updateAppPreItemNc(
            AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateAppPreItemNc", appPremisesPreInspectionNcItemDto);
    }

    @Override
    public FeignResponseEntity<AppPremPreInspectionNcDto> getPreNcByPreNcId(String preNcId) {
        return IaisEGPHelper.getFeignResponseEntity("getPreNcByPreNcId", preNcId);
    }

    @Override
    public FeignResponseEntity<AppPremPreInspectionNcDto> saveAppPremPreNc(AppPremPreInspectionNcDto appPremPreInspectionNcDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppPremPreNc", appPremPreInspectionNcDto);
    }

    @Override
    public FeignResponseEntity<AppPremPreInspectionNcDto> updateAppPremPreNc(AppPremPreInspectionNcDto appPremPreInspectionNcDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateAppPremPreNc", appPremPreInspectionNcDto);
    }

    @Override
    public FeignResponseEntity<ApplicationViewDto> searchAppByNo(String appNo) {
        return IaisEGPHelper.getFeignResponseEntity("searchAppByNo", appNo);
    }

    @Override
    public FeignResponseEntity<AppPremPreInspectionNcDto> getAppPremPreInsNcDtoByAppCorrId(String appCorrId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremPreInsNcDtoByAppCorrId", appCorrId);
    }

    @Override
    public FeignResponseEntity<AppPremisesPreInspectionNcItemDto> createAppNcItemDto(
            AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto) {
        return IaisEGPHelper.getFeignResponseEntity("createAppNcItemDto", appPremisesPreInspectionNcItemDto);
    }

    @Override
    public FeignResponseEntity<ApplicationGroupDto> getApplicationGroup(String appGroupId) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationGroup", appGroupId);
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> applicationIsRenwalByOriginId() {
        return IaisEGPHelper.getFeignResponseEntity("applicationIsRenwalByOriginId");
    }

    @Override
    public FeignResponseEntity<AppPremisesRecommendationDto> getAppPremRecordByIdAndType(String appPremId, String recomType) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremRecordByIdAndType", appPremId, recomType);
    }

    @Override
    public FeignResponseEntity<Void> updateStatus(Map<String, List<String>> map) {
        return IaisEGPHelper.getFeignResponseEntity("updateStatus", map);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesCorrelationDto>> appPremCorrDtosByApptViewDtos(List<ApptViewDto> apptViewDtos) {
        return IaisEGPHelper.getFeignResponseEntity("appPremCorrDtosByApptViewDtos", apptViewDtos);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> saveDraft(AppSubmissionDto appSubmissionDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveDraft", appSubmissionDto);
    }

    @Override
    public FeignResponseEntity<List<AppSubmissionDto>> saveDrafts(List<AppSubmissionDto> appSubmissionDtos) {
        return IaisEGPHelper.getFeignResponseEntity("saveDrafts", appSubmissionDtos);
    }

    @Override
    public FeignResponseEntity<Void> updateDrafts(String licenseeId, List<String> licenceIds, String excludeDraftNo) {
        return IaisEGPHelper.getFeignResponseEntity(licenceIds, excludeDraftNo);
    }

    @Override
    public FeignResponseEntity<AppGrpPremisesDto> getAppGrpPremisesByCorrId(String corrId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppGrpPremisesByCorrId", corrId);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesSelfDeclChklDto>> saveAllSelfAssessment(List<SelfAssessment> selfAssessmentList) {
        return IaisEGPHelper.getFeignResponseEntity("saveAllSelfAssessment", selfAssessmentList);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> saveAppsForRequestForChange(AppSubmissionDto appSubmissionDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppsForRequestForChange", appSubmissionDto);
    }

    @Override
    public FeignResponseEntity<List<ApplicationGroupDto>> getApplicationGroupsByIds(List<String> appGrpIds) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationGroupsByIds", appGrpIds);
    }

    @Override
    public FeignResponseEntity<ApplicationDto> getApplicationDtoByVersion(String applicationNo) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationDtoByVersion", applicationNo);
    }

    @Override
    public FeignResponseEntity<AppealPageDto> submitAppeal(AppealPageDto appealDto) {
        return IaisEGPHelper.getFeignResponseEntity("submitAppeal", appealDto);
    }

    @Override
    public FeignResponseEntity<AppInsRepDto> getHciNameAndAddress(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("getHciNameAndAddress", appId);
    }

    @Override
    public FeignResponseEntity<List<AppSubmissionDto>> saveAppsForRequestForChangeByList(List<AppSubmissionDto> appSubmissionDtos) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppsForRequestForChangeByList", appSubmissionDtos);
    }

    @Override
    public FeignResponseEntity<List<AppSubmissionDto>> saveAppsForRequestForGoupAndAppChangeByList(
            List<AppSubmissionDto> appSubmissionDtos) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppsForRequestForGoupAndAppChangeByList", appSubmissionDtos);
    }

    @Override
    public FeignResponseEntity<ApplicationDto> getApplicationsByLicenceId(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationsByLicenceId", licenceId);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> saveAppsForRenew(AppSubmissionDto appSubmissionDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppsForRenew", appSubmissionDto);
    }

    @Override
    public FeignResponseEntity<ApplicationDto> getApplicationByCorreId(String appPremCorrId) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationByCorreId", appPremCorrId);
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> getPremisesApplicationsByCorreId(String appPremCorrId) {
        return IaisEGPHelper.getFeignResponseEntity("getPremisesApplicationsByCorreId", appPremCorrId);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesCorrelationDto>> getLastAppPremisesCorrelationDtoByCorreId(String appCorreId) {
        return IaisEGPHelper.getFeignResponseEntity("getLastAppPremisesCorrelationDtoByCorreId", appCorreId);
    }

    @Override
    public FeignResponseEntity<AppPremiseMiscDto> getAppPremisesMisc(String correId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesMisc", correId);
    }

    @Override
    public FeignResponseEntity<List<AppSvcPrincipalOfficersDto>> getAppGrpPersonnelByGrpId(String grpId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppGrpPersonnelByGrpId", grpId);
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> getApplicationsByLicId(String licId) {
        return null;
    }

    @Override
    public FeignResponseEntity<List<AppGrpPersonnelDto>> getAppGrpPersonnelDtosByGrpId(String grpId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppGrpPersonnelDtosByGrpId", grpId);
    }

    @Override
    public FeignResponseEntity<AppPremisesSpecialDocDto> getAppPremisesSpecialDocDtoByCorreId(String correld) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesSpecialDocDtoByCorreId", correld);
    }

    @Override
    public FeignResponseEntity<List<AppSvcKeyPersonnelDto>> getAppSvcKeyPersonnel(ApplicationDto applicationDto) {
        return IaisEGPHelper.getFeignResponseEntity("getAppSvcKeyPersonnel", applicationDto);
    }

    @Override
    public FeignResponseEntity<ProcessFileTrackDto> isFileExistence(Map<String, String> map) {
        return IaisEGPHelper.getFeignResponseEntity("isFileExistence", map);
    }

    @Override
    public FeignResponseEntity<Boolean> isUseReason(String id, String reason) {
        return IaisEGPHelper.getFeignResponseEntity("isUseReason", id, reason);
    }

    @Override
    public FeignResponseEntity<String> getRequestForInfo(String applicationId) {
        return IaisEGPHelper.getFeignResponseEntity("getRequestForInfo", applicationId);
    }

    @Override
    public FeignResponseEntity<String> selectDarft(Map<String, Object> serviceCodes) {
        return IaisEGPHelper.getFeignResponseEntity("selectDarft", serviceCodes);
    }

    @Override
    public FeignResponseEntity<ApplicationGroupDto> createApplicationDataByWithOutRenewal(RenewDto renewDto) {
        return IaisEGPHelper.getFeignResponseEntity("createApplicationDataByWithOutRenewal", renewDto);
    }

    @Override
    public FeignResponseEntity<Boolean> isAppealEligibility(String id) {
        return IaisEGPHelper.getFeignResponseEntity("isAppealEligibility", id);
    }

    @Override
    public FeignResponseEntity<Integer> getApplicationSelfAssMtStatusByGroupId(String groupId) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationSelfAssMtStatusByGroupId", groupId);
    }

    @Override
    public FeignResponseEntity<List<SelfAssessment>> receiveSelfAssessmentDataByCorrId(String corrId) {
        return IaisEGPHelper.getFeignResponseEntity("receiveSelfAssessmentDataByCorrId", corrId);
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> updateApplicationList(List<ApplicationDto> applicationDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("updateApplicationList", applicationDtoList);
    }

    @Override
    public FeignResponseEntity<ApplicationDto> updateApplicationDto(ApplicationDto applicationDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateApplicationDto", applicationDto);
    }

    @Override
    public FeignResponseEntity<String> deleteOverdueDraft(String draftValidity) {
        return IaisEGPHelper.getFeignResponseEntity("deleteOverdueDraft", draftValidity);
    }

    @Override
    public FeignResponseEntity<AppFeeDetailsDto> saveAppFeeDetails(AppFeeDetailsDto appFeeDetailsDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppFeeDetails", appFeeDetailsDto);
    }

    @Override
    public FeignResponseEntity<AppFeeDetailsDto> getAppFeeDetailsDtoByApplicationNo(String applicationNo) {
        return IaisEGPHelper.getFeignResponseEntity("getAppFeeDetailsDtoByApplicationNo", applicationNo);
    }

    @Override
    public FeignResponseEntity<List<AppGrpPremisesDto>> getAppGrpPremisesDtoByHciName(String hciName, String licencessId,
            String premType) {
        return IaisEGPHelper.getFeignResponseEntity("getAppGrpPremisesDtoByHciName", hciName, licencessId, premType);

    }

    @Override
    public FeignResponseEntity<Boolean> isApplicationWithdrawal(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("isApplicationWithdrawal", appId);
    }

    @Override
    public FeignResponseEntity<Boolean> isLiscenceAppealOrCessation(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("isLiscenceAppealOrCessation", licenceId);
    }

    @Override
    public FeignResponseEntity<AppPremiseMiscDto> getAppPremiseMiscDtoByAppId(String appIdOrLicenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremiseMiscDtoByAppId", appIdOrLicenceId);
    }


    @Override
    public FeignResponseEntity<List<ApplicationDto>> saveApplicationDtos(List<ApplicationDto> applicationDtos) {
        return IaisEGPHelper.getFeignResponseEntity("saveApplicationDtos", applicationDtos);
    }

    @Override
    public FeignResponseEntity<Void> invalidApplicationDtos(List<ApplicationDto> applicationDtos) {
        return IaisEGPHelper.getFeignResponseEntity("invalidApplicationDtos", applicationDtos);
    }

    @Override
    public FeignResponseEntity<ApplicationDto> getApplicationByCorrId(String corrId) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationByCorrId", corrId);
    }

    @Override
    public FeignResponseEntity<List<ApplicationGroupDto>> getAppGrpDtoPaying() {
        return IaisEGPHelper.getFeignResponseEntity("getAppGrpDtoPaying");
    }

    @Override
    public FeignResponseEntity<String> deleteDraftNUmber(List<String> draftNumbers) {
        return IaisEGPHelper.getFeignResponseEntity("deleteDraftNUmber", draftNumbers);
    }

    @Override
    public FeignResponseEntity<List<WithdrawApplicationDto>> getApplicationByAppTypesAndStatus(List<String[]> appTandS,
            String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationByAppTypesAndStatus", appTandS, licenseeId);
    }

    @Override
    public FeignResponseEntity<List<ApplicationSubDraftDto>> getDraftByLicAppId(String licAppId) {
        return IaisEGPHelper.getFeignResponseEntity("getDraftByLicAppId", licAppId);
    }

    @Override
    public FeignResponseEntity deleteDraftByNo(String draftNo) {
        return IaisEGPHelper.getFeignResponseEntity("deleteDraftByNo", draftNo);
    }

    @Override
    public FeignResponseEntity<List<AppEditSelectDto>> getAppEditSelectDtos(String appId, String changeType) {
        return IaisEGPHelper.getFeignResponseEntity("getAppEditSelectDtos", appId, changeType);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> saveRfcCessationSubmision(
            AppSubmissionRequestInformationDto appSubmissionRequestInformationDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveRfcCessationSubmision", appSubmissionRequestInformationDto);
    }

    @Override
    public FeignResponseEntity<List<AppPremiseMiscDto>> getAppPremiseMiscDtoRelateId(String relateId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremiseMiscDtoRelateId", relateId);
    }

    @Override
    public FeignResponseEntity<WithdrawnDto> getWithdrawAppInfo(String appNo) {
        return IaisEGPHelper.getFeignResponseEntity("getWithdrawAppInfo", appNo);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> saveRfcWithdrawSubmission(
            AppSubmissionRequestInformationDto appSubmissionRequestInformationDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveRfcWithdrawSubmission", appSubmissionRequestInformationDto);
    }

    @Override
    public FeignResponseEntity<List<AppliSpecialDocDto>> getAppliSpecialDocDtoByGroupId(String groupId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppliSpecialDocDtoByGroupId", groupId);
    }

    @Override
    public FeignResponseEntity<List<AppliSpecialDocDto>> getAppliSpecialDocDtoByCorrId(String corrId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppliSpecialDocDtoByCorrId", corrId);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> getAppSubmissionDtoByAppGrpNo(String appGrpNo) {
        return IaisEGPHelper.getFeignResponseEntity("getAppSubmissionDtoByAppGrpNo", appGrpNo);
    }

    @Override
    public FeignResponseEntity<ApplicationGroupDto> updateAppGrpPmtStatus(ApplicationGroupDto applicationGroupDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateAppGrpPmtStatus", applicationGroupDto);
    }

    @Override
    public FeignResponseEntity<ApplicationGroupDto> getAppGrpByAppNo(String appNo) {
        return IaisEGPHelper.getFeignResponseEntity("getAppGrpByAppNo", appNo);
    }

    @Override
    public FeignResponseEntity<String> updateDraftStatus(String draftNo, String status) {
        return IaisEGPHelper.getFeignResponseEntity("updateDraftStatus", draftNo, status);
    }

    @Override
    public FeignResponseEntity<List<ApplicationSubDraftDto>> getDraftListBySvcCodeAndStatus(List<String> svcCodeList,
            String licenseeId, String status, String appType) {
        return IaisEGPHelper.getFeignResponseEntity("getDraftListBySvcCodeAndStatus", svcCodeList, licenseeId, status, appType);
    }

    @Override
    public FeignResponseEntity<AppGroupMiscDto> getAppGroupMiscDtoByGrpIdAndTypeAndStatus(String appGrpId, String miscType,
            String status) {
        return IaisEGPHelper.getFeignResponseEntity("getAppGroupMiscDtoByGrpIdAndTypeAndStatus", appGrpId, miscType, status);
    }

    @Override
    public FeignResponseEntity<List<AppDeclarationMessageDto>> getAppDeclarationMessageDto(String appGrpId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppDeclarationMessageDto", appGrpId);
    }

    @Override
    public FeignResponseEntity<List<AppDeclarationDocDto>> getAppDeclarationDocDto(String appGrpId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppDeclarationDocDto", appGrpId);
    }

    @Override
    public FeignResponseEntity<List<AppSvcVehicleDto>> getAppSvcVehicleDtoByVehicleNumber(String vehicleNumber) {
        return IaisEGPHelper.getFeignResponseEntity("getAppSvcVehicleDtoByVehicleNumber", vehicleNumber);
    }

    @Override
    public FeignResponseEntity<ApplicationGroupDto> updateAppGrpPmtStatus(ApplicationGroupDto applicationGroupDto, String giroAccNo) {
        return IaisEGPHelper.getFeignResponseEntity("updateAppGrpPmtStatus", applicationGroupDto, giroAccNo);
    }

    @Override
    public FeignResponseEntity<List<ApplicationSubDraftDto>> getDraftByLicAppIdAndStatus(String licAppId, String status) {
        return IaisEGPHelper.getFeignResponseEntity("getDraftByLicAppIdAndStatus", licAppId, status);
    }

    @Override
    public FeignResponseEntity<String> inActiveDeclaration(AppSubmissionDto appSubmissionDto) {
        return IaisEGPHelper.getFeignResponseEntity("inActiveDeclaration", appSubmissionDto);
    }

    @Override
    public FeignResponseEntity<List<AppGrpPremisesDto>> getActivePendingPremises(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity(licenseeId);
    }

    @Override
    public FeignResponseEntity<List<AppPremiseMiscDto>> getAppPremiseMiscsByConds(String type, String appId,
            List<String> excludeStatus) {
        return IaisEGPHelper.getFeignResponseEntity(type, appId, excludeStatus);
    }

    @Override
    public FeignResponseEntity<List<ProcessFileTrackDto>> allNeedProcessFile() {
        return IaisEGPHelper.getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<ProcessFileTrackDto> updateProcessFileTrack(ProcessFileTrackDto processFileTrackDto) {
        return IaisEGPHelper.getFeignResponseEntity(processFileTrackDto);
    }

    @Override
    public FeignResponseEntity<Void> saveFeData(ApplicationListFileDto applicationListFileDto) {
        return IaisEGPHelper.getFeignResponseEntity(applicationListFileDto);
    }

    @Override
    public FeignResponseEntity<MonitoringSheetsDto> getMonitoringAppSheetsDto() {
        return IaisEGPHelper.getFeignResponseEntity();
    }

}
