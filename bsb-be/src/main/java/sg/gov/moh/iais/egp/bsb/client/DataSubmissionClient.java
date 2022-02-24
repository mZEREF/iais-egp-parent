package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.datasubmission.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.datasubmission.MohReviewDataSubmissionDto;


@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface DataSubmissionClient {
    @GetMapping(path = "/data-submission-be/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<MohReviewDataSubmissionDto> getOfficerReviewDataByAppId(@PathVariable("appId") String applicationId);

    @PostMapping(path = "/data-submission-be/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateMohProcessDto(@RequestBody MohReviewDataSubmissionDto dto);

    @PostMapping(value = "/data-submission-be/do-process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> doProcessDataSubmission(@RequestBody MohProcessDto dto);

    @PostMapping(value = "/data-submission-be/ao-process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> aoProcessDataSubmission(@RequestBody MohProcessDto dto);
}
