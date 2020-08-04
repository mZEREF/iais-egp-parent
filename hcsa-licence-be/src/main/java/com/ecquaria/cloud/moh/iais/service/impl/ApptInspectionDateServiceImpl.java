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
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremInspCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptCalendarStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.ApptInspectionDateService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.OfficersReSchedulingService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppEicClient;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
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
import java.text.ParseException;
import java.util.ArrayList;
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
    private NotificationHelper notificationHelper;

    @Autowired
    private AppEicClient appEicClient;

    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;

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
    private AppointmentClient appointmentClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private OfficersReSchedulingService officersReSchedulingService;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

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

    @Override
    public ApptInspectionDateDto getInspectionDate(TaskDto taskDto, ApptInspectionDateDto apptInspectionDateDto, ApplicationViewDto applicationViewDto) {
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        boolean fastFlag = applicationDto.isFastTracking();
        String actionButtonFlag;
        List<String> premCorrIds;
        List<TaskDto> taskDtoList;
        Map<String, ApplicationDto> corrAppMap = IaisCommonUtils.genNewHashMap();
        if(!fastFlag) {
            //get all application info from same premises
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = getAppPremisesCorrelationsByPremises(taskDto.getRefNo());
            //filter cancel application
            corrAppMap = filterCancelAppByCorr(appPremisesCorrelationDtos);
            premCorrIds = getCorrIdsByCorrIdFromPremises(corrAppMap);
            apptInspectionDateDto.setRefNo(premCorrIds);
            //get Other Tasks From The Same Premises
            taskDtoList = getAllTaskFromSamePremises(premCorrIds);
            //The All Tasks is go to inspection or (some of them jump over Inspection and some of them go to inspection)
            actionButtonFlag = getActionButtonFlag(apptInspectionDateDto, applicationDto);
        } else {
            actionButtonFlag = AppConsts.SUCCESS;
            premCorrIds = IaisCommonUtils.genNewArrayList();
            premCorrIds.add(taskDto.getRefNo());
            corrAppMap.put(taskDto.getRefNo(), applicationDto);
            apptInspectionDateDto.setRefNo(premCorrIds);
            taskDtoList = IaisCommonUtils.genNewArrayList();
            List<TaskDto> taskDtos = organizationClient.getTasksByRefNo(taskDto.getRefNo()).getEntity();
            for(TaskDto tDto : taskDtos){
                if(tDto.getTaskStatus().equals(TaskConsts.TASK_STATUS_PENDING) || tDto.getTaskStatus().equals(TaskConsts.TASK_STATUS_READ)) {
                    taskDtoList.add(tDto);
                }
            }
        }
        //get application info show
        List<ApptAppInfoShowDto> applicationInfoShow = getApplicationInfoToShow(premCorrIds, taskDtoList, corrAppMap);
        apptInspectionDateDto.setCorrAppMap(corrAppMap);
        apptInspectionDateDto.setApplicationInfoShow(applicationInfoShow);
        apptInspectionDateDto.setActionButtonFlag(actionButtonFlag);
        apptInspectionDateDto.setTaskDto(taskDto);
        apptInspectionDateDto.setTaskDtos(taskDtoList);

        return apptInspectionDateDto;
    }

    private Map<String, ApplicationDto> filterCancelAppByCorr(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos) {
        Map<String, ApplicationDto> map = IaisCommonUtils.genNewHashMap();
        if(!IaisCommonUtils.isEmpty(appPremisesCorrelationDtos)){
            for(int i = 0; i < appPremisesCorrelationDtos.size(); i++){
                String applicationId = appPremisesCorrelationDtos.get(i).getApplicationId();
                ApplicationDto applicationDto = applicationClient.getApplicationById(applicationId).getEntity();
                if(ApplicationConsts.APPLICATION_STATUS_CREATE_AUDIT_TASK_CANCELED.equals(applicationDto.getStatus())){
                    appPremisesCorrelationDtos.remove(i);
                    i--;
                } else {
                    String appPremCorrId = appPremisesCorrelationDtos.get(i).getId();
                    map.put(appPremCorrId, applicationDto);
                }
            }
        }
        return map;
    }

    @Override
    public List<ApptAppInfoShowDto> getApplicationInfoToShow(List<String> premCorrIds, List<TaskDto> taskDtoList, Map<String, ApplicationDto> corrAppMap) {
        List<ApptAppInfoShowDto> apptAppInfoShowDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(premCorrIds)) {
            for (String appPremCorrId : premCorrIds) {
                ApptAppInfoShowDto apptAppInfoShowDto = new ApptAppInfoShowDto();
                List<String> workerName = IaisCommonUtils.genNewArrayList();
                List<String> ids = IaisCommonUtils.genNewArrayList();
                ApplicationDto applicationDto;
                if(corrAppMap == null){
                    applicationDto = inspectionTaskClient.getApplicationByCorreId(appPremCorrId).getEntity();
                } else {
                    applicationDto = corrAppMap.get(appPremCorrId);
                }
                if(!IaisCommonUtils.isEmpty(taskDtoList)){
                    for(TaskDto taskDto : taskDtoList){
                        if(taskDto.getRefNo().equals(appPremCorrId)) {
                            ids.add(taskDto.getUserId());
                        }
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
                    workerName.add(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                }
                apptAppInfoShowDto.setApplicationNo(applicationDto.getApplicationNo());
                apptAppInfoShowDto.setStatus(applicationDto.getStatus());
                apptAppInfoShowDto.setUserDisName(workerName);
                apptAppInfoShowDtos.add(apptAppInfoShowDto);
            }
        }
        return apptAppInfoShowDtos;
    }

    @Override
    public String getActionButtonFlag(ApptInspectionDateDto apptInspectionDateDto, ApplicationDto applicationDto) {
        boolean fastFlag = applicationDto.isFastTracking();
        String actionButtonFlag = AppConsts.SUCCESS;
        if(!fastFlag) {
            if (!IaisCommonUtils.isEmpty(apptInspectionDateDto.getRefNo())) {
                for (String appPremCorrId : apptInspectionDateDto.getRefNo()) {
                    List<TaskDto> taskDtos = organizationClient.getCurrTaskByRefNo(appPremCorrId).getEntity();
                    actionButtonFlag = getActionButtonFlagByTasks(taskDtos);
                    if (AppConsts.FAIL.equals(actionButtonFlag)) {
                        return actionButtonFlag;
                    }
                }
            }
        }
        return actionButtonFlag;
    }

    @Override
    public void saveAuditInspectionDate(ApptInspectionDateDto apptInspectionDateDto, ApplicationViewDto applicationViewDto) {
        TaskDto taskDto = apptInspectionDateDto.getTaskDto();
        List<TaskDto> taskDtos = apptInspectionDateDto.getTaskDtos();
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
        Date saveDate = apptInspectionDateDto.getSpecificStartDate();
        //end hour - 1, because the function save all start hour
        AppointmentDto appointmentDtoSave = officersReSchedulingService.subtractEndHourByApptDto(apptInspectionDateDto.getSpecificApptDto());
        //confirm date and calendar
        String apptRefNo = appointmentClient.saveManualUserCalendar(appointmentDtoSave).getEntity();
        List<String> confirmRefNo = IaisCommonUtils.genNewArrayList();
        confirmRefNo.add(apptRefNo);
        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        apptCalendarStatusDto.setConfirmRefNums(confirmRefNo);
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        cancelOrConfirmApptDate(apptCalendarStatusDto);

        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = IaisCommonUtils.genNewArrayList();
        for(TaskDto taskDto1 : taskDtos) {
            String appPremCorrId = taskDto1.getRefNo();
            AppPremisesInspecApptDto appPremisesInspecApptDto = new AppPremisesInspecApptDto();
            appPremisesInspecApptDto.setAppCorrId(appPremCorrId);
            appPremisesInspecApptDto.setApptRefNo(apptRefNo);
            appPremisesInspecApptDto.setSpecificInspDate(apptInspectionDateDto.getSpecificDate());
            appPremisesInspecApptDto.setId(null);
            appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appPremisesInspecApptDtoList.add(appPremisesInspecApptDto);
            AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrId, InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
            //save Inspection date
            createOrUpdateRecommendation(appPremisesRecommendationDto, appPremCorrId, saveDate);
            //update Inspection status
            updateInspectionStatus(appPremCorrId, InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
            //Application data
            ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(appPremCorrId).getEntity();
            ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);

            if (taskDto != null) {
                inspectionAssignTaskService.createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), null, apptInspectionDateDto.getProcessDec(), taskDto.getRoleId(), HcsaConsts.ROUTING_STAGE_PRE, taskDto.getWkGrpId());
                inspectionAssignTaskService.createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(), applicationDto1.getStatus(), taskDto.getTaskKey(), null, null, null, null, taskDto.getWkGrpId());
            }
        }
        appPremisesInspecApptDtoList = applicationClient.createAppPremisesInspecApptDto(appPremisesInspecApptDtoList).getEntity();
        apptInspectionDateDto.setAppPremisesInspecApptCreateList(appPremisesInspecApptDtoList);
        updateTaskDtoList(taskDtos);
        for (TaskDto taskDto2 : taskDtos) {
            int score = taskDto2.getScore();
            TaskDto tDto = createTaskDto(taskDto2, taskDto2.getUserId(), score);
            taskDtoList.add(tDto);
        }
        taskService.createTasks(taskDtoList);
    }

    @Override
    public void saveAppUserCorrelation(ApptInspectionDateDto apptInspectionDateDto) {
        List<TaskDto> taskDtos = apptInspectionDateDto.getTaskDtos();
        if(!IaisCommonUtils.isEmpty(taskDtos)){
            List<AppPremInspCorrelationDto> appPremInspCorrelationDtos = IaisCommonUtils.genNewArrayList();
            for(TaskDto taskDto : taskDtos){
                AppPremInspCorrelationDto appPremInspCorrelationDto = new AppPremInspCorrelationDto();
                appPremInspCorrelationDto.setId(null);
                appPremInspCorrelationDto.setUserId(taskDto.getUserId());
                appPremInspCorrelationDto.setApplicationNo(taskDto.getApplicationNo());
                appPremInspCorrelationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                appPremInspCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPremInspCorrelationDtos.add(appPremInspCorrelationDto);
            }
            inspectionTaskClient.createAppPremInspCorrelationDto(appPremInspCorrelationDtos);
        }
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

    private List<TaskDto> getAllTaskFromSamePremises(List<String> premCorrIds) {
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(premCorrIds)){
            for(String appPremCorrId : premCorrIds){
                boolean containIdFlag = listIsContainId(appPremCorrId, taskDtoList);
                if(!containIdFlag) {
                    List<TaskDto> taskDtos = organizationClient.getCurrTaskByRefNo(appPremCorrId).getEntity();
                    if (!IaisCommonUtils.isEmpty(taskDtos)) {
                        for (TaskDto taskDto : taskDtos) {
                            if(taskDto != null) {
                                taskDtoList.add(taskDto);
                            }
                        }
                    }
                }
            }
        }
        return taskDtoList;
    }

    private boolean listIsContainId(String appPremCorrId, List<TaskDto> taskDtoList) {
        if(!IaisCommonUtils.isEmpty(taskDtoList) && !StringUtil.isEmpty(appPremCorrId)){
            for(TaskDto taskDto : taskDtoList){
                if(appPremCorrId.equals(taskDto.getRefNo())){
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    private List<String> getCorrIdsByCorrIdFromPremises(Map<String, ApplicationDto> corrAppMap) {
        List<String> appPremCorrIds = IaisCommonUtils.genNewArrayList();
        if(corrAppMap != null){
            for(Map.Entry<String, ApplicationDto> map : corrAppMap.entrySet()){
                String appPremCorrId = map.getKey();
                appPremCorrIds.add(appPremCorrId);
            }
        }
        return appPremCorrIds;
    }

    private List<AppPremisesCorrelationDto> getAppPremisesCorrelationsByPremises(String appPremCorrId) {
        return appPremisesCorrClient.getAppPremisesCorrelationsByPremises(appPremCorrId).getEntity();
    }

    private String getApptDateToShow(Date date) {
        String specificDate = Formatter.formatDateTime(date, "dd/MM/yyyy");
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
        List<SelectOption> hourOption = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption("1", "09:00");
        SelectOption so2 = new SelectOption("2", "10:00");
        SelectOption so3 = new SelectOption("3", "11:00");
        SelectOption so4 = new SelectOption("4", "12:00");
        SelectOption so5 = new SelectOption("5", "14:00");
        SelectOption so6 = new SelectOption("6", "15:00");
        SelectOption so7 = new SelectOption("7", "16:00");
        SelectOption so8 = new SelectOption("8", "17:00");
        hourOption.add(so1);
        hourOption.add(so2);
        hourOption.add(so3);
        hourOption.add(so4);
        hourOption.add(so5);
        hourOption.add(so6);
        hourOption.add(so7);
        hourOption.add(so8);
        return hourOption;
    }

    @Override
    public List<SelectOption> getInspectionDateEndHours() {
        List<SelectOption> hourOption = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption("1", "10:00");
        SelectOption so2 = new SelectOption("2", "11:00");
        SelectOption so3 = new SelectOption("3", "12:00");
        SelectOption so4 = new SelectOption("4", "13:00");
        SelectOption so5 = new SelectOption("5", "15:00");
        SelectOption so6 = new SelectOption("6", "16:00");
        SelectOption so7 = new SelectOption("7", "17:00");
        SelectOption so8 = new SelectOption("8", "18:00");
        hourOption.add(so1);
        hourOption.add(so2);
        hourOption.add(so3);
        hourOption.add(so4);
        hourOption.add(so5);
        hourOption.add(so6);
        hourOption.add(so7);
        hourOption.add(so8);
        return hourOption;
    }

    @Override
    public List<SelectOption> getAmPmOption() {
        List<SelectOption> amPmOption = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption(Formatter.DAY_AM, Formatter.DAY_AM);
        SelectOption so2 = new SelectOption(Formatter.DAY_PM, Formatter.DAY_PM);
        amPmOption.add(so1);
        amPmOption.add(so2);
        return amPmOption;
    }

    @Override
    public void saveLeadSpecificDate(ApptInspectionDateDto apptInspectionDateDto, ApplicationViewDto applicationViewDto) {
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = IaisCommonUtils.genNewArrayList();
        String urlId = apptInspectionDateDto.getTaskDto().getRefNo();
        List<String> appPremCorrIds = apptInspectionDateDto.getRefNo();
        String serviceId;
        Date submitDt;
        if(apptInspectionDateDto.getAppointmentDto() != null){
            serviceId = apptInspectionDateDto.getAppointmentDto().getServiceId();
            submitDt = apptInspectionDateDto.getAppointmentDto().getSubmitDt();
        } else {
            ApplicationGroupDto applicationGroupDto = applicationViewDto.getApplicationGroupDto();
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            serviceId = applicationDto.getServiceId();
            submitDt = applicationGroupDto.getSubmitDt();
        }
        //end hour - 1, because the function save all start hour
        AppointmentDto appointmentDtoSave = officersReSchedulingService.subtractEndHourByApptDto(apptInspectionDateDto.getSpecificApptDto());
        String apptRefNo = appointmentClient.saveManualUserCalendar(appointmentDtoSave).getEntity();
        for(String appPremCorrId : appPremCorrIds) {
            AppPremisesInspecApptDto appPremisesInspecApptDto = new AppPremisesInspecApptDto();
            appPremisesInspecApptDto.setAppCorrId(appPremCorrId);
            appPremisesInspecApptDto.setApptRefNo(apptRefNo);
            appPremisesInspecApptDto.setSpecificInspDate(null);
            appPremisesInspecApptDto.setId(null);
            appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appPremisesInspecApptDtoList.add(appPremisesInspecApptDto);
        }
        appPremisesInspecApptDtoList = applicationClient.createAppPremisesInspecApptDto(appPremisesInspecApptDtoList).getEntity();
        appPremisesInspecApptDtoList = setAudiTrailDtoInspAppt(appPremisesInspecApptDtoList, IaisEGPHelper.getCurrentAuditTrailDto());
        apptInspectionDateDto.setAppPremisesInspecApptCreateList(appPremisesInspecApptDtoList);
        createFeAppPremisesInspecApptDto(apptInspectionDateDto);
        //cancel or confirm appointment date
        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        Map<String, List<ApptUserCalendarDto>> inspectionDateMap = apptInspectionDateDto.getInspectionDateMap();
        List<String> cancelRefNo = IaisCommonUtils.genNewArrayList();
        List<String> confirmRefNo = IaisCommonUtils.genNewArrayList();
        confirmRefNo.add(apptRefNo);
        if(inspectionDateMap != null) {
            for (Map.Entry<String, List<ApptUserCalendarDto>> inspDateMap : inspectionDateMap.entrySet()) {
                String refNo = inspDateMap.getKey();
                cancelRefNo.add(refNo);
            }
        }
        apptCalendarStatusDto.setCancelRefNums(cancelRefNo);
        apptCalendarStatusDto.setConfirmRefNums(confirmRefNo);
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        cancelOrConfirmApptDate(apptCalendarStatusDto);
        //url
        String applicationNo = apptInspectionDateDto.getTaskDto().getApplicationNo();
        String url = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() +
                MessageConstants.MESSAGE_INBOX_URL_APPT_LEAD_INSP_DATE + applicationNo;
        HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
        maskParams.put("applicationNo", applicationNo);
        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_INBOX;
        String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
        //send email
        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        inspectionDateSendEmail(loginUrl, licenseeId, applicationViewDto, hcsaServiceDto, urlId);
        //get service code to send message
        String serviceCode = hcsaServiceDto.getSvcCode();
        createMessage(url, serviceCode, submitDt, licenseeId, maskParams);
        //update app Info and insp Info
        updateStatusAndCreateHistory(apptInspectionDateDto.getTaskDtos(), InspectionConstants.INSPECTION_STATUS_PENDING_APPLICANT_CHECK_SPECIFIC_INSP_DATE, InspectionConstants.PROCESS_DECI_ASSIGN_SPECIFIC_DATE);
    }

    private void cancelOrConfirmApptDate(ApptCalendarStatusDto apptCalendarStatusDto) {
        appointmentClient.updateUserCalendarStatus(apptCalendarStatusDto);
    }

    @Override
    public void saveSystemInspectionDate(ApptInspectionDateDto apptInspectionDateDto, ApplicationViewDto applicationViewDto) {
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = IaisCommonUtils.genNewArrayList();
        List<String> appPremCorrIds = apptInspectionDateDto.getRefNo();
        String serviceId = applicationViewDto.getApplicationDto().getServiceId();
        Map<String, List<ApptUserCalendarDto>> inspectionDateMap = apptInspectionDateDto.getInspectionDateMap();
        //save AppPremisesInspecApptDto
        for(String appPremCorrId : appPremCorrIds) {
            for(Map.Entry<String, List<ApptUserCalendarDto>> inspDateMap : inspectionDateMap.entrySet()){
                String apptRefNo = inspDateMap.getKey();
                AppPremisesInspecApptDto appPremisesInspecApptDto = new AppPremisesInspecApptDto();
                appPremisesInspecApptDto.setAppCorrId(appPremCorrId);
                appPremisesInspecApptDto.setApptRefNo(apptRefNo);
                appPremisesInspecApptDto.setSpecificInspDate(null);
                appPremisesInspecApptDto.setId(null);
                appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                AppointmentDto appointmentDto = apptInspectionDateDto.getAppointmentDto();
                if(appointmentDto.getStartDate() != null) {
                    try {
                        appPremisesInspecApptDto.setStartDate(Formatter.parseDateTime(appointmentDto.getStartDate(), AppConsts.DEFAULT_DATE_TIME_FORMAT));
                    } catch (ParseException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                if(appointmentDto.getEndDate() != null) {
                    try {
                        appPremisesInspecApptDto.setEndDate(Formatter.parseDateTime(appointmentDto.getEndDate(), AppConsts.DEFAULT_DATE_TIME_FORMAT));
                    } catch (ParseException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                appPremisesInspecApptDtoList.add(appPremisesInspecApptDto);
            }
        }
        appPremisesInspecApptDtoList = applicationClient.createAppPremisesInspecApptDto(appPremisesInspecApptDtoList).getEntity();
        //synchronization FE
        appPremisesInspecApptDtoList = setAudiTrailDtoInspAppt(appPremisesInspecApptDtoList, IaisEGPHelper.getCurrentAuditTrailDto());
        apptInspectionDateDto.setAppPremisesInspecApptCreateList(appPremisesInspecApptDtoList);
        createFeAppPremisesInspecApptDto(apptInspectionDateDto);
        //cancel or confirm appointment date
        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        List<String> confirmRefNo = IaisCommonUtils.genNewArrayList();
        if(inspectionDateMap != null) {
            for (Map.Entry<String, List<ApptUserCalendarDto>> inspDateMap : inspectionDateMap.entrySet()) {
                String refNo = inspDateMap.getKey();
                confirmRefNo.add(refNo);
            }
        }
        apptCalendarStatusDto.setConfirmRefNums(confirmRefNo);
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        cancelOrConfirmApptDate(apptCalendarStatusDto);
        //send message and email
        String urlId = apptInspectionDateDto.getTaskDto().getRefNo();
        String applicationNo = apptInspectionDateDto.getTaskDto().getApplicationNo();
        String url = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() +
                MessageConstants.MESSAGE_INBOX_URL_APPT_SYS_INSP_DATE + applicationNo;
        HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
        maskParams.put("applicationNo", applicationNo);
        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_INBOX;
        Date submitDt = apptInspectionDateDto.getAppointmentDto().getSubmitDt();
        String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
        //send email
        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        inspectionDateSendEmail(loginUrl, licenseeId, applicationViewDto, hcsaServiceDto, urlId);
        //get service code to send message
        String serviceCode = hcsaServiceDto.getSvcCode();
        createMessage(url, serviceCode, submitDt, licenseeId, maskParams);
        //save data to app table
        updateStatusAndCreateHistory(apptInspectionDateDto.getTaskDtos(), InspectionConstants.INSPECTION_STATUS_PENDING_APPLICANT_CHECK_INSPECTION_DATE, InspectionConstants.PROCESS_DECI_ALLOW_SYSTEM_TO_PROPOSE_DATE);
    }

    private List<AppPremisesInspecApptDto> setAudiTrailDtoInspAppt(List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList, AuditTrailDto currentAuditTrailDto) {
        if(!IaisCommonUtils.isEmpty(appPremisesInspecApptDtoList)){
            for(AppPremisesInspecApptDto appPremisesInspecApptDto : appPremisesInspecApptDtoList){
                appPremisesInspecApptDto.setAuditTrailDto(currentAuditTrailDto);
            }
            return appPremisesInspecApptDtoList;
        }
        return appPremisesInspecApptDtoList;
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
        ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(taskDto.getRefNo()).getEntity();
        String appType = applicationDto.getApplicationType();
        //get All app from the same Premises
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = getAppPremisesCorrelationsByPremises(taskDto.getRefNo());
        //filter cancel application
        Map<String, ApplicationDto> corrAppMap = filterCancelAppByCorr(appPremisesCorrelationDtos);
        List<String> premCorrIds = getCorrIdsByCorrIdFromPremises(corrAppMap);
        //get All inspection date
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtos = inspectionTaskClient.getSpecificDtosByAppPremCorrIds(premCorrIds).getEntity();
        apptInspectionDateDto.setRefNo(premCorrIds);

        //get Other Tasks From The Same Premises
        List<TaskDto> taskDtoList = getAllTaskFromSamePremises(premCorrIds);
        String specificDateStr = "-";
        String apptFeReason = "-";
        if(!IaisCommonUtils.isEmpty(appPremisesInspecApptDtos) && appPremisesInspecApptDtos.get(0) != null) {
            specificDateStr = getApptDateToShow(appPremisesInspecApptDtos.get(0).getSpecificInspDate());
            apptFeReason = appPremisesInspecApptDtos.get(0).getReason();
            apptInspectionDateDto.setAppPremisesInspecApptDtos(appPremisesInspecApptDtos);
        }
        apptInspectionDateDto.setApptFeSpecificDate(specificDateStr);
        apptInspectionDateDto.setApptFeReason(apptFeReason);
        apptInspectionDateDto.setTaskDtos(taskDtoList);
        apptInspectionDateDto.setTaskDto(taskDto);
        //get Applicant set start date and end date from appGroup
        AppointmentDto specificApptDto = inspectionTaskClient.getApptStartEndDateByAppCorrId(taskDto.getRefNo()).getEntity();
        specificApptDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        Map<String, String> corrIdServiceIdMap = getServiceIdsByCorrIdsFromPremises(premCorrIds);
        List<String> serviceIds = IaisCommonUtils.genNewArrayList();
        for (Map.Entry<String, String> mapDate : corrIdServiceIdMap.entrySet()) {
            if(!StringUtil.isEmpty(mapDate.getValue())){
                serviceIds.add(mapDate.getValue());
            }
        }
        //get Start date and End date when group no date
        if (specificApptDto.getStartDate() == null && specificApptDto.getEndDate() == null) {
            specificApptDto.setServiceIds(serviceIds);
            specificApptDto = hcsaConfigClient.getApptStartEndDateByService(specificApptDto).getEntity();
        }
        //set data to validate
        List<AppointmentUserDto> appointmentUserDtos = IaisCommonUtils.genNewArrayList();
        for (TaskDto tDto : taskDtoList) {
            AppointmentUserDto appointmentUserDto = new AppointmentUserDto();
            OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(tDto.getUserId()).getEntity();
            appointmentUserDto.setLoginUserId(orgUserDto.getUserId());
            String workGroupId = tDto.getWkGrpId();
            WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(workGroupId).getEntity();
            appointmentUserDto.setWorkGrpName(workingGroupDto.getGroupName());
            //get service id by task refno
            String serviceId = corrIdServiceIdMap.get(tDto.getRefNo());
            //get manHours by service and stage
            ApptAppInfoShowDto apptAppInfoShowDto = new ApptAppInfoShowDto();
            apptAppInfoShowDto.setApplicationType(appType);
            apptAppInfoShowDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
            apptAppInfoShowDto.setServiceId(serviceId);
            int manHours = getServiceManHours(tDto.getRefNo(), apptAppInfoShowDto);
            //Divide the time according to the number of people
            List<TaskDto> sizeTask = organizationClient.getCurrTaskByRefNo(tDto.getRefNo()).getEntity();
            double hours = manHours;
            double peopleCount = sizeTask.size();
            int peopleHours = (int) Math.ceil(hours/peopleCount);
            appointmentUserDto.setWorkHours(peopleHours);
            appointmentUserDtos.add(appointmentUserDto);
        }
        specificApptDto.setUsers(appointmentUserDtos);
        apptInspectionDateDto.setSpecificApptDto(specificApptDto);
        return apptInspectionDateDto;
    }

    private int getServiceManHours(String refNo, ApptAppInfoShowDto apptAppInfoShowDto) {
        int manHours;
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(refNo, InspectionConstants.RECOM_TYPE_INSP_MAN_HOUR).getEntity();
        if(appPremisesRecommendationDto != null){
            String hours = appPremisesRecommendationDto.getRecomDecision();
            if(!StringUtil.isEmpty(hours)){
                manHours = Integer.parseInt(hours);
            } else {
                manHours = hcsaConfigClient.getManHour(apptAppInfoShowDto).getEntity();
            }
        } else {
            manHours = hcsaConfigClient.getManHour(apptAppInfoShowDto).getEntity();
        }
        return manHours;
    }

    private Map<String, String> getServiceIdsByCorrIdsFromPremises(List<String> premCorrIds) {
        Map<String, String> serviceIds = applicationClient.getServiceIdsByCorrIdsFromPremises(premCorrIds).getEntity();
        return serviceIds;
    }

    @Override
    public void saveSpecificDateLast(ApptInspectionDateDto apptInspectionDateDto, LoginContext loginContext) {
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtos = apptInspectionDateDto.getAppPremisesInspecApptDtos();
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        AppPremisesInspecApptDto appPremInspApptDto1 = new AppPremisesInspecApptDto();
        List<String> appPremCorrIds = apptInspectionDateDto.getRefNo();
        List<TaskDto> taskDtoList = apptInspectionDateDto.getTaskDtos();
        updateTaskDtoList(taskDtoList);
        List<TaskDto> taskDtos = IaisCommonUtils.genNewArrayList();
        //get appPremCorrId with Score
        Map<String, Integer> appPremScoreMap = getAppPremTaskScore(taskDtoList);
        for (TaskDto taskDto2 : taskDtoList) {
            int score = appPremScoreMap.get(taskDto2.getRefNo());
            TaskDto tDto = createTaskDto(taskDto2, taskDto2.getUserId(), score);
            taskDtos.add(tDto);
        }
        taskService.createTasks(taskDtos);
        if (apptInspectionDateDto.getProcessDec().equals(InspectionConstants.PROCESS_DECI_ASSIGN_SPECIFIC_DATE)) {
            List<AppPremisesInspecApptDto> appPremisesInspecApptDtoCreateList = IaisCommonUtils.genNewArrayList();
            List<AppPremisesInspecApptDto> appPremisesInspecApptDtoUpdateList = IaisCommonUtils.genNewArrayList();
            //end hour - 1, because the function save all start hour
            AppointmentDto appointmentDtoSave = officersReSchedulingService.subtractEndHourByApptDto(apptInspectionDateDto.getSpecificApptDto());
            String apptRefNo = appointmentClient.saveManualUserCalendar(appointmentDtoSave).getEntity();
            //remove inactive inspection date
            if (!IaisCommonUtils.isEmpty(appPremisesInspecApptDtos)) {
                appPremInspApptDto1 = appPremisesInspecApptDtos.get(0);
                for (AppPremisesInspecApptDto appPremisesInspecApptDto : appPremisesInspecApptDtos) {
                    appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    appPremisesInspecApptDto.setAuditTrailDto(auditTrailDto);
                    appPremisesInspecApptDto = applicationClient.updateAppPremisesInspecApptDto(appPremisesInspecApptDto).getEntity();
                    appPremisesInspecApptDto.setAuditTrailDto(auditTrailDto);
                    appPremisesInspecApptDtoUpdateList.add(appPremisesInspecApptDto);
                }
            }
            //create inspection date
            if (!IaisCommonUtils.isEmpty(appPremCorrIds)) {
                for (String appPremCorrId : appPremCorrIds) {
                    AppPremisesInspecApptDto appPremInspApptDto = new AppPremisesInspecApptDto();
                    appPremInspApptDto.setAppCorrId(appPremCorrId);
                    appPremInspApptDto.setApptRefNo(apptRefNo);
                    appPremInspApptDto.setSpecificInspDate(null);
                    appPremInspApptDto.setId(null);
                    appPremInspApptDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    if(appPremInspApptDto1 != null){
                        appPremInspApptDto1.setAuditTrailDto(auditTrailDto);
                        if(appPremInspApptDto1.getStartDate() != null) {
                            appPremInspApptDto.setStartDate(appPremInspApptDto1.getStartDate());
                        }
                        if(appPremInspApptDto1.getEndDate() != null) {
                            appPremInspApptDto.setEndDate(appPremInspApptDto1.getEndDate());
                        }
                    }
                    appPremInspApptDto.setAuditTrailDto(auditTrailDto);
                    appPremisesInspecApptDtoCreateList.add(appPremInspApptDto);
                }
            }
            appPremisesInspecApptDtoCreateList = applicationClient.createAppPremisesInspecApptDto(appPremisesInspecApptDtoCreateList).getEntity();
            for (AppPremisesInspecApptDto apptDto : appPremisesInspecApptDtoCreateList) {
                apptDto.setAuditTrailDto(auditTrailDto);
            }
            //synchronize FE
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            //run eic api
            ApptFeConfirmDateDto apptFeConfirmDateDto = new ApptFeConfirmDateDto();
            apptFeConfirmDateDto.setAppPremisesInspecApptCreateList(appPremisesInspecApptDtoCreateList);
            apptFeConfirmDateDto.setAppPremisesInspecApptUpdateList(appPremisesInspecApptDtoUpdateList);
            apptFeConfirmDateDto.setAppPremisesInspecApptDto(appPremInspApptDto1);
            //save eic record
            EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApptInspectionDateServiceImpl", "saveSpecificDateLast",
                    "hcsa-licence-web-intranet", ApptFeConfirmDateDto.class.getName(), JsonUtil.parseToJson(apptFeConfirmDateDto));
            String eicRefNo = eicRequestTrackingDto.getRefNo();
            beEicGatewayClient.reSchedulingSaveFeDate(apptFeConfirmDateDto, signature.date(), signature.authorization(),
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
        Date saveDate = apptInspectionDateDto.getSpecificStartDate();
        //save Inspection date / status, save Application
        saveRoutingData(appPremCorrIds, taskDtoList, saveDate, apptInspectionDateDto);
    }

    private void saveRoutingData(List<String> appPremCorrIds, List<TaskDto> taskDtoList, Date saveDate, ApptInspectionDateDto apptInspectionDateDto) {
        for(String appPremCorrId : appPremCorrIds){
            TaskDto taskDto = null;
            for(TaskDto tDto : taskDtoList){
                if(appPremCorrId.equals(tDto.getRefNo())){
                    taskDto = tDto;
                    break;
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
        Map<String, Integer> appPremScoreMap = IaisCommonUtils.genNewHashMap();
        List<String> appPremCorrIds = IaisCommonUtils.genNewArrayList();
        for(TaskDto taskDto : taskDtoList){
            appPremCorrIds.add(taskDto.getRefNo());
        }
        Set<String> appPremCorrIdSet = new HashSet<>(appPremCorrIds);
        appPremCorrIds = new ArrayList<>(appPremCorrIdSet);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
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
        tDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION);
        tDto.setScore(score);
        tDto.setApplicationNo(taskDto.getApplicationNo());
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

    private void createFeAppPremisesInspecApptDto(ApptInspectionDateDto apptInspectionDateDto) {
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = apptInspectionDateDto.getAppPremisesInspecApptCreateList();
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        //save eic record
        EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApptInspectionDateServiceImpl", "createFeAppPremisesInspecApptDto",
                "hcsa-licence-web-intranet", AppPremisesInspecApptDto.class.getName(), JsonUtil.parseToJson(apptInspectionDateDto));
        String eicRefNo = eicRequestTrackingDto.getRefNo();
        beEicGatewayClient.createAppPremisesInspecApptDto(appPremisesInspecApptDtoList, signature.date(), signature.authorization(),
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

    private void updateStatusAndCreateHistory(List<TaskDto> taskDtos, String inspecStatus, String processDec) {
        for(TaskDto taskDto : taskDtos) {
            String refNo = taskDto.getRefNo();
            ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppCorrId(refNo);
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING);
            applicationService.updateFEApplicaiton(applicationDto1);
            updateInspectionStatus(refNo, inspecStatus);
            inspectionAssignTaskService.createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), null, processDec, taskDto.getRoleId(), HcsaConsts.ROUTING_STAGE_PRE, taskDto.getWkGrpId());
            inspectionAssignTaskService.createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(), applicationDto1.getStatus(), taskDto.getTaskKey(), null, null, null, null, taskDto.getWkGrpId());
        }
        updateTaskDtoList(taskDtos);
    }

    private void updateInspectionStatus(String appPremCorrId, String status) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorrId).getEntity();
        appInspectionStatusDto.setStatus(status);
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appInspectionStatusClient.update(appInspectionStatusDto);
    }

    private void inspectionDateSendEmail(String url, String licenseeId, ApplicationViewDto applicationViewDto, HcsaServiceDto hcsaServiceDto, String appPremCorrId) {
        try{
            LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
            String licName = licenseeDto.getName();
            ApplicationGroupDto applicationGroupDto = applicationViewDto.getApplicationGroupDto();
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            String appNo = applicationDto.getApplicationNo();
            String appType = applicationDto.getApplicationType();
            Date submitDt = applicationGroupDto.getSubmitDt();
            if(submitDt == null){
                submitDt = new Date();
            }
            Date today = new Date();
            String strSubmitDt = Formatter.formatDateTime(submitDt, "dd/MM/yyyy HH:mm:ss");
            String todayDate = Formatter.formatDateTime(today, "dd/MM/yyyy");
            String todayTime = Formatter.formatDateTime(today, "HH:mm:ss");
            AppGrpPremisesDto appGrpPremisesDto = inspectionAssignTaskService.getAppGrpPremisesDtoByAppGroId(appPremCorrId);
            String address = inspectionAssignTaskService.getAddress(appGrpPremisesDto);
            String hciName = appGrpPremisesDto.getHciName();
            String hciCode = appGrpPremisesDto.getHciCode();
            if(StringUtil.isEmpty(hciName)){
                hciName = "-";
            }
            //todo
            String address1 = "";
            String address2 = "";
            String phoneNo = "";
            Map<String, Object> map = IaisCommonUtils.genNewHashMap();
            map.put("applicant", licName);
            map.put("applicationType", appType);
            map.put("applicationNo", appNo);
            map.put("submitDate", strSubmitDt);
            map.put("hciName", hciName);
            map.put("hciCode", hciCode);
            map.put("hciAddress", address);
            map.put("today", todayDate);
            map.put("time", todayTime);
            map.put("systemLink", url);
            map.put("emailAddressOne", address1);
            map.put("emailAddressTwo", address2);
            map.put("phoneNo", phoneNo);
            notificationHelper.sendNotification(MsgTemplateConstants.MSG_TEMPLATE_APPT_INSPECTION_DATE_FIRST, map, appNo, appNo,
                    NotificationHelper.RECEIPT_TYPE_LICENCE_ID, licenseeId);
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    private void createMessage(String url, String serviceCode, Date submitDt, String licenseeId, HashMap<String, String> maskParams) {
        MsgTemplateDto mtd = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APPT_INSPECTION_DATE_FIRST).getEntity();
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        String strSubmitDt = Formatter.formatDateTime(submitDt, "dd/MM/yyyy");
        map.put("submitDt", StringUtil.viewHtml(strSubmitDt));
        map.put("process_url", StringUtil.viewHtml(url));
        String templateMessageByContent;
        try {
            templateMessageByContent = MsgUtil.getTemplateMessageByContent(mtd.getMessageContent(), map);
        }catch (IOException | TemplateException e) {
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException(e);
        }
        InterMessageDto interMessageDto = new InterMessageDto();
        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
        interMessageDto.setSubject(MessageConstants.MESSAGE_SUBJECT_APPT_INSPECTION_DATE);
        interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED);
        String mesNO = inboxMsgService.getMessageNo();
        interMessageDto.setRefNo(mesNO);
        interMessageDto.setService_id(serviceCode+"@");
        interMessageDto.setUserId(licenseeId);
        interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        interMessageDto.setMsgContent(templateMessageByContent);
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        interMessageDto.setMaskParams(maskParams);
        inboxMsgService.saveInterMessage(interMessageDto);
    }

    private ApplicationDto updateApplication(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationViewService.updateApplicaiton(applicationDto);
    }
}