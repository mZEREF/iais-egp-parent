package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InspecEmailDelegator
 *
 * @author junyu
 * @date 2019/11/22
 */
@Delegator("inspEmailDelegator")
@Slf4j
public class InspecEmailDelegator {
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    InspectionService inspectionService;
    @Autowired
    TaskService taskService;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    ApplicationViewService applicationViewService;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    InsepctionNcCheckListService insepctionNcCheckListService;
    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;
    @Autowired
    AppInspectionStatusClient appInspectionStatusClient;
    @Autowired
    private OrganizationClient organizationClient;
    private final String TASK_DTO="taskDto";
    private final String MSG_CON="messageContent";
    private final String TD="</td><td>";
    private final String INS_EMAIL_DTO="insEmailDto";

    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");

        AccessUtil.initLoginUserInfo(bpc.request);

    }

    public void prepareData(BaseProcessClass bpc) throws IOException, TemplateException {
        log.info("=======>>>>>prepareData>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String templateId="08BDA324-5D13-EA11-BE78-000C29D29DB0";
        String taskId = ParamUtil.getRequestString(request,"taskId");
        if(StringUtil.isEmpty(taskId)){
            taskId = "F9359FD4-5934-EA11-BE7D-000C29F371DC";
        }
        TaskDto taskDto = taskService.getTaskById(taskId);
        if(StringUtil.isEmpty(taskDto)){
            taskDto= (TaskDto) ParamUtil.getSessionAttr(request,TASK_DTO);
        }
        String correlationId = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = inspEmailService.getAppViewByCorrelationId(correlationId);
        String appNo=applicationViewDto.getApplicationDto().getApplicationNo();
        String licenseeId=inspEmailService.getAppInsRepDto(correlationId).getLicenseeId();
        String licenseeName=inspEmailService.getLicenseeDtoById(licenseeId).getName();
        String appPremCorrId=correlationId;
        InspectionEmailTemplateDto inspectionEmailTemplateDto = inspEmailService.loadingEmailTemplate(templateId);
        inspectionEmailTemplateDto.setAppPremCorrId(appPremCorrId);
        inspectionEmailTemplateDto.setApplicantName(licenseeName);
        inspectionEmailTemplateDto.setApplicationNumber(appNo);
        inspectionEmailTemplateDto.setHciCode(applicationViewDto.getHciCode());
        inspectionEmailTemplateDto.setHciNameOrAddress(applicationViewDto.getHciAddress());
        HcsaServiceDto hcsaServiceDto=inspectionService.getHcsaServiceDtoByServiceId(applicationViewDto.getApplicationDto().getServiceId());
        inspectionEmailTemplateDto.setServiceName(hcsaServiceDto.getSvcName());
        List<NcAnswerDto> ncAnswerDtos =insepctionNcCheckListService.getNcAnswerDtoList(appPremCorrId);
        AppPremisesRecommendationDto appPreRecommentdationDto =insepctionNcCheckListService.getAppRecomDtoByAppCorrId(appPremCorrId,"RETYPE001");
        inspectionEmailTemplateDto.setBestPractices(appPreRecommentdationDto.getBestPractice());
        Map<String,Object> map=new HashMap<>();
        map.put("APPLICANT_NAME",StringUtil.viewHtml(inspectionEmailTemplateDto.getApplicantName()));
        map.put("APPLICATION_NUMBER",StringUtil.viewHtml(inspectionEmailTemplateDto.getApplicationNumber()));
        map.put("HCI_CODE",StringUtil.viewHtml(inspectionEmailTemplateDto.getHciCode()));
        map.put("HCI_NAME",StringUtil.viewHtml(inspectionEmailTemplateDto.getHciNameOrAddress()));
        map.put("SERVICE_NAME",StringUtil.viewHtml(inspectionEmailTemplateDto.getServiceName()));
        if(!ncAnswerDtos.isEmpty()){
            StringBuilder stringBuilder=new StringBuilder();int i=0;
            for (NcAnswerDto ncAnswerDto:ncAnswerDtos
                 ) {
                stringBuilder.append("<tr><td>"+ ++i);
                stringBuilder.append(TD+StringUtil.viewHtml(ncAnswerDto.getItemQuestion()));
                stringBuilder.append(TD+StringUtil.viewHtml(ncAnswerDto.getClause()));
                stringBuilder.append(TD+StringUtil.viewHtml(ncAnswerDto.getRemark()));
                stringBuilder.append("</td></tr>");
            }
            map.put("NC_DETAILS",StringUtil.viewHtml(stringBuilder.toString()));
        }
        if(inspectionEmailTemplateDto.getBestPractices()!=null){
            map.put("BEST_PRACTICE",StringUtil.viewHtml(inspectionEmailTemplateDto.getBestPractices()));
        }
        map.put("MOH_NAME", AppConsts.MOH_AGENCY_NAME);
        String mesContext= MsgUtil.getTemplateMessageByContent(inspectionEmailTemplateDto.getMessageContent(),map);
        String content=ParamUtil.getString(request,MSG_CON);
        if(content!=null){
            mesContext=content;
        }
        inspectionEmailTemplateDto.setMessageContent(mesContext);
        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{InspectionConstants.PROCESS_DECI_ROTE_EMAIL_AO1_REVIEW,InspectionConstants.PROCESS_DECI_SENDS_EMAIL_APPLICANT});

        ParamUtil.setSessionAttr(bpc.request, TASK_DTO, taskDto);
        ParamUtil.setSessionAttr(request,"appPremCorrId",appPremCorrId);
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setSessionAttr(request,"mesContext", mesContext);
        ParamUtil.setSessionAttr(request,"applicationViewDto",applicationViewDto);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);
    }

    public void emailSubmitStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        InspectionEmailTemplateDto inspectionEmailTemplateDto = (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(bpc.request,INS_EMAIL_DTO);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);
        String crudAction = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.debug("*******************crudAction-->:" + crudAction);
    }

    public void previewEmail(BaseProcessClass bpc){
        log.info("=======>>>>>previewEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"preview".equals(currentAction)){
            return;
        }
        String content=ParamUtil.getString(request,MSG_CON);
        String subject=ParamUtil.getString(request,"subject");
        ParamUtil.setRequestAttr(request,"subject", subject);
        ParamUtil.setRequestAttr(request,"content", content);


    }

    public void sendEmail(BaseProcessClass bpc) throws FeignException {

        log.info("=======>>>>>sendEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        ApplicationViewDto applicationViewDto= (ApplicationViewDto) ParamUtil.getSessionAttr(request,"applicationViewDto");
        String serviceId=applicationViewDto.getApplicationDto().getServiceId();
        TaskDto taskDto= (TaskDto) ParamUtil.getSessionAttr(request,TASK_DTO);
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"send".equals(currentAction)){
            return;
        }
        String decision=ParamUtil.getString(request,"decision");
        if(decision.equals("Please select")){decision=InspectionConstants.PROCESS_DECI_SENDS_EMAIL_APPLICANT;}

        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
        inspectionEmailTemplateDto.setSubject(ParamUtil.getString(request,"subject"));
        inspectionEmailTemplateDto.setMessageContent(ParamUtil.getString(request,MSG_CON));
        if (inspectionEmailTemplateDto.getSubject().isEmpty()){
            Map<String,String> errorMap = new HashMap<>();
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errorMap);
            ParamUtil.setRequestAttr(request,DemoConstants.ISVALID, SystemParameterConstants.STATUS_ACTIVE);
        }
        if (inspectionEmailTemplateDto.getMessageContent().isEmpty()){
            Map<String,String> errorMap = new HashMap<>();
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errorMap);
            ParamUtil.setRequestAttr(request,DemoConstants.ISVALID,SystemParameterConstants.STATUS_DEACTIVATED);
        }
        if (decision.equals(InspectionConstants.PROCESS_DECI_ROTE_EMAIL_AO1_REVIEW)){
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
            AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
            appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_AO1_EMAIL_VERIFY);
            appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusClient.update(appInspectionStatusDto);

            taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01,InspectionConstants.PROCESS_DECI_ROTE_EMAIL_AO1_REVIEW, taskDto,HcsaConsts.ROUTING_STAGE_POT,userId);
            completedTask(taskDto);

            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
            hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
            hcsaSvcStageWorkingGroupDto.setOrder(2);
            TaskDto taskDto1=taskDto;
            taskDto1.setTaskKey(HcsaConsts.ROUTING_STAGE_AO1);
            taskDto1.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_AO1_VALIDATE_NCEMAIL);
            taskDto1.setRoleId(RoleConsts.USER_ROLE_AO1);
            taskDto1.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());
            taskDto1.setUserId(taskService.getUserIdForWorkGroup(taskDto1.getWkGrpId()).getUserId());
            List<TaskDto> taskDtos = prepareTaskList(taskDto1,hcsaSvcStageWorkingGroupDto);
            taskService.createTasks(taskDtos);
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01, InspectionConstants.PROCESS_DECI_ROTE_EMAIL_AO1_REVIEW,taskDto1,HcsaConsts.ROUTING_STAGE_POT,userId);
        }
        else {
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
            AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
            appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_REVIEW_CHECKLIST_EMAIL);
            appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusClient.update(appInspectionStatusDto);
            taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS,InspectionConstants.PROCESS_DECI_SENDS_EMAIL_APPLICANT, taskDto,HcsaConsts.ROUTING_STAGE_POT,userId);
            completedTask(taskDto);

            boolean flag=true;
            List<ApplicationDto> applicationDtos= applicationService.getApplicaitonsByAppGroupId(applicationViewDto.getApplicationDto().getAppGrpId());
            for (ApplicationDto appDto:applicationDtos
                 ) {
                if(!appDto.getStatus().equals(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS)){
                    flag=false;
                }
            }
            if(flag){

                HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
                hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
                hcsaSvcStageWorkingGroupDto.setOrder(3);
                TaskDto taskDto1=taskDto;
                List<TaskDto> taskDtos = prepareTaskList(taskDto1,hcsaSvcStageWorkingGroupDto);
                taskDto1.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
                taskDto1.setRoleId(RoleConsts.USER_ROLE_INSPECTION_LEAD);
                taskDto1.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_MERGE_NCEMAIL);
                taskDto1.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());
                taskDto1.setUserId(organizationClient.getInspectionLead(taskDto1.getWkGrpId()).getEntity().get(0));
                taskService.createTasks(taskDtos);
                createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS, InspectionConstants.PROCESS_DECI_SENDS_EMAIL_APPLICANT,taskDto1,HcsaConsts.ROUTING_STAGE_POT,userId);
            }

        }
        inspEmailService.insertEmailTemplate(inspectionEmailTemplateDto);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);

    }
    public void doRecallEmail(BaseProcessClass bpc) {
        log.info("=======>>>>>doRecallEmail>>>>>>>>>>>>>>>>emailRequest");
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,String decision,
                                                                         TaskDto taskDto,String subStage,String userId ) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(taskDto.getTaskKey());
        appPremisesRoutingHistoryDto.setProcessDecision(decision);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(userId);

        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setRoleId(taskDto.getRoleId());
        appPremisesRoutingHistoryDto.setWrkGrpId(taskDto.getWkGrpId());
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        return appPremisesRoutingHistoryDto;
    }
    private List<TaskDto> prepareTaskList(TaskDto taskDto, HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto)  {
        List<TaskDto> list = new ArrayList<>();
        List<HcsaSvcStageWorkingGroupDto> listhcsaSvcStageWorkingGroupDto = hcsaConfigClient.getSvcWorkGroup(hcsaSvcStageWorkingGroupDto).getEntity();
        String schemeType = listhcsaSvcStageWorkingGroupDto.get(0).getSchemeType();
        Integer count = listhcsaSvcStageWorkingGroupDto.get(0).getCount();

        taskDto.setId(null);
        taskDto.setDateAssigned(new Date());
        taskDto.setSlaDateCompleted(null);
        taskDto.setSlaRemainInDays(null);
        taskDto.setScore(count);

        taskDto.setTaskType(schemeType);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        list.add(taskDto);
        return list;
    }
    private TaskDto completedTask(TaskDto taskDto) {
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(remainDays(taskDto));
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskService.updateTask(taskDto);
    }
    private int remainDays(TaskDto taskDto) {
        int result = 0;
        String resultStr = DurationFormatUtils.formatPeriod(taskDto.getDateAssigned().getTime(), taskDto.getSlaDateCompleted().getTime(), "d");
        log.debug(StringUtil.changeForLog("The resultStr is -->:") + resultStr);
        return result;
    }

}
