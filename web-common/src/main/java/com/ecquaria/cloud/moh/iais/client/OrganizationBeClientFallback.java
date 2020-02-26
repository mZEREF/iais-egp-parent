package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * @author: yichen
 * @date time:2/26/2020 1:09 PM
 * @description:
 */

public class OrganizationBeClientFallback implements OrganizationBeClient {
	@Override
	public FeignResponseEntity<List<String>> getEmailAddressListByLicenseeId(List<String> licenseeIdList) {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}
}
