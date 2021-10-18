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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessatonConfirmDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppSpecifiedLicDto;
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
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.CessationFeService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceFeMsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
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
import java.util.ListIterator;
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
    @Autowired
    private AppConfigClient appConfigClient;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    AppSubmissionService appSubmissionService;
    @Autowired
    LicenceFeMsgTemplateClient licenceFeMsgTemplateClient;
    @Autowired
    private FeEicGatewayClient feEicGatewayClient;
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
            List<String> specIds = licenceClient.getActSpecIdByActBaseId(licId).getEntity();
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
    public Map<String, List<String>> saveCessations(List<AppCessationDto> appCessationDtos, LoginContext loginContext) {
        String licenseeId = loginContext.getLicenseeId();
        List<AppCessMiscDto> appCessMiscDtos = IaisCommonUtils.genNewArrayList();
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
        Map<String, List<String>> appIdsPremisesMap = IaisCommonUtils.genNewHashMap();
        licPremiseIdMap.forEach((licId, premiseIds) -> {
            List<String> licIds = IaisCommonUtils.genNewArrayList();
            licIds.add(licId);
            AppSubmissionDto appSubmissionDto = licenceClient.getAppSubmissionDtos(licIds).getEntity().get(0);
            filetDoc(appSubmissionDto);
            Map<String, List<String>> baseMap = transform(appSubmissionDto, licenseeId, premiseIds,appCessationDtos);
            List<String> specLicIds = licenceClient.getSpecLicIdsByLicIds(licIds).getEntity();
            if (!IaisCommonUtils.isEmpty(specLicIds)) {
                AppSubmissionDto appSubmissionDtoSpec = licenceClient.getAppSubmissionDtos(specLicIds).getEntity().get(0);
                filetDoc(appSubmissionDtoSpec);
                Map<String, List<String>> specMap = transformSpec(appSubmissionDtoSpec, licenseeId, premiseIds);
                for (String premiseId : premiseIds) {
                    List<String> baseAppIds = baseMap.get(premiseId);
                    List<String> specAppIds = specMap.get(premiseId);
                    baseAppIds.addAll(specAppIds);
                }
            }
            appIdsPremisesMap.putAll(baseMap);
        });

        appIdsPremisesMap.forEach((premiseId, appIds) -> {
            for (AppCessationDto appCessationDto : appCessationDtos) {
                String premiseId1 = appCessationDto.getPremiseId();
                if (premiseId.equals(premiseId1)) {
                    List<AppCessMiscDto> appCessMiscDtos1 = setMiscData(appCessationDto, appIds);
                    appCessMiscDtos.addAll(appCessMiscDtos1);
                }
            }
        });
        cessationClient.saveCessation(appCessMiscDtos).getEntity();
        return appIdsPremisesMap;
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
        List<String> appIds = IaisCommonUtils.genNewArrayList();
        appIds.add(appId);
        List<AppCessMiscDto> appCessMiscDtos1 = setMiscData(appCessationDtos.get(0), appIds);
        appCessMiscDtos.addAll(appCessMiscDtos1);
        cessationClient.saveCessation(appCessMiscDtos).getEntity();
    }


    @Override
    public List<String> listHciName() {
        List<String> hciNames = licenceClient.listHciNames().getEntity();
        return hciNames;
    }

    @Override
    public List<AppSpecifiedLicDto> getSpecLicInfo(List<String> licIds) {
        List<AppSpecifiedLicDto> appSpecifiedLicDtos = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isEmpty(licIds)) {
            return appSpecifiedLicDtos;
        }
        for (String licId : licIds) {
            LicenceDto licenceDto = licenceClient.getLicDtoById(licId).getEntity();
            String svcName = licenceDto.getSvcName();
            String licenceNo = licenceDto.getLicenceNo();
                List<String> specLicIds = licenceClient.getActSpecIdByActBaseId(licId).getEntity();
                if (!IaisCommonUtils.isEmpty(specLicIds)) {
                    for (String specLicId : specLicIds) {
                        AppSpecifiedLicDto appSpecifiedLicDto = new AppSpecifiedLicDto();
                        LicenceDto specLicenceDto = licenceClient.getLicDtoById(specLicId).getEntity();
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
        return appSpecifiedLicDtos;
    }

    @Override
    public List<AppCessatonConfirmDto> getConfirmDto(List<AppCessationDto> appCessationDtos, Map<String, List<String>> appIdPremisesMap, LoginContext loginContext) throws ParseException {
        List<AppCessatonConfirmDto> appCessationDtosConfirms = IaisCommonUtils.genNewArrayList();
        List<String> licIds = IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> specApplicationDtos = IaisCommonUtils.genNewArrayList();
        AuditTrailDto currentAuditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        for (int i = 0; i < appCessationDtos.size(); i++) {
            List<String> serviceCodes = IaisCommonUtils.genNewArrayList();
            AppCessationDto appCessationDto = appCessationDtos.get(i);
            String premiseId = appCessationDto.getPremiseId();
            String licId = appCessationDto.getLicId();
            List<String> specLicIds = licenceClient.getActSpecIdByActBaseId(licId).getEntity();
            LicenceDto licenceDto = licenceClient.getLicDtoById(licId).getEntity();
            licIds.clear();
            licIds.add(licId);
            List<String> appIds = appIdPremisesMap.get(premiseId);
            String appId = null;
            String baseAppNo = null ;
            for (String id : appIds) {
                ApplicationDto applicationDto = applicationFeClient.getApplicationById(id).getEntity();
                String appNo = applicationDto.getApplicationNo();
                String originLicenceId = applicationDto.getOriginLicenceId();
                if (licId.equals(originLicenceId)) {
                    baseAppNo = appNo;
                    break;
                }
            }
            for (String id : appIds) {
                ApplicationDto applicationDto = applicationFeClient.getApplicationById(id).getEntity();
                String originLicenceId = applicationDto.getOriginLicenceId();
                if (licId.equals(originLicenceId)) {
                    applicationDtos.add(applicationDto);
                    appId = id;
                }else {
                    applicationDto.setBaseApplicationNo(baseAppNo);
                    specApplicationDtos.add(applicationDto);
                }
            }
            ApplicationDto applicationDto = applicationFeClient.getApplicationById(appId).getEntity();
            applicationDto.setAuditTrailDto(currentAuditTrailDto);
            applicationDtos.add(applicationDto);
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
                    String loginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
                    Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                    String applicantName = loginContext.getUserName();
                    emailMap.put("ApplicantName", applicantName);
                    emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
                    emailMap.put("ApplicationNumber", baseAppNo);
                    StringBuilder svcNameLicNo = new StringBuilder();
                    svcNameLicNo.append(svcName).append(" : ").append(licenceNo);
                    if (!IaisCommonUtils.isEmpty(specLicIds)) {
                        for (String specLicId : specLicIds) {
                            LicenceDto specLicDto = licenceClient.getLicBylicId(specLicId).getEntity();
                            String svcName1 = specLicDto.getSvcName();
                            String licenceNo1 = specLicDto.getLicenceNo();
                            serviceCodes.add("<br/>");
                            svcNameLicNo.append(svcName1).append(" : ").append(licenceNo1);
                            HcsaServiceDto hcsaServiceDto1 = HcsaServiceCacheHelper.getServiceByServiceName(svcName1);
                            serviceCodes.add(hcsaServiceDto1.getSvcCode());
                        }
                    }
                    emailMap.put(SERVICE_LICENCE_NAME, svcNameLicNo.toString());
                    emailMap.put(CESSATION_DATE, DateFormatUtils.format(effectiveDate, "dd/MM/yyyy"));
                    emailMap.put(APPLICATION_DATE, DateFormatUtils.format(new Date(), "dd/MM/yyyy"));
                    emailMap.put("email", systemParamConfig.getSystemAddressOne());
                    emailMap.put("systemLink", loginUrl);
                    emailMap.put("MOH_AGENCY_NAM_GROUP", "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "</b>");
                    emailMap.put("MOH_AGENCY_NAME", "<b>" + AppConsts.MOH_AGENCY_NAME + "</b>");
                    MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_CEASE_FUTURE_DATE).getEntity();
                    Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                    map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
                    map.put("ApplicationNumber", baseAppNo);
                    String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map);
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_CEASE_FUTURE_DATE);
                    emailParam.setTemplateContent(emailMap);
                    emailParam.setQueryCode(baseAppNo);
                    emailParam.setReqRefNum(baseAppNo);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                    emailParam.setRefId(baseAppNo);
                    emailParam.setSubject(subject);
                    //email
                    log.info(StringUtil.changeForLog("==================== email ===============>>>>>>>"));
                    notificationHelper.sendNotification(emailParam);
                    //msg
                    EmailParam msgParam = new EmailParam();
                    msgParam.setQueryCode(baseAppNo);
                    msgParam.setReqRefNum(baseAppNo);
                    msgParam.setRefId(baseAppNo);
                    msgParam.setTemplateContent(emailMap);

                    msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_CEASE_FUTURE_DATE_MSG).getEntity();
                    subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map);
                    msgParam.setSubject(subject);
                    msgParam.setSvcCodeList(serviceCodes);
                    msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_CEASE_FUTURE_DATE_MSG);
                    msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    log.info(StringUtil.changeForLog("==================== notification ===============>>>>>>>"));
                    notificationHelper.sendNotification(msgParam);
                    //sms
                    msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_CEASE_FUTURE_DATE_SMS).getEntity();
                    subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map);
                    EmailParam smsParam = new EmailParam();
                    smsParam.setQueryCode(baseAppNo);
                    smsParam.setReqRefNum(baseAppNo);
                    smsParam.setRefId(baseAppNo);
                    smsParam.setTemplateContent(emailMap);
                    smsParam.setSubject(subject);
                    smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_CEASE_FUTURE_DATE_SMS);
                    smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                    log.info(StringUtil.changeForLog("==================== sms ===============>>>>>>>"));
                    notificationHelper.sendNotification(smsParam);

                } else {
                    Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                    String applicantName = loginContext.getUserName();
                    emailMap.put("ApplicantName", applicantName);
                    emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
                    StringBuilder svcNameLicNo = new StringBuilder();
                    svcNameLicNo.append(svcName).append(" : ").append(licenceNo);
                    if (!IaisCommonUtils.isEmpty(specLicIds)) {
                        for (String specLicId : specLicIds) {
                            LicenceDto specLicDto = licenceClient.getLicBylicId(specLicId).getEntity();
                            String svcName1 = specLicDto.getSvcName();
                            String licenceNo1 = specLicDto.getLicenceNo();
                            svcNameLicNo.append("<br/>");
                            svcNameLicNo.append(svcName1).append(" : ").append(licenceNo1);
                            HcsaServiceDto hcsaServiceDto1 = HcsaServiceCacheHelper.getServiceByServiceName(svcName1);
                            serviceCodes.add(hcsaServiceDto1.getSvcCode());
                        }
                    }
                    emailMap.put(SERVICE_LICENCE_NAME, svcNameLicNo.toString());
                    emailMap.put("ApplicationNumber", baseAppNo);
                    emailMap.put(CESSATION_DATE, DateFormatUtils.format(effectiveDate, "dd/MM/yyyy"));
                    emailMap.put("email", systemParamConfig.getSystemAddressOne());
                    emailMap.put("MOH_AGENCY_NAM_GROUP", "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "</b>");
                    emailMap.put("MOH_AGENCY_NAME", "<b>" + AppConsts.MOH_AGENCY_NAME + "</b>");
                    Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                    map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
                    map.put("ApplicationNumber", baseAppNo);
                    MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_CEASE_PRESENT_DATE).getEntity();
                    String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map);
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_CEASE_PRESENT_DATE);
                    emailParam.setTemplateContent(emailMap);
                    emailParam.setQueryCode(baseAppNo);
                    emailParam.setReqRefNum(baseAppNo);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                    emailParam.setRefId(baseAppNo);
                    emailParam.setSubject(subject);
                    //email
                    notificationHelper.sendNotification(emailParam);
                    //msg
                    EmailParam msgParam = new EmailParam();
                    msgParam.setQueryCode(baseAppNo);
                    msgParam.setReqRefNum(baseAppNo);
                    msgParam.setRefId(baseAppNo);
                    msgParam.setTemplateContent(emailMap);
                    msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_CEASE_PRESENT_DATE_MSG).getEntity();
                    subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map);
                    msgParam.setSubject(subject);
                    msgParam.setSvcCodeList(serviceCodes);
                    msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_CEASE_PRESENT_DATE_MSG);
                    msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    notificationHelper.sendNotification(msgParam);
                    //sms
                    EmailParam smsParam = new EmailParam();
                    smsParam.setTemplateContent(emailMap);
                    smsParam.setQueryCode(baseAppNo);
                    smsParam.setReqRefNum(baseAppNo);
                    smsParam.setRefId(baseAppNo);
                    msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_CEASE_PRESENT_DATE_SMS).getEntity();
                    subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map);
                    smsParam.setSubject(subject);
                    smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_CEASE_PRESENT_DATE_SMS);
                    smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                    notificationHelper.sendNotification(smsParam);
                }
            } catch (Exception e) {
                log.info(StringUtil.changeForLog("==================== email error ===============>>>>>>>" + e.getMessage()));
            }
            AppCessatonConfirmDto appCessatonConfirmDto = new AppCessatonConfirmDto();
            appCessatonConfirmDto.setAppNo(baseAppNo);
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
            String appStatus = getStageId(applicationDto.getRoutingServiceId(), ApplicationConsts.APPLICATION_TYPE_CESSATION);
            if (!StringUtil.isEmpty(appStatus)) {
                applicationDto.setStatus(appStatus);
            }
        }
        applicationFeClient.updateApplicationList(applicationDtos);
        applicationFeClient.updateApplicationList(specApplicationDtos);
        String appStatus = getStageId(applicationDtos.get(0).getRoutingServiceId(), ApplicationConsts.APPLICATION_TYPE_CESSATION);
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
        LicenceDto entity = licenceClient.getLicDtoById(licIds.get(0)).getEntity();
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

    @Override
    public PremisesDto getPremiseByHciCodeName(String hciNameCode) {
        PremisesDto premisesDto = licenceClient.getPremiseDtoByHciCodeOrName(hciNameCode).getEntity();
        if(premisesDto!=null){
            String blkNo = premisesDto.getBlkNo();
            String streetName = premisesDto.getStreetName();
            String buildingName = premisesDto.getBuildingName();
            String floorNo = premisesDto.getFloorNo();
            String unitNo = premisesDto.getUnitNo();
            String postalCode = premisesDto.getPostalCode();
            String hciAddress = MiscUtil.getAddress(blkNo, streetName, buildingName, floorNo, unitNo, postalCode);
            premisesDto.setHciAddress(hciAddress);
        }
        return premisesDto;
    }

    @Autowired
private RequestForChangeService requestForChangeService;
    /*
    utils
     */
    private Map<String, List<String>> transform(AppSubmissionDto appSubmissionDto, String licenseeId, List<String> premiseIds,List<AppCessationDto> appCessationDtos) {
        Map<String, List<String>> map = IaisCommonUtils.genNewHashMap();
        Double amount = 0.0;
        AuditTrailDto internet = AuditTrailHelper.getCurrentAuditTrailDto();
        String grpNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_CESSATION).getEntity();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        String serviceName = appSvcRelatedInfoDtoList.get(0).getServiceName();
        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
        String svcId = hcsaServiceDto.getId();
        HcsaServiceDto hcsaServiceDto1 = appConfigClient.getActiveHcsaServiceDtoById(svcId).getEntity();
        String svcCode = hcsaServiceDto.getSvcCode();
        //get the base service id
        LicenceDto licenceDto = new LicenceDto();
        licenceDto.setId(appSubmissionDto.getLicenceId());
        licenceDto.setSvcName(serviceName);
        log.info(StringUtil.changeForLog("The appSubmissionDto.getLicenceId() is -->:"+appSubmissionDto.getLicenceId()));
        log.info(StringUtil.changeForLog("The serviceName is -->:"+serviceName));
        String baseServiceId = requestForChangeService.baseSpecLicenceRelation(licenceDto,false);
        log.info(StringUtil.changeForLog("The baseServiceId is -->:"+baseServiceId));
        appSvcRelatedInfoDtoList.get(0).setServiceId(hcsaServiceDto1.getId());
        appSvcRelatedInfoDtoList.get(0).setServiceCode(svcCode);
        appSvcRelatedInfoDtoList.get(0).setServiceName(hcsaServiceDto.getSvcName());
        appSvcRelatedInfoDtoList.get(0).setBaseServiceId(baseServiceId);

        appSubmissionDto.setAppGrpNo(grpNo);
        appSubmissionDto.setFromBe(false);
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_CESSATION);
        appSubmissionDto.setAmount(amount);
        appSubmissionDto.setAuditTrailDto(internet);
        appSubmissionDto.setPreInspection(true);
        appSubmissionDto.setRequirement(true);
        appSubmissionDto.setLicenseeId(licenseeId);
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_GET_DATA);
        AppDeclarationMessageDto appDeclarationMessageDto = appCessationDtos.get(0).getAppDeclarationMessageDto();
        List<AppDeclarationDocDto> appDeclarationDocDtoList = appCessationDtos.get(0).getAppDeclarationDocDtoList();
        if (appDeclarationMessageDto != null){
            appSubmissionDto.setAppDeclarationMessageDto(appDeclarationMessageDto);
        }
        if (appDeclarationDocDtoList != null && appDeclarationDocDtoList.size() > 0){
            appSubmissionDto.setAppDeclarationDocDtos(appDeclarationDocDtoList);
        }
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
                    List<String> appIds = IaisCommonUtils.genNewArrayList();
                    appIds.add(appId);
                    map.put(premiseId, appIds);
                }
            }
        }
        return map;
    }

    private Map<String, List<String>> transformSpec(AppSubmissionDto appSubmissionDto, String licenseeId, List<String> premiseIds) {
        Map<String, List<String>> map = IaisCommonUtils.genNewHashMap();
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
        appSvcRelatedInfoDtoList.get(0).setServiceName(hcsaServiceDto.getSvcName());
        appSubmissionDto.setAppGrpNo(grpNo);
        appSubmissionDto.setFromBe(false);
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_CESSATION);
        appSubmissionDto.setAmount(amount);
        appSubmissionDto.setAuditTrailDto(internet);
        appSubmissionDto.setPreInspection(true);
        appSubmissionDto.setRequirement(true);
        appSubmissionDto.setLicenseeId(licenseeId);
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_GET_DATA);
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
        for (ApplicationDto applicationDto : applicationDtos) {
            String id = applicationDto.getId();
            AppGrpPremisesDto dto = cessationClient.getAppGrpPremisesDtoByAppId(id).getEntity();
            String hciCode = dto.getHciCode();
            for (String premiseId : premiseIds) {
                PremisesDto entity1 = licenceClient.getLicPremisesDtoById(premiseId).getEntity();
                String hciCode1 = entity1.getHciCode();
                if (hciCode1.equals(hciCode)) {
                    String appId = id;
                    List<String> appIds = IaisCommonUtils.genNewArrayList();
                    appIds.add(appId);
                    map.put(premiseId, appIds);
                }
            }
        }
        return map;
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


    private List<AppCessMiscDto> setMiscData(AppCessationDto appCessationDto, List<String> appIds) {
        List<AppCessMiscDto> appCessMiscDtos = IaisCommonUtils.genNewArrayList();
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
        String emailAddress = appCessationDto.getEmailAddress();
        String mobileNo = appCessationDto.getMobileNo();
        for (String appId : appIds) {
            AppCessMiscDto appCessMiscDto = new AppCessMiscDto();
            appCessMiscDto.setAppealType(ApplicationConsts.CESSATION_TYPE_APPLICATION);
            appCessMiscDto.setEffectiveDate(effectiveDate);
            appCessMiscDto.setReason(reason);
            appCessMiscDto.setPatNeedTrans(patNeedTrans);
            appCessMiscDto.setPatTransType(patientSelect);
            appCessMiscDto.setAppId(appId);
            appCessMiscDto.setAuditTrailDto(currentAuditTrailDto);
            appCessMiscDto.setTransferDetail(appCessationDto.getTransferDetail());
            appCessMiscDto.setTransferredWhere(appCessationDto.getTransferredWhere());
            //reason
            if(ApplicationConsts.CESSATION_REASON_OTHER.equals(reason)){
                appCessMiscDto.setOtherReason(otherReason);
            }
            if(patNeedTrans){
               if(ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI.equals(patientSelect)) {
                   appCessMiscDto.setPatTransTo(patHciName);
               }else if(ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO.equals(patientSelect)){
                   appCessMiscDto.setPatTransTo(patRegNo);
               }else {
                   appCessMiscDto.setPatTransTo(patOthers);
                   appCessMiscDto.setMobileNo(mobileNo);
                   appCessMiscDto.setEmailAddress(emailAddress);
               }
            }else {
                appCessMiscDto.setPatNoReason(patNoRemarks);
            }
            appCessMiscDtos.add(appCessMiscDto);
        }
        return appCessMiscDtos;
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

    private void filetDoc(AppSubmissionDto appSubmissionDto){
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)){
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
                List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                if(!IaisCommonUtils.isEmpty(appSvcDocDtoLit)){
                    ListIterator<AppSvcDocDto> appSvcDocDtoListIterator = appSvcDocDtoLit.listIterator();
                    while (appSvcDocDtoListIterator.hasNext()){
                        AppSvcDocDto appSvcDocDto = appSvcDocDtoListIterator.next();
                        String fileRepoId = appSvcDocDto.getFileRepoId();
                        String svcDocId = appSvcDocDto.getSvcDocId();
                        if(StringUtil.isEmpty(fileRepoId)||StringUtil.isEmpty(svcDocId)){
                            appSvcDocDtoListIterator.remove();
                        }
                    }
                }
            }
        }
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)){
            ListIterator<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoListIterator = appGrpPrimaryDocDtos.listIterator();
            while (appGrpPrimaryDocDtoListIterator.hasNext()){
                AppGrpPrimaryDocDto next = appGrpPrimaryDocDtoListIterator.next();
                String fileRepoId = next.getFileRepoId();
                String svcDocId = next.getSvcDocId();
                if(StringUtil.isEmpty(fileRepoId)||StringUtil.isEmpty(svcDocId)){
                    appGrpPrimaryDocDtoListIterator.remove();
                }
            }
        }
    }
}
