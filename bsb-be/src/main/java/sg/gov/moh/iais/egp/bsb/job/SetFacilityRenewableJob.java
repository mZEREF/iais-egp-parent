package sg.gov.moh.iais.egp.bsb.job;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sg.gov.moh.iais.egp.bsb.client.AutoRenewalClient;


@Slf4j
@Component
@JobHandler("setFacilityRenewableJob")
public class SetFacilityRenewableJob extends IJobHandler {

    private final AutoRenewalClient autoRenewalClient;

    @Autowired
    public SetFacilityRenewableJob(AutoRenewalClient autoRenewalClient) {
        this.autoRenewalClient = autoRenewalClient;
    }

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try{
            autoRenewalClient.setFacilitiesRenewable();
            return ReturnT.SUCCESS;
        }catch (Exception e){
            return ReturnT.FAIL;
        }
    }
}
