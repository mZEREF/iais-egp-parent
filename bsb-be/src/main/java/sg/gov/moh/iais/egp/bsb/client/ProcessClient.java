package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.process.SubmitDetailsDto;

/**
 * @author : LiRan
 * @date : 2021/8/20
 */
@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface ProcessClient {

    @PostMapping(path = "/bsb_MohOfficer/saveMohProcess",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveMohProcess(@RequestBody MohProcessDto mohProcessDto);

    @GetMapping(path = "/bsb_MohOfficer/submitDetails/{applicationId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<SubmitDetailsDto> getSubmitDetailsByAppId(@PathVariable("applicationId") String applicationId);

    @PostMapping(path = "/bsb_MohOfficer/validate/MohProcessDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateMohProcessDto(@RequestBody MohProcessDto mohProcessDto);

}
