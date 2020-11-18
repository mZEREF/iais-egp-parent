package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RecommendInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.EicClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeMessageClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sop.util.CopyUtil;
import sop.webflow.rt.api.Process;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * AppSubmisionServiceImpl
 *
 * @author suocheng
 * @date 11/6/2019
 */
@Service
@Slf4j
public class AppSubmissionServiceImpl implements AppSubmissionService {
   // String draftUrl =  RestApiUrlConsts.HCSA_APP + RestApiUrlConsts.HCSA_APP_SUBMISSION_DRAFT;
    //String submission = RestApiUrlConsts.HCSA_APP + RestApiUrlConsts.HCSA_APP_SUBMISSION;

    @Autowired
    private ApplicationFeClient applicationFeClient;
    @Autowired
    private AppConfigClient appConfigClient;
    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private EventBusHelper eventBusHelper;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private FeEicGatewayClient feEicGatewayClient;
    @Autowired
    private FeMessageClient feMessageClient;
    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private ServiceConfigServiceImpl serviceConfigService;

    @Override
    public AppSubmissionDto submit(AppSubmissionDto appSubmissionDto, Process process) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appSubmissionDto= applicationFeClient.saveSubmision(appSubmissionDto).getEntity();
        //asynchronous save the other data.
        eventBus(appSubmissionDto, process);
        return appSubmissionDto;
    }
    @Override
    public void sendEmailAndSMSAndMessage(AppSubmissionDto appSubmissionDto,String applicantName){
        try{
            ApplicationDto applicationDto =  appSubmissionDto.getApplicationDtos().get(0);
            String applicationType =  MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MOH HALP - Your ").append(applicationType).append(',');
            int index = 0;
            StringBuilder stringBuilderAPPNum = new StringBuilder();
            for(ApplicationDto applicationDtoApp : appSubmissionDto.getApplicationDtos()){
                if(index == 0){
                    stringBuilderAPPNum.append(applicationDtoApp.getApplicationNo());
                }else {
                    stringBuilderAPPNum.append(" and ");
                    stringBuilderAPPNum.append(applicationDtoApp.getApplicationNo());
                }
                index++;
            }
            String applicationNumber = stringBuilderAPPNum.toString();
            stringBuilder.append(applicationNumber);
            stringBuilder.append(" has been submitted");
            String subject = stringBuilder.toString();
            Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
            templateContent.put("ApplicantName", applicantName);
            templateContent.put("ApplicationType",  applicationType);
            templateContent.put("ApplicationNumber", applicationNumber);
            templateContent.put("ApplicationDate", Formatter.formatDateTime(new Date()));
            templateContent.put("isSelfAssessment","No");
            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_INBOX;
            templateContent.put("systemLink", loginUrl);
            String paymentMethodName = "onlinePayment";
            if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(appSubmissionDto.getPaymentMethod())) {
                paymentMethodName = "GIRO";
                //need change giro
            }
            templateContent.put("emailAddress", systemParamConfig.getSystemAddressOne());
            templateContent.put("paymentMethod", paymentMethodName);
            templateContent.put("paymentAmount", appSubmissionDto.getAmountStr());
            String syName = "<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"<br/>"+AppConsts.MOH_AGENCY_NAME+"</b>";
            templateContent.put("MOH_AGENCY_NAME",syName);
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_EMAIL);
            emailParam.setTemplateContent(templateContent);
            emailParam.setSubject(subject);
            emailParam.setQueryCode(applicationDto.getApplicationNo());
            emailParam.setReqRefNum(applicationDto.getApplicationNo());
            emailParam.setRefId(applicationDto.getApplicationNo());
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
            notificationHelper.sendNotification(emailParam);

            EmailParam smsParam = new EmailParam();
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_SMS);
            smsParam.setSubject(subject);
            smsParam.setQueryCode(applicationDto.getApplicationNo());
            smsParam.setReqRefNum(applicationDto.getApplicationNo());
            smsParam.setRefId(applicationDto.getApplicationNo());
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            notificationHelper.sendNotification(smsParam);

            EmailParam msgParam = new EmailParam();
            msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_MSG);
            msgParam.setTemplateContent(templateContent);
            msgParam.setSubject(subject);
            msgParam.setQueryCode(applicationDto.getApplicationNo());
            msgParam.setReqRefNum(applicationDto.getApplicationNo());
            msgParam.setRefId(applicationDto.getApplicationNo());
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
                if( !svcCodeList.contains(appSvcRelatedInfoDto.getServiceCode())){
                    svcCodeList.add(appSvcRelatedInfoDto.getServiceCode());
                }
            }
            msgParam.setSvcCodeList(svcCodeList);
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            notificationHelper.sendNotification(msgParam);
            log.info("end send email sms and msg");
        }catch (Exception e){
            log.error(e.getMessage(),e);
            log.info("send app sumbit email fail");
        }

    }

    @Override
    public AppSubmissionDto submitRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process) {
        appSubmissionRequestInformationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        AppSubmissionDto appSubmissionDto = appSubmissionRequestInformationDto.getAppSubmissionDto();
        //asynchronous save the other data.
        informationEventBus(appSubmissionRequestInformationDto, process);
        return appSubmissionDto;
    }

    @Override
    public AppSubmissionDto submitPremisesListRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process) {
        appSubmissionRequestInformationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        AppSubmissionDto appSubmissionDto = appSubmissionRequestInformationDto.getAppSubmissionDto();
        //asynchronous save the other data.
        premisesListInformationEventBus(appSubmissionRequestInformationDto, process);
        return appSubmissionDto;
    }

    @Override
    public List<ApplicationDto> listApplicationByGroupId(String groupId) {
        return applicationFeClient.listApplicationByGroupId(groupId).getEntity();
    }

    @Override
    public AppSubmissionDto doSaveDraft(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationFeClient.saveDraft(appSubmissionDto).getEntity();
    }

    @Override
    public String getDraftNo(String appType) {
        return systemAdminClient.draftNumber(appType).getEntity();
    }

    @Override
    public String getGroupNo(String appType) {

        return systemAdminClient.applicationNumber(appType).getEntity();
    }

    @Override
    public FeeDto getNewAppAmount(AppSubmissionDto appSubmissionDto,boolean isCharity) {
        log.debug(StringUtil.changeForLog("the getNewAppAmount start ...."));
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        List<String> premisesTypes = IaisCommonUtils.genNewArrayList();
        List<LicenceFeeDto> licenceFeeQuaryDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                String premType = appGrpPremisesDto.getPremisesType();
                if(!StringUtil.isEmpty(premType)){
                    premisesTypes.add(premType);
                }
            }
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                LicenceFeeDto licenceFeeDto = new LicenceFeeDto();
                if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                    licenceFeeDto.setBaseService(appSvcRelatedInfoDto.getServiceCode());
                }else if(ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(appSvcRelatedInfoDto.getServiceType())){
                    if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getBaseServiceId())){
                        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(appSvcRelatedInfoDto.getBaseServiceId());
                        if(hcsaServiceDto != null) {
                            licenceFeeDto.setBaseService(hcsaServiceDto.getSvcCode());
                        }else{
                            log.info(StringUtil.changeForLog("current svc"+appSvcRelatedInfoDto.getServiceCode()+"'s baseSvcInfo is empty"));
                        }
                    }else{
                        log.info("base svcId is empty");
                    }
                }
                licenceFeeDto.setServiceCode(appSvcRelatedInfoDto.getServiceCode());
                licenceFeeDto.setServiceName(appSvcRelatedInfoDto.getServiceName());
                licenceFeeDto.setPremises(premisesTypes);
//                licenceFeeDto.setOnlyNewSpecified(onlySpecifiedSvc);
                if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                    licenceFeeDto.setOnlyNewSpecified(true);
                }else if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                    licenceFeeDto.setIncludeBase(true);
                }
                licenceFeeDto.setCharity(isCharity);
                //exiting offSite licence
                log.info(StringUtil.changeForLog("svcName:"+appSvcRelatedInfoDto.getServiceName()));
                Boolean existingOnSiteLic = licenceClient.existingOnSiteLicence(appSvcRelatedInfoDto.getServiceName(),appSubmissionDto.getLicenseeId()).getEntity();
                log.info(StringUtil.changeForLog("existing onSite licncence:"+existingOnSiteLic));
                licenceFeeDto.setExistOnsite(existingOnSiteLic);
                licenceFeeQuaryDtos.add(licenceFeeDto);
            }
        }
        log.info(StringUtil.changeForLog("licenceFeeQuaryDtos:"+JsonUtil.parseToJson(licenceFeeQuaryDtos)));
        log.debug(StringUtil.changeForLog("the getNewAppAmount end ...."));
        return appConfigClient.newFee(licenceFeeQuaryDtos).getEntity();
    }

    @Override
    public FeeDto getGroupAmount(AppSubmissionDto appSubmissionDto,boolean isCharity) {
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount start ...."));
        log.info(StringUtil.changeForLog("current account is charity:"+isCharity));
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        List<LicenceFeeDto> linenceFeeQuaryDtos = IaisCommonUtils.genNewArrayList();
        List<String> premisessTypes =  IaisCommonUtils.genNewArrayList();

        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
            premisessTypes.add(appGrpPremisesDto.getPremisesType());
        }
        List<String> baseServiceIds = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtos = appConfigClient.serviceCorrelation().getEntity();
        boolean onlySpecifiedSvc = false;
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                onlySpecifiedSvc = true;
            }
        }
       if(onlySpecifiedSvc && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                LicenceFeeDto licenceFeeDto = new LicenceFeeDto();
                if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                    licenceFeeDto.setIncludeBase(true);
                }
                HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceById(appSvcRelatedInfoDto.getBaseServiceId());
                licenceFeeDto.setBaseService(baseServiceDto.getSvcCode());
                licenceFeeDto.setServiceCode(appSvcRelatedInfoDto.getServiceCode());
                licenceFeeDto.setServiceName(appSvcRelatedInfoDto.getServiceName());
                licenceFeeDto.setPremises(premisessTypes);
                if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                    licenceFeeDto.setOnlyNewSpecified(true);
                }else{
                    licenceFeeDto.setIncludeBase(true);
                }
                licenceFeeDto.setCharity(isCharity);
                linenceFeeQuaryDtos.add(licenceFeeDto);
            }
        }else{
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                    baseServiceIds.add(appSvcRelatedInfoDto.getServiceId());
                }else if(ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(appSvcRelatedInfoDto.getServiceType())){
                    for(HcsaServiceCorrelationDto hcsaServiceCorrelationDto:hcsaServiceCorrelationDtos){
                        if(appSvcRelatedInfoDto.getServiceId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())){
                            baseServiceIds.add(hcsaServiceCorrelationDto.getBaseSvcId());
                        }
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("base service size:"+baseServiceIds.size()));
        if(!onlySpecifiedSvc) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                LicenceFeeDto licenceFeeDto = new LicenceFeeDto();
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(appSvcRelatedInfoDto.getServiceCode());
                if (hcsaServiceDto == null) {
                    log.info(StringUtil.changeForLog("hcsaServiceDto is empty "));
                    continue;
                }
                if (ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(hcsaServiceDto.getSvcType())) {
                    licenceFeeDto.setBaseService(hcsaServiceDto.getSvcCode());
                    licenceFeeDto.setIncludeBase(true);
                } else if (ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(hcsaServiceDto.getSvcType())) {
                    for (HcsaServiceCorrelationDto hcsaServiceCorrelationDto : hcsaServiceCorrelationDtos) {
                        if (hcsaServiceDto.getId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())) {
                            String baseSvcId = hcsaServiceCorrelationDto.getBaseSvcId();
                            if (baseServiceIds.contains(baseSvcId)) {
                                log.info("can match base service ...");
                                HcsaServiceDto baseService = HcsaServiceCacheHelper.getServiceById(baseSvcId);
                                licenceFeeDto.setBaseService(baseService.getSvcCode());
                            }
                        }
                    }
                }
                licenceFeeDto.setServiceCode(hcsaServiceDto.getSvcCode());
                licenceFeeDto.setServiceName(hcsaServiceDto.getSvcName());
                licenceFeeDto.setPremises(premisessTypes);
                licenceFeeDto.setCharity(isCharity);
                if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                    String licenceId = appSubmissionDto.getLicenceId();
                    Date licExpiryDate = appSubmissionDto.getLicExpiryDate();
                    licenceFeeDto.setExpiryDate(licExpiryDate);
                    licenceFeeDto.setLicenceId(licenceId);
                }
                linenceFeeQuaryDtos.add(licenceFeeDto);
            }
            log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl linenceFeeQuaryDtos.size() is -->:" + linenceFeeQuaryDtos.size()));
        }
        String appTYpe = appSubmissionDto.getAppType();
        FeeDto entity ;
        if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appTYpe)){
            entity = appConfigClient.renewFee(linenceFeeQuaryDtos).getEntity();
        }else{
            entity = appConfigClient.newFee(linenceFeeQuaryDtos).getEntity();
        }
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount end ...."));
        return  entity;
    }
    @Override
    public FeeDto getRenewalAmount(List<AppSubmissionDto> appSubmissionDtoList,boolean isCharity){
        List<LicenceFeeDto> linenceFeeQuaryDtos = IaisCommonUtils.genNewArrayList();
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getRenewalAmount start ...."));
        log.info(StringUtil.changeForLog("current account is charity:"+isCharity));
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtoList){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            List<String> premisessTypes =  IaisCommonUtils.genNewArrayList();
            if(isCharity){
                List<AppGrpPremisesDto> result = removeDuplicates(appGrpPremisesDtos);
                for(AppGrpPremisesDto appGrpPremisesDto:result){
                    premisessTypes.add(appGrpPremisesDto.getPremisesType());
                }
            }else{
                for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                    premisessTypes.add(appGrpPremisesDto.getPremisesType());
                }
            }
            List<String> baseServiceIds = IaisCommonUtils.genNewArrayList();
            List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtos = appConfigClient.serviceCorrelation().getEntity();
            boolean onlySpecifiedSvc = false;
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                    onlySpecifiedSvc = true;
                }
            }
            if(onlySpecifiedSvc && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    LicenceFeeDto licenceFeeDto = new LicenceFeeDto();
                    if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                        licenceFeeDto.setIncludeBase(true);
                    }
                    HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceById(appSvcRelatedInfoDto.getBaseServiceId());
                    licenceFeeDto.setBaseService(baseServiceDto.getSvcCode());
                    licenceFeeDto.setServiceCode(appSvcRelatedInfoDto.getServiceCode());
                    licenceFeeDto.setServiceName(appSvcRelatedInfoDto.getServiceName());
                    licenceFeeDto.setPremises(premisessTypes);
                    if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                        licenceFeeDto.setOnlyNewSpecified(true);
                    }else{
                        licenceFeeDto.setIncludeBase(true);
                    }
                    licenceFeeDto.setCharity(isCharity);
                    linenceFeeQuaryDtos.add(licenceFeeDto);
                }
            }else{
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                        baseServiceIds.add(appSvcRelatedInfoDto.getServiceId());
                    }else if(ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(appSvcRelatedInfoDto.getServiceType())){
                        for(HcsaServiceCorrelationDto hcsaServiceCorrelationDto:hcsaServiceCorrelationDtos){
                            if(appSvcRelatedInfoDto.getServiceId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())){
                                baseServiceIds.add(hcsaServiceCorrelationDto.getBaseSvcId());
                            }
                        }
                    }
                }
            }
            log.info(StringUtil.changeForLog("base service size:"+baseServiceIds.size()));
            if(!onlySpecifiedSvc) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    LicenceFeeDto licenceFeeDto = new LicenceFeeDto();
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(appSvcRelatedInfoDto.getServiceCode());
                    if (hcsaServiceDto == null) {
                        log.info(StringUtil.changeForLog("hcsaServiceDto is empty "));
                        continue;
                    }
                    if (ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(hcsaServiceDto.getSvcType())) {
                        licenceFeeDto.setBaseService(hcsaServiceDto.getSvcCode());
                        licenceFeeDto.setIncludeBase(true);
                    } else if (ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(hcsaServiceDto.getSvcType())) {
                        for (HcsaServiceCorrelationDto hcsaServiceCorrelationDto : hcsaServiceCorrelationDtos) {
                            if (hcsaServiceDto.getId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())) {
                                String baseSvcId = hcsaServiceCorrelationDto.getBaseSvcId();
                                if (baseServiceIds.contains(baseSvcId)) {
                                    log.info("can match base service ...");
                                    HcsaServiceDto baseService = HcsaServiceCacheHelper.getServiceById(baseSvcId);
                                    licenceFeeDto.setBaseService(baseService.getSvcCode());
                                }
                            }
                        }
                    }
                    licenceFeeDto.setServiceCode(hcsaServiceDto.getSvcCode());
                    licenceFeeDto.setServiceName(hcsaServiceDto.getSvcName());
                    licenceFeeDto.setPremises(premisessTypes);
                    licenceFeeDto.setCharity(isCharity);
                    if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                        String licenceId = appSubmissionDto.getLicenceId();
                        Date licExpiryDate = appSubmissionDto.getLicExpiryDate();
                        licenceFeeDto.setExpiryDate(licExpiryDate);
                        licenceFeeDto.setLicenceId(licenceId);
                    }
                    linenceFeeQuaryDtos.add(licenceFeeDto);
                }
                log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl linenceFeeQuaryDtos.size() is -->:" + linenceFeeQuaryDtos.size()));
            }
        }
        FeeDto entity ;
        entity = appConfigClient.renewFee(linenceFeeQuaryDtos).getEntity();
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount end ...."));
        return  entity;
    }
    @Override
    public FeeDto getCharityRenewalAmount(List<AppSubmissionDto> appSubmissionDtoList,boolean isCharity){
        List<LicenceFeeDto> linenceFeeQuaryDtos = IaisCommonUtils.genNewArrayList();
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getCharityRenewalAmount start ...."));
        log.info(StringUtil.changeForLog("current account is charity:"+isCharity));
        List<String> premises = IaisCommonUtils.genNewArrayList();
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtoList){
            List<String> premisessTypes =  IaisCommonUtils.genNewArrayList();
            String licenceId1 = appSubmissionDto.getLicenceId();
            List<PremisesDto> PremisesDtoList = licenceClient.getPremisesDto(licenceId1).getEntity();
            for(PremisesDto dto : PremisesDtoList){
                boolean flag = true;
                String premisesType = dto.getPremisesType();
                if(premises.size() == 0){
                    premises.add(dto.getHciCode());
                    premisessTypes.add(premisesType);
                    flag = false;
                }else{
                    for(String premisesCode : premises){
                        if(premisesCode.equals(dto.getHciCode())){
                            flag = false;
                            break;
                        }
                    }
                }
                if(flag){
                    premisessTypes.add(premisesType);
                    premises.add(dto.getHciCode());
                }
            }
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<String> baseServiceIds = IaisCommonUtils.genNewArrayList();
            List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtos = appConfigClient.serviceCorrelation().getEntity();
            boolean onlySpecifiedSvc = false;
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                    onlySpecifiedSvc = true;
                }
            }
            if(onlySpecifiedSvc && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    LicenceFeeDto licenceFeeDto = new LicenceFeeDto();
                    if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                        licenceFeeDto.setIncludeBase(true);
                    }
                    HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceById(appSvcRelatedInfoDto.getBaseServiceId());
                    licenceFeeDto.setBaseService(baseServiceDto.getSvcCode());
                    licenceFeeDto.setServiceCode(appSvcRelatedInfoDto.getServiceCode());
                    licenceFeeDto.setServiceName(appSvcRelatedInfoDto.getServiceName());
                    licenceFeeDto.setPremises(premisessTypes);
                    if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                        licenceFeeDto.setOnlyNewSpecified(true);
                    }else{
                        licenceFeeDto.setIncludeBase(true);
                    }
                    licenceFeeDto.setCharity(isCharity);
                    linenceFeeQuaryDtos.add(licenceFeeDto);
                }
            }else{
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                        baseServiceIds.add(appSvcRelatedInfoDto.getServiceId());
                    }else if(ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(appSvcRelatedInfoDto.getServiceType())){
                        for(HcsaServiceCorrelationDto hcsaServiceCorrelationDto:hcsaServiceCorrelationDtos){
                            if(appSvcRelatedInfoDto.getServiceId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())){
                                baseServiceIds.add(hcsaServiceCorrelationDto.getBaseSvcId());
                            }
                        }
                    }
                }
            }
            log.info(StringUtil.changeForLog("base service size:"+baseServiceIds.size()));
            if(!onlySpecifiedSvc) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    LicenceFeeDto licenceFeeDto = new LicenceFeeDto();
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(appSvcRelatedInfoDto.getServiceCode());
                    if (hcsaServiceDto == null) {
                        log.info(StringUtil.changeForLog("hcsaServiceDto is empty "));
                        continue;
                    }
                    if (ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(hcsaServiceDto.getSvcType())) {
                        licenceFeeDto.setBaseService(hcsaServiceDto.getSvcCode());
                        licenceFeeDto.setIncludeBase(true);
                    } else if (ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(hcsaServiceDto.getSvcType())) {
                        for (HcsaServiceCorrelationDto hcsaServiceCorrelationDto : hcsaServiceCorrelationDtos) {
                            if (hcsaServiceDto.getId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())) {
                                String baseSvcId = hcsaServiceCorrelationDto.getBaseSvcId();
                                if (baseServiceIds.contains(baseSvcId)) {
                                    log.info("can match base service ...");
                                    HcsaServiceDto baseService = HcsaServiceCacheHelper.getServiceById(baseSvcId);
                                    licenceFeeDto.setBaseService(baseService.getSvcCode());
                                }
                            }
                        }
                    }
                    licenceFeeDto.setServiceCode(hcsaServiceDto.getSvcCode());
                    licenceFeeDto.setServiceName(hcsaServiceDto.getSvcName());
                    licenceFeeDto.setPremises(premisessTypes);
                    licenceFeeDto.setCharity(isCharity);
                    if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                        String licenceId = appSubmissionDto.getLicenceId();
                        Date licExpiryDate = appSubmissionDto.getLicExpiryDate();
                        licenceFeeDto.setExpiryDate(licExpiryDate);
                        licenceFeeDto.setLicenceId(licenceId);
                    }
                    linenceFeeQuaryDtos.add(licenceFeeDto);
                }
                log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl linenceFeeQuaryDtos.size() is -->:" + linenceFeeQuaryDtos.size()));
            }
        }
        FeeDto entity ;
        entity = appConfigClient.renewFee(linenceFeeQuaryDtos).getEntity();
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount end ...."));
        return  entity;
    }

    private List<AppGrpPremisesDto> removeDuplicates(List<AppGrpPremisesDto> appGrpPremisesDtoList){
        if(IaisCommonUtils.isEmpty(appGrpPremisesDtoList)){
            return null;
        }
        List<AppGrpPremisesDto> result = IaisCommonUtils.genNewArrayList();
        if(result.size() == 0){
            result.add(appGrpPremisesDtoList.get(0));
        }else{
            for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
                boolean flag = true;
                for(AppGrpPremisesDto temp : result){
                    if(temp.getHciCode().equals(appGrpPremisesDto.getHciCode())){
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    result.add(appGrpPremisesDto);
                }
            }
        }
        return result;
    }

    @Override
    public PreOrPostInspectionResultDto judgeIsPreInspection(AppSubmissionDto appSubmissionDto) {
        RecommendInspectionDto recommendInspectionDto = new RecommendInspectionDto();
        List<RiskAcceptiionDto> riskAcceptiionDtos = IaisCommonUtils.genNewArrayList();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setSvcType(appSvcRelatedInfoDto.getServiceType());
            riskAcceptiionDto.setBaseServiceCodeList(appSvcRelatedInfoDto.getBaseServiceCodeList());
            riskAcceptiionDtos.add(riskAcceptiionDto);
        }

        return appConfigClient.recommendIsPreInspection(recommendInspectionDto).getEntity();
    }

    @Override
    public void setRiskToDto(AppSubmissionDto appSubmissionDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = IaisCommonUtils.genNewArrayList();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setApptype(appSubmissionDto.getAppType());
            riskAcceptiionDtoList.add(riskAcceptiionDto);
        }
        List<RiskResultDto> riskResultDtoList = appConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            String serviceCode = appSvcRelatedInfoDto.getServiceCode();
            RiskResultDto riskResultDto = getRiskResultDtoByServiceCode(riskResultDtoList,serviceCode);
            if(riskResultDto!= null){
                appSvcRelatedInfoDto.setScore(riskResultDto.getScore());
                appSvcRelatedInfoDto.setDoRiskDate(riskResultDto.getDoRiskDate());
            }
        }
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByAppNo(String appNo) {
        return applicationFeClient.getAppSubmissionDtoByAppNo(appNo).getEntity();
    }

    @Override
    public AppSubmissionDto getAppSubmissionDto(String appNo) {
        return applicationFeClient.getAppSubmissionDto(appNo).getEntity();
    }

    private RiskResultDto getRiskResultDtoByServiceCode(List<RiskResultDto> riskResultDtoList,String serviceCode){
        RiskResultDto result = null;
        if(riskResultDtoList == null || StringUtil.isEmpty(serviceCode)){
            return null;
        }
        for(RiskResultDto riskResultDto : riskResultDtoList){
            if(serviceCode.equals(riskResultDto.getSvcCode())){
                result = riskResultDto ;
                break;
            }
        }
        return result;
    }

    private void  informationEventBus(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process){
        //prepare request parameters
        appSubmissionRequestInformationDto.setEventRefNo(appSubmissionRequestInformationDto.getAppSubmissionDto().getAppGrpNo());
        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appSubmissionRequestInformationDto,
                generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT, EventBusConsts.OPERATION_REQUEST_INFORMATION,
                appSubmissionRequestInformationDto.getEventRefNo(), process);
    }

    private void premisesListInformationEventBus(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto,
                                                 Process process){
        appSubmissionRequestInformationDto.setEventRefNo(appSubmissionRequestInformationDto.getAppSubmissionDto().getAppGrpNo());
        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appSubmissionRequestInformationDto,
                generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_REQUEST_INFORMATION,
                appSubmissionRequestInformationDto.getEventRefNo(), process);
    }

    private  void eventBus(AppSubmissionDto appSubmissionDto, Process process){
        //prepare request parameters
        appSubmissionDto.setEventRefNo(appSubmissionDto.getAppGrpNo());

        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appSubmissionDto, generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_NEW_APP_SUBMIT,
                appSubmissionDto.getEventRefNo(), process);
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId) {
        return licenceClient.getAppSubmissionDto(licenceId).getEntity();
    }

    @Override
    public FeeDto getGroupAmendAmount(AmendmentFeeDto amendmentFeeDto) {
        return appConfigClient.amendmentFee(amendmentFeeDto).getEntity();
    }

    @Override
    public AppSubmissionDto submitRequestChange(AppSubmissionDto appSubmissionDto, Process process) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appSubmissionDto = applicationFeClient.saveAppsForRequestForChange(appSubmissionDto).getEntity();
        //eventBus(appSubmissionDto, process);
        return appSubmissionDto;
    }

    @Override
    public AppSubmissionDto submitRenew(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appSubmissionDto = applicationFeClient.saveAppsForRenew(appSubmissionDto).getEntity();
        return appSubmissionDto;
    }

    @Override
    public MsgTemplateDto getMsgTemplateById(String id) {
        MsgTemplateDto msgTemplateDto = systemAdminClient.getMsgTemplate(id).getEntity();
        return msgTemplateDto;
    }

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

    @Override
    public void feSendEmail(EmailDto emailDto) {
        String moduleName = currentApp + "-" + currentDomain;
        String refNo = String.valueOf(System.currentTimeMillis());
        EicRequestTrackingDto dto = new EicRequestTrackingDto();
        dto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        dto.setActionClsName(this.getClass().getName());
        dto.setActionMethod("callEicSendEmail");
        dto.setDtoClsName(emailDto.getClass().getName());
        dto.setDtoObject(JsonUtil.parseToJson(emailDto));
        dto.setRefNo(refNo);
        dto.setModuleName(moduleName);
        eicClient.saveEicTrack(dto);
        callEicSendEmail(emailDto);
        dto = eicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
        Date now = new Date();
        dto.setProcessNum(1);
        dto.setFirstActionAt(now);
        dto.setLastActionAt(now);
        dto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        List<EicRequestTrackingDto> list = IaisCommonUtils.genNewArrayList(1);
        list.add(dto);
        eicClient.updateStatus(list);
    }

    public void callEicSendEmail(EmailDto emailDto){
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        feEicGatewayClient.feSendEmail(emailDto,signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }

    @Override
    public ApplicationGroupDto createApplicationDataByWithOutRenewal(RenewDto renewDto) {
        return applicationFeClient.createApplicationDataByWithOutRenewal(renewDto).getEntity();
    }

    @Override
    public void updateApplicationsStatus(String appGroupId, String stuts) {
        List<ApplicationDto> applicationDtos = listApplicationByGroupId(appGroupId);
        for(ApplicationDto application : applicationDtos){
            application.setStatus(stuts);
            applicationFeClient.updateApplication(application);
        }
    }

    @Override
    public boolean checkRenewalStatus(String licenceId) {
        boolean flag = true;
        List<ApplicationDto> apps = applicationFeClient.getAppByLicIdAndExcludeNew(licenceId).getEntity();
        for(ApplicationDto app : apps){
            if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(app.getApplicationType())
                    && !(ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(app.getStatus()))){
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public AppSubmissionDto getExistBaseSvcInfo(List<String> licenceIds) {
        return licenceClient.getExistBaseSvcInfo(licenceIds).getEntity();
    }

    @Override
    public void transform(AppSubmissionDto appSubmissionDto, String licenseeId) {
        Double amount = 0.0;
        AuditTrailDto internet = AuditTrailHelper.getCurrentAuditTrailDto();
        String grpNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE).getEntity();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
            String serviceName = appSvcRelatedInfoDto.getServiceName();
            HcsaServiceDto hcsaServiceDto =appConfigClient.getActiveHcsaServiceDtoByName(serviceName).getEntity();
            String svcId = hcsaServiceDto.getId();
            String svcCode = hcsaServiceDto.getSvcCode();
            appSvcRelatedInfoDto.setServiceId(svcId);
            appSvcRelatedInfoDto.setServiceCode(svcCode);
        }

        appSubmissionDto.setAppGrpNo(grpNo);
        appSubmissionDto.setFromBe(false);
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        appSubmissionDto.setAmount(amount);
        appSubmissionDto.setAuditTrailDto(internet);
        appSubmissionDto.setPreInspection(true);
        appSubmissionDto.setRequirement(true);
        appSubmissionDto.setLicenseeId(licenseeId);
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
        String draftNo = appSubmissionDto.getDraftNo();
        if(draftNo==null){
            draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            appSubmissionDto.setDraftNo(draftNo);
        }
        appSubmissionDto.setAppGrpId(null);
        setRiskToDto(appSubmissionDto);
    }

    @Override
    public void saveAppsubmission(AppSubmissionDto appSubmissionDto) {
        AppSubmissionDto entity = applicationFeClient.saveSubmision(appSubmissionDto).getEntity();
        applicationFeClient.saveApps(entity).getEntity();
    }

    @Override
    public void setDraftNo(AppSubmissionDto appSubmissionDto) {
        String appType=null;
        if(appSubmissionDto!=null){
             appType = appSubmissionDto.getAppType();
        }
        if(appType!=null){
            String draft = systemAdminClient.draftNumber(appType).getEntity();
            appSubmissionDto.setDraftNo(draft);
        }

    }

    @Override
    public void saveAppGrpMisc(AppGroupMiscDto appGroupMiscDto) {
        applicationFeClient.saveAppGroupMiscDto(appGroupMiscDto);
    }

    @Override
    public List<AppSubmissionDto> getAppSubmissionDtoByGroupNo(String groupNo) {
        List<ApplicationDto> applicationDtos = applicationFeClient.getApplicationsByGroupNo(groupNo).getEntity();
        List<AppSubmissionDto> appSubmissionDtos =IaisCommonUtils.genNewArrayList();
        for(ApplicationDto applicationDto : applicationDtos){
            AppSubmissionDto appSubmissionDto = applicationFeClient.getAppSubmissionDtoByAppNo(applicationDto.getApplicationNo()).getEntity();
            appSubmissionDtos.add(appSubmissionDto);
        }
        return appSubmissionDtos;
    }

    @Override
    public void deleteOverdueDraft(String draftValidity) {
        applicationFeClient.deleteOverdueDraft(draftValidity);
    }

    @Override
    public List<AppGrpPremisesDto> getAppGrpPremisesDto(String appNo) {
        List<AppGrpPremisesDto> entity = applicationFeClient.getAppGrpPremisesDtoByAppGroId(appNo).getEntity();
        return entity;
    }

    @Override
    public AppFeeDetailsDto saveAppFeeDetails(AppFeeDetailsDto appFeeDetailsDto) {
        return   applicationFeClient.saveAppFeeDetails(appFeeDetailsDto).getEntity();
    }

    @Override
    public ApplicationDto getMaxVersionApp(String appNo) {
        return applicationFeClient.getApplicationDtoByVersion(appNo).getEntity();
    }

    @Override
    public void updateMsgStatus(String msgId, String status) {
        feMessageClient.updateMsgStatus(msgId,status);
    }

    @Override
    public InterMessageDto getInterMessageById(String msgId) {
        return feMessageClient.getInterMessageById(msgId).getEntity();
    }

    @Override
    public List<String> getHciFromPendAppAndLic(String licenseeId, List<HcsaServiceDto> hcsaServiceDtos) {
        List<String> result = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(licenseeId) && !IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            List<String> svcIds = IaisCommonUtils.genNewArrayList();
            List<String> svcNames = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtos){
                svcIds.add(hcsaServiceDto.getId());
                svcNames.add(hcsaServiceDto.getSvcName());
            }
            String svcNameStr = JsonUtil.parseToJson(svcNames);
            String svcIdStr = JsonUtil.parseToJson(svcIds);
            List<PremisesDto> premisesDtos = licenceClient.getPremisesByLicseeIdAndSvcName(licenseeId,svcNameStr).getEntity();
            List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos = applicationFeClient.getPendAppPremises(licenseeId,svcIdStr).getEntity();
            if(!IaisCommonUtils.isEmpty(premisesDtos)){
                for(PremisesDto premisesHciDto:premisesDtos){
                    result.addAll(NewApplicationHelper.genPremisesHciList(premisesHciDto));
                }
            }
            if(!IaisCommonUtils.isEmpty(appGrpPremisesEntityDtos)){
                for(AppGrpPremisesEntityDto premisesEntityDto:appGrpPremisesEntityDtos){
                    PremisesDto premisesDto = MiscUtil.transferEntityDto(premisesEntityDto,PremisesDto.class);
                    result.addAll(NewApplicationHelper.genPremisesHciList(premisesDto));
                }
            }
        }
        return result;
    }

    @Override
    public List<AppGrpPremisesEntityDto> getPendAppPremises(String licenseeId, List<HcsaServiceDto> hcsaServiceDtos) {
        List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(licenseeId) && !IaisCommonUtils.isEmpty(hcsaServiceDtos)) {
            List<String> svcIds = IaisCommonUtils.genNewArrayList();
            for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
                svcIds.add(hcsaServiceDto.getId());
            }
            String svcIdStr = JsonUtil.parseToJson(svcIds);
            appGrpPremisesEntityDtos = applicationFeClient.getPendAppPremises(licenseeId,svcIdStr).getEntity();
        }
        return appGrpPremisesEntityDtos;
    }

    @Override
    public List<AppAlignLicQueryDto> getAppAlignLicQueryDto(String licenseeId, List<String> svcNameList) {
        List<AppAlignLicQueryDto> appAlignLicQueryDtos = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(licenseeId) && !IaisCommonUtils.isEmpty(svcNameList)) {
            String svcNames = JsonUtil.parseToJson(svcNameList);
            appAlignLicQueryDtos = licenceClient.getAppAlignLicQueryDto(licenseeId,svcNames).getEntity();
        }
        return appAlignLicQueryDtos;
    }

    @Override
    public List<AppGrpPremisesDto> getLicPremisesInfo(String id) {
        return licenceClient.getLicPremisesById(id).getEntity();
    }

    @Override
    public Boolean isNewLicensee(String licenseeId) {
        return licenceClient.checkIsNewLicsee(licenseeId).getEntity();
    }

    @Override
    public InterMessageDto getInterMessageBySubjectLike(String subject, String status) {
        return feMessageClient.getInterMessageBySubjectLike(subject,status).getEntity();

    }

    @Override
    public AppGrpPremisesEntityDto getPremisesByAppNo(String appNo) {
        return applicationFeClient.getPremisesByAppNo(appNo).getEntity();
    }

    @Override
    public AppGrpPrimaryDocDto getMaxVersionPrimaryComDoc(String appGrpId, String configDocId) {
        return applicationFeClient.getMaxVersionPrimaryComDoc(appGrpId,configDocId).getEntity();
    }

    @Override
    public AppSvcDocDto getMaxVersionSvcComDoc(String appGrpId, String configDocId) {
        return applicationFeClient.getMaxVersionSvcComDoc(appGrpId,configDocId).getEntity();
    }

    @Override
    public AppGrpPrimaryDocDto getMaxVersionPrimarySpecDoc(String appGrpId, String configDocId, String appNo) {
        return applicationFeClient.getMaxVersionPrimarySpecDoc(appGrpId,configDocId,appNo).getEntity();
    }

    @Override
    public AppSvcDocDto getMaxVersionSvcSpecDoc(String appGrpId, String configDocId, String appNo) {
        return applicationFeClient.getMaxVersionSvcSpecDoc(appGrpId,configDocId,appNo).getEntity();
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByAppGrpNo(String appGrpNo) {
        return applicationFeClient.getAppSubmissionDtoByAppGrpNo(appGrpNo).getEntity();
    }

    @Override
    public List<AppGrpPrimaryDocDto> syncPrimaryDoc(String appType,Boolean isRfi,List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos, List<HcsaSvcDocConfigDto> primaryDocConfig) throws CloneNotSupportedException {
        List<AppGrpPrimaryDocDto> newGrpPrimaryDocList = IaisCommonUtils.genNewArrayList();
        //rfc/renew for primary doc
        boolean rfcOrRenw = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType) || ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType);
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos) && !IaisCommonUtils.isEmpty(primaryDocConfig)){
            List<String> docConfigIds = IaisCommonUtils.genNewArrayList();
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtos){
                docConfigIds.add(appGrpPrimaryDocDto.getSvcComDocId());
            }
            List<HcsaSvcDocConfigDto> oldHcsaSvcDocConfigDtos = serviceConfigService.getPrimaryDocConfigByIds(docConfigIds);
            if(!IaisCommonUtils.isEmpty(oldHcsaSvcDocConfigDtos)){
                if(rfcOrRenw && !isRfi){
                    for(HcsaSvcDocConfigDto oldDocConfig:oldHcsaSvcDocConfigDtos){
                        for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtos){
                            if(oldDocConfig.getId().equals(appGrpPrimaryDocDto.getSvcComDocId())){
                                String oldDocTitle = oldDocConfig.getDocTitle();
                                for(HcsaSvcDocConfigDto docConfig:primaryDocConfig){
                                    if(docConfig.getDocTitle().equals(oldDocTitle)){
                                        AppGrpPrimaryDocDto newGrpPrimaryDoc = (AppGrpPrimaryDocDto) CopyUtil.copyMutableObject(appGrpPrimaryDocDto);
                                        newGrpPrimaryDoc.setSvcComDocId(docConfig.getId());
                                        newGrpPrimaryDoc.setSvcComDocName(docConfig.getDocTitle());
                                        newGrpPrimaryDocList.add(newGrpPrimaryDoc);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }else if(isRfi){
                    //set title name
                    for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtos){
                        for(HcsaSvcDocConfigDto oldDocConfig:oldHcsaSvcDocConfigDtos){
                            if(oldDocConfig.getId().equals(appGrpPrimaryDocDto.getSvcComDocId())){
                                AppGrpPrimaryDocDto newGrpPrimaryDoc = (AppGrpPrimaryDocDto) CopyUtil.copyMutableObject(appGrpPrimaryDocDto);
                                newGrpPrimaryDoc.setSvcComDocName(oldDocConfig.getDocTitle());
                                newGrpPrimaryDocList.add(newGrpPrimaryDoc);
                                break;
                            }
                        }
                    }
                }
            }
        }else{
            newGrpPrimaryDocList = appGrpPrimaryDocDtos;
        }
        return newGrpPrimaryDocList;
    }

    private AppSvcRelatedInfoDto getAppSvcRelatedInfoDto(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos){
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            return appSvcRelatedInfoDtos.get(0);
        }
        return new AppSvcRelatedInfoDto();
    }

    private boolean compareHciName(List<AppGrpPremisesDto> appGrpPremisesDtos,List<AppGrpPremisesDto> oldAppGrpPremisesDtos){
        int length = appGrpPremisesDtos.size();
        int oldLength = oldAppGrpPremisesDtos.size();
        if(length == oldLength){
            for(int i=0; i<length; i++){
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(0);
                AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(0);
                if(!getHciName(appGrpPremisesDto).equals(getHciName(oldAppGrpPremisesDto))){
                    return false;
                }
            }
        }
        //is same
        return true;
    }
    private String getHciName(AppGrpPremisesDto appGrpPremisesDto){
        String hciName = "";
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
            hciName = appGrpPremisesDto.getHciName();
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            hciName = appGrpPremisesDto.getConveyanceVehicleNo();
        }
        return hciName;
    }

    private AppSvcLaboratoryDisciplinesDto getDisciplinesDto(List<AppSvcLaboratoryDisciplinesDto> disciplinesDto,String hciName){
        for(AppSvcLaboratoryDisciplinesDto iten:disciplinesDto){
            if(hciName.equals(iten.getPremiseVal())){
                return iten;
            }
        }
        return new AppSvcLaboratoryDisciplinesDto();
    }
    private boolean compareLocation(List<AppGrpPremisesDto> appGrpPremisesDtos,List<AppGrpPremisesDto> oldAppGrpPremisesDtos){
        int length = appGrpPremisesDtos.size();
        int oldLength = oldAppGrpPremisesDtos.size();
        if(length == oldLength){
            for(int i=0; i<length; i++){
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(0);
                AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(0);
                if(!appGrpPremisesDto.getAddress().equals(oldAppGrpPremisesDto.getAddress())){
                    return false;
                }
            }
        }
        //is same
        return true;
    }
}