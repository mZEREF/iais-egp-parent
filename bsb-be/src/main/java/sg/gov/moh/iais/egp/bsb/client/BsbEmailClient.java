package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.entity.FacilityAdmin;

import java.util.List;

/**
 * AUTHOR: YiMing
 * DATE:2021/9/16 14:01
 **/

@FeignClient(name = "bsb-api", configuration = FeignConfiguration.class)
public interface BsbEmailClient {
    @GetMapping(path = "/fac_admin/{facId}")
    ResponseDto<List<FacilityAdmin>> queryEmailByFacId(@PathVariable("facId") String facId);

    @GetMapping(path = "/fac_admin/app/{appNo}")
    ResponseDto<List<FacilityAdmin>> queryFacilityAdminByAppNo(@PathVariable("appNo") String appNo );
}
