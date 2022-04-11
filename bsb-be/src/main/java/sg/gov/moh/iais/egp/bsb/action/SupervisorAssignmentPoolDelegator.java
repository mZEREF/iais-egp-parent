package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbTaskClient;
import sg.gov.moh.iais.egp.bsb.client.OrganizationClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchResultDto;
import sg.gov.moh.iais.egp.bsb.entity.TaskView;
import sg.gov.moh.iais.egp.bsb.service.UserRoleService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_CODE_VALIDATION_FAIL;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.RoleConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.*;

/**
 * multi assign inspection task pool
 */
@Slf4j
@Delegator("supervisorAssignmentPoolDelegator")
public class SupervisorAssignmentPoolDelegator {
    private static final String MODULE_NAME = "BSB Task Pool";

    private final UserRoleService userRoleService;
    private final BsbTaskClient bsbTaskClient;
    private final OrganizationClient organizationClient;

    @Autowired
    public SupervisorAssignmentPoolDelegator(UserRoleService userRoleService, BsbTaskClient bsbTaskClient, OrganizationClient organizationClient) {
        this.userRoleService = userRoleService;
        this.bsbTaskClient = bsbTaskClient;
        this.organizationClient = organizationClient;
    }

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
        AuditTrailHelper.auditFunction(MODULE_NAME, "Supervisor Assignment Pool");

        HttpServletRequest request = bpc.request;
        TaskListSearchDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_TASK_LIST_SEARCH_DTO, searchDto);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String curRoleId = loginContext.getCurRoleId();

        ResponseDto<TaskListSearchResultDto> resultDto = bsbTaskClient.searchInspectionTaskPool(searchDto);

        if (resultDto.ok()) {
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
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_DATA_LIST, tasks);
        } else {
            log.info("Search Task List fail");
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_DATA_LIST, new ArrayList<>());
            if(ERROR_CODE_VALIDATION_FAIL.equals(resultDto.getErrorCode())) {
                log.warn("Fail reason: {}", resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG));
            }
        }
        ParamUtil.setRequestAttr(request, KEY_CUR_ROLE, curRoleId);
        List<SelectOption> inspectionAppStatusOption = MasterCodeUtil.retrieveOptionsByCodes(MasterCodeConstants.INSPECTION_APP_STATUS.toArray(new String[0]));
        ParamUtil.setRequestAttr(request,"insAppStatus",inspectionAppStatusOption);
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

    private TaskListSearchDto getSearchDto(HttpServletRequest request) {
        TaskListSearchDto dto = (TaskListSearchDto) ParamUtil.getSessionAttr(request, KEY_TASK_LIST_SEARCH_DTO);
        if (dto == null) {
            dto = new TaskListSearchDto();
            dto.defaultPaging();

            LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            dto.setUserId(loginContext.getUserId());
            dto.setRoleIds(Collections.singleton(loginContext.getCurRoleId()));
        }
        return dto;
    }

    private static TaskListSearchDto bindModel(HttpServletRequest request, TaskListSearchDto dto) {
        dto.setSearchAppNo(request.getParameter("searchAppNo"));
        dto.setSearchAppType(request.getParameter("searchAppType"));
        dto.setSearchAppStatus(request.getParameter("searchAppStatus"));
        dto.setSearchSubmissionType(request.getParameter("searchSubmissionType"));
        /* This is because we share the search dto with task list module,
         * When user open the common pool and task list at the same time, we set these columns to avoid error */
        if (dto.getUserId() == null) {
            LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            dto.setUserId(loginContext.getUserId());
        }
        return dto;
    }
}
