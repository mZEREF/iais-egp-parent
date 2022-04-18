package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocRfi.AdhocRfiQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocRfi.AdhocRfiQueryResultDto;

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
}
