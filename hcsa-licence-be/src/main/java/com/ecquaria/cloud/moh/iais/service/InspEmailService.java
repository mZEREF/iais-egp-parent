package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;

import java.util.List;
import java.util.Map;


/**
 * InspEmailService
 *
 * @author junyu
 * @date 2019/11/23
 */
public interface InspEmailService {
    String updateEmailDraft(InspectionEmailTemplateDto inspectionEmailTemplateDto);
    String insertEmailDraft(InspectionEmailTemplateDto inspectionEmailTemplateDto);
    InspectionEmailTemplateDto getInsertEmail(String appPremCorrId);
    ApplicationViewDto getAppViewByCorrelationId(String correlationId);
    InspectionEmailTemplateDto loadingEmailTemplate(String id);
    List<ChecklistQuestionDto> getcheckListQuestionDtoList(String svcCode, String svcType);
    Map<String, String > SendAndSaveEmail(EmailDto emailDto);
    AppInsRepDto getAppInsRepDto(String appCorrId);
    List<AppPremisesCorrelationDto>getAppPremisesCorrelationsByAppGroupId(String appGrpId);
    List<NcAnswerDto> getNcAnswerDtoList(InspectionFillCheckListDto cDto, InspectionFillCheckListDto commonDto,
                                         AdCheckListShowDto adchklDto,List<NcAnswerDto> acDtoList);
    LicenseeDto getLicenseeDtoById ( String id);
    List<ApplicationDto> getApplicationDtosByCorreId( String appCorreId);
    List<AppPremisesCorrelationDto>getAppPremisesCorrelationsByPremises(String appCorrId);

}
