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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPsnEditDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.RegistrationDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.NewApplicationConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.ServiceStepDto;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.utils.SingeFileUtil;
import com.ecquaria.cloud.moh.iais.validate.serviceInfo.ValidateCharges;
import com.ecquaria.cloud.moh.iais.validate.serviceInfo.ValidateClincalDirector;
import com.ecquaria.cloud.moh.iais.validate.serviceInfo.ValidateVehicle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


/**
 * ClinicalLaboratoryDelegator
 *
 * @author suocheng
 * @date 10/11/2019
 */
@Delegator("clinicalLaboratoryDelegator")

@Slf4j
public class ClinicalLaboratoryDelegator {

    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    WithOutRenewalService outRenewalService;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private ValidateVehicle validateVehicle;
    @Autowired
    private ValidateClincalDirector validateClincalDirector;
    @Autowired
    private ValidateCharges validateCharges;
    @Autowired
    private FeEicGatewayClient feEicGatewayClient;
    @Autowired
    private LicenceClient licenceClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Value("${moh.halp.prs.enable}")
    private String prsFlag;

    public static final String GOVERNANCEOFFICERS = "GovernanceOfficers";
    public static final String GOVERNANCEOFFICERSDTO = "GovernanceOfficersDto";
    public static final String GOVERNANCEOFFICERSDTOLIST = "GovernanceOfficersList";
    public static final String APPSVCRELATEDINFODTO = "AppSvcRelatedInfoDto";
    public static final String ERRORMAP_GOVERNANCEOFFICERS = "errorMap_governanceOfficers";
    public static final String RELOADSVCDOC = "ReloadSvcDoc";
    public static final String SERVICEPERSONNELTYPE = "ServicePersonnelType";
    public static final String VEHICLEDTOLIST = "vehicleDtoList";
    public static final String VEHICLECONFIGDTO = "vehicleConfigDto";
    public static final String CLINICALDIRECTORDTOLIST = "clinicalDirectorDtoList";
    public static final String CLINICALDIRECTORCONFIG = "clinicalDirectorConfig";
    public static final String EASMTSSPECIALTYSELECTLIST = "easMtsSpecialtySelectList";
    public static final String EASMTSDESIGNATIONSELECTLIST = "easMtsDesignationSelectList";
    public static final String GENERALCHARGESDTOLIST = "generalChargesDtoList";
    public static final String GENERALCHARGESCONFIG = "generalChargesConfig";
    public static final String OTHERCHARGESDTOLIST = "otherChargesDtoList";
    public static final String OTHERCHARGESCONFIG = "otherChargesConfig";
    public static final String PREMALIGNBUSINESSMAP = "premAlignBusinessMap";

    //dropdown
    public static final String DROPWOWN_IDTYPESELECT = "IdTypeSelect";

    private static final String CURR_STEP_NAME = "currStepName";
    private static final String PAGE_NAME_PO = "pageNamePo";
    private static final String PAGE_NAME_MAP = "pageNameMap";

    public static final String PERSON_OPTIONS = "PERSON_OPTIONS";

    public static final String SECTION_LEADER_LIST = "sectionLeaderList";

    public static final String PRS_SERVICE_DOWN = "PRS_SERVICE_DOWN";

    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doStart start ...."));

        //svc
        ParamUtil.setSessionAttr(bpc.request, GOVERNANCEOFFICERSDTOLIST, null);
        ParamUtil.setSessionAttr(bpc.request, ERRORMAP_GOVERNANCEOFFICERS, null);
        ParamUtil.setSessionAttr(bpc.request, RELOADSVCDOC, null);
        //ParamUtil.setSessionAttr(bpc.request, SERVICEPERSONNELCONFIG, null);

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
        ServiceStepDto serviceStepDto = (ServiceStepDto) ParamUtil.getSessionAttr(bpc.request, ShowServiceFormsDelegator.SERVICESTEPDTO);
        String svcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, svcId);
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
        serviceStepDto = getServiceStepDto(serviceStepDto, action, hcsaServiceDtoList, svcId, appSvcRelatedInfoDto);
        //reset value
        if (HcsaLicenceFeConstant.DISCIPLINEALLOCATION.equals(action)) {
            action = serviceStepDto.getCurrentStep().getStepCode();
        }
        ParamUtil.setSessionAttr(bpc.request, ShowServiceFormsDelegator.SERVICESTEPDTO, serviceStepDto);

        if (StringUtil.isEmpty(action) || IaisEGPConstant.YES.equals(formTab)) {
            if (serviceStepDto.getCurrentStep() != null) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, serviceStepDto.getCurrentStep().getStepCode());
            } else {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.LABORATORYDISCIPLINES);
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
        } else if (HcsaConsts.STEP_LABORATORY_DISCIPLINES.equals(currentStep)) {
            singleName = HcsaConsts.BUSINESS_NAME;
            prepareLaboratoryDisciplines(bpc);
        } else if (HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(currentStep)) {
            singleName = HcsaConsts.CLINICAL_GOVERNANCE_OFFICER;
            prepareGovernanceOfficers(bpc);
        } else if (HcsaConsts.STEP_SECTION_LEADER.equals(currentStep)) {
            // Section Leader
            singleName = HcsaConsts.SECTION_LEADER;
            prepareSectionLeader(bpc);
        } else if (HcsaConsts.STEP_DISCIPLINE_ALLOCATION.equals(currentStep)) {
            singleName = HcsaConsts.BUSINESS_NAME;
            prepareDisciplineAllocation(bpc);
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

        ParamUtil.setRequestAttr(bpc.request, "singleName", singleName);
        ParamUtil.setRequestAttr(bpc.request, CURR_STEP_NAME, currentStepName);
        ParamUtil.setRequestAttr(bpc.request, "currentStep", currentStep);
        AppSvcRelatedInfoDto currSvcInfoDto = getAppSvcRelatedInfo(bpc.request);
        ParamUtil.setRequestAttr(bpc.request, "currSvcInfoDto", currSvcInfoDto);
        log.info(StringUtil.changeForLog("--- Prepare " + currentStepName + " End ---"));
    }

    /**
     * Process: MohServiceRelatedInformation
     * Step: DoStep
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
        } else if (HcsaConsts.STEP_LABORATORY_DISCIPLINES.equals(currentStep)) {
            doLaboratoryDisciplines(bpc);
        } else if (HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(currentStep)) {
            doGovernanceOfficers(bpc);
        } else if (HcsaConsts.STEP_SECTION_LEADER.equals(currentStep)) {
            // Section Leader
            doSectionLeader(bpc);
        } else if (HcsaConsts.STEP_DISCIPLINE_ALLOCATION.equals(currentStep)) {
            doDisciplineAllocation(bpc);
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

    public void prepareSectionLeader(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("PrepareSectionLeader start ...."));
        String currSvcId = getCurrentServiceId(bpc.request);
        // Section Leader config
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList = serviceConfigService.getGOSelectInfo(currSvcId,
                ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
        if (hcsaSvcPersonnelList != null && hcsaSvcPersonnelList.size() > 0) {
            ParamUtil.setRequestAttr(bpc.request, "sectionLeaderConfig", hcsaSvcPersonnelList.get(0));
        }
        ParamUtil.setRequestAttr(bpc.request, "prsFlag", prsFlag);
        AppSvcRelatedInfoDto currSvcInfoDto = getAppSvcRelatedInfo(bpc.request, currSvcId);
        ParamUtil.setRequestAttr(bpc.request, SECTION_LEADER_LIST, currSvcInfoDto.getAppSvcSectionLeaderList());
        log.debug(StringUtil.changeForLog("PrepareSectionLeader end ...."));
    }

    public void doSectionLeader(BaseProcessClass bpc) {
        String currSvcId = getCurrentServiceId(bpc.request);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        AppSvcRelatedInfoDto currSvcInfoDto = getAppSvcRelatedInfo(appSubmissionDto, currSvcId, null);
        String isEdit = ParamUtil.getString(bpc.request, NewApplicationDelegator.IS_EDIT);
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto,
                ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        if (isGetDataFromPage) {
            List<AppSvcPersonnelDto> appSvcSectionLeaderList = getSectionLeadersFromPage(bpc.request);
            currSvcInfoDto.setAppSvcSectionLeaderList(appSvcSectionLeaderList);
            // re-set allocation
            List<AppSvcDisciplineAllocationDto> allocationList = currSvcInfoDto.getAppSvcDisciplineAllocationDtoList();
            if (allocationList != null && !allocationList.isEmpty()) {
                allocationList.forEach(dto -> {
                    String slIndex = dto.getSlIndex();
                    if (!StringUtil.isEmpty(slIndex) && appSvcSectionLeaderList.stream()
                            .noneMatch(sectionList -> slIndex.equals(sectionList.getIndexNo()))) {
                        dto.setSlIndex(null);
                    }
                });
                currSvcInfoDto.setAppSvcDisciplineAllocationDtoList(allocationList);
            }
            reSetChangesForApp(appSubmissionDto);
            setAppSvcRelatedInfoMap(bpc.request, currSvcId, currSvcInfoDto, appSubmissionDto);
            bpc.request.setAttribute(SECTION_LEADER_LIST, appSvcSectionLeaderList);
        }
        String action = ParamUtil.getRequestString(bpc.request, "nextStep");
        Map<String, String> errorMap = null;
        if ("next".equals(action)) {
            errorMap = appSubmissionService.validateSectionLeaders(currSvcInfoDto.getAppSvcSectionLeaderList());
            if (!isRfi) {
                List<HcsaSvcPersonnelDto> psnConfig = serviceConfigService.getGOSelectInfo(currSvcId,
                        ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
                int psnLength = Optional.ofNullable(currSvcInfoDto.getAppSvcSectionLeaderList())
                        .map(List::size)
                        .orElse(0);
                errorMap = NewApplicationHelper.psnMandatoryValidate(psnConfig, ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER,
                        errorMap, psnLength, "errorSECLDR", HcsaConsts.SECTION_LEADER);
            }
        }
        if (errorMap != null && !errorMap.isEmpty()) {
            bpc.request.setAttribute("errormapIs", "error");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaConsts.STEP_SECTION_LEADER);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, AppServicesConsts.NAVTABS_SERVICEFORMS);
        }
        log.debug(StringUtil.changeForLog("doSectionLeader end ..."));
    }

    private List<AppSvcPersonnelDto> getSectionLeadersFromPage(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("Get Section Leader start ..."));
        String currentSvcId = getCurrentServiceId(request);
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request,
                NewApplicationDelegator.APPSUBMISSIONDTO);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(appSubmissionDto, currentSvcId, null);
        List<AppSvcPersonnelDto> oldSectionLeadList = appSvcRelatedInfoDto.getAppSvcSectionLeaderList();
        boolean isRfi = NewApplicationHelper.checkIsRfi(request);
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
            if (!isRfi&&ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType()) ) {
                fromPage = true;
            } else if (AppConsts.YES.equals(isPartEdit)) {
                fromPage = true;
            } else if (!StringUtil.isEmpty(indexNo) && oldSectionLeadList != null && !oldSectionLeadList.isEmpty()) {
                fromOld = true;
            }
            if (fromPage) {
                sectionLeaderList.add(getSectionLeaderFromPage(String.valueOf(i), request));
            } else if (fromOld) {
                oldSectionLeadList.stream()
                        .filter(dto -> indexNo.equals(dto.getIndexNo()))
                        .findAny()
                        .ifPresent(dto -> sectionLeaderList.add(dto));
            }else {
                AppSvcPersonnelDto sectionLeader = new AppSvcPersonnelDto();
                sectionLeader.setIndexNo(UUID.randomUUID().toString());
                sectionLeaderList.add(sectionLeader);
            }
        }
        log.info(StringUtil.changeForLog("The Section Leader length: " + slLength + "; size: " + sectionLeaderList.size()));
        log.debug(StringUtil.changeForLog("Get Section Leader end ..."));
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

    /**
     * StartStep: prepareLaboratoryDisciplines
     *
     * @param bpc
     * @throws
     */
    public void prepareLaboratoryDisciplines(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prepareLaboratoryDisciplines start ...."));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        List<HcsaSvcSubtypeOrSubsumedDto> checkList = serviceConfigService.loadLaboratoryDisciplines(currentSvcId);
        ParamUtil.setSessionAttr(bpc.request, "HcsaSvcSubtypeOrSubsumedDto", (Serializable) checkList);

        //reload
        Map<String, String> reloadChkLstMap = IaisCommonUtils.genNewHashMap();
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        if (appSvcRelatedInfoDto != null) {
            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
            if (appSvcLaboratoryDisciplinesDtoList != null && !appSvcLaboratoryDisciplinesDtoList.isEmpty()) {
                for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList) {
                    String hciName = appSvcLaboratoryDisciplinesDto.getPremiseVal();
                    List<AppSvcChckListDto> appSvcChckListDtos = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                    if (appSvcChckListDtos != null && !appSvcChckListDtos.isEmpty()) {
                        for (AppSvcChckListDto appSvcChckListDto : appSvcChckListDtos) {
                            reloadChkLstMap.put(currentSvcId + hciName + appSvcChckListDto.getChkLstConfId(), "checked");
                        }
                    }
                }
                ParamUtil.setRequestAttr(bpc.request, "svcLaboratoryDisciplinesDto", appSvcLaboratoryDisciplinesDtoList);
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "reloadLaboratoryDisciplines", (Serializable) reloadChkLstMap);
        log.debug(StringUtil.changeForLog("the do prepareLaboratoryDisciplines end ...."));
    }


    /**
     * StartStep: prepareGovernanceOfficers
     *
     * @param bpc
     * @throws
     */
    public void prepareGovernanceOfficers(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prepareGovernanceOfficers start ...."));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        int mandatoryCount = 0;
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        if (!StringUtil.isEmpty(currentSvcId)) {
            //min and max count
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList = serviceConfigService.getGOSelectInfo(currentSvcId, ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
            if (hcsaSvcPersonnelList != null && hcsaSvcPersonnelList.size() > 0) {
                HcsaSvcPersonnelDto hcsaSvcPersonnelDto = hcsaSvcPersonnelList.get(0);
                mandatoryCount = hcsaSvcPersonnelDto.getMandatoryCount();
                ParamUtil.setSessionAttr(bpc.request, "HcsaSvcPersonnel", hcsaSvcPersonnelDto);
            }
        }
        if (appSvcRelatedInfoDto != null) {
            List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            if (appSvcCgoDtoList != null && appSvcCgoDtoList.size() > mandatoryCount) {
                mandatoryCount = appSvcCgoDtoList.size();
            }
        }
        ParamUtil.setRequestAttr(bpc.request, "CgoMandatoryCount", mandatoryCount);
        List<SelectOption> cgoSelectList = NewApplicationHelper.genAssignPersonSel(bpc.request, true);
        ParamUtil.setRequestAttr(bpc.request, "CgoSelectList", cgoSelectList);

        List<SelectOption> idTypeSelectList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ID_TYPE);
        ParamUtil.setRequestAttr(bpc.request, DROPWOWN_IDTYPESELECT, idTypeSelectList);

        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
        String currentSvcCode = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE);
        List<SelectOption> specialtySelectList = NewApplicationHelper.genSpecialtySelectList(currentSvcCode, true);
        ParamUtil.setSessionAttr(bpc.request, "SpecialtySelectList", (Serializable) specialtySelectList);
        Map<String, String> specialtyAttr = IaisCommonUtils.genNewHashMap();
        specialtyAttr.put("name", "specialty");
        specialtyAttr.put("class", "specialty");
        specialtyAttr.put("style", "display: none;");
        String specialtyHtml = NewApplicationHelper.generateDropDownHtml(specialtyAttr, specialtySelectList, null, null);
        ParamUtil.setRequestAttr(bpc.request, "SpecialtyHtml", specialtyHtml);
        List<SelectOption> designationOpList = NewApplicationHelper.genDesignationOpList(true);
        ParamUtil.setRequestAttr(bpc.request, "designationOpList", designationOpList);
        //reload
        if (appSvcRelatedInfoDto != null) {
            List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            ParamUtil.setRequestAttr(bpc.request, GOVERNANCEOFFICERSDTOLIST, appSvcCgoDtoList);
        }
        ParamUtil.setRequestAttr(bpc.request, "prsFlag", prsFlag);
        log.debug(StringUtil.changeForLog("the do prepareGovernanceOfficers end ...."));
    }

    /**
     * StartStep: prepare
     *
     * @param bpc
     * @throws
     */
    public void prepareDisciplineAllocation(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the do prepareDisciplineAllocation start ...."));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        List<HcsaSvcSubtypeOrSubsumedDto> svcScopeDtoList = (List<HcsaSvcSubtypeOrSubsumedDto>) ParamUtil.getSessionAttr(bpc.request, "HcsaSvcSubtypeOrSubsumedDto");
        Map<String, HcsaSvcSubtypeOrSubsumedDto> svcScopeAlignMap = IaisCommonUtils.genNewHashMap();
        if(svcScopeDtoList == null){
            svcScopeDtoList = serviceConfigService.loadLaboratoryDisciplines(currentSvcId);
        }
        NewApplicationHelper.recursingSvcScope(svcScopeDtoList,svcScopeAlignMap);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        List<AppSvcLaboratoryDisciplinesDto> newChkLstDtoList = IaisCommonUtils.genNewArrayList();
        for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList) {
            AppSvcLaboratoryDisciplinesDto loadSvcScopePageDto = (AppSvcLaboratoryDisciplinesDto) CopyUtil.copyMutableObject(appSvcLaboratoryDisciplinesDto);
            //107770
            List<AppSvcChckListDto> appSvcChckListDtos = loadSvcScopePageDto.getAppSvcChckListDtoList();
            List<AppSvcChckListDto> newAppSvcChckListDtos = NewApplicationHelper.handlerPleaseIndicateLab(appSvcChckListDtos,svcScopeAlignMap);
            loadSvcScopePageDto.setAppSvcChckListDtoList(newAppSvcChckListDtos);
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                if (loadSvcScopePageDto.getPremiseVal().equals(appGrpPremisesDto.getPremisesIndexNo())) {
                    loadSvcScopePageDto.setPremiseGetAddress(appGrpPremisesDto.getAddress());
                }
            }
            newChkLstDtoList.add(loadSvcScopePageDto);
        }

        ParamUtil.setSessionAttr(bpc.request, "PremisesAndChkLst", (Serializable) newChkLstDtoList);
        List<SelectOption> spList = IaisCommonUtils.genNewArrayList();
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
        if (appSvcCgoDtoList != null && !appSvcCgoDtoList.isEmpty()) {
            for (AppSvcPrincipalOfficersDto cgo : appSvcCgoDtoList) {
                SelectOption sp = new SelectOption(cgo.getIdNo(), cgo.getName());
                spList.add(sp);
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "CgoSelect", (Serializable) spList);
        List<AppSvcPersonnelDto> slList = appSvcRelatedInfoDto.getAppSvcSectionLeaderList();
        List<SelectOption> slOpts = IaisCommonUtils.genNewArrayList();
        if (slList != null && !slList.isEmpty()) {
            for (AppSvcPersonnelDto sl : slList) {
                SelectOption sp = new SelectOption(sl.getIndexNo(), sl.getName());
                slOpts.add(sp);
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "slSelectOpts", (Serializable) slOpts);

        Map<String, String> reloadAllocation = IaisCommonUtils.genNewHashMap();
        List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        if (appSvcDisciplineAllocationDtoList != null && !appSvcDisciplineAllocationDtoList.isEmpty()) {
            for (AppSvcDisciplineAllocationDto allocationDto : appSvcDisciplineAllocationDtoList) {
                reloadAllocation.put("cgo" + allocationDto.getPremiseVal() + allocationDto.getChkLstConfId(), allocationDto.getIdNo());
                reloadAllocation.put("sl" + allocationDto.getPremiseVal() + allocationDto.getChkLstConfId(), allocationDto.getSlIndex());
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "ReloadAllocationMap", (Serializable) reloadAllocation);

        // step name
        String svcScopePageName = getStepName(bpc,currentSvcId,HcsaLicenceFeConstant.LABORATORYDISCIPLINES);
        ParamUtil.setRequestAttr(bpc.request,"svcScopePageName",svcScopePageName);
        StringBuilder sb=new StringBuilder();
        sb.append("Please ensure that a clinical governance officer is assigned to each ")
                .append(svcScopePageName.toLowerCase());
        ParamUtil.setRequestAttr(bpc.request,"CURR_STEP_NAME_LABLE",sb.toString());
        //71688
        String rfiPremiseId = "nice-select";
        for (AppGrpPremisesDto appGrpPremisesDto : appSubmissionDto.getAppGrpPremisesDtoList()){
            if (appGrpPremisesDto.isRfiCanEdit()){
                rfiPremiseId = appGrpPremisesDto.getPremisesIndexNo();
            }
        }
        ParamUtil.setRequestAttr(bpc.request, "RfiPremiseId", rfiPremiseId);
        log.debug(StringUtil.changeForLog("the do prepareDisciplineAllocation end ...."));
    }

    /**
     * StartStep: preparePrincipalOfficers
     *
     * @param bpc
     * @throws
     */
    public void preparePrincipalOfficers(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do preparePrincipalOfficers start ...."));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        List<HcsaSvcPersonnelDto> principalOfficerConfig = serviceConfigService.getGOSelectInfo(currentSvcId, ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
        List<HcsaSvcPersonnelDto> deputyPrincipalOfficerConfig = serviceConfigService.getGOSelectInfo(currentSvcId, ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
        int mandatory = 0;
        int deputyMandatory = 0;
        if (principalOfficerConfig != null && !principalOfficerConfig.isEmpty()) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = principalOfficerConfig.get(0);
            ParamUtil.setRequestAttr(bpc.request, "poHcsaSvcPersonnelDto", hcsaSvcPersonnelDto);
            if (hcsaSvcPersonnelDto != null) {
                mandatory = hcsaSvcPersonnelDto.getMandatoryCount();
            }
        }

        if (deputyPrincipalOfficerConfig != null && !deputyPrincipalOfficerConfig.isEmpty()) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = deputyPrincipalOfficerConfig.get(0);
            ParamUtil.setRequestAttr(bpc.request, "dpoHcsaSvcPersonnelDto", hcsaSvcPersonnelDto);
            if (hcsaSvcPersonnelDto != null) {
                deputyMandatory = hcsaSvcPersonnelDto.getMandatoryCount();
            }
        }

        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        List<AppSvcPrincipalOfficersDto> principalOfficersDtos = IaisCommonUtils.genNewArrayList();
        List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)) {
            NewApplicationHelper.assignPoDpoDto(appSvcPrincipalOfficersDtos,principalOfficersDtos,deputyPrincipalOfficersDtos);
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
        ParamUtil.setRequestAttr(bpc.request, "ReloadDeputyPrincipalOfficers", deputyPrincipalOfficersDtos);
        if (StringUtil.isEmpty(appSvcRelatedInfoDto.getDeputyPoFlag())) {
            ParamUtil.setRequestAttr(bpc.request, "DeputyPoFlag", "0");
        } else {
            ParamUtil.setRequestAttr(bpc.request, "DeputyPoFlag", appSvcRelatedInfoDto.getDeputyPoFlag());
        }

        List<SelectOption> IdTypeSelect = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ID_TYPE);
        ParamUtil.setRequestAttr(bpc.request, "IdTypeSelect", IdTypeSelect);

        List<SelectOption> assignSelectList = NewApplicationHelper.genAssignPersonSel(bpc.request, true);
        ParamUtil.setRequestAttr(bpc.request, "PrincipalOfficersAssignSelect", assignSelectList);

        List<SelectOption> deputyAssignSelectList = NewApplicationHelper.genAssignPersonSel(bpc.request, true);
        ParamUtil.setRequestAttr(bpc.request, "DeputyPrincipalOfficersAssignSelect", deputyAssignSelectList);

        List<SelectOption> deputyFlagSelect = IaisCommonUtils.genNewArrayList();
        SelectOption deputyFlagOp1 = new SelectOption("-1", NewApplicationDelegator.FIRESTOPTION);
        deputyFlagSelect.add(deputyFlagOp1);
        SelectOption deputyFlagOp2 = new SelectOption("0", "No");
        deputyFlagSelect.add(deputyFlagOp2);
        SelectOption deputyFlagOp3 = new SelectOption("1", "Yes");
        deputyFlagSelect.add(deputyFlagOp3);
        ParamUtil.setRequestAttr(bpc.request, "DeputyFlagSelect", deputyFlagSelect);
        List<SelectOption> designationOpList = NewApplicationHelper.genDesignationOpList(true);
        ParamUtil.setRequestAttr(bpc.request, "designationOpList", designationOpList);

        log.debug(StringUtil.changeForLog("the do preparePrincipalOfficers end ...."));
    }

    public void prepareKeyAppointmentHolder(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("prepareKeyAppointmentHolder start ..."));
        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.CURRENTSERVICEID);
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList = serviceConfigService.getGOSelectInfo(currSvcId, ApplicationConsts.PERSONNEL_PSN_KAH);
        if (hcsaSvcPersonnelList != null && hcsaSvcPersonnelList.size() > 0) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = hcsaSvcPersonnelList.get(0);
            ParamUtil.setSessionAttr(bpc.request, "keyAppointmentHolderConfigDto", hcsaSvcPersonnelDto);
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currSvcId);
        List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderDtoList = appSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList();
        ParamUtil.setRequestAttr(bpc.request,"AppSvcKeyAppointmentHolderDtoList",appSvcKeyAppointmentHolderDtoList);
        List<SelectOption> assignSelectList = NewApplicationHelper.genAssignPersonSel(bpc.request, true);
        ParamUtil.setRequestAttr(bpc.request, "KeyAppointmentHolderAssignSelect", assignSelectList);
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
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        List<HcsaSvcDocConfigDto> hcsaSvcDocDtos = serviceConfigService.getAllHcsaSvcDocs(currentSvcId);
        if (hcsaSvcDocDtos != null && !hcsaSvcDocDtos.isEmpty()) {
            List<HcsaSvcDocConfigDto> serviceDocConfigDto = IaisCommonUtils.genNewArrayList();
            List<HcsaSvcDocConfigDto> premServiceDocConfigDto = IaisCommonUtils.genNewArrayList();
            for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:hcsaSvcDocDtos){
                if ("0".equals(hcsaSvcDocConfigDto.getDupForPrem())) {
                    serviceDocConfigDto.add(hcsaSvcDocConfigDto);
                } else if ("1".equals(hcsaSvcDocConfigDto.getDupForPrem())) {
                    premServiceDocConfigDto.add(hcsaSvcDocConfigDto);
                }
            }
            ParamUtil.setSessionAttr(bpc.request, "serviceDocConfigDto", (Serializable) serviceDocConfigDto);
            ParamUtil.setSessionAttr(bpc.request, "premServiceDocConfigDto", (Serializable) premServiceDocConfigDto);
        }
        ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.SVC_DOC_CONFIG, (Serializable) hcsaSvcDocDtos);

        Map<String, AppSvcDocDto> reloadSvcDo = IaisCommonUtils.genNewHashMap();
        if (appSvcRelatedInfoDto != null) {
            List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
            if (appSvcDocDtos != null && !appSvcDocDtos.isEmpty()) {
                for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                    /*String premVal = appSvcDocDto.getPremisesVal();
                    if(StringUtil.isEmpty(premVal)){
                        reloadSvcDo.put(appSvcDocDto.getSvcDocId(), appSvcDocDto);
                    }else{
                        reloadSvcDo.put("prem" + appSvcDocDto.getSvcDocId() + premVal, appSvcDocDto);
                    }*/
                    reloadSvcDo.put(appSvcDocDto.getPrimaryDocReloadName(), appSvcDocDto);
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request, RELOADSVCDOC, (Serializable) reloadSvcDo);

        List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
        Map<String, List<AppSvcDocDto>> reloadDocMap = IaisCommonUtils.genNewHashMap();
        if(!IaisCommonUtils.isEmpty(appSvcDocDtos)){
            for(AppSvcDocDto appSvcDocDto:appSvcDocDtos){
                String reloadDocMapKey;
                String premVal = appSvcDocDto.getPremisesVal();
                if(StringUtil.isEmpty(premVal)){
                    reloadDocMapKey = appSvcDocDto.getSvcDocId();
                }else{
                    reloadDocMapKey = premVal + appSvcDocDto.getSvcDocId();
                }
                String psnIndexNo = appSvcDocDto.getPsnIndexNo();
                if(!StringUtil.isEmpty(psnIndexNo)){
                    reloadDocMapKey = reloadDocMapKey + psnIndexNo;
                }

                List<AppSvcDocDto> appSvcDocDtos1 = reloadDocMap.get(reloadDocMapKey);
                if(IaisCommonUtils.isEmpty(appSvcDocDtos1)){
                    appSvcDocDtos1 = IaisCommonUtils.genNewArrayList();
                }
                appSvcDocDtos1.add(appSvcDocDto);
                reloadDocMap.put(reloadDocMapKey,appSvcDocDtos1);
            }
            //do sort
            reloadDocMap.forEach((k, v) -> Collections.sort(v, Comparator.comparing(AppSvcDocDto::getSeqNum)));
        }
        ParamUtil.setSessionAttr(bpc.request,"svcDocReloadMap", (Serializable) reloadDocMap);
        //set dupForPsn attr
        NewApplicationHelper.setDupForPersonAttr(bpc.request,appSvcRelatedInfoDto);


        int sysFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setRequestAttr(bpc.request,"sysFileSize",sysFileSize);
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
        HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(NewApplicationConstant.CO_MAP);
        Map<String, String> allChecked = isAllChecked(bpc, appSubmissionDto);
        if (allChecked.isEmpty()) {
            coMap.put("information", "information");
        } else {
            coMap.put("information", "");
        }

        bpc.request.getSession().setAttribute(NewApplicationConstant.CO_MAP, coMap);
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
            log.info(StringUtil.changeForLog("get current svc info...."));
            AppSvcRelatedInfoDto appSvcRelatedInfoDto;
            if(!StringUtil.isEmpty(appNo)){
                appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, svcId, appNo);
            }else{
                appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, svcId);
            }
            List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId = serviceConfigService.getHcsaServiceStepSchemesByServiceId(svcId);
            appSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
            //sort po,dpo
            List<AppSvcPrincipalOfficersDto> poAndDpo = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
            if(!IaisCommonUtils.isEmpty(poAndDpo)){
                poAndDpo.sort((h1,h2)->h2.getPsnType().compareTo(h1.getPsnType()));
                appSvcRelatedInfoDto.setAppSvcPrincipalOfficersDtoList(poAndDpo);
            }
            AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO);
            Map<String, List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = appSubmissionService.getDisciplineAllocationDtoList(appSubmissionDto, svcId);
            for(HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto:hcsaServiceStepSchemesByServiceId){
                switch (hcsaServiceStepSchemeDto.getStepCode()){
                    case HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS:
                        List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                        if(!IaisCommonUtils.isEmpty(appSvcCgoDtos)){
                            List<AppSvcPrincipalOfficersDto> reloadDto = IaisCommonUtils.genNewArrayList();
                            for(AppSvcPrincipalOfficersDto appSvcCgoDto:appSvcCgoDtos){
                                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = MiscUtil.transferEntityDto(appSvcCgoDto,AppSvcPrincipalOfficersDto.class);
                                boolean isAllFieldNull = NewApplicationHelper.isAllFieldNull(appSvcPrincipalOfficersDto);
                                if(!isAllFieldNull){
                                    reloadDto.add(appSvcCgoDto);
                                }
                            }
                            if(IaisCommonUtils.isEmpty(reloadDto)){
                                appSvcRelatedInfoDto.setAppSvcCgoDtoList(null);
                            }else{
                                appSvcRelatedInfoDto.setAppSvcCgoDtoList(reloadDto);
                            }
                        }
                        break;
                    case HcsaConsts.STEP_PRINCIPAL_OFFICERS:
                        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                        if(!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)){
                            List<AppSvcPrincipalOfficersDto> reloadDto = IaisCommonUtils.genNewArrayList();
                            removeEmptyPsn(appSvcPrincipalOfficersDtos,reloadDto);
                            if(IaisCommonUtils.isEmpty(reloadDto)){
                                appSvcRelatedInfoDto.setAppSvcPrincipalOfficersDtoList(null);
                            }else{
                                appSvcRelatedInfoDto.setAppSvcPrincipalOfficersDtoList(reloadDto);
                            }
                        }
                        break;
                    case HcsaConsts.STEP_SERVICE_PERSONNEL:
                        List<AppSvcPersonnelDto> appSvcPersonnelDtos = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
                        if(!IaisCommonUtils.isEmpty(appSvcPersonnelDtos)){
                            List<AppSvcPersonnelDto> reloadDto = IaisCommonUtils.genNewArrayList();
                            for(AppSvcPersonnelDto appSvcPersonnelDto:appSvcPersonnelDtos){
                                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = MiscUtil.transferEntityDto(appSvcPersonnelDto,AppSvcPrincipalOfficersDto.class);
                                boolean isAllFieldNull = NewApplicationHelper.isAllFieldNull(appSvcPrincipalOfficersDto);
                                if(!isAllFieldNull){
                                    reloadDto.add(appSvcPersonnelDto);
                                }
                            }
                            if(IaisCommonUtils.isEmpty(reloadDto)){
                                appSvcRelatedInfoDto.setAppSvcPersonnelDtoList(null);
                            }else{
                                appSvcRelatedInfoDto.setAppSvcPersonnelDtoList(reloadDto);
                            }
                        }
                        break;
                    case HcsaConsts.STEP_MEDALERT_PERSON:
                        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
                        if(!IaisCommonUtils.isEmpty(appSvcMedAlertPersonList)){
                            List<AppSvcPrincipalOfficersDto> reloadDto = IaisCommonUtils.genNewArrayList();
                            removeEmptyPsn(appSvcMedAlertPersonList,reloadDto);
                            if(IaisCommonUtils.isEmpty(reloadDto)){
                                appSvcRelatedInfoDto.setAppSvcMedAlertPersonList(null);
                            }else{
                                appSvcRelatedInfoDto.setAppSvcMedAlertPersonList(reloadDto);
                            }
                        }
                        break;
                    case HcsaConsts.STEP_LABORATORY_DISCIPLINES:
                        break;
                    case HcsaConsts.STEP_DISCIPLINE_ALLOCATION:
                        Map<String, List<AppSvcDisciplineAllocationDto>> newReloadMap = IaisCommonUtils.genNewHashMap();
                        for(Map.Entry<String,List<AppSvcDisciplineAllocationDto>> enntry:reloadDisciplineAllocationMap.entrySet()){
                            List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtos = enntry.getValue();
                            if(!IaisCommonUtils.isEmpty(appSvcDisciplineAllocationDtos)){
                                List<AppSvcDisciplineAllocationDto> newAllocationDto = IaisCommonUtils.genNewArrayList();
                                for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto:appSvcDisciplineAllocationDtos){
                                    String cgoName = appSvcDisciplineAllocationDto.getCgoSelName();
                                    if(!StringUtil.isEmpty(cgoName)){
                                        newAllocationDto.add(appSvcDisciplineAllocationDto);
                                    }
                                }
                                if(newAllocationDto.size() != 0){
                                    newReloadMap.put(enntry.getKey(),newAllocationDto);
                                }
                            }
                        }
                        if(newReloadMap.size() == 0){
                            reloadDisciplineAllocationMap = null;
                        }else{
                            reloadDisciplineAllocationMap = newReloadMap;
                        }
                        break;
                    case HcsaConsts.STEP_DOCUMENTS:
                        break;
                    default:
                        break;
                }
            }
            List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
            List<AppGrpPremisesDto> appGrpPremisesDtos= appSubmissionDto.getAppGrpPremisesDtoList();
            List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(svcId);
            ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.SVC_DOC_CONFIG, (Serializable) svcDocConfig);
            //set dupForPsn attr
            NewApplicationHelper.setDupForPersonAttr(bpc.request,appSvcRelatedInfoDto);
            //svc doc add align for dup for prem
            NewApplicationHelper.addPremAlignForSvcDoc(svcDocConfig,appSvcDocDtos,appGrpPremisesDtos);
            appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
            //set svc doc title
            Map<String,List<AppSvcDocDto>> reloadSvcDocMap = NewApplicationHelper.genSvcDocReloadMap(svcDocConfig,appGrpPremisesDtos,appSvcRelatedInfoDto);
            appSvcRelatedInfoDto.setMultipleSvcDoc(reloadSvcDocMap);

            ParamUtil.setSessionAttr(bpc.request, "currentPreviewSvcInfo", appSvcRelatedInfoDto);
            ParamUtil.setSessionAttr(bpc.request, "reloadDisciplineAllocationMap", (Serializable) reloadDisciplineAllocationMap);
            ParamUtil.setSessionAttr(bpc.request, "iframeId", iframeId);
        }

        log.debug(StringUtil.changeForLog("the do prepareView end ...."));
    }

    /**
     * StartStep: doLaboratoryDisciplines
     *
     * @param bpc
     * @throws
     */
    public void doLaboratoryDisciplines(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doLaboratoryDisciplines start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);

        String action = ParamUtil.getRequestString(bpc.request, "nextStep");
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)
                    || RfcConst.RFC_BTN_OPTION_SKIP.equals(action)) {
                return;
            }
        }
        String isEdit = ParamUtil.getString(bpc.request, NewApplicationDelegator.IS_EDIT);
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.REQUESTINFORMATIONCONFIG);
        boolean isRfi = false;
        if (requestInformationConfig != null) {
            isRfi = true;
        }
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        if (isGetDataFromPage) {
            AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto = null;
            Map<String, HcsaSvcSubtypeOrSubsumedDto> map = IaisCommonUtils.genNewHashMap();
            List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = (List<HcsaSvcSubtypeOrSubsumedDto>) ParamUtil.getSessionAttr(bpc.request, "HcsaSvcSubtypeOrSubsumedDto");
            NewApplicationHelper.recursingSvcScope(hcsaSvcSubtypeOrSubsumedDtos, map);
            String currentSvcId = getCurrentServiceId(bpc.request);
            AppSvcRelatedInfoDto currentSvcDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            Map<String, String> reloadChkLstMap = IaisCommonUtils.genNewHashMap();
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = IaisCommonUtils.genNewArrayList();
            int i = 0;
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                String name = appGrpPremisesDto.getPremisesIndexNo() + "control--runtime--1";
                String[] checkList = ParamUtil.getStrings(bpc.request, name);
                List<AppSvcChckListDto> appSvcChckListDtoList = IaisCommonUtils.genNewArrayList();
                if (!StringUtil.isEmpty(checkList)) {
                    for (String maskName : checkList) {
                        String checkBoxId = ParamUtil.getMaskedString(bpc.request, maskName);
                        HcsaSvcSubtypeOrSubsumedDto checkInfo = map.get(checkBoxId);

                        AppSvcChckListDto appSvcChckListDto = new AppSvcChckListDto();
                        appSvcChckListDto.setChkLstConfId(checkInfo.getId());
                        if (NewApplicationConstant.PLEASEINDICATE.equals(checkInfo.getName())) {
                            String subName = ParamUtil.getString(bpc.request, "pleaseIndicate" + i);
                            //appGrpPremisesDto.setOtherScopeName(subName);
                            appSvcChckListDto.setOtherScopeName(subName);
                        }
                        appSvcChckListDto.setChkLstType(checkInfo.getType());
                        appSvcChckListDto.setChkName(checkInfo.getName());
                        appSvcChckListDto.setParentName(checkInfo.getParentId());
                        appSvcChckListDto.setChildrenName(checkInfo.getChildrenId());
                        appSvcChckListDtoList.add(appSvcChckListDto);

                        //PremisesIndexNo()+checkCode()+checkParentId()
                        reloadChkLstMap.put(currentSvcId + appGrpPremisesDto.getPremisesIndexNo() + checkInfo.getId(), "checked");
                    }
                    String premisesType = appGrpPremisesDto.getPremisesType();
                    String premisesValue = appGrpPremisesDto.getPremisesIndexNo();
                    String premisesAddress = appGrpPremisesDto.getAddress();
                    appSvcLaboratoryDisciplinesDto = new AppSvcLaboratoryDisciplinesDto();
                    appSvcLaboratoryDisciplinesDto.setPremiseType(premisesType);
                    appSvcLaboratoryDisciplinesDto.setPremiseVal(premisesValue);
                    appSvcLaboratoryDisciplinesDto.setPremiseGetAddress(premisesAddress);
                    appSvcLaboratoryDisciplinesDto.setAppSvcChckListDtoList(appSvcChckListDtoList);
                    appSvcLaboratoryDisciplinesDtoList.add(appSvcLaboratoryDisciplinesDto);
                }
                i++;
            }
            String crud_action_type = ParamUtil.getRequestString(bpc.request, "nextStep");
            if ("next".equals(crud_action_type)) {
                errorMap = NewApplicationHelper.doValidateLaboratory(appGrpPremisesDtoList, appSvcLaboratoryDisciplinesDtoList, currentSvcId,hcsaSvcSubtypeOrSubsumedDtos);
                reSetChangesForApp(appSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
            }
            ParamUtil.setSessionAttr(bpc.request, "reloadLaboratoryDisciplines", (Serializable) reloadChkLstMap);

            if (!errorMap.isEmpty()) {
                //set audit
                bpc.request.setAttribute("errormapIs", "error");
                NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.LABORATORYDISCIPLINES);
                return;
            } else {
                handleDisciplineAllocations(appSvcLaboratoryDisciplinesDtoList, currentSvcDto);
            }
            currentSvcDto.setAppSvcLaboratoryDisciplinesDtoList(appSvcLaboratoryDisciplinesDtoList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcDto);
        }

        log.debug(StringUtil.changeForLog("the do doLaboratoryDisciplines end ...."));
    }

    private void handleDisciplineAllocations(List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList, AppSvcRelatedInfoDto currentSvcDto) {
        String currentSvcId = currentSvcDto.getServiceId();
        HcsaServiceStepSchemeDto step = serviceConfigService.getHcsaServiceStepSchemeByConds(currentSvcId,
                HcsaConsts.STEP_DISCIPLINE_ALLOCATION);
        if (step == null) {
            return;
        }
        // remove not used discipline allocations
        List<AppSvcDisciplineAllocationDto> allocationDtoList = currentSvcDto.getAppSvcDisciplineAllocationDtoList();
        if (allocationDtoList != null && !allocationDtoList.isEmpty()) {
            Iterator<AppSvcDisciplineAllocationDto> iterator = allocationDtoList.iterator();
            while (iterator.hasNext()) {
                AppSvcDisciplineAllocationDto dto = iterator.next();
                if (!NewApplicationHelper.isIn(dto.getChkLstConfId(), dto.getPremiseVal(),
                        appSvcLaboratoryDisciplinesDtoList)) {
                    iterator.remove();
                }
            }
        }
        // add new discipline allocations
        if (appSvcLaboratoryDisciplinesDtoList != null && !appSvcLaboratoryDisciplinesDtoList.isEmpty()) {
            int len = appSvcLaboratoryDisciplinesDtoList.size();
            if (allocationDtoList == null) {
                allocationDtoList = IaisCommonUtils.genNewArrayList(len);
            }
            int index = 0;
            for (int j = 0; j < len; j++) {
                AppSvcLaboratoryDisciplinesDto dto = appSvcLaboratoryDisciplinesDtoList.get(j);
                int n = dto.getAppSvcChckListDtoList().size();
                String premiseVal =  dto.getPremiseVal();
                boolean needNew = false;
                for (int k = 0; k < n; k++) {
                    String chkLstConfId = dto.getAppSvcChckListDtoList().get(k).getChkLstConfId();
                    if (allocationDtoList.size() > index) {
                        AppSvcDisciplineAllocationDto allocation = allocationDtoList.get(index);
                        if (!Objects.equals(premiseVal, allocation.getPremiseVal()) ||
                                !Objects.equals(chkLstConfId, allocation.getChkLstConfId())) {
                            needNew = true;
                        }
                    } else {
                        needNew = true;
                    }
                    if (needNew) {
                        AppSvcDisciplineAllocationDto allocation = new AppSvcDisciplineAllocationDto();
                        allocation.setChkLstConfId(chkLstConfId);
                        allocation.setPremiseVal(premiseVal);
                        allocationDtoList.add(index, allocation);
                        needNew = false;
                    }
                    index++;
                }
            }
            currentSvcDto.setAppSvcDisciplineAllocationDtoList(allocationDtoList);
        } else {
            currentSvcDto.setAppSvcDisciplineAllocationDtoList(null);
        }
    }

    private Map<String, String> isAllChecked(BaseProcessClass bpc, AppSubmissionDto appSubmissionDto) {
        StringBuilder errorSvcConfig = new StringBuilder();
        List<AppSvcRelatedInfoDto> dto = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        Map<String, String> errorMap = new HashMap<>();
        Map<String, AppSvcPersonAndExtDto> licPersonMap = null;
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,
                    NewApplicationDelegator.LICPERSONSELECTMAP);
        }
        for (int i = 0; i < dto.size(); i++) {
            AppSvcRelatedInfoDto currSvcInfoDto = dto.get(i);
            Map<String, String> map = appSubmissionService.doCheckBox(currSvcInfoDto, appSubmissionDto, licPersonMap);
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
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action) || RfcConst.RFC_BTN_OPTION_SKIP.equals(action)) {
                return;
            }
        }
        String isEdit = ParamUtil.getString(bpc.request, NewApplicationDelegator.IS_EDIT);
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.REQUESTINFORMATIONCONFIG);
        boolean isRfi = false;
        if (requestInformationConfig != null) {
            isRfi = true;
        }
        String actionType = bpc.request.getParameter("nextStep");
        Map<String, String> errList = IaisCommonUtils.genNewHashMap();
        Map<String,AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.PERSONSELECTMAP);
        Map<String,AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.LICPERSONSELECTMAP);
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION, isEdit, isRfi);
        String svcCode = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currentSvcRelatedDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = currentSvcRelatedDto.getAppSvcCgoDtoList();
        if (isGetDataFromPage) {
            appSvcCgoDtoList = genAppSvcCgoDto(bpc.request);
            log.debug(StringUtil.changeForLog("cycle cgo dto to retrieve prs info start ..."));
            log.debug("prs server flag {}",prsFlag);
            if("Y".equals(prsFlag) && !IaisCommonUtils.isEmpty(appSvcCgoDtoList)){
                for(int i=0;i<appSvcCgoDtoList.size();i++ ){
                    AppSvcPrincipalOfficersDto appSvcCgoDto = appSvcCgoDtoList.get(i);
                    String profRegNo = appSvcCgoDto.getProfRegNo();
                    ProfessionalResponseDto professionalResponseDto = appSubmissionService.retrievePrsInfo(profRegNo);
                    String specialtyStr = "";
                    String subSpecialtyStr = "";
                    String qualificationStr = "";
                    if (professionalResponseDto != null) {
                        if (professionalResponseDto.isHasException() || StringUtil.isNotEmpty(professionalResponseDto.getStatusCode())) {
                            log.debug(StringUtil.changeForLog("prs svc down ..."));
                            if (StringUtil.isEmpty(actionType) || "next".equals(actionType)) {
                                if (professionalResponseDto.isHasException()) {
                                    bpc.request.setAttribute(PRS_SERVICE_DOWN, PRS_SERVICE_DOWN);
                                    errList.put(PRS_SERVICE_DOWN, PRS_SERVICE_DOWN);
                                } else if ("401".equals(professionalResponseDto.getStatusCode())) {
                                    errList.put("professionRegoNo" + i, "GENERAL_ERR0054");
                                } else {
                                    errList.put("professionRegoNo" + i, "GENERAL_ERR0042");
                                }
                            }
                            appSvcCgoDto.setSpeciality(specialtyStr);
                            appSvcCgoDto.setSubSpeciality(subSpecialtyStr);
                            appSvcCgoDto.setQualification(qualificationStr);
                            continue;
                        }
                        String name = professionalResponseDto.getName();
                        boolean needLoadName =
                                !appSvcCgoDto.isLicPerson() && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType);
                        if(needLoadName){
                            appSvcCgoDto.setName(name);
                        }
                        //retrieve data from prs server
                        List<String> specialtyList = professionalResponseDto.getSpecialty();
                        if(!IaisCommonUtils.isEmpty(specialtyList)){
                            List<String> notNullList = IaisCommonUtils.genNewArrayList();
                            for(String value:specialtyList){
                                if(!StringUtil.isEmpty(value)){
                                    notNullList.add(value);
                                }
                            }
                            specialtyStr = String.join(",",notNullList);
                        }
                        appSvcCgoDto.setSpeciality(specialtyStr);

                        List<String> subSpecialtyList = professionalResponseDto.getSubspecialty();
                        if (subSpecialtyList != null && !subSpecialtyList.isEmpty()) {
                            subSpecialtyStr = subSpecialtyList.get(0);
                        }
                        appSvcCgoDto.setSubSpeciality(subSpecialtyStr);
                        List<String> qualificationList = professionalResponseDto.getQualification();
                        if (!IaisCommonUtils.isEmpty(qualificationList)) {
                            qualificationStr = qualificationList.get(0);
                        }
                        appSvcCgoDto.setQualification(qualificationStr);
                    }else{
                        appSvcCgoDto.setSpeciality(specialtyStr);
                        appSvcCgoDto.setSubSpeciality(subSpecialtyStr);
                        appSvcCgoDto.setQualification(qualificationStr);
                    }
                }
                currentSvcRelatedDto.setAppSvcCgoDtoList(appSvcCgoDtoList);
            }
            log.debug(StringUtil.changeForLog("cycle cgo dto to retrieve prs info end ..."));
            currentSvcRelatedDto.setAppSvcCgoDtoList(appSvcCgoDtoList);
            // re-set allocation
            List<AppSvcDisciplineAllocationDto> allocationList = currentSvcRelatedDto.getAppSvcDisciplineAllocationDtoList();
            if (allocationList != null && !allocationList.isEmpty()) {
                List<AppSvcPrincipalOfficersDto> cgoList = appSvcCgoDtoList;
                allocationList.forEach(dto -> {
                    String idNo = dto.getIdNo();
                    if (!StringUtil.isEmpty(idNo) && cgoList.stream()
                            .noneMatch(cgo -> idNo.equals(cgo.getIdNo()))) {
                        dto.setIdNo(null);
                    }
                });
                currentSvcRelatedDto.setAppSvcDisciplineAllocationDtoList(allocationList);
            }
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto);
        }
        if ("next".equals(actionType)) {
            Map<String, String> map = NewApplicationHelper.doValidateGovernanceOfficers(appSvcCgoDtoList, licPersonMap, svcCode);
            //validate mandatory count
            int psnLength = 0;
            if (!IaisCommonUtils.isEmpty(appSvcCgoDtoList)) {
                psnLength = appSvcCgoDtoList.size();
            }
            List<HcsaSvcPersonnelDto> psnConfig = serviceConfigService.getGOSelectInfo(currentSvcId,
                    ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
            if (!isRfi) {
                map = NewApplicationHelper.psnMandatoryValidate(psnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, map, psnLength,
                        "psnMandatory", ApplicationConsts.PERSONNEL_PSN_TYPE_CLINICAL_GOVERNANCE_OFFICER);
            }
            errList.putAll(map);
            reSetChangesForApp(appSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
        }
        if (!errList.isEmpty()) {
            //set audit
            bpc.request.setAttribute("errormapIs", "error");
            NewApplicationHelper.setAudiErrMap(isRfi, appSubmissionDto.getAppType(), errList, appSubmissionDto.getRfiAppNo(),
                    appSubmissionDto.getLicenceNo());
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,
                    HcsaLicenceFeConstant.GOVERNANCEOFFICERS);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errList));
            Map<String, AppSvcPersonAndExtDto> newPersonMap = removeDirtyDataFromPsnDropDown(appSubmissionDto, licPersonMap,
                    personMap);
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.PERSONSELECTMAP, (Serializable) newPersonMap);
        } else if (isGetDataFromPage) {
            //sync person dropdown and submisson dto
            syncDropDownAndPsn(appSubmissionDto, appSvcCgoDtoList, svcCode, bpc.request);
        }
        log.debug(StringUtil.changeForLog("the do doGovernanceOfficers end ...."));
    }


    /**
     * StartStep: doDisciplineAllocation
     *
     * @param bpc
     * @throws
     */
    public void doDisciplineAllocation(BaseProcessClass bpc) throws  Exception{
        log.debug(StringUtil.changeForLog("the do doDisciplineAllocation start ...."));
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String action = ParamUtil.getRequestString(bpc.request, "nextStep");
        boolean cgoChange=false;
        String appType = appSubmissionDto.getAppType();
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        if (isRfi || (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType))) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)
                    || RfcConst.RFC_BTN_OPTION_SKIP.equals(action)) {
                return;
            }
            AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "oldAppSubmissionDto");
            if(oldAppSubmissionDto!=null){
                List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getAppSvcCgoDtoList();
                if(appSvcCgoDtoList!=null){
                    List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList1 = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getAppSvcCgoDtoList();
                    if(!appSvcCgoDtoList.equals(appSvcCgoDtoList1)){
                        cgoChange=true;
                    }
                }
            }
        }
        String isEdit = ParamUtil.getString(bpc.request, NewApplicationDelegator.IS_EDIT);
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.REQUESTINFORMATIONCONFIG);
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        Set<String> clickEditPage = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
        boolean svcScopeEdit = false;
        for (String item : clickEditPage) {
            if (NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_LABORATORY.equals(item)) {
                svcScopeEdit = true;
                break;
            }
        }

        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currentSvcRelatedDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        if (isGetDataFromPage || svcScopeEdit || cgoChange) {
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            List<HcsaSvcSubtypeOrSubsumedDto> svcScopeDtoList = (List<HcsaSvcSubtypeOrSubsumedDto>) ParamUtil.getSessionAttr(bpc.request, "HcsaSvcSubtypeOrSubsumedDto");
            if(svcScopeDtoList == null){
                svcScopeDtoList = serviceConfigService.loadLaboratoryDisciplines(currentSvcId);
            }
            Map<String, HcsaSvcSubtypeOrSubsumedDto> svcScopeAlignMap = IaisCommonUtils.genNewHashMap();
            NewApplicationHelper.recursingSvcScope(svcScopeDtoList,svcScopeAlignMap);
            List<AppSvcDisciplineAllocationDto> daList = IaisCommonUtils.genNewArrayList();
            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = currentSvcRelatedDto.getAppSvcLaboratoryDisciplinesDtoList();
            if (appSvcLaboratoryDisciplinesDtoList != null) {
                for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList) {
                    String premisesValue = "";
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                        if (appSvcLaboratoryDisciplinesDto.getPremiseVal().equals(appGrpPremisesDto.getPremisesIndexNo())) {
                            premisesValue = appGrpPremisesDto.getPremisesIndexNo();
                            break;
                        }
                    }
                    List<AppSvcChckListDto> newAppSvcChckListDtos = NewApplicationHelper.handlerPleaseIndicateLab(appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList(),svcScopeAlignMap);
                    AppSvcChckListDto targetChkDto = NewApplicationHelper.getScopeDtoByRecursiveTarNameUpward(appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList(),svcScopeAlignMap,NewApplicationConstant.PLEASEINDICATE,NewApplicationConstant.SERVICE_SCOPE_LAB_OTHERS);
                    AppSvcDisciplineAllocationDto targetAllocationDto = null;
                    int chkLstSize = newAppSvcChckListDtos.size();
                    for (int i = 0; i < chkLstSize; i++) {
                        StringBuilder chkAndCgoName = new StringBuilder()
                                .append(premisesValue)
                                .append(i);
                        String chkAndCgoValue = ParamUtil.getString(bpc.request, chkAndCgoName.toString());
                        if (chkAndCgoValue != null && !"".equals(chkAndCgoValue)) {
                            AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto = new AppSvcDisciplineAllocationDto();
                            String svcScopeConfigId = chkAndCgoValue;
                            if(!StringUtil.isEmpty(svcScopeConfigId)){
                                HcsaSvcSubtypeOrSubsumedDto svcScopeConfigDto = svcScopeAlignMap.get(svcScopeConfigId);
                                /*if(targetChkDto != null && svcScopeConfigDto != null && ClinicalLaboratoryDelegator.PLEASEINDICATE.equals(svcScopeConfigDto.getName())){
                                    pleaseIndicateLabId = svcScopeConfigDto.getId();
                                    continue;
                                }*/
                                appSvcDisciplineAllocationDto.setPremiseVal(premisesValue);
                                appSvcDisciplineAllocationDto.setChkLstConfId(svcScopeConfigId);
                                String cgoIdNo = ParamUtil.getString(bpc.request, "cgo" + chkAndCgoName.toString());
                                appSvcDisciplineAllocationDto.setIdNo(cgoIdNo);
                                String slIndex = ParamUtil.getString(bpc.request, "sl" + chkAndCgoName.toString());
                                appSvcDisciplineAllocationDto.setSlIndex(slIndex);
                                daList.add(appSvcDisciplineAllocationDto);
                                if(targetChkDto != null && NewApplicationConstant.SERVICE_SCOPE_LAB_OTHERS.equals(svcScopeConfigDto.getName())){
                                    targetAllocationDto = (AppSvcDisciplineAllocationDto) CopyUtil.copyMutableObject(appSvcDisciplineAllocationDto);

                                }
                            }
                        }
                    }

                    if(targetChkDto != null && targetAllocationDto != null){
                        AppSvcChckListDto appSvcChckListDto = NewApplicationHelper.getSvcChckListDtoByConfigName(NewApplicationConstant.PLEASEINDICATE,appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList());
                        if(appSvcChckListDto != null){
                            targetAllocationDto.setChkLstConfId(appSvcChckListDto.getChkLstConfId());
                        }
                        daList.add(targetAllocationDto);
                    }
                }
            }
            currentSvcRelatedDto.setAppSvcDisciplineAllocationDtoList(daList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto);
        }
        String crud_action_additional = ParamUtil.getRequestString(bpc.request, "nextStep");
        if ("next".equals(crud_action_additional)) {
            appSubmissionService.doValidateDisciplineAllocation(errorMap, currentSvcRelatedDto.getAppSvcDisciplineAllocationDtoList(),
                    currentSvcRelatedDto);
            if (appSubmissionDto.isNeedEditController()) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                //clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_DISCIPLINE_ALLOCATION);
                appSubmissionDto.setClickEditPage(clickEditPages);
                reSetChangesForApp(appSubmissionDto);
            }
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);

        }

        if (!errorMap.isEmpty()) {
            //set audit
            bpc.request.setAttribute("errormapIs", "error");
            NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.DISCIPLINEALLOCATION);
            return;
        }

        log.debug(StringUtil.changeForLog("the do doDisciplineAllocation end ...."));
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
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        String action = ParamUtil.getRequestString(bpc.request, "nextStep");
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)
                    || RfcConst.RFC_BTN_OPTION_SKIP.equals(action)) {
                return;
            }
        }
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.REQUESTINFORMATIONCONFIG);
        boolean isRfi = false;
        if (requestInformationConfig != null) {
            isRfi = true;
        }
        String isEdit = ParamUtil.getString(bpc.request, NewApplicationDelegator.IS_EDIT);
        String isEditDpo = ParamUtil.getString(bpc.request, "isEditDpo");
        boolean isGetDataFromPagePo = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION, isEdit, isRfi);
        boolean isGetDataFromPageDpo = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION, isEditDpo, isRfi);
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        if (isGetDataFromPagePo || isGetDataFromPageDpo) {
            appSvcPrincipalOfficersDtoList = genAppSvcPrincipalOfficersDto(bpc.request, isGetDataFromPagePo, isGetDataFromPageDpo);
            if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType()) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())
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
        }
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        String svcCode = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE);
        Map<String, AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,
                NewApplicationDelegator.PERSONSELECTMAP);
        Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,
                NewApplicationDelegator.LICPERSONSELECTMAP);

        String crud_action_additional = ParamUtil.getRequestString(bpc.request, "nextStep");
        if ("next".equals(crud_action_additional)) {
            map = NewApplicationHelper.doValidatePo(appSvcPrincipalOfficersDtoList, licPersonMap, svcCode,
                    appSubmissionDto.getSubLicenseeDto());
            //validate mandatory count
            int poLength = 0;
            int dpoLength = 0;
            if (!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtoList)) {
                for (AppSvcPrincipalOfficersDto psnDto : appSvcPrincipalOfficersDtoList) {
                    String psnType = psnDto.getPsnType();
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)) {
                        poLength++;
                    } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnType)) {
                        dpoLength++;
                    }
                }
            }
            List<HcsaSvcPersonnelDto> poPsnConfig = serviceConfigService.getGOSelectInfo(currentSvcId,
                    ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
            List<HcsaSvcPersonnelDto> dpoPsnConfig = serviceConfigService.getGOSelectInfo(currentSvcId,
                    ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
            if (!isRfi) {
                map = NewApplicationHelper.psnMandatoryValidate(poPsnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_PO, map, poLength,
                        "poPsnMandatory", ApplicationConsts.PERSONNEL_PSN_TYPE_PRINCIPAL_OFFICER);
                if (dpoLength > 0) {
                    map = NewApplicationHelper.psnMandatoryValidate(dpoPsnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, map,
                            dpoLength, "dpoPsnMandatory", ApplicationConsts.PERSONNEL_PSN_TYPE_DEPUTY_PRINCIPAL_OFFICER);
                }
            }
            reSetChangesForApp(appSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
            if (map.isEmpty()) {
                //sync person dropdown and submisson dto
                personMap = syncDropDownAndPsn(personMap, appSubmissionDto, appSvcPrincipalOfficersDtoList, svcCode);
            }
        }
        if (!map.isEmpty()) {
            //set audit
            bpc.request.setAttribute("errormapIs", "error");
            NewApplicationHelper.setAudiErrMap(isRfi, appSubmissionDto.getAppType(), map, appSubmissionDto.getRfiAppNo(),
                    appSubmissionDto.getLicenceNo());
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,
                    HcsaLicenceFeConstant.PRINCIPALOFFICERS);
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(map));
        } else  if (isGetDataFromPagePo || isGetDataFromPageDpo) {
            //sync person dropdown and submisson dto
            personMap = syncDropDownAndPsn(personMap, appSubmissionDto, appSvcPrincipalOfficersDtoList, svcCode);
            //remove dirty psn dropdown info
            Map<String, AppSvcPersonAndExtDto> newPersonMap = removeDirtyDataFromPsnDropDown(appSubmissionDto, licPersonMap,
                    personMap);
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.PERSONSELECTMAP, (Serializable) newPersonMap);
            //remove dirty psn doc info
            List<AppSvcPrincipalOfficersDto> principalOfficersDtos = IaisCommonUtils.genNewArrayList();
            List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
            NewApplicationHelper.assignPoDpoDto(appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList(), principalOfficersDtos,
                    deputyPrincipalOfficersDtos);
            List<HcsaSvcDocConfigDto> svcDocConfigDtos = serviceConfigService.getAllHcsaSvcDocs(currentSvcId);
            //po
            appSvcRelatedInfoDto = removeDirtyPsnDoc(appSvcRelatedInfoDto, svcDocConfigDtos, principalOfficersDtos,
                    ApplicationConsts.DUP_FOR_PERSON_PO);
            //dpo
            appSvcRelatedInfoDto = removeDirtyPsnDoc(appSvcRelatedInfoDto, svcDocConfigDtos, deputyPrincipalOfficersDtos,
                    ApplicationConsts.DUP_FOR_PERSON_DPO);
        }
        setAppSvcRelatedInfoMap(bpc.request, currentSvcId, appSvcRelatedInfoDto, appSubmissionDto);
        log.debug(StringUtil.changeForLog("the do doPrincipalOfficers end ...."));
    }

    public void doKeyAppointmentHolder(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doKeyAppointmentHolder start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String action = ParamUtil.getRequestString(bpc.request, "nextStep");
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)
                    || RfcConst.RFC_BTN_OPTION_SKIP.equals(action)) {
                return;
            }
        }
        String isEdit = ParamUtil.getString(bpc.request, NewApplicationDelegator.IS_EDIT);
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.REQUESTINFORMATIONCONFIG);
        boolean isRfi = requestInformationConfig != null;
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION, isEdit, isRfi);
        log.info(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currentSvcRelatedDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        if (isGetDataFromPage) {
            List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderList = genAppSvcKeyAppointmentHolder(bpc.request, appType);
            currentSvcRelatedDto.setAppSvcKeyAppointmentHolderDtoList(appSvcKeyAppointmentHolderList);
            reSetChangesForApp(appSubmissionDto);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto, appSubmissionDto);
        }
        String nextStep = ParamUtil.getRequestString(bpc.request, "nextStep");
        String svcCode = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if ("next".equals(nextStep)) {
            Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.LICPERSONSELECTMAP);
            List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderList =
                    currentSvcRelatedDto.getAppSvcKeyAppointmentHolderDtoList();
            errorMap = NewApplicationHelper.doValidateKeyAppointmentHolder(appSvcKeyAppointmentHolderList,
                    licPersonMap, svcCode);
            //validate mandatory count
            int psnLength = 0;
            if (!IaisCommonUtils.isEmpty(appSvcKeyAppointmentHolderList)) {
                psnLength = appSvcKeyAppointmentHolderList.size();
            }
            List<HcsaSvcPersonnelDto> psnConfig = serviceConfigService.getGOSelectInfo(currentSvcId,
                    ApplicationConsts.PERSONNEL_PSN_KAH);
            if (!isRfi) {
                errorMap = NewApplicationHelper.psnMandatoryValidate(psnConfig, ApplicationConsts.PERSONNEL_PSN_KAH, errorMap,
                        psnLength, "psnMandatory", HcsaConsts.KEY_APPOINTMENT_HOLDER);
            }
        }
        if (!errorMap.isEmpty()) {
            //set audit
            bpc.request.setAttribute("errormapIs", "error");
            NewApplicationHelper.setAudiErrMap(isRfi, appSubmissionDto.getAppType(), errorMap, appSubmissionDto.getRfiAppNo(), appSubmissionDto.getLicenceNo());
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, AppServicesConsts.NAVTABS_SERVICEFORMS);
        } else if (isGetDataFromPage) {
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
        List<AppSvcDocDto> newAppSvcDocDtoList = IaisCommonUtils.genNewArrayList();
        String currSvcCode = (String) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.CURRENTSVCCODE);
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
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
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType()) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)
                    || RfcConst.RFC_BTN_OPTION_SKIP.equals(action)) {
                //clear
                ParamUtil.setSessionAttr(bpc.request, RELOADSVCDOC, null);
                return;
            }
        }

        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.REQUESTINFORMATIONCONFIG);
        boolean isRfi = false;
        if (requestInformationConfig != null) {
            isRfi = true;
        }
        String isEdit = ParamUtil.getString(mulReq, NewApplicationDelegator.IS_EDIT);
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        if (isGetDataFromPage) {
            Map<String, AppSvcDocDto> beforeReloadDocMap = (Map<String, AppSvcDocDto>) ParamUtil.getSessionAttr(bpc.request, RELOADSVCDOC);
            List<HcsaSvcDocConfigDto> svcDocConfigDtos = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.SVC_DOC_CONFIG);
            Map<String, CommonsMultipartFile> commonsMultipartFileMap = IaisCommonUtils.genNewHashMap();
            //CommonsMultipartFile file = null;
            AppSubmissionDto oldSubmissionDto = NewApplicationHelper.getOldAppSubmissionDto(bpc.request);
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos = null;
            String appGrpId = "";
            String appNo = "";
            if(oldSubmissionDto != null){
                oldAppSvcRelatedInfoDtos = oldSubmissionDto.getAppSvcRelatedInfoDtoList();
                appGrpId = oldSubmissionDto.getAppGrpId();
                appNo = oldSubmissionDto.getRfiAppNo();
            }
            List<AppSvcDocDto> oldDocs = IaisCommonUtils.genNewArrayList();
            if(!IaisCommonUtils.isEmpty(oldAppSvcRelatedInfoDtos)){
                for(AppSvcRelatedInfoDto oldSvcRelDto:oldAppSvcRelatedInfoDtos){
                    if(currentSvcId.equals(oldSvcRelDto.getServiceId())){
                        oldDocs = oldSvcRelDto.getAppSvcDocDtoLit();
                        break;
                    }
                }
            }
            if (appSubmissionDto.isNeedEditController()) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                //clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_DOCUMENT);
                appSubmissionDto.setClickEditPage(clickEditPages);
                reSetChangesForApp(appSubmissionDto);
            }

            List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.SVC_DOC_CONFIG);
            List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            //premIndexNo+configId+seqnum
            Map<String,File> saveFileMap = IaisCommonUtils.genNewHashMap();
            int maxPsnTypeNum = getMaxPersonTypeNumber(appSvcDocDtos,oldDocs);
            int [] psnTypeNumArr = new int[]{maxPsnTypeNum};
            for(int i =0;i<hcsaSvcDocConfigDtos.size();i++){
                HcsaSvcDocConfigDto hcsaSvcDocConfigDto = hcsaSvcDocConfigDtos.get(i);
                String dupForPrem = hcsaSvcDocConfigDto.getDupForPrem();
                if("0".equals(dupForPrem)){
                    String docKey = i+"svcDoc"+currSvcCode;
                    genSvcPersonDoc(hcsaSvcDocConfigDto,appSvcRelatedInfoDto,docKey,saveFileMap,appSvcDocDtos,newAppSvcDocDtoList,oldDocs,isRfi,appGrpId,appNo,"","",mulReq,bpc.request,psnTypeNumArr);
                }else if("1".equals(dupForPrem)){
                    for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                        String docKey = i+"svcDoc" + currSvcCode + appGrpPremisesDto.getPremisesIndexNo();
                        String premVal = appGrpPremisesDto.getPremisesIndexNo();
                        String premType = appGrpPremisesDto.getPremisesType();
                        genSvcPersonDoc(hcsaSvcDocConfigDto,appSvcRelatedInfoDto,docKey,saveFileMap,appSvcDocDtos,newAppSvcDocDtoList,oldDocs,isRfi,appGrpId,appNo,premVal,premType,mulReq,bpc.request,psnTypeNumArr);
                    }
                }
            }

            String crud_action_values = mulReq.getParameter("nextStep");
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
            if ("next".equals(crud_action_values)) {
                newAppSvcDocDtoList = doValidateSvcDocument(newAppSvcDocDtoList, errorMap, true);
                NewApplicationHelper.svcDocMandatoryValidate(svcDocConfigDtos,newAppSvcDocDtoList,appGrpPremisesDtos,appSvcRelatedInfoDto,errorMap);
                saveSvcFileAndSetFileId(newAppSvcDocDtoList,saveFileMap);
            }else{
                newAppSvcDocDtoList = doValidateSvcDocument(newAppSvcDocDtoList, errorMap,true);
                NewApplicationHelper.svcDocMandatoryValidate(svcDocConfigDtos,newAppSvcDocDtoList,appGrpPremisesDtos,appSvcRelatedInfoDto,errorMap);
                saveSvcFileAndSetFileId(newAppSvcDocDtoList,saveFileMap);
                errorMap = IaisCommonUtils.genNewHashMap();
            }

            appSvcRelatedInfoDto.setAppSvcDocDtoLit(newAppSvcDocDtoList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, appSvcRelatedInfoDto);

            if (!errorMap.isEmpty()) {
                bpc.request.setAttribute("errormapIs", "error");
                NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, AppServicesConsts.NAVTABS_SERVICEFORMS);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.DOCUMENTS);
                mulReq.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.DOCUMENTS);
                return;
            }
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
        if(errorMsg!=null){
            crudActionValue=null;
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
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        String currentSvcCode = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE);
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList = serviceConfigService.getGOSelectInfo(currentSvcId, ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL);
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
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        List<AppSvcPersonnelDto> appSvcPersonnelDtos = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
        if (appSvcPersonnelDtos != null && !appSvcPersonnelDtos.isEmpty()) {
            if (appSvcPersonnelDtos.size() > mandatory) {
                mandatory = appSvcPersonnelDtos.size();
            }
            boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
            String appType = appSubmissionDto.getAppType();
            if(isRfi || (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType))){
                log.debug(StringUtil.changeForLog("cycle cgo dto to retrieve prs info start ..."));
                log.debug("prs server flag {}",prsFlag);
                if("Y".equals(prsFlag)){
                    for(int i=0;i<appSvcPersonnelDtos.size();i++ ){
                        AppSvcPersonnelDto appSvcPersonDto = appSvcPersonnelDtos.get(i);
                        String profRegNo = appSvcPersonDto.getProfRegNo();
                        ProfessionalResponseDto professionalResponseDto = appSubmissionService.retrievePrsInfo(profRegNo);
                        if(professionalResponseDto != null){
                            String name = appSvcPersonDto.getName();
                            if(!StringUtil.isEmpty(name)){
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
        List<SelectOption> personnelTypeSel = genPersonnelTypeSel(currentSvcCode);
        ParamUtil.setRequestAttr(bpc.request, SERVICEPERSONNELTYPE, personnelTypeSel);

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
        log.debug(StringUtil.changeForLog("the do doServicePersonnel start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String action = ParamUtil.getRequestString(bpc.request, "nextStep");
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)
                    || RfcConst.RFC_BTN_OPTION_SKIP.equals(action)) {
                return;
            }
        }
        String isEdit = ParamUtil.getString(bpc.request, NewApplicationDelegator.IS_EDIT);
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION, isEdit, isRfi);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String nextStep = ParamUtil.getRequestString(bpc.request, "nextStep");
        if (isGetDataFromPage) {
            String currentSvcCod = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE);
            List<String> personnelTypeList = IaisCommonUtils.genNewArrayList();
            List<SelectOption> personnelTypeSel = genPersonnelTypeSel(currentSvcCod);
            for (SelectOption sp : personnelTypeSel) {
                personnelTypeList.add(sp.getValue());
            }
            List<AppSvcPersonnelDto> appSvcPersonnelDtos = genAppSvcPersonnelDtoList(bpc.request, personnelTypeList, currentSvcCod);

            log.debug(StringUtil.changeForLog("cycle cgo dto to retrieve prs info start ..."));
            log.debug("prs server flag {}",prsFlag);
            if("Y".equals(prsFlag) && !IaisCommonUtils.isEmpty(appSvcPersonnelDtos)){
                for(int i=0;i<appSvcPersonnelDtos.size();i++ ){
                    AppSvcPersonnelDto appSvcPersonDto = appSvcPersonnelDtos.get(i);
                    String profRegNo = appSvcPersonDto.getProfRegNo();
                    ProfessionalResponseDto professionalResponseDto = appSubmissionService.retrievePrsInfo(profRegNo);
                    if(professionalResponseDto != null){
                        String name = appSvcPersonDto.getName();
                        if(!StringUtil.isEmpty(name)){
                            appSvcPersonDto.setPrsLoading(true);
                        }
                    }
                }
            }
            log.debug(StringUtil.changeForLog("cycle cgo dto to retrieve prs info end ..."));

            appSvcRelatedInfoDto.setAppSvcPersonnelDtoList(appSvcPersonnelDtos);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, appSvcRelatedInfoDto);
            if (!StringUtil.isEmpty(nextStep)) {
                doValidatetionServicePerson(errorMap, appSvcPersonnelDtos, currentSvcCod);
                //validate mandatory count
                int psnLength = 0;
                if(!IaisCommonUtils.isEmpty(appSvcPersonnelDtos)){
                    psnLength = appSvcPersonnelDtos.size();
                }
                List<HcsaSvcPersonnelDto> psnConfig = serviceConfigService.getGOSelectInfo(currentSvcId, ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL);
                if(!isRfi){
                    errorMap = NewApplicationHelper.psnMandatoryValidate(psnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL, errorMap, psnLength, "psnMandatory", ApplicationConsts.PERSONNEL_PSN_TYPE_SVC);
                }

                errorMap = servicePersonPrsValidate(bpc.request,errorMap,appSvcRelatedInfoDto.getAppSvcPersonnelDtoList());
                if (appSubmissionDto.isNeedEditController()) {
                    Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                    //clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_SERVICE_PERSONNEL);
                    appSubmissionDto.setClickEditPage(clickEditPages);
                }
                ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
            }
            //
            //remove dirty psn doc info
            List<HcsaSvcDocConfigDto> svcDocConfigDtos = serviceConfigService.getAllHcsaSvcDocs(currentSvcId);
            List<AppSvcPrincipalOfficersDto> spList = IaisCommonUtils.genNewArrayList();
            List<AppSvcPersonnelDto> appSvcPersonnelDtosList = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcPersonnelDtosList)){
                for(AppSvcPersonnelDto appSvcPersonnelDto:appSvcPersonnelDtosList){
                    AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
                    String psnIndexNo = appSvcPersonnelDto.getIndexNo();
                    if(!StringUtil.isEmpty(psnIndexNo)){
                        appSvcPrincipalOfficersDto.setIndexNo(psnIndexNo);
                        spList.add(appSvcPrincipalOfficersDto);
                    }
                }
            }
            appSvcRelatedInfoDto = removeDirtyPsnDoc(appSvcRelatedInfoDto,svcDocConfigDtos,spList,ApplicationConsts.DUP_FOR_PERSON_SVCPSN);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, appSvcRelatedInfoDto);

            if (!errorMap.isEmpty() && "next".equals(nextStep)) {
                //set audit
                bpc.request.setAttribute("errormapIs", "error");
                NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.NUCLEARMEDICINEIMAGING);
                return;
            }
        }else{
            if(!isRfi){
                //validate mandatory count
                int psnLength = 0;
                List<AppSvcPersonnelDto> appSvcPersonnelDtos = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
                if(!IaisCommonUtils.isEmpty(appSvcPersonnelDtos)){
                    psnLength = appSvcPersonnelDtos.size();
                }
                List<HcsaSvcPersonnelDto> psnConfig = serviceConfigService.getGOSelectInfo(currentSvcId, ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL);
                errorMap = NewApplicationHelper.psnMandatoryValidate(psnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL, errorMap, psnLength, "psnMandatory", ApplicationConsts.PERSONNEL_PSN_TYPE_SVC);
            }
            errorMap = servicePersonPrsValidate(bpc.request,errorMap,appSvcRelatedInfoDto.getAppSvcPersonnelDtoList());
            if(!StringUtil.isEmpty(nextStep) && !errorMap.isEmpty() && "next".equals(nextStep)){
                NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.NUCLEARMEDICINEIMAGING);
            }
        }
        log.debug(StringUtil.changeForLog("the do doServicePersonnel end ...."));
    }

    /**
     * StartStep: prePareMedAlertPerson
     *
     * @param bpc
     * @throws
     */
    public void prePareMedAlertPerson(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prePareMedAlertPerson start ...."));
        Map<String, List<HcsaSvcPersonnelDto>> svcConfigInfo = (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.SERVICEALLPSNCONFIGMAP);
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        //min and max count
        int mandatoryCount = 0;
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList = serviceConfigService.getGOSelectInfo(currentSvcId, ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
        if (hcsaSvcPersonnelList != null && hcsaSvcPersonnelList.size() > 0) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = hcsaSvcPersonnelList.get(0);
            mandatoryCount = hcsaSvcPersonnelDto.getMandatoryCount();
            ParamUtil.setSessionAttr(bpc.request, "mapHcsaSvcPersonnel", hcsaSvcPersonnelDto);
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        List<AppSvcPrincipalOfficersDto> medAlertPsnDtos = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
        ParamUtil.setRequestAttr(bpc.request, "mandatoryCount", mandatoryCount);
        List<SelectOption> idTypeSelectList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ID_TYPE);
        ParamUtil.setRequestAttr(bpc.request, DROPWOWN_IDTYPESELECT, idTypeSelectList);
        List<SelectOption> assignSelectList = NewApplicationHelper.genAssignPersonSel(bpc.request, true);
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
        String currentSvcId = getCurrentServiceId(bpc.request);
        AppSvcRelatedInfoDto currentSvcRelatedDto = getAppSvcRelatedInfo(appSubmissionDto, currentSvcId, null);
        String isEdit = ParamUtil.getString(bpc.request, NewApplicationDelegator.IS_EDIT);
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto,
                ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION, isEdit, isRfi);
        log.info(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = currentSvcRelatedDto.getAppSvcMedAlertPersonList();
        if (isGetDataFromPage) {
            appSvcMedAlertPersonList = genAppSvcMedAlertPerson(bpc.request);
            currentSvcRelatedDto.setAppSvcMedAlertPersonList(appSvcMedAlertPersonList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto, appSubmissionDto);
        }
        String nextStep = ParamUtil.getRequestString(bpc.request, "nextStep");
        String svcCode = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE);
        Map<String, AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,
                NewApplicationDelegator.PERSONSELECTMAP);
        Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,
                NewApplicationDelegator.LICPERSONSELECTMAP);
        if ("next".equals(nextStep)) {
            reSetChangesForApp(appSubmissionDto);
            errorMap = NewApplicationHelper.doValidateMedAlertPsn(appSvcMedAlertPersonList, licPersonMap, svcCode);
            //validate mandatory count
            int psnLength = 0;
            if (!IaisCommonUtils.isEmpty(appSvcMedAlertPersonList)) {
                psnLength = appSvcMedAlertPersonList.size();
            }
            List<HcsaSvcPersonnelDto> psnConfig = serviceConfigService.getGOSelectInfo(currentSvcId,
                    ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
            if (!isRfi) {
                errorMap = NewApplicationHelper.psnMandatoryValidate(psnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, errorMap,
                        psnLength, "psnMandatory", ApplicationConsts.PERSONNEL_PSN_TYPE_MEDALERT);
            }
        }
        if (!errorMap.isEmpty()) {
            //set audit
            bpc.request.setAttribute("errormapIs", "error");
            NewApplicationHelper.setAudiErrMap(isRfi, appSubmissionDto.getAppType(), errorMap, appSubmissionDto.getRfiAppNo(),
                    appSubmissionDto.getLicenceNo());
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.MEDALERT_PERSON);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, AppServicesConsts.NAVTABS_SERVICEFORMS);
            Map<String, AppSvcPersonAndExtDto> newPersonMap = removeDirtyDataFromPsnDropDown(appSubmissionDto, licPersonMap,
                    personMap);
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.PERSONSELECTMAP, (Serializable) newPersonMap);
            return;
        } else if (isGetDataFromPage) {
            //sync person dropdown and submisson dto
            personMap = syncDropDownAndPsn(personMap, appSubmissionDto, appSvcMedAlertPersonList, svcCode);
            //remove dirty psn dropdown info
            Map<String, AppSvcPersonAndExtDto> newPersonMap = removeDirtyDataFromPsnDropDown(appSubmissionDto, licPersonMap,
                    personMap);
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.PERSONSELECTMAP, (Serializable) newPersonMap);
            //remove dirty psn doc info
            List<HcsaSvcDocConfigDto> svcDocConfigDtos = serviceConfigService.getAllHcsaSvcDocs(currentSvcId);
            currentSvcRelatedDto = removeDirtyPsnDoc(currentSvcRelatedDto, svcDocConfigDtos, appSvcMedAlertPersonList,
                    ApplicationConsts.DUP_FOR_PERSON_MAP);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto, appSubmissionDto);
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
        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = getAppSvcRelatedInfo(bpc.request,currSvcId);
        //vehicle config
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList = serviceConfigService.getGOSelectInfo(currSvcId, ApplicationConsts.PERSONNEL_VEHICLES);
        if (hcsaSvcPersonnelList != null && hcsaSvcPersonnelList.size() > 0) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = hcsaSvcPersonnelList.get(0);
            ParamUtil.setRequestAttr(bpc.request, VEHICLECONFIGDTO, hcsaSvcPersonnelDto);
        }

        List<AppSvcVehicleDto> appSvcVehicleDtos = currSvcInfoDto.getAppSvcVehicleDtoList();
        ParamUtil.setRequestAttr(bpc.request,VEHICLEDTOLIST,appSvcVehicleDtos);

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
        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = getAppSvcRelatedInfo(bpc.request,currSvcId);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String isEdit = ParamUtil.getString(bpc.request, NewApplicationDelegator.IS_EDIT);
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        if (isGetDataFromPage && !isRfi) {
            //get data from page
            List<AppSvcVehicleDto> appSvcVehicleDtos = genAppSvcVehicleDto(bpc.request, appSubmissionDto.getAppType());
            currSvcInfoDto.setAppSvcVehicleDtoList(appSvcVehicleDtos);
            setAppSvcRelatedInfoMap(bpc.request, currSvcId, currSvcInfoDto);
        }

        String crud_action_type = ParamUtil.getRequestString(bpc.request, "nextStep");
        Map<String,String> map=new HashMap<>();
        if("next".equals(crud_action_type)){
            List<AppSvcVehicleDto> appSvcVehicleDtos =IaisCommonUtils.genNewArrayList();
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
            List<String> appIds = NewApplicationHelper.getRelatedAppId(currSvcInfoDto.getAppId(), appSubmissionDto.getLicenceId(),
                    currSvcInfoDto.getServiceName());
            log.info(StringUtil.changeForLog("The current related application id: " + appIds));
            List<AppSvcVehicleDto> oldAppSvcVehicleDto = appSubmissionService.getActiveVehicles(appIds);
            validateVehicle.doValidateVehicles(map, appSvcVehicleDtos, currSvcInfoDto.getAppSvcVehicleDtoList(), oldAppSvcVehicleDto);
        }
        if(!map.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(map));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.VEHICLES);
        }
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

        String currSvcCode = (String) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.CURRENTSVCCODE);
        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = getAppSvcRelatedInfo(bpc.request,currSvcId);
        // Clinical Director config
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList = serviceConfigService.getGOSelectInfo(currSvcId, ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR);
        if (hcsaSvcPersonnelList != null && hcsaSvcPersonnelList.size() > 0) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = hcsaSvcPersonnelList.get(0);
            ParamUtil.setRequestAttr(bpc.request, CLINICALDIRECTORCONFIG, hcsaSvcPersonnelDto);
        }
        List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos = currSvcInfoDto.getAppSvcClinicalDirectorDtoList();
        ParamUtil.setRequestAttr(bpc.request,CLINICALDIRECTORDTOLIST,appSvcClinicalDirectorDtos);
        List<SelectOption> easMtsSpecialtySelectList = NewApplicationHelper.genEasMtsSpecialtySelectList(currSvcCode);
        ParamUtil.setRequestAttr(bpc.request,EASMTSSPECIALTYSELECTLIST,easMtsSpecialtySelectList);
        // Assgined person dropdown options
        ParamUtil.setRequestAttr(bpc.request, PERSON_OPTIONS, NewApplicationHelper.genAssignPersonSel(bpc.request, true));
        List<SelectOption> designationOpList = NewApplicationHelper.genDesignationOpList(true);
        ParamUtil.setRequestAttr(bpc.request, "designationOpList", designationOpList);
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
        String currSvcId = getCurrentServiceId(bpc.request);
        AppSvcRelatedInfoDto currSvcInfoDto = getAppSvcRelatedInfo(bpc.request,currSvcId);
        String isEdit = ParamUtil.getString(bpc.request, NewApplicationDelegator.IS_EDIT);
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.REQUESTINFORMATIONCONFIG);
        boolean isRfi = false;
        if (requestInformationConfig != null) {
            isRfi = true;
        }
        String actionType = ParamUtil.getRequestString(bpc.request, "nextStep");
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        Map<String,String> map=new HashMap<>(17);
        if (isGetDataFromPage) {
            //get data from page
            List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos = genAppSvcClinicalDirectorDto(bpc.request, appSubmissionDto.getAppType());
            currSvcInfoDto.setAppSvcClinicalDirectorDtoList(appSvcClinicalDirectorDtos);
            log.debug(StringUtil.changeForLog("cycle cd dto to retrieve prs info start ..."));
            log.debug("prs server flag {}",prsFlag);
            String appType = appSubmissionDto.getAppType();
            if("Y".equals(prsFlag) && !IaisCommonUtils.isEmpty(appSvcClinicalDirectorDtos)){
                for(int i=0;i<appSvcClinicalDirectorDtos.size();i++ ){
                    AppSvcPrincipalOfficersDto appSvcPsnDto = appSvcClinicalDirectorDtos.get(i);
                    String profRegNo = appSvcPsnDto.getProfRegNo();
                    ProfessionalResponseDto professionalResponseDto = appSubmissionService.retrievePrsInfo(profRegNo);
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
                        if (professionalResponseDto.isHasException() || StringUtil.isNotEmpty(professionalResponseDto.getStatusCode())) {
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
                        if(!IaisCommonUtils.isEmpty(specialtyList)){
                            List<String> notNullList = IaisCommonUtils.genNewArrayList();
                            for(String value:specialtyList){
                                if(!StringUtil.isEmpty(value)){
                                    notNullList.add(value);
                                }
                            }
                            specialtyStr = String.join(",",notNullList);
                        }
                        List<String> entryDateSpecialist = professionalResponseDto.getEntryDateSpecialist();
                        if(entryDateSpecialist != null && entryDateSpecialist.size() > 0){
                            specialtyGetDateStr = entryDateSpecialist.get(0);
                        }
                        List<RegistrationDto> registrationDtos = professionalResponseDto.getRegistration();
                        if(registrationDtos != null && registrationDtos.size() > 0){
                            RegistrationDto registrationDto = registrationDtos.get(0);
                            typeOfCurrRegi = registrationDto.getRegistrationType();
                            currRegiDateStr = registrationDto.getRegStartDate();
                            praCerEndDateStr = registrationDto.getPcEndDate();
                            typeOfRegister = registrationDto.getRegisterType();
                        }
                        setClinicalDirectorPrsInfo(appSvcPsnDto, specialtyStr, specialtyGetDateStr, typeOfCurrRegi, currRegiDateStr, praCerEndDateStr, typeOfRegister);
                    } else if (!StringUtil.isEmpty(profRegNo)) {
                        setClinicalDirectorPrsInfo(appSvcPsnDto, specialtyStr, specialtyGetDateStr, typeOfCurrRegi, currRegiDateStr, praCerEndDateStr, typeOfRegister);
                    }
                }
                currSvcInfoDto.setAppSvcClinicalDirectorDtoList(appSvcClinicalDirectorDtos);
            }
            setAppSvcRelatedInfoMap(bpc.request, currSvcId, currSvcInfoDto, appSubmissionDto);
            log.debug(StringUtil.changeForLog("cycle cd dto to retrieve prs info end ..."));
        }
        String currSvcCode = (String) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.CURRENTSVCCODE);
        if("next".equals(actionType)){
            validateClincalDirector.doValidateClincalDirector(map,currSvcInfoDto.getAppSvcClinicalDirectorDtoList(), currSvcCode);
        }
        if (!map.isEmpty()) {
            bpc.request.setAttribute("errormapIs", "error");
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(map));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,
                    HcsaLicenceFeConstant.CLINICAL_DIRECTOR);
        } else if (isGetDataFromPage) {
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
        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = getAppSvcRelatedInfo(bpc.request,currSvcId);
        //general charges config
        List<HcsaSvcPersonnelDto> generalChargesDtos = serviceConfigService.getGOSelectInfo(currSvcId, ApplicationConsts.PERSONNEL_CHARGES);
        if (generalChargesDtos != null && generalChargesDtos.size() > 0) {
            HcsaSvcPersonnelDto generalChargesDto = generalChargesDtos.get(0);
            ParamUtil.setRequestAttr(bpc.request, GENERALCHARGESCONFIG, generalChargesDto);
        }
        //other charges config
        List<HcsaSvcPersonnelDto> otherChargesDtos = serviceConfigService.getGOSelectInfo(currSvcId, ApplicationConsts.PERSONNEL_CHARGES_OTHER);
        if (otherChargesDtos != null && otherChargesDtos.size() > 0) {
            HcsaSvcPersonnelDto otherChargesDto = otherChargesDtos.get(0);
            ParamUtil.setRequestAttr(bpc.request, OTHERCHARGESCONFIG, otherChargesDto);
        }

        AppSvcChargesPageDto appSvcChargesPageDto = currSvcInfoDto.getAppSvcChargesPageDto();
        if(appSvcChargesPageDto != null){
            ParamUtil.setRequestAttr(bpc.request,GENERALCHARGESDTOLIST,appSvcChargesPageDto.getGeneralChargesDtos());
            ParamUtil.setRequestAttr(bpc.request,OTHERCHARGESDTOLIST,appSvcChargesPageDto.getOtherChargesDtos());
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
        String isEdit = ParamUtil.getString(bpc.request, NewApplicationDelegator.IS_EDIT);
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.REQUESTINFORMATIONCONFIG);
        boolean isRfi = false;
        if (requestInformationConfig != null) {
            isRfi = true;
        }
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = getAppSvcRelatedInfo(bpc.request,currSvcId);
        if (isGetDataFromPage) {
            //get data from page
            AppSvcChargesPageDto appSvcClinicalDirectorDto = genAppSvcChargesDto(bpc.request, appSubmissionDto.getAppType());
            currSvcInfoDto.setAppSvcChargesPageDto(appSvcClinicalDirectorDto);
            setAppSvcRelatedInfoMap(bpc.request, currSvcId, currSvcInfoDto);
            reSetChangesForApp(appSubmissionDto);
        }
        String crud_action_type = ParamUtil.getRequestString(bpc.request, "nextStep");
        Map<String,String> map=new HashMap<>(8);
        if("next".equals(crud_action_type)){
            validateCharges.doValidateCharges(map,currSvcInfoDto.getAppSvcChargesPageDto());
        }
        if(!map.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(map));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.CHARGES);
        }
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

        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = getAppSvcRelatedInfo(bpc.request,currSvcId);
        Map<String, AppSvcBusinessDto> premAlignBusinessMap = IaisCommonUtils.genNewHashMap();
        List<AppSvcBusinessDto> appSvcBusinessDtos = currSvcInfoDto.getAppSvcBusinessDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcBusinessDtos)){
            for(AppSvcBusinessDto appSvcBusinessDto:appSvcBusinessDtos){
                premAlignBusinessMap.put(appSvcBusinessDto.getPremIndexNo(), appSvcBusinessDto);
            }
        }
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        ParamUtil.setRequestAttr(bpc.request, "isRfi", isRfi);
        ParamUtil.setRequestAttr(bpc.request,PREMALIGNBUSINESSMAP, premAlignBusinessMap);

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

        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = getAppSvcRelatedInfo(bpc.request,currSvcId);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String isEdit = ParamUtil.getString(bpc.request, NewApplicationDelegator.IS_EDIT);
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.REQUESTINFORMATIONCONFIG);
        boolean isRfi = false;
        if (requestInformationConfig != null) {
            isRfi = true;
        }
        List<AppSvcBusinessDto> appSvcBusinessDtos = currSvcInfoDto.getAppSvcBusinessDtoList();
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        if (isGetDataFromPage) {
            //get data from page
            appSvcBusinessDtos = genAppSvcBusinessDtoList(bpc.request, appSubmissionDto.getAppGrpPremisesDtoList(), appSubmissionDto.getAppType());
            currSvcInfoDto.setAppSvcBusinessDtoList(appSvcBusinessDtos);
            setAppSvcRelatedInfoMap(bpc.request, currSvcId, currSvcInfoDto);
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crud_action_type = ParamUtil.getRequestString(bpc.request, "nextStep");
        if("next".equals(crud_action_type)){
            NewApplicationHelper.doValidateBusiness(appSvcBusinessDtos, appSubmissionDto.getAppType(),
                    appSubmissionDto.getLicenceId(), errorMap);
        }
        if(!errorMap.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaConsts.STEP_BUSINESS_NAME);
        }
        log.debug(StringUtil.changeForLog("do Business end ..."));
    }

    //=============================================================================
    //private method
    //=============================================================================

    private ServiceStepDto getServiceStepDto(ServiceStepDto serviceStepDto, String action, List<HcsaServiceDto> hcsaServiceDtoList, String svcId, AppSvcRelatedInfoDto appSvcRelatedInfoDto) {
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
                for (int i = 0; i < hcsaServiceStepSchemeDtos.size(); i++) {
                    if (action.equals(hcsaServiceStepSchemeDtos.get(i).getStepCode())) {
                        number = i;
                        if (HcsaLicenceFeConstant.DISCIPLINEALLOCATION.equals(action) && skipDisciplineAllocationPage(appSvcRelatedInfoDto)) {
                            if (currentNumber < i) {
                                number++;
                            } else if (currentNumber > i) {
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

    private boolean skipDisciplineAllocationPage(AppSvcRelatedInfoDto appSvcRelatedInfoDto) {
        boolean flag = false;
        if (appSvcRelatedInfoDto != null) {
            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtos = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcLaboratoryDisciplinesDtos)) {
                for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtos) {
                    List<AppSvcChckListDto> appSvcChckListDtos = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                    if (!IaisCommonUtils.isEmpty(appSvcChckListDtos)) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return !flag;
    }

    /*private void turn(List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos, Map<String, HcsaSvcSubtypeOrSubsumedDto> allCheckListMap) {

        for (HcsaSvcSubtypeOrSubsumedDto dto : hcsaSvcSubtypeOrSubsumedDtos) {
            allCheckListMap.put(dto.getId(), dto);
            if (dto.getList() != null && dto.getList().size() > 0) {
                turn(dto.getList(), allCheckListMap);
            }
        }

    }*/

    private static void doValidatetionServicePerson(Map<String, String> errorMap, List<AppSvcPersonnelDto> appSvcPersonnelDtos, String svcCode) {
        if(IaisCommonUtils.isEmpty(appSvcPersonnelDtos)){
            return;
        }
        String errName = MessageUtil.replaceMessage("GENERAL_ERR0006","Name","field");
        String errDesignation = MessageUtil.replaceMessage("GENERAL_ERR0006","Designation","field");
        String errRegnNo = MessageUtil.replaceMessage("GENERAL_ERR0006","Professional Regn. No.","field");
        String errWrkExpYear = MessageUtil.replaceMessage("GENERAL_ERR0006","Relevant working experience (Years)","field");
        String errQualification = MessageUtil.replaceMessage("GENERAL_ERR0006","Qualification","field");
        String errSelSvcPsnel = MessageUtil.replaceMessage("GENERAL_ERR0006","Select Service Personnel","field");
        String errOtherDesignation = MessageUtil.replaceMessage("GENERAL_ERR0006","Others Designation","field");

        String errLengthName = NewApplicationHelper.repLength("Name","66");
        String errLengthRegnNo = NewApplicationHelper.repLength("Professional Regn. No.","20");
        String errLengthWrkExpYear = NewApplicationHelper.repLength("Relevant working experience (Years)","2");
        String errLengthQualification = NewApplicationHelper.repLength("Qualification","100");
        String errLengthOtherDesignation = NewApplicationHelper.repLength("Others Designation","100");
        List<SelectOption> personnelTypeSel = genPersonnelTypeSel(svcCode);
        //Verify that each type of person has at least one
        //person type,value/empty
        Map<String,String> personCountMap = IaisCommonUtils.genNewHashMap();

        for (int i = 0; i < appSvcPersonnelDtos.size(); i++) {
            String personType = appSvcPersonnelDtos.get(i).getPersonnelType();
            if(!StringUtil.isEmpty(personType)){
                personCountMap.put(personType,AppConsts.YES);
            }
            if (AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode)) {
                String designation = appSvcPersonnelDtos.get(i).getDesignation();
                if (StringUtil.isEmpty(designation)) {
                    errorMap.put("designation" + i, errDesignation);
                }else if(NewApplicationConstant.DESIGNATION_OTHERS.equals(designation)){
                    String otherDesignation = appSvcPersonnelDtos.get(i).getOtherDesignation();
                    if(StringUtil.isEmpty(otherDesignation)){
                        errorMap.put("otherDesignation"+i,errOtherDesignation);
                    }else if(otherDesignation.length() > 100){
                        errorMap.put("otherDesignation" + i, errLengthOtherDesignation);
                    }
                }
                String name = appSvcPersonnelDtos.get(i).getName();
                if (StringUtil.isEmpty(name)) {
                    errorMap.put("name" + i, errName);
                }else if(name.length() > 66){
                    errorMap.put("name" + i, errLengthName);
                }
                String profRegNo = appSvcPersonnelDtos.get(i).getProfRegNo();
                if (StringUtil.isEmpty(profRegNo)) {
                    errorMap.put("regnNo" + i, errRegnNo);
                }else if(profRegNo.length() > 20){
                    errorMap.put("regnNo" + i, errLengthRegnNo);
                }
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                if (StringUtil.isEmpty(wrkExpYear)) {
                    errorMap.put("wrkExpYear" + i, errWrkExpYear);
                } else {
                    if(wrkExpYear.length() > 2){
                        errorMap.put("wrkExpYear" + i, errLengthWrkExpYear);
                    }
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        errorMap.put("wrkExpYear" + i, "GENERAL_ERR0002");
                    }
                }
            } else if (AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)) {
                String name = appSvcPersonnelDtos.get(i).getName();
                if (StringUtil.isEmpty(name)) {
                    errorMap.put("name" + i, errName);
                }else if(name.length() > 66){
                    errorMap.put("name" + i, errLengthName);
                }
                String quaification = appSvcPersonnelDtos.get(i).getQualification();
                if (StringUtil.isEmpty(quaification)) {
                    errorMap.put("qualification" + i, errQualification);
                }else if(quaification.length() > 100){
                    errorMap.put("qualification" + i, errLengthQualification);
                }
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                if (StringUtil.isEmpty(wrkExpYear)) {
                    errorMap.put("wrkExpYear" + i, errWrkExpYear);
                } else {
                    if(wrkExpYear.length() > 2){
                        errorMap.put("wrkExpYear" + i, errLengthWrkExpYear);
                    }
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        errorMap.put("wrkExpYear" + i, "GENERAL_ERR0002");
                    }
                }
            }else if(!AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)){
                String name = appSvcPersonnelDtos.get(i).getName();
                if (StringUtil.isEmpty(name)) {
                    errorMap.put("name" + i, errName);
                }else if(name.length() > 66){
                    errorMap.put("name" + i, errLengthName);
                }
                String quaification = appSvcPersonnelDtos.get(i).getQualification();
                if (StringUtil.isEmpty(quaification)) {
                    errorMap.put("qualification" + i, errQualification);
                }else if(quaification.length() > 100){
                    errorMap.put("qualification" + i, errLengthQualification);
                }
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                if (StringUtil.isEmpty(wrkExpYear)) {
                    errorMap.put("wrkExpYear" + i, errWrkExpYear);
                } else {
                    if(wrkExpYear.length() > 2){
                        errorMap.put("wrkExpYear" + i, errLengthWrkExpYear);
                    }
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        errorMap.put("wrkExpYear" + i, "GENERAL_ERR0002");
                    }
                }
            } else {
                String personnelSel = appSvcPersonnelDtos.get(i).getPersonnelType();
                if (StringUtils.isEmpty(personnelSel)) {
                    errorMap.put("personnelSelErrorMsg" + i, errSelSvcPsnel);
                }

                if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE.equals(personnelSel)) {
                    String profRegNo = appSvcPersonnelDtos.get(i).getProfRegNo();
                    String name = appSvcPersonnelDtos.get(i).getName();
                    if (StringUtil.isEmpty(name)) {
                        errorMap.put("name" + i, errName);
                    }else if(name.length() > 66){
                        errorMap.put("name" + i, errLengthName);
                    }
                    if (StringUtil.isEmpty(profRegNo)) {
                        errorMap.put("regnNo" + i, errRegnNo);
                    }else if(profRegNo.length() > 20){
                        errorMap.put("regnNo" + i, errLengthRegnNo);
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    String designation = appSvcPersonnelDtos.get(i).getDesignation();
                    String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                    String qualification = appSvcPersonnelDtos.get(i).getQualification();

                    if (StringUtil.isEmpty(name)) {
                        errorMap.put("name" + i, errName);
                    }else if(name.length() > 66){
                        errorMap.put("name" + i, errLengthName);
                    }
                    if (StringUtil.isEmpty(designation)) {
                        errorMap.put("designation" + i, errDesignation);
                    }else if(NewApplicationConstant.DESIGNATION_OTHERS.equals(designation)){
                        String otherDesignation = appSvcPersonnelDtos.get(i).getOtherDesignation();
                        if(StringUtil.isEmpty(otherDesignation)){
                            errorMap.put("otherDesignation"+i,errOtherDesignation);
                        }else if(otherDesignation.length() > 100){
                            errorMap.put("otherDesignation" + i, errLengthOtherDesignation);
                        }
                    }
                    if (StringUtil.isEmpty(wrkExpYear)) {
                        errorMap.put("wrkExpYear" + i, errWrkExpYear);
                    } else {
                        if(wrkExpYear.length() > 2){
                            errorMap.put("wrkExpYear" + i, errLengthWrkExpYear);
                        }
                        if (!wrkExpYear.matches("^[0-9]*$")) {
                            errorMap.put("wrkExpYear" + i, "GENERAL_ERR0002");
                        }
                    }
                    if (StringUtil.isEmpty(qualification)) {
                        errorMap.put("qualification" + i, errQualification);
                    }else if(qualification.length() > 100){
                        errorMap.put("qualification" + i, errLengthQualification);
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                    String quaification = appSvcPersonnelDtos.get(i).getQualification();
                    if (StringUtil.isEmpty(name)) {
                        errorMap.put("name" + i, errName);
                    }else if(name.length() > 66){
                        errorMap.put("name" + i, errLengthName);
                    }
                    if (StringUtil.isEmpty(wrkExpYear)) {
                        errorMap.put("wrkExpYear" + i, errWrkExpYear);
                    } else {
                        if(wrkExpYear.length() > 2){
                            errorMap.put("wrkExpYear" + i, errLengthWrkExpYear);
                        }
                        if (!wrkExpYear.matches("^[0-9]*$")) {
                            errorMap.put("wrkExpYear" + i, "GENERAL_ERR0002");
                        }
                    }
                    if (StringUtil.isEmpty(quaification)) {
                        errorMap.put("qualification" + i, errQualification);
                    }else if(quaification.length() > 100){
                        errorMap.put("qualification" + i, errLengthQualification);
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    if (StringUtil.isEmpty(name)) {
                        errorMap.put("name" + i, errName);
                    }else if(name.length() > 66){
                        errorMap.put("name" + i, errLengthName);
                    }
                }
            }

        }
    }

    private List<AppSvcPrincipalOfficersDto> genAppSvcCgoDto(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("genAppSvcCgoDto start ...."));
        ParamUtil.setSessionAttr(request, ERRORMAP_GOVERNANCEOFFICERS, null);
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = IaisCommonUtils.genNewArrayList();
        AppSvcPrincipalOfficersDto appSvcCgoDto;
        String[] assignSelect = ParamUtil.getStrings(request, "assignSelect");
        int size = 0;
        if (assignSelect != null && assignSelect.length > 0) {
            size = assignSelect.length;
        }
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        String appType = "";
        if (appSubmissionDto != null) {
            appType = appSubmissionDto.getAppType();
        }
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(request, currentSvcId);
        //indexNo
        String[] indexNos = ParamUtil.getStrings(request, "indexNo");
        boolean rfcOrRenew = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType);
        boolean isRfi = NewApplicationHelper.checkIsRfi(request);
        boolean needEdit = rfcOrRenew || isRfi;
        if (needEdit) {
            if(indexNos == null){
                size = 0;
            }else{
                size = indexNos.length;
            }
        }
        String[] existingPsn = ParamUtil.getStrings(request, "existingPsn");
        String[] licPerson = ParamUtil.getStrings(request, "licPerson");
        String[] isPartEdit = ParamUtil.getStrings(request, "isPartEdit");
        //form display data
        String[] salutation = ParamUtil.getStrings(request, "salutation");
        String[] name = ParamUtil.getStrings(request, "name");
        String[] idType = ParamUtil.getStrings(request, "idType");
        String[] idNo = ParamUtil.getStrings(request, "idNo");
        String[] designation = ParamUtil.getStrings(request, "designation");
        String[] otherDesignations = ParamUtil.getStrings(request, "otherDesignation");
        String[] professionType = ParamUtil.getStrings(request, "professionType");
        String[] professionRegoNo = ParamUtil.getStrings(request, "professionRegoNo");
        //String[] specialty = ParamUtil.getStrings(request, "specialty");
        //String[] specialtyOther = ParamUtil.getStrings(request, "specialtyOther");
        //String[] qualification = ParamUtil.getStrings(request, "qualification");
        String[] otherQualification = ParamUtil.getStrings(request, "otherQualification");
        String[] mobileNo = ParamUtil.getStrings(request, "mobileNo");
        String[] emailAddress = ParamUtil.getStrings(request, "emailAddress");
        //new and not rfi
        for (int i = 0; i < size; i++) {
            AppPsnEditDto appPsnEditDto = new AppPsnEditDto();
            boolean chooseExisting = false;
            boolean getPageData = false;
            appSvcCgoDto = new AppSvcPrincipalOfficersDto();
            String indexNo = indexNos[i];
            String assign = assignSelect[i];
            String licPsn = licPerson[i];
            if(!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                if (assign != null) {
                    if(isExistingPsn(assign,licPsn)){
                        chooseExisting = true;
                    }else{
                        getPageData = true;
                    }
                }
            }else if(needEdit){
                if (assign != null) {
                    if (!StringUtil.isEmpty(indexNo)) {
                        //not click edit
                        if (AppConsts.NO.equals(isPartEdit[i])) {
                            appSvcCgoDto = getAppSvcCgoByIndexNo(appSvcRelatedInfoDto, indexNo);
                            appSvcCgoDtoList.add(appSvcCgoDto);
                            //change arr
                            indexNos = removeArrIndex(indexNos, i);
                            isPartEdit = removeArrIndex(isPartEdit, i);
                            licPerson = removeArrIndex(licPerson, i);
                            //dropdown cannot disabled
                            assignSelect = removeArrIndex(assignSelect, i);
                            salutation = removeArrIndex(salutation, i);
                            idType = removeArrIndex(idType, i);
                            designation = removeArrIndex(designation, i);
                            professionType = removeArrIndex(professionType, i);
                            //specialty = removeArrIndex(specialty, i);
                            existingPsn = removeArrIndex(existingPsn, i);
                            //specialtyOther = removeArrIndex(specialtyOther,i);
                            //change arr index
                            --i;
                            --size;
                            continue;
                        }
                    }
                    //isPartEdit->1.click edit button 2.add more psn
                    if(isExistingPsn(assign,licPsn)){
                        //add cgo and choose existing
                        chooseExisting = true;
                    }else{
                        getPageData = true;
                    }

                }
            }else {
                log.info(StringUtil.changeForLog("The current type is not supported"));
            }
            log.info(StringUtil.changeForLog("chooseExisting:"+chooseExisting));
            log.info(StringUtil.changeForLog("getPageData:"+getPageData));
            if(chooseExisting){
                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = NewApplicationHelper.getPsnInfoFromLic(request, assignSelect[i]);
                try {
                    appPsnEditDto = NewApplicationHelper.setNeedEditField(appSvcPrincipalOfficersDto);
                } catch (Exception e) {
                    clearAppPsnEditDto(appPsnEditDto);
                    log.error(e.getMessage(), e);
                }
                if(appPsnEditDto.isSalutation()){
                    NewApplicationHelper.setPsnValue(salutation,i,appSvcPrincipalOfficersDto,"salutation");
                }
                if(appPsnEditDto.isIdType()){
                    NewApplicationHelper.setPsnValue(idType,i,appSvcPrincipalOfficersDto,"idType");
                }
                if(appPsnEditDto.isDesignation()){
                    NewApplicationHelper.setPsnValue(designation,i,appSvcPrincipalOfficersDto,"designation");
                }
                if(appPsnEditDto.isOtherDesignation()){
                    if(MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(appSvcPrincipalOfficersDto.getDesignation())){
                        NewApplicationHelper.setPsnValue(otherDesignations,i,appSvcPrincipalOfficersDto,"otherDesignation");
                    }else {
                        otherDesignations = removeArrIndex(otherDesignations, i);
                    }
                }
                if(appPsnEditDto.isProfessionType()){
                    NewApplicationHelper.setPsnValue(professionType,i,appSvcPrincipalOfficersDto,"professionType");
                }

                //input
                if(appPsnEditDto.isName()){
                    name = NewApplicationHelper.setPsnValue(name,i,appSvcPrincipalOfficersDto,"name");
                }
                if(appPsnEditDto.isIdNo()){
                    idNo = NewApplicationHelper.setPsnValue(idNo,i,appSvcPrincipalOfficersDto,"idNo");
                }
                if(appPsnEditDto.isMobileNo()){
                    mobileNo = NewApplicationHelper.setPsnValue(mobileNo,i,appSvcPrincipalOfficersDto,"mobileNo");
                }
                if(appPsnEditDto.isProfRegNo()){
                    professionRegoNo = NewApplicationHelper.setPsnValue(professionRegoNo,i,appSvcPrincipalOfficersDto,"profRegNo");
                }
                if(appPsnEditDto.isOtherQualification()){
                    otherQualification = NewApplicationHelper.setPsnValue(otherQualification,i,appSvcPrincipalOfficersDto,"otherQualification");
                }
                if(appPsnEditDto.isEmailAddr()){
                    emailAddress = NewApplicationHelper.setPsnValue(emailAddress,i,appSvcPrincipalOfficersDto,"emailAddr");
                }
                appSvcCgoDto = (AppSvcPrincipalOfficersDto) CopyUtil.copyMutableObject(appSvcPrincipalOfficersDto);
                appSvcCgoDto.setAssignSelect(assignSelect[i]);
                appSvcCgoDto.setLicPerson(true);
                appSvcCgoDto.setSelectDropDown(true);
                if(!StringUtil.isEmpty(indexNo)){
                    appSvcCgoDto.setIndexNo(indexNo);
                }else if (StringUtil.isEmpty(appSvcCgoDto.getIndexNo())) {
                    appSvcCgoDto.setIndexNo(UUID.randomUUID().toString());
                }
                //
                boolean needSpcOptList = appSvcCgoDto.isNeedSpcOptList();
                if(needSpcOptList){
                    Map<String,String> specialtyAttr = IaisCommonUtils.genNewHashMap();
                    specialtyAttr.put("name", "specialty");
                    specialtyAttr.put("class", "specialty");
                    specialtyAttr.put("style", "display: none;");
                    List<SelectOption> spcOpts = appSvcCgoDto.getSpcOptList();
                    String specialtySelectStr = NewApplicationHelper.generateDropDownHtml(specialtyAttr, spcOpts, null, appSvcCgoDto.getSpeciality());
                    appSvcCgoDto.setSpecialityHtml(specialtySelectStr);
                }
                appSvcCgoDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
                appSvcCgoDtoList.add(appSvcCgoDto);
                //change arr index
                indexNos = removeArrIndex(indexNos, i);
                isPartEdit = removeArrIndex(isPartEdit, i);
                licPerson = removeArrIndex(licPerson, i);
                existingPsn = removeArrIndex(existingPsn, i);
                //dropdown cannot disabled
                assignSelect = removeArrIndex(assignSelect, i);
                salutation = removeArrIndex(salutation, i);
                idType = removeArrIndex(idType, i);
                designation = removeArrIndex(designation, i);
                professionType = removeArrIndex(professionType, i);
                //specialty = removeArrIndex(specialty, i);
                --i;
                --size;
            }else if(getPageData){
                if (StringUtil.isEmpty(indexNo)) {
                    appSvcCgoDto.setIndexNo(UUID.randomUUID().toString());
                } else {
                    appSvcCgoDto.setIndexNo(indexNos[i]);
                }
                appSvcCgoDto.setAssignSelect(assignSelect[i]);
                appSvcCgoDto.setSalutation(salutation[i]);
                appSvcCgoDto.setName(name[i]);
                appSvcCgoDto.setIdType(idType[i]);
                appSvcCgoDto.setIdNo(StringUtil.toUpperCase(idNo[i]));
                appSvcCgoDto.setDesignation(designation[i]);
                if(MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation[i])){
                    appSvcCgoDto.setOtherDesignation(otherDesignations[i]);
                }
                appSvcCgoDto.setProfessionType(professionType[i]);
                appSvcCgoDto.setProfRegNo(professionRegoNo[i]);
                appSvcCgoDto.setOtherQualification(otherQualification[i]);
                appSvcCgoDto.setMobileNo(mobileNo[i]);
                String emailAddr = "";
                if(emailAddress != null){
                    if(!StringUtil.isEmpty(emailAddress[i])){
                        emailAddr = StringUtil.viewHtml(emailAddress[i]);
                    }
                }
                appSvcCgoDto.setEmailAddr(emailAddr);
                if (needEdit && AppConsts.YES.equals(licPerson[i])) {
                    appSvcCgoDto.setLicPerson(true);
                    AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = NewApplicationHelper.getPsnInfoFromLic(request, NewApplicationHelper.getPersonKey(appSvcCgoDto.getIdType(),appSvcCgoDto.getIdNo()));
                    if(appSvcPrincipalOfficersDto != null){
                        appSvcCgoDto.setCurPersonelId(appSvcPrincipalOfficersDto.getCurPersonelId());
                    }
                }
                appSvcCgoDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
                appSvcCgoDtoList.add(appSvcCgoDto);
            }
        }
        ParamUtil.setSessionAttr(request, GOVERNANCEOFFICERSDTOLIST, (Serializable) appSvcCgoDtoList);
        log.info(StringUtil.changeForLog("genAppSvcCgoDto end ...."));
        return appSvcCgoDtoList;
    }

    private List<AppSvcVehicleDto> genAppSvcVehicleDto(HttpServletRequest request, String appType){
        List<AppSvcVehicleDto> appSvcVehicleDtos = IaisCommonUtils.genNewArrayList();
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(request, currentSvcId);
        boolean isRfi = NewApplicationHelper.checkIsRfi(request);
        int vehicleLength = ParamUtil.getInt(request,"vehiclesLength");
        for(int i = 0; i < vehicleLength ; i++){
            boolean getDataByIndexNo = false;
            boolean getPageData = false;
            String isPartEdit = ParamUtil.getString(request,"isPartEdit"+i);
            String vehicleIndexNo = ParamUtil.getString(request,"vehicleIndexNo"+i);
            if(!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                getPageData = true;
            }else if(AppConsts.YES.equals(isPartEdit)){
                getPageData = true;
            }else if(!StringUtil.isEmpty(vehicleIndexNo)){
                getDataByIndexNo = true;
            }
            log.debug("get data by index no. is {}",getDataByIndexNo);
            log.debug("get page data is {}",getPageData);
            if(getDataByIndexNo){
                AppSvcVehicleDto appSvcVehicleDto = getAppSvcVehicleDtoByIndexNo(appSvcRelatedInfoDto, vehicleIndexNo);
                if(appSvcVehicleDto != null){
                    appSvcVehicleDtos.add(appSvcVehicleDto);
                }
            }else if(getPageData){
                String vehicleName = ParamUtil.getString(request,"vehicleName"+i);
                String chassisNum = ParamUtil.getString(request,"chassisNum"+i);
                String engineNum = ParamUtil.getString(request,"engineNum"+i);
                AppSvcVehicleDto appSvcVehicleDto = new AppSvcVehicleDto();
                appSvcVehicleDto.setVehicleNum(vehicleName);
                appSvcVehicleDto.setChassisNum(chassisNum);
                appSvcVehicleDto.setEngineNum(engineNum);
                appSvcVehicleDto.setDummyVehNum(StringUtil.isEmpty(vehicleName));
                String dummyVehNum = "";
                AppSvcVehicleDto oldAppSvcVehicleDto = getAppSvcVehicleDtoByIndexNo(appSvcRelatedInfoDto, vehicleIndexNo);
                if(oldAppSvcVehicleDto != null){
                    dummyVehNum = oldAppSvcVehicleDto.getVehicleName();
                }
                if (StringUtil.isEmpty(dummyVehNum)){
                    dummyVehNum = IaisEGPHelper.generateDummyVehicleNum(i);
                }
                appSvcVehicleDto.setVehicleName(dummyVehNum);
                if (appSvcVehicleDto.isDummyVehNum()){
                    appSvcVehicleDto.setVehicleNum("Vehicle_No_" + (i + 1));
                }
                if(StringUtil.isEmpty(vehicleIndexNo)){
                    appSvcVehicleDto.setVehicleIndexNo(UUID.randomUUID().toString());
                }else{
                    appSvcVehicleDto.setVehicleIndexNo(vehicleIndexNo);
                }
                appSvcVehicleDtos.add(appSvcVehicleDto);
            }
        }
        return appSvcVehicleDtos;
    }

    private AppSvcVehicleDto getAppSvcVehicleDtoByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo){
        AppSvcVehicleDto result = null;
        if(appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)){
            List<AppSvcVehicleDto> appSvcVehicleDtos = appSvcRelatedInfoDto.getAppSvcVehicleDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcVehicleDtos)){
                for(AppSvcVehicleDto appSvcVehicleDto:appSvcVehicleDtos){
                    if(indexNo.equals(appSvcVehicleDto.getVehicleIndexNo())){
                        result = appSvcVehicleDto;
                        break;
                    }
                }
            }
        }
        return result;
    }

    private AppSvcPrincipalOfficersDto getClinicalDirectorByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo){
        AppSvcPrincipalOfficersDto result = null;
        if(appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)){
            List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos = appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcClinicalDirectorDtos)){
                for(AppSvcPrincipalOfficersDto appSvcClinicalDirectorDto:appSvcClinicalDirectorDtos){
                    if(indexNo.equals(appSvcClinicalDirectorDto.getIndexNo())){
                        result = appSvcClinicalDirectorDto;
                        break;
                    }
                }
            }
        }
        return result;
    }

    private AppSvcChargesDto getChargesByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo, boolean isGeneral){
        AppSvcChargesDto result = null;
        if(appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)){
            AppSvcChargesPageDto appSvcChargesPageDto = appSvcRelatedInfoDto.getAppSvcChargesPageDto();
            if(appSvcChargesPageDto != null){
                if(isGeneral){
                    List<AppSvcChargesDto> generalChargesDtos = appSvcChargesPageDto.getGeneralChargesDtos();
                    result = getChargesByIndexNo(generalChargesDtos, indexNo);
                }else {
                    List<AppSvcChargesDto> otherChargesDtos = appSvcChargesPageDto.getOtherChargesDtos();
                    result = getChargesByIndexNo(otherChargesDtos, indexNo);
                }
            }
        }
        return result;
    }

    private AppSvcChargesDto getChargesByIndexNo(List<AppSvcChargesDto> chargesDtos, String indexNo){
        AppSvcChargesDto result = null;
        if(!IaisCommonUtils.isEmpty(chargesDtos) && !StringUtil.isEmpty(indexNo)){
            for(AppSvcChargesDto appSvcChargesDto:chargesDtos){
                if(indexNo.equals(appSvcChargesDto.getChargesIndexNo())){
                    result = appSvcChargesDto;
                    break;
                }
            }
        }
        return result;
    }

    private AppSvcPrincipalOfficersDto getKeyAppointmentHolderByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo){
        AppSvcPrincipalOfficersDto result = null;
        if(appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)){
            List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderDtoList = appSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcKeyAppointmentHolderDtoList)){
                for(AppSvcPrincipalOfficersDto appSvcKeyAppointmentHolder:appSvcKeyAppointmentHolderDtoList){
                    if(indexNo.equals(appSvcKeyAppointmentHolder.getIndexNo())){
                        result = appSvcKeyAppointmentHolder;
                        break;
                    }
                }
            }
        }
        return result;
    }

    private List<AppSvcPrincipalOfficersDto> genAppSvcClinicalDirectorDto(HttpServletRequest request, String appType){
        log.debug(StringUtil.changeForLog("gen app svc clinical director dto start ..."));
        String currSvcCode = (String) ParamUtil.getSessionAttr(request,NewApplicationDelegator.CURRENTSVCCODE);
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(request, currentSvcId);
        boolean isRfi = NewApplicationHelper.checkIsRfi(request);
        List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos = IaisCommonUtils.genNewArrayList();
        int cdLength = ParamUtil.getInt(request,"cdLength");
        for(int i = 0; i < cdLength ; i++){
            boolean getDataByIndexNo = false;
            boolean getPageData = false;
            String isPartEdit = ParamUtil.getString(request,"isPartEdit"+i);
            String cdIndexNo = ParamUtil.getString(request,"cdIndexNo"+i);
            if(!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                getPageData = true;
            }else if(AppConsts.YES.equals(isPartEdit)){
                getPageData = true;
            }else if(!StringUtil.isEmpty(cdIndexNo)){
                getDataByIndexNo = true;
            }
            log.debug("get data by index no. is {}",getDataByIndexNo);
            log.debug("get page data is {}",getPageData);
            if(getDataByIndexNo){
                AppSvcPrincipalOfficersDto appSvcClinicalDirectorDto = getClinicalDirectorByIndexNo(appSvcRelatedInfoDto, cdIndexNo);
                if(appSvcClinicalDirectorDto != null){
                    appSvcClinicalDirectorDtos.add(appSvcClinicalDirectorDto);
                }
            }else if(getPageData){
                String professionBoard = ParamUtil.getString(request,"professionBoard"+i);
                String profRegNo = ParamUtil.getString(request,"profRegNo"+i);
                String name = ParamUtil.getString(request,"name"+i);
                String salutation = ParamUtil.getString(request,"salutation"+i);
                String idType = ParamUtil.getString(request,"idType"+i);
                String idNo = ParamUtil.getString(request,"idNo"+i);
                String designation = ParamUtil.getString(request,"designation"+i);
                String otherDesignation = ParamUtil.getString(request, "otherDesignation"+i);
//                String specialty = ParamUtil.getString(request,"speciality"+i);
//                String specialityOther = ParamUtil.getString(request,"specialityOther"+i);
                String specialtyGetDateStr = ParamUtil.getString(request,"specialtyGetDate"+i);
                String typeOfCurrRegi = ParamUtil.getString(request,"typeOfCurrRegi"+i);
                String currRegiDateStr = ParamUtil.getString(request,"currRegiDate"+i);
                String praCerEndDateStr = ParamUtil.getString(request,"praCerEndDate"+i);
                String typeOfRegister = ParamUtil.getString(request,"typeOfRegister"+i);
                String relevantExperience = ParamUtil.getString(request,"relevantExperience"+i);
                String holdCerByEMS = ParamUtil.getString(request,"holdCerByEMS"+i);
                String aclsExpiryDateStr = ParamUtil.getString(request,"aclsExpiryDate"+i);
                String bclsExpiryDateStr = ParamUtil.getString(request,"bclsExpiryDate"+i);
                String mobileNo = ParamUtil.getString(request,"mobileNo"+i);
                String emailAddr = ParamUtil.getString(request,"emailAddr"+i);
                String noRegWithProfBoard = ParamUtil.getString(request,"noRegWithProfBoardVal"+i);
                String transportYear = ParamUtil.getString(request,"transportYear"+i);

                String assignSel = ParamUtil.getString(request,"assignSel"+i);
                AppSvcPrincipalOfficersDto appSvcClinicalDirectorDto = NewApplicationHelper.getPsnInfoFromLic(request, assignSel);
                appSvcClinicalDirectorDto.setPsnType(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR);
                log.info(StringUtil.changeForLog("Clinical Governance Officer assgined select: " + assignSel));
                if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType) || !NewApplicationHelper.isEmpty(assignSel)) {
                    appSvcClinicalDirectorDto.setAssignSelect(assignSel);
                } else {
                    appSvcClinicalDirectorDto.setAssignSelect(NewApplicationHelper.getAssignSelect(idType, idNo,
                            IaisEGPConstant.ASSIGN_SELECT_ADD_NEW));
                }
                AppPsnEditDto appPsnEditDto = appSvcClinicalDirectorDto.getPsnEditDto();
                if (appPsnEditDto == null) {
                    appPsnEditDto = NewApplicationHelper.setNeedEditField(appSvcClinicalDirectorDto);
                    appSvcClinicalDirectorDto.setPsnEditDto(appPsnEditDto);
                }
                boolean partEdit = AppConsts.YES.equals(isPartEdit) && !StringUtil.isEmpty(cdIndexNo);
                boolean isNewOfficer = IaisEGPConstant.ASSIGN_SELECT_ADD_NEW.equals(assignSel) || !appSvcClinicalDirectorDto.isLicPerson();
                if (canSetValue(appPsnEditDto.isProfessionBoard(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setProfessionBoard(professionBoard);
                }
                if (canSetValue(appPsnEditDto.isProfRegNo(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setProfRegNo(profRegNo);
                }
                if (canSetValue(appPsnEditDto.isName(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setName(name);
                }
                if (canSetValue(appPsnEditDto.isSalutation(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setSalutation(salutation);
                }
                if (canSetValue(appPsnEditDto.isIdType(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setIdType(idType);
                }
                if (canSetValue(appPsnEditDto.isIdNo(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setIdNo(idNo);
                }
                if (canSetValue(appPsnEditDto.isDesignation(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setDesignation(designation);
                }

                if(MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(appSvcClinicalDirectorDto.getDesignation())){
                    if (canSetValue(appPsnEditDto.isOtherDesignation(), isNewOfficer, partEdit)) {
                        appSvcClinicalDirectorDto.setOtherDesignation(otherDesignation);
                    }
                }else{
                    appSvcClinicalDirectorDto.setOtherDesignation(null);
                }
                if (canSetValue(appPsnEditDto.isSpeciality(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setSpeciality(null);
                }
                if (canSetValue(appPsnEditDto.isTypeOfRegister(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setTypeOfRegister(typeOfRegister);
                }
                if (canSetValue(appPsnEditDto.isHoldCerByEMS(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setHoldCerByEMS(holdCerByEMS);
                }
                if (canSetValue(appPsnEditDto.isMobileNo(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setMobileNo(mobileNo);
                }
                if (canSetValue(appPsnEditDto.isEmailAddr(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setEmailAddr(emailAddr);
                }
                if (canSetValue(appPsnEditDto.isTypeOfCurrRegi(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setTypeOfCurrRegi(typeOfCurrRegi);
                }
                if (canSetValue(appPsnEditDto.isRelevantExperience(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setRelevantExperience(relevantExperience);
                }
                if (StringUtil.isEmpty(cdIndexNo)) {
                    appSvcClinicalDirectorDto.setIndexNo(UUID.randomUUID().toString());
                } else {
                    appSvcClinicalDirectorDto.setIndexNo(cdIndexNo);
                }

                //date pick
                if (canSetValue(appPsnEditDto.isSpecialtyGetDate(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setSpecialtyGetDateStr(specialtyGetDateStr);
                    Date specialtyGetDate = DateUtil.parseDate(specialtyGetDateStr, Formatter.DATE);
                    appSvcClinicalDirectorDto.setSpecialtyGetDate(specialtyGetDate);
                }
                if (canSetValue(appPsnEditDto.isPraCerEndDate(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setPraCerEndDateStr(praCerEndDateStr);
                    Date praCerEndDate = DateUtil.parseDate(praCerEndDateStr, Formatter.DATE);
                    appSvcClinicalDirectorDto.setPraCerEndDate(praCerEndDate);
                }
                if (canSetValue(appPsnEditDto.isCurrRegiDate(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setCurrRegiDateStr(currRegiDateStr);
                    Date currRegiDate = DateUtil.parseDate(currRegiDateStr, Formatter.DATE);
                    appSvcClinicalDirectorDto.setCurrRegiDate(currRegiDate);
                }
                if (canSetValue(appPsnEditDto.isAclsExpiryDate(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setAclsExpiryDateStr(aclsExpiryDateStr);
                    Date aclsExpiryDate = DateUtil.parseDate(aclsExpiryDateStr, Formatter.DATE);
                    appSvcClinicalDirectorDto.setAclsExpiryDate(aclsExpiryDate);
                }
                if (canSetValue(appPsnEditDto.isBclsExpiryDate(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setBclsExpiryDateStr(bclsExpiryDateStr);
                    Date bclsExpiryDate = DateUtil.parseDate(bclsExpiryDateStr, Formatter.DATE);
                    appSvcClinicalDirectorDto.setBclsExpiryDate(bclsExpiryDate);
                }

                if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(currSvcCode)){
                    if (canSetValue(appPsnEditDto.isNoRegWithProfBoard(), isNewOfficer, partEdit)) {
                        if(AppConsts.YES.equals(noRegWithProfBoard)){
                            appSvcClinicalDirectorDto.setNoRegWithProfBoard(noRegWithProfBoard);
                        }else{
                            appSvcClinicalDirectorDto.setNoRegWithProfBoard(null);
                        }
                    }
                    if (canSetValue(appPsnEditDto.isTransportYear(), isNewOfficer, partEdit)) {
                        appSvcClinicalDirectorDto.setTransportYear(transportYear);
                    }
                }
                appSvcClinicalDirectorDtos.add(appSvcClinicalDirectorDto);
            }
        }
        log.debug(StringUtil.changeForLog("gen app svc clinical director dto end ..."));
        return appSvcClinicalDirectorDtos;
    }

    private boolean canSetValue(boolean canEdit, boolean isNewOfficer, boolean isPartEdit) {
        return isPartEdit || canEdit || isNewOfficer;
    }

    private AppSvcChargesPageDto genAppSvcChargesDto(HttpServletRequest request, String appType){
        AppSvcChargesPageDto appSvcChargesPageDto = new AppSvcChargesPageDto();
        List<AppSvcChargesDto> generalChargesDtos = IaisCommonUtils.genNewArrayList();
        List<AppSvcChargesDto> otherChargesDtos = IaisCommonUtils.genNewArrayList();
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(request, currentSvcId);
        boolean isRfi = NewApplicationHelper.checkIsRfi(request);
        int generalChargeLength = ParamUtil.getInt(request,"generalChargeLength");
        for(int i = 0; i < generalChargeLength ; i++){
            boolean getDataByIndexNo = false;
            boolean getPageData = false;
            String isPartEdit = ParamUtil.getString(request,"isPartEdit"+i);
            String chargesIndexNo = ParamUtil.getString(request,"chargesIndexNo"+i);
            if(!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                getPageData = true;
            }else if(AppConsts.YES.equals(isPartEdit)){
                getPageData = true;
            }else if(!StringUtil.isEmpty(chargesIndexNo)){
                getDataByIndexNo = true;
            }
            log.debug("get data by index no. is {}",getDataByIndexNo);
            log.debug("get page data is {}",getPageData);
            if(getDataByIndexNo){
                AppSvcChargesDto appSvcChargesDto = getChargesByIndexNo(appSvcRelatedInfoDto, chargesIndexNo, true);
                if(appSvcChargesDto != null){
                    generalChargesDtos.add(appSvcChargesDto);
                }
            }else if(getPageData){
                String chargesType = ParamUtil.getString(request,"chargesType"+i);
                String minAmount = ParamUtil.getString(request,"minAmount"+i);
                String maxAmount = ParamUtil.getString(request,"maxAmount"+i);
                String remarks = ParamUtil.getString(request,"remarks"+i);

                AppSvcChargesDto appSvcChargesDto = new AppSvcChargesDto();
                appSvcChargesDto.setChargesType(chargesType);
                appSvcChargesDto.setMinAmount(minAmount);
                appSvcChargesDto.setMaxAmount(maxAmount);
                appSvcChargesDto.setRemarks(remarks);
                if(StringUtil.isEmpty(chargesIndexNo)){
                    appSvcChargesDto.setChargesIndexNo(UUID.randomUUID().toString());
                }else{
                    appSvcChargesDto.setChargesIndexNo(chargesIndexNo);
                }
                generalChargesDtos.add(appSvcChargesDto);
            }



        }
        int otherChargeLength = ParamUtil.getInt(request,"otherChargeLength");
        for(int i = 0; i < otherChargeLength ; i++){
            boolean getDataByIndexNo = false;
            boolean getPageData = false;
            String isPartEdit = ParamUtil.getString(request,"otherChargesIsPartEdit"+i);
            String chargesIndexNo = ParamUtil.getString(request,"otherChargesIndexNo"+i);
            if(!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                getPageData = true;
            }else if(AppConsts.YES.equals(isPartEdit)){
                getPageData = true;
            }else if(!StringUtil.isEmpty(chargesIndexNo)){
                getDataByIndexNo = true;
            }
            log.debug("other charges get data by index no. is {}",getDataByIndexNo);
            log.debug("other charges get page data is {}",getPageData);
            if(getDataByIndexNo){
                AppSvcChargesDto appSvcChargesDto = getChargesByIndexNo(appSvcRelatedInfoDto, chargesIndexNo, false);
                if(appSvcChargesDto != null){
                    otherChargesDtos.add(appSvcChargesDto);
                }
            }else if(getPageData){
                String otherChargesCategory = ParamUtil.getString(request,"otherChargesCategory"+i);
                String otherChargesType = ParamUtil.getString(request,"otherChargesType"+i);
                String otherAmountMin = ParamUtil.getString(request,"otherAmountMin"+i);
                String otherAmountMax = ParamUtil.getString(request,"otherAmountMax"+i);
                String otherRemarks = ParamUtil.getString(request,"otherRemarks"+i);
                AppSvcChargesDto appSvcChargesDto = new AppSvcChargesDto();
                appSvcChargesDto.setChargesCategory(otherChargesCategory);
                appSvcChargesDto.setChargesType(otherChargesType);
                appSvcChargesDto.setMinAmount(otherAmountMin);
                appSvcChargesDto.setMaxAmount(otherAmountMax);
                appSvcChargesDto.setRemarks(otherRemarks);
                if(StringUtil.isEmpty(chargesIndexNo)){
                    appSvcChargesDto.setChargesIndexNo(UUID.randomUUID().toString());
                }else{
                    appSvcChargesDto.setChargesIndexNo(chargesIndexNo);
                }
                otherChargesDtos.add(appSvcChargesDto);
            }
        }
        appSvcChargesPageDto.setGeneralChargesDtos(generalChargesDtos);
        appSvcChargesPageDto.setOtherChargesDtos(otherChargesDtos);
        return appSvcChargesPageDto;
    }

    private List<AppSvcPrincipalOfficersDto> genAppSvcPrincipalOfficersDto(HttpServletRequest request, Boolean isGetDataFromPagePo, Boolean isGetDataFromPageDpo) {
        log.info(StringUtil.changeForLog("genAppSvcPrincipalOfficersDto start ...."));
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        String deputySelect = ParamUtil.getString(request, "deputyPrincipalOfficer");
        boolean isRfi = NewApplicationHelper.checkIsRfi(request);
        String appType = appSubmissionDto.getAppType();
        boolean rfcOrRenew = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType);
        boolean needEdit = rfcOrRenew || isRfi;
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(request, currentSvcId);
        if (isGetDataFromPagePo) {
            log.info(StringUtil.changeForLog("get po data..."));
            String[] poExistingPsn = ParamUtil.getStrings(request, "poExistingPsn");
            String[] poLicPerson = ParamUtil.getStrings(request, "poLicPerson");
            String[] assignSelect = ParamUtil.getStrings(request, "poSelect");
            String[] salutation = ParamUtil.getStrings(request, "salutation");
            String[] name = ParamUtil.getStrings(request, "name");
            String[] idType = ParamUtil.getStrings(request, "idType");
            String[] idNo = ParamUtil.getStrings(request, "idNo");
            String[] designation = ParamUtil.getStrings(request, "designation");
            String[] otherDesignations = ParamUtil.getStrings(request, "otherDesignation");
            String[] mobileNo = ParamUtil.getStrings(request, "mobileNo");
            String[] officeTelNo = ParamUtil.getStrings(request, "officeTelNo");
            String[] emailAddress = ParamUtil.getStrings(request, "emailAddress");
            String[] poIsPartEdit = ParamUtil.getStrings(request,"poIsPartEdit");
            String[] poIndexNos = ParamUtil.getStrings(request,"poIndexNo");
            String[] loadingTypes = ParamUtil.getStrings(request,"loadingType");
            int length =  0;
            if(assignSelect != null){
                length = assignSelect.length;
            }
            if (needEdit) {
                if(poIndexNos != null){
                    length = poIndexNos.length;
                }else{
                    length = 0;
                }
            }
            for (int i = 0; i < length; i++) {
                boolean chooseExisting = false;
                boolean getPageData = false;
                String assign = assignSelect[i];
                String licPsn = poLicPerson[i];
                String loadingType = loadingTypes[i];
                boolean loadingByBlur = NewApplicationConstant.NEW_PSN.equals(assign) && AppConsts.YES.equals(licPsn) && ApplicationConsts.PERSON_LOADING_TYPE_BLUR.equals(loadingType);
                //for rfi,rfc,renew use
//                String existingPsn = poExistingPsn[i];
                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
                if(!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                    if (assign != null) {
                        if(isExistingPsn(assign,licPsn)){
                            chooseExisting = true;
                        }else if(loadingByBlur){
                            chooseExisting = true;
                        } else{
                            getPageData = true;
                        }
                    }
                }else if(needEdit){
                    if (assign != null) {
                        String poIndexNo = poIndexNos[i];
                        if (!StringUtil.isEmpty(poIndexNo)) {
                            //not click edit
                            if (AppConsts.NO.equals(poIsPartEdit[i])) {
                                appSvcPrincipalOfficersDto = getPsnByIndexNo(appSvcRelatedInfoDto, poIndexNo,PAGE_NAME_PO);
                                appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                                //change arr
                                poIndexNos = removeArrIndex(poIndexNos, i);
                                poIsPartEdit = removeArrIndex(poIsPartEdit, i);
                                poLicPerson = removeArrIndex(poLicPerson, i);
                                loadingTypes = removeArrIndex(loadingTypes,i);
                                //dropdown cannot disabled
                                assignSelect = removeArrIndex(assignSelect, i);
                                salutation = removeArrIndex(salutation, i);
                                idType = removeArrIndex(idType, i);
                                designation = removeArrIndex(designation, i);
//                                existingPsn = removeArrIndex(existingPsn, i);
                                //change arr index
                                --i;
                                --length;
                                continue;
                            }
                        }
                        //isPartEdit->1.click edit button 2.add more psn
                        if(isExistingPsn(assign,licPsn)){
                            //add cgo and choose existing
                            chooseExisting = true;
                        }else if(loadingByBlur){
                            chooseExisting = true;
                        }else{
                            getPageData = true;
                        }
                    }

                }else{
                    log.info(StringUtil.changeForLog("The current type is not supported"));
                }
                log.info(StringUtil.changeForLog("chooseExisting:"+chooseExisting));
                log.info(StringUtil.changeForLog("getPageData:"+getPageData));
                String assignSel = assignSelect[i];
                if(chooseExisting){
                    if(loadingByBlur){
                        assignSel = NewApplicationHelper.getPersonKey(idType[i],idNo[i]);
                    }
                    appSvcPrincipalOfficersDto = NewApplicationHelper.getPsnInfoFromLic(request, assignSel);
                    appSvcPrincipalOfficersDto.setLoadingType(loadingType);
                    AppPsnEditDto appPsnEditDto;
                    try {
                        appPsnEditDto = NewApplicationHelper.setNeedEditField(appSvcPrincipalOfficersDto);
                    } catch (Exception e) {
                        appPsnEditDto = new AppPsnEditDto();
                        log.error(e.getMessage(), e);
                    }
                    if(appPsnEditDto.isIdType()){
                        NewApplicationHelper.setPsnValue(idType,i,appSvcPrincipalOfficersDto,"idType");
                    }
                    if(appPsnEditDto.isSalutation()){
                        NewApplicationHelper.setPsnValue(salutation,i,appSvcPrincipalOfficersDto,"salutation");
                    }
                    if(appPsnEditDto.isDesignation()){
                        NewApplicationHelper.setPsnValue(designation,i,appSvcPrincipalOfficersDto,"designation");
                    }
                    if(appPsnEditDto.isOtherDesignation()){
                        NewApplicationHelper.setPsnValue(otherDesignations,i,appSvcPrincipalOfficersDto,"otherDesignation");
                    }
                    if(appPsnEditDto.isOtherDesignation() && MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(appSvcPrincipalOfficersDto.getDesignation())){
                        NewApplicationHelper.setPsnValue(otherDesignations,i,appSvcPrincipalOfficersDto,"otherDesignation");
                    }

                    if(appPsnEditDto.isName()){
                        name = NewApplicationHelper.setPsnValue(name,i,appSvcPrincipalOfficersDto,"name");
                    }
                    if(appPsnEditDto.isIdNo()){
                        idNo = NewApplicationHelper.setPsnValue(idNo,i,appSvcPrincipalOfficersDto,"idNo");
                    }
                    if(appPsnEditDto.isMobileNo()){
                        mobileNo = NewApplicationHelper.setPsnValue(mobileNo,i,appSvcPrincipalOfficersDto,"mobileNo");
                    }
                    if(appPsnEditDto.isOfficeTelNo()){
                        officeTelNo = NewApplicationHelper.setPsnValue(officeTelNo,i,appSvcPrincipalOfficersDto,"officeTelNo");
                    }
                    if(appPsnEditDto.isEmailAddr()){
                        emailAddress = NewApplicationHelper.setPsnValue(emailAddress,i,appSvcPrincipalOfficersDto,"emailAddr");
                    }
                    String poIndexNo = poIndexNos[i];
                    if(!StringUtil.isEmpty(poIndexNo)){
                        appSvcPrincipalOfficersDto.setIndexNo(poIndexNo);
                    }
                    if(StringUtil.isEmpty(appSvcPrincipalOfficersDto.getIndexNo())){
                        appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                    }
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                    appSvcPrincipalOfficersDto.setLicPerson(true);
                    appSvcPrincipalOfficersDto.setSelectDropDown(true);
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
                    appSvcPrincipalOfficersDto.setPsnEditDto(appPsnEditDto);
                    appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                    //change arr index
                    poExistingPsn = removeArrIndex(poExistingPsn,i);
                    poLicPerson = removeArrIndex(poLicPerson, i);
                    poIndexNos = removeArrIndex(poIndexNos, i);
                    loadingTypes = removeArrIndex(loadingTypes,i);
                    //dropdown cannot disabled
                    assignSelect = removeArrIndex(assignSelect, i);
                    salutation = removeArrIndex(salutation, i);
                    idType = removeArrIndex(idType, i);
                    designation = removeArrIndex(designation, i);
                    --i;
                    --length;
                }else if(getPageData){
                    String poIndexNo = poIndexNos[i];
                    if (StringUtil.isEmpty(poIndexNo)) {
                        appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                    } else {
                        appSvcPrincipalOfficersDto.setIndexNo(poIndexNo);
                    }
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                    appSvcPrincipalOfficersDto.setSalutation(salutation[i]);
                    appSvcPrincipalOfficersDto.setName(name[i]);
                    appSvcPrincipalOfficersDto.setIdType(idType[i]);
                    appSvcPrincipalOfficersDto.setIdNo(StringUtil.toUpperCase(idNo[i]));
                    appSvcPrincipalOfficersDto.setDesignation(designation[i]);
                    appSvcPrincipalOfficersDto.setOtherDesignation(otherDesignations[i]);
                    appSvcPrincipalOfficersDto.setMobileNo(mobileNo[i]);
                    appSvcPrincipalOfficersDto.setOfficeTelNo(officeTelNo[i]);
                    String emailAddr = "";
                    if(emailAddress != null){
                        if(!StringUtil.isEmpty(emailAddress[i])){
                            emailAddr = StringUtil.viewHtml(emailAddress[i]);
                        }
                    }
                    appSvcPrincipalOfficersDto.setEmailAddr(emailAddr);
                    if (needEdit && AppConsts.YES.equals(licPsn)) {
                        appSvcPrincipalOfficersDto.setLicPerson(true);
                        AppSvcPrincipalOfficersDto licPerson = NewApplicationHelper.getPsnInfoFromLic(request, NewApplicationHelper.getPersonKey(appSvcPrincipalOfficersDto.getIdType(),appSvcPrincipalOfficersDto.getIdNo()));
                        if(licPerson != null){
                            appSvcPrincipalOfficersDto.setCurPersonelId(licPerson.getCurPersonelId());
                        }
                    }
                    appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                }
            }
        }
        //depo
        if ("1".equals(deputySelect) && isGetDataFromPageDpo) {
            log.info(StringUtil.changeForLog("get dpo data..."));
            String[] dpoExistingPsn = ParamUtil.getStrings(request, "dpoExistingPsn");
            String[] dpoLicPerson = ParamUtil.getStrings(request, "dpoLicPerson");
            String[] assignSelect = ParamUtil.getStrings(request, "deputyPoSelect");
            String[] deputySalutation = ParamUtil.getStrings(request, "deputySalutation");
            String[] deputyDesignation = ParamUtil.getStrings(request, "deputyDesignation");
            String[] deputyOtherDesignations = ParamUtil.getStrings(request, "deputyOtherDesignation");
            String[] deputyName = ParamUtil.getStrings(request, "deputyName");
            String[] deputyIdType = ParamUtil.getStrings(request, "deputyIdType");
            String[] deputyIdNo = ParamUtil.getStrings(request, "deputyIdNo");
            String[] deputyMobileNo = ParamUtil.getStrings(request, "deputyMobileNo");
            String[] deputyOfficeTelNo = ParamUtil.getStrings(request, "deputyOfficeTelNo");
            String[] deputyEmailAddr = ParamUtil.getStrings(request, "deputyEmailAddr");
            String[] dpoIsPartEdit = ParamUtil.getStrings(request,"dpoIsPartEdit");
            String[] dpoIndexNos = ParamUtil.getStrings(request,"dpoIndexNo");
            String[] dpoLoadingTypes = ParamUtil.getStrings(request,"dpoLoadingType");
            int length = 0;
            if(assignSelect != null){
                length = assignSelect.length;
            }
            if (needEdit) {
                if(dpoIndexNos != null){
                    length = dpoIndexNos.length;
                }else{
                    length = 0;
                }
            }
            for (int i = 0; i < length; i++) {
                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
                String assign = assignSelect[i];
                String licPsn = dpoLicPerson[i];
                boolean chooseExisting = false;
                boolean getPageData = false;
                String loadingType = dpoLoadingTypes[i];
                boolean loadingByBlur = NewApplicationConstant.NEW_PSN.equals(assign) && AppConsts.YES.equals(licPsn) && ApplicationConsts.PERSON_LOADING_TYPE_BLUR.equals(loadingType);
                //for rfi,rfc,renew use
                String existingPsn = dpoExistingPsn[i];
                //new and not rfi
                if(!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    if (assign != null) {
                        if (isExistingPsn(assign,licPsn)) {
                            chooseExisting = true;
                        }else if(loadingByBlur){
                            chooseExisting = true;
                        }else{
                            getPageData = true;
                        }
                    }
                }else if(needEdit){
                    if (assign != null) {
                        String dpoIndexNo = dpoIndexNos[i];
                        if (!StringUtil.isEmpty(dpoIndexNo)) {
                            //not click edit
                            if (AppConsts.NO.equals(dpoIsPartEdit[i])) {
                                appSvcPrincipalOfficersDto = getPsnByIndexNo(appSvcRelatedInfoDto, dpoIndexNo,PAGE_NAME_PO);
                                appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                                //change arr
                                dpoIndexNos = removeArrIndex(dpoIndexNos, i);
                                dpoIsPartEdit = removeArrIndex(dpoIsPartEdit, i);
                                dpoLicPerson = removeArrIndex(dpoLicPerson, i);
                                dpoLoadingTypes = removeArrIndex(dpoLoadingTypes,i);
                                //dropdown cannot disabled
                                assignSelect = removeArrIndex(assignSelect, i);
                                deputySalutation = removeArrIndex(deputySalutation, i);
                                deputyIdType = removeArrIndex(deputyIdType, i);
                                deputyDesignation = removeArrIndex(deputyDesignation, i);
//                                existingPsn = removeArrIndex(existingPsn, i);
                                //change arr index
                                --i;
                                --length;
                                continue;
                            }
                        }
                        //isPartEdit->1.click edit button 2.add more psn
                        if(isExistingPsn(assign,licPsn)){
                            //add cgo and choose existing
                            chooseExisting = true;
                        }else if(loadingByBlur){
                            chooseExisting = true;
                        }else{
                            getPageData = true;
                        }
                    }
                }else {
                    log.info(StringUtil.changeForLog("The current type is not supported"));
                }
                log.info(StringUtil.changeForLog("chooseExisting:"+chooseExisting));
                log.info(StringUtil.changeForLog("getPageData:"+getPageData));
                String assignSel = assignSelect[i];
                if(chooseExisting){
                    if(loadingByBlur){
                        assignSel = NewApplicationHelper.getPersonKey(deputyIdType[i],deputyIdNo[i]);
                    }
                    appSvcPrincipalOfficersDto = NewApplicationHelper.getPsnInfoFromLic(request, assignSel);
                    appSvcPrincipalOfficersDto.setLoadingType(loadingType);
                    String dpoIndexNo = dpoIndexNos[i];
                    if (StringUtil.isEmpty(dpoIndexNo)) {
                        appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                    } else {
                        appSvcPrincipalOfficersDto.setIndexNo(dpoIndexNo);
                    }
                    AppPsnEditDto appPsnEditDto;
                    try {
                        appPsnEditDto = NewApplicationHelper.setNeedEditField(appSvcPrincipalOfficersDto);
                    } catch (Exception e) {
                        appPsnEditDto = new AppPsnEditDto();
                        log.error(e.getMessage(), e);
                    }
                    if (appPsnEditDto.isIdType()) {
                        NewApplicationHelper.setPsnValue(deputyIdType, i, appSvcPrincipalOfficersDto, "idType");
                    }
                    if (appPsnEditDto.isSalutation()) {
                        NewApplicationHelper.setPsnValue(deputySalutation, i, appSvcPrincipalOfficersDto, "salutation");
                    }
                    if (appPsnEditDto.isDesignation()) {
                        NewApplicationHelper.setPsnValue(deputyDesignation, i, appSvcPrincipalOfficersDto, "designation");
                    }
                    if(appPsnEditDto.isOtherDesignation() && MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(appSvcPrincipalOfficersDto.getDesignation())){
                        NewApplicationHelper.setPsnValue(deputyOtherDesignations,i,appSvcPrincipalOfficersDto,"otherDesignation");
                    }
                    //input
                    if (appPsnEditDto.isName()) {
                        deputyName = NewApplicationHelper.setPsnValue(deputyName, i, appSvcPrincipalOfficersDto, "name");
                    }
                    if (appPsnEditDto.isIdNo()) {
                        deputyIdNo = NewApplicationHelper.setPsnValue(deputyIdNo, i, appSvcPrincipalOfficersDto, "idNo");
                    }
                    if (appPsnEditDto.isMobileNo()) {
                        deputyMobileNo = NewApplicationHelper.setPsnValue(deputyMobileNo, i, appSvcPrincipalOfficersDto, "mobileNo");
                    }
                    if (appPsnEditDto.isOfficeTelNo()) {
                        deputyOfficeTelNo = NewApplicationHelper.setPsnValue(deputyOfficeTelNo, i, appSvcPrincipalOfficersDto, "officeTelNo");
                    }
                    if (appPsnEditDto.isEmailAddr()) {
                        deputyEmailAddr = NewApplicationHelper.setPsnValue(deputyEmailAddr, i, appSvcPrincipalOfficersDto, "emailAddr");
                    }
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                    appSvcPrincipalOfficersDto.setLicPerson(true);
                    appSvcPrincipalOfficersDto.setSelectDropDown(true);
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
                    appSvcPrincipalOfficersDto.setPsnEditDto(appPsnEditDto);
                    appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                    //change arr index
                    dpoLicPerson = removeArrIndex(dpoLicPerson, i);
                    dpoExistingPsn = removeArrIndex(dpoExistingPsn,i);
                    dpoLoadingTypes = removeArrIndex(dpoLoadingTypes,i);
                    //dropdown cannot disabled
                    assignSelect = removeArrIndex(assignSelect, i);
                    deputySalutation = removeArrIndex(deputySalutation, i);
                    deputyIdType = removeArrIndex(deputyIdType, i);
                    deputyDesignation = removeArrIndex(deputyDesignation, i);
                    --i;
                    --length;
                }else if(getPageData){
                    String dpoIndexNo = dpoIndexNos[i];
                    if (StringUtil.isEmpty(dpoIndexNo)) {
                        appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                    } else {
                        appSvcPrincipalOfficersDto.setIndexNo(dpoIndexNo);
                    }
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                    appSvcPrincipalOfficersDto.setSalutation(deputySalutation[i]);
                    appSvcPrincipalOfficersDto.setName(deputyName[i]);
                    appSvcPrincipalOfficersDto.setIdType(deputyIdType[i]);
                    appSvcPrincipalOfficersDto.setIdNo(StringUtil.toUpperCase(deputyIdNo[i]));
                    appSvcPrincipalOfficersDto.setDesignation(deputyDesignation[i]);
                    appSvcPrincipalOfficersDto.setOtherDesignation(deputyOtherDesignations[i]);
                    appSvcPrincipalOfficersDto.setMobileNo(deputyMobileNo[i]);
                    appSvcPrincipalOfficersDto.setOfficeTelNo(deputyOfficeTelNo[i]);
                    String emailAddr = "";
                    if(deputyEmailAddr != null){
                        if(!StringUtil.isEmpty(deputyEmailAddr[i])){
                            emailAddr = StringUtil.viewHtml(deputyEmailAddr[i]);
                        }
                    }
                    appSvcPrincipalOfficersDto.setEmailAddr(emailAddr);
                    if (needEdit && AppConsts.YES.equals(licPsn)) {
                        appSvcPrincipalOfficersDto.setLicPerson(true);
                        AppSvcPrincipalOfficersDto licPerson = NewApplicationHelper.getPsnInfoFromLic(request, NewApplicationHelper.getPersonKey(appSvcPrincipalOfficersDto.getIdType(),appSvcPrincipalOfficersDto.getIdNo()));
                        if(licPerson != null){
                            appSvcPrincipalOfficersDto.setCurPersonelId(licPerson.getCurPersonelId());
                        }
                    }
                    appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                }
            }
        }
        log.info(StringUtil.changeForLog("genAppSvcPrincipalOfficersDto end ...."));
        return appSvcPrincipalOfficersDtos;
    }

    public static AppSubmissionDto getAppSubmissionDto(HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, NewApplicationDelegator.APPSUBMISSIONDTO);
        if (appSubmissionDto == null) {
            appSubmissionDto = new AppSubmissionDto();
        }
        return appSubmissionDto;
    }

    private List<AppSvcDocDto> doValidateSvcDocument(List<AppSvcDocDto> appSvcDocDtoList, Map<String, String> errorMap,boolean setIsPassValidate) {
        if (!IaisCommonUtils.isEmpty(appSvcDocDtoList)) {
            for (AppSvcDocDto appSvcDocDto:appSvcDocDtoList) {
                Integer docSize =appSvcDocDto.getDocSize();
                String docName = appSvcDocDto.getDocName();
                String id = appSvcDocDto.getSvcDocId();
                int uploadFileLimit = systemParamConfig.getUploadFileLimit();
                String premVal = appSvcDocDto.getPremisesVal();
                String premType = appSvcDocDto.getPremisesVal();
                String premKey = "";
                if(StringUtil.isEmpty(premVal) && StringUtil.isEmpty(premType)){
                    premKey =  appSvcDocDto.getSvcDocId();
                }else if(!StringUtil.isEmpty(premVal) && !StringUtil.isEmpty(premType)){
                    premKey =  "prem" + appSvcDocDto.getSvcDocId()+premVal;
                }
                if (docSize/1024 > uploadFileLimit) {
                    String err19 =  MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(uploadFileLimit),"sizeMax");
                    if(StringUtil.isEmpty(premVal)){
                        errorMap.put(id + "selectedFile", err19);
                    }else{
                        errorMap.put(premKey, err19);
                    }
                }
                if(docName.length() > 100){
                    String generalErr22 = MessageUtil.getMessageDesc("GENERAL_ERR0022");
                    if(StringUtil.isEmpty(premVal)){
                        errorMap.put(id + "selectedFile", generalErr22);
                    }else{
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
                    String err18 = MessageUtil.replaceMessage("GENERAL_ERR0018", sysFileType,"fileType");
                    if(StringUtil.isEmpty(premVal)){
                        errorMap.put(id + "selectedFile", err18);
                    }else{
                        errorMap.put(premKey, err18);
                    }
                }
                String errMsg = errorMap.get(id + "selectedFile");
                String errMsg2 = errorMap.get(premKey);
                if(StringUtil.isEmpty(errMsg) && StringUtil.isEmpty(errMsg2) && setIsPassValidate){
                    appSvcDocDto.setPassValidate(true);
                }
            }
        }
        return appSvcDocDtoList;
    }

    private String getCurrentServiceId(HttpServletRequest request){
       return (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSERVICEID);
    }

    private AppSvcRelatedInfoDto getAppSvcRelatedInfo(AppSubmissionDto appSubmissionDto, String currentSvcId, String appNo) {
        log.info(StringUtil.changeForLog("service id: " + currentSvcId + " - appNo: " + appNo));
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
        if (appSubmissionDto != null) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (appSvcRelatedInfoDtos != null && !appSvcRelatedInfoDtos.isEmpty()) {
                for (AppSvcRelatedInfoDto svcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    if (currentSvcId.equals(svcRelatedInfoDto.getServiceId()) && (StringUtil.isEmpty(appNo) ||
                            appNo.equals(svcRelatedInfoDto.getAppNo()))) {
                        appSvcRelatedInfoDto = svcRelatedInfoDto;
                        break;
                    }
                }
            }
        }
        return appSvcRelatedInfoDto;
    }

    /*
     * get current svc dto
     * */
    private AppSvcRelatedInfoDto getAppSvcRelatedInfo(HttpServletRequest request) {
        String currSvcId = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSERVICEID);
        return getAppSvcRelatedInfo(request, currSvcId);
    }

    private AppSvcRelatedInfoDto getAppSvcRelatedInfo(HttpServletRequest request, String currentSvcId) {
        log.debug(StringUtil.changeForLog("getAppSvcRelatedInfo service id:" + currentSvcId));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request,
                NewApplicationDelegator.APPSUBMISSIONDTO);
        return getAppSvcRelatedInfo(appSubmissionDto, currentSvcId, null);
    }

    private AppSvcRelatedInfoDto getAppSvcRelatedInfo(HttpServletRequest request, String currentSvcId, String appNo) {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, NewApplicationDelegator.APPSUBMISSIONDTO);
        return getAppSvcRelatedInfo(appSubmissionDto, currentSvcId, appNo);
    }

    private void setAppSvcRelatedInfoMap(HttpServletRequest request, String currentSvcId, AppSvcRelatedInfoDto appSvcRelatedInfoDto) {
        setAppSvcRelatedInfoMap(request, currentSvcId, appSvcRelatedInfoDto, null);
    }

    private void setAppSvcRelatedInfoMap(HttpServletRequest request, String currentSvcId, AppSvcRelatedInfoDto appSvcRelatedInfoDto,
            AppSubmissionDto appSubmissionDto) {
        if (appSubmissionDto == null) {
            appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, NewApplicationDelegator.APPSUBMISSIONDTO);
        }
        if (appSubmissionDto == null) {
            return;
        }
        String appNo = appSvcRelatedInfoDto.getAppNo();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (appSvcRelatedInfoDtos != null && !appSvcRelatedInfoDtos.isEmpty()) {
            int i = 0;
            for (AppSvcRelatedInfoDto svcRelatedInfoDto : appSvcRelatedInfoDtos) {
                if (currentSvcId.equals(svcRelatedInfoDto.getServiceId()) && (StringUtil.isEmpty(appNo) ||
                        appNo.equals(svcRelatedInfoDto.getAppNo()))) {
                    appSvcRelatedInfoDtos.set(i, appSvcRelatedInfoDto);
                    break;
                }
                i++;
            }
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
        }
        ParamUtil.setSessionAttr(request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
    }

    private List<AppSvcPersonnelDto> genAppSvcPersonnelDtoList(HttpServletRequest request, List<String> personnelTypeList, String svcCode) {
        List<AppSvcPersonnelDto> appSvcPersonnelDtos = IaisCommonUtils.genNewArrayList();
        String[] personnelSels = ParamUtil.getStrings(request, "personnelSel");
        String[] designations = ParamUtil.getStrings(request, "designation");
        String[] otherDesignationss = ParamUtil.getStrings(request, "otherDesignation");
        String[] names = ParamUtil.getStrings(request, "name");
        String[] qualifications = ParamUtil.getStrings(request, "qualification");
        String[] wrkExpYears = ParamUtil.getStrings(request, "wrkExpYear");
        String[] professionalRegnNos = ParamUtil.getStrings(request, "regnNo");
        String[] indexNos = ParamUtil.getStrings(request,"indexNo");
        String[] isPartEdit = ParamUtil.getStrings(request,"isPartEdit");
        String appType = getAppSubmissionDto(request).getAppType();
        boolean isRfi = NewApplicationHelper.checkIsRfi(request);
        boolean rfcOrRenew = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType);
        boolean needEdit = rfcOrRenew || isRfi;
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(request, currentSvcId);
        if (personnelSels != null && personnelSels.length > 0) {
            int length =  personnelSels.length;
            for (int i = 0; i < length; i++) {
                AppSvcPersonnelDto appSvcPersonnelDto = new AppSvcPersonnelDto();
                String personnelSel = personnelSels[i];
                appSvcPersonnelDto.setPersonnelType(personnelSel);
                if (needEdit && personnelSel != null) {
                    String indexNo = indexNos[i];
                    if (!StringUtil.isEmpty(indexNo)) {
                        //not click edit
                        if (AppConsts.NO.equals(isPartEdit[i])) {
                            appSvcPersonnelDto = getAppSvcPersonnelDtoByIndexNo(appSvcRelatedInfoDto, indexNo);
                            appSvcPersonnelDtos.add(appSvcPersonnelDto);
                            //change arr
                            indexNos = removeArrIndex(indexNos, i);
                            isPartEdit = removeArrIndex(isPartEdit, i);
                            //dropdown cannot disabled
                            designations = removeArrIndex(designations, i);
                            //change arr index
                            --i;
                            --length;
                            continue;
                        }
                    }
                }
                if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode) ||
                        AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode)) {
                    if (StringUtil.isEmpty(personnelSel) || !personnelTypeList.contains(personnelSel)) {
                        appSvcPersonnelDtos.add(appSvcPersonnelDto);
                        continue;
                    }
                }

                String designation = "";
                String name = "";
                String qualification = "";
                String wrkExpYear = "";
                String professionalRegnNo = "";
                if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)) {
                    if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST.equals(personnelSel)) {
                        name = names[i];
                        qualification = qualifications[i];
                        wrkExpYear = wrkExpYears[i];
                    } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER.equals(personnelSel)) {
                        name = names[i];
                    }
                } else if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode)) {
                    if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL.equals(personnelSel)) {
                        designation = designations[i];
                        name = names[i];
                        qualification = qualifications[i];
                        wrkExpYear = wrkExpYears[i];
                    } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST.equals(personnelSel)) {
                        name = names[i];
                        qualification = qualifications[i];
                        wrkExpYear = wrkExpYears[i];
                    } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER.equals(personnelSel)) {
                        name = names[i];
                    } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE.equals(personnelSel)) {
                        name = names[i];
                        professionalRegnNo = professionalRegnNos[i];
                    }
                } else if (AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode)) {
                    designation = designations[i];
                    name = names[i];
                    professionalRegnNo = professionalRegnNos[i];
                    wrkExpYear = wrkExpYears[i];
                } else if (AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)) {
                    name = names[i];
                    qualification = qualifications[i];
                    wrkExpYear = wrkExpYears[i];
                }else {
                    name = names[i];
                    qualification = qualifications[i];
                    wrkExpYear = wrkExpYears[i];
                }

                appSvcPersonnelDto.setDesignation(designation);
                if(NewApplicationConstant.DESIGNATION_OTHERS.equals(designation)){
                    appSvcPersonnelDto.setOtherDesignation(otherDesignationss[i]);
                }
                appSvcPersonnelDto.setName(name);
                appSvcPersonnelDto.setQualification(qualification);
                appSvcPersonnelDto.setWrkExpYear(wrkExpYear);
                appSvcPersonnelDto.setProfRegNo(professionalRegnNo);
                String indexNo = indexNos[i];
                if (!StringUtil.isEmpty(indexNo)) {
                    appSvcPersonnelDto.setIndexNo(indexNo);
                } else {
                    appSvcPersonnelDto.setIndexNo(UUID.randomUUID().toString());
                }
                appSvcPersonnelDtos.add(appSvcPersonnelDto);
            }
        }
        return appSvcPersonnelDtos;
    }

    public static List<SelectOption> genPersonnelTypeSel(String currentSvcCod) {
        List<SelectOption> personnelTypeSel = IaisCommonUtils.genNewArrayList();
        if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(currentSvcCod)) {
            SelectOption personnelTypeOp1 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL, MasterCodeUtil.getCodeDesc(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL));
            SelectOption personnelTypeOp2 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST, MasterCodeUtil.getCodeDesc(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST));
            SelectOption personnelTypeOp3 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER, MasterCodeUtil.getCodeDesc(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER));
            SelectOption personnelTypeOp4 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE, MasterCodeUtil.getCodeDesc(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE));
            personnelTypeSel.add(personnelTypeOp1);
            personnelTypeSel.add(personnelTypeOp2);
            personnelTypeSel.add(personnelTypeOp3);
            personnelTypeSel.add(personnelTypeOp4);
        } else if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(currentSvcCod)) {
            SelectOption personnelTypeOp3 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER, MasterCodeUtil.getCodeDesc(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER));
            personnelTypeSel.add(personnelTypeOp3);
        } else if (AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(currentSvcCod)) {

        } else if (AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(currentSvcCod)) {

        }
        NewApplicationHelper.doSortSelOption(personnelTypeSel);
        return personnelTypeSel;
    }

    public static List<SelectOption> genPersonnelDesignSel(String currentSvcCod) {
        List<SelectOption> designation = IaisCommonUtils.genNewArrayList();

        if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(currentSvcCod)) {
            SelectOption designationOp1 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_DIAGNOSTIC_RADIOGRAPHER, ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_DIAGNOSTIC_RADIOGRAPHER);
            SelectOption designationOp2 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_RADIATION_THERAPIST, ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_RADIATION_THERAPIST);
            SelectOption designationOp3 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_NUCLEAR_MEDICINE_TECHNOLOGIST, ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_NUCLEAR_MEDICINE_TECHNOLOGIST);
            designation.add(designationOp1);
            designation.add(designationOp3);
            designation.add(designationOp2);
        } else if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(currentSvcCod)) {

        } else if (AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(currentSvcCod)) {
            SelectOption designationOp1 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_MEDICAL_PRACTITIONER, ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_MEDICAL_PRACTITIONER);
            SelectOption designationOp2 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_CLINICAL_NURSE_LEADER, ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_CLINICAL_NURSE_LEADER);
            designation.add(designationOp2);
            designation.add(designationOp1);
        } else if (AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(currentSvcCod)) {

        }
        NewApplicationHelper.doSortSelOption(designation);
        if(designation.size() > 0){
            designation.add(new SelectOption(NewApplicationConstant.DESIGNATION_OTHERS,NewApplicationConstant.DESIGNATION_OTHERS));
        }
        return designation;
    }

    public static List<SelectOption> getAssignPrincipalOfficerSel(HttpServletRequest request, boolean needFirstOpt) {
        List<SelectOption> assignSelectList = IaisCommonUtils.genNewArrayList();
        if (needFirstOpt) {
            SelectOption assignOp1 = new SelectOption("-1", NewApplicationDelegator.FIRESTOPTION);
            assignSelectList.add(assignOp1);
        }
        SelectOption assignOp2 = new SelectOption(IaisEGPConstant.ASSIGN_SELECT_ADD_NEW, "I'd like to add a new personnel");
        assignSelectList.add(assignOp2);
        //get current cgo,po,dpo,medAlert
        Map<String, AppSvcPrincipalOfficersDto> psnMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.PERSONSELECTMAP);
        psnMap.forEach((k, v) -> {
            SelectOption sp = new SelectOption(k, v.getName() + v.getIdNo());
            assignSelectList.add(sp);
        });

        return assignSelectList;
    }

    public static List<SelectOption> getAssignMedAlertSel(boolean needFirstOpt) {
        List<SelectOption> assignSelectList = IaisCommonUtils.genNewArrayList();
        if (needFirstOpt) {
            SelectOption assignOp1 = new SelectOption("-1", NewApplicationDelegator.FIRESTOPTION);
            assignSelectList.add(assignOp1);
        }
        SelectOption assignOp2 = new SelectOption(IaisEGPConstant.ASSIGN_SELECT_ADD_NEW, "I'd like to add a new personnel");
        assignSelectList.add(assignOp2);
        return assignSelectList;
    }

    public static List<SelectOption> getMedAlertSelectList() {
        List<SelectOption> MedAlertSelectList = IaisCommonUtils.genNewArrayList();
        SelectOption idType0 = new SelectOption("", NewApplicationDelegator.FIRESTOPTION);
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
    }

    private List<AppSvcPrincipalOfficersDto> genAppSvcKeyAppointmentHolder(HttpServletRequest request, String appType) {
        List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderDtoList = IaisCommonUtils.genNewArrayList();
        int keyAppointmentHolderLength = ParamUtil.getInt(request,"keyAppointmentHolderLength");
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(request, currentSvcId);
        boolean isRfi = NewApplicationHelper.checkIsRfi(request);
        for(int i = 0; i < keyAppointmentHolderLength; i++){
            boolean getDataByIndexNo = false;
            boolean getPageData = false;
            String isPartEdit = ParamUtil.getString(request,"isPartEdit"+i);
            String indexNo = ParamUtil.getString(request,"indexNo"+i);
            if(!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                getPageData = true;
            }else if(AppConsts.YES.equals(isPartEdit)){
                getPageData = true;
            }else if(!StringUtil.isEmpty(indexNo)){
                getDataByIndexNo = true;
            }
            AppSvcPrincipalOfficersDto appSvcKeyAppointmentHolderDto = null;
            if(getDataByIndexNo){
                appSvcKeyAppointmentHolderDto = getKeyAppointmentHolderByIndexNo(appSvcRelatedInfoDto, indexNo);
            }else if(getPageData){
                String assignSel = ParamUtil.getString(request,"assignSel"+i);
                String name = ParamUtil.getString(request,"name"+i);
                String salutation = ParamUtil.getString(request,"salutation"+i);
                String idType = ParamUtil.getString(request,"idType"+i);
                String idNo = ParamUtil.getString(request,"idNo"+i);
                appSvcKeyAppointmentHolderDto = NewApplicationHelper.getPsnInfoFromLic(request, assignSel);
                appSvcKeyAppointmentHolderDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_KAH);
                if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType) || !NewApplicationHelper.isEmpty(assignSel)) {
                    appSvcKeyAppointmentHolderDto.setAssignSelect(assignSel);
                } else {
                    appSvcKeyAppointmentHolderDto.setAssignSelect(NewApplicationHelper.getAssignSelect(idType, idNo,
                            "-1"));
                }
                AppPsnEditDto appPsnEditDto = appSvcKeyAppointmentHolderDto.getPsnEditDto();
                if (appPsnEditDto == null) {
                    appPsnEditDto = NewApplicationHelper.setNeedEditField(appSvcKeyAppointmentHolderDto);
                    appSvcKeyAppointmentHolderDto.setPsnEditDto(appPsnEditDto);
                }
                boolean partEdit = AppConsts.YES.equals(isPartEdit) && !StringUtil.isEmpty(indexNo);
                boolean isNewOfficer = IaisEGPConstant.ASSIGN_SELECT_ADD_NEW.equals(assignSel) || !appSvcKeyAppointmentHolderDto.isLicPerson();
                if (isNewOfficer && (appPsnEditDto.isSalutation() || partEdit)) {
                    appSvcKeyAppointmentHolderDto.setSalutation(salutation);
                }
                if (isNewOfficer && (appPsnEditDto.isName() || partEdit)) {
                    appSvcKeyAppointmentHolderDto.setName(name);
                }
                if (isNewOfficer && (appPsnEditDto.isIdType() || partEdit)) {
                    appSvcKeyAppointmentHolderDto.setIdType(idType);
                }
                if (isNewOfficer && (appPsnEditDto.isIdNo() || partEdit)) {
                    appSvcKeyAppointmentHolderDto.setIdNo(idNo);
                }
                if (StringUtil.isEmpty(indexNo)) {
                    appSvcKeyAppointmentHolderDto.setIndexNo(UUID.randomUUID().toString());
                } else {
                    appSvcKeyAppointmentHolderDto.setIndexNo(indexNo);
                }
            }
            if (appSvcKeyAppointmentHolderDto == null) {
                appSvcKeyAppointmentHolderDto = new AppSvcPrincipalOfficersDto();
                appSvcKeyAppointmentHolderDto.setIndexNo(UUID.randomUUID().toString());
            }
            appSvcKeyAppointmentHolderDtoList.add(appSvcKeyAppointmentHolderDto);
        }
        return appSvcKeyAppointmentHolderDtoList;
    }

    private List<AppSvcPrincipalOfficersDto> genAppSvcMedAlertPerson(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("genAppSvcMedAlertPerson star ..."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        String appType = appSubmissionDto.getAppType();
        boolean isRfi = NewApplicationHelper.checkIsRfi(request);
        boolean rfcOrRenew = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType);
        boolean needEdit = rfcOrRenew || isRfi;
        String[] existingPsn = ParamUtil.getStrings(request, "existingPsn");
        String[] licPerson = ParamUtil.getStrings(request, "licPerson");
        String[] assignSelect = ParamUtil.getStrings(request, "assignSel");
        String[] salutation = ParamUtil.getStrings(request, "salutation");
        String[] name = ParamUtil.getStrings(request, "name");
        String[] idType = ParamUtil.getStrings(request, "idType");
        String[] idNo = ParamUtil.getStrings(request, "idNo");
        String[] mobileNo = ParamUtil.getStrings(request, "mobileNo");
        String[] emailAddress = ParamUtil.getStrings(request, "emailAddress");
        String[] isPartEdit = ParamUtil.getStrings(request,"isPartEdit");
        String[] mapIndexNos = ParamUtil.getStrings(request,"mapIndexNo");
        String[] loadingTypes = ParamUtil.getStrings(request,"loadingType");
        List<AppSvcPrincipalOfficersDto> medAlertPersons = IaisCommonUtils.genNewArrayList();
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(request, currentSvcId);
        int length = 0;
        if(assignSelect != null){
            length = assignSelect.length;
        }
        //new and not rfi
        for (int i = 0; i < length; i++) {
            AppPsnEditDto appPsnEditDto = new AppPsnEditDto();
            AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
            String assign = assignSelect[i];
            String licPsn = licPerson[i];
            boolean chooseExisting = false;
            boolean getPageData = false;
            String loadingType = loadingTypes[i];
            boolean loadingByBlur = NewApplicationConstant.NEW_PSN.equals(assign) && AppConsts.YES.equals(licPsn) && ApplicationConsts.PERSON_LOADING_TYPE_BLUR.equals(loadingType);
            String existPsn = existingPsn[i];
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                if (assign != null) {
                    if (isExistingPsn(assign, licPsn)) {
                        chooseExisting = true;
                    }else if(loadingByBlur){
                        chooseExisting = true;
                    } else {
                        getPageData = true;
                    }
                }
            } else if (needEdit) {
                if (assign != null) {
                    String mapIndexNo = mapIndexNos[i];
                    if (!StringUtil.isEmpty(mapIndexNo)) {
                        //not click edit
                        if (AppConsts.NO.equals(isPartEdit[i])) {
                            appSvcPrincipalOfficersDto = getPsnByIndexNo(appSvcRelatedInfoDto, mapIndexNo,PAGE_NAME_MAP);
                            medAlertPersons.add(appSvcPrincipalOfficersDto);
                            //change arr
                            mapIndexNos = removeArrIndex(mapIndexNos, i);
                            isPartEdit = removeArrIndex(isPartEdit, i);
                            licPerson = removeArrIndex(licPerson, i);
                            loadingTypes = removeArrIndex(loadingTypes,i);
                            //dropdown cannot disabled
                            assignSelect = removeArrIndex(assignSelect, i);
                            salutation = removeArrIndex(salutation, i);
                            idType = removeArrIndex(idType, i);
//                            designation = removeArrIndex(designation, i);
//                            existingPsn = removeArrIndex(existingPsn, i);
                            //change arr index
                            --i;
                            --length;
                            continue;
                        }
                    }
                    //isPartEdit->1.click edit button 2.add more psn
                    if(isExistingPsn(assign,licPsn)){
                        //add cgo and choose existing
                        chooseExisting = true;
                    }else if(loadingByBlur){
                        chooseExisting = true;
                    }else{
                        getPageData = true;
                    }
                }
            } else {
                log.info(StringUtil.changeForLog("The current type is not supported"));
            }
            log.info(StringUtil.changeForLog("chooseExisting:"+chooseExisting));
            log.info(StringUtil.changeForLog("getPageData:"+getPageData));
            String assignSel = assignSelect[i];
            if(chooseExisting){
                if(loadingByBlur){
                    assignSel = NewApplicationHelper.getPersonKey(idType[i],idNo[i]);
                }
                appSvcPrincipalOfficersDto = NewApplicationHelper.getPsnInfoFromLic(request, assignSel);
                appSvcPrincipalOfficersDto.setLoadingType(loadingType);
                try {
                    appPsnEditDto = NewApplicationHelper.setNeedEditField(appSvcPrincipalOfficersDto);
                } catch (Exception e) {
                    clearAppPsnEditDto(appPsnEditDto);
                    log.error(e.getMessage(), e);
                }
                if (appPsnEditDto.isSalutation()) {
                    NewApplicationHelper.setPsnValue(salutation, i, appSvcPrincipalOfficersDto, "salutation");
                }
                if (appPsnEditDto.isIdType()) {
                    NewApplicationHelper.setPsnValue(idType, i, appSvcPrincipalOfficersDto, "idType");
                }
                //input
                if (appPsnEditDto.isName()) {
                    name = NewApplicationHelper.setPsnValue(name, i, appSvcPrincipalOfficersDto, "name");
                }
                if (appPsnEditDto.isIdNo()) {
                    idNo = NewApplicationHelper.setPsnValue(idNo, i, appSvcPrincipalOfficersDto, "idNo");
                }
                if (appPsnEditDto.isMobileNo()) {
                    mobileNo = NewApplicationHelper.setPsnValue(mobileNo, i, appSvcPrincipalOfficersDto, "mobileNo");
                }
                if (appPsnEditDto.isEmailAddr()) {
                    emailAddress = NewApplicationHelper.setPsnValue(emailAddress, i, appSvcPrincipalOfficersDto, "emailAddr");
                }
                String mapIndexNo = mapIndexNos[i];
                if(!StringUtil.isEmpty(mapIndexNo)){
                    appSvcPrincipalOfficersDto.setIndexNo(mapIndexNo);
                }
                if(StringUtil.isEmpty(appSvcPrincipalOfficersDto.getIndexNo())){
                    appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                }
                appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
                appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                appSvcPrincipalOfficersDto.setLicPerson(true);
                appSvcPrincipalOfficersDto.setSelectDropDown(true);
                appSvcPrincipalOfficersDto.setPsnEditDto(appPsnEditDto);
                medAlertPersons.add(appSvcPrincipalOfficersDto);
                //change arr index
                licPerson = removeArrIndex(licPerson, i);
                existingPsn = removeArrIndex(existingPsn,i);
                loadingTypes = removeArrIndex(loadingTypes,i);
                //dropdown cannot disabled
                assignSelect = removeArrIndex(assignSelect, i);
                salutation = removeArrIndex(salutation, i);
                idType = removeArrIndex(idType, i);
                --i;
                --length;
            }else if(getPageData){
                String mapIndexNo = mapIndexNos[i];
                if (StringUtil.isEmpty(mapIndexNo)) {
                    appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                } else {
                    appSvcPrincipalOfficersDto.setIndexNo(mapIndexNo);
                }
                appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
                appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                appSvcPrincipalOfficersDto.setSalutation(salutation[i]);
                appSvcPrincipalOfficersDto.setName(name[i]);
                appSvcPrincipalOfficersDto.setIdType(idType[i]);
                appSvcPrincipalOfficersDto.setIdNo(StringUtil.toUpperCase(idNo[i]));
                appSvcPrincipalOfficersDto.setMobileNo(mobileNo[i]);
                String emailAddr = "";
                if(emailAddress != null){
                    if(!StringUtil.isEmpty(emailAddress[i])){
                        emailAddr = StringUtil.viewHtml(emailAddress[i]);
                    }
                }
                appSvcPrincipalOfficersDto.setEmailAddr(emailAddr);
                if (needEdit && AppConsts.YES.equals(licPsn)) {
                    appSvcPrincipalOfficersDto.setLicPerson(true);
                    AppSvcPrincipalOfficersDto licsPerson = NewApplicationHelper.getPsnInfoFromLic(request, NewApplicationHelper.getPersonKey(appSvcPrincipalOfficersDto.getIdType(),appSvcPrincipalOfficersDto.getIdNo()));
                    if(licsPerson != null){
                        appSvcPrincipalOfficersDto.setCurPersonelId(licsPerson.getCurPersonelId());
                    }
                }
                medAlertPersons.add(appSvcPrincipalOfficersDto);
            }
        }
        log.info(StringUtil.changeForLog("genAppSvcMedAlertPerson end ..."));
        return medAlertPersons;
    }

    private AppSvcPrincipalOfficersDto getAppSvcCgoByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo) {
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)) {
            List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcCgoDtos)) {
                for (AppSvcPrincipalOfficersDto appSvcCgoDto1 : appSvcCgoDtos) {
                    if (indexNo.equals(appSvcCgoDto1.getIndexNo())) {
                        return appSvcCgoDto1;
                    }
                }
            }
        }
        return new AppSvcPrincipalOfficersDto();
    }

    private AppSvcPersonnelDto getAppSvcPersonnelDtoByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo){
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)) {
            List<AppSvcPersonnelDto> appSvcPersonnelDtoList = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcPersonnelDtoList)) {
                for (AppSvcPersonnelDto appSvcPersonnelDto : appSvcPersonnelDtoList) {
                    if (indexNo.equals(appSvcPersonnelDto.getIndexNo())) {
                        return appSvcPersonnelDto;
                    }
                }
            }
        }
        return new AppSvcPersonnelDto();
    }

    private AppSvcPrincipalOfficersDto getPsnByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo, String pageName){
        if(appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)){
            List<AppSvcPrincipalOfficersDto> psnDtos = null;
            if(PAGE_NAME_PO.equals(pageName)){
                psnDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
            }else if(PAGE_NAME_MAP.equals(pageName)){
                psnDtos = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
            }
            if(!IaisCommonUtils.isEmpty(psnDtos)){
                for(AppSvcPrincipalOfficersDto psnDto:psnDtos){
                    if (indexNo.equals(psnDto.getIndexNo())) {
                        return psnDto;
                    }
                }
            }
        }
        return new AppSvcPrincipalOfficersDto();
    }

    private String[] removeArrItem(String[] arrs, String item) {
        if (arrs == null || StringUtil.isEmpty(item)) {
            return arrs;
        }
        String[] newArrs = new String[arrs.length - 1];
        int i = 0;
        for (String arr : arrs) {
            if (!item.equals(arr)) {
                newArrs[i] = arr;
                i++;
            }
        }
        return newArrs;
    }

    private String[] removeArrIndex(String[] arrs, int index) {
        if (arrs == null) {
            return new String[]{""};
        }
        String[] newArrs = new String[arrs.length - 1];
        int j = 0;
        for (int i = 0; i < arrs.length; i++) {
            if (i != index) {
                newArrs[j] = arrs[i];
                j++;
            }
        }
        return newArrs;
    }

    private void removeEmptyPsn(List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos,List<AppSvcPrincipalOfficersDto> reloadDto) throws Exception {
        for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto:appSvcPrincipalOfficersDtos){
            boolean isAllFieldNull = NewApplicationHelper.isAllFieldNull(appSvcPrincipalOfficersDto);
            if(!isAllFieldNull){
                reloadDto.add(appSvcPrincipalOfficersDto);
            }
        }
    }

    private boolean isExistingPsn(String assign,String licPsn){
        return !NewApplicationConstant.NEW_PSN.equals(assign) && !assign.equals("-1")&&AppConsts.YES.equals(licPsn);
    }

    private void clearAppPsnEditDto(AppPsnEditDto appPsnEditDto){
        appPsnEditDto.setName(false);
        appPsnEditDto.setSalutation(false);
        appPsnEditDto.setIdType(false);
        appPsnEditDto.setIdNo(false);
        appPsnEditDto.setDesignation(false);
        appPsnEditDto.setMobileNo(false);
        appPsnEditDto.setOfficeTelNo(false);
        appPsnEditDto.setEmailAddr(false);
        appPsnEditDto.setProfessionType(false);
        appPsnEditDto.setProfRegNo(false);
        appPsnEditDto.setSpeciality(false);
        appPsnEditDto.setSpecialityOther(false);
        appPsnEditDto.setSubSpeciality(false);
        appPsnEditDto.setDesignation(false);
    }

    private Map<String,AppSvcPersonAndExtDto> syncDropDownAndPsn(AppSubmissionDto appSubmissionDto,List<AppSvcPrincipalOfficersDto> personList,
            String svcCode, HttpServletRequest request){
        if (StringUtil.isEmpty(svcCode)) {
            svcCode = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSVCCODE);
        }
        Map<String,AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(request,NewApplicationDelegator.PERSONSELECTMAP);
        if (personList == null || personList.isEmpty()) {
            return personMap;
        }
        Map<String,AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(request,NewApplicationDelegator.LICPERSONSELECTMAP);
        personMap = syncDropDownAndPsn(personMap,appSubmissionDto,personList,svcCode);
        Map<String,AppSvcPersonAndExtDto> newPersonMap = removeDirtyDataFromPsnDropDown(appSubmissionDto,licPersonMap,personMap);
        ParamUtil.setSessionAttr(request,NewApplicationDelegator.PERSONSELECTMAP, (Serializable) newPersonMap);
        //remove dirty psn doc info
        String dupForPerson = null;
        String personType = personList.get(0).getPsnType();
        if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(personType)) {
            dupForPerson = ApplicationConsts.DUP_FOR_PERSON_CGO;
        } else if (ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR.equals(personType)) {
            dupForPerson = ApplicationConsts.DUP_FOR_PERSON_CD;
        }
        if (!StringUtil.isEmpty(dupForPerson)) {
            String currentSvcId = getCurrentServiceId(request);
            AppSvcRelatedInfoDto currentSvcRelatedDto = getAppSvcRelatedInfo(appSubmissionDto, currentSvcId, null);
            List<HcsaSvcDocConfigDto> svcDocConfigDtos = serviceConfigService.getAllHcsaSvcDocs(currentSvcId);
            List<AppSvcPrincipalOfficersDto> newList = NewApplicationHelper.transferCgoToPsnDtoList(personList);
            currentSvcRelatedDto = removeDirtyPsnDoc(currentSvcRelatedDto, svcDocConfigDtos, newList, dupForPerson);
            setAppSvcRelatedInfoMap(request, currentSvcId, currentSvcRelatedDto, appSubmissionDto);
        } else {
            ParamUtil.setSessionAttr(request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
        }

        return newPersonMap;
    }

    private Map<String,AppSvcPersonAndExtDto> syncDropDownAndPsn(Map<String,AppSvcPersonAndExtDto> personMap,AppSubmissionDto appSubmissionDto,List<AppSvcPrincipalOfficersDto> personList,String svcCode){
        List<AppSvcPrincipalOfficersDto> newPersonList = IaisCommonUtils.genNewArrayList();
        for(AppSvcPrincipalOfficersDto person:personList){
            String idType = person.getIdType();
            String idNo = person.getIdNo();
            String name = person.getName();
            //Provisional judgment
            //personnel data=>sync , personnel ext data => the same svc =>sync
            boolean needSync = !StringUtil.isEmpty(idType) && !StringUtil.isEmpty(idNo) && !StringUtil.isEmpty(name);
            if(needSync){
                newPersonList.add(person);
            }
        }
        //set person into dropdown
        personMap = NewApplicationHelper.initSetPsnIntoSelMap(personMap, newPersonList, svcCode);
        //sync data
        NewApplicationHelper.syncPsnData(appSubmissionDto, personMap);
        return personMap;
    }

    private String getStepName(BaseProcessClass bpc,String currSvcId,String stepCode){
        String stepName = "";
        ServiceStepDto serviceStepDto = (ServiceStepDto) ParamUtil.getSessionAttr(bpc.request, ShowServiceFormsDelegator.SERVICESTEPDTO);
        if(serviceStepDto != null && !StringUtil.isEmpty(currSvcId)){
            List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = serviceStepDto.getHcsaServiceStepSchemeDtos();
            if(!IaisCommonUtils.isEmpty(hcsaServiceStepSchemeDtos)){
                for(HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto:hcsaServiceStepSchemeDtos){
                    if(currSvcId.equals(hcsaServiceStepSchemeDto.getServiceId()) && stepCode.equals(hcsaServiceStepSchemeDto.getStepCode())){
                        stepName = hcsaServiceStepSchemeDto.getStepName();
                        break;
                    }
                }
            }
        }
        return stepName;
    }

    private Integer getAppSvcDocVersion(String configDocId, List<AppSvcDocDto> oldDocs, boolean isRfi, String md5Code, String appGrpId,String appNo,int seqNum,String dupForPrem,String dupForPerson,String psnId){
        Integer version = 1;
        if(StringUtil.isEmpty(configDocId) || IaisCommonUtils.isEmpty(oldDocs) || StringUtil.isEmpty(md5Code)){
            return version;
        }
        if(isRfi){
            boolean canFound = false;
            log.info(StringUtil.changeForLog("rfi appNo:"+appNo));
            for(AppSvcDocDto appSvcDocDto:oldDocs){
                Integer oldVersion = appSvcDocDto.getVersion();
                if(configDocId.equals(appSvcDocDto.getSvcDocId()) && seqNum == appSvcDocDto.getSeqNum()){
                    canFound = true;
                    if (MessageDigest.isEqual(md5Code.getBytes(StandardCharsets.UTF_8),
                            appSvcDocDto.getMd5Code().getBytes(StandardCharsets.UTF_8))) {
                        if(!StringUtil.isEmpty(oldVersion)){
                            version = oldVersion;
                        }
                    }else{
                        version = getVersion(appGrpId,configDocId,appNo,seqNum,dupForPrem,dupForPerson,psnId);
                    }
                    break;
                }
            }
            if(!canFound){
                //last doc is null
                version = getVersion(appGrpId,configDocId,appNo,seqNum,dupForPrem,dupForPerson,psnId);
            }
        }
        return version;
    }

    private Integer getVersion(String appGrpId,String configDocId,String appNo,Integer seqNum,String dupForPrem,String dupForPerson,String psnId){
        Integer version = 1;
        AppSvcDocDto searchDto = new AppSvcDocDto();
        searchDto.setAppGrpId(appGrpId);
        searchDto.setSvcDocId(configDocId);
        searchDto.setSeqNum(seqNum);
        if("0".equals(dupForPrem)){
            version = getMaxVersion(dupForPerson,searchDto,appNo,psnId,version);
        }else if("1".equals(dupForPrem)){
            version = getMaxVersion(dupForPerson,searchDto,appNo,psnId,version);
        }
        return version;
    }

    private Set<String> getNewPersonKeySet(AppSubmissionDto appSubmissionDto){
        Set<String> personKeySet = IaisCommonUtils.genNewHashSet();
        if(appSubmissionDto != null){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    // Clinical director
                    List<AppSvcPrincipalOfficersDto> appSvCdDtos = appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();
                    if (!IaisCommonUtils.isEmpty(appSvCdDtos)) {
                        for (AppSvcPrincipalOfficersDto psn : appSvCdDtos) {
                            setPsnKeySet(psn, personKeySet);
                        }
                    }
                    // CGO
                    List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                    if(!IaisCommonUtils.isEmpty(appSvcCgoDtos)){
                        for(AppSvcPrincipalOfficersDto psn:appSvcCgoDtos){
                            setPsnKeySet(psn,personKeySet);
                        }
                    }
                    List<AppSvcPrincipalOfficersDto> appSvcPoDpoDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                    if(!IaisCommonUtils.isEmpty(appSvcPoDpoDtos)){
                        for(AppSvcPrincipalOfficersDto psn:appSvcPoDpoDtos){
                            setPsnKeySet(psn,personKeySet);
                        }
                    }
                    List<AppSvcPrincipalOfficersDto> appSvcMapDtos = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
                    if(!IaisCommonUtils.isEmpty(appSvcMapDtos)){
                        for(AppSvcPrincipalOfficersDto psn:appSvcMapDtos){
                            setPsnKeySet(psn,personKeySet);
                        }
                    }
                    //Key Appointment Holder
                    List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderDtoList = appSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList();
                    if(!IaisCommonUtils.isEmpty(appSvcKeyAppointmentHolderDtoList)){
                        for(AppSvcPrincipalOfficersDto psn:appSvcKeyAppointmentHolderDtoList){
                            setPsnKeySet(psn,personKeySet);
                        }
                    }
                }
            }
        }
        return personKeySet;
    }

    private Map<String,AppSvcPersonAndExtDto> removeDirtyDataFromPsnDropDown(AppSubmissionDto appSubmissionDto,Map<String,AppSvcPersonAndExtDto> licPersonMap,Map<String,AppSvcPersonAndExtDto> personMap){
        //add new person key
        Set<String> personKeySet = new HashSet<>(licPersonMap.keySet());
        Set<String> newPersonKeySet = getNewPersonKeySet(appSubmissionDto);
        personKeySet.addAll(newPersonKeySet);
        //filter removed person
        Map<String,AppSvcPersonAndExtDto> newPersonMap = IaisCommonUtils.genNewHashMap();
        Set<String> finalPersonKeySet = personKeySet;
        personMap.forEach((k, v)->{
            if(finalPersonKeySet.contains(k)){
                newPersonMap.put(k,v);
            }
        });
        return newPersonMap;
    }

    private void setPsnKeySet(AppSvcPrincipalOfficersDto psn,Set<String> personKeySet){
        if (psn == null || personKeySet == null) {
            return;
        }
        String assignSel = psn.getAssignSelect();
        if(!psn.isLicPerson()){
            boolean partValidate = NewApplicationHelper.psnDoPartValidate(psn.getIdType(),psn.getIdNo(),psn.getName());
            if(partValidate){
                personKeySet.add(NewApplicationHelper.getPersonKey(psn.getIdType(),psn.getIdNo()));
            }else if(!StringUtil.isEmpty(assignSel) && !"-1".equals(assignSel)){
                psn.setAssignSelect(NewApplicationConstant.NEW_PSN);
            }
        }else{
            personKeySet.add(NewApplicationHelper.getPersonKey(psn.getIdType(),psn.getIdNo()));
        }

    }

    private  void genSvcDoc(Map<String, File> fileMap, String docKey, HcsaSvcDocConfigDto hcsaSvcDocConfigDto, Map<String,File> saveFileMap,
                                  List<AppSvcDocDto> currSvcDocDtoList, List<AppSvcDocDto> newAppSvcDocDtos,List<AppSvcDocDto> oldDocs,String premVal,
                                  String premType,String psnIndexNo,String dupForPrem,String dupForPerson,boolean isRfi,String appGrpId,String appNo,String psnId){
        if(fileMap != null){
            fileMap.forEach((k,v)->{
                int index = k.indexOf(docKey);
                String seqNumStr = k.substring(index+docKey.length());
                int seqNum = -1;
                try{
                    seqNum = Integer.parseInt(seqNumStr);
                }catch (Exception e){
                    log.error(StringUtil.changeForLog("doc seq num can not parse to int"));
                }
                AppSvcDocDto appSvcDocDto = new AppSvcDocDto();
                if(v != null){
                    appSvcDocDto.setSvcDocId(hcsaSvcDocConfigDto.getId());
                    appSvcDocDto.setUpFileName(hcsaSvcDocConfigDto.getDocTitle());
                    appSvcDocDto.setDocName(v.getName());
                    long size = v.length() / 1024;
                    appSvcDocDto.setDocSize(Integer.valueOf(String.valueOf(size)));
                    String md5Code = SingeFileUtil.getInstance().getFileMd5(v);
                    appSvcDocDto.setMd5Code(md5Code);
                    //if  common ==> set null
                    appSvcDocDto.setPremisesVal(premVal);
                    appSvcDocDto.setPremisesType(premType);
                    appSvcDocDto.setPsnIndexNo(psnIndexNo);
                    appSvcDocDto.setSeqNum(seqNum);
                    appSvcDocDto.setDupForPerson(dupForPerson);
                    appSvcDocDto.setVersion(getAppSvcDocVersion(hcsaSvcDocConfigDto.getId(),oldDocs,isRfi,md5Code,appGrpId,appNo,seqNum,dupForPrem,dupForPerson,psnId));
                    appSvcDocDto.setPersonType(NewApplicationHelper.getPsnType(dupForPerson));
                    saveFileMap.put(premVal+hcsaSvcDocConfigDto.getId()+psnIndexNo+seqNum,v);
                }else{
                    appSvcDocDto = getAppSvcDocByConfigIdAndSeqNum(currSvcDocDtoList,hcsaSvcDocConfigDto.getId(),seqNum,premVal,premType,psnIndexNo);
                }
                //the data is retrieved from the DTO a second time
                fileMap.put(k,null);
                if(appSvcDocDto != null){
                    newAppSvcDocDtos.add(appSvcDocDto);
                }
            });
        }
    }

    private static AppSvcDocDto getAppSvcDocByConfigIdAndSeqNum(List<AppSvcDocDto> appSvcDocDtos,String configId,int seqNum,String premVal,String premType,String psnIndexNo){
        log.debug(StringUtil.changeForLog("getAppGrpPrimaryDocByConfigIdAndSeqNum start..."));
        AppSvcDocDto appSvcDocDto= null;
        if(!IaisCommonUtils.isEmpty(appSvcDocDtos)){
            log.debug("configId is {}", configId);
            log.debug("seqNum is {}", seqNum);
            log.debug("premIndex is {}", premVal);
            log.debug("psnIndexNo is {}", psnIndexNo);
            for(AppSvcDocDto appSvcDocDto1:appSvcDocDtos){
                String currPremVal = "";
                String currPremType = "";
                String currPsnIndexNo = "";
                if(!StringUtil.isEmpty(appSvcDocDto1.getPremisesVal())){
                    currPremVal =appSvcDocDto1.getPremisesVal();
                }
                if(!StringUtil.isEmpty(appSvcDocDto1.getPremisesType())){
                    currPremType = appSvcDocDto1.getPremisesType();
                }
                if(!StringUtil.isEmpty(appSvcDocDto1.getPsnIndexNo())){
                    currPsnIndexNo = appSvcDocDto1.getPsnIndexNo();
                }
                if(configId.equals(appSvcDocDto1.getSvcDocId())
                        && seqNum == appSvcDocDto1.getSeqNum()
                        && premVal.equals(currPremVal)
                        && premType.equals(currPremType)
                        && psnIndexNo.equals(currPsnIndexNo)){
                    try {
                        appSvcDocDto = (AppSvcDocDto) CopyUtil.copyMutableObject(appSvcDocDto1);
                    } catch (Exception e) {
                        log.error(StringUtil.changeForLog("copy appSvcDocDto error !!!"));
                    }
                    break;
                }
            }
        }
        return appSvcDocDto;
    }

    private void saveSvcFileAndSetFileId(List<AppSvcDocDto> appSvcDocDtos, Map<String,File> saveFileMap){
        Map<String,File> passValidateFileMap = IaisCommonUtils.genNewLinkedHashMap();
        for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
            if(appSvcDocDto.isPassValidate()){
                String premIndexNo = "";
                if(!StringUtil.isEmpty(appSvcDocDto.getPremisesVal())){
                    premIndexNo = appSvcDocDto.getPremisesVal();
                }
                String psnIndexNo = "";
                if(!StringUtil.isEmpty(appSvcDocDto.getPsnIndexNo())){
                    psnIndexNo = appSvcDocDto.getPsnIndexNo();
                }
                String fileMapKey = premIndexNo + appSvcDocDto.getSvcDocId() + psnIndexNo + appSvcDocDto.getSeqNum();
                File file = saveFileMap.get(fileMapKey);
                if(file != null){
                    passValidateFileMap.put(fileMapKey,file);
                }
            }
        }
        if(passValidateFileMap.size() > 0){
            List<File> fileList = new ArrayList<>(passValidateFileMap.values());
            List<String> fileRepoIdList = appSubmissionService.saveFileList(fileList);
            int i = 0;
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                String premIndexNo = "";
                if(!StringUtil.isEmpty(appSvcDocDto.getPremisesVal())){
                    premIndexNo = appSvcDocDto.getPremisesVal();
                }
                String psnIndexNo = "";
                if(!StringUtil.isEmpty(appSvcDocDto.getPsnIndexNo())){
                    psnIndexNo = appSvcDocDto.getPsnIndexNo();
                }
                String saveFileMapKey = premIndexNo + appSvcDocDto.getSvcDocId()+ psnIndexNo + appSvcDocDto.getSeqNum();
                File file = saveFileMap.get(saveFileMapKey);
                if(file != null){
                    appSvcDocDto.setFileRepoId(fileRepoIdList.get(i));
                    i++;
                }
            }
        }
    }

    private void genSvcPersonDoc(HcsaSvcDocConfigDto hcsaSvcDocConfigDto,AppSvcRelatedInfoDto appSvcRelatedInfoDto,String docKey,Map<String,File> saveFileMap,
                                    List<AppSvcDocDto> currSvcDocDtoList, List<AppSvcDocDto> newAppSvcDocDtos,List<AppSvcDocDto> oldDocs,
                                    boolean isRfi,String appGrpId,String appNo,String premVal,String premType,MultipartHttpServletRequest mulReq,HttpServletRequest request,int [] psnTypeNumArr){

        String dupForPerson = hcsaSvcDocConfigDto.getDupForPerson();
        String configId = hcsaSvcDocConfigDto.getId();
        if(StringUtil.isEmpty(dupForPerson)){
            Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(mulReq,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docKey);
            genSvcDoc(fileMap,docKey,hcsaSvcDocConfigDto,saveFileMap,currSvcDocDtoList,newAppSvcDocDtos,oldDocs,premVal,premType,"",hcsaSvcDocConfigDto.getDupForPrem(),"",isRfi,appGrpId,appNo,"");
            ParamUtil.setSessionAttr(request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docKey, (Serializable) fileMap);
            //get target doc list
            List<AppSvcDocDto> newTargetDocDtoList = NewApplicationHelper.getSvcDocumentByParams(newAppSvcDocDtos,configId,premVal,"");
            List<AppSvcDocDto> oldTargetDocDtoList = NewApplicationHelper.getSvcDocumentByParams(oldDocs,configId,premVal,"");
            //set value for be doc sort
            setValueForBeDocSort(newTargetDocDtoList,oldTargetDocDtoList,psnTypeNumArr,NewApplicationHelper.getPsnType(dupForPerson));
        }else{
            //genDupForPersonDoc(hcsaSvcDocConfigDto,appSvcRelatedInfoDto,docKey,saveFileMap,appSvcDocDtos,newAppSvcDocDtoList,oldDocs,isRfi,appGrpId,appNo,mulReq,bpc.request);
            List<AppSvcPrincipalOfficersDto> psnDtoList = NewApplicationHelper.getPsnByDupForPerson(appSvcRelatedInfoDto,hcsaSvcDocConfigDto.getDupForPerson());
            for(AppSvcPrincipalOfficersDto psnDto:psnDtoList){
                String psnIndexNo = psnDto.getIndexNo();
                String psnDocKey = docKey + psnIndexNo;
                Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(mulReq,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+psnDocKey);
                genSvcDoc(fileMap,psnDocKey,hcsaSvcDocConfigDto,saveFileMap,currSvcDocDtoList,newAppSvcDocDtos,oldDocs,premVal,premType,psnIndexNo,hcsaSvcDocConfigDto.getDupForPrem(),hcsaSvcDocConfigDto.getDupForPerson(),isRfi,appGrpId,appNo,psnDto.getCurPersonelId());
                ParamUtil.setSessionAttr(request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+psnDocKey, (Serializable) fileMap);
                //get target doc list
                List<AppSvcDocDto> newTargetDocDtoList = NewApplicationHelper.getSvcDocumentByParams(newAppSvcDocDtos,configId,premVal,psnIndexNo);
                List<AppSvcDocDto> oldTargetDocDtoList = NewApplicationHelper.getSvcDocumentByParams(oldDocs,configId,premVal,psnIndexNo);
                //set value for be doc sort
                setValueForBeDocSort(newTargetDocDtoList,oldTargetDocDtoList,psnTypeNumArr,NewApplicationHelper.getPsnType(dupForPerson));
            }
        }
    }

    private Integer getMaxVersion(String dupForPerson,AppSvcDocDto searchDto,String appNo,String psnId,Integer version){
        AppSvcDocDto maxVersionDocDto;
        if(StringUtil.isEmpty(dupForPerson)){
            maxVersionDocDto = appSubmissionService.getMaxVersionSvcSpecDoc(searchDto,appNo);
        }else if(ApplicationConsts.DUP_FOR_PERSON_SVCPSN.equals(dupForPerson)){
            searchDto.setAppSvcPersonId(psnId);
            maxVersionDocDto = appSubmissionService.getMaxVersionSvcSpecDoc(searchDto,appNo);
        }else{
            searchDto.setAppGrpPersonId(psnId);
            maxVersionDocDto = appSubmissionService.getMaxVersionSvcSpecDoc(searchDto,appNo);
        }
        if(!StringUtil.isEmpty(maxVersionDocDto.getVersion())){
            //judege dto is null
            if(!StringUtil.isEmpty(maxVersionDocDto.getFileRepoId())){
                version = maxVersionDocDto.getVersion() + 1;
            }
        }
        return version;
    }

    private int getMaxPersonTypeNumber(List<AppSvcDocDto> newAppSvcDocDtos,List<AppSvcDocDto> oldAppSvcDocDtos){
        int maxPersonTypeNumber = 0;
        maxPersonTypeNumber = getMaxNumber(maxPersonTypeNumber,newAppSvcDocDtos);
        maxPersonTypeNumber = getMaxNumber(maxPersonTypeNumber,oldAppSvcDocDtos);
        return maxPersonTypeNumber;
    }

    private int getMaxNumber(int maxPersonTypeNumber,List<AppSvcDocDto> appSvcDocDtos){
        if(!IaisCommonUtils.isEmpty(appSvcDocDtos)){
            for(AppSvcDocDto appSvcDocDto:appSvcDocDtos){
                Integer personTypeNumber = appSvcDocDto.getPersonTypeNum();
                if(personTypeNumber != null && personTypeNumber > maxPersonTypeNumber){
                    maxPersonTypeNumber = personTypeNumber;
                }
            }
        }
        return maxPersonTypeNumber;
    }

    private void setValueForBeDocSort(List<AppSvcDocDto> newAppSvcDocDtos,List<AppSvcDocDto> oldAppSvcDocDtos,int [] psnTypeNumArr,String personType){
        //retrieve person type number
        Integer newPsnTypeNum = null;
        if(!IaisCommonUtils.isEmpty(newAppSvcDocDtos)){
            for(AppSvcDocDto appSvcDocDto:newAppSvcDocDtos){
                if(appSvcDocDto.getPersonTypeNum() != null){
                    newPsnTypeNum = appSvcDocDto.getPersonTypeNum();
                    break;
                }
            }
        }
        Integer oldPsnTypeNum = null;
        if(newPsnTypeNum == null && !IaisCommonUtils.isEmpty(oldAppSvcDocDtos)){
            //todo:change
            for(AppSvcDocDto oldAppSvcDocDto:oldAppSvcDocDtos){
                if(oldAppSvcDocDto.getPersonTypeNum() != null){
                    oldPsnTypeNum = oldAppSvcDocDto.getPersonTypeNum();
                    break;
                }
            }
        }
        int maxPsnTypeNum = psnTypeNumArr[0];
        int currPsnTypeNum = 0;
        if(newPsnTypeNum == null && oldPsnTypeNum == null){
            currPsnTypeNum = maxPsnTypeNum + 1;
        }else if(newPsnTypeNum != null){
            currPsnTypeNum = newPsnTypeNum;
        }else if(oldPsnTypeNum != null){
            currPsnTypeNum = oldPsnTypeNum;
        }

        //insert person type,person type number
        int psnTypeNumFinal = currPsnTypeNum;
        newAppSvcDocDtos.forEach(svcDoc ->{
            svcDoc.setPersonType(personType);
            svcDoc.setPersonTypeNum(psnTypeNumFinal);
        });

        //psnTypeNum increase
        if(currPsnTypeNum > maxPsnTypeNum){
            psnTypeNumArr[0] = currPsnTypeNum;
        }
    }

    private List<HcsaSvcDocConfigDto> getDocConfigDtoByDupForPerson(List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos,String dupForPerson){
        List<HcsaSvcDocConfigDto> result = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(hcsaSvcDocConfigDtos) && !StringUtil.isEmpty(dupForPerson)){
            for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:hcsaSvcDocConfigDtos){
                if(dupForPerson.equals(hcsaSvcDocConfigDto.getDupForPerson())){
                    result.add(hcsaSvcDocConfigDto);
                }
            }
        }
        return result;
    }

    private AppSvcRelatedInfoDto removeDirtyPsnDoc(AppSvcRelatedInfoDto currentSvcRelatedDto,List<HcsaSvcDocConfigDto> svcDocConfigDtos,List<AppSvcPrincipalOfficersDto> psnDtoList,String dupForPerson){
        log.debug(StringUtil.changeForLog("remove dirty psn doc info start ..."));
        List<AppSvcDocDto> appSvcDocDtoList = currentSvcRelatedDto.getAppSvcDocDtoLit();
        List<HcsaSvcDocConfigDto> targetConfigDtos = getDocConfigDtoByDupForPerson(svcDocConfigDtos,dupForPerson);
        if(!IaisCommonUtils.isEmpty(appSvcDocDtoList) && !IaisCommonUtils.isEmpty(targetConfigDtos) && !IaisCommonUtils.isEmpty(psnDtoList)){
            List<String> psnIndexList = IaisCommonUtils.genNewArrayList();
            for(AppSvcPrincipalOfficersDto psnDto:psnDtoList){
                if(!StringUtil.isEmpty(psnDto.getIndexNo())){
                    psnIndexList.add(psnDto.getIndexNo());
                }
            }
            List<String> targetConfigIdList = IaisCommonUtils.genNewArrayList();
            for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:targetConfigDtos){
                targetConfigIdList.add(hcsaSvcDocConfigDto.getId());
            }
            //
            List<AppSvcDocDto> newAppSvcDocDtoList = IaisCommonUtils.genNewArrayList();
            log.debug("appSvcDocDtoList size is {}", appSvcDocDtoList.size());
            for(AppSvcDocDto appSvcDocDto:appSvcDocDtoList){
                String svcDocId = appSvcDocDto.getSvcDocId();
                if(!StringUtil.isEmpty(svcDocId) && targetConfigIdList.contains(svcDocId)){
                    //judge align psn if existing
                    String psnIndexNo = appSvcDocDto.getPsnIndexNo();
                    if(psnIndexList.contains(psnIndexNo)){
                        newAppSvcDocDtoList.add(appSvcDocDto);
                    }
                }else{
                    newAppSvcDocDtoList.add(appSvcDocDto);
                }
            }
            log.debug("newAppSvcDocDtoList size is {}", newAppSvcDocDtoList.size());
            currentSvcRelatedDto.setAppSvcDocDtoLit(newAppSvcDocDtoList);
        }
        log.debug(StringUtil.changeForLog("remove dirty psn doc info end ..."));
        return currentSvcRelatedDto;
    }

    private Map<String,String> servicePersonPrsValidate(HttpServletRequest request, Map<String,String> errorMap, List<AppSvcPersonnelDto> appSvcPersonnelDtos){
        if(!IaisCommonUtils.isEmpty(appSvcPersonnelDtos)){
            for (int i = 0; i < appSvcPersonnelDtos.size(); i++) {
                AppSvcPersonnelDto appSvcPersonnelDto = appSvcPersonnelDtos.get(i);
                String profRegNo = appSvcPersonnelDto.getProfRegNo();
                if (!StringUtil.isEmpty(profRegNo)) {
                    ProfessionalResponseDto professionalResponseDto = appSubmissionService.retrievePrsInfo(profRegNo);
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

    private List<AppSvcBusinessDto> genAppSvcBusinessDtoList(HttpServletRequest request, List<AppGrpPremisesDto> appGrpPremisesDtos, String appType){
        List<AppSvcBusinessDto> appSvcBusinessDtos = IaisCommonUtils.genNewArrayList();
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(request, currentSvcId);
        boolean isRfi = NewApplicationHelper.checkIsRfi(request);
        if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
            int i = 0;
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                AppSvcBusinessDto appSvcBusinessDto = null;
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request,"isPartEdit" + i);
                String businessIndexNo = ParamUtil.getString(request,"businessIndexNo" + i);
                if(!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                    getPageData = true;
                }else if(AppConsts.YES.equals(isPartEdit)){
                    getPageData = true;
                }else if(!StringUtil.isEmpty(businessIndexNo)){
                    getDataByIndexNo = true;
                }
                log.debug("get data by index no. is {}",getDataByIndexNo);
                log.debug("get page data is {}",getPageData);
                if(getDataByIndexNo){
                    appSvcBusinessDto = getAppSvcBusinessDtoByIndexNo(appSvcRelatedInfoDto, businessIndexNo);
                }else if(getPageData){
                    String businessName = ParamUtil.getString(request,"businessName" + i);
                    appSvcBusinessDto = new AppSvcBusinessDto();
                    appSvcBusinessDto.setBusinessName(businessName);
                    if(StringUtil.isEmpty(businessIndexNo)){
                        appSvcBusinessDto.setBusinessIndexNo(UUID.randomUUID().toString());
                    }else{
                        appSvcBusinessDto.setBusinessIndexNo(businessIndexNo);
                    }
                }
                if(appSvcBusinessDto != null){
                    appSvcBusinessDto.setPremIndexNo(appGrpPremisesDto.getPremisesIndexNo());
                    appSvcBusinessDto.setPremType(appGrpPremisesDto.getPremisesType());
                    appSvcBusinessDto.setPremAddress(appGrpPremisesDto.getAddress());
                    appSvcBusinessDtos.add(appSvcBusinessDto);
                }
                i++;
            }
        }

        return appSvcBusinessDtos;
    }

    private AppSvcBusinessDto getAppSvcBusinessDtoByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String businessIndexNo){
        AppSvcBusinessDto result = null;
        if(appSvcRelatedInfoDto != null && !StringUtil.isEmpty(businessIndexNo)){
            List<AppSvcBusinessDto> appSvcBusinessDtos = appSvcRelatedInfoDto.getAppSvcBusinessDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcBusinessDtos)){
                for(AppSvcBusinessDto appSvcBusinessDto:appSvcBusinessDtos){
                    if(businessIndexNo.equals(appSvcBusinessDto.getBusinessIndexNo())){
                        result = appSvcBusinessDto;
                        break;
                    }
                }
            }
        }
        return result;
    }

    private void reSetChangesForApp(AppSubmissionDto appSubmissionDto){
        if (appSubmissionDto.isNeedEditController()) {
            AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
            appEditSelectDto.setServiceEdit(true);
            appSubmissionDto.setChangeSelectDto(appEditSelectDto);
        }
    }
}
