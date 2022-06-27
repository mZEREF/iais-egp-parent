package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceFileDownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;


@Slf4j
@Delegator("downloadFileEditDelegator")
public class DownloadEditFileDelegator {

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
