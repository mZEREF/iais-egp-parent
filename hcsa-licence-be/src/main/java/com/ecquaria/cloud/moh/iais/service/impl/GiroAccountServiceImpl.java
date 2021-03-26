package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountFormDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.OrganizationPremisesViewQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.GiroAccountService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.GiroAccountBeClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GiroAccountServiceImpl
 *
 * @author junyu
 * @date 2021/3/3
 */
@Slf4j
@Service
public class GiroAccountServiceImpl implements GiroAccountService {

    @Autowired
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
    private BeEicGatewayClient gatewayClient;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    GiroAccountBeClient giroAccountBeClient;
    @Autowired
    private NotificationHelper notificationHelper;
    @Override
    public SearchResult<GiroAccountInfoQueryDto> searchGiroInfoByParam(SearchParam searchParam) {
        return giroAccountBeClient.searchGiroInfoByParam(searchParam).getEntity();
    }

    @Override
    public SearchResult<OrganizationPremisesViewQueryDto> searchOrgPremByParam(SearchParam searchParam) {
        return giroAccountBeClient.searchOrgPremByParam(searchParam).getEntity();
    }

    @Override
    public List<GiroAccountInfoDto> createGiroAccountInfo(List<GiroAccountInfoDto> giroAccountInfoDto) {
        return giroAccountBeClient.createGiroAccountInfo(giroAccountInfoDto).getEntity();
    }

    @Override
    public void updateGiroAccountInfo(List<GiroAccountInfoDto> giroAccountInfoDto) {
        giroAccountBeClient.updateGiroAccountInfo(giroAccountInfoDto);
    }

    @Override
    public List<GiroAccountFormDocDto> findGiroAccountFormDocDtoListByAcctId(String acctId) {
        return giroAccountBeClient.findGiroAccountFormDocDtoListByAcctId(acctId).getEntity();
    }

    @Override
    public GiroAccountInfoDto findGiroAccountInfoDtoByAcctId(String acctId) {
        return giroAccountBeClient.findGiroAccountInfoDtoByAcctId(acctId).getEntity();
    }


    @Override
    public void syncFeGiroAcctDto(List<GiroAccountInfoDto> giroAccountInfoDtoList) {
        EicRequestTrackingDto trackDto = getLicEicRequestTrackingDtoByRefNo(giroAccountInfoDtoList.get(0).getEventRefNo());
        eicCallFeGiroLic(giroAccountInfoDtoList);
        trackDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        updateGiroAccountInfoTrackingDto(trackDto);

    }

    public void eicCallFeGiroLic(List<GiroAccountInfoDto> giroAccountInfoDtoList) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        log.info(StringUtil.changeForLog("=======>>>>>"+" Lic Giro Account Information Id :"+giroAccountInfoDtoList.get(0).getId()));

        gatewayClient.updateGiroAccountInfo(giroAccountInfoDtoList,
                signature.date(), signature.authorization(), signature2.date(), signature2.authorization());
    }

    @Override
    public void updateGiroAccountInfoTrackingDto(EicRequestTrackingDto licEicRequestTrackingDto) {
        licEicClient.saveEicTrack(licEicRequestTrackingDto);
    }


    public EicRequestTrackingDto getLicEicRequestTrackingDtoByRefNo(String refNo) {
        return licEicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
    }


    @Override
    public void sendEmailForGiroAccountAndSMSAndMessage(GiroAccountInfoDto giroAccountInfoDto) {
        try{
            //ApplicationDto applicationDto =  appSubmissionDto.getApplicationDtos().get(0);
            //String applicationType =  MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType());
            int index = 0;
            StringBuilder stringBuilderAPPNum = new StringBuilder();
//            for(ApplicationDto applicationDtoApp : appSubmissionDto.getApplicationDtos()){
//                if(index == 0){
//                    stringBuilderAPPNum.append(applicationDtoApp.getApplicationNo());
//                }else {
//                    stringBuilderAPPNum.append(" and ");
//                    stringBuilderAPPNum.append(applicationDtoApp.getApplicationNo());
//                }
//                index++;
//            }
            String applicationNumber = stringBuilderAPPNum.toString();
            Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
            //String applicationTypeShow = MasterCodeUtil.getCodeDesc(applicationType);
            //subMap.put("ApplicationType", applicationTypeShow);
            subMap.put("ApplicationNumber", applicationNumber);
            String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_003_EMAIL,subMap);
            String smsSubject = getEmailSubject(MsgTemplateConstants. MSG_TEMPLATE_EN_FEP_003_SMS ,subMap);
            String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_003_MSG,subMap);
            log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
            log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
            log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
            Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
            //templateContent.put("ApplicantName", applicantName);
            //templateContent.put("ApplicationType",  applicationType);
            templateContent.put("ApplicationNumber", applicationNumber);
            //todo need create new giro account time
            templateContent.put("DDMMYYYY", Formatter.formatDateTime(new Date()));
            templateContent.put("email", systemParamConfig.getSystemAddressOne());
            String syName = "<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"<br/>"+AppConsts.MOH_AGENCY_NAME+"</b>";
            templateContent.put("MOH_AGENCY_NAME",syName);
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_EMAIL);
            emailParam.setTemplateContent(templateContent);
            emailParam.setSubject(emailSubject);
//            emailParam.setQueryCode(applicationDto.getApplicationNo());
//            emailParam.setReqRefNum(applicationDto.getApplicationNo());
//            emailParam.setRefId(applicationDto.getApplicationNo());
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
            notificationHelper.sendNotification(emailParam);

            EmailParam smsParam = new EmailParam();
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_SMS);
            smsParam.setSubject(smsSubject);
//            smsParam.setQueryCode(applicationDto.getApplicationNo());
//            smsParam.setReqRefNum(applicationDto.getApplicationNo());
//            smsParam.setRefId(applicationDto.getApplicationNo());
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            notificationHelper.sendNotification(smsParam);

            EmailParam msgParam = new EmailParam();
            msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_MSG);
            msgParam.setTemplateContent(templateContent);
            msgParam.setSubject(messageSubject);
//            msgParam.setQueryCode(applicationDto.getApplicationNo());
//            msgParam.setReqRefNum(applicationDto.getApplicationNo());
//            msgParam.setRefId(applicationDto.getApplicationNo());
//            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
//
            List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
//            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
//                if( !svcCodeList.contains(appSvcRelatedInfoDto.getServiceCode())){
//                    svcCodeList.add(appSvcRelatedInfoDto.getServiceCode());
//                }
//            }
            msgParam.setSvcCodeList(svcCodeList);
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            notificationHelper.sendNotification(msgParam);
            log.info("end send email sms and msg");
        }catch (Exception e){
            log.error(e.getMessage(),e);
            log.info("send app sumbit email fail");
        }
    }

    private String getEmailSubject(String templateId, Map<String, Object> subMap){
        String subject = "-";
        if(!StringUtil.isEmpty(templateId)){
            MsgTemplateDto emailTemplateDto =notificationHelper.getMsgTemplate(templateId);
            if(emailTemplateDto != null){
                try {
                    if(!IaisCommonUtils.isEmpty(subMap)){
                        subject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(),subMap);
                    }else{
                        subject = emailTemplateDto.getTemplateName();
                    }
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
        return subject;
    }

}
