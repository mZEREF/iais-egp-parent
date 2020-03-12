package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.MohUenDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UenDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.EmailHelper;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.UenManagementService;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.UenManagementClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    UenManagementClient uenManagementClient;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    EmailClient emailClient;
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
    public boolean generatesMohIssuedUen(MohUenDto mohUenDto) throws IOException, TemplateException {
        MohUenDto mohUenDto1= uenManagementClient.generatesMohIssuedUen(mohUenDto).getEntity();
        //等ACRA api
        String templateId="BEFC2AF0-250C-EA11-BE78-000C29D29DB0";
        InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(templateId);
        Map<String,Object> map=new HashMap<>();
        map.put("UEN Number",StringUtil.viewHtml(mohUenDto1.getUenNo()));
        map.put("MOH_NAME", StringUtil.viewHtml(AppConsts.MOH_AGENCY_NAME));
        String mesContext= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getMessageContent(),map);
        try{
            EmailDto emailDto=new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject(rfiEmailTemplateDto.getSubject());
            emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
            List<String> licenseeIds=new ArrayList<>();
            licenseeIds.add(mohUenDto.getId());
            List<String> emailAddress = EmailHelper.getEmailAddressListByLicenseeId(licenseeIds);
            emailDto.setReceipts(emailAddress);
            String requestRefNum = emailClient.sendNotification(emailDto).getEntity();
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return false;
    }
}
