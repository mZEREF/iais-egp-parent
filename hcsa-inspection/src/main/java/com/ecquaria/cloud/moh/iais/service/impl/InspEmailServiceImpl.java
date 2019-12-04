package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.client.InsEmailClient;
import com.ecquaria.cloud.moh.iais.service.client.SysAdminMessageTemplateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    InsEmailClient insEmailClient;
    @Autowired
    SysAdminMessageTemplateClient sysAdminMessageTemplateClient;

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
        return insEmailClient.getAppViewByNo(appNo).getEntity();
    }

    @Override
    public InspectionEmailTemplateDto loadingEmailTemplate(String id) {
        return sysAdminMessageTemplateClient.loadingEmailTemplate(id).getEntity();
    }


}
