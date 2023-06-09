package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.*;

/**
 * @author Zhu Tangtang
 */
@FeignClient(name = "bsb-api", configuration = FeignConfiguration.class, contextId = "auditBE")
public interface AuditClientBE {

    @GetMapping(value = "/bsb-audit/facility", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityQueryResultDto> queryFacility(@SpringQueryMap AuditQueryDto queryDto);

    @PostMapping(path = "/bsb-audit/save",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveFacilityAudit(@RequestBody SaveAuditDto auditDto);

    @GetMapping(value = "/bsb-audit/{applicationId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<OfficerProcessAuditDto> getOfficerProcessDataByAppId(@PathVariable("applicationId") String appId);

    @PostMapping(value = "/bsb-audit/officer-process/auditDt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> officerProcessAuditDt(@RequestBody OfficerProcessAuditDto dto);

    @PostMapping(value = "/bsb-audit/officer-Process/self-audit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> officerProcessSelfAudit(@RequestBody OfficerProcessAuditDto dto);

    @PostMapping(value = "/bsb-audit/ao-process/cancel-audit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> officerCancelAudit(@RequestBody OfficerProcessAuditDto dto);

    @PostMapping(value = "/bsb-audit/do-process/cancel-audit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> doCancelAudit(@RequestBody CancelAuditDto dto);

    @PostMapping(path = "/bsb-audit/validate/manual", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateManualAudit(@RequestBody SaveAuditDto dto);

    @PostMapping(path = "/bsb-audit/validate/auditDt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateOfficerAuditDt(@RequestBody OfficerProcessAuditDto dto);

    @PostMapping(path = "/bsb-audit/validate/doCancel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDOCancelAudit(@RequestBody CancelAuditDto dto);
}
