package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.CessationFeService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.sz.commons.util.DateUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/2/7 13:17
 */
@Service
@Slf4j
public class CessationFeServiceImpl implements CessationFeService {

    private final static String SERVICE_LICENCE_NAME = "ServiceLicenceName";
    private final static String CESSATION_DATE = "CessationDate";
    private final static String APPLICATION_DATE = "ApplicationDate";

    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private CessationClient cessationClient;
    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private ApplicationFeClient applicationFeClient;
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
    @Autowired
    private Environment env;


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
    public List<String> filtrateSpecLicIds(List<String> licIds) {
        List<String> specLicIds = IaisCommonUtils.genNewArrayList();
        List<String> specLicIdsE = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isEmpty(licIds)) {
            return specLicIds;
        }
        for (String licId : licIds) {
            LicenceDto licenceDto = licenceClient.getLicBylicId(licId).getEntity();
            String svcName = licenceDto.getSvcName();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(svcName);
            String svcType = hcsaServiceDto.getSvcType();
            if (ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(svcType)) {
                List<String> specIds = licenceClient.getSpecIdsByBaseId(licId).getEntity();
                if (!IaisCommonUtils.isEmpty(specIds)) {
                    for (String specLicId : specIds) {
                        LicenceDto specLicenceDto = licenceClient.getLicBylicId(specLicId).getEntity();
                        if (specLicenceDto != null) {
                            String licenceDtoId = specLicenceDto.getId();
                            specLicIds.add(licenceDtoId);
                        }
                    }
                }
            }
        }
        if (!IaisCommonUtils.isEmpty(specLicIds)) {
            for (String specId : specLicIds) {
                if (licIds.contains(specId)) {
                    specLicIdsE.add(specId);
                }
            }
        }
        return specLicIdsE;
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
            licIds.add(licId);
            List<String> specLicIds = licenceClient.getSpecLicIdsByLicIds(licIds).getEntity();
            if (!IaisCommonUtils.isEmpty(specLicIds)) {
                AppSubmissionDto appSubmissionDtoSpec = licenceClient.getAppSubmissionDtos(specLicIds).getEntity().get(0);
                transformSpec(appSubmissionDtoSpec, licenseeId, premiseIds);
            }
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
    public void saveRfiCessations(List<AppCessationDto> appCessationDtos, LoginContext loginContext, String rfiAppId) throws Exception {
        String licenseeId = loginContext.getLicenseeId();
        List<AppCessMiscDto> appCessMiscDtos = IaisCommonUtils.genNewArrayList();
        ApplicationDto applicationDto = applicationFeClient.getApplicationById(rfiAppId).getEntity();
        String originLicenceId = applicationDto.getOriginLicenceId();
        List<String> licIds = IaisCommonUtils.genNewArrayList();
        licIds.add(originLicenceId);
        AppSubmissionDto appSubmissionDto = applicationFeClient.getAppSubmissionDtoByAppNo(applicationDto.getApplicationNo()).getEntity();
        String appId = transformRfi(appSubmissionDto, licenseeId, applicationDto);
        AppCessMiscDto appCessMiscDto = setMiscData(appCessationDtos.get(0), appId);
        appCessMiscDtos.add(appCessMiscDto);
        cessationClient.saveCessation(appCessMiscDtos).getEntity();
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
                            String licenceDtoId = specLicenceDto.getId();
                            appSpecifiedLicDto.setBaseLicNo(licenceNo);
                            appSpecifiedLicDto.setBaseSvcName(svcName);
                            appSpecifiedLicDto.setSpecLicNo(specLicenceNo);
                            appSpecifiedLicDto.setSpecSvcName(specSvcName);
                            appSpecifiedLicDto.setSpecLicId(licenceDtoId);
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
        List<String> serviceCodes = IaisCommonUtils.genNewArrayList();
        AuditTrailDto currentAuditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        for (int i = 0; i < appCessationDtos.size(); i++) {
            AppCessationDto appCessationDto = appCessationDtos.get(i);
            String premiseId = appCessationDto.getPremiseId();
            String licId = appCessationDto.getLicId();
            List<String> specLicIds = licenceClient.getSpecIdsByBaseId(licId).getEntity();
            LicenceDto licenceDto = licenceClient.getLicBylicId(licId).getEntity();
            String licenseeId = licenceDto.getLicenseeId();
            licIds.clear();
            licIds.add(licId);
            String appId = appIdPremisesMap.get(premiseId);
            ApplicationDto applicationDto = applicationFeClient.getApplicationById(appId).getEntity();
            applicationDto.setAuditTrailDto(currentAuditTrailDto);
            applicationDtos.add(applicationDto);
            String applicationNo = applicationDto.getApplicationNo();
            List<AppCessLicDto> appCessDtosByLicIds = getAppCessDtosByLicIds(licIds);
            AppCessLicDto appCessLicDto = appCessDtosByLicIds.get(0);
            String licenceNo = licenceDto.getLicenceNo();
            String svcName = appCessLicDto.getSvcName();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(svcName);
            serviceCodes.add(hcsaServiceDto.getSvcCode());
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
                    String applicantName = loginContext.getUserName();
                    emailMap.put("ApplicantName", applicantName);
                    emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
                    emailMap.put("ApplicationNumber", applicationNo);
                    StringBuilder svcNameLicNo = new StringBuilder();
                    svcNameLicNo.append(svcName).append(" : ").append(licenceNo);
                    if(!IaisCommonUtils.isEmpty(specLicIds)){
                       for(String specLicId :specLicIds){
                           LicenceDto specLicDto = licenceClient.getLicBylicId(specLicId).getEntity();
                           String svcName1 = specLicDto.getSvcName();
                           String licenceNo1 = specLicDto.getLicenceNo();
                           svcNameLicNo.append(svcName1).append(" : ").append(licenceNo1);
                           HcsaServiceDto hcsaServiceDto1 = HcsaServiceCacheHelper.getServiceByServiceName(svcName1);
                           serviceCodes.add(hcsaServiceDto1.getSvcCode());
                       }
                    }
                    emailMap.put(SERVICE_LICENCE_NAME, svcNameLicNo.toString());
                    emailMap.put(CESSATION_DATE, DateFormatUtils.format(effectiveDate,"dd/MM/yyyy"));
                    emailMap.put(APPLICATION_DATE, DateFormatUtils.format(new Date(),"dd/MM/yyyy"));
                    emailMap.put("email", systemParamConfig.getSystemAddressOne());
                    emailMap.put("systemLink", loginUrl);
                    emailMap.put("MOH_AGENCY_NAM_GROUP", "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "</b>");
                    emailMap.put("MOH_AGENCY_NAME", "<b>" + AppConsts.MOH_AGENCY_NAME + "</b>");
                    MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_CEASE_FUTURE_DATE).getEntity();
                    Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                    map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
                    map.put("ApplicationNumber", applicationNo);
                    String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map);
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
                    emailParam.setSvcCodeList(serviceCodes);
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_CEASE_FUTURE_DATE_MSG);
                    emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    notificationHelper.sendNotification(emailParam);
                    //sms
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_CEASE_FUTURE_DATE_SMS);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                    notificationHelper.sendNotification(emailParam);

                } else {
                    Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                    String applicantName = loginContext.getUserName();
                    emailMap.put("ApplicantName", applicantName);
                    emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
                    StringBuilder svcNameLicNo = new StringBuilder();
                    svcNameLicNo.append(svcName).append(" : ").append(licenceNo);
                    if(!IaisCommonUtils.isEmpty(specLicIds)){
                        for(String specLicId :specLicIds){
                            LicenceDto specLicDto = licenceClient.getLicBylicId(specLicId).getEntity();
                            String svcName1 = specLicDto.getSvcName();
                            String licenceNo1 = specLicDto.getLicenceNo();
                            svcNameLicNo.append(svcName1).append(" : ").append(licenceNo1);
                            HcsaServiceDto hcsaServiceDto1 = HcsaServiceCacheHelper.getServiceByServiceName(svcName1);
                            serviceCodes.add(hcsaServiceDto1.getSvcCode());
                        }
                    }
                    emailMap.put(SERVICE_LICENCE_NAME, svcNameLicNo.toString());
                    emailMap.put("ApplicationNumber", applicationNo);
                    emailMap.put(CESSATION_DATE, DateFormatUtils.format(effectiveDate,"dd/MM/yyyy"));
                    emailMap.put("email", systemParamConfig.getSystemAddressOne());
                    emailMap.put("MOH_AGENCY_NAM_GROUP", "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "</b>");
                    emailMap.put("MOH_AGENCY_NAME", "<b>" + AppConsts.MOH_AGENCY_NAME + "</b>");
                    MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_CEASE_PRESENT_DATE).getEntity();
                    Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                    map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
                    map.put("ApplicationNumber", applicationNo);
                    String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map);
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
                    emailParam.setSvcCodeList(serviceCodes);
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_CEASE_PRESENT_DATE_MSG);
                    emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    notificationHelper.sendNotification(emailParam);
                    //sms
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_CEASE_PRESENT_DATE_SMS);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                    notificationHelper.sendNotification(emailParam);

                    //lic email
                    emailMap.clear();
                    emailMap.put("ApplicantName", applicantName);
                    emailMap.put(SERVICE_LICENCE_NAME, svcNameLicNo.toString());
                    emailMap.put("LicenceNumber", licenceNo);
                    emailMap.put(CESSATION_DATE, DateFormatUtils.format(new Date(),"dd/MM/yyyy"));
                    emailMap.put("email", systemParamConfig.getSystemAddressOne());
                    emailMap.put("MOH_AGENCY_NAM_GROUP", "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "</b>");
                    emailMap.put("MOH_AGENCY_NAME", "<b>" + AppConsts.MOH_AGENCY_NAME + "</b>");

                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE);
                    emailParam.setTemplateContent(emailMap);
                    emailParam.setQueryCode(licenceNo);
                    emailParam.setReqRefNum(licenceNo);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                    emailParam.setRefId(licId);
                    map.clear();
                    msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE).getEntity();
                    map.put(SERVICE_LICENCE_NAME, svcName);
                    map.put("LicenceNumber", licenceNo);
                    subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map);
                    emailParam.setSubject(subject);
                    //email
                    notificationHelper.sendNotification(emailParam);
                    //sms
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_SMS);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
                    notificationHelper.sendNotification(emailParam);
                    //msg
                    emailParam.setSvcCodeList(serviceCodes);
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_MSG);
                    emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    emailParam.setRefId(licId);
                    notificationHelper.sendNotification(emailParam);
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
        for (ApplicationDto applicationDto : applicationDtos) {
            String appId = applicationDto.getId();
            List<AppPremiseMiscDto> appPremiseMiscDtos = cessationClient.getAppPremiseMiscDtoListByAppId(appId).getEntity();
            if (!IaisCommonUtils.isEmpty(appPremiseMiscDtos)) {
                Date effectiveDate = appPremiseMiscDtos.get(0).getEffectiveDate();
                String effectiveDateStr = DateUtil.formatDate(effectiveDate);
                Date date1 = DateUtil.parseDate(effectiveDateStr);
                if (date1.after(date2)) {
                    applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_TEMPORARY_LICENCE);
                }
            }
            String serviceId = applicationDto.getServiceId();
            String appStatus = getStageId(serviceId, ApplicationConsts.APPLICATION_TYPE_CESSATION);
            if (!StringUtil.isEmpty(appStatus)) {
                applicationDto.setStatus(appStatus);
            }
        }
        applicationFeClient.updateApplicationList(applicationDtos);
        String serviceId = applicationDtos.get(0).getServiceId();
        String appStatus = getStageId(serviceId, ApplicationConsts.APPLICATION_TYPE_CESSATION);
        List<String> licNos = IaisCommonUtils.genNewArrayList();
        if (StringUtil.isEmpty(appStatus)) {
            for (AppCessatonConfirmDto appCessatonConfirmDto : appCessationDtosConfirms) {
                String licenceNo = appCessatonConfirmDto.getLicenceNo();
                Date effectiveDate = appCessatonConfirmDto.getEffectiveDate();
                if (effectiveDate.before(new Date())) {
                    licNos.add(licenceNo);
                }
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
        Collections.sort(appCessationDtosConfirms, (s1, s2) -> (s1.getAppNo().compareTo(s2.getAppNo())));
        return appCessationDtosConfirms;
    }

    @Override
    public boolean isGrpLicence(List<String> licIds) {
        LicenceDto entity = licenceClient.getLicBylicId(licIds.get(0)).getEntity();
        return entity.isGrpLic();
    }

    @Override
    public String getStageId(String serviceId, String appType) {
        String appStatus;
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        List<HcsaSvcRoutingStageDto> serviceConfig = feEicGatewayClient.getServiceConfig(serviceId, appType, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
        if (IaisCommonUtils.isEmpty(serviceConfig)) {
            return null;
        } else {
            String stageId = serviceConfig.get(0).getStageCode();
            switch (stageId) {
                case RoleConsts.USER_ROLE_ASO:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING;
                    break;
                case RoleConsts.USER_ROLE_PSO:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING;
                    break;
                case RoleConsts.PROCESS_TYPE_INS:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION;
                    break;
                case RoleConsts.USER_ROLE_AO1:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01;
                    break;
                case RoleConsts.USER_ROLE_AO2:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02;
                    break;
                default:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
            }
        }
        return appStatus;
    }

    @Override
    public List<AppCessLicDto> initRfiData(String appId, String premiseId) {
        ApplicationDto entity = applicationFeClient.getApplicationById(appId).getEntity();
        String originLicenceId = entity.getOriginLicenceId();
        List<String> licIds = IaisCommonUtils.genNewArrayList();
        licIds.add(originLicenceId);
        List<AppCessLicDto> appCessDtosByLicIds = getAppCessDtosByLicIds(licIds);
        List<AppCessHciDto> appCessHciDtos = appCessDtosByLicIds.get(0).getAppCessHciDtos();
        appCessHciDtos.clear();
        AppCessMiscDto appCessMiscDto = cessationClient.getAppMiscDtoByAppId(appId).getEntity();
        AppCessHciDto appCessHciDto = new AppCessHciDto();
        Date effectiveDate = appCessMiscDto.getEffectiveDate();
        String reason = appCessMiscDto.getReason();
        String otherReason = appCessMiscDto.getOtherReason();
        String patTransType = appCessMiscDto.getPatTransType();
        String patTransTo = appCessMiscDto.getPatTransTo();
        appCessHciDto.setPatientSelect(patTransType);
        appCessHciDto.setReason(reason);
        appCessHciDto.setOtherReason(otherReason);
        appCessHciDto.setEffectiveDate(effectiveDate);
        appCessHciDto.setPremiseId(premiseId);
        if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI.equals(patTransType) && !StringUtil.isEmpty(patTransTo)) {
            appCessHciDto.setPatHciName(patTransTo);
            appCessHciDto.setPatNeedTrans(Boolean.TRUE);
        } else if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO.equals(patTransType) && !StringUtil.isEmpty(patTransTo)) {
            appCessHciDto.setPatRegNo(patTransTo);
            appCessHciDto.setPatNeedTrans(Boolean.TRUE);
        } else if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_OTHER.equals(patTransType) && !StringUtil.isEmpty(patTransTo)) {
            appCessHciDto.setPatOthers(patTransTo);
            appCessHciDto.setPatNeedTrans(Boolean.TRUE);
        } else {
            String remarks = appCessMiscDto.getPatNoReason();
            appCessHciDto.setPatNoRemarks(remarks);
            appCessHciDto.setPatNeedTrans(Boolean.FALSE);
        }
        appCessHciDtos.add(appCessHciDto);
        return appCessDtosByLicIds;
    }


    /*
    utils
     */
    private Map<String, String> transform(AppSubmissionDto appSubmissionDto, String licenseeId, List<String> premiseIds) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        Double amount = 0.0;
        AuditTrailDto internet = AuditTrailHelper.getCurrentAuditTrailDto();
        String grpNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_CESSATION).getEntity();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        String serviceName = appSvcRelatedInfoDtoList.get(0).getServiceName();
        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
        String svcId = hcsaServiceDto.getId();
        HcsaServiceDto hcsaServiceDto1 = appConfigClient.getActiveHcsaServiceDtoById(svcId).getEntity();
        String svcCode = hcsaServiceDto.getSvcCode();
        appSvcRelatedInfoDtoList.get(0).setServiceId(hcsaServiceDto1.getId());
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

        AppSubmissionDto entity = applicationFeClient.saveSubmision(appSubmissionDto).getEntity();
        AppSubmissionDto appSubmissionDtoSave = applicationFeClient.saveApps(entity).getEntity();
        List<ApplicationDto> applicationDtos = appSubmissionDtoSave.getApplicationDtos();
        List<String> hciCodes = IaisCommonUtils.genNewArrayList();
        for (String premiseId : premiseIds) {
            PremisesDto entity1 = licenceClient.getLicPremisesDtoById(premiseId).getEntity();
            String hciCode = entity1.getHciCode();
            hciCodes.add(hciCode);
        }
        AuditTrailDto currentAuditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        for (ApplicationDto applicationDto : applicationDtos) {
            applicationDto.setAuditTrailDto(currentAuditTrailDto);
            String id = applicationDto.getId();
            AppGrpPremisesDto dto = cessationClient.getAppGrpPremisesDtoByAppId(id).getEntity();
            String hciCode = dto.getHciCode();
            if (hciCodes.contains(hciCode)) {
                applicationDto.setNeedNewLicNo(false);
                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_NOT_LICENCE);
                applicationDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_CESSATION_NOT);
            } else {
                applicationDto.setNeedNewLicNo(true);
                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE);
                applicationDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_CESSATION_NEED);
            }
            applicationFeClient.updateApplicationDto(applicationDto);
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

    private void transformSpec(AppSubmissionDto appSubmissionDto, String licenseeId, List<String> premiseIds) {
        Double amount = 0.0;
        AuditTrailDto internet = AuditTrailHelper.getCurrentAuditTrailDto();
        String grpNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_CESSATION).getEntity();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        String serviceName = appSvcRelatedInfoDtoList.get(0).getServiceName();
        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
        String svcId = hcsaServiceDto.getId();
        HcsaServiceDto hcsaServiceDto1 = appConfigClient.getActiveHcsaServiceDtoById(svcId).getEntity();
        String svcCode = hcsaServiceDto.getSvcCode();
        appSvcRelatedInfoDtoList.get(0).setServiceId(hcsaServiceDto1.getId());
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

        AppSubmissionDto entity = applicationFeClient.saveSubmision(appSubmissionDto).getEntity();
        AppSubmissionDto appSubmissionDtoSave = applicationFeClient.saveApps(entity).getEntity();
        List<ApplicationDto> applicationDtos = appSubmissionDtoSave.getApplicationDtos();
        List<String> hciCodes = IaisCommonUtils.genNewArrayList();
        for (String premiseId : premiseIds) {
            PremisesDto entity1 = licenceClient.getLicPremisesDtoById(premiseId).getEntity();
            String hciCode = entity1.getHciCode();
            hciCodes.add(hciCode);
        }
        AuditTrailDto currentAuditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        for (ApplicationDto applicationDto : applicationDtos) {
            applicationDto.setAuditTrailDto(currentAuditTrailDto);
            String id = applicationDto.getId();
            AppGrpPremisesDto dto = cessationClient.getAppGrpPremisesDtoByAppId(id).getEntity();
            String hciCode = dto.getHciCode();
            if (hciCodes.contains(hciCode)) {
                applicationDto.setNeedNewLicNo(false);
                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_SPEC_NOT_LICENCE);
                applicationDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_CESSATION_NOT);
            } else {
                applicationDto.setNeedNewLicNo(true);
                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_SPEC_NEED_LICENCE);
                applicationDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_CESSATION_NEED);
            }
            applicationFeClient.updateApplicationDto(applicationDto);
        }
    }


    private String transformRfi(AppSubmissionDto appSubmissionDto, String licenseeId, ApplicationDto applicationDto) throws Exception {
        AppSubmissionRequestInformationDto appSubmissionRequestInformationDto = new AppSubmissionRequestInformationDto();
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDto);
        appSubmissionRequestInformationDto.setOldAppSubmissionDto(oldAppSubmissionDto);
        Double amount = 0.0;
        AuditTrailDto internet = AuditTrailHelper.getCurrentAuditTrailDto();
        appSubmissionDto.setAppGrpId(applicationDto.getAppGrpId());
        ApplicationGroupDto entity1 = applicationFeClient.getApplicationGroup(applicationDto.getAppGrpId()).getEntity();
        appSubmissionDto.setFromBe(false);
        appSubmissionDto.setAppGrpNo(entity1.getGroupNo());
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_CESSATION);
        appSubmissionDto.setAmount(amount);
        appSubmissionDto.setAuditTrailDto(internet);
        appSubmissionDto.setPreInspection(true);
        appSubmissionDto.setRequirement(true);
        appSubmissionDto.setLicenseeId(licenseeId);
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        //status
        List<AppPremisesRoutingHistoryDto> hisList;
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String gatewayUrl = env.getProperty("iais.inter.gateway.url");
        Map<String, Object> params = IaisCommonUtils.genNewHashMap(1);
        params.put("appNo", applicationDto.getApplicationNo());
        hisList = IaisEGPHelper.callEicGatewayWithParamForList(gatewayUrl + "/v1/app-routing-history", HttpMethod.GET, params,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization(), AppPremisesRoutingHistoryDto.class).getEntity();
        if (hisList != null) {
            for (AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto : hisList) {
                if (ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(appPremisesRoutingHistoryDto.getProcessDecision())
                        || InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION.equals(appPremisesRoutingHistoryDto.getProcessDecision())) {
                    if (ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(appPremisesRoutingHistoryDto.getAppStatus())) {
                        applicationDto.setStatus(ApplicationConsts.PENDING_ASO_REPLY);
                    } else if (ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING.equals(appPremisesRoutingHistoryDto.getAppStatus())) {
                        applicationDto.setStatus(ApplicationConsts.PENDING_PSO_REPLY);
                    } else if (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS.equals(appPremisesRoutingHistoryDto.getAppStatus())) {
                        applicationDto.setStatus(ApplicationConsts.PENDING_INP_REPLY);
                    }
                }
            }
        }
        setRiskToDto(appSubmissionDto);
        appSubmissionRequestInformationDto.setAppSubmissionDto(appSubmissionDto);
        appSubmissionRequestInformationDto.setRfiStatus(applicationDto.getStatus());
        AppSubmissionDto appSubmissionDto1 = applicationFeClient.saveRfcCessationSubmision(appSubmissionRequestInformationDto).getEntity();
        String appId = appSubmissionDto1.getApplicationDtos().get(0).getId();
        return appId;
    }


    private AppCessMiscDto setMiscData(AppCessationDto appCessationDto, String appId) {
        AuditTrailDto currentAuditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
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
        appCessMiscDto.setAuditTrailDto(currentAuditTrailDto);
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
