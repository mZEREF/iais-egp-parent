package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2020-02-11 18:04
 **/
@Slf4j
@Delegator("withdrawalDelegator")
public class WithdrawalDelegator {

    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private AppConfigClient appConfigClient;

    public void start(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("****The Start Step****"));
        AuditTrailHelper.auditFunction("Withdrawal Application", "Withdrawal Application");
    }

    public void prepareDate(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("****The prepareDate Step****"));
        ApplicationDto applicationDto = applicationClient.getApplicationById("CADC4DDF-E71B-EA11-BE78-000C29D29DB0").getEntity();
        applicationDto.setServiceId(appConfigClient.getServiceNameById(applicationDto.getServiceId()).getEntity());
        List<SelectOption> withdrawalReason = new ArrayList<>();
        withdrawalReason.add(new SelectOption("Duplicate Application", "Duplicate Application"));
        withdrawalReason.add(new SelectOption("Wrong Application", "Wrong Application"));
        withdrawalReason.add(new SelectOption("Failure ", "Failure to obtain pre requisite licence from other agency(ies)"));
        withdrawalReason.add(new SelectOption("No longer wish to provide the service", "No longer wish to provide the service"));
        withdrawalReason.add(new SelectOption("Others", "Others"));
        ParamUtil.setRequestAttr(bpc.request, "withdrawalReason", withdrawalReason);
        ParamUtil.setSessionAttr(bpc.request, "applicationDto", applicationDto);
    }

    public void withdrawalStep(BaseProcessClass bpc){
        WithdrawnDto withdrawnDto = new WithdrawnDto();
        String withdrawnReason = ParamUtil.getRequestString(bpc.request, "withdrawnReason");
        withdrawnDto.setWithdrawnReason(withdrawnReason);
        if ("Others".equals(withdrawnReason)){
            String withdrawnRemarks = ParamUtil.getRequestString(bpc.request, "withdrawnRemarks");
            withdrawnDto.setWithdrawnRemarks(withdrawnRemarks);
        }
        ValidationResult validationResult = WebValidationHelper.validateProperty(withdrawnDto,"save");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
        }else{
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
        }
    }

    public void uploadStep(BaseProcessClass bpc){

    }

    public void prepareUpload(BaseProcessClass bpc){

    }
}
