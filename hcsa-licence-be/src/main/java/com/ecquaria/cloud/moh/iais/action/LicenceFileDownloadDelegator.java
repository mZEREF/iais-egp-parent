package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RequestInformationSubmitDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.BroadcastService;
import com.ecquaria.cloud.moh.iais.service.LicenceFileDownloadService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Wenkang
 * @date 2019/11/9 16:05
 */
@Slf4j
@Delegator("licenceFileDownloadDelegator")
public class LicenceFileDownloadDelegator {
    private  List<ApplicationDto> list=new ArrayList<>();
    private List<ApplicationDto> requestForInfList=new ArrayList<>();
    @Autowired
    private TaskService taskService;
    @Autowired
    private BroadcastService broadcastService;
    @Autowired
    private LicenceFileDownloadService licenceFileDownloadService;
    @Autowired
    private ApplicationService applicationService;

    public  void start (BaseProcessClass bpc){
        list.clear();
        requestForInfList.clear();
        logAbout("start");

    }

    public  void prepareData(BaseProcessClass bpc) throws Exception {
         logAbout("preparetionData");
        licenceFileDownloadService.delete();
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto("INTRANET");

             licenceFileDownloadService.compress(list,requestForInfList);

           /*  licenceFileDownloadService.requestForInfList(requestForInfList);*/

            for(ApplicationDto applicationDto:list) {
                applicationDto.setAuditTrailDto(intranet);
            }
            //event bus update the data for new applicaiton
            TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(list,HcsaConsts.ROUTING_STAGE_ASO,RoleConsts.USER_ROLE_ASO,intranet);
            //for reqeust for information
            TaskHistoryDto requestTaskHistoryDto  = getRoutingTaskForRequestForInformation(requestForInfList,intranet);
            //
            if(taskHistoryDto!=null || requestTaskHistoryDto!=null){
                BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
                BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();
                broadcastOrganizationDto.setAuditTrailDto(intranet);
                broadcastApplicationDto.setAuditTrailDto(intranet);
                String eventRefNo = EventBusHelper.getEventRefNo();
                broadcastOrganizationDto.setEventRefNo(eventRefNo);
                broadcastApplicationDto.setEventRefNo(eventRefNo);

                List<TaskDto> onSubmitTaskList = new ArrayList<>();
                List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = new ArrayList<>();
                if(taskHistoryDto!=null){
                    if(!IaisCommonUtils.isEmpty(taskHistoryDto.getTaskDtoList())){
                        onSubmitTaskList.addAll(taskHistoryDto.getTaskDtoList());
                    }
                    if(!IaisCommonUtils.isEmpty(taskHistoryDto.getAppPremisesRoutingHistoryDtos())){
                        appPremisesRoutingHistoryDtos.addAll(taskHistoryDto.getAppPremisesRoutingHistoryDtos());
                    }
                }
                if(requestTaskHistoryDto!=null){
                    if(!IaisCommonUtils.isEmpty(requestTaskHistoryDto.getTaskDtoList())){
                        onSubmitTaskList.addAll(requestTaskHistoryDto.getTaskDtoList());
                    }
                    if(!IaisCommonUtils.isEmpty(requestTaskHistoryDto.getAppPremisesRoutingHistoryDtos())){
                        appPremisesRoutingHistoryDtos.addAll(requestTaskHistoryDto.getAppPremisesRoutingHistoryDtos());
                    }
                    broadcastApplicationDto.setApplicationDtos(requestTaskHistoryDto.getApplicationDtos());
                    broadcastApplicationDto.setRollBackApplicationDtos(requestTaskHistoryDto.getRollBackApplicationDtos());
                }
                broadcastOrganizationDto.setOneSubmitTaskList(onSubmitTaskList);
                broadcastApplicationDto.setOneSubmitTaskHistoryList(appPremisesRoutingHistoryDtos);
                broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,null);
                broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,null);

            }

    }
/*******************************/
    private void logAbout(String name){
        log.debug(StringUtil.changeForLog("****The***** " +name +" ******Start ****"));
    }

    private TaskHistoryDto getRoutingTaskForRequestForInformation(List<ApplicationDto> applicationDtos,AuditTrailDto auditTrailDto) throws Exception {
        log.debug(StringUtil.changeForLog("the do getRoutingTaskForRequestForInformation start ...."));
        TaskHistoryDto result = new TaskHistoryDto();
        List<TaskDto> taskDtoList = new ArrayList<>();
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = new ArrayList<>();
        List<ApplicationDto> newApplicationDtos = new ArrayList<>();
        List<ApplicationDto> rollBackApplicationDtos = new ArrayList<>();

        if(!IaisCommonUtils.isEmpty(applicationDtos)){
            log.debug(StringUtil.changeForLog("the applicationDtos size is-->"+applicationDtos.size()));
            List<RequestInformationSubmitDto> requestInformationSubmitDtos =  applicationService.getRequestInformationSubmitDtos(applicationDtos);
            if(!IaisCommonUtils.isEmpty(requestInformationSubmitDtos)){
               for(RequestInformationSubmitDto requestInformationSubmitDto : requestInformationSubmitDtos){
                   ApplicationDto applicationDto = requestInformationSubmitDto.getNewApplicationDto();
                   String appStatus = applicationDto.getStatus();
                   ApplicationDto oldApplicationDto = requestInformationSubmitDto.getOldApplicationDto();
                   AppPremisesRoutingHistoryDto reqeustAppPremisesRoutingHistoryDto = requestInformationSubmitDto.getReqeustAppPremisesRoutingHistoryDto();
                   List<AppPremisesCorrelationDto> oldAppPremisesCorrelationDtos =   requestInformationSubmitDto.getOldAppPremisesCorrelationDtos();
                   List<AppPremisesCorrelationDto> newAppPremisesCorrelationDtos =   requestInformationSubmitDto.getNewAppPremisesCorrelationDtos();
                   if(!IaisCommonUtils.isEmpty(oldAppPremisesCorrelationDtos)){
                     List<TaskDto> taskDtos =  licenceFileDownloadService.getTasksByRefNo(oldAppPremisesCorrelationDtos.get(0).getId());
                     if(!IaisCommonUtils.isEmpty(taskDtos)){
                         TaskDto taskDto = taskDtos.get(0);
                         TaskDto newTaskDto = TaskUtil.getUserTaskDto(taskDto.getTaskKey(),newAppPremisesCorrelationDtos.get(0).getId(),taskDto.getWkGrpId(),
                                 taskDto.getUserId(),0,taskDto.getProcessUrl(),taskDto.getRoleId(),auditTrailDto);
                         taskDtoList.add(newTaskDto);
                         //create history
                         AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto =
                                 createAppPremisesRoutingHistory(newAppPremisesCorrelationDtos.get(0).getId(),appStatus,
                                         taskDto.getTaskKey(),null,taskDto.getRoleId(),auditTrailDto);
                         appPremisesRoutingHistoryDtos.add(appPremisesRoutingHistoryDto);
                         rollBackApplicationDtos.add(applicationDto);
                         rollBackApplicationDtos.add(oldApplicationDto);
                         //
                         ApplicationDto newApplicationDto = (ApplicationDto)CopyUtil.copyMutableObject(applicationDto);
                         newApplicationDto.setStatus(reqeustAppPremisesRoutingHistoryDto.getAppStatus());
                         ApplicationDto oldApplicationDto1 = (ApplicationDto)CopyUtil.copyMutableObject(oldApplicationDto);
                         oldApplicationDto1.setStatus(ApplicationConsts.APPLICATION_STATUS_DELETED);
                         newApplicationDtos.add(newApplicationDto);
                         newApplicationDtos.add(oldApplicationDto1);
                         //
                         result.setTaskDtoList(taskDtoList);
                         result.setAppPremisesRoutingHistoryDtos(appPremisesRoutingHistoryDtos);
                         result.setApplicationDtos(applicationDtos);
                         result.setRollBackApplicationDtos(rollBackApplicationDtos);
                     }
                   }
               }
            }
        }else{
            log.debug(StringUtil.changeForLog("There are not reqest information application"));
        }
        log.debug(StringUtil.changeForLog("the do getRoutingTaskForRequestForInformation end ...."));
        return  result;
    }
    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,
                                                                         String stageId, String internalRemarks,String roleId,
                                                                         AuditTrailDto auditTrailDto){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(auditTrailDto.getMohUserGuid());
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setAuditTrailDto(auditTrailDto);
        return appPremisesRoutingHistoryDto;
    }
}
