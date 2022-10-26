package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.singpostAddress.SingpostAddressDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "system-admin", configuration = FeignConfiguration.class)
public interface AddressInfoClient {
    @GetMapping(value = "/iais-singpost-address/postal-code", produces = MediaType.APPLICATION_JSON_VALUE)
    SingpostAddressDto getAddressInfoByPostalCode(@RequestParam(name = "postalCode") String postalCode);
}
