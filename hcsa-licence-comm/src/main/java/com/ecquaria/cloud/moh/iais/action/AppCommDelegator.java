package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
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
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.ACKMESSAGE;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.ACKSTATUS;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.ACK_APP_SUBMISSIONS;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.ACTION_PREVIEW;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.APPSUBMISSIONDTO;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.APP_SUBMISSIONS;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.COND_TYPE_RFI;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.IS_EDIT;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.LICENSEE_MAP;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.LICENSEE_OPTIONS;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.PREMISESTYPE;

/**
 * @Auther chenlei on 5/3/2022.
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
    public void doStart(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the do Start start ...."));
        HcsaServiceCacheHelper.flushServiceMapping();
        DealSessionUtil.clearSession(bpc.request);
        //fro draft loading
        String draftNo = ParamUtil.getMaskedString(bpc.request, HcsaAppConst.DRAFT_NUMBER);
        //for rfi loading
        String appNo = ParamUtil.getMaskedString(bpc.request, "appNo");
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_NEW_APPLICATION, AuditTrailConsts.FUNCTION_NEW_APPLICATION);
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
        boolean flag = loadingServiceConfig(bpc);
        log.info(StringUtil.changeForLog("The loadingServiceConfig -->:" + flag));
        if (flag) {
            //init session and data reomve function to DealSessionUtil
            DealSessionUtil.initSession(bpc);
        }
        bpc.request.getSession().setAttribute("RFC_ERR004", MessageUtil.getMessageDesc("RFC_ERR004"));
        // app type and licence id
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,
                APPSUBMISSIONDTO);
        if (appSubmissionDto != null && appSubmissionDto.getAppSvcRelatedInfoDtoList() != null) {
            for (AppSvcRelatedInfoDto dto : appSubmissionDto.getAppSvcRelatedInfoDtoList()) {
                if (StringUtil.isEmpty(dto.getApplicationType())) {
                    dto.setApplicationType(appSubmissionDto.getAppType());
                }
                if (StringUtil.isEmpty(dto.getLicenceId())) {
                    dto.setLicenceId(appSubmissionDto.getLicenceId());
                }
            }
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
        if (canDoEdit && appSubmissionDto != null) {
            AuditTrailHelper.setAuditTrailInfoByAppType(appType);
            ParamUtil.setSessionAttr(request, "hasDetail", "Y");
            ParamUtil.setSessionAttr(request, "isSingle", "Y");
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            if (RfcConst.EDIT_LICENSEE.equals(currentEdit)) {
                appEditSelectDto.setLicenseeEdit(true);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "licensee");
            } else if (RfcConst.EDIT_PREMISES.equals(currentEdit)) {
                appEditSelectDto.setPremisesEdit(true);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, HcsaAppConst.ACTION_PREMISES);
            } else if (RfcConst.EDIT_PRIMARY_DOC.equals(currentEdit)) {
                appEditSelectDto.setDocEdit(true);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "documents");
            } else if (RfcConst.EDIT_SERVICE.equals(currentEdit)) {
                appEditSelectDto.setServiceEdit(true);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "serviceForms");
            }
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDto.setNeedEditController(true);
            ParamUtil.setSessionAttr(request, APPSUBMISSIONDTO, appSubmissionDto);
            HashMap<String, String> coMap = (HashMap<String, String>) request.getSession().getAttribute(HcsaAppConst.CO_MAP);
            coMap.put(HcsaAppConst.SECTION_LICENSEE, HcsaAppConst.SECTION_LICENSEE);
            coMap.put(HcsaAppConst.SECTION_PREMISES, HcsaAppConst.SECTION_PREMISES);
            coMap.put(HcsaAppConst.SECTION_DOCUMENT, HcsaAppConst.SECTION_PREMISES);
            coMap.put(HcsaAppConst.SECTION_SVCINFO, HcsaAppConst.SECTION_PREMISES);
            coMap.put(HcsaAppConst.SECTION_PREVIEW, HcsaAppConst.SECTION_PREVIEW);
            ParamUtil.setSessionAttr(request, HcsaAppConst.CO_MAP, coMap);
        }
        log.info(StringUtil.changeForLog("the do requestForChangeLoading end ...."));
    }

    protected void requestForInformationLoading(HttpServletRequest request, String appNo) {
    }

    protected void handlePremises(AppSubmissionDto appSubmissionDto, String appNo) {
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
            //filter other premise info
            if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())
                    && appGrpPremisesDtos.size() > 1) {
                List<AppGrpPremisesDto> newAppGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
                filtrationAppGrpPremisesDtos(appNo, appSubmissionDto, newAppGrpPremisesDtoList);
                appGrpPremisesDtos = newAppGrpPremisesDtoList;
                appSubmissionDto.setAppGrpPrimaryDocDtos(
                        filterPrimaryDocs(appSubmissionDto.getAppGrpPrimaryDocDtos(), newAppGrpPremisesDtoList));
            }
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                appGrpPremisesDto.setFromDB(true);
                appGrpPremisesDto.setExistingData(AppConsts.NO);
                appGrpPremisesDto.setRfiCanEdit(true);
                //clear ph id
                List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos = appGrpPremisesDto.getAppPremPhOpenPeriodList();
                if (!IaisCommonUtils.isEmpty(appPremPhOpenPeriodDtos)) {
                    for (AppPremPhOpenPeriodDto phDto : appPremPhOpenPeriodDtos) {
                        phDto.setId(null);
                        phDto.setPremId(null);
                    }
                    appGrpPremisesDto.setAppPremPhOpenPeriodList(appPremPhOpenPeriodDtos);
                }
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

    protected List<AppGrpPrimaryDocDto> filterPrimaryDocs(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos,
            List<AppGrpPremisesDto> newPremisesDtos) {
        if (appGrpPrimaryDocDtos == null || newPremisesDtos == null) {
            return appGrpPrimaryDocDtos;
        }
        return appGrpPrimaryDocDtos.stream()
                .filter(dto -> dto.getAppGrpPremId() == null ||
                        newPremisesDtos.stream().anyMatch(premise -> dto.getAppGrpPremId().equals(premise.getId())))
                .collect(Collectors.toList());
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
            AppGrpPremisesEntityDto actualPrem = appCommService.getPremisesByAppNo(appNo);
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
        ApplicationHelper.setWrkTime(appGrpPremisesDto);
        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos = appGrpPremisesDto.getAppPremPhOpenPeriodList();
        //set ph name
        ApplicationHelper.setPhName(appPremPhOpenPeriodDtos);
        newPremisesDtos.add(appGrpPremisesDto);
    }

    protected void svcRelatedInfoRFI(AppSubmissionDto appSubmissionDto, String appNo) {
        if (!ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            return;
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
            jumpToAckPage(bpc, HcsaAppConst.ACK_STATUS_ERROR, errMsg);
            return false;
        }

        List<HcsaServiceDto> hcsaServiceDtoList = null;
        if (!serviceConfigIds.isEmpty()) {
            hcsaServiceDtoList = configCommService.getHcsaServiceDtosById(serviceConfigIds);
        } else if (!names.isEmpty()) {
            hcsaServiceDtoList = configCommService.getActiveHcsaSvcByNames(names);
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
     * Step: PrepareSubLicensee
     * <p>
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
            appSubmissionDto.setSubLicenseeDto(subLicenseeDto);
        }
        SubLicenseeDto orgLicensee = organizationService.getSubLicenseeByLicenseeId(licenseeId);
        orgLicensee.setClaimUenNo(subLicenseeDto.getClaimUenNo());
        orgLicensee.setClaimCompanyName(subLicenseeDto.getClaimCompanyName());
        if (OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY.equals(subLicenseeDto.getLicenseeType())
                || OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(orgLicensee.getLicenseeType())) {
            subLicenseeDto = (SubLicenseeDto) CopyUtil.copyMutableObject(orgLicensee);
            appSubmissionDto.setSubLicenseeDto(subLicenseeDto);
        }
        // init option, map and some fields
        if (!OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(subLicenseeDto.getLicenseeType())) {
            Map<String, SubLicenseeDto> licenseeMap =
                    (Map<String, SubLicenseeDto>) bpc.request.getSession().getAttribute(LICENSEE_MAP);
            if (licenseeMap == null) {
                List<SubLicenseeDto> subLicenseeDtoList = licCommService.getIndividualSubLicensees(loginContext.getOrgId());
                licenseeMap = ApplicationHelper.genSubLicessMap(subLicenseeDtoList);
                bpc.request.getSession().setAttribute(LICENSEE_MAP, licenseeMap);
            }
            bpc.request.setAttribute(LICENSEE_OPTIONS, ApplicationHelper.genSubLicessOption(licenseeMap));
            String assignSelect = subLicenseeDto.getAssignSelect();
            if ((StringUtil.isEmpty(assignSelect) || IaisEGPConstant.ASSIGN_SELECT_ADD_NEW.equals(assignSelect))
                    && OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL.equals(subLicenseeDto.getLicenseeType())) {
                String assigned = ApplicationHelper.getAssignSelect(licenseeMap.keySet(), subLicenseeDto.getNationality(),
                        subLicenseeDto.getIdType(), subLicenseeDto.getIdNumber());
                if (StringUtil.isEmpty(assignSelect) || !"-1".equals(assigned) && !IaisEGPConstant.ASSIGN_SELECT_ADD_NEW.equals(
                        assigned)) {
                    subLicenseeDto.setAssignSelect(assigned);
                }
            }
        }
        if (StringUtil.isEmpty(subLicenseeDto.getUenNo())) {
            subLicenseeDto.setUenNo(loginContext.getUenNo());
        }
        bpc.request.setAttribute("subLicenseeDto", orgLicensee);
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
        if ("back".equals(action) || RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "jump");
            return;
        }
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String isEdit = ParamUtil.getString(bpc.request, IS_EDIT);
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(appSubmissionDto,
                ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_LICENSEE, isEdit, isRfi);
        log.info(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (isGetDataFromPage) {
            SubLicenseeDto subLicenseeDto = getSubLicenseeDtoFromPage(bpc.request);
            appSubmissionDto.setSubLicenseeDto(subLicenseeDto);
            if (appSubmissionDto.isNeedEditController()) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                clickEditPages.add(HcsaAppConst.APP_PAGE_NAME_LICENSEE);
                appSubmissionDto.setClickEditPage(clickEditPages);
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                appEditSelectDto.setLicenseeEdit(ApplicationHelper.canLicenseeEdit(appSubmissionDto, isRfi));
            }
            String actionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            if (!"saveDraft".equals(actionValue)) {
                AppValidatorHelper.validateSubLicenseeDto(errorMap, subLicenseeDto, bpc.request);
            }
        }

        if (!errorMap.isEmpty()) {
            //set audit
            AppValidatorHelper.setAudiErrMap(isRfi, appSubmissionDto.getAppType(), errorMap, appSubmissionDto.getRfiAppNo(),
                    appSubmissionDto.getLicenceNo());
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, HcsaAppConst.ACTION_LICENSEE);
            bpc.request.setAttribute("errormapIs", "error");
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(HcsaAppConst.CO_MAP);
            coMap.put(HcsaAppConst.SECTION_LICENSEE, "");
            bpc.request.getSession().setAttribute(HcsaAppConst.CO_MAP, coMap);
        } else {
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(HcsaAppConst.CO_MAP);
            coMap.put(HcsaAppConst.SECTION_LICENSEE, HcsaAppConst.SECTION_LICENSEE);
            bpc.request.getSession().setAttribute(HcsaAppConst.CO_MAP, coMap);
            String actionAdditional = ParamUtil.getString(bpc.request, "crud_action_additional");
            if ("rfcSaveDraft".equals(actionAdditional)) {
                try {
                    doSaveDraft(bpc);
                } catch (IOException e) {
                    log.error("error", e);
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
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
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            if (loginContext != null) {
                dto.setOrgId(loginContext.getOrgId());
                dto.setUenNo(loginContext.getUenNo());
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
        // boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        //get svcCode to get svcId
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,
                AppServicesConsts.HCSASERVICEDTOLIST);
        List<HcsaServiceDto> rfiHcsaService = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, "rfiHcsaService");
        List<String> svcIds = IaisCommonUtils.genNewArrayList();
        if (rfiHcsaService != null) {
            rfiHcsaService.forEach(v -> svcIds.add(v.getId()));
        } else {
            if (hcsaServiceDtoList != null) {
                hcsaServiceDtoList.forEach(item -> svcIds.add(item.getId()));
            }
        }

        String licenseeId = appSubmissionDto.getLicenseeId();
        if (StringUtil.isEmpty(licenseeId)) {
            licenseeId = ApplicationHelper.getLicenseeId(bpc.request);
        }
        log.info(StringUtil.changeForLog("The preparePremises licenseeId is -->:" + licenseeId));

        //premise select select options
        ApplicationHelper.setPremSelect(bpc.request);

        //get premises type
        if (!IaisCommonUtils.isEmpty(svcIds)) {
            log.info(StringUtil.changeForLog("svcId not null"));
            log.debug(StringUtil.changeForLog("svc id List :" + JsonUtil.parseToJson(svcIds)));
            Set<String> premisesType = configCommService.getAppGrpPremisesTypeBySvcId(svcIds);
            boolean readOnlyPrem = false;
            if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    if (!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo()) || !StringUtil.isEmpty(
                            appSvcRelatedInfoDto.getAlignLicenceNo())) {
                        readOnlyPrem = true;
                        break;
                    }
                }
                if (readOnlyPrem) {
                    premisesType = IaisCommonUtils.genNewHashSet();
                    AppGrpPremisesDto appGrpPremisesDto = appSubmissionDto.getAppGrpPremisesDtoList().get(0);
                    premisesType.add(appGrpPremisesDto.getPremisesType());
                }
            }

            ParamUtil.setSessionAttr(bpc.request, PREMISESTYPE, (Serializable) premisesType);
        } else {
            log.debug(StringUtil.changeForLog("do not have select the services"));
        }

        //reload dateTime
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if (!IaisCommonUtils.isEmpty(appGrpPremisesDtoList)) {
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                ApplicationHelper.setWrkTime(appGrpPremisesDto);
                ApplicationHelper.setOldHciCode(appGrpPremisesDto);
            }
        }
        appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
        //
        int baseSvcCount = 0;
        if (hcsaServiceDtoList != null) {
            for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtoList) {
                if (ApplicationConsts.SERVICE_TYPE_BASE.equalsIgnoreCase(hcsaServiceDto.getSvcType())) {
                    baseSvcCount++;
                }
            }
        }
        if (baseSvcCount > 1) {
            ParamUtil.setRequestAttr(bpc.request, "multiBase", AppConsts.TRUE);
        } else {
            ParamUtil.setRequestAttr(bpc.request, "multiBase", AppConsts.FALSE);
        }
        //when rfc/renew check is select existing premises
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(
                appType)) {
            AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);
            if (appSubmissionDto.getAppGrpPremisesDtoList().size() == oldAppSubmissionDto.getAppGrpPremisesDtoList().size()) {
                int length = appSubmissionDto.getAppGrpPremisesDtoList().size();
                for (int i = 0; i < length; i++) {
                    AppGrpPremisesDto appGrpPremisesDto = appSubmissionDto.getAppGrpPremisesDtoList().get(i);
                    AppGrpPremisesDto oldAppGrpPremisesDto = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(i);
                    if (appGrpPremisesDto != null && oldAppGrpPremisesDto != null) {
                        String premSel = appGrpPremisesDto.getPremisesSelect();
                        String oldPremSel = oldAppGrpPremisesDto.getPremisesSelect();
                        if (oldPremSel.equals(premSel) || "-1".equals(premSel)) {
                            ParamUtil.setRequestAttr(bpc.request, "PageCanEdit", AppConsts.TRUE);
                        }
                    }
                }
            }

            setSelectLicence(appSubmissionDto, bpc.request);
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        List<SelectOption> weeklyOpList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DAY_NAMES);
        ParamUtil.setRequestAttr(bpc.request, "weeklyOpList", weeklyOpList);
        List<SelectOption> phOpList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_PUBLIC_HOLIDAY);
        ParamUtil.setRequestAttr(bpc.request, "phOpList", phOpList);
        ParamUtil.setRequestAttr(bpc.request, "weeklyCount", systemParamConfig.getWeeklyCount());
        ParamUtil.setRequestAttr(bpc.request, "phCount", systemParamConfig.getPhCount());
        ParamUtil.setRequestAttr(bpc.request, "eventCount", systemParamConfig.getEventCount());
        ParamUtil.setRequestAttr(bpc.request, "postalCodeAckMsg", MessageUtil.getMessageDesc("NEW_ACK016"));
        //single premises service
        ParamUtil.setRequestAttr(bpc.request, "isMultiPremService", ApplicationHelper.isMultiPremService(hcsaServiceDtoList));
        log.info(StringUtil.changeForLog("the do preparePremises end ...."));
    }

    public void setSelectLicence(AppSubmissionDto appSubmissionDto, HttpServletRequest request) {
        List<AppGrpPremisesDto> appGrpPremisesDtoList1 = appSubmissionDto.getAppGrpPremisesDtoList();
        String licenceNo = appSubmissionDto.getLicenceNo();
        String licenseeId = appSubmissionDto.getLicenseeId();
        for (int i = 0; i < appGrpPremisesDtoList1.size(); i++) {
            List<LicenceDto> licenceDtoByHciCode = licCommService.getLicenceDtoByHciCode(licenseeId,
                    appGrpPremisesDtoList1.get(i), licenceNo);
            appGrpPremisesDtoList1.get(i).setLicenceDtos(licenceDtoByHciCode);
            request.getSession().setAttribute("selectLicence" + i, licenceDtoByHciCode);
        }
    }

    /**
     * StartStep: PrepareDocuments
     *
     * @param bpc
     * @throws
     * @throwsdo
     */
    public void prepareDocuments(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareDocuments start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<HcsaSvcDocConfigDto> hcsaSvcDocDtos;
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        AppSubmissionDto oldAppSubDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = IaisCommonUtils.genNewArrayList();
        if (oldAppSubDto != null) {
            appGrpPrimaryDocDtos = oldAppSubDto.getAppGrpPrimaryDocDtos();
        }
        if (isRfi && appGrpPrimaryDocDtos != null && appGrpPrimaryDocDtos.size() > 0) {
            hcsaSvcDocDtos = configCommService.getPrimaryDocConfigById(appGrpPrimaryDocDtos.get(0).getSvcComDocId());
        } else {
            hcsaSvcDocDtos = configCommService.getAllHcsaSvcDocs(null);
        }
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.PRIMARY_DOC_CONFIG, (Serializable) hcsaSvcDocDtos);

        //reload page
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = appSubmissionDto.getAppGrpPrimaryDocDtos();
        Map<String, List<AppGrpPrimaryDocDto>> reloadDocMap = IaisCommonUtils.genNewHashMap();
        if (!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtoList)) {
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtoList) {
                if (StringUtil.isEmpty(appGrpPrimaryDocDto.getFileRepoId()) && StringUtil.isEmpty(appGrpPrimaryDocDto.getDocName())) {
                    continue;
                }
                String reloadDocMapKey;
                if (StringUtil.isEmpty(appGrpPrimaryDocDto.getPremisessName())) {
                    reloadDocMapKey = appGrpPrimaryDocDto.getSvcComDocId();
                } else {
                    reloadDocMapKey = appGrpPrimaryDocDto.getPremisessName() + appGrpPrimaryDocDto.getSvcComDocId();
                }

                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos1 = reloadDocMap.get(reloadDocMapKey);
                if (IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos1)) {
                    appGrpPrimaryDocDtos1 = IaisCommonUtils.genNewArrayList();
                }
                appGrpPrimaryDocDtos1.add(appGrpPrimaryDocDto);
                reloadDocMap.put(reloadDocMapKey, appGrpPrimaryDocDtos1);
            }
            //do sort
            reloadDocMap.forEach((k, v) -> Collections.sort(v, Comparator.comparing(AppGrpPrimaryDocDto::getSeqNum)));
        }
        ParamUtil.setSessionAttr(bpc.request, "docReloadMap", (Serializable) reloadDocMap);

        int sysFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setRequestAttr(bpc.request, "sysFileSize", sysFileSize);
        String sysFileType = systemParamConfig.getUploadFileType();
        String[] sysFileTypeArr = FileUtils.fileTypeToArray(sysFileType);
        StringBuilder fileTypeStr = new StringBuilder();
        if (sysFileTypeArr != null) {
            int i = 0;
            int fileTypeLength = sysFileTypeArr.length;
            for (String fileType : sysFileTypeArr) {
                fileTypeStr.append(' ').append(fileType);
                if (fileTypeLength > 1 && i < fileTypeLength - 2) {
                    fileTypeStr.append(',');
                }
                if (fileTypeLength > 1 && i == sysFileTypeArr.length - 2) {
                    fileTypeStr.append(" and");
                }
                if (i == fileTypeLength - 1) {
                    fileTypeStr.append('.');
                }
                i++;
            }
        }
        ParamUtil.setRequestAttr(bpc.request, "sysFileType", fileTypeStr.toString());
        log.info(StringUtil.changeForLog("the do prepareDocuments end ...."));
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
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())
                || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
            RfcHelper.svcDocToPresmise(appSubmissionDto);
        }
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        List<HcsaSvcDocConfigDto> primaryDocConfig = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request,
                HcsaAppConst.PRIMARY_DOC_CONFIG);
        if (IaisCommonUtils.isEmpty(primaryDocConfig)) {
            if (isRfi && appGrpPrimaryDocDtos != null && appGrpPrimaryDocDtos.size() > 0) {
                primaryDocConfig = configCommService.getPrimaryDocConfigById(appGrpPrimaryDocDtos.get(0).getSvcComDocId());
            } else {
                primaryDocConfig = configCommService.getAllHcsaSvcDocs(null);
            }
            ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.PRIMARY_DOC_CONFIG, (Serializable) primaryDocConfig);
        }

        Map<String, List<AppGrpPrimaryDocDto>> reloadPrimaryDocMap = ApplicationHelper.genPrimaryDocReloadMap(primaryDocConfig,
                appSubmissionDto.getAppGrpPremisesDtoList(), appGrpPrimaryDocDtos);
        appSubmissionDto.setMultipleGrpPrimaryDoc(reloadPrimaryDocMap);
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
        AppDataHelper.initDeclarationFiles(appSubmissionDto.getAppDeclarationDocDtos(), appSubmissionDto.getAppType(), bpc.request);
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
        HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(HcsaAppConst.CO_MAP);

        String paymentMethod;

        String serviceConfig = (String) bpc.request.getSession().getAttribute("serviceConfig");

        ApplicationHelper.setStepColor(coMap, serviceConfig, appSubmissionDto);

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
                if (premTypes.size() == 1 && premTypes.contains(ApplicationConsts.PREMISES_TYPE_OFF_SITE)) {
                    boolean flag = true;
                    ParamUtil.setRequestAttr(bpc.request, "onlyOffsite", flag);
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
            ApplicationHelper.clearPremisesMap(bpc.request);
            return;
        }
        //gen dto
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);

        String isEdit = ParamUtil.getString(bpc.request, IS_EDIT);
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(appSubmissionDto,
                ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_PREMISES_INFORMATION, isEdit, isRfi);
        log.info(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        if (isGetDataFromPage) {
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            List<AppGrpPremisesDto> appGrpPremisesDtoList = AppDataHelper.genAppGrpPremisesDtoList(bpc.request);
            if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())
                    || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
                for (int i = 0; i < oldAppGrpPremisesDtoList.size(); i++) {
                    appGrpPremisesDtoList.get(i).setOldHciCode(oldAppGrpPremisesDtoList.get(i).getOldHciCode());
                }
            }

            appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
            if (appSubmissionDto.isNeedEditController()) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                clickEditPages.add(HcsaAppConst.APP_PAGE_NAME_PREMISES);
                appSubmissionDto.setClickEditPage(clickEditPages);
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                appEditSelectDto.setPremisesEdit(true);
                appSubmissionDto.setChangeSelectDto(appEditSelectDto);
            }
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                //65718
                ApplicationHelper.removePremiseEmptyAlignInfo(appSubmissionDto);
                //ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
            }
            //update address
            ApplicationHelper.updatePremisesAddress(appSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);

        }
        String crud_action_additional = ParamUtil.getString(bpc.request, "crud_action_additional");
        if (!"saveDraft".equals(crud_action_value)) {
            boolean isNeedShowValidation = !"back".equals(crud_action_value);
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,
                    AppServicesConsts.HCSASERVICEDTOLIST);
            List<String> premisesHciList = appCommService.getHciFromPendAppAndLic(appSubmissionDto.getLicenseeId(), hcsaServiceDtos);
            ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.PREMISES_HCI_LIST, (Serializable) premisesHciList);
            AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);

            String actionType = bpc.request.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);

            Map<String, String> errorMap = AppValidatorHelper.doValidatePremiss(appSubmissionDto, oldAppSubmissionDto,
                    premisesHciList, isRfi, true);

            String crud_action_type_continue = bpc.request.getParameter("crud_action_type_continue");
            bpc.request.setAttribute("continueStep", actionType);
            bpc.request.setAttribute("crudActionTypeContinue", crud_action_additional);
            if ("continue".equals(crud_action_type_continue)) {
                errorMap.remove("hciNameUsed");
            }
            String hciNameUsed = errorMap.get("hciNameUsed");
            if (errorMap.size() == 1 && hciNameUsed != null) {
                ParamUtil.setRequestAttr(bpc.request, "hciNameUsed", "hciNameUsed");
                ParamUtil.setRequestAttr(bpc.request, "newAppPopUpMsg", hciNameUsed);
            }
            // check result
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(HcsaAppConst.CO_MAP);
            if (errorMap.size() > 0) {
                if (isNeedShowValidation) {
                    //set audit
                    AppValidatorHelper.setAudiErrMap(isRfi, appSubmissionDto.getAppType(), errorMap, appSubmissionDto.getRfiAppNo(),
                            appSubmissionDto.getLicenceNo());
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, HcsaAppConst.ACTION_PREMISES);
                    bpc.request.setAttribute("errormapIs", "error");
                }
                coMap.put(HcsaAppConst.SECTION_PREMISES, "");
            } else {
                coMap.put(HcsaAppConst.SECTION_PREMISES, HcsaAppConst.SECTION_PREMISES);
                if ("rfcSaveDraft".equals(crud_action_additional)) {
                    try {
                        doSaveDraft(bpc);
                    } catch (IOException e) {
                        log.error("error", e);
                    }
                }
            }
            // coMap.put("serviceConfig", sB.toString());
            bpc.request.getSession().setAttribute(HcsaAppConst.CO_MAP, coMap);
        }
        log.info(StringUtil.changeForLog("the do doPremises end ...."));
    }

    /**
     * StartStep: DoDocument
     *
     * @param bpc
     * @throws
     */
    public void doDocument(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the do doDocument start ...."));
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(
                HttpHandler.SOP6_MULTIPART_REQUEST);
        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);
        String crud_action_additional = mulReq.getParameter("crud_action_additional");

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, crudActionValue);
        ParamUtil.setRequestAttr(bpc.request, crud_action_additional, crud_action_additional);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        List<AppGrpPrimaryDocDto> newAppGrpPrimaryDocDtoList = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        AppSubmissionDto oldSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);
        if (oldSubmissionDto == null) {
            oldSubmissionDto = new AppSubmissionDto();
        }
        List<AppGrpPrimaryDocDto> oldPrimaryDocDtoList = oldSubmissionDto.getAppGrpPrimaryDocDtos();
        String action = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)) {
                //clear
                ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.RELOADAPPGRPPRIMARYDOCMAP, null);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "jump");
                return;
            }
        }

        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        String isEdit = ParamUtil.getString(mulReq, IS_EDIT);
        boolean isGetDataFromPage = ApplicationHelper.isGetDataFromPage(appSubmissionDto,
                ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SUPPORTING_DOCUMENT, isEdit, isRfi);

        //premIndexNo+configId+seqnum
        Map<String, File> saveFileMap = IaisCommonUtils.genNewHashMap();
        if (isGetDataFromPage) {
            List<AppGrpPremisesDto> appGrpPremisesList = appSubmissionDto.getAppGrpPremisesDtoList();
            if (appSubmissionDto.isNeedEditController()) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                clickEditPages.add(HcsaAppConst.APP_PAGE_NAME_PRIMARY);
                appSubmissionDto.setClickEditPage(clickEditPages);
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                appEditSelectDto.setDpoEdit(true);
                appSubmissionDto.setChangeSelectDto(appEditSelectDto);
            }
            List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request,
                    HcsaAppConst.PRIMARY_DOC_CONFIG);
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
            for (int i = 0; i < hcsaSvcDocConfigDtos.size(); i++) {
                HcsaSvcDocConfigDto hcsaSvcDocConfigDto = hcsaSvcDocConfigDtos.get(i);
                String dupForPrem = hcsaSvcDocConfigDto.getDupForPrem();
                if ("0".equals(dupForPrem)) {
                    String docKey = i + "primaryDoc";
                    Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(mulReq,
                            IaisEGPConstant.SEESION_FILES_MAP_AJAX + docKey);
                    genPrimaryDoc(fileMap, docKey, hcsaSvcDocConfigDto, saveFileMap, appGrpPrimaryDocDtos, newAppGrpPrimaryDocDtoList,
                            "", "", isRfi, oldPrimaryDocDtoList, oldSubmissionDto.getAppGrpId(), oldSubmissionDto.getRfiAppNo(),
                            appSubmissionDto.getAppType(), dupForPrem);
                    ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.SEESION_FILES_MAP_AJAX + docKey, (Serializable) fileMap);
                } else if ("1".equals(dupForPrem)) {
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesList) {
                        String docKey = i + "primaryDoc" + appGrpPremisesDto.getPremisesIndexNo();
                        Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(mulReq,
                                IaisEGPConstant.SEESION_FILES_MAP_AJAX + docKey);
                        genPrimaryDoc(fileMap, docKey, hcsaSvcDocConfigDto, saveFileMap, appGrpPrimaryDocDtos,
                                newAppGrpPrimaryDocDtoList, appGrpPremisesDto.getPremisesIndexNo(),
                                appGrpPremisesDto.getPremisesType(), isRfi, oldPrimaryDocDtoList, oldSubmissionDto.getAppGrpId(),
                                oldSubmissionDto.getRfiAppNo(), appSubmissionDto.getAppType(), dupForPrem);
                        ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.SEESION_FILES_MAP_AJAX + docKey, (Serializable) fileMap);
                    }
                }
            }
            //set value into AppSubmissionDto
            appSubmissionDto.setAppGrpPrimaryDocDtos(newAppGrpPrimaryDocDtoList);
        }
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = AppValidatorHelper.documentValid(bpc.request, errorMap, true);
        doIsCommom(bpc.request, errorMap);
        saveFileAndSetFileId(appGrpPrimaryDocDtos, saveFileMap);
        appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        // check CO Map
        HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(HcsaAppConst.CO_MAP);
        if (errorMap.isEmpty()) {
            coMap.put(HcsaAppConst.SECTION_DOCUMENT, HcsaAppConst.SECTION_DOCUMENT);
        } else {
            coMap.put(HcsaAppConst.SECTION_DOCUMENT, "");
        }
        bpc.request.getSession().setAttribute(HcsaAppConst.CO_MAP, coMap);
        String actionValue = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        if (errorMap.size() > 0 && !"back".equals(actionValue)) {
            //set audit
            bpc.request.setAttribute("errormapIs", "error");
            AppValidatorHelper.setAudiErrMap(isRfi, appSubmissionDto.getAppType(), errorMap, appSubmissionDto.getRfiAppNo(),
                    appSubmissionDto.getLicenceNo());
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPGRPPRIMARYDOCERRMSGMAP, (Serializable) errorMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "documents");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, "documents");
        }
        log.info(StringUtil.changeForLog("the do doDocument end ...."));
    }

    private void doIsCommom(HttpServletRequest request, Map<String, String> errorMap) {
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = appSubmissionDto.getAppGrpPrimaryDocDtos();
        String err006 = MessageUtil.replaceMessage("GENERAL_ERR0006", "Document", "field");
        List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigList =
                (List<HcsaSvcDocConfigDto>) request.getSession().getAttribute(HcsaAppConst.PRIMARY_DOC_CONFIG);
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        if (!IaisCommonUtils.isEmpty(commonHcsaSvcDocConfigList) && !IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
            int i = 0;
            String suffix = "Error";
            for (HcsaSvcDocConfigDto comm : commonHcsaSvcDocConfigList) {
                String errKey = i + "primaryDoc";
                Boolean isMandatory = comm.getIsMandatory();
                String dupForPrem = comm.getDupForPrem();
                String configId = comm.getId();
                i++;
                if (!isMandatory) {
                    continue;
                }
                if (IaisCommonUtils.isEmpty(appGrpPrimaryDocDtoList)) {
                    appGrpPrimaryDocDtoList = IaisCommonUtils.genNewArrayList();
                }
                if ("0".equals(dupForPrem)) {
                    AppGrpPrimaryDocDto appGrpPrimaryDocDto = getAppGrpPrimaryDocByConfigIdAndPremIndex(appGrpPrimaryDocDtoList,
                            configId, "");
                    if (appGrpPrimaryDocDto == null) {
                        errorMap.put(errKey + suffix, err006);
                    }
                } else if ("1".equals(dupForPrem)) {
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                        String premIndex = appGrpPremisesDto.getPremisesIndexNo();
                        String currErrKey = errKey + premIndex + suffix;
                        AppGrpPrimaryDocDto appGrpPrimaryDocDto = getAppGrpPrimaryDocByConfigIdAndPremIndex(appGrpPrimaryDocDtoList,
                                configId, premIndex);
                        if (appGrpPrimaryDocDto == null) {
                            errorMap.put(currErrKey, err006);
                        }
                    }
                }
            }
        }
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
        String isGroupLic = ParamUtil.getString(bpc.request, "isGroupLic");
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        boolean needNewDeclaration = false;
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            if (!StringUtil.isEmpty(isGroupLic) && AppConsts.YES.equals(isGroupLic)) {
                appSubmissionDto.setGroupLic(true);
            } else {
                appSubmissionDto.setGroupLic(false);
            }
            needNewDeclaration = !isRfi;
        } else if (!isRfi && ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
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
            AppValidatorHelper.validateDeclarationDoc(errorMap, AppDataHelper.getFileAppendId(appSubmissionDto.getAppType()),
                    preQuesKindly == null ? false : "0".equals(preQuesKindly), bpc.request);
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
            AppValidatorHelper.setAudiErrMap(ApplicationHelper.checkIsRfi(bpc.request), appSubmissionDto.getAppType(), errorMap,
                    appSubmissionDto.getRfiAppNo(), appSubmissionDto.getLicenceNo());
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_PREVIEW);
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
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        if (!StringUtil.isEmpty(action)) {
            if ("MohAppPremSelfDecl".equals(action)) {
//                ParamUtil.setSessionAttr(bpc.request, AppCommConst.SESSION_PARAM_APPLICATION_GROUP_ID, appSubmissionDto.getAppGrpId());
//                ParamUtil.setSessionAttr(bpc.request,AppCommConst.SESSION_SELF_DECL_ACTION,"new");
            } else if ("DashBoard".equals(action)) {
                StringBuilder url = new StringBuilder();
                url.append("https://").append(bpc.request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            } else if ("ChooseSvc".equals(action)) {
                StringBuilder url = new StringBuilder();
                url.append("https://").append(bpc.request.getServerName()).append(
                        "/hcsa-licence-web/eservice/INTERNET/MohServiceFeMenu");
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            }
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, action);
        }

        log.info(StringUtil.changeForLog("the do preInvoke start ...."));
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
            initAction(ACTION_PREVIEW, null, appSubmissionDto, bpc.request);
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
            initAction(ACTION_PREVIEW, doComChangeMap, appSubmissionDto, bpc.request);
            return;
        }
        log.info("doComChange is ok ...");
        Map<String, String> map = AppValidatorHelper.doPreviewAndSumbit(bpc);
        if (!map.isEmpty()) {
            initAction(ACTION_PREVIEW, map, appSubmissionDto, bpc.request);
            return;
        }
        // check whether it has been withdrew or not
        String appId = getCurrentRfiAppId(oldAppSubmissionDto);
        List<AppPremiseMiscDto> appPremiseMiscs = appCommService.getActiveWithdrawAppPremiseMiscsByApp(appId);
        if (IaisCommonUtils.isNotEmpty(appPremiseMiscs)) {
            // RFI_ERR002: There is a withdrawal for this application.
            ParamUtil.setRequestAttr(bpc.request, "showRfiWithdrawal", AppConsts.YES);
            initAction(ACTION_PREVIEW, null, appSubmissionDto, bpc.request);
            return;
        }
        ApplicationHelper.reSetAdditionalFields(appSubmissionDto, oldAppSubmissionDto);
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        oldAppSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(
                appSubmissionDto.getAppType()) || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                appSubmissionDto.getAppType())) {
            appSubmissionDto.setAppDeclarationDocDtos(oldAppSubmissionDto.getAppDeclarationDocDtos());
            appSubmissionDto.setAppDeclarationMessageDto(oldAppSubmissionDto.getAppDeclarationMessageDto());
            RfcHelper.premisesDocToSvcDoc(appSubmissionDto);
        } else {
            //handler primary doc
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = RfcHelper.handlerPrimaryDoc(
                    appSubmissionDto.getAppGrpPremisesDtoList(), appSubmissionDto.getAppGrpPrimaryDocDtos());
            appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);
        }

        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                appSubmissionDto.getAppType()) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
            //beforeSubmitRfi(appSubmissionDto, appNo);
        }
        if (ApplicationHelper.isFrontend()) {
            String msgId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
            appSubmissionDto.setRfiMsgId(msgId);
        }
        appSubmissionRequestInformationDto.setAppSubmissionDto(appSubmissionDto);
        appSubmissionRequestInformationDto.setOldAppSubmissionDto(oldAppSubmissionDto);
        appSubmissionRequestInformationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        //update message statusdo
        //appSubmissionService.updateMsgStatus(msgId, MessageConstants.MESSAGE_STATUS_RESPONSE);

        appSubmissionDto = submitRequestInformation(appSubmissionRequestInformationDto, appSubmissionDto.getAppType());

        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
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
            List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos = oldAppSubmissionDto.getAppGrpPrimaryDocDtos();
            if (dtoAppGrpPrimaryDocDtos != null) {
                for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : dtoAppGrpPrimaryDocDtos) {
                    appGrpPrimaryDocDto.setPassValidate(true);
                }
            }
            List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos1 = appSubmissionDto.getAppGrpPrimaryDocDtos();
            if (dtoAppGrpPrimaryDocDtos1 != null) {
                for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : dtoAppGrpPrimaryDocDtos1) {
                    appGrpPrimaryDocDto.setPassValidate(true);
                }
            }
            if (!appEditSelectDto.isDocEdit()) {
                boolean b = RfcHelper.eqDocChange(appSubmissionDto.getAppGrpPrimaryDocDtos(),
                        oldAppSubmissionDto.getAppGrpPrimaryDocDtos());
                if (b) {
                    log.info(StringUtil.changeForLog(
                            "appGrpPrimaryDocDto" + JsonUtil.parseToJson(appSubmissionDto.getAppGrpPrimaryDocDtos())));
                    log.info(StringUtil.changeForLog(
                            "oldAppGrpPrimaryDocDto" + JsonUtil.parseToJson(oldAppSubmissionDto.getAppGrpPrimaryDocDtos())));
                    result.put("document", MessageUtil.replaceMessage("GENERAL_ERR0006", "document", "field"));
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

    protected void initAction(String action, Map<String, String> errorMap, AppSubmissionDto appSubmissionDto,
            HttpServletRequest request) {
        if (IaisCommonUtils.isNotEmpty(errorMap)) {
            boolean isRfi = ApplicationHelper.checkIsRfi(request);
            if (isRfi) {
                AppValidatorHelper.setAudiErrMap(isRfi, appSubmissionDto.getAppType(), errorMap, appSubmissionDto.getRfiAppNo(),
                        appSubmissionDto.getLicenceNo());
            }
        }
        ParamUtil.setRequestAttr(request, "Msg", errorMap);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, action);
        ParamUtil.setRequestAttr(request, COND_TYPE_RFI, "N");
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
        HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(HcsaAppConst.CO_MAP);

        String serviceConfig = (String) bpc.request.getSession().getAttribute("serviceConfig");

        ApplicationHelper.setStepColor(coMap, serviceConfig, appSubmissionDto);

        Integer maxFileIndex = (Integer) ParamUtil.getSessionAttr(bpc.request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR);
        if (maxFileIndex == null) {
            maxFileIndex = 0;
        }
        appSubmissionDto.setMaxFileIndex(maxFileIndex);
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
                AppValidatorHelper.validateDeclarationDoc(map, AppDataHelper.getFileAppendId(appSubmissionDto.getAppType()),
                        preQuesKindly == null ? false : "0".equals(preQuesKindly), bpc.request);
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
        /**
         * when use save it as draft in the previous, and the licence has been updated via other licence,
         * the licence will not be valid any more, so when use do the it from the old draft,
         * the licence will be null.
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
        if (map != null && !map.isEmpty()) {
            AppValidatorHelper.setErrorRequest(map, false, bpc.request);
            return;
        }
        RfcHelper.setRelatedInfoBaseServiceId(appSubmissionDto);
        String baseServiceId = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getBaseServiceId();
        if (StringUtil.isEmpty(baseServiceId)) {
            bpc.request.setAttribute(RfcConst.SERVICE_CONFIG_CHANGE, MessageUtil.getMessageDesc("RFC_ERR020"));
            return;
        }
        // change edit
        AppEditSelectDto appEditSelectDto = RfcHelper.rfcChangeModuleEvaluationDto(appSubmissionDto, oldAppSubmissionDto);
        boolean isAutoRfc = appEditSelectDto.isAutoRfc();
        // reSet: isNeedNewLicNo and self assessment flag
        ApplicationHelper.reSetAdditionalFields(appSubmissionDto, oldAppSubmissionDto, appEditSelectDto);
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
        double amount = feeDto.getTotal();
        double currentAmount = amount;
        if (licenceById.getMigrated() == 1 && IaisEGPHelper.isActiveMigrated()) {
            currentAmount = 0.0;
        }
        log.info(StringUtil.changeForLog("the current amount is -->:" + currentAmount));
        appSubmissionDto.setAmount(currentAmount);

        String appGroupNo = null;
        String autoGroupNo = null;
        String draftNo = Optional.ofNullable(appSubmissionDto.getDraftNo())
                .orElseGet(() -> appCommService.getDraftNo(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE));
        log.info(StringUtil.changeForLog("the draft is -->:" + draftNo));
        if (isAutoRfc) {
            autoGroupNo = getRfcGroupNo(autoGroupNo);
            appSubmissionDto.setAppGrpNo(autoGroupNo);
        } else {
            appGroupNo = getRfcGroupNo(appGroupNo);
            appSubmissionDto.setAppGrpNo(appGroupNo);
        }
        appSubmissionDto.setDraftNo(draftNo);

        //judge is the preInspection
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = configCommService.judgeIsPreInspection(appSubmissionDto);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
        } else {
            appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }
        //set Risk Score
        RfcHelper.setRiskToDto(appSubmissionDto);
        // set status
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
        if (MiscUtil.doubleEquals(0.0, currentAmount)) {
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        RfcHelper.premisesDocToSvcDoc(appSubmissionDto);
        RfcHelper.premisesDocToSvcDoc(oldAppSubmissionDto);
        appSubmissionDto.setGetAppInfoFromDto(true);
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        appSubmissionDto.setAuditTrailDto(auditTrailDto);
        Map<AppSubmissionDto, List<String>> errorListMap = IaisCommonUtils.genNewHashMap();
        List<AppSubmissionDto> appSubmissionDtoList = IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> autoSaveAppsubmission = IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> notAutoSaveAppsubmission = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto autoAppSubmissionDto = null;
        AppEditSelectDto autoChangeSelectDto = null;
        if (appEditSelectDto.isLicenseeEdit() || appEditSelectDto.isPremisesEdit() || appEditSelectDto.isDocEdit() || appEditSelectDto.isServiceEdit()) {
            // add the current dto to the group
            if (isAutoRfc) {
                autoSaveAppsubmission.add(appSubmissionDto);
            } else {
                notAutoSaveAppsubmission.add(appSubmissionDto);
            }
        }
        // init auto app submission
        if (!isAutoRfc && (appEditSelectDto.isLicenseeEdit() || appEditSelectDto.isDocEdit())) {
            autoAppSubmissionDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDto);
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
            List<AppSubmissionDto> appSubmissionDtos;
            if (rfcSplitFlag) {
                HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceByServiceName(appSubmissionDto.getServiceName());
                boolean checkSpec = ApplicationConsts.SERVICE_TYPE_BASE.equals(serviceDto.getSvcType());
                appSubmissionDtos = licCommService.getAlginAppSubmissionDtos(appSubmissionDto.getLicenceId(), checkSpec);
                if (IaisCommonUtils.isNotEmpty(appSubmissionDtos)) {
                    StreamSupport.stream(appSubmissionDtos.spliterator(), appSubmissionDtos.size() >= RfcConst.DFT_MIN_PARALLEL_SIZE)
                            .forEach(dto -> ApplicationHelper.reSetPremeses(dto, appGrpPremisesDtoList));
                    boolean isValid = checkAffectedAppSubmissions(appSubmissionDtos, otherAmount, draftNo, groupNo, changeSelectDto,
                            HcsaAppConst.SECTION_PREMISES, bpc.request);
                    if (!isValid) {
                        return;
                    }
                }
            } else {
                appSubmissionDtos = IaisCommonUtils.genNewArrayList();
                boolean isValid = checkAffectedAppSubmissions(appGrpPremisesDtoList, otherAmount, draftNo, groupNo, changeSelectDto,
                        appSubmissionDtos, bpc.request);
                if (!isValid) {
                    return;
                }
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
                    autoAppSubmissionDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDto);
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
        if (appEditSelectDto.isDocEdit() && autoAppSubmissionDto != null) {
            appEditSelectDto.setDocEdit(false);
            autoChangeSelectDto.setDocEdit(true);
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
                    .forEach(targetDto -> {
                        Optional<AppSubmissionDto> optional = autoSaveAppsubmission.stream()
                                .filter(source -> Objects.equals(targetDto.getLicenceId(), source.getLicenceId()))
                                .findAny();
                        if (optional.isPresent()) {
                            ApplicationHelper.reSetNonAutoDataByAppEditSelectDto(targetDto, optional.get());
                        }
                    });
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
        appCommService.saveAutoRFCLinkAppGroupMisc(notAutoGroupId, autoGroupId);
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
        int size = appGrpPremisesDtoList.size();
        for (int i = 0; i < size; i++) {
            AppGrpPremisesDto premisesDto = appGrpPremisesDtoList.get(i);
            // all latest licence under the current hci code and licensee
            List<LicenceDto> licenceDtos = (List<LicenceDto>) request.getSession().getAttribute("selectLicence" + i);
            if (licenceDtos == null) {
                licenceDtos = premisesDto.getLicenceDtos();
            }
            if (licenceDtos == null) {
                continue;
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
        boolean parallel = true;//appSubmissionDtos.size() >= RfcConst.DFT_MIN_PARALLEL_SIZE;
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
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName())
                .append("/hcsa-licence-web/eservice/INTERNET/MohNewApplication?DraftNumber=")
                .append(MaskUtil.maskValue(HcsaAppConst.DRAFT_NUMBER, draftNo));
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);

        log.info(StringUtil.changeForLog("do reSubmit end ..."));
    }

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
        if ("next".equals(action)) {
            if (!MiscUtil.doubleEquals(0.0, totalAmount) && StringUtil.isEmpty(noNeedPayment)) {
                Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
                if (StringUtil.isEmpty(payMethod)) {
                    errorMap.put("pay", MessageUtil.replaceMessage("GENERAL_ERR0006", "Payment Method", "field"));
                } else if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod) && StringUtil.isEmpty(giroAccNum)) {
                    errorMap.put("pay", MessageUtil.replaceMessage("GENERAL_ERR0006", "Giro Account", "field"));
                }
                if (!errorMap.isEmpty()) {
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "payment");
                    AppValidatorHelper.setAudiErrMap(ApplicationHelper.checkIsRfi(bpc.request), appSubmissionDto.getAppType(),
                            errorMap, appSubmissionDto.getRfiAppNo(), appSubmissionDto.getLicenceNo());
                    ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
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
                        sendRfcSubmittedEmail(appSubmissionDtos, null);
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
                RfcHelper.svcDocToPresmise(appSubmissionDto);
                AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);
                if (oldAppSubmissionDto != null) {
                    RfcHelper.svcDocToPresmise(oldAppSubmissionDto);
                    ApplicationHelper.setOldAppSubmissionDto(oldAppSubmissionDto, bpc.request);
                }
            }
            ApplicationHelper.clearPremisesMap(bpc.request);
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        String tranSferFlag = appSubmissionDto.getTransferFlag();
        //back to tansfer page
        if (!"next".equals(action) && !StringUtil.isEmpty(tranSferFlag)) {
            AppSubmissionDto tranferSub = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "app-rfc-tranfer");
            if (tranferSub != null) {
                tranferSub.setPaymentMethod(payMethod);
                ParamUtil.setSessionAttr(bpc.request, "app-rfc-tranfer", tranferSub);
            }
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohRequestForChange/prepareTranfer");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
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
        // validate all data
        Map<String, String> map = AppValidatorHelper.doPreviewAndSumbit(bpc);
        if (!map.isEmpty()) {
            //set audit
            ParamUtil.setRequestAttr(bpc.request, "Msg", map);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_PREVIEW);
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(HcsaAppConst.CO_MAP);
            coMap.put(HcsaAppConst.SECTION_PREVIEW, "");
            bpc.request.getSession().setAttribute(HcsaAppConst.CO_MAP, coMap);
            return;
        } else {
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(HcsaAppConst.CO_MAP);
            coMap.put(HcsaAppConst.SECTION_PREVIEW, HcsaAppConst.SECTION_PREVIEW);
            bpc.request.getSession().setAttribute(HcsaAppConst.CO_MAP, coMap);
        }
        //sync person data
        Map<String, AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,
                HcsaAppConst.PERSONSELECTMAP);
        ApplicationHelper.syncPsnData(appSubmissionDto, personMap);

        String draftNo = appSubmissionDto.getDraftNo();
        if (StringUtil.isEmpty(draftNo)) {
            draftNo = appCommService.getDraftNo(appSubmissionDto.getAppType());
            appSubmissionDto.setDraftNo(draftNo);
        }
        //get appGroupNo
        String appGroupNo = appCommService.getGroupNo(appSubmissionDto.getAppType());
        log.info(StringUtil.changeForLog("the appGroupNo is -->:" + appGroupNo));
        appSubmissionDto.setAppGrpNo(appGroupNo);
        //clear appGrpId
        appSubmissionDto.setAppGrpId(null);
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
        //judge is the preInspection
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = configCommService.judgeIsPreInspection(appSubmissionDto);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
        } else {
            appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }

        //set Risk Score
        RfcHelper.setRiskToDto(appSubmissionDto);

        //set psn dropdown
        setPsnDroTo(appSubmissionDto, bpc);
        //rfi select control
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setLicenseeEdit(ApplicationHelper.canLicenseeEdit(appSubmissionDto, false));
        appEditSelectDto.setPremisesEdit(true);
        appEditSelectDto.setDocEdit(true);
        appEditSelectDto.setServiceEdit(true);
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            if (!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo()) || !StringUtil.isEmpty(
                    appSvcRelatedInfoDto.getAlignLicenceNo())) {
                appEditSelectDto.setPremisesEdit(false);
                break;
            }
        }
        appSubmissionDto.setChangeSelectDto(appEditSelectDto);
        HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(HcsaAppConst.CO_MAP);
        List<String> strList = new ArrayList<>(5);
        coMap.forEach((k, v) -> {
            if (!StringUtil.isEmpty(v)) {
                strList.add(v);
            }
        });
        String serviceConfig = (String) bpc.request.getSession().getAttribute("serviceConfig");
        strList.add(serviceConfig);
        appSubmissionDto.setStepColor(strList);
        //judge is giro acc
        boolean isGiroAcc = organizationService.isGiroAccount(appSubmissionDto.getLicenseeId());
        appSubmissionDto.setGiroAccount(isGiroAcc);
        //handler primary doc
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = RfcHelper.handlerPrimaryDoc(appSubmissionDto.getAppGrpPremisesDtoList(),
                appSubmissionDto.getAppGrpPrimaryDocDtos());
        appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);
        Integer maxFileIndex = (Integer) ParamUtil.getSessionAttr(bpc.request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR);
        if (maxFileIndex == null) {
            maxFileIndex = 0;
        }
        appSubmissionDto.setMaxFileIndex(maxFileIndex);
        ApplicationHelper.reSetAdditionalFields(appSubmissionDto, true, false, appGroupNo);
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
            if (appEditSelectDto.isDocEdit()) {
                appSubmissionDto.setAppGrpPrimaryDocDtos(oldDto.getAppGrpPrimaryDocDtos());
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

    private static void jumpToAckPage(BaseProcessClass bpc, String ackStatus, String errorMsg) {
        String actionType = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (StringUtil.isEmpty(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "errorAck");
            if (HcsaAppConst.ACK_STATUS_ERROR.equals(ackStatus)) {
                ParamUtil.setRequestAttr(bpc.request, ACKSTATUS, "error");
                ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, errorMsg);
            }
        }
    }

    private void setPsnDroTo(AppSubmissionDto appSubmissionDto, BaseProcessClass bpc) {
        Map<String, AppSvcPrincipalOfficersDto> personMap =
                (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.PERSONSELECTMAP);
        String personMapStr = JsonUtil.parseToJson(personMap);
        appSubmissionDto.setDropDownPsnMapStr(personMapStr);
    }

    private Integer getAppGrpPrimaryDocVersion(String configDocId, List<AppGrpPrimaryDocDto> oldDocs, boolean isRfi, String md5Code,
            String appGrpId, String appNo, String appType, int seqNum, String dupForPrem) {
        log.info(StringUtil.changeForLog("AppGrpPrimaryDocVersion start..."));
        Integer version = 1;
        if (StringUtil.isEmpty(configDocId) || IaisCommonUtils.isEmpty(oldDocs) || StringUtil.isEmpty(md5Code)) {
            return version;
        }
        log.info(StringUtil.changeForLog("isRfi:" + isRfi));
        log.info(StringUtil.changeForLog("appType:" + appType));
        log.info(StringUtil.changeForLog("seqNum:" + seqNum));
        if (isRfi) {
            log.info(StringUtil.changeForLog("rfi appNo:" + appNo));
            boolean canFound = false;
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : oldDocs) {
                Integer oldVersion = appGrpPrimaryDocDto.getVersion();
                if (configDocId.equals(appGrpPrimaryDocDto.getSvcComDocId()) && seqNum == appGrpPrimaryDocDto.getSeqNum()) {
                    canFound = true;
                    if (MessageDigest.isEqual(md5Code.getBytes(StandardCharsets.UTF_8),
                            appGrpPrimaryDocDto.getMd5Code().getBytes(StandardCharsets.UTF_8))) {
                        if (!StringUtil.isEmpty(oldVersion)) {
                            version = oldVersion;
                        }
                    } else {
                        version = getVersion(appGrpId, configDocId, appNo, appType, dupForPrem, seqNum);
                    }
                    break;
                }
            }
            if (!canFound) {
                //last doc is null new rfi not use app no
                version = getVersion(appGrpId, configDocId, appNo, appType, dupForPrem, seqNum);
            }
        }
        log.info(StringUtil.changeForLog("AppGrpPrimaryDocVersion end..."));
        return version;
    }

    private Integer getVersion(String appGrpId, String configDocId, String appNo, String appType, String dupForPrem, int seqNum) {
        Integer version = 1;

        //common doc
        if ("0".equals(dupForPrem)) {
            if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                AppGrpPrimaryDocDto maxVersionDocDto = appCommService.getMaxVersionPrimaryComDoc(appGrpId, configDocId,
                        String.valueOf(seqNum));
                Integer maxVersion = maxVersionDocDto.getVersion();
                String fileRepoId = maxVersionDocDto.getFileRepoId();
                if (!StringUtil.isEmpty(maxVersion) && !StringUtil.isEmpty(fileRepoId)) {
                    version = maxVersionDocDto.getVersion() + 1;
                }
            } else {
                AppSvcDocDto maxVersionDocDto = appCommService.getMaxVersionSvcComDoc(appGrpId, configDocId, String.valueOf(seqNum));
                if (!StringUtil.isEmpty(maxVersionDocDto.getVersion()) && !StringUtil.isEmpty(maxVersionDocDto.getFileRepoId())) {
                    version = maxVersionDocDto.getVersion() + 1;
                }
            }

        } else if ("1".equals(dupForPrem)) {
            if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                AppGrpPrimaryDocDto maxVersionDocDto = appCommService.getMaxVersionPrimarySpecDoc(appGrpId, configDocId, appNo,
                        String.valueOf(seqNum));
                if (!StringUtil.isEmpty(maxVersionDocDto.getVersion()) && !StringUtil.isEmpty(maxVersionDocDto.getFileRepoId())) {
                    version = maxVersionDocDto.getVersion() + 1;
                }
            } else {
                AppSvcDocDto searchDto = new AppSvcDocDto();
                searchDto.setAppGrpId(appGrpId);
                searchDto.setSvcDocId(configDocId);
                searchDto.setSeqNum(seqNum);
                AppSvcDocDto maxVersionDocDto = appCommService.getMaxVersionSvcSpecDoc(searchDto, appNo);
                if (!StringUtil.isEmpty(maxVersionDocDto.getVersion()) && !StringUtil.isEmpty(maxVersionDocDto.getFileRepoId())) {
                    version = maxVersionDocDto.getVersion() + 1;
                }
            }
        }
        return version;
    }

    private static AppGrpPrimaryDocDto getAppGrpPrimaryDocByConfigIdAndSeqNum(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos,
            String configId, int seqNum, String premVal, String premType) {
        log.debug(StringUtil.changeForLog("getAppGrpPrimaryDocByConfigIdAndSeqNum start..."));
        AppGrpPrimaryDocDto appGrpPrimaryDocDto = null;
        if (!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)) {
            log.debug("configId is {}", configId);
            log.debug("seqNum is {}", seqNum);
            log.debug("premIndex is {}", premVal);
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto1 : appGrpPrimaryDocDtos) {
                String currPremVal = "";
                String currPremType = "";
                if (!StringUtil.isEmpty(appGrpPrimaryDocDto1.getPremisessName())) {
                    currPremVal = appGrpPrimaryDocDto1.getPremisessName();
                }
                if (!StringUtil.isEmpty(appGrpPrimaryDocDto1.getPremisessType())) {
                    currPremType = appGrpPrimaryDocDto1.getPremisessType();
                }
                if (!StringUtil.isEmpty(appGrpPrimaryDocDto1.getFileRepoId())
                        && configId.equals(appGrpPrimaryDocDto1.getSvcComDocId())
                        && seqNum == appGrpPrimaryDocDto1.getSeqNum()
                        && premVal.equals(currPremVal)
                        && premType.equals(currPremType)) {
                    appGrpPrimaryDocDto = (AppGrpPrimaryDocDto) CopyUtil.copyMutableObject(appGrpPrimaryDocDto1);
                    break;
                }
            }
        }
        return appGrpPrimaryDocDto;
    }

    private void genPrimaryDoc(Map<String, File> fileMap, String docKey, HcsaSvcDocConfigDto hcsaSvcDocConfigDto,
            Map<String, File> saveFileMap, List<AppGrpPrimaryDocDto> currDocDtoList,
            List<AppGrpPrimaryDocDto> newAppGrpPrimaryDocDtoList, String premVal, String premType, boolean isRfi,
            List<AppGrpPrimaryDocDto> oldPrimaryDotList, String appGrpId, String appNo, String appType, String dupForPrem) {
        if (fileMap != null) {
            Set<Integer> nums = IaisCommonUtils.genNewHashSet();
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                String k = entry.getKey();
                File v = entry.getValue();
                int index = k.indexOf(docKey);
                String seqNumStr = k.substring(index + docKey.length());
                int seqNum = -1;
                try {
                    seqNum = Integer.parseInt(seqNumStr);
                } catch (Exception e) {
                    log.error(StringUtil.changeForLog("doc seq num can not parse to int"));
                }
                while (!nums.add(seqNum)) {
                    seqNum++;
                }
                AppGrpPrimaryDocDto primaryDocDto = new AppGrpPrimaryDocDto();
                if (v != null) {
                    primaryDocDto.setSvcComDocId(hcsaSvcDocConfigDto.getId());
                    primaryDocDto.setSvcComDocName(hcsaSvcDocConfigDto.getDocTitle());
                    primaryDocDto.setDocName(v.getName());
                    primaryDocDto.setRealDocSize(v.length());
                    long size = v.length() / 1024;
                    primaryDocDto.setDocSize(Integer.valueOf(String.valueOf(size)));
                    String md5Code = FileUtils.getFileMd5(v);
                    primaryDocDto.setMd5Code(md5Code);
                    //if  common ==> set null
                    primaryDocDto.setPremisessName(premVal);
                    primaryDocDto.setPremisessType(premType);
                    primaryDocDto.setSeqNum(seqNum);
                    primaryDocDto.setVersion(
                            getAppGrpPrimaryDocVersion(hcsaSvcDocConfigDto.getId(), oldPrimaryDotList, isRfi, md5Code, appGrpId, appNo,
                                    appType, seqNum, dupForPrem));
                    saveFileMap.put(premVal + hcsaSvcDocConfigDto.getId() + seqNum, v);
                } else {
                    primaryDocDto = getAppGrpPrimaryDocByConfigIdAndSeqNum(currDocDtoList, hcsaSvcDocConfigDto.getId(), seqNum,
                            premVal, premType);
                }
                //the data is retrieved from the DTO a second time
                fileMap.put(k, null);
                if (primaryDocDto != null) {
                    newAppGrpPrimaryDocDtoList.add(primaryDocDto);
                }
            }
        }
    }

    private void saveFileAndSetFileId(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList, Map<String, File> saveFileMap) {
        Map<String, File> passValidateFileMap = IaisCommonUtils.genNewLinkedHashMap();
        for (AppGrpPrimaryDocDto primaryDocDto : appGrpPrimaryDocDtoList) {
            if (primaryDocDto.isPassValidate()) {
                String premIndexNo = primaryDocDto.getPremisessName();
                if (StringUtil.isEmpty(premIndexNo)) {
                    premIndexNo = "";
                }
                String fileMapKey = premIndexNo + primaryDocDto.getSvcComDocId() + primaryDocDto.getSeqNum();
                File file = saveFileMap.get(fileMapKey);
                if (file != null) {
                    passValidateFileMap.put(fileMapKey, file);
                }
            }
        }
        log.info(StringUtil.changeForLog("Primary Doc size: " + appGrpPrimaryDocDtoList.size()));
        if (saveFileMap.size() != appGrpPrimaryDocDtoList.size()) {
            log.info(StringUtil.changeForLog("Save file size: " + saveFileMap.size()));
        }
        if (passValidateFileMap.size() > 0) {
            List<File> fileList = new ArrayList<>(passValidateFileMap.values());
            List<String> fileRepoIdList = configCommService.saveFileRepo(fileList);
            int fileRepo = fileRepoIdList.size();
            log.info(StringUtil.changeForLog("File Repo size: " + fileRepo));
            int i = 0;
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtoList) {
                String premIndexNo = appGrpPrimaryDocDto.getPremisessName();
                if (StringUtil.isEmpty(premIndexNo)) {
                    premIndexNo = "";
                }
                String saveFileMapKey = premIndexNo + appGrpPrimaryDocDto.getSvcComDocId() + appGrpPrimaryDocDto.getSeqNum();
                File file = passValidateFileMap.get(saveFileMapKey);
                if (file != null) {
                    appGrpPrimaryDocDto.setFileRepoId(fileRepoIdList.get(i));
                    i++;
                }
            }
        }

    }

    private AppGrpPrimaryDocDto getAppGrpPrimaryDocByConfigIdAndPremIndex(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos,
            String config, String premIndex) {
        AppGrpPrimaryDocDto appGrpPrimaryDocDto = null;
        if (!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)) {
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto1 : appGrpPrimaryDocDtos) {
                String currPremVal = appGrpPrimaryDocDto1.getPremisessName();
                if (StringUtil.isEmpty(currPremVal)) {
                    currPremVal = "";
                }
                if (config.equals(appGrpPrimaryDocDto1.getSvcComDocId()) && premIndex.equals(currPremVal)) {
                    appGrpPrimaryDocDto = appGrpPrimaryDocDto1;
                    break;
                }
            }
        }
        return appGrpPrimaryDocDto;
    }

}
