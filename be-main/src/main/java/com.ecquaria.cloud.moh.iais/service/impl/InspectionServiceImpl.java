package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Shicheng
 * @date 2019/11/19 14:45
 **/
@Service
@Slf4j
public class InspectionServiceImpl implements InspectionService {
    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private OrganizationClient organizationClient;


    @Override
    public List<SelectOption> getAppTypeOption() {
        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION, ApplicationConsts.APPLICATION_TYPE_RENEWAL, ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE});
        return appTypeOption;
    }

    @Override
    public List<SelectOption> getAppStatusOption() {
        String[] statusStrs = new String[]{
                ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION,
                ApplicationConsts.APPLICATION_STATUS_APPROVED,
                ApplicationConsts.APPLICATION_STATUS_REJECTED,
                ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS,
                ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING,
                ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01,
                ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02,
                ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03
                                         };
        List<SelectOption> appStatusOption = MasterCodeUtil.retrieveOptionsByCodes(statusStrs);
        return appStatusOption;
    }

    @Override
    public List<TaskDto> getSupervisorPoolByGroupWordId(String workGroupId) {
        return organizationClient.getSupervisorPoolByGroupWordId(workGroupId).getEntity();
    }

    @Override
    public String[] getApplicationNoListByPool(List<TaskDto> commPools) {
        if(commPools == null || commPools.size() <= 0){
            return null;
        }
        Set<String> applicationNoSet = new HashSet<>();
        for(TaskDto tDto:commPools){
            applicationNoSet.add(tDto.getRefNo());
        }
        List<String> applicationNoList = new ArrayList<>(applicationNoSet);
        String[] applicationStrs = new String[applicationNoList.size()];
        for(int i = 0; i < applicationStrs.length; i++){
            applicationStrs[i] = applicationNoList.get(i);
        }
        return applicationStrs;
    }

    @Override
    public HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId){
        return hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
    }



    @Override
    public List<String> getWorkGroupIdsByLogin(LoginContext loginContext) {
        List<String> workGroupIdList = new ArrayList<>();
        List<UserGroupCorrelationDto> userGroupCorrelationDtos = organizationClient.getUserGroupCorreByUserId(loginContext.getUserId()).getEntity();
        for(UserGroupCorrelationDto ugcDto:userGroupCorrelationDtos){
            if(ugcDto.getIsLeadForGroup() == 1){
                workGroupIdList.add(ugcDto.getGroupId());
            }
        }
        return workGroupIdList;
    }

    @Override
    public InspectionTaskPoolListDto inputInspectorOption(InspectionTaskPoolListDto inspectionTaskPoolListDto, LoginContext loginContext) {
        List<SelectOption> inspectorOption = new ArrayList<>();
        List<OrgUserDto> orgUserDtoList = organizationClient.getUsersByWorkGroupName(inspectionTaskPoolListDto.getWorkGroupId(), AppConsts.COMMON_STATUS_ACTIVE).getEntity();
        String flag = AppConsts.FALSE;
        Set<String> roles = loginContext.getRoleIds();
        List<String> roleList = new ArrayList<>(roles);
        if(roleList.contains(RoleConsts.USER_ROLE_INSPECTIOR)){
            flag = AppConsts.TRUE;
        }
        for(OrgUserDto oDto:orgUserDtoList){
            if(!(oDto.getId().equals(loginContext.getUserId()))){
                SelectOption so = new SelectOption(oDto.getId(), oDto.getUserName());
                inspectorOption.add(so);
            } else {
                if(AppConsts.TRUE.equals(flag)){
                    SelectOption so = new SelectOption(oDto.getId(), oDto.getUserName());
                    inspectorOption.add(so);
                }
            }
        }
        inspectionTaskPoolListDto.setInspectorOption(inspectorOption);
        return inspectionTaskPoolListDto;
    }

    @Override
    public List<SelectOption> getInspectorOptionByLogin(LoginContext loginContext, List<String> workGroupIds) {
        List<SelectOption> inspectorOption = new ArrayList<>();
        if(workGroupIds == null || workGroupIds.size() <= 0){
            return null;
        }
        List<String> userIdList = new ArrayList<>();
        for(String workId:workGroupIds){
            List<OrgUserDto> orgUserDtoList = organizationClient.getUsersByWorkGroupName(workId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
            userIdList = getUserIdList(orgUserDtoList, userIdList);
        }
        for(String userId:userIdList){
            List<TaskDto> taskDtoList = organizationClient.getTasksByUserId(userId).getEntity();
            OrgUserDto orgUserDto =organizationClient.retrieveOneOrgUserAccount(userId).getEntity();
            String value = AppConsts.NO;
            if(taskDtoList != null && taskDtoList.size() > 0){
                value = getOptionValue(taskDtoList);
            }
            SelectOption so = new SelectOption(value, orgUserDto.getUserName());
            inspectorOption.add(so);
        }
        return inspectorOption;
    }

    private String getOptionValue(List<TaskDto> taskDtoList) {
        String value = taskDtoList.get(0).getRefNo();
        taskDtoList.remove(0);
        if(taskDtoList != null && taskDtoList.size() > 0) {
            for (TaskDto tDto : taskDtoList) {
                value = value + "," + tDto.getRefNo();
            }
        }
        return value;
    }

    private List<String> getUserIdList(List<OrgUserDto> orgUserDtoList, List<String> userIdList) {
        for(OrgUserDto oDto:orgUserDtoList){
            userIdList.add(oDto.getId());
        }
        return userIdList;
    }



    @Override
    public SearchResult<InspectionSubPoolQueryDto> getSupPoolByParam(SearchParam searchParam) {
        return inspectionTaskClient.searchInspectionSupPool(searchParam).getEntity();
    }

    @Override
    public SearchResult<InspectionTaskPoolListDto> getOtherDataForSr(SearchResult<InspectionSubPoolQueryDto> searchResult, List<TaskDto> commPools, LoginContext loginContext) {
        List<InspectionTaskPoolListDto> inspectionTaskPoolListDtoList = new ArrayList<>();
        if(commPools == null || commPools.size() <= 0){
            return null;
        }
        for(TaskDto tDto:commPools){
            InspectionTaskPoolListDto inspectionTaskPoolListDto = new InspectionTaskPoolListDto();
            inspectionTaskPoolListDto.setApplicationNo(tDto.getRefNo());
            inspectionTaskPoolListDto.setTaskId(tDto.getId());
            if(StringUtil.isEmpty(tDto.getUserId())){
                inspectionTaskPoolListDto.setInspectorName("");
            } else {
                inspectionTaskPoolListDto.setInspector(tDto.getUserId());
                OrgUserDto orgUserDto = organizationClient.retrieveOneOrgUserAccount(tDto.getUserId()).getEntity();
                inspectionTaskPoolListDto.setInspectorName(orgUserDto.getUserName());
            }
            inspectionTaskPoolListDto.setWorkGroupId(tDto.getWkGrpId());
            inspectionTaskPoolListDtoList.add(inspectionTaskPoolListDto);
        }
        OrgUserDto orgUserDto = organizationClient.retrieveOneOrgUserAccount(loginContext.getUserId()).getEntity();
        inspectionTaskPoolListDtoList = inputOtherData(searchResult.getRows(), inspectionTaskPoolListDtoList, orgUserDto);

        SearchResult<InspectionTaskPoolListDto> searchResult2 = new SearchResult<>();
        searchResult2.setRows(inspectionTaskPoolListDtoList);
        searchResult2.setRowCount(inspectionTaskPoolListDtoList.size());
        return searchResult2;
    }

    private List<InspectionTaskPoolListDto> inputOtherData(List<InspectionSubPoolQueryDto> rows, List<InspectionTaskPoolListDto> inspectionTaskPoolListDtoList, OrgUserDto orgUserDto) {
        for(InspectionSubPoolQueryDto iDto: rows){
            for(InspectionTaskPoolListDto itplDto:inspectionTaskPoolListDtoList){
                if((iDto.getApplicationNo()).equals(itplDto.getApplicationNo())){
                    itplDto.setServiceId(iDto.getServiceId());
                    HcsaServiceDto hcsaServiceDto = getHcsaServiceDtoByServiceId(iDto.getServiceId());
                    itplDto.setServiceName(hcsaServiceDto.getSvcName());
                    itplDto.setApplicationStatus(iDto.getApplicationStatus());
                    itplDto.setApplicationType(iDto.getApplicationType());
                    itplDto.setHciCode(iDto.getHciCode());
                    AppGrpPremisesDto appGrpPremisesDto = getAppGrpPremisesDtoByAppGroId(iDto.getId());
                    itplDto.setHciName(iDto.getHciName() + " / " + appGrpPremisesDto.getAddress());
                    itplDto.setSubmitDt(iDto.getSubmitDt());
                    itplDto.setApplicationType(iDto.getApplicationType());
                    itplDto.setInspectionTypeName(iDto.getInspectionType() == 0? "Post":"Pre");
                    itplDto.setServiceEndDate(hcsaServiceDto.getEndDate());
                    itplDto.setInspectionDate(new Date());
                    itplDto.setInspectorLead(orgUserDto.getUserName());
                }
            }
        }
        return inspectionTaskPoolListDtoList;
    }





    /**
     * @author: shicheng
     * @Date 2019/11/23
     * @Param: appGroupId
     * @return: AppGrpPremisesDto
     * @Descripation: get Application Group Premises By Application Id
     */
    public AppGrpPremisesDto getAppGrpPremisesDtoByAppGroId(String applicationId){
        return inspectionTaskClient.getAppGrpPremisesDtoByAppGroId(applicationId).getEntity();
    }
}
