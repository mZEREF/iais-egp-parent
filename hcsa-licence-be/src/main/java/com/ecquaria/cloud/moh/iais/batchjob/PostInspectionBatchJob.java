package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicInspectionGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.PostInspectionDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
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
    private EventBusHelper eventBusHelper;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private AuditSystemListService auditSystemListService;
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
            //generate grpNo
            String grpNo = beEicGatewayClient.getAppNo(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION, signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
            List<AppSubmissionDto> appSubmissionDtoList = hcsaLicenceClient.getAppSubmissionDtos(licIds).getEntity();
            for (AppSubmissionDto entity : appSubmissionDtoList) {
                try {
                    auditSystemListService.filetDoc(entity);
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
                    auditSystemListService.setRiskToDto(entity);
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


}
