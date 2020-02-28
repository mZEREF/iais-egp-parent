package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRectificationSaveDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2019/12/19 19:37
 **/
@Delegator("inspectionSendRecDelegator")
@Slf4j
public class InspectionSendRecBatchjob {

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private InboxMsgService inboxMsgService;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private SystemBeLicClient systemBeLicClient;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Autowired
    private InspectionSendRecBatchjob(SystemBeLicClient systemBeLicClient, SystemParamConfig systemParamConfig, FillUpCheckListGetAppClient fillUpCheckListGetAppClient, InboxMsgService inboxMsgService){
        this.fillUpCheckListGetAppClient = fillUpCheckListGetAppClient;
        this.inboxMsgService = inboxMsgService;
        this.systemParamConfig = systemParamConfig;
        this.systemBeLicClient = systemBeLicClient;
    }
    /**
     * StartStep: mohInspecSendRectifiToUserStart
     *
     * @param bpc
     * @throws
     */
    public void mohInspecSendRectifiToUserStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohInspecSendRectifiToUserStart start ...."));
    }

    /**
     * StartStep: mohInspecSendRectifiToUserPre
     *
     * @param bpc
     * @throws
     */
    public void mohInspecSendRectifiToUserPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohInspecSendRectifiToUserPre start ...."));
        Map<String, ApplicationDto> mapApp =  fillUpCheckListGetAppClient.getApplicationDtoByNcItem().getEntity();
        if(mapApp == null || mapApp.isEmpty()){
            return;
        }
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        List<AppPremPreInspectionNcDto> appPremPreInspectionNcDtos = new ArrayList<>();
        List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos = new ArrayList<>();
        InspRectificationSaveDto inspRectificationSaveDto = new InspRectificationSaveDto();

        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        for(Map.Entry<String, ApplicationDto> map : mapApp.entrySet()){
            ApplicationDto aDto = map.getValue();
            String appPremCorrId = map.getKey();
            JobRemindMsgTrackingDto jobRemindMsgTrackingDto2 = systemBeLicClient.getJobRemindMsgTrackingDto(aDto.getId(), MessageConstants.JOB_REMIND_MSG_KEY_SEND_REC_TO_FE).getEntity();
            if(jobRemindMsgTrackingDto2 == null) {
                InterMessageDto interMessageDto = new InterMessageDto();
                interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
                interMessageDto.setSubject(MessageConstants.MESSAGE_SUBJECT_REQUEST_FOR_INFORMATION);
                interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
                String mesNO = inboxMsgService.getMessageNo();
                interMessageDto.setRefNo(mesNO);
                interMessageDto.setService_id(aDto.getServiceId());
                String url = systemParamConfig.getInterServerName() +
                        MessageConstants.MESSAGE_INBOX_URL_USER_UPLOAD_RECTIFICATION +
                        appPremCorrId;
                interMessageDto.setProcessUrl(url);
                interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                inboxMsgService.saveInterMessage(interMessageDto);
                aDto.setAuditTrailDto(intranet);
                aDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION);
                applicationViewService.updateApplicaiton(aDto);
                applicationService.updateFEApplicaiton(aDto);

                AppPremPreInspectionNcDto appPremPreInspectionNcDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appPremCorrId).getEntity();
                appPremPreInspectionNcDtos.add(appPremPreInspectionNcDto);
                List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtoList = fillUpCheckListGetAppClient.getAppNcItemByNcId(appPremPreInspectionNcDto.getId()).getEntity();
                if(!IaisCommonUtils.isEmpty(appPremisesPreInspectionNcItemDtoList)){
                    for(AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto : appPremisesPreInspectionNcItemDtoList){
                        appPremisesPreInspectionNcItemDtos.add(appPremisesPreInspectionNcItemDto);
                    }
                }

                List<JobRemindMsgTrackingDto> jobRemindMsgTrackingDtos = new ArrayList<>();
                JobRemindMsgTrackingDto jobRemindMsgTrackingDto = new JobRemindMsgTrackingDto();
                jobRemindMsgTrackingDto.setAuditTrailDto(intranet);
                jobRemindMsgTrackingDto.setMsgKey(MessageConstants.JOB_REMIND_MSG_KEY_SEND_REC_TO_FE);
                jobRemindMsgTrackingDto.setRefNo(aDto.getId());
                jobRemindMsgTrackingDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                jobRemindMsgTrackingDto.setId(null);
                jobRemindMsgTrackingDtos.add(jobRemindMsgTrackingDto);
                systemBeLicClient.createJobRemindMsgTrackingDtos(jobRemindMsgTrackingDtos);
            }
        }
        inspRectificationSaveDto.setAppPremPreInspectionNcDtos(appPremPreInspectionNcDtos);
        inspRectificationSaveDto.setAppPremisesPreInspectionNcItemDtos(appPremisesPreInspectionNcItemDtos);
        inspRectificationSaveDto.setAuditTrailDto(intranet);
        beEicGatewayClient.beCreateNcData(inspRectificationSaveDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }
}
