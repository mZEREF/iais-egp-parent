package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.KpiCountDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppStageSlaTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Process MohKpiColourShow
 *
 * @author Shicheng
 * @date 2020/4/23 16:45
 **/
@Delegator("kpiColourByWorkDaysBatchJob")
@Slf4j
public class KpiColourByWorkDaysBatchJob {

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private AppointmentClient appointmentClient;

    @Autowired
    private TaskService taskService;
    /**
     * StartStep: mohKpiColourShowStart
     *
     * @param bpc
     * @throws
     */
    public void mohKpiColourShowStart(BaseProcessClass bpc){
        logAbout("MohKpiColourShow");
    }

    /**
     * StartStep: mohKpiColourShowStep
     *
     * @param bpc
     * @throws
     */
    public void mohKpiColourShowStep(BaseProcessClass bpc){
        logAbout("MohKpiColourShow");
        List<Date> holidays = appointmentClient.getHolidays().getEntity();
        List<Long> holidayTime = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(holidays)){
            for(Date date : holidays){
                holidayTime.add(date.getTime());
            }
        }
        List<TaskDto> taskDtos = organizationClient.getKpiTaskByStatus().getEntity();
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        if(!IaisCommonUtils.isEmpty(taskDtos)){
            for(TaskDto taskDto : taskDtos){
                getTimeLimitWarningColourByTask(taskDto, intranet, holidayTime);
            }
        }
    }

    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }

    private void getTimeLimitWarningColourByTask(TaskDto taskDto, AuditTrailDto intranet, List<Long> holidayTime) {
        String appPremCorrId = taskDto.getRefNo();
        //get current stageId
        String curStage;
        int days = 0;
        List<Date> workAndNonWorkDays = IaisCommonUtils.genNewArrayList();
        if(taskDto.getTaskKey().equals(HcsaConsts.ROUTING_STAGE_INS)) {
            String subStage = getSubStageByInspectionStatus(appPremCorrId);
            Map<Integer, Integer> workAndNonMap = getWorkingDaysBySubStage(subStage, taskDto, holidayTime);
            if(workAndNonMap != null){
                for(Map.Entry<Integer, Integer> map:workAndNonMap.entrySet()){
                    days = map.getKey();
                }
            }
            curStage = subStage;
        } else {
            curStage = taskDto.getTaskKey();
            int allWorkDays = 0;
            int allHolidays = 0;
            Map<Integer, Integer> workAndNonMap = new HashMap();
            Date startDate = taskDto.getDateAssigned();
            Date completeDate;
            List<TaskDto> taskDtoList = organizationClient.getOtherKpiTask(taskDto).getEntity();
            if(!(IaisCommonUtils.isEmpty(taskDtoList))){
                for(TaskDto tDto : taskDtoList){
                    if(taskDto.getSlaDateCompleted() != null){
                        completeDate = tDto.getSlaDateCompleted();
                    } else {
                        completeDate = new Date();
                    }
                    workAndNonWorkDays = getWorkAndNonWorkDays(workAndNonWorkDays, startDate, completeDate);
                }
                Set<Date> setDate = new HashSet<>(workAndNonWorkDays);
                workAndNonWorkDays = new ArrayList<>(setDate);
                //count work days
                KpiCountDto kpiCountDto = new KpiCountDto();
                kpiCountDto.setTaskDates(workAndNonWorkDays);
                kpiCountDto.setTimeList(holidayTime);
                Map<Integer, Integer> workAndNonMapS = appointmentClient.getWorkAndNonMap(kpiCountDto).getEntity();
                if(workAndNonMapS != null && workAndNonMapS.size() > 0) {
                    for (Map.Entry<Integer, Integer> map : workAndNonMapS.entrySet()) {
                        allWorkDays = allWorkDays + map.getKey();
                        allHolidays = allHolidays + map.getValue();
                    }
                }
            }
            workAndNonMap.put(allWorkDays, allHolidays);
            if(workAndNonMap != null){
                for(Map.Entry<Integer, Integer> map:workAndNonMap.entrySet()){
                    days = map.getKey();
                }
            }
        }
        //Remove today
        if(days > 0){
            days--;
        }
        //set work days to current stage
        ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(appPremCorrId).getEntity();
        if(applicationDto != null) {
            String appNo = applicationDto.getApplicationNo();
            setSalDaysToCurStage(appNo, curStage, days, intranet);
        }
    }

    private void setSalDaysToCurStage(String appNo, String curStage, int days, AuditTrailDto intranet) {
        if(!StringUtil.isEmpty(curStage)) {
            AppStageSlaTrackingDto appStageSlaTrackingDto = inspectionTaskClient.getSlaTrackByAppNoStageId(appNo, curStage).getEntity();
            if (appStageSlaTrackingDto == null) {
                appStageSlaTrackingDto = new AppStageSlaTrackingDto();
                appStageSlaTrackingDto.setId(null);
                appStageSlaTrackingDto.setApplicationNo(appNo);
                appStageSlaTrackingDto.setStageId(curStage);
                appStageSlaTrackingDto.setKpiSlaDays(days);
                appStageSlaTrackingDto.setAuditTrailDto(intranet);
                inspectionTaskClient.createAppStageSlaTrackingDto(appStageSlaTrackingDto);
            } else {
                appStageSlaTrackingDto.setApplicationNo(appNo);
                appStageSlaTrackingDto.setStageId(curStage);
                appStageSlaTrackingDto.setKpiSlaDays(days);
                appStageSlaTrackingDto.setAuditTrailDto(intranet);
                inspectionTaskClient.updateAppStageSlaTrackingDto(appStageSlaTrackingDto);
            }
        }
    }

    private Map<Integer, Integer> getWorkingDaysBySubStage(String subStage, TaskDto taskDto, List<Long> holidayTime) {
        Map<Integer, Integer> workAndNonMap = new HashMap();
        List<Date> workAndNonWorkDays = IaisCommonUtils.genNewArrayList();
        List<String> processUrls = IaisCommonUtils.genNewArrayList();
        if(StringUtil.isEmpty(subStage)){
            return null;
        }
        ApplicationViewDto applicationViewDto = applicationClient.getAppViewByCorrelationId(taskDto.getRefNo()).getEntity();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto.setWrkGrpId(taskDto.getWkGrpId());
        appPremisesRoutingHistoryDto.setRoleId(taskDto.getRoleId());
        appPremisesRoutingHistoryDto.setApplicationNo(applicationDto.getApplicationNo());
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = inspectionTaskClient.getHistoryForKpi(appPremisesRoutingHistoryDto).getEntity();
        List<String> roleIds = getRoleIdsByHistory(appPremisesRoutingHistoryDtos);
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
        int allWorkDays = 0;
        int allHolidays = 0;
        for(String role : roleIds) {
            TaskDto tDto = new TaskDto();
            InspecTaskCreAndAssDto inspecTaskCreAndAssDto = new InspecTaskCreAndAssDto();
            tDto.setRefNo(taskDto.getRefNo());
            tDto.setRoleId(role);
            tDto.setWkGrpId(taskDto.getWkGrpId());
            tDto.setTaskKey(taskDto.getTaskKey());
            inspecTaskCreAndAssDto.setTaskDto(tDto);
            if(HcsaConsts.ROUTING_STAGE_PRE.equals(subStage)){
                processUrls.add(TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION);
            } else if(HcsaConsts.ROUTING_STAGE_POT.equals(subStage)) {
                processUrls.add(TaskConsts.TASK_PROCESS_URL_INSPECTION_MERGE_NCEMAIL);
                processUrls.add(TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT);
                processUrls.add(TaskConsts.TASK_PROCESS_URL_INSPECTION_NCEMAIL);
                processUrls.add(TaskConsts.TASK_PROCESS_URL_INSPECTION_REVISE_NCEMAIL);
                processUrls.add(TaskConsts.TASK_PROCESS_URL_INSPECTION_AO1_VALIDATE_NCEMAIL);
                processUrls.add(TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT_REVIEW_AO1);
                processUrls.add(TaskConsts.TASK_PROCESS_URL_INSPECTION_CHECK_FILLUP);
            } else if(HcsaConsts.ROUTING_STAGE_INP.equals(subStage)){
                processUrls.add(TaskConsts.TASK_PROCESS_URL_INSPECTION_CHECKLIST_VERIFY);
            }
            inspecTaskCreAndAssDto.setProcessUrls(processUrls);
            List<TaskDto> taskDtos = organizationClient.getInsKpiTask(inspecTaskCreAndAssDto).getEntity();
            if(!IaisCommonUtils.isEmpty(taskDtos)){
                for(TaskDto taskDtoSingle : taskDtos){
                    taskDtoList.add(taskDtoSingle);
                }
            }
        }
        workAndNonMap = getActualWorkingDays(taskDtoList, allWorkDays, allHolidays, holidayTime);
        return workAndNonMap;
    }

    private List<String> getRoleIdsByHistory(List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos) {
        List<String> roleIds = IaisCommonUtils.genNewArrayList();
        if(!(IaisCommonUtils.isEmpty(appPremisesRoutingHistoryDtos))){
            for(AppPremisesRoutingHistoryDto aprhDto : appPremisesRoutingHistoryDtos) {
                roleIds.add(aprhDto.getRoleId());
            }
            Set<String> roleIdSet = new HashSet<>(roleIds);
            roleIds = new ArrayList<>(roleIdSet);
        }
        return roleIds;
    }

    private Map<Integer, Integer> getActualWorkingDays(List<TaskDto> taskDtoList, int allWorkDays, int allHolidays, List<Long> holidayTime) {
        Map<Integer, Integer> workAndNonMap = new HashMap();
        List<Date> workAndNonWorkDays = IaisCommonUtils.genNewArrayList();
        for(TaskDto td : taskDtoList){
            Date startDate = td.getDateAssigned();
            Date completeDate;
            if(td.getSlaDateCompleted() == null){
                completeDate = new Date();
            } else {
                completeDate = td.getSlaDateCompleted();
            }
            workAndNonWorkDays = getWorkAndNonWorkDays(workAndNonWorkDays, startDate, completeDate);
        }
        Set<Date> setDate = new HashSet<>(workAndNonWorkDays);
        workAndNonWorkDays = new ArrayList<>(setDate);
        //count work days
        KpiCountDto kpiCountDto = new KpiCountDto();
        kpiCountDto.setTaskDates(workAndNonWorkDays);
        kpiCountDto.setTimeList(holidayTime);
        Map<Integer, Integer> workAndNonMapS = appointmentClient.getWorkAndNonMap(kpiCountDto).getEntity();
        if(workAndNonMapS != null && workAndNonMapS.size() > 0) {
            for (Map.Entry<Integer, Integer> map : workAndNonMapS.entrySet()) {
                allWorkDays = allWorkDays + map.getKey();
                allHolidays = allHolidays + map.getValue();
            }
        }
        workAndNonMap.put(allWorkDays, allHolidays);
        return workAndNonMap;
    }

    private List<Date> getWorkAndNonWorkDays(List<Date> workAndNonWorkDays, Date startDate, Date completeDate) {
        List<Date> days = MiscUtil.getDateInPeriodByRecurrence(startDate, completeDate);
        if(!IaisCommonUtils.isEmpty(days)){
            for(Date date : days){
                workAndNonWorkDays.add(date);
            }
        }
        return workAndNonWorkDays;
    }

    private String getSubStageByInspectionStatus(String appPremCorrId) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorrId).getEntity();
        String subStage = "";
        if(appInspectionStatusDto != null) {
            String status = appInspectionStatusDto.getStatus();
            if (status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_PRE)) {
                subStage = HcsaConsts.ROUTING_STAGE_PRE;
            } else if (status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_CHECKLIST_VERIFY)) {
                subStage = HcsaConsts.ROUTING_STAGE_INP;
            } else if (status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT) ||
                    status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_AO1_RESULT) ||
                    status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_EMAIL_VERIFY) ||
                    status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_AO1_EMAIL_VERIFY) ||
                    status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_REVIEW_CHECKLIST_EMAIL) ||
                    status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_NC_RECTIFICATION_EMAIL) ||
                    status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_REVIEW_RECTIFICATION_NC)) {
                subStage = HcsaConsts.ROUTING_STAGE_POT;
            }
        }
        return subStage;
    }
}
