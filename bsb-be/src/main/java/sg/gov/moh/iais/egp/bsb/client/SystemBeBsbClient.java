package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author : LiRan
 * @date : 2021/12/1
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class)
public interface SystemBeBsbClient {
    @GetMapping(value = "/system-parameter/{id}")
    FeignResponseEntity<SystemParameterDto> getParameterByRowguid(@PathVariable(name = "id") String rowguid);
}
