package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
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
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author weilu
 * @date 2020/2/13 9:42
 * Check for Cessation Effective Date  when cessation date == new date make licence inactive
 */
@Delegator("CessationEffectiveDateBatchjob")
@Slf4j
public class CessationEffectiveDateBatchjob {
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private OrganizationClient organizationClient;
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

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("The CessationEffectiveDateBatchjob is start ..."));
    }

    public void doBatchJob(BaseProcessClass bpc) {
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        jobExecute();
    }

    public void jobExecute() {
        {
            Date date = new Date();
            //licence
            log.debug(StringUtil.changeForLog("The CessationLicenceBatchJob is doBatchJob ..."));
            Set<LicenceDto> licenceDtos = IaisCommonUtils.genNewHashSet();
            List<String> filterLicenceId = IaisCommonUtils.genNewArrayList();
            Map<String, String> licGrpMap = IaisCommonUtils.genNewHashMap();
            List<ApplicationGroupDto> applicationGroupDtos = cessationClient.listAppGrpForCess().getEntity();
            List<ApplicationGroupDto> applicationGroupDtosCesead = IaisCommonUtils.genNewArrayList();
            if (!IaisCommonUtils.isEmpty(applicationGroupDtos)) {
                for (ApplicationGroupDto applicationGroupDto : applicationGroupDtos) {
                    try {
                        String appGrpId = applicationGroupDto.getId();
                        log.info(StringUtil.changeForLog("====================appGrpId==================" + appGrpId));
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
                                if (licenceDto != null && ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceDto.getStatus())) {
                                    if (!filterLicenceId.contains(licenceDto.getId())) {
                                        licenceDtos.add(licenceDto);
                                        filterLicenceId.add(licenceDto.getId());
                                        licGrpMap.put(originLicenceId, appGrpId);
                                    }
                                }
                                continue;
                            }
                            for (ApplicationDto applicationDto : applicationDtos) {
                                try {
                                    String appId = applicationDto.getId();
                                    String status = applicationDto.getStatus();
                                    AppPremiseMiscDto appPremiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(appId).getEntity();
                                    if ((ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE.equals(status) || ApplicationConsts.APPLICATION_STATUS_CESSATION_TEMPORARY_LICENCE.equals(status))) {
                                        if (appPremiseMiscDto != null) {
                                            Date effectiveDate = appPremiseMiscDto.getEffectiveDate();
                                            if (effectiveDate.compareTo(date) <= 0) {
                                                String originLicenceId = applicationDto.getOriginLicenceId();
                                                LicenceDto licenceDto = hcsaLicenceClient.getLicenceDtoById(originLicenceId).getEntity();
                                                if (licenceDto != null) {
                                                    if (!filterLicenceId.contains(licenceDto.getId())) {
                                                        filterLicenceId.add(licenceDto.getId());
                                                        licenceDtos.add(licenceDto);
                                                        applicationGroupDtosCesead.add(applicationGroupDto);
                                                        licGrpMap.put(originLicenceId, appGrpId);
                                                    }
                                                }
                                                break;
                                            }
                                        } else {
                                            String originLicenceId = applicationDto.getOriginLicenceId();
                                            LicenceDto licenceDto = hcsaLicenceClient.getLicenceDtoById(originLicenceId).getEntity();
                                            if (licenceDto != null) {
                                                if (!filterLicenceId.contains(licenceDto.getId())) {
                                                    filterLicenceId.add(licenceDto.getId());
                                                    licenceDtos.add(licenceDto);
                                                    applicationGroupDtosCesead.add(applicationGroupDto);
                                                    licGrpMap.put(originLicenceId, appGrpId);
                                                    break;
                                                }
                                            }
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
                                            licGrpMap.put(originLicenceId, appGrpId);
//                                            String svcName = licenceDto.getSvcName();
//                                            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(svcName);
//                                            String svcType = hcsaServiceDto.getSvcType();
//                                            List<LicenceDto> specLicenceDto;
//                                            if (ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(svcType)) {
//                                                List<String> specLicIds = hcsaLicenceClient.getActSpecIdByActBaseId(originLicenceId).getEntity();
//                                                if (specLicIds != null && !specLicIds.isEmpty()) {
//                                                    specLicenceDto = hcsaLicenceClient.retrieveLicenceDtos(specLicIds).getEntity();
//                                                    updateLicence(specLicenceDto, date);
//                                                }
//                                            }
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
                    updateLicencesStatusAndSendMails(licenceDtos, date, licGrpMap);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                //update appGrp
                updateAppGroups(applicationGroupDtosCesead);
            }
        }
    }

    private void updateLicencesStatusAndSendMails(Set<LicenceDto> licenceDtos, Date date, Map<String, String> licGrpMap) {
        List<LicenceDto> updateLicenceDtos = IaisCommonUtils.genNewArrayList();
        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
        List<String> specLicIdsAll = IaisCommonUtils.genNewArrayList();
        for (LicenceDto licenceDto : licenceDtos) {
            String licId = licenceDto.getId();
            List<String> specLicIds = hcsaLicenceClient.getActSpecIdByActBaseId(licId).getEntity();
            if (!IaisCommonUtils.isEmpty(specLicIds)) {
                specLicIdsAll.addAll(specLicIds);
            }
        }
        for (LicenceDto licenceDto : licenceDtos) {
            if (licenceDto == null) {
                continue;
            }
            try {
                List<String> serviceCodes = IaisCommonUtils.genNewArrayList();
                licenceDto.setAuditTrailDto(auditTrailDto);
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
                licenceDto.setEndDate(date);
                updateLicenceDtos.add(licenceDto);
                if (specLicIdsAll.contains(licenceDto.getId())) {
                    continue;
                }
                String svcName = licenceDto.getSvcName();
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(svcName);
                serviceCodes.add(hcsaServiceDto.getSvcCode());
                String licenceNo = licenceDto.getLicenceNo();
                String licId = licenceDto.getId();
                Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                StringBuilder svcNameLicNo = new StringBuilder();
                svcNameLicNo.append(svcName).append(" : ").append(licenceNo);
                List<String> specLicIds = hcsaLicenceClient.getActSpecIdByActBaseId(licId).getEntity();
                if (!IaisCommonUtils.isEmpty(specLicIds)) {
                    for (String specLicId : specLicIds) {
                        LicenceDto specLicDto = hcsaLicenceClient.getLicDtoById(specLicId).getEntity();
                        String svcName1 = specLicDto.getSvcName();
                        String licenceNo1 = specLicDto.getLicenceNo();
                        serviceCodes.add("<br/>");
                        svcNameLicNo.append("<br/>");
                        svcNameLicNo.append(svcName1).append(" : ").append(licenceNo1);
                        HcsaServiceDto hcsaServiceDto1 = HcsaServiceCacheHelper.getServiceByServiceName(svcName1);
                        serviceCodes.add(hcsaServiceDto1.getSvcCode());
                    }
                }
                String groupId = licGrpMap.get(licId);
                StringBuilder appNos = new StringBuilder();
                OrgUserDto orgUserDto = null;
                if (!StringUtil.isEmpty(groupId)) {
                    ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(groupId).getEntity();
                    String groupNo = applicationGroupDto.getGroupNo();
                    groupNo = groupNo + "-01";
                    boolean isBeCessationFlow = false;
                    List<AppPremisesRoutingHistoryDto> temp = applicationClient.getHistoryByAppNoAndDecision(groupNo, ApplicationConsts.APPLICATION_STATUS_CESSATION_BE_DECISION).getEntity();
                    if (!IaisCommonUtils.isEmpty(temp) && temp.size() < 2) {
                        isBeCessationFlow = true;
                    }
                    if(isBeCessationFlow){
                        String userId = applicationClient.getUserIdBylicenseeId(applicationGroupDto.getLicenseeId()).getEntity();
                        orgUserDto = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
                    }else {
                        orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                    }
                    List<ApplicationDto> applicationDtos = applicationClient.getAppDtosByAppGrpId(groupId).getEntity();
                    for (ApplicationDto applicationDto : applicationDtos) {
                        String applicationNo = applicationDto.getApplicationNo();
                        String status = applicationDto.getStatus();
                        if (ApplicationConsts.APPLICATION_STATUS_CESSATION_NOT_LICENCE.equals(status)) {
                            appNos.append(" ");
                            appNos.append(applicationNo);
                        }
                    }
                }
                if (orgUserDto != null) {
                    emailMap.put("ApplicantName", orgUserDto.getDisplayName());
                } else {
                    emailMap.put("ApplicantName", "");
                    log.info(StringUtil.changeForLog("updateLicencesStatusAndSendMails Function: orgUserDto is null------->LicenceId =" + licenceDto.getId()));
                }
                emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.APPLICATION_TYPE_CESSATION}).get(0).getText());
                emailMap.put("ServiceLicenceName", svcNameLicNo.toString());
                emailMap.put("ApplicationNumber", appNos.toString());
                emailMap.put("CessationDate", DateFormatUtils.format(date, "dd/MM/yyyy"));
                emailMap.put("email", systemParamConfig.getSystemAddressOne());
                emailMap.put("MOH_AGENCY_NAM_GROUP", "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "</b>");
                emailMap.put("MOH_AGENCY_NAME", "<b>" + AppConsts.MOH_AGENCY_NAME + "</b>");
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE);
                emailParam.setTemplateContent(emailMap);
                emailParam.setQueryCode(licenceNo);
                emailParam.setReqRefNum(licenceNo);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                emailParam.setRefId(licId);
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE);
                map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.APPLICATION_TYPE_CESSATION}).get(0).getText());
                map.put("ApplicationNumber", appNos.toString());
                String subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                emailParam.setSubject(subject);
                //email
                notificationHelper.sendNotification(emailParam);
                //sms
                rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE_SMS);
                subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                emailParam.setSubject(subject);
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE_SMS);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
                notificationHelper.sendNotification(emailParam);
                //msg
                rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE_MSG);
                subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                emailParam.setSubject(subject);
                emailParam.setSvcCodeList(serviceCodes);
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE_MSG);
                emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                emailParam.setRefId(licenceDto.getId());
                notificationHelper.sendNotification(emailParam);
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

    private void updateLicence(List<LicenceDto> licenceDtos, Date date) {
        if (!IaisCommonUtils.isEmpty(licenceDtos)) {
            AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
            for (LicenceDto licenceDto : licenceDtos) {
                licenceDto.setAuditTrailDto(auditTrailDto);
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
                licenceDto.setEndDate(date);
            }
            hcsaLicenceClient.updateLicences(licenceDtos).getEntity();
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            gatewayClient.updateFeLicDto(licenceDtos, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
        }
    }

    private void updateAppGroups(List<ApplicationGroupDto> applicationGroupDtos) {
        if (IaisCommonUtils.isEmpty(applicationGroupDtos)) {
            return;
        }
        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
        for (ApplicationGroupDto applicationGroupDto : applicationGroupDtos) {
            applicationGroupDto.setAuditTrailDto(auditTrailDto);
            applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_LICENCE_GENERATED);
        }
        applicationClient.updateApplications(applicationGroupDtos);
    }
}
