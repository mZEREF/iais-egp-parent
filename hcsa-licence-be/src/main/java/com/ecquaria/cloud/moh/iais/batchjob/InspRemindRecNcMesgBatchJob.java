package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.List;

/**
 * @Process MohRemindRecNcMesg
 *
 * @author Shicheng
 * @date 2020/4/29 13:59
 **/
@Delegator("remindRecNcMesgBatchJob")
@Slf4j
public class InspRemindRecNcMesgBatchJob {

    @Autowired
    private InspEmailService inspEmailService;

    @Autowired
    private EmailClient emailClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    /**
     * StartStep: remindRecNcMesgStart
     *
     * @param bpc
     * @throws
     */
    public void remindRecNcMesgStart(BaseProcessClass bpc){
        logAbout("Remind Applicant Rectify N/C");
    }

    /**
     * StartStep: remindRecNcMesgStep
     *
     * @param bpc
     * @throws
     */
    public void remindRecNcMesgStep(BaseProcessClass bpc){
        logAbout("Remind Applicant Rectify N/C Do");
        List<ApplicationDto> applicationDtos = applicationClient.getApplicationByStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION).getEntity();
        if(IaisCommonUtils.isEmpty(applicationDtos)){
            return;
        }
        for(ApplicationDto applicationDto : applicationDtos){
            ApplicationGroupDto applicationGroupDto = inspectionTaskClient.getApplicationGroupDtoByAppGroId(applicationDto.getAppGrpId()).getEntity();
            String licenseeId = applicationGroupDto.getLicenseeId();
            inspectionDateSendEmail(licenseeId, applicationDto.getId());
        }
    }

    private void inspectionDateSendEmail(String licenseeId, String appId) {
        InspectionEmailTemplateDto inspectionEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_REMIND_NC_RECTIFICATION);
        if(inspectionEmailTemplateDto != null) {
            String mesContext;
            try {
                mesContext = MsgUtil.getTemplateMessageByContent(inspectionEmailTemplateDto.getMessageContent(), null);
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(), e);
                throw new IaisRuntimeException(e);
            }
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject(inspectionEmailTemplateDto.getSubject());
            emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(appId);
            emailClient.sendNotification(emailDto);
        }
    }

    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The*****" + methodName +"******Start****"));
    }
}
