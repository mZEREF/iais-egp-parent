package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AppDataHelper;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.RfcHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.OrganizationService;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import com.ecquaria.cloud.moh.iais.validation.DeclarationsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.ACKMESSAGE;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.ACKSTATUS;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.ACK_APP_SUBMISSIONS;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.ACTION_BACK;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.ACTION_NEXT;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.ACTION_PREVIEW;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.APPSUBMISSIONDTO;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.APP_SUBMISSIONS;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.COND_TYPE_RFI;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.IS_EDIT;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.LICENSEE_MAP;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.LICENSEE_OPTIONS;

/**
 * @author chenlei on 5/3/2022.
 */
@Slf4j
public abstract class AppCommDelegator {

    @Autowired
    protected AppCommService appCommService;

    @Autowired
    protected LicCommService licCommService;

    @Autowired
    protected OrganizationService organizationService;

    @Autowired
    protected ConfigCommService configCommService;

    @Autowired
    protected SystemParamConfig systemParamConfig;

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("the do Start start ...."));
        CompletableFuture<String> future = HcsaServiceCacheHelper.flushServiceMappingAsync();
        DealSessionUtil.clearSession(bpc.request);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_NEW_APPLICATION, AuditTrailConsts.FUNCTION_NEW_APPLICATION);
        //fro draft loading
        String draftNo = ParamUtil.getMaskedString(bpc.request, HcsaAppConst.DRAFT_NUMBER);
        //for rfi loading
        String appNo = ParamUtil.getMaskedString(bpc.request, "appNo");
        log.info(StringUtil.changeForLog("DraftNumber: + " + draftNo + " ----- AppNo: " + appNo));
        // rfc or renew
        requestForChangeOrRenewLoading(bpc.request);
        //renewLicence(bpc);
        requestForInformationLoading(bpc.request, appNo);
        //for loading the draft by appId
        loadingDraft(bpc.request, draftNo);
        //load new application info
        loadingNewAppInfo(bpc.request);
        //for loading Service Config
        String statust = future.get(5, TimeUnit.SECONDS);
        boolean flag = AppConsts.SUCCESS.equals(statust) && loadingServiceConfig(bpc);
        log.info(StringUtil.changeForLog("The loadingServiceConfig -->:" + flag));
        if (flag) {
            //TODO renewal draft test
            Object sessionAttr = ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.LOADING_DRAFT);
            log.info(StringUtil.changeForLog("Renewal Draft: " + sessionAttr));
            //TODO end
            boolean fromDraft = ApplicationHelper.checkFromDraft(bpc.request);
            DealSessionUtil.initSession(fromDraft, bpc.request);
        }
        log.info(StringUtil.changeForLog("the do Start end ...."));
    }

    protected void loadingNewAppInfo(HttpServletRequest request) {}

    protected void loadingDraft(HttpServletRequest request, String draftNo) {}

    protected void requestForChangeOrRenewLoading(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("the do requestForChangeLoading start ...."));
        String appType = (String) ParamUtil.getRequestAttr(request, "appType");
        String currentEdit = (String) ParamUtil.getRequestAttr(request, RfcConst.RFC_CURRENT_EDIT);
        boolean canDoEdit = (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType));
        if (!canDoEdit || StringUtil.isEmpty(currentEdit)) {
            return;
        }

        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getRequestAttr(request, RfcConst.APPSUBMISSIONDTORFCATTR);
        if (appSubmissionDto != null) {
            AuditTrailHelper.setAuditTrailInfoByAppType(appType);
            ParamUtil.setSessionAttr(request, "hasDetail", "Y");
            ParamUtil.setSessionAttr(request, "isSingle", "Y");
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            switch (currentEdit) {
                case HcsaAppConst.ACTION_LICENSEE:
                    appEditSelectDto.setLicenseeEdit(true);
                    ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "licensee");
                    break;
                case RfcConst.EDIT_PREMISES:
                    appEditSelectDto.setPremisesEdit(true);
                    ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, HcsaAppConst.ACTION_PREMISES);
                    break;
                case RfcConst.EDIT_SPECIALISED:
                    appEditSelectDto.setSpecialisedEdit(true);
                    ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "specialised");
                    break;
                case RfcConst.EDIT_SERVICE:
                    appEditSelectDto.setServiceEdit(true);
                    ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "serviceForms");
                    break;
            }
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDto.setNeedEditController(true);
            ParamUtil.setSessionAttr(request, APPSUBMISSIONDTO, appSubmissionDto);
            //DealSessionUtil.initCoMap(request);
        }
        log.info(StringUtil.changeForLog("the do requestForChangeLoading end ...."));
    }

    protected void requestForInformationLoading(HttpServletRequest request, String appNo) {
    }

    /**
     * Used in requestForInformationLoading
     *
     * @param appSubmissionDto
     * @param appNo
     */
    protected void handlePremises(AppSubmissionDto appSubmissionDto, String appNo) {
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
            //filter other premise info
            if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())
                    && appGrpPremisesDtos.size() > 1) {
                List<AppGrpPremisesDto> newAppGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
                filtrationAppGrpPremisesDtos(appNo, appSubmissionDto, newAppGrpPremisesDtoList);
                appGrpPremisesDtos = newAppGrpPremisesDtoList;
            }
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                //appGrpPremisesDto.setFromDB(true);
                appGrpPremisesDto.setExistingData(AppConsts.NO);
                //appGrpPremisesDto.setRfiCanEdit(true);
                //clear operation premId
                List<AppPremisesOperationalUnitDto> operationalUnitDtos = appGrpPremisesDto.getAppPremisesOperationalUnitDtos();
                if (!IaisCommonUtils.isEmpty(operationalUnitDtos)) {
                    for (AppPremisesOperationalUnitDto operationalUnitDto : operationalUnitDtos) {
                        operationalUnitDto.setId(null);
                        operationalUnitDto.setPremisesId(null);
                    }
                }
            }
            appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtos);
        }
    }

    protected void filtrationAppGrpPremisesDtos(String appNo, AppSubmissionDto appSubmissionDto,
            List<AppGrpPremisesDto> newPremisesDtos) {
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if (appGrpPremisesDtoList == null) {
            return;
        }
        boolean addPremisesSuccess = false;
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (appSvcRelatedInfoDtoList != null) {
            Optional<AppGrpPremisesDto> any = appGrpPremisesDtoList.stream()
                    .filter(appGrpPremisesDto -> appSvcRelatedInfoDtoList.stream()
                            .anyMatch(dto -> Objects.equals(dto.getAlignPremisesId(), appGrpPremisesDto.getId())
                                    && Objects.equals(dto.getAppNo(), appNo)))
                    .findAny();
            if (any.isPresent()) {
                addPremisesSuccess = true;
                addAppGrpPremisesDto(any.get(), newPremisesDtos);
            }
        }
        if (!addPremisesSuccess) {
            AppGrpPremisesDto actualPrem = appCommService.getActivePremisesByAppNo(appNo);
            if (actualPrem != null) {
                String actualId = actualPrem.getId();
                Optional<AppGrpPremisesDto> any = appGrpPremisesDtoList.stream()
                        .filter(appGrpPremisesDto -> Objects.equals(appGrpPremisesDto.getId(), actualId))
                        .findAny();
                if (any.isPresent()) {
                    addPremisesSuccess = true;
                    addAppGrpPremisesDto(any.get(), newPremisesDtos);
                } else {
                    String premKey = ApplicationHelper.getPremisesKey(actualPrem);
                    any = appGrpPremisesDtoList.stream()
                            .filter(appGrpPremisesDto -> Objects.equals(ApplicationHelper.getPremisesKey(appGrpPremisesDto), premKey))
                            .findAny();
                    if (any.isPresent()) {
                        addPremisesSuccess = true;
                        addAppGrpPremisesDto(any.get(), newPremisesDtos);
                    }
                }
            }
            log.info(StringUtil.changeForLog("Add Premises Success --> " + addPremisesSuccess));
        }
    }

    private void addAppGrpPremisesDto(AppGrpPremisesDto appGrpPremisesDto, List<AppGrpPremisesDto> newPremisesDtos) {
        newPremisesDtos.add(appGrpPremisesDto);
    }

    protected void svcRelatedInfoRFI(AppSubmissionDto appSubmissionDto, String appNo) {
        if (!ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            return;
        }
        List<AppPremSpecialisedDto> appPremSpecialisedDtoList = appSubmissionDto.getAppPremSpecialisedDtoList();
        if (appPremSpecialisedDtoList != null && appPremSpecialisedDtoList.size() > 1) {
            appSubmissionDto.setAppPremSpecialisedDtoList(appPremSpecialisedDtoList.stream()
                    .filter(dto -> Objects.equals(appNo, dto.getAppNo()))
                    .collect(Collectors.toList()));
        }

        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfoDtoByServiceId(appSubmissionDto.getAppSvcRelatedInfoDtoList(),
                appSubmissionDto.getRfiServiceId(), appNo);
        if (appSvcRelatedInfoDto == null) {
            return;
        }
        List<AppSvcRelatedInfoDto> result = IaisCommonUtils.genNewArrayList(1);
        result.add(appSvcRelatedInfoDto);
        appSubmissionDto.setAppSvcRelatedInfoDtoList(result);
    }

    protected AppSvcRelatedInfoDto getAppSvcRelatedInfoDtoByServiceId(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,
            String serviceId, String appNo) {
        Optional<AppSvcRelatedInfoDto> optional = appSvcRelatedInfoDtos.stream()
                .filter(dto -> Objects.equals(serviceId, dto.getServiceId())
                        && Objects.equals(appNo, dto.getAppNo()))
                .findAny();
        if (!optional.isPresent()) {
            optional = appSvcRelatedInfoDtos.stream()
                    .filter(dto -> Objects.equals(appNo, dto.getAppNo()))
                    .findAny();
        }
        if (!optional.isPresent()) {
            optional = appSvcRelatedInfoDtos.stream()
                    .filter(dto -> Objects.equals(serviceId, dto.getServiceId()))
                    .findAny();
        }
        return optional.orElseGet(() -> appSvcRelatedInfoDtos.get(0));
    }

    protected void loadingRfiGrpServiceConfig(AppSubmissionDto appSubmissionDto, HttpServletRequest request) {
        if (appSubmissionDto == null || appSubmissionDto.getAppGrpPremisesDtoList() == null) {
            return;
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<String> serviceConfigIds = appSvcRelatedInfoDtos.stream()
                .map(AppSvcRelatedInfoDto::getServiceId).collect(Collectors.toList());
        List<HcsaServiceDto> hcsaServiceDtoList = configCommService.getHcsaServiceDtosByIds(serviceConfigIds);
        ParamUtil.setSessionAttr(request, HcsaAppConst.HCSAS_GRP_SVC_LIST, (Serializable) hcsaServiceDtoList);
        log.info(StringUtil.changeForLog("the group config service size: " + hcsaServiceDtoList.size()));
    }

    protected boolean loadingServiceConfig(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do loadingServiceConfig start ...."));
        //loading the service
        List<String> serviceConfigIds = IaisCommonUtils.genNewArrayList();
        List<String> names = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        if (appSubmissionDto != null) {
            // from draft,rfi
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList) {
                    if (!StringUtil.isEmpty(appSvcRelatedInfoDto.getServiceId())) {
                        serviceConfigIds.add(appSvcRelatedInfoDto.getServiceId());
                    }
                    //if get the data from licence, only have the serviceName
                    if (!StringUtil.isEmpty(appSvcRelatedInfoDto.getServiceName())) {
                        names.add(appSvcRelatedInfoDto.getServiceName());
                    }

                }
            }
        } else {
            List<String> licenceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "licence");
            List<String> baseServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "baseServiceChecked");
            List<String> specifiedServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "specifiedServiceChecked");
            if (IaisCommonUtils.isEmpty(licenceIds)) {
                if (!IaisCommonUtils.isEmpty(baseServiceIds)) {
                    serviceConfigIds.addAll(baseServiceIds);
                }
                if (!IaisCommonUtils.isEmpty(specifiedServiceIds)) {
                    serviceConfigIds.addAll(specifiedServiceIds);
                }
            }
        }

        if (IaisCommonUtils.isEmpty(serviceConfigIds) && IaisCommonUtils.isEmpty(names)) {
            log.info(StringUtil.changeForLog("service id is empty"));
            String errMsg = "you have encountered some problems, please contact the administrator !!!";
            jumpToErrorPage(bpc.request, errMsg);
            return false;
        }

        List<HcsaServiceDto> hcsaServiceDtoList = null;
//        if (!serviceConfigIds.isEmpty()) {
//            hcsaServiceDtoList = configCommService.getHcsaServiceDtosByIds(serviceConfigIds);
//        } else if (!names.isEmpty()) {
//            hcsaServiceDtoList = HcsaServiceCacheHelper.getHcsaSvcsByNames(names);
//        }
        if (ApplicationHelper.checkIsRfi(bpc.request)) {
            if (!serviceConfigIds.isEmpty()) {
                hcsaServiceDtoList = configCommService.getHcsaServiceDtosByIds(serviceConfigIds);
            }
            if (IaisCommonUtils.isEmpty(hcsaServiceDtoList)  && !names.isEmpty()) {
                hcsaServiceDtoList = HcsaServiceCacheHelper.getHcsaSvcsByNames(names);
            }
        } else {
            if (!names.isEmpty()) {
                hcsaServiceDtoList = HcsaServiceCacheHelper.getHcsaSvcsByNames(names);
            }
            if (IaisCommonUtils.isEmpty(hcsaServiceDtoList) && !serviceConfigIds.isEmpty()) {
                hcsaServiceDtoList = configCommService.getHcsaServiceDtosByIds(serviceConfigIds);
            }
        }
        if (hcsaServiceDtoList != null) {
            hcsaServiceDtoList = ApplicationHelper.sortHcsaServiceDto(hcsaServiceDtoList);
        }
        ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
        log.info(StringUtil.changeForLog("the do loadingServiceConfig end ...."));
        return true;
    }

    /**
     * StartStep: Prepare
     *
     * @param bpc
     * @throws
     */
    public void prepare(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepare start ...."));
        //String action = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        String action = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (StringUtil.isEmpty(action)) {
            action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
            if (StringUtil.isEmpty(action) || "validation".equals(action)) {
                //first
                action = HcsaAppConst.ACTION_LICENSEE;
            }
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, action);
        log.info(StringUtil.changeForLog("the do prepare end ...."));
    }

    /**
     * StartStep: ControlSwitch
     *
     * @param bpc
     * @throws
     */
    public void controlSwitch(BaseProcessClass bpc) {
    }

    /**
     * Step: PrepareAction
     *
     * @param bpc
     */
    public void prepareAction(BaseProcessClass bpc) {
        String action = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
        if (StringUtil.isEmpty(action)) {
            action = HcsaAppConst.ACTION_LICENSEE;
        }
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.ACTION, action);
        switch (action) {
            case HcsaAppConst.ACTION_LICENSEE:
                prepareSubLicensee(bpc);
                break;
            case HcsaAppConst.ACTION_PREMISES:
                preparePremises(bpc);
                break;
            case HcsaAppConst.ACTION_SPECIALISED:
                prepareSpecialisedData(bpc);
                break;
        }
    }

    /**
     * Step: DoAction
     *
     * @param bpc
     */
    public void doAction(BaseProcessClass bpc) {
        String crudActionAdditional = bpc.request.getParameter("crud_action_additional");
        String action = (String) ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.ACTION);
        if ("jumpPage".equals(crudActionAdditional) || StringUtil.isEmpty(action)) {
            log.info("Jump Page!!!");
            return;
        }
        switch (action) {
            case HcsaAppConst.ACTION_LICENSEE:
                doSubLicensee(bpc);
                break;
            case HcsaAppConst.ACTION_PREMISES:
                doPremises(bpc);
                break;
            case HcsaAppConst.ACTION_SPECIALISED:
                doSpecialised(bpc);
                break;
            default:
                jumpToErrorPage(bpc.request, "Invalid Action!");
                break;
        }
    }

    public void prepareSpecialisedData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,
                AppServicesConsts.HCSASERVICEDTOLIST);
        String svcCode = ParamUtil.getRequestString(request, HcsaAppConst.SPECIALISED_SVC_CODE);
        if (StringUtil.isEmpty(svcCode)) {
            svcCode = hcsaServiceDtoList.get(0).getSvcCode();
        }
        ParamUtil.setRequestAttr(request, HcsaAppConst.SPECIALISED_SVC_CODE, svcCode);
        ParamUtil.setRequestAttr(request, HcsaAppConst.SPECIALISED_NEXT_CODE, getNextSvcCode(hcsaServiceDtoList, svcCode));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        DealSessionUtil.initAppPremSpecialisedDtoList(appSubmissionDto, hcsaServiceDtoList, false);
        ApplicationHelper.setAppSubmissionDto(appSubmissionDto, request);
    }

    private String getNextSvcCode(List<HcsaServiceDto> hcsaServiceDtoList, String svcCode) {
        int size = hcsaServiceDtoList.size();
        int i = 0;
        for (; i < size; i++) {
            if (Objects.equals(svcCode, hcsaServiceDtoList.get(i).getSvcCode())) {
                i++;
                break;
            }
        }
        if (i >= size) {
            return null;
        }
        return hcsaServiceDtoList.get(i).getSvcCode();
    }

    public void doSpecialised(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(RfcConst.EDIT_SPECIALISED, request);
        log.info(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        List<AppPremSpecialisedDto> appPremSpecialisedDtoList = appSubmissionDto.getAppPremSpecialisedDtoList();
        String svcCode = ParamUtil.getString(request, HcsaAppConst.SPECIALISED_SVC_CODE);
        log.info(StringUtil.changeForLog("Svc Code: " + svcCode));
        if (isGetDataFromPage) {
            AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(request);
            List<String> oldSpecialSerices = RfcHelper.getSpecialServiceList(oldAppSubmissionDto != null ? oldAppSubmissionDto :
                    appSubmissionDto);
            AppDataHelper.setSpecialisedData(appPremSpecialisedDtoList, svcCode, request);
            appSubmissionDto.setAppPremSpecialisedDtoList(appPremSpecialisedDtoList);
            // check specialised change
            checkSpecialisedChanged(appSubmissionDto, oldSpecialSerices);
        }
        // validation
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String actionValue = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        if (!StringUtil.isIn(actionValue, new String[]{"saveDraft", ACTION_BACK})) {
            errorMap = AppValidatorHelper.doValidateSpecialisedDtoList(svcCode, appPremSpecialisedDtoList);
        }
        Map<String, String> coMap = appSubmissionDto.getCoMap();
        if (!errorMap.isEmpty()) {
            initErrorAction(HcsaAppConst.ACTION_SPECIALISED, errorMap, appSubmissionDto, request);
            coMap.put(HcsaAppConst.SECTION_SPECIALISED, "");
            ApplicationHelper.setAppSubmissionDto(appSubmissionDto, bpc.request);
        } else {
            coMap.put(HcsaAppConst.SECTION_SPECIALISED, HcsaAppConst.SECTION_SPECIALISED);
            ApplicationHelper.setAppSubmissionDto(appSubmissionDto, bpc.request);
            saveDraft(bpc);
        }
    }

    protected void checkSpecialisedChanged(AppSubmissionDto appSubmissionDto, List<String> oldSpecialSerices) {
        List<String> specialServiceList = RfcHelper.getSpecialServiceList(appSubmissionDto);
        boolean changed = !IaisCommonUtils.isSame(specialServiceList, oldSpecialSerices);
        log.info(StringUtil.changeForLog("App Specialised Changed: " + changed));
        if (changed) {
            DealSessionUtil.reSetInit(appSubmissionDto, HcsaAppConst.SECTION_SPECIALISED);
        }
    }

    /**
     * Prepare licensee detail
     *
     * @param bpc
     */
    public void prepareSubLicensee(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("..... Prepare Sub Licensee...."));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        // init sub licensee
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String licenseeId = loginContext.getLicenseeId();
        if (StringUtil.isEmpty(licenseeId)) {
            licenseeId = appSubmissionDto.getLicenseeId();
        }
        SubLicenseeDto subLicenseeDto = appSubmissionDto.getSubLicenseeDto();
        if (subLicenseeDto == null) {
            subLicenseeDto = new SubLicenseeDto();
        }
        SubLicenseeDto orgLicensee = organizationService.getSubLicenseeByLicenseeId(licenseeId);
        orgLicensee.setClaimUenNo(subLicenseeDto.getClaimUenNo());
        orgLicensee.setClaimCompanyName(subLicenseeDto.getClaimCompanyName());
        if (OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY.equals(subLicenseeDto.getLicenseeType())
                || OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(orgLicensee.getLicenseeType())) {
            subLicenseeDto = CopyUtil.copyMutableObject(orgLicensee);
        }
        // init option, map and some fields
        if (!OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(subLicenseeDto.getLicenseeType())) {
            Map<String, SubLicenseeDto> licenseeMap =
                    (Map<String, SubLicenseeDto>) bpc.request.getSession().getAttribute(LICENSEE_MAP);
            if (licenseeMap == null) {
                List<SubLicenseeDto> subLicenseeDtoList = licCommService.getIndividualSubLicensees(orgLicensee.getOrgId());
                licenseeMap = ApplicationHelper.genSubLicessMap(subLicenseeDtoList);
                bpc.request.getSession().setAttribute(LICENSEE_MAP, licenseeMap);
            }
            bpc.request.setAttribute(LICENSEE_OPTIONS, ApplicationHelper.genSubLicessOption(licenseeMap));
            String assignSelect = subLicenseeDto.getAssignSelect();
            if ((StringUtil.isEmpty(assignSelect) || IaisEGPConstant.ASSIGN_SELECT_ADD_NEW.equals(assignSelect))
                    && OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL.equals(subLicenseeDto.getLicenseeType())) {
                String assigned = ApplicationHelper.getAssignSelect(licenseeMap.keySet(), subLicenseeDto.getNationality(),
                        subLicenseeDto.getIdType(), subLicenseeDto.getIdNumber());
                if (StringUtil.isEmpty(assignSelect) || !"-1".equals(assigned)
                        && !IaisEGPConstant.ASSIGN_SELECT_ADD_NEW.equals(assigned)) {
                    subLicenseeDto.setAssignSelect(assigned);
                }
            }
        }
        if (StringUtil.isEmpty(subLicenseeDto.getUenNo())) {
            subLicenseeDto.setUenNo(orgLicensee.getUenNo());
            subLicenseeDto.setOrgId(orgLicensee.getOrgId());
        }
        appSubmissionDto.setSubLicenseeDto(subLicenseeDto);
        ApplicationHelper.setAppSubmissionDto(appSubmissionDto, bpc.request);
        ParamUtil.setRequestAttr(bpc.request, "subLicenseeDto", orgLicensee);
        // check data for nav tab
        boolean onlyNextTab = IaisCommonUtils.isEmpty(appSubmissionDto.getAppGrpPremisesDtoList())
                || appSubmissionDto.getAppGrpPremisesDtoList().stream().anyMatch(dto -> StringUtil.isEmpty(dto.getPremisesType()))
                || IaisCommonUtils.isEmpty(appSubmissionDto.getAppPremSpecialisedDtoList())
                || appSubmissionDto.getAppPremSpecialisedDtoList().stream().anyMatch(dto -> StringUtil.isEmpty(dto.getPremisesType()));
        log.info(StringUtil.changeForLog("-----------onlyNextTab: " + onlyNextTab + "------------"));
        ParamUtil.setRequestAttr(bpc.request, "onlyNextTab", onlyNextTab);
    }

    /**
     * Process: MohNewApplication
     * Step: PrepareSubLicensee
     * <p>
     * Do licensee detail
     *
     * @param bpc
     */
    public void doSubLicensee(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("------doSubLicensee-------"));
        String action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        if (ACTION_BACK.equals(action) || RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "jump");
            return;
        }
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String isEdit = ParamUtil.getString(bpc.request, IS_EDIT);
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(appSubmissionDto,
                RfcConst.EDIT_LICENSEE, isEdit, isRfi);
        log.info(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        SubLicenseeDto subLicenseeDto = appSubmissionDto.getSubLicenseeDto();
        if (isGetDataFromPage) {
            subLicenseeDto = getSubLicenseeDtoFromPage(bpc.request);
            appSubmissionDto.setSubLicenseeDto(subLicenseeDto);
            if (appSubmissionDto.isNeedEditController()) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                clickEditPages.add(HcsaAppConst.APP_PAGE_NAME_LICENSEE);
                appSubmissionDto.setClickEditPage(clickEditPages);
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                appEditSelectDto.setLicenseeEdit(ApplicationHelper.canLicenseeEdit(appSubmissionDto, isRfi));
            }
        }
        // valiation
        String actionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        if (!StringUtil.isIn(actionValue, new String[]{"saveDraft", ACTION_BACK})) {
            AppValidatorHelper.validateSubLicenseeDto(errorMap, subLicenseeDto);
        }
        Map<String, String> coMap = appSubmissionDto.getCoMap();
        if (!errorMap.isEmpty()) {
            initErrorAction(HcsaAppConst.ACTION_LICENSEE, errorMap, appSubmissionDto, bpc.request);
            coMap.put(HcsaAppConst.SECTION_LICENSEE, "");
            ApplicationHelper.setAppSubmissionDto(appSubmissionDto, bpc.request);
        } else {
            coMap.put(HcsaAppConst.SECTION_LICENSEE, HcsaAppConst.SECTION_LICENSEE);
            ApplicationHelper.setAppSubmissionDto(appSubmissionDto, bpc.request);
            saveDraft(bpc);
        }
    }

    private SubLicenseeDto getSubLicenseeDtoFromPage(HttpServletRequest request) {
        String licenseeType = ParamUtil.getString(request, "licenseeType");
        SubLicenseeDto dto;
        // Check licensee type
        if (OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY.equals(licenseeType)
                || OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(licenseeType)) {
            dto = organizationService.getSubLicenseeByLicenseeId(ApplicationHelper.getLicenseeId(request));
            if (dto == null) {
                dto = new SubLicenseeDto();
            }
            dto.setLicenseeType(licenseeType);
        } else {
            String assignSelect = ParamUtil.getString(request, "assignSelect");
            dto = AppDataHelper.getSubLicenseeDtoDetailFromPage(request);
            AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
            if (!ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                SubLicenseeDto old = appSubmissionDto.getSubLicenseeDto();
                if (old != null) {
                    // RFC/Renew can't edit these fields
                    dto.setIdType(old.getIdType());
                    dto.setIdNumber(StringUtil.toUpperCase(old.getIdNumber()));
                    dto.setLicenseeName(old.getLicenseeName());
                    dto.setLicenseeType(old.getLicenseeType());
                    dto.setNationality(old.getNationality());
                }
                if (OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL.equals(dto.getLicenseeType())) {
                    dto.setAssignSelect(ApplicationHelper.getPersonKey(dto.getNationality(), dto.getIdType(), dto.getIdNumber()));
                } else {
                    dto.setAssignSelect(HcsaAppConst.NEW_PSN);
                }
            } else {
                dto.setAssignSelect(assignSelect);
                dto.setLicenseeType(licenseeType);
            }
            if (OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL.equals(dto.getLicenseeType())) {
                Map<String, SubLicenseeDto> licenseeMap = (Map<String, SubLicenseeDto>) request.getSession().getAttribute(
                        LICENSEE_MAP);
                Optional<SubLicenseeDto> optional = Optional.ofNullable(licenseeMap).map(map -> map.get(assignSelect));
                if (optional.isPresent()) {
                    if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                        MiscUtil.transferEntityDto(optional.get(), SubLicenseeDto.class, null, dto);
                        dto.setAssignSelect(assignSelect);
                        dto.setLicenseeType(OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL);
                    } else {
                        SubLicenseeDto selectedDto = optional.get();
                        dto.setIdType(selectedDto.getIdType());
                        dto.setIdNumber(StringUtil.toUpperCase(selectedDto.getIdNumber()));
                        dto.setLicenseeName(selectedDto.getLicenseeName());
                    }
                }
            }
            if (ApplicationHelper.isFrontend()) {
                LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
                if (loginContext != null) {
                    dto.setOrgId(loginContext.getOrgId());
                    dto.setUenNo(loginContext.getUenNo());
                }
            } else {
                SubLicenseeDto old = appSubmissionDto.getSubLicenseeDto();
                if (old != null) {
                    dto.setOrgId(old.getOrgId());
                    dto.setUenNo(old.getUenNo());
                }
            }
        }
        if (StringUtil.isEmpty(licenseeType)) {
            licenseeType = dto.getLicenseeType();
        }
        if (OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL.equals(licenseeType)
                || OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(licenseeType)) {
            String claimUenNo = ParamUtil.getString(request, "claimUenNo");
            String claimCompanyName = ParamUtil.getString(request, "claimCompanyName");
            dto.setClaimUenNo(claimUenNo);
            dto.setClaimCompanyName(claimCompanyName);
        }

        return dto;
    }

    /**
     * StartStep: PreparePremises
     *
     * @param bpc
     * @throws
     */
    public void preparePremises(BaseProcessClass bpc) {
        // Object attribute1 = bpc.request.getAttribute(RfcConst.SWITCH);
        log.info(StringUtil.changeForLog("the do preparePremises start ...."));
        ApplicationHelper.setTimeList(bpc.request);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String appType = appSubmissionDto.getAppType();
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        //rfc/renew
        boolean isRfcRenewal = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)
                || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType);
        if (isRfcRenewal && !isRfi) {
            setSelectLicence(appSubmissionDto);
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        //premise select select options
        Map<String, AppGrpPremisesDto> premisesMap = ApplicationHelper.setPremSelect(bpc.request);
        //get svcCode to get svcId
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,
                AppServicesConsts.HCSASERVICEDTOLIST);
        // check premises list
        List<AppGrpPremisesDto> removeList = new ArrayList<>();
        Set<String> premisesType = DealSessionUtil.initPremiseTypes(hcsaServiceDtoList, false, bpc.request);
        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
            if (!StringUtil.isEmpty(appGrpPremisesDto.getPremisesType())
                    && !premisesType.contains(appGrpPremisesDto.getPremisesType())) {
                // configuration changed
                removeList.add(appGrpPremisesDto);
                continue;
            }
            String premisesSelect = appGrpPremisesDto.getPremisesSelect();
            if (!StringUtil.isEmpty(premisesSelect) && !HcsaAppConst.DFT_FIRST_CODE.equals(premisesSelect)
                    && !HcsaAppConst.NEW_PREMISES.equals(premisesSelect) ) {
                // re-set premise select for error record
                if (premisesMap.get(premisesSelect) == null) {
                    appGrpPremisesDto.setExistingData(AppConsts.NO);
                    appGrpPremisesDto.setPremisesSelect(HcsaAppConst.NEW_PREMISES);
                }
            }
        }
        if (removeList.size() > 0) {
            appGrpPremisesDtoList.removeAll(removeList);
        }
        appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);

        int baseSvcCount = 0;
        boolean hasMs = true;//TODO test
        if (hcsaServiceDtoList != null) {
            for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtoList) {
                if (HcsaConsts.SERVICE_TYPE_BASE.equalsIgnoreCase(hcsaServiceDto.getSvcType())) {
                    baseSvcCount++;
                }
                if (AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE.equals(hcsaServiceDto.getSvcCode())) {
                    hasMs = true;
                }
            }
        }
        if (baseSvcCount > 1) {
            ParamUtil.setRequestAttr(bpc.request, "multiBase", AppConsts.TRUE);
        } else {
            ParamUtil.setRequestAttr(bpc.request, "multiBase", AppConsts.FALSE);
        }
        ParamUtil.setRequestAttr(bpc.request, "isMultiPremService", ApplicationHelper.isMultiPremService(hcsaServiceDtoList));
        /**
         * 14. For Medical Service, “Remote Delivery” and “Mobile Delivery” sections will be displayed if Applicant selects either
         * “Permanent Premises” or “Conveyance”
         */
        boolean isNew = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType);
        if (isNew && !isRfi && baseSvcCount == 1 && hasMs) {
            ParamUtil.setRequestAttr(bpc.request, "autoCheckRandM", AppConsts.YES);
        }

        ApplicationHelper.setAppSubmissionDto(appSubmissionDto, bpc.request);
        log.info(StringUtil.changeForLog("the do preparePremises end ...."));
    }

    private void setSelectLicence(AppSubmissionDto appSubmissionDto) {
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        String licenceNo = appSubmissionDto.getLicenceNo();
        String licenseeId = appSubmissionDto.getLicenseeId();
        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
            List<LicenceDto> licenceDtoByHciCode = licCommService.getLicenceDtoByHciCode(licenseeId,
                    appGrpPremisesDto, licenceNo);
            appGrpPremisesDto.setLicenceDtos(licenceDtoByHciCode);
        }
    }

    /**
     * StartStep: DoPremises
     *
     * @param bpc
     * @throws
     */
    public void doPremises(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doPremises start ...."));
        String crud_action_value = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        log.info(StringUtil.changeForLog("##### Action Value: " + crud_action_value));
        if ("undo".equals(crud_action_value)) {
            // Undo All Changes
            DealSessionUtil.clearPremisesMap(bpc.request);
            return;
        }
        //gen dto
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);
        String isEdit = ParamUtil.getString(bpc.request, IS_EDIT);
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(appSubmissionDto, RfcConst.EDIT_PREMISES, isEdit, isRfi);
        log.info(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        if (isGetDataFromPage) {
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList;
            if (oldAppSubmissionDto == null) {
                oldAppGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            } else {
                oldAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
            }
            List<AppGrpPremisesDto> appGrpPremisesDtoList = AppDataHelper.genAppGrpPremisesDtoList(bpc.request);
            appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
            if (appSubmissionDto.isNeedEditController()) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                clickEditPages.add(HcsaAppConst.APP_PAGE_NAME_PREMISES);
                appSubmissionDto.setClickEditPage(clickEditPages);
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                appEditSelectDto.setPremisesEdit(true);
                appSubmissionDto.setChangeSelectDto(appEditSelectDto);
            }
            //if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            //65718
            //ApplicationHelper.removePremiseEmptyAlignInfo(appSubmissionDto);
            //ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
            //}
            //update address
            //ApplicationHelper.updatePremisesAddress(appSubmissionDto);
            //ApplicationHelper.setAppSubmissionDto(appSubmissionDto, bpc.request);
            // check app premises change
            checkAppPremisesChanged(appSubmissionDto, oldAppGrpPremisesDtoList);
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crud_action_additional = ParamUtil.getString(bpc.request, "crud_action_additional");
        if (!"saveDraft".equals(crud_action_value)) {
            String actionType = bpc.request.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
            bpc.request.setAttribute("continueStep", actionType);
            bpc.request.setAttribute("crudActionTypeContinue", crud_action_additional);
            // validation
            //AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);
            List<String> premisesHciList = getPremisesHciList(appSubmissionDto.getLicenseeId(), isRfi, oldAppSubmissionDto,
                    bpc.request);
            errorMap = AppValidatorHelper.doValidatePremises(appSubmissionDto, premisesHciList, isRfi, true);
            String crud_action_type_continue = bpc.request.getParameter("crud_action_type_continue");
            if ("continue".equals(crud_action_type_continue)) {
                errorMap.remove("hciNameUsed");
            }
            String hciNameUsed = errorMap.get("hciNameUsed");
            if (errorMap.size() == 1 && hciNameUsed != null) {
                ParamUtil.setRequestAttr(bpc.request, "hciNameUsed", "hciNameUsed");
                ParamUtil.setRequestAttr(bpc.request, "newAppPopUpMsg", hciNameUsed);
            }
        }
        // check result
        Map<String, String> coMap = appSubmissionDto.getCoMap();
        if (errorMap.size() > 0) {
            boolean isNeedShowValidation = !ACTION_BACK.equals(crud_action_value);
            if (isNeedShowValidation) {
                initErrorAction(HcsaAppConst.ACTION_PREMISES, errorMap, appSubmissionDto, bpc.request);
            }
            coMap.put(HcsaAppConst.SECTION_PREMISES, "");
            ApplicationHelper.setAppSubmissionDto(appSubmissionDto, bpc.request);
        } else {
            coMap.put(HcsaAppConst.SECTION_PREMISES, HcsaAppConst.SECTION_PREMISES);
            ApplicationHelper.setAppSubmissionDto(appSubmissionDto, bpc.request);
            saveDraft(bpc);
        }
        log.info(StringUtil.changeForLog("the do doPremises end ...."));
    }

    protected void checkAppPremisesChanged(AppSubmissionDto appSubmissionDto, List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) {
        boolean changed = RfcHelper.isChangeAppPremisesAddress(appSubmissionDto.getAppGrpPremisesDtoList(),
                oldAppGrpPremisesDtoList);
        log.info(StringUtil.changeForLog("App Premises Changed: " + changed));
        if (changed) {
            DealSessionUtil.reSetInit(appSubmissionDto, HcsaAppConst.SECTION_PREMISES);
        }
    }

    private List<String> getPremisesHciList(String licenseeId, boolean isRfi, AppSubmissionDto oldAppSubmissionDto,
            HttpServletRequest request) {
        List<String> premisesHciList = (List<String>) ParamUtil.getSessionAttr(request, HcsaAppConst.PREMISES_HCI_LIST);
        if (premisesHciList != null) {
            return premisesHciList;
        }
        // if current is one of group new rfi, the premises will be only one, we need to check all apps in this group
        List<HcsaServiceDto> hcsaServiceDtos = null;
        if (isRfi) {
            // init: this#loadingRfiGrpServiceConfig
            hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request, HcsaAppConst.HCSAS_GRP_SVC_LIST);
        }
        if (hcsaServiceDtos == null) {
            hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request, AppServicesConsts.HCSASERVICEDTOLIST);
        }
        List<PremisesDto> excludePremisesList = null;
        List<AppGrpPremisesDto> excludeAppPremList = null;
        if (oldAppSubmissionDto != null) {
            if (isRfi) {
                excludePremisesList = licCommService.getPremisesListByLicenceId(oldAppSubmissionDto.getLicenceId());
            }
            excludeAppPremList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        }
        premisesHciList = appCommService.getHciFromPendAppAndLic(licenseeId, hcsaServiceDtos,
                excludePremisesList, excludeAppPremList);
        ParamUtil.setSessionAttr(request, HcsaAppConst.PREMISES_HCI_LIST, (Serializable) premisesHciList);
        return premisesHciList;
    }

    /**
     * StartStep: PrepareForms
     *
     * @param bpc
     * @throws
     */
    public void prepareForms(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareForms start ...."));
        //Object attribute = bpc.request.getAttribute(RfcConst.SWITCH);
        log.info(StringUtil.changeForLog("the do prepareForms end ...."));
    }

    /**
     * StartStep: PreparePreview
     *
     * @param bpc
     * @throws
     */
    public void preparePreview(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do preparePreview start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        DealSessionUtil.initView(appSubmissionDto);
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,
                AppServicesConsts.HCSASERVICEDTOLIST);
        //todo:wait task complete remove this
        boolean ableGrpLic = true;
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                if (!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo()) || !StringUtil.isEmpty(
                        appSvcRelatedInfoDto.getAlignLicenceNo())) {
                    ableGrpLic = false;
                    break;
                }
            }
        }

        if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos) && !IaisCommonUtils.isEmpty(hcsaServiceDtos) && ableGrpLic) {
            int premCount = appGrpPremisesDtos.size();
            int svcCount = hcsaServiceDtos.size();
            log.info(StringUtil.changeForLog("premises count:" + premCount + " ,service count:" + svcCount));
            if (premCount > 1 && svcCount >= 1) {
                //multi prem one svc
                ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.GROUPLICENCECONFIG, "test");
            }
        }

        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
            if (!ApplicationHelper.checkIsRfi(bpc.request)) {
                // 113164
                AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);
                List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
                List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
                if (oldAppGrpPremisesDtoList != null && appGrpPremisesDtoList != null) {
                    for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
                        boolean eqHciNameChange = RfcHelper.eqHciNameChange(appGrpPremisesDtoList.get(i),
                                oldAppGrpPremisesDtoList.get(i));
                        if (eqHciNameChange) {
                            bpc.request.setAttribute("RFC_eqHciNameChange", "RFC_eqHciNameChange");
                        }
                    }
                }
            } else {
                AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionDto.getAppDeclarationMessageDto();
                if (appSubmissionDto.getAppGrpNo() != null && appSubmissionDto.getAppGrpNo().startsWith("AR")) {
                    if (appDeclarationMessageDto != null) {
                        RenewDto renewDto = new RenewDto();
                        List<AppSubmissionDto> appSubmissionDtos = new ArrayList<>(1);
                        AppSubmissionDto renewAppsub = new AppSubmissionDto();
                        renewAppsub.setAppDeclarationMessageDto(appDeclarationMessageDto);
                        renewAppsub.setAppDeclarationDocDtos(appSubmissionDto.getAppDeclarationDocDtos());
                        renewAppsub.setAppType(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
                        appSubmissionDtos.add(renewAppsub);
                        renewDto.setAppSubmissionDtos(appSubmissionDtos);
                        bpc.request.setAttribute("renewDto", renewDto);
                        bpc.request.setAttribute("renew_rfc_show", "Y");
                    }
                } else {
                    if (appDeclarationMessageDto != null) {
                        bpc.request.setAttribute("RFC_eqHciNameChange", "RFC_eqHciNameChange");
                    }
                }
            }
        } else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
            if (ApplicationHelper.checkIsRfi(bpc.request)) {
                AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionDto.getAppDeclarationMessageDto();
                if (appDeclarationMessageDto != null) {
                    RenewDto renewDto = new RenewDto();
                    List<AppSubmissionDto> list = new ArrayList<>(1);
                    list.add(appSubmissionDto);
                    renewDto.setAppSubmissionDtos(list);
                    bpc.request.setAttribute("renewDto", renewDto);
                }
            }
        }
        // init uploaded File
//        AppDataHelper.initDeclarationFiles(appSubmissionDto.getAppDeclarationDocDtos(), appSubmissionDto.getAppType(), bpc.request);
        if (ApplicationHelper.checkIsRfi(bpc.request)) {
            ParamUtil.setSessionAttr(bpc.request, "viewPrint", "Y");
        } else {
            ParamUtil.setSessionAttr(bpc.request, "viewPrint", null);
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);

        ParamUtil.setRequestAttr(bpc.request, "isCharity", ApplicationHelper.isCharity(bpc.request));

        log.info(StringUtil.changeForLog("the do preparePreview end ...."));
    }

    /**
     * StartStep: PreparePayment
     *
     * @param bpc
     * @throws
     */
    public void preparePayment(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do preparePayment start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) bpc.request.getSession().getAttribute(APP_SUBMISSIONS);
        String paymentMethod;
        //get transfer info
        AppSubmissionDto tranferSub = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "app-rfc-tranfer");
        if (tranferSub != null) {
            if (appSubmissionDtos == null) {
                appSubmissionDtos = new ArrayList<>(1);
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = tranferSub.getAppSvcRelatedInfoDtoList();
                if (appSvcRelatedInfoDtoList != null) {
                    for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList) {
                        appSvcRelatedInfoDto.setGroupNo(tranferSub.getAppGrpNo());
                    }
                }
                appSubmissionDtos.add(tranferSub);
            }
            String transferFlag = appSubmissionDto.getTransferFlag();
            appSubmissionDto = tranferSub;
            appSubmissionDto.setTransferFlag(transferFlag);
            //reload transfer payment method
            paymentMethod = tranferSub.getPaymentMethod();
        } else {
            //reload new/rfc payment method
            paymentMethod = appSubmissionDto.getPaymentMethod();
        }
        Double total = 0.0;
        if (!ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(
                appSubmissionDto.getAppType()) && !ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                appSubmissionDto.getAppType())) {
            total += appSubmissionDto.getAmount();
        }
        if (appSubmissionDtos != null && !appSubmissionDtos.isEmpty()) {
            for (AppSubmissionDto appSubmissionDto1 : appSubmissionDtos) {
                Double amount = appSubmissionDto1.getAmount();
                if (amount != null) {
                    total = total + amount;
                }
                String amountStr = Formatter.formatterMoney(appSubmissionDto1.getAmount());
                appSubmissionDto1.setAmountStr(amountStr);
                appSubmissionDto1.setServiceName(appSubmissionDto1.getAppSvcRelatedInfoDtoList().get(0).getServiceName());
            }
        }
        appSubmissionDto.setAmount(total);
        if (!StringUtil.isEmpty(appSubmissionDto.getAmount())) {
            String amountStr = Formatter.formatterMoney(appSubmissionDto.getAmount());
            log.info(StringUtil.changeForLog("The amountStr is -->:" + amountStr));
            appSubmissionDto.setAmountStr(amountStr);
        }
        if (appSubmissionDtos != null) {
            bpc.request.getSession().setAttribute(APP_SUBMISSIONS, appSubmissionDtos);
        }
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            Set<String> premTypes = IaisCommonUtils.genNewHashSet();
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                    premTypes.add(appGrpPremisesDto.getPremisesType());
                }
            }
        }
        String flag = bpc.request.getParameter("flag");
        if (!StringUtil.isEmpty(flag)) {
            appSubmissionDto.setTransferFlag(flag);
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        bpc.request.setAttribute("flag", appSubmissionDto.getTransferFlag());
        bpc.request.setAttribute("transfer", appSubmissionDto.getTransferFlag());
        ParamUtil.setRequestAttr(bpc.request, "IsCharity", ApplicationHelper.isCharity(bpc.request));
        boolean isGiroAcc = false;
        List<SelectOption> giroAccSel = ApplicationHelper.getGiroAccOptions(appSubmissionDtos, appSubmissionDto);
        if (!IaisCommonUtils.isEmpty(giroAccSel)) {
            isGiroAcc = true;
            ParamUtil.setRequestAttr(bpc.request, "giroAccSel", giroAccSel);
        }
        ParamUtil.setRequestAttr(bpc.request, "IsGiroAcc", isGiroAcc);
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.ATTR_RELOAD_PAYMENT_METHOD, paymentMethod);
        log.info(StringUtil.changeForLog("the do preparePayment end ...."));
    }

    /**
     * StartStep: doForms
     *
     * @param bpc
     * @throws
     */
    public void doForms(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doForms start ...."));
        log.info(StringUtil.changeForLog("the do doForms end ...."));
    }

    /**
     * StartStep: doPreview
     *
     * @param bpc
     * @throws
     */
    public void doPreview(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doPreview start ...."));
        String action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String appType = appSubmissionDto.getAppType();
        String isGroupLic = ParamUtil.getString(bpc.request, "isGroupLic");
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        boolean needNewDeclaration = false;
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
            appSubmissionDto.setGroupLic(!StringUtil.isEmpty(isGroupLic) && AppConsts.YES.equals(isGroupLic));
            needNewDeclaration = !isRfi;
        } else if (!isRfi && ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)) {
            AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            if (oldAppGrpPremisesDtoList != null && appGrpPremisesDtoList != null) {
                for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
                    boolean eqHciNameChange = RfcHelper.eqHciNameChange(appGrpPremisesDtoList.get(i),
                            oldAppGrpPremisesDtoList.get(i));
                    if (eqHciNameChange) {
                        needNewDeclaration = true;
                        break;
                    }
                }
            }
            String verifyInfoCheckbox = ParamUtil.getString(bpc.request, "verifyInfoCheckbox");
            appSubmissionDto.setUserAgreement(AppConsts.YES.equals(verifyInfoCheckbox));
        }
        if (needNewDeclaration) {
            // declaration
            appSubmissionDto.setAppDeclarationMessageDto(
                    AppDataHelper.getAppDeclarationMessageDto(bpc.request, appSubmissionDto.getAppType()));
            DeclarationsUtil.declarationsValidate(errorMap, appSubmissionDto.getAppDeclarationMessageDto(),
                    appSubmissionDto.getAppType());
            // uploaded files
            appSubmissionDto.setAppDeclarationDocDtos(AppDataHelper.getDeclarationFiles(appSubmissionDto.getAppType(), bpc.request));
            String preQuesKindly = appSubmissionDto.getAppDeclarationMessageDto().getPreliminaryQuestionKindly();
            // validation
            AppValidatorHelper.validateDeclarationDoc(errorMap, ApplicationHelper.getFileAppendId(appSubmissionDto.getAppType()),
                    "0".equals(preQuesKindly), bpc.request);
        }

        if (!isRfi && ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
            String effectiveDateStr = ParamUtil.getString(bpc.request, "rfcEffectiveDate");
            appSubmissionDto.setEffectiveDateStr(effectiveDateStr);
            if (!StringUtil.isEmpty(effectiveDateStr)) {
                if (AppValidatorHelper.validateEffectiveDate("rfcEffectiveDate", effectiveDateStr, errorMap)) {
                    Date effDate = DateUtil.parseDate(effectiveDateStr, Formatter.DATE);
                    appSubmissionDto.setEffectiveDate(effDate);
                }
            } else {
                appSubmissionDto.setEffectiveDate(null);
            }
            effectiveDateStr = ParamUtil.getString(bpc.request, "effectiveDt");
            if (!StringUtil.isEmpty(effectiveDateStr)) {
                AppValidatorHelper.validateEffectiveDate("effectiveDt", effectiveDateStr, errorMap);
            }
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        if ("doSubmit".equals(action) && !errorMap.isEmpty()) {
            initErrorAction(ACTION_PREVIEW, errorMap, appSubmissionDto, bpc.request);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, "test");
        }
        log.info(StringUtil.changeForLog("the do doPreview end ...."));
    }


    /**
     * StartStep: doPreview
     *
     * @param bpc
     * @throws
     */
    public void doPayment(BaseProcessClass bpc) {}

    /**
     * StartStep: preInvoke
     *
     * @param bpc
     * @throws
     */
    public void preInvoke(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the do preInvoke start ...."));
        String action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (!StringUtil.isEmpty(action)) {
            switch (action) {
                case "MohAppPremSelfDecl":
//                ParamUtil.setSessionAttr(bpc.request, AppCommConst.SESSION_PARAM_APPLICATION_GROUP_ID, appSubmissionDto.getAppGrpId());
//                ParamUtil.setSessionAttr(bpc.request,AppCommConst.SESSION_SELF_DECL_ACTION,"new");
                    break;
                case "DashBoard": {
                    String tokenUrl = RedirectUtil.appendCsrfGuardToken(
                            "https://" + bpc.request.getServerName() + "/main-web/eservice/INTERNET/MohInternetInbox", bpc.request);
                    IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
                    break;
                }
                case "ChooseSvc": {
                    String url = "https://" + bpc.request.getServerName() +
                            "/hcsa-licence-web/eservice/INTERNET/MohServiceFeMenu";
                    String tokenUrl = RedirectUtil.appendCsrfGuardToken(url, bpc.request);
                    IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
                    break;
                }
                default:
                    break;
            }
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, action);
        }

        log.info(StringUtil.changeForLog("the do preInvoke start ...."));
    }

    private void saveDraft(BaseProcessClass bpc) {
        String actionAdditional = ParamUtil.getString(bpc.request, "crud_action_additional");
        if ("rfcSaveDraft".equals(actionAdditional)) {
            try {
                doSaveDraft(bpc);
            } catch (IOException e) {
                log.error("error", e);
            }
        }
    }

    /**
     * StartStep: doSaveDraft
     *
     * @param bpc
     * @throws
     */
    public void doSaveDraft(BaseProcessClass bpc) throws IOException {}

    public void jumpYeMian(HttpServletRequest request, HttpServletResponse response) throws IOException {}

    public void inboxToPreview(BaseProcessClass bpc) throws Exception {}

    private List<AppSvcRelatedInfoDto> getOtherAppSvcRelatedInfoDtos(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,
            String serviceId, String appNo) {
        return appSvcRelatedInfoDtos.stream()
                .filter(dto -> Objects.equals(serviceId, dto.getServiceId())
                        && !Objects.equals(appNo, dto.getAppNo()))
                .collect(Collectors.toList());
    }

    /**
     * StartStep: doReDquestInformationSubmit
     * prepare
     *
     * @param bpc
     * @throws
     */
    public void doRequestInformationSubmit(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("the do doRequestInformationSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        String appNo = appSubmissionDto.getRfiAppNo();
        Map<String, String> checkMap = checkNextStatusOnRfi(appSubmissionDto.getAppGrpNo(), appNo);
        String appError = checkMap.get(HcsaAppConst.ERROR_APP);
        if (!StringUtil.isEmpty(appError)) {
            initErrorAction(ACTION_PREVIEW, null, appSubmissionDto, bpc.request);
            ParamUtil.setRequestAttr(bpc.request, COND_TYPE_RFI, "N");
            ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.ERROR_APP, appError);
            return;
        }
        AppSubmissionRequestInformationDto appSubmissionRequestInformationDto = new AppSubmissionRequestInformationDto();
        appSubmissionRequestInformationDto.setAppGrpStatus(checkMap.get(HcsaAppConst.STATUS_GRP));
        appSubmissionRequestInformationDto.setRfiStatus(checkMap.get(HcsaAppConst.STATUS_APP));
        Integer maxFileIndex = (Integer) ParamUtil.getSessionAttr(bpc.request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR);
        if (maxFileIndex == null) {
            maxFileIndex = 0;
        }
        appSubmissionDto.setMaxFileIndex(maxFileIndex);
        //oldAppSubmissionDtos
        AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);
        StringBuilder stringBuilder = new StringBuilder(10);
        stringBuilder.append(appSubmissionDto);
        String str = stringBuilder.toString();
        log.info(StringUtil.changeForLog("appSubmissionDto:" + str));
        stringBuilder.setLength(0);
        stringBuilder.append(oldAppSubmissionDto);
        str = stringBuilder.toString();
        log.info(StringUtil.changeForLog("oldAppSubmissionDto:" + str));
        Map<String, String> doComChangeMap = doComChange(appSubmissionDto, oldAppSubmissionDto);
        if (!doComChangeMap.isEmpty()) {
            initErrorAction(ACTION_PREVIEW, doComChangeMap, appSubmissionDto, bpc.request);
            ParamUtil.setRequestAttr(bpc.request, COND_TYPE_RFI, "N");
            return;
        }
        log.info("doComChange is ok ...");
        Map<String, String> map = AppValidatorHelper.doPreviewAndSumbit(bpc);
        if (!map.isEmpty()) {
            initErrorAction(ACTION_PREVIEW, map, appSubmissionDto, bpc.request);
            ParamUtil.setRequestAttr(bpc.request, COND_TYPE_RFI, "N");
            return;
        }
        // check whether it has been withdrew or not
        String appId = getCurrentRfiAppId(oldAppSubmissionDto);
        List<AppPremiseMiscDto> appPremiseMiscs = appCommService.getActiveWithdrawAppPremiseMiscsByApp(appId);
        if (IaisCommonUtils.isNotEmpty(appPremiseMiscs)) {
            // RFI_ERR002: There is a withdrawal for this application.
            ParamUtil.setRequestAttr(bpc.request, "showRfiWithdrawal", AppConsts.YES);
            initErrorAction(ACTION_PREVIEW, null, appSubmissionDto, bpc.request);
            ParamUtil.setRequestAttr(bpc.request, COND_TYPE_RFI, "N");
            return;
        }
        ApplicationHelper.reSetAdditionalFields(appSubmissionDto, oldAppSubmissionDto);
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        oldAppSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)
                || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)) {
            appSubmissionDto.setAppDeclarationDocDtos(oldAppSubmissionDto.getAppDeclarationDocDtos());
            appSubmissionDto.setAppDeclarationMessageDto(oldAppSubmissionDto.getAppDeclarationMessageDto());
        }

        if (ApplicationHelper.isFrontend()) {
            String msgId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
            appSubmissionDto.setRfiMsgId(msgId);
        }
        appSubmissionRequestInformationDto.setAppSubmissionDto(appSubmissionDto);
        appSubmissionRequestInformationDto.setOldAppSubmissionDto(oldAppSubmissionDto);
        appSubmissionRequestInformationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());


        appSubmissionDto = submitRequestInformation(appSubmissionRequestInformationDto, appType);

        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)) {
            List<AppSubmissionDto> appSubmissionDtos = new ArrayList<>(1);
            appSubmissionDto.setAmountStr("N/A");
            appSubmissionDtos.add(appSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request, ACK_APP_SUBMISSIONS, (Serializable) appSubmissionDtos);
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, COND_TYPE_RFI, "Y");
        ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "The request for information save success");
        log.info(StringUtil.changeForLog("the do doRequestInformationSubmit end ...."));
    }

    protected abstract Map<String, String> checkNextStatusOnRfi(String appGrpNo, String appNo);

    protected abstract AppSubmissionDto submitRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto,
            String appType);

    private Map<String, String> doComChange(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto) throws Exception {
        Map<String, String> result = IaisCommonUtils.genNewHashMap();
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();

        if (appEditSelectDto != null) {
            if (!appEditSelectDto.isPremisesEdit()) {
                List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                    appGrpPremisesDto.setLicenceDtos(null);
                }
                boolean eqGrpPremises = RfcHelper.isChangeGrpPremises(appSubmissionDto.getAppGrpPremisesDtoList(),
                        oldAppSubmissionDto.getAppGrpPremisesDtoList());
                if (eqGrpPremises) {
                    log.info(StringUtil.changeForLog(
                            "appGrpPremisesDto" + JsonUtil.parseToJson(appSubmissionDto.getAppGrpPremisesDtoList())));
                    log.info(StringUtil.changeForLog(
                            "oldappGrpPremisesDto" + JsonUtil.parseToJson(oldAppSubmissionDto.getAppGrpPremisesDtoList())));
                    result.put("premiss", MessageUtil.replaceMessage("GENERAL_ERR0006", "premiss", "field"));
                }
            }
            if (!appEditSelectDto.isServiceEdit()) {
                boolean b = RfcHelper.eqServiceChange(appSubmissionDto.getAppSvcRelatedInfoDtoList(),
                        oldAppSubmissionDto.getAppSvcRelatedInfoDtoList());
                if (b) {
                    log.info(StringUtil.changeForLog(
                            "AppSvcRelatedInfoDtoList" + JsonUtil.parseToJson(appSubmissionDto.getAppSvcRelatedInfoDtoList())));
                    log.info(StringUtil.changeForLog(
                            "oldAppSvcRelatedInfoDtoList" + JsonUtil.parseToJson(oldAppSubmissionDto.getAppSvcRelatedInfoDtoList())));
                    result.put("serviceId", MessageUtil.replaceMessage("GENERAL_ERR0006", "serviceId", "field"));
                }
            }
        }
        return result;
    }

    protected void initErrorAction(String action, Map<String, String> errorMap, AppSubmissionDto appSubmissionDto,
            HttpServletRequest request) {
        if (errorMap != null && !errorMap.isEmpty()) {
            boolean isRfi = ApplicationHelper.checkIsRfi(request);
            AppValidatorHelper.setAudiErrMap(isRfi, appSubmissionDto.getAppType(), errorMap, appSubmissionDto.getRfiAppNo(),
                    appSubmissionDto.getLicenceNo());
            ParamUtil.setRequestAttr(request, "Msg", errorMap);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        }
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, action);
        if (StringUtil.isIn(action, new String[]{HcsaAppConst.ACTION_LICENSEE,
                HcsaAppConst.ACTION_PREMISES, HcsaAppConst.ACTION_SPECIALISED})) {
            ParamUtil.setRequestAttr(request, HcsaAppConst.ERROR_KEY, HcsaAppConst.ERROR_VAL);
        }
    }

    private void jumpToErrorPage(HttpServletRequest request, String errorMsg) {
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "errorAck");
        ParamUtil.setRequestAttr(request, ACKSTATUS, HcsaAppConst.ACK_STATUS_ERROR);
        ParamUtil.setRequestAttr(request, ACKMESSAGE, errorMsg);
    }

    private String getCurrentRfiAppId(AppSubmissionDto oldAppSubmissionDto) {
        if (oldAppSubmissionDto == null || IaisCommonUtils.isEmpty(oldAppSubmissionDto.getAppSvcRelatedInfoDtoList())) {
            return null;
        }
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : oldAppSubmissionDto.getAppSvcRelatedInfoDtoList()) {
            if (Objects.equals(oldAppSubmissionDto.getRfiAppNo(), appSvcRelatedInfoDto.getAppNo())) {
                return appSvcRelatedInfoDto.getAppId();
            }
        }
        return null;
    }

    /**
     * StartStep: doRequestForChangeSubmit
     *
     * @param bpc
     */
    public void doRequestForChangeSubmit(BaseProcessClass bpc) throws Exception {
        //validate reject  apst050
        log.info(StringUtil.changeForLog("the do doRequestForChangeSubmit start ...."));
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_PREVIEW);
        ParamUtil.setRequestAttr(bpc.request, COND_TYPE_RFI, "N");
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);
        log.info(StringUtil.changeForLog("The original licence No.: " + appSubmissionDto.getLicenceNo()));

        /*Integer maxFileIndex = (Integer) ParamUtil.getSessionAttr(bpc.request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR);
        if (maxFileIndex == null) {
            maxFileIndex = 0;
        }
        appSubmissionDto.setMaxFileIndex(maxFileIndex);*/
        // validate the submission data
        Map<String, String> map = AppValidatorHelper.doPreviewAndSumbit(bpc);
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        if (map.isEmpty() && !isRfi) {
            boolean changeHciName = false;
            if (oldAppGrpPremisesDtoList != null && appGrpPremisesDtoList != null
                    && !appGrpPremisesDtoList.isEmpty() && !oldAppGrpPremisesDtoList.isEmpty()) {
                changeHciName = RfcHelper.eqHciNameChange(appGrpPremisesDtoList.get(0), oldAppGrpPremisesDtoList.get(0));
            }
            if (changeHciName) {
                AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionDto.getAppDeclarationMessageDto();
                DeclarationsUtil.declarationsValidate(map, appDeclarationMessageDto, appSubmissionDto.getAppType());
                String preQuesKindly = appDeclarationMessageDto == null ? null : appDeclarationMessageDto.getPreliminaryQuestionKindly();
                AppValidatorHelper.validateDeclarationDoc(map, ApplicationHelper.getFileAppendId(appSubmissionDto.getAppType()),
                        "0".equals(preQuesKindly), bpc.request);
            }
        }
        if (!map.isEmpty()) {
            //set audit
            log.warn(StringUtil.changeForLog("Error Message: " + map));
            ParamUtil.setRequestAttr(bpc.request, "Msg", map);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(map));
            return;
        }
        String licenceId = appSubmissionDto.getLicenceId();
        LicenceDto licenceById = licCommService.getActiveLicenceById(licenceId);
        /*
          when use save it as draft in the previous, and the licence has been updated via other licence,
          the licence will not be valid any more, so when use do the it from the old draft,
          the licence will be null.
         */
        if (licenceById == null) {
            log.warn(StringUtil.changeForLog("Invalid selected Licence - " + licenceId));
            bpc.request.setAttribute(RfcConst.INVALID_LIC, MessageUtil.getMessageDesc("RFC_ERR023"));
            return;
        }
        Set<String> premiseTypes = null;
        if (appGrpPremisesDtoList != null) {
            premiseTypes = appGrpPremisesDtoList.stream().map(AppGrpPremisesDto::getPremisesType).collect(Collectors.toSet());
        }
        map = AppValidatorHelper.validateLicences(licenceById, premiseTypes, null);
        if (!map.isEmpty()) {
            AppValidatorHelper.setErrorRequest(map, false, bpc.request);
            return;
        }
        /*String baseServiceId = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getBaseServiceId();
        if (StringUtil.isEmpty(baseServiceId)) {
            bpc.request.setAttribute(RfcConst.SERVICE_CONFIG_CHANGE, MessageUtil.getMessageDesc("RFC_ERR020"));
            return;
        }*/
        // change edit
        AppEditSelectDto appEditSelectDto = RfcHelper.rfcChangeModuleEvaluationDto(appSubmissionDto, oldAppSubmissionDto);
        boolean isAutoRfc = appEditSelectDto.isAutoRfc();
        // reSet: isNeedNewLicNo and self assessment flag
        //ApplicationHelper.reSetAdditionalFields(appSubmissionDto, oldAppSubmissionDto, appEditSelectDto);
        appSubmissionDto.setChangeSelectDto(appEditSelectDto);
        log.info(StringUtil.changeForLog(appSubmissionDto.getLicenceNo() + " - App Edit Select Dto: "
                + JsonUtil.parseToJson(appEditSelectDto)));
        // the declaration only for HCI name changed
        if (!appEditSelectDto.isChangeHciName()) {
            appSubmissionDto.setAppDeclarationMessageDto(null);
            appSubmissionDto.setAppDeclarationDocDtos(null);
        }
        boolean isCharity = ApplicationHelper.isCharity(bpc.request);
        FeeDto feeDto = configCommService.getGroupAmendAmount(getAmendmentFeeDto(appEditSelectDto, isCharity));
        Double amount = feeDto.getTotal();
        double currentAmount = amount == null ? 0.0 : amount;
        if (licenceById.getMigrated() == 1 && IaisEGPHelper.isActiveMigrated()) {
            currentAmount = 0.0;
        }
        log.info(StringUtil.changeForLog("the current amount is -->:" + currentAmount));
        appSubmissionDto.setAmount(currentAmount);

        String appType = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE;
        String appGroupNo = null;
        String autoGroupNo = null;
        String draftNo = Optional.ofNullable(appSubmissionDto.getDraftNo())
                .orElseGet(() -> appCommService.getDraftNo(appType));
        log.info(StringUtil.changeForLog("the draft is -->:" + draftNo));
        if (isAutoRfc) {
            autoGroupNo = getRfcGroupNo(null);
            appSubmissionDto.setAppGrpNo(autoGroupNo);
        } else {
            appGroupNo = getRfcGroupNo(null);
            appSubmissionDto.setAppGrpNo(appGroupNo);
        }
        appSubmissionDto.setDraftNo(draftNo);

        /*//judge is the preInspection
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = configCommService.judgeIsPreInspection(appSubmissionDto);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
        } else {
            appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }
        //set Risk Score
        RfcHelper.setRiskToDto(appSubmissionDto);*/
        RfcHelper.beforeSubmit(appSubmissionDto, oldAppSubmissionDto, appEditSelectDto, null, appType, bpc.request);
        // set status
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
        if (MiscUtil.doubleEquals(0.0, currentAmount)) {
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        appSubmissionDto.setGetAppInfoFromDto(true);
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        appSubmissionDto.setAuditTrailDto(auditTrailDto);
        List<AppSubmissionDto> appSubmissionDtoList = IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> autoSaveAppsubmission = IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> notAutoSaveAppsubmission = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto autoAppSubmissionDto = null;
        AppEditSelectDto autoChangeSelectDto = null;
        if (appEditSelectDto.isLicenseeEdit() || appEditSelectDto.isPremisesEdit() || appEditSelectDto.isSpecialisedEdit() || appEditSelectDto.isServiceEdit()) {
            // add the current dto to the group
            if (isAutoRfc) {
                autoSaveAppsubmission.add(appSubmissionDto);
            } else {
                notAutoSaveAppsubmission.add(appSubmissionDto);
            }
        }
        // init auto app submission
        if (!isAutoRfc && (appEditSelectDto.isLicenseeEdit() || appEditSelectDto.isSpecialisedEdit())) {
            autoAppSubmissionDto = CopyUtil.copyMutableObject(appSubmissionDto);
            autoAppSubmissionDto.setAmount(0.0);
            autoChangeSelectDto = new AppEditSelectDto();
        }

        boolean rfcSplitFlag = ConfigHelper.getBoolean("halp.rfc.split.flag", false);
        log.info(StringUtil.changeForLog("##### halp rfc split flag: " + rfcSplitFlag));
        // check the premises step is auto or not
        int isAutoPremises = -1;
        // check app submissions affected by premises+
        if (appEditSelectDto.isPremisesEdit()) {
            AppEditSelectDto changeSelectDto = new AppEditSelectDto();
            changeSelectDto.setPremisesEdit(true);
            changeSelectDto.setPremisesListEdit(true);
            changeSelectDto.setChangeHciName(appEditSelectDto.isChangeHciName());
            changeSelectDto.setChangeInLocation(appEditSelectDto.isChangeInLocation());
            changeSelectDto.setChangeAddFloorUnit(appEditSelectDto.isChangeAddFloorUnit());
            String groupNo;
            if (changeSelectDto.isAutoRfc()) {
                autoGroupNo = getRfcGroupNo(autoGroupNo);
                groupNo = autoGroupNo;
            } else {
                appGroupNo = getRfcGroupNo(appGroupNo);
                groupNo = appGroupNo;
            }
            // reSet amount
            double otherAmount = 0.0D;
            FeeDto premiseFee = configCommService.getGroupAmendAmount(getAmendmentFeeDto(changeSelectDto, isCharity));
            if (premiseFee != null && premiseFee.getTotal() != null) {
                otherAmount = premiseFee.getTotal();
            }
            log.info(StringUtil.changeForLog("The premise changed amount: " + otherAmount));
            List<AppSubmissionDto> appSubmissionDtos = IaisCommonUtils.genNewArrayList();
            boolean isValid = checkAffectedAppSubmissions(appGrpPremisesDtoList, otherAmount, draftNo, groupNo, changeSelectDto,
                    appSubmissionDtos, bpc.request);
            if (!isValid) {
                return;
            }
            // add the premises affected list to the group
            if (changeSelectDto.isAutoRfc()) {
                ApplicationHelper.addToAuto(appSubmissionDtos, autoSaveAppsubmission);
                // re-set change edit select dto
                isAutoPremises = 1;
            } else {
                ApplicationHelper.addToNonAuto(appSubmissionDtos, notAutoSaveAppsubmission);
                // split out the auto parts
                isAutoPremises = 0;
            }
            // for spliting
            if (changeSelectDto.isAutoRfc() && !isAutoRfc) {
                if (autoAppSubmissionDto == null) {
                    autoAppSubmissionDto = CopyUtil.copyMutableObject(appSubmissionDto);
                    autoAppSubmissionDto.setAmount(0.0);
                    autoChangeSelectDto = new AppEditSelectDto();
                }
                autoChangeSelectDto.setPremisesEdit(true);
                appEditSelectDto.setPremisesEdit(false);
                appEditSelectDto.setPremisesListEdit(false);
            }
        }
        log.info(StringUtil.changeForLog("isAutoPremises: " + isAutoPremises));

        boolean addClaimed = false;
        // check app submissions affected by sub licensee
        if (appEditSelectDto.isLicenseeEdit()) {
            if (!rfcSplitFlag) {
                autoGroupNo = getRfcGroupNo(autoGroupNo);
                String groupNo = autoGroupNo;
                SubLicenseeDto oldSublicenseeDto = oldAppSubmissionDto.getSubLicenseeDto();
                List<AppSubmissionDto> licenseeAffectedList = licCommService.getAppSubmissionDtosBySubLicensee(
                        oldSublicenseeDto);
                if (licenseeAffectedList == null) {
                    licenseeAffectedList = IaisCommonUtils.genNewArrayList(0);
                }
                // remove the current app submission
                for (Iterator<AppSubmissionDto> it = licenseeAffectedList.iterator(); it.hasNext(); ) {
                    AppSubmissionDto dto = it.next();
                    if (licenceId.equals(dto.getLicenceId())) {
                        it.remove();
                        break;
                    }
                }
                AppEditSelectDto changeSelectDto = new AppEditSelectDto();
                changeSelectDto.setLicenseeEdit(true);
                StreamSupport.stream(licenseeAffectedList.spliterator(),
                        licenseeAffectedList.size() >= RfcConst.DFT_MIN_PARALLEL_SIZE)
                        .forEach(dto -> dto.setSubLicenseeDto(
                                MiscUtil.transferEntityDto(appSubmissionDto.getSubLicenseeDto(), SubLicenseeDto.class)));
                boolean isValid = checkAffectedAppSubmissions(licenseeAffectedList, 0.0D, draftNo, groupNo, changeSelectDto,
                        HcsaAppConst.SECTION_LICENSEE, bpc.request);
                if (!isValid) {
                    return;
                }
                ApplicationHelper.addToAuto(licenseeAffectedList, autoSaveAppsubmission);
            }

            // re-set change edit select dto
            if (autoAppSubmissionDto != null) {
                autoChangeSelectDto.setLicenseeEdit(true);
                appEditSelectDto.setLicenseeEdit(false);
            }
        } else if (rfcSplitFlag) {
            SubLicenseeDto subLicenseeDto = appSubmissionDto.getSubLicenseeDto();
            addClaimed = StringUtil.isNotEmpty(subLicenseeDto.getClaimUenNo())
                    || StringUtil.isNotEmpty(subLicenseeDto.getClaimCompanyName());
        }
        log.info(StringUtil.changeForLog("##### Only Add claimed: " + addClaimed));
        // Primary Doc
        // re-set change edit select dto
        if (appEditSelectDto.isSpecialisedEdit() && autoAppSubmissionDto != null) {
            appEditSelectDto.setSpecialisedEdit(false);
            autoChangeSelectDto.setSpecialisedEdit(true);
        }
        // check app submissions affected by personnel (service info)
        if (appEditSelectDto.isServiceEdit()) {
            String licenseeId = ApplicationHelper.getLicenseeId(bpc.request);
            List<AppSubmissionDto> personAppSubmissionList = licCommService.personContact(licenseeId,
                    appSubmissionDto, oldAppSubmissionDto, rfcSplitFlag ? 0 : 1);
            if (personAppSubmissionList != null && !personAppSubmissionList.isEmpty()) {
                autoGroupNo = getRfcGroupNo(autoGroupNo);
                boolean isValid = checkAffectedAppSubmissions(personAppSubmissionList, 0.0D, draftNo, autoGroupNo,
                        null, HcsaAppConst.SECTION_SVCINFO, bpc.request);
                if (!isValid) {
                    return;
                }
                ApplicationHelper.addToAuto(personAppSubmissionList, autoSaveAppsubmission);
            }

            // re-set current auto dto
            List<String> changeList = appSubmissionDto.getChangeSelectDto().getPersonnelEditList();
            List<String> stepList = appSubmissionDto.getAppEditSelectDto().getPersonnelEditList();
            log.info(StringUtil.changeForLog("StepList: " + stepList + " - ChangeList: " + changeList));
            if (!isAutoRfc && autoAppSubmissionDto == null && !IaisCommonUtils.isEmpty(changeList)) {
                autoAppSubmissionDto = CopyUtil.copyMutableObject(appSubmissionDto);
                autoAppSubmissionDto.setAmount(0.0);
                autoChangeSelectDto = new AppEditSelectDto();
            }
            if (autoAppSubmissionDto != null) {
                autoChangeSelectDto.setServiceEdit(true);
                autoAppSubmissionDto.setAppSvcRelatedInfoDtoList(
                        RfcHelper.generateDtosForAutoFields(autoAppSubmissionDto, oldAppSubmissionDto,
                                changeList, stepList));
                // re-set change edit select dto
                if (!appEditSelectDto.isChangeBusinessName() && !appEditSelectDto.isChangeVehicle()
                        && !appEditSelectDto.isChangePersonnel() && !appEditSelectDto.isChangeSectionLeader()) {
                    appEditSelectDto.setServiceEdit(false);
                }
            }
        }

        // synchronize data
        if (!autoSaveAppsubmission.isEmpty() && !notAutoSaveAppsubmission.isEmpty()) {
            StreamSupport.stream(notAutoSaveAppsubmission.spliterator(),
                    notAutoSaveAppsubmission.size() >= RfcConst.DFT_MIN_PARALLEL_SIZE)
                    .forEach(targetDto -> autoSaveAppsubmission.stream()
                            .filter(source -> Objects.equals(targetDto.getLicenceId(), source.getLicenceId()))
                            .findAny()
                            .ifPresent(submissionDto ->
                                    ApplicationHelper.reSetNonAutoDataByAppEditSelectDto(targetDto, submissionDto)));
        }

        // re-set autoAppSubmissionDto
        if (autoAppSubmissionDto != null) {
            if (0 == isAutoPremises) {
                autoAppSubmissionDto.setAppGrpPremisesDtoList(oldAppGrpPremisesDtoList);
            }
            autoGroupNo = getRfcGroupNo(autoGroupNo);
            ApplicationHelper.reSetAdditionalFields(autoAppSubmissionDto, autoChangeSelectDto, autoGroupNo);
            autoAppSubmissionDto.setChangeSelectDto(autoChangeSelectDto);
            autoSaveAppsubmission.add(0, autoAppSubmissionDto);
        }
        String autoGroupStatus = ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED;
        if (autoSaveAppsubmission.isEmpty() && notAutoSaveAppsubmission.isEmpty() && addClaimed) {
            autoGroupStatus = ApplicationConsts.APPLICATION_GROUP_STATUS_ADDITIONAL_CLAIM;
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
            ApplicationHelper.reSetAdditionalFields(appSubmissionDto, false, true, appSubmissionDto.getAppGrpNo());
            autoSaveAppsubmission.add(appSubmissionDto);
        }
        // check whether the data has been changed or not
        if (autoSaveAppsubmission.isEmpty() && notAutoSaveAppsubmission.isEmpty()) {
            bpc.request.setAttribute("RFC_ERROR_NO_CHANGE", MessageUtil.getMessageDesc("RFC_ERR010"));
            return;
        }
        log.info(StringUtil.changeForLog("the appGroupNo --> Not-auto: " + appGroupNo + " - Auto:" + autoGroupNo));
        AppDeclarationMessageDto appDeclarationMessageDto = !appEditSelectDto.isChangeHciName() ? null :
                appSubmissionDto.getAppDeclarationMessageDto();
        List<AppDeclarationDocDto> appDeclarationDocDtos = !appEditSelectDto.isChangeHciName() ? null :
                appSubmissionDto.getAppDeclarationDocDtos();
        Date effectiveDate = appDeclarationMessageDto != null && appDeclarationMessageDto.getEffectiveDt() != null ?
                appDeclarationMessageDto.getEffectiveDt() : appSubmissionDto.getEffectiveDate();
        String effectiveDateStr = Formatter.formatDate(effectiveDate);
        log.info(StringUtil.changeForLog("effectiveDate: " + effectiveDateStr));
        log.info(StringUtil.changeForLog("------ Save Data Start ------"));
        List<AppSubmissionDto> ackPageAppSubmissionDto = new ArrayList<>(2);
        List<String> svcNameSet = new ArrayList<>();
        String notAutoGroupId = null;
        String autoGroupId = null;
        if (!notAutoSaveAppsubmission.isEmpty()) {
            // save submission (notAUto data)
            String appGrpStatus = autoSaveAppsubmission.isEmpty() ? ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED : ApplicationConsts.APPLICATION_GROUP_STATUS_PENDING_AUTO;
            notAutoSaveAppsubmission.parallelStream().forEach(dto -> {
                dto.setEffectiveDateStr(effectiveDateStr);
                dto.setEffectiveDate(effectiveDate);
                dto.setAppDeclarationMessageDto(appDeclarationMessageDto);
                dto.setAppDeclarationDocDtos(appDeclarationDocDtos);
                dto.setAppGrpStatus(appGrpStatus);
            });
            // save application, group, declaration
            List<AppSubmissionDto> appSubmissionDtos1 = submitRequestForChange(notAutoSaveAppsubmission, false);
            notAutoGroupId = afterSubmitRequestForChange(appSubmissionDtos1, ackPageAppSubmissionDto, svcNameSet);
            appSubmissionDtoList.addAll(appSubmissionDtos1);
            appSubmissionDto.setAppGrpId(notAutoGroupId);
        }
        final String newAutoGrpStatus = autoGroupStatus;
        if (!autoSaveAppsubmission.isEmpty()) {
            // save submission (auto data)
            autoSaveAppsubmission.parallelStream().forEach(dto -> {
                dto.setEffectiveDateStr(effectiveDateStr);
                dto.setEffectiveDate(effectiveDate);
                dto.setAppDeclarationMessageDto(appDeclarationMessageDto);
                dto.setAppDeclarationDocDtos(appDeclarationDocDtos);
                dto.setAppGrpStatus(newAutoGrpStatus);
            });
            // save application, group, declaration
            List<AppSubmissionDto> appSubmissionDtos1 = submitRequestForChange(autoSaveAppsubmission, true);
            autoGroupId = afterSubmitRequestForChange(appSubmissionDtos1, ackPageAppSubmissionDto, svcNameSet);
            appSubmissionDtoList.addAll(appSubmissionDtos1);
            if (StringUtil.isEmpty(notAutoGroupId)) {
                appSubmissionDto.setAppGrpId(autoGroupId);
            }
        }
        // app group misc
        appCommService.saveAutoRfcLinkAppGroupMisc(notAutoGroupId, autoGroupId);
        handleDraft(draftNo, ApplicationHelper.getLicenseeId(bpc.request), appSubmissionDto, appSubmissionDtoList);
        log.info(StringUtil.changeForLog("------ Save Data End ------"));
        bpc.request.getSession().setAttribute(APP_SUBMISSIONS, appSubmissionDtoList);
        bpc.request.getSession().setAttribute(ACK_APP_SUBMISSIONS, ackPageAppSubmissionDto);
        bpc.request.getSession().setAttribute(HcsaAppConst.ALL_SVC_NAMES, svcNameSet);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "payment");
        log.info(StringUtil.changeForLog("the do doRequestForChangeSubmit start ...."));
    }

    protected void handleDraft(String draftNo, String licenseeId, AppSubmissionDto appSubmissionDto,
            List<AppSubmissionDto> appSubmissionDtoList) {}

    protected abstract List<AppSubmissionDto> submitRequestForChange(List<AppSubmissionDto> appSubmissionDtoList, boolean isAutoRfc);

    protected String afterSubmitRequestForChange(List<AppSubmissionDto> appSubmissionDtos,
            List<AppSubmissionDto> ackPageAppSubmissionDto, List<String> svcNameSet) {
        String appGrpNo = appSubmissionDtos.get(0).getAppGrpNo();
        String appGrpId = appSubmissionDtos.get(0).getAppGrpId();
        //appSubmissionDtos1.get(0).getAppSvcRelatedInfoDtoList().get(0).setGroupNo(appSubmissionDtos1.get(0).getAppGrpNo());
        double ackPageAmount = 0.0;
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
            Double amount = appSubmissionDto.getAmount();
            ackPageAmount = ackPageAmount + (amount == null ? 0D : amount);
            String s = Formatter.formatterMoney(amount);
            appSubmissionDto.setAmountStr(s);
            svcNameSet.add(appSubmissionDto.getServiceName());
            appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).setGroupNo(appGrpNo);
        }
        AppSubmissionDto o1 = CopyUtil.copyMutableObject(appSubmissionDtos.get(0));
        o1.setAmount(ackPageAmount);
        o1.setAmountStr(Formatter.formatterMoney(ackPageAmount));
        ackPageAppSubmissionDto.add(o1);
        return appGrpId;
    }

    private String getRfcGroupNo(String groupNo) {
        if (groupNo == null) {
            groupNo = appCommService.getGroupNo(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        }
        return groupNo;
    }

    private boolean checkAffectedAppSubmissions(List<AppGrpPremisesDto> appGrpPremisesDtoList, double amount, String draftNo,
            String appGroupNo, AppEditSelectDto appEditSelectDto, List<AppSubmissionDto> appSubmissionDtos,
            HttpServletRequest request) {
        if (appGrpPremisesDtoList == null) {
            return true;
        }
        for (AppGrpPremisesDto premisesDto : appGrpPremisesDtoList) {
            String[] selectedLicences = premisesDto.getSelectedLicences();
            List<LicenceDto> licenceDtos = null;
            List<LicenceDto> existLicences = premisesDto.getLicenceDtos();
            if (IaisCommonUtils.isNotEmpty(existLicences)) {
                licenceDtos = existLicences.stream()
                        .filter(dto -> StringUtil.isIn(dto.getId(), selectedLicences))
                        .collect(Collectors.toList());
            }
            Map<String, String> errorMap = appCommService.checkAffectedAppSubmissions(licenceDtos, premisesDto,
                    amount, draftNo, appGroupNo, appEditSelectDto, appSubmissionDtos);
            if (!errorMap.isEmpty()) {
                AppValidatorHelper.setErrorRequest(errorMap, false, request);
                return false;
            }
        }
        return true;
    }

    private boolean checkAffectedAppSubmissions(List<AppSubmissionDto> appSubmissionDtos, double amount, String draftNo,
            String appGroupNo, AppEditSelectDto appEditSelectDto, String type, HttpServletRequest request) {
        if (appSubmissionDtos == null || appSubmissionDtos.isEmpty()) {
            return true;
        }
        log.info(StringUtil.changeForLog("##### Affected Size: " + appSubmissionDtos.size()));
        boolean parallel = appSubmissionDtos.size() >= RfcConst.DFT_MIN_PARALLEL_SIZE;
        Map<AppSubmissionDto, List<String>> errorListMap = StreamSupport.stream(appSubmissionDtos.spliterator(), parallel)
                .collect(Collectors.toMap(Function.identity(), dto -> AppValidatorHelper.doPreviewSubmitValidate(null, dto, false)));
        String errorMsg = AppValidatorHelper.getErrorMsg(errorListMap);
        if (StringUtil.isNotEmpty(errorMsg)) {
            ParamUtil.setRequestAttr(request, RfcConst.SHOW_OTHER_ERROR, errorMsg);
            return false;
        }
        Map<String, String> map = AppValidatorHelper.validateLicences(appSubmissionDtos, type);
        if (map != null && !map.isEmpty()) {
            log.info(StringUtil.changeForLog("##### checkAffectedAppSubmissions: " + map));
            AppValidatorHelper.setErrorRequest(map, false, request);
            return false;
        }
        StreamSupport.stream(appSubmissionDtos.spliterator(), parallel)
                .forEach(dto -> appCommService.checkAffectedAppSubmissions(dto, null, amount, draftNo, appGroupNo,
                        appEditSelectDto, null));
        return true;
    }

    public void reSubmit(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("do reSubmit start ..."));
        String draftNo = ParamUtil.getMaskedString(bpc.request, "draftNo");
        String url = "https://" + bpc.request.getServerName() +
                "/hcsa-licence-web/eservice/INTERNET/MohNewApplication?DraftNumber=" +
                MaskUtil.maskValue(HcsaAppConst.DRAFT_NUMBER, draftNo);
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url, bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);

        log.info(StringUtil.changeForLog("do reSubmit end ..."));
    }

    /**
     * Step: doPayValidate
     *
     * @param bpc
     * @throws Exception
     */
    public void doPayValidate(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("do doPayValidate start ..."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        Double totalAmount = appSubmissionDto.getAmount();
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        appSubmissionDto.setPaymentMethod(payMethod);
        String giroAccNum = "";
        if (!StringUtil.isEmpty(payMethod) && ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)) {
            giroAccNum = ParamUtil.getString(bpc.request, "giroAccount");
        }
        appSubmissionDto.setGiroAcctNum(giroAccNum);
        String noNeedPayment = bpc.request.getParameter("noNeedPayment");
        log.debug(StringUtil.changeForLog("payMethod:" + payMethod));
        log.debug(StringUtil.changeForLog("noNeedPayment:" + noNeedPayment));
        String action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        if (ACTION_NEXT.equals(action)) {
            if (!MiscUtil.doubleEquals(0.0, totalAmount) && StringUtil.isEmpty(noNeedPayment)) {
                Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
                if (StringUtil.isEmpty(payMethod)) {
                    errorMap.put("pay", MessageUtil.replaceMessage("GENERAL_ERR0006", "Payment Method", "field"));
                } else if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod) && StringUtil.isEmpty(giroAccNum)) {
                    errorMap.put("pay", MessageUtil.replaceMessage("GENERAL_ERR0006", "Giro Account", "field"));
                }
                if (!errorMap.isEmpty()) {
                    initErrorAction(HcsaAppConst.ACTION_PAYMENT, errorMap, appSubmissionDto, bpc.request);
                }
            }
            if (!StringUtil.isEmpty(noNeedPayment)) {
                ParamUtil.setSessionAttr(bpc.request, "txnRefNo", "");
                try {
                    if (appSubmissionDto.getAppType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)) {
                        List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request,
                                APP_SUBMISSIONS);
                        if (appSubmissionDtos == null || appSubmissionDtos.size() == 0) {
                            appSubmissionDtos = IaisCommonUtils.genNewArrayList();
                            appSubmissionDtos.add(appSubmissionDto);
                        }
                        sendRfcSubmittedEmail(appSubmissionDtos, payMethod);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        } else {
            appSubmissionDto.setId(null);
            appSubmissionDto.setAppGrpId(null);
            appSubmissionDto.setAppGrpNo(null);
            if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                    || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);
                if (oldAppSubmissionDto != null) {
                    ApplicationHelper.setOldAppSubmissionDto(oldAppSubmissionDto, bpc.request);
                }
            }
            DealSessionUtil.clearPremisesMap(bpc.request);
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        String tranSferFlag = appSubmissionDto.getTransferFlag();
        //back to tansfer page
        if (!ACTION_NEXT.equals(action) && !StringUtil.isEmpty(tranSferFlag)) {
            AppSubmissionDto tranferSub = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "app-rfc-tranfer");
            if (tranferSub != null) {
                tranferSub.setPaymentMethod(payMethod);
                ParamUtil.setSessionAttr(bpc.request, "app-rfc-tranfer", tranferSub);
            }
            String url = "https://" + bpc.request.getServerName() +
                    "/hcsa-licence-web/eservice/INTERNET/MohRequestForChange/prepareTranfer";
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url, bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }
        log.info(StringUtil.changeForLog("do doPayValidate end ..."));
    }

    protected abstract void sendRfcSubmittedEmail(List<AppSubmissionDto> appSubmissionDtos, String pmtMethod) throws Exception;

    /**
     * StartStep: doSubmit
     *
     * @param bpc
     * @throws
     */
    public void doSubmit(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the do doSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        Map<String, String> coMap = appSubmissionDto.getCoMap();
        // validate all data
        Map<String, String> map = AppValidatorHelper.doPreviewAndSumbit(bpc);
        if (!map.isEmpty()) {
            //set audit
            ParamUtil.setRequestAttr(bpc.request, "Msg", map);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_PREVIEW);
            coMap.put(HcsaAppConst.SECTION_PREVIEW, "");
            ApplicationHelper.setAppSubmissionDto(appSubmissionDto, bpc.request);
            return;
        } else {
            coMap.put(HcsaAppConst.SECTION_PREVIEW, HcsaAppConst.SECTION_PREVIEW);
        }
        //sync person data
        Map<String, AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,
                HcsaAppConst.PERSONSELECTMAP);
        ApplicationHelper.syncPsnData(appSubmissionDto, personMap);

        String appType = appSubmissionDto.getAppType();
        String draftNo = appSubmissionDto.getDraftNo();
        if (StringUtil.isEmpty(draftNo)) {
            draftNo = appCommService.getDraftNo(appType);
            appSubmissionDto.setDraftNo(draftNo);
        }
        //get appGroupNo
        String appGroupNo = appCommService.getGroupNo(appType);
        log.info(StringUtil.changeForLog("the appGroupNo is -->:" + appGroupNo));
//        appSubmissionDto.setAppGrpNo(appGroupNo);
//        //clear appGrpId
//        appSubmissionDto.setAppGrpId(null);
        //get Amount
        FeeDto feeDto = getNewAppAmount(appSubmissionDto, ApplicationHelper.isCharity(bpc.request));
        appSubmissionDto.setFeeInfoDtos(feeDto.getFeeInfoDtos());
        Double amount = feeDto.getTotal();
        log.info(StringUtil.changeForLog("the amount is -->:" + amount));
        /*if(0.0==amount){
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
            appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        }*/
        appSubmissionDto.setAmount(amount);

        AppEditSelectDto appEditSelectDto = ApplicationHelper.createAppEditSelectDto(true);
        appEditSelectDto.setLicenseeEdit(ApplicationHelper.canLicenseeEdit(appSubmissionDto.getSubLicenseeDto(),
                appSubmissionDto.getAppType(), true, true));
        appSubmissionDto.setChangeSelectDto(appEditSelectDto);
        //judge is giro acc
        boolean isGiroAcc = organizationService.isGiroAccount(appSubmissionDto.getLicenseeId());
        appSubmissionDto.setGiroAccount(isGiroAcc);

        RfcHelper.beforeSubmit(appSubmissionDto, null, appEditSelectDto, appGroupNo, appType, bpc.request);

        appSubmissionDto = submit(appSubmissionDto);

        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);

        //get wrokgroup
        log.info(StringUtil.changeForLog("the do doSubmit end ...."));
    }

    protected abstract AppSubmissionDto submit(AppSubmissionDto appSubmissionDto);

    protected abstract FeeDto getNewAppAmount(AppSubmissionDto appSubmissionDto, boolean charity);

    /**
     * StartStep: PrepareAckPage
     *
     * @param bpc
     * @throws
     */
    public void prepareAckPage(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareAckPage start ...."));
    }

    /**
     * StartStep: prepareJumpv
     *
     * @param bpc
     * @throws
     */
    public void prepareJump(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("the do prepareJump start ...."));
        String action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        if (StringUtil.isEmpty(action)) {
            action = ParamUtil.getRequestString(bpc.request, "nextStep");
        }
        if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)) {
            AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);
            AppSubmissionDto oldDto = CopyUtil.copyMutableObject(oldAppSubmissionDto);
            AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
            AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
            if (appEditSelectDto.isLicenseeEdit()) {
                appSubmissionDto.setSubLicenseeDto(oldDto.getSubLicenseeDto());
            }
            if (appEditSelectDto.isPremisesEdit()) {
                appSubmissionDto.setAppGrpPremisesDtoList(oldDto.getAppGrpPremisesDtoList());
            }
            if (appEditSelectDto.isServiceEdit()) {
                appSubmissionDto.setAppSvcRelatedInfoDtoList(oldDto.getAppSvcRelatedInfoDtoList());
            }
            ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO, appSubmissionDto);
        }

        log.info(StringUtil.changeForLog("the do prepareJump end ...."));
    }

    private AmendmentFeeDto getAmendmentFeeDto(AppEditSelectDto appEditSelectDto, boolean isCharity) {
        return getAmendmentFeeDto(appEditSelectDto.isChangeHciName(),
                appEditSelectDto.isChangeInLocation() || appEditSelectDto.isChangeAddFloorUnit(),
                appEditSelectDto.isChangeVehicle(), isCharity, appEditSelectDto.isChangeBusinessName());
    }

    private AmendmentFeeDto getAmendmentFeeDto(boolean changeHciName, boolean changeLocation, boolean changeVehicles,
            boolean isCharity, boolean changeBusiness) {
        AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
        amendmentFeeDto.setChangeInLicensee(Boolean.FALSE);
        amendmentFeeDto.setChangeInHCIName(changeHciName);
        amendmentFeeDto.setChangeInLocation(changeLocation);
        if (changeVehicles) {
            amendmentFeeDto.setChangeInHCIName(Boolean.TRUE);
        }
        amendmentFeeDto.setIsCharity(isCharity);
        amendmentFeeDto.setChangeBusinessName(changeBusiness);
        return amendmentFeeDto;
    }

    protected static AppSubmissionDto getAppSubmissionDto(HttpServletRequest request) {
        return ApplicationHelper.getAppSubmissionDto(request);
    }

    /*private void setPsnDroTo(AppSubmissionDto appSubmissionDto, BaseProcessClass bpc) {
        Map<String, AppSvcPrincipalOfficersDto> personMap =
                (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.PERSONSELECTMAP);
        String personMapStr = JsonUtil.parseToJson(personMap);
        appSubmissionDto.setDropDownPsnMapStr(personMapStr);
    }*/

    /**
     * Step: PrepareErrorAck
     *
     * @param bpc
     * @throws
     */
    public void prepareErrorAck(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareErrorAck start ...."));

        log.info(StringUtil.changeForLog("the do prepareErrorAck end ...."));
    }

}
