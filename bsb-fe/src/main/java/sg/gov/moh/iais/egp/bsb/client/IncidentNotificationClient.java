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
import sg.gov.moh.iais.egp.bsb.dto.report.BiologicalInfo;
import sg.gov.moh.iais.egp.bsb.dto.report.FacilityInfo;
import sg.gov.moh.iais.egp.bsb.dto.report.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.report.notification.*;
import sg.gov.moh.iais.egp.bsb.dto.report.notification.view.IncidentViewDto;

import java.util.List;
import java.util.Map;


@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "incident")
public interface IncidentNotificationClient {
    @PostMapping(path = "/incident/notification/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftIncidentNotification(@RequestBody IncidentNotificationDto dto);

    @PostMapping(path = "/incident/notification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewIncidentNotification(@RequestBody IncidentNotificationDto dto);

    @GetMapping(path = "/incident/notification/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<IncidentNotificationDto> retrieveIncidentNotByReferenceId(@PathVariable("id") String refId);

    @GetMapping(path = "/incident/notification/view/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<IncidentViewDto> findIncidentViewDtoByIncidentId(@PathVariable("id") String incidentId);

    @PostMapping(path = "/incident/notification/form-validation/incident-info", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateIncidentInfo(@RequestBody IncidentInfoDto dto);

    @PostMapping(path = "/incident/notification/form-validation/person-reporting-info", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePersonInvolvedInfo(@RequestBody PersonInvolvedInfoDto dto);

    @PostMapping(path = "/incident/notification/form-validation/involved-person", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePersonReporting(@RequestBody PersonReportingDto dto);

    @PostMapping(path = "/incident/notification/form-validation/docs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePrimaryDoc(@RequestBody PrimaryDocDto.DocsMetaDto dto);

    @GetMapping(path = "/incident/notification/query/{facId}", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, List<BiologicalInfo>> queryFacilityActivityFacIdMap(@PathVariable("facId") String facId);

    @GetMapping(path = "/incident/notification/query/fac", produces = MediaType.APPLICATION_JSON_VALUE)
    List<FacilityInfo> queryDistinctFacilityName();
}
