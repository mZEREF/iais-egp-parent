package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/12/4 15:13
 */
public class OrganizationMainClientFallback implements OrganizationMainClient{

    @Override
    public FeignResponseEntity<List<OrgUserDto>> retrieveOrgUserAccount(List<String> ids) {
        return IaisEGPHelper.getFeignResponseEntity("retrieveOrgUserAccount",ids);
    }

    @Override
    public FeignResponseEntity<TaskDto> updateTask(TaskDto taskDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateTask",taskDto);
    }

    @Override
    public FeignResponseEntity<OrgUserDto> retrieveOrgUserAccountById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("retrieveOrgUserAccountById",id);
    }

    @Override
    public FeignResponseEntity<OrgUserDto> retrieveOneOrgUserAccount(String userId) {
        return IaisEGPHelper.getFeignResponseEntity("retrieveOneOrgUserAccount",userId);
    }

    @Override
    public FeignResponseEntity<List<TaskDto>> getSupervisorPoolByGroupWordId(String workGroupId) {
        return IaisEGPHelper.getFeignResponseEntity("getSupervisorPoolByGroupWordId",workGroupId);
    }

    @Override
    public FeignResponseEntity<List<TaskDto>> getTasksByUserId(String userId) {
        return IaisEGPHelper.getFeignResponseEntity("getTasksByUserId",userId);
    }

    @Override
    public FeignResponseEntity<List<TaskDto>> getTasksByUserIdAndRole(String userId, String roleId) {
        return IaisEGPHelper.getFeignResponseEntity("getTasksByUserIdAndRole",userId,roleId);
    }

    @Override
    public FeignResponseEntity<List<OrgUserDto>> getUsersByWorkGroupName(String workGroupId, String status) {
        return IaisEGPHelper.getFeignResponseEntity("getUsersByWorkGroupName",workGroupId,status);
    }

    @Override
    public FeignResponseEntity<TaskDto> getTaskById(String taskId) {
        return IaisEGPHelper.getFeignResponseEntity("getTaskById",taskId);
    }

    @Override
    public FeignResponseEntity<WorkingGroupDto> createWorkGroup(WorkingGroupDto workingGroupDto) {
        return IaisEGPHelper.getFeignResponseEntity("createWorkGroup",workingGroupDto);
    }

    @Override
    public FeignResponseEntity<BroadcastOrganizationDto> createBroadcastOrganization(BroadcastOrganizationDto broadcastOrganizationDto) {
        return IaisEGPHelper.getFeignResponseEntity("createBroadcastOrganization",broadcastOrganizationDto);
    }

    @Override
    public FeignResponseEntity<List<UserGroupCorrelationDto>> getUserGroupCorreByUserId(String userId) {
        return IaisEGPHelper.getFeignResponseEntity("getUserGroupCorreByUserId",userId);
    }

    @Override
    public FeignResponseEntity<BroadcastOrganizationDto> getBroadcastOrganizationDto(String groupName, String groupDomain) {
        return IaisEGPHelper.getFeignResponseEntity("getBroadcastOrganizationDto",groupName,groupDomain);
    }

    @Override
    public FeignResponseEntity<List<TaskDto>> getInsKpiTask(InspecTaskCreAndAssDto inspecTaskCreAndAssDto) {
        return IaisEGPHelper.getFeignResponseEntity("getInsKpiTask",inspecTaskCreAndAssDto);
    }

    @Override
    public FeignResponseEntity<List<TaskDto>> getOtherKpiTask(TaskDto taskDto) {
        return IaisEGPHelper.getFeignResponseEntity("getOtherKpiTask",taskDto);
    }

    @Override
    public FeignResponseEntity<WorkingGroupDto> getWrkGrpById(String workGroupId) {
        return IaisEGPHelper.getFeignResponseEntity("getWrkGrpById",workGroupId);
    }

    @Override
    public FeignResponseEntity<OrganizationDto> getOrganizationById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getOrganizationById",id);
    }

    @Override
    public FeignResponseEntity<String> taskRead(String id) {
        return IaisEGPHelper.getFeignResponseEntity("taskRead",id);
    }

    @Override
    public FeignResponseEntity<LicenseeDto> getLicenseeDtoById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenseeDtoById",id);
    }

    @Override
    public FeignResponseEntity<List<TaskDto>> getTaskByAppNoStatus(String appNo, String taskStatus, String processUrl) {
        return IaisEGPHelper.getFeignResponseEntity("getTaskByAppNoStatus",appNo,taskStatus,processUrl);
    }

    @Override
    public FeignResponseEntity<LicenseeDto> getLicenseeByUserAccountInfo(String userAccountString) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenseeByUserAccountInfo",userAccountString);
    }

    @Override
    public FeignResponseEntity<Void> patchWorkingGrpForRoles(String body) {
        return IaisEGPHelper.getFeignResponseEntity("patchWorkingGrpForRoles",body);
    }

    @Override
    public FeignResponseEntity<List<String>> getInspectionLead(String workGroupId) {
        return IaisEGPHelper.getFeignResponseEntity("getInspectionLead",workGroupId);
    }

    @Override
    public FeignResponseEntity<InspecTaskCreAndAssDto> assignCommonPool(InspecTaskCreAndAssDto inspecTaskCreAndAssDto) {
        return IaisEGPHelper.getFeignResponseEntity("assignCommonPool",inspecTaskCreAndAssDto);
    }

    @Override
    public FeignResponseEntity<TaskDto> updateTaskForAssign(TaskDto taskDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateTaskForAssign",taskDto);
    }

    @Override
    public FeignResponseEntity<List<OrgUserDto>> activeUsersByWorkGroupAndRole(String workGroupId, String roleId) {
        return IaisEGPHelper.getFeignResponseEntity("activeUsersByWorkGroupAndRole",workGroupId,roleId);
    }

    @Override
    public FeignResponseEntity<List<WorkingGroupDto>> getWorkingGroup(String uerDomain) {
        return IaisEGPHelper.getFeignResponseEntity("getWorkingGroup",uerDomain);
    }

    @Override
    public FeignResponseEntity<List<WorkingGroupDto>> getHcsaWorkGroupsByName(String workGroupName) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaWorkGroupsByName",workGroupName);
    }

    @Override
    public FeignResponseEntity<List<TaskDto>> getCurrTaskByRefNo(String refNo) {
        return IaisEGPHelper.getFeignResponseEntity("getCurrTaskByRefNo",refNo);
    }

    @Override
    public FeignResponseEntity<List<String>> getWorkGrpsByUserId(String userId) {
        return IaisEGPHelper.getFeignResponseEntity("getWorkGrpsByUserId",userId);
    }

    @Override
    public FeignResponseEntity<List<TaskDto>> getTasksByRefNo(String refNo) {
        return IaisEGPHelper.getFeignResponseEntity("getTasksByRefNo",refNo);
    }
}
