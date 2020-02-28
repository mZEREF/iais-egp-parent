package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;

import java.util.List;


/**
 * InspEmailService
 *
 * @author junyu
 * @date 2020/02/23
 */
public interface LicInspNcEmailService {
    String updateEmailDraft(InspectionEmailTemplateDto inspectionEmailTemplateDto);
    String insertEmailDraft(InspectionEmailTemplateDto inspectionEmailTemplateDto);
    InspectionEmailTemplateDto getInsertEmail(String licPremCorrId);
    InspectionEmailTemplateDto loadingEmailTemplate(String id);
    LicenseeDto getLicenseeDtoById(String id);
    LicenceViewDto getLicenceDtoByLicPremCorrId(String licPremCorrId);
    List<LicPremisesDto> getLicPremisesCorrelationsByPremises(String licPremCorrId);


}
