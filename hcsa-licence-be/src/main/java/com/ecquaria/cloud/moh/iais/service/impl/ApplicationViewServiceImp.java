package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSupDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.client.*;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
@Slf4j
public class ApplicationViewServiceImp implements ApplicationViewService {
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    ApplicationViewService applicationViewService;
    @Autowired
    private FillUpCheckListGetAppClient uploadFileClient;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private InspectionTaskClient inspectionTaskClient;
    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;
    @Override
    public ApplicationViewDto searchByCorrelationIdo(String correlationId) {
        //return applicationClient.getAppViewByNo(appNo).getEntity();
        return applicationClient.getAppViewByCorrelationId(correlationId).getEntity();
    }

    @Override
    public ApplicationDto getApplicaitonByAppNo(String appNo) {
        return applicationClient.getAppByNo(appNo).getEntity();
    }

    @Override
    public ApplicationDto updateApplicaiton(ApplicationDto applicationDto) {

        return  applicationClient.updateApplication(applicationDto).getEntity();
    }

    @Override
    public List<OrgUserDto> getUserNameById(List<String> userIdList) {

        return  organizationClient.retrieveOrgUserAccount(userIdList).getEntity();
    }

    @Override
    public List<HcsaSvcDocConfigDto> getTitleById(List<String> titleIdList) {

        return  hcsaConfigClient.listSvcDocConfig(titleIdList).getEntity();
    }

    @Override
    public List<HcsaSvcRoutingStageDto> getStage(String serviceId, String stageId) {
     
        return   hcsaConfigClient.getStageName(serviceId,stageId).getEntity();


    }

    @Override
    public OrgUserDto getUserById(String userId) {
        return organizationClient.retrieveOrgUserAccountById(userId).getEntity();
    }

    @Override
    public String getWrkGrpName(String id) {
        WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(id).getEntity();
        return workingGroupDto.getGroupName();
    }

    @Override
    public HcsaServiceDto getHcsaServiceDtoById(String id) {
        return hcsaConfigClient.getHcsaServiceDtoByServiceId(id).getEntity();
    }

    @Override
    public List<HcsaSvcSubtypeOrSubsumedDto> getHcsaSvcSubtypeOrSubsumedByServiceId(String serviceId) {
        return hcsaConfigClient.listSubCorrelation(serviceId).getEntity();
    }

    @Override
    public AppPremisesCorrelationDto getLastAppPremisesCorrelationDtoById(String id) {
        return applicationClient.getLastAppPremisesCorrelationDtoByCorreId(id).getEntity();
    }

    @Override
    public HcsaSvcRoutingStageDto getStageById(String id) {
        return hcsaConfigClient.getHcsaSvcRoutingStageById(id).getEntity();
    }

    @Override
    public ApplicationViewDto getApplicationViewDtoByCorrId(String appCorId) {
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationViewService.getLastAppPremisesCorrelationDtoById(appCorId);
        ApplicationViewDto applicationViewDto = applicationViewService.searchByCorrelationIdo(appCorId);
        List<HcsaSvcDocConfigDto> docTitleList=applicationViewService.getTitleById(applicationViewDto.getTitleIdList());
        List<OrgUserDto> userNameList=applicationViewService.getUserNameById(applicationViewDto.getUserIdList());
        List<AppSupDocDto> appSupDocDtos = applicationViewDto.getAppSupDocDtoList();
        for (int i = 0; i <appSupDocDtos.size(); i++) {
            for (int j = 0; j <docTitleList.size() ; j++) {
                if ((appSupDocDtos.get(i).getFile()).equals(docTitleList.get(j).getId())){
                    appSupDocDtos.get(i).setFile(docTitleList.get(j).getDocTitle());
                }
            }
            for (int j = 0; j <userNameList.size() ; j++) {
                if ((appSupDocDtos.get(i).getSubmittedBy()).equals(userNameList.get(j).getId())){
                    appSupDocDtos.get(i).setSubmittedBy(userNameList.get(j).getDisplayName());
                }
            }
        }
        String applicationType= MasterCodeUtil.getCodeDesc(applicationViewDto.getApplicationType());
        applicationViewDto.setApplicationType(applicationType);
        String serviceType = MasterCodeUtil.getCodeDesc(applicationViewDto.getApplicationDto().getServiceId());
        applicationViewDto.setServiceType(serviceType);


        String status = MasterCodeUtil.getCodeDesc(applicationViewDto.getApplicationDto().getStatus());

        //set status role back display name
        if(ApplicationConsts.APPLICATION_STATUS_ROUTE_BACK.equals(applicationViewDto.getApplicationDto().getStatus())){
            String appStatusTemp = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02;
            AppPremisesRoutingHistoryDto historyDto = null;
            try {
                historyDto = appPremisesRoutingHistoryService.getSecondRouteBackHistoryByAppNo(
                        applicationViewDto.getApplicationDto().getApplicationNo(), appStatusTemp);
                String actionby = historyDto.getActionby();
                if(!StringUtil.isEmpty(actionby)){
                    List<OrgUserRoleDto> orgUserRoleDtoList = organizationClient.getUserRoleByUserAccId(actionby).getEntity();
                    if(!IaisCommonUtils.isEmpty(orgUserRoleDtoList)){
                        String roleName = orgUserRoleDtoList.get(0).getRoleName();
                        //AO route back
                        if(RoleConsts.USER_ROLE_AO1.equals(roleName) || RoleConsts.USER_ROLE_AO2.equals(roleName) || RoleConsts.USER_ROLE_AO3.equals(roleName)){
                            status = "Pending Internal Clarification";
                        }else if(RoleConsts.USER_ROLE_ASO.equals(roleName)){
                            status = "Professional Screening Officer Enquire";
                        }else if(RoleConsts.USER_ROLE_INSPECTIOR.equals(roleName)){
                            status = "Inspector Enquire";
                        }
                    }
                }
            }catch (Exception e){
                log.error(StringUtil.changeForLog("first do main flow ,have no two history"));
            }
        }

        applicationViewDto.setCurrentStatus(status);
//        if(!StringUtil.isEmpty(applicationViewDto.getSubmissionDate()))
//        applicationViewDto.setSubmissionDate(IaisEGPHelper.parseToString(IaisEGPHelper.parseToDate( applicationViewDto.getSubmissionDate(),"yyyy-MM-dd hh:mm"),"yyyy-MM-dd"));
        HcsaServiceDto hcsaServiceDto=applicationViewService.getHcsaServiceDtoById(applicationViewDto.getApplicationDto().getServiceId());
        applicationViewDto.setServiceType(hcsaServiceDto.getSvcName());


        List<String> actionByList= IaisCommonUtils.genNewArrayList();
        for (AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto:applicationViewDto.getAppPremisesRoutingHistoryDtoList()
        ) {
            actionByList.add(appPremisesRoutingHistoryDto.getActionby());

        }
        List<OrgUserDto> actionByRealNameList=applicationViewService.getUserNameById(actionByList);
        for (int i = 0; i <applicationViewDto.getAppPremisesRoutingHistoryDtoList().size(); i++) {
            String username="-";
            for (int j = 0; j <actionByRealNameList.size() ; j++) {
                if ((applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).getActionby()).equals(actionByRealNameList.get(j).getId())){
                    username=actionByRealNameList.get(j).getDisplayName();
                    break;
                }
            }
            applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).setActionby(username);
            String statusUpdate=MasterCodeUtil.getCodeDesc(applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).getAppStatus());
            String RollBackStatusDecision = MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_ROUTE_BACK);
            applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).setAppStatus(statusUpdate);
            String workGroupId = applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).getWrkGrpId();
            String processDecision = applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).getProcessDecision();
            if(processDecision==null){
                if(RollBackStatusDecision.equals(statusUpdate)){
                    statusUpdate = status;
                }
                applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).setProcessDecision(statusUpdate);
            }else{
                String codeDesc = StringUtil.isEmpty(MasterCodeUtil.getCodeDesc(processDecision)) ? processDecision : MasterCodeUtil.getCodeDesc(processDecision);
                applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).setProcessDecision(codeDesc);
            }


            if (!StringUtil.isEmpty(workGroupId)){
                log.info("Wrk Group Id ======>" + workGroupId);
                String workingGroupName=applicationViewService.getWrkGrpName(workGroupId);
                if (!StringUtil.isEmpty(workingGroupName)){
                    applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).setWorkingGroup(workingGroupName);
                }else{
                    applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).setWorkingGroup("-");
                }
            }
        }
        applicationViewDto.setNewAppPremisesCorrelationDto(appPremisesCorrelationDto);

        //set internal files
        List<AppIntranetDocDto> intranetDocDtos = uploadFileClient.getAppIntranetDocListByPremIdAndStatus(appCorId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
        //applicationViewService
        for(AppIntranetDocDto intranetDocDto : intranetDocDtos){
            intranetDocDto.setDocSize(intranetDocDto.getDocSize()+"KB");
            OrgUserDto user = applicationViewService.getUserById(intranetDocDto.getSubmitBy());
            intranetDocDto.setSubmitByName(user.getDisplayName());
            intranetDocDto.setSubmitDtString(Formatter.formatDateTime(intranetDocDto.getSubmitDt(), "dd/MM/yyyy HH:mm:ss"));
        }
        applicationViewDto.setAppIntranetDocDtoList(intranetDocDtos);

        //get AppPremisesRecommendationDto
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appCorId,InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        if( appPremisesRecommendationDto != null)
            applicationViewDto.setRecomLiceStartDate(appPremisesRecommendationDto.getRecomInDate());

        // get Aduit dto
        if(applicationViewDto.getApplicationDto() != null && ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equalsIgnoreCase( applicationViewDto.getApplicationDto().getApplicationType())){
            AppGrpPremisesDto appGrpPremisesDto =   inspectionTaskClient.getAppGrpPremisesDtoByAppGroId(appCorId).getEntity();
            if( appGrpPremisesDto != null){
                LicPremisesAuditDto licPremisesAuditDto = hcsaLicenceClient.getLicPremisesAuditDtoByLicIdAndHCICode(applicationViewDto.getApplicationDto().getOriginLicenceId(),appGrpPremisesDto.getHciCode()).getEntity();
                applicationViewDto.setLicPremisesAuditDto(licPremisesAuditDto);
            }
        }
        return applicationViewDto;
    }


}
