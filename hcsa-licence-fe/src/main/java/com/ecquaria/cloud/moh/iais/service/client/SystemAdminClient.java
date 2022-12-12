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
    @GetMapping(path = "/draft-number/{type}")
    FeignResponseEntity<String> draftNumber(@PathVariable(name = "type")  String applicationType);

    @GetMapping(path = "/submission-id/{type}")
    FeignResponseEntity<String> submissionID(@PathVariable(name = "type")  String submissionType);

    @GetMapping(path = "/application-number")
    FeignResponseEntity<String> applicationNumber(@RequestParam(value = "type") String applicationType);

    @GetMapping(path = "/iais-messageTemplate/template/{msgId}")
    FeignResponseEntity<MsgTemplateDto> getMsgTemplate(@PathVariable("msgId") String id);

    @GetMapping(value = "/iais-mastercode/masterCode/{id}")
    FeignResponseEntity<MasterCodeDto> getMasterCodeById(@PathVariable("id") String id);

    @PutMapping(path = "/eicTracking",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, String>> saveEicTrack(@RequestBody List<EicRequestTrackingDto> dtoList);

    @GetMapping(path = "/message-id")
    FeignResponseEntity<String> createMessageId();

    @GetMapping(path = "/eicTracking/{moduleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<EicRequestTrackingDto>> getPendingRecords(@PathVariable("moduleName") String moduleName);

    @GetMapping(path = "/eicTracking/{referenceNumber}/eic-ref-num", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<EicRequestTrackingDto> getByRefNum(@PathVariable("referenceNumber") String refNum);

    @GetMapping(value = "/iais-messageTemplate/template-tocc/{msgId}")
    FeignResponseEntity<List<String>> getMsgTemplateReceiptToCc(@PathVariable("msgId") String id);
}
