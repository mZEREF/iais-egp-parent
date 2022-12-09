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
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
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

    private static final String WITHDRAW_DTO_VIEW = "withdrawDtoView";

    private static final String WITHDRAW_APP_NO = "withdrawAppNo";

    private static final String IS_DO_VIEW = "isDoView";

    private static final String WITHDRAW_PAGE_SHOW_FILES = "withdrawPageShowFiles";

    private static final String  WITHDRAW_PAGE_SHOW_FILE_HASH_MAP = "withdrawPageShowFileHashMap";

    private static final String SESSION_FILES_MAP_AJAX_FESELECTEDWDFILE = "seesion_files_map_ajax_feselectedWdFile";

    private static final String RFI_WITHDRAW_APP_NO = "rfiWithdrawAppNo";

    private static final String RFI_WITHDRAW_DTO = "rfiWithdrawDto";

    private static final String SELECTED_WD_FILE_DIV = "selectedWdFileDiv";

    private static final String SELECTED_WD_FILE = "selectedWdFile";

    private static final String APPLY_PAGE_PRINT = "applyPagePrint";

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
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.DASHBOARDTITLE,null);
        ParamUtil.setSessionAttr(bpc.request,WITHDRAW_DTO_VIEW,null);
        ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR, null);
        String withdrawAppNo = ParamUtil.getMaskedString(bpc.request, WITHDRAW_APP_NO);
        String isDoView = ParamUtil.getString(bpc.request, IS_DO_VIEW);
        ParamUtil.setSessionAttr(bpc.request, IS_DO_VIEW, isDoView);
        int configFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setSessionAttr(bpc.request, "addWithdrawnDtoList",null);
        ParamUtil.setSessionAttr(bpc.request, WITHDRAW_DTO_VIEW, null);
        ParamUtil.setSessionAttr(bpc.request, WITHDRAW_PAGE_SHOW_FILES, null);
        ParamUtil.setSessionAttr(bpc.request, WITHDRAW_PAGE_SHOW_FILE_HASH_MAP, null);
        ParamUtil.setSessionAttr(bpc.request, SESSION_FILES_MAP_AJAX_FESELECTEDWDFILE, null);
        ParamUtil.setSessionAttr(bpc.request, "seesion_files_map_ajax_feselectedWdFile_MaxIndex", null);
        ParamUtil.setSessionAttr(bpc.request, "configFileSize",configFileSize);
        if (StringUtil.isEmpty(isDoView)){
            isDoView = "N";
        }
        ApplicationDto entity=null;
        if (!StringUtil.isEmpty(withdrawAppNo)){
            ParamUtil.setSessionAttr(bpc.request, WITHDRAW_APP_NO, withdrawAppNo);
            entity = appCommService.getApplicationDtoByAppNo(withdrawAppNo);
        }
        String rfiWithdrawAppNo = ParamUtil.getMaskedString(bpc.request,RFI_WITHDRAW_APP_NO);
        if(entity!=null){
            String status = entity.getStatus();
            String applicationType = entity.getApplicationType();
            if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)
                    && ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(status)){
                isDoView="N";
                if(!Boolean.TRUE.equals(applicationFeClient.isApplicationWithdrawal(entity.getId()).getEntity())){
                    rfiWithdrawAppNo= withdrawAppNo;
                }
            }
        }
        // just view, so direct return
        if ("Y".equals(isDoView)){
            WithdrawnDto withdrawnDto = withdrawalService.getWithdrawAppInfo(withdrawAppNo);
            ParamUtil.setSessionAttr(bpc.request, WITHDRAW_APP_NO, withdrawnDto.getPrevAppNo());
            getFileInfo(withdrawnDto,bpc.request);
            ParamUtil.setSessionAttr(bpc.request, WITHDRAW_DTO_VIEW, withdrawnDto);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "");
            return;
        }

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_WITHDRAWAL, AuditTrailConsts.FUNCTION_WITHDRAWAL);
        if (!StringUtil.isEmpty(rfiWithdrawAppNo)){
            WithdrawnDto withdrawnDto = withdrawalService.getWithdrawAppInfo(rfiWithdrawAppNo);
            ParamUtil.setSessionAttr(bpc.request,
                    IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR, withdrawnDto.getMaxFileIndex());
            getFileInfo(withdrawnDto,bpc.request);
            ParamUtil.setSessionAttr(bpc.request, RFI_WITHDRAW_APP_NO, rfiWithdrawAppNo);
            ParamUtil.setSessionAttr(bpc.request, RFI_WITHDRAW_DTO, withdrawnDto);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "doRfi");
        }else{
            ParamUtil.setSessionAttr(bpc.request, RFI_WITHDRAW_DTO, null);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "");
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
                    pageShowFileDto.setFileMapId(SELECTED_WD_FILE_DIV + i);
                }else{
                    pageShowFileDto.setFileMapId(SELECTED_WD_FILE_DIV + index);
                    pageShowFileDto.setIndex(index);
                }
                pageShowFileDto.setFileName(viewDoc.get(i).getDocName());
                pageShowFileDto.setSize(viewDoc.get(i).getDocSize());
                pageShowFileDto.setMd5Code(viewDoc.get(i).getMd5Code());
                pageShowFileDto.setFileUploadUrl(viewDoc.get(i).getFileRepoId());
                pageShowFileDtos.add(pageShowFileDto);
                if (StringUtil.isEmpty(index)){
                    map.put(SELECTED_WD_FILE + i, null);
                    pageShowFileHashMap.put(SELECTED_WD_FILE + i, pageShowFileDto);
                }else{
                    map.put(SELECTED_WD_FILE + index, null);
                    pageShowFileHashMap.put(SELECTED_WD_FILE + index, pageShowFileDto);
                }
            }
            request.getSession().setAttribute(WITHDRAW_PAGE_SHOW_FILE_HASH_MAP, pageShowFileHashMap);
            request.getSession().setAttribute(SESSION_FILES_MAP_AJAX_FESELECTEDWDFILE, map);
            request.getSession().setAttribute("seesion_files_map_ajax_feselectedWdFile_MaxIndex", viewDoc.size());
            request.getSession().setAttribute(WITHDRAW_PAGE_SHOW_FILES, pageShowFileDtos);
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
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_REVIEW});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION_REPLY});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_CLARIFICATION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_DRAFT_LETTER});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_CREATE_MESG});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_BE_CREATE_TASK});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION_RFI});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,"APST052"});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ENQUIRE});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PROFESSIONAL_SCREENING_OFFICER_ENQUIRE});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,"APST055"});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,"APST056"});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_RFI_ONLY_SELF_CHECKLIST});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_PSO_ROUTE_BACK});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_ASO});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ROUTE_BACK});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_PSO});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_APPLICANT});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_PENDING_FE});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,"APST090"});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,"APST091"});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,"APST092"});

        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_REVIEW});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION_REPLY});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_CLARIFICATION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_DRAFT_LETTER});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_CREATE_MESG});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_BE_CREATE_TASK});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION_RFI});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,"APST052"});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ENQUIRE});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PROFESSIONAL_SCREENING_OFFICER_ENQUIRE});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,"APST055"});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,"APST056"});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_RFI_ONLY_SELF_CHECKLIST});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_PSO_ROUTE_BACK});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_ASO});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ROUTE_BACK});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_PSO});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_APPLICANT});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_PENDING_FE});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,"APST090"});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,"APST091"});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,"APST092"});

        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_REVIEW});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION_REPLY});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_CLARIFICATION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_DRAFT_LETTER});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_CREATE_MESG});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_BE_CREATE_TASK});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION_RFI});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,"APST052"});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ENQUIRE});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PROFESSIONAL_SCREENING_OFFICER_ENQUIRE});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,"APST055"});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,"APST056"});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_RFI_ONLY_SELF_CHECKLIST});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_PSO_ROUTE_BACK});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_ASO});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ROUTE_BACK});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_PSO});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_APPLICANT});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_PENDING_FE});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,"APST090"});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,"APST091"});
        applicationTandS.add(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,"APST092"});

        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_PENDING_CLARIFICATION});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING});
        applicationTandS.add(new String[]{"APTY001","APST038"});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_BE_CREATE_TASK});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION_RFI});
        applicationTandS.add(new String[]{"APTY001","APST052"});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ENQUIRE});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_PROFESSIONAL_SCREENING_OFFICER_ENQUIRE});
        applicationTandS.add(new String[]{"APTY001","APST055"});
        applicationTandS.add(new String[]{"APTY001","APST056"});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_RFI_ONLY_SELF_CHECKLIST});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_PSO_ROUTE_BACK});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ROUTE_BACK});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_ASO});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_PSO});
        applicationTandS.add(new String[]{"APTY001",ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR});
        applicationTandS.add(new String[]{"APTY001","APST090"});
        applicationTandS.add(new String[]{"APTY001","APST091"});
        applicationTandS.add(new String[]{"APTY001","APST092"});

        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);

        List<WithdrawApplicationDto> withdrawAppList =  withdrawalService.getCanWithdrawAppList(applicationTandS,loginContext.getLicenseeId());

        String applicationNo =  (String)ParamUtil.getSessionAttr(bpc.request, WITHDRAW_APP_NO);
        if(withdrawAppList != null && !withdrawAppList.isEmpty()){
            if(StringUtil.isEmpty(applicationNo)){
                applicationNo = (String)ParamUtil.getSessionAttr(bpc.request, RFI_WITHDRAW_APP_NO);
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
        if ((withdrawnDtoList != null) && (!withdrawnDtoList.isEmpty()) && IaisEGPConstant.YES.equals(wdIsValid)){
            String replaceStr = "";
            StringBuilder sb = new StringBuilder();
            withdrawnDtoList.forEach(h -> sb.append(h.getApplicationNo()).append(','));
            if (sb.toString().length() > 1){
                replaceStr = sb.substring(0,sb.toString().length() - 1);
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
        if ((withdrawnDtoList != null) && (!withdrawnDtoList.isEmpty()) && IaisEGPConstant.YES.equals(wdIsValid)){
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
        ParamUtil.setSessionAttr(bpc.request, WITHDRAW_APP_NO,null);
        WithdrawnDto withdrawnDto = (WithdrawnDto) ParamUtil.getSessionAttr(bpc.request, RFI_WITHDRAW_DTO);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);

        withdrawnDto.setLicenseeId(loginContext.getLicenseeId());
        ParamUtil.setSessionAttr(bpc.request, RFI_WITHDRAW_DTO, withdrawnDto);
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
        String isDoView = ParamUtil.getString(bpc.request, IS_DO_VIEW);
        List<WithdrawnDto> withdrawnDtoList = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(paramAppNos)){
            String[] withdrawAppNos = paramAppNos.split("#");
            for (int i =0;i<withdrawAppNos.length;i++){
                WithdrawnDto withdrawnDto = (WithdrawnDto) ParamUtil.getSessionAttr(bpc.request, RFI_WITHDRAW_DTO);
                if (withdrawnDto == null){
                    if (!APPLY_PAGE_PRINT.equals(printActionType)&&i==0) {
                        withdrawnDto = (WithdrawnDto) ParamUtil.getSessionAttr(bpc.request, WITHDRAW_DTO_VIEW);
                    }
                    if (withdrawnDto == null){
                        withdrawnDto = new WithdrawnDto();
                    }
                }
                String appNo = withdrawAppNos[i];
                ApplicationDto applicationDto = appCommService.getApplicationDtoByAppNo(appNo);
                String appId = applicationDto.getId();
                Map<String, File> map = (Map<String, File>)bpc.request.getSession().getAttribute(SESSION_FILES_MAP_AJAX_FESELECTEDWDFILE);
                Map<String, PageShowFileDto> pageShowFileHashMap = (Map<String, PageShowFileDto>)mulReq.getSession().getAttribute(WITHDRAW_PAGE_SHOW_FILE_HASH_MAP);
                List<AppPremisesSpecialDocDto> appPremisesSpecialDocDtoList = IaisCommonUtils.genNewArrayList();
                List<PageShowFileDto> pageShowFileDtos =IaisCommonUtils.genNewArrayList();
                List<File> files= IaisCommonUtils.genNewArrayList();
                if(!StringUtil.isEmpty(appId)){
                    withdrawnDto.setApplicationId(appId);
                }
                withdrawnDto.setApplicationNo(appNo);
                HcsaServiceDto hcsaServiceDto= hcsaConfigFeClient.getHcsaServiceDtoByServiceId(applicationDto.getServiceId()).getEntity();
                withdrawnDto.setSvcName(hcsaServiceDto.getSvcName());
                AppGrpPremisesDto appGrpPremisesDto = appCommService.getActivePremisesByAppNo(applicationDto.getApplicationNo());
                if (appGrpPremisesDto != null) {
                    withdrawnDto.setHciName(appGrpPremisesDto.getHciName());
                } else {
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
                                pageShowFileDto.setFileMapId(SELECTED_WD_FILE_DIV+e);
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
                        if(fileRepoId==null && iterator.hasNext()){
                            String next = iterator.next();
                            pageShowFileDtos.get(j).setFileUploadUrl(next);
                            appPremisesSpecialDocDtoList.get(j).setFileRepoId(next);
                            iterator.remove();
                        }
                    }
                }

                withdrawnDto.setAppPremisesSpecialDocDto(appPremisesSpecialDocDtoList);
                mulReq.getSession().setAttribute(WITHDRAW_PAGE_SHOW_FILES, pageShowFileDtos);

                if (APPLY_PAGE_PRINT.equals(printActionType)){
                    wdIsValid = IaisEGPConstant.NO;
                    ParamUtil.setSessionAttr(bpc.request,WITHDRAW_DTO_VIEW,withdrawnDto);
                    ParamUtil.setRequestAttr(bpc.request,"apply_page_print","Y");
                }
                if(validationResult != null && validationResult.isHasErrors() && !APPLY_PAGE_PRINT.equals(printActionType)){
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                    WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                    ParamUtil.setSessionAttr(bpc.request,WITHDRAW_DTO_VIEW,withdrawnDto);
                    wdIsValid = IaisEGPConstant.NO;
                }
                ParamUtil.setSessionAttr(bpc.request,WITHDRAW_DTO_VIEW,withdrawnDto);
                withdrawnDtoList.add(withdrawnDto);
            }
        }
        String applicationNo =  (String)ParamUtil.getSessionAttr(bpc.request, WITHDRAW_APP_NO);
        String rfiApplicationNo = (String)ParamUtil.getSessionAttr(bpc.request, RFI_WITHDRAW_APP_NO);
        List<WithdrawnDto> addWithdrawnDtoList = IaisCommonUtils.genNewArrayList();
        addWithdrawnDtoList.addAll(withdrawnDtoList);
        if (StringUtil.isEmpty(applicationNo)) {
            addWithdrawnDtoList.removeIf(h -> rfiApplicationNo.equals(h.getApplicationNo()));
        } else {
            addWithdrawnDtoList.removeIf(h -> applicationNo.equals(h.getApplicationNo()));
            if (!APPLY_PAGE_PRINT.equals(printActionType)) {
                for (WithdrawnDto withdrawnDto : addWithdrawnDtoList) {
                    if (!Boolean.TRUE.equals(applicationFeClient.isApplicationWithdrawal(withdrawnDto.getApplicationId()).getEntity())) {
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
