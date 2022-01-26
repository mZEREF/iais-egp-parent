package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sg.gov.moh.iais.egp.bsb.service.AutoSuspendService;

/**
 * @author tangtang
 * @date 2022/1/4 17:04
 */
@JobHandler(value="autoSuspendDelegatorHandler")
@Component
@Slf4j
public class AutoSuspendReinstateDelegator extends IJobHandler {
    private final AutoSuspendService autoSuspendService;

    public AutoSuspendReinstateDelegator(AutoSuspendService autoSuspendService) {
        this.autoSuspendService = autoSuspendService;
    }

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            autoSuspendService.startSuspend();
        }catch (Exception e){
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}
