package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppSupDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewMainService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationMainClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayMainClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class ApplicationViewMainServiceImp implements ApplicationViewMainService {
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Autowired
    private BeEicGatewayMainClient beEicGatewayClient;
    @Autowired
    private ApplicationMainClient applicationClient;
    @Autowired
    ApplicationViewMainService applicationViewService;
    @Autowired
    HcsaConfigMainClient hcsaConfigClient;
    @Autowired
    OrganizationMainClient organizationClient;
    @Override
    public List<ApplicationDto> getApplicaitonsByAppGroupId(String appGroupId) {
        return applicationClient.getGroupAppsByNo(appGroupId).getEntity();
    }

    @Override
    public boolean isOtherApplicaitonSubmit(List<ApplicationDto> applicationDtoList,List<String> appNos,String status) {
        if(IaisCommonUtils.isEmpty(applicationDtoList) || appNos.isEmpty() || StringUtil.isEmpty(status)){
            return  false;
        }
        boolean result = true;
        Map<String,List<ApplicationDto>> applicationMap = tidyApplicationDto(applicationDtoList);
        if(applicationMap!=null && applicationMap.size()>0){
            for (Map.Entry<String,List<ApplicationDto>> entry : applicationMap.entrySet()){
                String key = entry.getKey();
                List<ApplicationDto> value = entry.getValue();
                boolean isExistFlag = false;
                for (String appNo:appNos
                     ) {
                    if(appNo.equals(key)){
                        isExistFlag = true;
                    }
                }
                if(isExistFlag){
                    continue;
                }else if(!containStatus(value,status)){
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
    private boolean containStatus(List<ApplicationDto> applicationDtos,String status){
        boolean result = false;
        if(!IaisCommonUtils.isEmpty(applicationDtos) && !StringUtil.isEmpty(status)){
            for(ApplicationDto applicationDto : applicationDtos){
                if(status.equals(applicationDto.getStatus())){
                    result = true;
                    break;
                }
            }
        }
        return  result;
    }

    private Map<String,List<ApplicationDto>> tidyApplicationDto(List<ApplicationDto> applicationDtoList){
        Map<String,List<ApplicationDto>> result = null;
        if(!IaisCommonUtils.isEmpty(applicationDtoList)){
            result = IaisCommonUtils.genNewHashMap();
            for(ApplicationDto applicationDto : applicationDtoList){
                String appNo = applicationDto.getApplicationNo();
                List<ApplicationDto> applicationDtos = result.get(appNo);
                if(applicationDtos ==null){
                    applicationDtos = IaisCommonUtils.genNewArrayList();
                }
                applicationDtos.add(applicationDto);
                result.put(appNo,applicationDtos);
            }
        }
        return result;
    }

    @Override
    public ApplicationViewDto searchByCorrelationIdo(String correlationId) {
        //return applicationClient.getAppViewByNo(appNo).getEntity();
        return applicationClient.getAppViewByCorrelationId(correlationId).getEntity();
    }

    @Override
    public AppPremisesCorrelationDto getLastAppPremisesCorrelationDtoById(String id) {
        return applicationClient.getLastAppPremisesCorrelationDtoByCorreId(id).getEntity();
    }

    @Override
    public ApplicationGroupDto getApplicationGroupDtoById(String appGroupId) {
        return applicationClient.getAppById(appGroupId).getEntity();
    }

    @Override
    public ApplicationDto updateFEApplicaiton(ApplicationDto applicationDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return beEicGatewayClient.updateApplication(applicationDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
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
            String statusUpdate= MasterCodeUtil.getCodeDesc(applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).getAppStatus());
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
        applicationViewDto.setNewAppPremisesCorrelationDto(appPremisesCorrelationDto);
        return applicationViewDto;
    }

    @Override
    public HcsaServiceDto getHcsaServiceDtoById(String id) {
        return hcsaConfigClient.getHcsaServiceDtoByServiceId(id).getEntity();
    }

    @Override
    public List<HcsaSvcDocConfigDto> getTitleById(List<String> titleIdList) {

        return  hcsaConfigClient.listSvcDocConfig(titleIdList).getEntity();
    }

    @Override
    public List<OrgUserDto> getUserNameById(List<String> userIdList) {

        return  organizationClient.retrieveOrgUserAccount(userIdList).getEntity();
    }

    @Override
    public String getWrkGrpName(String id) {
        WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(id).getEntity();
        return workingGroupDto.getGroupName();
    }
}
