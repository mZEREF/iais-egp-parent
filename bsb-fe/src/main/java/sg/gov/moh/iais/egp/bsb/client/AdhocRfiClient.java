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
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocRfiDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocRfiQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocRfiQueryResultDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocRfiViewDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApplicationDto;

/**
 * AdhocRfiClient
 *
 * @author junyu
 * @date 2022/4/18
 */
@FeignClient(value = "bsb-fe-api", configuration = FeignConfiguration.class)
public interface AdhocRfiClient {

    @GetMapping(value = "/adhoc-rfi", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AdhocRfiQueryResultDto> queryAdhocRfi(@SpringQueryMap AdhocRfiQueryDto adhocRfiQueryDto);

    @GetMapping(path = "/adhoc-rfi/adhocRfi/application/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApplicationDto> getApplicationDtoByAppId(@PathVariable("appId") String applicationId);

    @GetMapping(path = "/adhoc-rfi/adhocRfi/info/{adhocRfiId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AdhocRfiDto> getAdhocRfiById(@PathVariable("adhocRfiId") String id);

    @PostMapping(path = "/adhoc-rfi/adhocRfi", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AdhocRfiViewDto> saveAdhocRfi(@RequestBody AdhocRfiViewDto dto);
}
