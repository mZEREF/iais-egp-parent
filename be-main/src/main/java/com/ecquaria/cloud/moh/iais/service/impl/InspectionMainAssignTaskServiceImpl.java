package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.InspectionMainAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskMainClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Shicheng
 * @date 2019/11/22 10:19
 **/
@Service
@Slf4j
public class InspectionMainAssignTaskServiceImpl implements InspectionMainAssignTaskService {
    @Autowired
    private InspectionTaskMainClient inspectionTaskMainClient;

    @Autowired
    private HcsaConfigMainClient hcsaConfigClient;

    @Autowired
    private OrganizationMainClient organizationClient;

    @Override
    public HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId) {
        return hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
    }

    @Override
    public String taskRead(String taskId){
        return organizationClient.taskRead(taskId).getEntity();
    }

    @Override
    public AppGrpPremisesDto getAppGrpPremisesDtoByAppGroId(String appCorrId) {
        AppGrpPremisesDto appGrpPremisesDto = inspectionTaskMainClient.getAppGrpPremisesDtoByAppGroId(appCorrId).getEntity();
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
    public InspecTaskCreAndAssDto setFastTrackFlag(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, ApplicationDto applicationDto) {
        boolean fastTrackFlag = false;
        String appStatus = applicationDto.getStatus();
        if(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(appStatus)){
            fastTrackFlag = true;
        }
        if(applicationDto.isFastTracking()){
            inspecTaskCreAndAssDto.setFastTrackCheckFlag(true);
        } else {
            inspecTaskCreAndAssDto.setFastTrackCheckFlag(false);
        }
        inspecTaskCreAndAssDto.setFastTrackFlag(fastTrackFlag);
        return inspecTaskCreAndAssDto;
    }

    @Override
    public InspecTaskCreAndAssDto getInspecTaskCreAndAssDto(ApplicationDto applicationDto, LoginContext loginContext, InspecTaskCreAndAssDto inspecTaskCreAndAssDto) {
        List<OrgUserDto> orgUserDtos = IaisCommonUtils.genNewArrayList();
        TaskDto taskDto = inspecTaskCreAndAssDto.getTaskDto();
        String workGroupId = "";
        String appCorrelationId = "";
        if (taskDto != null) {
            orgUserDtos = organizationClient.getUsersByWorkGroupName(taskDto.getWkGrpId(), AppConsts.COMMON_STATUS_ACTIVE).getEntity();
            workGroupId = taskDto.getWkGrpId();
            appCorrelationId = taskDto.getRefNo();
        }

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
            inspecTaskCreAndAssDto.setHciName(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
        } else {
            inspecTaskCreAndAssDto.setHciName(StringUtil.viewHtml(address));
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
        setInspectorLeadRecom(inspecTaskCreAndAssDto, appCorrelationId, workGroupId);
        return inspecTaskCreAndAssDto;
    }

    private void setInspectorLeadRecom(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, String appCorrelationId, String workGroupId) {
        if(!StringUtil.isEmpty(workGroupId)) {
            WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(workGroupId).getEntity();
            String workGroupName = workingGroupDto.getGroupName();
            if (!StringUtil.isEmpty(workGroupName) && workGroupName.contains("Inspection")) {
                AppPremisesRecommendationDto appPremisesRecommendationDto = inspectionTaskMainClient.getAppPremRecordByIdAndType(appCorrelationId, InspectionConstants.RECOM_TYPE_INSPECTION_LEAD).getEntity();
                if (appPremisesRecommendationDto == null) {
                    List<String> leadNames = inspecTaskCreAndAssDto.getInspectionLeads();
                    if (!IaisCommonUtils.isEmpty(leadNames)) {
                        String nameStr = "";
                        for (String name : leadNames) {
                            if (StringUtil.isEmpty(nameStr)) {
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
                        inspectionTaskMainClient.saveAppRecom(appPremisesRecommendationDto);
                    }
                }
            }
        }
    }

    public void setInspectorLeadName(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, List<OrgUserDto> orgUserDtos, String workGroupId) {
        if (StringUtil.isEmpty(workGroupId)) {
            return;
        }
        List<String> leadNames = IaisCommonUtils.genNewArrayList();
        List<String> leadIds = organizationClient.getInspectionLead(workGroupId).getEntity();
        StringBuilder leadStrBu = new StringBuilder();
        Collections.sort(leadIds);
        for (String id : leadIds) {
            for (OrgUserDto oDto : orgUserDtos) {
                if (id.equals(oDto.getId())) {
                    leadNames.add(oDto.getDisplayName());
                    if(StringUtil.isEmpty(leadStrBu.toString())) {
                        leadStrBu.append(oDto.getDisplayName());
                    } else {
                        leadStrBu.append(',');
                        leadStrBu.append(' ');
                        leadStrBu.append(oDto.getDisplayName());
                    }
                }
            }
        }
        inspecTaskCreAndAssDto.setInspectionLeads(leadNames);
        inspecTaskCreAndAssDto.setGroupLeadersShow(leadStrBu.toString());
    }

    private void setInspectorByOrgUserDto(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, List<OrgUserDto> orgUserDtos, LoginContext loginContext) {
        if (orgUserDtos == null || orgUserDtos.size() <= 0) {
            inspecTaskCreAndAssDto.setInspector(null);
            return;
        }
        List<SelectOption> inspectorList = IaisCommonUtils.genNewArrayList();
        List<String> roles = loginContext.getRoleIds();
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

    public ApplicationGroupDto getApplicationGroupDtoByAppGroId(String appGroupId) {
        return inspectionTaskMainClient.getApplicationGroupDtoByAppGroId(appGroupId).getEntity();
    }

    @Override
    public InspecTaskCreAndAssDto setEditHoursFlagByAppAndUser(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, ApplicationDto applicationDto) {
        List<String> appHoursStatusList = IaisCommonUtils.genNewArrayList();
        appHoursStatusList.add(ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL);
        appHoursStatusList.add(ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT);
        appHoursStatusList.add(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT);
        String appStatus = "";
        if(applicationDto != null) {
            appStatus = applicationDto.getStatus();
        }
        if(appHoursStatusList.contains(appStatus)) {//NOSONAR
            inspecTaskCreAndAssDto.setEditHoursFlag(AppConsts.COMMON_POOL);
        }
        return inspecTaskCreAndAssDto;
    }
}
