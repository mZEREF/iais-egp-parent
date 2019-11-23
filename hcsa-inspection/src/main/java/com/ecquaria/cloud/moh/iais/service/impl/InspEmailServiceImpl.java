package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
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
    @Override
    public InspectionEmailTemplateDto getInsEmailTemplateDto(String templateId) {
        return RestApiUtil.postGetObject("system-admin:8886/inspection/email-template",templateId, InspectionEmailTemplateDto.class);

    }
}
