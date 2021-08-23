package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.process.DoScreeningDto;
import sg.gov.moh.iais.egp.bsb.entity.Application;

/**
 * @author : LiRan
 * @date : 2021/8/20
 */
@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface ProcessClient {

    @GetMapping(path = "/bsb-MohOfficer/AOScreening/{applicationId}")
    FeignResponseEntity<Application> getApplicationById(@PathVariable(name = "applicationId") String applicationId);

    @PostMapping(path = "/bsb-MohOfficer/AOScreening/DoScreeningDto",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DoScreeningDto> updateFacilityByMohProcess(@RequestBody DoScreeningDto dto);
}
