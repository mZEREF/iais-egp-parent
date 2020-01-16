package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/12/4 15:13
 */
public class OrganizationClientFallback {
    public FeignResponseEntity<List<OrgUserDto>> retrieveOrgUserAccount(List<String> ids) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    public FeignResponseEntity<List<TaskDto>> getSupervisorPoolByGroupWordId(String workGroupId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<List<OrgUserDto>> getUsersByWorkGroupName(String workGroupId, String status) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<TaskDto> getTaskById(String taskId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<WorkingGroupDto> createWorkGroup(WorkingGroupDto workingGroupDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<BroadcastOrganizationDto> createBroadcastOrganization(BroadcastOrganizationDto broadcastOrganizationDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<List<UserGroupCorrelationDto>> getUserGroupCorreByUserId(String userId){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<List<TaskDto>> getTasksByUserId(String userId){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<OrgUserDto> retrieveOneOrgUserAccount(String userId){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    public FeignResponseEntity<BroadcastOrganizationDto> getBroadcastOrganizationDto(String groupName,String groupDomain){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<InspecTaskCreAndAssDto> assignCommonPool(InspecTaskCreAndAssDto inspecTaskCreAndAssDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<InspectionTaskPoolListDto> assignSupTasks(InspectionTaskPoolListDto inspectionTaskPoolListDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<InspectionTaskPoolListDto> getReassignTaskByWkId(String workGroupId){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<InspectionTaskPoolListDto> filterInspectorOption(InspectionTaskPoolListDto inspectionTaskPoolListDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<List<String>> getInspectionLead(String workGroupId){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<List<TaskDto>> getTaskByAppNo(String appNo){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    FeignResponseEntity<OrganizationDto> getOrganizationById( String id){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    FeignResponseEntity< List<OrgUserRoleDto>> getSendEmailUser(@RequestParam("organizationId") String organizationId){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
