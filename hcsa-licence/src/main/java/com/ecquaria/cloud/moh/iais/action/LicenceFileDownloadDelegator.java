package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.LicenceFileDownloadService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

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
    private static  final  String URL_APPLICATION="iais-application:8883/iais-application/list-application-dto";
    public  void start (BaseProcessClass bpc){

        logAbout("start");

    }

    public  void prepareData(BaseProcessClass bpc) throws FeignException {
         logAbout("preparetionData");
        licenceFileDownloadService.compress();
        licenceFileDownloadService.download();
        List<ApplicationDto> applicationDtos = licenceFileDownloadService.listApplication();

      /*  taskService.routingAdminScranTask(applicationDtos);*/
    }
/*******************************/
    private void logAbout(String name){
        log.debug(StringUtil.changeForLog("****The***** " +name +" ******Start ****"));
    }
}
