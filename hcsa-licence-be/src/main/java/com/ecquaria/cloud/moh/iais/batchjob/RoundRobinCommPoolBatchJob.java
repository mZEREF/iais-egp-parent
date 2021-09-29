package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.action.HcsaApplicationDelegator;
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
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremInspCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptCalendarStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.*;
import com.ecquaria.cloud.moh.iais.service.client.AppEicClient;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskOrganizationClient;
import com.ecquaria.cloud.moh.iais.util.WorkDayCalculateUtil;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * RoundRobinCommPoolBatchJob
 *
 * @author suocheng
 * @date 4/9/2020
 */
@Delegator("roundRobinCommPoolBatchJob")
@Slf4j
public class RoundRobinCommPoolBatchJob {
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private InspectionTaskClient inspectionTaskClient;
    @Autowired
    private ApplicationGroupService applicationGroupService;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private LicenseeService licenseeService;

    @Autowired
    private CessationClient cessationClient;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private AppointmentClient appointmentClient;

    @Autowired
    HcsaApplicationDelegator newApplicationDelegator;


    @Autowired
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;

    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Autowired
    private AppEicClient appEicClient;


    @Autowired
    private TaskOrganizationClient taskOrganizationClient;

    @Autowired
    private TaskService taskService;


    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    public void doBatchJob(BaseProcessClass bpc) throws FeignException {
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        jobExecute();
    }

    public void jobExecute(){
        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob start ..."));
        String roundDate = getDate();
        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob roundDate -- >:" +roundDate));
        List<TaskDto> taskDtoList = taskService.getTaskDtoByDate(roundDate,false);

        int rountingBackupDay = systemParamConfig.getRountingBackupDay();
        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob rountingBackupDay -- >:" +rountingBackupDay));
        List<Date> holidays = appointmentClient.getHolidays().getEntity();
        Date rountingDate= WorkDayCalculateUtil.getDate(new Date(),-rountingBackupDay,holidays);
        String rountingDateStr = Formatter.formatDateTime(rountingDate,AppConsts.DEFAULT_DATE_FORMAT);
        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob rountingDateStr -- >:" +rountingDateStr));
        List<TaskDto> routingTaskDtoList = taskService.getTaskDtoByDate(rountingDateStr,true);

        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
        if(!IaisCommonUtils.isEmpty(taskDtoList)){
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob taskDtoList.size() -- >:" +taskDtoList.size()));
            for (TaskDto taskDto : taskDtoList){
                try{
                ApplicationViewDto applicationViewDto=applicationClient.getAppViewByCorrelationId(taskDto.getRefNo()).getEntity();
                ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
                assignTask(taskDto,auditTrailDto,applicationViewDto);
                String workGroupId = taskDto.getWkGrpId();

                if(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(applicationDto.getStatus())) {
                    //set inspector leads
                    setInspLeadsInRecommendation(taskDto, workGroupId, auditTrailDto);
                }
                if(!RoleConsts.USER_ROLE_BROADCAST.equals(taskDto.getRoleId())&&(ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL.equals(applicationViewDto.getApplicationDto().getStatus()) ||
                        ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT.equals(applicationViewDto.getApplicationDto().getStatus()))){

                        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL taskId -- >:" +taskDto.getId()));
                        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL workGroupId -- >:" +workGroupId));
                        TaskDto taskScoreDto = taskService.getUserIdForWorkGroup(workGroupId);

                        //update the application.
                        String taskType = taskDto.getTaskType();
                        String appNo = taskDto.getApplicationNo();
                        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL taskType -- >:" + taskType));
                        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL appNo -- >:" + appNo));

                        if(ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL.equals(applicationViewDto.getApplicationDto().getStatus()) ||
                                ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT.equals(applicationViewDto.getApplicationDto().getStatus())){
                            //set inspector leads
                            setInspLeadsInRecommendation(taskDto, workGroupId, auditTrailDto);
                            List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
                            applicationDtos.add(applicationDto);
                            ApplicationGroupDto applicationGroupDto = applicationViewDto.getApplicationGroupDto();
                            if(taskScoreDto != null && !StringUtil.isEmpty(taskScoreDto.getUserId())) {
                                assignReschedulingTask(taskDto, taskScoreDto.getUserId(), applicationDtos, auditTrailDto, applicationGroupDto);
                            }
                        }
                }
                }catch (Exception e ){
                    log.debug(StringUtil.changeForLog("This  Task can not assign id-->:"+taskDto.getId()));
                    log.error(e.getMessage(),e);
                }
            }
        }else{
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob do  not need roud robin task !!!"));
        }
        //rounting the backup Ao1 Ao2 or Ao3
        if(!IaisCommonUtils.isEmpty(routingTaskDtoList)){
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob routingTaskDtoList.size() -- >:" +routingTaskDtoList.size()));
            for (TaskDto taskDto : routingTaskDtoList){
                try{
                    ApplicationViewDto applicationViewDto=applicationClient.getAppViewByCorrelationId(taskDto.getRefNo()).getEntity();
                    assignTask(taskDto,auditTrailDto,applicationViewDto);
                }catch (Exception e ){
                    log.debug(StringUtil.changeForLog("This  Task can not assign to backup id-->:"+taskDto.getId()));
                    log.error(e.getMessage(),e);
                }
            }
        }else{
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob do  not need roud assign to backup !!!"));
        }
        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob end ..."));
    }
    private void assignTask(TaskDto taskDto,AuditTrailDto auditTrailDto,ApplicationViewDto applicationViewDto) throws CloneNotSupportedException, FeignException {
        if(!RoleConsts.USER_ROLE_BROADCAST.equals(taskDto.getRoleId())&&!(ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL.equals(applicationViewDto.getApplicationDto().getStatus()) ||
                ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT.equals(applicationViewDto.getApplicationDto().getStatus()))){

            //completed the old task
            TaskDto oldTaskDto = (TaskDto) CopyUtil.copyMutableObject(taskDto);
            oldTaskDto.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
            oldTaskDto.setAuditTrailDto(auditTrailDto);
            oldTaskDto = taskService.updateTask(oldTaskDto);
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob update old task status"));

            String workGroupId = taskDto.getWkGrpId();
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob taskId -- >:" +taskDto.getId()));
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob workGroupId -- >:" +workGroupId));
            String oldUserId = taskDto.getUserId();
            TaskDto taskScoreDto = taskService.getUserIdForWorkGroup(workGroupId,oldUserId);
            String userId = null;
            boolean isSystemAdmin = false;
            if(taskScoreDto!= null){
                userId = taskScoreDto.getUserId();
            }
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob userId -- >:" +userId));
            if(StringUtil.isEmpty(userId)){
                //0066643
                List<OrgUserDto> orgUserDtos = taskOrganizationClient.retrieveOrgUserAccountByRoleId(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN).getEntity();
                if(!IaisCommonUtils.isEmpty(orgUserDtos)){
                    userId = orgUserDtos.get(0).getId();
                    isSystemAdmin = true;
                    log.info(StringUtil.changeForLog("The RoundRobinCommPoolBatchJob sendNoteToAdm "));
                    taskService.sendNoteToAdm(taskDto.getApplicationNo(),taskDto.getRefNo(),orgUserDtos.get(0));
                }
            }
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob userId -- >:" +userId));
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob isSystemAdmin -- >:" +isSystemAdmin));
            taskDto.setUserId(userId);
            taskDto.setDateAssigned(new Date());
            taskDto.setAuditTrailDto(auditTrailDto);
            List<TaskDto> taskDtos = IaisCommonUtils.genNewArrayList();
            taskDto.setId(null);
            if(isSystemAdmin){
                taskDto.setWkGrpId(null);
            }
            taskDtos.add(taskDto);
            taskDtos = taskService.createTasks(taskDtos);
            //update the application.
            String taskType = taskDto.getTaskType();
            String appNo = taskDto.getApplicationNo();
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob taskType -- >:" + taskType));
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob appNo -- >:" + appNo));
            String processDec = InspectionConstants.PROCESS_DECI_COMMON_POOL_ASSIGN;
            if(!StringUtil.isEmpty(oldUserId)){
                processDec = InspectionConstants.PROCESS_DECI_SUPER_USER_POOL_ASSIGN;
            }
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob processDec -- >:" + processDec));
            createAppPremisesRoutingHistory(appNo, applicationViewDto.getApplicationDto().getStatus(), taskDto.getTaskKey(), null, processDec, taskDto.getRoleId(), null, workGroupId);

            if(TaskConsts.TASK_TYPE_INSPECTION.equals(taskType)||TaskConsts.TASK_TYPE_MAIN_FLOW.equals(taskType)){
                List<ApplicationDto> applicationDtos = applicationService.getApplicationDtosByApplicationNo(appNo);
                if(!IaisCommonUtils.isEmpty(applicationDtos)){
                    ApplicationDto applicationDto = applicationDtos.get(0);
                    if(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(applicationDto.getStatus())){
                        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob update this applicaiton status to -- >:"
                                + ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING));
                        applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING);
                        applicationDto.setAuditTrailDto(auditTrailDto);
                        applicationDto = applicationService.updateBEApplicaiton(applicationDto);
                        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob BE update success ..."));
                        applicationDto = applicationService.updateFEApplicaiton(applicationDto);
                        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob FE update success ..."));
                    }
                }else{
                    log.debug(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob this appNo can not get the Application"));
                }
            }
        }else{
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob broadcast taskId -- >:" +taskDto.getId()));
        }
    }
    private void setInspLeadsInRecommendation(TaskDto taskDto, String workGroupId, AuditTrailDto auditTrailDto) {
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(taskDto.getRefNo(), InspectionConstants.RECOM_TYPE_INSPECTION_LEAD).getEntity();
        if(appPremisesRecommendationDto == null){
            if(HcsaConsts.ROUTING_STAGE_INS.equals(taskDto.getTaskKey())){
                List<String> leadIds = organizationClient.getInspectionLead(workGroupId).getEntity();
                List<OrgUserDto> orgUserDtos = organizationClient.getUsersByWorkGroupName(workGroupId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
                String nameStr = "";
                for (String id : leadIds) {
                    for (OrgUserDto oDto : orgUserDtos) {
                        if (id.equals(oDto.getId())) {
                            if(StringUtil.isEmpty(nameStr)){
                                nameStr = oDto.getDisplayName();
                            } else {
                                nameStr = nameStr + "," + oDto.getDisplayName();
                            }
                        }
                    }
                }
                appPremisesRecommendationDto = new AppPremisesRecommendationDto();
                appPremisesRecommendationDto.setAppPremCorreId(taskDto.getRefNo());
                appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPremisesRecommendationDto.setVersion(1);
                appPremisesRecommendationDto.setRecomInDate(null);
                appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPECTION_LEAD);
                appPremisesRecommendationDto.setRecomDecision(nameStr);
                appPremisesRecommendationDto.setAuditTrailDto(auditTrailDto);
                fillUpCheckListGetAppClient.saveAppRecom(appPremisesRecommendationDto);
            }
        }
    }

    private String getDate(){
        int day = systemParamConfig.getRoundRobinCpDays();
        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob day -- >:" +day));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR,-day);
        Date result = calendar.getTime();
        String dateStr = Formatter.formatDateTime(result,AppConsts.DEFAULT_DATE_FORMAT);
        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob dateStr -- >:" +dateStr));
        return dateStr;
    }

    private void assignReschedulingTask(TaskDto td,String userId, List<ApplicationDto> applicationDtos, AuditTrailDto auditTrailDto,
                                        ApplicationGroupDto applicationGroupDto) {
        //update
        td.setSlaDateCompleted(new Date());
        td.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
        td.setAuditTrailDto(auditTrailDto);
        ApplicationDto applicationDto = applicationDtos.get(0);
        String appPremCorrId = td.getRefNo();
        //get score
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, td.getTaskKey());
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT, td.getTaskKey(), null,
                InspectionConstants.PROCESS_DECI_PENDING_APPLICANT_ACCEPT_INSPECTION_DATE, td.getRoleId(), null, td.getWkGrpId());
        String appHistoryId = appPremisesRoutingHistoryDto.getId();
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
        List<AppPremInspCorrelationDto> appPremInspCorrelationDtoList = IaisCommonUtils.genNewArrayList();
        //create appInspCorrelation and task
        AppPremInspCorrelationDto appPremInspCorrelationDto = new AppPremInspCorrelationDto();
        appPremInspCorrelationDto.setApplicationNo(applicationDto.getApplicationNo());
        appPremInspCorrelationDto.setId(null);
        appPremInspCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        appPremInspCorrelationDto.setUserId(userId);
        appPremInspCorrelationDto.setAuditTrailDto(auditTrailDto);
        appPremInspCorrelationDtoList.add(appPremInspCorrelationDto);

        TaskDto taskDto = new TaskDto();
        taskDto.setId(null);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setPriority(td.getPriority());
        taskDto.setRefNo(td.getRefNo());
        taskDto.setSlaAlertInDays(td.getSlaAlertInDays());
        taskDto.setSlaDateCompleted(null);
        taskDto.setSlaInDays(td.getSlaInDays());
        taskDto.setSlaRemainInDays(null);
        taskDto.setTaskKey(td.getTaskKey());
        taskDto.setTaskType(td.getTaskType());
        taskDto.setWkGrpId(td.getWkGrpId());
        taskDto.setUserId(userId);
        taskDto.setDateAssigned(new Date());
        taskDto.setRoleId(td.getRoleId());
        taskDto.setAuditTrailDto(auditTrailDto);
        taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_RESCHEDULING_COMMON_POOL);
        taskDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
        taskDto.setApplicationNo(td.getApplicationNo());
        taskDtoList.add(taskDto);

        //get appStatus
        String appStatus;
        if(ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT.equals(applicationDto.getStatus())) {
            appStatus = ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_PENDING_FE;
        } else {
            appStatus = ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_APPLICANT;
        }

        //get inspection date
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrId, InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
        Date inspDate = appPremisesRecommendationDto.getRecomInDate();
        //get inspector address
        OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
        String address = orgUserDto.getEmail();
        //is fast tracking
        String appNo = applicationDto.getApplicationNo();
        String url = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() +
                MessageConstants.MESSAGE_INBOX_URL_RE_SCHEDULING_CONFIRM_DATE + appNo;
        HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
        maskParams.put("applicationNo", appNo);
        boolean fastTracking = applicationDto.isFastTracking();
        if(!fastTracking){
            //get all application info from same premises
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = getAppPremisesCorrelationsByPremises(appPremCorrId);
            List<String> premCorrIds = getAppPremCorrIdByList(appPremisesCorrelationDtos);
            List<ApplicationDto> applicationDtoList = getApplicationDtosByCorr(appPremisesCorrelationDtos);
            //get all users

            Map<String, String> apptUserIdSvrIdMap = getSchedulingUsersByAppList(applicationDtoList, Collections.singletonList(userId), applicationDto);
            boolean allInPlaceFlag = allAppFromSamePremisesIsOk(applicationDtoList);
            if(allInPlaceFlag){
                List<ApplicationDto> applicationDtoLists = IaisCommonUtils.genNewArrayList();
                for(String premCorrId : premCorrIds) {
                    ApplicationDto appDto = inspectionTaskClient.getApplicationByCorreId(premCorrId).getEntity();
                    applicationDtoLists.add(appDto);
                }
                saveInspectionDate(appPremCorrId, taskDtoList, applicationDto, apptUserIdSvrIdMap, premCorrIds, auditTrailDto, appHistoryId);
                //update App
                ApplicationDto applicationDto1 = updateApplication(applicationDto, appStatus);
                applicationDto1.setAuditTrailDto(auditTrailDto);
                applicationService.updateFEApplicaiton(applicationDto1);
                //update inspection status
                updateInspectionStatus(appPremCorrId, InspectionConstants.INSPECTION_STATUS_RESCHEDULING_APPLICANT_ACCEPT_DATE);
                taskService.updateTask(td);
                taskService.createTasks(taskDtoList);
                inspectionTaskClient.createAppPremInspCorrelationDto(appPremInspCorrelationDtoList);
                createMessage(url, applicationDto, applicationGroupDto, maskParams, inspDate, address, applicationDtoLists);
            } else {
                //update App
                ApplicationDto applicationDto1 = updateApplication(applicationDto, appStatus);
                applicationDto1.setAuditTrailDto(auditTrailDto);
                applicationService.updateFEApplicaiton(applicationDto1);
                //update inspection status
                updateInspectionStatus(appPremCorrId, InspectionConstants.INSPECTION_STATUS_RESCHEDULING_APPLICANT_ACCEPT_DATE);
                taskService.updateTask(td);
                taskService.createTasks(taskDtoList);
                inspectionTaskClient.createAppPremInspCorrelationDto(appPremInspCorrelationDtoList);
            }
        } else {
            List<String> premCorrIds = IaisCommonUtils.genNewArrayList();
            premCorrIds.add(appPremCorrId);
            //get all users
            Map<String, String> apptUserIdSvrIdMap = getSchedulingUsersByAppList(applicationDtos, null, applicationDto);
            saveInspectionDate(appPremCorrId, taskDtoList, applicationDto, apptUserIdSvrIdMap, premCorrIds, auditTrailDto, appHistoryId);
            //update App
            ApplicationDto applicationDto1 = updateApplication(applicationDto, appStatus);
            applicationDto1.setAuditTrailDto(auditTrailDto);
            applicationService.updateFEApplicaiton(applicationDto1);
            //update inspection status
            updateInspectionStatus(appPremCorrId, InspectionConstants.INSPECTION_STATUS_RESCHEDULING_APPLICANT_ACCEPT_DATE);
            taskService.updateTask(td);
            taskService.createTasks(taskDtoList);
            inspectionTaskClient.createAppPremInspCorrelationDto(appPremInspCorrelationDtoList);
            createMessage(url, applicationDto, applicationGroupDto, maskParams, inspDate, address, applicationDtos);
        }
    }

    private List<String> getAppPremCorrIdByList(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos) {
        List<String> appPremCorrIds = IaisCommonUtils.genNewArrayList();
        for(AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos){
            appPremCorrIds.add(appPremisesCorrelationDto.getId());
        }
        return appPremCorrIds;
    }

    private boolean allAppFromSamePremisesIsOk(List<ApplicationDto> applicationDtoList) {
        boolean allInPlaceFlag = false;
        if(!IaisCommonUtils.isEmpty(applicationDtoList)){
            int allAppSize = applicationDtoList.size() - 1;
            int reSchAppSize = 0;
            for(ApplicationDto applicationDto : applicationDtoList){
                String appStatus = applicationDto.getStatus();
                if(ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_APPLICANT.equals(appStatus) ||
                        ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_PENDING_FE.equals(appStatus)) {
                    reSchAppSize++;
                }
            }
            if(allAppSize == reSchAppSize){
                allInPlaceFlag = true;
            }
        }
        return allInPlaceFlag;
    }

    private List<ApplicationDto> getApplicationDtosByCorr(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos) {
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        for(AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos){
            String applicationId = appPremisesCorrelationDto.getApplicationId();
            ApplicationDto applicationDto = applicationClient.getApplicationById(applicationId).getEntity();
            applicationDtos.add(applicationDto);
        }
        return applicationDtos;
    }

    private void createMessage(String url, ApplicationDto applicationDto, ApplicationGroupDto applicationGroupDto, HashMap<String, String> maskParams,
                               Date inspDate, String address, List<ApplicationDto> applicationDtoLists) {
        List<String> serviceCodes = IaisCommonUtils.genNewArrayList();
        for(ApplicationDto appDto : applicationDtoLists){
            String serviceId = appDto.getServiceId();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
            String serviceCode = hcsaServiceDto.getSvcCode();
            if(!StringUtil.isEmpty(serviceCode)){
                serviceCodes.add(serviceCode);
            }
        }
        String dateStr = Formatter.formatDateTime(inspDate, "dd/MM/yyyy");
        String dateTime = Formatter.formatDateTime(inspDate, "HH:mm:ss");
        String appNo = applicationDto.getApplicationNo();
        String applicantId = applicationGroupDto.getSubmitBy();
        OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicantId).getEntity();
        String applicantName = orgUserDto.getDisplayName();
        AppGrpPremisesDto appGrpPremisesDto = cessationClient.getAppGrpPremisesDtoByAppId(applicationDto.getId()).getEntity();
        Map<String ,Object> map = IaisCommonUtils.genNewHashMap();
        map.put("applicant", applicantName);
        String hciName = appGrpPremisesDto.getHciName();
        if(!StringUtil.isEmpty(hciName)){
            map.put("hciName", hciName);
        }
        map.put("hciName", hciName);
        map.put("date", dateStr);
        map.put("dateTime", dateTime);
        map.put("systemLink", url);
        map.put("address", address);
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RE_SCHEDULING_INSPECTION_DATE);
        emailParam.setTemplateContent(map);
        emailParam.setMaskParams(maskParams);
        emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_ACTION_REQUIRED);
        emailParam.setQueryCode(appNo);
        emailParam.setReqRefNum(appNo);
        emailParam.setRefId(appNo);
        emailParam.setSvcCodeList(serviceCodes);
        notificationHelper.sendNotification(emailParam);
    }

    private void saveInspectionDate(String appPremCorrId, List<TaskDto> taskDtoList, ApplicationDto applicationDto, Map<String, String> apptUserIdSvrIdMap,
                                    List<String> premCorrIds, AuditTrailDto auditTrailDto, String appHistoryId) {
        AppointmentDto appointmentDto = inspectionTaskClient.getApptStartEndDateByAppCorrId(appPremCorrId).getEntity();
        appointmentDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        List<String> serviceIds = IaisCommonUtils.genNewArrayList();
        for (Map.Entry<String, String> map : apptUserIdSvrIdMap.entrySet()) {
            if(!StringUtil.isEmpty(map.getValue())){
                serviceIds.add(map.getValue());
            }
        }
        Set<String> serviceIdSet = new HashSet<>(serviceIds);
        serviceIds = new ArrayList<>(serviceIdSet);
        //get Start date and End date when group no date
        if (appointmentDto.getStartDate() == null && appointmentDto.getEndDate() == null) {
            appointmentDto.setServiceIds(serviceIds);
            appointmentDto = hcsaConfigClient.getApptStartEndDateByService(appointmentDto).getEntity();
        }
        //get inspection date
        List<AppointmentUserDto> appointmentUserDtos = IaisCommonUtils.genNewArrayList();
        TaskDto tDto = taskDtoList.get(0);
        for (Map.Entry<String, String> map : apptUserIdSvrIdMap.entrySet()) {
            AppointmentUserDto appointmentUserDto = new AppointmentUserDto();
            OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(map.getKey()).getEntity();
            appointmentUserDto.setLoginUserId(orgUserDto.getUserId());
            String workGroupId = tDto.getWkGrpId();
            WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(workGroupId).getEntity();
            appointmentUserDto.setWorkGrpName(workingGroupDto.getGroupName());
            //get service id by task refno
            String serviceId = map.getValue();
            //get manHours by service and stage
            ApptAppInfoShowDto apptAppInfoShowDto = new ApptAppInfoShowDto();
            apptAppInfoShowDto.setApplicationType(applicationDto.getApplicationType());
            apptAppInfoShowDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
            apptAppInfoShowDto.setServiceId(serviceId);
            int manHours = getServiceManHours(tDto.getRefNo(), apptAppInfoShowDto);
            //Divide the time according to the number of people
            double hours = manHours;
            double peopleCount = apptUserIdSvrIdMap.size();
            int peopleHours = (int) Math.ceil(hours/peopleCount);
            appointmentUserDto.setWorkHours(peopleHours);
            appointmentUserDtos.add(appointmentUserDto);
        }
        //If one person is doing multiple services at the same time, The superposition of time
        appointmentUserDtos = getOnePersonBySomeService(appointmentUserDtos);
        appointmentDto.setUsers(appointmentUserDtos);
        try {
            FeignResponseEntity<List<ApptRequestDto>> result = appointmentClient.getUserCalendarByUserId(appointmentDto);
            Map<String, Collection<String>> headers = result.getHeaders();
            //Has it been blown up
            if(headers != null && StringUtil.isEmpty(headers.get("fusing"))) {
                Map<String, List<ApptUserCalendarDto>> inspectionDateMap = IaisCommonUtils.genNewHashMap();
                List<ApptRequestDto> apptRequestDtos = result.getEntity();
                if(!IaisCommonUtils.isEmpty(apptRequestDtos)){
                    for(ApptRequestDto apptRequestDto : apptRequestDtos){
                        inspectionDateMap.put(apptRequestDto.getApptRefNo(), apptRequestDto.getUserClandars());
                    }
                }
                //save date, confirm appointment date and synchronization FE
                saveSynchronApptDate(inspectionDateMap, premCorrIds, appointmentDto, auditTrailDto);

            }
        } catch (Exception e){
            applicationClient.removeHistoryById(appHistoryId);
            log.error(e.getMessage(), e);
        }
    }

    private void saveSynchronApptDate(Map<String, List<ApptUserCalendarDto>> inspectionDateMap, List<String> appPremCorrIds, AppointmentDto appointmentDto, AuditTrailDto auditTrailDto) {
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = IaisCommonUtils.genNewArrayList();
        //save AppPremisesInspecApptDto
        //cancel AppPremisesInspecApptDto
        int reschedulingCount = updateAppPremisesInspecApptDtoList(appPremCorrIds, auditTrailDto);
        for(String appPremCorrId : appPremCorrIds) {
            //create AppPremisesInspecApptDto
            for(Map.Entry<String, List<ApptUserCalendarDto>> inspDateMap : inspectionDateMap.entrySet()){
                String apptRefNo = inspDateMap.getKey();
                AppPremisesInspecApptDto appPremisesInspecApptDto = new AppPremisesInspecApptDto();
                appPremisesInspecApptDto.setAppCorrId(appPremCorrId);
                appPremisesInspecApptDto.setApptRefNo(apptRefNo);
                appPremisesInspecApptDto.setSpecificInspDate(null);
                appPremisesInspecApptDto.setId(null);
                appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPremisesInspecApptDto.setReschedulingCount(reschedulingCount);
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
                appPremisesInspecApptDto.setAuditTrailDto(auditTrailDto);
                appPremisesInspecApptDtoList.add(appPremisesInspecApptDto);
            }
        }
        appPremisesInspecApptDtoList = applicationClient.createAppPremisesInspecApptDto(appPremisesInspecApptDtoList).getEntity();
        //synchronization FE
        appPremisesInspecApptDtoList = setAudiTrailDtoInspAppt(appPremisesInspecApptDtoList, auditTrailDto);
        ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
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
    }
    private int updateAppPremisesInspecApptDtoList(List<String> appPremCorrIds, AuditTrailDto auditTrailDto) {
        int reschedulingCount = 0;
        //remove(update) AppPremisesInspecApptDto
        for(String appPremCorrId : appPremCorrIds) {
            List<AppPremisesInspecApptDto> appPremisesInspecApptDtos = inspectionTaskClient.getSystemDtosByAppPremCorrId(appPremCorrId).getEntity();
            if (!IaisCommonUtils.isEmpty(appPremisesInspecApptDtos)) {
                List<String> cancelRefNo = IaisCommonUtils.genNewArrayList();
                List<AppPremisesInspecApptDto> appPremisesInspecApptDtoUpdateList = IaisCommonUtils.genNewArrayList();
                AppPremisesInspecApptDto apptDto = appPremisesInspecApptDtos.get(0);
                reschedulingCount = apptDto.getReschedulingCount() + 1;
                for (AppPremisesInspecApptDto inspecApptDto : appPremisesInspecApptDtos) {
                    inspecApptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    inspecApptDto.setAuditTrailDto(auditTrailDto);
                    //update BE
                    inspecApptDto = applicationClient.updateAppPremisesInspecApptDto(inspecApptDto).getEntity();
                    inspecApptDto.setAuditTrailDto(auditTrailDto);
                    appPremisesInspecApptDtoUpdateList.add(inspecApptDto);
                    cancelRefNo.add(inspecApptDto.getApptRefNo());
                }
                ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
                apptInspectionDateDto.setAppPremisesInspecApptCreateList(appPremisesInspecApptDtoUpdateList);
                //do update FE
                createFeAppPremisesInspecApptDto(apptInspectionDateDto);
                ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
                apptCalendarStatusDto.setCancelRefNums(cancelRefNo);
                apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
                cancelOrConfirmApptDate(apptCalendarStatusDto);
            }
        }
        return reschedulingCount;
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

    private void cancelOrConfirmApptDate(ApptCalendarStatusDto apptCalendarStatusDto) {
        appointmentClient.updateUserCalendarStatus(apptCalendarStatusDto);
    }

    private List<AppPremisesCorrelationDto> getAppPremisesCorrelationsByPremises(String appPremCorrId) {
        return applicationClient.getAppPremisesCorrelationsByPremises(appPremCorrId).getEntity();
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

    private List<AppointmentUserDto> getOnePersonBySomeService(List<AppointmentUserDto> appointmentUserDtos) {

        List<AppointmentUserDto> appointmentUserDtoList = null;
        if (!IaisCommonUtils.isEmpty(appointmentUserDtos)) {
            appointmentUserDtoList = new ArrayList<>(appointmentUserDtos.size());
            for (AppointmentUserDto appointmentUserDto : appointmentUserDtos) {
                if (appointmentUserDtoList.isEmpty()) {
                    appointmentUserDtoList.add(appointmentUserDto);
                } else {
                    filterRepetitiveUser(appointmentUserDto, appointmentUserDtoList);
                }
            }
        }
        return appointmentUserDtoList;
    }

    private List<AppointmentUserDto> filterRepetitiveUser(AppointmentUserDto appointmentUserDto, List<AppointmentUserDto> appointmentUserDtoList) {
        List<AppointmentUserDto> appointmentUserDtos = IaisCommonUtils.genNewArrayList();
        for(AppointmentUserDto appointmentUserDto1 : appointmentUserDtoList){
            String loginUserId = appointmentUserDto.getLoginUserId();
            String curLoginUserId = appointmentUserDto1.getLoginUserId();
            if (loginUserId.equals(curLoginUserId)) {
                int hours = appointmentUserDto.getWorkHours();
                int curHours = appointmentUserDto1.getWorkHours();
                int allHours = hours + curHours;
                appointmentUserDto1.setWorkHours(allHours);
            } else {
                appointmentUserDtos.add(appointmentUserDto);
            }
        }
        if(!IaisCommonUtils.isEmpty(appointmentUserDtos)){
            for(AppointmentUserDto auDto : appointmentUserDtos){
                if(auDto != null){
                    appointmentUserDtoList.add(auDto);
                }
            }
        }
        return appointmentUserDtoList;
    }

    private Map<String, String> getServiceIdsByCorrIdsFromPremises(List<String> premCorrIds) {
        Map<String, String> serviceIds = applicationClient.getServiceIdsByCorrIdsFromPremises(premCorrIds).getEntity();
        return serviceIds;
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

    private void updateInspectionStatus(String appPremCorrId, String status) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorrId).getEntity();
        appInspectionStatusDto.setStatus(status);
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appInspectionStatusClient.update(appInspectionStatusDto);
    }

    private ApplicationDto updateApplication(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationViewService.updateApplicaiton(applicationDto);
    }

    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId){
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

    public AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String status, String stageId, String internalRemarks,
                                                                        String processDec, String roleId, String subStage, String workGroupId) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(status);
        appPremisesRoutingHistoryDto.setActionby(AppConsts.USER_ID_SYSTEM);
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setProcessDecision(processDec);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto.setWrkGrpId(workGroupId);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto).getEntity();
        return appPremisesRoutingHistoryDto;
    }

    private Map<String, String> getSchedulingUsersByAppList(List<ApplicationDto> applicationDtoList, List<String> taskUserIds, ApplicationDto appDto) {
        Map<String, String> apptUserIdSvrIdMap = IaisCommonUtils.genNewHashMap();
        if(!IaisCommonUtils.isEmpty(applicationDtoList)){
            for(ApplicationDto applicationDto : applicationDtoList){
                String appNo = applicationDto.getApplicationNo();
                List<AppPremInspCorrelationDto> appPremInspCorrelationDtoList = inspectionTaskClient.getAppInspCorreByAppNoStatus(appNo, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
                if(!IaisCommonUtils.isEmpty(appPremInspCorrelationDtoList)){
                    for(AppPremInspCorrelationDto appPremInspCorrelationDto : appPremInspCorrelationDtoList){
                        apptUserIdSvrIdMap.put(appPremInspCorrelationDto.getUserId(), applicationDto.getServiceId());
                    }
                }
            }
            for(String userId : taskUserIds){
                if(!StringUtil.isEmpty(userId)){
                    apptUserIdSvrIdMap.put(userId, appDto.getServiceId());
                }
            }
        }
        return apptUserIdSvrIdMap;
    }

    private void sendEmailWIT005(ApplicationViewDto applicationViewDto,String userId){
        log.info("------------->  Send Withdraw 003 Email");
        String officerName = "";
        OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
        if (orgUserDto != null&&orgUserDto.getUserId()!=null){
            officerName = orgUserDto.getUserId();
        }
        log.info("------------->  Send Withdraw 003 Email  officerName {}",officerName);
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        if (applicationDto != null){
            String appGrpId = applicationDto.getAppGrpId();
            String applicationNo = applicationDto.getApplicationNo();
            String applicationType = applicationViewDto.getApplicationType();
            String serviceId = applicationViewDto.getServiceType();
            String serviceName = HcsaServiceCacheHelper.getServiceById(serviceId).getSvcName();
            ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(appGrpId);
            try {
                AppGrpPremisesDto appGrpPremisesDto = cessationClient.getAppGrpPremisesDtoByAppId(applicationDto.getId()).getEntity();
                String loginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
                Map<String, Object> msgInfoMap = IaisCommonUtils.genNewHashMap();
                msgInfoMap.put("systemLink", loginUrl);
                msgInfoMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationType));
                msgInfoMap.put("ApplicationNumber", applicationNo);
                msgInfoMap.put("HCIName", appGrpPremisesDto.getHciName());
                msgInfoMap.put("Address", appGrpPremisesDto.getAddress());
                msgInfoMap.put("Applicant", officerName);
                msgInfoMap.put("submissionDate", Formatter.formatDateTime(applicationGroupDto.getSubmitDt()));
                msgInfoMap.put("ApplicationDate", Formatter.formatDateTime(new Date()));
                msgInfoMap.put("S_LName", serviceName);
                msgInfoMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
                try {
                    newApplicationDelegator.sendEmail(MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_ASO_EMAIL, msgInfoMap, applicationDto);
                    newApplicationDelegator.sendSMS(applicationDto,MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_ASO_SMS, msgInfoMap);
                } catch (Exception e) {
                    log.info("------------->  Send Withdraw 003 Email  Failed");
                    log.error(e.getMessage(), e);
                }
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
        }
    }
}
