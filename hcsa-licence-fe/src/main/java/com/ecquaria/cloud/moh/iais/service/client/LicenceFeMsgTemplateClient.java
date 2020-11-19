package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author HuaChong
 * @Date 2019-12-24
 */

@FeignClient(name = "system-admin", configuration = FeignConfiguration.class, fallback = LicenceFeMsgTemplateClientFallback.class)
public interface LicenceFeMsgTemplateClient {
    @PostMapping(path = "/iais-messageTemplate/templates-param",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<MsgTemplateQueryDto>> getMsgTemplateResult(@RequestBody SearchParam param);

    @GetMapping(path = "/iais-messageTemplate/template/{msgId}")
    FeignResponseEntity<MsgTemplateDto> getMsgTemplate(@PathVariable("msgId") String id);

    @PutMapping(path = "/iais-messageTemplate/template",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<MsgTemplateDto> updateMasterCode(@RequestBody MsgTemplateDto dto);
}
