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

    @Override
    public String insertEmailTemplate(InspectionEmailTemplateDto inspectionEmailTemplateDto) {
        return RestApiUtil.postGetObject("system-admin:8886/inspection/insert-template",inspectionEmailTemplateDto, String.class);
    }

    @Override
    public void recallEmailTemplate(String id) {
        RestApiUtil.delete("system-admin:8886/inspection/recall-template",id);
    }

    @Override
    public String previewEmailTemplate(InspectionEmailTemplateDto inspectionEmailTemplateDto) {
        return RestApiUtil.postGetObject("system-admin:8886/inspection/preview-template",inspectionEmailTemplateDto, String.class);
    }

}
