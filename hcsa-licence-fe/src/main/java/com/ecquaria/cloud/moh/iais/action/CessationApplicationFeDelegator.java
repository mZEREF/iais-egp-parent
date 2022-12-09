package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranet.user.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessatonConfirmDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AppDataHelper;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.CessationFeService;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/2/7 13:12
 */
@Delegator("CessationApplication")
@Slf4j
public class CessationApplicationFeDelegator {

    @Autowired
    private CessationFeService cessationFeService;

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

    private static final String APPCESSATIONDTOS = "appCessationDtos";
    private static final String READINFO = "readInfo";
    private static final String PRELIMINARYQUESTIONKINDLY = "preliminaryQuestionKindly";
    private static final String SELECTEDFILEERROR = "selectedCessFileError";
    private static final String ISBEFORE = "isBefore";
    private static final String ISSURRENDERING = "isSurrendering";
    private static final String WHICHTODO = "whichTodo";
    private static final String EFFECTIVEDATE = "effectiveDate";
    private static final String REASON = "reason";
    private static final String OTHERREASON = "otherReason";
    private static final String PATRADIO = "patRadio";
    private static final String PATIENTSELECT = "patientSelect";
    private static final String PATNOREMARKS = "patNoRemarks";
    private static final String PATNOCONFIRM = "patNoConfirm";
    private static final String PATHCINAME = "patHciName";
    private static final String PATREGNO = "patRegNo";
    private static final String PATOTHERS = "patOthersTakeOver";
    private static final String PATOTHERSMOBILENO = "patOthersMobileNo";
    private static final String PATOTHERSEMAILADDRESS = "patOthersEmailAddress";
    private static final String TRANSFERREDWHERE = "transferredWhere";
    private static final String TRANSFERDETAIL = "transferDetail";
    private static final String APPSUBMISSIONDTO = "AppSubmissionDto";
    private static final String ERROR = "GENERAL_ERR0006";
    static String[] arrReason = new String[]{ApplicationConsts.CESSATION_REASON_NOT_PROFITABLE, ApplicationConsts.CESSATION_REASON_REDUCE_WORKLOA, ApplicationConsts.CESSATION_REASON_OTHER};
    static String[] arrPatients = new String[]{ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI, ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO, ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_OTHER};


    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>CessationApplicationDelegator");
        String rfiAppId = "";
        String rfiPremiseId = "";
        try {
            rfiAppId = ParamUtil.getMaskedString(bpc.request, "appId");
            rfiPremiseId = ParamUtil.getMaskedString(bpc.request, "premiseId");
        } catch (MaskAttackException e) {
            log.error(e.getMessage(), e);
            try {
                IaisEGPHelper.redirectUrl(bpc.response, "https://" + bpc.request.getServerName() + "/hcsa-licence-web/CsrfErrorPage.jsp");
            } catch (IOException ioe) {
                log.error(ioe.getMessage(), ioe);
                return;
            }
        }

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_CESSATION, AuditTrailConsts.FUNCTION_CESSATION);
        ParamUtil.setSessionAttr(bpc.request, APPCESSATIONDTOS, null);
//        ParamUtil.setSessionAttr(bpc.request, "specLicInfo", null);
//        ParamUtil.setSessionAttr(bpc.request, "specLicInfoFlag", null);
        ParamUtil.setSessionAttr(bpc.request, "rfiAppId", null);
        ParamUtil.setSessionAttr(bpc.request, "rfiPremiseId", null);
        ParamUtil.setSessionAttr(bpc.request, "rfiAppId", rfiAppId);
        ParamUtil.setSessionAttr(bpc.request, "rfiPremiseId", rfiPremiseId);
        ParamUtil.setSessionAttr(bpc.request, "isGrpLic", null);
        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,null);
        ParamUtil.setSessionAttr(bpc.request, RenewalConstants.RENEW_DTO,null);
        ParamUtil.setSessionAttr(bpc.request,"viewPrint",null);
        ParamUtil.setSessionAttr(bpc.request, "selectedCessFileDocShowPageDto", null);
        ParamUtil.setSessionAttr(bpc.request, "seesion_files_map_ajax_feselectedCessFile", null);
        ParamUtil.setSessionAttr(bpc.request, "seesion_files_map_ajax_feselectedCessFile_MaxIndex", null);
        int configFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setSessionAttr(bpc.request, "configFileSize",configFileSize);
        ParamUtil.setRequestAttr(bpc.request,"declaration_page_request","cessation");
        ParamUtil.setSessionAttr(bpc.request,"declaration_page_is","cessation");

        String cess_ack002 = MessageUtil.getMessageDesc("CESS_ACK002");

        ParamUtil.setSessionAttr(bpc.request,"cess_ack002",cess_ack002);
    }

    public void init(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("The init start ..."));
        List<String> licIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "licIds");

       /* String rfiAppId = (String) ParamUtil.getSessionAttr(bpc.request, "rfiAppId");
        String rfiPremiseId = (String) ParamUtil.getSessionAttr(bpc.request, "rfiPremiseId");
        if (!StringUtil.isEmpty(rfiAppId) && !StringUtil.isEmpty(rfiPremiseId)) {
            List<AppCessLicDto> appCessLicDtos = cessationFeService.initRfiData(rfiAppId, rfiPremiseId);
            appCessDtosByLicIds = appCessLicDtos;
        }*/
       /* if (!IaisCommonUtils.isEmpty(licIds)) {
            boolean isGrpLicence = cessationFeService.isGrpLicence(licIds);
            //specLid in licIds
            List<String> specLicIds = cessationFeService.filtrateSpecLicIds(licIds);
            List<AppSpecifiedLicDto> specLicInfo = cessationFeService.getSpecLicInfo(licIds);
            if (specLicInfo.size() > 0) {
                Map<String, List<AppSpecifiedLicDto>> map = IaisCommonUtils.genNewHashMap();
                for (AppSpecifiedLicDto appSpecifiedLicDto : specLicInfo) {
                    String specLicId = appSpecifiedLicDto.getSpecLicId();
                    String baseLicNo = appSpecifiedLicDto.getBaseLicNo();
                    if (specLicIds.contains(specLicId)) {
                        licIds.remove(specLicId);
                    }
                    List<AppSpecifiedLicDto> specLicInfoConfirmExist = map.get(baseLicNo);
                    if (!IaisCommonUtils.isEmpty(specLicInfoConfirmExist)) {
                        specLicInfoConfirmExist.add(appSpecifiedLicDto);
                    } else {
                        List<AppSpecifiedLicDto> specLicInfoConfirm = IaisCommonUtils.genNewArrayList();
                        specLicInfoConfirm.add(appSpecifiedLicDto);
                        map.put(baseLicNo, specLicInfoConfirm);
                    }
                }
                ParamUtil.setSessionAttr(bpc.request, "specLicInfo", (Serializable) map);
            }
            appCessDtosByLicIds = cessationFeService.getAppCessDtosByLicIds(licIds);
            ParamUtil.setSessionAttr(bpc.request, "isGrpLic", isGrpLicence);
        }*/

        List<AppCessLicDto> appCessDtosByLicIds = cessationFeService.getAppCessDtosByLicIds(licIds);

       // int size = appCessDtosByLicIds.size();
        List<SelectOption> reasonOption = getReasonOption();
        List<SelectOption> patientsOption = getPatientsOption();
        ParamUtil.setSessionAttr(bpc.request, APPCESSATIONDTOS, (Serializable) appCessDtosByLicIds);
        ParamUtil.setSessionAttr(bpc.request, "reasonOption", (Serializable) reasonOption);
        ParamUtil.setSessionAttr(bpc.request, "patientsOption", (Serializable) patientsOption);
        //ParamUtil.setSessionAttr(bpc.request, "size", size);
        ParamUtil.setSessionAttr(bpc.request, READINFO, null);
        ParamUtil.setSessionAttr(bpc.request, "licIds", (Serializable) licIds);
        log.info(StringUtil.changeForLog("The init end ..."));
    }

    public void prepareData(BaseProcessClass bpc) {
    }

    public void valiant(BaseProcessClass bpc) throws IOException {
        String action_type = ParamUtil.getRequestString(bpc.request, "crud_action_type");
        if ("back".equals(action_type)) {
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            return;
        }
        List<String> licIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "licIds");
        List<AppCessLicDto> appCessDtosByLicIds = (List<AppCessLicDto>) ParamUtil.getSessionAttr(bpc.request, APPCESSATIONDTOS);
        //int size = (int) ParamUtil.getSessionAttr(bpc.request, "size");

        List<AppCessLicDto> appCessLicDtos = prepareDataForValiant(bpc, appCessDtosByLicIds);

        /*List<AppCessLicDto> cloneAppCessHciDtos = IaisCommonUtils.genNewArrayList();
        CopyUtil.copyMutableObjectList(appCessHciDtos, cloneAppCessHciDtos);
        List<AppCessLicDto> confirmDtos = getConfirmDtos(cloneAppCessHciDtos);*/
        ParamUtil.setSessionAttr(bpc.request, APPCESSATIONDTOS, (Serializable) appCessLicDtos);

        /*String readInfo = ParamUtil.getRequestString(bpc.request, READINFO);
        ParamUtil.setSessionAttr(bpc.request, READINFO, readInfo);*/
        Map<String, String> errorMap = doValidate(appCessLicDtos);
       /* Boolean choose = Boolean.FALSE;
        for (int i = 1; i <= size; i++) {
            int size1 = appCessDtosByLicIds.get(i - 1).getAppCessHciDtos().size();
            for (int j = 1; j <= size1; j++) {
                String whichTodo = ParamUtil.getRequestString(bpc.request, i + WHICHTODO + j);
                if (!StringUtil.isEmpty(whichTodo)) {
                    choose = Boolean.TRUE;
                    break;
                }
            }
        }
        if (!choose) {
            errorMap.put("choose", MessageUtil.getMessageDesc("CESS_ERR003"));
        }*/
        /*if (confirmDtos.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            return;
        }*/
        /*for (int i = 1; i <= size; i++) {
            int size1 = appCessHciDtos.get(i - 1).getAppCessHciDtos().size();
            for (int j = 1; j <= size1; j++) {
                String whichTodo = ParamUtil.getRequestString(bpc.request, i + WHICHTODO + j);
                if (!StringUtil.isEmpty(whichTodo)) {
                    Map<String, String> validate = validate(bpc, i, j);
                    errorMap.putAll(validate);
                }
            }
        }*/
        if (!errorMap.isEmpty()) {
            if (!IaisCommonUtils.isEmpty(licIds)) {
                if (licIds.size() == 1) {
                    LicenceDto licenceDto = licenceClient.getLicBylicId(licIds.get(0)).getEntity();
                    WebValidationHelper.saveAuditTrailForNoUseResult(licenceDto, errorMap);
                }
            }
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request,"declaration_page_request","cessation");
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            return;
        }

        List<AppCessationDto> appCessationDtos = transformDto(appCessLicDtos);
        //ParamUtil.setSessionAttr(bpc.request, "confirmDtos", (Serializable) confirmDtos);
        ParamUtil.setRequestAttr(bpc.request, "printFlag","Y");
        ParamUtil.setRequestAttr(bpc.request, "viewPrint","Y");
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
        ParamUtil.setRequestAttr(bpc.request,"declaration_page_request","cessation");
        ParamUtil.setRequestAttr(bpc.request,"declaration_page_confirm","cessation");
        ParamUtil.setSessionAttr(bpc.request, "appCessationDtosSave", (Serializable) appCessationDtos);
    }

    public void action(BaseProcessClass bpc) {
        String action_type = ParamUtil.getRequestString(bpc.request, "crud_action_type");
        if ("submit".equals(action_type)) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
        } else if ("back".equals(action_type)) {
            ParamUtil.setRequestAttr(bpc.request,"declaration_page_request","cessation");
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
        }
    }

    public void saveData(BaseProcessClass bpc) throws Exception {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String rfiAppId = (String) ParamUtil.getSessionAttr(bpc.request, "rfiAppId");
        String rfiPremiseId = (String) ParamUtil.getSessionAttr(bpc.request, "rfiPremiseId");
        List<AppCessationDto> appCessationDtos = (List<AppCessationDto>) ParamUtil.getSessionAttr(bpc.request, "appCessationDtosSave");

        if (!StringUtil.isEmpty(rfiAppId) && !StringUtil.isEmpty(rfiPremiseId)) {
            cessationFeService.saveRfiCessations(appCessationDtos, loginContext, rfiAppId);
            Map<String, List<String>> appIdPremisesMap = IaisCommonUtils.genNewHashMap();
            List<String> rfiAppIds = IaisCommonUtils.genNewArrayList();
            rfiAppIds.add(rfiAppId);
            appIdPremisesMap.put(rfiPremiseId, rfiAppIds);
            List<AppCessatonConfirmDto> confirmDto = cessationFeService.getConfirmDto(appCessationDtos, appIdPremisesMap, loginContext);
            ParamUtil.setSessionAttr(bpc.request, "appCessConDtos", (Serializable) confirmDto);
        } else {
            Map<String, List<String>> appIdPremisesMap = cessationFeService.saveCessations(appCessationDtos, loginContext);
            List<AppCessatonConfirmDto> confirmDto = cessationFeService.getConfirmDto(appCessationDtos, appIdPremisesMap, loginContext);
            ParamUtil.setSessionAttr(bpc.request, "appCessConDtos", (Serializable) confirmDto);
        }
    }

    public void response(BaseProcessClass bpc) throws IOException {
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }



    private  AppDeclarationMessageDto getAppDeclarationMessageDto(BaseProcessClass bpc){
        AppDeclarationMessageDto appDeclarationMessageDto = new AppDeclarationMessageDto();
        String preliminaryquestionkindly = ParamUtil.getRequestString(bpc.request, PRELIMINARYQUESTIONKINDLY);
        String isbefore = ParamUtil.getRequestString(bpc.request, ISBEFORE);
        String issurrendering = ParamUtil.getRequestString(bpc.request, ISSURRENDERING);
        appDeclarationMessageDto.setAppType(ApplicationConsts.APPLICATION_TYPE_CESSATION);
        appDeclarationMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        appDeclarationMessageDto.setPreliminaryQuestionKindly(preliminaryquestionkindly);
        appDeclarationMessageDto.setPreliminaryQuestionItem1(isbefore);
        appDeclarationMessageDto.setPreliminaryQuestiontem2(issurrendering);
        return appDeclarationMessageDto;
    }

    /*
     * utils
     */

    private List<AppCessLicDto> prepareDataForValiant(BaseProcessClass bpc, List<AppCessLicDto> appCessDtosByLicIds) {
        //for Declaration Message
        AppDeclarationMessageDto appDeclarationMessageDto = getAppDeclarationMessageDto(bpc);
        List<AppDeclarationDocDto> cessationDocData = AppDataHelper.getDeclarationFiles(ApplicationConsts.APPLICATION_TYPE_CESSATION,
                bpc.request);
        DealSessionUtil.initDeclarationFiles(cessationDocData,ApplicationConsts.APPLICATION_TYPE_CESSATION,bpc.request);
        for (int i = 1; i <= appCessDtosByLicIds.size(); i++) {
            AppCessLicDto appCessLicDto = appCessDtosByLicIds.get(i - 1);
            List<AppCessHciDto> appCessHciDtoso = appCessLicDto.getAppCessHciDtos();
            for (int j = 1; j <= appCessHciDtoso.size(); j++) {
                AppCessHciDto appCessHciDto = appCessHciDtoso.get(j - 1);
                String whichTodo = ParamUtil.getRequestString(bpc.request, i + WHICHTODO + j);
                String effectiveDateStr = ParamUtil.getRequestString(bpc.request, i + EFFECTIVEDATE + j);
                Date effectiveDate = DateUtil.parseDate(effectiveDateStr, AppConsts.DEFAULT_DATE_FORMAT);
                String reason = ParamUtil.getRequestString(bpc.request, i + REASON + j);
                String otherReason = ParamUtil.getRequestString(bpc.request, i + OTHERREASON + j);
                String patRadio = ParamUtil.getRequestString(bpc.request, i + PATRADIO + j);
                Boolean patNeedTrans = null;
                if ("yes".equals(patRadio)) {
                    patNeedTrans = Boolean.TRUE;
                } else if ("no".equals(patRadio)) {
                    patNeedTrans = Boolean.FALSE;
                }
                /*String patientSelect = ParamUtil.getRequestString(bpc.request, i + PATIENTSELECT + j);
                String patNoRemarks = ParamUtil.getRequestString(bpc.request, i + PATNOREMARKS + j);
                String patNoConfirm = ParamUtil.getRequestString(bpc.request, i + PATNOCONFIRM + j);
                String patHciName = ParamUtil.getRequestString(bpc.request, i + PATHCINAME + j);
                String patRegNo = ParamUtil.getRequestString(bpc.request, i + PATREGNO + j);
                String patOthers = ParamUtil.getRequestString(bpc.request, i + PATOTHERS + j);
                String patMobile = ParamUtil.getRequestString(bpc.request, i + PATOTHERSMOBILENO + j);
                String patEmailAddress = ParamUtil.getRequestString(bpc.request, i + PATOTHERSEMAILADDRESS + j);
                String readInfo = ParamUtil.getRequestString(bpc.request, READINFO);
                String hciName = appCessHciDto.getHciName();
                String hciAddress = appCessHciDto.getHciAddress();*/
                String transferredWhere = ParamUtil.getRequestString(bpc.request, i + TRANSFERREDWHERE + j);
                String transferDetail = ParamUtil.getRequestString(bpc.request, i + TRANSFERDETAIL + j);

              /*  appCessHciDto.setHciAddress(hciAddress);
                if (!StringUtil.isEmpty(patHciName)) {
                    PremisesDto premisesDto = cessationFeService.getPremiseByHciCodeName(patHciName);
                    if (premisesDto != null) {
                        String hciAddressPat = premisesDto.getHciAddress();
                        String hciNamePat = premisesDto.getHciName();
                        String hciCodePat = premisesDto.getHciCode();
                        appCessHciDto.setHciNamePat(hciNamePat);
                        appCessHciDto.setHciCodePat(hciCodePat);
                        appCessHciDto.setHciAddressPat(hciAddressPat);
                    }
                }
                appCessHciDto.setHciName(hciName);*/
                appCessHciDto.setEffectiveDate(effectiveDate);
                appCessHciDto.setReason(reason);
                appCessHciDto.setOtherReason(otherReason);
                appCessHciDto.setPatNeedTrans(patNeedTrans);
                /*appCessHciDto.setPatientSelect(patientSelect);
                appCessHciDto.setPatNoRemarks(patNoRemarks);
                appCessHciDto.setPatNoConfirm(patNoConfirm);
                appCessHciDto.setPatHciName(patHciName);
                appCessHciDto.setPatRegNo(patRegNo);
                appCessHciDto.setPatOthers(patOthers);
                appCessHciDto.setMobileNo(patMobile);
                appCessHciDto.setEmailAddress(patEmailAddress);
                appCessHciDto.setReadInfo(readInfo);*/
                appCessHciDto.setPremiseIdChecked(whichTodo);
                appCessHciDto.setTransferredWhere(transferredWhere);
                appCessHciDto.setTransferDetail(transferDetail);
            }
            appCessLicDto.setAppCessHciDtos(appCessHciDtoso);
            appCessLicDto.setAppDeclarationDocDtos(cessationDocData);
            appCessLicDto.setAppDeclarationMessageDto(appDeclarationMessageDto);
        }
        return appCessDtosByLicIds;
    }

    private List<AppCessationDto> transformDto(List<AppCessLicDto> appCessLicDtos) {
        List<AppCessationDto> appCessationDtos = IaisCommonUtils.genNewArrayList();
        for (AppCessLicDto appCessLicDto : appCessLicDtos) {
            String licenceId = appCessLicDto.getLicenceId();
            List<AppCessHciDto> appCessHciDtos = appCessLicDto.getAppCessHciDtos();
            List<AppDeclarationDocDto> appDeclarationDocDtoList = appCessLicDto.getAppDeclarationDocDtos();
            AppDeclarationMessageDto appDeclarationMessageDto = appCessLicDto.getAppDeclarationMessageDto();
            if (appCessHciDtos != null && !appCessHciDtos.isEmpty()) {
                for (AppCessHciDto appCessHciDto : appCessHciDtos) {
                    String whichTodo = appCessHciDto.getPremiseIdChecked();
                    if (!StringUtil.isEmpty(whichTodo)) {
                        Date effectiveDate = appCessHciDto.getEffectiveDate();
                        String reason = appCessHciDto.getReason();
                        String otherReason = appCessHciDto.getOtherReason();
                        Boolean patNeedTrans = appCessHciDto.getPatNeedTrans();
                        String patientSelect = appCessHciDto.getPatientSelect();
                        String patTransType = appCessHciDto.getPatTransType();
                        String patTransTo = appCessHciDto.getPatTransTo();
                        String patRegNo = appCessHciDto.getPatRegNo();
                        String patNoRemarks = appCessHciDto.getPatNoRemarks();
                        String patOthers = appCessHciDto.getPatOthers();
                        String readInfo = appCessHciDto.getReadInfo();
                        String patHciName = appCessHciDto.getPatHciName();
                        String mobileNo = appCessHciDto.getMobileNo();
                        String emailAddress = appCessHciDto.getEmailAddress();

                        AppCessationDto appCessationDto = new AppCessationDto();
                        appCessationDto.setEffectiveDate(effectiveDate);
                        appCessationDto.setReason(reason);
                        appCessationDto.setOtherReason(otherReason);
                        appCessationDto.setPatNeedTrans(patNeedTrans);
                        appCessationDto.setPatientSelect(patientSelect);
                        appCessationDto.setPatTransType(patTransType);
                        appCessationDto.setPatTransTo(patTransTo);
                        appCessationDto.setPatRegNo(patRegNo);
                        appCessationDto.setPatNoRemarks(patNoRemarks);
                        appCessationDto.setPatOthers(patOthers);
                        appCessationDto.setPatHciName(patHciName);
                        appCessationDto.setPremiseId(whichTodo);
                        appCessationDto.setReadInfo(readInfo);
                        appCessationDto.setLicId(licenceId);
                        appCessationDto.setMobileNo(mobileNo);
                        appCessationDto.setEmailAddress(emailAddress);
                        appCessationDto.setTransferDetail(appCessHciDto.getTransferDetail());
                        appCessationDto.setTransferredWhere(appCessHciDto.getTransferredWhere());
                        appCessationDto.setAppDeclarationMessageDto(appDeclarationMessageDto);
                        appCessationDto.setAppDeclarationDocDtoList(appDeclarationDocDtoList);
                        appCessationDtos.add(appCessationDto);
                    }
                }
            }

        }
        return appCessationDtos;
    }

    /*private List<AppCessLicDto> getConfirmDtos(List<AppCessLicDto> appCessLicDtos) {
        for (AppCessLicDto appCessLicDto : appCessLicDtos) {
            List<AppCessHciDto> appCessHciDtos = appCessLicDto.getAppCessHciDtos();
            if (appCessHciDtos != null && !appCessHciDtos.isEmpty()) {
                List<AppCessHciDto> list = IaisCommonUtils.genNewArrayList();
                for (AppCessHciDto appCessHciDto : appCessHciDtos) {
                    String premiseIdChecked = appCessHciDto.getPremiseIdChecked();
                    if (StringUtil.isEmpty(premiseIdChecked)) {
                        list.add(appCessHciDto);
                    }
                }
                appCessHciDtos.removeAll(list);
            }
        }
        for (int i = 0; i < appCessLicDtos.size(); i++) {
            List<AppCessHciDto> appCessHciDtos = appCessLicDtos.get(i).getAppCessHciDtos();
            if (appCessHciDtos.size() == 0) {
                appCessLicDtos.remove(i);
                i = i - 1;
            }
        }
        return appCessLicDtos;
    }*/

    private Map<String, String> doValidate(List<AppCessLicDto> appCessLicDtos) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String otherError = MessageUtil.replaceMessage(ERROR, "Others", "field");
        if(IaisCommonUtils.isNotEmpty(appCessLicDtos)){
            for (int i = 1; i <= appCessLicDtos.size(); i++) {
                AppCessLicDto appCessLicDto = appCessLicDtos.get(i - 1);
                List<AppCessHciDto> appCessHciDtos = appCessLicDto.getAppCessHciDtos();
                for (int j = 1; j <= appCessHciDtos.size(); j++) {
                    AppCessHciDto appCessHciDto = appCessHciDtos.get(j - 1);
                    Date effectiveDate = appCessHciDto.getEffectiveDate();
                    if (effectiveDate == null) {
                        errorMap.put(i + EFFECTIVEDATE + j, MessageUtil.replaceMessage(ERROR, "Effective Date", "field"));
                    }
                    String reason = appCessHciDto.getReason();
                    if (StringUtil.isEmpty(reason)) {
                        errorMap.put(i + REASON + j, MessageUtil.replaceMessage(ERROR, "Cessation Reasons", "field"));
                    }
                    String otherReason = appCessHciDto.getOtherReason();
                    if (ApplicationConsts.CESSATION_REASON_OTHER.equals(reason)) {
                        if (StringUtil.isEmpty(otherReason)) {
                            errorMap.put(i + OTHERREASON + j, otherError);
                        }
                    }
                    Boolean patRadio = appCessHciDto.getPatNeedTrans();
                    if (patRadio == null) {
                        errorMap.put(i + PATRADIO + j, MessageUtil.replaceMessage(ERROR, "Patients' Record will be transferred", "field"));
                    }

                    String transferredWhere = appCessHciDto.getTransferredWhere();
                    String transferDetail = appCessHciDto.getTransferDetail(); String general_err0041= AppValidatorHelper.repLength("this","1000");
                    if ("yes".equals(patRadio)) {
                        if (!StringUtil.isEmpty(transferredWhere) && transferredWhere.length()>1000) {
                            errorMap.put(i + TRANSFERREDWHERE + j, general_err0041);
                        }
                    }
                    if ("no".equals(patRadio)) {
                        //String errMsg=MessageUtil.replaceMessage(ERROR, "Reason for no patients' records transfer", "field");
                        if (!StringUtil.isEmpty(transferDetail) && transferDetail.length()>1000) {
                            errorMap.put(i + TRANSFERDETAIL + j, general_err0041);
                        }
                    }
                }
                //for Declaration Message
                if(1 == i){
                    AppDeclarationMessageDto appDeclarationMessageDto = appCessLicDto.getAppDeclarationMessageDto();
                    String preliminaryquestionkindly = appDeclarationMessageDto.getPreliminaryQuestionKindly();
                    String isbefore = appDeclarationMessageDto.getPreliminaryQuestionItem1();
                    String issurrendering = appDeclarationMessageDto.getPreliminaryQuestiontem2();

                    if (StringUtil.isEmpty(preliminaryquestionkindly)){
                        errorMap.put(PRELIMINARYQUESTIONKINDLY, otherError);
                    }else{
                        if (AppConsts.NO.equals(preliminaryquestionkindly)){
                            List<AppDeclarationDocDto> cessationDocData = appCessLicDto.getAppDeclarationDocDtos();
                            if (cessationDocData == null){
                                errorMap.put(SELECTEDFILEERROR, otherError);
                            }
                        }
                    }
                    if (StringUtil.isEmpty(isbefore)){
                        errorMap.put(ISBEFORE, otherError);
                    }
                    if (StringUtil.isEmpty(issurrendering)){
                        errorMap.put(ISSURRENDERING, otherError);
                    }
                }
            }
        }
        return errorMap;
    }

    @GetMapping(value = "/hci-info")
    public @ResponseBody
    PremisesDto getPsnSelectInfo(HttpServletRequest request) {
        String hciNameCode = ParamUtil.getDate(request, "hciNameCode");
        PremisesDto premisesDto ;
        try{
            premisesDto = cessationFeService.getPremiseByHciCodeName(hciNameCode);
        }catch (Exception e){
            return null;
        }

        return premisesDto;
    }

    private List<SelectOption> getReasonOption() {
        List<SelectOption> selectOptions = MasterCodeUtil.retrieveOptionsByCodes(arrReason);
        return selectOptions;
    }

    private List<SelectOption> getPatientsOption() {
        List<SelectOption> selectOptions = MasterCodeUtil.retrieveOptionsByCodes(arrPatients);
        return selectOptions;
    }


}