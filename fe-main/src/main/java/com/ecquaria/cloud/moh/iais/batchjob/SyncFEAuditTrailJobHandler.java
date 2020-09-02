package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.service.SyncAuditTrailRecordsService;
import com.ecquaria.cloud.moh.iais.service.client.AuditTrailMainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * SyncFEAuditTrailJobHandler
 *
 * @author junyu
 * @date 2020/7/3
 */
@JobHandler(value="syncFEAuditTrailJobHandler")
@Component
@Slf4j
public class SyncFEAuditTrailJobHandler extends IJobHandler {

    @Autowired
    private SyncAuditTrailRecordsService syncAuditTrailRecordsService;
    @Autowired
    private AuditTrailMainClient auditTrailMainClient;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            while (true){
                List<AuditTrailEntityDto> auditTrail= syncAuditTrailRecordsService.getAuditTrailsByMigrated1();
                if (IaisCommonUtils.isEmpty(auditTrail)){
                    break;
                }

                log.info("------------------- getData  start --------------");
                String data = syncAuditTrailRecordsService.getData(auditTrail);
                log.info("------------------- getData  end --------------");
                syncAuditTrailRecordsService.saveFile(data);
                log.info("------------------- saveFile  end --------------");
                syncAuditTrailRecordsService.compressFile();
                for (AuditTrailEntityDto a:auditTrail
                ) {
                    a.setMigrated(2);
                }
                auditTrailMainClient.syucUpdateAuditTrail(auditTrail);
                log.info("------------------- compressFile  end --------------");
            }

            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}
