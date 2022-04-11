package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbTaskClient;
import sg.gov.moh.iais.egp.bsb.client.OrganizationClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskReassignDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchResultDto;
import sg.gov.moh.iais.egp.bsb.entity.TaskView;
import sg.gov.moh.iais.egp.bsb.service.UserRoleService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_CODE_VALIDATION_FAIL;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.RoleConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.*;


@Slf4j
@Delegator("bsbReassignTaskDelegator")
public class ReassignTaskDelegator {
    private static final String MODULE_NAME = "BSB Reassign Task";
    private static final String FUNCTION_NAME = "Reassign Task";
    private static final String USER_ID = "userId";
    private static final String APP_ID = "appId";
    private static final String MASK_USER_ID = "maskUserId";
    private static final String ACTION_TYPE = "action_type";
    private static final String ACTION_TYPE_NEXT = "doNext";
    private static final String ACTION_TYPE_PREPARE = "prepare";
    private static final String USER_OPTION = "userOption";
    private static final String TASK_REASSIGN_DTO = "taskReassignDto";

    private final UserRoleService userRoleService;
    private final BsbTaskClient bsbTaskClient;
    private final OrganizationClient organizationClient;

    @Autowired
    public ReassignTaskDelegator(UserRoleService userRoleService, BsbTaskClient bsbTaskClient, OrganizationClient organizationClient) {
        this.userRoleService = userRoleService;
        this.bsbTaskClient = bsbTaskClient;
        this.organizationClient = organizationClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_TASK_LIST_SEARCH_DTO);
        request.getSession().removeAttribute(USER_OPTION);
        request.getSession().removeAttribute(TASK_REASSIGN_DTO);
    }

    public void init(BaseProcessClass bpc) {
        // put user role options into session
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<String> userRoles = new ArrayList<>(loginContext.getRoleIds());
        ArrayList<SelectOption> roleOps = new ArrayList<>(userRoleService.getRoleSelectOptions(AppConsts.HALP_EGP_DOMAIN, userRoles));
        ParamUtil.setSessionAttr(bpc.request, KEY_ROLE_OPTIONS, roleOps);
    }

    public void prepareData(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME);

        HttpServletRequest request = bpc.request;
        TaskListSearchDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_TASK_LIST_SEARCH_DTO, searchDto);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String curRoleId = loginContext.getCurRoleId();
        ResponseDto<TaskListSearchResultDto> resultDto = bsbTaskClient.getTaskList(searchDto);

        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_PAGE_INFO, resultDto.getEntity().getPageInfo());
            List<TaskView> tasks = resultDto.getEntity().getTasks();
            List<String> doUserIds = new ArrayList<>(tasks.size());
            List<String> aoUserIds = new ArrayList<>(tasks.size());
            List<String> hmUserIds = new ArrayList<>(tasks.size());
            //get the do/ao/hm user id from task
            setUserIdList(tasks, doUserIds, aoUserIds, hmUserIds);
            //
            List<OrgUserDto> userDtoList = new ArrayList<>();
            if (curRoleId.equals(ROLE_BSB_DO) && !CollectionUtils.isEmpty(doUserIds)) {
                userDtoList = organizationClient.retrieveOrgUserAccount(doUserIds).getEntity();
            } else if (curRoleId.equals(ROLE_BSB_AO) && !CollectionUtils.isEmpty(aoUserIds)) {
                userDtoList = organizationClient.retrieveOrgUserAccount(aoUserIds).getEntity();
            } else if (curRoleId.equals(ROLE_BSB_HM) && !CollectionUtils.isEmpty(hmUserIds)) {
                userDtoList = organizationClient.retrieveOrgUserAccount(hmUserIds).getEntity();
            }
            Map<String, OrgUserDto> userDtoMap = sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(userDtoList, OrgUserDto::getId);
            //set current owner by current role and the user id from task
            setTaskCurOwner(tasks, curRoleId, userDtoMap);
            //
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_DATA_LIST, tasks);
        } else {
            log.info("Search Task List fail");
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_DATA_LIST, new ArrayList<>());
            if (ERROR_CODE_VALIDATION_FAIL.equals(resultDto.getErrorCode())) {
                log.warn("Fail reason: {}", resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG));
            }
        }
        ParamUtil.setRequestAttr(request, KEY_CUR_ROLE, curRoleId);
    }

    private void setUserIdList(List<TaskView> tasks, List<String> doUserIds, List<String> aoUserIds, List<String> hmUserIds) {
        for (TaskView task : tasks) {
            if (task.getApplication() != null) {
                if (StringUtils.hasLength(task.getApplication().getDoUserId())) {
                    doUserIds.add(task.getApplication().getDoUserId());
                }
                if (StringUtils.hasLength(task.getApplication().getAoUserId())) {
                    aoUserIds.add(task.getApplication().getAoUserId());
                }
                if (StringUtils.hasLength(task.getApplication().getHmUserId())) {
                    hmUserIds.add(task.getApplication().getHmUserId());
                }
            }
        }
    }

    private void setTaskCurOwner(List<TaskView> tasks, String curRoleId, Map<String, OrgUserDto> userDtoMap) {
        for (TaskView task : tasks) {
            if (task.getApplication() != null) {
                if (curRoleId.equals(ROLE_BSB_DO) && StringUtils.hasLength(task.getApplication().getDoUserId())) {
                    task.setCurOwner(userDtoMap.get(task.getApplication().getDoUserId()).getUserId());
                } else if (curRoleId.equals(ROLE_BSB_AO) && StringUtils.hasLength(task.getApplication().getAoUserId())) {
                    task.setCurOwner(userDtoMap.get(task.getApplication().getAoUserId()).getUserId());
                } else if (curRoleId.equals(ROLE_BSB_HM) && StringUtils.hasLength(task.getApplication().getHmUserId())) {
                    task.setCurOwner(userDtoMap.get(task.getApplication().getAoUserId()).getUserId());
                }
            }
        }
    }

    public void search(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        TaskListSearchDto searchDto = bindModel(request, getSearchDto(request));
        searchDto.setPage(0);
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

        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
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

    public void preReassignData(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME);
        HttpServletRequest request = bpc.request;
        TaskReassignDto dto = getReassignDto(request);
        if (StringUtils.isEmpty(dto.getTaskId()) && StringUtils.isEmpty(dto.getAppId())) {
            String maskedTaskId = ParamUtil.getString(request, KEY_ACTION_VALUE);
            String maskedAppId = ParamUtil.getString(request, APP_ID);

            if (log.isInfoEnabled()) {
                log.info("masked task id: [{}]", LogUtil.escapeCrlf(maskedTaskId));
                log.info("masked application id: [{}]", LogUtil.escapeCrlf(maskedAppId));
            }
            String taskId = MaskUtil.unMaskValue("id", maskedTaskId);
            if (taskId == null || taskId.equals(maskedTaskId)) {
                throw new IaisRuntimeException("Invalid masked task ID");
            }
            String appId = MaskUtil.unMaskValue(APP_ID, maskedAppId);
            if (appId == null || appId.equals(maskedAppId)) {
                throw new IaisRuntimeException("Invalid masked application id");
            }
            dto.setTaskId(taskId);
            dto.setAppId(appId);
            ParamUtil.setSessionAttr(request,TASK_REASSIGN_DTO,dto);
        }
        //
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String curRoleId = loginContext.getCurRoleId();
        List<OrgUserDto> dtoList = organizationClient.retrieveOrgUserAccountByRoleId(curRoleId).getEntity();

        List<SelectOption> opts = new ArrayList<>();
        dtoList.forEach(orgUserDto -> opts.add(new SelectOption(MaskUtil.maskValue(MASK_USER_ID, orgUserDto.getId()), orgUserDto.getUserId())));
        ParamUtil.setSessionAttr(request, USER_OPTION, (Serializable) opts);

    }

    public void validateReassign(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedUserId = ParamUtil.getString(request, "reassignUserId");
        if (log.isInfoEnabled()) {
            log.info("masked user id: [{}]", LogUtil.escapeCrlf(maskedUserId));
        }

        String actionType = "";
        if (StringUtils.hasLength(maskedUserId)) {
            actionType = ACTION_TYPE_NEXT;
            String userId = MaskUtil.unMaskValue(MASK_USER_ID, maskedUserId);
            ParamUtil.setRequestAttr(request, USER_ID, userId);
        } else {
            Map<String, String> errorMap = Maps.newHashMapWithExpectedSize(1);
            errorMap.put(USER_ID, "This is a mandatory field");
            ValidationResultDto validationResultDto = ValidationResultDto.of(false, errorMap);
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
        ParamUtil.setRequestAttr(request, "selectedUser", maskedUserId);
    }

    public void reassignTask(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        TaskReassignDto dto = getReassignDto(request);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String curRoleId = loginContext.getCurRoleId();
        String userId = ParamUtil.getRequestString(request, USER_ID);
        dto.setCurRoleId(curRoleId);
        dto.setUserId(userId);
        ResponseDto<String> responseDto = bsbTaskClient.reassignTask(dto);
        if (responseDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_ASSIGN_RESULT, "Task has been reassigned successfully!");
        } else {
            ParamUtil.setRequestAttr(request, KEY_ASSIGN_RESULT, "Fail to reassign this task!");
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

            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            dto.setRoleIds(Collections.singleton(loginContext.getCurRoleId()));
        }
        return dto;
    }

    private static TaskListSearchDto bindModel(HttpServletRequest request, TaskListSearchDto dto) {
        dto.setSearchAppNo(request.getParameter("searchAppNo"));
        dto.setSearchAppType(request.getParameter("searchAppType"));
        dto.setSearchAppStatus(request.getParameter("searchAppStatus"));
        /* This is because we share the search dto with task pool module,
         * When user open the common pool and task list at the same time, we set these columns to avoid error */
        if (dto.getUserId() == null) {
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            dto.setUserId(loginContext.getUserId());
        }
        return dto;
    }

    private TaskReassignDto getReassignDto(HttpServletRequest request){
        TaskReassignDto dto = (TaskReassignDto) ParamUtil.getSessionAttr(request,TASK_REASSIGN_DTO);
        if (dto == null){
            dto = new TaskReassignDto();
        }
        return dto;
    }
}
