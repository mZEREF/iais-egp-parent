package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.BeDashboardConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashComPoolAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewMainService;
import com.ecquaria.cloud.moh.iais.service.BeDashboardAjaxService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskMainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class MohHcsaBeDashboardAjax {

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
    private TaskService taskService;

    @RequestMapping(value = "appGroup.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> appGroup(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        String actionValue = ParamUtil.getRequestString(request, "switchAction");
        String groupNo = request.getParameter("groupNo");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(BeDashboardConstant.SWITCH_ACTION_COMMON.equals(actionValue)) {
            map = beDashboardAjaxService.getCommonDropdownResult(groupNo, loginContext, map, actionValue);
            //set url
            map = setDashComPoolUrl(map, request, loginContext);
        } else if(BeDashboardConstant.SWITCH_ACTION_ASSIGN_ME.equals(actionValue)) {

        } else if(BeDashboardConstant.SWITCH_ACTION_REPLY.equals(actionValue)) {

        } else if(BeDashboardConstant.SWITCH_ACTION_KPI.equals(actionValue)) {

        } else if(BeDashboardConstant.SWITCH_ACTION_RE_RENEW.equals(actionValue)) {

        } else if(BeDashboardConstant.SWITCH_ACTION_APPROVE.equals(actionValue)) {

        } else if(BeDashboardConstant.SWITCH_ACTION_WAIT.equals(actionValue)) {

        } else if(BeDashboardConstant.SWITCH_ACTION_GROUP.equals(actionValue)) {

        }
        return map;
    }

    private Map<String, Object> setDashComPoolUrl(Map<String, Object> map, HttpServletRequest request, LoginContext loginContext) {
        String roleId = "";
        if(loginContext != null && !StringUtil.isEmpty(loginContext.getCurRoleId())) {
            log.info(StringUtil.changeForLog("Dashboard Common Pool Current Role =====" + loginContext.getCurRoleId()));
            roleId = loginContext.getCurRoleId();
        }
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
                        TaskDto taskDto = taskService.getTaskById(dashComPoolAjaxQueryDto.getTaskId());
                        //set mask task Id
                        String maskId = MaskUtil.maskValue("taskId", dashComPoolAjaxQueryDto.getTaskId());
                        if (workGroupIds.contains(taskDto.getWkGrpId()) && roleId.equals(taskDto.getRoleId())) {
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

    @RequestMapping(value = "changeTaskStatus.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> changeTaskStatus(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>(1);
        String taskId = ParamUtil.getMaskedString(request, "taskId");
        String res = inspectionAssignTaskService.taskRead(taskId);
        map.put("res",res);
        return map;
    }

    @RequestMapping(value = "applicationView.show", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> dashApplicationView(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>(1);
        String appPremCorrId = request.getParameter("appPremCorrId");
        try {
            ApplicationViewDto applicationViewDto = applicationViewMainService.getApplicationViewDtoByCorrId(appPremCorrId);
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
        StringBuilder noApprove = new StringBuilder();
        for (String item:applications
        ) {
            ApplicationDto applicationDto = inspectionTaskMainClient.getApplicationDtoByAppNo(item).getEntity();
            if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01.equals(applicationDto.getStatus()) || ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02.equals(applicationDto.getStatus())){
                HcsaSvcRoutingStageDto canApproveStageDto = getCanApproveStageDto(applicationDto.getApplicationType(), applicationDto.getStatus(), applicationDto.getServiceId());
                boolean canApprove = checkCanApproveStage(canApproveStageDto);
                if(!canApprove){
                    noApprove.append(item).append(',');
                    approveCheck = 0;
                }
            }else{
                noApprove.append(item).append(',');
                approveCheck = 0;
            }
        }
        if(noApprove.length() > 0){
            noApprove.deleteCharAt(noApprove.lastIndexOf(","));
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
        if(appStatus.equals(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01)){
            stageId = HcsaConsts.ROUTING_STAGE_AO1;
        }else if(appStatus.equals(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02)){
            stageId = HcsaConsts.ROUTING_STAGE_AO2;
        }
        HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto = new HcsaSvcRoutingStageDto();
        hcsaSvcRoutingStageDto.setStageId(stageId);
        hcsaSvcRoutingStageDto.setServiceId(serviceId);
        hcsaSvcRoutingStageDto.setAppType(appType);
        HcsaSvcRoutingStageDto result = hcsaConfigClient.getHcsaSvcRoutingStageDto(hcsaSvcRoutingStageDto).getEntity();
        return result;
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
}