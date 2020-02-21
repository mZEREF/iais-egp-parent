package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/2/19 10:54
 */
@Slf4j
@Service
public class AuditSystemListServiceImpl implements AuditSystemListService {
    @Autowired
    HcsaConfigClient hcsaConfigClient;
    @Autowired
    OrganizationClient organizationClient;
    @Autowired
    private TaskService taskService;
    @Override
    public void getInspectors(List<AuditTaskDataFillterDto> auditTaskDataDtos) {
        if(!IaisCommonUtils.isEmpty(auditTaskDataDtos)){
            for(AuditTaskDataFillterDto temp:auditTaskDataDtos){
                HcsaSvcStageWorkingGroupDto dto = new HcsaSvcStageWorkingGroupDto();
                HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(temp.getSvcName()).getEntity();
                dto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
                dto.setOrder(1);
                dto.setServiceId(svcDto.getId());
                dto.setType("APTY002");
                dto = hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(dto).getEntity();
                String workGroupId = dto.getGroupId();
                temp.setWorkGroupId(workGroupId);
                List<OrgUserDto> orgDtos = organizationClient.getUsersByWorkGroupName(workGroupId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
                getinspectorOp(orgDtos,temp);
            }
        }

    }

    private void getinspectorOp(List<OrgUserDto> orgDtos, AuditTaskDataFillterDto temp) {
        List<SelectOption> ops = new ArrayList<>();
        if(!IaisCommonUtils.isEmpty(orgDtos)){
            for(OrgUserDto ou :orgDtos){
                SelectOption op = new SelectOption();
                op.setText(ou.getDisplayName());
                op.setValue(ou.getUserId());
                ops.add(op);
            }
            temp.setInspectors(ops);
        }
    }

    @Override
    public List<SelectOption> getAuditOp() {
        String category[] = {"ADTYPE001","ADTYPE002","ADTYPE003"};
        List<SelectOption> inpTypeOp =   MasterCodeUtil.retrieveOptionsByCodes(category);
        return inpTypeOp;
    }

    @Override
    public void doSubmit(List<AuditTaskDataFillterDto> auditTaskDataDtos) {
        if(!IaisCommonUtils.isEmpty(auditTaskDataDtos)){
            for(AuditTaskDataFillterDto temp: auditTaskDataDtos){
                if(temp.isSelectedForAudit()){
                    assignTask(temp);
                }
            }
        }
    }

    private void assignTask(AuditTaskDataFillterDto temp) {
        TaskDto taskDto = new TaskDto();
        taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
        taskDto.setUserId(temp.getInspectorId());
        taskDto.setProcessUrl("todo");//todo
        taskDto.setTaskType(TaskConsts.TASK_TYPE_MAIN_FLOW);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setSlaDateCompleted(null);
        taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        taskDto.setWkGrpId(temp.getWorkGroupId());
        taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INP);
        taskDto.setSlaAlertInDays(2);
        taskDto.setSlaRemainInDays(3);
        taskDto.setScore(4);
        taskDto.setRefNo(temp.getId());
        taskDto.setPriority(0);
        List<TaskDto> createTaskDtoList = new ArrayList<>();
        createTaskDtoList.add(taskDto);
        taskService.createTasks(createTaskDtoList);
    }
}
