package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;

/**
 * InspEmailService
 *
 * @author junyu
 * @date 2019/11/23
 */
public interface InspEmailService {
    InspectionEmailTemplateDto getInsEmailTemplateDto(String templateId);
    String insertEmailTemplate(InspectionEmailTemplateDto inspectionEmailTemplateDto);
    void recallEmailTemplate(String id);
    String previewEmailTemplate(InspectionEmailTemplateDto inspectionEmailTemplateDto);
    String applyInspection(InspectionEmailTemplateDto inspectionEmailTemplateDto);
}
