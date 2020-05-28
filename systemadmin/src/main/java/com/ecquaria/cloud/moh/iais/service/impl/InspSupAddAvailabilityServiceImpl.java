package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAgencyUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonAvailabilityDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserSystemCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.InspSupAddAvailabilityService;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
    public List<ApptNonAvailabilityDateDto> createNonAvailability(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto, GroupRoleFieldDto groupRoleFieldDto) {
        if(apptNonAvailabilityDateDto != null){
            //get AuditTrailDto
            AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
            //get apptRefNo
            String apptRefNo = UUID.randomUUID().toString();
            //get USER_SYS_CORRE_ID
            List<String> userSysCorrIds = apptNonAvailabilityDateDto.getUserSysCorrIds();
            //save date
            List<ApptUserCalendarDto> apptUserCalendarDtos = IaisCommonUtils.genNewArrayList();
            //get Non-Availability Date
            List<Date> timeSlots = MiscUtil.getDateInPeriodByRecurrence(apptNonAvailabilityDateDto.getBlockOutStart(),
                    apptNonAvailabilityDateDto.getBlockOutEnd(), apptNonAvailabilityDateDto.getRecurrence());

            for(String userSysCorrId : userSysCorrIds){
                //add create ApptNonAvailabilityDateDto List
                ApptNonAvailabilityDateDto apptNonAvailabilityDateDto1 = new ApptNonAvailabilityDateDto();
                apptNonAvailabilityDateDto1.setId(null);
                apptNonAvailabilityDateDto1.setBlockOutStart(apptNonAvailabilityDateDto.getBlockOutStart());
                apptNonAvailabilityDateDto1.setBlockOutEnd(apptNonAvailabilityDateDto.getBlockOutEnd());
                apptNonAvailabilityDateDto1.setRecurrence(apptNonAvailabilityDateDto.getRecurrence());
                apptNonAvailabilityDateDto1.setRefNo(apptRefNo);
                apptNonAvailabilityDateDto1.setNonAvaDescription(apptNonAvailabilityDateDto.getNonAvaDescription());
                apptNonAvailabilityDateDto1.setUserCorrId(userSysCorrId);
                apptNonAvailabilityDateDto1.setNonAvaStatus(AppConsts.COMMON_STATUS_ACTIVE);
                apptNonAvailabilityDateDto1.setAuditTrailDto(auditTrailDto);
                appointmentClient.createNonAvailability(apptNonAvailabilityDateDto1);
                //add create ApptUserCalendarDto List
                ApptUserCalendarDto apptUserCalendarDto = new ApptUserCalendarDto();
                apptUserCalendarDto.setSysUserCorrId(userSysCorrId);
                apptUserCalendarDto.setApptRefNo(apptRefNo);
                apptUserCalendarDto.setStartSlot(timeSlots);
                apptUserCalendarDto.setAuditTrailDto(auditTrailDto);
                apptUserCalendarDtos.add(apptUserCalendarDto);
            }
            //create do
            appointmentClient.createApptUserCalendarDtoList(apptUserCalendarDtos);
        }
        return null;
    }

    @Override
    public List<ApptNonAvailabilityDateDto> updateNonAvailability(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto) {
        List<ApptNonAvailabilityDateDto> apptNonAvailabilityDateDtos = IaisCommonUtils.genNewArrayList();
        if(apptNonAvailabilityDateDto != null){
            //get AuditTrailDto
            AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
            //get new apptRefNo
            String apptNewRefNo = UUID.randomUUID().toString();
            //get USER_SYS_CORRE_ID
            List<String> userSysCorrIds = apptNonAvailabilityDateDto.getUserSysCorrIds();
            //get old apptRefNo
            String apptRefNo = apptNonAvailabilityDateDto.getRefNo();
            apptNonAvailabilityDateDtos = appointmentClient.getNonAvailabilityListByApptRefNo(apptRefNo).getEntity();
            for(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto1 : apptNonAvailabilityDateDtos){
                apptNonAvailabilityDateDto1.setBlockOutStart(apptNonAvailabilityDateDto.getBlockOutStart());
                apptNonAvailabilityDateDto1.setBlockOutEnd(apptNonAvailabilityDateDto.getBlockOutEnd());
                apptNonAvailabilityDateDto1.setRecurrence(apptNonAvailabilityDateDto.getRecurrence());
                apptNonAvailabilityDateDto1.setRefNo(apptNewRefNo);
                apptNonAvailabilityDateDto1.setNonAvaDescription(apptNonAvailabilityDateDto.getNonAvaDescription());
                apptNonAvailabilityDateDto1.setNonAvaStatus(AppConsts.COMMON_STATUS_ACTIVE);
                apptNonAvailabilityDateDto1.setAuditTrailDto(auditTrailDto);
                appointmentClient.updateNonAvailability(apptNonAvailabilityDateDto1);
            }
            //cancel old calendar
            ApptUserCalendarDto cancelCalendarDto = new ApptUserCalendarDto();
            cancelCalendarDto.setApptRefNo(apptRefNo);
            cancelCalendarDto.setAuditTrailDto(auditTrailDto);
            cancelCalendarDto.setStatus(AppointmentConstants.CALENDAR_STATUS_NON_AVAILIBLE);
            appointmentClient.cancelCalenderByApptRefNoAndStatus(cancelCalendarDto);
            //save date
            List<ApptUserCalendarDto> apptUserCalendarDtos = IaisCommonUtils.genNewArrayList();
            //get Non-Availability Date
            List<Date> timeSlots = MiscUtil.getDateInPeriodByRecurrence(apptNonAvailabilityDateDto.getBlockOutStart(),
                    apptNonAvailabilityDateDto.getBlockOutEnd(), apptNonAvailabilityDateDto.getRecurrence());
            for(String userSysCorrId : userSysCorrIds){
                //add create ApptUserCalendarDto List
                ApptUserCalendarDto apptUserCalendarDto = new ApptUserCalendarDto();
                apptUserCalendarDto.setSysUserCorrId(userSysCorrId);
                apptUserCalendarDto.setApptRefNo(apptRefNo);
                apptUserCalendarDto.setStartSlot(timeSlots);
                apptUserCalendarDto.setAuditTrailDto(auditTrailDto);
                apptUserCalendarDtos.add(apptUserCalendarDto);
            }
            //create do
            appointmentClient.createApptUserCalendarDtoList(apptUserCalendarDtos);
        }
        return apptNonAvailabilityDateDtos;
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
        Map<String, String> userLoginIdMap = IaisCommonUtils.genNewHashMap();
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
                userLoginIdMap.put(i + "", orgUserDtos.get(i).getUserId());
                userIdMap.put(i + "", orgUserDtos.get(i).getId());
                SelectOption so = new SelectOption(i + "", orgUserDtos.get(i).getUserId());
                inspectorOption.add(so);
            }
        }
        groupRoleFieldDto.setUserIdMap(userIdMap);
        groupRoleFieldDto.setMemberOption(inspectorOption);
        groupRoleFieldDto.setUserLoginIdMap(userLoginIdMap);
        return groupRoleFieldDto;
    }

    @Override
    public List<String> getApptUserSysCorrIdByLoginId(String loginId, List<String> workGroupIds) {
        ApptAgencyUserDto apptAgencyUserDto = appointmentClient.getApptAgencyUserDtoLogin(loginId).getEntity();
        List<ApptUserSystemCorrelationDto> apptUserSystemCorrelationDtos;
        if(apptAgencyUserDto == null){
            ApptAppInfoShowDto apptAppInfoShowDto = new ApptAppInfoShowDto();
            List<String> workGroupNames = getWorkGroupNamesByIds(workGroupIds);
            apptAppInfoShowDto.setUserLoginId(loginId);
            apptAppInfoShowDto.setGroupNames(workGroupNames);
            apptAppInfoShowDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            apptUserSystemCorrelationDtos = appointmentClient.createApptUserInfo(apptAppInfoShowDto).getEntity();
        } else {
            String apptUserAgencyId = apptAgencyUserDto.getId();
            apptUserSystemCorrelationDtos = appointmentClient.getApptUserSystemCorrelationDtos(AppointmentConstants.APPT_SRC_SYSTEM_PK_ID, apptUserAgencyId).getEntity();
        }
        List<String> apptUserSysCorrIds = IaisCommonUtils.genNewArrayList();
        for(ApptUserSystemCorrelationDto apptUserSystemCorrelationDto : apptUserSystemCorrelationDtos){
            apptUserSysCorrIds.add(apptUserSystemCorrelationDto.getId());
        }
        return apptUserSysCorrIds;
    }

    @Override
    public ApptAgencyUserDto getAgencyUserByUserSysCorrId(String userSysCorrId) {
        ApptAgencyUserDto apptAgencyUserDto = appointmentClient.getAgencyUserByUserSysCorrId(userSysCorrId).getEntity();
        return apptAgencyUserDto;
    }

    @Override
    public List<String> getUserSysCorrIdByAgencyId(String useAgencyId) {
        List<String> userSysCorrIds = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(useAgencyId)){
            List<ApptUserSystemCorrelationDto> apptUserSystemCorrelationDtos = appointmentClient.getApptUserSystemCorrelationDtos(AppointmentConstants.APPT_SRC_SYSTEM_PK_ID, useAgencyId).getEntity();
            for(ApptUserSystemCorrelationDto apptUserSystemCorrelationDto : apptUserSystemCorrelationDtos){
                userSysCorrIds.add(apptUserSystemCorrelationDto.getId());
            }
        }
        return userSysCorrIds;
    }

    @Override
    public void deleteNonAvailabilityByApptRefNo(String apptRefNo) {
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        //cancel old calendar
        ApptUserCalendarDto cancelCalendarDto = new ApptUserCalendarDto();
        cancelCalendarDto.setApptRefNo(apptRefNo);
        cancelCalendarDto.setAuditTrailDto(auditTrailDto);
        cancelCalendarDto.setStatus(AppointmentConstants.CALENDAR_STATUS_NON_AVAILIBLE);
        appointmentClient.cancelCalenderByApptRefNoAndStatus(cancelCalendarDto);
        //cancel Non-Availability date
        ApptNonAvailabilityDateDto cancelNonAvailabilityDateDto = new ApptNonAvailabilityDateDto();
        cancelNonAvailabilityDateDto.setRefNo(apptRefNo);
        cancelNonAvailabilityDateDto.setAuditTrailDto(auditTrailDto);
        appointmentClient.cancelNonAvailabilityByApptRefNo(cancelNonAvailabilityDateDto);
    }

    @Override
    public ApptNonAvailabilityDateDto setUserSysCorrIdsByDto(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto, GroupRoleFieldDto groupRoleFieldDto) {
        String checkUserName = apptNonAvailabilityDateDto.getCheckUserName();
        Map<String, String> userIdMap = groupRoleFieldDto.getUserIdMap();
        Map<String, String> userLoginIdMap = groupRoleFieldDto.getUserLoginIdMap();
        String userPkId = userIdMap.get(checkUserName);
        String userLoginId = userLoginIdMap.get(checkUserName);
        List<UserGroupCorrelationDto> userGroupCorrelationDtos = organizationClient.getUserGroupCorreByUserId(userPkId).getEntity();
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        for(UserGroupCorrelationDto userGroupCorrelationDto : userGroupCorrelationDtos){
            workGroupIds.add(userGroupCorrelationDto.getGroupId());
        }
        List<String> userSysCorrIds = getApptUserSysCorrIdByLoginId(userLoginId, workGroupIds);
        apptNonAvailabilityDateDto.setUserSysCorrIds(userSysCorrIds);
        return apptNonAvailabilityDateDto;
    }

    private List<String> getWorkGroupNamesByIds(List<String> workGroupIds) {
        if(!IaisCommonUtils.isEmpty(workGroupIds)){
            List<String> workGroupNames = IaisCommonUtils.genNewArrayList();
            for(String workGroupId : workGroupIds){
                WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(workGroupId).getEntity();
                if(!AppConsts.DOMAIN_TEMPORARY.equals(workingGroupDto.getGroupDomain())){
                    workGroupNames.add(workingGroupDto.getGroupName());
                }
            }
            return workGroupNames;
        }
        return null;
    }

    private List<String> getUserIdList(List<OrgUserDto> orgUserDtoList, List<String> userIdList) {
        for(OrgUserDto oDto:orgUserDtoList){
            userIdList.add(oDto.getId());
        }
        return userIdList;
    }
}
