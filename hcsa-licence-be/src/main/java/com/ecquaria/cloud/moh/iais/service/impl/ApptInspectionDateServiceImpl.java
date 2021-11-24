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
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppPremInspApptDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptCalendarStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.ApptInspectionDateService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
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
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
    private SystemParamConfig systemParamConfig;

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
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    @Autowired
    private InspectionService inspectionService;

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
        List<String> appPremCorrShowIds;
        List<TaskDto> taskDtoList;
        Map<String, ApplicationDto> corrAppMap = IaisCommonUtils.genNewHashMap();
        Map<String, ApplicationDto> corrAppShowMap = IaisCommonUtils.genNewHashMap();
        if(!fastFlag) {
            //get all application info from same premises
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = getAppPremisesCorrelationsByPremises(taskDto.getRefNo());
            log.info(StringUtil.changeForLog("AppPremisesCorrelationDto Size = " + appPremisesCorrelationDtos.size()));
            //get show App Premises Ids
            corrAppShowMap = filterCancelAppByCorrShow(appPremisesCorrelationDtos);
            log.info(StringUtil.changeForLog("corrAppShowMap Size = " + corrAppShowMap.size()));
            appPremCorrShowIds = getCorrIdsByCorrIdFromPremises(corrAppShowMap);
            log.info(StringUtil.changeForLog("appPremCorrShowIds Size = " + appPremCorrShowIds.size()));
            //filter cancel application
            corrAppMap = filterCancelAppByCorr(appPremisesCorrelationDtos, applicationDto.getStatus());
            log.info(StringUtil.changeForLog("corrAppMap Size = " + corrAppMap.size()));
            premCorrIds = getCorrIdsByCorrIdFromPremises(corrAppMap);
            log.info(StringUtil.changeForLog("premCorrIds Size = " + premCorrIds.size()));
            apptInspectionDateDto.setRefNo(premCorrIds);
            apptInspectionDateDto.setRefShowNo(appPremCorrShowIds);
            //get Other Tasks From The Same Premises
            taskDtoList = getAllTaskFromSamePremises(premCorrIds);
            //The All Tasks is go to inspection or (some of them jump over Inspection and some of them go to inspection)
            actionButtonFlag = getActionButtonFlag(apptInspectionDateDto, applicationDto);
        } else {
            actionButtonFlag = AppConsts.SUCCESS;
            premCorrIds = IaisCommonUtils.genNewArrayList();
            premCorrIds.add(taskDto.getRefNo());
            appPremCorrShowIds = IaisCommonUtils.genNewArrayList();
            appPremCorrShowIds.add(taskDto.getRefNo());
            corrAppMap.put(taskDto.getRefNo(), applicationDto);
            corrAppShowMap.put(taskDto.getRefNo(), applicationDto);
            apptInspectionDateDto.setRefNo(premCorrIds);
            apptInspectionDateDto.setRefShowNo(appPremCorrShowIds);
            taskDtoList = IaisCommonUtils.genNewArrayList();
            List<TaskDto> taskDtos = organizationClient.getTasksByRefNo(taskDto.getRefNo()).getEntity();
            for(TaskDto tDto : taskDtos){
                if(tDto.getTaskStatus().equals(TaskConsts.TASK_STATUS_PENDING) || tDto.getTaskStatus().equals(TaskConsts.TASK_STATUS_READ)) {
                    taskDtoList.add(tDto);
                }
            }
        }
        //get application info show
        List<ApptAppInfoShowDto> applicationInfoShow = getApplicationInfoToShow(appPremCorrShowIds, taskDtoList, corrAppShowMap);
        apptInspectionDateDto.setCorrAppMap(corrAppMap);
        apptInspectionDateDto.setApplicationInfoShow(applicationInfoShow);
        apptInspectionDateDto.setActionButtonFlag(actionButtonFlag);
        apptInspectionDateDto.setTaskDto(taskDto);
        apptInspectionDateDto.setTaskDtos(taskDtoList);

        return apptInspectionDateDto;
    }

    private Map<String, ApplicationDto> filterCancelAppByCorrShow(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos) {
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

    private Map<String, ApplicationDto> filterCancelAppByCorr(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos, String appStatus) {
        Map<String, ApplicationDto> map = IaisCommonUtils.genNewHashMap();
        if(!IaisCommonUtils.isEmpty(appPremisesCorrelationDtos)){
            for(int i = 0; i < appPremisesCorrelationDtos.size(); i++){
                String applicationId = appPremisesCorrelationDtos.get(i).getApplicationId();
                ApplicationDto applicationDto = applicationClient.getApplicationById(applicationId).getEntity();
                if(ApplicationConsts.APPLICATION_STATUS_CREATE_AUDIT_TASK_CANCELED.equals(applicationDto.getStatus())){
                    appPremisesCorrelationDtos.remove(i);
                    i--;
                } else {
                    if(appStatus.equals(applicationDto.getStatus())) {
                        String appPremCorrId = appPremisesCorrelationDtos.get(i).getId();
                        map.put(appPremCorrId, applicationDto);
                    }
                }
            }
        }
        return map;
    }

    @Override
    public List<AppPremInspApptDraftDto> getInspApptDraftBySamePremises(ApptInspectionDateDto apptInspectionDateDto) {
        String actionButtonFlag = apptInspectionDateDto.getActionButtonFlag();
        if(AppConsts.SUCCESS.equals(actionButtonFlag)) {
            Map<String, ApplicationDto> corrAppMap = apptInspectionDateDto.getCorrAppMap();
            List<String> premCorrIds = apptInspectionDateDto.getRefNo();
            List<AppPremInspApptDraftDto> appPremInspApptDraftDtoList = IaisCommonUtils.genNewArrayList();
            if(!IaisCommonUtils.isEmpty(premCorrIds) && corrAppMap != null) {
                for (String appPremCorrId : premCorrIds) {
                    ApplicationDto applicationDto = corrAppMap.get(appPremCorrId);
                    String appNo = applicationDto.getApplicationNo();
                    List<AppPremInspApptDraftDto> appPremInspApptDraftDtos = inspectionTaskClient.getInspApptDraftListByAppNo(appNo).getEntity();
                    if(!IaisCommonUtils.isEmpty(appPremInspApptDraftDtos)) {
                        appPremInspApptDraftDtoList.addAll(appPremInspApptDraftDtos);
                    }
                }
                return appPremInspApptDraftDtoList;
            }
        }
        return null;
    }

    @Override
    public String getTcuAuditAnnouncedFlag(String appPremCorrId) {
        String tcuAudit = AppConsts.FALSE;
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrId, InspectionConstants.PROCESS_DECI_TCU_AUDIT).getEntity();
        if(appPremisesRecommendationDto != null) {
            tcuAudit = AppConsts.TRUE;
        }
        return tcuAudit;
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
            if (!IaisCommonUtils.isEmpty(apptInspectionDateDto.getRefShowNo())) {
                for (String appPremCorrId : apptInspectionDateDto.getRefShowNo()) {
                    List<TaskDto> taskDtos = organizationClient.getCurrTaskByRefNo(appPremCorrId).getEntity();
                    if(IaisCommonUtils.isEmpty(taskDtos)){
                        //the task wait other app or wait batch job
                        ApplicationDto appDto = inspectionTaskClient.getApplicationByCorreId(appPremCorrId).getEntity();
                        String appStatus;
                        if(ApplicationConsts.APPLICATION_STATUS_DELETED.equals(applicationDto.getStatus())){
                            ApplicationDto maxVerAppDto = applicationClient.getAppByNo(appDto.getApplicationNo()).getEntity();
                            appStatus = maxVerAppDto.getStatus();
                        } else {
                            appStatus = appDto.getStatus();
                        }
                        List<String> beforeStatus = TaskUtil.getStatusBeforeIns();
                        List<String> afterStatus = TaskUtil.getStatusAfterIns();
                        taskDtos = getTasksByStatus(appStatus, beforeStatus, afterStatus);
                    }
                    actionButtonFlag = getActionButtonFlagByTasks(taskDtos);
                    if (AppConsts.FAIL.equals(actionButtonFlag)) {
                        return actionButtonFlag;
                    }
                }
            }
        }
        return actionButtonFlag;
    }

    private List<TaskDto> getTasksByStatus(String appStatus, List<String> beforeStatus, List<String> afterStatus) {
        List<TaskDto> taskDtos = IaisCommonUtils.genNewArrayList();
        TaskDto taskDto = new TaskDto();
        if(beforeStatus.contains(appStatus)){
            taskDto.setRoleId(RoleConsts.USER_ROLE_ASO);
        } else if(afterStatus.contains(appStatus)){
            taskDto.setRoleId(RoleConsts.USER_ROLE_AO3);
        }
        taskDtos.add(taskDto);
        return taskDtos;
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
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        for(TaskDto taskDto1 : taskDtos) {
            String appPremCorrId = taskDto1.getRefNo();
            //add application
            ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(appPremCorrId).getEntity();
            //save
            AppPremisesInspecApptDto appPremisesInspecApptDto = new AppPremisesInspecApptDto();
            appPremisesInspecApptDto.setAppCorrId(appPremCorrId);
            appPremisesInspecApptDto.setApptRefNo(apptRefNo);
            appPremisesInspecApptDto.setSpecificInspDate(apptInspectionDateDto.getSpecificDate());
            appPremisesInspecApptDto.setId(null);
            appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremisesInspecApptDto.setAuditTrailDto(auditTrailDto);
            appPremisesInspecApptDtoList.add(appPremisesInspecApptDto);
            AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrId, InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
            //save Inspection date
            createOrUpdateRecommendation(appPremisesRecommendationDto, appPremCorrId, saveDate);
            //update Inspection status
            updateInspectionStatus(appPremCorrId, InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
            //Application data
            ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);

            if (taskDto != null) {
                inspectionAssignTaskService.createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), null, apptInspectionDateDto.getProcessDec(), taskDto.getRoleId(), HcsaConsts.ROUTING_STAGE_PRE, taskDto.getWkGrpId());
                inspectionAssignTaskService.createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(), applicationDto1.getStatus(), taskDto.getTaskKey(), null, null, taskDto.getRoleId(), HcsaConsts.ROUTING_STAGE_PRE, taskDto.getWkGrpId());
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
    public String getAppSubmitByWithLicId(String originLicenceId) {
        if(!StringUtil.isEmpty(originLicenceId)) {
            List<String> appIds = hcsaLicenceClient.getAppIdsByLicId(originLicenceId).getEntity();
            if(!IaisCommonUtils.isEmpty(appIds)) {
                String appId = appIds.get(appIds.size() - 1);
                if(!StringUtil.isEmpty(appId)) {
                    ApplicationDto applicationDto = applicationClient.getApplicationById(appId).getEntity();
                    if(applicationDto != null) {
                        ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();
                        if(applicationGroupDto != null) {
                            return applicationGroupDto.getSubmitBy();
                        }
                    }
                }
            }
        }
        return null;
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

    @Override
    public ApptInspectionDateDto cancelSystemDateBySpecStep(ApptInspectionDateDto apptInspectionDateDto) {
        Map<String, List<ApptUserCalendarDto>> inspectionDateMap = apptInspectionDateDto.getInspectionDateMap();
        List<String> cancelRefNo = IaisCommonUtils.genNewArrayList();
        if(inspectionDateMap != null) {
            for (Map.Entry<String, List<ApptUserCalendarDto>> inspDateMap : inspectionDateMap.entrySet()) {
                String refNo = inspDateMap.getKey();
                cancelRefNo.add(refNo);
            }
        }
        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        apptCalendarStatusDto.setCancelRefNums(cancelRefNo);
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        cancelOrConfirmApptDate(apptCalendarStatusDto);
        apptInspectionDateDto.setInspectionDateMap(null);
        apptInspectionDateDto.setInspectionDate(null);
        inspectionTaskClient.deleteInspDateDraftByApptRefNo(cancelRefNo);
        return apptInspectionDateDto;
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
                    //filter common pool
                    if(!StringUtil.isEmpty(taskDto.getUserId())) {
                        insp = insp + 1;
                    }
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
        //end hour - 1, because the function save all start hour
        //AppointmentDto appointmentDtoSave = officersReSchedulingService.subtractEndHourByApptDto(apptInspectionDateDto.getSpecificApptDto());
        //get AppointmentDto
        AppointmentDto appointmentDtoSave = apptInspectionDateDto.getSpecificApptDto();
        //save and return apptRefNo
        List<String> confirmRefNo = IaisCommonUtils.genNewArrayList();
        String apptRefNo = appointmentClient.saveManualUserCalendar(appointmentDtoSave).getEntity();
        confirmRefNo.add(apptRefNo);
        //generate appList for email
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        //save data
        for(String appPremCorrId : appPremCorrIds) {
            ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(appPremCorrId).getEntity();
            applicationDtos.add(applicationDto);
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
        Map<String, List<ApptUserCalendarDto>> inspectionDateMap = apptInspectionDateDto.getInspectionDateMap();
        List<String> cancelRefNo = IaisCommonUtils.genNewArrayList();
        if(inspectionDateMap != null) {
            for (Map.Entry<String, List<ApptUserCalendarDto>> inspDateMap : inspectionDateMap.entrySet()) {
                String refNo = inspDateMap.getKey();
                cancelRefNo.add(refNo);
            }
        }
        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        Date inspDate;
        try {
            inspDate = Formatter.parseDateTime(apptInspectionDateDto.getSpecificApptDto().getStartDate(), AppConsts.DEFAULT_DATE_TIME_FORMAT);
        } catch (ParseException e) {
            inspDate = new Date();
            log.error("Error when insp Date ==>", e);
        }
        apptCalendarStatusDto.setConfirmRefNums(confirmRefNo);
        apptCalendarStatusDto.setCancelRefNums(cancelRefNo);
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        cancelOrConfirmApptDate(apptCalendarStatusDto);
        //cancel draft
        if(!IaisCommonUtils.isEmpty(cancelRefNo)) {
            inspectionTaskClient.deleteInspDateDraftByApptRefNo(cancelRefNo);
        }
        //url
        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        //get submit user Id
        ApplicationDto appDto = applicationViewDto.getApplicationDto();
        String applicantId;
        if(!ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(appDto.getApplicationType())) {
            applicantId = applicationViewDto.getApplicationGroupDto().getSubmitBy();
        } else {
            applicantId = getAppSubmitByWithLicId(appDto.getOriginLicenceId());
        }
        //send email
        Map<String, Object> map = inspectionDateSendEmail(inspDate, loginUrl, applicantId, applicationViewDto, urlId, applicationDtos);
        //send msg
        String applicationNo = apptInspectionDateDto.getTaskDto().getApplicationNo();
        createMessage(loginUrl, applicationNo, map, applicationDtos);
        //update app Info and insp Info
        updateStatusAndCreateHistory(apptInspectionDateDto.getTaskDtos(), apptInspectionDateDto.getTaskDto(), inspDate, apptInspectionDateDto);
    }

    private void cancelOrConfirmApptDate(ApptCalendarStatusDto apptCalendarStatusDto) {
        appointmentClient.updateUserCalendarStatus(apptCalendarStatusDto);
    }

    @Override
    public void saveSystemInspectionDate(ApptInspectionDateDto apptInspectionDateDto, ApplicationViewDto applicationViewDto) {
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = IaisCommonUtils.genNewArrayList();
        List<String> appPremCorrIds = apptInspectionDateDto.getRefNo();
        Map<String, List<ApptUserCalendarDto>> inspectionDateMap = apptInspectionDateDto.getInspectionDateMap();
        //save AppPremisesInspecApptDto
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        for(String appPremCorrId : appPremCorrIds) {
            ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(appPremCorrId).getEntity();
            applicationDtos.add(applicationDto);
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
        Date inspDate = new Date();
        if(inspectionDateMap != null) {
            for (Map.Entry<String, List<ApptUserCalendarDto>> inspDateMap : inspectionDateMap.entrySet()) {
                String refNo = inspDateMap.getKey();
                confirmRefNo.add(refNo);
            }
            for (Map.Entry<String, List<ApptUserCalendarDto>> inspDateMap : inspectionDateMap.entrySet()) {
                List<ApptUserCalendarDto> apptUserCalendarDtos = inspDateMap.getValue();
                if(apptUserCalendarDtos.get(0).getStartSlot().get(0) != null){
                    inspDate = apptUserCalendarDtos.get(0).getStartSlot().get(0);
                    break;
                }
            }
        }
        apptCalendarStatusDto.setConfirmRefNums(confirmRefNo);
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        cancelOrConfirmApptDate(apptCalendarStatusDto);
        //cancel draft
        inspectionTaskClient.deleteInspDateDraftByApptRefNo(confirmRefNo);
        //send message and email
        String urlId = apptInspectionDateDto.getTaskDto().getRefNo();
        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        //get submit user Id
        ApplicationDto appDto = applicationViewDto.getApplicationDto();
        String applicantId;
        if(!ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(appDto.getApplicationType())) {
            applicantId = applicationViewDto.getApplicationGroupDto().getSubmitBy();
        } else {
            applicantId = getAppSubmitByWithLicId(appDto.getOriginLicenceId());
        }
        //send email
        Map<String, Object> map = inspectionDateSendEmail(inspDate, loginUrl, applicantId, applicationViewDto, urlId, applicationDtos);
        //send msg
        String applicationNo = apptInspectionDateDto.getTaskDto().getApplicationNo();
        createMessage(loginUrl, applicationNo, map, applicationDtos);
        //save data to app table
        updateStatusAndCreateHistory(apptInspectionDateDto.getTaskDtos(), apptInspectionDateDto.getTaskDto(), inspDate, apptInspectionDateDto);
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
        String appStatus = applicationDto.getStatus();
        //get All app from the same Premises
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = getAppPremisesCorrelationsByPremises(taskDto.getRefNo());
        //filter cancel application
        Map<String, ApplicationDto> corrAppMap = filterCancelAppByCorr(appPremisesCorrelationDtos, appStatus);
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
        if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
            Map<String, Date> svcIdLicDtMap = IaisCommonUtils.genNewHashMap();
            for (Map.Entry<String, String> mapDate : corrIdServiceIdMap.entrySet()) {
                if(!StringUtil.isEmpty(mapDate.getValue())){
                    svcIdLicDtMap = setSvcIdLicDtMapByApp(mapDate.getKey(), mapDate.getValue(), svcIdLicDtMap);
                    serviceIds.add(mapDate.getValue());
                }
            }
            specificApptDto.setSvcIdLicDtMap(svcIdLicDtMap);
        } else {
            for (Map.Entry<String, String> mapDate : corrIdServiceIdMap.entrySet()) {
                if(!StringUtil.isEmpty(mapDate.getValue())){
                    serviceIds.add(mapDate.getValue());
                }
            }
            specificApptDto.setSvcIdLicDtMap(null);
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

    private Map<String, Date> setSvcIdLicDtMapByApp(String appPremCorrId, String serviceId, Map<String, Date> svcIdLicDtMap) {
        ApplicationDto appDto = inspectionTaskClient.getApplicationByCorreId(appPremCorrId).getEntity();
        String orgLicId = appDto.getOriginLicenceId();
        if(!StringUtil.isEmpty(orgLicId)) {
            LicenceDto licDto = hcsaLicenceClient.getLicDtoById(orgLicId).getEntity();
            if(licDto != null && licDto.getExpiryDate() != null){
                svcIdLicDtMap.put(serviceId, licDto.getExpiryDate());
            }
        }
        return svcIdLicDtMap;
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
                    //get Max Rescheduling Version
                    Integer integer = applicationClient.getMaxReschedulingVersion(appPremCorrId).getEntity();
                    if(integer == null) {
                        appPremInspApptDto.setReschedulingCount(0);
                    } else {
                        appPremInspApptDto.setReschedulingCount(integer);
                    }
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
                inspectionAssignTaskService.createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(), applicationDto1.getStatus(), taskDto.getTaskKey(), null, null, taskDto.getRoleId(), HcsaConsts.ROUTING_STAGE_PRE, taskDto.getWkGrpId());
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
        for(String appPremCorrId : appPremCorrIds){
            ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(appPremCorrId).getEntity();
            List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
            applicationDtos.add(applicationDto);
            List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = inspectionAssignTaskService.generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_INS);
            hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
            appPremScoreMap.put(appPremCorrId, hcsaSvcStageWorkingGroupDtos.get(0).getCount());
        }

        return appPremScoreMap;
    }

    public List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId){
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos start ...."));
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos stageId -->:"+stageId));
        for(ApplicationDto applicationDto : applicationDtos){
            AppGrpPremisesEntityDto appGrpPremisesEntityDto = applicationClient.getPremisesByAppNo(applicationDto.getApplicationNo()).getEntity();
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            if(appGrpPremisesEntityDto != null){
                hcsaSvcStageWorkingGroupDto.setPremiseType(appGrpPremisesEntityDto.getPremisesType());
            }else{
                log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos this APP do not have the premise :"+applicationDto.getApplicationNo()));
            }
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos end ...."));
        return hcsaSvcStageWorkingGroupDtos;
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

    @Override
    public void createFeAppPremisesInspecApptDto(ApptInspectionDateDto apptInspectionDateDto) {
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

    private void updateStatusAndCreateHistory(List<TaskDto> taskDtos, TaskDto taskDto, Date saveDate, ApptInspectionDateDto apptInspectionDateDto) {
        for(TaskDto taskDto1 : taskDtos) {
            String appPremCorrId = taskDto1.getRefNo();
            AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrId, InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
            //save Inspection date
            createOrUpdateRecommendation(appPremisesRecommendationDto, appPremCorrId, saveDate);
            //update Inspection status
            updateInspectionStatus(appPremCorrId, InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
            //Application data
            ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(appPremCorrId).getEntity();
            ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
            applicationService.updateFEApplicaiton(applicationDto1);
            if (taskDto != null) {
                inspectionAssignTaskService.createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), null, apptInspectionDateDto.getProcessDec(), taskDto.getRoleId(), HcsaConsts.ROUTING_STAGE_PRE, taskDto.getWkGrpId());
                inspectionAssignTaskService.createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(), applicationDto1.getStatus(), taskDto.getTaskKey(), null, null, taskDto.getRoleId(), HcsaConsts.ROUTING_STAGE_PRE, taskDto.getWkGrpId());
            }
        }
        updateTaskDtoList(taskDtos);
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
        for (TaskDto taskDto2 : taskDtos) {
            int score = taskDto2.getScore();
            TaskDto tDto = createTaskDto(taskDto2, taskDto2.getUserId(), score);
            taskDtoList.add(tDto);
        }
        taskService.createTasks(taskDtoList);
    }

    private void updateInspectionStatus(String appPremCorrId, String status) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorrId).getEntity();
        appInspectionStatusDto.setStatus(status);
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appInspectionStatusClient.update(appInspectionStatusDto);
    }

    private Map<String, Object> inspectionDateSendEmail(Date inspecDate, String url, String applicantId, ApplicationViewDto applicationViewDto,
                                                        String appPremCorrId, List<ApplicationDto> applicationDtos) {
        StringBuilder appNoShow = new StringBuilder();
        for(ApplicationDto applicationDto : applicationDtos){
            if(StringUtil.isEmpty(appNoShow.toString())){
                appNoShow.append(applicationDto.getApplicationNo());
            } else {
                appNoShow.append(" and ");
                appNoShow.append(applicationDto.getApplicationNo());
            }
        }
        OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicantId).getEntity();
        String applicantName = orgUserDto.getDisplayName();
        ApplicationGroupDto applicationGroupDto = applicationViewDto.getApplicationGroupDto();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String appNo = applicationDto.getApplicationNo();
        String appType = applicationDto.getApplicationType();
        Date submitDt = applicationGroupDto.getSubmitDt();
        if(submitDt == null){
            submitDt = new Date();
        }
        String strSubmitDt = Formatter.formatDateTime(submitDt, "dd/MM/yyyy");
        String todayDate = Formatter.formatDateTime(inspecDate, "dd/MM/yyyy");
        String todayTime = Formatter.formatDateTime(inspecDate, "HH:mm:ss");
        AppGrpPremisesDto appGrpPremisesDto = getEmailAppGrpPremisesByCorrId(appPremCorrId);
        List<String> appGroupIds = IaisCommonUtils.genNewArrayList();
        appGroupIds.add(applicationGroupDto.getId());
        HcsaTaskAssignDto hcsaTaskAssignDto = inspectionService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
        String address = inspectionAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
        String hciName = appGrpPremisesDto.getHciName();
        String hciCode = appGrpPremisesDto.getHciCode();
        if(StringUtil.isEmpty(hciName)){
            hciName = "";
        }
        String address1 = systemParamConfig.getSystemAddressOne();
        String address2 = systemParamConfig.getSystemAddressTwo();
        String phoneNo = systemParamConfig.getSystemPhoneNumber();
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("applicant", applicantName);
        String appTypeShow = MasterCodeUtil.getCodeDesc(appType);
        map.put("applicationType", appTypeShow);
        map.put("applicationNo", appNoShow.toString());
        map.put("submitDate", strSubmitDt);
        String hciNameCode;
        if(StringUtil.isEmpty(hciName) && StringUtil.isEmpty(hciCode)) {
            hciNameCode = "";
        } else {
            StringBuilder html = new StringBuilder("for");
            html.append(' ');
            if(!StringUtil.isEmpty(hciName)){
                html.append(hciName);
            }
            if(!StringUtil.isEmpty(hciCode)){
                html.append(", ");
                html.append(hciCode);
            }
            hciNameCode = html.toString();
        }
        if(!StringUtil.isEmpty(hciNameCode)) {
            map.put("hciNameCode", hciNameCode);
        }
        map.put("hciAddress", address);
        map.put("today", todayDate);
        map.put("time", todayTime);
        map.put("systemLink", url);
        map.put("emailAddressOne", address1);
        map.put("emailAddressTwo", address2);
        map.put("phoneNo", phoneNo);
        try{
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APPT_INSPECTION_DATE_FIRST_EMAIL);
            emailParam.setTemplateContent(map);
            emailParam.setQueryCode(appNo);
            emailParam.setReqRefNum(appNo);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
            emailParam.setRefId(appNo);
            notificationHelper.sendNotification(emailParam);
            EmailParam smsParam = new EmailParam();
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APPT_INSPECTION_DATE_FIRST_SMS);
            smsParam.setSubject("MOH HALP - Select Inspection Appointment Date");
            smsParam.setQueryCode(appNo);
            smsParam.setReqRefNum(appNo);
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            smsParam.setRefId(appNo);
            notificationHelper.sendNotification(smsParam);
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return map;
    }

    private void createMessage(String url, String appNo, Map<String, Object> map, List<ApplicationDto> applicationDtos) {
        List<String> serviceCodes = IaisCommonUtils.genNewArrayList();
        for(ApplicationDto applicationDto : applicationDtos){
            String serviceId = applicationDto.getServiceId();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
            String serviceCode = hcsaServiceDto.getSvcCode();
            if(!StringUtil.isEmpty(serviceCode)){
                serviceCodes.add(serviceCode);
            }
        }
        map.put("systemLink", url);
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APPT_INSPECTION_DATE_FIRST_MSG);
        emailParam.setTemplateContent(map);
        emailParam.setQueryCode(appNo);
        emailParam.setReqRefNum(appNo);
        emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        emailParam.setRefId(appNo);
        emailParam.setSvcCodeList(serviceCodes);
        notificationHelper.sendNotification(emailParam);
    }

    private ApplicationDto updateApplication(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationViewService.updateApplicaiton(applicationDto);
    }

    public AppGrpPremisesDto getEmailAppGrpPremisesByCorrId(String appCorrId) {
        AppGrpPremisesDto appGrpPremisesDto = inspectionTaskClient.getAppGrpPremisesDtoByAppGroId(appCorrId).getEntity();
        if (StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
            appGrpPremisesDto.setHciName("");
        }
        if (StringUtil.isEmpty(appGrpPremisesDto.getHciCode())) {
            appGrpPremisesDto.setHciCode("");
        }
        setAddressByPremises(appGrpPremisesDto);
        if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())) {
            appGrpPremisesDto.setConveyanceBlockNo(appGrpPremisesDto.getBlkNo());
            appGrpPremisesDto.setConveyanceStreetName(appGrpPremisesDto.getStreetName());
            appGrpPremisesDto.setConveyanceBuildingName(appGrpPremisesDto.getBuildingName());
            appGrpPremisesDto.setConveyanceFloorNo(appGrpPremisesDto.getFloorNo());
            appGrpPremisesDto.setConveyanceUnitNo(appGrpPremisesDto.getUnitNo());
            appGrpPremisesDto.setConveyancePostalCode(appGrpPremisesDto.getPostalCode());
        } else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(appGrpPremisesDto.getPremisesType())) {
            appGrpPremisesDto.setOffSiteBlockNo(appGrpPremisesDto.getBlkNo());
            appGrpPremisesDto.setOffSiteStreetName(appGrpPremisesDto.getStreetName());
            appGrpPremisesDto.setOffSiteBuildingName(appGrpPremisesDto.getBuildingName());
            appGrpPremisesDto.setOffSiteFloorNo(appGrpPremisesDto.getFloorNo());
            appGrpPremisesDto.setOffSiteUnitNo(appGrpPremisesDto.getUnitNo());
            appGrpPremisesDto.setOffSitePostalCode(appGrpPremisesDto.getPostalCode());
        }
        return appGrpPremisesDto;
    }

    private void setAddressByPremises(AppGrpPremisesDto appGrpPremisesDto) {
        if (StringUtil.isEmpty(appGrpPremisesDto.getBlkNo())) {
            appGrpPremisesDto.setBlkNo("");
        }
        if (StringUtil.isEmpty(appGrpPremisesDto.getStreetName())) {
            appGrpPremisesDto.setStreetName("");
        }
        if (StringUtil.isEmpty(appGrpPremisesDto.getBuildingName())) {
            appGrpPremisesDto.setBuildingName("");
        }
        if (StringUtil.isEmpty(appGrpPremisesDto.getFloorNo())) {
            appGrpPremisesDto.setFloorNo("");
        }
        if (StringUtil.isEmpty(appGrpPremisesDto.getUnitNo())) {
            appGrpPremisesDto.setUnitNo("");
        }
        if (StringUtil.isEmpty(appGrpPremisesDto.getPostalCode())) {
            appGrpPremisesDto.setPostalCode("");
        }
    }
}