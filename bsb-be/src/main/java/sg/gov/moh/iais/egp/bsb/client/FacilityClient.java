package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocInspection.AdhocInspectionSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocInspection.AdhocInspectionSearchResultDto;
import sg.gov.moh.iais.egp.bsb.dto.facilitymanagement.FacilityManagementSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.facilitymanagement.FacilityManagementSearchResultDto;
import sg.gov.moh.iais.egp.bsb.entity.Facility;

/**
 * @author YiMing
 */

@FeignClient(name = "bsb-api", configuration = FeignConfiguration.class)
public interface FacilityClient {
    @GetMapping(path = "/fac_info/email/{appNo}")
    ResponseDto<Facility> queryEmailByAppNo(@PathVariable("appNo") String appNo);

    @GetMapping(value = "/facility/management", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityManagementSearchResultDto> searchFacilityList(@SpringQueryMap FacilityManagementSearchDto searchDto);

    @GetMapping(value = "/inspection/adhoc/search", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AdhocInspectionSearchResultDto> searchFacilityList(@SpringQueryMap AdhocInspectionSearchDto searchDto);
}
