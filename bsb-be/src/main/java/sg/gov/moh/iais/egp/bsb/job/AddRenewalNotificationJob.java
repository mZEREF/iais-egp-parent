package sg.gov.moh.iais.egp.bsb.job;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sg.gov.moh.iais.egp.bsb.client.RenewalNotificationClient;


@Slf4j
@Component
@JobHandler("addRenewalNotificationJob")
public class AddRenewalNotificationJob extends IJobHandler {
    private final RenewalNotificationClient renewalNotificationClient;

    @Autowired
    public AddRenewalNotificationJob(RenewalNotificationClient renewalNotificationClient) {
        this.renewalNotificationClient = renewalNotificationClient;
    }

    @Override
    public ReturnT<String> execute(String s){
        try {
            renewalNotificationClient.notificationAfterExpiryWithoutAction();
            renewalNotificationClient.reminderAfterExpiryWithoutAction();
            renewalNotificationClient.notificationBeforeExpiry();
            renewalNotificationClient.reminderBeforeExpiry();
            renewalNotificationClient.notificationBeforeApprovedDeferment();
            renewalNotificationClient.reminderBeforeApprovedDeferment();
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            return ReturnT.FAIL;
        }
    }
}
