package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.FEEicGatewayClient;
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
import java.util.List;
import java.util.Map;

/**
 * SendsReminderToReplyRfiDelegator
 *
 * @author junyu
 * @date 2020/1/8
 */
@Delegator("sendsReminderToReplyRfiDelegator")
@Slf4j
public class SendsReminderToReplyRfiBatchjob {
    @Autowired
    RequestForInformationService requestForInformationService;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    private FEEicGatewayClient gatewayClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    public void start(BaseProcessClass bpc){
        logAbout("start");
    }

    public void sendMsg(BaseProcessClass bpc) throws IOException, TemplateException {
        logAbout("sendMsg");
        List<LicPremisesReqForInfoDto> licPremisesReqForInfoDtos= requestForInformationService.getAllReqForInfo();
        for (LicPremisesReqForInfoDto rfi:licPremisesReqForInfoDtos
             ) {
            if(rfi.getDueDateSubmission().compareTo(new Date())>0&&rfi.getUserReply()==null){
                reminder(rfi);
            }
        }

    }

    private void reminder(LicPremisesReqForInfoDto licPremisesReqForInfoDto) throws IOException, TemplateException {
        String templateId="BEFC2AF0-250C-EA11-BE78-000C29D29DB0";
        InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(templateId);
        String licenseeId=requestForInformationService.getLicPreReqForInfo(licPremisesReqForInfoDto.getReqInfoId()).getLicenseeId();
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
        Map<String,Object> map=new HashMap<>();
        map.put("APPLICANT_NAME",licenseeDto.getName());
        map.put("DETAILS",licPremisesReqForInfoDto.getOfficerRemarks());
        map.put("MOH_NAME", AppConsts.MOH_AGENCY_NAME);
        String mesContext= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getMessageContent(),map);
        licPremisesReqForInfoDto.setReminder(licPremisesReqForInfoDto.getReminder()+1);
        Calendar cal = Calendar.getInstance();
        cal.setTime(licPremisesReqForInfoDto.getDueDateSubmission());
        cal.add(Calendar.DAY_OF_MONTH, 7);
        licPremisesReqForInfoDto.setDueDateSubmission(cal.getTime());
        LicPremisesReqForInfoDto licPremisesReqForInfoDto1 = requestForInformationService.updateLicPremisesReqForInfo(licPremisesReqForInfoDto);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        licPremisesReqForInfoDto1.setAction("update");
        gatewayClient.createLicPremisesReqForInfoFe(licPremisesReqForInfoDto1, signature.date(), signature.authorization()).getEntity();
    }
    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }
}
