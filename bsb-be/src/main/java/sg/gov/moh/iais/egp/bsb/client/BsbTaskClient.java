package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.multiassign.MultiAssignInsDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskAssignDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchResultDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskReassignDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

import java.util.List;
import java.util.Map;


@FeignClient(value = "bsb-be-api", configuration = FeignClientsConfiguration.class)
public interface BsbTaskClient {
    @GetMapping(value = "/task", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<TaskListSearchResultDto> getTaskList(@SpringQueryMap TaskListSearchDto dto);

    @GetMapping(value = "/task/pool", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<TaskListSearchResultDto> getTaskPool(@SpringQueryMap TaskListSearchDto dto);

    @PutMapping(value = "/task/assign", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> assignTask(@RequestParam("taskId") String taskId, @RequestParam("userId") String userId, @RequestParam("appId") String appId, @RequestParam("roleId")String curRoleId);

    @PutMapping(value = "/task/reassign", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> reassignTask(@RequestBody TaskReassignDto dto);

    @PutMapping(value = "/task/assign/multi", produces = MediaType.APPLICATION_JSON_VALUE)
    List<TaskAssignDto> assignMultiTasks(List<TaskAssignDto> taskAssignDtoList);

    @PostMapping(value = "/task/find/appNo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, String> findAppNoByTaskId(List<String> taskIdList);

    @GetMapping(value = "/inspection-task/pool", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<TaskListSearchResultDto> searchInspectionTaskPool(@SpringQueryMap TaskListSearchDto searchDto);

    @GetMapping(path = "/inspection-task/multi-assign/init-data", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<MultiAssignInsDto> getMultiAssignDataByAppId(@RequestParam("appId") String applicationId);

    @PostMapping(path = "/inspection-task/multi-assign/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateMultiAssignInsDto(@RequestBody MultiAssignInsDto dto);

    @PostMapping(value = "/inspection-task/multi-assign", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> multiAssignTask(@RequestBody MultiAssignInsDto dto);
}
