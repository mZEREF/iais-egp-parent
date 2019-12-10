package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterQueryDto;
import com.ecquaria.cloud.moh.iais.entity.MessageCode;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/*
 *author: yichen
 *date time:12/5/2019 8:05 PM
 *description:
 */

@FeignClient(name = "system-admin", url = "http://system-admin:8886", configuration = FeignConfiguration.class,
        fallback = SystemClientFallback.class)
public interface SystemClient {

    @PostMapping(path = "/system-parameter/results", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<SystemParameterQueryDto>> doQuery(@RequestBody SearchParam searchParam);

    @GetMapping(path = "/system-parameter/{id}")
    FeignResponseEntity<SystemParameterDto> getParameterByRowguid(@PathVariable(name = "id") String rowguid);

    @PostMapping(path = "/system-parameter/")
    FeignResponseEntity<String> saveSystemParameter(@RequestBody SystemParameterDto dto);

    @PostMapping(path = "/iais-message", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveMessage(@RequestBody MessageDto messageDto);

    @PostMapping(path = "/iais-message/results", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<MessageQueryDto>> queryMessage(@RequestBody SearchParam searchParam);

    @PostMapping(path = "/iais-message/allMsg", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<MessageCode>> queryAllMsg(@RequestBody SearchParam searchParam);

    @GetMapping(path = "/iais-message/{id}")
    FeignResponseEntity<MessageDto> getMessageByRowguid(@PathVariable(name = "id") String id);

    @PostMapping(path = "/iais-audit-trail/results", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<AuditTrailQueryDto>> listAuditTrailDto(SearchParam searchParam);
}
