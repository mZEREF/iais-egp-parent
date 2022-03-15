package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.*;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.IncidentViewDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.InvestViewDto;


@FeignClient(value = "bsb-be-api", configuration = FeignClientsConfiguration.class, contextId = "incident")
public interface IncidentProcessClient {
    @PostMapping(path = "/incident-be/notification/do-process",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveDOProcess(@RequestBody ProcessingDto processingDto);

    @PostMapping(path = "/incident-be/notification/ao-process",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAOProcess(@RequestBody ProcessingDto processingDto);

    @PostMapping(path = "/incident-be/notification/hm-process",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveHMProcess(@RequestBody ProcessingDto processingDto);

    @PostMapping(path = "/incident-be/follow-up/note",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNoteFromMOHDO(@RequestBody FollowupProcessDto dto);

    @PostMapping(path = "/incident-be/follow-up/close",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> closeDOTask(@RequestParam("taskId") String taskId);

    @PostMapping(path = "/incident-be/investigation/do-process",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveDOInvestProcess(@RequestBody ProcessingDto processingDto);

    @PostMapping(path = "/incident-be/investigation/ao-process",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAOInvestProcess(@RequestBody ProcessingDto processingDto);

    @PostMapping(path = "/incident-be/investigation/hm-process",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveHMInvestProcess(@RequestBody ProcessingDto processingDto);

    @GetMapping(path = "/incident-be/notification/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<IncidentNotificationDto> getIncidentNotificationByAppId(@PathVariable("appId") String appId);

    @GetMapping(path = "/incident-be/investigation/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<IncidentNotificationDto> getInvestReportByAppId(@PathVariable("appId") String appId);

    @GetMapping(path = "/incident-be/follow-up/1A/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupViewDto> getFollowup1AByAppId(@PathVariable("appId") String appId);

    @GetMapping(path = "/incident-be/follow-up/1B/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupViewDto> getFollowup1BByAppId(@PathVariable("appId") String appId);

    @GetMapping(path = "/incident-be/view/follow-up/1A/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<Followup1AViewDto> getFollowup1AViewDtoByApplicationId(@PathVariable("appId") String appId);

    @GetMapping(path = "/incident-be/view/follow-up/1B/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<Followup1BViewDto> getFollowup1BViewDtoByApplicationId(@PathVariable("appId") String appId);

    @GetMapping(path = "/incident-be/view/notification/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<IncidentViewDto> getIncidentViewDtoByApplicationId(@PathVariable("appId") String appId);

    @GetMapping(path = "/incident-be/view/investigation/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InvestViewDto> getInvestViewDtoByApplicationId(@PathVariable("appId") String appId);

    @PostMapping(path = "/incident-be/form-validation/decision", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateProcessingDto(@RequestBody ProcessingDto dto);
}
