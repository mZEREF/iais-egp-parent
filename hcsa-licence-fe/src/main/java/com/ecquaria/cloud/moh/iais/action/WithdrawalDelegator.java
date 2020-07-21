package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.WithdrawApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.memorypage.PaginationHandler;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.WithdrawalService;
import com.ecquaria.sz.commons.util.FileUtil;

import java.util.ArrayList;
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
    private WithdrawalService withdrawalService;

    @Autowired
    private ServiceConfigService serviceConfigService;

    private String withdrawAppId;

    private String withdrawAppNo;

    public void start(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("****The Start Step****"));
        withdrawAppNo = ParamUtil.getMaskedString(bpc.request, "withdrawAppNo");
        withdrawAppId = ParamUtil.getMaskedString(bpc.request, "withdrawAppId");
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
        List<String[]> applicationTandS = new ArrayList<String[]>(){
            {
                add(new String[]{"APTY002","APST007"});
                add(new String[]{"APTY002","APST028"});
                add(new String[]{"APTY002","APST003"});
                add(new String[]{"APTY002","APST001"});
                add(new String[]{"APTY002","APST029"});
                add(new String[]{"APTY002","APST023"});
                add(new String[]{"APTY002","APST024"});
                add(new String[]{"APTY002","APST012"});
                add(new String[]{"APTY002","APST039"});
                add(new String[]{"APTY002","APST011"});
                add(new String[]{"APTY002","APST004"});

                add(new String[]{"APTY004","APST007"});
                add(new String[]{"APTY004","APST028"});
                add(new String[]{"APTY004","APST003"});
                add(new String[]{"APTY004","APST001"});
                add(new String[]{"APTY004","APST029"});
                add(new String[]{"APTY004","APST023"});
                add(new String[]{"APTY004","APST024"});
                add(new String[]{"APTY004","APST012"});
                add(new String[]{"APTY004","APST039"});
                add(new String[]{"APTY004","APST011"});
                add(new String[]{"APTY004","APST004"});

                add(new String[]{"APTY005","APST007"});
                add(new String[]{"APTY005","APST028"});
                add(new String[]{"APTY005","APST003"});
                add(new String[]{"APTY005","APST001"});
                add(new String[]{"APTY005","APST029"});
                add(new String[]{"APTY005","APST023"});
                add(new String[]{"APTY005","APST024"});
                add(new String[]{"APTY005","APST012"});
                add(new String[]{"APTY005","APST039"});
                add(new String[]{"APTY005","APST011"});
                add(new String[]{"APTY005","APST004"});

                add(new String[]{"APTY001","APST007"});
                add(new String[]{"APTY001","APST002"});
                add(new String[]{"APTY001","APST012"});
                add(new String[]{"APTY001","APST038"});
                add(new String[]{"APTY001","APST011"});
                add(new String[]{"APTY001","APST023"});
                add(new String[]{"APTY001","APST039"});
                add(new String[]{"APTY001","APST011"});
            }
        };
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
        if (!StringUtil.isEmpty(paramAppNos)){
            String[] withdrawAppNos = paramAppNos.split("#");
            for (int i =0;i<withdrawAppNos.length;i++){
                WithdrawnDto withdrawnDto = new WithdrawnDto();
                String appNo = withdrawAppNos[i];
                CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) mulReq.getFile("selectedFile");
                withdrawnDto.setApplicationNo(appNo);
                withdrawnDto.setLicenseeId(loginContext.getLicenseeId());
                withdrawnDto.setWithdrawnReason(withdrawnReason);
                if ("WDR005".equals(withdrawnReason)){
                    String withdrawnRemarks = ParamUtil.getRequestString(bpc.request, "withdrawnRemarks");
                    withdrawnDto.setWithdrawnRemarks(withdrawnRemarks);
                }
                ValidationResult validationResult = WebValidationHelper.validateProperty(withdrawnDto,"save");
                if(validationResult != null && validationResult.isHasErrors()){
                    Map<String, String> errorMap = validationResult.retrieveAll();
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
                    if (commonsMultipartFile !=null){
                        ParamUtil.setRequestAttr(bpc.request,"file_upload_withdraw",commonsMultipartFile.getFileItem().getName());
                    }
                    ParamUtil.setRequestAttr(bpc.request,"withdrawDtoView",withdrawnDto);
                }else{
                    if (commonsMultipartFile != null){
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
        }
        withdrawalService.saveWithdrawn(withdrawnDtoList);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
    }

    public void saveDateStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("****The saveDateStep Step****"));
    }
}
