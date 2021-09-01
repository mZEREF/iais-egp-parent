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
import sg.gov.moh.iais.egp.bsb.entity.Facility;
import sg.gov.moh.iais.egp.bsb.entity.FacilityAudit;

/*
 *author: Zhu Tangtang
 */

@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface AuditClient {

    @GetMapping(value = "/bsb-audit/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AuditQueryResultDto> doQuery(@SpringQueryMap AuditQueryDto queryDto);

    @GetMapping(path = "/bsb-audit/{id}}")
    FeignResponseEntity<Facility> getFacilityById(@PathVariable(name = "id") String id);

    @PostMapping(path = "/bsb-audit/save",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacilityAudit> saveFacilityAudit(@RequestBody FacilityAudit facilityAudit);

}
