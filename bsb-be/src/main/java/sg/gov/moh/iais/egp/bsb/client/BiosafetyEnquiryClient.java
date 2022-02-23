package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.*;
import sg.gov.moh.iais.egp.bsb.dto.entity.BiologicalDto;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.BatBasicInfo;
import sg.gov.moh.iais.egp.bsb.entity.FacilityActivity;

import java.util.List;


@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface BiosafetyEnquiryClient {

    @GetMapping(path = "/facility-info/names")
    FeignResponseEntity<List<String>> queryDistinctFN();

    @GetMapping(path = "/bat-info/names")
    FeignResponseEntity<List<String>> queryDistinctFA();

    @GetMapping(path = "/fac_info/orgName")
    FeignResponseEntity<List<String>> queryDistinctOrgName();

    @GetMapping(path = "/app_info/{applicationNo}")
    ResponseDto<ApplicationResultDto> queryApplicationByAppNo(@PathVariable(name = "applicationNo") String applicationNo);

    @GetMapping(path = "/bat-info/basic", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<BatBasicInfo>> queryBiologicalBySchedule(@RequestParam(name = "schedule") String schedule);

    @GetMapping(value = "/app_info/app", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApplicationResultDto> getApp(@RequestBody  EnquiryDto dto);

    @GetMapping(value = "/fac_info/query/fac", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityResultDto> getFac(@RequestBody  EnquiryDto dto);

    @GetMapping(value = "/approval_info/app", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalResultDto> getApproval(@RequestBody EnquiryDto dto);

    @GetMapping(value = "/afc_info/afc", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovedFacilityCerResultDto> getAFC(@RequestBody  EnquiryDto dto);

    @GetMapping(path = "/bat-info/{id}")
    BiologicalDto getBiologicalById(@PathVariable(name = "id") String biologicalId);

    @GetMapping(value = "/bsb-facilityActivity/queryActivityByAppId")
    FeignResponseEntity<FacilityActivity> getFacilityActivityByApplicationId(@RequestParam("appId") String applicationId);

}
