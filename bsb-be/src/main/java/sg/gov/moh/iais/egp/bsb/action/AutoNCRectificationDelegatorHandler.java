package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sg.gov.moh.iais.egp.bsb.client.AutoTaskClient;


@JobHandler(value="autoNCRectificationDelegatorHandler")
@Component
@Slf4j
public class AutoNCRectificationDelegatorHandler extends IJobHandler {
    private final AutoTaskClient autoTaskClient;

    public AutoNCRectificationDelegatorHandler(AutoTaskClient autoTaskClient) {
        this.autoTaskClient = autoTaskClient;
    }

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            autoTaskClient.doInspectionRemindUserDoNCTask();
        }catch (Exception e){
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}
