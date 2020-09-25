package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicInspectionGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.PostInspectionDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

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
    private BeEicGatewayClient beEicGatewayClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private GenerateIdClient generateIdClient;

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
//        map.clear();
//        String inGrpId1 = "10F81292-D8E8-EA11-8B79-000C293F0C99";
//        String inGrpId2 = "A3368785-F8EB-EA11-8B79-000C293F0C99";
//        List<String> licIds1 = IaisCommonUtils.genNewArrayList();
//        licIds1.add("6BC15A13-BEFD-EA11-8B79-000C293F0C99");
//        licIds1.add("7AC15A13-BEFD-EA11-8B79-000C293F0C99");
//        List<String> licIds2 = IaisCommonUtils.genNewArrayList();
//        licIds2.add("54C15A13-BEFD-EA11-8B79-000C293F0C99");
//        licIds2.add("49C15A13-BEFD-EA11-8B79-000C293F0C99");
//        map.put(inGrpId1,licIds1);
//        map.put(inGrpId2,licIds2);
        map.forEach((insGrpId, licIds) -> {
            List<String> insGrpIds = IaisCommonUtils.genNewArrayList();
            List<AppSubmissionDto> appSubmissionDtos = IaisCommonUtils.genNewArrayList();
            String grpNo = beEicGatewayClient.getAppNo(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION, signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
            List<AppSubmissionDto> appSubmissionDtoList = hcsaLicenceClient.getAppSubmissionDtos(licIds).getEntity();
            for(AppSubmissionDto entity : appSubmissionDtoList){
                try{
                    entity.setAppGrpNo(grpNo);
                    entity.setAppType(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION);
                    entity.setAmount(0.0);
                    entity.setAuditTrailDto(auditTrailDto);
                    entity.setPreInspection(true);
                    entity.setRequirement(true);
                    entity.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION);
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
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                    continue;
                }
            }
            //applicationClient.savePostSubmision(appSubmissionDtos).getEntity();
            insGrpIds.add(insGrpId);
            List<LicInspectionGroupDto> licInspectionGroupDtos = hcsaLicenceClient.getLicInsGrpByIds(insGrpIds).getEntity();
            //hcsaLicenceClient.testLicence(licInspectionGroupDtos).getEntity();
            PostInspectionDto postInspectionDto = new PostInspectionDto();
            String submissionId = generateIdClient.getSeqId().getEntity();
            postInspectionDto.setSubmissionDtos(appSubmissionDtos);
            postInspectionDto.setLicInspectionGroupDtos(licInspectionGroupDtos);
            postInspectionDto.setEventRefNo(grpNo);
            log.debug(StringUtil.changeForLog("=============event bus start ================"));
            //app
            eventBusHelper.submitAsyncRequest(postInspectionDto, submissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                    EventBusConsts.OPERATION_POST_INSPECTION_APP_LIC, grpNo, bpc.process);
//            //licence
            eventBusHelper.submitAsyncRequest(postInspectionDto, submissionId, EventBusConsts.SERVICE_NAME_LICENCESAVE,
                    EventBusConsts.OPERATION_POST_INSPECTION_APP_LIC, grpNo, bpc.process);
            log.debug(StringUtil.changeForLog("=============  event bus end ================"));
        });
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
        if(riskResultDtoList == null || StringUtil.isEmpty(serviceCode)){
            return null;
        }
        for(RiskResultDto riskResultDto : riskResultDtoList){
            if(serviceCode.equals(riskResultDto.getSvcCode())){
                return riskResultDto ;
            }
        }
        return null;
    }
}
