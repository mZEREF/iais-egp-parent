package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;

import java.util.List;


/**
 * InspEmailService
 *
 * @author junyu
 * @date 2019/11/23
 */
public interface InspEmailService {
    void updateEmailDraft(InspectionEmailTemplateDto inspectionEmailTemplateDto);
    String insertEmailDraft(InspectionEmailTemplateDto inspectionEmailTemplateDto);
    InspectionEmailTemplateDto getInsertEmail(String appPremCorrId);
    ApplicationViewDto getAppViewByCorrelationId(String correlationId);
    InspectionEmailTemplateDto loadingEmailTemplate(String id);
    LicenseeDto getLicenseeDtoById ( String id);
    List<AppPremisesCorrelationDto>getAppPremisesCorrelationsByPremises(String appCorrId);
    List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId);
    TaskDto completedTask(TaskDto taskDto);
    String getLeadWithTheFewestScores(List<TaskDto> taskScoreDtos, List<String> leads);
}
