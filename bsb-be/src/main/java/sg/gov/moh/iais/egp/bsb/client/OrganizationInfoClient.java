package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class)
public interface OrganizationInfoClient {
    @GetMapping(path = "/iais-licensee/licenseeDto/{uenNo}",produces = MediaType.APPLICATION_JSON_VALUE)
    LicenseeDto getLicenseeByUenNo(@PathVariable(name = "uenNo") String uenNo);
}
