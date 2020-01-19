package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionPreTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2019/12/9 9:58
 **/
@Service
public class InspectionPreTaskServiceImpl implements InspectionPreTaskService {

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private InspectionAssignTaskService inspectionAssignTaskService;

    @Autowired
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private InboxMsgService inboxMsgService;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private AppointmentClient appointmentClient;

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private HcsaChklClient hcsaChklClient;

    @Autowired
    private FillupChklistService fillupChklistService;

    @Autowired
    private ApplicationService applicationService;

    @Override
    public ApplicationDto getAppStatusByTaskId(TaskDto taskDto) {
        ApplicationViewDto applicationViewDto = applicationClient.getAppViewByCorrelationId(taskDto.getRefNo()).getEntity();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        return applicationDto;
    }

    @Override
    public List<SelectOption> getProcessDecOption() {
        String[] processDecArr = new String[]{InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION, InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY};
        List<SelectOption> processDecOption = MasterCodeUtil.retrieveOptionsByCodes(processDecArr);
        return processDecOption;
    }

    @Override
    public void routingTask(TaskDto taskDto, String preInspecRemarks, List<ChecklistConfigDto> inspectionChecklist) {
        ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppCorrId(taskDto.getRefNo());
        taskDto.setSlaDateCompleted(new Date());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), applicationDto.getStatus(), taskDto.getTaskKey(), preInspecRemarks, InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY, RoleConsts.USER_ROLE_INSPECTIOR, taskDto.getWkGrpId(), HcsaConsts.ROUTING_STAGE_PRE);
        ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION);
        applicationService.updateFEApplicaiton(applicationDto1);
        applicationViewDto.setApplicationDto(applicationDto1);
        List<TaskDto> taskDtoList = organizationClient.getTaskByAppNo(taskDto.getRefNo()).getEntity();
        for(TaskDto tDto : taskDtoList){
            if(tDto.getTaskStatus().equals(TaskConsts.TASK_STATUS_PENDING) || tDto.getTaskStatus().equals(TaskConsts.TASK_STATUS_READ)) {
                tDto.setSlaDateCompleted(new Date());
                tDto.setSlaRemainInDays(taskService.remainDays(taskDto));
                tDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
                tDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                taskService.updateTask(tDto);
            }
        }
        saveInspectionChecklist(inspectionChecklist, taskDto.getRefNo());
        updateInspectionStatus(taskDto.getRefNo(), InspectionConstants.INSPECTION_STATUS_PENDING_INSPECTION);
        //todo:call inspection date

        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(taskDto.getRefNo(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
        if(appPremisesRecommendationDto == null){
            appPremisesRecommendationDto = new AppPremisesRecommendationDto();
            appPremisesRecommendationDto.setAppPremCorreId(taskDto.getRefNo());
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremisesRecommendationDto.setVersion(1);
            //todo:set inspection date
            appPremisesRecommendationDto.setRecomInDate(new Date());
            appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
            appPremisesRecommendationDto.setRecomDecision(InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY);
            appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fillUpCheckListGetAppClient.saveAppRecom(appPremisesRecommendationDto);
        } else {
            appPremisesRecommendationDto.setId(null);
            appPremisesRecommendationDto.setVersion(appPremisesRecommendationDto.getVersion() + 1);
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            //todo:set inspection date
            appPremisesRecommendationDto.setRecomInDate(new Date());
            appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
            appPremisesRecommendationDto.setRecomDecision(InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY);
            appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fillUpCheckListGetAppClient.saveAppRecom(appPremisesRecommendationDto);
        }
    }

    private void saveInspectionChecklist(List<ChecklistConfigDto> inspectionChecklist, String appCorrId) {
        for(ChecklistConfigDto ccDto : inspectionChecklist){
            AppPremisesPreInspectChklDto appDto = new AppPremisesPreInspectChklDto();
            appDto.setId(null);
            appDto.setAppPremCorrId(appCorrId);
            appDto.setVersion(AppConsts.YES);
            appDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appDto.setChkLstConfId(ccDto.getId());
            appDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fillUpCheckListGetAppClient.saveAppPreInspChkl(appDto);
        }
    }

    @Override
    public void routingBack(TaskDto taskDto, String reMarks) {
        ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppCorrId(taskDto.getRefNo());
        taskDto.setSlaDateCompleted(new Date());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), applicationDto.getStatus(), taskDto.getTaskKey(), reMarks, InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION, RoleConsts.USER_ROLE_INSPECTIOR, taskDto.getWkGrpId(), null);
        ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_CLARIFICATION);
        applicationService.updateFEApplicaiton(applicationDto1);
        applicationViewDto.setApplicationDto(applicationDto1);
        List<TaskDto> taskDtoList = organizationClient.getTaskByAppNo(taskDto.getRefNo()).getEntity();
        for(TaskDto tDto : taskDtoList){
            if(tDto.getTaskStatus().equals(TaskConsts.TASK_STATUS_PENDING) || tDto.getTaskStatus().equals(TaskConsts.TASK_STATUS_READ)) {
                tDto.setSlaDateCompleted(new Date());
                tDto.setSlaRemainInDays(taskService.remainDays(taskDto));
                tDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
                tDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                taskService.updateTask(tDto);
            }
        }
        updateInspectionStatus(taskDto.getRefNo(), InspectionConstants.INSPECTION_STATUS_PENDING_REQUEST_FOR_INFORMATION);
        InterMessageDto interMessageDto = new InterMessageDto();
        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_SRC_ID);
        interMessageDto.setSubject(MessageConstants.MESSAGE_SUBJECT_REQUEST_FOR_INFORMATION);
        interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
        String mesNO = inboxMsgService.getMessageNo();
        interMessageDto.setRefNo(mesNO);
        interMessageDto.setService_id(applicationDto1.getServiceId());
        String url = systemParamConfig.getInterServerName() +
                MessageConstants.MESSAGE_CALL_BACK_URL_NEWAPPLICATION +
                applicationDto1.getApplicationNo();
        interMessageDto.setProcessUrl(url);
        interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        inboxMsgService.saveInterMessage(interMessageDto);
    }


    @Override
    public void getAppointmentDate(TaskDto taskDto) {
        String applicationNo = taskDto.getRefNo();
        List<TaskDto> taskDtoList = organizationClient.getTaskByAppNo(applicationNo).getEntity();
        Integer inspectorNum = taskDtoList.size();
        ApplicationDto applicationDto = applicationClient.getAppByNo(applicationNo).getEntity();
        String serviceId = applicationDto.getServiceId();
        String stageId = taskDto.getId();
        Integer manHour = hcsaConfigClient.getManHour(serviceId, stageId).getEntity();
        /**
         * Working hours
         */
        Integer preManHour = (int) Math.ceil((double) manHour / inspectorNum);
        Map<Date,String> avaiDate = new HashMap<Date, String>();

        List<ApptNonWorkingDateDto> apptNonWorkingDateDtoList = appointmentClient.getNonWorkingDateListByWorkGroupId(applicationDto.getAppGrpId()).getEntity();
        for (ApptNonWorkingDateDto apptNonWorkingDateDto:apptNonWorkingDateDtoList
             ) {
            boolean avaiAM = apptNonWorkingDateDto.isAm();
            boolean avaiPM = apptNonWorkingDateDto.isPm();
            String recursivceDate = apptNonWorkingDateDto.getRecursivceDate();
            SearchResultHelper.getDateByWeekOfDay(avaiDate,recursivceDate,avaiAM,avaiPM);
        }

        List<PublicHolidayDto> publicHolidayDtoList = appointmentClient.getActiveHoliday().getEntity();
        for (PublicHolidayDto publicHolidayDto : publicHolidayDtoList) {
            List<Date> calculateDateList = getBetweenDays(publicHolidayDto.getFromDate(), publicHolidayDto.getToDate());
            for (Date calculateDate : calculateDateList) {
                avaiDate.put(calculateDate, "ALL");
            }
        }

        List<ApptBlackoutDateDto> apptBlackoutDateDtoList = appointmentClient.getAllByShortName(taskDto.getWkGrpId()).getEntity();
        for (ApptBlackoutDateDto apptBlackoutDateDto : apptBlackoutDateDtoList) {
            List<Date> calculateDateList = getBetweenDays(apptBlackoutDateDto.getStartDate(), apptBlackoutDateDto.getEndDate());
            for (Date calculateDate : calculateDateList) {
                avaiDate.put(calculateDate, "ALL");
            }
        }
        /**
         * TODO
         */

    }

    @Override
    public Map<InspectionFillCheckListDto, List<InspectionFillCheckListDto>> getSelfCheckListByCorrId(String refNo) {
        Map<InspectionFillCheckListDto, List<InspectionFillCheckListDto>> map = new HashMap<>();
        List<InspectionFillCheckListDto> chkDtoList = new ArrayList<>();
        InspectionFillCheckListDto commonDto = new InspectionFillCheckListDto();
        List<String> ids = new ArrayList<>();
        ids.add(refNo);
        List<String> selfDeclIdList = inspectionTaskClient.getSelfDeclChecklistByCorreId(ids).getEntity();
        if(IaisCommonUtils.isEmpty(selfDeclIdList)){
            return null;
        }
        for (int i = 0; i < selfDeclIdList.size(); i++){
            String configId = selfDeclIdList.get(i);
            ChecklistConfigDto dto = hcsaChklClient.getChecklistConfigById(configId).getEntity();
            if(dto != null){
                if(dto.isCommon()){
                    commonDto = fillupChklistService.transferToInspectionCheckListDto(dto,refNo);
                    commonDto.setConfigId(configId);
                }else if(!dto.isCommon()){
                    InspectionFillCheckListDto fDto = fillupChklistService.transferToInspectionCheckListDto(dto,refNo);
                    fDto.setSvcName(dto.getSvcName());
                    fDto.setConfigId(configId);
                    fDto.setSvcCode(dto.getSvcCode());
                    chkDtoList.add(fDto);
                }
            }
        }
        map.put(commonDto, chkDtoList);
        return map;
    }

    /**
     * @param start
     * @param end
     * @return Calculate the date between two days
     */
    private static List<Date> getBetweenDays(Date start, Date end) {
        List<Date> result = new ArrayList<>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        tempStart.add(Calendar.DAY_OF_YEAR, 1);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        tempEnd.add(Calendar.DAY_OF_YEAR, 1);
        result.add(start);
        while (tempStart.before(tempEnd)) {
            result.add(tempStart.getTime());
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }

    private void updateInspectionStatus(String appPremCorrId, String status) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorrId).getEntity();
        appInspectionStatusDto.setStatus(status);
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appInspectionStatusClient.update(appInspectionStatusDto);
    }

    private ApplicationDto updateApplication(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationViewService.updateApplicaiton(applicationDto);
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus, String stageId, String internalRemarks,
                                                                         String processDec, String roleId, String workGroupId, String subStage){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setProcessDecision(processDec);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setWrkGrpId(workGroupId);
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto).getEntity();
        return appPremisesRoutingHistoryDto;
    }
}
