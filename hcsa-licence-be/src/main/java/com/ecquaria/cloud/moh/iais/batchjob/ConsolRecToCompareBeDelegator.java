package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.ConsolRecToCompareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * ConsolRecToCompareBeDelegator
 *
 * @author junyu
 * @date 2022/5/18
 */
@Slf4j
@Delegator("consolRecToCompareBeDelegator")
public class ConsolRecToCompareBeDelegator {
    @Autowired
    private ConsolRecToCompareService compareService;


    public  void start (BaseProcessClass bpc){
        logAbout("start");

    }

    public  void preparetionData(BaseProcessClass bpc) throws Exception {
        logAbout("preparetionData");
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        compareService.initPath();

        compareService.decompression();

    }

    public void jobStart() throws Exception {
        compareService.initPath();

        compareService.decompression();

    }

    /*******************************/
    private void logAbout(String name){
        log.debug(StringUtil.changeForLog("****The***** " +name +" ******Start ****"));
    }
}
