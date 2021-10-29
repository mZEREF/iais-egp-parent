package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.TaskListClient;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchResultDto;
import sop.webflow.rt.api.BaseProcessClass;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_CODE_VALIDATION_FAIL;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;


@Slf4j
@Delegator("bsbTaskListDelegator")
public class TaskListDelegator {
    private static final String KEY_TASK_LIST_SEARCH_DTO = "taskListSearchDto";
    private static final String KEY_TASK_LIST_PAGE_INFO = "pageInfo";
    private static final String KEY_TASK_LIST_DATA_LIST = "dataList";

    private static final String KEY_ACTION_VALUE = "action_value";
    private static final String KEY_ACTION_ADDT = "action_additional";

    private static final String KEY_PAGE_SIZE = "pageJumpNoPageSize";
    private static final String KEY_PAGE_NO = "pageJumpNoTextchangePage";


    private final TaskListClient taskListClient;

    @Autowired
    public TaskListDelegator(TaskListClient taskListClient) {
        this.taskListClient = taskListClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_TASK_LIST_SEARCH_DTO);

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INTRANET_DASHBOARD, AuditTrailConsts.FUNCTION_TASK_LIST);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        TaskListSearchDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_TASK_LIST_SEARCH_DTO, searchDto);

        ResponseDto<TaskListSearchResultDto> resultDto = taskListClient.getTaskList(searchDto);

        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_DATA_LIST, resultDto.getEntity().getTasks());
        } else {
            log.info("Search Task List fail");
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_DATA_LIST, new ArrayList<>());
            if(ERROR_CODE_VALIDATION_FAIL.equals(resultDto.getErrorCode())) {
                ParamUtil.setRequestAttr(request, ERROR_INFO_ERROR_MSG, resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG));
            }
        }

        List<SelectOption> appTypeOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BSB_APP_TYPE);
        ParamUtil.setRequestAttr(request, "appTypeOps", appTypeOps);
        List<SelectOption> facTypeOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BSB_FAC_CLASSIFICATION);
        ParamUtil.setRequestAttr(request, "facTypeOps", facTypeOps);
        List<SelectOption> processTypeOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BSB_PRO_TYPE);
        ParamUtil.setRequestAttr(request, "processTypeOps", processTypeOps);
        List<SelectOption> appStatusTypeOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BSB_APP_STATUS);
        ParamUtil.setRequestAttr(request, "appStatusOps", appStatusTypeOps);
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




    private TaskListSearchDto getSearchDto(HttpServletRequest request) {
        TaskListSearchDto dto = (TaskListSearchDto) ParamUtil.getSessionAttr(request, KEY_TASK_LIST_SEARCH_DTO);
        if (dto == null) {
            dto = new TaskListSearchDto();
            dto.defaultPaging();
        }
        return dto;
    }

    private static TaskListSearchDto bindModel(HttpServletRequest request, TaskListSearchDto dto) {
        dto.setSearchFacName(request.getParameter("searchFacName"));
        dto.setSearchFacAddr(request.getParameter("searchFacAddr"));
        dto.setSearchFacType(request.getParameter("searchFacType"));
        dto.setSearchAppNo(request.getParameter("searchAppNo"));
        dto.setSearchAppType(request.getParameter("searchAppType"));
        dto.setSearchAppStatus(request.getParameter("searchAppStatus"));
        dto.setSearchProcessType(request.getParameter("searchProcessType"));
        dto.setSearchAppDateFrom(request.getParameter("searchAppDateFrom"));
        dto.setSearchAppDateTo(request.getParameter("searchAppDateTo"));
        return dto;
    }
}
