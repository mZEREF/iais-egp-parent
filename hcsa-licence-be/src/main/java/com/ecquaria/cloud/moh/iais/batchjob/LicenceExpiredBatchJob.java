package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.CessationBeService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
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

    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    private NotificationHelper notificationHelper;
    private final String LICENCEENDDATE = "52AD8B3B-E652-EA11-BE7F-000C29F371DC";

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("The licenceExpiredBatchJob is start ..."));
    }

    public void doBatchJob(BaseProcessClass bpc) throws Exception {
        //licence
        log.debug(StringUtil.changeForLog("The CessationLicenceBatchJob is doBatchJob ..."));
        Date date = new Date();
        String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
        String status = ApplicationConsts.LICENCE_STATUS_ACTIVE;
        //get expired date + 1 = today de licence
        List<LicenceDto> licenceDtos = hcsaLicenceClient.cessationLicenceDtos(status, dateStr).getEntity();
        List<LicenceDto> licenceDtosForSave = IaisCommonUtils.genNewArrayList();
        List<String> ids = IaisCommonUtils.genNewArrayList();
        if (licenceDtos != null && !licenceDtos.isEmpty()) {
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
    }

    private void updateLicenceStatus(List<LicenceDto> licenceDtos, Date date) {
        List<LicenceDto> updateLicenceDtos = IaisCommonUtils.genNewArrayList();
        for (LicenceDto licenceDto : licenceDtos) {
            String licId = licenceDto.getId();
            String svcName = licenceDto.getSvcName();
            String licenceNo = licenceDto.getLicenceNo();
            String licenseeId = licenceDto.getLicenseeId();
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
            try {
                Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
                String applicantName=licenseeDto.getName();
                emailMap.put("ApplicantName", applicantName);
                emailMap.put("ServiceLicenceName", svcName);
                emailMap.put("LicenceNumber", licenceNo);
                emailMap.put("CessationDate", Formatter.formatDateTime(date));
                emailMap.put("email", "");
                emailMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE);
                emailParam.setTemplateContent(emailMap);
                emailParam.setQueryCode(licenceNo);
                emailParam.setReqRefNum(licenceNo);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                emailParam.setRefId(licId);
                Map<String,Object> map=IaisCommonUtils.genNewHashMap();
                InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE);
                map.put("ServiceLicenceName", svcName);
                map.put("LicenceNumber", licenceNo);
                String subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
                emailParam.setSubject(subject);
                //email
                notificationHelper.sendNotification(emailParam);
                //sms
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_SMS);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
                notificationHelper.sendNotification(emailParam);
                //msg
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_MSG);
                emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_ACTION_REQUIRED);
                List<LicAppCorrelationDto> licAppCorrelationDtos=hcsaLicenceClient.getLicCorrBylicId(licId).getEntity();
                ApplicationDto applicationDto=applicationClient.getApplicationById(licAppCorrelationDtos.get(0).getApplicationId()).getEntity();
                emailParam.setRefId(applicationDto.getApplicationNo());
                notificationHelper.sendNotification(emailParam);

                //cessationBeService.sendEmail(LICENCEENDDATE, date, svcName, licId, licenseeId, licenceNo);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        try {
            hcsaLicenceClient.updateLicences(updateLicenceDtos).getEntity();
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            gatewayClient.updateFeLicDto(updateLicenceDtos, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

    }

    private void updateLicenceStatusEffect(List<LicenceDto> licenceDtos, Date date) {
        List<LicenceDto> updateLicenceDtos = IaisCommonUtils.genNewArrayList();
        for (LicenceDto licenceDto : licenceDtos) {
            String originLicenceId = licenceDto.getOriginLicenceId();
            LicenceDto interimLicDto = hcsaLicenceClient.getLicDtoById(originLicenceId).getEntity();
            interimLicDto.setStatus(ApplicationConsts.LICENCE_STATUS_IACTIVE);
            interimLicDto.setEndDate(date);
            licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_ACTIVE);
            updateLicenceDtos.add(licenceDto);
            updateLicenceDtos.add(interimLicDto);
            //send email
            String licenceDtoId = licenceDto.getId();
            String svcName = licenceDto.getSvcName();
            String licenceNo = licenceDto.getLicenceNo();
            String licenseeId = licenceDto.getLicenseeId();
            try {
                Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
                String applicantName=licenseeDto.getName();
                emailMap.put("ApplicantName", applicantName);
                emailMap.put("ServiceLicenceName", svcName);
                emailMap.put("LicenceNumber", licenceNo);
                emailMap.put("CessationDate", Formatter.formatDateTime(date));
                emailMap.put("email", "");
                emailMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE);
                emailParam.setTemplateContent(emailMap);
                emailParam.setQueryCode(licenceNo);
                emailParam.setReqRefNum(licenceNo);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                emailParam.setRefId(licenceDtoId);
                Map<String,Object> map=IaisCommonUtils.genNewHashMap();
                InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE);
                map.put("ServiceLicenceName", svcName);
                map.put("LicenceNumber", licenceNo);
                String subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
                emailParam.setSubject(subject);
                //email
                notificationHelper.sendNotification(emailParam);
                //sms
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_SMS);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
                notificationHelper.sendNotification(emailParam);
                //msg
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_MSG);
                emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_ACTION_REQUIRED);
                List<LicAppCorrelationDto> licAppCorrelationDtos=hcsaLicenceClient.getLicCorrBylicId(licenceDtoId).getEntity();
                ApplicationDto applicationDto=applicationClient.getApplicationById(licAppCorrelationDtos.get(0).getApplicationId()).getEntity();
                emailParam.setRefId(applicationDto.getApplicationNo());
                notificationHelper.sendNotification(emailParam);
                //cessationBeService.sendEmail(LICENCEENDDATE, date, svcName, licenceDtoId, licenseeId, licenceNo);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        try {
            hcsaLicenceClient.updateLicences(updateLicenceDtos).getEntity();
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            gatewayClient.updateFeLicDto(updateLicenceDtos, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

    }
}
