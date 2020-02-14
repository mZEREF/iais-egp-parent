package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
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

        licenceFileDownloadService.compress(list, requestForInfList);

        /*  licenceFileDownloadService.requestForInfList(requestForInfList);*/


            //event bus update the data for new applicaiton
          /*  TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(list,HcsaConsts.ROUTING_STAGE_ASO,RoleConsts.USER_ROLE_ASO,intranet);
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

            }*/

    }
/*******************************/
    private void logAbout(String name){
        log.debug(StringUtil.changeForLog("****The***** " +name +" ******Start ****"));
    }

}
