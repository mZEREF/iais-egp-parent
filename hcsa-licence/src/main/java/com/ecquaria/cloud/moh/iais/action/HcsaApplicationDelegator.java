package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSupDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
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

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;

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
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do cleanSession start ...."));
        AuditTrailHelper.auditFunction("hcsa-licence", "hcsa licence");
        ParamUtil.setSessionAttr(bpc.request,"taskDto",null);
        ParamUtil.setSessionAttr(bpc.request,"applicationViewDto",null);
        log.debug(StringUtil.changeForLog("the do cleanSession end ...."));
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
        ApplicationViewDto applicationViewDto = applicationViewService.searchByAppNo(appNo);
        List<HcsaSvcDocConfigDto> docTitleList=applicationViewService.getTitleById(applicationViewDto.getTitleIdList());
        List<OrganizationDto> userNameList=applicationViewService.getUserNameById(applicationViewDto.getUserIdList());
        List<AppSupDocDto> appSupDocDtos=applicationViewDto.getAppSupDocDtoList();
        for (int i = 0; i <appSupDocDtos.size(); i++) {
            for (int j = 0; j <docTitleList.size() ; j++) {
                if (appSupDocDtos.get(i).getFile().equals(docTitleList.get(j).getId())){
                    appSupDocDtos.get(i).setFile(docTitleList.get(j).getDocTitle());
                }
            }
            for (int j = 0; j <userNameList.size() ; j++) {
                if (appSupDocDtos.get(i).getSubmittedBy().equals(userNameList.get(j).getId())){
                    appSupDocDtos.get(i).setSubmittedBy(userNameList.get(j).getUserName());
                }
            }
        }
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtoList=applicationViewService.getStageName(applicationViewDto.getApplicationDto().getServiceId(),taskDto.getTaskKey());
        applicationViewDto.setHcsaSvcRoutingStageDtoList(hcsaSvcRoutingStageDtoList);
        ParamUtil.setRequestAttr(bpc.request,"applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request,"taskDto", taskDto);
        log.debug(StringUtil.changeForLog("the do prepareData end ...."));
    }

    /**
     * StartStep: chooseStage
     *
     * @param bpc
     * @throws
     */
    public void chooseStage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do chooseStage start ...."));



        log.debug(StringUtil.changeForLog("the do chooseStage end ...."));
    }


    /**
     * StartStep: rontingTaskToPSO
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToPSO(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToPSO start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_PSO,ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING);
        log.debug(StringUtil.changeForLog("the do rontingTaskToPSO end ...."));
    }


    /**
     * StartStep: rontingTaskToINS
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToINS(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToINS start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_INS,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION);
        log.debug(StringUtil.changeForLog("the do rontingTaskToINS end ...."));
    }


    /**
     * StartStep: rontingTaskToASO
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToASO(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToASO start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_ASO,ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
        log.debug(StringUtil.changeForLog("the do rontingTaskToASO end ...."));
    }

    /**
     * StartStep: rontingTaskToAO1
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO1(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO1 start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO1,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01);
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO1 end ...."));
    }



    /**
     * StartStep: rontingTaskToAO2
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO2(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO2 start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO2,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02);
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO2 end ...."));
    }

    /**
     * StartStep: rontingTaskToAO3
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO3(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO3 start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO3,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);

        //todo: isAllApplicationSubmit
       // boolean isAllApplicationSubmit = applicationViewService.isAllApplicationSubmit(taskDto.getRefNo());
        //create the licence
        //if(isAllApplicationSubmit){
          //todo:create licence
        //}
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO3 end ...."));
    }
    //***************************************
    //private methods
    //**************************************



    private void routingTask(BaseProcessClass bpc,String stageId,String appStatus ) throws FeignException {
        //completedTask
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
        taskDto =  completedTask(taskDto);
        //add history for this stage complate
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(),taskDto.getTaskKey(),internalRemarks);
        //updateApplicaiton
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        updateApplicaiton(applicationDto,appStatus);
        applicationViewDto.setApplicationDto(applicationDto);
        // send the task
        if(!StringUtil.isEmpty(stageId)){
            taskService.routingTask(applicationDto,stageId);
            //add history for next stage start
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(),stageId,null);
        }
    }
    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,
                                                                         String stageId, String internalRemarks){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserId());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        return appPremisesRoutingHistoryDto;
    }
    private int remainDays(TaskDto taskDto){
       int result = 0;
       //todo: wait count kpi
       String  resultStr = DurationFormatUtils.formatPeriod(taskDto.getDateAssigned().getTime(),taskDto.getSlaDateCompleted().getTime(), "d");
      log.debug(StringUtil.changeForLog("The resultStr is -->:")+resultStr);
      return  result;
    }


    private TaskDto completedTask(TaskDto taskDto){
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(remainDays(taskDto));
        return taskService.updateTask(taskDto);
    }
    private ApplicationDto updateApplicaiton(ApplicationDto applicationDto,String appStatus){
        applicationDto.setStatus(appStatus);
        return applicationViewService.updateApplicaiton(applicationDto);
    }
}
