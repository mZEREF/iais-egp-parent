package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2019/12/19 19:37
 **/
@JobHandler(value="inspectionSendRecJobHandler")
@Component
@Slf4j
public class InspectionSendRecJobHandler extends IJobHandler {
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

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            logAbout("Send Rectification Message To Applicant First");
            List<ApplicationViewDto> mapApp =  fillUpCheckListGetAppClient.getApplicationDtoByNcItem().getEntity();
            if(IaisCommonUtils.isEmpty(mapApp)){
                return ReturnT.SUCCESS;
            }
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            List<AppPremPreInspectionNcDto> appPremPreInspectionNcDtos = IaisCommonUtils.genNewArrayList();
            List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos = IaisCommonUtils.genNewArrayList();
            InspRectificationSaveDto inspRectificationSaveDto = new InspRectificationSaveDto();

            AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
            for(ApplicationViewDto dto : mapApp){
                ApplicationDto aDto = dto.getApplicationDto();
                try {
                    ApplicationGroupDto applicationGroupDto = dto.getApplicationGroupDto();
                    String appPremCorrId = dto.getAppPremisesCorrelationId();
                    JobRemindMsgTrackingDto jobRemindMsgTrackingDto2 = systemBeLicClient.getJobRemindMsgTrackingDto(aDto.getId(), MessageConstants.JOB_REMIND_MSG_KEY_SEND_REC_TO_FE).getEntity();
                    if(jobRemindMsgTrackingDto2 == null) {
                        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(aDto.getServiceId());
                        String serviceCode = hcsaServiceDto.getSvcCode();
                        InterMessageDto interMessageDto = new InterMessageDto();
                        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
                        interMessageDto.setSubject(MessageConstants.MESSAGE_SUBJECT_APPLICANT_RECTIFIES_NC);
                        interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED);
                        String mesNO = inboxMsgService.getMessageNo();
                        interMessageDto.setRefNo(mesNO);
                        interMessageDto.setService_id(serviceCode+'@');
                        interMessageDto.setUserId(applicationGroupDto.getLicenseeId());
                        String url = HmacConstants.HTTPS +"://"+systemParamConfig.getInterServerName() +
                                MessageConstants.MESSAGE_INBOX_URL_USER_UPLOAD_RECTIFICATION + appPremCorrId;
                        HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
                        maskParams.put("appPremCorrId", appPremCorrId);

                        MsgTemplateDto mtd = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_NC_RECTIFICATION).getEntity();
                        Map<String, Object> params = IaisCommonUtils.genNewHashMap();
                        params.put("process_url", url);
                        String licenseeId = dto.getApplicationGroupDto().getLicenseeId();
                        LicenseeDto licDto = licenseeService.getLicenseeDtoById(licenseeId);
                        params.put("applicant_name", StringUtil.viewHtml(licDto.getName()));
                        params.put("hci_code", StringUtil.viewHtml(dto.getHciCode()));
                        params.put("hci_name", StringUtil.viewHtml(dto.getHciName()));
                        params.put("service_name", StringUtil.viewHtml(HcsaServiceCacheHelper.getServiceNameById(aDto.getServiceId())));
                        params.put("application_number", aDto.getApplicationNo());
                        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(mtd.getMessageContent(), params);
                        interMessageDto.setMsgContent(templateMessageByContent);
                        interMessageDto.setStatus(MessageConstants.MESSAGE_STATUS_UNREAD);
                        interMessageDto.setAuditTrailDto(intranet);
                        interMessageDto.setMaskParams(maskParams);
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

                        List<JobRemindMsgTrackingDto> jobRemindMsgTrackingDtos = IaisCommonUtils.genNewArrayList();
                        JobRemindMsgTrackingDto jobRemindMsgTrackingDto = new JobRemindMsgTrackingDto();
                        jobRemindMsgTrackingDto.setAuditTrailDto(intranet);
                        jobRemindMsgTrackingDto.setMsgKey(MessageConstants.JOB_REMIND_MSG_KEY_SEND_REC_TO_FE);
                        jobRemindMsgTrackingDto.setRefNo(aDto.getId());
                        jobRemindMsgTrackingDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                        jobRemindMsgTrackingDto.setId(null);
                        jobRemindMsgTrackingDtos.add(jobRemindMsgTrackingDto);
                        systemBeLicClient.createJobRemindMsgTrackingDtos(jobRemindMsgTrackingDtos);
                    }
                } catch (Exception e) {
                    JobLogger.log(e);
                    log.error(e.getMessage(), e);
                    log.info(StringUtil.changeForLog("Application Id = " + aDto.getId()));
                    JobLogger.log(StringUtil.changeForLog("Application Id = " + aDto.getId()));
                    continue;
                }
            }
            inspRectificationSaveDto.setAppPremPreInspectionNcDtos(appPremPreInspectionNcDtos);
            inspRectificationSaveDto.setAppPremisesPreInspectionNcItemDtos(appPremisesPreInspectionNcItemDtos);
            inspRectificationSaveDto.setAuditTrailDto(intranet);
            beEicGatewayClient.beCreateNcData(inspRectificationSaveDto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }

    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The****" + methodName +" *****Start****"));
        JobLogger.log(StringUtil.changeForLog("****The****" + methodName +" *****Start****"));
    }
}
