package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/*
 *author: yichen
 *date time:12/5/2019 8:05 PM
 *description:
 */

@FeignClient(name = "system-admin", configuration = FeignConfiguration.class,
        fallback = SystemClientFallback.class)
public interface SystemClient {

    @PostMapping(path = "/system-parameter/results", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<SystemParameterQueryDto>> doQuery(@RequestBody SearchParam searchParam);

    @GetMapping(path = "/system-parameter/{id}")
    FeignResponseEntity<SystemParameterDto> getParameterByRowguid(@PathVariable(name = "id") String rowguid);

    @PostMapping(path = "/system-parameter/",  consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SystemParameterDto> saveSystemParameter(@RequestBody SystemParameterDto dto);

    @PostMapping(path = "/iais-message", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<MessageDto> saveMessage(@RequestBody MessageDto messageDto);

    @PostMapping(path = "/iais-message/results", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<MessageQueryDto>> queryMessage(@RequestBody SearchParam searchParam);

    @GetMapping(path = "/iais-message/{id}")
    FeignResponseEntity<MessageDto> getMessageByRowguid(@PathVariable(name = "id") String id);

    @PostMapping(path = "/api-postcodes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveAllPostCode(@RequestBody List<PostCodeDto> postCodeDtos);

    @RequestMapping(path = "/message-id",method = RequestMethod.GET)
    FeignResponseEntity<String> messageID();

    @GetMapping(path = "/iais-message/module-type", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> listModuleTypes();

}
