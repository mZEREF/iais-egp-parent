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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AppDataHelper;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/4/14 16:33
 */
@Slf4j
public class DealSessionUtil {

    private static ConfigCommService getConfigCommService() {
        return SpringHelper.getBean(ConfigCommService.class);
    }

    private static AppCommService getAppCommService() {
        return SpringHelper.getBean(AppCommService.class);
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
        String fileAppendId = AppDataHelper.getFileAppendId(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        session.removeAttribute(fileAppendId + "DocShowPageDto");
        session.removeAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId);
        // Request for Change
        fileAppendId = AppDataHelper.getFileAppendId(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        session.removeAttribute(fileAppendId + "DocShowPageDto");
        session.removeAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId);
        // Cessation
        fileAppendId = AppDataHelper.getFileAppendId(ApplicationConsts.APPLICATION_TYPE_CESSATION);
        session.removeAttribute(fileAppendId + "DocShowPageDto");
        session.removeAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId);
        // Renewal
        fileAppendId = AppDataHelper.getFileAppendId(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
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
        initCoMap(false, request);
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

    public static void initCoMap(HttpServletRequest request) {
        initCoMap(true, request);
    }

    public static void initCoMap(boolean withValue, HttpServletRequest request) {
        HashMap<String, String> coMap = (HashMap<String, String>) ParamUtil.getSessionAttr(request, HcsaAppConst.CO_MAP);
        if (coMap == null) {
            coMap = IaisCommonUtils.genNewHashMap(5);
        }
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
        ParamUtil.setSessionAttr(request, HcsaAppConst.CO_MAP, coMap);
    }

    public static void clearPremisesMap(HttpServletRequest request) {
        request.getSession().removeAttribute(HcsaAppConst.LIC_PREMISES_MAP);
        request.getSession().removeAttribute(HcsaAppConst.APP_PREMISES_MAP);
        request.getSession().removeAttribute("premisesSelect");
        request.getSession().removeAttribute("conveyancePremSel");
        request.getSession().removeAttribute("offSitePremSel");
        request.getSession().removeAttribute("easMtsPremSel");
    }

    public static void initSession(BaseProcessClass bpc) throws CloneNotSupportedException {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO);
        if (appSubmissionDto == null) {
            appSubmissionDto = new AppSubmissionDto();
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            List<AppGrpPremisesDto> appGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
            AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
            appGrpPremisesDtoList.add(appGrpPremisesDto);
            appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,
                    AppServicesConsts.HCSASERVICEDTOLIST);
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
            //set licseeId and psn drop down
            setLicseeAndPsnDropDown(ApplicationHelper.getLicenseeId(bpc.request), appSvcRelatedInfoDtoList, bpc.request);
        } else {
            String appType = appSubmissionDto.getAppType();
            boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
            //set svc info,this fun will set oldAppSubmission
            appSubmissionDto = ApplicationHelper.setSubmissionDtoSvcData(bpc.request, appSubmissionDto);
            //Object rfi = ParamUtil.getSessionAttr(bpc.request, REQUESTINFORMATIONCONFIG);
            //rfi just show one service
            if (isRfi) {
                List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,
                        AppServicesConsts.HCSASERVICEDTOLIST);
                List<HcsaServiceDto> oneHcsaServiceDto = IaisCommonUtils.genNewArrayList();
                for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
                    if (hcsaServiceDto.getId().equals(appSubmissionDto.getRfiServiceId())) {
                        oneHcsaServiceDto.add(hcsaServiceDto);
                        break;
                    }
                }
                ParamUtil.setSessionAttr(bpc.request, "rfiHcsaService", (Serializable) hcsaServiceDtos);
                ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) oneHcsaServiceDto);
            }
            init(appSubmissionDto, true, bpc.request);

            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            //set licseeId and psn drop down
            setLicseeAndPsnDropDown(appSubmissionDto.getLicenseeId(), appSvcRelatedInfoDtos, bpc.request);

            if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType) || isRfi) {
                //set oldAppSubmission when rfi,rfc,rene
                if (isRfi) {
                    //groupLicencePremiseRelationDis(appSubmissionDto);
                } else {
                    AppDeclarationMessageDto appDeclarationMessageDto = AppDataHelper.getAppDeclarationMessageDto(bpc.request,
                            ApplicationConsts.APPLICATION_TYPE_RENEWAL);
                    List<AppDeclarationDocDto> declarationFiles = AppDataHelper.getDeclarationFiles(
                            ApplicationConsts.APPLICATION_TYPE_RENEWAL, bpc.request);
                    appSubmissionDto.setAppDeclarationMessageDto(appDeclarationMessageDto);
                    appSubmissionDto.setAppDeclarationDocDtos(declarationFiles);
                }
                AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDto);
                ApplicationHelper.setOldAppSubmissionDto(oldAppSubmissionDto, bpc.request);
            } else if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
                AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();
                ApplicationHelper.setOldAppSubmissionDto(oldAppSubmissionDto, bpc.request);
            }
        }

        AppEditSelectDto changeSelectDto1 = appSubmissionDto.getChangeSelectDto() == null ? new AppEditSelectDto() : appSubmissionDto.getChangeSelectDto();
        appSubmissionDto.setChangeSelectDto(changeSelectDto1);

        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, "IndexNoCount", 0);

        //init svc psn conifg
        Map<String, List<HcsaSvcPersonnelDto>> svcConfigInfo = null;
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.SERVICEALLPSNCONFIGMAP, (Serializable) svcConfigInfo);
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
            licPersonMap = ApplicationHelper.getLicPsnIntoSelMap(feUserDtos, licPersonList, licPersonMap);
            ParamUtil.setSessionAttr(request, HcsaAppConst.LICPERSONSELECTMAP, (Serializable) licPersonMap);
            //set data into psnMap
            Map<String, AppSvcPersonAndExtDto> personMap = IaisCommonUtils.genNewHashMap();
            personMap.putAll(licPersonMap);
            if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)
                    && (ApplicationHelper.checkFromDraft(request) || ApplicationHelper.checkIsRfi(request))) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    String svcCode = appSvcRelatedInfoDto.getServiceCode();
                    personMap = ApplicationHelper.initSetPsnIntoSelMap(personMap,
                            appSvcRelatedInfoDto.getAppSvcCgoDtoList(), svcCode);
                    personMap = ApplicationHelper.initSetPsnIntoSelMap(personMap,
                            appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList(), svcCode);
                    personMap = ApplicationHelper.initSetPsnIntoSelMap(personMap,
                            appSvcRelatedInfoDto.getAppSvcNomineeDtoList(), svcCode);
                    personMap = ApplicationHelper.initSetPsnIntoSelMap(personMap,
                            appSvcRelatedInfoDto.getAppSvcMedAlertPersonList(), svcCode);
                    personMap = ApplicationHelper.initSetPsnIntoSelMap(personMap,
                            appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList(), svcCode);
                    personMap = ApplicationHelper.initSetPsnIntoSelMap(personMap,
                            appSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList(), svcCode);
                }
            }
            ParamUtil.setSessionAttr(request, HcsaAppConst.PERSONSELECTMAP, (Serializable) personMap);
        } else {
            ParamUtil.setSessionAttr(request, HcsaAppConst.LICPERSONSELECTMAP, (Serializable) licPersonMap);
            log.info(StringUtil.changeForLog("user info is empty....."));
        }
    }

    public static AppSubmissionDto init(AppSubmissionDto appSubmissionDto) {
        return init(appSubmissionDto, true, null);
    }

    public static AppSubmissionDto init(AppSubmissionDto appSubmissionDto, boolean newConfig, HttpServletRequest request) {
        if(appSubmissionDto == null) {
            return appSubmissionDto;
        }
        String licenceId = appSubmissionDto.getLicenceId();
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

        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,
                AppServicesConsts.HCSASERVICEDTOLIST);
        ApplicationHelper.initAppPremSpecialisedDtoList(appSubmissionDto, hcsaServiceDtoList);

        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for (AppSvcRelatedInfoDto currSvcInfoDto : appSvcRelatedInfoDtoList) {
            init(currSvcInfoDto, appGrpPremisesDtoList, appSubmissionDto.getAppPremSpecialisedDtoList(),
                    licenceId, newConfig, request);
        }
        appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList);
        return appSubmissionDto;
    }

    public static AppSvcRelatedInfoDto init(AppSvcRelatedInfoDto currSvcInfoDto, List<AppGrpPremisesDto> appGrpPremisesDtos,
            List<AppPremSpecialisedDto> appPremSpecialisedDtoList, String licenceId, boolean newConfig, HttpServletRequest request) {
        if (currSvcInfoDto == null) {
            return currSvcInfoDto;
        }
        String svcId = currSvcInfoDto.getServiceId();
        if (!StringUtil.isEmpty(licenceId) && !newConfig) {
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
        }
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
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId =
                getConfigCommService().getHcsaServiceStepSchemesByServiceId(svcId);
        currSvcInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
        List<AppSvcPrincipalOfficersDto> dpoList = currSvcInfoDto.getAppSvcNomineeDtoList();
        if (IaisCommonUtils.isEmpty(dpoList)) {
            currSvcInfoDto.setDeputyPoFlag(AppConsts.NO);
        } else {
            currSvcInfoDto.setDeputyPoFlag(AppConsts.YES);
        }

        ApplicationHelper.initSupplementoryForm(currSvcInfoDto, true, HcsaConsts.SUPPLEMENTARY_FORM);

        List<HcsaSvcDocConfigDto> svcDocConfigDtos = getConfigCommService().getAllHcsaSvcDocs(svcId);
        addPremAlignForSvcDoc(svcDocConfigDtos, currSvcInfoDto.getAppSvcDocDtoLit(), appGrpPremisesDtos);
        List<DocumentShowDto> documentShowDtos = ApplicationHelper.initShowDocumentList(currSvcInfoDto, appPremSpecialisedDtoList);
        if (documentShowDtos != null && request != null) {
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
        return currSvcInfoDto;
    }

    //for single premises
    public static void addPremAlignForSvcDoc(List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos, List<AppSvcDocDto> appSvcDocDtos,
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
    }

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
}
