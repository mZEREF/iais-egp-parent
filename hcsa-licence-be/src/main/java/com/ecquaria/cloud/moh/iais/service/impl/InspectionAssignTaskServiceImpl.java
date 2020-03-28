package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ComPoolAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

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
    private OrganizationClient organizationClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private ApplicationService applicationService;

    @Override
    public List<TaskDto> getCommPoolByGroupWordId(LoginContext loginContext) {
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
        Set<String> workGrpIds = loginContext.getWrkGrpIds();
        if(workGrpIds == null || workGrpIds.size() <= 0){
            return null;
        }
        List<String> workGrpIdList = new ArrayList<>(workGrpIds);
        for(String workGrpId:workGrpIdList){
            for(TaskDto tDto:taskService.getCommPoolByGroupWordId(workGrpId)){
                taskDtoList.add(tDto);
            }
        }
        return taskDtoList;
    }

    @Override
    public InspecTaskCreAndAssDto getInspecTaskCreAndAssDto(String appCorrelationId, List<TaskDto> commPools, LoginContext loginContext,
                                                            InspecTaskCreAndAssDto inspecTaskCreAndAssDto) {
        List<OrgUserDto> orgUserDtos = IaisCommonUtils.genNewArrayList();
        String workGroupId = "";
        for(TaskDto tDto:commPools){
            if(appCorrelationId.equals(tDto.getRefNo())){
                orgUserDtos =  organizationClient.getUsersByWorkGroupName(tDto.getWkGrpId(), AppConsts.COMMON_STATUS_ACTIVE).getEntity();
            }
            if(inspecTaskCreAndAssDto.getTaskId().equals(tDto.getId())){
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
        if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
            inspecTaskCreAndAssDto.setHciName(appGrpPremisesDto.getHciName() + " / " + address);
        } else {
            inspecTaskCreAndAssDto.setHciName(address);
        }
        inspecTaskCreAndAssDto.setHciName(appGrpPremisesDto.getHciName() + " / " + address);
        inspecTaskCreAndAssDto.setHciCode(appGrpPremisesDto.getHciCode());
        inspecTaskCreAndAssDto.setServiceName(hcsaServiceDto.getSvcName());
        //todo:inspection type
        inspecTaskCreAndAssDto.setInspectionTypeName(InspectionConstants.INSPECTION_TYPE_ONSITE);
        inspecTaskCreAndAssDto.setInspectionType(applicationGroupDto.getIsPreInspection());
        inspecTaskCreAndAssDto.setSubmitDt(applicationGroupDto.getSubmitDt());
        //set inspector checkbox list
        setInspectorByOrgUserDto(inspecTaskCreAndAssDto, orgUserDtos, loginContext);
        setInspectorLeadName(inspecTaskCreAndAssDto, orgUserDtos, workGroupId);
        return inspecTaskCreAndAssDto;
    }

    @Override
    public void setInspectorLeadName(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, List<OrgUserDto> orgUserDtos, String workGroupId) {
        if(StringUtil.isEmpty(workGroupId)){
            return;
        }
        List<String> leadNames = IaisCommonUtils.genNewArrayList();
        List<String> leadIds = organizationClient.getInspectionLead(workGroupId).getEntity();
        for(String id : leadIds){
            for(OrgUserDto oDto : orgUserDtos){
                if(id.equals(oDto.getId())){
                    leadNames.add(oDto.getDisplayName());
                }
            }
        }
        inspecTaskCreAndAssDto.setInspectionLeads(leadNames);
    }

    private void setInspectorByOrgUserDto(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, List<OrgUserDto> orgUserDtos, LoginContext loginContext) {
        if(orgUserDtos == null || orgUserDtos.size() <= 0){
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
        if(curRole.contains(RoleConsts.USER_LEAD)){
            leadRole = curRole;
        } else {
            leadRole = curRole + RoleConsts.USER_LEAD;
        }
        if(roleList.contains(leadRole)){
            addInspector(inspectorList, orgUserDtos, loginContext, roleList);
        } else {
            for(OrgUserDto oDto:orgUserDtos){
                if(oDto.getId().equals(loginContext.getUserId())){
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
        if(curRole.contains(RoleConsts.USER_LEAD)){
            memberRole = curRole.replaceFirst(RoleConsts.USER_LEAD, "");
        } else {
            memberRole = curRole;
        }
        if(roleList.contains(memberRole)){
            flag = AppConsts.TRUE;
        }
        for(OrgUserDto oDto:orgUserDtos){
            if(!(oDto.getId().equals(loginContext.getUserId()))){
                SelectOption so = new SelectOption(oDto.getId(), oDto.getDisplayName());
                inspectorList.add(so);
            } else {
                if(AppConsts.TRUE.equals(flag)){
                    SelectOption so = new SelectOption(oDto.getId(), oDto.getDisplayName());
                    inspectorList.add(so);
                }
            }
        }
    }

    @Override
    @SearchTrack(catalog = "inspectionQuery",key = "assignCommonTask")
    public SearchResult<InspectionCommonPoolQueryDto> getSearchResultByParam(SearchParam searchParam) {
        return inspectionTaskClient.searchInspectionPool(searchParam).getEntity();
    }

    @Override
    public List<String> getAppCorrIdListByPool(List<TaskDto> commPools) {
        if(IaisCommonUtils.isEmpty(commPools)){
            List<String> appCorrIdList = IaisCommonUtils.genNewArrayList();
            appCorrIdList.add(AppConsts.NO);
            return appCorrIdList;
        }
        Set<String> appCorrIdSet = IaisCommonUtils.genNewHashSet();
        for(TaskDto tDto:commPools){
            appCorrIdSet.add(tDto.getRefNo());
        }
        List<String> appCorrIdList = new ArrayList<>(appCorrIdSet);

        return appCorrIdList;
    }

    @Override
    public List<SelectOption> getAppTypeOption() {
        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION, ApplicationConsts.APPLICATION_TYPE_RENEWAL, ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE});
        return appTypeOption;
    }

    @Override
    public void assignTaskForInspectors(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto,
                                        ApplicationViewDto applicationViewDto, String internalRemarks, TaskDto taskDto) {
        try {
            List<SelectOption> inspectorCheckList = inspecTaskCreAndAssDto.getInspectorCheck();
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();

            List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
            applicationDtos.add(applicationDto);
            List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos,HcsaConsts.ROUTING_STAGE_INS);
            hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
            for(TaskDto td:commPools) {
                if (td.getId().equals(inspecTaskCreAndAssDto.getTaskId())) {
                    td.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
                    td.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    inspecTaskCreAndAssDto.setTaskDto(td);
                    inspecTaskCreAndAssDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    inspecTaskCreAndAssDto.setTaskDtos(commPools);
                    inspecTaskCreAndAssDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
                    organizationClient.assignCommonPool(inspecTaskCreAndAssDto);
                    AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorySubStage(td.getRefNo(), td.getTaskKey()).getEntity();
                    createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), internalRemarks,
                                                    InspectionConstants.PROCESS_DECI_COMMON_POOL_ASSIGN, td.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), td.getWkGrpId());
                    if(inspectorCheckList != null && inspectorCheckList.size() > 0){
                        for(int i = 0; i < inspectorCheckList.size(); i++){
                            if(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(applicationDto.getStatus())){
                                //todo:set authentic Application status
                                ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
                                applicationService.updateFEApplicaiton(applicationDto1);
                                inspecTaskCreAndAssDto.setApplicationStatus(applicationDto1.getStatus());
                                createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(), applicationDto1.getStatus(), taskDto.getTaskKey(), null, null, td.getRoleId(), null, td.getWkGrpId());
                            } else {
                                createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), null, null, td.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), td.getWkGrpId());
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            log.error(StringUtil.changeForLog("Error when Submit Assign Task Project: "), e);
            throw e;
        }
    }

    private ApplicationDto updateApplication(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationViewService.updateApplicaiton(applicationDto);
    }

    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId){
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = new ArrayList();
        for(ApplicationDto applicationDto : applicationDtos){
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
        for(InspectionCommonPoolQueryDto icpqDto: searchResult.getRows()){
            if(1 == icpqDto.getAppCount()){
                icpqDto.setSubmissionType(AppConsts.PAYMENT_STATUS_SINGLE);
            } else if(1 < icpqDto.getAppCount()) {
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
    public void routingTaskByCommonPool(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto, String internalRemarks) {
        TaskDto taskDto = getTaskDtoByPool(commPools, inspecTaskCreAndAssDto);
        ApplicationViewDto applicationViewDto = searchByAppCorrId(inspecTaskCreAndAssDto.getAppCorrelationId());
        assignTaskForInspectors(commPools, inspecTaskCreAndAssDto, applicationViewDto, internalRemarks, taskDto);
    }

    private TaskDto getTaskDtoByPool(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto) {
        TaskDto taskDto = new TaskDto();
        for(TaskDto tDto:commPools){
            if(tDto.getId().equals(inspecTaskCreAndAssDto.getTaskId())){
                taskDto = tDto;
            }
        }
        return taskDto;
    }

    /**
      * @author: shicheng
      * @Date 2019/11/22
      * @Param: serviceId
      * @return: HcsaServiceDto
      * @Descripation: get HcsaServiceDto By Service Id
      */
    public HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId){
        return hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
    }

    @Override
    public AppGrpPremisesDto getAppGrpPremisesDtoByAppGroId(String appCorrId){
        AppGrpPremisesDto appGrpPremisesDto = inspectionTaskClient.getAppGrpPremisesDtoByAppGroId(appCorrId).getEntity();
        if(StringUtil.isEmpty(appGrpPremisesDto.getHciName())){
            appGrpPremisesDto.setHciName("");
        }
        if(StringUtil.isEmpty(appGrpPremisesDto.getHciCode())){
            appGrpPremisesDto.setHciCode(HcsaConsts.HCSA_PREMISES_HCI_NULL);
        }
        setAddressByGroupPremises(appGrpPremisesDto);
        if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            appGrpPremisesDto.setConveyanceBlockNo(appGrpPremisesDto.getBlkNo());
            appGrpPremisesDto.setConveyanceStreetName(appGrpPremisesDto.getStreetName());
            appGrpPremisesDto.setConveyanceBuildingName(appGrpPremisesDto.getBuildingName());
            appGrpPremisesDto.setConveyanceFloorNo(appGrpPremisesDto.getFloorNo());
            appGrpPremisesDto.setConveyanceUnitNo(appGrpPremisesDto.getUnitNo());
            appGrpPremisesDto.setConveyancePostalCode(appGrpPremisesDto.getPostalCode());
        }
        return appGrpPremisesDto;
    }

    private void setAddressByGroupPremises(AppGrpPremisesDto appGrpPremisesDto) {
        if(StringUtil.isEmpty(appGrpPremisesDto.getBlkNo())){
            appGrpPremisesDto.setBlkNo("");
        }
        if(StringUtil.isEmpty(appGrpPremisesDto.getStreetName())){
            appGrpPremisesDto.setStreetName("");
        }
        if(StringUtil.isEmpty(appGrpPremisesDto.getBuildingName())){
            appGrpPremisesDto.setBuildingName("");
        }
        if(StringUtil.isEmpty(appGrpPremisesDto.getFloorNo())){
            appGrpPremisesDto.setFloorNo("");
        }
        if(StringUtil.isEmpty(appGrpPremisesDto.getUnitNo())){
            appGrpPremisesDto.setUnitNo("");
        }
        if(StringUtil.isEmpty(appGrpPremisesDto.getPostalCode())){
            appGrpPremisesDto.setPostalCode("");
        }
    }

    @Override
    public String getAddress(AppGrpPremisesDto appGrpPremisesDto){
        String result = "";
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
            if(!StringUtil.isEmpty(appGrpPremisesDto.getBlkNo())){
                result = result + appGrpPremisesDto.getBlkNo() + " ";
            }
            if(!StringUtil.isEmpty(appGrpPremisesDto.getStreetName())){
                result = result + appGrpPremisesDto.getStreetName() + " ";
            }
            if(!StringUtil.isEmpty(appGrpPremisesDto.getBuildingName())){
                result = result + appGrpPremisesDto.getBuildingName() + " ";
            }
            if(!StringUtil.isEmpty(appGrpPremisesDto.getFloorNo())){
                String floorNo = appGrpPremisesDto.getFloorNo();
                Pattern pattern = compile("[0-9]*");
                boolean noFlag =  pattern.matcher(floorNo).matches();
                if (noFlag) {
                    int floorNum  = Integer.valueOf(floorNo);
                    if(10 > floorNum){
                        floorNo = AppConsts.NO + floorNo;
                        result = result + " # " + floorNo;
                    } else {
                        result = result + " # " + floorNo;
                    }
                } else {
                    result = result + " # " + floorNo;
                }
            }
            if(!StringUtil.isEmpty(appGrpPremisesDto.getUnitNo())){
                result = result + (StringUtil.isEmpty(appGrpPremisesDto.getFloorNo())?"":"-") + appGrpPremisesDto.getUnitNo();
            }
            if(!StringUtil.isEmpty(appGrpPremisesDto.getPostalCode())){
                result = result + ", "+ appGrpPremisesDto.getPostalCode();
            }
        }else{
            result = getOtherAddress(appGrpPremisesDto, result);
        }

        return result;
    }

    @Override
    @SearchTrack(catalog = "inspectionQuery",key = "assignCommonTask")
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
        if(curRole.contains(RoleConsts.USER_LEAD)){
            leadRole = curRole;
            otherRole = curRole.replaceFirst(RoleConsts.USER_LEAD, "");
        } else {
            leadRole = curRole + RoleConsts.USER_LEAD;
            otherRole = curRole;
        }
        if(!StringUtil.isEmpty(leadRole)) {
            if (RoleConsts.USER_ROLE_INSPECTION_LEAD.equals(curRole)) {
                groupLeadName = MasterCodeUtil.getCodeDesc(RoleConsts.USER_MASTER_INSPECTION_LEAD);
            } else {
                groupLeadName = MasterCodeUtil.getCodeDesc(leadRole);
            }
        }
        if(!StringUtil.isEmpty(otherRole)) {
            groupMemBerName = MasterCodeUtil.getCodeDesc(otherRole);
        }
        groupRoleFieldDto.setGroupLeadName(groupLeadName);
        groupRoleFieldDto.setGroupMemBerName(groupMemBerName);
        return groupRoleFieldDto;
    }

    private String getOtherAddress(AppGrpPremisesDto appGrpPremisesDto, String result) {
        if(!StringUtil.isEmpty(appGrpPremisesDto.getConveyanceBlockNo())){
            result = result + appGrpPremisesDto.getConveyanceBlockNo() + " ";
        }
        if(!StringUtil.isEmpty(appGrpPremisesDto.getConveyanceStreetName())){
            result = result + appGrpPremisesDto.getConveyanceStreetName() + " ";
        }
        if(!StringUtil.isEmpty(appGrpPremisesDto.getConveyanceBuildingName())){
            result = result + appGrpPremisesDto.getConveyanceBuildingName() + " ";
        }
        if(!StringUtil.isEmpty(appGrpPremisesDto.getConveyanceFloorNo())){
            String floorNo = appGrpPremisesDto.getConveyanceFloorNo();
            Pattern pattern = compile("[0-9]*");
            boolean noFlag =  pattern.matcher(floorNo).matches();
            if (noFlag) {
                int floorNum  = Integer.valueOf(floorNo);
                if(10 > floorNum){
                    floorNo = AppConsts.NO + floorNo;
                    result = result + " # " + floorNo;
                } else {
                    result = result + " # " + floorNo;
                }
            } else {
                result = result + " # " + floorNo;
            }
        }
        if(!StringUtil.isEmpty(appGrpPremisesDto.getConveyanceUnitNo())){
            result = result + (StringUtil.isEmpty(appGrpPremisesDto.getConveyanceFloorNo())?"":"-") + appGrpPremisesDto.getConveyanceUnitNo();
        }
        if(!StringUtil.isEmpty(appGrpPremisesDto.getConveyancePostalCode())){
            result = result + ", "+ appGrpPremisesDto.getConveyancePostalCode();
        }
        return result;
    }

    /**
      * @author: shicheng
      * @Date 2019/11/23
      * @Param: appGroupId
      * @return: ApplicationGroupDto
      * @Descripation: get ApplicationGroup By Application Group Id
      */
    public ApplicationGroupDto getApplicationGroupDtoByAppGroId(String appGroupId){
        return inspectionTaskClient.getApplicationGroupDtoByAppGroId(appGroupId).getEntity();
    }
}
