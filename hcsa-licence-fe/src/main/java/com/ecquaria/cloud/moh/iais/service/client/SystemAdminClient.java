package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2019/11/27 17:11
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class,
        fallback = SystemAdminClientFallback.class)
public interface SystemAdminClient  {
    @RequestMapping(path = "/draft-number/{type}",method = RequestMethod.GET)
    FeignResponseEntity<String> draftNumber(@PathVariable(name = "type")  String applicationType);

    @GetMapping(path = "/application-number")
    FeignResponseEntity<String> applicationNumber(@RequestParam(value = "type") String applicationType);

    @GetMapping(path = "/iais-messageTemplate/template/{msgId}")
    FeignResponseEntity<MsgTemplateDto> getMsgTemplate(@PathVariable("msgId") String id);

    @GetMapping(value = "/iais-mastercode/masterCode/{id}")
    FeignResponseEntity<MasterCodeDto> getMasterCodeById(@PathVariable("id") String id);

    @PutMapping(path = "/eicTracking",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, String>> saveEicTrack(@RequestBody List<EicRequestTrackingDto> dtoList);

    @RequestMapping(path = "/message-id",method = RequestMethod.GET)
    FeignResponseEntity<String> createMessageId();

    @GetMapping(path = "/eicTracking/{moduleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<EicRequestTrackingDto>> getPendingRecords(@PathVariable("moduleName") String moduleName);

    @GetMapping(path = "/eicTracking/{referenceNumber}/eic-ref-num", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<EicRequestTrackingDto> getByRefNum(@PathVariable("referenceNumber") String refNum);
}
