package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoringExcel.MonitoringSheetsDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.ExcelMonitoringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * ConsolRecToCompareFeDelegator
 *
 * @author junyu
 * @date 2022/5/18
 */
@Slf4j
@Delegator("consolRecToCompareFeDelegator")
public class ConsolRecToCompareFeDelegator {

    @Autowired
    private ExcelMonitoringService excelMonitoringService;
    /*
     * start step
     * */
    public  void start(BaseProcessClass bpc){

        logAbout("start");

    }

    public void preparetionData (BaseProcessClass bpc){
        logAbout("preparetionData");
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        //get all data of need Carry from DB
        start();

    }

    public void jobStart(){
        start();
    }
    /**********************************/

    private void start(){

        log.info("------------------- getData  end --------------");
        //Parse the
        MonitoringSheetsDto parse = excelMonitoringService.parse();

        AuditTrailDto intenet = AuditTrailHelper.getCurrentAuditTrailDto();
        parse.setAuditTrailDto(intenet);

        String timeId = excelMonitoringService.saveFile(parse);
        
        log.info("------------------- saveFile  end --------------");
        String compressFileName = excelMonitoringService.compressFile(timeId);
        excelMonitoringService.renameAndSave(compressFileName,timeId);
        log.info("------------------- compressFile  end --------------");
    }
    private  void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " +methodName +" ******Start ****"));
    }
}
