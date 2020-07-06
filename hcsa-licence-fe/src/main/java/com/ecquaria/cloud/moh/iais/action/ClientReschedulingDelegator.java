package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschApptGrpPremsQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRectificationSaveDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ReschedulingApptUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.service.ApptConfirmReSchDateService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeMessageClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ClientReschedulingDelegator
 *
 * @author junyu
 * @date 2020/6/18
 */
@Slf4j
@Delegator("clientReschedulingDelegator")
public class ClientReschedulingDelegator {
    @Autowired
    ApptConfirmReSchDateService rescheduleService;
    private FilterParameter rescheduleParameter = new FilterParameter.Builder()
            .clz(ReschApptGrpPremsQueryDto.class)
            .searchAttr("SearchParam")
            .resultAttr("SearchResult")
            .sortField("ADDRESS").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(10).build();

    @Autowired
    SystemParamConfig systemParamConfig;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private FeMessageClient feMessageClient;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    @Autowired
    LicenceViewService licenceViewService;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private LicEicClient licEicClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Value("${iais.email.sender}")
    private String mailSender;
    public void start(BaseProcessClass bpc)  {

    }

    public void init(BaseProcessClass bpc)  {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        SearchParam rescheduleParam = IaisEGPHelper.getSearchParam(bpc.request, true,rescheduleParameter);
        StringBuilder stringBuilder=new StringBuilder();
        for (String appSt: ReschedulingApptUtil.APP_STATUS_NO_RESCHEDULE
             ) {
            stringBuilder.append(appSt).append(',');
        }
        rescheduleParam.addParam("appStatus_reschedule", "(app.status not in('"+stringBuilder.toString()+"'))");
        rescheduleParam.addParam("RESCHEDULE_COUNT", "(insAppt.RESCHEDULE_COUNT <"+systemParamConfig.getRescheduleMaxCount()+")");
        Date dueDate;
        Date tomorrow;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,systemParamConfig.getRescheduleDateRange());
        dueDate =calendar.getTime();
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        tomorrow =calendar.getTime();
        rescheduleParam.addParam("RECOM_IN_DATE", "( appRec.RECOM_IN_DATE >= convert(datetime,'"+ Formatter.formatDateTime(tomorrow, SystemAdminBaseConstants.DATE_FORMAT)+"') AND appRec.RECOM_IN_DATE <= convert(datetime,'"+Formatter.formatDateTime(dueDate, SystemAdminBaseConstants.DATE_FORMAT)+"'))");
        rescheduleParam.addFilter("licenseeId", licenseeId, true);
        QueryHelp.setMainSql("rescheduleQuery", "queryApptGrpPremises", rescheduleParam);

        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);


        try {
            SearchResult<ReschApptGrpPremsQueryDto> result  = feEicGatewayClient.eicSearchApptReschPrem(rescheduleParam, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getEntity();

            List<ReschApptGrpPremsQueryDto> rows = result.getRows();
            if(rows!=null){
                LinkedHashMap<String ,ApptViewDto> apptViewDtos= new LinkedHashMap<>(rows.size());

                ParamUtil.setRequestAttr(bpc.request,"SearchResult",result);
                for (ReschApptGrpPremsQueryDto reschApptGrpPremsQueryDto : rows) {
                    ApptViewDto apptViewDto=new ApptViewDto();
                    List<String> svcIds=IaisCommonUtils.genNewArrayList();
                    String viewCorrId=reschApptGrpPremsQueryDto.getAppGrpId()+reschApptGrpPremsQueryDto.getPremisesAddress();
                    if("1".equals(reschApptGrpPremsQueryDto.getFastTracking())){
                        viewCorrId=viewCorrId+reschApptGrpPremsQueryDto.getId();
                    }
                    if(apptViewDtos.get(viewCorrId)==null){
                        svcIds.add(reschApptGrpPremsQueryDto.getSvcId());
                    }else {
                        apptViewDtos.get(viewCorrId).getSvcIds().add(reschApptGrpPremsQueryDto.getSvcId());
                        svcIds=apptViewDtos.get(viewCorrId).getSvcIds();
                    }
                    apptViewDto.setSvcIds(svcIds);
                    apptViewDto.setVehicleNo(reschApptGrpPremsQueryDto.getVehicleNo());
                    apptViewDto.setAppId(reschApptGrpPremsQueryDto.getId());
                    apptViewDto.setAppCorrId(reschApptGrpPremsQueryDto.getAppCorrId());
                    apptViewDto.setLicenseeId(reschApptGrpPremsQueryDto.getLicenseeId());
                    apptViewDto.setAddress(reschApptGrpPremsQueryDto.getAddress());
                    apptViewDto.setInspStartDate(reschApptGrpPremsQueryDto.getRecomInDate());
                    apptViewDto.setFastTracking(reschApptGrpPremsQueryDto.getFastTracking());
                    apptViewDto.setViewCorrId(viewCorrId);
                    apptViewDtos.put(viewCorrId,apptViewDto);
                }
                List<ApptViewDto> apptViewDtos1=IaisCommonUtils.genNewArrayList();
                for (Map.Entry<String,ApptViewDto> entry:apptViewDtos.entrySet()
                     ) {
                    apptViewDtos1.add(entry.getValue());
                }
                ParamUtil.setRequestAttr(bpc.request, "apptViewDtos", apptViewDtos1);
                ParamUtil.setSessionAttr(bpc.request, "apptViewDtosMap", apptViewDtos);
            }
            ParamUtil.setRequestAttr(bpc.request,"SearchParam",rescheduleParam);
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }

    }

    public void doSort(BaseProcessClass bpc)  {
        SearchResultHelper.doSort(bpc.request,rescheduleParameter);
    }
    public void doPage(BaseProcessClass bpc)  {SearchResultHelper.doPage(bpc.request,rescheduleParameter);
    }

    public void doReschedule(BaseProcessClass bpc)  {}

    public void preCommPool(BaseProcessClass bpc)  {
        String [] keyIds=ParamUtil.getStrings(bpc.request,"appIds");
        Map<String ,ApptViewDto> apptViewDtos= (Map<String, ApptViewDto>) ParamUtil.getSessionAttr(bpc.request,"apptViewDtosMap");

        List<ApptViewDto> apptViewDtos1=IaisCommonUtils.genNewArrayList();
        for (String key: keyIds
             ) {
            ApptViewDto apptViewDto=apptViewDtos.get(key);
            apptViewDtos1.add(apptViewDto);


            String serviceId = apptViewDto.getSvcIds().get(0);
            if (!StringUtil.isEmpty(serviceId)){
                Map<String, Object> msgInfoMap = IaisCommonUtils.genNewHashMap();
                String licenseeName=licenceViewService.getLicenseeDtoBylicenseeId(apptViewDto.getLicenseeId()).getName();
                msgInfoMap.put("Applicant",licenseeName);
                msgInfoMap.put("Appointment Date",apptViewDto.getInspStartDate());
                msgInfoMap.put("MOH_NAME",AppConsts.MOH_AGENCY_NAME);
                try {
                    EmailDto emailDto = sendNotification(MsgTemplateConstants.MSG_TEMPLATE_RESCHEDULING_SUCCESSFULLY,msgInfoMap,apptViewDto.getApplicationNo(),apptViewDto.getLicenseeId());
                    sendInboxMessage(apptViewDto.getLicenseeId(),null,emailDto.getContent(),serviceId,emailDto.getSubject());
                } catch (IOException | TemplateException e) {
                    log.error(e.getMessage(), e);
                }
            }

        }
        rescheduleService.updateAppStatusCommPool(apptViewDtos1);

    }

    private void sendInboxMessage( String licenseeId, HashMap<String, String> maskParams, String templateMessageByContent, String serviceId, String subject){
        InterMessageDto interMessageDto = new InterMessageDto();
        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
        interMessageDto.setSubject(subject);
        interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String refNo = feEicGatewayClient.getMessageNo(signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
        interMessageDto.setRefNo(refNo);
        interMessageDto.setService_id(serviceId);
        interMessageDto.setUserId(licenseeId);
        interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        interMessageDto.setMsgContent(templateMessageByContent);
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        interMessageDto.setMaskParams(maskParams);
        feMessageClient.createInboxMessage(interMessageDto);
    }

    private EmailDto sendNotification(String msgId, Map<String, Object> msgInfoMap, String applicationNo, String licenseeId) throws IOException, TemplateException {
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(msgId).getEntity();
        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), msgInfoMap);
        EmailDto emailDto=new EmailDto();
        emailDto.setClientQueryCode(applicationNo);
        emailDto.setSender(mailSender);
        emailDto.setContent(templateMessageByContent);
        emailDto.setSubject(msgTemplateDto.getTemplateName()+applicationNo);
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
        if(licenseeEmailAddrs!=null){
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            emailDto.setReceipts(licenseeEmailAddrs);
            try {
                EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.WithdrawalServiceImpl", "sendNotification",
                        "hcsa-licence-web-internet", InspRectificationSaveDto.class.getName(), JsonUtil.parseToJson(emailDto));
                String eicRefNo = eicRequestTrackingDto.getRefNo();
                feEicGatewayClient.feSendEmail(emailDto,signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization());
                //get eic record
                eicRequestTrackingDto = licEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
                //update eic record status
                eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
                eicRequestTrackingDtos.add(eicRequestTrackingDto);
                licEicClient.updateStatus(eicRequestTrackingDtos);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }
        return emailDto;
    }

}
