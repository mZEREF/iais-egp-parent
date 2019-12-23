package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 12/16/2019 4:03 PM
 */
@Delegator("inspectionReassignTask")
@Slf4j
public class InspecReassignTaskDelegator {

    @Autowired
    private InspectionService inspectionService;

    public InspecReassignTaskDelegator(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    /**
     * StartStep: inspectionReassignTask
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchStart(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchStart start ...."));
        AuditTrailHelper.auditFunction("Inspection Reassign Task", "Sup Assign Task");
    }

    /**
     * StartStep: inspectionSupSearchInit
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchInit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchInit start ...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "reassignReason", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", null);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", null);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", null);
        ParamUtil.setSessionAttr(bpc.request, "workGroupIds", null);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", null);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectorOption", null);
    }

    /**
     * StartStep: inspectionSupSearchPre
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchPre(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchPre start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        SearchResult<InspectionSubPoolQueryDto> searchResult = (SearchResult) ParamUtil.getSessionAttr(bpc.request, "supTaskSearchResult");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        //get groupId by login id
        List<String> workGroupIds = inspectionService.getWorkGroupIdsByLogin(loginContext);
        //select de option
        List<SelectOption> appTypeOption = inspectionService.getAppTypeOption();
        List<SelectOption> appStatusOption = inspectionService.getAppStatusOption();
        //get Inspector Option
        List<SelectOption> inspectorOption = inspectionService.getInspectorOptionByLogin(loginContext, workGroupIds);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", searchResult);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", (Serializable) appTypeOption);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
        ParamUtil.setSessionAttr(bpc.request, "inspectorOption", (Serializable) inspectorOption);
        ParamUtil.setSessionAttr(bpc.request, "workGroupIds", (Serializable) workGroupIds);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc) {
        return getSearchParam(bpc, false);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc, boolean isNew) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "supTaskSearchParam");
        if (searchParam == null || isNew) {
            searchParam = new SearchParam(InspectionSubPoolQueryDto.class.getName());
            searchParam.setPageSize(10);
            searchParam.setPageNo(1);
            searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
        }
        return searchParam;
    }

    /**
     * StartStep: inspectionSupSearchStartStep1
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchStartStep1(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchStartStep1 start ...."));

    }

    /**
     * StartStep: inspectionSupSearchDoSearch
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchDoSearch(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchDoSearch start ...."));
        SearchParam searchParam = getSearchParam(bpc);

        List<String> workGroupIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "workGroupIds");
        String application_no = ParamUtil.getRequestString(bpc.request, "application_no");
        String application_type = ParamUtil.getRequestString(bpc.request, "application_type");
        String application_status = ParamUtil.getRequestString(bpc.request, "application_status");
        String hci_code = ParamUtil.getRequestString(bpc.request, "hci_code");
        String hci_name = ParamUtil.getRequestString(bpc.request, "hci_name");
        String hci_address = ParamUtil.getRequestString(bpc.request, "hci_address");
        String inspectorValue = ParamUtil.getMaskedString(bpc.request, "inspector_name");

        List<TaskDto> ReassignPools = getReassignPoolByGroupWordId(workGroupIds);

        String[] applicationNo_list = inspectionService.getApplicationNoListByPool(ReassignPools);
        if (applicationNo_list == null || applicationNo_list.length == 0) {
            applicationNo_list = new String[]{SystemParameterConstants.PARAM_FALSE};
        }
        String applicationStr = SqlHelper.constructInCondition("T1.APPLICATION_NO", applicationNo_list.length);
        searchParam.addParam("applicationNo_list", applicationStr);
        for (int i = 0; i < applicationNo_list.length; i++) {
            searchParam.addFilter("T1.APPLICATION_NO" + i, applicationNo_list[i]);
        }

        if (!StringUtil.isEmpty(application_no)) {
            searchParam.addFilter("application_no", application_no, true);
        }
        String[] appNoStrs;
        if (!(StringUtil.isEmpty(inspectorValue))) {
            appNoStrs = inspectorValue.split(",");
            String appNoStr = SqlHelper.constructInCondition("T5.APPLICATION_NO", appNoStrs.length);
            searchParam.addParam("appNo_list", appNoStr);
            for (int i = 0; i < appNoStrs.length; i++) {
                searchParam.addFilter("T5.APPLICATION_NO" + i, appNoStrs[i]);
            }
        }
        if (!StringUtil.isEmpty(application_type)) {
            searchParam.addFilter("application_type", application_type, true);
        }
        if (!StringUtil.isEmpty(application_status)) {
            searchParam.addFilter("application_status", application_status, true);
        }
        if (!StringUtil.isEmpty(hci_code)) {
            searchParam.addFilter("hci_code", hci_code, true);
        }
        if (!StringUtil.isEmpty(hci_name)) {
            searchParam.addFilter("hci_name", hci_name, true);
        }
        if (!StringUtil.isEmpty(hci_address)) {
            searchParam.addFilter("blk_no", hci_address, true);
            searchParam.addFilter("floor_no", hci_address, true);
            searchParam.addFilter("unit_no", hci_address, true);
            searchParam.addFilter("street_name", hci_address, true);
            searchParam.addFilter("building_name", hci_address, true);
            searchParam.addFilter("postal_code", hci_address, true);
        }
        ParamUtil.setSessionAttr(bpc.request, "ReassignPools", (Serializable) ReassignPools);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "inspectorValue", inspectorValue);
    }

    private List<TaskDto> getReassignPoolByGroupWordId(List<String> workGroupIds) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        if (workGroupIds == null || workGroupIds.size() <= 0) {
            return null;
        }
        for (String workGrpId : workGroupIds) {
            List<TaskDto> reassignPoolByGroupWordId = inspectionService.getReassignPoolByGroupWordId(workGrpId);
            if (reassignPoolByGroupWordId != null && !reassignPoolByGroupWordId.isEmpty()) {
                for (TaskDto tDto : reassignPoolByGroupWordId) {
                    taskDtoList.add(tDto);
                }
            }

        }
        return taskDtoList;
    }

    /**
     * StartStep: inspectionSupSearchSort
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchSort(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchSort start ...."));
    }

    /**
     * StartStep: inspectionSupSearchPage
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchPage(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchPage start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam, bpc.request);
    }

    /**
     * StartStep: inspectionSupSearchQuery1
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchQuery1(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchQuery1 start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        List<TaskDto> ReassignPools = (List<TaskDto>) ParamUtil.getSessionAttr(bpc.request, "ReassignPools");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        QueryHelp.setMainSql("inspectionQuery", "assignInspectorSupper", searchParam);
        SearchResult<InspectionSubPoolQueryDto> searchResult = inspectionService.getSupPoolByParam(searchParam);
        SearchResult<InspectionTaskPoolListDto> searchResult2 = inspectionService.getOtherDataForSr(searchResult, ReassignPools, loginContext);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", searchResult2);
    }

    /**
     * StartStep: inspectionSupSearchAssign
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchAssign(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchAssign start ...."));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        SearchResult<InspectionTaskPoolListDto> searchResult = (SearchResult<InspectionTaskPoolListDto>) ParamUtil.getSessionAttr(bpc.request, "supTaskSearchResult");
        String taskId = ParamUtil.getMaskedString(bpc.request, "taskId");
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto) ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        if (!(StringUtil.isEmpty(taskId))) {
            inspectionTaskPoolListDto = new InspectionTaskPoolListDto();
            for (InspectionTaskPoolListDto iDto : searchResult.getRows()) {
                if (taskId.equals(iDto.getTaskId())) {
                    inspectionTaskPoolListDto = iDto;
                }
            }
            //get inspector Option
            inspectionTaskPoolListDto = inspectionService.reassignInspectorOption(inspectionTaskPoolListDto, taskId);
            List<SelectOption> inspectorOption = inspectionTaskPoolListDto.getInspectorOption();
            ParamUtil.setSessionAttr(bpc.request, "inspectorOption", (Serializable) inspectorOption);
        }

        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", inspectionTaskPoolListDto);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", searchResult);
    }

    /**
     * StartStep: inspectionSupSearchValidate
     * @param bpc
     * @throws`
     */
    public void inspectionSupSearchValidate(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchValidate start ...."));
        InspectionTaskPoolListDto inspectionTaskPoolListDto = getValueFromPage(bpc);
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        String reassignReason = ParamUtil.getString(bpc.request, "reassignReason");
        if (!(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue))) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(inspectionTaskPoolListDto, "create");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.TRUE);
            }
        } else {
            ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", inspectionTaskPoolListDto);
        ParamUtil.setSessionAttr(bpc.request, "reassignReason", reassignReason);
    }

    public InspectionTaskPoolListDto getValueFromPage(BaseProcessClass bpc) {
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto) ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        String[] nameValue = ParamUtil.getStrings(bpc.request, "inspectorCheck");
        if (nameValue == null || nameValue.length < 0) {
            inspectionTaskPoolListDto.setInspectorCheck(null);
        } else {
            List<SelectOption> inspectorCheckList = inspectionService.getCheckInspector(nameValue, inspectionTaskPoolListDto);
            inspectionTaskPoolListDto.setInspectorCheck(inspectorCheckList);
        }
        return inspectionTaskPoolListDto;
    }

    /**
     * StartStep: inspectionSupSearchQuery2
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchQuery2(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchQuery2 start ...."));
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto) ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", inspectionTaskPoolListDto);
    }

    /**
     * StartStep: inspectionSupSearchConfirm
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchConfirm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchConfirm start ...."));
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto) ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", inspectionTaskPoolListDto);
    }

    /**
     * StartStep: InspectionInboxSearchQuery
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchSuccess(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchSuccess start ...."));
        String reassignReason = (String) ParamUtil.getSessionAttr(bpc.request, "reassignReason");
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto) ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        List<TaskDto> ReassignPools = (List<TaskDto>) ParamUtil.getSessionAttr(bpc.request, "ReassignPools");
        //String internalRemarks = ParamUtil.getString(bpc.request, "internalRemarks");
        inspectionService.routingTaskByPool(inspectionTaskPoolListDto, ReassignPools, reassignReason);
        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", inspectionTaskPoolListDto);
    }













//    private void routingTask(BaseProcessClass bpc,String stageId,String appStatus,String roleId ) throws FeignException {
//
//        //get the user for this applicationNo
//        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
//        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
//        BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
//        BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();
//
//        //complated this task and create the history
//        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
//        taskDto =  completedTask(taskDto);
//        broadcastOrganizationDto.setComplateTask(taskDto);
//        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
//        String processDecision = ParamUtil.getString(bpc.request,"nextStage");
//        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),
//                applicationDto.getStatus(),taskDto.getTaskKey(), taskDto.getWkGrpId(),internalRemarks,processDecision,taskDto.getRoleId());
//        broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
//        //update application status
//        String oldStatus = applicationDto.getStatus();
//        applicationDto.setStatus(appStatus);
//        broadcastApplicationDto.setApplicationDto(applicationDto);
//        // send the task
//        if(!StringUtil.isEmpty(stageId)){
//            //For the BROADCAST Rely
//            if(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(oldStatus)){
//                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto1 = appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryForCurrentStage(
//                        applicationDto.getId(),stageId
//                );
//                log.debug(StringUtil.changeForLog("The appId is-->;"+ applicationDto.getId()));
//                log.debug(StringUtil.changeForLog("The stageId is-->;"+ stageId));
//                if(appPremisesRoutingHistoryDto1 != null){
//                    TaskDto newTaskDto = TaskUtil.getTaskDto(stageId, TaskConsts.TASK_TYPE_MAIN_FLOW,
//                            applicationDto.getApplicationNo(),appPremisesRoutingHistoryDto1.getWrkGrpId(),
//                            appPremisesRoutingHistoryDto1.getActionby(),new Date(),0,TaskConsts.TASK_PROCESS_URL_MAIN_FLOW,roleId,
//                            IaisEGPHelper.getCurrentAuditTrailDto());
//                    broadcastOrganizationDto.setCreateTask(newTaskDto);
//                    //delete workgroup
//                    BroadcastOrganizationDto broadcastOrganizationDto1 = broadcastService.getBroadcastOrganizationDto(
//                            applicationDto.getApplicationNo(),AppConsts.DOMAIN_TEMPORARY);
//
//                    WorkingGroupDto workingGroupDto = broadcastOrganizationDto1.getWorkingGroupDto();
//                    workingGroupDto = changeStatusWrokGroup(workingGroupDto,AppConsts.COMMON_STATUS_DELETED);
//                    broadcastOrganizationDto.setWorkingGroupDto(workingGroupDto);
//                    List<UserGroupCorrelationDto> userGroupCorrelationDtos = broadcastOrganizationDto1.getUserGroupCorrelationDtoList();
//                    userGroupCorrelationDtos = changeStatusUserGroupCorrelationDtos(userGroupCorrelationDtos,AppConsts.COMMON_STATUS_DELETED);
//                    broadcastOrganizationDto.setUserGroupCorrelationDtoList(userGroupCorrelationDtos);
//                }else{
//                    throw new IaisRuntimeException("This getAppPremisesCorrelationId can not get the broadcast -- >:"+applicationViewDto.getAppPremisesCorrelationId());
//                }
//            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(appStatus)){
//                List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
//                boolean isAllSubmit = applicationService.isOtherApplicaitonSubmit(applicationDtoList,applicationDto.getId(),
//                        ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);
//                if(isAllSubmit){
//                    // send the task to Ao3
//                    TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(applicationDtoList,
//                            HcsaConsts.ROUTING_STAGE_AO3,roleId,IaisEGPHelper.getCurrentAuditTrailDto());
//                    List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
//                    List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
//                    broadcastOrganizationDto.setOneSubmitTaskList(taskDtos);
//                    broadcastApplicationDto.setOneSubmitTaskHistoryList(appPremisesRoutingHistoryDtos);
//                }
//            }else{
//                TaskDto newTaskDto = taskService.getRoutingTask(applicationDto,stageId,roleId);
//                broadcastOrganizationDto.setCreateTask(newTaskDto);
//            }
//            //add history for next stage start
//            if(!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(appStatus)){
//                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(),stageId,
//                        taskDto.getWkGrpId(),null,null,roleId);
//                broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
//            }
//        }
//        //save the broadcast
//        broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
//        broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
//        broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto);
//        broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto);
//    }

//    private TaskDto completedTask(TaskDto taskDto) {
//        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
//        taskDto.setSlaDateCompleted(new Date());
//        taskDto.setSlaRemainInDays(remainDays(taskDto));
//        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
//        return taskService.updateTask(taskDto);
//    }







}
