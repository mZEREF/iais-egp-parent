package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;

import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.AuditSystemPotitalListService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @Process auditChangeFinishedStatusJob
 *
 * @author wangyu
 * @date 2020/7/8 16:45
 **/
@JobHandler(value="auditChangeFinishedStatusJobHandler")
@Component
@Slf4j
public class AuditChangeFinishedStatusJobHandler extends IJobHandler {

    @Autowired
    private AuditSystemPotitalListService auditSystemPotitalListService;
    @Autowired
    private ApplicationClient applicationClient;
    @Override
    public ReturnT<String> execute(String s) {
        logAbout("AuditChangeFinishedStatusJob");
        try{
            List<AuditTaskDataFillterDto> auditTaskDataFillterDtos = auditSystemPotitalListService.getCanInactiveAudit();
            if(IaisCommonUtils.isEmpty(auditTaskDataFillterDtos)){
                log.info(StringUtil.changeForLog("--- no audit need inactive----"));
            }else {
                AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
                for (AuditTaskDataFillterDto auditTaskDataFillterDto : auditTaskDataFillterDtos) {
                    Boolean isEx = applicationClient.getExistAppByLicId(auditTaskDataFillterDto.getLicId(),auditTaskDataFillterDto.getHclCode()).getEntity();
                    if(isEx != null && isEx){
                        auditSystemPotitalListService.inActiveAudit(auditTaskDataFillterDto,intranet);
                    }
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
        log.info(StringUtil.changeForLog("---  audit inactive finish----"));
        return ReturnT.SUCCESS;
    }

    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
        JobLogger.log(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }

}
