package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptCalendarStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ProcessReSchedulingDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.ApptConfirmReSchDateService;
import com.ecquaria.cloud.moh.iais.service.client.AppEicClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Shicheng
 * @date 2020/6/18 14:38
 **/
@Service
@Slf4j
public class ApptConfirmReSchDateServiceImpl implements ApptConfirmReSchDateService {

    @Autowired
    private InspectionFeClient inspectionFeClient;

    @Autowired
    private ApplicationFeClient applicationFeClient;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private AppEicClient appEicClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Value("${iais.email.sender}")
    private String mailSender;

    @Autowired
    private AppSubmissionService appSubmissionService;

    @Override
    public String getAppPremCorrIdByAppId(String appId) {
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationFeClient.listAppPremisesCorrelation(appId).getEntity().get(0);
        String appPremCorrId = appPremisesCorrelationDto.getId();
        return appPremCorrId;
    }

    @Override
    public ApplicationDto getApplicationDtoByAppNo(String applicationNo) {
        ApplicationDto applicationDto = applicationFeClient.getApplicationDtoByAppNo(applicationNo).getEntity();
        return applicationDto;
    }

    @Override
    public ProcessReSchedulingDto getApptSystemDateByCorrId(String appPremCorrId, String appStatus) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        ProcessReSchedulingDto processReSchedulingDto = new ProcessReSchedulingDto();
        if(!StringUtil.isEmpty(appPremCorrId)) {
            //get All CorrDto From Same Premises
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationFeClient.getLastAppPremisesCorrelationDtoByCorreId(appPremCorrId).getEntity();
            //set All TaskRefNo (AppPremCorrIds)
            List<ApplicationDto> applicationDtos = getApplicationBySamePremCorrId(appPremisesCorrelationDtos, processReSchedulingDto, appStatus);
            //set appNo and appPremCorrId map
            processReSchedulingDto = setAppNoAndAppPremCorrIdMap(applicationDtos, processReSchedulingDto);
            processReSchedulingDto.setApplicationDtos(applicationDtos);
            List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = inspectionFeClient.getSystemDtosByAppPremCorrIdList(processReSchedulingDto.getTaskRefNo()).getEntity();
            processReSchedulingDto.setAppPremisesInspecApptDtoList(appPremisesInspecApptDtoList);
            if(!IaisCommonUtils.isEmpty(appPremisesInspecApptDtoList)){
                List<String> apptRefNos = IaisCommonUtils.genNewArrayList();
                for(AppPremisesInspecApptDto aDto : appPremisesInspecApptDtoList){
                    apptRefNos.add(aDto.getApptRefNo());
                }
                Set<String> apptRefNoSet = new HashSet<>(apptRefNos);
                apptRefNos = new ArrayList<>(apptRefNoSet);
                //save eic record
                ApptAppInfoShowDto apptAppInfoShowDto = new ApptAppInfoShowDto();
                apptAppInfoShowDto.setApptRefNo(apptRefNos);
                EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApplicantConfirmInspDateServiceImpl", "getApptSystemDate",
                        "hcsa-licence-web-internet", ApptAppInfoShowDto.class.getName(), JsonUtil.parseToJson(apptAppInfoShowDto));
                String eicRefNo = eicRequestTrackingDto.getRefNo();
                List<ApptRequestDto> apptRequestDtos = feEicGatewayClient.getAppointmentByApptRefNo(apptRefNos, signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization()).getEntity();
                Map<String, List<ApptUserCalendarDto>> apptInspDateMap = new LinkedHashMap<>(apptRequestDtos.size());
                if(!IaisCommonUtils.isEmpty(apptRequestDtos)){
                    for(ApptRequestDto apptRequestDto : apptRequestDtos){
                        apptInspDateMap.put(apptRequestDto.getApptRefNo(), apptRequestDto.getUserClandars());
                    }
                }
                //get eic record
                eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
                //update eic record status
                eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
                eicRequestTrackingDtos.add(eicRequestTrackingDto);
                appEicClient.updateStatus(eicRequestTrackingDtos);

                if(apptInspDateMap != null) {
                    processReSchedulingDto.setApptInspDateMap(apptInspDateMap);
                    setSystemDateMap(processReSchedulingDto);
                }
            }

            processReSchedulingDto.setAppPremCorrId(appPremCorrId);
        }
        return processReSchedulingDto;
    }

    private List<ApplicationDto> getApplicationBySamePremCorrId(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos, ProcessReSchedulingDto processReSchedulingDto, String appStatus) {
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        List<String> taskRefNo = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appPremisesCorrelationDtos)){//NOSONAR
            for(AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos){
                if(appPremisesCorrelationDto != null && !StringUtil.isEmpty(appPremisesCorrelationDto.getId())){
                    ApplicationDto applicationDto = applicationFeClient.getApplicationByCorreId(appPremisesCorrelationDto.getId()).getEntity();
                    if(!StringUtil.isEmpty(appStatus) && applicationDto != null) {
                        if (appStatus.equals(applicationDto.getStatus())) {
                            taskRefNo.add(appPremisesCorrelationDto.getId());
                            applicationDtos.add(applicationDto);
                        }
                    }
                }
            }
        }
        if(!IaisCommonUtils.isEmpty(taskRefNo)){
            processReSchedulingDto.setTaskRefNo(taskRefNo);
        }
        return applicationDtos;
    }

    private ProcessReSchedulingDto setAppNoAndAppPremCorrIdMap(List<ApplicationDto> applicationDtos, ProcessReSchedulingDto processReSchedulingDto) {
        if(!IaisCommonUtils.isEmpty(applicationDtos)){
            Map<String, String> appNoCorrIdMap = IaisCommonUtils.genNewHashMap();
            for(ApplicationDto applicationDto : applicationDtos){
                String appId = applicationDto.getId();
                String appNo = applicationDto.getApplicationNo();
                String appPremCorrId = getAppPremCorrIdByAppId(appId);
                appNoCorrIdMap.put(appNo, appPremCorrId);
            }
            processReSchedulingDto.setAppNoCorrIdMap(appNoCorrIdMap);
        }
        return processReSchedulingDto;
    }

    @Override
    public void acceptReschedulingDate(ProcessReSchedulingDto processReSchedulingDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        //get selected date(apptRefNo)
        String checkDate = processReSchedulingDto.getCheckDate();
        //Exclude a selected date
        List<String> cancelRefNo = IaisCommonUtils.genNewArrayList();
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = IaisCommonUtils.genNewArrayList();
        for(AppPremisesInspecApptDto apptDto : processReSchedulingDto.getAppPremisesInspecApptDtoList()){
            if(!(apptDto.getApptRefNo().equals(checkDate))){
                appPremisesInspecApptDtoList.add(apptDto);
                cancelRefNo.add(apptDto.getApptRefNo());
            }
        }
        //filter
        Set<String> cancelRefNoSet = new HashSet<>(cancelRefNo);
        cancelRefNo = new ArrayList<>(cancelRefNoSet);
        processReSchedulingDto.setAppPremisesInspecApptDtoList(appPremisesInspecApptDtoList);
        //update appt
        setApptUpdateList(processReSchedulingDto);
        //update application
        setUpdateApplicationDto(processReSchedulingDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
        //set history
        setCreateHistoryDto(processReSchedulingDto);
        //set some data to update inspection status
        setCreateInspectionStatus(processReSchedulingDto, InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
        //set some data to update recommendation
        setRecommendationDto(processReSchedulingDto);
        //create task
        createApptDateTask(processReSchedulingDto, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION);
        //save eic record
        EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApplicantConfirmInspDateServiceImpl", "acceptReschedulingDate",
                "hcsa-licence-web-internet", ProcessReSchedulingDto.class.getName(), JsonUtil.parseToJson(processReSchedulingDto));
        String eicRefNo = eicRequestTrackingDto.getRefNo();
        feEicGatewayClient.reSchFeSaveAllData(processReSchedulingDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        //get eic record
        eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
        //update eic record status
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
        eicRequestTrackingDtos.add(eicRequestTrackingDto);
        appEicClient.updateStatus(eicRequestTrackingDtos);
        //cancel or confirm appointment date
        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        apptCalendarStatusDto.setCancelRefNums(cancelRefNo);
        ccCalendarStatusEic(apptCalendarStatusDto);
    }

    @Override
    public void rejectReschedulingDate(ProcessReSchedulingDto processReSchedulingDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

        ApplicationDto applicationDto = processReSchedulingDto.getApplicationDto();
        ApplicationGroupDto applicationGroupDto = applicationFeClient.getApplicationGroup(applicationDto.getAppGrpId()).getEntity();
        String appStatus = applicationDto.getStatus();
        if(ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_APPLICANT.equals(appStatus)){
            try{
                EmailDto emailDto = new EmailDto();
                emailDto.setContent("Please contact the respective MOH officer to reschedule your appointment.");
                emailDto.setSubject("MOH IAIS - Applicant Rescheduling Rejected");
                emailDto.setSender(mailSender);
                emailDto.setClientQueryCode(applicationDto.getId());
                emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(applicationGroupDto.getLicenseeId()));
                //appSubmissionService.feSendEmail(emailDto);
            } catch (Exception e){
                log.info(e.getMessage(),e);
            }
        } else if (ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_PENDING_FE.equals(appStatus)) {
            EmailDto emailDto = new EmailDto();
            emailDto.setContent("Please contact the respective MOH officer to reschedule your appointment.");
            emailDto.setSubject("MOH IAIS - Applicant Rescheduling Rejected For Moh Officer(s)");
            emailDto.setSender(mailSender);
            emailDto.setClientQueryCode(applicationDto.getId());
            processReSchedulingDto.setEmailDto(emailDto);
        }
        //Exclude a selected date
        List<String> cancelRefNo = IaisCommonUtils.genNewArrayList();
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = IaisCommonUtils.genNewArrayList();
        for(AppPremisesInspecApptDto apptDto : processReSchedulingDto.getAppPremisesInspecApptDtoList()){
            appPremisesInspecApptDtoList.add(apptDto);
            cancelRefNo.add(apptDto.getApptRefNo());
        }
        //filter
        Set<String> cancelRefNoSet = new HashSet<>(cancelRefNo);
        cancelRefNo = new ArrayList<>(cancelRefNoSet);
        processReSchedulingDto.setAppPremisesInspecApptDtoList(appPremisesInspecApptDtoList);
        //update appt
        setApptUpdateList(processReSchedulingDto);
        //update application
        setUpdateApplicationDto(processReSchedulingDto, ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING);
        //set history
        setCreateHistoryDto(processReSchedulingDto);
        //set some data to update inspection status
        setCreateInspectionStatus(processReSchedulingDto, InspectionConstants.INSPECTION_STATUS_PENDING_RE_APPOINTMENT_INSPECTION_DATE);
        //create task
        createApptDateTask(processReSchedulingDto, TaskConsts.TASK_PROCESS_URL_RE_CONFIRM_INSPECTION_DATE);
        //save eic record
        EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApplicantConfirmInspDateServiceImpl", "acceptReschedulingDate",
                "hcsa-licence-web-internet", ProcessReSchedulingDto.class.getName(), JsonUtil.parseToJson(processReSchedulingDto));
        String eicRefNo = eicRequestTrackingDto.getRefNo();
        feEicGatewayClient.reSchFeSaveAllData(processReSchedulingDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        //get eic record
        eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
        //update eic record status
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
        eicRequestTrackingDtos.add(eicRequestTrackingDto);
        appEicClient.updateStatus(eicRequestTrackingDtos);
        //cancel or confirm appointment date
        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        apptCalendarStatusDto.setCancelRefNums(cancelRefNo);
        ccCalendarStatusEic(apptCalendarStatusDto);
    }

    private void setRecommendationDto(ProcessReSchedulingDto processReSchedulingDto) {
        List<AppPremisesRecommendationDto> appPremisesRecommendationDtos = IaisCommonUtils.genNewArrayList();
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        appPremisesRecommendationDto.setRecomDecision(InspectionConstants.PROCESS_DECI_ALLOW_SYSTEM_TO_PROPOSE_DATE);
        appPremisesRecommendationDto.setRecomInDate(processReSchedulingDto.getSaveDate());
        appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
        appPremisesRecommendationDtos.add(appPremisesRecommendationDto);
        processReSchedulingDto.setAppPremisesRecommendationDtos(appPremisesRecommendationDtos);
    }

    private void setCreateInspectionStatus(ProcessReSchedulingDto processReSchedulingDto, String status) {
        List<AppInspectionStatusDto> appInspectionStatusDtos = IaisCommonUtils.genNewArrayList();
        AppInspectionStatusDto appInspectionStatusDto = new AppInspectionStatusDto();
        appInspectionStatusDto.setStatus(status);
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appInspectionStatusDtos.add(appInspectionStatusDto);
        processReSchedulingDto.setAppInspectionStatusDtos(appInspectionStatusDtos);
    }

    private void setCreateHistoryDto(ProcessReSchedulingDto processReSchedulingDto) {
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = IaisCommonUtils.genNewArrayList();
        for(ApplicationDto applicationDto : processReSchedulingDto.getApplicationDtos()) {
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
            appPremisesRoutingHistoryDto.setApplicationNo(applicationDto.getApplicationNo());
            appPremisesRoutingHistoryDto.setSubStage(HcsaConsts.ROUTING_STAGE_PRE);
            appPremisesRoutingHistoryDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
            appPremisesRoutingHistoryDto.setAppStatus(applicationDto.getStatus());
            appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
            appPremisesRoutingHistoryDtos.add(appPremisesRoutingHistoryDto);
        }
        processReSchedulingDto.setAppPremisesRoutingHistoryDtos(appPremisesRoutingHistoryDtos);
    }

    private void setUpdateApplicationDto(ProcessReSchedulingDto processReSchedulingDto, String status) {
        List<ApplicationDto> applicationDtos = processReSchedulingDto.getApplicationDtos();
        for(ApplicationDto applicationDto : applicationDtos) {
            applicationDto.setStatus(status);
            applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            applicationDto = applicationFeClient.updateApplication(applicationDto).getEntity();
            applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        processReSchedulingDto.setApplicationDtos(applicationDtos);
    }

    private void setApptUpdateList(ProcessReSchedulingDto processReSchedulingDto) {
        for(AppPremisesInspecApptDto apptDto : processReSchedulingDto.getAppPremisesInspecApptDtoList()){
            apptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            apptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = inspectionFeClient.updateAppPremisesInspecApptDtoList(processReSchedulingDto.getAppPremisesInspecApptDtoList()).getEntity();
        if(!IaisCommonUtils.isEmpty(appPremisesInspecApptDtoList)){
            for(AppPremisesInspecApptDto appPremisesInspecApptDto : appPremisesInspecApptDtoList){
                appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            }
            processReSchedulingDto.setAppPremisesInspecApptUpdateList(appPremisesInspecApptDtoList);
        }
    }

    private void setSystemDateMap(ProcessReSchedulingDto processReSchedulingDto) {
        Map<String, Date> inspectionDateMap = IaisCommonUtils.genNewHashMap();
        List<SelectOption> inspectionDate = IaisCommonUtils.genNewArrayList();

        for(Map.Entry<String, List<ApptUserCalendarDto>> apptInspDateMap : processReSchedulingDto.getApptInspDateMap().entrySet()){
            String apptRefNo = apptInspDateMap.getKey();
            List<ApptUserCalendarDto> apptUserCalendarDtoList = apptInspDateMap.getValue();
            int timeSize = apptUserCalendarDtoList.get(0).getEndSlot().size();
            Date startDate = apptUserCalendarDtoList.get(0).getStartSlot().get(0);
            Date endDate = apptUserCalendarDtoList.get(0).getEndSlot().get(timeSize - 1);
            String inspStartDate = apptDateToStringShow(startDate);
            String inspEndDate = apptDateToStringShow(endDate);
            String dateStr = inspStartDate + " - " + inspEndDate;
            inspectionDateMap.put(apptRefNo, startDate);
            SelectOption so = new SelectOption(apptRefNo, dateStr);
            inspectionDate.add(so);
        }
        processReSchedulingDto.setInspectionDate(inspectionDate);
        processReSchedulingDto.setInspectionDateMap(inspectionDateMap);
    }

    private List<String> getTaskRefNoList(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos) {
        List<String> taskRefNo = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appPremisesCorrelationDtos)){
            for(AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos){
                taskRefNo.add(appPremisesCorrelationDto.getId());
            }
        }
        return taskRefNo;
    }

    private void createApptDateTask(ProcessReSchedulingDto processReSchedulingDto, String processUrl) {
        String applicationNo = processReSchedulingDto.getApplicationDto().getApplicationNo();
        TaskDto taskDto = new TaskDto();
        taskDto.setRefNo(processReSchedulingDto.getAppPremCorrId());
        taskDto.setApplicationNo(applicationNo);
        taskDto.setProcessUrl(processUrl);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        processReSchedulingDto.setTaskDto(taskDto);
        //set list and map
        setAppNoListByCorrIds(processReSchedulingDto);
    }

    private void ccCalendarStatusEic(ApptCalendarStatusDto apptCalendarStatusDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        //save eic record
        EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApplicantConfirmInspDateServiceImpl", "ccCalendarStatusEic",
                "hcsa-licence-web-internet", ApptCalendarStatusDto.class.getName(), JsonUtil.parseToJson(apptCalendarStatusDto));
        String eicRefNo = eicRequestTrackingDto.getRefNo();
        feEicGatewayClient.updateUserCalendarStatus(apptCalendarStatusDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        //get eic record
        eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
        //update eic record status
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
        eicRequestTrackingDtos.add(eicRequestTrackingDto);
        appEicClient.updateStatus(eicRequestTrackingDtos);
    }

    private ProcessReSchedulingDto setAppNoListByCorrIds(ProcessReSchedulingDto processReSchedulingDto) {
        List<String> appNoList = IaisCommonUtils.genNewArrayList();
        Map<String, String> appNoCorrIdMap = IaisCommonUtils.genNewHashMap();
        List<String> refNoList = processReSchedulingDto.getTaskRefNo();
        if(!IaisCommonUtils.isEmpty(refNoList)){
            Set<String> refNoSet = new HashSet<>(refNoList);
            refNoList = new ArrayList<>(refNoSet);
            for(String refNo : refNoList){
                String appNo = getApplicationByCorrId(refNo);
                appNoList.add(appNo);
                appNoCorrIdMap.put(appNo, refNo);
            }
        }
        processReSchedulingDto.setAppNoList(appNoList);
        processReSchedulingDto.setAppNoCorrIdMap(appNoCorrIdMap);
        return processReSchedulingDto;
    }

    private String getApplicationByCorrId(String appPremCorrId) {
        String appNo = "";
        if(!StringUtil.isEmpty(appPremCorrId)){
            ApplicationDto applicationDto = applicationFeClient.getApplicationByCorreId(appPremCorrId).getEntity();
            appNo = applicationDto.getApplicationNo();
        }
        return appNo;
    }

    private String apptDateToStringShow(Date date) {
        String specificDate = Formatter.formatDateTime(date, "dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int minutes = cal.get(Calendar.MINUTE);
        if(minutes > 0){
            cal.add(Calendar.HOUR_OF_DAY, 1);
        }
        int curHour24 = cal.get(Calendar.HOUR_OF_DAY);
        String hoursShow = "";
        if(curHour24 < 10){
            hoursShow = "0";
        }
        specificDate = specificDate + " " + hoursShow + curHour24 + ":00";
        return specificDate;
    }

    @Override
    public void updateAppStatusCommPool(List<ApptViewDto> apptViewDtos) {
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos= applicationFeClient.appPremCorrDtosByApptViewDtos(apptViewDtos).getEntity();

        for (AppPremisesCorrelationDto appPremisesCorrelationDto:appPremisesCorrelationDtos
        ) {
            String appId=appPremisesCorrelationDto.getApplicationId();
            String appCorrId=appPremisesCorrelationDto.getId();
            ApplicationDto applicationDto= applicationFeClient.getApplicationById(appId).getEntity();
            ProcessReSchedulingDto processReSchedulingDto;
            Map<String,List<String>> map=IaisCommonUtils.genNewHashMap();
            for (ApptViewDto appt:apptViewDtos
            ) {
                if(appt.getAppCorrId().equals(appPremisesCorrelationDto.getId())){
                    map.put(applicationDto.getApplicationNo(),appt.getUserIds());
                }
            }

            processReSchedulingDto = getApptComPolSystemDateByCorrId(appCorrId,applicationDto);
            processReSchedulingDto.setAppNoUserIds(map);

            for (ApptViewDto appt:apptViewDtos
            ) {
                if(appt.getAppGrpId().equals(appPremisesCorrelationDto.getAppGrpPremId())){
                    for (AppPremisesInspecApptDto insAppt: processReSchedulingDto.getAppPremisesInspecApptDtoList()
                    ) {
                        insAppt.setReason(appt.getReason());
                        insAppt.setEndDate(appt.getSpecificEndDate());
                        insAppt.setStartDate(appt.getSpecificStartDate());
                        insAppt.setSpecificInspDate(appt.getInspNewDate());
                        insAppt.setReschedulingCount(insAppt.getReschedulingCount()+1);
                        processReSchedulingDto.setSaveDate(appt.getInspNewDate());

                    }
                }
            }
            processReSchedulingDto.setApplicationDto(applicationDto);
            processReSchedulingDto.setCreateFlag(AppConsts.COMMON_POOL);
            //eic update to be status and task
            comPolReschedulingDate(processReSchedulingDto);
        }
    }


    private ProcessReSchedulingDto getApptComPolSystemDateByCorrId(String appPremCorrId ,ApplicationDto applicationDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        ProcessReSchedulingDto processReSchedulingDto = new ProcessReSchedulingDto();
        //get All CorrDto From Same Premises
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = IaisCommonUtils.genNewArrayList();
        appPremisesCorrelationDtos.add(applicationFeClient.getCorrelationByAppNo(applicationDto.getApplicationNo()).getEntity());
        //set All TaskRefNo (AppPremCorrIds)
        List<String> taskRefNo = getTaskRefNoList(appPremisesCorrelationDtos);
        processReSchedulingDto.setTaskRefNo(taskRefNo);
        if(!StringUtil.isEmpty(appPremCorrId)) {
            List<ApplicationDto> applicationDtos =IaisCommonUtils.genNewArrayList();
            applicationDtos.add(applicationDto);
            //set appNo and appPremCorrId map
            processReSchedulingDto = setAppNoAndAppPremCorrIdMap(applicationDtos, processReSchedulingDto);
            processReSchedulingDto.setApplicationDtos(applicationDtos);
            List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = inspectionFeClient.getSystemDtosByAppPremCorrIdList(taskRefNo).getEntity();
            processReSchedulingDto.setAppPremisesInspecApptDtoList(appPremisesInspecApptDtoList);
            if(!IaisCommonUtils.isEmpty(appPremisesInspecApptDtoList)){
                List<String> apptRefNos = IaisCommonUtils.genNewArrayList();
                for(AppPremisesInspecApptDto aDto : appPremisesInspecApptDtoList){
                    apptRefNos.add(aDto.getApptRefNo());
                }
                Set<String> apptRefNoSet = new HashSet<>(apptRefNos);
                apptRefNos = new ArrayList<>(apptRefNoSet);
                //save eic record
                ApptAppInfoShowDto apptAppInfoShowDto = new ApptAppInfoShowDto();
                apptAppInfoShowDto.setApptRefNo(apptRefNos);
                EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApplicantConfirmInspDateServiceImpl", "getApptSystemDate",
                        "hcsa-licence-web-internet", ApptAppInfoShowDto.class.getName(), JsonUtil.parseToJson(apptAppInfoShowDto));
                String eicRefNo = eicRequestTrackingDto.getRefNo();
                List<ApptRequestDto> apptRequestDtos = feEicGatewayClient.getAppointmentByApptRefNo(apptRefNos, signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization()).getEntity();
                Map<String, List<ApptUserCalendarDto>> apptInspDateMap = new LinkedHashMap<>(apptRequestDtos.size());
                if(!IaisCommonUtils.isEmpty(apptRequestDtos)){
                    for(ApptRequestDto apptRequestDto : apptRequestDtos){
                        apptInspDateMap.put(apptRequestDto.getApptRefNo(), apptRequestDto.getUserClandars());
                    }
                }
                //get eic record
                eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
                //update eic record status
                eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
                eicRequestTrackingDtos.add(eicRequestTrackingDto);
                appEicClient.updateStatus(eicRequestTrackingDtos);

                if(apptInspDateMap != null) {
                    processReSchedulingDto.setApptInspDateMap(apptInspDateMap);
                    setSystemDateMap(processReSchedulingDto);
                }
            }

            processReSchedulingDto.setAppPremCorrId(appPremCorrId);
        }
        return processReSchedulingDto;
    }

    @Override
    public void comPolReschedulingDate(ProcessReSchedulingDto processReSchedulingDto) {
        ApplicationDto applicationDto = processReSchedulingDto.getApplicationDto();
        String appStatus = applicationDto.getStatus();

        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

        //update appt
        for(AppPremisesInspecApptDto apptDto : processReSchedulingDto.getAppPremisesInspecApptDtoList()){
//            apptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            apptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList1=processReSchedulingDto.getAppPremisesInspecApptDtoList();
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = inspectionFeClient.updateAppPremisesInspecApptDtoList(appPremisesInspecApptDtoList1).getEntity();
        if(!IaisCommonUtils.isEmpty(appPremisesInspecApptDtoList)){
            for(AppPremisesInspecApptDto appPremisesInspecApptDto : appPremisesInspecApptDtoList){
                appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            }
            processReSchedulingDto.setAppPremisesInspecApptUpdateList(appPremisesInspecApptDtoList);
        }
        //Exclude a selected date
        List<String> cancelRefNo = IaisCommonUtils.genNewArrayList();
        for(AppPremisesInspecApptDto apptDto : processReSchedulingDto.getAppPremisesInspecApptDtoList()){
            cancelRefNo.add(apptDto.getApptRefNo());
        }
        //filter
        Set<String> cancelRefNoSet = new HashSet<>(cancelRefNo);
        cancelRefNo = new ArrayList<>(cancelRefNoSet);
        //update application
        //setUpdateApplicationDto(processReSchedulingDto,ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL);
        //set history
        //setCreateHistoryDto(processReSchedulingDto);
        //set some data to update recommendation
        setRecommendationDto(processReSchedulingDto);
        //set some data to update inspection status
        setCreateInspectionStatus(processReSchedulingDto, InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
        //create task
        createApptDateTask(processReSchedulingDto, TaskConsts.TASK_PROCESS_URL_COMMON_POOL);
        //save eic record
        EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApplicantConfirmInspDateServiceImpl", "acceptReschedulingDate",
                "hcsa-licence-web-internet", ProcessReSchedulingDto.class.getName(), JsonUtil.parseToJson(processReSchedulingDto));
        String eicRefNo = eicRequestTrackingDto.getRefNo();
        feEicGatewayClient.reSchFeSaveAllData(processReSchedulingDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        //get eic record
        eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
        //update eic record status
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
        eicRequestTrackingDtos.add(eicRequestTrackingDto);
        appEicClient.updateStatus(eicRequestTrackingDtos);
        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        apptCalendarStatusDto.setCancelRefNums(cancelRefNo);
        ccCalendarStatusEic(apptCalendarStatusDto);

    }
}
