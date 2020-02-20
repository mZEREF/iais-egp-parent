package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.ApptInspectionDateService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2020/2/11 11:32
 **/
@Service
@Slf4j
public class ApptInspectionDateServiceImpl implements ApptInspectionDateService {

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private InspEmailService inspEmailService;

    @Autowired
    private EmailClient emailClient;

    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;

    @Autowired
    private AppointmentClient appointmentClient;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private InboxMsgService inboxMsgService;

    @Autowired
    private InspectionAssignTaskService inspectionAssignTaskService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    public ApptInspectionDateDto getInspectionDate(String taskId, ApptInspectionDateDto apptInspectionDateDto) {
        TaskDto taskDto = taskService.getTaskById(taskId);
        AppointmentDto appointmentDto = inspectionTaskClient.getApptStartEndDateByAppCorrId(taskDto.getRefNo()).getEntity();
        List<TaskDto> taskDtoList = organizationClient.getCurrTaskByRefNo(taskDto.getRefNo()).getEntity();
        List<String> systemCorrIds = new ArrayList<>();
        if(!IaisCommonUtils.isEmpty(taskDtoList)){
            for(TaskDto tDto : taskDtoList){
                systemCorrIds = getSystemCorrIdByUserId(tDto.getUserId(), systemCorrIds);
            }
        }
        appointmentDto.setUserId(systemCorrIds);
        if(appointmentDto.getStartDate() == null && appointmentDto.getEndDate() == null){
            appointmentDto = hcsaConfigClient.getApptStartEndDateByService(appointmentDto).getEntity();
        }
        List<List<ApptUserCalendarDto>> apptUserCalendarDtoList = appointmentClient.getUserCalendarByUserId(appointmentDto).getEntity();
        apptInspectionDateDto = getShowTimeStringList(apptUserCalendarDtoList, apptInspectionDateDto);
        apptInspectionDateDto.setTaskDto(taskDto);
        apptInspectionDateDto.setTaskDtos(taskDtoList);
        return apptInspectionDateDto;
    }

    private ApptInspectionDateDto getShowTimeStringList(List<List<ApptUserCalendarDto>> apptUserCalendarDtoList, ApptInspectionDateDto apptInspectionDateDto) {
        List<String> inspectionDates = new ArrayList<>();
        if(!IaisCommonUtils.isEmpty(apptUserCalendarDtoList)){
            List<ApptUserCalendarDto> apptUserCalendarDtoListAll = new ArrayList<>();
            for(List<ApptUserCalendarDto> apptUserCalendarDtos : apptUserCalendarDtoList){
                apptUserCalendarDtoListAll.add(apptUserCalendarDtos.get(0));
                if(!IaisCommonUtils.isEmpty(apptUserCalendarDtos)){

                    String fullDate = getApptDateToShow(apptUserCalendarDtos.get(0).getTimeSlot());
                    inspectionDates.add(fullDate);
                }
            }
            apptInspectionDateDto.setInspectionDate(inspectionDates);
            apptInspectionDateDto.setApptUserCalendarDtoListAll(apptUserCalendarDtoListAll);
        }
        return apptInspectionDateDto;
    }

    private String getApptDateToShow(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int curHour24 = cal.get(Calendar.HOUR_OF_DAY);
        String hours;
        if(curHour24 > 12){
            hours = (curHour24 - 12) + "pm";
        } else {
            hours = curHour24 + "am";
        }
        String[] weeks = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String week = weeks[w];
        SimpleDateFormat format = new SimpleDateFormat("d");
        String temp = format.format(date);
        if(temp.endsWith("1") && !temp.endsWith("11")){
            format = new SimpleDateFormat("dd'st'MMMM yyyy", Locale.ENGLISH);
        }else if(temp.endsWith("2") && !temp.endsWith("12")){
            format = new SimpleDateFormat("dd'nd'MMMM yyyy",Locale.ENGLISH);
        }else if(temp.endsWith("3") && !temp.endsWith("13")){
            format = new SimpleDateFormat("dd'rd'MMMM yyyy",Locale.ENGLISH);
        }else{
            format = new SimpleDateFormat("dd'th'MMMM yyyy",Locale.ENGLISH);
        }
        String englishDate = format.format(date);
        String fullDate = week + ", " + englishDate + ", " + hours;
        return fullDate;
    }

    private List<String> getSystemCorrIdByUserId(String userId, List<String> systemCorrIds) {
        List<String> systemCorrIdList = appointmentClient.getIdByAgencyUserId(userId).getEntity();
        if(!IaisCommonUtils.isEmpty(systemCorrIdList)){
            for(String sId : systemCorrIdList){
                systemCorrIds.add(sId);
            }
        }
        return systemCorrIds;
    }

    @Override
    public List<SelectOption> getProcessDecList() {
        String[] processDecArr = new String[]{InspectionConstants.PROCESS_DECI_ALLOW_SYSTEM_TO_PROPOSE_DATE, InspectionConstants.PROCESS_DECI_ASSIGN_SPECIFIC_DATE};
        List<SelectOption> processDecOption = MasterCodeUtil.retrieveOptionsByCodes(processDecArr);
        return processDecOption;
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
    public void saveLeadSpecificDate(ApptInspectionDateDto apptInspectionDateDto) {
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = new ArrayList<>();
        String appPremCorrId = apptInspectionDateDto.getTaskDto().getRefNo();
        String serviceId = apptInspectionDateDto.getAppointmentDto().getServiceId();
        Date submitDt = apptInspectionDateDto.getAppointmentDto().getSubmitDt();
        AppPremisesInspecApptDto appPremisesInspecApptDto = new AppPremisesInspecApptDto();
        appPremisesInspecApptDto.setAppCorrId(appPremCorrId);
        appPremisesInspecApptDto.setApptRefNo(null);
        appPremisesInspecApptDto.setSpecificInspDate(apptInspectionDateDto.getSpecificDate());
        appPremisesInspecApptDto.setId(null);
        appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesInspecApptDtoList.add(appPremisesInspecApptDto);

        appPremisesInspecApptDtoList = applicationClient.createAppPremisesInspecApptDto(appPremisesInspecApptDtoList).getEntity();
        createFeAppPremisesInspecApptDto(appPremisesInspecApptDtoList);
        String url = systemParamConfig.getInterServerName() +
                MessageConstants.MESSAGE_INBOX_URL_APPT_LEAD_INSP_DATE +
                appPremCorrId;
        createMessage(url, serviceId);
        inspectionDateSendEmail(submitDt);
        updateStatusAndCreateHistory(apptInspectionDateDto.getTaskDtos(), InspectionConstants.INSPECTION_STATUS_PENDING_APPLICANT_CHECK_SPECIFIC_INSP_DATE, InspectionConstants.PROCESS_DECI_ASSIGN_SPECIFIC_DATE);
    }

    @Override
    public void saveSystemInspectionDate(ApptInspectionDateDto apptInspectionDateDto) {
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = new ArrayList<>();
        String appPremCorrId = apptInspectionDateDto.getTaskDto().getRefNo();
        String serviceId = apptInspectionDateDto.getAppointmentDto().getServiceId();
        for(ApptUserCalendarDto aucDto : apptInspectionDateDto.getApptUserCalendarDtoListAll()) {
            AppPremisesInspecApptDto appPremisesInspecApptDto = new AppPremisesInspecApptDto();
            appPremisesInspecApptDto.setAppCorrId(appPremCorrId);
            appPremisesInspecApptDto.setApptRefNo(aucDto.getApptRefNo());
            appPremisesInspecApptDto.setSpecificInspDate(null);
            appPremisesInspecApptDto.setId(null);
            appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appPremisesInspecApptDtoList.add(appPremisesInspecApptDto);
        }

        appPremisesInspecApptDtoList = applicationClient.createAppPremisesInspecApptDto(appPremisesInspecApptDtoList).getEntity();
        createFeAppPremisesInspecApptDto(appPremisesInspecApptDtoList);
        String url = systemParamConfig.getInterServerName() +
                MessageConstants.MESSAGE_INBOX_URL_APPT_SYS_INSP_DATE +
                appPremCorrId;
        createMessage(url, serviceId);
        Date submitDt = apptInspectionDateDto.getAppointmentDto().getSubmitDt();
        inspectionDateSendEmail(submitDt);
        updateStatusAndCreateHistory(apptInspectionDateDto.getTaskDtos(), InspectionConstants.INSPECTION_STATUS_PENDING_APPLICANT_CHECK_INSPECTION_DATE, InspectionConstants.PROCESS_DECI_ALLOW_SYSTEM_TO_PROPOSE_DATE);
    }

    @Override
    public List<SelectOption> getReShProcessDecList(ApptInspectionDateDto apptInspectionDateDto) {
        String[] processDecArr;
        if(apptInspectionDateDto.getAppPremisesInspecApptDto() != null){
            processDecArr = new String[]{InspectionConstants.PROCESS_DECI_ACCEPTS_THE_DATE, InspectionConstants.PROCESS_DECI_ASSIGN_SPECIFIC_DATE};
        } else {
            processDecArr = new String[]{InspectionConstants.PROCESS_DECI_ASSIGN_SPECIFIC_DATE};
        }
        List<SelectOption> processDecOption = MasterCodeUtil.retrieveOptionsByCodes(processDecArr);
        return processDecOption;
    }

    @Override
    public ApptInspectionDateDto getApptSpecificDate(String taskId, ApptInspectionDateDto apptInspectionDateDto) {
        TaskDto taskDto = taskService.getTaskById(taskId);
        AppPremisesInspecApptDto appPremisesInspecApptDto = inspectionTaskClient.getSpecificDtoByAppPremCorrId(taskDto.getRefNo()).getEntity();
        List<TaskDto> taskDtoList = organizationClient.getCurrTaskByRefNo(taskDto.getRefNo()).getEntity();
        String specificDateStr = "-";
        String apptFeReason = "-";
        if(appPremisesInspecApptDto != null) {
            specificDateStr = getApptDateToShow(appPremisesInspecApptDto.getSpecificInspDate());
            apptInspectionDateDto.setAppPremisesInspecApptDto(appPremisesInspecApptDto);

        }
        apptInspectionDateDto.setApptFeSpecificDate(specificDateStr);
        apptInspectionDateDto.setApptFeReason(apptFeReason);
        apptInspectionDateDto.setTaskDtos(taskDtoList);
        apptInspectionDateDto.setTaskDto(taskDto);
        return apptInspectionDateDto;
    }

    @Override
    public void saveSpecificDateLast(ApptInspectionDateDto apptInspectionDateDto, LoginContext loginContext) {
        AppPremisesInspecApptDto appPremisesInspecApptDto = apptInspectionDateDto.getAppPremisesInspecApptDto();
        TaskDto taskDto = apptInspectionDateDto.getTaskDto();
        List<TaskDto> taskDtoList = apptInspectionDateDto.getTaskDtos();
        updateTaskDtoList(taskDtoList);
        List<TaskDto> taskDtos = new ArrayList<>();
        for(TaskDto taskDto2 : taskDtoList){
            TaskDto tDto = createTaskDto(taskDto2, loginContext.getUserId());
            taskDtos.add(tDto);
        }
        taskService.createTasks(taskDtos);
        Date saveDate = null;
        if(apptInspectionDateDto.getProcessDec().equals(InspectionConstants.PROCESS_DECI_ASSIGN_SPECIFIC_DATE)){
            List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = new ArrayList<>();
            saveDate = apptInspectionDateDto.getSpecificDate();
            if(appPremisesInspecApptDto != null) {
                //update and create
                appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                applicationClient.updateAppPremisesInspecApptDto(appPremisesInspecApptDto);
            }
            AppPremisesInspecApptDto appPremInspApptDto = new AppPremisesInspecApptDto();
            appPremInspApptDto.setAppCorrId(taskDto.getRefNo());
            appPremInspApptDto.setApptRefNo(null);
            appPremInspApptDto.setSpecificInspDate(saveDate);
            appPremInspApptDto.setId(null);
            appPremInspApptDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremInspApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appPremisesInspecApptDtoList.add(appPremisesInspecApptDto);
            applicationClient.createAppPremisesInspecApptDto(appPremisesInspecApptDtoList);
        } else if(apptInspectionDateDto.getProcessDec().equals(InspectionConstants.PROCESS_DECI_ACCEPTS_THE_DATE)) {
            saveDate = appPremisesInspecApptDto.getSpecificInspDate();
        }
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(taskDto.getRefNo(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
        if(appPremisesRecommendationDto == null){
            appPremisesRecommendationDto = new AppPremisesRecommendationDto();
            appPremisesRecommendationDto.setAppPremCorreId(taskDto.getRefNo());
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremisesRecommendationDto.setVersion(1);
            appPremisesRecommendationDto.setRecomInDate(saveDate);
            appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
            appPremisesRecommendationDto.setRecomDecision(InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY);
            appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fillUpCheckListGetAppClient.saveAppRecom(appPremisesRecommendationDto);
        } else {
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fillUpCheckListGetAppClient.updateAppRecom(appPremisesRecommendationDto);
            AppPremisesRecommendationDto appPremRecDto = new AppPremisesRecommendationDto();
            appPremRecDto.setId(null);
            appPremRecDto.setAppPremCorreId(taskDto.getRefNo());
            appPremRecDto.setVersion(appPremisesRecommendationDto.getVersion() + 1);
            appPremRecDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremRecDto.setRecomInDate(saveDate);
            appPremRecDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
            appPremRecDto.setRecomDecision(InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY);
            appPremRecDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fillUpCheckListGetAppClient.saveAppRecom(appPremRecDto);
        }
        updateInspectionStatus(taskDto.getRefNo(), InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
        ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppCorrId(taskDto.getRefNo());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
        applicationDto1 = applicationService.updateFEApplicaiton(applicationDto1);
        inspectionAssignTaskService.createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), null, apptInspectionDateDto.getProcessDec(), taskDto.getRoleId(), HcsaConsts.ROUTING_STAGE_PRE, taskDto.getWkGrpId());
        inspectionAssignTaskService.createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(), applicationDto1.getStatus(), taskDto.getTaskKey(), null, null, null, null, taskDto.getWkGrpId());
    }

    private TaskDto createTaskDto(TaskDto taskDto, String userId) {
        TaskDto tDto = new TaskDto();
        tDto.setId(null);
        tDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        tDto.setPriority(taskDto.getPriority());
        tDto.setRefNo(taskDto.getRefNo());
        tDto.setSlaAlertInDays(taskDto.getSlaAlertInDays());
        tDto.setSlaDateCompleted(null);
        tDto.setSlaInDays(taskDto.getSlaInDays());
        tDto.setSlaRemainInDays(null);
        tDto.setTaskKey(taskDto.getTaskKey());
        tDto.setTaskType(taskDto.getTaskType());
        tDto.setWkGrpId(taskDto.getWkGrpId());
        tDto.setUserId(userId);
        tDto.setDateAssigned(new Date());
        tDto.setRoleId(taskDto.getRoleId());
        taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION);
        taskDto.setScore(taskDto.getScore());
        tDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return tDto;
    }

    private void updateTaskDtoList(List<TaskDto> taskDtoList) {
        for(TaskDto tDto : taskDtoList){
            tDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
            tDto.setSlaDateCompleted(new Date());
            tDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            taskService.updateTask(tDto);
        }
    }

    private void createFeAppPremisesInspecApptDto(List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        beEicGatewayClient.createAppPremisesInspecApptDto(appPremisesInspecApptDtoList, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }

    private void updateStatusAndCreateHistory(List<TaskDto> taskDtos, String inspecStatus, String processDec) {
        String refNo = taskDtos.get(0).getRefNo();
        TaskDto taskDto = taskDtos.get(0);
        ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppCorrId(refNo);
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING);
        applicationService.updateFEApplicaiton(applicationDto1);
        updateInspectionStatus(refNo, inspecStatus);
        updateTaskDtoList(taskDtos);
        inspectionAssignTaskService.createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), null, processDec, taskDto.getRoleId(), HcsaConsts.ROUTING_STAGE_PRE, taskDto.getWkGrpId());
        inspectionAssignTaskService.createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(), applicationDto1.getStatus(), taskDto.getTaskKey(), null, null, null, null, taskDto.getWkGrpId());
    }

    private void updateInspectionStatus(String appPremCorrId, String status) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorrId).getEntity();
        appInspectionStatusDto.setStatus(status);
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appInspectionStatusClient.update(appInspectionStatusDto);
    }

    private void inspectionDateSendEmail(Date submitDt) {
        InspectionEmailTemplateDto inspectionEmailTemplateDto = inspEmailService.loadingEmailTemplate("DD00433B-924F-EA11-BE7F-000C29F371DC");
        if(inspectionEmailTemplateDto != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
            String strSubmitDt = format.format(submitDt);
            Map<String, Object> map = new HashMap<>();
            map.put("submitDt", StringUtil.viewHtml(strSubmitDt));
            String mesContext = null;
            try {
                mesContext = MsgUtil.getTemplateMessageByContent(inspectionEmailTemplateDto.getMessageContent(), map);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject(inspectionEmailTemplateDto.getSubject());
            emailDto.setSender(AppConsts.MOH_AGENCY_NAME);

            //String requestRefNum = emailClient.sendNotification(emailDto).getEntity();
        }
    }

    private void createMessage(String url, String serviceId) {
        InterMessageDto interMessageDto = new InterMessageDto();
        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_SRC_ID);
        interMessageDto.setSubject(MessageConstants.MESSAGE_SUBJECT_APPT_INSPECTION_DATE);
        interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
        String mesNO = inboxMsgService.getMessageNo();
        interMessageDto.setRefNo(mesNO);
        interMessageDto.setService_id(serviceId);
        interMessageDto.setProcessUrl(url);
        interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        inboxMsgService.saveInterMessage(interMessageDto);
    }

    private ApplicationDto updateApplication(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationViewService.updateApplicaiton(applicationDto);
    }
}
