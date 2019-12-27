package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2019/11/28 10:28
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class,
        fallback = SystemClientBeLicFallback.class)
public interface SystemBeLicClient {
    @RequestMapping(path = "/file-existence",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> isFileExistence(@RequestBody Map<String,String> map);

    @GetMapping(value = "/file-type-status/{processType}/{status}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ProcessFileTrackDto>> getFileTypeAndStatus
            (@PathVariable(name = "processType") String processType, @PathVariable(name = "status") String status);

    @PutMapping(value = "/uprocessfiletrack", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ProcessFileTrackDto> updateApplication(@RequestBody ProcessFileTrackDto processFileTrackDto);

    @RequestMapping(path = "/hcl-code/{serviceCode}",method = RequestMethod.GET)
    FeignResponseEntity<String> hclCodeByCode(@PathVariable(name = "serviceCode") String code);

    @RequestMapping(path = "/licence-number",method = RequestMethod.GET)
    FeignResponseEntity<String> licence(@RequestParam("hciCode") String hciCode, @RequestParam("serviceCode") String serviceCode,
                                        @RequestParam("yearLength") Integer yearLength, @RequestParam("licenceSeq") Integer licenceSeq) ;
    @RequestMapping
    FeignResponseEntity<String> groupLicence(@RequestParam("hscaCode") String hscaCode,
                                             @RequestParam("yearLength") String yearLength,@RequestParam("licence") String licence);

    @PostMapping(value = "/iais-messageTemplate" ,consumes =  MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<InspectionEmailTemplateDto> loadingEmailTemplate(@RequestBody String id);

    @RequestMapping(path = "/message-id",method = RequestMethod.GET)
    FeignResponseEntity<String> messageID();

    @PostMapping(value = "/iais-jobmsg-track", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes =  MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<JobRemindMsgTrackingDto>> createJobRemindMsgTrackingDtos(@RequestBody List<JobRemindMsgTrackingDto> jobRemindMsgTrackingDtoList);

    @PutMapping(value = "/iais-jobmsg-track", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes =  MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<JobRemindMsgTrackingDto> updateJobRemindMsgTrackingDto(@RequestBody JobRemindMsgTrackingDto jobRemindMsgTrackingDto);
}
