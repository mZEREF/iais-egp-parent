package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.BeDashboardConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.PoolRoleCheckDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashAssignMeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashComPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashKpiPoolQuery;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashRenewQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashReplyQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashWaitApproveQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashWorkTeamQueryDto;
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
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskMainClient;
import com.ecquaria.cloud.moh.iais.service.client.IntraDashboardClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import com.ecquaria.cloud.role.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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


    @Override
    public AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus, String decision,
                                                                         TaskDto taskDto, String userId, String remarks, String subStage) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(HcsaConsts.ROUTING_STAGE_AO1);
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
                    //for approver
                    workGroupIds = getByAo1AndLead(searchParam, loginContext, switchAction);
                } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO2) || curRoleId.contains(RoleConsts.USER_ROLE_AO3)) {
                    //for approver
                    workGroupIds = getByAo2Ao3AndLead(searchParam, loginContext, switchAction);
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
        searchParam.addFilter("dashRoleId", memberRole, true);
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

    private List<SelectOption> getRenewAppStatusOptionByRole(String curRoleId, List<SelectOption> appStatusOption) {
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
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO1));
            } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO2)) {
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_ASO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_PSO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTIOR));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO1));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO2));
            } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO3)) {
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_ASO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_PSO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTIOR));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO1));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO2));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO3));
            }
        }
        return appStatusOption;
    }

    private List<SelectOption> getWaitAppStatusOptionByRole(String curRoleId, List<SelectOption> appStatusOption) {
        if(!StringUtil.isEmpty(curRoleId)) {
            String[] statusStrs = new String[]{
                ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS,
                ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01,
                ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02,
                ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03,
            };
            appStatusOption = MasterCodeUtil.retrieveOptionsByCodes(statusStrs);
        }
        return appStatusOption;
    }

    private List<SelectOption> getTeamAppStatusOptionByRole(String curRoleId, List<SelectOption> appStatusOption) {
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
            } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO2)) {
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_ASO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_PSO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTIOR));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO1));
            } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO3)) {
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_ASO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_PSO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTIOR));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO1));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO2));
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
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO1));
            } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO2)) {
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_ASO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_PSO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTIOR));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO1));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO2));
            } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO3)) {
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_ASO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_PSO));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_INSPECTIOR));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO1));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO2));
                appStatusOption.addAll(inspectionService.getAppStatusOption(RoleConsts.USER_ROLE_AO3));
            }
        }
        return appStatusOption;
    }
}
