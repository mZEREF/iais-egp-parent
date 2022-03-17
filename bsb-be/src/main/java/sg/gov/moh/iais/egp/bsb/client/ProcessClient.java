package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.process.*;

import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/8/20
 */
@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface ProcessClient {
    @GetMapping(path = "/bsbMohOfficer/getDOScreeningData/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DOScreeningDto> getDOScreeningDataByAppId(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/bsbMohOfficer/getAOScreeningData/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AOScreeningDto> getAOScreeningDataByAppId(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/bsbMohOfficer/getHMScreeningData/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<HMScreeningDto> getHMScreeningDataByAppId(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/bsbMohOfficer/getDOProcessingData/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DOProcessingDto> getDOProcessingDataByAppId(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/bsbMohOfficer/getAOProcessingData/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AOProcessingDto> getAOProcessingDataByAppId(@PathVariable("applicationId") String applicationId);

    @PostMapping(path = "/bsbMohOfficer/validate/doScreeningDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDOScreeningDto(@RequestBody DOScreeningDto doScreeningDto);

    @PostMapping(path = "/bsbMohOfficer/validate/aoScreeningDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAOScreeningDto(@RequestBody AOScreeningDto aoScreeningDto);

    @PostMapping(path = "/bsbMohOfficer/validate/hmScreeningDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateHMScreeningDto(@RequestBody HMScreeningDto hmScreeningDto);

    @PostMapping(path = "/bsbMohOfficer/validate/doProcessingDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDOProcessingDto(@RequestBody DOProcessingDto doProcessingDto);

    @PostMapping(path = "/bsbMohOfficer/validate/aoProcessingDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAOProcessingDto(@RequestBody AOProcessingDto aoProcessingDto);

    @PostMapping(path = "/bsbMohOfficer/saveDOScreening",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveDOScreening(@RequestBody DOScreeningDto doScreeningDto);

    @PostMapping(path = "/bsbMohOfficer/saveAOScreening",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAOScreening(@RequestBody AOScreeningDto aoScreeningDto);

    @PostMapping(path = "/bsbMohOfficer/saveHMScreening",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveHMScreening(@RequestBody HMScreeningDto hmScreeningDto);

    @PostMapping(path = "/bsbMohOfficer/saveDOProcessing",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveDOProcessing(@RequestBody DOProcessingDto doProcessingDto);

    @PostMapping(path = "/bsbMohOfficer/saveAOProcessing",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAOProcessing(@RequestBody AOProcessingDto aoProcessingDto);

    @GetMapping(path = "/bsbMohOfficer/getDifferentModuleDoc/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<DocDisplayDto> getDifferentModuleDoc(@PathVariable("applicationId") String applicationId);
}
