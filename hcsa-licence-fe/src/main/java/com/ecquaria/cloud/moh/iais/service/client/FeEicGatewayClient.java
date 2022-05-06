package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.FeSelfAssessmentSyncDataDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptCalendarStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ProcessReSchedulingDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschApptGrpPremsQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.arcaUen.GenerateUENDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationTruckDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GobalRiskAccpetDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRectificationSaveDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * EicGatewayClient
 *
 * @author Jinhua
 * @date 2019/12/3 17:33
 */
@Component
public class FeEicGatewayClient {
    @Value("${iais.inter.gateway.url}")
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

    public <T> void callEicWithTrack(T obj, Consumer<T> consumer, String actionMethod) {
        requestTrackingHelper.callEicWithTrack(obj, consumer, this.getClass().getName(), actionMethod, currentApp, currentDomain,
                EicClientConstant.APPLICATION_CLIENT);
    }

    public FeignResponseEntity<GenerateUENDto> getUenInfo(String uen) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        Map<String, Object> params = IaisCommonUtils.genNewHashMap();
        params.put("uen", uen);
        return IaisEGPHelper.callEicGatewayWithParam(gateWayUrl + "/v1/svc-sync-acra/{uen}", HttpMethod.POST, params,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization(), GenerateUENDto.class);
    }

    public FeignResponseEntity<String> saveFile(ProcessFileTrackDto processFileTrackDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/file-sync-trackings", HttpMethod.POST, processFileTrackDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                String.class);
    }

    public FeignResponseEntity<List> getProfessionalDetail(ProfessionalParameterDto professionalParameterDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/prs-server/prs-api/getProfessionalDetail", HttpMethod.POST, professionalParameterDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                ProfessionalResponseDto.class);
    }

    /**
    * @author: yichen
    * @description: route to BE db
    * @param:
    * @return:
    */
    public FeignResponseEntity<String> saveFileApplication(ProcessFileTrackDto processFileTrackDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/app-file-sync-trackings", HttpMethod.POST, processFileTrackDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                String.class);
    }

    public FeignResponseEntity<AppPremisesRecommendationDto> getBeAppPremisesRecommendationByIdAndType(Map<String, Object> params) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithParam(gateWayUrl + "/v1/app-recom", HttpMethod.GET, params,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization(), AppPremisesRecommendationDto.class);
    }

    public FeignResponseEntity<IaisApiResult> routeSelfAssessment(FeSelfAssessmentSyncDataDto selfDeclSyncDataDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/self-decl-bridge", HttpMethod.POST, selfDeclSyncDataDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                IaisApiResult.class);
    }

    /**
     *@Author :weilu on 2020/1/15 12:35
     *@param :
     *@return :
     *@Description :
     */
    public FeignResponseEntity<List> routePaymentStatus(ApplicationGroupDto applicationGroupDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/payment-status", HttpMethod.PUT, applicationGroupDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                LicenceDto.class);
    }


    /**
     *@Author :weilu on 2020/3/12 17:03
     *@param :
     *@return :
     *@Description :update licence when effective date <= new Date
     */
    public FeignResponseEntity<List> updateLicenceStatus(List<LicenceDto> licenceDtos) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/cessation-status", HttpMethod.PUT, licenceDtos,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                LicenceDto.class);
    }

    public FeignResponseEntity<LicPremisesReqForInfoDto> routeRfiData(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/rfi-reply-bridge", HttpMethod.POST, licPremisesReqForInfoDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                LicPremisesReqForInfoDto.class);
    }

    public FeignResponseEntity<List> getUserCalendarByUserId(AppointmentDto appointmentDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/hcsa-reschedule-appt", HttpMethod.POST, appointmentDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                ApptRequestDto.class);
    }

    public FeignResponseEntity<List> getAppointmentByApptRefNo(List<String> apptRefNos) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/hcsa-appt-refno", HttpMethod.POST, apptRefNos,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                ApptRequestDto.class);
    }

    public FeignResponseEntity<ApptInspectionDateDto> apptFeDataUpdateCreateBe(ApptInspectionDateDto apptInspectionDateDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-app-insdate-up", HttpMethod.POST, apptInspectionDateDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                ApptInspectionDateDto.class);
    }

    public FeignResponseEntity<List> createFeReplyTask(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/hcsa-task-assignment", HttpMethod.POST, apptFeConfirmDateDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                TaskDto.class);
    }

    public FeignResponseEntity<ProcessReSchedulingDto> reSchFeSaveAllData(ProcessReSchedulingDto processReSchedulingDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-reschedule", HttpMethod.POST, processReSchedulingDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                ProcessReSchedulingDto.class);
    }

    public FeignResponseEntity<InspRectificationSaveDto> feCreateAndUpdateItemDoc(InspRectificationSaveDto inspRectificationSaveDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-inspect-nc-up", HttpMethod.POST, inspRectificationSaveDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                InspRectificationSaveDto.class);
    }

    public FeignResponseEntity<List> getpublicHoliday() {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/appt-public-holiday", HttpMethod.GET, null,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                PublicHolidayDto.class);
    }

    public FeignResponseEntity<Void> updateUserCalendarStatus(ApptCalendarStatusDto apptCalDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/appointment-status", HttpMethod.PUT, apptCalDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(), Void.class);
    }

    public FeignResponseEntity<String> getMessageNo(String date,
                                  String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/new-inbox-msg-no", HttpMethod.GET, null,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, String.class);
    }

    public FeignResponseEntity<GobalRiskAccpetDto>  getGobalRiskAccpetDtoCallBeByGobalRiskAccpetDto(GobalRiskAccpetDto gobalRiskAccpetDto,
                                                                     String date, String authorization, String dateSec,
                                                                     String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-config-gol-risk", HttpMethod.POST, gobalRiskAccpetDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, GobalRiskAccpetDto.class);
    }

    public FeignResponseEntity<SearchResult> eicSearchApptReschPrem(SearchParam rescheduleParam) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForSearchResult(gateWayUrl + "/v1/hcsa-query-appointment", HttpMethod.POST, rescheduleParam,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                ReschApptGrpPremsQueryDto.class);
    }

    public FeignResponseEntity<List> getServiceConfig(String serviceId,String applicationType) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        Map<String, Object> param = IaisCommonUtils.genNewHashMap(2);
        param.put("service", serviceId);
        param.put("type", applicationType);
        return IaisEGPHelper.callEicGatewayWithParamForList(gateWayUrl + "/v1/svc-routing-stage", HttpMethod.GET, param,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                HcsaSvcRoutingStageDto.class);
    }

    public FeignResponseEntity<String> saveAppGroupSysnEic(ApplicationGroupDto applicationGroupDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/app-group", HttpMethod.PUT, applicationGroupDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(), String.class);
    }

    public FeignResponseEntity<RecallApplicationDto> withdrawAppChangeTask(RecallApplicationDto recallApplicationDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/task-recall", HttpMethod.POST, recallApplicationDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                RecallApplicationDto.class);
    }


    public FeignResponseEntity<List> genInspApptRescheduleDate(AppointmentDto appointmentDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/appt-reschedule-action", HttpMethod.POST, appointmentDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                ApptRequestDto.class);
    }

    public FeignResponseEntity<Void> saveApplicationDtosForFe(ApplicationTruckDto applicationTruckDto,
                                                                          String date, String authorization, String dateSec,
                                                                          String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/app-withdrawn", HttpMethod.POST, applicationTruckDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, Void.class);
    }

    public FeignResponseEntity<PostCodeDto> getPostalCode(String postalCode) {
        Map<String, Object> params = IaisCommonUtils.genNewHashMap(1);
        params.put("postalCode", postalCode);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithParam(gateWayUrl + "/v1/postal-code", HttpMethod.GET, params, MediaType.APPLICATION_JSON,
                signature.date(), signature.authorization(), signature2.date(), signature2.authorization(), PostCodeDto.class);
    }

    public void sendEmail(EmailDto emailDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/no-attach-emails", HttpMethod.POST, emailDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization(), String.class);
    }

    public void sendSms(SmsDto smsDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/send-sms", HttpMethod.POST, smsDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization(), InterMessageDto.class);
    }

    public FeignResponseEntity<List> getRoutingHistoryDtos(Map<String, Object> params) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithParamForList(gateWayUrl + "/v1/app-routing-history", HttpMethod.GET, params,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization(), AppPremisesRoutingHistoryDto.class);
    }
}