package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.bsb.dto.ResponseDto;
import sg.gov.moh.iais.bsb.dto.enquiry.ApplicationInfoDto;
import sg.gov.moh.iais.bsb.dto.enquiry.BiologicalDto;
import sg.gov.moh.iais.bsb.dto.enquiry.FacilityInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.ApplicationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.EnquiryDto;

import java.util.List;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/27 16:34
 * DESCRIPTION: TODO
 **/

@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface BiosafetyEnquiryClient {

    @PostMapping(path = "/app_info/results", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ApplicationInfoDto>> queryAppInfo(@RequestBody SearchParam searchParam);

    @PostMapping(path = "/fac_info/results", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<FacilityInfoDto>> queryFacilityInfo(@RequestBody SearchParam searchParam);

    @GetMapping(path = "/fac_info/facName")
    FeignResponseEntity<List<String>> queryDistinctFN();

    @GetMapping(path = "/bio_info/bioName")
    FeignResponseEntity<List<String>> queryDistinctFA();

    @GetMapping(path = "/fac_info/approval")
    FeignResponseEntity<List<String>> queryDistinctApproval();

    @GetMapping(path = "/app_info/{applicationNo}")
    FeignResponseEntity<List<ApplicationInfoDto>> queryApplicationByAppNo(@PathVariable(name = "applicationNo") String applicationNo);

    @GetMapping(path = "/fac_info/{facilityName}")
    FeignResponseEntity<List<FacilityInfoDto>> queryFacilityByFacName(@PathVariable(name = "facilityName") String facilityName);

    @GetMapping(path = "/bio_info/{schedule}")
    FeignResponseEntity<List<BiologicalDto>> queryBiologicalBySchedule(@PathVariable(name = "schedule") String schedule);

    @GetMapping(value = "/app_info/app", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApplicationResultDto> getApp(@SpringQueryMap EnquiryDto dto);
}
