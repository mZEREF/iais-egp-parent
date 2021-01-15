package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
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
import java.util.ListIterator;
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
        AuditTrailHelper.setupBatchJobAuditTrail(this);
    }



    public void doBatchJob(BaseProcessClass bpc) {
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        log.debug(StringUtil.changeForLog("The prepareCeaastion is do ..."));
        jobExecute();
    }

    public void jobExecute(){
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        Map<String, List<String>> map = hcsaLicenceClient.getPostInspectionMap().getEntity();
        log.debug(StringUtil.changeForLog("=============map size================" + map.size()));
        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
        map.forEach((insGrpId, licIds) -> {
            List<String> insGrpIds = IaisCommonUtils.genNewArrayList();
            List<AppSubmissionDto> appSubmissionDtos = IaisCommonUtils.genNewArrayList();
            String grpNo = beEicGatewayClient.getAppNo(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION, signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
            List<AppSubmissionDto> appSubmissionDtoList = hcsaLicenceClient.getAppSubmissionDtos(licIds).getEntity();
            for (AppSubmissionDto entity : appSubmissionDtoList) {
                try {
                    filetDoc(entity);
                    entity.setAppGrpNo(grpNo);
                    entity.setAppType(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION);
                    entity.setAmount(0.0);
                    entity.setAuditTrailDto(auditTrailDto);
                    entity.setPreInspection(false);
                    entity.setRequirement(true);
                    entity.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT);
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
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    continue;
                }
            }
            insGrpIds.add(insGrpId);
            List<LicInspectionGroupDto> licInspectionGroupDtos = hcsaLicenceClient.getLicInsGrpByIds(insGrpIds).getEntity();
            PostInspectionDto postInspectionDto = new PostInspectionDto();
            String submissionId = generateIdClient.getSeqId().getEntity();
            postInspectionDto.setSubmissionDtos(appSubmissionDtos);
            postInspectionDto.setLicInspectionGroupDtos(licInspectionGroupDtos);
            postInspectionDto.setEventRefNo(grpNo);
            log.debug(StringUtil.changeForLog("=============event bus start ================"));
            //app
            eventBusHelper.submitAsyncRequest(postInspectionDto, submissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                    EventBusConsts.OPERATION_POST_INSPECTION_APP_LIC, grpNo, null);
            //lic
            eventBusHelper.submitAsyncRequest(postInspectionDto, submissionId, EventBusConsts.SERVICE_NAME_LICENCESAVE,
                    EventBusConsts.OPERATION_POST_INSPECTION_APP_LIC, grpNo, null);
            log.debug(StringUtil.changeForLog("=============  event bus end ================"));
        });
    }

    private void setRiskToDto(AppSubmissionDto appSubmissionDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = IaisCommonUtils.genNewArrayList();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setApptype(appSubmissionDto.getAppType());
            riskAcceptiionDtoList.add(riskAcceptiionDto);
        }

        List<RiskResultDto> riskResultDtoList = hcsaConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();

        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            String serviceCode = appSvcRelatedInfoDto.getServiceCode();
            RiskResultDto riskResultDto = getRiskResultDtoByServiceCode(riskResultDtoList, serviceCode);
            if (riskResultDto != null) {
                appSvcRelatedInfoDto.setScore(riskResultDto.getScore());
                appSvcRelatedInfoDto.setDoRiskDate(riskResultDto.getDoRiskDate());
            }
        }
    }


    private RiskResultDto getRiskResultDtoByServiceCode(List<RiskResultDto> riskResultDtoList, String serviceCode) {
        if (riskResultDtoList == null || StringUtil.isEmpty(serviceCode)) {
            return null;
        }
        for (RiskResultDto riskResultDto : riskResultDtoList) {
            if (serviceCode.equals(riskResultDto.getSvcCode())) {
                return riskResultDto;
            }
        }
        return null;
    }

    private void filetDoc(AppSubmissionDto appSubmissionDto){
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)){
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
                List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                if(!IaisCommonUtils.isEmpty(appSvcDocDtoLit)){
                    ListIterator<AppSvcDocDto> appSvcDocDtoListIterator = appSvcDocDtoLit.listIterator();
                    while (appSvcDocDtoListIterator.hasNext()){
                        AppSvcDocDto appSvcDocDto = appSvcDocDtoListIterator.next();
                        String fileRepoId = appSvcDocDto.getFileRepoId();
                        String svcDocId = appSvcDocDto.getSvcDocId();
                        if(StringUtil.isEmpty(fileRepoId)||StringUtil.isEmpty(svcDocId)){
                            appSvcDocDtoListIterator.remove();
                        }
                    }
                }
            }
        }
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)){
            ListIterator<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoListIterator = appGrpPrimaryDocDtos.listIterator();
            while (appGrpPrimaryDocDtoListIterator.hasNext()){
                AppGrpPrimaryDocDto next = appGrpPrimaryDocDtoListIterator.next();
                String fileRepoId = next.getFileRepoId();
                String svcDocId = next.getSvcDocId();
                if(StringUtil.isEmpty(fileRepoId)||StringUtil.isEmpty(svcDocId)){
                    appGrpPrimaryDocDtoListIterator.remove();
                }
            }
        }
    }
}
