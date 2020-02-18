package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.FeToBeRecFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Batch Job
 * @Process MohInspecUserRecFeToBe
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
        AuditTrailDto internet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTERNET);
        String data = feToBeRecFileService.getRecData();
        if(!(StringUtil.isEmpty(data))){
            Map<List<Map<String, String>>, List<ApplicationDto>> appItemMap = feToBeRecFileService.getDocFile();
            List<ApplicationDto> applicationDtos = new ArrayList<>();
            List<Map<String, String>> appIdItemIds = new ArrayList<>();
            for(Map.Entry<List<Map<String, String>>, List<ApplicationDto>> entry : appItemMap.entrySet()){
                appIdItemIds = entry.getKey();
                applicationDtos = entry.getValue();
            }
            Map<String, String> appIdItemIdMap = getAppIdItemIdMap(appIdItemIds);
            feToBeRecFileService.createDataTxt(data);
            feToBeRecFileService.compressFile(appIdItemIdMap);
            feToBeRecFileService.changeStatus(applicationDtos, internet);
        }
    }

    private Map<String, String> getAppIdItemIdMap(List<Map<String, String>> appIdItemIds) {
        Map<String, String> appIdItemIdMap = new HashMap<>();
        for(Map<String, String> map:appIdItemIds){
            for(Map.Entry<String, String> entry : map.entrySet()){
                appIdItemIdMap.put(entry.getKey(), entry.getValue());
            }
        }
        return appIdItemIdMap;
    }

    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }
}
