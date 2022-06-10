package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "bsb-api", configuration = FeignConfiguration.class)
public interface AutoTaskClient {
    @PostMapping(value = "/auto/inspection/non-compliance/remind",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> doInspectionRemindUserDoNCTask();

    @PostMapping(value = "auto/inspection/readiness/not-mark-as-ready/remind",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void doInspectionRemindReadinessNotMarkAsReady();


    @PostMapping(value = "auto/inspection/afc/moh-no-response",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void doInspectionAFCDOOrAONotRespond();
}
