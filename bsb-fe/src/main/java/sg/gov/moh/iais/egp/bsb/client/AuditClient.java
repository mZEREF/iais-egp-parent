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

@FeignClient(name = "bsb-fe-api", configuration = FeignConfiguration.class)
public interface AuditClient {

    @GetMapping(value = "/bsb-audit/getAllAudit", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AuditQueryResultDto> getAllAudit(@SpringQueryMap AuditQueryDto queryDto);

    @GetMapping(value = "/bsb-audit/getFacilityByApproval")
    FeignResponseEntity<Facility> getFacilityByApproval(@RequestBody Approval approval);

    @GetMapping(path = "/bsb-audit/{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacilityAudit> getFacilityAuditById(@PathVariable(name = "id") String id);

    @PostMapping(value = "/bsb-audit/specifyAndChangeAuditDt",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> specifyAndChangeAuditDt(@RequestBody FacilityAudit facilityAudit);

    @PostMapping(value = "/bsb-audit/saveSelfAuditReport", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacilityAuditApp> saveSelfAuditReport(@RequestBody FacilityAudit facilityAudit);

    @GetMapping(value = "/bsb-audit/facName")
    FeignResponseEntity<List<String>> queryDistinctFN();
}
