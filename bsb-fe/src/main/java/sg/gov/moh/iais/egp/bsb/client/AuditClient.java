package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditQueryResultDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.FacilitySubmitSelfAuditDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import java.util.List;


@FeignClient(name = "bsb-fe-api", configuration = FeignConfiguration.class, contextId = "audit")
public interface AuditClient {
    @GetMapping(value = "/audit", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AuditQueryResultDto> getAllAudit(@SpringQueryMap AuditQueryDto queryDto);

    @GetMapping(value = "/facility-info/approvalId")
    FeignResponseEntity<Facility> getFacilityByApproval(@RequestParam("approvalId") String approvalId,@RequestParam("processType") String processType);

    @PostMapping(value = "/audit/self-audit/date",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> specifyAndChangeAuditDt(@RequestBody FacilitySubmitSelfAuditDto dto);

    @GetMapping(value = "/facility-info/names")
    FeignResponseEntity<List<String>> queryDistinctFN();

    @GetMapping(value = "/audit/self-audit/{auditId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilitySubmitSelfAuditDto> getSelfAuditDataByAuditId(@PathVariable("auditId") String auditId);

    @PostMapping(value = "/audit/self-audit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> facilitySubmitSelfAudit(@RequestBody FacilitySubmitSelfAuditDto dto);

    @PostMapping(path = "/audit/form-validation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAuditDt(@RequestBody FacilitySubmitSelfAuditDto dto);

}
