package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author yichen
 * @Date:2021/4/16
 */
@FeignClient(name = "halp-report", configuration = FeignConfiguration.class)
public interface HalpReportServerClient {
    @PostMapping(value = "/halp-sftp-elis/view-data/")
    FeignResponseEntity<Void> createElisData();

    @PostMapping(value = "/halp-sftp-HealthHub/view-data/")
    FeignResponseEntity<String> receive();
}
