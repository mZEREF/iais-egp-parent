package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchDto;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.KEY_TASK_LIST_SEARCH_DTO;

public class BsbTaskHelper {
    private BsbTaskHelper() {}

    /**
     * Common method, get query condition from page
     */
    public static TaskListSearchDto bindQueryCondition(HttpServletRequest request, TaskListSearchDto dto) {
        if (dto == null) {
            dto = (TaskListSearchDto) ParamUtil.getSessionAttr(request, KEY_TASK_LIST_SEARCH_DTO);
        }
        if (dto == null) {
            dto = new TaskListSearchDto();
        }
        dto.setSearchAppNo(request.getParameter("searchAppNo"));
        dto.setSearchAppType(request.getParameter("searchAppType"));
        dto.setSearchAppSubType(request.getParameter("searchAppSubType"));
        dto.setSearchSubmissionType(request.getParameter("searchSubmissionType"));
        dto.setSearchAppStatus(request.getParameter("searchAppStatus"));
        dto.setSearchAppDateFrom(request.getParameter("searchAppDateFrom"));
        dto.setSearchAppDateTo(request.getParameter("searchAppDateTo"));
        dto.setSearchModifiedDateFrom(request.getParameter("searchModifiedDateFrom"));
        dto.setSearchModifiedDateTo(request.getParameter("searchModifiedDateTo"));
        return dto;
    }

    /**
     * Common method, assign a value to the query condition 'Application Status' dropdown box based on the current role
     */
    public static void setQueryAppStatusByRoleId(HttpServletRequest request, String roleId){
        List<SelectOption> appStatusTypeOps;
        if (RoleConsts.USER_ROLE_BSB_DO.equals(roleId)) {
            appStatusTypeOps = MasterCodeConstants.DO_SEARCH_APP_STATUS_SELECT_OPTION;
        } else if (RoleConsts.USER_ROLE_BSB_AO.equals(roleId)) {
            appStatusTypeOps = MasterCodeUtil.retrieveOptionsByCodes(MasterCodeConstants.AO_QUERY_APP_STATUS.toArray(new String[0]));
        } else if (RoleConsts.USER_ROLE_BSB_HM.equals(roleId)) {
            appStatusTypeOps = MasterCodeUtil.retrieveOptionsByCodes(MasterCodeConstants.HM_QUERY_APP_STATUS.toArray(new String[0]));
        } else {
            appStatusTypeOps = new ArrayList<>();
        }
        ParamUtil.setRequestAttr(request, "appStatusOps", appStatusTypeOps);
    }

    /**
     * Common method, assign a value to the query condition 'Submission Type' drop-down box
     */
    public static void setQuerySubmissionType(HttpServletRequest request){
        List<SelectOption> submissionTypeOps = MasterCodeUtil.retrieveOptionsByCodes(MasterCodeConstants.COMMON_QUERY_SUBMISSION_TYPE.toArray(new String[0]));
        ParamUtil.setRequestAttr(request, "submissionTypeOps", submissionTypeOps);
    }

    /**
     * Common method, assign a value to the query condition 'Application Sub-Type' drop-down box
     */
    public static void setQueryAppSubType(HttpServletRequest request){
        List<SelectOption> appSubTypeOps = MasterCodeUtil.retrieveOptionsByCodes(MasterCodeConstants.COMMON_QUERY_APP_SUB_TYPE.toArray(new String[0]));
        ParamUtil.setRequestAttr(request, "appSubTypeOps", appSubTypeOps);
    }
}
