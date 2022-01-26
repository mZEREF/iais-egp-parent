package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.processderegistration.AOProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.processderegistration.DOProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.processderegistration.HMProcessDto;

/**
 * @author : LiRan
 * @date : 2022/1/21
 */
@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface ProcessDeregistrationClient {
    @GetMapping(path = "/deregistration/getDOProcessData/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DOProcessDto> getDOProcessDataByAppId(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/deregistration/getAOProcessData/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AOProcessDto> getAOProcessDataByAppId(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/deregistration/getHMProcessData/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<HMProcessDto> getHMProcessDataByAppId(@PathVariable("applicationId") String applicationId);

    @PostMapping(path = "/deregistration/validate/doProcessDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDOProcessDto(@RequestBody DOProcessDto doProcessDto);

    @PostMapping(path = "/deregistration/validate/aoProcessDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAOProcessDto(@RequestBody AOProcessDto aoProcessDto);

    @PostMapping(path = "/deregistration/validate/hmProcessDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateHMProcessDto(@RequestBody HMProcessDto hmProcessDto);

    @PostMapping(path = "/deregistration/saveDOProcessDto",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveDOProcessDto(@RequestBody DOProcessDto doProcessDto);

    @PostMapping(path = "/deregistration/saveAOProcessDto",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAOProcessDto(@RequestBody AOProcessDto aoProcessDto);

    @PostMapping(path = "/deregistration/saveHMProcessDto",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveHMProcessDto(@RequestBody HMProcessDto hmProcessDto);
}
