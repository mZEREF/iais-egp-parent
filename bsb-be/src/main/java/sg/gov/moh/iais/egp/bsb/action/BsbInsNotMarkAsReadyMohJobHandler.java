package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sg.gov.moh.iais.egp.bsb.client.AutoTaskClient;

@JobHandler(value="bsbInsNotMarkAsReadyMohJobHandler")
@Component
@Slf4j
@RequiredArgsConstructor
public class BsbInsNotMarkAsReadyMohJobHandler extends IJobHandler {
    private final AutoTaskClient autoTaskClient;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            autoTaskClient.doMohInsTaskNotMarkAsReady();
            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}
