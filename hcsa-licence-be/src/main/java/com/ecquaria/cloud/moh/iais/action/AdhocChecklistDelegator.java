package com.ecquaria.cloud.moh.iais.action;

/*
 *author: yichen
 *date time:12/10/2019 12:59 PM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.service.AdhocChecklistService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Delegator(value = "adhocChecklistDelegator")
@Slf4j
public class AdhocChecklistDelegator {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private AdhocChecklistService adhocChecklistService;

    public void initialize(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        //String taskId = (String) ParamUtil.getRequestAttr(request, "taskId");
        String taskId = "7102C311-D10D-EA11-BE7D-000C29F371DC";
        TaskDto task = taskService.getTaskById(taskId);
        Optional.ofNullable(task).ifPresent(t -> {
                String appNo = t.getRefNo();
                ApplicationDto application = applicationViewService.getApplicaitonByAppNo(appNo);
                List<ChecklistConfigDto> inspectionChecklist = adhocChecklistService.getInspectionChecklist(application);
                log.info("inspectionChecklist info =====>>>>>>>>>>> " + inspectionChecklist.toString());

                ParamUtil.setSessionAttr(request, "inspectionChecklist", (Serializable) inspectionChecklist);
           }
        );
    }
}
