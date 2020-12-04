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
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskInspectionComplianceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
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
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionPreTaskService;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private NotificationHelper notificationHelper;

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

    static private String[] processDec = new String[]{InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION,
            InspectionConstants.PROCESS_DECI_ROUTE_BACK_APSO,
            InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY};

    @Override
    public ApplicationDto getAppStatusByTaskId(TaskDto taskDto) {
        ApplicationViewDto applicationViewDto = applicationClient.getAppViewByCorrelationId(taskDto.getRefNo()).getEntity();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        return applicationDto;
    }

    @Override
    public List<SelectOption> getProcessDecOption(String appType) {
        String[] processDecArr;
        if(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(appType)) {
            processDecArr = new String[]{InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY};
        } else {
            processDecArr = processDec;
        }
        List<SelectOption> processDecOption = MasterCodeUtil.retrieveOptionsByCodes(processDecArr);
        return processDecOption;
    }

    @Override
    public void routingTask(TaskDto taskDto, String preInspecRemarks, List<ChecklistConfigDto> inspectionChecklist) {
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppCorrId(taskDto.getRefNo());
        taskDto.setSlaDateCompleted(new Date());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), preInspecRemarks, InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY, RoleConsts.USER_ROLE_INSPECTIOR, taskDto.getWkGrpId(), HcsaConsts.ROUTING_STAGE_PRE);
        //close self-checklist
        applicationDto.setSelfAssMtFlag(4);
        ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION);
        applicationDto1.setAuditTrailDto(auditTrailDto);
        applicationService.updateFEApplicaiton(applicationDto1);
        applicationViewDto.setApplicationDto(applicationDto1);
        List<TaskDto> taskDtoList = organizationClient.getCurrTaskByRefNo(taskDto.getRefNo()).getEntity();
        List<TaskDto> createTaskList = IaisCommonUtils.genNewArrayList();
        //CREATE / UPDATE TASK
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_INS);
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        //get score
        int score = hcsaSvcStageWorkingGroupDtos.get(0).getCount();
        for(TaskDto td : taskDtoList){
            td.setSlaDateCompleted(new Date());
            td.setSlaRemainInDays(0);
            td.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
            td.setAuditTrailDto(auditTrailDto);
            taskService.updateTask(td);
            TaskDto createTaskDto = new TaskDto();
            createTaskDto.setId(null);
            createTaskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
            createTaskDto.setPriority(td.getPriority());
            createTaskDto.setRefNo(td.getRefNo());
            createTaskDto.setSlaAlertInDays(td.getSlaAlertInDays());
            createTaskDto.setSlaDateCompleted(null);
            createTaskDto.setSlaInDays(td.getSlaInDays());
            createTaskDto.setSlaRemainInDays(null);
            createTaskDto.setTaskKey(td.getTaskKey());
            createTaskDto.setTaskType(td.getTaskType());
            createTaskDto.setWkGrpId(td.getWkGrpId());
            createTaskDto.setUserId(td.getUserId());
            createTaskDto.setDateAssigned(new Date());
            createTaskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
            createTaskDto.setAuditTrailDto(auditTrailDto);
            createTaskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_CHECKLIST_VERIFY);
            createTaskDto.setScore(score);
            createTaskDto.setApplicationNo(td.getApplicationNo());
            createTaskList.add(createTaskDto);
        }
        taskService.createTasks(createTaskList);
        //create history
        createAppPremisesRoutingHistory(applicationDto.getApplicationNo(),ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION,taskDto.getTaskKey(),null,
                InspectionConstants.PROCESS_DECI_PENDING_INSPECTION, RoleConsts.USER_ROLE_INSPECTIOR, taskDto.getWkGrpId(), HcsaConsts.ROUTING_STAGE_INP);
        //save checklist
        saveInspectionChecklist(inspectionChecklist, taskDto.getRefNo());
        //update insp status
        updateInspectionStatus(taskDto.getRefNo(), InspectionConstants.INSPECTION_STATUS_PENDING_INSPECTION);
    }

    private void saveInspectionChecklist(List<ChecklistConfigDto> inspectionChecklist, String appCorrId) {
        List<AppPremisesPreInspectChklDto> appPremisesPreInspectChklDtos = fillUpCheckListGetAppClient.getPremInsChklList(appCorrId).getEntity();
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        if(!IaisCommonUtils.isEmpty(appPremisesPreInspectChklDtos)) {
            int version = Integer.parseInt(appPremisesPreInspectChklDtos.get(0).getVersion());
            int newVersion = version++;
            for(AppPremisesPreInspectChklDto appPremisesPreInspectChklDto : appPremisesPreInspectChklDtos){
                appPremisesPreInspectChklDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                appPremisesPreInspectChklDto.setAuditTrailDto(auditTrailDto);
                fillUpCheckListGetAppClient.updateAppPreInspChkl(appPremisesPreInspectChklDto);
            }
            for (ChecklistConfigDto ccDto : inspectionChecklist) {
                AppPremisesPreInspectChklDto appDto = new AppPremisesPreInspectChklDto();
                appDto.setId(null);
                appDto.setAppPremCorrId(appCorrId);
                appDto.setVersion(newVersion + "");
                appDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appDto.setChkLstConfId(ccDto.getId());
                appDto.setAuditTrailDto(auditTrailDto);
                fillUpCheckListGetAppClient.saveAppPreInspChkl(appDto);
            }
        } else {
            for (ChecklistConfigDto ccDto : inspectionChecklist) {
                AppPremisesPreInspectChklDto appDto = new AppPremisesPreInspectChklDto();
                appDto.setId(null);
                appDto.setAppPremCorrId(appCorrId);
                appDto.setVersion(AppConsts.YES);
                appDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appDto.setChkLstConfId(ccDto.getId());
                appDto.setAuditTrailDto(auditTrailDto);
                fillUpCheckListGetAppClient.saveAppPreInspChkl(appDto);
            }
        }
    }

    @Override
    public void routingBack(TaskDto taskDto, InspectionPreTaskDto inspectionPreTaskDto, LoginContext loginContext, List<PremCheckItem> premCheckItems, ApplicationViewDto applicationViewDto) throws IOException, TemplateException {
        String reMarks = inspectionPreTaskDto.getReMarks();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        ApplicationGroupDto applicationGroupDto = applicationViewDto.getApplicationGroupDto();
        String applicantId = applicationGroupDto.getSubmitBy();
        OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicantId).getEntity();
        String applicantName = orgUserDto.getDisplayName();
        String applicationNo = applicationDto.getApplicationNo();
        createAppPremisesRoutingHistory(applicationNo, applicationDto.getStatus(), taskDto.getTaskKey(), reMarks, InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION, RoleConsts.USER_ROLE_INSPECTIOR, taskDto.getWkGrpId(), null);

        List<TaskDto> taskDtoList = organizationClient.getTasksByRefNo(taskDto.getRefNo()).getEntity();
        for(TaskDto tDto : taskDtoList){
            if(tDto.getTaskStatus().equals(TaskConsts.TASK_STATUS_PENDING) || tDto.getTaskStatus().equals(TaskConsts.TASK_STATUS_READ)) {
                tDto.setSlaDateCompleted(new Date());
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
        ApplicationDto applicationDto1;
        //app rfi
        int selfAssMtFlag = 0;
        if(!StringUtil.isEmpty(selfRfiDecision)){
            if(IaisCommonUtils.isEmpty(premCheckItems)) {
                selfAssMtFlag = 0;
            } else {
                selfAssMtFlag = 2;
            }
        }
        String preInspecComments = inspectionPreTaskDto.getPreInspecComments();
        if(!StringUtil.isEmpty(appRfiDecision)){
            applicationService.applicationRfiAndEmail(applicationViewDto, applicationDto, loginContext, preInspecComments);
            if(!StringUtil.isEmpty(selfRfiDecision)){
                applicationDto.setSelfAssMtFlag(selfAssMtFlag);
            }
            applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION);
            if(!StringUtil.isEmpty(selfRfiDecision)){
                applicationDto1.setSelfAssMtFlag(selfAssMtFlag);
            }
            applicationDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            applicationService.updateFEApplicaiton(applicationDto1);
            applicationViewDto.setApplicationDto(applicationDto1);
        } else {
            applicationDto.setSelfAssMtFlag(selfAssMtFlag);
            applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_CLARIFICATION);
            applicationDto1.setSelfAssMtFlag(selfAssMtFlag);
            applicationDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            applicationService.updateFEApplicaiton(applicationDto1);
            applicationViewDto.setApplicationDto(applicationDto1);
        }
        //self rfi
        if(!StringUtil.isEmpty(selfRfiDecision)){
            String url;
            HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
            //Have you completed a self-assessment checklist?
            if(IaisCommonUtils.isEmpty(premCheckItems)) {
                url = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() +
                        MessageConstants.MESSAGE_INBOX_URL_REQUEST_SELF_CHECKLIST_GROUP + applicationDto.getAppGrpId() +
                        "&selfDeclApplicationNumber=" + taskDto.getApplicationNo();
                maskParams.put("appGroupId", applicationDto.getAppGrpId());
                maskParams.put("selfDeclApplicationNumber", taskDto.getApplicationNo());
            } else {
                url = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() +
                        MessageConstants.MESSAGE_INBOX_URL_REQUEST_SELF_CHECKLIST + taskDto.getApplicationNo() +
                        "&selfDeclAction=rfi";
                maskParams.put("selfDeclApplicationNumber", taskDto.getApplicationNo());
            }
            Date now = new Date();
            if(applicationViewDto.getApplicationGroupDto() != null && applicationViewDto.getApplicationGroupDto().getSubmitDt() != null){
                now = applicationViewDto.getApplicationGroupDto().getSubmitDt();
            }
            String editSelect = "";
            //judge premises amend or licence amend
            AppEditSelectDto appEditSelectDto = applicationViewDto.getAppEditSelectDto();
            if(appEditSelectDto != null && !StringUtil.isEmpty(appRfiDecision)){
                if(appEditSelectDto.isPremisesEdit()){
                    editSelect = editSelect + "Premises";
                }
                if(appEditSelectDto.isDocEdit()){
                    editSelect = editSelect +(StringUtil.isEmpty(editSelect)?"":", ") + "Primary Documents";
                }
                if(appEditSelectDto.isServiceEdit()){
                    editSelect = editSelect + (StringUtil.isEmpty(editSelect)?"":", ") + "Service Related Information - " + applicationViewDto.getServiceType();
                }
                if(appEditSelectDto.isPoEdit()){
                    editSelect = editSelect + (StringUtil.isEmpty(editSelect)?"":", ") + "PO";
                }
                if(appEditSelectDto.isDpoEdit()){
                    editSelect = editSelect + (StringUtil.isEmpty(editSelect)?"":", ") + "DPO";
                }
                if(appEditSelectDto.isMedAlertEdit()){
                    editSelect = editSelect + (StringUtil.isEmpty(editSelect)?"":", ") + "medAlert";
                }
            }
            String remarks = "Sections Allowed for Change : " + editSelect;
            int rfiDueDate = systemParamConfig.getRfiDueDate();
            LocalDate tatTime = LocalDate.now().plusDays(rfiDueDate);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Formatter.DATE);
            String tatTimeStr = tatTime.format(dtf);
            String appType = MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType());
            Map<String ,Object> map = IaisCommonUtils.genNewHashMap();
            map.put("ApplicantName", applicantName);
            map.put("ApplicationType", appType);
            map.put("ApplicationNumber", StringUtil.viewHtml(applicationNo));
            map.put("ApplicationDate", Formatter.formatDateTime(now, Formatter.DATE));
            if(!StringUtil.isEmpty(appRfiDecision)) {
                map.put("Remarks", remarks);
            }
            map.put("systemLink", url);
            map.put("TATtime", tatTimeStr);
            map.put("email", systemParamConfig.getSystemAddressOne());
            map.put("MOH_AGENCY_NAM_GROUP", "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "</b>");
            map.put("MOH_AGENCY_NAME", "<b>" + AppConsts.MOH_AGENCY_NAME + "</b>");
            List<String> serviceCodes = IaisCommonUtils.genNewArrayList();
            String serviceId = applicationDto.getServiceId();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
            String serviceCode = hcsaServiceDto.getSvcCode();
            serviceCodes.add(serviceCode);
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_MSG);
            emailParam.setMaskParams(maskParams);
            emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_ACTION_REQUIRED);
            emailParam.setQueryCode(applicationNo);
            emailParam.setReqRefNum(applicationNo);
            emailParam.setTemplateContent(map);
            emailParam.setRefId(applicationNo);
            emailParam.setSvcCodeList(serviceCodes);
            MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_MSG).getEntity();
            String subject = "";
            if(msgTemplateDto != null){
                String templateName = msgTemplateDto.getTemplateName();
                if(!StringUtil.isEmpty(templateName)){
                    Map<String, Object> mapSubject = IaisCommonUtils.genNewHashMap();
                    mapSubject.put("ApplicationType", appType);
                    mapSubject.put("ApplicationNumber", applicationNo);
                    try {
                        subject = MsgUtil.getTemplateMessageByContent(templateName, mapSubject);
                    } catch (IOException | TemplateException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            emailParam.setSubject(subject);
            notificationHelper.sendNotification(emailParam);
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
            licenceDto = hcsaLicenceClient.getLicDtoById(originLicenceId).getEntity();
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
        List<TaskDto> taskDtos = organizationClient.getTasksByRefNo(taskDto.getRefNo()).getEntity();
        for(TaskDto tDto : taskDtos){
            if(tDto.getTaskStatus().equals(TaskConsts.TASK_STATUS_PENDING) || tDto.getTaskStatus().equals(TaskConsts.TASK_STATUS_READ)) {
                tDto.setSlaDateCompleted(new Date());
                tDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
                tDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                taskService.updateTask(tDto);
            }
        }
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
            createTask.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
            createTask.setDateAssigned(new Date());
            createTask.setRoleId(stageKey);
            createTask.setApplicationNo(applicationNo);
            createTask.setAuditTrailDto(auditTrailDto);
            createTask.setProcessUrl(TaskConsts.TASK_PROCESS_URL_MAIN_FLOW);
            taskDtoList.add(createTask);
        }
        taskService.createTasks(taskDtoList);
        String appPremCorrId = taskDto.getRefNo();
        //update inspection status
        updateInspectionStatus(appPremCorrId, InspectionConstants.INSPECTION_STATUS_PRE_INSPECTION_ROUTE_BACK_APSO);
        //update App
        ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ROUTE_BACK);
        applicationDto1.setAuditTrailDto(auditTrailDto);
        applicationService.updateFEApplicaiton(applicationDto1);
        //create history
        createAppPremisesRoutingHistory(applicationNo, applicationDto.getStatus(), HcsaConsts.ROUTING_STAGE_INS, inspectionPreTaskDto.getReMarks(),
                InspectionConstants.PROCESS_DECI_ROUTE_BACK_APSO, taskDto.getRoleId(), taskDto.getWkGrpId(), HcsaConsts.ROUTING_STAGE_PRE);
        createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(), applicationDto1.getStatus(), compTask.getTaskKey(), null,
                null, HcsaConsts.ROUTING_STAGE_INS, taskDto.getWkGrpId(), null);
    }

    @Override
    public List<InspectionHistoryShowDto> getInspectionHistory(String originLicenceId, String applicationId) {
        List<InspectionHistoryShowDto> inspectionHistoryShowDtos = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(originLicenceId)) {
            List<ApplicationDto> applicationDtos = inspectionTaskClient.getInspHistoryAppByLicId(originLicenceId, applicationId).getEntity();
            if (!IaisCommonUtils.isEmpty(applicationDtos)) {
                int index = 0;
                for (ApplicationDto applicationDto : applicationDtos) {
                    if (index <= 1) {
                        InspectionHistoryShowDto inspectionHistoryShowDto = new InspectionHistoryShowDto();
                        String appId = applicationDto.getId();
                        LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(originLicenceId).getEntity();
                        Date licStartDate = licenceDto.getStartDate();
                        Date licEndDate = licenceDto.getExpiryDate();
                        if (licStartDate != null && licEndDate != null) {
                            String startDateStr = Formatter.formatDateTime(licStartDate, "dd/MM/yyyy");
                            String endDateStr = Formatter.formatDateTime(licEndDate, "dd/MM/yyyy");
                            String licPeriod = startDateStr + " - " + endDateStr;
                            inspectionHistoryShowDto.setLicencePeriod(licPeriod);
                        }
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
                        //get history Hci_code
                        String hciCode = getHciCodeByFiveField(appGrpPremisesDto, originLicenceId);
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
                        inspectionHistoryShowDto = getInspectorAndLeadByAppNo(applicationDto.getApplicationNo(), taskRefNo, inspectionHistoryShowDto);
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

    private String getHciCodeByFiveField(AppGrpPremisesDto appGrpPremisesDto, String originLicenceId) {
        String postalCode = appGrpPremisesDto.getPostalCode();
        String unitNo = appGrpPremisesDto.getUnitNo();
        String floorNo = appGrpPremisesDto.getFloorNo();
        String blkNo = appGrpPremisesDto.getBlkNo();
        String premisesType = appGrpPremisesDto.getPremisesType();
        String hicCode = "-";
        List<PremisesDto> premisesDtos = hcsaLicenceClient.getPremisess(originLicenceId).getEntity();
        if(!IaisCommonUtils.isEmpty(premisesDtos)){
            for(PremisesDto premisesDto : premisesDtos){
                if(!StringUtil.isEmpty(premisesType) && premisesType.equals(premisesDto.getPremisesType())){
                    String premisesStr = null;
                    String premisesStr2 = null;
                    if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType)) {
                        String hciName = appGrpPremisesDto.getHciName();
                        premisesStr = splicingAddressInformation(postalCode, unitNo, floorNo, blkNo, hciName);
                        premisesStr2 = splicingAddressInformation(premisesDto.getPostalCode(), premisesDto.getUnitNo(), premisesDto.getFloorNo(),
                                premisesDto.getBlkNo(), premisesDto.getHciName());
                    } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)) {
                        String vehicleNo = appGrpPremisesDto.getConveyanceVehicleNo();
                        premisesStr = splicingAddressInformation(postalCode, unitNo, floorNo, blkNo, vehicleNo);
                        premisesStr2 = splicingAddressInformation(premisesDto.getPostalCode(), premisesDto.getUnitNo(), premisesDto.getFloorNo(),
                                premisesDto.getBlkNo(), premisesDto.getVehicleNo());
                    } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType)) {
                        premisesStr = splicingAddressInformation(postalCode, unitNo, floorNo, blkNo, "");
                        premisesStr2 = splicingAddressInformation(premisesDto.getPostalCode(), premisesDto.getUnitNo(), premisesDto.getFloorNo(),
                                premisesDto.getBlkNo(), "");
                    }
                    if(premisesStr != null && premisesStr2 != null) {
                        if (premisesStr.equals(premisesStr2)) {
                            hicCode = premisesDto.getHciCode();
                            return hicCode;
                        }
                    }
                }
            }
        }
        return hicCode;
    }

    private String splicingAddressInformation(String postalCode, String unitNo, String floorNo, String blkNo, String last) {
        StringBuilder stringBuilder = new StringBuilder();
        if(postalCode == null){
            postalCode = "";
        }
        if(unitNo == null){
            unitNo = "";
        }
        if(floorNo == null){
            floorNo = "";
        }
        if(blkNo == null){
            blkNo = "";
        }
        if(last == null){
            last = "";
        }
        stringBuilder.append(postalCode);
        stringBuilder.append(unitNo);
        stringBuilder.append(floorNo);
        stringBuilder.append(blkNo);
        stringBuilder.append(last);
        String premisesStr = stringBuilder.toString();
        return premisesStr;
    }

    @Override
    public InspectionPreTaskDto getPreInspRbOption(String applicationNo, InspectionPreTaskDto inspectionPreTaskDto) {
        List<SelectOption> preInspRbOption = IaisCommonUtils.genNewArrayList();
        Map<String, String> userIdMap = IaisCommonUtils.genNewHashMap();
        //get history
        AppPremisesRoutingHistoryDto asoHistory = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorysByAppNoAndStageId(applicationNo, HcsaConsts.ROUTING_STAGE_ASO).getEntity();
        AppPremisesRoutingHistoryDto psoHistory = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorysByAppNoAndStageId(applicationNo, HcsaConsts.ROUTING_STAGE_PSO).getEntity();
        if(psoHistory != null){
            String asoUserId = asoHistory.getActionby();
            String psoUserId = psoHistory.getActionby();
            OrgUserDto aso = organizationClient.retrieveOrgUserAccountById(asoUserId).getEntity();
            OrgUserDto pso = organizationClient.retrieveOrgUserAccountById(psoUserId).getEntity();
            SelectOption asoSo = new SelectOption(RoleConsts.USER_ROLE_ASO, aso.getDisplayName() + " (" + RoleConsts.USER_ROLE_ASO + ")");
            SelectOption psoSo = new SelectOption(RoleConsts.USER_ROLE_PSO, pso.getDisplayName() + " (" + RoleConsts.USER_ROLE_PSO + ")");
            preInspRbOption.add(asoSo);
            preInspRbOption.add(psoSo);
            userIdMap.put(RoleConsts.USER_ROLE_ASO, asoUserId);
            userIdMap.put(RoleConsts.USER_ROLE_PSO, psoUserId);
        } else {
            String asoUserId = asoHistory.getActionby();
            OrgUserDto aso = organizationClient.retrieveOrgUserAccountById(asoUserId).getEntity();
            SelectOption asoSo = new SelectOption(RoleConsts.USER_ROLE_ASO, aso.getDisplayName() + " (" + RoleConsts.USER_ROLE_ASO + ")");
            preInspRbOption.add(asoSo);
            userIdMap.put(RoleConsts.USER_ROLE_ASO, asoUserId);
        }
        inspectionPreTaskDto.setPreInspRbOption(preInspRbOption);
        inspectionPreTaskDto.setStageUserIdMap(userIdMap);
        return inspectionPreTaskDto;
    }

    @Override
    public ApplicationViewDto setApplicationRfiInfo(ApplicationViewDto applicationViewDto) {
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String appType = applicationDto.getApplicationType();
        List<AppEditSelectDto> appEditSelectDtos = IaisCommonUtils.genNewArrayList();
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            appEditSelectDto.setEditType(ApplicationConsts.APPLICATION_EDIT_TYPE_RFI);
            appEditSelectDto.setServiceEdit(true);
            appEditSelectDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appEditSelectDto.setPoEdit(true);
            appEditSelectDto.setDocEdit(true);
            appEditSelectDto.setMedAlertEdit(true);
            appEditSelectDto.setPremisesListEdit(true);
            appEditSelectDto.setApplicationId(applicationDto.getId());
            applicationViewDto.setAppEditSelectDto(appEditSelectDto);
        } else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            appEditSelectDto.setEditType(ApplicationConsts.APPLICATION_EDIT_TYPE_RFI);
            appEditSelectDto.setServiceEdit(true);
            appEditSelectDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appEditSelectDto.setPoEdit(true);
            appEditSelectDto.setDocEdit(true);
            appEditSelectDto.setMedAlertEdit(true);
            appEditSelectDto.setPremisesListEdit(true);
            appEditSelectDto.setApplicationId(applicationDto.getId());
            applicationViewDto.setAppEditSelectDto(appEditSelectDto);
        } else if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
            appEditSelectDtos = applicationService.getAppEditSelectDtos(applicationDto.getId(), ApplicationConsts.APPLICATION_EDIT_TYPE_RFC);
        }
        if (!IaisCommonUtils.isEmpty(appEditSelectDtos)) {
            applicationViewDto.setAppEditSelectDto(appEditSelectDtos.get(0));
        }
        return applicationViewDto;
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
        } else {
            hcsaRiskInspectionComplianceDto.setSvcCode(serviceCode);
            hcsaRiskInspectionComplianceDto.setNcCountThCritical(0);
            hcsaRiskInspectionComplianceDto.setNcCountThMajor(0);
            hcsaRiskInspectionComplianceDto.setNcCountThMinor(0);
            hcsaRiskInspectionComplianceDto = hcsaConfigClient.getHcsaRiskInspectionComplianceDto(hcsaRiskInspectionComplianceDto).getEntity();
        }
        return hcsaRiskInspectionComplianceDto;
    }

    private InspectionHistoryShowDto getInspectorAndLeadByAppNo(String appNo, String taskRefNo, InspectionHistoryShowDto inspectionHistoryShowDto) {
        List<TaskDto> taskDtos = organizationClient.getTaskByAppNoStatus(appNo, TaskConsts.TASK_STATUS_COMPLETED, TaskConsts.TASK_PROCESS_URL_INSPECTION_CHECKLIST_VERIFY).getEntity();
        if(!IaisCommonUtils.isEmpty(taskDtos)){
            List<String> inspectorNames = IaisCommonUtils.genNewArrayList();
            List<String> inspectorIds = IaisCommonUtils.genNewArrayList();
            //get inspectorId
            for(TaskDto taskDto : taskDtos){
                String userId = taskDto.getUserId();
                inspectorIds.add(userId);
            }
            //filer id
            if(!IaisCommonUtils.isEmpty(inspectorIds)){
                Set<String> inspectorIdSet = new HashSet<>(inspectorIds);
                inspectorIds = new ArrayList<>(inspectorIdSet);
                for(String inspectorId : inspectorIds){
                    OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(inspectorId).getEntity();
                    String name = orgUserDto.getDisplayName();
                    inspectorNames.add(name);
                }
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
        List<TaskDto> taskDtos = organizationClient.getTaskByAppNoStatus(taskDto.getApplicationNo(), TaskConsts.TASK_STATUS_COMPLETED, TaskConsts.TASK_PROCESS_URL_MAIN_FLOW).getEntity();
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
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos start ...."));
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos stageId -->:"+stageId));
        for(ApplicationDto applicationDto : applicationDtos){
            AppGrpPremisesEntityDto appGrpPremisesEntityDto = applicationClient.getPremisesByAppNo(applicationDto.getApplicationNo()).getEntity();
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            if(appGrpPremisesEntityDto != null){
                hcsaSvcStageWorkingGroupDto.setPremiseType(appGrpPremisesEntityDto.getPremisesType());
            }else{
                log.error(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos this APP do not have the premise :"+applicationDto.getApplicationNo()));
            }
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos end ...."));
        return hcsaSvcStageWorkingGroupDtos;
    }
}
