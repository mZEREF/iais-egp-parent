package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspSetMaskValueDto;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicantConfirmInspDateService;
import com.ecquaria.cloud.moh.iais.service.InspecUserRecUploadService;
import com.ecquaria.cloud.moh.iais.service.SubmitInspectionDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @Process MohFeTcuAuditApptPreDate
 *
 * @author Shicheng
 * @date 2021/7/27 15:53
 **/
@Delegator(value = "tcuAuditApptPreDateDelegator")
@Slf4j
public class TcuAuditApptPreDateDelegator {

    @Autowired
    private SubmitInspectionDate submitInspectionDate;

    @Autowired
    private ApplicantConfirmInspDateService applicantConfirmInspDateService;

    @Autowired
    private InspecUserRecUploadService inspecUserRecUploadService;

    private static final String INSPSETMASKVALUEDTO = "inspSetMaskValueDto";
    private static final String INDICATE_PREFERRED_INSPECTION_DATE = "Indicate Preferred Inspection Date";
    private static final String INSPSTARTDATE = "inspStartDate";
    private static final String INSPENDDATE = "inspEndDate";

    /**
     * StartStep: feTcuAuditApptPreDateStart
     *
     * @param bpc
     * @throws
     */
    public void feTcuAuditApptPreDateStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the feTcuAuditApptPreDateStart start ...."));
        ParamUtil.setSessionAttr(bpc.request, INSPSETMASKVALUEDTO, null);
        String applicationNo = "";
        try{
            applicationNo = ParamUtil.getMaskedString(bpc.request, "applicationNo");
        }catch (MaskAttackException e){
            log.error(e.getMessage(), e);
            try{
                IaisEGPHelper.redirectUrl(bpc.response, "https://"+bpc.request.getServerName()+"/hcsa-licence-web/CsrfErrorPage.jsp");
            } catch (IOException ioe){
                log.error(ioe.getMessage(), ioe);
                return;
            }
        }
        String messageId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        InspSetMaskValueDto inspSetMaskValueDto = new InspSetMaskValueDto();
        inspSetMaskValueDto.setApplicationNo(applicationNo);
        ParamUtil.setSessionAttr(bpc.request, INSPSETMASKVALUEDTO, inspSetMaskValueDto);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_APPOINTMENT, AuditTrailConsts.FUNCTION_TCU_AUDIT_APPT_PRE_DATE);
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID, messageId);
    }

    /**
     * StartStep: feTcuAuditApptPreDateInit
     *
     * @param bpc
     * @throws
     */
    public void feTcuAuditApptPreDateInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the feTcuAuditApptPreDateInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.DASHBOARDTITLE, null);
        ParamUtil.setSessionAttr(bpc.request, "tcuAuditApptPreDateFlag", null);
    }

    /**
     * StartStep: feTcuAuditApptPreDatePre
     *
     * @param bpc
     * @throws
     */
    public void feTcuAuditApptPreDatePre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the feTcuAuditApptPreDatePre start ...."));
        InspSetMaskValueDto inspSetMaskValueDto = (InspSetMaskValueDto)ParamUtil.getSessionAttr(bpc.request, INSPSETMASKVALUEDTO);
        String apptPreDateFlag = applicantConfirmInspDateService.getTcuAuditApptPreDateFlag(inspSetMaskValueDto);
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.DASHBOARDTITLE,INDICATE_PREFERRED_INSPECTION_DATE);
        ParamUtil.setSessionAttr(bpc.request, "tcuAuditApptPreDateFlag", apptPreDateFlag);
    }

    /**
     * StartStep: feTcuAuditApptPreDateStep
     *
     * @param bpc
     * @throws
     */
    public void feTcuAuditApptPreDateStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the feTcuAuditApptPreDateStep start ...."));
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.DASHBOARDTITLE,INDICATE_PREFERRED_INSPECTION_DATE);
    }

    /**
     * StartStep: feTcuAuditApptPreDateVali
     *
     * @param bpc
     * @throws
     */
    public void feTcuAuditApptPreDateVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the feTcuAuditApptPreDateVali start ...."));
        String startDate = ParamUtil.getRequestString(bpc.request, INSPSTARTDATE);
        String endDate = ParamUtil.getRequestString(bpc.request, INSPENDDATE);
        ParamUtil.setRequestAttr(bpc.request, INSPSTARTDATE, startDate);
        ParamUtil.setRequestAttr(bpc.request, INSPENDDATE, endDate);

        //do validate
        Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
        if(StringUtil.isEmpty(startDate)) {
            errorMap.put(INSPSTARTDATE, "GENERAL_ERR0006");
        }
        if(StringUtil.isEmpty(endDate)) {
            errorMap.put(INSPENDDATE, "GENERAL_ERR0006");
        }
        if(!StringUtil.isEmpty(startDate) && !StringUtil.isEmpty(endDate)) {
            Date sDate = IaisEGPHelper.parseToDate(startDate, AppConsts.DEFAULT_DATE_FORMAT);
            Date eDate = IaisEGPHelper.parseToDate(endDate, AppConsts.DEFAULT_DATE_FORMAT);
            if (!IaisEGPHelper.isAfterDate(new Date(), sDate)){
                errorMap.put("dateError", "UC_INSTA004_ERR007");
            } else if (!IaisEGPHelper.isAfterDateSecond(sDate, eDate)) {
                errorMap.put(INSPSTARTDATE, "UC_INSP_ACK019");
            }
        }

        if(errorMap != null && !(errorMap.isEmpty())){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
        } else {
            ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.TRUE);
        }
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.DASHBOARDTITLE,INDICATE_PREFERRED_INSPECTION_DATE);
    }

    /**
     * StartStep: feTcuAuditApptPreDateSubmit
     *
     * @param bpc
     * @throws
     */
    public void feTcuAuditApptPreDateSubmit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the feTcuAuditApptPreDateSubmit start ...."));
        InspSetMaskValueDto inspSetMaskValueDto = (InspSetMaskValueDto)ParamUtil.getSessionAttr(bpc.request, INSPSETMASKVALUEDTO);
        String startDate = (String) ParamUtil.getRequestAttr(bpc.request, INSPSTARTDATE);
        String endDate = (String) ParamUtil.getRequestAttr(bpc.request,INSPENDDATE);
        if(!StringUtil.isEmpty(startDate) && !StringUtil.isEmpty(endDate)) {
            Date sDate = IaisEGPHelper.parseToDate(startDate, AppConsts.DEFAULT_DATE_FORMAT);
            Date eDate = IaisEGPHelper.parseToDate(endDate, AppConsts.DEFAULT_DATE_FORMAT);
            String groupId = applicantConfirmInspDateService.getAppGroupIdByMaskValueDto(inspSetMaskValueDto);
            submitInspectionDate.submitInspStartDateAndEndDate(groupId, sDate, eDate);
            String messageId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
            inspecUserRecUploadService.updateMessageStatus(messageId, MessageConstants.MESSAGE_STATUS_RESPONSE);
        }
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.DASHBOARDTITLE,INDICATE_PREFERRED_INSPECTION_DATE);
    }
}
