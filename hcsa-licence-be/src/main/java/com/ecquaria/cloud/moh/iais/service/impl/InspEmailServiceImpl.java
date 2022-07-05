package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.InsEmailClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * InspEmailServiceImpl
 *
 * @author junyu
 * @date 2019/11/23
 */
@Service
@Slf4j
public class InspEmailServiceImpl implements InspEmailService {
    @Autowired
    private InsEmailClient insEmailClient;
    @Autowired
    AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private SystemBeLicClient systemClient;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    ApplicationClient applicationClient;

    @Autowired
    OrganizationClient licenseeClient;

    @Autowired
    TaskService taskService;

    @Autowired
    private AppCommService appCommService;

    @Override
    public void updateEmailDraft(InspectionEmailTemplateDto inspectionEmailTemplateDto) {
        inspectionEmailTemplateDto.setMessageContent(StringUtil.removeNonUtf8(inspectionEmailTemplateDto.getMessageContent()));
        inspectionEmailTemplateDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        insEmailClient.updateEmailDraft(inspectionEmailTemplateDto).getEntity();
    }

    @Override
    public String insertEmailDraft(InspectionEmailTemplateDto inspectionEmailTemplateDto) {
        inspectionEmailTemplateDto.setMessageContent(StringUtil.removeNonUtf8(inspectionEmailTemplateDto.getMessageContent()));
        inspectionEmailTemplateDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return insEmailClient.insertEmailDraft(inspectionEmailTemplateDto).getEntity();
    }


    @Override
    public InspectionEmailTemplateDto getInsertEmail(String appPremCorrId) {
        return insEmailClient.getInspectionEmail(appPremCorrId).getEntity();
    }

    @Override
    public ApplicationViewDto getAppViewByCorrelationId(String correlationId) {
        return applicationViewService.getApplicationViewDtoByCorrId(correlationId);
    }

    @Override
    public InspectionEmailTemplateDto loadingEmailTemplate(String id) {
        return systemClient.loadingEmailTemplate(id).getEntity();
    }


    @Override
    public LicenseeDto getLicenseeDtoById(String id) {
        return licenseeClient.getLicenseeDtoById(id).getEntity();
    }


    @Override
    public List<AppPremisesCorrelationDto> getAppPremisesCorrelationsByPremises(String appCorrId) {
        return appPremisesCorrClient.getAppPremisesCorrelationsByPremises(appCorrId).getEntity();
    }

    @Override
    public List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId){
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos start ...."));
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos stageId -->:"+stageId));
        for(ApplicationDto applicationDto : applicationDtos){
            AppGrpPremisesDto appGrpPremisesEntityDto = appCommService.getActivePremisesByAppNo(applicationDto.getApplicationNo());
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);

                hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setBaseServiceId(applicationDto.getRoutingServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            if(appGrpPremisesEntityDto != null){
                hcsaSvcStageWorkingGroupDto.setPremiseType(appGrpPremisesEntityDto.getPremisesType());
            }else{
                log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos this APP do not have the premise :"+applicationDto.getApplicationNo()));
            }
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos end ...."));
        return hcsaSvcStageWorkingGroupDtos;
    }

    @Override
    public TaskDto completedTask(TaskDto taskDto) {
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskService.updateTask(taskDto);
    }
    @Override
    public String getLeadWithTheFewestScores(List<TaskDto> taskScoreDtos, List<String> leads) {
        List<TaskDto> taskUserDtos = IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isEmpty(taskScoreDtos)){
            log.info(StringUtil.changeForLog("taskScoreDtos = null"));
            JobLogger.log(StringUtil.changeForLog("taskScoreDtos = null"));
            return leads.get(0);
        } else {
            for(TaskDto taskDto : taskScoreDtos){
                String userId = taskDto.getUserId();
                for(String lead : leads) {
                    if (!StringUtil.isEmpty(userId)) {
                        if(userId.equals(lead)){
                            taskUserDtos.add(taskDto);
                        }
                    }
                }
            }
            String lead = getLeadByTaskScore(taskUserDtos, leads);
            return lead;
        }
    }

    private String getLeadByTaskScore(List<TaskDto> taskUserDtos, List<String> leads) {
        if(IaisCommonUtils.isEmpty(taskUserDtos)){
            return leads.get(0);
        } else {
            int score1 = 0;
            String lead = "";
            for(TaskDto taskDto : taskUserDtos){
                if(StringUtil.isEmpty(lead)){
                    lead = taskDto.getUserId();
                    score1 = taskDto.getScore();
                } else {
                    int scoreNow = taskDto.getScore();
                    if(scoreNow < score1){
                        lead = taskDto.getUserId();
                        score1 = taskDto.getScore();
                    }
                }
            }
            return lead;
        }
    }
}
