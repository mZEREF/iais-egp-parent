package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.job.executor.util.SpringHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.SpecialServiceSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremGroupOutsourcedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoAbortDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoDto;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.SuppleFormItemConfigDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import com.ecquaria.cloud.moh.iais.util.PageDataCopyUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
        boolean grpPremiseIsChange = hciNameChange || changeInLocation || changeFloorUnits || changeCoLocation
                || changePremiseAutoFields;
        String premType = appGrpPremisesDtoList.get(0).getPremisesType();
        appEditSelectDto.setPremType(premType);
        appEditSelectDto.setChangeHciName(hciNameChange);
        appEditSelectDto.setChangeInLocation(changeInLocation);
        appEditSelectDto.setChangeFloorUnits(changeFloorUnits);
        appEditSelectDto.setChangePremiseAutoFields(changePremiseAutoFields);
        appEditSelectDto.setChangeCoLocation(changeCoLocation);
        appEditSelectDto.setPremisesEdit(grpPremiseIsChange);
        // specialised
        int changeSpecialisedFields = isChangeSpecialisedFields(appSubmissionDto.getAppPremSpecialisedDtoList(),
                oldAppSubmissionDto.getAppPremSpecialisedDtoList());
        boolean changeSpecialised = changeSpecialisedFields != RfcConst.RFC_UNCHANGED;
        if (changeSpecialised) {
            boolean changeSpecialisedNonAutoFields = (changeSpecialisedFields & RfcConst.RFC_AMENDMENT) != 0;
            boolean changeSpecialisedAutoFields = (changeSpecialisedFields & RfcConst.RFC_NOTIFICATION) != 0;
            appEditSelectDto.setChangeSpecialisedAutoFields(changeSpecialisedAutoFields);
            appEditSelectDto.setChangeSpecialisedNonAutoFields(changeSpecialisedNonAutoFields);
        }
        appEditSelectDto.setSpecialisedEdit(changeSpecialised);
        // service related info
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<String> autoList = IaisCommonUtils.genNewArrayList();
        List<String> nonAutoList = IaisCommonUtils.genNewArrayList();
        int changeVehiclesFields = isChangeAppSvcVehicleDtos(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, autoList, nonAutoList);
        boolean changeVehicles = changeVehiclesFields != RfcConst.RFC_UNCHANGED;
        if (changeVehicles) {
            boolean changeVehicleNonAutoFields = (changeVehiclesFields & RfcConst.RFC_AMENDMENT) != 0;
            boolean changeVehicleAutoFields = (changeVehiclesFields & RfcConst.RFC_NOTIFICATION) != 0;
            appEditSelectDto.setChangeVehicleAutoFields(changeVehicleAutoFields);
            appEditSelectDto.setChangeVehicleNonAutoFields(changeVehicleNonAutoFields);
        }
        appEditSelectDto.setChangeVehicle(changeVehicles);
        int changeBusinessFields = isChangeAppSvcBusinessDtos(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, autoList, nonAutoList);
        boolean changeBusiness = changeBusinessFields != RfcConst.RFC_UNCHANGED;
        if (changeBusiness) {
            boolean changeBusinessNonAutoFields = (changeBusinessFields & RfcConst.RFC_AMENDMENT) != 0;
            boolean changeBusinessAutoFields = (changeBusinessFields & RfcConst.RFC_NOTIFICATION) != 0;
            appEditSelectDto.setChangeBusinessName(changeBusinessNonAutoFields);
            appEditSelectDto.setChangeBusinessAutoFields(changeBusinessAutoFields);
        }
        boolean addOrReplacePersonnel = addOrReplacePersonnel(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, nonAutoList, autoList);
        boolean removePersonnel = removePersonnel(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, nonAutoList, autoList);
        boolean changePersonnel = changePersonnel(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, nonAutoList, autoList);
//        Supplementary Form
        boolean changeSupplementaryForm = isChangeSupplementaryForm(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, nonAutoList);
        appEditSelectDto.setSendMessageAndNoAutoSuForm(changeSupplementaryForm);
        boolean changeCharges = isChangeAppSvcChargesPageDto(appSvcRelatedInfoDtos.get(0).getAppSvcChargesPageDto(),
                oldAppSvcRelatedInfoDtos.get(0).getAppSvcChargesPageDto());
        //SpecialService Information
        boolean changeSpecialServiceInformation = ischangeSpecialServiceInformation(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos,
                nonAutoList);
        //other info
        int changeOtherInfoFields = isChangeAppSvcOtherInfoDto(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, nonAutoList, autoList);
        boolean changeOtherInfoDto = changeOtherInfoFields != RfcConst.RFC_UNCHANGED;
        if (changeOtherInfoDto) {
            boolean changeOtherInfoNonAutoFields = (changeOtherInfoFields & RfcConst.RFC_AMENDMENT) != 0;
            boolean changeOtherService = (changeOtherInfoFields & RfcConst.RFC_NOTIFICATION) != 0;
            appEditSelectDto.setChangeOtherInfoTop(changeOtherInfoNonAutoFields);
            appEditSelectDto.setChangeOtherService(changeOtherService);
        }
        //outsource
        boolean changeOutsourceFields = isChangeAppSvcOutsouredDto(appSvcRelatedInfoDtos.get(0).getAppSvcOutsouredDto(),
                oldAppSvcRelatedInfoDtos.get(0).getAppSvcOutsouredDto());
        if (changeCharges) {
            autoList.add(HcsaConsts.STEP_CHARGES);
        }
        if (changeOutsourceFields) {
            nonAutoList.add(HcsaConsts.STEP_OUTSOURCED_PROVIDERS);
        }
        appEditSelectDto.setChangeSpecialServiceInformation(changeSpecialServiceInformation);
        appEditSelectDto.setChangeOtherInfo(changeOtherInfoDto);
        appEditSelectDto.setChangeOutsourcedProviders(changeOutsourceFields);
        appEditSelectDto.setAddOrReplacePersonnel(addOrReplacePersonnel);
        appEditSelectDto.setRemovePersonnel(removePersonnel);
        appEditSelectDto.setChangePersonnel(changePersonnel);
        // document
        boolean changeSvcDocs = isChangeSvcDocs(appSvcRelatedInfoDtos.get(0), oldAppSvcRelatedInfoDtos.get(0));
        if (changeSvcDocs) {
            autoList.add(HcsaConsts.STEP_DOCUMENTS);
        }
        boolean changeServiceAutoFields = changeCharges || changeSvcDocs;
        boolean serviceIsChange = changeVehicles || changeBusiness
                || changeServiceAutoFields || changeSpecialServiceInformation || addOrReplacePersonnel
                || removePersonnel || changePersonnel || changeSupplementaryForm || changeOtherInfoDto
                || changeOutsourceFields;
        appEditSelectDto.setServiceEdit(serviceIsChange);
        // set to appSubmissionDto
        appSubmissionDto.setChangeSelectDto(appEditSelectDto);
        // for splitting the submission
        AppEditSelectDto showDto = appSubmissionDto.getAppEditSelectDto();
        /*List<String> stepList = IaisCommonUtils.getList(showDto.getPersonnelEditList());
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
        }*/
        showDto.setPersonnelEditList(nonAutoList);
        appSubmissionDto.setAppEditSelectDto(showDto);
        appEditSelectDto.setPersonnelEditList(autoList);
        log.info(StringUtil.changeForLog(appSubmissionDto.getLicenceNo() + " - App Edit Select Dto: "
                + JsonUtil.parseToJson(appEditSelectDto)));
        appEditSelectDto.initEmailSend();
        return appEditSelectDto;
    }

    //    SendMessageNoAuto
    public static boolean addOrReplacePersonnel(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos, List<String> nonAutoList, List<String> autoList) {
        boolean sectionLeader = isAddOrReplaceAppSvcSectionLeaders(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, nonAutoList);
        boolean svcPersonnel = isAddOrReplaceSvcPersonnel(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, nonAutoList);
        boolean governanceOfficer = isAddOrReplaceCd(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, true, nonAutoList);
        boolean keyPersonnel = isAddOrReplaceKey(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, true, nonAutoList);
        boolean flag = sectionLeader || svcPersonnel || governanceOfficer || keyPersonnel;
        return flag;
    }

    //    SendMessageAuto
    public static boolean removePersonnel(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos, List<String> nonAutoList, List<String> autoList) {
        boolean sectionLeader = isRemoveAppSvcSectionLeaders(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, autoList);
        boolean svcPersonnel = isRemoveSvcPersonnel(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, autoList);
        boolean governanceOfficer = isRemoveClinicalGovernanceOfficers(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, autoList);
        boolean keyPersonnel = isRemoveKeyPersonnel(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, autoList);
        boolean flag = sectionLeader || svcPersonnel || governanceOfficer || keyPersonnel;
        return flag;
    }

    //    noSendMessageAndAuto
    public static boolean changePersonnel(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos, List<String> nonAutoList, List<String> autoList) {
        boolean sectionLeader = isChangeDetailAppSvcSectionLeaders(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, autoList);
        boolean svcPersonnel = isChangeDetailSvcPersonnel(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, autoList);
        boolean governanceOfficer = isChangeDetailClinicalGovernanceOfficers(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos,
                autoList);
        boolean keyPersonnel = isChangeDetailKeyPersonnel(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, autoList);
        boolean flag = sectionLeader || svcPersonnel || governanceOfficer || keyPersonnel;
        return flag;
    }

    public static boolean isChangeDetailClinicalGovernanceOfficers(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, List<String> autoList) {
        if (appSvcRelatedInfoDtoList == null || oldAppSvcRelatedInfoDtoList == null) {
            return false;
        }
        int size = appSvcRelatedInfoDtoList.size();
        if (size != oldAppSvcRelatedInfoDtoList.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(i);
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtoList.get(i);
            List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            if (combinationAppSvcCd(appSvcCgoDtoList, oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList(), false)) {
                autoList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
                return true;
            }
        }
        return false;
    }

    public static boolean isRemoveClinicalGovernanceOfficers(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, List<String> autoList) {
        int compareLength = compareLength(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        if (compareLength == RfcConst.RFC_REMOVE) {
            return true;
        }
        if (compareLength == RfcConst.RFC_SAME) {
            int size = appSvcRelatedInfoDtoList.size();
            for (int i = 0; i < size; i++) {
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(i);
                AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtoList.get(i);
                List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList = oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList();
                if (compareDeleteCd(appSvcCgoDtoList, oldAppSvcCgoDtoList)) {
                    autoList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
                    return true;
                }
            }
        }
        return false;
    }

    //    change detail
    public static boolean isChangeDetailAppSvcSectionLeaders(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos, List<String> autoList) {
        if (appSvcRelatedInfoDtos == null || oldAppSvcRelatedInfoDtos == null) {
            return false;
        }
        int size = appSvcRelatedInfoDtos.size();
        if (size != oldAppSvcRelatedInfoDtos.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtos.get(i);
            List<AppSvcPersonnelDto> appSvcSectionLeaderList = appSvcRelatedInfoDto.getAppSvcSectionLeaderList();
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtos.get(i);
            List<AppSvcPersonnelDto> oldAppSvcSectionLeaderList = oldAppSvcRelatedInfoDto.getAppSvcSectionLeaderList();
            if (combinationAppSvcPersonnelDto(appSvcSectionLeaderList, oldAppSvcSectionLeaderList, false)) {
                autoList.add(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
                return true;
            }
        }
        return false;
    }

    public static boolean isAddOrReplaceAppSvcSectionLeaders(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos, List<String> nonAutoList) {
        int compareLength = compareLength(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);
        if (compareLength == RfcConst.RFC_NULL) {
            return false;
        }
        if (compareLength == RfcConst.RFC_ADD) {
            return true;
        }
        int size = appSvcRelatedInfoDtos.size();
        for (int i = 0; i < size; i++) {
            List<AppSvcPersonnelDto> appSvcSectionLeaderList = appSvcRelatedInfoDtos.get(i).getAppSvcSectionLeaderList();
            List<AppSvcPersonnelDto> oldAppSvcSectionLeaderList = oldAppSvcRelatedInfoDtos.get(i).getAppSvcSectionLeaderList();
            if (RfcConst.RFC_ADD == compareLength(appSvcSectionLeaderList, oldAppSvcSectionLeaderList)) {
                nonAutoList.add(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
                return true;
            }
            boolean isSame = combinationAppSvcPersonnelDto(appSvcSectionLeaderList, oldAppSvcSectionLeaderList, true);
            if (isSame) {
                nonAutoList.add(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
                return true;
            }
        }
        return false;
    }

    public static boolean isRemoveAppSvcSectionLeaders(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos, List<String> autoList) {
        int compareLength = compareLength(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);
        if (compareLength == RfcConst.RFC_REMOVE) {
            return true;
        }
        if (compareLength == RfcConst.RFC_NULL) {
            return false;
        }
        if (compareLength == RfcConst.RFC_SAME) {
            int size = appSvcRelatedInfoDtos.size();
            for (int i = 0; i < size; i++) {
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtos.get(i);
                AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtos.get(i);
                List<AppSvcPersonnelDto> appSvcSectionLeaderList = appSvcRelatedInfoDto.getAppSvcSectionLeaderList();
                List<AppSvcPersonnelDto> oldAppSvcSectionLeaderList = oldAppSvcRelatedInfoDto.getAppSvcSectionLeaderList();
                int length = compareLength(appSvcSectionLeaderList, oldAppSvcSectionLeaderList);
                if (length == RfcConst.RFC_REMOVE) {
                    autoList.add(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
                    return true;
                } else {
                    if (compareDelete(appSvcSectionLeaderList, oldAppSvcSectionLeaderList)) {
                        autoList.add(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
                    }
                    return compareDelete(appSvcSectionLeaderList, oldAppSvcSectionLeaderList);
                }
            }
        }
        return false;
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

    public static boolean isAddOrReplaceSvcPersonnel(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, List<String> nonAutoList) {
        int compareLength = compareLength(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        if (compareLength == RfcConst.RFC_NULL) {
            return false;
        }
        if (compareLength == RfcConst.RFC_ADD) {
            return true;
        }
        int size = appSvcRelatedInfoDtoList.size();
        for (int i = 0; i < size; i++) {
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(i);
            SvcPersonnelDto svcPersonnelDto = appSvcRelatedInfoDto.getSvcPersonnelDto();
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtoList.get(i);
            SvcPersonnelDto oldSvcPersonnelDto = oldAppSvcRelatedInfoDto.getSvcPersonnelDto();
            if (compareSvcPersonnelDto(svcPersonnelDto, oldSvcPersonnelDto, nonAutoList)) {
                return true;
            }
        }
        return false;
    }

    public static boolean compareSvcPersonnelDto(SvcPersonnelDto svcPersonnelDto, SvcPersonnelDto oldSvcPersonnelDto,
            List<String> nonAutoList) {
        if (StringUtil.isEmpty(svcPersonnelDto) && !StringUtil.isEmpty(oldSvcPersonnelDto) || Objects.equals(svcPersonnelDto,
                oldSvcPersonnelDto)) {
            return false;
        }
        if (!StringUtil.isEmpty(svcPersonnelDto) && !StringUtil.isEmpty(oldSvcPersonnelDto)) {
            List<AppSvcPersonnelDto> arPractitionerList = svcPersonnelDto.getArPractitionerList();
            List<AppSvcPersonnelDto> embryologistList = svcPersonnelDto.getEmbryologistList();
            List<AppSvcPersonnelDto> nurseList = svcPersonnelDto.getNurseList();
            List<AppSvcPersonnelDto> normalList = svcPersonnelDto.getNormalList();
            List<AppSvcPersonnelDto> specialList = svcPersonnelDto.getSpecialList();
            int arCount = compareLength(arPractitionerList, oldSvcPersonnelDto.getArPractitionerList());
            int emCount = compareLength(embryologistList, oldSvcPersonnelDto.getEmbryologistList());
            int nurCount = compareLength(nurseList, oldSvcPersonnelDto.getNurseList());
            int norCount = compareLength(normalList, oldSvcPersonnelDto.getNormalList());
            int speCount = compareLength(specialList, oldSvcPersonnelDto.getSpecialList());
            boolean ar = combinationAppSvcPersonnelDto(arPractitionerList, oldSvcPersonnelDto.getArPractitionerList(), true);
            boolean em = combinationAppSvcPersonnelDto(embryologistList, oldSvcPersonnelDto.getEmbryologistList(), true);
            boolean nor = combinationAppSvcPersonnelDto(normalList, oldSvcPersonnelDto.getNormalList(), true);
            boolean spe = combinationAppSvcPersonnelDto(specialList, oldSvcPersonnelDto.getSpecialList(), true);
            boolean nur = combinationAppSvcPersonnelDto(nurseList, oldSvcPersonnelDto.getNurseList(), true);

            if (ar || RfcConst.RFC_ADD == arCount) {
                nonAutoList.add(ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER);
            }
            if (nur || RfcConst.RFC_ADD == nurCount) {
                nonAutoList.add(ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES);
            }
            if (em || RfcConst.RFC_ADD == emCount) {
                nonAutoList.add(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST);
            }
            if (nor || RfcConst.RFC_ADD == norCount) {
                nonAutoList.add(ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS);
            }
            if (spe || RfcConst.RFC_ADD == speCount) {
                nonAutoList.add(ApplicationConsts.SERVICE_PERSONNEL_TYPE_SPECIALS);
            }
            boolean flag = RfcConst.RFC_ADD == arCount || RfcConst.RFC_ADD == emCount || RfcConst.RFC_ADD == nurCount || RfcConst.RFC_ADD == norCount || RfcConst.RFC_ADD == speCount;
            if (flag) {
                return true;
            }
            return ar || em || nur || nor || spe;
        }
        return false;
    }

    public static boolean isRemoveSvcPersonnel(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, List<String> autoList) {
        int compareLength = compareLength(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        if (compareLength == RfcConst.RFC_REMOVE) {
            return true;
        }
        if (compareLength == RfcConst.RFC_NULL) {
            return false;
        }
        if (compareLength == RfcConst.RFC_SAME) {
            int size = appSvcRelatedInfoDtoList.size();
            for (int i = 0; i < size; i++) {
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(i);
                SvcPersonnelDto svcPersonnelDto = appSvcRelatedInfoDto.getSvcPersonnelDto();
                AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtoList.get(i);
                SvcPersonnelDto oldSvcPersonnelDto = oldAppSvcRelatedInfoDto.getSvcPersonnelDto();
                if (isRemoveSvcPersonnelDto(svcPersonnelDto, oldSvcPersonnelDto, autoList)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isRemoveSvcPersonnelDto(SvcPersonnelDto svcPersonnelDto, SvcPersonnelDto oldSvcPersonnelDto,
            List<String> autoList) {
        if (!StringUtil.isEmpty(svcPersonnelDto) && StringUtil.isEmpty(oldSvcPersonnelDto) || Objects.equals(svcPersonnelDto,
                oldSvcPersonnelDto)) {
            return false;
        }
        if (StringUtil.isEmpty(svcPersonnelDto) && !StringUtil.isEmpty(oldSvcPersonnelDto)) {
            return true;
        }
        if (!StringUtil.isEmpty(svcPersonnelDto) && !StringUtil.isEmpty(oldSvcPersonnelDto)) {
            List<AppSvcPersonnelDto> arPractitionerList = svcPersonnelDto.getArPractitionerList();
            List<AppSvcPersonnelDto> embryologistList = svcPersonnelDto.getEmbryologistList();
            List<AppSvcPersonnelDto> nurseList = svcPersonnelDto.getNurseList();
            List<AppSvcPersonnelDto> normalList = svcPersonnelDto.getNormalList();
            List<AppSvcPersonnelDto> specialList = svcPersonnelDto.getSpecialList();
            boolean ar = compareDelete(arPractitionerList, oldSvcPersonnelDto.getArPractitionerList());
            boolean em = compareDelete(embryologistList, oldSvcPersonnelDto.getEmbryologistList());
            boolean nur = compareDelete(nurseList, oldSvcPersonnelDto.getNurseList());
            boolean nor = compareDelete(normalList, oldSvcPersonnelDto.getNormalList());
            boolean spe = compareDelete(specialList, oldSvcPersonnelDto.getSpecialList());
            if (ar) {
                autoList.add(ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER);
            }
            if (nur) {
                autoList.add(ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES);
            }
            if (em) {
                autoList.add(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST);
            }
            if (nor) {
                autoList.add(ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS);
            }
            if (spe) {
                autoList.add(ApplicationConsts.SERVICE_PERSONNEL_TYPE_SPECIALS);
            }
            boolean flag = ar || em || nur || nor || spe;
            return flag;
        }
        return false;
    }

    public static boolean isChangeDetailSvcPersonnel(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, List<String> autoList) {
        if (appSvcRelatedInfoDtoList == null || oldAppSvcRelatedInfoDtoList == null) {
            return false;
        }
        int size = appSvcRelatedInfoDtoList.size();
        if (size != oldAppSvcRelatedInfoDtoList.size()) {
            return false;
        }
        List<SvcPersonnelDto> svcPersonnelDtoList = IaisCommonUtils.genNewArrayList();
        appSvcRelatedInfoDtoList.forEach(e -> svcPersonnelDtoList.add(e.getSvcPersonnelDto()));
        List<SvcPersonnelDto> oldSvcPersonnelDtoList = IaisCommonUtils.genNewArrayList();
        oldAppSvcRelatedInfoDtoList.forEach(e -> oldSvcPersonnelDtoList.add(e.getSvcPersonnelDto()));
        return isChangeSvcPersonnelDto(svcPersonnelDtoList, oldSvcPersonnelDtoList, autoList);
    }

    public static boolean isChangeSvcPersonnelDto(List<SvcPersonnelDto> svcPersonnelDtoList,
            List<SvcPersonnelDto> oldSvcPersonnelDtoList, List<String> autoList) {
        if (IaisCommonUtils.isEmpty(svcPersonnelDtoList) && IaisCommonUtils.isEmpty(oldSvcPersonnelDtoList)) {
            int size = svcPersonnelDtoList.size();
            for (int i = 0; i < size; i++) {
                SvcPersonnelDto oldSvcPersonnelDto = new SvcPersonnelDto();
                SvcPersonnelDto svcPersonnelDto = svcPersonnelDtoList.get(i);
                if (!StringUtil.isEmpty(oldSvcPersonnelDtoList.get(i))) {
                    oldSvcPersonnelDto = oldSvcPersonnelDtoList.get(i);
                }
                if (StringUtil.isEmpty(svcPersonnelDto)) {
                    svcPersonnelDto = new SvcPersonnelDto();
                }
                List<AppSvcPersonnelDto> arPractitionerList = svcPersonnelDto.getArPractitionerList();
                List<AppSvcPersonnelDto> nurseList = svcPersonnelDto.getNurseList();
                List<AppSvcPersonnelDto> embryologistList = svcPersonnelDto.getEmbryologistList();
                List<AppSvcPersonnelDto> normalList = svcPersonnelDto.getNormalList();
                List<AppSvcPersonnelDto> specialList = svcPersonnelDto.getSpecialList();
                boolean isAr = combinationAppSvcPersonnelDto(arPractitionerList, oldSvcPersonnelDto.getArPractitionerList(), false);
                boolean isNur = combinationAppSvcPersonnelDto(nurseList, oldSvcPersonnelDto.getNurseList(), false);
                boolean isEm = combinationAppSvcPersonnelDto(embryologistList, oldSvcPersonnelDto.getEmbryologistList(), false);
                boolean isNor = combinationAppSvcPersonnelDto(normalList, oldSvcPersonnelDto.getNormalList(), false);
                boolean isSpe = combinationAppSvcPersonnelDto(specialList, oldSvcPersonnelDto.getSpecialList(), false);
                if (isAr) {
                    autoList.add(ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER);
                }
                if (isNur) {
                    autoList.add(ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES);
                }
                if (isEm) {
                    autoList.add(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST);
                }
                if (isNor) {
                    autoList.add(ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS);
                }
                if (isSpe) {
                    autoList.add(ApplicationConsts.SERVICE_PERSONNEL_TYPE_SPECIALS);
                }
                if (isAr || isNur || isEm || isNor || isSpe) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAddOrReplaceCd(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, boolean flag, List<String> nonAutoList) {
        int compareLength = compareLength(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        if (compareLength == RfcConst.RFC_NULL) {
            return false;
        }
        if (compareLength == RfcConst.RFC_ADD) {
            return true;
        }
        int size = appSvcRelatedInfoDtoList.size();
        for (int i = 0; i < size; i++) {
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(i);
            List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtoList.get(i);
            List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList = oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList();
            if (RfcConst.RFC_ADD == compareLength(appSvcCgoDtoList, oldAppSvcCgoDtoList)) {
                nonAutoList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
                return true;
            }
            if (combinationAppSvcCd(appSvcCgoDtoList, oldAppSvcCgoDtoList, flag)) {
                nonAutoList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
                return true;
            }
        }
        return false;
    }

    public static boolean combinationAppSvcCd(List<AppSvcPrincipalOfficersDto> newList, List<AppSvcPrincipalOfficersDto> oldList,
            Boolean flag) {
        List<String> newType = new ArrayList<>();
        List<String> newTypes = new ArrayList<>();
        List<AppSvcPrincipalOfficersDto> source = IaisCommonUtils.genNewArrayList();
        List<AppSvcPrincipalOfficersDto> target = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isNotEmpty(newList) && IaisCommonUtils.isNotEmpty(oldList)) {
            newList.forEach(e -> newType.add(e.getIndexNo()));
            oldList.forEach(e -> newTypes.add(e.getIndexNo()));
            if (newType.size() == newTypes.size() && !Objects.equals(newType, newTypes) && flag) {
                return true;
            }
            newTypes.clear();
            for (String type : newType) {
                List<AppSvcPrincipalOfficersDto> collect = oldList.stream().filter(
                        dto -> Objects.equals(type, dto.getIndexNo())).collect(Collectors.toList());
                if (IaisCommonUtils.isNotEmpty(collect)) {
                    AppSvcPrincipalOfficersDto dto = collect.get(0);
                    source.add(dto);
                }
            }
            source.forEach(e -> newTypes.add(e.getIndexNo()));
            for (String type : newTypes) {
                List<AppSvcPrincipalOfficersDto> collect = newList.stream().filter(
                        dto -> Objects.equals(type, dto.getIndexNo())).collect(Collectors.toList());
                if (IaisCommonUtils.isNotEmpty(collect)) {
                    AppSvcPrincipalOfficersDto dto = collect.get(0);
                    target.add(dto);
                }
            }
        }
        if (flag) {
            return isAddOrSame(source, target, PageDataCopyUtil::copyAppSvcCd);
        }
        return !Objects.equals(source, target);
    }

    public static boolean compareDeleteCd(List<AppSvcPrincipalOfficersDto> source, List<AppSvcPrincipalOfficersDto> target) {
        List<String> sourceIndex = IaisCommonUtils.genNewArrayList();
        List<String> targetIndex = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isNotEmpty(source) && IaisCommonUtils.isNotEmpty(target)) {
            source.forEach(e -> sourceIndex.add(e.getIndexNo()));
            target.forEach(e -> targetIndex.add(e.getIndexNo()));
            if (RfcConst.RFC_ADD == compareLength(sourceIndex, targetIndex)) {
                return false;
            }
            return !Objects.equals(sourceIndex, targetIndex);
        }
        return false;
    }

    public static boolean isAddOrReplaceKey(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, boolean flag, List<String> nonAutoList) {
        int compareLength = compareLength(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        if (compareLength == RfcConst.RFC_NULL) {
            return false;
        }
        if (compareLength == RfcConst.RFC_ADD) {
            return true;
        }
        int size = appSvcRelatedInfoDtoList.size();
        for (int i = 0; i < size; i++) {
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(i);
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtoList.get(i);
            List<AppSvcPrincipalOfficersDto> newList = appSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList();
            List<AppSvcPrincipalOfficersDto> oldList = oldAppSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList();
            if (RfcConst.RFC_ADD == compareLength(newList, oldList)) {
                nonAutoList.add(ApplicationConsts.PERSONNEL_PSN_KAH);
                return true;
            }
            if (combinationAppSvcCd(newList, oldList, flag)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRemoveKeyPersonnel(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, List<String> autoList) {
        int compareLength = compareLength(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        if (compareLength == RfcConst.RFC_REMOVE) {
            return true;
        }
        if (compareLength == RfcConst.RFC_SAME) {
            int size = appSvcRelatedInfoDtoList.size();
            for (int i = 0; i < size; i++) {
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(i);
                AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtoList.get(i);
                List<AppSvcPrincipalOfficersDto> newList = appSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList();
                List<AppSvcPrincipalOfficersDto> oldList = oldAppSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList();
                if (compareDeleteCd(newList, oldList)) {
                    autoList.add(ApplicationConsts.PERSONNEL_PSN_KAH);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isChangeDetailKeyPersonnel(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, List<String> autoList) {
        if (appSvcRelatedInfoDtoList == null || oldAppSvcRelatedInfoDtoList == null) {
            return false;
        }
        int size = appSvcRelatedInfoDtoList.size();
        if (size != oldAppSvcRelatedInfoDtoList.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(i);
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtoList.get(i);
            List<AppSvcPrincipalOfficersDto> newList = appSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList();
            if (combinationAppSvcCd(newList, oldAppSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList(), false)) {
                autoList.add(ApplicationConsts.PERSONNEL_PSN_KAH);
                return true;
            }
        }
        return false;
    }

    public static boolean isChangeSupplementaryForm(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, List<String> nonAutoList) {
        if (appSvcRelatedInfoDtoList == null || oldAppSvcRelatedInfoDtoList == null) {
            return false;
        }
        int size = appSvcRelatedInfoDtoList.size();
        if (size != oldAppSvcRelatedInfoDtoList.size()) {
            return true;
        }
        for (int i = 0; i < size; i++) {
            List<AppSvcSuplmFormDto> appSvcSuplmFormList = appSvcRelatedInfoDtoList.get(i).getAppSvcSuplmFormList();
            List<AppSvcSuplmFormDto> oldAppSvcSuplmFormList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcSuplmFormList();
            if (compareSupplementaryForm(appSvcSuplmFormList, oldAppSvcSuplmFormList, nonAutoList)) {
                nonAutoList.add(HcsaConsts.STEP_SUPPLEMENTARY_FORM);
                return true;
            }
        }
        return false;
    }

    public static boolean compareSupplementaryForm(List<AppSvcSuplmFormDto> appSvcSuplmFormList,
            List<AppSvcSuplmFormDto> oldAppSvcSuplmFormList, List<String> nonAutoList) {
        Map<String, String> newMap = IaisCommonUtils.genNewHashMap();
        Map<String, String> oldMap = IaisCommonUtils.genNewHashMap();
        if (appSvcSuplmFormList == null || oldAppSvcSuplmFormList == null) {
            return false;
        }
        int size = appSvcSuplmFormList.size();
        if (size != oldAppSvcSuplmFormList.size()) {
            nonAutoList.add(HcsaConsts.STEP_SUPPLEMENTARY_FORM);
            return true;
        }
        for (int i = 0; i < size; i++) {
            AppSvcSuplmFormDto appSvcSuplmFormDto = appSvcSuplmFormList.get(i);
            AppSvcSuplmFormDto oldAppSvcSuplmFormDto = oldAppSvcSuplmFormList.get(i);
            List<AppSvcSuplmGroupDto> appSvcSuplmGroupDtoList = appSvcSuplmFormDto.getAppSvcSuplmGroupDtoList();
            List<AppSvcSuplmGroupDto> oldAppSvcSuplmGroupDtoList = oldAppSvcSuplmFormDto.getAppSvcSuplmGroupDtoList();
            if (appSvcSuplmGroupDtoList == null || oldAppSvcSuplmGroupDtoList == null) {
                return false;
            }
            int sizes = appSvcSuplmGroupDtoList.size();
            if (sizes != oldAppSvcSuplmGroupDtoList.size()) {
                nonAutoList.add(HcsaConsts.STEP_SUPPLEMENTARY_FORM);
                return true;
            }
            for (int j = 0; j < sizes; j++) {
                AppSvcSuplmGroupDto appSvcSuplmGroupDto = appSvcSuplmGroupDtoList.get(j);
                List<AppSvcSuplmItemDto> appSvcSuplmItemDtoList = appSvcSuplmGroupDto.getAppSvcSuplmItemDtoList();
                AppSvcSuplmGroupDto oldAppSvcSuplmGroupDto = oldAppSvcSuplmGroupDtoList.get(j);
                List<AppSvcSuplmItemDto> oldAppSvcSuplmItemDtoList = oldAppSvcSuplmGroupDto.getAppSvcSuplmItemDtoList();
                if (appSvcSuplmItemDtoList == null || oldAppSvcSuplmItemDtoList == null) {
                    return false;
                }
                int count = appSvcSuplmItemDtoList.size();
                if (count != oldAppSvcSuplmItemDtoList.size()) {
                    nonAutoList.add(HcsaConsts.STEP_SUPPLEMENTARY_FORM);
                    return true;
                }
                for (int m = 0; m < count; m++) {
                    String newKey = "";
                    String oldKey = "";
                    AppSvcSuplmItemDto appSvcSuplmItemDto = appSvcSuplmItemDtoList.get(m);
                    SuppleFormItemConfigDto itemConfigDto = appSvcSuplmItemDto.getItemConfigDto();
                    AppSvcSuplmItemDto oldAppSvcSuplmItemDto = oldAppSvcSuplmItemDtoList.get(m);
                    SuppleFormItemConfigDto oldItemConfigDto = oldAppSvcSuplmItemDto.getItemConfigDto();
                    if (!StringUtil.isEmpty(itemConfigDto)) {
                        String displayInfo = itemConfigDto.getDisplayInfo();
                        String id = itemConfigDto.getId();
                        newKey = displayInfo + id;
                    }
                    if (!StringUtil.isEmpty(oldItemConfigDto)) {
                        String displayInfo = oldItemConfigDto.getDisplayInfo();
                        String id = oldItemConfigDto.getId();
                        oldKey = displayInfo + id;
                    }
                    newMap.put(newKey, appSvcSuplmItemDto.getInputValue());
                    oldMap.put(oldKey, oldAppSvcSuplmItemDto.getInputValue());
                    if (!newMap.equals(oldMap)) {
                        nonAutoList.add(HcsaConsts.STEP_SUPPLEMENTARY_FORM);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean compareDelete(List<AppSvcPersonnelDto> source, List<AppSvcPersonnelDto> target) {
        List<String> sourceIndex = IaisCommonUtils.genNewArrayList();
        List<String> targetIndex = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isNotEmpty(source) && IaisCommonUtils.isNotEmpty(target)) {
            source.forEach(e -> sourceIndex.add(e.getIndexNo()));
            target.forEach(e -> targetIndex.add(e.getIndexNo()));
            if (RfcConst.RFC_ADD == compareLength(sourceIndex, targetIndex)) {
                return false;
            }
            if (!Objects.equals(sourceIndex, targetIndex)) {
                return true;
            }
        }
        return false;
    }

    public static boolean combinationAppSvcPersonnelDto(List<AppSvcPersonnelDto> newList, List<AppSvcPersonnelDto> oldList,
            boolean flag) {
        List<String> indexNo = new ArrayList<>();
        List<String> indexNos = new ArrayList<>();
        List<AppSvcPersonnelDto> source = IaisCommonUtils.genNewArrayList();
        List<AppSvcPersonnelDto> target = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isNotEmpty(newList) && IaisCommonUtils.isNotEmpty(oldList)) {
            newList.forEach(e -> indexNo.add(e.getIndexNo()));
            oldList.forEach(e -> indexNos.add(e.getIndexNo()));
            if (indexNo.size() == indexNos.size() && !Objects.equals(indexNo, indexNos) && flag) {
                return true;
            }
            indexNos.clear();
            for (String index : indexNo) {
                List<AppSvcPersonnelDto> collect = oldList.stream().filter(dto -> Objects.equals(index, dto.getIndexNo())).collect(
                        Collectors.toList());
                if (IaisCommonUtils.isNotEmpty(collect)) {
                    AppSvcPersonnelDto appSvcPersonnelDto = collect.get(0);
                    source.add(appSvcPersonnelDto);
                }
            }
            source.forEach(e -> indexNos.add(e.getIndexNo()));
            for (String index : indexNos) {
                List<AppSvcPersonnelDto> collect = newList.stream().filter(dto -> Objects.equals(index, dto.getIndexNo())).collect(
                        Collectors.toList());
                if (IaisCommonUtils.isNotEmpty(collect)) {
                    AppSvcPersonnelDto appSvcPersonnelDto = collect.get(0);
                    target.add(appSvcPersonnelDto);
                }
            }
        }
        if (flag) {
            return isAddOrSame(source, target, PageDataCopyUtil::copySvcPersonnel);
        }
        return !Objects.equals(source, target);
    }

    public static <T> int compareLength(List<T> newList, List<T> oldList) {       //add  true
        if (IaisCommonUtils.isEmpty(newList) && IaisCommonUtils.isEmpty(oldList)) {
            return RfcConst.RFC_NULL;
        }
        int newSize = Optional.ofNullable(newList).map(List::size).orElse(-1);        //  -1
        int oldSize = Optional.ofNullable(oldList).map(List::size).orElse(-1);       //  1
        if (newSize != oldSize) {
            if (newSize > oldSize) {
                return RfcConst.RFC_ADD;
            } else {
                return RfcConst.RFC_REMOVE;
            }
        }
        return RfcConst.RFC_SAME;

    }

    public static <T, R> boolean isAddOrSame(List<T> source, List<T> target, Function<List<T>, List<R>> newList) {
        int compareLength = compareLength(source, target);
        if (compareLength == RfcConst.RFC_NULL) {
            return false;
        }
        if (compareLength == RfcConst.RFC_ADD) {
            return true;
        }
        List<R> newSrc = newList.apply(source);
        List<R> newTar = newList.apply(target);
        return !newSrc.equals(newTar);
    }

    private static boolean ischangeSpecialServiceInformation(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, List<String> nonAutoList) {
        if (appSvcRelatedInfoDtoList == null || oldAppSvcRelatedInfoDtoList == null) {
            return false;
        }
        if (appSvcRelatedInfoDtoList.size() != oldAppSvcRelatedInfoDtoList.size()) {
            return false;
        }
        List<AppSvcSpecialServiceInfoDto> appSvcSpecialServiceInfoDtoList = IaisCommonUtils.genNewArrayList();
        appSvcRelatedInfoDtoList.forEach((item) -> {
            if (IaisCommonUtils.isNotEmpty(item.getAppSvcSpecialServiceInfoList())) {
                appSvcSpecialServiceInfoDtoList.addAll(item.getAppSvcSpecialServiceInfoList());
            }
        });
        List<AppSvcSpecialServiceInfoDto> oldAppSvcSpecialServiceInfoDtoList = IaisCommonUtils.genNewArrayList();
        oldAppSvcRelatedInfoDtoList.forEach((item) -> {
            if (IaisCommonUtils.isNotEmpty(item.getAppSvcSpecialServiceInfoList())) {
                oldAppSvcSpecialServiceInfoDtoList.addAll(item.getAppSvcSpecialServiceInfoList());
            }
        });
        boolean result = false;
        List<AppSvcPrincipalOfficersDto> keyPersonnelList = IaisCommonUtils.genNewArrayList();
        appSvcSpecialServiceInfoDtoList.forEach((item) -> keyPersonnelList.addAll(item.getAppSvcCgoDtoList()));
        List<AppSvcPrincipalOfficersDto> oldKeyPersonnelList = IaisCommonUtils.genNewArrayList();
        oldAppSvcSpecialServiceInfoDtoList.forEach((item) -> keyPersonnelList.addAll(item.getAppSvcCgoDtoList()));
        boolean changeKeyPersonnel = false;
        if (IaisCommonUtils.isNotEmpty(keyPersonnelList) && IaisCommonUtils.isNotEmpty(oldKeyPersonnelList)) {
            changeKeyPersonnel = isChangeKeyPersonnel(keyPersonnelList, oldKeyPersonnelList, false);
        }
        List<AppSvcPersonnelDto> personnelList = IaisCommonUtils.genNewArrayList();
        appSvcSpecialServiceInfoDtoList.forEach((item) -> personnelList.addAll(item.getSpecialPersonnelDtoList()));
        List<AppSvcPersonnelDto> oldPersonnelList = IaisCommonUtils.genNewArrayList();
        oldAppSvcSpecialServiceInfoDtoList.forEach((item) -> oldPersonnelList.addAll(item.getSpecialPersonnelDtoList()));
        boolean changePersonal = false;
        if (IaisCommonUtils.isNotEmpty(personnelList) && IaisCommonUtils.isNotEmpty(oldPersonnelList)) {
            changePersonal = !isChangeServicePersonnels(personnelList, oldPersonnelList);
        }
        List<AppSvcSuplmFormDto> appSvcSuplmFormList = IaisCommonUtils.genNewArrayList();
        appSvcSpecialServiceInfoDtoList.forEach((item) -> appSvcSuplmFormList.addAll(item.getAppSvcSuplmFormDtoList()));
        List<AppSvcSuplmFormDto> oldAppSvcSuplmFormList = IaisCommonUtils.genNewArrayList();
        oldAppSvcSpecialServiceInfoDtoList.forEach((item) -> oldAppSvcSuplmFormList.addAll(item.getAppSvcSuplmFormDtoList()));
        boolean changeSupplementaryForm = false;
        if (IaisCommonUtils.isNotEmpty(appSvcSuplmFormList) && IaisCommonUtils.isNotEmpty(oldAppSvcSuplmFormList)) {
            changeSupplementaryForm = compareSupplementaryForm(appSvcSuplmFormList, oldAppSvcSuplmFormList, nonAutoList);
        }
        if (changeKeyPersonnel || changePersonal || changeSupplementaryForm) {
            nonAutoList.add(HcsaConsts.STEP_SPECIAL_SERVICES_FORM);
            return true;
        }
        return result;
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
        if (IaisCommonUtils.isEmpty(specialisedList) && IaisCommonUtils.isEmpty(oldSpecialisedList)) {
            return RfcConst.RFC_UNCHANGED;
        }
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
        int result = RfcConst.RFC_UNCHANGED;
        for (int i = 0; i < size; i++) {
            result |= isChangeSpecialisedFields(specialisedList.get(i), oldSpecialisedList.get(i));
        }
        return result;
    }

    public static int isChangeSpecialisedFields(AppPremSpecialisedDto specialisedDto, AppPremSpecialisedDto oldSpecialisedDto) {
        if (specialisedDto == null || oldSpecialisedDto == null) {
            return RfcConst.RFC_AMENDMENT;
        }
        int result = isChangeAppPremScopeList(specialisedDto.getCheckedAppPremScopeDtoList(),
                oldSpecialisedDto.getCheckedAppPremScopeDtoList());
        if (IaisCommonUtils.isEmpty(specialisedDto.getAppPremSubSvcRelDtoList())
                && IaisCommonUtils.isEmpty(oldSpecialisedDto.getAppPremSubSvcRelDtoList())) {
            return result;
        }
        List<AppPremSubSvcRelDto> appPremSubSvcRelList = specialisedDto.getFlatAppPremSubSvcRelList(dto ->
                StringUtil.isNotEmpty(dto.getActCode()));
        if (StringUtil.isEmpty(appPremSubSvcRelList)) {
            result |= RfcConst.RFC_AMENDMENT;
        } else {
            for (AppPremSubSvcRelDto relDto : appPremSubSvcRelList) {
                if (ApplicationConsts.RECORD_ACTION_CODE_ADD.equals(relDto.getActCode())) {
                    result |= relDto.isAdditionFlow() ? RfcConst.RFC_AMENDMENT : RfcConst.RFC_NOTIFICATION;
                } else if (ApplicationConsts.RECORD_ACTION_CODE_REMOVE.equals(relDto.getActCode())) {
                    result |= relDto.isRemovalFlow() ? RfcConst.RFC_AMENDMENT : RfcConst.RFC_NOTIFICATION;
                }
            }
        }
        return result;
    }

    public static int isChangeAppPremScopeList(List<AppPremScopeDto> appPremScopeDtoList,
            List<AppPremScopeDto> oldAppPremScopeDtoList) {
        if (IaisCommonUtils.isEmpty(appPremScopeDtoList) && IaisCommonUtils.isEmpty(oldAppPremScopeDtoList)) {
            return RfcConst.RFC_UNCHANGED;
        }
        if (IaisCommonUtils.isEmpty(appPremScopeDtoList)) {
            return RfcConst.RFC_NOTIFICATION;
        }
        if (IaisCommonUtils.isEmpty(oldAppPremScopeDtoList)) {
            return RfcConst.RFC_AMENDMENT;
        }
        int scopeResult = isChangedList(appPremScopeDtoList, oldAppPremScopeDtoList, null,
                (obj, list) -> list.stream()
                        .filter(dto -> Objects.equals(dto.getScopeName(), obj.getScopeName())
                                && (obj.isChecked() ? dto.isChecked() : true))
                        .findAny()
                        .orElse(null),
                (dto, oldDto) -> dto.isChecked() == oldDto.isChecked());
        int result = RfcConst.RFC_UNCHANGED;
        if ((scopeResult & RfcConst.STATUS_ADDITION) != 0) {
            result |= RfcConst.RFC_AMENDMENT;
        }
        if ((scopeResult & RfcConst.STATUS_REMOVAL) != 0) {
            result |= RfcConst.RFC_NOTIFICATION;
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

    /*public static boolean isChangeSvcInfoAutoFields(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, AppEditSelectDto appEditSelectDto) {
        List<String> changeList = IaisCommonUtils.genNewArrayList();
        List<AppSvcRelatedInfoDto> n = (List<AppSvcRelatedInfoDto>) CopyUtil.copyMutableObjectList(appSvcRelatedInfoDtoList);
        List<AppSvcRelatedInfoDto> o = (List<AppSvcRelatedInfoDto>) CopyUtil.copyMutableObjectList(oldAppSvcRelatedInfoDtoList);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = n.get(0);
        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = o.get(0);
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
    }*/

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

    private static boolean isChangeSvcDocs(AppSvcRelatedInfoDto appSvcRelatedInfoDto, AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto) {
        return isChangeSvcDocs(appSvcRelatedInfoDto.getAppSvcDocDtoLit(), oldAppSvcRelatedInfoDto.getAppSvcDocDtoLit());
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
                anyMatch(tar -> Objects.equals(dto.getUnitNo(), tar.getUnitNo())
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
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos, List<String> autoList, List<String> nonAutoList) {
        if (IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos) && IaisCommonUtils.isEmpty(oldAppSvcRelatedInfoDtos)) {
            return RfcConst.RFC_UNCHANGED;
        }
        if (IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos) || IaisCommonUtils.isEmpty(oldAppSvcRelatedInfoDtos)) {
            return RfcConst.RFC_AMENDMENT;
        }
        if (appSvcRelatedInfoDtos.size() != oldAppSvcRelatedInfoDtos.size()) {
            return RfcConst.RFC_AMENDMENT;
        }
        int result = RfcConst.RFC_UNCHANGED;
        for (int i = 0, len = appSvcRelatedInfoDtos.size(); i < len; i++) {
            result |= isChangeAppSvcVehicleDto(appSvcRelatedInfoDtos.get(i).getAppSvcVehicleDtoList(),
                    oldAppSvcRelatedInfoDtos.get(i).getAppSvcVehicleDtoList(), autoList, nonAutoList);
        }
        return result;
    }

    public static int isChangeAppSvcVehicleDto(List<AppSvcVehicleDto> appSvcVehicleDtoList,
            List<AppSvcVehicleDto> oldAppSvcVehicleDtoList, List<String> autoList, List<String> nonAutoList) {
        if (IaisCommonUtils.isEmpty(appSvcVehicleDtoList) && IaisCommonUtils.isEmpty(oldAppSvcVehicleDtoList)) {
            return RfcConst.RFC_UNCHANGED;
        }
        if (IaisCommonUtils.isEmpty(appSvcVehicleDtoList)) {
            return RfcConst.RFC_NOTIFICATION;
        }
        if (IaisCommonUtils.isEmpty(oldAppSvcVehicleDtoList)) {
            return RfcConst.RFC_AMENDMENT;
        }
        int result = RfcConst.RFC_UNCHANGED;
        // add
        boolean noneMatch = appSvcVehicleDtoList.stream()
                .allMatch(dto -> oldAppSvcVehicleDtoList.stream()
                        .anyMatch(oldDto -> Objects.equals(dto.getEngineNum(), oldDto.getEngineNum())
                                && Objects.equals(dto.getChassisNum(), oldDto.getChassisNum())));
        if (!noneMatch) {
            nonAutoList.add(HcsaConsts.STEP_VEHICLES);
            result |= RfcConst.RFC_AMENDMENT;
        }
        // removal
        noneMatch = oldAppSvcVehicleDtoList.stream()
                .allMatch(dto -> appSvcVehicleDtoList.stream()
                        .anyMatch(oldDto -> Objects.equals(dto.getEngineNum(), oldDto.getEngineNum())
                                && Objects.equals(dto.getChassisNum(), oldDto.getChassisNum())));
        if (!noneMatch) {
            autoList.add(HcsaConsts.STEP_VEHICLES);
            result |= RfcConst.RFC_NOTIFICATION;
        }
        return result;
    }

    public static int isChangeAppSvcBusinessDtos(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, List<String> autoList, List<String> nonAutoList) {
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
        int result = RfcConst.RFC_UNCHANGED;
        boolean changeAppSvcBusinessDto = isChangeAppSvcBusinessDto(appSvcBusinessDtoList, oldAppSvcBusinessDtoList);
        if (changeAppSvcBusinessDto) {
            nonAutoList.add(HcsaConsts.STEP_BUSINESS_NAME);
            result |= RfcConst.RFC_AMENDMENT;
        }
        boolean changeAppSvcBusinessDtoOtherInfo = isChangeAppSvcBusinessDtoOtherInfo(appSvcBusinessDtoList,
                oldAppSvcBusinessDtoList);
        if (changeAppSvcBusinessDtoOtherInfo) {
            autoList.add(HcsaConsts.STEP_BUSINESS_NAME);
            result |= RfcConst.RFC_NOTIFICATION;
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
        boolean isChange = false;
        List<String> appSvcBusinessContactList = IaisCommonUtils.genNewArrayList();
        appSvcBusinessDtoList.forEach((v) -> appSvcBusinessContactList.add(v.getContactNo()));
        List<String> oldAppSvcBusinessContactList = IaisCommonUtils.genNewArrayList();
        oldAppSvcBusinessDtoList.forEach((v) -> oldAppSvcBusinessContactList.add(v.getContactNo()));
        if (!appSvcBusinessContactList.equals(oldAppSvcBusinessContactList)) {
            isChange = true;
        }
        List<String> appSvcBusinessEmailList = IaisCommonUtils.genNewArrayList();
        appSvcBusinessDtoList.forEach((v) -> appSvcBusinessEmailList.add(v.getEmailAddr()));
        List<String> oldAppSvcBusinessEmailList = IaisCommonUtils.genNewArrayList();
        oldAppSvcBusinessDtoList.forEach((v) -> oldAppSvcBusinessEmailList.add(v.getEmailAddr()));
        if (!appSvcBusinessEmailList.equals(oldAppSvcBusinessEmailList)) {
            isChange = true;
        }
        List<String> appSvcBusinessWebSite = IaisCommonUtils.genNewArrayList();
        appSvcBusinessDtoList.forEach((v) -> {
            if (StringUtil.isNotEmpty(v.getCorporateWebsite())) {
                appSvcBusinessWebSite.add(v.getCorporateWebsite());
            }
        });
        List<String> oldAppSvcBusinessWebSite = IaisCommonUtils.genNewArrayList();
        oldAppSvcBusinessDtoList.forEach((v) -> {
            if (StringUtil.isNotEmpty(v.getCorporateWebsite())) {
                oldAppSvcBusinessWebSite.add(v.getCorporateWebsite());
            }
        });
        if (!appSvcBusinessWebSite.equals(oldAppSvcBusinessWebSite)) {
            isChange = true;
        }
        List<OperationHoursReloadDto> appSvcBusinessOperationHoursList = IaisCommonUtils.genNewArrayList();
        appSvcBusinessDtoList.forEach((v) -> {
            if (IaisCommonUtils.isNotEmpty(v.getWeeklyDtoList())) {
                appSvcBusinessOperationHoursList.addAll(v.getWeeklyDtoList());
            }
            if (IaisCommonUtils.isNotEmpty(v.getPhDtoList())) {
                appSvcBusinessOperationHoursList.addAll(v.getPhDtoList());
            }
        });
        List<OperationHoursReloadDto> n = PageDataCopyUtil.copyOperationHoursReloadDto(appSvcBusinessOperationHoursList);
        List<OperationHoursReloadDto> oldAppSvcBusinessOperationHoursList = IaisCommonUtils.genNewArrayList();
        oldAppSvcBusinessDtoList.forEach((v) -> {
            if (IaisCommonUtils.isNotEmpty(v.getWeeklyDtoList())) {
                oldAppSvcBusinessOperationHoursList.addAll(v.getWeeklyDtoList());
            }
            if (IaisCommonUtils.isNotEmpty(v.getPhDtoList())) {
                oldAppSvcBusinessOperationHoursList.addAll(v.getPhDtoList());
            }
        });
        List<OperationHoursReloadDto> o = PageDataCopyUtil.copyOperationHoursReloadDto(oldAppSvcBusinessOperationHoursList);
        if (!compareOperationHoursReloadDto(n, o)) {
            isChange = true;
        }
        List<AppPremEventPeriodDto> event = IaisCommonUtils.genNewArrayList();
        appSvcBusinessDtoList.forEach((v) -> {
            if (IaisCommonUtils.isNotEmpty(v.getEventDtoList())) {
                event.addAll(v.getEventDtoList());
            }
        });
        List<AppPremEventPeriodDto> copyEvent = PageDataCopyUtil.copyEvent(event);
        List<AppPremEventPeriodDto> oldevent = IaisCommonUtils.genNewArrayList();
        oldAppSvcBusinessDtoList.forEach((v) -> {
            if (IaisCommonUtils.isNotEmpty(v.getEventDtoList())) {
                oldevent.addAll(v.getEventDtoList());
            }
        });
        List<AppPremEventPeriodDto> copyOldEvent = PageDataCopyUtil.copyEvent(oldevent);
        if (!copyEvent.equals(copyOldEvent)) {
            isChange = true;
        }
        return isChange;

    }

    public static boolean compareOperationHoursReloadDto(List<OperationHoursReloadDto> n, List<OperationHoursReloadDto> o) {
        if (n.size() != o.size()) {
            return false;
        }
        for (int i = 0; i < n.size(); i++) {
            OperationHoursReloadDto operationHoursReloadDto = n.get(i);
            OperationHoursReloadDto oldOperationHoursReloadDto = o.get(i);

            List<String> selectValList = operationHoursReloadDto.getSelectValList();
            List<String> oldSelectValList = oldOperationHoursReloadDto.getSelectValList();
            if (IaisCommonUtils.isEmpty(selectValList)) {
                if (IaisCommonUtils.isNotEmpty(oldSelectValList)) {
                    return false;
                }
            } else if (IaisCommonUtils.isEmpty(oldSelectValList)) {
                if (IaisCommonUtils.isNotEmpty(selectValList)) {
                    return false;
                }
            } else {
                if (selectValList.size() != oldSelectValList.size()) {
                    return false;
                }
                if (!(selectValList.containsAll(oldSelectValList) && oldSelectValList.containsAll(selectValList))) {
                    return false;
                }
            }
            if (operationHoursReloadDto.getStartFrom() == null) {
                if (oldOperationHoursReloadDto.getStartFrom() != null) {
                    return false;
                }
            } else if (!operationHoursReloadDto.getStartFrom().equals(oldOperationHoursReloadDto.getStartFrom())) {
                return false;
            }
            if (operationHoursReloadDto.getEndTo() == null) {
                if (oldOperationHoursReloadDto.getEndTo() != null) {
                    return false;
                }
            } else if (!operationHoursReloadDto.getEndTo().equals(oldOperationHoursReloadDto.getEndTo())) {
                return false;
            }
            if (operationHoursReloadDto.isSelectAllDay() != oldOperationHoursReloadDto.isSelectAllDay()) {
                return false;
            }
        }
        return true;
    }

    public static int isChangeAppSvcOtherInfoDto(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, List<String> nonAutoList, List<String> autoList) {
        if (IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList) && IaisCommonUtils.isEmpty(oldAppSvcRelatedInfoDtoList)) {
            return RfcConst.RFC_UNCHANGED;
        }
        if (IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList) || IaisCommonUtils.isEmpty(oldAppSvcRelatedInfoDtoList)) {
            return RfcConst.RFC_AMENDMENT;
        }
        if (appSvcRelatedInfoDtoList.size() != oldAppSvcRelatedInfoDtoList.size()) {
            return RfcConst.RFC_AMENDMENT;
        }
        int result = RfcConst.RFC_UNCHANGED;
        List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList = IaisCommonUtils.genNewArrayList();
        appSvcRelatedInfoDtoList.stream()
                .filter(dto -> dto.getAppSvcOtherInfoList() != null)
                .forEach((item) -> appSvcOtherInfoDtoList.addAll(item.getAppSvcOtherInfoList()));
        List<AppSvcOtherInfoDto> oldAppSvcOtherInfoDtoList = IaisCommonUtils.genNewArrayList();
        oldAppSvcRelatedInfoDtoList.stream()
                .filter(dto -> dto.getAppSvcOtherInfoList() != null).forEach(
                (item) -> oldAppSvcOtherInfoDtoList.addAll(item.getAppSvcOtherInfoList()));
        boolean changeOtherInfo = isChangeOtherInfoDto(appSvcOtherInfoDtoList, oldAppSvcOtherInfoDtoList);
        if (changeOtherInfo) {
            nonAutoList.add(HcsaConsts.STEP_OTHER_INFORMATION);
            result |= RfcConst.RFC_AMENDMENT;
        }
        boolean changeOtherInfoTop = isChangeAppSvcOtherInfoTop(appSvcOtherInfoDtoList, oldAppSvcOtherInfoDtoList, nonAutoList);
        if (changeOtherInfoTop) {
            result |= RfcConst.RFC_AMENDMENT;
        }
        boolean changeOtherService = isChangeCheckOtherService(appSvcOtherInfoDtoList, oldAppSvcOtherInfoDtoList);
        if (changeOtherService) {
            autoList.add(HcsaConsts.STEP_OTHER_INFORMATION);
            result |= RfcConst.RFC_NOTIFICATION;
        }
        return result;
    }

    private static boolean isChangeOtherInfoDto(List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList,
            List<AppSvcOtherInfoDto> oldAppSvcOtherInfoDtoList) {
        if (IaisCommonUtils.isEmpty(appSvcOtherInfoDtoList) && IaisCommonUtils.isEmpty(appSvcOtherInfoDtoList)) {
            return false;
        }
        if (IaisCommonUtils.isEmpty(appSvcOtherInfoDtoList) || IaisCommonUtils.isEmpty(appSvcOtherInfoDtoList)) {
            return true;
        }
        return !isSame(appSvcOtherInfoDtoList, oldAppSvcOtherInfoDtoList, PageDataCopyUtil::copyAppSvcOtherInfoList);
    }

    public static boolean isChangeAppSvcOtherInfoTop(List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList,
            List<AppSvcOtherInfoDto> oldAppSvcOtherInfoDtoList, List<String> nonAutoList) {
        if (IaisCommonUtils.isEmpty(appSvcOtherInfoDtoList) && IaisCommonUtils.isEmpty(appSvcOtherInfoDtoList)) {
            return false;
        }
        if (IaisCommonUtils.isEmpty(appSvcOtherInfoDtoList) || IaisCommonUtils.isEmpty(appSvcOtherInfoDtoList)) {
            return true;
        }
        boolean result = false;
        boolean changeOtherInfoTop = isChangeOtherInfoTopList(appSvcOtherInfoDtoList, oldAppSvcOtherInfoDtoList);
        boolean changeOtherInfoPersonPractitioners = isChangeOtherInfoPersonPractitionersList(appSvcOtherInfoDtoList,
                oldAppSvcOtherInfoDtoList, nonAutoList);
        boolean changeOtherInfoPersonAnaesthetists = isChangeOtherInfoPersonAnaesthetistsList(appSvcOtherInfoDtoList,
                oldAppSvcOtherInfoDtoList, nonAutoList);
        boolean changeOtherInfoPersonNurses = isChangeOtherInfoPersonNursesList(appSvcOtherInfoDtoList, oldAppSvcOtherInfoDtoList,
                nonAutoList);
        boolean changeOtherInfoPersonCounsellors = isChangeOtherInfoPersonCounsellorsList(appSvcOtherInfoDtoList,
                oldAppSvcOtherInfoDtoList, nonAutoList);
        boolean changeOtherInfoAbortDrug = isChangeOtherInfoAbortDrugList(appSvcOtherInfoDtoList, oldAppSvcOtherInfoDtoList);
        boolean changeOtherInfoAbortSurgicalProcedure = isChangeOtherInfoAbortSurgicalProcedureList(appSvcOtherInfoDtoList,
                oldAppSvcOtherInfoDtoList);
        boolean changeOtherInfoAbortDrugAndSurgical = isChangeOtherInfoAbortDrugAndSurgicalList(appSvcOtherInfoDtoList,
                oldAppSvcOtherInfoDtoList);
        boolean changeOther = isChangeOther(appSvcOtherInfoDtoList, oldAppSvcOtherInfoDtoList);
        List<AppSvcSuplmFormDto> appSvcSuplmFormList = IaisCommonUtils.genNewArrayList();
        appSvcOtherInfoDtoList.stream()
                .filter(dto -> dto.getAppSvcSuplmFormDto() != null)
                .forEach((item) -> appSvcSuplmFormList.add(item.getAppSvcSuplmFormDto()));
        List<AppSvcSuplmFormDto> oldAppSvcSuplmFormList = IaisCommonUtils.genNewArrayList();
        oldAppSvcOtherInfoDtoList.stream()
                .filter(dto -> dto.getAppSvcSuplmFormDto() != null)
                .forEach((item) -> oldAppSvcSuplmFormList.add(item.getAppSvcSuplmFormDto()));
        boolean changeOtherInfoSupplemForm = compareSupplementaryForm(appSvcSuplmFormList, oldAppSvcSuplmFormList, nonAutoList);
        if (changeOtherInfoTop || changeOtherInfoPersonPractitioners || changeOtherInfoPersonAnaesthetists || changeOtherInfoPersonNurses
                || changeOtherInfoPersonCounsellors || changeOtherInfoAbortDrug || changeOtherInfoAbortSurgicalProcedure || changeOtherInfoAbortDrugAndSurgical
                || changeOther || changeOtherInfoSupplemForm) {
            nonAutoList.add(HcsaConsts.STEP_OTHER_INFORMATION);
            result = true;
        }
        return result;
    }

    private static boolean isChangeOtherInfoTopList(List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList,
            List<AppSvcOtherInfoDto> oldAppSvcOtherInfoDtoList) {
        boolean result = false;
        for (int i = 0; i < appSvcOtherInfoDtoList.size(); i++) {
            if (!result && AppConsts.YES.equals(appSvcOtherInfoDtoList.get(i).getProvideTop())) {
                result = isChangeOtherInfoTopDto(appSvcOtherInfoDtoList.get(i).getAppSvcOtherInfoTopDto(),
                        oldAppSvcOtherInfoDtoList.get(i).getAppSvcOtherInfoTopDto());
            } else {
                break;
            }
        }
        return result;
    }

    private static boolean isChangeOtherInfoTopDto(AppSvcOtherInfoTopDto appSvcOtherInfoTopDto,
            AppSvcOtherInfoTopDto oldAppSvcOtherInfoTopDto) {
        return !isSame(appSvcOtherInfoTopDto, oldAppSvcOtherInfoTopDto, PageDataCopyUtil::copyAppSvcOtherInfoTopDto);
    }

    private static boolean isChangeOtherInfoPersonPractitionersList(List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList,
            List<AppSvcOtherInfoDto> oldAppSvcOtherInfoDtoList, List<String> nonAutoList) {
        if (appSvcOtherInfoDtoList == null || oldAppSvcOtherInfoDtoList == null) {
            return false;
        }
        boolean result = false;
        for (int i = 0; i < appSvcOtherInfoDtoList.size(); i++) {
            if (AppConsts.YES.equals(appSvcOtherInfoDtoList.get(i).getProvideTop())) {
                if (!result) {
                    result = isChangeOtherInfoPersonDto(appSvcOtherInfoDtoList.get(i).getOtherInfoTopPersonPractitionersList(),
                            oldAppSvcOtherInfoDtoList.get(i).getOtherInfoTopPersonPractitionersList()) ? true : false;
                } else {
                    addOtherInfoPersonAutoList(appSvcOtherInfoDtoList.get(i).getOtherInfoTopPersonPractitionersList(), nonAutoList);
                    break;
                }
            }
        }
        return result;
    }

    private static boolean isChangeOtherInfoPersonAnaesthetistsList(List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList,
            List<AppSvcOtherInfoDto> oldAppSvcOtherInfoDtoList, List<String> nonAutoList) {
        boolean result = false;
        for (int i = 0; i < appSvcOtherInfoDtoList.size(); i++) {
            if (AppConsts.YES.equals(appSvcOtherInfoDtoList.get(i).getProvideTop())) {
                if (!result) {
                    result |= isChangeOtherInfoPersonDto(appSvcOtherInfoDtoList.get(i).getOtherInfoTopPersonAnaesthetistsList(),
                            oldAppSvcOtherInfoDtoList.get(i).getOtherInfoTopPersonAnaesthetistsList()) ? true : false;
                } else {
                    addOtherInfoPersonAutoList(appSvcOtherInfoDtoList.get(i).getOtherInfoTopPersonAnaesthetistsList(), nonAutoList);
                    break;
                }
            }
        }
        return result;
    }

    private static boolean isChangeOtherInfoPersonNursesList(List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList,
            List<AppSvcOtherInfoDto> oldAppSvcOtherInfoDtoList, List<String> nonAutoList) {
        boolean result = false;
        for (int i = 0; i < appSvcOtherInfoDtoList.size(); i++) {
            if (AppConsts.YES.equals(appSvcOtherInfoDtoList.get(i).getProvideTop())) {
                if (!result) {
                    result = isChangeOtherInfoPersonDto(appSvcOtherInfoDtoList.get(i).getOtherInfoTopPersonNursesList(),
                            oldAppSvcOtherInfoDtoList.get(i).getOtherInfoTopPersonNursesList()) ? true : false;
                } else {
                    addOtherInfoPersonAutoList(appSvcOtherInfoDtoList.get(i).getOtherInfoTopPersonNursesList(), nonAutoList);
                    break;
                }
            }
        }
        return result;
    }

    private static boolean isChangeOtherInfoPersonCounsellorsList(List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList,
            List<AppSvcOtherInfoDto> oldAppSvcOtherInfoDtoList, List<String> nonAutoList) {
        boolean result = false;
        for (int i = 0; i < appSvcOtherInfoDtoList.size(); i++) {
            if (AppConsts.YES.equals(appSvcOtherInfoDtoList.get(i).getProvideTop())) {
                if (!result) {
                    result = isChangeOtherInfoPersonDto(appSvcOtherInfoDtoList.get(i).getOtherInfoTopPersonCounsellorsList(),
                            oldAppSvcOtherInfoDtoList.get(i).getOtherInfoTopPersonCounsellorsList()) ? true : false;
                } else {
                    addOtherInfoPersonAutoList(appSvcOtherInfoDtoList.get(i).getOtherInfoTopPersonCounsellorsList(), nonAutoList);
                    break;
                }
            }
        }
        return result;
    }

    private static boolean isChangeOtherInfoPersonDto(List<AppSvcOtherInfoTopPersonDto> appSvcOtherInfoTopPersonDtos,
            List<AppSvcOtherInfoTopPersonDto> oldAppSvcOtherInfoTopPersonDtos) {
        List<AppSvcOtherInfoTopPersonDto> appSvcOtherInfoPerson = PageDataCopyUtil.copyAppSvcOtherInfoPerson(
                appSvcOtherInfoTopPersonDtos);
        List<AppSvcOtherInfoTopPersonDto> oldAppSvcOtherInfoPerson = PageDataCopyUtil.copyAppSvcOtherInfoPerson(
                oldAppSvcOtherInfoTopPersonDtos);
        return !appSvcOtherInfoPerson.equals(oldAppSvcOtherInfoPerson);
    }

    private static void addOtherInfoPersonAutoList(List<AppSvcOtherInfoTopPersonDto> appSvcOtherInfoTopPersonDtos,
            List<String> nonAutoList) {
        appSvcOtherInfoTopPersonDtos.stream()
                .forEach((item) -> nonAutoList.add(item.getPsnType()));
    }

    private static boolean isChangeOtherInfoAbortDrugList(List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList,
            List<AppSvcOtherInfoDto> oldAppSvcOtherInfoDtoList) {
        boolean result = false;
        for (int i = 0; i < appSvcOtherInfoDtoList.size(); i++) {
            if (!result && AppConsts.YES.equals(appSvcOtherInfoDtoList.get(i).getProvideTop()) && appSvcOtherInfoDtoList.get(
                    i).getAppSvcOtherInfoTopDto() != null) {
                if (ApplicationConsts.OTHER_INFO_SD.equals(appSvcOtherInfoDtoList.get(i).getAppSvcOtherInfoTopDto().getTopType())
                        || ApplicationConsts.OTHER_INFO_DSP.equals(
                        appSvcOtherInfoDtoList.get(i).getAppSvcOtherInfoTopDto().getTopType())) {
                    result = isChangeOtherInfoAbortDto(appSvcOtherInfoDtoList.get(i).getOtherInfoAbortDrugList(),
                            oldAppSvcOtherInfoDtoList.get(i).getOtherInfoAbortDrugList()) ? true : false;
                }
            } else {
                break;
            }
        }
        return result;
    }

    private static boolean isChangeOtherInfoAbortSurgicalProcedureList(List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList,
            List<AppSvcOtherInfoDto> oldAppSvcOtherInfoDtoList) {
        boolean result = false;
        for (int i = 0; i < appSvcOtherInfoDtoList.size(); i++) {
            if (!result && AppConsts.YES.equals(appSvcOtherInfoDtoList.get(i).getProvideTop()) && appSvcOtherInfoDtoList.get(
                    i).getAppSvcOtherInfoTopDto() != null) {
                if (ApplicationConsts.OTHER_INFO_SSP.equals(appSvcOtherInfoDtoList.get(i).getAppSvcOtherInfoTopDto())
                        || ApplicationConsts.OTHER_INFO_DSP.equals(appSvcOtherInfoDtoList.get(i).getAppSvcOtherInfoTopDto())) {
                    result |= isChangeOtherInfoAbortDto(appSvcOtherInfoDtoList.get(i).getOtherInfoAbortSurgicalProcedureList(),
                            oldAppSvcOtherInfoDtoList.get(i).getOtherInfoAbortSurgicalProcedureList()) ? true : false;
                }
            } else {
                break;
            }
        }
        return result;
    }

    private static boolean isChangeOtherInfoAbortDrugAndSurgicalList(List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList,
            List<AppSvcOtherInfoDto> oldAppSvcOtherInfoDtoList) {
        boolean result = false;
        for (int i = 0; i < appSvcOtherInfoDtoList.size(); i++) {
            if (!result && AppConsts.YES.equals(appSvcOtherInfoDtoList.get(i).getProvideTop()) && appSvcOtherInfoDtoList.get(
                    i).getAppSvcOtherInfoTopDto() != null) {
                if (ApplicationConsts.OTHER_INFO_DSP.equals(appSvcOtherInfoDtoList.get(i).getAppSvcOtherInfoTopDto())) {
                    result |= isChangeOtherInfoAbortDto(appSvcOtherInfoDtoList.get(i).getOtherInfoAbortDrugAndSurgicalList(),
                            oldAppSvcOtherInfoDtoList.get(i).getOtherInfoAbortDrugAndSurgicalList()) ? true : false;
                }
            } else {
                break;
            }
        }
        return result;
    }

    private static boolean isChangeOtherInfoAbortDto(List<AppSvcOtherInfoAbortDto> appSvcOtherInfoAbortDtos,
            List<AppSvcOtherInfoAbortDto> oldAppSvcOtherInfoAbortDtos) {
        return !isSame(appSvcOtherInfoAbortDtos, oldAppSvcOtherInfoAbortDtos, PageDataCopyUtil::copyAppSvcOtherInfoAbortDto);
    }

    private static boolean isChangeOther(List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList,
            List<AppSvcOtherInfoDto> oldAppSvcOtherInfoDtoList) {
        boolean result = isChangeOtherDto(appSvcOtherInfoDtoList, oldAppSvcOtherInfoDtoList);
        return result;
    }

    private static boolean isChangeOtherDto(List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList,
            List<AppSvcOtherInfoDto> oldAppSvcOtherInfoDtoList) {
        return !isSame(appSvcOtherInfoDtoList, oldAppSvcOtherInfoDtoList, PageDataCopyUtil::copyAppSvcOtherInfoPersonList);
    }

    private static boolean isChangeCheckOtherService(List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList,
            List<AppSvcOtherInfoDto> oldAppSvcOtherInfoDtoList) {
        if (appSvcOtherInfoDtoList == null || oldAppSvcOtherInfoDtoList == null) {
            return false;
        }
        boolean result = false;
        List<AppPremSubSvcRelDto> appPremSubSvcRelDtoList = IaisCommonUtils.genNewArrayList();
        appSvcOtherInfoDtoList.stream().filter(dto -> IaisCommonUtils.isNotEmpty(dto.getAppPremSubSvcRelDtoList())).forEach((item) -> {
            appPremSubSvcRelDtoList.addAll(item.getAppPremSubSvcRelDtoList());
        });
        List<AppPremSubSvcRelDto> oldAppPremSubSvcRelDtoList = IaisCommonUtils.genNewArrayList();
        oldAppSvcOtherInfoDtoList.stream().filter(dto -> IaisCommonUtils.isNotEmpty(dto.getAppPremSubSvcRelDtoList())).forEach(
                (item) -> {
                    oldAppPremSubSvcRelDtoList.addAll(item.getAppPremSubSvcRelDtoList());
                });
        if (IaisCommonUtils.isEmpty(appPremSubSvcRelDtoList) && IaisCommonUtils.isEmpty(oldAppPremSubSvcRelDtoList)) {
            return result;
        }
        for (AppSvcOtherInfoDto appSvcOtherInfoDto : appSvcOtherInfoDtoList) {
            if (appSvcOtherInfoDto != null) {
                List<AppPremSubSvcRelDto> appPremSubSvcRelList = appSvcOtherInfoDto.getFlatAppPremSubSvcRelList(
                        dto -> StringUtil.isNotEmpty(dto.getActCode()));
                if (IaisCommonUtils.isNotEmpty(appPremSubSvcRelList)) {
                    for (AppPremSubSvcRelDto relDto : appPremSubSvcRelList) {
                        if (ApplicationConsts.RECORD_ACTION_CODE_ADD.equals(relDto.getActCode())) {
                            result |= relDto.isAdditionFlow();
                        } else if (ApplicationConsts.RECORD_ACTION_CODE_REMOVE.equals(relDto.getActCode())) {
                            result |= relDto.isRemovalFlow();
                        }
                    }
                }
            }
        }
        return result;
    }

    public static boolean isChangeAppSvcOutsouredDto(AppSvcOutsouredDto appSvcOutsouredDto, AppSvcOutsouredDto oldAppSvcOutsouredDto) {
        if (appSvcOutsouredDto == null || oldAppSvcOutsouredDto == null) {
            return false;
        }
        boolean result = false;
        boolean changeOutsourcedClb = isChangeOutsourced(appSvcOutsouredDto.getClinicalLaboratoryList(),
                oldAppSvcOutsouredDto.getClinicalLaboratoryList());
        boolean changeOutsourcedRs = isChangeOutsourced(appSvcOutsouredDto.getRadiologicalServiceList(),
                oldAppSvcOutsouredDto.getRadiologicalServiceList());
        if (changeOutsourcedClb || changeOutsourcedRs) {
            result = true;
        }
        return result;
    }

    private static boolean isChangeOutsourced(List<AppPremGroupOutsourcedDto> appPremGroupOutsourcedDtoList,
            List<AppPremGroupOutsourcedDto> oldAppPremGroupOutsourcedDtoList) {
        return isSame(appPremGroupOutsourcedDtoList, oldAppPremGroupOutsourcedDtoList,
                PageDataCopyUtil::copyAppPremGroupSoutsourcedList);
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

    private static boolean isChangeKeyPersonnel(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, List<String> personnelList) {
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
        for (String psnType : IaisCommonUtils.getKeyPersonnel()) {
            if (isChangeKeyPersonnel(appSvcRelatedInfoDto, oldAppSvcRelatedInfoDto, psnType, true)) {
                isChanged = true;
                personnelList.add(psnType);
            }
        }
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
        int status = isChangedList(newList, oldList, list -> PageDataCopyUtil.copyKeyPersonnel(list, onlyCheckAddRemoval ? 1 : 0),
                (dto, list) -> list.stream()
                        .filter(obj -> Objects.equals(dto.getAssignSelect(), obj.getAssignSelect()))
                        .findAny().orElse(null),
                (dto1, dto2) -> Objects.equals(dto1, dto2));
        return status != RfcConst.STATUS_UNCHANGED;
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

    public static void syncKeyPersonnel(AppSvcRelatedInfoDto sourceReletedInfo, AppSvcRelatedInfoDto targetReletedInfo, String psnType,
            List<String> personnelEditList) {
        if (sourceReletedInfo == null || targetReletedInfo == null) {
            return;
        }
        log.info(StringUtil.changeForLog("Re-set personnel affected by " + psnType));
        List<AppSvcPrincipalOfficersDto> sourceList = ApplicationHelper.getKeyPersonnel(psnType, sourceReletedInfo);
        if (IaisCommonUtils.isEmpty(sourceList)) {
            return;
        }
        for (String tarPsnType : IaisCommonUtils.getKeyPersonnel()) {
            List<AppSvcPrincipalOfficersDto> targetList = ApplicationHelper.getKeyPersonnel(tarPsnType, targetReletedInfo);
            syncKeyPersonnel(sourceList, targetList, psnType, tarPsnType, personnelEditList);
        }
    }

    private static void syncKeyPersonnel(List<AppSvcPrincipalOfficersDto> sourceList, List<AppSvcPrincipalOfficersDto> targetList,
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
        /*if (appSubmissionDto == null) {
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
                if (!StringUtil.isEmpty(baseService)) {
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
                }
            }
            var1.setServiceId(activeHcsaServiceDtoByName.getId());
        }*/
    }

    /*public static void recursingChooseLabUpward(Map<String, HcsaSvcSubtypeOrSubsumedDto> map, String targetSvcScopeId,
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

    }*/

    /**
     * Generate the svc related info with the auto fields changed
     *
     * @param appSubmissionDto
     * @param oldAppSubmissionDto
     * @param autoList:           The changed personnel types.
     *                            Refer to the method - eqServiceChange and personContact
     * @param nonAutoList:        The steps which is changed, it will contains not-auto fields and maybe it contains auto fields.
     *                            Refer to the method - compareNotChangePersonnel
     *                            && rfcChangeModuleEvaluationDto
     * @return The svc related info with the auto fields changed
     */
    public static List<AppSvcRelatedInfoDto> generateDtosForAutoFields(AppSubmissionDto appSubmissionDto,
            AppSubmissionDto oldAppSubmissionDto, List<String> autoList, List<String> nonAutoList) {
        AppSvcRelatedInfoDto oldSvcInfoDto = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        AppSvcRelatedInfoDto currSvcInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        if (currSvcInfoDto == null || oldSvcInfoDto == null) {
            return null;
        }
        if (nonAutoList == null) {
            return appSubmissionDto.getAppSvcRelatedInfoDtoList();
        }
        if (autoList == null) {
            autoList = IaisCommonUtils.genNewArrayList();
        }
        AppSvcRelatedInfoDto newDto = CopyUtil.copyMutableObject(currSvcInfoDto);
        for (String step : nonAutoList) {
            if (HcsaConsts.STEP_BUSINESS_NAME.equals(step)) {
                reSetBusiness(oldSvcInfoDto, newDto, autoList);
            } else if (HcsaConsts.STEP_VEHICLES.equals(step)) {
                reSetVehicle(oldSvcInfoDto, newDto, autoList);
            } else if (ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER.equals(step)) {
                reSetSvcPersonnel(oldSvcInfoDto, newDto, step, autoList);
            } else if (HcsaConsts.STEP_DOCUMENTS.equals(step)) {
                /*List<AppSvcDocDto> oldASvcDocDtoLit = oldSvcInfoDto.getAppSvcDocDtoLit();
                List<AppSvcDocDto> appSvcDocDtoLit = newDto.getAppSvcDocDtoLit();*/
            } else if (HcsaConsts.STEP_PRINCIPAL_OFFICERS.equals(step)) {
                reSetPersonnels(oldSvcInfoDto, newDto, ApplicationConsts.PERSONNEL_PSN_TYPE_PO, autoList);
                reSetPersonnels(oldSvcInfoDto, newDto, ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, autoList);
            } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(step)) {
                reSetPersonnel(oldSvcInfoDto, newDto, ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, autoList);
            } else if (HcsaConsts.STEP_CLINICAL_DIRECTOR.equals(step)) {
                reSetPersonnels(oldSvcInfoDto, newDto, ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, autoList);
            } else if (ApplicationConsts.PERSONNEL_PSN_KAH.equals(step)) {
                reSetPersonnel(oldSvcInfoDto, newDto, ApplicationConsts.PERSONNEL_PSN_KAH, autoList);
            } else if (HcsaConsts.MEDALERT_PERSON.equals(step)) {
                reSetPersonnels(oldSvcInfoDto, newDto, ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, autoList);
            } else if (HcsaConsts.STEP_OUTSOURCED_PROVIDERS.equals(step)) {
                newDto.setAppSvcOutsouredDto(
                        CopyUtil.copyMutableObject(oldSvcInfoDto.getAppSvcOutsouredDto()));
            } else if (HcsaConsts.STEP_OTHER_INFORMATION.equals(step)) {
                reSetOtherInfo(oldSvcInfoDto, newDto);
            }
        }
        List<AppSvcRelatedInfoDto> result = IaisCommonUtils.genNewArrayList(1);
        result.add(newDto);
        return result;
    }

    private static void reSetPersonnel(AppSvcRelatedInfoDto sourceReletedInfo, AppSvcRelatedInfoDto targetReletedInfo, String psnType,
            List<String> changeList) {
        List<AppSvcPrincipalOfficersDto> psnList = (List<AppSvcPrincipalOfficersDto>) CopyUtil.copyMutableObjectList(
                ApplicationHelper.getKeyPersonnel(psnType, sourceReletedInfo));
        List<AppSvcPrincipalOfficersDto> newList = ApplicationHelper.getKeyPersonnel(psnType, targetReletedInfo);
        boolean isChanged = changeList.contains(psnType);
        if (isChanged && psnList != null && newList != null) {
            for (int i = 0, len = psnList.size(); i < len; i++) {
                AppSvcPrincipalOfficersDto psnDto = psnList.get(i);
                int init = RfcConst.RFC_NULL;
                for (AppSvcPrincipalOfficersDto newPsn : newList) {
                    if (Objects.equals(psnDto.getIdNo(), newPsn.getIdNo())) {
                        psnList.set(i, newPsn);
                        init = RfcConst.RFC_UNCHANGED;
                        break;
                    }
                }
                if (init == RfcConst.RFC_NULL) {
                    psnList.remove(i);
                }
            }
        }
        ApplicationHelper.setKeyPersonnel(psnList, psnType, targetReletedInfo);
    }

    private static void reSetSvcPersonnel(AppSvcRelatedInfoDto sourceReletedInfo, AppSvcRelatedInfoDto targetReletedInfo,
            String psnType, List<String> changeList) {
        List<AppSvcPersonnelDto> oldList = getStepPersonnel(sourceReletedInfo, psnType);
        List<AppSvcPersonnelDto> newList = getStepPersonnel(targetReletedInfo, psnType);
        boolean isChanged = changeList.contains(psnType);
        if (isChanged && oldList != null && newList != null) {
            for (int i = 0, len = oldList.size(); i < len; i++) {
                AppSvcPersonnelDto appSvcPersonnelDto = oldList.get(i);
                int init = RfcConst.RFC_NULL;
                for (int j = 0; j < newList.size(); j++) {
                    AppSvcPersonnelDto appSvcDto = newList.get(j);
                    if (Objects.equals(appSvcPersonnelDto.getIndexNo(), appSvcDto.getIndexNo())) {
                        oldList.set(i, appSvcDto);
                        init = RfcConst.RFC_UNCHANGED;
                        break;
                    }
                }
                if (init == RfcConst.RFC_NULL) {
                    oldList.remove(i);
                }
            }
        }
        ApplicationHelper.setSvcPersonnel(oldList, psnType, targetReletedInfo);
    }

    private static List<AppSvcPersonnelDto> getStepPersonnel(AppSvcRelatedInfoDto sourceReletedInfo, String psnType) {
        SvcPersonnelDto svcPersonnelDto = sourceReletedInfo.getSvcPersonnelDto();
        List<AppSvcPersonnelDto> list = IaisCommonUtils.genNewArrayList();
        boolean flag = ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER.equals(psnType);
        if (StringUtil.isEmpty(svcPersonnelDto) && !flag) {
            return list;
        }
        switch (psnType) {
            case ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER:
                list = svcPersonnelDto.getArPractitionerList();
                break;
            case ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES:
                list = svcPersonnelDto.getNurseList();
                break;
            case ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST:
                list = svcPersonnelDto.getEmbryologistList();
                break;
            case ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS:
                list = svcPersonnelDto.getNormalList();
                break;
            case ApplicationConsts.SERVICE_PERSONNEL_TYPE_SPECIALS:
                list = svcPersonnelDto.getSpecialList();
                break;
            case ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER:
                list = sourceReletedInfo.getAppSvcSectionLeaderList();
        }
        return list;
    }

    private static void reSetSupplementaryForm(AppSvcRelatedInfoDto sourceReletedInfo, AppSvcRelatedInfoDto targetReletedInfo,
            String psnType, List<String> changeList) {
        List<AppSvcSuplmFormDto> oldAppSvcSuplmFormList = sourceReletedInfo.getAppSvcSuplmFormList();
        List<AppSvcSuplmFormDto> appSvcSuplmFormList = targetReletedInfo.getAppSvcSuplmFormList();
        boolean isChanged = changeList.contains(psnType);
        if (isChanged && oldAppSvcSuplmFormList != null && appSvcSuplmFormList != null) {
            for (int i = 0, len = oldAppSvcSuplmFormList.size(); i < len; i++) {
                AppSvcSuplmFormDto appSvcSuplmFormDto = oldAppSvcSuplmFormList.get(i);
                for (AppSvcSuplmFormDto formDto : appSvcSuplmFormList) {
                    if (Objects.equals(appSvcSuplmFormDto.getSvcId(), formDto.getSvcId())) {
                        oldAppSvcSuplmFormList.set(i, formDto);
                        break;
                    }
                }
            }
        }
        targetReletedInfo.setAppSvcSuplmFormList(oldAppSvcSuplmFormList);

    }

    private static void reSetPersonnels(AppSvcRelatedInfoDto sourceReletedInfo, AppSvcRelatedInfoDto targetReletedInfo, String psnType,
            List<String> changeList) {
        List<AppSvcPrincipalOfficersDto> psnList = (List<AppSvcPrincipalOfficersDto>) CopyUtil.copyMutableObjectList(
                ApplicationHelper.getKeyPersonnel(psnType, sourceReletedInfo));
        List<AppSvcPrincipalOfficersDto> newList = ApplicationHelper.getKeyPersonnel(psnType, targetReletedInfo);
        boolean isChanged = changeList.contains(psnType);
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
        ApplicationHelper.setKeyPersonnel(psnList, psnType, targetReletedInfo);
    }

    private static void reSetBusiness(AppSvcRelatedInfoDto sourceReletedInfo, AppSvcRelatedInfoDto targetReletedInfo,
            List<String> changeList) {
        List<AppSvcBusinessDto> oldList = (List<AppSvcBusinessDto>) CopyUtil.copyMutableObjectList(
                sourceReletedInfo.getAppSvcBusinessDtoList());
        List<AppSvcBusinessDto> newList = targetReletedInfo.getAppSvcBusinessDtoList();
        boolean isChanged = changeList.contains(HcsaConsts.STEP_BUSINESS_NAME);
        if (isChanged && oldList != null && newList != null) {
            for (int i = 0, len = oldList.size(); i < len; i++) {
                AppSvcBusinessDto oldBusinessDto = oldList.get(i);
                for (AppSvcBusinessDto newBusinessDto : newList) {
                    if (Objects.equals(oldBusinessDto.getPremIndexNo(), newBusinessDto.getPremIndexNo())) {
                        oldBusinessDto.setContactNo(newBusinessDto.getContactNo());
                        oldBusinessDto.setEmailAddr(newBusinessDto.getEmailAddr());
                        oldBusinessDto.setWeeklyDtoList(newBusinessDto.getWeeklyDtoList());
                        oldBusinessDto.setPhDtoList(newBusinessDto.getPhDtoList());
                        oldBusinessDto.setEventDtoList(newBusinessDto.getEventDtoList());
                    }
                }
            }
        }
        targetReletedInfo.setAppSvcBusinessDtoList(oldList);
    }

    private static void reSetVehicle(AppSvcRelatedInfoDto sourceReletedInfo, AppSvcRelatedInfoDto targetReletedInfo,
            List<String> changeList) {
        List<AppSvcVehicleDto> oldList = (List<AppSvcVehicleDto>) CopyUtil.copyMutableObjectList(
                sourceReletedInfo.getAppSvcVehicleDtoList());
        List<AppSvcVehicleDto> newList = targetReletedInfo.getAppSvcVehicleDtoList();
        boolean isChanged = changeList.contains(HcsaConsts.STEP_VEHICLES);
        if (isChanged && oldList != null && newList != null) {
            for (int i = 0, len = oldList.size(); i < len; i++) {
                AppSvcVehicleDto oldVehicleDto = oldList.get(i);
                boolean match = false;
                for (AppSvcVehicleDto newVehicleDto : oldList) {
                    match = Objects.equals(oldVehicleDto.getEngineNum(), newVehicleDto.getEngineNum())
                            && Objects.equals(oldVehicleDto.getChassisNum(), newVehicleDto.getChassisNum());
                }
                if (!match) {
                    oldList.remove(i);
                }
            }
        }
        targetReletedInfo.setAppSvcVehicleDtoList(oldList);
    }

    private static void reSetOtherInfo(AppSvcRelatedInfoDto sourceReletedInfo, AppSvcRelatedInfoDto targetReletedInfo) {
        if (sourceReletedInfo != null && targetReletedInfo != null) {
            List<AppSvcOtherInfoDto> oldList = (List<AppSvcOtherInfoDto>) CopyUtil.copyMutableObjectList(
                    sourceReletedInfo.getAppSvcOtherInfoList());
            List<AppSvcOtherInfoDto> newList = targetReletedInfo.getAppSvcOtherInfoList();
            if (oldList != null && newList != null) {
                for (int i = 0; i < oldList.size(); i++) {
                    AppSvcOtherInfoDto oldAppSvcOtherInfoDto = oldList.get(i);
                    for (AppSvcOtherInfoDto newAppSvcOtherInfoDto : newList) {
                        if (Objects.equals(oldAppSvcOtherInfoDto.getPremisesVal(), newAppSvcOtherInfoDto.getPremisesVal())) {
                            // set autoList
                            reSetOtherService(oldAppSvcOtherInfoDto, newAppSvcOtherInfoDto);
                        }
                    }
                }
            }
            targetReletedInfo.setAppSvcOtherInfoList(oldList);
        }
    }

    private static void reSetOtherService(AppSvcOtherInfoDto oldAppSvcOtherInfoDto, AppSvcOtherInfoDto newAppSvcOtherInfoDto) {
        List<AppPremSubSvcRelDto> appPremSubSvcRelDtoList = newAppSvcOtherInfoDto.getFlatAppPremSubSvcRelList(
                dto -> StringUtil.isNotEmpty(dto.getActCode()));
        oldAppSvcOtherInfoDto.setAppPremSubSvcRelDtoList(appPremSubSvcRelDtoList);
        oldAppSvcOtherInfoDto.initAllAppPremSubSvcRelDtoList();
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

    public static <T, R> boolean isSame(T source, T target, Function<T, R> newObject) {
        R newSrc = newObject.apply(source);
        R newTar = newObject.apply(target);
        return Objects.equals(newSrc, newTar);
    }

    public static <T, R> boolean isSame(List<T> source, List<T> target, Function<List<T>, List<R>> newList) {
        if (IaisCommonUtils.isEmpty(source) && IaisCommonUtils.isEmpty(target)) {
            return false;
        } else if (source == null ^ target == null) {
            return true;
        }
        if (source.size() != target.size()) {
            return false;
        }
        List<R> newSrc = newList.apply(source);
        List<R> newTar = newList.apply(target);
        return newSrc.equals(newTar);
    }

    public static <T> boolean isChangedList(List<T> src, List<T> oldSrc, BiPredicate<T, List<T>> check) {
        return isChangedList(src, oldSrc, null, check);
    }

    public static <T> boolean isChangedList(List<T> source, List<T> oldSource, Function<List<T>, List<T>> newFun,
            BiPredicate<T, List<T>> check) {
        if (IaisCommonUtils.isEmpty(source) && IaisCommonUtils.isEmpty(oldSource)) {
            return false;
        } else if (source == null ^ oldSource == null) {
            return true;
        }
        int size = source.size();
        if (source.size() != oldSource.size()) {
            return true;
        }
        List<T> src = newFun != null ? newFun.apply(source) : source;
        List<T> oldSrc = newFun != null ? newFun.apply(oldSource) : oldSource;
        boolean noneMatch = src.stream()
                .noneMatch(t -> check.test(t, oldSrc));
        if (noneMatch) {
            return true;
        }
        return oldSrc.stream()
                .noneMatch(t -> check.test(t, src));
    }

    /**
     * @param source
     * @param oldSource
     * @param newFun
     * @param target
     * @param check
     * @param <T>
     * @return
     */
    public static <T> int isChangedList(List<T> source, List<T> oldSource, Function<List<T>, List<T>> newFun,
            BiFunction<T, List<T>, T> target, BiPredicate<T, T> check) {
        int status = RfcConst.STATUS_UNCHANGED;
        if (IaisCommonUtils.isEmpty(source) && IaisCommonUtils.isEmpty(oldSource)) {
            return status;
        }
        if (source == null) {
            return status | RfcConst.STATUS_REMOVAL;
        }
        if (oldSource == null) {
            return status | RfcConst.STATUS_ADDITION;
        }
        List<T> newList = newFun != null ? newFun.apply(source) : source;
        List<T> oldList = newFun != null ? newFun.apply(oldSource) : oldSource;
        for (T t : newList) {
            T u = target.apply(t, oldList);
            if (u == null) {
                status |= RfcConst.STATUS_ADDITION;
            } else if (!check.test(t, u)) {
                status |= RfcConst.STATUS_CHANGED;
            }
        }
        for (T t : oldList) {
            T u = target.apply(t, newList);
            if (u == null) {
                status |= RfcConst.STATUS_REMOVAL;
            } else if (!check.test(t, u)) {
                status |= RfcConst.STATUS_CHANGED;
            }
        }
        return status;
    }

    public static void resolveSpecialisedRfc(AppSubmissionDto autoAppSubmissionDto, AppSubmissionDto oldAppSubmissionDto,
            boolean isAuto) {
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
        resolveSpecialisedRfc(appPremSpecialisedDtoList.get(0), oldAppPremSpecialisedDtoList.get(0),
                appSvcRelatedInfoDtoList.get(0), oldAppSvcRelatedInfoDtoList.get(0), isAuto);
    }

    public static void resolveSpecialisedRfc(AppPremSpecialisedDto appPremSpecialisedDto,
            AppPremSpecialisedDto oldAppPremSpecialisedDto, AppSvcRelatedInfoDto appSvcRelatedInfoDto,
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto, boolean isAuto) {
        // scope
        Map<String, AppPremScopeDto> scopeDtoMap = oldAppPremSpecialisedDto.getCheckedAppPremScopeDtoList()
                .stream().collect(Collectors.toMap(AppPremScopeDto::getSubTypeId, Function.identity()));
        List<AppPremScopeDto> appPremScopeDtoList = appPremSpecialisedDto.getAppPremScopeDtoList();
        if (IaisCommonUtils.isNotEmpty(appPremScopeDtoList)) {
            for (AppPremScopeDto scopeDto : appPremScopeDtoList) {
                scopeDto.resolveAppPremScopeDtos((dto) -> {
                    if (scopeDtoMap.get(dto.getSubTypeId()) != null) {
                        return;
                    }
                    if (dto.isChecked()) {
                        // Addition
                        if (isAuto) {
                            dto.setChecked(false);
                            dto.setActCode(null);
                        } else {
                            dto.setActCode(ApplicationConsts.RECORD_ACTION_CODE_ADD);
                        }
                    } else {
                        // removal
                        if (isAuto) {
                            dto.setActCode(ApplicationConsts.RECORD_ACTION_CODE_REMOVE);
                        } else {
                            dto.setActCode(null);
                        }
                    }
                });
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
            int ssiSize = sectionList.size();
            // document
            List<AppSvcDocDto> appSvcDocDtoLit = IaisCommonUtils.getList(appSvcRelatedInfoDto.getAppSvcDocDtoLit());
            Map<String, List<AppSvcDocDto>> docMap = IaisCommonUtils.getList(oldAppSvcRelatedInfoDto.getAppSvcDocDtoLit())
                    .stream().collect(Collectors.groupingBy(AppSvcDocDto::getSvcId, Collectors.toList()));
            int docSize = appSvcDocDtoLit.size();
            Consumer<AppPremSubSvcRelDto> change = relDto -> {
                AppPremSubSvcRelDto oldRel = relDtoMap.get(relDto.getSvcCode());
                if (oldRel == null) {
                    oldRel = new AppPremSubSvcRelDto();
                }
                if (relDto.isAdditionFlow()) {
                    if (isAuto && relDto.isChecked() && !oldRel.isChecked()) {
                        relDto.setChecked(false);
                        relDto.setActCode(null);
                        relDto.setStatus(null);
                    }
                } else {
                    if (relDto.isChecked()) {
                        relDto.setStatus(ApplicationConsts.RECORD_STATUS_APPROVE_CODE);
                    }
                    if (!isAuto && relDto.isChecked() && !oldRel.isChecked()) {
                        relDto.setChecked(true);
                        relDto.setActCode(ApplicationConsts.RECORD_ACTION_CODE_UNCHANGE);
                    }
                }
                if (relDto.isRemovalFlow()) {
                    if (isAuto && !relDto.isChecked() && oldRel.isChecked()) {
                        relDto.setChecked(true);
                        relDto.setActCode(ApplicationConsts.RECORD_ACTION_CODE_UNCHANGE);
                        relDto.setStatus(ApplicationConsts.RECORD_STATUS_APPROVE_CODE);
                        SpecialServiceSectionDto sectionDto = sectionDtoMap.get(relDto.getSvcCode());
                        if (sectionDto != null) {
                            sectionList.add(sectionDto);
                        }
                        List<AppSvcDocDto> appSvcDocDtos = docMap.get(relDto.getSvcId());
                        if (IaisCommonUtils.isNotEmpty(appSvcDocDtos)) {
                            appSvcDocDtoLit.addAll(appSvcDocDtos);
                        }
                    }
                } else {
                    if (oldRel.isChecked()) {
                        relDto.setStatus(ApplicationConsts.RECORD_STATUS_APPROVE_CODE);
                    }
                    if (!isAuto && !relDto.isChecked() && oldRel.isChecked()) {
                        relDto.setChecked(false);
                        relDto.setActCode(null);
                        relDto.setStatus(null);
                    }
                }
                if (relDto.isChecked() && isAuto) {
                    relDto.setStatus(ApplicationConsts.RECORD_STATUS_APPROVE_CODE);
                }
            };
            for (AppPremSubSvcRelDto relDto : appPremSubSvcRelDtoList) {
                change.accept(relDto);
                relDto.resolveAppPremSubSvcRelDtos(change);
            }
            appPremSpecialisedDto.initAllAppPremSubSvcRelDtoList();
            log.info(StringUtil.changeForLog("The SSI size: " + ssiSize + " - " + sectionList.size()
                    + "; Doc size: " + docSize + " " + "- " + appSvcDocDtoLit.size()));
            appSvcSpecialServiceInfoDto.setSpecialServiceSectionDtoList(sectionList);
            appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtoLit);
        }
    }

    public static void resolveSvcActionCode(List<AppPremSubSvcRelDto> relList, Map<String, AppPremSubSvcRelDto> oldDtaMap,
            String appType) {
        if (IaisCommonUtils.isEmpty(relList)) {
            return;
        }
        for (AppPremSubSvcRelDto relDto : relList) {
            AppPremSubSvcRelDto oldRelDto = oldDtaMap.get(relDto.getSvcCode());
            if (oldRelDto == null && !relDto.isChecked()) {
                continue;
            }
            if (HcsaConsts.SERVICE_TYPE_SPECIFIED.equals(relDto.getSvcType())) {
                relDto.setStatus(ApplicationConsts.RECORD_STATUS_SUBMIT_CODE);
            } else {
                if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)) {
                    relDto.setStatus(ApplicationConsts.RECORD_STATUS_APPROVE_CODE);
                } else {
                    relDto.setStatus(ApplicationConsts.RECORD_STATUS_SUBMIT_CODE);
                }
            }
            if (oldRelDto == null && relDto.isChecked()) {
                relDto.setActCode(ApplicationConsts.RECORD_ACTION_CODE_ADD);
            } else if (oldRelDto != null) {
                boolean oldExisted = !ApplicationConsts.RECORD_ACTION_CODE_REMOVE.equals(oldRelDto.getActCode())
                        && oldRelDto.isChecked();
                if (!relDto.isChecked() && oldExisted) {
                    relDto.setActCode(ApplicationConsts.RECORD_ACTION_CODE_REMOVE);
                } else if (relDto.isChecked() && oldExisted) {
                    relDto.setActCode(ApplicationConsts.RECORD_ACTION_CODE_UNCHANGE);
                    relDto.setStatus(ApplicationConsts.RECORD_STATUS_APPROVE_CODE);
                } else if (!relDto.isChecked() && !oldExisted) {
                    if (ApplicationConsts.RECORD_ACTION_CODE_REMOVE.equals(oldRelDto.getActCode())) {
                        relDto.setActCode(ApplicationConsts.RECORD_ACTION_CODE_REMOVE);
                    }
                } else if (relDto.isChecked() && !oldExisted) {
                    relDto.setActCode(ApplicationConsts.RECORD_ACTION_CODE_ADD);
                }
            }
            resolveSvcActionCode(relDto.getAppPremSubSvcRelDtos(), oldDtaMap, appType);
        }
    }

    public static AmendmentFeeDto getAmendmentFeeDto(AppSubmissionDto appSubmissionDto, AppEditSelectDto appEditSelectDto,
            boolean isCharity) {
        AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
        amendmentFeeDto.setChangeInLicensee(appEditSelectDto.isLicenseeEdit());
        amendmentFeeDto.setChangeInLocation(appEditSelectDto.isChangeInLocation() || appEditSelectDto.isChangeFloorUnits());
        amendmentFeeDto.setAdditionOrRemovalVehicles(appEditSelectDto.isChangeVehicle());
        amendmentFeeDto.setIsCharity(isCharity);
        amendmentFeeDto.setChangeBusinessName(appEditSelectDto.isChangeBusinessName());
        //amendmentFeeDto.setAdditionOrRemovalSpecialisedServices(appEditSelectDto.isSpecialisedEdit());
        amendmentFeeDto.setAdditionOrRemovalCategory(appEditSelectDto.isSpecialisedEdit());
        amendmentFeeDto.setAddress(appSubmissionDto.getAppGrpPremisesDtoList().get(0).getAddress());
        amendmentFeeDto.setServiceName(appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceName());
        amendmentFeeDto.setServiceCode(appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceCode());
        amendmentFeeDto.setAppGrpNo(appSubmissionDto.getAppGrpNo());
        amendmentFeeDto.setLicenceNo(appSubmissionDto.getLicenceNo());
        amendmentFeeDto.setIsCharity(isCharity);
        if (appEditSelectDto.isSpecialisedEdit()) {
            //add ss fee
            List<AppPremSubSvcRelDto> appPremSubSvcRelDtoList = appSubmissionDto.getAppPremSpecialisedDtoList().get(
                    0).getFlatAppPremSubSvcRelList(dto -> ApplicationConsts.RECORD_ACTION_CODE_ADD.equals(dto.getActCode()));
            if (IaisCommonUtils.isNotEmpty(appPremSubSvcRelDtoList)) {
                amendmentFeeDto.setAdditionOrRemovalSpecialisedServices(Boolean.TRUE);
                List<LicenceFeeDto> licenceFeeSpecDtos = IaisCommonUtils.genNewArrayList();
                for (AppPremSubSvcRelDto subSvc : appPremSubSvcRelDtoList) {
                    LicenceFeeDto specFeeDto = new LicenceFeeDto();
                    specFeeDto.setBundle(0);
                    specFeeDto.setBaseService(appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceCode());
                    specFeeDto.setServiceCode(subSvc.getSvcCode());
                    specFeeDto.setServiceName(subSvc.getSvcName());
                    specFeeDto.setPremises(appSubmissionDto.getAppGrpPremisesDtoList().get(0).getAddress());
                    specFeeDto.setCharity(isCharity);
                    licenceFeeSpecDtos.add(specFeeDto);
                }
                amendmentFeeDto.setSpecifiedLicenceFeeDto(licenceFeeSpecDtos);
            }
            List<AppPremSubSvcRelDto> removalDtoList = appSubmissionDto.getAppPremSpecialisedDtoList().get(
                    0).getFlatAppPremSubSvcRelList(dto -> ApplicationConsts.RECORD_ACTION_CODE_REMOVE.equals(dto.getActCode()));
            if (IaisCommonUtils.isNotEmpty(removalDtoList)) {
                amendmentFeeDto.setAdditionOrRemovalSpecialisedServices(Boolean.TRUE);
            }
        }
        return amendmentFeeDto;
    }

    public static void beforeSubmit(AppSubmissionDto appSubmissionDto, AppEditSelectDto appEditSelectDto, String appGrpNo,
            String appType, HttpServletRequest request) {
        if (appSubmissionDto == null) {
            return;
        }
        DealSessionUtil.initView(appSubmissionDto);
        if (!ApplicationHelper.checkIsRfi(request)) {
            appSubmissionDto.setAppGrpId(null);
        }
        appSubmissionDto.setFromBe(ApplicationHelper.isBackend());
        appSubmissionDto.setAppType(appType);
        appSubmissionDto.setAuditTrailDto(AuditTrailHelper.getCurrentAuditTrailDto());
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
        if (appEditSelectDto != null) {
            if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)) {
                appEditSelectDto.initEmailSend();
            }
            appSubmissionDto.setChangeSelectDto(appEditSelectDto);
        }
        ApplicationHelper.reSetAdditionalFields(appSubmissionDto, appEditSelectDto, appGrpNo);
//        ApplicationHelper.reSetAdditionalFields(appSubmissionDto, oldAppSubmissionDto);
        // bind application
//        setRelatedInfoBaseServiceId(appSubmissionDto);
        /*if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)) {
            appSubmissionDto.setAppStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
        } else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            appSubmissionDto.setAppStatus(ApplicationConsts.APPLICATION_STATUS_RENEWAL);
        }*/
    }

}
