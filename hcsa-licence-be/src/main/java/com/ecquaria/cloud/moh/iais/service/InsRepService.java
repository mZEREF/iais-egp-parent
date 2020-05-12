package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloudfeign.FeignException;
import java.util.List;

/**
 * @author weilu
 * date 2019/11/20 16:10
 */
public interface InsRepService {

    InspectionReportDto getInsRepDto (TaskDto taskDto, ApplicationViewDto applicationViewDto , LoginContext loginContext) throws FeignException;

    void saveRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto);
    void updateengageRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto);
    void updateRiskRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto);
    void updateFollowRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto);
    void updateRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto);
    List<SelectOption> getRiskOption(ApplicationViewDto applicationViewDto);
    List<String> getPeriods(ApplicationViewDto applicationViewDto);
    ApplicationViewDto getApplicationViewDto (String appNo);
    ApplicationDto updateApplicaiton(ApplicationDto applicationDto);
    String  getRobackUserId(String appId,String stageId);
    void routingTaskToAo1(TaskDto taskDto,ApplicationDto applicationDto,String appPremisesCorrelationId,AppPremisesRecommendationDto appPremisesRecommendationDto) throws FeignException;
    void routingTaskToAo2(TaskDto taskDto,ApplicationDto applicationDto,String appPremisesCorrelationId,String historyRemarks) throws FeignException;
    void routBackTaskToInspector(TaskDto taskDto,ApplicationDto applicationDto,String appPremisesCorrelationId,String historyRemarks) throws FeignException;
    void routTastToRoutBack(TaskDto taskDto,ApplicationDto applicationDto,String appPremisesCorrelationId,String historyRemarks);



    InspectionReportDto getInspectorUser(TaskDto taskDto,LoginContext loginContext);
    InspectionReportDto getInspectorAo(TaskDto taskDto,ApplicationViewDto applicationViewDto);


    void sendPostInsTaskFeData(String submissionId,String eventRefNum) throws FeignException;

}
