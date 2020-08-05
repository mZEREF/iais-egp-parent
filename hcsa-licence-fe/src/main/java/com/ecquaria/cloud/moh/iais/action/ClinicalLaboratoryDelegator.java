package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.base.FileType;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
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
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
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

    public static final String GOVERNANCEOFFICERS = "GovernanceOfficers";
    public static final String GOVERNANCEOFFICERSDTO = "GovernanceOfficersDto";
    public static final String GOVERNANCEOFFICERSDTOLIST = "GovernanceOfficersList";
    public static final String APPSVCRELATEDINFODTO = "AppSvcRelatedInfoDto";
    public static final String ERRORMAP_GOVERNANCEOFFICERS = "errorMap_governanceOfficers";
    public static final String RELOADSVCDOC = "ReloadSvcDoc";
    public static final String SERVICEPERSONNELTYPE = "ServicePersonnelType";
    public static final String PLEASEINDICATE = "Please indicate";

    //dropdown
    public static final String DROPWOWN_IDTYPESELECT = "IdTypeSelect";

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

        List<SelectOption> idTypeSelectList = NewApplicationHelper.getIdTypeSelOp();
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
    public void prepareDisciplineAllocation(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prepareDisciplineAllocation start ...."));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        List<AppSvcLaboratoryDisciplinesDto> newChkLstDtoList = IaisCommonUtils.genNewArrayList();
        for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList) {
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                if (appSvcLaboratoryDisciplinesDto.getPremiseVal().equals(appGrpPremisesDto.getPremisesIndexNo())) {
                    appSvcLaboratoryDisciplinesDto.setPremiseGetAddress(appGrpPremisesDto.getAddress());
                }
            }
            newChkLstDtoList.add(appSvcLaboratoryDisciplinesDto);
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
        if (appSvcPrincipalOfficersDtos != null && !appSvcPrincipalOfficersDtos.isEmpty()) {
            for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtos) {
                if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(appSvcPrincipalOfficersDto.getPsnType())) {
                    principalOfficersDtos.add(appSvcPrincipalOfficersDto);
                } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(appSvcPrincipalOfficersDto.getPsnType())) {
                    deputyPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                }
            }
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

        List<SelectOption> IdTypeSelect = NewApplicationHelper.getIdTypeSelOp();
        ParamUtil.setRequestAttr(bpc.request, "IdTypeSelect", IdTypeSelect);

        List<SelectOption> assignSelectList = NewApplicationHelper.genAssignPersonSel(bpc.request, true);
        ParamUtil.setRequestAttr(bpc.request, "PrincipalOfficersAssignSelect", assignSelectList);

        List<SelectOption> deputyAssignSelectList = NewApplicationHelper.genAssignPersonSel(bpc.request, true);
        ParamUtil.setRequestAttr(bpc.request, "DeputyPrincipalOfficersAssignSelect", deputyAssignSelectList);

        List<SelectOption> deputyFlagSelect = IaisCommonUtils.genNewArrayList();
        SelectOption deputyFlagOp1 = new SelectOption("-1", NewApplicationDelegator.FIRESTOPTION);
        deputyFlagSelect.add(deputyFlagOp1);
        SelectOption deputyFlagOp2 = new SelectOption("0", "N");
        deputyFlagSelect.add(deputyFlagOp2);
        SelectOption deputyFlagOp3 = new SelectOption("1", "Y");
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
            ParamUtil.setSessionAttr(bpc.request, "serviceDocConfigDto", (Serializable) hcsaSvcDocDtos);
        }

        Map<String, AppSvcDocDto> reloadSvcDo = IaisCommonUtils.genNewHashMap();
        if (appSvcRelatedInfoDto != null) {
            List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
            if (appSvcDocDtos != null && !appSvcDocDtos.isEmpty()) {
                for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                    reloadSvcDo.put(appSvcDocDto.getSvcDocId(), appSvcDocDto);
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request, RELOADSVCDOC, (Serializable) reloadSvcDo);

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
            Map<String, List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = NewApplicationHelper.getDisciplineAllocationDtoList(appSubmissionDto, svcId);
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
            turn(hcsaSvcSubtypeOrSubsumedDtos, map);
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
                        if (PLEASEINDICATE.equals(checkInfo.getName())) {
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
                errorMap = NewApplicationHelper.doValidateLaboratory(appGrpPremisesDtoList, appSvcLaboratoryDisciplinesDtoList, currentSvcId);
                if (appSubmissionDto.isNeedEditController()) {
                    Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                    clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_LABORATORY);
                    appSubmissionDto.setClickEditPage(clickEditPages);
                    AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                    appEditSelectDto.setServiceEdit(true);
                    appSubmissionDto.setChangeSelectDto(appEditSelectDto);
                }
                ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
                HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
                Map<String, String> allChecked = isAllChecked(bpc, appSubmissionDto);
                if (errorMap.isEmpty() && allChecked.isEmpty()) {
                    coMap.put("information", "information");
                } else {
                    coMap.put("information", "");
                }
                bpc.request.getSession().setAttribute("coMap", coMap);
            }
            ParamUtil.setSessionAttr(bpc.request, "reloadLaboratoryDisciplines", (Serializable) reloadChkLstMap);

            currentSvcDto.setAppSvcLaboratoryDisciplinesDtoList(appSvcLaboratoryDisciplinesDtoList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcDto);
            if (!errorMap.isEmpty()) {
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
            map = NewApplicationDelegator.doCheckBox(bpc, sB, svcAllPsnConfig, currentSvcAllPsnConfig, dto.get(i));
        }

        if (!StringUtil.isEmpty(sB.toString())) {
            map.put("error", "error");
        }
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
        if (isGetDataFromPage) {
            List<AppSvcCgoDto> appSvcCgoDtoList = genAppSvcCgoDto(bpc.request);
            //do validate
            Map<String, String> errList = IaisCommonUtils.genNewHashMap();
            String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
            AppSvcRelatedInfoDto currentSvcRelatedDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
            currentSvcRelatedDto.setAppSvcCgoDtoList(appSvcCgoDtoList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto);
            String crud_action_additional = bpc.request.getParameter("nextStep");
            String svcCode = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE);
            List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = NewApplicationHelper.transferCgoToPsnDtoList(appSvcCgoDtoList);
            Map<String,AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.PERSONSELECTMAP);
            //sync person dropdown and submisson dto
            personMap = syncDropDownAndPsn(personMap,appSubmissionDto,appSvcCgoDtos,svcCode);
            ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.PERSONSELECTMAP, (Serializable) personMap);
            if ("next".equals(crud_action_additional)) {
                List<AppSvcCgoDto> appSvcCgoList = (List<AppSvcCgoDto>) ParamUtil.getSessionAttr(bpc.request, GOVERNANCEOFFICERSDTOLIST);
                Map<String,AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.LICPERSONSELECTMAP);
                errList = NewApplicationHelper.doValidateGovernanceOfficers(appSvcCgoList, licPersonMap, svcCode);
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
                    }
                } else {
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.GOVERNANCEOFFICERS);
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errList));
                    coMap.put("information", "");
                    return;
                }
                bpc.request.getSession().setAttribute("coMap", coMap);
            }
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
                    AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto = null;
                    int chkLstSize = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList().size();
                    for (int i = 0; i < chkLstSize; i++) {
                        StringBuilder chkAndCgoName = new StringBuilder()
                                .append(premisesValue)
                                .append(i);
                        String[] chkAndCgoValue = ParamUtil.getStrings(bpc.request, chkAndCgoName.toString());
                        if (chkAndCgoValue != null && chkAndCgoValue.length > 1) {
                            appSvcDisciplineAllocationDto = new AppSvcDisciplineAllocationDto();
                            //appSvcDisciplineAllocationDto.setPremiseType(premisesType);
                            appSvcDisciplineAllocationDto.setPremiseVal(premisesValue);
                            appSvcDisciplineAllocationDto.setChkLstConfId(chkAndCgoValue[0]);//NOSONAR
                            appSvcDisciplineAllocationDto.setIdNo(chkAndCgoValue[1]);//NOSONAR
                            daList.add(appSvcDisciplineAllocationDto);
                        }
                    }
                }
            }

            String crud_action_additional = ParamUtil.getRequestString(bpc.request, "nextStep");
            if ("next".equals(crud_action_additional)) {

                doValidateDisciplineAllocation(errorMap, daList, bpc.request);
                if (appSubmissionDto.isNeedEditController()) {
                    Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                    clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_DISCIPLINE_ALLOCATION);
                    appSubmissionDto.setClickEditPage(clickEditPages);
                    AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                    appEditSelectDto.setServiceEdit(true);
                    appSubmissionDto.setChangeSelectDto(appEditSelectDto);
                }
                ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);


                HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
                Map<String, String> allChecked = isAllChecked(bpc, appSubmissionDto);
                if (errorMap.isEmpty() && allChecked.isEmpty()) {
                    coMap.put("information", "information");
                } else {
                    coMap.put("information", "");
                }
                bpc.request.getSession().setAttribute("coMap", coMap);
            }
            currentSvcRelatedDto.setAppSvcDisciplineAllocationDtoList(daList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto);
            if (!errorMap.isEmpty()) {
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
            //sync person dropdown and submisson dto
            personMap = syncDropDownAndPsn(personMap,appSubmissionDto,appSvcPrincipalOfficersDtoList,svcCode);
            ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.PERSONSELECTMAP, (Serializable) personMap);
            if ("next".equals(crud_action_additional)) {
                List<AppSvcPrincipalOfficersDto> poDto = (List<AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(bpc.request, "AppSvcPrincipalOfficersDto");
                Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.LICPERSONSELECTMAP);
                map = NewApplicationHelper.doValidatePo(poDto, licPersonMap,svcCode);
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
                    }
                } else {
                    coMap.put("information", "");
                }
                bpc.request.getSession().setAttribute("coMap", coMap);
            }
            if (!map.isEmpty()) {
                //ParamUtil.setSessionAttr(bpc.request, "", );
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
        List<AppSvcDocDto> appSvcDocDtoList = IaisCommonUtils.genNewArrayList();
        AppSvcDocDto appSvcDocDto = null;
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
        if (isGetDataFromPage) {
            Map<String, AppSvcDocDto> beforeReloadDocMap = (Map<String, AppSvcDocDto>) ParamUtil.getSessionAttr(bpc.request, RELOADSVCDOC);
            List<HcsaSvcDocConfigDto> svcDocConfigDtos = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request, "serviceDocConfigDto");
            Map<String, CommonsMultipartFile> commonsMultipartFileMap = IaisCommonUtils.genNewHashMap();
            CommonsMultipartFile file = null;

            if (appSubmissionDto.isNeedEditController()) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_DOCUMENT);
                appSubmissionDto.setClickEditPage(clickEditPages);
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                appEditSelectDto.setServiceEdit(true);
                appSubmissionDto.setChangeSelectDto(appEditSelectDto);
            }

            if (svcDocConfigDtos != null && !svcDocConfigDtos.isEmpty()) {
                for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : svcDocConfigDtos) {
                    String docConfigId = hcsaSvcDocConfigDto.getId();
                    String delFlag = docConfigId + "flag";
                    String delFlagValue = mulReq.getParameter(delFlag);
                    String fileName = docConfigId + "selectedFile";
                    file = (CommonsMultipartFile) mulReq.getFile(fileName);
                    if (file != null && file.getSize() != 0) {
                        if (!StringUtil.isEmpty(file.getOriginalFilename())) {
                            file.getFileItem().setFieldName("selectedFile");
                            appSvcDocDto = new AppSvcDocDto();
                            appSvcDocDto.setSvcDocId(docConfigId);
                            appSvcDocDto.setUpFileName(hcsaSvcDocConfigDto.getDocTitle());
                            appSvcDocDto.setDocName(file.getOriginalFilename());
                            long size = file.getSize() / 1024;
                            appSvcDocDto.setDocSize(Integer.valueOf(String.valueOf(size)));
                            String md5Code = FileUtil.genMd5FileChecksum(file.getBytes());
                            appSvcDocDto.setMd5Code(md5Code);
                            commonsMultipartFileMap.put(docConfigId, file);
                            //wait api change to get fileRepoId
                            appSvcDocDtoList.add(appSvcDocDto);
                        }
                    } else if ("N".equals(delFlagValue)) {
                        AppSvcDocDto beforeDto = beforeReloadDocMap.get(docConfigId);
                        if (beforeDto != null) {
                            appSvcDocDtoList.add(beforeDto);
                        }
                    }

                }
            }
            String crud_action_values = mulReq.getParameter("nextStep");

            String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
            appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtoList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, appSvcRelatedInfoDto);
            log.debug(StringUtil.changeForLog("the do doDocuments end ...."));

            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
            if ("next".equals(crud_action_values)) {
                doValidateSvcDocument(bpc.request, errorMap);
            }
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
            Map<String, String> allChecked = isAllChecked(bpc, appSubmissionDto);
            if (errorMap.isEmpty() && allChecked.isEmpty()) {
                coMap.put("information", "information");
            } else {
                coMap.put("information", "");
            }
            bpc.request.getSession().setAttribute("coMap", coMap);

            if (!errorMap.isEmpty()) {
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, AppServicesConsts.NAVTABS_SERVICEFORMS);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.DOCUMENTS);
                mulReq.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.DOCUMENTS);
                return;
            }

            if (commonsMultipartFileMap != null && commonsMultipartFileMap.size() > 0) {
                for (AppSvcDocDto appSvcDocDto1 : appSvcDocDtoList) {
                    String key = appSvcDocDto1.getSvcDocId();
                    CommonsMultipartFile commonsMultipartFile = commonsMultipartFileMap.get(key);
                    if (commonsMultipartFile != null && commonsMultipartFile.getSize() != 0) {
                        String fileRepoGuid = serviceConfigService.saveFileToRepo(commonsMultipartFile);
                        appSvcDocDto1.setFileRepoId(fileRepoGuid);
                    }
                }
            }

        }

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
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION, isEdit, isRfi);
        if (isGetDataFromPage) {
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
            String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
            String currentSvcCod = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE);
            List<AppSvcPersonnelDto> appSvcPersonnelDtos = IaisCommonUtils.genNewArrayList();
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
            List<String> personnelTypeList = IaisCommonUtils.genNewArrayList();
            List<SelectOption> personnelTypeSel = genPersonnelTypeSel(currentSvcCod);
            for (SelectOption sp : personnelTypeSel) {
                personnelTypeList.add(sp.getValue());
            }
            appSvcPersonnelDtos = genAppSvcPersonnelDtoList(bpc.request, personnelTypeList, currentSvcCod);
            appSvcRelatedInfoDto.setAppSvcPersonnelDtoList(appSvcPersonnelDtos);

       /* if(AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(currentSvcCod)){

        }else if(AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(currentSvcCod)){

        }else if(AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(currentSvcCod)){
            //:todo
        }else if(AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(currentSvcCod)){
            //:todo
        }*/

            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, appSvcRelatedInfoDto);
            String nextStep = ParamUtil.getRequestString(bpc.request, "nextStep");
            if (!StringUtil.isEmpty(nextStep)) {
                doValidatetionServicePerson(errorMap, appSvcPersonnelDtos, currentSvcCod);
                if (appSubmissionDto.isNeedEditController()) {
                    Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                    clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_SERVICE_PERSONNEL);
                    appSubmissionDto.setClickEditPage(clickEditPages);
                }
                ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
            }
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
            Map<String, String> allChecked = isAllChecked(bpc, appSubmissionDto);
            if (errorMap.isEmpty() && allChecked.isEmpty()) {
                coMap.put("information", "information");
            } else {
                coMap.put("information", "");
            }
            bpc.request.getSession().setAttribute("coMap", coMap);
            if (!errorMap.isEmpty()) {
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.NUCLEARMEDICINEIMAGING);
                return;
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
        List<SelectOption> idTypeSelectList = NewApplicationHelper.getIdTypeSelOp();
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
            //sync person dropdown and submisson dto
            personMap = syncDropDownAndPsn(personMap,appSubmissionDto,appSvcMedAlertPersonList,svcCode);
            ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.PERSONSELECTMAP, (Serializable) personMap);
            if ("next".equals(nextStep)) {
                if (appSubmissionDto.isNeedEditController()) {
//                    Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
//                    clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_MEDALERT_PERSON);
//                    appSubmissionDto.setClickEditPage(clickEditPages);
                    AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                    appEditSelectDto.setServiceEdit(true);
                    appSubmissionDto.setChangeSelectDto(appEditSelectDto);
                }
                Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.LICPERSONSELECTMAP);
                Map<String, String> errorMap = NewApplicationHelper.doValidateMedAlertPsn(appSvcMedAlertPersonList, licPersonMap, svcCode);
                HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
                Map<String, String> allChecked = isAllChecked(bpc, appSubmissionDto);
                if (errorMap.isEmpty() && allChecked.isEmpty()) {
                    coMap.put("information", "information");
                } else {
                    coMap.put("information", "");
                }
                bpc.request.getSession().setAttribute("coMap",coMap);
                if (!errorMap.isEmpty()) {
                    ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.MEDALERT_PERSON);
                    //todo change(medAlert page is the final svc page,will jump to preview page)
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,AppServicesConsts.NAVTABS_SERVICEFORMS);
                    return;
                } else {
                }
            }
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
        }

        log.debug(StringUtil.changeForLog("the do doMedAlertPerson end ...."));
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

    private void turn(List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos, Map<String, HcsaSvcSubtypeOrSubsumedDto> allCheckListMap) {

        for (HcsaSvcSubtypeOrSubsumedDto dto : hcsaSvcSubtypeOrSubsumedDtos) {
            allCheckListMap.put(dto.getId(), dto);
            if (dto.getList() != null && dto.getList().size() > 0) {
                turn(dto.getList(), allCheckListMap);
            }
        }

    }

    private static void doValidatetionServicePerson(Map<String, String> errorMap, List<AppSvcPersonnelDto> appSvcPersonnelDtos, String svcCode) {
        for (int i = 0; i < appSvcPersonnelDtos.size(); i++) {
            if (AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode)) {
                String designation = appSvcPersonnelDtos.get(i).getDesignation();
                if (StringUtil.isEmpty(designation)) {
                    errorMap.put("designation" + i, "UC_CHKLMD001_ERR001");
                }
                String name = appSvcPersonnelDtos.get(i).getName();
                if (StringUtil.isEmpty(name)) {
                    errorMap.put("name" + i, "UC_CHKLMD001_ERR001");
                }
                String profRegNo = appSvcPersonnelDtos.get(i).getProfRegNo();
                if (StringUtil.isEmpty(profRegNo)) {
                    errorMap.put("regnNo" + i, "UC_CHKLMD001_ERR001");
                }
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                if (StringUtil.isEmpty(wrkExpYear)) {
                    errorMap.put("wrkExpYear" + i, "UC_CHKLMD001_ERR001");
                } else {
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        errorMap.put("wrkExpYear" + i, "CHKLMD001_ERR003");
                    }
                }
            } else if (AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)) {
                String name = appSvcPersonnelDtos.get(i).getName();
                if (StringUtil.isEmpty(name)) {
                    errorMap.put("name" + i, "UC_CHKLMD001_ERR001");
                }
                String quaification = appSvcPersonnelDtos.get(i).getQualification();
                if (StringUtil.isEmpty(quaification)) {
                    errorMap.put("qualification" + i, "UC_CHKLMD001_ERR001");
                }
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                if (StringUtil.isEmpty(wrkExpYear)) {
                    errorMap.put("wrkExpYear" + i, "UC_CHKLMD001_ERR001");
                } else {
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        errorMap.put("wrkExpYear" + i, "CHKLMD001_ERR003");
                    }
                }
            } else {
                String personnelSel = appSvcPersonnelDtos.get(i).getPersonnelType();
                if (StringUtils.isEmpty(personnelSel)) {
                    errorMap.put("personnelSelErrorMsg" + i, "UC_CHKLMD001_ERR001");
                }

                if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE.equals(personnelSel)) {
                    String profRegNo = appSvcPersonnelDtos.get(i).getProfRegNo();
                    String name = appSvcPersonnelDtos.get(i).getName();
                    if (StringUtil.isEmpty(name)) {
                        errorMap.put("name" + i, "UC_CHKLMD001_ERR001");
                    }
                    if (StringUtil.isEmpty(profRegNo)) {
                        errorMap.put("regnNo" + i, "UC_CHKLMD001_ERR001");
                    }
                }
                if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    String designation = appSvcPersonnelDtos.get(i).getDesignation();
                    String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                    String qualification = appSvcPersonnelDtos.get(i).getQualification();

                    if (StringUtil.isEmpty(name)) {
                        errorMap.put("name" + i, "UC_CHKLMD001_ERR001");
                    }
                    if (StringUtil.isEmpty(designation)) {
                        errorMap.put("designation" + i, "UC_CHKLMD001_ERR001");
                    }
                    if (StringUtil.isEmpty(wrkExpYear)) {
                        errorMap.put("wrkExpYear" + i, "UC_CHKLMD001_ERR001");
                    } else {
                        if (!wrkExpYear.matches("^[0-9]*$")) {
                            errorMap.put("wrkExpYear" + i, "CHKLMD001_ERR003");
                        }
                    }
                    if (StringUtil.isEmpty(qualification)) {
                        errorMap.put("qualification" + i, "UC_CHKLMD001_ERR001");
                    }
                }

                if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                    String quaification = appSvcPersonnelDtos.get(i).getQualification();
                    if (StringUtil.isEmpty(name)) {
                        errorMap.put("name" + i, "UC_CHKLMD001_ERR001");
                    }
                    if (StringUtil.isEmpty(wrkExpYear)) {
                        errorMap.put("wrkExpYear" + i, "UC_CHKLMD001_ERR001");
                    } else {
                        if (!wrkExpYear.matches("^[0-9]*$")) {
                            errorMap.put("wrkExpYear" + i, "CHKLMD001_ERR003");
                        }
                    }
                    if (StringUtil.isEmpty(quaification)) {
                        errorMap.put("qualification" + i, "UC_CHKLMD001_ERR001");
                    }
                }
                if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    if (StringUtil.isEmpty(name)) {
                        errorMap.put("name" + i, "UC_CHKLMD001_ERR001");
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
            size = cgoIndexNos.length;
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
                if(appPsnEditDto.isSpecialityOther() && "other".equals(appSvcPrincipalOfficersDto.getSpecialityOther())){
                    specialtyOther = NewApplicationHelper.setPsnValue(specialtyOther,i,appSvcPrincipalOfficersDto,"specialityOther");
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
                if (StringUtil.isEmpty(appSvcPrincipalOfficersDto.getCgoIndexNo())) {
                    appSvcCgoDto.setCgoIndexNo(UUID.randomUUID().toString());
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
                appSvcCgoDto.setIdNo(idNo[i]);
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
                appSvcCgoDto.setEmailAddr(emailAddress[i]);
                if (needEdit && AppConsts.YES.equals(licPerson[i])) {
                    appSvcCgoDto.setLicPerson(true);
                }
                appSvcCgoDtoList.add(appSvcCgoDto);
            }
        }
        ParamUtil.setSessionAttr(request, GOVERNANCEOFFICERSDTOLIST, (Serializable) appSvcCgoDtoList);
        log.info(StringUtil.changeForLog("genAppSvcCgoDto end ...."));
        return appSvcCgoDtoList;
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
            int length =  0;
            if(assignSelect != null){
                length = assignSelect.length;
            }

            for (int i = 0; i < length; i++) {
                boolean chooseExisting = false;
                boolean getPageData = false;
                String assign = assignSelect[i];
                String licPsn = poLicPerson[i];
                //for rfi,rfc,renew use
                String existingPsn = poExistingPsn[i];
                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
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
                        if(AppConsts.YES.equals(existingPsn)){
                            chooseExisting = true;
                        } else{
                            getPageData = true;
                        }
                    }
                }else{
                    log.info(StringUtil.changeForLog("The current type is not supported"));
                }
                log.info(StringUtil.changeForLog("chooseExisting:"+chooseExisting));
                log.info(StringUtil.changeForLog("getPageData:"+getPageData));
                if(chooseExisting){
                    appSvcPrincipalOfficersDto = NewApplicationHelper.getPsnInfoFromLic(request, assignSelect[i]);
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
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSelect[i]);
                    appSvcPrincipalOfficersDto.setLicPerson(true);
                    appSvcPrincipalOfficersDto.setSelectDropDown(true);
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
                    appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                    //change arr index
                    poExistingPsn = removeArrIndex(poExistingPsn,i);
                    poLicPerson = removeArrIndex(poLicPerson, i);
                    //dropdown cannot disabled
                    assignSelect = removeArrIndex(assignSelect, i);
                    salutation = removeArrIndex(salutation, i);
                    idType = removeArrIndex(idType, i);
                    designation = removeArrIndex(designation, i);
                    --i;
                    --length;
                }else if(getPageData){
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSelect[i]);
                    appSvcPrincipalOfficersDto.setSalutation(salutation[i]);
                    appSvcPrincipalOfficersDto.setName(name[i]);
                    appSvcPrincipalOfficersDto.setIdType(idType[i]);
                    appSvcPrincipalOfficersDto.setIdNo(idNo[i]);
                    appSvcPrincipalOfficersDto.setDesignation(designation[i]);
                    appSvcPrincipalOfficersDto.setMobileNo(mobileNo[i]);
                    appSvcPrincipalOfficersDto.setOfficeTelNo(officeTelNo[i]);
                    appSvcPrincipalOfficersDto.setEmailAddr(emailAddress[i]);
                    if (needEdit && AppConsts.YES.equals(licPsn)) {
                        appSvcPrincipalOfficersDto.setLicPerson(true);
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
            int length = 0;
            if(assignSelect != null){
                length = assignSelect.length;
            }
            for (int i = 0; i < length; i++) {
                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
                String assign = assignSelect[i];
                String licPsn = dpoLicPerson[i];
                boolean chooseExisting = false;
                boolean getPageData = false;
                //for rfi,rfc,renew use
                String existingPsn = dpoExistingPsn[i];
                //new and not rfi
                if(!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    if (assign != null) {
                        if (isExistingPsn(assign,licPsn)) {
                            chooseExisting = true;
                        }else{
                            getPageData = true;
                        }
                    }
                }else if(needEdit){
                    if (assign != null) {
                        if(AppConsts.YES.equals(existingPsn)){
                            chooseExisting = true;
                        } else{
                            getPageData = true;
                        }
                    }
                }else {
                    log.info(StringUtil.changeForLog("The current type is not supported"));
                }
                log.info(StringUtil.changeForLog("chooseExisting:"+chooseExisting));
                log.info(StringUtil.changeForLog("getPageData:"+getPageData));
                if(chooseExisting){
                    appSvcPrincipalOfficersDto = NewApplicationHelper.getPsnInfoFromLic(request, assignSelect[i]);
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
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSelect[i]);
                    appSvcPrincipalOfficersDto.setLicPerson(true);
                    appSvcPrincipalOfficersDto.setSelectDropDown(true);
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
                    appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                    //change arr index
                    dpoLicPerson = removeArrIndex(dpoLicPerson, i);
                    dpoExistingPsn = removeArrIndex(dpoExistingPsn,i);
                    //dropdown cannot disabled
                    assignSelect = removeArrIndex(assignSelect, i);
                    deputySalutation = removeArrIndex(deputySalutation, i);
                    deputyIdType = removeArrIndex(deputyIdType, i);
                    deputyDesignation = removeArrIndex(deputyDesignation, i);
                    --i;
                    --length;
                }else if(getPageData){
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSelect[i]);
                    appSvcPrincipalOfficersDto.setSalutation(deputySalutation[i]);
                    appSvcPrincipalOfficersDto.setName(deputyName[i]);
                    appSvcPrincipalOfficersDto.setIdType(deputyIdType[i]);
                    appSvcPrincipalOfficersDto.setIdNo(deputyIdNo[i]);
                    appSvcPrincipalOfficersDto.setDesignation(deputyDesignation[i]);
                    appSvcPrincipalOfficersDto.setMobileNo(deputyMobileNo[i]);
                    appSvcPrincipalOfficersDto.setOfficeTelNo(deputyOfficeTelNo[i]);
                    appSvcPrincipalOfficersDto.setEmailAddr(deputyEmailAddr[i]);
                    if (needEdit && AppConsts.YES.equals(licPsn)) {
                        appSvcPrincipalOfficersDto.setLicPerson(true);
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
                map.put("disciplineAllocation" + i, "UC_CHKLMD001_ERR001");
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
                    String error = MessageUtil.getMessageDesc("UC_CHKLMD001_ERR005");
                    if (substring.contains(",")) {
                        error = error.replaceFirst("is", "are");
                    }
                    String replace = error.replace("<CGO Name>", substring);
                    map.put("CGO", replace);
                }
            }
        }
    }

    private void doValidateSvcDocument(HttpServletRequest request, Map<String, String> errorMap) {
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        if (appSubmissionDto != null) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (appSvcRelatedInfoDtoList != null) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList) {
                    List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                    if (appSvcDocDtoLit != null) {
                        for (int i = 0; i < appSvcDocDtoLit.size(); i++) {
                            Integer docSize = appSvcDocDtoLit.get(i).getDocSize();
                            String docName = appSvcDocDtoLit.get(i).getDocName();
                            String id = appSvcDocDtoLit.get(i).getSvcDocId();
                            if (docSize > 4 * 1024) {
                                errorMap.put(id + "selectedFile", "UC_CHKLMD001_ERR007");
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
                                errorMap.put(id + "selectedFile", "Wrong file type");
                            }
                            String errMsg = errorMap.get(id + "selectedFile");
                            if(StringUtil.isEmpty(errMsg)){
                                appSvcDocDtoLit.get(i).setPassValidate(true);
                            }
                        }
                    }
                    appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtoLit);
                }
                appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList);
                ParamUtil.setSessionAttr(request,NewApplicationDelegator.APPSUBMISSIONDTO,appSubmissionDto);
            }
        }

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
                }


                appSvcPersonnelDto.setDesignation(designation);
                appSvcPersonnelDto.setName(name);
                appSvcPersonnelDto.setQualification(qualification);
                appSvcPersonnelDto.setWrkExpYear(wrkExpYear);
                appSvcPersonnelDto.setProfRegNo(professionalRegnNo);
                appSvcPersonnelDtos.add(appSvcPersonnelDto);
            }
        }
        return appSvcPersonnelDtos;
    }

    public static List<SelectOption> genPersonnelTypeSel(String currentSvcCod) {
        List<SelectOption> personnelTypeSel = IaisCommonUtils.genNewArrayList();
        if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(currentSvcCod)) {
            SelectOption personnelTypeOp1 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL, ApplicationConsts.SERVICE_PERSONNEL_TYPE_STR_RADIOLOGY_PROFESSIONAL);
            SelectOption personnelTypeOp2 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST, ApplicationConsts.SERVICE_PERSONNEL_TYPE_STR_MEDICAL_PHYSICIST);
            SelectOption personnelTypeOp3 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER, ApplicationConsts.SERVICE_PERSONNEL_TYPE_STR_RADIATION_SAFETY_OFFICER);
            SelectOption personnelTypeOp4 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE, ApplicationConsts.SERVICE_PERSONNEL_TYPE_STR_REGISTERED_NURSE);
            personnelTypeSel.add(personnelTypeOp1);
            personnelTypeSel.add(personnelTypeOp2);
            personnelTypeSel.add(personnelTypeOp3);
            personnelTypeSel.add(personnelTypeOp4);
        } else if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(currentSvcCod)) {
            SelectOption personnelTypeOp2 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST, ApplicationConsts.SERVICE_PERSONNEL_TYPE_STR_MEDICAL_PHYSICIST);
            SelectOption personnelTypeOp3 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER, ApplicationConsts.SERVICE_PERSONNEL_TYPE_STR_RADIATION_SAFETY_OFFICER);
            personnelTypeSel.add(personnelTypeOp2);
            personnelTypeSel.add(personnelTypeOp3);
        } else if (AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(currentSvcCod)) {

        } else if (AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(currentSvcCod)) {

        }
        return personnelTypeSel;
    }

    public static List<SelectOption> genPersonnelDesignSel(String currentSvcCod) {
        List<SelectOption> designation = IaisCommonUtils.genNewArrayList();
        if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(currentSvcCod)) {
            SelectOption designationOp1 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_DIAGNOSTIC_RADIOGRAPHER, ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_DIAGNOSTIC_RADIOGRAPHER);
            SelectOption designationOp2 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_RADIATION_THERAPIST, ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_RADIATION_THERAPIST);
            SelectOption designationOp3 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_NUCLEAR_MEDICINE_TECHNOLOGIST, ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_NUCLEAR_MEDICINE_TECHNOLOGIST);
            designation.add(designationOp1);
            designation.add(designationOp2);
            designation.add(designationOp3);
        } else if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(currentSvcCod)) {

        } else if (AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(currentSvcCod)) {
            SelectOption designationOp1 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_MEDICAL_PRACTITIONER, ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_MEDICAL_PRACTITIONER);
            SelectOption designationOp2 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_CLINICAL_NURSE_LEADER, ApplicationConsts.SERVICE_PERSONNEL_DESIGNATION_CLINICAL_NURSE_LEADER);
            designation.add(designationOp1);
            designation.add(designationOp2);
        } else if (AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(currentSvcCod)) {

        }
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

    private AppSubmissionDto setPrincipalOfficersClickEditPage(AppSubmissionDto appSubmissionDto, Boolean isGetDataFromPagePo, Boolean isGetDataFromPageDpo) {
        Set<String> clickEditPages = getRfcClickEditPageSet(appSubmissionDto);
        if (isGetDataFromPagePo) {
            clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_PRINCIPAL_OFFICERS);
        }
        if (isGetDataFromPageDpo) {
            clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_DEPUTY_PRINCIPAL_OFFICERS);
        }
        appSubmissionDto.setClickEditPage(clickEditPages);
        return appSubmissionDto;
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
        List<AppSvcPrincipalOfficersDto> medAlertPersons = IaisCommonUtils.genNewArrayList();
        int length = assignSelect.length;
        //new and not rfi
        int preferredModeLength = 0;
        for (int i = 0; i < length; i++) {
            AppPsnEditDto appPsnEditDto = new AppPsnEditDto();
            AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
            String assign = assignSelect[i];
            String licPsn = licPerson[i];
            boolean chooseExisting = false;
            boolean getPageData = false;
            String existPsn = existingPsn[i];
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                if (assign != null) {
                    if (isExistingPsn(assign, licPsn)) {
                        chooseExisting = true;
                    } else {
                        getPageData = true;
                    }
                }
            } else if (needEdit) {
                if (assign != null) {
                    if(AppConsts.YES.equals(existPsn)){
                        chooseExisting = true;
                    } else{
                        getPageData = true;
                    }
                }
            } else {
                log.info(StringUtil.changeForLog("The current type is not supported"));
            }
            log.info(StringUtil.changeForLog("chooseExisting:"+chooseExisting));
            log.info(StringUtil.changeForLog("getPageData:"+getPageData));
            String[] preferredModes = ParamUtil.getStrings(request, "preferredMode" + preferredModeLength);
            if (chooseExisting) {
                appSvcPrincipalOfficersDto = NewApplicationHelper.getPsnInfoFromLic(request, assignSelect[i]);
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
                if (appPsnEditDto.isPreferredMode()) {
                    if (preferredModes != null) {
                        if (preferredModes.length == 2) {
                            appSvcPrincipalOfficersDto.setPreferredMode("3");
                        } else {
                            appSvcPrincipalOfficersDto.setPreferredMode(preferredModes[0]);
                        }
                    }
                }
                appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
                appSvcPrincipalOfficersDto.setAssignSelect(assignSelect[i]);
                appSvcPrincipalOfficersDto.setLicPerson(true);
                appSvcPrincipalOfficersDto.setSelectDropDown(true);
                medAlertPersons.add(appSvcPrincipalOfficersDto);
                //change arr index
                licPerson = removeArrIndex(licPerson, i);
                existingPsn = removeArrIndex(existingPsn,i);
                //dropdown cannot disabled
                assignSelect = removeArrIndex(assignSelect, i);
                salutation = removeArrIndex(salutation, i);
                idType = removeArrIndex(idType, i);
                --i;
                --length;
                ++preferredModeLength;
            }else if(getPageData){
                appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
                appSvcPrincipalOfficersDto.setAssignSelect(assignSelect[i]);
                appSvcPrincipalOfficersDto.setSalutation(salutation[i]);
                appSvcPrincipalOfficersDto.setName(name[i]);
                appSvcPrincipalOfficersDto.setIdType(idType[i]);
                appSvcPrincipalOfficersDto.setIdNo(idNo[i]);
                appSvcPrincipalOfficersDto.setMobileNo(mobileNo[i]);
                appSvcPrincipalOfficersDto.setEmailAddr(emailAddress[i]);
                if (preferredModes != null) {
                    if (preferredModes.length == 2) {
                        appSvcPrincipalOfficersDto.setPreferredMode("3");
                    } else {
                        appSvcPrincipalOfficersDto.setPreferredMode(preferredModes[0]);
                    }
                }
                ++preferredModeLength;
                if (needEdit && AppConsts.YES.equals(licPsn)) {
                    appSvcPrincipalOfficersDto.setLicPerson(true);
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
        appPsnEditDto.setPreferredMode(false);
    }

    private Map<String,AppSvcPersonAndExtDto> syncDropDownAndPsn(Map<String,AppSvcPersonAndExtDto> personMap,AppSubmissionDto appSubmissionDto,List<AppSvcPrincipalOfficersDto> personList,String svcCode){
        for(AppSvcPrincipalOfficersDto person:personList){
            String idType = person.getIdType();
            String idNo = person.getIdNo();
            String name = person.getName();
            //Provisional judgment
            //personnel data=>sync , personnel ext data => the same svc =>sync
            boolean needSync = !StringUtil.isEmpty(idType) && !StringUtil.isEmpty(idNo) && !StringUtil.isEmpty(name);
            if(needSync){
                //set person into dropdown
                personMap = NewApplicationHelper.setPsnIntoSelMap(personMap, personList, svcCode);
                //sync data
                NewApplicationHelper.syncPsnData(appSubmissionDto, personMap);
            }
        }
        return personMap;
    }

}
