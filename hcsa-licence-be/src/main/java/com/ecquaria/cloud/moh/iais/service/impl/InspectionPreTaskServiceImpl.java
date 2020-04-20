package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
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
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private HcsaChklClient hcsaChklClient;

    @Autowired
    private FillupChklistService fillupChklistService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;

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
        createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), preInspecRemarks, InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY, RoleConsts.USER_ROLE_INSPECTIOR, taskDto.getWkGrpId(), null);
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
        createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), reMarks, InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION, RoleConsts.USER_ROLE_INSPECTIOR, taskDto.getWkGrpId(), null);
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
        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
        interMessageDto.setSubject(MessageConstants.MESSAGE_SUBJECT_REQUEST_FOR_INFORMATION);
        interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
        String mesNO = inboxMsgService.getMessageNo();
        interMessageDto.setRefNo(mesNO);
        interMessageDto.setService_id(applicationDto1.getServiceId());
        String url = systemParamConfig.getInterServerName() +
                MessageConstants.MESSAGE_CALL_BACK_URL_NEWAPPLICATION +
                applicationDto1.getApplicationNo();
        //interMessageDto.setProcessUrl(url);
        interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        inboxMsgService.saveInterMessage(interMessageDto);
    }

    @Override
    public Map<InspectionFillCheckListDto, List<InspectionFillCheckListDto>> getSelfCheckListByCorrId(String refNo) {
        Map<InspectionFillCheckListDto, List<InspectionFillCheckListDto>> map = IaisCommonUtils.genNewHashMap();
        List<InspectionFillCheckListDto> chkDtoList = IaisCommonUtils.genNewArrayList();
        InspectionFillCheckListDto commonDto = new InspectionFillCheckListDto();
        List<String> ids = IaisCommonUtils.genNewArrayList();
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

    @Override
    public LicenceDto getLicenceDtoByLicenceId(String originLicenceId) {
        LicenceDto licenceDto = new LicenceDto();
        if(!StringUtil.isEmpty(originLicenceId)){
            licenceDto = hcsaLicenceClient.getLicenceDtoById(originLicenceId).getEntity();
        }
        return licenceDto;
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

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus, String stageId, String internalRemarks,
                                                                         String processDec, String roleId, String workGroupId, String subStage){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
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
