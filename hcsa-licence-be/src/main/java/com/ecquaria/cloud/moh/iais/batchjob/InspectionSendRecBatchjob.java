package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRectificationSaveDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.LicenseeService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Shicheng
 * @date 2019/12/19 19:37
 **/
@Delegator("inspectionSendRecDelegator")
@Slf4j
public class InspectionSendRecBatchjob {
    @Autowired
    private LicenseeService licenseeService;

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

    @Autowired
    private MsgTemplateClient msgTemplateClient;

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
    public void mohInspecSendRectifiToUserPre(BaseProcessClass bpc) throws IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the mohInspecSendRectifiToUserPre start ...."));
        List<ApplicationViewDto> mapApp =  fillUpCheckListGetAppClient.getApplicationDtoByNcItem().getEntity();
        if(IaisCommonUtils.isEmpty(mapApp)){
            return;
        }
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        List<AppPremPreInspectionNcDto> appPremPreInspectionNcDtos = new ArrayList<>();
        List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos = new ArrayList<>();
        InspRectificationSaveDto inspRectificationSaveDto = new InspRectificationSaveDto();

        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        for(ApplicationViewDto dto : mapApp){
            ApplicationDto aDto = dto.getApplicationDto();
            String appPremCorrId = dto.getAppPremisesCorrelationId();
            JobRemindMsgTrackingDto jobRemindMsgTrackingDto2 = systemBeLicClient.getJobRemindMsgTrackingDto(aDto.getId(), MessageConstants.JOB_REMIND_MSG_KEY_SEND_REC_TO_FE).getEntity();
            if(jobRemindMsgTrackingDto2 == null) {
                InterMessageDto interMessageDto = new InterMessageDto();
                interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
                interMessageDto.setSubject(MessageConstants.MESSAGE_SUBJECT_APPLICANT_RECTIFIES_NC);
                interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
                String mesNO = inboxMsgService.getMessageNo();
                interMessageDto.setRefNo(mesNO);
                interMessageDto.setService_id(aDto.getServiceId());
                interMessageDto.setUserId(intranet.getMohUserGuid());
                String url = HmacConstants.HTTPS +"://"+systemParamConfig.getInterServerName() +
                             MessageConstants.MESSAGE_INBOX_URL_USER_UPLOAD_RECTIFICATION + appPremCorrId;
                MsgTemplateDto mtd = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_NC_RECTIFICATION).getEntity();
                Map<String, Object> params = new HashMap<>();
                params.put("process_url", url);
                LicenseeDto licDto = licenseeService.getLicenseeDtoById(dto.getApplicationGroupDto().getLicenseeId());
                params.put("applicant_name", StringUtil.viewHtml(licDto.getName()));
                params.put("hci_code", StringUtil.viewHtml(dto.getHciCode()));
                params.put("hci_name", StringUtil.viewHtml(dto.getHciName()));
                params.put("service_name", StringUtil.viewHtml(HcsaServiceCacheHelper.getServiceNameById(aDto.getServiceId())));
                params.put("application_number", aDto.getApplicationNo());
                String templateMessageByContent = MsgUtil.getTemplateMessageByContent(mtd.getMessageContent(), params);
                interMessageDto.setMsgContent(templateMessageByContent);
                interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                interMessageDto.setAuditTrailDto(intranet);
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
