package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditQueryResultDto;
import sg.gov.moh.iais.egp.bsb.entity.*;

import java.util.List;

/**
 * @author Zhu Tangtang
 */

@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface AuditClient {

    @GetMapping(value = "/bsb-audit/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AuditQueryResultDto> doQuery(@SpringQueryMap AuditQueryDto queryDto);

    @GetMapping(value = "/bsb-audit/getAllAudit", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AuditQueryResultDto> getAllAudit(@SpringQueryMap AuditQueryDto queryDto);

    @GetMapping(value = "/bsb-audit/getCancelAuditList", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AuditQueryResultDto> getCancelAuditList(@SpringQueryMap AuditQueryDto queryDto);

    @GetMapping(path = "/bsb-audit/getFacilityById")
    FeignResponseEntity<Facility> getFacilityById(@RequestParam("id") String id);

    @PostMapping(path = "/bsb-audit/save",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacilityAudit> saveFacilityAudit(@RequestBody FacilityAudit facilityAudit);

    @GetMapping(path = "/bsb-audit/{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacilityAudit> getFacilityAuditById(@PathVariable(name = "id") String id);

    @PostMapping(value = "/bsb-audit/specifyAndChangeAuditDt",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> specifyAndChangeAuditDt(@RequestBody FacilityAudit facilityAudit);

    @GetMapping(value = "/bsb-audit/getFacilityAuditAppById",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacilityAuditApp> getFacilityAuditAppById(@RequestParam("id") String id);

    @PostMapping(value = "/bsb-audit/updateAuditApp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> processAuditDate(@RequestBody FacilityAuditApp auditApp);

    @PostMapping(value = "/bsb-audit/saveSelfAuditReport", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacilityAuditApp> saveSelfAuditReport(@RequestBody FacilityAudit facilityAudit);

    @PostMapping(value = "/bsb-auditHistory/saveHistory", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacilityAuditAppHistory> saveHistory(@RequestBody FacilityAuditAppHistory history);

    @GetMapping(value = "/bsb-auditHistory/getAllHistory", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<FacilityAuditAppHistory>> getAllHistory();

    @GetMapping(value = "/bsb-auditHistory/getAllHistoryByAuditAppId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<FacilityAuditAppHistory>> getAllHistoryByAuditAppId(@RequestParam("auditAppId") String auditAppId);

    @GetMapping(value = "/bsb-facilityActivity/queryActivityByFacId")
    FeignResponseEntity<List<FacilityActivity>> getFacilityActivityByFacilityId(@RequestParam("facId") String facilityId);

    @GetMapping(path = "/fac_info/facName")
    FeignResponseEntity<List<String>> queryDistinctFN();
}
