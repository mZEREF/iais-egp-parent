package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.acra.AcraConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppInGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.*;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BankedInboxDelegator
 *
 * @author junyu
 * @date 2019/12/12
 */
@Delegator("backendInboxDelegator")
@Slf4j
public class BackendInboxDelegator {
    @Autowired
    InspectionMainService inspectionService;

    @Autowired
    TaskService taskService;

    @Autowired
    ApplicationViewMainService applicationViewService;

    @Autowired
    private GenerateIdClient generateIdClient;

    @Autowired
    private OrganizationMainClient organizationMainClient;
    @Autowired
    private AppInspectionStatusMainClient appInspectionStatusMainClient;

    @Autowired
    AppPremisesRoutingHistoryMainService appPremisesRoutingHistoryService;

    @Autowired
    private BroadcastMainService broadcastService;

    @Autowired
    private HcsaConfigMainClient hcsaConfigMainClient;

    @Autowired
    private AppPremisesRoutingHistoryMainClient appPremisesRoutingHistoryMainClient;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private EmailClient emailClient;

    @Autowired
    private InspEmailService inboxMsgService;

    @Autowired
    ApplicationMainClient applicationMainClient;

    @Value("${iais.email.sender}")
    private String mailSender;

    @Value("${iais.system.one.address}")
    private String systemAddressOne;

    static private String APPSTATUSCATEID = "BEE661EE-220C-EA11-BE7D-000C29F371DC";

    public void start(BaseProcessClass bpc){
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<SelectOption> selectOptionArrayList = IaisCommonUtils.genNewArrayList();
        for (String item : loginContext.getRoleIds()) {
            selectOptionArrayList.add(new SelectOption(item,item));
        }
        log.debug(StringUtil.changeForLog("the BackendInboxDelegator start ...."));
        String curRole = "";
        curRole = loginContext.getCurRoleId();
        SearchParam searchParamGroup = getSearchParam(bpc.request,true);
        ParamUtil.setSessionAttr(bpc.request, "backendinboxSearchParam", searchParamGroup);
        ParamUtil.setSessionAttr(bpc.request, "curRole",curRole);
        ParamUtil.setSessionAttr(bpc.request, "searchParamAjax",null);
        ParamUtil.setSessionAttr(bpc.request, "taskList",null);
        ParamUtil.setSessionAttr(bpc.request, "hastaskList",null);
        ParamUtil.setSessionAttr(bpc.request, "appNoUrl",null);
        ParamUtil.setSessionAttr(bpc.request, "taskMap",null);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", null);
        ParamUtil.setSessionAttr(bpc.request, "roleIds", (Serializable) selectOptionArrayList);
        AuditTrailHelper.auditFunction("Backend Inbox", "Backend Inbox");

    }



    public void searchInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, "taskList",null);
        ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
    }

    /**
     * StartStep: inspectionSupSearchPre
     *
     * @param bpc
     * @throws
     */
    public void prepareData(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchPre start ...."));
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        AuditTrailHelper.auditFunction("BE-inbox", "Backend Inbox");
        List<String> workGroupIds = inspectionService.getWorkGroupIdsByLogin(loginContext);
        List<SelectOption> appTypeOption = inspectionService.getAppTypeOption();
        List<SelectOption> appStatusOption = inspectionService.getAppStatusOption(loginContext.getCurRoleId());
        SearchParam searchParamGroup = getSearchParam(bpc.request,false);
        ParamUtil.setSessionAttr(bpc.request, "backendinboxSearchParam", searchParamGroup);

        String hci_name = (String) searchParamGroup.getFilters().get("hci_name");
        String address = (String) searchParamGroup.getFilters().get("address");
        String application_no = (String) searchParamGroup.getFilters().get("application_no");
        if(!StringUtil.isEmpty(hci_name)){
            hci_name = hci_name.substring(1,hci_name.length()-1);
        }
        if(!StringUtil.isEmpty(address)){
            address = address.substring(1,address.length()-1);
        }
        if(!StringUtil.isEmpty(application_no)){
            application_no = application_no.substring(1,application_no.length()-1);
        }
        ParamUtil.setRequestAttr(bpc.request, "hci_name", hci_name);
        ParamUtil.setRequestAttr(bpc.request, "address", address);
        ParamUtil.setRequestAttr(bpc.request, "application_no", application_no);
        ParamUtil.setRequestAttr(bpc.request, "appTypeOption", (Serializable) appTypeOption);
        ParamUtil.setRequestAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
        ParamUtil.setRequestAttr(bpc.request, "workGroupIds", (Serializable) workGroupIds);
        String swithtype = (String)ParamUtil.getRequestAttr(bpc.request, "SearchSwitchType");
        if(swithtype == null || "".equals(swithtype)){
            ParamUtil.setRequestAttr(bpc.request, "SearchSwitchType","search");
        }
    }

    private SearchParam getSearchParam(HttpServletRequest request, boolean neednew){
        SearchParam searchParamGroup = (SearchParam) ParamUtil.getSessionAttr(request, "backendinboxSearchParam");
        if(neednew){
            searchParamGroup = new SearchParam(InspectionAppGroupQueryDto.class.getName());
            searchParamGroup.setPageSize(10);
            searchParamGroup.setPageNo(1);
            searchParamGroup.setSort("SUBMIT_DT", SearchParam.ASCENDING);
        }
        return searchParamGroup;

    }

    /**
     * StartStep: inspectionSupSearchStartStep1
     *
     * @param bpc
     * @throws
     */
    public void searchStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchStartStep1 start ...."));

    }

    /**
     * StartStep: inspectionSupSearchDoSearch
     *
     * @param bpc
     * @throws
     */
    public void doSearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchDoSearch start ...."));
        SearchParam searchParamGroup = getSearchParam(bpc.request,true);
        ParamUtil.setSessionAttr(bpc.request, "backendinboxSearchParam", searchParamGroup);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);

        String inspectorValue = loginContext.getLoginId();
        List<TaskDto> commPools = getCommPoolBygetUserId(loginContext.getUserId(),loginContext.getCurRoleId());
        String[] applicationNo_list = inspectionService.getApplicationNoListByPool(commPools);
        if(applicationNo_list == null || applicationNo_list.length == 0){
            applicationNo_list = new String[]{SystemParameterConstants.PARAM_FALSE};
        }
        String applicationStr = SqlHelper.constructInCondition("T1.APPLICATION_NO",applicationNo_list.length);
    }

    /**
     * StartStep: inspectionSupSearchsearchPage
     *
     * @param bpc
     * @throws
     */
    public void searchPage(BaseProcessClass bpc){
        SearchParam searchParamGroup = getSearchParam(bpc.request,false);
        CrudHelper.doPaging(searchParamGroup,bpc.request);
    }

    public void changeRole(BaseProcessClass bpc){
        SearchParam searchParamGroup = getSearchParam(bpc.request,true);
        ParamUtil.setSessionAttr(bpc.request, "backendinboxSearchParam", searchParamGroup);
        String curRole = ParamUtil.getRequestString(bpc.request, "roleIds");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        loginContext.setCurRoleId(curRole);
        ParamUtil.setSessionAttr(bpc.request,"curRole",curRole);
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
    }



    private List<TaskDto> getCommPoolBygetUserId(String getUserId, String curRole) {
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
        if(getUserId == null){
            return null;
        }
        if(curRole == null){
            return null;
        }
        for(TaskDto tDto:inspectionService.getTasksByUserIdAndRole(getUserId,curRole)){
            taskDtoList.add(tDto);
        }
        return taskDtoList;
    }

    /**
     * StartStep: searchSort
     *
     * @param bpc
     * @throws
     */
    public void searchSort(BaseProcessClass bpc){
    }


    /**
     * StartStep: doApprove
     *
     * @param bpc
     * @throws
     */
    public void doApprove(BaseProcessClass bpc)  throws FeignException, CloneNotSupportedException {

        ParamUtil.setSessionAttr(bpc.request,"BackendInboxApprove",null);
        ParamUtil.setSessionAttr(bpc.request,"BackendInboxReturnFee",null);

        String[] taskList =  ParamUtil.getMaskedStrings(bpc.request, "taskId");
        String action =  ParamUtil.getString(bpc.request, "action");
        String successStatus = "";
        String successInfo = "Success";
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(!StringUtil.isEmpty(taskList)){
            for (String item:taskList
            ) {
                TaskDto taskDto = taskService.getTaskById(item);
                String correlationId;
                if(taskDto != null){
                    correlationId = taskDto.getRefNo();
                }else {
                    log.info(StringUtil.changeForLog("-------- task id "+ item+" is no task dto----"));
                    continue;
                }

                AppPremisesCorrelationDto appPremisesCorrelationDto = applicationViewService.getLastAppPremisesCorrelationDtoById(correlationId);
                appPremisesCorrelationDto.setOldCorrelationId(correlationId);

                String newCorrelationId = appPremisesCorrelationDto.getId();
                ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(newCorrelationId);
                applicationViewDto.setNewAppPremisesCorrelationDto(appPremisesCorrelationDto);
                ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
                List<String> app = (List<String>)ParamUtil.getSessionAttr(bpc.request,"BackendInboxApprove");
                if(app == null){
                    app = IaisCommonUtils.genNewArrayList();
                }
                app.add(applicationDto.getApplicationNo());
                ParamUtil.setSessionAttr(bpc.request,"BackendInboxApprove",(Serializable) app);
                String status = applicationDto.getStatus();
                if(("trigger").equals(action)){
                    routeToDMS(bpc,applicationViewDto,taskDto);
                    successStatus = ApplicationConsts.PROCESSING_DECISION_ROUTE_TO_DMS;

                }else if(RoleConsts.USER_ROLE_AO1.equals(loginContext.getCurRoleId())){
                    log.debug(StringUtil.changeForLog("the do rontingTaskToAO2 start ...."));
                    if(ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO.equals(status)){
                        try{
                            replay(bpc,applicationViewDto,taskDto);
                            successStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02;
                        }catch (Exception e){
                            log.info(e.getMessage(),e);
                        }

                    }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW.equals(status)){
                        successStatus = InspectionConstants.INSPECTION_STATUS_PENDING_JOB_CREATE_TASK_TO_LEADER;
                        inspectorAo1(bpc,applicationViewDto,taskDto);
                    }else{
                        successStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02;
                        routingTask(bpc, HcsaConsts.ROUTING_STAGE_AO2, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02, RoleConsts.USER_ROLE_AO2,applicationViewDto,taskDto);
                    }


                    log.debug(StringUtil.changeForLog("the do rontingTaskToAO2 end ...."));
                }else if(RoleConsts.USER_ROLE_AO2.equals(loginContext.getCurRoleId())){
                    log.debug(StringUtil.changeForLog("the do rontingTaskToAO3 start ...."));
                    if(ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO.equals(status)){
                        try{
                            replay(bpc,applicationViewDto,taskDto);
                        }catch (Exception e){
                            log.info(e.getMessage(),e);
                        }

                    }else{
                        routingTask(bpc, HcsaConsts.ROUTING_STAGE_AO3, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03, RoleConsts.USER_ROLE_AO3,applicationViewDto,taskDto);
                    }
                    log.debug(StringUtil.changeForLog("the do rontingTaskToAO3 end ...."));
                    successStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
                }else if(RoleConsts.USER_ROLE_AO3.equals(loginContext.getCurRoleId())){
                    //judge the final status is Approve or Reject.
                    AppPremisesRecommendationDto appPremisesRecommendationDto = applicationViewDto.getAppPremisesRecommendationDto();
                    if(appPremisesRecommendationDto!=null){
                        Integer recomInNumber =  appPremisesRecommendationDto.getRecomInNumber();
                        if(null != recomInNumber && recomInNumber == 0){
                            successStatus =  ApplicationConsts.APPLICATION_STATUS_REJECTED;
                        }else{
                            successStatus = ApplicationConsts.APPLICATION_STATUS_APPROVED;
                        }
                    }else{
                        successStatus = ApplicationConsts.APPLICATION_STATUS_APPROVED;
                    }
                    log.debug(StringUtil.changeForLog("the do approve start ...."));
                    routingTask(bpc,"",successStatus,"",applicationViewDto,taskDto);
                    log.debug(StringUtil.changeForLog("the do approve end ...."));
                    //send reject email
                    if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(successStatus)){
                        try{
                            rejectSendNotification(applicationViewDto);
                        }catch (Exception e){
                            log.error(StringUtil.changeForLog("send reject notification error"),e);
                        }
                    }

                }
            }
            //update commPools
            List<TaskDto> commPools = getCommPoolBygetUserId(loginContext.getUserId(),loginContext.getCurRoleId());

            if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02.equals(successStatus)){
                successInfo = "LOLEV_ACK009";
            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(successStatus)){
                //AO2 -> AO3
                successInfo = "LOLEV_ACK013";
            }else if(ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(successStatus)){
                //AO3 APPROVAL
                successInfo = "LOLEV_ACK020";
            }else if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(successStatus)){
                //AO3 REJECT
                successInfo = "LOLEV_ACK022";
            }else if(ApplicationConsts.PROCESSING_DECISION_ROUTE_TO_DMS.equals(successStatus)){
                //AO3 DMS
                successInfo = "LOLEV_ACK024";
            }else if(InspectionConstants.INSPECTION_STATUS_PENDING_JOB_CREATE_TASK_TO_LEADER.equals(successStatus)){
                //AO1 inspector draft email
                successInfo = "LOLEV_ACK035";
            }
            ParamUtil.setRequestAttr(bpc.request,"successInfo",successInfo);
        }

    }

    private void sendMessage(String subject, String licenseeId, String templateMessageByContent, HashMap<String, String> maskParams, String serviceId, String messageType){
        if(StringUtil.isEmpty(messageType)){
            messageType = MessageConstants.MESSAGE_TYPE_NOTIFICATION;
        }
        HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        InterMessageDto interMessageDto = new InterMessageDto();
        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
        interMessageDto.setSubject(subject);
        interMessageDto.setMessageType(messageType);
        String refNo = inboxMsgService.getMessageNo();
        interMessageDto.setRefNo(refNo);
        if(serviceDto != null){
            interMessageDto.setService_id(serviceDto.getSvcCode()+"@");
        }
        interMessageDto.setUserId(licenseeId);
        interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        interMessageDto.setMsgContent(templateMessageByContent);
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        interMessageDto.setMaskParams(maskParams);
        inboxMsgService.saveInterMessage(interMessageDto);
    }

    private void rejectSendNotification(ApplicationViewDto applicationViewDto) {
        String applicationNo = applicationViewDto.getApplicationDto().getApplicationNo();
        String appGrpId = applicationViewDto.getApplicationDto().getAppGrpId();
        String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
        Date date = new Date();
        String appDate = Formatter.formatDateTime(date, "dd/MM/yyyy");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String MohName = AppConsts.MOH_AGENCY_NAME;
        String applicationType = applicationDto.getApplicationType();
        String applicationTypeShow = MasterCodeUtil.getCodeDesc(applicationType);
        String emailAddress = "ecquaria@ecquaria.com";
        if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType)){
            renewalSendNotification(applicationTypeShow,applicationNo,appDate,MohName,applicationDto);
        }else if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType)){
            newAppSendNotification(applicationTypeShow,applicationNo,appDate,MohName,applicationDto);
        }
    }

    private void newAppSendNotification(String applicationTypeShow,String applicationNo,String appDate,String MohName,ApplicationDto applicationDto){
        log.info(StringUtil.changeForLog("send new application notification start"));
        //send email
        ApplicationGroupDto applicationGroupDto = applicationViewService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
        if(applicationGroupDto != null) {
            String groupLicenseeId = applicationGroupDto.getLicenseeId();
            log.info(StringUtil.changeForLog("send new application notification groupLicenseeId : " + groupLicenseeId));
            LicenseeDto licenseeDto = organizationMainClient.getLicenseeDtoById(groupLicenseeId).getEntity();
            if (licenseeDto != null) {
                String applicantName = licenseeDto.getName();
                log.info(StringUtil.changeForLog("send new application notification applicantName : " + applicantName));
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                map.put("ApplicantName", applicantName);
                map.put("applicationType", applicationTypeShow);
                map.put("applicationNumber", applicationNo);
                map.put("applicationDate", appDate);
                map.put("emailAddress", systemAddressOne);
                map.put("MOH_AGENCY_NAME", MohName);
                try {
                    String subject = "MOH HALP - Your "+ applicationTypeShow + ", "+ applicationNo +" is rejected ";
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_ID);
                    emailParam.setTemplateContent(map);
                    emailParam.setQueryCode(applicationNo);
                    emailParam.setReqRefNum(applicationNo);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                    emailParam.setRefId(applicationNo);
                    emailParam.setSubject(subject);
                    //send email
                    log.info(StringUtil.changeForLog("send new application email"));
                    notificationHelper.sendNotification(emailParam);
                    //send sms
                    EmailParam smsParam = new EmailParam();
                    smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_SMS_ID);
                    smsParam.setSubject(subject);
                    smsParam.setQueryCode(applicationNo);
                    smsParam.setReqRefNum(applicationNo);
                    smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                    smsParam.setRefId(applicationNo);
                    log.info(StringUtil.changeForLog("send new application sms"));
                    notificationHelper.sendNotification(smsParam);
                    //send message
                    EmailParam messageParam = new EmailParam();
                    messageParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_MESSAGE_ID);
                    messageParam.setTemplateContent(map);
                    messageParam.setQueryCode(applicationNo);
                    messageParam.setReqRefNum(applicationNo);
                    messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    messageParam.setRefId(applicationNo);
                    messageParam.setSubject(subject);
                    log.info(StringUtil.changeForLog("send new application message"));
                    notificationHelper.sendNotification(messageParam);
                    log.info(StringUtil.changeForLog("send new application notification end"));
                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private void renewalSendNotification(String applicationTypeShow,String applicationNo,String appDate,String MohName,ApplicationDto applicationDto){
        log.info(StringUtil.changeForLog("send renewal application notification start"));
        //send email
        ApplicationGroupDto applicationGroupDto = applicationViewService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
        if(applicationGroupDto != null){
            String groupLicenseeId = applicationGroupDto.getLicenseeId();
            log.info(StringUtil.changeForLog("send renewal application notification groupLicenseeId : " + groupLicenseeId));
            LicenseeDto licenseeDto = organizationMainClient.getLicenseeDtoById(groupLicenseeId).getEntity();
            if(licenseeDto != null){
                String applicantName = licenseeDto.getName();
                log.info(StringUtil.changeForLog("send renewal application notification applicantName : " + applicantName));
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                map.put("ApplicantName", applicantName);
                map.put("ApplicationType", applicationTypeShow);
                map.put("ApplicationNumber", applicationNo);
                map.put("ApplicationDate", appDate);
                map.put("emailAddress", systemAddressOne);
                map.put("MOH_AGENCY_NAME", MohName);
                try {
                    String subject = "MOH HALP - Your "+ applicationTypeShow + ", "+ applicationNo +" is rejected ";
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT);
                    emailParam.setTemplateContent(map);
                    emailParam.setQueryCode(applicationNo);
                    emailParam.setReqRefNum(applicationNo);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                    emailParam.setRefId(applicationNo);
                    emailParam.setSubject(subject);
                    //send email
                    log.info(StringUtil.changeForLog("send renewal application email"));
                    notificationHelper.sendNotification(emailParam);
                    //send sms
                    EmailParam smsParam = new EmailParam();
                    smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT_SMS);
                    smsParam.setSubject(subject);
                    smsParam.setQueryCode(applicationNo);
                    smsParam.setReqRefNum(applicationNo);
                    smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                    smsParam.setRefId(applicationNo);
                    log.info(StringUtil.changeForLog("send renewal application sms"));
                    notificationHelper.sendNotification(smsParam);
                    //send message
                    EmailParam messageParam = new EmailParam();
                    messageParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT_MESSAGE);
                    messageParam.setTemplateContent(map);
                    messageParam.setQueryCode(applicationNo);
                    messageParam.setReqRefNum(applicationNo);
                    messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    messageParam.setRefId(applicationNo);
                    messageParam.setSubject(subject);
                    log.info(StringUtil.changeForLog("send renewal application message"));
                    notificationHelper.sendNotification(messageParam);
                    log.info(StringUtil.changeForLog("send renewal application notification end"));
                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }
            }
        }
    }


    private void inspectorAo1(BaseProcessClass bpc, ApplicationViewDto applicationViewDto,TaskDto taskDto){
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING);
        applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusMainClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
        appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_JOB_CREATE_TASK_TO_LEADER);
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appInspectionStatusMainClient.update(appInspectionStatusDto);
        taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        taskService.updateTask(taskDto);

        createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT, taskDto, userId,"inspector ao1 approve in backend inbox",HcsaConsts.ROUTING_STAGE_POT);
        createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING, taskDto, userId,"",HcsaConsts.ROUTING_STAGE_POT);

        //update FE  application status.
        applicationViewService.updateFEApplicaiton(applicationViewDto.getApplicationDto());
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus, String decision,
                                                                         TaskDto taskDto, String userId, String remarks,String subStage) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(HcsaConsts.ROUTING_STAGE_AO1);
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

    /**
     * StartStep: replay
     *
     * @param bpc
     * @throws
     */
    private void replay(BaseProcessClass bpc,ApplicationViewDto applicationViewDto,TaskDto taskDto) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do replay start ...."));
        String nextStatus = ApplicationConsts.APPLICATION_STATUS_REPLY;
        String getHistoryStatus = applicationViewDto.getApplicationDto().getStatus();
        if(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(getHistoryStatus)){
            getHistoryStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
        }else if(ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(getHistoryStatus)){
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
        }
        log.info(StringUtil.changeForLog("----------- route back historyStatus : " + getHistoryStatus + "----------"));
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.getSecondRouteBackHistoryByAppNo(
                applicationViewDto.getApplicationDto().getApplicationNo(),getHistoryStatus);
        String wrkGrpId=appPremisesRoutingHistoryDto.getWrkGrpId();
        String roleId=appPremisesRoutingHistoryDto.getRoleId();
        String stageId=appPremisesRoutingHistoryDto.getStageId();
        String userId=appPremisesRoutingHistoryDto.getActionby();
        String subStageId = appPremisesRoutingHistoryDto.getSubStage();

        if(!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_ASO.equals(stageId)){
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING;
        }else if(!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_PSO.equals(stageId)){
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING;
        }else if(!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_INS.equals(stageId)){
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS;
        }else if(!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_AO1.equals(stageId)){
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01;
        }else if(!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_AO2.equals(stageId)){
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02;
        }else if(!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_AO3.equals(stageId)){
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
        }

        String routeHistoryId = appPremisesRoutingHistoryDto.getId();
        AppPremisesRoutingHistoryExtDto historyExtDto = appPremisesRoutingHistoryMainClient.getAppPremisesRoutingHistoryExtByHistoryAndComponentName(routeHistoryId, ApplicationConsts.APPLICATION_ROUTE_BACK_REVIEW).getEntity();
        if(historyExtDto == null){
            rollBack(bpc,applicationViewDto,stageId,nextStatus,roleId,wrkGrpId,userId,taskDto);
        }else{
            String componentValue = historyExtDto.getComponentValue();
            if("N".equals(componentValue)){
                List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtoList = applicationViewService.getStage(applicationViewDto.getApplicationDto().getServiceId(),
                        stageId,applicationViewDto.getApplicationDto().getApplicationType());
                if(hcsaSvcRoutingStageDtoList != null){
                    HcsaSvcRoutingStageDto nextStage = hcsaSvcRoutingStageDtoList.get(0);
                    String stageCode = nextStage.getStageCode();
                    String routeNextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02;
                    String nextStageId = HcsaConsts.ROUTING_STAGE_AO2;
                    if(RoleConsts.USER_ROLE_AO3.equals(stageCode)){
                        nextStageId = HcsaConsts.ROUTING_STAGE_AO3;
                        routeNextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
                    }
                    routingTask(bpc,nextStageId,routeNextStatus,stageCode,applicationViewDto,taskDto);
                }else{
                    log.error(StringUtil.changeForLog("RoutingStageDtoList is null"));
                }
            }else{
                rollBack(bpc,applicationViewDto,stageId,nextStatus,roleId,wrkGrpId,userId,taskDto);
            }
        }
        log.debug(StringUtil.changeForLog("the do replay end ...."));
    }

    private void routingTask(BaseProcessClass bpc,String stageId,String appStatus,String roleId ,ApplicationViewDto applicationViewDto,TaskDto taskDto) throws FeignException, CloneNotSupportedException {

        //get the user for this applicationNo
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        AppPremisesCorrelationDto newAppPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();
        String newCorrelationId = newAppPremisesCorrelationDto.getId();
        BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
        String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
        BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();
        List<String> applicationDtoIds = (List<String>) ParamUtil.getSessionAttr(bpc.request,"BackendInboxApprove");

        //judge the final status is Approve or Reject.
        AppPremisesRecommendationDto appPremisesRecommendationDto = applicationViewDto.getAppPremisesRecommendationDto();
        String applicationType = applicationDto.getApplicationType();
        if(appPremisesRecommendationDto!= null && ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus)){
            if(!ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){
                Integer recomInNumber =  appPremisesRecommendationDto.getRecomInNumber();
                if(null != recomInNumber && recomInNumber == 0){
                    appStatus =  ApplicationConsts.APPLICATION_STATUS_REJECTED;
                }
            }else{
                String recomDecision = appPremisesRecommendationDto.getRecomDecision();
                if("reject".equals(recomDecision)){
                    appStatus =  ApplicationConsts.APPLICATION_STATUS_REJECTED;
                }
            }
        }

        Map<String, String> returnFeeMap = (Map<String, String>) ParamUtil.getSessionAttr(bpc.request,"BackendInboxReturnFee");
        if(returnFeeMap == null){
            returnFeeMap = IaisCommonUtils.genNewHashMap();
        }
        returnFeeMap.put(applicationDto.getApplicationNo(),appStatus);
        ParamUtil.setSessionAttr(bpc.request,"BackendInboxReturnFee",(Serializable) returnFeeMap);
        //appeal save return fee
        if(ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus)){
            if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){
                String returnFee = appPremisesRecommendationDto.getRemarks();
                if(!StringUtil.isEmpty(returnFee)){
                    List<AppPremiseMiscDto> premiseMiscDtoList = applicationMainClient.getAppPremiseMiscDtoListByAppId(applicationDto.getId()).getEntity();
                    if(!IaisCommonUtils.isEmpty(premiseMiscDtoList)){
                        AppPremiseMiscDto appPremiseMiscDto = premiseMiscDtoList.get(0);
                        String oldAppId = appPremiseMiscDto.getRelateRecId();
                        ApplicationDto oldApplication = applicationMainClient.getApplicationById(oldAppId).getEntity();
                        if(oldApplication != null){
                            String oldApplicationNo = oldApplication.getApplicationNo();
                            if(!StringUtil.isEmpty(oldApplicationNo)){
                                AppReturnFeeDto appReturnFeeDto = new AppReturnFeeDto();
                                appReturnFeeDto.setApplicationNo(oldApplicationNo);
                                appReturnFeeDto.setReturnAmount(Double.parseDouble(returnFee));
                                appReturnFeeDto.setReturnType(ApplicationConsts.APPLICATION_RETURN_FEE_TYPE_APPEAL);
                                List<AppReturnFeeDto> saveReturnFeeDtos = IaisCommonUtils.genNewArrayList();
                                saveReturnFeeDtos.add(appReturnFeeDto);
                                broadcastApplicationDto.setReturnFeeDtos(saveReturnFeeDtos);
                                broadcastApplicationDto.setRollBackReturnFeeDtos(saveReturnFeeDtos);
                            }
                        }
                    }
                }
            }
        }
        //complated this task and create the history
        broadcastOrganizationDto.setRollBackComplateTask((TaskDto) CopyUtil.copyMutableObject(taskDto));
        taskDto =  completedTask(taskDto);
        broadcastOrganizationDto.setComplateTask(taskDto);
        String decision = null;
        String currentStatus = applicationDto.getStatus();
        if(ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO.equals(currentStatus)){
            decision = ApplicationConsts.PROCESSING_DECISION_REPLY;
        }

        if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01.equals(currentStatus) || ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02.equals(currentStatus)){
            if(HcsaConsts.ROUTING_STAGE_INS.equals(stageId)){
                decision = InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_INSPECTION_REPORT;
            }else{
                decision = ApplicationConsts.PROCESSING_DECISION_VERIFIED;
            }
        }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(currentStatus)){
            decision = ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL;
        }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW.equals(currentStatus)){
            decision = InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_INSPECTION_REPORT;
        }

        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                applicationDto.getStatus(),taskDto.getTaskKey(),null, taskDto.getWkGrpId(),null,null,decision,taskDto.getRoleId());
        broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
        //update application status
        broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto) CopyUtil.copyMutableObject(applicationDto));
        String oldStatus = applicationDto.getStatus();
        applicationDto.setStatus(appStatus);

        broadcastApplicationDto.setApplicationDto(applicationDto);
        if(!StringUtil.isEmpty(stageId)){
            log.debug(StringUtil.changeForLog("has next stageId :" + stageId));
            if(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(oldStatus)){
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto1 = appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryForCurrentStage(
                        applicationDto.getApplicationNo(),stageId
                );
                log.debug(StringUtil.changeForLog("The appId is-->;"+ applicationDto.getId()));
                log.debug(StringUtil.changeForLog("The stageId is-->;"+ stageId));
                if(appPremisesRoutingHistoryDto1 != null){
                    TaskDto newTaskDto = TaskUtil.getTaskDto(applicationDto.getApplicationNo(),stageId,TaskConsts.TASK_TYPE_MAIN_FLOW,
                            taskDto.getRefNo(),appPremisesRoutingHistoryDto1.getWrkGrpId(),
                            appPremisesRoutingHistoryDto1.getActionby(),new Date(),0,TaskConsts.TASK_PROCESS_URL_MAIN_FLOW,roleId,
                            IaisEGPHelper.getCurrentAuditTrailDto());
                    broadcastOrganizationDto.setCreateTask(newTaskDto);
                    //delete workgroup
                    BroadcastOrganizationDto broadcastOrganizationDto1 = broadcastService.getBroadcastOrganizationDto(
                            applicationDto.getApplicationNo(),AppConsts.DOMAIN_TEMPORARY);

                    WorkingGroupDto workingGroupDto = broadcastOrganizationDto1.getWorkingGroupDto();
                    broadcastOrganizationDto.setRollBackworkingGroupDto((WorkingGroupDto) CopyUtil.copyMutableObject(workingGroupDto));
                    workingGroupDto = changeStatusWrokGroup(workingGroupDto,AppConsts.COMMON_STATUS_DELETED);
                    broadcastOrganizationDto.setWorkingGroupDto(workingGroupDto);
                    List<UserGroupCorrelationDto> userGroupCorrelationDtos = broadcastOrganizationDto1.getUserGroupCorrelationDtoList();
                    List<UserGroupCorrelationDto> cloneUserGroupCorrelationDtos = IaisCommonUtils.genNewArrayList();
                    CopyUtil.copyMutableObjectList(userGroupCorrelationDtos,cloneUserGroupCorrelationDtos);
                    broadcastOrganizationDto.setRollBackUserGroupCorrelationDtoList(cloneUserGroupCorrelationDtos);
                    userGroupCorrelationDtos = changeStatusUserGroupCorrelationDtos(userGroupCorrelationDtos,AppConsts.COMMON_STATUS_DELETED);
                    broadcastOrganizationDto.setUserGroupCorrelationDtoList(userGroupCorrelationDtos);
                }else{
                    throw new IaisRuntimeException("This getAppPremisesCorrelationId can not get the broadcast -- >:"+applicationViewDto.getAppPremisesCorrelationId());
                }
            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(appStatus)){
                if(applicationDto.isFastTracking()){
                    TaskDto newTaskDto = taskService.getRoutingTask(applicationDto,stageId,roleId,newCorrelationId);
                    broadcastOrganizationDto.setCreateTask(newTaskDto);
                }
                List<ApplicationDto> applicationDtoList = applicationViewService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
                applicationDtoList = removeFastTracking(applicationDtoList);
                boolean isAllSubmit = applicationViewService.isOtherApplicaitonSubmit(applicationDtoList,applicationDtoIds,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);
                log.debug(StringUtil.changeForLog("isAllSubmit is " + isAllSubmit));
                if(isAllSubmit){
                    // send the task to Ao3
                    TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(applicationDtoList,
                            HcsaConsts.ROUTING_STAGE_AO3,roleId,IaisEGPHelper.getCurrentAuditTrailDto());
                    List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
                    List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
                    broadcastOrganizationDto.setOneSubmitTaskList(taskDtos);
                    broadcastApplicationDto.setOneSubmitTaskHistoryList(appPremisesRoutingHistoryDtos);
                }
            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(appStatus)){
                AppInspectionStatusDto appInspectionStatusDto = new AppInspectionStatusDto();
                appInspectionStatusDto.setAppPremCorreId(taskDto.getRefNo());
                appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
                broadcastApplicationDto.setAppInspectionStatusDto(appInspectionStatusDto);
                TaskDto newTaskDto = taskService.getRoutingTask(applicationDto,stageId,roleId,newCorrelationId);
                broadcastOrganizationDto.setCreateTask(newTaskDto);
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                        applicationDto.getStatus(),taskDto.getTaskKey(),null, taskDto.getWkGrpId(),null,null,null,taskDto.getRoleId());
                broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
            }else{
                TaskDto newTaskDto = taskService.getRoutingTask(applicationDto,stageId,roleId,newCorrelationId);
                broadcastOrganizationDto.setCreateTask(newTaskDto);
            }
            //add history for next stage start
            if(!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(appStatus)&&!ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(appStatus)){
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),stageId,null,
                        taskDto.getWkGrpId(),null,null,null,roleId);
                broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
            }
        }else{
            log.debug(StringUtil.changeForLog("not has next stageId :" + stageId));
            log.debug(StringUtil.changeForLog("do ao3 approve ----- "));
            List<ApplicationDto> applicationDtoList = applicationViewService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
            List<ApplicationDto> saveApplicationDtoList = IaisCommonUtils.genNewArrayList();
            CopyUtil.copyMutableObjectList(applicationDtoList,saveApplicationDtoList);
            applicationDtoList = removeFastTracking(applicationDtoList);
            boolean isAllSubmit = applicationViewService.isOtherApplicaitonSubmit(applicationDtoList,applicationDtoIds,
                    appStatus);
            log.debug(StringUtil.changeForLog("isAllSubmit is " + isAllSubmit));
            if(isAllSubmit || applicationDto.isFastTracking()){
                //update application Group status
                ApplicationGroupDto applicationGroupDto = applicationViewService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
                broadcastApplicationDto.setRollBackApplicationGroupDto((ApplicationGroupDto)CopyUtil.copyMutableObject(applicationGroupDto));
                applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
                applicationGroupDto.setAo3ApprovedDt(new Date());
                applicationGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                broadcastApplicationDto.setApplicationGroupDto(applicationGroupDto);
                if(isAllSubmit){
                    //update current application status in db search result
                    updateCurrentApplicationStatus(bpc,saveApplicationDtoList,licenseeId);

                    for (ApplicationDto viewitem:saveApplicationDtoList
                         ) {
                        log.info(StringUtil.changeForLog("****viewitem ***** " + viewitem.getApplicationNo()));
                    }
                    //get and set return fee
                    saveApplicationDtoList = hcsaConfigMainClient.returnFee(saveApplicationDtoList).getEntity();
                    //save return fee
                    saveRejectReturnFee(saveApplicationDtoList,broadcastApplicationDto);
                }
            }
        }
        //save the broadcast
        broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        String evenRefNum = String.valueOf(System.currentTimeMillis());
        broadcastOrganizationDto.setEventRefNo(evenRefNum);
        broadcastApplicationDto.setEventRefNo(evenRefNum);
        String submissionId = generateIdClient.getSeqId().getEntity();
        log.info(StringUtil.changeForLog(submissionId));
        broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,bpc.process,submissionId);
        broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,bpc.process,submissionId);
        //0062460 update FE  application status.
        applicationViewService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());
    }

    private void saveRejectReturnFee(List<ApplicationDto> applicationDtos,BroadcastApplicationDto broadcastApplicationDto){
        List<AppReturnFeeDto> saveReturnFeeDtos = IaisCommonUtils.genNewArrayList();
        //save return fee
        for(ApplicationDto applicationDto : applicationDtos){
            log.info(StringUtil.changeForLog("**** saveRejectReturnFee applicationDto ***** " + applicationDto.getApplicationNo()));
            log.info(StringUtil.changeForLog("**** saveRejectReturnFee applicationDto ***** " + applicationDto.getStatus()));
            if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(applicationDto.getStatus())){
                AppReturnFeeDto appReturnFeeDto = new AppReturnFeeDto();
                Double returnFee = applicationDto.getReturnFee();
                if(returnFee==null || returnFee == 0d){
                    continue;
                }
                appReturnFeeDto.setReturnAmount(returnFee);
                appReturnFeeDto.setApplicationNo(applicationDto.getApplicationNo());
                appReturnFeeDto.setReturnType(ApplicationConsts.APPLICATION_RETURN_FEE_REJECT);
                saveReturnFeeDtos.add(appReturnFeeDto);
                log.info(StringUtil.changeForLog("**** saveRejectReturnFee applicationDto ReturnFee***** " +applicationDto.getApplicationNo() + " and " + applicationDto.getReturnFee()));
//                applicationService.saveAppReturnFee(appReturnFeeDto);
            }
        }
        if(!IaisCommonUtils.isEmpty(saveReturnFeeDtos)){
            broadcastApplicationDto.setReturnFeeDtos(saveReturnFeeDtos);
            broadcastApplicationDto.setRollBackReturnFeeDtos(saveReturnFeeDtos);
        }
    }

    private ApplicationDto getCurrentApplicationDto(List<ApplicationDto> applicationDtos,String applicationId){
        ApplicationDto applicationDto = null;
        if(IaisCommonUtils.isEmpty(applicationDtos) || StringUtil.isEmpty(applicationId)){
            return null;
        }
        for(ApplicationDto app : applicationDtos){
            if(applicationId.equals(app.getId())){
                applicationDto = app;
                break;
            }
        }
        return applicationDto;
    }

    private List<ApplicationDto> removeFastTracking(List<ApplicationDto> applicationDtos){
        List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(applicationDtos)){
            for (ApplicationDto applicationDto : applicationDtos){
                if(!applicationDto.isFastTracking()){
                    result.add(applicationDto);
                }
            }
        }
        return  result;
    }

    private void updateCurrentApplicationStatus(BaseProcessClass bpc,List<ApplicationDto> applicationDtos,String licenseeId) {
        Map<String, String> returnFee = (Map<String, String>) ParamUtil.getSessionAttr(bpc.request, "BackendInboxReturnFee");
        if (!IaisCommonUtils.isEmpty(returnFee) && !IaisCommonUtils.isEmpty(applicationDtos)) {
            String entityType = "";
            LicenseeDto licenseeDto = organizationMainClient.getLicenseeDtoById(licenseeId).getEntity();
            if (licenseeDto != null) {
                LicenseeEntityDto licenseeEntityDto = licenseeDto.getLicenseeEntityDto();
                if (licenseeEntityDto != null) {
                    entityType = licenseeEntityDto.getEntityType();
                }
            }
            boolean isCharity = false;
            if (AcraConsts.ENTITY_TYPE_CHARITIES.equals(entityType)) {
                isCharity = true;
            } else {
                isCharity = false;
            }
            for (ApplicationDto applicationDto : applicationDtos) {
                log.info(StringUtil.changeForLog("****application return fee applicationDto***** " + applicationDto.getApplicationNo()));
                for (Map.Entry<String, String> entry : returnFee.entrySet()) {
                    log.info(StringUtil.changeForLog("****application return fee returnFee***** " + entry.getKey()));
                    if (entry.getKey().equals(applicationDto.getApplicationNo())) {
                        log.info(StringUtil.changeForLog("****application return fee***** " + entry.getKey() + " *****" + entry.getValue()));
                        applicationDto.setStatus(entry.getValue());
                        applicationDto.setIsCharity(isCharity);
                    }
                }
            }
        }
    }

    private void updateFeApplications(List<ApplicationDto> applications){
        try{
            applicationViewService.updateFEApplicaitons(applications);
        }catch (Exception e){
            log.error(StringUtil.changeForLog("update fe applications error"));
        }
    }

    private TaskDto completedTask(TaskDto taskDto){
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(remainDays(taskDto));
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskDto;
    }

    private int remainDays(TaskDto taskDto){
        int result = 0;
        //todo: wait count kpi
        // String  resultStr = DurationFormatUtils.formatPeriod(taskDto.getDateAssigned().getTime(),taskDto.getSlaDateCompleted().getTime(), "d");
        // log.debug(StringUtil.changeForLog("The resultStr is -->:")+resultStr);
        return  result;
    }

    private AppPremisesRoutingHistoryDto getAppPremisesRoutingHistory(String appNo, String appStatus,
                                                                      String stageId,String subStageId,String wrkGrpId, String internalRemarks,String externalRemarks,String processDecision,
                                                                      String roleId){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setSubStage(subStageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setExternalRemarks(externalRemarks);
        appPremisesRoutingHistoryDto.setProcessDecision(processDecision);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setWrkGrpId(wrkGrpId);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return appPremisesRoutingHistoryDto;
    }

    /**
     * StartStep: inspectionSupSearchQuery1
     *
     * @param bpc
     * @throws
     */
    public void searchQuery(BaseProcessClass bpc) throws ParseException {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchQuery1 start ...."));
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<TaskDto> commPools = getCommPoolBygetUserId(loginContext.getUserId(),loginContext.getCurRoleId());
        SearchParam searchParamGroup = getSearchParam(bpc.request,false);
        SearchParam searchParamAjax = new SearchParam(InspectionAppInGroupQueryDto.class.getName());
        searchParamAjax.setSort("APPLICATION_NO", SearchParam.ASCENDING);

        String application_no = ParamUtil.getString(bpc.request, "application_no");
        String application_type = ParamUtil.getString(bpc.request, "application_type");
        String application_status = ParamUtil.getString(bpc.request, "application_status");
        String hci_code = ParamUtil.getString(bpc.request, "hci_code");
        String hci_address = ParamUtil.getString(bpc.request, "hci_address");
        String hci_name = ParamUtil.getString(bpc.request, "hci_name");

        if(commPools != null && !commPools.isEmpty()){
            String inspectorValue = loginContext.getLoginId();
            StringBuilder sb = new StringBuilder("(");
            int i =0;
            for (TaskDto item: commPools) {
                sb.append(":itemKey").append(i).append(',');
                i++;
            }
            String inSql = sb.substring(0, sb.length() - 1) + ")";
            searchParamGroup.addParam("remises_corr_id_in", inSql);
            searchParamAjax.addParam("remises_corr_id_in", inSql);
            i = 0;
            for (TaskDto item: commPools) {
                searchParamGroup.addFilter("itemKey" + i,
                        item.getRefNo());
                searchParamAjax.addFilter("itemKey" + i,
                        item.getRefNo());
                i ++;
            }

            if(!StringUtil.isEmpty(application_no)){
                searchParamGroup.addFilter("application_no","%" +application_no +"%" ,true);
                searchParamAjax.addFilter("application_no", "%" +application_no +"%",true);
            }else{
                searchParamGroup.removeFilter("application_no");
                searchParamAjax.removeFilter("application_no");
            }

            if(!StringUtil.isEmpty(application_type)){
                searchParamGroup.addFilter("application_type", application_type,true);
                searchParamAjax.addFilter("application_type", application_type,true);
            }else{
                searchParamGroup.removeFilter("application_type");
                searchParamAjax.removeFilter("application_type");
            }
            if(!StringUtil.isEmpty(hci_code)){
                String trim=hci_code.trim();
                searchParamGroup.addFilter("hci_code", trim,true);
                searchParamAjax.addFilter("hci_code", trim,true);
            }else{
                searchParamGroup.removeFilter("hci_code");
                searchParamAjax.removeFilter("hci_code");
            }
            if(!StringUtil.isEmpty(application_status)){
                List<MasterCodeView> masterCodeViews = MasterCodeUtil.retrieveByCategory(APPSTATUSCATEID);
                String codeValue = MasterCodeUtil.getCodeDesc(application_status);
                int statusi =0;
                StringBuilder builder = new StringBuilder("(");
                for (MasterCodeView item:masterCodeViews
                ) {
                    if(codeValue.equals(item.getCodeValue())){
                        builder.append(":statusKey").append(statusi).append(',');
                        statusi++;
                    }
                }
                String appStatusSql = builder.substring(0, builder.length() - 1) + ")";
                searchParamGroup.addParam("application_status", appStatusSql);
                searchParamAjax.addParam("application_status", appStatusSql);
                statusi =0;
                for (MasterCodeView item:masterCodeViews
                ) {
                    if(codeValue.equals(item.getCodeValue())){
                        searchParamGroup.addFilter("statusKey" + statusi,
                                item.getCode());
                        searchParamAjax.addFilter("statusKey" + statusi,
                                item.getCode());
                        statusi++;
                    }
                }
                ParamUtil.setRequestAttr(bpc.request, "application_status", application_status);
            }else{
                searchParamGroup.removeFilter("application_status");
                searchParamAjax.removeFilter("application_status");
            }

            if(!StringUtil.isEmpty(hci_address)){
                searchParamGroup.addFilter("address", "%" +hci_address +"%",true);
                searchParamAjax.addFilter("address", "%" +hci_address +"%",true);
            }else{
                searchParamGroup.removeFilter("address");
                searchParamAjax.removeFilter("address");
            }

            if(!StringUtil.isEmpty(hci_name)){
                searchParamGroup.addFilter("hci_name", "%" +hci_name +"%",true);
                searchParamAjax.addFilter("hci_name", "%" +hci_name +"%",true);
            }else{
                searchParamGroup.removeFilter("hci_name");
                searchParamAjax.removeFilter("hci_name");
            }


            QueryHelp.setMainSql("inspectionQuery", "AppGroup",searchParamGroup);
            log.debug(StringUtil.changeForLog("searchResult3 searchParamGroup = "+JsonUtil.parseToJson(searchParamGroup)));
            SearchResult<InspectionAppGroupQueryDto> searchResult3 = inspectionService.searchInspectionBeAppGroup(searchParamGroup);
            List<InspectionAppGroupQueryDto> inspectionAppGroupQueryDtoList = searchResult3.getRows();
            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat newformat =  new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT);
            for (InspectionAppGroupQueryDto item:inspectionAppGroupQueryDtoList
            ) {
                long lt = format.parse(item.getSubmitDate()).getTime();
                Date date = new Date(lt);
                String newdate = newformat.format(date);
                item.setSubmitDate(newdate);
                if(item.getPaymentstatus() == null || item.getPaymentstatus().isEmpty()){
                    item.setPaymentstatus("N/A");
                }else{
                    item.setPaymentstatus(MasterCodeUtil.getCodeDesc(item.getPaymentstatus()));
                }
                item.setApplicationTypeCode(String.copyValueOf(item.getApplicationType().toCharArray()));
                item.setApplicationType(MasterCodeUtil.getCodeDesc(item.getApplicationType()));
            }

            ParamUtil.setRequestAttr(bpc.request, "supTaskSearchResult", searchResult3);
            ParamUtil.setSessionAttr(bpc.request, "searchParamAjax", searchParamAjax);
        }else{
            ParamUtil.setRequestAttr(bpc.request, "supTaskSearchResult", null);
        }
        Map<String,String> appNoUrl = IaisCommonUtils.genNewHashMap();

        Map<String,TaskDto> taskMap = IaisCommonUtils.genNewHashMap();


        Map<String,String> taskList = IaisCommonUtils.genNewHashMap();
        if(commPools != null && commPools.size() >0){
            for (TaskDto item:commPools
            ) {
                String taskId = MaskUtil.maskValue("taskId", item.getId());
                appNoUrl.put(item.getRefNo(), generateProcessUrl(item, bpc.request, taskId));
                taskList.put(item.getRefNo(), taskId);
                taskMap.put(item.getRefNo(), item);
            }
        }
        if(RoleConsts.USER_ROLE_AO1.equals(loginContext.getCurRoleId()) || RoleConsts.USER_ROLE_AO2.equals(loginContext.getCurRoleId()) || RoleConsts.USER_ROLE_AO3.equals(loginContext.getCurRoleId())){
            ParamUtil.setSessionAttr(bpc.request, "hastaskList",AppConsts.TRUE);
        }else{
            ParamUtil.setSessionAttr(bpc.request, "hastaskList",AppConsts.FALSE);
        }
        ParamUtil.setSessionAttr(bpc.request, "taskList",(Serializable) taskList);
        ParamUtil.setSessionAttr(bpc.request, "appNoUrl",(Serializable) appNoUrl);
        ParamUtil.setSessionAttr(bpc.request, "taskMap",(Serializable) taskMap);
        ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.TRUE);
        ParamUtil.setSessionAttr(bpc.request, "backendinboxSearchParam", searchParamGroup);

    }

    private String generateProcessUrl(TaskDto dto, HttpServletRequest request,String taskId) {
        StringBuilder sb = new StringBuilder("https://");
        String url = dto.getProcessUrl();
        sb.append(request.getServerName());
        if (!url.startsWith("/")) {
            sb.append('/');
        }
        sb.append(url);
        if (url.indexOf('?') >= 0) {
            sb.append('&');
        } else {
            sb.append('?');
        }
        sb.append("taskId=").append(taskId);
        return RedirectUtil.appendCsrfGuardToken(sb.toString(), request);
    }

    private WorkingGroupDto changeStatusWrokGroup(WorkingGroupDto workingGroupDto,String staus){
        if(workingGroupDto!= null && !StringUtil.isEmpty(staus)){
            workingGroupDto.setStatus(staus);
        }
        return workingGroupDto;
    }


    private List<UserGroupCorrelationDto> changeStatusUserGroupCorrelationDtos(List<UserGroupCorrelationDto> userGroupCorrelationDtos,String status){
        List<UserGroupCorrelationDto> result = IaisCommonUtils.genNewArrayList();
        if(userGroupCorrelationDtos!= null && userGroupCorrelationDtos.size() >0){
            for (UserGroupCorrelationDto userGroupCorrelationDto : userGroupCorrelationDtos){
                userGroupCorrelationDto.setStatus(status);
                result.add(userGroupCorrelationDto);
            }
        }
        return  result;
    }

    /**
     * StartStep: routeToDMS
     *
     * @param bpc
     * @throws
     */
    public void routeToDMS(BaseProcessClass bpc,ApplicationViewDto applicationViewDto,TaskDto taskDto) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do routeToDMS start ...."));
        ApplicationDto application = applicationViewDto.getApplicationDto();
        if(application != null){
            String appNo =  application.getApplicationNo();
            log.info(StringUtil.changeForLog("The appNo is -->:"+appNo));
            //HcsaConsts.ROUTING_STAGE_INS
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto =  appPremisesRoutingHistoryService.
                    getAppPremisesRoutingHistoryForCurrentStage(appNo,"14848A70-820B-EA11-BE7D-000C29F371DC",RoleConsts.USER_ROLE_INSPECTIOR,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT);
            if(appPremisesRoutingHistoryDto == null){
                appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.
                        getAppPremisesRoutingHistoryForCurrentStage(appNo,HcsaConsts.ROUTING_STAGE_ASO);
            }
            if(appPremisesRoutingHistoryDto != null){
                rollBack(bpc,applicationViewDto,appPremisesRoutingHistoryDto.getStageId(),ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS,
                        appPremisesRoutingHistoryDto.getRoleId(),appPremisesRoutingHistoryDto.getWrkGrpId(),appPremisesRoutingHistoryDto.getActionby(),taskDto);
            }else{
                log.error(StringUtil.changeForLog("can not get the appPremisesRoutingHistoryDto ..."));
            }
        }else{
            log.error(StringUtil.changeForLog("do not have the applicaiton"));
        }
        log.debug(StringUtil.changeForLog("the do routeToDMS end ...."));
    }


    private void rollBack(BaseProcessClass bpc, ApplicationViewDto applicationViewDto,String stageId,String appStatus,String roleId ,String wrkGpId,String userId, TaskDto taskDto) throws CloneNotSupportedException {
        //send internal route back email
        String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();

        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
        BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();

        //complated this task and create the history
        String refNo = taskDto.getRefNo();
        String subStageId = null;
        broadcastOrganizationDto.setRollBackComplateTask((TaskDto) CopyUtil.copyMutableObject(taskDto));
        taskDto =  completedTask(taskDto);
        broadcastOrganizationDto.setComplateTask(taskDto);
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        String processDecision = ParamUtil.getString(bpc.request,"nextStage");
        String nextStageReplys = ParamUtil.getString(bpc.request, "nextStageReplys");
        if(!StringUtil.isEmpty(nextStageReplys) && StringUtil.isEmpty(processDecision)){
            processDecision = nextStageReplys;
        }

        String routeBackReview = (String)ParamUtil.getSessionAttr(bpc.request,"routeBackReview");
        if("canRouteBackReview".equals(routeBackReview)){
            AppPremisesRoutingHistoryExtDto appPremisesRoutingHistoryExtDto = new AppPremisesRoutingHistoryExtDto();
            appPremisesRoutingHistoryExtDto.setComponentName(ApplicationConsts.APPLICATION_ROUTE_BACK_REVIEW);
            String[] routeBackReviews =  ParamUtil.getStrings(bpc.request,"routeBackReview");
            if(routeBackReviews!=null){
                appPremisesRoutingHistoryExtDto.setComponentValue("Y");
            }else{
                appPremisesRoutingHistoryExtDto.setComponentValue("N");
                //route back and route task processing
                processDecision = ApplicationConsts.PROCESSING_DECISION_ROUTE_BACK_AND_ROUTE_TASK;
            }
            broadcastApplicationDto.setNewTaskHistoryExt(appPremisesRoutingHistoryExtDto);
        }

        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                applicationDto.getStatus(),taskDto.getTaskKey(),null, taskDto.getWkGrpId(),internalRemarks,null,processDecision,taskDto.getRoleId());
        broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
        //update application status
        broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto) CopyUtil.copyMutableObject(applicationDto));
        applicationDto.setStatus(appStatus);
        broadcastApplicationDto.setApplicationDto(applicationDto);
        String taskType = TaskConsts.TASK_TYPE_MAIN_FLOW;
        String TaskUrl = TaskConsts.TASK_PROCESS_URL_MAIN_FLOW;
        if(HcsaConsts.ROUTING_STAGE_INS.equals(stageId) && !ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS.equals(appStatus)){
            taskType = TaskConsts.TASK_TYPE_INSPECTION;
            if(RoleConsts.USER_ROLE_AO1.equals(roleId)){
                TaskUrl = TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT_REVIEW_AO1;
            }else if((!RoleConsts.USER_ROLE_AO2.equals(roleId)) &&
                    (!RoleConsts.USER_ROLE_AO3.equals(roleId)) &&
                    (!RoleConsts.USER_ROLE_ASO.equals(roleId)) &&
                    (!RoleConsts.USER_ROLE_PSO.equals(roleId))
            ){
                TaskUrl = TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT;
            }
            subStageId = HcsaConsts.ROUTING_STAGE_POT;
            //update inspector status
            updateInspectionStatus(applicationViewDto.getAppPremisesCorrelationId(),InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
        }
        //reply inspector
        if(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS.equals(appStatus)){
            List<TaskDto> taskDtos = organizationMainClient.getTaskByAppNoStatus(applicationDto.getApplicationNo(), TaskConsts.TASK_STATUS_COMPLETED, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION).getEntity();
            taskType = taskDtos.get(0).getTaskType();
            TaskUrl = TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION;
            subStageId = HcsaConsts.ROUTING_STAGE_PRE;
            //update inspector status
            updateInspectionStatus(applicationViewDto.getAppPremisesCorrelationId(),InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
        }
        //DMS go to main flow
        if(ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(appStatus)){
            taskType = TaskConsts.TASK_TYPE_MAIN_FLOW;
            TaskUrl = TaskConsts.TASK_PROCESS_URL_MAIN_FLOW;
        }

        TaskDto newTaskDto = TaskUtil.getTaskDto(applicationDto.getApplicationNo(),stageId,taskType,
                taskDto.getRefNo(),wrkGpId, userId,new Date(),0,TaskUrl,roleId,
                IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastOrganizationDto.setCreateTask(newTaskDto);

        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),stageId,subStageId,
                taskDto.getWkGrpId(),null,null,null,roleId);
        broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);

        //save the broadcast
        broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        String evenRefNum = String.valueOf(System.currentTimeMillis());
        broadcastOrganizationDto.setEventRefNo(evenRefNum);
        broadcastApplicationDto.setEventRefNo(evenRefNum);
        String submissionId = generateIdClient.getSeqId().getEntity();
        log.info(StringUtil.changeForLog(submissionId));
        broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,bpc.process,submissionId);
        broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,bpc.process,submissionId);

        //0062460 update FE  application status.
        applicationViewService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());
    }

    private void updateInspectionStatus(String appPremisesCorrelationId, String status) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusMainClient.getAppInspectionStatusByPremId(appPremisesCorrelationId).getEntity();
        if (appInspectionStatusDto != null) {
            appInspectionStatusDto.setStatus(status);
            appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusMainClient.update(appInspectionStatusDto);
        }
    }


}
