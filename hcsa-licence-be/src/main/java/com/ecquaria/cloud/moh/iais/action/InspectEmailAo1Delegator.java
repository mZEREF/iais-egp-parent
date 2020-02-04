package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocNcCheckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.CheckListVadlidateDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.validation.InspectionCheckListValidation;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.sz.commons.util.MsgUtil;
import com.esotericsoftware.minlog.Log;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ValidateEmailDelegator
 *
 * @author junyu
 * @date 2019/12/3
 */
@Delegator("validateEmailDelegator")
@Slf4j
public class InspectEmailAo1Delegator {
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    InspectionService inspectionService;
    @Autowired
    private TaskService taskService;
    @Autowired
    InsRepService insRepService;
    @Autowired
    ApplicationViewService applicationViewService;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    InsepctionNcCheckListService insepctionNcCheckListService;
    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;
    @Autowired
    AppInspectionStatusClient appInspectionStatusClient;
    @Autowired
    FillupChklistService fillupChklistService;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    ApplicationService applicationService;
    private static final String ADCHK_DTO="adchklDto";
    private static final String TASK_DTO="taskDto";
    private static final String APP_VIEW_DTO="applicationViewDto";
    private static final String COM_DTO="commonDto";
    private static final String MSG_CON="messageContent";
    private static final String EMAIL_VIEW="emailView";
    private static final String INS_EMAIL_DTO="insEmailDto";
    private static final String AC_DTO="acDto";
    private static final String SER_LIST_DTO= "serListDto";
    private static final String TD="</td><td>";
    private static final String SUBJECT="subject";

    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");


        Log.info("=======>>>>>initStep>>>>>>>>>>>>>>>>initRequest");
        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Config");
        AccessUtil.initLoginUserInfo(bpc.request);
        HttpServletRequest request = bpc.request;
        String taskId = ParamUtil.getRequestString(request,"taskId");
        if (StringUtil.isEmpty(taskId)) {
            taskId = "06528289-4246-EA11-BE7F-000C29F371DC";
    }
        TaskDto  taskDto = fillupChklistService.getTaskDtoById(taskId);
        String appPremCorrId = taskDto.getRefNo();
        List<InspectionFillCheckListDto> cDtoList = fillupChklistService.getInspectionFillCheckListDtoListForReview(taskId,"service");
        List<InspectionFillCheckListDto> commonList = fillupChklistService.getInspectionFillCheckListDtoListForReview(taskId,"common");
        InspectionFillCheckListDto commonDto = null;
        if(commonList!=null && !commonList.isEmpty()){
            commonDto = commonList.get(0);
        }
        InspectionFDtosDto serListDto = new InspectionFDtosDto();
        serListDto.setFdtoList(cDtoList);
        fillupChklistService.getTcuInfo(serListDto,appPremCorrId);
        AdCheckListShowDto adchklDto =insepctionNcCheckListService.getAdhocCheckListDto(appPremCorrId);
        ApplicationViewDto appViewDto = fillupChklistService.getAppViewDto(taskId);
        ParamUtil.setSessionAttr(request,ADCHK_DTO,adchklDto);
        ParamUtil.setSessionAttr(request,TASK_DTO,taskDto);
        ParamUtil.setSessionAttr(request,"applicationViewDto",appViewDto);
        ParamUtil.setSessionAttr(request,ADCHK_DTO,adchklDto);
        ParamUtil.setSessionAttr(request,COM_DTO,commonDto);
        ParamUtil.setSessionAttr(request,SER_LIST_DTO,serListDto);
        ParamUtil.setSessionAttr(request,MSG_CON, null);
        ParamUtil.setSessionAttr(request,APP_VIEW_DTO,appViewDto);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, null);
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, EMAIL_VIEW);
    }


    public void prepareData(BaseProcessClass bpc) {
        log.info("=======>>>>>prepareData>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, EMAIL_VIEW);

    }

    public CheckListVadlidateDto getValueFromPage(HttpServletRequest request) {
        CheckListVadlidateDto dto = new CheckListVadlidateDto();
        getDataFromPage(request);
        getCommonDataFromPage(request);
        getAdhocDtoFromPage(request);
        return dto;
    }
    public void emailSubmitStep(BaseProcessClass bpc){
        log.info("=======>>>>>emailSubmitStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        InspectionEmailTemplateDto inspectionEmailTemplateDto = (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(bpc.request,INS_EMAIL_DTO);
        String content=ParamUtil.getString(request,MSG_CON);
        ParamUtil.setSessionAttr(request,MSG_CON,content);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);
    }
    public void doProcessing(BaseProcessClass bpc){
        log.info("=======>>>>>doProcessing>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
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
        ParamUtil.setSessionAttr(request,SUBJECT, subject);
        ParamUtil.setSessionAttr(request,MSG_CON, content);


    }

    public void sendEmail(BaseProcessClass bpc) throws FeignException {

        log.info("=======>>>>>sendEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        ApplicationViewDto applicationViewDto= (ApplicationViewDto) ParamUtil.getSessionAttr(request,APP_VIEW_DTO);
        TaskDto taskDto= (TaskDto) ParamUtil.getSessionAttr(request,TASK_DTO);
        String serviceId=applicationViewDto.getApplicationDto().getServiceId();
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"send".equals(currentAction)){
            return;
        }
        String decision=ParamUtil.getString(request,"decision");
        if(decision.equals("Select")){decision=InspectionConstants.PROCESS_DECI_SENDS_EMAIL_APPLICANT;}

        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
        inspectionEmailTemplateDto.setSubject(ParamUtil.getString(request,SUBJECT));
        inspectionEmailTemplateDto.setMessageContent(ParamUtil.getString(request,MSG_CON));
        inspectionEmailTemplateDto.setRemarks(ParamUtil.getString(request, IntranetUserConstant.INTRANET_REMARKS));
        if (inspectionEmailTemplateDto.getSubject().isEmpty()){
            Map<String,String> errorMap = new HashMap<>();
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errorMap);
            ParamUtil.setRequestAttr(request,DemoConstants.ISVALID,"N");
        }
        if (inspectionEmailTemplateDto.getMessageContent().isEmpty()){
            Map<String,String> errorMap = new HashMap<>();
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errorMap);
            ParamUtil.setRequestAttr(request,DemoConstants.ISVALID,"N");
        }
        if (decision.equals(InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT)){
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
            AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
            appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_REVIEW_CHECKLIST_EMAIL);
            appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusClient.update(appInspectionStatusDto);
            taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_AO1);
            completedTask(taskDto);
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT, taskDto,HcsaConsts.ROUTING_STAGE_POT,userId,inspectionEmailTemplateDto.getRemarks());

            boolean flag=true;
            List<ApplicationDto> applicationDtos= inspEmailService.getApplicationDtosByCorreId(applicationViewDto.getAppPremisesCorrelationId());
            for (ApplicationDto appDto:applicationDtos
            ) {
                if(!appDto.getStatus().equals(ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING)){
                    flag=false;
                }
            }
            if(flag){

                HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
                hcsaSvcStageWorkingGroupDto.setType(applicationViewDto.getApplicationDto().getApplicationType());
                hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
                hcsaSvcStageWorkingGroupDto.setOrder(3);
                TaskDto taskDto1=new TaskDto();
                taskDto1.setRefNo(taskDto.getRefNo());
                taskDto1.setTaskType(taskDto.getTaskType());
                taskDto1.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
                taskDto1.setRoleId(RoleConsts.USER_ROLE_INSPECTION_LEAD);
                taskDto1.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_MERGE_NCEMAIL);
                taskDto1.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());
                taskDto1.setUserId(organizationClient.getInspectionLead(taskDto1.getWkGrpId()).getEntity().get(0));
                List<TaskDto> taskDtos = prepareTaskList(taskDto1,hcsaSvcStageWorkingGroupDto);
                taskService.createTasks(taskDtos);
                createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING, InspectionConstants.PROCESS_DECI_SENDS_EMAIL_APPLICANT,taskDto1,HcsaConsts.ROUTING_STAGE_POT,taskDto1.getUserId(),inspectionEmailTemplateDto.getRemarks());
            }
        }
        else {
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
            AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
            appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_CHECKLIST_VERIFY);
            appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusClient.update(appInspectionStatusDto);
            taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_AO1);
            completedTask(taskDto);
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT, taskDto,HcsaConsts.ROUTING_STAGE_POT,userId,inspectionEmailTemplateDto.getRemarks());


            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryForCurrentStage(applicationViewDto.getApplicationDto().getId(),HcsaConsts.ROUTING_STAGE_INS);
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setType(applicationViewDto.getApplicationDto().getApplicationType());
            hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
            hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
            hcsaSvcStageWorkingGroupDto.setOrder(1);
            TaskDto taskDto1=new TaskDto();
            taskDto1.setRefNo(taskDto.getRefNo());
            taskDto1.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            taskDto1.setUserId(appPremisesRoutingHistoryDto.getActionby());
            taskDto1.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());
            taskDto1.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REVISE_NCEMAIL);
            taskDto1.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
            taskDto1.setTaskType(taskDto.getTaskType());
            List<TaskDto> taskDtos = prepareTaskList(taskDto1,hcsaSvcStageWorkingGroupDto);
            taskService.createTasks(taskDtos);
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER, InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT,taskDto1,HcsaConsts.ROUTING_STAGE_POT,taskDto1.getUserId(),inspectionEmailTemplateDto.getRemarks());


        }
        inspEmailService.updateEmailDraft(inspectionEmailTemplateDto);

        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);

    }
    public void doRecallEmail(BaseProcessClass bpc) {
        log.info("=======>>>>>doRecallEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, EMAIL_VIEW);
    }

    public void preCheckList(BaseProcessClass bpc) {
        log.info("=======>>>>>preCheckList>>>>>>>>>>>>>>>>emailRequest");

    }
    public void checkListNext(BaseProcessClass bpc)  {
        log.info("=======>>>>>checkListNext>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        InspectionFDtosDto serListDto = getDataFromPage(request);
        InspectionFillCheckListDto commonDto= getCommonDataFromPage(request);
        AdCheckListShowDto adchklDto = getAdhocDtoFromPage(request);
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(request,TASK_DTO);
        ParamUtil.setSessionAttr(request,SER_LIST_DTO,serListDto);
        ParamUtil.setSessionAttr(request,ADCHK_DTO,adchklDto);
        ParamUtil.setSessionAttr(request,COM_DTO,commonDto);
        List<NcAnswerDto> ncDtoList = insepctionNcCheckListService.getNcAnswerDtoList(taskDto.getRefNo());
        InspectionCheckListValidation inspectionCheckListValidation = new InspectionCheckListValidation();
        Map<String, String> errMap = inspectionCheckListValidation.validate(request);
        ParamUtil.setSessionAttr(request,AC_DTO, (Serializable) ncDtoList);
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, "isValid", "N");
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errMap));
        }else{
            ParamUtil.setRequestAttr(request, "isValid", "Y");
            String saveFlag = ParamUtil.getString(request,"saveflag");
            if(!StringUtil.isEmpty(saveFlag)){
                insepctionNcCheckListService.submit(commonDto,adchklDto,serListDto,taskDto.getRefNo());
            }
        }
    }
    public void preEmailView(BaseProcessClass bpc)  {
        log.info("=======>>>>>preEmailView>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, TASK_DTO);
        String correlationId = taskDto.getRefNo();
        InspectionEmailTemplateDto inspectionEmailTemplateDto= inspEmailService.getInsertEmail(correlationId);
        ParamUtil.setSessionAttr(request,"draftEmailId",inspectionEmailTemplateDto.getId());
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);
    }
    public void preProcess(BaseProcessClass bpc)  {
        log.info("=======>>>>>preEmailView>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, TASK_DTO);
        String correlationId = taskDto.getRefNo();
        String appPremCorrId=correlationId;
        InspectionEmailTemplateDto inspectionEmailTemplateDto= inspEmailService.getInsertEmail(appPremCorrId);
        ApplicationViewDto applicationViewDto = inspEmailService.getAppViewByCorrelationId(correlationId);
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryDtosByAppId(applicationViewDto.getApplicationDto().getId());
        AppPremisesRoutingHistoryDto appPremisesRoutingHisDto= appPremisesRoutingHistoryDtos.get(0);
        String upDt=appPremisesRoutingHistoryDtos.get(0).getUpdatedDt();
        for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto1:appPremisesRoutingHistoryDtos){
            if(appPremisesRoutingHistoryDto1.getUpdatedDt().compareTo(upDt)>0){
                appPremisesRoutingHisDto=appPremisesRoutingHistoryDto1;
            }
            upDt=appPremisesRoutingHistoryDto1.getUpdatedDt();
            if(!StringUtil.isEmpty(appPremisesRoutingHistoryDto1.getWrkGrpId())) {
                appPremisesRoutingHistoryDto1.setWrkGrpId(applicationViewService.getWrkGrpName(appPremisesRoutingHistoryDto1.getWrkGrpId()));
            }
            if(StringUtil.isEmpty(appPremisesRoutingHistoryDto1.getActionby())){
                appPremisesRoutingHistoryDto1.setActionby("-");
            }
            else {
                appPremisesRoutingHistoryDto1.setActionby(applicationViewService.getUserNameById(new ArrayList<String>(Collections.singleton(appPremisesRoutingHistoryDto1.getActionby()))).get(0).getDisplayName());
            }
            if(StringUtil.isEmpty(appPremisesRoutingHistoryDto1.getAppStatus())){
                appPremisesRoutingHistoryDto1.setAppStatus("-");
            }else {
                appPremisesRoutingHistoryDto1.setAppStatus(MasterCodeUtil.retrieveOptionsByCodes(new String[]{appPremisesRoutingHistoryDto1.getAppStatus()}).get(0).getText());
            }
        }
        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT,InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT});
        String content= (String) ParamUtil.getSessionAttr(request,MSG_CON);
        if(content!=null){
            inspectionEmailTemplateDto.setMessageContent(content);
        }
        inspectionEmailTemplateDto.setAppStatus(appPremisesRoutingHisDto.getAppStatus());
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setRequestAttr(request,"appPremisesRoutingHistoryDtos", appPremisesRoutingHistoryDtos);
        ParamUtil.setSessionAttr(request,"draftEmailId",inspectionEmailTemplateDto.getId());
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);
    }

    public void emailView(BaseProcessClass bpc) {
        log.info("=======>>>>>emailView>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, EMAIL_VIEW);
    }


    public InspectionFillCheckListDto getCommonDataFromPage(HttpServletRequest request){
        InspectionFillCheckListDto cDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,COM_DTO);
        List<InspectionCheckQuestionDto> checkListDtoList = cDto.getCheckList();
        for(InspectionCheckQuestionDto temp:checkListDtoList){
            String answer = ParamUtil.getString(request,temp.getSectionName()+temp.getItemId()+"comrad");
            String remark = ParamUtil.getString(request,temp.getSectionName()+temp.getItemId()+"comremark");
            String rectified = ParamUtil.getString(request,temp.getSectionName()+temp.getItemId()+"comrec");
            temp.setRectified(!StringUtil.isEmpty(rectified)&&"No".equals(answer));
            temp.setChkanswer(answer);
            temp.setRemark(remark);
        }
        fillupChklistService.fillInspectionFillCheckListDto(cDto);
        return cDto;
    }

    public InspectionFDtosDto getDataFromPage(HttpServletRequest request){
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SER_LIST_DTO);
        if(serListDto.getFdtoList()!=null &&!serListDto.getFdtoList().isEmpty()){
            for(InspectionFillCheckListDto fdto:serListDto.getFdtoList()){
                if(fdto!=null&&fdto.getCheckList()!=null&&!fdto.getCheckList().isEmpty()){
                    List<InspectionCheckQuestionDto> checkListDtoList = fdto.getCheckList();
                    for(InspectionCheckQuestionDto temp:checkListDtoList){
                        String answer = ParamUtil.getString(request,fdto.getSvcCode()+temp.getSectionName()+temp.getItemId()+"rad");
                        String remark = ParamUtil.getString(request,fdto.getSvcCode()+temp.getSectionName()+temp.getItemId()+"remark");
                        String rectified = ParamUtil.getString(request,fdto.getSvcCode()+temp.getSectionName()+temp.getItemId()+"rec");
                        temp.setRectified(!StringUtil.isEmpty(rectified)&&"No".equals(answer));
                        temp.setChkanswer(answer);
                        temp.setRemark(remark);
                    }
                    fillupChklistService.fillInspectionFillCheckListDto(fdto);
                }
            }
        }
        String tcu = ParamUtil.getString(request,"tuc");
        String bestpractice = ParamUtil.getString(request,"bestpractice");
        String tcuremark = ParamUtil.getString(request,"tcuRemark");
        serListDto.setTcuRemark(tcuremark);
        serListDto.setTuc(tcu);
        serListDto.setBestPractice(bestpractice);
        return serListDto;
    }

    public AdCheckListShowDto getAdhocDtoFromPage(HttpServletRequest request){
        AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,ADCHK_DTO);
        List<AdhocNcCheckItemDto> itemDtoList = showDto.getAdItemList();
        if(itemDtoList!=null && !itemDtoList.isEmpty()){
            for(AdhocNcCheckItemDto temp:itemDtoList){
                String answer = ParamUtil.getString(request,temp.getId()+"adhocrad");
                String remark = ParamUtil.getString(request,temp.getId()+"adhocremark");
                String rec = ParamUtil.getString(request,temp.getId()+"adhocrec");
                temp.setAdAnswer(answer);
                temp.setRemark(remark);
                temp.setRectified(!StringUtil.isEmpty(rec)&&"No".equals(answer));
            }
        }
        showDto.setAdItemList(itemDtoList);
        return showDto;
    }

    @GetMapping(value = "/reload-nc-email")
    public @ResponseBody
    String reloadNcEmail(HttpServletRequest request) throws IOException, TemplateException {
        String templateId="08BDA324-5D13-EA11-BE78-000C29D29DB0";
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(request, TASK_DTO);
        String correlationId = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = inspEmailService.getAppViewByCorrelationId(correlationId);
        String appNo=applicationViewDto.getApplicationDto().getApplicationNo();
        String licenseeId=inspEmailService.getAppInsRepDto(appNo).getLicenseeId();
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
        List<NcAnswerDto> ncAnswerDtos=insepctionNcCheckListService.getNcAnswerDtoList(appPremCorrId);
        AppPremisesRecommendationDto appPreRecommentdationDto =insepctionNcCheckListService.getAppRecomDtoByAppCorrId(appPremCorrId,InspectionConstants.RECOM_TYPE_TCU);
        inspectionEmailTemplateDto.setBestPractices(appPreRecommentdationDto.getBestPractice());

        Map<String,Object> map=new HashMap<>();
        map.put("APPLICANT_NAME",StringUtil.viewHtml(inspectionEmailTemplateDto.getApplicantName()));
        map.put("APPLICATION_NUMBER",StringUtil.viewHtml(inspectionEmailTemplateDto.getApplicationNumber()));
        map.put("HCI_CODE",StringUtil.viewHtml(inspectionEmailTemplateDto.getHciCode()));
        map.put("HCI_NAME",StringUtil.viewHtml(inspectionEmailTemplateDto.getHciNameOrAddress()));
        map.put("SERVICE_NAME",StringUtil.viewHtml(inspectionEmailTemplateDto.getServiceName()));
        if(!ncAnswerDtos.isEmpty()){
            StringBuilder stringBuilder=new StringBuilder();
            int i=0;
            for (NcAnswerDto ncAnswerDto:ncAnswerDtos
            ) {
                stringBuilder.append("<tr><td>"+ ++i);
                stringBuilder.append(TD+StringUtil.viewHtml(ncAnswerDto.getItemQuestion()));
                stringBuilder.append(TD+StringUtil.viewHtml(ncAnswerDto.getClause()));
                stringBuilder.append(TD+StringUtil.viewHtml(ncAnswerDto.getRemark()));
                stringBuilder.append("</td></tr>");
            }
            map.put("NC_DETAILS",stringBuilder.toString());
        }
        if(inspectionEmailTemplateDto.getBestPractices()!=null){
            map.put("BEST_PRACTICE",inspectionEmailTemplateDto.getBestPractices());
        }
        map.put("MOH_NAME", AppConsts.MOH_AGENCY_NAME);
        String mesContext= MsgUtil.getTemplateMessageByContent(inspectionEmailTemplateDto.getMessageContent(),map);
        inspectionEmailTemplateDto.setMessageContent(mesContext);
        String draftEmailId= (String) ParamUtil.getSessionAttr(request,"draftEmailId");
        inspectionEmailTemplateDto.setId(draftEmailId);
        inspEmailService.updateEmailDraft(inspectionEmailTemplateDto);
        return mesContext;
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,String decision,
                                                                         TaskDto taskDto,String subStage,String userId ,String remarks) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(taskDto.getTaskKey());
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
        List<TaskDto> list = new ArrayList<>();
        List<HcsaSvcStageWorkingGroupDto> listhcsaSvcStageWorkingGroupDto = hcsaConfigClient.getSvcWorkGroup(hcsaSvcStageWorkingGroupDto).getEntity();
        String schemeType = listhcsaSvcStageWorkingGroupDto.get(0).getSchemeType();
        Integer count = listhcsaSvcStageWorkingGroupDto.get(0).getCount();
        taskDto.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());

        taskDto.setId(null);
        taskDto.setDateAssigned(new Date());
        taskDto.setSlaDateCompleted(null);
        taskDto.setSlaRemainInDays(null);
        taskDto.setScore(count);
        taskDto.setSlaAlertInDays(2);
        taskDto.setPriority(0);
        taskDto.setSlaInDays(5);
        //taskDto.setTaskType(schemeType);
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
        String resultStr = DurationFormatUtils.formatPeriod(taskDto.getDateAssigned().getTime(), System.currentTimeMillis(), "d");
        log.debug(StringUtil.changeForLog("The resultStr is -->:") + resultStr);
        return result;
    }
}
