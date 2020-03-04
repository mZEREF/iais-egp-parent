package com.ecquaria.cloud.moh.iais.service.client;

/*
 *author: yichen
 *date time:12/5/2019 8:09 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

public class SystemClientFallback implements SystemClient{

    @Override
    public FeignResponseEntity<SearchResult<SystemParameterQueryDto>> doQuery(SearchParam searchParam) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<SystemParameterDto> getParameterByRowguid(String rowguid) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<String> saveSystemParameter(SystemParameterDto dto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<MessageDto> saveMessage(MessageDto messageDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<SearchResult<MessageQueryDto>> queryMessage(SearchParam searchParam) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<MessageDto> getMessageByRowguid(String id) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    @Override
    public FeignResponseEntity<String> saveAllPostCode(List<PostCodeDto> postCodeDtos) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

}
