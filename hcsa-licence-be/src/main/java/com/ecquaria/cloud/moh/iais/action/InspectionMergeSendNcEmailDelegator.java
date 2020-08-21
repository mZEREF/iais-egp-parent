package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocNcCheckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionPreTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * InspectionMergeSendNcEmailDelegator
 *
 * @author junyu
 * @date 2019/12/17
 */
@Delegator("inspectionMergeSendNcEmailDelegator")
@Slf4j
public class InspectionMergeSendNcEmailDelegator {
    @Value("${iais.email.sender}")
    private String mailSender;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    InsepctionNcCheckListService insepctionNcCheckListService;
    @Autowired
    InspectionService inspectionService;
    @Autowired
    private TaskService taskService;
    @Autowired
    InspectionPreTaskService inspectionPreTaskService;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    ApplicationViewService applicationViewService;
    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    EmailClient emailClient;
    @Autowired
    OrganizationClient organizationClient;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    FillupChklistService fillupChklistService;
    @Autowired
    AppInspectionStatusClient appInspectionStatusClient;
    @Autowired
    NotificationHelper notificationHelper;
    @Autowired
    InspectionTaskClient inspectionTaskClient;
    @Autowired
    private AppointmentClient appointmentClient;
    private static final String INS_EMAIL_DTO="insEmailDto";
    private static final String TASK_DTO="taskDto";
    private static final String SUBJECT="subject";
    private static final String MSG_CON="messageContent";
    private static final String APP_VIEW_DTO="applicationViewDto";
    private static final String TD="</td><td>";

    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request=bpc.request;
        String taskId = ParamUtil.getMaskedString(request,"taskId");
        AuditTrailHelper.auditFunction("Merge NcEmail Management", "Post Inspection Task");
        TaskDto  taskDto = fillupChklistService.getTaskDtoById(taskId);
        ParamUtil.setSessionAttr(bpc.request, TASK_DTO, taskDto);
        ParamUtil.setSessionAttr(request,"appPremCorrIds",null);
        ParamUtil.setSessionAttr(request,MSG_CON, null);
        ParamUtil.setSessionAttr(request,APP_VIEW_DTO,null);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, null);

    }

    public void prepareData(BaseProcessClass bpc) throws IOException, TemplateException {
        log.info("=======>>>>>prepareData>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        TaskDto taskDto= (TaskDto) ParamUtil.getSessionAttr(request,TASK_DTO);
        String correlationId = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = fillupChklistService.getAppViewDto(taskDto.getId());
        applicationViewDto.setCurrentStatus(MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationDto().getStatus()}).get(0).getText());

        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos=inspEmailService.getAppPremisesCorrelationsByPremises(correlationId);
        if(applicationViewDto.getApplicationDto().isFastTracking()){
            appPremisesCorrelationDtos.clear();
            AppPremisesCorrelationDto appCorrDto =new AppPremisesCorrelationDto();
            appCorrDto.setId(correlationId);
            appPremisesCorrelationDtos.add(appCorrDto);
        }
        Iterator<AppPremisesCorrelationDto> iterator=appPremisesCorrelationDtos.iterator();
        while (iterator.hasNext()){
            AppPremisesCorrelationDto appPremisesCorrelationDto=iterator.next();
            try {
                inspEmailService.getInsertEmail(appPremisesCorrelationDto.getId());
            }catch (Exception e){
                iterator.remove();
            }

        }

        List<String> appPremCorrIds= IaisCommonUtils.genNewArrayList();
        List<String> svcNames=IaisCommonUtils.genNewArrayList();



        InspectionEmailTemplateDto inspectionEmailTemplateDto= new InspectionEmailTemplateDto();
        String mesContext;
        {
            List<String> leads = organizationClient.getInspectionLead(taskDto.getWkGrpId()).getEntity();
            OrgUserDto leadDto=organizationClient.retrieveOrgUserAccountById(leads.get(0)).getEntity();
            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_INBOX;
            MsgTemplateDto msgTemplateDto= notificationHelper.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_INS_002_INSPECTOR_EMAIL);
            Map<String,Object> mapTemplate=IaisCommonUtils.genNewHashMap();
            LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(applicationViewDto.getApplicationGroupDto().getLicenseeId());
            mapTemplate.put("inspection_lead", leadDto.getDisplayName());
            mapTemplate.put("ApplicantName", licenseeDto.getName());
            mapTemplate.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationDto().getApplicationType()}).get(0).getText());
            mapTemplate.put("ApplicationNumber", applicationViewDto.getApplicationDto().getApplicationNo());
            mapTemplate.put("ApplicationDate", applicationViewDto.getSubmissionDate());
            mapTemplate.put("systemLink", loginUrl);
            mapTemplate.put("HCI_CODE", applicationViewDto.getHciCode());
            mapTemplate.put("Address", applicationViewDto.getHciAddress());
            mapTemplate.put("HCI_Postal_Code", applicationViewDto.getHciPostalCode());
            mapTemplate.put("LicenseeName", licenseeDto.getName());
            AppPremisesRecommendationDto appPreRecommentdationDtoInspectionDate =insepctionNcCheckListService.getAppRecomDtoByAppCorrId(correlationId,InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
            mapTemplate.put("InspectionDate", Formatter.formatDate(appPreRecommentdationDtoInspectionDate.getRecomInDate()));
//cancel old calendar
            AppPremisesInspecApptDto appPremisesInspecApptDto=inspectionTaskClient.getSpecificDtoByAppPremCorrId(correlationId).getEntity();
            ApptUserCalendarDto cancelCalendarDto = new ApptUserCalendarDto();
            AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
            if(appPremisesInspecApptDto!=null&&appPremisesInspecApptDto.getApptRefNo()!=null){
                cancelCalendarDto.setApptRefNo(appPremisesInspecApptDto.getApptRefNo());
                cancelCalendarDto.setAuditTrailDto(auditTrailDto);
                cancelCalendarDto.setStatus(AppointmentConstants.CALENDAR_STATUS_RESERVED);
                List<ApptUserCalendarDto> apptUserCalendarDtos= appointmentClient.getCalenderByApptRefNoAndStatus(cancelCalendarDto).getEntity();
                mapTemplate.put("InspectionEndDate", Formatter.formatDate(apptUserCalendarDtos.get(0).getEndSlot().get(0)));
            }
            Map<String,Object> mapTableTemplate=IaisCommonUtils.genNewHashMap();
            MsgTemplateDto msgTableTemplateDto= notificationHelper.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_INS_002_TABLE_12);
            int sn=1;
            for (AppPremisesCorrelationDto appPremisesCorrelationDto:appPremisesCorrelationDtos
            ) {
                try{
                    appPremCorrIds.add(appPremisesCorrelationDto.getId());
                    ApplicationViewDto appViewDto = inspEmailService.getAppViewByCorrelationId(appPremisesCorrelationDto.getId());
                    svcNames.add(inspectionService.getHcsaServiceDtoByServiceId(appViewDto.getApplicationDto().getServiceId()).getSvcName());

                    AppPremisesRecommendationDto appPreRecommentdationDto =insepctionNcCheckListService.getAppRecomDtoByAppCorrId(appPremisesCorrelationDto.getId(),InspectionConstants.RECOM_TYPE_TCU);
                    List<NcAnswerDto> ncAnswerDtos=insepctionNcCheckListService.getNcAnswerDtoList(appPremisesCorrelationDto.getId());

                    if(ncAnswerDtos.size()!=0){
                        StringBuilder stringBuilder=new StringBuilder();
                        stringBuilder.append("<tr>").append(appViewDto.getServiceType()).append("</tr>");
                        int i=0;
                        for (NcAnswerDto ncAnswerDto:ncAnswerDtos
                        ) {
                            stringBuilder.append("<tr><td>").append(++i);
                            stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getType()));
                            stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getItemQuestion()));
                            stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getClause()));
                            stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getRemark()));
                            stringBuilder.append(TD).append(StringUtil.viewHtml("1".equals(ncAnswerDto.getRef())?"Yes":"No"));
                            stringBuilder.append("</td></tr>");
                        }
                        mapTableTemplate.put("NC_DETAILS",StringUtil.viewHtml(stringBuilder.toString()));
                    }
                    //mapTemplate.put("ServiceName", applicationViewDto.getServiceType());
                    if(appPreRecommentdationDto!=null&&(appPreRecommentdationDto.getBestPractice()!=null||appPreRecommentdationDto.getRemarks()!=null)){
                        String stringBuilder = "<tr><td>" + sn +
                                TD + StringUtil.viewHtml(appPreRecommentdationDto.getBestPractice()) +
                                TD + StringUtil.viewHtml(appPreRecommentdationDto.getRemarks()) +
                                "</td></tr>";
                        mapTableTemplate.put("Observation_Recommendation",StringUtil.viewHtml(stringBuilder));
                        sn++;
                    }
                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }
            }

            msgTableTemplateDto.setMessageContent(MsgUtil.getTemplateMessageByContent(msgTableTemplateDto.getMessageContent(),mapTableTemplate));

            mapTemplate.put("NC_DETAILS_AND_Observation_Recommendation",msgTableTemplateDto.getMessageContent());
            mapTemplate.put("HALP", AppConsts.MOH_SYSTEM_NAME);
            mapTemplate.put("DDMMYYYY", StringUtil.viewHtml(Formatter.formatDate(new Date())));
            mapTemplate.put("Inspector_mail_Address", leadDto.getEmail());
            mapTemplate.put("InspectorDID", leadDto.getMobileNo());
            mapTemplate.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
            msgTemplateDto.setMessageContent(MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(),mapTemplate));

            int index = 1;
            String replaceStr = "num_rep";
            while(msgTemplateDto.getMessageContent().contains(replaceStr)){
                msgTemplateDto.setMessageContent(msgTemplateDto.getMessageContent().replaceFirst(replaceStr,  index + "."));
                index++;
            }
            mesContext= msgTemplateDto.getMessageContent();
            inspectionEmailTemplateDto.setSubject(MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),mapTemplate));
        }
        inspectionEmailTemplateDto.setAppPremCorrId(applicationViewDto.getAppPremisesCorrelationId());
        inspectionEmailTemplateDto.setMessageContent(mesContext);
        inspectionEmailTemplateDto.setSubject("Inspection - Summary of Inspection Outcome");

        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT,InspectionConstants.PROCESS_DECI_SENDS_EMAIL_APPLICANT});

        ParamUtil.setSessionAttr(bpc.request, TASK_DTO, taskDto);
        ParamUtil.setSessionAttr(request,"appPremCorrIds", (Serializable) appPremCorrIds);
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setSessionAttr(request,MSG_CON, mesContext);
        ParamUtil.setRequestAttr(request,"svcNames",svcNames);
        ParamUtil.setSessionAttr(request,APP_VIEW_DTO,applicationViewDto);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);

        ParamUtil.setSessionAttr(request,"serListDto",fillupChklistService.getInspectionFDtosDtoOnlyForChecklistLetter(taskDto.getRefNo()));
    }

    public void emailSubmitStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        InspectionEmailTemplateDto inspectionEmailTemplateDto = (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(bpc.request,INS_EMAIL_DTO);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);
        String crudAction = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.info(StringUtil.changeForLog("*******************crudAction-->:" + crudAction));
    }

    public void previewEmail(BaseProcessClass bpc){
        log.info("=======>>>>>previewEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"preview".equals(currentAction)){
            return;
        }
        String content=ParamUtil.getString(request,MSG_CON);
        String subject=ParamUtil.getString(request,SUBJECT);
        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
        inspectionEmailTemplateDto.setMessageContent(content);
        inspectionEmailTemplateDto.setSubject(subject);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO,inspectionEmailTemplateDto);
        ParamUtil.setRequestAttr(request,SUBJECT, subject);
        ParamUtil.setSessionAttr(request,MSG_CON, content);
    }

    public void sendEmail(BaseProcessClass bpc)  {

        log.info("=======>>>>>sendEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        ApplicationViewDto applicationViewDto= (ApplicationViewDto) ParamUtil.getSessionAttr(request,APP_VIEW_DTO);
        TaskDto taskDto= (TaskDto) ParamUtil.getSessionAttr(request,TASK_DTO);

        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
        inspectionEmailTemplateDto.setSubject(ParamUtil.getString(request,SUBJECT));
        inspectionEmailTemplateDto.setMessageContent(ParamUtil.getString(request,MSG_CON));
        inspectionEmailTemplateDto.setRemarks(ParamUtil.getString(request, IntranetUserConstant.INTRANET_REMARKS));
        String decision=ParamUtil.getString(request,"decision");
        if("Select".equals(decision)){decision=InspectionConstants.PROCESS_DECI_SENDS_EMAIL_APPLICANT;}
        List<String>appPremCorrIds= (List<String>) ParamUtil.getSessionAttr(request,"appPremCorrIds");

        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos=inspEmailService.getAppPremisesCorrelationsByPremises(applicationViewDto.getAppPremisesCorrelationId());


        if (inspectionEmailTemplateDto.getSubject().isEmpty()){
            Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errorMap);
        }
        if (inspectionEmailTemplateDto.getMessageContent().isEmpty()){
            Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errorMap);
        }
        if (decision.equals(InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT)){
            taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTION_LEAD);
            completedTask(taskDto,appPremCorrIds);
            for(int i=1;i<=appPremCorrIds.size();i++){
                String param="revise"+i;
                if(ParamUtil.getString(request, param)==null){
                    AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorrIds.get(i-1)).getEntity();
                    appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_JOB_CREATE_TASK_TO_LEADER);
                    appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    appInspectionStatusClient.update(appInspectionStatusDto);
                    appPremCorrIds.set(i-1,"");

                }
            }

            createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING,InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT,taskDto, userId,inspectionEmailTemplateDto.getRemarks(),HcsaConsts.ROUTING_STAGE_INP);

            for(int i=0;i<appPremCorrIds.size();i++){
                if(appPremCorrIds.get(i).equals(appPremisesCorrelationDtos.get(i).getId())){
                    ApplicationViewDto applicationViewDto1=applicationViewService.searchByCorrelationIdo(appPremCorrIds.get(i));
                    List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryDtosByAppNo(applicationViewDto1.getApplicationDto().getApplicationNo());
                    AppPremisesRoutingHistoryDto appPremisesRoutingHisDto= appPremisesRoutingHistoryDtos.get(0);
                    String upDt=appPremisesRoutingHistoryDtos.get(0).getUpdatedDt();
                    for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto1:appPremisesRoutingHistoryDtos){
                        if(appPremisesRoutingHistoryDto1.getUpdatedDt()!=null&&appPremisesRoutingHistoryDto1.getRoleId()!=null) {
                            if(appPremisesRoutingHistoryDto1.getUpdatedDt().compareTo(upDt)>=0&&appPremisesRoutingHistoryDto1.getRoleId().equals(RoleConsts.USER_ROLE_INSPECTIOR)){
                                appPremisesRoutingHisDto=appPremisesRoutingHistoryDto1;
                            }
                            upDt=appPremisesRoutingHistoryDto1.getUpdatedDt();
                        }
                    }

                    applicationViewDto1.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER);
                    applicationViewService.updateApplicaiton(applicationViewDto1.getApplicationDto());
                    applicationService.updateFEApplicaitons(Collections.singletonList(applicationViewDto1.getApplicationDto()));
                    AppInspectionStatusDto appInspectionStatusDto1 = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorrIds.get(i)).getEntity();
                    appInspectionStatusDto1.setStatus(InspectionConstants.INSPECTION_STATUS_INSPECTOR_LEAD_ROUTE_BACK_EMAIL);
                    appInspectionStatusDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    appInspectionStatusClient.update(appInspectionStatusDto1);

                    String serviceId=applicationViewDto1.getApplicationDto().getServiceId();
                    HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                    hcsaSvcStageWorkingGroupDto.setType(applicationViewDto1.getApplicationDto().getApplicationType());
                    hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
                    hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
                    hcsaSvcStageWorkingGroupDto.setOrder(1);
                    TaskDto taskDto2=new TaskDto();
                    taskDto2.setApplicationNo(applicationViewDto1.getApplicationDto().getApplicationNo());
                    taskDto2.setRefNo(appPremCorrIds.get(i));
                    taskDto2.setTaskType(taskDto.getTaskType());
                    taskDto2.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
                    taskDto2.setUserId(appPremisesRoutingHisDto.getActionby());
                    taskDto2.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REVISE_NCEMAIL);
                    taskDto2.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
                    taskDto2.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());

                    List<TaskDto> taskDtos = prepareTaskList(taskDto2,hcsaSvcStageWorkingGroupDto);
                    taskService.createTasks(taskDtos);
                    createAppPremisesRoutingHistory(applicationViewDto1.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING, ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER,taskDto2, userId,"",HcsaConsts.ROUTING_STAGE_INP);

                }
            }

        }
        else {
            taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTION_LEAD);
            completedTask(taskDto,appPremCorrIds);
            List<String>appPremCorrIdsIsNc=IaisCommonUtils.genNewArrayList();
            List<String>appPremCorrIdsNoNc=IaisCommonUtils.genNewArrayList();

            for (String appPremCorrId:appPremCorrIds
            ) {
                boolean isNoNc=true;
                try{
                    List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos = insepctionNcCheckListService.getNcItemDtoByAppCorrId(appPremCorrId);
                    if(appPremisesPreInspectionNcItemDtos.size()!=0){
                        for (AppPremisesPreInspectionNcItemDto nc:appPremisesPreInspectionNcItemDtos
                        ) {
                            if(nc.getIsRecitfied()==0){
                                isNoNc=false;
                            }
                        }
                    }
                    AdCheckListShowDto adCheckListShowDto = fillupChklistService.getAdhoc(appPremCorrId);
                    if(adCheckListShowDto!=null){
                        List<AdhocNcCheckItemDto> adItemList = adCheckListShowDto.getAdItemList();
                        if(adItemList!=null && !adItemList.isEmpty()){
                            for(AdhocNcCheckItemDto temp:adItemList){
                                if(temp.getRectified()){
                                    isNoNc=false;
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }

                if(isNoNc){
                    appPremCorrIdsNoNc.add(appPremCorrId);
                }
                else {
                    appPremCorrIdsIsNc.add(appPremCorrId);
                }
            }

            {
                applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT);
                applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
                applicationService.updateFEApplicaitons(Collections.singletonList(applicationViewDto.getApplicationDto()));
                AppInspectionStatusDto appInspectionStatusDto1 = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
                appInspectionStatusDto1.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_NC_RECTIFICATION_EMAIL);
                appInspectionStatusDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                appInspectionStatusClient.update(appInspectionStatusDto1);
                createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING,InspectionConstants.PROCESS_DECI_SENDS_EMAIL_APPLICANT, taskDto, userId,inspectionEmailTemplateDto.getRemarks(),HcsaConsts.ROUTING_STAGE_POT);


                for(int i=0;i<appPremCorrIdsNoNc.size();i++){
                    ApplicationViewDto applicationViewDto1=applicationViewService.searchByCorrelationIdo(appPremCorrIdsNoNc.get(i));
                    List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryDtosByAppNo(applicationViewDto1.getApplicationDto().getApplicationNo());
                    AppPremisesRoutingHistoryDto appPremisesRoutingHisDto= appPremisesRoutingHistoryDtos.get(0);
                    String upDt=appPremisesRoutingHistoryDtos.get(0).getUpdatedDt();
                    for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto:appPremisesRoutingHistoryDtos){
                        if(appPremisesRoutingHistoryDto.getUpdatedDt()!=null&&appPremisesRoutingHistoryDto.getRoleId()!=null){
                            if(appPremisesRoutingHistoryDto.getUpdatedDt().compareTo(upDt)>=0 &&appPremisesRoutingHistoryDto.getRoleId().equals(RoleConsts.USER_ROLE_INSPECTIOR)){
                                appPremisesRoutingHisDto=appPremisesRoutingHistoryDto;
                            }
                            upDt=appPremisesRoutingHistoryDto.getUpdatedDt();
                        }
                    }

                    applicationViewDto1.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT);
                    applicationViewService.updateApplicaiton(applicationViewDto1.getApplicationDto());
                    applicationService.updateFEApplicaitons(Collections.singletonList(applicationViewDto1.getApplicationDto()));
                    AppInspectionStatusDto appInspectionStatusDto2 = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorrIdsNoNc.get(i)).getEntity();
                    appInspectionStatusDto2.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
                    appInspectionStatusDto2.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    appInspectionStatusClient.update(appInspectionStatusDto2);

                    String serviceId=applicationViewDto1.getApplicationDto().getServiceId();
                    HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                    hcsaSvcStageWorkingGroupDto.setType(applicationViewDto1.getApplicationDto().getApplicationType());
                    hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
                    hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
                    hcsaSvcStageWorkingGroupDto.setOrder(1);
                    TaskDto taskDto2=new TaskDto();
                    taskDto2.setApplicationNo(applicationViewDto1.getApplicationDto().getApplicationNo());
                    taskDto2.setRefNo(appPremCorrIdsNoNc.get(i));
                    taskDto2.setTaskType(taskDto.getTaskType());
                    taskDto2.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
                    taskDto2.setUserId(appPremisesRoutingHisDto.getActionby());
                    taskDto2.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT);
                    taskDto2.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
                    taskDto2.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());

                    List<TaskDto> taskDtos = prepareTaskList(taskDto2,hcsaSvcStageWorkingGroupDto);
                    taskService.createTasks(taskDtos);
                    createAppPremisesRoutingHistory(applicationViewDto1.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT,taskDto2, userId,"",HcsaConsts.ROUTING_STAGE_POT);

                }
            }
            {
                for(int i=0;i<appPremCorrIdsIsNc.size();i++){
                    ApplicationViewDto applicationViewDto1=applicationViewService.searchByCorrelationIdo(appPremCorrIdsIsNc.get(i));
                    applicationViewDto1.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_CREATE_MESG);
                    applicationViewService.updateApplicaiton(applicationViewDto1.getApplicationDto());
                    applicationService.updateFEApplicaitons(Collections.singletonList(applicationViewDto1.getApplicationDto()));
                    AppInspectionStatusDto appInspectionStatusDto1 = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto1.getAppPremisesCorrelationId()).getEntity();
                    appInspectionStatusDto1.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_NC_RECTIFICATION_EMAIL);
                    appInspectionStatusDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    appInspectionStatusClient.update(appInspectionStatusDto1);
                    createAppPremisesRoutingHistory(applicationViewDto1.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_CREATE_MESG,ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_CREATE_MESG, taskDto, userId,"",HcsaConsts.ROUTING_STAGE_POT);
                }

            }
            try {
                String licenseeId=applicationViewDto.getApplicationGroupDto().getLicenseeId();
//                InterMessageDto interMessageDto = new InterMessageDto();
//                interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
//                interMessageDto.setSubject(inspectionEmailTemplateDto.getSubject());
//                interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
//                String mesNO = inboxMsgService.getMessageNo();
//                interMessageDto.setRefNo(mesNO);
//                HcsaServiceDto hcsaServiceDto= HcsaServiceCacheHelper.getServiceById(applicationViewDto.getApplicationDto().getServiceId());
//                interMessageDto.setService_id(hcsaServiceDto.getSvcCode()+'@');
//                interMessageDto.setMsgContent(inspectionEmailTemplateDto.getMessageContent());
//                interMessageDto.setStatus(MessageConstants.MESSAGE_STATUS_UNREAD);
//                interMessageDto.setUserId(licenseeId);
//                interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
//                inboxMsgService.saveInterMessage(interMessageDto);

//                EmailDto emailDto=new EmailDto();
//                emailDto.setContent(inspectionEmailTemplateDto.getMessageContent());
//                emailDto.setSubject(inspectionEmailTemplateDto.getSubject());
//                emailDto.setSender(mailSender);
//                emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
//                emailDto.setClientQueryCode(applicationViewDto.getApplicationDto().getAppGrpId());
                Map<String,Object> mapTemplate=IaisCommonUtils.genNewHashMap();
                mapTemplate.put("msgContent",inspectionEmailTemplateDto.getMessageContent());
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_INS_002_INSPECTOR_EMAIL);
                emailParam.setTemplateContent(mapTemplate);
                emailParam.setQueryCode(applicationViewDto.getApplicationDto().getApplicationNo());
                emailParam.setReqRefNum(applicationViewDto.getApplicationDto().getApplicationNo());
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                emailParam.setRefId(applicationViewDto.getApplicationDto().getApplicationNo());
                emailParam.setSubject(inspectionEmailTemplateDto.getSubject());
                notificationHelper.sendNotification(emailParam);
                //emailClient.sendNotification(emailDto).getEntity();
                //msg
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_INS_002_INSPECTOR_EMAIL_MSG);
                emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                notificationHelper.sendNotification(emailParam);
                //sms
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_INS_002_INSPECTOR_EMAIL_SMS);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                notificationHelper.sendNotification(emailParam);
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }

        }
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);

    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus, String decision,
                                                                         TaskDto taskDto, String userId, String remarks,String subStage) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
        appPremisesRoutingHistoryDto.setProcessDecision(decision);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(userId);
        appPremisesRoutingHistoryDto.setInternalRemarks(remarks);
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setRoleId(taskDto.getRoleId());
        appPremisesRoutingHistoryDto.setWrkGrpId(taskDto.getWkGrpId());
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        return appPremisesRoutingHistoryDto;
    }
    private List<TaskDto> prepareTaskList(TaskDto taskDto, HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto) {
        List<TaskDto> list = IaisCommonUtils.genNewArrayList();
        List<HcsaSvcStageWorkingGroupDto> listhcsaSvcStageWorkingGroupDto = hcsaConfigClient.getSvcWorkGroup(hcsaSvcStageWorkingGroupDto).getEntity();

        Integer count = listhcsaSvcStageWorkingGroupDto.get(0).getCount();
        taskDto.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());

        taskDto.setId(null);
        if (StringUtil.isEmpty(taskDto.getUserId())) {
            taskDto.setDateAssigned(null);
        } else {
            taskDto.setDateAssigned(new Date());
        }
        taskDto.setSlaDateCompleted(null);
        taskDto.setSlaRemainInDays(null);
        taskDto.setScore(count);
        taskDto.setSlaAlertInDays(2);
        taskDto.setPriority(0);
        taskDto.setSlaInDays(5);

        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        list.add(taskDto);
        return list;
    }
    private void completedTask(TaskDto taskDto, List<String> appPremCorrIds) {
        for (String refNo:appPremCorrIds
             ) {
            List<TaskDto> taskDtos=taskService.getTaskByUrlAndRefNo(refNo,TaskConsts.TASK_PROCESS_URL_INSPECTION_MERGE_NCEMAIL);
            for (TaskDto dto:taskDtos
            ) {
                dto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
                dto.setSlaDateCompleted(new Date());
                dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                taskService.updateTask(dto);
            }
        }
    }


    public void doRecallEmail() {
        log.info("=======>>>>>doRecallEmail>>>>>>>>>>>>>>>>emailRequest");
    }

}
