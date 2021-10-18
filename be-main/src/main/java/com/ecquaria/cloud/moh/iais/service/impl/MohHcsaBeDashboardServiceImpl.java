package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.BeDashboardConstant;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskScoreDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.PoolRoleCheckDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashAllActionAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashAllGrpAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashAssignMeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashComPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashKpiPoolQuery;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashRenewQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashReplyQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashStageCircleKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashWaitApproveQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashWorkTeamQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryMainService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainService;
import com.ecquaria.cloud.moh.iais.service.MohHcsaBeDashboardService;
import com.ecquaria.cloud.moh.iais.service.RoleService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusMainClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationMainClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskMainClient;
import com.ecquaria.cloud.moh.iais.service.client.IntraDashboardClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import com.ecquaria.cloud.privilege.Privilege;
import com.ecquaria.cloud.privilege.PrivilegeServiceClient;
import com.ecquaria.cloud.role.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sop.rbac.user.UserIdentifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Shicheng
 * @date 2021/4/14 14:38
 **/
@Service
@Slf4j
public class MohHcsaBeDashboardServiceImpl implements MohHcsaBeDashboardService {
    private static final String[] STATUS_STRS_INSPECTOR = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02,
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03, ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS};

    private static final String[] STATUS_STRS_PSO = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01,
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03};

    private static final String[] STATUS_STRS_ELSE = new String[]{
            ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01,
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03};

    @Autowired
    private AppPremisesRoutingHistoryMainService appPremisesRoutingHistoryService;

    @Autowired
    private AppInspectionStatusMainClient appInspectionStatusMainClient;

    @Autowired
    private InspectionTaskMainClient inspectionTaskMainClient;

    @Autowired
    private IntraDashboardClient intraDashboardClient;

    @Autowired
    private OrganizationMainClient organizationMainClient;

    @Autowired
    private RoleService roleService;

    @Autowired
    private InspectionMainService inspectionService;

    @Autowired
    private ApplicationMainClient applicationMainClient;

    @Autowired
    private HcsaConfigMainClient hcsaConfigMainClient;

    @Autowired
    private PrivilegeServiceClient privilegeServiceClient;

    @Override
    public AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus, String decision,
                                                                         TaskDto taskDto, String userId, String remarks, String subStage) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
        appPremisesRoutingHistoryDto.setProcessDecision(decision);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(userId);
        appPremisesRoutingHistoryDto.setInternalRemarks(remarks);
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setRoleId(taskDto.getRoleId());
        appPremisesRoutingHistoryDto.setWrkGrpId(taskDto.getWkGrpId());
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        return appPremisesRoutingHistoryDto;
    }

    @Override
    public void updateInspectionStatus(String appPremisesCorrelationId, String status) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusMainClient.getAppInspectionStatusByPremId(appPremisesCorrelationId).getEntity();
        if (appInspectionStatusDto != null) {
            appInspectionStatusDto.setStatus(status);
            appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusMainClient.update(appInspectionStatusDto);
        }
    }

    @Override
    public AppPremisesRoutingHistoryDto getAppPremisesRoutingHistory(String appNo, String appStatus,
                                                                      String stageId,String subStageId,String wrkGrpId, String internalRemarks,String externalRemarks,String processDecision,
                                                                      String roleId){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setSubStage(subStageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setExternalRemarks(externalRemarks);
        appPremisesRoutingHistoryDto.setProcessDecision(processDecision);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setWrkGrpId(wrkGrpId);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return appPremisesRoutingHistoryDto;
    }

    @Override
    public List<String> setPoolScopeByCurRoleId(SearchParam searchParam, LoginContext loginContext, String switchAction, List<String> workGroupIds) {
        if(loginContext != null) {
            String curRoleId = loginContext.getCurRoleId();
            if(!StringUtil.isEmpty(curRoleId)) {
                if (curRoleId.contains(RoleConsts.USER_LEAD) &&
                    !curRoleId.contains(RoleConsts.USER_ROLE_AO)) {
                    //for ASO / PSO / Inspector lead
                    workGroupIds = getByAsoPsoInspLead(searchParam, loginContext);
                } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO1)) {
                    //for approver 1
                    workGroupIds = getByAo1AndLead(searchParam, loginContext, switchAction);
                } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO2) || curRoleId.contains(RoleConsts.USER_ROLE_AO3)) {
                    //for approver 2 / 3
                    workGroupIds = getByAo2Ao3AndLead(searchParam, loginContext, switchAction);
                } else if (RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN.equals(curRoleId)) {
                    //for System admin
                    workGroupIds = getBySystemAdmin(searchParam);
                } else {
                    //for myself
                    if(loginContext != null && !StringUtil.isEmpty(loginContext.getCurRoleId())){
                        curRoleId = loginContext.getCurRoleId();
                    } else {
                        curRoleId = RoleConsts.USER_LEAD;
                    }
                    searchParam.addFilter("dashRoleId", curRoleId, true);
                    if(!("common".equals(switchAction))) {
                        searchParam.addFilter("dashUserId", loginContext.getUserId(), true);
                    } else {
                        Set<String> wrkGrpIds = loginContext.getWrkGrpIds();
                        if(!IaisCommonUtils.isEmpty(wrkGrpIds)) {
                            workGroupIds = new ArrayList<>(wrkGrpIds);
                        }
                        int workGroupIdsSize = 0;
                        if(!IaisCommonUtils.isEmpty(workGroupIds)) {
                            workGroupIdsSize = workGroupIds.size();
                            String workGroupId = SqlHelper.constructInCondition("T7.WRK_GRP_ID", workGroupIdsSize);
                            searchParam.addParam("workGroup_list", workGroupId);
                            for (int i = 0; i < workGroupIds.size(); i++) {
                                searchParam.addFilter("T7.WRK_GRP_ID" + i, workGroupIds.get(i));
                            }
                        } else {
                            String workGroupId = SqlHelper.constructInCondition("T7.WRK_GRP_ID", workGroupIdsSize);
                            searchParam.addParam("workGroup_list", workGroupId);
                        }
                    }
                }
            }
        }
        return workGroupIds;
    }

    private List<String> getBySystemAdmin(SearchParam searchParam) {
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        //get ASO work group ids
        workGroupIds = getAsoWorkGroupIds(workGroupIds);
        //get PSO work group ids
        workGroupIds = getPsoWorkGroupIds(workGroupIds);
        //get Insp work group ids
        workGroupIds = getInspWorkGroupIds(workGroupIds);
        //set Ao1 Ao2 Groups
        workGroupIds = getAo1WorkGroupIds(workGroupIds);
        workGroupIds = getAo2WorkGroupIds(workGroupIds);
        //set Ao3 Groups
        workGroupIds = getAo3WorkGroupIds(workGroupIds);
        //duplicate removal
        Set<String> workGroupIdSet = new HashSet<>(workGroupIds);
        workGroupIds = new ArrayList<>(workGroupIdSet);
        int workGroupIdsSize = 0;
        if(!IaisCommonUtils.isEmpty(workGroupIds)) {
            workGroupIdsSize = workGroupIds.size();
            String workGroupId = SqlHelper.constructInCondition("T7.WRK_GRP_ID", workGroupIdsSize);
            searchParam.addParam("workGroup_list", workGroupId);
            for (int i = 0; i < workGroupIds.size(); i++) {
                searchParam.addFilter("T7.WRK_GRP_ID" + i, workGroupIds.get(i));
            }
        } else {
            String workGroupId = SqlHelper.constructInCondition("T7.WRK_GRP_ID", workGroupIdsSize);
            searchParam.addParam("workGroup_list", workGroupId);
        }
        return workGroupIds;
    }

    private List<String> getByAo1AndLead(SearchParam searchParam, LoginContext loginContext, String switchAction) {
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        //get login officer work groups
        Set<String> wrkGrpIdSet = loginContext.getWrkGrpIds();
        //get ASO work group ids
        workGroupIds = getAsoWorkGroupIds(workGroupIds);
        //set pso inspector ao1
        if(BeDashboardConstant.SWITCH_ACTION_COMMON.equals(switchAction)) {
            workGroupIds = getPsoInspByAo1GrpSet(workGroupIds, wrkGrpIdSet);
        } else if(BeDashboardConstant.SWITCH_ACTION_KPI.equals(switchAction)) {
            workGroupIds = setPsoInspAo1GrpsByAo1GrpSet(workGroupIds, wrkGrpIdSet);
        } else if(BeDashboardConstant.SWITCH_ACTION_REPLY.equals(switchAction)) {
            workGroupIds = getPsoInspByAo1GrpSet(workGroupIds, wrkGrpIdSet);
        } else if(BeDashboardConstant.SWITCH_ACTION_RE_RENEW.equals(switchAction)) {
            workGroupIds = setPsoInspAo1GrpsByAo1GrpSet(workGroupIds, wrkGrpIdSet);
        } else if(BeDashboardConstant.SWITCH_ACTION_WAIT.equals(switchAction)) {
            workGroupIds = getPsoInspByAo1GrpSet(workGroupIds, wrkGrpIdSet);
        } else if(BeDashboardConstant.SWITCH_ACTION_GROUP.equals(switchAction)) {
            workGroupIds = getPsoInspByAo1GrpSet(workGroupIds, wrkGrpIdSet);
        }
        Set<String> workGroupIdSet = new HashSet<>(workGroupIds);
        workGroupIds = new ArrayList<>(workGroupIdSet);
        int workGroupIdsSize = 0;
        if(!IaisCommonUtils.isEmpty(workGroupIds)) {
            workGroupIdsSize = workGroupIds.size();
            String workGroupId = SqlHelper.constructInCondition("T7.WRK_GRP_ID", workGroupIdsSize);
            searchParam.addParam("workGroup_list", workGroupId);
            for (int i = 0; i < workGroupIds.size(); i++) {
                searchParam.addFilter("T7.WRK_GRP_ID" + i, workGroupIds.get(i));
            }
        } else {
            String workGroupId = SqlHelper.constructInCondition("T7.WRK_GRP_ID", workGroupIdsSize);
            searchParam.addParam("workGroup_list", workGroupId);
        }
        return workGroupIds;
    }

    private List<String> setPsoInspAo1GrpsByAo1GrpSet(List<String> workGroupIds, Set<String> wrkGrpIdSet) {
        if(!IaisCommonUtils.isEmpty(wrkGrpIdSet)) {
            for(String wrkGrpId : wrkGrpIdSet) {
                WorkingGroupDto workingGroupDto = organizationMainClient.getWrkGrpById(wrkGrpId).getEntity();
                if(workingGroupDto != null) {
                    String workGroupName = workingGroupDto.getGroupName();
                    if(!StringUtil.isEmpty(workGroupName) && workGroupName.contains("Level 1 Approval")) {
                        workGroupIds.add(workingGroupDto.getId());
                        List<String> wrkGrpIds = getInspOrPsoWorkGroupByAo1(workingGroupDto.getId());
                        if(!IaisCommonUtils.isEmpty(wrkGrpIds)) {
                            workGroupIds.addAll(wrkGrpIds);
                        }
                    }
                }
            }
        }
        return workGroupIds;
    }

    private List<String> getPsoInspByAo1GrpSet(List<String> workGroupIds, Set<String> wrkGrpIdSet) {
        if(!IaisCommonUtils.isEmpty(wrkGrpIdSet)) {
            for(String wrkGrpId : wrkGrpIdSet) {
                WorkingGroupDto workingGroupDto = organizationMainClient.getWrkGrpById(wrkGrpId).getEntity();
                if(workingGroupDto != null) {
                    String workGroupName = workingGroupDto.getGroupName();
                    if(!StringUtil.isEmpty(workGroupName) && workGroupName.contains("Level 1 Approval")) {
                        List<String> wrkGrpIds = getInspOrPsoWorkGroupByAo1(workingGroupDto.getId());
                        if(!IaisCommonUtils.isEmpty(wrkGrpIds)) {
                            workGroupIds.addAll(wrkGrpIds);
                        }
                    }
                }
            }
        }
        return workGroupIds;
    }

    private List<String> getAsoWorkGroupIds(List<String> workGroupIds) {
        List<WorkingGroupDto> workingGroupDtos = organizationMainClient.getHcsaWorkGroupsByName("Admin Screening officer").getEntity();
        if(!IaisCommonUtils.isEmpty(workingGroupDtos)) {
            for(WorkingGroupDto workingGroupDto : workingGroupDtos) {
                if(workingGroupDto != null && !StringUtil.isEmpty(workingGroupDto.getId())) {
                    workGroupIds.add(workingGroupDto.getId());
                }
            }
        }
        return workGroupIds;
    }

    private List<String> getPsoWorkGroupIds(List<String> workGroupIds) {
        List<WorkingGroupDto> workingGroupDtos = organizationMainClient.getHcsaWorkGroupsByName("Professional Screening").getEntity();
        if(!IaisCommonUtils.isEmpty(workingGroupDtos)) {
            for(WorkingGroupDto workingGroupDto : workingGroupDtos) {
                if(workingGroupDto != null && !StringUtil.isEmpty(workingGroupDto.getId())) {
                    workGroupIds.add(workingGroupDto.getId());
                }
            }
        }
        return workGroupIds;
    }

    private List<String> getInspWorkGroupIds(List<String> workGroupIds) {
        List<WorkingGroupDto> workingGroupDtos = organizationMainClient.getHcsaWorkGroupsByName("Inspection").getEntity();
        if(!IaisCommonUtils.isEmpty(workingGroupDtos)) {
            for(WorkingGroupDto workingGroupDto : workingGroupDtos) {
                if(workingGroupDto != null && !StringUtil.isEmpty(workingGroupDto.getId())) {
                    if(!StringUtil.isEmpty(workingGroupDto.getGroupName()) && !workingGroupDto.getGroupName().contains("Level 1 Approval")) {
                        workGroupIds.add(workingGroupDto.getId());
                    }
                }
            }
        }
        return workGroupIds;
    }

    private List<String> getAo1WorkGroupIds(List<String> workGroupIds) {
        List<WorkingGroupDto> workingGroupDtos = organizationMainClient.getHcsaWorkGroupsByName("Level 1 Approval").getEntity();
        if(!IaisCommonUtils.isEmpty(workingGroupDtos)) {
            for(WorkingGroupDto workingGroupDto : workingGroupDtos) {
                if(workingGroupDto != null && !StringUtil.isEmpty(workingGroupDto.getId())) {
                    workGroupIds.add(workingGroupDto.getId());
                }
            }
        }
        return workGroupIds;
    }

    private List<String> getAo2WorkGroupIds(List<String> workGroupIds) {
        List<WorkingGroupDto> workingGroupDtos = organizationMainClient.getHcsaWorkGroupsByName("Level 2 Approval").getEntity();
        if(!IaisCommonUtils.isEmpty(workingGroupDtos)) {
            for(WorkingGroupDto workingGroupDto : workingGroupDtos) {
                if(workingGroupDto != null && !StringUtil.isEmpty(workingGroupDto.getId())) {
                    workGroupIds.add(workingGroupDto.getId());
                }
            }
        }
        return workGroupIds;
    }

    private List<String> getAo3WorkGroupIds(List<String> workGroupIds) {
        List<WorkingGroupDto> workingGroupDtos = organizationMainClient.getHcsaWorkGroupsByName("Level 3 Approval").getEntity();
        if(!IaisCommonUtils.isEmpty(workingGroupDtos)) {
            for(WorkingGroupDto workingGroupDto : workingGroupDtos) {
                if(workingGroupDto != null && !StringUtil.isEmpty(workingGroupDto.getId())) {
                    workGroupIds.add(workingGroupDto.getId());
                }
            }
        }
        return workGroupIds;
    }

    private List<String> getByAo2Ao3AndLead(SearchParam searchParam, LoginContext loginContext, String switchAction) {
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        //get lead role
        String curRoleId = loginContext.getCurRoleId();
        //get ASO work group ids
        workGroupIds = getAsoWorkGroupIds(workGroupIds);
        //get PSO work group ids
        workGroupIds = getPsoWorkGroupIds(workGroupIds);
        //get Insp work group ids
        workGroupIds = getInspWorkGroupIds(workGroupIds);
        if(BeDashboardConstant.SWITCH_ACTION_KPI.equals(switchAction)) {
            //set Ao1 Ao2 Groups
            workGroupIds = getAo1WorkGroupIds(workGroupIds);
            workGroupIds = getAo2WorkGroupIds(workGroupIds);
            if (curRoleId.contains(RoleConsts.USER_ROLE_AO3)) {
                //set Ao3 Groups
                workGroupIds = getAo3WorkGroupIds(workGroupIds);
            }
        } else if(BeDashboardConstant.SWITCH_ACTION_RE_RENEW.equals(switchAction)) {
            //set Ao1 Ao2 Groups
            workGroupIds = getAo1WorkGroupIds(workGroupIds);
            workGroupIds = getAo2WorkGroupIds(workGroupIds);
            if (curRoleId.contains(RoleConsts.USER_ROLE_AO3)) {
                //set Ao3 Groups
                workGroupIds = getAo3WorkGroupIds(workGroupIds);
            }
        } else if(BeDashboardConstant.SWITCH_ACTION_WAIT.equals(switchAction)) {
            //set Ao1 Ao2 Groups
            workGroupIds = getAo1WorkGroupIds(workGroupIds);
            workGroupIds = getAo2WorkGroupIds(workGroupIds);
            //set Ao3 Groups
            workGroupIds = getAo3WorkGroupIds(workGroupIds);
        } else if(BeDashboardConstant.SWITCH_ACTION_GROUP.equals(switchAction)) {
            //set Ao1 Groups
            workGroupIds = getAo1WorkGroupIds(workGroupIds);
            if (curRoleId.contains(RoleConsts.USER_ROLE_AO3)) {
                //set Ao3 Groups
                workGroupIds = getAo2WorkGroupIds(workGroupIds);
            }
        }
        Set<String> workGroupIdSet = new HashSet<>(workGroupIds);
        workGroupIds = new ArrayList<>(workGroupIdSet);
        int workGroupIdsSize = 0;
        if(!IaisCommonUtils.isEmpty(workGroupIds)) {
            workGroupIdsSize = workGroupIds.size();
            String workGroupId = SqlHelper.constructInCondition("T7.WRK_GRP_ID", workGroupIdsSize);
            searchParam.addParam("workGroup_list", workGroupId);
            for (int i = 0; i < workGroupIds.size(); i++) {
                searchParam.addFilter("T7.WRK_GRP_ID" + i, workGroupIds.get(i));
            }
        } else {
            String workGroupId = SqlHelper.constructInCondition("T7.WRK_GRP_ID", workGroupIdsSize);
            searchParam.addParam("workGroup_list", workGroupId);
        }
        return workGroupIds;
    }

    private List<String> getByAsoPsoInspLead(SearchParam searchParam, LoginContext loginContext) {
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        //get login officer's id
        String userId = loginContext.getUserId();
        //get lead role and memberRole
        String curRoleId = loginContext.getCurRoleId();
        String memberRole = curRoleId.replaceFirst(RoleConsts.USER_LEAD, "");
        //get login officer work groups
        Set<String> wrkGrpIdSet = loginContext.getWrkGrpIds();
        workGroupIds = getLoginOfficerWorkGroups(workGroupIds, wrkGrpIdSet, userId, curRoleId);
        //set filter
        setRoleAndWrkGrpsInParam(memberRole, workGroupIds, searchParam);
        return workGroupIds;
    }

    private List<String> getInspOrPsoWorkGroupByAo1(String workGroupId) {
        WorkingGroupDto workingGroupDto = organizationMainClient.getWrkGrpById(workGroupId).getEntity();
        if(workingGroupDto != null) {
            String wrkGrpDomain = workingGroupDto.getGroupDomain();
            List<WorkingGroupDto> workingGroupDtos = organizationMainClient.getWorkingGroup(wrkGrpDomain).getEntity();
            if(!IaisCommonUtils.isEmpty(workingGroupDtos)) {
                List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
                for (WorkingGroupDto wrkGrpDto : workingGroupDtos) {
                    if(wrkGrpDto != null) {
                        String workGrpName = wrkGrpDto.getGroupName();
                        if(!StringUtil.isEmpty(workGrpName) && !workGrpName.contains("Level 1 Approval")) {
                            workGroupIds.add(wrkGrpDto.getId());
                        }
                    }
                }
                return workGroupIds;
            }
        }
        return null;
    }

    private SearchParam setRoleAndWrkGrpsInParam(String memberRole, List<String> workGroupIds, SearchParam searchParam) {
        if(RoleConsts.USER_ROLE_INSPECTIOR.equals(memberRole)) {
            List<String> dashRoleIdList = IaisCommonUtils.genNewArrayList();
            dashRoleIdList.add(RoleConsts.USER_ROLE_INSPECTIOR);
            dashRoleIdList.add(RoleConsts.USER_ROLE_INSPECTION_LEAD);
            String dashRoleIdStr = SqlHelper.constructInCondition("T7.ROLE_ID", dashRoleIdList.size());
            searchParam.addParam("dashRoleIdList", dashRoleIdStr);
            for (int i = 0; i < dashRoleIdList.size(); i++) {
                searchParam.addFilter("T7.ROLE_ID" + i, dashRoleIdList.get(i));
            }
        } else {
            searchParam.addFilter("dashRoleId", memberRole, true);
        }
        int workGroupIdsSize = 0;
        if(!IaisCommonUtils.isEmpty(workGroupIds)) {
            workGroupIdsSize = workGroupIds.size();
            String workGroupId = SqlHelper.constructInCondition("T7.WRK_GRP_ID", workGroupIdsSize);
            searchParam.addParam("workGroup_list", workGroupId);
            for (int i = 0; i < workGroupIds.size(); i++) {
                searchParam.addFilter("T7.WRK_GRP_ID" + i, workGroupIds.get(i));
            }
        } else {
            String workGroupId = SqlHelper.constructInCondition("T7.WRK_GRP_ID", workGroupIdsSize);
            searchParam.addParam("workGroup_list", workGroupId);
        }
        return searchParam;
    }

    private List<String> getLoginOfficerWorkGroups(List<String> workGroupIds, Set<String> wrkGrpIdSet, String userId, String curRoleId) {
        if(!IaisCommonUtils.isEmpty(wrkGrpIdSet)) {
            for (String workGroupId : wrkGrpIdSet) {
                List<OrgUserDto> orgUserDtoList = organizationMainClient.activeUsersByWorkGroupAndRole(workGroupId, curRoleId).getEntity();
                if(!IaisCommonUtils.isEmpty(orgUserDtoList)) {
                    for(OrgUserDto orgUserDto : orgUserDtoList) {
                        if(orgUserDto != null) {
                            if(userId.equals(orgUserDto.getId())) {
                                workGroupIds.add(workGroupId);
                                break;
                            }
                        }
                    }
                }
            }
            Set<String> workGroupIdSet = new HashSet<>(workGroupIds);
            workGroupIds = new ArrayList<>(workGroupIdSet);
        }
        return workGroupIds;
    }

    @Override
    @SearchTrack(catalog = "intraDashboardQuery", key = "dashCommonTask")
    public SearchResult<DashComPoolQueryDto> getDashComPoolResult(SearchParam searchParam) {
        return inspectionTaskMainClient.searchDashComPoolResult(searchParam).getEntity();
    }

    @Override
    public SearchResult<DashComPoolQueryDto> getDashComPoolOtherData(SearchResult<DashComPoolQueryDto> searchResult) {
        //Sets the description of appGroup's quantity
        for (DashComPoolQueryDto dashComPoolQueryDto : searchResult.getRows()) {
            if (1 == dashComPoolQueryDto.getAppCount()) {
                dashComPoolQueryDto.setSubmissionType(AppConsts.PAYMENT_STATUS_SINGLE);
            } else if (1 < dashComPoolQueryDto.getAppCount()) {
                dashComPoolQueryDto.setSubmissionType(AppConsts.PAYMENT_STATUS_MULTIPLE);
            } else {
                dashComPoolQueryDto.setSubmissionType("-");
            }
            //set max update
            Date maxUpdateDate = getMaxUpdateByAppGroup(dashComPoolQueryDto.getId());
            dashComPoolQueryDto.setGroupUpDt(maxUpdateDate);
        }
        return searchResult;
    }

    @Override
    @SearchTrack(catalog = "intraDashboardQuery", key = "dashKpiTask")
    public SearchResult<DashKpiPoolQuery> getDashKpiPoolResult(SearchParam searchParam) {
        return intraDashboardClient.searchDashKpiPoolResult(searchParam).getEntity();
    }

    @Override
    public SearchResult<DashKpiPoolQuery> getDashKpiPoolOtherData(SearchResult<DashKpiPoolQuery> searchResult) {
        //Sets the description of appGroup's quantity
        for (DashKpiPoolQuery dashKpiPoolQuery : searchResult.getRows()) {
            if (1 == dashKpiPoolQuery.getAppCount()) {
                dashKpiPoolQuery.setSubmissionType(AppConsts.PAYMENT_STATUS_SINGLE);
            } else if (1 < dashKpiPoolQuery.getAppCount()) {
                dashKpiPoolQuery.setSubmissionType(AppConsts.PAYMENT_STATUS_MULTIPLE);
            } else {
                dashKpiPoolQuery.setSubmissionType("-");
            }
            //set max update
            Date maxUpdateDate = getMaxUpdateByAppGroup(dashKpiPoolQuery.getId());
            dashKpiPoolQuery.setGroupUpDt(maxUpdateDate);
        }
        return searchResult;
    }

    @Override
    public PoolRoleCheckDto getDashRoleOption(LoginContext loginContext, PoolRoleCheckDto poolRoleCheckDto) {
        List<String> roles = new ArrayList<>(loginContext.getRoleIds());
        String curRole = loginContext.getCurRoleId();
        if(!IaisCommonUtils.isEmpty(roles)){
            //sort
            Collections.sort(roles);
            List<SelectOption> roleOptions = IaisCommonUtils.genNewArrayList();
            Map<String, String> roleMap = IaisCommonUtils.genNewHashMap();
            int index = 0;
            //set role option
            for(String role : roles) {
                Role roleDto = roleService.getRoleByDomainRoleId(AppConsts.HALP_EGP_DOMAIN, role);
                if(roleDto != null) {
                    SelectOption so = new SelectOption(index + "", roleDto.getName());
                    roleOptions.add(so);
                    roleMap.put(index + "", role);
                    if(!StringUtil.isEmpty(role) && role.equals(curRole)){
                        poolRoleCheckDto.setCheckCurRole(index + "");
                    }
                    index++;
                }
            }
            poolRoleCheckDto.setRoleOptions(roleOptions);
            poolRoleCheckDto.setRoleMap(roleMap);
            //set current role in loginContext
            String checkCurRole = poolRoleCheckDto.getCheckCurRole();
            if(roleMap != null && !StringUtil.isEmpty(checkCurRole)){
                String role = roleMap.get(checkCurRole);
                if(!StringUtil.isEmpty(role)){
                    loginContext.setCurRoleId(role);
                }
            }
        } else {
            List<SelectOption> roleOptions = IaisCommonUtils.genNewArrayList();
            SelectOption so = new SelectOption("", "Please Select");
            roleOptions.add(so);
            poolRoleCheckDto.setRoleOptions(roleOptions);
        }
        return poolRoleCheckDto;
    }

    @Override
    @SearchTrack(catalog = "intraDashboardQuery", key = "dashAssignMe")
    public SearchResult<DashAssignMeQueryDto> getDashAssignMeResult(SearchParam searchParam) {
        return inspectionTaskMainClient.searchDashAssignMeResult(searchParam).getEntity();
    }

    @Override
    public SearchResult<DashAssignMeQueryDto> getDashAssignMeOtherData(SearchResult<DashAssignMeQueryDto> searchResult) {
        //Sets the description of appGroup's quantity
        for (DashAssignMeQueryDto dashAssignMeQueryDto : searchResult.getRows()) {
            if (1 == dashAssignMeQueryDto.getAppCount()) {
                dashAssignMeQueryDto.setSubmissionType(AppConsts.PAYMENT_STATUS_SINGLE);
            } else if (1 < dashAssignMeQueryDto.getAppCount()) {
                dashAssignMeQueryDto.setSubmissionType(AppConsts.PAYMENT_STATUS_MULTIPLE);
            } else {
                dashAssignMeQueryDto.setSubmissionType("-");
            }
            //set max update
            Date maxUpdateDate = getMaxUpdateByAppGroup(dashAssignMeQueryDto.getId());
            dashAssignMeQueryDto.setGroupUpDt(maxUpdateDate);
        }
        return searchResult;
    }

    @Override
    @SearchTrack(catalog = "intraDashboardQuery", key = "dashSupervisorTask")
    public SearchResult<DashWorkTeamQueryDto> getDashWorkTeamResult(SearchParam searchParam) {
        return inspectionTaskMainClient.searchDashWorkTeamResult(searchParam).getEntity();
    }

    @Override
    public SearchResult<DashWorkTeamQueryDto> getDashWorkTeamOtherData(SearchResult<DashWorkTeamQueryDto> searchResult) {
        //Sets the description of appGroup's quantity
        for (DashWorkTeamQueryDto dashWorkTeamQueryDto : searchResult.getRows()) {
            if (1 == dashWorkTeamQueryDto.getAppCount()) {
                dashWorkTeamQueryDto.setSubmissionType(AppConsts.PAYMENT_STATUS_SINGLE);
            } else if (1 < dashWorkTeamQueryDto.getAppCount()) {
                dashWorkTeamQueryDto.setSubmissionType(AppConsts.PAYMENT_STATUS_MULTIPLE);
            } else {
                dashWorkTeamQueryDto.setSubmissionType("-");
            }
            //set max update
            Date maxUpdateDate = getMaxUpdateByAppGroup(dashWorkTeamQueryDto.getId());
            dashWorkTeamQueryDto.setGroupUpDt(maxUpdateDate);
        }
        return searchResult;
    }

    @Override
    @SearchTrack(catalog = "intraDashboardQuery", key = "dashAppReplyTask")
    public SearchResult<DashReplyQueryDto> getDashReplyResult(SearchParam searchParam) {
        return intraDashboardClient.searchDashReplyPoolResult(searchParam).getEntity();
    }

    @Override
    public SearchResult<DashReplyQueryDto> getDashReplyOtherData(SearchResult<DashReplyQueryDto> searchResult) {
        //Sets the description of appGroup's quantity
        for (DashReplyQueryDto dashReplyQueryDto : searchResult.getRows()) {
            if (1 == dashReplyQueryDto.getAppCount()) {
                dashReplyQueryDto.setSubmissionType(AppConsts.PAYMENT_STATUS_SINGLE);
            } else if (1 < dashReplyQueryDto.getAppCount()) {
                dashReplyQueryDto.setSubmissionType(AppConsts.PAYMENT_STATUS_MULTIPLE);
            } else {
                dashReplyQueryDto.setSubmissionType("-");
            }
            //set max update
            Date maxUpdateDate = getMaxUpdateByAppGroup(dashReplyQueryDto.getId());
            dashReplyQueryDto.setGroupUpDt(maxUpdateDate);
        }
        return searchResult;
    }

    @Override
    @SearchTrack(catalog = "intraDashboardQuery", key = "dashWaitApproveTask")
    public SearchResult<DashWaitApproveQueryDto> getDashWaitApproveResult(SearchParam searchParam) {
        return intraDashboardClient.searchDashWaitApproveResult(searchParam).getEntity();
    }

    @Override
    public SearchResult<DashWaitApproveQueryDto> getDashWaitApproveOtherData(SearchResult<DashWaitApproveQueryDto> searchResult) {
        //Sets the description of appGroup's quantity
        for (DashWaitApproveQueryDto dashWaitApproveQueryDto : searchResult.getRows()) {
            if (1 == dashWaitApproveQueryDto.getAppCount()) {
                dashWaitApproveQueryDto.setSubmissionType(AppConsts.PAYMENT_STATUS_SINGLE);
            } else if (1 < dashWaitApproveQueryDto.getAppCount()) {
                dashWaitApproveQueryDto.setSubmissionType(AppConsts.PAYMENT_STATUS_MULTIPLE);
            } else {
                dashWaitApproveQueryDto.setSubmissionType("-");
            }
            //set max update
            Date maxUpdateDate = getMaxUpdateByAppGroup(dashWaitApproveQueryDto.getId());
            dashWaitApproveQueryDto.setGroupUpDt(maxUpdateDate);
        }
        return searchResult;
    }

    @Override
    @SearchTrack(catalog = "intraDashboardQuery", key = "dashAppRenewTask")
    public SearchResult<DashRenewQueryDto> getDashRenewResult(SearchParam searchParam) {
        return intraDashboardClient.searchDashRenewResult(searchParam).getEntity();
    }

    @Override
    public SearchResult<DashRenewQueryDto> getDashRenewOtherData(SearchResult<DashRenewQueryDto> searchResult) {
        //Sets the description of appGroup's quantity
        for (DashRenewQueryDto dashRenewQueryDto : searchResult.getRows()) {
            if (1 == dashRenewQueryDto.getAppCount()) {
                dashRenewQueryDto.setSubmissionType(AppConsts.PAYMENT_STATUS_SINGLE);
            } else if (1 < dashRenewQueryDto.getAppCount()) {
                dashRenewQueryDto.setSubmissionType(AppConsts.PAYMENT_STATUS_MULTIPLE);
            } else {
                dashRenewQueryDto.setSubmissionType("-");
            }
            //set max update
            Date maxUpdateDate = getMaxUpdateByAppGroup(dashRenewQueryDto.getId());
            dashRenewQueryDto.setGroupUpDt(maxUpdateDate);
        }
        return searchResult;
    }

    @Override
    public List<SelectOption> getAppStatusOptionByRoleAndSwitch(String curRoleId, String dashSwitchActionValue) {
        List<SelectOption> appStatusOption = IaisCommonUtils.genNewArrayList();
        //add status option
        if(BeDashboardConstant.SWITCH_ACTION_KPI.equals(dashSwitchActionValue)) {
            appStatusOption = getKpiAppStatusOptionByRole(curRoleId, appStatusOption);

        } else if(BeDashboardConstant.SWITCH_ACTION_ASSIGN_ME.equals(dashSwitchActionValue)) {
            appStatusOption = inspectionService.getAppStatusOption(curRoleId);

        } else if(BeDashboardConstant.SWITCH_ACTION_GROUP.equals(dashSwitchActionValue)) {
            appStatusOption = getTeamAppStatusOptionByRole(curRoleId, appStatusOption);

        } else if(BeDashboardConstant.SWITCH_ACTION_WAIT.equals(dashSwitchActionValue)) {
            appStatusOption = getWaitAppStatusOptionByRole(curRoleId, appStatusOption);

        } else if(BeDashboardConstant.SWITCH_ACTION_RE_RENEW.equals(dashSwitchActionValue)) {
            appStatusOption = getRenewAppStatusOptionByRole(curRoleId, appStatusOption);

        }
        //duplicate removal
        appStatusOption = delDuplicateStatusOption(appStatusOption);
        return appStatusOption;
    }

    @Override
    public Date getMaxUpdateByAppGroup(String appGroupId) {
        Date maxDate = null;
        if(!StringUtil.isEmpty(appGroupId)) {
            List<ApplicationDto> applicationDtos = applicationMainClient.getGroupAppsByNo(appGroupId).getEntity();
            for(ApplicationDto applicationDto : applicationDtos) {
                if(applicationDto != null && applicationDto.getModifiedAt() != null) {
                    if(maxDate == null) {
                        maxDate = applicationDto.getModifiedAt();
                    } else if(applicationDto.getModifiedAt().after(maxDate)) {
                        maxDate = applicationDto.getModifiedAt();
                    }
                }
            }
        } else {
            maxDate = new Date();
        }
        return maxDate;
    }

    @Override
    public boolean containsAppStatus(List<SelectOption> appStatusOption, String application_status) {
        if(!IaisCommonUtils.isEmpty(appStatusOption)) {
            for(SelectOption so : appStatusOption) {
                if(so != null && !StringUtil.isEmpty(so.getValue())) {
                    if(so.getValue().equals(application_status)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<String> getSearchAppStatus(String application_status) {
        List<String> appStatus = IaisCommonUtils.genNewArrayList();
        List<MasterCodeView> masterCodeViews = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_APP_STATUS);
        String codeValue = MasterCodeUtil.getCodeDesc(application_status);
        for (MasterCodeView masterCodeView : masterCodeViews) {
            if(masterCodeView != null && codeValue.equals(masterCodeView.getCodeValue())){
                appStatus.add(masterCodeView.getCode());
            }
        }
        return appStatus;
    }

    @Override
    public List<SelectOption> getHashServiceOption() {
        List<HcsaServiceDto> hcsaServiceDtoList = hcsaConfigMainClient.getActiveServices().getEntity();
        List<SelectOption> serviceOptions = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(hcsaServiceDtoList)) {
            List<String> svcCodes = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtoList) {
                if(hcsaServiceDto != null) {
                    String svcCode = hcsaServiceDto.getSvcCode();
                    String svcName = hcsaServiceDto.getSvcName();
                    if(IaisCommonUtils.isEmpty(svcCodes)) {
                        SelectOption selectOption = new SelectOption(svcCode, svcName);
                        svcCodes.add(svcCode);
                        serviceOptions.add(selectOption);
                    } else if(!svcCodes.contains(svcCode)) {
                        SelectOption selectOption = new SelectOption(svcCode, svcName);
                        svcCodes.add(svcCode);
                        serviceOptions.add(selectOption);
                    }
                }
            }
        }
        return serviceOptions;
    }

    @Override
    public List<DashStageCircleKpiDto> getDashStageCircleKpiShow(SearchResult<DashAllActionAppQueryDto> searchResult) {
        List<DashStageCircleKpiDto> dashStageCircleKpiDtos = IaisCommonUtils.genNewArrayList();
        //Initialize the stage dto
        DashStageCircleKpiDto dashAllCircleKpiDto = new DashStageCircleKpiDto();
        DashStageCircleKpiDto dashAsoCircleKpiDto = new DashStageCircleKpiDto();
        dashAsoCircleKpiDto.setStageId(HcsaConsts.ROUTING_STAGE_ASO);
        DashStageCircleKpiDto dashPsoCircleKpiDto = new DashStageCircleKpiDto();
        dashPsoCircleKpiDto.setStageId(HcsaConsts.ROUTING_STAGE_PSO);
        DashStageCircleKpiDto dashPreInspCircleKpiDto = new DashStageCircleKpiDto();
        dashPreInspCircleKpiDto.setStageId(HcsaConsts.ROUTING_STAGE_PRE);
        DashStageCircleKpiDto dashInspCircleKpiDto = new DashStageCircleKpiDto();
        dashInspCircleKpiDto.setStageId(HcsaConsts.ROUTING_STAGE_INP);
        DashStageCircleKpiDto dashPostInspCircleKpiDto = new DashStageCircleKpiDto();
        dashPostInspCircleKpiDto.setStageId(HcsaConsts.ROUTING_STAGE_POT);
        DashStageCircleKpiDto dashAo1CircleKpiDto = new DashStageCircleKpiDto();
        dashAo1CircleKpiDto.setStageId(HcsaConsts.ROUTING_STAGE_AO1);
        DashStageCircleKpiDto dashAo2CircleKpiDto = new DashStageCircleKpiDto();
        dashAo2CircleKpiDto.setStageId(HcsaConsts.ROUTING_STAGE_AO2);
        DashStageCircleKpiDto dashAo3CircleKpiDto = new DashStageCircleKpiDto();
        dashAo3CircleKpiDto.setStageId(HcsaConsts.ROUTING_STAGE_AO3);
        //count
        if(searchResult != null) {
            List<DashAllActionAppQueryDto> dashAllActionAppQueryDtos = searchResult.getRows();
            if(!IaisCommonUtils.isEmpty(dashAllActionAppQueryDtos)) {
                //set color count by stage and kpi
                for(DashAllActionAppQueryDto dashAllActionAppQueryDto : dashAllActionAppQueryDtos) {
                    String stageId = dashAllActionAppQueryDto.getStageId();
                    String subStage = dashAllActionAppQueryDto.getSubStage();
                    if(!StringUtil.isEmpty(subStage)) {
                        if(HcsaConsts.ROUTING_STAGE_PRE.equals(subStage)) {
                            dashPreInspCircleKpiDto = setKpiCountShowDataByAllActionApp(dashPreInspCircleKpiDto, dashAllActionAppQueryDto);

                        } else if(HcsaConsts.ROUTING_STAGE_INP.equals(subStage)) {
                            dashInspCircleKpiDto = setKpiCountShowDataByAllActionApp(dashInspCircleKpiDto, dashAllActionAppQueryDto);

                        } else if(HcsaConsts.ROUTING_STAGE_POT.equals(subStage)) {
                            dashPostInspCircleKpiDto = setKpiCountShowDataByAllActionApp(dashPostInspCircleKpiDto, dashAllActionAppQueryDto);

                        }
                    } else if (!StringUtil.isEmpty(stageId)) {
                        if(HcsaConsts.ROUTING_STAGE_ASO.equals(stageId)) {
                            dashAsoCircleKpiDto = setKpiCountShowDataByAllActionApp(dashAsoCircleKpiDto, dashAllActionAppQueryDto);

                        } else if(HcsaConsts.ROUTING_STAGE_PSO.equals(stageId)) {
                            dashPsoCircleKpiDto = setKpiCountShowDataByAllActionApp(dashPsoCircleKpiDto, dashAllActionAppQueryDto);

                        } else if(HcsaConsts.ROUTING_STAGE_AO1.equals(stageId)) {
                            dashAo1CircleKpiDto = setKpiCountShowDataByAllActionApp(dashAo1CircleKpiDto, dashAllActionAppQueryDto);

                        } else if(HcsaConsts.ROUTING_STAGE_AO2.equals(stageId)) {
                            dashAo2CircleKpiDto = setKpiCountShowDataByAllActionApp(dashAo2CircleKpiDto, dashAllActionAppQueryDto);

                        } else if(HcsaConsts.ROUTING_STAGE_AO3.equals(stageId)) {
                            dashAo3CircleKpiDto = setKpiCountShowDataByAllActionApp(dashAo3CircleKpiDto, dashAllActionAppQueryDto);

                        }
                    }
                }
            }
        }
        //add list
        dashStageCircleKpiDtos.add(dashAsoCircleKpiDto);
        dashStageCircleKpiDtos.add(dashPsoCircleKpiDto);
        dashStageCircleKpiDtos.add(dashPreInspCircleKpiDto);
        dashStageCircleKpiDtos.add(dashInspCircleKpiDto);
        dashStageCircleKpiDtos.add(dashPostInspCircleKpiDto);
        dashStageCircleKpiDtos.add(dashAo1CircleKpiDto);
        dashStageCircleKpiDtos.add(dashAo2CircleKpiDto);
        dashStageCircleKpiDtos.add(dashAo3CircleKpiDto);
        //set all kpi color show count
        dashStageCircleKpiDtos = setAllKpiColorCount(dashStageCircleKpiDtos, dashAllCircleKpiDto);
        return dashStageCircleKpiDtos;
    }

    private List<DashStageCircleKpiDto> setAllKpiColorCount(List<DashStageCircleKpiDto> dashStageCircleKpiDtos, DashStageCircleKpiDto dashAllCircleKpiDto) {
        if(!IaisCommonUtils.isEmpty(dashStageCircleKpiDtos)) {
            if(dashAllCircleKpiDto != null) {
                int allBlueCount = dashAllCircleKpiDto.getDashBlueCount();
                int allAmberCount = dashAllCircleKpiDto.getDashAmberCount();
                int allRedCount = dashAllCircleKpiDto.getDashRedCount();
                for(DashStageCircleKpiDto dashStageCircleKpiDto : dashStageCircleKpiDtos) {
                    if(dashStageCircleKpiDto != null) {
                        allBlueCount = allBlueCount + dashStageCircleKpiDto.getDashBlueCount();
                        allAmberCount = allAmberCount + dashStageCircleKpiDto.getDashAmberCount();
                        allRedCount = allRedCount + dashStageCircleKpiDto.getDashRedCount();
                        dashAllCircleKpiDto.setDashBlueCount(allBlueCount);
                        dashAllCircleKpiDto.setDashAmberCount(allAmberCount);
                        dashAllCircleKpiDto.setDashRedCount(allRedCount);
                    }
                }
                int allStageCount = allBlueCount + allAmberCount + allRedCount;
                dashAllCircleKpiDto.setDashStageCount(allStageCount);
                dashStageCircleKpiDtos.add(dashAllCircleKpiDto);
            }
        }
        return dashStageCircleKpiDtos;
    }

    private DashStageCircleKpiDto setKpiCountShowDataByAllActionApp(DashStageCircleKpiDto dashStageCircleKpiDto, DashAllActionAppQueryDto dashAllActionAppQueryDto) {
        if(dashStageCircleKpiDto != null && dashAllActionAppQueryDto != null) {
            dashStageCircleKpiDto.setDashStageCount(dashStageCircleKpiDto.getDashStageCount() + 1);
            int slaDays = 0;
            int rmdThreshold = 0;
            int kpi = 0;
            if(dashAllActionAppQueryDto.getSlaDays() != null) {
                slaDays = dashAllActionAppQueryDto.getSlaDays();
            }
            if(dashAllActionAppQueryDto.getRmdThreshold() != null) {
                rmdThreshold = dashAllActionAppQueryDto.getRmdThreshold();
            }
            if(dashAllActionAppQueryDto.getKpi() != null) {
                kpi = dashAllActionAppQueryDto.getKpi();
            }
            if(rmdThreshold != 0 && kpi != 0) {
                if (slaDays < rmdThreshold) {
                    dashStageCircleKpiDto.setDashBlueCount(dashStageCircleKpiDto.getDashBlueCount() + 1);
                }
                if (rmdThreshold <= slaDays && slaDays <= kpi) {
                    dashStageCircleKpiDto.setDashAmberCount(dashStageCircleKpiDto.getDashAmberCount() + 1);
                } else if (slaDays > kpi) {
                    dashStageCircleKpiDto.setDashRedCount(dashStageCircleKpiDto.getDashRedCount() + 1);
                }
            } else {
                dashStageCircleKpiDto.setDashBlueCount(dashStageCircleKpiDto.getDashBlueCount() + 1);
            }
        }
        return dashStageCircleKpiDto;
    }

    @Override
    @SearchTrack(catalog = "intraDashboardQuery", key = "dashSystemOverAll")
    public SearchResult<DashAllActionAppQueryDto> getDashAllActionResult(SearchParam searchParam) {
        return intraDashboardClient.searchDashAllActionAppResult(searchParam).getEntity();
    }

    @Override
    @SearchTrack(catalog = "intraDashboardQuery", key = "dashSystemDetail")
    public SearchResult<DashAllGrpAppQueryDto> getDashSysGrpDetailQueryResult(SearchParam searchParam) {
        return intraDashboardClient.searchDashAllGrpAppResult(searchParam).getEntity();
    }

    @Override
    public SearchResult<DashAllGrpAppQueryDto> getDashSysGrpDetailOtherData(SearchResult<DashAllGrpAppQueryDto> searchResult) {
        //Sets the description of appGroup's quantity
        for (DashAllGrpAppQueryDto dashAllGrpAppQueryDto : searchResult.getRows()) {
            if (1 == dashAllGrpAppQueryDto.getAppCount()) {
                dashAllGrpAppQueryDto.setSubmissionType(AppConsts.PAYMENT_STATUS_SINGLE);
            } else if (1 < dashAllGrpAppQueryDto.getAppCount()) {
                dashAllGrpAppQueryDto.setSubmissionType(AppConsts.PAYMENT_STATUS_MULTIPLE);
            } else {
                dashAllGrpAppQueryDto.setSubmissionType("-");
            }
            //set max update
            Date maxUpdateDate = getMaxUpdateByAppGroup(dashAllGrpAppQueryDto.getId());
            dashAllGrpAppQueryDto.setGroupUpDt(maxUpdateDate);
        }
        return searchResult;
    }

    @Override
    public List<DashStageCircleKpiDto> getDashStageSvcKpiShow(SearchResult<DashAllActionAppQueryDto> searchCountResult, List<SelectOption> serviceOption) {
        List<DashStageCircleKpiDto> dashStageCircleKpiDtos = IaisCommonUtils.genNewArrayList();
        //init dashStageCircleKpiDtos
        dashStageCircleKpiDtos = initDashStageSvcKpiShow(dashStageCircleKpiDtos, serviceOption);
        if(!IaisCommonUtils.isEmpty(dashStageCircleKpiDtos)) {
            if(searchCountResult != null) {
                List<DashAllActionAppQueryDto> dashAllActionAppQueryDtos = searchCountResult.getRows();
                if (!IaisCommonUtils.isEmpty(dashAllActionAppQueryDtos)) {
                    //create return val
                    List<DashStageCircleKpiDto> dashStageCircleKpiDtoList = IaisCommonUtils.genNewArrayList();
                    //set data
                    for (DashStageCircleKpiDto dashStageCircleKpiDto : dashStageCircleKpiDtos) {
                        for (DashAllActionAppQueryDto dashAllActionAppQueryDto : dashAllActionAppQueryDtos) {
                            dashStageCircleKpiDto = setDashStageSvcKpiData(dashStageCircleKpiDto, dashAllActionAppQueryDto);
                        }
                        dashStageCircleKpiDtoList.add(dashStageCircleKpiDto);
                    }
                    dashStageCircleKpiDtoList = addSaveAllCountCircleKpiDto(dashStageCircleKpiDtoList);
                    return dashStageCircleKpiDtoList;
                } else {
                    //set 'all Circle' for show
                    DashStageCircleKpiDto dashStageCircleKpiAllDto = new DashStageCircleKpiDto();
                    dashStageCircleKpiDtos.add(dashStageCircleKpiAllDto);
                }
            } else {
                //set 'all Circle' for show
                DashStageCircleKpiDto dashStageCircleKpiAllDto = new DashStageCircleKpiDto();
                dashStageCircleKpiDtos.add(dashStageCircleKpiAllDto);
            }
        }

        return dashStageCircleKpiDtos;
    }

    @Override
    public SearchParam setStatisticsDashFilter(SearchParam searchParam, String[] services, String[] appTypes, String applicationNo) {
        if(services != null && services.length > 0) {
            String serviceStr = SqlHelper.constructInCondition("viewApp.SVC_CODE", services.length);
            searchParam.addParam("svc_codes", serviceStr);
            for(int i = 0; i < services.length; i++){
                searchParam.addFilter("viewApp.SVC_CODE" + i, services[i]);
            }
        }
        if(appTypes != null && appTypes.length > 0) {
            String appTypeStr = SqlHelper.constructInCondition("viewApp.APP_TYPE", appTypes.length);
            searchParam.addParam("application_types", appTypeStr);
            for(int i = 0; i < appTypes.length; i++){
                searchParam.addFilter("viewApp.APP_TYPE" + i, appTypes[i]);
            }
        }
        if(!StringUtil.isEmpty(applicationNo)){
            searchParam.addFilter("applicationNo", applicationNo, true);
        }
        return searchParam;
    }

    @Override
    public String getPrivilegeFlagByRole(LoginContext loginContext) {
        //
        if(loginContext != null) {
            List<String> roleIds = loginContext.getRoleIds();
            if(!IaisCommonUtils.isEmpty(roleIds)) {
                if(roleIds.size() == 1 && roleIds.contains(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN)) {
                    //new user data
                    UserIdentifier userIdentifier = new UserIdentifier();
                    userIdentifier.setId(loginContext.getLoginId());
                    userIdentifier.setUserDomain("cs_hcsa");
                    //add role
                    String[] roleArr = {RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN};
                    //get privilege Number
                    Long[] privilegeNo = privilegeServiceClient.getAccessiblePrivilegeNos(userIdentifier, roleArr).getEntity();
                    if(privilegeNo != null && privilegeNo.length > 0) {
                        //get Privilege
                        long[] privilegeNoArr = transferSmallLong(privilegeNo);
                        List<Privilege> privileges = privilegeServiceClient.getprivilegesByNos(privilegeNoArr).getEntity();
                        if(!IaisCommonUtils.isEmpty(privileges)) {
                            for(Privilege privilege : privileges) {
                                if(privilege != null && "HALP_INTRA_STATISTICS_BOARD".equals(privilege.getId())) {
                                    return AppConsts.SUCCESS;
                                }
                            }
                        }
                    }
                }
            }
        }
        return AppConsts.FAIL;
    }

    private long[] transferSmallLong(Long[] privilegeNo) {
        long[] privilegeNoArr = new long[privilegeNo.length];
        for(int i = 0; i < privilegeNo.length; i++) {
            privilegeNoArr[i] = privilegeNo[i];
        }
        return privilegeNoArr;
    }

    private List<DashStageCircleKpiDto> addSaveAllCountCircleKpiDto(List<DashStageCircleKpiDto> dashStageCircleKpiDtoList) {
        DashStageCircleKpiDto dashStageCircleKpiAllDto = new DashStageCircleKpiDto();
        int allBlueCount = dashStageCircleKpiAllDto.getDashBlueCount();
        int allAmberCount = dashStageCircleKpiAllDto.getDashAmberCount();
        int allRedCount = dashStageCircleKpiAllDto.getDashRedCount();
        for(DashStageCircleKpiDto dashStageCircleKpiDto : dashStageCircleKpiDtoList) {
            if(dashStageCircleKpiDto != null) {
                allBlueCount = allBlueCount + dashStageCircleKpiDto.getDashBlueCount();
                allAmberCount = allAmberCount + dashStageCircleKpiDto.getDashAmberCount();
                allRedCount = allRedCount + dashStageCircleKpiDto.getDashRedCount();
                dashStageCircleKpiAllDto.setDashBlueCount(allBlueCount);
                dashStageCircleKpiAllDto.setDashAmberCount(allAmberCount);
                dashStageCircleKpiAllDto.setDashRedCount(allRedCount);
            }
        }
        int allStageCount = allBlueCount + allAmberCount + allRedCount;
        dashStageCircleKpiAllDto.setDashStageCount(allStageCount);
        dashStageCircleKpiDtoList.add(dashStageCircleKpiAllDto);
        return dashStageCircleKpiDtoList;
    }

    private DashStageCircleKpiDto setDashStageSvcKpiData(DashStageCircleKpiDto dashStageCircleKpiDto, DashAllActionAppQueryDto dashAllActionAppQueryDto) {
        if(dashAllActionAppQueryDto != null && dashStageCircleKpiDto != null) {
            String svcCodeOpVal = dashStageCircleKpiDto.getSvcCode();
            String svcCode = dashAllActionAppQueryDto.getSvcCode();
            //check svc code
            if(!StringUtil.isEmpty(svcCodeOpVal)) {
                if(svcCodeOpVal.equals(svcCode)) {
                    dashStageCircleKpiDto = setKpiCountShowDataByAllActionApp(dashStageCircleKpiDto, dashAllActionAppQueryDto);
                }
            }
        }
        return dashStageCircleKpiDto;
    }

    private List<DashStageCircleKpiDto> initDashStageSvcKpiShow(List<DashStageCircleKpiDto> dashStageCircleKpiDtos, List<SelectOption> serviceOption) {
        if(!IaisCommonUtils.isEmpty(serviceOption)) {
            for(SelectOption selectOption : serviceOption) {
                if(selectOption != null) {
                    String svcCode = selectOption.getValue();
                    DashStageCircleKpiDto dashStageCircleKpiDto = new DashStageCircleKpiDto();
                    dashStageCircleKpiDto.setSvcCode(svcCode);
                    dashStageCircleKpiDtos.add(dashStageCircleKpiDto);
                }
            }
        }

        return dashStageCircleKpiDtos;
    }

    @Override
    public SearchParam setSysDashFilter(SearchParam searchParam, String[] services, String[] appTypes) {
        if(services != null && services.length > 0) {
            String serviceStr = SqlHelper.constructInCondition("viewApp.SVC_CODE", services.length);
            searchParam.addParam("svc_codes", serviceStr);
            for(int i = 0; i < services.length; i++){
                searchParam.addFilter("viewApp.SVC_CODE" + i, services[i]);
            }
        }
        if(appTypes != null && appTypes.length > 0) {
            String appTypeStr = SqlHelper.constructInCondition("viewApp.APP_TYPE", appTypes.length);
            searchParam.addParam("application_types", appTypeStr);
            for(int i = 0; i < appTypes.length; i++){
                searchParam.addFilter("viewApp.APP_TYPE" + i, appTypes[i]);
            }
        }
        return searchParam;
    }

    @Override
    public String getStageIdByJspClickVal(String dashSysStageVal) {
        String stageId = "";
        if(BeDashboardConstant.BE_DASH_SYSTEM_SWITCH_ASO.equals(dashSysStageVal)) {
            stageId = HcsaConsts.ROUTING_STAGE_ASO;
        } else if(BeDashboardConstant.BE_DASH_SYSTEM_SWITCH_PSO.equals(dashSysStageVal)) {
            stageId = HcsaConsts.ROUTING_STAGE_PSO;
        } else if(BeDashboardConstant.BE_DASH_SYSTEM_SWITCH_PRE.equals(dashSysStageVal)) {
            stageId = HcsaConsts.ROUTING_STAGE_PRE;
        } else if(BeDashboardConstant.BE_DASH_SYSTEM_SWITCH_INS.equals(dashSysStageVal)) {
            stageId = HcsaConsts.ROUTING_STAGE_INP;
        } else if(BeDashboardConstant.BE_DASH_SYSTEM_SWITCH_POT.equals(dashSysStageVal)) {
            stageId = HcsaConsts.ROUTING_STAGE_POT;
        } else if(BeDashboardConstant.BE_DASH_SYSTEM_SWITCH_AO1.equals(dashSysStageVal)) {
            stageId = HcsaConsts.ROUTING_STAGE_AO1;
        } else if(BeDashboardConstant.BE_DASH_SYSTEM_SWITCH_AO2.equals(dashSysStageVal)) {
            stageId = HcsaConsts.ROUTING_STAGE_AO2;
        } else if(BeDashboardConstant.BE_DASH_SYSTEM_SWITCH_AO3.equals(dashSysStageVal)) {
            stageId = HcsaConsts.ROUTING_STAGE_AO3;
        }
        return stageId;
    }

    private List<SelectOption> getRenewAppStatusOptionByRole(String curRoleId, List<SelectOption> appStatusOption) {
        if(!StringUtil.isEmpty(curRoleId)) {
            if(curRoleId.contains(RoleConsts.USER_ROLE_ASO) ||
                    curRoleId.contains(RoleConsts.USER_ROLE_PSO) ||
                    curRoleId.equals(RoleConsts.USER_ROLE_INSPECTIOR)
            ) {
                appStatusOption = inspectionService.getAppStatusOption(curRoleId);
            } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO1)) {
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_ASO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_PSO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTIOR));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTION_LEAD));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO1));
            } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO2)) {
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_ASO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_PSO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTIOR));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTION_LEAD));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO1));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO2));
            } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO3)) {
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_ASO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_PSO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTIOR));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTION_LEAD));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO1));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO2));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO3));
            } else if (curRoleId.equals(RoleConsts.USER_ROLE_INSPECTION_LEAD)) {
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTIOR));
                appStatusOption.addAll(inspectionService.getAppStatusOption(curRoleId));
            }
        }
        return appStatusOption;
    }

    private List<SelectOption> getWaitAppStatusOptionByRole(String curRoleId, List<SelectOption> appStatusOption) {
        if(!StringUtil.isEmpty(curRoleId)) {
            if(curRoleId.contains(RoleConsts.USER_ROLE_INSPECTIOR)) {
                appStatusOption = MasterCodeUtil.retrieveOptionsByCodes(STATUS_STRS_INSPECTOR);
            } else if(curRoleId.contains(RoleConsts.USER_ROLE_PSO)) {
                appStatusOption = MasterCodeUtil.retrieveOptionsByCodes(STATUS_STRS_PSO);
            } else {
                appStatusOption = MasterCodeUtil.retrieveOptionsByCodes(STATUS_STRS_ELSE);
            }
        }
        return appStatusOption;
    }

    private List<SelectOption> getTeamAppStatusOptionByRole(String curRoleId, List<SelectOption> appStatusOption) {
        if(!StringUtil.isEmpty(curRoleId)) {
            if(curRoleId.contains(RoleConsts.USER_ROLE_ASO) ||
                    curRoleId.contains(RoleConsts.USER_ROLE_PSO) ||
                    curRoleId.equals(RoleConsts.USER_ROLE_INSPECTIOR)
            ) {
                appStatusOption = inspectionService.getAppStatusOption(curRoleId);
            } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO1)) {
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_ASO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_PSO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTIOR));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTION_LEAD));
            } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO2)) {
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_ASO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_PSO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTIOR));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTION_LEAD));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO1));
            } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO3)) {
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_ASO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_PSO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTIOR));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTION_LEAD));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO1));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO2));
            } else if (curRoleId.equals(RoleConsts.USER_ROLE_INSPECTION_LEAD)) {
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTIOR));
            }
        }
        return appStatusOption;
    }

    private List<SelectOption> delDuplicateStatusOption(List<SelectOption> appStatusOption) {
        if(!IaisCommonUtils.isEmpty(appStatusOption)) {
            List<SelectOption> appStatusOptionSet = IaisCommonUtils.genNewArrayList();
            List<String> appStatusKeys = IaisCommonUtils.genNewArrayList();
            for(SelectOption selectOption : appStatusOption) {
                if(selectOption != null) {
                    String value = selectOption.getValue();
                    if(!StringUtil.isEmpty(value) && !appStatusKeys.contains(value)) {
                        appStatusKeys.add(value);
                        appStatusOptionSet.add(selectOption);
                    }
                }
            }
            return appStatusOptionSet;
        }
        return appStatusOption;
    }

    private List<SelectOption> getKpiAppStatusOptionByRole(String curRoleId, List<SelectOption> appStatusOption) {
        if(!StringUtil.isEmpty(curRoleId)) {
            if(curRoleId.contains(RoleConsts.USER_ROLE_ASO) ||
               curRoleId.contains(RoleConsts.USER_ROLE_PSO) ||
               curRoleId.contains(RoleConsts.USER_ROLE_INSPECTIOR)
            ) {
                appStatusOption = inspectionService.getAppStatusOption(curRoleId);
            } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO1)) {
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_ASO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_PSO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTIOR));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTION_LEAD));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO1));
            } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO2)) {
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_ASO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_PSO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTIOR));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTION_LEAD));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO1));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO2));
            } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO3)) {
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_ASO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_PSO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTIOR));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTION_LEAD));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO1));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO2));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO3));
            }
        }
        return appStatusOption;
    }

    @Override
    public void createReportResult(TaskDto taskDto, ApplicationViewDto applicationViewDto, String userId) {
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        //Date time
        Date inspectionDate = null;
        AppPremisesRecommendationDto appPreRecommentdationDtoDate = applicationMainClient.getAppPremRecordByIdAndType(appPremisesCorrelationId, InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
        //add listReportNcRectifiedDto and add ncItemId
        AppPremPreInspectionNcDto appPremPreInspectionNcDto = applicationMainClient.getAppNcByAppCorrId(appPremisesCorrelationId).getEntity();
        if (appPreRecommentdationDtoDate != null) {
            inspectionDate = appPreRecommentdationDtoDate.getRecomInDate();
        }
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String applicationType = applicationDto.getApplicationType();
        String applicationDtoId = applicationDto.getId();
        String riskLevel = null;

        if (ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationType)) {
            HcsaRiskScoreDto hcsaRiskScoreDto = new HcsaRiskScoreDto();
            hcsaRiskScoreDto.setAppType(applicationType);
            hcsaRiskScoreDto.setLicId(applicationDto.getOriginLicenceId());
            List<ApplicationDto> applicationDtos = new ArrayList<>(1);
            applicationDtos.add(applicationDto);
            hcsaRiskScoreDto.setApplicationDtos(applicationDtos);
            hcsaRiskScoreDto.setServiceId(applicationDto.getServiceId());
            hcsaRiskScoreDto.setBeExistAppId(applicationDtoId);
            HcsaRiskScoreDto entity = hcsaConfigMainClient.getHcsaRiskScoreDtoByHcsaRiskScoreDto(hcsaRiskScoreDto).getEntity();
            riskLevel = entity.getRiskLevel();
        }

        ReportResultDto reportResultDto = new ReportResultDto();
        reportResultDto.setInspDate(inspectionDate);
        reportResultDto.setInspEnddate(new Date());
        reportResultDto.setAppPremId(appPremisesCorrelationId);
        reportResultDto.setRiskLevel(riskLevel);
        if (appPremPreInspectionNcDto != null) {
            reportResultDto.setNc(true);
        }else {
            reportResultDto.setNc(false);
        }
        saveReportResult(reportResultDto);
    }

    private void saveReportResult(ReportResultDto reportResultDto) {
        applicationMainClient.saveReportResult(reportResultDto).getEntity();
    }
}
