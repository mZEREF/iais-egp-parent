package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.job.executor.util.SpringHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocSecDetailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocumentShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.SpecialServiceSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpSecondAddrDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppLicBundleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremGroupOutsourcedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremNonLicRelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremOutSourceLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremOutSourceProvidersQueryDto;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOutsouredDto;
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
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.client.ComSystemAdminClient;
import java.io.File;
import java.sql.Time;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import sop.iwe.SessionManager;
import sop.rbac.user.User;
import sop.util.DateUtil;

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

    public static List<AppGrpSecondAddrDto> genAppGrpSecondAddrDto(AppGrpPremisesDto appGrpPremisesDto,
            String appType, HttpServletRequest request, String premIndexNo, String premType) {
        List<AppGrpSecondAddrDto> list = IaisCommonUtils.genNewArrayList();
        if (StringUtil.isEmpty(appGrpPremisesDto)) {
            return list;
        }
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        String prefix = "address";
        String[] count = ParamUtil.getStrings(request, prefix + "Count");
        String[] isPartEdit = ParamUtil.getStrings(request, prefix + "isPartEdit");
        String[] indexNos = ParamUtil.getStrings(request, prefix + "index");
        int size = 0;
        if (count != null && count.length > 0) {
            size = count.length;
        }
        for (int i = 0; i < size; i++) {
            boolean pageData = false;
            boolean nonChanged = false;
            String indexNo = getVal(indexNos, i);
            AppGrpSecondAddrDto appGrpSecondAddrDto = new AppGrpSecondAddrDto();
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                pageData = true;
            } else if (AppConsts.YES.equals(getVal(isPartEdit, i))) {
                pageData = true;
            } else if (!StringUtil.isEmpty(indexNo)) {
                nonChanged = true;
            }
            if (nonChanged) {
                List<AppGrpSecondAddrDto> appGrpSecondAddrDtos = appGrpPremisesDto.getAppGrpSecondAddrDtos();
                appGrpSecondAddrDto = appGrpSecondAddrDtos.stream().
                        filter(dto -> Objects.equals(indexNo, dto.getIndexNo()))
                        .findAny()
                        .orElseGet(() -> {
                            AppGrpSecondAddrDto dto = new AppGrpSecondAddrDto();
                            dto.setId(indexNo);
                            return dto;
                        });
            } else if (pageData) {
                appGrpSecondAddrDto = getAppGrpSecondAddrDto(prefix, String.valueOf(i), appGrpSecondAddrDto, request, premIndexNo,
                        premType);
            }
            list.add(appGrpSecondAddrDto);
        }
        return list;
    }

    public static AppGrpSecondAddrDto getAppGrpSecondAddrDto(String prefix, String suffix, AppGrpSecondAddrDto appGrpSecondAddrDto,
            HttpServletRequest request, String premIndexNo, String premType) {
        appGrpSecondAddrDto.setPostalCode(ParamUtil.getString(request, prefix + "postalCode" + suffix));
        appGrpSecondAddrDto.setAddrType(ParamUtil.getString(request, prefix + "addrType" + suffix));
        appGrpSecondAddrDto.setBlkNo(ParamUtil.getString(request, prefix + "blkNo" + suffix));
        appGrpSecondAddrDto.setFloorNo(ParamUtil.getString(request, 0 + "FloorNos" + 0));
        appGrpSecondAddrDto.setUnitNo(ParamUtil.getString(request, 0 + "UnitNos" + 0));
        appGrpSecondAddrDto.setStreetName(ParamUtil.getString(request, prefix + "streetName" + suffix));
        appGrpSecondAddrDto.setBuildingName(ParamUtil.getString(request, prefix + "buildingName" + suffix));
        appGrpSecondAddrDto.setIndexNo(UUID.randomUUID().toString());
        String addressSize = ParamUtil.getString(request, "addressSize");
        List<AppPremisesOperationalUnitDto> unitDtos = IaisCommonUtils.genNewArrayList();
        int length = 1;
        if (StringUtil.isNotEmpty(addressSize)) {
            length = Integer.valueOf(addressSize);
        }
        for (int i = 1; i < length; i++) {
            String floorNo = ParamUtil.getString(request, suffix + "FloorNos" + i);
            String unitNo = ParamUtil.getString(request, suffix + "UnitNos" + i);
            if (StringUtil.isEmpty(floorNo) && StringUtil.isEmpty(unitNo)) {
                continue;
            }
            AppPremisesOperationalUnitDto appPremisesOperationalUnitDto = new AppPremisesOperationalUnitDto();
            appPremisesOperationalUnitDto.setFloorNo(floorNo);
            appPremisesOperationalUnitDto.setUnitNo(unitNo);
            appPremisesOperationalUnitDto.setPremType(premType);
            appPremisesOperationalUnitDto.setPremVal(premIndexNo);
            appPremisesOperationalUnitDto.setSeqNum(i);
            unitDtos.add(appPremisesOperationalUnitDto);
        }
        appGrpSecondAddrDto.setAppPremisesOperationalUnitDtos(unitDtos);
        return appGrpSecondAddrDto;

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
        List<AppGrpPremisesDto> appGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
        String[] premisesIndexNos = ParamUtil.getStrings(request, "premisesIndexNo");
        String[] chooseExistData = ParamUtil.getStrings(request, "chooseExistData");
        String[] premTypeValue = ParamUtil.getStrings(request, "premType");
        String[] premSelValue = ParamUtil.getStrings(request, "premSelValue");
        String[] isPartEdit = ParamUtil.getStrings(request, "isPartEdit");
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
            boolean isRfi = ApplicationHelper.checkIsRfi(request);
            AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
            String appType = appSubmissionDto.getAppType();
            List<AppGrpPremisesDto> oldList = appSubmissionDto.getAppGrpPremisesDtoList();
            AppGrpPremisesDto oldAppGrpPremisesDto = null;
            for (AppGrpPremisesDto grpPremisesDto : oldList) {
                if (premIndexNo.equals(grpPremisesDto.getPremisesIndexNo())) {
                    oldAppGrpPremisesDto = grpPremisesDto;
                    break;
                }
            }
            List<AppGrpSecondAddrDto> appGrpSecondAddrDtoList = Optional.ofNullable(oldAppGrpPremisesDto)
                    .map(AppGrpPremisesDto::getAppGrpSecondAddrDtos).orElse(null);
            if (ApplicationHelper.isBackend()) {
                appGrpSecondAddrDtoList = genAppGrpSecondAddrDto(oldAppGrpPremisesDto, appType, request, premIndexNo, premType);
            }
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)
                    || AppConsts.YES.equals(getVal(isPartEdit, i))) {
                if (!AppConsts.YES.equals(existingData)) {
                    setAppGrpPremiseNonAutoFields(appGrpPremisesDto, premIndexNo, premType, i, request);
                }
                if (licPremise != null) {
                    appGrpPremisesDto.setRelatedServices(licPremise.getRelatedServices());
                    appGrpPremisesDto.setHciCode(licPremise.getHciCode());
                }
            } else if (StringUtil.isNotEmpty(premIndexNo)) {
                appGrpPremisesDto = oldAppGrpPremisesDto;
                appGrpPremisesDtoList.add(appGrpPremisesDto);
                continue;
            }
            setAppGrpPremiseFromPage(appGrpPremisesDto, i, request);
            // rfc and renewal
            setSelectedLicences(appGrpPremisesDto, premIndexNo, request);
            AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(request);
            checkHciCode(appGrpPremisesDto, premIndexNo, premisesSel, oldAppSubmissionDto);
            appGrpPremisesDto.setSeqNum(i + 1);
            appGrpPremisesDto.setHasError(null);
            appGrpPremisesDto.setPremisesIndexNo(premIndexNo);
            appGrpPremisesDto.setExistingData(existingData);
            appGrpPremisesDto.setPremisesType(premType);
            appGrpPremisesDto.setPremisesSelect(premisesSel);
            appGrpPremisesDto.setAppGrpSecondAddrDtos(appGrpSecondAddrDtoList);
            appGrpPremisesDtoList.add(appGrpPremisesDto);
        }
        return appGrpPremisesDtoList;
    }


    private static void setSelectedLicences(AppGrpPremisesDto appGrpPremisesDto, String premIndexNo,
            HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
        if (appSubmissionDto != null && appSubmissionDto.getAppGrpPremisesDtoList() != null) {
            List<LicenceDto> licenceDtos = appSubmissionDto.getAppGrpPremisesDtoList().stream()
                    .filter(dto -> Objects.equals(premIndexNo, dto.getPremisesIndexNo()))
                    .map(AppGrpPremisesDto::getLicenceDtos)
                    .filter(Objects::nonNull)
                    .findAny()
                    .orElse(null);
            appGrpPremisesDto.setLicenceDtos(licenceDtos);
        }
        String[] selectedLicences = ParamUtil.getStrings(request, "selectedLicence");
        appGrpPremisesDto.setSelectedLicences(selectedLicences);
    }

    private static void checkHciCode(AppGrpPremisesDto appGrpPremisesDto, String premisesIndexNo, String premisesSel,
            AppSubmissionDto oldAppSubmissionDto) {
        if (oldAppSubmissionDto == null || HcsaAppConst.DFT_FIRST_CODE.equals(premisesSel)
                || HcsaAppConst.NEW_PREMISES.equals(premisesSel) || StringUtil.isEmpty(premisesSel)) {
            appGrpPremisesDto.setHciCode(null);
            return;
        }
        for (AppGrpPremisesDto grpPremisesDto : oldAppSubmissionDto.getAppGrpPremisesDtoList()) {
            if (Objects.equals(premisesSel, ApplicationHelper.getPremisesKey(grpPremisesDto))) {
                appGrpPremisesDto.setHciCode(grpPremisesDto.getHciCode());
                break;
            }
        }
        for (AppGrpPremisesDto grpPremisesDto : oldAppSubmissionDto.getAppGrpPremisesDtoList()) {
            if (Objects.equals(premisesIndexNo, grpPremisesDto.getPremisesIndexNo())) {
                appGrpPremisesDto.setOldHciCode(grpPremisesDto.getHciCode());
                break;
            }
        }
    }

    private static void setAppGrpPremiseNonAutoFields(AppGrpPremisesDto appGrpPremisesDto, String premIndexNo,
            String premType, int i, HttpServletRequest request) {
        String[] retrieveflag = ParamUtil.getStrings(request, "retrieveflag");
        appGrpPremisesDto.setClickRetrieve(AppConsts.YES.equals(getVal(retrieveflag, i)));
        appGrpPremisesDto.setVehicleNo(ParamUtil.getString(request, "vehicleNo" + i));
        appGrpPremisesDto.setHciName(ParamUtil.getString(request, "hciName" + i));
        appGrpPremisesDto.setPostalCode(ParamUtil.getString(request, "postalCode" + i));
        appGrpPremisesDto.setAddrType(ParamUtil.getString(request, "addrType" + i));
        appGrpPremisesDto.setBlkNo(ParamUtil.getString(request, "blkNo" + i));
        String floorNo = ParamUtil.getString(request, i + "FloorNo" + 0);
        String unitNo = ParamUtil.getString(request, i + "UnitNo" + 0);
        appGrpPremisesDto.setFloorNo(IaisCommonUtils.getFloorNo(floorNo));
        appGrpPremisesDto.setUnitNo(unitNo);
        String[] opLengths = ParamUtil.getStrings(request, "opLength");
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
        appGrpPremisesDto.setStreetName(ParamUtil.getString(request, "streetName" + i));
        appGrpPremisesDto.setBuildingName(ParamUtil.getString(request, "buildingName" + i));
    }

    private static void setAppGrpPremiseFromPage(AppGrpPremisesDto appGrpPremisesDto, int i, HttpServletRequest request) {
        //ControllerHelper.get(request, appGrpPremisesDto, String.valueOf(i));
        appGrpPremisesDto.setScdfRefNo(ParamUtil.getString(request, "scdfRefNo" + i));
        String certIssuedDtStr = ParamUtil.getString(request, "certIssuedDt" + i);
        appGrpPremisesDto.setCertIssuedDtStr(certIssuedDtStr);
        if (CommonValidator.isDate(certIssuedDtStr)) {
            try {
                appGrpPremisesDto.setCertIssuedDt(Formatter.parseDate(certIssuedDtStr));
            } catch (Exception e) {
                log.warn(StringUtil.changeForLog(e.getMessage()), e);
            }
        }
        appGrpPremisesDto.setEasMtsUseOnly(ParamUtil.getString(request, "easMtsUseOnly" + i));
        appGrpPremisesDto.setEasMtsPubEmail(ParamUtil.getString(request, "easMtsPubEmail" + i));
        appGrpPremisesDto.setEasMtsPubHotline(ParamUtil.getString(request, "easMtsPubHotline" + i));

        appGrpPremisesDto.setLocateWtihHcsa(ParamUtil.getString(request, "locateWtihHcsa" + i));
        String locateWtihNonHcsa = ParamUtil.getString(request, "locateWtihNonHcsa" + i);
        appGrpPremisesDto.setLocateWtihNonHcsa(locateWtihNonHcsa);
        if (AppConsts.YES.equals(locateWtihNonHcsa)) {
            String[] nonHcsaLengths = ParamUtil.getStrings(request, "nonHcsaLength");
            List<AppPremNonLicRelationDto> appPremNonLicRelationDtos = IaisCommonUtils.genNewArrayList();
            int nonHcsaLength = 0;
            try {
                nonHcsaLength = Integer.parseInt(nonHcsaLengths[i]);
            } catch (Exception e) {
                log.warn(StringUtil.changeForLog("Non-hcsa service length can not parse to int"));
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
                    LoginContext loginContext = AccessUtil.getLoginUser(request);
                    if (RoleConsts.USER_ROLE_AO1.equals(loginContext.getCurRoleId())
                            || RoleConsts.USER_ROLE_AO2.equals(loginContext.getCurRoleId())
                            || RoleConsts.USER_ROLE_AO3.equals(loginContext.getCurRoleId())
                            || RoleConsts.USER_ROLE_INSPECTION_LEAD.equals(loginContext.getCurRoleId())) {
                        appSvcVehicleDto.setStatus(ApplicationConsts.VEHICLE_STATUS_APPROVE);
                    } else {
                        appSvcVehicleDto.setStatus(ApplicationConsts.VEHICLE_STATUS_SUBMIT);
                    }
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

    private static final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(AppPremOutSourceProvidersQueryDto.class)
            .searchAttr(ApplicationConsts.OUT_SOURCE_PARAM)
            .resultAttr(ApplicationConsts.OUT_SOURCE_RESULT)
            .sortFieldToMap("SVC_NAME", SearchParam.ASCENDING).build();

    public static AppSvcOutsouredDto genAppPremOutSourceProvidersDto(String curAct, AppSvcOutsouredDto appSvcOutsouredDto,
            HttpServletRequest request, AppSubmissionDto appSubmissionDto, String appType) {
        boolean getPageData = false;
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        String isPartEdit = ParamUtil.getString(request, "isPartEdit");
        if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
            getPageData = true;
        } else if (AppConsts.YES.equals(isPartEdit)) {
            getPageData = true;
        }
        if (getPageData) {
            List<AppPremGroupOutsourcedDto> clinicalLaboratoryList = appSvcOutsouredDto.getClinicalLaboratoryList();
            List<AppPremGroupOutsourcedDto> radiologicalServiceList = appSvcOutsouredDto.getRadiologicalServiceList();
            if (IaisCommonUtils.isEmpty(clinicalLaboratoryList)) {
                clinicalLaboratoryList = IaisCommonUtils.genNewArrayList();
            }
            if (IaisCommonUtils.isEmpty(radiologicalServiceList)) {
                radiologicalServiceList = IaisCommonUtils.genNewArrayList();
            }
            if ("search".equals(curAct)) {
                appSvcOutsouredDto = getSerchAppPremOutSourceLicenceDto(request, appSvcOutsouredDto, appSubmissionDto);
            }
            if ("add".equals(curAct)) {
                appSvcOutsouredDto = getAddAppPremOutSourceLicenceDto(request, appSvcOutsouredDto, clinicalLaboratoryList,
                        radiologicalServiceList);
            }
            if ("sort".equals(curAct)) {
                appSvcOutsouredDto = sortOutSourceProviders(request, appSvcOutsouredDto);
            }
            if ("changePage".equals(curAct)) {
                appSvcOutsouredDto = doOutSourceProvidersPaging(request, appSvcOutsouredDto);
            }
            if ("delete".equals(curAct)) {
                appSvcOutsouredDto = getDelAppOutSourcedDto(request, appSvcOutsouredDto, clinicalLaboratoryList,
                        radiologicalServiceList);
            }
        }
        return appSvcOutsouredDto;
    }

    private static AppSvcOutsouredDto getSerchAppPremOutSourceLicenceDto(HttpServletRequest request,
            AppSvcOutsouredDto appSvcOutsouredDto, AppSubmissionDto appSubmissionDto) {
        String svcName = ParamUtil.getString(request, "serviceCode");
        String licNo = ParamUtil.getString(request, "licNo");
        String businessName = ParamUtil.getString(request, "businessName");
        String postalCode = ParamUtil.getString(request, "postalCode");

        SearchParam searchParam = appSvcOutsouredDto.getSearchParam();
        if (StringUtil.isNotEmpty(svcName)) {
            if (searchParam == null) {
                searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
            }
            appSvcOutsouredDto.setBundleSvcCode(svcName);
            clearOldSearchParam(searchParam);
            searchParam.addFilter("svcName", svcName, true);
            if (StringUtil.isNotEmpty(licNo)) {
                searchParam.addFilter("licenceNo", licNo, true);
            }
            if (StringUtil.isNotEmpty(businessName)) {
                searchParam.addFilter("businessName", businessName, true);
            }
            if (IaisCommonUtils.isNotEmpty(appSvcOutsouredDto.getClinicalLaboratoryList())) {
                searchParam.addFilter("id", getOutSourceIds(appSvcOutsouredDto.getClinicalLaboratoryList()), true);
                searchParam.addFilter("sLicenceNo", getOutSourcedLicenceNos(appSvcOutsouredDto.getClinicalLaboratoryList()), true);
            }
            if (IaisCommonUtils.isNotEmpty(appSvcOutsouredDto.getRadiologicalServiceList())) {
                searchParam.addFilter("ids", getOutSourceIds(appSvcOutsouredDto.getRadiologicalServiceList()), true);
                searchParam.addFilter("sLicenceNo", getOutSourcedLicenceNos(appSvcOutsouredDto.getClinicalLaboratoryList()), true);
            }
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            if (IaisCommonUtils.isNotEmpty(appGrpPremisesDtos)) {
                searchParam.addFilter("dPostCode", getPostalCode(appGrpPremisesDtos), true);
            }
            if (StringUtil.isNotEmpty(postalCode)) {
                searchParam.addFilter("postalCode", postalCode, true);
            }
            if (StringUtil.isNotEmpty(appSubmissionDto.getLicenseeId())) {
                searchParam.addFilter("licenseeId", appSubmissionDto.getLicenseeId(), true);
            }
            List<AppLicBundleDto> appLicBundleDtoList = appSubmissionDto.getAppLicBundleDtoList();
            if (IaisCommonUtils.isNotEmpty(appLicBundleDtoList)) {
                for (AppLicBundleDto appLicBundleDto : appLicBundleDtoList) {
                    String svcCode = appLicBundleDto.getSvcCode();
                    searchParam.addFilter("bundleSvcName", HcsaServiceCacheHelper.getServiceByCode(svcCode).getSvcName(), true);
                }
            }
            appSvcOutsouredDto.setSearchParam(searchParam);
        } else {
            if (searchParam != null) {
                searchParam.removeParam("svcName");
                searchParam.removeFilter("svcName");
            }
        }
        return appSvcOutsouredDto;
    }

    private static void clearOldSearchParam(SearchParam searchParam) {
        searchParam.removeParam("svcName");
        searchParam.removeFilter("svcName");
        searchParam.removeParam("businessName");
        searchParam.removeFilter("businessName");
        searchParam.removeParam("licenceNo");
        searchParam.removeFilter("licenceNo");
        searchParam.removeParam("postalCode");
        searchParam.removeFilter("postalCode");
    }

    private static AppSvcOutsouredDto getAddAppPremOutSourceLicenceDto(HttpServletRequest request,
            AppSvcOutsouredDto appSvcOutsouredDto,
            List<AppPremGroupOutsourcedDto> clinicalLaboratoryList,
            List<AppPremGroupOutsourcedDto> radiologicalServiceList) {
        SearchParam searchParam = appSvcOutsouredDto.getSearchParam();
        String prefix = ParamUtil.getString(request, "prefixVal");
        if (StringUtil.isNotEmpty(prefix)) {
            searchParam.addFilter("addId", prefix, true);
        }
        SearchResult<AppPremOutSourceProvidersQueryDto> searchResult = getLicCommService().queryOutsouceLicences(searchParam);
        String startDate = ParamUtil.getString(request, prefix + "agreementStartDate");
        String endDate = ParamUtil.getString(request, prefix + "agreementEndDate");
        String scpoing = ParamUtil.getString(request, prefix + "outstandingScope");
        if (searchResult != null && IaisCommonUtils.isNotEmpty(searchResult.getRows())) {
            ArrayList<AppPremOutSourceProvidersQueryDto> rows = searchResult.getRows();
            for (AppPremOutSourceProvidersQueryDto row : rows) {
                if (row != null) {
                    if (AppServicesConsts.SERVICE_NAME_CLINICAL_LABORATORY.equals(row.getSvcName())) {
                        resolveAppPremGroupOutsourcedList(appSvcOutsouredDto, clinicalLaboratoryList, row, startDate, endDate,
                                scpoing);
                        appSvcOutsouredDto.setClinicalLaboratoryList(clinicalLaboratoryList);
                    }
                    if (AppServicesConsts.SERVICE_NAME_RADIOLOGICAL_SERVICES.equals(row.getSvcName())) {
                        resolveAppPremGroupOutsourcedList(appSvcOutsouredDto, radiologicalServiceList, row, startDate, endDate,
                                scpoing);
                        appSvcOutsouredDto.setClinicalLaboratoryList(radiologicalServiceList);
                    }
                }
            }
        }
        searchParam.removeParam("addId");
        searchParam.removeFilter("addId");
        if (IaisCommonUtils.isNotEmpty(appSvcOutsouredDto.getClinicalLaboratoryList())) {
            searchParam.addFilter("id", getOutSourceIds(appSvcOutsouredDto.getClinicalLaboratoryList()), true);
            searchParam.addFilter("sLicenceNo", getOutSourcedLicenceNos(appSvcOutsouredDto.getClinicalLaboratoryList()), true);
        }
        if (IaisCommonUtils.isNotEmpty(appSvcOutsouredDto.getRadiologicalServiceList())) {
            searchParam.addFilter("ids", getOutSourceIds(appSvcOutsouredDto.getRadiologicalServiceList()), true);
            searchParam.addFilter("sLicenceNo", getOutSourcedLicenceNos(appSvcOutsouredDto.getClinicalLaboratoryList()), true);
        }
        return appSvcOutsouredDto;
    }

    private static void resolveAppPremGroupOutsourcedList(AppSvcOutsouredDto appSvcOutsouredDto,
            List<AppPremGroupOutsourcedDto> appPremGroupOutsourcedDtoList,
            AppPremOutSourceProvidersQueryDto row, String startDate, String endDate, String scoping) {
        AppPremGroupOutsourcedDto appPremGroupOutsourcedDto = new AppPremGroupOutsourcedDto();
        AppPremOutSourceLicenceDto appPremOutSourceLicenceDto = new AppPremOutSourceLicenceDto();
        appPremOutSourceLicenceDto.setId(row.getId());
        appPremOutSourceLicenceDto.setServiceCode(HcsaServiceCacheHelper.getServiceByServiceName(row.getSvcName()).getSvcCode());
        appPremOutSourceLicenceDto.setLicenceNo(row.getLicenceNo());
        appPremGroupOutsourcedDto.setBusinessName(row.getBusinessName());
        appPremGroupOutsourcedDto.setAddress(row.getAddress());
        appPremGroupOutsourcedDto.setExpiryDate(row.getExpiryDate());
        try {
            appPremOutSourceLicenceDto.setAgreementStartDate(Formatter.parseDate(startDate));
            appPremOutSourceLicenceDto.setAgreementEndDate(Formatter.parseDate(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        appPremOutSourceLicenceDto.setOutstandingScope(scoping);
        appPremGroupOutsourcedDto.setAppPremOutSourceLicenceDto(appPremOutSourceLicenceDto);
        appPremGroupOutsourcedDto.setStartDateStr(startDate);
        appPremGroupOutsourcedDto.setEndDateStr(endDate);
        if (StringUtil.isNotEmpty(scoping) && StringUtil.isNotEmpty(startDate) && StringUtil.isNotEmpty(endDate)) {
            appPremGroupOutsourcedDtoList.add(appPremGroupOutsourcedDto);
        }
        appSvcOutsouredDto.setSearchOutsourced(appPremGroupOutsourcedDto);
        appSvcOutsouredDto.setPrefixVal(row.getId());
    }

    private static AppSvcOutsouredDto sortOutSourceProviders(HttpServletRequest request,
            AppSvcOutsouredDto appPremOutSourceProvidersDto) {
        String classSort = ParamUtil.getString(request, "classSort");
        String sortFieldName = ParamUtil.getString(request, "crud_action_value");
        String sortType = ParamUtil.getString(request, "crud_action_additional");
        if ("cLDSort".equals(classSort)) {
            sortList(appPremOutSourceProvidersDto.getClinicalLaboratoryList(), sortFieldName, sortType);
        } else if ("rdsSort".equals(classSort)) {
            sortList(appPremOutSourceProvidersDto.getRadiologicalServiceList(), sortFieldName, sortType);
        } else {
            SearchParam searchParam = appPremOutSourceProvidersDto.getSearchParam();
            CrudHelper.doSorting(searchParam, request);
            appPremOutSourceProvidersDto.setSearchParam(searchParam);
        }
        return appPremOutSourceProvidersDto;
    }

    private static void sortList(List<AppPremGroupOutsourcedDto> appPremGroupOutsourcedList, String sortFieldName, String sortType) {
        if (IaisCommonUtils.isNotEmpty(appPremGroupOutsourcedList) && StringUtil.isNotEmpty(sortFieldName)) {
            if ("LICENCE_NO".equals(sortFieldName)) {
                Collections.sort(appPremGroupOutsourcedList, (o1, o2) -> {
                    if ("DESC".equals(sortType)) {
                        return -o1.getAppPremOutSourceLicenceDto().getLicenceNo().compareTo(
                                o2.getAppPremOutSourceLicenceDto().getLicenceNo());
                    }
                    return o1.getAppPremOutSourceLicenceDto().getLicenceNo().compareTo(
                            o2.getAppPremOutSourceLicenceDto().getLicenceNo());
                });
            }
            if ("BUSINESS_NAME".equals(sortFieldName)) {
                Collections.sort(appPremGroupOutsourcedList, (o1, o2) -> {
                    if ("DESC".equals(sortType)) {
                        return -o1.getBusinessName().compareTo(o2.getBusinessName());
                    }
                    return o1.getBusinessName().compareTo(o2.getBusinessName());
                });
            }
            if ("ADDRESS".equals(sortFieldName)) {
                Collections.sort(appPremGroupOutsourcedList, (o1, o2) -> {
                    if ("DESC".equals(sortType)) {
                        return -o1.getAddress().compareTo(o2.getAddress());
                    }
                    return o1.getAddress().compareTo(o2.getAddress());
                });
            }
            if ("EXPIRY_DATE".equals(sortFieldName)) {
                Collections.sort(appPremGroupOutsourcedList, (o1, o2) -> {
                    if ("DESC".equals(sortType)) {
                        return -o1.getExpiryDate().compareTo(o2.getExpiryDate());
                    }
                    return o1.getExpiryDate().compareTo(o2.getExpiryDate());
                });
            }
            if ("AGREEMENT_START_DATE".equals(sortFieldName)) {
                Collections.sort(appPremGroupOutsourcedList, (o1, o2) -> {
                    if ("DESC".equals(sortType)) {
                        return -o1.getAppPremOutSourceLicenceDto().getAgreementStartDate().compareTo(
                                o2.getAppPremOutSourceLicenceDto().getAgreementStartDate());
                    }
                    return o1.getAppPremOutSourceLicenceDto().getAgreementStartDate().compareTo(
                            o2.getAppPremOutSourceLicenceDto().getAgreementStartDate());
                });
            }
            if ("AGREEMENT_END_DATE".equals(sortFieldName)) {
                Collections.sort(appPremGroupOutsourcedList, (o1, o2) -> {
                    if ("DESC".equals(sortType)) {
                        return -o1.getAppPremOutSourceLicenceDto().getAgreementEndDate().compareTo(
                                o2.getAppPremOutSourceLicenceDto().getAgreementEndDate());
                    }
                    return o1.getAppPremOutSourceLicenceDto().getAgreementEndDate().compareTo(
                            o2.getAppPremOutSourceLicenceDto().getAgreementEndDate());
                });
            }
            if ("OUTSTANDING_SCOPE".equals(sortFieldName)) {
                Collections.sort(appPremGroupOutsourcedList, (o1, o2) -> {
                    if ("DESC".equals(sortType)) {
                        return -o1.getAppPremOutSourceLicenceDto().getOutstandingScope().compareTo(
                                o2.getAppPremOutSourceLicenceDto().getOutstandingScope());
                    }
                    return o1.getAppPremOutSourceLicenceDto().getOutstandingScope().compareTo(
                            o2.getAppPremOutSourceLicenceDto().getOutstandingScope());
                });
            }
        }
    }

    private static AppSvcOutsouredDto doOutSourceProvidersPaging(HttpServletRequest request,
            AppSvcOutsouredDto appPremOutSourceProvidersDto) {
        SearchParam searchParam = appPremOutSourceProvidersDto.getSearchParam();
        CrudHelper.doPaging(searchParam, request);
        appPremOutSourceProvidersDto.setSearchParam(searchParam);
        return appPremOutSourceProvidersDto;
    }

    private static AppSvcOutsouredDto getDelAppOutSourcedDto(HttpServletRequest request, AppSvcOutsouredDto appSvcOutsouredDto,
            List<AppPremGroupOutsourcedDto> clinicalLaboratoryList,
            List<AppPremGroupOutsourcedDto> radiologicalServiceList) {
        String prefix = ParamUtil.getString(request, "prefixVal");
        SearchParam searchParam = appSvcOutsouredDto.getSearchParam();
        if (IaisCommonUtils.isNotEmpty(clinicalLaboratoryList)) {
            removeAppPremOutsourced(clinicalLaboratoryList, prefix, appSvcOutsouredDto);
            if (appSvcOutsouredDto.getClinicalLaboratoryList().size() > 1) {
                if (IaisCommonUtils.isNotEmpty(appSvcOutsouredDto.getClinicalLaboratoryList())) {
                    searchParam.addFilter("id", getOutSourceIds(appSvcOutsouredDto.getClinicalLaboratoryList()), true);
                }
            } else {
                searchParam.removeParam("id");
                searchParam.removeFilter("id");
            }
        }
        if (IaisCommonUtils.isNotEmpty(radiologicalServiceList)) {
            removeAppPremOutsourced(radiologicalServiceList, prefix, appSvcOutsouredDto);
            if (appSvcOutsouredDto.getRadiologicalServiceList().size() > 1) {
                if (IaisCommonUtils.isNotEmpty(appSvcOutsouredDto.getRadiologicalServiceList())) {
                    searchParam.addFilter("ids", getOutSourceIds(appSvcOutsouredDto.getRadiologicalServiceList()), true);
                }
            } else {
                searchParam.removeParam("ids");
                searchParam.removeFilter("ids");
            }
        }
        appSvcOutsouredDto.setPrefixVal(null);
        return appSvcOutsouredDto;
    }

    private static void removeAppPremOutsourced(List<AppPremGroupOutsourcedDto> appPremGroupOutsourcedDtoList, String prefixVal,
            AppSvcOutsouredDto appSvcOutsouredDto) {
        Iterator<AppPremGroupOutsourcedDto> outsourcedDtoIterator = appPremGroupOutsourcedDtoList.iterator();
        while (outsourcedDtoIterator.hasNext()) {
            AppPremGroupOutsourcedDto appPremGroupOutsourcedDto = outsourcedDtoIterator.next();
            if (appPremGroupOutsourcedDto != null && appPremGroupOutsourcedDto.getAppPremOutSourceLicenceDto() != null) {
                String id = appPremGroupOutsourcedDto.getAppPremOutSourceLicenceDto().getId();
                if (StringUtil.isNotEmpty(prefixVal) && prefixVal.equals(id)) {
                    appPremGroupOutsourcedDto.setEndDateStr(null);
                    appPremGroupOutsourcedDto.setStartDateStr(null);
                    appPremGroupOutsourcedDto.getAppPremOutSourceLicenceDto().setOutstandingScope("");
                    appPremGroupOutsourcedDto.getAppPremOutSourceLicenceDto().setAgreementEndDate(null);
                    appPremGroupOutsourcedDto.getAppPremOutSourceLicenceDto().setAgreementStartDate(null);
                    outsourcedDtoIterator.remove();
                }
            }
        }
    }

    private static List<String> getPostalCode(List<AppGrpPremisesDto> appGrpPremisesDtos) {
        List<String> postcode = IaisCommonUtils.genNewArrayList();
        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
            postcode.add(appGrpPremisesDto.getPostalCode());
        }
        return postcode;
    }

    private static List<String> getOutSourceIds(List<AppPremGroupOutsourcedDto> appPremGroupOutsourcedDtoList) {
        List<String> ids = IaisCommonUtils.genNewArrayList();
        for (AppPremGroupOutsourcedDto appPremGroupOutsourcedDto : appPremGroupOutsourcedDtoList) {
            ids.add(appPremGroupOutsourcedDto.getAppPremOutSourceLicenceDto().getId());
        }
        return ids;
    }

    private static List<String> getOutSourcedLicenceNos(List<AppPremGroupOutsourcedDto> appPremGroupOutsourcedDtoList) {
        List<String> licenceNo = IaisCommonUtils.genNewArrayList();
        for (AppPremGroupOutsourcedDto appPremGroupOutsourcedDto : appPremGroupOutsourcedDtoList) {
            licenceNo.add(appPremGroupOutsourcedDto.getAppPremOutSourceLicenceDto().getLicenceNo());
        }
        return licenceNo;
    }

    public static List<AppSvcPrincipalOfficersDto> genAppSvcClinicalDirectorDto(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("gen app svc clinical director dto start ..."));
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = genKeyPersonnels(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, "",
                request, false);
        log.debug(StringUtil.changeForLog("gen app svc clinical director dto end ..."));
        return appSvcCgoDtoList;
    }

    private static boolean canSetValue(boolean canEdit, boolean isNewOfficer, boolean isPartEdit) {
        return isPartEdit || canEdit || isNewOfficer;
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
                    break;
                }
            }
        }
        return result;
    }

    private static AppSvcOtherInfoTopPersonDto getOtherInfoByName(List<AppSvcOtherInfoTopPersonDto> appSvcOtherInfoTopPersonDtos,
            String name) {
        AppSvcOtherInfoTopPersonDto result = null;
        if (!IaisCommonUtils.isEmpty(appSvcOtherInfoTopPersonDtos) && !StringUtil.isEmpty(name)) {
            for (AppSvcOtherInfoTopPersonDto appSvcOtherInfoTopPersonDto : appSvcOtherInfoTopPersonDtos) {
                if (name.equals(appSvcOtherInfoTopPersonDto.getName())) {
                    result = appSvcOtherInfoTopPersonDto;
                }
            }
        }
        return result;
    }

    private static AppSvcOtherInfoAbortDto getOtherInfoByTopType(List<AppSvcOtherInfoAbortDto> appSvcOtherInfoAboutDtos,
            String topType) {
        AppSvcOtherInfoAbortDto result = null;

        if (!IaisCommonUtils.isEmpty(appSvcOtherInfoAboutDtos) && !StringUtil.isEmpty(topType)) {
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

    public static List<AppSvcOtherInfoDto> genAppSvcOtherInfoList(HttpServletRequest request, String appType,
            List<AppSvcOtherInfoDto> appSvcOtherInfoDtos) {
        if (IaisCommonUtils.isEmpty(appSvcOtherInfoDtos)) {
            return IaisCommonUtils.genNewArrayList();
        }
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        String otherInfoServiceCode = ParamUtil.getString(request, "otherInfoServiceCode");
        for (AppSvcOtherInfoDto appSvcOtherInfoDto : appSvcOtherInfoDtos) {
            String prefix = appSvcOtherInfoDto.getPremisesVal();
            //otherInfoMed==>>dentalService medicalService
            if (StringUtil.isIn(otherInfoServiceCode, new String[]{AppServicesConsts.SERVICE_CODE_DENTAL_SERVICE,
                    AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE})) {
                boolean getPageData = false;
                boolean getDataByIndexNo = false;
                String isPartEdit = ParamUtil.getString(request, "isPartEdit");
                String otherInfoMedId = ParamUtil.getString(request, "otherInfoMedId");
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(otherInfoMedId)) {
                    getDataByIndexNo = true;
                }
                if (getPageData) {
                    appSvcOtherInfoDto.setAppSvcOtherInfoMedDto(
                            getAppSvcOtherInfoMedDto(appSvcOtherInfoDto.getAppSvcOtherInfoMedDto(), prefix, request, getDataByIndexNo,
                                    getPageData, otherInfoMedId));
                    String dsDeclaration = ParamUtil.getString(request, prefix + "dsDeclaration");
                    appSvcOtherInfoDto.setDsDeclaration(dsDeclaration);
                }
            }
            //otherInfoMed==>>asc
            if (AppServicesConsts.SERVICE_CODE_AMBULATORY_SURGICAL_CENTRE.equals(otherInfoServiceCode)) {
                boolean getPageData = false;
                boolean getDataByIndexNo = false;
                String isPartEdit = ParamUtil.getString(request, "isPartEdit");
                String otherInfoMedASCId = ParamUtil.getString(request, "otherInfoMedASCId");
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(otherInfoMedASCId)) {
                    getDataByIndexNo = true;
                }
                if (getPageData) {
                    String ascsDeclaration = ParamUtil.getString(request, prefix + "ascsDeclaration");
                    appSvcOtherInfoDto.setAscsDeclaration(ascsDeclaration);
                    appSvcOtherInfoDto.setOtherInfoMedAmbulatorySurgicalCentre(
                            getAppSvcOtherInfoASCMedDto(appSvcOtherInfoDto.getOtherInfoMedAmbulatorySurgicalCentre(), prefix, request,
                                    getDataByIndexNo, getPageData, otherInfoMedASCId));
                }
            }
            //otherInfoNurse==>>RDC
            if (AppServicesConsts.SERVICE_CODE_RENAL_DIALYSIS_CENTRE.equals(otherInfoServiceCode)) {
                boolean getPageData = false;
                boolean getDataByIndexNo = false;
                String isPartEdit = ParamUtil.getString(request, "isPartEdit");
                String otherInfoNurseId = ParamUtil.getString(request, "otherInfoNurseId");
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(otherInfoNurseId)) {
                    getDataByIndexNo = true;
                }
                if (getPageData) {
                    appSvcOtherInfoDto.setAppSvcOtherInfoNurseDto(
                            getAppSvcOtherInfoNurseDto(appSvcOtherInfoDto.getAppSvcOtherInfoNurseDto(), prefix, request,
                                    getDataByIndexNo, getPageData, otherInfoNurseId));
                }
            }
            //otherInfoTop
            if (StringUtil.isIn(otherInfoServiceCode, new String[]{AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL,
                    AppServicesConsts.SERVICE_CODE_AMBULATORY_SURGICAL_CENTRE,
                    AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE})) {
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request, "isPartEditTop");
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                }
                if (getPageData) {
                    String provideTop = ParamUtil.getString(request, prefix + "provideTop");
                    if (AppConsts.YES.equals(provideTop)) {
                        String topType = ParamUtil.getString(request, prefix + "topType");
                        String declaration = ParamUtil.getString(request, prefix + "declaration");
                        appSvcOtherInfoDto.setAppSvcOtherInfoTopDto(
                                getAppSvcOtherInfoTopDto(appSvcOtherInfoDto.getAppSvcOtherInfoTopDto(), prefix, request, appType,
                                        isRfi, getPageData, isPartEdit));
                        appSvcOtherInfoDto.setOtherInfoTopPersonPractitionersList(getAppSvcOtherInfoTopPersonDtoPractitioners(request,
                                appType, isRfi, appSvcOtherInfoDto.getOtherInfoTopPersonPractitionersList(), prefix));
                        appSvcOtherInfoDto.setOtherInfoTopPersonAnaesthetistsList(getAppSvcOtherInfoTopPersonDtoAnaesthetists(request,
                                appType, isRfi, appSvcOtherInfoDto.getOtherInfoTopPersonAnaesthetistsList(), prefix));
                        appSvcOtherInfoDto.setOtherInfoTopPersonNursesList(
                                getAppSvcOtherInfoTopPersonDtoNurses(request, appType, isRfi,
                                        appSvcOtherInfoDto.getOtherInfoTopPersonNursesList(), prefix));
                        appSvcOtherInfoDto.setOtherInfoTopPersonCounsellorsList(getAppSvcOtherInfoTopPersonDtoCounsellors(request,
                                appType, isRfi, appSvcOtherInfoDto.getOtherInfoTopPersonCounsellorsList(), prefix));
                        if (ApplicationConsts.OTHER_INFO_SD.equals(topType) || ApplicationConsts.OTHER_INFO_DSP.equals(topType)) {
                            appSvcOtherInfoDto.setOtherInfoAbortDrugList(getAppSvcOtherInfoAbortDto1(request, appType, isRfi,
                                    appSvcOtherInfoDto.getOtherInfoAbortDrugList(), prefix));
                        } else {
                            appSvcOtherInfoDto.setOtherInfoAbortDrugList(null);
                        }
                        if (ApplicationConsts.OTHER_INFO_SSP.equals(topType) || ApplicationConsts.OTHER_INFO_DSP.equals(topType)) {
                            appSvcOtherInfoDto.setOtherInfoAbortSurgicalProcedureList(getAppSvcOtherInfoAbortDto2(request, appType
                                    , isRfi, appSvcOtherInfoDto.getOtherInfoAbortSurgicalProcedureList(), prefix));
                        } else {
                            appSvcOtherInfoDto.setOtherInfoAbortSurgicalProcedureList(null);
                        }
                        if (ApplicationConsts.OTHER_INFO_DSP.equals(topType)) {
                            appSvcOtherInfoDto.setOtherInfoAbortDrugAndSurgicalList(getAppSvcOtherInfoAbortDto3(request, appType,
                                    isRfi, appSvcOtherInfoDto.getOtherInfoAbortDrugAndSurgicalList(), prefix));
                        } else {
                            appSvcOtherInfoDto.setOtherInfoAbortDrugAndSurgicalList(null);
                        }
                        appSvcOtherInfoDto.setDeclaration(declaration);
                        appSvcOtherInfoDto.setAppSvcSuplmFormDto(appSvcOtherInfoDto.getAppSvcSuplmFormDto());
                        setAppSvcOtherFormList(appSvcOtherInfoDtos, request);
                    }
                    appSvcOtherInfoDto.setProvideTop(provideTop);
                }
            }
            if (StringUtil.isIn(otherInfoServiceCode, new String[]{AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL,
                    AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL,
                    AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE})) {
                boolean getPageData = false;
                String isPartEditTop = ParamUtil.getString(request, "isPartEditTop");
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEditTop)) {
                    getPageData = true;
                }
                if (getPageData) {
                    String provideYfVs = ParamUtil.getString(request, prefix + "provideYfVs");
                    String yfCommencementDateStr = ParamUtil.getString(request, prefix + "yfCommencementDate");
                    appSvcOtherInfoDto.setYfCommencementDateStr(yfCommencementDateStr);
                    if (StringUtil.isEmpty(yfCommencementDateStr)) {
                        appSvcOtherInfoDto.setYfCommencementDate(null);
                    } else {
                        Date date = DateUtil.parseDate(yfCommencementDateStr, Formatter.DATE);
                        appSvcOtherInfoDto.setYfCommencementDate(date);
                    }
                    appSvcOtherInfoDto.setProvideYfVs(provideYfVs);
                    appSvcOtherInfoDto.setOrgUserDto(getOtherInfoYfVs(request));
                }
            }
            boolean getPageData = false;
            String isPartEditOtherService = ParamUtil.getString(request, "isPartEditOtherService");
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                getPageData = true;
            } else if (AppConsts.YES.equals(isPartEditOtherService)) {
                getPageData = true;
            }
            if (getPageData) {
                appSvcOtherInfoDto.setAppPremSubSvcRelDtoList(
                        genAppPremSubSvcRelDtoList(appSvcOtherInfoDto.getAppPremSubSvcRelDtoList(),
                                prefix, "", request));
                appSvcOtherInfoDto.initAllAppPremSubSvcRelDtoList();
            }
        }
        return appSvcOtherInfoDtos;
    }

    //DentalService and MedicalService==>>otherInfoMedDto
    private static AppSvcOtherInfoMedDto getOtherInfoMedById(AppSvcOtherInfoMedDto appSvcOtherInfoMedDto, String id) {
        AppSvcOtherInfoMedDto result = null;
        if (appSvcOtherInfoMedDto != null && StringUtil.isNotEmpty(id)) {
            if (id.equals(appSvcOtherInfoMedDto.getId())) {
                result = appSvcOtherInfoMedDto;
            }
        }
        return result;
    }

    private static AppSvcOtherInfoMedDto getAppSvcOtherInfoMedDto(AppSvcOtherInfoMedDto appSvcOtherInfoMedDto, String prefix,
            HttpServletRequest request, boolean getDataByIndexNo, boolean getPageData, String otherInfoMedId) {
        if (appSvcOtherInfoMedDto == null) {
            appSvcOtherInfoMedDto = new AppSvcOtherInfoMedDto();
        }
        if (getDataByIndexNo) {
            AppSvcOtherInfoMedDto svcOtherInfoMedDto = getOtherInfoMedById(appSvcOtherInfoMedDto, otherInfoMedId);
            if (svcOtherInfoMedDto != null) {
                appSvcOtherInfoMedDto = svcOtherInfoMedDto;
            }
        } else if (getPageData) {
            String isMedicalTypeIt = ParamUtil.getString(request, prefix + "isMedicalTypeIt");
            String isMedicalTypePaper = ParamUtil.getString(request, prefix + "isMedicalTypePaper");
            String systemOption = ParamUtil.getString(request, prefix + "systemOption");
            String isOpenToPublic = ParamUtil.getString(request, prefix + "isOpenToPublic");
            String gfaValue = ParamUtil.getString(request, prefix + "gfaValue");
            appSvcOtherInfoMedDto.setGfaValue(gfaValue);
            appSvcOtherInfoMedDto.setIsOpenToPublic(isOpenToPublic);
            appSvcOtherInfoMedDto.setSystemOption(systemOption);
            appSvcOtherInfoMedDto.setIsMedicalTypeIt(isMedicalTypeIt);
            appSvcOtherInfoMedDto.setIsMedicalTypePaper(isMedicalTypePaper);
            if ("MED06".equals(systemOption)) {
                String otherSystemOption = ParamUtil.getString(request, prefix + "otherSystemOption");
                appSvcOtherInfoMedDto.setOtherSystemOption(otherSystemOption);
            }
        }
        return appSvcOtherInfoMedDto;
    }

    //asc==>>MedDto
    private static AppSvcOtherInfoMedDto getAppSvcOtherInfoASCMedDto(AppSvcOtherInfoMedDto appSvcOtherInfoMedDto, String prefix,
            HttpServletRequest request, boolean getDataByIndexNo, boolean getPageData, String otherInfoMedASCId) {
        if (appSvcOtherInfoMedDto == null) {
            appSvcOtherInfoMedDto = new AppSvcOtherInfoMedDto();
        }
        if (getDataByIndexNo) {
            AppSvcOtherInfoMedDto svcOtherInfoMedDto = getOtherInfoMedById(appSvcOtherInfoMedDto, otherInfoMedASCId);
            if (svcOtherInfoMedDto != null) {
                appSvcOtherInfoMedDto = svcOtherInfoMedDto;
            }
        } else if (getPageData) {
            String gfValue = ParamUtil.getString(request, prefix + "agfaValue");
            appSvcOtherInfoMedDto.setGfaValue(gfValue);
        }
        return appSvcOtherInfoMedDto;
    }

    //YfVs
    public static OrgUserDto getOtherInfoYfVs(HttpServletRequest request) {
        User user = SessionManager.getInstance(request).getCurrentUser();
        ComSystemAdminClient client = SpringContextHelper.getContext().getBean(ComSystemAdminClient.class);
        OrgUserDto orgUserDto = client.retrieveOrgUserAccount(user.getId()).getEntity();
        return orgUserDto;
    }

    //other nurse
    private static AppSvcOtherInfoNurseDto getOtherInfoNurseById(AppSvcOtherInfoNurseDto appSvcOtherInfoNurseDto, String id) {
        AppSvcOtherInfoNurseDto result = null;
        if (appSvcOtherInfoNurseDto != null && StringUtil.isNotEmpty(id)) {
            if (id.equals(appSvcOtherInfoNurseDto.getId())) {
                result = appSvcOtherInfoNurseDto;
            }
        }
        return result;
    }

    private static AppSvcOtherInfoNurseDto getAppSvcOtherInfoNurseDto(AppSvcOtherInfoNurseDto appSvcOtherInfoNurseDto, String prefix,
            HttpServletRequest request, boolean getDataByIndexNo, boolean getPageData, String otherInfoNurseId) {
        if (appSvcOtherInfoNurseDto == null) {
            appSvcOtherInfoNurseDto = new AppSvcOtherInfoNurseDto();
        }
        if (getDataByIndexNo) {
            AppSvcOtherInfoNurseDto svcOtherInfoNurseDto = getOtherInfoNurseById(appSvcOtherInfoNurseDto, otherInfoNurseId);
            if (svcOtherInfoNurseDto != null) {
                appSvcOtherInfoNurseDto = svcOtherInfoNurseDto;
            }
        } else if (getPageData) {
            String perShiftNum = ParamUtil.getString(request, prefix + "perShiftNum");
            String dialysisStationsNum = ParamUtil.getString(request, prefix + "dialysisStationsNum");
            String helpBStationNum = ParamUtil.getString(request, prefix + "helpBStationNum");
            String nisOpenToPublic = ParamUtil.getString(request, prefix + "nisOpenToPublic");
            appSvcOtherInfoNurseDto.setHelpBStationNum(helpBStationNum);
            appSvcOtherInfoNurseDto.setIsOpenToPublic(nisOpenToPublic);
            appSvcOtherInfoNurseDto.setPerShiftNum(perShiftNum);
            appSvcOtherInfoNurseDto.setDialysisStationsNum(dialysisStationsNum);
        }
        return appSvcOtherInfoNurseDto;
    }

    //other top
    private static AppSvcOtherInfoTopDto getAppSvcOtherInfoTopDtoById(AppSvcOtherInfoTopDto appSvcOtherInfoTopDto, String id) {
        AppSvcOtherInfoTopDto result = null;
        if (appSvcOtherInfoTopDto != null && StringUtil.isNotEmpty(id)) {
            if (id.equals(appSvcOtherInfoTopDto.getId())) {
                result = appSvcOtherInfoTopDto;
            }
        }
        return result;
    }

    private static AppSvcOtherInfoTopDto getAppSvcOtherInfoTopDto(AppSvcOtherInfoTopDto appSvcOtherInfoTopDto, String prefix,
            HttpServletRequest request, String appType, boolean isRfi,
            boolean getPageData, String isPartEdit) {
        if (appSvcOtherInfoTopDto == null) {
            appSvcOtherInfoTopDto = new AppSvcOtherInfoTopDto();
        }
        boolean getDataByIndexNo = false;
        String otherInfoNurseId = ParamUtil.getString(request, "otherInfoNurseId");
        if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
            getPageData = true;
        } else if (AppConsts.YES.equals(isPartEdit)) {
            getPageData = true;
        } else if (!StringUtil.isEmpty(otherInfoNurseId)) {
            getDataByIndexNo = true;
        }
        if (getDataByIndexNo) {
            AppSvcOtherInfoTopDto svcOtherInfoTopDto = getAppSvcOtherInfoTopDtoById(appSvcOtherInfoTopDto, otherInfoNurseId);
            if (svcOtherInfoTopDto != null) {
                appSvcOtherInfoTopDto = svcOtherInfoTopDto;
            }
        } else if (getPageData) {
            String topType = ParamUtil.getString(request, prefix + "topType");
            String hasConsuAttendCourse = ParamUtil.getString(request, prefix + "hasConsuAttendCourse");
            String isProvideHpb = ParamUtil.getString(request, prefix + "isProvideHpb");
            String isOutcomeProcRecord = ParamUtil.getString(request, prefix + "isOutcomeProcRecord");
            String compCaseNum = ParamUtil.getString(request, prefix + "compCaseNum");
            appSvcOtherInfoTopDto.setTopType(topType);
            appSvcOtherInfoTopDto.setHasConsuAttendCourse(hasConsuAttendCourse);
            appSvcOtherInfoTopDto.setIsProvideHpb(isProvideHpb);
            appSvcOtherInfoTopDto.setIsOutcomeProcRecord(isOutcomeProcRecord);
            appSvcOtherInfoTopDto.setCompCaseNum(compCaseNum);
        }

        return appSvcOtherInfoTopDto;
    }

    //other top person practitioners
    public static List<AppSvcOtherInfoTopPersonDto> getAppSvcOtherInfoTopPersonDtoPractitioners(HttpServletRequest request,
            String appType, boolean isRfi,
            List<AppSvcOtherInfoTopPersonDto> otherInfoTopPersonPractitionersList,
            String prefix) {
        List<AppSvcOtherInfoTopPersonDto> result = IaisCommonUtils.genNewArrayList();
        String c = ParamUtil.getString(request, prefix + "cdLength");
        if (StringUtil.isNotEmpty(c)) {
            int cdLength = ParamUtil.getInt(request, prefix + "cdLength");
            for (int i = 0; i < cdLength; i++) {
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request, prefix + "isPartEdit" + i);
                String idNo = ParamUtil.getString(request, prefix + "idNo" + i);
                String psnType = ParamUtil.getString(request, prefix + "psnType" + i);
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(idNo)) {
                    getDataByIndexNo = true;
                }
                if (getDataByIndexNo) {
                    AppSvcOtherInfoTopPersonDto appPremOtherInfoTopPersonDto = getOtherInfoByIdNo(otherInfoTopPersonPractitionersList,
                            idNo);
                    if (appPremOtherInfoTopPersonDto != null) {
                        result.add(appPremOtherInfoTopPersonDto);
                    }
                } else if (getPageData) {
                    AppSvcOtherInfoTopPersonDto appSvcOtherInfoTopPersonDto = new AppSvcOtherInfoTopPersonDto();
                    String profRegNo = ParamUtil.getString(request, prefix + "profRegNo" + i);
                    String regType = ParamUtil.getString(request, prefix + "regType" + i);
                    String isMedAuthByMoh = ParamUtil.getString(request, prefix + "medAuthByMoh" + i);
                    String name = ParamUtil.getString(request, prefix + "name" + i);
                    String speciality = ParamUtil.getString(request, prefix + "speciality" + i);
                    String qualification = ParamUtil.getString(request, prefix + "qualification" + i);
                    appSvcOtherInfoTopPersonDto.setQualification(qualification);
                    appSvcOtherInfoTopPersonDto.setSpeciality(speciality);
                    appSvcOtherInfoTopPersonDto.setName(name);
                    appSvcOtherInfoTopPersonDto.setProfRegNo(profRegNo);
                    appSvcOtherInfoTopPersonDto.setPsnType(psnType);
                    appSvcOtherInfoTopPersonDto.setRegType(regType);
                    appSvcOtherInfoTopPersonDto.setMedAuthByMoh(isMedAuthByMoh);
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
            String appType, boolean isRfi, List<AppSvcOtherInfoTopPersonDto> otherInfoTopPersonAnaesthetistsList, String prefix) {
        List<AppSvcOtherInfoTopPersonDto> result = IaisCommonUtils.genNewArrayList();
        String a = ParamUtil.getString(request, prefix + "anaLength");
        if (StringUtil.isNotEmpty(a)) {
            int anaLength = ParamUtil.getInt(request, prefix + "anaLength");
            for (int i = 0; i < anaLength; i++) {
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request, prefix + "aisPartEdit" + i);
                String idNo = ParamUtil.getString(request, prefix + "idANo" + i);
                String apsnType = ParamUtil.getString(request, prefix + "apsnType" + i);
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(idNo)) {
                    getDataByIndexNo = true;
                }
                if (getDataByIndexNo) {
                    AppSvcOtherInfoTopPersonDto appPremOtherInfoTopPersonDto = getOtherInfoByIdNo(otherInfoTopPersonAnaesthetistsList,
                            idNo);
                    if (appPremOtherInfoTopPersonDto != null) {
                        result.add(appPremOtherInfoTopPersonDto);
                    }
                } else if (getPageData) {
                    String profRegNo = ParamUtil.getString(request, prefix + "aprofRegNo" + i);
                    String name = ParamUtil.getString(request, prefix + "aname" + i);
                    String regType = ParamUtil.getString(request, prefix + "aregType" + i);
                    String qualification = ParamUtil.getString(request, prefix + "aqualification" + i);
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
            boolean isRfi, List<AppSvcOtherInfoTopPersonDto> otherInfoTopPersonNursesList, String prefix) {
        List<AppSvcOtherInfoTopPersonDto> result = IaisCommonUtils.genNewArrayList();
        String n = ParamUtil.getString(request, prefix + "nLength");
        if (StringUtil.isNotEmpty(n)) {
            int nLength = ParamUtil.getInt(request, prefix + "nLength");
            for (int i = 0; i < nLength; i++) {
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request, prefix + "nisPartEdit" + i);
                String name = ParamUtil.getString(request, prefix + "nname" + i);
                String npsnType = ParamUtil.getString(request, prefix + "npsnType" + i);
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(name)) {
                    getDataByIndexNo = true;
                }
                if (getDataByIndexNo) {
                    AppSvcOtherInfoTopPersonDto appPremOtherInfoTopPersonDto = getOtherInfoByName(otherInfoTopPersonNursesList, name);
                    if (appPremOtherInfoTopPersonDto != null) {
                        result.add(appPremOtherInfoTopPersonDto);
                    }
                } else if (getPageData) {
                    String nqualification = ParamUtil.getString(request, prefix + "nqualification" + i);
                    AppSvcOtherInfoTopPersonDto appSvcOtherInfoTopPersonDto = new AppSvcOtherInfoTopPersonDto();
                    appSvcOtherInfoTopPersonDto.setPsnType(npsnType);
                    appSvcOtherInfoTopPersonDto.setName(name);
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
            String appType, boolean isRfi, List<AppSvcOtherInfoTopPersonDto> otherInfoTopPersonCounsellorsList, String prefix) {
        List<AppSvcOtherInfoTopPersonDto> result = IaisCommonUtils.genNewArrayList();
        String co = ParamUtil.getString(request, prefix + "cLength");
        if (StringUtil.isNotEmpty(co)) {
            int cLength = ParamUtil.getInt(request, prefix + "cLength");
            for (int i = 0; i < cLength; i++) {
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request, prefix + "cisPartEdit" + i);
                String cidNo = ParamUtil.getString(request, prefix + "cidNo" + i);
                String cpsnType = ParamUtil.getString(request, prefix + "cpsnType" + i);
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(cidNo)) {
                    getDataByIndexNo = true;
                }
                if (getDataByIndexNo) {
                    AppSvcOtherInfoTopPersonDto appPremOtherInfoTopPersonDto = getOtherInfoByIdNo(otherInfoTopPersonCounsellorsList,
                            cidNo);
                    if (appPremOtherInfoTopPersonDto != null) {
                        result.add(appPremOtherInfoTopPersonDto);
                    }
                } else if (getPageData) {
                    String cqualification = ParamUtil.getString(request, prefix + "cqualification" + i);
                    String cname = ParamUtil.getString(request, prefix + "cname" + i);
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
    public static List<AppSvcOtherInfoAbortDto> getAppSvcOtherInfoAbortDto1(HttpServletRequest request, String appType,
            boolean isRfi, List<AppSvcOtherInfoAbortDto> appSvcOtherInfoAboutDtos, String prefix) {
        List<AppSvcOtherInfoAbortDto> result = IaisCommonUtils.genNewArrayList();
        String at = ParamUtil.getString(request, prefix + "atdLength");
        if (StringUtil.isNotEmpty(at)) {
            int atdLength = ParamUtil.getInt(request, prefix + "atdLength");
            for (int i = 0; i < atdLength; i++) {
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request, prefix + "isPartEditDrug" + i);
                String topType = ParamUtil.getString(request, prefix + "topTypeDrug" + i);
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(topType)) {
                    getDataByIndexNo = true;
                }
                if (getDataByIndexNo) {
                    AppSvcOtherInfoAbortDto appSvcOtherInfoAboutDto = getOtherInfoByTopType(appSvcOtherInfoAboutDtos, topType);
                    if (appSvcOtherInfoAboutDto != null) {
                        result.add(appSvcOtherInfoAboutDto);
                    }
                } else if (getPageData) {
                    String year = ParamUtil.getString(request, prefix + "year" + i);
                    String abortNum = ParamUtil.getString(request, prefix + "abortNum" + i);

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

    public static List<AppSvcOtherInfoAbortDto> getAppSvcOtherInfoAbortDto2(HttpServletRequest request, String appType, boolean isRfi,
            List<AppSvcOtherInfoAbortDto> otherInfoAbortSurgicalProcedureList, String prefix) {
        List<AppSvcOtherInfoAbortDto> result = IaisCommonUtils.genNewArrayList();
        String p = ParamUtil.getString(request, prefix + "pLength");
        if (StringUtil.isNotEmpty(p)) {
            int pLength = ParamUtil.getInt(request, prefix + "pLength");
            for (int i = 0; i < pLength; i++) {
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request, prefix + "isPartEditSurgical" + i);
                String topType = ParamUtil.getString(request, prefix + "topTypeSurgical" + i);
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(topType)) {
                    getDataByIndexNo = true;
                }
                if (getDataByIndexNo) {
                    AppSvcOtherInfoAbortDto appSvcOtherInfoAboutDto = getOtherInfoByTopType(otherInfoAbortSurgicalProcedureList,
                            topType);
                    if (appSvcOtherInfoAboutDto != null) {
                        result.add(appSvcOtherInfoAboutDto);
                    }
                } else if (getPageData) {
                    String year = ParamUtil.getString(request, prefix + "pyear" + i);
                    String abortNum = ParamUtil.getString(request, prefix + "pabortNum" + i);

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

    public static List<AppSvcOtherInfoAbortDto> getAppSvcOtherInfoAbortDto3(HttpServletRequest request, String appType, boolean isRfi,
            List<AppSvcOtherInfoAbortDto> otherInfoAbortDrugAndSurgicalList, String prefix) {
        List<AppSvcOtherInfoAbortDto> result = IaisCommonUtils.genNewArrayList();
        String a = ParamUtil.getString(request, prefix + "aLength");
        if (StringUtil.isNotEmpty(a)) {
            int aLength = ParamUtil.getInt(request, prefix + "aLength");
            for (int i = 0; i < aLength; i++) {
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request, prefix + "isPartEditAll" + i);
                String topType = ParamUtil.getString(request, prefix + "topTypeAll" + i);
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(topType)) {
                    getDataByIndexNo = true;
                }
                if (getDataByIndexNo) {
                    AppSvcOtherInfoAbortDto appSvcOtherInfoAboutDto = getOtherInfoByTopType(otherInfoAbortDrugAndSurgicalList,
                            topType);
                    if (appSvcOtherInfoAboutDto != null) {
                        result.add(appSvcOtherInfoAboutDto);
                    }
                } else if (getPageData) {
                    String year = ParamUtil.getString(request, prefix + "ayear" + i);
                    String abortNum = ParamUtil.getString(request, prefix + "aabortNum" + i);

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
        return genKeyPersonnels(ApplicationConsts.PERSONNEL_PSN_TYPE_PO, "po", request, false);
    }

    public static List<AppSvcPrincipalOfficersDto> genAppSvcNomineeDtos(HttpServletRequest request) {
        return genKeyPersonnels(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, "dpo", request, false);
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
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = genKeyPersonnels(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, "", request,
                false);
        log.info(StringUtil.changeForLog("genAppSvcCgoDto end ...."));
        return appSvcCgoDtoList;
    }

    public static List<AppSvcPrincipalOfficersDto> genKeyPersonnels(String psnType, String prefix, HttpServletRequest request,
            boolean isSpecial) {
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
                if (isSpecial) {
                    person = ApplicationHelper.getKeyPersonnel(psnType, true, currSvcInfoDto).stream()
                            .filter(dto -> Objects.equals(indexNo, dto.getIndexNo()))
                            .findAny()
                            .orElseGet(() -> {
                                AppSvcPrincipalOfficersDto dto = new AppSvcPrincipalOfficersDto();
                                dto.setIndexNo(indexNo);
                                return dto;
                            });
                } else {
                    person = ApplicationHelper.getKeyPersonnel(psnType, currSvcInfoDto).stream()
                            .filter(dto -> Objects.equals(indexNo, dto.getIndexNo()))
                            .findAny()
                            .orElseGet(() -> {
                                AppSvcPrincipalOfficersDto dto = new AppSvcPrincipalOfficersDto();
                                dto.setIndexNo(indexNo);
                                return dto;
                            });
                }

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
            String[] keys = psnType.split(AppConsts.DFT_DELIMITER);
            person.setPsnType(keys[keys.length - 1]);
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
        String registered = ParamUtil.getString(request, prefix + "noRegWithProfBoard" + suffix);
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
        setPsnValue(person, appPsnEditDto, "officeTelNo", prefix, suffix, request);

        if (person.getPsnEditDto() == null) {
            if (appPsnEditDto == null) {
                appPsnEditDto = ApplicationHelper.setNeedEditField(person);
            }
            person.setPsnEditDto(appPsnEditDto);
        }
        String bclsExpiryDateStr = ParamUtil.getString(request, prefix + "bclsExpiryDate" + suffix);
        person.setBclsExpiryDateStr(bclsExpiryDateStr);
        String aclsExpiryDateStr = ParamUtil.getString(request, prefix + "aclsExpiryDate" + suffix);
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
            List<AppSvcSpecialServiceInfoDto> appSvcSpecialServiceInfoDtoList, String appType) {
        if (!IaisCommonUtils.isEmpty(appSvcSpecialServiceInfoDtoList)) {
            String prefix = "";
            int i = 0;
            for (AppSvcSpecialServiceInfoDto appSvcSpecialServiceInfoDto : appSvcSpecialServiceInfoDtoList) {
                int j = 0;
                for (SpecialServiceSectionDto specialServiceSectionDto : appSvcSpecialServiceInfoDto.getSpecialServiceSectionDtoList()) {
                    Map<String, Integer> maxCount = specialServiceSectionDto.getMaxCount();
                    int cgomaxCount = maxCount.get(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
                    int nicMaxCount = maxCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE);
                    int slMaxCount = maxCount.get(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
                    int rsoMaxCount = maxCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER);
                    int drMaxCount = maxCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_DR);
                    int mpMaxCount = maxCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST);
                    int rpMaxCount = maxCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL);
                    int nmMaxCount = maxCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NM);
                    int diMaxCount = maxCount.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR);
                    int nuMaxCount = maxCount.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR);
                    if (cgomaxCount != 0) {
                        List<AppSvcPrincipalOfficersDto> dtos = genKeyPersonnels(
                                appSvcSpecialServiceInfoDto.getNewPsnKey(specialServiceSectionDto.getSvcCode(),
                                        ApplicationConsts.PERSONNEL_PSN_TYPE_CGO), prefix + i + j + "cgo", request, true);
                        specialServiceSectionDto.setAppSvcCgoDtoList(dtos);
                    }
                    if (slMaxCount != 0) {
                        List<AppSvcPersonnelDto> personnelDtoList = getSpecialServiceInforamtionPerson(request, prefix + i + j,
                                ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER,
                                "sl", specialServiceSectionDto.getAppSvcSectionLeaderList(), appType);
                        if (IaisCommonUtils.isNotEmpty(personnelDtoList)) {
                            specialServiceSectionDto.setAppSvcSectionLeaderList(personnelDtoList);
                        }
                    }
                    if (nicMaxCount != 0) {
                        List<AppSvcPersonnelDto> personnelDtoList = getSpecialServiceInforamtionPerson(request, prefix + i + j,
                                ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE,
                                "nic", specialServiceSectionDto.getAppSvcNurseDtoList(), appType);
                        if (IaisCommonUtils.isNotEmpty(personnelDtoList)) {
                            specialServiceSectionDto.setAppSvcNurseDtoList(personnelDtoList);
                        }
                    }
                    if (rsoMaxCount != 0) {
                        List<AppSvcPersonnelDto> personnelDtoList = getSpecialServiceInforamtionPerson(request, prefix + i + j,
                                ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER,
                                "rso", specialServiceSectionDto.getAppSvcRadiationSafetyOfficerDtoList(), appType);
                        if (IaisCommonUtils.isNotEmpty(personnelDtoList)) {
                            specialServiceSectionDto.setAppSvcRadiationSafetyOfficerDtoList(personnelDtoList);
                        }
                    }
                    if (drMaxCount != 0) {
                        List<AppSvcPersonnelDto> personnelDtoList = getSpecialServiceInforamtionPerson(request, prefix + i + j,
                                ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_DR,
                                "dr", specialServiceSectionDto.getAppSvcDiagnosticRadiographerDtoList(), appType);
                        if (IaisCommonUtils.isNotEmpty(personnelDtoList)) {
                            specialServiceSectionDto.setAppSvcDiagnosticRadiographerDtoList(personnelDtoList);
                        }
                    }
                    if (mpMaxCount != 0) {
                        List<AppSvcPersonnelDto> personnelDtoList = getSpecialServiceInforamtionPerson(request, prefix + i + j,
                                ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST,
                                "mp", specialServiceSectionDto.getAppSvcMedicalPhysicistDtoList(), appType);
                        if (IaisCommonUtils.isNotEmpty(personnelDtoList)) {
                            specialServiceSectionDto.setAppSvcMedicalPhysicistDtoList(personnelDtoList);
                        }
                    }
                    if (rpMaxCount != 0) {
                        List<AppSvcPersonnelDto> personnelDtoList = getSpecialServiceInforamtionPerson(request, prefix + i + j,
                                ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL,
                                "rp", specialServiceSectionDto.getAppSvcRadiationPhysicistDtoList(), appType);
                        if (IaisCommonUtils.isNotEmpty(personnelDtoList)) {
                            specialServiceSectionDto.setAppSvcRadiationPhysicistDtoList(personnelDtoList);
                        }
                    }
                    if (nmMaxCount != 0) {
                        List<AppSvcPersonnelDto> personnelDtoList = getSpecialServiceInforamtionPerson(request, prefix + i + j,
                                ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NM,
                                "nm", specialServiceSectionDto.getAppSvcNMTechnologistDtoList(), appType);
                        if (IaisCommonUtils.isNotEmpty(personnelDtoList)) {
                            specialServiceSectionDto.setAppSvcNMTechnologistDtoList(personnelDtoList);
                        }
                    }
                    if (diMaxCount != 0) {
                        List<AppSvcPersonnelDto> personnelDtoList = getSpecialServiceInforamtionPerson(request, prefix + i + j,
                                ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR,
                                "dir", specialServiceSectionDto.getAppSvcDirectorDtoList(), appType);
                        if (IaisCommonUtils.isNotEmpty(personnelDtoList)) {
                            specialServiceSectionDto.setAppSvcDirectorDtoList(personnelDtoList);
                        }
                    }
                    if (nuMaxCount != 0) {
                        List<AppSvcPersonnelDto> personnelDtoList = getSpecialServiceInforamtionPerson(request, prefix + i + j,
                                ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR,
                                "nur", specialServiceSectionDto.getAppSvcNurseDirectorDtoList(), appType);
                        if (IaisCommonUtils.isNotEmpty(personnelDtoList)) {
                            specialServiceSectionDto.setAppSvcNurseDirectorDtoList(personnelDtoList);
                        }
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

    private static List<AppSvcPersonnelDto> getSpecialServiceInforamtionPerson(HttpServletRequest request, String prefix,
            String personType, String personTypeAbbr, List<AppSvcPersonnelDto> originalPersonnelList, String appType) {
        List<AppSvcPersonnelDto> personnelDtoList = IaisCommonUtils.genNewArrayList();
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        int Length = ParamUtil.getInt(request, prefix + personType + "Length");
        for (int x = 0; x < Length; x++) {
            AppSvcPersonnelDto appSvcPersonnelDto = null;
            boolean getDataByOld = false;
            boolean getPageData = false;
            String isPartEdit = ParamUtil.getString(request, prefix + personTypeAbbr + "isPartEdit" + x);
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                getPageData = true;
            } else if (AppConsts.YES.equals(isPartEdit)) {
                getPageData = true;
            } else {
                getDataByOld = true;
            }
            if (getDataByOld && IaisCommonUtils.isNotEmpty(originalPersonnelList) && x < originalPersonnelList.size()) {
                appSvcPersonnelDto = originalPersonnelList.get(x);
            } else if (getPageData) {
                appSvcPersonnelDto = getAppSvcPersonnelParam(null, request, prefix + personTypeAbbr, "" + x, personType);
            }
            personnelDtoList.add(appSvcPersonnelDto);
        }
        return personnelDtoList;
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

    public static AppSvcPersonnelDto getAppSvcPersonnelParam(String indexNo, HttpServletRequest request, String prefix, String suffix,
            String personnelType) {

        AppSvcPersonnelDto svcPersonnelDto = ControllerHelper.get(request, AppSvcPersonnelDto.class, prefix, suffix);

        if (StringUtil.isEmpty(svcPersonnelDto.getIndexNo()) && StringUtil.isEmpty(indexNo)) {
            svcPersonnelDto.setIndexNo(UUID.randomUUID().toString());
        } else {
            svcPersonnelDto.setIndexNo(indexNo);
        }
        if (StringUtil.isNotEmpty(personnelType)) {
            svcPersonnelDto.setPersonnelType(personnelType);
//            special
        } else if (ApplicationConsts.SERVICE_PERSONNEL_TYPE_SPECIALS.equals(prefix) || "".equals(personnelType)) {
        } else {
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


    //    AR
    public static List<AppSvcPersonnelDto> getArPersonnel(HttpServletRequest request, String prefix, Boolean isRfi, String appType,
            AppSvcRelatedInfoDto appSvcRelatedInfoDto) {
        List<AppSvcPersonnelDto> arPractitionerList = IaisCommonUtils.genNewArrayList();
        AppSvcPersonnelDto appSvcPersonnelDto = new AppSvcPersonnelDto();
        String[] arCountStrs = ParamUtil.getStrings(request, prefix + "arCount");
        String[] isPartEdit = ParamUtil.getStrings(request, prefix + "isPartEdit");
        String[] indexNos = ParamUtil.getStrings(request, prefix + "indexNo");
        int size = 0;
        if (arCountStrs != null && arCountStrs.length > 0) {
            size = arCountStrs.length;
        }
        for (int i = 0; i < size; i++) {
            boolean pageData = false;
            boolean nonChanged = false;
            String indexNo = getVal(indexNos, i);
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                pageData = true;
            } else if (AppConsts.YES.equals(getVal(isPartEdit, i))) {
                pageData = true;
            } else if (!StringUtil.isEmpty(indexNo)) {
                nonChanged = true;
            }
            if (nonChanged) {
                List<AppSvcPersonnelDto> list = appSvcRelatedInfoDto.getSvcPersonnelDto().getArPractitionerList();
                appSvcPersonnelDto = list.stream().
                        filter(dto -> Objects.equals(indexNo, dto.getIndexNo()))
                        .findAny()
                        .orElseGet(() -> {
                            AppSvcPersonnelDto dto = new AppSvcPersonnelDto();
                            dto.setIndexNo(indexNo);
                            return dto;
                        });
            } else if (pageData) {
                appSvcPersonnelDto = getAppSvcPersonnelParam(indexNo, request, prefix, String.valueOf(i), null);
            }
            arPractitionerList.add(appSvcPersonnelDto);
        }
        return arPractitionerList;

    }

    public static List<AppSvcPersonnelDto> getNuPersonnel(HttpServletRequest request, String prefix, Boolean isRfi, String appType,
            AppSvcRelatedInfoDto appSvcRelatedInfoDto) {
        List<AppSvcPersonnelDto> nurseList = IaisCommonUtils.genNewArrayList();
        AppSvcPersonnelDto appSvcPersonnelDto = new AppSvcPersonnelDto();
        String[] nuCountStrs = ParamUtil.getStrings(request, prefix + "nuCount");
        String[] isPartEdit = ParamUtil.getStrings(request, prefix + "isPartEdit");
        String[] indexNos = ParamUtil.getStrings(request, prefix + "indexNo");
        int size = 0;
        if (nuCountStrs != null && nuCountStrs.length > 0) {
            size = nuCountStrs.length;
        }
        for (int i = 0; i < size; i++) {
            boolean pageData = false;
            boolean nonChanged = false;
            String indexNo = getVal(indexNos, i);
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                pageData = true;
            } else if (AppConsts.YES.equals(getVal(isPartEdit, i))) {
                pageData = true;
            } else if (!StringUtil.isEmpty(indexNo)) {
                nonChanged = true;
            }
            if (nonChanged) {
                List<AppSvcPersonnelDto> list = appSvcRelatedInfoDto.getSvcPersonnelDto().getNurseList();
                appSvcPersonnelDto = list.stream().
                        filter(dto -> Objects.equals(indexNo, dto.getIndexNo()))
                        .findAny()
                        .orElseGet(() -> {
                            AppSvcPersonnelDto dto = new AppSvcPersonnelDto();
                            dto.setIndexNo(indexNo);
                            return dto;
                        });
            } else if (pageData) {
                appSvcPersonnelDto = getAppSvcPersonnelParam(indexNo, request, prefix, String.valueOf(i), null);
            }
            nurseList.add(appSvcPersonnelDto);
        }
        return nurseList;
    }


    public static List<AppSvcPersonnelDto> getEmPersonnel(HttpServletRequest request, String prefix, Boolean isRfi, String appType,
            AppSvcRelatedInfoDto appSvcRelatedInfoDto) {
        List<AppSvcPersonnelDto> embryologistList = IaisCommonUtils.genNewArrayList();
        AppSvcPersonnelDto appSvcPersonnelDto = new AppSvcPersonnelDto();
        String[] emCountStrs = ParamUtil.getStrings(request, prefix + "emCount");
        String[] isPartEdit = ParamUtil.getStrings(request, prefix + "isPartEdit");
        String[] indexNos = ParamUtil.getStrings(request, prefix + "indexNo");
        int size = 0;
        if (emCountStrs != null && emCountStrs.length > 0) {
            size = emCountStrs.length;
        }
        for (int i = 0; i < size; i++) {
            String indexNo = getVal(indexNos, i);
            boolean pageData = false;
            boolean nonChanged = false;
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                pageData = true;
            } else if (AppConsts.YES.equals(getVal(isPartEdit, i))) {
                pageData = true;
            } else if (!StringUtil.isEmpty(indexNo)) {
                nonChanged = true;
            }
            if (nonChanged) {
                List<AppSvcPersonnelDto> list = appSvcRelatedInfoDto.getSvcPersonnelDto().getEmbryologistList();
                appSvcPersonnelDto = list.stream().
                        filter(dto -> Objects.equals(indexNo, dto.getIndexNo()))
                        .findAny()
                        .orElseGet(() -> {
                            AppSvcPersonnelDto dto = new AppSvcPersonnelDto();
                            dto.setIndexNo(indexNo);
                            return dto;
                        });
            } else if (pageData) {
                appSvcPersonnelDto = getAppSvcPersonnelParam(indexNo, request, prefix, String.valueOf(i), null);
            }
            embryologistList.add(appSvcPersonnelDto);
        }
        return embryologistList;

    }

    public static List<AppSvcPersonnelDto> getNorPersonnel(HttpServletRequest request, String prefix, Boolean isRfi, String appType,
            AppSvcRelatedInfoDto appSvcRelatedInfoDto) {
        List<AppSvcPersonnelDto> normalList = IaisCommonUtils.genNewArrayList();
        AppSvcPersonnelDto appSvcPersonnelDto = new AppSvcPersonnelDto();
        String[] norCountStrs = ParamUtil.getStrings(request, prefix + "noCount");
        String[] isPartEdit = ParamUtil.getStrings(request, prefix + "isPartEdit");
        String[] indexNos = ParamUtil.getStrings(request, prefix + "indexNo");
        int size = 0;
        if (norCountStrs != null && norCountStrs.length > 0) {
            size = norCountStrs.length;
        }
        for (int i = 0; i < size; i++) {
            String indexNo = getVal(indexNos, i);
            boolean pageData = false;
            boolean nonChanged = false;
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                pageData = true;
            } else if (AppConsts.YES.equals(getVal(isPartEdit, i))) {
                pageData = true;
            } else if (!StringUtil.isEmpty(indexNo)) {
                nonChanged = true;
            }
            if (nonChanged) {
                List<AppSvcPersonnelDto> list = appSvcRelatedInfoDto.getSvcPersonnelDto().getNormalList();
                appSvcPersonnelDto = list.stream().
                        filter(dto -> Objects.equals(indexNo, dto.getIndexNo()))
                        .findAny()
                        .orElseGet(() -> {
                            AppSvcPersonnelDto dto = new AppSvcPersonnelDto();
                            dto.setIndexNo(indexNo);
                            return dto;
                        });
            } else if (pageData) {
                appSvcPersonnelDto = getAppSvcPersonnelParam(indexNo, request, prefix, String.valueOf(i), null);
            }
            normalList.add(appSvcPersonnelDto);
        }
        return normalList;

    }

    public static List<AppSvcPersonnelDto> getSpePersonnel(HttpServletRequest request, String prefix, Boolean isRfi, String appType,
            AppSvcRelatedInfoDto appSvcRelatedInfoDto) {
        List<AppSvcPersonnelDto> specialList = IaisCommonUtils.genNewArrayList();
        AppSvcPersonnelDto appSvcPersonnelDto = new AppSvcPersonnelDto();
        String[] speCountStrs = ParamUtil.getStrings(request, prefix + "speCount");
        String[] isPartEdit = ParamUtil.getStrings(request, prefix + "isPartEdit");
        String[] indexNos = ParamUtil.getStrings(request, prefix + "indexNo");
        int size = 0;
        if (speCountStrs != null && speCountStrs.length > 0) {
            size = speCountStrs.length;
        }
        for (int i = 0; i < size; i++) {
            String indexNo = getVal(indexNos, i);
            boolean pageData = false;
            boolean nonChanged = false;
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                pageData = true;
            } else if (AppConsts.YES.equals(getVal(isPartEdit, i))) {
                pageData = true;
            } else if (!StringUtil.isEmpty(indexNo)) {
                nonChanged = true;
            }
            if (nonChanged) {
                List<AppSvcPersonnelDto> list = appSvcRelatedInfoDto.getSvcPersonnelDto().getSpecialList();
                appSvcPersonnelDto = list.stream().
                        filter(dto -> Objects.equals(indexNo, dto.getIndexNo()))
                        .findAny()
                        .orElseGet(() -> {
                            AppSvcPersonnelDto dto = new AppSvcPersonnelDto();
                            dto.setIndexNo(indexNo);
                            return dto;
                        });
            } else if (pageData) {
                appSvcPersonnelDto = getAppSvcPersonnelParam(indexNo, request, prefix, String.valueOf(i), null);
            }
            specialList.add(appSvcPersonnelDto);
        }
        return specialList;
    }


    public static SvcPersonnelDto genAppSvcPersonnelDtoList(HttpServletRequest request, AppSvcRelatedInfoDto appSvcRelatedInfoDto,
            String appType) {
        SvcPersonnelDto svcPersonnelDto = appSvcRelatedInfoDto.getSvcPersonnelDto();
        if (StringUtil.isEmpty(svcPersonnelDto)) {
            svcPersonnelDto = new SvcPersonnelDto();
        }
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        List<AppSvcPersonnelDto> arPersonnel = getArPersonnel(request, ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER, isRfi,
                appType, appSvcRelatedInfoDto);
        List<AppSvcPersonnelDto> nuPersonnel = getNuPersonnel(request, ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES, isRfi, appType,
                appSvcRelatedInfoDto);
        List<AppSvcPersonnelDto> emPersonnel = getEmPersonnel(request, ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST, isRfi,
                appType, appSvcRelatedInfoDto);
        List<AppSvcPersonnelDto> norPersonnel = getNorPersonnel(request, ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS, isRfi,
                appType, appSvcRelatedInfoDto);
        List<AppSvcPersonnelDto> spePersonnel = getSpePersonnel(request, ApplicationConsts.SERVICE_PERSONNEL_TYPE_SPECIALS, isRfi,
                appType, appSvcRelatedInfoDto);
        if (IaisCommonUtils.isNotEmpty(arPersonnel)) {
            svcPersonnelDto.setArPractitionerList(arPersonnel);
        }
        if (IaisCommonUtils.isNotEmpty(nuPersonnel)) {
            svcPersonnelDto.setNurseList(nuPersonnel);
        }
        if (IaisCommonUtils.isNotEmpty(emPersonnel)) {
            svcPersonnelDto.setEmbryologistList(emPersonnel);
        }
        if (IaisCommonUtils.isNotEmpty(norPersonnel)) {
            svcPersonnelDto.setNormalList(norPersonnel);
        }
        if (IaisCommonUtils.isNotEmpty(spePersonnel)) {
            svcPersonnelDto.setSpecialList(spePersonnel);
        }
        return svcPersonnelDto;
    }

    public static List<AppSvcPrincipalOfficersDto> genAppSvcKeyAppointmentHolder(HttpServletRequest request) {
        return genKeyPersonnels(ApplicationConsts.PERSONNEL_PSN_KAH, "", request, false);
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
                    /*if (ApplicationConsts.PREMISES_TYPE_MOBILE.equals(premisesType) || ApplicationConsts.PREMISES_TYPE_REMOTE.equals(
                            premisesType)) {
                        getOHData = false;
                    }*/
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
                            String[] weeklyVal = ParamUtil.getStrings(request, i + "onSiteWeekly" + j);
                            String allDay = ParamUtil.getString(request, i + "onSiteWeeklyAllDay" + j);
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
                                String weeklyStartHH = ParamUtil.getString(request, i + "onSiteWeeklyStartHH" + j);
                                String weeklyStartMM = ParamUtil.getString(request, i + "onSiteWeeklyStartMM" + j);
                                int weeklyStartH = weeklyStartHH != null ? Integer.parseInt(weeklyStartHH) : 0;
                                int weeklyStartM = weeklyStartMM != null ? Integer.parseInt(weeklyStartMM) : 0;
                                Time timStart = Time.valueOf(LocalTime.of(weeklyStartH, weeklyStartM, 0));

                                String weeklyEndHH = ParamUtil.getString(request, i + "onSiteWeeklyEndHH" + j);
                                String weeklyEndMM = ParamUtil.getString(request, i + "onSiteWeeklyEndMM" + j);
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
                            String[] phVal = ParamUtil.getStrings(request, i + "onSitePubHoliday" + j);
                            String allDay = ParamUtil.getString(request, i + "onSitePhAllDay" + j);
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
                                String phStartHH = ParamUtil.getString(request, i + "onSitePhStartHH" + j);
                                String phStartMM = ParamUtil.getString(request, i + "onSitePhStartMM" + j);
                                int phStartH = phStartHH != null ? Integer.parseInt(phStartHH) : 0;
                                int phStartM = phStartMM != null ? Integer.parseInt(phStartMM) : 0;
                                Time timStart = Time.valueOf(LocalTime.of(phStartH, phStartM, 0));

                                String phEndHH = ParamUtil.getString(request, i + "onSitePhEndHH" + j);
                                String phEndMM = ParamUtil.getString(request, i + "onSitePhEndMM" + j);
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
                            String eventName = ParamUtil.getString(request, i + "onSiteEvent" + j);
                            String eventStartStr = ParamUtil.getString(request, i + "onSiteEventStart" + j);
                            Date eventStart = DateUtil.parseDate(eventStartStr, Formatter.DATE);
                            String eventEndStr = ParamUtil.getString(request, i + "onSiteEventEnd" + j);
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

    public static void setAppSvcOtherFormList(List<AppSvcOtherInfoDto> appSvcOtherInfoList, HttpServletRequest request) {
        if (IaisCommonUtils.isEmpty(appSvcOtherInfoList)) {
            log.info("The appSvcOtherInfoList is null!!!!");
            return;
        }
        for (AppSvcOtherInfoDto appSvcOtherInfoDto : appSvcOtherInfoList) {
            if (appSvcOtherInfoDto != null) {
                setAppSvcSuplmFormDto(appSvcOtherInfoDto.getAppSvcSuplmFormDto(), appSvcOtherInfoDto.getPremisesVal(), request);
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

    public static void genRenewalData(AppSubmissionDto appSubmissionDto, boolean isSingle, HttpServletRequest request)
            throws Exception {
        String renewEffectiveDate = ParamUtil.getDate(request, "renewEffectiveDate");
        appSubmissionDto.setEffectiveDateStr(renewEffectiveDate);
        if (CommonValidator.isDate(renewEffectiveDate)) {
            appSubmissionDto.setEffectiveDate(Formatter.parseDate(renewEffectiveDate));
        }
        String userAgreement = ParamUtil.getString(request, "verifyInfoCheckbox");
        appSubmissionDto.setUserAgreement(AppConsts.YES.equals(userAgreement));
        /*if (!StringUtil.isEmpty(userAgreement) && AppConsts.YES.equals(userAgreement)) {
            ParamUtil.setSessionAttr(request, "userAgreement", Boolean.TRUE);
        } else {
            ParamUtil.setSessionAttr(request, "userAgreement", Boolean.FALSE);
        }*/
        if (isSingle) {
            AppDeclarationMessageDto appDeclarationMessageDto = AppDataHelper.getAppDeclarationMessageDto(request,
                    ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            appSubmissionDto.setAppDeclarationMessageDto(appDeclarationMessageDto);
            appSubmissionDto.setAppDeclarationDocDtos(
                    AppDataHelper.getDeclarationFiles(ApplicationConsts.APPLICATION_TYPE_RENEWAL, request));
        }
    }

}
