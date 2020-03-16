package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicantConfirmInspDateService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionFeClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Shicheng
 * @date 2020/2/15 17:21
 **/
@Service
@Slf4j
public class ApplicantConfirmInspDateServiceImpl implements ApplicantConfirmInspDateService {

    @Autowired
    private InspectionFeClient inspectionFeClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    /**
     *System Date
     */
    @Override
    public ApptFeConfirmDateDto getApptSystemDate(String appPremCorrId) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        ApptFeConfirmDateDto apptFeConfirmDateDto = new ApptFeConfirmDateDto();
        //get All CorrDto From Same Premises
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationClient.getLastAppPremisesCorrelationDtoByCorreId(appPremCorrId).getEntity();
        //set All TaskRefNo (AppPremCorrIds)
        List<String> taskRefNo = getTaskRefNoList(appPremisesCorrelationDtos);
        apptFeConfirmDateDto.setTaskRefNo(taskRefNo);

        if(!StringUtil.isEmpty(appPremCorrId)) {
            List<ApplicationDto> applicationDtos = applicationClient.getPremisesApplicationsByCorreId(appPremCorrId).getEntity();
            apptFeConfirmDateDto.setApplicationDtos(applicationDtos);
            List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = inspectionFeClient.getSystemDtosByAppPremCorrIdList(taskRefNo).getEntity();
            if(!IaisCommonUtils.isEmpty(appPremisesInspecApptDtoList)){
                List<String> apptRefNos = new ArrayList<>();
                for(AppPremisesInspecApptDto aDto : appPremisesInspecApptDtoList){
                    apptRefNos.add(aDto.getApptRefNo());
                }
                Set<String> apptRefNoSet = new HashSet<>(apptRefNos);
                apptRefNos = new ArrayList<>(apptRefNoSet);
                Map<String, List<ApptUserCalendarDto>> apptInspDateMap = feEicGatewayClient.getAppointmentByApptRefNo(apptRefNos, signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization()).getEntity();
                if(apptInspDateMap != null) {
                    apptFeConfirmDateDto.setApptInspDateMap(apptInspDateMap);
                    setSystemDateMap(apptFeConfirmDateDto);
                }
            }
            apptFeConfirmDateDto.setAppPremisesInspecApptDtoList(appPremisesInspecApptDtoList);
            apptFeConfirmDateDto.setAppPremisesInspecApptDto(appPremisesInspecApptDtoList.get(0));
            apptFeConfirmDateDto.setAppPremCorrId(appPremCorrId);
        }
        return apptFeConfirmDateDto;
    }

    private List<String> getTaskRefNoList(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos) {
        List<String> taskRefNo = new ArrayList<>();
        if(!IaisCommonUtils.isEmpty(appPremisesCorrelationDtos)){
            for(AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos){
                taskRefNo.add(appPremisesCorrelationDto.getId());
            }
        }
        return taskRefNo;
    }

    private void setSystemDateMap(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        List<ApptUserCalendarDto> apptUserCalendarDtos = new ArrayList<>();
        for(Map.Entry<String, List<ApptUserCalendarDto>> apptInspDateMap : apptFeConfirmDateDto.getApptInspDateMap().entrySet()){
            List<ApptUserCalendarDto> apptUserCalendarDtoList = apptInspDateMap.getValue();
            if(!IaisCommonUtils.isEmpty(apptUserCalendarDtoList)){
                apptUserCalendarDtos.add(apptUserCalendarDtoList.get(0));
            }
        }
        if(!IaisCommonUtils.isEmpty(apptUserCalendarDtos)) {
            Map<String, Date> inspectionDateMap = new HashMap<>();
            List<SelectOption> inspectionDate = new ArrayList<>();
            for (AppPremisesInspecApptDto appPremisesInspecApptDto : apptFeConfirmDateDto.getAppPremisesInspecApptDtoList()) {
                for(int i = 0; i < apptUserCalendarDtos.size(); i++){
                    if(appPremisesInspecApptDto.getApptRefNo().equals(apptUserCalendarDtos.get(i).getApptRefNo())){
                        Date inspDate = apptUserCalendarDtos.get(i).getTimeSlot().get(0);
                        String dateStr = apptDateToStringShow(inspDate);
                        inspectionDateMap.put(appPremisesInspecApptDto.getId(), inspDate);
                        SelectOption so = new SelectOption(appPremisesInspecApptDto.getId(), dateStr);
                        inspectionDate.add(so);
                        apptUserCalendarDtos.remove(i);
                        i--;
                    }
                }
            }
            apptFeConfirmDateDto.setInspectionDate(inspectionDate);
            apptFeConfirmDateDto.setInspectionDateMap(inspectionDateMap);
        }
    }

    @Override
    public void confirmInspectionDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
        String checkDate = apptFeConfirmDateDto.getCheckDate();
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = new ArrayList<>();
        for(AppPremisesInspecApptDto apptDto : apptFeConfirmDateDto.getAppPremisesInspecApptDtoList()){
            if(!(apptDto.getId().equals(checkDate))){
                appPremisesInspecApptDtoList.add(apptDto);
            }
        }
        apptFeConfirmDateDto.setAppPremisesInspecApptDtoList(appPremisesInspecApptDtoList);
        setApptUpdateList(apptFeConfirmDateDto, apptInspectionDateDto);
        setUpdateApplicationDto(apptFeConfirmDateDto, apptInspectionDateDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
        setCreateHistoryDto(apptFeConfirmDateDto, apptInspectionDateDto);
        setCreateInspectionStatus(apptInspectionDateDto, InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
        setRecommendationDto(apptFeConfirmDateDto, apptInspectionDateDto);

        feEicGatewayClient.apptFeDataUpdateCreateBe(apptInspectionDateDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        createApptDateTask(apptFeConfirmDateDto, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION);
    }

    private void setRecommendationDto(ApptFeConfirmDateDto apptFeConfirmDateDto, ApptInspectionDateDto apptInspectionDateDto) {
        List<AppPremisesRecommendationDto> appPremisesRecommendationDtos = new ArrayList<>();
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        appPremisesRecommendationDto.setRecomDecision(InspectionConstants.PROCESS_DECI_ALLOW_SYSTEM_TO_PROPOSE_DATE);
        appPremisesRecommendationDto.setRecomInDate(apptFeConfirmDateDto.getSaveDate());
        appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
        appPremisesRecommendationDtos.add(appPremisesRecommendationDto);
        apptInspectionDateDto.setAppPremisesRecommendationDtos(appPremisesRecommendationDtos);
    }

    @Override
    public ApptFeConfirmDateDto getApptNewSystemDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        List<String> systemCorrId = new ArrayList<>();
//        for(ApptUserCalendarAndUserIdDto aucauDto : apptFeConfirmDateDto.getApptUserCalendarAndUserIdDtos()){
//            for(ApptUserCalendarDto apptUserCalendarDto : aucauDto.getAppointmentDtoList()){
//                //systemCorrId.add(apptUserCalendarDto.getUserSysCorreId());
//            }
//        }
        AppointmentDto appointmentDto = new AppointmentDto();
        AppPremisesInspecApptDto appPremisesInspecApptDto = apptFeConfirmDateDto.getAppPremisesInspecApptDto();
        appointmentDto.setStartDate(Formatter.formatDateTime(appPremisesInspecApptDto.getStartDate(), AppConsts.DEFAULT_DATE_TIME_FORMAT));
        appointmentDto.setEndDate(Formatter.formatDateTime(appPremisesInspecApptDto.getEndDate(), AppConsts.DEFAULT_DATE_TIME_FORMAT));
        List<List<ApptUserCalendarDto>> apptUserCalendarDtoLists = feEicGatewayClient.getUserCalendarByUserId(appointmentDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
        Map<String, Date> inspectionNewDateMap = new HashMap<>();
        List<SelectOption> inspectionNewDate = new ArrayList<>();
        Map<String, String> refMap = new HashMap<>();
        if(!IaisCommonUtils.isEmpty(apptUserCalendarDtoLists)) {
            for (int i = 0; i < apptUserCalendarDtoLists.size(); i++) {
                /*ApptUserCalendarDto apptUserCalendarDto = apptUserCalendarDtoLists.get(i).get(0);
                Date inspDate = apptUserCalendarDto.getTimeSlot();
                String dateStr = apptDateToStringShow(inspDate);
                inspectionNewDateMap.put(i + "", inspDate);
                refMap.put(i + "", apptUserCalendarDto.getApptRefNo());
                SelectOption so = new SelectOption(i + "", dateStr);
                inspectionNewDate.add(so);*/
            }
            apptFeConfirmDateDto.setInspectionNewDate(inspectionNewDate);
            apptFeConfirmDateDto.setInspectionNewDateMap(inspectionNewDateMap);
            apptFeConfirmDateDto.setRefNoMap(refMap);
        }
        return apptFeConfirmDateDto;
    }

    @Override
    public ApptFeConfirmDateDto confirmNewDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
        Map<String, Date> newDateMap = apptFeConfirmDateDto.getInspectionNewDateMap();
        String key = apptFeConfirmDateDto.getCheckNewDate();
        Date checkDate = newDateMap.get(key);
        Map<String, String> refNoMap = apptFeConfirmDateDto.getRefNoMap();
        String refNo = refNoMap.get(key);
        apptFeConfirmDateDto.setSaveDate(checkDate);
        apptFeConfirmDateDto.setApptRefNo(refNo);

        setApptUpdateList(apptFeConfirmDateDto, apptInspectionDateDto);
        setApptCreateList(apptFeConfirmDateDto, apptInspectionDateDto, null);
        setUpdateApplicationDto(apptFeConfirmDateDto, apptInspectionDateDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
        setCreateHistoryDto(apptFeConfirmDateDto, apptInspectionDateDto);
        setCreateInspectionStatus(apptInspectionDateDto, InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
        setRecommendationDto(apptFeConfirmDateDto, apptInspectionDateDto);

        feEicGatewayClient.apptFeDataUpdateCreateBe(apptInspectionDateDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        createApptDateTask(apptFeConfirmDateDto, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION);
        return apptFeConfirmDateDto;
    }

    @Override
    public void saveAccSpecificDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        setUpdateApplicationDto(apptFeConfirmDateDto, apptInspectionDateDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
        setCreateHistoryDto(apptFeConfirmDateDto, apptInspectionDateDto);
        setCreateInspectionStatus(apptInspectionDateDto, InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
        setRecommendationDto(apptFeConfirmDateDto, apptInspectionDateDto);

        feEicGatewayClient.apptFeDataUpdateCreateBe(apptInspectionDateDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        createApptDateTask(apptFeConfirmDateDto, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION);
    }

    private void setCreateInspectionStatus(ApptInspectionDateDto apptInspectionDateDto, String status) {
        List<AppInspectionStatusDto> appInspectionStatusDtos = new ArrayList<>();
        AppInspectionStatusDto appInspectionStatusDto = new AppInspectionStatusDto();
        appInspectionStatusDto.setStatus(status);
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appInspectionStatusDtos.add(appInspectionStatusDto);
        apptInspectionDateDto.setAppInspectionStatusDtos(appInspectionStatusDtos);
    }

    private void setCreateHistoryDto(ApptFeConfirmDateDto apptFeConfirmDateDto, ApptInspectionDateDto apptInspectionDateDto) {
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = new ArrayList<>();
        for(ApplicationDto applicationDto : apptFeConfirmDateDto.getApplicationDtos()) {
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
            appPremisesRoutingHistoryDto.setApplicationNo(applicationDto.getApplicationNo());
            appPremisesRoutingHistoryDto.setSubStage(HcsaConsts.ROUTING_STAGE_PRE);
            appPremisesRoutingHistoryDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
            appPremisesRoutingHistoryDto.setAppStatus(applicationDto.getStatus());
            appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
            appPremisesRoutingHistoryDtos.add(appPremisesRoutingHistoryDto);
        }
        apptInspectionDateDto.setAppPremisesRoutingHistoryDtos(appPremisesRoutingHistoryDtos);
    }

    private void setUpdateApplicationDto(ApptFeConfirmDateDto apptFeConfirmDateDto, ApptInspectionDateDto apptInspectionDateDto, String status) {
        List<ApplicationDto> applicationDtos = apptFeConfirmDateDto.getApplicationDtos();
        for(ApplicationDto applicationDto : applicationDtos) {
            applicationDto.setStatus(status);
            applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            applicationDto = applicationClient.updateApplication(applicationDto).getEntity();
            applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        apptInspectionDateDto.setApplicationDtos(applicationDtos);
    }

    private void setApptCreateList(ApptFeConfirmDateDto apptFeConfirmDateDto, ApptInspectionDateDto apptInspectionDateDto, String processDo) {
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = new ArrayList<>();
        AppPremisesInspecApptDto appPremisesInspecApptDto = new AppPremisesInspecApptDto();
        appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesInspecApptDto.setId(null);
        if(InspectionConstants.SWITCH_ACTION_REJECT.equals(processDo)) {
            appPremisesInspecApptDto.setSpecificInspDate(apptFeConfirmDateDto.getSaveDate());
            appPremisesInspecApptDto.setApptRefNo(null);
        } else if(StringUtil.isEmpty(processDo)) {
            appPremisesInspecApptDto.setSpecificInspDate(null);
            appPremisesInspecApptDto.setApptRefNo(apptFeConfirmDateDto.getApptRefNo());
        }
        appPremisesInspecApptDto.setAppCorrId(apptFeConfirmDateDto.getAppPremCorrId());
        appPremisesInspecApptDto.setStartDate(apptFeConfirmDateDto.getAppPremisesInspecApptDtoList().get(0).getStartDate());
        appPremisesInspecApptDto.setEndDate(apptFeConfirmDateDto.getAppPremisesInspecApptDtoList().get(0).getEndDate());
        appPremisesInspecApptDto.setReason(apptFeConfirmDateDto.getReason());
        appPremisesInspecApptDtoList.add(appPremisesInspecApptDto);
        appPremisesInspecApptDtoList = inspectionFeClient.createAppPremisesInspecApptDto(appPremisesInspecApptDtoList).getEntity();

        for(AppPremisesInspecApptDto apptDto : appPremisesInspecApptDtoList){
            apptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        apptInspectionDateDto.setAppPremisesInspecApptCreateList(appPremisesInspecApptDtoList);
    }

    private void setApptUpdateList(ApptFeConfirmDateDto apptFeConfirmDateDto, ApptInspectionDateDto apptInspectionDateDto) {
        for(AppPremisesInspecApptDto apptDto : apptFeConfirmDateDto.getAppPremisesInspecApptDtoList()){
            apptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            apptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = inspectionFeClient.updateAppPremisesInspecApptDtoList(apptFeConfirmDateDto.getAppPremisesInspecApptDtoList()).getEntity();
        if(!IaisCommonUtils.isEmpty(appPremisesInspecApptDtoList)){
            for(AppPremisesInspecApptDto appPremisesInspecApptDto : appPremisesInspecApptDtoList){
                appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            }
            apptInspectionDateDto.setAppPremisesInspecApptUpdateList(appPremisesInspecApptDtoList);
        }
    }

    private void createApptDateTask(ApptFeConfirmDateDto apptFeConfirmDateDto, String processUrl) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        TaskDto taskDto = new TaskDto();
        taskDto.setRefNo(apptFeConfirmDateDto.getAppPremCorrId());
        taskDto.setProcessUrl(processUrl);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        feEicGatewayClient.createFeReplyTask(taskDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }

    @Override
    public List<SelectOption> getInspectionDateHours() {
        List<SelectOption> hourOption = new ArrayList<>();
        for(int i = 1; i < 13; i++){
            SelectOption so = new SelectOption(i + "", i + "");
            hourOption.add(so);
        }
        return hourOption;
    }

    @Override
    public List<SelectOption> getAmPmOption() {
        List<SelectOption> amPmOption = new ArrayList<>();
        SelectOption so1 = new SelectOption(Formatter.DAY_AM, "am");
        SelectOption so2 = new SelectOption(Formatter.DAY_PM, "pm");
        amPmOption.add(so1);
        amPmOption.add(so2);
        return amPmOption;
    }

    @Override
    public void rejectSystemDateAndCreateTask(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        setApptUpdateList(apptFeConfirmDateDto, apptInspectionDateDto);
        setApptCreateList(apptFeConfirmDateDto, apptInspectionDateDto, InspectionConstants.SWITCH_ACTION_REJECT);
        setUpdateApplicationDto(apptFeConfirmDateDto, apptInspectionDateDto, ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING);
        setCreateHistoryDto(apptFeConfirmDateDto, apptInspectionDateDto);
        setCreateInspectionStatus(apptInspectionDateDto, InspectionConstants.INSPECTION_STATUS_PENDING_RE_APPOINTMENT_INSPECTION_DATE);
        apptInspectionDateDto.setAppPremisesRecommendationDtos(null);

        feEicGatewayClient.apptFeDataUpdateCreateBe(apptInspectionDateDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        createApptDateTask(apptFeConfirmDateDto, TaskConsts.TASK_PROCESS_URL_RE_CONFIRM_INSPECTION_DATE);
    }

    /**
     * Specific Date
     */
    @Override
    public ApptFeConfirmDateDto getSpecificDateDto(String appPremCorrId) {
        ApptFeConfirmDateDto apptFeConfirmDateDto = new ApptFeConfirmDateDto();
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = new ArrayList<>();
        if(!StringUtil.isEmpty(appPremCorrId)){
            AppPremisesInspecApptDto appPremisesInspecApptDto = inspectionFeClient.getSpecificDtoByAppPremCorrId(appPremCorrId).getEntity();
            appPremisesInspecApptDtoList.add(appPremisesInspecApptDto);

            apptFeConfirmDateDto.setAppPremisesInspecApptDto(appPremisesInspecApptDto);
            apptFeConfirmDateDto.setAppPremisesInspecApptDtoList(appPremisesInspecApptDtoList);
            ApplicationDto applicationDto = applicationClient.getApplicationByCorreId(appPremCorrId).getEntity();
            LicenceDto licenceDto = licenceClient.getLicBylicId(applicationDto.getLicenceId()).getEntity();
            if(appPremisesInspecApptDto != null){
                apptFeConfirmDateDto.setSpecificDate(appPremisesInspecApptDto.getSpecificInspDate());
                apptFeConfirmDateDto.setApptStartDate(appPremisesInspecApptDto.getStartDate());
                apptFeConfirmDateDto.setApptEndDate(appPremisesInspecApptDto.getEndDate());
                apptFeConfirmDateDto.setAppPremCorrId(appPremCorrId);
                apptFeConfirmDateDto.setApptRefNo(appPremisesInspecApptDto.getApptRefNo());
                apptFeConfirmDateDto.setApplicationNo(applicationDto.getApplicationNo());
                apptFeConfirmDateDto.setApplicationType(applicationDto.getApplicationType());

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String licenceStartDate = format.format(licenceDto.getStartDate());
                String licenceEndDate = format.format(licenceDto.getEndDate());
                String licensePeriod = licenceStartDate + " - " + licenceEndDate;
                apptFeConfirmDateDto.setLicencePeriod(licensePeriod);
                String specificDate = apptDateToStringShow(appPremisesInspecApptDto.getSpecificInspDate());
                apptFeConfirmDateDto.setSpecificDateShow(specificDate);
            }
        }
        return apptFeConfirmDateDto;
    }

    @Override
    public void rejectSpecificDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = new ArrayList<>();
        AppPremisesInspecApptDto appPremisesInspecApptDto = apptFeConfirmDateDto.getAppPremisesInspecApptDto();
        appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
        appPremisesInspecApptDto = inspectionFeClient.updateAppPremisesInspecApptDto(appPremisesInspecApptDto).getEntity();
        appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesInspecApptDtoList.add(appPremisesInspecApptDto);

        apptInspectionDateDto.setAppPremisesInspecApptUpdateList(appPremisesInspecApptDtoList);
        setUpdateApplicationDto(apptFeConfirmDateDto, apptInspectionDateDto, ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING);
        setCreateHistoryDto(apptFeConfirmDateDto, apptInspectionDateDto);
        setCreateInspectionStatus(apptInspectionDateDto, InspectionConstants.INSPECTION_STATUS_PENDING_RE_APPOINTMENT_INSPECTION_DATE);
        apptInspectionDateDto.setAppPremisesRecommendationDtos(null);

        feEicGatewayClient.apptFeDataUpdateCreateBe(apptInspectionDateDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        createApptDateTask(apptFeConfirmDateDto, TaskConsts.TASK_PROCESS_URL_RE_CONFIRM_INSPECTION_DATE);
    }

    private String apptDateToStringShow(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("d MMM");
        String specificDate = format.format(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int curHour24 = cal.get(Calendar.HOUR_OF_DAY);
        if(curHour24 > 12){
            int hours = curHour24 - 12;
            specificDate = specificDate + " " + hours + ":00" + "PM";
        } else {
            specificDate = specificDate + " " + curHour24 + ":00" + "AM";
        }
        return specificDate;
    }
}
