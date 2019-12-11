package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.InsEmailClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * InspEmailServiceImpl
 *
 * @author junyu
 * @date 2019/11/23
 */
@Service
public class InspEmailServiceImpl implements InspEmailService {
    @Autowired
    private InsEmailClient insEmailClient;

    @Autowired
    private SystemBeLicClient systemClient;

    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaChklClient hcsaChklClient;

    @Override
    public String insertEmailTemplate(InspectionEmailTemplateDto inspectionEmailTemplateDto) {
        return insEmailClient.insertEmailTemplate(inspectionEmailTemplateDto).getEntity();
    }

    @Override
    public void recallEmailTemplate(String id) {
        insEmailClient.recallEmailTemplate(id);
    }

    @Override
    public InspectionEmailTemplateDto getInsertEmail(String appPremCorrId) {
        return insEmailClient.getInsertEmail(appPremCorrId);
    }

    @Override
    public ApplicationViewDto getAppViewByNo(String appNo) {
        return applicationClient.getAppViewByNo(appNo).getEntity();
    }

    @Override
    public InspectionEmailTemplateDto loadingEmailTemplate(String id) {
        return systemClient.loadingEmailTemplate(id).getEntity();
    }

    @Override
    public ApplicationDto getApplicationDtoByAppPremCorrId(String appPremCorrId) {
        return insEmailClient.getApplicationDtoByAppPremCorrId(appPremCorrId).getEntity();
    }
    @Override
    public List<ChecklistQuestionDto> getcheckListQuestionDtoList(String svcCode,String svcType){
        return hcsaChklClient.getcheckListQuestionDtoList(svcCode,"Inspection").getEntity();
    }

    @Override
    public Map<String, String > SendAndSaveEmail(EmailDto emailDto){
        return insEmailClient.SendAndSaveEmail(emailDto).getEntity();
    }
}
