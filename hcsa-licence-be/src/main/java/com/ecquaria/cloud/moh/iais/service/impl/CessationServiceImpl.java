package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessatonConfirmDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.CessationService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/2/26 16:29
 */
@Service
@Slf4j
public class CessationServiceImpl implements CessationService {
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
    private final String FURTHERDATECESSATION = "4FAD8B3B-E652-EA11-BE7F-000C29F371DC";
    private final String PRESENTDATECESSATION = "50AD8B3B-E652-EA11-BE7F-000C29F371DC";

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
                        String hciAddress = MiscUtil.getAddress(blkNo, streetName, buildingName, floorNo, unitNo, postalCode);
                        AppCessHciDto appCessHciDto = new AppCessHciDto();
                        String hciName = premisesDto.getHciName();
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
        List<AppCessMiscDto> appCessMiscDtos = IaisCommonUtils.genNewArrayList();
        List<String> appIds = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < appCessationDtos.size(); i++) {
            AppCessationDto appCessationDto = appCessationDtos.get(0);
            String licId = appCessationDto.getLicId();
            String premiseId = appCessationDto.getPremiseId();
            List<String> licIds = IaisCommonUtils.genNewArrayList();
            licIds.clear();
            licIds.add(licId);
            String appId = transform(licIds,licenseeId,premiseId);
            appIds.add(appId);
            AppCessMiscDto appCessMiscDto = setMiscData(appCessationDto, appId);
            appCessMiscDtos.add(appCessMiscDto);
        }
        cessationClient.saveCessation(appCessMiscDtos).getEntity();
        return appIds;
    }

    @Override
    public List<Boolean> listResultCeased(List<String> licIds) {
        List<Boolean> results = IaisCommonUtils.genNewArrayList();
        for (String licId : licIds) {
            List<String> appIds = hcsaLicenceClient.getAppIdsByLicId(licId).getEntity();
            List<String> appIdsTrue = IaisCommonUtils.genNewArrayList();
            if (appIds != null && !appIds.isEmpty()) {
                for (String appId : appIds) {
                    ApplicationDto applicationDto = applicationClient.getApplicationById(appId).getEntity();
                    String status = applicationDto.getStatus();
                    if (ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(status) || ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(status) || ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED.equals(status)) {
                        appIdsTrue.add(appId);
                    }
                }
            }
            int size = appIds.size();
            int size1 = appIdsTrue.size();
            if (size == size1) {
                results.add(true);
            } else {
                results.add(false);
            }
        }
        return results;
    }

    @Override
    public List<String> listHciName() {
        List<String> hciNames = cessationClient.listHciNames().getEntity();
        return hciNames;
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
            if (effectiveDate.after(new Date())) {
                sendEmail(FURTHERDATECESSATION, effectiveDate, svcName, licId, licenseeId, licenceNo);
            } else {
                sendEmail(PRESENTDATECESSATION, effectiveDate, svcName, licId, licenseeId, licenceNo);
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
        emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
        emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
        emailDto.setClientQueryCode(appGrpId);
        emailClient.sendNotification(emailDto).getEntity();
    }

    private void routingTaskToAo3(List<ApplicationDto> applicationDtos, LoginContext loginContext) throws FeignException {
        String curRoleId = loginContext.getCurRoleId();
        log.info("=============>>>roleId" + curRoleId);
        TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(applicationDtos, HcsaConsts.ROUTING_STAGE_AO3, RoleConsts.USER_ROLE_AO3, IaisEGPHelper.getCurrentAuditTrailDto());
        List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
        taskService.createTasks(taskDtos);
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryDtos.get(0);
        appPremisesRoutingHistoryDto.setRoleId(curRoleId);
        appPremisesRoutingHistoryDto.setStageId(HcsaConsts.ROUTING_STAGE_ASO);
        appPremisesRoutingHistoryDto.setProcessDecision(ApplicationConsts.PROCESSING_DECISION_VERIFIED);
        taskService.createHistorys(appPremisesRoutingHistoryDtos);
    }

    private String transform(List<String> licIds, String licenseeId,String premiseId) {
        Double amount = 0.0;
        String appId = null;
        AuditTrailDto internet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String grpNo = beEicGatewayClient.getAppNo(ApplicationConsts.APPLICATION_TYPE_CESSATION, signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
        List<AppSubmissionDto> appSubmissionDtoList = hcsaLicenceClient.getAppSubmissionDtos(licIds).getEntity();
        AppSubmissionDto appSubmissionDto = appSubmissionDtoList.get(0);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        String serviceName = appSvcRelatedInfoDtoList.get(0).getServiceName();
        log.info("============================serviceName" + serviceName);
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
        appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        setRiskToDto(appSubmissionDto);
        AppSubmissionDto entity = applicationClient.saveApps(appSubmissionDto).getEntity();
        AppSubmissionDto appSubmissionDtoSave = applicationClient.saveSubmision(entity).getEntity();
        List<ApplicationDto> applicationDtos = appSubmissionDtoSave.getApplicationDtos();
        for(ApplicationDto applicationDto :applicationDtos) {
            String id = applicationDto.getId();
            AppGrpPremisesDto dto = cessationClient.getAppGrpPremisesDtoByAppId(id).getEntity();
            String hciCode = dto.getHciCode();
            PremisesDto entity1 = hcsaLicenceClient.getLicPremisesDtoById(premiseId).getEntity();
            String hciCode1 = entity1.getHciCode();
            if (hciCode1.equals(hciCode)) {
                appId = id ;
            }
        }
        return appId;
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
        List<RiskAcceptiionDto> riskAcceptiionDtoList = new ArrayList();
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

    private List<LicenceDto> updateLicenceStatus(List<LicenceDto> licenceDtos,Date date){
        List<LicenceDto> updateLicenceDtos = IaisCommonUtils.genNewArrayList();
        for(LicenceDto licenceDto :licenceDtos){
            licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
            licenceDto.setEndDate(date);
            updateLicenceDtos.add(licenceDto);
        }
        return updateLicenceDtos;
    }
}
