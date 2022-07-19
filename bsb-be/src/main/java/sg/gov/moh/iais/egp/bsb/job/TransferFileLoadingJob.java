package sg.gov.moh.iais.egp.bsb.job;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sg.gov.moh.iais.egp.bsb.client.FileSyncClient;

@Slf4j
@Component
@JobHandler("transferFileLoadingJob")
public class TransferFileLoadingJob extends IJobHandler {
    private final FileSyncClient fileSyncClient;

    @Autowired
    public TransferFileLoadingJob(FileSyncClient fileSyncClient) {
        this.fileSyncClient = fileSyncClient;
    }


    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            fileSyncClient.loadSyncZip();
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            return ReturnT.FAIL;
        }
    }
}
