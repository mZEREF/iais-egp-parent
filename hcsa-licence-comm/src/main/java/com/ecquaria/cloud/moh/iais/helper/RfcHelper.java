package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.job.executor.util.SpringHelper;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.SpecialServiceSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSpecialServiceInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.OperationHoursReloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ReflectionUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import com.ecquaria.cloud.moh.iais.util.PageDataCopyUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Auther chenlei on 5/3/2022.
 */
@Slf4j
public final class RfcHelper {

    private static ConfigCommService getConfigCommService() {
        return SpringHelper.getBean(ConfigCommService.class);
    }

    private static LicCommService getLicCommService() {
        return SpringHelper.getBean(LicCommService.class);
    }

    public static AppEditSelectDto rfcChangeModuleEvaluationDto(AppSubmissionDto appSubmissionDto,
            AppSubmissionDto oldAppSubmissionDto) {
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        boolean hciNameChange = false;
        if (IaisCommonUtils.isNotEmpty(oldAppGrpPremisesDtoList) && IaisCommonUtils.isNotEmpty(appGrpPremisesDtoList)) {
            for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
                hciNameChange = eqHciNameChange(appGrpPremisesDtoList.get(i), oldAppGrpPremisesDtoList.get(i));
                if (hciNameChange) {
                    appEditSelectDto.setChangeHciName(hciNameChange);
                    break;
                }
            }
        }
        boolean licenseeChange = isChangeSubLicensee(appSubmissionDto.getSubLicenseeDto(), oldAppSubmissionDto.getSubLicenseeDto());
        appEditSelectDto.setLicenseeEdit(licenseeChange);
        // MOSD
        boolean changeInLocation = isChangeInLocation(appSubmissionDto.getAppGrpPremisesDtoList(),
                oldAppSubmissionDto.getAppGrpPremisesDtoList());
        boolean changeFloorUnits = isChangeFloorUnit(appGrpPremisesDtoList, oldAppGrpPremisesDtoList);
        boolean changeCoLocation = isChangeCoLocation(appGrpPremisesDtoList, oldAppGrpPremisesDtoList);
        boolean changePremiseAutoFields = isChangeGrpPremisesAutoFields(appGrpPremisesDtoList,
                oldAppGrpPremisesDtoList);
        boolean grpPremiseIsChange = changeInLocation || changeFloorUnits || hciNameChange || changeCoLocation || changePremiseAutoFields;
        String premType = appGrpPremisesDtoList.get(0).getPremisesType();
        appEditSelectDto.setPremType(premType);
        appEditSelectDto.setChangeInLocation(changeInLocation);
        appEditSelectDto.setChangeFloorUnits(changeFloorUnits);
        appEditSelectDto.setChangePremiseAutoFields(changePremiseAutoFields);
        appEditSelectDto.setChangeCoLocation(changeCoLocation);
        appEditSelectDto.setPremisesEdit(grpPremiseIsChange);
        // specialised
        int changeSpecialisedFields = isChangeSpecialisedFields(appSubmissionDto.getAppPremSpecialisedDtoList(),
                appSubmissionDto.getAppPremSpecialisedDtoList());
        boolean changeSpecialised = changeSpecialisedFields != RfcConst.RFC_BASE;
        boolean changeSpecialisedNonAutoFields = (changeSpecialisedFields & RfcConst.RFC_AMENDMENT) != 0;
        boolean changeSpecialisedAutoFields = (changeSpecialisedFields & RfcConst.RFC_NOTIFICATION) != 0;
        appEditSelectDto.setChangeSpecialisedAutoFields(changeSpecialisedAutoFields);
        appEditSelectDto.setChangeSpecialisedNonAutoFields(changeSpecialisedNonAutoFields);
        appEditSelectDto.setSpecialisedEdit(changeSpecialised);
        // service related info
        boolean changePersonnel = isChangeKeyPersonnel(appSubmissionDto, oldAppSubmissionDto);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
        int changeVehiclesFields = isChangeAppSvcVehicleDtos(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);
        boolean changeVehicles = changeVehiclesFields!= RfcConst.RFC_BASE;
        boolean changeVehicleNonAutoFields = (changeVehiclesFields & RfcConst.RFC_AMENDMENT) != 0;
        boolean changeVehicleAutoFields = (changeVehiclesFields & RfcConst.RFC_NOTIFICATION) != 0;
        int changeBusinessFields = isChangeAppSvcBusinessDtos(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);
        boolean changeBusiness = changeBusinessFields!= RfcConst.RFC_BASE;
        boolean changeBusinessNonAutoFields = (changeBusinessFields & RfcConst.RFC_AMENDMENT) != 0;
        boolean changeBusinessAutoFields = (changeBusinessFields & RfcConst.RFC_NOTIFICATION) != 0;
        boolean changeSectionLeader = isChangeAppSvcSectionLeadersViaSvcInfo(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);
        boolean changeCharges = isChangeAppSvcChargesPageDto(appSvcRelatedInfoDtos.get(0).getAppSvcChargesPageDto(),
                oldAppSvcRelatedInfoDtos.get(0).getAppSvcChargesPageDto());
        boolean changeServiceAutoFields = changeCharges || isChangeSvcInfoAutoFields(appSvcRelatedInfoDtos,
                oldAppSvcRelatedInfoDtos, appEditSelectDto);
        appEditSelectDto.setChangeVehicle(changeVehicles);
        appEditSelectDto.setChangeVehicleAutoFields(changeVehicleAutoFields);
        appEditSelectDto.setChangeVehicleNonAutoFields(changeVehicleNonAutoFields);
        appEditSelectDto.setChangeBusinessName(changeBusinessAutoFields);
        appEditSelectDto.setChangeBusinessNonAutoFields(changeBusinessNonAutoFields);
        appEditSelectDto.setChangePersonnel(changePersonnel);
        appEditSelectDto.setChangeSectionLeader(changeSectionLeader);
        boolean serviceIsChange = changeVehicles || changeBusiness || changeSectionLeader
                || changeServiceAutoFields;
        appEditSelectDto.setServiceEdit(serviceIsChange);
        // set to appSubmissionDto
        appSubmissionDto.setChangeSelectDto(appEditSelectDto);
        // for splitting the submission
        AppEditSelectDto showDto = appSubmissionDto.getAppEditSelectDto();
        List<String> stepList = IaisCommonUtils.getList(showDto.getPersonnelEditList());
        if (changeCharges) {
            IaisCommonUtils.addToList(HcsaConsts.STEP_CHARGES, stepList);
        }
        if (changeVehicles) {
            stepList.add(HcsaConsts.STEP_VEHICLES);
        }
        if (changeBusiness) {
            stepList.add(HcsaConsts.STEP_BUSINESS_NAME);
        }
        if (changeSectionLeader) {
            stepList.add(HcsaConsts.STEP_SECTION_LEADER);
            //stepList.add(HcsaConsts.STEP_DOCUMENTS);
        }
        showDto.setPersonnelEditList(stepList);
        appSubmissionDto.setAppEditSelectDto(showDto);
        return appEditSelectDto;
    }

    public static boolean eqHciNameChange(AppGrpPremisesDto appGrpPremisesDto, AppGrpPremisesDto oldAppGrpPremisesDto) {
        String hciName = ApplicationHelper.getHciName(appGrpPremisesDto);
        String oldHciName = ApplicationHelper.getHciName(oldAppGrpPremisesDto);
        if (hciName == null && oldHciName == null) {
            return false;
        } else if (hciName == null) {
            return true;
        } else if (oldHciName == null) {
            return true;
        }
        return !hciName.equals(oldHciName);
    }

    public static boolean isChangeGrpPremises(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) {
        if (appGrpPremisesDtoList == null || oldAppGrpPremisesDtoList == null) {
            return false;
        }
        if (IaisCommonUtils.listChange(appGrpPremisesDtoList, oldAppGrpPremisesDtoList)) {
            return true;
        }
        for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
            AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtoList.get(i);
            AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtoList.get(i);
            if (isChangeGrpPremisesAutoFields(appGrpPremisesDto, oldAppGrpPremisesDto)) {
                return true;
            }
            if (eqHciNameChange(appGrpPremisesDto, oldAppGrpPremisesDto)) {
                return true;
            }
            if (isChangeInLocation(appGrpPremisesDto, oldAppGrpPremisesDto)) {
                return true;
            }
            if (isChangeFloorUnit(appGrpPremisesDto, oldAppGrpPremisesDto)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isChangeAppPremisesAddress(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) {
        if (appGrpPremisesDtoList == null || oldAppGrpPremisesDtoList == null) {
            return false;
        }
        if (IaisCommonUtils.listChange(appGrpPremisesDtoList, oldAppGrpPremisesDtoList)) {
            return true;
        }
        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
            AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtoList.stream()
                    .filter(dto -> Objects.equals(dto.getPremisesIndexNo(), appGrpPremisesDto.getPremisesIndexNo()))
                    .findAny()
                    .orElse(null);
            if (isChangeAppPremAddress(appGrpPremisesDto, oldAppGrpPremisesDto)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChangeAppPremAddress(AppGrpPremisesDto appGrpPremisesDto, AppGrpPremisesDto oldAppGrpPremisesDto) {
        if (appGrpPremisesDto == null || oldAppGrpPremisesDto == null) {
            return true;
        }
        if (!Objects.equals(appGrpPremisesDto.getPremisesType(), oldAppGrpPremisesDto.getPremisesType())) {
            return true;
        }
        if (!appGrpPremisesDto.getAddressWithoutFU().equals(oldAppGrpPremisesDto.getAddressWithoutFU())) {
            return true;
        }
        if (isChangeFloorUnit(appGrpPremisesDto, oldAppGrpPremisesDto)) {
            return true;
        }
        return !Objects.equals(appGrpPremisesDto.getBuildingName(), oldAppGrpPremisesDto.getBuildingName());
    }

    public static boolean isChangeGrpPremisesAutoFields(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) {
        if (appGrpPremisesDtoList == null || oldAppGrpPremisesDtoList == null) {
            return false;
        }
        if (IaisCommonUtils.listChange(appGrpPremisesDtoList, oldAppGrpPremisesDtoList)) {
            return true;
        }
        for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
            AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtoList.get(i);
            AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtoList.get(i);
            if (isChangeGrpPremisesAutoFields(appGrpPremisesDto, oldAppGrpPremisesDto)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChangeGrpPremisesAutoFields(AppGrpPremisesDto appGrpPremisesDto, AppGrpPremisesDto oldAppGrpPremisesDto) {
        return !isSame(appGrpPremisesDto, oldAppGrpPremisesDto, PageDataCopyUtil::copyAppGrpPremisesDtoForAutoField);
    }

    public static boolean isChangeCoLocation(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) {
        if (appGrpPremisesDtoList == null || oldAppGrpPremisesDtoList == null) {
            return false;
        }
        if (IaisCommonUtils.listChange(appGrpPremisesDtoList, oldAppGrpPremisesDtoList)) {
            return true;
        }
        for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
            AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtoList.get(i);
            AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtoList.get(i);
            if (!isSame(appGrpPremisesDto, oldAppGrpPremisesDto, PageDataCopyUtil::copyCoLocationFields)) {
                return true;
            }
        }
        return false;
    }

    public static int isChangeSpecialisedFields(List<AppPremSpecialisedDto> specialisedList,
            List<AppPremSpecialisedDto> oldSpecialisedList) {
        if (IaisCommonUtils.isEmpty(specialisedList)) {
            return RfcConst.RFC_NOTIFICATION;
        }
        if (IaisCommonUtils.isEmpty(oldSpecialisedList)) {
            return RfcConst.RFC_AMENDMENT;
        }
        int size = specialisedList.size();
        if (size != oldSpecialisedList.size()) {
            return RfcConst.RFC_AMENDMENT;
        }
        int result = RfcConst.RFC_BASE;
        for (int i = 0; i < size; i++) {
            result &= isChangeSpecialisedFields(specialisedList.get(i), oldSpecialisedList.get(i));
        }
        return result;
    }

    public static int isChangeSpecialisedFields(AppPremSpecialisedDto specialisedDto, AppPremSpecialisedDto oldSpecialisedDto) {
        if (specialisedDto == null || oldSpecialisedDto == null) {
            return RfcConst.RFC_AMENDMENT;
        }
        int result = isChangeAppPremScopeList(specialisedDto.getCheckedAppPremScopeDtoList(),
                oldSpecialisedDto.getCheckedAppPremScopeDtoList());
        List<AppPremSubSvcRelDto> appPremSubSvcRelList = specialisedDto.getFlatAppPremSubSvcRelList(dto ->
                StringUtil.isNotEmpty(dto.getActCode()));
        if (StringUtil.isEmpty(appPremSubSvcRelList)) {
            result &= RfcConst.RFC_AMENDMENT;
        } else {
            for (AppPremSubSvcRelDto relDto : appPremSubSvcRelList) {
                if (ApplicationConsts.RECORD_ACTION_CODE_ADD.equals(relDto.getActCode())) {
                    result &= relDto.isAdditionFlow() ? RfcConst.RFC_AMENDMENT : RfcConst.RFC_NOTIFICATION;
                } else if (ApplicationConsts.RECORD_ACTION_CODE_REMOVE.equals(relDto.getActCode())) {
                    result &= relDto.isRemovalFlow() ? RfcConst.RFC_AMENDMENT : RfcConst.RFC_NOTIFICATION;
                }
            }
        }
        return result;
    }

    public static int isChangeAppPremScopeList(List<AppPremScopeDto> appPremScopeDtoList,
            List<AppPremScopeDto> oldAppPremScopeDtoList) {
        if (IaisCommonUtils.isEmpty(appPremScopeDtoList)) {
            return RfcConst.RFC_NOTIFICATION;
        }
        if (IaisCommonUtils.isEmpty(oldAppPremScopeDtoList)) {
            return RfcConst.RFC_AMENDMENT;
        }
        int result = RfcConst.RFC_BASE;
        // add
        boolean noneMatch = appPremScopeDtoList.stream()
                .noneMatch(dto -> oldAppPremScopeDtoList.stream()
                        .anyMatch(oldDto -> Objects.equals(dto.getScopeName(), oldDto.getScopeName())));
        if (noneMatch) {
            result &= RfcConst.RFC_AMENDMENT;
        }
        // removal
        noneMatch = oldAppPremScopeDtoList.stream()
                .noneMatch(dto -> appPremScopeDtoList.stream()
                        .anyMatch(oldDto -> Objects.equals(dto.getScopeName(), oldDto.getScopeName())));
        if (noneMatch) {
            result &= RfcConst.RFC_NOTIFICATION;
        }
        return result;
    }
/*
    public static int isChangeAppPremScopeList(List<AppPremScopeDto> appPremScopeDtoList,
            List<AppPremScopeDto> oldAppPremScopeDtoList) {
        if (appPremScopeDtoList == null || appPremScopeDtoList == null) {
            return RfcConst.RFC_AMENDMENT;
        }
        return isChangedList(appPremScopeDtoList, appPremScopeDtoList, PageDataCopyUtil::copyMutableObjectList,
                "id", "appPremCorreId", "subTypeId", "parentId", "level", "appPremScopeDtos");
    }
*/

    public static boolean isChangeServiceInfo(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        return isChangeServiceInfo(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList, null);
    }

    public static boolean isChangeSvcInfoAutoFields(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, AppEditSelectDto appEditSelectDto) {
        List<String> changeList = IaisCommonUtils.genNewArrayList();
        List<AppSvcRelatedInfoDto> n = (List<AppSvcRelatedInfoDto>) CopyUtil.copyMutableObjectList(appSvcRelatedInfoDtoList);
        List<AppSvcRelatedInfoDto> o = (List<AppSvcRelatedInfoDto>) CopyUtil.copyMutableObjectList(oldAppSvcRelatedInfoDtoList);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = n.get(0);
        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = o.get(0);
        /*List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = appSvcRelatedInfoDto.getHcsaServiceStepSchemeDtos();
        String deputyPoFlag = appSvcRelatedInfoDto.getDeputyPoFlag();
        oldAppSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);
        oldAppSvcRelatedInfoDto.setDeputyPoFlag(deputyPoFlag);*/

        List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
        List<AppSvcDocDto> oldAppSvcDocDtoLit = oldAppSvcRelatedInfoDto.getAppSvcDocDtoLit();
        boolean changeSvcDocs = isChangeSvcDocs(appSvcDocDtoLit, oldAppSvcDocDtoLit);
        if (changeSvcDocs) {
            IaisCommonUtils.addToList(HcsaConsts.STEP_DOCUMENTS, changeList);
        }
        boolean eqAppSvcBusiness = isChangeAppSvcBusinessDto(appSvcRelatedInfoDto.getAppSvcBusinessDtoList(),
                oldAppSvcRelatedInfoDto.getAppSvcBusinessDtoList());

        if (appEditSelectDto != null) {
            List<String> personnelEditList = IaisCommonUtils.getList(appEditSelectDto.getPersonnelEditList());
            personnelEditList.addAll(changeList);
            appEditSelectDto.setPersonnelEditList(personnelEditList);
        }
        return changeSvcDocs || isChangePersonnel(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList)
                || eqAppSvcBusiness;
    }

    public static boolean isChangeServiceInfo(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, AppEditSelectDto appEditSelectDto) {
        List<String> changeList = IaisCommonUtils.genNewArrayList();
        List<AppSvcRelatedInfoDto> n = (List<AppSvcRelatedInfoDto>) CopyUtil.copyMutableObjectList(appSvcRelatedInfoDtoList);
        List<AppSvcRelatedInfoDto> o = (List<AppSvcRelatedInfoDto>) CopyUtil.copyMutableObjectList(oldAppSvcRelatedInfoDtoList);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = n.get(0);
        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = o.get(0);
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = appSvcRelatedInfoDto.getHcsaServiceStepSchemeDtos();
        String deputyPoFlag = appSvcRelatedInfoDto.getDeputyPoFlag();
        oldAppSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);
        oldAppSvcRelatedInfoDto.setDeputyPoFlag(deputyPoFlag);

        List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
        List<AppSvcDocDto> oldAppSvcDocDtoLit = oldAppSvcRelatedInfoDto.getAppSvcDocDtoLit();
        boolean changeSvcDocs = isChangeSvcDocs(appSvcDocDtoLit, oldAppSvcDocDtoLit);
        if (changeSvcDocs) {
            IaisCommonUtils.addToList(HcsaConsts.STEP_DOCUMENTS, changeList);
        }
        boolean eqAppSvcChargesPageDto = isChangeAppSvcChargesPageDto(appSvcRelatedInfoDto.getAppSvcChargesPageDto(),
                oldAppSvcRelatedInfoDto.getAppSvcChargesPageDto());
        if (eqAppSvcChargesPageDto) {
            IaisCommonUtils.addToList(HcsaConsts.STEP_CHARGES, changeList);
        }
        if (appEditSelectDto != null) {
            List<String> personnelEditList = IaisCommonUtils.getList(appEditSelectDto.getPersonnelEditList());
            personnelEditList.addAll(changeList);
            appEditSelectDto.setPersonnelEditList(personnelEditList);
        }
        return changeSvcDocs || eqAppSvcChargesPageDto || isChangePersonnel(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList)
                || isChangeAppSvcBusinessDto(appSvcRelatedInfoDto.getAppSvcBusinessDtoList(),
                oldAppSvcRelatedInfoDto.getAppSvcBusinessDtoList());
    }

    private static boolean isChangePersonnel(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        if (appSvcRelatedInfoDtoList == null && oldAppSvcRelatedInfoDtoList == null) {
            return false;
        } else if (appSvcRelatedInfoDtoList == null ^ oldAppSvcRelatedInfoDtoList == null) {
            return true;
        }
        if (appSvcRelatedInfoDtoList.size() != oldAppSvcRelatedInfoDtoList.size()) {
            return true;
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(0);
        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtoList.get(0);

        for (String psnType : IaisCommonUtils.getKeyPersonnel()) {
            if (isChangeKeyPersonnel(appSvcRelatedInfoDto, oldAppSvcRelatedInfoDto, psnType, false)) {
                return true;
            }
        }
        // section leader
        return isChangeServicePersonnels(appSvcRelatedInfoDto.getAppSvcSectionLeaderList(),
                oldAppSvcRelatedInfoDto.getAppSvcSectionLeaderList());
    }

    private static boolean isChangeSvcDocs(List<AppSvcDocDto> appSvcDocDtoLit, List<AppSvcDocDto> oldAppSvcDocDtoLit) {
        return !isSame(appSvcDocDtoLit, oldAppSvcDocDtoLit, PageDataCopyUtil::copySvcDocs);
    }

    public static boolean eqHciCode(AppGrpPremisesDto appGrpPremisesDto, AppGrpPremisesDto oldAppGrpPremisesDto) {
        String hciCode = appGrpPremisesDto.getHciCode();
        String oldHciCode = oldAppGrpPremisesDto.getHciCode();
        if (!StringUtil.isEmpty(hciCode)) {
            return hciCode.equals(oldHciCode);
        }
        return true;
    }

    public static boolean isChangeFloorUnit(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto) {
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        return isChangeFloorUnit(appGrpPremisesDtoList, oldAppGrpPremisesDtoList);
    }

    public static boolean isChangeFloorUnit(AppGrpPremisesDto appGrpPremisesDto, AppGrpPremisesDto oldAppGrpPremisesDto) {
        if (appGrpPremisesDto == null || oldAppGrpPremisesDto == null) {
            return true;
        }
        List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = resolveFloorUnitList(appGrpPremisesDto.getFloorNo(),
                appGrpPremisesDto.getUnitNo(), appGrpPremisesDto.getAppPremisesOperationalUnitDtos());
        List<AppPremisesOperationalUnitDto> oldAppPremisesOperationalUnitDtos = resolveFloorUnitList(oldAppGrpPremisesDto.getFloorNo(),
                oldAppGrpPremisesDto.getUnitNo(), oldAppGrpPremisesDto.getAppPremisesOperationalUnitDtos());
        return isChangeFloorUnitList(appPremisesOperationalUnitDtos, oldAppPremisesOperationalUnitDtos);
    }

    public static boolean isChangeFloorUnit(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) {
        List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = IaisCommonUtils.genNewArrayList();
        appGrpPremisesDtoList.forEach((v) -> appPremisesOperationalUnitDtos.addAll(resolveFloorUnitList(v.getFloorNo(),
                v.getUnitNo(), v.getAppPremisesOperationalUnitDtos())));
        List<AppPremisesOperationalUnitDto> oldAppPremisesOperationalUnitDtos = IaisCommonUtils.genNewArrayList();
        oldAppGrpPremisesDtoList.forEach((v) -> oldAppPremisesOperationalUnitDtos.addAll(resolveFloorUnitList(v.getFloorNo(),
                v.getUnitNo(), v.getAppPremisesOperationalUnitDtos())));
        return isChangeFloorUnitList(appPremisesOperationalUnitDtos, oldAppPremisesOperationalUnitDtos);
    }

    private static List<AppPremisesOperationalUnitDto> resolveFloorUnitList(String floorNo, String unitNo,
            List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos) {
        List<AppPremisesOperationalUnitDto> result = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(floorNo) && !StringUtil.isEmpty(unitNo)) {
            AppPremisesOperationalUnitDto currDto = new AppPremisesOperationalUnitDto();
            currDto.setFloorNo(floorNo);
            currDto.setUnitNo(unitNo);
            result.add(currDto);
        }
        if (!IaisCommonUtils.isEmpty(appPremisesOperationalUnitDtos)) {
            result.addAll(appPremisesOperationalUnitDtos);
        }
        return result;
    }

    private static boolean isChangeFloorUnitList(List<AppPremisesOperationalUnitDto> source,
            List<AppPremisesOperationalUnitDto> oldSource) {
        return isChangedList(source, oldSource, (dto, list) -> list.stream().
                noneMatch(tar -> Objects.equals(dto.getUnitNo(), tar.getUnitNo())
                        && Objects.equals(dto.getFloorNo(), tar.getFloorNo())));
    }

    public static boolean compareHciName(AppGrpPremisesDto premisesListQueryDto, AppGrpPremisesDto appGrpPremisesDto) {
        String newHciName = ApplicationHelper.getHciName(appGrpPremisesDto);
        String oldHciName = ApplicationHelper.getHciName(premisesListQueryDto);
        String oldVehicleNo = "";
        if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesListQueryDto.getPremisesType())) {
            oldVehicleNo = premisesListQueryDto.getVehicleNo();
        }
        String newVehicleNo = "";
        if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())) {
            newVehicleNo = appGrpPremisesDto.getVehicleNo();
        }

        return newHciName.equals(oldHciName) && newVehicleNo.equals(oldVehicleNo);
    }

    public static boolean isChangeHciName(List<AppGrpPremisesDto> appGrpPremisesDtos, List<AppGrpPremisesDto> oldAppGrpPremisesDtos) {
        boolean isChanged = false;
        int length = appGrpPremisesDtos.size();
        int oldLength = oldAppGrpPremisesDtos.size();
        if (length == oldLength) {
            for (int i = 0; i < length; i++) {
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(0);
                AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(0);
                if (!compareHciName(appGrpPremisesDto, oldAppGrpPremisesDto)) {
                    isChanged = true;
                    break;
                }
            }
        }
        return isChanged;
    }

    /**
     * @param appGrpPremisesDtos
     * @param oldAppGrpPremisesDtos
     * @return true: the same
     */
    public static boolean isChangeInLocation(List<AppGrpPremisesDto> appGrpPremisesDtos,
            List<AppGrpPremisesDto> oldAppGrpPremisesDtos) {
        if (appGrpPremisesDtos == null || oldAppGrpPremisesDtos == null) {
            return false;
        }
        if (IaisCommonUtils.listChange(appGrpPremisesDtos, oldAppGrpPremisesDtos)) {
            return true;
        }
        for (int i = 0; i < appGrpPremisesDtos.size(); i++) {
            AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(i);
            AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(i);
            if (isChangeInLocation(appGrpPremisesDto, oldAppGrpPremisesDto)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChangeInLocation(AppGrpPremisesDto appGrpPremisesDto,
            AppGrpPremisesDto oldAppGrpPremisesDto) {
        if (appGrpPremisesDto == null || oldAppGrpPremisesDto == null) {
            return true;
        }
        return !isSame(appGrpPremisesDto, oldAppGrpPremisesDto, PageDataCopyUtil::copyInLocationFields);
    }

    public static int isChangeAppSvcVehicleDtos(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos) {
        if (appSvcRelatedInfoDtos == null || oldAppSvcRelatedInfoDtos == null) {
            return RfcConst.RFC_AMENDMENT;
        }
        if (appSvcRelatedInfoDtos.size() != oldAppSvcRelatedInfoDtos.size()) {
            return RfcConst.RFC_AMENDMENT;
        }
        int result = RfcConst.RFC_BASE;
        for (int i = 0, len = appSvcRelatedInfoDtos.size(); i < len; i++) {
            result &=isChangeAppSvcVehicleDto(appSvcRelatedInfoDtos.get(i).getAppSvcVehicleDtoList(),
                    oldAppSvcRelatedInfoDtos.get(i).getAppSvcVehicleDtoList());
        }
        return result;
    }

    public static int isChangeAppSvcVehicleDto(List<AppSvcVehicleDto> appSvcVehicleDtoList,
            List<AppSvcVehicleDto> oldAppSvcVehicleDtoList) {
        if (IaisCommonUtils.isEmpty(appSvcVehicleDtoList)) {
            return RfcConst.RFC_NOTIFICATION;
        }
        if (IaisCommonUtils.isEmpty(oldAppSvcVehicleDtoList)) {
            return RfcConst.RFC_AMENDMENT;
        }
        int result = RfcConst.RFC_BASE;
        // add
        boolean noneMatch = appSvcVehicleDtoList.stream()
                .noneMatch(dto -> oldAppSvcVehicleDtoList.stream()
                        .anyMatch(oldDto -> Objects.equals(dto.getEngineNum(), oldDto.getEngineNum())));
        if (noneMatch) {
            result &= RfcConst.RFC_AMENDMENT;
        }
        // removal
        noneMatch = oldAppSvcVehicleDtoList.stream()
                .noneMatch(dto -> appSvcVehicleDtoList.stream()
                        .anyMatch(oldDto -> Objects.equals(dto.getEngineNum(), oldDto.getEngineNum())));
        if (noneMatch) {
            result &= RfcConst.RFC_NOTIFICATION;
        }
        return result;
    }

    public static int isChangeAppSvcBusinessDtos(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        if (appSvcRelatedInfoDtoList == null || oldAppSvcRelatedInfoDtoList == null) {
            return RfcConst.RFC_AMENDMENT;
        }
        if (appSvcRelatedInfoDtoList.size() != oldAppSvcRelatedInfoDtoList.size()) {
            return RfcConst.RFC_AMENDMENT;
        }
        List<AppSvcBusinessDto> appSvcBusinessDtoList = IaisCommonUtils.genNewArrayList();
        appSvcRelatedInfoDtoList.forEach((item) -> appSvcBusinessDtoList.addAll(item.getAppSvcBusinessDtoList()));
        List<AppSvcBusinessDto> oldAppSvcBusinessDtoList = IaisCommonUtils.genNewArrayList();
        oldAppSvcRelatedInfoDtoList.forEach((item) -> oldAppSvcBusinessDtoList.addAll(item.getAppSvcBusinessDtoList()));
        int result = RfcConst.RFC_BASE;
        for (int i = 0, len = appSvcRelatedInfoDtoList.size(); i < len; i++) {
            boolean changeAppSvcBusinessDto = isChangeAppSvcBusinessDto(appSvcBusinessDtoList, oldAppSvcBusinessDtoList);
            if (changeAppSvcBusinessDto){
                result &= RfcConst.RFC_AMENDMENT;
            }
            boolean changeAppSvcBusinessDtoOtherInfo = isChangeAppSvcBusinessDtoOtherInfo(appSvcBusinessDtoList, oldAppSvcBusinessDtoList);
            if (changeAppSvcBusinessDtoOtherInfo){
                result &= RfcConst.RFC_NOTIFICATION;
            }
        }
        return result;
    }

    public static boolean isChangeAppSvcBusinessDto(List<AppSvcBusinessDto> appSvcBusinessDtoList,
            List<AppSvcBusinessDto> oldAppSvcBusinessDtoList) {
        List<String> appSvcBusinessNameList = IaisCommonUtils.genNewArrayList();
        appSvcBusinessDtoList.forEach((v) -> appSvcBusinessNameList.add(v.getBusinessName()));
        List<String> oldAppSvcBusinessNameList = IaisCommonUtils.genNewArrayList();
        oldAppSvcBusinessDtoList.forEach((v) -> oldAppSvcBusinessNameList.add(v.getBusinessName()));
        return !appSvcBusinessNameList.equals(oldAppSvcBusinessNameList);
    }

    public static boolean isChangeAppSvcBusinessDtoOtherInfo(List<AppSvcBusinessDto> appSvcBusinessDtoList,
            List<AppSvcBusinessDto> oldAppSvcBusinessDtoList) {
        boolean isChange=false;
        List<String> appSvcBusinessContactList = IaisCommonUtils.genNewArrayList();
        appSvcBusinessDtoList.forEach((v) -> appSvcBusinessContactList.add(v.getContactNo()));
        List<String> oldAppSvcBusinessContactList = IaisCommonUtils.genNewArrayList();
        oldAppSvcBusinessDtoList.forEach((v) -> oldAppSvcBusinessContactList.add(v.getContactNo()));
        if (!appSvcBusinessContactList.equals(oldAppSvcBusinessContactList)){
            isChange=true;
        }
        List<String> appSvcBusinessEmailList = IaisCommonUtils.genNewArrayList();
        appSvcBusinessDtoList.forEach((v) -> appSvcBusinessEmailList.add(v.getEmailAddr()));
        List<String> oldAppSvcBusinessEmailList = IaisCommonUtils.genNewArrayList();
        oldAppSvcBusinessDtoList.forEach((v) -> oldAppSvcBusinessEmailList.add(v.getEmailAddr()));
        if (!appSvcBusinessEmailList.equals(oldAppSvcBusinessEmailList)){
            isChange=true;
        }
        List<OperationHoursReloadDto> appSvcBusinessOperationHoursList = IaisCommonUtils.genNewArrayList();
        appSvcBusinessDtoList.forEach((v) -> {
            appSvcBusinessOperationHoursList.addAll(v.getWeeklyDtoList());
            appSvcBusinessOperationHoursList.addAll(v.getPhDtoList());

        });
        List<OperationHoursReloadDto> n = PageDataCopyUtil.copyOperationHoursReloadDto(appSvcBusinessOperationHoursList);
        List<OperationHoursReloadDto> oldAppSvcBusinessOperationHoursList = IaisCommonUtils.genNewArrayList();
        oldAppSvcBusinessDtoList.forEach((v) -> {
            oldAppSvcBusinessOperationHoursList.addAll(v.getWeeklyDtoList());
            oldAppSvcBusinessOperationHoursList.addAll(v.getPhDtoList());

        });
        List<OperationHoursReloadDto> o = PageDataCopyUtil.copyOperationHoursReloadDto(appSvcBusinessOperationHoursList);
        if (!n.equals(o)){
            isChange=true;
        }
        List<AppPremEventPeriodDto> event=IaisCommonUtils.genNewArrayList();
        appSvcBusinessDtoList.forEach((v) -> event.addAll(v.getEventDtoList()));
        List<AppPremEventPeriodDto> copyEvent = PageDataCopyUtil.copyEvent(event);
        List<AppPremEventPeriodDto> oldevent=IaisCommonUtils.genNewArrayList();
        oldAppSvcBusinessDtoList.forEach((v) -> oldevent.addAll(v.getEventDtoList()));
        List<AppPremEventPeriodDto> copyOldEvent = PageDataCopyUtil.copyEvent(event);
        if (!copyEvent.equals(copyOldEvent)){
            isChange=true;
        }
        return isChange;

    }

    public static boolean isChangeAppSvcChargesPageDto(AppSvcChargesPageDto appSvcChargesPageDto,
            AppSvcChargesPageDto oldAppSvcChargesPageDto) {
        AppSvcChargesPageDto n = PageDataCopyUtil.copyAppSvcClinicalDirector(appSvcChargesPageDto);
        AppSvcChargesPageDto o = PageDataCopyUtil.copyAppSvcClinicalDirector(oldAppSvcChargesPageDto);
        return !n.equals(o);
    }

    public static boolean eqAppSvcClinicalDirector(List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos,
            List<AppSvcPrincipalOfficersDto> oldAppSvcClinicalDirectorDtos) {
        List<AppSvcPrincipalOfficersDto> n = PageDataCopyUtil.copyAppSvcClinicalDirector(appSvcClinicalDirectorDtos);
        List<AppSvcPrincipalOfficersDto> o = PageDataCopyUtil.copyAppSvcClinicalDirector(oldAppSvcClinicalDirectorDtos);
        return !n.equals(o);
    }

    public static boolean isChangeSubLicensee(SubLicenseeDto subLicenseeDto, SubLicenseeDto oldSbLicenseeDto) {
        return !Objects.equals(subLicenseeDto, oldSbLicenseeDto);
    }

    public static boolean isChangeKeyPersonnel(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto) {
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
        if (appEditSelectDto == null || !appEditSelectDto.isServiceEdit()) {
            return false;
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (appSvcRelatedInfoDtoList == null && oldAppSvcRelatedInfoDtoList == null) {
            return false;
        } else if (appSvcRelatedInfoDtoList == null ^ oldAppSvcRelatedInfoDtoList == null) {
            return true;
        }
        if (appSvcRelatedInfoDtoList.size() != oldAppSvcRelatedInfoDtoList.size()) {
            return true;
        }
        boolean isChanged = false;
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(0);
        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtoList.get(0);
        List<String> personnelEditList = IaisCommonUtils.genNewArrayList();
        for (String psnType : IaisCommonUtils.getKeyPersonnel()) {
            if (isChangeKeyPersonnel(appSvcRelatedInfoDto, oldAppSvcRelatedInfoDto, psnType, true)) {
                isChanged = true;
                personnelEditList.add(psnType);
            }
        }
        appEditSelectDto.setPersonnelEditList(personnelEditList);
        appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
        return !isChanged;
    }

    private static boolean isChangeKeyPersonnel(AppSvcRelatedInfoDto appSvcRelatedInfoDto,
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto, String psnType, boolean onlyCheckAddRemoval) {
        List<AppSvcPrincipalOfficersDto> newList = ApplicationHelper.getKeyPersonnel(psnType, appSvcRelatedInfoDto);
        List<AppSvcPrincipalOfficersDto> oldList = ApplicationHelper.getKeyPersonnel(psnType, oldAppSvcRelatedInfoDto);
        return isChangeKeyPersonnel(newList, oldList, onlyCheckAddRemoval);
    }

    private static boolean isChangeKeyPersonnel(List<AppSvcPrincipalOfficersDto> newList, List<AppSvcPrincipalOfficersDto> oldList,
            boolean onlyCheckAddRemoval) {
        return isChangedList(newList, oldList, (dto, list) -> list.stream()
                .filter(obj -> Objects.equals(IaisCommonUtils.getPersonKey(obj), IaisCommonUtils.getPersonKey(dto)))
                .findAny()
                .orElse(null), (clazz, fieldName) -> {
            if (onlyCheckAddRemoval) {
                return StringUtil.isIn(fieldName, new String[]{"nationality", "idType", "idNo"});
            } else {
                return !StringUtil.isIn(fieldName, new String[]{"psnEditDto", "licPerson", "backend", "singleName", "needSpcOptList",
                        "spcOptList", "specialityHtml", "id", "indexNo", "curPersonelId", "nationality", "idType", "idNo"});
            }
        });
    }

    public static boolean isChangeAppSvcSectionLeadersViaSvcInfo(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos) {
        if (appSvcRelatedInfoDtos == null && oldAppSvcRelatedInfoDtos == null) {
            return false;
        } else if (appSvcRelatedInfoDtos == null ^ oldAppSvcRelatedInfoDtos == null) {
            return true;
        }
        int size = appSvcRelatedInfoDtos.size();
        if (size != oldAppSvcRelatedInfoDtos.size()) {
            return true;
        }
        for (int i = 0; i < size; i++) {
            if (isChangeServicePersonnels(appSvcRelatedInfoDtos.get(i).getAppSvcSectionLeaderList(),
                    oldAppSvcRelatedInfoDtos.get(i).getAppSvcSectionLeaderList())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChangeServicePersonnels(List<AppSvcPersonnelDto> servicePersonnelList,
            List<AppSvcPersonnelDto> oldServicePersonnelListList) {
        return isSame(servicePersonnelList, oldServicePersonnelListList, PageDataCopyUtil::copySvcPersonnels);
    }

    public static boolean isChanged(List<AppSvcPrincipalOfficersDto> psnList, List<AppSvcPrincipalOfficersDto> oldPsnList) {
        if (psnList == null || psnList.isEmpty() || oldPsnList == null || oldPsnList.isEmpty()) {
            return false;
        }
        for (AppSvcPrincipalOfficersDto psnDto : psnList) {
            String personKey = ApplicationHelper.getPersonKey(psnDto);
            for (AppSvcPrincipalOfficersDto oldPsnDto : oldPsnList) {
                if (Objects.equals(personKey, ApplicationHelper.getPersonKey(oldPsnDto))) {
                    return !Objects.equals(PageDataCopyUtil.copyKeyPersonnel(psnDto), PageDataCopyUtil.copyKeyPersonnel(oldPsnDto));
                }
            }
        }
        return false;
    }

    public static List<String> checkKeyPersonnelChanged(List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList,
            List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList) {
        List<String> ids = IaisCommonUtils.genNewArrayList();
        if (appSvcCgoDtoList != null && oldAppSvcCgoDtoList != null) {
            if (appSvcCgoDtoList.equals(oldAppSvcCgoDtoList)) {
                return ids;
            }
            for (AppSvcPrincipalOfficersDto appSvcCgoDto : appSvcCgoDtoList) {
                String personKey = ApplicationHelper.getPersonKey(appSvcCgoDto);
                for (AppSvcPrincipalOfficersDto appSvcCgoDto1 : oldAppSvcCgoDtoList) {
                    if (Objects.equals(personKey, ApplicationHelper.getPersonKey(appSvcCgoDto1))) {
                        if (isChangSpecialFields(appSvcCgoDto, appSvcCgoDto1)) {
                            ids.add(personKey);
                        }
                    }
                }
            }
        }
        return ids;
    }

    private static boolean isChangSpecialFields(AppSvcPrincipalOfficersDto source, AppSvcPrincipalOfficersDto target) {
        if (source == null || target == null) {
            return true;
        }
        return !Objects.equals(PageDataCopyUtil.copyKeyPersonnel(source, 2),
                PageDataCopyUtil.copyKeyPersonnel(target, 2));
    }

    public static void reSetPersonnels(AppSvcRelatedInfoDto sourceReletedInfo, AppSvcRelatedInfoDto targetReletedInfo, String psnType,
            List<String> personnelEditList) {
        if (sourceReletedInfo == null || targetReletedInfo == null) {
            return;
        }
        log.info(StringUtil.changeForLog("Re-set personnel affected by " + psnType));
        List<AppSvcPrincipalOfficersDto> sourceList = null;
        if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)) {
            sourceList = sourceReletedInfo.getAppSvcCgoDtoList();
        } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(psnType)) {
            sourceList = sourceReletedInfo.getAppSvcMedAlertPersonList();
        } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)) {
            sourceList = sourceReletedInfo.getAppSvcPrincipalOfficersDtoList();
        } else if (ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR.equals(psnType)) {
            sourceList = sourceReletedInfo.getAppSvcClinicalDirectorDtoList();
        } else if (ApplicationConsts.PERSONNEL_PSN_KAH.equals(psnType)) {
            sourceList = sourceReletedInfo.getAppSvcKeyAppointmentHolderDtoList();
        }
        reSetPersonnels(sourceList, targetReletedInfo.getAppSvcCgoDtoList(), psnType,
                ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, personnelEditList);
        reSetPersonnels(sourceList, targetReletedInfo.getAppSvcMedAlertPersonList(), psnType,
                ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, personnelEditList);
        reSetPersonnels(sourceList, targetReletedInfo.getAppSvcPrincipalOfficersDtoList(), psnType,
                ApplicationConsts.PERSONNEL_PSN_TYPE_PO, personnelEditList);
        reSetPersonnels(sourceList, targetReletedInfo.getAppSvcClinicalDirectorDtoList(), psnType,
                ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, personnelEditList);
        reSetPersonnels(sourceList, targetReletedInfo.getAppSvcKeyAppointmentHolderDtoList(), psnType,
                ApplicationConsts.PERSONNEL_PSN_KAH, personnelEditList);
    }

    private static void reSetPersonnels(List<AppSvcPrincipalOfficersDto> sourceList, List<AppSvcPrincipalOfficersDto> targetList,
            String sourcePsnType, String psnType, List<String> personnelEditList) {
        if (sourceList == null || targetList == null || StringUtil.isEmpty(sourcePsnType)) {
            return;
        }
        boolean isPersonnelEdit = false;
        for (AppSvcPrincipalOfficersDto target : targetList) {
            for (AppSvcPrincipalOfficersDto source : sourceList) {
                if (Objects.equals(target.getIdNo(), source.getIdNo())) {
                    if (sourcePsnType.equals(psnType)) {
                        AppSvcPrincipalOfficersDto newDto = CopyUtil.copyMutableObject(source);
                        newDto.setIndexNo(target.getIndexNo());
                        newDto.setCurPersonelId(target.getCurPersonelId());
                        targetList.set(targetList.indexOf(target), newDto);
                    } else {
                        ApplicationHelper.syncPsnDto(source, target);
                    }
                    isPersonnelEdit = true;
                    break;
                }
            }
        }
        if (isPersonnelEdit) {
            personnelEditList.add(psnType);
        }
    }

    /**
     * Bundle / align
     * <p>
     * TODO need to be changed
     *
     * @param appSubmissionDto
     */
    public static void setRelatedInfoBaseServiceId(AppSubmissionDto appSubmissionDto) {
        if (appSubmissionDto == null) {
            return;
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (appSvcRelatedInfoDtoList == null || appSvcRelatedInfoDtoList.isEmpty()) {
            return;
        }
        ConfigCommService configCommService = getConfigCommService();
        LicCommService licCommService = getLicCommService();
        for (AppSvcRelatedInfoDto var1 : appSvcRelatedInfoDtoList) {
            if (var1.getBaseServiceId() != null && var1.getServiceId() != null) {
                continue;
            }
            String serviceName = var1.getServiceName();//cannot null
            HcsaServiceDto activeHcsaServiceDtoByName = configCommService.getActiveHcsaServiceDtoByName(serviceName);
            String svcType = activeHcsaServiceDtoByName.getSvcType();
            String id = activeHcsaServiceDtoByName.getId();
            String baseService = "";
            if (HcsaConsts.SERVICE_TYPE_BASE.equals(svcType)) {
                var1.setBaseServiceId(activeHcsaServiceDtoByName.getId());
            } else if (HcsaConsts.SERVICE_TYPE_SPECIFIED.equals(svcType)) {
                String licenceId = appSubmissionDto.getLicenceId();
                List<HcsaServiceCorrelationDto> serviceCorrelationDtos = configCommService.getActiveSvcCorrelation();
                if (serviceCorrelationDtos == null || serviceCorrelationDtos.isEmpty()) {
                    continue;
                }
                Iterator<HcsaServiceCorrelationDto> iterator = serviceCorrelationDtos.iterator();
                while (iterator.hasNext()) {
                    HcsaServiceCorrelationDto next = iterator.next();
                    String specifiedSvcId = next.getSpecifiedSvcId();
                    if (id.equals(specifiedSvcId)) {
                        baseService = next.getBaseSvcId();
                        break;
                    }
                }
                /*if (!StringUtil.isEmpty(baseService)) {
                    String service_name = configCommService.getServiceNameById(baseService);
                    List<LicBaseSpecifiedCorrelationDto> entity = licCommService.getLicBaseSpecifiedCorrelationDtos(
                            HcsaConsts.SERVICE_TYPE_SPECIFIED, licenceId);
                    if (entity != null && !entity.isEmpty()) {
                        Iterator<LicBaseSpecifiedCorrelationDto> iterator1 = entity.iterator();
                        while (iterator1.hasNext()) {
                            LicBaseSpecifiedCorrelationDto next = iterator1.next();
                            String baseLicId = next.getBaseLicId();
                            LicenceDto licenceDto = licCommService.getActiveLicenceById(baseLicId);
                            if (licenceDto.getSvcName().equals(service_name)) {
                                var1.setBaseServiceId(baseService);
                                break;
                            }
                        }
                    }
                }*/
            }
            var1.setServiceId(activeHcsaServiceDtoByName.getId());
        }
    }

    public static void recursingChooseLabUpward(Map<String, HcsaSvcSubtypeOrSubsumedDto> map, String targetSvcScopeId,
            List<String> svcScopeIdList, List<AppSvcChckListDto> newSvcScopeList) {
        HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto = map.get(targetSvcScopeId);
        if (hcsaSvcSubtypeOrSubsumedDto != null) {
            String id = hcsaSvcSubtypeOrSubsumedDto.getId();
            if (!svcScopeIdList.contains(id)) {
                //check this parent checkbox
                AppSvcChckListDto appSvcChckListDto = new AppSvcChckListDto();
                appSvcChckListDto.setChkLstConfId(id);
                appSvcChckListDto.setChkLstType(hcsaSvcSubtypeOrSubsumedDto.getType());
                appSvcChckListDto.setChkName(hcsaSvcSubtypeOrSubsumedDto.getName());
                appSvcChckListDto.setParentName(hcsaSvcSubtypeOrSubsumedDto.getParentId());
                appSvcChckListDto.setChildrenName(hcsaSvcSubtypeOrSubsumedDto.getChildrenId());
                newSvcScopeList.add(appSvcChckListDto);
                svcScopeIdList.add(id);
            }
            String parentId = hcsaSvcSubtypeOrSubsumedDto.getParentId();
            if (!StringUtil.isEmpty(parentId)) {
                if (!svcScopeIdList.contains(parentId)) {
                    //turn
                    recursingChooseLabUpward(map, parentId, svcScopeIdList, newSvcScopeList);
                }
            }
        }

    }

    /**
     * Generate the svc related info with the auto fields changed
     *
     * @param appSubmissionDto
     * @param oldAppSubmissionDto
     * @param changeList:         The changed personnel types.
     *                            Refer to the method - eqServiceChange and personContact
     * @param stepList:           The steps which is changed, it will contains not-auto fields and maybe it contains auto fields.
     *                            Refer to the method - compareNotChangePersonnel
     *                            && rfcChangeModuleEvaluationDto
     * @return The svc related info with the auto fields changed
     */
    public static List<AppSvcRelatedInfoDto> generateDtosForAutoFields(AppSubmissionDto appSubmissionDto,
            AppSubmissionDto oldAppSubmissionDto, List<String> changeList, List<String> stepList) {
        AppSvcRelatedInfoDto oldSvcInfoDto = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        AppSvcRelatedInfoDto currSvcInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        if (currSvcInfoDto == null || oldSvcInfoDto == null) {
            return null;
        }
        if (stepList == null || stepList.isEmpty()) {
            return appSubmissionDto.getAppSvcRelatedInfoDtoList();
        }
        if (changeList == null) {
            changeList = IaisCommonUtils.genNewArrayList();
        }
        AppSvcRelatedInfoDto newDto = CopyUtil.copyMutableObject(currSvcInfoDto);
        for (String step : stepList) {
            if (HcsaConsts.STEP_BUSINESS_NAME.equals(step)) {
                newDto.setAppSvcBusinessDtoList(
                        (List<AppSvcBusinessDto>) CopyUtil.copyMutableObjectList(oldSvcInfoDto.getAppSvcBusinessDtoList()));
            } else if (HcsaConsts.STEP_VEHICLES.equals(step)) {
                newDto.setAppSvcVehicleDtoList(
                        (List<AppSvcVehicleDto>) CopyUtil.copyMutableObjectList(oldSvcInfoDto.getAppSvcVehicleDtoList()));
            } else if (HcsaConsts.STEP_SECTION_LEADER.equals(step)) {
                newDto.setAppSvcSectionLeaderList(
                        (List<AppSvcPersonnelDto>) CopyUtil.copyMutableObjectList(oldSvcInfoDto.getAppSvcSectionLeaderList()));
            } else if (HcsaConsts.STEP_DOCUMENTS.equals(step)) {
                /*List<AppSvcDocDto> oldASvcDocDtoLit = oldSvcInfoDto.getAppSvcDocDtoLit();
                List<AppSvcDocDto> appSvcDocDtoLit = newDto.getAppSvcDocDtoLit();*/
            } else if (HcsaConsts.STEP_PRINCIPAL_OFFICERS.equals(step)) {
                reSetPersonnels(oldSvcInfoDto, newDto, step, changeList.contains(ApplicationConsts.PERSONNEL_PSN_TYPE_PO));
            } else if (HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(step)) {
                reSetPersonnels(oldSvcInfoDto, newDto, step, changeList.contains(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO));
            } else if (HcsaConsts.STEP_CLINICAL_DIRECTOR.equals(step)) {
                reSetPersonnels(oldSvcInfoDto, newDto, step, changeList.contains(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR));
            } else if (HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER.equals(step)) {
                reSetPersonnels(oldSvcInfoDto, newDto, step, changeList.contains(ApplicationConsts.PERSONNEL_PSN_KAH));
            } else if (HcsaConsts.MEDALERT_PERSON.equals(step)) {
                reSetPersonnels(oldSvcInfoDto, newDto, step, changeList.contains(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP));
            }
        }
        List<AppSvcRelatedInfoDto> result = IaisCommonUtils.genNewArrayList(1);
        result.add(newDto);
        return result;
    }

    private static void reSetPersonnels(AppSvcRelatedInfoDto sourceReletedInfo, AppSvcRelatedInfoDto targetReletedInfo, String step,
            boolean isChanged) {
        List<AppSvcPrincipalOfficersDto> psnList = null;
        List<AppSvcPrincipalOfficersDto> newList = null;
        if (HcsaConsts.STEP_PRINCIPAL_OFFICERS.equals(step)) {
            psnList = (List<AppSvcPrincipalOfficersDto>) CopyUtil.copyMutableObjectList(
                    sourceReletedInfo.getAppSvcPrincipalOfficersDtoList());
            newList = targetReletedInfo.getAppSvcPrincipalOfficersDtoList();
        } else if (HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(step)) {
            psnList = (List<AppSvcPrincipalOfficersDto>) CopyUtil.copyMutableObjectList(
                    sourceReletedInfo.getAppSvcCgoDtoList());
            newList = targetReletedInfo.getAppSvcCgoDtoList();
        } else if (HcsaConsts.STEP_CLINICAL_DIRECTOR.equals(step)) {
            psnList = (List<AppSvcPrincipalOfficersDto>) CopyUtil.copyMutableObjectList(
                    sourceReletedInfo.getAppSvcClinicalDirectorDtoList());
            newList = targetReletedInfo.getAppSvcClinicalDirectorDtoList();
        } else if (HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER.equals(step)) {
            psnList = (List<AppSvcPrincipalOfficersDto>) CopyUtil.copyMutableObjectList(
                    sourceReletedInfo.getAppSvcKeyAppointmentHolderDtoList());
            newList = targetReletedInfo.getAppSvcKeyAppointmentHolderDtoList();
        } else if (HcsaConsts.STEP_MEDALERT_PERSON.equals(step)) {
            psnList = (List<AppSvcPrincipalOfficersDto>) CopyUtil.copyMutableObjectList(
                    sourceReletedInfo.getAppSvcMedAlertPersonList());
            newList = targetReletedInfo.getAppSvcMedAlertPersonList();
        }
        if (isChanged && psnList != null && newList != null) {
            for (int i = 0, len = psnList.size(); i < len; i++) {
                AppSvcPrincipalOfficersDto psnDto = psnList.get(i);
                for (AppSvcPrincipalOfficersDto newPsn : newList) {
                    if (Objects.equals(psnDto.getIdNo(), newPsn.getIdNo())) {
                        psnList.set(i, newPsn);
                        break;
                    }
                }
            }
        }
        if (HcsaConsts.STEP_PRINCIPAL_OFFICERS.equals(step)) {
            targetReletedInfo.setAppSvcPrincipalOfficersDtoList(psnList);
        } else if (HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(step)) {
            targetReletedInfo.setAppSvcCgoDtoList(psnList);
        } else if (HcsaConsts.STEP_CLINICAL_DIRECTOR.equals(step)) {
            targetReletedInfo.setAppSvcClinicalDirectorDtoList(psnList);
        } else if (HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER.equals(step)) {
            targetReletedInfo.setAppSvcKeyAppointmentHolderDtoList(psnList);
        } else if (HcsaConsts.STEP_MEDALERT_PERSON.equals(step)) {
            targetReletedInfo.setAppSvcMedAlertPersonList(psnList);
        }
    }

    public static void setRiskToDto(AppSubmissionDto appSubmissionDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = IaisCommonUtils.genNewArrayList();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setApptype(appSubmissionDto.getAppType());
            riskAcceptiionDtoList.add(riskAcceptiionDto);
        }
        ConfigCommService configCommService = getConfigCommService();
        List<RiskResultDto> riskResultDtoList = configCommService.getRiskResult(riskAcceptiionDtoList);
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            String serviceCode = appSvcRelatedInfoDto.getServiceCode();
            RiskResultDto riskResultDto = getRiskResultDtoByServiceCode(riskResultDtoList, serviceCode);
            if (riskResultDto != null) {
                appSvcRelatedInfoDto.setScore(riskResultDto.getScore());
                appSvcRelatedInfoDto.setDoRiskDate(riskResultDto.getDoRiskDate());
            }
        }
    }

    private static RiskResultDto getRiskResultDtoByServiceCode(List<RiskResultDto> riskResultDtoList, String serviceCode) {
        RiskResultDto result = null;
        if (riskResultDtoList == null || StringUtil.isEmpty(serviceCode)) {
            return null;
        }
        for (RiskResultDto riskResultDto : riskResultDtoList) {
            if (serviceCode.equals(riskResultDto.getSvcCode())) {
                result = riskResultDto;
                break;
            }
        }
        return result;
    }

    public static List<String> getSpecialServiceList(AppSubmissionDto appSubmissionDto) {
        List<AppPremSpecialisedDto> appPremSpecialisedDtoList = appSubmissionDto.getAppPremSpecialisedDtoList();
        if (IaisCommonUtils.isEmpty(appPremSpecialisedDtoList)) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<String> result = IaisCommonUtils.genNewArrayList();
        appPremSpecialisedDtoList.forEach(dto -> dto.getCheckedAppPremSubSvcRelDtoList().forEach(
                rel -> result.add(dto.getPremisesVal() + dto.getBaseSvcId() + rel.getSvcId())));
        return result;
    }

    public static void beforeSubmit(AppSubmissionDto appSubmissionDto, AppEditSelectDto appEditSelectDto, String appGrpNo,
            String appType, HttpServletRequest request) {
        if (appSubmissionDto == null) {
            return;
        }
        DealSessionUtil.initView(appSubmissionDto);
        appSubmissionDto.setAppGrpId(null);
        appSubmissionDto.setFromBe(ApplicationHelper.isBackend());
        appSubmissionDto.setAppType(appType);
        // max file index
        DealSessionUtil.initMaxFileIndex(appSubmissionDto, request);

        //judge is the preInspection
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = getConfigCommService().judgeIsPreInspection(appSubmissionDto);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
        } else {
            appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }
        //set Risk Score
        setRiskToDto(appSubmissionDto);
        // reSetAdditionalFields
        appSubmissionDto.setChangeSelectDto(appEditSelectDto);
        ApplicationHelper.reSetAdditionalFields(appSubmissionDto, appEditSelectDto, appGrpNo);
//        ApplicationHelper.reSetAdditionalFields(appSubmissionDto, oldAppSubmissionDto);
        // bind application
        setRelatedInfoBaseServiceId(appSubmissionDto);
        /*if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)) {
            appSubmissionDto.setAppStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
        } else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            appSubmissionDto.setAppStatus(ApplicationConsts.APPLICATION_STATUS_RENEWAL);
        }*/
    }

    public static void resolveSvcActionCode(List<AppPremSubSvcRelDto> relList, Map<String, AppPremSubSvcRelDto> oldDtaMap) {
        if (IaisCommonUtils.isEmpty(relList)) {
            return;
        }
        for (AppPremSubSvcRelDto relDto : relList) {
            AppPremSubSvcRelDto oldRelDto = oldDtaMap.get(relDto.getSvcCode());
            if (oldRelDto == null && !relDto.isChecked()) {
                continue;
            }
            relDto.setStatus(ApplicationConsts.RECORD_STATUS_SUBMIT_CODE);
            if (oldRelDto == null && relDto.isChecked()) {
                relDto.setActCode(ApplicationConsts.RECORD_ACTION_CODE_ADD);
            } else if (oldRelDto != null) {
                boolean oldExisted = !ApplicationConsts.RECORD_ACTION_CODE_REMOVE.equals(oldRelDto.getActCode())
                        && oldRelDto.isChecked();
                if (!relDto.isChecked() && oldExisted) {
                    relDto.setActCode(ApplicationConsts.RECORD_ACTION_CODE_REMOVE);
                } else if (relDto.isChecked() && oldExisted) {
                    relDto.setActCode(ApplicationConsts.RECORD_ACTION_CODE_UNCHANGE);
                } else if (!relDto.isChecked() && !oldExisted) {
                    if (ApplicationConsts.RECORD_ACTION_CODE_REMOVE.equals(oldRelDto.getActCode())) {
                        relDto.setActCode(ApplicationConsts.RECORD_ACTION_CODE_REMOVE);
                    }
                } else if (relDto.isChecked() && !oldExisted) {
                    relDto.setActCode(ApplicationConsts.RECORD_ACTION_CODE_ADD);
                }
            }
            resolveSvcActionCode(relDto.getAppPremSubSvcRelDtos(), oldDtaMap);
        }
    }

    public static void resolveSpecialisedNonAutoData(AppSubmissionDto autoAppSubmissionDto, AppSubmissionDto oldAppSubmissionDto) {
        if (autoAppSubmissionDto == null || oldAppSubmissionDto == null) {
            return;
        }
        List<AppPremSpecialisedDto> appPremSpecialisedDtoList = autoAppSubmissionDto.getAppPremSpecialisedDtoList();
        List<AppPremSpecialisedDto> oldAppPremSpecialisedDtoList = oldAppSubmissionDto.getAppPremSpecialisedDtoList();
        if (IaisCommonUtils.isEmpty(appPremSpecialisedDtoList) || IaisCommonUtils.isEmpty(oldAppPremSpecialisedDtoList)) {
            return;
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = autoAppSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList) || IaisCommonUtils.isEmpty(oldAppSvcRelatedInfoDtoList)) {
            return;
        }
        resolveSpecialisedNonAutoData(appPremSpecialisedDtoList.get(0), oldAppPremSpecialisedDtoList.get(0),
                appSvcRelatedInfoDtoList.get(0), oldAppSvcRelatedInfoDtoList.get(0));
    }

    public static void resolveSpecialisedNonAutoData(AppPremSpecialisedDto appPremSpecialisedDto,
            AppPremSpecialisedDto oldAppPremSpecialisedDto, AppSvcRelatedInfoDto appSvcRelatedInfoDto,
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto) {
        // scope
        Map<String, AppPremScopeDto> scopeDtoMap = oldAppPremSpecialisedDto.getCheckedAppPremScopeDtoList()
                .stream().collect(Collectors.toMap(AppPremScopeDto::getSubTypeId, Function.identity()));
        List<AppPremScopeDto> appPremScopeDtoList = appPremSpecialisedDto.getAppPremScopeDtoList();
        if (IaisCommonUtils.isNotEmpty(appPremScopeDtoList)) {
            for (AppPremScopeDto scopeDto : appPremScopeDtoList) {
                if (scopeDtoMap.get(scopeDto.getSubTypeId()) == null) {
                    scopeDto.setChecked(false);
                    scopeDto.resolveAppPremScopeDtos(dto -> dto.setChecked(false));
                }
            }
            appPremSpecialisedDto.initAllAppPremScopeDtoList();
        }
        // service
        Map<String, AppPremSubSvcRelDto> relDtoMap = oldAppPremSpecialisedDto.getFlatAppPremSubSvcRelList(dto -> true)
                .stream().collect(Collectors.toMap(AppPremSubSvcRelDto::getSvcCode, Function.identity()));
        List<AppPremSubSvcRelDto> appPremSubSvcRelDtoList = appPremSpecialisedDto.getAppPremSubSvcRelDtoList();
        if (IaisCommonUtils.isNotEmpty(appPremSubSvcRelDtoList)) {
            // special service info
            AppSvcSpecialServiceInfoDto appSvcSpecialServiceInfoDto = appSvcRelatedInfoDto.getAppSvcSpecialServiceInfoList().get(0);
            AppSvcSpecialServiceInfoDto oldAppSvcSpecialServiceInfoDto =
                    oldAppSvcRelatedInfoDto.getAppSvcSpecialServiceInfoList().get(0);
            List<SpecialServiceSectionDto> sectionList = IaisCommonUtils.getList(
                    appSvcSpecialServiceInfoDto.getSpecialServiceSectionDtoList());
            Map<String, SpecialServiceSectionDto> sectionDtoMap = IaisCommonUtils.getList(
                    oldAppSvcSpecialServiceInfoDto.getSpecialServiceSectionDtoList())
                    .stream().collect(Collectors.toMap(SpecialServiceSectionDto::getSvcCode, Function.identity()));
            // document
            List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
            Map<String, List<AppSvcDocDto>> docMap = IaisCommonUtils.getList(oldAppSvcRelatedInfoDto.getAppSvcDocDtoLit())
                    .stream().collect(Collectors.groupingBy(AppSvcDocDto::getSvcId, Collectors.toList()));
            Consumer<AppPremSubSvcRelDto> change = relDto -> {
                if (relDto.isAdditionFlow()) {
                    AppPremSubSvcRelDto oldRel = relDtoMap.get(relDto.getSvcCode());
                    if (relDto.isChecked() && !oldRel.isChecked()) {
                        relDto.setChecked(false);
                    }
                }
                if (relDto.isRemovalFlow()) {
                    AppPremSubSvcRelDto oldRel = relDtoMap.get(relDto.getSvcCode());
                    if (!relDto.isChecked() && oldRel.isChecked()) {
                        relDto.setChecked(true);
                        SpecialServiceSectionDto sectionDto = sectionDtoMap.get(relDto.getSvcCode());
                        if (sectionDto != null) {
                            sectionList.add(sectionDto);
                        }
                        List<AppSvcDocDto> appSvcDocDtos = docMap.get(relDto.getSvcId());
                        if (IaisCommonUtils.isNotEmpty(appSvcDocDtos)) {
                            appSvcDocDtoLit.addAll(appSvcDocDtos);
                        }
                    }
                }
                if (relDto.isChecked()) {
                    relDto.setActCode(ApplicationConsts.RECORD_STATUS_APPROVE_CODE);
                }
            };
            for (AppPremSubSvcRelDto relDto : appPremSubSvcRelDtoList) {
                change.accept(relDto);
                relDto.resolveAppPremSubSvcRelDtos(change);
            }
            appSvcSpecialServiceInfoDto.setSpecialServiceSectionDtoList(sectionList);
            appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtoLit);
        }
    }

    public static <T, R> boolean isSame(T source, T target, Function<T, R> newObject) {
        R newSrc = newObject.apply(source);
        R newTar = newObject.apply(target);
        return Objects.equals(newSrc, newTar);
    }

    public static <T, R> boolean isSame(List<T> source, List<T> target, Function<List<T>, List<R>> newList) {
        if (source == null && target == null) {
            return false;
        } else if (source == null ^ target == null) {
            return true;
        }
        if (source.size() != target.size()) {
            return true;
        }
        List<R> newSrc = newList.apply(source);
        List<R> newTar = newList.apply(target);
        return newSrc.equals(newTar);
    }

    public static <R> boolean isChanged(Object source, Object target, BiFunction<Object, Object, Object> checkTarget,
            BiPredicate<Class, String> filter) {
        if (source == null && target == null) {
            return false;
        } else if (source == null ^ target == null) {
            return true;
        }
        if (!Objects.equals(source.getClass(), target.getClass())) {
            return true;
        }
        boolean isChanged;
        Class<?> type = target.getClass();
        if (String.class.isAssignableFrom(type)) {
            isChanged = !Objects.equals(source, target);
        } else if (int.class.isAssignableFrom(type)) {
            isChanged = (int) source != (int) target;
        } else if (long.class.isAssignableFrom(type)) {
            isChanged = (long) source == (int) target;
        } else if (double.class.isAssignableFrom(type)
                || Double.class.isAssignableFrom(type)) {
            BigDecimal s = BigDecimal.valueOf((double) source);
            BigDecimal t = BigDecimal.valueOf((double) target);
            isChanged = s.compareTo(t) != 0;
        } else if (Integer.class.isAssignableFrom(type)) {
            isChanged = !Objects.equals(source, target);
        } else if (Long.class.isAssignableFrom(type)) {
            isChanged = !Objects.equals(source, target);
        } else if (boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) {
            isChanged = (boolean) source != (boolean) target;
        } else if (Date.class.isAssignableFrom(type)) {
            isChanged = !Objects.equals(source, target);
        } else if (String[].class.isAssignableFrom(type)) {
            isChanged = isChangedArray((String[]) source, (String[]) target, Comparator.naturalOrder(), null);
        } else if (Integer[].class.isAssignableFrom(type) || int[].class.isAssignableFrom(type)) {
            isChanged = isChangedArray((Integer[]) source, (Integer[]) target, Comparator.naturalOrder(), null);
        } else if (Long[].class.isAssignableFrom(type) || long[].class.isAssignableFrom(type)) {
            isChanged = isChangedArray((Long[]) source, (Long[]) target, Comparator.naturalOrder(), null);
        } else if (Double[].class.isAssignableFrom(type) || double[].class.isAssignableFrom(type)) {
            isChanged = isChangedArray((Double[]) source, (Double[]) target, Comparator.naturalOrder(), null);
        } else if (List.class.isAssignableFrom(type)) {
            BiFunction<R, List<R>, R> check = null;
            if (checkTarget != null) {
                check = (r, list) -> (R) checkTarget.apply(r, list);
            }
            isChanged = isChangedList((List<R>) source, (List<R>) target, check, filter);
        } else if (Set.class.isAssignableFrom(type)) {
            BiFunction<R, Set<R>, R> check = null;
            if (checkTarget != null) {
                check = (r, list) -> (R) checkTarget.apply(r, list);
            }
            isChanged = isChangedSet((Set<R>) source, (Set<R>) target, check, filter);
        } else if (Map.class.isAssignableFrom(type)) {
            isChanged = false;// can't
        } else {
            Field[] fields = type.getDeclaredFields();
            if (fields.length == 0) {
                isChanged = false;
            } else {
                isChanged = !Arrays.stream(fields)
                        .filter(field -> filter == null || filter.test(type, field.getName()))
                        .filter(field -> !Modifier.isStatic(field.getModifiers()) && Modifier.isNative(field.getModifiers()))
                        .allMatch(field -> {
                            Object srcObj = ReflectionUtil.getPropertyObj(field, source);
                            Object tarObj = ReflectionUtil.getPropertyObj(field, target);
                            return !isChanged(srcObj, tarObj, checkTarget, filter);
                        });
            }
        }
        return isChanged;
    }

    public static <T> boolean isChangedArray(T[] src, T[] oldSrc, Comparator<T> comparator,
            BiPredicate<Class, String> filter) {
        if (src == null && oldSrc == null) {
            return false;
        } else if (src == null ^ oldSrc == null) {
            return true;
        }
        if (src.length != oldSrc.length) {
            return true;
        }
        if (comparator != null) {
            Arrays.sort(src, comparator);
            Arrays.sort(oldSrc, comparator);
        }
        int len = src.length;
        for (int i = 0; i < len; i++) {
            if (isChanged(src[i], oldSrc[i], null, filter)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean isChangedArray(T[] src, T[] oldSrc, BiFunction<T, T[], T> target,
            BiPredicate<Class, String> filter) {
        if (src == null && oldSrc == null) {
            return false;
        } else if (src == null ^ oldSrc == null) {
            return true;
        }
        if (src.length != oldSrc.length) {
            return true;
        }
        boolean anyMatch = Arrays.stream(src)
                .anyMatch(t -> isChanged(t, target.apply(t, oldSrc), null, filter));
        if (anyMatch) {
            return true;
        }
        return Arrays.stream(oldSrc)
                .anyMatch(t -> isChanged(t, target.apply(t, src), null, filter));
    }

    public static <T> boolean isChangedList(List<T> src, List<T> oldSrc, BiFunction<T, List<T>, T> target,
            BiPredicate<Class, String> filter) {
        if (src == null && oldSrc == null) {
            return false;
        } else if (src == null ^ oldSrc == null) {
            return true;
        }
        if (src.size() != oldSrc.size()) {
            return true;
        }
        boolean anyMatch = src.stream()
                .anyMatch(t -> isChanged(t, target.apply(t, oldSrc), null, filter));
        if (anyMatch) {
            return true;
        }
        return oldSrc.stream()
                .anyMatch(t -> isChanged(t, target.apply(t, src), null, filter));
    }

    public static <T> boolean isChangedList(List<T> src, List<T> oldSrc, BiPredicate<T, List<T>> check) {
        if (src == null && oldSrc == null) {
            return false;
        } else if (src == null ^ oldSrc == null) {
            return true;
        }
        if (src.size() != oldSrc.size()) {
            return true;
        }
        boolean noneMatch = src.stream()
                .noneMatch(t -> check.test(t, oldSrc));
        if (noneMatch) {
            return true;
        }
        return oldSrc.stream()
                .noneMatch(t -> check.test(t, src));
    }

    public static <T> boolean isChangedSet(Set<T> src, Set<T> oldSrc, BiFunction<T, Set<T>, T> target,
            BiPredicate<Class, String> filter) {
        if (src == null && oldSrc == null) {
            return false;
        } else if (src == null ^ oldSrc == null) {
            return true;
        }
        if (src.size() != oldSrc.size()) {
            return true;
        }
        boolean anyMatch = src.stream()
                .anyMatch(t -> isChanged(t, target.apply(t, oldSrc), null, filter));
        if (anyMatch) {
            return true;
        }
        return oldSrc.stream()
                .anyMatch(t -> isChanged(t, target.apply(t, src), null, filter));
    }

}
