package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.entity.Facility;

/**
 * @author YiMing
 */

@FeignClient(name = "bsb-api", configuration = FeignConfiguration.class)
public interface FacilityClient {
    @GetMapping(path = "/fac_info/email/{appNo}")
    ResponseDto<Facility> queryEmailByAppNo(@PathVariable("appNo") String appNo);
}
