package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.impl.ApplicationViewServiceImp;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * HcsaApplicationDelegator
 *
 * @author suocheng
 * @date 10/17/2019
 */
@Delegator("hcsaApplicationDelegator")
@Slf4j
public class HcsaApplicationDelegator {
    @Autowired
    private TaskService taskService;
    public void routingTask(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the do routingTask start ...."));
        AuditTrailHelper.auditFunction("hcsa-licence", "hcsa licence");
        List<ApplicationDto> applicationDtos = new ArrayList();
        ApplicationDto applicationDto0 = new ApplicationDto();
        applicationDto0.setApplicationNo("test applicaitonNo");
        applicationDto0.setServiceId("test serviceId");
        applicationDtos.add(applicationDto0);
        List<TaskDto> taskDtos = new ArrayList<>();
        if(applicationDtos != null && applicationDtos.size() > 0){
            for(ApplicationDto applicationDto : applicationDtos){
                //comm pool
                if(true){
                    String workGroupName = "asows";
                    TaskDto taskDto = new TaskDto();
                    taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_ASO);
                    taskDto.setTaskType(TaskConsts.TASK_TYPE_MAIN_FLOW);
                    taskDto.setTaskCategory(applicationDto.getServiceId());
                    //taskDto.setTaskSubject("Task Subject");
                    taskDto.setPriority(TaskConsts.TASK_PRIORITY_NOTHING);
                    taskDto.setRefNo(applicationDto.getApplicationNo());
                    taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
                    taskDto.setGroupShortName(workGroupName);
                    taskDto.setSlaInDays(TaskConsts.TASK_SLA_IN_DAYS);
                    taskDto.setSlaAlertInDays(TaskConsts.TASK_SLA_ALERT_IN_DAYS);
                    taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    taskDtos.add(taskDto);
                }
            }

        }else{
            log.error(StringUtil.changeForLog("The applicationDtos is null"));
        }
        taskDtos = taskService.createTasks(taskDtos);


//        List<UserGroup> userGroups = WorkGroupService.getInstance().retrieveAgencyWorkingGroup(workGroupName);
//        if(userGroups!=null && userGroups.size()>0){
//            List<User> users = WorkGroupService.getInstance().getUsersByGroupNo(userGroups.get(0).getGroupNo());
//
//        }
        log.debug(StringUtil.changeForLog("the do routingTask end ...."));
    }

    /**
     * StartStep: prepareData
     *
     * @param bpc
     * @throws
     */
    public void prepareData(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareData start ...."));
        String  appNo = "AN1911136061-01";
        ApplicationViewServiceImp applicationViewService = new ApplicationViewServiceImp();
        ApplicationViewDto applicationViewDto = applicationViewService.searchByAppNo(appNo);
        HttpServletRequest request=bpc.request;
        ParamUtil.setRequestAttr(request,"applicationViewDto", applicationViewDto);
        log.debug(StringUtil.changeForLog("the do prepareData end ...."));
    }



}
