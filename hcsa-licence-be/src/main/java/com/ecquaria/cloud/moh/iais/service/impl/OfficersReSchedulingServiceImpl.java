package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremInspCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptCalendarStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschedulingOfficerDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschedulingOfficerQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AppointmentUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.OfficersReSchedulingService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Shicheng
 * @date 2020/6/30 13:57
 **/
@Service
@Slf4j
public class OfficersReSchedulingServiceImpl implements OfficersReSchedulingService {

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AppointmentClient appointmentClient;

    @Value("${iais.email.sender}")
    private String mailSender;

    @Autowired
    private EmailClient emailClient;

    @Override
    public List<SelectOption> getInspWorkGroupByLogin(LoginContext loginContext, ReschedulingOfficerDto reschedulingOfficerDto) {
        Set<String> workGroupIds = loginContext.getWrkGrpIds();
        if(workGroupIds == null){
            return null;
        }
        List<String> workGroupNames = IaisCommonUtils.genNewArrayList();
        Map<String, String> workGroupMap = IaisCommonUtils.genNewHashMap();
        for(String workGroupId : workGroupIds){
            WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(workGroupId).getEntity();
            String workGroupName = workingGroupDto.getGroupName();
            if(workGroupName.contains("Inspection")){
                workGroupNames.add(workGroupName);
                workGroupMap.put(workGroupName, workGroupId);
            }
        }
        reschedulingOfficerDto.setWorkGroupIdMap(workGroupMap);
        List<SelectOption> workGroupOption = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(workGroupNames)){
            //sort name
            Collections.sort(workGroupNames);
            int index = 0;
            for(String workGroupName : workGroupNames){
                ++index;
                SelectOption selectOption = new SelectOption(index + "", workGroupName);
                workGroupOption.add(selectOption);
            }
        }
        return workGroupOption;
    }

    @Override
    public List<SelectOption> getInspectorByWorkGroupId(String workGroupId, ReschedulingOfficerDto reschedulingOfficerDto, String workGroupNo) {
        List<SelectOption> inspectorOption = IaisCommonUtils.genNewArrayList();
        Map<String, String> userIdMap = IaisCommonUtils.genNewHashMap();
        Map<String, Map<String, String>> groupCheckUserIdMap = reschedulingOfficerDto.getGroupCheckUserIdMap();
        if(groupCheckUserIdMap == null){
            groupCheckUserIdMap = IaisCommonUtils.genNewHashMap();
        }
        List<OrgUserDto> orgUserDtoList = organizationClient.getUsersByWorkGroupName(workGroupId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
        if(!IaisCommonUtils.isEmpty(orgUserDtoList)){
            for(int i = 0; i < orgUserDtoList.size(); i++){
                //key and id
                userIdMap.put(i + "", orgUserDtoList.get(i).getId());
                //key and name
                SelectOption so = new SelectOption(i + "", orgUserDtoList.get(i).getDisplayName());
                inspectorOption.add(so);
            }
            groupCheckUserIdMap.put(workGroupNo, userIdMap);
        }
        reschedulingOfficerDto.setGroupCheckUserIdMap(groupCheckUserIdMap);
        return inspectorOption;
    }

    @Override
    public List<String> allInspectorFromGroupList(ReschedulingOfficerDto reschedulingOfficerDto, List<SelectOption> workGroupOption) {
        List<String> workGroupNos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(workGroupOption)){
            Map<String, String> workGroupMap = reschedulingOfficerDto.getWorkGroupIdMap();
            Map<String, List<SelectOption>> inspectorByGroup = IaisCommonUtils.genNewHashMap();
            for(SelectOption selectOption : workGroupOption){
                String workGroupNo = selectOption.getValue();
                String workGroupName = selectOption.getText();
                String workGroupId = workGroupMap.get(workGroupName);
                List<SelectOption> inspectorOption = getInspectorByWorkGroupId(workGroupId, reschedulingOfficerDto, workGroupNo);
                inspectorByGroup.put(workGroupNo, inspectorOption);
                workGroupNos.add(workGroupNo);
            }
            reschedulingOfficerDto.setInspectorByGroup(inspectorByGroup);
        }
        return workGroupNos;
    }

    @Override
    public List<String> getAppNoByInspectorAndConditions(ReschedulingOfficerDto reschedulingOfficerDto) {
        //get not in appStatus
        String[] appStatusStr = AppointmentUtil.getNoReschdulingAppStatus();
        List<String> appStatusList = IaisCommonUtils.genNewArrayList();
        Collections.addAll(appStatusList, appStatusStr);

        List<String> appNoList = IaisCommonUtils.genNewArrayList();
        String workGroupCheck = reschedulingOfficerDto.getWorkGroupCheck();
        Map<String, Map<String, String>> groupCheckUserIdMap = reschedulingOfficerDto.getGroupCheckUserIdMap();
        Map<String, List<SelectOption>> inspectorByGroup = reschedulingOfficerDto.getInspectorByGroup();

        if(!StringUtil.isEmpty(workGroupCheck) && inspectorByGroup != null && groupCheckUserIdMap != null){
            //get group key and userId
            Map<String, String> userIdMap = groupCheckUserIdMap.get(workGroupCheck);
            List<SelectOption> inspectorOption = inspectorByGroup.get(workGroupCheck);

            if(!IaisCommonUtils.isEmpty(inspectorOption) && userIdMap != null){
                Map<String, List<String>> inspectorAppNoMap = reschedulingOfficerDto.getInspectorAppNoMap();
                if (inspectorAppNoMap == null) {
                    inspectorAppNoMap = IaisCommonUtils.genNewHashMap();
                }
                List<String> repeatAppNo = IaisCommonUtils.genNewArrayList();
                for (SelectOption selectOption : inspectorOption) {
                    String inspectorValue = selectOption.getValue();
                    String userId = userIdMap.get(inspectorValue);
                    List<AppPremInspCorrelationDto> appPremInspCorrelationDtoList = inspectionTaskClient.getAppInspCorreByUserIdStatus(userId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
                    //filter rescheduling time limit
                    List<String> appNos = filterTimeLimit(appPremInspCorrelationDtoList);
                    //filter fast tracking and the same premises
                    appNos = filterPremisesAndFast(appNos, reschedulingOfficerDto, repeatAppNo, appStatusList);
                    inspectorAppNoMap.put(userId, appNos);
                    if(!IaisCommonUtils.isEmpty(appNos)) {
                        appNoList.addAll(appNos);
                    }
                }
                reschedulingOfficerDto.setInspectorAppNoMap(inspectorAppNoMap);
            }
        }

        return appNoList;
    }

    @Override
    public SearchResult<ReschedulingOfficerQueryDto> getOfficersSearch(SearchParam searchParam) {
        return inspectionTaskClient.officerReSchSearch(searchParam).getEntity();
    }

    @Override
    public SearchResult<ReschedulingOfficerQueryDto> setInspectorsAndServices(SearchResult<ReschedulingOfficerQueryDto> searchResult, ReschedulingOfficerDto reschedulingOfficerDto) {
        if(searchResult != null && !IaisCommonUtils.isEmpty(searchResult.getRows())){
            for(ReschedulingOfficerQueryDto reschedulingOfficerQueryDto : searchResult.getRows()){
                String appNo = reschedulingOfficerQueryDto.getAppNo();
                Map<String, List<String>> samePremisesAppMap = reschedulingOfficerDto.getSamePremisesAppMap();
                List<String> applicationNos = samePremisesAppMap.get(appNo);
                List<String> inspectorNames = getInspectorsByAppNoList(applicationNos);
                Set<String> inspectorNameSet = new HashSet<>(inspectorNames);
                inspectorNames = new ArrayList<>(inspectorNameSet);
                Collections.sort(inspectorNames);
                reschedulingOfficerQueryDto.setInspectors(inspectorNames);
                List<String> serviceNames = getServiceNameByAppNoList(applicationNos);
                reschedulingOfficerQueryDto.setServiceNames(serviceNames);
            }
        }
        return searchResult;
    }

    @Override
    public List<String> appNoListByGroupAndUserCheck(ReschedulingOfficerDto reschedulingOfficerDto, String workGroupCheck, String inspectorCheck) {
        List<String> appNoList = IaisCommonUtils.genNewArrayList();
        Map<String, Map<String, String>> groupCheckUserIdMap = reschedulingOfficerDto.getGroupCheckUserIdMap();
        Map<String, List<String>> inspectorAppNoMap = reschedulingOfficerDto.getInspectorAppNoMap();
        if(groupCheckUserIdMap != null && inspectorAppNoMap != null){
            if(!StringUtil.isEmpty(inspectorCheck)){
                appNoList = getAppNoByInspector(groupCheckUserIdMap, workGroupCheck, inspectorAppNoMap, inspectorCheck, appNoList);
            } else {
                appNoList = getAppNoByGroup(groupCheckUserIdMap, workGroupCheck, inspectorAppNoMap, appNoList);
            }
        }
        return appNoList;
    }

    @Override
    public ApplicationDto getApplicationByAppNo(String applicationNo) {
        return applicationClient.getAppByNo(applicationNo).getEntity();
    }

    @Override
    public void reScheduleRoutingTask(ReschedulingOfficerDto reschedulingOfficerDto) {
        String appNo = reschedulingOfficerDto.getAssignNo();
        ApplicationDto appDto = getApplicationByAppNo(appNo);
        ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(appDto.getAppGrpId()).getEntity();
        String licenseeId = applicationGroupDto.getLicenseeId();
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        Map<String, List<String>> samePremisesAppMap = reschedulingOfficerDto.getSamePremisesAppMap();
        if(samePremisesAppMap != null) {
            List<String> appNoList = samePremisesAppMap.get(appNo);
            List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
            if(!IaisCommonUtils.isEmpty(appNoList)){
                for(String applicationNo : appNoList){
                    //get application, task score
                    ApplicationDto applicationDto = applicationClient.getAppByNo(applicationNo).getEntity();
                    List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
                    applicationDtos.add(applicationDto);
                    List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_INS);
                    hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
                    int taskScore = hcsaSvcStageWorkingGroupDtos.get(0).getCount();
                    AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(applicationDto.getId()).getEntity();
                    List<TaskDto> taskDtos = organizationClient.getCurrTaskByRefNo(appPremisesCorrelationDto.getId()).getEntity();
                    if(!IaisCommonUtils.isEmpty(taskDtos)){
                        taskDtoList.add(taskDtos.get(0));
                        //create and update task
                        reSchSaveTask(taskDtos, auditTrailDto, taskScore);
                    }
                }
            }
            reschedulingOfficerDto.setSaveAppNoList(appNoList);
            reschedulingOfficerDto.setTaskDtos(taskDtoList);
            reschedulingOfficerDto.setAuditTrailDto(auditTrailDto);
            inspectionTaskClient.reScheduleSaveRouteData(reschedulingOfficerDto);
            try {
                EmailDto emailDto = new EmailDto();
                emailDto.setContent("Please contact the respective MOH officer to reschedule your appointment.");
                emailDto.setSubject("MOH IAIS - Moh Officer Rescheduling");
                emailDto.setSender(mailSender);
                emailDto.setClientQueryCode(appDto.getId());
                emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
                //emailClient.sendNotification(emailDto);
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
        }
    }

    @Override
    public AppointmentDto getInspDateValidateData(ReschedulingOfficerDto reschedulingOfficerDto) {
        String appNo = reschedulingOfficerDto.getAssignNo();
        ApplicationDto applicationDto = applicationClient.getAppByNo(appNo).getEntity();
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(applicationDto.getId()).getEntity();
        //get Applicant set start date and end date from appGroup
        AppointmentDto appointmentDto = inspectionTaskClient.getApptStartEndDateByAppCorrId(appPremisesCorrelationDto.getId()).getEntity();
        //specific date dto
        appointmentDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        Map<String, List<String>> samePremisesAppMap = reschedulingOfficerDto.getSamePremisesAppMap();
        List<String> premCorrIds = getPremIdsByAppNoList(samePremisesAppMap, appNo);
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
        //get Other Tasks From The Same Premises
        List<TaskDto> taskDtoList = getAllTaskFromSamePremises(premCorrIds);
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
            List<TaskDto> sizeTask = organizationClient.getCurrTaskByRefNo(tDto.getRefNo()).getEntity();
            double hours = manHours;
            double peopleCount = sizeTask.size();
            int peopleHours = (int) Math.ceil(hours/peopleCount);
            appointmentUserDto.setWorkHours(peopleHours);
            appointmentUserDtos.add(appointmentUserDto);
        }
        //If one person is doing multiple services at the same time, The superposition of time
        appointmentUserDtos = getOnePersonBySomeService(appointmentUserDtos);
        appointmentDto.setUsers(appointmentUserDtos);
        return appointmentDto;
    }

    @Override
    public void reScheduleRoutingAudit(ReschedulingOfficerDto reschedulingOfficerDto) {
        String appNo = reschedulingOfficerDto.getAssignNo();
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        Date saveDate = reschedulingOfficerDto.getSpecificStartDate();
        //end hour - 1, because the function save all start hour
        AppointmentDto appointmentDto = subtractEndHourByApptDto(reschedulingOfficerDto.getAppointmentDto());
        String apptRefNo = appointmentClient.saveManualUserCalendar(appointmentDto).getEntity();
        Map<String, List<String>> samePremisesAppMap = reschedulingOfficerDto.getSamePremisesAppMap();
        if(samePremisesAppMap != null) {
            List<String> appNoList = samePremisesAppMap.get(appNo);
            if(!IaisCommonUtils.isEmpty(appNoList)){
                List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = IaisCommonUtils.genNewArrayList();
                for(String applicationNo : appNoList){
                    List<String> userIds = IaisCommonUtils.genNewArrayList();
                    ApplicationDto applicationDto = applicationClient.getAppByNo(applicationNo).getEntity();
                    AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(applicationDto.getId()).getEntity();

                    //confirm and cancel Inspection date
                    cancelInspectionDate(appPremisesCorrelationDto.getId(), auditTrailDto);
                    appPremisesInspecApptDtoList = confirmInspectionDate(apptRefNo, appPremisesCorrelationDto.getId(), auditTrailDto, appPremisesInspecApptDtoList);
                    List<TaskDto> taskDtos = organizationClient.getCurrTaskByRefNo(appPremisesCorrelationDto.getId()).getEntity();
                    if(!IaisCommonUtils.isEmpty(taskDtos)){
                        for(TaskDto taskDto : taskDtos){
                            userIds.add(taskDto.getUserId());
                        }
                        //cancel the original inspector and create new
                        List<AppPremInspCorrelationDto> appPremInspCorrelationDtoList = inspectionTaskClient.getAppInspCorreByAppNoStatus(applicationNo, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
                        saveNewInspectorReScheduling(appPremInspCorrelationDtoList, userIds, auditTrailDto, applicationNo);
                    }
                    //save Inspection date by Recommendation
                    AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationDto.getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                    createOrUpdateRecommendation(appPremisesRecommendationDto, appPremisesCorrelationDto.getId(), saveDate);
                }
                //confirm Inspection date
                applicationClient.createAppPremisesInspecApptDto(appPremisesInspecApptDtoList).getEntity();
                List<String> confirmRefNo = IaisCommonUtils.genNewArrayList();
                confirmRefNo.add(apptRefNo);
                ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
                apptCalendarStatusDto.setConfirmRefNums(confirmRefNo);
                apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
                cancelOrConfirmApptDate(apptCalendarStatusDto);
            }
        }
    }

    @Override
    public AppointmentDto subtractEndHourByApptDto(AppointmentDto appointmentDto) {
        String endDateStr = appointmentDto.getEndDate();
        if(!StringUtil.isEmpty(endDateStr)){
            try {
                Date endDate = Formatter.parseDateTime(endDateStr, "yyyy-MM-dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                cal.setTime(endDate);
                cal.add(Calendar.HOUR, -1);
                appointmentDto.setEndDate(Formatter.formatDateTime(cal.getTime(), "yyyy-MM-dd HH:mm:ss"));
            } catch (ParseException e) {
                log.info(e.getMessage());
                return appointmentDto;
            }
        }
        return appointmentDto;
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
            appPremisesRecommendationDto.setRecomInDate(saveDate);
            appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fillUpCheckListGetAppClient.updateAppRecom(appPremisesRecommendationDto);
        }
    }

    private void saveNewInspectorReScheduling(List<AppPremInspCorrelationDto> appPremInspCorrelationDtoList, List<String> userIds, AuditTrailDto auditTrailDto, String applicationNo) {
        if(!IaisCommonUtils.isEmpty(appPremInspCorrelationDtoList)){
            for(AppPremInspCorrelationDto appPremInspCorrelationDto : appPremInspCorrelationDtoList){
                appPremInspCorrelationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                appPremInspCorrelationDto.setAuditTrailDto(auditTrailDto);
                inspectionTaskClient.updateAppPremInspCorrelationDto(appPremInspCorrelationDto);
            }
        }
        if(!IaisCommonUtils.isEmpty(userIds)){
            List<AppPremInspCorrelationDto> appPremInspCorrelationDtos = IaisCommonUtils.genNewArrayList();
            for(String userId : userIds) {
                AppPremInspCorrelationDto appPremInspCorrelationDto = new AppPremInspCorrelationDto();
                appPremInspCorrelationDto.setId(null);
                appPremInspCorrelationDto.setUserId(userId);
                appPremInspCorrelationDto.setApplicationNo(applicationNo);
                appPremInspCorrelationDto.setAuditTrailDto(auditTrailDto);
                appPremInspCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPremInspCorrelationDtos.add(appPremInspCorrelationDto);
            }
            inspectionTaskClient.createAppPremInspCorrelationDto(appPremInspCorrelationDtos);
        }
    }

    private List<AppPremisesInspecApptDto> confirmInspectionDate(String apptRefNo, String id, AuditTrailDto auditTrailDto, List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList) {
        AppPremisesInspecApptDto appPremisesInspecApptDto = new AppPremisesInspecApptDto();
        appPremisesInspecApptDto.setAppCorrId(id);
        appPremisesInspecApptDto.setApptRefNo(apptRefNo);
        appPremisesInspecApptDto.setSpecificInspDate(null);
        appPremisesInspecApptDto.setId(null);
        appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        appPremisesInspecApptDto.setAuditTrailDto(auditTrailDto);
        appPremisesInspecApptDtoList.add(appPremisesInspecApptDto);
        return appPremisesInspecApptDtoList;
    }

    private void cancelInspectionDate(String id, AuditTrailDto auditTrailDto) {
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtos = inspectionTaskClient.getSystemDtosByAppPremCorrId(id).getEntity();
        if (!IaisCommonUtils.isEmpty(appPremisesInspecApptDtos)) {
            List<String> cancelRefNo = IaisCommonUtils.genNewArrayList();
            List<AppPremisesInspecApptDto> appPremisesInspecApptDtoUpdateList = IaisCommonUtils.genNewArrayList();
            for (AppPremisesInspecApptDto inspecApptDto : appPremisesInspecApptDtos) {
                inspecApptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                inspecApptDto.setAuditTrailDto(auditTrailDto);
                //update BE
                inspecApptDto = applicationClient.updateAppPremisesInspecApptDto(inspecApptDto).getEntity();
                inspecApptDto.setAuditTrailDto(auditTrailDto);
                appPremisesInspecApptDtoUpdateList.add(inspecApptDto);
                cancelRefNo.add(inspecApptDto.getApptRefNo());
            }
            ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
            apptCalendarStatusDto.setCancelRefNums(cancelRefNo);
            apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
            cancelOrConfirmApptDate(apptCalendarStatusDto);
        }
    }

    private void cancelOrConfirmApptDate(ApptCalendarStatusDto apptCalendarStatusDto) {
        appointmentClient.updateUserCalendarStatus(apptCalendarStatusDto);
    }

    private List<AppointmentUserDto> getOnePersonBySomeService(List<AppointmentUserDto> appointmentUserDtos) {
        List<AppointmentUserDto> appointmentUserDtoList = null;
        if(!IaisCommonUtils.isEmpty(appointmentUserDtos)){
            for(AppointmentUserDto appointmentUserDto : appointmentUserDtos){
                if(IaisCommonUtils.isEmpty(appointmentUserDtoList)){
                    appointmentUserDtoList = IaisCommonUtils.genNewArrayList();
                    appointmentUserDtoList.add(appointmentUserDto);
                } else {
                    appointmentUserDtoList = filterRepetitiveUser(appointmentUserDto, appointmentUserDtoList);//NOSONAR
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

    private List<String> getPremIdsByAppNoList(Map<String, List<String>> samePremisesAppMap, String appNo) {
        List<String> premCorrIds = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(appNo) && samePremisesAppMap != null){
            List<String> appNoList = samePremisesAppMap.get(appNo);
            if(!IaisCommonUtils.isEmpty(appNoList)){
                for(String applicationNo : appNoList){
                    AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremCorrByAppNo(applicationNo).getEntity();
                    premCorrIds.add(appPremisesCorrelationDto.getId());
                }
            }
        }
        return premCorrIds;
    }

    private void reSchSaveTask(List<TaskDto> taskDtos, AuditTrailDto auditTrailDto, int taskScore) {
        List<TaskDto> saveTaskDtoList = IaisCommonUtils.genNewArrayList();
        //filter by common be one
        List<String> repetitiveAppNo = IaisCommonUtils.genNewArrayList();
        for(TaskDto taskDto : taskDtos){
            taskDto.setSlaDateCompleted(new Date());
            taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
            taskDto.setAuditTrailDto(auditTrailDto);
            taskService.updateTask(taskDto);
            if(!repetitiveAppNo.contains(taskDto.getApplicationNo())) {
                repetitiveAppNo.add(taskDto.getApplicationNo());
                TaskDto saveTask = new TaskDto();
                saveTask.setId(null);
                saveTask.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
                saveTask.setPriority(taskDto.getPriority());
                saveTask.setRefNo(taskDto.getRefNo());
                saveTask.setScore(taskScore);
                saveTask.setSlaAlertInDays(taskDto.getSlaAlertInDays());
                saveTask.setSlaDateCompleted(null);
                saveTask.setSlaInDays(taskDto.getSlaInDays());
                saveTask.setSlaRemainInDays(null);
                saveTask.setTaskKey(taskDto.getTaskKey());
                saveTask.setTaskType(taskDto.getTaskType());
                saveTask.setWkGrpId(taskDto.getWkGrpId());
                saveTask.setUserId(null);
                saveTask.setDateAssigned(new Date());
                saveTask.setAuditTrailDto(auditTrailDto);
                saveTask.setProcessUrl(TaskConsts.TASK_PROCESS_URL_RESCHEDULING_COMMON_POOL);
                saveTask.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
                saveTask.setApplicationNo(taskDto.getApplicationNo());
                saveTaskDtoList.add(saveTask);
            }
        }
        taskService.createTasks(saveTaskDtoList);
    }

    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId){
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
        for(ApplicationDto applicationDto : applicationDtos){
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        return hcsaSvcStageWorkingGroupDtos;
    }

    private List<String> getAppNoByGroup(Map<String, Map<String, String>> groupCheckUserIdMap, String workGroupCheck,
                                         Map<String, List<String>> inspectorAppNoMap, List<String> appNoList) {
        Map<String, String> checkUserId = groupCheckUserIdMap.get(workGroupCheck);
        if(checkUserId != null){
            for(Map.Entry<String, String> userIdMap : checkUserId.entrySet()){
                String userId = userIdMap.getValue();
                if(!StringUtil.isEmpty(userId)){
                    List<String> appNos = inspectorAppNoMap.get(userId);
                    appNoList.addAll(appNos);
                }
            }
        }
        Set<String> appNoSet = new HashSet<>(appNoList);
        appNoList = new ArrayList<>(appNoSet);
        return appNoList;
    }

    private List<String> getAppNoByInspector(Map<String, Map<String, String>> groupCheckUserIdMap, String workGroupCheck,
                                             Map<String, List<String>> inspectorAppNoMap, String inspectorCheck, List<String> appNoList) {
        Map<String, String> checkUserId = groupCheckUserIdMap.get(workGroupCheck);
        if(checkUserId != null){
            String userId = checkUserId.get(inspectorCheck);
            if(!StringUtil.isEmpty(userId)){
                appNoList = inspectorAppNoMap.get(userId);
            }
        }
        return appNoList;
    }

    private List<String> getServiceNameByAppNoList(List<String> applicationNos) {
        List<String> serviceNames = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty (applicationNos)){
            for(String appNo : applicationNos){
                ApplicationDto applicationDto = applicationClient.getAppByNo(appNo).getEntity();
                String serviceId = applicationDto.getServiceId();
                HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
                String serviceName = hcsaServiceDto.getSvcName();
                serviceNames.add(serviceName);
            }
        }
        return serviceNames;
    }

    private List<String> getInspectorsByAppNoList(List<String> applicationNos) {
        List<String> inspectorNames = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty (applicationNos)){
            for(String appNo : applicationNos){
                List<AppPremInspCorrelationDto> appPremInspCorrelationDtoList = inspectionTaskClient.getAppInspCorreByAppNoStatus(appNo, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
                if(!IaisCommonUtils.isEmpty(appPremInspCorrelationDtoList)){
                    for(AppPremInspCorrelationDto appPremInspCorrelationDto : appPremInspCorrelationDtoList){
                        String userId = appPremInspCorrelationDto.getUserId();
                        OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
                        inspectorNames.add(orgUserDto.getDisplayName());
                    }
                }
            }
        }
        return inspectorNames;
    }

    private List<String> filterPremisesAndFast(List<String> appNos, ReschedulingOfficerDto reschedulingOfficerDto, List<String> repeatAppNo, List<String> appStatusList) {
        List<String> applicationNos = IaisCommonUtils.genNewArrayList();
        Map<String, List<String>> samePremisesAppMap = reschedulingOfficerDto.getSamePremisesAppMap();
        if(samePremisesAppMap == null){
            samePremisesAppMap = IaisCommonUtils.genNewHashMap();
        }
        if(!IaisCommonUtils.isEmpty(appNos)){
            //duplicate removal
            for(int i = 0; i < appNos.size(); i++){
                String appNo = appNos.get(i);
                if(repeatAppNo.contains(appNo)){
                    continue;
                } else {
                    ApplicationDto applicationDto = applicationClient.getAppByNo(appNo).getEntity();
                    boolean fastTracking = applicationDto.isFastTracking();
                    if(fastTracking){
                        if(!appStatusList.contains(applicationDto.getStatus())){
                            repeatAppNo.add(appNo);
                            //put same Premises Application No or Fast tracking Map
                            List<String> appNoList = IaisCommonUtils.genNewArrayList();
                            appNoList.add(appNo);
                            samePremisesAppMap.put(appNo, appNoList);
                            applicationNos.add(appNo);
                        }
                    } else {
                        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(applicationDto.getId()).getEntity();
                        //get all same premises by Group
                        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = appPremisesCorrClient.getAppPremisesCorrelationsByPremises(appPremisesCorrelationDto.getId()).getEntity();
                        //put same Premises Application No or Fast tracking Map
                        List<String> appNoList = IaisCommonUtils.genNewArrayList();
                        appNoList = filterCancelAppByCorr(appNoList, appPremisesCorrelationDtos, appStatusList);
                        if(!IaisCommonUtils.isEmpty(appNoList)) {
                            samePremisesAppMap.put(appNo, appNoList);
                            repeatAppNo.addAll(appNoList);
                            applicationNos.add(appNo);
                        }
                    }
                }
            }
        }
        reschedulingOfficerDto.setSamePremisesAppMap(samePremisesAppMap);
        return applicationNos;
    }

    private List<String> filterCancelAppByCorr(List<String> appNoList, List<AppPremisesCorrelationDto> appPremisesCorrelationDtos, List<String> appStatusList) {
        if(!IaisCommonUtils.isEmpty(appPremisesCorrelationDtos)){
            boolean addFlag = true;
            for(int i = 0; i < appPremisesCorrelationDtos.size(); i++){
                String applicationId = appPremisesCorrelationDtos.get(i).getApplicationId();
                ApplicationDto applicationDto = applicationClient.getApplicationById(applicationId).getEntity();
                if(appStatusList.contains(applicationDto.getStatus())){
                    addFlag = false;
                }
            }
            if(addFlag) {
                for (int i = 0; i < appPremisesCorrelationDtos.size(); i++) {
                    String applicationId = appPremisesCorrelationDtos.get(i).getApplicationId();
                    ApplicationDto applicationDto = applicationClient.getApplicationById(applicationId).getEntity();
                    appNoList.add(applicationDto.getApplicationNo());
                }
            }
        }
        return appNoList;
    }

    private List<String> filterTimeLimit(List<AppPremInspCorrelationDto> appPremInspCorrelationDtoList) {
        List<String> appNos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appPremInspCorrelationDtoList)){
            for(AppPremInspCorrelationDto appPremInspCorrelationDto : appPremInspCorrelationDtoList) {
                String appNo = appPremInspCorrelationDto.getApplicationNo();
                AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremCorrByAppNo(appNo).getEntity();
                if(appPremisesCorrelationDto != null){
                    String appPremCorrId = appPremisesCorrelationDto.getId();
                    AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrId, InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                    if(appPremisesRecommendationDto != null) {
                        Date inspecDate = appPremisesRecommendationDto.getRecomInDate();
                        if(inspecDate != null){
                            Calendar inspecDateCal = Calendar.getInstance();
                            inspecDateCal.setTime(inspecDate);
                            inspecDateCal.set(Calendar.HOUR_OF_DAY, 0);
                            inspecDateCal.set(Calendar.MINUTE, 0);
                            inspecDateCal.set(Calendar.SECOND, 0);
                            inspecDateCal.set(Calendar.MILLISECOND, 0);

                            Date today = new Date();
                            Calendar todayCal = Calendar.getInstance();
                            todayCal.setTime(today);
                            todayCal.set(Calendar.HOUR_OF_DAY, 0);
                            todayCal.set(Calendar.MINUTE, 0);
                            todayCal.set(Calendar.SECOND, 0);
                            todayCal.set(Calendar.MILLISECOND, 0);
                            boolean dateBefore = inspecDateCal.getTime().before(todayCal.getTime());
                            if(!dateBefore){
                                appNos.add(appNo);
                            }
                        }
                    }
                }
            }
        }
        return appNos;
    }
}
