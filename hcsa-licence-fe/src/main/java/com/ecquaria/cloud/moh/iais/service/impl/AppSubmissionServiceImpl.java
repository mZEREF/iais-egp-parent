package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.NewApplicationConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.ServiceStepDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
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
import sop.webflow.rt.api.BaseProcessClass;
import sop.webflow.rt.api.Process;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
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
    @Autowired
    private RequestForChangeServiceImpl requestForChangeService;


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
            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
            templateContent.put("systemLink", loginUrl);
            String paymentMethodName = "noNeedPayment";
            String payMethod = appSubmissionDto.getPaymentMethod();
            if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)) {
                paymentMethodName = "GIRO";
                //need change giro
            }else if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payMethod)
                    || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payMethod)
                    || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payMethod)) {
                paymentMethodName = "onlinePayment";
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
                Boolean existingOnSiteLic = licenceClient.existingOnSiteOrConveLic(appSvcRelatedInfoDto.getServiceName(),appSubmissionDto.getLicenseeId()).getEntity();
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
        log.debug(StringUtil.changeForLog("do syncPrimaryDoc start ..."));
        log.debug(StringUtil.changeForLog("appType:"+ appType));
        log.debug(StringUtil.changeForLog("isRfi:"+ isRfi));
        List<AppGrpPrimaryDocDto> newGrpPrimaryDocList = IaisCommonUtils.genNewArrayList();
        //rfc/renew for primary doc
        boolean rfcOrRenwOrNew = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType) || ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType);
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos) && !IaisCommonUtils.isEmpty(primaryDocConfig)){
            List<String> docConfigIds = IaisCommonUtils.genNewArrayList();
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtos){
                docConfigIds.add(appGrpPrimaryDocDto.getSvcComDocId());
            }
            List<HcsaSvcDocConfigDto> oldHcsaSvcDocConfigDtos = serviceConfigService.getPrimaryDocConfigByIds(docConfigIds);
            if(!IaisCommonUtils.isEmpty(oldHcsaSvcDocConfigDtos)){
                if(rfcOrRenwOrNew && !isRfi){
                    for(HcsaSvcDocConfigDto oldDocConfig:oldHcsaSvcDocConfigDtos){
                        for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtos){
                            if(oldDocConfig.getId().equals(appGrpPrimaryDocDto.getSvcComDocId())){
                                String oldDocTitle = oldDocConfig.getDocTitle();
                                for(HcsaSvcDocConfigDto docConfig:primaryDocConfig){
                                    if(docConfig.getDocTitle().equals(oldDocTitle)){
                                        AppGrpPrimaryDocDto newGrpPrimaryDoc = (AppGrpPrimaryDocDto) CopyUtil.copyMutableObject(appGrpPrimaryDocDto);
                                        newGrpPrimaryDoc.setSvcComDocId(docConfig.getId());
                                        newGrpPrimaryDoc.setSvcComDocName(docConfig.getDocTitle());
                                        //newGrpPrimaryDoc.setDocConfigVersion(docConfig.getVersion());
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
                                //newGrpPrimaryDoc.setDocConfigVersion(oldDocConfig.getVersion());
                                newGrpPrimaryDocList.add(newGrpPrimaryDoc);
                                break;
                            }
                        }
                    }
                }
            }
        }
        log.debug(StringUtil.changeForLog("do syncPrimaryDoc end ..."));
        return newGrpPrimaryDocList;
    }

    @Override
    public List<AppGrpPrimaryDocDto> handlerPrimaryDoc(List<AppGrpPremisesDto> appGrpPremisesDtos,List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos) {
        log.debug(StringUtil.changeForLog("do handlerPrimaryDoc start ..."));
        if(IaisCommonUtils.isEmpty(appGrpPremisesDtos) || IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)){
            return appGrpPrimaryDocDtos;
        }
        //remove empty doc
        List<AppGrpPrimaryDocDto> notEmptyDocList = IaisCommonUtils.genNewArrayList();
        for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtos){
            if(!StringUtil.isEmpty(appGrpPrimaryDocDto.getFileRepoId())){
                notEmptyDocList.add(appGrpPrimaryDocDto);
            }
        }

        //add empty doc
        List<HcsaSvcDocConfigDto> hcsaSvcDocDtos = serviceConfigService.getAllHcsaSvcDocs(null);
        List<AppGrpPrimaryDocDto> newPrimaryDocList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(hcsaSvcDocDtos)){
            log.debug(StringUtil.changeForLog("hcsaSvcDocDtos not empty ..."));
            if(notEmptyDocList != null && notEmptyDocList.size() > 0){
                List<HcsaSvcDocConfigDto> oldHcsaSvcDocDtos = serviceConfigService.getPrimaryDocConfigById(notEmptyDocList.get(0).getSvcComDocId());
                for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:hcsaSvcDocDtos){
                    String docTitle = hcsaSvcDocConfigDto.getDocTitle();
                    String dupPrem = hcsaSvcDocConfigDto.getDupForPrem();
                    int i = 0;
                    for(HcsaSvcDocConfigDto oldHcsaSvcDocDto:oldHcsaSvcDocDtos){
                        if(docTitle.equals(oldHcsaSvcDocDto.getDocTitle())){
                            AppGrpPrimaryDocDto appGrpPrimaryDocDto = NewApplicationHelper.getAppGrpprimaryDocDto(oldHcsaSvcDocDto.getId(),notEmptyDocList);
                            if(appGrpPrimaryDocDto == null){
                                appGrpPrimaryDocDto = NewApplicationHelper.genEmptyPrimaryDocDto(hcsaSvcDocConfigDto.getId());
                                handlerDupPremDoc(dupPrem,appGrpPrimaryDocDto,appGrpPremisesDtos,newPrimaryDocList);
                            }else{
                                appGrpPrimaryDocDto.setSvcComDocId(hcsaSvcDocConfigDto.getId());
                                handlerDupPremDoc(dupPrem,appGrpPrimaryDocDto,appGrpPremisesDtos,newPrimaryDocList);
                            }
                        }
                        if(i == oldHcsaSvcDocDtos.size()){
                            //add empty doc
                            AppGrpPrimaryDocDto appGrpPrimaryDocDto = NewApplicationHelper.genEmptyPrimaryDocDto(hcsaSvcDocConfigDto.getId());
                            newPrimaryDocList.add(appGrpPrimaryDocDto);
                        }
                        i++;
                    }
                }
            }else{
                for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:hcsaSvcDocDtos){
                    AppGrpPrimaryDocDto appGrpPrimaryDocDto = NewApplicationHelper.genEmptyPrimaryDocDto(hcsaSvcDocConfigDto.getId());
                    newPrimaryDocList.add(appGrpPrimaryDocDto);
                }
            }
        }
        log.debug(StringUtil.changeForLog("do handlerPrimaryDoc end ..."));
        return newPrimaryDocList;
    }

    @Override
    public Map<String, String> doPreviewAndSumbit(BaseProcessClass bpc) {
        Map<String, String> previewAndSubmitMap = IaisCommonUtils.genNewHashMap();
        //
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.OLDAPPSUBMISSIONDTO);
        previewAndSubmitMap = doPreviewSubmitValidate(previewAndSubmitMap,appSubmissionDto,oldAppSubmissionDto,bpc);
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),previewAndSubmitMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
        return previewAndSubmitMap;
    }

    @Override
    public Map<String, String> doPreviewSubmitValidate(Map<String, String> previewAndSubmitMap, AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto,BaseProcessClass bpc) {
        StringBuilder sB = new StringBuilder(10);
        HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
        List<String> premisesHciList = (List<String>) ParamUtil.getSessionAttr(bpc.request, NewApplicationConstant.PREMISES_HCI_LIST);
        String keyWord = MasterCodeUtil.getCodeDesc("MS001");
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        Map<String, String> premissMap = requestForChangeService.doValidatePremiss(appSubmissionDto, oldAppSubmissionDto, premisesHciList, keyWord, isRfi);
        premissMap.remove("hciNameUsed");
        if (!premissMap.isEmpty()) {
            previewAndSubmitMap.put("premiss", MessageUtil.replaceMessage("GENERAL_ERR0006","premiss","field"));
            String premissMapStr = JsonUtil.parseToJson(premissMap);
            log.info(StringUtil.changeForLog("premissMap json str:" + premissMapStr));
            coMap.put("premises", "");
        } else {
            coMap.put("premises", "premises");
        }
        //
        Map<String, AppSvcPrincipalOfficersDto> licPersonMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.LICPERSONSELECTMAP);
        //
        Map<String, List<HcsaSvcPersonnelDto>> allSvcAllPsnConfig = getAllSvcAllPsnConfig(bpc.request);
        List<AppSvcRelatedInfoDto> dto = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        ServiceStepDto serviceStepDto = new ServiceStepDto();
        for (int i = 0; i < dto.size(); i++) {
            String serviceId = dto.get(i).getServiceId();
            List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = serviceConfigService.getHcsaServiceStepSchemesByServiceId(serviceId);
            serviceStepDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);
            List<HcsaSvcPersonnelDto> currentSvcAllPsnConfig = serviceConfigService.getSvcAllPsnConfig(hcsaServiceStepSchemeDtos, serviceId);
            Map<String, String> map = doCheckBox(bpc, sB, allSvcAllPsnConfig, currentSvcAllPsnConfig, dto.get(i),systemParamConfig.getUploadFileLimit(),systemParamConfig.getUploadFileType());
            if (!map.isEmpty()) {
                previewAndSubmitMap.putAll(map);
                String mapStr = JsonUtil.parseToJson(map);
                log.info(StringUtil.changeForLog("map json str:" + mapStr));
            }
            NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),map,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
        }
        Map<String, String> documentMap = IaisCommonUtils.genNewHashMap();
        documentValid(bpc.request, documentMap);
        doCommomDocument(bpc.request, documentMap);
        NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),documentMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
        if (!documentMap.isEmpty()) {
            previewAndSubmitMap.put("document", MessageUtil.replaceMessage("GENERAL_ERR0006","document","field"));
            String documentMapStr = JsonUtil.parseToJson(documentMap);
            log.info(StringUtil.changeForLog("documentMap json str:" + documentMapStr));
            coMap.put("document", "");
        } else {
            coMap.put("document", "document");
        }
        if (!StringUtil.isEmpty(sB.toString())) {
            previewAndSubmitMap.put("serviceId", sB.toString());
            coMap.put("information", "");
        } else {
            coMap.put("information", "information");
        }
        bpc.request.getSession().setAttribute("coMap", coMap);

        return previewAndSubmitMap;
    }

    @Override
    public Map<String, List<HcsaSvcPersonnelDto>> getAllSvcAllPsnConfig(HttpServletRequest request) {
        Map<String, List<HcsaSvcPersonnelDto>> svcAllPsnConfig = (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.SERVICEALLPSNCONFIGMAP);
        if (svcAllPsnConfig == null) {
            AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<String> svcIds = IaisCommonUtils.genNewArrayList();
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                svcIds.add(appSvcRelatedInfoDto.getServiceId());
            }
            List<HcsaServiceStepSchemeDto> svcStepConfigs = serviceConfigService.getHcsaServiceStepSchemesByServiceId(svcIds);
            svcAllPsnConfig = serviceConfigService.getAllSvcAllPsnConfig(svcStepConfigs, svcIds);
        }
        return svcAllPsnConfig;
    }

    @Override
    public Map<String, String> doCheckBox(BaseProcessClass bpc, StringBuilder sB, Map<String, List<HcsaSvcPersonnelDto>> allSvcAllPsnConfig, List<HcsaSvcPersonnelDto> currentSvcAllPsnConfig, AppSvcRelatedInfoDto dto, int uploadFileLimit, String sysFileType) {
        String serviceId = dto.getServiceId();
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto : currentSvcAllPsnConfig) {
            String psnType = hcsaSvcPersonnelDto.getPsnType();
            int mandatoryCount = hcsaSvcPersonnelDto.getMandatoryCount();
            if ("PO".equals(psnType)) {
                List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = dto.getAppSvcPrincipalOfficersDtoList();
                if (appSvcPrincipalOfficersDtoList == null) {
                    if (mandatoryCount > 0) {
                        errorMap.put("error", "PO");
                        sB.append(serviceId);
                        log.info("PO null");
                    }
                } else if (appSvcPrincipalOfficersDtoList.size() < mandatoryCount) {
                    errorMap.put("error", "PO");
                    sB.append(serviceId);
                    log.info("PO mandatoryCount");
                }
            } else if ("SVCPSN".equals(psnType)) {
                List<AppSvcPersonnelDto> appSvcPersonnelDtoList = dto.getAppSvcPersonnelDtoList();
                if (appSvcPersonnelDtoList == null) {
                    if (mandatoryCount > 0) {
                        errorMap.put("error", "SVCPSN");
                        sB.append(serviceId);
                        log.info("SVCPSN null");
                    }
                } else if (appSvcPersonnelDtoList.size() < mandatoryCount) {
                    errorMap.put("error", "SVCPSN");
                    sB.append(serviceId);
                    log.info("SVCPSN mandatoryCount");
                }
            } else if ("CGO".equals(psnType)) {
                List<AppSvcCgoDto> appSvcCgoDtoList = dto.getAppSvcCgoDtoList();
                if (appSvcCgoDtoList == null) {
                    if (mandatoryCount > 0) {
                        errorMap.put("error", "CGO");
                        sB.append(serviceId);
                        log.info("CGO null");
                    }
                } else if (appSvcCgoDtoList.size() < mandatoryCount) {
                    errorMap.put("error", "CGO");
                    sB.append(serviceId);
                    log.info("CGO mandatoryCount");
                }
            } else if ("MAP".equals(psnType)) {
                List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = dto.getAppSvcMedAlertPersonList();
                if (appSvcMedAlertPersonList == null) {
                    if (mandatoryCount > 0) {
                        errorMap.put("error", "MAP");
                        sB.append(serviceId);
                        log.info("MAP null");
                    }
                } else if (appSvcMedAlertPersonList.size() < mandatoryCount) {
                    errorMap.put("error", "MAP");
                    sB.append(serviceId);
                    log.info("MAP mandatoryCount");
                }
            }
        }
        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = dto.getAppSvcMedAlertPersonList();
        Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.LICPERSONSELECTMAP);
        Map<String, String> map = NewApplicationHelper.doValidateMedAlertPsn(appSvcMedAlertPersonList, licPersonMap, dto.getServiceCode());
        log.info(JsonUtil.parseToJson(map));
        if (!map.isEmpty()) {
            sB.append(serviceId);
            errorMap.put("Medaler", "error");
        }
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = dto.getAppSvcLaboratoryDisciplinesDtoList();
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = allSvcAllPsnConfig.get(serviceId);

        List<AppSvcCgoDto> appSvcCgoDtoList = dto.getAppSvcCgoDtoList();
        doAppSvcCgoDto(hcsaSvcPersonnelDtos, errorMap, appSvcCgoDtoList, serviceId, sB);
        log.info(sB.toString());
        List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = dto.getAppSvcDisciplineAllocationDtoList();
        doSvcDis(errorMap, appSvcDisciplineAllocationDtoList, serviceId, sB);
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(errorMap) + "doSvcDis"));
        dolabory(errorMap,appSvcDisciplineAllocationDtoList, appSvcLaboratoryDisciplinesDtoList, serviceId, sB);
        log.info(sB.toString());
        doSvcDisdolabory(errorMap, appSvcDisciplineAllocationDtoList, appSvcLaboratoryDisciplinesDtoList, serviceId, sB);
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(errorMap) + "doSvcDisdolabory"));
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = dto.getAppSvcPrincipalOfficersDtoList();
        Map<String, String> govenMap = NewApplicationHelper.doValidateGovernanceOfficers(dto.getAppSvcCgoDtoList(), licPersonMap, dto.getServiceCode());
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(govenMap)));
        if (!govenMap.isEmpty()) {
            errorMap.put("CGO", "error");
            sB.append(serviceId);
            log.info("govenMap is error");
        }
        doPO(hcsaSvcPersonnelDtos, errorMap, appSvcPrincipalOfficersDtoList, serviceId, sB);
        log.info(sB.toString());
        List<AppSvcPersonnelDto> appSvcPersonnelDtoList = dto.getAppSvcPersonnelDtoList();
        doAppSvcPersonnelDtoList(hcsaSvcPersonnelDtos, errorMap, appSvcPersonnelDtoList, serviceId, sB,dto.getServiceCode());
        log.info(sB.toString());
        List<AppSvcDocDto> appSvcDocDtoLit = dto.getAppSvcDocDtoLit();
        doSvcDocument(errorMap, appSvcDocDtoLit, serviceId, sB,uploadFileLimit,sysFileType);
        log.info(sB.toString());

        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(errorMap)));

        return errorMap;
    }

    @Override
    public List<AppGrpPrimaryDocDto> documentValid(HttpServletRequest request, Map<String, String> errorMap) {
        log.info(StringUtil.changeForLog("the do doValidatePremiss start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = appSubmissionDto.getAppGrpPrimaryDocDtos();
        if (appGrpPrimaryDocDtoList == null) {
            return null;
        }
        for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtoList) {
            if(StringUtil.isEmpty(appGrpPrimaryDocDto.getMd5Code())){
                continue;
            }
            String keyName = "";
            if (StringUtil.isEmpty(appGrpPrimaryDocDto.getPremisessName()) && StringUtil.isEmpty(appGrpPrimaryDocDto.getPremisessType())) {
                //common
                keyName = "common" + appGrpPrimaryDocDto.getSvcComDocId();
            } else {
                keyName = "prem" + appGrpPrimaryDocDto.getSvcComDocId() + appGrpPrimaryDocDto.getPremisessName();
            }
            long length = appGrpPrimaryDocDto.getRealDocSize();
            int uploadFileLimit = systemParamConfig.getUploadFileLimit();
            if (length / 1024 / 1024 > uploadFileLimit) {
                errorMap.put(keyName, MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(uploadFileLimit), "sizeMax"));
                continue;
            }
            Boolean flag = Boolean.FALSE;
            String name = appGrpPrimaryDocDto.getDocName();
            String substring = name.substring(name.lastIndexOf('.') + 1);
            String sysFileType = systemParamConfig.getUploadFileType();
            String[] sysFileTypeArr = FileUtils.fileTypeToArray(sysFileType);
            for (String f : sysFileTypeArr) {
                if (f.equalsIgnoreCase(substring)) {
                    flag = Boolean.TRUE;
                }
            }
            if (!flag) {
                errorMap.put(keyName, MessageUtil.replaceMessage("GENERAL_ERR0018", sysFileType, "fileType"));
            }
            String errMsg = errorMap.get(keyName);
            if (StringUtil.isEmpty(errMsg)) {
                appGrpPrimaryDocDto.setPassValidate(true);
            }
        }
        return appGrpPrimaryDocDtoList;
    }

    private static void doSvcDocument(Map<String, String> map, List<AppSvcDocDto> appSvcDocDtoLit, String serviceId, StringBuilder sB,int uploadFileLimit,String sysFileType) {
        if (appSvcDocDtoLit != null) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtoLit) {
                Integer docSize = appSvcDocDto.getDocSize();
                String docName = appSvcDocDto.getDocName();
                Boolean flag = Boolean.FALSE;
                String substring = docName.substring(docName.lastIndexOf('.') + 1);
                if (docSize/1024 > uploadFileLimit) {
                    sB.append(serviceId);
                }

                String[] sysFileTypeArr = FileUtils.fileTypeToArray(sysFileType);
                for (String f : sysFileTypeArr) {
                    if (f.equalsIgnoreCase(substring)) {
                        flag = Boolean.TRUE;
                    }
                }
                if (!flag) {
                    sB.append(serviceId);
                }
            }


        }


    }

    private static void doAppSvcPersonnelDtoList(List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos, Map map, List<AppSvcPersonnelDto> appSvcPersonnelDtos, String serviceId, StringBuilder sB,String svcCode) {
        if (appSvcPersonnelDtos == null) {
            if (hcsaSvcPersonnelDtos != null) {
                for (HcsaSvcPersonnelDto every : hcsaSvcPersonnelDtos) {
                    String psnType = every.getPsnType();
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL.equals(psnType)) {
                        sB.append(serviceId);
                        return;
                    }
                }
            }

            return;
        }

        boolean flag = false;
        String errName = MessageUtil.replaceMessage("GENERAL_ERR0006","Name","field");
        String errDesignation = MessageUtil.replaceMessage("GENERAL_ERR0006","Designation","field");
        String errRegnNo = MessageUtil.replaceMessage("GENERAL_ERR0006","Professional Regn No.","field");
        String errWrkExpYear = MessageUtil.replaceMessage("GENERAL_ERR0006","Relevant working experience (Years)","field");
        String errQualification = MessageUtil.replaceMessage("GENERAL_ERR0006","Qualification","field");
        String errSelSvcPsnel = MessageUtil.replaceMessage("GENERAL_ERR0006","Select Service Personnel","field");
        for (int i = 0; i < appSvcPersonnelDtos.size(); i++) {
            if (AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode)) {
                String designation = appSvcPersonnelDtos.get(i).getDesignation();
                if (StringUtil.isEmpty(designation)) {
                    map.put("designation" + i, errDesignation);
                    flag = true;
                }
                String name = appSvcPersonnelDtos.get(i).getName();
                if (StringUtil.isEmpty(name)) {
                    map.put("name" + i, errName);
                    flag = true;
                }
                String profRegNo = appSvcPersonnelDtos.get(i).getProfRegNo();
                if (StringUtil.isEmpty(profRegNo)) {
                    map.put("regnNo" + i, errRegnNo);
                    flag = true;
                }
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                if (StringUtil.isEmpty(wrkExpYear)) {
                    map.put("wrkExpYear" + i, errWrkExpYear);
                    flag = true;
                } else {
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        map.put("wrkExpYear" + i, "GENERAL_ERR0002");
                        flag = true;
                    }
                }
            } else if (AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)) {
                String name = appSvcPersonnelDtos.get(i).getName();
                if (StringUtil.isEmpty(name)) {
                    map.put("name" + i, errName);
                    flag = true;
                }
                String quaification = appSvcPersonnelDtos.get(i).getQualification();
                if (StringUtil.isEmpty(quaification)) {
                    map.put("qualification" + i, errQualification);
                    flag = true;
                }
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                if (StringUtil.isEmpty(wrkExpYear)) {
                    map.put("wrkExpYear" + i, errWrkExpYear);
                    flag = true;
                } else {
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        map.put("wrkExpYear" + i, "GENERAL_ERR0002");
                        flag = true;
                    }
                }
            }else if(!AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)){
                String name = appSvcPersonnelDtos.get(i).getName();
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                String quaification = appSvcPersonnelDtos.get(i).getQualification();
                if (StringUtil.isEmpty(name)) {
                    map.put("name" + i, errName);
                    flag = true;
                }
                if (StringUtil.isEmpty(wrkExpYear)) {
                    map.put("wrkExpYear" + i, errWrkExpYear);
                    flag = true;
                } else {
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        map.put("wrkExpYear" + i, "GENERAL_ERR0002");
                        flag = true;
                    }
                }
                if (StringUtil.isEmpty(quaification)) {
                    map.put("quaification" + i, errQualification);
                    flag = true;
                }
            } else {
                String personnelSel = appSvcPersonnelDtos.get(i).getPersonnelType();
                if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE.equals(personnelSel)) {
                    String profRegNo = appSvcPersonnelDtos.get(i).getProfRegNo();
                    String name = appSvcPersonnelDtos.get(i).getName();
                    if (StringUtil.isEmpty(name)) {
                        map.put("name" + i, errName);
                        flag = true;
                    }
                    if (StringUtil.isEmpty(profRegNo)) {
                        map.put("regnNo" + i, errRegnNo);
                        flag = true;
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    String designation = appSvcPersonnelDtos.get(i).getDesignation();
                    String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                    String qualification = appSvcPersonnelDtos.get(i).getQualification();

                    if (StringUtil.isEmpty(name)) {
                        map.put("name" + i, errName);
                        flag = true;
                    }
                    if (StringUtil.isEmpty(designation)) {
                        map.put("designation" + i, errDesignation);
                        flag = true;
                    }
                    if (StringUtil.isEmpty(wrkExpYear)) {
                        map.put("wrkExpYear" + i, errWrkExpYear);
                        flag = true;
                    } else {
                        if (!wrkExpYear.matches("^[0-9]*$")) {
                            map.put("wrkExpYear" + i, "GENERAL_ERR0002");
                            flag = true;
                        }
                    }
                    if (StringUtil.isEmpty(qualification)) {
                        map.put("qualification" + i, errQualification);
                        flag = true;
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                    String quaification = appSvcPersonnelDtos.get(i).getQualification();
                    if (StringUtil.isEmpty(name)) {
                        map.put("name" + i, errName);
                        flag = true;
                    }
                    if (StringUtil.isEmpty(wrkExpYear)) {
                        map.put("wrkExpYear" + i, errWrkExpYear);
                        flag = true;
                    } else {
                        if (!wrkExpYear.matches("^[0-9]*$")) {
                            map.put("wrkExpYear" + i, "GENERAL_ERR0002");
                            flag = true;
                        }
                    }
                    if (StringUtil.isEmpty(quaification)) {
                        map.put("quaification" + i, errQualification);
                        flag = true;
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    if (StringUtil.isEmpty(name)) {
                        map.put("name" + i, errName);
                        flag = true;
                    }
                }
            }

            if (flag) {
                sB.append(serviceId);
            }
        }

    }

    private static void doAppSvcCgoDto(List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos, Map map, List<AppSvcCgoDto> list, String serviceId, StringBuilder sB) {
        if (list == null) {
            if (hcsaSvcPersonnelDtos != null) {
                for (HcsaSvcPersonnelDto every : hcsaSvcPersonnelDtos) {
                    String psnType = every.getPsnType();
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)) {
                        log.info("PERSONNEL_PSN_TYPE_CGO null");
                        sB.append(serviceId);
                        return;
                    }
                }
            }

            return;
        }


        boolean flag = false;
        for (int i = 0; i < list.size(); i++) {
            String assignSelect = list.get(i).getAssignSelect();
            if ("".equals(assignSelect) || assignSelect == null) {
                map.put("cgoassignSelect" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","cgoassignSelect","field"));
                flag = true;
            }
            String idType = list.get(i).getIdType();
            if (StringUtil.isEmpty(idType)) {
                map.put("cgotype" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","cgotype","field"));
                flag = true;
            }
            String mobileNo = list.get(i).getMobileNo();
            if (StringUtil.isEmpty(mobileNo)) {
                map.put("cgomobileNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","cgomobileNo","field"));
                flag = true;
            } else {
                if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                    map.put("cgomobileNo" + i, "GENERAL_ERR0007");
                    flag = true;
                }
            }
            String emailAddr = list.get(i).getEmailAddr();
            if (StringUtil.isEmpty(emailAddr)) {
                map.put("cgoemailAddr" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","cgoemailAddr","field"));
                flag = true;
            } else {
                if (!ValidationUtils.isEmail(emailAddr)) {
                    map.put("cgoemailAddr" + i, "GENERAL_ERR0014");
                    flag = true;
                }
            }
            if (flag) {
                sB.append(serviceId);

            }

        }
    }

    private static void dolabory(Map<String, String> map,List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList, List<AppSvcLaboratoryDisciplinesDto> list, String serviceId, StringBuilder sB) {
        if (list != null && list.isEmpty()) {

        }
        if(list!=null&&appSvcDisciplineAllocationDtoList!=null&&!list.isEmpty()){
            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : list){
                List<AppSvcChckListDto> appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                if(appSvcChckListDtoList!=null&&!appSvcChckListDtoList.isEmpty()){
                    for(AppSvcChckListDto appSvcChckListDto : appSvcChckListDtoList){
                        boolean flag=false;
                        for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : appSvcDisciplineAllocationDtoList){
                            if(appSvcChckListDto.getChkLstConfId().equals(appSvcDisciplineAllocationDto.getChkLstConfId())){
                                flag=true;
                            }
                        }
                        if(!flag){
                            map.put("allocation","allocation error");
                            sB.append(serviceId);
                        }
                    }
                }
            }

        }
    }

    private static void doSvcDis(Map map, List<AppSvcDisciplineAllocationDto> list, String serviceId, StringBuilder sB) {
        if (list == null) {
            return;
        } else {
            for (AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : list) {
                String idNo = appSvcDisciplineAllocationDto.getIdNo();
                if (StringUtil.isEmpty(idNo)) {
                    map.put("idNo", "idNo empty");
                    sB.append(serviceId);
                    return;
                }
            }
        }
    }

    private static void doSvcDisdolabory(Map map, List<AppSvcDisciplineAllocationDto> appSvcDislist, List<AppSvcLaboratoryDisciplinesDto> appSvclaborlist, String serviceId, StringBuilder sB) {
        if (appSvclaborlist == null || appSvclaborlist.isEmpty()) {
            return;
        } else if (appSvclaborlist != null && !appSvclaborlist.isEmpty()) {
            List<AppSvcChckListDto> appSvcChckListDtoList = IaisCommonUtils.genNewArrayList();
            for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvclaborlist) {
                appSvcChckListDtoList.addAll(appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList());
            }

            if (appSvcChckListDtoList != null) {
                if (appSvcDislist == null) {
                    map.put("appSvcDislist", "appSvcDislist null");
                    sB.append(serviceId);
                    return;
                } else {
                  /*  if( appSvcChckListDtoList.size()!=appSvcDislist.size()){
                        log.info(appSvcChckListDtoList.size()+" appSvcChckListDtoList ");
                        log.info(appSvcDislist.size()+" appSvcDislist ");
                        map.put("appSvcChckListDtoListsize","size");
                        sB.append(serviceId);
                        return;
                    }
*/
                }
            }
        }
    }

    private static void doPO(List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos, Map oneErrorMap, List<AppSvcPrincipalOfficersDto> poDto, String serviceId, StringBuilder sB) {
        if (poDto == null) {
            log.info("podto is null");
            if (hcsaSvcPersonnelDtos != null) {
                for (HcsaSvcPersonnelDto every : hcsaSvcPersonnelDtos) {
                    String psnType = every.getPsnType();
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)) {
                        sB.append(serviceId);
                        return;
                    }
                }
            }

            return;
        }


        boolean flag = false;
        int poIndex = 0;
        int dpoIndex = 0;
        List<String> stringList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < poDto.size(); i++) {
            String psnType = poDto.get(i).getPsnType();
            if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)) {

                StringBuilder stringBuilder = new StringBuilder(10);

                String assignSelect = poDto.get(i).getAssignSelect();
                if ("-1".equals(assignSelect)) {
                    oneErrorMap.put("assignSelect" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","assignSelect","field"));
                } else {
                    //do by wenkang
                    String mobileNo = poDto.get(i).getMobileNo();
                    String officeTelNo = poDto.get(i).getOfficeTelNo();
                    String emailAddr = poDto.get(i).getEmailAddr();
                    String idNo = poDto.get(i).getIdNo();
                    String name = poDto.get(i).getName();
                    String salutation = poDto.get(i).getSalutation();
                    String designation = poDto.get(i).getDesignation();
                    String idType = poDto.get(i).getIdType();

                    if ("-1".equals(idType)) {
                        oneErrorMap.put("idType" + poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","idType","field"));
                    }
                    if (StringUtil.isEmpty(name)) {
                        oneErrorMap.put("name" + poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","name","field"));
                    } else if (name.length() > 66) {

                    }
                    if (StringUtil.isEmpty(salutation)) {
                        oneErrorMap.put("salutation" + poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","salutation","field"));
                    }
                    if (StringUtil.isEmpty(designation)) {
                        oneErrorMap.put("designation" + poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","designation","field"));
                    }
                    if (!StringUtil.isEmpty(idNo)) {
                        if ("FIN".equals(idType)) {
                            boolean b = SgNoValidator.validateFin(idNo);
                            if (!b) {
                                oneErrorMap.put("NRICFIN", "GENERAL_ERR0008");
                            } else {
                                stringBuilder.append(idType).append(idNo);

                            }
                        }
                        if ("NRIC".equals(idType)) {
                            boolean b1 = SgNoValidator.validateNric(idNo);
                            if (!b1) {
                                oneErrorMap.put("NRICFIN", "GENERAL_ERR0008");
                            } else {
                                stringBuilder.append(idType).append(idNo);

                            }
                        }
                    } else {
                        oneErrorMap.put("NRICFIN", MessageUtil.replaceMessage("GENERAL_ERR0006","NRICFIN","field"));
                    }
                    if (!StringUtil.isEmpty(mobileNo)) {

                        if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                            oneErrorMap.put("mobileNo" + poIndex, "GENERAL_ERR0007");
                        }
                    } else {
                        oneErrorMap.put("mobileNo" + poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","mobileNo","field"));
                    }
                    if (!StringUtil.isEmpty(emailAddr)) {
                        if (!ValidationUtils.isEmail(emailAddr)) {
                            oneErrorMap.put("emailAddr" + poIndex, "GENERAL_ERR0014");
                        } else if (emailAddr.length() > 66) {

                        }
                    } else {
                        oneErrorMap.put("emailAddr" + poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","emailAddr","field"));
                    }
                    if (!StringUtil.isEmpty(officeTelNo)) {
                        if (!officeTelNo.matches("^[6][0-9]{7}$")) {
                            oneErrorMap.put("officeTelNo" + poIndex, "GENERAL_ERR0015");
                        }
                    } else {
                        oneErrorMap.put("officeTelNo" + poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","officeTelNo","field"));
                    }
                }
                poIndex++;
                String s = stringBuilder.toString();

                if (stringList.contains(s)) {

                    oneErrorMap.put("NRICFIN", "NEW_ERR0012");

                } else {
                    stringList.add(stringBuilder.toString());
                }
            }

            if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnType)) {
                StringBuilder stringBuilder = new StringBuilder(10);
                String salutation = poDto.get(i).getSalutation();
                String name = poDto.get(i).getName();
                String idType = poDto.get(i).getIdType();
                String mobileNo = poDto.get(i).getMobileNo();
                String emailAddr = poDto.get(i).getEmailAddr();
                String idNo = poDto.get(i).getIdNo();
                String designation = poDto.get(i).getDesignation();
                String officeTelNo = poDto.get(i).getOfficeTelNo();

                if (StringUtil.isEmpty(designation) || "-1".equals(designation)) {
                    oneErrorMap.put("deputyDesignation" + dpoIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","deputyDesignation","field"));
                }
                if (StringUtil.isEmpty(salutation) || "-1".equals(salutation)) {
                    oneErrorMap.put("deputySalutation" + dpoIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","deputySalutation","field"));
                }

                if (StringUtil.isEmpty(idType) || "-1".equals(idType)) {
                    oneErrorMap.put("deputyIdType" + dpoIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","deputyIdType","field"));
                }
                if (StringUtil.isEmpty(name)) {
                    oneErrorMap.put("deputyName" + dpoIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","deputyName","field"));
                } else if (name.length() > 66) {

                }
                if (StringUtil.isEmpty(officeTelNo)) {
                    oneErrorMap.put("deputyofficeTelNo" + dpoIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","deputyofficeTelNo","field"));
                } else {
                    if (!officeTelNo.matches("^[6][0-9]{7}$")) {
                        oneErrorMap.put("deputyofficeTelNo" + dpoIndex, "GENERAL_ERR0015");
                    }
                }
                if (StringUtil.isEmpty(idNo)) {
                    oneErrorMap.put("deputyIdNo" + dpoIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","deputyIdNo","field"));
                }
                if ("FIN".equals(idType)) {
                    boolean b = SgNoValidator.validateFin(idNo);
                    if (!b) {
                        oneErrorMap.put("deputyIdNo" + dpoIndex, "GENERAL_ERR0008");
                    } else {
                        stringBuilder.append(idType).append(idNo);
                    }
                }
                if ("NRIC".equals(idType)) {
                    boolean b1 = SgNoValidator.validateNric(idNo);
                    if (!b1) {
                        oneErrorMap.put("deputyIdNo" + dpoIndex, "GENERAL_ERR0008");
                    } else {
                        stringBuilder.append(idType).append(idNo);
                    }
                }

                if (StringUtil.isEmpty(mobileNo)) {
                    oneErrorMap.put("deputyMobileNo" + dpoIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","deputyMobileNo","field"));
                } else {
                    if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                        oneErrorMap.put("deputyMobileNo" + dpoIndex, "GENERAL_ERR0007");
                    }
                }
                if (StringUtil.isEmpty(emailAddr)) {
                    oneErrorMap.put("deputyEmailAddr" + dpoIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","deputyEmailAddr","field"));
                } else {
                    if (!ValidationUtils.isEmail(emailAddr)) {
                        oneErrorMap.put("deputyEmailAddr" + dpoIndex, "GENERAL_ERR0014");
                    } else if (emailAddr.length() > 66) {

                    }
                }
                dpoIndex++;

                String s = stringBuilder.toString();

                if (stringList.contains(s) && !StringUtil.isEmpty(s)) {

                    oneErrorMap.put("NRICFIN", "NEW_ERR0012");

                } else {
                    stringList.add(stringBuilder.toString());
                }
            }


        }
        if (flag) {
            sB.append(serviceId);
        }
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(oneErrorMap) + "oneErrorMap"));
    }

    private void doCommomDocument(HttpServletRequest request, Map<String, String> documentMap) {
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);

        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = appSubmissionDto.getAppGrpPrimaryDocDtos();
        List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigList = (List<HcsaSvcDocConfigDto>) request.getSession().getAttribute(NewApplicationDelegator.COMMONHCSASVCDOCCONFIGDTO);
        if (commonHcsaSvcDocConfigList == null) {
            List<HcsaSvcDocConfigDto> hcsaSvcDocDtos;
            boolean isRfi = NewApplicationHelper.checkIsRfi(request);
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
            if(isRfi && appGrpPrimaryDocDtos != null && appGrpPrimaryDocDtos.size() > 0){
                hcsaSvcDocDtos = serviceConfigService.getPrimaryDocConfigById(appGrpPrimaryDocDtos.get(0).getSvcComDocId());
            }else{
                hcsaSvcDocDtos = serviceConfigService.getAllHcsaSvcDocs(null);
            }
            if (hcsaSvcDocDtos != null) {
                List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigDto = IaisCommonUtils.genNewArrayList();
                for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : hcsaSvcDocDtos) {
                    if ("0".equals(hcsaSvcDocConfigDto.getDupForPrem())) {
                        commonHcsaSvcDocConfigDto.add(hcsaSvcDocConfigDto);
                    }
                }
                commonHcsaSvcDocConfigList = commonHcsaSvcDocConfigDto;
            } else {
                return;
            }
        }
        for (HcsaSvcDocConfigDto comm : commonHcsaSvcDocConfigList) {
            String name = "common" + comm.getId();

            Boolean isMandatory = comm.getIsMandatory();
            String err006 = MessageUtil.replaceMessage("GENERAL_ERR0006", "Document", "field");
            if (isMandatory && appGrpPrimaryDocDtoList == null || isMandatory && appGrpPrimaryDocDtoList.isEmpty()) {
                documentMap.put(name, err006);
            } else if (isMandatory && !appGrpPrimaryDocDtoList.isEmpty()) {
                Boolean flag = Boolean.FALSE;
                for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtoList) {
                    if(StringUtil.isEmpty(appGrpPrimaryDocDto.getMd5Code())){
                        continue;
                    }
                    String svcComDocId = appGrpPrimaryDocDto.getSvcComDocId();
                    if (comm.getId().equals(svcComDocId)) {
                        flag = Boolean.TRUE;
                        break;
                    }
                }
                if (!flag) {
                    documentMap.put(name, err006);
                }
            }
        }

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

    private void handlerDupPremDoc(String dupPrem,AppGrpPrimaryDocDto targetDto,List<AppGrpPremisesDto> appGrpPremisesDtos,List<AppGrpPrimaryDocDto> newPrimaryDocList){
        if(AppConsts.NO.equals(dupPrem)){
            newPrimaryDocList.add(targetDto);
        }else if(AppConsts.YES.equals(dupPrem)){
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                targetDto.setPremisessName(appGrpPremisesDto.getPremisesIndexNo());
                targetDto.setPremisessType(appGrpPremisesDto.getPremisesType());
                newPrimaryDocList.add(targetDto);
            }
        }

    }

    private static AppSubmissionDto getAppSubmissionDto(HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, NewApplicationDelegator.APPSUBMISSIONDTO);
        if (appSubmissionDto == null) {
            log.info(StringUtil.changeForLog("appSubmissionDto is empty "));
            appSubmissionDto = new AppSubmissionDto();
        }
        return appSubmissionDto;
    }

}