package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;

/**
 * InspEmailService
 *
 * @author junyu
 * @date 2019/11/23
 */
public interface InspEmailService {
    InspectionEmailTemplateDto getInsEmailTemplateDto (String templateId);
}
