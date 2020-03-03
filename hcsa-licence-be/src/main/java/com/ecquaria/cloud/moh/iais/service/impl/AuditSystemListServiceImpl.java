package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicInspectionGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremInspGrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremInspectStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditInspectorDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
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
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
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
        taskDto.setSlaAlertInDays(5);
        taskDto.setScore(4);
        taskDto.setRefNo(temp.getId());
        taskDto.setPriority(0);
        List<TaskDto> createTaskDtoList = new ArrayList<>();
        createTaskDtoList.add(taskDto);
        taskService.createTasks(createTaskDtoList);
        //createauditTypedata
        createAudit(temp);

        //updatestastus
        List<LicPremInspectStatusDto> instatusList = new ArrayList<>();
        LicPremInspectStatusDto statusDto = new LicPremInspectStatusDto();
        statusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        statusDto.setLicPremId(temp.getId());
        statusDto.setStatus("task assign");
        hcsaLicenceClient.create(instatusList).getEntity();
        //
        //create
        String preInspecRemarks = null;//todo
        HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(temp.getSvcName()).getEntity();
        String svcId = svcDto.getId();
        String stgId = HcsaConsts.ROUTING_STAGE_INS;
        HcsaSvcStageWorkingGroupDto dto = new HcsaSvcStageWorkingGroupDto();
        dto.setType("APTY002");//todo
        dto.setStageId(stgId);
        dto.setServiceId(svcId);
        dto.setOrder(1);
        dto = hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(dto).getEntity();
        String workGrp = dto.getGroupId();
        String subStage = HcsaConsts.ROUTING_STAGE_PRE;
        String status = ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING;
        createLicPremisesRoutingHistory(temp.getId(),status,taskDto.getTaskKey(),preInspecRemarks, InspectionConstants.INSPECTION_STATUS_PENDING_PRE, RoleConsts.USER_ROLE_INSPECTIOR,workGrp,subStage);
        //create grop info
        createInspectionGroupInfo(temp);
    }

    private void createAudit(AuditTaskDataFillterDto temp) {
        LicPremisesAuditDto licPremisesAuditDto = new LicPremisesAuditDto();
        LicPremisesAuditInspectorDto licPremisesAuditInspectorDto = new LicPremisesAuditInspectorDto();
        licPremisesAuditDto.setAuditRiskType(temp.getRiskType());
        licPremisesAuditDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        licPremisesAuditDto.setRemarks(null);
        licPremisesAuditDto.setInRiskSocre(1);
        licPremisesAuditDto.setIncludeRiskType(temp.getRiskType());//todo
        licPremisesAuditDto.setLicPremId(temp.getId());
        licPremisesAuditDto.setAuditType(temp.getAuditType());
        licPremisesAuditDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        licPremisesAuditDto = hcsaLicenceClient.createLicPremAudit(licPremisesAuditDto).getEntity();
        LicPremisesAuditInspectorDto audinspDto = new LicPremisesAuditInspectorDto();
        audinspDto.setInspectorId(temp.getInspectorId());
        audinspDto.setAuditId(licPremisesAuditDto.getId());
        audinspDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        hcsaLicenceClient.createLicPremisesAuditInspector(audinspDto);
    }

    private void createInspectionGroupInfo(AuditTaskDataFillterDto temp) {
        LicInspectionGroupDto dto = new LicInspectionGroupDto();
        dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        dto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        dto = hcsaLicenceClient.createLicInspectionGroup(dto).getEntity();
        LicPremInspGrpCorrelationDto dtocorre = new LicPremInspGrpCorrelationDto();
        dtocorre.setInsGrpId(dto.getId());
        dtocorre.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        dtocorre.setLicPremId(temp.getId());
        dtocorre.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
    }

    @Override
    public List<AuditTaskDataFillterDto> doRemove(List<AuditTaskDataFillterDto> auditTaskDataDtos) {
        List<AuditTaskDataFillterDto> removeList = new ArrayList<>();
        if(!IaisCommonUtils.isEmpty(auditTaskDataDtos)){
            for(AuditTaskDataFillterDto temp:auditTaskDataDtos){
                if(!temp.isSelectedForAudit()){
                    removeList.add(temp);
                }
            }
        }
        return removeList;
    }

    private LicPremisesRoutingHistoryDto createLicPremisesRoutingHistory(String licPremId, String appStatus,
                                                                         String stageId, String internalRemarks, String processDec, String role, String wrkGroupId, String subStageId){
        LicPremisesRoutingHistoryDto licPremisesRoutingHistoryDto = new LicPremisesRoutingHistoryDto();
        licPremisesRoutingHistoryDto.setLicPremId(licPremId);
        licPremisesRoutingHistoryDto.setStageId(stageId);
        licPremisesRoutingHistoryDto.setRoleId(role);
        licPremisesRoutingHistoryDto.setInternalRemark(internalRemarks);
        licPremisesRoutingHistoryDto.setRouteStatus(appStatus);
        licPremisesRoutingHistoryDto.setActionBy(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        licPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        licPremisesRoutingHistoryDto.setPorcessDescision(processDec);
        licPremisesRoutingHistoryDto.setWrkGrpId(wrkGroupId);
        licPremisesRoutingHistoryDto.setSubStage(subStageId);
        licPremisesRoutingHistoryDto = hcsaLicenceClient.createLicPremisesRoutingHistory(licPremisesRoutingHistoryDto).getEntity();
        return licPremisesRoutingHistoryDto;
    }

    @Override
    public void doCancel(List<AuditTaskDataFillterDto> auditTaskDataDtos) {
        if(!IaisCommonUtils.isEmpty(auditTaskDataDtos)){
            for(AuditTaskDataFillterDto temp: auditTaskDataDtos){
                if(temp.isSelectedForAudit()){
                    cancelTask(temp);
                }
            }
        }
    }

    private void cancelTask(AuditTaskDataFillterDto temp) {
        TaskDto taskDto = new TaskDto();
        taskDto.setUserId(temp.getInspectorId());
        taskDto.setProcessUrl("todo");//todo
        taskDto.setTaskType(TaskConsts.TASK_TYPE_INSPECTION);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setSlaDateCompleted(null);
        taskDto.setRoleId(RoleConsts.USER_ROLE_AO1);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        taskDto.setWkGrpId(temp.getWorkGroupId());
        taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INP);
        taskDto.setSlaAlertInDays(2);
        taskDto.setSlaRemainInDays(3);
        taskDto.setSlaAlertInDays(5);
        taskDto.setScore(4);
        taskDto.setRefNo(temp.getId());
        taskDto.setPriority(0);
        List<TaskDto> createTaskDtoList = new ArrayList<>();
        createTaskDtoList.add(taskDto);
        taskService.createTasks(createTaskDtoList);
        //createauditTypedata
        createAudit(temp);

        //updatestastus
        List<LicPremInspectStatusDto> instatusList = new ArrayList<>();
        LicPremInspectStatusDto statusDto = new LicPremInspectStatusDto();
        statusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        statusDto.setLicPremId(temp.getId());
        statusDto.setStatus("task cancel");
        hcsaLicenceClient.create(instatusList).getEntity();
        //
        //create
        String preInspecRemarks = null;//todo
        HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(temp.getSvcName()).getEntity();
        String svcId = svcDto.getId();
        String stgId = HcsaConsts.ROUTING_STAGE_INS;
        HcsaSvcStageWorkingGroupDto dto = new HcsaSvcStageWorkingGroupDto();
        dto.setType("APTY002");//todo
        dto.setStageId(stgId);
        dto.setServiceId(svcId);
        dto.setOrder(2);
        dto = hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(dto).getEntity();
        String workGrp = dto.getGroupId();
        String subStage = HcsaConsts.ROUTING_STAGE_PRE;
        String status = "status";//todo
        createLicPremisesRoutingHistory(temp.getId(),status,taskDto.getTaskKey(),preInspecRemarks, InspectionConstants.INSPECTION_STATUS_PENDING_PRE, RoleConsts.USER_ROLE_INSPECTIOR,workGrp,subStage);
        //create grop info
        createInspectionGroupInfo(temp);
    }
}
