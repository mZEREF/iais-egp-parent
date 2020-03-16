package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
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
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

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
        //get all application info from same premises
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = getAppPremisesCorrelationsByPremises(taskDto.getRefNo());
        List<String> premCorrIds = getCorrIdsByCorrIdFromPremises(appPremisesCorrelationDtos);
        apptInspectionDateDto.setRefNo(premCorrIds);
        //get Other Tasks From The Same Premises
        List<TaskDto> taskDtoList = getAllTaskFromSamePremises(premCorrIds);
        //get application info show
        Map<ApplicationDto, List<String>> applicationInfoMap = getApplicationInfoToShow(premCorrIds, taskDtoList);
        apptInspectionDateDto.setApplicationInfoShow(applicationInfoMap);
        //The All Tasks is go to inspection or (some of them jump over Inspection and some of them go to inspection)
        String actionButtonFlag = getActionButtonFlag(apptInspectionDateDto);
        if(AppConsts.SUCCESS.equals(actionButtonFlag)) {
            //get Applicant set start date and end date from appGroup
            AppointmentDto appointmentDto = inspectionTaskClient.getApptStartEndDateByAppCorrId(taskDto.getRefNo()).getEntity();
            Map<String, String> corrIdServiceIdMap = getServiceIdsByCorrIdsFromPremises(premCorrIds);
            List<String> serviceIds = new ArrayList<>();
            for (Map.Entry<String, String> map : corrIdServiceIdMap.entrySet()) {
                serviceIds.add(map.getValue());
            }
            //get Start date and End date when group no date
            if (appointmentDto.getStartDate() == null && appointmentDto.getEndDate() == null) {
                appointmentDto.setServiceIds(serviceIds);
                appointmentDto = hcsaConfigClient.getApptStartEndDateByService(appointmentDto).getEntity();
            }
            //get inspection date
            List<AppointmentUserDto> appointmentUserDtos = new ArrayList<>();
            for (TaskDto tDto : taskDtoList) {
                AppointmentUserDto appointmentUserDto = new AppointmentUserDto();
                appointmentUserDto.setLoginUserId(tDto.getUserId());
                appointmentUserDto.setWorkGrpName(tDto.getWkGrpId());
                //get service id by task refno
                String serviceId = corrIdServiceIdMap.get(tDto.getRefNo());
                //get manHours by service and stage
                int manHours = hcsaConfigClient.getManHour(serviceId, HcsaConsts.ROUTING_STAGE_INS).getEntity();
                appointmentUserDto.setWorkHours(manHours);
                appointmentUserDtos.add(appointmentUserDto);
            }
            appointmentDto.setUsers(appointmentUserDtos);
            Map<String, List<ApptUserCalendarDto>> inspectionDateMap = appointmentClient.getUserCalendarByUserId(appointmentDto).getEntity();
            apptInspectionDateDto = getShowTimeStringList(inspectionDateMap, apptInspectionDateDto);
        } else {

        }
        apptInspectionDateDto.setTaskDto(taskDto);
        apptInspectionDateDto.setTaskDtos(taskDtoList);

        return apptInspectionDateDto;
    }

    @Override
    public Map<ApplicationDto, List<String>> getApplicationInfoToShow(List<String> premCorrIds, List<TaskDto> taskDtoList) {
        Map<ApplicationDto, List<String>> applicationInfoMap = new HashMap<>();
        List<String> workerName = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        if(!IaisCommonUtils.isEmpty(premCorrIds)) {
            for (String appPremCorrId : premCorrIds) {
                ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(appPremCorrId).getEntity();
                if(!IaisCommonUtils.isEmpty(taskDtoList)){
                    for(TaskDto taskDto : taskDtoList){
                        ids.add(taskDto.getUserId());
                    }
                }
                Set<String> idSet = new HashSet<>(ids);
                ids = new ArrayList<>(idSet);
                List<OrgUserDto> orgUserDtos = organizationClient.retrieveOrgUserAccount(ids).getEntity();
                if(!IaisCommonUtils.isEmpty(orgUserDtos)){
                    for(OrgUserDto userDto : orgUserDtos){
                        workerName.add(userDto.getDisplayName());
                    }
                } else {
                    workerName.add("-");
                }
                applicationInfoMap.put(applicationDto, workerName);
            }
        }
        return applicationInfoMap;
    }

    @Override
    public String getActionButtonFlag(ApptInspectionDateDto apptInspectionDateDto) {
        String actionButtonFlag = AppConsts.SUCCESS;
        if(!IaisCommonUtils.isEmpty(apptInspectionDateDto.getRefNo())){
            for(String appPremCorrId : apptInspectionDateDto.getRefNo()){
                List<TaskDto> taskDtos = organizationClient.getCurrTaskByRefNo(appPremCorrId).getEntity();
                actionButtonFlag = getActionButtonFlagByTasks(taskDtos);
                if(AppConsts.FAIL.equals(actionButtonFlag)){
                    return actionButtonFlag;
                }
            }
        }
        return actionButtonFlag;
    }

    private String getActionButtonFlagByTasks(List<TaskDto> taskDtos) {
        if(!IaisCommonUtils.isEmpty(taskDtos)){
            int apSo = 0;
            int insp = 0;
            int ao = 0;
            for(TaskDto taskDto : taskDtos){
                if(RoleConsts.USER_ROLE_ASO.equals(taskDto.getRoleId()) || RoleConsts.USER_ROLE_PSO.equals(taskDto.getRoleId())) {
                    apSo = apSo + 1;
                } else if(RoleConsts.USER_ROLE_INSPECTIOR.equals(taskDto.getRoleId()) || RoleConsts.USER_ROLE_INSPECTION_LEAD.equals(taskDto.getRoleId())) {
                    insp = insp + 1;
                } else if(RoleConsts.USER_ROLE_AO1.equals(taskDto.getRoleId()) ||
                          RoleConsts.USER_ROLE_AO2.equals(taskDto.getRoleId()) ||
                          RoleConsts.USER_ROLE_AO3.equals(taskDto.getRoleId())) {
                    ao = ao + 1;
                }

            }
            //task on ASO / PSO
            if(apSo > 0 && insp == 0 && ao == 0) {
                return AppConsts.FAIL;
            //task on inspector / Lead / AO
            } else if(apSo == 0 && (insp > 0 || ao > 0)) {
                return AppConsts.SUCCESS;
            //application RFI
            } else {
                return AppConsts.FAIL;
            }
        }
        return AppConsts.FAIL;
    }

    private Map<String, String> getServiceIdsByCorrIdsFromPremises(List<String> premCorrIds) {
        Map<String, String> serviceIds = applicationClient.getServiceIdsByCorrIdsFromPremises(premCorrIds).getEntity();
        return serviceIds;
    }

    private List<TaskDto> getAllTaskFromSamePremises(List<String> premCorrIds) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        if(!IaisCommonUtils.isEmpty(premCorrIds)){
            for(String appPremCorrId : premCorrIds){
                List<TaskDto> taskDtos = organizationClient.getCurrTaskByRefNo(appPremCorrId).getEntity();
                if(!IaisCommonUtils.isEmpty(taskDtos)){
                    for(TaskDto taskDto : taskDtos){
                        taskDtoList.add(taskDto);
                    }
                }
            }
        }
        return taskDtoList;
    }

    private List<String> getCorrIdsByCorrIdFromPremises(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos) {
        List<String> appPremCorrIds = new ArrayList<>();
        if(!IaisCommonUtils.isEmpty(appPremisesCorrelationDtos)){
            for(AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos){
                appPremCorrIds.add(appPremisesCorrelationDto.getId());
            }
        }
        return appPremCorrIds;
    }

    private List<AppPremisesCorrelationDto> getAppPremisesCorrelationsByPremises(String appPremCorrId) {
        return appPremisesCorrClient.getAppPremisesCorrelationsByPremises(appPremCorrId).getEntity();
    }

    private ApptInspectionDateDto getShowTimeStringList(Map<String, List<ApptUserCalendarDto>> inspectionDateMap, ApptInspectionDateDto apptInspectionDateDto) {
        List<String> inspectionDates = new ArrayList<>();
        if(inspectionDateMap != null){
            List<ApptUserCalendarDto> apptUserCalendarDtoListAll = new ArrayList<>();
            for(Map.Entry<String, List<ApptUserCalendarDto>> inspDateMap : inspectionDateMap.entrySet()){
                inspDateMap.getValue();
            }
            apptInspectionDateDto.setInspectionDate(inspectionDates);
            apptInspectionDateDto.setApptUserCalendarDtoListAll(apptUserCalendarDtoListAll);
        }
        return apptInspectionDateDto;
    }

    private String getApptDateToShow(Date date) {
        String specificDate =  Formatter.formatDateTime(date, "d MMM");
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
    public void saveLeadSpecificDate(ApptInspectionDateDto apptInspectionDateDto, ApplicationViewDto applicationViewDto) {
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
        String url = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() +
                MessageConstants.MESSAGE_INBOX_URL_APPT_LEAD_INSP_DATE + appPremCorrId;
        String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
        inspectionDateSendEmail(submitDt, url, licenseeId);
        createMessage(url, serviceId, submitDt);
        updateStatusAndCreateHistory(apptInspectionDateDto.getTaskDtos(), InspectionConstants.INSPECTION_STATUS_PENDING_APPLICANT_CHECK_SPECIFIC_INSP_DATE, InspectionConstants.PROCESS_DECI_ASSIGN_SPECIFIC_DATE);
    }

    @Override
    public void saveSystemInspectionDate(ApptInspectionDateDto apptInspectionDateDto, ApplicationViewDto applicationViewDto) {
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
        String url = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() +
                MessageConstants.MESSAGE_INBOX_URL_APPT_SYS_INSP_DATE + appPremCorrId;
        Date submitDt = apptInspectionDateDto.getAppointmentDto().getSubmitDt();
        String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
        inspectionDateSendEmail(submitDt, url, licenseeId);
        createMessage(url, serviceId, submitDt);
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
        //get All app from the same Premises
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = getAppPremisesCorrelationsByPremises(taskDto.getRefNo());
        List<String> premCorrIds = getCorrIdsByCorrIdFromPremises(appPremisesCorrelationDtos);
        //get All inspection date
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtos = inspectionTaskClient.getSpecificDtosByAppPremCorrIds(premCorrIds).getEntity();
        apptInspectionDateDto.setRefNo(premCorrIds);

        //get Other Tasks From The Same Premises
        List<TaskDto> taskDtoList = getAllTaskFromSamePremises(premCorrIds);
        String specificDateStr = "-";
        String apptFeReason = "-";
        if(!IaisCommonUtils.isEmpty(appPremisesInspecApptDtos)) {
            for(AppPremisesInspecApptDto appPremisesInspecApptDto : appPremisesInspecApptDtos) {
                specificDateStr = getApptDateToShow(appPremisesInspecApptDto.getSpecificInspDate());
            }
            apptInspectionDateDto.setAppPremisesInspecApptDtos(appPremisesInspecApptDtos);
        }
        apptInspectionDateDto.setApptFeSpecificDate(specificDateStr);
        apptInspectionDateDto.setApptFeReason(apptFeReason);
        apptInspectionDateDto.setTaskDtos(taskDtoList);
        apptInspectionDateDto.setTaskDto(taskDto);
        return apptInspectionDateDto;
    }

    @Override
    public void saveSpecificDateLast(ApptInspectionDateDto apptInspectionDateDto, LoginContext loginContext) {
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtos = apptInspectionDateDto.getAppPremisesInspecApptDtos();
        List<String> appPremCorrIds = apptInspectionDateDto.getRefNo();
        List<TaskDto> taskDtoList = apptInspectionDateDto.getTaskDtos();
        updateTaskDtoList(taskDtoList);
        List<TaskDto> taskDtos = new ArrayList<>();
        //get appPremCorrId with Score
        Map<String, Integer> appPremScoreMap = getAppPremTaskScore(taskDtoList);
        for (TaskDto taskDto2 : taskDtoList) {
            int score = appPremScoreMap.get(taskDto2.getRefNo());
            TaskDto tDto = createTaskDto(taskDto2, loginContext.getUserId(), score);
            taskDtos.add(tDto);
        }
        taskService.createTasks(taskDtos);
        Date saveDate = null;
        if (apptInspectionDateDto.getProcessDec().equals(InspectionConstants.PROCESS_DECI_ASSIGN_SPECIFIC_DATE)) {
            List<AppPremisesInspecApptDto> appPremisesInspecApptDtoCreateList = new ArrayList<>();
            List<AppPremisesInspecApptDto> appPremisesInspecApptDtoUpdateList = new ArrayList<>();
            saveDate = apptInspectionDateDto.getSpecificDate();
            //remove inactive inspection date
            if (!IaisCommonUtils.isEmpty(appPremisesInspecApptDtos)) {
                for (AppPremisesInspecApptDto appPremisesInspecApptDto : appPremisesInspecApptDtos) {
                    appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    appPremisesInspecApptDto = applicationClient.updateAppPremisesInspecApptDto(appPremisesInspecApptDto).getEntity();
                    appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    appPremisesInspecApptDtoUpdateList.add(appPremisesInspecApptDto);
                }
            }
            //create inspection date
            if (!IaisCommonUtils.isEmpty(appPremCorrIds)) {
                for (String appPremCorrId : appPremCorrIds) {
                    AppPremisesInspecApptDto appPremInspApptDto = new AppPremisesInspecApptDto();
                    appPremInspApptDto.setAppCorrId(appPremCorrId);
                    appPremInspApptDto.setApptRefNo(null);
                    appPremInspApptDto.setSpecificInspDate(saveDate);
                    appPremInspApptDto.setId(null);
                    appPremInspApptDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    appPremInspApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    appPremisesInspecApptDtoCreateList.add(appPremInspApptDto);
                }
            }
            appPremisesInspecApptDtoCreateList = applicationClient.createAppPremisesInspecApptDto(appPremisesInspecApptDtoCreateList).getEntity();
            for (AppPremisesInspecApptDto apptDto : appPremisesInspecApptDtoCreateList) {
                apptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            }
            //synchronize FE
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            ApptFeConfirmDateDto apptFeConfirmDateDto = new ApptFeConfirmDateDto();
            apptFeConfirmDateDto.setAppPremisesInspecApptCreateList(appPremisesInspecApptDtoCreateList);
            apptFeConfirmDateDto.setAppPremisesInspecApptUpdateList(appPremisesInspecApptDtoUpdateList);
            beEicGatewayClient.reSchedulingSaveFeDate(apptFeConfirmDateDto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());

        } else if (apptInspectionDateDto.getProcessDec().equals(InspectionConstants.PROCESS_DECI_ACCEPTS_THE_DATE)) {
            saveDate = appPremisesInspecApptDtos.get(0).getSpecificInspDate();
        }
        //save Inspection date / status, save Application
        saveRoutingData(appPremCorrIds, taskDtoList, saveDate, apptInspectionDateDto);
    }

    private void saveRoutingData(List<String> appPremCorrIds, List<TaskDto> taskDtoList, Date saveDate, ApptInspectionDateDto apptInspectionDateDto) {
        for(String appPremCorrId : appPremCorrIds){
            TaskDto taskDto = null;
            for(TaskDto tDto : taskDtoList){
                if(appPremCorrId.equals(tDto.getRefNo())){
                    taskDto = tDto;
                }
            }
            AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrId, InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
            //save Inspection date
            createOrUpdateRecommendation(appPremisesRecommendationDto, appPremCorrId, saveDate);
            //update Inspection status
            updateInspectionStatus(appPremCorrId, InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
            //Application data
            ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(appPremCorrId).getEntity();
            ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
            applicationDto1 = applicationService.updateFEApplicaiton(applicationDto1);
            if(taskDto != null) {
                inspectionAssignTaskService.createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), null, apptInspectionDateDto.getProcessDec(), taskDto.getRoleId(), HcsaConsts.ROUTING_STAGE_PRE, taskDto.getWkGrpId());
                inspectionAssignTaskService.createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(), applicationDto1.getStatus(), taskDto.getTaskKey(), null, null, null, null, taskDto.getWkGrpId());
            }
        }
    }

    private void createOrUpdateRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto, String appPremCorrId, Date saveDate) {
        if (appPremisesRecommendationDto == null) {
            appPremisesRecommendationDto = new AppPremisesRecommendationDto();
            appPremisesRecommendationDto.setAppPremCorreId(appPremCorrId);
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
            appPremRecDto.setAppPremCorreId(appPremCorrId);
            appPremRecDto.setVersion(appPremisesRecommendationDto.getVersion() + 1);
            appPremRecDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremRecDto.setRecomInDate(saveDate);
            appPremRecDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
            appPremRecDto.setRecomDecision(InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY);
            appPremRecDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fillUpCheckListGetAppClient.saveAppRecom(appPremRecDto);
        }
    }

    private Map<String, Integer> getAppPremTaskScore(List<TaskDto> taskDtoList) {
        Map<String, Integer> appPremScoreMap = new HashMap<>();
        List<String> appPremCorrIds = new ArrayList<>();
        for(TaskDto taskDto : taskDtoList){
            appPremCorrIds.add(taskDto.getRefNo());
        }
        Set<String> appPremCorrIdSet = new HashSet<>(appPremCorrIds);
        appPremCorrIds = new ArrayList<>(appPremCorrIdSet);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = new ArrayList<>();
        for(String appPremCorrId : appPremCorrIds){
            ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(appPremCorrId).getEntity();
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = generateHcsaSvcStageWorkingGroupDto(appPremCorrId, applicationDto, HcsaConsts.ROUTING_STAGE_INS);
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        if(!IaisCommonUtils.isEmpty(hcsaSvcStageWorkingGroupDtos)){
            for(HcsaSvcStageWorkingGroupDto hDto : hcsaSvcStageWorkingGroupDtos){
                appPremScoreMap.put(hDto.getTaskRefNo(), hDto.getCount());
            }
        }
        return appPremScoreMap;
    }

    private HcsaSvcStageWorkingGroupDto generateHcsaSvcStageWorkingGroupDto(String appPremCorrId, ApplicationDto applicationDto, String routingStageIns) {
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
        hcsaSvcStageWorkingGroupDto.setStageId(routingStageIns);
        hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
        hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
        hcsaSvcStageWorkingGroupDto.setTaskRefNo(appPremCorrId);
        return hcsaSvcStageWorkingGroupDto;
    }

    private TaskDto createTaskDto(TaskDto taskDto, String userId, int score) {
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
        taskDto.setScore(score);
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

    private void inspectionDateSendEmail(Date submitDt, String url, String licenseeId) {
        InspectionEmailTemplateDto inspectionEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_NC_RECTIFICATION);
        if(inspectionEmailTemplateDto != null) {
            String strSubmitDt = Formatter.formatDateTime(submitDt, "dd MMM yyyy");
            Map<String, Object> map = new HashMap<>();
            map.put("submitDt", StringUtil.viewHtml(strSubmitDt));
            map.put("process_url", StringUtil.viewHtml(url));
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
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            //String requestRefNum = emailClient.sendNotification(emailDto).getEntity();
        }
    }

    private void createMessage(String url, String serviceId, Date submitDt) {
        MsgTemplateDto mtd = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_NC_RECTIFICATION).getEntity();
        Map<String, Object> map = new HashMap<>();
        String strSubmitDt = Formatter.formatDateTime(submitDt, "dd MMM yyyy");
        map.put("submitDt", StringUtil.viewHtml(strSubmitDt));
        map.put("process_url", StringUtil.viewHtml(url));
        String templateMessageByContent = null;
        try {
            templateMessageByContent = MsgUtil.getTemplateMessageByContent(mtd.getMessageContent(), map);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        InterMessageDto interMessageDto = new InterMessageDto();
        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
        interMessageDto.setSubject(MessageConstants.MESSAGE_SUBJECT_APPT_INSPECTION_DATE);
        interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
        String mesNO = inboxMsgService.getMessageNo();
        interMessageDto.setRefNo(mesNO);
        interMessageDto.setService_id(serviceId);
        interMessageDto.setUserId(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        interMessageDto.setMsgContent(templateMessageByContent);
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        inboxMsgService.saveInterMessage(interMessageDto);
    }

    private ApplicationDto updateApplication(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationViewService.updateApplicaiton(applicationDto);
    }
}