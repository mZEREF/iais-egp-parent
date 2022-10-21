package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.acra.AcraConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.AjaxResDto;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.LicenseeClient;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/****
 *
 *   @date 1/5/2020
 *   @author zixian
 */
@Slf4j
@Delegator("requestForChangeDelegator")
public class RequestForChangeDelegator {

    @Autowired
    RequestForChangeService requestForChangeService;

    @Autowired
    private AppSubmissionService appSubmissionService;

    @Autowired
    private ServiceConfigService serviceConfigService;

    @Value("${iais.email.sender}")
    private String mailSender;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private LicenseeClient licenseeClient;

    @Autowired
    private LicenceViewService licenceViewService;
    
    @Autowired
    private AppCommService appCommService;
    @Autowired
    private ConfigCommService configCommService;

    @PostMapping(value = "/check-uen")
    public @ResponseBody
    AjaxResDto checkUen(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("the do checkUen start ...."));
        AjaxResDto ajaxResDto = new AjaxResDto();
        String uen = ParamUtil.getString(request, "uen");
        log.info(StringUtil.changeForLog("uen is -->:"+uen));
        String licenceId = (String) ParamUtil.getSessionAttr(request, RfcConst.LICENCEID);
        log.info(StringUtil.changeForLog("licenceId is -->:"+licenceId));
        Map<String,String> error = doValidateEmpty(uen,new String[]{"selectCheakboxs"},"Email","subLicensee");
        if (error.isEmpty()) {
            //Judge the uen type;
            LicenseeDto licenseeDto = requestForChangeService.getLicenseeByUenNo(uen);
            LicenceDto licenceDto = requestForChangeService.getLicDtoById(licenceId);
            doValidateLojic(uen,error,licenceDto,licenseeDto);
            if(error.isEmpty() && licenseeDto != null){
                ajaxResDto.setResCode(AppConsts.AJAX_RES_CODE_SUCCESS);
                ajaxResDto.setType(licenseeDto.getLicenseeType());
                log.info(StringUtil.changeForLog("licenseeDto.getLicenseeType() is -->:"+licenseeDto.getLicenseeType()));
                if(OrganizationConstants.LICENSEE_TYPE_CORPPASS.equals(licenseeDto.getLicenseeType())){
                    Map<String, String> chargesTypeAttr = IaisCommonUtils.genNewHashMap();
                    chargesTypeAttr.put("name", "subLicensee");
                    chargesTypeAttr.put("id", "subLicensee");

                    List<String> checkedVals = IaisCommonUtils.genNewArrayList();
                    String subLicensee = (String) ParamUtil.getSessionAttr(request, "subLicensee");
                    if(!StringUtil.isEmpty(subLicensee)){
                        checkedVals.add(subLicensee);
                    }
                    log.info(StringUtil.changeForLog("subLicensee is -->:"+subLicensee));
                    String chargeTypeSelHtml = ApplicationHelper.genMutilSelectOpHtml(chargesTypeAttr, getSelect(uen,licenceDto),
                            HcsaAppConst.FIRESTOPTION, checkedVals, false,true);

                    String subLicenseeError = (String) ParamUtil.getSessionAttr(request, "subLicenseeError");
                    chargeTypeSelHtml = chargeTypeSelHtml + "<span  class=\"error-msg\" name=\"iaisErrorMsg\" id=\"error_subLicenseeError\">";
                    if(!StringUtil.isEmpty(subLicenseeError)){
                        subLicenseeError = MessageUtil.getMessageDesc(subLicenseeError);
                        chargeTypeSelHtml = chargeTypeSelHtml +subLicenseeError;
                    }
                    ParamUtil.setSessionAttr(request,"subLicenseeError",null);
                    log.info(StringUtil.changeForLog("subLicenseeError is -->:"+subLicenseeError));
                    chargeTypeSelHtml = chargeTypeSelHtml +" </span>";
                    ajaxResDto.setResultJson(chargeTypeSelHtml);
                }else if(licenceDto != null && licenceDto.getLicenseeId().equals(licenseeDto.getId())){
                    error.put("uenError","RFC_ERR021");
                }
            }
        }
        if(!error.isEmpty()){
            ajaxResDto.setResCode(AppConsts.AJAX_RES_CODE_VALIDATE_ERROR);
            ajaxResDto.setResultJson(MessageUtil.getMessageDesc(error.get("uenError")));
        }
        log.info(StringUtil.changeForLog("the do checkUen end ...."));
        return ajaxResDto;
    }
    private List<SelectOption> getSelect(String uen,LicenceDto licenceDto){
        log.info(StringUtil.changeForLog("the getSelect start ...."));
        List<SelectOption> result = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(uen)){
            if(isSameUEN(uen,licenceDto)){
                result.add(new SelectOption("new","Add a new individual licensee"));
            }
            OrganizationDto organizationDto = serviceConfigService.findOrganizationByUen(uen);
            if(organizationDto != null){
                List<SubLicenseeDto>  subLicenseeDtos = licenceViewService.getSubLicenseeDto(organizationDto.getId());
                if(!IaisCommonUtils.isEmpty(subLicenseeDtos)){
                    for(SubLicenseeDto subLicenseeDto : subLicenseeDtos){
                        result.add(new SelectOption(subLicenseeDto.getId(),subLicenseeDto.getDisplayName()));
                    }
                }else{
                    log.error(StringUtil.changeForLog("This orgId can not find out the subLicenseeDtos -->:"+organizationDto.getId()));
                }
            }else{
                log.info(StringUtil.changeForLog("This uen can not OrganizationDto -->:" +uen));
            }
        }else {
            log.info(StringUtil.changeForLog("The uen is null"));
        }

        log.info(StringUtil.changeForLog("the getSelect end ...."));
        return result;
    }

    private  boolean isSameUEN(String uen,LicenceDto licenceDto){
        log.info(StringUtil.changeForLog("the isSameUEN start ...."));
        boolean result = false;
        if(licenceDto != null){
            OrganizationDto organizationDto = licenceViewService.getOrganizationDtoByLicenseeId(licenceDto.getLicenseeId());
            if(organizationDto != null && organizationDto.getUenNo().equals(uen)){
                result = true;
            }
        }
        log.info(StringUtil.changeForLog("the isSameUEN result is -->:"+result));
        log.info(StringUtil.changeForLog("the isSameUEN end ...."));
        return result;
    }
    /**
     *
     * @param bpc
     * @Decription doStart
     */
    public void doStart(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the do doStart start ...."));
        HcsaServiceCacheHelper.flushServiceMapping();
        String licenceId = ParamUtil.getMaskedString(bpc.request, "licenceId");
        ParamUtil.setSessionAttr(bpc.request, RfcConst.LICENCEID, null);
        ParamUtil.setSessionAttr(bpc.request, "SvcName", null);
        ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, null);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.RFCAPPSUBMISSIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.DODRAFTCONFIG, null);
        ParamUtil.setSessionAttr(bpc.request, "AmendTypeValue", null);
        ParamUtil.setRequestAttr(bpc.request, "premisesIndexNo", null);
        ParamUtil.setSessionAttr(bpc.request, "prepareTranfer", null);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.DASHBOARDTITLE, null);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.PRIMARY_DOC_CONFIG, null);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.SVC_DOC_CONFIG, null);
        ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR, 0);
        removeSession(bpc.request);
        init(bpc, licenceId);
        log.debug(StringUtil.changeForLog("the do doStart start ...."));
    }

    private void removeSession(HttpServletRequest request){
        request.getSession().removeAttribute("appSubmissionDtos");
        request.getSession().removeAttribute(HcsaAppConst.APPSUBMISSIONDTO);
        request.getSession().removeAttribute("rfc_eqHciCode");
        request.getSession().removeAttribute("seesion_files_map_ajax_feselectedDeclFile");
        request.getSession().removeAttribute("pageShowFileDtos");
        request.getSession().removeAttribute("selectedRFCFileDocShowPageDto");
        request.getSession().removeAttribute("seesion_files_map_ajax_feselectedRFCFile");
        request.getSession().removeAttribute("seesion_files_map_ajax_feselectedRFCFile_MaxIndex");
        request.getSession().removeAttribute("DraftNumber");
        request.getSession().removeAttribute("renewDto");
        request.getSession().removeAttribute("declaration_page_is");
        request.getSession().removeAttribute("viewPrint");
        DealSessionUtil.clearSession(request);
    }

    private void init(BaseProcessClass bpc, String licenceId) {
        ParamUtil.setSessionAttr(bpc.request, RfcConst.LICENCEID, licenceId);
        //load data
        if (!StringUtil.isEmpty(licenceId)) {
            AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByLicenceId(licenceId);
            if (appSubmissionDto == null || IaisCommonUtils.isEmpty(appSubmissionDto.getAppGrpPremisesDtoList()) ||
                    IaisCommonUtils.isEmpty(appSubmissionDto.getAppSvcRelatedInfoDtoList())) {
                log.info(StringUtil.changeForLog("appSubmissionDto incomplete , licenceId:" + licenceId));
            } else {
                log.info(StringUtil.changeForLog("do request for change ------ licence no:" + appSubmissionDto.getLicenceNo()));
                AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_REQUEST_FOR_CHANGE,
                        AuditTrailConsts.FUNCTION_REQUEST_FOR_CHANGE);
                List<HcsaServiceDto> hcsaServiceDtoList = DealSessionUtil.getLatestServiceConfigsFormApp(appSubmissionDto);
                if (!IaisCommonUtils.isEmpty(hcsaServiceDtoList)) {
                    ParamUtil.setSessionAttr(bpc.request, "SvcId", hcsaServiceDtoList.get(0).getId());
                    ParamUtil.setSessionAttr(bpc.request, "SvcName", hcsaServiceDtoList.get(0).getSvcName());
                }
                appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                DealSessionUtil.init(appSubmissionDto, hcsaServiceDtoList, true, bpc.request);

                AppSubmissionDto oldAppSubmissionDto = CopyUtil.copyMutableObject(appSubmissionDto);
                appSubmissionDto.setOldAppSubmissionDto(oldAppSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request, RfcConst.RFCAPPSUBMISSIONDTO, appSubmissionDto);
            }
        }
    }

    /**
     *
     * @param bpc
     * @Decription prepare
     */
    public void prepare(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepare start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO);
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO, appSubmissionDto);
        String amendTypeValue = ParamUtil.getString(bpc.request,"AmendTypeValue");
        if(!StringUtil.isEmpty(amendTypeValue)){
            ParamUtil.setSessionAttr(bpc.request,"AmendTypeValue",amendTypeValue);
        }
        log.debug(StringUtil.changeForLog("the do prepare end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription prepareDraft
     */
    public void prepareDraft(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareDraft start ...."));
        ParamUtil.setSessionAttr(bpc.request, RfcConst.DODRAFTCONFIG,null);
        String draftNo = ParamUtil.getMaskedString(bpc.request, "DraftNumber");
        removeSession(bpc.request);
        loadingDraft(bpc,draftNo);
        log.debug(StringUtil.changeForLog("the do prepareDraft end ...."));
    }


    /**
     *
     * @param bpc
     * @Decription doChoose
     */
    public void doChoose(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do start doChoose ...."));
        String action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if("back".equals(action)){
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/main-web/eservice/INTERNET/MohInternetInbox")
                    .append("?init_to_page=initLic");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            return;
        }
        String amendType = ParamUtil.getString(bpc.request, "amendType");
        boolean flag = true;
        String licenceId = (String) ParamUtil.getSessionAttr(bpc.request, RfcConst.LICENCEID);
        LicenceDto licenceDto = requestForChangeService.getLicenceDtoByLicenceId(licenceId);
        String UNID=ParamUtil.getString(bpc.request, "UNID");
        if(StringUtil.isEmpty(amendType)){
            flag = false;
            String errMsg = MessageUtil.getMessageDesc("RFC_ERR010");
            ParamUtil.setRequestAttr(bpc.request, "ErrorMsg", errMsg);
            //set audit
            Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
            errorMap.put("ErrorMsg",errMsg);
            WebValidationHelper.saveAuditTrailForNoUseResult(licenceDto,errorMap);
        }
        if(licenceDto != null && UNID==null) {
            String status = licenceDto.getStatus();
            if (!ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(status) &&
                    !(ApplicationConsts.LICENCE_STATUS_APPROVED.equals(status) && IaisEGPHelper.isActiveMigrated())) {
                String errMsg = "licence status is not active";
                ParamUtil.setRequestAttr(bpc.request, "ErrorMsg", errMsg);
                flag = false;
                //set audit
                Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                errorMap.put("ErrorMsg",errMsg);
                WebValidationHelper.saveAuditTrailForNoUseResult(licenceDto,errorMap);
            }
        }
        if(flag){
            if(AppConsts.NO.equals(amendType)){
                //....
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "doTranfer");
                flag = true;
                ParamUtil.setSessionAttr(bpc.request,"UEN",null);
                ParamUtil.setSessionAttr(bpc.request,"email",null);
                ParamUtil.setSessionAttr(bpc.request,"reason",null);
                ParamUtil.setSessionAttr(bpc.request,"hasSubLicensee",null);
                ParamUtil.setSessionAttr(bpc.request,"dto",null);
                ParamUtil.setSessionAttr(bpc.request,"hasNewSubLicensee",null);
                ParamUtil.setSessionAttr(bpc.request,"subLicensee",null);
                ParamUtil.setSessionAttr(bpc.request,"subLicenseeError",null);
                ParamUtil.setSessionAttr(bpc.request,"subLicenseeDto",null);
                ParamUtil.setSessionAttr(bpc.request,"premisesInput",null);
                ParamUtil.setSessionAttr(bpc.request,"appPremisesSpecialDocDto",null);
            }else if(AppConsts.YES.equals(amendType)){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "doAmend");
            }
        }
        if(!flag){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prepare");
            ParamUtil.setRequestAttr(bpc.request, "AmendType", amendType);
        }
        ParamUtil.setSessionAttr(bpc.request,"AmendTypeValue", amendType);
        ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.SEESION_FILES_MAP_AJAX + "selectedFile", null);
        log.debug(StringUtil.changeForLog("the do doChoose end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription jump
     */
    public void jump(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do jump start ...."));
        String crudActionTypeForm = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(StringUtil.isEmpty(crudActionTypeForm)){
            crudActionTypeForm = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM);
        }


        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM, crudActionTypeForm);
        log.debug(StringUtil.changeForLog("the do jump end ...."));
    }

    /**
     * @param bpc
     * @Decription prepareFirstView
     */
    public void prepareFirstView(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do prepareFirstView start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.RFCAPPSUBMISSIONDTO);
        // prepare AppEditSelectDto and some service info
        appSubmissionService.setPreviewDta(appSubmissionDto, bpc);
        // init
        DealSessionUtil.initView(appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.RFCAPPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO, appSubmissionDto);
        log.debug(StringUtil.changeForLog("the do prepareFirstView end ...."));
    }

    /**
     * @param bpc
     * @Decription doFirstView
     */
    public void doFirstView(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doFirstView start ...."));
        String switchValue = ParamUtil.getString(bpc.request, RfcConst.SWITCH_VALUE);
        if ("back".equals(switchValue)) {
            ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH, "prepare");
            return;
        }
        String editValue = ParamUtil.getString(bpc.request, "EditValue");
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.RFCAPPSUBMISSIONDTO);
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto() == null ? new AppEditSelectDto() : appSubmissionDto.getAppEditSelectDto();
        String switchVal = "prepareFirstView";
        if (!StringUtil.isEmpty(editValue)) {
            switchVal = "doEdit";
            switch (editValue) {
                case RfcConst.EDIT_LICENSEE:
                    appEditSelectDto.setLicenseeEdit(true);
                    ParamUtil.setRequestAttr(bpc.request, RfcConst.RFC_CURRENT_EDIT, RfcConst.EDIT_LICENSEE);
                    break;
                case RfcConst.EDIT_PREMISES:
                    appEditSelectDto.setPremisesEdit(true);
                    ParamUtil.setRequestAttr(bpc.request, RfcConst.RFC_CURRENT_EDIT, RfcConst.EDIT_PREMISES);
                    break;
                case RfcConst.EDIT_SPECIALISED:
                    appEditSelectDto.setSpecialisedEdit(true);
                    ParamUtil.setRequestAttr(bpc.request, RfcConst.RFC_CURRENT_EDIT, RfcConst.EDIT_SPECIALISED);
                    break;
                case RfcConst.EDIT_SERVICE:
                    appEditSelectDto.setServiceEdit(true);
                    ParamUtil.setRequestAttr(bpc.request, RfcConst.RFC_CURRENT_EDIT, RfcConst.EDIT_SERVICE);
                    break;
            }
            //appEditSelectDto.setDoEdit(switchVal);
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDto.setClickEditPage(null);
            ParamUtil.setRequestAttr(bpc.request, RfcConst.APPSUBMISSIONDTORFCATTR, appSubmissionDto);
            ParamUtil.setRequestAttr(bpc.request, "appType", ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        }
        ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH, switchVal);
        log.debug(StringUtil.changeForLog("the do doFirstView end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription doBack
     */
    public void doBack(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doBack start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO);
        ParamUtil.setSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO,appSubmissionDto);

        log.debug(StringUtil.changeForLog("the do doBack end ...."));
    }


    /**
     * @param bpc
     * @Decription prepareTranfer
     */
    public void prepareTranfer(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prepareTranfer start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO);
        String serviceName = (String)ParamUtil.getSessionAttr(bpc.request,"SvcName");
        if(!StringUtil.isEmpty(serviceName)){
            appSubmissionDto.setServiceName(serviceName);
        }
        if(!appSubmissionDto.isGroupLic()){
            String premisesIndexNo = appSubmissionDto.getAppGrpPremisesDtoList().get(0).getPremisesIndexNo();
            ParamUtil.setRequestAttr(bpc.request, "premisesIndexNo", premisesIndexNo);
        }

        Map<String, File> map = (Map<String, File>) ParamUtil.getSessionAttr(bpc.request,IaisEGPConstant.SEESION_FILES_MAP_AJAX + "selectedFile");
        List<PageShowFileDto> pageShowFileDtos = FileUtils.transForFileMapToPageShowFileDto(map);

        int maxFile = systemParamConfig.getUploadFileLimit();
        ParamUtil.setSessionAttr(bpc.request, "prepareTranfer", appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, "AppSubmissionDto", appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, "maxFile", maxFile);
        ParamUtil.setRequestAttr(bpc.request, "pageShowFileDtos", pageShowFileDtos);
        bpc.request.getSession().removeAttribute("appSubmissionDtos");
        log.debug(StringUtil.changeForLog("the do prepareTranfer end ...."));
    }


    /**
     * @param bpc
     * @Decription compareChangePercentage
     */
    public void preparePage(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("The preparePage start ..."));
         prepareTranfer(bpc);

        log.info(StringUtil.changeForLog("The preparePage end ..."));
    }
    /**
     * @param bpc
     * @Decription doTransfer
     */
    public void doTransfer(BaseProcessClass bpc)throws CloneNotSupportedException,IOException{
        log.info(StringUtil.changeForLog("The compareChangePercentage start ..."));
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String licenceId = (String) ParamUtil.getSessionAttr(bpc.request, RfcConst.LICENCEID);
        String uen = (String) ParamUtil.getSessionAttr(bpc.request, "UEN");
        String[] selectCheakboxs = null;

        selectCheakboxs =  (String[])ParamUtil.getSessionAttr(bpc.request,"premisesInput");

        String email = ParamUtil.getString(mulReq,"email");
        String reason = ParamUtil.getString(mulReq,"reason");

        String[] confirms = ParamUtil.getStrings(mulReq, "confirm");
        log.info(StringUtil.changeForLog("The compareChangePercentage licenceId is -->:"+licenceId));
        log.info(StringUtil.changeForLog("The compareChangePercentage uen is -->:"+uen));
        Map<String,String> error = doValidateEmpty(uen,selectCheakboxs,email,"subLicensee");
        boolean isEmail = ValidationUtils.isEmail(email);
        if(!isEmail){
            error.put("emailError","GENERAL_ERR0014");
        }
        if(confirms == null || confirms.length == 0){
            error.put("confirmError","RFC_ERR004");
        }
        if(StringUtil.isNotEmpty(reason) && reason.length()>=300){
            String general_err0041= AppValidatorHelper.repLength("This","300");
            error.put("reasonError",general_err0041);
        }
        Map<String, File> map = (Map<String, File>) ParamUtil.getSessionAttr(bpc.request,IaisEGPConstant.SEESION_FILES_MAP_AJAX + "selectedFile");
        List<AppPremisesSpecialDocDto> appPremisesSpecialDocDtos = IaisCommonUtils.genNewArrayList();
        if(map == null || map.size()==0){
            error.put("selectedFileError",MessageUtil.replaceMessage("GENERAL_ERR0006","Letter of Undertaking","field"));
        }
        if(error.isEmpty()){
            LicenceDto licenceDto = requestForChangeService.getLicDtoById(licenceId);
            LicenseeDto licenseeDto = requestForChangeService.getLicenseeByUenNo(uen);
            doValidateLojic(uen,error,licenceDto,licenseeDto);
            if(error.isEmpty()){
                WebValidationHelper.saveAuditTrailForNoUseResult(licenceDto,error);
                String newLicenseeId="";
                if(licenseeDto!=null){
                    newLicenseeId = licenseeDto.getId();
                }else{
                    return;
                }
                log.info(StringUtil.changeForLog("The newLicenseeId is -->:"+newLicenseeId));
                //sub licensee
                SubLicenseeDto subLicenseeDto = null;
                log.info(StringUtil.changeForLog("The licenseeDto.getLicenseeType() is -->:"+licenseeDto.getLicenseeType()));
                if(OrganizationConstants.LICENSEE_TYPE_SINGPASS.equals(licenseeDto.getLicenseeType())){
                    OrganizationDto organizationDto = serviceConfigService.findOrganizationByUen(uen);
                    if(organizationDto != null) {
                        List<SubLicenseeDto> subLicenseeDtos = licenceViewService.getSubLicenseeDto(organizationDto.getId());
                        if (!IaisCommonUtils.isEmpty(subLicenseeDtos)) {
                            subLicenseeDto =  subLicenseeDtos.get(0);
                        }
                    }
                }else {
                    subLicenseeDto = (SubLicenseeDto)ParamUtil.getSessionAttr(bpc.request,"subLicenseeDto");
                }
                if(subLicenseeDto == null){
                    log.error(StringUtil.changeForLog("this uen can not get the subLicenseeDto" + uen));
                    error.put("uenError","Data Error !!!");
                }
            }
        }
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO);
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO,appSubmissionDto);

//        Map<AppSubmissionDto,List<String>> errorListMap = IaisCommonUtils.genNewHashMap();
//        List<String> errorList = appSubmissionService.doPreviewSubmitValidate(null, appSubmissionDto, false);
//        if (!errorList.isEmpty()) {
//            errorListMap.put(appSubmissionDto, errorList);
//        }
//        if (!errorListMap.isEmpty()) {
//            bpc.request.setAttribute(NewApplicationConstant.SHOW_OTHER_ERROR, ApplicationHelper.getErrorMsg(errorListMap));
//            error.put(NewApplicationConstant.SHOW_OTHER_ERROR,"The data is incomplete.");
//        }
        if(!error.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"errorMsg" , WebValidationHelper.generateJsonStr(error));
            ParamUtil.setRequestAttr(bpc.request,"UEN",uen);
            ParamUtil.setRequestAttr(bpc.request,"email",email);
            ParamUtil.setRequestAttr(bpc.request,"reason",reason);
            log.info(StringUtil.changeForLog("The selectCheakboxs.toString() is -->:"+ArrayUtils.toString(selectCheakboxs)));
            ParamUtil.setRequestAttr(bpc.request,"selectCheakboxs",ArrayUtils.toString(selectCheakboxs));
            ParamUtil.setRequestAttr(bpc.request,"crud_action_type_confirm","validate");
        }else{
            List<PageShowFileDto> pageShowFileDtos = FileUtils.transForFileMapToPageShowFileDto(map);
            ParamUtil.setRequestAttr(bpc.request, "pageShowFileDtos", pageShowFileDtos);
            ParamUtil.setSessionAttr(bpc.request,"email",email);
            ParamUtil.setSessionAttr(bpc.request,"reason",reason);
            ParamUtil.setRequestAttr(bpc.request,"crud_action_type_confirm","confirm");
        }

        log.info(StringUtil.changeForLog("The compareChangePercentage end ..."));
    }

    /**
     * @param bpc
     * @Decription doTransfer
     */
    public void doSubmit(BaseProcessClass bpc)throws CloneNotSupportedException,IOException{
        log.info(StringUtil.changeForLog("The doSubmit start ..."));
        //MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        AppSubmissionDto appSubmissionDto  = (AppSubmissionDto)ParamUtil.getSessionAttr(bpc.request,"prepareTranfer");
        String licenceId = (String) ParamUtil.getSessionAttr(bpc.request, RfcConst.LICENCEID);
        String uen = (String) ParamUtil.getSessionAttr(bpc.request, "UEN");
        String[] selectCheakboxs = null;

        selectCheakboxs =  (String[])ParamUtil.getSessionAttr(bpc.request,"premisesInput");

        String email = (String)ParamUtil.getSessionAttr(bpc.request,"email");
        String reason = (String)ParamUtil.getSessionAttr(bpc.request,"reason");

        log.info(StringUtil.changeForLog("The doSubmit licenceId is -->:"+licenceId));
        log.info(StringUtil.changeForLog("The doSubmit uen is -->:"+uen));
        Map<String,String> error = doValidateEmpty(uen,selectCheakboxs,email,"subLicensee");
        boolean isEmail = ValidationUtils.isEmail(email);
        if(!isEmail){
            error.put("emailError","GENERAL_ERR0014");
        }
        if(StringUtil.isNotEmpty(reason) && reason.length()>=300){
            String general_err0041= AppValidatorHelper.repLength("This","300");
            error.put("reasonError",general_err0041);
        }
        Map<String, File> map = (Map<String, File>) ParamUtil.getSessionAttr(bpc.request,IaisEGPConstant.SEESION_FILES_MAP_AJAX + "selectedFile");
        List<AppPremisesSpecialDocDto> appPremisesSpecialDocDtos = IaisCommonUtils.genNewArrayList();
        if(map == null || map.size()==0){
            error.put("selectedFileError",MessageUtil.replaceMessage("GENERAL_ERR0006","Letter of Undertaking","field"));
        }else{
            log.info(StringUtil.changeForLog("The map size is -->:"+map.size()));
            List<File> files = new ArrayList<File>(map.values());
            List<String> fileIds = appSubmissionService.saveFileList(files);
            for (int i = 0;i<files.size();i++){
                File file = files.get(i);
                String fileRepoGuid = fileIds.get(i);
                AppPremisesSpecialDocDto appPremisesSpecialDocDto = new AppPremisesSpecialDocDto();
                log.info(StringUtil.changeForLog("The fileRepoGuid is -->:"+fileRepoGuid));
                Long size= file.length()/1024;
                AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
                appPremisesSpecialDocDto.setDocName(file.getName());
                appPremisesSpecialDocDto.setMd5Code(FileUtils.getFileMd5(file));
                appPremisesSpecialDocDto.setFileRepoId(fileRepoGuid);
                appPremisesSpecialDocDto.setDocSize(Integer.valueOf(size.toString()));
                appPremisesSpecialDocDto.setIndex(String.valueOf(i));
                appPremisesSpecialDocDto.setSubmitBy(auditTrailDto.getMohUserGuid());
                appPremisesSpecialDocDto.setSubmitDt(new Date());
                appPremisesSpecialDocDtos.add(appPremisesSpecialDocDto);
            }
        }
        if(error.isEmpty()){
            LicenceDto licenceDto = requestForChangeService.getLicDtoById(licenceId);
            LicenseeDto licenseeDto = requestForChangeService.getLicenseeByUenNo(uen);
            doValidateLojic(uen,error,licenceDto,licenseeDto);
            if(error.isEmpty()){
                WebValidationHelper.saveAuditTrailForNoUseResult(licenceDto,error);
                String newLicenseeId="";
                if(licenseeDto!=null){
                    newLicenseeId = licenseeDto.getId();
                }else{
                    return;
                }
                log.info(StringUtil.changeForLog("The newLicenseeId is -->:"+newLicenseeId));
                appSubmissionDto = requestForChangeService.getAppSubmissionDtoByLicenceId(licenceId);
                appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                appSubmissionDto.setNewLicenseeId(newLicenseeId);
                appSubmissionDto.setLicenseeId(licenceDto.getLicenseeId());
                appSubmissionDto.setAutoRfc(false);
                //sub licensee
                SubLicenseeDto subLicenseeDto = null;
                log.info(StringUtil.changeForLog("The licenseeDto.getLicenseeType() is -->:"+licenseeDto.getLicenseeType()));
                if(OrganizationConstants.LICENSEE_TYPE_SINGPASS.equals(licenseeDto.getLicenseeType())){
                    OrganizationDto organizationDto = serviceConfigService.findOrganizationByUen(uen);
                    if(organizationDto != null) {
                        List<SubLicenseeDto> subLicenseeDtos = licenceViewService.getSubLicenseeDto(organizationDto.getId());
                        if (!IaisCommonUtils.isEmpty(subLicenseeDtos)) {
                            subLicenseeDto =  subLicenseeDtos.get(0);
                        }
                    }
                }else {
                    subLicenseeDto = (SubLicenseeDto)ParamUtil.getSessionAttr(bpc.request,"subLicenseeDto");
                }
                if(subLicenseeDto != null){
                    subLicenseeDto.setId(null);
                    subLicenseeDto.setUenNo(uen);
                    subLicenseeDto.setOrgId(licenseeDto.getOrganizationId());
                    appSubmissionDto.setSubLicenseeDto(subLicenseeDto);

                    /*String baseServiceId = requestForChangeService.baseSpecLicenceRelation(licenceDto,false);
                    log.info(StringUtil.changeForLog("The baseServiceId is -->:"+baseServiceId));
                    appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).setBaseServiceId(baseServiceId);*/
                    boolean isCharity = ApplicationHelper.isCharity(bpc.request);
                    AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
                    amendmentFeeDto.setChangeInLicensee(Boolean.TRUE);

                    amendmentFeeDto.setIsCharity(isCharity);
                    amendmentFeeDto.setAddress(appSubmissionDto.getAppGrpPremisesDtoList().get(0).getAddress());
                    amendmentFeeDto.setServiceName(appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceName());
                    amendmentFeeDto.setAppGrpNo(appSubmissionDto.getAppGrpNo());

                    FeeDto feeDto = configCommService.getGroupAmendAmount(amendmentFeeDto);
                    if(feeDto != null){
                        Double amount = feeDto.getTotal();
                        if(licenceDto.getStatus().equals(ApplicationConsts.LICENCE_STATUS_APPROVED)&&licenceDto.getMigrated()==1&& IaisEGPHelper.isActiveMigrated()){
                            amount=0.0;
                        }
                        appSubmissionDto.setAmount(amount);
                        log.info(StringUtil.changeForLog("The amount.length is -->:"+amount));
                        log.info(StringUtil.changeForLog("The selectCheakboxs.length is -->:"+selectCheakboxs.length));
                        log.info(StringUtil.changeForLog("The appSubmissionDto.getAppGrpPremisesDtoList().size() is -->:"
                                +appSubmissionDto.getAppGrpPremisesDtoList().size()));
                        if (selectCheakboxs.length == appSubmissionDto.getAppGrpPremisesDtoList().size()) {
                            for (AppGrpPremisesDto appGrpPremisesDto: appSubmissionDto.getAppGrpPremisesDtoList()) {
                                appGrpPremisesDto.setNeedNewLicNo(Boolean.FALSE);
                                appGrpPremisesDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_All_TRANSFER);
                            }
                        } else {
                            for (AppGrpPremisesDto appGrpPremisesDto : appSubmissionDto.getAppGrpPremisesDtoList()) {
                                String premise = appGrpPremisesDto.getPremisesIndexNo();
                                boolean isSelect  = isSelect(selectCheakboxs,premise);
                                if(isSelect){
                                    appGrpPremisesDto.setNeedNewLicNo(Boolean.TRUE);
                                    appGrpPremisesDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_TRANSFER);
                                }else {
                                    appGrpPremisesDto.setNeedNewLicNo(Boolean.FALSE);
                                    appGrpPremisesDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_ORIGIN);
                                }
                            }
                        }
                        String grpNo = appSubmissionService.getGroupNo(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                        log.info(StringUtil.changeForLog("The grpNo is -->:"+grpNo));
                        appSubmissionDto.setAppGrpNo(grpNo);
                        List<String> serviceNames = IaisCommonUtils.genNewArrayList();
                        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSubmissionDto.getAppSvcRelatedInfoDtoList()) {
                            serviceNames.add(appSvcRelatedInfoDto.getServiceName());
                        }
                        List<HcsaServiceDto> hcsaServiceDtos = serviceConfigService.getHcsaServiceByNames(serviceNames);
                        ParamUtil.setRequestAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, hcsaServiceDtos);
                        ApplicationHelper.setSubmissionDtoSvcData(bpc.request, appSubmissionDto);
                        appSubmissionService.setRiskToDto(appSubmissionDto);

                        String draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
                        log.info(StringUtil.changeForLog("the draftNo -->:"+ draftNo) );
                        appSubmissionDto.setDraftNo(draftNo);

                        //file
                        List<AppPremisesSpecialDocDto> appPremisesSpecialDocDtoList = IaisCommonUtils.genNewArrayList();
                        if(!IaisCommonUtils.isEmpty(appPremisesSpecialDocDtos)){
                            log.info(StringUtil.changeForLog("the appPremisesSpecialDocDtos size is -->:" + appPremisesSpecialDocDtos.size()));
                            appPremisesSpecialDocDtoList.addAll(appPremisesSpecialDocDtos);
                            appSubmissionDto.setAppPremisesSpecialDocDtos(appPremisesSpecialDocDtoList);
                        }
                        //save the email to the app_group_misc
                        List<AppGroupMiscDto> appGroupMiscDtoList = IaisCommonUtils.genNewArrayList();
                        if(!StringUtil.isEmpty(email)){
                            AppGroupMiscDto appGroupMiscDto = new AppGroupMiscDto();
                            appGroupMiscDto.setMiscType(ApplicationConsts.APP_GROUP_MISC_TYPE_TRANSFER_EMAIL);
                            appGroupMiscDto.setMiscValue(email);
                            appGroupMiscDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                            appGroupMiscDtoList.add(appGroupMiscDto);
                        }
                        if(!StringUtil.isEmpty(reason)){
                            AppGroupMiscDto appGroupMiscDto = new AppGroupMiscDto();
                            appGroupMiscDto.setMiscType(ApplicationConsts.APP_GROUP_MISC_TYPE_TRANSFER_REASON);
                            appGroupMiscDto.setMiscValue(reason);
                            appGroupMiscDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                            appGroupMiscDtoList.add(appGroupMiscDto);
                        }
                        appSubmissionDto.setAppGroupMiscDtos(appGroupMiscDtoList);
                         AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
                        appEditSelectDto.setNeedNewLicNo(true);
                        appEditSelectDto.setAuto(AppConsts.NO);
                        appCommService.checkAffectedAppSubmissions(appSubmissionDto, null, feeDto, draftNo, grpNo,
                                appEditSelectDto, null);
                        appSubmissionDto.setGetAppInfoFromDto(false);
                        setAppPremSubSvcRelDtoStatus(appSubmissionDto,ApplicationConsts.RECORD_STATUS_APPROVE_CODE);
                        AppSubmissionDto tranferSub = requestForChangeService.submitChange(appSubmissionDto);
                        ParamUtil.setSessionAttr(bpc.request, "app-rfc-tranfer", tranferSub);
                        if (amount == null || MiscUtil.doubleEquals(amount, 0.0)) {
                            List<AppSubmissionDto> ackPageAppSubmission=new ArrayList<>(1);
                            ackPageAppSubmission.add(appSubmissionDto);
                            bpc.request.getSession().setAttribute("ackPageAppSubmissionDto",ackPageAppSubmission);
                        } else {
                            bpc.request.getSession().removeAttribute("ackPageAppSubmissionDto");
                        }
                        StringBuilder url = new StringBuilder();
                        url.append("https://").append(bpc.request.getServerName())
                                .append(RfcConst.PAYMENTPROCESS);
                        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
                    }
                }else{
                    log.error(StringUtil.changeForLog("this uen can not get the subLicenseeDto" + uen));
                    error.put("uenError","Data Error !!!");
                }
            }
        }
        if(!error.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"errorMsg" , WebValidationHelper.generateJsonStr(error));
            ParamUtil.setRequestAttr(bpc.request,"UEN",uen);
            ParamUtil.setRequestAttr(bpc.request,"email",email);
            ParamUtil.setRequestAttr(bpc.request,"reason",reason);
            log.info(StringUtil.changeForLog("The selectCheakboxs.toString() is -->:"+ArrayUtils.toString(selectCheakboxs)));
            ParamUtil.setRequestAttr(bpc.request,"selectCheakboxs",ArrayUtils.toString(selectCheakboxs));
        }
        log.info(StringUtil.changeForLog("The doSubmit end ..."));
    }

    private void setAppPremSubSvcRelDtoStatus(AppSubmissionDto appSubmissionDto,String status){
        log.info(StringUtil.changeForLog("The setAppPremSubSvcRelDtoStatus start ..."));
        List<AppPremSpecialisedDto> appPremSpecialisedDtoList = appSubmissionDto.getAppPremSpecialisedDtoList();
        if(IaisCommonUtils.isNotEmpty(appPremSpecialisedDtoList)){
            log.info(StringUtil.changeForLog("The appPremSpecialisedDtoList.size() is -->:"+appPremSpecialisedDtoList.size()));
            for(AppPremSpecialisedDto appPremSpecialisedDto : appPremSpecialisedDtoList){
               List<AppPremSubSvcRelDto> appPremSubSvcRelDtoList = appPremSpecialisedDto.getAppPremSubSvcRelDtoList();
                setAppPremSubSvcRelDtoStatusDetail(appPremSubSvcRelDtoList,status);
           }
        }else{
           log.info(StringUtil.changeForLog("The appPremSpecialisedDtoList is null"));
        }
        log.info(StringUtil.changeForLog("The setAppPremSubSvcRelDtoStatus end ..."));
    }

    private void setAppPremSubSvcRelDtoStatusDetail(List<AppPremSubSvcRelDto> appPremSubSvcRelDtoList,String status){
        log.info(StringUtil.changeForLog("The setAppPremSubSvcRelDtoStatusDetail start ..."));
        if(IaisCommonUtils.isNotEmpty(appPremSubSvcRelDtoList)){
            log.info(StringUtil.changeForLog("The appPremSubSvcRelDtoList.size() is -->:"+appPremSubSvcRelDtoList.size()));
            for(AppPremSubSvcRelDto appPremSubSvcRelDto : appPremSubSvcRelDtoList){
                appPremSubSvcRelDto.setStatus(status);
                List<AppPremSubSvcRelDto> subAppPremSubSvcRelDtoList = appPremSubSvcRelDto.getAppPremSubSvcRelDtos();
                setAppPremSubSvcRelDtoStatusDetail(subAppPremSubSvcRelDtoList,status);
            }
        }
        log.info(StringUtil.changeForLog("The setAppPremSubSvcRelDtoStatusDetail end ..."));
    }

    /**
     * @param bpc
     * @Decription compareChangePercentage
     */
    public void doValidate(BaseProcessClass bpc) throws CloneNotSupportedException,IOException {
        log.info(StringUtil.changeForLog("The doValidate start ..."));
        ParamUtil.setSessionAttr(bpc.request,"hasSubLicensee",null);
        ParamUtil.setSessionAttr(bpc.request,"hasNewSubLicensee",null);
        ParamUtil.setSessionAttr(bpc.request,"subLicenseeDto",null);
        AppSubmissionDto appSubmissionDto  = (AppSubmissionDto)ParamUtil.getSessionAttr(bpc.request,"prepareTranfer");
        String licenceId = (String) ParamUtil.getSessionAttr(bpc.request, RfcConst.LICENCEID);
        String uen = ParamUtil.getString(bpc.request, "UEN");
        String subLicensee = ParamUtil.getString(bpc.request, "subLicensee");
        String[] selectCheakboxs = null;
        if(appSubmissionDto.isGroupLic()){
             selectCheakboxs = ParamUtil.getStrings(bpc.request, "premisesInput");
        }else {
            String premisesIndexNo = (String)ParamUtil.getSessionAttr(bpc.request,"premisesIndexNo");
            selectCheakboxs = new String[]{premisesIndexNo};
            log.info(StringUtil.changeForLog("The doValidate premisesIndexNo is -->:"+premisesIndexNo));
        }

        ParamUtil.setSessionAttr(bpc.request,"UEN",uen);
        ParamUtil.setSessionAttr(bpc.request, "subLicensee", subLicensee);
        ParamUtil.setSessionAttr(bpc.request,"premisesInput",selectCheakboxs);
        log.info(StringUtil.changeForLog("The doValidate licenceId is -->:"+licenceId));
        log.info(StringUtil.changeForLog("The doValidate uen is -->:"+uen));
        log.info(StringUtil.changeForLog("The doValidate subLicensee is -->:"+subLicensee));
        LicenceDto licenceDto = requestForChangeService.getLicDtoById(licenceId);
        Map<String,String> error = doValidateEmpty(uen,selectCheakboxs,"Email","subLicensee");
        if(error.isEmpty()){
            LicenseeDto licenseeDto = requestForChangeService.getLicenseeByUenNo(uen);
            doValidateLojic(uen,error,licenceDto,licenseeDto);
            if(error.isEmpty()){
                if(licenseeDto != null && licenceDto != null){
                    if(OrganizationConstants.LICENSEE_TYPE_CORPPASS.equals(licenseeDto.getLicenseeType())){
                        error = doValidateEmpty(uen,selectCheakboxs,"Email",subLicensee);
                        if(error.isEmpty()) {
                            if(subLicensee.equals(licenceDto.getSubLicenseeId())){
                                error.put("subLicenseeError","RFC_ERR021");
                            }
                        }
                        if(error.isEmpty() && !"new".equals(subLicensee)) {
                            SubLicenseeDto subLicenseeDto = licenceViewService.getSubLicenseesById(subLicensee);
                            if (subLicenseeDto != null) {
                                ParamUtil.setSessionAttr(bpc.request, "hasSubLicensee", Boolean.TRUE);
                                ParamUtil.setSessionAttr(bpc.request, "subLicenseeDto", subLicenseeDto);
                            }else{
                                log.error(StringUtil.changeForLog("This id can not get th subLicensee -->:"+subLicensee));
                            }
                        }
                    }else if(licenceDto.getLicenseeId().equals(licenseeDto.getId())){
                        error.put("uenError","RFC_ERR021");
                    }
                }
            }
        }
        String  subLicenseeError = error.get("subLicenseeError");
        if(!StringUtil.isEmpty(subLicenseeError)){
            ParamUtil.setSessionAttr(bpc.request,"subLicenseeError",subLicenseeError);
        }
        ParamUtil.setRequestAttr(bpc.request,"errorMsg" , WebValidationHelper.generateJsonStr(error));
        ParamUtil.setRequestAttr(bpc.request,"UEN",uen);
        log.info(StringUtil.changeForLog("The selectCheakboxs.toString() is -->:"+ArrayUtils.toString(selectCheakboxs)));
        ParamUtil.setRequestAttr(bpc.request,"selectCheakboxs",ArrayUtils.toString(selectCheakboxs));
        if(!error.isEmpty()){
            WebValidationHelper.saveAuditTrailForNoUseResult(licenceDto,error);
            ParamUtil.setRequestAttr(bpc.request,"isValidate","N");
        }else{
            prepareTranfer(bpc);
            ParamUtil.setRequestAttr(bpc.request,"isValidate","Y");
        }
        log.info(StringUtil.changeForLog("The doValidate end ..."));
    }


    /**
     * @param bpc
     * @Decription submitPayment
     */
    public void submitPayment(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do submitPayment start ...."));
        AppSubmissionDto appSubmissionDto=(AppSubmissionDto) ParamUtil.getRequestAttr(bpc.request, "tranferPayment");

        ParamUtil.setRequestAttr(bpc.request, "tranferPayment", appSubmissionDto);
        log.debug(StringUtil.changeForLog("the do submitPayment end ...."));

    }
    /**
     *
     * @param bpc
     * @Decription prepareAddLicensee
     */
    public void prepareAddLicensee(BaseProcessClass bpc){
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO);
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO,appSubmissionDto);
    }
    /**
     *
     * @param bpc
     * @Decription prepareCond
     */
    public void prepareCond(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("The prepareCond  start..."));
        log.info(StringUtil.changeForLog("The prepareCond  isValidate is -->"+
                ParamUtil.getRequestAttr(bpc.request,"isValidate")));
        log.info(StringUtil.changeForLog("The prepareCond  end..."));
    }

    private void loadingDraft(BaseProcessClass bpc,String draftNo){
        log.info(StringUtil.changeForLog("the do loadingDraft start ...."));
        //draftNo = "DQ2003030005426";
        String action = "doAmend";
        if(!StringUtil.isEmpty(draftNo)){
            log.info(StringUtil.changeForLog("draftNo is not empty"));
            bpc.request.setAttribute("RFC_DRAFT_NO",draftNo);
            AppSubmissionDto appSubmissionDto = serviceConfigService.getAppSubmissionDtoDraft(draftNo);
//            if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())||ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){
//                AppDataHelper.initDeclarationFiles(appSubmissionDto.getAppDeclarationDocDtos(),appSubmissionDto.getAppType(),bpc.request);
//            }
            if(appSubmissionDto.getAppGrpPremisesDtoList() != null && appSubmissionDto.getAppGrpPremisesDtoList().size() >0){
                /*List<AppDeclarationDocDto> appDeclarationDocDtos = appSubmissionDto.getAppDeclarationDocDtos();
                if(appDeclarationDocDtos!=null){
                    List<PageShowFileDto> pageShowFileDtos =new ArrayList<>(5);
                    for (AppDeclarationDocDto v : appDeclarationDocDtos) {
                        PageShowFileDto pageShowFileDto=new PageShowFileDto();
                        pageShowFileDto.setSize(v.getDocSize());
                        pageShowFileDto.setFileName(v.getDocName());
                        pageShowFileDto.setVersion(v.getVersion());
                        pageShowFileDto.setFileMapId("selectedFileDiv"+v.getSeqNum());
                        pageShowFileDto.setMd5Code(v.getMd5Code());
                        pageShowFileDto.setFileUploadUrl(v.getFileRepoId());
                        pageShowFileDto.setIndex(String.valueOf(v.getSeqNum()));
                        pageShowFileDtos.add(pageShowFileDto);
                    }
                    bpc.request.getSession().setAttribute("pageShowFileDtos",pageShowFileDtos);
                }*/
                ParamUtil.setSessionAttr(bpc.request, RfcConst.RFCAPPSUBMISSIONDTO, appSubmissionDto);
            }else{
                ParamUtil.setSessionAttr(bpc.request, RfcConst.RFCAPPSUBMISSIONDTO, null);
            }
            ParamUtil.setSessionAttr(bpc.request, RfcConst.DODRAFTCONFIG,"test");
            //set max file index into session
            Integer maxFileIndex = appSubmissionDto.getMaxFileIndex();
            if(maxFileIndex == null){
                maxFileIndex = 0;
            }else{
                maxFileIndex ++;
            }
            String svcName = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceName();
            ParamUtil.setSessionAttr(bpc.request,"SvcName",svcName);
            ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR,maxFileIndex);
        }else{
            action = "error";
            ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.ACKMESSAGE,"error !!!");
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM, action);
        log.info(StringUtil.changeForLog("the do loadingDraft end ...."));
    }

    private boolean canTransfer(List<LicenseeKeyApptPersonDto> oldLicenseeKeyApptPersonDtos,
                                List<LicenseeKeyApptPersonDto> licenseeKeyApptPersonDtoList){
        boolean canTransfer = true;
        if(IaisCommonUtils.isEmpty(oldLicenseeKeyApptPersonDtos)||IaisCommonUtils.isEmpty(licenseeKeyApptPersonDtoList)){
            canTransfer = false;
        }else{
            int existCount = 0;
            for(LicenseeKeyApptPersonDto newLicenseeKeyApptPersonDto : licenseeKeyApptPersonDtoList){
                for(LicenseeKeyApptPersonDto oldLicenseeKeyApptPersonDto1 : oldLicenseeKeyApptPersonDtos){
                    if(newLicenseeKeyApptPersonDto.getIdType().equals(oldLicenseeKeyApptPersonDto1.getIdType())
                            && newLicenseeKeyApptPersonDto.getIdNo().equals(oldLicenseeKeyApptPersonDto1.getIdNo())){
                        existCount = existCount+1;
                        break;
                    }
                }
            }
            log.info(StringUtil.changeForLog("The  existCount  is -->:"+existCount));
            log.info(StringUtil.changeForLog("The  licenseeKeyApptPersonDtoList.size()  is -->:"+licenseeKeyApptPersonDtoList.size()));
            log.info(StringUtil.changeForLog("The  oldLicenseeKeyApptPersonDtos.size()  is -->:"+oldLicenseeKeyApptPersonDtos.size()));
            int totleCount = licenseeKeyApptPersonDtoList.size();
            if(oldLicenseeKeyApptPersonDtos.size() > licenseeKeyApptPersonDtoList.size()){
                totleCount =  oldLicenseeKeyApptPersonDtos.size();
            }
            log.info(StringUtil.changeForLog("The  totleCount  is -->:"+totleCount));
            int diffrent = totleCount - existCount;
            log.info(StringUtil.changeForLog("The  diffrent  is -->:"+diffrent));
            BigDecimal ratio = new BigDecimal(diffrent).divide(new BigDecimal(totleCount));
            log.info(StringUtil.changeForLog("The  ratio  is -->:"+ratio));
            if(ratio.compareTo(BigDecimal.valueOf(0.5))>=0){
                canTransfer = false;
            }else{
                log.info(StringUtil.changeForLog("can transfer"));
            }
        }

        return canTransfer;

    }



    private boolean isSelect(String[] selectCheakboxs,String premisesIndexNo){
        boolean isSelect = false;
        if(!StringUtil.isEmpty(premisesIndexNo) && selectCheakboxs != null){
            for(String select : selectCheakboxs){
                if(premisesIndexNo.equals(select)){
                    isSelect = true;
                    break;
                }
            }
        }
        return isSelect;
    }


    private  Map<String,String> doValidateEmpty(String uen,String[] selectCheakboxs,String email,String subLicensee){
        Map<String,String> error = IaisCommonUtils.genNewHashMap();
        if(selectCheakboxs == null || selectCheakboxs.length == 0){
            error.put("premisesError","RFC_ERR005");
        }
        String msgGenError006 = MessageUtil.replaceMessage("GENERAL_ERR0006","UEN of Licensee to transfer licence to","field");
        if(StringUtil.isEmpty(subLicensee)){
            error.put("subLicenseeError", msgGenError006);
        }
        if(StringUtil.isEmpty(uen) || uen.length() > 10){
            error.put("uenError", msgGenError006);
        }else{
            try{
                String acra = ConfigHelper.getString("moh.halp.acra.enable","");
                if(AcraConsts.YES.equals(acra)){
                    licenseeClient.getEntityByUEN(uen);
                }
            }catch (Throwable e){
             log.error(StringUtil.changeForLog("The gent uen info throw exception"+e.getMessage()));
            }
        }
        if(StringUtil.isEmpty(email)){
            error.put("emailError","GENERAL_ERR0006");
        }if(email.length()>=320){
            String general_err0041= AppValidatorHelper.repLength("This","320");
            error.put("emailError",general_err0041);
        }
        return error;
    }
    private Map<String,String> doValidateLojic(String uen,Map<String,String> error,LicenceDto licenceDto,LicenseeDto licenseeDto){
        if(licenceDto==null){
            error.put("uenError","NEW_ERR0010");
        }else{
            if(licenseeDto == null){
                error.put("uenError","RFC_ERR002");
            }else{
                String svcName = licenceDto.getSvcName();
                log.info(StringUtil.changeForLog("The doValidate svcName is -->:"+svcName));
                if(AppServicesConsts.SERVICE_NAME_EMERGENCY_AMBULANCE_SERVICE.equals(svcName)
                        || AppServicesConsts.SERVICE_NAME_MEDICAL_TRANSPORT_SERVICE.equals(svcName) ){
                    if(!isSameUEN(uen,licenceDto)){
                        List<HcsaServiceDto> hcsaServiceDtos = IaisCommonUtils.genNewArrayList();
                        HcsaServiceDto hcsaServiceDto = new HcsaServiceDto();
                        hcsaServiceDto.setSvcName(svcName);
                        hcsaServiceDtos.add(hcsaServiceDto);
                        boolean canCreateEasOrMts = appSubmissionService.canApplyEasOrMts(licenseeDto.getId(),hcsaServiceDtos);
                        log.info(StringUtil.changeForLog("The doValidate canCreateEasOrMts is -->:"+canCreateEasOrMts));
                        if(!canCreateEasOrMts){
                            error.put("uenError","RFC_ERR022");
                            return error;
                        }
                    }else{
                        log.info(StringUtil.changeForLog("The same UEN ..."));
                    }
                }
                //if(OrganizationConstants.LICENSEE_TYPE_CORPPASS.equals(licenseeDto.getLicenseeType())){
                    if(!licenceDto.getLicenseeId().equals(licenseeDto.getId())){
                        List<LicenseeKeyApptPersonDto> oldLicenseeKeyApptPersonDtos = requestForChangeService.
                                getLicenseeKeyApptPersonDtoListByLicenseeId(licenceDto.getLicenseeId());
                        List<LicenseeKeyApptPersonDto> licenseeKeyApptPersonDtoList = requestForChangeService.getLicenseeKeyApptPersonDtoListByUen(uen);
                        boolean canTransfer = canTransfer(oldLicenseeKeyApptPersonDtos,licenseeKeyApptPersonDtoList);
                        if(!canTransfer){
                            error.put("uenError","RFC_ERR007");
                        }
                    }else{
                        log.info(StringUtil.changeForLog("The same uen -->:"+uen));
                    }
//                }else{
//                    log.info(StringUtil.changeForLog("This is the solo uen-->:"+uen));
//                }
            }
        }
        return error;
    }



    private void sendEmail(String appId, String templateHtml, List<String> emailAddr, Map<String,Object> map, String subject){
        EmailDto email = new EmailDto();
        String mesContext = null;
        try {
            mesContext = MsgUtil.getTemplateMessageByContent(templateHtml, map);
            email.setReqRefNum(appId);
            email.setSubject(subject);
            email.setContent(mesContext);
            email.setSender(mailSender);
            email.setClientQueryCode(appId);
            email.setReceipts(emailAddr);
            requestForChangeService.sendNotification(email);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
