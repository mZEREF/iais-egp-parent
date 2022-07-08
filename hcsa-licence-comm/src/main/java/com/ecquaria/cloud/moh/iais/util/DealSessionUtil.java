package com.ecquaria.cloud.moh.iais.util;

import com.ecquaria.cloud.job.executor.util.SpringHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

            //set premises info
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();

            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            //set licseeId and psn drop down
            setLicseeAndPsnDropDown(appSubmissionDto.getLicenseeId(), appSvcRelatedInfoDtos, bpc.request);

            if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    String currentSvcId = appSvcRelatedInfoDto.getServiceId();
                    if (!StringUtil.isEmpty(currentSvcId)) {
                        //set doc name
                        List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                        List<HcsaSvcDocConfigDto> svcDocConfig = getConfigCommService().getAllHcsaSvcDocs(currentSvcId);
                        ApplicationHelper.setDocInfo(appSvcDocDtos, svcDocConfig);
                        //set dupForPrem info for not rfi rfc or renew
                        if (!isRfi && (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                                appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType))) {
                            if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                                String premTye = appGrpPremisesDtos.get(0).getPremisesType();
                                String premVal = appGrpPremisesDtos.get(0).getPremisesIndexNo();
                                for (AppSvcDocDto svcDocDto : appSvcDocDtos) {
                                    HcsaSvcDocConfigDto docConfig = ApplicationHelper.getHcsaSvcDocConfigDtoById(svcDocConfig,
                                            svcDocDto.getSvcDocId());
                                    if (docConfig != null && "1".equals(docConfig.getDupForPrem())) {
                                        svcDocDto.setPremisesVal(premVal);
                                        svcDocDto.setPremisesType(premTye);
                                    }
                                }
                            }
                        }
                    }
                    //set dpo select flag
                    List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                    if (!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)) {
                        for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtos) {
                            if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(appSvcPrincipalOfficersDto.getPsnType())) {
                                appSvcRelatedInfoDto.setDeputyPoFlag(AppConsts.YES);
                                break;
                            }
                        }
                    }
                }
            }
            if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType()) || isRfi) {
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

        //error_msg
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.ERRORMAP_PREMISES, null);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPGRPPRIMARYDOCERRMSGMAP, null);

        //init svc psn conifg
        Map<String, List<HcsaSvcPersonnelDto>> svcConfigInfo = null;
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.SERVICEALLPSNCONFIGMAP, (Serializable) svcConfigInfo);

        //clear primary file session
        AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        String appType = appSubmissionDto.getAppType();
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        //clear and set svc file session
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                String svcCode = appSvcRelatedInfoDto.getServiceCode();
                List<HcsaSvcDocConfigDto> svcDocConfigList = getConfigCommService().getAllHcsaSvcDocs(
                        appSvcRelatedInfoDto.getServiceId());
                //premIndex + config + svcCode
                Map<String, List<AppSvcDocDto>> svcDocMap = IaisCommonUtils.genNewHashMap();
                List<AppSvcDocDto> maxVersionSvcDocList = IaisCommonUtils.genNewArrayList();
                Set<String> psnIndexList = new HashSet<>(2);
                if (!IaisCommonUtils.isEmpty(appSvcDocDtos)) {
                    if (isRfi) {
                        maxVersionSvcDocList = getAppCommService().getMaxSeqNumSvcDocList(oldAppSubmissionDto.getAppGrpId());
                    }

                    for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                        String premIndex = appSvcDocDto.getPremisesVal();
                        if (StringUtil.isEmpty(premIndex)) {
                            premIndex = "";
                        }

                        String docMapKey = appSvcDocDto.getSvcDocId() + premIndex + svcCode;
                        List<AppSvcDocDto> appSvcDocDtos1 = svcDocMap.get(docMapKey);
                        if (IaisCommonUtils.isEmpty(appSvcDocDtos1)) {
                            appSvcDocDtos1 = IaisCommonUtils.genNewArrayList();
                        }
                        appSvcDocDtos1.add(appSvcDocDto);
                        svcDocMap.put(docMapKey, appSvcDocDtos1);
                        if (!StringUtil.isEmpty(appSvcDocDto.getPsnIndexNo())) {
                            psnIndexList.add(appSvcDocDto.getPsnIndexNo());
                        }
                    }
                }
                if (!IaisCommonUtils.isEmpty(svcDocConfigList)) {
                    for (int i = 0; i < svcDocConfigList.size(); i++) {
                        HcsaSvcDocConfigDto hcsaSvcDocConfigDto = svcDocConfigList.get(i);
                        String dupForPrem = hcsaSvcDocConfigDto.getDupForPrem();
                        String dupForPerson = hcsaSvcDocConfigDto.getDupForPerson();
                        String docMapKey;
                        if ("0".equals(dupForPrem)) {
                            docMapKey = hcsaSvcDocConfigDto.getId() + svcCode;
                            List<AppSvcDocDto> appSvcDocDtosList = svcDocMap.get(docMapKey);
                            String docSessionKey = i + "svcDoc" + svcCode;
                            setSvcDocSession(appSvcDocDtosList, docSessionKey, bpc.request, maxVersionSvcDocList, dupForPerson,
                                    psnIndexList);
                        } else if ("1".equals(dupForPrem)) {
                            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                                docMapKey = hcsaSvcDocConfigDto.getId() + appGrpPremisesDto.getPremisesIndexNo() + svcCode;
                                List<AppSvcDocDto> appSvcDocDtosList = svcDocMap.get(docMapKey);
                                String docSessionKey = i + "svcDoc" + svcCode + appGrpPremisesDto.getPremisesIndexNo();
                                setSvcDocSession(appSvcDocDtosList, docSessionKey, bpc.request, maxVersionSvcDocList, dupForPerson,
                                        psnIndexList);
                            }
                        }
                    }
                }
            }
        }

    }

    public static void setSvcDocSession(List<AppSvcDocDto> appSvcDocDtos, String docSessionKey, HttpServletRequest request,
            List<AppSvcDocDto> maxVersionSvcDocList, String dupForPerson, Set<String> psnIndexList) {
        if (appSvcDocDtos != null && appSvcDocDtos.size() > 0) {
            if (appSvcDocDtos.size() > 1) {
                Collections.sort(appSvcDocDtos, Comparator.comparing(AppSvcDocDto::getSeqNum));
            }
            Map<String, File> fileMap = IaisCommonUtils.genNewHashMap();
            Map<String, Map<String, File>> dupPsnFileMap = IaisCommonUtils.genNewHashMap();
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                int seqNum = appSvcDocDto.getSeqNum();
                String fileMapKey = docSessionKey + seqNum;
                fileMap.put(fileMapKey, null);
                String psnIndex = appSvcDocDto.getPsnIndexNo();
                if (!StringUtil.isEmpty(dupForPerson)) {
                    Map<String, File> psnFileMap = dupPsnFileMap.get(psnIndex);
                    if (psnFileMap == null) {
                        psnFileMap = IaisCommonUtils.genNewHashMap();
                    }
                    psnFileMap.put(docSessionKey + psnIndex + seqNum, null);
                    dupPsnFileMap.put(psnIndex, psnFileMap);
                }
            }
            String configId = appSvcDocDtos.get(0).getSvcDocId();
            int initSeqNum = appSvcDocDtos.get(appSvcDocDtos.size() - 1).getSeqNum() + 1;
            if (!IaisCommonUtils.isEmpty(maxVersionSvcDocList)) {
                for (AppSvcDocDto appSvcDocDto : maxVersionSvcDocList) {
                    int seqNum = appSvcDocDto.getSeqNum();
                    if (seqNum > initSeqNum && configId.equals(appSvcDocDto.getSvcDocId())) {
                        initSeqNum = seqNum;
                    }
                }
            }
            if (!StringUtil.isEmpty(dupForPerson)) {
                for (String psnIndex : psnIndexList) {
                    Map<String, File> psnFileMap = dupPsnFileMap.get(psnIndex);
                    String psnDocSessionKey = docSessionKey + psnIndex;
                    ParamUtil.setSessionAttr(request, IaisEGPConstant.SEESION_FILES_MAP_AJAX + psnDocSessionKey,
                            (Serializable) psnFileMap);
                    //ParamUtil.setSessionAttr(request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+psnDocSessionKey+HcsaFileAjaxController.SEESION_FILES_MAP_AJAX_MAX_INDEX,initSeqNum);
                }
            } else {
                ParamUtil.setSessionAttr(request, IaisEGPConstant.SEESION_FILES_MAP_AJAX + docSessionKey, (Serializable) fileMap);
                //ParamUtil.setSessionAttr(request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docSessionKey+HcsaFileAjaxController.SEESION_FILES_MAP_AJAX_MAX_INDEX,initSeqNum);
            }
        }
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

}
