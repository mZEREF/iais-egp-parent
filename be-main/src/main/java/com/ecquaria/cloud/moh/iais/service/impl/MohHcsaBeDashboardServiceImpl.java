package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashComPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryMainService;
import com.ecquaria.cloud.moh.iais.service.MohHcsaBeDashboardService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusMainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public List<String> setPoolScopeByCurRoleId(SearchParam searchParam, LoginContext loginContext, String actionValue, List<String> workGroupIds) {
        if(loginContext != null) {
            String curRoleId = loginContext.getCurRoleId();
            if(!StringUtil.isEmpty(curRoleId)) {
                if (curRoleId.contains(RoleConsts.USER_LEAD) &&
                    !curRoleId.contains(RoleConsts.USER_ROLE_AO)) {
                    //for ASO / PSO / Inspector lead

                } else if (curRoleId.contains(RoleConsts.USER_ROLE_AO)) {
                    //for approver

                } else {
                    //for myself
                    if(loginContext != null && !StringUtil.isEmpty(loginContext.getCurRoleId())){
                        curRoleId = loginContext.getCurRoleId();
                    } else {
                        curRoleId = RoleConsts.USER_LEAD;
                    }
                    searchParam.addFilter("dashRoleId", curRoleId,true);
                    if(!("common".equals(actionValue))) {
                        searchParam.addFilter("officerId", loginContext.getUserId(), true);
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

    @Override
    @SearchTrack(catalog = "intraDashboardQuery", key = "dashCommonTask")
    public SearchResult<DashComPoolQueryDto> getDashComPoolResult(SearchParam searchParam) {
        return null;
    }

    @Override
    public SearchResult getDashComPoolOtherData(SearchResult searchResult) {
        return null;
    }
}
