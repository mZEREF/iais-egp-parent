package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageCodeKey;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSupDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MessageTemplateUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.BroadcastService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.LicenseeService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.cloud.moh.iais.validation.HcsaApplicationViewValidate;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * HcsaApplicationDelegator
 *
 * @author suocheng
 * @date 10/17/2019
 */
@Delegator("hcsaApplicationDelegator")
@Slf4j
public class HcsaApplicationDelegator {
    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationGroupService applicationGroupService;

    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;

    @Autowired
    private BroadcastService broadcastService;

    @Autowired
    private InboxMsgService inboxMsgService;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private InsRepService insRepService;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private LicenseeService licenseeService;

    @Autowired
    private GenerateIdClient generateIdClient;

    @Autowired
    private EmailClient emailClient;

    @Autowired
    private FillUpCheckListGetAppClient uploadFileClient;

    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do cleanSession start ...."));
        AuditTrailHelper.auditFunction("hcsa-licence", "hcsa licence");
        ParamUtil.setSessionAttr(bpc.request,"taskDto",null);
        ParamUtil.setSessionAttr(bpc.request,"applicationViewDto",null);
        ParamUtil.setSessionAttr(bpc.request,"isSaveRfiSelect",null);
        ParamUtil.setSessionAttr(bpc.request, "nextStages", null);
        ParamUtil.setSessionAttr(bpc.request, "nextStageReply", null);
        log.debug(StringUtil.changeForLog("the do cleanSession end ...."));
    }

    /**
     * StartStep: prepareData
     *
     * @param bpc
     * @throws
     */
    public void prepareData(BaseProcessClass bpc) throws Exception{
        log.debug(StringUtil.changeForLog("the do prepareData start ..."));
        //get the task
       String  taskId = ParamUtil.getString(bpc.request,"taskId");
       if(taskId != null){
           ParamUtil.setRequestAttr(bpc.request,"taskId",taskId);
       }else{
           taskId = (String)ParamUtil.getRequestAttr(bpc.request,"taskId");
           ParamUtil.setRequestAttr(bpc.request,"taskId",taskId);
       }

       TaskDto taskDto = taskService.getTaskById(taskId);
        String correlationId = "";
       if(taskDto != null){
           correlationId = taskDto.getRefNo();
       }
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationViewService.getLastAppPremisesCorrelationDtoById(correlationId);
        appPremisesCorrelationDto.setOldCorrelationId(correlationId);
        String newCorrelationId = appPremisesCorrelationDto.getId();
        ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(newCorrelationId);
        applicationViewDto.setNewAppPremisesCorrelationDto(appPremisesCorrelationDto);
        //set internal files
        List<AppIntranetDocDto> intranetDocDtos = uploadFileClient.getAppIntranetDocListByPremIdAndStatus(correlationId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
        applicationViewDto.setAppIntranetDocDtoList(intranetDocDtos);
//        get routing stage dropdown send to page.
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtoList=applicationViewService.getStage(applicationViewDto.getApplicationDto().getServiceId(),taskDto.getTaskKey());

        Map<String,String> routingStage=IaisCommonUtils.genNewHashMap();
        if(hcsaSvcRoutingStageDtoList!=null){
            if(hcsaSvcRoutingStageDtoList.size()>0){
                for (HcsaSvcRoutingStageDto hcsaSvcRoutingStage:hcsaSvcRoutingStageDtoList) {
                    routingStage.put(hcsaSvcRoutingStage.getStageCode(),hcsaSvcRoutingStage.getStageName());
                }
            }else{
                //if  this is the last stage
                routingStage.put(ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL,
                        MasterCodeUtil.getCodeDesc(ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL));
            }
        }

        //   rollback
        Map<String,String> rollBackMap = IaisCommonUtils.genNewHashMap();
        if(applicationViewDto.getRollBackHistroyList()!=null){
        for (AppPremisesRoutingHistoryDto e:applicationViewDto.getRollBackHistroyList()) {
            String stageName=applicationViewService.getStageById(e.getStageId()).getStageName();
            String userId=e.getActionby();
            String wrkGrpId=e.getWrkGrpId();
            OrgUserDto user=applicationViewService.getUserById(userId);
            String actionBy=user.getDisplayName();

            rollBackMap.put(actionBy+"("+e.getRoleId()+")",e.getStageId()+","+wrkGrpId+","+userId);
          }
        }
        applicationViewDto.setRollBack(rollBackMap);




       Integer rfiCount =  applicationService.getAppBYGroupIdAndStatus(applicationViewDto.getApplicationDto().getAppGrpId(),
               ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION);
       log.info(StringUtil.changeForLog("The rfiCount is -->:"+rfiCount));

        if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(applicationViewDto.getApplicationDto().getStatus())){
            routingStage.put(ApplicationConsts.PROCESSING_DECISION_BROADCAST_QUERY,"Broadcast");
            routingStage.put(ApplicationConsts.PROCESSING_DECISION_ROUTE_TO_DMS,"Trigger to DMS");
        }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(applicationViewDto.getApplicationDto().getStatus())){
            routingStage.put(ApplicationConsts.PROCESSING_DECISION_BROADCAST_REPLY,"Broadcast Reply For Internal");
        }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(applicationViewDto.getApplicationDto().getStatus())
                || ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING.equals(applicationViewDto.getApplicationDto().getStatus())
                || ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION_REPLY.equals(applicationViewDto.getApplicationDto().getStatus())){
            if(rfiCount==0){
//                routingStage.put(ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION,"Request For Information");
            }
        }else if(ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(applicationViewDto.getApplicationDto().getStatus())){
            routingStage.put(ApplicationConsts.PROCESSING_DECISION_BROADCAST_REPLY,"Broadcast Reply For Internal");
        }
        applicationViewDto.setVerified(routingStage);

        //recomeDation
        HcsaServiceDto hcsaServiceDto=applicationViewService.getHcsaServiceDtoById(applicationViewDto.getApplicationDto().getServiceId());
        String svcCode=hcsaServiceDto.getSvcCode();
        RiskAcceptiionDto riskAcceptiionDto=new RiskAcceptiionDto();
        riskAcceptiionDto.setScvCode(svcCode);
        List<RiskAcceptiionDto> listRiskAcceptiionDto = IaisCommonUtils.genNewArrayList();
        listRiskAcceptiionDto.add(riskAcceptiionDto);
        List<RiskResultDto> listRiskResultDto = hcsaConfigClient.getRiskResult(listRiskAcceptiionDto).getEntity();
        List<String> riskResult = IaisCommonUtils.genNewArrayList();
        if(listRiskResultDto!=null && !listRiskResultDto.isEmpty()){
            for(RiskResultDto riskResultDto :listRiskResultDto){
                String dateType = riskResultDto.getDateType();
                String codeDesc = MasterCodeUtil.getCodeDesc(dateType);
                String count = String.valueOf(riskResultDto.getTimeCount());
                String recommTime = count+" "+codeDesc;
                riskResult.add(recommTime);
            }
        }
        applicationViewDto.setRecomeDation(riskResult);
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        if(applicationDto != null){
            if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType()) ){
                //RFC
                if (!StringUtil.isEmpty(applicationDto.getId())) {
                    List<AppEditSelectDto> appEditSelectDtos = applicationService.getAppEditSelectDtos(applicationDto.getId(), ApplicationConsts.APPLICATION_EDIT_TYPE_RFC);
                    if (!IaisCommonUtils.isEmpty(appEditSelectDtos)) {
                        applicationViewDto.setAppEditSelectDto(appEditSelectDtos.get(0));
                    }
                }
            }else{
                AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
                appEditSelectDto.setPremisesEdit(true);
                appEditSelectDto.setDocEdit(true);
                appEditSelectDto.setMedAlertEdit(true);
                appEditSelectDto.setServiceEdit(true);
                appEditSelectDto.setPoEdit(true);
                appEditSelectDto.setDpoEdit(true);
                applicationViewDto.setAppEditSelectDto(appEditSelectDto);
            }
        }
        List<AppSupDocDto> appSupDocDtoList = applicationViewDto.getAppSupDocDtoList();
        if(appSupDocDtoList == null || (appSupDocDtoList.size() == 0)){
            ParamUtil.setRequestAttr(bpc.request, "appSupDocDtoListNull","Y");
        }
        String roleId = taskDto.getRoleId();
        AppPremisesRecommendationDto appPremisesRecommendationDto = applicationViewDto.getAppPremisesRecommendationDto();
        if(appPremisesRecommendationDto != null){
            Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
            String recommendationOnlyShow = "";
            if(recomInNumber == null || recomInNumber == 0){
                recommendationOnlyShow = "reject";
            }else if(recomInNumber ==2){
                recommendationOnlyShow = "2 Year";
            }
            //PSO 0062307
            if(RoleConsts.USER_ROLE_PSO.equals(roleId)){
                ParamUtil.setRequestAttr(bpc.request, "recommendationStr",recommendationOnlyShow);
            }

            Date recomInDate = appPremisesRecommendationDto.getRecomInDate();
            String recomInDateOnlyShow = Formatter.formatDateTime(recomInDate,Formatter.DATE);
            ParamUtil.setRequestAttr(bpc.request, "recomInDateOnlyShow",recomInDateOnlyShow);
            ParamUtil.setRequestAttr(bpc.request, "recommendationOnlyShow",recommendationOnlyShow);
        }

        List<SelectOption> nextStageList = IaisCommonUtils.genNewArrayList();
        nextStageList.add(new SelectOption("", "Please Select"));
        if(RoleConsts.USER_ROLE_AO1.equals(roleId) || RoleConsts.USER_ROLE_AO2.equals(roleId)){
            nextStageList.add(new SelectOption("VERIFIED", "Support"));
        }else{
            nextStageList.add(new SelectOption("VERIFIED", "Verified"));
        }
        if((ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION_REPLY.equals(applicationViewDto.getApplicationDto().getStatus())
                || ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(applicationViewDto.getApplicationDto().getStatus()))
                && RoleConsts.USER_ROLE_ASO.equals(taskDto.getRoleId())){

        }else{
            nextStageList.add(new SelectOption("ROLLBACK", "Internal Route Back"));
        }
        nextStageList.add(new SelectOption("PROCRFI", "Request For Information"));
        ParamUtil.setSessionAttr(bpc.request, "nextStages", (Serializable)nextStageList);

        List<SelectOption> nextStageReplyList = IaisCommonUtils.genNewArrayList();
        nextStageReplyList.add(new SelectOption("", "Please Select"));
        nextStageReplyList.add(new SelectOption("PROCREP", "Give Clarification"));
        ParamUtil.setSessionAttr(bpc.request, "nextStageReply", (Serializable)nextStageReplyList);

        ParamUtil.setSessionAttr(bpc.request,"applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request,"taskDto", taskDto);

        List<SelectOption> decisionValues = IaisCommonUtils.genNewArrayList();
        decisionValues.add(new SelectOption("", "Please Select"));
        decisionValues.add(new SelectOption("decisionApproval", "Approval"));
        decisionValues.add(new SelectOption("decisionReject", "Reject"));
        ParamUtil.setSessionAttr(bpc.request, "decisionValues", (Serializable)decisionValues);

        log.debug(StringUtil.changeForLog("the do prepareData end ...."));
    }

    /**
     * StartStep: chooseStage
     *
     * @param bpc
     * @throws
     */
    public void chooseStage(BaseProcessClass bpc) throws ParseException {
        log.debug(StringUtil.changeForLog("the do chooseStage start ...."));
        //validate
        HcsaApplicationViewValidate hcsaApplicationViewValidate = new HcsaApplicationViewValidate();
        Map<String, String> errorMap = hcsaApplicationViewValidate.validate(bpc.request);
        if(!errorMap.isEmpty()){
            String doProcess = "Y";
            ParamUtil.setRequestAttr(bpc.request, "doProcess",doProcess);
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", "PREPARE");
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
        }else{
            TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
            String appPremCorreId=taskDto.getRefNo();
            //save recommendation
            String recommendationStr = ParamUtil.getString(bpc.request,"recommendation");
            if(StringUtil.isEmpty(recommendationStr)){

            }else if(("reject").equals(recommendationStr)){
//                ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
//                AppPremisesRecommendationDto oldAppPremisesRecommendationDto = applicationViewDto.getAppPremisesRecommendationDto();
//                if(oldAppPremisesRecommendationDto != null){
//                    oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
//                    insRepService.updateRecommendation(oldAppPremisesRecommendationDto);
//                }
                AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
                appPremisesRecommendationDto.setAppPremCorreId(appPremCorreId);
                appPremisesRecommendationDto.setRecomInNumber(0);
                appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
                appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                String dateStr = ParamUtil.getDate(bpc.request, "tuc");
                if(!StringUtil.isEmpty(dateStr)){
                    Date date = Formatter.parseDate(dateStr);
                    appPremisesRecommendationDto.setRecomInDate(date);
                }
                insRepService.updateRecommendation(appPremisesRecommendationDto);
            }else{
                AppPremisesRecommendationDto appPremisesRecommendationDto=new AppPremisesRecommendationDto();
                String[] strs=recommendationStr.split("\\s+");
                appPremisesRecommendationDto.setAppPremCorreId(appPremCorreId);
                appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
                appPremisesRecommendationDto.setRecomInNumber( Integer.valueOf(strs[0]));
                appPremisesRecommendationDto.setChronoUnit(strs[1]);
                String dateStr = ParamUtil.getDate(bpc.request, "tuc");
                if(!StringUtil.isEmpty(dateStr)){
                    Date date = Formatter.parseDate(dateStr);
                    appPremisesRecommendationDto.setRecomInDate(date);
                }
                insRepService.updateRecommendation(appPremisesRecommendationDto);
            }
            String verified = ParamUtil.getString(bpc.request,"verified");
            String rollBack=ParamUtil.getString(bpc.request,"rollBack");
            String nextStage=null;
            if(StringUtil.isEmpty(rollBack)){
                nextStage=verified;
            }else{
                nextStage="PROCRB";
            }

            String reply=ParamUtil.getString(bpc.request,"nextStageReplys");
            if(!StringUtil.isEmpty(reply)){
                nextStage=reply;
            }

            log.debug(StringUtil.changeForLog("the nextStage is -->:"+nextStage));
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", nextStage);

            ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
            if(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(applicationViewDto.getApplicationDto().getStatus())){
                String[] fastTracking =  ParamUtil.getStrings(bpc.request,"fastTracking");
                if(fastTracking!=null){
                    applicationViewDto.getApplicationDto().setFastTracking(true);
                }
            }
            log.debug(StringUtil.changeForLog("the do chooseStage end ...."));
        }
    }


    /**
     * StartStep: chooseAckValue
     */

    public void chooseAckValue(BaseProcessClass bpc) throws Exception{
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
        String roleId = taskDto.getRoleId();
        String successInfo = MessageCodeKey.ACK003;
        String verified = ParamUtil.getString(bpc.request,"verified");
        String rollBack = ParamUtil.getString(bpc.request,"rollBack");
        String decisionValue = ParamUtil.getString(bpc.request,"decisionValues");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String status = applicationDto.getStatus();
        String crud_action_type = (String) ParamUtil.getRequestAttr(bpc.request, "crud_action_type");
        //replay "PROCREP"
        if("PROCREP".equals(crud_action_type)){
            if("decisionApproval".equals(decisionValue)){
                //DMS APPROVAL
                successInfo = MessageCodeKey.ACK013;
            }else if("decisionReject".equals(decisionValue)){
                //DMS REJECT
                successInfo = MessageCodeKey.ACK014;
            }else{
                successInfo = MessageCodeKey.ACK015;
            }
        }else{
            //ASO PSO
            if(RoleConsts.USER_ROLE_ASO.equals(roleId) || RoleConsts.USER_ROLE_PSO.equals(roleId)){
                successInfo = MessageCodeKey.ACK003;
            }
            //verified
            if(!StringUtil.isEmpty(verified)){
                //AO1 -> AO2
                if(RoleConsts.USER_ROLE_AO1.equals(roleId) && RoleConsts.USER_ROLE_AO2.equals(verified)){
                    successInfo = MessageCodeKey.ACK005;
                }else if(RoleConsts.USER_ROLE_AO2.equals(roleId) && RoleConsts.USER_ROLE_AO3.equals(verified)){
                    //AO2 -> AO3
                    successInfo = MessageCodeKey.ACK007;
                }else if(RoleConsts.USER_ROLE_AO3.equals(roleId) && ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(status)){
                    //AO3 APPROVAL
                    successInfo = MessageCodeKey.ACK009;
                }else if(RoleConsts.USER_ROLE_AO3.equals(roleId) && ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(status)){
                    //AO3 REJECT
                    successInfo = MessageCodeKey.ACK010;
                }else if(RoleConsts.USER_ROLE_AO3.equals(roleId) && ApplicationConsts.PROCESSING_DECISION_ROUTE_TO_DMS.equals(verified)){
                    //AO3 DMS
                    successInfo = MessageCodeKey.ACK011;
                }


            }else if(!StringUtil.isEmpty(rollBack)){
                //roll back
                if(RoleConsts.USER_ROLE_AO1.equals(roleId)){
                    //AO1
                    successInfo = MessageCodeKey.ACK006;
                }else if(RoleConsts.USER_ROLE_AO2.equals(roleId)){
                    //AO2
                    successInfo = MessageCodeKey.ACK008;
                }else if((RoleConsts.USER_ROLE_AO3.equals(roleId))){
                    //AO3
                    successInfo = MessageCodeKey.ACK012;
                }
            }
        }

        ParamUtil.setRequestAttr(bpc.request,"successInfo",successInfo);




    }

    /**
     * StartStep: rontingTaskToPSO
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToPSO(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToPSO start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_PSO,ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING,RoleConsts.USER_ROLE_PSO);
        log.debug(StringUtil.changeForLog("the do rontingTaskToPSO end ...."));
    }


    /**
     * StartStep: rontingTaskToINS
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToINS(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToINS start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_INS,ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT,RoleConsts.USER_ROLE_INSPECTIOR);
        log.debug(StringUtil.changeForLog("the do rontingTaskToINS end ...."));
    }


    /**
     * StartStep: rontingTaskToASO
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToASO(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToASO start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_ASO,ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING,RoleConsts.USER_ROLE_ASO);
        log.debug(StringUtil.changeForLog("the do rontingTaskToASO end ...."));
    }

    /**
     * StartStep: rontingTaskToAO1
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO1(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO1 start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO1,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01,RoleConsts.USER_ROLE_AO1);
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO1 end ...."));
    }

    /**
     * StartStep: rontingTaskToAO2
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO2(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO2 start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO2,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02,RoleConsts.USER_ROLE_AO2);
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO2 end ...."));
    }

    /**
     * StartStep: rontingTaskToAO3
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO3(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO3 start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO3,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03,RoleConsts.USER_ROLE_AO3);
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO3 end ...."));
    }

    /**
     * StartStep: approve
     *
     * @param bpc
     * @throws
     */
    public void approve(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do approve start ...."));
        routingTask(bpc,null,ApplicationConsts.APPLICATION_STATUS_APPROVED,null);
        log.debug(StringUtil.changeForLog("the do approve end ...."));
    }

    /**
     * StartStep: routeToDMS
     *
     * @param bpc
     * @throws
     */
    public void routeToDMS(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do routeToDMS start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto application = applicationViewDto.getApplicationDto();
        if(application != null){
            String appNo =  application.getApplicationNo();
            log.info(StringUtil.changeForLog("The appNo is -->:"+appNo));
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto =  appPremisesRoutingHistoryService.
                    getAppPremisesRoutingHistoryForCurrentStage(appNo,HcsaConsts.ROUTING_STAGE_INS);
            if(appPremisesRoutingHistoryDto == null){
                appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.
                        getAppPremisesRoutingHistoryForCurrentStage(appNo,HcsaConsts.ROUTING_STAGE_ASO);
            }
            if(appPremisesRoutingHistoryDto != null){
                rollBack(bpc,appPremisesRoutingHistoryDto.getStageId(),ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS,
                        appPremisesRoutingHistoryDto.getRoleId(),appPremisesRoutingHistoryDto.getWrkGrpId(),appPremisesRoutingHistoryDto.getActionby());
            }else{
                log.error(StringUtil.changeForLog("can not get the appPremisesRoutingHistoryDto ..."));
            }
        }else{
            log.error(StringUtil.changeForLog("do not have the applicaiton"));
        }
        log.debug(StringUtil.changeForLog("the do routeToDMS end ...."));
    }

    /**
     * StartStep: routeBack
     *
     * @param bpc
     * @throws
     */
    public void routeBack(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do routeBack start ...."));
        String str=ParamUtil.getString(bpc.request,"rollBack");
        String[] result=str.split(",");
        String satageId=result[0];
        String wrkGpId=result[1];
        String userId=result[2];
        if(HcsaConsts.ROUTING_STAGE_ASO.equals(satageId)){
            rollBack(bpc,HcsaConsts.ROUTING_STAGE_ASO,ApplicationConsts.APPLICATION_STATUS_ROLL_BACK,RoleConsts.USER_ROLE_ASO,wrkGpId,userId);
        }else if(HcsaConsts.ROUTING_STAGE_PSO.equals(satageId)){
            rollBack(bpc,HcsaConsts.ROUTING_STAGE_PSO,ApplicationConsts.APPLICATION_STATUS_ROLL_BACK,RoleConsts.USER_ROLE_PSO,wrkGpId,userId);
        }else if(HcsaConsts.ROUTING_STAGE_INS.equals(satageId)){
            rollBack(bpc,HcsaConsts.ROUTING_STAGE_INS,ApplicationConsts.APPLICATION_STATUS_ROLL_BACK,RoleConsts.USER_ROLE_INSPECTIOR,wrkGpId,userId);
        }else if(HcsaConsts.ROUTING_STAGE_AO1.equals(satageId)){
            rollBack(bpc,HcsaConsts.ROUTING_STAGE_AO1,ApplicationConsts.APPLICATION_STATUS_ROLL_BACK,RoleConsts.USER_ROLE_AO1,wrkGpId,userId);
        }else if(HcsaConsts.ROUTING_STAGE_AO2.equals(satageId)){
            rollBack(bpc,HcsaConsts.ROUTING_STAGE_AO2,ApplicationConsts.APPLICATION_STATUS_ROLL_BACK,RoleConsts.USER_ROLE_AO2,wrkGpId,userId);
        }else if(HcsaConsts.ROUTING_STAGE_AO3.equals(satageId)){
            rollBack(bpc,HcsaConsts.ROUTING_STAGE_AO3,ApplicationConsts.APPLICATION_STATUS_ROLL_BACK,RoleConsts.USER_ROLE_AO3,wrkGpId,userId);
        }
        log.debug(StringUtil.changeForLog("the do routeBack end ...."));
    }

    /**
     * StartStep: internalEnquiry
     *
     * @param bpc
     * @throws
     */
    public void internalEnquiry(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do internalEnquiry start ...."));
        //TODO:internalEnquiry
        log.debug(StringUtil.changeForLog("the do internalEnquiry end ...."));
    }


    /**
     * StartStep: support
     *
     * @param bpc
     * @throws
     */
    public void support(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do support start ...."));
        //TODO:support

        log.debug(StringUtil.changeForLog("the do support end ...."));
    }

    /**
     * StartStep: broadcast
     *
     * @param bpc
     * @throws
     */
    public void broadcast(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do broadcast start ...."));
        //get the user for this applicationNo
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
          List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = appPremisesRoutingHistoryService.
                  getAppPremisesRoutingHistoryDtosByAppNo(applicationDto.getApplicationNo());
        List<String> userIds = getUserIds(appPremisesRoutingHistoryDtos);
        if(userIds != null && userIds.size() > 0){
            BroadcastOrganizationDto broadcastOrganizationDto = broadcastService.getBroadcastOrganizationDto(
                    applicationDto.getApplicationNo(),AppConsts.DOMAIN_TEMPORARY);
            BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();
            //create workgroup
            WorkingGroupDto workingGroupDto = broadcastOrganizationDto.getWorkingGroupDto();
            broadcastOrganizationDto.setRollBackworkingGroupDto((WorkingGroupDto)CopyUtil.copyMutableObject(workingGroupDto));
            if(workingGroupDto ==  null){
                workingGroupDto = new WorkingGroupDto();
                workingGroupDto.setGroupName(applicationDto.getApplicationNo());
                workingGroupDto.setGroupDomain(AppConsts.DOMAIN_TEMPORARY);
            }
            workingGroupDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            broadcastOrganizationDto.setWorkingGroupDto(workingGroupDto);

            //add this user to this workgroup
            List<UserGroupCorrelationDto> userGroupCorrelationDtoList = broadcastOrganizationDto.getUserGroupCorrelationDtoList();
            if(broadcastOrganizationDto.getWorkingGroupDto()!= null && userGroupCorrelationDtoList != null && userGroupCorrelationDtoList.size() > 0){
                List<UserGroupCorrelationDto> cloneUserGroupCorrelationDtos = IaisCommonUtils.genNewArrayList();
                CopyUtil.copyMutableObjectList(userGroupCorrelationDtoList,cloneUserGroupCorrelationDtos);
                broadcastOrganizationDto.setRollBackUserGroupCorrelationDtoList(cloneUserGroupCorrelationDtos);
                userGroupCorrelationDtoList =changeStatusUserGroupCorrelationDtos(userGroupCorrelationDtoList,AppConsts.COMMON_STATUS_ACTIVE);
            }else{
                userGroupCorrelationDtoList = IaisCommonUtils.genNewArrayList();
                for(String id : userIds) {
                    UserGroupCorrelationDto userGroupCorrelationDto = new UserGroupCorrelationDto();
                    userGroupCorrelationDto.setUserId(id);
                    userGroupCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    userGroupCorrelationDto.setIsLeadForGroup(Integer.parseInt(AppConsts.NO));
                    userGroupCorrelationDtoList.add(userGroupCorrelationDto);
                }
            }
            broadcastOrganizationDto.setUserGroupCorrelationDtoList(userGroupCorrelationDtoList);

            //complated this task and create the history
            TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
            taskDto =  completedTask(taskDto);
            broadcastOrganizationDto.setComplateTask(taskDto);
            String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
            String processDecision = ParamUtil.getString(bpc.request,"nextStage");
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                    applicationDto.getStatus(),taskDto.getTaskKey(),null, taskDto.getWkGrpId(),internalRemarks,processDecision,taskDto.getRoleId());
            broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
            broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto)CopyUtil.copyMutableObject(applicationDto));
            //update application status
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST);
            broadcastApplicationDto.setApplicationDto(applicationDto);
            //create the new task and create the history
            TaskDto taskDtoNew = TaskUtil.getTaskDto(taskDto.getTaskKey(),TaskConsts.TASK_TYPE_MAIN_FLOW, taskDto.getRefNo(),null,
                    null,null,0,TaskConsts.TASK_PROCESS_URL_MAIN_FLOW, RoleConsts.USER_ROLE_BROADCAST,IaisEGPHelper.getCurrentAuditTrailDto());
            broadcastOrganizationDto.setCreateTask(taskDtoNew);
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),
                    taskDto.getTaskKey(),null, taskDto.getWkGrpId(),null,null,RoleConsts.USER_ROLE_AO3);
            broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
            //save the broadcast
            broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            String submissionId = generateIdClient.getSeqId().getEntity();
            log.info(StringUtil.changeForLog(submissionId));
            broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,null,submissionId);
            broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,null,submissionId);
            //0062460 update FE  application status.
            applicationService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());
        }

        log.debug(StringUtil.changeForLog("the do broadcast end ...."));
    }


    /**
     * StartStep: broadcastReply
     *
     * @param bpc
     * @throws
     */
    public void broadcastReply(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do broadcastReply start ...."));
        //routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO3,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03,RoleConsts.USER_ROLE_AO3);
        replay(bpc);
        log.debug(StringUtil.changeForLog("the do broadcastReply end ...."));
    }


    /**
     * StartStep: replay
     *
     * @param bpc
     * @throws
     */
    public void replay(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do replay start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        String nextStatus = ApplicationConsts.APPLICATION_STATUS_REPLY;
        String getHistoryStatus = applicationViewDto.getApplicationDto().getStatus();
          if(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(getHistoryStatus)){
              getHistoryStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
              nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
          }
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.getSecondRouteBackHistoryByAppNo(
                applicationViewDto.getApplicationDto().getApplicationNo(),getHistoryStatus);
        String wrkGrpId=appPremisesRoutingHistoryDto.getWrkGrpId();
        String roleId=appPremisesRoutingHistoryDto.getRoleId();
        String stageId=appPremisesRoutingHistoryDto.getStageId();
        String userId=appPremisesRoutingHistoryDto.getActionby();
        rollBack(bpc,stageId,nextStatus,roleId,wrkGrpId,userId);
        log.debug(StringUtil.changeForLog("the do replay end ...."));
    }


    /**
     * StartStep: reject
     *
     * @param bpc
     * @throws
     */
    public void reject(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do reject start ...."));
        //TODO:reject
        log.debug(StringUtil.changeForLog("the do reject end ...."));
    }

    /**
     * StartStep: requestForInformation
     *
     * @param bpc
     * @throws
     */
    public void requestForInformation(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException, IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the do requestForInformation start ...."));
        routingTask(bpc,null,ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION,null);
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        //send message to FE user.
        String messageNo = inboxMsgService.getMessageNo();
        String url = HmacConstants.HTTPS +"://"+systemParamConfig.getInterServerName()+MessageConstants.MESSAGE_CALL_BACK_URL_NEWAPPLICATION+applicationDto.getApplicationNo();
        //Request For Change
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType())){
            //judge premises amend or licence amend
            AppEditSelectDto appEditSelectDto = applicationViewDto.getAppEditSelectDto();
            if(appEditSelectDto != null){
                if(appEditSelectDto.isPremisesListEdit()){
                    url = HmacConstants.HTTPS +"://"+systemParamConfig.getInterServerName()+MessageConstants.MESSAGE_CALL_BACK_URL_PREMISES_LIST+applicationDto.getApplicationNo();
                }
            }
        }
        MsgTemplateDto autoEntity = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_RFI).getEntity();
        Map<String ,Object> map=IaisCommonUtils.genNewHashMap();
        String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
        LicenseeDto licenseeDto = licenseeService.getLicenseeDtoById(licenseeId);
        map.put("APPLICANT_NAME",licenseeDto.getName());
        map.put("DETAILS","");
        map.put("A_HREF",url);
        map.put("MOH_NAME",AppConsts.MOH_AGENCY_NAME);
        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(autoEntity.getMessageContent(), map);

        InterMessageDto interMessageDto = MessageTemplateUtil.getInterMessageDto(MessageConstants.MESSAGE_SUBJECT_REQUEST_FOR_INFORMATION,MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED,
                messageNo,applicationDto.getServiceId(),templateMessageByContent, applicationViewDto.getApplicationGroupDto().getLicenseeId(),IaisEGPHelper.getCurrentAuditTrailDto());
        inboxMsgService.saveInterMessage(interMessageDto);
        //send email
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVAL_OFFICE_ROUTES_ID).getEntity();
        if(msgTemplateDto != null){
            String applicationNo = applicationViewDto.getApplicationDto().getApplicationNo();
            String username = licenseeDto.getName();
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            String approvalOfficerName = loginContext.getUserName();

            Map<String ,Object> tempMap = IaisCommonUtils.genNewHashMap();
            tempMap.put("userName",StringUtil.viewHtml(username));
            tempMap.put("applicationNumber",StringUtil.viewHtml(applicationNo));
            tempMap.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);
            tempMap.put("approvalOfficerName",StringUtil.viewHtml(approvalOfficerName));

            String mesContext = null;
            mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), tempMap);

            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject(" " + msgTemplateDto.getTemplateName() + " " + applicationNo);
            emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(applicationViewDto.getApplicationDto().getAppGrpId());
            //send
            emailClient.sendNotification(emailDto).getEntity();
        }

        log.debug(StringUtil.changeForLog("the do requestForInformation end ...."));
    }

    /**
     * StartStep: lienceStartDate
     *
     * @param bpc
     * @throws
     */
    public void lienceStartDate(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do lienceStartDate start ...."));
        //TODO:lienceStartDate
        log.debug(StringUtil.changeForLog("the do lienceStartDate end ...."));
    }


    /**
     * StartStep: doDocument
     *
     * @param bpc
     * @throws
     */
    public void doDocument(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doDocument start ...."));
//        //TODO:save file
//        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
//        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
//        String dcrudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
//        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);
//
//        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
//        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, crudActionValue);

//        uploadFileClient.saveAppIntranetDocByAppIntranetDoc();


        log.debug(StringUtil.changeForLog("the do doDocument end ...."));
    }



    //***************************************
    //private methods
    //*************************************

    private void routingTask(BaseProcessClass bpc,String stageId,String appStatus,String roleId ) throws FeignException, CloneNotSupportedException {
        //get the user for this applicationNo
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        AppPremisesCorrelationDto newAppPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();
        String newCorrelationId = newAppPremisesCorrelationDto.getId();
        BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
        BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();

        //judge the final status is Approve or Reject.
        if(ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus)){
            AppPremisesRecommendationDto appPremisesRecommendationDto = applicationViewDto.getAppPremisesRecommendationDto();
            if(appPremisesRecommendationDto!=null){
               Integer recomInNumber =  appPremisesRecommendationDto.getRecomInNumber();
               if(null != recomInNumber && recomInNumber == 0){
                   appStatus =  ApplicationConsts.APPLICATION_STATUS_REJECTED;
               }
            }
        }
        //complated this task and create the history
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
        broadcastOrganizationDto.setRollBackComplateTask((TaskDto) CopyUtil.copyMutableObject(taskDto));
        taskDto =  completedTask(taskDto);
        broadcastOrganizationDto.setComplateTask(taskDto);
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        String processDecision = ParamUtil.getString(bpc.request,"nextStage");
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                applicationDto.getStatus(),taskDto.getTaskKey(),null, taskDto.getWkGrpId(),internalRemarks,processDecision,taskDto.getRoleId());
        broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
        //update application status
        broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto) CopyUtil.copyMutableObject(applicationDto));
        String oldStatus = applicationDto.getStatus();
        applicationDto.setStatus(appStatus);
        broadcastApplicationDto.setApplicationDto(applicationDto);
        // send the task
        if(!StringUtil.isEmpty(stageId)){
            //For the BROADCAST Rely
            if(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(oldStatus)){
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto1 = appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryForCurrentStage(
                        applicationDto.getApplicationNo(),stageId
                );
                log.debug(StringUtil.changeForLog("The appId is-->;"+ applicationDto.getId()));
                log.debug(StringUtil.changeForLog("The stageId is-->;"+ stageId));
                if(appPremisesRoutingHistoryDto1 != null){
                    TaskDto newTaskDto = TaskUtil.getTaskDto(stageId,TaskConsts.TASK_TYPE_MAIN_FLOW,
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
            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(appStatus)&&!applicationDto.isFastTracking()){
                List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
                applicationDtoList = removeFastTracking(applicationDtoList);
                boolean isAllSubmit = applicationService.isOtherApplicaitonSubmit(applicationDtoList,applicationDto.getApplicationNo(),
                        ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);
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
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),stageId,HcsaConsts.ROUTING_STAGE_PRE,
                        taskDto.getWkGrpId(),null,null,roleId);
                broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
            }else{
                TaskDto newTaskDto = taskService.getRoutingTask(applicationDto,stageId,roleId,newCorrelationId);
                broadcastOrganizationDto.setCreateTask(newTaskDto);
            }
            //add history for next stage start
            if(!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(appStatus)&&!ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(appStatus)){
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),stageId,null,
                        taskDto.getWkGrpId(),null,null,roleId);
                broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
            }
        }else{
            List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
            applicationDtoList = removeFastTracking(applicationDtoList);
            boolean isAllSubmit = applicationService.isOtherApplicaitonSubmit(applicationDtoList,applicationDto.getApplicationNo(),
                    ApplicationConsts.APPLICATION_STATUS_APPROVED);
            if(isAllSubmit || applicationDto.isFastTracking()){
                //update application Group status
                ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
                broadcastApplicationDto.setRollBackApplicationGroupDto((ApplicationGroupDto)CopyUtil.copyMutableObject(applicationGroupDto));
                applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
                applicationGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                broadcastApplicationDto.setApplicationGroupDto(applicationGroupDto);
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
        applicationService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());
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
    private void rollBack(BaseProcessClass bpc,String stageId,String appStatus,String roleId ,String wrkGpId,String userId) throws FeignException, CloneNotSupportedException {
        //get the user for this applicationNo
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
        BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();

        //complated this task and create the history
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
        broadcastOrganizationDto.setRollBackComplateTask((TaskDto) CopyUtil.copyMutableObject(taskDto));
        taskDto =  completedTask(taskDto);
        broadcastOrganizationDto.setComplateTask(taskDto);
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        String processDecision = ParamUtil.getString(bpc.request,"nextStage");
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                applicationDto.getStatus(),taskDto.getTaskKey(),null, taskDto.getWkGrpId(),internalRemarks,processDecision,taskDto.getRoleId());
        broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
        //update application status
        broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto) CopyUtil.copyMutableObject(applicationDto));
        applicationDto.setStatus(appStatus);
        broadcastApplicationDto.setApplicationDto(applicationDto);

        TaskDto newTaskDto = TaskUtil.getTaskDto(stageId,TaskConsts.TASK_TYPE_MAIN_FLOW,
                taskDto.getRefNo(),wrkGpId, userId,new Date(),0,TaskConsts.TASK_PROCESS_URL_MAIN_FLOW,roleId,
                IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastOrganizationDto.setCreateTask(newTaskDto);

        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),stageId,null,
                taskDto.getWkGrpId(),null,null,roleId);
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
        applicationService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());

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
    private WorkingGroupDto changeStatusWrokGroup(WorkingGroupDto workingGroupDto,String staus){
        if(workingGroupDto!= null && !StringUtil.isEmpty(staus)){
            workingGroupDto.setStatus(staus);
        }
       return workingGroupDto;
    }
    private AppPremisesRoutingHistoryDto getAppPremisesRoutingHistory(String appNo, String appStatus,
                                                                         String stageId,String subStageId,String wrkGrpId, String internalRemarks,String processDecision,
                                                                      String roleId){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setSubStage(subStageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setProcessDecision(processDecision);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setWrkGrpId(wrkGrpId);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return appPremisesRoutingHistoryDto;
    }
    private int remainDays(TaskDto taskDto){
       int result = 0;
       //todo: wait count kpi
      // String  resultStr = DurationFormatUtils.formatPeriod(taskDto.getDateAssigned().getTime(),taskDto.getSlaDateCompleted().getTime(), "d");
     // log.debug(StringUtil.changeForLog("The resultStr is -->:")+resultStr);
      return  result;
    }


    private TaskDto completedTask(TaskDto taskDto){
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(remainDays(taskDto));
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskDto;
    }
    private ApplicationDto updateApplicaiton(ApplicationDto applicationDto,String appStatus){
        applicationDto.setStatus(appStatus);
        return applicationViewService.updateApplicaiton(applicationDto);
    }

    private List<String> getUserIds(List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos){
        Set<String> set = IaisCommonUtils.genNewHashSet();
        if(appPremisesRoutingHistoryDtos == null || appPremisesRoutingHistoryDtos.size() ==0){
            return  null;
        }
        for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto :appPremisesRoutingHistoryDtos ){
            set.add(appPremisesRoutingHistoryDto.getActionby());
        }
        return  new ArrayList(set);


    }

    /************************/

}
