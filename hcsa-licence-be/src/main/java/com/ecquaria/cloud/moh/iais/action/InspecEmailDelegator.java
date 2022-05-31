package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstant;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
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
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.util.WorkDayCalculateUtil;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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
    FillupChklistService fillupChklistService;
    @Autowired
    OrganizationClient organizationClient;
    @Autowired
    NotificationHelper notificationHelper;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private AppointmentClient appointmentClient;
    @Autowired
    ApplicationClient applicationClient;
    @Autowired
    InspectionTaskClient inspectionTaskClient;
    @Value("${easmts.vehicle.sperate.flag}")
    private String vehicleOpenFlag;
    private static final String TASK_DTO="taskDto";
    private static final String MSG_CON="messageContent";
    private static final String TD="</td><td>";
    private static final String INS_EMAIL_DTO="insEmailDto";
    private static final String SUBJECT="subject";
    private static final String APP_VIEW_DTO="applicationViewDto";

    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request=bpc.request;
        String taskId = "";
        try{
            taskId = ParamUtil.getMaskedString(request,"taskId");
        }catch (MaskAttackException e){
            log.error(e.getMessage(),e);
            try{
                IaisEGPHelper.redirectUrl(bpc.response, "https://"+bpc.request.getServerName()+"/hcsa-licence-web/CsrfErrorPage.jsp");
            } catch (IOException ioe){
                log.error(ioe.getMessage(),ioe);
                return;
            }

        }
        TaskDto taskDto = taskService.getTaskById(taskId);
        if( taskDto == null) {
            return;
        }
        AuditTrailHelper.auditFunctionWithAppNo(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_INSPECTION_MAIL,taskDto.getApplicationNo());
        ParamUtil.setSessionAttr(bpc.request, TASK_DTO, taskDto);
        ParamUtil.setSessionAttr(request,"appPremCorrId",null);
        ParamUtil.setSessionAttr(request,MSG_CON, null);
        ParamUtil.setSessionAttr(request,APP_VIEW_DTO,null);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, null);
        SearchParam searchParamGroup = (SearchParam)ParamUtil.getSessionAttr(bpc.request, "backendinboxSearchParam");
        ParamUtil.setSessionAttr(bpc.request,"backSearchParamFromHcsaApplication",searchParamGroup);
    }

    public void prepareData(BaseProcessClass bpc) throws IOException, TemplateException {
        log.info("=======>>>>>prepareData>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        TaskDto taskDto= (TaskDto) ParamUtil.getSessionAttr(request,TASK_DTO);

        String correlationId = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = fillupChklistService.getAppViewDto(taskDto.getId());
        applicationViewDto.setCurrentStatus(MasterCodeUtil.getCodeDesc(applicationViewDto.getApplicationDto().getStatus()));
        String appNo=applicationViewDto.getApplicationDto().getApplicationNo();
        String licenseeId=applicationViewDto.getApplicationGroupDto().getLicenseeId();
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
        String applicantName=licenseeDto.getName();
        InspectionEmailTemplateDto inspectionEmailTemplateDto = new InspectionEmailTemplateDto();
        inspectionEmailTemplateDto.setAppPremCorrId(correlationId);
        inspectionEmailTemplateDto.setApplicantName(applicantName);
        inspectionEmailTemplateDto.setApplicationNumber(appNo);
        inspectionEmailTemplateDto.setHciCode(applicationViewDto.getHciCode());
        HcsaServiceDto hcsaServiceDto=inspectionService.getHcsaServiceDtoByServiceId(applicationViewDto.getApplicationDto().getServiceId());
        inspectionEmailTemplateDto.setServiceName(hcsaServiceDto.getSvcName());
        AppPremisesRecommendationDto appPreRecommentdationDto =insepctionNcCheckListService.getAppRecomDtoByAppCorrId(correlationId,InspectionConstants.RECOM_TYPE_TCU);
        List<NcAnswerDto> ncAnswerDtos=insepctionNcCheckListService.getNcAnswerDtoList(correlationId);

        String mesContext;
        {
            List<String> leads = organizationClient.getInspectionLead(taskDto.getWkGrpId()).getEntity();
            List<TaskDto> taskScoreDtos = taskService.getTaskDtoScoresByWorkGroupId(taskDto.getWkGrpId());
            String lead = inspEmailService.getLeadWithTheFewestScores(taskScoreDtos, leads);
            OrgUserDto leadDto=organizationClient.retrieveOrgUserAccountById(lead).getEntity();
            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
            MsgTemplateDto msgTemplateDto= notificationHelper.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_INS_002_INSPECTOR_EMAIL);
            Map<String,Object> mapTemplate=IaisCommonUtils.genNewHashMap();
            mapTemplate.put("inspection_lead", leadDto.getDisplayName());
            ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationViewDto.getApplicationDto().getAppGrpId()).getEntity();
            if (applicationGroupDto != null){
                OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                if (orgUserDto != null){
                    mapTemplate.put("ApplicantName", orgUserDto.getDisplayName());
                }
            }
            mapTemplate.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationViewDto.getApplicationDto().getApplicationType()));
            mapTemplate.put("ApplicationNumber", applicationViewDto.getApplicationDto().getApplicationNo());
            applicationViewDto.setSubmissionDate(IaisEGPHelper.parseToString(IaisEGPHelper.parseToDate( applicationViewDto.getSubmissionDate(),"dd/MM/yyyy HH:mm:ss"),"dd/MM/yyyy"));
            mapTemplate.put("ApplicationDate", applicationViewDto.getSubmissionDate());
            mapTemplate.put("systemLink", loginUrl);
            mapTemplate.put("HCI_CODE", applicationViewDto.getHciCode());
            mapTemplate.put("HCI_NAME",applicationViewDto.getHciName());
            mapTemplate.put("Address", applicationViewDto.getHciAddress());
            mapTemplate.put("HCI_Postal_Code", applicationViewDto.getHciPostalCode());
            mapTemplate.put("LicenseeName", licenseeDto.getName());
            AppPremisesRecommendationDto appPreRecommentdationDtoInspectionDate =insepctionNcCheckListService.getAppRecomDtoByAppCorrId(correlationId,InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
            if(appPreRecommentdationDtoInspectionDate!=null){
                mapTemplate.put("InspectionDate", Formatter.formatDate(appPreRecommentdationDtoInspectionDate.getRecomInDate()));
            }else {
                mapTemplate.put("InspectionDate", "-");
            }
//cancel old calendar
            AppPremisesInspecApptDto appPremisesInspecApptDto=inspectionTaskClient.getSpecificDtoByAppPremCorrId(correlationId).getEntity();
            ApptUserCalendarDto cancelCalendarDto = new ApptUserCalendarDto();
            AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
            if(appPremisesInspecApptDto!=null&&appPremisesInspecApptDto.getApptRefNo()!=null){
                cancelCalendarDto.setApptRefNo(appPremisesInspecApptDto.getApptRefNo());
                cancelCalendarDto.setAuditTrailDto(auditTrailDto);
                cancelCalendarDto.setStatus(AppointmentConstants.CALENDAR_STATUS_RESERVED);
                try {
                    List<ApptUserCalendarDto> apptUserCalendarDtos= appointmentClient.getCalenderByApptRefNoAndStatus(cancelCalendarDto).getEntity();
                    if(apptUserCalendarDtos!=null&&!apptUserCalendarDtos.isEmpty()&&apptUserCalendarDtos.get(0).getEndSlot()!=null){
                        Date startDt= apptUserCalendarDtos.get(0).getStartSlot().get(0);
                        Date endDt= apptUserCalendarDtos.get(0).getEndSlot().get(0);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(startDt);
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        String inspStartDate_1 = Formatter.formatDateTime(cal.getTime());
                        String inspEndDate = Formatter.formatDateTime(endDt);
                        if(inspStartDate_1.compareTo(inspEndDate)>0){
                            mapTemplate.put("InspectionEndDate", Formatter.formatDate(endDt));
                        }
                    }
                }catch (Exception e){
                    log.info(e.getMessage(),e);
                }
            }
            Map<String,Object> mapTableTemplate=IaisCommonUtils.genNewHashMap();
            MsgTemplateDto msgTableTemplateDto= notificationHelper.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_INS_002_TABLE_12);

            if(ncAnswerDtos.size()!=0){
                StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append("<tr><td colspan=\"6\"><b>").append(applicationViewDto.getServiceType()).append("</b></td></tr>");
                int i=0;
                for (NcAnswerDto ncAnswerDto:ncAnswerDtos
                ) {
                    stringBuilder.append("<tr><td>").append(++i);
                    //EAS or MTS
                    if(vehicleOpenFlag.equals(InspectionConstants.SWITCH_ACTION_YES)&&applicationViewDto.getAppSvcVehicleDtos()!=null&&(applicationViewDto.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE)||applicationViewDto.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE))){
                        boolean isDisplayName=false;
                        for (AppSvcVehicleDto asvd:applicationViewDto.getAppSvcVehicleDtos()
                             ) {
                            if(asvd.getVehicleName().equals(ncAnswerDto.getVehicleName())){
                                stringBuilder.append(TD).append(StringUtil.viewHtml(asvd.getDisplayName()));
                                isDisplayName=true;
                                break;
                            }
                        }
                        if(!isDisplayName){
                            stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getType()));
                        }
                    }else {
                        stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getType()));
                    }
                    stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getItemQuestion()));
                    stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getNcs()));
                    stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getRemark()));
                    stringBuilder.append(TD).append(StringUtil.viewHtml("1".equals(ncAnswerDto.getRef())?"Yes":"No"));
                    stringBuilder.append("</td></tr>");
                }
                mapTableTemplate.put("NC_DETAILS",StringUtil.viewHtml(stringBuilder.toString()));
            }
            String observation = fillupChklistService.getObservationByAppPremCorrId(taskDto.getRefNo());
            //mapTemplate.put("ServiceName", applicationViewDto.getServiceType());
            if(appPreRecommentdationDto!=null&&(appPreRecommentdationDto.getBestPractice()!=null||observation!=null)){
                int sn=1;
                String[] observations=new String[]{};
                if(observation!=null){
                    observations=observation.split("\n");
                }
                String[] recommendations=new String[]{};
                if(appPreRecommentdationDto.getBestPractice()!=null){
                    recommendations=appPreRecommentdationDto.getBestPractice().split("\n");
                }
                StringBuilder stringBuilder=new StringBuilder();
                if(recommendations.length>=observations.length){
                    for (int i=0;i<recommendations.length;i++){
                        if(i<observations.length){
                            stringBuilder.append("<tr><td>").append(sn).append(TD).append(StringUtil.viewHtml(observations[i])).append(TD).append(StringUtil.viewHtml(recommendations[i])).append("</td></tr>");
                        }else {
                            stringBuilder.append("<tr><td>").append(sn).append(TD).append(StringUtil.viewHtml("")).append(TD).append(StringUtil.viewHtml(recommendations[i])).append("</td></tr>");
                        }
                        sn++;

                    }
                }else {
                    for (int i=0;i<observations.length;i++){
                        if(i<recommendations.length){
                            stringBuilder.append("<tr><td>").append(sn).append(TD).append(StringUtil.viewHtml(observations[i])).append(TD).append(StringUtil.viewHtml(recommendations[i])).append("</td></tr>");
                        }else {
                            stringBuilder.append("<tr><td>").append(sn).append(TD).append(StringUtil.viewHtml(observations[i])).append(TD).append(StringUtil.viewHtml("")).append("</td></tr>");
                        }
                        sn++;
                    }
                }
                mapTableTemplate.put("Observation_Recommendation",StringUtil.viewHtml(stringBuilder.toString()));
            }
            msgTableTemplateDto.setMessageContent(MsgUtil.getTemplateMessageByContent(msgTableTemplateDto.getMessageContent(),mapTableTemplate));

            mapTemplate.put("NC_DETAILS_AND_Observation_Recommendation",msgTableTemplateDto.getMessageContent());
            HcsaServiceDto svcDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(applicationViewDto.getApplicationDto().getServiceId()).getEntity();
            if("NSH".equals(svcDto.getSvcCode())){
                mapTemplate.put("SVC_NSH","is Nursing Home");
            }
            mapTemplate.put("HALP", AppConsts.MOH_SYSTEM_NAME);
            AppPremisesRecommendationDto appPreRecommentdationDtoInspDate =insepctionNcCheckListService.getAppRecomDtoByAppCorrId(applicationViewDto.getAppPremisesCorrelationId(),InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
            List<Date> holidays = appointmentClient.getHolidays().getEntity();
            String groupName = organizationClient.getWrkGrpById(taskDto.getWkGrpId()).getEntity().getGroupName();
            List<ApptNonWorkingDateDto> nonWorkingDateListByWorkGroupId = appointmentClient.getNonWorkingDateListByWorkGroupId(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY,groupName).getEntity();
            Date addTenDays= WorkDayCalculateUtil.getDate(appPreRecommentdationDtoInspDate.getRecomInDate(),systemParamConfig.getRectificateDay(),holidays,nonWorkingDateListByWorkGroupId);
            mapTemplate.put("DDMMYYYY", StringUtil.viewHtml(Formatter.formatDateTime(addTenDays,Formatter.DATE)));
            mapTemplate.put("Inspector_mail_Address", leadDto.getEmail());
            mapTemplate.put("InspectorDID", leadDto.getOfficeTelNo());
            mapTemplate.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
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

        // mesContext= MsgUtil.getTemplateMessageByContent(inspectionEmailTemplateDto.getMessageContent(),map);

        String content= (String) ParamUtil.getSessionAttr(request,MSG_CON);
        if(content!=null){
            mesContext=content;
        }
        inspectionEmailTemplateDto.setMessageContent(mesContext);

        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{InspectionConstants.PROCESS_DECI_ROTE_EMAIL_AO1_REVIEW,InspectionConstants.PROCESS_DECI_ROTE_EMAIL_INSPECTION_LEAD_REVIEW});
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryDtosByAppNo(applicationViewDto.getApplicationDto().getApplicationNo());
        for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto1:appPremisesRoutingHistoryDtos){
            if(!StringUtil.isEmpty(appPremisesRoutingHistoryDto1.getWrkGrpId())) {
                appPremisesRoutingHistoryDto1.setWrkGrpId(applicationViewService.getWrkGrpName(appPremisesRoutingHistoryDto1.getWrkGrpId()));
            }
            if(StringUtil.isEmpty(appPremisesRoutingHistoryDto1.getActionby())){
                appPremisesRoutingHistoryDto1.setActionby("-");
            }
            else {
                appPremisesRoutingHistoryDto1.setActionby(applicationViewService.getUserNameById(new ArrayList<String>(Collections.singleton(appPremisesRoutingHistoryDto1.getActionby()))).get(0).getDisplayName());
            }
            if(StringUtil.isEmpty(appPremisesRoutingHistoryDto1.getProcessDecision())){
                if(StringUtil.isEmpty(appPremisesRoutingHistoryDto1.getAppStatus())){
                    appPremisesRoutingHistoryDto1.setProcessDecision(MasterCodeUtil.retrieveOptionsByCodes(new String[]{appPremisesRoutingHistoryDto1.getAppStatus()}).get(0).getText());
                }else {
                    appPremisesRoutingHistoryDto1.setProcessDecision("-");
                }
            }else {
                appPremisesRoutingHistoryDto1.setProcessDecision(MasterCodeUtil.retrieveOptionsByCodes(new String[]{appPremisesRoutingHistoryDto1.getProcessDecision()}).get(0).getText());
            }
        }
        inspectionEmailTemplateDto.setAppStatus(MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationDto().getStatus()}).get(0).getText());
        ParamUtil.setSessionAttr(bpc.request, TASK_DTO, taskDto);
        ParamUtil.setSessionAttr(request,"appPremCorrId", correlationId);
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setSessionAttr(request,MSG_CON, mesContext);
        ParamUtil.setSessionAttr(request,APP_VIEW_DTO,applicationViewDto);
        ParamUtil.setRequestAttr(request,"appPremisesRoutingHistoryDtos", appPremisesRoutingHistoryDtos);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);
    }


    public void emailSubmitStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        InspectionEmailTemplateDto inspectionEmailTemplateDto = (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(bpc.request,INS_EMAIL_DTO);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);
        String crudAction = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.info("*******************crudAction-->:{}" , crudAction);
    }

    public void previewEmail(BaseProcessClass bpc){
        log.info("=======>>>>>previewEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String content=ParamUtil.getString(request,MSG_CON);
        String subject=ParamUtil.getString(request,SUBJECT);
        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
        inspectionEmailTemplateDto.setMessageContent(content);
        inspectionEmailTemplateDto.setSubject(subject);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO,inspectionEmailTemplateDto);
        ParamUtil.setSessionAttr(request,SUBJECT, subject);
        ParamUtil.setSessionAttr(request,MSG_CON, content);


    }

    public void sendEmail(BaseProcessClass bpc) throws FeignException {

        log.info("=======>>>>>sendEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        ApplicationViewDto applicationViewDto= (ApplicationViewDto) ParamUtil.getSessionAttr(request,APP_VIEW_DTO);
        String serviceId=applicationViewDto.getApplicationDto().getServiceId();
        TaskDto taskDto= (TaskDto) ParamUtil.getSessionAttr(request,TASK_DTO);
        TaskDto taskDtoNew = taskService.getTaskById(taskDto.getId());
        if(taskDtoNew.getTaskStatus().equals(TaskConsts.TASK_STATUS_COMPLETED)){
            ParamUtil.setRequestAttr(request,"COMPLETED",Boolean.TRUE);
            return;
        }

        String decision=ParamUtil.getString(request,"decision");

        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
        inspectionEmailTemplateDto.setSubject(ParamUtil.getString(request,SUBJECT));
        inspectionEmailTemplateDto.setMessageContent(ParamUtil.getString(request,MSG_CON));
        inspectionEmailTemplateDto.setRemarks(ParamUtil.getString(request, "Remarks"));
        if (inspectionEmailTemplateDto.getSubject().isEmpty()){
            Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errorMap);
            ParamUtil.setRequestAttr(request,DemoConstants.ISVALID, SystemParameterConstant.STATUS_ACTIVE);
        }
        if (inspectionEmailTemplateDto.getMessageContent().isEmpty()){
            Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errorMap);
            ParamUtil.setRequestAttr(request,DemoConstants.ISVALID,SystemParameterConstant.STATUS_DEACTIVATED);
        }
        if (decision.equals(InspectionConstants.PROCESS_DECI_ROTE_EMAIL_AO1_REVIEW)){
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
            try {
                applicationService.updateFEApplicaitons(Collections.singletonList(applicationViewDto.getApplicationDto()));
            }catch (Exception e){
                log.info(e.getMessage(),e);
            }
            AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
            appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_AO1_EMAIL_VERIFY);
            appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusClient.update(appInspectionStatusDto);

            taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            inspEmailService.completedTask(taskDto);

            if(applicationViewDto.getApplicationDto().getRoutingServiceId()!=null){
                serviceId=applicationViewDto.getApplicationDto().getRoutingServiceId();
            }
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
            hcsaSvcStageWorkingGroupDto.setType(applicationViewDto.getApplicationDto().getApplicationType());
            hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
            hcsaSvcStageWorkingGroupDto.setOrder(2);
            String ao1Sel = ParamUtil.getString(bpc.request, "aoSelect");
            TaskDto taskDto1=new TaskDto();
            taskDto1.setApplicationNo(applicationViewDto.getApplicationDto().getApplicationNo());
            taskDto1.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            taskDto1.setRefNo(taskDto.getRefNo());
            taskDto1.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_AO1_VALIDATE_NCEMAIL);
            taskDto1.setRoleId(RoleConsts.USER_ROLE_AO1);

            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto1=hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity();
            taskDto1.setWkGrpId(hcsaSvcStageWorkingGroupDto1.getGroupId());
            String aoUserId = null;
            if (!StringUtil.isEmpty(ao1Sel)) {
                aoUserId = ao1Sel.replaceAll(hcsaSvcStageWorkingGroupDto1.getGroupId() + "_", "");
            }
            switch (hcsaSvcStageWorkingGroupDto1.getSchemeType()) {
                case TaskConsts.TASK_SCHEME_TYPE_COMMON:
                    taskDto1.setTaskType(TaskConsts.TASK_TYPE_INSPECTION);
                    taskDto1.setUserId(!StringUtil.isEmpty(aoUserId) ? aoUserId : null);
                    break;
                case TaskConsts.TASK_SCHEME_TYPE_ROUND:
                    taskDto1.setTaskType(taskDto.getTaskType());
                    taskDto1.setUserId(!StringUtil.isEmpty(aoUserId) ? aoUserId :
                            taskService.getUserIdForWorkGroup(taskDto1.getWkGrpId()).getUserId());
                    break;
                case TaskConsts.TASK_SCHEME_TYPE_ASSIGN:
                    taskDto1.setTaskType(TaskConsts.TASK_TYPE_INSPECTION_SUPER);
                    taskDto1.setUserId(!StringUtil.isEmpty(aoUserId) ? aoUserId : null);
                    break;
                default:taskDto1.setTaskType(taskDto.getTaskType());
                    taskDto1.setUserId(!StringUtil.isEmpty(aoUserId) ? aoUserId :
                            taskService.getUserIdForWorkGroup(taskDto1.getWkGrpId()).getUserId());
                    break;
            }
            List<TaskDto> taskDtos = prepareTaskList(taskDto1,applicationViewDto.getApplicationDto());
            taskService.createTasks(taskDtos);
            createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW, InspectionConstants.PROCESS_DECI_ROTE_EMAIL_AO1_REVIEW,taskDto,userId,inspectionEmailTemplateDto.getRemarks());
            createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW, taskDto,userId,"");
        }
        else {
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
            try {
                applicationService.updateFEApplicaitons(Collections.singletonList(applicationViewDto.getApplicationDto()));
            }catch (Exception e){
                log.info(e.getMessage(),e);
            }
            AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
            appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_JOB_CREATE_TASK_TO_LEADER);
            appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusClient.update(appInspectionStatusDto);
            taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            inspEmailService.completedTask(taskDto);
            createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_DRAFT_LETTER,InspectionConstants.PROCESS_DECI_ROTE_EMAIL_INSPECTION_LEAD_REVIEW, taskDto, userId,inspectionEmailTemplateDto.getRemarks());
            taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTION_LEAD);
            createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING, taskDto, userId,"");

        }
        String draftId=inspEmailService.insertEmailDraft(inspectionEmailTemplateDto);
        if(draftId==null){
            inspEmailService.insertEmailDraft(inspectionEmailTemplateDto);
        }
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);

    }
    public void doRecallEmail(BaseProcessClass bpc) {
        log.info("=======>>>>>doRecallEmail>>>>>>>>>>>>>>>>emailRequest");
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus, String decision,
                                                                         TaskDto taskDto, String userId, String remarks) {
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
        appPremisesRoutingHistoryDto.setSubStage(HcsaConsts.ROUTING_STAGE_POT);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        return appPremisesRoutingHistoryDto;
    }



    private List<TaskDto> prepareTaskList(TaskDto taskDto, ApplicationDto applicationDto)  {
        List<TaskDto> list = IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> applicationDtos= IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = inspEmailService.generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_INS);
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        Integer count = hcsaSvcStageWorkingGroupDtos.get(0).getCount();

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


}
