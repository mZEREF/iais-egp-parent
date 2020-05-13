package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskInspectionComplianceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionHistoryShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionPreTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionPreTaskService;
import com.ecquaria.cloud.moh.iais.service.LicenseeService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2019/12/9 9:58
 **/
@Service
@Slf4j
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
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private FillupChklistService fillupChklistService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private LicenseeService licenseeService;

    @Override
    public ApplicationDto getAppStatusByTaskId(TaskDto taskDto) {
        ApplicationViewDto applicationViewDto = applicationClient.getAppViewByCorrelationId(taskDto.getRefNo()).getEntity();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        return applicationDto;
    }

    @Override
    public List<SelectOption> getProcessDecOption(String appStatus) {
        String[] processDecArr;
        if(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(appStatus)) {
            processDecArr = new String[]{InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY};
        } else {
            processDecArr = new String[]{InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION,
                    InspectionConstants.PROCESS_DECI_ROUTE_BACK_APSO,
                    InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY};
        }
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
        applicationDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
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
    public void routingBack(TaskDto taskDto, InspectionPreTaskDto inspectionPreTaskDto, LoginContext loginContext) throws IOException, TemplateException {
        String reMarks = inspectionPreTaskDto.getReMarks();
        ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppCorrId(taskDto.getRefNo());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        ApplicationGroupDto applicationGroupDto = applicationViewDto.getApplicationGroupDto();
        String licenseeId = applicationGroupDto.getLicenseeId();
        LicenseeDto licenseeDto = licenseeService.getLicenseeDtoById(licenseeId);
        String applicationNo = applicationDto.getApplicationNo();
        createAppPremisesRoutingHistory(applicationNo, applicationDto.getStatus(), taskDto.getTaskKey(), reMarks, InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION, RoleConsts.USER_ROLE_INSPECTIOR, taskDto.getWkGrpId(), null);
        ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_CLARIFICATION);
        applicationDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
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
        //RFI Do
        String appRfiDecision = null;
        String selfRfiDecision = null;
        List<String> preInspRfiCheck = inspectionPreTaskDto.getPreInspRfiCheck();
        if(!IaisCommonUtils.isEmpty(preInspRfiCheck)){
            for(String decision : preInspRfiCheck){
                if(InspectionConstants.SWITCH_ACTION_APPLICATION.equals(decision)){
                    appRfiDecision = decision;
                } else if (InspectionConstants.SWITCH_ACTION_SELF.equals(decision)){
                    selfRfiDecision = decision;
                }
            }
        }
        //app rfi
        if(!StringUtil.isEmpty(appRfiDecision)){
            String preInspecComments = inspectionPreTaskDto.getPreInspecComments();
            applicationService.applicationRfiAndEmail(applicationViewDto, applicationDto, licenseeId,
                    licenseeDto, loginContext, preInspecComments);
        }
        //self rfi
        if(!StringUtil.isEmpty(selfRfiDecision)){
            InterMessageDto interMessageDto = new InterMessageDto();
            interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
            interMessageDto.setSubject(MessageConstants.MESSAGE_SUBJECT_REQUEST_FOR_INFORMATION);
            interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
            String mesNO = inboxMsgService.getMessageNo();
            interMessageDto.setRefNo(mesNO);
            interMessageDto.setService_id(applicationDto1.getServiceId());
            String url = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() +
                    MessageConstants.MESSAGE_INBOX_URL_REQUEST_SELF_CHECKLIST + applicationGroupDto.getId() +
                    "&selfDeclAction=rfi";
            HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
            maskParams.put("appPremCorrId", taskDto.getRefNo());
            maskParams.put("selfDeclAction", "rfi");
            MsgTemplateDto autoEntity = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_RFI).getEntity();
            Map<String ,Object> map=IaisCommonUtils.genNewHashMap();
            map.put("APPLICANT_NAME",licenseeDto.getName());
            map.put("DETAILS","");
            map.put("COMMENTS",StringUtil.viewHtml(""));
            map.put("A_HREF",url);
            map.put("MOH_NAME",AppConsts.MOH_AGENCY_NAME);
            String templateMessageByContent = MsgUtil.getTemplateMessageByContent(autoEntity.getMessageContent(), map);
            interMessageDto.setMsgContent(templateMessageByContent);
            interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            interMessageDto.setUserId(licenseeId);
            interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            interMessageDto.setMaskParams(maskParams);
            inboxMsgService.saveInterMessage(interMessageDto);
        }
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

    @Override
    public List<SelectOption> getRfiCheckOption() {
        List<SelectOption> rfiCheckOption = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption(InspectionConstants.SWITCH_ACTION_APPLICATION, "Application");
        SelectOption so2 = new SelectOption(InspectionConstants.SWITCH_ACTION_SELF, "Self-Assessment Checklists");
        rfiCheckOption.add(so1);
        rfiCheckOption.add(so2);
        return rfiCheckOption;
    }

    @Override
    public void routingAsoPsoBack(TaskDto taskDto, InspectionPreTaskDto inspectionPreTaskDto, LoginContext loginContext) {
        ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppCorrId(taskDto.getRefNo());
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        String applicationNo = applicationDto.getApplicationNo();
        //routing
        //get check stage
        String stageKey = inspectionPreTaskDto.getCheckRbStage();
        String checkUserId = inspectionPreTaskDto.getStageUserIdMap().get(stageKey);
        //update task
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(taskService.remainDays(taskDto));
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setAuditTrailDto(auditTrailDto);
        taskService.updateTask(taskDto);
        //create task
        TaskDto compTask = getCompletedTaskByHistory(taskDto, checkUserId);
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
        if(compTask != null){
            List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_INS);
            hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
            TaskDto createTask = new TaskDto();
            createTask.setId(null);
            createTask.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
            createTask.setPriority(compTask.getPriority());
            createTask.setRefNo(compTask.getRefNo());
            createTask.setSlaAlertInDays(compTask.getSlaAlertInDays());
            createTask.setSlaDateCompleted(null);
            createTask.setSlaInDays(compTask.getSlaInDays());
            createTask.setSlaRemainInDays(null);
            createTask.setTaskKey(compTask.getTaskKey());
            createTask.setTaskType(compTask.getTaskType());
            createTask.setWkGrpId(compTask.getWkGrpId());
            createTask.setUserId(checkUserId);
            createTask.setDateAssigned(new Date());
            createTask.setRoleId(stageKey);
            createTask.setAuditTrailDto(auditTrailDto);
            createTask.setProcessUrl(TaskConsts.TASK_PROCESS_URL_MAIN_FLOW);
            taskDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
            taskDtoList.add(createTask);
        }
        taskService.createTasks(taskDtoList);
        String appPremCorrId = taskDto.getRefNo();
        //update inspection status
        updateInspectionStatus(appPremCorrId, InspectionConstants.INSPECTION_STATUS_PENDING_REQUEST_FOR_INFORMATION);
        //update App
        ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ENQUIRE);
        applicationDto1.setAuditTrailDto(auditTrailDto);
        applicationService.updateFEApplicaiton(applicationDto1);
        //create history
        createAppPremisesRoutingHistory(applicationNo, applicationDto.getStatus(), HcsaConsts.ROUTING_STAGE_INS, inspectionPreTaskDto.getReMarks(),
                InspectionConstants.PROCESS_DECI_ROUTE_BACK_APSO, taskDto.getRoleId(), taskDto.getWkGrpId(), HcsaConsts.ROUTING_STAGE_PRE);
    }

    @Override
    public List<InspectionHistoryShowDto> getInspectionHistory(String originLicenceId) {
        List<InspectionHistoryShowDto> inspectionHistoryShowDtos = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(originLicenceId)) {
            List<LicAppCorrelationDto> licAppCorrelationDtos = hcsaLicenceClient.getLicCorrBylicId(originLicenceId).getEntity();
            if (!IaisCommonUtils.isEmpty(licAppCorrelationDtos)) {
                int index = 0;
                for (LicAppCorrelationDto licAppCorrelationDto : licAppCorrelationDtos) {
                    if (index <= 1) {
                        InspectionHistoryShowDto inspectionHistoryShowDto = new InspectionHistoryShowDto();
                        String appId = licAppCorrelationDto.getApplicationId();
                        LicenceDto licenceDto = hcsaLicenceClient.getLicenceDtoById(originLicenceId).getEntity();
                        Date licStartDate = licenceDto.getStartDate();
                        Date licEndDate = licenceDto.getExpiryDate();
                        if (licStartDate != null && licEndDate != null) {
                            String startDateStr = Formatter.formatDateTime(licStartDate, "dd/MM/yyyy");
                            String endDateStr = Formatter.formatDateTime(licEndDate, "dd/MM/yyyy");
                            String licPeriod = startDateStr + " - " + endDateStr;
                            inspectionHistoryShowDto.setLicencePeriod(licPeriod);
                        }
                        ApplicationDto applicationDto = applicationClient.getApplicationById(appId).getEntity();
                        //get service name
                        String serviceId = applicationDto.getServiceId();
                        HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
                        inspectionHistoryShowDto.setServiceName(hcsaServiceDto.getSvcName());
                        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appId).getEntity();
                        //get task refNo. (appPremCorrId)
                        String taskRefNo = appPremisesCorrelationDto.getId();
                        //get inspection date
                        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(taskRefNo, InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                        //get remarks
                        AppPremisesRecommendationDto appPremisesRecommendationDto2 = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(taskRefNo, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
                        String remark;
                        if(appPremisesRecommendationDto2 != null) {
                            remark = StringUtil.viewHtml(appPremisesRecommendationDto2.getRemarks());
                        } else {
                            remark = "-";
                        }
                        //get group premises and address
                        AppGrpPremisesDto appGrpPremisesDto = inspectionAssignTaskService.getAppGrpPremisesDtoByAppGroId(taskRefNo);
                        String hciCode = StringUtil.viewHtml(appGrpPremisesDto.getHciCode());
                        String hciName = appGrpPremisesDto.getHciName();
                        String address = inspectionAssignTaskService.getAddress(appGrpPremisesDto);
                        inspectionHistoryShowDto.setHciCode(hciCode);
                        if (StringUtil.isEmpty(hciName)) {
                            inspectionHistoryShowDto.setHciNameAddress(address);
                        } else {
                            String hciNameAddress = hciName + " / " + address;
                            inspectionHistoryShowDto.setHciNameAddress(hciNameAddress);
                        }
                        //get inspectors and leads
                        inspectionHistoryShowDto = getInspectorAndLeadByRefNo(taskRefNo, inspectionHistoryShowDto);
                        //get risk lvl
                        HcsaRiskInspectionComplianceDto hcsaRiskInspectionComplianceDto = getRiskLevelByRefNo(taskRefNo, hcsaServiceDto.getSvcCode());
                        String riskLvl = "-";
                        if (hcsaRiskInspectionComplianceDto != null) {
                            riskLvl = StringUtil.viewHtml(hcsaRiskInspectionComplianceDto.getRiskRating());
                        }
                        inspectionHistoryShowDto.setComplianceRisk(riskLvl);
                        inspectionHistoryShowDto.setRemark(remark);
                        if(appPremisesRecommendationDto != null){
                            inspectionHistoryShowDto.setInspDate(appPremisesRecommendationDto.getRecomInDate());
                        }
                        inspectionHistoryShowDtos.add(inspectionHistoryShowDto);
                        index++;
                    }
                }
            }
        }
        return inspectionHistoryShowDtos;
    }

    @Override
    public InspectionPreTaskDto getPreInspRbOption(String applicationNo, InspectionPreTaskDto inspectionPreTaskDto) {
        List<SelectOption> preInspRbOption = IaisCommonUtils.genNewArrayList();
        Map<String, String> userIdMap = IaisCommonUtils.genNewHashMap();
        //get history
        AppPremisesRoutingHistoryDto asoHistory = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorysByAppNoAndStageId(applicationNo, HcsaConsts.ROUTING_STAGE_ASO).getEntity();
        AppPremisesRoutingHistoryDto psoHistory = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorysByAppNoAndStageId(applicationNo, HcsaConsts.ROUTING_STAGE_PSO).getEntity();
        if(psoHistory != null){
            HcsaSvcRoutingStageDto asoStageDto = hcsaConfigClient.getHcsaSvcRoutingStageById(HcsaConsts.ROUTING_STAGE_ASO).getEntity();
            HcsaSvcRoutingStageDto psoStageDto = hcsaConfigClient.getHcsaSvcRoutingStageById(HcsaConsts.ROUTING_STAGE_PSO).getEntity();
            String asoUserId = asoHistory.getActionby();
            String psoUserId = psoHistory.getActionby();
            SelectOption asoSo = new SelectOption(RoleConsts.USER_ROLE_ASO, asoStageDto.getStageName());
            SelectOption psoSo = new SelectOption(RoleConsts.USER_ROLE_PSO, psoStageDto.getStageName());
            preInspRbOption.add(asoSo);
            preInspRbOption.add(psoSo);
            userIdMap.put(RoleConsts.USER_ROLE_ASO, asoUserId);
            userIdMap.put(RoleConsts.USER_ROLE_PSO, psoUserId);
        } else {
            HcsaSvcRoutingStageDto asoStageDto = hcsaConfigClient.getHcsaSvcRoutingStageById(HcsaConsts.ROUTING_STAGE_ASO).getEntity();
            String asoUserId = asoHistory.getActionby();
            SelectOption asoSo = new SelectOption(RoleConsts.USER_ROLE_ASO, asoStageDto.getStageName());
            preInspRbOption.add(asoSo);
            userIdMap.put(RoleConsts.USER_ROLE_ASO, asoUserId);
        }
        inspectionPreTaskDto.setPreInspRbOption(preInspRbOption);
        inspectionPreTaskDto.setStageUserIdMap(userIdMap);
        return inspectionPreTaskDto;
    }

    private HcsaRiskInspectionComplianceDto getRiskLevelByRefNo(String taskRefNo, String serviceCode) {
        HcsaRiskInspectionComplianceDto hcsaRiskInspectionComplianceDto = new HcsaRiskInspectionComplianceDto();
        AppPremPreInspectionNcDto appPremPreInspectionNcDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(taskRefNo).getEntity();
        if(appPremPreInspectionNcDto != null) {
            String ncId = appPremPreInspectionNcDto.getId();
            List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos = fillUpCheckListGetAppClient.getAppNcItemByNcId(ncId).getEntity();
            if(!IaisCommonUtils.isEmpty(appPremisesPreInspectionNcItemDtos)){
                int ncCountThCritical = 0;
                int ncCountThMajor = 0;
                int ncCountThMinor = 0;
                for(AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto : appPremisesPreInspectionNcItemDtos){
                    String itemId = appPremisesPreInspectionNcItemDto.getItemId();
                    ChecklistItemDto checklistItemDto = hcsaChklClient.getChklItemById(itemId).getEntity();
                    String riskRating = checklistItemDto.getRiskLevel();
                    if(RiskConsts.CRITICAL.equals(riskRating)){
                        ncCountThCritical++;
                    } else if(RiskConsts.MAJOR.equals(riskRating)){
                        ncCountThMajor++;
                    } else if(RiskConsts.MINOR.equals(riskRating)){
                        ncCountThMinor++;
                    }
                }
                hcsaRiskInspectionComplianceDto.setNcCountThCritical(ncCountThCritical);
                hcsaRiskInspectionComplianceDto.setNcCountThMajor(ncCountThMajor);
                hcsaRiskInspectionComplianceDto.setNcCountThMinor(ncCountThMinor);
                hcsaRiskInspectionComplianceDto.setSvcCode(serviceCode);
                hcsaRiskInspectionComplianceDto = hcsaConfigClient.getHcsaRiskInspectionComplianceDto(hcsaRiskInspectionComplianceDto).getEntity();
            }
        }
        return hcsaRiskInspectionComplianceDto;
    }

    private InspectionHistoryShowDto getInspectorAndLeadByRefNo(String taskRefNo, InspectionHistoryShowDto inspectionHistoryShowDto) {
        List<TaskDto> taskDtos = organizationClient.getTaskByRefNoStatus(taskRefNo, TaskConsts.TASK_STATUS_COMPLETED, TaskConsts.TASK_PROCESS_URL_INSPECTION_CHECKLIST_VERIFY).getEntity();
        if(!IaisCommonUtils.isEmpty(taskDtos)){
            List<String> inspectorNames = IaisCommonUtils.genNewArrayList();
            for(TaskDto taskDto : taskDtos){
                String userId = taskDto.getUserId();
                OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
                String name = orgUserDto.getDisplayName();
                inspectorNames.add(name);
            }
            inspectionHistoryShowDto.setInspectors(inspectorNames);
            AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(taskRefNo, InspectionConstants.RECOM_TYPE_INSPECTION_LEAD).getEntity();
            if(appPremisesRecommendationDto != null) {
                String nameStr = appPremisesRecommendationDto.getRecomDecision();
                if(!StringUtil.isEmpty(nameStr)) {
                    List<String> leadNameList = IaisCommonUtils.genNewArrayList();
                    String[] leadNameStr = nameStr.split(",");
                    for(int i = 0; i < leadNameStr.length; i++){
                        String leadName = leadNameStr[i].trim();
                        leadNameList.add(leadName);
                    }
                    inspectionHistoryShowDto.setInspLeads(leadNameList);
                }
            }
        }
        return inspectionHistoryShowDto;
    }

    private TaskDto getCompletedTaskByHistory(TaskDto taskDto, String userId) {
        List<TaskDto> taskDtos = organizationClient.getTaskByRefNoStatus(taskDto.getRefNo(), TaskConsts.TASK_STATUS_COMPLETED, TaskConsts.TASK_PROCESS_URL_MAIN_FLOW).getEntity();
        if(!IaisCommonUtils.isEmpty(taskDtos)){
            for(TaskDto tDto : taskDtos){
                if(tDto.getUserId().equals(userId)){
                    return tDto;
                }
            }
        }
        return new TaskDto();
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

    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId){
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
        for(ApplicationDto applicationDto : applicationDtos){
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        return hcsaSvcStageWorkingGroupDtos;
    }
}
