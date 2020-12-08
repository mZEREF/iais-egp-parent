package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * @Process auditAppCalculaterRiskJobHandler
 *
 * @author wangyu
 * @date 2020/12/7 16:45
 **/
@JobHandler(value="auditAppCalculaterRiskJobHandler")
@Component
@Slf4j
public class AuditAppCalculaterRiskJobHandler extends IJobHandler {


    @Autowired
    private ApplicationClient applicationClient;
    @Override
    public ReturnT<String> execute(String s) {
        logAbout("AuditAppCalculaterRiskJobHandler");
        try{
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            List<String> statuses = new ArrayList<>(2);
            statuses.add( ApplicationConsts.APPLICATION_STATUS_APPROVED);
            // statuses.add(ApplicationConsts.APPLICATION_STATUS_REJECTED);
            List<ApplicationDto> applicationDtos = applicationClient.getApplicationsByApplicationTypeAndStatusIn(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK,statuses).getEntity();
           if(IaisCommonUtils.isEmpty(applicationDtos)){
               logAbout("apps is null");
           }else {
               for(ApplicationDto applicationDto : applicationDtos){
                   logAbout(StringUtil.changeForLog("start app risk appno :" + applicationDto.getApplicationNo()));
                   //
               }
           }
        }catch (Exception e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
        JobLogger.log(StringUtil.changeForLog("---  AuditAppCalculaterRiskJobHandler end----"));
        return ReturnT.SUCCESS;
    }

    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
        JobLogger.log(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }

}
