package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.BsbTaskClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskAssignDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchResultDto;
import sg.gov.moh.iais.egp.bsb.service.BsbTaskService;
import sg.gov.moh.iais.egp.bsb.service.UserRoleService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_CODE_VALIDATION_FAIL;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_ADDITIONAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_PAGE_NO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_PAGE_SIZE;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.*;


@Slf4j
@Delegator("bsbTaskCommonPoolDelegator")
@RequiredArgsConstructor
public class TaskCommonPoolDelegator {
    private static final String MODULE_NAME = "BSB Task Pool";

    private final UserRoleService userRoleService;
    private final BsbTaskClient bsbTaskClient;
    private final BsbTaskService taskService;

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_TASK_LIST_SEARCH_DTO);
    }

    public void init(BaseProcessClass bpc) {
        // put user role options into session
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<String> userRoles = new ArrayList<>(loginContext.getRoleIds());
        ArrayList<SelectOption> roleOps = new ArrayList<>(userRoleService.getRoleSelectOptions(AppConsts.HALP_EGP_DOMAIN, userRoles));
        ParamUtil.setSessionAttr(bpc.request, KEY_ROLE_OPTIONS, roleOps);
    }

    public void prepareData(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(MODULE_NAME, "Task List");

        HttpServletRequest request = bpc.request;
        TaskListSearchDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_TASK_LIST_SEARCH_DTO, searchDto);

        ResponseDto<TaskListSearchResultDto> resultDto = bsbTaskClient.getTaskPool(searchDto);

        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_DATA_LIST, resultDto.getEntity().getResultDtos());
        } else {
            log.info("Search Task List fail");
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_DATA_LIST, new ArrayList<>());
            if(ERROR_CODE_VALIDATION_FAIL.equals(resultDto.getErrorCode())) {
                log.warn("Fail reason: {}", resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG));
                ParamUtil.setRequestAttr(request,"errorMsg", JsonUtil.parseToJson(resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG)));
            }
        }

        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        ParamUtil.setRequestAttr(request, KEY_CUR_ROLE, loginContext.getCurRoleId());
        List<SelectOption> appTypeOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BSB_APP_TYPE);
        ParamUtil.setRequestAttr(request, "appTypeOps", appTypeOps);
        List<SelectOption> appStatusTypeOps = MasterCodeUtil.retrieveOptionsByCodes(MasterCodeConstants.COMMON_QUERY_APP_STATUS.toArray(new String[0]));
        ParamUtil.setRequestAttr(request, "appStatusOps", appStatusTypeOps);
        List<SelectOption> appSubTypeOps = MasterCodeUtil.retrieveOptionsByCodes(MasterCodeConstants.COMMON_QUERY_APP_SUB_TYPE.toArray(new String[0]));
        ParamUtil.setRequestAttr(request, "appSubTypeOps", appSubTypeOps);
        List<SelectOption> submissionTypeOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BSB_SUBMISSION_TYPE);
        ParamUtil.setRequestAttr(request, "submissionTypeOps", submissionTypeOps);
    }

    public void search(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        TaskListSearchDto searchDto = bindModel(request, getSearchDto(request));
        searchDto.setPage(0);
        ParamUtil.setSessionAttr(request, KEY_TASK_LIST_SEARCH_DTO, searchDto);
    }

    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        TaskListSearchDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDITIONAL);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, KEY_TASK_LIST_SEARCH_DTO, searchDto);
    }

    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        TaskListSearchDto searchDto = getSearchDto(request);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        switch (actionValue) {
            case "changeSize":
                int pageSize = ParamUtil.getInt(request, KEY_PAGE_SIZE);
                searchDto.setPage(0);
                searchDto.setSize(pageSize);
                break;
            case "changePage":
                int pageNo = ParamUtil.getInt(request, KEY_PAGE_NO);
                searchDto.setPage(pageNo - 1);
                break;
            default:
                log.warn("page, action_value is invalid: {}", actionValue);
                break;
        }
        ParamUtil.setSessionAttr(request, KEY_TASK_LIST_SEARCH_DTO, searchDto);
    }

    public void changeRole(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String roleId = ParamUtil.getString(request, KEY_ROLE_ID);

        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        /* if the roleId in request is not a member of user's roleIds, the valus is invalid */
        if (roleId == null || !loginContext.getRoleIds().contains(roleId)) {
            throw new IaisRuntimeException("Invalid role Id");
        }

        loginContext.setCurRoleId(roleId);
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);

        /* Call bindModel, so the new search condition will be in effect.
         * It's also ok to use the old search condition, then just remove the bindModel */
        TaskListSearchDto searchDto = bindModel(request, getSearchDto(request));
        searchDto.setRoleIds(Collections.singleton(roleId));
        ParamUtil.setSessionAttr(request, KEY_TASK_LIST_SEARCH_DTO, searchDto);
    }

    public void assign(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(MODULE_NAME, "Assign Task");
        HttpServletRequest request = bpc.request;
        String maskedTaskId = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String maskedAppId = ParamUtil.getString(request, "appId");

        if (log.isInfoEnabled()) {
            log.info("masked task id: [{}]", LogUtil.escapeCrlf(maskedTaskId));
            log.info("masked application id: [{}]", LogUtil.escapeCrlf(maskedAppId));
        }
        String taskId = MaskUtil.unMaskValue("id", maskedTaskId);
        if (taskId == null || taskId.equals(maskedTaskId)) {
            throw new IaisRuntimeException("Invalid masked task ID");
        }
        String appId = MaskUtil.unMaskValue("appId", maskedAppId);
        if (appId == null || appId.equals(maskedAppId)) {
            throw new IaisRuntimeException("Invalid masked application id");
        }

        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        String curRoleId = loginContext.getCurRoleId();
        ResponseDto<String> assignResult = bsbTaskClient.assignTask(taskId, userId, appId,curRoleId);
        if (assignResult.ok()) {
            ParamUtil.setRequestAttr(request, KEY_ASSIGN_RESULT, "Task has been assigned to you successfully!");
        } else {
            ParamUtil.setRequestAttr(request, KEY_ASSIGN_RESULT, "Fail to assign this task to you!");
        }
    }

    public void multiAssign(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(MODULE_NAME, "Assign Task");
        HttpServletRequest request = bpc.request;
        String[] maskedTaskIds = ParamUtil.getStrings(request, KEY_PICK_UP_TASK);
        if (maskedTaskIds == null || maskedTaskIds.length < 1) {
            ParamUtil.setRequestAttr(request, KEY_ASSIGN_RESULT, "No task has been picked up!");
            return;
        }

        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();

        // create DTO used to call assign API
        List<TaskAssignDto> taskAssignDtoList = new ArrayList<>(maskedTaskIds.length);
        for (String maskedTaskId : maskedTaskIds) {
            String taskId = MaskUtil.unMaskValue("id", maskedTaskId);
            if ((taskId == null || taskId.equals(maskedTaskId)) && log.isWarnEnabled()) {
                log.warn("Invalid masked task id: {}", maskedTaskId);
            } else {
                TaskAssignDto dto = new TaskAssignDto(taskId, userId);
                taskAssignDtoList.add(dto);
            }
        }

        if (taskAssignDtoList.isEmpty()) {
            ParamUtil.setRequestAttr(request, KEY_ASSIGN_RESULT, "No task has been picked up!");
        } else {
            // assign tasks
            taskAssignDtoList = bsbTaskClient.assignMultiTasks(taskAssignDtoList);

            // generate result acknowledge message
            List<String> successAppNos = new ArrayList<>(taskAssignDtoList.size());
            for (TaskAssignDto dto : taskAssignDtoList) {
                if (dto.isSuccess()) {
                    successAppNos.add(dto.getAppNo());
                }
            }
            String msg = "Following tasks have been assigned (Application No.): " + String.join(", ", successAppNos);
            ParamUtil.setRequestAttr(request, KEY_ASSIGN_RESULT, msg);
        }

    }

    public void detail(BaseProcessClass bpc) {
        // do nothing now
    }


    private TaskListSearchDto getSearchDto(HttpServletRequest request) {
        TaskListSearchDto dto = (TaskListSearchDto) ParamUtil.getSessionAttr(request, KEY_TASK_LIST_SEARCH_DTO);
        if (dto == null) {
            dto = new TaskListSearchDto();
            dto.defaultPaging();

            LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            dto.setRoleIds(Collections.singleton(loginContext.getCurRoleId()));
        }
        return dto;
    }

    private TaskListSearchDto bindModel(HttpServletRequest request, TaskListSearchDto dto) {
        taskService.bindQueryCondition(request,dto);
        /* This is because we share the search dto with task list module,
         * When user open the common pool and task list at the same time, we set these columns to avoid error */
        dto.setUserId(null);
        dto.setSearchAppStatus(null);
        return dto;
    }
}
