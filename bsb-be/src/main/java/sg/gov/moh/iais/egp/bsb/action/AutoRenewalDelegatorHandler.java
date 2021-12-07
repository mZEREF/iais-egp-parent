package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sg.gov.moh.iais.egp.bsb.service.AutoRenewalService;

/**
 * @author : LiRan
 * @date : 2021/12/1
 */
@JobHandler(value="autoRenewalDelegatorHandler")
@Component
@Slf4j
public class AutoRenewalDelegatorHandler extends IJobHandler {
    private final AutoRenewalService autoRenewalService;

    public AutoRenewalDelegatorHandler(AutoRenewalService autoRenewalService) {
        this.autoRenewalService = autoRenewalService;
    }

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            autoRenewalService.startRenewal();
        }catch (Exception e){
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}
