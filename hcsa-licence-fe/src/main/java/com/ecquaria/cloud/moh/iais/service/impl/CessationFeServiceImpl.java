package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessatonConfirmDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppSpecifiedLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.CessationFeService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.sz.commons.util.DateUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/2/7 13:17
 */
@Service
@Slf4j
public class CessationFeServiceImpl implements CessationFeService {

    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private CessationClient cessationClient;
    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private ApplicationClient applicationClient;
    @Value("${iais.email.sender}")
    private String mailSender;
    @Autowired
    private AppConfigClient appConfigClient;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private OrganizationLienceseeClient organizationLienceseeClient;
    @Autowired
    AppSubmissionService appSubmissionService;
    @Autowired
    MsgTemplateClient msgTemplateClient;
    @Autowired
    private FeEicGatewayClient feEicGatewayClient;
    private LicEicClient licEicClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;



    private final static String FURTHERDATECESSATION = "4FAD8B3B-E652-EA11-BE7F-000C29F371DC";
    private final static String PRESENTDATECESSATION = "50AD8B3B-E652-EA11-BE7F-000C29F371DC";

    @Override
    public List<AppCessLicDto> getAppCessDtosByLicIds(List<String> licIds) {
        List<AppCessLicDto> appCessLicDtos = IaisCommonUtils.genNewArrayList();
        if (licIds != null && !licIds.isEmpty()) {
            for (String licId : licIds) {
                AppCessLicDto appCessDto = new AppCessLicDto();
                LicenceDto licenceDto = licenceClient.getLicDtoById(licId).getEntity();
                List<PremisesDto> premisesDtos = licenceClient.getPremisesDto(licId).getEntity();
                String svcName = licenceDto.getSvcName();
                String licenceNo = licenceDto.getLicenceNo();
                appCessDto.setLicenceNo(licenceNo);
                appCessDto.setSvcName(svcName);
                appCessDto.setLicenceId(licId);
                List<AppCessHciDto> appCessHciDtos = IaisCommonUtils.genNewArrayList();
                if (premisesDtos != null && !premisesDtos.isEmpty()) {
                    for (PremisesDto premisesDto : premisesDtos) {
                        String blkNo = premisesDto.getBlkNo();
                        String premisesId = premisesDto.getId();
                        String streetName = premisesDto.getStreetName();
                        String buildingName = premisesDto.getBuildingName();
                        String floorNo = premisesDto.getFloorNo();
                        String unitNo = premisesDto.getUnitNo();
                        String postalCode = premisesDto.getPostalCode();
                        String hciAddress = MiscUtil.getAddress(blkNo, streetName, buildingName, floorNo, unitNo, postalCode);
                        AppCessHciDto appCessHciDto = new AppCessHciDto();
                        String hciName = premisesDto.getHciName();
                        String hciCode = premisesDto.getHciCode();
                        appCessHciDto.setHciCode(hciCode);
                        appCessHciDto.setHciName(hciName);
                        appCessHciDto.setPremiseId(premisesId);
                        appCessHciDto.setHciAddress(hciAddress);
                        appCessHciDtos.add(appCessHciDto);
                    }
                }
                appCessDto.setAppCessHciDtos(appCessHciDtos);
                appCessLicDtos.add(appCessDto);
            }
            return appCessLicDtos;
        } else {
            return null;
        }
    }

    @Override
    public void updateLicenceFe(List<String> licNos) {
        List<LicenceDto> licenceDtos = licenceClient.getLicDtosByLicNos(licNos).getEntity();
        List<String> licIds = IaisCommonUtils.genNewArrayList();
        if (licenceDtos != null && !licenceDtos.isEmpty()) {
            for (LicenceDto licenceDto : licenceDtos) {
                String licId = licenceDto.getId();
                licIds.add(licId);
            }
        } else {
            licenceDtos = IaisCommonUtils.genNewArrayList();
        }
        List<String> specLicIds = licenceClient.getSpecLicIdsByLicIds(licIds).getEntity();
        if (!IaisCommonUtils.isEmpty(specLicIds) && !IaisCommonUtils.isEmpty(licenceDtos)) {
            for (String specId : specLicIds) {
                LicenceDto entity = licenceClient.getLicBylicId(specId).getEntity();
                licenceDtos.add(entity);
            }
        }
        if (licenceDtos != null && !licenceDtos.isEmpty()) {
            for (LicenceDto licenceDto : licenceDtos) {
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
                licenceClient.doUpdate(licenceDto);
            }
        }
    }

    @Override
    public Map<String, String> saveCessations(List<AppCessationDto> appCessationDtos, LoginContext loginContext) {
        String licenseeId = loginContext.getLicenseeId();
        List<AppCessMiscDto> appCessMiscDtos = IaisCommonUtils.genNewArrayList();
        List<String> appIds = IaisCommonUtils.genNewArrayList();
        Map<String, List<String>> licPremiseIdMap = IaisCommonUtils.genNewHashMap();
        for (AppCessationDto appCessationDto : appCessationDtos) {
            String licId = appCessationDto.getLicId();
            String premiseId = appCessationDto.getPremiseId();
            List<String> premiseIds = licPremiseIdMap.get(licId);
            if (premiseIds == null) {
                List<String> premisesIds = IaisCommonUtils.genNewArrayList();
                premisesIds.add(premiseId);
                licPremiseIdMap.put(licId, premisesIds);
            } else {
                premiseIds.add(premiseId);
            }
        }
        Map<String, String> appIdPremisesMap = IaisCommonUtils.genNewHashMap();
        licPremiseIdMap.forEach((licId, premiseIds) -> {
            List<String> licIds = IaisCommonUtils.genNewArrayList();
            licIds.clear();
            licIds.add(licId);
            AppSubmissionDto appSubmissionDto = licenceClient.getAppSubmissionDtos(licIds).getEntity().get(0);
            Map<String, String> transform = transform(appSubmissionDto, licenseeId, premiseIds);
            appIdPremisesMap.putAll(transform);
        });
        appIdPremisesMap.forEach((premiseId, appId) -> {
            for (AppCessationDto appCessationDto : appCessationDtos) {
                String premiseId1 = appCessationDto.getPremiseId();
                if (premiseId.equals(premiseId1)) {
                    AppCessMiscDto appCessMiscDto = setMiscData(appCessationDto, appId);
                    appCessMiscDtos.add(appCessMiscDto);
                    appIds.add(appId);
                }
            }
        });
        cessationClient.saveCessation(appCessMiscDtos).getEntity();
        return appIdPremisesMap;
    }


    @Override
    public List<String> listHciName() {
        List<String> hciNames = cessationClient.listHciNames().getEntity();
        return hciNames;
    }

    @Override
    public List<AppSpecifiedLicDto> getSpecLicInfo(List<String> licIds) {
        List<AppSpecifiedLicDto> appSpecifiedLicDtos = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isEmpty(licIds)) {
            return appSpecifiedLicDtos;
        }
        for (String licId : licIds) {
            LicenceDto licenceDto = licenceClient.getLicBylicId(licId).getEntity();
            String svcName = licenceDto.getSvcName();
            String licenceNo = licenceDto.getLicenceNo();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(svcName);
            String svcType = hcsaServiceDto.getSvcType();
            if (ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(svcType)) {
                List<String> specLicIds = licenceClient.getSpecIdsByBaseId(licId).getEntity();
                if (!IaisCommonUtils.isEmpty(specLicIds)) {
                    for (String specLicId : specLicIds) {
                        AppSpecifiedLicDto appSpecifiedLicDto = new AppSpecifiedLicDto();
                        LicenceDto specLicenceDto = licenceClient.getLicBylicId(specLicId).getEntity();
                        if (specLicenceDto != null) {
                            String specLicenceNo = specLicenceDto.getLicenceNo();
                            String specSvcName = specLicenceDto.getSvcName();
                            appSpecifiedLicDto.setBaseLicNo(licenceNo);
                            appSpecifiedLicDto.setBaseSvcName(svcName);
                            appSpecifiedLicDto.setSpecLicNo(specLicenceNo);
                            appSpecifiedLicDto.setSpecSvcName(specSvcName);
                            appSpecifiedLicDtos.add(appSpecifiedLicDto);
                        }
                    }
                }
            }
        }
        return appSpecifiedLicDtos;
    }

    @Override
    public List<AppCessatonConfirmDto> getConfirmDto(List<AppCessationDto> appCessationDtos, Map<String, String> appIdPremisesMap, LoginContext loginContext) throws ParseException {
        List<AppCessatonConfirmDto> appCessationDtosConfirms = IaisCommonUtils.genNewArrayList();
        List<String> licIds = IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < appCessationDtos.size(); i++) {
            AppCessationDto appCessationDto = appCessationDtos.get(i);
            String premiseId = appCessationDto.getPremiseId();
            String licId = appCessationDto.getLicId();
            LicenceDto licenceDto = licenceClient.getLicBylicId(licId).getEntity();
            String licenseeId = licenceDto.getLicenseeId();
            licIds.clear();
            licIds.add(licId);
            String appId = appIdPremisesMap.get(premiseId);
            ApplicationDto applicationDto = applicationClient.getApplicationById(appId).getEntity();
            applicationDtos.add(applicationDto);
            String applicationNo = applicationDto.getApplicationNo();
            List<AppCessLicDto> appCessDtosByLicIds = getAppCessDtosByLicIds(licIds);
            AppCessLicDto appCessLicDto = appCessDtosByLicIds.get(0);
            String licenceNo = appCessLicDto.getLicenceNo();
            String svcName = appCessLicDto.getSvcName();
            AppGrpPremisesDto appGrpPremisesDto = cessationClient.getAppGrpPremisesDtoByAppId(appId).getEntity();
            String hciCode = appGrpPremisesDto.getHciCode();
            String hciName = null;
            String hciAddress = null;
            List<AppCessHciDto> appCessHciDtos = appCessLicDto.getAppCessHciDtos();
            if (!IaisCommonUtils.isEmpty(appCessHciDtos)) {
                for (AppCessHciDto appCessHciDto : appCessHciDtos) {
                    String hciCode1 = appCessHciDto.getHciCode();
                    if (hciCode.equals(hciCode1)) {
                        hciName = appCessHciDto.getHciName();
                        hciAddress = appCessHciDto.getHciAddress();
                    }
                }
            }
            Date effectiveDate = appCessationDto.getEffectiveDate();
            try {
                if (effectiveDate.after(new Date())) {
                    String loginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_INBOX;
                    Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                    LicenseeDto licenseeDto = organizationLienceseeClient.getLicenseeById(licenseeId).getEntity();
                    String applicantName = licenseeDto.getName();
                    emailMap.put("ApplicantName", applicantName);
                    emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
                    emailMap.put("ApplicationNumber", applicationNo);
                    emailMap.put("ServiceLicenceName", svcName);
                    emailMap.put("CessationDate", Formatter.formatDateTime(effectiveDate));
                    emailMap.put("ApplicationDate", Formatter.formatDateTime(new Date()));
                    emailMap.put("email", systemParamConfig.getSystemAddressOne());
                    emailMap.put("systemLink", loginUrl);
                    emailMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
                    MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_CEASE_FUTURE_DATE).getEntity();
                    Map<String,Object> map=IaisCommonUtils.genNewHashMap();
                    map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
                    map.put("ApplicationNumber", applicationNo);
                    String subject= MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),map);
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_CEASE_FUTURE_DATE);
                    emailParam.setTemplateContent(emailMap);
                    emailParam.setQueryCode(applicationNo);
                    emailParam.setReqRefNum(applicationNo);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                    emailParam.setRefId(applicationNo);
                    emailParam.setSubject(subject);
                    //email
                    notificationHelper.sendNotification(emailParam);
                    //msg
                    HcsaServiceDto svcDto = appConfigClient.getHcsaServiceDtoByServiceId(applicationDto.getServiceId()).getEntity();
                    List<String> svcCode=IaisCommonUtils.genNewArrayList();
                    svcCode.add(svcDto.getSvcCode());
                    emailParam.setSvcCodeList(svcCode);
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_CEASE_FUTURE_DATE_MSG);
                    emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    notificationHelper.sendNotification(emailParam);
                    //sms
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_CEASE_FUTURE_DATE_SMS);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                    notificationHelper.sendNotification(emailParam);
                    //sendEmail(FURTHERDATECESSATION, effectiveDate, svcName, licId, licenseeId, licenceNo);
                } else {
                    Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                    LicenseeDto licenseeDto = organizationLienceseeClient.getLicenseeById(licenseeId).getEntity();
                    String applicantName = licenseeDto.getName();
                    emailMap.put("ApplicantName", applicantName);
                    emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
                    emailMap.put("ServiceLicenceName", svcName);
                    emailMap.put("ApplicationNumber", applicationNo);
                    emailMap.put("CessationDate", Formatter.formatDateTime(effectiveDate));
                    emailMap.put("email", systemParamConfig.getSystemAddressOne());
                    emailMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
                    emailMap.put("MOH_AGENCY_NAM_GROUP",AppConsts.MOH_AGENCY_NAM_GROUP);
                    MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_CEASE_PRESENT_DATE).getEntity();
                    Map<String,Object> map=IaisCommonUtils.genNewHashMap();
                    map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
                    map.put("ApplicationNumber", applicationNo);
                    String subject= MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),map);
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_CEASE_PRESENT_DATE);
                    emailParam.setTemplateContent(emailMap);
                    emailParam.setQueryCode(applicationNo);
                    emailParam.setReqRefNum(applicationNo);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                    emailParam.setRefId(applicationNo);
                    emailParam.setSubject(subject);
                    //email
                    notificationHelper.sendNotification(emailParam);
                    //msg
                    HcsaServiceDto svcDto = appConfigClient.getHcsaServiceDtoByServiceId(applicationDto.getServiceId()).getEntity();
                    List<String> svcCode=IaisCommonUtils.genNewArrayList();
                    svcCode.add(svcDto.getSvcCode());
                    emailParam.setSvcCodeList(svcCode);
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_CEASE_PRESENT_DATE_MSG);
                    emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    notificationHelper.sendNotification(emailParam);
                    //sms
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_CEASE_PRESENT_DATE_SMS);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                    notificationHelper.sendNotification(emailParam);
                    //sendEmail(PRESENTDATECESSATION, effectiveDate, svcName, licId, licenseeId, licenceNo);
                }
            } catch (Exception e) {
                log.info(StringUtil.changeForLog("==================== email error ===============>>>>>>>" + e.getMessage()));
            }
            AppCessatonConfirmDto appCessatonConfirmDto = new AppCessatonConfirmDto();
            appCessatonConfirmDto.setAppNo(applicationNo);
            appCessatonConfirmDto.setEffectiveDate(effectiveDate);
            appCessatonConfirmDto.setHciAddress(hciAddress);
            appCessatonConfirmDto.setSvcName(svcName);
            appCessatonConfirmDto.setLicenceNo(licenceNo);
            appCessatonConfirmDto.setHciName(hciName);
            appCessationDtosConfirms.add(appCessatonConfirmDto);
        }
        //update apps
        Date today = new Date();
        String todayStr = DateUtil.formatDate(today);
        Date date2 = DateUtil.parseDate(todayStr);
        for(ApplicationDto applicationDto : applicationDtos){
            String appId = applicationDto.getId();
            String serviceId = applicationDto.getServiceId();
            boolean configService = isConfigService(serviceId, ApplicationConsts.APPLICATION_TYPE_CESSATION);
            if(configService){
                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
            }
            List<AppPremiseMiscDto> appPremiseMiscDtos = cessationClient.getAppPremiseMiscDtoListByAppId(appId).getEntity();
            if(!IaisCommonUtils.isEmpty(appPremiseMiscDtos)){
                Date effectiveDate = appPremiseMiscDtos.get(0).getEffectiveDate();
                String effectiveDateStr = DateUtil.formatDate(effectiveDate);
                Date date1 = DateUtil.parseDate(effectiveDateStr);
                if(date1.after(date2)){
                    applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_TEMPORARY_LICENCE);
                }
            }
        }
        applicationClient.updateApplicationList(applicationDtos);
        List<String> licNos = IaisCommonUtils.genNewArrayList();
        for (AppCessatonConfirmDto appCessatonConfirmDto : appCessationDtosConfirms) {
            String licenceNo = appCessatonConfirmDto.getLicenceNo();
            Date effectiveDate = appCessatonConfirmDto.getEffectiveDate();
            if (effectiveDate.before(new Date())) {
                licNos.add(licenceNo);
            }
        }
        if (!licNos.isEmpty()) {
            try {
                updateLicenceFe(licNos);
            } catch (Exception e) {
                log.info(StringUtil.changeForLog("====================eic error================="));
            }
        }
        //sort by appNo
        Collections.sort(appCessationDtosConfirms,(s1,s2)->(s1.getAppNo().compareTo(s2.getAppNo())));
        return appCessationDtosConfirms;
    }

    @Override
    public boolean isGrpLicence(List<String> licIds) {
        LicenceDto entity = licenceClient.getLicBylicId(licIds.get(0)).getEntity();
        return entity.isGrpLic();
    }

    @Override
    public boolean isConfigService(String serviceId, String appType) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        List<HcsaSvcRoutingStageDto> serviceConfig = feEicGatewayClient.getServiceConfig(serviceId, appType, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
        if(IaisCommonUtils.isEmpty(serviceConfig)){
            return false;
        }else {
            return true;
        }
    }


    /*
    utils
     */
    private Map<String, String> transform(AppSubmissionDto appSubmissionDto, String licenseeId, List<String> premiseIds) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        Double amount = 0.0;
        AuditTrailDto internet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTERNET);
        String grpNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_CESSATION).getEntity();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        String serviceName = appSvcRelatedInfoDtoList.get(0).getServiceName();
        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
        String svcId = hcsaServiceDto.getId();
        String svcCode = hcsaServiceDto.getSvcCode();
        appSvcRelatedInfoDtoList.get(0).setServiceId(svcId);
        appSvcRelatedInfoDtoList.get(0).setServiceCode(svcCode);
        appSubmissionDto.setAppGrpNo(grpNo);
        appSubmissionDto.setFromBe(false);
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_CESSATION);
        appSubmissionDto.setAmount(amount);
        appSubmissionDto.setAuditTrailDto(internet);
        appSubmissionDto.setPreInspection(true);
        appSubmissionDto.setRequirement(true);
        appSubmissionDto.setLicenseeId(licenseeId);
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
        setRiskToDto(appSubmissionDto);

        AppSubmissionDto entity = applicationClient.saveSubmision(appSubmissionDto).getEntity();
        AppSubmissionDto appSubmissionDtoSave = applicationClient.saveApps(entity).getEntity();
        List<ApplicationDto> applicationDtos = appSubmissionDtoSave.getApplicationDtos();
        List<String> hciCodes = IaisCommonUtils.genNewArrayList();
        for (String premiseId : premiseIds) {
            PremisesDto entity1 = licenceClient.getLicPremisesDtoById(premiseId).getEntity();
            String hciCode = entity1.getHciCode();
            hciCodes.add(hciCode);
        }
        for (ApplicationDto applicationDto : applicationDtos) {
            String id = applicationDto.getId();
            AppGrpPremisesDto dto = cessationClient.getAppGrpPremisesDtoByAppId(id).getEntity();
            String hciCode = dto.getHciCode();
            if (hciCodes.contains(hciCode)) {
                applicationDto.setNeedNewLicNo(false);
                applicationDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_TRANSFER);
                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_NOT_LICENCE);
            } else {
                applicationDto.setNeedNewLicNo(true);
                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE);
                applicationDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_ORIGIN);
            }
            applicationClient.updateApplicationDto(applicationDto);
        }

        for (ApplicationDto applicationDto : applicationDtos) {
            String id = applicationDto.getId();
            AppGrpPremisesDto dto = cessationClient.getAppGrpPremisesDtoByAppId(id).getEntity();
            String hciCode = dto.getHciCode();
            for (String premiseId : premiseIds) {
                PremisesDto entity1 = licenceClient.getLicPremisesDtoById(premiseId).getEntity();
                String hciCode1 = entity1.getHciCode();
                if (hciCode1.equals(hciCode)) {
                    String appId = id;
                    map.put(premiseId, appId);
                }
            }
        }
        return map;
    }

    private AppCessMiscDto setMiscData(AppCessationDto appCessationDto, String appId) {
        Date effectiveDate = appCessationDto.getEffectiveDate();
        String reason = appCessationDto.getReason();
        String otherReason = appCessationDto.getOtherReason();
        Boolean patNeedTrans = appCessationDto.getPatNeedTrans();
        String patientSelect = appCessationDto.getPatientSelect();
        String patHciName = appCessationDto.getPatHciName();
        String patRegNo = appCessationDto.getPatRegNo();
        String patOthers = appCessationDto.getPatOthers();
        String patNoRemarks = appCessationDto.getPatNoRemarks();
        AppCessMiscDto appCessMiscDto = new AppCessMiscDto();
        appCessMiscDto.setAppealType(ApplicationConsts.CESSATION_TYPE_APPLICATION);
        appCessMiscDto.setEffectiveDate(effectiveDate);
        appCessMiscDto.setReason(reason);
        appCessMiscDto.setOtherReason(otherReason);
        appCessMiscDto.setPatNeedTrans(patNeedTrans);
        appCessMiscDto.setPatNoReason(patNoRemarks);
        appCessMiscDto.setPatTransType(patientSelect);
        appCessMiscDto.setAppId(appId);
        if (!StringUtil.isEmpty(patHciName)) {
            appCessMiscDto.setPatTransTo(patHciName);
        }
        if (!StringUtil.isEmpty(patRegNo)) {
            appCessMiscDto.setPatTransTo(patRegNo);
        }
        if (!StringUtil.isEmpty(patOthers)) {
            appCessMiscDto.setPatTransTo(patOthers);
        }
        return appCessMiscDto;
    }

    private void setRiskToDto(AppSubmissionDto appSubmissionDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = IaisCommonUtils.genNewArrayList();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setApptype(appSubmissionDto.getAppType());
            riskAcceptiionDtoList.add(riskAcceptiionDto);
        }
        List<RiskResultDto> riskResultDtoList = appConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            String serviceCode = appSvcRelatedInfoDto.getServiceCode();
            RiskResultDto riskResultDto = getRiskResultDtoByServiceCode(riskResultDtoList, serviceCode);
            if (riskResultDto != null) {
                appSvcRelatedInfoDto.setScore(riskResultDto.getScore());
                appSvcRelatedInfoDto.setDoRiskDate(riskResultDto.getDoRiskDate());
            }
        }
    }

    private RiskResultDto getRiskResultDtoByServiceCode(List<RiskResultDto> riskResultDtoList, String serviceCode) {
        RiskResultDto result = null;
        if (riskResultDtoList == null || StringUtil.isEmpty(serviceCode)) {
            return null;
        }
        for (RiskResultDto riskResultDto : riskResultDtoList) {
            if (serviceCode.equals(riskResultDto.getSvcCode())) {
                result = riskResultDto;
                break;
            }
        }
        return result;
    }
}
