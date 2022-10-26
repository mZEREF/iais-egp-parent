package sg.gov.moh.iais.egp.bsb.client;

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


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class)
public interface WithdrawnClient {
    @PostMapping(path = "/withdraw-be/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateWithdrawnDto(@RequestBody AppSubmitWithdrawnDto dto);

    @GetMapping(path = "/withdraw-be/{appId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppSubmitWithdrawnDto> getWithdrawnDataByApplicationId(@PathVariable("appId") String applicationId);

    @PostMapping(value = "/withdraw-be//do-accept", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> doAcceptWithdrawal(@RequestBody AppSubmitWithdrawnDto dto);

    @PostMapping(value = "/withdraw-be/do-reject", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> doRejectWithdrawal(@RequestBody AppSubmitWithdrawnDto dto);

    @PostMapping(value = "/rfi/withdrawal", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveWithdrawalRfi(@RequestBody AppSubmitWithdrawnDto dto);
}
