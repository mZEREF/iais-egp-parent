package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.SystemAssignTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.SuperPoolTaskQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.SystemAssignSearchQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SysParamUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.SystemSearchAssignPoolService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Process: MohSystemPoolAssign
 *
 * @author Shicheng
 * @date 2020/8/26 14:17
 **/
@Delegator("systemSearchAssignPool")
@Slf4j
public class SystemSearchAssignPoolDelegator {

    @Autowired
    private InspectionService inspectionService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private SystemSearchAssignPoolService systemSearchAssignPoolService;

    @Autowired
    private SystemSearchAssignPoolDelegator(InspectionService inspectionService, TaskService taskService, ApplicationViewService applicationViewService,
                                            SystemSearchAssignPoolService systemSearchAssignPoolService){
        this.inspectionService = inspectionService;
        this.taskService = taskService;
        this.applicationViewService = applicationViewService;
        this.systemSearchAssignPoolService = systemSearchAssignPoolService;
    }

    /**
     * StartStep: systemPoolAssignStart
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignStart start ...."));
        AuditTrailHelper.auditFunction("System Task Pool", "System Task Assign Pool");
    }

    /**
     * StartStep: systemPoolAssignInit
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", null);
        ParamUtil.setSessionAttr(bpc.request, "systemPool", null);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", null);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", null);
        ParamUtil.setSessionAttr(bpc.request, "systemSearchParam", null);
        ParamUtil.setSessionAttr(bpc.request, "systemSearchResult", null);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", null);
        ParamUtil.setSessionAttr(bpc.request, "systemAssignMap", null);
    }

    /**
     * StartStep: systemPoolAssignPre
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignPre start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        SearchResult<SystemAssignSearchQueryDto> searchResult = (SearchResult) ParamUtil.getSessionAttr(bpc.request, "systemSearchParam");
        //First search
        if(searchResult == null) {
            LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            //get userId
            String userId = loginContext.getUserId();
            //get stage
            GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto)ParamUtil.getSessionAttr(bpc.request, "groupRoleFieldDto");
            if(groupRoleFieldDto == null) {
                groupRoleFieldDto = systemSearchAssignPoolService.getSystemSearchStage();
            }
            List<TaskDto> systemPool = systemSearchAssignPoolService.getSystemTaskPool(userId);
            List<TaskDto> systemFilterPool = filterPoolByStage(systemPool, groupRoleFieldDto.getCurStage());
            List<String> appCorrId_list = inspectionService.getApplicationNoListByPool(systemFilterPool);
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < appCorrId_list.size(); i++) {
                sb.append(":appCorrId")
                        .append(i)
                        .append(',');
            }
            String inSql = sb.substring(0, sb.length() - 1) + ")";
            searchParam.addParam("appCorrId_list", inSql);
            for (int i = 0; i < appCorrId_list.size(); i++) {
                searchParam.addFilter("appCorrId" + i, appCorrId_list.get(i));
            }
            QueryHelp.setMainSql("inspectionQuery", "systemGroupPoolSearch",searchParam);
            searchResult = systemSearchAssignPoolService.getSystemGroupPoolByParam(searchParam);
            List<SelectOption> appTypeOption = inspectionService.getAppTypeOption();
            List<SelectOption> appStatusOption = systemSearchAssignPoolService.getAppStatusOption(groupRoleFieldDto.getCurStage());
            ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
            ParamUtil.setSessionAttr(bpc.request, "systemPool", (Serializable) systemPool);
            ParamUtil.setSessionAttr(bpc.request, "appTypeOption", (Serializable) appTypeOption);
            ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
        }
        ParamUtil.setSessionAttr(bpc.request, "systemSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "systemSearchResult", searchResult);
    }

    private List<TaskDto> filterPoolByStage(List<TaskDto> systemPool, String curStage) {
        List<TaskDto> systemFilterPool = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(systemPool)){
            for(TaskDto taskDto : systemPool){
                String stage = taskDto.getTaskKey();
                if(!StringUtil.isEmpty(stage) && stage.equals(curStage)){
                    systemFilterPool.add(taskDto);
                }
            }
        }
        return systemFilterPool;
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc,boolean isNew){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "systemSearchParam");
        int pageSize = SysParamUtil.getDefaultPageSize();
        if(searchParam == null || isNew){
            searchParam = new SearchParam(SystemAssignSearchQueryDto.class.getName());
            searchParam.setPageSize(pageSize);
            searchParam.setPageNo(1);
            searchParam.setSort("GROUP_NO", SearchParam.ASCENDING);
        }
        return searchParam;
    }

    /**
     * StartStep: systemPoolAssignStep
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignStep start ...."));
    }

    /**
     * StartStep: systemPoolAssignDoSearch
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignDoSearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignDoSearch start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        List<TaskDto> systemPool = (List<TaskDto>)ParamUtil.getSessionAttr(bpc.request, "systemPool");
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto)ParamUtil.getSessionAttr(bpc.request, "groupRoleFieldDto");
        //get search filter
        String systemAssignStage = ParamUtil.getRequestString(bpc.request, "systemAssignStage");
        String application_no = ParamUtil.getRequestString(bpc.request, "application_no");
        String application_type = ParamUtil.getRequestString(bpc.request, "application_type");
        String application_status = ParamUtil.getRequestString(bpc.request, "application_status");
        String hci_code = ParamUtil.getRequestString(bpc.request, "hci_code");
        String hci_name = ParamUtil.getRequestString(bpc.request, "hci_name");
        String hci_address = ParamUtil.getRequestString(bpc.request, "hci_address");
        //systemAssignStage is true and Set current stage
        boolean stageFlag = getStageBooleanAndSet(systemAssignStage, groupRoleFieldDto);
        if(stageFlag) {
            searchParam = getSearchParam(bpc, true);
            List<TaskDto> systemFilterPool = filterPoolByStage(systemPool, groupRoleFieldDto.getCurStage());
            List<String> appCorrId_list = inspectionService.getApplicationNoListByPool(systemFilterPool);
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < appCorrId_list.size(); i++) {
                sb.append(":appCorrId")
                        .append(i)
                        .append(',');
            }
            String inSql = sb.substring(0, sb.length() - 1) + ")";
            searchParam.addParam("appCorrId_list", inSql);
            for (int i = 0; i < appCorrId_list.size(); i++) {
                searchParam.addFilter("appCorrId" + i, appCorrId_list.get(i));
            }
            if (!StringUtil.isEmpty(application_no)) {
                searchParam.addFilter("application_no", application_no, true);
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
                searchParam.addFilter("hci_address", hci_address, true);
            }
        }
        List<SelectOption> appStatusOption = systemSearchAssignPoolService.getAppStatusOption(groupRoleFieldDto.getCurStage());
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
        ParamUtil.setSessionAttr(bpc.request, "systemPool", (Serializable) systemPool);
        ParamUtil.setSessionAttr(bpc.request, "systemSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
    }

    private boolean getStageBooleanAndSet(String systemAssignStage, GroupRoleFieldDto groupRoleFieldDto) {
        if(!StringUtil.isEmpty(systemAssignStage) && groupRoleFieldDto != null){
            List<SelectOption> stageOptions = groupRoleFieldDto.getStageOption();
            if(!IaisCommonUtils.isEmpty(stageOptions)){
                for(SelectOption so : stageOptions){
                    if(so != null) {
                        if (systemAssignStage.equals(so.getValue())) {
                            groupRoleFieldDto.setCurStage(systemAssignStage);
                            return true;
                        }
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * StartStep: systemPoolAssignSort
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignSort(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignSort start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doSorting(searchParam, bpc.request);
    }

    /**
     * StartStep: systemPoolAssignPage
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignPage start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    /**
     * StartStep: systemPoolAssignQuery
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignQuery(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignQuery start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        QueryHelp.setMainSql("inspectionQuery", "systemGroupPoolSearch",searchParam);
        SearchResult<SystemAssignSearchQueryDto> searchResult = systemSearchAssignPoolService.getSystemGroupPoolByParam(searchParam);
        ParamUtil.setSessionAttr(bpc.request, "systemSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "systemSearchResult", searchResult);
    }

    /**
     * StartStep: systemPoolAssignAssign
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignAssign(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignAssign start ...."));
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto)ParamUtil.getSessionAttr(bpc.request, "groupRoleFieldDto");
        SystemAssignTaskDto systemAssignTaskDto = (SystemAssignTaskDto)ParamUtil.getSessionAttr(bpc.request, "systemAssignTaskDto");
        if(systemAssignTaskDto == null){
            systemAssignTaskDto = new SystemAssignTaskDto();
        }
        //set MOH Officer Field Name
        groupRoleFieldDto = systemSearchAssignPoolService.setGroupMemberName(groupRoleFieldDto);
        String taskId = ParamUtil.getMaskedString(bpc.request, "taskId");
        TaskDto taskDto = taskService.getTaskById(taskId);
        systemAssignTaskDto.setTaskDto(taskDto);
        Map<String, SuperPoolTaskQueryDto> assignMap = (Map<String, SuperPoolTaskQueryDto>) ParamUtil.getSessionAttr(bpc.request, "assignMap");
        //get work group
        ParamUtil.setSessionAttr(bpc.request, "systemAssignTaskDto", systemAssignTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
    }

    /**
     * StartStep: systemPoolAssignVali
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignVali start ...."));
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto)ParamUtil.getSessionAttr(bpc.request, "groupRoleFieldDto");
        SystemAssignTaskDto systemAssignTaskDto = (SystemAssignTaskDto)ParamUtil.getSessionAttr(bpc.request, "systemAssignTaskDto");
        ParamUtil.setSessionAttr(bpc.request, "systemAssignTaskDto", systemAssignTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
    }

    /**
     * StartStep: systemPoolAssignConfirm
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignConfirm(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignConfirm start ...."));
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto)ParamUtil.getSessionAttr(bpc.request, "groupRoleFieldDto");
        SystemAssignTaskDto systemAssignTaskDto = (SystemAssignTaskDto)ParamUtil.getSessionAttr(bpc.request, "systemAssignTaskDto");
        ParamUtil.setSessionAttr(bpc.request, "systemAssignTaskDto", systemAssignTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
    }

    /**
     * StartStep: systemPoolAssignSuccess
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignSuccess start ...."));
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto)ParamUtil.getSessionAttr(bpc.request, "groupRoleFieldDto");
        SystemAssignTaskDto systemAssignTaskDto = (SystemAssignTaskDto)ParamUtil.getSessionAttr(bpc.request, "systemAssignTaskDto");
        ParamUtil.setSessionAttr(bpc.request, "systemAssignTaskDto", systemAssignTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
    }
}
