package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
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
            taskService.routingTaskOneUserForSubmisison(applicationDtos,HcsaConsts.ROUTING_STAGE_ASO);
        }

    }
/*******************************/
    private void logAbout(String name){
        log.debug(StringUtil.changeForLog("****The***** " +name +" ******Start ****"));
    }
}
