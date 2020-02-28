package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.service.LicInspNcEmailService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * LicInspNcEmailServiceImpl
 *
 * @author junyu
 * @date 2020/2/27
 */
@Service
public class LicInspNcEmailServiceImpl implements LicInspNcEmailService {


    @Autowired
    private SystemBeLicClient systemClient;

    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;

    @Autowired
    OrganizationClient licenseeClient;

    @Override
    public String updateEmailDraft(InspectionEmailTemplateDto inspectionEmailTemplateDto) {
        return hcsaLicenceClient.updateEmailDraft(inspectionEmailTemplateDto).getEntity();
    }

    @Override
    public String insertEmailDraft(InspectionEmailTemplateDto inspectionEmailTemplateDto) {
        return hcsaLicenceClient.insertEmailDraft(inspectionEmailTemplateDto).getEntity();
    }

    @Override
    public InspectionEmailTemplateDto getInsertEmail(String licPremCorrId) {
        return hcsaLicenceClient.getInspectionEmail(licPremCorrId).getEntity();
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
    public LicenceViewDto getLicenceDtoByLicPremCorrId(String licPremCorrId) {
        return hcsaLicenceClient.getLicenceViewDtoByLicPremCorrId(licPremCorrId).getEntity();
    }

    @Override
    public List<LicPremisesDto> getLicPremisesCorrelationsByPremises(String licPremCorrId) {
        return hcsaLicenceClient.getlicPremisesCorrelationsByPremises(licPremCorrId).getEntity();
    }
}
