package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.*;
import sg.gov.moh.iais.egp.bsb.entity.Biological;
import sg.gov.moh.iais.egp.bsb.entity.FacilityActivity;

import java.util.List;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/27 16:34
 * DESCRIPTION: TODO
 **/

@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface BiosafetyEnquiryClient {

    @GetMapping(path = "/fac_info/facName")
    FeignResponseEntity<List<String>> queryDistinctFN();

    @GetMapping(path = "/bio_info/bioName")
    FeignResponseEntity<List<String>> queryDistinctFA();

    @GetMapping(path = "/fac_info/approval")
    FeignResponseEntity<List<String>> queryDistinctApproval();

    @GetMapping(path = "/app_info/{applicationNo}")
    ResponseDto<ApplicationResultDto> queryApplicationByAppNo(@PathVariable(name = "applicationNo") String applicationNo);

    @GetMapping(path = "/fac_info/{facilityName}")
    ResponseDto<FacilityResultDto> queryFacilityByFacName(@PathVariable(name = "facilityName") String facilityName);

    @GetMapping(path = "/bio_info/{schedule}")
    FeignResponseEntity<List<BiologicalDto>> queryBiologicalBySchedule(@PathVariable(name = "schedule") String schedule);

    @GetMapping(value = "/app_info/app", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApplicationResultDto> getApp(@RequestBody  EnquiryDto dto);

    @GetMapping(value = "/fac_info/query/fac", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityResultDto> getFac(@RequestBody  EnquiryDto dto);

    @GetMapping(value = "/approval_info/app", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalResultDto> getApproval(@RequestBody  EnquiryDto dto);

    @GetMapping(value = "/afc_info/afc", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovedFacilityCerResultDto> getAFC(@RequestBody  EnquiryDto dto);

    @PostMapping(path ="/bio_info/biologicalIdList", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<List<Biological>> getBiologicalsById(@RequestBody List<String> biologicalIdList);

    @GetMapping(path ="/bio_info/name/{bioName}")
    ResponseDto<Biological> getBiologicalByName(@PathVariable(name = "bioName") String bioName);

    @GetMapping(path = "/bio_info/info/{biologicalId}")
    ResponseDto<Biological> getBiologicalById(@PathVariable(name = "biologicalId") String biologicalId);

    @GetMapping(value = "/bsb-facilityActivity/queryActivityByAppId")
    FeignResponseEntity<FacilityActivity> getFacilityActivityByApplicationId(@RequestParam("appId") String applicationId);

}
