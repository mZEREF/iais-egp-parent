package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
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
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremInspCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppPremInspApptDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptCalendarStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschedulingOfficerDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschedulingOfficerQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
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
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MessageTemplateUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AppointmentUtil;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApptInspectionDateService;
import com.ecquaria.cloud.moh.iais.service.OfficersReSchedulingService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailHistoryCommonClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    private ApplicationService applicationService;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;

    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApptInspectionDateService apptInspectionDateService;

    @Autowired
    private AppointmentClient appointmentClient;

    @Autowired
    private CessationClient cessationClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Value("${iais.email.sender}")
    private String mailSender;

    @Autowired
    private EmailSmsClient emailSmsClient;
    @Autowired
    private EmailHistoryCommonClient emailHistoryCommonClient;


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
            if(workGroupName.contains("Inspection") && !workGroupName.contains("Approval")){
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
    public List<SelectOption> getInspectorByWorkGroupId(String workGroupId, ReschedulingOfficerDto reschedulingOfficerDto, String workGroupNo,String userId) {
        List<SelectOption> inspectorOption = IaisCommonUtils.genNewArrayList();
        Map<String, String> userIdMap = IaisCommonUtils.genNewHashMap();
        Map<String, Map<String, String>> groupCheckUserIdMap = reschedulingOfficerDto.getGroupCheckUserIdMap();
        if(groupCheckUserIdMap == null){
            groupCheckUserIdMap = IaisCommonUtils.genNewHashMap();
        }
        List<OrgUserDto> orgUserDtoList = organizationClient.getUsersByWorkGroupName(workGroupId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
        List<UserGroupCorrelationDto> userGroupCorrelationDtos=organizationClient.getUserGroupCorreByUserId(userId).getEntity();
        boolean isLead=false;
        for (UserGroupCorrelationDto ugd:userGroupCorrelationDtos
             ) {
            if(ugd.getGroupId().equals(workGroupId)&&ugd.getIsLeadForGroup().equals(1)){
                isLead=true;
            }
        }
        if(!isLead){
            orgUserDtoList.clear();
            OrgUserDto orgUserDto=organizationClient.retrieveOrgUserAccountById(userId).getEntity();
            if(orgUserDto!=null){
                orgUserDtoList.add(orgUserDto);
            }
        }

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
    public List<String> allInspectorFromGroupList(ReschedulingOfficerDto reschedulingOfficerDto, List<SelectOption> workGroupOption,String userId) {
        List<String> workGroupNos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(workGroupOption)){
            Map<String, String> workGroupMap = reschedulingOfficerDto.getWorkGroupIdMap();
            Map<String, List<SelectOption>> inspectorByGroup = IaisCommonUtils.genNewHashMap();
            for(SelectOption selectOption : workGroupOption){
                String workGroupNo = selectOption.getValue();
                String workGroupName = selectOption.getText();
                String workGroupId = workGroupMap.get(workGroupName);
                List<SelectOption> inspectorOption = getInspectorByWorkGroupId(workGroupId, reschedulingOfficerDto, workGroupNo, userId);
                inspectorByGroup.put(workGroupNo, inspectorOption);
                workGroupNos.add(workGroupNo);
            }
            reschedulingOfficerDto.setInspectorByGroup(inspectorByGroup);
        }
        return workGroupNos;
    }

    @Override
    public List<String> getAppNoByInspectorAndConditions(ReschedulingOfficerDto reschedulingOfficerDto,String loginUserId,List<SelectOption> workGroupOption) {
        //get not in appStatus
        String[] appStatusStr = AppointmentUtil.getNoReschdulingAppStatus();
        List<String> appStatusList = IaisCommonUtils.genNewArrayList();
        Collections.addAll(appStatusList, appStatusStr);

        List<String> appNoList = IaisCommonUtils.genNewArrayList();
        String workGroupCheck = reschedulingOfficerDto.getWorkGroupCheck();
        String wKGpId ="";
        for (SelectOption wkGp:workGroupOption) {
            if (wkGp.getValue().equals(workGroupCheck)){
                wKGpId = reschedulingOfficerDto.getWorkGroupIdMap().get(wkGp.getText());
                break;
            }
        }
        Map<String, Map<String, String>> groupCheckUserIdMap = reschedulingOfficerDto.getGroupCheckUserIdMap();
        Map<String, List<SelectOption>> inspectorByGroup = reschedulingOfficerDto.getInspectorByGroup();

        if(!StringUtil.isEmpty(workGroupCheck) && inspectorByGroup != null && groupCheckUserIdMap != null){
            List<String> leadIds = organizationClient.getInspectionLead(wKGpId).getEntity();
            //get group key and userId
            Map<String, String> userIdMap = groupCheckUserIdMap.get(workGroupCheck);
            List<SelectOption> inspectorOption = inspectorByGroup.get(workGroupCheck);

            if(!IaisCommonUtils.isEmpty(inspectorOption) && userIdMap != null){
                Map<String, List<String>> inspectorAppNoMap = reschedulingOfficerDto.getInspectorAppNoMap();
                if (inspectorAppNoMap == null) {
                    inspectorAppNoMap = IaisCommonUtils.genNewHashMap();
                }
                List<String> repeatAppNo = IaisCommonUtils.genNewArrayList();
                if(leadIds.contains(loginUserId)){
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
                } else {
                    for (SelectOption selectOption : inspectorOption) {
                        String inspectorValue = selectOption.getValue();
                        String userId = userIdMap.get(inspectorValue);
                        List<AppPremInspCorrelationDto> appPremInspCorrelationDtoList = inspectionTaskClient.getAppInspCorreByUserIdStatus(userId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
                        //filter rescheduling time limit
                        List<String> appNos = filterTimeLimit(appPremInspCorrelationDtoList);
                        //filter fast tracking and the same premises
                        appNos = filterPremisesAndFast(appNos, reschedulingOfficerDto, repeatAppNo, appStatusList);
                        inspectorAppNoMap.put(userId, appNos);
                        if(!IaisCommonUtils.isEmpty(appNos)&&userId.equals(loginUserId)) {
                            appNoList.addAll(appNos);
                        }
                    }
                }

                reschedulingOfficerDto.setInspectorAppNoMap(inspectorAppNoMap);
            }
        }

        return appNoList;
    }

    @Override
    @SearchTrack(catalog = "inspectionQuery", key = "reschedulingSearch")
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
        // ApplicationDto appDto = getApplicationByAppNo(appNo);
        // ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(appDto.getAppGrpId()).getEntity();
        // String licenseeId = applicationGroupDto.getLicenseeId();
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
                    applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT);
                    applicationDto.setAuditTrailDto(auditTrailDto);
                    applicationService.updateFEApplicaiton(applicationDto);
                }
            }
            reschedulingOfficerDto.setSaveAppNoList(appNoList);
            reschedulingOfficerDto.setTaskDtos(taskDtoList);
            reschedulingOfficerDto.setAuditTrailDto(auditTrailDto);
            inspectionTaskClient.reScheduleSaveRouteData(reschedulingOfficerDto);
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
                        for (String userId:userIds
                             ) {
                            try {
                                sendReschedulingEmailToInspector(applicationNo,userId);
                            } catch (Exception e) {
                                log.error(e.getMessage());
                            }
                        }
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
        /*String endDateStr = appointmentDto.getEndDate();
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
        }*/
        return appointmentDto;
    }

    @Override
    public List<ApptAppInfoShowDto> getReScheduleNewDateInfo(ReschedulingOfficerDto reschedulingOfficerDto) {
        List<ApptAppInfoShowDto> apptAppInfoShowDtos = IaisCommonUtils.genNewArrayList();
        String applicationNo = reschedulingOfficerDto.getAssignNo();
        if(!StringUtil.isEmpty(applicationNo)){
            AppointmentDto appointmentDto = new AppointmentDto();
            ApplicationDto applicationDto = getApplicationByAppNo(applicationNo);
            Map<String, List<String>> samePremisesAppMap = reschedulingOfficerDto.getSamePremisesAppMap();
            //service's licence end date Map
            Map<String, Date> svcIdLicDtMap = null;
            //get start date and end date by renewal flag
            boolean renewalDateFlag = false;
            //get start date and end date by premises
            if(applicationDto != null) {
                AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(applicationDto.getId()).getEntity();
                //get Applicant set start date and end date from appGroup
                appointmentDto = inspectionTaskClient.getApptStartEndDateByAppCorrId(appPremisesCorrelationDto.getId()).getEntity();
                if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationDto.getApplicationType())){
                    renewalDateFlag = true;
                    svcIdLicDtMap = IaisCommonUtils.genNewHashMap();
                }
            }
            //get insp date draft
            List<AppPremInspApptDraftDto> appPremInspApptDraftDtoList = IaisCommonUtils.genNewArrayList();
            //get start date and end date by Service and appShow info
            if(samePremisesAppMap != null) {
                List<String> appNoList = samePremisesAppMap.get(applicationNo);
                //set application no list
                appointmentDto.setAppNoList(appNoList);
                if(!IaisCommonUtils.isEmpty(appNoList)){
                    List<String> serviceIds = IaisCommonUtils.genNewArrayList();
                    for(String appNo : appNoList) {
                        ApptAppInfoShowDto apptAppInfoShowDto = new ApptAppInfoShowDto();
                        ApplicationDto appInfoDto = getApplicationByAppNo(appNo);
                        //set application data
                        apptAppInfoShowDto.setApplicationNo(appNo);
                        if(appInfoDto != null){
                            apptAppInfoShowDto.setStatus(appInfoDto.getStatus());
                            serviceIds.add(appInfoDto.getServiceId());
                            if(renewalDateFlag) {
                                //set service's licence end date
                                svcIdLicDtMap = setSvcIdLicDtMapByApp(appInfoDto, svcIdLicDtMap);
                            }
                        }
                        apptAppInfoShowDtos.add(apptAppInfoShowDto);
                        //set insp date draft
                        List<AppPremInspApptDraftDto> appPremInspApptDraftDtos = inspectionTaskClient.getInspApptDraftListByAppNo(appNo).getEntity();
                        if(!IaisCommonUtils.isEmpty(appPremInspApptDraftDtos)) {
                            appPremInspApptDraftDtoList.addAll(appPremInspApptDraftDtos);
                        }
                    }
                    appointmentDto.setSvcIdLicDtMap(svcIdLicDtMap);
                    appointmentDto.setServiceIds(serviceIds);
                }
            }
            //get Start date and End date when group no date
            if (appointmentDto.getStartDate() == null && appointmentDto.getEndDate() == null) {
                appointmentDto = hcsaConfigClient.getApptStartEndDateByService(appointmentDto).getEntity();
            }
            boolean dateFlag = getStartEndDateFlag(appointmentDto);
            reschedulingOfficerDto.setAppointmentDto(appointmentDto);
            if(IaisCommonUtils.isEmpty(appPremInspApptDraftDtoList) && dateFlag) {
                //set app data to show ,and set userId correlation app No. to save
                apptAppInfoShowDtos = setInfoByDateAndUserIdToSave(apptAppInfoShowDtos, reschedulingOfficerDto);
            } else if(!IaisCommonUtils.isEmpty(appPremInspApptDraftDtoList)) {
                //set app data to show ,and set userId correlation app No.By Inspection Date Draft
                apptAppInfoShowDtos = setInfoByDateAndUserIdByDraft(appPremInspApptDraftDtoList, apptAppInfoShowDtos, reschedulingOfficerDto);
            }
        }
        return apptAppInfoShowDtos;
    }

    private List<ApptAppInfoShowDto> setInfoByDateAndUserIdByDraft(List<AppPremInspApptDraftDto> appPremInspApptDraftDtoList, List<ApptAppInfoShowDto> apptAppInfoShowDtos,
                                                                   ReschedulingOfficerDto reschedulingOfficerDto) {
        for (ApptAppInfoShowDto apptAppInfoShowDto : apptAppInfoShowDtos) {
            //user uuId list
            List<String> userIds = IaisCommonUtils.genNewArrayList();
            //user name list
            List<String> userNameList = IaisCommonUtils.genNewArrayList();
            if(apptAppInfoShowDto != null) {
                String appNo = apptAppInfoShowDto.getApplicationNo();
                for (AppPremInspApptDraftDto appPremInspApptDraftDto : appPremInspApptDraftDtoList) {
                    if(appPremInspApptDraftDto != null) {
                        //set new inspection date string to show
                        reschedulingOfficerDto = getShowDraftDateTimeStringList(appPremInspApptDraftDto, reschedulingOfficerDto);
                        //set user uuId
                        String applicationNo = appPremInspApptDraftDto.getApplicationNo();
                        if (appNo.equals(applicationNo)) {
                            String userId = appPremInspApptDraftDto.getUserId();
                            userIds.add(userId);
                            //set user name
                            OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
                            if(orgUserDto != null) {
                                userNameList.add(orgUserDto.getDisplayName());
                            }
                        }
                    }
                }
                AppPremInspApptDraftDto appPremInspApptDraftDto = appPremInspApptDraftDtoList.get(0);
                //set insp start date
                apptAppInfoShowDto.setInspDate(appPremInspApptDraftDto.getStartDate());
                apptAppInfoShowDto.setInspEndDate(appPremInspApptDraftDto.getEndDate());
                //set apptRefNo
                List<String> apptRefNos = IaisCommonUtils.genNewArrayList();
                apptRefNos.add(appPremInspApptDraftDto.getApptRefNo());
                apptAppInfoShowDto.setApptRefNo(apptRefNos);
                //sort
                Collections.sort(userNameList);
                apptAppInfoShowDto.setUserIdList(userIds);
                apptAppInfoShowDto.setUserDisName(userNameList);
            }
        }
        return apptAppInfoShowDtos;
    }

    private ReschedulingOfficerDto getShowDraftDateTimeStringList(AppPremInspApptDraftDto appPremInspApptDraftDto, ReschedulingOfficerDto reschedulingOfficerDto) {
        if(reschedulingOfficerDto != null){
            List<String> newInspDates = IaisCommonUtils.genNewArrayList();
            if(appPremInspApptDraftDto != null) {
                String inspStartDate = apptDateToStringShow(appPremInspApptDraftDto.getStartDate());
                String inspEndDate = apptDateToStringShow(appPremInspApptDraftDto.getEndDate());
                String inspectionDate = inspStartDate + " - " + inspEndDate;
                newInspDates.add(inspectionDate);
            }
            reschedulingOfficerDto.setNewInspDates(newInspDates);
        }
        return reschedulingOfficerDto;
    }

    @Override
    public List<ApptAppInfoShowDto> setInfoByDateAndUserIdToSave(List<ApptAppInfoShowDto> apptAppInfoShowDtos,
                                                                  ReschedulingOfficerDto reschedulingOfficerDto) {
        try {
            AppointmentDto appointmentDto = reschedulingOfficerDto.getAppointmentDto();
            if (appointmentDto != null) {
                FeignResponseEntity<List<ApptRequestDto>> result = inspectionTaskClient.reScheduleNewDate(appointmentDto);
                Map<String, Collection<String>> headers = result.getHeaders();
                //Has it been blown up
                if (headers != null && StringUtil.isEmpty(headers.get("fusing"))) {
                    List<ApptRequestDto> apptRequestDtos = result.getEntity();
                    if (!IaisCommonUtils.isEmpty(apptRequestDtos)) {
                        for (ApptRequestDto apptRequestDto : apptRequestDtos) {
                            //set new inspection date string to show
                            reschedulingOfficerDto = getShowDateTimeStringList(apptRequestDto, reschedulingOfficerDto);
                        }
                        //There's only one piece of data now
                        ApptRequestDto apptReDto = apptRequestDtos.get(0);
                        //set user with appNo and save inspection date draft
                        apptAppInfoShowDtos = setUserWithAppNo(apptReDto, apptAppInfoShowDtos);
                    } else {
                        reschedulingOfficerDto.setNewInspDates(null);
                    }
                } else {
                    reschedulingOfficerDto.setNewInspDates(null);
                }
            }
        } catch(Exception e){
                log.error(e.getMessage(), e);
        }
        return apptAppInfoShowDtos;
    }

    @Override
    public void sendEmailToApplicant(ReschedulingOfficerDto reschedulingOfficerDto) {
        String appNo = reschedulingOfficerDto.getAssignNo();
        if(!StringUtil.isEmpty(appNo)){
            //get Official Email Address
            String address1 = systemParamConfig.getSystemAddressOne();
            ApplicationDto applicationDto = getApplicationByAppNo(appNo);
            //get old inspection start date
            Date inspDate = getOldInspectionStartDate(applicationDto);
            String dateStr = Formatter.formatDateTime(inspDate, "dd/MM/yyyy");
            String dateTime = Formatter.formatDateTime(inspDate, "HH:mm:ss");
            //url
            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
            if(applicationDto != null) {
                String applicantId;
                if(!ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationDto.getApplicationType())) {
                    ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();
                    applicantId = applicationGroupDto.getSubmitBy();
                } else {
                    applicantId = apptInspectionDateService.getAppSubmitByWithLicId(applicationDto.getOriginLicenceId());
                }
                OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicantId).getEntity();
                String applicantName = orgUserDto.getDisplayName();
                AppGrpPremisesDto appGrpPremisesDto = cessationClient.getAppGrpPremisesDtoByAppId(applicationDto.getId()).getEntity();
                Map<String ,Object> map = IaisCommonUtils.genNewHashMap();
                map.put("applicant", applicantName);
                String hciName = appGrpPremisesDto.getHciName();
                if(!StringUtil.isEmpty(hciName)){
                    map.put("hciName", hciName);
                }
                map.put("date", dateStr);
                map.put("dateTime", dateTime);
                map.put("newDate", "-");
                map.put("newDateTime", "-");
                map.put("systemLink", loginUrl);
                map.put("address", address1);
                //msg service code
                Map<String, List<String>> samePremisesAppMap = reschedulingOfficerDto.getSamePremisesAppMap();
                List<String> serviceCodes = IaisCommonUtils.genNewArrayList();
                if(samePremisesAppMap != null) {
                    serviceCodes = msgSvcCodeByAppNos(appNo, samePremisesAppMap, serviceCodes);
                }
                try{
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RE_SCHEDULING_INSPECTION_DATE);
                    emailParam.setTemplateContent(map);
                    emailParam.setQueryCode(appNo);
                    emailParam.setReqRefNum(appNo);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                    emailParam.setRefId(appNo);
                    notificationHelper.sendNotification(emailParam);
                    EmailParam smsParam = new EmailParam();
                    smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RE_SCHEDULING_INSPECTION_DATE_SMS);
                    smsParam.setSubject("MOH HALP - Rescheduling of inspection date");
                    smsParam.setQueryCode(appNo);
                    smsParam.setReqRefNum(appNo);
                    smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                    smsParam.setRefId(appNo);
                    notificationHelper.sendNotification(smsParam);
                    EmailParam msgParam = new EmailParam();
                    msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RE_SCHEDULING_INSPECTION_DATE_MSG);
                    msgParam.setTemplateContent(map);
                    msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    msgParam.setQueryCode(appNo);
                    msgParam.setReqRefNum(appNo);
                    msgParam.setRefId(appNo);
                    msgParam.setSvcCodeList(serviceCodes);
                    notificationHelper.sendNotification(msgParam);
                } catch (Exception e){
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public void sendEmailToApplicantNewDate(ReschedulingOfficerDto reschedulingOfficerDto,ApptAppInfoShowDto appInfoShowDto) {
        String appNo = reschedulingOfficerDto.getAssignNo();
        if(!StringUtil.isEmpty(appNo)){
            //get Official Email Address
            String address1 = systemParamConfig.getSystemAddressOne();
            ApplicationDto applicationDto = getApplicationByAppNo(appNo);
            //get old inspection start date
            Date inspDate = getOldInspectionStartDate(applicationDto);
            String dateStr = Formatter.formatDateTime(inspDate, "dd/MM/yyyy");
            String dateTime = Formatter.formatDateTime(inspDate, "HH:mm:ss");

            String newDateStartStr = Formatter.formatDateTime(appInfoShowDto.getInspDate(), "dd/MM/yyyy");
            String newDateStartTime = Formatter.formatDateTime(appInfoShowDto.getInspDate(), "HH:mm:ss");
//            String newDateEndStr = Formatter.formatDateTime(appInfoShowDto.getInspEndDate(), "dd/MM/yyyy");
//            String newDateEndTime = Formatter.formatDateTime(appInfoShowDto.getInspEndDate(), "HH:mm:ss");
//            String newDateTime="";
//            if(newDateStartStr.equals(newDateEndStr)){
//                newDateTime=newDateStartTime+" - "+newDateEndTime;
//            }else {
//                newDateTime=newDateStartTime+" - "+newDateEndStr+" at "+newDateEndTime;
//            }

            //url
            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
            if(applicationDto != null) {
                ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();
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
                map.put("date", dateStr);
                map.put("dateTime", dateTime);
                map.put("hasNewDate", "true");
                map.put("newDate", newDateStartStr);
                map.put("newDateTime", newDateStartTime);
                map.put("systemLink", loginUrl);
                map.put("address", address1);
                //msg service code
                Map<String, List<String>> samePremisesAppMap = reschedulingOfficerDto.getSamePremisesAppMap();
                List<String> serviceCodes = IaisCommonUtils.genNewArrayList();
                if(samePremisesAppMap != null) {
                    serviceCodes = msgSvcCodeByAppNos(appNo, samePremisesAppMap, serviceCodes);
                }
                try{
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RE_SCHEDULING_INSPECTION_DATE);
                    emailParam.setTemplateContent(map);
                    emailParam.setQueryCode(appNo);
                    emailParam.setReqRefNum(appNo);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                    emailParam.setRefId(appNo);
                    notificationHelper.sendNotification(emailParam);
                    EmailParam smsParam = new EmailParam();
                    smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RE_SCHEDULING_INSPECTION_DATE_SMS);
                    smsParam.setSubject("MOH HALP - Rescheduling of inspection date");
                    smsParam.setQueryCode(appNo);
                    smsParam.setReqRefNum(appNo);
                    smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                    smsParam.setRefId(appNo);
                    notificationHelper.sendNotification(smsParam);
                    EmailParam msgParam = new EmailParam();
                    msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RE_SCHEDULING_INSPECTION_DATE_MSG);
                    msgParam.setTemplateContent(map);
                    msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    msgParam.setQueryCode(appNo);
                    msgParam.setReqRefNum(appNo);
                    msgParam.setRefId(appNo);
                    msgParam.setSvcCodeList(serviceCodes);
                    notificationHelper.sendNotification(msgParam);
                } catch (Exception e){
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private List<String> msgSvcCodeByAppNos(String appNo, Map<String, List<String>> samePremisesAppMap, List<String> serviceCodes) {
        List<String> appNoList = samePremisesAppMap.get(appNo);
        for(String applicationNo : appNoList){
            if(!StringUtil.isEmpty(applicationNo)) {
                ApplicationDto applicationDto = getApplicationByAppNo(applicationNo);
                String serviceId = applicationDto.getServiceId();
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
                if(hcsaServiceDto != null) {
                    String serviceCode = hcsaServiceDto.getSvcCode();
                    if (!StringUtil.isEmpty(serviceCode)) {
                        serviceCodes.add(serviceCode);
                    }
                }
            }
        }
        return serviceCodes;
    }

    @Override
    public String changeInspectorAndDate(ReschedulingOfficerDto reschedulingOfficerDto, List<ApptAppInfoShowDto> apptReSchAppInfoShowDtos) {
        //check app status
        boolean appStatusFlag = getAppStatusNotInRfi(reschedulingOfficerDto);
        //get auditTrailDto
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        if(appStatusFlag) {
            if(!IaisCommonUtils.isEmpty(apptReSchAppInfoShowDtos) && reschedulingOfficerDto != null) {
                //get new apptRefNo and cancel old
                ApptCalendarStatusDto apptCalendarStatusDto = updateApptRefNoForCalendar(reschedulingOfficerDto, apptReSchAppInfoShowDtos);
                //update any data
                List<AppPremisesInspecApptDto> appPremInspecApptUpdateDtos = IaisCommonUtils.genNewArrayList();
                List<AppPremisesInspecApptDto> appPremInspecApptCreateDtos = IaisCommonUtils.genNewArrayList();
                //need delete inspection date draft
                List<String> apptRefNos = IaisCommonUtils.genNewArrayList();
                for (ApptAppInfoShowDto apptAppInfoShowDto : apptReSchAppInfoShowDtos) {
                    if(apptAppInfoShowDto != null) {
                        String appNo = apptAppInfoShowDto.getApplicationNo();
                        //set appt RefNos
                        apptRefNos.addAll(apptAppInfoShowDto.getApptRefNo());
                        //get application data
                        ApplicationDto applicationDto = applicationClient.getAppByNo(appNo).getEntity();
                        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(applicationDto.getId()).getEntity();
                        //update and create apptRefNo;
                        List<AppPremisesInspecApptDto> appPremisesInspecApptDtos = inspectionTaskClient.getSystemDtosByAppPremCorrId(appPremisesCorrelationDto.getId()).getEntity();
                        updateAndCreateApptRefNo(appPremisesInspecApptDtos, appPremInspecApptUpdateDtos, appPremInspecApptCreateDtos, apptAppInfoShowDto,
                                auditTrailDto, reschedulingOfficerDto.getAppointmentDto());
                        //synchronization update FE apptRefNo
                        createFeAppPremisesInspecApptDto(appPremInspecApptUpdateDtos);
                        //cancel the original inspector and create new
                        List<AppPremInspCorrelationDto> appPremInspCorrelationDtoList = inspectionTaskClient.getAppInspCorreByAppNoStatus(appNo, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
                        updateAppNoCorrelationInspectors(appPremInspCorrelationDtoList, apptAppInfoShowDto, appNo, auditTrailDto);
                        //update task
                        List<TaskDto> taskDtos = organizationClient.getCurrTaskByRefNo(appPremisesCorrelationDto.getId()).getEntity();
                        updateUserForTask(taskDtos, apptAppInfoShowDto);
                        //update recommendation inspection date
                        try{
                            sendEmailToApplicantNewDate(reschedulingOfficerDto,apptAppInfoShowDto);
                        }catch (Exception e){
                            log.error(e.getMessage());
                        }
                        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationDto.getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                        createOrUpdateRecommendation(appPremisesRecommendationDto, appPremisesCorrelationDto.getId(), apptAppInfoShowDto.getInspDate());
                        try {
                            for (String userId:apptAppInfoShowDto.getUserIdList()
                                 ) {
                                sendReschedulingEmailToInspector(appNo,userId);
                            }
                        }catch (Exception e){
                            log.error(e.getMessage());
                        }
                    }
                }
                //save new apptRefNo and cancel old
                cancelOrConfirmApptDate(apptCalendarStatusDto);
                //delete draft
                inspectionTaskClient.deleteInspDateDraftByApptRefNo(apptRefNos);
            }
            return AppConsts.SUCCESS;
        } else {
            return AppConsts.FAIL;
        }
    }

    @Override
    public void sendReschedulingEmailToInspector(String appNo,String userId) throws IOException {
        Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
        AppGrpPremisesEntityDto appGrpPremisesEntityDto=applicationClient.getPremisesByAppNo(appNo).getEntity();
        OrgUserDto orgUserDto= organizationClient.retrieveOrgUserAccountById(userId).getEntity();
        emailMap.put("InspectorName", orgUserDto.getDisplayName());
        emailMap.put("DDMMYYYY", Formatter.formatDate(new Date()));
        emailMap.put("time", Formatter.formatTime(new Date()));
        emailMap.put("HCI_Name", appGrpPremisesEntityDto.getHciName());
        emailMap.put("HCI_Code", appGrpPremisesEntityDto.getHciCode());
        String add = MiscUtil.getAddressForApp(appGrpPremisesEntityDto.getBlkNo(),appGrpPremisesEntityDto.getStreetName(),appGrpPremisesEntityDto.getBuildingName(),appGrpPremisesEntityDto.getFloorNo(),appGrpPremisesEntityDto.getUnitNo(),appGrpPremisesEntityDto.getPostalCode(),appGrpPremisesEntityDto.getAppPremisesOperationalUnitDtos());
        emailMap.put("premises_address", add);
        emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
        emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");

        String emailContent = getEmailContent(MsgTemplateConstants.MSG_TEMPLATE_RESCHEDULING_SUCCESSFULLY_TO_INSP,emailMap);
        String smsContent = getEmailContent(MsgTemplateConstants. MSG_TEMPLATE_RESCHEDULING_SUCCESSFULLY_TO_INSP_SMS ,emailMap);
        String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RESCHEDULING_SUCCESSFULLY_TO_INSP,null);

        EmailDto emailDto = new EmailDto();
        List<String> receiptEmail=IaisCommonUtils.genNewArrayList();
        receiptEmail.add(orgUserDto.getEmail());
        List<String> mobile = IaisCommonUtils.genNewArrayList();
        mobile.add(orgUserDto.getMobileNo());
        emailDto.setReceipts(receiptEmail);
        emailDto.setContent(emailContent);
        emailDto.setSubject(emailSubject);
        emailDto.setSender(this.mailSender);
        emailDto.setClientQueryCode(appNo);
        emailDto.setReqRefNum(appNo);
        if(orgUserDto.getEmail()!=null){
            emailSmsClient.sendEmail(emailDto, null);
        }

        SmsDto smsDto = new SmsDto();
        smsDto.setSender(mailSender);
        smsDto.setContent(smsContent);
        smsDto.setOnlyOfficeHour(false);
        smsDto.setReceipts(mobile);
        smsDto.setReqRefNum(appNo);
        if(orgUserDto.getMobileNo()!=null){
            emailHistoryCommonClient.sendSMS(mobile, smsDto, appNo);
        }
    }

    @Override
    public ReschedulingOfficerDto setNewInspStartDate(List<ApptAppInfoShowDto> apptReSchAppInfoShowDtos, ReschedulingOfficerDto reschedulingOfficerDto) {
        if(reschedulingOfficerDto != null && !IaisCommonUtils.isEmpty(apptReSchAppInfoShowDtos)) {
            ApptAppInfoShowDto apptAppInfoShowDto = apptReSchAppInfoShowDtos.get(0);
            AppointmentDto appointmentDto = reschedulingOfficerDto.getAppointmentDto();
            if(apptAppInfoShowDto != null) {
                //get new start date
                Date endDate = apptAppInfoShowDto.getInspEndDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDate);
                calendar.add(Calendar.DATE, 1);
                Date newStartDate = calendar.getTime();
                //get map
                HashMap<String, Date> userSpecMap = appointmentDto.getUserSpecMap();
                if(userSpecMap == null) {
                    userSpecMap = IaisCommonUtils.genNewHashMap();
                }
                for(ApptAppInfoShowDto appInfoShowDto : apptReSchAppInfoShowDtos) {
                    List<String> userIds = appInfoShowDto.getUserIdList();
                    if(!IaisCommonUtils.isEmpty(userIds)) {
                        for(String userId : userIds) {
                            OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
                            //login Id and date
                            userSpecMap.put(orgUserDto.getUserId(), newStartDate);
                        }
                    }
                }
                appointmentDto.setUserSpecMap(userSpecMap);
            }
            reschedulingOfficerDto.setAppointmentDto(appointmentDto);
        }
        return reschedulingOfficerDto;
    }

    @Override
    public List<String> getOldApptRefNos(List<ApptAppInfoShowDto> apptReSchAppInfoShowDtos) {
        List<String> apptRefNos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(apptReSchAppInfoShowDtos)) {
            for(ApptAppInfoShowDto apptAppInfoShowDto : apptReSchAppInfoShowDtos) {
                if(apptAppInfoShowDto != null) {
                    apptRefNos.addAll(apptAppInfoShowDto.getApptRefNo());
                }
            }
        }
        return apptRefNos;
    }

    @Override
    public void confirmAndCancelApptRefNo(List<String> confirmApptRefNo, List<String> cancelApptRefNos) {
        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        if(!IaisCommonUtils.isEmpty(confirmApptRefNo)) {
            apptCalendarStatusDto.setCancelRefNums(confirmApptRefNo);
        }
        if(!IaisCommonUtils.isEmpty(cancelApptRefNos)) {
            apptCalendarStatusDto.setCancelRefNums(cancelApptRefNos);
        }
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        appointmentClient.updateUserCalendarStatus(apptCalendarStatusDto);
        //cancel draft
        inspectionTaskClient.deleteInspDateDraftByApptRefNo(cancelApptRefNos);
    }


    private String getEmailContent(String templateId, Map<String, Object> subMap){
        String mesContext = "-";
        if(!StringUtil.isEmpty(templateId)){
            MsgTemplateDto emailTemplateDto =notificationHelper.getMsgTemplate(templateId);
            if(emailTemplateDto != null){
                try {
                    if(!IaisCommonUtils.isEmpty(subMap)){
                        mesContext = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getMessageContent(), subMap);
                    }
                    //replace num
                    mesContext = MessageTemplateUtil.replaceNum(mesContext);
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
        return mesContext;
    }

    private String getEmailSubject(String templateId, Map<String, Object> subMap){
        String subject = "-";
        if(!StringUtil.isEmpty(templateId)){
            MsgTemplateDto emailTemplateDto =notificationHelper.getMsgTemplate(templateId);
            if(emailTemplateDto != null){
                try {
                    if(!IaisCommonUtils.isEmpty(subMap)){
                        subject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(),subMap);
                    }else{
                        subject = emailTemplateDto.getTemplateName();
                    }
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
        return subject;
    }

    private void updateUserForTask(List<TaskDto> taskDtos, ApptAppInfoShowDto apptAppInfoShowDto) {
        if(!IaisCommonUtils.isEmpty(taskDtos) && apptAppInfoShowDto != null) {
            List<String> userIdList = apptAppInfoShowDto.getUserIdList();
            if(!IaisCommonUtils.isEmpty(userIdList)) {
                log.info(StringUtil.changeForLog("Rescheduling tasks size ====" + taskDtos.size()));
                log.info(StringUtil.changeForLog("Rescheduling users size ====" + userIdList.size()));
                for (int i = 0; i < taskDtos.size(); i++) {
                    if(taskDtos.get(i) != null) {
                        taskDtos.get(i).setUserId(userIdList.get(i));
                        taskService.updateTask(taskDtos.get(i));
                    }
                }
            }
        }
    }

    private void updateAppNoCorrelationInspectors(List<AppPremInspCorrelationDto> appPremInspCorrelationDtoList, ApptAppInfoShowDto apptAppInfoShowDto,
                                                  String appNo, AuditTrailDto auditTrailDto) {
        if(!IaisCommonUtils.isEmpty(appPremInspCorrelationDtoList) && apptAppInfoShowDto != null) {
            for(AppPremInspCorrelationDto appPremInspCorrelationDto : appPremInspCorrelationDtoList) {
                appPremInspCorrelationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                appPremInspCorrelationDto.setAuditTrailDto(auditTrailDto);
                inspectionTaskClient.updateAppPremInspCorrelationDto(appPremInspCorrelationDto);
            }
            //user uuid
            List<String> userIds = apptAppInfoShowDto.getUserIdList();
            if(!IaisCommonUtils.isEmpty(userIds)) {
                List<AppPremInspCorrelationDto> appPremInspCorrCreateDtos = IaisCommonUtils.genNewArrayList();
                for (String userId : userIds) {
                    //create appInspCorrelation and task
                    AppPremInspCorrelationDto appPremInspCorrCreateDto = new AppPremInspCorrelationDto();
                    appPremInspCorrCreateDto.setApplicationNo(appNo);
                    appPremInspCorrCreateDto.setId(null);
                    appPremInspCorrCreateDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    appPremInspCorrCreateDto.setUserId(userId);
                    appPremInspCorrCreateDto.setAuditTrailDto(auditTrailDto);
                    appPremInspCorrCreateDtos.add(appPremInspCorrCreateDto);
                }
                inspectionTaskClient.createAppPremInspCorrelationDto(appPremInspCorrCreateDtos);
            }
        }
    }

    private void updateAndCreateApptRefNo(List<AppPremisesInspecApptDto> appPremisesInspecApptDtos, List<AppPremisesInspecApptDto> appPremInspecApptUpdateDtos,
                                          List<AppPremisesInspecApptDto> appPremInspecApptCreateDtos, ApptAppInfoShowDto apptAppInfoShowDto, AuditTrailDto auditTrailDto,
                                          AppointmentDto appointmentDto) {
        if(!IaisCommonUtils.isEmpty(appPremisesInspecApptDtos)) {
            List<String> apptRefNos = apptAppInfoShowDto.getApptRefNo();
            if(!IaisCommonUtils.isEmpty(apptRefNos)) {
                String apptRefNo = apptRefNos.get(0);
                for (AppPremisesInspecApptDto appPremisesInspecApptDto : appPremisesInspecApptDtos) {
                    //old
                    appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    appPremisesInspecApptDto.setAuditTrailDto(auditTrailDto);
                    //update
                    appPremisesInspecApptDto = applicationClient.updateAppPremisesInspecApptDto(appPremisesInspecApptDto).getEntity();
                    //new
                    AppPremisesInspecApptDto createAppPremInspDto = new AppPremisesInspecApptDto();
                    createAppPremInspDto.setAppCorrId(appPremisesInspecApptDto.getAppCorrId());
                    createAppPremInspDto.setApptRefNo(apptRefNo);
                    createAppPremInspDto.setSpecificInspDate(null);
                    createAppPremInspDto.setId(null);
                    createAppPremInspDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    createAppPremInspDto.setReschedulingCount(appPremisesInspecApptDto.getReschedulingCount());
                    if(appointmentDto != null) {
                        if (appointmentDto.getStartDate() != null) {
                            try {
                                createAppPremInspDto.setStartDate(Formatter.parseDateTime(appointmentDto.getStartDate(), AppConsts.DEFAULT_DATE_TIME_FORMAT));
                            } catch (ParseException e) {
                                log.error(e.getMessage(), e);
                            }
                        }
                        if (appointmentDto.getEndDate() != null) {
                            try {
                                createAppPremInspDto.setEndDate(Formatter.parseDateTime(appointmentDto.getEndDate(), AppConsts.DEFAULT_DATE_TIME_FORMAT));
                            } catch (ParseException e) {
                                log.error(e.getMessage(), e);
                            }
                        }
                    }
                    createAppPremInspDto.setAuditTrailDto(auditTrailDto);
                    //add
                    appPremInspecApptUpdateDtos.add(appPremisesInspecApptDto);
                    appPremInspecApptCreateDtos.add(createAppPremInspDto);
                }
                appPremInspecApptCreateDtos = applicationClient.createAppPremisesInspecApptDto(appPremInspecApptCreateDtos).getEntity();
                //synchronization update FE apptRefNo
                createFeAppPremisesInspecApptDto(appPremInspecApptCreateDtos);
            }
        }
    }

    private ApptCalendarStatusDto updateApptRefNoForCalendar(ReschedulingOfficerDto reschedulingOfficerDto,
                                                             List<ApptAppInfoShowDto> apptReSchAppInfoShowDtos) {
        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        if(reschedulingOfficerDto != null) {
            //confirm apptRefNo
            ApptAppInfoShowDto apptAppInfoShowDto = apptReSchAppInfoShowDtos.get(0);
            if(apptAppInfoShowDto != null) {
                apptCalendarStatusDto.setConfirmRefNums(apptAppInfoShowDto.getApptRefNo());
            }
            //cancel apptRefNo
            String appNo = reschedulingOfficerDto.getAssignNo();
            ApplicationDto applicationDto = applicationClient.getAppByNo(appNo).getEntity();
            AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(applicationDto.getId()).getEntity();
            List<AppPremisesInspecApptDto> appPremisesInspecApptDtos = inspectionTaskClient.getSystemDtosByAppPremCorrId(appPremisesCorrelationDto.getId()).getEntity();
            List<String> cancelRefNo = IaisCommonUtils.genNewArrayList();
            for (AppPremisesInspecApptDto inspecApptDto : appPremisesInspecApptDtos) {
                if(inspecApptDto != null) {
                    cancelRefNo.add(inspecApptDto.getApptRefNo());
                }
            }
            apptCalendarStatusDto.setCancelRefNums(cancelRefNo);
            apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        }
        return apptCalendarStatusDto;
    }

    private boolean getAppStatusNotInRfi(ReschedulingOfficerDto reschedulingOfficerDto) {
        if(reschedulingOfficerDto != null) {
            Map<String, List<String>> samePremisesAppMap = reschedulingOfficerDto.getSamePremisesAppMap();
            if (samePremisesAppMap != null && !StringUtil.isEmpty(reschedulingOfficerDto.getAssignNo())) {
                List<String> appNoList = samePremisesAppMap.get(reschedulingOfficerDto.getAssignNo());
                if(!IaisCommonUtils.isEmpty(appNoList)) {
                    for(String appNo : appNoList) {
                        if(!StringUtil.isEmpty(appNo)) {
                            ApplicationDto applicationDto = getApplicationByAppNo(appNo);
                            if(applicationDto != null) {
                                String appStatus = applicationDto.getStatus();
                                if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(appStatus) ||
                                        ApplicationConsts.APPLICATION_STATUS_PENDING_CLARIFICATION.equals(appStatus)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private Date getOldInspectionStartDate(ApplicationDto applicationDto) {
        Date inspDate = new Date();
        if(applicationDto != null){
            AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremCorrByAppNo(applicationDto.getApplicationNo()).getEntity();
            if(appPremisesCorrelationDto != null){
                AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType
                        (appPremisesCorrelationDto.getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                if(appPremisesRecommendationDto != null && appPremisesRecommendationDto.getRecomInDate() != null){
                    inspDate = appPremisesRecommendationDto.getRecomInDate();
                }
            }
        }
        return inspDate;
    }

    private List<ApptAppInfoShowDto> setUserWithAppNo(ApptRequestDto apptReDto, List<ApptAppInfoShowDto> apptAppInfoShowDtos) {
        if(apptReDto != null && !IaisCommonUtils.isEmpty(apptAppInfoShowDtos)) {
            List<String> apptRefNos = IaisCommonUtils.genNewArrayList();
            String apptRefNo = apptReDto.getApptRefNo();
            apptRefNos.add(apptRefNo);
            List<ApptUserCalendarDto> userClandars = apptReDto.getUserClandars();
            //set user with App No.
            if(!IaisCommonUtils.isEmpty(userClandars)){
                Date inspDate = userClandars.get(0).getStartSlot().get(0);
                int endTimeSize = userClandars.get(0).getEndSlot().size();
                Date inspEndDate = userClandars.get(0).getEndSlot().get(endTimeSize - 1);
                Map<String, List<String>> appNoUserLoginId = getAppNoUserLoginIdByUserClandars(userClandars);
                if(appNoUserLoginId != null) {
                    for (ApptAppInfoShowDto apptAppInfoShowDto : apptAppInfoShowDtos) {
                        if(apptAppInfoShowDto != null) {
                            //set appointment ref NO.
                            apptAppInfoShowDto.setApptRefNo(apptRefNos);
                            List<String> userLoginIds = appNoUserLoginId.get(apptAppInfoShowDto.getApplicationNo());
                            //sort for show
                            Collections.sort(userLoginIds);
                            //set user
                            apptAppInfoShowDto = setUserIdByLoginIds(apptAppInfoShowDto, userLoginIds);
                            apptAppInfoShowDto.setInspDate(inspDate);
                            apptAppInfoShowDto.setInspEndDate(inspEndDate);
                            //create inspection date Draft
                            List<String> userIds = apptAppInfoShowDto.getUserIdList();
                            List<AppPremInspApptDraftDto> appPremInspApptDraftDtos = getInspecDateDraftList(userIds, apptReDto, apptAppInfoShowDto);
                            inspectionTaskClient.createAppPremisesInspecApptDto(appPremInspApptDraftDtos).getEntity();
                        }
                    }
                }
            }
        }
        return apptAppInfoShowDtos;
    }

    private List<AppPremInspApptDraftDto> getInspecDateDraftList(List<String> userIds, ApptRequestDto apptReDto, ApptAppInfoShowDto apptAppInfoShowDto) {
        List<AppPremInspApptDraftDto> appPremInspApptDraftDtos = IaisCommonUtils.genNewArrayList();
        //get date
        int endTimeSize = apptReDto.getUserClandars().get(0).getEndSlot().size();
        Date inspStartDate = apptReDto.getUserClandars().get(0).getStartSlot().get(0);
        Date inspEndDate = apptReDto.getUserClandars().get(0).getEndSlot().get(endTimeSize - 1);
        if(!IaisCommonUtils.isEmpty(userIds)) {
            for (String userId : userIds) {
                if (!StringUtil.isEmpty(userId)) {
                    //set data
                    AppPremInspApptDraftDto appPremInspApptDraftDto = new AppPremInspApptDraftDto();
                    appPremInspApptDraftDto.setApplicationNo(apptAppInfoShowDto.getApplicationNo());
                    appPremInspApptDraftDto.setApptRefNo(apptReDto.getApptRefNo());
                    appPremInspApptDraftDto.setStartDate(inspStartDate);
                    appPremInspApptDraftDto.setEndDate(inspEndDate);
                    appPremInspApptDraftDto.setUserId(userId);
                    appPremInspApptDraftDtos.add(appPremInspApptDraftDto);
                }
            }
        }
        return appPremInspApptDraftDtos;
    }

    private ApptAppInfoShowDto setUserIdByLoginIds(ApptAppInfoShowDto apptAppInfoShowDto, List<String> userLoginIds) {
        if(!IaisCommonUtils.isEmpty(userLoginIds)) {
            List<String> userIdList = IaisCommonUtils.genNewArrayList();
            List<String> userNameList = IaisCommonUtils.genNewArrayList();
            for(String userLoginId : userLoginIds) {
                if(!StringUtil.isEmpty(userLoginId)) {
                    OrgUserDto orgUserDto = organizationClient.retrieveOneOrgUserAccount(userLoginId).getEntity();
                    if(orgUserDto != null) {
                        userIdList.add(orgUserDto.getId());
                        userNameList.add(orgUserDto.getDisplayName());
                    }
                }
            }
            Set<String> userNameSet = new HashSet<>(userNameList);
            Set<String> userIdSet = new HashSet<>(userIdList);
            userNameList = new ArrayList<>(userNameSet);
            userIdList = new ArrayList<>(userIdSet);
            Collections.sort(userNameList);
            Collections.sort(userIdList);
            apptAppInfoShowDto.setUserDisName(userNameList);
            apptAppInfoShowDto.setUserIdList(userIdList);
        }
        return apptAppInfoShowDto;
    }

    private Map<String, List<String>> getAppNoUserLoginIdByUserClandars(List<ApptUserCalendarDto> userClandars) {
        Map<String, List<String>> appNoUserLoginId = IaisCommonUtils.genNewHashMap();
        if(!IaisCommonUtils.isEmpty(userClandars)) {
            for (ApptUserCalendarDto apptUserCalendarDto : userClandars) {
                if(apptUserCalendarDto != null) {
                    String appNo = apptUserCalendarDto.getAppNo();
                    if(!StringUtil.isEmpty(appNo)) {
                        List<String> userLoginId = appNoUserLoginId.get(appNo);
                        if(IaisCommonUtils.isEmpty(userLoginId)){
                           userLoginId = IaisCommonUtils.genNewArrayList();
                        }
                        userLoginId.add(apptUserCalendarDto.getLoginUserId());
                        appNoUserLoginId.put(appNo, userLoginId);
                    }
                }
            }
        }
        return appNoUserLoginId;
    }

    private ReschedulingOfficerDto getShowDateTimeStringList(ApptRequestDto apptRequestDto, ReschedulingOfficerDto reschedulingOfficerDto) {
        if(reschedulingOfficerDto != null){
            List<String> newInspDates;
            if(IaisCommonUtils.isEmpty(reschedulingOfficerDto.getNewInspDates())){
                newInspDates = IaisCommonUtils.genNewArrayList();
            } else {
                newInspDates = reschedulingOfficerDto.getNewInspDates();
            }
            if(apptRequestDto != null) {
                List<ApptUserCalendarDto> userClandars = apptRequestDto.getUserClandars();
                if(!IaisCommonUtils.isEmpty(userClandars)) {
                    int endTimeSize = userClandars.get(0).getEndSlot().size();
                    String inspStartDate = apptDateToStringShow(userClandars.get(0).getStartSlot().get(0));
                    String inspEndDate = apptDateToStringShow(userClandars.get(0).getEndSlot().get(endTimeSize - 1));
                    String inspectionDate = inspStartDate + " - " + inspEndDate;
                    newInspDates.add(inspectionDate);
                }
            }
            reschedulingOfficerDto.setNewInspDates(newInspDates);
        }
        return reschedulingOfficerDto;
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

    private boolean getStartEndDateFlag(AppointmentDto appointmentDto) {
        Date today = new Date();
        String todayStr = Formatter.formatDateTime(today, AppConsts.DEFAULT_DATE_FORMAT);
        String startDateStr = appointmentDto.getStartDate();
        String endDateStr = appointmentDto.getEndDate();
        Date startDate = null;
        Date endDate = null;
        try {
            today = Formatter.parseDateTime(todayStr, AppConsts.DEFAULT_DATE_FORMAT);
            if(!StringUtil.isEmpty(startDateStr)) {
                startDate = Formatter.parseDateTime(startDateStr, AppConsts.DEFAULT_DATE_FORMAT);
            }
            if(!StringUtil.isEmpty(endDateStr)) {
                endDate = Formatter.parseDateTime(endDateStr, AppConsts.DEFAULT_DATE_FORMAT);
            }
        } catch (ParseException e) {
            log.info("Appt Date Error!!!!!");
            log.error(e.getMessage(), e);
        }
        if(endDate != null){
            if(endDate.before(today)){
                return false;
            } else {
                if(startDate == null){
                    return false;
                } else {
                    if(startDate.before(today)){
                        startDate = new Date();
                        appointmentDto.setStartDate(Formatter.formatDateTime(startDate, AppConsts.DEFAULT_DATE_TIME_FORMAT));
                    }
                }
            }
        } else {
            if(startDate == null){
                return false;
            } else {
                if(startDate.before(today)){
                    startDate = new Date();
                    appointmentDto.setStartDate(Formatter.formatDateTime(startDate, AppConsts.DEFAULT_DATE_TIME_FORMAT));
                }
            }
        }
        return true;
    }

    private Map<String, Date> setSvcIdLicDtMapByApp(ApplicationDto appInfoDto, Map<String, Date> svcIdLicDtMap) {
        if(svcIdLicDtMap == null) {
            svcIdLicDtMap = IaisCommonUtils.genNewHashMap();
        }
        if(appInfoDto != null) {
            String orgLicId = appInfoDto.getOriginLicenceId();
            if(!StringUtil.isEmpty(orgLicId)) {
                LicenceDto licDto = hcsaLicenceClient.getLicDtoById(orgLicId).getEntity();
                if(licDto != null && licDto.getExpiryDate() != null){
                    svcIdLicDtMap.put(appInfoDto.getServiceId(), licDto.getExpiryDate());
                }
            }
        }
        return svcIdLicDtMap;
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
            appointmentUserDtoList = IaisCommonUtils.genNewArrayList();
            for(AppointmentUserDto appointmentUserDto : appointmentUserDtos){
                if(appointmentUserDtoList.isEmpty()){
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

    private List<String> getAppNoByGroup(Map<String, Map<String, String>> groupCheckUserIdMap, String workGroupCheck,
                                         Map<String, List<String>> inspectorAppNoMap, List<String> appNoList) {
        Map<String, String> checkUserId = groupCheckUserIdMap.get(workGroupCheck);
        if(checkUserId != null){
            for(Map.Entry<String, String> userIdMap : checkUserId.entrySet()){
                String userId = userIdMap.getValue();
                if(!StringUtil.isEmpty(userId)){
                    List<String> appNos = inspectorAppNoMap.get(userId);
                    if(appNos!=null){
                        appNoList.addAll(appNos);
                    }
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
                log.info(StringUtil.changeForLog("Rescheduling Application No. = " + appNo));
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

    private void createFeAppPremisesInspecApptDto(List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList) {
        beEicGatewayClient.callEicWithTrack(appPremisesInspecApptDtoList, beEicGatewayClient::createAppPremisesInspecApptDto,
                "createFeAppPremisesInspecApptDto");
    }
}