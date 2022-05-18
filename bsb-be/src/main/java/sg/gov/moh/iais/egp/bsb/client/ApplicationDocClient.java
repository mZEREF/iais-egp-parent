package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;

import java.util.List;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class)
public interface ApplicationDocClient {
    @GetMapping(value = "/application-doc/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<DocDisplayDto> getApplicationDocForDisplay(@PathVariable("appId") String appId);
}
