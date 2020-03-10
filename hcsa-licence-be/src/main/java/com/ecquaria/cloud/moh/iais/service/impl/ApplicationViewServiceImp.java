package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppSupDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
        applicationViewDto.setCurrentStatus(status);
        HcsaServiceDto hcsaServiceDto=applicationViewService.getHcsaServiceDtoById(applicationViewDto.getApplicationDto().getServiceId());
        applicationViewDto.setServiceType(hcsaServiceDto.getSvcName());


        List<String> actionByList=new ArrayList<>();
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
            applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).setAppStatus(statusUpdate);
            String workGroupId = applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).getWrkGrpId();
            if(applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).getProcessDecision()==null){
                applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).setProcessDecision(statusUpdate);
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

        return applicationViewDto;
    }


}
