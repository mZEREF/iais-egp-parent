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

/**
 * @author YiMing
 * @version 2021/12/6 17:47
 **/

@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "incident")
public interface IncidentNotificationClient {
    @PostMapping(path = "/incident/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftIncidentNotification(@RequestBody IncidentNotificationDto dto);

    @PostMapping(path = "/incident/save/incidentNot", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewIncidentNotification(@RequestBody IncidentNotificationDto dto);

    @PostMapping(path = "/incident/validate/incidentInfo", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateIncidentInfo(@RequestBody IncidentInfoDto dto);

    @PostMapping(path = "/incident/validate/personInvolved", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePersonInvolvedInfo(@RequestBody PersonInvolvedInfoDto dto);

    @PostMapping(path = "/incident/validate/personReport", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePersonReporting(@RequestBody PersonReportingDto dto);

    @PostMapping(path = "/incident/validate/primaryDoc", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePrimaryDoc(@RequestBody PrimaryDocDto.DocsMetaDto dto);

    @GetMapping(path = "/incident/query/{facId}", produces =MediaType.APPLICATION_JSON_VALUE)
    Map<String, List<BiologicalInfo>> queryFacilityActivityFacIdMap(@PathVariable("facId") String facId);

    @GetMapping(path = "/incident/query/fac", produces =MediaType.APPLICATION_JSON_VALUE)
    List<FacilityInfo> queryDistinctFacilityName();

    @GetMapping(path="/incident/query/not/{refId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<IncidentNotificationDto> retrieveIncidentNotByReferenceId(@PathVariable("refId") String refId);

    @GetMapping(path = "/incident/view/notification/{incidentId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<IncidentViewDto> findIncidentViewDtoByIncidentId(@PathVariable("incidentId") String incidentId);
}
