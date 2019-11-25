package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.impl.ApplicationViewServiceImp;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.ArrayList;
import java.util.Date;
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



    /**
     * StartStep: cleanSession
     *
     * @param bpc
     * @throws
     */
    public void cleanSession(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do cleanSession start ...."));

        log.debug(StringUtil.changeForLog("the do cleanSession end ...."));
    }



    public void routingTask(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the do routingTask start ...."));
        AuditTrailHelper.auditFunction("hcsa-licence", "hcsa licence");
        List<ApplicationDto> applicationDtos = new ArrayList();
        ApplicationDto applicationDto0 = new ApplicationDto();
        applicationDto0.setApplicationNo("test applicaitonNo");
        applicationDto0.setServiceId("35F99D15-820B-EA11-BE7D-000C29F371DC");
        applicationDto0.setApplicationType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        applicationDtos.add(applicationDto0);
       // taskService.routingAdminScranTask(applicationDtos);
        taskService.routingTask(applicationDto0,HcsaConsts.ROUTING_STAGE_PSO);
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
        //get the task
        String  taskId = ParamUtil.getString(bpc.request,"taskId");
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appNo = taskDto.getRefNo();
        //get routing stage dropdown send to page.
        ApplicationViewServiceImp applicationViewService = new ApplicationViewServiceImp();
        ApplicationViewDto applicationViewDto = applicationViewService.searchByAppNo(appNo);
        applicationViewDto.setApplicationNo(appNo);









        ParamUtil.setRequestAttr(bpc.request,"applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request,"taskDto", taskDto);
        log.debug(StringUtil.changeForLog("the do prepareData end ...."));
    }


    /**
     * StartStep: rontingTaskToPSO
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToPSO(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do rontingTaskToPSO start ...."));
        //update Task information
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
        taskDto.setTaskStatus("");
        //todo: otehr fields
        taskDto =  taskService.updateTask(taskDto);
       // application status
        //create new task.
        ApplicationDto applicationDto =  new ApplicationDto();

        //taskService.routingTask(applicationDto,);

        log.debug(StringUtil.changeForLog("the do rontingTaskToPSO end ...."));
    }

    /**
     * StartStep: rontingTaskToINS
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToINS(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do rontingTaskToINS start ...."));

        log.debug(StringUtil.changeForLog("the do rontingTaskToINS end ...."));
    }


    /**
     * StartStep: rontingTaskToASO
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToASO(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do rontingTaskToASO start ...."));

        log.debug(StringUtil.changeForLog("the do rontingTaskToASO end ...."));
    }

    /**
     * StartStep: rontingTaskToAO1
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO1(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO1 start ...."));

        log.debug(StringUtil.changeForLog("the do rontingTaskToAO1 end ...."));
    }

    /**
     * StartStep: rontingTaskToAO3
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO3(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO3 start ...."));
        //update Task information
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(remainDays(taskDto));
        taskDto =  taskService.updateTask(taskDto);
        // application status
        //create new task.
        ApplicationDto applicationDto =  new ApplicationDto();

        //taskService.routingTask(applicationDto,);

        log.debug(StringUtil.changeForLog("the do rontingTaskToAO3 end ...."));
    }

    /**
     * StartStep: rontingTaskToAO2
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO2(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO2 start ...."));

        log.debug(StringUtil.changeForLog("the do rontingTaskToAO2 end ...."));
    }

    private int remainDays(TaskDto taskDto){
       int result = 0;
       //todo:
       String  resultStr = DurationFormatUtils.formatPeriod(taskDto.getDateAssigned().getTime(),taskDto.getSlaDateCompleted().getTime(), "d");
      log.debug(StringUtil.changeForLog("The resultStr is -->:")+resultStr);
        return  result;
    }
}
