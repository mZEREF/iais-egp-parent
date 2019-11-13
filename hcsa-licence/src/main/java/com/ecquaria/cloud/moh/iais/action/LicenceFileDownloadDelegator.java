package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.service.LicenceFileDownloadService;
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
    private LicenceFileDownloadService licenceFileDownloadService;

    public  void start (BaseProcessClass bpc){

        logAbout("start");

    }

    public  void prepareData(BaseProcessClass bpc){
         logAbout("preparetionData");
        licenceFileDownloadService.download();


    }
/*******************************/
    private void logAbout(String name){
        log.debug("************the ****"+name+"****start***********");
    }
}
