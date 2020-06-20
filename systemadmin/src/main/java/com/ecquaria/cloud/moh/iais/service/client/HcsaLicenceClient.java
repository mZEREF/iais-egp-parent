package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/12/4 16:02
 */
@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class,
        fallback = HcsaLicenceClientFallback.class)
public interface HcsaLicenceClient {
    @GetMapping(value = "/hcsa-key-personnel/getEmailByRole/{role}")
    FeignResponseEntity<List<String>> getEmailByRole(@PathVariable(name = "role") String role);

    @GetMapping(value = "/hcsa-key-personnel/getMobileByRole/{role}")
    FeignResponseEntity<List<String>> getMobileByRole(@PathVariable(name = "role") String role);

}
