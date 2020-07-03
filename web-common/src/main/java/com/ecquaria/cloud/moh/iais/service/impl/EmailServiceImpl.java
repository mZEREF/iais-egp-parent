package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.EmailService;
import com.ecquaria.cloud.moh.iais.service.client.TaskOrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * EmailServiceImpl
 *
 * @author guyin
 * @date 11/20/2019
 */
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Autowired
    TaskOrganizationClient taskOrganizationClient;
    @Override
    public void callEicSendEmail(EmailDto emailDto){
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
//        feEicGatewayClient.feSendEmail(emailDto,signature.date(), signature.authorization(),
//                signature2.date(), signature2.authorization());
    }

    @Override
    public List<OrgUserDto> retrieveOrgUserByroleId(List<String> roleId){
        return taskOrganizationClient.retrieveOrgUserByroleId(roleId).getEntity();
    }

    @Override
    public List<OrgUserDto> retrieveOrgUser(){
        return taskOrganizationClient.retrieveOrgUser().getEntity();
    }


}
