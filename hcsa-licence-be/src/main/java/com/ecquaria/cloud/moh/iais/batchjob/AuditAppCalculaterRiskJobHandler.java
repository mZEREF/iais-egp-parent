package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AuditRiskDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.SearchAuditRiskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
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
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private EventBusHelper eventBusHelper;

    @Override
    public ReturnT<String> execute(String s) {
        logAbout("AuditAppCalculaterRiskJobHandler");
        try{
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            List<String>  statuses= new ArrayList<>(2);
            statuses.add( ApplicationConsts.APPLICATION_STATUS_APPROVED);
            // statuses.add(ApplicationConsts.APPLICATION_STATUS_REJECTED);
            SearchAuditRiskDto searchAuditRiskDto = new SearchAuditRiskDto();
            searchAuditRiskDto.setAppType(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK);
            searchAuditRiskDto.setStatuses(statuses);
            List< AuditRiskDto> auditRiskDtoList = applicationClient.getApplicationsByApplicationTypeAndStatusInOnlyForAuditRisk(searchAuditRiskDto).getEntity();
           if(IaisCommonUtils.isEmpty(auditRiskDtoList)){
               logAbout("apps is null");
           }else {
               for(AuditRiskDto auditRiskDto : auditRiskDtoList){
                   logAbout(StringUtil.changeForLog("start app risk appno :" +  auditRiskDto.getApplicationDto().getApplicationNo()));
                   //event bus save risk to db
                   auditRiskDto.setAuditTrailDto(AuditTrailHelper.getCurrentAuditTrailDto());
                   String submitId = generateIdClient.getSeqId().getEntity();
                   auditRiskDto.setEventRefNo(submitId);
                   try {
                       eventBusHelper.submitAsyncRequest( auditRiskDto,submitId, EventBusConsts.SERVICE_NAME_LICENCESAVE,EventBusConsts.OPERATION_AUDIT_RISK_SAVE, auditRiskDto.getEventRefNo(),null);
                       eventBusHelper.submitAsyncRequest(auditRiskDto,submitId, EventBusConsts.SERVICE_NAME_APPSUBMIT,EventBusConsts.OPERATION_AUDIT_RISK_SAVE,auditRiskDto.getEventRefNo(),null);
                   } catch (Exception e) {
                       log.error(e.getMessage(), e);
                   }
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
