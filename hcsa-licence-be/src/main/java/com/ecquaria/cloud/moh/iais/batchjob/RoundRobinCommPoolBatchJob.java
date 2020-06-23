package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.client.AppEicClient;
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
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptCalendarStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
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
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private AppointmentClient appointmentClient;

    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;

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
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private InboxMsgService inboxMsgService;

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
        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob start ..."));
        String date = getDate();
        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob date -- >:" +date));
        List<TaskDto> taskDtoList = taskService.getTaskDtoByDate(date);
        if(!IaisCommonUtils.isEmpty(taskDtoList)){
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob taskDtoList.size() -- >:" +taskDtoList.size()));
          for (TaskDto taskDto : taskDtoList){
              if(!RoleConsts.USER_ROLE_BROADCAST.equals(taskDto.getRoleId())){
                  try{
                      String workGroupId = taskDto.getWkGrpId();
                      log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob taskId -- >:" +taskDto.getId()));
                      log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob workGroupId -- >:" +workGroupId));
                      TaskDto taskScoreDto = taskService.getUserIdForWorkGroup(workGroupId);
                      taskDto.setUserId(taskScoreDto.getUserId());
                      taskDto.setDateAssigned(new Date());
                      taskDto.setAuditTrailDto(AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET));
                      taskDto = taskService.updateTask(taskDto);
                      ApplicationViewDto applicationViewDto=applicationClient.getAppViewByCorrelationId(taskDto.getRefNo()).getEntity();
                      if(ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL.equals(applicationViewDto.getApplicationDto().getStatus()) ||
                              ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT.equals(applicationViewDto.getApplicationDto().getStatus())){
                          ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
                          List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
                          applicationDtos.add(applicationDto);
                          AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
                          String taskUserId = taskScoreDto.getUserId();
                          List<String> taskUserIds = IaisCommonUtils.genNewArrayList();
                          taskUserIds.add(taskUserId);
                          ApplicationGroupDto applicationGroupDto = applicationViewDto.getApplicationGroupDto();
                          assignReschedulingTask(taskDto, taskUserIds, applicationDtos, auditTrailDto, applicationGroupDto);
                      }

                  }catch (Exception e){
                      log.error(StringUtil.changeForLog("This  Task can not assign id-->:"+taskDto.getId()));
                  }
              }else{
                  log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob broadcast taskId -- >:" +taskDto.getId()));
              }
          }
        }else{
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob do  not need roud robin task !!!"));
        }
        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob end ..."));
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


    private void assignReschedulingTask(TaskDto td, List<String> taskUserIds, List<ApplicationDto> applicationDtos, AuditTrailDto auditTrailDto,
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
        List<AppPremisesRoutingHistoryExtDto> appPremisesRoutingHistoryExtDtoList = IaisCommonUtils.genNewArrayList();
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
        for(String taskUserId : taskUserIds){
            //create AppPremisesRoutingHistoryExtDto and task
            AppPremisesRoutingHistoryExtDto appPremisesRoutingHistoryExtDto = new AppPremisesRoutingHistoryExtDto();
            appPremisesRoutingHistoryExtDto.setAppPremRhId(appHistoryId);
            appPremisesRoutingHistoryExtDto.setComponentName(RoleConsts.USER_ROLE_INSPECTIOR);
            appPremisesRoutingHistoryExtDto.setComponentValue(taskUserId);
            appPremisesRoutingHistoryExtDto.setId(null);
            appPremisesRoutingHistoryExtDto.setAuditTrailDto(auditTrailDto);
            appPremisesRoutingHistoryExtDtoList.add(appPremisesRoutingHistoryExtDto);

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
            taskDto.setUserId(taskUserId);
            taskDto.setDateAssigned(new Date());
            taskDto.setRoleId(td.getRoleId());
            taskDto.setAuditTrailDto(auditTrailDto);
            taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_RESCHEDULING_COMMON_POOL);
            taskDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
            taskDto.setApplicationNo(td.getApplicationNo());
            taskDtoList.add(taskDto);
        }

        //get appStatus
        String appStatus;
        if(ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT.equals(applicationDto.getStatus())) {
            appStatus = ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_PENDING_FE;
        } else {
            appStatus = ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_APPLICANT;
        }

        //get inspection date
        //is fast tracking
        String serviceId = applicationDto.getServiceId();
        Date submitDt = applicationGroupDto.getSubmitDt();
        String licenseeId = applicationGroupDto.getLicenseeId();
        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        String serviceCode = hcsaServiceDto.getSvcCode();
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
            boolean allInPlaceFlag = allAppFromSamePremisesIsOk(applicationDtoList);
            if(allInPlaceFlag){
                saveInspectionDate(appPremCorrId, taskDtoList, applicationDto, taskUserIds, premCorrIds, auditTrailDto);
                //update App
                ApplicationDto applicationDto1 = updateApplication(applicationDto, appStatus);
                applicationDto1.setAuditTrailDto(auditTrailDto);
                applicationService.updateFEApplicaiton(applicationDto1);
                //update inspection status
                updateInspectionStatus(appPremCorrId, InspectionConstants.INSPECTION_STATUS_RESCHEDULING_APPLICANT_ACCEPT_DATE);
                taskService.updateTask(td);
                taskService.createTasks(taskDtoList);
                inspectionTaskClient.createAppPremisesRoutingHistoryExtDtos(appPremisesRoutingHistoryExtDtoList);
                createMessage(url, serviceCode, submitDt, licenseeId, maskParams);
            } else {
                //update App
                ApplicationDto applicationDto1 = updateApplication(applicationDto, appStatus);
                applicationDto1.setAuditTrailDto(auditTrailDto);
                applicationService.updateFEApplicaiton(applicationDto1);
                //update inspection status
                updateInspectionStatus(appPremCorrId, InspectionConstants.INSPECTION_STATUS_RESCHEDULING_APPLICANT_ACCEPT_DATE);
                taskService.updateTask(td);
                taskService.createTasks(taskDtoList);
                inspectionTaskClient.createAppPremisesRoutingHistoryExtDtos(appPremisesRoutingHistoryExtDtoList);
            }
        } else {
            List<String> premCorrIds = IaisCommonUtils.genNewArrayList();
            premCorrIds.add(appPremCorrId);
            saveInspectionDate(appPremCorrId, taskDtoList, applicationDto, taskUserIds, premCorrIds, auditTrailDto);
            //update App
            ApplicationDto applicationDto1 = updateApplication(applicationDto, appStatus);
            applicationDto1.setAuditTrailDto(auditTrailDto);
            applicationService.updateFEApplicaiton(applicationDto1);
            //update inspection status
            updateInspectionStatus(appPremCorrId, InspectionConstants.INSPECTION_STATUS_RESCHEDULING_APPLICANT_ACCEPT_DATE);
            taskService.updateTask(td);
            taskService.createTasks(taskDtoList);
            inspectionTaskClient.createAppPremisesRoutingHistoryExtDtos(appPremisesRoutingHistoryExtDtoList);
            createMessage(url, serviceCode, submitDt, licenseeId, maskParams);
        }
    }

    private List<String> getAppPremCorrIdByList(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos) {
        List<String> appPremCorrIds = IaisCommonUtils.getAppPendingStatus();
        for(AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos){
            appPremCorrIds.add(appPremisesCorrelationDto.getId());
        }
        return appPremCorrIds;
    }

    private boolean allAppFromSamePremisesIsOk(List<ApplicationDto> applicationDtoList) {
        boolean allInPlaceFlag = false;
        if(!IaisCommonUtils.isEmpty(applicationDtoList)){
            int allAppSize = applicationDtoList.size();
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
        interMessageDto.setService_id(serviceCode);
        interMessageDto.setUserId(licenseeId);
        interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        interMessageDto.setMsgContent(templateMessageByContent);
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        interMessageDto.setMaskParams(maskParams);
        inboxMsgService.saveInterMessage(interMessageDto);
    }

    private void saveInspectionDate(String appPremCorrId, List<TaskDto> taskDtoList, ApplicationDto applicationDto,
                                    List<String> taskUserIds, List<String> premCorrIds, AuditTrailDto auditTrailDto) {
        AppointmentDto appointmentDto = inspectionTaskClient.getApptStartEndDateByAppCorrId(appPremCorrId).getEntity();
        appointmentDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        Map<String, String> corrIdServiceIdMap = getServiceIdsByCorrIdsFromPremises(premCorrIds);
        List<String> serviceIds = IaisCommonUtils.genNewArrayList();
        for (Map.Entry<String, String> mapDate : corrIdServiceIdMap.entrySet()) {
            if(!StringUtil.isEmpty(mapDate.getValue())){
                serviceIds.add(mapDate.getValue());
            }
        }
        //get Start date and End date when group no date
        if (appointmentDto.getStartDate() == null && appointmentDto.getEndDate() == null) {
            appointmentDto.setServiceIds(serviceIds);
            appointmentDto = hcsaConfigClient.getApptStartEndDateByService(appointmentDto).getEntity();
        }
        //get inspection date
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
            apptAppInfoShowDto.setApplicationType(applicationDto.getApplicationType());
            apptAppInfoShowDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
            apptAppInfoShowDto.setServiceId(serviceId);
            int manHours = getServiceManHours(tDto.getRefNo(), apptAppInfoShowDto);
            //Divide the time according to the number of people
            double hours = manHours;
            double peopleCount = taskUserIds.size();
            int peopleHours = (int) Math.ceil(hours/peopleCount);
            appointmentUserDto.setWorkHours(peopleHours);
            appointmentUserDtos.add(appointmentUserDto);
        }
        //If one person is doing multiple services at the same time, The superposition of time
        appointmentUserDtos = getOnePersonBySomeService(appointmentUserDtos);
        appointmentDto.setUsers(appointmentUserDtos);
        try {
            FeignResponseEntity<Map<String, List<ApptUserCalendarDto>>> result = appointmentClient.getUserCalendarByUserId(appointmentDto);
            Map<String, Collection<String>> headers = result.getHeaders();
            //Has it been blown up
            if(headers != null && StringUtil.isEmpty(headers.get("fusing"))) {
                Map<String, List<ApptUserCalendarDto>> inspectionDateMap = result.getEntity();
                //save date, confirm appointment date and synchronization FE
                saveSynchronApptDate(inspectionDateMap, premCorrIds, appointmentDto, auditTrailDto);

            }
        } catch (Exception e){
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException("get InspectionDate Error!!!");
        }
    }

    private void saveSynchronApptDate(Map<String, List<ApptUserCalendarDto>> inspectionDateMap, List<String> appPremCorrIds, AppointmentDto appointmentDto, AuditTrailDto auditTrailDto) {
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = IaisCommonUtils.genNewArrayList();
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
        return appPremisesCorrClient.getAppPremisesCorrelationsByPremises(appPremCorrId).getEntity();
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
        if(!IaisCommonUtils.isEmpty(appointmentUserDtos)){
            for(AppointmentUserDto appointmentUserDto : appointmentUserDtos){
                if(IaisCommonUtils.isEmpty(appointmentUserDtoList)){
                    appointmentUserDtoList = IaisCommonUtils.genNewArrayList();
                    appointmentUserDtoList.add(appointmentUserDto);
                } else {
                    appointmentUserDtoList = filterRepetitiveUser(appointmentUserDto, appointmentUserDtoList);
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

    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId) {
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
        for (ApplicationDto applicationDto : applicationDtos) {
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        return hcsaSvcStageWorkingGroupDtos;
    }

    public AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String status, String stageId, String internalRemarks,
                                                                        String processDec, String roleId, String subStage, String workGroupId) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(status);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setProcessDecision(processDec);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto.setWrkGrpId(workGroupId);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto).getEntity();
        return appPremisesRoutingHistoryDto;
    }


}
