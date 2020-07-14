package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.CessationBeService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.sz.commons.util.DateUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @author weilu
 * @date 2020/2/26 16:29
 */
@Service
@Slf4j
public class CessationBeServiceImpl implements CessationBeService {
    private final static String FURTHERDATECESSATION = "4FAD8B3B-E652-EA11-BE7F-000C29F371DC";
    private final static String PRESENTDATECESSATION = "50AD8B3B-E652-EA11-BE7F-000C29F371DC";

    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private CessationClient cessationClient;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private TaskService taskService;
    @Autowired
    private MsgTemplateClient msgTemplateClient;
    @Autowired
    private EmailClient emailClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;


    @Value("${iais.email.sender}")
    private String mailSender;

    @Override
    public List<AppCessLicDto> getAppCessDtosByLicIds(List<String> licIds) {
        List<AppCessLicDto> appCessDtos = IaisCommonUtils.genNewArrayList();
        if (licIds != null && !licIds.isEmpty()) {
            for (String licId : licIds) {
                AppCessLicDto appCessDto = new AppCessLicDto();
                LicenceDto licenceDto = hcsaLicenceClient.getLicenceDtoById(licId).getEntity();
                List<PremisesDto> premisesDtos = hcsaLicenceClient.getPremisess(licId).getEntity();
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
                        String hciCode = premisesDto.getHciCode();
                        String hciAddress = MiscUtil.getAddress(blkNo, streetName, buildingName, floorNo, unitNo, postalCode);
                        AppCessHciDto appCessHciDto = new AppCessHciDto();
                        String hciName = premisesDto.getHciName();
                        appCessHciDto.setHciCode(hciCode);
                        appCessHciDto.setHciName(hciName);
                        appCessHciDto.setPremiseId(premisesId);
                        appCessHciDto.setHciAddress(hciAddress);
                        appCessHciDtos.add(appCessHciDto);
                    }
                }
                appCessDto.setAppCessHciDtos(appCessHciDtos);
                appCessDtos.add(appCessDto);
            }
            return appCessDtos;
        } else {
            return null;
        }
    }

    @Override
    public List<String> saveCessations(List<AppCessationDto> appCessationDtos, String licenseeId) {
        if (StringUtil.isEmpty(licenseeId)) {
            licenseeId = "9ED45E34-B4E9-E911-BE76-000C29C8FBE4";
        }
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
        String finalLicenseeId = licenseeId;
        licPremiseIdMap.forEach((licId, premiseIds) -> {
            List<String> licIds = IaisCommonUtils.genNewArrayList();
            licIds.clear();
            licIds.add(licId);
            AppSubmissionDto appSubmissionDto = hcsaLicenceClient.getAppSubmissionDtos(licIds).getEntity().get(0);
            Map<String, String> transform = transform(appSubmissionDto, finalLicenseeId, premiseIds);
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
        return appIds;
    }

    @Override
    public Map<String, Boolean> listResultCeased(List<String> licIds) {
        return cessationClient.listCanCeased(licIds).getEntity();
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
            LicenceDto licenceDto = hcsaLicenceClient.getLicenceDtoById(licId).getEntity();
            String svcName = licenceDto.getSvcName();
            String licenceNo = licenceDto.getLicenceNo();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(svcName);
            String svcType = hcsaServiceDto.getSvcType();
            if (ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(svcType)) {
                List<String> specLicIds = hcsaLicenceClient.getSpecIdsByBaseId(licId).getEntity();
                if (!IaisCommonUtils.isEmpty(specLicIds)) {
                    for (String specLicId : specLicIds) {
                        AppSpecifiedLicDto appSpecifiedLicDto = new AppSpecifiedLicDto();
                        LicenceDto specLicenceDto = hcsaLicenceClient.getLicenceDtoById(specLicId).getEntity();
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
    public List<AppCessatonConfirmDto> getConfirmDto(List<AppCessationDto> appCessationDtos, List<String> appIds, LoginContext loginContext) throws Exception {
        List<String> licIds = IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        List<AppCessatonConfirmDto> appCessationDtosConfirms = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < appCessationDtos.size(); i++) {
            AppCessationDto appCessationDto = appCessationDtos.get(i);
            String licId = appCessationDto.getLicId();
            LicenceDto entity = hcsaLicenceClient.getLicenceDtoById(licId).getEntity();
            String licenseeId = entity.getLicenseeId();
            licIds.clear();
            licIds.add(licId);
            String appId = appIds.get(i);
            ApplicationDto applicationDto = applicationClient.getApplicationById(appId).getEntity();
            applicationDtos.add(applicationDto);
            List<AppCessLicDto> appCessDtosByLicIds = getAppCessDtosByLicIds(licIds);
            AppCessLicDto appCessLicDto = appCessDtosByLicIds.get(0);
            String licenceNo = appCessLicDto.getLicenceNo();
            String svcName = appCessLicDto.getSvcName();
            String hciName = appCessLicDto.getAppCessHciDtos().get(0).getHciName();
            String hciAddress = appCessLicDto.getAppCessHciDtos().get(0).getHciAddress();
            String applicationNo = applicationDto.getApplicationNo();
            Date effectiveDate = appCessationDto.getEffectiveDate();
            try {
                if (effectiveDate.after(new Date())) {
                    sendEmail(FURTHERDATECESSATION, effectiveDate, svcName, licId, licenseeId, licenceNo);
                } else {
                    sendEmail(PRESENTDATECESSATION, effectiveDate, svcName, licId, licenseeId, licenceNo);
                }
            } catch (Exception e) {
                e.getMessage();
                log.info("======send email error");
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
        routingTaskToAo3(applicationDtos, loginContext);
        List<String> licNos = IaisCommonUtils.genNewArrayList();
        for (AppCessatonConfirmDto appCessatonConfirmDto : appCessationDtosConfirms) {
            String licenceNo = appCessatonConfirmDto.getLicenceNo();
            Date effectiveDate = appCessatonConfirmDto.getEffectiveDate();
            if (effectiveDate.before(new Date())) {
                licNos.add(licenceNo);
            }
        }
        return appCessationDtosConfirms;
    }

    @Override
    public void sendEmail(String msgId, Date date, String svcName, String appGrpId, String licenseeId, String licNo) throws IOException, TemplateException {
        Map<String, Object> map = new HashMap<>(34);
        String dateStr = DateUtil.formatDateTime(date, "dd/MM/yyyy");
        map.put("date", dateStr);
        map.put("licenceA", svcName + ": " + licNo);
        MsgTemplateDto entity = msgTemplateClient.getMsgTemplate(msgId).getEntity();
        String messageContent = entity.getMessageContent();
        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(messageContent, map);
        EmailDto emailDto = new EmailDto();
        emailDto.setContent(templateMessageByContent);
        emailDto.setSubject("MOH IAIS – Cessation");
        emailDto.setSender(mailSender);
        emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
        emailDto.setClientQueryCode(appGrpId);
        emailClient.sendNotification(emailDto).getEntity();
    }

    private void routingTaskToAo3(List<ApplicationDto> applicationDtos, LoginContext loginContext) throws FeignException {
        String curRoleId = loginContext.getCurRoleId();
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_ASO);
        List<HcsaSvcStageWorkingGroupDto> taskConfig = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        for(ApplicationDto applicationDto :applicationDtos) {
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
            appPremisesRoutingHistoryDto.setRoleId(curRoleId);
            appPremisesRoutingHistoryDto.setStageId(HcsaConsts.ROUTING_STAGE_ASO);
            appPremisesRoutingHistoryDto.setProcessDecision(ApplicationConsts.PROCESSING_DECISION_VERIFIED);
            appPremisesRoutingHistoryDto.setApplicationNo(applicationDto.getApplicationNo());
            appPremisesRoutingHistoryDto.setActionby(loginContext.getUserId());
            appPremisesRoutingHistoryDto.setAppStatus(applicationDto.getStatus());
            appPremisesRoutingHistoryDto.setAuditTrailDto(AuditTrailDto.getThreadDto());
            appPremisesRoutingHistoryDto.setWrkGrpId(taskConfig.get(0).getGroupId());
            List<AppPremisesRoutingHistoryDto> asoHistory = IaisCommonUtils.genNewArrayList();
            asoHistory.add(appPremisesRoutingHistoryDto);
            taskService.createHistorys(asoHistory);
        }
            TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(applicationDtos, HcsaConsts.ROUTING_STAGE_AO3, RoleConsts.USER_ROLE_AO3, IaisEGPHelper.getCurrentAuditTrailDto());
            List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
            taskService.createTasks(taskDtos);
            List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
            appPremisesRoutingHistoryDtos.get(0).setActionby(loginContext.getUserId());
            appPremisesRoutingHistoryDtos.get(0).setWrkGrpId(taskConfig.get(0).getGroupId());
            taskService.createHistorys(appPremisesRoutingHistoryDtos);
        }

    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId) {
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
        for (ApplicationDto applicationDto : applicationDtos) {
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        return hcsaSvcStageWorkingGroupDtos;
    }

    private Map<String, String> transform(AppSubmissionDto appSubmissionDto, String licenseeId, List<String> premiseIds) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        Double amount = 0.0;
        AuditTrailDto internet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String grpNo = beEicGatewayClient.getAppNo(ApplicationConsts.APPLICATION_TYPE_CESSATION, signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        String serviceName = appSvcRelatedInfoDtoList.get(0).getServiceName();
        log.info(StringUtil.changeForLog("============================serviceName" + serviceName));
        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
        String svcId = hcsaServiceDto.getId();
        String svcCode = hcsaServiceDto.getSvcCode();
        appSvcRelatedInfoDtoList.get(0).setServiceId(svcId);
        appSvcRelatedInfoDtoList.get(0).setServiceCode(svcCode);
        appSubmissionDto.setAppGrpNo(grpNo);
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_CESSATION);
        appSubmissionDto.setAmount(amount);
        appSubmissionDto.setAuditTrailDto(internet);
        appSubmissionDto.setFromBe(true);
        appSubmissionDto.setPreInspection(true);
        appSubmissionDto.setRequirement(true);
        appSubmissionDto.setLicenseeId(licenseeId);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        setRiskToDto(appSubmissionDto);
        AppSubmissionDto entity = applicationClient.saveApps(appSubmissionDto).getEntity();
        AppSubmissionDto appSubmissionDtoSave = applicationClient.saveSubmision(entity).getEntity();
        List<ApplicationDto> applicationDtos = appSubmissionDtoSave.getApplicationDtos();
        List<String> hciCodes = IaisCommonUtils.genNewArrayList();
        for (String premiseId : premiseIds) {
            PremisesDto entity1 = hcsaLicenceClient.getLicPremisesDtoById(premiseId).getEntity();
            String hciCode = entity1.getHciCode();
            hciCodes.add(hciCode);
        }
        for (ApplicationDto applicationDto : applicationDtos) {
            String id = applicationDto.getId();
            AppGrpPremisesDto dto = cessationClient.getAppGrpPremisesDtoByAppId(id).getEntity();
            String hciCode = dto.getHciCode();
            if (hciCodes.contains(hciCode)) {
                String appId = id;
                for (String premiseId : premiseIds) {
                    PremisesDto entity1 = hcsaLicenceClient.getLicPremisesDtoById(premiseId).getEntity();
                    String hciCode1 = entity1.getHciCode();
                    hciCodes.add(hciCode);
                    if (hciCode.equals(hciCode1)) {
                        map.put(premiseId, appId);
                    }
                }
                applicationDto.setNeedNewLicNo(false);
                applicationDto.setGroupLicenceFlag(null);
            } else {
                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
            }
            applicationClient.updateApplication(applicationDto);
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

        List<RiskResultDto> riskResultDtoList = hcsaConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();

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
            return result;
        }
        for (RiskResultDto riskResultDto : riskResultDtoList) {
            if (serviceCode.equals(riskResultDto.getSvcCode())) {
                result = riskResultDto;
            }
        }
        return result;
    }

    private List<LicenceDto> updateLicenceStatus(List<LicenceDto> licenceDtos, Date date) {
        List<LicenceDto> updateLicenceDtos = IaisCommonUtils.genNewArrayList();
        for (LicenceDto licenceDto : licenceDtos) {
            licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
            licenceDto.setEndDate(date);
            updateLicenceDtos.add(licenceDto);
        }
        return updateLicenceDtos;
    }
}
