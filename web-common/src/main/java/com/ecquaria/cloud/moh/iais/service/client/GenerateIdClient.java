package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;
import java.util.List;

/**
 * SystemParamClient
 *
 * @author Jinhua
 * @date 2019/11/23 16:15
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class,
        fallback = GenerateIdClientFallback.class)
public interface GenerateIdClient {
    @RequestMapping(path = "/system-parameter/new-sequence-id",method = RequestMethod.GET)
    FeignResponseEntity<String> getSeqId();
    @GetMapping(path = "/system-parameter/results", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<SystemParameterDto>> receiveAllSystemParam();

    @PostMapping(path = "/iais-messageTemplate/templates-param",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<MsgTemplateQueryDto>> getMsgTemplateResult(@RequestBody SearchParam param);

    @GetMapping(path = "/iais-messageTemplate/template/{msgId}")
    FeignResponseEntity<MsgTemplateDto> getMsgTemplate(@PathVariable("msgId") String id);

    @GetMapping(path = "/iais-messageTemplate/template/{msgId}/{recipient}")
    FeignResponseEntity<MsgTemplateDto> getMsgTemplate(@PathVariable("msgId") String id ,@PathVariable("recipient") String recipient);

    @PutMapping(path = "/iais-messageTemplate/template",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<MsgTemplateDto> updateMasterCode(@RequestBody MsgTemplateDto dto);

    @PostMapping(path = "/iais-jobmsg-track", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<JobRemindMsgTrackingDto>> createJobRemindMsgTracking(@RequestBody List<JobRemindMsgTrackingDto> jobRemindMsgTrackingDtoList);

    @GetMapping(value = "/iais-message/pendRefresh")
    FeignResponseEntity<List<MessageDto>> getMessagesToRefresh();

    @PutMapping(value = "/iais-message/messages", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> saveMessages(@RequestBody Collection<MessageDto> messageDtos);

}
