package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementListDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author guyin
 * @date 2019/12/28 10:45
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class, fallback = IntranetUserClientFallback.class)
public interface BlastManagementListClient {
    @RequestMapping(value = "/iais-emails/getBlastManagementList",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<BlastManagementListDto>> getBlastManagementList(SearchParam searchParam);

    @PostMapping(value = "/iais-emails/blastList",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<BlastManagementDto> saveBlastList(@RequestBody BlastManagementDto blastManagementDto);

    @DeleteMapping(value = "/iais-emails/blastList", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> deleteBlastList(@RequestBody List<String> list);

    @GetMapping(value = "/iais-emails/blastEditCheck/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> blastEditCheck(@PathVariable("id") String id);

    @GetMapping(value = "/iais-emails/blastList/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<BlastManagementDto> getBlastById(@PathVariable("id") String id);

    @GetMapping(value = "/iais-emails/blastByMsgId/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<BlastManagementDto> getBlastByMsgId(@PathVariable("id") String id);

    @PostMapping(value = "/iais-emails/getBlastBySendTime", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<BlastManagementDto>> getBlastBySendTime(@RequestParam("date") String date);

    @GetMapping(value = "/iais-emails/sendedBlast", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<BlastManagementDto>> getSendedBlast();

    @GetMapping(value = "/iais-emails/sendedSMS", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<BlastManagementDto>> getSendedSMS();

    @PostMapping(value = "/iais-emails/setActual", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> setActual(@RequestParam("id") String id);

    @PostMapping(value = "/iais-emails/setSchedule",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> setSchedule(@RequestBody BlastManagementDto blastManagementDto);

    @PostMapping(value = "/iais-emails/checkUse",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> checkUse(@RequestBody List<String> disList);

}
