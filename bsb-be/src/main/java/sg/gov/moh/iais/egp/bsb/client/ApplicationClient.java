package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppBasicInfo;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class, contextId = "application")
public interface ApplicationClient {
    @GetMapping(value = "/application/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    AppBasicInfo getAppBasicInfoById(@PathVariable("appId") String appId);
}
