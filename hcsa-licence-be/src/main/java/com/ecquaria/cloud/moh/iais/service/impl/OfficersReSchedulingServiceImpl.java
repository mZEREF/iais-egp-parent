package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremInspCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschedulingOfficerDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschedulingOfficerQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.service.OfficersReSchedulingService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
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
    private AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

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
    public List<String> allInspectorFromGroupList(ReschedulingOfficerDto reschedulingOfficerDto, List<SelectOption> workGroupOption) {
        List<String> workGroupNos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(workGroupOption)){
            Map<String, String> workGroupMap = reschedulingOfficerDto.getWorkGroupIdMap();
            Map<String, List<SelectOption>> inspectorByGroup = IaisCommonUtils.genNewHashMap();
            for(SelectOption selectOption : workGroupOption){
                String workGroupNo = selectOption.getValue();
                String workGroupName = selectOption.getText();
                String workGroupId = workGroupMap.get(workGroupName);
                List<SelectOption> inspectorOption = getInspectorByWorkGroupId(workGroupId, reschedulingOfficerDto, workGroupNo);
                inspectorByGroup.put(workGroupNo, inspectorOption);
                workGroupNos.add(workGroupNo);
            }
            reschedulingOfficerDto.setInspectorByGroup(inspectorByGroup);
        }
        return workGroupNos;
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
                    if(!IaisCommonUtils.isEmpty(appNos)) {
                        appNoList.addAll(appNos);
                    }
                }
            }
        }

        return appNoList;
    }

    @Override
    public SearchResult<ReschedulingOfficerQueryDto> getOfficersSearch(SearchParam searchParam) {
        return inspectionTaskClient.officerReSchSearch(searchParam).getEntity();
    }

    @Override
    public SearchResult<ReschedulingOfficerQueryDto> setInspectorsAndServices(SearchResult<ReschedulingOfficerQueryDto> searchResult, ReschedulingOfficerDto reschedulingOfficerDto) {
        if(searchResult != null && !IaisCommonUtils.isEmpty(searchResult.getRows())){
            for(ReschedulingOfficerQueryDto reschedulingOfficerQueryDto : searchResult.getRows()){
                String appNo = reschedulingOfficerQueryDto.getAppNo();
                Map<String, List<String>> samePremisesAppMap = reschedulingOfficerDto.getSamePremisesAppMap();
                List<String> applicationNos = samePremisesAppMap.get(appNo);
                List<String> inspectorNames = getInspectorsByAppNoList(applicationNos);
                reschedulingOfficerQueryDto.setInspectors(inspectorNames);
                List<String> serviceNames = getServiceNameByAppNoList(applicationNos);
                reschedulingOfficerQueryDto.setServiceNames(serviceNames);
            }
        }
        return searchResult;
    }

    private List<String> getServiceNameByAppNoList(List<String> applicationNos) {
        List<String> serviceNames = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty (applicationNos)){
            for(String appNo : applicationNos){
                ApplicationDto applicationDto = applicationClient.getAppByNo(appNo).getEntity();
                String serviceId = applicationDto.getServiceId();
                HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
                String serviceName = hcsaServiceDto.getSvcName();
                serviceNames.add(serviceName);
            }
        }
        return serviceNames;
    }

    private List<String> getInspectorsByAppNoList(List<String> applicationNos) {
        List<String> inspectorNames = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty (applicationNos)){
            for(String appNo : applicationNos){
                List<AppPremInspCorrelationDto> appPremInspCorrelationDtoList = inspectionTaskClient.getAppInspCorreByAppNoStatus(appNo, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
                if(!IaisCommonUtils.isEmpty(appPremInspCorrelationDtoList)){
                    for(AppPremInspCorrelationDto appPremInspCorrelationDto : appPremInspCorrelationDtoList){
                        String userId = appPremInspCorrelationDto.getUserId();
                        OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
                        inspectorNames.add(orgUserDto.getDisplayName());
                    }
                }
            }
        }
        return applicationNos;
    }

    private List<String> filterPremisesAndFast(List<String> appNos, ReschedulingOfficerDto reschedulingOfficerDto) {
        List<String> repeatAppNo = IaisCommonUtils.genNewArrayList();
        List<String> applicationNos = IaisCommonUtils.genNewArrayList();
        Map<String, List<String>> samePremisesAppMap = reschedulingOfficerDto.getSamePremisesAppMap();
        if(samePremisesAppMap == null){
            samePremisesAppMap = IaisCommonUtils.genNewHashMap();
        }
        if(!IaisCommonUtils.isEmpty(appNos)){
            //duplicate removal
            for(int i = 0; i < appNos.size(); i++){
                String appNo = appNos.get(i);
                if(repeatAppNo.contains(appNo)){
                    continue;
                } else {
                    ApplicationDto applicationDto = applicationClient.getAppByNo(appNo).getEntity();
                    boolean fastTracking = applicationDto.isFastTracking();
                    if(fastTracking){
                        repeatAppNo.add(appNo);
                        //put same Premises Application No or Fast tracking Map
                        List<String> appNoList = IaisCommonUtils.genNewArrayList();
                        appNoList.add(appNo);
                        samePremisesAppMap.put(appNo, appNoList);
                        applicationNos.add(appNo);
                    } else {
                        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(applicationDto.getId()).getEntity();
                        //get all same premises by Group
                        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = appPremisesCorrClient.getAppPremisesCorrelationsByPremises(appPremisesCorrelationDto.getId()).getEntity();
                        //put same Premises Application No or Fast tracking Map
                        List<String> appNoList = IaisCommonUtils.genNewArrayList();
                        appNoList = filterCancelAppByCorr(appNoList, appPremisesCorrelationDtos);
                        if(!IaisCommonUtils.isEmpty(appNoList)) {
                            samePremisesAppMap.put(appNo, appNoList);
                            repeatAppNo.addAll(appNoList);
                        }
                        applicationNos.add(appNo);
                    }
                }
            }
        }
        return applicationNos;
    }

    private List<String> filterCancelAppByCorr(List<String> appNoList, List<AppPremisesCorrelationDto> appPremisesCorrelationDtos) {
        if(!IaisCommonUtils.isEmpty(appPremisesCorrelationDtos)){
            for(int i = 0; i < appPremisesCorrelationDtos.size(); i++){
                String applicationId = appPremisesCorrelationDtos.get(i).getApplicationId();
                ApplicationDto applicationDto = applicationClient.getApplicationById(applicationId).getEntity();
                if(ApplicationConsts.APPLICATION_STATUS_CREATE_AUDIT_TASK_CANCELED.equals(applicationDto.getStatus())){
                    appPremisesCorrelationDtos.remove(i);
                    i--;
                } else {
                    appNoList.add(applicationDto.getApplicationNo());
                }
            }
        }
        return appNoList;
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
