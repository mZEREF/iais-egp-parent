package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.acra.AcraConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskScoreDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicInspectionGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremInspGrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.PostInsGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryMainService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewMainService;
import com.ecquaria.cloud.moh.iais.service.BroadcastMainService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainService;
import com.ecquaria.cloud.moh.iais.service.MohHcsaBeDashboardService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusMainClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryMainClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationMainClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationMainClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateMainClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Process: MohHcsaBeDashboard
 *
 * @author Shicheng
 * @date 2021/4/1 13:31
 **/
@Delegator(value = "mohHcsaBeDashboardDelegator")
@Slf4j
public class MohHcsaBeDashboardDelegator {

    @Autowired
    private TaskService taskService;

    @Autowired
    private InspectionMainService inspectionMainService;

    @Autowired
    private ApplicationViewMainService applicationViewService;

    @Autowired
    private GenerateIdClient generateIdClient;

    @Autowired
    private OrganizationMainClient organizationMainClient;

    @Autowired
    private MohHcsaBeDashboardService mohHcsaBeDashboardService;

    @Autowired
    private AppInspectionStatusMainClient appInspectionStatusMainClient;

    @Autowired
    private AppPremisesRoutingHistoryMainService appPremisesRoutingHistoryService;

    @Autowired
    private BroadcastMainService broadcastService;

    @Autowired
    private CessationMainClient cessationClient;

    @Autowired
    private MsgTemplateMainClient msgTemplateMainClient;

    @Autowired
    private HcsaConfigMainClient hcsaConfigMainClient;

    @Autowired
    private AppPremisesRoutingHistoryMainClient appPremisesRoutingHistoryMainClient;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private InspEmailService inboxMsgService;

    @Autowired
    private ApplicationMainClient applicationMainClient;

    @Value("${iais.email.sender}")
    private String mailSender;

    @Value("${iais.system.one.address}")
    private String systemAddressOne;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private LicenceClient licenceClient;

    static private String APPSTATUSCATEID = "BEE661EE-220C-EA11-BE7D-000C29F371DC";

    /**
     * StartStep: hcsaBeDashboardStart
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardStart(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardStart start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardInit
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardInit(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardInit start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardInitPre
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardInitPre(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardInitPre start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardStep
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardStep(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardStep start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardApprove
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardApprove(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the hcsaBeDashboardApprove start ...."));
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
                    if(("ao1approve").equals(action)){
                        log.info(StringUtil.changeForLog("the do ao1 approve start ...."));
                        ParamUtil.setSessionAttr(bpc.request,"bemainAo1Ao2Approve","Y");
                        successStatus = ApplicationConsts.APPLICATION_STATUS_APPROVED;
                        routingTask(bpc,"",successStatus,"",applicationViewDto,taskDto);
                        log.info(StringUtil.changeForLog("the do ao1 approve end ...."));
                    }else{
                        log.info(StringUtil.changeForLog("the do rontingTaskToAO2 start ...."));
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
                        }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW.equals(status)){
                            successStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW;
                            routingTask(bpc, HcsaConsts.ROUTING_STAGE_AO2, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02, RoleConsts.USER_ROLE_AO2,applicationViewDto,taskDto);
                        }else{
                            successStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02;
                            routingTask(bpc, HcsaConsts.ROUTING_STAGE_AO2, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02, RoleConsts.USER_ROLE_AO2,applicationViewDto,taskDto);
                        }


                        log.info(StringUtil.changeForLog("the do rontingTaskToAO2 end ...."));
                    }
                }else if(RoleConsts.USER_ROLE_AO2.equals(loginContext.getCurRoleId())){
                    if(("ao2approve").equals(action)){
                        log.info(StringUtil.changeForLog("the do ao2 approve start ...."));
                        ParamUtil.setSessionAttr(bpc.request,"bemainAo1Ao2Approve","Y");
                        successStatus = ApplicationConsts.APPLICATION_STATUS_APPROVED;
                        routingTask(bpc,"",successStatus,"",applicationViewDto,taskDto);
                        log.info(StringUtil.changeForLog("the do ao2 approve end ...."));
                    }else{
                        log.info(StringUtil.changeForLog("the do rontingTaskToAO3 start ...."));
                        if(ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO.equals(status)){
                            try{
                                replay(bpc,applicationViewDto,taskDto);
                            }catch (Exception e){
                                log.info(e.getMessage(),e);
                            }

                        }else{
                            routingTask(bpc, HcsaConsts.ROUTING_STAGE_AO3, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03, RoleConsts.USER_ROLE_AO3,applicationViewDto,taskDto);
                        }
                        log.info(StringUtil.changeForLog("the do rontingTaskToAO3 end ...."));
                        successStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
                    }

                }else if(RoleConsts.USER_ROLE_AO3.equals(loginContext.getCurRoleId())){
                    //judge the final status is Approve or Reject.
                    AppPremisesRecommendationDto appPremisesRecommendationDto = applicationViewDto.getAppPremisesRecommendationDto();
                    if(appPremisesRecommendationDto!=null){
                        Integer recomInNumber =  appPremisesRecommendationDto.getRecomInNumber();
                        String appgroupName = applicationDto.getAppGrpId() + "backendAppGroupStatus";
                        if(null != recomInNumber && recomInNumber == 0){
                            String appGroupStatus = (String)ParamUtil.getSessionAttr(bpc.request,appgroupName);
                            if(StringUtil.isEmpty(appGroupStatus)){
                                ParamUtil.setSessionAttr(bpc.request,appgroupName,ApplicationConsts.APPLICATION_STATUS_REJECTED);
                            }
                            successStatus =  ApplicationConsts.APPLICATION_STATUS_REJECTED;
                        }else{
                            ParamUtil.setSessionAttr(bpc.request,appgroupName,ApplicationConsts.APPLICATION_STATUS_APPROVED);
                            successStatus = ApplicationConsts.APPLICATION_STATUS_APPROVED;
                        }
                    }else{
                        successStatus = ApplicationConsts.APPLICATION_STATUS_APPROVED;
                    }
                    log.info(StringUtil.changeForLog("the do approve start ...."));
                    routingTask(bpc,"",successStatus,"",applicationViewDto,taskDto);
                    log.info(StringUtil.changeForLog("the do approve end ...."));
                }
            }
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
            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW.equals(successStatus)){
                successInfo = "LOLEV_ACK036";
            }
            ParamUtil.setRequestAttr(bpc.request,"successInfo",successInfo);
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

        mohHcsaBeDashboardService.createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT, taskDto, userId,"",HcsaConsts.ROUTING_STAGE_POT);
        mohHcsaBeDashboardService.createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING, taskDto, userId,"",HcsaConsts.ROUTING_STAGE_POT);

        //update FE  application status.
        applicationViewService.updateFEApplicaiton(applicationViewDto.getApplicationDto());
    }

    /**
     * StartStep: hcsaBeDashboardSwitchSort
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardSwitchSort(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardSwitchSort start ...."));
        //todo:The current requirements do not need to be sorted
    }

    /**
     * StartStep: hcsaBeDashboardPage
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardPage(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardPage start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    /**
     * StartStep: hcsaBeDashboardInGroup
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardInGroup(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardInGroup start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardApplicantReply
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardApplicantReply(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardApplicantReply start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardKpi
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardKpi(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardKpi start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardCommonPool
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardCommonPool(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardCommonPool start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardWaitApprove
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardWaitApprove(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardWaitApprove start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardRenewExpiry
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardRenewExpiry(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardRenewExpiry start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardAssignToMe
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardAssignToMe(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardAssignToMe start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardQuery
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardQuery(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardQuery start ...."));
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false, null);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc,boolean isNew, String className){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "dashSearchParam");
        int pageSize = SystemParamUtil.getDefaultPageSize();
        if(searchParam == null || isNew){
            searchParam = new SearchParam(className);
            searchParam.setPageSize(pageSize);
            searchParam.setPageNo(1);
            searchParam.setSort("GROUP_NO", SearchParam.ASCENDING);
        }
        return searchParam;
    }

    /**
     * @Function: Routing Task
     * @param bpc
     * @param stageId
     * @param appStatus
     * @param roleId
     * @param applicationViewDto
     * @param taskDto
     * @throws FeignException
     * @throws CloneNotSupportedException
     */
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
                if(null == recomInNumber || recomInNumber == 0){
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
        log.info(StringUtil.changeForLog("BackendInboxReturnFee:" + JsonUtil.parseToJson(returnFeeMap)));
        changePostInsForTodoAudit(applicationViewDto);
        //set risk score
        setRiskScore(applicationDto,newCorrelationId);
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
                                appReturnFeeDto.setStatus("paying");
                                appReturnFeeDto.setTriggerCount(0);
                                List<AppReturnFeeDto> saveReturnFeeDtos = IaisCommonUtils.genNewArrayList();
                                saveReturnFeeDtos.add(appReturnFeeDto);
                                try {
                                    doRefunds(saveReturnFeeDtos);
                                }catch (Exception e){
                                    log.info(e.getMessage(),e);
                                }
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
        taskDto = completedTask(taskDto);
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

        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = mohHcsaBeDashboardService.getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                applicationDto.getStatus(),taskDto.getTaskKey(),null, taskDto.getWkGrpId(),null,null,decision,taskDto.getRoleId());
        broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
        //update application status
        applicationDto.setStatus(appStatus);
        broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto) CopyUtil.copyMutableObject(applicationDto));
        String oldStatus = applicationDto.getStatus();

        broadcastApplicationDto.setApplicationDto(applicationDto);
        if(!StringUtil.isEmpty(stageId)){
            log.info(StringUtil.changeForLog("has next stageId :" + stageId));
            if(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(oldStatus)){
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto1 = appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryForCurrentStage(
                        applicationDto.getApplicationNo(),stageId
                );
                log.info(StringUtil.changeForLog("The appId is-->;"+ applicationDto.getId()));
                log.info(StringUtil.changeForLog("The stageId is-->;"+ stageId));
                if(appPremisesRoutingHistoryDto1 != null){
                    TaskDto newTaskDto = TaskUtil.getTaskDto(applicationDto.getApplicationNo(),stageId, TaskConsts.TASK_TYPE_MAIN_FLOW,
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
            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(appStatus) || ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02.equals(appStatus)){

                if(HcsaConsts.ROUTING_STAGE_INS.equals(taskDto.getTaskKey())){
                    mohHcsaBeDashboardService.updateInspectionStatus(applicationViewDto.getAppPremisesCorrelationId(), InspectionConstants.INSPECTION_STATUS_PENDING_AO2_RESULT);
                }

                if(applicationDto.isFastTracking()){
                    TaskDto newTaskDto = taskService.getRoutingTask(applicationDto,stageId,roleId,newCorrelationId);
                    broadcastOrganizationDto.setCreateTask(newTaskDto);
                }
                List<ApplicationDto> applicationDtoList = applicationViewService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
                applicationDtoList = removeFastTrackingAndTransfer(applicationDtoList);
                List<ApplicationDto> flagApplicationDtoList = IaisCommonUtils.genNewArrayList();
                CopyUtil.copyMutableObjectList(applicationDtoList,flagApplicationDtoList);
                flagApplicationDtoList = removeCurrentApplicationDto(flagApplicationDtoList,applicationDto.getId());
                boolean flag = taskService.checkCompleteTaskByApplicationNo(flagApplicationDtoList,newCorrelationId);

                log.info(StringUtil.changeForLog("isAllSubmit is " + flag));
                if(flag){
                    List<ApplicationDto> saveApplicationDtoList = IaisCommonUtils.genNewArrayList();
                    CopyUtil.copyMutableObjectList(applicationDtoList,saveApplicationDtoList);
                    //update current application status in db search result
                    updateCurAppStatusByLicensee(bpc,saveApplicationDtoList,licenseeId);
                    List<ApplicationDto> ao2AppList = getStatusAppList(saveApplicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02);
                    List<ApplicationDto> ao3AppList = getStatusAppList(saveApplicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);
                    List<ApplicationDto> creatTaskApplicationList = ao2AppList;
                    //routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO2,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02,RoleConsts.USER_ROLE_AO2);
                    if(IaisCommonUtils.isEmpty(ao2AppList) && !IaisCommonUtils.isEmpty(ao3AppList)){
                        creatTaskApplicationList = ao3AppList;
                    }else{
                        stageId = HcsaConsts.ROUTING_STAGE_AO2;
                        roleId = RoleConsts.USER_ROLE_AO2;
                    }

                    // send the task to Ao3 or ao2
                    TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(creatTaskApplicationList,
                            stageId,roleId,IaisEGPHelper.getCurrentAuditTrailDto(),taskDto.getRoleId());
                    List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
                    List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
                    broadcastOrganizationDto.setOneSubmitTaskList(taskDtos);
                    broadcastApplicationDto.setOneSubmitTaskHistoryList(appPremisesRoutingHistoryDtos);
                }
            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(appStatus)){
                AppInspectionStatusDto appInspectionStatusDto = new AppInspectionStatusDto();
                appInspectionStatusDto.setAppPremCorreId(taskDto.getRefNo());
                appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
                broadcastApplicationDto.setAppInspectionStatusDto(appInspectionStatusDto);
                TaskDto newTaskDto = taskService.getRoutingTask(applicationDto,stageId,roleId,newCorrelationId);
                broadcastOrganizationDto.setCreateTask(newTaskDto);
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew = mohHcsaBeDashboardService.getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                        applicationDto.getStatus(),taskDto.getTaskKey(),null, taskDto.getWkGrpId(),null,null,null,taskDto.getRoleId());
                broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
            }else{
                TaskDto newTaskDto = taskService.getRoutingTask(applicationDto,stageId,roleId,newCorrelationId);
                broadcastOrganizationDto.setCreateTask(newTaskDto);
            }
            //add history for next stage start
            if(!(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(appStatus)||ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02.equals(appStatus))&&!ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(appStatus)){
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew = mohHcsaBeDashboardService.getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),stageId,null,
                        taskDto.getWkGrpId(),null,null,null,roleId);
                broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
            }
        }else{
            log.info(StringUtil.changeForLog("not has next stageId :" + stageId));
            log.info(StringUtil.changeForLog("do ao3 approve ----- "));

            String aoapprove = (String)ParamUtil.getSessionAttr(bpc.request,"bemainAo1Ao2Approve");
            boolean isAo1Ao2Approve = "Y".equals(aoapprove);
            List<ApplicationDto> applicationDtoList = applicationViewService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
            List<ApplicationDto> saveApplicationDtoList = IaisCommonUtils.genNewArrayList();
            CopyUtil.copyMutableObjectList(applicationDtoList,saveApplicationDtoList);
            applicationDtoList = removeFastTracking(applicationDtoList);
            boolean isAllSubmit = applicationViewService.isOtherApplicaitonSubmit(applicationDtoList,applicationDtoIds,
                    appStatus);
            log.info(StringUtil.changeForLog("isAllSubmit is " + isAllSubmit));
            if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(appStatus)){
                //send reject email
                try{
                    rejectSendNotification(applicationDto);
                }catch (Exception e){
                    log.error(StringUtil.changeForLog("send reject notification error"),e);
                }
            }
            if(isAllSubmit || applicationDto.isFastTracking() || isAo1Ao2Approve){
                if(isAo1Ao2Approve){
                    doAo1Ao2Approve(broadcastOrganizationDto,broadcastApplicationDto,applicationDto,applicationDtoIds,taskDto,newCorrelationId);
                }

                boolean needUpdateGroup = applicationViewService.isOtherApplicaitonSubmit(applicationDtoList, applicationDtoIds,
                        ApplicationConsts.APPLICATION_STATUS_APPROVED, ApplicationConsts.APPLICATION_STATUS_REJECTED);
                if(needUpdateGroup || applicationDto.isFastTracking()){
                    //update application Group status
                    ApplicationGroupDto applicationGroupDto = applicationViewService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
                    broadcastApplicationDto.setRollBackApplicationGroupDto((ApplicationGroupDto)CopyUtil.copyMutableObject(applicationGroupDto));
                    String appStatusIsAllRejected = checkAllStatus(saveApplicationDtoList,applicationDtoIds);
                    String appgroupName = applicationDto.getAppGrpId() + "backendAppGroupStatus";
                    String sessionStatus = (String) ParamUtil.getSessionAttr(bpc.request,appgroupName);
                    if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(appStatusIsAllRejected) && ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(sessionStatus)){
                        applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_REJECT);
                    }else{
                        applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
                    }
                    applicationGroupDto.setAo3ApprovedDt(new Date());
                    applicationGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    broadcastApplicationDto.setApplicationGroupDto(applicationGroupDto);

                    //update current application status in db search result
                    updateCurAppStatusByLicensee(bpc,saveApplicationDtoList,licenseeId);

                    for (ApplicationDto viewitem:saveApplicationDtoList
                    ) {
                        log.info(StringUtil.changeForLog("****viewitem ***** " + viewitem.getApplicationNo()));
                    }
                    if(needUpdateGroup){
                        //get and set return fee
                        saveApplicationDtoList = hcsaConfigMainClient.returnFee(saveApplicationDtoList).getEntity();
                        //save return fee
                        saveRejectReturnFee(saveApplicationDtoList,broadcastApplicationDto);
                        //clearApprovedHclCodeByExistRejectApp
                        applicationViewService.clearApprovedHclCodeByExistRejectApp(saveApplicationDtoList,applicationGroupDto.getAppType(), broadcastApplicationDto.getApplicationDto());
                    }
                }
            }
            //cessation
            if (ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)) {
                String originLicenceId = applicationDto.getOriginLicenceId();
                List<String> specLicIds = licenceClient.getActSpecIdByActBaseId(originLicenceId).getEntity();
                if (!IaisCommonUtils.isEmpty(specLicIds)) {
                    List<ApplicationDto> specApplicationDtos = applicationMainClient.getAppsByLicId(specLicIds.get(0)).getEntity();
                    if (!IaisCommonUtils.isEmpty(specApplicationDtos)) {
                        for (ApplicationDto dto : specApplicationDtos) {
                            dto.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                        }
                    }
                    applicationMainClient.updateCessationApplications(specApplicationDtos);
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
        //save the broadcast
        broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        log.info(StringUtil.changeForLog(submissionId));
        broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,bpc.process,submissionId);
        broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,bpc.process,submissionId);
        //0062460 update FE  application status.
        applicationViewService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());
    }

    /**
     * Route Back
     */
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

        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = mohHcsaBeDashboardService.getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
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
            mohHcsaBeDashboardService.updateInspectionStatus(applicationViewDto.getAppPremisesCorrelationId(),InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
        }
        //reply inspector
        if(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS.equals(appStatus)){
            List<TaskDto> taskDtos = organizationMainClient.getTaskByAppNoStatus(applicationDto.getApplicationNo(), TaskConsts.TASK_STATUS_COMPLETED, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION).getEntity();
            taskType = taskDtos.get(0).getTaskType();
            TaskUrl = TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION;
            subStageId = HcsaConsts.ROUTING_STAGE_PRE;
            //update inspector status
            mohHcsaBeDashboardService.updateInspectionStatus(applicationViewDto.getAppPremisesCorrelationId(),InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
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

        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew = mohHcsaBeDashboardService.getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),stageId,subStageId,
                taskDto.getWkGrpId(),null,null,null,roleId);
        broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);

        //save the broadcast
        broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        String evenRefNum = String.valueOf(System.currentTimeMillis());
        broadcastOrganizationDto.setEventRefNo(evenRefNum);
        broadcastApplicationDto.setEventRefNo(evenRefNum);
        String submissionId = generateIdClient.getSeqId().getEntity();
        //save the broadcast
        broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
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
                appReturnFeeDto.setStatus("paying");
                appReturnFeeDto.setTriggerCount(0);
                saveReturnFeeDtos.add(appReturnFeeDto);
                try {
                    doRefunds(saveReturnFeeDtos);
                }catch (Exception e){
                    log.info(e.getMessage(),e);
                }
                log.info(StringUtil.changeForLog("**** saveRejectReturnFee applicationDto ReturnFee***** " +applicationDto.getApplicationNo() + " and " + applicationDto.getReturnFee()));
            }
        }
        if(!IaisCommonUtils.isEmpty(saveReturnFeeDtos)){
            broadcastApplicationDto.setReturnFeeDtos(saveReturnFeeDtos);
            broadcastApplicationDto.setRollBackReturnFeeDtos(saveReturnFeeDtos);
        }
    }

    private List<ApplicationDto> getStatusAppList(List<ApplicationDto> applicationDtos, String status){
        if(IaisCommonUtils.isEmpty(applicationDtos) || StringUtil.isEmpty(status)){
            return null;
        }
        List<ApplicationDto> applicationDtoList = null;
        for(ApplicationDto applicationDto : applicationDtos){
            if(status.equals(applicationDto.getStatus())){
                if(applicationDtoList == null){
                    applicationDtoList = IaisCommonUtils.genNewArrayList();
                    applicationDtoList.add(applicationDto);
                }else{
                    applicationDtoList.add(applicationDto);
                }
            }
        }

        return applicationDtoList;
    }

    private List<ApplicationDto> removeFastTracking(List<ApplicationDto> applicationDtos){
        List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(applicationDtos)){
            for (ApplicationDto applicationDto : applicationDtos){
                if(ApplicationConsts.APPLICATION_STATUS_TRANSFER_ORIGIN.equals(applicationDto.getStatus())){
                    continue;
                }
                if(!applicationDto.isFastTracking()){
                    result.add(applicationDto);
                }
            }
        }
        return  result;
    }

    private void updateCurrentApplicationStatus(List<ApplicationDto> applicationDtos,String applicationId,String status){
        if(!IaisCommonUtils.isEmpty(applicationDtos) && !StringUtil.isEmpty(applicationId)){
            for (ApplicationDto applicationDto : applicationDtos){
                if(applicationId.equals(applicationDto.getId())){
                    applicationDto.setStatus(status);
                }
            }
        }
    }

    private void updateCurAppStatusByLicensee(BaseProcessClass bpc,List<ApplicationDto> applicationDtos,String licenseeId) {
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
            boolean isCharity;
            if (AcraConsts.ENTITY_TYPE_CHARITIES.equals(entityType)) {
                isCharity = true;
            } else {
                isCharity = false;
            }
            for (ApplicationDto applicationDto : applicationDtos) {
                log.info(StringUtil.changeForLog("****application return fee applicationDto***** " + applicationDto.getApplicationNo()));
                applicationDto.setIsCharity(isCharity);
                applicationDto.setReturnType(ApplicationConsts.APPLICATION_RETURN_FEE_REJECT);
                for (Map.Entry<String, String> entry : returnFee.entrySet()) {
                    log.info(StringUtil.changeForLog("****application return fee returnFee***** " + entry.getKey()));
                    if (entry.getKey().equals(applicationDto.getApplicationNo())) {
                        log.info(StringUtil.changeForLog("****application return fee***** " + entry.getKey() + " *****" + entry.getValue()));
                        applicationDto.setStatus(entry.getValue());
                    }
                }
            }
        }
    }

    private List<ApplicationDto> removeCurrentApplicationDto(List<ApplicationDto> applicationDtoList, String currentId){
        List<ApplicationDto> result = null;
        if(!IaisCommonUtils.isEmpty(applicationDtoList) && !StringUtil.isEmpty(currentId)){
            result = IaisCommonUtils.genNewArrayList();
            for(ApplicationDto applicationDto : applicationDtoList){
                if(currentId.equals(applicationDto.getId())){
                    continue;
                }
                result.add(applicationDto);
            }
        }
        return result;
    }

    private TaskDto completedTask(TaskDto taskDto){
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(0);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskDto;
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

    private List<ApplicationDto> removeFastTrackingAndTransfer(List<ApplicationDto> applicationDtos){
        List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(applicationDtos)){
            for (ApplicationDto applicationDto : applicationDtos){
                if(ApplicationConsts.APPLICATION_STATUS_TRANSFER_ORIGIN.equals(applicationDto.getStatus())){
                    continue;
                }
                if(!applicationDto.isFastTracking()){
                    result.add(applicationDto);
                }
            }
        }
        return  result;
    }

    /**
     * StartStep: replay
     *
     * @param bpc
     * @throws
     */
    private void replay(BaseProcessClass bpc,ApplicationViewDto applicationViewDto,TaskDto taskDto) throws FeignException, CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the do replay start ...."));
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
                    log.debug(StringUtil.changeForLog("RoutingStageDtoList is null"));
                }
            }else{
                rollBack(bpc,applicationViewDto,stageId,nextStatus,roleId,wrkGrpId,userId,taskDto);
            }
        }
        log.info(StringUtil.changeForLog("the do replay end ...."));
    }

    /**
     * StartStep: routeToDMS
     *
     * @param bpc
     * @throws
     */
    public void routeToDMS(BaseProcessClass bpc,ApplicationViewDto applicationViewDto,TaskDto taskDto) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the do routeToDMS start ...."));
        ApplicationDto application = applicationViewDto.getApplicationDto();
        if(application != null){
            String appNo =  application.getApplicationNo();
            log.info(StringUtil.changeForLog("The appNo is -->:"+appNo));
            //HcsaConsts.ROUTING_STAGE_INS
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto =  appPremisesRoutingHistoryService.
                    getAppPremisesRoutingHistoryForCurrentStage(appNo, HcsaConsts.ROUTING_STAGE_INS, RoleConsts.USER_ROLE_INSPECTIOR,
                            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT);
            if(appPremisesRoutingHistoryDto == null){
                log.info(StringUtil.changeForLog("appPremisesRoutingHistoryDto is null"));
                appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.
                        getAppPremisesRoutingHistoryForCurrentStage(appNo,HcsaConsts.ROUTING_STAGE_ASO);
            }
            if(appPremisesRoutingHistoryDto != null){
                log.info(StringUtil.changeForLog("appPremisesRoutingHistoryDto.getRoleId() ：" + appPremisesRoutingHistoryDto.getRoleId()));
                rollBack(bpc,applicationViewDto,appPremisesRoutingHistoryDto.getStageId(),ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS,
                        appPremisesRoutingHistoryDto.getRoleId(),appPremisesRoutingHistoryDto.getWrkGrpId(),appPremisesRoutingHistoryDto.getActionby(),taskDto);
            }else{
                log.debug(StringUtil.changeForLog("can not get the appPremisesRoutingHistoryDto ..."));
            }
        }else{
            log.debug(StringUtil.changeForLog("do not have the applicaiton"));
        }
        log.info(StringUtil.changeForLog("the do routeToDMS end ...."));
    }

    public void changePostInsForTodoAudit(ApplicationViewDto applicationViewDto){
        if(applicationViewDto.getLicPremisesAuditDto() != null && ApplicationConsts.INCLUDE_RISK_TYPE_INSPECTION_KEY.equalsIgnoreCase(applicationViewDto.getLicPremisesAuditDto().getIncludeRiskType())){
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            AppPremisesCorrelationDto newAppPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();
            if(applicationDto.getApplicationType().equalsIgnoreCase(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK)){
                PostInsGroupDto postInsGroupDto =licenceClient.getPostInsGroupDto(applicationDto.getOriginLicenceId(),newAppPremisesCorrelationDto.getId()).getEntity();
                if(postInsGroupDto != null && postInsGroupDto.getLicInspectionGroupDto() != null && postInsGroupDto.getLicPremInspGrpCorrelationDto()!= null){
                    LicInspectionGroupDto licInspectionGroupDto = postInsGroupDto.getLicInspectionGroupDto();
                    licInspectionGroupDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    LicPremInspGrpCorrelationDto licPremInspGrpCorrelationDto = postInsGroupDto.getLicPremInspGrpCorrelationDto();
                    licPremInspGrpCorrelationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    postInsGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    licenceClient.savePostInsGroupDto(postInsGroupDto);
                }
            }
        }
    }

    private String checkAllStatus(List<ApplicationDto> applicationDtoList,List<String> applist){
        String status = ApplicationConsts.APPLICATION_STATUS_REJECTED;
        for(ApplicationDto applicationDto : applicationDtoList){
            int needChange = 1;
            for (String item:applist) {
                if(item.equals(applicationDto.getApplicationNo())){
                    needChange = 0;
                    break;
                }
            }
            if(needChange == 1){
                if(ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(applicationDto.getStatus())){
                    status = ApplicationConsts.APPLICATION_STATUS_APPROVED;
                }
            }
        }
        return status;
    }

    private void doRefunds(List<AppReturnFeeDto> saveReturnFeeDtos){
        List<AppReturnFeeDto> saveReturnFeeDtosStripe=IaisCommonUtils.genNewArrayList();
        if(saveReturnFeeDtos!=null){
            for (AppReturnFeeDto appreturn:saveReturnFeeDtos
            ) {
                ApplicationDto applicationDto=applicationMainClient.getAppByNo(appreturn.getApplicationNo()).getEntity();
                ApplicationGroupDto applicationGroupDto=applicationMainClient.getAppById(applicationDto.getAppGrpId()).getEntity();
                if(applicationGroupDto.getPayMethod()!=null&&applicationGroupDto.getPayMethod().equals(ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT)){
                    saveReturnFeeDtosStripe.add(appreturn);
                }
            }
            List<PaymentRequestDto> paymentRequestDtos= applicationViewService.eicFeStripeRefund(saveReturnFeeDtosStripe);
            for (PaymentRequestDto refund:paymentRequestDtos
            ) {
                for (AppReturnFeeDto appreturn:saveReturnFeeDtos
                ) {
                    if(appreturn.getApplicationNo().equals(refund.getReqRefNo())){
                        appreturn.setStatus(refund.getStatus());
                    }
                }
            }
        }
    }

    private void doAo1Ao2Approve(BroadcastOrganizationDto broadcastOrganizationDto, BroadcastApplicationDto broadcastApplicationDto, ApplicationDto applicationDto,List<String> appNo,TaskDto taskDto,String newCorrelationId) throws FeignException {
        String appGrpId = applicationDto.getAppGrpId();
        String status = applicationDto.getStatus();
        String appId = applicationDto.getId();
        List<ApplicationDto> applicationDtoList = applicationViewService.getApplicaitonsByAppGroupId(appGrpId);
        applicationDtoList = removeFastTrackingAndTransfer(applicationDtoList);
        applicationDtoList = removeCurrentApplicationDto(applicationDtoList,appId);
        if (IaisCommonUtils.isEmpty(applicationDtoList)) {
            return;
        } else {
            boolean flag = taskService.checkCompleteTaskByApplicationNo(applicationDtoList,newCorrelationId);
            if(flag) {
                String stageId = HcsaConsts.ROUTING_STAGE_AO3;
                String roleId = RoleConsts.USER_ROLE_AO3;
                updateCurrentApplicationStatus(applicationDtoList, appId, status);
                List<ApplicationDto> ao2AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02);
                List<ApplicationDto> ao3AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);
                List<ApplicationDto> creatTaskApplicationList = ao2AppList;
                if (IaisCommonUtils.isEmpty(ao2AppList) && !IaisCommonUtils.isEmpty(ao3AppList)) {
                    creatTaskApplicationList = ao3AppList;
                } else {
                    stageId = HcsaConsts.ROUTING_STAGE_AO2;
                    roleId = RoleConsts.USER_ROLE_AO2;
                }
                // send the task to Ao2  or Ao3
                TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(creatTaskApplicationList,
                        stageId, roleId, IaisEGPHelper.getCurrentAuditTrailDto(),taskDto.getRoleId());
                List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
                List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
                broadcastOrganizationDto.setOneSubmitTaskList(taskDtos);
                broadcastApplicationDto.setOneSubmitTaskHistoryList(appPremisesRoutingHistoryDtos);
            }
        }
    }

    private void setRiskScore(ApplicationDto applicationDto,String newCorrelationId){
        log.debug(StringUtil.changeForLog("correlationId : " + newCorrelationId));
        try {
            if(applicationDto != null && !StringUtil.isEmpty(newCorrelationId)){
                AppPremisesRecommendationDto appPreRecommentdationDtoInspectionDate = inspectionMainService.getAppRecomDtoByAppCorrId(newCorrelationId,InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
                if(appPreRecommentdationDtoInspectionDate != null){
                    HcsaRiskScoreDto hcsaRiskScoreDto = new HcsaRiskScoreDto();
                    hcsaRiskScoreDto.setAppType(applicationDto.getApplicationType());
                    hcsaRiskScoreDto.setLicId(applicationDto.getOriginLicenceId());
                    List<ApplicationDto> applicationDtos = new ArrayList<>(1);
                    applicationDtos.add(applicationDto);
                    hcsaRiskScoreDto.setApplicationDtos(applicationDtos);
                    hcsaRiskScoreDto.setServiceId(applicationDto.getServiceId());
                    HcsaRiskScoreDto entity = hcsaConfigMainClient.getHcsaRiskScoreDtoByHcsaRiskScoreDto(hcsaRiskScoreDto).getEntity();
                    String riskLevel = entity.getRiskLevel();
                    log.debug(StringUtil.changeForLog("riskLevel : " + riskLevel));
                    applicationDto.setRiskLevel(riskLevel);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    private void rejectSendNotification(ApplicationDto applicationDto) {
        String applicationNo = applicationDto.getApplicationNo();
        Date date = new Date();
        String appDate = Formatter.formatDateTime(date, "dd/MM/yyyy");
        String MohName = AppConsts.MOH_AGENCY_NAME;
        String applicationType = applicationDto.getApplicationType();
        String applicationTypeShow = MasterCodeUtil.getCodeDesc(applicationType);
        HcsaServiceDto svcDto = hcsaConfigMainClient.getHcsaServiceDtoByServiceId(applicationDto.getServiceId()).getEntity();
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        if(svcDto != null){
            svcCodeList.add(svcDto.getSvcCode());
        }
        if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType)){
            renewalSendNotification(applicationTypeShow,applicationNo,appDate,MohName,applicationDto,svcCodeList);
        }else if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType)){
            newAppSendNotification(applicationTypeShow,applicationNo,appDate,MohName,applicationDto,svcCodeList);
        }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)){
            rfcSendRejectNotification(applicationTypeShow,applicationNo,appDate,MohName,applicationDto,svcCodeList);
        }else if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){
            log.info("ao1 or ao2 send application reject email");
            try {
                sendAppealReject(applicationDto,MohName);
                log.info("reject email success");
            }catch (Exception e){
                log.error(e.getMessage()+"error",e);
            }
        }
    }

    private  void  sendAppealReject(ApplicationDto applicationDto,String MohName) throws IOException, TemplateException {
        log.info("start send email sms and msg");
        log.info(StringUtil.changeForLog("appNo: " + applicationDto.getApplicationNo()));
        String applicantName = "";
        Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
        ApplicationGroupDto applicationGroupDto =  applicationMainClient.getAppById(applicationDto.getAppGrpId()).getEntity();
        OrgUserDto orgUserDto = organizationMainClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
        if(orgUserDto != null){
            applicantName = orgUserDto.getDisplayName();
        }
        List<AppPremiseMiscDto> premiseMiscDtoList = cessationClient.getAppPremiseMiscDtoListByAppId(applicationDto.getId()).getEntity();
        String appType = "Licence";
        String appealNo = "-";
        if(premiseMiscDtoList != null){
            AppPremiseMiscDto premiseMiscDto = premiseMiscDtoList.get(0);
            if(premiseMiscDto != null){
                String oldAppId = premiseMiscDto.getRelateRecId();
                ApplicationDto oldApplication = applicationMainClient.getApplicationById(oldAppId).getEntity();
                if(oldApplication != null){
                    appType =  MasterCodeUtil.getCodeDesc(oldApplication.getApplicationType());
                    appealNo = oldApplication.getApplicationNo();
                }
                String appealType = premiseMiscDto.getAppealType();
                if(ApplicationConsts.APPEAL_TYPE_LICENCE.equals(appealType)){
                    LicenceDto licenceDto = licenceClient.getLicBylicId(premiseMiscDto.getRelateRecId()).getEntity();
                    if(licenceDto != null){
                        appealNo = licenceDto.getLicenceNo();
                        appType = "Licence";
                    }
                }
            }
        }
        if(StringUtil.isEmpty(appType)){
            appType = "Licence";
        }
        templateContent.put("ApplicantName", applicantName);
        templateContent.put("ApplicationType",  appType);
        templateContent.put("ApplicationNo", appealNo);
        templateContent.put("ApplicationDate", Formatter.formatDateTime(new Date(),"dd/MM/yyyy"));
        templateContent.put("MOH_AGENCY_NAME", MohName);
        templateContent.put("emailAddress", systemParamConfig.getSystemAddressOne());


        MsgTemplateDto emailTemplateDto = msgTemplateMainClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_EMAIL).getEntity();
        MsgTemplateDto smsTemplateDto = msgTemplateMainClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_SMS).getEntity();
        MsgTemplateDto msgTemplateDto = msgTemplateMainClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_MSG).getEntity();
        Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
        subMap.put("ApplicationType", appType);
        subMap.put("ApplicationNo", appealNo);
        String emailSubject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(),subMap);
        String smsSubject = MsgUtil.getTemplateMessageByContent(smsTemplateDto.getTemplateName(),subMap);
        String msgSubject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),subMap);

        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_EMAIL);
        emailParam.setTemplateContent(templateContent);
        emailParam.setSubject(emailSubject);
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);

        notificationHelper.sendNotification(emailParam);
        EmailParam smsParam = new EmailParam();
        smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_SMS);
        smsParam.setSubject(smsSubject);
        smsParam.setTemplateContent(templateContent);
        smsParam.setQueryCode(applicationDto.getApplicationNo());
        smsParam.setReqRefNum(applicationDto.getApplicationNo());
        smsParam.setRefId(applicationDto.getApplicationNo());
        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        notificationHelper.sendNotification(smsParam);

        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_MSG);
        msgParam.setTemplateContent(templateContent);
        msgParam.setSubject(msgSubject);
        msgParam.setQueryCode(applicationDto.getApplicationNo());
        msgParam.setReqRefNum(applicationDto.getApplicationNo());
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        HcsaServiceDto svcDto = hcsaConfigMainClient.getHcsaServiceDtoByServiceId(applicationDto.getServiceId()).getEntity();
        svcCodeList.add(svcDto.getSvcCode());
        msgParam.setSvcCodeList(svcCodeList);
        msgParam.setRefId(applicationDto.getApplicationNo());
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        notificationHelper.sendNotification(msgParam);
        log.info("end send email sms and msg");
    }

    private void rfcSendRejectNotification(String applicationTypeShow,String applicationNo,String appDate,String MohName,ApplicationDto applicationDto,List<String> svcCodeList){
        ApplicationGroupDto applicationGroupDto = applicationViewService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
        String applicantName = "";
        OrgUserDto orgUserDto = organizationMainClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
        if(orgUserDto != null){
            applicantName = orgUserDto.getDisplayName();
        }
        Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
        emailMap.put("ApplicantName", applicantName);
        emailMap.put("ApplicationType", applicationTypeShow);
        emailMap.put("ApplicationNumber", applicationNo);
        emailMap.put("ApplicationDate", appDate);
        emailMap.put("email_address", systemParamConfig.getSystemAddressOne());
        emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
        emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_004_REJECTED);
        emailParam.setTemplateContent(emailMap);
        emailParam.setQueryCode(applicationNo);
        emailParam.setReqRefNum(applicationNo);
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(applicationNo);
        Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        MsgTemplateDto rfiEmailTemplateDto = generateIdClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_004_REJECTED).getEntity();
        map.put("ApplicationType", applicationTypeShow);
        map.put("ApplicationNumber", applicationNo);
        String subject= null;
        try {
            subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(),map);
        } catch (IOException | TemplateException e) {
            log.info(e.getMessage(),e);
        }
        emailParam.setSubject(subject);
        //email
        log.info(StringUtil.changeForLog("send RFC Reject email send"));
        notificationHelper.sendNotification(emailParam);
        log.info(StringUtil.changeForLog("send RFC Reject email end"));
        //msg
        rfiEmailTemplateDto = generateIdClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_004_REJECTED_MSG).getEntity();
        subject = null;
        try {
            subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
        } catch (IOException |TemplateException e) {
            log.info(e.getMessage(),e);
        }
        emailParam.setSubject(subject);
        emailParam.setSvcCodeList(svcCodeList);
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_004_REJECTED_MSG);
        emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        emailParam.setRefId(applicationNo);
        log.info(StringUtil.changeForLog("send RFC Reject msg send"));
        notificationHelper.sendNotification(emailParam);
        log.info(StringUtil.changeForLog("send RFC Reject msg end"));
        //sms
        rfiEmailTemplateDto = generateIdClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_004_REJECTED_SMS).getEntity();
        subject = null;
        try {
            subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
        } catch (IOException |TemplateException e) {
            log.info(e.getMessage(),e);
        }
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_004_REJECTED_SMS);
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        log.info(StringUtil.changeForLog("send RFC Reject sms send"));
        notificationHelper.sendNotification(emailParam);
        log.info(StringUtil.changeForLog("send RFC Reject sms end"));
    }

    private void newAppSendNotification(String applicationTypeShow,String applicationNo,String appDate,String MohName,ApplicationDto applicationDto,List<String> svcCodeList){
        log.info(StringUtil.changeForLog("send new application notification start"));
        //send email
        ApplicationGroupDto applicationGroupDto = applicationViewService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
        if(applicationGroupDto != null) {
            String groupLicenseeId = applicationGroupDto.getLicenseeId();
            String aubmitBy = applicationGroupDto.getSubmitBy();
            log.info(StringUtil.changeForLog("send new application notification groupLicenseeId : " + groupLicenseeId));

            Map<String, Object> map = IaisCommonUtils.genNewHashMap();

            OrgUserDto orgUserDto = organizationMainClient.retrieveOrgUserAccountById(aubmitBy).getEntity();
            if (orgUserDto != null){
                map.put("ApplicantName", orgUserDto.getDisplayName());
            }

            map.put("applicationType", applicationTypeShow);
            map.put("applicationNumber", applicationNo);
            map.put("applicationDate", appDate);
            map.put("emailAddress", systemAddressOne);
            map.put("MOH_AGENCY_NAME", MohName);
            try {
//                String subject = "MOH HALP - Your "+ applicationTypeShow + ", "+ applicationNo +" is rejected ";
                Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
                subMap.put("ApplicationType", applicationTypeShow);
                subMap.put("ApplicationNumber", applicationNo);
                String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_ID,subMap);
                String smsSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_SMS_ID,subMap);
                String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_MESSAGE_ID,subMap);
                log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
                log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
                log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_ID);
                emailParam.setTemplateContent(map);
                emailParam.setQueryCode(applicationNo);
                emailParam.setReqRefNum(applicationNo);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                emailParam.setRefId(applicationNo);
                emailParam.setSubject(emailSubject);
                //send email
                log.info(StringUtil.changeForLog("send new application email"));
                notificationHelper.sendNotification(emailParam);
                //send sms
                EmailParam smsParam = new EmailParam();
                smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_SMS_ID);
                smsParam.setSubject(smsSubject);
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
                messageParam.setSubject(messageSubject);
                messageParam.setSvcCodeList(svcCodeList);
                log.info(StringUtil.changeForLog("send new application message"));
                notificationHelper.sendNotification(messageParam);
                log.info(StringUtil.changeForLog("send new application notification end"));
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }

        }
    }

    private void renewalSendNotification(String applicationTypeShow,String applicationNo,String appDate,String MohName,ApplicationDto applicationDto,List<String> svcCodeList){
        log.info(StringUtil.changeForLog("send renewal application notification start"));
        //send email
        ApplicationGroupDto applicationGroupDto = applicationViewService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
        if(applicationGroupDto != null){
            String groupLicenseeId = applicationGroupDto.getLicenseeId();
            log.info(StringUtil.changeForLog("send renewal application notification groupLicenseeId : " + groupLicenseeId));
            LicenseeDto licenseeDto = organizationMainClient.getLicenseeDtoById(groupLicenseeId).getEntity();
            if(licenseeDto != null){
                String applicantName = "";
                OrgUserDto orgUserDto = organizationMainClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                if(orgUserDto != null){
                    applicantName = orgUserDto.getDisplayName();
                }
                log.info(StringUtil.changeForLog("send renewal application notification applicantName : " + applicantName));
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                map.put("ApplicantName", applicantName);
                map.put("ApplicationType", applicationTypeShow);
                map.put("ApplicationNumber", applicationNo);
                map.put("ApplicationDate", appDate);
                map.put("emailAddress", systemAddressOne);
                map.put("MOH_AGENCY_NAME", MohName);
                try {
                    Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
                    subMap.put("ApplicationType", applicationTypeShow);
                    subMap.put("ApplicationNumber", applicationNo);
                    String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT,subMap);
                    String smsSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT_SMS,subMap);
                    String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT_MESSAGE,subMap);
                    log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
                    log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
                    log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT);
                    emailParam.setTemplateContent(map);
                    emailParam.setQueryCode(applicationNo);
                    emailParam.setReqRefNum(applicationNo);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                    emailParam.setRefId(applicationNo);
                    emailParam.setSubject(emailSubject);
                    //send email
                    log.info(StringUtil.changeForLog("send renewal application email"));
                    notificationHelper.sendNotification(emailParam);
                    //send sms
                    EmailParam smsParam = new EmailParam();
                    smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT_SMS);
                    smsParam.setSubject(smsSubject);
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
                    messageParam.setSubject(messageSubject);
                    messageParam.setSvcCodeList(svcCodeList);
                    log.info(StringUtil.changeForLog("send renewal application message"));
                    notificationHelper.sendNotification(messageParam);
                    log.info(StringUtil.changeForLog("send renewal application notification end"));
                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private String getEmailSubject(String templateId,Map<String, Object> subMap){
        String subject = "-";
        if(!StringUtil.isEmpty(templateId)){
            MsgTemplateDto emailTemplateDto = msgTemplateMainClient.getMsgTemplate(templateId).getEntity();
            if(emailTemplateDto != null){
                try {
                    if(!IaisCommonUtils.isEmpty(subMap)){
                        subject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(),subMap);
                    }else{
                        subject = emailTemplateDto.getTemplateName();
                    }
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
        return subject;
    }
}