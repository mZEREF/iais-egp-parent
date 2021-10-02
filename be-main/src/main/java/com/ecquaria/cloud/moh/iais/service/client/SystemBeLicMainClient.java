package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * SystemBeLicMainClient
 *
 * @author junyu
 * @date 2020/4/16
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class,
        fallback = SystemBeLicMainClientFallback.class)
public interface SystemBeLicMainClient {
    @PostMapping(path = "/file-existence",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ProcessFileTrackDto> isFileExistence(@RequestBody Map<String,String> map);

    @GetMapping(value = "/file-type-status/{processType}/{status}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ProcessFileTrackDto>> getFileTypeAndStatus
            (@PathVariable(name = "processType") String processType, @PathVariable(name = "status") String status);

    @PutMapping(value = "/uprocessfiletrack", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ProcessFileTrackDto> updateProcessFileTrack(@RequestBody ProcessFileTrackDto processFileTrackDto);

    @GetMapping(path = "/hcl-code/{serviceCode}")
    FeignResponseEntity<String> hclCodeByCode(@PathVariable(name = "serviceCode") String code);

    @GetMapping(path = "/licence-number")
    FeignResponseEntity<String> licence(@RequestParam("hciCode") String hciCode, @RequestParam("serviceCode") String serviceCode,
                                        @RequestParam("yearLength") Integer yearLength, @RequestParam("licenceSeq") Integer licenceSeq) ;

    @PostMapping(value = "/iais-messageTemplate" ,consumes =  MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<InspectionEmailTemplateDto> loadingEmailTemplate(@RequestBody String id);

    @GetMapping(path = "/message-id")
    FeignResponseEntity<String> messageID();

    @PostMapping(value = "/iais-jobmsg-track", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes =  MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<JobRemindMsgTrackingDto>> createJobRemindMsgTrackingDtos(@RequestBody List<JobRemindMsgTrackingDto> jobRemindMsgTrackingDtoList);

    @PutMapping(value = "/iais-jobmsg-track", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes =  MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<JobRemindMsgTrackingDto> updateJobRemindMsgTrackingDto(@RequestBody JobRemindMsgTrackingDto jobRemindMsgTrackingDto);

    @PostMapping(value = "/data-mail-job" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> saveSendMailJob(@RequestBody JobRemindMsgTrackingDto jobRemindMsgTrackingDto);

    @GetMapping(value = "/list-job-msg-auto-renwal" ,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<JobRemindMsgTrackingDto>>  listJob();

    @GetMapping(value = "/iais-jobmsg-track/job-rem-msg-tra/{refNo}/{msgKey}")
    FeignResponseEntity<JobRemindMsgTrackingDto> getJobRemindMsgTrackingDto(@PathVariable(name = "refNo") String refNo, @PathVariable(name = "msgKey") String msgKey);

    @GetMapping(value = "/api-postcodes/init-cache", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> initCachePostCodes();
}
