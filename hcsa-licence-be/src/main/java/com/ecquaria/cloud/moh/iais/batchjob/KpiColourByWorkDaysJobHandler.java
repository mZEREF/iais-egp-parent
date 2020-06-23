package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
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
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Process MohKpiColourShow
 *
 * @author Shicheng
 * @date 2020/4/23 16:45
 **/
@JobHandler(value="kpiColourByWorkDaysJobHandler")
@Component
@Slf4j
public class KpiColourByWorkDaysJobHandler extends IJobHandler {

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

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
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
                    log.info(StringUtil.changeForLog("Task Id = " + taskDto.getId()));
                    JobLogger.log(StringUtil.changeForLog("Task Id = " + taskDto.getId()));
                    getTimeLimitWarningColourByTask(taskDto, intranet, holidayTime);
                }
            }

            return ReturnT.SUCCESS;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }

    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
        JobLogger.log(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
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
            Date startDate;
            Date completeDate;
            List<TaskDto> taskDtoList = organizationClient.getOtherKpiTask(taskDto).getEntity();
            if(!(IaisCommonUtils.isEmpty(taskDtoList))){
                List<Date> endDates = IaisCommonUtils.genNewArrayList();
                List<Date> beginDates = IaisCommonUtils.genNewArrayList();
                for(TaskDto tDto : taskDtoList){
                    if(tDto.getDateAssigned() != null){
                        startDate = tDto.getDateAssigned();
                    } else {
                        startDate = new Date();
                    }
                    if(taskDto.getSlaDateCompleted() != null){
                        completeDate = tDto.getSlaDateCompleted();
                    } else {
                        completeDate = new Date();
                    }
                    beginDates.add(startDate);
                    endDates.add(completeDate);
                    workAndNonWorkDays = getDaysWithoutAssignDay(workAndNonWorkDays, startDate, completeDate);
                }
                Date endDate = sortLastEndDate(endDates);
                Date beginDate = sortFirstDate(beginDates);
                Set<Date> setDate = new HashSet<>(workAndNonWorkDays);
                workAndNonWorkDays = new ArrayList<>(setDate);
                //count work days
                KpiCountDto kpiCountDto = new KpiCountDto();
                kpiCountDto.setTaskDates(workAndNonWorkDays);
                kpiCountDto.setTimeList(holidayTime);
                kpiCountDto.setApptSrcSysId(AppointmentConstants.APPT_SRC_SYSTEM_PK_ID);
                kpiCountDto.setStartDate(beginDate);
                String workGroupName = getWorkGroupByPkId(taskDto.getWkGrpId());
                kpiCountDto.setWorkGroupName(workGroupName);
                kpiCountDto.setEndDate(endDate);
                //get The number of holidays and work days
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
        //set work days to current stage
        ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(appPremCorrId).getEntity();
        if(applicationDto != null) {
            String appNo = applicationDto.getApplicationNo();
            setSalDaysToCurStage(appNo, curStage, days, intranet);
        }
    }

    private Date sortFirstDate(List<Date> beginDates) {
        Date beginDate = null;
        for(Date date : beginDates){
            if(beginDate == null){
                beginDate = date;
            } else {
                if(date.before(beginDate)){
                    beginDate = date;
                }
            }
        }
        return beginDate;
    }

    private Date sortLastEndDate(List<Date> endDates) {
        Date endDate = null;
        for(Date date : endDates){
            if(endDate == null){
                endDate = date;
            } else {
                if(date.after(endDate)){
                    endDate = date;
                }
            }
        }
        return endDate;
    }

    private String getWorkGroupByPkId(String wkGrpId) {
        WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(wkGrpId).getEntity();
        String workGroupName = workingGroupDto.getGroupName();
        return workGroupName;
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
        log.debug(StringUtil.changeForLog("History Size = " + appPremisesRoutingHistoryDtos.size()));
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
            inspecTaskCreAndAssDto.setApplicationNo(applicationDto.getApplicationNo());
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
            log.debug(StringUtil.changeForLog("Task Size = " + taskDtos.size()));
        }
        log.debug(StringUtil.changeForLog("All Task Size = " + taskDtoList.size()));
        Map<Integer, Integer> workAndNonMap = getActualWorkingDays(taskDtoList, allWorkDays, allHolidays, holidayTime, taskDto.getWkGrpId());
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

    private Map<Integer, Integer> getActualWorkingDays(List<TaskDto> taskDtoList, int allWorkDays, int allHolidays, List<Long> holidayTime, String wkGrpId) {
        Map<Integer, Integer> workAndNonMap = new HashMap();
        List<Date> workAndNonWorkDays = IaisCommonUtils.genNewArrayList();
        List<Date> beginDates = IaisCommonUtils.genNewArrayList();
        List<Date> endDates = IaisCommonUtils.genNewArrayList();
        for(TaskDto td : taskDtoList){
            Date startDate;
            if(td.getDateAssigned() != null){
                startDate = td.getDateAssigned();
            } else {
                startDate = new Date();
            }
            Date completeDate;
            if(td.getSlaDateCompleted() == null){
                completeDate = new Date();
            } else {
                completeDate = td.getSlaDateCompleted();
            }
            beginDates.add(startDate);
            endDates.add(completeDate);

            workAndNonWorkDays = getDaysWithoutAssignDay(workAndNonWorkDays, startDate, completeDate);
        }
        //get The scope of date
        Date endDate = sortLastEndDate(endDates);
        Date beginDate = sortFirstDate(beginDates);

        Set<Date> setDate = new HashSet<>(workAndNonWorkDays);
        workAndNonWorkDays = new ArrayList<>(setDate);
        //count work days
        KpiCountDto kpiCountDto = new KpiCountDto();
        kpiCountDto.setTaskDates(workAndNonWorkDays);
        kpiCountDto.setTimeList(holidayTime);
        kpiCountDto.setApptSrcSysId(AppointmentConstants.APPT_SRC_SYSTEM_PK_ID);
        kpiCountDto.setStartDate(beginDate);
        String workGroupName = getWorkGroupByPkId(wkGrpId);
        kpiCountDto.setWorkGroupName(workGroupName);
        kpiCountDto.setEndDate(endDate);
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

    private List<Date> getDaysWithoutAssignDay(List<Date> workAndNonWorkDays, Date startDate, Date completeDate) {
        List<Date> days = MiscUtil.getDateInPeriodByRecurrence(startDate, completeDate);
        Date assignDay = sortFirstDate(days);
        if(!IaisCommonUtils.isEmpty(days)){
            for(Date date : days){
                if(!date.equals(assignDay)) {
                    workAndNonWorkDays.add(date);
                }
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
