package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.BiologicalDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.ConsumeNotificationDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.FacListDto;

import java.util.List;

/**
 * @author Zhu Tangtang
 */

@FeignClient(name = "bsb-fe-api", configuration = FeignConfiguration.class, contextId = "dataSubmission")
public interface DataSubmissionClient {
    @GetMapping(path = "/dataSubmission/{schedule}")
    FeignResponseEntity<List<BiologicalDto>> queryBiologicalBySchedule(@PathVariable(name = "schedule") String schedule);

    @GetMapping(value = "/facList/getFacList", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacListDto> queryAllApprovalFacList();

    @PostMapping(value = "/dataSubmission/saveConsumeNot", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveConsumeNot(@RequestBody ConsumeNotificationDto notificationDto);

    @PostMapping(path = "/dataSubmission/validate/conNot", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateConsumeNot(@RequestBody ConsumeNotificationDto dto);
}
