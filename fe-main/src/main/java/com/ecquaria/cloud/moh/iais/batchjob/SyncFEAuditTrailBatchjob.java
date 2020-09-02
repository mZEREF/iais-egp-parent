package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.SyncAuditTrailRecordsService;
import com.ecquaria.cloud.moh.iais.service.client.AuditTrailMainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * SyncFEAuditTrailBatchjob
 *
 * @author junyu
 * @date 2020/4/16
 */
@Delegator("syncFEAuditTrailBatchjob")
@Slf4j
public class SyncFEAuditTrailBatchjob {
    @Autowired
    private SyncAuditTrailRecordsService syncAuditTrailRecordsService;
    @Autowired
    private AuditTrailMainClient auditTrailMainClient;

    public void start(BaseProcessClass bpc){
        log.info("-------------------   start --------------");
    }

    public void preDate(BaseProcessClass bpc)  {

        log.debug(StringUtil.changeForLog("The SyncFEAuditTrailBatchjob is start..." ));

        while (true){
            List<AuditTrailEntityDto> auditTrail = syncAuditTrailRecordsService.getAuditTrailsByMigrated1();
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
    }


}
