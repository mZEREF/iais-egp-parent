package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Shicheng
 * @date 2020/9/25 9:45
 **/
@JobHandler(value="syncServiceByEndJobHandler")
@Component
@Slf4j
public class SyncServiceByEndJobHandler extends MohJobHandler {

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Override
    public ReturnT<String> doExecute(String var1) {
        try {
            logAbout("SyncServiceByEndJobHandler");
            //get expire Service By End Date
            List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.getNeedInActiveServices(AppConsts.COMMON_STATUS_ACTIVE).getEntity();
            if(!IaisCommonUtils.isEmpty(hcsaServiceDtos)){//NOSONAR
                List<HcsaServiceDto> updateServiceList = IaisCommonUtils.genNewArrayList();
                for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
                    log.info(StringUtil.changeForLog("hcsaServiceDto Id = " + hcsaServiceDto.getId()));
                    JobLogger.log(StringUtil.changeForLog("hcsaServiceDto Id = " + hcsaServiceDto.getId()));
                    if(hcsaServiceDto != null){
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
                HcsaServiceCacheHelper.receiveServiceMapping();
            } else {
                log.info(StringUtil.changeForLog("hcsaServiceDtos is Null"));
                JobLogger.log(StringUtil.changeForLog("hcsaServiceDtos is Null"));
            }
            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }

    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The****" + methodName + "*****Start****"));
        JobLogger.log(StringUtil.changeForLog("****The****" + methodName + "*****Start****"));
    }
}
