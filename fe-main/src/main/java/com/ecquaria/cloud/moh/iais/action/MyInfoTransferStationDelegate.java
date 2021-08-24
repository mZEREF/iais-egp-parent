package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.myinfo.MyInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.model.MyinfoUtil;
import java.security.NoSuchAlgorithmException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * FeAdminManageDelegate
 *
 * @author wangyu
 * @date 2021/8/2
 */
@Delegator("MyinfoTransferStationDelegator")
@Slf4j
public class MyInfoTransferStationDelegate {

    @Autowired
    private MyInfoAjax myInfoAjax;
    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        log.debug("****doStart Process ****");
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_USER_MANAGEMENT, AuditTrailConsts.FUNCTION_USER_MANAGEMENT);
    }


    /**
     * StartStep: transmit
     *
     * @param bpc
     * @throws
     */
    public void transmit(BaseProcessClass bpc) throws NoSuchAlgorithmException {
        log.debug("****doStart Process ****");
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_USER_MANAGEMENT, AuditTrailConsts.FUNCTION_USER_MANAGEMENT);
        HttpServletRequest request = bpc.request;
        String nric =(String) ParamUtil.getSessionAttr(request,MyinfoUtil.CALL_MYINFO_PROCESS_SESSION_NAME_NRIC);
        if(StringUtil.isNotEmpty(nric)){
             String callPrcoessUrl ="https://"+request.getServerName()+"/main-web/eservice/INTERNET/";
             callPrcoessUrl += (String) ParamUtil.getSessionAttr(request,MyinfoUtil.CALL_MYINFO_PROCESS_SESSION_NAME+"_"+ nric);
             MyInfoDto myInfoDto = myInfoAjax.noTakenCallMyInfo(bpc,callPrcoessUrl,nric);
             if(myInfoDto != null && !myInfoDto.isServiceDown()){
                 ParamUtil.setSessionAttr(request,MyinfoUtil.CALL_MYINFO_DTO_SEESION+"_"+ nric,myInfoDto);
             }
            ParamUtil.setSessionAttr(request,MyinfoUtil.MYINFO_TRANSFER_CALL_BACK,AppConsts.YES);
            try{
                IaisEGPHelper.redirectUrl(bpc.response,callPrcoessUrl);
            } catch (IOException ioe){
                log.error(ioe.getMessage(),ioe);
            }
        }else {
            log.info("-----------------------------call back nric is null-------------------");
        }
    }
}
