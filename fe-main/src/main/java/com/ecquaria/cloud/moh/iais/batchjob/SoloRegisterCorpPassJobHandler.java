package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import com.ecquaria.cloud.moh.iais.service.client.FeMainMsgTemplateClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * SoloRegisterCorpPassJobHandler
 *
 * @author guyin
 * @date 2020/7/30 16:53
 */
@JobHandler(value="soloRegisterCorpPassJobHandler")
@Component
@Slf4j
public class SoloRegisterCorpPassJobHandler extends IJobHandler {

    @Autowired
    OrgUserManageService orgUserManageService;
    @Autowired
    FeMainMsgTemplateClient feMainMsgTemplateClient;
    @Override
    public ReturnT<String> execute(String s) {
        try{
            log.info("<====== solo register corPass start======>");
            //get licensee
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            orgUserManageService.sendReminderForExpiredSingPass();
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return ReturnT.FAIL;
        }

        return ReturnT.SUCCESS;
    }

}
