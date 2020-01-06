package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.Date;
import java.util.List;

/**
 * @author: yichen
 * @date time:1/6/2020 2:35 PM
 * @description:
 */

public class ApplicationBeClientFallback implements ApplicationBeClient{
	@Override
	public FeignResponseEntity<List<Date>> getInspectionRecomInDateByCorreId(List<String> taskRefNum) {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}
}
