package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremInspCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschedulingOfficerDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.service.OfficersReSchedulingService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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
    private ApplicationClient applicationClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

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
            if(workGroupName.contains("Inspection")){
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
    public List<SelectOption> getInspectorByWorkGroupId(String workGroupId, ReschedulingOfficerDto reschedulingOfficerDto, String workGroupNo) {
        List<SelectOption> inspectorOption = IaisCommonUtils.genNewArrayList();
        Map<String, String> userIdMap = IaisCommonUtils.genNewHashMap();
        Map<String, Map<String, String>> groupCheckUserIdMap = reschedulingOfficerDto.getGroupCheckUserIdMap();
        if(groupCheckUserIdMap == null){
            groupCheckUserIdMap = IaisCommonUtils.genNewHashMap();
        }
        List<OrgUserDto> orgUserDtoList = organizationClient.getUsersByWorkGroupName(workGroupId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
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
    public void allInspectorFromGroupList(ReschedulingOfficerDto reschedulingOfficerDto, List<SelectOption> workGroupOption) {
        if(!IaisCommonUtils.isEmpty(workGroupOption)){
            Map<String, String> workGroupMap = reschedulingOfficerDto.getWorkGroupIdMap();
            Map<String, List<SelectOption>> inspectorByGroup = IaisCommonUtils.genNewHashMap();
            for(SelectOption selectOption : workGroupOption){
                String workGroupNo = selectOption.getValue();
                String workGroupName = selectOption.getText();
                String workGroupId = workGroupMap.get(workGroupName);
                List<SelectOption> inspectorOption = getInspectorByWorkGroupId(workGroupId, reschedulingOfficerDto, workGroupNo);
                inspectorByGroup.put(workGroupNo, inspectorOption);
            }
            reschedulingOfficerDto.setInspectorByGroup(inspectorByGroup);
        }
    }

    @Override
    public List<String> getAppNoByInspectorAndConditions(ReschedulingOfficerDto reschedulingOfficerDto) {
        List<String> appNoList = IaisCommonUtils.genNewArrayList();
        String workGroupCheck = reschedulingOfficerDto.getWorkGroupCheck();
        Map<String, Map<String, String>> groupCheckUserIdMap = reschedulingOfficerDto.getGroupCheckUserIdMap();
        Map<String, List<SelectOption>> inspectorByGroup = reschedulingOfficerDto.getInspectorByGroup();

        if(!StringUtil.isEmpty(workGroupCheck) && inspectorByGroup != null && groupCheckUserIdMap != null){
            //get group key and userId
            Map<String, String> userIdMap = groupCheckUserIdMap.get(workGroupCheck);
            List<SelectOption> inspectorOption = inspectorByGroup.get(workGroupCheck);

            if(!IaisCommonUtils.isEmpty(inspectorOption) && userIdMap != null){
                Map<String, List<String>> inspectorAppNoMap = reschedulingOfficerDto.getInspectorAppNoMap();
                if (inspectorAppNoMap == null) {
                    inspectorAppNoMap = IaisCommonUtils.genNewHashMap();
                }

                for (SelectOption selectOption : inspectorOption) {
                    String inspectorValue = selectOption.getValue();
                    String userId = userIdMap.get(inspectorValue);
                    List<AppPremInspCorrelationDto> appPremInspCorrelationDtoList = inspectionTaskClient.getAppInspCorreByUserIdStatus(userId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
                    //filter rescheduling time limit
                    List<String> appNos = filterTimeLimit(appPremInspCorrelationDtoList);
                    //filter fast tracking and the same premises
                    appNos = filterPremisesAndFast(appNos, reschedulingOfficerDto);
                    inspectorAppNoMap.put(userId, appNos);
                }
            }
        }

        return appNoList;
    }

    private List<String> filterPremisesAndFast(List<String> appNos, ReschedulingOfficerDto reschedulingOfficerDto) {
        if(!IaisCommonUtils.isEmpty(appNos)){
            List<String> repeatAppNo = IaisCommonUtils.genNewArrayList();
            for(int i = 0; i < appNos.size(); i++){
                String appNo = appNos.get(i);
                if(repeatAppNo.contains(appNo)){
                    continue;
                } else {

                }
            }
        }
        return appNos;
    }

    private List<String> filterTimeLimit(List<AppPremInspCorrelationDto> appPremInspCorrelationDtoList) {
        List<String> appNos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appPremInspCorrelationDtoList)){
            for(AppPremInspCorrelationDto appPremInspCorrelationDto : appPremInspCorrelationDtoList) {
                String appNo = appPremInspCorrelationDto.getApplicationNo();
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
}
