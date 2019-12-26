package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.FeToBeRecFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * Batch Job
 * @author Shicheng
 * @date 2019/12/26 10:37
 **/
@Slf4j
@Delegator("inspecUserRecFeToBeDelegator")
public class InspecUserRecFeToBeDelegator {

    @Autowired
    private FeToBeRecFileService feToBeRecFileService;

    /**
     * StartStep: inspecUserRecFeToBeStatrt
     *
     * @param bpc
     * @throws
     */
    public void inspecUserRecFeToBeStatrt(BaseProcessClass bpc){
        logAbout("Fe Create Zip");
    }

    /**
     * StartStep: inspecUserRecFeToBePre
     *
     * @param bpc
     * @throws
     */
    public void inspecUserRecFeToBePre(BaseProcessClass bpc){
        logAbout("inspecUserRecFeToBePre");
        String data = feToBeRecFileService.getRecData();
        if(!(StringUtil.isEmpty(data))){
            List<ApplicationDto> applicationDtos = feToBeRecFileService.getDocFile();
            feToBeRecFileService.createDataTxt(data);
            feToBeRecFileService.compressFile();
            feToBeRecFileService.changeStatus(applicationDtos);
        }
    }

    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }
}
