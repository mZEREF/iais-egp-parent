package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.service.BroadcastService;
import com.ecquaria.cloud.moh.iais.service.LicenceFileDownloadService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloudfeign.FeignException;
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
    @Autowired
    private TaskService taskService;

    @Autowired
    private BroadcastService broadcastService;

    @Autowired
    private LicenceFileDownloadService licenceFileDownloadService;
    public  void start (BaseProcessClass bpc){

        logAbout("start");

    }

    public  void prepareData(BaseProcessClass bpc) throws FeignException {
         logAbout("preparetionData");
        licenceFileDownloadService.delete();
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto("INTRANET");
        licenceFileDownloadService.compress();
        Boolean download = licenceFileDownloadService.download();
        if(download){
            List<ApplicationDto> applicationDtos = licenceFileDownloadService.listApplication();
            for(ApplicationDto applicationDto:applicationDtos){
                applicationDto.setAuditTrailDto(intranet);
            }
            //event bus update the data
            TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(applicationDtos,HcsaConsts.ROUTING_STAGE_ASO,RoleConsts.USER_ROLE_ASO,intranet);
            if(taskHistoryDto!=null){
                BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
                BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();
                broadcastOrganizationDto.setAuditTrailDto(intranet);
                broadcastApplicationDto.setAuditTrailDto(intranet);
                String eventRefNo = EventBusHelper.getEventRefNo();
                broadcastOrganizationDto.setEventRefNo(eventRefNo);
                broadcastApplicationDto.setEventRefNo(eventRefNo);
                broadcastOrganizationDto.setOneSubmitTaskList(taskHistoryDto.getTaskDtoList());
                broadcastApplicationDto.setOneSubmitTaskHistoryList(taskHistoryDto.getAppPremisesRoutingHistoryDtos());
                broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,null);
                broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,null);
            }

        }

    }
/*******************************/
    private void logAbout(String name){
        log.debug(StringUtil.changeForLog("****The***** " +name +" ******Start ****"));
    }
}
