package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Wenkang
 * @date 2020/9/23 14:51
 */
@JobHandler(value="licenceFileDownloadDelegatorHander")
@Component
@Slf4j
public class LicenceFileDownloadDelegatorHander extends IJobHandler {
    @Autowired
    private LicenceFileDownloadDelegator licenceFileDownloadDelegator;


    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.debug(StringUtil.changeForLog("The licenceFileDownloadDelegatorHander is Start ..."));
        try {
            licenceFileDownloadDelegator.jobStart();
        }catch (Exception e){
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}
