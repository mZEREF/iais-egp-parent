package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremInspCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Date;
import java.util.List;

/**
 * @Process MohSkipInspAppt
 *
 * @author Shicheng
 * @date 2020/4/26 9:46
 **/
@Delegator("skipInspApptBatchJob")
@Slf4j
public class MohSkipInspApptBatchJob {

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    /**
     * StartStep: mohSkipInspApptStart
     *
     * @param bpc
     * @throws
     */
    public void mohSkipInspApptStart(BaseProcessClass bpc){
        logAbout("Skip Online Appointment Inspection Date");
    }

    /**
     * StartStep: mohSkipInspApptStep
     *
     * @param bpc
     * @throws
     */
    public void mohSkipInspApptStep(BaseProcessClass bpc){
        logAbout("Skip Online Appointment Inspection Date");
        List<TaskDto> taskDtos = organizationClient.getActiveTaskByUrl(TaskConsts.TASK_PROCESS_URL_APPT_INSPECTION_DATE).getEntity();
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        if(!IaisCommonUtils.isEmpty(taskDtos)){
            List<AppPremInspCorrelationDto> appPremInspCorrelationDtos = IaisCommonUtils.genNewArrayList();
            try{
                for(TaskDto taskDto : taskDtos){
                    taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION);
                    taskDto.setAuditTrailDto(intranet);
                    taskService.updateTask(taskDto);
                    String appPremCorrId = taskDto.getRefNo();
                    //update be Application
                    ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(appPremCorrId).getEntity();
                    applicationDto.setAuditTrailDto(intranet);
                    ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
                    //update fe Application
                    applicationDto1.setAuditTrailDto(intranet);
                    applicationService.updateFEApplicaiton(applicationDto1);
                    //update inspection status
                    updateInspectionStatus(appPremCorrId, InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
                    AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrId, InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                    createOrUpdateRecommendation(appPremisesRecommendationDto, appPremCorrId, new Date());
                    AppPremInspCorrelationDto appPremInspCorrelationDto = new AppPremInspCorrelationDto();
                    appPremInspCorrelationDto.setId(null);
                    appPremInspCorrelationDto.setUserId(taskDto.getUserId());
                    appPremInspCorrelationDto.setApplicationNo(taskDto.getApplicationNo());
                    appPremInspCorrelationDto.setAuditTrailDto(intranet);
                    appPremInspCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    appPremInspCorrelationDtos.add(appPremInspCorrelationDto);
                }
                inspectionTaskClient.createAppPremInspCorrelationDto(appPremInspCorrelationDtos);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void updateInspectionStatus(String appPremCorreId, String status) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorreId).getEntity();
        appInspectionStatusDto.setStatus(status);
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appInspectionStatusClient.update(appInspectionStatusDto);
    }

    private ApplicationDto updateApplication(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        return applicationViewService.updateApplicaiton(applicationDto);
    }

    private void createOrUpdateRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto, String appPremCorrId, Date saveDate) {
        if (appPremisesRecommendationDto == null) {
            appPremisesRecommendationDto = new AppPremisesRecommendationDto();
            appPremisesRecommendationDto.setAppPremCorreId(appPremCorrId);
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremisesRecommendationDto.setVersion(1);
            appPremisesRecommendationDto.setRecomInDate(saveDate);
            appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
            appPremisesRecommendationDto.setRecomDecision(InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY);
            appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fillUpCheckListGetAppClient.saveAppRecom(appPremisesRecommendationDto);
        } else {
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fillUpCheckListGetAppClient.updateAppRecom(appPremisesRecommendationDto);
            AppPremisesRecommendationDto appPremRecDto = new AppPremisesRecommendationDto();
            appPremRecDto.setId(null);
            appPremRecDto.setAppPremCorreId(appPremCorrId);
            appPremRecDto.setVersion(appPremisesRecommendationDto.getVersion() + 1);
            appPremRecDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremRecDto.setRecomInDate(saveDate);
            appPremRecDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
            appPremRecDto.setRecomDecision(InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY);
            appPremRecDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fillUpCheckListGetAppClient.saveAppRecom(appPremRecDto);
        }
    }

    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The*****" + methodName +" ******Start****"));
    }
}
