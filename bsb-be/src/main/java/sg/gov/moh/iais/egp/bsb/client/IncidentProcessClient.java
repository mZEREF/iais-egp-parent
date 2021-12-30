package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.IncidentNotificationDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.ProcessingDto;

/**
 * @author YiMing
 * @version 2021/12/30 16:03
 **/

@FeignClient(value = "bsb-be-api", configuration = FeignClientsConfiguration.class, contextId = "incident")
public interface IncidentProcessClient {
    @PostMapping(path = "/bsb_repoEvent/processDO",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveDOProcess(@RequestBody ProcessingDto processingDto);

    @PostMapping(path = "/bsb_repoEvent/processAO",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAOProcess(@RequestBody ProcessingDto processingDto);

    @PostMapping(path = "/bsb_repoEvent/processHM",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveHMProcess(@RequestBody ProcessingDto processingDto);

    @GetMapping(path = "/bsb_repoEvent/notification/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<IncidentNotificationDto> getIncidentNotificationByAppId(@PathVariable("appId") String appId);
}
