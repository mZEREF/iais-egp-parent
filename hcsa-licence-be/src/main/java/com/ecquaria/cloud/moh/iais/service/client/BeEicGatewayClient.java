package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionForAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.BeSyncCompareDataRequest;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.BeSyncCompareDataResponse;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.SyncDataBody;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFeSupportDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRectificationSaveDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

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

    public FeignResponseEntity<EventBusLicenceGroupDtos> createLicence(EventBusLicenceGroupDtos eventBusLicenceGroupDtos,
                 String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-licence-transport-licence", HttpMethod.POST, eventBusLicenceGroupDtos,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, EventBusLicenceGroupDtos.class);
    }

    public FeignResponseEntity<ApplicationDto> updateApplication(ApplicationDto applicationDto,
                            String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/iais-application", HttpMethod.PUT, applicationDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, ApplicationDto.class);
    }

    public FeignResponseEntity<InterMessageDto> saveInboxMessage(InterMessageDto interInboxDto,
              String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/iais-inter-inbox-message", HttpMethod.POST, interInboxDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, InterMessageDto.class);
    }

    public FeignResponseEntity<LicPremisesReqForInfoDto> createLicPremisesReqForInfoFe(LicPremisesReqForInfoDto licPremisesReqForInfoDto,
            String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/rfi-fe-bridge", HttpMethod.POST, licPremisesReqForInfoDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, LicPremisesReqForInfoDto.class);
    }

    public FeignResponseEntity<AppEditSelectDto> createAppEditSelectDto(AppEditSelectDto  appEditSelectDto,
                                             String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/app-req-for-info", HttpMethod.POST, appEditSelectDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, AppEditSelectDto.class);
    }

    public FeignResponseEntity<Void> updateStatus(@RequestBody Map<String,List<String>> map,
                                      String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/iais-application/status", HttpMethod.PUT, map,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, Void.class);
    }

    public FeignResponseEntity<AppealLicenceDto> updateAppealLicence(AppealLicenceDto appealLicenceDto,
                                                         String date, String authorization, String dateSec,
                                                         String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-licence-transport-appeal", HttpMethod.PUT, appealLicenceDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, AppealLicenceDto.class);
    }

    public FeignResponseEntity<AppealApplicationDto> updateAppealApplication(@RequestBody AppealApplicationDto appealApplicationDto,
                                                    String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/appeal-beApplication", HttpMethod.PUT, appealApplicationDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, AppealApplicationDto.class);
    }

    public FeignResponseEntity<List> createAppPremisesInspecApptDto(List<AppPremisesInspecApptDto> appPremisesInspecApptDtos,
                                                                    String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/inspec-sync-rectification", HttpMethod.POST, appPremisesInspecApptDtos,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, AppPremisesInspecApptDto.class);
    }

    public FeignResponseEntity<ApptFeConfirmDateDto> reSchedulingSaveFeDate(@RequestBody ApptFeConfirmDateDto apptFeConfirmDateDto,
                                                        String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-app-insdate-down", HttpMethod.POST, apptFeConfirmDateDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, ApptFeConfirmDateDto.class);
    }

    public FeignResponseEntity<InspRectificationSaveDto> beCreateNcData(InspRectificationSaveDto inspRectificationSaveDto,
                                                    String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-inspect-nc-down", HttpMethod.POST, inspRectificationSaveDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, InspRectificationSaveDto.class);
    }

    public FeignResponseEntity<HcsaRiskFeSupportDto> feCreateRiskData(HcsaRiskFeSupportDto hcsaRiskFeSupportDto,
                                                   String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsal/riskscore-configs", HttpMethod.POST, hcsaRiskFeSupportDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, HcsaRiskFeSupportDto.class);
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

    public FeignResponseEntity<String> getAppNo(String applicationType,
                                                                 String date, String authorization, String dateSec,
                                                                 String authorizationSec) {
        Map<String, Object> param = IaisCommonUtils.genNewHashMap(1);
        param.put("type", applicationType);
        return IaisEGPHelper.callEicGatewayWithParam(gateWayUrl + "/v1/new-app-no", HttpMethod.GET, param,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, String.class);
    }


    public FeignResponseEntity<Void> syncInspPeriodToFe(HcsaServicePrefInspPeriodDto period,
                                         String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-insp-period-sync", HttpMethod.PUT, period,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, Void.class);
    }

    public FeignResponseEntity<AppSubmissionForAuditDto> saveAppForAuditToFe(AppSubmissionForAuditDto appSubmissionForAuditDto,
                                                                  String date, String authorization, String dateSec,
                                                                  String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-audit-insp-app-sync", HttpMethod.POST, appSubmissionForAuditDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, AppSubmissionForAuditDto.class);
    }

    public FeignResponseEntity<HttpStatus> syncAdhocItemData(AdhocCheckListConifgDto configDto,
                                              String date, String authorization, String dateSec,
                                              String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-adhoc-chkl-sync", HttpMethod.POST, configDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, HttpStatus.class);
    }

    public FeignResponseEntity<HttpStatus> updateFeLicDto(List<LicenceDto> licenceDtos, String date,
                                                   String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-licence-status", HttpMethod.PUT, licenceDtos,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, HttpStatus.class);
    }

    public FeignResponseEntity<HttpStatus> saveFeServiceConfig(HcsaServiceConfigDto hcsaServiceConfigDto,
                                                        String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-service-config-sync", HttpMethod.POST, hcsaServiceConfigDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, HttpStatus.class);
    }

    public FeignResponseEntity<List> updateAppSvcKeyPersonnelDto(List<AppSvcKeyPersonnelDto> appealPersonnel,
                                                               String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/app-svc-key-personel", HttpMethod.POST, appealPersonnel,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, AppSvcKeyPersonnelDto.class);
    }
}
