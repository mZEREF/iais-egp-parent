package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;

import java.util.List;


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
    ApplicationViewDto getAppViewByNo( String appNo);
    InspectionEmailTemplateDto loadingEmailTemplate(String id);
    ApplicationDto getApplicationDtoByAppPremCorrId(String appPremCorrId);
    public List<ChecklistQuestionDto> getcheckListQuestionDtoList(String svcCode, String svcType);
}
