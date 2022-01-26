package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.*;
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

    @PostMapping(path = "/bsb-repoEvent/save/note",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNoteFromMOHDO(@RequestBody FollowupProcessDto dto);

    @PostMapping(path = "/bsb-repoEvent/close",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> closeDOTask(@RequestParam("taskId") String taskId);

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

    @GetMapping(path = "/bsb-repoEvent/followup1A/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupViewDto> getFollowup1AByAppId(@PathVariable("appId") String appId);

    @GetMapping(path = "/bsb-repoEvent/followup1B/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupViewDto> getFollowup1BByAppId(@PathVariable("appId") String appId);

    @GetMapping(path = "/bsb-repoEvent/view/followup1A/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<Followup1AViewDto> getFollowup1AViewDtoByApplicationId(@PathVariable("appId") String appId);

    @GetMapping(path = "/bsb-repoEvent/view/followup1B/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<Followup1BViewDto> getFollowup1BViewDtoByApplicationId(@PathVariable("appId") String appId);

    @GetMapping(path = "/bsb-repoEvent/view/notification/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<IncidentViewDto> getIncidentViewDtoByApplicationId(@PathVariable("appId") String appId);

    @GetMapping(path = "/bsb-repoEvent/view/invest/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InvestViewDto> getInvestViewDtoByApplicationId(@PathVariable("appId") String appId);

    @PostMapping(path = "/bsb-repoEvent/validate/process", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateProcessingDto(@RequestBody ProcessingDto dto);
}
