package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author weilu
 * @date 2019/12/25 15:45
 */
@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class, fallback = IntranetUserClientFallback.class)
public interface IntranetUserClient {

    @PostMapping(value = "/iais-orguser-be/user-management", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> createOrgUserDto(@RequestBody OrgUserDto user);

    @PostMapping(value = "/iais-orguser-be/intranet-user-param", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<OrgUserQueryDto>> doQuery(@RequestBody SearchParam searchParam);

    @PutMapping(value = "/iais-orguser-be" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrgUserDto> updateOrgUserDto(@RequestBody OrgUserDto orgUserDto);

    @DeleteMapping(value = "/iais-orguser-be/{id}")
    FeignResponseEntity<Void> delOrgUser (@PathVariable("id")String id);

    @GetMapping(value = "/iais-orguser-be/users-account/{id}")
    FeignResponseEntity<OrgUserDto> findIntranetUserById(@PathVariable("id")String id);

    @PostMapping(value = "/iais-task//task/results", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<TaskDto>> getTaskListBySearchParam(@RequestBody SearchParam searchParam);

    @RequestMapping(value = "/iais-orguser-be/users-by-loginId/{user_id}",method = RequestMethod.GET,produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<OrgUserDto> retrieveOneOrgUserAccount(@PathVariable(value = "user_id") String user_id);

    @PostMapping(value = "/iais-workgroup/work-group/results", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<WorkingGroupQueryDto>> getWorkingGroupBySearchParam(@RequestBody SearchParam searchParam);
}
