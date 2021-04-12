package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppPremInspApptDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptCalendarStatusDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author Shicheng
 * @date 2021/4/10 10:39
 **/
@JobHandler(value="inspDateDraftRegularDelHandler")
@Component
@Slf4j
public class InspDateDraftRegularDelHandler extends IJobHandler {

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private AppointmentClient appointmentClient;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            logAbout("InspDateDraftRegularDelete");
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            int removeHours = systemParamConfig.getRemoveInspDateDraft();
            removeInspDateDraftByConfigValue(removeHours);
            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }

    public void removeInspDateDraftByConfigValue (int hours){
        List<AppPremInspApptDraftDto> appPremInspApptDraftDtos = inspectionTaskClient.getAllInspApptDrafts().getEntity();
        if(!IaisCommonUtils.isEmpty(appPremInspApptDraftDtos)) {
            for(AppPremInspApptDraftDto appPremInspApptDraftDto : appPremInspApptDraftDtos) {
                if(appPremInspApptDraftDto != null) {
                    try {
                        log.info(StringUtil.changeForLog("Remove InspDate Draft Application No. = " + appPremInspApptDraftDto.getApplicationNo()));
                        JobLogger.log(StringUtil.changeForLog("Remove InspDate Draft Application No. = " + appPremInspApptDraftDto.getApplicationNo()));
                        Date createDate = appPremInspApptDraftDto.getCreateTime();
                        Date today = new Date();
                        long diffMs = today.getTime() - createDate.getTime();
                        long diffHours = diffMs / (1000 * 60 * 60);
                        long hoursLong = hours;
                        if(diffHours >= hoursLong) {
                            inspectionTaskClient.deleteInspDateDraftById(appPremInspApptDraftDto.getId());
                            List<String> cancelRefNo = IaisCommonUtils.genNewArrayList();
                            cancelRefNo.add(appPremInspApptDraftDto.getApptRefNo());
                            ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
                            apptCalendarStatusDto.setCancelRefNums(cancelRefNo);
                            apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
                            appointmentClient.updateUserCalendarStatus(apptCalendarStatusDto);
                        }
                    } catch (Exception e) {
                        JobLogger.log(e);
                        log.error(e.getMessage(), e);
                        continue;
                    }
                }
            }
        }
    }

    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
        JobLogger.log(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }
}
