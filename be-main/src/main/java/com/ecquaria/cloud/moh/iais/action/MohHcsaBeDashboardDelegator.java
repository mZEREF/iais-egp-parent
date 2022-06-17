package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.BeDashboardConstant;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionReportConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppStageSlaTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.PoolRoleCheckDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashAssignMeAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashAssignMeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashComAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashComPoolAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashComPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashKpiPoolAjaxQuery;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashKpiPoolQuery;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashRenewAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashRenewQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashReplyAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashReplyQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashWaitApproveAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashWaitApproveQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashWorkTeamAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashWorkTeamQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryMainService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewMainService;
import com.ecquaria.cloud.moh.iais.service.BeDashboardAjaxService;
import com.ecquaria.cloud.moh.iais.service.BeDashboardSupportService;
import com.ecquaria.cloud.moh.iais.service.BroadcastMainService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainService;
import com.ecquaria.cloud.moh.iais.service.MohHcsaBeDashboardService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryMainClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationMainClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskMainClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import com.ecquaria.cloudfeign.FeignException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Process: MohHcsaBeDashboard
 *
 * @author Shicheng
 * @date 2021/4/1 13:31
 **/
@Delegator(value = "mohHcsaBeDashboardDelegator")
@Slf4j
public class MohHcsaBeDashboardDelegator {
    private static final String CAN_APPROVE_API_URL = "http://hcsa-licence-web/hcsa-licence-web/canApproveValidation";

    @Autowired
    private TaskService taskService;

    @Autowired
    private BeDashboardSupportService beDashboardSupportService;

    @Autowired
    private InspectionMainAssignTaskService inspectionMainAssignTaskService;

    @Autowired
    private ApplicationViewMainService applicationViewService;

    @Autowired
    private GenerateIdClient generateIdClient;

    @Autowired
    private OrganizationMainClient organizationMainClient;

    @Autowired
    private MohHcsaBeDashboardService mohHcsaBeDashboardService;

    @Autowired
    private AppPremisesRoutingHistoryMainService appPremisesRoutingHistoryService;

    @Autowired
    private BroadcastMainService broadcastService;

    @Autowired
    private HcsaConfigMainClient hcsaConfigMainClient;

    @Autowired
    private AppPremisesRoutingHistoryMainClient appPremisesRoutingHistoryMainClient;

    @Autowired
    private ApplicationMainClient applicationMainClient;

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private InspectionMainService inspectionService;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private BeDashboardAjaxService beDashboardAjaxService;

    @Autowired
    private InspectionTaskMainClient inspectionTaskMainClient;

    @Autowired
    @Qualifier(value = "iaisRestTemplate")
    private RestTemplate restTemplate;

    /**
     * StartStep: hcsaBeDashboardStart
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardStart(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardStart start ...."));
        //get Privilege flag
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String privilegeFlag = mohHcsaBeDashboardService.getPrivilegeFlagByRole(loginContext);
        if(AppConsts.SUCCESS.equals(privilegeFlag)) {
            ParamUtil.setRequestAttr(bpc.request, "hcsaBeDashSysSwitchType", "app");
        } else {
            ParamUtil.setRequestAttr(bpc.request, "hcsaBeDashSysSwitchType", "task");
            //clear session
            ParamUtil.setSessionAttr(bpc.request, "dashActionValue", null);
            ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", null);
            String backFlag = ParamUtil.getRequestString(bpc.request, "dashProcessBack");
            if(!AppConsts.YES.equals(backFlag)) {
                ParamUtil.setSessionAttr(bpc.request, "dashSwitchActionValue", null);
                ParamUtil.setSessionAttr(bpc.request, "appTypeOption", null);
                ParamUtil.setSessionAttr(bpc.request, "appStatusOption", null);
                ParamUtil.setSessionAttr(bpc.request, "dashSearchParam", null);
                ParamUtil.setSessionAttr(bpc.request, "dashSearchResult", null);
                ParamUtil.setSessionAttr(bpc.request, "dashWorkGroupIds", null);
                ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", null);
                ParamUtil.setSessionAttr(bpc.request, "beDashRoleIds", null);
                ParamUtil.setSessionAttr(bpc.request, "dashRoleCheckDto", null);
                ParamUtil.setSessionAttr(bpc.request, "dashAppStatus", null);
                ParamUtil.setSessionAttr(bpc.request, "application_status", null);
            } else {
                ParamUtil.setRequestAttr(bpc.request, "dashProcessBackFlag", backFlag);
            }
            ParamUtil.setSessionAttr(bpc.request, "inspecTaskCreAndAssDto", null);
        }
        ParamUtil.setSessionAttr(bpc.request, "dashHciAddress", null);
        ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", null);
    }

    /**
     * StartStep: hcsaBeDashboardInit
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardInit(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardInit start ...."));
        String backFlag = (String)ParamUtil.getRequestAttr(bpc.request, "dashProcessBackFlag");
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INTRANET_DASHBOARD, AuditTrailConsts.FUNCTION_INTRANET_DASHBOARD);
        //set role list and current role
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(!AppConsts.YES.equals(backFlag)) {
            if (loginContext != null) {
                PoolRoleCheckDto poolRoleCheckDto = new PoolRoleCheckDto();
                poolRoleCheckDto = mohHcsaBeDashboardService.getDashRoleOption(loginContext, poolRoleCheckDto);
                ParamUtil.setSessionAttr(bpc.request, "beDashRoleIds", (Serializable) poolRoleCheckDto.getRoleOptions());
                ParamUtil.setSessionAttr(bpc.request, "dashRoleCheckDto", poolRoleCheckDto);
            }
            ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
        } else {
            ParamUtil.setRequestAttr(bpc.request, "dashProcessBackFlag", backFlag);
        }
    }

    /**
     * StartStep: hcsaBeDashboardInitPre
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardInitPre(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardInitPre start ...."));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String backFlag = (String)ParamUtil.getRequestAttr(bpc.request, "dashProcessBackFlag");
        if(loginContext != null && !AppConsts.YES.equals(backFlag)) {
            //get appType option
            List<SelectOption> appTypeOption = inspectionService.getAppTypeOption();
            SearchParam searchParam = getSearchParam(bpc, true, DashAssignMeQueryDto.class.getName());
            //get app status option
            List<SelectOption> appStatusOption = mohHcsaBeDashboardService.getAppStatusOptionByRoleAndSwitch(loginContext.getCurRoleId(), BeDashboardConstant.SWITCH_ACTION_ASSIGN_ME);
            //set app status and search filter
            if (RoleConsts.USER_ROLE_AO3.equals(loginContext.getCurRoleId()) || RoleConsts.USER_ROLE_AO3_LEAD.equals(loginContext.getCurRoleId())) {
                String application_status = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
                List<String> appStatusList = IaisCommonUtils.genNewArrayList();
                appStatusList.add(application_status);
                String appStatusStr = SqlHelper.constructInCondition("T5.STATUS", appStatusList.size());
                searchParam.addParam("application_status", appStatusStr);
                for (int i = 0; i < appStatusList.size(); i++) {
                    searchParam.addFilter("T5.STATUS" + i, appStatusList.get(i));
                }
                ParamUtil.setSessionAttr(bpc.request, "dashAppStatus", application_status);
            }
            //set filter by login info
            //role
            String curRoleId = loginContext.getCurRoleId();
            if (!StringUtil.isEmpty(curRoleId)) {
                searchParam.addFilter("dashRoleId", curRoleId, true);
            }
            //user uuid
            String userId = loginContext.getUserId();
            if (!StringUtil.isEmpty(userId)) {
                searchParam.addFilter("dashUserId", userId, true);
            }
            QueryHelp.setMainSql("intraDashboardQuery", "dashAssignMe", searchParam);
            SearchResult<DashAssignMeQueryDto> searchResult = mohHcsaBeDashboardService.getDashAssignMeResult(searchParam);
            searchResult = mohHcsaBeDashboardService.getDashAssignMeOtherData(searchResult);

            //set session
            ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
            ParamUtil.setSessionAttr(bpc.request, "dashSearchParam", searchParam);
            ParamUtil.setSessionAttr(bpc.request, "dashSearchResult", searchResult);
            ParamUtil.setSessionAttr(bpc.request, "dashSwitchActionValue", BeDashboardConstant.SWITCH_ACTION_ASSIGN_ME);
            //set session
            ParamUtil.setSessionAttr(bpc.request, "appTypeOption", (Serializable) appTypeOption);
        }
    }

    /**
     * StartStep: hcsaBeDashboardStep
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardStep(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardStep start ...."));
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        ParamUtil.setRequestAttr(bpc.request, "dashActionValue", actionValue);
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

        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String[] taskList =  ParamUtil.getMaskedStrings(bpc.request, "taskId");
        String action =  ParamUtil.getString(bpc.request, "action");
        String successStatus = "";
        String successInfo = "Success";
        if(!StringUtil.isEmpty(taskList)){
            for (String item:taskList) {
                if(StringUtil.isEmpty(item)) {
                    log.info(StringUtil.changeForLog("-------- task id is NULL----"));
                    continue;
                }
                TaskDto taskDto = taskService.getTaskById(item);
                log.info(StringUtil.changeForLog("Dashboard -------- task id "+ item));
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
                        Map<String,String> errMap = validateCanApprove(applicationViewDto);
                        if (IaisCommonUtils.isNotEmpty(errMap)) {
                            ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.FALSE);
                            ParamUtil.setRequestAttr(bpc.request,"successInfo", errMap.get("nextStage"));
                            return;
                        }
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
                            beDashboardSupportService.inspectorAo1(loginContext, applicationViewDto, taskDto);
                        }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW.equals(status)){
                            mohHcsaBeDashboardService.createReportResult(taskDto,applicationViewDto,loginContext.getUserId());
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
                        Map<String,String> errMap = validateCanApprove(applicationViewDto);
                        log.info("AO2 approve validation rslt ==> {}", errMap);
                        if (IaisCommonUtils.isNotEmpty(errMap)) {
                            ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.FALSE);
                            ParamUtil.setRequestAttr(bpc.request,"successInfo", errMap.get("nextStage"));
                            return;
                        }
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
                    Map<String,String> errMap = validateCanApprove(applicationViewDto);
                    if (IaisCommonUtils.isNotEmpty(errMap)) {
                        ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.FALSE);
                        ParamUtil.setRequestAttr(bpc.request,"successInfo", errMap.get("nextStage"));
                        return;
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
        ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
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
        CrudHelper.doPaging(searchParam, bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "dashSearchParam", searchParam);
    }

    /**
     * StartStep: hcsaBeDashboardInGroup
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardInGroup(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardInGroup start ...."));
        String switchAction = ParamUtil.getRequestString(bpc.request, "switchAction");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        SearchParam searchParam = getSearchParam(bpc, true, DashWorkTeamQueryDto.class.getName());
        //set form value
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        //app status
        List<SelectOption> appStatusOption = mohHcsaBeDashboardService.getAppStatusOptionByRoleAndSwitch(loginContext.getCurRoleId(), switchAction);
        searchParam = setFilterByDashForm(searchParam, bpc.request, switchAction, loginContext, appStatusOption,
                "T5.STATUS");
        //if not lead and approver, set userId
        workGroupIds = mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);

        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
        ParamUtil.setSessionAttr(bpc.request, "dashWorkGroupIds", (Serializable) workGroupIds);
        ParamUtil.setSessionAttr(bpc.request, "dashSwitchActionValue", switchAction);
        ParamUtil.setSessionAttr(bpc.request, "dashSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
    }

    /**
     * StartStep: hcsaBeDashboardApplicantReply
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardApplicantReply(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardApplicantReply start ...."));
        String switchAction = ParamUtil.getRequestString(bpc.request, "switchAction");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        SearchParam searchParam = getSearchParam(bpc, true, DashReplyQueryDto.class.getName());
        //set form value
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        searchParam = setFilterByDashForm(searchParam, bpc.request, switchAction, loginContext, null,
                null);
        //if not lead and approver, set userId
        workGroupIds = mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", null);
        ParamUtil.setSessionAttr(bpc.request, "dashWorkGroupIds", (Serializable) workGroupIds);
        ParamUtil.setSessionAttr(bpc.request, "dashSwitchActionValue", switchAction);
        ParamUtil.setSessionAttr(bpc.request, "dashSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
    }

    /**
     * StartStep: hcsaBeDashboardKpi
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardKpi(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardKpi start ...."));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        SearchParam searchParam = getSearchParam(bpc, true, DashKpiPoolQuery.class.getName());
        String switchAction = ParamUtil.getRequestString(bpc.request, "switchAction");
        //app status
        List<SelectOption> appStatusOption = mohHcsaBeDashboardService.getAppStatusOptionByRoleAndSwitch(loginContext.getCurRoleId(), switchAction);
        //set form value
        searchParam = setFilterByDashForm(searchParam, bpc.request, switchAction, loginContext, appStatusOption,
                "T1.STATUS");
        //get work groups
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);

        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
        ParamUtil.setSessionAttr(bpc.request, "dashSwitchActionValue", switchAction);
        ParamUtil.setSessionAttr(bpc.request, "dashSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
    }

    /**
     * StartStep: hcsaBeDashboardCommonPool
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardCommonPool(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardCommonPool start ...."));
        String dashActionValue = (String)ParamUtil.getRequestAttr(bpc.request, "dashActionValue");
        if(!StringUtil.isEmpty(dashActionValue) && MessageDigest.isEqual(dashActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_BACK.getBytes(StandardCharsets.UTF_8))) {
            ParamUtil.setRequestAttr(bpc.request, "dashActionValue", dashActionValue);
        } else {
            String switchAction = ParamUtil.getRequestString(bpc.request, "switchAction");
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            SearchParam searchParam = getSearchParam(bpc, true, DashComPoolQueryDto.class.getName());
            //set form value
            List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
            searchParam = setFilterByDashForm(searchParam, bpc.request, switchAction, loginContext, null,
                    null);
            //if not lead and approver, set userId
            workGroupIds = mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);

            ParamUtil.setSessionAttr(bpc.request, "dashWorkGroupIds", (Serializable) workGroupIds);
            ParamUtil.setSessionAttr(bpc.request, "dashSwitchActionValue", switchAction);
            ParamUtil.setSessionAttr(bpc.request, "dashSearchParam", searchParam);
            ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
        }
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", null);
    }

    private SearchParam setFilterByDashForm(SearchParam searchParam, HttpServletRequest request, String actionValue, LoginContext loginContext,
                                            List<SelectOption> appStatusOption, String appStatusKey) {
        PoolRoleCheckDto poolRoleCheckDto = (PoolRoleCheckDto)ParamUtil.getSessionAttr(request, "dashRoleCheckDto");
        String curRoleKey = ParamUtil.getRequestString(request, "beDashRoleId");
        if(!StringUtil.isEmpty(curRoleKey)) {
            Map<String, String> roleMap = poolRoleCheckDto.getRoleMap();
            String roleId = getCheckRoleIdByMap(curRoleKey, roleMap);
            if(loginContext != null) {
                loginContext.setCurRoleId(roleId);
            }
            if(!StringUtil.isEmpty(roleId)) {
                poolRoleCheckDto.setCheckCurRole(curRoleKey);
            }
        }
        String application_no = ParamUtil.getRequestString(request, "application_no");
        String application_type = ParamUtil.getRequestString(request, "application_type");
        String application_status = ParamUtil.getRequestString(request, "application_status");
        String hci_code = ParamUtil.getRequestString(request, "hci_code");
        String hci_name = ParamUtil.getRequestString(request, "hci_name");
        String hci_address = ParamUtil.getRequestString(request, "hci_address");
        if(!StringUtil.isEmpty(application_no)){
            searchParam.addFilter("application_no", application_no, true);
            ParamUtil.setSessionAttr(request, "dashFilterAppNo", application_no);
        } else {
            ParamUtil.setSessionAttr(request, "dashFilterAppNo", null);
        }
        if(!StringUtil.isEmpty(application_type)){
            searchParam.addFilter("application_type", application_type, true);
        }
        if(!StringUtil.isEmpty(application_status) &&
                (!(BeDashboardConstant.SWITCH_ACTION_COMMON.equals(actionValue)) && !(BeDashboardConstant.SWITCH_ACTION_REPLY.equals(actionValue))))
        {
            boolean flag = mohHcsaBeDashboardService.containsAppStatus(appStatusOption, application_status);
            if(flag) {
                //Filter the Common Pool Task in another place
                if (!application_status.equals(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT)) {
                    List<String> appStatus = mohHcsaBeDashboardService.getSearchAppStatus(application_status);
                    String appStatusStr = SqlHelper.constructInCondition(appStatusKey, appStatus.size());
                    searchParam.addParam("application_status", appStatusStr);
                    for(int i = 0; i < appStatus.size(); i++) {
                        searchParam.addFilter(appStatusKey + i, appStatus.get(i));
                    }
                    ParamUtil.setSessionAttr(request, "dashAppStatus", application_status);
                    ParamUtil.setSessionAttr(request, "dashCommonPoolStatus", null);
                } else {//Filter the Common Pool Task
                    searchParam.addParam("dashCommonPoolStatus", "dashCommonPoolStatus");
                    ParamUtil.setSessionAttr(request, "dashCommonPoolStatus", "dashCommonPoolStatus");
                    ParamUtil.setSessionAttr(request, "dashAppStatus", null);
                }
            } else {
                ParamUtil.setSessionAttr(request, "dashCommonPoolStatus", null);
                ParamUtil.setSessionAttr(request, "dashAppStatus", null);
            }
        } else {
            ParamUtil.setSessionAttr(request, "dashCommonPoolStatus", null);
            ParamUtil.setSessionAttr(request, "dashAppStatus", null);
        }
        if(!StringUtil.isEmpty(hci_code)){
            searchParam.addFilter("hci_code", hci_code, true);
        }
        if(!StringUtil.isEmpty(hci_name)){
            searchParam.addFilter("hci_name", hci_name, true);
        }
        if(!StringUtil.isEmpty(hci_address)){
            ParamUtil.setSessionAttr(request, "dashHciAddress", hci_address);
        } else {
            ParamUtil.setSessionAttr(request, "dashHciAddress", null);
        }
        //licence expire days
        if(BeDashboardConstant.SWITCH_ACTION_RE_RENEW.equals(actionValue)) {
            searchParam.addFilter("lic_renew_exp", systemParamConfig.getDashRenewDate(), true);
        }
        ParamUtil.setSessionAttr(request, "dashRoleCheckDto", poolRoleCheckDto);
        return searchParam;
    }

    private String getCheckRoleIdByMap(String roleIdCheck, Map<String, String> roleMap) {
        String roleId = "";
        if(roleMap != null && !StringUtil.isEmpty(roleIdCheck)){
            roleId = roleMap.get(roleIdCheck);
            if(!StringUtil.isEmpty(roleId)){
                return roleId;
            } else {
                return "";
            }
        }
        return roleId;
    }

    /**
     * StartStep: hcsaBeDashboardComAssign
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardComAssign(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardComAssign start ...."));
        String taskId = getDashTaskIdByBpc(bpc);
        if(!StringUtil.isEmpty(taskId)) {
            HcsaTaskAssignDto hcsaTaskAssignDto = (HcsaTaskAssignDto)ParamUtil.getSessionAttr(bpc.request, "hcsaTaskAssignDto");
            LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            ParamUtil.setSessionAttr(bpc.request, "inspecTaskCreAndAssDto", null);
            ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", null);
            ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", null);
            TaskDto taskDto = taskService.getTaskById(taskId);
            String appCorrelationId = taskDto.getRefNo();
            if(!StringUtil.isEmpty(appCorrelationId)) {
                ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(appCorrelationId);
                InspecTaskCreAndAssDto inspecTaskCreAndAssDto = new InspecTaskCreAndAssDto();
                inspecTaskCreAndAssDto.setTaskId(taskId);
                inspecTaskCreAndAssDto.setTaskDto(taskDto);
                //set role field
                GroupRoleFieldDto groupRoleFieldDto = inspectionMainAssignTaskService.getGroupRoleField(loginContext);
                ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
                if(applicationDto.isFastTracking()) {
                    inspecTaskCreAndAssDto.setFastTrackCheck(AppConsts.TRUE);
                }
                //set fastTrackFlag
                inspecTaskCreAndAssDto = inspectionMainAssignTaskService.setFastTrackFlag(inspecTaskCreAndAssDto, applicationDto);
                inspecTaskCreAndAssDto = inspectionMainAssignTaskService.getInspecTaskCreAndAssDto(applicationDto, loginContext, inspecTaskCreAndAssDto, hcsaTaskAssignDto);
                //set Edit Hours Flag
                inspecTaskCreAndAssDto = inspectionMainAssignTaskService.setEditHoursFlagByAppAndUser(inspecTaskCreAndAssDto, applicationDto);
                ParamUtil.setSessionAttr(bpc.request, "inspecTaskCreAndAssDto", inspecTaskCreAndAssDto);
                ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
                ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
            }
        }
    }

    private String getDashTaskIdByBpc(BaseProcessClass bpc) {
        String taskId = "";
        try{
            taskId = ParamUtil.getMaskedString(bpc.request,"taskId");
        }catch (MaskAttackException e){
            log.error(e.getMessage(), e);
            try{
                IaisEGPHelper.redirectUrl(bpc.response, "https://"+bpc.request.getServerName()+"/hcsa-licence-web/CsrfErrorPage.jsp");
            } catch (IOException ioe){
                log.error(ioe.getMessage(), ioe);
                return taskId;
            }
        }
        return taskId;
    }

    /**
     * StartStep: hcsaBeDashboardComVali
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardComVali(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardComVali start ...."));
        InspecTaskCreAndAssDto inspecTaskCreAndAssDto = (InspecTaskCreAndAssDto)ParamUtil.getSessionAttr(bpc.request, "inspecTaskCreAndAssDto");
        SearchResult<InspectionCommonPoolQueryDto> searchResult = (SearchResult) ParamUtil.getSessionAttr(bpc.request, "cPoolSearchResult");
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(BeDashboardConstant.SWITCH_ACTION_COMMON_CONFIRM.equals(actionValue)){
            inspecTaskCreAndAssDto = getValueFromPage(bpc);
            if(RoleConsts.USER_ROLE_INSPECTION_LEAD.equals(loginContext.getCurRoleId()) || RoleConsts.USER_ROLE_INSPECTIOR.equals(loginContext.getCurRoleId())){
                ValidationResult validationResult = WebValidationHelper.validateProperty(inspecTaskCreAndAssDto, AppConsts.COMMON_POOL);
                if (validationResult.isHasErrors()) {
                    Map<String, String> errorMap = validationResult.retrieveAll();
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                    WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                    ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
                } else {
                    ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
                }
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        } else if(BeDashboardConstant.SWITCH_ACTION_BACK.equals(actionValue)){
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        } else {
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.FALSE);
        }
        ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", inspecTaskCreAndAssDto);
        ParamUtil.setSessionAttr(bpc.request, "cPoolSearchResult", searchResult);
    }

    public InspecTaskCreAndAssDto getValueFromPage(BaseProcessClass bpc) {
        InspecTaskCreAndAssDto inspecTaskCreAndAssDto = (InspecTaskCreAndAssDto)ParamUtil.getSessionAttr(bpc.request, "inspecTaskCreAndAssDto");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(RoleConsts.USER_ROLE_INSPECTION_LEAD.equals(loginContext.getCurRoleId()) || RoleConsts.USER_ROLE_INSPECTIOR.equals(loginContext.getCurRoleId())){
            String inspManHours = ParamUtil.getRequestString(bpc.request, "inspManHours");
            inspecTaskCreAndAssDto.setInspManHours(inspManHours);
        }
        String[] fastTrackCommon = ParamUtil.getStrings(bpc.request, "fastTrackCommon");
        if(fastTrackCommon != null && fastTrackCommon.length > 0){
            inspecTaskCreAndAssDto.setFastTrackCheck(AppConsts.TRUE);
        } else {
            inspecTaskCreAndAssDto.setFastTrackCheck(null);
        }
        SelectOption so = new SelectOption(loginContext.getUserId(), "text");
        List<SelectOption> inspectorCheckList = IaisCommonUtils.genNewArrayList();
        inspectorCheckList.add(so);
        inspecTaskCreAndAssDto.setInspectorCheck(inspectorCheckList);
        return inspecTaskCreAndAssDto;
    }

    /**
     * StartStep: hcsaBeDashboardComConfirm
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardComConfirm(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardComConfirm start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardComDo
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardComDo(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardComDo start ...."));
        InspecTaskCreAndAssDto inspecTaskCreAndAssDto = (InspecTaskCreAndAssDto)ParamUtil.getSessionAttr(bpc.request, "inspecTaskCreAndAssDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        String saveFlag = inspectionMainAssignTaskService.routingTaskByCommonPool(applicationViewDto, inspecTaskCreAndAssDto, internalRemarks, loginContext);
        if(AppConsts.FAIL.equals(saveFlag)){
            ParamUtil.setRequestAttr(bpc.request,"taskHasBeenAssigned", AppConsts.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", inspecTaskCreAndAssDto);
    }

    /**
     * StartStep: hcsaBeDashboardWaitApprove
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardWaitApprove(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardWaitApprove start ...."));
        String switchAction = ParamUtil.getRequestString(bpc.request, "switchAction");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        SearchParam searchParam = getSearchParam(bpc, true, DashWaitApproveQueryDto.class.getName());
        //set form value
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        //app status
        List<SelectOption> appStatusOption = mohHcsaBeDashboardService.getAppStatusOptionByRoleAndSwitch(loginContext.getCurRoleId(), switchAction);
        searchParam = setFilterByDashForm(searchParam, bpc.request, switchAction, loginContext, appStatusOption,
                "T7.STATUS");
        //if not lead and approver, set userId
        workGroupIds = mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);

        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
        ParamUtil.setSessionAttr(bpc.request, "dashWorkGroupIds", (Serializable) workGroupIds);
        ParamUtil.setSessionAttr(bpc.request, "dashSwitchActionValue", switchAction);
        ParamUtil.setSessionAttr(bpc.request, "dashSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
    }

    /**
     * StartStep: hcsaBeDashboardRenewExpiry
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardRenewExpiry(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardRenewExpiry start ...."));
        String switchAction = ParamUtil.getRequestString(bpc.request, "switchAction");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        SearchParam searchParam = getSearchParam(bpc, true, DashRenewQueryDto.class.getName());
        //set form value
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        //app status
        List<SelectOption> appStatusOption = mohHcsaBeDashboardService.getAppStatusOptionByRoleAndSwitch(loginContext.getCurRoleId(), switchAction);
        searchParam = setFilterByDashForm(searchParam, bpc.request, switchAction, loginContext, appStatusOption,
                "T1.STATUS");
        //if not lead and approver, set userId
        workGroupIds = mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);

        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
        ParamUtil.setSessionAttr(bpc.request, "dashWorkGroupIds", (Serializable) workGroupIds);
        ParamUtil.setSessionAttr(bpc.request, "dashSwitchActionValue", switchAction);
        ParamUtil.setSessionAttr(bpc.request, "dashSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
    }

    /**
     * StartStep: hcsaBeDashboardAssignToMe
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardAssignToMe(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardAssignToMe start ...."));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if (loginContext != null) {
            SearchParam searchParam = getSearchParam(bpc, true, DashAssignMeQueryDto.class.getName());
            //user uuid
            String userId = loginContext.getUserId();
            if(!StringUtil.isEmpty(userId)) {
                searchParam.addFilter("dashUserId", userId, true);
            }
            //app status
            List<SelectOption> appStatusOption = mohHcsaBeDashboardService.getAppStatusOptionByRoleAndSwitch(loginContext.getCurRoleId(), BeDashboardConstant.SWITCH_ACTION_ASSIGN_ME);
            //set form value
            searchParam = setFilterByDashForm(searchParam, bpc.request, BeDashboardConstant.SWITCH_ACTION_ASSIGN_ME, loginContext, appStatusOption,
                    "T5.STATUS");
            //role
            String curRoleId = loginContext.getCurRoleId();
            if(!StringUtil.isEmpty(curRoleId)) {
                searchParam.addFilter("dashRoleId", curRoleId, true);
            }

            ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
            ParamUtil.setSessionAttr(bpc.request, "dashSearchParam", searchParam);
            ParamUtil.setSessionAttr(bpc.request, "dashSwitchActionValue", BeDashboardConstant.SWITCH_ACTION_ASSIGN_ME);
            ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
        }
    }

    /**
     * StartStep: hcsaBeDashboardQuery
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardQuery(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardQuery start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        String dashSwitchActionValue = (String)ParamUtil.getSessionAttr(bpc.request, "dashSwitchActionValue");
        String dashActionValue = (String)ParamUtil.getRequestAttr(bpc.request, "dashActionValue");
        //address for second search
        String hci_address = (String)ParamUtil.getSessionAttr(bpc.request, "dashHciAddress");
        ArrayList<String> groupNos = IaisCommonUtils.genNewArrayList();
        //get result
        if(!StringUtil.isEmpty(dashActionValue) && MessageDigest.isEqual(dashActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_BACK.getBytes(StandardCharsets.UTF_8))) {
            ParamUtil.setRequestAttr(bpc.request, "dashActionValue", dashActionValue);
        } else if(!StringUtil.isEmpty(dashSwitchActionValue) && MessageDigest.isEqual(dashSwitchActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_COMMON.getBytes(StandardCharsets.UTF_8))) {
            SearchResult<DashComPoolQueryDto> searchResult;
            if(!StringUtil.isEmpty(hci_address)) {
                //copy SearchParam for searchAllParam
                SearchParam searchAllParam = (SearchParam) CopyUtil.copyMutableObject(searchParam);
                searchAllParam.setPageSize(-1);
                //get all appGroupIds
                QueryHelp.setMainSql("intraDashboardQuery", "dashCommonTask", searchAllParam);
                searchResult = mohHcsaBeDashboardService.getDashComPoolResult(searchAllParam);
                //set all address data map for filter address
                List<String> appGroupIds = mohHcsaBeDashboardService.getComPoolAppGrpIdByResult(searchResult);
                HcsaTaskAssignDto hcsaTaskAssignDto = mohHcsaBeDashboardService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
                //filter unit no for group
                searchParam = mohHcsaBeDashboardService.setAppGrpIdsByUnitNos(searchParam, hci_address, hcsaTaskAssignDto, "T5.ID", "appGroup_list");
                QueryHelp.setMainSql("intraDashboardQuery", "dashCommonTask", searchParam);
                searchResult = mohHcsaBeDashboardService.getDashComPoolResult(searchParam);
                //set hcsaTaskAssignDto in session
                ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", hcsaTaskAssignDto);
            } else {
                QueryHelp.setMainSql("intraDashboardQuery", "dashCommonTask", searchParam);
                searchResult = mohHcsaBeDashboardService.getDashComPoolResult(searchParam);
                //clear hcsaTaskAssignDto in session
                ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", null);
            }
            searchResult = mohHcsaBeDashboardService.getDashComPoolOtherData(searchResult);
            if (searchResult != null && searchResult.getRowCount() > 0) {
                for (DashComPoolQueryDto dcp : searchResult.getRows()) {
                    groupNos.add(dcp.getAppGroupNo());
                }
            }
            //set session
            ParamUtil.setSessionAttr(bpc.request, "dashSearchResult", searchResult);
        } else if(!StringUtil.isEmpty(dashSwitchActionValue) && MessageDigest.isEqual(dashSwitchActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_KPI.getBytes(StandardCharsets.UTF_8))) {
            SearchResult<DashKpiPoolQuery> searchResult;
            if(!StringUtil.isEmpty(hci_address)) {
                //copy SearchParam for searchAllParam
                SearchParam searchAllParam = (SearchParam) CopyUtil.copyMutableObject(searchParam);
                searchAllParam.setPageSize(-1);
                //get all appGroupIds
                QueryHelp.setMainSql("intraDashboardQuery", "dashKpiTask", searchAllParam);
                searchResult = mohHcsaBeDashboardService.getDashKpiPoolResult(searchAllParam);
                //set all address data map for filter address
                List<String> appGroupIds = mohHcsaBeDashboardService.getKpiPoolAppGrpIdByResult(searchResult);
                HcsaTaskAssignDto hcsaTaskAssignDto = mohHcsaBeDashboardService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
                //filter unit no for group
                searchParam = mohHcsaBeDashboardService.setAppGrpIdsByUnitNos(searchParam, hci_address, hcsaTaskAssignDto, "T1.ID", "appGroup_list");
                QueryHelp.setMainSql("intraDashboardQuery", "dashKpiTask", searchParam);
                searchResult = mohHcsaBeDashboardService.getDashKpiPoolResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", hcsaTaskAssignDto);
            } else {
                QueryHelp.setMainSql("intraDashboardQuery", "dashKpiTask", searchParam);
                searchResult = mohHcsaBeDashboardService.getDashKpiPoolResult(searchParam);
            }
            searchResult = mohHcsaBeDashboardService.getDashKpiPoolOtherData(searchResult);
            if (searchResult != null && searchResult.getRowCount() > 0) {
                for (DashKpiPoolQuery dcp : searchResult.getRows()) {
                    groupNos.add(dcp.getAppGroupNo());
                }
            }
            //set session
            ParamUtil.setSessionAttr(bpc.request, "dashSearchResult", searchResult);
        } else if(!StringUtil.isEmpty(dashSwitchActionValue) && MessageDigest.isEqual(dashSwitchActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_ASSIGN_ME.getBytes(StandardCharsets.UTF_8))) {
            SearchResult<DashAssignMeQueryDto> searchResult;
            if(!StringUtil.isEmpty(hci_address)) {
                //copy SearchParam for searchAllParam
                SearchParam searchAllParam = (SearchParam) CopyUtil.copyMutableObject(searchParam);
                searchAllParam.setPageSize(-1);
                //get all appGroupIds
                QueryHelp.setMainSql("intraDashboardQuery", "dashAssignMe", searchAllParam);
                searchResult = mohHcsaBeDashboardService.getDashAssignMeResult(searchAllParam);
                //set all address data map for filter address
                List<String> appGroupIds = mohHcsaBeDashboardService.getAssignMeAppGrpIdByResult(searchResult);
                HcsaTaskAssignDto hcsaTaskAssignDto = mohHcsaBeDashboardService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
                //filter unit no for group
                searchParam = mohHcsaBeDashboardService.setAppGrpIdsByUnitNos(searchParam, hci_address, hcsaTaskAssignDto, "T5.ID", "appGroup_list");
                QueryHelp.setMainSql("intraDashboardQuery", "dashAssignMe", searchParam);
                searchResult = mohHcsaBeDashboardService.getDashAssignMeResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", hcsaTaskAssignDto);
            } else {
                QueryHelp.setMainSql("intraDashboardQuery", "dashAssignMe", searchParam);
                searchResult = mohHcsaBeDashboardService.getDashAssignMeResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", null);
            }
            searchResult = mohHcsaBeDashboardService.getDashAssignMeOtherData(searchResult);
            if (searchResult != null && searchResult.getRowCount() > 0) {
                for (DashAssignMeQueryDto dcp : searchResult.getRows()) {
                    groupNos.add(dcp.getAppGroupNo());
                }
            }
            //set session
            ParamUtil.setSessionAttr(bpc.request, "dashSearchResult", searchResult);
        } else if(!StringUtil.isEmpty(dashSwitchActionValue) && MessageDigest.isEqual(dashSwitchActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_GROUP.getBytes(StandardCharsets.UTF_8))) {
            SearchResult<DashWorkTeamQueryDto> searchResult;
            if(!StringUtil.isEmpty(hci_address)) {
                //copy SearchParam for searchAllParam
                SearchParam searchAllParam = (SearchParam) CopyUtil.copyMutableObject(searchParam);
                searchAllParam.setPageSize(-1);
                //get all appGroupIds
                QueryHelp.setMainSql("intraDashboardQuery", "dashSupervisorTask", searchAllParam);
                searchResult = mohHcsaBeDashboardService.getDashWorkTeamResult(searchAllParam);
                //set all address data map for filter address
                List<String> appGroupIds = mohHcsaBeDashboardService.getSuperPoolAppGrpIdByResult(searchResult);
                HcsaTaskAssignDto hcsaTaskAssignDto = mohHcsaBeDashboardService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
                //filter unit no for group
                searchParam = mohHcsaBeDashboardService.setAppGrpIdsByUnitNos(searchParam, hci_address, hcsaTaskAssignDto, "T5.ID", "appGroup_list");
                QueryHelp.setMainSql("intraDashboardQuery", "dashSupervisorTask", searchParam);
                searchResult = mohHcsaBeDashboardService.getDashWorkTeamResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", hcsaTaskAssignDto);
            } else {
                QueryHelp.setMainSql("intraDashboardQuery", "dashSupervisorTask", searchParam);
                searchResult = mohHcsaBeDashboardService.getDashWorkTeamResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", null);
            }
            searchResult = mohHcsaBeDashboardService.getDashWorkTeamOtherData(searchResult);
            if (searchResult != null && searchResult.getRowCount() > 0) {
                for (DashWorkTeamQueryDto dcp : searchResult.getRows()) {
                    groupNos.add(dcp.getAppGroupNo());
                }
            }
            //set session
            ParamUtil.setSessionAttr(bpc.request, "dashSearchResult", searchResult);
        } else if(!StringUtil.isEmpty(dashSwitchActionValue) && MessageDigest.isEqual(dashSwitchActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_REPLY.getBytes(StandardCharsets.UTF_8))) {
            SearchResult<DashReplyQueryDto> searchResult;
            if(!StringUtil.isEmpty(hci_address)) {
                //copy SearchParam for searchAllParam
                SearchParam searchAllParam = (SearchParam) CopyUtil.copyMutableObject(searchParam);
                searchAllParam.setPageSize(-1);
                //get all appGroupIds
                QueryHelp.setMainSql("intraDashboardQuery", "dashAppReplyTask", searchAllParam);
                searchResult = mohHcsaBeDashboardService.getDashReplyResult(searchAllParam);
                //set all address data map for filter address
                List<String> appGroupIds = mohHcsaBeDashboardService.getReplyAppGrpIdByResult(searchResult);
                HcsaTaskAssignDto hcsaTaskAssignDto = mohHcsaBeDashboardService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
                //filter unit no for group
                searchParam = mohHcsaBeDashboardService.setAppGrpIdsByUnitNos(searchParam, hci_address, hcsaTaskAssignDto, "T7.ID", "appGroup_list");
                QueryHelp.setMainSql("intraDashboardQuery", "dashAppReplyTask", searchParam);
                searchResult = mohHcsaBeDashboardService.getDashReplyResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", hcsaTaskAssignDto);
            } else {
                QueryHelp.setMainSql("intraDashboardQuery", "dashAppReplyTask", searchParam);
                searchResult = mohHcsaBeDashboardService.getDashReplyResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", null);
            }
            searchResult = mohHcsaBeDashboardService.getDashReplyOtherData(searchResult);
            if (searchResult != null && searchResult.getRowCount() > 0) {
                for (DashReplyQueryDto dcp : searchResult.getRows()) {
                    groupNos.add(dcp.getAppGroupNo());
                }
            }
            //set session
            ParamUtil.setSessionAttr(bpc.request, "dashSearchResult", searchResult);
        } else if(!StringUtil.isEmpty(dashSwitchActionValue) && MessageDigest.isEqual(dashSwitchActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_WAIT.getBytes(StandardCharsets.UTF_8))) {
            SearchResult<DashWaitApproveQueryDto> searchResult;
            if(!StringUtil.isEmpty(hci_address)) {
                //copy SearchParam for searchAllParam
                SearchParam searchAllParam = (SearchParam) CopyUtil.copyMutableObject(searchParam);
                searchAllParam.setPageSize(-1);
                //get all appGroupIds
                QueryHelp.setMainSql("intraDashboardQuery", "dashWaitApproveTask", searchAllParam);
                searchResult = mohHcsaBeDashboardService.getDashWaitApproveResult(searchAllParam);
                //set all address data map for filter address
                List<String> appGroupIds = mohHcsaBeDashboardService.getWaitApproveAppGrpIdByResult(searchResult);
                HcsaTaskAssignDto hcsaTaskAssignDto = mohHcsaBeDashboardService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
                //filter unit no for group
                searchParam = mohHcsaBeDashboardService.setAppGrpIdsByUnitNos(searchParam, hci_address, hcsaTaskAssignDto, "T7.ID", "appGroup_list");
                QueryHelp.setMainSql("intraDashboardQuery", "dashWaitApproveTask", searchParam);
                searchResult = mohHcsaBeDashboardService.getDashWaitApproveResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", hcsaTaskAssignDto);
            } else {
                QueryHelp.setMainSql("intraDashboardQuery", "dashWaitApproveTask", searchParam);
                searchResult = mohHcsaBeDashboardService.getDashWaitApproveResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", null);
            }
            searchResult = mohHcsaBeDashboardService.getDashWaitApproveOtherData(searchResult);
            if (searchResult != null && searchResult.getRowCount() > 0) {
                for (DashWaitApproveQueryDto dcp : searchResult.getRows()) {
                    groupNos.add(dcp.getAppGroupNo());
                }
            }
            //set session
            ParamUtil.setSessionAttr(bpc.request, "dashSearchResult", searchResult);
        } else if(!StringUtil.isEmpty(dashSwitchActionValue) && MessageDigest.isEqual(dashSwitchActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_RE_RENEW.getBytes(StandardCharsets.UTF_8))) {
            SearchResult<DashRenewQueryDto> searchResult;
            if(!StringUtil.isEmpty(hci_address)) {
                //copy SearchParam for searchAllParam
                SearchParam searchAllParam = (SearchParam) CopyUtil.copyMutableObject(searchParam);
                searchAllParam.setPageSize(-1);
                //get all appGroupIds
                QueryHelp.setMainSql("intraDashboardQuery", "dashAppRenewTask", searchAllParam);
                searchResult = mohHcsaBeDashboardService.getDashRenewResult(searchAllParam);
                //set all address data map for filter address
                List<String> appGroupIds = mohHcsaBeDashboardService.getRenewAppGrpIdByResult(searchResult);
                HcsaTaskAssignDto hcsaTaskAssignDto = mohHcsaBeDashboardService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
                //filter unit no for group
                searchParam = mohHcsaBeDashboardService.setAppGrpIdsByUnitNos(searchParam, hci_address, hcsaTaskAssignDto, "T1.ID", "appGroup_list");
                QueryHelp.setMainSql("intraDashboardQuery", "dashAppRenewTask", searchParam);
                searchResult = mohHcsaBeDashboardService.getDashRenewResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", hcsaTaskAssignDto);
            } else {
                QueryHelp.setMainSql("intraDashboardQuery", "dashAppRenewTask", searchParam);
                searchResult = mohHcsaBeDashboardService.getDashRenewResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", null);
            }
            searchResult = mohHcsaBeDashboardService.getDashRenewOtherData(searchResult);
            if (searchResult != null && searchResult.getRowCount() > 0) {
                for (DashRenewQueryDto dcp : searchResult.getRows()) {
                    groupNos.add(dcp.getAppGroupNo());
                }
            }
            //set session
            ParamUtil.setSessionAttr(bpc.request, "dashSearchResult", searchResult);
        }
        ParamUtil.setSessionAttr(bpc.request, "dashSearchParam", searchParam);
        // For expand CR
//        expandAppGroup(bpc.request, groupNos);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false, null);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc, boolean isNew, String className){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "dashSearchParam");
        int pageSize = SystemParamUtil.getDefaultPageSize();
        if(searchParam == null || isNew){
            searchParam = new SearchParam(className);
            searchParam.setPageSize(pageSize);
            searchParam.setPageNo(1);
            searchParam.setSort("SUBMIT_DT", SearchParam.ASCENDING);
            ParamUtil.setSessionAttr(bpc.request, "dashHciAddress", null);
        }
        return searchParam;
    }

    /**
     * @Function: Routing Task
     *
     * @param bpc
     */
    private void routingTask(BaseProcessClass bpc, String stageId, String appStatus, String roleId, ApplicationViewDto applicationViewDto, TaskDto taskDto) throws FeignException, CloneNotSupportedException {

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
                if(InspectionReportConstants.REJECTED.equals(recomDecision) || "reject".equals(recomDecision) || InspectionReportConstants.RFC_REJECTED.equals(recomDecision)){
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
        beDashboardSupportService.changePostInsForTodoAudit(applicationViewDto);
        //set risk score
        beDashboardSupportService.setRiskScore(applicationDto,newCorrelationId);
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
                                    beDashboardSupportService.doRefunds(saveReturnFeeDtos);
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
        taskDto = beDashboardSupportService.completedTask(taskDto);
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
                            taskDto.getRefNo(),TaskConsts.TASK_STATUS_PENDING,appPremisesRoutingHistoryDto1.getWrkGrpId(),
                            appPremisesRoutingHistoryDto1.getActionby(),new Date(),null,0,TaskConsts.TASK_PROCESS_URL_MAIN_FLOW,roleId,
                            IaisEGPHelper.getCurrentAuditTrailDto());
                    broadcastOrganizationDto.setCreateTask(newTaskDto);
                    //delete workgroup
                    BroadcastOrganizationDto broadcastOrganizationDto1 = broadcastService.getBroadcastOrganizationDto(
                            applicationDto.getApplicationNo(),AppConsts.DOMAIN_TEMPORARY);

                    WorkingGroupDto workingGroupDto = broadcastOrganizationDto1.getWorkingGroupDto();
                    broadcastOrganizationDto.setRollBackworkingGroupDto((WorkingGroupDto) CopyUtil.copyMutableObject(workingGroupDto));
                    workingGroupDto = beDashboardSupportService.changeStatusWrokGroup(workingGroupDto,AppConsts.COMMON_STATUS_DELETED);
                    broadcastOrganizationDto.setWorkingGroupDto(workingGroupDto);
                    List<UserGroupCorrelationDto> userGroupCorrelationDtos = broadcastOrganizationDto1.getUserGroupCorrelationDtoList();
                    List<UserGroupCorrelationDto> cloneUserGroupCorrelationDtos = IaisCommonUtils.genNewArrayList();
                    CopyUtil.copyMutableObjectList(userGroupCorrelationDtos,cloneUserGroupCorrelationDtos);
                    broadcastOrganizationDto.setRollBackUserGroupCorrelationDtoList(cloneUserGroupCorrelationDtos);
                    userGroupCorrelationDtos = beDashboardSupportService.changeStatusUserGroupCorrelationDtos(userGroupCorrelationDtos,AppConsts.COMMON_STATUS_DELETED);
                    broadcastOrganizationDto.setUserGroupCorrelationDtoList(userGroupCorrelationDtos);
                }else{
                    throw new IaisRuntimeException("This getAppPremisesCorrelationId can not get the broadcast -- >:"+applicationViewDto.getAppPremisesCorrelationId());
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
            if(!ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(appStatus)){
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
            applicationDtoList = beDashboardSupportService.removeFastTracking(applicationDtoList);
            boolean isAllSubmit = applicationViewService.isOtherApplicaitonSubmit(applicationDtoList,applicationDtoIds,
                    appStatus);
            log.info(StringUtil.changeForLog("isAllSubmit is " + isAllSubmit));
            if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(appStatus)){
                //send reject email
                try{
                    beDashboardSupportService.rejectSendNotification(applicationDto);
                }catch (Exception e){
                    log.error(StringUtil.changeForLog("send reject notification error"),e);
                }
            }
            if(isAllSubmit || applicationDto.isFastTracking() || isAo1Ao2Approve){
                if(isAo1Ao2Approve){
                    beDashboardSupportService.doAo1Ao2Approve(broadcastOrganizationDto,broadcastApplicationDto,applicationDto,applicationDtoIds,taskDto,newCorrelationId);
                }

                boolean needUpdateGroup = applicationViewService.isOtherApplicaitonSubmit(applicationDtoList, applicationDtoIds,
                        ApplicationConsts.APPLICATION_STATUS_APPROVED, ApplicationConsts.APPLICATION_STATUS_REJECTED);
                if(needUpdateGroup || applicationDto.isFastTracking()){
                    //update application Group status
                    ApplicationGroupDto applicationGroupDto = applicationViewService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
                    broadcastApplicationDto.setRollBackApplicationGroupDto((ApplicationGroupDto)CopyUtil.copyMutableObject(applicationGroupDto));
                    String appStatusIsAllRejected = beDashboardSupportService.checkAllStatus(saveApplicationDtoList,applicationDtoIds);
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
                    Map<String, String> returnFee = (Map<String, String>)ParamUtil.getSessionAttr(bpc.request, "BackendInboxReturnFee");
                    beDashboardSupportService.updateCurAppStatusByLicensee(returnFee, saveApplicationDtoList, licenseeId);

                    for (ApplicationDto viewitem:saveApplicationDtoList
                    ) {
                        log.info(StringUtil.changeForLog("****viewitem ***** " + viewitem.getApplicationNo()));
                    }
                    if(needUpdateGroup){
                        //get and set return fee
                        saveApplicationDtoList = hcsaConfigMainClient.returnFee(saveApplicationDtoList).getEntity();
                        //save return fee
                        beDashboardSupportService.saveRejectReturnFee(saveApplicationDtoList,broadcastApplicationDto);
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
            } else if (ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationType)) {
                if (ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus)) {
                    broadcastApplicationDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
                }
            }
        }
        //set appSvcVehicleDto
        broadcastApplicationDto = broadcastService.setAppSvcVehicleDtoByAppView(broadcastApplicationDto, applicationViewDto, appStatus, applicationType);
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
        //if Giro payment fail
        ApplicationGroupDto appGroupDtoView = applicationViewDto.getApplicationGroupDto();
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType) ||
                ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType) ||
                ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)
        ) {
            if (ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus)) {
                if (ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_FAIL.equals(appGroupDtoView.getPmtStatus()) ||
                        ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_FAIL_REMIND_OK.equals(appGroupDtoView.getPmtStatus()) ||
                        ApplicationConsts.PAYMENT_STATUS_PENDING_GIRO.equals(appGroupDtoView.getPmtStatus())) {
                    broadcastApplicationDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_GIRO_PAYMENT_FAIL);
                } else if (ApplicationConsts.PAYMENT_STATUS_GIRO_RETRIGGER.equals(appGroupDtoView.getPmtStatus())) {
                    broadcastApplicationDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_PAYMENT_RESUBMIT);
                }
            }
        } else if (ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationType)) {
            if (ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus)) {
                broadcastApplicationDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
            }
        }
        broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,bpc.process,submissionId);
        broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,bpc.process,submissionId);
        //0062460 update FE  application status.
        applicationViewService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());
    }

    /**
     * @Function: Route Back
     *
     * @param bpc
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
        taskDto = beDashboardSupportService.completedTask(taskDto);
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
                taskDto.getRefNo(),TaskConsts.TASK_STATUS_PENDING,wrkGpId, userId,new Date(),null,0,TaskUrl,roleId,
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

    /**
     * @Function: replay
     *
     * @param bpc
     */
    private void replay(BaseProcessClass bpc, ApplicationViewDto applicationViewDto, TaskDto taskDto) throws FeignException, CloneNotSupportedException {
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
     */
    public void routeToDMS(BaseProcessClass bpc, ApplicationViewDto applicationViewDto, TaskDto taskDto) throws CloneNotSupportedException {
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
                appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryForCurrentStage(appNo,HcsaConsts.ROUTING_STAGE_ASO);
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

    private void expandAppGroup(HttpServletRequest request, ArrayList<String> groupNos) {
        if (IaisCommonUtils.isEmpty(groupNos)) {
            return;
        }
        SearchResult result = (SearchResult) ParamUtil.getSessionAttr(request, "dashSearchResult");
        if (result == null || result.getRowCount() == 0) {
            return;
        }
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        Map<String, String> taskIdMap = IaisCommonUtils.genNewHashMap();
        String dashFilterAppNo = (String)ParamUtil.getSessionAttr(request, "dashFilterAppNo");
        String dashAppStatus = (String)ParamUtil.getSessionAttr(request, "dashAppStatus");
        HcsaTaskAssignDto hcsaTaskAssignDto = (HcsaTaskAssignDto)ParamUtil.getSessionAttr(request, "hcsaTaskAssignDto");
        //address for second search
        String hci_address = (String)ParamUtil.getSessionAttr(request, "dashHciAddress");
        String switchAction = request.getParameter("switchActionParam");
        if (StringUtil.isEmpty(switchAction)) {
            switchAction = (String)ParamUtil.getSessionAttr(request, "dashSwitchActionValue");
        }
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        SearchParam searchParamGroup = (SearchParam) ParamUtil.getSessionAttr(request, "dashSearchParam");
        //set dash support flag
        map.put("dashSupportFlag", AppConsts.FALSE);
        if (BeDashboardConstant.SWITCH_ACTION_COMMON.equals(switchAction)) {
            map = beDashboardAjaxService.getCommonDropdownResultOnce(groupNos, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo,
                    hcsaTaskAssignDto, hci_address);
            SearchResult<DashComPoolAjaxQueryDto> ajaxResult = (SearchResult<DashComPoolAjaxQueryDto>) map.get("ajaxResult");
            if (ajaxResult != null && ajaxResult.getRowCount() > 0) {
                for (DashComPoolAjaxQueryDto dto : ajaxResult.getRows()) {
                    taskIdMap.put(dto.getApplicationNo(), dto.getTaskId());
                }
            }
        } else if(BeDashboardConstant.SWITCH_ACTION_ASSIGN_ME.equals(switchAction)) {
            map = beDashboardAjaxService.getAssignMeDropdownResultOnce(groupNos, loginContext, map, searchParamGroup, dashFilterAppNo, dashAppStatus,
                    hcsaTaskAssignDto, hci_address);
            //set dash support flag
            if(loginContext != null && map != null) {
                String curRole = loginContext.getCurRoleId();
                if(!StringUtil.isEmpty(curRole)) {
                    if(curRole.contains(RoleConsts.USER_ROLE_AO1) ||
                            curRole.contains(RoleConsts.USER_ROLE_AO2) ||
                            curRole.contains(RoleConsts.USER_ROLE_AO3)
                    ) {
                        map.put("dashSupportFlag", AppConsts.TRUE);
                    }
                }
            }
            SearchResult<DashAssignMeAjaxQueryDto> ajaxResult = (SearchResult<DashAssignMeAjaxQueryDto>) map.get("ajaxResult");
            if (ajaxResult != null && ajaxResult.getRowCount() > 0) {
                for (DashAssignMeAjaxQueryDto dto : ajaxResult.getRows()) {
                    taskIdMap.put(dto.getApplicationNo(), dto.getTaskId());
                }
            }
        } else if(BeDashboardConstant.SWITCH_ACTION_REPLY.equals(switchAction)) {
            map = beDashboardAjaxService.getReplyDropdownResultOnce(groupNos, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo,
                    hcsaTaskAssignDto, hci_address);
            SearchResult<DashReplyAjaxQueryDto> ajaxResult = (SearchResult<DashReplyAjaxQueryDto>) map.get("ajaxResult");
            if (ajaxResult != null && ajaxResult.getRowCount() > 0) {
                for (DashReplyAjaxQueryDto dto : ajaxResult.getRows()) {
                    taskIdMap.put(dto.getApplicationNo(), dto.getTaskId());
                }
            }
        } else if(BeDashboardConstant.SWITCH_ACTION_KPI.equals(switchAction)) {
            map = beDashboardAjaxService.getKpiDropdownResultOnce(groupNos, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo, dashAppStatus,
                    hcsaTaskAssignDto, hci_address);
            SearchResult<DashKpiPoolAjaxQuery> ajaxResult = (SearchResult<DashKpiPoolAjaxQuery>) map.get("ajaxResult");
            if (ajaxResult != null && ajaxResult.getRowCount() > 0) {
                for (DashKpiPoolAjaxQuery dto : ajaxResult.getRows()) {
                    taskIdMap.put(dto.getApplicationNo(), dto.getTaskId());
                }
            }
        } else if(BeDashboardConstant.SWITCH_ACTION_RE_RENEW.equals(switchAction)) {
            map = beDashboardAjaxService.getRenewDropdownResultOnce(groupNos, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo, dashAppStatus,
                    hcsaTaskAssignDto, hci_address);
            SearchResult<DashRenewAjaxQueryDto> ajaxResult = (SearchResult<DashRenewAjaxQueryDto>) map.get("ajaxResult");
            if (ajaxResult != null && ajaxResult.getRowCount() > 0) {
                for (DashRenewAjaxQueryDto dto : ajaxResult.getRows()) {
                    taskIdMap.put(dto.getApplicationNo(), dto.getTaskId());
                }
            }
        } else if(BeDashboardConstant.SWITCH_ACTION_WAIT.equals(switchAction)) {
            map = beDashboardAjaxService.getWaitApproveDropResultOnce(groupNos, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo, dashAppStatus,
                    hcsaTaskAssignDto, hci_address);
            SearchResult<DashWaitApproveAjaxQueryDto> ajaxResult = (SearchResult<DashWaitApproveAjaxQueryDto>) map.get("ajaxResult");
            if (ajaxResult != null && ajaxResult.getRowCount() > 0) {
                for (DashWaitApproveAjaxQueryDto dto : ajaxResult.getRows()) {
                    taskIdMap.put(dto.getApplicationNo(), dto.getTaskId());
                }
            }
        } else if(BeDashboardConstant.SWITCH_ACTION_GROUP.equals(switchAction)) {
            String dashCommonPoolStatus = (String)ParamUtil.getSessionAttr(request, "dashCommonPoolStatus");
            map = beDashboardAjaxService.getWorkTeamDropdownResultOnce(groupNos, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo,
                    dashCommonPoolStatus, dashAppStatus, hcsaTaskAssignDto, hci_address);
            SearchResult<DashWorkTeamAjaxQueryDto> ajaxResult = (SearchResult<DashWorkTeamAjaxQueryDto>) map.get("ajaxResult");
            if (ajaxResult != null && ajaxResult.getRowCount() > 0) {
                for (DashWorkTeamAjaxQueryDto dto : ajaxResult.getRows()) {
                    taskIdMap.put(dto.getApplicationNo(), dto.getTaskId());
                }
            }
        }
        //Retrieve tasks
        List<String> taskIds = IaisCommonUtils.genNewArrayList();
        taskIds.addAll(taskIdMap.values());
        List<TaskDto> taskDtoList = taskService.getTaskList(taskIds);
        Map<String, TaskDto> taskMap = IaisCommonUtils.genNewHashMap();
        Map<String, String> tskParamMap = IaisCommonUtils.genNewHashMap();
        Map<String, String> slaParamMap = IaisCommonUtils.genNewHashMap();
        for (TaskDto td : taskDtoList) {
            taskMap.put(td.getId(), td);
            tskParamMap.put(td.getApplicationNo(), td.getTaskKey());

        }
        Map<String, AppPremisesRoutingHistoryDto> hisMap = appPremisesRoutingHistoryMainClient
                .getAppPremisesRoutingHistoriesSubStage(tskParamMap).getEntity();
        for (TaskDto td : taskDtoList) {
            String stage;
            if (HcsaConsts.ROUTING_STAGE_INS.equals(td.getTaskKey())) {
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = hisMap.get(td.getApplicationNo());
                stage = appPremisesRoutingHistoryDto.getSubStage();
            } else {
                stage = td.getTaskKey();
            }
            slaParamMap.put(td.getApplicationNo(), stage);
        }
        List<AppStageSlaTrackingDto> slaList = inspectionTaskMainClient.getSlaTrackByAppNoStageIds(slaParamMap).getEntity();
        Map<String, AppStageSlaTrackingDto> slaTrackingMap = IaisCommonUtils.genNewHashMap();
        for (AppStageSlaTrackingDto ass : slaList) {
            slaTrackingMap.put(ass.getApplicationNo() + ass.getStageId(), ass);
        }
        if (BeDashboardConstant.SWITCH_ACTION_COMMON.equals(switchAction)) {
            setDashComPoolUrl(map, loginContext, taskMap, hisMap, slaTrackingMap);
        } else if (BeDashboardConstant.SWITCH_ACTION_ASSIGN_ME.equals(switchAction)) {
            setDashAssignMeUrl(map, request, loginContext, taskMap, hisMap, slaTrackingMap);
        } else if (BeDashboardConstant.SWITCH_ACTION_REPLY.equals(switchAction)) {
            setReplyPoolUrl(map);
        } else if (BeDashboardConstant.SWITCH_ACTION_KPI.equals(switchAction)) {
            setDashKpiPoolUrl(map, request, loginContext, taskMap, hisMap, slaTrackingMap);
        } else if (BeDashboardConstant.SWITCH_ACTION_RE_RENEW.equals(switchAction)) {
            setDashRenewPoolUrl(map, request, loginContext, taskMap, hisMap, slaTrackingMap);
        } else if (BeDashboardConstant.SWITCH_ACTION_WAIT.equals(switchAction)) {
            setDashWaitApproveUrl(map, request, loginContext, taskMap, hisMap, slaTrackingMap);
        } else if (BeDashboardConstant.SWITCH_ACTION_GROUP.equals(switchAction)) {
            setWorkTeamPoolUrl(map, taskMap, hisMap, slaTrackingMap);
        }

        ParamUtil.setRequestAttr(request, "expandAppDropdownMap", map);
    }

    private Map<String, Object> setDashComPoolUrl(Map<String, Object> map, LoginContext loginContext,
                  Map<String, TaskDto> taskMap, Map<String, AppPremisesRoutingHistoryDto> hisMap,
                  Map<String, AppStageSlaTrackingDto> slaTrackingMap) {
        String roleId = "";
        if(loginContext != null && !StringUtil.isEmpty(loginContext.getCurRoleId())) {
            log.info(StringUtil.changeForLog("Dashboard Common Pool Current Role =====" + loginContext.getCurRoleId()));
            roleId = loginContext.getCurRoleId();
        }
        List<HcsaSvcKpiDto> kpiConfList = hcsaConfigMainClient.retrieveForDashboard().getEntity();
        if(map != null) {
            SearchResult<DashComPoolAjaxQueryDto> ajaxResult = (SearchResult<DashComPoolAjaxQueryDto>) map.get("ajaxResult");
            if(ajaxResult != null) {
                List<DashComPoolAjaxQueryDto> dashComPoolAjaxQueryDtos = ajaxResult.getRows();
                if(!IaisCommonUtils.isEmpty(dashComPoolAjaxQueryDtos)) {
                    Set<String> workGroupIds = IaisCommonUtils.genNewHashSet();
                    if (loginContext != null && !IaisCommonUtils.isEmpty(loginContext.getWrkGrpIds())) {
                        workGroupIds = loginContext.getWrkGrpIds();
                    }
                    for(DashComPoolAjaxQueryDto dashComPoolAjaxQueryDto : dashComPoolAjaxQueryDtos) {
                        //task is current work
                        TaskDto taskDto = taskMap.get(dashComPoolAjaxQueryDto.getTaskId());
                        //set kpi color
                        String color = getKpiColorByTask(taskDto, dashComPoolAjaxQueryDto, hisMap, kpiConfList, slaTrackingMap);
                        dashComPoolAjaxQueryDto.setKpiColor(color);
                        if(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN.equals(roleId)) {
                            dashComPoolAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                        } else if (workGroupIds.contains(taskDto.getWkGrpId()) && roleId.equals(taskDto.getRoleId())) {
                            //set mask task Id
                            String maskId = MaskUtil.maskValue("taskId", dashComPoolAjaxQueryDto.getTaskId());
                            dashComPoolAjaxQueryDto.setTaskMaskId(maskId);
                            dashComPoolAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_COMMON_POOL_DO);
                        } else {
                            dashComPoolAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                        }
                    }
                }
            }
        }
        return map;
    }

    private Map<String, Object> setDashAssignMeUrl(Map<String, Object> map, HttpServletRequest request, LoginContext loginContext,
                       Map<String, TaskDto> taskMap, Map<String, AppPremisesRoutingHistoryDto> hisMap,
                       Map<String, AppStageSlaTrackingDto> slaTrackingMap) {
        if (map != null) {
            SearchResult<DashAssignMeAjaxQueryDto> ajaxResult = (SearchResult<DashAssignMeAjaxQueryDto>) map.get("ajaxResult");
            if (ajaxResult != null) {
                List<DashAssignMeAjaxQueryDto> dashAssignMeAjaxQueryDtos = ajaxResult.getRows();
                if (!IaisCommonUtils.isEmpty(dashAssignMeAjaxQueryDtos)) {
                    List<HcsaSvcKpiDto> kpiConfList = hcsaConfigMainClient.retrieveForDashboard().getEntity();
                    for (DashAssignMeAjaxQueryDto dashAssignMeAjaxQueryDto : dashAssignMeAjaxQueryDtos) {
                        //task is current work
                        if (!StringUtil.isEmpty(dashAssignMeAjaxQueryDto.getTaskId())) {
                            TaskDto taskDto = taskMap.get(dashAssignMeAjaxQueryDto.getTaskId());
                            //set kpi color
                            String color = getKpiColorByTask(taskDto, dashAssignMeAjaxQueryDto, hisMap, kpiConfList, slaTrackingMap);
                            dashAssignMeAjaxQueryDto.setKpiColor(color);
                            //set mask task Id
                            String maskId = MaskUtil.maskValue("taskId", dashAssignMeAjaxQueryDto.getTaskId());
                            dashAssignMeAjaxQueryDto.setTaskMaskId(maskId);
                            dashAssignMeAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_PROCESS);
                            String dashTaskUrl = generateProcessUrl(taskDto.getProcessUrl(), request, maskId);
                            dashAssignMeAjaxQueryDto.setDashTaskUrl(dashTaskUrl);
                        } else {
                            dashAssignMeAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                        }
                    }
                }
            }
        }
        return map;
    }

    private Map<String, Object> setReplyPoolUrl(Map<String, Object> map) {
        if(map != null) {
            SearchResult<DashReplyAjaxQueryDto> ajaxResult = (SearchResult<DashReplyAjaxQueryDto>) map.get("ajaxResult");
            if(ajaxResult != null) {
                List<DashReplyAjaxQueryDto> dashReplyAjaxQueryDtos = ajaxResult.getRows();
                if(!IaisCommonUtils.isEmpty(dashReplyAjaxQueryDtos)) {
                    for(DashReplyAjaxQueryDto dashReplyAjaxQueryDto : dashReplyAjaxQueryDtos) {
                        dashReplyAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                    }
                }
            }
        }
        return map;
    }

    private Map<String, Object> setDashKpiPoolUrl(Map<String, Object> map, HttpServletRequest request, LoginContext loginContext,
                              Map<String, TaskDto> taskMap, Map<String, AppPremisesRoutingHistoryDto> hisMap,
                              Map<String, AppStageSlaTrackingDto> slaTrackingMap) {
        String userId = "";
        String roleId = "";
        if(loginContext != null && !StringUtil.isEmpty(loginContext.getUserId()) && !StringUtil.isEmpty(loginContext.getCurRoleId())) {
            log.info(StringUtil.changeForLog("Dashboard Kpi Pool user Id =====" + loginContext.getUserId()));
            log.info(StringUtil.changeForLog("Dashboard Kpi Pool Role Id =====" + loginContext.getCurRoleId()));
            userId = loginContext.getUserId();
            roleId = loginContext.getCurRoleId();
        }
        if(map != null) {
            SearchResult<DashKpiPoolAjaxQuery> ajaxResult = (SearchResult<DashKpiPoolAjaxQuery>) map.get("ajaxResult");
            if(ajaxResult != null) {
                List<DashKpiPoolAjaxQuery> dashKpiPoolAjaxQueries = ajaxResult.getRows();
                if(!IaisCommonUtils.isEmpty(dashKpiPoolAjaxQueries)) {
                    List<HcsaSvcKpiDto> kpiConfList = hcsaConfigMainClient.retrieveForDashboard().getEntity();
                    for(DashKpiPoolAjaxQuery dashKpiPoolAjaxQuery : dashKpiPoolAjaxQueries) {
                        //task is current work
                        if(!StringUtil.isEmpty(dashKpiPoolAjaxQuery.getTaskId())) {
                            TaskDto taskDto = taskMap.get(dashKpiPoolAjaxQuery.getTaskId());
                            //set kpi color
                            String color = getKpiColorByTask(taskDto, dashKpiPoolAjaxQuery, hisMap, kpiConfList, slaTrackingMap);
                            dashKpiPoolAjaxQuery.setKpiColor(color);
                            if(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN.equals(roleId)) {
                                dashKpiPoolAjaxQuery.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                            } else if (userId.equals(taskDto.getUserId()) && roleId.equals(taskDto.getRoleId())) {
                                //set mask task Id
                                String maskId = MaskUtil.maskValue("taskId", dashKpiPoolAjaxQuery.getTaskId());
                                dashKpiPoolAjaxQuery.setTaskMaskId(maskId);
                                dashKpiPoolAjaxQuery.setCanDoTask(BeDashboardConstant.TASK_PROCESS);
                                String dashTaskUrl = generateProcessUrl(taskDto.getProcessUrl(), request, maskId);
                                dashKpiPoolAjaxQuery.setDashTaskUrl(dashTaskUrl);
                            } else {
                                dashKpiPoolAjaxQuery.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                            }
                        } else {
                            dashKpiPoolAjaxQuery.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                        }
                    }
                }
            }
        }
        return map;
    }

    private Map<String, Object> setDashRenewPoolUrl(Map<String, Object> map, HttpServletRequest request, LoginContext loginContext,
                                Map<String, TaskDto> taskMap, Map<String, AppPremisesRoutingHistoryDto> hisMap,
                                Map<String, AppStageSlaTrackingDto> slaTrackingMap) {
        String userId = "";
        String roleId = "";
        if (loginContext != null && !StringUtil.isEmpty(loginContext.getUserId()) && !StringUtil.isEmpty(loginContext.getCurRoleId())) {
            log.info(StringUtil.changeForLog("Dashboard Kpi Pool user Id =====" + loginContext.getUserId()));
            log.info(StringUtil.changeForLog("Dashboard Kpi Pool Role Id =====" + loginContext.getCurRoleId()));
            userId = loginContext.getUserId();
            roleId = loginContext.getCurRoleId();
        }
        if (map != null) {
            SearchResult<DashRenewAjaxQueryDto> ajaxResult = (SearchResult<DashRenewAjaxQueryDto>) map.get("ajaxResult");
            if(ajaxResult != null) {
                List<DashRenewAjaxQueryDto> dashRenewAjaxQueryDtos = ajaxResult.getRows();
                if(!IaisCommonUtils.isEmpty(dashRenewAjaxQueryDtos)) {
                    List<HcsaSvcKpiDto> kpiConfList = hcsaConfigMainClient.retrieveForDashboard().getEntity();
                    for(DashRenewAjaxQueryDto dashRenewAjaxQueryDto : dashRenewAjaxQueryDtos) {
                        //task is current work
                        if(!StringUtil.isEmpty(dashRenewAjaxQueryDto.getTaskId())) {
                            TaskDto taskDto = taskMap.get(dashRenewAjaxQueryDto.getTaskId());
                            //set kpi color
                            String color = getKpiColorByTask(taskDto, dashRenewAjaxQueryDto, hisMap, kpiConfList, slaTrackingMap);
                            dashRenewAjaxQueryDto.setKpiColor(color);
                            if(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN.equals(roleId)) {
                                dashRenewAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                            } else if (userId.equals(taskDto.getUserId()) && roleId.equals(taskDto.getRoleId())) {
                                //set mask task Id
                                String maskId = MaskUtil.maskValue("taskId", dashRenewAjaxQueryDto.getTaskId());
                                dashRenewAjaxQueryDto.setTaskMaskId(maskId);
                                dashRenewAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_PROCESS);
                                String dashTaskUrl = generateProcessUrl(taskDto.getProcessUrl(), request, maskId);
                                dashRenewAjaxQueryDto.setDashTaskUrl(dashTaskUrl);
                            } else {
                                dashRenewAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                            }
                        } else {
                            dashRenewAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                        }
                    }
                }
            }
        }
        return map;
    }

    private Map<String, Object> setDashWaitApproveUrl(Map<String, Object> map, HttpServletRequest request, LoginContext loginContext,
                          Map<String, TaskDto> taskMap, Map<String, AppPremisesRoutingHistoryDto> hisMap,
                          Map<String, AppStageSlaTrackingDto> slaTrackingMap) {
        String userId = "";
        String roleId = "";
        if(loginContext != null && !StringUtil.isEmpty(loginContext.getUserId()) && !StringUtil.isEmpty(loginContext.getCurRoleId())) {
            log.info(StringUtil.changeForLog("Dashboard Kpi Pool user Id =====" + loginContext.getUserId()));
            log.info(StringUtil.changeForLog("Dashboard Kpi Pool Role Id =====" + loginContext.getCurRoleId()));
            userId = loginContext.getUserId();
            roleId = loginContext.getCurRoleId();
        }
        if(map != null) {
            SearchResult<DashWaitApproveAjaxQueryDto> ajaxResult = (SearchResult<DashWaitApproveAjaxQueryDto>) map.get("ajaxResult");
            if(ajaxResult != null) {
                List<DashWaitApproveAjaxQueryDto> dashWaitApproveAjaxQueryDtos = ajaxResult.getRows();
                if(!IaisCommonUtils.isEmpty(dashWaitApproveAjaxQueryDtos)) {
                    List<HcsaSvcKpiDto> kpiConfList = hcsaConfigMainClient.retrieveForDashboard().getEntity();
                    for(DashWaitApproveAjaxQueryDto dashWaitApproveAjaxQueryDto : dashWaitApproveAjaxQueryDtos) {
                        //task is current work
                        if(!StringUtil.isEmpty(dashWaitApproveAjaxQueryDto.getTaskId())) {
                            TaskDto taskDto = taskMap.get(dashWaitApproveAjaxQueryDto.getTaskId());
                            //set kpi color
                            String color = getKpiColorByTask(taskDto, dashWaitApproveAjaxQueryDto, hisMap, kpiConfList, slaTrackingMap);
                            dashWaitApproveAjaxQueryDto.setKpiColor(color);
                            if(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN.equals(roleId)) {
                                dashWaitApproveAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                            } else if (userId.equals(taskDto.getUserId()) && roleId.equals(taskDto.getRoleId())) {
                                //set mask task Id
                                String maskId = MaskUtil.maskValue("taskId", dashWaitApproveAjaxQueryDto.getTaskId());
                                dashWaitApproveAjaxQueryDto.setTaskMaskId(maskId);
                                dashWaitApproveAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_PROCESS);
                                String dashTaskUrl = generateProcessUrl(taskDto.getProcessUrl(), request, maskId);
                                dashWaitApproveAjaxQueryDto.setDashTaskUrl(dashTaskUrl);
                            } else {
                                dashWaitApproveAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                            }
                        } else {
                            dashWaitApproveAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                        }
                    }
                }
            }
        }
        return map;
    }

    private Map<String, Object> setWorkTeamPoolUrl(Map<String, Object> map, Map<String, TaskDto> taskMap,
                   Map<String, AppPremisesRoutingHistoryDto> hisMap, Map<String, AppStageSlaTrackingDto> slaTrackingMap) {
        if(map != null) {
            SearchResult<DashWorkTeamAjaxQueryDto> ajaxResult = (SearchResult<DashWorkTeamAjaxQueryDto>) map.get("ajaxResult");
            if(ajaxResult != null) {
                List<DashWorkTeamAjaxQueryDto> dashWorkTeamAjaxQueryDtos = ajaxResult.getRows();
                if(!IaisCommonUtils.isEmpty(dashWorkTeamAjaxQueryDtos)) {
                    List<HcsaSvcKpiDto> kpiConfList = hcsaConfigMainClient.retrieveForDashboard().getEntity();
                    for(DashWorkTeamAjaxQueryDto dashWorkTeamAjaxQueryDto : dashWorkTeamAjaxQueryDtos) {
                        if(!StringUtil.isEmpty(dashWorkTeamAjaxQueryDto.getTaskId())) {
                            TaskDto taskDto = taskMap.get(dashWorkTeamAjaxQueryDto.getTaskId());
                            //set kpi color
                            String color = getKpiColorByTask(taskDto, dashWorkTeamAjaxQueryDto, hisMap, kpiConfList, slaTrackingMap);
                            dashWorkTeamAjaxQueryDto.setKpiColor(color);
                        }
                        dashWorkTeamAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                    }
                }
            }
        }
        return map;
    }

    private String generateProcessUrl(String url, HttpServletRequest request, String taskMaskId) {
        StringBuilder sb = new StringBuilder("https://");
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
        sb.append("taskId=").append(taskMaskId);
        return RedirectUtil.appendCsrfGuardToken(sb.toString(), request);
    }

    private String getKpiColorByTask(TaskDto taskDto, DashComAjaxQueryDto ajaxDto,
                 Map<String, AppPremisesRoutingHistoryDto> hisMap, List<HcsaSvcKpiDto> kpiConfList,
                 Map<String, AppStageSlaTrackingDto> slaTrackingMap) {
        String colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK;
        if (ajaxDto != null) {
            String stage;
            if (HcsaConsts.ROUTING_STAGE_INS.equals(taskDto.getTaskKey())) {
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = hisMap.get(taskDto.getApplicationNo());
                stage = appPremisesRoutingHistoryDto.getSubStage();
            } else {
                stage = taskDto.getTaskKey();
            }
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(ajaxDto.getServiceId());
            HcsaSvcKpiDto hcsaSvcKpiDto = null;
            for (HcsaSvcKpiDto hsd : kpiConfList) {
                if (hsd.getServiceCode().equals(hcsaServiceDto.getSvcCode()) && hsd.getModule().equals(ajaxDto.getAppType())) {
                    hcsaSvcKpiDto = hsd;
                    break;
                }
            }
            if (hcsaSvcKpiDto != null) {
                //get current stage worked days
                int days = 0;
                if (!StringUtil.isEmpty(stage)) {
                    AppStageSlaTrackingDto appStageSlaTrackingDto = slaTrackingMap.get(ajaxDto.getApplicationNo() + stage);
                    if (appStageSlaTrackingDto != null) {
                        days = appStageSlaTrackingDto.getKpiSlaDays();
                    }
                }
                //get warning value
                Map<String, Integer> kpiMap = hcsaSvcKpiDto.getStageIdKpi();
                int kpi = 0;
                if (!StringUtil.isEmpty(stage)) {
                    if (kpiMap != null && kpiMap.get(stage) != null) {
                        kpi = kpiMap.get(stage);
                    }
                }
                //get threshold value
                int remThreshold = 0;
                if (hcsaSvcKpiDto.getRemThreshold() != null) {
                    remThreshold = hcsaSvcKpiDto.getRemThreshold();
                }
                //get color
                colour = getColorByWorkAndKpiDay(kpi, days, remThreshold);
            }
        }
        return colour;
    }

    private String getColorByWorkAndKpiDay(int kpi, int days, int remThreshold) {
        String colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK;
        if(remThreshold != 0) {
            if (days < remThreshold) {
                colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK;
            }
            if (kpi != 0) {
                if (remThreshold <= days && days <= kpi) {
                    colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_AMBER;
                } else if (days > kpi) {
                    colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_RED;
                }
            }
        }
        return colour;
    }

    private Map<String, String> validateCanApprove(ApplicationViewDto applicationViewDto) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity<>(applicationViewDto);
        return restTemplate.postForObject(CAN_APPROVE_API_URL, applicationViewDto, Map.class);
    }
}