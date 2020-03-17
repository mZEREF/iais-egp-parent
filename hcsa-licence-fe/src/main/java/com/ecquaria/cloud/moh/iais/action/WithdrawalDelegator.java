package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.CessationService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.sz.commons.util.FileUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2020-02-11 18:04
 **/
@Slf4j
@Delegator("withdrawalDelegator")
public class WithdrawalDelegator {

    @Autowired
    private CessationService cessationService;

    @Autowired
    private ServiceConfigService serviceConfigService;

    private String appId;

    public void start(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("****The Start Step****"));
        AccessUtil.initLoginUserInfo(bpc.request);
        AuditTrailHelper.auditFunction("Withdrawal Application", "Withdrawal Application");
    }

    public void prepareDate(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("****The prepareDate Step****"));
        List<SelectOption> withdrawalReason = IaisCommonUtils.genNewArrayList();
        withdrawalReason.add(new SelectOption("WDR001", "Duplicate Application"));
        withdrawalReason.add(new SelectOption("WDR002", "Wrong Application"));
        withdrawalReason.add(new SelectOption("WDR003", "Failure to obtain pre requisite licence from other agency(ies)"));
        withdrawalReason.add(new SelectOption("WDR004", "No longer wish to provide the service"));
        withdrawalReason.add(new SelectOption("WDR005", "Others"));
        appId = ParamUtil.getString(bpc.request, "appId");
        ParamUtil.setSessionAttr(bpc.request, "appNo", ParamUtil.getRequestString(bpc.request, "appNo"));
        ParamUtil.setRequestAttr(bpc.request, "withdrawalReasonList", withdrawalReason);
    }

    public void withdrawalStep(BaseProcessClass bpc) throws Exception {
        WithdrawnDto withdrawnDto = new WithdrawnDto();
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        LoginContext loginContext= (LoginContext)ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
//        String appId = ParamUtil.getString(bpc.request, "appId");
        String withdrawnReason = ParamUtil.getRequestString(mulReq, "withdrawalReason");
        CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) mulReq.getFile("selectedFile");
        withdrawnDto.setApplicationId(appId);
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
            String fileRepoId = serviceConfigService.saveFileToRepo(commonsMultipartFile);
            AppPremisesSpecialDocDto appPremisesSpecialDocDto = new AppPremisesSpecialDocDto();
            appPremisesSpecialDocDto.setSubmitDt(new Date());
            appPremisesSpecialDocDto.setSubmitBy(loginContext.getUserId());
            appPremisesSpecialDocDto.setDocName(commonsMultipartFile.getOriginalFilename());
            appPremisesSpecialDocDto.setDocSize((int)commonsMultipartFile.getSize() / 1024);
            appPremisesSpecialDocDto.setMd5Code(FileUtil.genMd5FileChecksum(commonsMultipartFile.getBytes()));
            withdrawnDto.setAppPremisesSpecialDocDto(appPremisesSpecialDocDto);
            withdrawnDto.setFileRepoId(fileRepoId);
            cessationService.saveWithdrawn(withdrawnDto);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
        }
    }

    public void saveDateStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("****The saveDateStep Step****"));
    }

    private void sendTaskToASO(){

    }
}
