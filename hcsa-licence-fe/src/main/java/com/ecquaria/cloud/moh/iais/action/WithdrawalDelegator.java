package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.WithdrawApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
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
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.LicFeInboxClient;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
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
    private ApplicationFeClient applicationFeClient;

    @Autowired
    private LicFeInboxClient licFeInboxClient;

    private LoginContext loginContext = null;

    private String wdIsValid = IaisEGPConstant.YES;

    public void start(BaseProcessClass bpc){
        loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        log.debug(StringUtil.changeForLog("****The Start Step****"));
        ParamUtil.setSessionAttr(bpc.request,HcsaLicenceFeConstant.DASHBOARDTITLE,null);
        String withdrawAppNo = ParamUtil.getMaskedString(bpc.request, "withdrawAppNo");
        String isDoView = ParamUtil.getString(bpc.request, "isDoView");
        ParamUtil.setSessionAttr(bpc.request, "isDoView", isDoView);
        int configFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setSessionAttr(bpc.request, "withdrawDtoView", null);
        ParamUtil.setSessionAttr(bpc.request,"configFileSize",configFileSize);


   //     String withdrawAppId = ParamUtil.getMaskedString(bpc.request, "withdrawAppId");
        ApplicationDto entity=null;
        if (!StringUtil.isEmpty(withdrawAppNo)){
            ParamUtil.setSessionAttr(bpc.request, "withdrawAppNo", withdrawAppNo);
            entity = applicationFeClient.getApplicationDtoByAppNo(withdrawAppNo).getEntity();
        }
        String rfiWithdrawAppNo = ParamUtil.getMaskedString(bpc.request,"rfiWithdrawAppNo");
        if(entity!=null){
            String status = entity.getStatus();
            if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(status)){
                isDoView="N";
                rfiWithdrawAppNo= withdrawAppNo;
            }
        }
        // just view, so direct return
        if ("Y".equals(isDoView)){
            WithdrawnDto withdrawnDto = withdrawalService.getWithdrawAppInfo(withdrawAppNo);
            ParamUtil.setSessionAttr(bpc.request, "withdrawAppNo", withdrawnDto.getPrevAppNo());
            AppPremisesSpecialDocDto viewDoc = withdrawnDto.getAppPremisesSpecialDocDto();
            if (viewDoc != null){
                ParamUtil.setRequestAttr(bpc.request,"file_upload_withdraw",viewDoc.getDocName());
            }

            ParamUtil.setSessionAttr(bpc.request, "withdrawDtoView", withdrawnDto);
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", "");
            return;
        }

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_WITHDRAWAL, AuditTrailConsts.FUNCTION_WITHDRAWAL);
        if (!StringUtil.isEmpty(rfiWithdrawAppNo)){
            WithdrawnDto withdrawnDto = withdrawalService.getWithdrawAppInfo(rfiWithdrawAppNo);
            ParamUtil.setSessionAttr(bpc.request, "rfiWithdrawAppNo", rfiWithdrawAppNo);
            ParamUtil.setSessionAttr(bpc.request, "rfiWithdrawDto", withdrawnDto);
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", "doRfi");
        }else{
            ParamUtil.setSessionAttr(bpc.request, "rfiWithdrawDto", null);
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", "");
        }
    }

    public void prepareDate(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("****The prepareDate Step****"));
        List<SelectOption> withdrawalReason = IaisCommonUtils.genNewArrayList();
        withdrawalReason.add(new SelectOption("", "Please Select"));
        withdrawalReason.add(new SelectOption("WDR001", "Duplicate Application"));
        withdrawalReason.add(new SelectOption("WDR002", "Wrong Application"));
        withdrawalReason.add(new SelectOption("WDR003", "Failure to obtain pre requisite licence from other agency(ies)"));
        withdrawalReason.add(new SelectOption("WDR004", "No longer wish to provide the service"));
        withdrawalReason.add(new SelectOption("WDR005", "Others"));
        ParamUtil.setRequestAttr(bpc.request, "withdrawalReasonList", withdrawalReason);
        List<String[]> applicationTandS = new ArrayList<>(43);
        applicationTandS.add(new String[]{"APTY002","APST007"});
        applicationTandS.add(new String[]{"APTY002","APST028"});
        applicationTandS.add(new String[]{"APTY002","APST003"});
        applicationTandS.add(new String[]{"APTY002","APST004"});
        applicationTandS.add(new String[]{"APTY002","APST001"});
        applicationTandS.add(new String[]{"APTY002","APST029"});
        applicationTandS.add(new String[]{"APTY002","APST023"});
        applicationTandS.add(new String[]{"APTY002","APST024"});
        applicationTandS.add(new String[]{"APTY002","APST012"});
        applicationTandS.add(new String[]{"APTY002","APST039"});
        applicationTandS.add(new String[]{"APTY002","APST011"});
        applicationTandS.add(new String[]{"APTY002","APST071"});
        applicationTandS.add(new String[]{"APTY002","APST069"});
        applicationTandS.add(new String[]{"APTY002","APST040"});
        applicationTandS.add(new String[]{"APTY002","APST077"});
        applicationTandS.add(new String[]{"APTY002","APST019"});
        applicationTandS.add(new String[]{"APTY002","APST031"});
        applicationTandS.add(new String[]{"APTY002","APST020"});
        applicationTandS.add(new String[]{"APTY002","APST021"});
        applicationTandS.add(new String[]{"APTY002","APST022"});
        applicationTandS.add(new String[]{"APTY002","APST037"});
        applicationTandS.add(new String[]{"APTY002","APST067"});
        applicationTandS.add(new String[]{"APTY002","APST032"});
        applicationTandS.add(new String[]{"APTY002","APST033"});
        applicationTandS.add(new String[]{"APTY002","APST034"});
        applicationTandS.add(new String[]{"APTY002","APST092"});
        applicationTandS.add(new String[]{"APTY002","APST001"});
        applicationTandS.add(new String[]{"APTY002","APST001"});
        applicationTandS.add(new String[]{"APTY002","APST049"});

        applicationTandS.add(new String[]{"APTY004","APST007"});
        applicationTandS.add(new String[]{"APTY004","APST028"});
        applicationTandS.add(new String[]{"APTY004","APST003"});
        applicationTandS.add(new String[]{"APTY004","APST004"});
        applicationTandS.add(new String[]{"APTY004","APST001"});
        applicationTandS.add(new String[]{"APTY004","APST029"});
        applicationTandS.add(new String[]{"APTY004","APST023"});
        applicationTandS.add(new String[]{"APTY004","APST024"});
        applicationTandS.add(new String[]{"APTY004","APST012"});
        applicationTandS.add(new String[]{"APTY004","APST039"});
        applicationTandS.add(new String[]{"APTY004","APST011"});
        applicationTandS.add(new String[]{"APTY004","APST004"});
        applicationTandS.add(new String[]{"APTY004","APST077"});
        applicationTandS.add(new String[]{"APTY004","APST019"});
        applicationTandS.add(new String[]{"APTY004","APST031"});
        applicationTandS.add(new String[]{"APTY004","APST020"});
        applicationTandS.add(new String[]{"APTY004","APST021"});
        applicationTandS.add(new String[]{"APTY004","APST022"});
        applicationTandS.add(new String[]{"APTY004","APST037"});
        applicationTandS.add(new String[]{"APTY004","APST067"});
        applicationTandS.add(new String[]{"APTY004","APST032"});
        applicationTandS.add(new String[]{"APTY004","APST033"});
        applicationTandS.add(new String[]{"APTY004","APST034"});
        applicationTandS.add(new String[]{"APTY004","APST092"});
        applicationTandS.add(new String[]{"APTY004","APST001"});
        applicationTandS.add(new String[]{"APTY004","APST049"});

        applicationTandS.add(new String[]{"APTY005","APST007"});
        applicationTandS.add(new String[]{"APTY005","APST028"});
        applicationTandS.add(new String[]{"APTY005","APST003"});
        applicationTandS.add(new String[]{"APTY005","APST004"});
        applicationTandS.add(new String[]{"APTY005","APST001"});
        applicationTandS.add(new String[]{"APTY005","APST029"});
        applicationTandS.add(new String[]{"APTY005","APST023"});
        applicationTandS.add(new String[]{"APTY005","APST024"});
        applicationTandS.add(new String[]{"APTY005","APST012"});
        applicationTandS.add(new String[]{"APTY005","APST039"});
        applicationTandS.add(new String[]{"APTY005","APST011"});
        applicationTandS.add(new String[]{"APTY005","APST004"});
        applicationTandS.add(new String[]{"APTY005","APST077"});
        applicationTandS.add(new String[]{"APTY005","APST019"});
        applicationTandS.add(new String[]{"APTY005","APST031"});
        applicationTandS.add(new String[]{"APTY005","APST020"});
        applicationTandS.add(new String[]{"APTY005","APST021"});
        applicationTandS.add(new String[]{"APTY005","APST022"});
        applicationTandS.add(new String[]{"APTY005","APST037"});
        applicationTandS.add(new String[]{"APTY005","APST067"});
        applicationTandS.add(new String[]{"APTY005","APST032"});
        applicationTandS.add(new String[]{"APTY005","APST033"});
        applicationTandS.add(new String[]{"APTY005","APST034"});
        applicationTandS.add(new String[]{"APTY005","APST092"});
        applicationTandS.add(new String[]{"APTY005","APST001"});
        applicationTandS.add(new String[]{"APTY005","APST049"});

        applicationTandS.add(new String[]{"APTY001","APST007"});
        applicationTandS.add(new String[]{"APTY001","APST002"});
        applicationTandS.add(new String[]{"APTY001","APST012"});
        applicationTandS.add(new String[]{"APTY001","APST038"});
        applicationTandS.add(new String[]{"APTY001","APST011"});
        applicationTandS.add(new String[]{"APTY001","APST023"});
        applicationTandS.add(new String[]{"APTY001","APST039"});
        applicationTandS.add(new String[]{"APTY001","APST011"});
        applicationTandS.add(new String[]{"APTY001","APST004"});
        applicationTandS.add(new String[]{"APTY001","APST067"});
        applicationTandS.add(new String[]{"APTY001","APST032"});
        applicationTandS.add(new String[]{"APTY001","APST033"});
        applicationTandS.add(new String[]{"APTY001","APST034"});
        applicationTandS.add(new String[]{"APTY001","APST092"});
        applicationTandS.add(new String[]{"APTY001","APST001"});
        applicationTandS.add(new String[]{"APTY001","APST049"});

        List<WithdrawApplicationDto> withdrawAppList =  withdrawalService.getCanWithdrawAppList(applicationTandS,loginContext.getLicenseeId());

        String applicationNo =  (String)ParamUtil.getSessionAttr(bpc.request, "withdrawAppNo");
        if(withdrawAppList != null && withdrawAppList.size() > 0){
            if(StringUtil.isEmpty(applicationNo)){
                applicationNo = (String)ParamUtil.getSessionAttr(bpc.request, "rfiWithdrawAppNo");
            }
            String finalApplicationNo = applicationNo;
            if (!StringUtil.isEmpty(finalApplicationNo)){
                withdrawAppList.removeIf(h -> finalApplicationNo.equals(h.getApplicationNo()));
            }
        }

        PaginationHandler<WithdrawApplicationDto> handler = new PaginationHandler<>("withdrawPagDiv", "withdrawBodyDiv");
        handler.setAllData(withdrawAppList);
        handler.preLoadingPage();

    }

    public void withdrawalStep(BaseProcessClass bpc) throws Exception {
        wdIsValid = IaisEGPConstant.YES;
        List<WithdrawnDto> withdrawnDtoList = getWithdrawAppList(bpc);
        if ((withdrawnDtoList != null) && (withdrawnDtoList.size() > 0) && IaisEGPConstant.YES.equals(wdIsValid)){
            String replaceStr = "";
            StringBuilder sb = new StringBuilder();
            withdrawnDtoList.forEach(h -> sb.append(h.getApplicationNo()).append(','));
            if (sb.toString().length() > 1){
                replaceStr = sb.toString().substring(0,sb.toString().length() - 1);
            }
            String ackMsg = MessageUtil.replaceMessage("WDL_ACK001",replaceStr,"Application No");
            ParamUtil.setRequestAttr(bpc.request,"WITHDRAW_ACKMSG",ackMsg);
            withdrawalService.saveWithdrawn(withdrawnDtoList,bpc.request);
        }
    }

    public void withdrawDoRfi(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("****The withdrawDoRfi Step****"));
        wdIsValid = IaisEGPConstant.YES;
        String messageId = (String)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        List<WithdrawnDto> withdrawnDtoList = getWithdrawAppList(bpc);
        if ((withdrawnDtoList != null) && (withdrawnDtoList.size() > 0) && IaisEGPConstant.YES.equals(wdIsValid)){
//            withdrawalService.saveWithdrawn(withdrawnDtoList);
            String replaceStr = "";
            StringBuilder sb = new StringBuilder();
            withdrawnDtoList.forEach(h -> sb.append(h.getApplicationNo()).append(','));
            if (sb.toString().length() > 1){
                replaceStr = sb.toString().substring(0,sb.toString().length() - 1);
            }
            String ackMsg = MessageUtil.replaceMessage("WDL_ACK001",replaceStr,"Application No");
            ParamUtil.setRequestAttr(bpc.request,"WITHDRAW_ACKMSG",ackMsg);
            withdrawalService.saveRfiWithdrawn(withdrawnDtoList,bpc.request);
            if (!StringUtil.isEmpty(messageId)){
                licFeInboxClient.updateMsgStatusTo(messageId, MessageConstants.MESSAGE_STATUS_RESPONSE);
            }
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID,wdIsValid);
    }

    public void prepareRfiDate(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("****The saveDateStep Step****"));
        prepareDate(bpc);
        ParamUtil.setSessionAttr(bpc.request, "withdrawAppNo",null);
        WithdrawnDto withdrawnDto = (WithdrawnDto) ParamUtil.getSessionAttr(bpc.request, "rfiWithdrawDto");
        withdrawnDto.setLicenseeId(loginContext.getLicenseeId());
        ParamUtil.setSessionAttr(bpc.request, "rfiWithdrawDto", withdrawnDto);
        String messageId = (String)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        InterMessageDto interMessageById = licFeInboxClient.getInterMessageById(messageId).getEntity();
        if (MessageConstants.MESSAGE_STATUS_RESPONSE.equals(interMessageById.getStatus())){
            ParamUtil.setRequestAttr(bpc.request, "rfi_already_err",MessageUtil.getMessageDesc("INBOX_ERR001"));
        }
        ApplicationDto applicationDto = applicationFeClient.getApplicationById(withdrawnDto.getApplicationId()).getEntity();
        String serviceId = applicationDto.getServiceId();
        HcsaServiceDto serviceDtoById = serviceConfigService.getServiceDtoById(serviceId);
        bpc.request.setAttribute("rfiServiceName",serviceDtoById.getSvcName());
    }

    public void saveDateStep(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("****The saveDateStep Step****"));
    }

    private List<WithdrawnDto> getWithdrawAppList(BaseProcessClass bpc) throws IOException {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String withdrawnReason = ParamUtil.getRequestString(mulReq, "withdrawalReason");
        String paramAppNos = ParamUtil.getString(mulReq, "withdraw_app_list");
        List<WithdrawnDto> withdrawnDtoList = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(paramAppNos)){
            String[] withdrawAppNos = paramAppNos.split("#");
            for (int i =0;i<withdrawAppNos.length;i++){
                WithdrawnDto withdrawnDto = (WithdrawnDto) ParamUtil.getSessionAttr(bpc.request, "rfiWithdrawDto");
                if (withdrawnDto == null){
                    withdrawnDto = new WithdrawnDto();
                }
                String appNo = withdrawAppNos[i];
                ApplicationDto applicationDto = applicationFeClient.getApplicationDtoByAppNo(appNo).getEntity();
                String appId = applicationDto.getId();
                CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) mulReq.getFile("selectedFile");
                if(!StringUtil.isEmpty(appId)){
                    withdrawnDto.setApplicationId(appId);
                }
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
                    WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                    //isValid = IaisEGPConstant.NO;
                    if (commonsMultipartFile !=null && commonsMultipartFile.getSize() > 0){
                        ParamUtil.setRequestAttr(bpc.request,"file_upload_withdraw",commonsMultipartFile.getFileItem().getName());
                    }
                    ParamUtil.setRequestAttr(bpc.request,"withdrawDtoView",withdrawnDto);
                    wdIsValid = IaisEGPConstant.NO;
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
        }
        String applicationNo =  (String)ParamUtil.getSessionAttr(bpc.request, "withdrawAppNo");
        String rfiApplicationNo = (String)ParamUtil.getSessionAttr(bpc.request, "rfiWithdrawAppNo");
        List<WithdrawnDto> addWithdrawnDtoList = IaisCommonUtils.genNewArrayList();
        addWithdrawnDtoList.addAll(withdrawnDtoList);
        if (StringUtil.isEmpty(applicationNo)){
            addWithdrawnDtoList.removeIf(h -> rfiApplicationNo.equals(h.getApplicationNo()));
        }else{
            addWithdrawnDtoList.removeIf(h -> applicationNo.equals(h.getApplicationNo()));
            for (WithdrawnDto withdrawnDto : addWithdrawnDtoList) {
                if (!applicationFeClient.isApplicationWithdrawal(withdrawnDto.getApplicationId()).getEntity()) {
                    String withdrawalError = MessageUtil.replaceMessage("WDL_EER002","appNo",withdrawnDto.getApplicationNo());
                    ParamUtil.setRequestAttr(bpc.request,"appIsWithdrawal",Boolean.TRUE);
                    bpc.request.setAttribute(InboxConst.APP_RECALL_RESULT,withdrawalError);
                    wdIsValid = IaisEGPConstant.NO;
                    break;
                }
            }
        }


        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID,wdIsValid);
        ParamUtil.setRequestAttr(bpc.request, "addWithdrawnDtoList",addWithdrawnDtoList);
        return withdrawnDtoList;
    }
}
