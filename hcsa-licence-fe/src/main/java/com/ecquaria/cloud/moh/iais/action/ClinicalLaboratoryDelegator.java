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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcClinicalDirectorDto;
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
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.NewApplicationConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.ServiceStepDto;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.utils.SingeFileUtil;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    private FeEicGatewayClient feEicGatewayClient;
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

    //dropdown
    public static final String DROPWOWN_IDTYPESELECT = "IdTypeSelect";

    private static final String CURR_STEP_NAME = "currStepName";
    private static final String PAGE_NAME_PO = "pageNamePo";
    private static final String PAGE_NAME_MAP = "pageNameMap";
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
        String actionForm = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_TAB);
        actionForm = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_TAB);
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
        ParamUtil.setSessionAttr(bpc.request, ShowServiceFormsDelegator.SERVICESTEPDTO, (Serializable) serviceStepDto);

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
        //curr step name
        String stepName = getStepName(bpc,currentSvcId,HcsaLicenceFeConstant.LABORATORYDISCIPLINES);
        ParamUtil.setRequestAttr(bpc.request,CURR_STEP_NAME,stepName);
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
            List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
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
        //reload
        if (appSvcRelatedInfoDto != null) {
            List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            ParamUtil.setRequestAttr(bpc.request, GOVERNANCEOFFICERSDTOLIST, appSvcCgoDtoList);
        }
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
        List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
        SelectOption sp = null;
        if (appSvcCgoDtoList != null && !appSvcCgoDtoList.isEmpty()) {
            for (AppSvcCgoDto cgo : appSvcCgoDtoList) {
                sp = new SelectOption(cgo.getIdNo(), cgo.getName());
                spList.add(sp);
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "CgoSelect", (Serializable) spList);

        Map<String, String> reloadAllocation = IaisCommonUtils.genNewHashMap();
        List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        if (appSvcDisciplineAllocationDtoList != null && !appSvcDisciplineAllocationDtoList.isEmpty()) {
            for (AppSvcDisciplineAllocationDto allocationDto : appSvcDisciplineAllocationDtoList) {
                reloadAllocation.put(allocationDto.getPremiseVal() + allocationDto.getChkLstConfId(), allocationDto.getIdNo());
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "ReloadAllocationMap", (Serializable) reloadAllocation);

        //curr step name
        String stepName = getStepName(bpc,currentSvcId,HcsaLicenceFeConstant.DISCIPLINEALLOCATION);
        ParamUtil.setRequestAttr(bpc.request,CURR_STEP_NAME,stepName);
        String svcScopePageName = getStepName(bpc,currentSvcId,HcsaLicenceFeConstant.LABORATORYDISCIPLINES);
        ParamUtil.setRequestAttr(bpc.request,"svcScopePageName",svcScopePageName);
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

        log.debug(StringUtil.changeForLog("the do preparePrincipalOfficers end ...."));
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

            reloadDocMap.forEach((k,v)->{
                Collections.sort(v,(s1, s2)->(
                        s1.getSeqNum().compareTo(s2.getSeqNum())
                ));
            });
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
        if (!StringUtil.isEmpty(svcId)) {
            log.info(StringUtil.changeForLog("get current svc info...."));
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, svcId);
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
            //64688
            //
            if(reloadDisciplineAllocationMap != null){
            }
            for(HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto:hcsaServiceStepSchemesByServiceId){
                switch (hcsaServiceStepSchemeDto.getStepCode()){
                    case HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS:
                        List<AppSvcCgoDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                        if(!IaisCommonUtils.isEmpty(appSvcCgoDtos)){
                            List<AppSvcCgoDto> reloadDto = IaisCommonUtils.genNewArrayList();
                            for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtos){
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
            /*if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
                reloadSvcDocMap.forEach((k,v)->{
                    if(v != null && v.size() > 1){
                        Collections.sort(v,(s1,s2)->s1.getSeqNum().compareTo(s2.getSeqNum()));
                    }
                });
            }*/
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
            String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
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
                    appSvcLaboratoryDisciplinesDto = new AppSvcLaboratoryDisciplinesDto();
                    appSvcLaboratoryDisciplinesDto.setPremiseType(premisesType);
                    appSvcLaboratoryDisciplinesDto.setPremiseVal(premisesValue);
                    appSvcLaboratoryDisciplinesDto.setPremiseGetAddress(appGrpPremisesDto.getAddress());
                    appSvcLaboratoryDisciplinesDto.setAppSvcChckListDtoList(appSvcChckListDtoList);
                    appSvcLaboratoryDisciplinesDtoList.add(appSvcLaboratoryDisciplinesDto);
                }
                i++;
            }
            String crud_action_type = ParamUtil.getRequestString(bpc.request, "nextStep");
            if ("next".equals(crud_action_type)) {
                errorMap = NewApplicationHelper.doValidateLaboratory(appGrpPremisesDtoList, appSvcLaboratoryDisciplinesDtoList, currentSvcId,hcsaSvcSubtypeOrSubsumedDtos);

                if (appSubmissionDto.isNeedEditController()) {
                    /*Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                    clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_LABORATORY);
                    appSubmissionDto.setClickEditPage(clickEditPages);*/
                    AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                    appEditSelectDto.setServiceEdit(true);
                    appSubmissionDto.setChangeSelectDto(appEditSelectDto);
                }
                ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
            }
            ParamUtil.setSessionAttr(bpc.request, "reloadLaboratoryDisciplines", (Serializable) reloadChkLstMap);

            currentSvcDto.setAppSvcLaboratoryDisciplinesDtoList(appSvcLaboratoryDisciplinesDtoList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcDto);
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
            Map<String, String> allChecked = isAllChecked(bpc, appSubmissionDto);
            if (errorMap.isEmpty() && allChecked.isEmpty()) {
                coMap.put("information", "information");
            } else {
                coMap.put("information", "");
            }
            bpc.request.getSession().setAttribute("coMap", coMap);
            if (!errorMap.isEmpty()) {
                //set audit
                bpc.request.setAttribute("errormapIs", "error");
                NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.LABORATORYDISCIPLINES);
                return;
            }
        }

        log.debug(StringUtil.changeForLog("the do doLaboratoryDisciplines end ...."));
    }

    private Map<String, String> isAllChecked(BaseProcessClass bpc, AppSubmissionDto appSubmissionDto) {
        StringBuilder sB = new StringBuilder();
        Map<String, List<HcsaSvcPersonnelDto>> svcAllPsnConfig = (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.SERVICEALLPSNCONFIGMAP);
        if (svcAllPsnConfig == null) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<String> svcIds = IaisCommonUtils.genNewArrayList();
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                svcIds.add(appSvcRelatedInfoDto.getServiceId());
            }
            List<HcsaServiceStepSchemeDto> svcStepConfigs = serviceConfigService.getHcsaServiceStepSchemesByServiceId(svcIds);
            svcAllPsnConfig = serviceConfigService.getAllSvcAllPsnConfig(svcStepConfigs, svcIds);
        }

        List<AppSvcRelatedInfoDto> dto = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        Map<String, String> map = new HashMap<>();
        ServiceStepDto serviceStepDto = new ServiceStepDto();
        for (int i = 0; i < dto.size(); i++) {
            String serviceId = dto.get(i).getServiceId();
            List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = serviceConfigService.getHcsaServiceStepSchemesByServiceId(serviceId);
            serviceStepDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);
            List<HcsaSvcPersonnelDto> currentSvcAllPsnConfig = serviceConfigService.getSvcAllPsnConfig(hcsaServiceStepSchemeDtos, serviceId);
            map = appSubmissionService.doCheckBox(bpc, sB, svcAllPsnConfig, currentSvcAllPsnConfig, dto.get(i),systemParamConfig.getUploadFileLimit(),systemParamConfig.getUploadFileType(),appSubmissionDto.getAppGrpPremisesDtoList());
        }

        if (!StringUtil.isEmpty(sB.toString())) {
            map.put("error", "error");
        }
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(map)+"---map----"));
        bpc.request.getSession().setAttribute("serviceConfig", sB.toString());
        return map;
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
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION, isEdit, isRfi);
        log.debug(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currentSvcRelatedDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        if (isGetDataFromPage) {
            List<AppSvcCgoDto> appSvcCgoDtoList = genAppSvcCgoDto(bpc.request);
            //do validate
            Map<String, String> errList = IaisCommonUtils.genNewHashMap();
            currentSvcRelatedDto.setAppSvcCgoDtoList(appSvcCgoDtoList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto);
            String crud_action_additional = bpc.request.getParameter("nextStep");
            String svcCode = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE);

            Map<String,AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.PERSONSELECTMAP);
            Map<String,AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.LICPERSONSELECTMAP);
            if ("next".equals(crud_action_additional)) {
                //List<AppSvcCgoDto> appSvcCgoList = (List<AppSvcCgoDto>) ParamUtil.getSessionAttr(bpc.request, GOVERNANCEOFFICERSDTOLIST);
                for(int i=0;i<appSvcCgoDtoList.size();i++ ){
                    AppSvcCgoDto appSvcCgoDto = appSvcCgoDtoList.get(i);
                    String profRegNo = appSvcCgoDto.getProfRegNo();
                    if(!StringUtil.isEmpty(profRegNo)){
                        List<String> prgNos = IaisCommonUtils.genNewArrayList();
                        prgNos.add(profRegNo);
                            if("Y".equals(prsFlag)){
                                ProfessionalParameterDto professionalParameterDto =new ProfessionalParameterDto();
                                professionalParameterDto.setRegNo(prgNos);
                                professionalParameterDto.setClientId("22222");
                                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmssSSS");
                                String format = simpleDateFormat.format(new Date());
                                professionalParameterDto.setTimestamp(format);
                                professionalParameterDto.setSignature("2222");
                                HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                                HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                                try {
                                    List<ProfessionalResponseDto> professionalResponseDtos = feEicGatewayClient.getProfessionalDetail(professionalParameterDto, signature.date(), signature.authorization(),
                                            signature2.date(), signature2.authorization()).getEntity();
                                    String name = professionalResponseDtos.get(0).getName();
                                    if(StringUtil.isEmpty(name)){
                                        errList.put("professionRegoNo"+i,"GENERAL_ERR0042");
                                    }
                                }catch (Throwable e){
                                    bpc.request.setAttribute("PRS_SERVICE_DOWN","PRS_SERVICE_DOWN");
                                }

                            }

                    }
                }
                List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = NewApplicationHelper.transferCgoToPsnDtoList(appSvcCgoDtoList);
                Map<String, String> map = NewApplicationHelper.doValidateGovernanceOfficers(appSvcCgoDtoList, licPersonMap, svcCode);
                //validate mandatory count
                int psnLength = 0;
                if(!IaisCommonUtils.isEmpty(appSvcCgoDtoList)){
                    psnLength = appSvcCgoDtoList.size();
                }
                List<HcsaSvcPersonnelDto> psnConfig = serviceConfigService.getGOSelectInfo(currentSvcId, ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
                if(!isRfi){
                    map = NewApplicationHelper.psnMandatoryValidate(psnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, map, psnLength, "psnMandatory", ApplicationConsts.PERSONNEL_PSN_TYPE_CLINICAL_GOVERNANCE_OFFICER);
                }
                errList.putAll(map);
                if (appSubmissionDto.isNeedEditController()) {
                    /*Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                    clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_GOVERNANCE_OFFICERS);
                    appSubmissionDto.setClickEditPage(clickEditPages);*/
                    AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                    appEditSelectDto.setServiceEdit(true);
                    appSubmissionDto.setChangeSelectDto(appEditSelectDto);
                }
                ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
                HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
                Map<String, String> allChecked = isAllChecked(bpc, appSubmissionDto);
                if (errList.isEmpty()) {
                    if (allChecked.isEmpty()) {
                        coMap.put("information", "information");
                    }else {
                        coMap.put("information", "");
                    }
                    //sync person dropdown and submisson dto
                    personMap = syncDropDownAndPsn(personMap,appSubmissionDto,appSvcCgoDtos,svcCode);
                } else {
                    //set audit
                    bpc.request.setAttribute("errormapIs", "error");
                    NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),errList,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.GOVERNANCEOFFICERS);
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errList));
                    coMap.put("information", "");
                    Map<String,AppSvcPersonAndExtDto> newPersonMap = removeDirtyDataFromPsnDropDown(appSubmissionDto,licPersonMap,personMap);
                    ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.PERSONSELECTMAP, (Serializable) newPersonMap);
                    return;
                }
                bpc.request.getSession().setAttribute("coMap", coMap);
            }else{
                //sync person dropdown and submisson dto
                List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = NewApplicationHelper.transferCgoToPsnDtoList(appSvcCgoDtoList);
                personMap = syncDropDownAndPsn(personMap,appSubmissionDto,appSvcCgoDtos,svcCode);
            }
            //remove dirty psn dropdown info
            Map<String,AppSvcPersonAndExtDto> newPersonMap = removeDirtyDataFromPsnDropDown(appSubmissionDto,licPersonMap,personMap);
            ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.PERSONSELECTMAP, (Serializable) newPersonMap);
            //remove dirty psn doc info
            List<HcsaSvcDocConfigDto> svcDocConfigDtos = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.SVC_DOC_CONFIG);
            List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = NewApplicationHelper.transferCgoToPsnDtoList(appSvcCgoDtoList);
            currentSvcRelatedDto = removeDirtyPsnDoc(currentSvcRelatedDto,svcDocConfigDtos,appSvcCgoDtos,ApplicationConsts.DUP_FOR_PERSON_CGO);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto);
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
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)
                    || RfcConst.RFC_BTN_OPTION_SKIP.equals(action)) {
                return;
            }
            AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "oldAppSubmissionDto");
            if(oldAppSubmissionDto!=null){
                List<AppSvcCgoDto> appSvcCgoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getAppSvcCgoDtoList();
                if(appSvcCgoDtoList!=null){
                    List<AppSvcCgoDto> appSvcCgoDtoList1 = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getAppSvcCgoDtoList();
                    if(!appSvcCgoDtoList.equals(appSvcCgoDtoList1)){
                        cgoChange=true;
                    }
                }
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
        Set<String> clickEditPage = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
        boolean svcScopeEdit = false;
        for (String item : clickEditPage) {
            if (NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_LABORATORY.equals(item)) {
                svcScopeEdit = true;
                break;
            }
        }

        if (isGetDataFromPage || svcScopeEdit || cgoChange) {
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
            List<HcsaSvcSubtypeOrSubsumedDto> svcScopeDtoList = (List<HcsaSvcSubtypeOrSubsumedDto>) ParamUtil.getSessionAttr(bpc.request, "HcsaSvcSubtypeOrSubsumedDto");
            if(svcScopeDtoList == null){
                svcScopeDtoList = serviceConfigService.loadLaboratoryDisciplines(currentSvcId);
            }
            Map<String, HcsaSvcSubtypeOrSubsumedDto> svcScopeAlignMap = IaisCommonUtils.genNewHashMap();
            NewApplicationHelper.recursingSvcScope(svcScopeDtoList,svcScopeAlignMap);
            AppSvcRelatedInfoDto currentSvcRelatedDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
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
                        String[] chkAndCgoValue = ParamUtil.getStrings(bpc.request, chkAndCgoName.toString());
                        if (chkAndCgoValue != null && chkAndCgoValue.length > 1) {
                            AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto = new AppSvcDisciplineAllocationDto();
                            String svcScopeConfigId = chkAndCgoValue[0];
                            if(!StringUtil.isEmpty(svcScopeConfigId)){
                                HcsaSvcSubtypeOrSubsumedDto svcScopeConfigDto = svcScopeAlignMap.get(svcScopeConfigId);
                                /*if(targetChkDto != null && svcScopeConfigDto != null && ClinicalLaboratoryDelegator.PLEASEINDICATE.equals(svcScopeConfigDto.getName())){
                                    pleaseIndicateLabId = svcScopeConfigDto.getId();
                                    continue;
                                }*/
                                appSvcDisciplineAllocationDto.setPremiseVal(premisesValue);
                                appSvcDisciplineAllocationDto.setChkLstConfId(svcScopeConfigId);//NOSONAR
                                appSvcDisciplineAllocationDto.setIdNo(chkAndCgoValue[1]);//NOSONAR

                                daList.add(appSvcDisciplineAllocationDto);
                                if(targetChkDto != null && NewApplicationConstant.SERVICE_SCOPE_LAB_OTHERS.equals(svcScopeConfigDto.getName())){
                                    targetAllocationDto = (AppSvcDisciplineAllocationDto) CopyUtil.copyMutableObject(appSvcDisciplineAllocationDto);//NOSONAR
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

            String crud_action_additional = ParamUtil.getRequestString(bpc.request, "nextStep");
            if ("next".equals(crud_action_additional)) {

                doValidateDisciplineAllocation(errorMap, daList, bpc.request);
                if (appSubmissionDto.isNeedEditController()) {
                    Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                    //clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_DISCIPLINE_ALLOCATION);
                    appSubmissionDto.setClickEditPage(clickEditPages);
                    AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                    appEditSelectDto.setServiceEdit(true);
                    appSubmissionDto.setChangeSelectDto(appEditSelectDto);
                }
                ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);

            }
            currentSvcRelatedDto.setAppSvcDisciplineAllocationDtoList(daList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto);
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
            Map<String, String> allChecked = isAllChecked(bpc, appSubmissionDto);
            if (errorMap.isEmpty() && allChecked.isEmpty()) {
                coMap.put("information", "information");
            } else {
                coMap.put("information", "");
            }
            bpc.request.getSession().setAttribute("coMap", coMap);
            if (!errorMap.isEmpty()) {
                //set audit
                bpc.request.setAttribute("errormapIs", "error");
                NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.DISCIPLINEALLOCATION);
                return;
            }
            //save into sub-svc dto
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

        if (isGetDataFromPagePo || isGetDataFromPageDpo) {
            List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = genAppSvcPrincipalOfficersDto(bpc.request, isGetDataFromPagePo, isGetDataFromPageDpo);
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

            ParamUtil.setSessionAttr(bpc.request, "AppSvcPrincipalOfficersDto", (Serializable) appSvcPrincipalOfficersDtoList);
            Map<String, String> map = IaisCommonUtils.genNewHashMap();
            String crud_action_additional = ParamUtil.getRequestString(bpc.request, "nextStep");
            String deputyPoFlag = ParamUtil.getString(bpc.request, "deputyPrincipalOfficer");

            if (!StringUtil.isEmpty(deputyPoFlag)) {
                appSvcRelatedInfoDto.setDeputyPoFlag(deputyPoFlag);
            }
            appSvcRelatedInfoDto.setAppSvcPrincipalOfficersDtoList(appSvcPrincipalOfficersDtoList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, appSvcRelatedInfoDto);
            String svcCode = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE);
            Map<String,AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.PERSONSELECTMAP);
            Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.LICPERSONSELECTMAP);
            if ("next".equals(crud_action_additional)) {
                List<AppSvcPrincipalOfficersDto> poDto = (List<AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(bpc.request, "AppSvcPrincipalOfficersDto");
                map = NewApplicationHelper.doValidatePo(poDto, licPersonMap,svcCode);
                //validate mandatory count
                int poLength = 0;
                int dpoLength = 0;
                if(!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtoList)){
                    for(AppSvcPrincipalOfficersDto psnDto:appSvcPrincipalOfficersDtoList){
                        String psnType = psnDto.getPsnType();
                        if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)){
                            poLength++;
                        }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnType)){
                            dpoLength++;
                        }
                    }
                }
                List<HcsaSvcPersonnelDto> poPsnConfig = serviceConfigService.getGOSelectInfo(currentSvcId, ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
                List<HcsaSvcPersonnelDto> dpoPsnConfig = serviceConfigService.getGOSelectInfo(currentSvcId, ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
                if(!isRfi){
                    map = NewApplicationHelper.psnMandatoryValidate(poPsnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_PO, map, poLength, "poPsnMandatory", ApplicationConsts.PERSONNEL_PSN_TYPE_PRINCIPAL_OFFICER);
                    if(dpoLength > 0){
                        map = NewApplicationHelper.psnMandatoryValidate(dpoPsnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, map, dpoLength, "dpoPsnMandatory", ApplicationConsts.PERSONNEL_PSN_TYPE_DEPUTY_PRINCIPAL_OFFICER);
                    }
                }
                if (appSubmissionDto.isNeedEditController()) {
//                    Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
//                    if (isGetDataFromPagePo) {
//                        clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_PRINCIPAL_OFFICERS);
//                    }
//                    if (isGetDataFromPageDpo) {
//                        clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_DEPUTY_PRINCIPAL_OFFICERS);
//                    }
//                    appSubmissionDto.setClickEditPage(clickEditPages);
                    AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                    appEditSelectDto.setServiceEdit(true);
                    appSubmissionDto.setChangeSelectDto(appEditSelectDto);
                }
                ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
                HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
                Map<String, String> allChecked = isAllChecked(bpc, appSubmissionDto);
                if (map.isEmpty()) {
                    if (allChecked.isEmpty()) {
                        coMap.put("information", "information");
                    }else {
                        coMap.put("information", "");
                    }
                    //sync person dropdown and submisson dto
                    personMap = syncDropDownAndPsn(personMap,appSubmissionDto,appSvcPrincipalOfficersDtoList,svcCode);
                } else {
                    coMap.put("information", "");
                }
                bpc.request.getSession().setAttribute("coMap", coMap);
            }else{
                //sync person dropdown and submisson dto
                personMap = syncDropDownAndPsn(personMap,appSubmissionDto,appSvcPrincipalOfficersDtoList,svcCode);
            }
            //remove dirty psn dropdown info
            Map<String,AppSvcPersonAndExtDto> newPersonMap = removeDirtyDataFromPsnDropDown(appSubmissionDto,licPersonMap,personMap);
            ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.PERSONSELECTMAP, (Serializable) newPersonMap);
            //remove dirty psn doc info

            List<AppSvcPrincipalOfficersDto> principalOfficersDtos = IaisCommonUtils.genNewArrayList();
            List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
            NewApplicationHelper.assignPoDpoDto(appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList(),principalOfficersDtos,deputyPrincipalOfficersDtos);
            List<HcsaSvcDocConfigDto> svcDocConfigDtos = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.SVC_DOC_CONFIG);
            //po
            appSvcRelatedInfoDto = removeDirtyPsnDoc(appSvcRelatedInfoDto,svcDocConfigDtos,principalOfficersDtos,ApplicationConsts.DUP_FOR_PERSON_PO);
            //dpo
            appSvcRelatedInfoDto = removeDirtyPsnDoc(appSvcRelatedInfoDto,svcDocConfigDtos,deputyPrincipalOfficersDtos,ApplicationConsts.DUP_FOR_PERSON_DPO);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, appSvcRelatedInfoDto);
            if (!map.isEmpty()) {
                //set audit
                bpc.request.setAttribute("errormapIs", "error");
                NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),map,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.PRINCIPALOFFICERS);
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(map));
                return;
            }
        }

        log.debug(StringUtil.changeForLog("the do doPrincipalOfficers end ...."));
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
            AppSubmissionDto oldSubmissionDto = NewApplicationHelper.getOldSubmissionDto(bpc.request,appSubmissionDto.getAppType());
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
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                appEditSelectDto.setServiceEdit(true);
                appSubmissionDto.setChangeSelectDto(appEditSelectDto);
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

            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
            Map<String, String> allChecked = isAllChecked(bpc, appSubmissionDto);
            if (errorMap.isEmpty() && allChecked.isEmpty()) {
                coMap.put("information", "information");
            } else {
                coMap.put("information", "");
            }
            bpc.request.getSession().setAttribute("coMap", coMap);
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
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        List<AppSvcPersonnelDto> appSvcPersonnelDtos = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
        if (appSvcPersonnelDtos != null && !appSvcPersonnelDtos.isEmpty()) {
            if (appSvcPersonnelDtos.size() > mandatory) {
                mandatory = appSvcPersonnelDtos.size();
            }
            ParamUtil.setRequestAttr(bpc.request, "AppSvcPersonnelDtoList", appSvcPersonnelDtos);
        }
        ParamUtil.setRequestAttr(bpc.request, "ServicePersonnelMandatory", mandatory);
        List<SelectOption> personnelTypeSel = genPersonnelTypeSel(currentSvcCode);
        ParamUtil.setRequestAttr(bpc.request, SERVICEPERSONNELTYPE, personnelTypeSel);

        List<SelectOption> designation = genPersonnelDesignSel(currentSvcCode);
        ParamUtil.setSessionAttr(bpc.request, "NuclearMedicineImagingDesignation", (Serializable) designation);

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
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.REQUESTINFORMATIONCONFIG);
        boolean isRfi = false;
        if (requestInformationConfig != null) {
            isRfi = true;
        }
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION, isEdit, isRfi);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (isGetDataFromPage) {
            String currentSvcCod = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE);
            List<AppSvcPersonnelDto> appSvcPersonnelDtos = IaisCommonUtils.genNewArrayList();
            List<String> personnelTypeList = IaisCommonUtils.genNewArrayList();
            List<SelectOption> personnelTypeSel = genPersonnelTypeSel(currentSvcCod);
            for (SelectOption sp : personnelTypeSel) {
                personnelTypeList.add(sp.getValue());
            }
            appSvcPersonnelDtos = genAppSvcPersonnelDtoList(bpc.request, personnelTypeList, currentSvcCod);
            appSvcRelatedInfoDto.setAppSvcPersonnelDtoList(appSvcPersonnelDtos);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, appSvcRelatedInfoDto);
            String nextStep = ParamUtil.getRequestString(bpc.request, "nextStep");
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

                for (int i = 0; i < appSvcPersonnelDtos.size(); i++) {
                    AppSvcPersonnelDto appSvcPersonnelDto = appSvcPersonnelDtos.get(i);
                    String profRegNo = appSvcPersonnelDto.getProfRegNo();
                    if(!StringUtil.isEmpty(profRegNo)){
                        List<String> prgNos = IaisCommonUtils.genNewArrayList();
                        prgNos.add(profRegNo);

                            if("Y".equals(prsFlag)){
                                ProfessionalParameterDto professionalParameterDto =new ProfessionalParameterDto();
                                professionalParameterDto.setRegNo(prgNos);
                                professionalParameterDto.setClientId("22222");
                                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmssSSS");
                                String format = simpleDateFormat.format(new Date());
                                professionalParameterDto.setTimestamp(format);
                                professionalParameterDto.setSignature("2222");
                                HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                                HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                                try {
                                    List<ProfessionalResponseDto> professionalResponseDtos = feEicGatewayClient.getProfessionalDetail(professionalParameterDto, signature.date(), signature.authorization(),
                                            signature2.date(), signature2.authorization()).getEntity();
                                    String name = professionalResponseDtos.get(0).getName();
                                    if(StringUtil.isEmpty(name)){
                                        errorMap.put("regnNo" + i,"Professional Regn. No. is not correct.");
//                            appSvcCgoDto.setSubSpeciality(null);
//                            appSvcCgoDto.setSpeciality(null);
                                    }else {
                                        appSvcPersonnelDto.setName(name);
                                    }
                                }catch (Throwable e){
                                    bpc.request.setAttribute("PRS_SERVICE_DOWN","PRS_SERVICE_DOWN");
                                }

                            }


                    }
                }
                if (appSubmissionDto.isNeedEditController()) {
                    Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                    //clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_SERVICE_PERSONNEL);
                    appSubmissionDto.setClickEditPage(clickEditPages);
                }
                ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
            }
            //
            //remove dirty psn doc info
            List<HcsaSvcDocConfigDto> svcDocConfigDtos = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.SVC_DOC_CONFIG);
            List<AppSvcPrincipalOfficersDto> spList = IaisCommonUtils.genNewArrayList();
            List<AppSvcPersonnelDto> appSvcPersonnelDtosList = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcPersonnelDtosList)){
                for(AppSvcPersonnelDto appSvcPersonnelDto:appSvcPersonnelDtosList){
                    AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
                    String psnIndexNo = appSvcPersonnelDto.getCgoIndexNo();
                    if(!StringUtil.isEmpty(psnIndexNo)){
                        appSvcPrincipalOfficersDto.setCgoIndexNo(psnIndexNo);
                        spList.add(appSvcPrincipalOfficersDto);
                    }
                }
            }
            appSvcRelatedInfoDto = removeDirtyPsnDoc(appSvcRelatedInfoDto,svcDocConfigDtos,spList,ApplicationConsts.DUP_FOR_PERSON_SVCPSN);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, appSvcRelatedInfoDto);

            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
            Map<String, String> allChecked = isAllChecked(bpc, appSubmissionDto);
            if (errorMap.isEmpty() && allChecked.isEmpty()) {
                coMap.put("information", "information");
            } else {
                coMap.put("information", "");
            }
            bpc.request.getSession().setAttribute("coMap", coMap);
            if (!errorMap.isEmpty()) {
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
                if(!isRfi){
                    errorMap = NewApplicationHelper.psnMandatoryValidate(psnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL, errorMap, psnLength, "psnMandatory", ApplicationConsts.PERSONNEL_PSN_TYPE_SVC);
                }
                if(!errorMap.isEmpty()){
                    NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
                    ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.NUCLEARMEDICINEIMAGING);
                }
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
        log.info(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        if (isGetDataFromPage) {
            String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
            AppSvcRelatedInfoDto currentSvcRelatedDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
            List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = genAppSvcMedAlertPerson(bpc.request);
            currentSvcRelatedDto.setAppSvcMedAlertPersonList(appSvcMedAlertPersonList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto);
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
            String nextStep = ParamUtil.getRequestString(bpc.request, "nextStep");
            String svcCode = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE);
            Map<String,AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.PERSONSELECTMAP);
            Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.LICPERSONSELECTMAP);
            if ("next".equals(nextStep)) {
                if (appSubmissionDto.isNeedEditController()) {
//                    Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
//                    clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_MEDALERT_PERSON);
//                    appSubmissionDto.setClickEditPage(clickEditPages);
                    AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                    appEditSelectDto.setServiceEdit(true);
                    appSubmissionDto.setChangeSelectDto(appEditSelectDto);
                }
                Map<String, String> errorMap = NewApplicationHelper.doValidateMedAlertPsn(appSvcMedAlertPersonList, licPersonMap, svcCode);
                //validate mandatory count
                int psnLength = 0;
                if(!IaisCommonUtils.isEmpty(appSvcMedAlertPersonList)){
                    psnLength = appSvcMedAlertPersonList.size();
                }
                List<HcsaSvcPersonnelDto> psnConfig = serviceConfigService.getGOSelectInfo(currentSvcId, ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
                if(!isRfi){
                    errorMap = NewApplicationHelper.psnMandatoryValidate(psnConfig, ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, errorMap, psnLength, "psnMandatory", ApplicationConsts.PERSONNEL_PSN_TYPE_MEDALERT);
                }

                HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
                Map<String, String> allChecked = isAllChecked(bpc, appSubmissionDto);
                if (errorMap.isEmpty() && allChecked.isEmpty()) {
                    coMap.put("information", "information");
                } else {
                    coMap.put("information", "");
                }
                bpc.request.getSession().setAttribute("coMap",coMap);
                if (!errorMap.isEmpty()) {
                    //set audit
                    bpc.request.setAttribute("errormapIs", "error");
                    NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
                    ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.MEDALERT_PERSON);
                    //todo change(medAlert page is the final svc page,will jump to preview page)
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,AppServicesConsts.NAVTABS_SERVICEFORMS);
                    Map<String,AppSvcPersonAndExtDto> newPersonMap = removeDirtyDataFromPsnDropDown(appSubmissionDto,licPersonMap,personMap);
                    ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.PERSONSELECTMAP, (Serializable) newPersonMap);
                    return;
                } else {
                    //sync person dropdown and submisson dto
                    personMap = syncDropDownAndPsn(personMap,appSubmissionDto,appSvcMedAlertPersonList,svcCode);
                }
            }else{
                //sync person dropdown and submisson dto
                personMap = syncDropDownAndPsn(personMap,appSubmissionDto,appSvcMedAlertPersonList,svcCode);
            }
            //remove dirty psn dropdown info
            Map<String,AppSvcPersonAndExtDto> newPersonMap = removeDirtyDataFromPsnDropDown(appSubmissionDto,licPersonMap,personMap);
            ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.PERSONSELECTMAP, (Serializable) newPersonMap);
            //remove dirty psn doc info
            List<HcsaSvcDocConfigDto> svcDocConfigDtos = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.SVC_DOC_CONFIG);
            currentSvcRelatedDto = removeDirtyPsnDoc(currentSvcRelatedDto,svcDocConfigDtos,appSvcMedAlertPersonList,ApplicationConsts.DUP_FOR_PERSON_MAP);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto);

            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
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
        //retrieve date from page
        List<AppSvcVehicleDto> appSvcVehicleDtos = genAppSvcVehicleDto(bpc.request);
        currSvcInfoDto.setAppSvcVehicleDtoList(appSvcVehicleDtos);
        setAppSvcRelatedInfoMap(bpc.request, currSvcId, currSvcInfoDto);
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
        //vehicle config
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList = serviceConfigService.getGOSelectInfo(currSvcId, ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR);
        if (hcsaSvcPersonnelList != null && hcsaSvcPersonnelList.size() > 0) {
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = hcsaSvcPersonnelList.get(0);
            ParamUtil.setRequestAttr(bpc.request, CLINICALDIRECTORCONFIG, hcsaSvcPersonnelDto);
        }
        List<AppSvcClinicalDirectorDto> appSvcClinicalDirectorDtos = currSvcInfoDto.getAppSvcClinicalDirectorDtoList();
        ParamUtil.setRequestAttr(bpc.request,CLINICALDIRECTORDTOLIST,appSvcClinicalDirectorDtos);
        List<SelectOption> easMtsSpecialtySelectList = NewApplicationHelper.genEasMtsSpecialtySelectList(currSvcCode);
        ParamUtil.setRequestAttr(bpc.request,EASMTSSPECIALTYSELECTLIST,easMtsSpecialtySelectList);
        List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
        List<SelectOption> easMtsDesignationSelectList = NewApplicationHelper.genEasMtsDesignationSelectList(hcsaServiceDtos);
        ParamUtil.setRequestAttr(bpc.request,EASMTSDESIGNATIONSELECTLIST,easMtsDesignationSelectList);


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

        String currSvcId = (String) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = getAppSvcRelatedInfo(bpc.request,currSvcId);
        //retrieve date from page
        List<AppSvcClinicalDirectorDto> appSvcClinicalDirectorDtos = genAppSvcClinicalDirectorDto(bpc.request);
        currSvcInfoDto.setAppSvcClinicalDirectorDtoList(appSvcClinicalDirectorDtos);
        setAppSvcRelatedInfoMap(bpc.request, currSvcId, currSvcInfoDto);

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
        log.debug(StringUtil.changeForLog("doCharges end ..."));
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

        String errLengthName = NewApplicationHelper.repLength("Name","66");
        String errLengthRegnNo = NewApplicationHelper.repLength("Professional Regn. No.","20");
        String errLengthWrkExpYear = NewApplicationHelper.repLength("Relevant working experience (Years)","2");
        String errLengthQualification = NewApplicationHelper.repLength("Qualification","100");
        for (int i = 0; i < appSvcPersonnelDtos.size(); i++) {
            if (AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode)) {
                String designation = appSvcPersonnelDtos.get(i).getDesignation();
                if (StringUtil.isEmpty(designation)) {
                    errorMap.put("designation" + i, errDesignation);
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


    private List<AppSvcCgoDto> genAppSvcCgoDto(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("genAppSvcCgoDto start ...."));
        ParamUtil.setSessionAttr(request, ERRORMAP_GOVERNANCEOFFICERS, null);
        List<AppSvcCgoDto> appSvcCgoDtoList = IaisCommonUtils.genNewArrayList();
        AppSvcCgoDto appSvcCgoDto;
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
        //cgoIndexNo
        String[] cgoIndexNos = ParamUtil.getStrings(request, "cgoIndexNo");
        boolean rfcOrRenew = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType);
        boolean isRfi = NewApplicationHelper.checkIsRfi(request);
        boolean needEdit = rfcOrRenew || isRfi;
        if (needEdit) {
            if(cgoIndexNos == null){
                size = 0;
            }else{
                size = cgoIndexNos.length;
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
        String[] professionType = ParamUtil.getStrings(request, "professionType");
        String[] professionRegoNo = ParamUtil.getStrings(request, "professionRegoNo");
        String[] specialty = ParamUtil.getStrings(request, "specialty");
        String[] specialtyOther = ParamUtil.getStrings(request, "specialtyOther");
        String[] qualification = ParamUtil.getStrings(request, "qualification");
        String[] mobileNo = ParamUtil.getStrings(request, "mobileNo");
        String[] emailAddress = ParamUtil.getStrings(request, "emailAddress");
        //new and not rfi
        for (int i = 0; i < size; i++) {
            AppPsnEditDto appPsnEditDto = new AppPsnEditDto();
            boolean chooseExisting = false;
            boolean getPageData = false;
            appSvcCgoDto = new AppSvcCgoDto();
            String cgoIndexNo = cgoIndexNos[i];
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
                    if (!StringUtil.isEmpty(cgoIndexNo)) {
                        //not click edit
                        if (AppConsts.NO.equals(isPartEdit[i])) {
                            appSvcCgoDto = getAppSvcCgoByIndexNo(appSvcRelatedInfoDto, cgoIndexNo);
                            appSvcCgoDtoList.add(appSvcCgoDto);
                            //change arr
                            cgoIndexNos = removeArrIndex(cgoIndexNos, i);
                            isPartEdit = removeArrIndex(isPartEdit, i);
                            licPerson = removeArrIndex(licPerson, i);
                            //dropdown cannot disabled
                            assignSelect = removeArrIndex(assignSelect, i);
                            salutation = removeArrIndex(salutation, i);
                            idType = removeArrIndex(idType, i);
                            designation = removeArrIndex(designation, i);
                            professionType = removeArrIndex(professionType, i);
                            specialty = removeArrIndex(specialty, i);
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
                if(appPsnEditDto.isProfessionType()){
                    NewApplicationHelper.setPsnValue(professionType,i,appSvcPrincipalOfficersDto,"professionType");
                }
                if(appPsnEditDto.isSpeciality()){
                    NewApplicationHelper.setPsnValue(specialty,i,appSvcPrincipalOfficersDto,"speciality");
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
                if(appPsnEditDto.isSpecialityOther() && "other".equals(appSvcPrincipalOfficersDto.getSpeciality())){
                    specialtyOther = NewApplicationHelper.setPsnValue(specialtyOther,i,appSvcPrincipalOfficersDto,"specialityOther");
                }else{
                    specialtyOther = removeArrIndex(specialtyOther,i);
                }
                if(appPsnEditDto.isSubSpeciality()){
                    qualification = NewApplicationHelper.setPsnValue(qualification,i,appSvcPrincipalOfficersDto,"subSpeciality");
                }
                if(appPsnEditDto.isEmailAddr()){
                    emailAddress = NewApplicationHelper.setPsnValue(emailAddress,i,appSvcPrincipalOfficersDto,"emailAddr");
                }
                appSvcCgoDto = MiscUtil.transferEntityDto(appSvcPrincipalOfficersDto, AppSvcCgoDto.class);
                appSvcCgoDto.setAssignSelect(assignSelect[i]);
                appSvcCgoDto.setLicPerson(true);
                appSvcCgoDto.setSelectDropDown(true);
                if(!StringUtil.isEmpty(cgoIndexNo)){
                    appSvcCgoDto.setCgoIndexNo(cgoIndexNo);
                }else if (StringUtil.isEmpty(appSvcCgoDto.getCgoIndexNo())) {
                    appSvcCgoDto.setCgoIndexNo(UUID.randomUUID().toString());
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

                appSvcCgoDtoList.add(appSvcCgoDto);
                //change arr index
                cgoIndexNos = removeArrIndex(cgoIndexNos, i);
                isPartEdit = removeArrIndex(isPartEdit, i);
                licPerson = removeArrIndex(licPerson, i);
                existingPsn = removeArrIndex(existingPsn, i);
                //dropdown cannot disabled
                assignSelect = removeArrIndex(assignSelect, i);
                salutation = removeArrIndex(salutation, i);
                idType = removeArrIndex(idType, i);
                designation = removeArrIndex(designation, i);
                professionType = removeArrIndex(professionType, i);
                specialty = removeArrIndex(specialty, i);
                --i;
                --size;
            }else if(getPageData){
                if (StringUtil.isEmpty(cgoIndexNo)) {
                    appSvcCgoDto.setCgoIndexNo(UUID.randomUUID().toString());
                } else {
                    appSvcCgoDto.setCgoIndexNo(cgoIndexNos[i]);
                }
                appSvcCgoDto.setAssignSelect(assignSelect[i]);
                appSvcCgoDto.setSalutation(salutation[i]);
                appSvcCgoDto.setName(name[i]);
                appSvcCgoDto.setIdType(idType[i]);
                appSvcCgoDto.setIdNo(StringUtil.toUpperCase(idNo[i]));
                appSvcCgoDto.setDesignation(designation[i]);
                appSvcCgoDto.setProfessionType(professionType[i]);
                appSvcCgoDto.setProfRegNo(professionRegoNo[i]);
                String specialtyStr = specialty[i];
                appSvcCgoDto.setSpeciality(specialtyStr);
                if ("other".equals(specialtyStr)) {
                    appSvcCgoDto.setSpecialityOther(specialtyOther[i]);
                }
                //qualification(before)
                appSvcCgoDto.setSubSpeciality(qualification[i]);
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
                appSvcCgoDtoList.add(appSvcCgoDto);
            }
        }
        ParamUtil.setSessionAttr(request, GOVERNANCEOFFICERSDTOLIST, (Serializable) appSvcCgoDtoList);
        log.info(StringUtil.changeForLog("genAppSvcCgoDto end ...."));
        return appSvcCgoDtoList;
    }

    private List<AppSvcVehicleDto> genAppSvcVehicleDto(HttpServletRequest request){
        List<AppSvcVehicleDto> appSvcVehicleDtos = IaisCommonUtils.genNewArrayList();
        int vehicleLength = ParamUtil.getInt(request,"vehiclesLength");
        for(int i = 0; i < vehicleLength ; i++){
            String vehicleName = ParamUtil.getString(request,"vehicleName"+i);
            String chassisNum = ParamUtil.getString(request,"chassisNum"+i);
            String engineNum = ParamUtil.getString(request,"engineNum"+i);
            AppSvcVehicleDto appSvcVehicleDto = new AppSvcVehicleDto();
            appSvcVehicleDto.setVehicleName(vehicleName);
            appSvcVehicleDto.setChassisNum(chassisNum);
            appSvcVehicleDto.setEngineNum(engineNum);
            appSvcVehicleDtos.add(appSvcVehicleDto);
        }
        return appSvcVehicleDtos;
    }

    private List<AppSvcClinicalDirectorDto> genAppSvcClinicalDirectorDto(HttpServletRequest request){
        String currSvcCode = (String) ParamUtil.getSessionAttr(request,NewApplicationDelegator.CURRENTSVCCODE);
        List<AppSvcClinicalDirectorDto> appSvcClinicalDirectorDtos = IaisCommonUtils.genNewArrayList();
        int cdLength = ParamUtil.getInt(request,"cdLength");
        for(int i = 0; i < cdLength ; i++){
            String professionBoard = ParamUtil.getString(request,"professionBoard"+i);
            String profRegNo = ParamUtil.getString(request,"profRegNo"+i);
            String name = ParamUtil.getString(request,"name"+i);
            String salutation = ParamUtil.getString(request,"salutation"+i);
            String idType = ParamUtil.getString(request,"idType"+i);
            String idNo = ParamUtil.getString(request,"idNo"+i);
            String designation = ParamUtil.getString(request,"designation"+i);
            String specialty = ParamUtil.getString(request,"specialty"+i);
            String otherSpecialty = ParamUtil.getString(request,"otherSpecialty"+i);
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

            AppSvcClinicalDirectorDto appSvcClinicalDirectorDto = new AppSvcClinicalDirectorDto();
            appSvcClinicalDirectorDto.setProfessionBoard(professionBoard);
            appSvcClinicalDirectorDto.setProfRegNo(profRegNo);
            appSvcClinicalDirectorDto.setName(name);
            appSvcClinicalDirectorDto.setSalutation(salutation);
            appSvcClinicalDirectorDto.setIdType(idType);
            appSvcClinicalDirectorDto.setIdNo(idNo);
            appSvcClinicalDirectorDto.setDesignation(designation);
            appSvcClinicalDirectorDto.setSpecialty(specialty);
            if(ApplicationConsts.EAS_MTS_SPECIALTY_OTHERS.equals(specialty)){
                appSvcClinicalDirectorDto.setOtherSpecialty(otherSpecialty);
            }else{
                appSvcClinicalDirectorDto.setOtherSpecialty(null);
            }
            appSvcClinicalDirectorDto.setTypeOfRegister(typeOfRegister);
            appSvcClinicalDirectorDto.setHoldCerByEMS(holdCerByEMS);
            appSvcClinicalDirectorDto.setMobileNo(mobileNo);
            appSvcClinicalDirectorDto.setEmailAddr(emailAddr);
            appSvcClinicalDirectorDto.setTypeOfCurrRegi(typeOfCurrRegi);
            appSvcClinicalDirectorDto.setRelevantExperience(relevantExperience);


            //date pick
            appSvcClinicalDirectorDto.setSpecialtyGetDateStr(specialtyGetDateStr);
            appSvcClinicalDirectorDto.setPraCerEndDateStr(praCerEndDateStr);
            appSvcClinicalDirectorDto.setCurrRegiDateStr(currRegiDateStr);
            appSvcClinicalDirectorDto.setAclsExpiryDateStr(aclsExpiryDateStr);
            appSvcClinicalDirectorDto.setBclsExpiryDateStr(bclsExpiryDateStr);

            Date specialtyGetDate = DateUtil.parseDate(specialtyGetDateStr, Formatter.DATE);
            Date praCerEndDate = DateUtil.parseDate(praCerEndDateStr, Formatter.DATE);
            Date currRegiDate = DateUtil.parseDate(currRegiDateStr, Formatter.DATE);
            Date aclsExpiryDate = DateUtil.parseDate(aclsExpiryDateStr, Formatter.DATE);
            Date bclsExpiryDate = DateUtil.parseDate(bclsExpiryDateStr, Formatter.DATE);
            appSvcClinicalDirectorDto.setSpecialtyGetDate(specialtyGetDate);
            appSvcClinicalDirectorDto.setPraCerEndDate(praCerEndDate);
            appSvcClinicalDirectorDto.setCurrRegiDate(currRegiDate);
            appSvcClinicalDirectorDto.setAclsExpiryDate(aclsExpiryDate);
            appSvcClinicalDirectorDto.setBclsExpiryDate(bclsExpiryDate);

            if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(currSvcCode)){
                if(AppConsts.YES.equals(noRegWithProfBoard)){
                    appSvcClinicalDirectorDto.setNoRegWithProfBoard(noRegWithProfBoard);
                }else{
                    appSvcClinicalDirectorDto.setNoRegWithProfBoard(null);
                }
                appSvcClinicalDirectorDto.setTransportYear(transportYear);
            }
            appSvcClinicalDirectorDtos.add(appSvcClinicalDirectorDto);
        }

        return appSvcClinicalDirectorDtos;
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
                    if(appPsnEditDto.isOfficeTelNo()){
                        officeTelNo = NewApplicationHelper.setPsnValue(officeTelNo,i,appSvcPrincipalOfficersDto,"officeTelNo");
                    }
                    if(appPsnEditDto.isEmailAddr()){
                        emailAddress = NewApplicationHelper.setPsnValue(emailAddress,i,appSvcPrincipalOfficersDto,"emailAddr");
                    }
                    String poIndexNo = poIndexNos[i];
                    if(!StringUtil.isEmpty(poIndexNo)){
                        appSvcPrincipalOfficersDto.setCgoIndexNo(poIndexNo);
                    }
                    if(StringUtil.isEmpty(appSvcPrincipalOfficersDto.getCgoIndexNo())){
                        appSvcPrincipalOfficersDto.setCgoIndexNo(UUID.randomUUID().toString());
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
                        appSvcPrincipalOfficersDto.setCgoIndexNo(UUID.randomUUID().toString());
                    } else {
                        appSvcPrincipalOfficersDto.setCgoIndexNo(poIndexNo);
                    }
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                    appSvcPrincipalOfficersDto.setSalutation(salutation[i]);
                    appSvcPrincipalOfficersDto.setName(name[i]);
                    appSvcPrincipalOfficersDto.setIdType(idType[i]);
                    appSvcPrincipalOfficersDto.setIdNo(StringUtil.toUpperCase(idNo[i]));
                    appSvcPrincipalOfficersDto.setDesignation(designation[i]);
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
                        appSvcPrincipalOfficersDto.setCgoIndexNo(UUID.randomUUID().toString());
                    } else {
                        appSvcPrincipalOfficersDto.setCgoIndexNo(dpoIndexNo);
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
                        appSvcPrincipalOfficersDto.setCgoIndexNo(UUID.randomUUID().toString());
                    } else {
                        appSvcPrincipalOfficersDto.setCgoIndexNo(dpoIndexNo);
                    }
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                    appSvcPrincipalOfficersDto.setSalutation(deputySalutation[i]);
                    appSvcPrincipalOfficersDto.setName(deputyName[i]);
                    appSvcPrincipalOfficersDto.setIdType(deputyIdType[i]);
                    appSvcPrincipalOfficersDto.setIdNo(StringUtil.toUpperCase(deputyIdNo[i]));
                    appSvcPrincipalOfficersDto.setDesignation(deputyDesignation[i]);
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

    private void doValidateDisciplineAllocation(Map<String, String> map, List<AppSvcDisciplineAllocationDto> daList, HttpServletRequest request) {
        Map<String, String> cgoMap = new HashMap<>();
        for (int i = 0; i < daList.size(); i++) {
            String idNo = daList.get(i).getIdNo();
            if (StringUtil.isEmpty(idNo)) {
                map.put("disciplineAllocation" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","Clinical Governance Officers","field"));
            } else {
                cgoMap.put(idNo, idNo);
            }
        }
        if(map.isEmpty()){
            List<AppSvcCgoDto> appSvcCgoList = (List<AppSvcCgoDto>) ParamUtil.getSessionAttr(request, GOVERNANCEOFFICERSDTOLIST);
            List<AppSvcCgoDto> appSvcCgoDtos = IaisCommonUtils.genNewArrayList();
            if (appSvcCgoList != null) {
                if (daList.size() < appSvcCgoList.size()) {
                    return;
                }
                if (appSvcCgoList.size() <= daList.size()) {
                    cgoMap.forEach((k, v) -> {
                        for (AppSvcCgoDto appSvcCgoDto : appSvcCgoList) {
                            String idNo = appSvcCgoDto.getIdNo();
                            if (k.equals(idNo)) {
                                appSvcCgoDtos.add(appSvcCgoDto);
                            }
                        }
                    });
                }
                appSvcCgoList.removeAll(appSvcCgoDtos);
                StringBuilder stringBuilder = new StringBuilder();
                for (AppSvcCgoDto appSvcCgoDto : appSvcCgoList) {
                    stringBuilder.append(appSvcCgoDto.getName()).append(',');
                }
                if (!StringUtil.isEmpty(stringBuilder.toString())) {
                    String string = stringBuilder.toString();
                    String substring = string.substring(0, string.lastIndexOf(','));
                    String error = MessageUtil.getMessageDesc("NEW_ERR0011");
                    if (substring.contains(",")) {
                        error = error.replaceFirst("is", "are");
                    }
                    String replace = error.replace("{CGO Name}", substring);
                    map.put("CGO", replace);
                }
            }
        }
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

    private void chose(HttpServletRequest request, String type) {
        if ("goveOffice".equals(type)) {
            List<AppSvcCgoDto> appSvcCgoList = (List<AppSvcCgoDto>) ParamUtil.getSessionAttr(request, GOVERNANCEOFFICERSDTOLIST);
            ParamUtil.setRequestAttr(request, "goveOffice", appSvcCgoList);
        }
        if ("checkBox".equals(type)) {
            List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = (List<HcsaSvcSubtypeOrSubsumedDto>) ParamUtil.getSessionAttr(request, "HcsaSvcSubtypeOrSubsumedDto");
            ParamUtil.setRequestAttr(request, "hcsaSvcSubtypeOrSubsumedDtos", hcsaSvcSubtypeOrSubsumedDtos);
        }
    }


    /*
     * get current svc dto
     * */
    private AppSvcRelatedInfoDto getAppSvcRelatedInfo(HttpServletRequest request, String currentSvcId) {
        log.debug(StringUtil.changeForLog("getAppSvcRelatedInfo service id:"+currentSvcId));
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, NewApplicationDelegator.APPSUBMISSIONDTO);
        if (appSubmissionDto != null) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (appSvcRelatedInfoDtos != null && !appSvcRelatedInfoDtos.isEmpty()) {
                for (AppSvcRelatedInfoDto svcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    if (currentSvcId.equals(svcRelatedInfoDto.getServiceId())) {
                        appSvcRelatedInfoDto = svcRelatedInfoDto;
                        break;
                    }
                }
            }
        }
        return appSvcRelatedInfoDto;
    }

    private void setAppSvcRelatedInfoMap(HttpServletRequest request, String currentSvcId, AppSvcRelatedInfoDto appSvcRelatedInfoDto) {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, NewApplicationDelegator.APPSUBMISSIONDTO);
        if (appSubmissionDto != null) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (appSvcRelatedInfoDtos != null && !appSvcRelatedInfoDtos.isEmpty()) {
                for (AppSvcRelatedInfoDto svcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    if (currentSvcId.equals(svcRelatedInfoDto.getServiceId())) {
                        svcRelatedInfoDto = appSvcRelatedInfoDto;
                        break;
                    }
                }
            }
        }
        ParamUtil.setSessionAttr(request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
    }


    private List<AppSvcPersonnelDto> genAppSvcPersonnelDtoList(HttpServletRequest request, List<String> personnelTypeList, String svcCode) {
        List<AppSvcPersonnelDto> appSvcPersonnelDtos = IaisCommonUtils.genNewArrayList();
        String[] personnelSels = ParamUtil.getStrings(request, "personnelSel");
        String[] designations = ParamUtil.getStrings(request, "designation");
        String[] names = ParamUtil.getStrings(request, "name");
        String[] qualifications = ParamUtil.getStrings(request, "qualification");
        String[] wrkExpYears = ParamUtil.getStrings(request, "wrkExpYear");
        String[] professionalRegnNos = ParamUtil.getStrings(request, "regnNo");
        String[] cgoIndexNos = ParamUtil.getStrings(request,"cgoIndexNo");
        if (personnelSels != null && personnelSels.length > 0) {
            for (int i = 0; i < personnelSels.length; i++) {
                AppSvcPersonnelDto appSvcPersonnelDto = new AppSvcPersonnelDto();
                String personnelSel = personnelSels[i];
                appSvcPersonnelDto.setPersonnelType(personnelSel);
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
                appSvcPersonnelDto.setName(name);
                appSvcPersonnelDto.setQualification(qualification);
                appSvcPersonnelDto.setWrkExpYear(wrkExpYear);
                appSvcPersonnelDto.setProfRegNo(professionalRegnNo);
                String cgoIndexNo = cgoIndexNos[i];
                if(!StringUtil.isEmpty(cgoIndexNo)){
                    appSvcPersonnelDto.setCgoIndexNo(cgoIndexNo);
                }else{
                    appSvcPersonnelDto.setCgoIndexNo(UUID.randomUUID().toString());
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
            SelectOption personnelTypeOp2 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST, MasterCodeUtil.getCodeDesc(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST));
            SelectOption personnelTypeOp3 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER, MasterCodeUtil.getCodeDesc(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER));
            personnelTypeSel.add(personnelTypeOp2);
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
        return designation;
    }

    public static List<SelectOption> getAssignPrincipalOfficerSel(HttpServletRequest request, boolean needFirstOpt) {
        List<SelectOption> assignSelectList = IaisCommonUtils.genNewArrayList();
        if (needFirstOpt) {
            SelectOption assignOp1 = new SelectOption("-1", NewApplicationDelegator.FIRESTOPTION);
            assignSelectList.add(assignOp1);
        }
        SelectOption assignOp2 = new SelectOption("newOfficer", "I'd like to add a new personnel");
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
        SelectOption assignOp2 = new SelectOption("newOfficer", "I'd like to add a new personnel");
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
                    appSvcPrincipalOfficersDto.setCgoIndexNo(mapIndexNo);
                }
                if(StringUtil.isEmpty(appSvcPrincipalOfficersDto.getCgoIndexNo())){
                    appSvcPrincipalOfficersDto.setCgoIndexNo(UUID.randomUUID().toString());
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
                    appSvcPrincipalOfficersDto.setCgoIndexNo(UUID.randomUUID().toString());
                } else {
                    appSvcPrincipalOfficersDto.setCgoIndexNo(mapIndexNo);
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

    private AppSvcCgoDto getAppSvcCgoByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo) {
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)) {
            List<AppSvcCgoDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcCgoDtos)) {
                for (AppSvcCgoDto appSvcCgoDto1 : appSvcCgoDtos) {
                    if (indexNo.equals(appSvcCgoDto1.getCgoIndexNo())) {
                        return appSvcCgoDto1;
                    }
                }
            }
        }
        return new AppSvcCgoDto();
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
                    if (indexNo.equals(psnDto.getCgoIndexNo())) {
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
        personMap = NewApplicationHelper.setPsnIntoSelMap(personMap, newPersonList, svcCode);
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
                    if(md5Code.equals(appSvcDocDto.getMd5Code())){
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
                    List<AppSvcCgoDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                    if(!IaisCommonUtils.isEmpty(appSvcCgoDtos)){
                        for(AppSvcCgoDto psn:appSvcCgoDtos){
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
        if(psn == null || IaisCommonUtils.isEmpty(personKeySet)){
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
        Map<String,File> passValidateFileMap = IaisCommonUtils.genNewHashMap();
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
                String psnIndexNo = psnDto.getCgoIndexNo();
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
                if(!StringUtil.isEmpty(psnDto.getCgoIndexNo())){
                    psnIndexList.add(psnDto.getCgoIndexNo());
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
}
