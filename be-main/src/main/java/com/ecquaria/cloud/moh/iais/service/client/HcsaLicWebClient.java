package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * HcsaLicWebClient
 *
 * @author Jinhua
 * @date 2022/6/1 9:48
 */
@FeignClient(name = "hcsa-licence-web", configuration = FeignConfiguration.class)
public interface HcsaLicWebClient {
    @PostMapping("/hcsa-licence-web/canApproveValidation")
    FeignResponseEntity<Map<String, String>> validateCanApprove(@RequestBody ApplicationViewDto applicationViewDto);
}
