package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.SyncAuditTrailRecordsService;
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

    public void start(BaseProcessClass bpc){

    }

    public void preDate(BaseProcessClass bpc)  {

        log.debug(StringUtil.changeForLog("The SyncFEAuditTrailBatchjob is start..." ));
        List<AuditTrailEntityDto> auditTrailDtos= syncAuditTrailRecordsService.getAuditTrailsByMigrated1();
        log.info("------------------- getData  start --------------");
        String data = syncAuditTrailRecordsService.getData(auditTrailDtos);
        log.info("------------------- getData  end --------------");
        syncAuditTrailRecordsService.saveFile(data);
        log.info("------------------- saveFile  end --------------");
        syncAuditTrailRecordsService.compressFile();
        log.info("------------------- compressFile  end --------------");

    }
}
