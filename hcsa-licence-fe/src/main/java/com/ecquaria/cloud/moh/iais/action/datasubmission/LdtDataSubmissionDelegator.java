package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsLaboratoryDevelopTestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.LdtSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceFeMsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DsLicenceService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.LdtDataSubmissionService;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Slf4j
@Delegator("ldtDataSubmissionDelegator")
public class LdtDataSubmissionDelegator {

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private LdtDataSubmissionService ldtDataSubmissionService;

    @Autowired
    LicenceFeMsgTemplateClient licenceFeMsgTemplateClient;

    @Autowired
    NotificationHelper notificationHelper;

    @Autowired
    LicenceViewService licenceViewService;

    @Autowired
    DsLicenceService dsLicenceService;

    public static final String CRUD_ACTION_TYPE_LDT = "crud_action_type_ldt";
    public static final String CURRENT_PAGE = "ldt_current_page";

    public static final String ACTION_TYPE_RETURN = "return";
    public static final String ACTION_TYPE_PAGE = "page";
    public static final String ACTION_TYPE_CONFIRM = "confirm";
    public static final String ACTION_TYPE_DRAFT = "draft";
    public static final String ACTION_TYPE_SUBMIT = "submit";

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void start(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Start -----"));
        DataSubmissionHelper.clearSession(bpc.request);

        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if (loginContext != null) {
            String licenseeId = loginContext.getLicenseeId();
            List<LicenceDto> licenceDtos = licenceClient.getLicenceDtosByLicenseeId(licenseeId).getEntity();
            boolean containCLB = containCLB(licenceDtos);
            if (containCLB) {
                setSelectOptions(bpc);
            } else {
                ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.LDT_CANOT_LDT, "Y");
                ParamUtil.setRequestAttr(bpc.request, CRUD_ACTION_TYPE_LDT, ACTION_TYPE_RETURN);
            }
        }

        String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(bpc.request))
                .map(LoginContext::getOrgId).orElse("");
        LdtSuperDataSubmissionDto dataSubmissionDraft = ldtDataSubmissionService.getLdtSuperDataSubmissionDraftByConds(orgId);
        if (dataSubmissionDraft != null) {
            ParamUtil.setRequestAttr(bpc.request, "hasDraft", Boolean.TRUE);
        }

        String isGuide = ParamUtil.getString(bpc.request,DataSubmissionConstant.LDT_IS_GUIDE);
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.LDT_IS_GUIDE, isGuide);
    }

    /**
     * StartStep: Draft
     *
     * @param bpc
     * @throws
     */
    public void draft(BaseProcessClass bpc) {
        String currentStage = (String) ParamUtil.getRequestAttr(bpc.request, CURRENT_PAGE);
        ParamUtil.setRequestAttr(bpc.request, CRUD_ACTION_TYPE_LDT, currentStage);
        LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = DataSubmissionHelper.getCurrentLdtSuperDataSubmissionDto(bpc.request);
        if (ldtSuperDataSubmissionDto != null) {
            if (StringUtil.isEmpty(ldtSuperDataSubmissionDto.getDraftNo())) {
                ldtSuperDataSubmissionDto.setDraftNo(ldtDataSubmissionService.getDraftNo());
            }
            ldtSuperDataSubmissionDto = ldtDataSubmissionService.saveDataSubmissionDraft(ldtSuperDataSubmissionDto);
            DataSubmissionHelper.setCurrentLdtSuperDataSubmissionDto(ldtSuperDataSubmissionDto, bpc.request);
            ParamUtil.setRequestAttr(bpc.request, "saveDraftSuccess", "success");
        } else {
            log.info(StringUtil.changeForLog("The ldtSuperDataSubmissionDto is null"));
        }
    }

    /**
     * StartStep: Submit
     *
     * @param bpc
     * @throws
     */
    public void submit(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Do Submission -----"));
        LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = DataSubmissionHelper.getCurrentLdtSuperDataSubmissionDto(bpc.request);

        DataSubmissionDto dataSubmissionDto = ldtSuperDataSubmissionDto.getDataSubmissionDto();
        CycleDto cycle = ldtSuperDataSubmissionDto.getCycleDto();

        if (StringUtil.isEmpty(dataSubmissionDto.getSubmissionNo())) {
            String submissionNo = ldtDataSubmissionService.getSubmissionNo();
            dataSubmissionDto.setSubmissionNo(submissionNo);
        }

        DsLaboratoryDevelopTestDto dsLaboratoryDevelopTestDto = ldtSuperDataSubmissionDto.getDsLaboratoryDevelopTestDto();
        dsLaboratoryDevelopTestDto.setLdtNo(dataSubmissionDto.getSubmissionNo());

        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(dataSubmissionDto.getAppType())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_AMENDED);
        } else if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
        }

        cycle.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);

        LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
        if (loginContext != null) {
            dataSubmissionDto.setSubmitBy(loginContext.getUserId());
            dataSubmissionDto.setSubmitDt(new Date());
        }
        ldtSuperDataSubmissionDto = ldtDataSubmissionService.saveLdtSuperDataSubmissionDto(ldtSuperDataSubmissionDto);
        try {
            ldtSuperDataSubmissionDto = ldtDataSubmissionService.saveLdtSuperDataSubmissionDtoToBE(ldtSuperDataSubmissionDto);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Eic saveLdtSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
        }
        if (!StringUtil.isEmpty(ldtSuperDataSubmissionDto.getDraftId())) {
            ldtDataSubmissionService.updateDataSubmissionDraftStatus(ldtSuperDataSubmissionDto.getDraftId(),
                    DataSubmissionConsts.DS_STATUS_INACTIVE);
        }
        String licenseeId = loginContext.getLicenseeId();

        LicenseeDto licenseeDto = licenceViewService.getLicenseeDtoBylicenseeId(licenseeId);
        String licenseeDtoName = licenseeDto.getName();
        String submissionNo = ldtSuperDataSubmissionDto.getDataSubmissionDto().getSubmissionNo();
        String licenceId = "";
        List<LicenceDto> licenceDtoList = licenceClient.getLicenceDtoByHciCode(dsLaboratoryDevelopTestDto.getHciCode(), licenseeId).getEntity();
        if (!IaisCommonUtils.isEmpty(licenceDtoList)) {
            LicenceDto licenceDto = licenceDtoList.get(0);
            licenceId = licenceDto.getId();
        }
        try {
            sendNotification(licenseeDtoName, submissionNo, licenceId);
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(), e);
        }

        DataSubmissionHelper.setCurrentLdtSuperDataSubmissionDto(ldtSuperDataSubmissionDto, bpc.request);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.EMAIL_ADDRESS, DataSubmissionHelper.getLicenseeEmailAddrs(bpc.request));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.SUBMITTED_BY, DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
        ParamUtil.setRequestAttr(bpc.request, CURRENT_PAGE, ACTION_TYPE_SUBMIT);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKLDT);
    }

    /**
     * StartStep: PrepareConfirm
     *
     * @param bpc
     * @throws
     */
    public void prepareConfirm(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, CURRENT_PAGE, ACTION_TYPE_CONFIRM);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_LDT);
    }

    /**
     * StartStep: PageAction
     *
     * @param bpc
     * @throws
     */
    public void pageAction(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = DataSubmissionHelper.getCurrentLdtSuperDataSubmissionDto(request);

        DsLaboratoryDevelopTestDto dsLaboratoryDevelopTestDto = transformPageData(request);
        ldtSuperDataSubmissionDto.setDsLaboratoryDevelopTestDto(dsLaboratoryDevelopTestDto);
        DataSubmissionHelper.setCurrentLdtSuperDataSubmissionDto(ldtSuperDataSubmissionDto, request);

        String crud_action_type = ParamUtil.getRequestString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();

        //draft
        if (crud_action_type.equals("resume")) {
            ldtSuperDataSubmissionDto = ldtDataSubmissionService.getLdtSuperDataSubmissionDraftByConds(ldtSuperDataSubmissionDto.getOrgId());
            if (ldtSuperDataSubmissionDto == null) {
                log.warn("Can't resume data!");
                ldtSuperDataSubmissionDto = new LdtSuperDataSubmissionDto();
            }
            DataSubmissionHelper.setCurrentLdtSuperDataSubmissionDto(ldtSuperDataSubmissionDto, bpc.request);
            ParamUtil.setRequestAttr(request, CRUD_ACTION_TYPE_LDT, ACTION_TYPE_PAGE);
            return;
        } else if (crud_action_type.equals("delete")) {
            ldtDataSubmissionService.deleteLdtSuperDataSubmissionDtoDraftByConds(ldtSuperDataSubmissionDto.getOrgId(), DataSubmissionConsts.LDT_TYPE_SBT);
        }

        if (crud_action_type.equals(ACTION_TYPE_CONFIRM)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(dsLaboratoryDevelopTestDto, "save");
            errorMap = validationResult.retrieveAll();
            if (isRfc(request)) {
                LdtSuperDataSubmissionDto oldLdtSuperDataSubmissionDto = DataSubmissionHelper.getOldLdtSuperDataSubmissionDto(request);
                if (errorMap.isEmpty() && oldLdtSuperDataSubmissionDto != null && oldLdtSuperDataSubmissionDto.getDsLaboratoryDevelopTestDto() != null && dsLaboratoryDevelopTestDto.equals(oldLdtSuperDataSubmissionDto.getDsLaboratoryDevelopTestDto())) {
                    ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                    crud_action_type = ACTION_TYPE_PAGE;
                }
            }
        }
        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            crud_action_type = ACTION_TYPE_PAGE;
        } else {
            CycleDto cycleDto = ldtSuperDataSubmissionDto.getCycleDto();
            String hciCode = dsLaboratoryDevelopTestDto.getHciCode();
            ldtSuperDataSubmissionDto.setHciCode(hciCode);
            cycleDto.setHciCode(hciCode);
        }
        ParamUtil.setRequestAttr(request, CRUD_ACTION_TYPE_LDT, crud_action_type);
        ParamUtil.setRequestAttr(bpc.request, CURRENT_PAGE, ACTION_TYPE_PAGE);
    }

    /**
     * StartStep: Return
     *
     * @param bpc
     * @throws
     */
    public void doReturn(BaseProcessClass bpc) throws IOException {
        log.info(" ----- DoReturn ------ ");
        LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = DataSubmissionHelper.getCurrentLdtSuperDataSubmissionDto(bpc.request);
        String cannotCLT = ParamUtil.getRequestString(bpc.request, DataSubmissionConstant.LDT_CANOT_LDT);
        String target = InboxConst.URL_MAIN_WEB_MODULE + "MohInternetInbox";
        if ("Y".equals(cannotCLT) || (ldtSuperDataSubmissionDto != null && DataSubmissionConsts.DS_APP_TYPE_NEW.equals(ldtSuperDataSubmissionDto.getAppType()))) {
            target = InboxConst.URL_LICENCE_WEB_MODULE + "MohDataSubmission";
            ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.LDT_CANOT_LDT, cannotCLT);
            String isGuide = (String) ParamUtil.getSessionAttr(bpc.request, DataSubmissionConstant.LDT_IS_GUIDE);
            if ("true".equals(isGuide)){
                target = InboxConst.URL_MAIN_WEB_MODULE + "MohAccessmentGuide/subDateMoh";
            }
        }
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(target);
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

    /**
     * StartStep: PageConfirmAction
     *
     * @param bpc
     * @throws
     */
    public void pageConfirmAction(BaseProcessClass bpc) {
        String crud_action_type = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        ParamUtil.setRequestAttr(bpc.request, CRUD_ACTION_TYPE_LDT, crud_action_type);
        ParamUtil.setRequestAttr(bpc.request, CURRENT_PAGE, ACTION_TYPE_CONFIRM);
        // declaration
        String[] declaration = ParamUtil.getStrings(bpc.request, "declaration");
        LdtSuperDataSubmissionDto currentLdtSuperDataSubmissionDto = DataSubmissionHelper.getCurrentLdtSuperDataSubmissionDto(bpc.request);
        DataSubmissionDto dataSubmissionDto = currentLdtSuperDataSubmissionDto.getDataSubmissionDto();
        if (declaration != null && declaration.length > 0) {
            dataSubmissionDto.setDeclaration(declaration[0]);
        } else {
            dataSubmissionDto.setDeclaration(null);
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
        //for declaration validate
        if (ACTION_TYPE_SUBMIT.equals(crud_action_type)) {
            if (declaration == null || declaration.length == 0) {
                errorMap.put("declaration", "GENERAL_ERR0006");
            }
        }
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, CRUD_ACTION_TYPE_LDT, ACTION_TYPE_CONFIRM);
        }
    }

    /**
     * StartStep: PrepareStepData
     *
     * @param bpc
     * @throws
     */
    public void prepareStepData(BaseProcessClass bpc) {
        LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = DataSubmissionHelper.getCurrentLdtSuperDataSubmissionDto(bpc.request);
        DsLaboratoryDevelopTestDto dsLaboratoryDevelopTestDto = ldtSuperDataSubmissionDto.getDsLaboratoryDevelopTestDto();
        if (dsLaboratoryDevelopTestDto == null) {
            dsLaboratoryDevelopTestDto = new DsLaboratoryDevelopTestDto();
            ldtSuperDataSubmissionDto.setDsLaboratoryDevelopTestDto(dsLaboratoryDevelopTestDto);
        }

        DataSubmissionHelper.setCurrentLdtSuperDataSubmissionDto(ldtSuperDataSubmissionDto, bpc.request);
        ParamUtil.setRequestAttr(bpc.request, CURRENT_PAGE, ACTION_TYPE_PAGE);
    }


    private void setSelectOptions(BaseProcessClass bpc) {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if (loginContext == null) {
            return;
        }
        List<PremisesDto> entity = dsLicenceService.getLdtCenterPremiseList(loginContext.getOrgId());
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isNotEmpty(entity)) {
            ArrayList<PremisesDto> collect = entity.stream().collect(
                    Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(PremisesDto::getHciCode))), ArrayList::new));
            for (PremisesDto appGrpPremisesDto : collect
            ) {
                selectOptions.add(new SelectOption(appGrpPremisesDto.getHciCode(), appGrpPremisesDto.getPremiseLabel()));
            }
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.LDT_PREMISS_OPTION, (Serializable) selectOptions);
    }

    /**
     * StartStep: PrepareSwitch
     *
     * @param bpc
     * @throws
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        String currentAction = ParamUtil.getRequestString(bpc.request, CRUD_ACTION_TYPE_LDT);
        if (StringUtil.isEmpty(currentAction)) {
            currentAction = ACTION_TYPE_PAGE;
            ParamUtil.setRequestAttr(bpc.request, CRUD_ACTION_TYPE_LDT, currentAction);
        }
        String crudType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if (DataSubmissionConstant.CRUD_TYPE_FROM_DRAFT.equals(crudType) || DataSubmissionConstant.CRUD_TYPE_RFC.equals(crudType)) {
            setSelectOptions(bpc);
        }
        LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = DataSubmissionHelper.getCurrentLdtSuperDataSubmissionDto(bpc.request);
        if (ldtSuperDataSubmissionDto == null) {
            ldtSuperDataSubmissionDto = initLdtSuperDataSubmissionDto(bpc.request);
            DataSubmissionHelper.setCurrentLdtSuperDataSubmissionDto(ldtSuperDataSubmissionDto, bpc.request);
        }
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionHelper.getMainTitle(ldtSuperDataSubmissionDto.getAppType()));
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", DataSubmissionHelper.getSmallTitle(DataSubmissionConsts.DS_LDT,
                ldtSuperDataSubmissionDto.getAppType(), ldtSuperDataSubmissionDto.getSubmissionType()));
    }

    private boolean isRfc(HttpServletRequest request) {
        LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = DataSubmissionHelper.getCurrentLdtSuperDataSubmissionDto(request);
        return ldtSuperDataSubmissionDto != null && ldtSuperDataSubmissionDto.getDataSubmissionDto() != null && DataSubmissionConsts.DS_APP_TYPE_RFC.equalsIgnoreCase(ldtSuperDataSubmissionDto.getDataSubmissionDto().getAppType());
    }

    private DsLaboratoryDevelopTestDto transformPageData(HttpServletRequest request) throws ParseException {
        DsLaboratoryDevelopTestDto dsLaboratoryDevelopTestDto = new DsLaboratoryDevelopTestDto();
        String hciCode = ParamUtil.getString(request, "hciCode");
        String ldtTestName = ParamUtil.getString(request, "ldtTestName");
        String intendedPurpose = ParamUtil.getString(request, "intendedPurpose");
        Date ldtDate = Formatter.parseDate(ParamUtil.getString(request, "ldtDate"));
        String responsePerson = ParamUtil.getString(request, "responsePerson");
        String testStatus = ParamUtil.getString(request, "testStatus");
        String designation = ParamUtil.getString(request, "designation");
        String remarks = ParamUtil.getString(request, "remarks");
        dsLaboratoryDevelopTestDto.setDesignation(designation);
        dsLaboratoryDevelopTestDto.setHciCode(hciCode);
        dsLaboratoryDevelopTestDto.setRemarks(remarks);
        dsLaboratoryDevelopTestDto.setIntendedPurpose(intendedPurpose);
        dsLaboratoryDevelopTestDto.setLdtTestName(ldtTestName);
        dsLaboratoryDevelopTestDto.setResponsePerson(responsePerson);
        dsLaboratoryDevelopTestDto.setTestStatus(testStatus);
        dsLaboratoryDevelopTestDto.setLdtDate(ldtDate);

        if (isRfc(request)) {
            LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = DataSubmissionHelper.getCurrentLdtSuperDataSubmissionDto(request);
            DataSubmissionDto dataSubmissionDto = ldtSuperDataSubmissionDto.getDataSubmissionDto();
            dataSubmissionDto.setAmendReason(ParamUtil.getString(request, "amendReason"));
            if ("LDTRE_002".equals(dataSubmissionDto.getAmendReason())) {
                dataSubmissionDto.setAmendReasonOther(ParamUtil.getString(request, "amendReasonOther"));
            } else {
                dataSubmissionDto.setAmendReasonOther(null);
            }
        }

        return dsLaboratoryDevelopTestDto;
    }

    private LdtSuperDataSubmissionDto initLdtSuperDataSubmissionDto(HttpServletRequest request) {
        LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = new LdtSuperDataSubmissionDto();

        String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(request))
                .map(LoginContext::getOrgId).orElse("");

        ldtSuperDataSubmissionDto.setOrgId(orgId);
        ldtSuperDataSubmissionDto.setSubmissionMethod(DataSubmissionConsts.DS_METHOD_MANUAL_ENTRY);
        ldtSuperDataSubmissionDto.setSubmissionType(DataSubmissionConsts.LDT_TYPE_SBT);
        ldtSuperDataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_NEW);
        ldtSuperDataSubmissionDto.setFe(true);
        ldtSuperDataSubmissionDto.setSvcName(AppServicesConsts.SERVICE_NAME_CLINICAL_LABORATORY);
        ldtSuperDataSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        ldtSuperDataSubmissionDto.setCycleDto(DataSubmissionHelper.initCycleDto(ldtSuperDataSubmissionDto,
                DataSubmissionHelper.getLicenseeId(request), false));
        ldtSuperDataSubmissionDto.setDataSubmissionDto(DataSubmissionHelper.initDataSubmission(ldtSuperDataSubmissionDto, false));

        return ldtSuperDataSubmissionDto;
    }

    private boolean containCLB(List<LicenceDto> licenceDtos) {
        log.info(StringUtil.changeForLog("The containCLB  start ..."));
        boolean result = false;
        if (!IaisCommonUtils.isEmpty(licenceDtos)) {
            for (LicenceDto licenceDto : licenceDtos) {
                if (AppServicesConsts.SERVICE_NAME_CLINICAL_LABORATORY.equals(licenceDto.getSvcName())
                        && ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceDto.getStatus())) {
                    result = true;
                    break;
                }
            }
        } else {
            log.info(StringUtil.changeForLog("The containCLB  licenceDtos is empty"));
        }
        log.info(StringUtil.changeForLog("The containCLB  result is -->:" + result));
        log.info(StringUtil.changeForLog("The containCLB  end ..."));
        return result;
    }

    private void sendNotification(String applicantName, String LDTId, String licenceId) throws IOException, TemplateException {
        MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_LDT_MSG).getEntity();
        Map<String, Object> msgContentMap = IaisCommonUtils.genNewHashMap();
        msgContentMap.put("ApplicantName", applicantName);
        msgContentMap.put("LDTId", LDTId);
        msgContentMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
        Map<String, Object> msgSubjectMap = IaisCommonUtils.genNewHashMap();
        msgSubjectMap.put("LDTId", LDTId);
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), msgSubjectMap);
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LDT_MSG);
        emailParam.setTemplateContent(msgContentMap);
        emailParam.setQueryCode(LDTId);
        emailParam.setReqRefNum(LDTId);
        emailParam.setServiceTypes(DataSubmissionConsts.DS_LDT);
        emailParam.setRefId(licenceId);
        emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        emailParam.setSubject(subject);
        notificationHelper.sendNotification(emailParam);
        log.info(StringUtil.changeForLog("***************** send LDT Notification  end *****************"));
    }
}
