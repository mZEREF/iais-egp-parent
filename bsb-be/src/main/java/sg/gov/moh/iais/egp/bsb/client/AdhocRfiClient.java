package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocRfi.AdhocRfiQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocRfi.AdhocRfiQueryResultDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocRfi.NewAdhocRfiDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocRfi.ViewAdhocRfiDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

/**
 * @author Ren Fanghao
 */
@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface AdhocRfiClient {

    @GetMapping(value = "/adhoc-rfi", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AdhocRfiQueryResultDto> queryAdhocRfi(@SpringQueryMap AdhocRfiQueryDto adhocRfiQueryDto);

    @GetMapping(value = "/adhoc-rfi/pre-newRfi/{approvalId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<NewAdhocRfiDto> queryPreNewData(@PathVariable("approvalId") String approvalId);

    @PostMapping(path = "/adhoc-rfi/update",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAdhocRfiView(@RequestBody ViewAdhocRfiDto viewAdhocRfiDto);

    @PostMapping(path = "/adhoc-rfi/save",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAdhocRfi(@RequestBody NewAdhocRfiDto newAdhocRfiDto);

    @PostMapping(path = "/adhoc-rfi/validate/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateManualAdhocRfi(@RequestBody NewAdhocRfiDto newAdhocRfiDto);

    @PostMapping(path = "/adhoc-rfi/validate/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateManualAdhocRfiUpdate(@RequestBody ViewAdhocRfiDto viewAdhocRfiDto);

    @GetMapping(value = "/adhoc-rfi/pre-viewRfi/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ViewAdhocRfiDto> findAdhocRfiById(@PathVariable("id") String id);

}
