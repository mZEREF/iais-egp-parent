package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
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

    public  void prepareData(BaseProcessClass bpc) throws Exception {
         logAbout("preparetionData");
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        licenceFileDownloadService.initPath();

        licenceFileDownloadService.decompression();

    }

    public void jobStart() throws Exception {
        licenceFileDownloadService.initPath();

        licenceFileDownloadService.decompression();

    }

/*******************************/
    private void logAbout(String name){
        log.debug(StringUtil.changeForLog("****The***** " +name +" ******Start ****"));
    }

}
