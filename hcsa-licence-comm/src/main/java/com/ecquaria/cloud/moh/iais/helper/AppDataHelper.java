package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.job.executor.util.SpringHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocSecDetailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocumentShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.SpecialServiceSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremNonLicRelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremOutSourceLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPsnEditDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoAbortDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoMedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoNurseDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoTopDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoTopPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSpecialServiceInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmFormDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.OperationHoursReloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.RegistrationDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ReflectionUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AppDeclarationDocShowPageDto;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.client.ComSystemAdminClient;
import lombok.extern.slf4j.Slf4j;
import sop.iwe.SessionManager;
import sop.rbac.user.User;
import sop.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Time;
import java.text.Collator;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.CURRENTSERVICEID;

/**
 * @Auther chenlei on 5/4/2022.
 */
@Slf4j
public final class AppDataHelper {

    private static LicCommService licCommService;
    private static ConfigCommService configCommService;
    private static AppCommService appCommService;

    private static ConfigCommService getConfigCommService() {
        if (configCommService == null) {
            configCommService = SpringHelper.getBean(ConfigCommService.class);
        }
        return configCommService;
    }

    private static AppCommService getAppCommService() {
        if (appCommService == null) {
            appCommService = SpringHelper.getBean(AppCommService.class);
        }
        return appCommService;
    }

    private static LicCommService getLicCommService() {
        if (licCommService == null) {
            licCommService = SpringHelper.getBean(LicCommService.class);
        }
        return licCommService;
    }

    public static SubLicenseeDto getSubLicenseeDtoDetailFromPage(HttpServletRequest request) {
        String idType = ParamUtil.getString(request, "idType");
        String idNumber = ParamUtil.getString(request, "idNumber");
        String licenseeName = ParamUtil.getString(request, "licenseeName");
        String postalCode = ParamUtil.getString(request, "postalCode");
        String addrType = ParamUtil.getString(request, "addrType");
        String blkNo = ParamUtil.getString(request, "blkNo");
        String floorNo = ParamUtil.getString(request, "floorNo");
        String unitNo = ParamUtil.getString(request, "unitNo");
        String streetName = ParamUtil.getString(request, "streetName");
        String buildingName = ParamUtil.getString(request, "buildingName");
        String telephoneNo = ParamUtil.getString(request, "telephoneNo");
        String emailAddr = ParamUtil.getString(request, "emailAddr");
        String nationality = ParamUtil.getString(request, "nationality");

        SubLicenseeDto dto = new SubLicenseeDto();
        dto.setIdType(idType);
        dto.setIdNumber(StringUtil.toUpperCase(idNumber));
        dto.setLicenseeName(licenseeName);
        dto.setPostalCode(postalCode);
        dto.setAddrType(addrType);
        dto.setBlkNo(blkNo);
        dto.setFloorNo(IaisCommonUtils.getFloorNo(floorNo));
        dto.setUnitNo(unitNo);
        dto.setStreetName(streetName);
        dto.setBuildingName(buildingName);
        dto.setTelephoneNo(telephoneNo);
        dto.setEmailAddr(emailAddr);
        dto.setNationality(nationality);
        return dto;
    }

    /**
     * @description: get data from page
     * @author: zixian
     * @date: 11/6/2019 5:05 PM
     * @param: singlePrem
     * @param: request
     * @return: AppGrpPremisesDto
     */
    public static List<AppGrpPremisesDto> genAppGrpPremisesDtoList(boolean singlePrem, HttpServletRequest request) {
        //AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
        /*boolean readonly = ApplicationHelper.readonlyPremises(appSubmissionDto);
        if (readonly) {
            return appSubmissionDto.getAppGrpPremisesDtoList();
        }*/
        List<AppGrpPremisesDto> appGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
        /*List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,
                AppServicesConsts.HCSASERVICEDTOLIST);
        boolean isMultiPremService = ApplicationHelper.isMultiPremService(hcsaServiceDtoList);*/
        String[] premisesIndexNos = ParamUtil.getStrings(request, "premisesIndexNo");
        String[] chooseExistData = ParamUtil.getStrings(request, "chooseExistData");
        String[] premTypeValue = ParamUtil.getStrings(request, "premType");
        String[] premSelValue = ParamUtil.getStrings(request, "premSelValue");
        String[] isParyEdit = ParamUtil.getStrings(request, "isParyEdit");
        int count = premisesIndexNos.length;
        if (singlePrem) {
            count = 1;
        }
        for (int i = 0; i < count; i++) {
            String premType = ParamUtil.getString(request, "premType" + i);
            if (StringUtil.isEmpty(premType)) {
                premType = getVal(premTypeValue, i);
            }
            String premisesSel = "";
            if (ApplicationConsts.PREMISES_TYPE_PERMANENT.equals(premType)) {
                premisesSel = ParamUtil.getString(request, "permanentSel" + i);
            } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premType)) {
                premisesSel = ParamUtil.getString(request, "conveyanceSel" + i);
            } else if (ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premType)) {
                premisesSel = ParamUtil.getString(request, "easMtsSel" + i);
            } else if (ApplicationConsts.PREMISES_TYPE_MOBILE.equals(premType)) {
                premisesSel = ParamUtil.getString(request, "mobileSel" + i);
            } else if (ApplicationConsts.PREMISES_TYPE_REMOTE.equals(premType)) {
                premisesSel = ParamUtil.getString(request, "remoteSel" + i);
            }
            if (StringUtil.isEmpty(premisesSel)) {
                premisesSel = getVal(premSelValue, i);
            }
            String premIndexNo = getVal(premisesIndexNos, i);
            if (StringUtil.isEmpty(premIndexNo)) {
                log.info(StringUtil.changeForLog("New premise index"));
                premIndexNo = UUID.randomUUID().toString();
            }
            String existingData = getVal(chooseExistData, i);
            // data
            AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
            // check
            AppGrpPremisesDto licPremise = ApplicationHelper.getPremisesFromMap(premisesSel, request);
            if (AppConsts.YES.equals(existingData)) {
                setDataFromExisting(appGrpPremisesDto, licPremise);
            }
            // edit current or edit existed data
            if (!AppConsts.YES.equals(existingData) || AppConsts.YES.equals(getVal(isParyEdit, i))) {
                if (licPremise != null) {
                    appGrpPremisesDto.setRelatedServices(licPremise.getRelatedServices());
                    appGrpPremisesDto.setHciCode(licPremise.getHciCode());
                }
                setAppGrpPremiseNonAutoFields(appGrpPremisesDto, premIndexNo, premType, i, request);
            }
            setAppGrpPremiseFromPage(appGrpPremisesDto, premIndexNo, i, request);
            // rfc and renewal
            String[] selectedLicences = ParamUtil.getStrings(request, "selectedLicence");
            appGrpPremisesDto.setSelectedLicences(selectedLicences);
            appGrpPremisesDto.setSeqNum(i + 1);
            appGrpPremisesDto.setHasError(null);
            appGrpPremisesDto.setExistingData(existingData);
            appGrpPremisesDto.setPremisesIndexNo(premIndexNo);
            appGrpPremisesDto.setPremisesType(premType);
            appGrpPremisesDto.setPremisesSelect(premisesSel);
            appGrpPremisesDtoList.add(appGrpPremisesDto);
        }
        return appGrpPremisesDtoList;
    }

    private static void setAppGrpPremiseNonAutoFields(AppGrpPremisesDto appGrpPremisesDto, String premIndexNo,
            String premType, int i, HttpServletRequest request) {
        String[] opLengths = ParamUtil.getStrings(request, "opLength");
        String[] retrieveflag = ParamUtil.getStrings(request, "retrieveflag");
        appGrpPremisesDto.setClickRetrieve(AppConsts.YES.equals(getVal(retrieveflag, i)));
        String floorNo = ParamUtil.getString(request, i + "FloorNo" + 0);
        String unitNo = ParamUtil.getString(request, i + "UnitNo" + 0);
        appGrpPremisesDto.setFloorNo(IaisCommonUtils.getFloorNo(floorNo));
        appGrpPremisesDto.setUnitNo(unitNo);

        List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = IaisCommonUtils.genNewArrayList();
        int opLength = 0;
        try {
            opLength = Integer.parseInt(opLengths[i]);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("operation length can not parse to int"));
        }
        for (int k = 1; k < opLength; k++) {
            floorNo = ParamUtil.getString(request, i + "FloorNo" + k);
            unitNo = ParamUtil.getString(request, i + "UnitNo" + k);
            if (StringUtil.isEmpty(floorNo) && StringUtil.isEmpty(unitNo)) {
                continue;
            }
            AppPremisesOperationalUnitDto dto = new AppPremisesOperationalUnitDto();
            dto.setFloorNo(IaisCommonUtils.getFloorNo(floorNo));
            dto.setUnitNo(unitNo);
            dto.setPremType(premType);
            dto.setPremVal(premIndexNo);
            dto.setSeqNum(k);
            appPremisesOperationalUnitDtos.add(dto);
        }
        appGrpPremisesDto.setAppPremisesOperationalUnitDtos(appPremisesOperationalUnitDtos);
        appGrpPremisesDto.setAppPremisesOperationalUnitDtos(appPremisesOperationalUnitDtos);
    }

    private static void setAppGrpPremiseFromPage(AppGrpPremisesDto appGrpPremisesDto, String premIndexNo,
            int i, HttpServletRequest request) {
        String[] nonHcsaLengths = ParamUtil.getStrings(request, "nonHcsaLength");
        ControllerHelper.get(request, appGrpPremisesDto, String.valueOf(i));
        String certIssuedDtStr = ParamUtil.getString(request, "certIssuedDt" + i);
        appGrpPremisesDto.setCertIssuedDtStr(certIssuedDtStr);
        setPremise(appGrpPremisesDto, premIndexNo, ApplicationHelper.getOldAppSubmissionDto(request));
        List<AppPremNonLicRelationDto> appPremNonLicRelationDtos = IaisCommonUtils.genNewArrayList();
        int nonHcsaLength = 0;
        try {
            nonHcsaLength = Integer.parseInt(nonHcsaLengths[i]);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("Non-hcsa service length can not parse to int"));
        }
        for (int k = 0; k < nonHcsaLength; k++) {
            String coBusinessName = ParamUtil.getString(request, i + "CoBusinessName" + k);
            String coSvcName = ParamUtil.getString(request, i + "CoSvcName" + k);
            if (StringUtil.isEmpty(coBusinessName) && StringUtil.isEmpty(coSvcName)) {
                continue;
            }
            AppPremNonLicRelationDto dto = new AppPremNonLicRelationDto();
            dto.setBusinessName(coBusinessName);
            dto.setProvidedService(coSvcName);
            dto.setSeqNum(k);
            appPremNonLicRelationDtos.add(dto);
        }
        appGrpPremisesDto.setAppPremNonLicRelationDtos(appPremNonLicRelationDtos);
    }

    private static void setPremise(AppGrpPremisesDto appGrpPremisesDto, String premIndexNo, AppSubmissionDto oldAppSubmissionDto) {
        String oldHciCode = null;
        List<LicenceDto> licenceDtos = null;
        if (oldAppSubmissionDto != null && oldAppSubmissionDto.getAppGrpPremisesDtoList() != null) {
            oldHciCode = oldAppSubmissionDto.getAppGrpPremisesDtoList().stream()
                    .filter(dto -> Objects.equals(premIndexNo, dto.getPremisesIndexNo()))
                    .map(AppGrpPremisesDto::getHciCode)
                    .filter(Objects::nonNull)
                    .findAny()
                    .orElse(null);
            licenceDtos = oldAppSubmissionDto.getAppGrpPremisesDtoList().stream()
                    .filter(dto -> Objects.equals(premIndexNo, dto.getPremisesIndexNo()))
                    .map(AppGrpPremisesDto::getLicenceDtos)
                    .filter(Objects::nonNull)
                    .findAny()
                    .orElse(null);
            appGrpPremisesDto.setLicenceDtos(licenceDtos);
        }
        log.info(StringUtil.changeForLog("--- Old Hci Code: " + oldHciCode));
        log.info(StringUtil.changeForLog("--- Maybe Affected Licence size: " + (licenceDtos == null ? 0 : licenceDtos.size())));
        appGrpPremisesDto.setHciCode(oldHciCode);
        appGrpPremisesDto.setPremisesIndexNo(premIndexNo);
    }

    private static void setDataFromExisting(AppGrpPremisesDto dto, AppGrpPremisesDto licPremise) {
        dto.setPremisesSelect(licPremise.getPremisesSelect());
        dto.setHciName(licPremise.getHciName());
        dto.setPostalCode(licPremise.getPostalCode());
        dto.setAddrType(licPremise.getAddrType());
        dto.setBlkNo(licPremise.getBlkNo());
        dto.setFloorNo(licPremise.getFloorNo());
        dto.setUnitNo(licPremise.getUnitNo());
        dto.setAppPremisesOperationalUnitDtos(MiscUtil.transferEntityDtos(licPremise.getAppPremisesOperationalUnitDtos(),
                AppPremisesOperationalUnitDto.class));
        dto.setStreetName(licPremise.getStreetName());
        dto.setBuildingName(licPremise.getBuildingName());
        dto.setHciCode(licPremise.getHciCode());
        dto.setRelatedServices(licPremise.getRelatedServices());
    }

    private static String genPageName(Object prefix, String name, Object suffix) {
        return prefix + name + suffix;
    }

    public static AppDeclarationMessageDto getAppDeclarationMessageDto(HttpServletRequest request, String type) {
        AppDeclarationMessageDto appDeclarationMessageDto = new AppDeclarationMessageDto();
        appDeclarationMessageDto.setAppType(type);
        appDeclarationMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(type)) {
            String preliminaryQuestionKindly = request.getParameter("preliminaryQuestionKindly");
            String preliminaryQuestionItem1 = request.getParameter("preliminaryQuestionItem1");
            String preliminaryQuestiontem2 = request.getParameter("preliminaryQuestiontem2");
            String effectiveDt = request.getParameter("effectiveDt");
            appDeclarationMessageDto.setPreliminaryQuestionKindly(preliminaryQuestionKindly);
            appDeclarationMessageDto.setPreliminaryQuestionItem1(preliminaryQuestionItem1);
            appDeclarationMessageDto.setPreliminaryQuestiontem2(preliminaryQuestiontem2);
            if (CommonValidator.isDate(effectiveDt)) {
                try {
                    appDeclarationMessageDto.setEffectiveDt(Formatter.parseDate(effectiveDt));
                } catch (ParseException e) {
                    log.warn(StringUtil.changeForLog(e.getMessage()), e);
                }
            }
        } else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(type)) {
            String preliminaryQuestionKindly = request.getParameter("preliminaryQuestionKindly");
            appDeclarationMessageDto.setPreliminaryQuestionKindly(preliminaryQuestionKindly);
            String bankruptcyItem1 = request.getParameter("bankruptcyItem1");
            appDeclarationMessageDto.setBankruptcyItem1(bankruptcyItem1);
            String bankruptcyItem2 = request.getParameter("bankruptcyItem2");
            appDeclarationMessageDto.setBankruptcyItem2(bankruptcyItem2);
            String bankruptcyItem3 = request.getParameter("bankruptcyItem3");
            appDeclarationMessageDto.setBankruptcyItem3(bankruptcyItem3);
            String bankruptcyItem4 = request.getParameter("bankruptcyItem4");
            appDeclarationMessageDto.setBankruptcyItem4(bankruptcyItem4);
            String bankruptcyRemark = request.getParameter("bankruptcyRemark");
            appDeclarationMessageDto.setBankruptcyRemark(bankruptcyRemark);
            String competenciesItem1 = request.getParameter("competenciesItem1");
            appDeclarationMessageDto.setCompetenciesItem1(competenciesItem1);
            String competenciesItem2 = request.getParameter("competenciesItem2");
            appDeclarationMessageDto.setCompetenciesItem2(competenciesItem2);
            String competenciesItem3 = request.getParameter("competenciesItem3");
            appDeclarationMessageDto.setCompetenciesItem3(competenciesItem3);
            String competenciesRemark = request.getParameter("competenciesRemark");
            appDeclarationMessageDto.setCompetenciesRemark(competenciesRemark);
            String criminalRecordsItem1 = request.getParameter("criminalRecordsItem1");
            appDeclarationMessageDto.setCriminalRecordsItem1(criminalRecordsItem1);
            String criminalRecordsItem2 = request.getParameter("criminalRecordsItem2");
            appDeclarationMessageDto.setCriminalRecordsItem2(criminalRecordsItem2);
            String criminalRecordsItem3 = request.getParameter("criminalRecordsItem3");
            appDeclarationMessageDto.setCriminalRecordsItem3(criminalRecordsItem3);
            String criminalRecordsItem4 = request.getParameter("criminalRecordsItem4");
            appDeclarationMessageDto.setCriminalRecordsItem4(criminalRecordsItem4);
            String criminalRecordsRemark = request.getParameter("criminalRecordsRemark");
            appDeclarationMessageDto.setCriminalRecordsRemark(criminalRecordsRemark);
            String generalAccuracyItem1 = request.getParameter("generalAccuracyItem1");
            appDeclarationMessageDto.setGeneralAccuracyItem1(generalAccuracyItem1);

        } else if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(type)) {
            // Preliminary Question
            String preliminaryQuestionKindly = request.getParameter("preliminaryQuestionKindly");
            appDeclarationMessageDto.setPreliminaryQuestionKindly(preliminaryQuestionKindly);
            // Declaration on Bankruptcy
            appDeclarationMessageDto.setBankruptcyItem1(ParamUtil.getString(request, "bankruptcyItem1"));
            appDeclarationMessageDto.setBankruptcyItem2(ParamUtil.getString(request, "bankruptcyItem2"));
            appDeclarationMessageDto.setBankruptcyItem3(ParamUtil.getString(request, "bankruptcyItem3"));
            appDeclarationMessageDto.setBankruptcyItem4(ParamUtil.getString(request, "bankruptcyItem4"));
            appDeclarationMessageDto.setBankruptcyRemark(ParamUtil.getString(request, "bankruptcyRemark"));
            // Declaration on Competencies
            appDeclarationMessageDto.setCompetenciesItem1(ParamUtil.getString(request, "competenciesItem1"));
            appDeclarationMessageDto.setCompetenciesItem2(ParamUtil.getString(request, "competenciesItem2"));
            appDeclarationMessageDto.setCompetenciesItem3(ParamUtil.getString(request, "competenciesItem3"));
            appDeclarationMessageDto.setCompetenciesRemark(ParamUtil.getString(request, "competenciesRemark"));
            // Declaration on Criminal Records and Past Suspension/ Revocation under PHMCA/HCSA
            appDeclarationMessageDto.setCriminalRecordsItem1(ParamUtil.getString(request, "criminalRecordsItem1"));
            appDeclarationMessageDto.setCriminalRecordsItem2(ParamUtil.getString(request, "criminalRecordsItem2"));
            appDeclarationMessageDto.setCriminalRecordsItem3(ParamUtil.getString(request, "criminalRecordsItem3"));
            appDeclarationMessageDto.setCriminalRecordsItem4(ParamUtil.getString(request, "criminalRecordsItem4"));
            appDeclarationMessageDto.setCriminalRecordsRemark(ParamUtil.getString(request, "criminalRecordsRemark"));
            // General Accuracy Declaration
            appDeclarationMessageDto.setGeneralAccuracyItem1(ParamUtil.getString(request, "generalAccuracyItem1"));
        }
        appDeclarationMessageDto.setAppType(type);
        appDeclarationMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        return appDeclarationMessageDto;
    }

    public static List<AppDeclarationDocDto> getDeclarationFiles(String appType, HttpServletRequest request) {
        return getDeclarationFiles(appType, request, false);
    }

    public static List<AppDeclarationDocDto> getDeclarationFiles(String appType, HttpServletRequest request, boolean forPrint) {
        String fileAppendId = ApplicationHelper.getFileAppendId(appType);
        Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(request,
                IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId);
        if (IaisCommonUtils.isEmpty(fileMap)) {
            request.getSession().setAttribute(fileAppendId + "DocShowPageDto", null);
            return null;
        }
        AppDeclarationDocShowPageDto dto = (AppDeclarationDocShowPageDto) request.getSession().getAttribute(
                fileAppendId + "DocShowPageDto");
        if (Objects.isNull(dto)) {
            dto = new AppDeclarationDocShowPageDto();
            dto.setPageShowFileHashMap(IaisCommonUtils.genNewHashMap());
        }
        Map<String, PageShowFileDto> pageShowFileHashMap = dto.getPageShowFileHashMap();
        List<PageShowFileDto> pageDtos = IaisCommonUtils.genNewArrayList();
        List<File> files = IaisCommonUtils.genNewArrayList();
        List<AppDeclarationDocDto> docDtos = IaisCommonUtils.genNewArrayList();
        List<AppDeclarationDocDto> oldDocDtos = IaisCommonUtils.genNewArrayList();
        fileMap.forEach((s, file) -> {
            // the current uploaed files
            String index = s.substring(fileAppendId.length());
            if (file != null) {
                long length = file.length();
                if (length > 0) {
                    long size = length / 1024;
                    files.add(file);
                    AppDeclarationDocDto docDto = new AppDeclarationDocDto();
                    docDto.setDocName(file.getName());
                    String fileMd5 = FileUtils.getFileMd5(file);
                    docDto.setMd5Code(FileUtils.getFileMd5(file));
                    docDto.setDocSize(Integer.valueOf(Long.toString(size)));
                    docDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    docDto.setSeqNum(Integer.valueOf(index));
                    Optional<Integer> versions = pageShowFileHashMap.entrySet()
                            .stream()
                            .filter(i -> s.equals(i.getKey()))
                            .map(i -> i.getValue().getVersion())
                            .findAny();
                    docDto.setVersion(versions.orElse(0) + 1);
                    docDtos.add(docDto);
                    PageShowFileDto pageShowFileDto = new PageShowFileDto();
                    pageShowFileDto.setIndex(index);
                    pageShowFileDto.setFileName(file.getName());
                    pageShowFileDto.setFileMapId(fileAppendId + "Div" + index);
                    pageShowFileDto.setSize(Integer.valueOf(Long.toString(size)));
                    pageShowFileDto.setMd5Code(fileMd5);
                    pageDtos.add(pageShowFileDto);
                }
            } else {
                // the previous / old files
                PageShowFileDto pageShowFileDto = pageShowFileHashMap.get(s);
                if (Objects.nonNull(pageShowFileDto)) {
                    AppDeclarationDocDto docDto = new AppDeclarationDocDto();
                    docDto.setDocName(pageShowFileDto.getFileName());
                    docDto.setMd5Code(pageShowFileDto.getMd5Code());
                    docDto.setDocSize(pageShowFileDto.getSize());
                    docDto.setFileRepoId(pageShowFileDto.getFileUploadUrl());
                    docDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    docDto.setSeqNum(Integer.valueOf(index));
                    docDto.setVersion(Optional.ofNullable(pageShowFileDto.getVersion()).orElse(1));
                    oldDocDtos.add(docDto);
                    pageDtos.add(pageShowFileDto);
                }
            }
        });
        if (!forPrint) {
            dto.setPageShowFileDtos(pageDtos);
            request.getSession().setAttribute(fileAppendId + "DocShowPageDto", dto);
            // dto.setFileMaxIndex(pageDtos.size());
            List<String> list = getConfigCommService().saveFileRepo(files);
            if (list != null) {
                ListIterator<String> iterator = list.listIterator();
                for (int j = 0; j < docDtos.size(); j++) {
                    String fileRepoId = docDtos.get(j).getFileRepoId();
                    if (fileRepoId == null) {
                        if (iterator.hasNext()) {
                            String next = iterator.next();
                            pageDtos.get(j).setFileUploadUrl(next);
                            docDtos.get(j).setFileRepoId(next);
                            iterator.remove();
                        }
                    }
                }
            }
        }
        docDtos.addAll(oldDocDtos);
        return docDtos;
    }

    public static List<AppSvcVehicleDto> genAppSvcVehicleDto(HttpServletRequest request, String appType) {
        List<AppSvcVehicleDto> appSvcVehicleDtos = IaisCommonUtils.genNewArrayList();
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        int vehicleLength = ParamUtil.getInt(request, "vehiclesLength");
        for (int i = 0; i < vehicleLength; i++) {
            boolean getDataByIndexNo = false;
            boolean getPageData = false;
            String isPartEdit = ParamUtil.getString(request, "isPartEdit" + i);
            String vehicleIndexNo = ParamUtil.getString(request, "vehicleIndexNo" + i);
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                getPageData = true;
            } else if (AppConsts.YES.equals(isPartEdit)) {
                getPageData = true;
            } else if (!StringUtil.isEmpty(vehicleIndexNo)) {
                getDataByIndexNo = true;
            }
            log.debug("get data by index no. is {}", getDataByIndexNo);
            log.debug("get page data is {}", getPageData);
            if (getDataByIndexNo) {
                AppSvcVehicleDto appSvcVehicleDto = getAppSvcVehicleDtoByIndexNo(appSvcRelatedInfoDto, vehicleIndexNo);
                if (appSvcVehicleDto != null) {
                    appSvcVehicleDtos.add(appSvcVehicleDto);
                }
            } else if (getPageData) {
                String vehicleName = ParamUtil.getString(request, "vehicleName" + i);
                String chassisNum = ParamUtil.getString(request, "chassisNum" + i);
                String engineNum = ParamUtil.getString(request, "engineNum" + i);
                AppSvcVehicleDto appSvcVehicleDto = new AppSvcVehicleDto();
                appSvcVehicleDto.setVehicleNum(vehicleName);
                appSvcVehicleDto.setChassisNum(chassisNum);
                appSvcVehicleDto.setEngineNum(engineNum);
                appSvcVehicleDto.setDummyVehNum(StringUtil.isEmpty(vehicleName));
                String dummyVehNum = "";
                AppSvcVehicleDto oldAppSvcVehicleDto = getAppSvcVehicleDtoByIndexNo(appSvcRelatedInfoDto, vehicleIndexNo);
                if (oldAppSvcVehicleDto != null) {
                    dummyVehNum = oldAppSvcVehicleDto.getVehicleName();
                    appSvcVehicleDto.setStatus(oldAppSvcVehicleDto.getStatus());
                } else {
                    appSvcVehicleDto.setStatus(ApplicationConsts.VEHICLE_STATUS_SUBMIT);
                }
                if (StringUtil.isEmpty(dummyVehNum)) {
                    dummyVehNum = IaisEGPHelper.generateDummyVehicleNum(i);
                }
                appSvcVehicleDto.setVehicleName(dummyVehNum);
                if (appSvcVehicleDto.isDummyVehNum()) {
                    appSvcVehicleDto.setVehicleNum("Vehicle_No_" + (i + 1));
                }
                if (StringUtil.isEmpty(vehicleIndexNo)) {
                    appSvcVehicleDto.setVehicleIndexNo(UUID.randomUUID().toString());
                } else {
                    appSvcVehicleDto.setVehicleIndexNo(vehicleIndexNo);
                }
                appSvcVehicleDtos.add(appSvcVehicleDto);
            }
        }
        return appSvcVehicleDtos;
    }

    private static AppSvcVehicleDto getAppSvcVehicleDtoByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo) {
        AppSvcVehicleDto result = null;
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)) {
            List<AppSvcVehicleDto> appSvcVehicleDtos = appSvcRelatedInfoDto.getAppSvcVehicleDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcVehicleDtos)) {
                for (AppSvcVehicleDto appSvcVehicleDto : appSvcVehicleDtos) {
                    if (indexNo.equals(appSvcVehicleDto.getVehicleIndexNo())) {
                        result = appSvcVehicleDto;
                        break;
                    }
                }
            }
        }
        return result;
    }

    private static AppSvcPrincipalOfficersDto getClinicalDirectorByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo) {
        AppSvcPrincipalOfficersDto result = null;
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)) {
            List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos = appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcClinicalDirectorDtos)) {
                for (AppSvcPrincipalOfficersDto appSvcClinicalDirectorDto : appSvcClinicalDirectorDtos) {
                    if (indexNo.equals(appSvcClinicalDirectorDto.getIndexNo())) {
                        result = appSvcClinicalDirectorDto;
                        break;
                    }
                }
            }
        }
        return result;
    }

    private static AppSvcChargesDto getChargesByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo, boolean isGeneral) {
        AppSvcChargesDto result = null;
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)) {
            AppSvcChargesPageDto appSvcChargesPageDto = appSvcRelatedInfoDto.getAppSvcChargesPageDto();
            if (appSvcChargesPageDto != null) {
                if (isGeneral) {
                    List<AppSvcChargesDto> generalChargesDtos = appSvcChargesPageDto.getGeneralChargesDtos();
                    result = getChargesByIndexNo(generalChargesDtos, indexNo);
                } else {
                    List<AppSvcChargesDto> otherChargesDtos = appSvcChargesPageDto.getOtherChargesDtos();
                    result = getChargesByIndexNo(otherChargesDtos, indexNo);
                }
            }
        }
        return result;
    }

    private static AppSvcOtherInfoAbortDto getSvcOtherInfoAboutByTopType(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String topType,
            String c) {
        AppSvcOtherInfoAbortDto result = null;
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(topType)) {
            List<AppSvcOtherInfoDto> appSvcOtherInfoDto = appSvcRelatedInfoDto.getAppSvcOtherInfoList();
            if (appSvcOtherInfoDto != null) {
                for (AppSvcOtherInfoDto svcOtherInfoDto : appSvcOtherInfoDto) {
                    AppSvcOtherInfoTopDto appSvcOtherInfoTopDto = svcOtherInfoDto.getAppSvcOtherInfoTopDto();
                    if (AppConsts.OTHER_INFO_ABORT_ONE.equals(c)) {
                        List<AppSvcOtherInfoAbortDto> appSvcOtherInfoAboutDtos = svcOtherInfoDto.getOtherInfoAbortDrugList();
                        result = getOtherInfoByTopType(appSvcOtherInfoAboutDtos, appSvcOtherInfoTopDto, topType);
                    }
                    if (AppConsts.OTHER_INFO_ABORT_TWO.equals(c)) {
                        List<AppSvcOtherInfoAbortDto> appSvcOtherInfoAboutDtos = svcOtherInfoDto.getOtherInfoAbortSurgicalProcedureList();
                        result = getOtherInfoByTopType(appSvcOtherInfoAboutDtos, appSvcOtherInfoTopDto, topType);
                    }
                    if (AppConsts.OTHER_INFO_ABORT_THREE.equals(c)) {
                        List<AppSvcOtherInfoAbortDto> appSvcOtherInfoAboutDtos = svcOtherInfoDto.getOtherInfoAbortDrugAndSurgicalList();
                        result = getOtherInfoByTopType(appSvcOtherInfoAboutDtos, appSvcOtherInfoTopDto, topType);
                    }
                }
            }
        }
        return result;
    }

    private static AppSvcOtherInfoTopPersonDto getSvcOtherInfoTopPersonByIdNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String idNo,
            String psnType) {
        AppSvcOtherInfoTopPersonDto result = null;
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(idNo)) {
            List<AppSvcOtherInfoDto> appSvcOtherInfoDto = appSvcRelatedInfoDto.getAppSvcOtherInfoList();
            if (appSvcOtherInfoDto != null) {
                for (AppSvcOtherInfoDto svcOtherInfoDto : appSvcOtherInfoDto) {
                    if (AppConsts.OTHER_INFO_P.equals(psnType)) {
                        List<AppSvcOtherInfoTopPersonDto> appSvcOtherInfoTopPersonDtos = svcOtherInfoDto.getOtherInfoTopPersonPractitionersList();
                        result = getOtherInfoByIdNo(appSvcOtherInfoTopPersonDtos, idNo);
                    }
                    if (AppConsts.OTHER_INFO_A.equals(psnType)) {
                        List<AppSvcOtherInfoTopPersonDto> appSvcOtherInfoTopPersonDtos1 = svcOtherInfoDto.getOtherInfoTopPersonAnaesthetistsList();
                        result = getOtherInfoByIdNo(appSvcOtherInfoTopPersonDtos1, idNo);
                    }
                    if (AppConsts.OTHER_INFO_N.equals(psnType)) {
                        List<AppSvcOtherInfoTopPersonDto> appSvcOtherInfoTopPersonDtos2 = svcOtherInfoDto.getOtherInfoTopPersonNursesList();
                        result = getOtherInfoByIdNo(appSvcOtherInfoTopPersonDtos2, idNo);
                    }
                    if (AppConsts.OTHER_INFO_C.equals(psnType)) {
                        List<AppSvcOtherInfoTopPersonDto> appSvcOtherInfoTopPersonDtos3 = svcOtherInfoDto.getOtherInfoTopPersonCounsellorsList();
                        result = getOtherInfoByIdNo(appSvcOtherInfoTopPersonDtos3, idNo);
                    }
                }
            }
        }
        return result;
    }

    private static AppSvcChargesDto getChargesByIndexNo(List<AppSvcChargesDto> chargesDtos, String indexNo) {
        AppSvcChargesDto result = null;
        if (!IaisCommonUtils.isEmpty(chargesDtos) && !StringUtil.isEmpty(indexNo)) {
            for (AppSvcChargesDto appSvcChargesDto : chargesDtos) {
                if (indexNo.equals(appSvcChargesDto.getChargesIndexNo())) {
                    result = appSvcChargesDto;
                    break;
                }
            }
        }
        return result;
    }

    private static AppSvcOtherInfoTopPersonDto getOtherInfoByIdNo(List<AppSvcOtherInfoTopPersonDto> appSvcOtherInfoTopPersonDtos,
            String idNo) {
        AppSvcOtherInfoTopPersonDto result = null;
        if (!IaisCommonUtils.isEmpty(appSvcOtherInfoTopPersonDtos) && !StringUtil.isEmpty(idNo)) {
            for (AppSvcOtherInfoTopPersonDto appSvcOtherInfoTopPersonDto : appSvcOtherInfoTopPersonDtos) {
                if (idNo.equals(appSvcOtherInfoTopPersonDto.getIdNo())) {
                    result = appSvcOtherInfoTopPersonDto;
                }
            }
        }
        return result;
    }

    private static AppSvcOtherInfoAbortDto getOtherInfoByTopType(List<AppSvcOtherInfoAbortDto> appSvcOtherInfoAboutDtos,
            AppSvcOtherInfoTopDto appSvcOtherInfoTopDto, String topType) {
        AppSvcOtherInfoAbortDto result = null;

        if (!IaisCommonUtils.isEmpty(appSvcOtherInfoAboutDtos) && (appSvcOtherInfoTopDto != null) && !StringUtil.isEmpty(topType)) {
            if (ApplicationConsts.OTHER_INFO_SD.equals(topType)) {
                for (AppSvcOtherInfoAbortDto appSvcOtherInfoAboutDto : appSvcOtherInfoAboutDtos) {
                    result = appSvcOtherInfoAboutDto;
                }
            }
            if (ApplicationConsts.OTHER_INFO_SSP.equals(topType)) {
                for (AppSvcOtherInfoAbortDto appSvcOtherInfoAboutDto : appSvcOtherInfoAboutDtos) {
                    result = appSvcOtherInfoAboutDto;
                }
            }
            if (ApplicationConsts.OTHER_INFO_DSP.equals(topType)) {
                for (AppSvcOtherInfoAbortDto appSvcOtherInfoAboutDto : appSvcOtherInfoAboutDtos) {
                    result = appSvcOtherInfoAboutDto;
                }
            }
        }
        return result;
    }

    public static AppPremOutSourceLicenceDto genAppPremOutSourceProvidersDto(List<String> appPremOutSourceProvidersIds, String curAct,
                                                                             AppPremOutSourceLicenceDto appPremOutSourceProvidersDto, HttpServletRequest request){
        if (StringUtil.isEmpty(appPremOutSourceProvidersDto)){
            appPremOutSourceProvidersDto = new AppPremOutSourceLicenceDto();
        }
        String bName = ParamUtil.getString(request, "name");
        String postCode = ParamUtil.getString(request,"postalCode");
        appPremOutSourceProvidersDto.setBusinessName(bName);
        appPremOutSourceProvidersDto.setPostCode(postCode);
        List<AppPremOutSourceLicenceDto> clinicalLaboratoryList = appPremOutSourceProvidersDto.getClinicalLaboratoryList();
        List<AppPremOutSourceLicenceDto> radiologicalServiceList = appPremOutSourceProvidersDto.getRadiologicalServiceList();
        List<String> prefixId = appPremOutSourceProvidersIds;
        if ("search".equals(curAct)){
            appPremOutSourceProvidersDto = getSerchAppPremOutSourceLicenceDto(request,appPremOutSourceProvidersDto);
        }
        if ("add".equals(curAct)){
            if (IaisCommonUtils.isEmpty(clinicalLaboratoryList)){
                clinicalLaboratoryList = IaisCommonUtils.genNewArrayList();
            }
            if (IaisCommonUtils.isEmpty(radiologicalServiceList)){
                radiologicalServiceList = IaisCommonUtils.genNewArrayList();
            }
            appPremOutSourceProvidersDto = getAddAppPremOutSourceLicenceDto(request,appPremOutSourceProvidersDto,clinicalLaboratoryList,radiologicalServiceList);
        }
        if ("delete".equals(curAct)){
            appPremOutSourceProvidersDto = getDelAppOutSourcedDto(request,appPremOutSourceProvidersDto,clinicalLaboratoryList,radiologicalServiceList);
        }
        return appPremOutSourceProvidersDto;
    }

    private static AppPremOutSourceLicenceDto getDelAppOutSourcedDto(HttpServletRequest request,
                                                                     AppPremOutSourceLicenceDto appPremOutSourceLicenceDto,
                                                                     List<AppPremOutSourceLicenceDto> clinicalLaboratoryList,
                                                                     List<AppPremOutSourceLicenceDto> radiologicalServiceList){
        String prefix = ParamUtil.getString(request,"prefixVal");
        if (IaisCommonUtils.isNotEmpty(clinicalLaboratoryList)){
            for (AppPremOutSourceLicenceDto outSourceLicenceDto : clinicalLaboratoryList) {
                if (prefix.equals(outSourceLicenceDto.getId())){
                    outSourceLicenceDto.setStatus(1);
                }
            }
        }

        if (IaisCommonUtils.isNotEmpty(radiologicalServiceList)){
            for (AppPremOutSourceLicenceDto outSourceLicenceDto : radiologicalServiceList) {
                if (prefix.equals(outSourceLicenceDto.getServiceCode())){
                    outSourceLicenceDto.setStatus(1);
                }
            }
        }
        return appPremOutSourceLicenceDto;

    }

    private static AppPremOutSourceLicenceDto getSerchAppPremOutSourceLicenceDto(HttpServletRequest request,AppPremOutSourceLicenceDto appPremOutSourceLicenceDto){
        String svcName = ParamUtil.getString(request, "serviceCode");
        String licNo = ParamUtil.getString(request, "licNo");
        String businessName = ParamUtil.getString(request,"businessName");
        appPremOutSourceLicenceDto.setServiceCode(svcName);
        appPremOutSourceLicenceDto.setLicenceNo(licNo);
        appPremOutSourceLicenceDto.setBusinessName(businessName);
        return appPremOutSourceLicenceDto;
    }

    private static AppPremOutSourceLicenceDto getAddAppPremOutSourceLicenceDto(HttpServletRequest request,
                                                                               AppPremOutSourceLicenceDto appPremOutSourceLicenceDto,
                                                                               List<AppPremOutSourceLicenceDto> clinicalLaboratoryList,
                                                                               List<AppPremOutSourceLicenceDto> radiologicalServiceList){
        AppPremOutSourceLicenceDto premOutSourceLicenceDto = new AppPremOutSourceLicenceDto();
        String prefix = ParamUtil.getString(request,"prefixVal");
        String startDate = ParamUtil.getString(request,prefix+"agreementStartDate");
        String endDate = ParamUtil.getString(request,prefix+"agreementEndDate");
        String scpoing = ParamUtil.getString(request,prefix+"scopeOfOutsourcing");
        String svcName = ParamUtil.getString(request,prefix+"svcName");
        String bName = ParamUtil.getString(request,prefix+"bName");
        String addr = ParamUtil.getString(request,prefix+"address");
        String licNo = ParamUtil.getString(request,prefix+"licNo");
        String expiryDate = ParamUtil.getString(request,prefix+"expiryDate");

        premOutSourceLicenceDto.setId(prefix);
        premOutSourceLicenceDto.setServiceCode(svcName);
        premOutSourceLicenceDto.setLicenceNo(licNo);
        premOutSourceLicenceDto.setBusinessName(bName);
        premOutSourceLicenceDto.setAddress(addr);
        premOutSourceLicenceDto.setOutstandingScope(scpoing);
        premOutSourceLicenceDto.setExpiryDate(expiryDate);
        premOutSourceLicenceDto.setStatus(0);
        try {
            premOutSourceLicenceDto.setAgreementStartDate(Formatter.parseDate(startDate));
            premOutSourceLicenceDto.setAgreementEndDate(Formatter.parseDate(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (HcsaAppConst.CLINICALLABORATOYY.equals(premOutSourceLicenceDto.getServiceCode())){
            clinicalLaboratoryList.add(premOutSourceLicenceDto);
        }
        if (HcsaAppConst.RADIOLOGICALSERVICE.equals(premOutSourceLicenceDto.getServiceCode())){
            radiologicalServiceList.add(premOutSourceLicenceDto);
        }
        appPremOutSourceLicenceDto.setClinicalLaboratoryList(clinicalLaboratoryList);
        appPremOutSourceLicenceDto.setRadiologicalServiceList(radiologicalServiceList);
        return appPremOutSourceLicenceDto;
    }

    public static List<String> addIds(String curAct,String addIds,List<String> ids){
        if (IaisCommonUtils.isEmpty(ids) && StringUtil.isNotEmpty(addIds)){
            ids = IaisCommonUtils.genNewArrayList();
            ids.add(addIds);
            return ids;
        }
        if ("add".equals(curAct) && StringUtil.isNotEmpty(addIds) && IaisCommonUtils.isNotEmpty(ids)){
            for (String id : ids) {
                if (!addIds.equals(id)){
                    ids.add(addIds);
                    break;
                }
            }
        }
        return ids;
    }
//
//    private static List<AppPremOutSourceLicenceDto> sortCLDList(List<AppPremOutSourceLicenceDto> appPremOutSourceLicenceDtos){
//        Collections.sort(appPremOutSourceLicenceDtos, new Comparator<AppPremOutSourceLicenceDto>() {
//            @Override
//            public int compare(AppPremOutSourceLicenceDto o1, AppPremOutSourceLicenceDto o2) {
//                return o1.getLicenceNo().compareTo(o2.getLicenceNo());
//            }
//        });
//    }

    public static List<AppSvcPrincipalOfficersDto> genAppSvcClinicalDirectorDto(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("gen app svc clinical director dto start ..."));
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = genKeyPersonnels(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, "",
                request);
        log.debug(StringUtil.changeForLog("gen app svc clinical director dto end ..."));
        return appSvcCgoDtoList;
    }

    private static boolean canSetValue(boolean canEdit, boolean isNewOfficer, boolean isPartEdit) {
        return isPartEdit || canEdit || isNewOfficer;
    }

    public static List<AppSvcOtherInfoDto> genAppSvcOtherInfoList(HttpServletRequest request, String appType,
                                                                  List<AppSvcOtherInfoDto> appSvcOtherInfoDtos,
                                                                  List<AppGrpPremisesDto> appGrpPremisesDtos) {
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        if (IaisCommonUtils.isNotEmpty(appGrpPremisesDtos)){
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                String premName = appGrpPremisesDto.getPremTypeName();
                String prefix = appGrpPremisesDto.getPremisesIndexNo();
                if (IaisCommonUtils.isNotEmpty(appSvcOtherInfoDtos)){
                    for (AppSvcOtherInfoDto appSvcOtherInfoDto : appSvcOtherInfoDtos) {
                        if (appSvcOtherInfoDto.getPremisesVal() == prefix){
                            appSvcOtherInfoDto.setPremName(premName);
                            String topType = ParamUtil.getString(request, prefix+"topType");
                            String provideTop = ParamUtil.getString(request, prefix+"provideTop");
                            String dsDeclaration = ParamUtil.getString(request, prefix+"dsDeclaration");
                            String ascsDeclaration = ParamUtil.getString(request, prefix+"ascsDeclaration");
                            String declaration = ParamUtil.getString(request, prefix+"declaration");
                            AppSvcOtherInfoMedDto appSvcOtherInfoMedDto1 = new AppSvcOtherInfoMedDto();
                            String gfValue = ParamUtil.getString(request, prefix+"agfaValue");
                            appSvcOtherInfoMedDto1.setGfaValue(gfValue);
                            AppSvcOtherInfoNurseDto appSvcOtherInfoNurseDto = new AppSvcOtherInfoNurseDto();
                            ControllerHelper.get(request, appSvcOtherInfoNurseDto);
                            String provideYfVs = ParamUtil.getString(request,prefix+"provideYfVs");
                            String yfCommencementDateStr = ParamUtil.getString(request,prefix+"yfCommencementDate");
                            appSvcOtherInfoDto.setYfCommencementDateStr(yfCommencementDateStr);
                            if (StringUtil.isEmpty(yfCommencementDateStr)){
                                appSvcOtherInfoDto.setYfCommencementDate(null);
                            }else {
                                Date date = DateUtil.parseDate(yfCommencementDateStr,Formatter.DATE);
                                appSvcOtherInfoDto.setYfCommencementDate(date);
                            }
                            appSvcOtherInfoDto.setProvideTop(provideTop);
                            appSvcOtherInfoDto.setDsDeclaration(dsDeclaration);
                            appSvcOtherInfoDto.setAscsDeclaration(ascsDeclaration);
                            appSvcOtherInfoDto.setDeclaration(declaration);
                            appSvcOtherInfoDto.setProvideYfVs(provideYfVs);
                            appSvcOtherInfoDto.setOtherInfoTopPersonPractitionersList(
                                    getAppSvcOtherInfoTopPersonDtoPractitioners(request, appType, isRfi, appSvcRelatedInfoDto,prefix));
                            appSvcOtherInfoDto.setOtherInfoTopPersonAnaesthetistsList(
                                    getAppSvcOtherInfoTopPersonDtoAnaesthetists(request, appType, isRfi, appSvcRelatedInfoDto,prefix));
                            appSvcOtherInfoDto.setOtherInfoTopPersonNursesList(
                                    getAppSvcOtherInfoTopPersonDtoNurses(request, appType, isRfi, appSvcRelatedInfoDto,prefix));
                            appSvcOtherInfoDto.setOtherInfoTopPersonCounsellorsList(
                                    getAppSvcOtherInfoTopPersonDtoCounsellors(request, appType, isRfi, appSvcRelatedInfoDto,prefix));
                            appSvcOtherInfoDto.setOtherInfoAbortDrugList(
                                    getAppSvcOtherInfoAbortDto1(request, appType, topType, isRfi, appSvcRelatedInfoDto,prefix));
                            appSvcOtherInfoDto.setOtherInfoAbortSurgicalProcedureList(
                                    getAppSvcOtherInfoAbortDto2(request, appType, topType, isRfi, appSvcRelatedInfoDto,prefix));
                            appSvcOtherInfoDto.setOtherInfoAbortDrugAndSurgicalList(
                                    getAppSvcOtherInfoAbortDto3(request, appType, topType, isRfi, appSvcRelatedInfoDto,prefix));
                            appSvcOtherInfoDto.setAppSvcOtherInfoTopDto(getAppSvcOtherInfoTopDto(request,prefix));
                            appSvcOtherInfoDto.setAppSvcOtherInfoMedDto(getAppSvcOtherDental(request,prefix));
                            appSvcOtherInfoDto.setOtherInfoMedAmbulatorySurgicalCentre(appSvcOtherInfoMedDto1);
                            appSvcOtherInfoDto.setAppSvcOtherInfoNurseDto(getAppSvcOtherInfoNurseDto(request,prefix));
                            appSvcOtherInfoDto.setOrgUserDto(getOtherInfoYfVs(request));
                            appSvcOtherInfoDto.setAppSvcSuplmFormDto(appSvcOtherInfoDto.getAppSvcSuplmFormDto());
                            appSvcOtherInfoDto.setAppPremSubSvcRelDtoList(genAppPremSubSvcRelDtoList(appSvcOtherInfoDto.getAppPremSubSvcRelDtoList(),
                                    prefix,"",request));
                            appSvcOtherInfoDto.initAllAppPremSubSvcRelDtoList();
                        }
                    }
                    setAppSvcOtherFormList(appSvcOtherInfoDtos,request);
                }
            }
        }
        return appSvcOtherInfoDtos;
    }
    //YfVs
    public static OrgUserDto getOtherInfoYfVs(HttpServletRequest request){
        User user = SessionManager.getInstance(request).getCurrentUser();
        ComSystemAdminClient client = SpringContextHelper.getContext().getBean(ComSystemAdminClient.class);
        OrgUserDto orgUserDto = client.retrieveOrgUserAccount(user.getId()).getEntity();
        return orgUserDto;
    }

    //other nurse
    public static AppSvcOtherInfoNurseDto getAppSvcOtherInfoNurseDto(HttpServletRequest request,String prefix){
        AppSvcOtherInfoNurseDto appSvcOtherInfoNurseDto = new AppSvcOtherInfoNurseDto();
        String perShiftNum = ParamUtil.getString(request, prefix+"perShiftNum");
        String dialysisStationsNum = ParamUtil.getString(request, prefix+"dialysisStationsNum");
        String helpBStationNum = ParamUtil.getString(request, prefix+"helpBStationNum");
        String nisOpenToPublic = ParamUtil.getString(request, prefix+"nisOpenToPublic");
        appSvcOtherInfoNurseDto.setHelpBStationNum(helpBStationNum);
        appSvcOtherInfoNurseDto.setIsOpenToPublic(nisOpenToPublic);
        appSvcOtherInfoNurseDto.setPerShiftNum(perShiftNum);
        appSvcOtherInfoNurseDto.setDialysisStationsNum(dialysisStationsNum);
        return appSvcOtherInfoNurseDto;
    }

    //other med
    public static AppSvcOtherInfoMedDto getAppSvcOtherDental(HttpServletRequest request,String prefix){
        AppSvcOtherInfoMedDto result = new AppSvcOtherInfoMedDto();
        String isMedicalTypeIt = ParamUtil.getString(request, prefix+"isMedicalTypeIt");
        String isMedicalTypePaper = ParamUtil.getString(request, prefix+"isMedicalTypePaper");
        String systemOption = ParamUtil.getString(request, prefix+"systemOption");
        String isOpenToPublic = ParamUtil.getString(request, prefix+"isOpenToPublic");
        String gfaValue = ParamUtil.getString(request, prefix+"gfaValue");
        result.setGfaValue(gfaValue);
        result.setIsOpenToPublic(isOpenToPublic);
        result.setSystemOption(systemOption);
        result.setIsMedicalTypeIt(isMedicalTypeIt);
        result.setIsMedicalTypePaper(isMedicalTypePaper);
        if ("MED06".equals(systemOption)) {
            String otherSystemOption = ParamUtil.getString(request, prefix+"otherSystemOption");
            result.setOtherSystemOption(otherSystemOption);
        }
        return result;
    }

    //other top
    public static AppSvcOtherInfoTopDto getAppSvcOtherInfoTopDto(HttpServletRequest request,String prefix) {
        AppSvcOtherInfoTopDto result = new AppSvcOtherInfoTopDto();
        String topType = ParamUtil.getString(request, prefix+"topType");
        String hasConsuAttendCourse = ParamUtil.getString(request, prefix+"hasConsuAttendCourse");
        String isProvideHpb = ParamUtil.getString(request, prefix+"isProvideHpb");
        String isOutcomeProcRecord = ParamUtil.getString(request, prefix+"isOutcomeProcRecord");
        String compCaseNum = ParamUtil.getString(request, prefix+"compCaseNum");
        result.setTopType(topType);
        result.setHasConsuAttendCourse(hasConsuAttendCourse);
        result.setIsProvideHpb(isProvideHpb);
        result.setIsOutcomeProcRecord(isOutcomeProcRecord);
        result.setCompCaseNum(compCaseNum);
        return result;
    }

    //other top person practitioners
    public static List<AppSvcOtherInfoTopPersonDto> getAppSvcOtherInfoTopPersonDtoPractitioners(HttpServletRequest request,
            String appType, boolean isRfi, AppSvcRelatedInfoDto appSvcRelatedInfoDto,String prefix) {
        List<AppSvcOtherInfoTopPersonDto> result = IaisCommonUtils.genNewArrayList();
        String c = ParamUtil.getString(request,prefix+"cdLength");
        if (StringUtil.isNotEmpty(c)){
            int cdLength = ParamUtil.getInt(request, prefix+"cdLength");
            for (int i = 0; i < cdLength; i++) {
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request, "isPartEdit" + i);
                String idNo = ParamUtil.getString(request, prefix+"idNo" + i);
                String psnType = ParamUtil.getString(request, prefix+"psnType" + i);
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(idNo)) {
                    getDataByIndexNo = true;
                }
                if (getDataByIndexNo) {
                    AppSvcOtherInfoTopPersonDto appPremOtherInfoTopPersonDto = getSvcOtherInfoTopPersonByIdNo(appSvcRelatedInfoDto, idNo,
                            psnType);
                    if (appPremOtherInfoTopPersonDto != null) {
                        result.add(appPremOtherInfoTopPersonDto);
                    }
                } else if (getPageData) {
                    AppSvcOtherInfoTopPersonDto appSvcOtherInfoTopPersonDto = new AppSvcOtherInfoTopPersonDto();
                    String profRegNo = ParamUtil.getString(request,prefix+"profRegNo"+i);
                    String regType = ParamUtil.getString(request,prefix+"regType"+i);
                    String isMedAuthByMoh = ParamUtil.getString(request,prefix+"isMedAuthByMoh"+i);
                    String name = ParamUtil.getString(request,prefix+"name"+i);
                    String speciality = ParamUtil.getString(request,prefix+"speciality"+i);
                    String qualification = ParamUtil.getString(request,prefix+"qualification"+i);
                    appSvcOtherInfoTopPersonDto.setQualification(qualification);
                    appSvcOtherInfoTopPersonDto.setSpeciality(speciality);
                    appSvcOtherInfoTopPersonDto.setName(name);
                    appSvcOtherInfoTopPersonDto.setProfRegNo(profRegNo);
                    appSvcOtherInfoTopPersonDto.setPsnType(psnType);
                    appSvcOtherInfoTopPersonDto.setRegType(regType);
                    appSvcOtherInfoTopPersonDto.setIsMedAuthByMoh(isMedAuthByMoh);
                    appSvcOtherInfoTopPersonDto.setSeqNum(i);
                    appSvcOtherInfoTopPersonDto.setIdNo(idNo);
                    result.add(appSvcOtherInfoTopPersonDto);
                }
            }
        }
        return result;
    }

    //other top person anaesthetists
    public static List<AppSvcOtherInfoTopPersonDto> getAppSvcOtherInfoTopPersonDtoAnaesthetists(HttpServletRequest request,
            String appType, boolean isRfi, AppSvcRelatedInfoDto appSvcRelatedInfoDto,String prefix) {
        List<AppSvcOtherInfoTopPersonDto> result = IaisCommonUtils.genNewArrayList();
        String a = ParamUtil.getString(request,prefix+"anaLength");
        if (StringUtil.isNotEmpty(a)){
            int anaLength = ParamUtil.getInt(request, prefix+"anaLength");
            for (int i = 0; i < anaLength; i++) {
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request, "isPartEdit" + i);
                String idNo = ParamUtil.getString(request, prefix+"idANo" + i);
                String apsnType = ParamUtil.getString(request, prefix+"apsnType" + i);
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(idNo)) {
                    getDataByIndexNo = true;
                }
                if (getDataByIndexNo) {
                    AppSvcOtherInfoTopPersonDto appPremOtherInfoTopPersonDto = getSvcOtherInfoTopPersonByIdNo(appSvcRelatedInfoDto, idNo,
                            apsnType);
                    if (appPremOtherInfoTopPersonDto != null) {
                        result.add(appPremOtherInfoTopPersonDto);
                    }
                } else if (getPageData) {
                    String profRegNo = ParamUtil.getString(request, prefix+"aprofRegNo" + i);
                    String name = ParamUtil.getString(request, prefix+"aname" + i);
                    String regType = ParamUtil.getString(request, prefix+"aregType" + i);
                    String qualification = ParamUtil.getString(request, prefix+"aqualification" + i);
                    AppSvcOtherInfoTopPersonDto appSvcOtherInfoTopPersonDto = new AppSvcOtherInfoTopPersonDto();
                    appSvcOtherInfoTopPersonDto.setPsnType(apsnType);
                    appSvcOtherInfoTopPersonDto.setProfRegNo(profRegNo);
                    appSvcOtherInfoTopPersonDto.setName(name);
                    appSvcOtherInfoTopPersonDto.setRegType(regType);
                    appSvcOtherInfoTopPersonDto.setQualification(qualification);
                    appSvcOtherInfoTopPersonDto.setSeqNum(i);
                    appSvcOtherInfoTopPersonDto.setIdNo(idNo);
                    result.add(appSvcOtherInfoTopPersonDto);
                }
            }
        }
        return result;
    }

    //other top person nurses
    public static List<AppSvcOtherInfoTopPersonDto> getAppSvcOtherInfoTopPersonDtoNurses(HttpServletRequest request, String appType,
            boolean isRfi, AppSvcRelatedInfoDto appSvcRelatedInfoDto,String prefix) {
        List<AppSvcOtherInfoTopPersonDto> result = IaisCommonUtils.genNewArrayList();
        String n = ParamUtil.getString(request,prefix+"nLength");
        if (StringUtil.isNotEmpty(n)){
            int nLength = ParamUtil.getInt(request, prefix+"nLength");
            for (int i = 0; i < nLength; i++) {
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request, "isPartEdit" + i);
                String nname = ParamUtil.getString(request, prefix+"nname" + i);
                String npsnType = ParamUtil.getString(request, prefix+"npsnType" + i);
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(nname)) {
                    getDataByIndexNo = true;
                }
                if (getDataByIndexNo) {
                    AppSvcOtherInfoTopPersonDto appPremOtherInfoTopPersonDto = getSvcOtherInfoTopPersonByIdNo(appSvcRelatedInfoDto, nname,
                            npsnType);
                    if (appPremOtherInfoTopPersonDto != null) {
                        result.add(appPremOtherInfoTopPersonDto);
                    }
                } else if (getPageData) {
                    String nqualification = ParamUtil.getString(request, prefix+"nqualification" + i);
                    AppSvcOtherInfoTopPersonDto appSvcOtherInfoTopPersonDto = new AppSvcOtherInfoTopPersonDto();
                    appSvcOtherInfoTopPersonDto.setPsnType(npsnType);
                    appSvcOtherInfoTopPersonDto.setName(nname);
                    appSvcOtherInfoTopPersonDto.setQualification(nqualification);
                    appSvcOtherInfoTopPersonDto.setSeqNum(i);
                    result.add(appSvcOtherInfoTopPersonDto);
                }
            }
        }
        return result;
    }

    //other top person counsellors
    public static List<AppSvcOtherInfoTopPersonDto> getAppSvcOtherInfoTopPersonDtoCounsellors(HttpServletRequest request,
            String appType, boolean isRfi, AppSvcRelatedInfoDto appSvcRelatedInfoDto,String prefix) {
        List<AppSvcOtherInfoTopPersonDto> result = IaisCommonUtils.genNewArrayList();
        String co = ParamUtil.getString(request,prefix+"cLength");
        if (StringUtil.isNotEmpty(co)){
            int cLength = ParamUtil.getInt(request, prefix+"cLength");
            for (int i = 0; i < cLength; i++) {
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request, "isPartEdit" + i);
                String cidNo = ParamUtil.getString(request, prefix+"cidNo" + i);
                String cpsnType = ParamUtil.getString(request, prefix+"cpsnType" + i);
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(cidNo)) {
                    getDataByIndexNo = true;
                }
                if (getDataByIndexNo) {
                    AppSvcOtherInfoTopPersonDto appPremOtherInfoTopPersonDto = getSvcOtherInfoTopPersonByIdNo(appSvcRelatedInfoDto, cidNo,
                            cpsnType);
                    if (appPremOtherInfoTopPersonDto != null) {
                        result.add(appPremOtherInfoTopPersonDto);
                    }
                } else if (getPageData) {
                    String cqualification = ParamUtil.getString(request, prefix+"cqualification" + i);
                    String cname = ParamUtil.getString(request, prefix+"cname" + i);
                    AppSvcOtherInfoTopPersonDto appSvcOtherInfoTopPersonDto = new AppSvcOtherInfoTopPersonDto();
                    appSvcOtherInfoTopPersonDto.setPsnType(cpsnType);
                    appSvcOtherInfoTopPersonDto.setName(cname);
                    appSvcOtherInfoTopPersonDto.setQualification(cqualification);
                    appSvcOtherInfoTopPersonDto.setSeqNum(i);
                    appSvcOtherInfoTopPersonDto.setIdNo(cidNo);
                    result.add(appSvcOtherInfoTopPersonDto);
                }
            }
        }
        return result;
    }

    //other abort
    public static List<AppSvcOtherInfoAbortDto> getAppSvcOtherInfoAbortDto1(HttpServletRequest request, String appType, String topType,
            boolean isRfi, AppSvcRelatedInfoDto appSvcRelatedInfoDto,String prefix) {
        List<AppSvcOtherInfoAbortDto> result = IaisCommonUtils.genNewArrayList();
        String at = ParamUtil.getString(request,prefix+"atdLength");
        if (StringUtil.isNotEmpty(at)){
            int atdLength = ParamUtil.getInt(request, prefix+"atdLength");
            for (int i = 0; i < atdLength; i++) {
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request, "isPartEdit" + i);
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(topType)) {
                    getDataByIndexNo = true;
                }
                if (getDataByIndexNo) {
                    AppSvcOtherInfoAbortDto appSvcOtherInfoAboutDto = getSvcOtherInfoAboutByTopType(appSvcRelatedInfoDto,
                            ApplicationConsts.OTHER_INFO_SD, "1");
                    if (appSvcOtherInfoAboutDto != null) {
                        result.add(appSvcOtherInfoAboutDto);
                    }
                } else if (getPageData) {
                    String year = ParamUtil.getString(request, prefix+"year" + i);
                    String abortNum = ParamUtil.getString(request, prefix+"abortNum" + i);

                    AppSvcOtherInfoAbortDto appSvcOtherInfoAboutDto = new AppSvcOtherInfoAbortDto();
                    if (year != null && abortNum != null) {
                        appSvcOtherInfoAboutDto.setTopType(ApplicationConsts.OTHER_INFO_SD);
                        appSvcOtherInfoAboutDto.setYear(year);
                        appSvcOtherInfoAboutDto.setAbortNum(abortNum);
                    }
                    result.add(appSvcOtherInfoAboutDto);
                }
            }
        }

        return result;
    }

    public static List<AppSvcOtherInfoAbortDto> getAppSvcOtherInfoAbortDto2(HttpServletRequest request, String appType, String topType,
            boolean isRfi, AppSvcRelatedInfoDto appSvcRelatedInfoDto,String prefix) {
        List<AppSvcOtherInfoAbortDto> result = IaisCommonUtils.genNewArrayList();
        String p = ParamUtil.getString(request,prefix+"pLength");
        if (StringUtil.isNotEmpty(p)){
            int pLength = ParamUtil.getInt(request, prefix+"pLength");
            for (int i = 0; i < pLength; i++) {
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request, "isPartEdit" + i);
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(topType)) {
                    getDataByIndexNo = true;
                }
                if (getDataByIndexNo) {
                    AppSvcOtherInfoAbortDto appSvcOtherInfoAboutDto = getSvcOtherInfoAboutByTopType(appSvcRelatedInfoDto,
                            ApplicationConsts.OTHER_INFO_SSP, "2");
                    if (appSvcOtherInfoAboutDto != null) {
                        result.add(appSvcOtherInfoAboutDto);
                    }
                } else if (getPageData) {
                    String year = ParamUtil.getString(request, prefix+"pyear" + i);
                    String abortNum = ParamUtil.getString(request, prefix+"pabortNum" + i);

                    AppSvcOtherInfoAbortDto appSvcOtherInfoAboutDto = new AppSvcOtherInfoAbortDto();
                    if (year != null && abortNum != null) {
                        appSvcOtherInfoAboutDto.setTopType(ApplicationConsts.OTHER_INFO_SSP);
                        appSvcOtherInfoAboutDto.setYear(year);
                        appSvcOtherInfoAboutDto.setAbortNum(abortNum);

                    }
                    result.add(appSvcOtherInfoAboutDto);
                }
            }
        }

        return result;
    }

    public static List<AppSvcOtherInfoAbortDto> getAppSvcOtherInfoAbortDto3(HttpServletRequest request, String appType, String topType,
            boolean isRfi, AppSvcRelatedInfoDto appSvcRelatedInfoDto,String prefix) {
        List<AppSvcOtherInfoAbortDto> result = IaisCommonUtils.genNewArrayList();
        String a =ParamUtil.getString(request,prefix+"aLength");
        if (StringUtil.isNotEmpty(a)){
            int aLength = ParamUtil.getInt(request, prefix+"aLength");
            for (int i = 0; i < aLength; i++) {
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request, "isPartEdit" + i);
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(topType)) {
                    getDataByIndexNo = true;
                }
                if (getDataByIndexNo) {
                    AppSvcOtherInfoAbortDto appSvcOtherInfoAboutDto = getSvcOtherInfoAboutByTopType(appSvcRelatedInfoDto,
                            ApplicationConsts.OTHER_INFO_DSP, "3");
                    if (appSvcOtherInfoAboutDto != null) {
                        result.add(appSvcOtherInfoAboutDto);
                    }
                } else if (getPageData) {
                    String year = ParamUtil.getString(request, prefix+"ayear" + i);
                    String abortNum = ParamUtil.getString(request, prefix+"aabortNum" + i);

                    AppSvcOtherInfoAbortDto appSvcOtherInfoAboutDto = new AppSvcOtherInfoAbortDto();

                    if (year != null && abortNum != null) {
                        appSvcOtherInfoAboutDto.setTopType(ApplicationConsts.OTHER_INFO_DSP);
                        appSvcOtherInfoAboutDto.setYear(year);
                        appSvcOtherInfoAboutDto.setAbortNum(abortNum);
                    }
                    result.add(appSvcOtherInfoAboutDto);
                }
            }
        }

        return result;
    }

    public static AppSvcChargesPageDto genAppSvcChargesDto(HttpServletRequest request, String appType) {
        AppSvcChargesPageDto appSvcChargesPageDto = new AppSvcChargesPageDto();
        List<AppSvcChargesDto> generalChargesDtos = IaisCommonUtils.genNewArrayList();
        List<AppSvcChargesDto> otherChargesDtos = IaisCommonUtils.genNewArrayList();
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        int generalChargeLength = ParamUtil.getInt(request, "generalChargeLength");
        for (int i = 0; i < generalChargeLength; i++) {
            boolean getDataByIndexNo = false;
            boolean getPageData = false;
            String isPartEdit = ParamUtil.getString(request, "isPartEdit" + i);
            String chargesIndexNo = ParamUtil.getString(request, "chargesIndexNo" + i);
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                getPageData = true;
            } else if (AppConsts.YES.equals(isPartEdit)) {
                getPageData = true;
            } else if (!StringUtil.isEmpty(chargesIndexNo)) {
                getDataByIndexNo = true;
            }
            log.debug("get data by index no. is {}", getDataByIndexNo);
            log.debug("get page data is {}", getPageData);
            if (getDataByIndexNo) {
                AppSvcChargesDto appSvcChargesDto = getChargesByIndexNo(appSvcRelatedInfoDto, chargesIndexNo, true);
                if (appSvcChargesDto != null) {
                    generalChargesDtos.add(appSvcChargesDto);
                }
            } else if (getPageData) {
                String chargesType = ParamUtil.getString(request, "chargesType" + i);
                String minAmount = ParamUtil.getString(request, "minAmount" + i);
                String maxAmount = ParamUtil.getString(request, "maxAmount" + i);
                String remarks = ParamUtil.getString(request, "remarks" + i);

                AppSvcChargesDto appSvcChargesDto = new AppSvcChargesDto();
                appSvcChargesDto.setChargesType(chargesType);
                appSvcChargesDto.setMinAmount(minAmount);
                appSvcChargesDto.setMaxAmount(maxAmount);
                appSvcChargesDto.setRemarks(remarks);
                if (StringUtil.isEmpty(chargesIndexNo)) {
                    appSvcChargesDto.setChargesIndexNo(UUID.randomUUID().toString());
                } else {
                    appSvcChargesDto.setChargesIndexNo(chargesIndexNo);
                }
                generalChargesDtos.add(appSvcChargesDto);
            }
        }
        int otherChargeLength = ParamUtil.getInt(request, "otherChargeLength");
        for (int i = 0; i < otherChargeLength; i++) {
            boolean getDataByIndexNo = false;
            boolean getPageData = false;
            String isPartEdit = ParamUtil.getString(request, "otherChargesIsPartEdit" + i);
            String chargesIndexNo = ParamUtil.getString(request, "otherChargesIndexNo" + i);
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                getPageData = true;
            } else if (AppConsts.YES.equals(isPartEdit)) {
                getPageData = true;
            } else if (!StringUtil.isEmpty(chargesIndexNo)) {
                getDataByIndexNo = true;
            }
            log.debug("other charges get data by index no. is {}", getDataByIndexNo);
            log.debug("other charges get page data is {}", getPageData);
            if (getDataByIndexNo) {
                AppSvcChargesDto appSvcChargesDto = getChargesByIndexNo(appSvcRelatedInfoDto, chargesIndexNo, false);
                if (appSvcChargesDto != null) {
                    otherChargesDtos.add(appSvcChargesDto);
                }
            } else if (getPageData) {
                String otherChargesCategory = ParamUtil.getString(request, "otherChargesCategory" + i);
                String otherChargesType = ParamUtil.getString(request, "otherChargesType" + i);
                String otherAmountMin = ParamUtil.getString(request, "otherAmountMin" + i);
                String otherAmountMax = ParamUtil.getString(request, "otherAmountMax" + i);
                String otherRemarks = ParamUtil.getString(request, "otherRemarks" + i);
                AppSvcChargesDto appSvcChargesDto = new AppSvcChargesDto();
                appSvcChargesDto.setChargesCategory(otherChargesCategory);
                appSvcChargesDto.setChargesType(otherChargesType);
                appSvcChargesDto.setMinAmount(otherAmountMin);
                appSvcChargesDto.setMaxAmount(otherAmountMax);
                appSvcChargesDto.setRemarks(otherRemarks);
                if (StringUtil.isEmpty(chargesIndexNo)) {
                    appSvcChargesDto.setChargesIndexNo(UUID.randomUUID().toString());
                } else {
                    appSvcChargesDto.setChargesIndexNo(chargesIndexNo);
                }
                otherChargesDtos.add(appSvcChargesDto);
            }
        }
        appSvcChargesPageDto.setGeneralChargesDtos(generalChargesDtos);
        appSvcChargesPageDto.setOtherChargesDtos(otherChargesDtos);
        return appSvcChargesPageDto;
    }


    public static List<AppSvcPrincipalOfficersDto> genAppSvcPrincipalOfficersDtos(HttpServletRequest request) {
        return genKeyPersonnels(ApplicationConsts.PERSONNEL_PSN_TYPE_PO, "", request);
    }

    public static List<AppSvcPrincipalOfficersDto> genAppSvcNomineeDtos(HttpServletRequest request) {
        return genKeyPersonnels(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, "dpo", request);
    }

    /*
    public static List<AppSvcPrincipalOfficersDto> genAppSvcPrincipalOfficersDto(HttpServletRequest request,
            Boolean isGetDataFromPagePo, Boolean isGetDataFromPageDpo) {
        log.info(StringUtil.changeForLog("genAppSvcPrincipalOfficersDto start ...."));
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        String appType = appSubmissionDto.getAppType();
        boolean rfcOrRenew = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType);
        boolean needEdit = rfcOrRenew || isRfi;
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
        if (isGetDataFromPagePo) {
            log.info(StringUtil.changeForLog("get po data..."));
            String[] poExistingPsn = ParamUtil.getStrings(request, "poExistingPsn");
            String[] poLicPerson = ParamUtil.getStrings(request, "poLicPerson");
            String[] assignSelect = ParamUtil.getStrings(request, "poSelect");
            String[] salutation = ParamUtil.getStrings(request, "salutation");
            String[] name = ParamUtil.getStrings(request, "name");
            String[] idType = ParamUtil.getStrings(request, "idType");
            String[] idNo = ParamUtil.getStrings(request, "idNo");
            String[] nationality = ParamUtil.getStrings(request, "nationality");
            String[] designation = ParamUtil.getStrings(request, "designation");
            String[] otherDesignations = ParamUtil.getStrings(request, "otherDesignation");
            String[] mobileNo = ParamUtil.getStrings(request, "mobileNo");
            String[] officeTelNo = ParamUtil.getStrings(request, "officeTelNo");
            String[] emailAddress = ParamUtil.getStrings(request, "emailAddress");
            String[] poIsPartEdit = ParamUtil.getStrings(request, "poIsPartEdit");
            String[] poIndexNos = ParamUtil.getStrings(request, "poIndexNo");
            String[] loadingTypes = ParamUtil.getStrings(request, "loadingType");
            int length = 0;
            if (assignSelect != null) {
                length = assignSelect.length;
            }
            if (needEdit) {
                if (poIndexNos != null) {
                    length = poIndexNos.length;
                } else {
                    length = 0;
                }
            }
            for (int i = 0; i < length; i++) {
                boolean chooseExisting = false;
                boolean getPageData = false;
                String assign = assignSelect[i];
                String licPsn = poLicPerson[i];
                String loadingType = loadingTypes[i];
                boolean loadingByBlur = HcsaAppConst.NEW_PSN.equals(assign) && AppConsts.YES.equals(licPsn)
                        && ApplicationConsts.PERSON_LOADING_TYPE_BLUR.equals(loadingType);
                //for rfi,rfc,renew use
//                String existingPsn = poExistingPsn[i];
                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    if (assign != null) {
                        if (isExistingPsn(assign, licPsn)) {
                            chooseExisting = true;
                        } else if (loadingByBlur) {
                            chooseExisting = true;
                        } else {
                            getPageData = true;
                        }
                    }
                } else if (needEdit) {
                    if (assign != null) {
                        String poIndexNo = poIndexNos[i];
                        if (!StringUtil.isEmpty(poIndexNo)) {
                            //not click edit
                            if (AppConsts.NO.equals(poIsPartEdit[i])) {
                                appSvcPrincipalOfficersDto = getPsnByIndexNo(appSvcRelatedInfoDto, poIndexNo,
                                        ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
                                appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                                //change arr
                                poIndexNos = removeArrIndex(poIndexNos, i);
                                poIsPartEdit = removeArrIndex(poIsPartEdit, i);
                                poLicPerson = removeArrIndex(poLicPerson, i);
                                loadingTypes = removeArrIndex(loadingTypes, i);
                                //dropdown cannot disabled
                                assignSelect = removeArrIndex(assignSelect, i);
                                salutation = removeArrIndex(salutation, i);
                                idType = removeArrIndex(idType, i);
                                nationality = removeArrIndex(nationality, i);
                                designation = removeArrIndex(designation, i);
//                                existingPsn = removeArrIndex(existingPsn, i);
                                //change arr index
                                --i;
                                --length;
                                continue;
                            }
                        }
                        //isPartEdit->1.click edit button 2.add more psn
                        if (isExistingPsn(assign, licPsn)) {
                            //add cgo and choose existing
                            chooseExisting = true;
                        } else if (loadingByBlur) {
                            chooseExisting = true;
                        } else {
                            getPageData = true;
                        }
                    }

                } else {
                    log.info(StringUtil.changeForLog("The current type is not supported"));
                }
                log.info(StringUtil.changeForLog("chooseExisting:" + chooseExisting));
                log.info(StringUtil.changeForLog("getPageData:" + getPageData));
                String assignSel = assignSelect[i];
                if (chooseExisting) {
                    if (loadingByBlur) {
                        assignSel = ApplicationHelper.getPersonKey(nationality[i], idType[i], idNo[i]);
                    }
                    appSvcPrincipalOfficersDto = ApplicationHelper.getPsnInfoFromLic(request, assignSel);
                    appSvcPrincipalOfficersDto.setLoadingType(loadingType);
                    AppPsnEditDto appPsnEditDto;
                    try {
                        appPsnEditDto = ApplicationHelper.setNeedEditField(appSvcPrincipalOfficersDto);
                    } catch (Exception e) {
                        appPsnEditDto = new AppPsnEditDto();
                        log.error(e.getMessage(), e);
                    }
                    if (appPsnEditDto.isIdType()) {
                        setPsnValue(idType, i, appSvcPrincipalOfficersDto, "idType");
                    }
                    if (appPsnEditDto.isSalutation()) {
                        setPsnValue(salutation, i, appSvcPrincipalOfficersDto, "salutation");
                    }
                    if (appPsnEditDto.isNationality()) {
                        setPsnValue(nationality, i, appSvcPrincipalOfficersDto, "nationality");
                    }
                    if (appPsnEditDto.isDesignation()) {
                        setPsnValue(designation, i, appSvcPrincipalOfficersDto, "designation");
                    }
                    if (appPsnEditDto.isOtherDesignation()) {
                        if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(appSvcPrincipalOfficersDto.getDesignation())) {
                            setPsnValue(otherDesignations, i, appSvcPrincipalOfficersDto, "otherDesignation");
                        } else {
                            otherDesignations = removeArrIndex(otherDesignations, i);
                        }
                    }

                    if (appPsnEditDto.isName()) {
                        name = setPsnValue(name, i, appSvcPrincipalOfficersDto, "name");
                    }
                    if (appPsnEditDto.isIdNo()) {
                        idNo = setPsnValue(idNo, i, appSvcPrincipalOfficersDto, "idNo");
                    }
                    if (appPsnEditDto.isMobileNo()) {
                        mobileNo = setPsnValue(mobileNo, i, appSvcPrincipalOfficersDto, "mobileNo");
                    }
                    if (appPsnEditDto.isOfficeTelNo()) {
                        officeTelNo = setPsnValue(officeTelNo, i, appSvcPrincipalOfficersDto, "officeTelNo");
                    }
                    if (appPsnEditDto.isEmailAddr()) {
                        emailAddress = setPsnValue(emailAddress, i, appSvcPrincipalOfficersDto, "emailAddr");
                    }
                    String poIndexNo = poIndexNos[i];
                    if (!StringUtil.isEmpty(poIndexNo)) {
                        appSvcPrincipalOfficersDto.setIndexNo(poIndexNo);
                    }
                    if (StringUtil.isEmpty(appSvcPrincipalOfficersDto.getIndexNo())) {
                        appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                    }
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                    appSvcPrincipalOfficersDto.setLicPerson(true);
                    //appSvcPrincipalOfficersDto.setSelectDropDown(true);
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
                    appSvcPrincipalOfficersDto.setPsnEditDto(appPsnEditDto);
                    appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                    //change arr index
                    poExistingPsn = removeArrIndex(poExistingPsn, i);
                    poLicPerson = removeArrIndex(poLicPerson, i);
                    poIndexNos = removeArrIndex(poIndexNos, i);
                    loadingTypes = removeArrIndex(loadingTypes, i);
                    //dropdown cannot disabled
                    assignSelect = removeArrIndex(assignSelect, i);
                    salutation = removeArrIndex(salutation, i);
                    idType = removeArrIndex(idType, i);
                    nationality = removeArrIndex(nationality, i);
                    designation = removeArrIndex(designation, i);
                    --i;
                    --length;
                } else if (getPageData) {
                    String poIndexNo = poIndexNos[i];
                    if (StringUtil.isEmpty(poIndexNo)) {
                        appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                    } else {
                        appSvcPrincipalOfficersDto.setIndexNo(poIndexNo);
                    }
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                    appSvcPrincipalOfficersDto.setSalutation(getVal(salutation, i));
                    appSvcPrincipalOfficersDto.setName(getVal(name, i));
                    appSvcPrincipalOfficersDto.setIdType(getVal(idType, i));
                    appSvcPrincipalOfficersDto.setIdNo(StringUtil.toUpperCase(getVal(idNo, i)));
                    appSvcPrincipalOfficersDto.setNationality(getVal(nationality, i));
                    appSvcPrincipalOfficersDto.setDesignation(getVal(designation, i));
                    appSvcPrincipalOfficersDto.setOtherDesignation(getVal(otherDesignations, i));
                    appSvcPrincipalOfficersDto.setMobileNo(getVal(mobileNo, i));
                    appSvcPrincipalOfficersDto.setOfficeTelNo(getVal(officeTelNo, i));
                    String emailAddr = "";
                    if (emailAddress != null) {
                        String val = getVal(emailAddress, i);
                        if (!StringUtil.isEmpty(val)) {
                            emailAddr = StringUtil.viewHtml(val);
                        }
                    }
                    appSvcPrincipalOfficersDto.setEmailAddr(emailAddr);
                    if (needEdit && AppConsts.YES.equals(licPsn)) {
                        appSvcPrincipalOfficersDto.setLicPerson(true);
                        String personKey = ApplicationHelper.getPersonKey(appSvcPrincipalOfficersDto.getNationality(),
                                appSvcPrincipalOfficersDto.getIdType(), appSvcPrincipalOfficersDto.getIdNo());
                        AppSvcPrincipalOfficersDto licPerson = ApplicationHelper.getPsnInfoFromLic(request, personKey);
                        if (licPerson != null) {
                            appSvcPrincipalOfficersDto.setCurPersonelId(licPerson.getCurPersonelId());
                        }
                    }
                    appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                }
            }
        }
        //depo
        String deputySelect = ParamUtil.getString(request, "deputyPrincipalOfficer");
        if (AppConsts.YES.equals(deputySelect) && isGetDataFromPageDpo) {
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
            String[] deputyNationlity = ParamUtil.getStrings(request, "deputyNationality");
            String[] deputyMobileNo = ParamUtil.getStrings(request, "deputyMobileNo");
            String[] deputyOfficeTelNo = ParamUtil.getStrings(request, "deputyOfficeTelNo");
            String[] deputyEmailAddr = ParamUtil.getStrings(request, "deputyEmailAddr");
            String[] dpoIsPartEdit = ParamUtil.getStrings(request, "dpoIsPartEdit");
            String[] dpoIndexNos = ParamUtil.getStrings(request, "dpoIndexNo");
            String[] dpoLoadingTypes = ParamUtil.getStrings(request, "dpoLoadingType");
            int length = 0;
            if (assignSelect != null) {
                length = assignSelect.length;
            }
            if (needEdit) {
                if (dpoIndexNos != null) {
                    length = dpoIndexNos.length;
                } else {
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
                boolean loadingByBlur = HcsaAppConst.NEW_PSN.equals(assign) && AppConsts.YES.equals(licPsn)
                        && ApplicationConsts.PERSON_LOADING_TYPE_BLUR.equals(loadingType);
                //for rfi,rfc,renew use
                String existingPsn = dpoExistingPsn[i];
                //new and not rfi
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    if (assign != null) {
                        if (isExistingPsn(assign, licPsn)) {
                            chooseExisting = true;
                        } else if (loadingByBlur) {
                            chooseExisting = true;
                        } else {
                            getPageData = true;
                        }
                    }
                } else if (needEdit) {
                    if (assign != null) {
                        String dpoIndexNo = dpoIndexNos[i];
                        if (!StringUtil.isEmpty(dpoIndexNo)) {
                            //not click edit
                            if (AppConsts.NO.equals(dpoIsPartEdit[i])) {
                                appSvcPrincipalOfficersDto = getPsnByIndexNo(appSvcRelatedInfoDto, dpoIndexNo,
                                        ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
                                appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                                //change arr
                                dpoIndexNos = removeArrIndex(dpoIndexNos, i);
                                dpoIsPartEdit = removeArrIndex(dpoIsPartEdit, i);
                                dpoLicPerson = removeArrIndex(dpoLicPerson, i);
                                dpoLoadingTypes = removeArrIndex(dpoLoadingTypes, i);
                                //dropdown cannot disabled
                                assignSelect = removeArrIndex(assignSelect, i);
                                deputySalutation = removeArrIndex(deputySalutation, i);
                                deputyIdType = removeArrIndex(deputyIdType, i);
                                deputyNationlity = removeArrIndex(deputyNationlity, i);
                                deputyDesignation = removeArrIndex(deputyDesignation, i);
//                                existingPsn = removeArrIndex(existingPsn, i);
                                //change arr index
                                --i;
                                --length;
                                continue;
                            }
                        }
                        //isPartEdit->1.click edit button 2.add more psn
                        if (isExistingPsn(assign, licPsn)) {
                            //add cgo and choose existing
                            chooseExisting = true;
                        } else if (loadingByBlur) {
                            chooseExisting = true;
                        } else {
                            getPageData = true;
                        }
                    }
                } else {
                    log.info(StringUtil.changeForLog("The current type is not supported"));
                }
                log.info(StringUtil.changeForLog("chooseExisting:" + chooseExisting));
                log.info(StringUtil.changeForLog("getPageData:" + getPageData));
                String assignSel = assignSelect[i];
                if (chooseExisting) {
                    if (loadingByBlur) {
                        assignSel = ApplicationHelper.getPersonKey(deputyNationlity[i], deputyIdType[i], deputyIdNo[i]);
                    }
                    appSvcPrincipalOfficersDto = ApplicationHelper.getPsnInfoFromLic(request, assignSel);
                    appSvcPrincipalOfficersDto.setLoadingType(loadingType);
                    String dpoIndexNo = dpoIndexNos[i];
                    if (StringUtil.isEmpty(dpoIndexNo)) {
                        appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                    } else {
                        appSvcPrincipalOfficersDto.setIndexNo(dpoIndexNo);
                    }
                    AppPsnEditDto appPsnEditDto;
                    try {
                        appPsnEditDto = ApplicationHelper.setNeedEditField(appSvcPrincipalOfficersDto);
                    } catch (Exception e) {
                        appPsnEditDto = new AppPsnEditDto();
                        log.error(e.getMessage(), e);
                    }
                    if (appPsnEditDto.isIdType()) {
                        setPsnValue(deputyIdType, i, appSvcPrincipalOfficersDto, "idType");
                    }
                    if (appPsnEditDto.isSalutation()) {
                        setPsnValue(deputySalutation, i, appSvcPrincipalOfficersDto, "salutation");
                    }
                    if (appPsnEditDto.isNationality()) {
                        setPsnValue(deputyNationlity, i, appSvcPrincipalOfficersDto, "nationality");
                    }
                    if (appPsnEditDto.isDesignation()) {
                        setPsnValue(deputyDesignation, i, appSvcPrincipalOfficersDto, "designation");
                    }
                    if (appPsnEditDto.isOtherDesignation() && MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(
                            appSvcPrincipalOfficersDto.getDesignation())) {
                        setPsnValue(deputyOtherDesignations, i, appSvcPrincipalOfficersDto, "otherDesignation");
                    }
                    //input
                    if (appPsnEditDto.isName()) {
                        deputyName = setPsnValue(deputyName, i, appSvcPrincipalOfficersDto, "name");
                    }
                    if (appPsnEditDto.isIdNo()) {
                        deputyIdNo = setPsnValue(deputyIdNo, i, appSvcPrincipalOfficersDto, "idNo");
                    }
                    if (appPsnEditDto.isMobileNo()) {
                        deputyMobileNo = setPsnValue(deputyMobileNo, i, appSvcPrincipalOfficersDto, "mobileNo");
                    }
                    if (appPsnEditDto.isOfficeTelNo()) {
                        deputyOfficeTelNo = setPsnValue(deputyOfficeTelNo, i, appSvcPrincipalOfficersDto,
                                "officeTelNo");
                    }
                    if (appPsnEditDto.isEmailAddr()) {
                        deputyEmailAddr = setPsnValue(deputyEmailAddr, i, appSvcPrincipalOfficersDto, "emailAddr");
                    }
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                    appSvcPrincipalOfficersDto.setLicPerson(true);
                    //appSvcPrincipalOfficersDto.setSelectDropDown(true);
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
                    appSvcPrincipalOfficersDto.setPsnEditDto(appPsnEditDto);
                    appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                    //change arr index
                    dpoLicPerson = removeArrIndex(dpoLicPerson, i);
                    dpoExistingPsn = removeArrIndex(dpoExistingPsn, i);
                    dpoLoadingTypes = removeArrIndex(dpoLoadingTypes, i);
                    //dropdown cannot disabled
                    assignSelect = removeArrIndex(assignSelect, i);
                    deputySalutation = removeArrIndex(deputySalutation, i);
                    deputyIdType = removeArrIndex(deputyIdType, i);
                    deputyNationlity = removeArrIndex(deputyNationlity, i);
                    deputyDesignation = removeArrIndex(deputyDesignation, i);
                    --i;
                    --length;
                } else if (getPageData) {
                    String dpoIndexNo = getVal(dpoIndexNos, i);
                    if (StringUtil.isEmpty(dpoIndexNo)) {
                        appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                    } else {
                        appSvcPrincipalOfficersDto.setIndexNo(dpoIndexNo);
                    }
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                    appSvcPrincipalOfficersDto.setSalutation(getVal(deputySalutation, i));
                    appSvcPrincipalOfficersDto.setName(getVal(deputyName, i));
                    appSvcPrincipalOfficersDto.setIdType(getVal(deputyIdType, i));
                    appSvcPrincipalOfficersDto.setIdNo(StringUtil.toUpperCase(getVal(deputyIdNo, i)));
                    appSvcPrincipalOfficersDto.setNationality(getVal(deputyNationlity, i));
                    appSvcPrincipalOfficersDto.setDesignation(getVal(deputyDesignation, i));
                    appSvcPrincipalOfficersDto.setOtherDesignation(getVal(deputyOtherDesignations, i));
                    appSvcPrincipalOfficersDto.setMobileNo(getVal(deputyMobileNo, i));
                    appSvcPrincipalOfficersDto.setOfficeTelNo(getVal(deputyOfficeTelNo, i));
                    String emailAddr = "";
                    if (deputyEmailAddr != null) {
                        String val = getVal(deputyEmailAddr, i);
                        if (!StringUtil.isEmpty(val)) {
                            emailAddr = StringUtil.viewHtml(val);
                        }
                    }
                    appSvcPrincipalOfficersDto.setEmailAddr(emailAddr);
                    if (needEdit && AppConsts.YES.equals(licPsn)) {
                        appSvcPrincipalOfficersDto.setLicPerson(true);
                        String personKey = ApplicationHelper.getPersonKey(appSvcPrincipalOfficersDto.getNationality(),
                                appSvcPrincipalOfficersDto.getIdType(), appSvcPrincipalOfficersDto.getIdNo());
                        AppSvcPrincipalOfficersDto licPerson = ApplicationHelper.getPsnInfoFromLic(request, personKey);
                        if (licPerson != null) {
                            appSvcPrincipalOfficersDto.setCurPersonelId(licPerson.getCurPersonelId());
                        }
                    }
                    appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                }
            }
        }
        log.info(StringUtil.changeForLog("genAppSvcPrincipalOfficersDto end ...."));
        return appSvcPrincipalOfficersDtos;
    }*/

    private static AppSvcPrincipalOfficersDto getPsnByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo,
            String psnType) {
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)) {
            List<AppSvcPrincipalOfficersDto> psnDtos = null;
            if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)) {
                psnDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
            } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(psnType)) {
                psnDtos = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
            }
            if (!IaisCommonUtils.isEmpty(psnDtos)) {
                for (AppSvcPrincipalOfficersDto psnDto : psnDtos) {
                    if (indexNo.equals(psnDto.getIndexNo())) {
                        return psnDto;
                    }
                }
            }
        }
        return new AppSvcPrincipalOfficersDto();
    }

    public static List<AppSvcPrincipalOfficersDto> genAppSvcGovernanceOfficersDto(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("genAppSvcCgoDto start ...."));
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = genKeyPersonnels(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, "", request);
        log.info(StringUtil.changeForLog("genAppSvcCgoDto end ...."));
        return appSvcCgoDtoList;
    }

    public static List<AppSvcPrincipalOfficersDto> genKeyPersonnels(String psnType, String prefix, HttpServletRequest request) {
        List<AppSvcPrincipalOfficersDto> personList = IaisCommonUtils.genNewArrayList();
        String[] licPerson = ParamUtil.getStrings(request, prefix + "licPerson");
        String[] isPartEdit = ParamUtil.getStrings(request, prefix + "isPartEdit");
        String[] indexNos = ParamUtil.getStrings(request, prefix + "indexNo");
        String[] assignSelect = ParamUtil.getStrings(request, prefix + "assignSelVal");
        int size = 0;
        if (assignSelect != null && assignSelect.length > 0) {
            size = assignSelect.length;
        }
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
        String appType = appSubmissionDto.getAppType();
        String currSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(appSubmissionDto, currSvcId,
                appSubmissionDto.getRfiAppNo());

        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        for (int i = 0; i < size; i++) {
            AppSvcPrincipalOfficersDto person = null;
            String indexNo = indexNos[i];
            String assign = assignSelect[i];
            String licPsn = licPerson[i];
            boolean pageData = false;
            boolean nonChanged = false;
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                pageData = true;
            } else if (AppConsts.YES.equals(getVal(isPartEdit, i))) {
                pageData = true;
            } else if (!StringUtil.isEmpty(indexNo)) {
                nonChanged = true;
            }
            log.info(StringUtil.changeForLog("Non changed:" + nonChanged));
            log.info(StringUtil.changeForLog("PageData:" + pageData));
            if (nonChanged) {
                person = ApplicationHelper.getKeyPersonnel(psnType, currSvcInfoDto).stream()
                        .filter(dto -> Objects.equals(indexNo, dto.getIndexNo()))
                        .findAny()
                        .orElseGet(() -> {
                            AppSvcPrincipalOfficersDto dto = new AppSvcPrincipalOfficersDto();
                            dto.setIndexNo(indexNo);
                            return dto;
                        });
            } else if (pageData) {
                AppPsnEditDto appPsnEditDto = null;
                if (isExistingPsn(assign, licPsn)) {
                    person = ApplicationHelper.getPsnInfoFromLic(request, assign);
                    appPsnEditDto = ApplicationHelper.setNeedEditField(person);
                }
                boolean needLoadName = isNeedLoadName(appType, licPsn);
                person = genKeyPersonnel(person, appPsnEditDto, prefix, String.valueOf(i), needLoadName, request);
            } else {
                log.info(StringUtil.changeForLog("Invalid data!!!"));
                continue;
            }
            if (StringUtil.isEmpty(indexNo)) {
                person.setIndexNo(UUID.randomUUID().toString());
            } else {
                person.setIndexNo(indexNo);
            }
            person.setLicPerson(AppConsts.YES.equals(licPsn));
            person.setAssignSelect(assign);
            person.setPsnType(psnType);
            personList.add(person);
        }
        log.info(StringUtil.changeForLog(StringUtil.changeForLog(psnType + " size: " + personList.size())));
        return personList;
    }

    private static AppSvcPrincipalOfficersDto genKeyPersonnel(AppSvcPrincipalOfficersDto person, AppPsnEditDto appPsnEditDto,
            String prefix, String suffix, boolean needLoadName, HttpServletRequest request) {
        if (person == null) {
            person = new AppSvcPrincipalOfficersDto();
        }
        setPsnValue(person, appPsnEditDto, "salutation", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "name", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "idType", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "idNo", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "nationality", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "designation", prefix, suffix, request);

        if (appPsnEditDto == null || appPsnEditDto.isOtherDesignation()) {
            if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(person.getDesignation())) {
                String otherDesignation = ParamUtil.getString(request, "otherDesignation" + suffix);
                person.setOtherDesignation(otherDesignation);
            } else {
                person.setOtherDesignation(null);
            }
        }
        setPsnValue(person, appPsnEditDto, "noRegWithProfBoard", prefix, suffix, request);
        String registered = ParamUtil.getString(request,prefix + "noRegWithProfBoard" + suffix);
        person.setNoRegWithProfBoard(registered);

        setPsnValue(person, appPsnEditDto, "professionBoard", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "professionType", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "profRegNo", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "typeOfCurrRegi", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "currRegiDate", prefix, suffix, true, request);
        setPsnValue(person, appPsnEditDto, "praCerEndDate", prefix, suffix, true, request);
        setPsnValue(person, appPsnEditDto, "typeOfRegister", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "specialityOther", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "specialtyGetDate", prefix, suffix, true, request);
        setPsnValue(person, appPsnEditDto, "otherQualification", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "mobileNo", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "emailAddr", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "holdCerByEMS", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "aclsExpiryDate", prefix, suffix, true, request);
        setPsnValue(person, appPsnEditDto, "bclsExpiryDate", prefix, suffix, true, request);
        setPsnValue(person, appPsnEditDto, "relevantExperience", prefix, suffix, request);

        if (person.getPsnEditDto() == null) {
            if (appPsnEditDto == null) {
                appPsnEditDto = ApplicationHelper.setNeedEditField(person);
            }
            person.setPsnEditDto(appPsnEditDto);
        }
        String bclsExpiryDateStr = ParamUtil.getString(request,prefix + "bclsExpiryDate" + suffix);
        person.setBclsExpiryDateStr(bclsExpiryDateStr);
        String aclsExpiryDateStr = ParamUtil.getString(request,prefix + "aclsExpiryDate" + suffix);
        person.setAclsExpiryDateStr(aclsExpiryDateStr);

        String profRegNo = person.getProfRegNo();
        if (!StringUtil.isEmpty(profRegNo)) {
            ProfessionalResponseDto professionalResponseDto = getAppCommService().retrievePrsInfo(profRegNo);
            if (professionalResponseDto != null) {
                if (IaisCommonUtils.isEmpty(professionalResponseDto.getSpecialty())) {
                    person.setSpeciality("");
                } else {
                    person.setSpeciality(professionalResponseDto.getSpecialty().get(0));
                }
                if (IaisCommonUtils.isEmpty(professionalResponseDto.getSubspecialty())) {
                    person.setSubSpeciality("");
                } else {
                    person.setSubSpeciality(professionalResponseDto.getSubspecialty().get(0));
                }
                String specialtyGetDateStr = "";
                List<String> entryDateSpecialist = professionalResponseDto.getEntryDateSpecialist();
                if (entryDateSpecialist != null && entryDateSpecialist.size() > 0) {
                    specialtyGetDateStr = entryDateSpecialist.get(0);
                }
                person.setSpecialtyGetDateStr(specialtyGetDateStr);
                if (StringUtil.isEmpty(specialtyGetDateStr)) {
                    person.setSpecialtyGetDate(null);
                } else {
                    Date date = DateUtil.parseDate(specialtyGetDateStr, Formatter.DATE);
                    person.setSpecialtyGetDate(date);
                }

                if (IaisCommonUtils.isEmpty(professionalResponseDto.getQualification())) {
                    person.setQualification("");
                } else {
                    person.setQualification(professionalResponseDto.getQualification().get(0));
                }

                String typeOfCurrRegi = "";
                String currRegiDateStr = "";
                String praCerEndDateStr = "";
                String typeOfRegister = "";
                List<RegistrationDto> registrationDtos = professionalResponseDto.getRegistration();
                if (registrationDtos != null && registrationDtos.size() > 0) {
                    RegistrationDto registrationDto = registrationDtos.get(0);
                    typeOfCurrRegi = registrationDto.getRegistrationType();
                    currRegiDateStr = registrationDto.getRegStartDate();
                    praCerEndDateStr = registrationDto.getPcEndDate();
                    typeOfRegister = registrationDto.getRegisterType();
                }
                person.setTypeOfCurrRegi(typeOfCurrRegi);
                person.setTypeOfRegister(typeOfRegister);
                person.setCurrRegiDateStr(currRegiDateStr);
                if (StringUtil.isEmpty(currRegiDateStr)) {
                    person.setCurrRegiDate(null);
                } else {
                    Date date = DateUtil.parseDate(currRegiDateStr, Formatter.DATE);
                    person.setCurrRegiDate(date);
                }
                person.setPraCerEndDateStr(praCerEndDateStr);
                if (StringUtil.isEmpty(praCerEndDateStr)) {
                    person.setPraCerEndDate(null);
                } else {
                    Date date = DateUtil.parseDate(praCerEndDateStr, Formatter.DATE);
                    person.setPraCerEndDate(date);
                }
                if (needLoadName && !StringUtil.isEmpty(professionalResponseDto.getName())) {
                    person.setName(professionalResponseDto.getName());
                }
            }
        }
        return person;
    }

    public static List<AppSvcSpecialServiceInfoDto> getAppSvcSpecialServiceInfoList(HttpServletRequest request,
            List<AppSvcSpecialServiceInfoDto> appSvcSpecialServiceInfoDtoList) {
        if (!IaisCommonUtils.isEmpty(appSvcSpecialServiceInfoDtoList)) {
            String prefix = "";
            int i = 0;
            for (AppSvcSpecialServiceInfoDto appSvcSpecialServiceInfoDto : appSvcSpecialServiceInfoDtoList) {
                int j = 0;
                for (SpecialServiceSectionDto specialServiceSectionDto : appSvcSpecialServiceInfoDto.getSpecialServiceSectionDtoList()) {
                    Map<String, Integer> maxCount = specialServiceSectionDto.getMaxCount();
                    int cgomaxCount=maxCount.get(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
                    int nicMaxCount = maxCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE);
                    int slMaxCount = maxCount.get(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
                    int rsoMaxCount = maxCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER);
                    int drMaxCount = maxCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_DR);
                    int mpMaxCount = maxCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST);
                    int rpMaxCount = maxCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL);
                    int nmMaxCount = maxCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NM);
                    int diMaxCount = maxCount.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR);
                    int nuMaxCount = maxCount.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR);
                    if (cgomaxCount!=0){
                        List<AppSvcPrincipalOfficersDto> dtos = genKeyPersonnels(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, prefix + i + j + "cgo", request);
                        specialServiceSectionDto.setAppSvcCgoDtoList(dtos);
                    }
                    if (slMaxCount != 0) {
                        List<AppSvcPersonnelDto> personnelDtoList = IaisCommonUtils.genNewArrayList();
                        int Length = ParamUtil.getInt(request, prefix + i + j + ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER+"Length");
                        for (int x = 0; x < Length; x++) {
                            AppSvcPersonnelDto appSvcPersonnelDto = getAppSvcPersonnelParam(request, prefix + i + j+"sl",
                                    prefix + x, ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
                            personnelDtoList.add(appSvcPersonnelDto);
                        }
                        specialServiceSectionDto.setAppSvcSectionLeaderList(personnelDtoList);
                    }
                    if (nicMaxCount != 0) {
                        List<AppSvcPersonnelDto> appSvcNurseDtoList = IaisCommonUtils.genNewArrayList();
                        int nicLength = ParamUtil.getInt(request, prefix + i + j + ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE+"Length");
                        for (int x = 0; x < nicLength; x++) {
                            AppSvcPersonnelDto appSvcPersonnelDto = getAppSvcPersonnelParam(request, prefix + i + j + "nic",
                                    prefix + x, ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_NURSE_IN_CHARGE);
                            appSvcNurseDtoList.add(appSvcPersonnelDto);
                        }
                        specialServiceSectionDto.setAppSvcNurseDtoList(appSvcNurseDtoList);
                    }
                    if (rsoMaxCount != 0) {
                        List<AppSvcPersonnelDto> appSvcPersonnelDtoList = IaisCommonUtils.genNewArrayList();
                        int length = ParamUtil.getInt(request, prefix + i + j + ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER+"Length");
                        for (int x = 0; x < length; x++) {
                            AppSvcPersonnelDto appSvcPersonnelDto = getAppSvcPersonnelParam(request, prefix + i + j + "rso",
                                    prefix + x,"");
                            appSvcPersonnelDtoList.add(appSvcPersonnelDto);
                        }
                        specialServiceSectionDto.setAppSvcRadiationSafetyOfficerDtoList(appSvcPersonnelDtoList);
                    }
                    if (drMaxCount != 0) {
                        List<AppSvcPersonnelDto> appSvcPersonnelDtoList = IaisCommonUtils.genNewArrayList();
                        int length = ParamUtil.getInt(request, prefix + i + j + ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_DR+"Length");
                        for (int x = 0; x < length; x++) {
                            AppSvcPersonnelDto appSvcPersonnelDto = getAppSvcPersonnelParam(request, prefix + i + j + "dr",
                                    prefix + x,"");
                            appSvcPersonnelDtoList.add(appSvcPersonnelDto);
                        }
                        specialServiceSectionDto.setAppSvcDiagnosticRadiographerDtoList(appSvcPersonnelDtoList);
                    }
                    if (mpMaxCount != 0) {
                        List<AppSvcPersonnelDto> appSvcPersonnelDtoList = IaisCommonUtils.genNewArrayList();
                        int length = ParamUtil.getInt(request, prefix + i + j + ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST+"Length");
                        for (int x = 0; x < length; x++) {
                            AppSvcPersonnelDto appSvcPersonnelDto = getAppSvcPersonnelParam(request, prefix + i + j + "mp",
                                    prefix + x,"");
                            appSvcPersonnelDtoList.add(appSvcPersonnelDto);
                        }
                        specialServiceSectionDto.setAppSvcMedicalPhysicistDtoList(appSvcPersonnelDtoList);
                    }
                    if (rpMaxCount != 0) {
                        List<AppSvcPersonnelDto> appSvcPersonnelDtoList = IaisCommonUtils.genNewArrayList();
                        int length = ParamUtil.getInt(request, prefix + i + j + ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL+"Length");
                        for (int x = 0; x < length; x++) {
                            AppSvcPersonnelDto appSvcPersonnelDto = getAppSvcPersonnelParam(request, prefix + i + j + "rp",
                                    prefix + x,"");
                            appSvcPersonnelDtoList.add(appSvcPersonnelDto);
                        }
                        specialServiceSectionDto.setAppSvcRadiationPhysicistDtoList(appSvcPersonnelDtoList);
                    }
                    if (nmMaxCount != 0) {
                        List<AppSvcPersonnelDto> appSvcPersonnelDtoList = IaisCommonUtils.genNewArrayList();
                        int length = ParamUtil.getInt(request, prefix + i + j + ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NM+"Length");
                        for (int x = 0; x < length; x++) {
                            AppSvcPersonnelDto appSvcPersonnelDto = getAppSvcPersonnelParam(request, prefix + i + j + "nm",
                                    prefix + x,"");
                            appSvcPersonnelDtoList.add(appSvcPersonnelDto);
                        }
                        specialServiceSectionDto.setAppSvcNMTechnologistDtoList(appSvcPersonnelDtoList);
                    }
                    if (diMaxCount != 0) {
                        List<AppSvcPersonnelDto> appSvcDirectorDtoList = IaisCommonUtils.genNewArrayList();
                        int diLength = ParamUtil.getInt(request, prefix + i + j +ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR+"Length");
                        for (int x = 0; x < diLength; x++) {
                            AppSvcPersonnelDto appSvcPersonnelDto = getAppSvcPersonnelParam(request, prefix + i + j + "dir",
                                    prefix + x, ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR);
                            appSvcDirectorDtoList.add(appSvcPersonnelDto);
                        }
                        specialServiceSectionDto.setAppSvcDirectorDtoList(appSvcDirectorDtoList);
                    }
                    if (nuMaxCount != 0) {
                        int nuLength = ParamUtil.getInt(request, prefix + i + j +ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR+ "Length");
                        List<AppSvcPersonnelDto> appSvcNurseDirectorDtoList = IaisCommonUtils.genNewArrayList();
                        for (int x = 0; x < nuLength; x++) {
                            AppSvcPersonnelDto appSvcPersonnelDto = getAppSvcPersonnelParam(request, prefix + i + j + "nur",
                                    prefix + x, ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR);
                            appSvcNurseDirectorDtoList.add(appSvcPersonnelDto);
                        }
                        specialServiceSectionDto.setAppSvcNurseDirectorDtoList(appSvcNurseDirectorDtoList);
                    }
                    if (!IaisCommonUtils.isEmpty(specialServiceSectionDto.getAppSvcSuplmFormDto().getAppSvcSuplmGroupDtoList())) {
                        setAppSvcSuplmFormDto(specialServiceSectionDto.getAppSvcSuplmFormDto(), prefix + i + j, request);
                    }
                    j++;
                }
                i++;
            }
        }
        return appSvcSpecialServiceInfoDtoList;
    }


    private static void setPsnValue(AppSvcPrincipalOfficersDto person, AppPsnEditDto appPsnEditDto, String fieldName,
            String prefix, String suffix, HttpServletRequest request) {
        setPsnValue(person, appPsnEditDto, fieldName, prefix, suffix, false, request);
    }

    private static void setPsnValue(AppSvcPrincipalOfficersDto person, AppPsnEditDto appPsnEditDto, String fieldName,
            String prefix, String suffix, boolean isDate, HttpServletRequest request) {
        if (appPsnEditDto != null) {
            boolean canSet = ReflectionUtil.getPropertyObj(appPsnEditDto, fieldName);
            if (!canSet) {
                return;
            }
        }
        if (isDate) {
            String data = ParamUtil.getString(request, prefix + fieldName + suffix);
            Date value = null;
            if (CommonValidator.isDate(data)) {
                try {
                    value = Formatter.parseDate(data);
                } catch (ParseException e) {
                    log.info(StringUtil.changeForLog(e.getMessage()), e);
                }
            }
            ReflectionUtil.setPropertyObj(fieldName + "Str", data, person);
            ReflectionUtil.setPropertyObj(fieldName, value, person);
        } else {
            String data = ParamUtil.getString(request, prefix + fieldName + suffix);
            ReflectionUtil.setPropertyObj(fieldName, data, person);
        }
    }

    private static String[] setPsnValue(String[] arr, int i, AppSvcPrincipalOfficersDto person, String fieldName) {
        if (arr == null || arr.length <= i) {
            return new String[0];
        }
        ReflectionUtil.setPropertyObj(fieldName, arr[i], person);
        return removeArrIndex(arr, i);
    }

   /* private static AppSvcPrincipalOfficersDto getAppSvcCgoByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo) {
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
    }*/

    public static AppSvcPersonnelDto getAppSvcPersonnelParam(HttpServletRequest request, String prefix, String suffix,
            String personnelType) {

        AppSvcPersonnelDto svcPersonnelDto = ControllerHelper.get(request, AppSvcPersonnelDto.class, prefix, suffix);

        if (StringUtil.isEmpty(svcPersonnelDto.getIndexNo())) {
            svcPersonnelDto.setIndexNo(UUID.randomUUID().toString());
        }
        if ("SP999".equals(prefix)) {
            svcPersonnelDto.setPersonnelType(ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS);
        } else if (StringUtil.isNotEmpty(personnelType)) {
            svcPersonnelDto.setPersonnelType(personnelType);
        } else if ("".equals(prefix)||"".equals(personnelType)) {
        }  else {
            svcPersonnelDto.setPersonnelType(prefix);
        }
        String profRegNos = svcPersonnelDto.getProfRegNo();
        if (!StringUtil.isEmpty(profRegNos)) {
            ProfessionalResponseDto professionalResponseDto = getAppCommService().retrievePrsInfo(profRegNos);
            if (professionalResponseDto != null) {
                if (IaisCommonUtils.isEmpty(professionalResponseDto.getSpecialty())) {
                    svcPersonnelDto.setSpeciality("");
                } else {
                    svcPersonnelDto.setSpeciality(professionalResponseDto.getSpecialty().get(0));
                }
                if (IaisCommonUtils.isEmpty(professionalResponseDto.getSubspecialty())) {
                    svcPersonnelDto.setSubSpeciality("");
                } else {
                    svcPersonnelDto.setSubSpeciality(professionalResponseDto.getSubspecialty().get(0));
                }
                String specialtyGetDateStr = "";
                List<String> entryDateSpecialist = professionalResponseDto.getEntryDateSpecialist();
                if (entryDateSpecialist != null && entryDateSpecialist.size() > 0) {
                    specialtyGetDateStr = entryDateSpecialist.get(0);
                }
                svcPersonnelDto.setSpecialtyGetDate(specialtyGetDateStr);
                if (StringUtil.isEmpty(specialtyGetDateStr)) {
                    svcPersonnelDto.setSpecialtyGetDate(null);
                } else {
                    svcPersonnelDto.setSpecialtyGetDate(specialtyGetDateStr);
                }
                if (IaisCommonUtils.isEmpty(professionalResponseDto.getQualification())) {
                    svcPersonnelDto.setQualification("");
                } else {
                    svcPersonnelDto.setQualification(professionalResponseDto.getQualification().get(0));
                }
                String typeOfCurrRegis = "";
                String currRegiDateStrs = "";
                String praCerEndDateStrs = "";
                String typeOfRegisters = "";
                List<RegistrationDto> registrationDtos = professionalResponseDto.getRegistration();
                if (registrationDtos != null && registrationDtos.size() > 0) {
                    RegistrationDto registrationDto = registrationDtos.get(0);
                    typeOfCurrRegis = registrationDto.getRegistrationType();
                    currRegiDateStrs = registrationDto.getRegStartDate();
                    praCerEndDateStrs = registrationDto.getPcEndDate();
                    typeOfRegisters = registrationDto.getRegisterType();
                }
                svcPersonnelDto.setTypeOfCurrRegi(typeOfCurrRegis);
                svcPersonnelDto.setTypeOfRegister(typeOfRegisters);
                svcPersonnelDto.setCurrRegiDate(currRegiDateStrs);
                svcPersonnelDto.setPraCerEndDate(praCerEndDateStrs);
                if (!StringUtil.isEmpty(professionalResponseDto.getName())) {
                    svcPersonnelDto.setName(professionalResponseDto.getName());
                }
            }
        }
        return svcPersonnelDto;
    }

    public static SvcPersonnelDto genAppSvcPersonnelDtoList(HttpServletRequest request, SvcPersonnelDto
            svcPersonnelDto) {
        if (StringUtil.isEmpty(svcPersonnelDto)) {
            svcPersonnelDto = new SvcPersonnelDto();
        }
        List<AppSvcPersonnelDto> normalList = IaisCommonUtils.genNewArrayList();
        List<AppSvcPersonnelDto> nurseList = IaisCommonUtils.genNewArrayList();
        List<AppSvcPersonnelDto> specialList = IaisCommonUtils.genNewArrayList();
        List<AppSvcPersonnelDto> embryologistList = IaisCommonUtils.genNewArrayList();
        List<AppSvcPersonnelDto> arPractitionerList = IaisCommonUtils.genNewArrayList();
        int arCount = 0;
        int nuCount = 0;
        int emCount = 0;
        int speCount = 0;
        int noCount = 0;
        String[] arCountStrs = ParamUtil.getStrings(request, "SP002arCount");
        String[] nuCountStrs = ParamUtil.getStrings(request, "SP003nuCount");
        String[] emCountStrs = ParamUtil.getStrings(request, "SP001emCount");
        String[] speCountStrs = ParamUtil.getStrings(request, "SP000speCount");
        String[] noCountStrs = ParamUtil.getStrings(request, "SP999noCount");
        if (!StringUtil.isEmpty(arCountStrs)) {
            arCount = arCountStrs.length;
        }
        if (!StringUtil.isEmpty(nuCountStrs)) {
            nuCount = nuCountStrs.length;
        }
        if (!StringUtil.isEmpty(emCountStrs)) {
            emCount = emCountStrs.length;
        }
        if (!StringUtil.isEmpty(speCountStrs)) {
            speCount = speCountStrs.length;
        }
        if (!StringUtil.isEmpty(noCountStrs)) {
            noCount = noCountStrs.length;
        }

        for (int i = 0; i < arCount; i++) {
            AppSvcPersonnelDto dto = getAppSvcPersonnelParam(request, "SP002", String.valueOf(i), null);
            arPractitionerList.add(dto);
        }
        for (int i = 0; i < nuCount; i++) {
            AppSvcPersonnelDto dto = getAppSvcPersonnelParam(request, "SP003", String.valueOf(i), null);
            nurseList.add(dto);
        }
        for (int i = 0; i < emCount; i++) {
            AppSvcPersonnelDto dto = getAppSvcPersonnelParam(request, "SP001", String.valueOf(i), null);
            embryologistList.add(dto);
        }
        for (int i = 0; i < noCount; i++) {
            AppSvcPersonnelDto dto = getAppSvcPersonnelParam(request, "SP999", String.valueOf(i), null);
            normalList.add(dto);
        }
        for (int i = 0; i < speCount; i++) {
            AppSvcPersonnelDto dto = getAppSvcPersonnelParam(request, "", String.valueOf(i), null);
            specialList.add(dto);
        }

        if (arPractitionerList.size() != 0) {
            svcPersonnelDto.setArPractitionerList(arPractitionerList);
        }
        if (nurseList.size() != 0) {
            svcPersonnelDto.setNurseList(nurseList);
        }
        if (embryologistList.size() != 0) {
            svcPersonnelDto.setEmbryologistList(embryologistList);
        }
        if (specialList.size() != 0) {
            svcPersonnelDto.setSpecialList(specialList);
        }
        if (normalList.size() != 0) {
            svcPersonnelDto.setNormalList(normalList);
        }
        return svcPersonnelDto;
    }

    public static List<AppSvcPrincipalOfficersDto> genAppSvcKeyAppointmentHolder(HttpServletRequest request) {
        return genKeyPersonnels(ApplicationConsts.PERSONNEL_PSN_KAH, "", request);
    }

/*
    public static List<AppSvcPrincipalOfficersDto> genAppSvcMedAlertPerson(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("genAppSvcMedAlertPerson star ..."));
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
        String appType = appSubmissionDto.getAppType();
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        boolean rfcOrRenew = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType);
        boolean needEdit = rfcOrRenew || isRfi;
        String[] existingPsn = ParamUtil.getStrings(request, "existingPsn");
        String[] licPerson = ParamUtil.getStrings(request, "licPerson");
        String[] assignSelect = ParamUtil.getStrings(request, "assignSel");
        String[] salutation = ParamUtil.getStrings(request, "salutation");
        String[] name = ParamUtil.getStrings(request, "name");
        String[] idType = ParamUtil.getStrings(request, "idType");
        String[] idNo = ParamUtil.getStrings(request, "idNo");
        String[] nationality = ParamUtil.getStrings(request, "nationality");
        String[] mobileNo = ParamUtil.getStrings(request, "mobileNo");
        String[] emailAddress = ParamUtil.getStrings(request, "emailAddress");
        String[] isPartEdit = ParamUtil.getStrings(request, "isPartEdit");
        String[] mapIndexNos = ParamUtil.getStrings(request, "mapIndexNo");
        String[] loadingTypes = ParamUtil.getStrings(request, "loadingType");
        List<AppSvcPrincipalOfficersDto> medAlertPersons = IaisCommonUtils.genNewArrayList();
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
        int length = 0;
        if (assignSelect != null) {
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
            boolean loadingByBlur = HcsaAppConst.NEW_PSN.equals(assign) && AppConsts.YES.equals(licPsn)
                    && ApplicationConsts.PERSON_LOADING_TYPE_BLUR.equals(loadingType);
            //String existPsn = existingPsn[i];
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                if (assign != null) {
                    if (isExistingPsn(assign, licPsn)) {
                        chooseExisting = true;
                    } else if (loadingByBlur) {
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
                            appSvcPrincipalOfficersDto = getPsnByIndexNo(appSvcRelatedInfoDto, mapIndexNo,
                                    ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
                            medAlertPersons.add(appSvcPrincipalOfficersDto);
                            //change arr
                            mapIndexNos = removeArrIndex(mapIndexNos, i);
                            isPartEdit = removeArrIndex(isPartEdit, i);
                            licPerson = removeArrIndex(licPerson, i);
                            loadingTypes = removeArrIndex(loadingTypes, i);
                            //dropdown cannot disabled
                            assignSelect = removeArrIndex(assignSelect, i);
                            salutation = removeArrIndex(salutation, i);
                            nationality = removeArrIndex(nationality, i);
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
                    if (isExistingPsn(assign, licPsn)) {
                        //add cgo and choose existing
                        chooseExisting = true;
                    } else if (loadingByBlur) {
                        chooseExisting = true;
                    } else {
                        getPageData = true;
                    }
                }
            } else {
                log.info(StringUtil.changeForLog("The current type is not supported"));
            }
            log.info(StringUtil.changeForLog("chooseExisting:" + chooseExisting));
            log.info(StringUtil.changeForLog("getPageData:" + getPageData));
            String assignSel = assignSelect[i];
            if (chooseExisting) {
                if (loadingByBlur) {
                    assignSel = ApplicationHelper.getPersonKey(nationality[i], idType[i], idNo[i]);
                }
                appSvcPrincipalOfficersDto = ApplicationHelper.getPsnInfoFromLic(request, assignSel);
                appSvcPrincipalOfficersDto.setLoadingType(loadingType);
                try {
                    appPsnEditDto = ApplicationHelper.setNeedEditField(appSvcPrincipalOfficersDto);
                } catch (Exception e) {
                    clearAppPsnEditDto(appPsnEditDto);
                    log.error(e.getMessage(), e);
                }
                if (appPsnEditDto.isSalutation()) {
                    setPsnValue(salutation, i, appSvcPrincipalOfficersDto, "salutation");
                }
                if (appPsnEditDto.isIdType()) {
                    setPsnValue(idType, i, appSvcPrincipalOfficersDto, "idType");
                }
                //input
                if (appPsnEditDto.isName()) {
                    name = setPsnValue(name, i, appSvcPrincipalOfficersDto, "name");
                }
                if (appPsnEditDto.isIdNo()) {
                    idNo = setPsnValue(idNo, i, appSvcPrincipalOfficersDto, "idNo");
                }
                if (appPsnEditDto.isNationality()) {
                    setPsnValue(nationality, i, appSvcPrincipalOfficersDto, "nationality");
                }
                if (appPsnEditDto.isMobileNo()) {
                    mobileNo = setPsnValue(mobileNo, i, appSvcPrincipalOfficersDto, "mobileNo");
                }
                if (appPsnEditDto.isEmailAddr()) {
                    emailAddress = setPsnValue(emailAddress, i, appSvcPrincipalOfficersDto, "emailAddr");
                }
                String mapIndexNo = mapIndexNos[i];
                if (!StringUtil.isEmpty(mapIndexNo)) {
                    appSvcPrincipalOfficersDto.setIndexNo(mapIndexNo);
                }
                if (StringUtil.isEmpty(appSvcPrincipalOfficersDto.getIndexNo())) {
                    appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                }
                appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
                appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                appSvcPrincipalOfficersDto.setLicPerson(true);
                //appSvcPrincipalOfficersDto.setSelectDropDown(true);
                appSvcPrincipalOfficersDto.setPsnEditDto(appPsnEditDto);
                medAlertPersons.add(appSvcPrincipalOfficersDto);
                //change arr index
                licPerson = removeArrIndex(licPerson, i);
                existingPsn = removeArrIndex(existingPsn, i);
                loadingTypes = removeArrIndex(loadingTypes, i);
                //dropdown cannot disabled
                assignSelect = removeArrIndex(assignSelect, i);
                salutation = removeArrIndex(salutation, i);
                idType = removeArrIndex(idType, i);
                nationality = removeArrIndex(nationality, i);
                --i;
                --length;
            } else if (getPageData) {
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
                appSvcPrincipalOfficersDto.setNationality(nationality[i]);
                appSvcPrincipalOfficersDto.setMobileNo(mobileNo[i]);
                String emailAddr = "";
                if (emailAddress != null) {
                    if (!StringUtil.isEmpty(emailAddress[i])) {
                        emailAddr = StringUtil.viewHtml(emailAddress[i]);
                    }
                }
                appSvcPrincipalOfficersDto.setEmailAddr(emailAddr);
                if (needEdit && AppConsts.YES.equals(licPsn)) {
                    appSvcPrincipalOfficersDto.setLicPerson(true);
                    String personKey = ApplicationHelper.getPersonKey(appSvcPrincipalOfficersDto.getNationality(),
                            appSvcPrincipalOfficersDto.getIdType(), appSvcPrincipalOfficersDto.getIdNo());
                    AppSvcPrincipalOfficersDto licsPerson = ApplicationHelper.getPsnInfoFromLic(request, personKey);
                    if (licsPerson != null) {
                        appSvcPrincipalOfficersDto.setCurPersonelId(licsPerson.getCurPersonelId());
                    }
                }
                medAlertPersons.add(appSvcPrincipalOfficersDto);
            }
        }
        log.info(StringUtil.changeForLog("genAppSvcMedAlertPerson end ..."));
        return medAlertPersons;
    }*/

    public static List<AppSvcBusinessDto> genAppSvcBusinessDtoList(HttpServletRequest request,
            List<AppGrpPremisesDto> appGrpPremisesDtos,
            String appType) {
        List<AppSvcBusinessDto> appSvcBusinessDtos = IaisCommonUtils.genNewArrayList();
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
            int i = 0;
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                AppSvcBusinessDto appSvcBusinessDto = null;
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request, "isPartEdit" + i);
                String businessIndexNo = ParamUtil.getString(request, "businessIndexNo" + i);
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(businessIndexNo)) {
                    getDataByIndexNo = true;
                }
                log.debug("get data by index no. is {}", getDataByIndexNo);
                log.debug("get page data is {}", getPageData);
                if (getDataByIndexNo) {
                    appSvcBusinessDto = getAppSvcBusinessDtoByIndexNo(appSvcRelatedInfoDto, businessIndexNo);
                } else if (getPageData) {
                    appSvcBusinessDto = new AppSvcBusinessDto();
                    boolean getOHData = true;
                    String premisesType = appGrpPremisesDto.getPremisesType();
                    if (ApplicationConsts.PREMISES_TYPE_MOBILE.equals(premisesType) || ApplicationConsts.PREMISES_TYPE_REMOTE.equals(
                            premisesType)) {
                        getOHData = false;
                    }
                    String serviceCode = ParamUtil.getString(request, "currService" + i);
                    if (AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL.equals(
                            serviceCode) || AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL.equals(serviceCode)) {
                        getOHData = false;
                    }

                    List<OperationHoursReloadDto> weeklyDtoList = IaisCommonUtils.genNewArrayList();
                    List<OperationHoursReloadDto> phDtoList = IaisCommonUtils.genNewArrayList();
                    List<AppPremEventPeriodDto> eventList = IaisCommonUtils.genNewArrayList();

                    String businessName = ParamUtil.getString(request, "businessName" + i);
                    String contactNo = ParamUtil.getString(request, "contactNo" + i);
                    String emailAddr = ParamUtil.getString(request, "emailAddr" + i);

                    if (getOHData) {
                        int weeklyLength = ParamUtil.getInt(request, "weeklyLength" + i);
                        int phLength = ParamUtil.getInt(request, "phLength" + i);
                        int eventLength = ParamUtil.getInt(request, "eventLength" + i);

                        //weekly
                        for (int j = 0; j < weeklyLength; j++) {
                            OperationHoursReloadDto weeklyDto = new OperationHoursReloadDto();
                            String[] weeklyVal = ParamUtil.getStrings(request, "onSiteWeekly" + i + j);
                            String allDay = ParamUtil.getString(request, "onSiteWeeklyAllDay" + i + j);
                            //reload
                            String weeklySelect = StringUtil.arrayToString(weeklyVal);
                            weeklyDto.setSelectVal(weeklySelect);
                            if (weeklyVal != null) {
                                List<String> selectValList = Arrays.asList(weeklyVal);
                                weeklyDto.setSelectValList(selectValList);
                            }
                            if (AppConsts.TRUE.equals(allDay)) {
                                Time tim = Time.valueOf(LocalTime.of(0, 0, 0));
                                weeklyDto.setSelectAllDay(true);
                                weeklyDto.setStartFromHH(null);
                                weeklyDto.setStartFromMM(null);
                                weeklyDto.setStartFrom(tim);
                                weeklyDto.setEndToHH(null);
                                weeklyDto.setEndToMM(null);
                                weeklyDto.setEndTo(tim);
                            } else {
                                String weeklyStartHH = ParamUtil.getString(request, "onSiteWeeklyStartHH" + i + j);
                                String weeklyStartMM = ParamUtil.getString(request, "onSiteWeeklyStartMM" + i + j);
                                int weeklyStartH = weeklyStartHH != null ? Integer.parseInt(weeklyStartHH) : 0;
                                int weeklyStartM = weeklyStartMM != null ? Integer.parseInt(weeklyStartMM) : 0;
                                Time timStart = Time.valueOf(LocalTime.of(weeklyStartH, weeklyStartM, 0));

                                String weeklyEndHH = ParamUtil.getString(request, "onSiteWeeklyEndHH" + i + j);
                                String weeklyEndMM = ParamUtil.getString(request, "onSiteWeeklyEndMM" + i + j);
                                int weeklyEndH = weeklyEndHH != null ? Integer.parseInt(weeklyEndHH) : 0;
                                int weeklyEndM = weeklyEndMM != null ? Integer.parseInt(weeklyEndMM) : 0;
                                Time timEnd = Time.valueOf(LocalTime.of(weeklyEndH, weeklyEndM, 0));

                                weeklyDto.setStartFromHH(weeklyStartHH);
                                weeklyDto.setStartFromMM(weeklyStartMM);
                                weeklyDto.setStartFrom(timStart);
                                weeklyDto.setEndToHH(weeklyEndHH);
                                weeklyDto.setEndToMM(weeklyEndMM);
                                weeklyDto.setEndTo(timEnd);
                            }
                            weeklyDtoList.add(weeklyDto);
                        }

                        //ph
                        for (int j = 0; j < phLength; j++) {
                            OperationHoursReloadDto phDto = new OperationHoursReloadDto();
                            String[] phVal = ParamUtil.getStrings(request, "onSitePubHoliday" + i + j);
                            String allDay = ParamUtil.getString(request, "onSitePhAllDay" + i + j);
                            //reload
                            String phSelect = StringUtil.arrayToString(phVal);
                            phDto.setSelectVal(phSelect);
                            if (phSelect != null) {
                                List<String> selectValList = Arrays.asList(phVal);
                                phDto.setSelectValList(selectValList);
                            }
                            if (AppConsts.TRUE.equals(allDay)) {
                                Time tim = Time.valueOf(LocalTime.of(0, 0, 0));
                                phDto.setSelectAllDay(true);
                                phDto.setStartFromHH(null);
                                phDto.setStartFromMM(null);
                                phDto.setStartFrom(tim);
                                phDto.setEndToHH(null);
                                phDto.setEndToMM(null);
                                phDto.setEndTo(tim);
                                phDtoList.add(phDto);
                            } else {
                                String phStartHH = ParamUtil.getString(request, "onSitePhStartHH" + i + j);
                                String phStartMM = ParamUtil.getString(request, "onSitePhStartMM" + i + j);
                                int phStartH = phStartHH != null ? Integer.parseInt(phStartHH) : 0;
                                int phStartM = phStartMM != null ? Integer.parseInt(phStartMM) : 0;
                                Time timStart = Time.valueOf(LocalTime.of(phStartH, phStartM, 0));

                                String phEndHH = ParamUtil.getString(request, "onSitePhEndHH" + i + j);
                                String phEndMM = ParamUtil.getString(request, "onSitePhEndMM" + i + j);
                                int phEndH = phEndHH != null ? Integer.parseInt(phEndHH) : 0;
                                int phEndM = phEndMM != null ? Integer.parseInt(phEndMM) : 0;
                                Time timEnd = Time.valueOf(LocalTime.of(phEndH, phEndM, 0));

                                phDto.setStartFromHH(phStartHH);
                                phDto.setStartFromMM(phStartMM);
                                phDto.setStartFrom(timStart);
                                phDto.setEndToHH(phEndHH);
                                phDto.setEndToMM(phEndMM);
                                phDto.setEndTo(timEnd);
                                if (!StringUtil.isEmpty(phSelect) || !StringUtil.isEmpty(phStartHH) || !StringUtil.isEmpty(
                                        phStartMM) || !StringUtil.isEmpty(phEndHH) || !StringUtil.isEmpty(phEndMM)) {
                                    phDtoList.add(phDto);
                                }
                            }

                        }

                        //event
                        for (int j = 0; j < eventLength; j++) {
                            AppPremEventPeriodDto appPremEventPeriodDto = new AppPremEventPeriodDto();
                            String eventName = ParamUtil.getString(request, "onSiteEvent" + i + j);
                            String eventStartStr = ParamUtil.getString(request, "onSiteEventStart" + i + j);
                            Date eventStart = DateUtil.parseDate(eventStartStr, Formatter.DATE);
                            String eventEndStr = ParamUtil.getString(request, "onSiteEventEnd" + i + j);
                            Date eventEnd = DateUtil.parseDate(eventEndStr, Formatter.DATE);
                            appPremEventPeriodDto.setEventName(eventName);
                            appPremEventPeriodDto.setStartDate(eventStart);
                            appPremEventPeriodDto.setStartDateStr(eventStartStr);
                            appPremEventPeriodDto.setEndDate(eventEnd);
                            appPremEventPeriodDto.setEndDateStr(eventEndStr);
                            if (!StringUtil.isEmpty(eventName) || !StringUtil.isEmpty(eventStartStr) || !StringUtil.isEmpty(
                                    eventEndStr)) {
                                eventList.add(appPremEventPeriodDto);
                            }
                        }
                    }

                    appSvcBusinessDto.setCurrService(serviceCode);
                    appSvcBusinessDto.setBusinessName(businessName);
                    appSvcBusinessDto.setContactNo(contactNo);
                    appSvcBusinessDto.setEmailAddr(emailAddr);

                    if (getOHData) {
                        appSvcBusinessDto.setWeeklyDtoList(weeklyDtoList);
                        appSvcBusinessDto.setPhDtoList(phDtoList);
                        appSvcBusinessDto.setEventDtoList(eventList);

                    } else {
                        appSvcBusinessDto.setWeeklyDtoList(null);
                        appSvcBusinessDto.setPhDtoList(null);
                        appSvcBusinessDto.setEventDtoList(null);
                    }
                    if (StringUtil.isEmpty(businessIndexNo)) {
                        appSvcBusinessDto.setBusinessIndexNo(UUID.randomUUID().toString());
                    } else {
                        appSvcBusinessDto.setBusinessIndexNo(businessIndexNo);
                    }
                }
                if (appSvcBusinessDto != null) {
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

    private static AppSvcBusinessDto getAppSvcBusinessDtoByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String businessIndexNo) {
        AppSvcBusinessDto result = null;
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(businessIndexNo)) {
            List<AppSvcBusinessDto> appSvcBusinessDtos = appSvcRelatedInfoDto.getAppSvcBusinessDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcBusinessDtos)) {
                for (AppSvcBusinessDto appSvcBusinessDto : appSvcBusinessDtos) {
                    if (businessIndexNo.equals(appSvcBusinessDto.getBusinessIndexNo())) {
                        result = appSvcBusinessDto;
                        break;
                    }
                }
            }
        }
        return result;
    }


    private static boolean isExistingPsn(String assign, String licPsn) {
        return !HcsaAppConst.NEW_PSN.equals(assign) && !HcsaAppConst.DFT_FIRST_CODE.equals(assign) && AppConsts.YES.equals(licPsn);
    }

    private static boolean isNeedLoadName(String appType, String licPsn) {
        return !AppConsts.YES.equals(licPsn) && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType);
    }

    private static void clearAppPsnEditDto(AppPsnEditDto appPsnEditDto) {
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

    private static String[] removeArrIndex(String[] arrs, int index) {
        if (arrs == null || arrs.length == 0) {
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

    private static String getVal(String[] arrs, int index) {
        if (arrs == null || arrs.length == 0 || arrs.length < index) {
            return null;
        }
        return arrs[index];
    }

    public static void setAppSvcDocuments(List<DocumentShowDto> documentShowDtoList, String baseSvcId,
            Map<String, File> saveFileMap, HttpServletRequest request) {
        if (documentShowDtoList == null || documentShowDtoList.isEmpty()) {
            return;
        }
        AtomicInteger psnTypeNum = new AtomicInteger(1001);
        int i = 0;
        for (DocumentShowDto documentShowDto : documentShowDtoList) {
            String premisesVal = documentShowDto.getPremisesVal();
            List<DocSectionDto> docSectionList = documentShowDto.getDocSectionList();
            for (DocSectionDto docSectionDto : docSectionList) {
                String svcCode = docSectionDto.getSvcCode();
                List<DocSecDetailDto> docSecDetailList = docSectionDto.getDocSecDetailList();
                int secSize = docSecDetailList.size();
                for (int j = 0; j < secSize; j++) {
                    String docKey = ApplicationHelper.getSvcDocKey(j, svcCode, premisesVal);
                    DocSecDetailDto docSecDetailDto = docSecDetailList.get(j);
                    List<AppSvcDocDto> appSvcDocDtoList = genSvcPersonDoc(documentShowDto, docSectionDto, docSecDetailDto, docKey,
                            saveFileMap, request);
                    if (!appSvcDocDtoList.isEmpty()) {
                        appSvcDocDtoList.forEach(doc -> {
                            doc.setPersonTypeNum(psnTypeNum.getAndIncrement());
                            doc.setBaseSvcId(baseSvcId);
                        });
                        i += 1000;
                        while (psnTypeNum.get() >= i) {
                            i += 1000;
                        }
                        psnTypeNum.set(i + 1);
                    } else if (!appSvcDocDtoList.isEmpty()) {
                        appSvcDocDtoList.forEach(doc -> doc.setBaseSvcId(baseSvcId));
                    }
                    docSecDetailDto.setAppSvcDocDtoList(appSvcDocDtoList);
                }
            }
        }
    }

    private static List<AppSvcDocDto> genSvcPersonDoc(DocumentShowDto documentShowDto, DocSectionDto docSectionDto,
            DocSecDetailDto docSecDetailDto, String docKey, Map<String, File> saveFileMap,
            HttpServletRequest request) {
        List<AppSvcDocDto> newAppSvcDocDtoList = IaisCommonUtils.genNewArrayList();
        Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(request,
                IaisEGPConstant.SEESION_FILES_MAP_AJAX + docKey);
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
                AppSvcDocDto appSvcDocDto = getAppSvcDoc(docSecDetailDto, seqNum);
                if (v != null) {
                    if (appSvcDocDto == null) {
                        appSvcDocDto = new AppSvcDocDto();
                    }
                    String premVal = documentShowDto.getPremisesVal();
                    String svcDocId = docSecDetailDto.getConfigId();
                    String psnIndexNo = docSecDetailDto.getPsnIndexNo();
                    String svcId = docSectionDto.getSvcId();
                    appSvcDocDto.setSvcId(svcId);
                    appSvcDocDto.setSvcDocId(svcDocId);
                    appSvcDocDto.setUpFileName(docSecDetailDto.getDocTitle());
                    appSvcDocDto.setDocName(v.getName());
                    long size = v.length() / 1024;
                    appSvcDocDto.setDocSize(Integer.valueOf(String.valueOf(size)));
                    appSvcDocDto.setMd5Code(FileUtils.getFileMd5(v));
                    appSvcDocDto.setPremisesVal(premVal);
                    appSvcDocDto.setPremisesType(documentShowDto.getPremisesType());
                    appSvcDocDto.setPsnIndexNo(psnIndexNo);
                    appSvcDocDto.setSeqNum(seqNum);
                    appSvcDocDto.setPersonType(docSecDetailDto.getPsnType());
                    setAppSvcDocDtoFileds(appSvcDocDto);
                    String key = ApplicationHelper.getFileMapKey(premVal, svcId, svcDocId, psnIndexNo, seqNum);
                    saveFileMap.put(key, v);
                }
                //the data is retrieved from the DTO a second time
                fileMap.put(k, null);
                if (appSvcDocDto != null) {
                    newAppSvcDocDtoList.add(appSvcDocDto);
                }
            });
        }
        return newAppSvcDocDtoList;
    }

    private static AppSvcDocDto getAppSvcDoc(DocSecDetailDto docSecDetailDto, int seqNum) {
        if (!docSecDetailDto.isExistDoc()) {
            return null;
        }
        return docSecDetailDto.getAppSvcDocDtoList().stream()
                .filter(doc -> seqNum == doc.getSeqNum())
                .findAny()
                .orElse(null);
    }

    private static void setAppSvcDocDtoFileds(AppSvcDocDto appSvcDocDto) {
        if (appSvcDocDto.getSubmitDt() == null) {
            appSvcDocDto.setSubmitDt(new Date());
        }
        if (StringUtil.isEmpty(appSvcDocDto.getSubmitBy())) {
            appSvcDocDto.setSubmitBy(ApplicationHelper.getLoginContext().getUserId());
        }
    }

    public static void setSpecialisedData(List<AppPremSpecialisedDto> appPremSpecialisedDtoList,
            String svcCode, HttpServletRequest request) {
        if (IaisCommonUtils.isEmpty(appPremSpecialisedDtoList)) {
            return;
        }
        for (AppPremSpecialisedDto specialisedDto : appPremSpecialisedDtoList) {
            if (!Objects.equals(specialisedDto.getBaseSvcCode(), svcCode)) {
                continue;
            }
            String premisesVal = specialisedDto.getPremisesVal();
            specialisedDto.setAppPremScopeDtoList(genAppPremScopeDtoList(specialisedDto.getAppPremScopeDtoList(),
                    premisesVal, "", request));
            specialisedDto.setAppPremSubSvcRelDtoList(genAppPremSubSvcRelDtoList(specialisedDto.getAppPremSubSvcRelDtoList(),
                    premisesVal, "", request));
            specialisedDto.initAllAppPremScopeDtoList();
            specialisedDto.initAllAppPremSubSvcRelDtoList();
        }
    }

    private static List<AppPremSubSvcRelDto> genAppPremSubSvcRelDtoList(List<AppPremSubSvcRelDto> appPremScopeDtoList,
            String premisesVal, String parentId, HttpServletRequest request) {
        if (IaisCommonUtils.isEmpty(appPremScopeDtoList)) {
            return null;
        }
        String[] values = ParamUtil.getStrings(request, premisesVal + "_" + parentId + "_service");
        for (AppPremSubSvcRelDto relDto : appPremScopeDtoList) {
            relDto.setChecked(StringUtil.isIn(relDto.getSvcId(), values));
            relDto.setAppPremSubSvcRelDtos(genAppPremSubSvcRelDtoList(relDto.getAppPremSubSvcRelDtos(), premisesVal,
                    relDto.getSvcId(), request));
        }
        return appPremScopeDtoList;
    }

    private static List<AppPremScopeDto> genAppPremScopeDtoList(List<AppPremScopeDto> appPremScopeDtoList,
            String premisesVal, String parentId, HttpServletRequest request) {
        if (IaisCommonUtils.isEmpty(appPremScopeDtoList)) {
            return null;
        }
        String[] values = ParamUtil.getStrings(request, premisesVal + "_" + parentId + "_sub_type");
        for (AppPremScopeDto scopeDto : appPremScopeDtoList) {
            scopeDto.setChecked(StringUtil.isIn(scopeDto.getSubTypeId(), values));
            scopeDto.setAppPremScopeDtos(genAppPremScopeDtoList(scopeDto.getAppPremScopeDtos(), premisesVal,
                    scopeDto.getSubTypeId(), request));
        }
        return appPremScopeDtoList;
    }

    public static void setAppSvcSuplmFormList(List<AppSvcSuplmFormDto> appSvcSuplmFormList, HttpServletRequest request) {
        if (IaisCommonUtils.isEmpty(appSvcSuplmFormList)) {
            log.info("The appSvcSuplmFormList is null!!!!");
            return;
        }
        for (AppSvcSuplmFormDto appSvcSuplmFormDto : appSvcSuplmFormList) {
            setAppSvcSuplmFormDto(appSvcSuplmFormDto, appSvcSuplmFormDto.getPremisesVal(), request);
        }
    }

    public static void setAppSvcOtherFormList(List<AppSvcOtherInfoDto> appSvcOtherInfoList, HttpServletRequest request){
        if (IaisCommonUtils.isEmpty(appSvcOtherInfoList)) {
            log.info("The appSvcOtherInfoList is null!!!!");
            return;
        }
        for (AppSvcOtherInfoDto appSvcOtherInfoDto : appSvcOtherInfoList) {
            if (appSvcOtherInfoDto != null){
                setAppSvcSuplmFormDto(appSvcOtherInfoDto.getAppSvcSuplmFormDto(),appSvcOtherInfoDto.getPremisesVal(), request);
            }

        }
    }

    public static void setAppSvcSuplmFormDto(AppSvcSuplmFormDto appSvcSuplmFormDto, String prefix, HttpServletRequest request) {
        if (appSvcSuplmFormDto == null) {
            log.info("The AppSvcSuplmFormDto is null!!!!");
            return;
        }
        List<AppSvcSuplmGroupDto> appSvcSuplmGroupDtoList = appSvcSuplmFormDto.getAppSvcSuplmGroupDtoList();
        if (IaisCommonUtils.isEmpty(appSvcSuplmGroupDtoList)) {
            log.info("The AppSvcSuplmItemDto List is null!!!!");
            return;
        }
        for (AppSvcSuplmGroupDto groupDto : appSvcSuplmGroupDtoList) {
            String groupId = groupDto.getGroupId();
            List<AppSvcSuplmItemDto> appSvcSuplmItemDtoList = groupDto.getAppSvcSuplmItemDtoList();
            if (IaisCommonUtils.isEmpty(appSvcSuplmItemDtoList)) {
                continue;
            }
            int count = ParamUtil.getInt(request, prefix + groupId, 1);
            int baseSize = groupDto.getBaseSize();
            groupDto.setAppSvcSuplmItemDtoList(genAppSvcSuplmItemDtoList(appSvcSuplmItemDtoList, baseSize, count, prefix, request));
            groupDto.setCount(count);
        }
    }

    private static List<AppSvcSuplmItemDto> genAppSvcSuplmItemDtoList(List<AppSvcSuplmItemDto> appSvcSuplmItemDtoList, int baseSize,
            int count, String prefix, HttpServletRequest request) {
        List<AppSvcSuplmItemDto> result = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < baseSize; j++) {
                AppSvcSuplmItemDto dto = CopyUtil.copyMutableObject(appSvcSuplmItemDtoList.get(j));
                dto.setInputValue(getInputValue(dto, prefix, i, request));
                dto.setSeqNum(i);
                result.add(dto);
            }
        }
        return result;
    }

    private static String getInputValue(AppSvcSuplmItemDto dto, String prefix, int i, HttpServletRequest request) {
        String inputValue = null;
        String value = ParamUtil.getString(request, prefix + dto.getItemConfigId() + i);
        if (StringUtil.isNotEmpty(value)) {
            inputValue = value;
        } else {
            String radioBatchNum = dto.getItemConfigDto().getRadioBatchNum();
            if (StringUtil.isNotEmpty(radioBatchNum)) {
                String[] strings = ParamUtil.getStrings(request, prefix + radioBatchNum + i);
                if (StringUtil.isIn(dto.getItemConfigId(), strings)) {
                    inputValue = dto.getItemConfigId();
                }
            }
        }
        return inputValue;
    }

}
