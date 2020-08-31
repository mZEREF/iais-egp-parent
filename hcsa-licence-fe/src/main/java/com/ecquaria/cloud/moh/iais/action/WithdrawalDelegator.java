package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.WithdrawApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.memorypage.PaginationHandler;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.WithdrawalService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.ArrayList;
import java.util.Date;
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
    private WithdrawalService withdrawalService;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private ServiceConfigService serviceConfigService;

    @Autowired
    private ApplicationClient applicationClient;



    public void start(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("****The Start Step****"));
        ParamUtil.setSessionAttr(bpc.request,HcsaLicenceFeConstant.DASHBOARDTITLE,null);
        String withdrawAppNo = ParamUtil.getMaskedString(bpc.request, "withdrawAppNo");
        int configFileSize = systemParamConfig.getUploadFileLimit();

        ParamUtil.setSessionAttr(bpc.request,"configFileSize",configFileSize);
   //     String withdrawAppId = ParamUtil.getMaskedString(bpc.request, "withdrawAppId");
        if (!StringUtil.isEmpty(withdrawAppNo)){
            ParamUtil.setSessionAttr(bpc.request, "withdrawAppNo", withdrawAppNo);
        }
        AuditTrailHelper.auditFunction("Withdrawal Application", "Withdrawal Application");
    }

    public void prepareDate(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("****The prepareDate Step****"));
        List<SelectOption> withdrawalReason = IaisCommonUtils.genNewArrayList();
        withdrawalReason.add(new SelectOption("", "Please select a withdrawal reason"));
        withdrawalReason.add(new SelectOption("WDR001", "Duplicate Application"));
        withdrawalReason.add(new SelectOption("WDR002", "Wrong Application"));
        withdrawalReason.add(new SelectOption("WDR003", "Failure to obtain pre requisite licence from other agency(ies)"));
        withdrawalReason.add(new SelectOption("WDR004", "No longer wish to provide the service"));
        withdrawalReason.add(new SelectOption("WDR005", "Others"));
        ParamUtil.setRequestAttr(bpc.request, "withdrawalReasonList", withdrawalReason);
        List<String[]> applicationTandS = new ArrayList<>(41);
        applicationTandS.add(new String[]{"APTY002","APST007"});
        applicationTandS.add(new String[]{"APTY002","APST028"});
        applicationTandS.add(new String[]{"APTY002","APST003"});
        applicationTandS.add(new String[]{"APTY002","APST001"});
        applicationTandS.add(new String[]{"APTY002","APST029"});
        applicationTandS.add(new String[]{"APTY002","APST023"});
        applicationTandS.add(new String[]{"APTY002","APST024"});
        applicationTandS. add(new String[]{"APTY002","APST012"});
        applicationTandS.add(new String[]{"APTY002","APST039"});
        applicationTandS.add(new String[]{"APTY002","APST011"});
        applicationTandS.add(new String[]{"APTY002","APST004"});

        applicationTandS.add(new String[]{"APTY004","APST007"});
        applicationTandS. add(new String[]{"APTY004","APST028"});
        applicationTandS. add(new String[]{"APTY004","APST003"});
        applicationTandS. add(new String[]{"APTY004","APST001"});
        applicationTandS.add(new String[]{"APTY004","APST029"});
        applicationTandS.add(new String[]{"APTY004","APST023"});
        applicationTandS.add(new String[]{"APTY004","APST024"});
        applicationTandS.add(new String[]{"APTY004","APST012"});
        applicationTandS.add(new String[]{"APTY004","APST039"});
        applicationTandS.add(new String[]{"APTY004","APST011"});
        applicationTandS.add(new String[]{"APTY004","APST004"});

        applicationTandS. add(new String[]{"APTY005","APST007"});
        applicationTandS. add(new String[]{"APTY005","APST028"});
        applicationTandS. add(new String[]{"APTY005","APST003"});
        applicationTandS.add(new String[]{"APTY005","APST001"});
        applicationTandS.add(new String[]{"APTY005","APST029"});
        applicationTandS. add(new String[]{"APTY005","APST023"});
        applicationTandS.add(new String[]{"APTY005","APST024"});
        applicationTandS. add(new String[]{"APTY005","APST012"});
        applicationTandS.add(new String[]{"APTY005","APST039"});
        applicationTandS.add(new String[]{"APTY005","APST011"});
        applicationTandS.add(new String[]{"APTY005","APST004"});

        applicationTandS.  add(new String[]{"APTY001","APST007"});
        applicationTandS. add(new String[]{"APTY001","APST002"});
        applicationTandS. add(new String[]{"APTY001","APST012"});
        applicationTandS. add(new String[]{"APTY001","APST038"});
        applicationTandS. add(new String[]{"APTY001","APST011"});
        applicationTandS. add(new String[]{"APTY001","APST023"});
        applicationTandS.add(new String[]{"APTY001","APST039"});
        applicationTandS. add(new String[]{"APTY001","APST011"});

        List<WithdrawApplicationDto> withdrawAppList =  withdrawalService.getCanWithdrawAppList(applicationTandS);
        PaginationHandler<WithdrawApplicationDto> handler = new PaginationHandler<>("withdrawPagDiv", "withdrawBodyDiv");
        handler.setPageSize(5);
        handler.setAllData(withdrawAppList);
        handler.preLoadingPage();
    }

    public void withdrawalStep(BaseProcessClass bpc) throws Exception {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        LoginContext loginContext= (LoginContext)ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        String withdrawnReason = ParamUtil.getRequestString(mulReq, "withdrawalReason");
        String paramAppNos = ParamUtil.getString(mulReq, "withdraw_app_list");
        List<WithdrawnDto> withdrawnDtoList = IaisCommonUtils.genNewArrayList();
        String isValid = IaisEGPConstant.YES;
        if (!StringUtil.isEmpty(paramAppNos)){
            String[] withdrawAppNos = paramAppNos.split("#");
            for (int i =0;i<withdrawAppNos.length;i++){
                WithdrawnDto withdrawnDto = new WithdrawnDto();
                String appNo = withdrawAppNos[i];
                ApplicationDto applicationDto = applicationClient.getApplicationDtoByAppNo(appNo).getEntity();
                String appId = applicationDto.getId();
                CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) mulReq.getFile("selectedFile");
                if(!StringUtil.isEmpty(appId)){
                    withdrawnDto.setApplicationId(appId);
                }
                withdrawnDto.setApplicationNo(appNo);
                withdrawnDto.setLicenseeId(loginContext.getLicenseeId());
                withdrawnDto.setWithdrawnReason(withdrawnReason);
                boolean remarkEmpty = true;
                if ("WDR005".equals(withdrawnReason)){
                    String withdrawnRemarks = ParamUtil.getRequestString(bpc.request, "withdrawnRemarks");
                    withdrawnDto.setWithdrawnRemarks(withdrawnRemarks);
                    if(StringUtil.isEmpty(withdrawnRemarks)){
                        remarkEmpty = false;
                    }
                }
                ValidationResult validationResult = WebValidationHelper.validateProperty(withdrawnDto,"save");
                if(!remarkEmpty){
                    validationResult.addMessage("withdrawnRemarks",MessageUtil.replaceMessage("GENERAL_ERR0006","Remarks","field"));
                }
                if(validationResult != null && validationResult.isHasErrors()){
                    Map<String, String> errorMap = validationResult.retrieveAll();
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                    isValid = IaisEGPConstant.NO;
                    if (commonsMultipartFile !=null && commonsMultipartFile.getSize() > 0){
                        ParamUtil.setRequestAttr(bpc.request,"file_upload_withdraw",commonsMultipartFile.getFileItem().getName());
                    }
                    ParamUtil.setRequestAttr(bpc.request,"withdrawDtoView",withdrawnDto);
                }else{
                    if (commonsMultipartFile != null && commonsMultipartFile.getSize() > 0 ){
                        String fileRepoId = serviceConfigService.saveFileToRepo(commonsMultipartFile);
                        AppPremisesSpecialDocDto appPremisesSpecialDocDto = new AppPremisesSpecialDocDto();
                        appPremisesSpecialDocDto.setSubmitDt(new Date());
                        appPremisesSpecialDocDto.setSubmitBy(loginContext.getUserId());
                        appPremisesSpecialDocDto.setDocName(commonsMultipartFile.getOriginalFilename());
                        appPremisesSpecialDocDto.setDocSize((int)commonsMultipartFile.getSize() / 1024);
                        appPremisesSpecialDocDto.setMd5Code(FileUtil.genMd5FileChecksum(commonsMultipartFile.getBytes()));
                        withdrawnDto.setAppPremisesSpecialDocDto(appPremisesSpecialDocDto);
                        withdrawnDto.setFileRepoId(fileRepoId);
                    }
                    withdrawnDtoList.add(withdrawnDto);
                }
            }
            withdrawalService.saveWithdrawn(withdrawnDtoList);
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID,isValid);
    }

    public void saveDateStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("****The saveDateStep Step****"));
    }
}
