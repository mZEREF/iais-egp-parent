package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.processderegistration.AOProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.processderegistration.DOProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.processderegistration.HMProcessDto;


@FeignClient(name = "bsb-api", configuration = FeignConfiguration.class)
public interface ProcessDeregistrationClient {
    @GetMapping(path = "/deregister-cancel-be/do-process/init-data/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DOProcessDto> getDOProcessDataByAppId(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/deregister-cancel-be/ao-process/init-data/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AOProcessDto> getAOProcessDataByAppId(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/deregister-cancel-be/hm-process/init-data/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<HMProcessDto> getHMProcessDataByAppId(@PathVariable("applicationId") String applicationId);

    @PostMapping(path = "/deregister-cancel-be/do-process/form-validation/decision", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDOProcessDto(@RequestBody DOProcessDto doProcessDto);

    @PostMapping(path = "/deregister-cancel-be/ao-process/form-validation/decision", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAOProcessDto(@RequestBody AOProcessDto aoProcessDto);

    @PostMapping(path = "/deregister-cancel-be/hm-process/form-validation/decision", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateHMProcessDto(@RequestBody HMProcessDto hmProcessDto);

    @PostMapping(path = "/deregister-cancel-be/do-process",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveDOProcessDto(@RequestBody DOProcessDto doProcessDto);

    @PostMapping(path = "/deregister-cancel-be/ao-process",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAOProcessDto(@RequestBody AOProcessDto aoProcessDto);

    @PostMapping(path = "/deregister-cancel-be/hm-process",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveHMProcessDto(@RequestBody HMProcessDto hmProcessDto);
}
