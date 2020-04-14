package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.*;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.client.*;

import java.util.ArrayList;
import java.util.List;

import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.kafka.model.Submission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private EventClient eventClient;
    @Autowired
    private ApplicationClient applicationClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    public void getInspectors(List<AuditTaskDataFillterDto> auditTaskDataDtos) {
        if (!IaisCommonUtils.isEmpty(auditTaskDataDtos)) {
            for (AuditTaskDataFillterDto temp : auditTaskDataDtos) {
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
                getinspectorOp(orgDtos, temp);
            }
        }

    }

    private void getinspectorOp(List<OrgUserDto> orgDtos, AuditTaskDataFillterDto temp) {
        List<SelectOption> ops = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(orgDtos)) {
            for (OrgUserDto ou : orgDtos) {
                SelectOption op = new SelectOption();
                op.setText(ou.getDisplayName());
                op.setValue(ou.getId());
                ops.add(op);
            }
            temp.setInspectors(ops);
        }
    }

    @Override
    public List<SelectOption> getAuditOp() {
        String category[] = {"ADTYPE001", "ADTYPE002", "ADTYPE003"};
        List<SelectOption> inpTypeOp = MasterCodeUtil.retrieveOptionsByCodes(category);
        return inpTypeOp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doSubmit(List<AuditTaskDataFillterDto> auditTaskDataDtos) {
        if (!IaisCommonUtils.isEmpty(auditTaskDataDtos)) {
            for (AuditTaskDataFillterDto temp : auditTaskDataDtos) {
                if (temp.isSelectedForAudit()) {
                    assignTask(temp);
                }
            }
        }
    }

    private void assignTask(AuditTaskDataFillterDto temp) {
        String submitId = generateIdClient.getSeqId().getEntity();
        //create auditType data  and create grop info
        AuditCombinationDto auditCombinationDto = new AuditCombinationDto();
        auditCombinationDto.setAuditTaskDataFillterDto(temp);
        createAudit(temp,submitId,auditCombinationDto);
        //create grop info
        // createInspectionGroupInfo(temp,submitId);

        // create app
        List<String> licIds = new ArrayList<>(1);
        if( !StringUtil.isEmpty(temp.getLicId())){
            licIds.add(temp.getLicId());
            createAuditTaskApp(licIds,submitId,auditCombinationDto);
        }

        //create task
        //createTask(temp,submitId,auditCombinationDto);
    }

    public void createTaskCallBack(String eventRefNum,String submissionId)throws FeignException {
        log.info("call back createTaskCallBack ===================>>>>>");
        List<Submission> submissionList = eventClient.getSubmission(submissionId).getEntity();
        AuditCombinationDto auditCombinationDto = null;
        if(submissionList!= null && submissionList.size() > 0){
            log.info(submissionList .size() +"submissionList .size()");
            for(Submission submission : submissionList){
                if(EventBusConsts.SERVICE_NAME_APPSUBMIT.equals(submission.getSubmissionIdentity().getService())){
                    auditCombinationDto = JsonUtil.parseToObject(submission.getData(), AuditCombinationDto.class);
                    break;
                }
            }
            if(auditCombinationDto != null){
                createTask(auditCombinationDto.getAuditTaskDataFillterDto(), submissionId, auditCombinationDto,eventRefNum);
            }

        }else {
            log.info("========createTaskCallBack  submissionList is null.");
        }
    }
    public void createTask(AuditTaskDataFillterDto temp,String submitId,AuditCombinationDto auditCombinationDto,String eventRefNum){
        TaskDto taskDto = new TaskDto();
        taskDto.setId(generateIdClient.getSeqId().getEntity());
        taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
        taskDto.setUserId(temp.getInspectorId());
        taskDto.setProcessUrl("todo");//todo
        taskDto.setTaskType(TaskConsts.TASK_TYPE_INSPECTION_SUPER);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setSlaDateCompleted(null);
        taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        taskDto.setWkGrpId(temp.getWorkGroupId());
        taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INP);
        taskDto.setSlaAlertInDays(2);
        taskDto.setSlaRemainInDays(3);
        taskDto.setSlaInDays(5);
        taskDto.setScore(4);
        List<ApplicationDto> postApps = applicationClient.getAppsByGrpNo(eventRefNum).getEntity();
        if(postApps != null && postApps.size() >0 ){
            String corrId = applicationClient.getCorrIdByAppId(postApps.get(0).getId()).getEntity();
            taskDto.setRefNo(corrId);
        }else {
            log.info("=============== group id is null.==========");
            return;
        }
        taskDto.setPriority(0);
        List<TaskDto> createTaskDtoList = IaisCommonUtils.genNewArrayList();
        createTaskDtoList.add(taskDto);
        auditCombinationDto.setTaskDtos(createTaskDtoList);
        //taskService.createTasks(createTaskDtoList);
        log.info("========================>>>>> create task !!!!");
        try {
            eventBusHelper.submitAsyncRequest(auditCombinationDto,submitId, EventBusConsts.SERVICE_NAME_ROUNTINGTASK,EventBusConsts.OPERATION_CREATE_AUDIT_TASK,auditCombinationDto.getEventRefNo(),null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateLicPremisesAuditDto(AuditTaskDataFillterDto temp,String status){
        LicPremisesAuditDto licPremisesAuditDto = hcsaLicenceClient.getLicPremAuditByGuid(temp.getAuditId()).getEntity();
        licPremisesAuditDto.setStatus(status);
        licPremisesAuditDto.setRemarks( temp.getCancelReason());
        hcsaLicenceClient.createLicPremAudit(licPremisesAuditDto);
    }
    private void createAudit(AuditTaskDataFillterDto temp,String submitId,AuditCombinationDto auditCombinationDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String grpNo = beEicGatewayClient.getAppNo(ApplicationConsts.APPLICATION_TYPE_REINSTATEMENT,signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
        auditCombinationDto.setEventRefNo(grpNo);
        LicPremisesAuditDto licPremisesAuditDto = new LicPremisesAuditDto();
        licPremisesAuditDto.setId(generateIdClient.getSeqId().getEntity());
        licPremisesAuditDto.setAuditRiskType(temp.getRiskType());
        licPremisesAuditDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        licPremisesAuditDto.setRemarks(null);
        licPremisesAuditDto.setInRiskSocre(1);
        licPremisesAuditDto.setIncludeRiskType(temp.getRiskType());//todo
        licPremisesAuditDto.setLicPremId(temp.getId());
        licPremisesAuditDto.setAuditType(temp.getAuditType());
        licPremisesAuditDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        // create audit event bus
        //licPremisesAuditDto = hcsaLicenceClient.createLicPremAudit(licPremisesAuditDto).getEntity();
        LicPremisesAuditInspectorDto audinspDto = new LicPremisesAuditInspectorDto();
        audinspDto.setInspectorId(temp.getInspectorId());
        audinspDto.setAuditId(licPremisesAuditDto.getId());
        audinspDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
         //hcsaLicenceClient.createLicPremisesAuditInspector(audinspDto);
        LicInspectionGroupDto dto = new LicInspectionGroupDto();
        dto.setId(generateIdClient.getSeqId().getEntity());
        dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        dto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        LicPremInspGrpCorrelationDto dtocorre = new LicPremInspGrpCorrelationDto();
        dtocorre.setInsGrpId(dto.getId());
        dtocorre.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        dtocorre.setLicPremId(temp.getId());
        dtocorre.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        auditCombinationDto.setLicPremisesAuditDto(licPremisesAuditDto);
        auditCombinationDto.setLicPremisesAuditInspectorDto(audinspDto);
        auditCombinationDto.setLicInspectionGroupDto(dto);
        auditCombinationDto.setLicPremInspGrpCorrelationDto(dtocorre);
        log.info("========================>>>>> create audit !!!!");
        try {
            eventBusHelper.submitAsyncRequest(auditCombinationDto,submitId, EventBusConsts.SERVICE_NAME_LICENCESAVE,EventBusConsts.OPERATION_CREATE_AUDIT_TASK,auditCombinationDto.getEventRefNo(),null);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        hcsaLicenceClient.createLicInspectionGroupCorre(dtocorre);
    }

    @Override
    public List<AuditTaskDataFillterDto> doRemove(List<AuditTaskDataFillterDto> auditTaskDataDtos) {
        List<AuditTaskDataFillterDto> removeList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(auditTaskDataDtos)) {
            for (AuditTaskDataFillterDto temp : auditTaskDataDtos) {
                if (!temp.isSelectedForAudit()) {
                    removeList.add(temp);
                }
            }
        }
        return removeList;
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
        taskDto.setSlaInDays(5);
        taskDto.setScore(4);
        taskDto.setRefNo(temp.getId());
        taskDto.setPriority(0);
        List<TaskDto> createTaskDtoList = IaisCommonUtils.genNewArrayList();
        createTaskDtoList.add(taskDto);
       // taskService.createTasks(createTaskDtoList);
        //update audit status
        updateLicPremisesAuditDto(temp,AppConsts.AUDIT_TASK_CANCEL_PENDING_STATUS);
    }

    @Override
    public List<HcsaServiceDto> getActiveHCIServices() {
        return hcsaConfigClient.getActiveServices().getEntity();
    }

    @Override
    public  List<SelectOption> getActiveHCIServicesByNameOrCode(List<HcsaServiceDto> hcsaServiceDtos,String type){
        List<SelectOption> activeHCISelections = IaisCommonUtils.genNewArrayList();
        if (hcsaServiceDtos != null && hcsaServiceDtos.size() > 0) {
            for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
                if (StringUtil.isEmpty(type)) {
                    activeHCISelections.add(new SelectOption(String.valueOf(hcsaServiceDto.getSvcCode()), String.valueOf(hcsaServiceDto.getSvcName())));
                } else if (HcsaLicenceBeConstant.GET_HCI_SERVICE_SELECTION_NAME_TAG.equalsIgnoreCase(type)) {
                    activeHCISelections.add(new SelectOption(String.valueOf(hcsaServiceDto.getSvcName()), String.valueOf(hcsaServiceDto.getSvcName())));
                } else {
                    activeHCISelections.add(new SelectOption(String.valueOf(hcsaServiceDto.getSvcName()), String.valueOf(hcsaServiceDto.getSvcCode())));
                }
            }
        }
        return activeHCISelections;
    }

    @Override
    public List<SelectOption> getActiveHCICode() {
        List<String> activeHcicode = hcsaLicenceClient.getActiveHCICode().getEntity();
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        if(activeHcicode != null) {
            for (String string : activeHcicode) {
                selectOptions.add(new SelectOption(string, string));
            }
        }
        return selectOptions;
    }

    public String createAuditTaskApp(List<String> licIds, String submissionId,AuditCombinationDto auditCombinationDto) {
        List<AppSubmissionDto> appSubmissionDtoList = hcsaLicenceClient.getAppSubmissionDtos(licIds).getEntity();
        String grpNo = auditCombinationDto.getEventRefNo();
        for(AppSubmissionDto entity : appSubmissionDtoList){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = entity.getAppSvcRelatedInfoDtoList();
            String serviceName = appSvcRelatedInfoDtoList.get(0).getServiceName();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
            String svcId = hcsaServiceDto.getId();
            String svcCode = hcsaServiceDto.getSvcCode();
            appSvcRelatedInfoDtoList.get(0).setServiceId(svcId);
            appSvcRelatedInfoDtoList.get(0).setServiceCode(svcCode);
            entity.setAppGrpNo(grpNo);
            entity.setAppType(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK);
            entity.setAmount(0.0);
            entity.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            entity.setPreInspection(true);
            entity.setRequirement(true);
            entity.setStatus(ApplicationConsts.APPLICATION_STATUS_CREATE_AUDIT_TASK);
            entity.setEventRefNo(grpNo);
            setRiskToDto(entity);
        }
        auditCombinationDto.setAppSubmissionDtoList(appSubmissionDtoList);
        log.info("========================>>>>> creat application!!!!");
        try {
            eventBusHelper.submitAsyncRequest(auditCombinationDto,submissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,EventBusConsts.OPERATION_CREATE_AUDIT_TASK,auditCombinationDto.getEventRefNo(),null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setRiskToDto(AppSubmissionDto appSubmissionDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = new ArrayList();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setApptype(appSubmissionDto.getAppType());
            riskAcceptiionDtoList.add(riskAcceptiionDto);
        }

        List<RiskResultDto> riskResultDtoList = hcsaConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();

        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            String serviceCode = appSvcRelatedInfoDto.getServiceCode();
            RiskResultDto riskResultDto = getRiskResultDtoByServiceCode(riskResultDtoList,serviceCode);
            if(riskResultDto!= null){
                appSvcRelatedInfoDto.setScore(riskResultDto.getScore());
                appSvcRelatedInfoDto.setDoRiskDate(riskResultDto.getDoRiskDate());
            }
        }
    }


    private RiskResultDto getRiskResultDtoByServiceCode(List<RiskResultDto> riskResultDtoList,String serviceCode){
        RiskResultDto result = null;
        if(riskResultDtoList == null || StringUtil.isEmpty(serviceCode)){
            return result;
        }
        for(RiskResultDto riskResultDto : riskResultDtoList){
            if(serviceCode.equals(riskResultDto.getSvcCode())){
                result = riskResultDto ;
            }
        }
        return result;
    }
}
