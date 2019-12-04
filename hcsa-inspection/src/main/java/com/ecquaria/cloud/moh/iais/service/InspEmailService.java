package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;

/**
 * InspEmailService
 *
 * @author junyu
 * @date 2019/11/23
 */
public interface InspEmailService {
    String insertEmailTemplate(InspectionEmailTemplateDto inspectionEmailTemplateDto);
    void recallEmailTemplate(String id);
    InspectionEmailTemplateDto getInsertEmail(String appPremCorrId);
}
