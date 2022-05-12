package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionForAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.BeSyncCompareDataRequest;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.BeSyncCompareDataResponse;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.SyncDataBody;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.giro.GiroDeductionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFeSupportDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRectificationSaveDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * EicGatewayClient
 *
 * @author suocheng
 * @date 2019/12/14 17:33
 */
@Component
public class BeEicGatewayClient {

    @Value("${iais.intra.gateway.url}")
    private String gateWayUrl;

    @Value("${iais.hmac.keyId}")
    private String keyId;

    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;

    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Autowired
    private EicRequestTrackingHelper requestTrackingHelper;

    @Value("${spring.application.name}")
    private String currentApp;

    @Value("${iais.current.domain}")
    private String currentDomain;

    public <T, R> R callEicWithTrack(T obj, Function<T, R> function, String actionMethod) {
        return requestTrackingHelper.callEicWithTrack(obj, function, this.getClass().getName(), actionMethod, currentApp, currentDomain,
                EicClientConstant.APPLICATION_CLIENT);
    }

    public <T, R> R callEicWithTrack(T obj, Function<T, R> function, Class<?> actionClass, String actionMethod) {
        return requestTrackingHelper.callEicWithTrack(obj, function, actionClass.getName(), actionMethod, currentApp, currentDomain,
                EicClientConstant.APPLICATION_CLIENT);
    }

    public <T> void callEicWithTrack(T obj, Consumer<T> consumer, Class<?> actionClass, String actionMethod) {
        requestTrackingHelper.callEicWithTrack(obj, consumer, actionClass.getName(), actionMethod, currentApp, currentDomain,
                EicClientConstant.APPLICATION_CLIENT);
    }

    public <T> void callEicWithTrack(T obj, Consumer<T> consumer, Class<?> actionClass, String actionMethod, int clientId) {
        requestTrackingHelper.callEicWithTrack(obj, consumer, actionClass.getName(), actionMethod, currentApp, currentDomain,
                clientId);
    }

    public FeignResponseEntity<EventBusLicenceGroupDtos> createLicence(EventBusLicenceGroupDtos eventBusLicenceGroupDtos,
                 String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-licence-transport-licence", HttpMethod.POST, eventBusLicenceGroupDtos,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, EventBusLicenceGroupDtos.class);
    }

    public FeignResponseEntity<List> getProfessionalDetail(ProfessionalParameterDto professionalParameterDto,
                                                           String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/prs-server/prs-api/getProfessionalDetail", HttpMethod.POST, professionalParameterDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, ProfessionalResponseDto.class);
    }
//
//    public FeignResponseEntity<List> getDisciplinaryRecord(ProfessionalParameterDto professionalParameterDto,
//                                                           String date, String authorization, String dateSec, String authorizationSec) {
//        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/prs-server/prs-api/getDisciplinaryRecord", HttpMethod.POST, professionalParameterDto,
//                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, DisciplinaryRecordResponseDto.class);
//    }

    public FeignResponseEntity<ApplicationDto> updateApplication(ApplicationDto applicationDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/iais-application", HttpMethod.PUT, applicationDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                ApplicationDto.class);
    }

    public FeignResponseEntity<InterMessageDto> saveInboxMessage(InterMessageDto interInboxDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/iais-inter-inbox-message", HttpMethod.POST, interInboxDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                InterMessageDto.class);
    }

    public FeignResponseEntity<LicPremisesReqForInfoDto> createLicPremisesReqForInfoFe(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/rfi-fe-bridge", HttpMethod.POST, licPremisesReqForInfoDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                LicPremisesReqForInfoDto.class);
    }

    public FeignResponseEntity<Void> updateGiroAccountInfo(List<GiroAccountInfoDto> giroAccountInfoDtoList) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/giro-acct-sync", HttpMethod.POST, giroAccountInfoDtoList,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                Void.class);
    }

    public FeignResponseEntity<AppEditSelectDto> createAppEditSelectDto(AppEditSelectDto  appEditSelectDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/app-req-for-info", HttpMethod.POST, appEditSelectDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                AppEditSelectDto.class);
    }

    public FeignResponseEntity<Void> updateStatus(@RequestBody Map<String,List<String>> map,
                                      String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/iais-application/status", HttpMethod.PUT, map,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, Void.class);
    }

    public FeignResponseEntity<AppealLicenceDto> updateAppealLicence(AppealLicenceDto appealLicenceDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-licence-transport-appeal", HttpMethod.PUT, appealLicenceDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                AppealLicenceDto.class);
    }

    public FeignResponseEntity<AppealApplicationDto> updateAppealApplication(@RequestBody AppealApplicationDto appealApplicationDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/appeal-beApplication", HttpMethod.PUT, appealApplicationDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                AppealApplicationDto.class);
    }

    public FeignResponseEntity<List> createAppPremisesInspecApptDto(List<AppPremisesInspecApptDto> appPremisesInspecApptDtos) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/inspec-sync-rectification", HttpMethod.POST,
                appPremisesInspecApptDtos,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                AppPremisesInspecApptDto.class);
    }

    /**
     * EIC Tracking List
     *
     * @param jsonList
     */
    public void createFeAppPremisesInspecApptDto(String jsonList) {
        if (StringUtil.isEmpty(jsonList)) {
            return;
        }
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtos = JsonUtil.parseToList(jsonList, AppPremisesInspecApptDto.class);
        createAppPremisesInspecApptDto(appPremisesInspecApptDtos);
    }

    public FeignResponseEntity<ApptFeConfirmDateDto> reSchedulingSaveFeDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-app-insdate-down", HttpMethod.POST, apptFeConfirmDateDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                ApptFeConfirmDateDto.class);
    }

    public FeignResponseEntity<InspRectificationSaveDto> beCreateNcData(InspRectificationSaveDto inspRectificationSaveDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-inspect-nc-down", HttpMethod.POST, inspRectificationSaveDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                InspRectificationSaveDto.class);
    }

    public FeignResponseEntity<HcsaRiskFeSupportDto> feCreateRiskData(HcsaRiskFeSupportDto hcsaRiskFeSupportDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsal/riskscore-configs", HttpMethod.POST, hcsaRiskFeSupportDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                HcsaRiskFeSupportDto.class);
    }

    public FeignResponseEntity<List> compareFeData(BeSyncCompareDataRequest beSyncCompareDataRequest,
                                               String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/hcsa-chklst-sync-comp", HttpMethod.POST, beSyncCompareDataRequest,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, BeSyncCompareDataResponse.class);
    }

    public FeignResponseEntity<Void> saveSyncData(SyncDataBody syncDataBody, String date,
                                                               String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-chklst-sync-save", HttpMethod.POST, syncDataBody,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, Void.class);
    }

    public FeignResponseEntity<String> getAppNo(String applicationType) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        Map<String, Object> param = IaisCommonUtils.genNewHashMap(1);
        param.put("type", applicationType);
        return IaisEGPHelper.callEicGatewayWithParam(gateWayUrl + "/v1/new-app-no", HttpMethod.GET, param,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                String.class);
    }

    public FeignResponseEntity<List> doStripeRefunds(List<AppReturnFeeDto> appReturnFeeDtos) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/stripe-refund", HttpMethod.POST, appReturnFeeDtos,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                PaymentRequestDto.class);
    }


    public FeignResponseEntity<Void> syncInspPeriodToFe(HcsaServicePrefInspPeriodDto period) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-insp-period-sync", HttpMethod.PUT, period,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                Void.class);
    }

    public FeignResponseEntity<AppSubmissionForAuditDto> saveAppForAuditToFe(AppSubmissionForAuditDto appSubmissionForAuditDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-audit-insp-app-sync", HttpMethod.POST,
                appSubmissionForAuditDto, MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(),
                signature2.authorization(), AppSubmissionForAuditDto.class);
    }

    public FeignResponseEntity<HttpStatus> syncAdhocItemData(AdhocCheckListConifgDto configDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-adhoc-chkl-sync", HttpMethod.POST, configDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                HttpStatus.class);
    }

    public FeignResponseEntity<List> updateFeLicDto(List<LicenceDto> licenceDtos) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/hcsa-licence-status", HttpMethod.PUT, licenceDtos,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                LicenceDto.class);
    }

    /**
     * EIC Tracking List
     *
     * @param jsonList
     */
    public void updateFeLicDto(String jsonList) {
        if (StringUtil.isEmpty(jsonList)) {
            return;
        }
        List<LicenceDto> licenceDtos = JsonUtil.parseToList(jsonList, LicenceDto.class);
        updateFeLicDto(licenceDtos);
    }

    public FeignResponseEntity<HttpStatus> saveFeServiceConfig(HcsaServiceConfigDto hcsaServiceConfigDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-service-config-sync", HttpMethod.POST, hcsaServiceConfigDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                HttpStatus.class);
    }

    public FeignResponseEntity<List> updateAppSvcKeyPersonnelDto(List<AppSvcKeyPersonnelDto> appealPersonnel) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/app-svc-key-personel", HttpMethod.POST, appealPersonnel,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                AppSvcKeyPersonnelDto.class);
    }

    public FeignResponseEntity<List> saveHciNameByDto(List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/app-premises-sync", HttpMethod.PUT, appGrpPremisesEntityDtos,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                AppGrpPremisesEntityDto.class);
    }

    public FeignResponseEntity<List> deRegisterAcra(List<String> licenseeIds,
                                                     String date, String authorization, String dateSec,
                                                     String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/acra-deregister", HttpMethod.PUT, licenseeIds,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, String.class);
    }

    public FeignResponseEntity<Void> saveFePostApplicationDtos(ApplicationListFileDto applicationListFileDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/post-inspection", HttpMethod.POST, applicationListFileDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                Void.class);
    }

    public FeignResponseEntity<SearchResult> giroDeductionDtoSearchResult(SearchParam searchParam) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForSearchResult(gateWayUrl + "/v1/giro-payment-query", HttpMethod.POST, searchParam,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(),
                signature2.authorization(), GiroDeductionDto.class);
    }

    public FeignResponseEntity<List> updateDeductionDtoSearchResultUseGroups(List<GiroDeductionDto> groups) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/giro-result-status", HttpMethod.PUT, groups,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                GiroDeductionDto.class);
    }

    public FeignResponseEntity<List> updateFeApplicationGroupStatus(List<ApplicationGroupDto> applicationGroupDtos) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/app-grp-status", HttpMethod.PUT, applicationGroupDtos,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                ApplicationGroupDto.class);
    }

    public FeignResponseEntity<List> savePremises(List<PremisesDto> premisesDtos) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/appeal-change-premises", HttpMethod.POST, premisesDtos,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                PremisesDto.class);
    }

    public FeignResponseEntity<String> saveFileApplication(ProcessFileTrackDto processFileTrackDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/app-file-sync-trackings", HttpMethod.POST, processFileTrackDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                String.class);
    }
}
