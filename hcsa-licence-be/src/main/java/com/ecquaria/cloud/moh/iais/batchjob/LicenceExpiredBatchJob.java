package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.CessationBeService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author weilu
 * @Date 2020/4/27 8:27
 */
@Delegator("licenceExpiredBatchJob")
@Slf4j
public class LicenceExpiredBatchJob {
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private CessationBeService cessationBeService;
    @Autowired
    ApplicationClient applicationClient;
    @Autowired
    private BeEicGatewayClient gatewayClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;

    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Autowired
    SystemParamConfig systemParamConfig;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    HcsaConfigClient hcsaConfigClient;
    @Autowired
    OrganizationClient organizationClient;

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("The licenceExpiredBatchJob is start ..."));
    }

    public void doBatchJob(BaseProcessClass bpc) {
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        jobExecute();
    }

    public void jobExecute() {
        //licence
        log.debug(StringUtil.changeForLog("The CessationLicenceBatchJob is doBatchJob ..."));
        Date date = new Date();
        String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
        //get expired date + 1 = today de licence
        List<LicenceDto> licenceDtos = hcsaLicenceClient.cessationLicenceDtos(ApplicationConsts.LICENCE_STATUS_ACTIVE,
                dateStr).getEntity();
        List<LicenceDto> licenceDtosForSave = IaisCommonUtils.genNewArrayList();
        List<String> ids = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(licenceDtos)) {
            for (LicenceDto licenceDto : licenceDtos) {
                //shi fou you qi ta de app zai zuo
                String licId = licenceDto.getId();
                ids.clear();
                ids.add(licId);
                Map<String, Boolean> stringBooleanMap = cessationBeService.listResultCeased(ids);
                if (stringBooleanMap.get(licId)) {
                    licenceDtosForSave.add(licenceDto);
                }
            }
            updateLicenceStatus(licenceDtosForSave, date);
        }
        //effective Date
        List<LicenceDto> effectLicDtos = hcsaLicenceClient.getLicByEffDate().getEntity();
        if (!IaisCommonUtils.isEmpty(effectLicDtos)) {
            updateLicenceStatusEffect(effectLicDtos, date);
        }
        //approved licence
        List<LicenceDto> approvedLicence = hcsaLicenceClient.getLicDtosWithApproved().getEntity();
        if (!IaisCommonUtils.isEmpty(approvedLicence)) {
            for (LicenceDto licenceDto : approvedLicence) {
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_ACTIVE);
            }
            updateLicenceStatusApproved(approvedLicence);
        }

    }

    private void updateLicenceStatus(List<LicenceDto> licenceDtos, Date date) {
        log.info(StringUtil.changeForLog("The updateLicenceStatus start ..."));
        List<LicenceDto> updateLicenceDtos = IaisCommonUtils.genNewArrayList();
        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
        for (LicenceDto licenceDto : licenceDtos) {
            try {
            List<String> serviceCodes = IaisCommonUtils.genNewArrayList();
            licenceDto.setAuditTrailDto(auditTrailDto);
            String licId = licenceDto.getId();
            String svcName = licenceDto.getSvcName();
            String licenceNo = licenceDto.getLicenceNo();
            log.info(StringUtil.changeForLog("The updateLicenceStatus  licenceNo is -->:"+licenceNo));
            StringBuilder svcNameLicNo = new StringBuilder();
            svcNameLicNo.append(svcName).append(" : ").append(licenceNo);
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(svcName);
            serviceCodes.add(hcsaServiceDto.getSvcCode());
            List<String> specLicIds = hcsaLicenceClient.getActSpecIdByActBaseId(licId).getEntity();
            if(!IaisCommonUtils.isEmpty(specLicIds)){
                for(String specLicId :specLicIds){
                    LicenceDto specLicDto = hcsaLicenceClient.getLicDtoById(specLicId).getEntity();
                    String svcName1 = specLicDto.getSvcName();
                    String licenceNo1 = specLicDto.getLicenceNo();
                    svcNameLicNo.append(svcName1).append(" : ").append(licenceNo1);
                    HcsaServiceDto hcsaServiceDto1 = HcsaServiceCacheHelper.getServiceByServiceName(svcName1);
                    serviceCodes.add(hcsaServiceDto1.getSvcCode());
                }
            }

            //pan daun shi dou you xin de licence sheng cheng
            LicenceDto newLicDto = hcsaLicenceClient.getLicdtoByOrgId(licId).getEntity();
            if (newLicDto == null) {
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_LAPSED);
            } else {
                if (ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(newLicDto.getStatus())) {
                    licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_EXPIRY);
                } else if (ApplicationConsts.LICENCE_STATUS_APPROVED.equals(newLicDto.getStatus())) {
                    licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_EXPIRY);
                    newLicDto.setStatus(ApplicationConsts.LICENCE_STATUS_ACTIVE);
                    updateLicenceDtos.add(newLicDto);
                } else {
                    licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_LAPSED);
                }
            }
            licenceDto.setEndDate(date);
            updateLicenceDtos.add(licenceDto);

                Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                String appId= hcsaLicenceClient.getLicCorrBylicId(licId).getEntity().get(0).getApplicationId();
                ApplicationDto applicationDto=applicationClient.getApplicationById(appId).getEntity();
                ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();
                if (applicationGroupDto != null){
                    OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                    if (orgUserDto != null){
                        emailMap.put("ApplicantName", orgUserDto.getDisplayName());
                    }
                }
                emailMap.put("ServiceLicenceName", svcNameLicNo.toString());
                emailMap.put("CessationDate", DateFormatUtils.format(date,"dd/MM/yyyy"));
                emailMap.put("email", systemParamConfig.getSystemAddressOne());
                emailMap.put("MOH_AGENCY_NAM_GROUP", "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "</b>");
                emailMap.put("MOH_AGENCY_NAME", "<b>" + AppConsts.MOH_AGENCY_NAME + "</b>");
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE);
                emailParam.setTemplateContent(emailMap);
                emailParam.setQueryCode(licenceNo);
                emailParam.setReqRefNum(licenceNo);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                emailParam.setRefId(licId);
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE);
                map.put("ServiceLicenceName", svcName);
                map.put("LicenceNumber", licenceNo);
                String subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                emailParam.setSubject(subject);
                //email
                notificationHelper.sendNotification(emailParam);
                //sms
                EmailParam smsParam = new EmailParam();
                smsParam.setQueryCode(licenceNo);
                smsParam.setReqRefNum(licenceNo);
                smsParam.setRefId(licId);
                smsParam.setTemplateContent(emailMap);
                rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_SMS);
                subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                smsParam.setSubject(subject);
                smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_SMS);
                smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
                notificationHelper.sendNotification(smsParam);
                //msg
                rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_MSG);
                subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                EmailParam msgParam = new EmailParam();
                msgParam.setQueryCode(licenceNo);
                msgParam.setReqRefNum(licenceNo);
                msgParam.setRefId(licId);
                msgParam.setTemplateContent(emailMap);
                msgParam.setSubject(subject);
                msgParam.setSvcCodeList(serviceCodes);
                msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_MSG);
                msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                msgParam.setRefId(licenceDto.getId());
                notificationHelper.sendNotification(msgParam);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("The error LicenceNo -->:"+licenceDto.getLicenceNo()));
                log.error(e.getMessage(), e);
            }
        }
        try {
            hcsaLicenceClient.updateLicences(updateLicenceDtos).getEntity();
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            gatewayClient.updateFeLicDto(updateLicenceDtos, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info(StringUtil.changeForLog("The updateLicenceStatus end ..."));
    }

    private void updateLicenceStatusEffect(List<LicenceDto> licenceDtos, Date date) {
        List<LicenceDto> updateLicenceDtos = IaisCommonUtils.genNewArrayList();
        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
        for (LicenceDto licenceDto : licenceDtos) {
            try {
                licenceDto.setAuditTrailDto(auditTrailDto);
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_ACTIVE);
                updateLicenceDtos.add(licenceDto);

                String originLicenceId = licenceDto.getOriginLicenceId();
                LicenceDto interimLicDto = hcsaLicenceClient.getLicDtoById(originLicenceId).getEntity();

                while(interimLicDto != null && (ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(interimLicDto.getStatus())
                        ||ApplicationConsts.LICENCE_STATUS_APPROVED.equals(interimLicDto.getStatus()))){
                    interimLicDto.setStatus(ApplicationConsts.LICENCE_STATUS_IACTIVE);
                    interimLicDto.setEndDate(date);
                    updateLicenceDtos.add(interimLicDto);
                    interimLicDto = hcsaLicenceClient.getLicDtoById(interimLicDto.getOriginLicenceId()).getEntity();
                }
                //send email
                String licenceDtoId = licenceDto.getId();
                String svcName = licenceDto.getSvcName();
                String licenceNo = licenceDto.getLicenceNo();

                Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                String appId= hcsaLicenceClient.getLicCorrBylicId(licenceDtoId).getEntity().get(0).getApplicationId();
                ApplicationDto applicationDto=applicationClient.getApplicationById(appId).getEntity();
                ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();
                if (applicationGroupDto != null){
                    OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                    if (orgUserDto != null){
                        emailMap.put("ApplicantName", orgUserDto.getDisplayName());
                    }
                }
                emailMap.put("ServiceLicenceName", svcName);
                emailMap.put("LicenceNumber", licenceNo);
                emailMap.put("CessationDate", Formatter.formatDateTime(date));
                emailMap.put("email", systemParamConfig.getSystemAddressOne());
                emailMap.put("MOH_AGENCY_NAM_GROUP", "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "</b>");
                emailMap.put("MOH_AGENCY_NAME", "<b>" + AppConsts.MOH_AGENCY_NAME + "</b>");
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE);
                emailParam.setTemplateContent(emailMap);
                emailParam.setQueryCode(licenceNo);
                emailParam.setReqRefNum(licenceNo);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                emailParam.setRefId(licenceDtoId);
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE);
                map.put("ServiceLicenceName", svcName);
                map.put("LicenceNumber", licenceNo);
                String subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                emailParam.setSubject(subject);
                //email
                notificationHelper.sendNotification(emailParam);
                //sms
                rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_SMS);
                subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                EmailParam smsParam = new EmailParam();
                smsParam.setQueryCode(licenceNo);
                smsParam.setReqRefNum(licenceNo);
                smsParam.setRefId(licenceDtoId);
                smsParam.setTemplateContent(emailMap);
                smsParam.setSubject(subject);
                smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_SMS);
                smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
                notificationHelper.sendNotification(smsParam);
                //msg
                rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_MSG);
                subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                EmailParam msgParam = new EmailParam();
                msgParam.setQueryCode(licenceNo);
                msgParam.setReqRefNum(licenceNo);
                msgParam.setRefId(licenceDtoId);
                msgParam.setTemplateContent(emailMap);
                msgParam.setSubject(subject);
                HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(svcName).getEntity();
                List<String> svcCode = IaisCommonUtils.genNewArrayList();
                svcCode.add(svcDto.getSvcCode());
                msgParam.setSvcCodeList(svcCode);
                msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_MSG);
                msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                msgParam.setRefId(licenceDto.getId());
                notificationHelper.sendNotification(msgParam);
                //cessationBeService.sendEmail(LICENCEENDDATE, date, svcName, licenceDtoId, licenseeId, licenceNo);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                continue;
            }
        }
        try {
            hcsaLicenceClient.updateLicences(updateLicenceDtos).getEntity();
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            gatewayClient.updateFeLicDto(updateLicenceDtos, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private void updateLicenceStatusApproved(List<LicenceDto> licenceDtos) {
        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
        for (LicenceDto licenceDto : licenceDtos) {
            licenceDto.setAuditTrailDto(auditTrailDto);
            String licId = licenceDto.getId();
            String svcName = licenceDto.getSvcName();
            String licenceNo = licenceDto.getLicenceNo();
            try {
                Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                String appId= hcsaLicenceClient.getLicCorrBylicId(licId).getEntity().get(0).getApplicationId();
                ApplicationDto applicationDto=applicationClient.getApplicationById(appId).getEntity();
                ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();
                if (applicationGroupDto != null){
                    OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                    if (orgUserDto != null){
                        emailMap.put("ApplicantName", orgUserDto.getDisplayName());
                    }
                }
                emailMap.put("ServiceLicenceName", svcName);
                emailMap.put("LicenceNumber", licenceNo);
                emailMap.put("CessationDate", Formatter.formatDate(new Date()));
                emailMap.put("email", systemParamConfig.getSystemAddressOne());
                emailMap.put("MOH_AGENCY_NAM_GROUP", "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "</b>");
                emailMap.put("MOH_AGENCY_NAME", "<b>" + AppConsts.MOH_AGENCY_NAME + "</b>");
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE);
                emailParam.setTemplateContent(emailMap);
                emailParam.setQueryCode(licenceNo);
                emailParam.setReqRefNum(licenceNo);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                emailParam.setRefId(licId);
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE);
                map.put("ServiceLicenceName", svcName);
                map.put("LicenceNumber", licenceNo);
                String subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                emailParam.setSubject(subject);
                //email
                notificationHelper.sendNotification(emailParam);
                //sms
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_SMS);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
                notificationHelper.sendNotification(emailParam);
                //msg
                HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(svcName).getEntity();
                List<String> svcCode = IaisCommonUtils.genNewArrayList();
                svcCode.add(svcDto.getSvcCode());
                emailParam.setSvcCodeList(svcCode);
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_MSG);
                emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                emailParam.setRefId(licenceDto.getId());
                notificationHelper.sendNotification(emailParam);

                //cessationBeService.sendEmail(LICENCEENDDATE, date, svcName, licId, licenseeId, licenceNo);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        try {
            hcsaLicenceClient.updateLicences(licenceDtos).getEntity();
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            gatewayClient.updateFeLicDto(licenceDtos, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
}
