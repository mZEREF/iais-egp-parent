package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;

import java.util.List;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class)
public interface AppSupportingDocClient {
    @GetMapping(path = "/bsb-moh-officer/supporting-doc", produces = MediaType.APPLICATION_JSON_VALUE)
    List<DocDisplayDto> getAppSupportingDocForProcessByAppId(@RequestParam("appId") String appId, @RequestParam("sort") String sort);
}
