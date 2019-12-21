package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/19 19:37
 **/
@Delegator("inspectionSendRecDelegator")
@Slf4j
public class InspectionSendRecDelegator {

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private InboxMsgService inboxMsgService;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private SystemBeLicClient systemBeLicClient;

    @Autowired
    private InspectionSendRecDelegator(SystemBeLicClient systemBeLicClient, SystemParamConfig systemParamConfig, FillUpCheckListGetAppClient fillUpCheckListGetAppClient, InboxMsgService inboxMsgService){
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
        List<ApplicationDto> applicationDtos = fillUpCheckListGetAppClient.getApplicationDtoByNcItem().getEntity();
        if(applicationDtos == null || applicationDtos.isEmpty()){
            return;
        }
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        for(ApplicationDto aDto : applicationDtos){
            InterMessageDto interMessageDto = new InterMessageDto();
            interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_SRC_ID);
            interMessageDto.setSubject(MessageConstants.MESSAGE_SUBJECT_REQUEST_FOR_INFORMATION);
            interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
            String mesNO = inboxMsgService.getMessageNo();
            interMessageDto.setRefNo(mesNO);
            interMessageDto.setService_id(aDto.getServiceId());
            String url = systemParamConfig.getInterServerName() +
                    MessageConstants.MESSAGE_CALL_BACK_URL_NEWAPPLICATION +
                    aDto.getApplicationNo();
            interMessageDto.setProcessUrl(url);
            interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            inboxMsgService.saveInterMessage(interMessageDto);
            List<JobRemindMsgTrackingDto> jobRemindMsgTrackingDtos = new ArrayList<>();
            JobRemindMsgTrackingDto jobRemindMsgTrackingDto = new JobRemindMsgTrackingDto();
            jobRemindMsgTrackingDto.setAuditTrailDto(intranet);
            jobRemindMsgTrackingDto.setMsgKey(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
            jobRemindMsgTrackingDto.setRefNo(aDto.getId());
            jobRemindMsgTrackingDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            jobRemindMsgTrackingDto.setId(null);
            jobRemindMsgTrackingDtos.add(jobRemindMsgTrackingDto);
            systemBeLicClient.createJobRemindMsgTrackingDtos(jobRemindMsgTrackingDtos);
        }
    }
}
