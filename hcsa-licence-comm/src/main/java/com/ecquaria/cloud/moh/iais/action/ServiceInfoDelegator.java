package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.RegistrationDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
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
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.validation.ValidateCharges;
import com.ecquaria.cloud.moh.iais.validation.ValidateClincalDirector;
import com.ecquaria.cloud.moh.iais.validation.ValidateVehicle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.APPSUBMISSIONDTO;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.CO_MAP;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.CURRENTSERVICEID;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.CURRENTSVCCODE;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.CURR_STEP_CONFIG;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.CURR_STEP_PSN_OPTS;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.IS_EDIT;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.LICPERSONSELECTMAP;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.PERSONSELECTMAP;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.PRS_SERVICE_DOWN;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.SECTION_LEADER_LIST;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.SVC_DOC_CONFIG;

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
    protected SystemParamConfig systemParamConfig;

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

        //svc
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.RELOADSVCDOC, null);
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
        ServiceStepDto serviceStepDto = (ServiceStepDto) ParamUtil.getSessionAttr(bpc.request,
                ShowServiceFormsDelegator.SERVICESTEPDTO);
        String svcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,
                AppServicesConsts.HCSASERVICEDTOLIST);
        serviceStepDto = getServiceStepDto(serviceStepDto, action, hcsaServiceDtoList, svcId);
        //reset value
        if (HcsaConsts.STEP_DISCIPLINE_ALLOCATION.equals(action)) {
            action = serviceStepDto.getCurrentStep().getStepCode();
        }
        ParamUtil.setSessionAttr(bpc.request, ShowServiceFormsDelegator.SERVICESTEPDTO, serviceStepDto);

        if (StringUtil.isEmpty(action) || IaisEGPConstant.YES.equals(formTab)) {
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

        String crudActionType = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
        if (StringUtil.isEmpty(crudActionType)) {
            crudActionType = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, crudActionType);
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
        String currentStepName = Optional.ofNullable(step)
                .map(HcsaServiceStepSchemeDto::getStepName)
                .orElse(HcsaConsts.BUSINESS_NAME);
        log.info(StringUtil.changeForLog("--- Prepare " + currentStepName + " Start ---"));
        String currentStep = Optional.ofNullable(step)
                .map(HcsaServiceStepSchemeDto::getStepCode)
                .orElse(HcsaConsts.STEP_BUSINESS_NAME);
        String singleName = "";
        if (HcsaConsts.STEP_BUSINESS_NAME.equals(currentStep)) {
            singleName = HcsaConsts.BUSINESS_NAME;
            prepareBusiness(bpc);
        } else if (HcsaConsts.STEP_VEHICLES.equals(currentStep)) {
            singleName = HcsaConsts.VEHICLE;
            prePareVehicles(bpc);
        } else if (HcsaConsts.STEP_CLINICAL_DIRECTOR.equals(currentStep)) {
            singleName = HcsaConsts.CLINICAL_DIRECTOR;
            prePareClinicalDirector(bpc);
        } else if (HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(currentStep)) {
            singleName = HcsaConsts.CLINICAL_GOVERNANCE_OFFICER;
            prepareGovernanceOfficers(bpc);
        } else if (HcsaConsts.STEP_SECTION_LEADER.equals(currentStep)) {
            // Section Leader
            singleName = HcsaConsts.SECTION_LEADER;
            prepareSectionLeader(bpc);
        } else if (HcsaConsts.STEP_CHARGES.equals(currentStep)) {
            singleName = "General Conveyance Charges";
            prePareCharges(bpc);
        } else if (HcsaConsts.STEP_SERVICE_PERSONNEL.equals(currentStep)) {
            singleName = HcsaConsts.SERVICE_PERSONNEL;
            prepareServicePersonnel(bpc);
        } else if (HcsaConsts.STEP_PRINCIPAL_OFFICERS.equals(currentStep)) {
            singleName = HcsaConsts.PRINCIPAL_OFFICER;
            preparePrincipalOfficers(bpc);
        } else if (HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER.equals(currentStep)) {
            singleName = HcsaConsts.KEY_APPOINTMENT_HOLDER;
            prepareKeyAppointmentHolder(bpc);
        } else if (HcsaConsts.STEP_MEDALERT_PERSON.equals(currentStep)) {
            singleName = HcsaConsts.MEDALERT_PERSON;
            prePareMedAlertPerson(bpc);
        } else if (HcsaConsts.STEP_DOCUMENTS.equals(currentStep)) {
            prepareDocuments(bpc);
        } else {
            log.warn(StringUtil.changeForLog("Wrong Step!!!"));
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
        String currentStepName = Optional.ofNullable(step)
                .map(HcsaServiceStepSchemeDto::getStepName)
                .orElse(HcsaConsts.BUSINESS_NAME);
        log.info(StringUtil.changeForLog("--- Do " + currentStepName + " Start ---"));
        String currentStep = Optional.ofNullable(step)
                .map(HcsaServiceStepSchemeDto::getStepCode)
                .orElse(HcsaConsts.STEP_BUSINESS_NAME);
        String pageStep = ParamUtil.getString(bpc.request, "currentStep");
        if (currentStep.equals(pageStep)) {
            log.warn(StringUtil.changeForLog("Wrong page step - " + pageStep));
        }
        if (HcsaConsts.STEP_BUSINESS_NAME.equals(currentStep)) {
            doBusiness(bpc);
        } else if (HcsaConsts.STEP_VEHICLES.equals(currentStep)) {
            doVehicles(bpc);
        } else if (HcsaConsts.STEP_CLINICAL_DIRECTOR.equals(currentStep)) {
            doClinicalDirector(bpc);
        } else if (HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(currentStep)) {
            doGovernanceOfficers(bpc);
        } else if (HcsaConsts.STEP_SECTION_LEADER.equals(currentStep)) {
            // Section Leader
            doSectionLeader(bpc);
        } else if (HcsaConsts.STEP_CHARGES.equals(currentStep)) {
            doCharges(bpc);
        } else if (HcsaConsts.STEP_SERVICE_PERSONNEL.equals(currentStep)) {
            doServicePersonnel(bpc);
        } else if (HcsaConsts.STEP_PRINCIPAL_OFFICERS.equals(currentStep)) {
            doPrincipalOfficers(bpc);
        } else if (HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER.equals(currentStep)) {
            doKeyAppointmentHolder(bpc);
        } else if (HcsaConsts.STEP_MEDALERT_PERSON.equals(currentStep)) {
            doMedAlertPerson(bpc);
        } else if (HcsaConsts.STEP_DOCUMENTS.equals(currentStep)) {
            doDocuments(bpc);
        } else {
            log.warn(StringUtil.changeForLog("--- Wrong Step!!!"));
        }
        log.info(StringUtil.changeForLog("--- Do " + currentStepName + " End ---"));
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
        Map<String, String> errorMap = null;
        if ("next".equals(action)) {
            if (StringUtil.isEmpty(currSvcInfoDto.getServiceCode())) {
                HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(currSvcId);
                if (serviceDto != null) {
                    currSvcInfoDto.setServiceId(currSvcId);
                    currSvcInfoDto.setServiceCode(serviceDto.getSvcCode());
                    currSvcInfoDto.setServiceName(serviceDto.getSvcName());
                }
            }
            errorMap = AppValidatorHelper.validateSectionLeaders(currSvcInfoDto.getAppSvcSectionLeaderList(),
                    currSvcInfoDto.getServiceCode());
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
            removeDirtyPsnDoc(ApplicationConsts.DUP_FOR_PERSON_SL, bpc.request);
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

        if (principalOfficerConfig != null && !principalOfficerConfig.isEmpty()) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = principalOfficerConfig.get(0);
            ParamUtil.setRequestAttr(bpc.request, CURR_STEP_CONFIG, hcsaSvcPersonnelDto);
        }

        if (deputyPrincipalOfficerConfig != null && !deputyPrincipalOfficerConfig.isEmpty()) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = deputyPrincipalOfficerConfig.get(0);
            ParamUtil.setRequestAttr(bpc.request, "dpoHcsaSvcPersonnelDto", hcsaSvcPersonnelDto);
        }

        /*AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currentSvcId);
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        List<AppSvcPrincipalOfficersDto> principalOfficersDtos = IaisCommonUtils.genNewArrayList();
        List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)) {
            ApplicationHelper.assignPoDpoDto(appSvcPrincipalOfficersDtos, principalOfficersDtos, deputyPrincipalOfficersDtos);
            if (principalOfficersDtos.size() > mandatory) {
                mandatory = principalOfficersDtos.size();
            }
            if (deputyPrincipalOfficersDtos.size() > deputyMandatory) {
                deputyMandatory = deputyPrincipalOfficersDtos.size();
            }
        }
        //reload
        ParamUtil.setRequestAttr(bpc.request, "PrincipalOfficersMandatory", mandatory);
        ParamUtil.setRequestAttr(bpc.request, "DeputyPrincipalOfficersMandatory", deputyMandatory);
        ParamUtil.setRequestAttr(bpc.request, "ReloadPrincipalOfficers", principalOfficersDtos);
        ParamUtil.setRequestAttr(bpc.request, "ReloadDeputyPrincipalOfficers", deputyPrincipalOfficersDtos);*/
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currentSvcId);
        if (StringUtil.isEmpty(currSvcInfoDto.getDeputyPoFlag())) {
            List<AppSvcPrincipalOfficersDto> dpoList = currSvcInfoDto.getAppSvcNomineeDtoList();
            if (IaisCommonUtils.isEmpty(dpoList)) {
                currSvcInfoDto.setDeputyPoFlag(AppConsts.NO);
            } else {
                currSvcInfoDto.setDeputyPoFlag(AppConsts.YES);
            }
        }
        setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currSvcInfoDto);
        /*if (StringUtil.isEmpty(appSvcRelatedInfoDto.getDeputyPoFlag())) {
            ParamUtil.setRequestAttr(bpc.request, "DeputyPoFlag", "0");
        } else {
            ParamUtil.setRequestAttr(bpc.request, "DeputyPoFlag", appSvcRelatedInfoDto.getDeputyPoFlag());
        }*/

      /*  List<SelectOption> IdTypeSelect = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ID_TYPE);
        ParamUtil.setRequestAttr(bpc.request, "IdTypeSelect", IdTypeSelect);

        List<SelectOption> assignSelectList = ApplicationHelper.genAssignPersonSel(bpc.request, true);
        ParamUtil.setRequestAttr(bpc.request, "PrincipalOfficersAssignSelect", assignSelectList);

        List<SelectOption> deputyAssignSelectList = ApplicationHelper.genAssignPersonSel(bpc.request, true);
        ParamUtil.setRequestAttr(bpc.request, "DeputyPrincipalOfficersAssignSelect", deputyAssignSelectList);*/

        List<SelectOption> personList = ApplicationHelper.genAssignPersonSel(bpc.request, true);
        ParamUtil.setRequestAttr(bpc.request, CURR_STEP_PSN_OPTS, personList);

        List<SelectOption> deputyFlagSelect = IaisCommonUtils.genNewArrayList();
        deputyFlagSelect.add(new SelectOption("-1", HcsaAppConst.FIRESTOPTION));
        deputyFlagSelect.add(new SelectOption("0", "No"));
        deputyFlagSelect.add(new SelectOption("1", "Yes"));
        ParamUtil.setRequestAttr(bpc.request, "DeputyFlagSelect", deputyFlagSelect);

        ParamUtil.setRequestAttr(bpc.request, "currStepName2", "Nominee (Optional)");
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
        log.debug(StringUtil.changeForLog("the do prepareDocuments start ...."));
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(bpc.request);
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(appSubmissionDto, currentSvcId);
        ApplicationHelper.initDocumentShowDtoList(appSubmissionDto.getAppGrpPremisesDtoList(), currSvcInfoDto);
        /*List<HcsaSvcDocConfigDto> svcDocConfigDtos = configCommService.getAllHcsaSvcDocs(currentSvcId);
        List<DocumentShowDto> documentShowDtos = ApplicationHelper.genDocumentShowDtoList(svcDocConfigDtos,
                appSubmissionDto.getAppGrpPremisesDtoList(), currSvcInfoDto);
        currSvcInfoDto.setDocumentShowDtoList(documentShowDtos);*/
        setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currSvcInfoDto, appSubmissionDto);
        /*if (hcsaSvcDocDtos != null && !hcsaSvcDocDtos.isEmpty()) {
            List<HcsaSvcDocConfigDto> serviceDocConfigDto = IaisCommonUtils.genNewArrayList();
            List<HcsaSvcDocConfigDto> premServiceDocConfigDto = IaisCommonUtils.genNewArrayList();
            for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : hcsaSvcDocDtos) {
                if ("0".equals(hcsaSvcDocConfigDto.getDupForPrem())) {
                    serviceDocConfigDto.add(hcsaSvcDocConfigDto);
                } else if ("1".equals(hcsaSvcDocConfigDto.getDupForPrem())) {
                    premServiceDocConfigDto.add(hcsaSvcDocConfigDto);
                }
            }
            ParamUtil.setSessionAttr(bpc.request, "serviceDocConfigDto", (Serializable) serviceDocConfigDto);
            ParamUtil.setSessionAttr(bpc.request, "premServiceDocConfigDto", (Serializable) premServiceDocConfigDto);
        }
        ParamUtil.setSessionAttr(bpc.request, SVC_DOC_CONFIG, (Serializable) hcsaSvcDocDtos);

        Map<String, AppSvcDocDto> reloadSvcDo = IaisCommonUtils.genNewHashMap();
        if (appSvcRelatedInfoDto != null) {
            List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
            if (appSvcDocDtos != null && !appSvcDocDtos.isEmpty()) {
                for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                    *//*String premVal = appSvcDocDto.getPremisesVal();
                    if(StringUtil.isEmpty(premVal)){
                        reloadSvcDo.put(appSvcDocDto.getSvcDocId(), appSvcDocDto);
                    }else{
                        reloadSvcDo.put("prem" + appSvcDocDto.getSvcDocId() + premVal, appSvcDocDto);
                    }*//*
                    reloadSvcDo.put(appSvcDocDto.getPrimaryDocReloadName(), appSvcDocDto);
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.RELOADSVCDOC, (Serializable) reloadSvcDo);

        List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
        Map<String, List<AppSvcDocDto>> reloadDocMap = IaisCommonUtils.genNewHashMap();
        if (!IaisCommonUtils.isEmpty(appSvcDocDtos)) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                String reloadDocMapKey;
                String premVal = appSvcDocDto.getPremisesVal();
                if (StringUtil.isEmpty(premVal)) {
                    reloadDocMapKey = appSvcDocDto.getSvcDocId();
                } else {
                    reloadDocMapKey = premVal + appSvcDocDto.getSvcDocId();
                }
                String psnIndexNo = appSvcDocDto.getPsnIndexNo();
                if (!StringUtil.isEmpty(psnIndexNo)) {
                    reloadDocMapKey = reloadDocMapKey + psnIndexNo;
                }

                List<AppSvcDocDto> appSvcDocDtos1 = reloadDocMap.get(reloadDocMapKey);
                if (IaisCommonUtils.isEmpty(appSvcDocDtos1)) {
                    appSvcDocDtos1 = IaisCommonUtils.genNewArrayList();
                }
                appSvcDocDtos1.add(appSvcDocDto);
                reloadDocMap.put(reloadDocMapKey, appSvcDocDtos1);
            }
            //do sort
            reloadDocMap.forEach((k, v) -> Collections.sort(v, Comparator.comparing(AppSvcDocDto::getSeqNum)));
        }
        ParamUtil.setSessionAttr(bpc.request, "svcDocReloadMap", (Serializable) reloadDocMap);*/
        //set dupForPsn attr
        //ApplicationHelper.setDupForPersonAttr(bpc.request, appSvcRelatedInfoDto);


        int sysFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setRequestAttr(bpc.request, "sysFileSize", sysFileSize);
        log.debug(StringUtil.changeForLog("the do prepareDocuments end ...."));
    }

    /**
     * StartStep: prepareJump
     *
     * @param bpc
     * @throws
     */
    public void prepareJump(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prepareJump start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(CO_MAP);
        Map<String, String> allChecked = isAllChecked(bpc, appSubmissionDto);
        if (allChecked.isEmpty()) {
            coMap.put("information", "information");
        } else {
            coMap.put("information", "");
        }

        bpc.request.getSession().setAttribute(CO_MAP, coMap);
        log.debug(StringUtil.changeForLog("the do prepareJump end ...."));
    }

    /**
     * StartStep: prepareView
     *
     * @param bpc
     * @throws
     */
    public void prepareView(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the do prepareView start ...."));
        String iframeId = ParamUtil.getString(bpc.request, "iframeId");
        String maskName = ParamUtil.getString(bpc.request, "maskName");
        String svcId = ParamUtil.getMaskedString(bpc.request, maskName);
        String appNo = ParamUtil.getString(bpc.request, "appNo");
        if (!StringUtil.isEmpty(svcId)) {
            AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
            log.info(StringUtil.changeForLog("get current svc info...."));
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(appSubmissionDto, svcId, appNo);
            /*List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId = configCommService.getHcsaServiceStepSchemesByServiceId(
                    svcId);
            appSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
            //sort po,dpo
            for (HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto : hcsaServiceStepSchemesByServiceId) {
                switch (hcsaServiceStepSchemeDto.getStepCode()) {
                    case HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS:
                        List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                        if (!IaisCommonUtils.isEmpty(appSvcCgoDtos)) {
                            List<AppSvcPrincipalOfficersDto> reloadDto = IaisCommonUtils.genNewArrayList();
                            for (AppSvcPrincipalOfficersDto appSvcCgoDto : appSvcCgoDtos) {
                                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = MiscUtil.transferEntityDto(appSvcCgoDto,
                                        AppSvcPrincipalOfficersDto.class);
                                boolean isAllFieldNull = ApplicationHelper.isAllFieldNull(appSvcPrincipalOfficersDto);
                                if (!isAllFieldNull) {
                                    reloadDto.add(appSvcCgoDto);
                                }
                            }
                            if (IaisCommonUtils.isEmpty(reloadDto)) {
                                appSvcRelatedInfoDto.setAppSvcCgoDtoList(null);
                            } else {
                                appSvcRelatedInfoDto.setAppSvcCgoDtoList(reloadDto);
                            }
                        }
                        break;
                    case HcsaConsts.STEP_PRINCIPAL_OFFICERS:
                        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                        if (!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)) {
                            List<AppSvcPrincipalOfficersDto> reloadDto = IaisCommonUtils.genNewArrayList();
                            removeEmptyPsn(appSvcPrincipalOfficersDtos, reloadDto);
                            if (IaisCommonUtils.isEmpty(reloadDto)) {
                                appSvcRelatedInfoDto.setAppSvcPrincipalOfficersDtoList(null);
                            } else {
                                appSvcRelatedInfoDto.setAppSvcPrincipalOfficersDtoList(reloadDto);
                            }
                        }
                        break;
                    case HcsaConsts.STEP_SERVICE_PERSONNEL:
                        List<AppSvcPersonnelDto> appSvcPersonnelDtos = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
                        if (!IaisCommonUtils.isEmpty(appSvcPersonnelDtos)) {
                            List<AppSvcPersonnelDto> reloadDto = IaisCommonUtils.genNewArrayList();
                            for (AppSvcPersonnelDto appSvcPersonnelDto : appSvcPersonnelDtos) {
                                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = MiscUtil.transferEntityDto(appSvcPersonnelDto,
                                        AppSvcPrincipalOfficersDto.class);
                                boolean isAllFieldNull = ApplicationHelper.isAllFieldNull(appSvcPrincipalOfficersDto);
                                if (!isAllFieldNull) {
                                    reloadDto.add(appSvcPersonnelDto);
                                }
                            }
                            if (IaisCommonUtils.isEmpty(reloadDto)) {
                                appSvcRelatedInfoDto.setAppSvcPersonnelDtoList(null);
                            } else {
                                appSvcRelatedInfoDto.setAppSvcPersonnelDtoList(reloadDto);
                            }
                        }
                        break;
                    case HcsaConsts.STEP_MEDALERT_PERSON:
                        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
                        if (!IaisCommonUtils.isEmpty(appSvcMedAlertPersonList)) {
                            List<AppSvcPrincipalOfficersDto> reloadDto = IaisCommonUtils.genNewArrayList();
                            removeEmptyPsn(appSvcMedAlertPersonList, reloadDto);
                            if (IaisCommonUtils.isEmpty(reloadDto)) {
                                appSvcRelatedInfoDto.setAppSvcMedAlertPersonList(null);
                            } else {
                                appSvcRelatedInfoDto.setAppSvcMedAlertPersonList(reloadDto);
                            }
                        }
                        break;
                    case HcsaConsts.STEP_LABORATORY_DISCIPLINES:
                        break;
                    case HcsaConsts.STEP_DOCUMENTS:
                        break;
                    default:
                        break;
                }
            }*/
//            List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            ApplicationHelper.initDocumentShowDtoList(appGrpPremisesDtos, appSvcRelatedInfoDto);
//            List<HcsaSvcDocConfigDto> svcDocConfig = configCommService.getAllHcsaSvcDocs(svcId);

            //ParamUtil.setSessionAttr(bpc.request, SVC_DOC_CONFIG, (Serializable) svcDocConfig);
            //set dupForPsn attr
            //ApplicationHelper.setDupForPersonAttr(bpc.request, appSvcRelatedInfoDto);
            //svc doc add align for dup for prem
           /* ApplicationHelper.addPremAlignForSvcDoc(svcDocConfig, appSvcDocDtos, appGrpPremisesDtos);
            appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
            //set svc doc title
            Map<String, List<AppSvcDocDto>> reloadSvcDocMap = ApplicationHelper.genSvcDocReloadMap(svcDocConfig, appGrpPremisesDtos,
                    appSvcRelatedInfoDto);
            appSvcRelatedInfoDto.setMultipleSvcDoc(reloadSvcDocMap);*/

            ParamUtil.setSessionAttr(bpc.request, "currentPreviewSvcInfo", appSvcRelatedInfoDto);
            ParamUtil.setSessionAttr(bpc.request, "iframeId", iframeId);
        }

        log.debug(StringUtil.changeForLog("the do prepareView end ...."));
    }

    private Map<String, String> isAllChecked(BaseProcessClass bpc, AppSubmissionDto appSubmissionDto) {
        StringBuilder errorSvcConfig = new StringBuilder();
        List<AppSvcRelatedInfoDto> dto = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        Map<String, String> errorMap = new HashMap<>();
        Map<String, AppSvcPersonAndExtDto> licPersonMap = null;
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request, LICPERSONSELECTMAP);
        }
        for (int i = 0; i < dto.size(); i++) {
            AppSvcRelatedInfoDto currSvcInfoDto = dto.get(i);
            Map<String, String> map = AppValidatorHelper.doCheckBox(currSvcInfoDto, appSubmissionDto, licPersonMap);
            if (!map.isEmpty()) {
                errorMap.putAll(map);
                errorSvcConfig.append(currSvcInfoDto.getServiceId());
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "serviceConfig", errorSvcConfig.toString());
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
            Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,
                    LICPERSONSELECTMAP);
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

    /**
     * StartStep: doPrincipalOfficers
     *
     * @param bpc
     * @throws
     */
    public void doPrincipalOfficers(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doPrincipalOfficers start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currentSvcId);
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
        if (isGetDataFromPageDpo) {
            if (AppConsts.NO.equals(deputySelect)) {
                dpoList = IaisCommonUtils.genNewArrayList();
            } else {
                dpoList = AppDataHelper.genAppSvcNomineeDtos(bpc.request);
            }
            currSvcInfoDto.setAppSvcNomineeDtoList(dpoList);
        }
        setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currSvcInfoDto, appSubmissionDto);
        /*if (isGetDataFromPagePo || isGetDataFromPageDpo) {
            poList = AppDataHelper.genAppSvcPrincipalOfficersDto(bpc.request);
            dpoList = AppDataHelper.genAppSvcDeputyPrincipalOfficersDto(bpc.request);
            if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                    appSubmissionDto.getAppType()) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())
                    || isRfi) {
                List<AppSvcPrincipalOfficersDto> oldOfficersDtoList = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                for (AppSvcPrincipalOfficersDto officersDto : oldOfficersDtoList) {
                    if (!isGetDataFromPagePo && ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(officersDto.getPsnType())) {
                        appSvcPrincipalOfficersDtoList.add(officersDto);
                    } else if (!isGetDataFromPageDpo && ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(officersDto.getPsnType())) {
                        appSvcPrincipalOfficersDtoList.add(officersDto);
                    }
                }
            }

            String deputyPoFlag = ParamUtil.getString(bpc.request, "deputyPrincipalOfficer");
            if (!StringUtil.isEmpty(deputyPoFlag)) {
                appSvcRelatedInfoDto.setDeputyPoFlag(deputyPoFlag);
            }
            appSvcRelatedInfoDto.setAppSvcPrincipalOfficersDtoList(appSvcPrincipalOfficersDtoList);
        }*/
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        if ("next".equals(action)) {
            Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,
                    LICPERSONSELECTMAP);
            map.putAll(AppValidatorHelper.doValidatePoAndDpo(poList, dpoList, deputySelect, licPersonMap,
                    appSubmissionDto.getSubLicenseeDto(), true));
            //validate mandatory count
            List<HcsaSvcPersonnelDto> poPsnConfig = configCommService.getHcsaSvcPersonnel(currentSvcId,
                    ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
            List<HcsaSvcPersonnelDto> dpoPsnConfig = configCommService.getHcsaSvcPersonnel(currentSvcId,
                    ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
            AppValidatorHelper.psnMandatoryValidate(poPsnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_PO, map, poList.size(),
                    "poPsnMandatory", HcsaConsts.PRINCIPAL_OFFICER);
            AppValidatorHelper.psnMandatoryValidate(dpoPsnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, map,
                    dpoList.size(), "dpoPsnMandatory", HcsaConsts.NOMINEE);
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
        if (isValid && (isGetDataFromPagePo)) {
            String svcCode = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSVCCODE);
            syncDropDownAndPsn(appSubmissionDto, poList, svcCode, bpc.request);
            syncDropDownAndPsn(appSubmissionDto, dpoList, svcCode, bpc.request);
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
     * @throws
     */
    public void doDocuments(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do doDocuments start ...."));
        String currSvcCode = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSVCCODE);
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(
                HttpHandler.SOP6_MULTIPART_REQUEST);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);
        String crudActionTypeTab = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE_TAB);
        String crudActionTypeForm = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE_FORM);
        String crudActionTypeFormPage = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE_FORM_PAGE);
        String formTab = mulReq.getParameter(IaisEGPConstant.FORM_TAB);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, crudActionValue);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_TAB, crudActionTypeTab);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM, crudActionTypeForm);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_PAGE, crudActionTypeFormPage);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.FORM_TAB, formTab);
        if (!StringUtil.isEmpty(crudActionTypeFormPage)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, crudActionTypeFormPage);
        } else {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "jump");
        }
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String action = mulReq.getParameter("nextStep");
        ParamUtil.setRequestAttr(bpc.request, "nextStep", action);
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                appSubmissionDto.getAppType()) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)
                    || RfcConst.RFC_BTN_OPTION_SKIP.equals(action)) {
                //clear
                ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.RELOADSVCDOC, null);
                return;
            }
        }

        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        String isEdit = ParamUtil.getString(mulReq, IS_EDIT);
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(appSubmissionDto,
                RfcConst.EDIT_SERVICE, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(appSubmissionDto, currentSvcId, null);
        List<AppSvcDocDto> newAppSvcDocDtoList = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
        List<HcsaSvcDocConfigDto> svcDocConfigDtos = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request, SVC_DOC_CONFIG);
        Map<String, File> saveFileMap = IaisCommonUtils.genNewHashMap();
        if (isGetDataFromPage) {
            newAppSvcDocDtoList = IaisCommonUtils.genNewArrayList();
            //CommonsMultipartFile file = null;
            AppSubmissionDto oldSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos = null;
            String appGrpId = "";
            String appNo = "";
            if (oldSubmissionDto != null) {
                oldAppSvcRelatedInfoDtos = oldSubmissionDto.getAppSvcRelatedInfoDtoList();
                appGrpId = oldSubmissionDto.getAppGrpId();
                appNo = oldSubmissionDto.getRfiAppNo();
            }
            List<AppSvcDocDto> oldDocs = IaisCommonUtils.genNewArrayList();
            if (!IaisCommonUtils.isEmpty(oldAppSvcRelatedInfoDtos)) {
                for (AppSvcRelatedInfoDto oldSvcRelDto : oldAppSvcRelatedInfoDtos) {
                    if (currentSvcId.equals(oldSvcRelDto.getServiceId())) {
                        oldDocs = oldSvcRelDto.getAppSvcDocDtoLit();
                        break;
                    }
                }
            }
            if (appSubmissionDto.isNeedEditController()) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                //clickEditPages.add(APPLICATION_SVC_PAGE_NAME_DOCUMENT);
                appSubmissionDto.setClickEditPage(clickEditPages);
                reSetChangesForApp(appSubmissionDto);
            }

            List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request,
                    SVC_DOC_CONFIG);
            List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
            //premIndexNo+configId+seqnum
            int maxPsnTypeNum = getMaxPersonTypeNumber(appSvcDocDtos, oldDocs);
            int[] psnTypeNumArr = new int[]{maxPsnTypeNum};
            for (int i = 0; i < hcsaSvcDocConfigDtos.size(); i++) {
                HcsaSvcDocConfigDto hcsaSvcDocConfigDto = hcsaSvcDocConfigDtos.get(i);
                String dupForPrem = hcsaSvcDocConfigDto.getDupForPrem();
                if ("0".equals(dupForPrem)) {
                    String docKey = i + "svcDoc" + currSvcCode;
                    genSvcPersonDoc(hcsaSvcDocConfigDto, appSvcRelatedInfoDto, docKey, saveFileMap, appSvcDocDtos, newAppSvcDocDtoList,
                            oldDocs, isRfi, appGrpId, appNo, "", "", mulReq, bpc.request, psnTypeNumArr);
                } else if ("1".equals(dupForPrem)) {
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                        String docKey = i + "svcDoc" + currSvcCode + appGrpPremisesDto.getPremisesIndexNo();
                        String premVal = appGrpPremisesDto.getPremisesIndexNo();
                        String premType = appGrpPremisesDto.getPremisesType();
                        genSvcPersonDoc(hcsaSvcDocConfigDto, appSvcRelatedInfoDto, docKey, saveFileMap, appSvcDocDtos,
                                newAppSvcDocDtoList, oldDocs, isRfi, appGrpId, appNo, premVal, premType, mulReq, bpc.request,
                                psnTypeNumArr);
                    }
                }
            }

        }
        String crud_action_values = mulReq.getParameter("nextStep");
        if ("next".equals(crud_action_values)) {
            newAppSvcDocDtoList = doValidateSvcDocument(newAppSvcDocDtoList, errorMap, true);
            ApplicationHelper.svcDocMandatoryValidate(svcDocConfigDtos, newAppSvcDocDtoList, appGrpPremisesDtos,
                    appSvcRelatedInfoDto, errorMap);
            saveSvcFileAndSetFileId(newAppSvcDocDtoList, saveFileMap);
        } else if (isGetDataFromPage) {
            newAppSvcDocDtoList = doValidateSvcDocument(newAppSvcDocDtoList, errorMap, true);
            saveSvcFileAndSetFileId(newAppSvcDocDtoList, saveFileMap);
        }
        appSvcRelatedInfoDto.setAppSvcDocDtoLit(newAppSvcDocDtoList);
        setAppSvcRelatedInfoMap(bpc.request, currentSvcId, appSvcRelatedInfoDto, appSubmissionDto);
        boolean isValid = checkAction(errorMap, HcsaConsts.STEP_DOCUMENTS, appSubmissionDto, bpc.request);
        if (!isValid) {
            mulReq.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaConsts.STEP_DOCUMENTS);
        }
        log.debug(StringUtil.changeForLog("the do doDocuments end ...."));
    }


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
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList = configCommService.getHcsaSvcPersonnel(currentSvcId,
                ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL);
        int mandatory = 0;
        if (hcsaSvcPersonnelList != null && !hcsaSvcPersonnelList.isEmpty()) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = hcsaSvcPersonnelList.get(0);
            ParamUtil.setRequestAttr(bpc.request, "spHcsaSvcPersonnelDto", hcsaSvcPersonnelDto);
            if (hcsaSvcPersonnelDto != null) {
                mandatory = hcsaSvcPersonnelDto.getMandatoryCount();
            }
        }
        //reload
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currentSvcId);
        List<AppSvcPersonnelDto> appSvcPersonnelDtos = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
        if (appSvcPersonnelDtos != null && !appSvcPersonnelDtos.isEmpty()) {
            if (appSvcPersonnelDtos.size() > mandatory) {
                mandatory = appSvcPersonnelDtos.size();
            }
            boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
            String appType = appSubmissionDto.getAppType();
            if (isRfi || (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                    appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType))) {
                log.debug(StringUtil.changeForLog("cycle cgo dto to retrieve prs info start ..."));
                if ("Y".equals(ApplicationHelper.getPrsFlag())) {
                    for (int i = 0; i < appSvcPersonnelDtos.size(); i++) {
                        AppSvcPersonnelDto appSvcPersonDto = appSvcPersonnelDtos.get(i);
                        String profRegNo = appSvcPersonDto.getProfRegNo();
                        ProfessionalResponseDto professionalResponseDto = appCommService.retrievePrsInfo(profRegNo);
                        if (professionalResponseDto != null) {
                            String name = appSvcPersonDto.getName();
                            if (!StringUtil.isEmpty(name)) {
                                appSvcPersonDto.setPrsLoading(true);
                            }
                        }
                    }
                }
                log.debug(StringUtil.changeForLog("cycle cgo dto to retrieve prs info end ..."));
            }
            ParamUtil.setRequestAttr(bpc.request, "AppSvcPersonnelDtoList", appSvcPersonnelDtos);
        }
        ParamUtil.setRequestAttr(bpc.request, "ServicePersonnelMandatory", mandatory);
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
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(RfcConst.EDIT_SERVICE,
                bpc.request);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        List<AppSvcPersonnelDto> appSvcPersonnelDtos = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
        if (isGetDataFromPage) {
            List<String> personnelTypeList = IaisCommonUtils.genNewArrayList();
            List<SelectOption> personnelTypeSel = ApplicationHelper.genPersonnelTypeSel(currentSvcCod);
            for (SelectOption sp : personnelTypeSel) {
                personnelTypeList.add(sp.getValue());
            }
            appSvcPersonnelDtos = AppDataHelper.genAppSvcPersonnelDtoList(bpc.request, personnelTypeList, currentSvcCod);
            log.debug(StringUtil.changeForLog("cycle cgo dto to retrieve prs info start ..."));
            log.debug("prs server flag {}", prsFlag);
            if ("Y".equals(prsFlag) && !IaisCommonUtils.isEmpty(appSvcPersonnelDtos)) {
                for (int i = 0; i < appSvcPersonnelDtos.size(); i++) {
                    AppSvcPersonnelDto appSvcPersonDto = appSvcPersonnelDtos.get(i);
                    String profRegNo = appSvcPersonDto.getProfRegNo();
                    ProfessionalResponseDto professionalResponseDto = appCommService.retrievePrsInfo(profRegNo);
                    if (professionalResponseDto != null) {
                        String name = appSvcPersonDto.getName();
                        if (!StringUtil.isEmpty(name)) {
                            appSvcPersonDto.setPrsLoading(true);
                        }
                    }
                }
            }
            log.debug(StringUtil.changeForLog("cycle cgo dto to retrieve prs info end ..."));
            appSvcRelatedInfoDto.setAppSvcPersonnelDtoList(appSvcPersonnelDtos);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, appSvcRelatedInfoDto, appSubmissionDto);
        }

        if (!"back".equals(action)) {
            AppValidatorHelper.doValidateSvcPersonnel(errorMap, appSvcPersonnelDtos, currentSvcCod);
            //validate mandatory count
            int psnLength = 0;
            if (!IaisCommonUtils.isEmpty(appSvcPersonnelDtos)) {
                psnLength = appSvcPersonnelDtos.size();
            }
            List<HcsaSvcPersonnelDto> psnConfig = configCommService.getHcsaSvcPersonnel(currentSvcId,
                    ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL);
            if (!ApplicationHelper.checkIsRfi(bpc.request)) {
                errorMap = AppValidatorHelper.psnMandatoryValidate(psnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL,
                        errorMap, psnLength, "psnMandatory", HcsaConsts.SERVICE_PERSONNEL);
            }
            errorMap = servicePersonPrsValidate(bpc.request, errorMap, appSvcRelatedInfoDto.getAppSvcPersonnelDtoList());
            if (appSubmissionDto.isNeedEditController()) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                //clickEditPages.add(APPLICATION_SVC_PAGE_NAME_SERVICE_PERSONNEL);
                appSubmissionDto.setClickEditPage(clickEditPages);
            }
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        }
        boolean isValid = checkAction(errorMap, HcsaConsts.STEP_SERVICE_PERSONNEL, appSubmissionDto, bpc.request);
        if (isValid && isGetDataFromPage) {
            //remove dirty psn doc info
            removeDirtyPsnDoc(ApplicationConsts.DUP_FOR_PERSON_SVCPSN, bpc.request);
        }
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
//        Map<String, List<HcsaSvcPersonnelDto>> svcConfigInfo =
//                (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(bpc.request, AppCommConst.SERVICEALLPSNCONFIGMAP);
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        //min and max count
        int mandatoryCount = 0;
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList = configCommService.getHcsaSvcPersonnel(currentSvcId,
                ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
        if (hcsaSvcPersonnelList != null && hcsaSvcPersonnelList.size() > 0) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = hcsaSvcPersonnelList.get(0);
            mandatoryCount = hcsaSvcPersonnelDto.getMandatoryCount();
            ParamUtil.setSessionAttr(bpc.request, "mapHcsaSvcPersonnel", hcsaSvcPersonnelDto);
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currentSvcId);
        List<AppSvcPrincipalOfficersDto> medAlertPsnDtos = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
        ParamUtil.setRequestAttr(bpc.request, "mandatoryCount", mandatoryCount);
        List<SelectOption> idTypeSelectList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ID_TYPE);
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.DROPWOWN_IDTYPESELECT, idTypeSelectList);
        List<SelectOption> assignSelectList = ApplicationHelper.genAssignPersonSel(bpc.request, true);
        ParamUtil.setRequestAttr(bpc.request, "MedAlertAssignSelect", assignSelectList);
        ParamUtil.setRequestAttr(bpc.request, "AppSvcMedAlertPsn", medAlertPsnDtos);
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
            List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = AppDataHelper.genKeyPersonnels(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, "", bpc.request);
            currentSvcRelatedDto.setAppSvcMedAlertPersonList(appSvcMedAlertPersonList);
            reSetChangesForApp(appSubmissionDto);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto, appSubmissionDto);
        }
        String svcCode = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSVCCODE);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if ("next".equals(action)) {
            Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,
                    LICPERSONSELECTMAP);
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
        if (isGetDataFromPage && !isRfi) {
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
                appSubmissionDto.getAppSvcRelatedInfoDtoList().stream().forEach(obj -> {
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
            List<AppSvcVehicleDto> oldAppSvcVehicleDto = appCommService.getActiveVehicles(ids);
            new ValidateVehicle().doValidateVehicles(map, appSvcVehicleDtos, currSvcInfoDto.getAppSvcVehicleDtoList(),
                    oldAppSvcVehicleDto);
        }
        checkAction(map, HcsaConsts.STEP_VEHICLES, appSubmissionDto, bpc.request);
        log.debug(StringUtil.changeForLog("doVehicles end ..."));
    }

    /**
     * StartStep: prePareClinicalDirector
     *
     * @param bpc
     * @throws
     */
    public void prePareClinicalDirector(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("prePareClinicalDirector start ..."));

        String currSvcCode = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSVCCODE);
        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currSvcId);
        // Clinical Director config
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList = configCommService.getHcsaSvcPersonnel(currSvcId,
                ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR);
        if (hcsaSvcPersonnelList != null && hcsaSvcPersonnelList.size() > 0) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = hcsaSvcPersonnelList.get(0);
            ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.CLINICALDIRECTORCONFIG, hcsaSvcPersonnelDto);
        }
        List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos = currSvcInfoDto.getAppSvcClinicalDirectorDtoList();
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.CLINICALDIRECTORDTOLIST, appSvcClinicalDirectorDtos);
        List<SelectOption> easMtsSpecialtySelectList = ApplicationHelper.genEasMtsSpecialtySelectList(currSvcCode);
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.EASMTSSPECIALTYSELECTLIST, easMtsSpecialtySelectList);
        // Assgined person dropdown options
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.PERSON_OPTIONS, ApplicationHelper.genAssignPersonSel(bpc.request, true));
        log.debug(StringUtil.changeForLog("prePareClinicalDirector end ..."));
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
        String currSvcId = ApplicationHelper.getCurrentServiceId(bpc.request);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currSvcId);
        String isEdit = ParamUtil.getString(bpc.request, IS_EDIT);
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        String actionType = ParamUtil.getRequestString(bpc.request, "nextStep");
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(appSubmissionDto,
                RfcConst.EDIT_SERVICE, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        Map<String, String> map = new HashMap<>(17);
        if (isGetDataFromPage) {
            //get data from page
            List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos = AppDataHelper.genAppSvcClinicalDirectorDto(bpc.request,
                    appSubmissionDto.getAppType());
            currSvcInfoDto.setAppSvcClinicalDirectorDtoList(appSvcClinicalDirectorDtos);
            log.debug(StringUtil.changeForLog("cycle cd dto to retrieve prs info start ..."));
            log.debug("prs server flag {}", prsFlag);
            String appType = appSubmissionDto.getAppType();
            if ("Y".equals(prsFlag) && !IaisCommonUtils.isEmpty(appSvcClinicalDirectorDtos)) {
                for (int i = 0; i < appSvcClinicalDirectorDtos.size(); i++) {
                    AppSvcPrincipalOfficersDto appSvcPsnDto = appSvcClinicalDirectorDtos.get(i);
                    String profRegNo = appSvcPsnDto.getProfRegNo();
                    ProfessionalResponseDto professionalResponseDto = appCommService.retrievePrsInfo(profRegNo);
                    String specialtyStr = "";
                    String specialtyGetDateStr = "";
                    String typeOfCurrRegi = "";
                    String currRegiDateStr = "";
                    String praCerEndDateStr = "";
                    String typeOfRegister = "";
                    if (professionalResponseDto != null) {
                        boolean needLoadName =
                                !appSvcPsnDto.isLicPerson() && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType);
                        String name = professionalResponseDto.getName();
                        log.info(StringUtil.changeForLog("Need Load Name: " + needLoadName + "; PRS Name: " + name));
                        if (professionalResponseDto.isHasException() || StringUtil.isNotEmpty(
                                professionalResponseDto.getStatusCode())) {
                            if (StringUtil.isEmpty(actionType) || "next".equals(actionType)) {
                                if (professionalResponseDto.isHasException()) {
                                    bpc.request.setAttribute(PRS_SERVICE_DOWN, PRS_SERVICE_DOWN);
                                    map.put(PRS_SERVICE_DOWN, PRS_SERVICE_DOWN);
                                } else if ("401".equals(professionalResponseDto.getStatusCode())) {
                                    map.put("profRegNo" + i, "GENERAL_ERR0054");
                                } else {
                                    map.put("profRegNo" + i, "GENERAL_ERR0042");
                                }
                            }
                            setClinicalDirectorPrsInfo(appSvcPsnDto, specialtyStr, specialtyGetDateStr, typeOfCurrRegi,
                                    currRegiDateStr, praCerEndDateStr, typeOfRegister);
                            continue;
                        }
                        if (needLoadName) {
                            appSvcPsnDto.setName(name);
                        }
                        //retrieve data from prs server
                        List<String> specialtyList = professionalResponseDto.getSpecialty();
                        if (!IaisCommonUtils.isEmpty(specialtyList)) {
                            List<String> notNullList = IaisCommonUtils.genNewArrayList();
                            for (String value : specialtyList) {
                                if (!StringUtil.isEmpty(value)) {
                                    notNullList.add(value);
                                }
                            }
                            specialtyStr = String.join(",", notNullList);
                        }
                        List<String> entryDateSpecialist = professionalResponseDto.getEntryDateSpecialist();
                        if (entryDateSpecialist != null && entryDateSpecialist.size() > 0) {
                            specialtyGetDateStr = entryDateSpecialist.get(0);
                        }
                        List<RegistrationDto> registrationDtos = professionalResponseDto.getRegistration();
                        if (registrationDtos != null && registrationDtos.size() > 0) {
                            RegistrationDto registrationDto = registrationDtos.get(0);
                            typeOfCurrRegi = registrationDto.getRegistrationType();
                            currRegiDateStr = registrationDto.getRegStartDate();
                            praCerEndDateStr = registrationDto.getPcEndDate();
                            typeOfRegister = registrationDto.getRegisterType();
                        }
                        setClinicalDirectorPrsInfo(appSvcPsnDto, specialtyStr, specialtyGetDateStr, typeOfCurrRegi, currRegiDateStr,
                                praCerEndDateStr, typeOfRegister);
                    } else if (!StringUtil.isEmpty(profRegNo)) {
                        setClinicalDirectorPrsInfo(appSvcPsnDto, specialtyStr, specialtyGetDateStr, typeOfCurrRegi, currRegiDateStr,
                                praCerEndDateStr, typeOfRegister);
                    }
                }
                currSvcInfoDto.setAppSvcClinicalDirectorDtoList(appSvcClinicalDirectorDtos);
            }
            setAppSvcRelatedInfoMap(bpc.request, currSvcId, currSvcInfoDto, appSubmissionDto);
            log.debug(StringUtil.changeForLog("cycle cd dto to retrieve prs info end ..."));
        }
        String currSvcCode = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSVCCODE);
        if ("next".equals(actionType)) {
            Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(
                    bpc.request,
                    LICPERSONSELECTMAP);
            new ValidateClincalDirector().doValidateClincalDirector(map, currSvcInfoDto.getAppSvcClinicalDirectorDtoList(),
                    licPersonMap,
                    currSvcCode);
        }
        boolean isValid = checkAction(map, HcsaConsts.STEP_CLINICAL_DIRECTOR, appSubmissionDto, bpc.request);
        if (isValid && isGetDataFromPage) {
            syncDropDownAndPsn(appSubmissionDto, currSvcInfoDto.getAppSvcClinicalDirectorDtoList(), null, bpc.request);
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

        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(bpc.request, currSvcId);
        Map<String, AppSvcBusinessDto> premAlignBusinessMap = IaisCommonUtils.genNewHashMap();
        List<AppSvcBusinessDto> appSvcBusinessDtos = currSvcInfoDto.getAppSvcBusinessDtoList();
        if (!IaisCommonUtils.isEmpty(appSvcBusinessDtos)) {
            for (AppSvcBusinessDto appSvcBusinessDto : appSvcBusinessDtos) {
                premAlignBusinessMap.put(appSvcBusinessDto.getPremIndexNo(), appSvcBusinessDto);
            }
        }
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        ParamUtil.setRequestAttr(bpc.request, "isRfi", isRfi);
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.PREMALIGNBUSINESSMAP, premAlignBusinessMap);

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
            AppValidatorHelper.doValidateBusiness(appSvcBusinessDtos, appSubmissionDto.getAppType(),
                    appSubmissionDto.getLicenceId(), errorMap);
        }
        checkAction(errorMap, HcsaConsts.STEP_BUSINESS_NAME, appSubmissionDto, bpc.request);
        log.debug(StringUtil.changeForLog("do Business end ..."));
    }

    //=============================================================================
    //private method
    //=============================================================================

    private ServiceStepDto getServiceStepDto(ServiceStepDto serviceStepDto, String action, List<HcsaServiceDto> hcsaServiceDtoList,
            String svcId) {
        //get the service information
        int serviceNum = -1;
        if (svcId != null && hcsaServiceDtoList != null && hcsaServiceDtoList.size() > 0) {
            for (int i = 0; i < hcsaServiceDtoList.size(); i++) {
                if (svcId.equals(hcsaServiceDtoList.get(i).getId())) {
                    serviceNum = i;
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
                String[] skipList = new String[]{HcsaConsts.STEP_LABORATORY_DISCIPLINES,
                        HcsaConsts.STEP_DISCIPLINE_ALLOCATION, HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS,HcsaConsts.STEP_SECTION_LEADER,HcsaConsts.STEP_PRINCIPAL_OFFICERS};
                for (int i = 0; i < hcsaServiceStepSchemeDtos.size(); i++) {
                    if (action.equals(hcsaServiceStepSchemeDtos.get(i).getStepCode())) {
                        number = i;
                        boolean toNext = currentNumber < i;
                        while (StringUtil.isIn(hcsaServiceStepSchemeDtos.get(i++).getStepCode(), skipList)) {
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
            if (number + 1 == hcsaServiceStepSchemeDtos.size()) {
                stepEnd = true;
            }
            serviceStepDto.setStepFirst(stepFirst);
            serviceStepDto.setStepEnd(stepEnd);
            if (number != -1) {
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

    public static AppSubmissionDto getAppSubmissionDto(HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
        if (appSubmissionDto == null) {
            appSubmissionDto = new AppSubmissionDto();
        }
        return appSubmissionDto;
    }

    private List<AppSvcDocDto> doValidateSvcDocument(List<AppSvcDocDto> appSvcDocDtoList, Map<String, String> errorMap,
            boolean setIsPassValidate) {
        if (!IaisCommonUtils.isEmpty(appSvcDocDtoList)) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtoList) {
                Integer docSize = appSvcDocDto.getDocSize();
                String docName = appSvcDocDto.getDocName();
                String id = appSvcDocDto.getSvcDocId();
                int uploadFileLimit = systemParamConfig.getUploadFileLimit();
                String premVal = appSvcDocDto.getPremisesVal();
                String premType = appSvcDocDto.getPremisesVal();
                String premKey = "";
                if (StringUtil.isEmpty(premVal) && StringUtil.isEmpty(premType)) {
                    premKey = appSvcDocDto.getSvcDocId();
                } else if (!StringUtil.isEmpty(premVal) && !StringUtil.isEmpty(premType)) {
                    premKey = "prem" + appSvcDocDto.getSvcDocId() + premVal;
                }
                if (docSize / 1024 > uploadFileLimit) {
                    String err19 = MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(uploadFileLimit), "sizeMax");
                    if (StringUtil.isEmpty(premVal)) {
                        errorMap.put(id + "selectedFile", err19);
                    } else {
                        errorMap.put(premKey, err19);
                    }
                }
                if (docName.length() > 100) {
                    String generalErr22 = MessageUtil.getMessageDesc("GENERAL_ERR0022");
                    if (StringUtil.isEmpty(premVal)) {
                        errorMap.put(id + "selectedFile", generalErr22);
                    } else {
                        errorMap.put(premKey, generalErr22);
                    }
                }
                Boolean flag = Boolean.FALSE;
                String substring = docName.substring(docName.lastIndexOf('.') + 1);
                String sysFileType = systemParamConfig.getUploadFileType();
                String[] sysFileTypeArr = FileUtils.fileTypeToArray(sysFileType);
                for (String f : sysFileTypeArr) {
                    if (f.equalsIgnoreCase(substring)) {
                        flag = Boolean.TRUE;
                    }
                }
                if (!flag) {
                    String err18 = MessageUtil.replaceMessage("GENERAL_ERR0018", sysFileType, "fileType");
                    if (StringUtil.isEmpty(premVal)) {
                        errorMap.put(id + "selectedFile", err18);
                    } else {
                        errorMap.put(premKey, err18);
                    }
                }
                String errMsg = errorMap.get(id + "selectedFile");
                String errMsg2 = errorMap.get(premKey);
                if (StringUtil.isEmpty(errMsg) && StringUtil.isEmpty(errMsg2) && setIsPassValidate) {
                    appSvcDocDto.setPassValidate(true);
                }
            }
        }
        return appSvcDocDtoList;
    }

    private void setAppSvcRelatedInfoMap(HttpServletRequest request, String currentSvcId, AppSvcRelatedInfoDto currSvcInfoDto) {
        setAppSvcRelatedInfoMap(request, currentSvcId, currSvcInfoDto, null);
    }

    private void setAppSvcRelatedInfoMap(HttpServletRequest request, String currentSvcId, AppSvcRelatedInfoDto currSvcInfoDto,
            AppSubmissionDto appSubmissionDto) {
        if (appSubmissionDto == null) {
            appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
        }
        if (appSubmissionDto == null) {
            return;
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
        ApplicationHelper.doSortSelOption(designation);
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
            List<AppSvcPrincipalOfficersDto> personList,
            String svcCode, HttpServletRequest request) {
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
        ParamUtil.setSessionAttr(request, APPSUBMISSIONDTO, appSubmissionDto);
        //remove dirty psn doc info
        String dupForPerson = null;
        String personType = personList.get(0).getPsnType();
        if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(personType)) {
            dupForPerson = ApplicationConsts.DUP_FOR_PERSON_CGO;
        } else if (ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR.equals(personType)) {
            dupForPerson = ApplicationConsts.DUP_FOR_PERSON_CD;
        } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(personType)) {
            dupForPerson = ApplicationConsts.DUP_FOR_PERSON_MAP;
        } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(personType)) {
            dupForPerson = ApplicationConsts.DUP_FOR_PERSON_PO;
        } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(personType)) {
            dupForPerson = ApplicationConsts.DUP_FOR_PERSON_DPO;
        }
        removeDirtyPsnDoc(dupForPerson, request);

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
        personMap = ApplicationHelper.initSetPsnIntoSelMap(personMap, newPersonList, svcCode);
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
                    List<AppSvcPrincipalOfficersDto> appSvcPoDpoDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                    if (!IaisCommonUtils.isEmpty(appSvcPoDpoDtos)) {
                        for (AppSvcPrincipalOfficersDto psn : appSvcPoDpoDtos) {
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

    private Map<String, AppSvcPersonAndExtDto> removeDirtyDataFromPsnDropDown(AppSubmissionDto appSubmissionDto,
            Map<String, AppSvcPersonAndExtDto> licPersonMap, Map<String, AppSvcPersonAndExtDto> personMap) {
        //add new person key
        Set<String> personKeySet = new HashSet<>(licPersonMap.keySet());
        Set<String> newPersonKeySet = getNewPersonKeySet(appSubmissionDto);
        personKeySet.addAll(newPersonKeySet);
        //filter removed person
        Map<String, AppSvcPersonAndExtDto> newPersonMap = IaisCommonUtils.genNewHashMap();
        Set<String> finalPersonKeySet = personKeySet;
        personMap.forEach((k, v) -> {
            if (finalPersonKeySet.contains(k)) {
                newPersonMap.put(k, v);
            }
        });
        return newPersonMap;
    }

    private void setPsnKeySet(AppSvcPrincipalOfficersDto psn, Set<String> personKeySet) {
        if (psn == null || personKeySet == null) {
            return;
        }
        String assignSel = psn.getAssignSelect();
        if (!psn.isLicPerson()) {
            boolean partValidate = AppValidatorHelper.psnDoPartValidate(psn.getIdType(), psn.getIdNo(), psn.getName());
            if (partValidate) {
                personKeySet.add(ApplicationHelper.getPersonKey(psn.getNationality(), psn.getIdType(), psn.getIdNo()));
            } else if (!StringUtil.isEmpty(assignSel) && !"-1".equals(assignSel)) {
                psn.setAssignSelect(HcsaAppConst.NEW_PSN);
            }
        } else {
            personKeySet.add(ApplicationHelper.getPersonKey(psn.getNationality(), psn.getIdType(), psn.getIdNo()));
        }
    }

    private void genSvcDoc(Map<String, File> fileMap, String docKey, HcsaSvcDocConfigDto hcsaSvcDocConfigDto,
            Map<String, File> saveFileMap,
            List<AppSvcDocDto> currSvcDocDtoList, List<AppSvcDocDto> newAppSvcDocDtos, List<AppSvcDocDto> oldDocs, String premVal,
            String premType, String psnIndexNo, String dupForPrem, String dupForPerson, boolean isRfi, String appGrpId, String appNo,
            String psnId) {
        if (fileMap != null) {
            fileMap.forEach((k, v) -> {
                int index = k.indexOf(docKey);
                String seqNumStr = k.substring(index + docKey.length());
                int seqNum = -1;
                try {
                    seqNum = Integer.parseInt(seqNumStr);
                } catch (Exception e) {
                    log.error(StringUtil.changeForLog("doc seq num can not parse to int"));
                }
                AppSvcDocDto appSvcDocDto = new AppSvcDocDto();
                if (v != null) {
                    appSvcDocDto.setSvcDocId(hcsaSvcDocConfigDto.getId());
                    appSvcDocDto.setUpFileName(hcsaSvcDocConfigDto.getDocTitle());
                    appSvcDocDto.setDocName(v.getName());
                    long size = v.length() / 1024;
                    appSvcDocDto.setDocSize(Integer.valueOf(String.valueOf(size)));
                    String md5Code = FileUtils.getFileMd5(v);
                    appSvcDocDto.setMd5Code(md5Code);
                    //if  common ==> set null
                    appSvcDocDto.setPremisesVal(premVal);
                    appSvcDocDto.setPremisesType(premType);
                    appSvcDocDto.setPsnIndexNo(psnIndexNo);
                    appSvcDocDto.setSeqNum(seqNum);
                    appSvcDocDto.setDupForPrem(dupForPrem);
                    appSvcDocDto.setDupForPerson(dupForPerson);
                    appSvcDocDto.setPersonType(ApplicationHelper.getPsnType(dupForPerson));
                    setAppSvcDocDtoFileds(appSvcDocDto, oldDocs, hcsaSvcDocConfigDto.getId(), isRfi, appGrpId, appNo, psnId);
                    saveFileMap.put(premVal + hcsaSvcDocConfigDto.getId() + psnIndexNo + seqNum, v);
                } else {
                    appSvcDocDto = getAppSvcDocByConfigIdAndSeqNum(currSvcDocDtoList, hcsaSvcDocConfigDto.getId(), seqNum, premVal,
                            premType, psnIndexNo);
                }
                //the data is retrieved from the DTO a second time
                fileMap.put(k, null);
                if (appSvcDocDto != null) {
                    newAppSvcDocDtos.add(appSvcDocDto);
                }
            });
        }
    }

    private void setAppSvcDocDtoFileds(AppSvcDocDto appSvcDocDto, List<AppSvcDocDto> oldDocs, String configDocId, boolean isRfi, String appGrpId, String appNo, String psnId) {
        String md5Code = appSvcDocDto.getMd5Code();
        String dupForPrem = appSvcDocDto.getDupForPrem();
        String dupForPerson = appSvcDocDto.getDupForPerson();
        int seqNum = appSvcDocDto.getSeqNum();

        Integer version = 1;
        if (StringUtil.isEmpty(configDocId) || IaisCommonUtils.isEmpty(oldDocs) || StringUtil.isEmpty(md5Code)) {
            appSvcDocDto.setVersion(version);
            appSvcDocDto.setSubmitDt(new Date());
            appSvcDocDto.setSubmitBy(ApplicationHelper.getLoginContext().getUserId());
            return;
        }
        if (isRfi) {
            boolean canFound = false;
            log.info(StringUtil.changeForLog("rfi appNo:" + appNo));
            for (AppSvcDocDto dto : oldDocs) {
                Integer oldVersion = dto.getVersion();
                if (configDocId.equals(dto.getSvcDocId()) && seqNum == dto.getSeqNum()) {
                    canFound = true;
                    if (MessageDigest.isEqual(md5Code.getBytes(StandardCharsets.UTF_8),
                            dto.getMd5Code().getBytes(StandardCharsets.UTF_8))) {
                        if (!StringUtil.isEmpty(oldVersion)) {
                            version = oldVersion;
                        }
                        //appSvcDocDto.setSubmitDt(dto.getSubmitDt());
                        //appSvcDocDto.setSubmitBy(dto.getSubmitBy());
                    } else {
                        version = getVersion(appGrpId, configDocId, appNo, seqNum, dupForPrem, dupForPerson, psnId);
                    }
                    break;
                }
            }
            if (!canFound) {
                //last doc is null
                version = getVersion(appGrpId, configDocId, appNo, seqNum, dupForPrem, dupForPerson, psnId);
            }
        }
        if (appSvcDocDto.getSubmitDt() == null) {
            appSvcDocDto.setSubmitDt(new Date());
        }
        if (StringUtil.isEmpty(appSvcDocDto.getSubmitBy())) {
            appSvcDocDto.setSubmitBy(ApplicationHelper.getLoginContext().getUserId());
        }
        appSvcDocDto.setVersion(version);
    }

    private Integer getVersion(String appGrpId, String configDocId, String appNo, Integer seqNum, String dupForPrem,
            String dupForPerson, String psnId) {
        Integer version = 1;
        AppSvcDocDto searchDto = new AppSvcDocDto();
        searchDto.setAppGrpId(appGrpId);
        searchDto.setSvcDocId(configDocId);
        searchDto.setSeqNum(seqNum);
        if ("0".equals(dupForPrem)) {
            version = getMaxVersion(dupForPerson, searchDto, appNo, psnId, version);
        } else if ("1".equals(dupForPrem)) {
            version = getMaxVersion(dupForPerson, searchDto, appNo, psnId, version);
        }
        return version;
    }

    private static AppSvcDocDto getAppSvcDocByConfigIdAndSeqNum(List<AppSvcDocDto> appSvcDocDtos, String configId, int seqNum,
            String premVal, String premType, String psnIndexNo) {
        log.debug(StringUtil.changeForLog("getAppGrpPrimaryDocByConfigIdAndSeqNum start..."));
        AppSvcDocDto appSvcDocDto = null;
        if (!IaisCommonUtils.isEmpty(appSvcDocDtos)) {
            log.debug("configId is {}", configId);
            log.debug("seqNum is {}", seqNum);
            log.debug("premIndex is {}", premVal);
            log.debug("psnIndexNo is {}", psnIndexNo);
            for (AppSvcDocDto appSvcDocDto1 : appSvcDocDtos) {
                String currPremVal = "";
                String currPremType = "";
                String currPsnIndexNo = "";
                if (!StringUtil.isEmpty(appSvcDocDto1.getPremisesVal())) {
                    currPremVal = appSvcDocDto1.getPremisesVal();
                }
                if (!StringUtil.isEmpty(appSvcDocDto1.getPremisesType())) {
                    currPremType = appSvcDocDto1.getPremisesType();
                }
                if (!StringUtil.isEmpty(appSvcDocDto1.getPsnIndexNo())) {
                    currPsnIndexNo = appSvcDocDto1.getPsnIndexNo();
                }
                if (configId.equals(appSvcDocDto1.getSvcDocId())
                        && seqNum == appSvcDocDto1.getSeqNum()
                        && premVal.equals(currPremVal)
                        && premType.equals(currPremType)
                        && psnIndexNo.equals(currPsnIndexNo)) {
                    try {
                        appSvcDocDto = CopyUtil.copyMutableObject(appSvcDocDto1);
                    } catch (Exception e) {
                        log.error(StringUtil.changeForLog("copy appSvcDocDto error !!!"));
                    }
                    break;
                }
            }
        }
        return appSvcDocDto;
    }

    private void saveSvcFileAndSetFileId(List<AppSvcDocDto> appSvcDocDtos, Map<String, File> saveFileMap) {
        if (IaisCommonUtils.isEmpty(saveFileMap) || appSvcDocDtos == null) {
            return;
        }
        Map<String, File> passValidateFileMap = IaisCommonUtils.genNewLinkedHashMap();
        for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
            if (appSvcDocDto.isPassValidate()) {
                String premIndexNo = "";
                if (!StringUtil.isEmpty(appSvcDocDto.getPremisesVal())) {
                    premIndexNo = appSvcDocDto.getPremisesVal();
                }
                String psnIndexNo = "";
                if (!StringUtil.isEmpty(appSvcDocDto.getPsnIndexNo())) {
                    psnIndexNo = appSvcDocDto.getPsnIndexNo();
                }
                String fileMapKey = premIndexNo + appSvcDocDto.getSvcDocId() + psnIndexNo + appSvcDocDto.getSeqNum();
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
                String premIndexNo = "";
                if (!StringUtil.isEmpty(appSvcDocDto.getPremisesVal())) {
                    premIndexNo = appSvcDocDto.getPremisesVal();
                }
                String psnIndexNo = "";
                if (!StringUtil.isEmpty(appSvcDocDto.getPsnIndexNo())) {
                    psnIndexNo = appSvcDocDto.getPsnIndexNo();
                }
                String saveFileMapKey = premIndexNo + appSvcDocDto.getSvcDocId() + psnIndexNo + appSvcDocDto.getSeqNum();
                File file = saveFileMap.get(saveFileMapKey);
                if (file != null) {
                    appSvcDocDto.setFileRepoId(fileRepoIdList.get(i));
                    i++;
                }
            }
        }
    }

    private void genSvcPersonDoc(HcsaSvcDocConfigDto hcsaSvcDocConfigDto, AppSvcRelatedInfoDto appSvcRelatedInfoDto, String docKey,
            Map<String, File> saveFileMap,
            List<AppSvcDocDto> currSvcDocDtoList, List<AppSvcDocDto> newAppSvcDocDtos, List<AppSvcDocDto> oldDocs,
            boolean isRfi, String appGrpId, String appNo, String premVal, String premType, MultipartHttpServletRequest mulReq,
            HttpServletRequest request, int[] psnTypeNumArr) {
        String dupForPerson = hcsaSvcDocConfigDto.getDupForPerson();
        String configId = hcsaSvcDocConfigDto.getId();
        if (StringUtil.isEmpty(dupForPerson)) {
            Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(mulReq,
                    IaisEGPConstant.SEESION_FILES_MAP_AJAX + docKey);
            genSvcDoc(fileMap, docKey, hcsaSvcDocConfigDto, saveFileMap, currSvcDocDtoList, newAppSvcDocDtos, oldDocs, premVal,
                    premType, "", hcsaSvcDocConfigDto.getDupForPrem(), "", isRfi, appGrpId, appNo, "");
            ParamUtil.setSessionAttr(request, IaisEGPConstant.SEESION_FILES_MAP_AJAX + docKey, (Serializable) fileMap);
            //get target doc list
            List<AppSvcDocDto> newTargetDocDtoList = ApplicationHelper.getSvcDocumentByParams(newAppSvcDocDtos, configId, premVal, "");
            List<AppSvcDocDto> oldTargetDocDtoList = ApplicationHelper.getSvcDocumentByParams(oldDocs, configId, premVal, "");
            //set value for be doc sort
            setValueForBeDocSort(newTargetDocDtoList, oldTargetDocDtoList, psnTypeNumArr, ApplicationHelper.getPsnType(dupForPerson));
        } else {
            //genDupForPersonDoc(hcsaSvcDocConfigDto,appSvcRelatedInfoDto,docKey,saveFileMap,appSvcDocDtos,newAppSvcDocDtoList,oldDocs,isRfi,appGrpId,appNo,mulReq,bpc.request);
            List<AppSvcPrincipalOfficersDto> psnDtoList = ApplicationHelper.getPsnByDupForPerson(appSvcRelatedInfoDto,
                    hcsaSvcDocConfigDto.getDupForPerson());
            for (AppSvcPrincipalOfficersDto psnDto : psnDtoList) {
                String psnIndexNo = psnDto.getIndexNo();
                String psnDocKey = docKey + psnIndexNo;
                Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(mulReq,
                        IaisEGPConstant.SEESION_FILES_MAP_AJAX + psnDocKey);
                genSvcDoc(fileMap, psnDocKey, hcsaSvcDocConfigDto, saveFileMap, currSvcDocDtoList, newAppSvcDocDtos, oldDocs, premVal,
                        premType, psnIndexNo, hcsaSvcDocConfigDto.getDupForPrem(), hcsaSvcDocConfigDto.getDupForPerson(), isRfi,
                        appGrpId, appNo, psnDto.getCurPersonelId());
                ParamUtil.setSessionAttr(request, IaisEGPConstant.SEESION_FILES_MAP_AJAX + psnDocKey, (Serializable) fileMap);
                //get target doc list
                List<AppSvcDocDto> newTargetDocDtoList = ApplicationHelper.getSvcDocumentByParams(newAppSvcDocDtos, configId, premVal,
                        psnIndexNo);
                List<AppSvcDocDto> oldTargetDocDtoList = ApplicationHelper.getSvcDocumentByParams(oldDocs, configId, premVal,
                        psnIndexNo);
                //set value for be doc sort
                setValueForBeDocSort(newTargetDocDtoList, oldTargetDocDtoList, psnTypeNumArr,
                        ApplicationHelper.getPsnType(dupForPerson));
            }
        }
    }

    private Integer getMaxVersion(String dupForPerson, AppSvcDocDto searchDto, String appNo, String psnId, Integer version) {
        AppSvcDocDto maxVersionDocDto;
        if (StringUtil.isEmpty(dupForPerson)) {
            maxVersionDocDto = appCommService.getMaxVersionSvcSpecDoc(searchDto, appNo);
        } else if (ApplicationConsts.DUP_FOR_PERSON_SVCPSN.equals(dupForPerson)) {
            searchDto.setAppSvcPersonId(psnId);
            maxVersionDocDto = appCommService.getMaxVersionSvcSpecDoc(searchDto, appNo);
        } else {
            searchDto.setAppGrpPersonId(psnId);
            maxVersionDocDto = appCommService.getMaxVersionSvcSpecDoc(searchDto, appNo);
        }
        if (!StringUtil.isEmpty(maxVersionDocDto.getVersion())) {
            //judege dto is null
            if (!StringUtil.isEmpty(maxVersionDocDto.getFileRepoId())) {
                version = maxVersionDocDto.getVersion() + 1;
            }
        }
        return version;
    }

    private int getMaxPersonTypeNumber(List<AppSvcDocDto> newAppSvcDocDtos, List<AppSvcDocDto> oldAppSvcDocDtos) {
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
    }

    private void setValueForBeDocSort(List<AppSvcDocDto> newAppSvcDocDtos, List<AppSvcDocDto> oldAppSvcDocDtos, int[] psnTypeNumArr,
            String personType) {
        //retrieve person type number
        Integer newPsnTypeNum = null;
        if (!IaisCommonUtils.isEmpty(newAppSvcDocDtos)) {
            for (AppSvcDocDto appSvcDocDto : newAppSvcDocDtos) {
                if (appSvcDocDto.getPersonTypeNum() != null) {
                    newPsnTypeNum = appSvcDocDto.getPersonTypeNum();
                    break;
                }
            }
        }
        Integer oldPsnTypeNum = null;
        if (newPsnTypeNum == null && !IaisCommonUtils.isEmpty(oldAppSvcDocDtos)) {
            //todo:change
            for (AppSvcDocDto oldAppSvcDocDto : oldAppSvcDocDtos) {
                if (oldAppSvcDocDto.getPersonTypeNum() != null) {
                    oldPsnTypeNum = oldAppSvcDocDto.getPersonTypeNum();
                    break;
                }
            }
        }
        int maxPsnTypeNum = psnTypeNumArr[0];
        int currPsnTypeNum = 0;
        if (newPsnTypeNum == null && oldPsnTypeNum == null) {
            currPsnTypeNum = maxPsnTypeNum + 1;
        } else if (newPsnTypeNum != null) {
            currPsnTypeNum = newPsnTypeNum;
        } else if (oldPsnTypeNum != null) {
            currPsnTypeNum = oldPsnTypeNum;
        }

        //insert person type,person type number
        int psnTypeNumFinal = currPsnTypeNum;
        newAppSvcDocDtos.forEach(svcDoc -> {
            svcDoc.setPersonType(personType);
            svcDoc.setPersonTypeNum(psnTypeNumFinal);
        });

        //psnTypeNum increase
        if (currPsnTypeNum > maxPsnTypeNum) {
            psnTypeNumArr[0] = currPsnTypeNum;
        }
    }

    private List<HcsaSvcDocConfigDto> getDocConfigDtoByDupForPerson(List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos,
            String dupForPerson) {
        List<HcsaSvcDocConfigDto> result = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(hcsaSvcDocConfigDtos) && !StringUtil.isEmpty(dupForPerson)) {
            for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : hcsaSvcDocConfigDtos) {
                if (dupForPerson.equals(hcsaSvcDocConfigDto.getDupForPerson())) {
                    result.add(hcsaSvcDocConfigDto);
                }
            }
        }
        return result;
    }

    private void removeDirtyPsnDoc(final String dupForPerson, HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("remove dirty psn doc info start ..."));
        if (StringUtil.isEmpty(dupForPerson)) {
            return;
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CompletableFuture.runAsync(() -> {
            String currentSvcId = ApplicationHelper.getCurrentServiceId(request);
            AppSvcRelatedInfoDto currentSvcRelatedDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
            List<HcsaSvcDocConfigDto> svcDocConfigDtos = configCommService.getAllHcsaSvcDocs(currentSvcId);
            List<AppSvcPrincipalOfficersDto> psnDtoList = ApplicationHelper.getPsnByDupForPerson(currentSvcRelatedDto,
                    dupForPerson);
            List<AppSvcDocDto> appSvcDocDtoList = currentSvcRelatedDto.getAppSvcDocDtoLit();
            List<HcsaSvcDocConfigDto> targetConfigDtos = getDocConfigDtoByDupForPerson(svcDocConfigDtos, dupForPerson);
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
                if (newAppSvcDocDtoList.size() != appSvcDocDtoList.size()) {
                    synchronized (this) {
                        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
                        currentSvcRelatedDto = ApplicationHelper.getAppSvcRelatedInfo(appSubmissionDto, currentSvcId, null);
                        currentSvcRelatedDto.setAppSvcDocDtoLit(newAppSvcDocDtoList);
                        setAppSvcRelatedInfoMap(request, currentSvcId, currentSvcRelatedDto, appSubmissionDto);
                    }
                }
            }
            countDownLatch.countDown();
        });
        if (ApplicationConsts.DUP_FOR_PERSON_MAP.equals(dupForPerson)) {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                log.error(StringUtil.changeForLog(e.getMessage()), e);
                Thread.currentThread().interrupt();
            }
        }
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

}
