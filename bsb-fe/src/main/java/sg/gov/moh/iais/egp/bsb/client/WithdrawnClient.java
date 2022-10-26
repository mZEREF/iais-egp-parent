package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.WithdrawApplicationDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto;

import java.util.List;
import java.util.Map;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class, contextId = "withdrawn")
public interface WithdrawnClient {
    @PostMapping(path = "/withdraw/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateWithdrawnDto(@RequestBody AppSubmitWithdrawnDto dto);

    @GetMapping(path = "/withdraw/to-be-withdrawn-appId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppSubmitWithdrawnDto> getWithdrawnDataByApplicationId(@RequestParam("appId") String applicationId);

    @GetMapping(path = "/withdraw/withdrawn-appId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppSubmitWithdrawnDto> getRfiWithdrawnDataByWithdrawnAppId(@RequestParam("appId") String applicationId);

    @GetMapping(value = "/withdraw/can-withdraw-apps",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<WithdrawApplicationDto>> getApplicationByAppTypesAndStatus(@RequestBody Map<String,List<String>> appTypeAndStatusMap);

    @PostMapping(value = "/withdraw", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    void saveWithdrawnApp(@RequestBody AppSubmitWithdrawnDto dto);

    @PostMapping(value = "/rfi/withdrawal", produces = MediaType.APPLICATION_JSON_VALUE)
    void saveWithdrawalRfi(@RequestBody AppSubmitWithdrawnDto dto, @RequestParam("rfiDataId") String rfiDataId);
}
