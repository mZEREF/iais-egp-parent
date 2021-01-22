package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSupDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewMainService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationMainClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayMainClient;
import com.ecquaria.cloud.moh.iais.service.client.EgpUserMainClient;
import com.ecquaria.cloud.moh.iais.service.client.EicClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import com.ecquaria.cloud.role.Role;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


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
    private EicClient eicClient;
    @Value("${spring.application.name}")
    private String currentApp;
    @Value("${iais.current.domain}")
    private String currentDomain;
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

    @Autowired
    EgpUserMainClient egpUserMainClient;
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
        log.debug(StringUtil.changeForLog(" isOtherApplicaitonSubmit start ....."));
        Map<String,List<ApplicationDto>> applicationMap = tidyApplicationDto(applicationDtoList);
        if(applicationMap!=null && applicationMap.size()>0){
            log.debug(StringUtil.changeForLog(" applicationMap.size() is" + applicationMap.size()));
            for (Map.Entry<String,List<ApplicationDto>> entry : applicationMap.entrySet()){
                log.debug(StringUtil.changeForLog(" entry.getKey() is" + entry.getKey()));
                String key = entry.getKey();
                List<ApplicationDto> value = entry.getValue();
                boolean isExistFlag = false;
                for (String appNo:appNos
                     ) {
                    log.debug(StringUtil.changeForLog(" appNo is" + appNo));
                    log.debug(StringUtil.changeForLog(" key is" + key));
                    log.debug(StringUtil.changeForLog(" isExistFlag is" + isExistFlag));
                    if(appNo.equals(key)){
                        isExistFlag = true;
                    }
                    log.debug(StringUtil.changeForLog(" isExistFlag is" + isExistFlag));
                }
                if(isExistFlag){
                    log.debug(StringUtil.changeForLog(" countine ..."));
                    continue;
                }else if(!containStatus(value,status)){
                    log.debug(StringUtil.changeForLog("else if containStatus result is false"));
                    result = false;
                    break;
                }
            }
        }
        log.debug(StringUtil.changeForLog("result is " + result));
        log.debug(StringUtil.changeForLog(" isOtherApplicaitonSubmit end ....."));
        return result;
    }

    @Override
    public boolean isOtherApplicaitonSubmit(List<ApplicationDto> applicationDtoList,List<String> appNos,String status1, String status2) {
        if(IaisCommonUtils.isEmpty(applicationDtoList) || appNos.isEmpty() || StringUtil.isEmpty(status1) || StringUtil.isEmpty(status2)){
            return  false;
        }
        boolean result = true;
        log.debug(StringUtil.changeForLog(" isOtherApplicaitonSubmit start ....."));
        Map<String,List<ApplicationDto>> applicationMap = tidyApplicationDto(applicationDtoList);
        if(applicationMap!=null && applicationMap.size()>0){
            log.debug(StringUtil.changeForLog(" applicationMap.size() is" + applicationMap.size()));
            for (Map.Entry<String,List<ApplicationDto>> entry : applicationMap.entrySet()){
                log.debug(StringUtil.changeForLog(" entry.getKey() is" + entry.getKey()));
                String key = entry.getKey();
                List<ApplicationDto> value = entry.getValue();
                boolean isExistFlag = false;
                for (String appNo:appNos
                ) {
                    log.debug(StringUtil.changeForLog(" appNo is" + appNo));
                    log.debug(StringUtil.changeForLog(" key is" + key));
                    log.debug(StringUtil.changeForLog(" isExistFlag is" + isExistFlag));
                    if(appNo.equals(key)){
                        isExistFlag = true;
                    }
                    log.debug(StringUtil.changeForLog(" isExistFlag is" + isExistFlag));
                }
                if(isExistFlag){
                    log.debug(StringUtil.changeForLog(" countine ..."));
                    continue;
                }else if(!(containStatus(value,status1) || containStatus(value,status2))){
                    log.debug(StringUtil.changeForLog("else if containStatus result is false"));
                    result = false;
                    break;
                }
            }
        }
        log.debug(StringUtil.changeForLog("result is " + result));
        log.debug(StringUtil.changeForLog(" isOtherApplicaitonSubmit end ....."));
        return result;
    }

    private boolean containStatus(List<ApplicationDto> applicationDtos,String status){
        boolean result = false;
        if(!IaisCommonUtils.isEmpty(applicationDtos) && !StringUtil.isEmpty(status)){
            for(ApplicationDto applicationDto : applicationDtos){
                if(status.equals(applicationDto.getStatus())
                        || ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED.equals(applicationDto.getStatus())
                        || ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(applicationDto.getStatus())
                        || ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(applicationDto.getStatus())
                ){
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
    public ApplicationDto updateApplicaiton(ApplicationDto applicationDto) {

        return  applicationClient.updateApplication(applicationDto).getEntity();
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
    public List<ApplicationDto> updateFEApplicaitons(List<ApplicationDto> applicationDtos){
        for(ApplicationDto applicationdto : applicationDtos){
            updateFEApplicaiton(applicationdto);
        }
        return applicationDtos;
    }

    @Override
    public List<PaymentRequestDto> eicFeStripeRefund(List<AppReturnFeeDto> appReturnFeeDtos) {
        log.info(StringUtil.changeForLog("The updateFEPaymentRefund start ..."));
        String moduleName = currentApp + "-" + currentDomain;
        EicRequestTrackingDto dto = new EicRequestTrackingDto();
        dto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        dto.setActionClsName(this.getClass().getName());
        dto.setActionMethod("callEicInterPaymentRefund");
        dto.setDtoClsName(appReturnFeeDtos.getClass().getName());
        dto.setDtoObject(JsonUtil.parseToJson(appReturnFeeDtos));
        String refNo = String.valueOf(System.currentTimeMillis());
        log.info(StringUtil.changeForLog("The updateFEPaymentRefund refNo is  -- >:"+refNo));
        dto.setRefNo(refNo);
        dto.setModuleName(moduleName);
        eicClient.saveEicTrack(dto);
        List<PaymentRequestDto> paymentRequestDtos=callEicInterPaymentRefund(appReturnFeeDtos);
        dto = eicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
        Date now = new Date();
        dto.setProcessNum(1);
        dto.setFirstActionAt(now);
        dto.setLastActionAt(now);
        dto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        List<EicRequestTrackingDto> list = IaisCommonUtils.genNewArrayList(1);
        list.add(dto);
        eicClient.updateStatus(list);
        log.info(StringUtil.changeForLog("The updateFEPaymentRefund end ..."));
        return paymentRequestDtos;
    }

    private List<PaymentRequestDto> callEicInterPaymentRefund(List<AppReturnFeeDto> appReturnFeeDtos) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return beEicGatewayClient.doStripeRefunds(appReturnFeeDtos, signature.date(), signature.authorization(),
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
                log.info(StringUtil.changeForLog("Wrk Group Id ======>" + workGroupId));
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

    @Override
    public AppReturnFeeDto saveAppReturnFee(AppReturnFeeDto appReturnFeeDto) {
        return applicationClient.saveAppReturnFee(appReturnFeeDto).getEntity();
    }

    @Override
    public List<HcsaSvcRoutingStageDto> getStage(String serviceId, String stageId, String type) {

        return   hcsaConfigClient.getStageName(serviceId,stageId,type).getEntity();


    }

    @Override
    public List<SelectOption> getCanViewAuditRoles(List<String> roleIds){
        List<SelectOption> selectOptionArrayList = IaisCommonUtils.genNewArrayList();
        List<Role> roles = getRolesByDomain(AppConsts.HALP_EGP_DOMAIN);
        for (String item:roleIds
             ) {
            add(roleIds, item,selectOptionArrayList,roles);
        }
        return selectOptionArrayList;
    }

    public List<Role> getRolesByDomain(String domain) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        map.put("userDomain", domain);
        return egpUserMainClient.search(map).getEntity();
    }

    private void add(List<String> roleIds,String roleId, List<SelectOption> selectOptionArrayList,List<Role> roles){
        for (String item : roleIds){
            if(roleId.equalsIgnoreCase(item)){
                selectOptionArrayList.add(getRoleSelectOption(roles,roleId));
                break;
            }
        }
    }

    private  SelectOption getRoleSelectOption(List<Role> roles,String roleId){
        if(IaisCommonUtils.isEmpty(roles) || StringUtil.isEmpty(roleId)){
            return null;
        }
        for(Role role : roles){
            if(roleId.equalsIgnoreCase(role.getId())){
                return new SelectOption(role.getId(),role.getName());
            }
        }
        return  new SelectOption(roleId,roleId);
    }

    @Override
    public void clearApprovedHclCodeByExistRejectApp(List<ApplicationDto> saveApplicationDtoList, String appGroupType,ApplicationDto applicationDtoMain) {
        log.info("-----------clearApprovedHclCodeByExistRejectApp start------");
        if(saveApplicationDtoList.size() > 1 && ( ApplicationConsts.APPLICATION_TYPE_RENEWAL.equalsIgnoreCase(appGroupType) || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equalsIgnoreCase(appGroupType))){
            int size = saveApplicationDtoList.size();
            List<ApplicationDto> appovedNum = new ArrayList<>(size);
            List<ApplicationDto> rejectNum = new ArrayList<>(size);
            for(ApplicationDto applicationDto : saveApplicationDtoList){
                if(ApplicationConsts.APPLICATION_STATUS_APPROVED .equalsIgnoreCase(applicationDto.getStatus())){
                    appovedNum.add(applicationDto);
                }else if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equalsIgnoreCase(applicationDto.getStatus())){
                    rejectNum.add(applicationDto);
                }
            }
            if(appovedNum.size() > 0 && rejectNum.size() >0){
                //clear approve hclcode
                appovedNum.get(0).setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                // set main appoved true
                for(ApplicationDto applicationDto : appovedNum){
                    if("0".equalsIgnoreCase(String.valueOf(applicationDto.getSecondaryFloorNoChange()))) {
                        applicationDto.setNeedNewLicNo(true);
                        if(applicationDtoMain.getId().equalsIgnoreCase(applicationDto.getId())) {
                            applicationDtoMain.setNeedNewLicNo(true);
                        }
                    }
                }
                applicationClient.clearHclcodeByAppIds(appovedNum);
            }
        }
        log.info("-----------clearApprovedHclCodeByExistRejectApp end------");
    }
}
