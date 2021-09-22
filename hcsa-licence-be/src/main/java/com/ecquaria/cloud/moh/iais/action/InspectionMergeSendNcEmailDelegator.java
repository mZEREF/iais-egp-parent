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
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
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
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.util.WorkDayCalculateUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    InsepctionNcCheckListService insepctionNcCheckListService;
    @Autowired
    InspectionService inspectionService;
    @Autowired
    private TaskService taskService;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    ApplicationViewService applicationViewService;
    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;

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
    ApplicationClient applicationClient;
    @Autowired
    private AppointmentClient appointmentClient;
    @Value("${easmts.vehicle.sperate.flag}")
    private String vehicleOpenFlag;
    private static final String INS_EMAIL_DTO="insEmailDto";
    private static final String TASK_DTO="taskDto";
    private static final String SUBJECT="subject";
    private static final String MSG_CON="messageContent";
    private static final String APP_VIEW_DTO="applicationViewDto";
    private static final String TD="</td><td>";

    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request=bpc.request;String taskId = "";
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
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_INSPECTION_MAIL);
        TaskDto taskDto = taskService.getTaskById(taskId);
        if( taskDto == null) {
            return;
        }
        ParamUtil.setSessionAttr(bpc.request, TASK_DTO, taskDto);
        ParamUtil.setSessionAttr(request,"appPremCorrIds",null);
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

        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos1=inspEmailService.getAppPremisesCorrelationsByPremises(correlationId);

        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos=IaisCommonUtils.genNewArrayList();
        for (AppPremisesCorrelationDto appPremisesCorrDto:appPremisesCorrelationDtos1
        ) {
            ApplicationDto appDto = applicationService.getApplicationBytaskId(appPremisesCorrDto.getId());
            if(appDto.getStatus().equals(ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING)){
                appPremisesCorrelationDtos.add(appPremisesCorrDto);
            }
        }
        if(applicationViewDto.getApplicationDto().isFastTracking()){
            appPremisesCorrelationDtos.clear();
            AppPremisesCorrelationDto appCorrDto =new AppPremisesCorrelationDto();
            appCorrDto.setId(correlationId);
            appPremisesCorrelationDtos.add(appCorrDto);
        }

        List<String> appPremCorrIds= IaisCommonUtils.genNewArrayList();
        List<String> svcNames=IaisCommonUtils.genNewArrayList();



        InspectionEmailTemplateDto inspectionEmailTemplateDto= new InspectionEmailTemplateDto();
        if(appPremisesCorrelationDtos.size()==1){
            if(ParamUtil.getSessionAttr(request,INS_EMAIL_DTO)!=null){
                inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
            }
            else {
                inspectionEmailTemplateDto=inspEmailService.getInsertEmail(appPremisesCorrelationDtos.get(0).getId());
            }
            appPremCorrIds.add(appPremisesCorrelationDtos.get(0).getId());
            svcNames.add(inspectionService.getHcsaServiceDtoByServiceId(applicationViewDto.getApplicationDto().getServiceId()).getSvcName());
        }else {

            List<String> leads = organizationClient.getInspectionLead(taskDto.getWkGrpId()).getEntity();
            List<TaskDto> taskScoreDtos = taskService.getTaskDtoScoresByWorkGroupId(taskDto.getWkGrpId());
            String lead = inspEmailService.getLeadWithTheFewestScores(taskScoreDtos, leads);
            OrgUserDto leadDto=organizationClient.retrieveOrgUserAccountById(lead).getEntity();
            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
            MsgTemplateDto msgTemplateDto= notificationHelper.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_INS_002_INSPECTOR_EMAIL);
            Map<String,Object> mapTemplate=IaisCommonUtils.genNewHashMap();
            LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(applicationViewDto.getApplicationGroupDto().getLicenseeId());
            mapTemplate.put("inspection_lead", leadDto.getDisplayName());
            ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationViewDto.getApplicationDto().getAppGrpId()).getEntity();
            if (applicationGroupDto != null){
                OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                if (orgUserDto != null){
                    mapTemplate.put("ApplicantName", orgUserDto.getDisplayName());
                }
            }
            mapTemplate.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationDto().getApplicationType()}).get(0).getText());
            StringBuilder appNos= new StringBuilder();
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
            int sn=1;
            StringBuilder stringTable=new StringBuilder();
            StringBuilder stringBuilder1=new StringBuilder();
            StringBuilder stringBuilder2=new StringBuilder();
            for (AppPremisesCorrelationDto appPremisesCorrelationDto:appPremisesCorrelationDtos
            ) {
                try{
                    appPremCorrIds.add(appPremisesCorrelationDto.getId());
                    ApplicationViewDto appViewDto = inspEmailService.getAppViewByCorrelationId(appPremisesCorrelationDto.getId());
                    HcsaServiceDto svcDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(appViewDto.getApplicationDto().getServiceId()).getEntity();
                    if("NSH".equals(svcDto.getSvcCode())){
                        mapTemplate.put("SVC_NSH","is Nursing Home");
                    }
                    appNos.append(appViewDto.getApplicationDto().getApplicationNo()).append(' ');
                    svcNames.add(inspectionService.getHcsaServiceDtoByServiceId(appViewDto.getApplicationDto().getServiceId()).getSvcName());

                    AppPremisesRecommendationDto appPreRecommentdationDto =insepctionNcCheckListService.getAppRecomDtoByAppCorrId(appPremisesCorrelationDto.getId(),InspectionConstants.RECOM_TYPE_TCU);
                    List<NcAnswerDto> ncAnswerDtos=insepctionNcCheckListService.getNcAnswerDtoList(appPremisesCorrelationDto.getId());
                    String observation = fillupChklistService.getObservationByAppPremCorrId(appPremisesCorrelationDto.getId());
                    if(ncAnswerDtos.size()!=0){
                        stringBuilder1.append("<tr><td colspan=\"6\"><b>").append(appViewDto.getServiceType()).append("</b></td></tr>");
                        int i=0;
                        for (NcAnswerDto ncAnswerDto:ncAnswerDtos
                        ) {
                            stringBuilder1.append("<tr><td>").append(++i);
                            //EAS or MTS
                            if(vehicleOpenFlag.equals(InspectionConstants.SWITCH_ACTION_YES)
                                    &&applicationViewDto.getAppSvcVehicleDtos()!=null
                                    &&(applicationViewDto.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE)
                                    ||applicationViewDto.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE))){
                                boolean isDisplayName=false;
                                for (AppSvcVehicleDto asvd:applicationViewDto.getAppSvcVehicleDtos()
                                ) {
                                    if(asvd.getVehicleName().equals(ncAnswerDto.getVehicleName())){
                                        stringBuilder1.append(TD).append(StringUtil.viewHtml(asvd.getDisplayName()));
                                        isDisplayName=true;
                                        break;
                                    }
                                }
                                if(!isDisplayName){
                                    stringBuilder1.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getVehicleName()));
                                }
                            }else {
                                stringBuilder1.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getType()));
                            }
                            stringBuilder1.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getItemQuestion()));
                            stringBuilder1.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getNcs()));
                            stringBuilder1.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getRemark()));
                            stringBuilder1.append(TD).append(StringUtil.viewHtml("1".equals(ncAnswerDto.getRef())?"Yes":"No"));
                            stringBuilder1.append("</td></tr>");
                        }
                        mapTableTemplate.put("NC_DETAILS",StringUtil.viewHtml(stringBuilder1.toString()));
                    }
                    if(appPreRecommentdationDto!=null&&(appPreRecommentdationDto.getBestPractice()!=null||observation!=null)){
                        String[] observations=new String[]{};
                        if(observation!=null){
                            observations=observation.split("\n");
                        }
                        String[] recommendations=new String[]{};
                        if(appPreRecommentdationDto.getBestPractice()!=null){
                            recommendations=appPreRecommentdationDto.getBestPractice().split("\n");
                        }
                        if(recommendations.length>=observations.length){
                            for (int i=0;i<recommendations.length;i++){
                                if(i<observations.length){
                                    stringBuilder2.append("<tr><td>").append(sn).append(TD).append(StringUtil.viewHtml(observations[i])).append(TD).append(StringUtil.viewHtml(recommendations[i])).append("</td></tr>");
                                }else {
                                    stringBuilder2.append("<tr><td>").append(sn).append(TD).append(StringUtil.viewHtml("")).append(TD).append(StringUtil.viewHtml(recommendations[i])).append("</td></tr>");
                                }
                                sn++;

                            }
                        }else {
                            for (int i=0;i<observations.length;i++){
                                if(i<recommendations.length){
                                    stringBuilder2.append("<tr><td>").append(sn).append(TD).append(StringUtil.viewHtml(observations[i])).append(TD).append(StringUtil.viewHtml(recommendations[i])).append("</td></tr>");
                                }else {
                                    stringBuilder2.append("<tr><td>").append(sn).append(TD).append(StringUtil.viewHtml(observations[i])).append(TD).append(StringUtil.viewHtml("")).append("</td></tr>");
                                }
                                sn++;
                            }
                        }
                        mapTableTemplate.put("Observation_Recommendation",StringUtil.viewHtml(stringBuilder2.toString()));
                    }
                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }
            }
            stringTable.append(MsgUtil.getTemplateMessageByContent(msgTableTemplateDto.getMessageContent(),mapTableTemplate));
            mapTemplate.put("ApplicationNumber", appNos.toString());
            mapTemplate.put("NC_DETAILS_AND_Observation_Recommendation",stringTable.toString());

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
            inspectionEmailTemplateDto.setSubject(MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),mapTemplate));

            inspectionEmailTemplateDto.setAppPremCorrId(applicationViewDto.getAppPremisesCorrelationId());
            inspectionEmailTemplateDto.setMessageContent(msgTemplateDto.getMessageContent());
        }

        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT,InspectionConstants.PROCESS_DECI_SENDS_EMAIL_APPLICANT});

        ParamUtil.setSessionAttr(bpc.request, TASK_DTO, taskDto);
        ParamUtil.setSessionAttr(request,"appPremCorrIds", (Serializable) appPremCorrIds);
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setSessionAttr(request,MSG_CON, inspectionEmailTemplateDto.getMessageContent());
        ParamUtil.setSessionAttr(request,"svcNames", (Serializable) svcNames);
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
        TaskDto taskDtoNew = taskService.getTaskById(taskDto.getId());
        if(taskDtoNew.getTaskStatus().equals(TaskConsts.TASK_STATUS_COMPLETED)){
            ParamUtil.setRequestAttr(request,"COMPLETED",Boolean.TRUE);
            return;
        }
        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
        inspectionEmailTemplateDto.setSubject(ParamUtil.getString(request,SUBJECT));
        inspectionEmailTemplateDto.setMessageContent(ParamUtil.getString(request,MSG_CON));
        inspectionEmailTemplateDto.setRemarks(ParamUtil.getString(request, "Remarks"));
        String decision=ParamUtil.getString(request,"decision");
        if("Select".equals(decision)){decision=InspectionConstants.PROCESS_DECI_SENDS_EMAIL_APPLICANT;}
        List<String>appPremCorrIds= (List<String>) ParamUtil.getSessionAttr(request,"appPremCorrIds");

        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos1=inspEmailService.getAppPremisesCorrelationsByPremises(applicationViewDto.getAppPremisesCorrelationId());
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos=IaisCommonUtils.genNewArrayList();
        for (AppPremisesCorrelationDto appPremisesCorrDto:appPremisesCorrelationDtos1
        ) {
            ApplicationDto appDto = applicationService.getApplicationBytaskId(appPremisesCorrDto.getId());
            if(appDto.getStatus().equals(ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING)){
                appPremisesCorrelationDtos.add(appPremisesCorrDto);
            }
        }
        if(applicationViewDto.getApplicationDto().isFastTracking()){
            appPremisesCorrelationDtos.clear();
            AppPremisesCorrelationDto appCorrDto =new AppPremisesCorrelationDto();
            appCorrDto.setId(applicationViewDto.getAppPremisesCorrelationId());
            appPremisesCorrelationDtos.add(appCorrDto);
        }

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
            completedTask(appPremCorrIds);
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
                    try {
                        applicationService.updateFEApplicaitons(Collections.singletonList(applicationViewDto1.getApplicationDto()));
                    }catch (Exception e){
                        log.info(e.getMessage(),e);
                    }
                    AppInspectionStatusDto appInspectionStatusDto1 = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorrIds.get(i)).getEntity();
                    appInspectionStatusDto1.setStatus(InspectionConstants.INSPECTION_STATUS_INSPECTOR_LEAD_ROUTE_BACK_EMAIL);
                    appInspectionStatusDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    appInspectionStatusClient.update(appInspectionStatusDto1);

                    String serviceId=applicationViewDto1.getApplicationDto().getServiceId();
                    HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                    hcsaSvcStageWorkingGroupDto.setType(applicationViewDto1.getApplicationDto().getApplicationType());
                    if(applicationViewDto1.getApplicationDto().getRoutingServiceId()!=null){
                        serviceId=applicationViewDto1.getApplicationDto().getRoutingServiceId();
                    }
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

                    List<TaskDto> taskDtos = prepareTaskList(taskDto2,applicationViewDto1.getApplicationDto());
                    taskService.createTasks(taskDtos);
                    createAppPremisesRoutingHistory(applicationViewDto1.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING,InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT,taskDto, loginContext.getUserId(),inspectionEmailTemplateDto.getRemarks(),HcsaConsts.ROUTING_STAGE_POT);
                    createAppPremisesRoutingHistory(applicationViewDto1.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING, ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER,taskDto2, userId,"",HcsaConsts.ROUTING_STAGE_POT);

                }
            }

        }
        else {
            taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTION_LEAD);
            completedTask(appPremCorrIds);
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
                try {
                    applicationService.updateFEApplicaitons(Collections.singletonList(applicationViewDto.getApplicationDto()));
                }catch (Exception e){
                    log.info(e.getMessage(),e);
                }
                AppInspectionStatusDto appInspectionStatusDto1 = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
                appInspectionStatusDto1.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_NC_RECTIFICATION_EMAIL);
                appInspectionStatusDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                appInspectionStatusClient.update(appInspectionStatusDto1);
                createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING,InspectionConstants.PROCESS_DECI_SENDS_EMAIL_APPLICANT, taskDto, userId,inspectionEmailTemplateDto.getRemarks(),HcsaConsts.ROUTING_STAGE_POT);


                for (String s : appPremCorrIdsNoNc) {
                    ApplicationViewDto applicationViewDto1 = applicationViewService.searchByCorrelationIdo(s);
                    List<String> roleIds = new ArrayList<>();
                    roleIds.add(RoleConsts.USER_ROLE_INSPECTIOR);
                    List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryDtosByAppNoAndRoleIds(applicationViewDto1.getApplicationDto().getApplicationNo(),roleIds);
                    appPremisesRoutingHistoryDtos.sort(Comparator.comparing(AppPremisesRoutingHistoryDto::getUpdatedDt));
                    AppPremisesRoutingHistoryDto appPremisesRoutingHisDto = appPremisesRoutingHistoryDtos.get(appPremisesRoutingHistoryDtos.size()-1);

                    applicationViewDto1.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT);
                    applicationViewService.updateApplicaiton(applicationViewDto1.getApplicationDto());
                    try {
                        applicationService.updateFEApplicaitons(Collections.singletonList(applicationViewDto1.getApplicationDto()));
                    } catch (Exception e) {
                        log.info(e.getMessage(), e);
                    }
                    AppInspectionStatusDto appInspectionStatusDto2 = appInspectionStatusClient.getAppInspectionStatusByPremId(s).getEntity();
                    appInspectionStatusDto2.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
                    appInspectionStatusDto2.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    appInspectionStatusClient.update(appInspectionStatusDto2);

                    String serviceId = applicationViewDto1.getApplicationDto().getServiceId();
                    HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                    hcsaSvcStageWorkingGroupDto.setType(applicationViewDto1.getApplicationDto().getApplicationType());
                    if(applicationViewDto1.getApplicationDto().getRoutingServiceId()!=null){
                        serviceId=applicationViewDto1.getApplicationDto().getRoutingServiceId();
                    }
                    hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
                    hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
                    hcsaSvcStageWorkingGroupDto.setOrder(1);
                    TaskDto taskDto2 = new TaskDto();
                    taskDto2.setApplicationNo(applicationViewDto1.getApplicationDto().getApplicationNo());
                    taskDto2.setRefNo(s);
                    taskDto2.setTaskType(taskDto.getTaskType());
                    taskDto2.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
                    taskDto2.setUserId(appPremisesRoutingHisDto.getActionby());
                    taskDto2.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT);
                    taskDto2.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
                    taskDto2.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());

                    List<TaskDto> taskDtos = prepareTaskList(taskDto2, applicationViewDto1.getApplicationDto());
                    taskService.createTasks(taskDtos);
                    createAppPremisesRoutingHistory(applicationViewDto1.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT, taskDto2, userId, "", HcsaConsts.ROUTING_STAGE_POT);

                }
            }
            {
                for (String s : appPremCorrIdsIsNc) {
                    ApplicationViewDto applicationViewDto1 = applicationViewService.searchByCorrelationIdo(s);
                    applicationViewDto1.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_CREATE_MESG);
                    applicationViewService.updateApplicaiton(applicationViewDto1.getApplicationDto());
                    try {
                        applicationService.updateFEApplicaitons(Collections.singletonList(applicationViewDto1.getApplicationDto()));
                    } catch (Exception e) {
                        log.info(e.getMessage(), e);
                    }
                    AppInspectionStatusDto appInspectionStatusDto1 = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto1.getAppPremisesCorrelationId()).getEntity();
                    appInspectionStatusDto1.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_NC_RECTIFICATION_EMAIL);
                    appInspectionStatusDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    appInspectionStatusClient.update(appInspectionStatusDto1);
                    createAppPremisesRoutingHistory(applicationViewDto1.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_CREATE_MESG, ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_CREATE_MESG, taskDto, userId, "", HcsaConsts.ROUTING_STAGE_POT);
                }

            }
            try {
                ParamUtil.setRequestAttr(request,"LEADER_SEND",Boolean.TRUE);

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
                //msg
                List<String> svcCode=IaisCommonUtils.genNewArrayList();
                List<String> svcNames= (List<String>) ParamUtil.getSessionAttr(request,"svcNames");
                for (String svcName:svcNames
                ) {
                    HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(svcName).getEntity();
                    svcCode.add(svcDto.getSvcCode());
                }
                EmailParam msgParam = new EmailParam();
                msgParam.setQueryCode(applicationViewDto.getApplicationDto().getApplicationNo());
                msgParam.setReqRefNum(applicationViewDto.getApplicationDto().getApplicationNo());
                msgParam.setRefId(applicationViewDto.getApplicationDto().getApplicationNo());
                msgParam.setTemplateContent(mapTemplate);
                msgParam.setSubject(inspectionEmailTemplateDto.getSubject());
                msgParam.setSvcCodeList(svcCode);
                msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_INS_002_INSPECTOR_EMAIL_MSG);
                msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                notificationHelper.sendNotification(msgParam);
                //sms
                EmailParam smsParam = new EmailParam();
                smsParam.setQueryCode(applicationViewDto.getApplicationDto().getApplicationNo());
                smsParam.setReqRefNum(applicationViewDto.getApplicationDto().getApplicationNo());
                smsParam.setRefId(applicationViewDto.getApplicationDto().getApplicationNo());
                smsParam.setTemplateContent(mapTemplate);
                smsParam.setSubject(inspectionEmailTemplateDto.getSubject());
                smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_INS_002_INSPECTOR_EMAIL_SMS);
                smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                notificationHelper.sendNotification(smsParam);
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }

        }
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);

    }

    private void createAppPremisesRoutingHistory(String appNo, String appStatus, String decision,
                                                 TaskDto taskDto, String userId, String remarks, String subStage) {
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
        appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
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
    private void completedTask(List<String> appPremCorrIds) {
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
