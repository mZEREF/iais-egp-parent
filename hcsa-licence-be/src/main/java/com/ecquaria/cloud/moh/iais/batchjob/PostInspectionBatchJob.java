package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/3/17 15:06
 */
@Delegator("postInspectionTask")
@Slf4j
public class PostInspectionBatchJob {

    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private InsRepService insRepService;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("The CessationEffectiveDateBatchjob is start ..."));
    }

    public void doBatchJob(BaseProcessClass bpc) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        Map<String, List<String>> map = hcsaLicenceClient.getPostInspectionMap().getEntity();
        String appType = ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION;
        String appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING;
        map.forEach((insGrpId, licPremisIds) -> {
            String grpNo = beEicGatewayClient.getAppNo(ApplicationConsts.APPLICATION_TYPE_REINSTATEMENT,signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
            Double amount = 0.0;
            AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
            List<AppSubmissionDto> appSubmissionDtoList = hcsaLicenceClient.getAppSubmissionDtos(licPremisIds).getEntity();
            for(AppSubmissionDto entity : appSubmissionDtoList){
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = entity.getAppSvcRelatedInfoDtoList();
                String serviceName = appSvcRelatedInfoDtoList.get(0).getServiceName();
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
                String svcId = hcsaServiceDto.getId();
                String svcCode = hcsaServiceDto.getSvcCode();
                appSvcRelatedInfoDtoList.get(0).setServiceId(svcId);
                appSvcRelatedInfoDtoList.get(0).setServiceCode(svcCode);
                entity.setAppGrpNo(grpNo);
                entity.setAppType(appType);
                entity.setAmount(amount);
                entity.setAuditTrailDto(intranet);
                entity.setPreInspection(true);
                entity.setRequirement(true);
                entity.setStatus(appStatus);
                setRiskToDto(entity);
            }
            log.info("========================>>>>>success!!!!");
            try {
                Long l = System.currentTimeMillis();
                String submissionId = generateIdClient.getSeqId().getEntity();
                eventBusHelper.submitAsyncRequest(appSubmissionDtoList,submissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,EventBusConsts.OPERATION_POST_INSPECTION_TASK,l.toString(),null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



    public void setRiskToDto(AppSubmissionDto appSubmissionDto) {
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
