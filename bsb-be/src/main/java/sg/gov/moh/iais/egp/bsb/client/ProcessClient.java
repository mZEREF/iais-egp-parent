package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.process.DoScreeningDto;
import sg.gov.moh.iais.egp.bsb.entity.Application;
import sg.gov.moh.iais.egp.bsb.entity.Biological;
import sg.gov.moh.iais.egp.bsb.entity.RoutingHistory;

import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/8/20
 */
@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface ProcessClient {

    @GetMapping(path = "/bsb_MohOfficer/AOScreening/{applicationId}")
    FeignResponseEntity<Application> getApplicationById(@PathVariable(name = "applicationId") String applicationId);

    @PostMapping(path = "/bsb_MohOfficer/AOScreening/DoScreeningDto",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DoScreeningDto> updateFacilityByMohProcess(@RequestBody DoScreeningDto doScreeningDto);

    @GetMapping(path = "/bio_info/info/{biologicalId}")
    ResponseDto<Biological> getBiologicalById(@PathVariable(name = "biologicalId") String biologicalId);

    @GetMapping(path = "/app_info/applicationNo")
    FeignResponseEntity<String> getAndCreateApplicationNo();

    @GetMapping(path = "/bsb_MohOfficer/routingHistory/{applicationNo}")
    FeignResponseEntity<List<RoutingHistory>> getRoutingHistoriesByApplicationNo(@RequestParam(name = "applicationNo") String applicationNo);
}
