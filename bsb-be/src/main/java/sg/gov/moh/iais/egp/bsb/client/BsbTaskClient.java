package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.multiassign.MultiAssignInsDto;
import sg.gov.moh.iais.egp.bsb.dto.task.MultiTaskAssignDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskAssignDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskDetailDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchResultDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskReassignDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

import java.util.List;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class)
public interface BsbTaskClient {
    @GetMapping(value = "/task", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<TaskListSearchResultDto> getTaskList(@SpringQueryMap TaskListSearchDto dto);

    @GetMapping(value = "/task/pool", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<TaskListSearchResultDto> getTaskPool(@SpringQueryMap TaskListSearchDto dto);

    @GetMapping(value = "/task/detail", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<TaskDetailDto> getTaskDetail(@RequestParam("appId") String appId);

    @PutMapping(value = "/task/assign", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> assignTask(@RequestBody TaskAssignDto dto);

    @PutMapping(value = "/task/reassign", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> reassignTask(@RequestBody TaskReassignDto dto);

    @PutMapping(value = "/task/assign/multi", produces = MediaType.APPLICATION_JSON_VALUE)
    MultiTaskAssignDto assignMultiTasks(@RequestBody List<TaskAssignDto> taskAssignDtoList);

    @GetMapping(value = "/inspection-task/pool", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<TaskListSearchResultDto> searchInspectionTaskPool(@SpringQueryMap TaskListSearchDto searchDto);

    @GetMapping(path = "/inspection-task/multi-assign/init-data", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<MultiAssignInsDto> getMultiAssignDataByAppId(@RequestParam("appId") String applicationId);

    @PostMapping(path = "/inspection-task/multi-assign/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateMultiAssignInsDto(@RequestBody MultiAssignInsDto dto);

    @PostMapping(value = "/inspection-task/multi-assign", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> multiAssignTask(@RequestBody MultiAssignInsDto dto);
}
