package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.TaskListClient;
import sg.gov.moh.iais.egp.bsb.constant.ProcessContants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchResultDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : LiRan
 * @date : 2021/9/2
 */
@Slf4j
@Delegator
public class MohProcessTaskListDelegator {

    private static final String KEY_TASK_LIST_SEARCH_DTO = "taskListSearchDto";
    private static final String KEY_TASK_LIST_PAGE_INFO = "pageInfo";
    private static final String KEY_TASK_LIST_DATA_LIST = "dataList";

    private final TaskListClient taskListClient;

    @Autowired
    public MohProcessTaskListDelegator(TaskListClient taskListClient) {
        this.taskListClient = taskListClient;
    }

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG,
                AuditTrailConsts.FUNCTION_ERROR_MESSAGES_MANAGEMENT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, ProcessContants.class);
    }

    public void prepare(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        TaskListSearchDto searchDto = getSearchDto(request);
        ResponseDto<TaskListSearchResultDto> resultDto = taskListClient.getTaskList(searchDto);
        ParamUtil.setRequestAttr(request, KEY_TASK_LIST_PAGE_INFO, resultDto.getEntity().getPageInfo());
        ParamUtil.setRequestAttr(request, KEY_TASK_LIST_DATA_LIST, resultDto.getEntity().getTasks());
    }

    private TaskListSearchDto getSearchDto(HttpServletRequest request) {
        TaskListSearchDto dto = (TaskListSearchDto) ParamUtil.getSessionAttr(request, KEY_TASK_LIST_SEARCH_DTO);
        if (dto == null) {
            dto = new TaskListSearchDto();
            dto.defaultPaging();
        }
        return dto;
    }
}
