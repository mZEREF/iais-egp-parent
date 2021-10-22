package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.approvalApp.ActivityDto;
import sg.gov.moh.iais.egp.bsb.dto.approvalApp.ApprovalAppDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PreviewSubmitDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.entity.Facility;
import sg.gov.moh.iais.egp.bsb.entity.FacilityActivity;

import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/10/13
 */
@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "approvalApp")
public interface ApprovalAppClient {

    @PostMapping(path = "/approvalApp/approvalToPossess/validate/activity", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateActivity(@RequestBody ActivityDto dto);

    @PostMapping(path = "/approvalApp/approvalToPossess/validate/primaryDocs", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityPrimaryDocs(@RequestBody PrimaryDocDto dto);

    @PostMapping(path = "/approvalApp/approvalToPossess/validate/previewSubmit", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityPreviewSubmit(@RequestBody PreviewSubmitDto dto);

    @GetMapping(path = "/approvalApp/approvalToPossess/application/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalAppDto> getApprovalAppAppData(@PathVariable("appId") String appId);

    @GetMapping(path = "/approvalApp/approvalToPossess/getAllApprovalFac", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<Facility>> getAllMainActApprovalFac();

    @GetMapping(path = "/approvalApp/approvalToPossess/getApprovalFAByFacId/{facilityId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<FacilityActivity>> getApprovalFAByFacId(@RequestParam("facilityId") String facilityId);
}
