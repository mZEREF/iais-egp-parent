package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.report.notification.*;

/**
 * @author YiMing
 * @version 2021/12/6 17:47
 **/

@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "incident")
public interface IncidentNotificationClient {
    @PostMapping(path = "/save/incidentNot", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewIncidentNotification(@RequestBody IncidentNotificationDto dto);

    @PostMapping(path = "/validate/incidentInfo", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateIncidentInfo(@RequestBody IncidentInfoDto dto);

    @PostMapping(path = "/validate/personInvolved", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePersonInvolvedInfo(@RequestBody PersonInvolvedInfoDto dto);

    @PostMapping(path = "/validate/personReport", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePersonReporting(@RequestBody PersonReportingDto dto);

    @PostMapping(path = "/validate/primaryDoc", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePrimaryDoc(@RequestBody PrimaryDocDto dto);
}
