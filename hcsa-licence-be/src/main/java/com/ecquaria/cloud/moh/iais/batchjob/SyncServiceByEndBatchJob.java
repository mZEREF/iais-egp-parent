package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Shicheng
 * @date 2020/9/25 9:45
 **/
@Delegator("syncServiceByEndBatchJob")
@Slf4j
public class SyncServiceByEndBatchJob {

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private SyncServiceByEndJobHandler syncServiceByEndJobHandler;

    /**
     * StartStep: syncServiceByEndStart
     *
     * @param bpc
     * @throws
     */
    public void syncServiceByEndStart(BaseProcessClass bpc){
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        logAbout("Synchronize Service Status By End Date");
    }

    /**
     * StartStep: syncServiceByEndDo
     *
     * @param bpc
     * @throws
     */
    public void syncServiceByEndDo(BaseProcessClass bpc){
        logAbout("syncServiceByEndBatchJob");
        syncServiceByEndJobHandler.doExecute("serviceConfig");
        //get expire Service By End Date
        /*List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.getNeedInActiveServices(AppConsts.COMMON_STATUS_ACTIVE).getEntity();
        if(!IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            List<HcsaServiceDto> updateServiceList = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
                if(hcsaServiceDto != null){
                    log.info(StringUtil.changeForLog("hcsaServiceDto03 Id = " + hcsaServiceDto.getId()));
                    try {
                        hcsaServiceDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                        updateServiceList.add(hcsaServiceDto);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        JobLogger.log(e);
                        continue;
                    }
                }
            }
            hcsaConfigClient.saveServiceList(updateServiceList);
            HcsaServiceCacheHelper.flushServiceMapping();
        } else {
            log.info(StringUtil.changeForLog("hcsaServiceDtos is Null"));
        }
        List<HcsaServiceDto> hcsaServiceDtoList = hcsaConfigClient.getNeedActiveServices(AppConsts.COMMON_STATUS_IACTIVE).getEntity();
        if(!IaisCommonUtils.isEmpty(hcsaServiceDtoList)){
            List<HcsaServiceDto> updateServiceList = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtoList){
                if(hcsaServiceDto != null){
                    log.info(StringUtil.changeForLog("hcsaServiceDto01 Id = " + hcsaServiceDto.getId()));
                    try {
                        hcsaServiceDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                        updateServiceList.add(hcsaServiceDto);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        JobLogger.log(e);
                        continue;
                    }
                }
            }
            hcsaConfigClient.saveServiceList(updateServiceList);
            HcsaServiceCacheHelper.flushServiceMapping();
        } else {
            log.info(StringUtil.changeForLog("hcsaServiceDtoList is Null"));
        }*/
    }

    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The****" + methodName + "*****Start****"));
    }
}
