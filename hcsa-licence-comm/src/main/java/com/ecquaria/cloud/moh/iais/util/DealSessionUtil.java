package com.ecquaria.cloud.moh.iais.util;

import com.ecquaria.cloud.job.executor.util.SpringHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocSecDetailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocumentShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSpecialServiceInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmFormDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AppDeclarationDocShowPageDto;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.PREMISESTYPE;

/**
 * @author Wenkang
 * @date 2021/4/14 16:33
 */
@Slf4j
public class DealSessionUtil {

    private static ConfigCommService getConfigCommService() {
        return SpringHelper.getBean(ConfigCommService.class);
    }

    private static LicCommService getLicCommService() {
        return SpringHelper.getBean(LicCommService.class);
    }

    private static OrganizationService getOrganizationService() {
        return SpringHelper.getBean(OrganizationService.class);
    }

    public static void clearSession(HttpServletRequest request) {
        if (request == null) {
            return;
        }
        HttpSession session = request.getSession();
        // New Application - Declaration - clear uploaded dto
        String fileAppendId = ApplicationHelper.getFileAppendId(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        session.removeAttribute(fileAppendId + "DocShowPageDto");
        session.removeAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId);
        // Request for Change
        fileAppendId = ApplicationHelper.getFileAppendId(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        session.removeAttribute(fileAppendId + "DocShowPageDto");
        session.removeAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId);
        // Cessation
        fileAppendId = ApplicationHelper.getFileAppendId(ApplicationConsts.APPLICATION_TYPE_CESSATION);
        session.removeAttribute(fileAppendId + "DocShowPageDto");
        session.removeAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId);
        // Renewal
        fileAppendId = ApplicationHelper.getFileAppendId(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
        session.removeAttribute(fileAppendId + "DocShowPageDto");
        session.removeAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId);
        // View and Print
        session.removeAttribute("viewPrint");

        //clear Session
        session.removeAttribute(HcsaAppConst.ACTION);
        session.removeAttribute(HcsaAppConst.ALL_SVC_NAMES);
        session.removeAttribute(HcsaAppConst.APPSUBMISSIONDTO);
        session.removeAttribute(HcsaAppConst.HCSASERVICEDTO);
        session.removeAttribute(RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        //Primary Documents
        session.removeAttribute(HcsaAppConst.COMMONHCSASVCDOCCONFIGDTO);
        session.removeAttribute(HcsaAppConst.PREMHCSASVCDOCCONFIGDTO);
        session.removeAttribute(HcsaAppConst.DRAFTCONFIG);
        Map<String, AppSvcPrincipalOfficersDto> psnMap = IaisCommonUtils.genNewHashMap();
        session.setAttribute(HcsaAppConst.PERSONSELECTMAP, psnMap);
        session.removeAttribute(AppServicesConsts.HCSASERVICEDTOLIST);
        session.removeAttribute(HcsaAppConst.HCSAS_GRP_SVC_LIST);

        session.removeAttribute("oldSubmitAppSubmissionDto");
        session.removeAttribute("submitAppSubmissionDto");
        session.removeAttribute("appSubmissionDtos");
        session.removeAttribute("rfiHcsaService");
        session.removeAttribute("ackPageAppSubmissionDto");
        session.removeAttribute("serviceConfig");
        session.removeAttribute("app-rfc-tranfer");
        session.removeAttribute("rfc_eqHciCode");
        session.removeAttribute("declaration_page_is");

        session.removeAttribute(HcsaAppConst.PREMISES_HCI_LIST);
        session.removeAttribute(HcsaAppConst.LICPERSONSELECTMAP);
        session.removeAttribute(HcsaAppConst.DASHBOARDTITLE);
        session.removeAttribute("AssessMentConfig");
        session.removeAttribute(HcsaAppConst.CURR_ORG_USER_ACCOUNT);
        session.removeAttribute(HcsaAppConst.PRIMARY_DOC_CONFIG);
        session.removeAttribute(HcsaAppConst.SVC_DOC_CONFIG);
        session.removeAttribute("app-rfc-tranfer");
        //initCoMap(false, request);
        //request For Information Loading
        session.removeAttribute(HcsaAppConst.REQUESTINFORMATIONCONFIG);
        session.removeAttribute("HcsaSvcSubtypeOrSubsumedDto");
        // CR: Licensee Details
        session.removeAttribute(HcsaAppConst.LICENSEE_MAP);
        session.removeAttribute(HcsaAppConst.RFC_APP_GRP_PREMISES_DTO_LIST);
        session.removeAttribute(HcsaAppConst.PREMISESTYPE);
        // CR: Split RFC Logic
        clearPremisesMap(request);
    }

//    public static Map<String, String> initCoMap(HttpServletRequest request) {
//        return initCoMap(true, request);
//    }

   /* public static Map<String, String> initCoMap(boolean withValue, HttpServletRequest request) {
//        HashMap<String, String> coMap = (HashMap<String, String>) ParamUtil.getSessionAttr(request, HcsaAppConst.CO_MAP);
//        if (coMap == null) {
//            coMap = IaisCommonUtils.genNewHashMap(5);
//        }
        HashMap<String, String> coMap = IaisCommonUtils.genNewHashMap(5);
        if (withValue) {
            coMap.put(HcsaAppConst.SECTION_LICENSEE, HcsaAppConst.SECTION_LICENSEE);
            coMap.put(HcsaAppConst.SECTION_PREMISES, HcsaAppConst.SECTION_PREMISES);
            coMap.put(HcsaAppConst.SECTION_SPECIALISED, HcsaAppConst.SECTION_SPECIALISED);
            coMap.put(HcsaAppConst.SECTION_SVCINFO, HcsaAppConst.SECTION_SVCINFO);
            coMap.put(HcsaAppConst.SECTION_PREVIEW, HcsaAppConst.SECTION_PREVIEW);
        } else {
            coMap.put(HcsaAppConst.SECTION_LICENSEE, "");
            coMap.put(HcsaAppConst.SECTION_PREMISES, "");
            coMap.put(HcsaAppConst.SECTION_SPECIALISED, "");
            coMap.put(HcsaAppConst.SECTION_SVCINFO, "");
            coMap.put(HcsaAppConst.SECTION_PREVIEW, "");
        }
        //ParamUtil.setSessionAttr(request, HcsaAppConst.CO_MAP, coMap);
        return coMap;
    }*/

    /*public static void loadCoMap(AppSubmissionDto appSubmissionDto, HttpServletRequest request) {
        List<String> stepColor = appSubmissionDto.getStepColor();
        if (stepColor != null) {
            HashMap<String, String> coMap = new HashMap<>(5);
            coMap.put(HcsaAppConst.SECTION_LICENSEE, "");
            coMap.put(HcsaAppConst.SECTION_PREMISES, "");
            coMap.put(HcsaAppConst.SECTION_SPECIALISED, "");
            coMap.put(HcsaAppConst.SECTION_SVCINFO, "");
            coMap.put(HcsaAppConst.SECTION_PREVIEW, "");
            if (!stepColor.isEmpty()) {
                for (String str : stepColor) {
                    if (HcsaAppConst.SECTION_LICENSEE.equals(str)) {
                        coMap.put(HcsaAppConst.SECTION_LICENSEE, str);
                    } else if (HcsaAppConst.SECTION_PREMISES.equals(str)) {
                        coMap.put(HcsaAppConst.SECTION_PREMISES, str);
                    } else if (HcsaAppConst.SECTION_SPECIALISED.equals(str)) {
                        coMap.put(HcsaAppConst.SECTION_SPECIALISED, str);
                    } else if (HcsaAppConst.SECTION_SVCINFO.equals(str)) {
                        coMap.put(HcsaAppConst.SECTION_SVCINFO, str);
                    } else if (HcsaAppConst.SECTION_PREVIEW.equals(str)) {
                        coMap.put(HcsaAppConst.SECTION_PREVIEW, str);
                    } else {
                        ParamUtil.setSessionAttr(request, "serviceConfig", str);
                    }
                }
            }
            ParamUtil.setSessionAttr(request, HcsaAppConst.CO_MAP, coMap);
        }
    }*/

    public static void clearPremisesMap(HttpServletRequest request) {
        request.getSession().removeAttribute(HcsaAppConst.LIC_PREMISES_MAP);
        request.getSession().removeAttribute(HcsaAppConst.APP_PREMISES_MAP);
        request.getSession().removeAttribute("premisesSelect");
        request.getSession().removeAttribute("conveyancePremSel");
        request.getSession().removeAttribute("offSitePremSel");
        request.getSession().removeAttribute("easMtsPremSel");
    }

    public static void initSession(boolean forceInit, HttpServletRequest request) throws CloneNotSupportedException {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, HcsaAppConst.APPSUBMISSIONDTO);
        List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,
                AppServicesConsts.HCSASERVICEDTOLIST);
        if (appSubmissionDto == null) {
            appSubmissionDto = new AppSubmissionDto();
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            List<AppGrpPremisesDto> appGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
            AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
            appGrpPremisesDtoList.add(appGrpPremisesDto);
            appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = IaisCommonUtils.genNewArrayList();
            AppSvcRelatedInfoDto appSvcRelatedInfoDto;
            for (HcsaServiceDto svc : hcsaServiceDtos) {
                appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                appSvcRelatedInfoDto.setServiceId(svc.getId());
                appSvcRelatedInfoDto.setServiceCode(svc.getSvcCode());
                appSvcRelatedInfoDto.setServiceType(svc.getSvcType());
                appSvcRelatedInfoDto.setServiceName(svc.getSvcName());
                appSvcRelatedInfoDtoList.add(appSvcRelatedInfoDto);
            }
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList);
        } else {
            boolean isRfi = ApplicationHelper.checkIsRfi(request);
            //set svc info,this fun will set oldAppSubmission
            appSubmissionDto = ApplicationHelper.setSubmissionDtoSvcData(request, appSubmissionDto);
            //Object rfi = ParamUtil.getSessionAttr(bpc.request, REQUESTINFORMATIONCONFIG);
            //rfi just show one service
            if (isRfi) {
                List<HcsaServiceDto> oneHcsaServiceDto = IaisCommonUtils.genNewArrayList();
                for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
                    if (hcsaServiceDto.getId().equals(appSubmissionDto.getRfiServiceId())) {
                        oneHcsaServiceDto.add(hcsaServiceDto);
                        break;
                    }
                }
                ParamUtil.setSessionAttr(request, "rfiHcsaService", (Serializable) CopyUtil.copyMutableObjectList(hcsaServiceDtos));
                hcsaServiceDtos = oneHcsaServiceDto;
                setHcsaServiceDtoList(oneHcsaServiceDto, request);
            }
            //set licseeId and psn drop down
            //setLicseeAndPsnDropDown(appSubmissionDto.getLicenseeId(), appSvcRelatedInfoDtos, bpc.request);
            /*if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType) || isRfi) {
                //set oldAppSubmission when rfi,rfc,rene
                if (isRfi) {
                    //groupLicencePremiseRelationDis(appSubmissionDto);
                } else {
//                    AppDeclarationMessageDto appDeclarationMessageDto = AppDataHelper.getAppDeclarationMessageDto(bpc.request,
//                            ApplicationConsts.APPLICATION_TYPE_RENEWAL);
//                    List<AppDeclarationDocDto> declarationFiles = AppDataHelper.getDeclarationFiles(
//                            ApplicationConsts.APPLICATION_TYPE_RENEWAL, bpc.request);
//                    appSubmissionDto.setAppDeclarationMessageDto(appDeclarationMessageDto);
//                    appSubmissionDto.setAppDeclarationDocDtos(declarationFiles);
                }
                AppSubmissionDto oldAppSubmissionDto = null;
                if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                    oldAppSubmissionDto = appSubmissionDto.getOldRenewAppSubmissionDto();
                    if (oldAppSubmissionDto == null) {
                        oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();
                    }
                }
                if (oldAppSubmissionDto == null) {
                    oldAppSubmissionDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDto);
                }
                ApplicationHelper.setOldAppSubmissionDto(oldAppSubmissionDto, bpc.request);
            } else if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
                AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();
                ApplicationHelper.setOldAppSubmissionDto(oldAppSubmissionDto, bpc.request);
            }*/
        }
        //String appType = appSubmissionDto.getAppType();
        //initPremiseTypes(hcsaServiceDtos, true, request);
        init(appSubmissionDto, hcsaServiceDtos, forceInit, request);

        //set licseeId and psn drop down
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        setLicseeAndPsnDropDown(ApplicationHelper.getLicenseeId(request), appSvcRelatedInfoDtos, request);

        AppEditSelectDto changeSelectDto = appSubmissionDto.getChangeSelectDto() == null ? new AppEditSelectDto() : appSubmissionDto.getChangeSelectDto();
        appSubmissionDto.setChangeSelectDto(changeSelectDto);

        ApplicationHelper.setAppSubmissionDto(appSubmissionDto, request);
        ParamUtil.setSessionAttr(request, "IndexNoCount", 0);

        //init svc psn conifg
        /*Map<String, List<HcsaSvcPersonnelDto>> svcConfigInfo = null;
        ParamUtil.setSessionAttr(request, HcsaAppConst.SERVICEALLPSNCONFIGMAP, (Serializable) svcConfigInfo);*/
    }

    public static List<HcsaServiceDto> getServiceConfigsFormApp(AppSubmissionDto appSubmissionDto) {
        if (appSubmissionDto == null) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<String> serviceConfigIds = IaisCommonUtils.genNewArrayList();
        List<String> names = IaisCommonUtils.genNewArrayList();
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
        ConfigCommService configCommService = getConfigCommService();
        List<HcsaServiceDto> hcsaServiceDtoList = null;
        if (!serviceConfigIds.isEmpty()) {
            hcsaServiceDtoList = configCommService.getHcsaServiceDtosByIds(serviceConfigIds);
        } else if (!names.isEmpty()) {
            hcsaServiceDtoList = configCommService.getActiveHcsaSvcByNames(names);
        }
        return hcsaServiceDtoList;
    }

    public static void setHcsaServiceDtoList(List<HcsaServiceDto> hcsaServiceDtoList, HttpServletRequest request) {
        ParamUtil.setSessionAttr(request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
    }

    public static Set<String> initPremiseTypes(List<HcsaServiceDto> hcsaServiceDtoList, boolean init, HttpServletRequest request) {
        Collection<String> collection = (Collection<String>) ParamUtil.getSessionAttr(request, PREMISESTYPE);
        if (!init && IaisCommonUtils.isNotEmpty(collection)) {
            return new HashSet<>(collection);
        }
        Set<String> premisesType = IaisCommonUtils.genNewHashSet();
        List<HcsaServiceDto> rfiHcsaService = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request, "rfiHcsaService");
        List<String> svcIds = IaisCommonUtils.genNewArrayList();
        if (rfiHcsaService != null) {
            rfiHcsaService.forEach(v -> svcIds.add(v.getId()));
        } else {
            if (hcsaServiceDtoList == null) {
                hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,
                        AppServicesConsts.HCSASERVICEDTOLIST);
            }
            if (hcsaServiceDtoList != null) {
                hcsaServiceDtoList.forEach(item -> svcIds.add(item.getId()));
            }
        }
        if (!IaisCommonUtils.isEmpty(svcIds)) {
            premisesType = getConfigCommService().getAppGrpPremisesTypeBySvcId(svcIds);
        } else {
            log.info(StringUtil.changeForLog("do not have select the services"));
        }
        ParamUtil.setSessionAttr(request, PREMISESTYPE, (Serializable) sortPremisesTypes(premisesType));
        return premisesType;
    }

    private static List<String> sortPremisesTypes(Collection<String> premisesTypes) {
        if (premisesTypes == null) {
            return IaisCommonUtils.genNewArrayList();
        }
        if (premisesTypes.size() <= 1) {
            return new ArrayList<>(premisesTypes);
        }
        return premisesTypes.stream()
                .sorted(Comparator.comparingInt(IaisCommonUtils::getPremSeqNum))
                .collect(Collectors.toList());
    }

    public static void setLicseeAndPsnDropDown(String licenseeId, List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,
            HttpServletRequest request) {
        Map<String, AppSvcPersonAndExtDto> licPersonMap = IaisCommonUtils.genNewHashMap();
        if (!StringUtil.isEmpty(licenseeId)) {
            //user account
            List<FeUserDto> feUserDtos = getOrganizationService().getFeUserDtoByLicenseeId(licenseeId);
            ParamUtil.setSessionAttr(request, HcsaAppConst.CURR_ORG_USER_ACCOUNT, (Serializable) feUserDtos);
            //existing person
            List<PersonnelListQueryDto> licPersonList = getLicCommService().getLicencePersonnelListQueryDto(licenseeId);
            ApplicationHelper.getLicPsnIntoSelMap(feUserDtos, licPersonList, licPersonMap);
            ParamUtil.setSessionAttr(request, HcsaAppConst.LICPERSONSELECTMAP, (Serializable) licPersonMap);
            //set data into psnMap
            Map<String, AppSvcPersonAndExtDto> personMap = IaisCommonUtils.genNewHashMap();
            personMap.putAll(licPersonMap);
            boolean loadCurrPsn = !IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)
                    && (ApplicationHelper.checkFromDraft(request) || ApplicationHelper.checkIsRfi(request));
            if (loadCurrPsn) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    String svcCode = appSvcRelatedInfoDto.getServiceCode();
                    ApplicationHelper.initSetPsnIntoSelMap(personMap,
                            appSvcRelatedInfoDto.getAppSvcCgoDtoList(), svcCode);
                    ApplicationHelper.initSetPsnIntoSelMap(personMap,
                            appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList(), svcCode);
                    ApplicationHelper.initSetPsnIntoSelMap(personMap,
                            appSvcRelatedInfoDto.getAppSvcNomineeDtoList(), svcCode);
                    ApplicationHelper.initSetPsnIntoSelMap(personMap,
                            appSvcRelatedInfoDto.getAppSvcMedAlertPersonList(), svcCode);
                    ApplicationHelper.initSetPsnIntoSelMap(personMap,
                            appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList(), svcCode);
                    ApplicationHelper.initSetPsnIntoSelMap(personMap,
                            appSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList(), svcCode);
                    List<AppSvcSpecialServiceInfoDto> appSvcSpecialServiceInfoList = appSvcRelatedInfoDto.getAppSvcSpecialServiceInfoList();
                    if (IaisCommonUtils.isNotEmpty(appSvcSpecialServiceInfoList)) {
                        for (AppSvcSpecialServiceInfoDto appSvcSpecialServiceInfoDto : appSvcSpecialServiceInfoList) {
                            List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcSpecialServiceInfoDto.getAppSvcCgoDtoList();
                            ApplicationHelper.initSetPsnIntoSelMap(personMap, appSvcCgoDtoList, svcCode);
                        }
                    }
                }
            }
            ParamUtil.setSessionAttr(request, HcsaAppConst.PERSONSELECTMAP, (Serializable) personMap);
        } else {
            ParamUtil.setSessionAttr(request, HcsaAppConst.LICPERSONSELECTMAP, (Serializable) licPersonMap);
            log.info(StringUtil.changeForLog("user info is empty....."));
        }
    }

    /*public static void initViewRequest(AppSubmissionDto appSubmissionDto, HttpServletRequest request) {
        if (appSubmissionDto == null || request == null) {
            return;
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = Optional.ofNullable(appSubmissionDto.getAppSvcRelatedInfoDtoList())
                .filter(IaisCommonUtils::isNotEmpty)
                .map(appSvcRelatedInfoDtoList -> appSvcRelatedInfoDtoList.get(0))
                .orElse(null);
        ParamUtil.setRequestAttr(request, "currentPreviewSvcInfo", appSvcRelatedInfoDto);
//        HcsaServiceDto hcsaServiceDto = getConfigCommService().getHcsaServiceDtoById(svcId);
//        ParamUtil.setRequestAttr(request, HcsaAppConst.HCSASERVICEDTO, hcsaServiceDto);
        String svcCode = Optional.ofNullable(appSubmissionDto.getAppPremSpecialisedDtoList())
                .filter(IaisCommonUtils::isNotEmpty)
                .map(appPremSpecialisedDtoList -> appPremSpecialisedDtoList.get(0).getBaseSvcCode())
                .orElse(null);
        ParamUtil.setRequestAttr(request, HcsaAppConst.SPECIALISED_SVC_CODE, svcCode);
    }*/

    public static AppSubmissionDto initView(AppSubmissionDto appSubmissionDto) {
        return init(appSubmissionDto, getServiceConfigsFormApp(appSubmissionDto), false, null);
    }

    public static AppSubmissionDto init(AppSubmissionDto appSubmissionDto, List<HcsaServiceDto> hcsaServiceDtos,
            boolean forceInit, HttpServletRequest request) {
        if (appSubmissionDto == null) {
            return null;
        }
        log.info(StringUtil.changeForLog("ForceInit: " + forceInit));
        String appType = appSubmissionDto.getAppType();
        //String licenceId = appSubmissionDto.getLicenceId();
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if (!IaisCommonUtils.isEmpty(appGrpPremisesDtoList)) {
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                if (StringUtil.isEmpty(appGrpPremisesDto.getOldHciCode())) {
                    appGrpPremisesDto.setOldHciCode(appGrpPremisesDto.getHciCode());
                }
                if (StringUtil.isEmpty(appGrpPremisesDto.getExistingData())) {
                    appGrpPremisesDto.setExistingData(AppConsts.NO);
                }
            }
        }
        appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
        initAppPremSpecialisedDtoList(appSubmissionDto, hcsaServiceDtos, forceInit);

        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for (AppSvcRelatedInfoDto currSvcInfoDto : appSvcRelatedInfoDtoList) {
            if (StringUtil.isEmpty(currSvcInfoDto.getApplicationType())) {
                currSvcInfoDto.setApplicationType(appType);
            }
            if (StringUtil.isEmpty(currSvcInfoDto.getLicenceId())) {
                currSvcInfoDto.setLicenceId(appSubmissionDto.getLicenceId());
            }
            init(currSvcInfoDto, appGrpPremisesDtoList, appSubmissionDto.getAppPremSpecialisedDtoList(), hcsaServiceDtos,
                    forceInit, request);
        }
        // preview
        List<AppDeclarationDocDto> appDeclarationDocDtos = appSubmissionDto.getAppDeclarationDocDtos();
        initDeclarationFiles(appDeclarationDocDtos, appType, request);

        appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList);
        if (appSubmissionDto.getCoMap() == null) {
            appSubmissionDto.setCoMap(ApplicationHelper.createCoMap(true));
        }
        return appSubmissionDto;
    }

    public static AppSvcRelatedInfoDto init(AppSvcRelatedInfoDto currSvcInfoDto, List<AppGrpPremisesDto> appGrpPremisesDtos,
            List<AppPremSpecialisedDto> appPremSpecialisedDtoList, List<HcsaServiceDto> hcsaServiceDtos,
            boolean forceInit, HttpServletRequest request) {
        if (currSvcInfoDto == null) {
            return null;
        }
        String svcId = currSvcInfoDto.getServiceId();
        /*if (!StringUtil.isEmpty(licenceId) && !newConfig) {
            String licAlignAppSvcId = "";
            List<LicAppCorrelationDto> licAppCorrelationDtos = getLicCommService().getLicCorrBylicId(licenceId);
            if (licAppCorrelationDtos != null && licAppCorrelationDtos.size() > 0) {
                LicAppCorrelationDto licAppCorrelationDto = licAppCorrelationDtos.get(0);
                ApplicationDto applicationDto = getAppCommService().getApplicationById(licAppCorrelationDto.getApplicationId());
                if (applicationDto != null) {
                    licAlignAppSvcId = applicationDto.getServiceId();
                }
            }
            if (!StringUtil.isEmpty(licAlignAppSvcId)) {
                svcId = licAlignAppSvcId;
            }
        }*/
        String name = currSvcInfoDto.getServiceName();
        HcsaServiceDto hcsaServiceDto = null;
        if (!StringUtil.isEmpty(svcId)) {
            hcsaServiceDto = getConfigCommService().getHcsaServiceDtoById(svcId);
        } else if (!StringUtil.isEmpty(name)) {
            hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(name);
        }
        if (hcsaServiceDto != null) {
            svcId = hcsaServiceDto.getId();
            currSvcInfoDto.setServiceId(svcId);
            currSvcInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
            currSvcInfoDto.setServiceType(hcsaServiceDto.getSvcType());
            currSvcInfoDto.setServiceName(hcsaServiceDto.getSvcName());
        }
        //set service step
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemes =
                getConfigCommService().getHcsaServiceStepSchemesByServiceId(svcId);
        currSvcInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemes);
        List<AppSvcPrincipalOfficersDto> dpoList = currSvcInfoDto.getAppSvcNomineeDtoList();
        if (IaisCommonUtils.isEmpty(dpoList)) {
            currSvcInfoDto.setDeputyPoFlag(AppConsts.NO);
        } else {
            currSvcInfoDto.setDeputyPoFlag(AppConsts.YES);
        }
        for (HcsaServiceStepSchemeDto hcsaServiceStepScheme : hcsaServiceStepSchemes) {
            String stepCode = hcsaServiceStepScheme.getStepCode();
            if (HcsaConsts.STEP_SUPPLEMENTARY_FORM.equals(stepCode)) {
                ApplicationHelper.initSupplementoryForm(currSvcInfoDto, appGrpPremisesDtos, forceInit);
                if (!forceInit) {
                    List<AppSvcSuplmFormDto> appSvcSuplmFormList = currSvcInfoDto.getAppSvcSuplmFormList();
                    if (IaisCommonUtils.isNotEmpty(appSvcSuplmFormList)) {
                        appSvcSuplmFormList.forEach(AppSvcSuplmFormDto::checkDisplay);
                    }
                }
            } else if (HcsaConsts.STEP_OTHER_INFORMATION.equals(stepCode)) {
                ApplicationHelper.initOtherInfoForm(currSvcInfoDto, appGrpPremisesDtos, forceInit,request);
                if (!forceInit) {
                    List<AppSvcOtherInfoDto> appSvcOtherInfoList = currSvcInfoDto.getAppSvcOtherInfoList();
                    if (IaisCommonUtils.isNotEmpty(appSvcOtherInfoList)) {
                        appSvcOtherInfoList.forEach(dto -> {
                            AppSvcSuplmFormDto appSvcSuplmFormDto = dto.getAppSvcSuplmFormDto();
                            if (appSvcSuplmFormDto != null) {
                                appSvcSuplmFormDto.checkDisplay();
                            }
                        });
                    }
                }
            } else if (HcsaConsts.STEP_SPECIAL_SERVICES_FORM.equals(stepCode)) {
                ApplicationHelper.initAppSvcSpecialServiceInfoDtoList(currSvcInfoDto, appPremSpecialisedDtoList);
                if (!forceInit) {
                    List<AppSvcSpecialServiceInfoDto> appSvcSpecialServiceInfoList = currSvcInfoDto.getAppSvcSpecialServiceInfoList();
                    if (IaisCommonUtils.isNotEmpty(appSvcSpecialServiceInfoList)) {
                        appSvcSpecialServiceInfoList.forEach(dto -> dto.getSpecialServiceSectionDtoList().forEach(sDto -> {
                            AppSvcSuplmFormDto appSvcSuplmFormDto = sDto.getAppSvcSuplmFormDto();
                            if (appSvcSuplmFormDto != null) {
                                appSvcSuplmFormDto.checkDisplay();
                            }
                        }));
                    }
                }
            } else if (HcsaConsts.STEP_DOCUMENTS.equals(stepCode)) {
                List<DocumentShowDto> documentShowDtos = ApplicationHelper.initShowDocumentList(currSvcInfoDto,
                        appPremSpecialisedDtoList, forceInit);
                initDocumentSession(documentShowDtos, request);
            }
        }
        return currSvcInfoDto;
    }

    public static List<AppPremSpecialisedDto> initAppPremSpecialisedDtoList(AppSubmissionDto appSubmissionDto,
            List<HcsaServiceDto> hcsaServiceDtoList, boolean forceInit) {
        if (appSubmissionDto == null) {
            return null;
        }
        if (IaisCommonUtils.isEmpty(hcsaServiceDtoList)) {
            appSubmissionDto.setAppPremSpecialisedDtoList(IaisCommonUtils.genNewArrayList());
            return appSubmissionDto.getAppPremSpecialisedDtoList();
        }
        List<AppPremSpecialisedDto> appPremSpecialisedDtos = appSubmissionDto.getAppPremSpecialisedDtoList();
        if (!forceInit && appPremSpecialisedDtos != null
                && appPremSpecialisedDtos.stream().allMatch(AppPremSpecialisedDto::isInit)) {
            return appPremSpecialisedDtos;
        }
        appPremSpecialisedDtos = genAppPremSpecialisedDtoList(appSubmissionDto.getAppGrpPremisesDtoList(),
                appSubmissionDto.getAppPremSpecialisedDtoList(), hcsaServiceDtoList);
        appSubmissionDto.setAppPremSpecialisedDtoList(appPremSpecialisedDtos);
        /*for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSubmissionDto.getAppSvcRelatedInfoDtoList()) {
            appSvcRelatedInfoDto.setDocumentShowDtoList(null);
        }*/
        return appPremSpecialisedDtos;
    }

    private static List<AppPremSpecialisedDto> genAppPremSpecialisedDtoList(List<AppGrpPremisesDto> appGrpPremisesDtos,
            List<AppPremSpecialisedDto> appPremSpecialisedDtos, List<HcsaServiceDto> baseServiceDtoList) {
        if (IaisCommonUtils.isEmpty(appGrpPremisesDtos) || IaisCommonUtils.isEmpty(baseServiceDtoList)) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<AppPremSpecialisedDto> result = IaisCommonUtils.genNewArrayList();
        for (HcsaServiceDto serviceDto : baseServiceDtoList) {
            ConfigCommService configCommService = getConfigCommService();
            List<HcsaSvcSpecifiedCorrelationDto> svcSpeCorrelationList = configCommService.getSvcSpeCorrelationsByBaseSvcId(
                    serviceDto.getId(), HcsaConsts.SERVICE_TYPE_SPECIFIED);
            List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeDtos = configCommService.listSubtype(serviceDto.getId());
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                AppPremSpecialisedDto appPremSpecialisedDto;
                if (appPremSpecialisedDtos != null) {
                    appPremSpecialisedDto = appPremSpecialisedDtos.stream()
                            .filter(dto -> Objects.equals(dto.getPremisesVal(), appGrpPremisesDto.getPremisesIndexNo())
                                    && (StringUtil.isNotEmpty(dto.getBaseSvcId()) && dto.getBaseSvcId().equals(serviceDto.getId())
                                    || StringUtil.isNotEmpty(dto.getBaseSvcName())
                                    && dto.getBaseSvcName().equals(serviceDto.getSvcName())))
                            .findAny()
                            .orElseGet(AppPremSpecialisedDto::new);
                } else {
                    appPremSpecialisedDto = new AppPremSpecialisedDto();
                }
                appPremSpecialisedDto.setAppGrpPremisesDto(appGrpPremisesDto);
                appPremSpecialisedDto.setBaseSvcConfigDto(serviceDto);
                appPremSpecialisedDto.setSvcSpecifiedCorrelationList(svcSpeCorrelationList);
                appPremSpecialisedDto.setSvcSubtypeList(hcsaSvcSubtypeDtos);
                appPremSpecialisedDto.setInit(true);
                result.add(appPremSpecialisedDto);
            }
        }
        return result;
    }

    private static void initDocumentSession(List<DocumentShowDto> documentShowDtos, HttpServletRequest request) {
        if (documentShowDtos == null || request == null) {
            return;
        }
        HttpSession session = request.getSession();
        for (DocumentShowDto documentShowDto : documentShowDtos) {
            for (DocSectionDto docSectionDto : documentShowDto.getDocSectionList()) {
                String svcCode = docSectionDto.getSvcCode();
                String premisesVal = documentShowDto.getPremisesVal();
                List<DocSecDetailDto> docSecDetailList = docSectionDto.getDocSecDetailList();
                int secSize = docSecDetailList.size();
                for (int j = 0; j < secSize; j++) {
                    String docKey = ApplicationHelper.getSvcDocKey(j, svcCode, premisesVal);
                    DocSecDetailDto docSecDetailDto = docSecDetailList.get(j);
                    if (docSecDetailDto.isExistDoc()) {
                        Map<String, Map<String, File>> fileMap = IaisCommonUtils.genNewHashMap();
                        for (AppSvcDocDto appSvcDocDto : docSecDetailDto.getAppSvcDocDtoList()) {
                            fileMap.put(docKey + appSvcDocDto.getSeqNum(), null);
                        }
                        session.setAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX + docKey, fileMap);
                    } else {
                        session.removeAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX + docKey);
                    }
                }
            }
        }
    }

    public static void initDeclarationFiles(List<AppDeclarationDocDto> appDeclarationDocDtos, String appType,
            HttpServletRequest request) {
        if (request == null) {
            request = MiscUtil.getCurrentRequest();
        }
        if (request == null) {
            log.info("The request is null!");
            return;
        }
        String fileAppendId = ApplicationHelper.getFileAppendId(appType);
        if (IaisCommonUtils.isEmpty(appDeclarationDocDtos)) {
            request.getSession().removeAttribute(fileAppendId + "DocShowPageDto");
            request.getSession().removeAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId);
            return;
        }
        AppDeclarationDocShowPageDto dto = (AppDeclarationDocShowPageDto) request.getSession().getAttribute(
                fileAppendId + "DocShowPageDto");
        if (Objects.nonNull(dto)) {
            return;
        }
        List<PageShowFileDto> pageShowFileDtos = IaisCommonUtils.genNewArrayList();
        HashMap<String, File> map = IaisCommonUtils.genNewHashMap();
        Map<String, PageShowFileDto> pageShowFileHashMap = IaisCommonUtils.genNewHashMap();
        for (AppDeclarationDocDto viewDoc : appDeclarationDocDtos) {
            String index = String.valueOf(Optional.ofNullable(viewDoc.getSeqNum()).orElse(0));
            PageShowFileDto pageShowFileDto = new PageShowFileDto();
            pageShowFileDto.setFileMapId(fileAppendId + "Div" + index);
            pageShowFileDto.setIndex(index);
            pageShowFileDto.setFileName(viewDoc.getDocName());
            pageShowFileDto.setSize(viewDoc.getDocSize());
            pageShowFileDto.setMd5Code(viewDoc.getMd5Code());
            pageShowFileDto.setFileUploadUrl(viewDoc.getFileRepoId());
            pageShowFileDto.setVersion(Optional.ofNullable(viewDoc.getVersion()).orElse(1));
            pageShowFileDtos.add(pageShowFileDto);
            map.put(fileAppendId + index, null);
            pageShowFileHashMap.put(fileAppendId + index, pageShowFileDto);
        }
        // put page entity to sesstion
        dto = new AppDeclarationDocShowPageDto();
        dto.setFileMaxIndex(appDeclarationDocDtos.size());
        dto.setPageShowFileDtos(pageShowFileDtos);
        dto.setPageShowFileHashMap(pageShowFileHashMap);
        request.getSession().setAttribute(fileAppendId + "DocShowPageDto", dto);
        request.getSession().setAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId, map);
    }

    //for single premises
    /*public static void addPremAlignForSvcDoc(List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos, List<AppSvcDocDto> appSvcDocDtos,
            List<AppGrpPremisesDto> appGrpPremisesDtos) {
        if (IaisCommonUtils.isEmpty(hcsaSvcDocConfigDtos) || IaisCommonUtils.isEmpty(appSvcDocDtos)
                || IaisCommonUtils.isEmpty(appGrpPremisesDtos) || appGrpPremisesDtos.size() != 1) {
            return;
        }
        for (HcsaSvcDocConfigDto config : hcsaSvcDocConfigDtos) {
            if ("1".equals(config.getDupForPrem())) {
                List<AppSvcDocDto> appSvcDocDtoList = getAppSvcDocDtoByConfigId(appSvcDocDtos, config.getId());
                if (!IaisCommonUtils.isEmpty(appSvcDocDtoList)) {
                    String premIndex = appGrpPremisesDtos.get(0).getPremisesIndexNo();
                    String premType = appGrpPremisesDtos.get(0).getPremisesType();
                    for (AppSvcDocDto appSvcDocDto : appSvcDocDtoList) {
                        appSvcDocDto.setPremisesType(premType);
                        appSvcDocDto.setPremisesVal(premIndex);
                    }
                }
            }
        }
    }*/

    private static List<AppSvcDocDto> getAppSvcDocDtoByConfigId(List<AppSvcDocDto> appSvcDocDtos, String configId) {
        List<AppSvcDocDto> appSvcDocDtoList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appSvcDocDtos) && !StringUtil.isEmpty(configId)) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                if (configId.equals(appSvcDocDto.getSvcDocId())) {
                    appSvcDocDtoList.add(appSvcDocDto);
                }
            }
        }
        return appSvcDocDtoList;
    }

    public static void reSetInit(AppSubmissionDto appSubmissionDto, String type) {
        if (HcsaAppConst.SECTION_PREMISES.equals(type)) {
            List<AppPremSpecialisedDto> appPremSpecialisedDtoList = appSubmissionDto.getAppPremSpecialisedDtoList();
            if (IaisCommonUtils.isNotEmpty(appPremSpecialisedDtoList)) {
                appPremSpecialisedDtoList.forEach(dto -> {
                    if (dto.isInit()) {
                        dto.setInit(false);
                    }
                });
            }
        }
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSubmissionDto.getAppSvcRelatedInfoDtoList()) {
            reSetInit(appSvcRelatedInfoDto, type);
        }
    }

    public static void reSetInit(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String type) {
        if (HcsaAppConst.SECTION_PREMISES.equals(type)) {
            List<AppSvcSuplmFormDto> appSvcSuplmFormList = appSvcRelatedInfoDto.getAppSvcSuplmFormList();
            if (IaisCommonUtils.isNotEmpty(appSvcSuplmFormList)) {
                appSvcSuplmFormList.forEach(dto -> {
                    if (dto.isInit()) {
                        dto.setInit(false);
                    }
                });
            }
            List<AppSvcOtherInfoDto> appSvcOtherInfoList = appSvcRelatedInfoDto.getAppSvcOtherInfoList();
            if (IaisCommonUtils.isNotEmpty(appSvcOtherInfoList)) {
                appSvcOtherInfoList.forEach(dto -> {
                    if (dto.isInit()) {
                        dto.setInit(false);
                    }
                });
            }
        }
        List<AppSvcSpecialServiceInfoDto> appSvcSpecialServiceInfoList = appSvcRelatedInfoDto.getAppSvcSpecialServiceInfoList();
        if (IaisCommonUtils.isNotEmpty(appSvcSpecialServiceInfoList)) {
            appSvcSpecialServiceInfoList.forEach(dto -> {
                if (dto.isInit()) {
                    dto.setInit(false);
                }
            });
        }
        appSvcRelatedInfoDto.setDocumentShowDtoList(null);
    }

}
