package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto;

/**
 * @author tangtang
 **/
@FeignClient(value = "bsb-be-api", configuration = FeignClientsConfiguration.class)
public interface WithdrawnClient {
    @PostMapping(path = "/bsbWithdrawnBE/validate/withdrawnApp", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateWithdrawnDto(@RequestBody AppSubmitWithdrawnDto dto);

    @GetMapping(path = "/bsbWithdrawnBE/application/{appId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppSubmitWithdrawnDto> getWithdrawnDataByApplicationId(@PathVariable("appId") String applicationId);

    @PostMapping(value = "/bsbWithdrawnBE/doProcessWithdrawnApp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> doProcessWithdrawnApp(@RequestBody AppSubmitWithdrawnDto dto);

    @PostMapping(value = "/bsbWithdrawnBE/aoProcessWithdrawnApp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> aoProcessWithdrawnApp(@RequestBody AppSubmitWithdrawnDto dto);
}
