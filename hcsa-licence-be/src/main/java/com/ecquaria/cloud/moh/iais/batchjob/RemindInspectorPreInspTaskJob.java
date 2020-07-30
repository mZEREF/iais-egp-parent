package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.InspectionRectificationProService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * @Process MohRemindDoPreInspTask
 *
 * @author Shicheng
 * @date 2020/7/29 17:49
 **/
@Delegator("remindInspectorPreInspTask")
@Slf4j
public class RemindInspectorPreInspTaskJob {

    @Autowired
    private InspectionRectificationProService inspectionRectificationProService;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private HcsaChklClient hcsaChklClient;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private SystemBeLicClient systemBeLicClient;

    /**
     * StartStep: remindDoPreInspTaskStart
     *
     * @param bpc
     * @throws
     */
    public void remindDoPreInspTaskStart(BaseProcessClass bpc){
        logAbout("Remind Inspector Do Pre Inspection");
    }

    /**
     * StartStep: remindDoPreInspTaskStep
     *
     * @param bpc
     * @throws
     */
    public void remindDoPreInspTaskStep(BaseProcessClass bpc) {
        logAbout("Remind Inspector Do Pre Inspection");
        List<ApplicationDto> applicationDtos =
                applicationClient.getApplicationByStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS).getEntity();
        if(!IaisCommonUtils.isEmpty(applicationDtos)){
            for(ApplicationDto applicationDto : applicationDtos){
                try {
                    sendEmailByInspReadiness(applicationDto);
                } catch (Exception e) {
                    JobLogger.log(e);
                    log.error(e.getMessage(), e);
                    continue;
                }
            }
        }
    }

    private void sendEmailByInspReadiness(ApplicationDto applicationDto) {
        int days = systemParamConfig.getPreInspTaskTeminder();
        String appNo = applicationDto.getApplicationNo();
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremCorrByAppNo(appNo).getEntity();
        String taskRefNo = appPremisesCorrelationDto.getId();
        List<TaskDto> taskDtos = organizationClient.getCurrTaskByRefNo(taskRefNo).getEntity();
        if(!IaisCommonUtils.isEmpty(taskDtos)){

        }
    }

    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The*****" + methodName +"******Start****"));
    }
}
