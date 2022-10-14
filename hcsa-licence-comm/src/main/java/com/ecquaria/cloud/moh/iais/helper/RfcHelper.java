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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmFormDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.OperationHoursReloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
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
import org.apache.commons.math3.util.Combinations;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        // key personnel
        boolean changePersonnel = isChangeKeyPersonnel(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos, nonAutoList);
        int changeVehiclesFields = isChangeAppSvcVehicleDtos(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);
        boolean changeVehicles = changeVehiclesFields != RfcConst.RFC_BASE;
        if (changeVehicles) {
            boolean changeVehicleNonAutoFields = (changeVehiclesFields & RfcConst.RFC_AMENDMENT) != 0;
            boolean changeVehicleAutoFields = (changeVehiclesFields & RfcConst.RFC_NOTIFICATION) != 0;
            appEditSelectDto.setChangeVehicleAutoFields(changeVehicleAutoFields);
            appEditSelectDto.setChangeVehicleNonAutoFields(changeVehicleNonAutoFields);
        }
        appEditSelectDto.setChangeVehicle(changeVehicles);
        int changeBusinessFields = isChangeAppSvcBusinessDtos(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);
        boolean changeBusiness = changeBusinessFields != RfcConst.RFC_BASE;
        boolean changeBusinessNonAutoFields = (changeBusinessFields & RfcConst.RFC_AMENDMENT) != 0;
        boolean changeBusinessAutoFields = (changeBusinessFields & RfcConst.RFC_NOTIFICATION) != 0;
        boolean changeSectionLeader = isChangeAppSvcSectionLeadersViaSvcInfo(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);



//        add
        boolean sectionSendMessageNoAuto = isAddOrReplaceAppSvcSectionLeaders(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);
//        remove
        boolean sectionSendMessageAuto = isRemoveAppSvcSectionLeaders(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos,autoList);
//        detail
        boolean sectionNoSendMessageAuto = isChangeDetailAppSvcSectionLeaders(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);


//        add and replacing
        boolean sendMessageAndNoAuto = isAddOrReplaceSvcPersonnel(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);
//        remove
        boolean sendMessageAndAuto = isRemoveSvcPersonnel(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos,autoList);
//        detail
        boolean noSendMessageAndAuto = isChangeDetailSvcPersonnel(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);


//        add  and replacing
        boolean sendMessageAndNoAutoCd = isAddOrReplaceClinicalGovernanceOfficers(appSvcRelatedInfoDtos,oldAppSvcRelatedInfoDtos);
//        remove
        boolean sendMessageAndAutoCd = isRemoveClinicalGovernanceOfficers(appSvcRelatedInfoDtos,oldAppSvcRelatedInfoDtos,autoList);

        boolean noSendMessageAndAutoCd = isChangeDetailClinicalGovernanceOfficers(appSvcRelatedInfoDtos,oldAppSvcRelatedInfoDtos);




//        Supplementary Form
        boolean changeSupplementaryForm = isChangeSupplementaryForm(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos,autoList);
        appEditSelectDto.setSendMessageAndNoAutoSuForm(changeSupplementaryForm);


        int[] arr = {1,2,3,4,5};
        int i = arr[7];

        boolean changeCharges = isChangeAppSvcChargesPageDto(appSvcRelatedInfoDtos.get(0).getAppSvcChargesPageDto(),
                oldAppSvcRelatedInfoDtos.get(0).getAppSvcChargesPageDto());
        boolean changeServiceAutoFields = changeCharges || isChangeSvcInfoAutoFields(appSvcRelatedInfoDtos,
                oldAppSvcRelatedInfoDtos, appEditSelectDto);
        boolean changeSpecialServiceInformation=ischangeSpecialServiceInformation(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);
        appEditSelectDto.setChangeBusinessName(changeBusinessNonAutoFields);
        appEditSelectDto.setChangeBusinessAutoFields(changeBusinessAutoFields);
        appEditSelectDto.setChangePersonnel(changePersonnel);
        appEditSelectDto.setChangeSectionLeader(changeSectionLeader);
        appEditSelectDto.setChangeSpecialServiceInformation(changeSpecialServiceInformation);
        appEditSelectDto.setSendMessageAndAuto(sendMessageAndAuto);
        appEditSelectDto.setSendMessageAndNoAuto(sendMessageAndNoAuto);
        appEditSelectDto.setNoSendMessageAndAuto(noSendMessageAndAuto);
        appEditSelectDto.setSectionNoSendMessageAuto(sectionNoSendMessageAuto);
        appEditSelectDto.setSectionSendMessageNoAuto(sectionSendMessageNoAuto);
        appEditSelectDto.setSendMessageAndAuto(sectionSendMessageAuto);

        boolean serviceIsChange = changeVehicles || changeBusiness || changeSectionLeader
                || changeServiceAutoFields||changeSpecialServiceInformation;
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
        log.info(StringUtil.changeForLog(appSubmissionDto.getLicenceNo() + " - App Edit Select Dto: "
                + JsonUtil.parseToJson(appEditSelectDto)));
        return appEditSelectDto;
    }

    public static boolean isChangeDetailClinicalGovernanceOfficers(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList){
        if (appSvcRelatedInfoDtoList == null || oldAppSvcRelatedInfoDtoList == null) {
            return false;
        }
        int size = appSvcRelatedInfoDtoList.size();
        if (size != oldAppSvcRelatedInfoDtoList.size()){
            return false;
        }
        for (int i = 0; i < size; i++) {
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(i);
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtoList.get(i);
            List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            List<AppSvcPrincipalOfficersDto> oldCopyAppSvcCgoDtoList = combinationAppSvcRelatedInfoDto(appSvcCgoDtoList,oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList());
            if (isChangeDetails(appSvcCgoDtoList,oldCopyAppSvcCgoDtoList,PageDataCopyUtil::copyAppSvcPrincipalOfficersDtoDetail)){
                return true;
            }
        }
        return false;
    }


    public static List<AppSvcPrincipalOfficersDto> combinationAppSvcRelatedInfoDto(List<AppSvcPrincipalOfficersDto> newList, List<AppSvcPrincipalOfficersDto> oldList){
        List<String> indexNo = new ArrayList<>();
        List<AppSvcPrincipalOfficersDto> oldCopyAppSvcCgoDtoList = new ArrayList<>();
            if (IaisCommonUtils.isNotEmpty(newList) && IaisCommonUtils.isNotEmpty(oldList)){
                newList.forEach(e -> indexNo.add(e.getIndexNo()));
                for (String index : indexNo) {
                    List<AppSvcPrincipalOfficersDto> dtoList = oldList.stream().filter(dto -> Objects.equals(index, dto.getIndexNo())).collect(Collectors.toList());
                    if (IaisCommonUtils.isNotEmpty(dtoList)) {
                        AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = dtoList.get(0);
                        oldCopyAppSvcCgoDtoList.add(appSvcPrincipalOfficersDto);
                    }
                }
                return oldCopyAppSvcCgoDtoList;
            }
            return null;
    }

    public static boolean isAddOrReplaceClinicalGovernanceOfficers(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList){
        int compareLength = compareLength(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        if (compareLength == RfcConst.RFC_NULL || compareLength == RfcConst.RFC_REMOVE){
            return false;
        }
        if (compareLength == RfcConst.RFC_ADD){
            return true;
        }
        int size = appSvcRelatedInfoDtoList.size();
        for (int i = 0; i < size; i++) {
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(i);
            List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtoList.get(i);
            List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList = oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList();
            if (compareClinicalGovernanceOfficers(appSvcCgoDtoList, oldAppSvcCgoDtoList)) {
                return true;
            }
        }
        return false;
    }

    public static boolean compareClinicalGovernanceOfficers(List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList, List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList){
        int compareLength = compareLength(appSvcCgoDtoList, oldAppSvcCgoDtoList);
        if (compareLength == RfcConst.RFC_NULL || compareLength == RfcConst.RFC_REMOVE){
            return false;
        }
        if (compareLength == RfcConst.RFC_ADD){
            return true;
        }
        int size = appSvcCgoDtoList.size();
        for (int i = 0; i < size; i++) {
            AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = appSvcCgoDtoList.get(i);
            AppSvcPrincipalOfficersDto oldAppSvcPrincipalOfficersDto = oldAppSvcCgoDtoList.get(i);
            if (!isSame(appSvcPrincipalOfficersDto,oldAppSvcPrincipalOfficersDto,PageDataCopyUtil::copyAppSvcPrincipalOfficersDto)){
                return true;
            }
        }
        return false;
    }


    public static boolean isRemoveClinicalGovernanceOfficers(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList,List<String> autoList) {
        int compareLength = compareLength(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        if (compareLength == RfcConst.RFC_REMOVE){
            return true;
        }
        if (compareLength == RfcConst.RFC_SAME){
            int size = appSvcRelatedInfoDtoList.size();
            for (int i = 0; i < size; i++) {
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(i);
                AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtoList.get(i);
                List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList = oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList();
                int length = compareLength(appSvcCgoDtoList, oldAppSvcCgoDtoList);
                if (length == RfcConst.RFC_REMOVE){
                    appSvcCgoDtoList.forEach(e->autoList.add(e.getPsnType()));
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isChangeSupplementaryForm(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList,List<String> autoList) {
        if (appSvcRelatedInfoDtoList == null || oldAppSvcRelatedInfoDtoList == null) {
            return true;
        }
        int size = appSvcRelatedInfoDtoList.size();
        if (size != oldAppSvcRelatedInfoDtoList.size()) {
            return true;
        }
        for (int i = 0; i < size; i++) {
            List<AppSvcSuplmFormDto> appSvcSuplmFormList = appSvcRelatedInfoDtoList.get(i).getAppSvcSuplmFormList();
            List<AppSvcSuplmFormDto> oldAppSvcSuplmFormList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcSuplmFormList();
            if (compareSupplementaryForm(appSvcSuplmFormList,oldAppSvcSuplmFormList)){
                autoList.add(HcsaConsts.STEP_SUPPLEMENTARY_FORM);
                return true;
            }
        }
        return false;
    }
    public static boolean compareSupplementaryForm(List<AppSvcSuplmFormDto> appSvcSuplmFormList, List<AppSvcSuplmFormDto> oldAppSvcSuplmFormList) {
        Map<String,String> newMap = IaisCommonUtils.genNewHashMap();
        Map<String,String> oldMap = IaisCommonUtils.genNewHashMap();
        if (appSvcSuplmFormList == null || oldAppSvcSuplmFormList == null) {
            return true;
        }
        int size = appSvcSuplmFormList.size();
        if (size != oldAppSvcSuplmFormList.size()) {
            return true;
        }
        for (int i = 0; i < size; i++) {
            AppSvcSuplmFormDto appSvcSuplmFormDto = appSvcSuplmFormList.get(i);
            AppSvcSuplmFormDto oldAppSvcSuplmFormDto = oldAppSvcSuplmFormList.get(i);
            List<AppSvcSuplmGroupDto> appSvcSuplmGroupDtoList = appSvcSuplmFormDto.getAppSvcSuplmGroupDtoList();
            List<AppSvcSuplmGroupDto> oldAppSvcSuplmGroupDtoList = oldAppSvcSuplmFormDto.getAppSvcSuplmGroupDtoList();
            if (appSvcSuplmGroupDtoList == null || oldAppSvcSuplmGroupDtoList == null) {
                return true;
            }
            int sizes = appSvcSuplmGroupDtoList.size();
            if (sizes != oldAppSvcSuplmGroupDtoList.size()) {
                return true;
            }
            for (int j = 0; j < sizes; j++) {
                AppSvcSuplmGroupDto appSvcSuplmGroupDto = appSvcSuplmGroupDtoList.get(j);
                List<AppSvcSuplmItemDto> appSvcSuplmItemDtoList = appSvcSuplmGroupDto.getAppSvcSuplmItemDtoList();
                AppSvcSuplmGroupDto oldAppSvcSuplmGroupDto = oldAppSvcSuplmGroupDtoList.get(j);
                List<AppSvcSuplmItemDto> oldAppSvcSuplmItemDtoList = oldAppSvcSuplmGroupDto.getAppSvcSuplmItemDtoList();
                if (appSvcSuplmItemDtoList == null || oldAppSvcSuplmItemDtoList == null) {
                    return true;
                }
                int count = appSvcSuplmItemDtoList.size();
                if (count != oldAppSvcSuplmItemDtoList.size()) {
                    return true;
                }
                for (int m = 0; m < count; m++) {
                    String newKey = "";
                    String oldKey = "";
                    AppSvcSuplmItemDto appSvcSuplmItemDto = appSvcSuplmItemDtoList.get(m);
                    SuppleFormItemConfigDto itemConfigDto = appSvcSuplmItemDto.getItemConfigDto();
                    AppSvcSuplmItemDto oldAppSvcSuplmItemDto = oldAppSvcSuplmItemDtoList.get(m);
                    SuppleFormItemConfigDto oldItemConfigDto = oldAppSvcSuplmItemDto.getItemConfigDto();
                    if (!StringUtil.isEmpty(itemConfigDto)){
                        String displayInfo = itemConfigDto.getDisplayInfo();
                        String id = itemConfigDto.getId();
                        newKey = displayInfo + id;
                    }
                    if (!StringUtil.isEmpty(oldItemConfigDto)){
                        String displayInfo = oldItemConfigDto.getDisplayInfo();
                        String id = oldItemConfigDto.getId();
                        oldKey = displayInfo + id;
                    }
                    newMap.put(newKey,appSvcSuplmItemDto.getInputValue());
                    oldMap.put(oldKey,oldAppSvcSuplmItemDto.getInputValue());
                    if (!newMap.equals(oldMap)){
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static boolean isChangeDetailSvcPersonnel(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        if (appSvcRelatedInfoDtoList == null || oldAppSvcRelatedInfoDtoList == null) {
            return false;
        }
        if (appSvcRelatedInfoDtoList == null || oldAppSvcRelatedInfoDtoList == null) {
            return false;
        }
        int size = appSvcRelatedInfoDtoList.size();
        if (size != oldAppSvcRelatedInfoDtoList.size()){
            return false;
        }
        List<SvcPersonnelDto> svcPersonnelDtoList = IaisCommonUtils.genNewArrayList();
        appSvcRelatedInfoDtoList.forEach(e -> svcPersonnelDtoList.add(e.getSvcPersonnelDto()));
        List<SvcPersonnelDto> oldSvcPersonnelDtoList = IaisCommonUtils.genNewArrayList();
        oldAppSvcRelatedInfoDtoList.forEach(e -> oldSvcPersonnelDtoList.add(e.getSvcPersonnelDto()));
        return isChangeSvcPersonnelDto(svcPersonnelDtoList, oldSvcPersonnelDtoList);
    }

    public static boolean isChangeSvcPersonnelDto(List<SvcPersonnelDto> svcPersonnelDtoList, List<SvcPersonnelDto> oldSvcPersonnelDtoList) {
        if (IaisCommonUtils.isNotEmpty(svcPersonnelDtoList) && IaisCommonUtils.isNotEmpty(oldSvcPersonnelDtoList)) {
            int size = svcPersonnelDtoList.size();
            for (int i = 0; i < size; i++) {
                SvcPersonnelDto oldSvcPersonnelDto = new SvcPersonnelDto();
                SvcPersonnelDto svcPersonnelDto = svcPersonnelDtoList.get(i);
                if (!StringUtil.isEmpty(oldSvcPersonnelDtoList.get(i))) {
                    oldSvcPersonnelDto = oldSvcPersonnelDtoList.get(i);
                }


                List<AppSvcPersonnelDto> arPractitionerList = svcPersonnelDto.getArPractitionerList();
                List<AppSvcPersonnelDto> nurseList = svcPersonnelDto.getNurseList();
                List<AppSvcPersonnelDto> embryologistList = svcPersonnelDto.getEmbryologistList();
                List<AppSvcPersonnelDto> normalList = svcPersonnelDto.getNormalList();
                List<AppSvcPersonnelDto> specialList = svcPersonnelDto.getSpecialList();

                List<AppSvcPersonnelDto> copyAr = combinationAppSvcPersonnelDto(arPractitionerList, oldSvcPersonnelDto.getArPractitionerList());
                List<AppSvcPersonnelDto> copyNur = combinationAppSvcPersonnelDto(nurseList, oldSvcPersonnelDto.getNurseList());
                List<AppSvcPersonnelDto> copyEm = combinationAppSvcPersonnelDto(embryologistList, oldSvcPersonnelDto.getEmbryologistList());
                List<AppSvcPersonnelDto> copyNor = combinationAppSvcPersonnelDto(normalList, oldSvcPersonnelDto.getNormalList());
                List<AppSvcPersonnelDto> copySpe = combinationAppSvcPersonnelDto(specialList, oldSvcPersonnelDto.getSpecialList());

                boolean isAr = isChangeDetails(arPractitionerList, copyAr, PageDataCopyUtil::copySvcDetailPersonnel);
                boolean isNur = isChangeDetails(nurseList, copyNur, PageDataCopyUtil::copySvcDetailPersonnel);
                boolean isEm = isChangeDetails(embryologistList, copyEm, PageDataCopyUtil::copySvcDetailPersonnel);
                boolean isNor = isChangeDetails(normalList, copyNor, PageDataCopyUtil::copySvcDetailPersonnel);
                boolean isSpe = isChangeDetails(specialList, copySpe, PageDataCopyUtil::copySvcDetailPersonnel);


                if (isAr || isNur || isEm || isNor || isSpe) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<AppSvcPersonnelDto> combinationAppSvcPersonnelDto(List<AppSvcPersonnelDto> newList, List<AppSvcPersonnelDto> oldList) {
        List<String> indexNo = new ArrayList<>();
        List<AppSvcPersonnelDto> source = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isNotEmpty(newList) && IaisCommonUtils.isNotEmpty(oldList)){
            newList.forEach(e->indexNo.add(e.getIndexNo()));
            for (String index : indexNo) {
                List<AppSvcPersonnelDto> collect = oldList.stream().filter(dto -> Objects.equals(index, dto.getIndexNo())).collect(Collectors.toList());
                if (IaisCommonUtils.isNotEmpty(collect)){
                    AppSvcPersonnelDto appSvcPersonnelDto = collect.get(0);
                    source.add(appSvcPersonnelDto);
                }
            }
            return source;
        }
        return null;
    }

    public static boolean isAddOrReplaceSvcPersonnel(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        int compareLength = compareLength(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        if (compareLength == RfcConst.RFC_NULL || compareLength == RfcConst.RFC_REMOVE){
            return false;
        }
        if (compareLength == RfcConst.RFC_ADD){
            return true;
        }
        int size = appSvcRelatedInfoDtoList.size();
        for (int i = 0; i < size; i++) {
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(i);
            SvcPersonnelDto svcPersonnelDto = appSvcRelatedInfoDto.getSvcPersonnelDto();
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtoList.get(i);
            SvcPersonnelDto oldSvcPersonnelDto = oldAppSvcRelatedInfoDto.getSvcPersonnelDto();
            compareSvcPersonnelDto(svcPersonnelDto,oldSvcPersonnelDto);
        }
        return false;
    }

    public static boolean compareSvcPersonnelDto(SvcPersonnelDto svcPersonnelDto, SvcPersonnelDto oldSvcPersonnelDto) {
       if (StringUtil.isEmpty(svcPersonnelDto) && !StringUtil.isEmpty(oldSvcPersonnelDto)  || Objects.equals(svcPersonnelDto,oldSvcPersonnelDto)){
           return  false;
       }
       if (!StringUtil.isEmpty(svcPersonnelDto) && StringUtil.isEmpty(oldSvcPersonnelDto)){
           return true;
       }
       if (!StringUtil.isEmpty(svcPersonnelDto) && !StringUtil.isEmpty(oldSvcPersonnelDto)){
           List<AppSvcPersonnelDto> arPractitionerList = svcPersonnelDto.getArPractitionerList();
           List<AppSvcPersonnelDto> embryologistList = svcPersonnelDto.getEmbryologistList();
           List<AppSvcPersonnelDto> nurseList = svcPersonnelDto.getNurseList();
           List<AppSvcPersonnelDto> normalList = svcPersonnelDto.getNormalList();
           List<AppSvcPersonnelDto> specialList = svcPersonnelDto.getSpecialList();
           boolean ar = isAddOrSame(arPractitionerList, oldSvcPersonnelDto.getArPractitionerList(), PageDataCopyUtil::copySvcArPersonnel);
           boolean em = isAddOrSame(embryologistList, oldSvcPersonnelDto.getEmbryologistList(), PageDataCopyUtil::copySvcArPersonnel);
           boolean nur = isAddOrSame(nurseList, oldSvcPersonnelDto.getNurseList(), PageDataCopyUtil::copySvcArPersonnel);
           boolean nor = isAddOrSame(normalList, oldSvcPersonnelDto.getNormalList(), PageDataCopyUtil::copySvcArPersonnel);
           boolean spe = isAddOrSame(specialList, oldSvcPersonnelDto.getSpecialList(), PageDataCopyUtil::copySvcArPersonnel);
           boolean flag = ar || em || nur || nor || spe;
           if (flag){
               return true;
           }
       }
       return false;
    }

    public static boolean isRemoveSvcPersonnel(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList,List<String> autoList) {
        int compareLength = compareLength(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        if (compareLength == RfcConst.RFC_REMOVE){
            return true;
        }
        if (compareLength == RfcConst.RFC_NULL){
            return false;
        }
        if (compareLength == RfcConst.RFC_SAME){
            int size = appSvcRelatedInfoDtoList.size();
            for (int i = 0; i < size; i++) {
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(i);
                SvcPersonnelDto svcPersonnelDto = appSvcRelatedInfoDto.getSvcPersonnelDto();
                AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtoList.get(i);
                SvcPersonnelDto oldSvcPersonnelDto = oldAppSvcRelatedInfoDto.getSvcPersonnelDto();
                if (isRemoveSvcPersonnelDto(svcPersonnelDto, oldSvcPersonnelDto,autoList)){
                    return true;
                }
            }
        }
        return false;

    }

    public static boolean isRemoveSvcPersonnelDto(SvcPersonnelDto svcPersonnelDto, SvcPersonnelDto oldSvcPersonnelDto,List<String> autoList) {
        if (!StringUtil.isEmpty(svcPersonnelDto) && StringUtil.isEmpty(oldSvcPersonnelDto)  || Objects.equals(svcPersonnelDto,oldSvcPersonnelDto)){
            return  false;
        }
        if (StringUtil.isEmpty(svcPersonnelDto) && !StringUtil.isEmpty(oldSvcPersonnelDto)){
            return true;
        }
        if (!StringUtil.isEmpty(svcPersonnelDto) && !StringUtil.isEmpty(oldSvcPersonnelDto)){
            List<AppSvcPersonnelDto> arPractitionerList = svcPersonnelDto.getArPractitionerList();
            List<AppSvcPersonnelDto> embryologistList = svcPersonnelDto.getEmbryologistList();
            List<AppSvcPersonnelDto> nurseList = svcPersonnelDto.getNurseList();
            List<AppSvcPersonnelDto> normalList = svcPersonnelDto.getNormalList();
            List<AppSvcPersonnelDto> specialList = svcPersonnelDto.getSpecialList();
            boolean ar = isRemove(arPractitionerList, oldSvcPersonnelDto.getArPractitionerList());
            boolean em = isRemove(embryologistList, oldSvcPersonnelDto.getEmbryologistList());
            boolean nur = isRemove(nurseList, oldSvcPersonnelDto.getNurseList());
            boolean nor = isRemove(normalList, oldSvcPersonnelDto.getNormalList());
            boolean spe = isRemove(specialList, oldSvcPersonnelDto.getSpecialList());
            if (ar){
                arPractitionerList.forEach(e->autoList.add(e.getPersonnelType()));
            }
            if (em){
                embryologistList.forEach(e->autoList.add(e.getPersonnelType()));
            }
            if (nur){
                nurseList.forEach(e->autoList.add(e.getPersonnelType()));
            }
            if (nor){
                normalList.forEach(e->autoList.add(e.getPersonnelType()));
            }
            if (spe){
                specialList.forEach(e->autoList.add(e.getPersonnelType()));
            }
            boolean flag = ar || em || nur || nor || spe;
            if (flag){
                return true;
            }
        }
        return false;
    }
    public static <T> boolean isRemove(List<T> newList, List<T> oldList) {
        int compareLength = compareLength(newList, oldList);
        if (compareLength == RfcConst.RFC_REMOVE){
            return true;
        }
        return false;
    }

    public static <T> int compareLength(List<T> newList, List<T> oldList) {       //add  true
        if (IaisCommonUtils.isEmpty(newList) && IaisCommonUtils.isEmpty(oldList)){
            return RfcConst.RFC_NULL;
        }
        int newSize = Optional.ofNullable(newList).map(List::size).orElse(-1);        //  -1
        int oldSize = Optional.ofNullable(oldList).map(List::size).orElse(-1);       //  1
        if (newSize != oldSize){
            if (newSize > oldSize){
                return RfcConst.RFC_ADD;
            }else {
                return RfcConst.RFC_REMOVE;
            }
        }
        return RfcConst.RFC_SAME;
    }

    public static <T, R> boolean isChangeDetails(List<T> source, List<T> target, Function<List<T>, List<R>> newList) {
        if (source == null && target == null) {
            return false;
        } else if (source == null ^ target == null) {
            return false;
        }
        List<R> newSrc = newList.apply(source);
        List<R> newTar = newList.apply(target);
        return !newSrc.equals(newTar);
    }
    public static <T, R> boolean isAddOrSame(List<T> source, List<T> target, Function<List<T>, List<R>> newList) {
        int compareLength = compareLength(source, target);
        if (compareLength == RfcConst.RFC_NULL  || compareLength == RfcConst.RFC_REMOVE){
            return false;
        }
        if (compareLength == RfcConst.RFC_ADD){
            return true;
        }
        List<R> newSrc = newList.apply(source);
        List<R> newTar = newList.apply(target);

        return !newSrc.equals(newTar);
    }

    private static boolean ischangeSpecialServiceInformation(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        if (appSvcRelatedInfoDtoList == null || oldAppSvcRelatedInfoDtoList == null) {
            return false;
        }
        if (appSvcRelatedInfoDtoList.size() != oldAppSvcRelatedInfoDtoList.size()) {
            return false;
        }
        List<AppSvcSpecialServiceInfoDto> appSvcSpecialServiceInfoDtoList = IaisCommonUtils.genNewArrayList();
        appSvcRelatedInfoDtoList.forEach((item) -> appSvcSpecialServiceInfoDtoList.addAll(item.getAppSvcSpecialServiceInfoList()));
        List<AppSvcSpecialServiceInfoDto> oldAppSvcSpecialServiceInfoDtoList = IaisCommonUtils.genNewArrayList();
        oldAppSvcRelatedInfoDtoList.forEach((item) -> oldAppSvcSpecialServiceInfoDtoList.addAll(item.getAppSvcSpecialServiceInfoList()));
        boolean result = false;
        List<AppSvcPrincipalOfficersDto> keyPersonnelList = IaisCommonUtils.genNewArrayList();
        appSvcSpecialServiceInfoDtoList.forEach((item) -> keyPersonnelList.addAll(item.getAppSvcCgoDtoList()));
        List<AppSvcPrincipalOfficersDto> oldKkeyPersonnelList = IaisCommonUtils.genNewArrayList();
        appSvcSpecialServiceInfoDtoList.forEach((item) -> keyPersonnelList.addAll(item.getAppSvcCgoDtoList()));
        boolean changeKeyPersonnel = isChangeKeyPersonnel(keyPersonnelList, oldKkeyPersonnelList, false);
        List<AppSvcPersonnelDto> personnelList = IaisCommonUtils.genNewArrayList();
        appSvcSpecialServiceInfoDtoList.forEach((item) -> personnelList.addAll(item.getSpecialPersonnelDtoList()));
        List<AppSvcPersonnelDto> oldPersonnelList = IaisCommonUtils.genNewArrayList();
        appSvcSpecialServiceInfoDtoList.forEach((item) -> oldPersonnelList.addAll(item.getSpecialPersonnelDtoList()));
        boolean changePersonal = isChangeServicePersonnels(personnelList, oldPersonnelList);
        if (changeKeyPersonnel||changePersonal){
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
            return RfcConst.RFC_BASE;
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
        if (IaisCommonUtils.isEmpty(specialisedDto.getAppPremSubSvcRelDtoList())
                && IaisCommonUtils.isEmpty(oldSpecialisedDto.getAppPremSubSvcRelDtoList())) {
            return result;
        }
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
        if (IaisCommonUtils.isEmpty(appPremScopeDtoList) && IaisCommonUtils.isEmpty(oldAppPremScopeDtoList)) {
            return RfcConst.RFC_BASE;
        }
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
        return isChangeServicePersonnels(appSvcRelatedInfoDto.getAppSvcSectionLeaderList(), oldAppSvcRelatedInfoDto.getAppSvcSectionLeaderList());
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
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos) {
        if (appSvcRelatedInfoDtos == null || oldAppSvcRelatedInfoDtos == null) {
            return RfcConst.RFC_AMENDMENT;
        }
        if (appSvcRelatedInfoDtos.size() != oldAppSvcRelatedInfoDtos.size()) {
            return RfcConst.RFC_AMENDMENT;
        }
        int result = RfcConst.RFC_BASE;
        for (int i = 0, len = appSvcRelatedInfoDtos.size(); i < len; i++) {
            result &= isChangeAppSvcVehicleDto(appSvcRelatedInfoDtos.get(i).getAppSvcVehicleDtoList(),
                    oldAppSvcRelatedInfoDtos.get(i).getAppSvcVehicleDtoList());
        }
        return result;
    }

    public static int isChangeAppSvcVehicleDto(List<AppSvcVehicleDto> appSvcVehicleDtoList,
            List<AppSvcVehicleDto> oldAppSvcVehicleDtoList) {
        if (IaisCommonUtils.isEmpty(appSvcVehicleDtoList) && IaisCommonUtils.isEmpty(oldAppSvcVehicleDtoList)) {
            return RfcConst.RFC_BASE;
        }
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
        boolean changeAppSvcBusinessDto = isChangeAppSvcBusinessDto(appSvcBusinessDtoList, oldAppSvcBusinessDtoList);
        if (changeAppSvcBusinessDto) {
            result &= RfcConst.RFC_AMENDMENT;
        }
        boolean changeAppSvcBusinessDtoOtherInfo = isChangeAppSvcBusinessDtoOtherInfo(appSvcBusinessDtoList,
                oldAppSvcBusinessDtoList);
        if (changeAppSvcBusinessDtoOtherInfo) {
            result &= RfcConst.RFC_NOTIFICATION;
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
        if (!n.equals(o)) {
            isChange = true;
        }
        List<AppPremEventPeriodDto> event = IaisCommonUtils.genNewArrayList();
        appSvcBusinessDtoList.forEach((v) -> event.addAll(v.getEventDtoList()));
        List<AppPremEventPeriodDto> copyEvent = PageDataCopyUtil.copyEvent(event);
        List<AppPremEventPeriodDto> oldevent = IaisCommonUtils.genNewArrayList();
        oldAppSvcBusinessDtoList.forEach((v) -> oldevent.addAll(v.getEventDtoList()));
        List<AppPremEventPeriodDto> copyOldEvent = PageDataCopyUtil.copyEvent(event);
        if (!copyEvent.equals(copyOldEvent)) {
            isChange = true;
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
//    change detail
    public static boolean isChangeDetailAppSvcSectionLeaders(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos) {
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
            List<AppSvcPersonnelDto> dtoList = combinationAppSvcPersonnelDto(appSvcSectionLeaderList, oldAppSvcSectionLeaderList);
            if (isChangeDetails(appSvcSectionLeaderList, dtoList, PageDataCopyUtil::copySectionLeaderDetail)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAddOrReplaceAppSvcSectionLeaders(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos) {
        int compareLength = compareLength(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);
        if (compareLength == RfcConst.RFC_NULL || compareLength == RfcConst.RFC_REMOVE){
            return false;
        }
        if (compareLength == RfcConst.RFC_ADD){
            return true;
        }
        int size = appSvcRelatedInfoDtos.size();
        for (int i = 0; i < size; i++) {
            List<AppSvcPersonnelDto> appSvcSectionLeaderList = appSvcRelatedInfoDtos.get(i).getAppSvcSectionLeaderList();
            List<AppSvcPersonnelDto> oldAppSvcSectionLeaderList = oldAppSvcRelatedInfoDtos.get(i).getAppSvcSectionLeaderList();
            boolean isSame = isAddOrSame(appSvcSectionLeaderList, oldAppSvcSectionLeaderList, PageDataCopyUtil::copySectionLeader);
            if (isSame){
                return true;
            }
        }
        return false;
    }

    public static boolean isRemoveAppSvcSectionLeaders(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos,List<String> autoList) {
        int compareLength = compareLength(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);
        if (compareLength == RfcConst.RFC_REMOVE){
            return true;
        }
        if (compareLength == RfcConst.RFC_NULL){
            return false;
        }
        if (compareLength == RfcConst.RFC_SAME){
            int size = appSvcRelatedInfoDtos.size();
            for (int i = 0; i < size; i++) {
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtos.get(i);
                AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSvcRelatedInfoDtos.get(i);
                List<AppSvcPersonnelDto> appSvcSectionLeaderList = appSvcRelatedInfoDto.getAppSvcSectionLeaderList();
                List<AppSvcPersonnelDto> oldAppSvcSectionLeaderList = oldAppSvcRelatedInfoDto.getAppSvcSectionLeaderList();
                int length = compareLength(appSvcSectionLeaderList, oldAppSvcSectionLeaderList);
                if (length == RfcConst.RFC_REMOVE){
                    appSvcSectionLeaderList.forEach(e->autoList.add(e.getPersonnelType()));
                    return true;
                }
            }
        }
        return false;
    }



    public static boolean isChangeAppSvcSectionLeadersViaSvcInfo(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos) {
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
            if (isChangeServicePersonnels(appSvcRelatedInfoDtos.get(i).getAppSvcSectionLeaderList(), oldAppSvcRelatedInfoDtos.get(i).getAppSvcSectionLeaderList())) {
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
                reSetPersonnels(oldSvcInfoDto, newDto, ApplicationConsts.PERSONNEL_PSN_TYPE_PO, changeList);
                reSetPersonnels(oldSvcInfoDto, newDto, ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, changeList);
            } else if (HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(step)) {
                reSetPersonnels(oldSvcInfoDto, newDto, ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, changeList);
            } else if (HcsaConsts.STEP_CLINICAL_DIRECTOR.equals(step)) {
                reSetPersonnels(oldSvcInfoDto, newDto, ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, changeList);
            } else if (HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER.equals(step)) {
                reSetPersonnels(oldSvcInfoDto, newDto, ApplicationConsts.PERSONNEL_PSN_KAH, changeList);
            } else if (HcsaConsts.MEDALERT_PERSON.equals(step)) {
                reSetPersonnels(oldSvcInfoDto, newDto, ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, changeList);
            }
        }
        List<AppSvcRelatedInfoDto> result = IaisCommonUtils.genNewArrayList(1);
        result.add(newDto);
        return result;
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

    public static <T> boolean isChangedList(List<T> src, List<T> oldSrc, BiPredicate<T, List<T>> check) {
       return isChangedList(src, oldSrc, null, check);
    }

    public static <T> boolean isChangedList(List<T> source, List<T> oldSource, Function<List<T>, List<T>> newFun,
            BiPredicate<T, List<T>> check) {
        if (source == null && oldSource == null) {
            return false;
        } else if (source == null ^ oldSource == null) {
            return true;
        }
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
     *
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
        if (source == null && oldSource == null) {
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

}
