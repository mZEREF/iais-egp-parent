package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;
import com.ecquaria.cloud.moh.iais.service.SyncAuditTrailRecordsService;
import com.ecquaria.cloud.moh.iais.service.client.AuditTrailMainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    class Task implements Runnable{
        @Override
        public void run() {
            try {
                List<AuditTrailEntityDto>  atList = syncAuditTrailRecordsService.getAuditTrailsByMigrated1();

                log.info("------------------- getData  start --------------");

                String data = syncAuditTrailRecordsService.getData(atList);

                log.info("------------------- getData  end --------------");

                try {
                    syncAuditTrailRecordsService.saveFile(data);

                    log.info("------------------- saveFile  end --------------");

                    syncAuditTrailRecordsService.compressFile();

                    for (AuditTrailEntityDto a: atList) {
                        a.setMigrated(2);
                    }

                    auditTrailMainClient.syucUpdateAuditTrail(atList);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
                log.info("------------------- compressFile  end --------------");
            } catch (CancellationException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void preDate(BaseProcessClass bpc) throws IOException {
        Integer threadCounter = 0;
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(5);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                10, 5000, TimeUnit.MILLISECONDS, blockingQueue);
        executor.setRejectedExecutionHandler((r, executor1) -> {
            log.info("Waiting for a second !!");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
               log.error(e.getMessage(), e);
            }

            executor1.execute(r);
        });

        executor.prestartAllCoreThreads();

        while (true){
            threadCounter++;
            log.info("Adding DemoTask : " + threadCounter);
            executor.execute(new Task());

            if (threadCounter == 10)
                break;
        }

    }
}
