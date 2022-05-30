package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.facilityapproval.FacApprovalInitDataDto;
import sg.gov.moh.iais.egp.bsb.dto.facilityapproval.FacApprovalProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

@FeignClient(name = "bsb-api", configuration = FeignConfiguration.class)
public interface FacilityApprovalClient {
    @GetMapping(path = "/facility-approval-be/actual/facility-approval-info/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacApprovalInitDataDto> getInitFacilityApprovalData(@PathVariable("appId") String appId);

    @PostMapping(path = "/facility-approval-be/form-validation/decision", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacApprovalProcessDto(@RequestBody FacApprovalProcessDto dto);

    @PostMapping(value = "/facility-approval-be/decision/approve",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> facApprovalApprove(@RequestBody FacApprovalProcessDto dto,@RequestParam("role") String role);

    @PostMapping(value = "/facility-approval-be/decision/reject",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> facApprovalReject(@RequestBody FacApprovalProcessDto dto, @RequestParam("role") String role);

    @PostMapping(value = "/facility-approval-be/decision/route-to-do",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> facApprovalRouteToDO(@RequestBody FacApprovalProcessDto dto);

    @PostMapping(value = "/facility-approval-be/decision/route-to-hm",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> facApprovalRouteToHM(@RequestBody FacApprovalProcessDto dto);
    }
