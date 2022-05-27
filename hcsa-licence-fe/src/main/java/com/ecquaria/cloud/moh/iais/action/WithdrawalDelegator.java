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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
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
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.dto.memorypage.PaginationHandler;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.WithdrawalService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.ComFileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigFeClient;
import com.ecquaria.cloud.moh.iais.service.client.LicFeInboxClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
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

    @Autowired
    private ComFileRepoClient comFileRepoClient;

    @Autowired
    private HcsaConfigFeClient hcsaConfigFeClient;

    @Autowired
    private AppCommService appCommService;

    private String wdIsValid = IaisEGPConstant.YES;

    public void start(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("****The Start Step****"));
        ParamUtil.setSessionAttr(bpc.request,HcsaLicenceFeConstant.DASHBOARDTITLE,null);
        ParamUtil.setSessionAttr(bpc.request,"withdrawDtoView",null);
        ParamUtil.setSessionAttr(bpc.request, HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR, null);
        String withdrawAppNo = ParamUtil.getMaskedString(bpc.request, "withdrawAppNo");
        String isDoView = ParamUtil.getString(bpc.request, "isDoView");
        ParamUtil.setSessionAttr(bpc.request, "isDoView", isDoView);
        int configFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setSessionAttr(bpc.request, "addWithdrawnDtoList",null);
        ParamUtil.setSessionAttr(bpc.request, "withdrawDtoView", null);
        ParamUtil.setSessionAttr(bpc.request, "withdrawPageShowFiles", null);
        ParamUtil.setSessionAttr(bpc.request, "withdrawPageShowFileHashMap", null);
        ParamUtil.setSessionAttr(bpc.request, "seesion_files_map_ajax_feselectedWdFile", null);
        ParamUtil.setSessionAttr(bpc.request, "seesion_files_map_ajax_feselectedWdFile_MaxIndex", null);
        ParamUtil.setSessionAttr(bpc.request, "configFileSize",configFileSize);
        //     String withdrawAppId = ParamUtil.getMaskedString(bpc.request, "withdrawAppId");
        if (StringUtil.isEmpty(isDoView)){
            isDoView = "N";
        }
        ApplicationDto entity=null;
        if (!StringUtil.isEmpty(withdrawAppNo)){
            ParamUtil.setSessionAttr(bpc.request, "withdrawAppNo", withdrawAppNo);
            entity = appCommService.getApplicationDtoByAppNo(withdrawAppNo);
        }
        String rfiWithdrawAppNo = ParamUtil.getMaskedString(bpc.request,"rfiWithdrawAppNo");
        if(entity!=null){
            String status = entity.getStatus();
            String applicationType = entity.getApplicationType();
            if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)
                    && ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(status)){
                isDoView="N";
                if(!applicationFeClient.isApplicationWithdrawal(entity.getId()).getEntity()){
                    rfiWithdrawAppNo= withdrawAppNo;
                }
            }
        }
        // just view, so direct return
        if ("Y".equals(isDoView)){
            WithdrawnDto withdrawnDto = withdrawalService.getWithdrawAppInfo(withdrawAppNo);
            ParamUtil.setSessionAttr(bpc.request, "withdrawAppNo", withdrawnDto.getPrevAppNo());
            getFileInfo(withdrawnDto,bpc.request);
            ParamUtil.setSessionAttr(bpc.request, "withdrawDtoView", withdrawnDto);
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", "");
            return;
        }

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_WITHDRAWAL, AuditTrailConsts.FUNCTION_WITHDRAWAL);
        if (!StringUtil.isEmpty(rfiWithdrawAppNo)){
            WithdrawnDto withdrawnDto = withdrawalService.getWithdrawAppInfo(rfiWithdrawAppNo);
            ParamUtil.setSessionAttr(bpc.request,
                    HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR, withdrawnDto.getMaxFileIndex());
            getFileInfo(withdrawnDto,bpc.request);
            ParamUtil.setSessionAttr(bpc.request, "rfiWithdrawAppNo", rfiWithdrawAppNo);
            ParamUtil.setSessionAttr(bpc.request, "rfiWithdrawDto", withdrawnDto);
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", "doRfi");
        }else{
            ParamUtil.setSessionAttr(bpc.request, "rfiWithdrawDto", null);
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", "");
        }
    }

    private void getFileInfo(WithdrawnDto withdrawnDto, HttpServletRequest request){
        List<AppPremisesSpecialDocDto> viewDoc = withdrawnDto.getAppPremisesSpecialDocDto();
        List<PageShowFileDto> pageShowFileDtos = IaisCommonUtils.genNewArrayList();
        HashMap<String,File> map= IaisCommonUtils.genNewHashMap();
        HashMap<String, PageShowFileDto> pageShowFileHashMap = IaisCommonUtils.genNewHashMap();
        if (viewDoc != null && !viewDoc.isEmpty()) {
            for (int i = 0; i < viewDoc.size(); i++) {
                PageShowFileDto pageShowFileDto = new PageShowFileDto();
                String index = viewDoc.get(i).getIndex();
                if (StringUtil.isEmpty(index)){
                    pageShowFileDto.setIndex(String.valueOf(i));
                    pageShowFileDto.setFileMapId("selectedWdFileDiv" + i);
                }else{
                    pageShowFileDto.setFileMapId("selectedWdFileDiv" + index);
                    pageShowFileDto.setIndex(index);
                }
                pageShowFileDto.setFileName(viewDoc.get(i).getDocName());
                pageShowFileDto.setSize(viewDoc.get(i).getDocSize());
                pageShowFileDto.setMd5Code(viewDoc.get(i).getMd5Code());
                pageShowFileDto.setFileUploadUrl(viewDoc.get(i).getFileRepoId());
                pageShowFileDtos.add(pageShowFileDto);
                if (StringUtil.isEmpty(index)){
                    map.put("selectedWdFile" + i, null);
                    pageShowFileHashMap.put("selectedWdFile" + i, pageShowFileDto);
                }else{
                    map.put("selectedWdFile" + index, null);
                    pageShowFileHashMap.put("selectedWdFile" + index, pageShowFileDto);
                }
            }
            request.getSession().setAttribute("withdrawPageShowFileHashMap", pageShowFileHashMap);
            request.getSession().setAttribute("seesion_files_map_ajax_feselectedWdFile", map);
            request.getSession().setAttribute("seesion_files_map_ajax_feselectedWdFile_MaxIndex", viewDoc.size());
            request.getSession().setAttribute("withdrawPageShowFiles", pageShowFileDtos);
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
        List<String[]> applicationTandS = new ArrayList<>(180);
        applicationTandS.add(new String[]{"APTY002","APST001"});
        applicationTandS.add(new String[]{"APTY002","APST002"});
        applicationTandS.add(new String[]{"APTY002","APST003"});
        applicationTandS.add(new String[]{"APTY002","APST004"});
        applicationTandS.add(new String[]{"APTY002","APST007"});
        applicationTandS.add(new String[]{"APTY002","APST010"});
        applicationTandS.add(new String[]{"APTY002","APST011"});
        applicationTandS.add(new String[]{"APTY002","APST012"});
        applicationTandS.add(new String[]{"APTY002","APST013"});
        applicationTandS.add(new String[]{"APTY002","APST014"});
        applicationTandS.add(new String[]{"APTY002","APST019"});
        applicationTandS.add(new String[]{"APTY002","APST020"});
        applicationTandS.add(new String[]{"APTY002","APST021"});
        applicationTandS.add(new String[]{"APTY002","APST022"});
        applicationTandS.add(new String[]{"APTY002","APST023"});
        applicationTandS.add(new String[]{"APTY002","APST024"});
        applicationTandS.add(new String[]{"APTY002","APST027"});
        applicationTandS.add(new String[]{"APTY002","APST028"});
        applicationTandS.add(new String[]{"APTY002","APST029"});
        applicationTandS.add(new String[]{"APTY002","APST031"});
        applicationTandS.add(new String[]{"APTY002","APST032"});
        applicationTandS.add(new String[]{"APTY002","APST033"});
        applicationTandS.add(new String[]{"APTY002","APST034"});
        applicationTandS.add(new String[]{"APTY002","APST037"});
        applicationTandS.add(new String[]{"APTY002","APST039"});
        applicationTandS.add(new String[]{"APTY002","APST040"});
        applicationTandS.add(new String[]{"APTY002","APST041"});
        applicationTandS.add(new String[]{"APTY002","APST048"});
        applicationTandS.add(new String[]{"APTY002","APST049"});
        applicationTandS.add(new String[]{"APTY002","APST052"});
        applicationTandS.add(new String[]{"APTY002","APST053"});
        applicationTandS.add(new String[]{"APTY002","APST054"});
        applicationTandS.add(new String[]{"APTY002","APST055"});
        applicationTandS.add(new String[]{"APTY002","APST056"});
        applicationTandS.add(new String[]{"APTY002","APST061"});
        applicationTandS.add(new String[]{"APTY002","APST062"});
        applicationTandS.add(new String[]{"APTY002","APST063"});
        applicationTandS.add(new String[]{"APTY002","APST065"});
        applicationTandS.add(new String[]{"APTY002","APST064"});
        applicationTandS.add(new String[]{"APTY002","APST066"});
        applicationTandS.add(new String[]{"APTY002","APST067"});
        applicationTandS.add(new String[]{"APTY002","APST068"});
        applicationTandS.add(new String[]{"APTY002","APST069"});
        applicationTandS.add(new String[]{"APTY002","APST070"});
        applicationTandS.add(new String[]{"APTY002","APST071"});
        applicationTandS.add(new String[]{"APTY002","APST077"});
        applicationTandS.add(new String[]{"APTY002","APST090"});
        applicationTandS.add(new String[]{"APTY002","APST091"});
        applicationTandS.add(new String[]{"APTY002","APST092"});

        applicationTandS.add(new String[]{"APTY004","APST001"});
        applicationTandS.add(new String[]{"APTY004","APST002"});
        applicationTandS.add(new String[]{"APTY004","APST003"});
        applicationTandS.add(new String[]{"APTY004","APST004"});
        applicationTandS.add(new String[]{"APTY004","APST007"});
        applicationTandS.add(new String[]{"APTY004","APST010"});
        applicationTandS.add(new String[]{"APTY004","APST011"});
        applicationTandS.add(new String[]{"APTY004","APST012"});
        applicationTandS.add(new String[]{"APTY004","APST013"});
        applicationTandS.add(new String[]{"APTY004","APST014"});
        applicationTandS.add(new String[]{"APTY004","APST019"});
        applicationTandS.add(new String[]{"APTY004","APST020"});
        applicationTandS.add(new String[]{"APTY004","APST021"});
        applicationTandS.add(new String[]{"APTY004","APST022"});
        applicationTandS.add(new String[]{"APTY004","APST023"});
        applicationTandS.add(new String[]{"APTY004","APST024"});
        applicationTandS.add(new String[]{"APTY004","APST027"});
        applicationTandS.add(new String[]{"APTY004","APST028"});
        applicationTandS.add(new String[]{"APTY004","APST029"});
        applicationTandS.add(new String[]{"APTY004","APST031"});
        applicationTandS.add(new String[]{"APTY004","APST032"});
        applicationTandS.add(new String[]{"APTY004","APST033"});
        applicationTandS.add(new String[]{"APTY004","APST034"});
        applicationTandS.add(new String[]{"APTY004","APST037"});
        applicationTandS.add(new String[]{"APTY004","APST039"});
        applicationTandS.add(new String[]{"APTY004","APST040"});
        applicationTandS.add(new String[]{"APTY004","APST041"});
        applicationTandS.add(new String[]{"APTY004","APST048"});
        applicationTandS.add(new String[]{"APTY004","APST049"});
        applicationTandS.add(new String[]{"APTY004","APST052"});
        applicationTandS.add(new String[]{"APTY004","APST053"});
        applicationTandS.add(new String[]{"APTY004","APST054"});
        applicationTandS.add(new String[]{"APTY004","APST055"});
        applicationTandS.add(new String[]{"APTY004","APST056"});
        applicationTandS.add(new String[]{"APTY004","APST061"});
        applicationTandS.add(new String[]{"APTY004","APST062"});
        applicationTandS.add(new String[]{"APTY004","APST063"});
        applicationTandS.add(new String[]{"APTY004","APST065"});
        applicationTandS.add(new String[]{"APTY004","APST064"});
        applicationTandS.add(new String[]{"APTY004","APST066"});
        applicationTandS.add(new String[]{"APTY004","APST067"});
        applicationTandS.add(new String[]{"APTY004","APST068"});
        applicationTandS.add(new String[]{"APTY004","APST069"});
        applicationTandS.add(new String[]{"APTY004","APST070"});
        applicationTandS.add(new String[]{"APTY004","APST071"});
        applicationTandS.add(new String[]{"APTY004","APST077"});
        applicationTandS.add(new String[]{"APTY004","APST090"});
        applicationTandS.add(new String[]{"APTY004","APST091"});
        applicationTandS.add(new String[]{"APTY004","APST092"});

        applicationTandS.add(new String[]{"APTY005","APST001"});
        applicationTandS.add(new String[]{"APTY005","APST002"});
        applicationTandS.add(new String[]{"APTY005","APST003"});
        applicationTandS.add(new String[]{"APTY005","APST004"});
        applicationTandS.add(new String[]{"APTY005","APST007"});
        applicationTandS.add(new String[]{"APTY005","APST010"});
        applicationTandS.add(new String[]{"APTY005","APST011"});
        applicationTandS.add(new String[]{"APTY005","APST012"});
        applicationTandS.add(new String[]{"APTY005","APST013"});
        applicationTandS.add(new String[]{"APTY005","APST014"});
        applicationTandS.add(new String[]{"APTY005","APST019"});
        applicationTandS.add(new String[]{"APTY005","APST020"});
        applicationTandS.add(new String[]{"APTY005","APST021"});
        applicationTandS.add(new String[]{"APTY005","APST022"});
        applicationTandS.add(new String[]{"APTY005","APST023"});
        applicationTandS.add(new String[]{"APTY005","APST024"});
        applicationTandS.add(new String[]{"APTY005","APST027"});
        applicationTandS.add(new String[]{"APTY005","APST028"});
        applicationTandS.add(new String[]{"APTY005","APST029"});
        applicationTandS.add(new String[]{"APTY005","APST031"});
        applicationTandS.add(new String[]{"APTY005","APST032"});
        applicationTandS.add(new String[]{"APTY005","APST033"});
        applicationTandS.add(new String[]{"APTY005","APST034"});
        applicationTandS.add(new String[]{"APTY005","APST037"});
        applicationTandS.add(new String[]{"APTY005","APST039"});
        applicationTandS.add(new String[]{"APTY005","APST040"});
        applicationTandS.add(new String[]{"APTY005","APST041"});
        applicationTandS.add(new String[]{"APTY005","APST048"});
        applicationTandS.add(new String[]{"APTY005","APST049"});
        applicationTandS.add(new String[]{"APTY005","APST052"});
        applicationTandS.add(new String[]{"APTY005","APST053"});
        applicationTandS.add(new String[]{"APTY005","APST054"});
        applicationTandS.add(new String[]{"APTY005","APST055"});
        applicationTandS.add(new String[]{"APTY005","APST056"});
        applicationTandS.add(new String[]{"APTY005","APST061"});
        applicationTandS.add(new String[]{"APTY005","APST062"});
        applicationTandS.add(new String[]{"APTY005","APST063"});
        applicationTandS.add(new String[]{"APTY005","APST065"});
        applicationTandS.add(new String[]{"APTY005","APST064"});
        applicationTandS.add(new String[]{"APTY005","APST066"});
        applicationTandS.add(new String[]{"APTY005","APST067"});
        applicationTandS.add(new String[]{"APTY005","APST068"});
        applicationTandS.add(new String[]{"APTY005","APST069"});
        applicationTandS.add(new String[]{"APTY005","APST070"});
        applicationTandS.add(new String[]{"APTY005","APST071"});
        applicationTandS.add(new String[]{"APTY005","APST077"});
        applicationTandS.add(new String[]{"APTY005","APST090"});
        applicationTandS.add(new String[]{"APTY005","APST091"});
        applicationTandS.add(new String[]{"APTY005","APST092"});

        applicationTandS.add(new String[]{"APTY001","APST001"});
        applicationTandS.add(new String[]{"APTY001","APST002"});
        applicationTandS.add(new String[]{"APTY001","APST004"});
        applicationTandS.add(new String[]{"APTY001","APST007"});
        applicationTandS.add(new String[]{"APTY001","APST010"});
        applicationTandS.add(new String[]{"APTY001","APST011"});
        applicationTandS.add(new String[]{"APTY001","APST012"});
        applicationTandS.add(new String[]{"APTY001","APST013"});
        applicationTandS.add(new String[]{"APTY001","APST014"});
        applicationTandS.add(new String[]{"APTY001","APST023"});
        applicationTandS.add(new String[]{"APTY001","APST028"});
        applicationTandS.add(new String[]{"APTY001","APST032"});
        applicationTandS.add(new String[]{"APTY001","APST033"});
        applicationTandS.add(new String[]{"APTY001","APST034"});
        applicationTandS.add(new String[]{"APTY001","APST038"});
        applicationTandS.add(new String[]{"APTY001","APST039"});
        applicationTandS.add(new String[]{"APTY001","APST048"});
        applicationTandS.add(new String[]{"APTY001","APST049"});
        applicationTandS.add(new String[]{"APTY001","APST052"});
        applicationTandS.add(new String[]{"APTY001","APST053"});
        applicationTandS.add(new String[]{"APTY001","APST054"});
        applicationTandS.add(new String[]{"APTY001","APST055"});
        applicationTandS.add(new String[]{"APTY001","APST056"});
        applicationTandS.add(new String[]{"APTY001","APST061"});
        applicationTandS.add(new String[]{"APTY001","APST062"});
        applicationTandS.add(new String[]{"APTY001","APST063"});
        applicationTandS.add(new String[]{"APTY001","APST064"});
        applicationTandS.add(new String[]{"APTY001","APST065"});
        applicationTandS.add(new String[]{"APTY001","APST066"});
        applicationTandS.add(new String[]{"APTY001","APST067"});
        applicationTandS.add(new String[]{"APTY001","APST090"});
        applicationTandS.add(new String[]{"APTY001","APST091"});
        applicationTandS.add(new String[]{"APTY001","APST092"});

        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);

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
            ParamUtil.setRequestAttr(bpc.request,"withdrawnDtoListAck",withdrawnDtoList);
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
            ParamUtil.setRequestAttr(bpc.request,"withdrawnDtoListAck",withdrawnDtoList);
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
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);

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
        String printActionType = ParamUtil.getString(bpc.request, "print_action_type");
        String isDoView = ParamUtil.getString(bpc.request, "isDoView");
        List<WithdrawnDto> withdrawnDtoList = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(paramAppNos)){
            String[] withdrawAppNos = paramAppNos.split("#");
            for (int i =0;i<withdrawAppNos.length;i++){
                WithdrawnDto withdrawnDto = (WithdrawnDto) ParamUtil.getSessionAttr(bpc.request, "rfiWithdrawDto");
                if (withdrawnDto == null){
                    if (!"applyPagePrint".equals(printActionType)&&i==0) {
                        withdrawnDto = (WithdrawnDto) ParamUtil.getSessionAttr(bpc.request, "withdrawDtoView");
                    }
                    if (withdrawnDto == null){
                        withdrawnDto = new WithdrawnDto();
                    }
                }
                String appNo = withdrawAppNos[i];
                ApplicationDto applicationDto = appCommService.getApplicationDtoByAppNo(appNo);
                String appId = applicationDto.getId();
                Map<String, File> map = (Map<String, File>)bpc.request.getSession().getAttribute("seesion_files_map_ajax_feselectedWdFile");
                Map<String, PageShowFileDto> pageShowFileHashMap = (Map<String, PageShowFileDto>)mulReq.getSession().getAttribute("withdrawPageShowFileHashMap");
                List<AppPremisesSpecialDocDto> appPremisesSpecialDocDtoList = IaisCommonUtils.genNewArrayList();
                List<PageShowFileDto> pageShowFileDtos =IaisCommonUtils.genNewArrayList();
                List<File> files= IaisCommonUtils.genNewArrayList();
                if(!StringUtil.isEmpty(appId)){
                    withdrawnDto.setApplicationId(appId);
                }
                withdrawnDto.setApplicationNo(appNo);
                HcsaServiceDto hcsaServiceDto= hcsaConfigFeClient.getHcsaServiceDtoByServiceId(applicationDto.getServiceId()).getEntity();
                withdrawnDto.setSvcName(hcsaServiceDto.getSvcName());
                List<AppGrpPremisesDto> appGrpPremisesDtos=applicationFeClient.getAppGrpPremisesDtoByAppGroId(applicationDto.getApplicationNo()).getEntity();
                if(appGrpPremisesDtos!=null&&appGrpPremisesDtos.size()!=0){
                    AppGrpPremisesDto agp = appGrpPremisesDtos.get(0);
                    if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(agp.getPremisesType())) {
                        withdrawnDto.setHciName(agp.getHciName());
                    } else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(agp.getPremisesType())) {
                        withdrawnDto.setHciName(agp.getOffSiteHciName());
                    } else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(agp.getPremisesType())) {
                        withdrawnDto.setHciName(agp.getEasMtsHciName());
                    } else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(agp.getPremisesType())){
                        withdrawnDto.setHciName(agp.getConveyanceHciName());
                    }
                }else {
                    withdrawnDto.setHciName("");
                }
                LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);

                withdrawnDto.setLicenseeId(loginContext.getLicenseeId());
                withdrawnDto.setWithdrawnReason(withdrawnReason);

                if ("WDR005".equals(withdrawnReason)){
                    String withdrawnRemarks = ParamUtil.getRequestString(bpc.request, "withdrawnRemarks");
                    withdrawnDto.setWithdrawnRemarks(withdrawnRemarks);
                }
                ValidationResult validationResult = WebValidationHelper.validateProperty(withdrawnDto,"save");
                Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
                if (validationResult != null && validationResult.isHasErrors()){
                    errorMap=validationResult.retrieveAll();
                }
                if(map!=null&&!map.isEmpty()){
                    map.forEach((str, file)->{
                        if(file!=null){
                            long length = file.length();
                            if(length>0){
                                Long size=length/1024;
                                files.add(file);
                                AppPremisesSpecialDocDto premisesSpecialDocDto = new AppPremisesSpecialDocDto();
                                String e = str.substring(str.lastIndexOf('e') + 1);
                                premisesSpecialDocDto.setDocName(file.getName());
                                String fileMd5 = FileUtils.getFileMd5(file);
                                premisesSpecialDocDto.setMd5Code(fileMd5);
                                premisesSpecialDocDto.setIndex(e);
                                premisesSpecialDocDto.setSubmitBy(loginContext.getUserId());
                                premisesSpecialDocDto.setDocSize(Integer.valueOf(size.toString()));
                                appPremisesSpecialDocDtoList.add(premisesSpecialDocDto);
                                PageShowFileDto pageShowFileDto =new PageShowFileDto();
                                pageShowFileDto.setIndex(e);
                                pageShowFileDto.setFileName(file.getName());
                                pageShowFileDto.setFileMapId("selectedWdFileDiv"+e);
                                pageShowFileDto.setSize(Integer.valueOf(size.toString()));
                                pageShowFileDto.setMd5Code(fileMd5);
                                pageShowFileDtos.add(pageShowFileDto);
                            }
                        }else {
                            if(pageShowFileHashMap!=null){
                                PageShowFileDto pageShowFileDto = pageShowFileHashMap.get(str);
                                String e = str.substring(str.lastIndexOf('e') + 1);
                                AppPremisesSpecialDocDto premisesSpecialDocDto = new AppPremisesSpecialDocDto();
                                premisesSpecialDocDto.setIndex(e);
                                premisesSpecialDocDto.setFileRepoId(pageShowFileDto.getFileUploadUrl());
                                premisesSpecialDocDto.setDocName(pageShowFileDto.getFileName());
                                premisesSpecialDocDto.setDocSize(pageShowFileDto.getSize());
                                premisesSpecialDocDto.setMd5Code(pageShowFileDto.getMd5Code());
                                premisesSpecialDocDto.setSubmitBy(loginContext.getUserId());
                                appPremisesSpecialDocDtoList.add(premisesSpecialDocDto);
                                pageShowFileDtos.add(pageShowFileDto);
                            }
                        }
                    });
                }

                List<String> list = comFileRepoClient.saveFileRepo(files);
                if(list!=null){
                    ListIterator<String> iterator = list.listIterator();
                    for(int j=0;j< appPremisesSpecialDocDtoList.size();j++){
                        String fileRepoId = appPremisesSpecialDocDtoList.get(j).getFileRepoId();
                        if(fileRepoId==null){
                            if(iterator.hasNext()){
                                String next = iterator.next();
                                pageShowFileDtos.get(j).setFileUploadUrl(next);
                                appPremisesSpecialDocDtoList.get(j).setFileRepoId(next);
                                iterator.remove();
                            }
                        }
                    }
                }

                withdrawnDto.setAppPremisesSpecialDocDto(appPremisesSpecialDocDtoList);
                mulReq.getSession().setAttribute("withdrawPageShowFiles", pageShowFileDtos);

                if ("applyPagePrint".equals(printActionType)){
                    wdIsValid = IaisEGPConstant.NO;
                    ParamUtil.setSessionAttr(bpc.request,"withdrawDtoView",withdrawnDto);
                    ParamUtil.setRequestAttr(bpc.request,"apply_page_print","Y");
                }
                if(validationResult != null && validationResult.isHasErrors()){
                    if (!"applyPagePrint".equals(printActionType)){
                        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                        WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                        ParamUtil.setSessionAttr(bpc.request,"withdrawDtoView",withdrawnDto);
                        wdIsValid = IaisEGPConstant.NO;
                    }
                }
                ParamUtil.setSessionAttr(bpc.request,"withdrawDtoView",withdrawnDto);
                withdrawnDtoList.add(withdrawnDto);
            }
        }
        String applicationNo =  (String)ParamUtil.getSessionAttr(bpc.request, "withdrawAppNo");
        String rfiApplicationNo = (String)ParamUtil.getSessionAttr(bpc.request, "rfiWithdrawAppNo");
        List<WithdrawnDto> addWithdrawnDtoList = IaisCommonUtils.genNewArrayList();
        addWithdrawnDtoList.addAll(withdrawnDtoList);
        if (StringUtil.isEmpty(applicationNo)) {
            addWithdrawnDtoList.removeIf(h -> rfiApplicationNo.equals(h.getApplicationNo()));
        } else {
            addWithdrawnDtoList.removeIf(h -> applicationNo.equals(h.getApplicationNo()));
            if (!"applyPagePrint".equals(printActionType)) {
                for (WithdrawnDto withdrawnDto : addWithdrawnDtoList) {
                    if (!applicationFeClient.isApplicationWithdrawal(withdrawnDto.getApplicationId()).getEntity()) {
                        String withdrawalError = MessageUtil.replaceMessage("WDL_EER002", withdrawnDto.getApplicationNo(), "appNo");
                        ParamUtil.setRequestAttr(bpc.request, "appIsWithdrawal", Boolean.TRUE);
                        bpc.request.setAttribute(InboxConst.APP_RECALL_RESULT, withdrawalError);
                        wdIsValid = IaisEGPConstant.NO;
                        break;
                    }
                }
            }
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID,wdIsValid);
        ParamUtil.setSessionAttr(bpc.request, "addWithdrawnDtoList",(Serializable) addWithdrawnDtoList);
        return withdrawnDtoList;
    }
}
