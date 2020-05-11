package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.MohUenDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UenDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.EmailHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.UenManagementService;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEmailClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.client.UenManagementClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * UenManagementServiceImpl
 *
 * @author junyu
 * @date 2020/1/22
 */
@Slf4j
@Service
public class UenManagementServiceImpl implements UenManagementService {
    @Autowired
    private UenManagementClient uenManagementClient;
    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private FeEmailClient emailClient;
    @Autowired
    private EmailHelper emailHelper;

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

    private UenDto getUenDetails(String uenNo) {
        //等ACRA api
        return null;
    }

    @Override
    public boolean validityCheckforAcraissuedUen(MohUenDto mohUenDto) {
        MohUenDto mohUenDto1=uenManagementClient.getMohUenById(mohUenDto.getUenNo()).getEntity();
        //等ACRA api
        return false;
    }

    @Override
    public boolean generatesMohIssuedUen(BaseProcessClass bpc, MohUenDto mohUenDto) throws IOException, TemplateException {
        MohUenDto mohUenDto1= uenManagementClient.generatesMohIssuedUen(mohUenDto).getEntity();
        //等ACRA api
        sendUenEmail(bpc,"registrationUen");
        return false;
    }

    @Override
    public void sendUenEmail(BaseProcessClass bpc, String active) throws IOException, TemplateException {
        String licenceId= (String) ParamUtil.getSessionAttr(bpc.request,"licenceId");
        String uenNo=(String) ParamUtil.getSessionAttr(bpc.request,"uenNo");
        switch (active){
            case "registration":sendRegistrationEmail(licenceId,uenNo);break;
            case "registrationUen":sendRegistrationUenEmail(licenceId,uenNo);break;
            case "deRegistered":sendDeRegisteredEmail(licenceId,uenNo);break;
            default:break;
        }

    }

    private void sendRegistrationEmail(String licenceId,String uenNo) throws IOException, TemplateException {
        String templateId="D03B3C73-2893-EA11-BE7A-000C29D29DB0";
        sendUenEmails(licenceId, uenNo, templateId);
    }
    private void sendRegistrationUenEmail(String licenceId,String uenNo) throws IOException, TemplateException {
        String templateId="D03B3C73-2893-EA11-BE7A-000C29D29DB0";
        sendUenEmails(licenceId, uenNo, templateId);
    }
    private void sendDeRegisteredEmail(String licenceId,String uenNo) throws IOException, TemplateException {
        String templateId="7ED3CCAC-2893-EA11-BE7A-000C29D29DB0";
        sendUenEmails(licenceId, uenNo, templateId);
    }

    private void sendUenEmails(String licenceId, String uenNo, String templateId) throws IOException, TemplateException {
        MsgTemplateDto rfiEmailTemplateDto = systemAdminClient.getMsgTemplate(templateId).getEntity();
        Map<String,Object> map= IaisCommonUtils.genNewHashMap();
        map.put("UEN Number", StringUtil.viewHtml(uenNo));
        map.put("MOH_NAME", StringUtil.viewHtml(AppConsts.MOH_AGENCY_NAME));
        String mesContext= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getMessageContent(),map);
        try{
            EmailDto emailDto=new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject(rfiEmailTemplateDto.getTemplateName());
            emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
            List<String> licenseeIds= IaisCommonUtils.genNewArrayList();
            licenseeIds.add(licenceId);
            List<String> emailAddress = emailHelper.getEmailAddressListByLicenseeId(licenseeIds);
            emailDto.setReceipts(emailAddress);
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            feEicGatewayClient.feSendEmail(emailDto,signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

}
