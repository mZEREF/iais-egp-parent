package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.moh.iais.action.LoginAccessCheck;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.BeDashboardConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppSpecifiedLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.PoolRoleCheckDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashAppDetailsQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashAssignMeAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashComPoolAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashKpiPoolAjaxQuery;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashRenewAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashReplyAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashWaitApproveAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashWorkTeamAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewMainService;
import com.ecquaria.cloud.moh.iais.service.BeDashboardAjaxService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.CessationMainClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskMainClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Shicheng
 * @date 2021/4/14 13:02
 **/
@Slf4j
@Controller
@RequestMapping("/hcsa/intranet/dashboard")
public class MohHcsaBeDashboardAjax implements LoginAccessCheck {

    @Autowired
    private InspectionMainAssignTaskService inspectionAssignTaskService;

    @Autowired
    private ApplicationViewMainService applicationViewMainService;

    @Autowired
    private BeDashboardAjaxService beDashboardAjaxService;

    @Autowired
    private HcsaConfigMainClient hcsaConfigClient;

    @Autowired
    private InspectionTaskMainClient inspectionTaskMainClient;

    @Autowired
    private HcsaAppClient hcsaAppClient;

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private CessationMainClient cessationMainClient;

    @Autowired
    private TaskService taskService;

    private static final String[] reasonArr = new String[]{ApplicationConsts.CESSATION_REASON_NOT_PROFITABLE, ApplicationConsts.CESSATION_REASON_REDUCE_WORKLOA, ApplicationConsts.CESSATION_REASON_OTHER};
    private static final String[] patientsArr = new String[]{ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI, ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO, ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_OTHER};

    private static final String AJAXRESULT    = "ajaxResult";
    private static final String TASKID        = "taskId";
    private static final String DASH_ROLE_SWITCH_FLAG = "dashRoleSwitchFlag";
    @PostMapping(value = "appGroup.do")
    @ResponseBody
    public Map<String, Object> appGroup(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        String dashFilterAppNo = (String)ParamUtil.getSessionAttr(request, "dashFilterAppNo");
        String dashAppStatus = (String)ParamUtil.getSessionAttr(request, "dashAppStatus");
        HcsaTaskAssignDto hcsaTaskAssignDto = (HcsaTaskAssignDto)ParamUtil.getSessionAttr(request, "hcsaTaskAssignDto");
        //address for second search
        String hciAddress = (String)ParamUtil.getSessionAttr(request, "dashHciAddress");
        String groupNo = request.getParameter("groupNo");
        String switchAction = request.getParameter("switchActionParam");
        if (StringUtil.isEmpty(switchAction)) {
            switchAction = (String)ParamUtil.getSessionAttr(request, "dashSwitchActionValue");
        }
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        SearchParam searchParamGroup = (SearchParam) ParamUtil.getSessionAttr(request, "dashSearchParam");
        //set dash support flag
        map.put("dashSupportFlag", AppConsts.FALSE);
        if(BeDashboardConstant.SWITCH_ACTION_COMMON.equals(switchAction)) {
            map = beDashboardAjaxService.getCommonDropdownResult(groupNo, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo,
                    hcsaTaskAssignDto, hciAddress);
            //set url and kpi color
            setDashComPoolUrl(map, loginContext);

        } else if(BeDashboardConstant.SWITCH_ACTION_ASSIGN_ME.equals(switchAction)) {
            map = beDashboardAjaxService.getAssignMeDropdownResult(groupNo, loginContext, map, searchParamGroup, dashFilterAppNo, dashAppStatus,
                    hcsaTaskAssignDto, hciAddress);
            //set url and kpi color
            setDashAssignMeUrl(map, request, loginContext);
            //set dash support flag
            if(loginContext != null && map != null) {
                String curRole = loginContext.getCurRoleId();
                if (!StringUtil.isEmpty(curRole) && (curRole.contains(RoleConsts.USER_ROLE_AO1) ||
                        curRole.contains(RoleConsts.USER_ROLE_AO2) ||
                        curRole.contains(RoleConsts.USER_ROLE_AO3))) {
                    map.put("dashSupportFlag", AppConsts.TRUE);
                }
            }

        } else if(BeDashboardConstant.SWITCH_ACTION_REPLY.equals(switchAction)) {
            map = beDashboardAjaxService.getReplyDropdownResult(groupNo, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo,
                    hcsaTaskAssignDto, hciAddress);
            //set url and kpi color
            setReplyPoolUrl(map);

        } else if(BeDashboardConstant.SWITCH_ACTION_KPI.equals(switchAction)) {
            map = beDashboardAjaxService.getKpiDropdownResult(groupNo, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo, dashAppStatus,
                    hcsaTaskAssignDto, hciAddress);
            //set url and kpi color
            setDashKpiPoolUrl(map, request, loginContext);

        } else if(BeDashboardConstant.SWITCH_ACTION_RE_RENEW.equals(switchAction)) {
            map = beDashboardAjaxService.getRenewDropdownResult(groupNo, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo, dashAppStatus,
                    hcsaTaskAssignDto, hciAddress);
            //set url and kpi color
            setDashRenewPoolUrl(map, request, loginContext);

        } else if(BeDashboardConstant.SWITCH_ACTION_WAIT.equals(switchAction)) {
            map = beDashboardAjaxService.getWaitApproveDropResult(groupNo, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo, dashAppStatus,
                    hcsaTaskAssignDto, hciAddress);
            //set url and kpi color
            setDashWaitApproveUrl(map, request, loginContext);

        } else if(BeDashboardConstant.SWITCH_ACTION_GROUP.equals(switchAction)) {
            String dashCommonPoolStatus = (String)ParamUtil.getSessionAttr(request, "dashCommonPoolStatus");
            map = beDashboardAjaxService.getWorkTeamDropdownResult(groupNo, loginContext, map, searchParamGroup, switchAction, dashFilterAppNo,
                    dashCommonPoolStatus, dashAppStatus, hcsaTaskAssignDto, hciAddress);
            //set url and kpi color
            setWorkTeamPoolUrl(map);
        }
        return map;
    }

    @PostMapping(value = "dashSysDetail.do")
    public @ResponseBody
    Map<String, Object> dashSysDetailAjax(HttpServletRequest request, HttpServletResponse response) {
        String dashFilterAppNo = (String)ParamUtil.getSessionAttr(request, "dashFilterAppNo");
        List<String> serviceList = (List<String>)ParamUtil.getSessionAttr(request, "dashSvcCheckList");
        List<String> appTypeList = (List<String>)ParamUtil.getSessionAttr(request, "dashAppTypeCheckList");
        SearchParam searchParamGroup = (SearchParam) ParamUtil.getSessionAttr(request, "dashSearchParam");
        String groupId = request.getParameter("groupId");
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        if(!StringUtil.isEmpty(groupId)){
            SearchParam searchParam = new SearchParam(DashAppDetailsQueryDto.class.getName());
            searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
            searchParam.setPageNo(1);
            searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
            //set filter
            dashSysDetailDropFilter(searchParam, groupId, serviceList, appTypeList, searchParamGroup, dashFilterAppNo);
            //search
            QueryHelp.setMainSql("intraDashboardQuery", "dashSystemDetailAjax", searchParam);
            SearchResult<DashAppDetailsQueryDto> searchResult = beDashboardAjaxService.getDashAllActionResult(searchParam);
            //set other data
            searchResult = beDashboardAjaxService.setDashSysDetailsDropOtherData(searchResult);

            map.put("result", "Success");
            map.put(AJAXRESULT, searchResult);
        } else {
            map.put("result", "Fail");
        }
        return map;
    }

    private SearchParam dashSysDetailDropFilter(SearchParam searchParam, String groupId, List<String> serviceList, List<String> appTypeList,
                                                SearchParam searchParamGroup, String dashFilterAppNo) {
        //filter appGroup NO.
        searchParam.addFilter("groupId", groupId, true);
        if(IaisCommonUtils.isNotEmpty(serviceList)) {
            String serviceStr = SqlHelper.constructInCondition("viewApp.SVC_CODE", serviceList.size());
            searchParam.addParam("svc_codes", serviceStr);
            for(int i = 0; i < serviceList.size(); i++){
                searchParam.addFilter("viewApp.SVC_CODE" + i, serviceList.get(i));
            }
        }
        if(IaisCommonUtils.isNotEmpty(appTypeList)) {
            String appTypeStr = SqlHelper.constructInCondition("viewApp.APP_TYPE", appTypeList.size());
            searchParam.addParam("application_types", appTypeStr);
            for(int i = 0; i < appTypeList.size(); i++){
                searchParam.addFilter("viewApp.APP_TYPE" + i, appTypeList.get(i));
            }
        }
        if(!StringUtil.isEmpty(dashFilterAppNo)){
            searchParam.addFilter("applicationNo", dashFilterAppNo, true);
        }
        Map<String, Object> filters = searchParamGroup.getFilters();
        if(filters != null) {
            String stageId = (String) filters.get("stage_id");
            if(!StringUtil.isEmpty(stageId)) {
                searchParam.addFilter("stage_id", stageId, true);
            }
        }
        return searchParam;
    }

    private Map<String, Object> setDashWaitApproveUrl(Map<String, Object> map, HttpServletRequest request, LoginContext loginContext) {
        String userId = "";
        String roleId = "";
        if(loginContext != null && !StringUtil.isEmpty(loginContext.getUserId()) && !StringUtil.isEmpty(loginContext.getCurRoleId())) {
            log.info(StringUtil.changeForLog("Dashboard-Kpi-Pool-user-Id =====" + loginContext.getUserId()));
            log.info(StringUtil.changeForLog("Dashboard=Kpi=Pool=Role=Id =====" + loginContext.getCurRoleId()));
            userId = loginContext.getUserId();
            roleId = loginContext.getCurRoleId();
        }
        if(map != null) {
            SearchResult<DashWaitApproveAjaxQueryDto> ajaxResult = (SearchResult<DashWaitApproveAjaxQueryDto>) map.get(AJAXRESULT);
            if(ajaxResult != null) {
                List<DashWaitApproveAjaxQueryDto> dashWaitApproveAjaxQueryDtos = ajaxResult.getRows();
                if(!IaisCommonUtils.isEmpty(dashWaitApproveAjaxQueryDtos)) {
                    for(DashWaitApproveAjaxQueryDto dashWaitApproveAjaxQueryDto : dashWaitApproveAjaxQueryDtos) {
                        //task is current work
                        if(!StringUtil.isEmpty(dashWaitApproveAjaxQueryDto.getTaskId())) {
                            TaskDto taskDto = taskService.getTaskById(dashWaitApproveAjaxQueryDto.getTaskId());
                            //set kpi color
                            String color = beDashboardAjaxService.getKpiColorByTask(taskDto);
                            dashWaitApproveAjaxQueryDto.setKpiColor(color);
                            if(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN.equals(roleId)) {
                                dashWaitApproveAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                            } else if (userId.equals(taskDto.getUserId()) && roleId.equals(taskDto.getRoleId())) {
                                //set mask task Id
                                String maskId = MaskUtil.maskValue(TASKID, dashWaitApproveAjaxQueryDto.getTaskId());
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

    private Map<String, Object> setDashRenewPoolUrl(Map<String, Object> map, HttpServletRequest request, LoginContext loginContext) {
        String userId = "";
        String roleId = "";
        if(loginContext != null && !StringUtil.isEmpty(loginContext.getUserId()) && !StringUtil.isEmpty(loginContext.getCurRoleId())) {
            log.info(StringUtil.changeForLog("Dashboard Kpi Pool user Id =====" + loginContext.getUserId()));
            log.info(StringUtil.changeForLog("Dashboard Kpi Pool Role Id =====" + loginContext.getCurRoleId()));
            userId = loginContext.getUserId();
            roleId = loginContext.getCurRoleId();
        }
        if(map != null) {
            SearchResult<DashRenewAjaxQueryDto> ajaxResult = (SearchResult<DashRenewAjaxQueryDto>) map.get(AJAXRESULT);
            if(ajaxResult != null) {
                List<DashRenewAjaxQueryDto> dashRenewAjaxQueryDtos = ajaxResult.getRows();
                if(!IaisCommonUtils.isEmpty(dashRenewAjaxQueryDtos)) {
                    for(DashRenewAjaxQueryDto dashRenewAjaxQueryDto : dashRenewAjaxQueryDtos) {
                        //task is current work
                        if(!StringUtil.isEmpty(dashRenewAjaxQueryDto.getTaskId())) {
                            TaskDto taskDto = taskService.getTaskById(dashRenewAjaxQueryDto.getTaskId());
                            //set kpi color
                            String color = beDashboardAjaxService.getKpiColorByTask(taskDto);
                            dashRenewAjaxQueryDto.setKpiColor(color);
                            if(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN.equals(roleId)) {
                                dashRenewAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                            } else if (userId.equals(taskDto.getUserId()) && roleId.equals(taskDto.getRoleId())) {
                                //set mask task Id
                                String maskId = MaskUtil.maskValue(TASKID, dashRenewAjaxQueryDto.getTaskId());
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

    private Map<String, Object> setWorkTeamPoolUrl(Map<String, Object> map) {
        if(map != null) {
            SearchResult<DashWorkTeamAjaxQueryDto> ajaxResult = (SearchResult<DashWorkTeamAjaxQueryDto>) map.get(AJAXRESULT);
            if(ajaxResult != null) {
                List<DashWorkTeamAjaxQueryDto> dashWorkTeamAjaxQueryDtos = ajaxResult.getRows();
                if(!IaisCommonUtils.isEmpty(dashWorkTeamAjaxQueryDtos)) {
                    for(DashWorkTeamAjaxQueryDto dashWorkTeamAjaxQueryDto : dashWorkTeamAjaxQueryDtos) {
                        if(!StringUtil.isEmpty(dashWorkTeamAjaxQueryDto.getTaskId())) {
                            TaskDto taskDto = taskService.getTaskById(dashWorkTeamAjaxQueryDto.getTaskId());
                            //set kpi color
                            String color = beDashboardAjaxService.getKpiColorByTask(taskDto);
                            dashWorkTeamAjaxQueryDto.setKpiColor(color);
                        }
                        dashWorkTeamAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                    }
                }
            }
        }
        return map;
    }

    private Map<String, Object> setDashAssignMeUrl(Map<String, Object> map, HttpServletRequest request, LoginContext loginContext) {
        if(map != null) {
            SearchResult<DashAssignMeAjaxQueryDto> ajaxResult = (SearchResult<DashAssignMeAjaxQueryDto>) map.get(AJAXRESULT);
            if(ajaxResult != null) {
                List<DashAssignMeAjaxQueryDto> dashAssignMeAjaxQueryDtos = ajaxResult.getRows();
                if(!IaisCommonUtils.isEmpty(dashAssignMeAjaxQueryDtos)) {
                    for(DashAssignMeAjaxQueryDto dashAssignMeAjaxQueryDto : dashAssignMeAjaxQueryDtos) {
                        //task is current work
                        if(!StringUtil.isEmpty(dashAssignMeAjaxQueryDto.getTaskId())) {
                            TaskDto taskDto = taskService.getTaskById(dashAssignMeAjaxQueryDto.getTaskId());
                            //set kpi color
                            String color = beDashboardAjaxService.getKpiColorByTask(taskDto);
                            dashAssignMeAjaxQueryDto.setKpiColor(color);
                            //set mask task Id
                            String maskId = MaskUtil.maskValue(TASKID, dashAssignMeAjaxQueryDto.getTaskId());
                            dashAssignMeAjaxQueryDto.setTaskMaskId(maskId);
                            dashAssignMeAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_PROCESS);
                            String dashTaskUrl = generateProcessUrl(taskDto.getProcessUrl(), request, maskId);
                            dashAssignMeAjaxQueryDto.setDashTaskUrl(dashTaskUrl);
                        } else {
                            dashAssignMeAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                            log.info("LoginContext -> {}",StringUtil.isEmpty(loginContext));
                        }
                    }
                }
            }
        }
        return map;
    }

    private Map<String, Object> setDashKpiPoolUrl(Map<String, Object> map, HttpServletRequest request, LoginContext loginContext) {
        String userId = "";
        String roleId = "";
        if(loginContext != null && !StringUtil.isEmpty(loginContext.getUserId()) && !StringUtil.isEmpty(loginContext.getCurRoleId())) {
            log.info(StringUtil.changeForLog("Dashboard Kpi Pool user Id =====" + loginContext.getUserId()));
            log.info(StringUtil.changeForLog("Dashboard Kpi Pool Role Id =====" + loginContext.getCurRoleId()));
            userId = loginContext.getUserId();
            roleId = loginContext.getCurRoleId();
        }
        if(map != null) {
            SearchResult<DashKpiPoolAjaxQuery> ajaxResult = (SearchResult<DashKpiPoolAjaxQuery>) map.get(AJAXRESULT);
            if(ajaxResult != null) {
                List<DashKpiPoolAjaxQuery> dashKpiPoolAjaxQueries = ajaxResult.getRows();
                if(!IaisCommonUtils.isEmpty(dashKpiPoolAjaxQueries)) {
                    for(DashKpiPoolAjaxQuery dashKpiPoolAjaxQuery : dashKpiPoolAjaxQueries) {
                        //task is current work
                        if(!StringUtil.isEmpty(dashKpiPoolAjaxQuery.getTaskId())) {
                            TaskDto taskDto = taskService.getTaskById(dashKpiPoolAjaxQuery.getTaskId());
                            //set kpi color
                            String color = beDashboardAjaxService.getKpiColorByTask(taskDto);
                            dashKpiPoolAjaxQuery.setKpiColor(color);
                            if(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN.equals(roleId)) {
                                dashKpiPoolAjaxQuery.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                            } else if (userId.equals(taskDto.getUserId()) && roleId.equals(taskDto.getRoleId())) {
                                //set mask task Id
                                String maskId = MaskUtil.maskValue(TASKID, dashKpiPoolAjaxQuery.getTaskId());
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

    private Map<String, Object> setDashComPoolUrl(Map<String, Object> map, LoginContext loginContext) {
        String roleId = "";
        if(loginContext != null && !StringUtil.isEmpty(loginContext.getCurRoleId())) {
            log.info(StringUtil.changeForLog("Dashboard Common Pool Current Role =====" + loginContext.getCurRoleId()));
            roleId = loginContext.getCurRoleId();
        }
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
                        TaskDto taskDto = taskService.getTaskById(dashComPoolAjaxQueryDto.getTaskId());
                        //set kpi color
                        String color = beDashboardAjaxService.getKpiColorByTask(taskDto);
                        dashComPoolAjaxQueryDto.setKpiColor(color);
                        if(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN.equals(roleId)) {
                            dashComPoolAjaxQueryDto.setCanDoTask(BeDashboardConstant.TASK_SHOW);
                        } else if (workGroupIds.contains(taskDto.getWkGrpId()) && roleId.equals(taskDto.getRoleId())) {
                            //set mask task Id
                            String maskId = MaskUtil.maskValue(TASKID, dashComPoolAjaxQueryDto.getTaskId());
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

    @PostMapping(value = "changeTaskStatus.do")
    public @ResponseBody
    Map<String, Object> changeTaskStatus(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>(1);
        String taskId = ParamUtil.getMaskedString(request, TASKID);
        String res = inspectionAssignTaskService.taskRead(taskId);
        map.put("res",res);
        return map;
    }

    @PostMapping(value = "dashRole.switch")
    public @ResponseBody
    Map<String, Object> dashChangeRoleSwitch(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>(1);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        PoolRoleCheckDto poolRoleCheckDto = (PoolRoleCheckDto)ParamUtil.getSessionAttr(request, "dashRoleCheckDto");
        String roleSelectVal = request.getParameter("roleSelectVal");
        map.put(DASH_ROLE_SWITCH_FLAG, AppConsts.TRUE);
        if(loginContext != null && poolRoleCheckDto != null) {
            String curRoleId = loginContext.getCurRoleId();
            if(!StringUtil.isEmpty(roleSelectVal)) {
                Map<String, String> roleMap = poolRoleCheckDto.getRoleMap();
                String roleId = getCheckRoleIdByMap(roleSelectVal, roleMap);
                if(!StringUtil.isEmpty(curRoleId)) {
                    if(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN.equals(curRoleId)) {
                        if (curRoleId.equals(roleId)) {
                            map.put(DASH_ROLE_SWITCH_FLAG, AppConsts.TRUE);
                        } else {
                            map.put(DASH_ROLE_SWITCH_FLAG, AppConsts.FAIL);
                        }
                    } else {
                        map.put(DASH_ROLE_SWITCH_FLAG, AppConsts.TRUE);
                    }
                    loginContext.setCurRoleId(roleId);
                    poolRoleCheckDto.setCheckCurRole(roleSelectVal);
                }
            }
        }
        ParamUtil.setSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
        ParamUtil.setSessionAttr(request, "dashRoleCheckDto", poolRoleCheckDto);
        return map;
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

    @PostMapping(value = "applicationView.show")
    public @ResponseBody
    Map<String, Object> dashApplicationView(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>(1);
        String appPremCorrId = request.getParameter("appPremCorrId");
        try {
            ApplicationViewDto applicationViewDto = applicationViewMainService.getApplicationViewDtoByCorrId(appPremCorrId);
            if(applicationViewDto != null) {
                ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
                if(applicationDto != null && ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationDto.getApplicationType())) {
                    setCessation(request, applicationViewDto);
                }
            }
            map.put("dashAppShowFlag", AppConsts.SUCCESS);

            ParamUtil.setSessionAttr(request, "applicationViewDto", applicationViewDto);
        } catch (Exception e) {
            map.put("dashAppShowFlag", AppConsts.FAIL);
            log.error(e.getMessage(), e);
        }
        return map;
    }

    @PostMapping(value = "aoApprove.do")
    public @ResponseBody
    Map<String, Object> aoApproveCheck(HttpServletRequest request) {
        String applicationString =  ParamUtil.getString(request, "applications");
        String[] applications = applicationString.split(",");
        int approveCheck = 1;
        String noApprove = MessageUtil.getMessageDesc("GENERAL_ERR0044");
        for (String item:applications) {
            ApplicationDto applicationDto = inspectionTaskMainClient.getApplicationDtoByAppNo(item).getEntity();
            if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01.equals(applicationDto.getStatus()) || ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02.equals(applicationDto.getStatus())){
                HcsaSvcRoutingStageDto canApproveStageDto = getCanApproveStageDto(applicationDto.getApplicationType(), applicationDto.getStatus(), applicationDto.getRoutingServiceId());
                boolean canApprove = checkCanApproveStage(canApproveStageDto);
                if(!canApprove){
                    approveCheck = 0;
                }
            }else{
                approveCheck = 0;
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("res",approveCheck);
        map.put("noApprove",noApprove);
        return map;
    }

    private HcsaSvcRoutingStageDto getCanApproveStageDto(String appType, String appStatus, String serviceId){
        if(StringUtil.isEmpty(appType) || StringUtil.isEmpty(appStatus) || StringUtil.isEmpty(serviceId)){
            return null;
        }
        String stageId = HcsaConsts.ROUTING_STAGE_AO1;
        if(appStatus.equals(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02)){
            stageId = HcsaConsts.ROUTING_STAGE_AO2;
        }
        HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto = new HcsaSvcRoutingStageDto();
        hcsaSvcRoutingStageDto.setStageId(stageId);
        hcsaSvcRoutingStageDto.setServiceId(serviceId);
        hcsaSvcRoutingStageDto.setAppType(appType);
        return hcsaConfigClient.getHcsaSvcRoutingStageDto(hcsaSvcRoutingStageDto).getEntity();
    }

    private boolean checkCanApproveStage(HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto){
        if(hcsaSvcRoutingStageDto == null){
            return false;
        }
        boolean flag = false;
        String canApprove = hcsaSvcRoutingStageDto.getCanApprove();
        if("1".equals(canApprove)){
            flag = true;
        }
        return flag;
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

    private void setCessation(HttpServletRequest request, ApplicationViewDto applicationViewDto) {
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String applicationType = applicationDto.getApplicationType();
        if (ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)) {
            AppCessLicDto appCessLicDto = new AppCessLicDto();
            String originLicenceId = applicationDto.getOriginLicenceId();
            LicenceDto licenceDto = licenceClient.getLicDtoById(originLicenceId).getEntity();
            String svcName = licenceDto.getSvcName();
            String licenceNo = licenceDto.getLicenceNo();
            appCessLicDto.setLicenceNo(licenceNo);
            appCessLicDto.setSvcName(svcName);
            AppPremiseMiscDto appPremiseMiscDto = cessationMainClient.getAppPremiseMiscDtoByAppId(applicationDto.getId()).getEntity();
            AppCessMiscDto appCessMiscDto = MiscUtil.transferEntityDto(appPremiseMiscDto, AppCessMiscDto.class);
            AppGrpPremisesDto appGrpPremisesDto = hcsaAppClient.getActivePremisesByAppNo(applicationDto.getApplicationNo()).getEntity();
            String hciAddress = IaisCommonUtils.getAddress(appGrpPremisesDto);
            AppCessHciDto appCessHciDto = new AppCessHciDto();
            String hciName = appGrpPremisesDto.getHciName();
            String hciCode = appGrpPremisesDto.getHciCode();
            appCessHciDto.setHciCode(hciCode);
            appCessHciDto.setHciName(hciName);
            appCessHciDto.setPremiseId(appGrpPremisesDto.getId());
            appCessHciDto.setHciAddress(hciAddress);
            if (appCessMiscDto != null) {
                Date effectiveDate = appCessMiscDto.getEffectiveDate();
                String reason = appCessMiscDto.getReason();
                String otherReason = appCessMiscDto.getOtherReason();
                String patTransType = appCessMiscDto.getPatTransType();
                String patTransTo = appCessMiscDto.getPatTransTo();
                String mobileNo = appCessMiscDto.getMobileNo();
                String emailAddress = appCessMiscDto.getEmailAddress();
                appCessHciDto.setPatientSelect(patTransType);
                appCessHciDto.setReason(reason);
                appCessHciDto.setOtherReason(otherReason);
                appCessHciDto.setEffectiveDate(effectiveDate);
                appCessHciDto.setTransferDetail(appCessMiscDto.getTransferDetail());
                appCessHciDto.setTransferredWhere(appCessMiscDto.getTransferredWhere());
                Map<String, String> fieldMap = IaisCommonUtils.genNewHashMap();
                MiscUtil.transferEntityDto(appCessMiscDto, AppCessHciDto.class, fieldMap, appCessHciDto);
                Boolean patNeedTrans = appCessMiscDto.getPatNeedTrans();
                if (Boolean.TRUE.equals(patNeedTrans)) {
                    appCessHciDto.setPatientSelect(patTransType);
                    if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI.equals(patTransType) && !StringUtil.isEmpty(patTransTo)) {
                        appCessHciDto.setPatHciName(patTransTo);
                        appCessHciDto.setPatNeedTrans(Boolean.TRUE);
                        PremisesDto premisesDto = getPremiseByHciCodeName(patTransTo);
                        String hciAddressPat = premisesDto.getHciAddress();
                        String hciNamePat = premisesDto.getHciName();
                        String hciCodePat = premisesDto.getHciCode();
                        appCessHciDto.setHciNamePat(hciNamePat);
                        appCessHciDto.setHciCodePat(hciCodePat);
                        appCessHciDto.setHciAddressPat(hciAddressPat);
                    }
                    if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO.equals(patTransType) && !StringUtil.isEmpty(patTransTo)) {
                        appCessHciDto.setPatRegNo(patTransTo);
                        appCessHciDto.setPatNeedTrans(Boolean.TRUE);
                    }
                    if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_OTHER.equals(patTransType) && !StringUtil.isEmpty(patTransTo)) {
                        appCessHciDto.setPatOthers(patTransTo);
                        appCessHciDto.setPatNeedTrans(Boolean.TRUE);
                        appCessHciDto.setMobileNo(mobileNo);
                        appCessHciDto.setEmailAddress(emailAddress);
                    }
                } else {
                    String remarks = appCessMiscDto.getPatNoReason();
                    appCessHciDto.setPatNoRemarks(remarks);
                    appCessHciDto.setPatNoConfirm("no");
                    appCessHciDto.setPatNeedTrans(Boolean.FALSE);
                }
                List<AppCessHciDto> appCessHciDtos = IaisCommonUtils.genNewArrayList();
                appCessHciDtos.add(appCessHciDto);
                appCessLicDto.setAppCessHciDtos(appCessHciDtos);
                //spec
                String applicationNo = applicationDto.getApplicationNo();
                List<ApplicationDto> specApps = cessationMainClient.getAppByBaseAppNo(applicationNo).getEntity();
                List<AppSpecifiedLicDto> appSpecifiedLicDtos = IaisCommonUtils.genNewArrayList();
                if (!IaisCommonUtils.isEmpty(specApps)) {
                    for(ApplicationDto specApp : specApps ){
                        String specId = specApp.getOriginLicenceId();
                        LicenceDto specLicenceDto = licenceClient.getLicDtoById(specId).getEntity();
                        if (specLicenceDto != null) {
                            AppSpecifiedLicDto appSpecifiedLicDto = new AppSpecifiedLicDto();
                            LicenceDto baseLic = licenceClient.getLicDtoById(originLicenceId).getEntity();
                            String specLicenceNo = specLicenceDto.getLicenceNo();
                            String licenceDtoId = specLicenceDto.getId();
                            String specSvcName = specLicenceDto.getSvcName();
                            appSpecifiedLicDto.setBaseLicNo(baseLic.getLicenceNo());
                            appSpecifiedLicDto.setBaseSvcName(baseLic.getSvcName());
                            appSpecifiedLicDto.setSpecLicNo(specLicenceNo);
                            appSpecifiedLicDto.setSpecSvcName(specSvcName);
                            appSpecifiedLicDto.setSpecLicId(licenceDtoId);
                            appSpecifiedLicDtos.add(appSpecifiedLicDto);
                        }
                    }
                    ParamUtil.setSessionAttr(request, "specLicInfo", (Serializable) appSpecifiedLicDtos);
                }
                ParamUtil.setSessionAttr(request, "confirmDto", appCessLicDto);
                ParamUtil.setSessionAttr(request, "reasonOption", (Serializable) getReasonOption());
                ParamUtil.setSessionAttr(request, "patientsOption", (Serializable) getPatientsOption());
            }
        }
    }

    public PremisesDto getPremiseByHciCodeName(String hciNameCode) {
        PremisesDto premisesDto = licenceClient.getPremiseDtoByHciCodeOrName(hciNameCode).getEntity();
        if (premisesDto != null) {
            premisesDto.setHciAddress(IaisCommonUtils.getAddress(premisesDto));
        }
        return premisesDto;
    }

    private List<SelectOption> getReasonOption() {
        return MasterCodeUtil.retrieveOptionsByCodes(reasonArr);
    }

    private List<SelectOption> getPatientsOption() {
        return MasterCodeUtil.retrieveOptionsByCodes(patientsArr);
    }
}