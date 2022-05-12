package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "batInfo")
public interface BatClient {
    @GetMapping(value = "/bat-info/name")
    String queryBatName(@RequestParam(name = "code") String code);
}
