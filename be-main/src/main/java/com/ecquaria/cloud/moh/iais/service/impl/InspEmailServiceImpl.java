package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayMainClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * InspEmailServiceImpl
 *
 * @author junyu
 * @date 2019/11/23
 */
@Service
public class InspEmailServiceImpl implements InspEmailService {


    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Autowired
    private BeEicGatewayMainClient beEicGatewayMainClient;
    @Autowired
    private SystemBeLicClient systemBeLicClient;

    @Autowired
    private OrganizationMainClient organizationMainClient;

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private EmailClient emailClient;
    @Override
    public String getMessageNo() {
        return systemBeLicClient.messageID().getEntity();
    }
    @Override
    public InterMessageDto saveInterMessage(InterMessageDto interMessageDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return beEicGatewayMainClient.saveInboxMessage(interMessageDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
    }


    @Override
    public InspectionEmailTemplateDto loadingEmailTemplate(String id) {
        return systemBeLicClient.loadingEmailTemplate(id).getEntity();
    }

    @Override
    public LicenseeDto getLicenseeDtoById(String licenseeId){
        return organizationMainClient.getLicenseeDtoById(licenseeId).getEntity();
    }

    @Override
    public LicenceDto getLicenceByAppId(String appId){
        return licenceClient.getLicenceByAppId(appId).getEntity();
    }

    @Override
    public LicenceViewDto getLicenceViewDtoByLicPremCorrId(String licPremCorrId){
        return licenceClient.getLicenceViewDtoByLicPremCorrId(licPremCorrId).getEntity();
    }

    @Override
    public String sendNotification(EmailDto email){
        return emailClient.sendNotification(email).getEntity();
    }

    @Override
    public LicenceDto getLicBylicId(String licenceId){
        return licenceClient.getLicBylicId(licenceId).getEntity();
    }
}
