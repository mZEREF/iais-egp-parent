package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.role.Role;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;


@FeignClient(value = "rbac-service", configuration = FeignClientsConfiguration.class, fallback = EgpUserClientFallback.class)
public interface EgpUserClient {
    /* This method is copied from phase 1.
     * The PathVariable {map} is very strange, but I don't have time to research the API now */
    @GetMapping(path = "/api/v1/roles/{map}", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    List<Role> search(@RequestParam("map") Map<String, String> map);
}
