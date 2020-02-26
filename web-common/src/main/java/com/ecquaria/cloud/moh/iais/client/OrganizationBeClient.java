package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author: yichen
 * @date time:2/26/2020 1:08 PM
 * @description:
 */
@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class,
		fallback = OrganizationBeClientFallback.class)
public interface OrganizationBeClient {

	@PostMapping(value = "/iais-licensee-be/user-account/email-address", consumes = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<List<String>> getEmailAddressListByLicenseeId(@RequestBody List<String> licenseeIdList);
}
