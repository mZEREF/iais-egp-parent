package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.CessationService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.*;

import java.io.IOException;
import java.util.*;

import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.sz.commons.util.DateUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Override
    public List<String> getActiveLicence(List<String> licIds) {

        return null;
    }

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
                        String streetName = premisesDto.getStreetName();
                        String buildingName = premisesDto.getBuildingName();
                        String floorNo = premisesDto.getFloorNo();
                        String unitNo = premisesDto.getUnitNo();
                        String postalCode = premisesDto.getPostalCode();
                        String hciAddress = IaisCommonUtils.appendPremisesAddress(blkNo, streetName, buildingName, floorNo, unitNo, postalCode);
                        AppCessHciDto appCessHciDto = new AppCessHciDto();
                        String hciName = premisesDto.getHciName();
                        appCessHciDto.setHciName(hciName);
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
    public List<String> saveCessations(List<AppCessationDto> appCessationDtos) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        List<AppCessMiscDto> appCessMiscDtos = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < appCessationDtos.size(); i++) {
            Double amount = 0.0;
            AuditTrailDto internet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
            AppCessationDto appCessationDto = appCessationDtos.get(0);
            AppCessMiscDto appCessMiscDto = new AppCessMiscDto();
            String licId = appCessationDto.getWhichTodo();
            List<String> licIds = IaisCommonUtils.genNewArrayList();
            licIds.clear();
            licIds.add(licId);
            String grpNo = beEicGatewayClient.getAppNo(ApplicationConsts.APPLICATION_TYPE_CESSATION, signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
            List<AppSubmissionDto> appSubmissionDtoList = hcsaLicenceClient.getAppSubmissionDtos(licIds).getEntity();
            AppSubmissionDto appSubmissionDto = appSubmissionDtoList.get(0);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            String serviceName = appSvcRelatedInfoDtoList.get(0).getServiceName();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
            String svcId = hcsaServiceDto.getId();
            String svcCode = hcsaServiceDto.getSvcCode();
            appSvcRelatedInfoDtoList.get(0).setServiceId(svcId);
            appSvcRelatedInfoDtoList.get(0).setServiceCode(svcCode);
            appSubmissionDto.setAppGrpNo(grpNo);
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_CESSATION);
            appSubmissionDto.setAmount(amount);
            appSubmissionDto.setAuditTrailDto(internet);
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
            appSubmissionDto.setLicenseeId("9ED45E34-B4E9-E911-BE76-000C29C8FBE4");
            appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
            setRiskToDto(appSubmissionDto);
            AppSubmissionDto entity = applicationClient.saveApps(appSubmissionDto).getEntity();
            AppSubmissionDto appSubmissionDtoSave = applicationClient.saveSubmision(entity).getEntity();
            List<ApplicationDto> applicationDtos = appSubmissionDtoSave.getApplicationDtos();
            String appId = applicationDtos.get(0).getId();
            setMiscData(appCessationDto, appCessMiscDto, appId);
            appCessMiscDtos.add(appCessMiscDto);
        }
        List<String> appIds = cessationClient.saveCessation(appCessMiscDtos).getEntity();
        return appIds;
    }

    @Override
    public void updateCesation(List<AppCessationDto> appCessationDtos) {
        List<AppCessMiscDto> appCessMiscDtos = IaisCommonUtils.genNewArrayList();
//        for (AppCessationDto appCessationDto : appCessationDtos) {
//            AppCessMiscDto appCessMiscDto = new AppCessMiscDto();
//            String licId = appCessationDto.getWhichTodo();
//            ApplicationGroupDto applicationGroupDto = new ApplicationGroupDto();
//            ApplicationDto applicationDto = applicationClient.getApplicationByLicId(licId).getEntity();
//            String appGrpId = applicationDto.getAppGrpId();
//            applicationGroupDto.setId(appGrpId);
//            List<ApplicationDto> applicationDtoList = IaisCommonUtils.genNewArrayList();
//            List<ApplicationDto> applicationDtos = applicationClient.getApplicationByLicId(licId).getEntity();
//            ApplicationDto applicationDto = applicationDtos.get(0);
//            String applicationNo = applicationDto.getApplicationNo();
//            applicationDto.setStatus("APST009");
//            applicationDtoList.add(applicationDto);
//            ApplicationDto applicationDto1 = new ApplicationDto();
//            applicationDto1.setApplicationType("APTY001");
//            applicationDto1.setApplicationNo(applicationNo);
//            applicationDto1.setStatus("APST007");
//            applicationDto1.setAppGrpId(appGrpId);
//            applicationDto1.setServiceId("35F99D15-820B-EA11-BE7D-000C29F371DC");
//            applicationDto1.setVersion(1);
//            applicationDto1.setLicenceId(licId);
//            applicationDtoList.add(applicationDto1);
//            appCessMiscDto.setApplicationDto(applicationDtoList);
//            List<AppGrpPremisesDto> appGrpPremisesDto = getAppGrpPremisesDto();
//            appCessMiscDto.setAppGrpPremisesDtos(appGrpPremisesDto);
//            appCessMiscDto.setApplicationGroupDto(applicationGroupDto);
//            appCessMiscDtos.add(appCessMiscDto);
//        }
        cessationClient.updateCessation(appCessMiscDtos).getEntity();

    }

    @Override
    public void updateLicence(List<String> licNos) {
        AppealLicenceDto appealLicenceDto = new AppealLicenceDto();
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        List<LicenceDto> licenceDtos = hcsaLicenceClient.getLicDtosByLicNos(licNos).getEntity();
        List<LicenceDto> licenceDtoNew = IaisCommonUtils.genNewArrayList();
        if (licenceDtos != null && !licenceDtos.isEmpty()) {
            for (LicenceDto licenceDto : licenceDtos) {
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
                licenceDtoNew.add(licenceDto);
            }
        }
        appealLicenceDto.setAppealLicence(licenceDtoNew);
        beEicGatewayClient.updateAppealLicence(appealLicenceDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
        hcsaLicenceClient.updateLicences(licenceDtoNew);
    }

    @Override
    public void routingTaskToAo3(List<ApplicationDto> applicationDtos, LoginContext loginContext) throws FeignException {
        String curRoleId = loginContext.getCurRoleId();
        log.info("=============>>>roleId" + curRoleId);
        TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(applicationDtos, HcsaConsts.ROUTING_STAGE_AO3, RoleConsts.USER_ROLE_AO3, IaisEGPHelper.getCurrentAuditTrailDto());
        List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
        List<TaskDto> tasks = taskService.createTasks(taskDtos);
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
        appPremisesRoutingHistoryDtos.get(0).setRoleId(curRoleId);
        appPremisesRoutingHistoryDtos.get(0).setProcessDecision(ApplicationConsts.APPLICATION_STATUS_VERIFIED);
        taskService.createHistorys(appPremisesRoutingHistoryDtos);
    }

    @Override
    public List<String> listLicIdsCeased(List<String> licIds) {
        List<String> activeLicIds = cessationClient.getlicIdToCessation(licIds).getEntity();
        return activeLicIds;
    }

    @Override
    public List<String> listHciName() {
        List<String> hciNames = cessationClient.listHciNames().getEntity();
        return hciNames;
    }

    @Override
    public void sendEmail(String msgId, Date date,String svcName,String appGrpId,String licenseeId) throws IOException, TemplateException {
        Map<String,Object> map = new HashMap<>(34);
        String dateStr = DateUtil.formatDateTime(date, "dd/MM/yyyy");
        map.put("date",dateStr);
        map.put("licenceA",svcName);
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

    private AppCessMiscDto setMiscData(AppCessationDto appCessationDto, AppCessMiscDto appCessMiscDto, String appId) {
        Date effectiveDate = appCessationDto.getEffectiveDate();
        String reason = appCessationDto.getReason();
        String otherReason = appCessationDto.getOtherReason();
        Boolean patNeedTrans = appCessationDto.getPatNeedTrans();
        String patientSelect = appCessationDto.getPatientSelect();
        String patHciName = appCessationDto.getPatHciName();
        String patRegNo = appCessationDto.getPatRegNo();
        String patOthers = appCessationDto.getPatOthers();
        String patNoRemarks = appCessationDto.getPatNoRemarks();

        appCessMiscDto.setAppealType(ApplicationConsts.CESSATION_TYPE_APPLICATION);
        appCessMiscDto.setEffectiveDate(effectiveDate);
        appCessMiscDto.setReason(reason);
        appCessMiscDto.setOtherReason(otherReason);
        appCessMiscDto.setPatNeedTrans(patNeedTrans);
        appCessMiscDto.setPatNoReason(patNoRemarks);
        appCessMiscDto.setPatTransType(patientSelect);
        appCessMiscDto.setAppPremCorreId(appId);
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
}
