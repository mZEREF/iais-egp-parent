package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * FileRepoClient
 *
 * @author Jinhua
 * @date 2019/11/26 12:29
 */
@FeignClient(name = "bsb-fe-api", configuration = FeignConfiguration.class)
public interface FeApplicationClient {
    @GetMapping(path = "/application/appNo")
    FeignResponseEntity<String> genApplicationNumber(@RequestParam("appType") String appType);
}
