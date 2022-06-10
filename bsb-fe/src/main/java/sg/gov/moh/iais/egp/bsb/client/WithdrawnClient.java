package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.WithdrawApplicationDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto;

import java.util.List;
import java.util.Map;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class, contextId = "withdrawn")
public interface WithdrawnClient {
    @PostMapping(path = "/withdraw/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateWithdrawnDto(@RequestBody AppSubmitWithdrawnDto dto);

    @GetMapping(path = "/withdraw/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppSubmitWithdrawnDto> getWithdrawnDataByApplicationId(@PathVariable("appId") String applicationId);

    @GetMapping(value = "/withdraw/can-withdraw-apps",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<WithdrawApplicationDto>> getApplicationByAppTypesAndStatus(@RequestBody Map<String,List<String>> appTypeAndStatusMap);

    @PostMapping(value = "/withdraw", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> saveWithdrawnApp(@RequestBody AppSubmitWithdrawnDto dto);
}
