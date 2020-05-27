package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonAvailabilityDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.InspSupAddAvailabilityService;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * InspSupAddAvailabilityServiceImpl
 *
 * @author Shicheng
 * @date 2020/1/13 13:54
 **/
@Service
@Slf4j
public class InspSupAddAvailabilityServiceImpl implements InspSupAddAvailabilityService {

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private AppointmentClient appointmentClient;

    @Override
    public List<SelectOption> getRecurrenceOption() {
        List<SelectOption> recurrenceOption = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DATE_TYPE);
        return recurrenceOption;
    }

    @Override
    public OrgUserDto getOrgUserDtoById(String userId) {
        OrgUserDto orgUserDto = new OrgUserDto();
        if(!StringUtil.isEmpty(userId)) {
            orgUserDto = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
        }
        return orgUserDto;
    }

    @Override
    public ApptNonAvailabilityDateDto getApptNonAvailabilityDateDtoById(String nonAvaId) {
        ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = new ApptNonAvailabilityDateDto();
        if(!StringUtil.isEmpty(nonAvaId)) {
            apptNonAvailabilityDateDto = appointmentClient.getNonAvailabilityById(nonAvaId).getEntity();
        }
        return apptNonAvailabilityDateDto;
    }

    @Override
    public void deleteNonAvailabilityById(String removeId) {
        ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = getApptNonAvailabilityDateDtoById(removeId);
        apptNonAvailabilityDateDto.setNonAvaStatus(AppConsts.COMMON_STATUS_DELETED);
        updateNonAvailability(apptNonAvailabilityDateDto);
    }

    @Override
    public ApptNonAvailabilityDateDto createNonAvailability(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto) {
        if(apptNonAvailabilityDateDto != null){
            return appointmentClient.createNonAvailability(apptNonAvailabilityDateDto).getEntity();
        }
        return null;
    }

    @Override
    public ApptNonAvailabilityDateDto updateNonAvailability(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto) {
        if(apptNonAvailabilityDateDto != null){
            return appointmentClient.updateNonAvailability(apptNonAvailabilityDateDto).getEntity();
        }
        return null;
    }

    @Override
    public String dateIsContainNonWork(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto) {
        String dateContainFlag = AppConsts.FALSE;
        if(apptNonAvailabilityDateDto != null) {
            dateContainFlag = appointmentClient.dateIsContainNonWork(apptNonAvailabilityDateDto).getEntity();
        }
        return dateContainFlag;
    }

    @Override
    public List<String> getWorkGroupIdsByLogin(LoginContext loginContext) {
        List<String> workGroupIdList = IaisCommonUtils.genNewArrayList();
        List<UserGroupCorrelationDto> userGroupCorrelationDtos = organizationClient.getUserGroupCorreByUserId(loginContext.getUserId()).getEntity();
        for(UserGroupCorrelationDto ugcDto:userGroupCorrelationDtos){
            if(ugcDto.getIsLeadForGroup() == 1){
                workGroupIdList.add(ugcDto.getGroupId());
            }
        }
        return workGroupIdList;
    }

    @Override
    public GroupRoleFieldDto getInspectorOptionByLogin(LoginContext loginContext, List<String> workGroupIds, GroupRoleFieldDto groupRoleFieldDto) {
        List<SelectOption> inspectorOption = IaisCommonUtils.genNewArrayList();
        Map<String, String> userIdMap = IaisCommonUtils.genNewHashMap();
        if(IaisCommonUtils.isEmpty(workGroupIds)){
            return null;
        }
        List<String> userIdList = IaisCommonUtils.genNewArrayList();
        for(String workId:workGroupIds){
            List<OrgUserDto> orgUserDtoList = organizationClient.getUsersByWorkGroupName(workId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
            userIdList = getUserIdList(orgUserDtoList, userIdList);
            Set<String> userIdSet = new HashSet<>(userIdList);
            userIdList = new ArrayList<>(userIdSet);
        }
        List<OrgUserDto> orgUserDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(userIdList)){
            orgUserDtos = organizationClient.retrieveOrgUserAccount(userIdList).getEntity();
        }
        //get Login Id and User Name
        if(orgUserDtos != null && !(orgUserDtos.isEmpty())){
            for(int i = 0; i < orgUserDtos.size(); i++){
                userIdMap.put(i + "", orgUserDtos.get(i).getUserId());
                SelectOption so = new SelectOption(i + "", orgUserDtos.get(i).getDisplayName());
                inspectorOption.add(so);
            }
        }
        groupRoleFieldDto.setUserIdMap(userIdMap);
        groupRoleFieldDto.setMemberOption(inspectorOption);
        return groupRoleFieldDto;
    }

    @Override
    public List<String> getApptUserSysCorrIdByLoginId(String loginId, LoginContext loginContext) {

        return null;
    }

    private List<String> getUserIdList(List<OrgUserDto> orgUserDtoList, List<String> userIdList) {
        for(OrgUserDto oDto:orgUserDtoList){
            userIdList.add(oDto.getId());
        }
        return userIdList;
    }
}
