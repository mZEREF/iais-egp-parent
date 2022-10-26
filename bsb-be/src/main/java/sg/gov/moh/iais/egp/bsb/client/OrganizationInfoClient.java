package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class)
public interface OrganizationInfoClient {
    @GetMapping(value = "/iais-licensee-be/licenseeDto-by-uenNo/{uenNo}")
    FeignResponseEntity<List<LicenseeDto>> getLicenseeDtoByUen(@PathVariable(name = "uenNo") String uenNo);
}
