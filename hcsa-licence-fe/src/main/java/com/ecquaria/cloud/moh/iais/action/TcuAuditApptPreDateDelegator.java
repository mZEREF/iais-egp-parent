package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspSetMaskValueDto;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.SubmitInspectionDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;

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

    /**
     * StartStep: feTcuAuditApptPreDateStart
     *
     * @param bpc
     * @throws
     */
    public void feTcuAuditApptPreDateStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the feTcuAuditApptPreDateStart start ...."));
        ParamUtil.setSessionAttr(bpc.request, "inspSetMaskValueDto", null);
        String appGroupId = "";
        try{
            appGroupId = ParamUtil.getMaskedString(bpc.request, "appGroupId");
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
        inspSetMaskValueDto.setAppGroupId(appGroupId);
        ParamUtil.setSessionAttr(bpc.request, "inspSetMaskValueDto", inspSetMaskValueDto);
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

    }

    /**
     * StartStep: feTcuAuditApptPreDatePre
     *
     * @param bpc
     * @throws
     */
    public void feTcuAuditApptPreDatePre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the feTcuAuditApptPreDatePre start ...."));

    }

    /**
     * StartStep: feTcuAuditApptPreDateStep
     *
     * @param bpc
     * @throws
     */
    public void feTcuAuditApptPreDateStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the feTcuAuditApptPreDateStep start ...."));

    }

    /**
     * StartStep: feTcuAuditApptPreDateVali
     *
     * @param bpc
     * @throws
     */
    public void feTcuAuditApptPreDateVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the feTcuAuditApptPreDateVali start ...."));

    }

    /**
     * StartStep: feTcuAuditApptPreDateSubmit
     *
     * @param bpc
     * @throws
     */
    public void feTcuAuditApptPreDateSubmit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the feTcuAuditApptPreDateSubmit start ...."));

    }
}
