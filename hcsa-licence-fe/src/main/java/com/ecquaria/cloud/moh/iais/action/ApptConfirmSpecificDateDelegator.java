package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspSetMaskValueDto;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicantConfirmInspDateService;
import com.ecquaria.cloud.moh.iais.service.InspecUserRecUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;

/**
 * @Process MohUserConfirmSpecificDate
 *
 * @author Shicheng
 * @date 2020/2/18 10:16
 **/
@Delegator(value = "userConfirmSpecificDateDelegator")
@Slf4j
public class ApptConfirmSpecificDateDelegator {
    @Autowired
    private ApplicantConfirmInspDateService applicantConfirmInspDateService;

    @Autowired
    private InspecUserRecUploadService inspecUserRecUploadService;

    @Autowired
    private ApptConfirmSpecificDateDelegator(InspecUserRecUploadService inspecUserRecUploadService, ApplicantConfirmInspDateService applicantConfirmInspDateService){
        this.applicantConfirmInspDateService = applicantConfirmInspDateService;
        this.inspecUserRecUploadService = inspecUserRecUploadService;
    }
    /**
     * StartStep: userConfirmSpecificDateStart
     *
     * @param bpc
     * @throws
     */
    public void userConfirmSpecificDateStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmSpecificDateStart start ...."));
        String applicationNo = "";
        try{
            applicationNo = ParamUtil.getMaskedString(bpc.request, "applicationNo");
        }catch (MaskAttackException e){
            log.error(e.getMessage(), e);
            try{
                bpc.response.sendRedirect("https://"+bpc.request.getServerName()+"/hcsa-licence-web/CsrfErrorPage.jsp");
            } catch (IOException ioe){
                log.error(ioe.getMessage(), ioe);
                return;
            }
        }
        String appPremCorrId = applicantConfirmInspDateService.getAppPremCorrIdByAppNo(applicationNo);
        String messageId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        InspSetMaskValueDto inspSetMaskValueDto = new InspSetMaskValueDto();
        inspSetMaskValueDto.setAppPremCorrId(appPremCorrId);
        ParamUtil.setSessionAttr(bpc.request, "inspSetMaskValueDto", inspSetMaskValueDto);
        AuditTrailHelper.auditFunction("Appointment Confirm Specific Date", "Appointment Confirm Specific Date");
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID, messageId);
    }

    /**
     * StartStep: userConfirmSpecificDateInit
     *
     * @param bpc
     * @throws
     */
    public void userConfirmSpecificDateInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmSpecificDateInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", null);
        ParamUtil.setSessionAttr(bpc.request, "apptInspFlag", null);
        ParamUtil.setSessionAttr(bpc.request, "appType", null);
        ParamUtil.setSessionAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE,null);
    }

    /**
     * StartStep: userConfirmSpecificDatePre
     *
     * @param bpc
     * @throws
     */
    public void userConfirmSpecificDatePre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmSpecificDatePre start ...."));
        ApptFeConfirmDateDto apptFeConfirmDateDto = (ApptFeConfirmDateDto) ParamUtil.getSessionAttr(bpc.request, "apptFeConfirmDateDto");
        InspSetMaskValueDto inspSetMaskValueDto = (InspSetMaskValueDto)ParamUtil.getSessionAttr(bpc.request, "inspSetMaskValueDto");
        if(apptFeConfirmDateDto == null){
            String appPremCorrId = inspSetMaskValueDto.getAppPremCorrId();
            if(!StringUtil.isEmpty(appPremCorrId)) {
                ApplicationDto applicationDto = inspecUserRecUploadService.getApplicationByCorrId(appPremCorrId);
                String appStatus = applicationDto.getStatus();
                if(ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING.equals(appStatus)){
                    apptFeConfirmDateDto = applicantConfirmInspDateService.getSpecificDateDto(appPremCorrId);
                    apptFeConfirmDateDto.setCTaskUrl(TaskConsts.TASK_PROCESS_URL_APPT_INSPECTION_DATE);
                    String appType = apptFeConfirmDateDto.getApplicationDtos().get(0).getApplicationType();
                    ParamUtil.setSessionAttr(bpc.request, "apptInspFlag", AppConsts.FALSE);
                    ParamUtil.setSessionAttr(bpc.request, "appType", appType);
                } else {
                    ParamUtil.setSessionAttr(bpc.request, "apptInspFlag", AppConsts.SUCCESS);
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", apptFeConfirmDateDto);
        ParamUtil.setSessionAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE,"Inspector Assigns Specific Date");
    }

    /**
     * StartStep: userConfirmSpecificDateStep
     *
     * @param bpc
     * @throws
     */
    public void userConfirmSpecificDateStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmSpecificDateStep start ...."));
    }

    /**
     * StartStep: userConfirmSpecificDateAcc
     *
     * @param bpc
     * @throws
     */
    public void userConfirmSpecificDateAcc(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmSpecificDateAcc start ...."));
        ApptFeConfirmDateDto apptFeConfirmDateDto = (ApptFeConfirmDateDto) ParamUtil.getSessionAttr(bpc.request, "apptFeConfirmDateDto");
        applicantConfirmInspDateService.saveAccSpecificDate(apptFeConfirmDateDto);
        String messageId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        inspecUserRecUploadService.updateMessageStatus(messageId, MessageConstants.MESSAGE_STATUS_RESPONSE);
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", apptFeConfirmDateDto);
    }

    /**
     * StartStep: userConfirmSpecificDateReject
     *
     * @param bpc
     * @throws
     */
    public void userConfirmSpecificDateReject(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmSpecificDateReject start ...."));
        ApptFeConfirmDateDto apptFeConfirmDateDto = (ApptFeConfirmDateDto) ParamUtil.getSessionAttr(bpc.request, "apptFeConfirmDateDto");
        applicantConfirmInspDateService.rejectSpecificDate(apptFeConfirmDateDto);
        String messageId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        inspecUserRecUploadService.updateMessageStatus(messageId, MessageConstants.MESSAGE_STATUS_RESPONSE);
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", apptFeConfirmDateDto);
    }
}
