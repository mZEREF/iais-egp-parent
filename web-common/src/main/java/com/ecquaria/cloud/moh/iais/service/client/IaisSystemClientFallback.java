package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateQueryDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.http.HttpHeaders;

/**
 * @author: yichen
 * @date time:2/8/2020 12:07 PM
 * @description:
 */

public class IaisSystemClientFallback implements IaisSystemClient {

	@Override
	public FeignResponseEntity<List<SystemParameterDto>> receiveAllSystemParam() {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}

	@Override
	public FeignResponseEntity<SearchResult<MsgTemplateQueryDto>> getMsgTemplateResult(SearchParam param) {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}

	@Override
	public FeignResponseEntity<MsgTemplateDto> getMsgTemplate(String id) {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}

	@Override
	public FeignResponseEntity<MsgTemplateDto> updateMasterCode(MsgTemplateDto dto) {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}

	@Override
	public FeignResponseEntity<List<JobRemindMsgTrackingDto>> createJobRemindMsgTracking(List<JobRemindMsgTrackingDto> jobRemindMsgTrackingDtoList) {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}
}
