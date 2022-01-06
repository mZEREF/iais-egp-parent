package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.IncidentNotificationDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.ProcessingDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.IncidentViewDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.InvestViewDto;

/**
 * @author YiMing
 * @version 2021/12/30 16:03
 **/

@FeignClient(value = "bsb-be-api", configuration = FeignClientsConfiguration.class, contextId = "incident")
public interface IncidentProcessClient {
    @PostMapping(path = "/bsb-repoEvent/incidentDO",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveDOProcess(@RequestBody ProcessingDto processingDto);

    @PostMapping(path = "/bsb-repoEvent/incidentAO",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAOProcess(@RequestBody ProcessingDto processingDto);

    @PostMapping(path = "/bsb-repoEvent/incidentHM",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveHMProcess(@RequestBody ProcessingDto processingDto);

    @PostMapping(path = "/bsb-repoEvent/investDO",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveDOInvestProcess(@RequestBody ProcessingDto processingDto);

    @PostMapping(path = "/bsb-repoEvent/investAO",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAOInvestProcess(@RequestBody ProcessingDto processingDto);

    @PostMapping(path = "/bsb-repoEvent/investHM",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveHMInvestProcess(@RequestBody ProcessingDto processingDto);

    @GetMapping(path = "/bsb-repoEvent/notification/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<IncidentNotificationDto> getIncidentNotificationByAppId(@PathVariable("appId") String appId);

    @GetMapping(path = "/bsb-repoEvent/invest/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<IncidentNotificationDto> getInvestReportByAppId(@PathVariable("appId") String appId);

    @GetMapping(path = "/bsb-repoEvent/view/notification/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<IncidentViewDto> getIncidentViewDtoByApplicationId(@PathVariable("appId") String appId);

    @GetMapping(path = "/bsb-repoEvent/view/invest/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InvestViewDto> getInvestViewDtoByApplicationId(@PathVariable("appId") String appId);

    @PostMapping(path = "/bsb-repoEvent/validate/process", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateProcessingDto(@RequestBody ProcessingDto dto);
}
