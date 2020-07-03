package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RfiFileRepoDownloadJobHandler
 *
 * @author junyu
 * @date 2020/7/3
 */
@JobHandler(value="rfiFileRepoDownloadJobHandler")
@Component
@Slf4j
public class RfiFileRepoDownloadJobHandler extends IJobHandler {
    @Autowired
    private RequestForInformationService requestForInformationService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            requestForInformationService.delete();
            requestForInformationService.compress();

            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}
