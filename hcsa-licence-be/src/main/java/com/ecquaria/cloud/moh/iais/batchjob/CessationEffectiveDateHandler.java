package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author weilu
 * @Date 2020/9/23 10:14
 */
@JobHandler(value="CessationEffectiveDateHandler")
@Component
@Slf4j
public class CessationEffectiveDateHandler extends IJobHandler {

    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    SystemParamConfig systemParamConfig;
    @Autowired
    private CessationClient cessationClient;
    @Autowired
    private LicenceApproveBatchjob licenceApproveBatchjob;
    @Autowired
    private LicenceService licenceService;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private BeEicGatewayClient gatewayClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;

    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;

    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            Date date = new Date();
            //licence
            log.debug(StringUtil.changeForLog("The CessationEffectiveDateHandler is doBatchJob ..."));
            List<LicenceDto> licenceDtos = IaisCommonUtils.genNewArrayList();
            List<ApplicationGroupDto> applicationGroupDtos = cessationClient.listAppGrpForCess().getEntity();
            log.info(StringUtil.changeForLog("====================appGrpGtos  size ==================" + applicationGroupDtos.size()));
            List<ApplicationGroupDto> applicationGroupDtosCesead = IaisCommonUtils.genNewArrayList();
            if (!IaisCommonUtils.isEmpty(applicationGroupDtos)) {
                for (ApplicationGroupDto applicationGroupDto : applicationGroupDtos) {
                    try {
                        String appGrpId = applicationGroupDto.getId();
                        List<ApplicationDto> applicationDtos = applicationClient.getAppDtosByAppGrpId(appGrpId).getEntity();
                        boolean grpLic = applicationDtos.get(0).isGrpLic();
                        if (grpLic) {
                            Set<String> statusSet = IaisCommonUtils.genNewHashSet();
                            for (ApplicationDto applicationDto : applicationDtos) {
                                String status = applicationDto.getStatus();
                                statusSet.add(status);
                            }
                            if (statusSet.size() == 1 && statusSet.contains(ApplicationConsts.APPLICATION_STATUS_CESSATION_NOT_LICENCE)) {
                                String originLicenceId = applicationDtos.get(0).getOriginLicenceId();
                                LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(originLicenceId).getEntity();
                                licenceDtos.add(licenceDto);
                                continue;
                            }
                            for (ApplicationDto applicationDto : applicationDtos) {
                                try {
                                    String appId = applicationDto.getId();
                                    String status = applicationDto.getStatus();
                                    AppPremiseMiscDto appPremiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(appId).getEntity();
                                    if (appPremiseMiscDto != null && (ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE.equals(status) || ApplicationConsts.APPLICATION_STATUS_CESSATION_TEMPORARY_LICENCE.equals(status))) {
                                        Date effectiveDate = appPremiseMiscDto.getEffectiveDate();
                                        if (effectiveDate.compareTo(date) <= 0) {
                                            String originLicenceId = applicationDto.getOriginLicenceId();
                                            LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(originLicenceId).getEntity();
                                            if (!licenceDtos.contains(licenceDto)) {
                                                licenceDtos.add(licenceDto);
                                                applicationGroupDtosCesead.add(applicationGroupDto);
                                            }
                                            break;
                                        }
                                    }
                                } catch (Exception e) {
                                    log.error(e.getMessage(), e);
                                    continue;
                                }
                            }
                            //create grp licence and ceased old licence
                            List<String> grpLicIds = IaisCommonUtils.genNewArrayList();
                            grpLicIds.add(appGrpId);
                            List<ApplicationLicenceDto> applicationLicenceDtos = applicationClient.getCessGroup(grpLicIds).getEntity();
                            List<String> serviceIds = licenceApproveBatchjob.getAllServiceId(applicationLicenceDtos);
                            List<HcsaServiceDto> hcsaServiceDtos = licenceService.getHcsaServiceById(serviceIds);
                            if (hcsaServiceDtos == null || hcsaServiceDtos.size() == 0) {
                                log.error(StringUtil.changeForLog("This serviceIds can not get the HcsaServiceDto -->:" + serviceIds));
                                continue;
                            }
                            for (ApplicationLicenceDto applicationLicenceDto : applicationLicenceDtos) {
                                List<ApplicationListDto> newApplicationListDtoLists = IaisCommonUtils.genNewArrayList();
                                List<ApplicationListDto> oldApplicationListDtoList = applicationLicenceDto.getApplicationListDtoList();
                                for (ApplicationListDto applicationListDto : oldApplicationListDtoList) {
                                    ApplicationDto applicationDto = applicationListDto.getApplicationDto();
                                    if (applicationDto.isNeedNewLicNo()) {
                                        newApplicationListDtoLists.add(applicationListDto);
                                    }
                                }
                                log.info(StringUtil.changeForLog("====================newApplicationListDtoLists  size==================" + newApplicationListDtoLists.size()));
                                applicationLicenceDto.setApplicationListDtoList(newApplicationListDtoLists);
                                LicenceApproveBatchjob.GenerateResult groupGenerateResult = licenceApproveBatchjob.generateGroupLicence(applicationLicenceDto, hcsaServiceDtos);
                                licenceApproveBatchjob.createCessLicence(applicationGroupDto, null, groupGenerateResult);
                            }
                        } else {
                            for (ApplicationDto applicationDto : applicationDtos) {
                                String appId = applicationDto.getId();
                                String originLicenceId = applicationDto.getOriginLicenceId();
                                AppPremiseMiscDto appPremiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(appId).getEntity();
                                if (appPremiseMiscDto != null) {
                                    Date effectiveDate = appPremiseMiscDto.getEffectiveDate();
                                    if (effectiveDate.compareTo(date) <= 0) {
                                        LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(originLicenceId).getEntity();
                                        String status = licenceDto.getStatus();
                                        if (ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(status)) {
                                            applicationGroupDtosCesead.add(applicationGroupDto);
                                            licenceDtos.add(licenceDto);
                                            updateLicenceStatusAndSendMails(licenceDto, date);
                                            String svcName = licenceDto.getSvcName();
                                            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(svcName);
                                            String svcType = hcsaServiceDto.getSvcType();
                                            List<LicenceDto> specLicenceDto = null;
                                            if (ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(svcType)) {
                                                List<String> specLicIds = hcsaLicenceClient.getSpecIdsByBaseId(originLicenceId).getEntity();
                                                if (specLicIds != null && !specLicIds.isEmpty()) {
                                                    specLicenceDto = hcsaLicenceClient.retrieveLicenceDtos(specLicIds).getEntity();
                                                    updateLicencesStatusAndSendMails(specLicenceDto, date);
                                                }
                                            }
                                            if (!IaisCommonUtils.isEmpty(specLicenceDto)) {
                                                licenceDtos.addAll(specLicenceDto);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        continue;
                    }
                }
                try {
                    //update and send email
                    log.info(StringUtil.changeForLog("====================licenceDtos  size==================" + licenceDtos.size()));
                    if (!IaisCommonUtils.isEmpty(licenceDtos)) {
                        for (LicenceDto licenceDto : licenceDtos) {
                            String id = licenceDto.getId();
                            log.info(StringUtil.changeForLog("====================licence id ==================" + id));
                        }
                    }
                    updateLicencesStatusAndSendMails(licenceDtos, date);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                //update appGrp
                if (!IaisCommonUtils.isEmpty(applicationGroupDtosCesead)) {
                    for (ApplicationGroupDto applicationGroupDto : applicationGroupDtosCesead) {
                        String id = applicationGroupDto.getId();
                        log.info(StringUtil.changeForLog("====================licence id ==================" + id));
                    }
                }
                updateAppGroups(applicationGroupDtosCesead);
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }

    private void updateLicencesStatusAndSendMails(List<LicenceDto> licenceDtos, Date date) {
        List<LicenceDto> updateLicenceDtos = IaisCommonUtils.genNewArrayList();
        AuditTrailDto auditTrailDto = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        for (LicenceDto licenceDto : licenceDtos) {
            try {
                licenceDto.setAuditTrailDto(auditTrailDto);
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
                licenceDto.setEndDate(date);
                updateLicenceDtos.add(licenceDto);
                String svcName = licenceDto.getSvcName();
                String licenseeId = licenceDto.getLicenseeId();
                String licenceNo = licenceDto.getLicenceNo();
                String id = licenceDto.getId();
                Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                LicenseeDto licenseeDto = inspEmailService.getLicenseeDtoById(licenseeId);
                String applicantName = licenseeDto.getName();
                emailMap.put("ApplicantName", applicantName);
                emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.LICENCE_STATUS_CEASED}).get(0).getText());
                emailMap.put("ServiceLicenceName", svcName);
                emailMap.put("ApplicationNumber", licenceNo);
                emailMap.put("CessationDate", Formatter.formatDateTime(date));
                emailMap.put("email", systemParamConfig.getSystemAddressOne());
                emailMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE);
                emailParam.setTemplateContent(emailMap);
                emailParam.setQueryCode(licenceNo);
                emailParam.setReqRefNum(licenceNo);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                emailParam.setRefId(id);
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE);
                map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.LICENCE_STATUS_CEASED}).get(0).getText());
                map.put("ApplicationNumber", licenceNo);
                String subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                emailParam.setSubject(subject);
                //email
                notificationHelper.sendNotification(emailParam);
                //sms
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE_SMS);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
                notificationHelper.sendNotification(emailParam);
                //msg
                HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(svcName).getEntity();
                List<String> svcCode = IaisCommonUtils.genNewArrayList();
                svcCode.add(svcDto.getSvcCode());
                emailParam.setSvcCodeList(svcCode);
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE_MSG);
                emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                emailParam.setRefId(licenceDto.getId());
                //notificationHelper.sendNotification(emailParam);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                continue;
            }
        }
        if (!IaisCommonUtils.isEmpty(updateLicenceDtos)) {
            hcsaLicenceClient.updateLicences(updateLicenceDtos).getEntity();
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            gatewayClient.updateFeLicDto(updateLicenceDtos, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
        }
    }

    private void updateLicenceStatusAndSendMails(LicenceDto licenceDto, Date date) {
        if (licenceDto == null) {
            return;
        }
        AuditTrailDto auditTrailDto = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        licenceDto.setAuditTrailDto(auditTrailDto);
        licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
        licenceDto.setEndDate(date);
        String svcName = licenceDto.getSvcName();
        String licenseeId = licenceDto.getLicenseeId();
        String licenceNo = licenceDto.getLicenceNo();
        String id = licenceDto.getId();
        List<LicenceDto> licenceDtos = IaisCommonUtils.genNewArrayList();
        licenceDtos.add(licenceDto);
        hcsaLicenceClient.updateLicences(licenceDtos);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        try {
            gatewayClient.updateFeLicDto(licenceDtos, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
            Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
            LicenseeDto licenseeDto = inspEmailService.getLicenseeDtoById(licenseeId);
            String applicantName = licenseeDto.getName();
            emailMap.put("ApplicantName", applicantName);
            emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.LICENCE_STATUS_CEASED}).get(0).getText());
            emailMap.put("ServiceLicenceName", svcName);
            emailMap.put("ApplicationNumber", licenceNo);
            emailMap.put("CessationDate", Formatter.formatDateTime(date));
            emailMap.put("email", systemParamConfig.getSystemAddressOne());
            emailMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE);
            emailParam.setTemplateContent(emailMap);
            emailParam.setQueryCode(licenceNo);
            emailParam.setReqRefNum(licenceNo);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
            emailParam.setRefId(id);
            Map<String, Object> map = IaisCommonUtils.genNewHashMap();
            InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE);
            map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.LICENCE_STATUS_CEASED}).get(0).getText());
            map.put("ApplicationNumber", licenceNo);
            String subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
            emailParam.setSubject(subject);
            //email
            notificationHelper.sendNotification(emailParam);
            //sms
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE_SMS);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
            notificationHelper.sendNotification(emailParam);
            //msg
            HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(svcName).getEntity();
            List<String> svcCode = IaisCommonUtils.genNewArrayList();
            svcCode.add(svcDto.getSvcCode());
            emailParam.setSvcCodeList(svcCode);
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE_MSG);
            emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            emailParam.setRefId(licenceDto.getId());
            notificationHelper.sendNotification(emailParam);
            //cessationBeService.sendEmail(EFFECTIVEDATAEQUALDATA, date, svcName, id, licenseeId, licenceNo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void updateAppGroups(List<ApplicationGroupDto> applicationGroupDtos) {
        if (IaisCommonUtils.isEmpty(applicationGroupDtos)) {
            return;
        }
        AuditTrailDto auditTrailDto = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        for (ApplicationGroupDto applicationGroupDto : applicationGroupDtos) {
            applicationGroupDto.setAuditTrailDto(auditTrailDto);
            applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_LICENCE_GENERATED);
        }
        applicationClient.updateApplications(applicationGroupDtos);
    }
}
