package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sg.gov.moh.iais.egp.bsb.client.AutoTaskClient;


@JobHandler(value="autoInspectionDelegatorHandler")
@Component
@Slf4j
public class AutoInspectionDelegatorHandler extends IJobHandler {
    private final AutoTaskClient autoTaskClient;

    public AutoInspectionDelegatorHandler(AutoTaskClient autoTaskClient) {
        this.autoTaskClient = autoTaskClient;
    }

    @Override
    public ReturnT<String> execute(String s){
        try {
            autoTaskClient.doInspectionRemindUserDoNCTask();
            autoTaskClient.doInspectionRemindReadinessNotMarkAsReady();
            autoTaskClient.doInspectionAFCDOOrAONotRespond();
        }catch (Exception e){
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}
