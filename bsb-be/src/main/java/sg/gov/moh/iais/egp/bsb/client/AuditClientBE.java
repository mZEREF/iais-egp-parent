package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.FacilityQueryResultDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.OfficerProcessAuditDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.SaveAuditDto;
import sg.gov.moh.iais.egp.bsb.entity.*;

import java.util.List;

/**
 * @author Zhu Tangtang
 */
@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface AuditClientBE {

    @GetMapping(value = "/bsb-audit/queryFacility", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityQueryResultDto> queryFacility(@SpringQueryMap AuditQueryDto queryDto);

    @PostMapping(path = "/bsb-audit/save",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveFacilityAudit(@RequestBody List<SaveAuditDto> auditDtos);

    @GetMapping(path = "/bsb-audit/{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacilityAudit> getFacilityAuditById(@PathVariable(name = "id") String id);

    @GetMapping(value = "/bsb-audit/getFacilityAuditAppById",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacilityAuditApp> getFacilityAuditAppById(@RequestParam("id") String id);

    @PostMapping(value = "/bsb-audit/updateAudit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateAudit(@RequestBody FacilityAudit facilityAudit);

    @PostMapping(value = "/bsb-auditHistory/saveHistory", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacilityAuditAppHistory> saveHistory(@RequestBody FacilityAuditAppHistory history);

    @GetMapping(value = "/bsb-auditHistory/getAllHistory", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<FacilityAuditAppHistory>> getAllHistory();

    @GetMapping(value = "/bsb-auditHistory/getAllHistoryByAuditAppId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<FacilityAuditAppHistory>> getAllHistoryByAuditAppId(@RequestParam("auditAppId") String auditAppId);

    @GetMapping(value = "/bsb-facilityActivity/queryActivityByFacId")
    FeignResponseEntity<List<FacilityActivity>> getFacilityActivityByFacilityId(@RequestParam("facId") String facilityId);

    @GetMapping(value = "/bsb-audit/getOfficerProcessDataByAppId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<OfficerProcessAuditDto> getOfficerProcessDataByAppId(@RequestParam("appId") String appId);

    @PostMapping(value = "/bsb-audit/officerProcessAuditDt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> officerProcessAuditDt(@RequestBody OfficerProcessAuditDto dto);

    @PostMapping(value = "/bsb-audit/officerProcessSelfAudit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> officerProcessSelfAudit(@RequestBody OfficerProcessAuditDto dto);

    @PostMapping(value = "/bsb-audit/officerCancelAudit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> officerCancelAudit(@RequestBody OfficerProcessAuditDto dto);
}
