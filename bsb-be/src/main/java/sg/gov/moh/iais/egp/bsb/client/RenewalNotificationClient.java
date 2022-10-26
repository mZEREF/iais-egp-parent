package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class)
public interface RenewalNotificationClient {

    //010
    @PostMapping(value = "/renewalNotification/notificationAfterExpiryWithoutAction")
    void notificationAfterExpiryWithoutAction();

    //011
    @PostMapping(value = "/renewalNotification/reminderAfterExpiryWithoutAction")
    void reminderAfterExpiryWithoutAction();

    //012
    @PostMapping(value = "/renewalNotification/notificationBeforeExpiry")
    void notificationBeforeExpiry();

    //013
    @PostMapping(value = "/renewalNotification/reminderBeforeExpiry")
    void reminderBeforeExpiry();

    //014
    @PostMapping(value = "/renewalNotification/notificationBeforeApprovedDeferment")
    void notificationBeforeApprovedDeferment();

    //015
    @PostMapping(value = "/renewalNotification/reminderBeforeApprovedDeferment")
    void reminderBeforeApprovedDeferment();

}