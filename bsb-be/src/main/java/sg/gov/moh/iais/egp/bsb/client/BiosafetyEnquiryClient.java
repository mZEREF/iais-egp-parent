package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
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

    @GetMapping(path = "/bat-info/basic", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<BatBasicInfo>> queryBiologicalBySchedule(@RequestParam(name = "schedule") String schedule);

    @GetMapping(value = "/online-enquiry/application", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppResultPageInfoDto> getApplication(@SpringQueryMap AppSearchDto dto);

    @GetMapping(value = "/online-enquiry/facility", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacResultPageInfoDto> getFacility(@SpringQueryMap  FacSearchDto dto);

    @GetMapping(value = "/online-enquiry/approval", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalResultDto> getApproval(@SpringQueryMap ApprovalSearchDto dto);

    @GetMapping(value = "/online-enquiry/afc", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AFCResultPageInfoDto> getApprovedFacilityCertifier(@SpringQueryMap  AFCSearchDto dto);

    @GetMapping(path = "/bat-info/{id}")
    BiologicalDto getBiologicalById(@PathVariable(name = "id") String biologicalId);

    @GetMapping(value = "/bsb-facilityActivity/queryActivityByAppId")
    FeignResponseEntity<FacilityActivity> getFacilityActivityByApplicationId(@RequestParam("appId") String applicationId);

}
