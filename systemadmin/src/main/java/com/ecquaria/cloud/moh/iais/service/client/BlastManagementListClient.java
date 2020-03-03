package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementListDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author guyin
 * @date 2019/12/28 10:45
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class, fallback = IntranetUserClientFallback.class)
public interface BlastManagementListClient {
    @PostMapping(value = "/iais-emails/getBlastManagementList",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<BlastManagementListDto>> getBlastManagementList(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/iais-emails/saveBlastList",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<BlastManagementDto> saveBlastList(@RequestBody BlastManagementDto blastManagementDto);

    @PostMapping(value = "/iais-emails/deleteBlastList", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> deleteBlastList(@RequestBody List<String> list);

    @PostMapping(value = "/iais-emails/getBlastById", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<BlastManagementDto> getBlastById(@RequestParam("id") String id);

    @PostMapping(value = "/iais-emails/setSchedule",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> setSchedule(@RequestBody BlastManagementDto blastManagementDto);

}
