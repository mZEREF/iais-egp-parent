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
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
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
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashAssignMeAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashAssignMeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashComAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashComPoolAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashComPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashKpiPoolAjaxQuery;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashKpiPoolQuery;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashRenewAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashRenewQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashReplyAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashReplyQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashWaitApproveAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashWaitApproveQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashWorkTeamAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashWorkTeamQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
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
import com.ecquaria.cloud.moh.iais.service.client.CessationMainClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskMainClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateMainClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private CessationMainClient cessationMainClient;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    MsgTemplateMainClient msgTemplateMainClient;

    @Autowired
    @Qualifier(value = "iaisRestTemplate")
    private RestTemplate restTemplate;

    private static final String DASH_ACTION_VALUE = "dashActionValue";

    private static final String APPLICATION_VIEW_DTO = "applicationViewDto";

    private static final String DASH_SWITCH_ACTION_VALUE = "dashSwitchActionValue";

    private static final String APP_STATUS_OPTION = "appStatusOption";

    private static final String DASH_SEARCH_PARAM = "dashSearchParam";

    private static final String DASH_SEARCH_RESULT = "dashSearchResult";

    private static final String DASH_WORK_GROUP_IDS = "dashWorkGroupIds";

    private static final String GROUP_ROLE_FIELD_DTO = "groupRoleFieldDto";

    private static final String DASH_ROLE_CHECK_DTO = "dashRoleCheckDto";

    private static final String DASH_APP_STATUS = "dashAppStatus";

    private static final String APPLICATION_STATUS = "application_status";

    private static final String DASH_PROCESS_BACK_FLAG = "dashProcessBackFlag";

    private static final String INSPEC_TASK_CRE_AND_ASS_DTO = "inspecTaskCreAndAssDto";

    private static final String DASH_HCI_ADDRESS = "dashHciAddress";

    private static final String HCSA_TASK_ASSIGN_DTO = "hcsaTaskAssignDto";

    private static final String BACKEND_INBOX_APPROVE = "BackendInboxApprove";

    private static final String BACKEND_INBOX_RETURN_FEE = "BackendInboxReturnFee";

    private static final String TASK_ID = "taskId";

    private static final String BE_MAIN_AO_APPROVE = "bemainAo1Ao2Approve";

    private static final String NEXT_STAGE = "nextStage";

    private static final String SUCCESS_INFO = "successInfo";

    private static final String SWITCH_ACTION = "switchAction";

    private static final String DASH_FILTER_APP_NO = "dashFilterAppNo";

    private static final String DASH_COMMON_POOL_STATUS = "dashCommonPoolStatus";

    private static final String DASH_COMMON_TASK = "dashCommonTask";

    private static final String T_ID = "T5.ID";

    private static final String APP_GROUP_LIST = "appGroup_list";

    private static final String DASH_KPI_TASK = "dashKpiTask";

    private static final String DASH_SUPERVIDOR_TASK = "dashSupervisorTask";

    private static final String DASH_APP_REPLY_TASK = "dashAppReplyTask";

    private static final String DASH_WAIT_APPROVE_TASK = "dashWaitApproveTask";

    private static final String DASH_APP_RENEW_TASK = "dashAppRenewTask";

    private static final String APPLICATION_NUMBER = "ApplicationNumber";

    private static final String APPLICATION_TYPE = "ApplicationType";

    private static final String T5STATUS         = "T5.STATUS";
    private static final String DASHASSIGNME         = "dashAssignMe";
    private static final String INTRADASHBOARDQUERY         = "intraDashboardQuery";
    private static final String AJAXRESULT      = "ajaxResult";

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
            ParamUtil.setSessionAttr(bpc.request, DASH_ACTION_VALUE, null);
            ParamUtil.setSessionAttr(bpc.request, APPLICATION_VIEW_DTO, null);
            String backFlag = ParamUtil.getRequestString(bpc.request, "dashProcessBack");
            if(!AppConsts.YES.equals(backFlag)) {
                ParamUtil.setSessionAttr(bpc.request, DASH_SWITCH_ACTION_VALUE, null);
                ParamUtil.setSessionAttr(bpc.request, "appTypeOption", null);
                ParamUtil.setSessionAttr(bpc.request, APP_STATUS_OPTION, null);
                ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_PARAM, null);
                ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_RESULT, null);
                ParamUtil.setSessionAttr(bpc.request, DASH_WORK_GROUP_IDS, null);
                ParamUtil.setSessionAttr(bpc.request, GROUP_ROLE_FIELD_DTO, null);
                ParamUtil.setSessionAttr(bpc.request, "beDashRoleIds", null);
                ParamUtil.setSessionAttr(bpc.request, DASH_ROLE_CHECK_DTO, null);
                ParamUtil.setSessionAttr(bpc.request, DASH_APP_STATUS, null);
                ParamUtil.setSessionAttr(bpc.request, APPLICATION_STATUS, null);
            } else {
                ParamUtil.setRequestAttr(bpc.request, DASH_PROCESS_BACK_FLAG, backFlag);
            }
            ParamUtil.setSessionAttr(bpc.request, INSPEC_TASK_CRE_AND_ASS_DTO, null);
        }
        ParamUtil.setSessionAttr(bpc.request, DASH_HCI_ADDRESS, null);
        ParamUtil.setSessionAttr(bpc.request, HCSA_TASK_ASSIGN_DTO, null);
    }

    /**
     * StartStep: hcsaBeDashboardInit
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardInit(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardInit start ...."));
        String backFlag = (String)ParamUtil.getRequestAttr(bpc.request, DASH_PROCESS_BACK_FLAG);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INTRANET_DASHBOARD, AuditTrailConsts.FUNCTION_INTRANET_DASHBOARD);
        //set role list and current role
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(!AppConsts.YES.equals(backFlag)) {
            if (loginContext != null) {
                PoolRoleCheckDto poolRoleCheckDto = new PoolRoleCheckDto();
                poolRoleCheckDto = mohHcsaBeDashboardService.getDashRoleOption(loginContext, poolRoleCheckDto);
                ParamUtil.setSessionAttr(bpc.request, "beDashRoleIds", (Serializable) poolRoleCheckDto.getRoleOptions());
                ParamUtil.setSessionAttr(bpc.request, DASH_ROLE_CHECK_DTO, poolRoleCheckDto);
            }
            ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
        } else {
            ParamUtil.setRequestAttr(bpc.request, DASH_PROCESS_BACK_FLAG, backFlag);
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
        String backFlag = (String)ParamUtil.getRequestAttr(bpc.request, DASH_PROCESS_BACK_FLAG);
        if(loginContext != null && !AppConsts.YES.equals(backFlag)) {
            //get appType option
            List<SelectOption> appTypeOption = inspectionService.getAppTypeOption();
            SearchParam searchParam = getSearchParam(bpc, true, DashAssignMeQueryDto.class.getName());
            //get app status option
            List<SelectOption> appStatusOption = mohHcsaBeDashboardService.getAppStatusOptionByRoleAndSwitch(loginContext.getCurRoleId(), BeDashboardConstant.SWITCH_ACTION_ASSIGN_ME);
            //set app status and search filter
            if (RoleConsts.USER_ROLE_AO3.equals(loginContext.getCurRoleId()) || RoleConsts.USER_ROLE_AO3_LEAD.equals(loginContext.getCurRoleId())) {
                String applicationStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
                List<String> appStatusList = IaisCommonUtils.genNewArrayList();
                appStatusList.add(applicationStatus);
                String appStatusStr = SqlHelper.constructInCondition(T5STATUS, appStatusList.size());
                searchParam.addParam(APPLICATION_STATUS, appStatusStr);
                for (int i = 0; i < appStatusList.size(); i++) {
                    searchParam.addFilter(T5STATUS + i, appStatusList.get(i));
                }
                ParamUtil.setSessionAttr(bpc.request, DASH_APP_STATUS, applicationStatus);
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
            QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASHASSIGNME, searchParam);
            SearchResult<DashAssignMeQueryDto> searchResult = mohHcsaBeDashboardService.getDashAssignMeResult(searchParam);
            searchResult = mohHcsaBeDashboardService.getDashAssignMeOtherData(searchResult);

            //set session
            ParamUtil.setSessionAttr(bpc.request, APP_STATUS_OPTION, (Serializable) appStatusOption);
            ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_PARAM, searchParam);
            ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_RESULT, searchResult);
            ParamUtil.setSessionAttr(bpc.request, DASH_SWITCH_ACTION_VALUE, BeDashboardConstant.SWITCH_ACTION_ASSIGN_ME);
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
        ParamUtil.setRequestAttr(bpc.request, DASH_ACTION_VALUE, actionValue);
    }

    /**
     * StartStep: hcsaBeDashboardApprove
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardApprove(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException, TemplateException, IOException {
        log.info(StringUtil.changeForLog("the hcsaBeDashboardApprove start ...."));
        ParamUtil.setSessionAttr(bpc.request,BACKEND_INBOX_APPROVE,null);
        ParamUtil.setSessionAttr(bpc.request,BACKEND_INBOX_RETURN_FEE,null);

        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String[] taskList =  ParamUtil.getMaskedStrings(bpc.request, TASK_ID);
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
                List<String> app = (List<String>)ParamUtil.getSessionAttr(bpc.request,BACKEND_INBOX_APPROVE);
                if(app == null){
                    app = IaisCommonUtils.genNewArrayList();
                }
                app.add(applicationDto.getApplicationNo());
                ParamUtil.setSessionAttr(bpc.request,BACKEND_INBOX_APPROVE,(Serializable) app);
                String status = applicationDto.getStatus();
                if(("trigger").equals(action)){
                    routeToDMS(bpc,applicationViewDto,taskDto);
                    successStatus = ApplicationConsts.PROCESSING_DECISION_ROUTE_TO_DMS;

                }else if(RoleConsts.USER_ROLE_AO1.equals(loginContext.getCurRoleId())){
                    if(("ao1approve").equals(action)){
                        log.info(StringUtil.changeForLog("the do ao1 approve start ...."));
                        ParamUtil.setSessionAttr(bpc.request,BE_MAIN_AO_APPROVE,"Y");
                        successStatus = ApplicationConsts.APPLICATION_STATUS_APPROVED;
                        Map<String,String> errMap = validateCanApprove(applicationViewDto);
                        if (IaisCommonUtils.isNotEmpty(errMap)) {
                            ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.FALSE);
                            ParamUtil.setRequestAttr(bpc.request,SUCCESS_INFO, errMap.get(NEXT_STAGE));
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
                        ParamUtil.setSessionAttr(bpc.request,BE_MAIN_AO_APPROVE,"Y");
                        successStatus = ApplicationConsts.APPLICATION_STATUS_APPROVED;
                        Map<String,String> errMap = validateCanApprove(applicationViewDto);
                        log.info("AO2 approve validation rslt ==> {}", errMap);
                        if (IaisCommonUtils.isNotEmpty(errMap)) {
                            ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.FALSE);
                            ParamUtil.setRequestAttr(bpc.request,SUCCESS_INFO, errMap.get(NEXT_STAGE));
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
                        ParamUtil.setRequestAttr(bpc.request,SUCCESS_INFO, errMap.get(NEXT_STAGE));
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
            ParamUtil.setRequestAttr(bpc.request,SUCCESS_INFO,successInfo);
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
        log.info(StringUtil.changeForLog("the hcsaBeDashboardSwitchSort start ...."),bpc);
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
        ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_PARAM, searchParam);
    }

    /**
     * StartStep: hcsaBeDashboardInGroup
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardInGroup(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardInGroup start ...."));
        String switchAction = ParamUtil.getRequestString(bpc.request, SWITCH_ACTION);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        SearchParam searchParam = getSearchParam(bpc, true, DashWorkTeamQueryDto.class.getName());
        //set form value
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        //app status
        List<SelectOption> appStatusOption = mohHcsaBeDashboardService.getAppStatusOptionByRoleAndSwitch(loginContext.getCurRoleId(), switchAction);
        searchParam = setFilterByDashForm(searchParam, bpc.request, switchAction, loginContext, appStatusOption,
                T5STATUS);
        //if not lead and approver, set userId
        workGroupIds = mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);

        ParamUtil.setSessionAttr(bpc.request, APP_STATUS_OPTION, (Serializable) appStatusOption);
        ParamUtil.setSessionAttr(bpc.request, DASH_WORK_GROUP_IDS, (Serializable) workGroupIds);
        ParamUtil.setSessionAttr(bpc.request, DASH_SWITCH_ACTION_VALUE, switchAction);
        ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_PARAM, searchParam);
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
        String switchAction = ParamUtil.getRequestString(bpc.request, SWITCH_ACTION);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        SearchParam searchParam = getSearchParam(bpc, true, DashReplyQueryDto.class.getName());
        //set form value
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        searchParam = setFilterByDashForm(searchParam, bpc.request, switchAction, loginContext, null,
                null);
        //if not lead and approver, set userId
        workGroupIds = mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
        ParamUtil.setSessionAttr(bpc.request, APP_STATUS_OPTION, null);
        ParamUtil.setSessionAttr(bpc.request, DASH_WORK_GROUP_IDS, (Serializable) workGroupIds);
        ParamUtil.setSessionAttr(bpc.request, DASH_SWITCH_ACTION_VALUE, switchAction);
        ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_PARAM, searchParam);
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
        String switchAction = ParamUtil.getRequestString(bpc.request, SWITCH_ACTION);
        //app status
        List<SelectOption> appStatusOption = mohHcsaBeDashboardService.getAppStatusOptionByRoleAndSwitch(loginContext.getCurRoleId(), switchAction);
        //set form value
        searchParam = setFilterByDashForm(searchParam, bpc.request, switchAction, loginContext, appStatusOption,
                "T1.STATUS");
        //get work groups
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);

        ParamUtil.setSessionAttr(bpc.request, APP_STATUS_OPTION, (Serializable) appStatusOption);
        ParamUtil.setSessionAttr(bpc.request, DASH_SWITCH_ACTION_VALUE, switchAction);
        ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_PARAM, searchParam);
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
        String dashActionValue = (String)ParamUtil.getRequestAttr(bpc.request, DASH_ACTION_VALUE);
        if(!StringUtil.isEmpty(dashActionValue) && MessageDigest.isEqual(dashActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_BACK.getBytes(StandardCharsets.UTF_8))) {
            ParamUtil.setRequestAttr(bpc.request, DASH_ACTION_VALUE, dashActionValue);
        } else {
            String switchAction = ParamUtil.getRequestString(bpc.request, SWITCH_ACTION);
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            SearchParam searchParam = getSearchParam(bpc, true, DashComPoolQueryDto.class.getName());
            //set form value
            List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
            searchParam = setFilterByDashForm(searchParam, bpc.request, switchAction, loginContext, null,
                    null);
            //if not lead and approver, set userId
            workGroupIds = mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);

            ParamUtil.setSessionAttr(bpc.request, DASH_WORK_GROUP_IDS, (Serializable) workGroupIds);
            ParamUtil.setSessionAttr(bpc.request, DASH_SWITCH_ACTION_VALUE, switchAction);
            ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_PARAM, searchParam);
            ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
        }
        ParamUtil.setSessionAttr(bpc.request, APP_STATUS_OPTION, null);
    }

    private SearchParam setFilterByDashForm(SearchParam searchParam, HttpServletRequest request, String actionValue, LoginContext loginContext,
                                            List<SelectOption> appStatusOption, String appStatusKey) {
        PoolRoleCheckDto poolRoleCheckDto = (PoolRoleCheckDto)ParamUtil.getSessionAttr(request, DASH_ROLE_CHECK_DTO);
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
        String applicationNo = ParamUtil.getRequestString(request, "application_no");
        String applicationType = ParamUtil.getRequestString(request, "application_type");
        String applicationStatus = ParamUtil.getRequestString(request, APPLICATION_STATUS);
        String hciCode = ParamUtil.getRequestString(request, "hci_code");
        String hciName = ParamUtil.getRequestString(request, "hci_name");
        String hciAddress = ParamUtil.getRequestString(request, "hci_address");
        if(!StringUtil.isEmpty(applicationNo)){
            searchParam.addFilter("application_no", applicationNo, true);
            ParamUtil.setSessionAttr(request, DASH_FILTER_APP_NO, applicationNo);
        } else {
            ParamUtil.setSessionAttr(request, DASH_FILTER_APP_NO, null);
        }
        if(!StringUtil.isEmpty(applicationType)){
            searchParam.addFilter("application_type", applicationType, true);
        }
        if(!StringUtil.isEmpty(applicationStatus) &&
                (!(BeDashboardConstant.SWITCH_ACTION_COMMON.equals(actionValue)) && !(BeDashboardConstant.SWITCH_ACTION_REPLY.equals(actionValue))))
        {
            boolean flag = mohHcsaBeDashboardService.containsAppStatus(appStatusOption, applicationStatus);
            if(flag) {
                //Filter the Common Pool Task in another place
                if (!applicationStatus.equals(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT)) {
                    List<String> appStatus = mohHcsaBeDashboardService.getSearchAppStatus(applicationStatus);
                    String appStatusStr = SqlHelper.constructInCondition(appStatusKey, appStatus.size());
                    searchParam.addParam(APPLICATION_STATUS, appStatusStr);
                    for(int i = 0; i < appStatus.size(); i++) {
                        searchParam.addFilter(appStatusKey + i, appStatus.get(i));
                    }
                    ParamUtil.setSessionAttr(request, DASH_APP_STATUS, applicationStatus);
                    ParamUtil.setSessionAttr(request, DASH_COMMON_POOL_STATUS, null);
                } else {//Filter the Common Pool Task
                    searchParam.addParam(DASH_COMMON_POOL_STATUS, DASH_COMMON_POOL_STATUS);
                    ParamUtil.setSessionAttr(request, DASH_COMMON_POOL_STATUS, DASH_COMMON_POOL_STATUS);
                    ParamUtil.setSessionAttr(request, DASH_APP_STATUS, null);
                }
            } else {
                ParamUtil.setSessionAttr(request, DASH_COMMON_POOL_STATUS, null);
                ParamUtil.setSessionAttr(request, DASH_APP_STATUS, null);
            }
        } else {
            ParamUtil.setSessionAttr(request, DASH_COMMON_POOL_STATUS, null);
            ParamUtil.setSessionAttr(request, DASH_APP_STATUS, null);
        }
        if(!StringUtil.isEmpty(hciCode)){
            searchParam.addFilter("hci_code", hciCode, true);
        }
        if(!StringUtil.isEmpty(hciName)){
            searchParam.addFilter("hci_name", hciName, true);
        }
        if(!StringUtil.isEmpty(hciAddress)){
            ParamUtil.setSessionAttr(request, DASH_HCI_ADDRESS, hciAddress);
        } else {
            ParamUtil.setSessionAttr(request, DASH_HCI_ADDRESS, null);
        }
        //licence expire days
        if(BeDashboardConstant.SWITCH_ACTION_RE_RENEW.equals(actionValue)) {
            searchParam.addFilter("lic_renew_exp", systemParamConfig.getDashRenewDate(), true);
        }
        ParamUtil.setSessionAttr(request, DASH_ROLE_CHECK_DTO, poolRoleCheckDto);
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
            HcsaTaskAssignDto hcsaTaskAssignDto = (HcsaTaskAssignDto)ParamUtil.getSessionAttr(bpc.request, HCSA_TASK_ASSIGN_DTO);
            LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            ParamUtil.setSessionAttr(bpc.request, INSPEC_TASK_CRE_AND_ASS_DTO, null);
            ParamUtil.setSessionAttr(bpc.request, APPLICATION_VIEW_DTO, null);
            ParamUtil.setSessionAttr(bpc.request, GROUP_ROLE_FIELD_DTO, null);
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
                ParamUtil.setSessionAttr(bpc.request, INSPEC_TASK_CRE_AND_ASS_DTO, inspecTaskCreAndAssDto);
                ParamUtil.setSessionAttr(bpc.request, APPLICATION_VIEW_DTO, applicationViewDto);
                ParamUtil.setSessionAttr(bpc.request, GROUP_ROLE_FIELD_DTO, groupRoleFieldDto);
            }
        }
    }

    private String getDashTaskIdByBpc(BaseProcessClass bpc) {
        String taskId = "";
        try{
            taskId = ParamUtil.getMaskedString(bpc.request,TASK_ID);
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
        InspecTaskCreAndAssDto inspecTaskCreAndAssDto = (InspecTaskCreAndAssDto)ParamUtil.getSessionAttr(bpc.request, INSPEC_TASK_CRE_AND_ASS_DTO);
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
        ParamUtil.setSessionAttr(bpc.request,INSPEC_TASK_CRE_AND_ASS_DTO, inspecTaskCreAndAssDto);
        ParamUtil.setSessionAttr(bpc.request, "cPoolSearchResult", searchResult);
    }

    public InspecTaskCreAndAssDto getValueFromPage(BaseProcessClass bpc) {
        InspecTaskCreAndAssDto inspecTaskCreAndAssDto = (InspecTaskCreAndAssDto)ParamUtil.getSessionAttr(bpc.request, INSPEC_TASK_CRE_AND_ASS_DTO);
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
        log.info(StringUtil.changeForLog("the hcsaBeDashboardComConfirm start ...."),bpc);
    }

    /**
     * StartStep: hcsaBeDashboardComDo
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardComDo(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardComDo start ...."));
        InspecTaskCreAndAssDto inspecTaskCreAndAssDto = (InspecTaskCreAndAssDto)ParamUtil.getSessionAttr(bpc.request, INSPEC_TASK_CRE_AND_ASS_DTO);
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request, APPLICATION_VIEW_DTO);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        String saveFlag = inspectionMainAssignTaskService.routingTaskByCommonPool(applicationViewDto, inspecTaskCreAndAssDto, internalRemarks, loginContext);
        if(AppConsts.FAIL.equals(saveFlag)){
            ParamUtil.setRequestAttr(bpc.request,"taskHasBeenAssigned", AppConsts.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request,INSPEC_TASK_CRE_AND_ASS_DTO, inspecTaskCreAndAssDto);
    }

    /**
     * StartStep: hcsaBeDashboardWaitApprove
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardWaitApprove(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardWaitApprove start ...."));
        String switchAction = ParamUtil.getRequestString(bpc.request, SWITCH_ACTION);
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

        ParamUtil.setSessionAttr(bpc.request, APP_STATUS_OPTION, (Serializable) appStatusOption);
        ParamUtil.setSessionAttr(bpc.request, DASH_WORK_GROUP_IDS, (Serializable) workGroupIds);
        ParamUtil.setSessionAttr(bpc.request, DASH_SWITCH_ACTION_VALUE, switchAction);
        ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_PARAM, searchParam);
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
        String switchAction = ParamUtil.getRequestString(bpc.request, SWITCH_ACTION);
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

        ParamUtil.setSessionAttr(bpc.request, APP_STATUS_OPTION, (Serializable) appStatusOption);
        ParamUtil.setSessionAttr(bpc.request, DASH_WORK_GROUP_IDS, (Serializable) workGroupIds);
        ParamUtil.setSessionAttr(bpc.request, DASH_SWITCH_ACTION_VALUE, switchAction);
        ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_PARAM, searchParam);
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
                    T5STATUS);
            //role
            String curRoleId = loginContext.getCurRoleId();
            if(!StringUtil.isEmpty(curRoleId)) {
                searchParam.addFilter("dashRoleId", curRoleId, true);
            }

            ParamUtil.setSessionAttr(bpc.request, APP_STATUS_OPTION, (Serializable) appStatusOption);
            ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_PARAM, searchParam);
            ParamUtil.setSessionAttr(bpc.request, DASH_SWITCH_ACTION_VALUE, BeDashboardConstant.SWITCH_ACTION_ASSIGN_ME);
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
        String dashSwitchActionValue = (String)ParamUtil.getSessionAttr(bpc.request, DASH_SWITCH_ACTION_VALUE);
        String dashActionValue = (String)ParamUtil.getRequestAttr(bpc.request, DASH_ACTION_VALUE);
        //address for second search
        String hciAddress = (String)ParamUtil.getSessionAttr(bpc.request, DASH_HCI_ADDRESS);
        ArrayList<String> groupNos = IaisCommonUtils.genNewArrayList();
        //get result
        if(!StringUtil.isEmpty(dashActionValue) && MessageDigest.isEqual(dashActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_BACK.getBytes(StandardCharsets.UTF_8))) {
            ParamUtil.setRequestAttr(bpc.request, DASH_ACTION_VALUE, dashActionValue);
        } else if(!StringUtil.isEmpty(dashSwitchActionValue) && MessageDigest.isEqual(dashSwitchActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_COMMON.getBytes(StandardCharsets.UTF_8))) {
            SearchResult<DashComPoolQueryDto> searchResult;
            if(!StringUtil.isEmpty(hciAddress)) {
                //copy SearchParam for searchAllParam
                SearchParam searchAllParam = CopyUtil.copyMutableObject(searchParam);
                searchAllParam.setPageSize(-1);
                //get all appGroupIds
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_COMMON_TASK, searchAllParam);
                searchResult = mohHcsaBeDashboardService.getDashComPoolResult(searchAllParam);
                //set all address data map for filter address
                List<String> appGroupIds = mohHcsaBeDashboardService.getComPoolAppGrpIdByResult(searchResult);
                HcsaTaskAssignDto hcsaTaskAssignDto = mohHcsaBeDashboardService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
                //filter unit no for group
                searchParam = mohHcsaBeDashboardService.setAppGrpIdsByUnitNos(searchParam, hciAddress, hcsaTaskAssignDto, T_ID, APP_GROUP_LIST);
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_COMMON_TASK, searchParam);
                searchResult = mohHcsaBeDashboardService.getDashComPoolResult(searchParam);
                //set hcsaTaskAssignDto in session
                ParamUtil.setSessionAttr(bpc.request, HCSA_TASK_ASSIGN_DTO, hcsaTaskAssignDto);
            } else {
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_COMMON_TASK, searchParam);
                searchResult = mohHcsaBeDashboardService.getDashComPoolResult(searchParam);
                //clear hcsaTaskAssignDto in session
                ParamUtil.setSessionAttr(bpc.request, HCSA_TASK_ASSIGN_DTO, null);
            }
            searchResult = mohHcsaBeDashboardService.getDashComPoolOtherData(searchResult);
            if (searchResult != null && searchResult.getRowCount() > 0) {
                for (DashComPoolQueryDto dcp : searchResult.getRows()) {
                    groupNos.add(dcp.getAppGroupNo());
                }
            }
            //set session
            ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_RESULT, searchResult);
        } else if(!StringUtil.isEmpty(dashSwitchActionValue) && MessageDigest.isEqual(dashSwitchActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_KPI.getBytes(StandardCharsets.UTF_8))) {
            SearchResult<DashKpiPoolQuery> searchResult;
            if(!StringUtil.isEmpty(hciAddress)) {
                //copy SearchParam for searchAllParam
                SearchParam searchAllParam = CopyUtil.copyMutableObject(searchParam);
                searchAllParam.setPageSize(-1);
                //get all appGroupIds
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_KPI_TASK, searchAllParam);
                searchResult = mohHcsaBeDashboardService.getDashKpiPoolResult(searchAllParam);
                //set all address data map for filter address
                List<String> appGroupIds = mohHcsaBeDashboardService.getKpiPoolAppGrpIdByResult(searchResult);
                HcsaTaskAssignDto hcsaTaskAssignDto = mohHcsaBeDashboardService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
                //filter unit no for group
                searchParam = mohHcsaBeDashboardService.setAppGrpIdsByUnitNos(searchParam, hciAddress, hcsaTaskAssignDto, "T1.ID", APP_GROUP_LIST);
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_KPI_TASK, searchParam);
                searchResult = mohHcsaBeDashboardService.getDashKpiPoolResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, HCSA_TASK_ASSIGN_DTO, hcsaTaskAssignDto);
            } else {
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_KPI_TASK, searchParam);
                searchResult = mohHcsaBeDashboardService.getDashKpiPoolResult(searchParam);
            }
            searchResult = mohHcsaBeDashboardService.getDashKpiPoolOtherData(searchResult);
            if (searchResult != null && searchResult.getRowCount() > 0) {
                for (DashKpiPoolQuery dcp : searchResult.getRows()) {
                    groupNos.add(dcp.getAppGroupNo());
                }
            }
            //set session
            ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_RESULT, searchResult);
        } else if(!StringUtil.isEmpty(dashSwitchActionValue) && MessageDigest.isEqual(dashSwitchActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_ASSIGN_ME.getBytes(StandardCharsets.UTF_8))) {
            SearchResult<DashAssignMeQueryDto> searchResult;
            if(!StringUtil.isEmpty(hciAddress)) {
                //copy SearchParam for searchAllParam
                SearchParam searchAllParam = CopyUtil.copyMutableObject(searchParam);
                searchAllParam.setPageSize(-1);
                //get all appGroupIds
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASHASSIGNME, searchAllParam);
                searchResult = mohHcsaBeDashboardService.getDashAssignMeResult(searchAllParam);
                //set all address data map for filter address
                List<String> appGroupIds = mohHcsaBeDashboardService.getAssignMeAppGrpIdByResult(searchResult);
                HcsaTaskAssignDto hcsaTaskAssignDto = mohHcsaBeDashboardService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
                //filter unit no for group
                searchParam = mohHcsaBeDashboardService.setAppGrpIdsByUnitNos(searchParam, hciAddress, hcsaTaskAssignDto, T_ID, APP_GROUP_LIST);
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASHASSIGNME, searchParam);
                searchResult = mohHcsaBeDashboardService.getDashAssignMeResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, HCSA_TASK_ASSIGN_DTO, hcsaTaskAssignDto);
            } else {
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASHASSIGNME, searchParam);
                searchResult = mohHcsaBeDashboardService.getDashAssignMeResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, HCSA_TASK_ASSIGN_DTO, null);
            }
            searchResult = mohHcsaBeDashboardService.getDashAssignMeOtherData(searchResult);
            if (searchResult != null && searchResult.getRowCount() > 0) {
                for (DashAssignMeQueryDto dcp : searchResult.getRows()) {
                    groupNos.add(dcp.getAppGroupNo());
                }
            }
            //set session
            ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_RESULT, searchResult);
        } else if(!StringUtil.isEmpty(dashSwitchActionValue) && MessageDigest.isEqual(dashSwitchActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_GROUP.getBytes(StandardCharsets.UTF_8))) {
            SearchResult<DashWorkTeamQueryDto> searchResult;
            if(!StringUtil.isEmpty(hciAddress)) {
                //copy SearchParam for searchAllParam
                SearchParam searchAllParam = CopyUtil.copyMutableObject(searchParam);
                searchAllParam.setPageSize(-1);
                //get all appGroupIds
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_SUPERVIDOR_TASK, searchAllParam);
                searchResult = mohHcsaBeDashboardService.getDashWorkTeamResult(searchAllParam);
                //set all address data map for filter address
                List<String> appGroupIds = mohHcsaBeDashboardService.getSuperPoolAppGrpIdByResult(searchResult);
                HcsaTaskAssignDto hcsaTaskAssignDto = mohHcsaBeDashboardService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
                //filter unit no for group
                searchParam = mohHcsaBeDashboardService.setAppGrpIdsByUnitNos(searchParam, hciAddress, hcsaTaskAssignDto, T_ID, APP_GROUP_LIST);
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_SUPERVIDOR_TASK, searchParam);
                searchResult = mohHcsaBeDashboardService.getDashWorkTeamResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, HCSA_TASK_ASSIGN_DTO, hcsaTaskAssignDto);
            } else {
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_SUPERVIDOR_TASK, searchParam);
                searchResult = mohHcsaBeDashboardService.getDashWorkTeamResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, HCSA_TASK_ASSIGN_DTO, null);
            }
            searchResult = mohHcsaBeDashboardService.getDashWorkTeamOtherData(searchResult);
            if (searchResult != null && searchResult.getRowCount() > 0) {
                for (DashWorkTeamQueryDto dcp : searchResult.getRows()) {
                    groupNos.add(dcp.getAppGroupNo());
                }
            }
            //set session
            ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_RESULT, searchResult);
        } else if(!StringUtil.isEmpty(dashSwitchActionValue) && MessageDigest.isEqual(dashSwitchActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_REPLY.getBytes(StandardCharsets.UTF_8))) {
            SearchResult<DashReplyQueryDto> searchResult;
            if(!StringUtil.isEmpty(hciAddress)) {
                //copy SearchParam for searchAllParam
                SearchParam searchAllParam = CopyUtil.copyMutableObject(searchParam);
                searchAllParam.setPageSize(-1);
                //get all appGroupIds
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_APP_REPLY_TASK, searchAllParam);
                searchResult = mohHcsaBeDashboardService.getDashReplyResult(searchAllParam);
                //set all address data map for filter address
                List<String> appGroupIds = mohHcsaBeDashboardService.getReplyAppGrpIdByResult(searchResult);
                HcsaTaskAssignDto hcsaTaskAssignDto = mohHcsaBeDashboardService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
                //filter unit no for group
                searchParam = mohHcsaBeDashboardService.setAppGrpIdsByUnitNos(searchParam, hciAddress, hcsaTaskAssignDto, "T7.ID", APP_GROUP_LIST);
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_APP_REPLY_TASK, searchParam);
                searchResult = mohHcsaBeDashboardService.getDashReplyResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, HCSA_TASK_ASSIGN_DTO, hcsaTaskAssignDto);
            } else {
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_APP_REPLY_TASK, searchParam);
                searchResult = mohHcsaBeDashboardService.getDashReplyResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, HCSA_TASK_ASSIGN_DTO, null);
            }
            searchResult = mohHcsaBeDashboardService.getDashReplyOtherData(searchResult);
            if (searchResult != null && searchResult.getRowCount() > 0) {
                for (DashReplyQueryDto dcp : searchResult.getRows()) {
                    groupNos.add(dcp.getAppGroupNo());
                }
            }
            //set session
            ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_RESULT, searchResult);
        } else if(!StringUtil.isEmpty(dashSwitchActionValue) && MessageDigest.isEqual(dashSwitchActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_WAIT.getBytes(StandardCharsets.UTF_8))) {
            SearchResult<DashWaitApproveQueryDto> searchResult;
            if(!StringUtil.isEmpty(hciAddress)) {
                //copy SearchParam for searchAllParam
                SearchParam searchAllParam = CopyUtil.copyMutableObject(searchParam);
                searchAllParam.setPageSize(-1);
                //get all appGroupIds
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_WAIT_APPROVE_TASK, searchAllParam);
                searchResult = mohHcsaBeDashboardService.getDashWaitApproveResult(searchAllParam);
                //set all address data map for filter address
                List<String> appGroupIds = mohHcsaBeDashboardService.getWaitApproveAppGrpIdByResult(searchResult);
                HcsaTaskAssignDto hcsaTaskAssignDto = mohHcsaBeDashboardService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
                //filter unit no for group
                searchParam = mohHcsaBeDashboardService.setAppGrpIdsByUnitNos(searchParam, hciAddress, hcsaTaskAssignDto, "T7.ID", APP_GROUP_LIST);
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_WAIT_APPROVE_TASK, searchParam);
                searchResult = mohHcsaBeDashboardService.getDashWaitApproveResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, HCSA_TASK_ASSIGN_DTO, hcsaTaskAssignDto);
            } else {
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_WAIT_APPROVE_TASK, searchParam);
                searchResult = mohHcsaBeDashboardService.getDashWaitApproveResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, HCSA_TASK_ASSIGN_DTO, null);
            }
            searchResult = mohHcsaBeDashboardService.getDashWaitApproveOtherData(searchResult);
            if (searchResult != null && searchResult.getRowCount() > 0) {
                for (DashWaitApproveQueryDto dcp : searchResult.getRows()) {
                    groupNos.add(dcp.getAppGroupNo());
                }
            }
            //set session
            ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_RESULT, searchResult);
        } else if(!StringUtil.isEmpty(dashSwitchActionValue) && MessageDigest.isEqual(dashSwitchActionValue.getBytes(StandardCharsets.UTF_8),BeDashboardConstant.SWITCH_ACTION_RE_RENEW.getBytes(StandardCharsets.UTF_8))) {
            SearchResult<DashRenewQueryDto> searchResult;
            if(!StringUtil.isEmpty(hciAddress)) {
                //copy SearchParam for searchAllParam
                SearchParam searchAllParam = CopyUtil.copyMutableObject(searchParam);
                searchAllParam.setPageSize(-1);
                //get all appGroupIds
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_APP_RENEW_TASK, searchAllParam);
                searchResult = mohHcsaBeDashboardService.getDashRenewResult(searchAllParam);
                //set all address data map for filter address
                List<String> appGroupIds = mohHcsaBeDashboardService.getRenewAppGrpIdByResult(searchResult);
                HcsaTaskAssignDto hcsaTaskAssignDto = mohHcsaBeDashboardService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
                //filter unit no for group
                searchParam = mohHcsaBeDashboardService.setAppGrpIdsByUnitNos(searchParam, hciAddress, hcsaTaskAssignDto, "T1.ID", APP_GROUP_LIST);
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_APP_RENEW_TASK, searchParam);
                searchResult = mohHcsaBeDashboardService.getDashRenewResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, HCSA_TASK_ASSIGN_DTO, hcsaTaskAssignDto);
            } else {
                QueryHelp.setMainSql(INTRADASHBOARDQUERY, DASH_APP_RENEW_TASK, searchParam);
                searchResult = mohHcsaBeDashboardService.getDashRenewResult(searchParam);
                ParamUtil.setSessionAttr(bpc.request, HCSA_TASK_ASSIGN_DTO, null);
            }
            searchResult = mohHcsaBeDashboardService.getDashRenewOtherData(searchResult);
            if (searchResult != null && searchResult.getRowCount() > 0) {
                for (DashRenewQueryDto dcp : searchResult.getRows()) {
                    groupNos.add(dcp.getAppGroupNo());
                }
            }
            //set session
            ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_RESULT, searchResult);
        }
        ParamUtil.setSessionAttr(bpc.request, DASH_SEARCH_PARAM, searchParam);
        // For expand CR
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false, null);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc, boolean isNew, String className){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, DASH_SEARCH_PARAM);
        int pageSize = SystemParamUtil.getDefaultPageSize();
        if(searchParam == null || isNew){
            searchParam = new SearchParam(className);
            searchParam.setPageSize(pageSize);
            searchParam.setPageNo(1);
            searchParam.setSort("SUBMIT_DT", SearchParam.ASCENDING);
            ParamUtil.setSessionAttr(bpc.request, DASH_HCI_ADDRESS, null);
        }
        return searchParam;
    }

    /**
     * @Function: Routing Task
     *
     * @param bpc
     */
    private void routingTask(BaseProcessClass bpc, String stageId, String appStatus, String roleId, ApplicationViewDto applicationViewDto, TaskDto taskDto) throws FeignException, CloneNotSupportedException, TemplateException, IOException {

        //get the user for this applicationNo
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        AppPremisesCorrelationDto newAppPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();
        String newCorrelationId = newAppPremisesCorrelationDto.getId();
        BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
        String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
        BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();
        List<String> applicationDtoIds = (List<String>) ParamUtil.getSessionAttr(bpc.request,BACKEND_INBOX_APPROVE);

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

        Map<String, String> returnFeeMap = (Map<String, String>) ParamUtil.getSessionAttr(bpc.request,BACKEND_INBOX_RETURN_FEE);
        if(returnFeeMap == null){
            returnFeeMap = IaisCommonUtils.genNewHashMap();
        }
        returnFeeMap.put(applicationDto.getApplicationNo(),appStatus);
        ParamUtil.setSessionAttr(bpc.request,BACKEND_INBOX_RETURN_FEE,(Serializable) returnFeeMap);
        log.info(StringUtil.changeForLog("BackendInboxReturnFee:" + JsonUtil.parseToJson(returnFeeMap)));
        beDashboardSupportService.changePostInsForTodoAudit(applicationViewDto);
        //set risk score
        beDashboardSupportService.setRiskScore(applicationDto,newCorrelationId);
        //appeal save return fee
        if(ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus) && ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){
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
        //complated this task and create the history
        broadcastOrganizationDto.setRollBackComplateTask(CopyUtil.copyMutableObject(taskDto));
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
        broadcastApplicationDto.setRollBackApplicationDto(CopyUtil.copyMutableObject(applicationDto));
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
                    broadcastOrganizationDto.setRollBackworkingGroupDto(CopyUtil.copyMutableObject(workingGroupDto));
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

            String aoapprove = (String)ParamUtil.getSessionAttr(bpc.request,BE_MAIN_AO_APPROVE);
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
                if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(appStatus)){
                    List<ApplicationDto> applicationDtos=IaisCommonUtils.genNewArrayList();
                    applicationDtos.add(applicationDto);
                    //get and set return fee
                    applicationDtos = hcsaConfigMainClient.returnFee(applicationDtos).getEntity();
                    //save return fee
                    beDashboardSupportService.saveRejectReturnFee(applicationDtos, broadcastApplicationDto);
                }
                if(needUpdateGroup || applicationDto.isFastTracking()){
                    //update application Group status
                    ApplicationGroupDto applicationGroupDto = applicationViewService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
                    broadcastApplicationDto.setRollBackApplicationGroupDto(CopyUtil.copyMutableObject(applicationGroupDto));
                    String appStatusIsAllRejected = beDashboardSupportService.checkAllStatus(saveApplicationDtoList,applicationDtoIds);
                    String appgroupName = applicationDto.getAppGrpId() + "backendAppGroupStatus";
                    String sessionStatus = (String) ParamUtil.getSessionAttr(bpc.request,appgroupName);
                    if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(appStatusIsAllRejected) &&  ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(sessionStatus)){
                        applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_REJECT);
                    }else{
                        applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
                        List<ApplicationGroupDto> applicationGroupDtoList=IaisCommonUtils.genNewArrayList();
                        applicationGroupDtoList.add(applicationGroupDto);
                        applicationViewService.syncFeApplicationGroupStatus(applicationGroupDtoList);

                    }
                    applicationGroupDto.setAo3ApprovedDt(new Date());
                    applicationGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    broadcastApplicationDto.setApplicationGroupDto(applicationGroupDto);

                    //update current application status in db search result
                    Map<String, String> returnFee = (Map<String, String>)ParamUtil.getSessionAttr(bpc.request, BACKEND_INBOX_RETURN_FEE);
                    beDashboardSupportService.updateCurAppStatusByLicensee(returnFee, saveApplicationDtoList, licenseeId);

                    for (ApplicationDto viewitem:saveApplicationDtoList
                    ) {
                        log.info(StringUtil.changeForLog("****viewitem ***** " + viewitem.getApplicationNo()));
                    }
                    if(needUpdateGroup){
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
            } else if (ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationType) && ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus)) {
                broadcastApplicationDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
            }
        }
        //set appSvcVehicleDto
        broadcastApplicationDto = broadcastService.setAppSvcVehicleDtoByAppView(broadcastApplicationDto, applicationViewDto, appStatus, applicationType);
        broadcastApplicationDto = broadcastService.setAppPremSubSvcDtoByAppView(broadcastApplicationDto, applicationViewDto, appStatus, applicationType);
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
        } else if (ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationType) && ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus)) {
            broadcastApplicationDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
        }
        broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,bpc.process,submissionId);
        broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,bpc.process,submissionId);
        //0062460 update FE  application status.
        applicationViewService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());

        ApplicationDto withdrawApplicationDto = broadcastApplicationDto.getApplicationDto();
        if (withdrawApplicationDto != null && ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(withdrawApplicationDto.getApplicationType())) {
            /**
             * Send Withdrawal Application Email
             14      */
            String applicantName = "";
            String serviceId = applicationViewDto.getApplicationDto().getServiceId();
            AppPremiseMiscDto premiseMiscDto = cessationMainClient.getAppPremiseMiscDtoByAppId(applicationDto.getId()).getEntity();
            if (premiseMiscDto != null) {
                String oldAppId = premiseMiscDto.getRelateRecId();
                if (!StringUtil.isEmpty(oldAppId)) {
                    ApplicationDto oldApplication = applicationMainClient.getApplicationById(oldAppId).getEntity();
                    String applicationNo = oldApplication.getApplicationNo();
                    String applicationType1 = oldApplication.getApplicationType();
                    ApplicationGroupDto applicationGroupDto = applicationMainClient.getAppById(oldApplication.getAppGrpId()).getEntity();
                    OrgUserDto orgUserDto = organizationMainClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                    if (orgUserDto != null) {
                        applicantName = orgUserDto.getDisplayName();
                    }
                    if (ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(withdrawApplicationDto.getStatus())) {
                        try {
                            if(!oldApplication.getStatus().equals(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION)){
                                //WITHDRAWAL To restore the old task
                                AppPremisesCorrelationDto appPremisesCorrelationDto=applicationMainClient.getAppPremCorrByAppNo(applicationNo).getEntity();
                                String corrId=appPremisesCorrelationDto.getId();
                                List<TaskDto> taskDtos = organizationMainClient.getTasksByRefNo(corrId).getEntity();
                                TaskDto oldTaskDto=taskDtos.get(0);
                                oldTaskDto.setTaskStatus(TaskConsts.TASK_STATUS_READ);
                                oldTaskDto.setId(null);
                                taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                                List<TaskDto> newTaskDto=IaisCommonUtils.genNewArrayList();
                                newTaskDto.add(oldTaskDto);
                                taskService.createTasks(newTaskDto);
                            }
                        }catch (Exception e){
                            log.error(e.getMessage(),e);
                        }
                        Map<String, Object> msgInfoMap = IaisCommonUtils.genNewHashMap();
                        msgInfoMap.put(APPLICATION_NUMBER, applicationNo);
                        msgInfoMap.put(APPLICATION_TYPE, MasterCodeUtil.getCodeDesc(applicationType1));
                        msgInfoMap.put("Applicant", applicantName);
                        msgInfoMap.put("ApplicationDate", Formatter.formatDateTime(applicationGroupDto.getSubmitDt()));
                        msgInfoMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
                        sendEmail(MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_REJECT_EMAIL, msgInfoMap, oldApplication);
                        sendInboxMessage(oldApplication, serviceId, msgInfoMap, MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_REJECT_MESSAGE);
                        sendSMS(oldApplication, MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_REJECT_SMS, msgInfoMap);
                    }
                }
            }
        }
    }

    /**
     * @Function: Route Back
     *
     * @param bpc
     */
    private void rollBack(BaseProcessClass bpc, ApplicationViewDto applicationViewDto,String appStatus, TaskDto taskDto ,AppPremisesRoutingHistoryDto historyDto) throws CloneNotSupportedException {
        String wrkGpId=historyDto.getWrkGrpId();
        String roleId=historyDto.getRoleId();
        String stageId=historyDto.getStageId();
        String userId=historyDto.getActionby();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
        BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();

        //complated this task and create the history
        String subStageId = null;
        broadcastOrganizationDto.setRollBackComplateTask(CopyUtil.copyMutableObject(taskDto));
        taskDto = beDashboardSupportService.completedTask(taskDto);
        broadcastOrganizationDto.setComplateTask(taskDto);
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        String processDecision = ParamUtil.getString(bpc.request,NEXT_STAGE);
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
        broadcastApplicationDto.setRollBackApplicationDto(CopyUtil.copyMutableObject(applicationDto));
        applicationDto.setStatus(appStatus);
        broadcastApplicationDto.setApplicationDto(applicationDto);
        String taskType = TaskConsts.TASK_TYPE_MAIN_FLOW;
        String taskUrl = TaskConsts.TASK_PROCESS_URL_MAIN_FLOW;
        if(HcsaConsts.ROUTING_STAGE_INS.equals(stageId) && !ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS.equals(appStatus)){
            taskType = TaskConsts.TASK_TYPE_INSPECTION;
            if(RoleConsts.USER_ROLE_AO1.equals(roleId)){
                taskUrl = TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT_REVIEW_AO1;
            }else if((!RoleConsts.USER_ROLE_AO2.equals(roleId)) &&
                    (!RoleConsts.USER_ROLE_AO3.equals(roleId)) &&
                    (!RoleConsts.USER_ROLE_ASO.equals(roleId)) &&
                    (!RoleConsts.USER_ROLE_PSO.equals(roleId))
            ){
                taskUrl = TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT;
            }
            subStageId = HcsaConsts.ROUTING_STAGE_POT;
            //update inspector status
            mohHcsaBeDashboardService.updateInspectionStatus(applicationViewDto.getAppPremisesCorrelationId(),InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
        }
        //reply inspector
        if(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS.equals(appStatus)){
            List<TaskDto> taskDtos = organizationMainClient.getTaskByAppNoStatus(applicationDto.getApplicationNo(), TaskConsts.TASK_STATUS_COMPLETED, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION).getEntity();
            taskType = taskDtos.get(0).getTaskType();
            taskUrl = TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION;
            subStageId = HcsaConsts.ROUTING_STAGE_PRE;
            //update inspector status
            mohHcsaBeDashboardService.updateInspectionStatus(applicationViewDto.getAppPremisesCorrelationId(),InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
        }
        //DMS go to main flow
        if(ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(appStatus)){
            taskType = TaskConsts.TASK_TYPE_MAIN_FLOW;
            taskUrl = TaskConsts.TASK_PROCESS_URL_MAIN_FLOW;
        }

        TaskDto newTaskDto = TaskUtil.getTaskDto(applicationDto.getApplicationNo(),stageId,taskType,
                taskDto.getRefNo(),TaskConsts.TASK_STATUS_PENDING,wrkGpId, userId,new Date(),null,0,taskUrl,roleId,
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
        broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,bpc.process,submissionId);
        broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,bpc.process,submissionId);

        //0062460 update FE  application status.
        applicationViewService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());
    }

    /**
     * @Function: replay
     *
     * @param bpc
     */
    private void replay(BaseProcessClass bpc, ApplicationViewDto applicationViewDto, TaskDto taskDto) throws FeignException, CloneNotSupportedException, TemplateException, IOException {
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
        String stageId=appPremisesRoutingHistoryDto.getStageId();
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
            rollBack(bpc,applicationViewDto,nextStatus,taskDto,appPremisesRoutingHistoryDto);
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
                rollBack(bpc,applicationViewDto,nextStatus,taskDto,appPremisesRoutingHistoryDto);
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
                rollBack(bpc,applicationViewDto,ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS,taskDto,appPremisesRoutingHistoryDto);
            }else{
                log.debug(StringUtil.changeForLog("can not get the appPremisesRoutingHistoryDto ..."));
            }
        }else{
            log.debug(StringUtil.changeForLog("do not have the applicaiton"));
        }
        log.info(StringUtil.changeForLog("the do routeToDMS end ...."));
    }

    public void expandAppGroup(HttpServletRequest request, ArrayList<String> groupNos) {
        if (IaisCommonUtils.isEmpty(groupNos)) {
            return;
        }
        SearchResult result = (SearchResult) ParamUtil.getSessionAttr(request, DASH_SEARCH_RESULT);

        if (result == null || result.getRowCount() == 0) {
            return;
        }
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        Map<String, String> taskIdMap = IaisCommonUtils.genNewHashMap();
        String dashFilterAppNo = (String)ParamUtil.getSessionAttr(request, DASH_FILTER_APP_NO);
        String dashAppStatus = (String)ParamUtil.getSessionAttr(request, DASH_APP_STATUS);
        HcsaTaskAssignDto hcsaTaskAssignDto = (HcsaTaskAssignDto)ParamUtil.getSessionAttr(request, HCSA_TASK_ASSIGN_DTO);
        //address for second search
        String hciAddress = (String)ParamUtil.getSessionAttr(request, DASH_HCI_ADDRESS);
        String switchAction = request.getParameter("switchActionParam");
        if (StringUtil.isEmpty(switchAction)) {
            switchAction = (String)ParamUtil.getSessionAttr(request, DASH_SWITCH_ACTION_VALUE);
        }
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        SearchParam searchParamGroup = (SearchParam) ParamUtil.getSessionAttr(request, DASH_SEARCH_PARAM);
        //set dash support flag
        map.put("dashSupportFlag", AppConsts.FALSE);
        if (BeDashboardConstant.SWITCH_ACTION_COMMON.equals(switchAction)) {
            map = beDashboardAjaxService.getCommonDropdownResultOnce(groupNos, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo,
                    hcsaTaskAssignDto, hciAddress);
            SearchResult<DashComPoolAjaxQueryDto> ajaxResult = (SearchResult<DashComPoolAjaxQueryDto>) map.get(AJAXRESULT);
            if (ajaxResult != null && ajaxResult.getRowCount() > 0) {
                for (DashComPoolAjaxQueryDto dto : ajaxResult.getRows()) {
                    taskIdMap.put(dto.getApplicationNo(), dto.getTaskId());
                }
            }
        } else if(BeDashboardConstant.SWITCH_ACTION_ASSIGN_ME.equals(switchAction)) {
            map = beDashboardAjaxService.getAssignMeDropdownResultOnce(groupNos, loginContext, map, searchParamGroup, dashFilterAppNo, dashAppStatus,
                    hcsaTaskAssignDto, hciAddress);
            //set dash support flag
            if(loginContext != null && map != null) {
                String curRole = loginContext.getCurRoleId();
                if(!StringUtil.isEmpty(curRole) && curRole.contains(RoleConsts.USER_ROLE_AO1) ||
                        curRole.contains(RoleConsts.USER_ROLE_AO2) ||
                        curRole.contains(RoleConsts.USER_ROLE_AO3)) {
                        map.put("dashSupportFlag", AppConsts.TRUE);
                }
            }
            SearchResult<DashAssignMeAjaxQueryDto> ajaxResult = (SearchResult<DashAssignMeAjaxQueryDto>) map.get(AJAXRESULT);
            if (ajaxResult != null && ajaxResult.getRowCount() > 0) {
                for (DashAssignMeAjaxQueryDto dto : ajaxResult.getRows()) {
                    taskIdMap.put(dto.getApplicationNo(), dto.getTaskId());
                }
            }
        } else if(BeDashboardConstant.SWITCH_ACTION_REPLY.equals(switchAction)) {
            map = beDashboardAjaxService.getReplyDropdownResultOnce(groupNos, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo,
                    hcsaTaskAssignDto, hciAddress);
            SearchResult<DashReplyAjaxQueryDto> ajaxResult = (SearchResult<DashReplyAjaxQueryDto>) map.get(AJAXRESULT);
            if (ajaxResult != null && ajaxResult.getRowCount() > 0) {
                for (DashReplyAjaxQueryDto dto : ajaxResult.getRows()) {
                    taskIdMap.put(dto.getApplicationNo(), dto.getTaskId());
                }
            }
        } else if(BeDashboardConstant.SWITCH_ACTION_KPI.equals(switchAction)) {
            map = beDashboardAjaxService.getKpiDropdownResultOnce(groupNos, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo, dashAppStatus,
                    hcsaTaskAssignDto, hciAddress);
            SearchResult<DashKpiPoolAjaxQuery> ajaxResult = (SearchResult<DashKpiPoolAjaxQuery>) map.get(AJAXRESULT);
            if (ajaxResult != null && ajaxResult.getRowCount() > 0) {
                for (DashKpiPoolAjaxQuery dto : ajaxResult.getRows()) {
                    taskIdMap.put(dto.getApplicationNo(), dto.getTaskId());
                }
            }
        } else if(BeDashboardConstant.SWITCH_ACTION_RE_RENEW.equals(switchAction)) {
            map = beDashboardAjaxService.getRenewDropdownResultOnce(groupNos, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo, dashAppStatus,
                    hcsaTaskAssignDto, hciAddress);
            SearchResult<DashRenewAjaxQueryDto> ajaxResult = (SearchResult<DashRenewAjaxQueryDto>) map.get(AJAXRESULT);
            if (ajaxResult != null && ajaxResult.getRowCount() > 0) {
                for (DashRenewAjaxQueryDto dto : ajaxResult.getRows()) {
                    taskIdMap.put(dto.getApplicationNo(), dto.getTaskId());
                }
            }
        } else if(BeDashboardConstant.SWITCH_ACTION_WAIT.equals(switchAction)) {
            map = beDashboardAjaxService.getWaitApproveDropResultOnce(groupNos, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo, dashAppStatus,
                    hcsaTaskAssignDto, hciAddress);
            SearchResult<DashWaitApproveAjaxQueryDto> ajaxResult = (SearchResult<DashWaitApproveAjaxQueryDto>) map.get(AJAXRESULT);
            if (ajaxResult != null && ajaxResult.getRowCount() > 0) {
                for (DashWaitApproveAjaxQueryDto dto : ajaxResult.getRows()) {
                    taskIdMap.put(dto.getApplicationNo(), dto.getTaskId());
                }
            }
        } else if(BeDashboardConstant.SWITCH_ACTION_GROUP.equals(switchAction)) {
            String dashCommonPoolStatus = (String)ParamUtil.getSessionAttr(request, DASH_COMMON_POOL_STATUS);
            map = beDashboardAjaxService.getWorkTeamDropdownResultOnce(groupNos, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo,
                    dashCommonPoolStatus, dashAppStatus, hcsaTaskAssignDto, hciAddress);
            SearchResult<DashWorkTeamAjaxQueryDto> ajaxResult = (SearchResult<DashWorkTeamAjaxQueryDto>) map.get(AJAXRESULT);
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
            setDashAssignMeUrl(map, request, taskMap, hisMap, slaTrackingMap);
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
            SearchResult<DashComPoolAjaxQueryDto> ajaxResult = (SearchResult<DashComPoolAjaxQueryDto>) map.get(AJAXRESULT);
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
                            String maskId = MaskUtil.maskValue(TASK_ID, dashComPoolAjaxQueryDto.getTaskId());
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

    private Map<String, Object> setDashAssignMeUrl(Map<String, Object> map, HttpServletRequest request,
                                                   Map<String, TaskDto> taskMap, Map<String, AppPremisesRoutingHistoryDto> hisMap,
                                                   Map<String, AppStageSlaTrackingDto> slaTrackingMap) {
        if (map != null) {
            SearchResult<DashAssignMeAjaxQueryDto> ajaxResult = (SearchResult<DashAssignMeAjaxQueryDto>) map.get(AJAXRESULT);
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
                            String maskId = MaskUtil.maskValue(TASK_ID, dashAssignMeAjaxQueryDto.getTaskId());
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
            SearchResult<DashReplyAjaxQueryDto> ajaxResult = (SearchResult<DashReplyAjaxQueryDto>) map.get(AJAXRESULT);
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
            log.info(StringUtil.changeForLog("Dashboard-Kpi-Pool-user-Id =====" + loginContext.getUserId()));
            log.info(StringUtil.changeForLog("Dashboard=Kpi=Pool=Role=Id =====" + loginContext.getCurRoleId()));
            userId = loginContext.getUserId();
            roleId = loginContext.getCurRoleId();
        }
        if(map != null) {
            SearchResult<DashKpiPoolAjaxQuery> ajaxResult = (SearchResult<DashKpiPoolAjaxQuery>) map.get(AJAXRESULT);
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
                                String maskId = MaskUtil.maskValue(TASK_ID, dashKpiPoolAjaxQuery.getTaskId());
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
            log.info(StringUtil.changeForLog("Dashboard -- Kpi -- Pool --user Id =====" + loginContext.getUserId()));
            log.info(StringUtil.changeForLog("Dashboard Kpi Pool ==Role ==Id =====" + loginContext.getCurRoleId()));
            userId = loginContext.getUserId();
            roleId = loginContext.getCurRoleId();
        }
        if (map != null) {
            SearchResult<DashRenewAjaxQueryDto> ajaxResult = (SearchResult<DashRenewAjaxQueryDto>) map.get(AJAXRESULT);
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
                                String maskId = MaskUtil.maskValue(TASK_ID, dashRenewAjaxQueryDto.getTaskId());
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
            log.info(StringUtil.changeForLog("-Dashboard =-Kpi Pool -=user -Id =====" + loginContext.getUserId()));
            log.info(StringUtil.changeForLog("=Dashboard -=Kpi Pool -=Role Id= =====" + loginContext.getCurRoleId()));
            userId = loginContext.getUserId();
            roleId = loginContext.getCurRoleId();
        }
        if(map != null) {
            SearchResult<DashWaitApproveAjaxQueryDto> ajaxResult = (SearchResult<DashWaitApproveAjaxQueryDto>) map.get(AJAXRESULT);
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
                                String maskId = MaskUtil.maskValue(TASK_ID, dashWaitApproveAjaxQueryDto.getTaskId());
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
            SearchResult<DashWorkTeamAjaxQueryDto> ajaxResult = (SearchResult<DashWorkTeamAjaxQueryDto>) map.get(AJAXRESULT);
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
                if (!StringUtil.isEmpty(stage) && (kpiMap != null && kpiMap.get(stage) != null)) {
                    kpi = kpiMap.get(stage);
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
        if(remThreshold != 0 && kpi != 0) {
            if (remThreshold <= days && days <= kpi) {
                colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_AMBER;
            } else if (days > kpi) {
                colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_RED;
            }
        }
        return colour;
    }

    private Map<String, String> validateCanApprove(ApplicationViewDto applicationViewDto) {
        return restTemplate.postForObject(CAN_APPROVE_API_URL, applicationViewDto, Map.class);
    }

    public void sendEmail(String msgId, Map<String, Object> msgInfoMap, ApplicationDto applicationDto) throws IOException, TemplateException {
        log.info(StringUtil.changeForLog("***************** send withdraw application Email  *****************"));
        MsgTemplateDto msgTemplateDto = msgTemplateMainClient.getMsgTemplate(msgId).getEntity();
        EmailParam emailParam = new EmailParam();
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put(APPLICATION_TYPE, MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        map.put(APPLICATION_NUMBER, applicationDto.getApplicationNo());
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map);
        emailParam.setTemplateContent(msgInfoMap);
        emailParam.setTemplateId(msgId);
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setSubject(subject);
        log.info(StringUtil.changeForLog("***************** send withdraw application Email  end*****************"));
        notificationHelper.sendNotification(emailParam);
    }

    public void sendSMS(ApplicationDto applicationDto, String msgId, Map<String, Object> msgInfoMap) throws IOException, TemplateException {
        log.info(StringUtil.changeForLog("***************** send withdraw application sms  *****************"));
        EmailParam emailParam = new EmailParam();
        MsgTemplateDto msgTemplateDto = msgTemplateMainClient.getMsgTemplate(msgId).getEntity();
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put(APPLICATION_TYPE, MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        map.put(APPLICATION_NUMBER, applicationDto.getApplicationNo());
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map);
        emailParam.setTemplateId(msgId);
        emailParam.setTemplateContent(msgInfoMap);
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setSubject(subject);
        notificationHelper.sendNotification(emailParam);
    }

    public void sendInboxMessage(ApplicationDto applicationDto, String serviceId, Map<String, Object> map, String messageTemplateId) throws IOException, TemplateException {
        EmailParam messageParam = new EmailParam();
        HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        if (serviceDto != null) {
            svcCodeList.add(serviceDto.getSvcCode());
        }
        MsgTemplateDto msgTemplateDto = msgTemplateMainClient.getMsgTemplate(messageTemplateId).getEntity();
        Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
        subMap.put(APPLICATION_NUMBER, applicationDto.getApplicationNo());
        subMap.put(APPLICATION_TYPE, MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), subMap);
        messageParam.setTemplateId(messageTemplateId);
        messageParam.setQueryCode(applicationDto.getApplicationNo());
        messageParam.setTemplateContent(map);
        messageParam.setReqRefNum(applicationDto.getApplicationNo());
        messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        messageParam.setRefId(applicationDto.getApplicationNo());
        messageParam.setSubject(subject);
        messageParam.setSvcCodeList(svcCodeList);
        log.info(StringUtil.changeForLog("send withdraw Application approve message"));
        notificationHelper.sendNotification(messageParam);
    }
}