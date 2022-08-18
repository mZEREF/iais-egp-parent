package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchDto;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.KEY_TASK_LIST_SEARCH_DTO;

@Service
public class BsbTaskService {
    public TaskListSearchDto bindQueryCondition(HttpServletRequest request, TaskListSearchDto dto) {
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
}
