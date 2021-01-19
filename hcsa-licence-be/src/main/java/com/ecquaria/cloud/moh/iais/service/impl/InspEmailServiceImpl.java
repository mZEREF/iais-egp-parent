package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.InsEmailClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private SystemBeLicClient systemClient;

    @Autowired
    private ApplicationViewService applicationViewService;


    @Autowired
    OrganizationClient licenseeClient;

    @Override
    public void updateEmailDraft(InspectionEmailTemplateDto inspectionEmailTemplateDto) {
        inspectionEmailTemplateDto.setMessageContent(StringUtil.removeNonUtf8(inspectionEmailTemplateDto.getMessageContent()));
        inspectionEmailTemplateDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        insEmailClient.updateEmailDraft(inspectionEmailTemplateDto).getEntity();
    }

    @Override
    public String insertEmailDraft(InspectionEmailTemplateDto inspectionEmailTemplateDto) {
        inspectionEmailTemplateDto.setMessageContent(StringUtil.removeNonUtf8(inspectionEmailTemplateDto.getMessageContent()));
        inspectionEmailTemplateDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return insEmailClient.insertEmailDraft(inspectionEmailTemplateDto).getEntity();
    }


    @Override
    public InspectionEmailTemplateDto getInsertEmail(String appPremCorrId) {
        return insEmailClient.getInspectionEmail(appPremCorrId).getEntity();
    }

    @Override
    public ApplicationViewDto getAppViewByCorrelationId(String correlationId) {
        return applicationViewService.getApplicationViewDtoByCorrId(correlationId);
    }

    @Override
    public InspectionEmailTemplateDto loadingEmailTemplate(String id) {
        return systemClient.loadingEmailTemplate(id).getEntity();
    }


    @Override
    public LicenseeDto getLicenseeDtoById(String id) {
        return licenseeClient.getLicenseeDtoById(id).getEntity();
    }


    @Override
    public List<AppPremisesCorrelationDto> getAppPremisesCorrelationsByPremises(String appCorrId) {
        return appPremisesCorrClient.getAppPremisesCorrelationsByPremises(appCorrId).getEntity();
    }


}
