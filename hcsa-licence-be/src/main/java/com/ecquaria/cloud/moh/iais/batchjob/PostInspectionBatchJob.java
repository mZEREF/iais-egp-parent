package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditCombinationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremInspGrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.PostInspectionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.*;
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
        log.debug(StringUtil.changeForLog("The postInspection is start ..."));
    }

    public void doBatchJob(BaseProcessClass bpc) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        Map<String, List<String>> map = hcsaLicenceClient.getPostInspectionMap().getEntity();
        log.debug(StringUtil.changeForLog("=============map size================"+map.size()));
        //insGrpId  licIds
        AuditTrailDto auditTrailDto = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        String appType = ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION;
        String appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING;
        List<AppSubmissionDto> appSubmissionDtos = IaisCommonUtils.genNewArrayList();
        List<String> insGrpIds = IaisCommonUtils.genNewArrayList();
        map.forEach((insGrpId, licIds) -> {
            String grpNo = beEicGatewayClient.getAppNo(appType, signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
            List<AppSubmissionDto> appSubmissionDtoList = hcsaLicenceClient.getAppSubmissionDtos(licIds).getEntity();
            for(AppSubmissionDto entity : appSubmissionDtoList){
                entity.setAppGrpNo(grpNo);
                entity.setAppType(appType);
                entity.setAmount(0.0);
                entity.setAuditTrailDto(auditTrailDto);
                entity.setPreInspection(true);
                entity.setRequirement(true);
                entity.setStatus(appStatus);
                entity.setEventRefNo(grpNo);
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = entity.getAppSvcRelatedInfoDtoList();
                String serviceName = appSvcRelatedInfoDtoList.get(0).getServiceName();
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
                String svcId = hcsaServiceDto.getId();
                String svcCode = hcsaServiceDto.getSvcCode();
                appSvcRelatedInfoDtoList.get(0).setServiceId(svcId);
                appSvcRelatedInfoDtoList.get(0).setServiceCode(svcCode);
                setRiskToDto(entity);
                appSubmissionDtos.add(entity);
            }
            insGrpIds.clear();
            insGrpIds.add(insGrpId);
            log.debug(StringUtil.changeForLog("=============insGrpIds size================"+insGrpIds.size()));
            List<LicPremInspGrpCorrelationDto> licInsGrpDtos = hcsaLicenceClient.getLicInsGrpByIds(insGrpIds).getEntity();
            if(!IaisCommonUtils.isEmpty(licInsGrpDtos)){
                for(LicPremInspGrpCorrelationDto dto : licInsGrpDtos){
                    dto.setAuditTrailDto(auditTrailDto);
                }
            }
//        List<AppSubmissionDto> entity = applicationClient.savePostSubmision(appSubmissionDtos).getEntity();
            PostInspectionDto postInspectionDto = new PostInspectionDto();
            Long auto = System.currentTimeMillis();
            String submissionId = generateIdClient.getSeqId().getEntity();
            postInspectionDto.setSubmissionDtos(appSubmissionDtos);
            postInspectionDto.setLicPremInspGrpCorrelationDtos(licInsGrpDtos);
            postInspectionDto.setEventRefNo(grpNo);
            log.debug(StringUtil.changeForLog("=============event bus start ================"));
            //app
            eventBusHelper.submitAsyncRequest(postInspectionDto, submissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                    EventBusConsts.OPERATION_POST_INSPECTION_APP_LIC, grpNo, bpc.process);
            //licence
            eventBusHelper.submitAsyncRequest(postInspectionDto, submissionId, EventBusConsts.SERVICE_NAME_LICENCESAVE,
                    EventBusConsts.OPERATION_POST_INSPECTION_APP_LIC, grpNo, bpc.process);
        });
    }



    private String createPostTaskApp(List<String> licIds, String submissionId, AuditCombinationDto auditCombinationDto) {
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
            appSvcRelatedInfoDtoList.get(0).setHciCode(auditCombinationDto.getAuditTaskDataFillterDto().getHclCode());
            entity.setAppGrpNo(grpNo);
            entity.setAppType(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION);
            entity.setAmount(0.0);
            entity.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            entity.setPreInspection(true);
            entity.setRequirement(true);
            entity.setStatus(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION);
            entity.setEventRefNo(grpNo);
            entity.setLicenceId(auditCombinationDto.getAuditTaskDataFillterDto().getLicId());
            entity.setLicenseeId(auditCombinationDto.getAuditTaskDataFillterDto().getLicenseeId());
            setRiskToDto(entity);
        }
        auditCombinationDto.setAppSubmissionDtoList(appSubmissionDtoList);
        log.info("========================>>>>> creat application!!!!");
        try {
            eventBusHelper.submitAsyncRequest(auditCombinationDto,submissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,EventBusConsts.OPERATION_CREATE_AUDIT_TASK,auditCombinationDto.getEventRefNo(),null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }






    private void setRiskToDto(AppSubmissionDto appSubmissionDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = IaisCommonUtils.genNewArrayList();
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
