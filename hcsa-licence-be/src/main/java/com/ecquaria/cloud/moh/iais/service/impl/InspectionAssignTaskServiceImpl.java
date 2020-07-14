package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.service.client.AppEicClient;
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
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremInspCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessmentConfig;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptCalendarStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppStageSlaTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.PoolRoleCheckDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ComPoolAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.BeSelfChecklistHelper;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Shicheng
 * @date 2019/11/22 10:19
 **/
@Service
@Slf4j
public class InspectionAssignTaskServiceImpl implements InspectionAssignTaskService {
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
    private TaskService taskService;

    @Autowired
    private ApplicationClient applicationClient;

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

    @Value("${iais.email.sender}")
    private String mailSender;

    @Autowired
    private EmailClient emailClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private InboxMsgService inboxMsgService;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    public List<TaskDto> getCommPoolByGroupWordId(LoginContext loginContext) {
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
        String curRole = loginContext.getCurRoleId();
        Set<String> workGrpIds = loginContext.getWrkGrpIds();
        if (IaisCommonUtils.isEmpty(workGrpIds) || StringUtil.isEmpty(curRole)) {
            return null;
        }
        List<String> workGrpIdList = new ArrayList<>(workGrpIds);
        for (String workGrpId : workGrpIdList) {
            for (TaskDto tDto : taskService.getCommPoolByGroupWordId(workGrpId)) {
                if(tDto.getRoleId() != null) {
                    if (tDto.getRoleId().equals(curRole)) {
                        taskDtoList.add(tDto);
                    }
                }
            }
        }

        return taskDtoList;
    }

    @Override
    public InspecTaskCreAndAssDto getInspecTaskCreAndAssDto(String appCorrelationId, List<TaskDto> commPools, LoginContext loginContext,
                                                            InspecTaskCreAndAssDto inspecTaskCreAndAssDto) {
        List<OrgUserDto> orgUserDtos = IaisCommonUtils.genNewArrayList();
        String workGroupId = "";
        for (TaskDto tDto : commPools) {
            if (appCorrelationId.equals(tDto.getRefNo())) {
                orgUserDtos = organizationClient.getUsersByWorkGroupName(tDto.getWkGrpId(), AppConsts.COMMON_STATUS_ACTIVE).getEntity();
            }
            if (inspecTaskCreAndAssDto.getTaskId().equals(tDto.getId())) {
                workGroupId = tDto.getWkGrpId();
            }
        }

        ApplicationDto applicationDto = searchByAppCorrId(appCorrelationId).getApplicationDto();
        AppGrpPremisesDto appGrpPremisesDto = getAppGrpPremisesDtoByAppGroId(appCorrelationId);
        String address = getAddress(appGrpPremisesDto);
        HcsaServiceDto hcsaServiceDto = getHcsaServiceDtoByServiceId(applicationDto.getServiceId());
        ApplicationGroupDto applicationGroupDto = getApplicationGroupDtoByAppGroId(applicationDto.getAppGrpId());

        inspecTaskCreAndAssDto.setApplicationId(applicationDto.getId());
        inspecTaskCreAndAssDto.setApplicationNo(applicationDto.getApplicationNo());
        inspecTaskCreAndAssDto.setAppCorrelationId(appCorrelationId);
        inspecTaskCreAndAssDto.setApplicationType(applicationDto.getApplicationType());
        inspecTaskCreAndAssDto.setApplicationStatus(applicationDto.getStatus());
        if (!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
            inspecTaskCreAndAssDto.setHciName(appGrpPremisesDto.getHciName() + " / " + address);
        } else {
            inspecTaskCreAndAssDto.setHciName(address);
        }
        inspecTaskCreAndAssDto.setHciCode(appGrpPremisesDto.getHciCode());
        inspecTaskCreAndAssDto.setServiceName(hcsaServiceDto.getSvcName());
        //todo:inspection type
        inspecTaskCreAndAssDto.setInspectionTypeName(InspectionConstants.INSPECTION_TYPE_ONSITE);
        inspecTaskCreAndAssDto.setInspectionType(applicationGroupDto.getIsPreInspection());
        inspecTaskCreAndAssDto.setSubmitDt(applicationGroupDto.getSubmitDt());
        //set inspector checkbox list
        setInspectorByOrgUserDto(inspecTaskCreAndAssDto, orgUserDtos, loginContext);
        setInspectorLeadName(inspecTaskCreAndAssDto, orgUserDtos, workGroupId);
        //set recommendation leads
        setInspectorLeadRecom(inspecTaskCreAndAssDto, appCorrelationId);
        return inspecTaskCreAndAssDto;
    }

    private void setInspectorLeadRecom(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, String appCorrelationId) {
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appCorrelationId, InspectionConstants.RECOM_TYPE_INSPECTION_LEAD).getEntity();
        if(appPremisesRecommendationDto == null){
            List<String> leadNames = inspecTaskCreAndAssDto.getInspectionLeads();
            if(!IaisCommonUtils.isEmpty(leadNames)){
                String nameStr = "";
                for(String name : leadNames){
                    if(StringUtil.isEmpty(nameStr)){
                        nameStr = name;
                    } else {
                        nameStr = nameStr + "," + name;
                    }
                }
                appPremisesRecommendationDto = new AppPremisesRecommendationDto();
                appPremisesRecommendationDto.setAppPremCorreId(appCorrelationId);
                appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPremisesRecommendationDto.setVersion(1);
                appPremisesRecommendationDto.setRecomInDate(null);
                appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPECTION_LEAD);
                appPremisesRecommendationDto.setRecomDecision(nameStr);
                appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                fillUpCheckListGetAppClient.saveAppRecom(appPremisesRecommendationDto);
            }
        }
    }

    @Override
    public void setInspectorLeadName(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, List<OrgUserDto> orgUserDtos, String workGroupId) {
        if (StringUtil.isEmpty(workGroupId)) {
            return;
        }
        List<String> leadNames = IaisCommonUtils.genNewArrayList();
        List<String> leadIds = organizationClient.getInspectionLead(workGroupId).getEntity();
        for (String id : leadIds) {
            for (OrgUserDto oDto : orgUserDtos) {
                if (id.equals(oDto.getId())) {
                    leadNames.add(oDto.getDisplayName());
                }
            }
        }
        inspecTaskCreAndAssDto.setInspectionLeads(leadNames);
    }

    @Override
    public PoolRoleCheckDto getRoleOptionByKindPool(LoginContext loginContext, String poolName, PoolRoleCheckDto poolRoleCheckDto) {
        List<String> roles = new ArrayList<>(loginContext.getRoleIds());
        if(!IaisCommonUtils.isEmpty(roles)){
            String curRole = loginContext.getCurRoleId();
            if(AppConsts.COMMON_POOL.equals(poolName)){
                //set common option an current role
                poolRoleCheckDto = commonCurRoleAndOption(roles, curRole, poolRoleCheckDto);
            } else if(AppConsts.SUPERVISOR_POOL.equals(poolName)){
                //set supervisor option an current role
                poolRoleCheckDto = superCurRoleAndOption(roles, curRole, poolRoleCheckDto);
            }
        } else {
            List<SelectOption> roleOptions = IaisCommonUtils.genNewArrayList();
            SelectOption so = new SelectOption("", "Please Select");
            roleOptions.add(so);
            poolRoleCheckDto.setRoleOptions(roleOptions);
        }
        //set current role in loginContext
        Map<String, String> roleMap = poolRoleCheckDto.getRoleMap();
        String checkCurRole = poolRoleCheckDto.getCheckCurRole();
        if(roleMap != null && !StringUtil.isEmpty(checkCurRole)){
            String role = roleMap.get(checkCurRole);
            if(!StringUtil.isEmpty(role)){
                loginContext.setCurRoleId(role);
            }
        }
        return poolRoleCheckDto;
    }

    private PoolRoleCheckDto superCurRoleAndOption(List<String> roles, String curRole, PoolRoleCheckDto poolRoleCheckDto) {
        List<SelectOption> roleOptions = IaisCommonUtils.genNewArrayList();
        Map<String, String> roleMap = IaisCommonUtils.genNewHashMap();
        int index = 0;
        String curCheckRole = "";
        for(String role : roles){
            //set lead role
            if(role.contains(RoleConsts.USER_LEAD)) {
                SelectOption so = new SelectOption(index + "", role);
                roleOptions.add(so);
                roleMap.put(index + "", role);
                //set current role check key
                if (role.equals(curRole)) {
                    curCheckRole = String.valueOf(index);
                }
                index++;
            }
        }
        if(StringUtil.isEmpty(curCheckRole)){
            curCheckRole = "0";
        }
        poolRoleCheckDto.setCheckCurRole(curCheckRole);
        poolRoleCheckDto.setRoleOptions(roleOptions);
        poolRoleCheckDto.setRoleMap(roleMap);
        return poolRoleCheckDto;
    }

    private PoolRoleCheckDto commonCurRoleAndOption(List<String> roles, String curRole, PoolRoleCheckDto poolRoleCheckDto) {
        List<SelectOption> roleOptions = IaisCommonUtils.genNewArrayList();
        Map<String, String> roleMap = IaisCommonUtils.genNewHashMap();
        int index = 0;
        for(String role : roles){
            SelectOption so = new SelectOption(index + "", role);
            roleOptions.add(so);
            roleMap.put(index + "", role);
            //set current role check key
            if(role.equals(curRole)){
                poolRoleCheckDto.setCheckCurRole(index + "");
            }
            index++;
        }
        //set default role check key
        if(StringUtil.isEmpty(curRole)){
            poolRoleCheckDto.setCheckCurRole("0");
        }
        poolRoleCheckDto.setRoleOptions(roleOptions);
        poolRoleCheckDto.setRoleMap(roleMap);
        return poolRoleCheckDto;
    }


    private void setInspectorByOrgUserDto(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, List<OrgUserDto> orgUserDtos, LoginContext loginContext) {
        if (orgUserDtos == null || orgUserDtos.size() <= 0) {
            inspecTaskCreAndAssDto.setInspector(null);
            return;
        }
        List<SelectOption> inspectorList = IaisCommonUtils.genNewArrayList();
        Set<String> roles = loginContext.getRoleIds();
        List<String> roleList = new ArrayList<>(roles);
        //get current lead role
        String curRole = loginContext.getCurRoleId();
        //get member role
        String leadRole;
        if (curRole.contains(RoleConsts.USER_LEAD)) {
            leadRole = curRole;
        } else {
            leadRole = curRole + RoleConsts.USER_LEAD;
        }
        if (roleList.contains(leadRole)) {
            addInspector(inspectorList, orgUserDtos, loginContext, roleList);
        } else {
            for (OrgUserDto oDto : orgUserDtos) {
                if (oDto.getId().equals(loginContext.getUserId())) {
                    SelectOption so = new SelectOption(oDto.getId(), oDto.getDisplayName());
                    inspectorList.add(so);
                }
            }
        }
        inspecTaskCreAndAssDto.setInspector(inspectorList);
    }

    private void addInspector(List<SelectOption> inspectorList, List<OrgUserDto> orgUserDtos, LoginContext loginContext, List<String> roleList) {
        String flag = AppConsts.FALSE;
        String curRole = loginContext.getCurRoleId();
        //get member role
        String memberRole;
        if (curRole.contains(RoleConsts.USER_LEAD)) {
            memberRole = curRole.replaceFirst(RoleConsts.USER_LEAD, "");
        } else {
            memberRole = curRole;
        }
        if (roleList.contains(memberRole)) {
            flag = AppConsts.TRUE;
        }
        for (OrgUserDto oDto : orgUserDtos) {
            if (!(oDto.getId().equals(loginContext.getUserId()))) {
                SelectOption so = new SelectOption(oDto.getId(), oDto.getDisplayName());
                inspectorList.add(so);
            } else {
                if (AppConsts.TRUE.equals(flag)) {
                    SelectOption so = new SelectOption(oDto.getId(), oDto.getDisplayName());
                    inspectorList.add(so);
                }
            }
        }
    }

    @Override
    @SearchTrack(catalog = "inspectionQuery", key = "assignCommonTask")
    public SearchResult<InspectionCommonPoolQueryDto> getSearchResultByParam(SearchParam searchParam) {
        return inspectionTaskClient.searchInspectionPool(searchParam).getEntity();
    }

    @Override
    public List<String> getAppCorrIdListByPool(List<TaskDto> commPools) {
        if (IaisCommonUtils.isEmpty(commPools)) {
            List<String> appCorrIdList = IaisCommonUtils.genNewArrayList();
            appCorrIdList.add(AppConsts.NO);
            return appCorrIdList;
        }
        Set<String> appCorrIdSet = IaisCommonUtils.genNewHashSet();
        for (TaskDto tDto : commPools) {
            appCorrIdSet.add(tDto.getRefNo());
        }
        List<String> appCorrIdList = new ArrayList<>(appCorrIdSet);

        return appCorrIdList;
    }

    @Override
    public List<SelectOption> getAppTypeOption() {
        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{
                ApplicationConsts.APPLICATION_TYPE_APPEAL,
                ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK,
                ApplicationConsts.APPLICATION_TYPE_CESSATION,
                ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,
                ApplicationConsts.APPLICATION_TYPE_RENEWAL,
                ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,
                ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL
        });
        return appTypeOption;
    }

    @Override
    public void assignTaskForInspectors(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto, ApplicationViewDto applicationViewDto,
                                        String internalRemarks, TaskDto taskDto, LoginContext loginContext) {
        List<SelectOption> inspectorCheckList = inspecTaskCreAndAssDto.getInspectorCheck();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String appStatus = applicationDto.getStatus();
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        for (TaskDto td : commPools) {
            if (td.getId().equals(inspecTaskCreAndAssDto.getTaskId())) {
                //rescheduling assign
                if(ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL.equals(appStatus) ||
                        ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT.equals(appStatus)){
                    String taskUserId = loginContext.getUserId();
                    List<String> taskUserIds = IaisCommonUtils.genNewArrayList();
                    taskUserIds.add(taskUserId);
                    ApplicationGroupDto applicationGroupDto = applicationViewDto.getApplicationGroupDto();
                    assignReschedulingTask(td, taskUserIds, applicationDtos, auditTrailDto, applicationGroupDto, inspecTaskCreAndAssDto.getInspManHours());
                } else if(RoleConsts.USER_ROLE_BROADCAST.equals(td.getRoleId())){
                    //broadcast task assign
                    assignBroadcastTask(td, applicationDtos, auditTrailDto, loginContext);
                } else {
                    //get score
                    List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, td.getTaskKey());
                    hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
                    //other tasks assign
                    td.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
                    td.setAuditTrailDto(auditTrailDto);
                    inspecTaskCreAndAssDto.setTaskDto(td);
                    inspecTaskCreAndAssDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    inspecTaskCreAndAssDto.setTaskDtos(commPools);
                    inspecTaskCreAndAssDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
                    organizationClient.assignCommonPool(inspecTaskCreAndAssDto);
                    AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorySubStage(td.getRefNo(), td.getTaskKey()).getEntity();
                    createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), internalRemarks,
                            InspectionConstants.PROCESS_DECI_COMMON_POOL_ASSIGN, td.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), td.getWkGrpId());
                    if (inspectorCheckList != null && inspectorCheckList.size() > 0) {
                        for (int i = 0; i < inspectorCheckList.size(); i++) {
                            if (ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(applicationDto.getStatus())) {
                                ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING);
                                applicationService.updateFEApplicaiton(applicationDto1);
                                inspecTaskCreAndAssDto.setApplicationStatus(applicationDto1.getStatus());
                                createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(), applicationDto1.getStatus(), taskDto.getTaskKey(), null, null, td.getRoleId(), null, td.getWkGrpId());
                            } else {
                                createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), null, null, td.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), td.getWkGrpId());
                            }
                        }
                    }
                    //Self-Checklist
                    boolean selfCheckListFlag = applicantIsSubmit(td.getRefNo());
                    if(selfCheckListFlag) {
                        String taskUserId = loginContext.getUserId();
                        List<String> taskUserIds = IaisCommonUtils.genNewArrayList();
                        taskUserIds.add(taskUserId);
                        String taskId = td.getId();
                        sendSelfCheckListEmail(taskId, taskUserIds);
                    }
                }
            }
        }
    }

    @Override
    public boolean applicantIsSubmit(String refNo) {
        boolean flag = false;
        List<SelfAssessment> selfAssessments = BeSelfChecklistHelper.receiveSelfAssessmentDataByCorrId(refNo);
        if(!IaisCommonUtils.isEmpty(selfAssessments)){
            //one refNo(appPremCorrId) --> one SelfAssessment
            List<SelfAssessmentConfig> selfAssessmentConfigs = selfAssessments.get(0).getSelfAssessmentConfig();
            if(!IaisCommonUtils.isEmpty(selfAssessmentConfigs)) {
                for(SelfAssessmentConfig selfAssessmentConfig : selfAssessmentConfigs){
                    if(selfAssessmentConfig == null){
                        continue;
                    }
                    if(selfAssessmentConfig.isCommon()){
                        List<PremCheckItem> premCheckItems = selfAssessmentConfig.getQuestion();
                        if(!IaisCommonUtils.isEmpty(premCheckItems)){
                            flag = true;
                        }
                    }
                }
            }
        }
        return flag;
    }

    @Override
    public void sendSelfCheckListEmail(String taskId, List<String> taskUserIds) {
        try{
            if(!IaisCommonUtils.isEmpty(taskUserIds)){
                List<String> addressList = IaisCommonUtils.genNewArrayList();
                for(String userId : taskUserIds){
                    OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
                    String address = orgUserDto.getEmail();
                    if(!StringUtil.isEmpty(address)){
                        addressList.add(address);
                    }
                }
                if(!IaisCommonUtils.isEmpty(addressList)) {
                    EmailDto emailDto = new EmailDto();
                    emailDto.setContent("Self-Checklist Complete");
                    emailDto.setSubject("MOH lAIS - Self-assessment Checklist Submission");
                    emailDto.setSender(mailSender);
                    emailDto.setReceipts(addressList);
                    emailDto.setClientQueryCode(taskId);
                    emailClient.sendNotification(emailDto);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void assignReschedulingTask(TaskDto td, List<String> taskUserIds, List<ApplicationDto> applicationDtos, AuditTrailDto auditTrailDto,
                                       ApplicationGroupDto applicationGroupDto, String inspManHours) {
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
        for(String taskUserId : taskUserIds){
            //create appInspCorrelation and task
            AppPremInspCorrelationDto appPremInspCorrelationDto = new AppPremInspCorrelationDto();
            appPremInspCorrelationDto.setApplicationNo(applicationDto.getApplicationNo());
            appPremInspCorrelationDto.setId(null);
            appPremInspCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremInspCorrelationDto.setUserId(taskUserId);
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
            //get all users
            Map<String, String> apptUserIdSvrIdMap = getSchedulingUsersByAppList(applicationDtoList, taskUserIds, applicationDto);
            boolean allInPlaceFlag = allAppFromSamePremisesIsOk(applicationDtoList);
            if(allInPlaceFlag){
                saveInspectionDate(appPremCorrId, taskDtoList, applicationDto, apptUserIdSvrIdMap, premCorrIds, auditTrailDto, appHistoryId, inspManHours);
                //update App
                ApplicationDto applicationDto1 = updateApplication(applicationDto, appStatus);
                applicationDto1.setAuditTrailDto(auditTrailDto);
                applicationService.updateFEApplicaiton(applicationDto1);
                //update inspection status
                updateInspectionStatus(appPremCorrId, InspectionConstants.INSPECTION_STATUS_RESCHEDULING_APPLICANT_ACCEPT_DATE);
                taskService.updateTask(td);
                taskService.createTasks(taskDtoList);
                inspectionTaskClient.createAppPremInspCorrelationDto(appPremInspCorrelationDtoList);
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
                inspectionTaskClient.createAppPremInspCorrelationDto(appPremInspCorrelationDtoList);
            }
        } else {
            List<String> premCorrIds = IaisCommonUtils.genNewArrayList();
            premCorrIds.add(appPremCorrId);
            //get all users
            Map<String, String> apptUserIdSvrIdMap = getSchedulingUsersByAppList(applicationDtos, taskUserIds, applicationDto);
            saveInspectionDate(appPremCorrId, taskDtoList, applicationDto, apptUserIdSvrIdMap, premCorrIds, auditTrailDto, appHistoryId, inspManHours);
            //update App
            ApplicationDto applicationDto1 = updateApplication(applicationDto, appStatus);
            applicationDto1.setAuditTrailDto(auditTrailDto);
            applicationService.updateFEApplicaiton(applicationDto1);
            //update inspection status
            updateInspectionStatus(appPremCorrId, InspectionConstants.INSPECTION_STATUS_RESCHEDULING_APPLICANT_ACCEPT_DATE);
            taskService.updateTask(td);
            taskService.createTasks(taskDtoList);
            inspectionTaskClient.createAppPremInspCorrelationDto(appPremInspCorrelationDtoList);
            createMessage(url, serviceCode, submitDt, licenseeId, maskParams);
        }
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
        interMessageDto.setSubject(MessageConstants.MESSAGE_SUBJECT_INSP_FE_RE_SCHEDULING);
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

    private void saveInspectionDate(String appPremCorrId, List<TaskDto> taskDtoList, ApplicationDto applicationDto, Map<String, String> apptUserIdSvrIdMap,
                                    List<String> premCorrIds, AuditTrailDto auditTrailDto, String appHistoryId, String inspManHours) {
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
            int manHours;
            if(StringUtil.isEmpty(inspManHours)){
                manHours = getServiceManHours(tDto.getRefNo(), apptAppInfoShowDto);
            } else {
                manHours = Integer.parseInt(inspManHours);
            }
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
            throw new IaisRuntimeException("get InspectionDate Error!!!", e);
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

        List<AppointmentUserDto> appointmentUserDtoList = null;//NOSONAR
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
                    appointmentUserDtoList.add(auDto);//NOSONAR
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

    private void assignBroadcastTask(TaskDto td, List<ApplicationDto> applicationDtos, AuditTrailDto auditTrailDto, LoginContext loginContext) {
        //get role and stage
        Set<String> roleIdSet = loginContext.getRoleIds();
        List<String> roleIds = new ArrayList<>(roleIdSet);
        Map<String, String> stageRoleMap = MiscUtil.getStageRoleByBroadcast(roleIds);
        if(stageRoleMap != null){
            List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
            for(Map.Entry<String, String> map : stageRoleMap.entrySet()){
                td.setSlaDateCompleted(new Date());
                td.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
                td.setAuditTrailDto(auditTrailDto);
                taskService.updateTask(td);
                ApplicationDto applicationDto = applicationDtos.get(0);
                createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), td.getTaskKey(), null,
                        InspectionConstants.PROCESS_DECI_COMMON_POOL_ASSIGN, td.getRoleId(), null, td.getWkGrpId());
                String subStage = null;
                String stageId;
                String role = map.getValue();
                if(RoleConsts.USER_ROLE_AO1.equals(role)){
                    stageId = getAoOneStage(applicationDto.getApplicationNo());
                    if(HcsaConsts.ROUTING_STAGE_INS.equals(stageId)){
                        subStage = HcsaConsts.ROUTING_STAGE_POT;
                    }
                } else {
                    stageId = map.getKey();
                }
                List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, stageId);
                hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
                int score = hcsaSvcStageWorkingGroupDtos.get(0).getCount();
                String processUrl = getProcessUrlByRoleAndStageId(role, stageId);
                TaskDto taskDto = new TaskDto();
                taskDto.setId(null);
                taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
                taskDto.setPriority(td.getPriority());
                taskDto.setRefNo(td.getRefNo());
                taskDto.setSlaAlertInDays(td.getSlaAlertInDays());
                taskDto.setSlaDateCompleted(null);
                taskDto.setSlaInDays(td.getSlaInDays());
                taskDto.setSlaRemainInDays(null);
                taskDto.setTaskKey(stageId);
                taskDto.setTaskType(td.getTaskType());
                taskDto.setWkGrpId(td.getWkGrpId());
                taskDto.setUserId(loginContext.getUserId());
                taskDto.setDateAssigned(new Date());
                taskDto.setRoleId(td.getRoleId());
                taskDto.setAuditTrailDto(auditTrailDto);
                taskDto.setProcessUrl(processUrl);
                taskDto.setScore(score);
                taskDto.setApplicationNo(td.getApplicationNo());
                taskDtoList.add(taskDto);
                createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), stageId, null, null, td.getRoleId(), subStage, td.getWkGrpId());
            }
            taskService.createTasks(taskDtoList);
        }
    }

    private String getProcessUrlByRoleAndStageId(String role, String stageId) {
        String processUrl = TaskConsts.TASK_PROCESS_URL_MAIN_FLOW;
        if(RoleConsts.USER_ROLE_AO1.equals(role)){
            if(HcsaConsts.ROUTING_STAGE_INS.equals(stageId)){
                processUrl = TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT_REVIEW_AO1;
            }
        } else if(RoleConsts.USER_ROLE_INSPECTIOR.equals(role)){
            processUrl = TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT;
        }
        return processUrl;
    }

    private String getAoOneStage(String applicationNo) {
        String stageId = HcsaConsts.ROUTING_STAGE_AO1;
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorysByAppNo(applicationNo).getEntity();
        if(!IaisCommonUtils.isEmpty(appPremisesRoutingHistoryDtos)){
            for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto : appPremisesRoutingHistoryDtos){
                if(HcsaConsts.ROUTING_STAGE_INS.equals(appPremisesRoutingHistoryDto.getStageId())){
                    stageId = HcsaConsts.ROUTING_STAGE_INS;
                    return stageId;
                }
            }
        }
        return stageId;
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

    @Override
    public SearchResult<InspectionCommonPoolQueryDto> getAddressByResult(SearchResult<InspectionCommonPoolQueryDto> searchResult) {
        for (InspectionCommonPoolQueryDto icpqDto : searchResult.getRows()) {
            if (1 == icpqDto.getAppCount()) {
                icpqDto.setSubmissionType(AppConsts.PAYMENT_STATUS_SINGLE);
            } else if (1 < icpqDto.getAppCount()) {
                icpqDto.setSubmissionType(AppConsts.PAYMENT_STATUS_MULTIPLE);
            } else {
                icpqDto.setSubmissionType("-");
            }
        }
        return searchResult;
    }

    @Override
    public ApplicationViewDto searchByAppCorrId(String correlationId) {
        return applicationClient.getAppViewByCorrelationId(correlationId).getEntity();
    }

    @Override
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

    @Override
    public void routingTaskByCommonPool(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto, String internalRemarks, LoginContext loginContext) {
        TaskDto taskDto = getTaskDtoByPool(commPools, inspecTaskCreAndAssDto);
        ApplicationViewDto applicationViewDto = searchByAppCorrId(inspecTaskCreAndAssDto.getAppCorrelationId());
        assignTaskForInspectors(commPools, inspecTaskCreAndAssDto, applicationViewDto, internalRemarks, taskDto, loginContext);
        if(!StringUtil.isEmpty(inspecTaskCreAndAssDto.getInspManHours())){
            //create inspManHours recommendation or update
            AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(taskDto.getRefNo(), InspectionConstants.RECOM_TYPE_INSP_MAN_HOUR).getEntity();
            if(appPremisesRecommendationDto != null){
                appPremisesRecommendationDto.setRecomDecision(inspecTaskCreAndAssDto.getInspManHours());
                appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            } else {
                appPremisesRecommendationDto = new AppPremisesRecommendationDto();
                appPremisesRecommendationDto.setAppPremCorreId(taskDto.getRefNo());
                appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPremisesRecommendationDto.setVersion(1);
                appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSP_MAN_HOUR);
                appPremisesRecommendationDto.setRecomDecision(inspecTaskCreAndAssDto.getInspManHours());
                appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            }
            fillUpCheckListGetAppClient.saveAppRecom(appPremisesRecommendationDto);
        }
    }

    private TaskDto getTaskDtoByPool(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto) {
        for (TaskDto tDto : commPools) {
            if (tDto.getId().equals(inspecTaskCreAndAssDto.getTaskId())) {
                return tDto;
            }
        }
        return new TaskDto();
    }

    /**
     * @author: shicheng
     * @Date 2019/11/22
     * @Param: serviceId
     * @return: HcsaServiceDto
     * @Descripation: get HcsaServiceDto By Service Id
     */
    public HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId) {
        return hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
    }

    @Override
    public AppGrpPremisesDto getAppGrpPremisesDtoByAppGroId(String appCorrId) {
        AppGrpPremisesDto appGrpPremisesDto = inspectionTaskClient.getAppGrpPremisesDtoByAppGroId(appCorrId).getEntity();
        if (StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
            appGrpPremisesDto.setHciName("");
        }
        if (StringUtil.isEmpty(appGrpPremisesDto.getHciCode())) {
            appGrpPremisesDto.setHciCode(HcsaConsts.HCSA_PREMISES_HCI_NULL);
        }
        setAddressByGroupPremises(appGrpPremisesDto);
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

    private void setAddressByGroupPremises(AppGrpPremisesDto appGrpPremisesDto) {
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

    @Override
    public String getAddress(AppGrpPremisesDto appGrpPremisesDto) {
        String result = "";
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())) {
            result = MiscUtil.getAddress(appGrpPremisesDto.getBlkNo(), appGrpPremisesDto.getStreetName(), appGrpPremisesDto.getBuildingName(),
                    appGrpPremisesDto.getFloorNo(), appGrpPremisesDto.getUnitNo(), appGrpPremisesDto.getPostalCode());
        } else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())) {
            result = MiscUtil.getAddress(appGrpPremisesDto.getBlkNo(), appGrpPremisesDto.getStreetName(), appGrpPremisesDto.getBuildingName(),
                    appGrpPremisesDto.getFloorNo(), appGrpPremisesDto.getUnitNo(), appGrpPremisesDto.getPostalCode());
        } else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(appGrpPremisesDto.getPremisesType())) {
            result = MiscUtil.getAddress(appGrpPremisesDto.getBlkNo(), appGrpPremisesDto.getStreetName(), appGrpPremisesDto.getBuildingName(),
                    appGrpPremisesDto.getFloorNo(), appGrpPremisesDto.getUnitNo(), appGrpPremisesDto.getPostalCode());
        }

        return result;
    }

    @Override
    @SearchTrack(catalog = "inspectionQuery", key = "assignCommonTask")
    public SearchResult<ComPoolAjaxQueryDto> getAjaxResultByParam(SearchParam searchParam) {
        return inspectionTaskClient.commonPoolResult(searchParam).getEntity();
    }

    @Override
    public GroupRoleFieldDto getGroupRoleField(LoginContext loginContext) {
        GroupRoleFieldDto groupRoleFieldDto = new GroupRoleFieldDto();
        String curRole = loginContext.getCurRoleId();
        String otherRole;
        String leadRole;
        String groupLeadName = "";
        String groupMemBerName = "";
        if (curRole.contains(RoleConsts.USER_LEAD)) {
            leadRole = curRole;
            otherRole = curRole.replaceFirst(RoleConsts.USER_LEAD, "");
        } else {
            leadRole = curRole + RoleConsts.USER_LEAD;
            otherRole = curRole;
        }
        if (!StringUtil.isEmpty(leadRole)) {
            if (RoleConsts.USER_ROLE_INSPECTION_LEAD.equals(leadRole)) {
                groupLeadName = MasterCodeUtil.getCodeDesc(RoleConsts.USER_MASTER_INSPECTION_LEAD);
            } else if(RoleConsts.USER_ROLE_BROADCAST.equals(otherRole)){
                groupLeadName = MasterCodeUtil.getCodeDesc(RoleConsts.USER_MASTER_BROADCAST_LEAD);
            } else {
                groupLeadName = MasterCodeUtil.getCodeDesc(leadRole);
            }
        }
        if (!StringUtil.isEmpty(otherRole)) {
            groupMemBerName = MasterCodeUtil.getCodeDesc(otherRole);
        }
        groupRoleFieldDto.setGroupLeadName(groupLeadName);
        groupRoleFieldDto.setGroupMemBerName(groupMemBerName);
        return groupRoleFieldDto;
    }

    /**
     * @author: shicheng
     * @Date 2019/11/23
     * @Param: appGroupId
     * @return: ApplicationGroupDto
     * @Descripation: get ApplicationGroup By Application Group Id
     */
    public ApplicationGroupDto getApplicationGroupDtoByAppGroId(String appGroupId) {
        return inspectionTaskClient.getApplicationGroupDtoByAppGroId(appGroupId).getEntity();
    }

    @Override
    public AppStageSlaTrackingDto searchSlaTrackById(String appNo,String stageId){
        return inspectionTaskClient.getSlaTrackByAppNoStageId(appNo,stageId).getEntity();
    }
}