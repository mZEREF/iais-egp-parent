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
import sg.gov.moh.iais.egp.bsb.dto.adhocrfi.AdhocRfiQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocrfi.AdhocRfiQueryResultDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocrfi.AdhocRfiViewDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

/**
 * AdhocRfiClient
 *
 * @author junyu
 * @date 2022/4/18
 */
@FeignClient(value = "bsb-api", configuration = FeignConfiguration.class)
public interface AdhocRfiClient {

    @GetMapping(value = "/adhoc-rfi", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AdhocRfiQueryResultDto> queryAdhocRfi(@SpringQueryMap AdhocRfiQueryDto adhocRfiQueryDto);

    @GetMapping(path = "/adhoc-rfi/adhocRfi/info/{adhocRfiId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AdhocRfiViewDto> getAdhocRfiById(@PathVariable("adhocRfiId") String id);

    @PostMapping(path = "/adhoc-rfi/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAdhocRfiViewDto(@RequestBody AdhocRfiViewDto dto);

    @PostMapping(path = "/adhoc-rfi/adhocRfi", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    void saveAdhocRfi(@RequestBody AdhocRfiViewDto dto);
}
