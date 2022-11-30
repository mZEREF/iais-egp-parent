package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocumentShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppLicBundleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOutsouredDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSpecialServiceInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmFormDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.ServiceStepDto;
import com.ecquaria.cloud.moh.iais.helper.AppDataHelper;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.RfcHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import com.ecquaria.cloud.moh.iais.validation.ValidateCharges;
import com.ecquaria.cloud.moh.iais.validation.ValidateVehicle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.APPSUBMISSIONDTO;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.CURRENTSERVICEID;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.CURRENTSVCCODE;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.CURR_STEP_CONFIG;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.CURR_STEP_PSN_OPTS;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.IS_EDIT;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.LICPERSONSELECTMAP;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.OUTSOURCED_SERVICE_OPTS;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.PERSONSELECTMAP;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.PRS_SERVICE_DOWN;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.SECTION_LEADER_LIST;


/**
 * @Auther chenlei on 5/5/2022.
 */
@Delegator("serviceInfoDelegator")
@Slf4j
public class ServiceInfoDelegator {

    @Autowired
    protected ConfigCommService configCommService;

    @Autowired
    protected AppCommService appCommService;

    @Autowired
    protected LicCommService licCommService;

    @Value("${moh.halp.prs.enable}")
    protected String prsFlag;

    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doStart start ...."));

        /*AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(bpc.request);
        DealSessionUtil.reSetInit(appSubmissionDto, HcsaAppConst.SECTION_SVCINFO);
        ApplicationHelper.setAppSubmissionDto(appSubmissionDto, bpc.request);*/
//        if (IaisCommonUtils.isEmpty(appSubmissionDto.getAppGrpPremisesDtoList())) {
//            List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,
//                    AppServicesConsts.HCSASERVICEDTOLIST);
//            ApplicationHelper.initAppPremSpecialisedDtoList(appSubmissionDto, hcsaServiceDtoList);
//            ApplicationHelper.setAppSubmissionDto(appSubmissionDto, bpc.request);
//        }

        //svc
//        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.RELOADSVCDOC, null);
        //ParamUtil.setSessionAttr(bpc.request, SERVICEPERSONNELCONFIG, null);

        List<SelectOption> designationOpList = ApplicationHelper.genDesignationOpList();
        ParamUtil.setSessionAttr(bpc.request, "designationOpList", (Serializable) designationOpList);
        log.debug(StringUtil.changeForLog("the do doStart end ...."));
    }

    /**
     * StartStep: prepareJumpPage
     *
     * @param bpc
     * @throws
     */
    public void prepareJumpPage(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prepareJumpPage start ...."));
        String action = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if (StringUtil.isEmpty(action)) {
            action = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM);
            if (StringUtil.isEmpty(action)) {
                action = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_PAGE);
            }
        }

        log.debug(StringUtil.changeForLog("The prepareJumpPage action is -->;" + action));
        String formTab = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.FORM_TAB);
        log.debug(StringUtil.changeForLog("The form_tab action is -->;" + formTab));
        //controller the step.
        if (IaisEGPConstant.YES.equals(formTab)) {
            action = null;
        }
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(bpc.request);
        ServiceStepDto serviceStepDto = (ServiceStepDto) ParamUtil.getSessionAttr(bpc.request,
                ShowServiceFormsDelegator.SERVICESTEPDTO);
        String svcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,
                AppServicesConsts.HCSASERVICEDTOLIST);
        getServiceStepDto(serviceStepDto, action, hcsaServiceDtoList, svcId, appSubmissionDto);
        //reset value
        /*if (HcsaConsts.STEP_DISCIPLINE_ALLOCATION.equals(action)) {
            action = serviceStepDto.getCurrentStep().getStepCode();
        }*/
        ParamUtil.setSessionAttr(bpc.request, ShowServiceFormsDelegator.SERVICESTEPDTO, serviceStepDto);

        if (StringUtil.isEmpty(action)) {
            if (serviceStepDto.getCurrentStep() != null) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,
                        serviceStepDto.getCurrentStep().getStepCode());
            } else {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,
                        HcsaConsts.STEP_LABORATORY_DISCIPLINES);
            }
        } else {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, action);
        }

        String actionType = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);

        String crudActionType = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
        if (StringUtil.isEmpty(crudActionType)) {
            crudActionType = actionType;
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, crudActionType);
        log.debug(StringUtil.changeForLog("The crud_action_type  is -->;" + crudActionType));
        if (!AppServicesConsts.NAVTABS_SERVICEFORMS.equals(crudActionType)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, "jump");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "jump");
        }

        log.debug(StringUtil.changeForLog("the do prepareJumpPage end ...."));
    }

    /**
     * Process: MohServiceRelatedInformation
     * Step: PrepareStepData
     *
     * @param bpc
     */
    public void prepareStepData(BaseProcessClass bpc) throws Exception {
        ServiceStepDto serviceStepDto = (ServiceStepDto) ParamUtil.getSessionAttr(bpc.request,
                ShowServiceFormsDelegator.SERVICESTEPDTO);
        HcsaServiceStepSchemeDto step = Optional.ofNullable(serviceStepDto)
                .map(ServiceStepDto::getCurrentStep)
                .orElseGet(HcsaServiceStepSchemeDto::new);
        String currentStepName = Optional.of(step)
                .map(HcsaServiceStepSchemeDto::getStepName)
                .orElse(HcsaConsts.BUSINESS_NAME);
        log.info(StringUtil.changeForLog("--- Prepare " + currentStepName + " Start ---"));
        String currentStep = Optional.of(step)
                .map(HcsaServiceStepSchemeDto::getStepCode)
                .orElse(HcsaConsts.STEP_BUSINESS_NAME);
        String singleName = "";
        switch (currentStep) {
            case HcsaConsts.STEP_BUSINESS_NAME:
                singleName = HcsaConsts.BUSINESS_NAME;
                prepareBusiness(bpc);
                break;
            case HcsaConsts.STEP_VEHICLES:
                singleName = HcsaConsts.VEHICLE;
                prePareVehicles(bpc);
                break;
            case HcsaConsts.STEP_CLINICAL_DIRECTOR:
                singleName = HcsaConsts.CLINICAL_DIRECTOR;
                prepareClinicalDirector(bpc);
                break;
            case HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS:
                singleName = HcsaConsts.CLINICAL_GOVERNANCE_OFFICER;
                prepareGovernanceOfficers(bpc);
                break;
            case HcsaConsts.STEP_SECTION_LEADER:
                // Section Leader
                singleName = HcsaConsts.SECTION_LEADER;
                prepareSectionLeader(bpc);
                break;
            case HcsaConsts.STEP_CHARGES:
                singleName = "General Conveyance Charges";
                prePareCharges(bpc);
                break;
            case HcsaConsts.STEP_SERVICE_PERSONNEL:
                singleName = HcsaConsts.SERVICE_PERSONNEL;
                prepareServicePersonnel(bpc);
                break;
            case HcsaConsts.STEP_PRINCIPAL_OFFICERS:
                singleName = HcsaConsts.PRINCIPAL_OFFICER;
                preparePrincipalOfficers(bpc);
                break;
            case HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER:
                singleName = HcsaConsts.KEY_APPOINTMENT_HOLDER;
                prepareKeyAppointmentHolder(bpc);
                break;
            case HcsaConsts.STEP_MEDALERT_PERSON:
                singleName = HcsaConsts.MEDALERT_PERSON;
                prePareMedAlertPerson(bpc);
                break;
            case HcsaConsts.STEP_OTHER_INFORMATION:
                prepareOtherInformation(bpc);
                break;
            case HcsaConsts.STEP_SUPPLEMENTARY_FORM:
                prepareSupplementaryForm(bpc.request);
                break;
            case HcsaConsts.STEP_SPECIAL_SERVICES_FORM:
                prepareSpecialServicesForm(bpc.request);
                break;
            case HcsaConsts.STEP_DOCUMENTS:
                prepareDocuments(bpc);
                break;
            case HcsaConsts.STEP_OUTSOURCED_PROVIDERS:
                prepareOutsourcedProviders(bpc.request);
                break;
            default:
                log.warn(StringUtil.changeForLog("Wrong Step!!!"));
                break;
        }

        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.CURR_SINGLE_NAME, singleName);
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.CURR_STEP_NAME, currentStepName);
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.CURR_SVC_STEP, currentStep);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request);
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.CURR_SVC_INFO, currSvcInfoDto);
        log.info(StringUtil.changeForLog("--- Prepare " + currentStepName + " End ---"));
    }

    /**
     * Process: MohServiceRelatedInformation
     * Step: DoStep
     *
     * @param bpc
     */
    public void doStep(BaseProcessClass bpc) throws Exception {
        ServiceStepDto serviceStepDto = (ServiceStepDto) ParamUtil.getSessionAttr(bpc.request,
                ShowServiceFormsDelegator.SERVICESTEPDTO);
        HcsaServiceStepSchemeDto step = Optional.ofNullable(serviceStepDto)
                .map(ServiceStepDto::getCurrentStep)
                .orElseGet(HcsaServiceStepSchemeDto::new);
        String currentStepName = Optional.of(step)
                .map(HcsaServiceStepSchemeDto::getStepName)
                .orElse(HcsaConsts.BUSINESS_NAME);
        log.info(StringUtil.changeForLog("--- Do " + currentStepName + " Start ---"));
        String currentStep = Optional.of(step)
                .map(HcsaServiceStepSchemeDto::getStepCode)
                .orElse(HcsaConsts.STEP_BUSINESS_NAME);
        String pageStep = ParamUtil.getString(bpc.request, "currentStep");
        if (currentStep.equals(pageStep)) {
            log.warn(StringUtil.changeForLog("Wrong page step - " + pageStep));
        }
        switch (currentStep) {
            case HcsaConsts.STEP_BUSINESS_NAME:
                doBusiness(bpc);
                break;
            case HcsaConsts.STEP_VEHICLES:
                doVehicles(bpc);
                break;
            case HcsaConsts.STEP_CLINICAL_DIRECTOR:
                doClinicalDirector(bpc);
                break;
            case HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS:
                doGovernanceOfficers(bpc);
                break;
            case HcsaConsts.STEP_SECTION_LEADER:
                // Section Leader
                doSectionLeader(bpc);
                break;
            case HcsaConsts.STEP_CHARGES:
                doCharges(bpc);
                break;
            case HcsaConsts.STEP_SERVICE_PERSONNEL:
                doServicePersonnel(bpc);
                break;
            case HcsaConsts.STEP_PRINCIPAL_OFFICERS:
                doPrincipalOfficers(bpc);
                break;
            case HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER:
                doKeyAppointmentHolder(bpc);
                break;
            case HcsaConsts.STEP_MEDALERT_PERSON:
                doMedAlertPerson(bpc);
                break;
            case HcsaConsts.STEP_OTHER_INFORMATION:
                doOtherInformation(bpc);
                break;
            case HcsaConsts.STEP_SUPPLEMENTARY_FORM:
                doSupplementaryForm(bpc.request);
                break;
            case HcsaConsts.STEP_SPECIAL_SERVICES_FORM:
                doSpecialServicesForm(bpc.request);
                break;
            case HcsaConsts.STEP_DOCUMENTS:
                doDocuments(bpc);
                break;
            case HcsaConsts.STEP_OUTSOURCED_PROVIDERS:
                doOutsourcedProviders(bpc.request);
                break;
            default:
                log.warn(StringUtil.changeForLog("--- Wrong Step!!!"));
                break;
        }
        log.info(StringUtil.changeForLog("--- Do " + currentStepName + " End ---"));
    }

    private void prepareSpecialServicesForm(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("prepare SpecialServicesInformation start ..."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        String currSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currSvcId);
        List<AppPremSpecialisedDto> appPremSpecialisedDtos = appSubmissionDto.getAppPremSpecialisedDtoList();
        List<AppPremSpecialisedDto> appPremSpecialisedDtoList = IaisCommonUtils.genNewArrayList();
        for (AppPremSpecialisedDto appPremSpecialisedDto : appPremSpecialisedDtos) {
            if (appPremSpecialisedDto.getBaseSvcId().equals(currSvcId)) {
                appPremSpecialisedDtoList.add(appPremSpecialisedDto);
            }
        }
        List<AppSvcSpecialServiceInfoDto> appSvcSpecialServiceInfoList = DealSessionUtil.initAppSvcSpecialServiceInfoDtoList(
                currSvcInfoDto, appPremSpecialisedDtos, false);
        currSvcInfoDto.setAppSvcSpecialServiceInfoList(appSvcSpecialServiceInfoList);
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        List<SelectOption> personList = ApplicationHelper.genAssignPersonSel(request, true);
        ParamUtil.setRequestAttr(request, CURR_STEP_PSN_OPTS, personList);
        ApplicationHelper.genSpecialServiceInforamtionPersonsel(request);
        ParamUtil.setRequestAttr(request, "isRfi", isRfi);
        ParamUtil.setRequestAttr(request, "appSvcSpecialServiceInfoList", appSvcSpecialServiceInfoList);
        log.debug(StringUtil.changeForLog("prepare SpecialServicesInformation end ..."));
    }

    private void doSpecialServicesForm(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("do SpecialServicesInformation start ..."));
        String currSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currSvcId);
        String currSvcCode = (String) ParamUtil.getSessionAttr(request, CURRENTSVCCODE);
        String isEdit = ParamUtil.getString(request, IS_EDIT);
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        List<AppSvcSpecialServiceInfoDto> appSvcSpecialServiceInfoList = currSvcInfoDto.getAppSvcSpecialServiceInfoList();
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(appSubmissionDto, RfcConst.EDIT_SERVICE, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        if (isGetDataFromPage) {
            //get data from page
            appSvcSpecialServiceInfoList = AppDataHelper.getAppSvcSpecialServiceInfoList(request, appSvcSpecialServiceInfoList,
                    appSubmissionDto.getAppType());
            currSvcInfoDto.setAppSvcSpecialServiceInfoList(appSvcSpecialServiceInfoList);
            setAppSvcRelatedInfoMap(request, currSvcId, currSvcInfoDto);
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crud_action_type = ParamUtil.getRequestString(request, "nextStep");
        if ("next".equals(crud_action_type)) {
            Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(
                    request, LICPERSONSELECTMAP);
            AppValidatorHelper.doValidateSpecialServicesForm(appSvcSpecialServiceInfoList, errorMap, licPersonMap);
        }
        boolean isValid = checkAction(errorMap, HcsaConsts.STEP_CLINICAL_DIRECTOR, appSubmissionDto, request);
        if (isValid && isGetDataFromPage) {
            List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorList = IaisCommonUtils.genNewArrayList();
            appSvcSpecialServiceInfoList.forEach((item) -> appSvcClinicalDirectorList.addAll(item.getAppSvcCgoDtoList()));
            syncDropDownAndPsn(appSubmissionDto, appSvcClinicalDirectorList, currSvcCode, request);
            DealSessionUtil.reSetInit(appSubmissionDto, HcsaAppConst.SECTION_SVCINFO);
        }
        checkAction(errorMap, HcsaConsts.STEP_SPECIAL_SERVICES_FORM, appSubmissionDto, request);
        log.debug(StringUtil.changeForLog("do SpecialServicesInformation end ..."));
    }

    private void prepareSupplementaryForm(HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(appSubmissionDto, currentSvcId, null);
        if (DealSessionUtil.initSupplementoryForm(currSvcInfoDto, appSubmissionDto.getAppGrpPremisesDtoList(), false)) {
            setAppSvcRelatedInfoMap(request, currentSvcId, currSvcInfoDto, appSubmissionDto);
        }
    }

    private void doSupplementaryForm(HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        String action = ParamUtil.getRequestString(request, "nextStep");
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)
                || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action) || RfcConst.RFC_BTN_OPTION_SKIP.equals(action)) {
                return;
            }
        }
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(appSubmissionDto, currentSvcId, null);
        List<AppSvcSuplmFormDto> appSvcSuplmFormList = currSvcInfoDto.getAppSvcSuplmFormList();
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(RfcConst.EDIT_SERVICE, request);
        log.info(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        if (isGetDataFromPage) {
            AppDataHelper.setAppSvcSuplmFormList(appSvcSuplmFormList, request);
            setAppSvcRelatedInfoMap(request, currentSvcId, currSvcInfoDto, appSubmissionDto);
        }
        // validateion
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if ("next".equals(action)) {
            errorMap = AppValidatorHelper.doValidateSupplementaryFormList(appSvcSuplmFormList);
        }
        checkAction(errorMap, HcsaConsts.STEP_SUPPLEMENTARY_FORM, appSubmissionDto, request);
    }

    /**
     * StartStep: prepareOtherInformation
     *
     * @param bpc
     * @throws
     */
    private void prepareOtherInformation(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("prePareOtherInformationDirector start ..."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String appType = appSubmissionDto.getAppType();
        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currSvcId, null);
        if (DealSessionUtil.initAppSvcOtherInfoList(currSvcInfoDto, appSubmissionDto.getAppGrpPremisesDtoList(), false, bpc.request,
                appType)) {
            setAppSvcRelatedInfoMap(bpc.request, currSvcId, currSvcInfoDto, appSubmissionDto);
        }
    }

    /**
     * StartStep: doOtherInformation
     *
     * @param bpc
     * @throws
     */
    private void doOtherInformation(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("doOtherInformation start ..."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String actionType = ParamUtil.getRequestString(bpc.request, "nextStep");
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)
                || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(actionType)
                    || RfcConst.RFC_BTN_OPTION_SKIP.equals(actionType)) {
                return;
            }
        }
        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        String currSvcCode = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSVCCODE);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currSvcId, null);
        List<AppSvcOtherInfoDto> appSvcOtherInfoDtos = currSvcInfoDto.getAppSvcOtherInfoList();
        String isEdit = ParamUtil.getString(bpc.request, IS_EDIT);
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(appSubmissionDto,
                RfcConst.EDIT_SERVICE, isEdit, isRfi);
        if (isGetDataFromPage) {
            //get data from page
            appSvcOtherInfoDtos = AppDataHelper.genAppSvcOtherInfoList(bpc.request, appSubmissionDto.getAppType(),
                    appSvcOtherInfoDtos);
            currSvcInfoDto.setAppSvcOtherInfoList(appSvcOtherInfoDtos);
            reSetChangesForApp(appSubmissionDto);
            setAppSvcRelatedInfoMap(bpc.request, currSvcId, currSvcInfoDto, appSubmissionDto);
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if ("next".equals(actionType)) {
            errorMap = AppValidatorHelper.doValidateOtherInformation(appSvcOtherInfoDtos, currSvcCode);
        }
        boolean isValid = checkAction(errorMap, HcsaConsts.STEP_OTHER_INFORMATION, appSubmissionDto, bpc.request);
        if (isValid && isGetDataFromPage) {
            AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);
            AppSvcRelatedInfoDto oldSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfoBySvcCode(oldAppSubmissionDto,
                    currSvcInfoDto.getServiceCode(), appSubmissionDto.getRfiAppNo());
            RfcHelper.resolveOtherServiceActionCode(currSvcInfoDto, oldSvcInfoDto, appType);
            setAppSvcRelatedInfoMap(bpc.request, currSvcId, currSvcInfoDto, appSubmissionDto);
        }
    }


    private void prepareOutsourcedProviders(HttpServletRequest request) {
        String currSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,
                AppServicesConsts.HCSASERVICEDTOLIST);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currSvcId, null);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        if (DealSessionUtil.initSvcOutsourcedProvider(appSubmissionDto, currSvcInfoDto, false, hcsaServiceDtoList)) {
            setAppSvcRelatedInfoMap(request, currSvcId, currSvcInfoDto);
        }
        AppSvcOutsouredDto appSvcOutsouredDto = currSvcInfoDto.getAppSvcOutsouredDto();
        SearchParam searchParam = appSvcOutsouredDto.getSearchParam();
        if (searchParam != null) {
            SearchResult searchResult = licCommService.queryOutsouceLicences(searchParam);
            ParamUtil.setSessionAttr(request, ApplicationConsts.OUT_SOURCE_PARAM, searchParam);
            ParamUtil.setSessionAttr(request, ApplicationConsts.OUT_SOURCE_RESULT, searchResult);
        } else {
            ParamUtil.setSessionAttr(request, ApplicationConsts.OUT_SOURCE_PARAM, null);
            ParamUtil.setSessionAttr(request, ApplicationConsts.OUT_SOURCE_RESULT, null);
        }
        //OutsourcedProviders services dropdown options
        List<SelectOption> optionList = ApplicationHelper.genOutsourcedServiceSel(appSvcOutsouredDto);
        ParamUtil.setRequestAttr(request, OUTSOURCED_SERVICE_OPTS, optionList);
    }

    private void doOutsourcedProviders(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("doOutsourcedProviders start ..."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        String actionType = ParamUtil.getRequestString(request, "nextStep");
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)
                || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(actionType)
                    || RfcConst.RFC_BTN_OPTION_SKIP.equals(actionType)) {
                return;
            }
        }
        String currSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currSvcId, null);
        String isEdit = ParamUtil.getString(request, IS_EDIT);
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(appSubmissionDto,
                RfcConst.EDIT_SERVICE, isEdit, isRfi);
        AppSvcOutsouredDto appSvcOutsouredDto = currSvcInfoDto.getAppSvcOutsouredDto();
        String curAct = ParamUtil.getString(request, "btnStep");
        currSvcInfoDto.setCurAt(curAct);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (isGetDataFromPage) {
            //get data from page
            appSvcOutsouredDto = AppDataHelper.genAppPremOutSourceProvidersDto(curAct, appSvcOutsouredDto, request, appSubmissionDto,
                    appType);
            currSvcInfoDto.setAppSvcOutsouredDto(appSvcOutsouredDto);
            reSetChangesForApp(appSubmissionDto);
            setAppSvcRelatedInfoMap(request, currSvcId, currSvcInfoDto, appSubmissionDto);
            if (StringUtil.isIn(curAct, new String[]{"search", "add"})) {
                errorMap = AppValidatorHelper.doValidationOutsourced(appSvcOutsouredDto, curAct);
            }
        }

        if ("next".equals(actionType)) {
            errorMap = AppValidatorHelper.doValidationOutsourced(appSvcOutsouredDto, actionType);
        }
        checkAction(errorMap, HcsaConsts.STEP_OUTSOURCED_PROVIDERS, appSubmissionDto, request);
    }

    private boolean checkAction(Map<String, String> errorMap, String step, AppSubmissionDto appSubmissionDto,
            HttpServletRequest request) {
        if (errorMap == null || errorMap.isEmpty()) {
            return true;
        }
        if (errorMap.containsKey(PRS_SERVICE_DOWN)) {
            ParamUtil.setRequestAttr(request, PRS_SERVICE_DOWN, PRS_SERVICE_DOWN);
        }
        ParamUtil.setRequestAttr(request, HcsaAppConst.ERROR_KEY, HcsaAppConst.ERROR_VAL);
        AppValidatorHelper.setAudiErrMap(ApplicationHelper.checkIsRfi(request), appSubmissionDto.getAppType(),
                errorMap, appSubmissionDto.getRfiAppNo(), appSubmissionDto.getLicenceNo());
        ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, step);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, AppServicesConsts.NAVTABS_SERVICEFORMS);
        return false;
    }

    public void prepareSectionLeader(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("PrepareSectionLeader start ...."));
        String currSvcId = ApplicationHelper.getCurrentServiceId(bpc.request);
        // Section Leader config
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList = configCommService.getHcsaSvcPersonnel(currSvcId,
                ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
        if (hcsaSvcPersonnelList != null && hcsaSvcPersonnelList.size() > 0) {
            ParamUtil.setRequestAttr(bpc.request, "sectionLeaderConfig", hcsaSvcPersonnelList.get(0));
        }
        ParamUtil.setRequestAttr(bpc.request, "prsFlag", prsFlag);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currSvcId);
        ParamUtil.setRequestAttr(bpc.request, SECTION_LEADER_LIST, currSvcInfoDto.getAppSvcSectionLeaderList());
        log.debug(StringUtil.changeForLog("PrepareSectionLeader end ...."));
    }

    public void doSectionLeader(BaseProcessClass bpc) {
        String svcCode = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSVCCODE);
        String currSvcId = ApplicationHelper.getCurrentServiceId(bpc.request);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(appSubmissionDto, currSvcId, null);
        String isEdit = ParamUtil.getString(bpc.request, IS_EDIT);
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(appSubmissionDto,
                RfcConst.EDIT_SERVICE, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        if (isGetDataFromPage) {
            List<AppSvcPersonnelDto> appSvcSectionLeaderList = getSectionLeadersFromPage(bpc.request);
            currSvcInfoDto.setAppSvcSectionLeaderList(appSvcSectionLeaderList);
            reSetChangesForApp(appSubmissionDto);
            setAppSvcRelatedInfoMap(bpc.request, currSvcId, currSvcInfoDto, appSubmissionDto);
            bpc.request.setAttribute(SECTION_LEADER_LIST, appSvcSectionLeaderList);
        }
        String action = ParamUtil.getRequestString(bpc.request, "nextStep");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if ("next".equals(action)) {
            AppValidatorHelper.doValidateSectionLeader(errorMap, currSvcInfoDto.getAppSvcSectionLeaderList(), svcCode);
            if (!isRfi) {
                List<HcsaSvcPersonnelDto> psnConfig = configCommService.getHcsaSvcPersonnel(currSvcId,
                        ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
                int psnLength = Optional.ofNullable(currSvcInfoDto.getAppSvcSectionLeaderList())
                        .map(List::size)
                        .orElse(0);
                errorMap = AppValidatorHelper.psnMandatoryValidate(psnConfig, ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER,
                        errorMap, psnLength, "errorSECLDR", HcsaConsts.SECTION_LEADER);
            }
        }
        boolean isValid = checkAction(errorMap, HcsaConsts.STEP_SECTION_LEADER, appSubmissionDto, bpc.request);
        if (isValid && isGetDataFromPage) {
            DealSessionUtil.reSetInit(appSubmissionDto, HcsaAppConst.SECTION_SVCINFO);
            ApplicationHelper.setAppSubmissionDto(appSubmissionDto, bpc.request);
            //removeDirtyPsnDoc(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER, bpc.request);
        }
        log.debug(StringUtil.changeForLog("doSectionLeader end ..."));
    }

    private List<AppSvcPersonnelDto> getSectionLeadersFromPage(HttpServletRequest request) {
        String currentSvcId = ApplicationHelper.getCurrentServiceId(request);
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(appSubmissionDto, currentSvcId, null);
        List<AppSvcPersonnelDto> oldSectionLeadList = appSvcRelatedInfoDto.getAppSvcSectionLeaderList();
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        List<AppSvcPersonnelDto> sectionLeaderList = IaisCommonUtils.genNewArrayList();
        int slLength = ParamUtil.getInt(request, "slLength");
        if (slLength <= 0) {
            return sectionLeaderList;
        }
        for (int i = 0; i < slLength; i++) {
            String isPartEdit = ParamUtil.getString(request, "isPartEdit" + i);
            String indexNo = ParamUtil.getString(request, "indexNo" + i);
            boolean fromPage = false;
            boolean fromOld = false;
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                fromPage = true;
            } else if (AppConsts.YES.equals(isPartEdit)) {
                fromPage = true;
            } else if (!StringUtil.isEmpty(indexNo) && oldSectionLeadList != null && !oldSectionLeadList.isEmpty()) {
                fromOld = true;
            }
            AppSvcPersonnelDto sectionLeader;
            if (fromPage) {
                sectionLeader = getSectionLeaderFromPage(String.valueOf(i), request);
            } else if (fromOld) {
                sectionLeader = oldSectionLeadList.stream()
                        .filter(dto -> indexNo.equals(dto.getIndexNo()))
                        .findAny()
                        .orElse(null);
            } else {
                sectionLeader = new AppSvcPersonnelDto();
                sectionLeader.setIndexNo(UUID.randomUUID().toString());
                sectionLeaderList.add(sectionLeader);
            }
            if (sectionLeader != null) {
                sectionLeader.setSeqNum(i);
                sectionLeaderList.add(sectionLeader);
            }
        }
        log.info(StringUtil.changeForLog("The Section Leader length: " + slLength + "; size: " + sectionLeaderList.size()));
        return sectionLeaderList;
    }

    private AppSvcPersonnelDto getSectionLeaderFromPage(String index, HttpServletRequest request) {
        String salutation = ParamUtil.getString(request, "salutation" + index);
        String name = ParamUtil.getString(request, "name" + index);
        String qualification = ParamUtil.getString(request, "qualification" + index);
        String wrkExpYear = ParamUtil.getString(request, "wrkExpYear" + index);
        String indexNo = ParamUtil.getString(request, "indexNo" + index);
        AppSvcPersonnelDto sectionLeader = new AppSvcPersonnelDto();
        sectionLeader.setSalutation(salutation);
        sectionLeader.setName(name);
        sectionLeader.setQualification(qualification);
        sectionLeader.setWrkExpYear(wrkExpYear);
        sectionLeader.setIndexNo(indexNo);
        sectionLeader.setPersonnelType(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
        if (StringUtil.isEmpty(sectionLeader.getIndexNo())) {
            sectionLeader.setIndexNo(UUID.randomUUID().toString());
        }
        return sectionLeader;
    }

    public void prepareGovernanceOfficers(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareGovernanceOfficers start ...."));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        if (!StringUtil.isEmpty(currentSvcId)) {
            //min and max count
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList = configCommService.getHcsaSvcPersonnel(currentSvcId,
                    ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
            if (hcsaSvcPersonnelList != null && hcsaSvcPersonnelList.size() > 0) {
                HcsaSvcPersonnelDto hcsaSvcPersonnelDto = hcsaSvcPersonnelList.get(0);
                ParamUtil.setSessionAttr(bpc.request, CURR_STEP_CONFIG, hcsaSvcPersonnelDto);
            }
        }
        List<SelectOption> personList = ApplicationHelper.genAssignPersonSel(bpc.request, true);
        ParamUtil.setRequestAttr(bpc.request, CURR_STEP_PSN_OPTS, personList);
        //ParamUtil.setRequestAttr(bpc.request, "prsFlag", prsFlag);
        log.info(StringUtil.changeForLog("the do prepareGovernanceOfficers end ...."));
    }

    /**
     * StartStep: preparePrincipalOfficers
     *
     * @param bpc
     * @throws
     */
    public void preparePrincipalOfficers(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do preparePrincipalOfficers start ...."));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        List<HcsaSvcPersonnelDto> principalOfficerConfig = configCommService.getHcsaSvcPersonnel(currentSvcId,
                ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
        List<HcsaSvcPersonnelDto> deputyPrincipalOfficerConfig = configCommService.getHcsaSvcPersonnel(currentSvcId,
                ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currentSvcId);
        if (principalOfficerConfig != null && !principalOfficerConfig.isEmpty()) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = principalOfficerConfig.get(0);
            ParamUtil.setRequestAttr(bpc.request, CURR_STEP_CONFIG, hcsaSvcPersonnelDto);
        }
        HcsaSvcPersonnelDto personnelDto = deputyPrincipalOfficerConfig.get(0);
        Integer minCount = personnelDto.getMandatoryCount();
        Integer maximumCount = personnelDto.getMaximumCount();
        List<AppSvcPrincipalOfficersDto> dpoList = currSvcInfoDto.getAppSvcNomineeDtoList();
//        boolean flag = minCount == 0 && maximumCount > 0;
        if (deputyPrincipalOfficerConfig != null && !deputyPrincipalOfficerConfig.isEmpty()) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = deputyPrincipalOfficerConfig.get(0);
            ParamUtil.setRequestAttr(bpc.request, "dpoHcsaSvcPersonnelDto", hcsaSvcPersonnelDto);
        }
        if (minCount > 0 && maximumCount > 0) {
            currSvcInfoDto.setDeputyPoFlag(AppConsts.YES);
            ParamUtil.setRequestAttr(bpc.request, "currStepName2", "Nominee");
        }else {
            ParamUtil.setRequestAttr(bpc.request, "currStepName2", "Nominee (Optional)");
        }
//        if (flag){
//            currSvcInfoDto.setDeputyPoFlag(IaisCommonUtils.isEmpty(dpoList) ? AppConsts.NO : AppConsts.YES);
//        }
        if (StringUtil.isEmpty(currSvcInfoDto.getDeputyPoFlag())) {
            currSvcInfoDto.setDeputyPoFlag(IaisCommonUtils.isEmpty(dpoList) ? AppConsts.NO : AppConsts.YES);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currSvcInfoDto);
        }
        if (minCount == maximumCount && minCount == 0){
            currSvcInfoDto.setAppSvcNomineeDtoList(null);
        }
        List<SelectOption> personList = ApplicationHelper.genAssignPersonSel(bpc.request, true);
        ParamUtil.setRequestAttr(bpc.request, CURR_STEP_PSN_OPTS, personList);

        List<SelectOption> deputyFlagSelect = IaisCommonUtils.genNewArrayList();
        deputyFlagSelect.add(new SelectOption("-1", HcsaAppConst.FIRESTOPTION));
        deputyFlagSelect.add(new SelectOption("0", "No"));
        deputyFlagSelect.add(new SelectOption("1", "Yes"));
        ParamUtil.setRequestAttr(bpc.request, "DeputyFlagSelect", deputyFlagSelect);
        ParamUtil.setRequestAttr(bpc.request, "singleName2", "Nominee");

        log.debug(StringUtil.changeForLog("the do preparePrincipalOfficers end ...."));
    }

    public void prepareKeyAppointmentHolder(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("prepareKeyAppointmentHolder start ..."));
        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList = configCommService.getHcsaSvcPersonnel(currSvcId,
                ApplicationConsts.PERSONNEL_PSN_KAH);
        if (hcsaSvcPersonnelList != null && hcsaSvcPersonnelList.size() > 0) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = hcsaSvcPersonnelList.get(0);
            ParamUtil.setSessionAttr(bpc.request, CURR_STEP_CONFIG, hcsaSvcPersonnelDto);
        }
        List<SelectOption> personList = ApplicationHelper.genAssignPersonSel(bpc.request, true);
        ParamUtil.setRequestAttr(bpc.request, CURR_STEP_PSN_OPTS, personList);
        log.debug(StringUtil.changeForLog("prepareKeyAppointmentHolder end ..."));
    }

    /**
     * StartStep: prepareDocuments
     *
     * @param bpc
     * @throws
     */
    public void prepareDocuments(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareDocuments start ...."));
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(bpc.request);
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(appSubmissionDto, currentSvcId);
        if (IaisCommonUtils.isEmpty(currSvcInfoDto.getDocumentShowDtoList())) {
            DealSessionUtil.initDocumentShowList(currSvcInfoDto, appSubmissionDto.getAppPremSpecialisedDtoList(), true);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currSvcInfoDto, appSubmissionDto);
        }
        log.info(StringUtil.changeForLog("the do prepareDocuments end ...."));
    }

    /**
     * StartStep: prepareJump
     *
     * @param bpc
     * @throws
     */
    public void prepareJump(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareJump start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        Map<String, String> coMap = appSubmissionDto.getCoMap();
        Map<String, String> allChecked = isAllChecked(bpc, appSubmissionDto);
        if (allChecked.isEmpty()) {
            coMap.put(HcsaAppConst.SECTION_SVCINFO, HcsaAppConst.SECTION_SVCINFO);
        } else {
            coMap.put(HcsaAppConst.SECTION_SVCINFO, "");
        }
        setAppSubmissionDto(appSubmissionDto, bpc.request);
//        bpc.request.getSession().setAttribute(CO_MAP, coMap);
        log.info(StringUtil.changeForLog("the do prepareJump end ...."));
    }

    /**
     * StartStep: prepareView
     *
     * @param bpc
     * @throws
     */
    public void prepareView(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("the do prepareView start ...."));
        String iframeId = ParamUtil.getString(bpc.request, "iframeId");
        String maskName = ParamUtil.getString(bpc.request, "maskName");
        String svcId = ParamUtil.getMaskedString(bpc.request, maskName);
        String appNo = ParamUtil.getString(bpc.request, "appNo");
        if (!StringUtil.isEmpty(svcId)) {
            log.info(StringUtil.changeForLog("get current svc info...."));
            AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
            DealSessionUtil.initView(appSubmissionDto);
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(appSubmissionDto, svcId, appNo);
            ParamUtil.setSessionAttr(bpc.request, "currentPreviewSvcInfo", appSvcRelatedInfoDto);
            ParamUtil.setSessionAttr(bpc.request, "iframeId", iframeId);
        }

        log.info(StringUtil.changeForLog("the do prepareView end ...."));
    }

    private Map<String, String> isAllChecked(BaseProcessClass bpc, AppSubmissionDto appSubmissionDto) {
        List<AppSvcRelatedInfoDto> dto = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        Map<String, String> errorMap = new HashMap<>();
        Map<String, AppSvcPersonAndExtDto> licPersonMap = null;
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request, LICPERSONSELECTMAP);
        }
        Map<String, String> coMap = appSubmissionDto.getCoMap();
        StringJoiner joiner = new StringJoiner(AppConsts.DFT_DELIMITER);
        for (AppSvcRelatedInfoDto currSvcInfoDto : dto) {
            Map<String, String> map = AppValidatorHelper.doCheckBox(currSvcInfoDto, appSubmissionDto, licPersonMap);
            if (!map.isEmpty()) {
                errorMap.putAll(map);
                joiner.add(currSvcInfoDto.getServiceCode());
            }
        }
        String sign = joiner.toString();
        coMap.put(HcsaAppConst.SECTION_MULTI_SVC, sign);
        if (StringUtil.isEmpty(sign)) {
            coMap.put(HcsaAppConst.SECTION_SVCINFO, HcsaAppConst.SECTION_SVCINFO);
        } else {
            coMap.put(HcsaAppConst.SECTION_SVCINFO, "");
        }
        appSubmissionDto.setCoMap(coMap);
        return errorMap;
    }

    /**
     * StartStep: doGovernanceOfficers
     *
     * @param bpc
     * @throws
     */
    public void doGovernanceOfficers(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doGovernanceOfficers start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String action = ParamUtil.getRequestString(bpc.request, "nextStep");
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)
                || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action) || RfcConst.RFC_BTN_OPTION_SKIP.equals(action)) {
                return;
            }
        }
        Map<String, String> errList = IaisCommonUtils.genNewHashMap();
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currentSvcRelatedDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currentSvcId);
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = currentSvcRelatedDto.getAppSvcCgoDtoList();
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(RfcConst.EDIT_SERVICE, bpc.request);
        log.info(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        if (isGetDataFromPage) {
            appSvcCgoDtoList = AppDataHelper.genAppSvcGovernanceOfficersDto(bpc.request);
            currentSvcRelatedDto.setAppSvcCgoDtoList(appSvcCgoDtoList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto, appSubmissionDto);
        }
        if ("next".equals(action)) {
            Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(
                    bpc.request, LICPERSONSELECTMAP);
            Map<String, String> map = AppValidatorHelper.doValidateGovernanceOfficers(appSvcCgoDtoList, licPersonMap, true);
            //validate mandatory count
            int psnLength = 0;
            if (!IaisCommonUtils.isEmpty(appSvcCgoDtoList)) {
                psnLength = appSvcCgoDtoList.size();
            }
            List<HcsaSvcPersonnelDto> psnConfig = configCommService.getHcsaSvcPersonnel(currentSvcId,
                    ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
            AppValidatorHelper.psnMandatoryValidate(psnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, map, psnLength,
                    "psnMandatory", HcsaConsts.CLINICAL_GOVERNANCE_OFFICER);
            errList.putAll(map);
            reSetChangesForApp(appSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        }
        boolean isValid = checkAction(errList, HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS, appSubmissionDto, bpc.request);
        if (isValid && isGetDataFromPage) {
            //sync person dropdown and submisson dto
            String svcCode = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSVCCODE);
            syncDropDownAndPsn(appSubmissionDto, appSvcCgoDtoList, svcCode, bpc.request);
        }
        log.debug(StringUtil.changeForLog("the do doGovernanceOfficers end ...."));
    }

    public void doPrincipalOfficers(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doPrincipalOfficers start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currentSvcId);
        List<HcsaSvcPersonnelDto> deputyPrincipalOfficerConfig = configCommService.getHcsaSvcPersonnel(currentSvcId,
                ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
        HcsaSvcPersonnelDto personnelDto = deputyPrincipalOfficerConfig.get(0);
        Integer minCount = personnelDto.getMandatoryCount();
        Integer maximumCount = personnelDto.getMaximumCount();
        if (minCount == maximumCount && minCount == 0){
            currSvcInfoDto.setAppSvcNomineeDtoList(null);
        }
        String action = ParamUtil.getRequestString(bpc.request, "nextStep");
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(
                appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)
                    || RfcConst.RFC_BTN_OPTION_SKIP.equals(action)) {
                return;
            }
        }
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        String isEdit = ParamUtil.getString(bpc.request, IS_EDIT);
        String isEditDpo = ParamUtil.getString(bpc.request, "isEditDpo");
        String isEditDpoSelect = ParamUtil.getString(bpc.request, "isEditDpoSelect");
        boolean isGetDataFromPagePo = ApplicationHelper.isGetDataFromPage(appSubmissionDto,
                RfcConst.EDIT_SERVICE, isEdit, isRfi);
        boolean isGetDataFromPageDpoSelect = ApplicationHelper.isGetDataFromPage(appSubmissionDto,
                RfcConst.EDIT_SERVICE, isEditDpoSelect, isRfi);
        boolean isGetDataFromPageDpo = ApplicationHelper.isGetDataFromPage(appSubmissionDto,
                RfcConst.EDIT_SERVICE, isEditDpo, isRfi);
        List<AppSvcPrincipalOfficersDto> poList = currSvcInfoDto.getAppSvcPrincipalOfficersDtoList();
        List<AppSvcPrincipalOfficersDto> dpoList = currSvcInfoDto.getAppSvcNomineeDtoList();
        if (isGetDataFromPagePo) {
            poList = AppDataHelper.genAppSvcPrincipalOfficersDtos(bpc.request);
            currSvcInfoDto.setAppSvcPrincipalOfficersDtoList(poList);
        }
        String deputySelect = ParamUtil.getString(bpc.request, "deputyPrincipalOfficer");
        if (isGetDataFromPageDpoSelect) {
            deputySelect = ParamUtil.getString(bpc.request, "deputyPrincipalOfficer");
            currSvcInfoDto.setDeputyPoFlag(deputySelect);
        }
        if (isGetDataFromPageDpo) {       //   0 - 4
            if (AppConsts.NO.equals(deputySelect)) {
                dpoList = IaisCommonUtils.genNewArrayList();
            } else {
                dpoList = AppDataHelper.genAppSvcNomineeDtos(bpc.request);
            }
            currSvcInfoDto.setAppSvcNomineeDtoList(dpoList);
        }
        setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currSvcInfoDto, appSubmissionDto);
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        if ("next".equals(action)) {
            Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(
                    bpc.request, LICPERSONSELECTMAP);
            map.putAll(AppValidatorHelper.doValidatePoAndDpo(poList, dpoList, deputySelect, licPersonMap,
                    appSubmissionDto.getSubLicenseeDto(), true));
            //validate mandatory count
            List<HcsaSvcPersonnelDto> poPsnConfig = configCommService.getHcsaSvcPersonnel(currentSvcId,
                    ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
            List<HcsaSvcPersonnelDto> dpoPsnConfig = configCommService.getHcsaSvcPersonnel(currentSvcId,
                    ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
            int poSize = 0;
            if (poList != null) {
                poSize = poList.size();
            }
            AppValidatorHelper.psnMandatoryValidate(poPsnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_PO, map, poSize,
                    "poPsnMandatory", HcsaConsts.PRINCIPAL_OFFICER);
            int dpoSize = 0;
            if (dpoList != null) {
                dpoSize = dpoList.size();
            }
            AppValidatorHelper.psnMandatoryValidate(dpoPsnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, map,
                    dpoSize, "dpoPsnMandatory", HcsaConsts.NOMINEE);
            if (map.containsKey("dpoPsnMandatory") && !AppConsts.YES.equals(deputySelect)) {
                map.remove("dpoPsnMandatory");
                if (AppConsts.NO.equals(deputySelect)) {
                    map.put("deputyPrincipalOfficer", "GENERAL_ERR0051");
                } else {
                    map.put("deputyPrincipalOfficer", "GENERAL_ERR0006");
                }
            }
            reSetChangesForApp(appSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        }
        boolean isValid = checkAction(map, HcsaConsts.STEP_PRINCIPAL_OFFICERS, appSubmissionDto, bpc.request);
        if (isValid && (isGetDataFromPagePo || isGetDataFromPageDpo)) {
            String svcCode = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSVCCODE);
            syncDropDownAndPsn(appSubmissionDto, IaisCommonUtils.combineList(poList, dpoList), svcCode, bpc.request);
        }
        log.debug(StringUtil.changeForLog("the do doPrincipalOfficers end ...."));
    }

    public void doKeyAppointmentHolder(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doKeyAppointmentHolder start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String action = ParamUtil.getRequestString(bpc.request, "nextStep");
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)
                || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)
                    || RfcConst.RFC_BTN_OPTION_SKIP.equals(action)) {
                return;
            }
        }
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currentSvcRelatedDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currentSvcId);
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(RfcConst.EDIT_SERVICE, bpc.request);
        log.info(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        if (isGetDataFromPage) {
            List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderList = AppDataHelper.genAppSvcKeyAppointmentHolder(bpc.request);
            currentSvcRelatedDto.setAppSvcKeyAppointmentHolderDtoList(appSvcKeyAppointmentHolderList);
            reSetChangesForApp(appSubmissionDto);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto, appSubmissionDto);
        }

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if ("next".equals(action)) {
            Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(
                    bpc.request, LICPERSONSELECTMAP);
            List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderList =
                    currentSvcRelatedDto.getAppSvcKeyAppointmentHolderDtoList();
            errorMap = AppValidatorHelper.doValidateKeyAppointmentHolder(appSvcKeyAppointmentHolderList,
                    licPersonMap, true);
            //validate mandatory count
            int psnLength = 0;
            if (!IaisCommonUtils.isEmpty(appSvcKeyAppointmentHolderList)) {
                psnLength = appSvcKeyAppointmentHolderList.size();
            }
            List<HcsaSvcPersonnelDto> psnConfig = configCommService.getHcsaSvcPersonnel(currentSvcId,
                    ApplicationConsts.PERSONNEL_PSN_KAH);
            AppValidatorHelper.psnMandatoryValidate(psnConfig, ApplicationConsts.PERSONNEL_PSN_KAH, errorMap,
                    psnLength, "psnMandatory", HcsaConsts.KEY_APPOINTMENT_HOLDER);
        }
        boolean isValid = checkAction(errorMap, HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER, appSubmissionDto, bpc.request);
        if (isValid && isGetDataFromPage) {
            //sync person dropdown and submisson dto
            syncDropDownAndPsn(appSubmissionDto, currentSvcRelatedDto.getAppSvcKeyAppointmentHolderDtoList(), null, bpc.request);
        }
        log.debug(StringUtil.changeForLog("the do doKeyAppointmentHolder end ...."));
    }

    /**
     * StartStep: doDocuments
     *
     * @param bpc
     */
    public void doDocuments(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doDocuments start ...."));
        HttpServletRequest request = bpc.request;
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        String action = ParamUtil.getRequestString(request, "nextStep");
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                appSubmissionDto.getAppType()) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)
                    || RfcConst.RFC_BTN_OPTION_SKIP.equals(action)) {
                //clear
                ParamUtil.setSessionAttr(request, HcsaAppConst.RELOADSVCDOC, null);
                return;
            }
        }
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        String isEdit = ParamUtil.getString(request, IS_EDIT);
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(appSubmissionDto,
                RfcConst.EDIT_SERVICE, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(appSubmissionDto, currentSvcId, null);
        List<DocumentShowDto> documentShowDtoList = currSvcInfoDto.getDocumentShowDtoList();
        Map<String, File> saveFileMap = IaisCommonUtils.genNewHashMap();
        if (isGetDataFromPage) {
            if (appSubmissionDto.isNeedEditController()) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                //clickEditPages.add(APPLICATION_SVC_PAGE_NAME_DOCUMENT);
                appSubmissionDto.setClickEditPage(clickEditPages);
                reSetChangesForApp(appSubmissionDto);
            }
            //int maxPsnTypeNum = getMaxPersonTypeNumber(appSvcDocDtos, oldDocs);
            AppDataHelper.setAppSvcDocuments(documentShowDtoList, currSvcInfoDto.getServiceId(), saveFileMap, request);
        }
        AppValidatorHelper.doValidateSvcDocuments(documentShowDtoList, errorMap);
        if (isGetDataFromPage) {
            List<AppSvcDocDto> appSvcDocDtos = documentShowDtoList.stream()
                    .map(DocumentShowDto::allDocuments)
                    .collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
            saveSvcFileAndSetFileId(appSvcDocDtos, saveFileMap);
            currSvcInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
            currSvcInfoDto.setDocumentShowDtoList(documentShowDtoList);
        }
        // check validation
        //String crud_action_values = request.getParameter("nextStep");
        if (!"next".equals(action) && isGetDataFromPage) {
            errorMap.clear();//no need to block page
        }
        setAppSvcRelatedInfoMap(request, currentSvcId, currSvcInfoDto, appSubmissionDto);
        checkAction(errorMap, HcsaConsts.STEP_DOCUMENTS, appSubmissionDto, request);
        log.info(StringUtil.changeForLog("the do doDocuments end ...."));
    }

    private void saveSvcFileAndSetFileId(List<AppSvcDocDto> appSvcDocDtos, Map<String, File> saveFileMap) {
        if (IaisCommonUtils.isEmpty(saveFileMap) || appSvcDocDtos == null) {
            return;
        }
        Map<String, File> passValidateFileMap = IaisCommonUtils.genNewLinkedHashMap();
        for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
            if (appSvcDocDto.isPassValidate()) {
                String fileMapKey = ApplicationHelper.getFileMapKey(appSvcDocDto.getPremisesVal(), appSvcDocDto.getSvcId(),
                        appSvcDocDto.getSvcDocId(), appSvcDocDto.getPsnIndexNo(), appSvcDocDto.getSeqNum());
                File file = saveFileMap.get(fileMapKey);
                if (file != null) {
                    passValidateFileMap.put(fileMapKey, file);
                }
            }
        }
        if (passValidateFileMap.size() > 0) {
            List<File> fileList = new ArrayList<>(passValidateFileMap.values());
            List<String> fileRepoIdList = configCommService.saveFileRepo(fileList);
            int i = 0;
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                String fileMapKey = ApplicationHelper.getFileMapKey(appSvcDocDto.getPremisesVal(), appSvcDocDto.getSvcId(),
                        appSvcDocDto.getSvcDocId(), appSvcDocDto.getPsnIndexNo(), appSvcDocDto.getSeqNum());
                File file = passValidateFileMap.get(fileMapKey);
                if (file != null) {
                    appSvcDocDto.setFileRepoId(fileRepoIdList.get(i));
                    i++;
                }
            }
        }
    }

    /*private int getMaxPersonTypeNumber(List<AppSvcDocDto> newAppSvcDocDtos, List<AppSvcDocDto> oldAppSvcDocDtos) {
        int maxPersonTypeNumber = 0;
        maxPersonTypeNumber = getMaxNumber(maxPersonTypeNumber, newAppSvcDocDtos);
        maxPersonTypeNumber = getMaxNumber(maxPersonTypeNumber, oldAppSvcDocDtos);
        return maxPersonTypeNumber;
    }

    private int getMaxNumber(int maxPersonTypeNumber, List<AppSvcDocDto> appSvcDocDtos) {
        if (!IaisCommonUtils.isEmpty(appSvcDocDtos)) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                Integer personTypeNumber = appSvcDocDto.getPersonTypeNum();
                if (personTypeNumber != null && personTypeNumber > maxPersonTypeNumber) {
                    maxPersonTypeNumber = personTypeNumber;
                }
            }
        }
        return maxPersonTypeNumber;
    }*/

    /**
     * StartStep: doSaveDraft
     *
     * @param bpc
     * @throws
     */
    public void doSaveDraft(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doSaveDraft start ...."));
        log.info("The ClinicalLaboratoryDelegator doSaveDraft ... ");
        log.debug(StringUtil.changeForLog("the do doSaveDraft end ...."));
    }

    /**
     * StartStep: doSubmit
     *
     * @param bpc
     * @throws
     */
    public void doSubmit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doSubmit start ...."));
        log.info("The ClinicalLaboratoryDelegator doSubmit ... ");
        log.debug(StringUtil.changeForLog("the do doSubmit end ...."));
    }

    /**
     * StartStep: prepareResult
     *
     * @param bpc
     * @throws
     */
    public void prepareResult(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prepareResult start ...."));
        String crudActionValue = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        if (StringUtil.isEmpty(crudActionValue)) {
            crudActionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        }
        Object errorMsg = bpc.request.getAttribute("errorMsg");
        if (errorMsg != null) {
            crudActionValue = null;
        }
        if ("saveDraft".equals(crudActionValue)) {
            ParamUtil.setRequestAttr(bpc.request, "Switch2", "saveDraft");
        } else {
            ParamUtil.setRequestAttr(bpc.request, "Switch2", "jumPage");
        }
//        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
//        String appType = appSubmissionDto.getAppType();
//        if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
//            ServiceStepDto serviceStepDto = (ServiceStepDto) ParamUtil.getSessionAttr(bpc.request, ShowServiceFormsDelegator.SERVICESTEPDTO);
//            if(serviceStepDto.isServiceEnd()){
//                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "jump");
//            }
//
//        }
        log.debug(StringUtil.changeForLog("the do prepareResult end ...."));
    }

    /**
     * StartStep: prepareResult
     *
     * @param bpc
     * @throws
     */
    public void prepareServicePersonnel(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prepareServicePersonnel start ...."));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        String currentSvcCode = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSVCCODE);
        Map<String, Integer> minPersonnle = IaisCommonUtils.genNewHashMap();
        Map<String, Integer> maxPersonnle = IaisCommonUtils.genNewHashMap();
        String[] personType = new String[]{ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST, ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER,
                ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES/*, ApplicationConsts.SERVICE_PERSONNEL_TYPE_SPECIALS*/, ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS};
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currentSvcId);
        List<HcsaSvcPersonnelDto> typeConfigs = configCommService.getHcsaSvcPersonnel(currentSvcId, personType);
        if (IaisCommonUtils.isNotEmpty(typeConfigs)) {
            for (HcsaSvcPersonnelDto typeConfig : typeConfigs) {
                minPersonnle.put(typeConfig.getPsnType(), typeConfig.getMandatoryCount());
                maxPersonnle.put(typeConfig.getPsnType(), typeConfig.getMaximumCount());
            }
        }
        SvcPersonnelDto svcPersonnelDto = appSvcRelatedInfoDto.getSvcPersonnelDto();
        if (StringUtil.isEmpty(svcPersonnelDto)) {
            svcPersonnelDto = new SvcPersonnelDto();
        }
        svcPersonnelDto.setMinPersonnle(minPersonnle);
        svcPersonnelDto.setMaxPersonnel(maxPersonnle);
        int emCount = 0;
        int nuCount = 0;
        int arCount = 0;
//        int speCount = 0;
        int norCount = 0;
        int number = 0;
        if (currentSvcCode != null) {
            number = StringUtil.isEmpty(minPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST)) ? -1 : minPersonnle.get(
                    ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST);  // 6      0
            emCount = Optional.ofNullable(svcPersonnelDto.getEmbryologistList()).map(List::size).orElse(number);    //    0
            emCount = handleLength(number,emCount,maxPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST));
            number = StringUtil.isEmpty(
                    minPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES)) ? -1 : minPersonnle.get(
                    ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES);
            nuCount = Optional.ofNullable(svcPersonnelDto.getNurseList())
                    .map(List::size)
                    .orElse(number);
            nuCount = handleLength(number,nuCount,maxPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES));
            number = StringUtil.isEmpty(minPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER)) ? -1 : minPersonnle.get(
                    ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER);
            arCount = Optional.ofNullable(svcPersonnelDto.getArPractitionerList())
                    .map(List::size)
                    .orElse(number);
            arCount = handleLength(number,arCount,maxPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER));
            svcPersonnelDto.setEmbryologistMinCount(emCount);
            svcPersonnelDto.setNurseCount(nuCount);
            svcPersonnelDto.setArPractitionerCount(arCount);

            number = StringUtil.isEmpty(minPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS)) ? -1 : minPersonnle.get(
                    ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS);
            norCount = Optional.ofNullable(svcPersonnelDto.getNormalList())
                    .map(List::size)
                    .orElse(number);
            norCount = handleLength(number,norCount,maxPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS));
            svcPersonnelDto.setNormalCount(norCount);

/*            if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(currentSvcCode)) {
                number = StringUtil.isEmpty(
                        minPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_SPECIALS)) ? -1 : minPersonnle.get(
                        ApplicationConsts.SERVICE_PERSONNEL_TYPE_SPECIALS);
                speCount = Optional.ofNullable(svcPersonnelDto.getSpecialList())
                        .map(List::size)
                        .orElse(number);
                speCount = handleLength(number,speCount,maxPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_SPECIALS));
                svcPersonnelDto.setSpecialCount(speCount);
            }*/
        }
        appSvcRelatedInfoDto.setSvcPersonnelDto(svcPersonnelDto);
        ParamUtil.setRequestAttr(bpc.request, "svcPersonnelDto", svcPersonnelDto);
        ParamUtil.setRequestAttr(bpc.request, "emPersonnelMax",
                maxPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST));
        ParamUtil.setRequestAttr(bpc.request, "arPersonnelMax",
                maxPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER));
        ParamUtil.setRequestAttr(bpc.request, "nuPersonnelMax", maxPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES));
//        ParamUtil.setRequestAttr(bpc.request, "spePersonnelMax", maxPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_SPECIALS));
        ParamUtil.setRequestAttr(bpc.request, "othersPersonnelMax", maxPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS));
        List<SelectOption> personnelTypeSel = ApplicationHelper.genPersonnelTypeSel(currentSvcCode);
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.SERVICEPERSONNELTYPE, personnelTypeSel);
        List<SelectOption> designation = genPersonnelDesignSel(currentSvcCode);
        ParamUtil.setSessionAttr(bpc.request, "NuclearMedicineImagingDesignation", (Serializable) designation);
        ParamUtil.setRequestAttr(bpc.request, "prsFlag", prsFlag);
        log.debug(StringUtil.changeForLog("the do prepareServicePersonnel end ...."));
    }

    /**
     * StartStep: prepareResult
     *
     * @param bpc
     * @throws
     */
    public void doServicePersonnel(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doServicePersonnel start ...."));
//        RFC
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String action = ParamUtil.getRequestString(bpc.request, "nextStep");
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(
                appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)
                    || RfcConst.RFC_BTN_OPTION_SKIP.equals(action)) {
                return;
            }
        }
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        String currentSvcCod = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSVCCODE);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(appSubmissionDto, currentSvcId, null);
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(RfcConst.EDIT_SERVICE, bpc.request);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (isGetDataFromPage) {
            List<String> personnelTypeList = IaisCommonUtils.genNewArrayList();
            List<SelectOption> personnelTypeSel = ApplicationHelper.genPersonnelTypeSel(currentSvcCod);
            for (SelectOption sp : personnelTypeSel) {
                personnelTypeList.add(sp.getValue());
            }
//            get pageData
            AppDataHelper.genAppSvcPersonnelDtoList(bpc.request, appSvcRelatedInfoDto, appType);
            log.debug(StringUtil.changeForLog("cycle cgo dto to retrieve prs info start ..."));
            log.debug("prs server flag {}", prsFlag);
            log.debug(StringUtil.changeForLog("cycle cgo dto to retrieve prs info end ..."));
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, appSvcRelatedInfoDto);
        }
        if ("next".equals(action)) {
            if (appSubmissionDto.isNeedEditController()) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                appSubmissionDto.setClickEditPage(clickEditPages);
            }
            AppValidatorHelper.doValidateSvcPersonnel(errorMap, appSvcRelatedInfoDto.getSvcPersonnelDto());
        }
        boolean isValid = checkAction(errorMap, HcsaConsts.STEP_SERVICE_PERSONNEL, appSubmissionDto, bpc.request);
        if (isValid && isGetDataFromPage) {
            DealSessionUtil.reSetInit(appSubmissionDto, HcsaAppConst.SECTION_SVCINFO);
        }
        ApplicationHelper.setAppSubmissionDto(appSubmissionDto, bpc.request);
        log.info(StringUtil.changeForLog("the do doServicePersonnel end ...."));
    }

    /**
     * StartStep: prePareMedAlertPerson
     *
     * @param bpc
     * @throws
     */
    public void prePareMedAlertPerson(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prePareMedAlertPerson start ...."));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList = configCommService.getHcsaSvcPersonnel(currentSvcId,
                ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
        if (hcsaSvcPersonnelList != null && hcsaSvcPersonnelList.size() > 0) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = hcsaSvcPersonnelList.get(0);
            ParamUtil.setSessionAttr(bpc.request, CURR_STEP_CONFIG, hcsaSvcPersonnelDto);
        }
        List<SelectOption> assignSelectList = ApplicationHelper.genAssignPersonSel(bpc.request, true);
        ParamUtil.setRequestAttr(bpc.request, CURR_STEP_PSN_OPTS, assignSelectList);
        ParamUtil.setRequestAttr(bpc.request, "MedAlertAssignSelect", assignSelectList);
        log.debug(StringUtil.changeForLog("the do prePareMedAlertPerson end ...."));
    }

    /**
     * StartStep: doMedAlertPerson
     *
     * @param bpc
     * @throws
     */
    public void doMedAlertPerson(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doMedAlertPerson start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String action = ParamUtil.getRequestString(bpc.request, "nextStep");
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)
                || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)
                    || RfcConst.RFC_BTN_OPTION_SKIP.equals(action)) {
                return;
            }
        }
        String currentSvcId = ApplicationHelper.getCurrentServiceId(bpc.request);
        AppSvcRelatedInfoDto currentSvcRelatedDto = ApplicationHelper.getAppSvcRelatedInfo(appSubmissionDto, currentSvcId, null);
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(RfcConst.EDIT_SERVICE, bpc.request);
        log.info(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        if (isGetDataFromPage) {
            List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = AppDataHelper.genKeyPersonnels(
                    ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, "", bpc.request, false);
            currentSvcRelatedDto.setAppSvcMedAlertPersonList(appSvcMedAlertPersonList);
            reSetChangesForApp(appSubmissionDto);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto, appSubmissionDto);
        }
        String svcCode = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSVCCODE);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if ("next".equals(action)) {
            Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(
                    bpc.request, LICPERSONSELECTMAP);
            List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = currentSvcRelatedDto.getAppSvcMedAlertPersonList();
            errorMap = AppValidatorHelper.doValidateMedAlertPsn(appSvcMedAlertPersonList, licPersonMap, svcCode);
            //validate mandatory count
            int psnLength = 0;
            if (!IaisCommonUtils.isEmpty(appSvcMedAlertPersonList)) {
                psnLength = appSvcMedAlertPersonList.size();
            }
            List<HcsaSvcPersonnelDto> psnConfig = configCommService.getHcsaSvcPersonnel(currentSvcId,
                    ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
            AppValidatorHelper.psnMandatoryValidate(psnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, errorMap,
                    psnLength, "psnMandatory", HcsaConsts.MEDALERT_PERSON);
        }
        boolean isValid = checkAction(errorMap, HcsaConsts.STEP_MEDALERT_PERSON, appSubmissionDto, bpc.request);
        if (isValid && isGetDataFromPage) {
            syncDropDownAndPsn(appSubmissionDto, currentSvcRelatedDto.getAppSvcMedAlertPersonList(), svcCode, bpc.request);
        }
        log.debug(StringUtil.changeForLog("the do doMedAlertPerson end ...."));
    }

    /**
     * StartStep: prePareVehicles
     *
     * @param bpc
     * @throws
     */
    public void prePareVehicles(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("prePareVehicles start ..."));
        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currSvcId);
        //vehicle config
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList = configCommService.getHcsaSvcPersonnel(currSvcId,
                ApplicationConsts.PERSONNEL_VEHICLES);
        if (hcsaSvcPersonnelList != null && hcsaSvcPersonnelList.size() > 0) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = hcsaSvcPersonnelList.get(0);
            ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.VEHICLECONFIGDTO, hcsaSvcPersonnelDto);
        }

        List<AppSvcVehicleDto> appSvcVehicleDtos = currSvcInfoDto.getAppSvcVehicleDtoList();
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.VEHICLEDTOLIST, appSvcVehicleDtos);

        log.debug(StringUtil.changeForLog("prePareVehicles end ..."));
    }

    /**
     * StartStep: doVehicles
     *
     * @param bpc
     * @throws
     */
    public void doVehicles(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("doVehicles start ..."));
        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currSvcId);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String isEdit = ParamUtil.getString(bpc.request, IS_EDIT);
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(appSubmissionDto,
                RfcConst.EDIT_SERVICE, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        if (isGetDataFromPage) {
            //get data from page
            List<AppSvcVehicleDto> appSvcVehicleDtos = AppDataHelper.genAppSvcVehicleDto(bpc.request, appSubmissionDto.getAppType());
            currSvcInfoDto.setAppSvcVehicleDtoList(appSvcVehicleDtos);
            setAppSvcRelatedInfoMap(bpc.request, currSvcId, currSvcInfoDto);
        }

        String crud_action_type = ParamUtil.getRequestString(bpc.request, "nextStep");
        Map<String, String> map = new HashMap<>();
        if ("next".equals(crud_action_type)) {
            List<AppSvcVehicleDto> appSvcVehicleDtos = IaisCommonUtils.genNewArrayList();
            if (!IaisCommonUtils.isEmpty(appSubmissionDto.getAppSvcRelatedInfoDtoList())) {
                appSubmissionDto.getAppSvcRelatedInfoDtoList().forEach(obj -> {
                    // Don't add current service vehicles
                    if (Objects.equals(obj.getServiceId(), currSvcId)) {
                        return;
                    }
                    if (!IaisCommonUtils.isEmpty(obj.getAppSvcVehicleDtoList())) {
                        appSvcVehicleDtos.addAll(obj.getAppSvcVehicleDtoList());
                    }
                });
            }
            List<String> ids = ApplicationHelper.getRelatedId(currSvcInfoDto.getAppId(), appSubmissionDto.getLicenceId(),
                    currSvcInfoDto.getServiceName());
            List<AppSvcVehicleDto> oldAppSvcVehicleDto = appCommService.getActiveVehicles(ids, true);
            new ValidateVehicle().doValidateVehicles(map, appSvcVehicleDtos, currSvcInfoDto.getAppSvcVehicleDtoList(),
                    oldAppSvcVehicleDto, isRfi);
        }
        checkAction(map, HcsaConsts.STEP_VEHICLES, appSubmissionDto, bpc.request);
        log.debug(StringUtil.changeForLog("doVehicles end ..."));
    }

    public void prepareClinicalDirector(BaseProcessClass bpc) {
        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        // Clinical Director config
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList = configCommService.getHcsaSvcPersonnel(currSvcId,
                ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR);
        if (hcsaSvcPersonnelList != null && hcsaSvcPersonnelList.size() > 0) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = hcsaSvcPersonnelList.get(0);
            ParamUtil.setRequestAttr(bpc.request, CURR_STEP_CONFIG, hcsaSvcPersonnelDto);
        }
        // Assgined person dropdown options
        List<SelectOption> personList = ApplicationHelper.genAssignPersonSel(bpc.request, true);
        List<SelectOption> personBoard = ApplicationHelper.genPersonnelBoard();
        ParamUtil.setRequestAttr(bpc.request, "PERSONBOARD", personBoard);
        ParamUtil.setRequestAttr(bpc.request, CURR_STEP_PSN_OPTS, personList);
    }

    /**
     * StartStep: doClinicalDirector
     *
     * @param bpc
     * @throws
     */
    public void doClinicalDirector(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("doClinicalDirector start ..."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String actionType = ParamUtil.getRequestString(bpc.request, "nextStep");
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)
                || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(actionType)
                    || RfcConst.RFC_BTN_OPTION_SKIP.equals(actionType)) {
                return;
            }
        }
        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currSvcId);
        List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorList = currSvcInfoDto.getAppSvcClinicalDirectorDtoList();
        String isEdit = ParamUtil.getString(bpc.request, IS_EDIT);
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(
                appSubmissionDto,
                RfcConst.EDIT_SERVICE, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        if (isGetDataFromPage) {
            //get data from page
            appSvcClinicalDirectorList = AppDataHelper.genAppSvcClinicalDirectorDto(bpc.request);
            currSvcInfoDto.setAppSvcClinicalDirectorDtoList(appSvcClinicalDirectorList);
            reSetChangesForApp(appSubmissionDto);
            setAppSvcRelatedInfoMap(bpc.request, currSvcId, currSvcInfoDto, appSubmissionDto);
            log.debug(StringUtil.changeForLog("cycle cd dto to retrieve prs info end ..."));
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String currSvcCode = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSVCCODE);
        if ("next".equals(actionType)) {
            Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(
                    bpc.request, LICPERSONSELECTMAP);
            errorMap = AppValidatorHelper.doValidateClincalDirector(appSvcClinicalDirectorList,
                    licPersonMap, true, currSvcCode);
            int psnLength = 0;
            if (!IaisCommonUtils.isEmpty(appSvcClinicalDirectorList)) {
                psnLength = appSvcClinicalDirectorList.size();
            }
            List<HcsaSvcPersonnelDto> psnConfig = configCommService.getHcsaSvcPersonnel(currSvcId,
                    ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR);
            AppValidatorHelper.psnMandatoryValidate(psnConfig, ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, errorMap,
                    psnLength, "psnMandatory", HcsaConsts.CLINICAL_DIRECTOR);
        }
        boolean isValid = checkAction(errorMap, HcsaConsts.STEP_CLINICAL_DIRECTOR, appSubmissionDto, bpc.request);
        if (isValid && isGetDataFromPage) {
            syncDropDownAndPsn(appSubmissionDto, currSvcInfoDto.getAppSvcClinicalDirectorDtoList(), currSvcCode, bpc.request);
        }
        log.debug(StringUtil.changeForLog("doClinicalDirector end ..."));
    }

    /**
     * StartStep: prePareCharges
     *
     * @param bpc
     * @throws
     */
    public void prePareCharges(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("prePareCharges start ..."));
        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currSvcId);
        //general charges config
        List<HcsaSvcPersonnelDto> generalChargesDtos = configCommService.getHcsaSvcPersonnel(currSvcId,
                ApplicationConsts.PERSONNEL_CHARGES);
        if (generalChargesDtos != null && generalChargesDtos.size() > 0) {
            HcsaSvcPersonnelDto generalChargesDto = generalChargesDtos.get(0);
            ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.GENERALCHARGESCONFIG, generalChargesDto);
        }
        //other charges config
        List<HcsaSvcPersonnelDto> otherChargesDtos = configCommService.getHcsaSvcPersonnel(currSvcId,
                ApplicationConsts.PERSONNEL_CHARGES_OTHER);
        if (otherChargesDtos != null && otherChargesDtos.size() > 0) {
            HcsaSvcPersonnelDto otherChargesDto = otherChargesDtos.get(0);
            ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.OTHERCHARGESCONFIG, otherChargesDto);
        }

        AppSvcChargesPageDto appSvcChargesPageDto = currSvcInfoDto.getAppSvcChargesPageDto();
        if (appSvcChargesPageDto != null) {
            ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.GENERALCHARGESDTOLIST, appSvcChargesPageDto.getGeneralChargesDtos());
            ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.OTHERCHARGESDTOLIST, appSvcChargesPageDto.getOtherChargesDtos());
        }

        log.debug(StringUtil.changeForLog("prePareCharges end ..."));
    }

    /**
     * StartStep: doCharges
     *
     * @param bpc
     * @throws
     */
    public void doCharges(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("doCharges start ..."));

        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String isEdit = ParamUtil.getString(bpc.request, IS_EDIT);
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(appSubmissionDto,
                RfcConst.EDIT_SERVICE, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currSvcId);
        if (isGetDataFromPage) {
            //get data from page
            AppSvcChargesPageDto appSvcClinicalDirectorDto = AppDataHelper.genAppSvcChargesDto(bpc.request,
                    appSubmissionDto.getAppType());
            currSvcInfoDto.setAppSvcChargesPageDto(appSvcClinicalDirectorDto);
            setAppSvcRelatedInfoMap(bpc.request, currSvcId, currSvcInfoDto);
            reSetChangesForApp(appSubmissionDto);
        }
        String crud_action_type = ParamUtil.getRequestString(bpc.request, "nextStep");
        Map<String, String> map = new HashMap<>(8);
        if ("next".equals(crud_action_type)) {
            new ValidateCharges().doValidateCharges(map, currSvcInfoDto.getAppSvcChargesPageDto());
        }
        checkAction(map, HcsaConsts.STEP_CHARGES, appSubmissionDto, bpc.request);
        log.debug(StringUtil.changeForLog("doCharges end ..."));
    }

    /**
     * StartStep: prepareBusiness
     *
     * @param bpc
     * @throws
     */
    public void prepareBusiness(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("prepare business start ..."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        int serviceCount = appSubmissionDto.getAppSvcRelatedInfoDtoList().size();
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        ParamUtil.setRequestAttr(bpc.request, "maxCount", 3);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currSvcId);
        Map<String, AppSvcBusinessDto> premAlignBusinessMap = IaisCommonUtils.genNewHashMap();
        List<AppSvcBusinessDto> appSvcBusinessDtos = currSvcInfoDto.getAppSvcBusinessDtoList();
        String serviceCode = currSvcInfoDto.getServiceCode();
        ParamUtil.setRequestAttr(bpc.request, "serviceCode", serviceCode);
        if (!IaisCommonUtils.isEmpty(appSvcBusinessDtos)) {
            for (AppSvcBusinessDto appSvcBusinessDto : appSvcBusinessDtos) {
                if (StringUtil.isEmpty(appSvcBusinessDto.getPremIndexNo())) {
                    AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtoList.stream()
                            .filter(s -> appSvcBusinessDto.getPremAddress().equals(s.getAddress())).findAny().get();
                    appSvcBusinessDto.setPremIndexNo(appGrpPremisesDto.getPremisesIndexNo());
                }
                premAlignBusinessMap.put(appSvcBusinessDto.getPremIndexNo(), appSvcBusinessDto);
            }
        }
        ApplicationHelper.setTimeList(bpc.request);
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        ParamUtil.setRequestAttr(bpc.request, "isRfi", isRfi);
        ParamUtil.setRequestAttr(bpc.request, "serviceCount", serviceCount);
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.PREMALIGNBUSINESSMAP, premAlignBusinessMap);
        ParamUtil.setRequestAttr(bpc.request, "newBusiness", new AppSvcBusinessDto());
        log.debug(StringUtil.changeForLog("prepare business end ..."));
    }

    /**
     * StartStep: doBusiness
     *
     * @param bpc
     * @throws
     */
    public void doBusiness(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("do Business start ..."));

        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currSvcId);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String isEdit = ParamUtil.getString(bpc.request, IS_EDIT);
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        List<AppSvcBusinessDto> appSvcBusinessDtos = currSvcInfoDto.getAppSvcBusinessDtoList();
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(appSubmissionDto,
                RfcConst.EDIT_SERVICE, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        if (isGetDataFromPage) {
            //get data from page
            appSvcBusinessDtos = AppDataHelper.genAppSvcBusinessDtoList(bpc.request, appSubmissionDto.getAppGrpPremisesDtoList(),
                    appSubmissionDto.getAppType());
            currSvcInfoDto.setAppSvcBusinessDtoList(appSvcBusinessDtos);
            setAppSvcRelatedInfoMap(bpc.request, currSvcId, currSvcInfoDto);
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crud_action_type = ParamUtil.getRequestString(bpc.request, "nextStep");
        if ("next".equals(crud_action_type)) {
            AppValidatorHelper.doValidateBusiness(appSubmissionDto,appSvcBusinessDtos, appSubmissionDto.getAppType(),
                    appSubmissionDto.getLicenceId(),currSvcId, errorMap);
        }
        checkAction(errorMap, HcsaConsts.STEP_BUSINESS_NAME, appSubmissionDto, bpc.request);
        log.debug(StringUtil.changeForLog("do Business end ..."));
    }

    private ServiceStepDto getServiceStepDto(ServiceStepDto serviceStepDto, String action, List<HcsaServiceDto> hcsaServiceDtoList,
            String svcId, AppSubmissionDto appSubmissionDto) {
        //get the service information
        int serviceNum = -1;
        String svcCode = null;
        if (svcId != null && hcsaServiceDtoList != null && hcsaServiceDtoList.size() > 0) {
            for (int i = 0; i < hcsaServiceDtoList.size(); i++) {
                if (svcId.equals(hcsaServiceDtoList.get(i).getId())) {
                    serviceNum = i;
                    svcCode = hcsaServiceDtoList.get(i).getSvcCode();
                    break;
                }
            }
        }
        if (serviceStepDto == null || hcsaServiceDtoList == null) {
            log.info(StringUtil.changeForLog("serviceStepDto or hcsaServiceDtoList is null..."));
            throw new IaisRuntimeException("serviceStepDto or hcsaServiceDtoList is null...");
        }
        serviceStepDto.setServiceNumber(serviceNum);
        boolean serviceFirst = false;
        boolean serviceEnd = false;
        if (serviceNum == 0) {
            serviceFirst = true;
        }
        if (serviceNum + 1 == hcsaServiceDtoList.size()) {
            serviceEnd = true;
        }
        serviceStepDto.setServiceFirst(serviceFirst);
        serviceStepDto.setServiceEnd(serviceEnd);
        //get the step information
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = serviceStepDto.getHcsaServiceStepSchemeDtos();
        if (hcsaServiceStepSchemeDtos != null && hcsaServiceStepSchemeDtos.size() > 0) {
            int number = -1;
            int currentNumber = serviceStepDto.getCurrentNumber();
            if (StringUtil.isEmpty(action)) {
                number = 0;
            } else {
                for (int i = 0; i < hcsaServiceStepSchemeDtos.size(); i++) {
                    if (action.equals(hcsaServiceStepSchemeDtos.get(i).getStepCode())) {
                        number = i;
                        boolean toNext = currentNumber < i;
                        while (skipStep(hcsaServiceStepSchemeDtos, i++, svcCode, appSubmissionDto)) {
                            if (toNext) {
                                number++;
                            } else {
                                number--;
                            }
                        }
                        break;
                    }
                }
            }
            boolean stepFirst = false;
            boolean stepEnd = false;
            if (number == 0) {
                stepFirst = true;
            }
            if (number + 1 >= hcsaServiceStepSchemeDtos.size()) {
                stepEnd = true;
            }
            if (!stepEnd) {
                int i = number + 1;
                while (skipStep(hcsaServiceStepSchemeDtos, i, svcCode, appSubmissionDto)) {
                    i++;
                }
                if (i >= hcsaServiceStepSchemeDtos.size()) {
                    stepEnd = true;
                }
            }

            serviceStepDto.setStepFirst(stepFirst);
            serviceStepDto.setStepEnd(stepEnd);
            if (number > -1) {
                //clear the old data
                serviceStepDto.setPreviousStep(null);
                serviceStepDto.setNextStep(null);
                //set the new data
                serviceStepDto.setCurrentNumber(number);
                serviceStepDto.setCurrentStep(hcsaServiceStepSchemeDtos.get(number));
                if (stepFirst) {
                    if (!serviceFirst) {
                        HcsaServiceDto preHcsaServiceDto = hcsaServiceDtoList.get(serviceNum - 1);
                        HcsaServiceStepSchemeDto preHcsaServiceStepSchemeDto = new HcsaServiceStepSchemeDto();
                        preHcsaServiceStepSchemeDto.setStepCode(preHcsaServiceDto.getSvcCode());
                        serviceStepDto.setPreviousStep(preHcsaServiceStepSchemeDto);
                    }
                    if (stepEnd) {
                        if (!serviceEnd) {
                            HcsaServiceDto nextHcsaServiceDto = hcsaServiceDtoList.get(serviceNum + 1);
                            HcsaServiceStepSchemeDto nextHcsaServiceStepSchemeDto = new HcsaServiceStepSchemeDto();
                            nextHcsaServiceStepSchemeDto.setStepCode(nextHcsaServiceDto.getSvcCode());
                            serviceStepDto.setNextStep(nextHcsaServiceStepSchemeDto);
                        }
                    } else {
                        serviceStepDto.setNextStep(hcsaServiceStepSchemeDtos.get(number + 1));
                    }
                } else if (stepEnd) {
                    if (stepFirst) {
                        if (!serviceFirst) {
                            HcsaServiceDto preHcsaServiceDto = hcsaServiceDtoList.get(serviceNum - 1);
                            HcsaServiceStepSchemeDto preHcsaServiceStepSchemeDto = new HcsaServiceStepSchemeDto();
                            preHcsaServiceStepSchemeDto.setStepCode(preHcsaServiceDto.getSvcCode());
                            serviceStepDto.setPreviousStep(preHcsaServiceStepSchemeDto);
                        }
                    } else {
                        serviceStepDto.setPreviousStep(hcsaServiceStepSchemeDtos.get(number - 1));
                    }
                    if (!serviceEnd) {
                        HcsaServiceDto nextHcsaServiceDto = hcsaServiceDtoList.get(serviceNum + 1);
                        HcsaServiceStepSchemeDto nextHcsaServiceStepSchemeDto = new HcsaServiceStepSchemeDto();
                        nextHcsaServiceStepSchemeDto.setStepCode(nextHcsaServiceDto.getSvcCode());
                        serviceStepDto.setNextStep(nextHcsaServiceStepSchemeDto);
                    }

                } else {
                    serviceStepDto.setPreviousStep(hcsaServiceStepSchemeDtos.get(number - 1));
                    serviceStepDto.setNextStep(hcsaServiceStepSchemeDtos.get(number + 1));
                }
            }
        }
        return serviceStepDto;
    }

    private boolean skipStep(List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos, int i, String svcCode,
            AppSubmissionDto appSubmissionDto) {
        if (i >= hcsaServiceStepSchemeDtos.size()) {
            return false;
        }
        String stepCode = hcsaServiceStepSchemeDtos.get(i).getStepCode();
        String[] skipList = new String[]{HcsaConsts.STEP_LABORATORY_DISCIPLINES,
                HcsaConsts.STEP_DISCIPLINE_ALLOCATION};
        if (StringUtil.isIn(stepCode, skipList)) {
            return true;
        }
        if (HcsaConsts.STEP_OUTSOURCED_PROVIDERS.equals(stepCode)) {
            List<String> checkCodeList = IaisCommonUtils.genNewArrayList();
            checkCodeList.add(AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES);
            checkCodeList.add(AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL);
            checkCodeList.add(AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY);
            boolean match = appSubmissionDto.getAppSvcRelatedInfoDtoList()
                    .stream().anyMatch(s -> AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL.equals(s.getServiceCode()));
            if (match) {
                List<String> collect = appSubmissionDto.getAppSvcRelatedInfoDtoList().stream().map(
                        AppSvcRelatedInfoDto::getServiceCode).collect(Collectors.toList());
                checkCodeList.removeAll(collect);
                if (IaisCommonUtils.isNotEmpty(appSubmissionDto.getAppLicBundleDtos())) {
                    for (AppLicBundleDto appLicBundleDto : appSubmissionDto.getAppLicBundleDtos().get(0)) {
                        if (appLicBundleDto == null || StringUtil.isEmpty(appLicBundleDto.getSvcCode())) {
                            continue;
                        }
                        checkCodeList.remove(appLicBundleDto.getSvcCode());
                    }
                }
                if (checkCodeList.size() == 0) {
                    return true;
                }
            }
        }
        if (StringUtil.isIn(stepCode, skipList)) {
            return true;
        }
        //no special service,skip special_service_information
        if (HcsaConsts.STEP_SPECIAL_SERVICES_FORM.equals(stepCode)) {
            List<AppPremSpecialisedDto> appPremSpecialisedDtoList = appSubmissionDto.getAppPremSpecialisedDtoList();
            if (appPremSpecialisedDtoList == null || appPremSpecialisedDtoList.isEmpty() || appPremSpecialisedDtoList.stream()
                    .filter(dto -> Objects.equals(svcCode, dto.getBaseSvcCode()))
                    .noneMatch(AppPremSpecialisedDto::isExistCheckedRels)) {
                return true;
            }
        }
        return false;
    }

    public static AppSubmissionDto getAppSubmissionDto(HttpServletRequest request) {
        return ApplicationHelper.getAppSubmissionDto(request);
    }

    public static void setAppSubmissionDto(AppSubmissionDto appSubmissionDto, HttpServletRequest request) {
        ApplicationHelper.setAppSubmissionDto(appSubmissionDto, request);
    }

    private void setAppSvcRelatedInfoMap(HttpServletRequest request, String currentSvcId, AppSvcRelatedInfoDto currSvcInfoDto) {
        setAppSvcRelatedInfoMap(request, currentSvcId, currSvcInfoDto, null);
    }

    private void setAppSvcRelatedInfoMap(HttpServletRequest request, String currentSvcId, AppSvcRelatedInfoDto currSvcInfoDto,
            AppSubmissionDto appSubmissionDto) {
        if (appSubmissionDto == null) {
            appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
        }
        String appNo = currSvcInfoDto.getAppNo();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (appSvcRelatedInfoDtos != null && !appSvcRelatedInfoDtos.isEmpty()) {
            int i = 0;
            for (AppSvcRelatedInfoDto svcRelatedInfoDto : appSvcRelatedInfoDtos) {
                if (currentSvcId.equals(svcRelatedInfoDto.getServiceId()) && (StringUtil.isEmpty(appNo) ||
                        appNo.equals(svcRelatedInfoDto.getAppNo()))) {
                    appSvcRelatedInfoDtos.set(i, currSvcInfoDto);
                    break;
                }
                i++;
            }
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
        }
        ParamUtil.setSessionAttr(request, APPSUBMISSIONDTO, appSubmissionDto);
    }

    public static List<SelectOption> genPersonnelDesignSel(String currentSvcCod) {
        List<SelectOption> designation = IaisCommonUtils.genNewArrayList();

        if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(currentSvcCod)) {
            SelectOption designationOp1 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_DIAGNOSTIC_RADIOGRAPHER,
                    ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_DIAGNOSTIC_RADIOGRAPHER);
            SelectOption designationOp2 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_RADIATION_THERAPIST,
                    ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_RADIATION_THERAPIST);
            SelectOption designationOp3 = new SelectOption(
                    ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_NUCLEAR_MEDICINE_TECHNOLOGIST,
                    ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_NUCLEAR_MEDICINE_TECHNOLOGIST);
            designation.add(designationOp1);
            designation.add(designationOp3);
            designation.add(designationOp2);
        } else if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(currentSvcCod)) {

        } else if (AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(currentSvcCod)) {
            SelectOption designationOp1 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_MEDICAL_PRACTITIONER,
                    ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_MEDICAL_PRACTITIONER);
            SelectOption designationOp2 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_CLINICAL_NURSE_LEADER,
                    ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_CLINICAL_NURSE_LEADER);
            designation.add(designationOp2);
            designation.add(designationOp1);
        } else if (AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(currentSvcCod)) {

        }
        designation.sort(Comparator.comparing(SelectOption::getText));
        if (designation.size() > 0) {
            designation.add(new SelectOption(HcsaAppConst.DESIGNATION_OTHERS, HcsaAppConst.DESIGNATION_OTHERS));
        }
        return designation;
    }

    /*public static List<SelectOption> getAssignPrincipalOfficerSel(HttpServletRequest request, boolean needFirstOpt) {
        List<SelectOption> assignSelectList = IaisCommonUtils.genNewArrayList();
        if (needFirstOpt) {
            SelectOption assignOp1 = new SelectOption("-1", AppCommConst.FIRESTOPTION);
            assignSelectList.add(assignOp1);
        }
        SelectOption assignOp2 = new SelectOption(IaisEGPConstant.ASSIGN_SELECT_ADD_NEW, "I'd like to add a new personnel");
        assignSelectList.add(assignOp2);
        //get current cgo,po,dpo,medAlert
        Map<String, AppSvcPrincipalOfficersDto> psnMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(request, PERSONSELECTMAP);
        psnMap.forEach((k, v) -> {
            SelectOption sp = new SelectOption(k, v.getName() + v.getIdNo());
            assignSelectList.add(sp);
        });

        return assignSelectList;
    }

    public static List<SelectOption> getAssignMedAlertSel(boolean needFirstOpt) {
        List<SelectOption> assignSelectList = IaisCommonUtils.genNewArrayList();
        if (needFirstOpt) {
            SelectOption assignOp1 = new SelectOption("-1", AppCommConst.FIRESTOPTION);
            assignSelectList.add(assignOp1);
        }
        SelectOption assignOp2 = new SelectOption(IaisEGPConstant.ASSIGN_SELECT_ADD_NEW, "I'd like to add a new personnel");
        assignSelectList.add(assignOp2);
        return assignSelectList;
    }

    public static List<SelectOption> getMedAlertSelectList() {
        List<SelectOption> MedAlertSelectList = IaisCommonUtils.genNewArrayList();
        SelectOption idType0 = new SelectOption("", AppCommConst.FIRESTOPTION);
        MedAlertSelectList.add(idType0);
        SelectOption idType1 = new SelectOption("Email", "Email");
        MedAlertSelectList.add(idType1);
        SelectOption idType2 = new SelectOption("SMS", "SMS");
        MedAlertSelectList.add(idType2);
        return MedAlertSelectList;
    }

    private boolean judegCanEdit(AppSubmissionDto appSubmissionDto) {
        boolean canEdit = true;
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
        if (appEditSelectDto != null) {
            if (ApplicationConsts.APPLICATION_EDIT_TYPE_RFI.equals(appEditSelectDto.getEditType()) && !appEditSelectDto.isServiceEdit()) {
                canEdit = false;
            }
        }
        return canEdit;
    }

    private Set<String> getRfcClickEditPageSet(AppSubmissionDto appSubmissionDto) {
        return appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
    }*/


    private void removeEmptyPsn(List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos,
            List<AppSvcPrincipalOfficersDto> reloadDto) throws Exception {
        for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtos) {
            boolean isAllFieldNull = ApplicationHelper.isAllFieldNull(appSvcPrincipalOfficersDto);
            if (!isAllFieldNull) {
                reloadDto.add(appSvcPrincipalOfficersDto);
            }
        }
    }

    private Map<String, AppSvcPersonAndExtDto> syncDropDownAndPsn(AppSubmissionDto appSubmissionDto,
            List<AppSvcPrincipalOfficersDto> personList, String svcCode, HttpServletRequest request) {
        if (StringUtil.isEmpty(svcCode)) {
            svcCode = (String) ParamUtil.getSessionAttr(request, CURRENTSVCCODE);
        }
        Map<String, AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(request,
                PERSONSELECTMAP);
        if (personList == null || personList.isEmpty()) {
            return personMap;
        }
        boolean isSync = syncDropDownAndPsn(personMap, appSubmissionDto, personList, svcCode);
        log.info(StringUtil.changeForLog("-----Sync Dropdown and Psn: " + isSync + "-----"));
        if (!isSync) {
            return personMap;
        }
        Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(request,
                LICPERSONSELECTMAP);
        Map<String, AppSvcPersonAndExtDto> newPersonMap = removeDirtyDataFromPsnDropDown(appSubmissionDto, licPersonMap, personMap);
        ParamUtil.setSessionAttr(request, PERSONSELECTMAP, (Serializable) newPersonMap);
        DealSessionUtil.reSetInit(appSubmissionDto, HcsaAppConst.SECTION_SVCINFO);
        ParamUtil.setSessionAttr(request, APPSUBMISSIONDTO, appSubmissionDto);
        //remove dirty psn doc info
        /*String personType = personList.get(0).getPsnType();
        removeDirtyPsnDoc(personType, request);*/

        return newPersonMap;
    }

    private boolean syncDropDownAndPsn(Map<String, AppSvcPersonAndExtDto> personMap,
            AppSubmissionDto appSubmissionDto, List<AppSvcPrincipalOfficersDto> personList, String svcCode) {
        List<AppSvcPrincipalOfficersDto> newPersonList = IaisCommonUtils.genNewArrayList();
        for (AppSvcPrincipalOfficersDto person : personList) {
            //Provisional judgment
            //personnel data=>sync , personnel ext data => the same svc =>sync
            if (AppValidatorHelper.psnDoPartValidate(person.getIdType(), person.getIdNo(), person.getName())) {
                newPersonList.add(person);
            }
        }
        if (newPersonList.isEmpty()) {
            return false;
        }
        //set person into dropdown
        ApplicationHelper.initSetPsnIntoSelMap(personMap, newPersonList, svcCode);
        //sync data
        ApplicationHelper.syncPsnData(appSubmissionDto, personMap);
        return true;
    }

    private String getStepName(BaseProcessClass bpc, String currSvcId, String stepCode) {
        String stepName = "";
        ServiceStepDto serviceStepDto = (ServiceStepDto) ParamUtil.getSessionAttr(bpc.request,
                ShowServiceFormsDelegator.SERVICESTEPDTO);
        if (serviceStepDto != null && !StringUtil.isEmpty(currSvcId)) {
            List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = serviceStepDto.getHcsaServiceStepSchemeDtos();
            if (!IaisCommonUtils.isEmpty(hcsaServiceStepSchemeDtos)) {
                for (HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto : hcsaServiceStepSchemeDtos) {
                    if (currSvcId.equals(hcsaServiceStepSchemeDto.getServiceId()) && stepCode.equals(
                            hcsaServiceStepSchemeDto.getStepCode())) {
                        stepName = hcsaServiceStepSchemeDto.getStepName();
                        break;
                    }
                }
            }
        }
        return stepName;
    }

    private Map<String, AppSvcPersonAndExtDto> removeDirtyDataFromPsnDropDown(AppSubmissionDto appSubmissionDto,
            Map<String, AppSvcPersonAndExtDto> licPersonMap, Map<String, AppSvcPersonAndExtDto> personMap) {
        //add new person key
        Set<String> personKeySet = new HashSet<>(licPersonMap.keySet());
        Set<String> newPersonKeySet = getNewPersonKeySet(appSubmissionDto);
        personKeySet.addAll(newPersonKeySet);
        //filter removed person
        Map<String, AppSvcPersonAndExtDto> newPersonMap = IaisCommonUtils.genNewHashMap();
        personMap.forEach((k, v) -> {
            if (personKeySet.contains(k)) {
                newPersonMap.put(k, v);
            }
        });
        return newPersonMap;
    }

    private Set<String> getNewPersonKeySet(AppSubmissionDto appSubmissionDto) {
        Set<String> personKeySet = IaisCommonUtils.genNewHashSet();
        if (appSubmissionDto != null) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    // Clinical director
                    List<AppSvcPrincipalOfficersDto> appSvCdDtos = appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();
                    if (!IaisCommonUtils.isEmpty(appSvCdDtos)) {
                        for (AppSvcPrincipalOfficersDto psn : appSvCdDtos) {
                            setPsnKeySet(psn, personKeySet);
                        }
                    }
                    // CGO
                    List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                    if (!IaisCommonUtils.isEmpty(appSvcCgoDtos)) {
                        for (AppSvcPrincipalOfficersDto psn : appSvcCgoDtos) {
                            setPsnKeySet(psn, personKeySet);
                        }
                    }
                    List<AppSvcPrincipalOfficersDto> appSvcPoDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                    if (!IaisCommonUtils.isEmpty(appSvcPoDtos)) {
                        for (AppSvcPrincipalOfficersDto psn : appSvcPoDtos) {
                            setPsnKeySet(psn, personKeySet);
                        }
                    }
                    List<AppSvcPrincipalOfficersDto> appSvcDpoDtos = appSvcRelatedInfoDto.getAppSvcNomineeDtoList();
                    if (!IaisCommonUtils.isEmpty(appSvcDpoDtos)) {
                        for (AppSvcPrincipalOfficersDto psn : appSvcDpoDtos) {
                            setPsnKeySet(psn, personKeySet);
                        }
                    }
                    List<AppSvcPrincipalOfficersDto> appSvcMapDtos = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
                    if (!IaisCommonUtils.isEmpty(appSvcMapDtos)) {
                        for (AppSvcPrincipalOfficersDto psn : appSvcMapDtos) {
                            setPsnKeySet(psn, personKeySet);
                        }
                    }
                    //Key Appointment Holder
                    List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderDtoList = appSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList();
                    if (!IaisCommonUtils.isEmpty(appSvcKeyAppointmentHolderDtoList)) {
                        for (AppSvcPrincipalOfficersDto psn : appSvcKeyAppointmentHolderDtoList) {
                            setPsnKeySet(psn, personKeySet);
                        }
                    }
                }
            }
        }
        return personKeySet;
    }

    private void setPsnKeySet(AppSvcPrincipalOfficersDto psn, Set<String> personKeySet) {
        if (psn == null || personKeySet == null) {
            return;
        }
        String assignSel = psn.getAssignSelect();
        if (!psn.isLicPerson()) {
            boolean partValidate = AppValidatorHelper.psnDoPartValidate(psn.getIdType(), psn.getIdNo(), psn.getName());
            if (partValidate) {
                String personKey = ApplicationHelper.getPersonKey(psn.getNationality(), psn.getIdType(), psn.getIdNo());
                personKeySet.add(personKey);
                psn.setAssignSelect(personKey);
            } else if (!StringUtil.isEmpty(assignSel) && !"-1".equals(assignSel)) {
                psn.setAssignSelect(HcsaAppConst.NEW_PSN);
            }
        } else {
            personKeySet.add(ApplicationHelper.getPersonKey(psn.getNationality(), psn.getIdType(), psn.getIdNo()));
        }
    }

    private void removeDirtyPsnDoc(final String psnType, HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("remove dirty psn doc info start ..."));
        if (StringUtil.isEmpty(psnType)) {
            return;
        }
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
        DealSessionUtil.reSetInit(appSubmissionDto, HcsaAppConst.SECTION_SVCINFO);
        ApplicationHelper.setAppSubmissionDto(appSubmissionDto, request);
        /*String currentSvcId = ApplicationHelper.getCurrentServiceId(request);
        AppSvcRelatedInfoDto currentSvcRelatedDto = ApplicationHelper.getAppSvcRelatedInfo(appSubmissionDto, currentSvcId, null);
        List<HcsaSvcDocConfigDto> svcDocConfigDtos = configCommService.getAllHcsaSvcDocs(currentSvcId);
        List<AppSvcPrincipalOfficersDto> psnDtoList = ApplicationHelper.getBasePersonnel(currentSvcRelatedDto,
                psnType);
        List<AppSvcDocDto> appSvcDocDtoList = currentSvcRelatedDto.getAppSvcDocDtoLit();
        List<HcsaSvcDocConfigDto> targetConfigDtos = getDocConfigDtoByPsnType(svcDocConfigDtos, psnType);
        if (!IaisCommonUtils.isEmpty(appSvcDocDtoList) && !IaisCommonUtils.isEmpty(targetConfigDtos)
                && !IaisCommonUtils.isEmpty(psnDtoList)) {
            log.info("appSvcDocDtoList size is {}", appSvcDocDtoList.size());
            List<String> psnIndexList = IaisCommonUtils.genNewArrayList();
            for (AppSvcPrincipalOfficersDto psnDto : psnDtoList) {
                if (!StringUtil.isEmpty(psnDto.getIndexNo())) {
                    psnIndexList.add(psnDto.getIndexNo());
                }
            }
            List<String> targetConfigIdList = IaisCommonUtils.genNewArrayList();
            for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : targetConfigDtos) {
                targetConfigIdList.add(hcsaSvcDocConfigDto.getId());
            }
            List<AppSvcDocDto> newAppSvcDocDtoList = IaisCommonUtils.genNewArrayList();
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtoList) {
                String svcDocId = appSvcDocDto.getSvcDocId();
                if (!StringUtil.isEmpty(svcDocId) && targetConfigIdList.contains(svcDocId)) {
                    //judge align psn if existing
                    String psnIndexNo = appSvcDocDto.getPsnIndexNo();
                    if (psnIndexList.contains(psnIndexNo)) {
                        newAppSvcDocDtoList.add(appSvcDocDto);
                    }
                } else {
                    newAppSvcDocDtoList.add(appSvcDocDto);
                }
            }
            log.info("newAppSvcDocDtoList size is {}", newAppSvcDocDtoList.size());
            currentSvcRelatedDto.setAppSvcDocDtoLit(newAppSvcDocDtoList);
        }
        currentSvcRelatedDto.setDocumentShowDtoList(null);
        setAppSvcRelatedInfoMap(request, currentSvcId, currentSvcRelatedDto, appSubmissionDto);*/
    }

    private List<HcsaSvcDocConfigDto> getDocConfigDtoByPsnType(List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos,
            String... psnType) {
        if (IaisCommonUtils.isEmpty(hcsaSvcDocConfigDtos) || IaisCommonUtils.isSpecialEmpty(psnType)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return hcsaSvcDocConfigDtos.stream()
                .filter(config -> StringUtil.isIn(config.getDupForPerson(), psnType))
                .collect(Collectors.toList());
    }

    private Map<String, String> servicePersonPrsValidate(HttpServletRequest request, Map<String, String> errorMap,
            List<AppSvcPersonnelDto> appSvcPersonnelDtos) {
        if (!IaisCommonUtils.isEmpty(appSvcPersonnelDtos)) {
            for (int i = 0; i < appSvcPersonnelDtos.size(); i++) {
                AppSvcPersonnelDto appSvcPersonnelDto = appSvcPersonnelDtos.get(i);
                String profRegNo = appSvcPersonnelDto.getProfRegNo();
                if (!StringUtil.isEmpty(profRegNo)) {
                    ProfessionalResponseDto professionalResponseDto = appCommService.retrievePrsInfo(profRegNo);
                    if (professionalResponseDto == null) {
                        continue;
                    }
                    if (professionalResponseDto.isHasException() || StringUtil.isNotEmpty(professionalResponseDto.getStatusCode())) {
                        log.debug(StringUtil.changeForLog("prs svc down ..."));
                        if (professionalResponseDto.isHasException()) {
                            request.setAttribute(PRS_SERVICE_DOWN, PRS_SERVICE_DOWN);
                            errorMap.put(PRS_SERVICE_DOWN, PRS_SERVICE_DOWN);
                        } else if ("401".equals(professionalResponseDto.getStatusCode())) {
                            errorMap.put("regnNo" + i, "GENERAL_ERR0054");
                        } else {
                            errorMap.put("regnNo" + i, "GENERAL_ERR0042");
                        }
                        continue;
                    }
                    appSvcPersonnelDto.setName(professionalResponseDto.getName());
                }
            }
        }
        return errorMap;
    }

    private AppSvcPrincipalOfficersDto setClinicalDirectorPrsInfo(AppSvcPrincipalOfficersDto appSvcPsnDto, String specialtyStr,
            String specialtyGetDateStr, String typeOfCurrRegi, String currRegiDateStr, String praCerEndDateStr,
            String typeOfRegister) {
        appSvcPsnDto.setSpeciality(specialtyStr);
        appSvcPsnDto.setTypeOfCurrRegi(typeOfCurrRegi);
        appSvcPsnDto.setTypeOfRegister(typeOfRegister);

        appSvcPsnDto.setSpecialtyGetDateStr(specialtyGetDateStr);
        if (StringUtil.isEmpty(specialtyGetDateStr)) {
            appSvcPsnDto.setSpecialtyGetDate(null);
        } else {
            Date date = DateUtil.parseDate(specialtyGetDateStr, Formatter.DATE);
            appSvcPsnDto.setSpecialtyGetDate(date);
        }
        appSvcPsnDto.setCurrRegiDateStr(currRegiDateStr);
        if (StringUtil.isEmpty(currRegiDateStr)) {
            appSvcPsnDto.setCurrRegiDate(null);
        } else {
            Date date = DateUtil.parseDate(currRegiDateStr, Formatter.DATE);
            appSvcPsnDto.setCurrRegiDate(date);
        }
        appSvcPsnDto.setPraCerEndDateStr(praCerEndDateStr);
        if (StringUtil.isEmpty(praCerEndDateStr)) {
            appSvcPsnDto.setPraCerEndDate(null);
        } else {
            Date date = DateUtil.parseDate(praCerEndDateStr, Formatter.DATE);
            appSvcPsnDto.setPraCerEndDate(date);
        }
        return appSvcPsnDto;
    }

    private void reSetChangesForApp(AppSubmissionDto appSubmissionDto) {
        if (appSubmissionDto.isNeedEditController()) {
            AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
            appEditSelectDto.setServiceEdit(true);
            appSubmissionDto.setChangeSelectDto(appEditSelectDto);
        }
    }

    public int handleLength(int mandatoryCount, int listSize, Integer maxCount) {
        if (StringUtil.isEmpty(maxCount) || (mandatoryCount == maxCount && mandatoryCount == 0) || mandatoryCount == -1) {
            listSize = 0;
            return listSize;
        }
        if (mandatoryCount != maxCount && mandatoryCount == 0) {
            if (mandatoryCount == listSize) {
                listSize = 1;
            }
        }
        if (mandatoryCount > listSize) {
            listSize = mandatoryCount;
        }
        return listSize;
    }
}
