package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSupDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewMainService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationMainClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayMainClient;
import com.ecquaria.cloud.moh.iais.service.client.EgpUserMainClient;
import com.ecquaria.cloud.moh.iais.service.client.EicClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskMainClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import com.ecquaria.cloud.role.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


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
    private ApplicationViewMainService applicationViewService;

    @Autowired
    private HcsaConfigMainClient hcsaConfigClient;

    @Autowired
    private OrganizationMainClient organizationClient;

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private InspectionTaskMainClient inspectionTaskMainClient;

    @Autowired
    private EgpUserMainClient egpUserMainClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

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
                        || ApplicationConsts.APPLICATION_STATUS_WITHDRAWN.equals(applicationDto.getStatus())
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

    public AppSubmissionDto getAppSubmissionByAppId(String appId) {

        return  applicationClient.getAppSubmissionByAppId(appId).getEntity();
    }

    @Override
    public ApplicationViewDto getApplicationViewDtoByCorrId(String appCorId) {
        return getApplicationViewDtoByCorrId(appCorId,null);
    }

    @Override
    public ApplicationViewDto getApplicationViewDtoByCorrId(String appCorId,String currentRoleId) {
        AppPremisesCorrelationDto appPremisesCorrelationDto = getLastAppPremisesCorrelationDtoById(appCorId);
        ApplicationViewDto applicationViewDto = searchByCorrelationIdo(appCorId);
        List<HcsaSvcDocConfigDto> docTitleList = getTitleById(applicationViewDto.getTitleIdList());
        List<OrgUserDto> userNameList = getUserNameById(applicationViewDto.getUserIdList());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        Map<String,Integer> map = IaisCommonUtils.genNewHashMap();
        Map<String,Integer> map1 = IaisCommonUtils.genNewHashMap();
        if(applicationDto!=null){
            AppSubmissionDto appSubmissionByAppId = getAppSubmissionByAppId(applicationDto.getId());
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionByAppId.getAppSvcRelatedInfoDtoList().get(0);
            List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
            AtomicInteger i=new AtomicInteger(1);
            if(appSvcDocDtoLit!=null){
                appSvcDocDtoLit.forEach((v)->{
                    String appGrpPersonId = v.getAppGrpPersonId();
                    if(appGrpPersonId!=null){
                        Integer integer = map.get(appGrpPersonId);
                        if(integer==null){
                            map.put(appGrpPersonId,i.get());
                            i.getAndIncrement();
                        }else {
                            map.put(appGrpPersonId,1);
                        }
                        map1.put(v.getSvcDocId(),map.get(appGrpPersonId));
                    }
                });
            }
        }

        List<AppSupDocDto> appSupDocDtos = applicationViewDto.getAppSupDocDtoList();
        for (int i = 0; i <appSupDocDtos.size(); i++) {
            for (int j = 0; j <docTitleList.size() ; j++) {
                if ((appSupDocDtos.get(i).getFile()).equals(docTitleList.get(j).getId())){
                    String psnIndex = StringUtil.nullToEmpty(map1.get(appSupDocDtos.get(i).getFile()));
                    if("0".equals(docTitleList.get(j).getDupForPrem())&&docTitleList.get(j).getDupForPerson()!=null){
                        switch (docTitleList.get(j).getDupForPerson()){
                            case "1" :   appSupDocDtos.get(i).setFile("Clinical Governance Officer "+ psnIndex +": "+docTitleList.get(j).getDocTitle()) ;break;
                            case "2" :   appSupDocDtos.get(i).setFile("Principal Officers "+ psnIndex +": "+docTitleList.get(j).getDocTitle());break;
                            case "4" :   appSupDocDtos.get(i).setFile("Nominee "+ psnIndex +": "+docTitleList.get(j).getDocTitle());break;
                            case "8" :   appSupDocDtos.get(i).setFile("MedAlert Person "+ psnIndex +": "+docTitleList.get(j).getDocTitle());break;
                            case "16":   appSupDocDtos.get(i).setFile("Service Personnel "+ psnIndex +": "+docTitleList.get(j).getDocTitle());break;
                            case "32":   appSupDocDtos.get(i).setFile("Clinical Director "+ psnIndex +": "+docTitleList.get(j).getDocTitle());break;
                            default:     appSupDocDtos.get(i).setFile(docTitleList.get(j).getDocTitle());
                        }
                    }else if(docTitleList.get(j).getDupForPerson()!=null && "1".equals(docTitleList.get(j).getDupForPrem())){
                        switch (docTitleList.get(j).getDupForPerson()){
                            case "1" :   appSupDocDtos.get(i).setFile("Premises 1: Clinical Governance Officer "+ psnIndex +": "+docTitleList.get(j).getDocTitle());break;
                            case "2" :   appSupDocDtos.get(i).setFile(" Premises 1: Principal Officers "+ psnIndex +": "+docTitleList.get(j).getDocTitle());break;
                            case "4" :   appSupDocDtos.get(i).setFile("Premises 1: Nominee "+ psnIndex +": "+docTitleList.get(j).getDocTitle());break;
                            case "8" :   appSupDocDtos.get(i).setFile("Premises 1: MedAlert Person "+ psnIndex +": "+docTitleList.get(j).getDocTitle());break;
                            case "16":   appSupDocDtos.get(i).setFile("Service Personnel "+ psnIndex +": "+docTitleList.get(j).getDocTitle());break;
                            case "32":   appSupDocDtos.get(i).setFile("Premises 1: Clinical Director "+ psnIndex +": "+docTitleList.get(j).getDocTitle());break;
                            default:     appSupDocDtos.get(i).setFile(docTitleList.get(j).getDocTitle());
                        }
                    }else if(docTitleList.get(j).getDupForPerson()==null && "1".equals(docTitleList.get(j).getDupForPrem())){
                        appSupDocDtos.get(i).setFile("Premises 1 :"+docTitleList.get(j).getDocTitle());
                    }else {
                        appSupDocDtos.get(i).setFile(docTitleList.get(j).getDocTitle());
                    }
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
            String statusUpdate=MasterCodeUtil.getCodeDesc(applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).getAppStatus());
            applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).setAppStatus(statusUpdate);
            String workGroupId = applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).getWrkGrpId();
            String processDecision = applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).getProcessDecision();
            if(StringUtil.isEmpty(processDecision)){
                applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).setProcessDecision(statusUpdate);
            }else{
                String codeDesc = StringUtil.isEmpty(MasterCodeUtil.getCodeDesc(processDecision)) ? processDecision : MasterCodeUtil.getCodeDesc(processDecision);
                applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).setProcessDecision(codeDesc);
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

        //set internal files
        List<AppIntranetDocDto> intranetDocDtos =  inspectionTaskMainClient.getAppIntranetDocListByPremIdAndStatus(appCorId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
        //applicationViewService
        for(AppIntranetDocDto intranetDocDto : intranetDocDtos){
            intranetDocDto.setDocSize(intranetDocDto.getDocSize()+"KB");
            OrgUserDto user = getUserById(intranetDocDto.getSubmitBy());
            intranetDocDto.setSubmitByName(user.getDisplayName());
            intranetDocDto.setSubmitDtString(Formatter.formatDateTime(intranetDocDto.getSubmitDt(), "dd/MM/yyyy HH:mm:ss"));
        }
        applicationViewDto.setAppIntranetDocDtoList(intranetDocDtos);

        //get AppPremisesRecommendationDto
        AppPremisesRecommendationDto appPremisesRecommendationDto = inspectionTaskMainClient.getAppPremRecordByIdAndType(appCorId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        if( appPremisesRecommendationDto != null) {
            applicationViewDto.setRecomLiceStartDate(appPremisesRecommendationDto.getRecomInDate());
        }
        // get Aduit dto
        if(applicationViewDto.getApplicationDto() != null && ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equalsIgnoreCase( applicationViewDto.getApplicationDto().getApplicationType())){
            AppGrpPremisesDto appGrpPremisesDto = inspectionTaskMainClient.getAppGrpPremisesDtoByAppGroId(appCorId).getEntity();
            if( appGrpPremisesDto != null){
                LicPremisesAuditDto licPremisesAuditDto = licenceClient.getLicPremisesAuditDtoByLicIdAndHCICode(applicationViewDto.getApplicationDto().getOriginLicenceId(),appGrpPremisesDto.getHciCode()).getEntity();
                if(licPremisesAuditDto != null){
                    licPremisesAuditDto.setIncludeRiskTypeOld(licPremisesAuditDto.getIncludeRiskType());
                }
                applicationViewDto.setLicPremisesAuditDto(licPremisesAuditDto);
            }
        }
        //setMaxFileSize
        applicationViewDto.setSystemMaxFileSize(systemParamConfig.getUploadFileLimit());
        applicationViewDto.setSystemFileType( FileUtils.getStringFromSystemConfigString(systemParamConfig.getUploadFileType()));
        try{
            setAppealTypeValues(applicationViewDto);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        //RollBackHistory remove currentRoleId
        log.info(StringUtil.changeForLog("The currentRoleId is -->:"+currentRoleId));
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = applicationViewDto.getRollBackHistroyList();
        List<AppPremisesRoutingHistoryDto> newAppPremisesRoutingHistoryDtos = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(currentRoleId)&&!IaisCommonUtils.isEmpty(appPremisesRoutingHistoryDtos)){
            for (AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto : appPremisesRoutingHistoryDtos){
                if(!currentRoleId.equals(appPremisesRoutingHistoryDto.getRoleId())){
                    newAppPremisesRoutingHistoryDtos.add(appPremisesRoutingHistoryDto);
                }
            }
        }
        log.info(StringUtil.changeForLog("The newAppPremisesRoutingHistoryDtos.size() is -->:"+newAppPremisesRoutingHistoryDtos.size()));
        applicationViewDto.setRollBackHistroyList(newAppPremisesRoutingHistoryDtos);
        return applicationViewDto;
    }

    private void setAppealTypeValues(ApplicationViewDto applicationViewDto){
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String applicationType = applicationDto.getApplicationType();
        if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)) {
            //get appeal type
            String appId = applicationDto.getId();
            List<AppPremiseMiscDto> premiseMiscDtoList = inspectionTaskMainClient.getAppPremiseMiscDtoListByAppId(appId).getEntity();
            if(premiseMiscDtoList != null && ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){
                AppPremiseMiscDto premiseMiscDto = premiseMiscDtoList.get(0);
                String appealNo = "";
                String reason = premiseMiscDto.getReason();
                if (ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO.equals(reason)) {
                    String serviceId = applicationViewDto.getApplicationDto().getServiceId();
                    String serviceName = HcsaServiceCacheHelper.getServiceById(serviceId).getSvcName();
                    AppSvcPrincipalOfficersDto appSvcCgoDto = inspectionTaskMainClient.getApplicationCgoByAppId(appId,ApplicationConsts.PERSONNEL_PSN_TYPE_CGO).getEntity();
                    appSvcCgoDto.setAssignSelect("newOfficer");
                    List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = IaisCommonUtils.genNewArrayList();
                    appSvcCgoDtoList.add(appSvcCgoDto);
                    SelectOption sp0 = new SelectOption("-1", "Please Select");
                    List<SelectOption> cgoSelectList = IaisCommonUtils.genNewArrayList();
                    cgoSelectList.add(sp0);
                    SelectOption sp1 = new SelectOption("newOfficer", "I'd like to add a new personnel");
                    cgoSelectList.add(sp1);
                    List<SelectOption> idTypeSelOp = getIdTypeSelOp();
                    if (serviceName != null) {
                        HcsaServiceDto serviceByServiceName = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
                        List<SelectOption> list = genSpecialtySelectList(serviceByServiceName.getSvcCode());
                        applicationViewDto.setSpecialtySelectList(list);
                    }
                    applicationViewDto.setCgoMandatoryCount(1);
                    applicationViewDto.setGovernanceOfficersList(appSvcCgoDtoList);
                    applicationViewDto.setCgoSelectList(cgoSelectList);
                    applicationViewDto.setIdTypeSelect(idTypeSelOp);
                }
                //file
                List<AppPremisesSpecialDocDto> appealSpecialDocDto = inspectionTaskMainClient.getAppPremisesSpecialDocByPremId(premiseMiscDto.getAppPremCorreId()).getEntity();
                if(appealSpecialDocDto != null){
                    Collections.sort(appealSpecialDocDto,(s1, s2)->(s1.getIndex().compareTo(s2.getIndex())));
                    applicationViewDto.setFeAppealSpecialDocDto(appealSpecialDocDto);
                }
                String oldAppId = premiseMiscDto.getRelateRecId();
                ApplicationDto oldApplication = applicationClient.getApplicationById(oldAppId).getEntity();
                if(oldApplication != null){
                    List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = inspectionTaskMainClient.getAppPremisesCorrelationsByAppId(oldApplication.getId()).getEntity();
                    if(appPremisesCorrelationDtos != null && appPremisesCorrelationDtos.size()>0){
                        AppPremisesCorrelationDto appPremisesCorrelationDto = appPremisesCorrelationDtos.get(0);
                        AppInsRepDto appInsRepDto = inspectionTaskMainClient.appGrpPremises(appPremisesCorrelationDto.getId()).getEntity();
                        if(appInsRepDto != null){
                            String hciName = appInsRepDto.getHciName();
                            List<String> hciNames = IaisCommonUtils.genNewArrayList();
                            if(!StringUtil.isEmpty(hciName)){
                                hciNames.add(hciName);
                                applicationViewDto.setHciNames(hciNames);
                            }
                        }
                    }
                    appealNo = oldApplication.getApplicationNo();
                }
                String appealType = premiseMiscDto.getAppealType();
                if(ApplicationConsts.APPEAL_TYPE_LICENCE.equals(appealType)){
                    LicenceDto licenceDto = getLicenceDto(premiseMiscDto.getRelateRecId());
                    appealNo = licenceDto.getLicenceNo();
                }
                applicationViewDto.setAppealNo(appealNo);
                applicationViewDto.setPremiseMiscDto(premiseMiscDto);
            }
        }
    }

    public LicenceDto getLicenceDto(String licenceId) {
        LicenceDto result = null;
        if(!StringUtil.isEmpty(licenceId)){
            result = licenceClient.getLicenceDtoById(licenceId).getEntity();
        }
        return result;
    }

    private List<SelectOption> genSpecialtySelectList(String svcCode){
        List<SelectOption> specialtySelectList = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(svcCode)){
            if(AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)){
                specialtySelectList = IaisCommonUtils.genNewArrayList();
                SelectOption ssl1 = new SelectOption("-1", "Please select");
                SelectOption ssl2 = new SelectOption("Pathology", "Pathology");
                SelectOption ssl3 = new SelectOption("Haematology", "Haematology");
                SelectOption ssl4 = new SelectOption("other", "Others");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl2);
                specialtySelectList.add(ssl3);
                specialtySelectList.add(ssl4);
            }else if(AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)){
                specialtySelectList = IaisCommonUtils.genNewArrayList();
                SelectOption ssl1 = new SelectOption("-1", "Please select");
                SelectOption ssl2 = new SelectOption("Diagnostic Radiology", "Diagnostic Radiology");
                SelectOption ssl3 = new SelectOption("Nuclear Medicine", "Nuclear Medicine");
                SelectOption ssl4 = new SelectOption("other", "Others");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl2);
                specialtySelectList.add(ssl3);
                specialtySelectList.add(ssl4);
            }
        }
        return specialtySelectList;
    }

    private List<SelectOption> getIdTypeSelOp(){
        List<SelectOption> idTypeSelectList = IaisCommonUtils.genNewArrayList();
        SelectOption idType0 = new SelectOption("-1", "Please Select");
        idTypeSelectList.add(idType0);
        SelectOption idType1 = new SelectOption("NRIC", "NRIC");
        idTypeSelectList.add(idType1);
        SelectOption idType2 = new SelectOption("FIN", "FIN");
        idTypeSelectList.add(idType2);
        return idTypeSelectList;
    }

    public OrgUserDto getUserById(String userId) {
        return organizationClient.retrieveOrgUserAccountById(userId).getEntity();
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
        map.put("userDomains", domain);
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
                }else if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equalsIgnoreCase(applicationDto.getStatus()) || ApplicationConsts.APPLICATION_STATUS_WITHDRAWN.equalsIgnoreCase(applicationDto.getStatus())){
                    rejectNum.add(applicationDto);
                    if(applicationDto.getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)){
                        List<AppEditSelectDto> appEditSelectDtos = applicationClient.getAppEditSelectDto(applicationDto.getId(), ApplicationConsts.APPLICATION_EDIT_TYPE_RFC).getEntity();
                        boolean changePrem=false;
                        for (AppEditSelectDto edit:appEditSelectDtos
                        ) {
                            if(edit.isPremisesEdit()||edit.isPremisesListEdit()){
                                changePrem=true;
                            }
                        }
                        if(changePrem){
                            List<ApplicationDto> apps=IaisCommonUtils.genNewArrayList();
                            apps.add(applicationDto);
                            applicationClient.clearHclcodeByAppIds(apps);
                        }
                    }
                }
            }
            if(appovedNum.size() > 0 && rejectNum.size() >0){
                //clear approve hclcode
                appovedNum.get(0).setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                // set main appoved true
                for(ApplicationDto applicationDto : appovedNum){
                    if("0".equalsIgnoreCase(String.valueOf(applicationDto.getSecondaryFloorNoChange()))){
                        applicationDto.setNeedNewLicNo(true);
                        if(applicationDtoMain.getId().equalsIgnoreCase(applicationDto.getId())){
                            applicationDtoMain.setNeedNewLicNo(true);
                        }
                    }
                }
            }
        }
        log.info("-----------clearApprovedHclCodeByExistRejectApp end------");
    }
}
