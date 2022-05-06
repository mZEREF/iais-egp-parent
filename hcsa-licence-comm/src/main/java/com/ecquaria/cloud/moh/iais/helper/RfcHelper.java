package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.job.executor.util.SpringHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicBaseSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.util.PageDataCopyUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
            AppSubmissionDto oldAppSubmissionDto) throws Exception {
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

        boolean changeInLocation = !compareLocation(appSubmissionDto.getAppGrpPremisesDtoList(),
                oldAppSubmissionDto.getAppGrpPremisesDtoList());
        boolean eqAddFloorNo = isChangeFloorUnit(appSubmissionDto, oldAppSubmissionDto);
        boolean changePremiseAutoFields = isChangeGrpPremisesAutoFields(appGrpPremisesDtoList, oldAppGrpPremisesDtoList);
        boolean notChangePersonnel = compareNotChangePersonnel(appSubmissionDto, oldAppSubmissionDto);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
        boolean changeVehicles = isChangeAppSvcVehicleDtos(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);
        boolean changeBusiness = isChangeAppSvcBusinessDtos(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);
        boolean changeSectionLeader = isChangeAppSvcSectionLeadersViaSvcInfo(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);
        // for splitting the submission
        AppEditSelectDto showDto = appSubmissionDto.getAppEditSelectDto();
        List<String> stepList = IaisCommonUtils.getList(showDto.getPersonnelEditList());
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
        if (!stepList.contains(HcsaConsts.STEP_DISCIPLINE_ALLOCATION)
                && isChangeNotAutoDataForAllocation(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos)) {
            stepList.add(HcsaConsts.STEP_DISCIPLINE_ALLOCATION);
        }
        showDto.setPersonnelEditList(stepList);
        appSubmissionDto.setAppEditSelectDto(showDto);
        // change edit select dto
        appEditSelectDto.setChangeInLocation(changeInLocation);
        appEditSelectDto.setChangeAddFloorUnit(eqAddFloorNo);
        appEditSelectDto.setChangePremiseAutoFields(changePremiseAutoFields);
        appEditSelectDto.setChangeVehicle(changeVehicles);
        appEditSelectDto.setChangeBusinessName(changeBusiness);
        appEditSelectDto.setChangePersonnel(!notChangePersonnel);
        appEditSelectDto.setChangeSectionLeader(changeSectionLeader);

        List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        List<AppGrpPrimaryDocDto> oldAppGrpPrimaryDocDtos = oldAppSubmissionDto.getAppGrpPrimaryDocDtos();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
        boolean licenseeChange = isChangeSubLicensee(appSubmissionDto.getSubLicenseeDto(), oldAppSubmissionDto.getSubLicenseeDto());
        boolean grpPremiseIsChange = changeInLocation || eqAddFloorNo || hciNameChange || changePremiseAutoFields;
        boolean docIsChange = eqDocChange(dtoAppGrpPrimaryDocDtos, oldAppGrpPrimaryDocDtos);
        boolean serviceIsChange = eqServiceChange(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList, appEditSelectDto);
        appEditSelectDto.setLicenseeEdit(licenseeChange);
        appEditSelectDto.setPremisesEdit(grpPremiseIsChange);
        appEditSelectDto.setDocEdit(docIsChange);
        appEditSelectDto.setServiceEdit(serviceIsChange);
        appSubmissionDto.setChangeSelectDto(appEditSelectDto);
        return appEditSelectDto;
    }

    public static boolean eqHciNameChange(AppGrpPremisesDto appGrpPremisesDto, AppGrpPremisesDto oldAppGrpPremisesDto) {
        String hciName = ApplicationHelper.getHciName(appGrpPremisesDto);
        String oldHciName = ApplicationHelper.getHciName(oldAppGrpPremisesDto);
        if (hciName == null && oldHciName == null) {
            return false;
        } else if (hciName == null && oldHciName != null) {
            return true;
        } else if (hciName != null && oldHciName == null) {
            return true;
        }
        if (!hciName.equals(oldHciName)) {
            return true;
        }
        return false;
    }

    public static boolean eqDocChange(List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos,
            List<AppGrpPrimaryDocDto> oldAppGrpPrimaryDocDtos) {
        dtoAppGrpPrimaryDocDtos = IaisCommonUtils.getList(dtoAppGrpPrimaryDocDtos);
        oldAppGrpPrimaryDocDtos = IaisCommonUtils.getList(oldAppGrpPrimaryDocDtos);
        List<AppGrpPrimaryDocDto> n = PageDataCopyUtil.copyGrpPrimaryDoc(dtoAppGrpPrimaryDocDtos);
        List<AppGrpPrimaryDocDto> o = PageDataCopyUtil.copyGrpPrimaryDoc(oldAppGrpPrimaryDocDtos);
        return !n.equals(o);
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
            if (!appGrpPremisesDto.getAddressWithoutFU().equals(oldAppGrpPremisesDto.getAddressWithoutFU())) {
                return true;
            }
            if (isChangeFloorUnit(appGrpPremisesDto, oldAppGrpPremisesDto)) {
                return true;
            }
        }

        return false;
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
        return !PageDataCopyUtil.copyAppGrpPremisesDtoForAutoField(appGrpPremisesDto).equals(
                PageDataCopyUtil.copyAppGrpPremisesDtoForAutoField(oldAppGrpPremisesDto));
    }

    /*public static List<AppGrpPremisesDto> generateDtosForAutoFields(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList, AppEditSelectDto appEditSelectDto) {
        if (appGrpPremisesDtoList == null || appGrpPremisesDtoList.isEmpty() || oldAppGrpPremisesDtoList == null || oldAppGrpPremisesDtoList.isEmpty()) {
            return null;
        }
        int len = appGrpPremisesDtoList.size();
        if (len != oldAppGrpPremisesDtoList.size()) {
            return oldAppGrpPremisesDtoList;
        }
        List<AppGrpPremisesDto> result = IaisCommonUtils.genNewArrayList(len);
        for (int i = 0; i < len; i++) {
            AppGrpPremisesDto copy = (AppGrpPremisesDto) CopyUtil.copyMutableObject(appGrpPremisesDtoList.get(i));
            AppGrpPremisesDto appGrpPremisesDto = oldAppGrpPremisesDtoList.get(i);
            // re-set not auto fields
            if (appEditSelectDto.isChangeHciName() || appEditSelectDto.isChangeInLocation() || appEditSelectDto.isChangeAddFloorUnit()) {
                copy.setPremisesType(appGrpPremisesDto.getPremisesType());
                // on site or all
                copy.setPostalCode(appGrpPremisesDto.getPostalCode());
                copy.setAddrType(appGrpPremisesDto.getAddrType());
                copy.setBlkNo(appGrpPremisesDto.getBlkNo());
                copy.setFloorNo(appGrpPremisesDto.getFloorNo());
                copy.setUnitNo(appGrpPremisesDto.getUnitNo());
                copy.setStreetName(appGrpPremisesDto.getStreetName());
                copy.setBuildingName(appGrpPremisesDto.getBuildingName());
                copy.setHciName(appGrpPremisesDto.getHciName());
                // off site
                copy.setOffSitePostalCode(appGrpPremisesDto.getOffSitePostalCode());
                copy.setOffSiteAddressType(appGrpPremisesDto.getOffSiteAddressType());
                copy.setOffSiteBlockNo(appGrpPremisesDto.getOffSiteBlockNo());
                copy.setOffSiteFloorNo(appGrpPremisesDto.getOffSiteFloorNo());
                copy.setOffSiteUnitNo(appGrpPremisesDto.getOffSiteUnitNo());
                copy.setOffSiteStreetName(appGrpPremisesDto.getOffSiteStreetName());
                copy.setOffSiteBuildingName(appGrpPremisesDto.getOffSiteBuildingName());
                copy.setOffSiteHciName(appGrpPremisesDto.getOffSiteHciName());
                //EASMTS
                copy.setEasMtsHciName(appGrpPremisesDto.getEasMtsHciName());
                copy.setEasMtsPostalCode(appGrpPremisesDto.getEasMtsPostalCode());
                copy.setEasMtsAddressType(appGrpPremisesDto.getEasMtsAddressType());
                copy.setEasMtsBlockNo(appGrpPremisesDto.getEasMtsBlockNo());
                copy.setEasMtsFloorNo(appGrpPremisesDto.getEasMtsFloorNo());
                copy.setEasMtsUnitNo(appGrpPremisesDto.getEasMtsUnitNo());
                copy.setEasMtsStreetName(appGrpPremisesDto.getEasMtsStreetName());
                copy.setEasMtsBuildingName(appGrpPremisesDto.getEasMtsBuildingName());
                // conveyance
                copy.setConveyancePostalCode(appGrpPremisesDto.getConveyancePostalCode());
                copy.setConveyanceAddressType(appGrpPremisesDto.getConveyanceAddressType());
                copy.setConveyanceBlockNo(appGrpPremisesDto.getConveyanceBlockNo());
                copy.setConveyanceFloorNo(appGrpPremisesDto.getConveyanceFloorNo());
                copy.setConveyanceUnitNo(appGrpPremisesDto.getConveyanceUnitNo());
                copy.setConveyanceStreetName(appGrpPremisesDto.getConveyanceStreetName());
                copy.setConveyanceBuildingName(appGrpPremisesDto.getConveyanceBuildingName());
                copy.setConveyanceHciName(appGrpPremisesDto.getConveyanceHciName());
            }
            if (appEditSelectDto.isChangeAddFloorUnit()) {
                List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtoList =
                        PageDataCopyUtil.copyAppPremisesOperationalUnitDto(appGrpPremisesDto.getAppPremisesOperationalUnitDtos());
                copy.setAppPremisesOperationalUnitDtos(appPremisesOperationalUnitDtoList);
            }
            // re-set other day
            copy.setLicenceDtos(appGrpPremisesDto.getLicenceDtos());
            if (StringUtil.isEmpty(copy.getCertIssuedDtStr()) && StringUtil.isEmpty(appGrpPremisesDto.getCertIssuedDtStr())) {
                copy.setCertIssuedDtStr(appGrpPremisesDto.getCertIssuedDtStr());
            }
            result.add(copy);
        }
        return result;
    }*/

    public static boolean eqServiceChange(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) throws Exception {
        return eqServiceChange(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList, null);
    }

    public static boolean eqServiceChange(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
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

        // allocation
        int flag = isChangeDisciplineAllocation(appSvcRelatedInfoDto, oldAppSvcRelatedInfoDto);
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        List<AppSvcLaboratoryDisciplinesDto> oldAppSvcLaboratoryDisciplinesDtoList = oldAppSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        boolean flag1 = eqAppSvcLaboratoryDisciplines(appSvcLaboratoryDisciplinesDtoList, oldAppSvcLaboratoryDisciplinesDtoList);

        List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
        List<AppSvcDocDto> oldAppSvcDocDtoLit = oldAppSvcRelatedInfoDto.getAppSvcDocDtoLit();
        boolean eqSvcDoc = eqSvcDoc(appSvcDocDtoLit, oldAppSvcDocDtoLit);
        boolean eqAppSvcVehicle = isChangeAppSvcVehicleDto(appSvcRelatedInfoDto.getAppSvcVehicleDtoList(),
                oldAppSvcRelatedInfoDto.getAppSvcVehicleDtoList());
        boolean eqAppSvcChargesPageDto = eqAppSvcChargesPageDto(appSvcRelatedInfoDto.getAppSvcChargesPageDto(),
                oldAppSvcRelatedInfoDto.getAppSvcChargesPageDto());
        boolean changePersonnel = changePersonnel(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList, changeList);
        boolean eqAppSvcBusiness = isChangeAppSvcBusinessDto(appSvcRelatedInfoDto.getAppSvcBusinessDtoList(),
                oldAppSvcRelatedInfoDto.getAppSvcBusinessDtoList());
        if (flag == 1) {
            changeList.add(HcsaConsts.STEP_DISCIPLINE_ALLOCATION);
        }
        if (eqSvcDoc) {
            changeList.add(HcsaConsts.STEP_DOCUMENTS);
        }
        if (eqAppSvcChargesPageDto) {
            changeList.add(HcsaConsts.STEP_CHARGES);
        }
        if (appEditSelectDto != null) {
            List<String> personnelEditList = IaisCommonUtils.getList(appEditSelectDto.getPersonnelEditList());
            personnelEditList.addAll(changeList);
            appEditSelectDto.setPersonnelEditList(personnelEditList);
        }
        if (flag != 0 || !flag1 || eqSvcDoc || eqAppSvcVehicle || eqAppSvcChargesPageDto || changePersonnel || eqAppSvcBusiness) {
            return true;
        }
        return false;
    }

    private static boolean changePersonnel(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList, List<String> changeList) {
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
        boolean changePersonnel = false;
        boolean eqAppSvcClinicalDirector = eqAppSvcClinicalDirector(appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList(),
                oldAppSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList());
        if (eqAppSvcClinicalDirector) {
            //changeList.add(HcsaConsts.STEP_CLINICAL_DIRECTOR);
            changePersonnel = true;
        }
        List<AppSvcPersonnelDto> appSvcPersonnelDtoList = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
        List<AppSvcPersonnelDto> oldAppSvcPersonnelDtoList = oldAppSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
        boolean eqServicePseronnel = isChangeServicePersonnels(appSvcPersonnelDtoList, oldAppSvcPersonnelDtoList);
        if (eqServicePseronnel) {
            changeList.add(HcsaConsts.STEP_SERVICE_PERSONNEL);
            changePersonnel = true;
        }
        if (changePersonnel) {
            return true;
        }
        // section leader
        List<AppSvcPersonnelDto> appSvcSectionLeaderList = appSvcRelatedInfoDto.getAppSvcSectionLeaderList();
        List<AppSvcPersonnelDto> oldAppSvcSectionLeaderList = oldAppSvcRelatedInfoDto.getAppSvcSectionLeaderList();
        boolean eqSectionLeader = isChangeServicePersonnels(appSvcSectionLeaderList, oldAppSvcSectionLeaderList);
        if (eqSectionLeader) {
            //changeList.add(HcsaConsts.STEP_SECTION_LEADER);
            return true;
        }
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtoList = oldAppSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        boolean eqSvcPrincipalOfficers = eqSvcPrincipalOfficers(appSvcPrincipalOfficersDtoList, oldAppSvcPrincipalOfficersDtoList);
        if (eqSvcPrincipalOfficers) {
            //changeList.add(HcsaConsts.STEP_PRINCIPAL_OFFICERS);
            return true;
        }
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList = oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList();
        boolean eqCgo = eqCgo(appSvcCgoDtoList, oldAppSvcCgoDtoList);
        if (eqCgo) {
            //changeList.add(HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS);
            return true;
        }
        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList = oldAppSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
        boolean eqMeadrter = eqMeadrter(appSvcMedAlertPersonList, oldAppSvcMedAlertPersonList);
        if (eqMeadrter) {
            //changeList.add(HcsaConsts.STEP_MEDALERT_PERSON);
            return true;
        }
        // kah
        List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderDtoList = appSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcKeyAppointmentHolderDtoList = oldAppSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList();
        boolean eqKeyAppointmentHolder = eqKeyAppointmentHolder(appSvcKeyAppointmentHolderDtoList,
                oldAppSvcKeyAppointmentHolderDtoList);
        if (eqKeyAppointmentHolder) {
            //changeList.add(HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER);
            return true;
        }
        return false;
    }

    private static boolean eqAppSvcLaboratoryDisciplines(List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList,
            List<AppSvcLaboratoryDisciplinesDto> oldAppSvcLaboratoryDisciplinesDtoList) {
        if (appSvcLaboratoryDisciplinesDtoList != null && oldAppSvcLaboratoryDisciplinesDtoList != null) {
            List<AppSvcChckListDto> list = IaisCommonUtils.genNewArrayList();
            List<AppSvcChckListDto> list1 = IaisCommonUtils.genNewArrayList();
            for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList) {
                List<AppSvcChckListDto> appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                list.addAll(PageDataCopyUtil.copyAppSvcChckListDto(appSvcChckListDtoList));
            }
            for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : oldAppSvcLaboratoryDisciplinesDtoList) {
                List<AppSvcChckListDto> appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                list1.addAll(PageDataCopyUtil.copyAppSvcChckListDto(appSvcChckListDtoList));
            }
            for (AppSvcChckListDto appSvcChckListDto : list) {
                String chkLstConfId = appSvcChckListDto.getChkLstConfId();
                String chkName = appSvcChckListDto.getChkName();
                for (AppSvcChckListDto appSvcChckListDto1 : list1) {
                    String chkLstConfId1 = appSvcChckListDto1.getChkLstConfId();
                    if (chkLstConfId == null && chkLstConfId1 == null) {
                        appSvcChckListDto.setChkName(null);
                        appSvcChckListDto1.setChkName(null);
                    }
                    if (chkLstConfId != null && chkLstConfId.equals(chkLstConfId1)) {
                        appSvcChckListDto1.setChkName(chkName);
                    }
                    if (chkLstConfId1 != null && chkLstConfId1.equals(chkLstConfId)) {
                        appSvcChckListDto1.setChkName(chkName);
                    }
                }
            }
            return list.equals(list1);
        } else {
            return true;
        }
    }

    /**
     * 0: not changed
     * 1: changed with related cgo and sl no changed
     * 2: changed with related cgo no changed and sl changed
     * 3: changed with related cgo changed and sl no changed
     * 4: changed with cgo and sl both changed
     *
     * @param appSvcDisciplineAllocationDtoList
     * @param oldAppSvcDisciplineAllocationDtoList
     * @param cgoList
     * @param slList
     * @return
     */
    private static int isChangeDisciplineAllocation(List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList,
            List<AppSvcDisciplineAllocationDto> oldAppSvcDisciplineAllocationDtoList, List<String> cgoList, List<String> slList) {
        int flag = 0;
        if (appSvcDisciplineAllocationDtoList != null && oldAppSvcDisciplineAllocationDtoList != null) {
            List<AppSvcDisciplineAllocationDto> list1 = PageDataCopyUtil.copyAppSvcDisciplineAllocationDto(
                    appSvcDisciplineAllocationDtoList);
            List<AppSvcDisciplineAllocationDto> list2 = PageDataCopyUtil.copyAppSvcDisciplineAllocationDto(
                    oldAppSvcDisciplineAllocationDtoList);
            flag = list1.equals(list2) ? 0 : 1;
        } else if (appSvcDisciplineAllocationDtoList != null ^ oldAppSvcDisciplineAllocationDtoList != null) {
            flag = 1;
        }
        if (appSvcDisciplineAllocationDtoList != null && flag != 0) {
            boolean newCgo = false;
            if (cgoList != null) {
                newCgo = appSvcDisciplineAllocationDtoList.stream()
                        .anyMatch(dto -> !cgoList.contains(dto.getCgoPerson()));
            }
            boolean newSL = false;
            if (slList != null) {
                newSL = appSvcDisciplineAllocationDtoList.stream()
                        .anyMatch(dto -> !slList.contains(dto.getSectionLeaderName()));
            }
            if (newCgo && newSL) {
                flag = 4;
            } else if (newCgo) {
                flag = 3;
            } else if (newSL) {
                flag = 2;
            }
        }
        log.info(StringUtil.changeForLog("---Change Discipline Allocation Flag: " + flag + "---"));
        return flag;
    }

/*    private static boolean eqServicePseronnel(List<AppSvcPersonnelDto> appSvcPersonnelDtoList,
            List<AppSvcPersonnelDto> oldAppSvcPersonnelDtoList) {
        if (appSvcPersonnelDtoList == null) {
            appSvcPersonnelDtoList = new ArrayList<>();
        }
        if (oldAppSvcPersonnelDtoList == null) {
            oldAppSvcPersonnelDtoList = new ArrayList<>();
        }
        if (!PageDataCopyUtil.copySvcPersonnel(appSvcPersonnelDtoList).equals(
                PageDataCopyUtil.copySvcPersonnel(oldAppSvcPersonnelDtoList))) {
            return true;
        }
        return false;
    }*/

    private static boolean eqSvcPrincipalOfficers(List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList,
            List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtoList) {
        if (appSvcPrincipalOfficersDtoList != null && oldAppSvcPrincipalOfficersDtoList != null) {
            List<AppSvcPrincipalOfficersDto> n = PageDataCopyUtil.copyAppSvcPo(appSvcPrincipalOfficersDtoList);
            List<AppSvcPrincipalOfficersDto> o = PageDataCopyUtil.copyAppSvcPo(oldAppSvcPrincipalOfficersDtoList);
            if (!n.equals(o)) {
                return true;
            }
        } else if (appSvcPrincipalOfficersDtoList == null && oldAppSvcPrincipalOfficersDtoList != null || appSvcPrincipalOfficersDtoList != null && oldAppSvcPrincipalOfficersDtoList == null) {
            return true;
        }
        return false;
    }

    private static boolean eqMeadrter(List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList,
            List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList1) {
        if (appSvcMedAlertPersonList != null && oldAppSvcMedAlertPersonList1 != null) {
            List<AppSvcPrincipalOfficersDto> n = PageDataCopyUtil.copyMedaler(appSvcMedAlertPersonList);
            List<AppSvcPrincipalOfficersDto> o = PageDataCopyUtil.copyMedaler(oldAppSvcMedAlertPersonList1);

            if (!n.equals(o)) {
                return true;
            }
        } else if (appSvcMedAlertPersonList != null && oldAppSvcMedAlertPersonList1 == null || appSvcMedAlertPersonList == null && oldAppSvcMedAlertPersonList1 != null) {
            return true;
        }
        return false;
    }

    private static boolean eqKeyAppointmentHolder(List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderDtoList,
            List<AppSvcPrincipalOfficersDto> oldAppSvcKeyAppointmentHolderDtoList) {
        if (appSvcKeyAppointmentHolderDtoList != null && oldAppSvcKeyAppointmentHolderDtoList != null) {
            List<AppSvcPrincipalOfficersDto> n = PageDataCopyUtil.copyAppSvcKah(appSvcKeyAppointmentHolderDtoList);
            List<AppSvcPrincipalOfficersDto> o = PageDataCopyUtil.copyAppSvcKah(oldAppSvcKeyAppointmentHolderDtoList);
            if (!n.equals(o)) {
                return true;
            }
        } else if (appSvcKeyAppointmentHolderDtoList != null && oldAppSvcKeyAppointmentHolderDtoList == null || appSvcKeyAppointmentHolderDtoList == null && oldAppSvcKeyAppointmentHolderDtoList != null) {
            return true;
        }
        return false;
    }

    private static boolean eqSvcDoc(List<AppSvcDocDto> appSvcDocDtoLit, List<AppSvcDocDto> oldAppSvcDocDtoLit) {
        if (appSvcDocDtoLit == null) {
            appSvcDocDtoLit = new ArrayList<>();
        }
        if (oldAppSvcDocDtoLit == null) {
            oldAppSvcDocDtoLit = new ArrayList<>();
        }
        List<AppSvcDocDto> n = PageDataCopyUtil.copySvcDoc(appSvcDocDtoLit);
        List<AppSvcDocDto> o = PageDataCopyUtil.copySvcDoc(oldAppSvcDocDtoLit);
        if (!o.equals(n)) {
            return true;
        }

        return false;
    }

    private static boolean eqCgo(List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList,
            List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList) {
        if (appSvcCgoDtoList != null && oldAppSvcCgoDtoList != null) {
            List<AppSvcPrincipalOfficersDto> n = PageDataCopyUtil.copyAppSvcCgo(appSvcCgoDtoList);
            List<AppSvcPrincipalOfficersDto> o = PageDataCopyUtil.copyAppSvcCgo(oldAppSvcCgoDtoList);
            if (!n.equals(o)) {
                return true;
            }
        } else if (appSvcCgoDtoList != null && oldAppSvcCgoDtoList == null || appSvcCgoDtoList == null && oldAppSvcCgoDtoList != null) {
            return true;
        }
        return false;
    }

    public static boolean eqHciCode(AppGrpPremisesDto appGrpPremisesDto, AppGrpPremisesDto oldAppGrpPremisesDto) {
        String hciCode = appGrpPremisesDto.getHciCode();
        String oldHciCode = oldAppGrpPremisesDto.getHciCode();
        if (!StringUtil.isEmpty(hciCode)) {
            return hciCode.equals(oldHciCode);
        }
        return true;
    }

    private static boolean eqOperationalUnitDtoList(List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtoList,
            List<AppPremisesOperationalUnitDto> oldAppSubmissionDtoAppGrpPremisesDtoList) {
        if (appPremisesOperationalUnitDtoList == null || oldAppSubmissionDtoAppGrpPremisesDtoList == null) {
            return false;
        }
        int n1 = appPremisesOperationalUnitDtoList.size();
        int n2 = oldAppSubmissionDtoAppGrpPremisesDtoList.size();
        if (n1 != n2) {
            return false;
        }
        oldAppSubmissionDtoAppGrpPremisesDtoList.forEach(dto -> {
            dto.setFloorNo(StringUtil.getNonNull(dto.getFloorNo()));
            dto.setUnitNo(StringUtil.getNonNull(dto.getUnitNo()));
        });
        for (AppPremisesOperationalUnitDto originalDto : appPremisesOperationalUnitDtoList) {
            String floorNo = StringUtil.getNonNull(originalDto.getFloorNo());
            String unitNo = StringUtil.getNonNull(originalDto.getUnitNo());
            if (!oldAppSubmissionDtoAppGrpPremisesDtoList.parallelStream()
                    .anyMatch(dto -> Objects.equals(dto.getUnitNo(), unitNo)
                            && Objects.equals(dto.getFloorNo(), floorNo))) {
                return false;
            }
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
        List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = new ArrayList<>();
        appPremisesOperationalUnitDtos.add(getFirstOpeUnitDto(appGrpPremisesDto));
        appPremisesOperationalUnitDtos.addAll(appGrpPremisesDto.getAppPremisesOperationalUnitDtos());

        List<AppPremisesOperationalUnitDto> oldAppPremisesOperationalUnitDtos = new ArrayList<>();
        oldAppPremisesOperationalUnitDtos.add(getFirstOpeUnitDto(oldAppGrpPremisesDto));
        oldAppPremisesOperationalUnitDtos.addAll(oldAppGrpPremisesDto.getAppPremisesOperationalUnitDtos());

        return !eqOperationalUnitDtoList(appPremisesOperationalUnitDtos, oldAppPremisesOperationalUnitDtos);
    }

    private static AppPremisesOperationalUnitDto getFirstOpeUnitDto(AppGrpPremisesDto appGrpPremisesDto) {
        AppPremisesOperationalUnitDto currDto = new AppPremisesOperationalUnitDto();
        String premisesType = appGrpPremisesDto.getPremisesType();
        String floorNo = null;
        String unitNo = null;
        if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)) {
            floorNo = appGrpPremisesDto.getConveyanceFloorNo();
            unitNo = appGrpPremisesDto.getConveyanceUnitNo();
        } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType)) {
            floorNo = appGrpPremisesDto.getOffSiteFloorNo();
            unitNo = appGrpPremisesDto.getOffSiteUnitNo();
        } else if (ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesType)) {
            floorNo = appGrpPremisesDto.getEasMtsFloorNo();
            unitNo = appGrpPremisesDto.getEasMtsUnitNo();
        }
        if (StringUtil.isEmpty(floorNo) || StringUtil.isEmpty(unitNo)) {
            floorNo = appGrpPremisesDto.getFloorNo();
            unitNo = appGrpPremisesDto.getUnitNo();
        }
        currDto.setFloorNo(floorNo);
        currDto.setUnitNo(unitNo);
        return currDto;
    }

    public static boolean isChangeFloorUnit(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) {
        List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = new ArrayList<>(10);
        appGrpPremisesDtoList.forEach((v) -> {
            appPremisesOperationalUnitDtos.add(getFirstOpeUnitDto(v));
            appPremisesOperationalUnitDtos.addAll(v.getAppPremisesOperationalUnitDtos());
        });
        List<AppPremisesOperationalUnitDto> oldAppPremisesOperationalUnitDtos = new ArrayList<>(10);
        oldAppGrpPremisesDtoList.forEach((v) -> {
            oldAppPremisesOperationalUnitDtos.add(getFirstOpeUnitDto(v));
            oldAppPremisesOperationalUnitDtos.addAll(v.getAppPremisesOperationalUnitDtos());
        });
        return !eqOperationalUnitDtoList(appPremisesOperationalUnitDtos, oldAppPremisesOperationalUnitDtos);
    }

    public static boolean isFloorUnitAllIn(AppGrpPremisesDto appGrpPremisesDto, AppGrpPremisesDto oldAppGrpPremisesDto) {
        if (appGrpPremisesDto == null || oldAppGrpPremisesDto == null) {
            return true;
        }
        List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = new ArrayList<>();
        appPremisesOperationalUnitDtos.add(getFirstOpeUnitDto(appGrpPremisesDto));
        appPremisesOperationalUnitDtos.addAll(appGrpPremisesDto.getAppPremisesOperationalUnitDtos());

        List<AppPremisesOperationalUnitDto> oldAppPremisesOperationalUnitDtos = new ArrayList<>();
        oldAppPremisesOperationalUnitDtos.add(getFirstOpeUnitDto(oldAppGrpPremisesDto));
        oldAppPremisesOperationalUnitDtos.addAll(oldAppGrpPremisesDto.getAppPremisesOperationalUnitDtos());

        int n1 = appPremisesOperationalUnitDtos.size();
        int n2 = oldAppPremisesOperationalUnitDtos.size();
        if (n1 > n2) {
            return false;
        }
        for (AppPremisesOperationalUnitDto originalDto : appPremisesOperationalUnitDtos) {
            if (!oldAppPremisesOperationalUnitDtos.parallelStream()
                    .anyMatch(dto -> Objects.equals(dto.getUnitNo(), originalDto.getUnitNo())
                            && Objects.equals(dto.getFloorNo(), originalDto.getFloorNo()))) {
                return false;
            }
        }
        return true;
    }

    public static boolean compareHciName(AppGrpPremisesDto premisesListQueryDto, AppGrpPremisesDto appGrpPremisesDto) {
        String newHciName = ApplicationHelper.getHciName(appGrpPremisesDto);
        String oldHciName = ApplicationHelper.getHciName(premisesListQueryDto);
        String oldVehicleNo = "";
        if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesListQueryDto.getPremisesType())) {
            oldVehicleNo = premisesListQueryDto.getConveyanceVehicleNo();
        }
        String newVehicleNo = "";
        if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())) {
            newVehicleNo = appGrpPremisesDto.getConveyanceVehicleNo();
        }

        if (!newHciName.equals(oldHciName) || !newVehicleNo.equals(oldVehicleNo)) {
            return false;
        }

        return true;
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
    public static boolean compareLocation(List<AppGrpPremisesDto> appGrpPremisesDtos, List<AppGrpPremisesDto> oldAppGrpPremisesDtos) {
        int length = appGrpPremisesDtos.size();
        int oldLength = oldAppGrpPremisesDtos.size();
        if (length == oldLength) {
            for (int i = 0; i < length; i++) {
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(i);
                AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(i);
                if (!appGrpPremisesDto.getAddressWithoutFU().equals(oldAppGrpPremisesDto.getAddressWithoutFU())
                        || !Objects.equals(StringUtil.getNonNull(appGrpPremisesDto.getConveyanceVehicleNo()),
                        StringUtil.getNonNull(oldAppGrpPremisesDto.getConveyanceVehicleNo()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isChangeAppSvcVehicleDtos(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtos) {
        if (appSvcRelatedInfoDtos == null || oldAppSvcRelatedInfoDtos == null) {
            return true;
        }
        if (appSvcRelatedInfoDtos.size() != oldAppSvcRelatedInfoDtos.size()) {
            return true;
        }
        for (int i = 0, len = appSvcRelatedInfoDtos.size(); i < len; i++) {
            if (isChangeAppSvcVehicleDto(appSvcRelatedInfoDtos.get(i).getAppSvcVehicleDtoList(),
                    oldAppSvcRelatedInfoDtos.get(i).getAppSvcVehicleDtoList())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChangeAppSvcVehicleDto(List<AppSvcVehicleDto> appSvcVehicleDtoList,
            List<AppSvcVehicleDto> oldAppSvcVehicleDtoList) {
        List<AppSvcVehicleDto> n = PageDataCopyUtil.copyAppSvcVehicleDto(appSvcVehicleDtoList);
        List<AppSvcVehicleDto> o = PageDataCopyUtil.copyAppSvcVehicleDto(oldAppSvcVehicleDtoList);
        if (!n.equals(o)) {
            return true;
        }
        return false;
    }

    public static boolean isChangeAppSvcBusinessDtos(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        if (appSvcRelatedInfoDtoList == null || oldAppSvcRelatedInfoDtoList == null) {
            return true;
        }
        if (appSvcRelatedInfoDtoList.size() != oldAppSvcRelatedInfoDtoList.size()) {
            return true;
        }
        List<AppSvcBusinessDto> appSvcBusinessDtoList = IaisCommonUtils.genNewArrayList();
        appSvcRelatedInfoDtoList.forEach((item) -> {
            appSvcBusinessDtoList.addAll(item.getAppSvcBusinessDtoList());
        });
        List<AppSvcBusinessDto> oldAppSvcBusinessDtoList = IaisCommonUtils.genNewArrayList();
        oldAppSvcRelatedInfoDtoList.forEach((item) -> {
            oldAppSvcBusinessDtoList.addAll(item.getAppSvcBusinessDtoList());
        });
        return isChangeAppSvcBusinessDto(appSvcBusinessDtoList, oldAppSvcBusinessDtoList);
    }

    public static boolean isChangeAppSvcBusinessDto(List<AppSvcBusinessDto> appSvcBusinessDtoList,
            List<AppSvcBusinessDto> oldAppSvcBusinessDtoList) {
        List<String> appSvcBusinessNameList = IaisCommonUtils.genNewArrayList();
        appSvcBusinessDtoList.forEach((v) -> {
            appSvcBusinessNameList.add(v.getBusinessName());
        });
        List<String> oldAppSvcBusinessNameList = IaisCommonUtils.genNewArrayList();
        oldAppSvcBusinessDtoList.forEach((v) -> {
            oldAppSvcBusinessNameList.add(v.getBusinessName());
        });
        return !appSvcBusinessNameList.equals(oldAppSvcBusinessNameList);
    }

    public static boolean eqAppSvcChargesPageDto(AppSvcChargesPageDto appSvcChargesPageDto,
            AppSvcChargesPageDto oldAppSvcChargesPageDto) {
        AppSvcChargesPageDto n = PageDataCopyUtil.copyAppSvcClinicalDirector(appSvcChargesPageDto);
        AppSvcChargesPageDto o = PageDataCopyUtil.copyAppSvcClinicalDirector(oldAppSvcChargesPageDto);
        if (!n.equals(o)) {
            return true;
        }
        return false;
    }

    public static boolean eqAppSvcClinicalDirector(List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos,
            List<AppSvcPrincipalOfficersDto> oldAppSvcClinicalDirectorDtos) {
        List<AppSvcPrincipalOfficersDto> n = PageDataCopyUtil.copyAppSvcClinicalDirector(appSvcClinicalDirectorDtos);
        List<AppSvcPrincipalOfficersDto> o = PageDataCopyUtil.copyAppSvcClinicalDirector(oldAppSvcClinicalDirectorDtos);
        if (!n.equals(o)) {
            return true;
        }
        return false;
    }

    public static boolean isChangeSubLicensee(SubLicenseeDto subLicenseeDto, SubLicenseeDto oldSbLicenseeDto) {
        return !Objects.equals(subLicenseeDto, oldSbLicenseeDto);
    }

    public static boolean compareNotChangePersonnel(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto) {
        boolean isAuto = true;
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
        if (appEditSelectDto == null || !appEditSelectDto.isServiceEdit()) {
            return isAuto;
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        List<String> personnelEditList = IaisCommonUtils.genNewArrayList();
        // PO & DPO
        List<AppSvcPrincipalOfficersDto> newAppSvcPrincipalOfficersDto =
                IaisCommonUtils.getList(appSvcRelatedInfoDtoList.getAppSvcPrincipalOfficersDtoList());
        List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDto =
                IaisCommonUtils.getList(oldAppSvcRelatedInfoDtoList.getAppSvcPrincipalOfficersDtoList());
        if (newAppSvcPrincipalOfficersDto.size() > oldAppSvcPrincipalOfficersDto.size()) {
            isAuto = false;
        }
        //change
        List<String> newPoIdNos = IaisCommonUtils.genNewArrayList();
        List<String> oldPoIdNos = IaisCommonUtils.genNewArrayList();
        List<String> newDpoIdNos = IaisCommonUtils.genNewArrayList();
        List<String> olddDpoIdNos = IaisCommonUtils.genNewArrayList();

        for (AppSvcPrincipalOfficersDto item : newAppSvcPrincipalOfficersDto) {
            if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(item.getPsnType())) {
                newPoIdNos.add(ApplicationHelper.getPersonKey(item));
            } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(item.getPsnType())) {
                newDpoIdNos.add(ApplicationHelper.getPersonKey(item));
            }
        }
        for (AppSvcPrincipalOfficersDto item : oldAppSvcPrincipalOfficersDto) {
            if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(item.getPsnType())) {
                oldPoIdNos.add(ApplicationHelper.getPersonKey(item));
            } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(item.getPsnType())) {
                olddDpoIdNos.add(ApplicationHelper.getPersonKey(item));
            }
        }
        if (!newPoIdNos.equals(oldPoIdNos)) {
            isAuto = false;
            personnelEditList.add(HcsaConsts.STEP_PRINCIPAL_OFFICERS);
        }
        if (!newDpoIdNos.equals(olddDpoIdNos)) {
            isAuto = false;
            IaisCommonUtils.addToList(HcsaConsts.STEP_PRINCIPAL_OFFICERS, personnelEditList);
            personnelEditList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
        }
        // CGO
        List<AppSvcPrincipalOfficersDto> newAppSvcCgoDto = IaisCommonUtils.getList(appSvcRelatedInfoDtoList.getAppSvcCgoDtoList());
        List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDto = IaisCommonUtils.getList(oldAppSvcRelatedInfoDtoList.getAppSvcCgoDtoList());
        List<String> newIdNos = IaisCommonUtils.genNewArrayList();
        List<String> oldIdNos = IaisCommonUtils.genNewArrayList();
        for (AppSvcPrincipalOfficersDto item : newAppSvcCgoDto) {
            newIdNos.add(ApplicationHelper.getPersonKey(item));
        }
        for (AppSvcPrincipalOfficersDto item : oldAppSvcCgoDto) {
            oldIdNos.add(ApplicationHelper.getPersonKey(item));
        }
        if (!newIdNos.equals(oldIdNos)) {
            isAuto = false;
            personnelEditList.add(HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS);
        }
        // CD
        List<AppSvcPrincipalOfficersDto> newAppSvcCdDto = IaisCommonUtils.getList(
                appSvcRelatedInfoDtoList.getAppSvcClinicalDirectorDtoList());
        List<AppSvcPrincipalOfficersDto> oldAppSvcCdDto = IaisCommonUtils.getList(
                oldAppSvcRelatedInfoDtoList.getAppSvcClinicalDirectorDtoList());
        newIdNos = IaisCommonUtils.genNewArrayList();
        oldIdNos = IaisCommonUtils.genNewArrayList();
        for (AppSvcPrincipalOfficersDto item : newAppSvcCdDto) {
            newIdNos.add(ApplicationHelper.getPersonKey(item));
        }
        for (AppSvcPrincipalOfficersDto item : oldAppSvcCdDto) {
            oldIdNos.add(ApplicationHelper.getPersonKey(item));
        }
        if (!newIdNos.equals(oldIdNos)) {
            isAuto = false;
            personnelEditList.add(HcsaConsts.STEP_CLINICAL_DIRECTOR);
        }
        // MAP
        List<AppSvcPrincipalOfficersDto> newAppSvcMapDto = IaisCommonUtils.getList(
                appSvcRelatedInfoDtoList.getAppSvcMedAlertPersonList());
        List<AppSvcPrincipalOfficersDto> oldAppSvcMapDto = IaisCommonUtils.getList(
                oldAppSvcRelatedInfoDtoList.getAppSvcMedAlertPersonList());
        newIdNos = IaisCommonUtils.genNewArrayList();
        oldIdNos = IaisCommonUtils.genNewArrayList();
        for (AppSvcPrincipalOfficersDto item : newAppSvcMapDto) {
            newIdNos.add(ApplicationHelper.getPersonKey(item));
        }
        for (AppSvcPrincipalOfficersDto item : oldAppSvcMapDto) {
            oldIdNos.add(ApplicationHelper.getPersonKey(item));
        }
        if (!newIdNos.equals(oldIdNos)) {
            isAuto = false;
            personnelEditList.add(HcsaConsts.STEP_MEDALERT_PERSON);
        }
        // disciplines
        List<AppSvcLaboratoryDisciplinesDto> newDisciplinesDto = appSvcRelatedInfoDtoList.getAppSvcLaboratoryDisciplinesDtoList();
        List<AppSvcLaboratoryDisciplinesDto> oldDisciplinesDto = oldAppSvcRelatedInfoDtoList.getAppSvcLaboratoryDisciplinesDtoList();
        if (!IaisCommonUtils.isEmpty(newDisciplinesDto) && !IaisCommonUtils.isEmpty(oldDisciplinesDto)) {
            for (AppSvcLaboratoryDisciplinesDto item : newDisciplinesDto) {
                String hciName = item.getPremiseVal();
                AppSvcLaboratoryDisciplinesDto oldAppSvcLaboratoryDisciplinesDto = getDisciplinesDto(oldDisciplinesDto, hciName);
                List<AppSvcChckListDto> newCheckList = item.getAppSvcChckListDtoList();
                List<AppSvcChckListDto> oldCheckList = oldAppSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                if (!IaisCommonUtils.isEmpty(newCheckList) && !IaisCommonUtils.isEmpty(oldCheckList)) {
                    List<String> oldCheckListIds = IaisCommonUtils.genNewArrayList();
                    for (AppSvcChckListDto checBox : oldCheckList) {
                        oldCheckListIds.add(checBox.getChkLstConfId());
                    }
                    for (AppSvcChckListDto checBox : newCheckList) {
                        if (!oldCheckListIds.contains(checBox.getChkLstConfId())) {
                            isAuto = false;
                            personnelEditList.add(HcsaConsts.STEP_LABORATORY_DISCIPLINES);
                            IaisCommonUtils.addToList(HcsaConsts.STEP_DISCIPLINE_ALLOCATION, personnelEditList);
                            break;
                        }
                    }
                } else if (!IaisCommonUtils.isEmpty(newCheckList) || !IaisCommonUtils.isEmpty(oldCheckList)) {
                    isAuto = false;
                    personnelEditList.add(HcsaConsts.STEP_LABORATORY_DISCIPLINES);
                    IaisCommonUtils.addToList(HcsaConsts.STEP_DISCIPLINE_ALLOCATION, personnelEditList);
                }
            }
        } else if (!IaisCommonUtils.isEmpty(newDisciplinesDto) || !IaisCommonUtils.isEmpty(oldDisciplinesDto)) {
            isAuto = false;
            personnelEditList.add(HcsaConsts.STEP_LABORATORY_DISCIPLINES);
            IaisCommonUtils.addToList(HcsaConsts.STEP_DISCIPLINE_ALLOCATION, personnelEditList);
        }
        // KAH
        List<AppSvcPrincipalOfficersDto> kahList = IaisCommonUtils.getList(
                appSvcRelatedInfoDtoList.getAppSvcKeyAppointmentHolderDtoList());
        List<AppSvcPrincipalOfficersDto> oldKahList = IaisCommonUtils.getList(
                oldAppSvcRelatedInfoDtoList.getAppSvcKeyAppointmentHolderDtoList());
        List<String> newKahIdNos = IaisCommonUtils.genNewArrayList();
        List<String> oldKahIdNos = IaisCommonUtils.genNewArrayList();
        for (AppSvcPrincipalOfficersDto item : kahList) {
            newKahIdNos.add(ApplicationHelper.getPersonKey(item));
        }
        for (AppSvcPrincipalOfficersDto item : oldKahList) {
            oldKahIdNos.add(ApplicationHelper.getPersonKey(item));
        }
        if (!newKahIdNos.equals(oldKahIdNos)) {
            isAuto = false;
            personnelEditList.add(HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER);
        }
        appEditSelectDto.setPersonnelEditList(personnelEditList);
        appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
        return isAuto;
    }

    private static AppSvcLaboratoryDisciplinesDto getDisciplinesDto(List<AppSvcLaboratoryDisciplinesDto> disciplinesDto,
            String hciName) {
        for (AppSvcLaboratoryDisciplinesDto iten : disciplinesDto) {
            if (hciName.equals(iten.getPremiseVal())) {
                return iten;
            }
        }
        return new AppSvcLaboratoryDisciplinesDto();
    }

    /*public static AppSubmissionDto generateDtosForAutoPremesis(AppSubmissionDto srcDto, List<AppGrpPremisesDto> autoPremisesDtos,
            String autoGroupNo) {
        AppSubmissionDto autoDto = (AppSubmissionDto) CopyUtil.copyMutableObject(srcDto);
        AppEditSelectDto newChangeSelectDto = new AppEditSelectDto();
        newChangeSelectDto.setPremisesEdit(true);
        newChangeSelectDto.setPremisesListEdit(true);
        autoDto.setAppGrpPremisesDtoList(ApplicationHelper.updatePremisesIndex(
                (List<AppGrpPremisesDto>) CopyUtil.copyMutableObjectList(autoPremisesDtos),
                autoDto.getAppGrpPremisesDtoList()));
        autoDto.setChangeSelectDto(newChangeSelectDto);
        autoDto.setAppGrpStatus(null);
        autoDto.setAmount(0.0);
        ApplicationHelper.reSetAdditionalFields(autoDto, newChangeSelectDto, autoGroupNo);
        return autoDto;
    }*/

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
        if (servicePersonnelList == null && oldServicePersonnelListList == null) {
            return false;
        } else if (servicePersonnelList == null ^ oldServicePersonnelListList == null) {
            return true;
        }
        if (servicePersonnelList.size() != oldServicePersonnelListList.size()) {
            return true;
        }
        List<AppSvcPersonnelDto> o1 = PageDataCopyUtil.copySvcPersonnels(servicePersonnelList);
        List<AppSvcPersonnelDto> o2 = PageDataCopyUtil.copySvcPersonnels(oldServicePersonnelListList);
        return !o1.equals(o2);
    }

    private static boolean isChangeNotAutoDataForAllocation(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,
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
            int flag = isChangeDisciplineAllocation(appSvcRelatedInfoDtos.get(i), oldAppSvcRelatedInfoDtos.get(i));
            if (flag != 0 && flag != 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 0: not changed
     * 1: changed with related cgo and sl no changed
     * 2: changed with related cgo no changed and sl changed
     * 3: changed with related cgo changed and sl no changed
     * 4: changed with cgo and sl both changed
     * 5: changed with svc info
     *
     * @param appSvcRelatedInfoDto
     * @param oldAppSvcRelatedInfoDto
     * @return
     */
    private static int isChangeDisciplineAllocation(AppSvcRelatedInfoDto appSvcRelatedInfoDto,
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto) {
        if (appSvcRelatedInfoDto == null && oldAppSvcRelatedInfoDto == null) {
            return 0;
        } else if (appSvcRelatedInfoDto == null ^ oldAppSvcRelatedInfoDto == null) {
            return 5;
        }
        List<String> cgoList = null;
        if (oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList() != null) {
            cgoList = oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList().stream()
                    .map(dto -> ApplicationHelper.getPersonKey(dto))
                    .collect(Collectors.toList());
        }
        List<String> slList = null;
        if (oldAppSvcRelatedInfoDto.getAppSvcSectionLeaderList() != null) {
            slList = oldAppSvcRelatedInfoDto.getAppSvcSectionLeaderList().stream()
                    .map(AppSvcPersonnelDto::getName)
                    .collect(Collectors.toList());
        }
        return isChangeDisciplineAllocation(appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList(),
                oldAppSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList(), cgoList, slList);
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
                        AppSvcPrincipalOfficersDto newDto = (AppSvcPrincipalOfficersDto) CopyUtil.copyMutableObject(source);
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

    /*
     * if svc doc have primary doc ,change doc -> primary doc (only rfc ,renew now)
     * */
    public static void svcDocToPresmise(AppSubmissionDto appSubmissionDto) {
        if (appSubmissionDto == null) {
            return;
        }
        ConfigCommService configCommService = getConfigCommService();
        String premisesIndexNo = Optional.of(appSubmissionDto.getAppGrpPremisesDtoList())
                .filter(list -> list.size() > 0)
                .map(list -> list.get(0))
                .map(AppGrpPremisesDto::getPremisesIndexNo)
                .orElse("");
        String premisesType = Optional.of(appSubmissionDto.getAppGrpPremisesDtoList())
                .filter(list -> list.size() > 0)
                .map(list -> list.get(0))
                .map(AppGrpPremisesDto::getPremisesType)
                .orElse("");
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
        List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = IaisCommonUtils.genNewArrayList();
        List<AppSvcDocDto> appSvcDocDtos = IaisCommonUtils.genNewArrayList();
        List<AppSvcDocDto> deleteSvcDoc = IaisCommonUtils.genNewArrayList();
        List<AppGrpPrimaryDocDto> deletePrimary = IaisCommonUtils.genNewArrayList();
        if (appSvcDocDtoLit != null) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtoLit) {
                String svcDocId = appSvcDocDto.getSvcDocId();
                String fileRepoId = appSvcDocDto.getFileRepoId();
                if (StringUtil.isEmpty(svcDocId)) {
                    deleteSvcDoc.add(appSvcDocDto);
                    continue;
                }
                if (StringUtil.isEmpty(fileRepoId)) {
                    deleteSvcDoc.add(appSvcDocDto);
                    continue;
                }
                HcsaSvcDocConfigDto entity = configCommService.getHcsaSvcDocConfigDtoById(svcDocId);
                if (entity != null) {
                    String serviceId = entity.getServiceId();
                    if (StringUtil.isEmpty(serviceId)) {// the current is primary document
                        AppGrpPrimaryDocDto appGrpPrimaryDocDto = new AppGrpPrimaryDocDto();
                        appGrpPrimaryDocDto.setSvcDocId(svcDocId);
                        appGrpPrimaryDocDto.setSvcComDocId(svcDocId);
                        appGrpPrimaryDocDto.setSvcComDocName(entity.getDocTitle());
                        appGrpPrimaryDocDto.setDocName(appSvcDocDto.getDocName());
                        appGrpPrimaryDocDto.setAppGrpId(appSubmissionDto.getAppGrpId());
                        appGrpPrimaryDocDto.setDocSize(appSvcDocDto.getDocSize());
                        appGrpPrimaryDocDto.setFileRepoId(appSvcDocDto.getFileRepoId());
                        appGrpPrimaryDocDto.setPassValidate(appSvcDocDto.isPassValidate());
                        appGrpPrimaryDocDto.setMd5Code(appSvcDocDto.getMd5Code());
                        appGrpPrimaryDocDto.setVersion(appSvcDocDto.getVersion());
                        appGrpPrimaryDocDto.setSeqNum(appSvcDocDto.getSeqNum());
                        if ("1".equals(entity.getDupForPrem())) {
                            appGrpPrimaryDocDto.setPremisessName(premisesIndexNo);
                            appGrpPrimaryDocDto.setPremisessType(premisesType);
                        }
                        appGrpPrimaryDocDtos.add(appGrpPrimaryDocDto);
                        appSvcDocDtos.add(appSvcDocDto);
                    } else {
                        appSvcDocDto.setUpFileName(entity.getDocTitle());
                    }
                }
            }
            appSvcDocDtoLit.removeAll(deleteSvcDoc);
            // remove the primary documents
            appSvcDocDtoLit.removeAll(appSvcDocDtos);
            for (int i = 0; i < appSvcDocDtoLit.size(); i++) {
                for (int j = 0; j < appSvcDocDtoLit.size() && j != i; j++) {
                    if (appSvcDocDtoLit.get(i).getFileRepoId().equals(appSvcDocDtoLit.get(j).getFileRepoId())) {
                        appSvcDocDtoLit.remove(appSvcDocDtoLit.get(i));
                        i--;
                        break;
                    }
                }
            }
        }
        if (dtoAppGrpPrimaryDocDtos != null) {
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : dtoAppGrpPrimaryDocDtos) {
                String fileRepoId = appGrpPrimaryDocDto.getFileRepoId();
                if (StringUtil.isEmpty(fileRepoId)) {
                    deletePrimary.add(appGrpPrimaryDocDto);
                    continue;
                }
                if (StringUtil.isEmpty(appGrpPrimaryDocDto.getSvcComDocName())) {
                    String svcDocId = appGrpPrimaryDocDto.getSvcDocId();
                    if (svcDocId != null) {
                        HcsaSvcDocConfigDto hcsaSvcDocConfigDto = configCommService.getHcsaSvcDocConfigDtoById(svcDocId);
                        if (hcsaSvcDocConfigDto != null) {
                            appGrpPrimaryDocDto.setSvcComDocName(hcsaSvcDocConfigDto.getDocTitle());
                        }
                    }
                }
            }
            dtoAppGrpPrimaryDocDtos.removeAll(deletePrimary);
            if (appGrpPrimaryDocDtos.isEmpty()) {
                appGrpPrimaryDocDtos.addAll(dtoAppGrpPrimaryDocDtos);
            } else {
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = IaisCommonUtils.genNewArrayList();
                for (AppGrpPrimaryDocDto appGrpPrimaryDocDto1 : dtoAppGrpPrimaryDocDtos) {
                    for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos) {
                        String svcComDocName = appGrpPrimaryDocDto.getSvcComDocName();
                        String svcComDocName1 = appGrpPrimaryDocDto1.getSvcComDocName();
                        if (svcComDocName1 != null) {
                            if (svcComDocName1.equals(svcComDocName)) {
                                continue;
                            } else {
                                appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto1);
                            }
                        } else if (svcComDocName != null) {
                            if (svcComDocName.equals(svcComDocName1)) {
                                continue;
                            } else {
                                appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto1);
                            }
                        }
                    }
                }
                appGrpPrimaryDocDtos.addAll(appGrpPrimaryDocDtoList);
            }
        }
        // check seq num and remove duplicate the documents
        int maxSeqNum = ApplicationHelper.getMaxFileIndex(appSubmissionDto.getMaxFileIndex());
        Integer seqNum = appGrpPrimaryDocDtos.stream()
                .map(AppGrpPrimaryDocDto::getSeqNum)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(-1);
        if (seqNum >= maxSeqNum) {
            maxSeqNum = seqNum + 1;
        }
        for (int i = 0; i < appGrpPrimaryDocDtos.size(); i++) {
            if (appGrpPrimaryDocDtos.get(i).getSeqNum() == null) {
                appGrpPrimaryDocDtos.get(i).setSeqNum(maxSeqNum++);
            }
            for (int j = 0; j < appGrpPrimaryDocDtos.size() && j != i; j++) {
                if (appGrpPrimaryDocDtos.get(i).getFileRepoId().equals(appGrpPrimaryDocDtos.get(j).getFileRepoId())) {
                    appGrpPrimaryDocDtos.remove(appGrpPrimaryDocDtos.get(i));
                    i--;
                    break;
                }
            }
        }
        ApplicationHelper.reSetMaxFileIndex(maxSeqNum);
        appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);
    }

    public static void premisesDocToSvcDoc(AppSubmissionDto appSubmissionDto) {
        log.debug(StringUtil.changeForLog("do premisesDocToSvcDoc start ..."));
        //handler primary doc
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = handlerPrimaryDoc(
                appSubmissionDto.getAppGrpPremisesDtoList(), appSubmissionDto.getAppGrpPrimaryDocDtos());
        appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);

//        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDtoByLicenceId.getAppGrpPrimaryDocDtos();
        List<AppSvcDocDto> appSvcDocDtoLits = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getAppSvcDocDtoLit();
        List<AppSvcDocDto> removeList = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(appSvcDocDtoLits)) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtoLits) {
                String svcDocId = appSvcDocDto.getSvcDocId();
                if (StringUtil.isEmpty(svcDocId)) {
                    removeList.add(appSvcDocDto);
                    continue;
                }
                HcsaSvcDocConfigDto entity = getConfigCommService().getHcsaSvcDocConfigDtoById(svcDocId);
                if (StringUtil.isEmpty(entity.getServiceId())) {
                    removeList.add(appSvcDocDto);
                }
            }
            appSvcDocDtoLits.removeAll(removeList);
        }

        List<AppSvcDocDto> appSvcDocDtoList = IaisCommonUtils.genNewArrayList();
        if (appGrpPrimaryDocDtos != null) {
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos) {
                AppSvcDocDto appSvcDocDto = MiscUtil.transferEntityDto(appGrpPrimaryDocDto, AppSvcDocDto.class);
                appSvcDocDto.setSvcDocId(appGrpPrimaryDocDto.getSvcComDocId());
                appSvcDocDto.setDocName(appGrpPrimaryDocDto.getDocName());
                appSvcDocDto.setVersion(appGrpPrimaryDocDto.getVersion());
                appSvcDocDtoList.add(appSvcDocDto);
            }
            appSubmissionDto.setAppGrpPrimaryDocDtos(null);
        }
        List<AppSvcDocDto> appSvcDocDtoLit = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getAppSvcDocDtoLit();
        if (appSvcDocDtoLit != null) {
            appSvcDocDtoList.addAll(appSvcDocDtoLit);
        }
        appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).setAppSvcDocDtoLit(appSvcDocDtoList);
        setRelatedInfoBaseServiceId(appSubmissionDto);
    }

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
            if (ApplicationConsts.SERVICE_TYPE_BASE.equals(svcType)) {
                var1.setBaseServiceId(activeHcsaServiceDtoByName.getId());
            } else if (ApplicationConsts.SERVICE_TYPE_SPECIFIED.equals(svcType)) {
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
                            ApplicationConsts.SERVICE_TYPE_SPECIFIED, licenceId);
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
        }
    }

    public static void oldPremiseToNewPremise(AppSubmissionDto appSubmissionDto) {
        if (appSubmissionDto != null) {
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
            if (appGrpPremisesDtoList != null) {
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                    String premisesIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                    if (appSvcLaboratoryDisciplinesDtoList != null) {
                        for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList) {
                            String premiseVal = appSvcLaboratoryDisciplinesDto.getPremiseVal();
                            if (!premisesIndexNo.equals(premiseVal)) {
                                appSvcLaboratoryDisciplinesDto.setPremiseVal(premisesIndexNo);
                            }
                        }

                    }
                }
            }
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
        AppSvcRelatedInfoDto newDto = (AppSvcRelatedInfoDto) CopyUtil.copyMutableObject(currSvcInfoDto);
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
            } else if (HcsaConsts.STEP_LABORATORY_DISCIPLINES.equals(step)) {
                newDto.setAppSvcLaboratoryDisciplinesDtoList(
                        (List<AppSvcLaboratoryDisciplinesDto>) CopyUtil.copyMutableObjectList(
                                oldSvcInfoDto.getAppSvcLaboratoryDisciplinesDtoList()));
            } else if (HcsaConsts.STEP_DISCIPLINE_ALLOCATION.equals(step)) {
                newDto.setAppSvcDisciplineAllocationDtoList(
                        (List<AppSvcDisciplineAllocationDto>) CopyUtil.copyMutableObjectList(
                                oldSvcInfoDto.getAppSvcDisciplineAllocationDtoList()));
            } else if (HcsaConsts.STEP_PRINCIPAL_OFFICERS.equals(step)) {
                reSetPersonnels(oldSvcInfoDto, newDto, step, changeList.contains(ApplicationConsts.PERSONNEL_PSN_TYPE_PO));
            } else if (HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(step)) {
                if (changeList.contains(HcsaConsts.STEP_DISCIPLINE_ALLOCATION) && !stepList.contains(
                        HcsaConsts.STEP_DISCIPLINE_ALLOCATION)) {
                    newDto.setAppSvcDisciplineAllocationDtoList(reSetAllocation(newDto, oldSvcInfoDto));
                }
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

    private static List<AppSvcDisciplineAllocationDto> reSetAllocation(AppSvcRelatedInfoDto newDto,
            AppSvcRelatedInfoDto oldSvcInfoDto) {
        List<AppSvcDisciplineAllocationDto> newAllocations = newDto.getAppSvcDisciplineAllocationDtoList();
        if (newAllocations == null || newAllocations.isEmpty() || oldSvcInfoDto.getAppSvcCgoDtoList() == null) {
            return newAllocations;
        }
        boolean isOld = true;
        for (AppSvcDisciplineAllocationDto dto : newAllocations) {
            isOld = oldSvcInfoDto.getAppSvcCgoDtoList().stream().anyMatch(cgo -> Objects.equals(dto.getCgoPerson(),
                    ApplicationHelper.getPersonKey(cgo)));
            if (!isOld) {
                break;
            }
        }
        if (!isOld) {
            return (List<AppSvcDisciplineAllocationDto>) CopyUtil.copyMutableObjectList(
                    oldSvcInfoDto.getAppSvcDisciplineAllocationDtoList());
        }
        return newAllocations;
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

    public static List<AppGrpPrimaryDocDto> handlerPrimaryDoc(List<AppGrpPremisesDto> appGrpPremisesDtos,
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos) {
        log.debug(StringUtil.changeForLog("do handlerPrimaryDoc start ..."));
        if (IaisCommonUtils.isEmpty(appGrpPremisesDtos) || IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)) {
            return appGrpPrimaryDocDtos;
        }
        log.debug(StringUtil.changeForLog("appGrpPrimaryDocDtos size:" + appGrpPrimaryDocDtos.size()));
        //remove empty doc
        List<AppGrpPrimaryDocDto> notEmptyDocList = IaisCommonUtils.genNewArrayList();
        for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos) {
            if (!StringUtil.isEmpty(appGrpPrimaryDocDto.getFileRepoId())) {
                notEmptyDocList.add(appGrpPrimaryDocDto);
            }
        }
        // log.debug(StringUtil.changeForLog("notEmptyDocList size:" +  notEmptyDocList.size()));
        //add empty doc
        ConfigCommService configCommService = getConfigCommService();
        List<HcsaSvcDocConfigDto> docConfigDtos = configCommService.getAllHcsaSvcDocs(null);
        List<AppGrpPrimaryDocDto> newPrimaryDocList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(docConfigDtos)) {
            log.debug(StringUtil.changeForLog("hcsaSvcDocDtos not empty ..."));
            log.debug(StringUtil.changeForLog("hcsa svc doc config dto size:" + docConfigDtos.size()));
            if (notEmptyDocList != null && notEmptyDocList.size() > 0) {
                List<HcsaSvcDocConfigDto> oldHcsaSvcDocDtos = configCommService.getPrimaryDocConfigById(
                        notEmptyDocList.get(0).getSvcComDocId());
                log.debug(StringUtil.changeForLog("oldHcsaSvcDocDtos:" + JsonUtil.parseToJson(oldHcsaSvcDocDtos)));
                for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : docConfigDtos) {
                    String docTitle = hcsaSvcDocConfigDto.getDocTitle();
                    String dupPrem = hcsaSvcDocConfigDto.getDupForPrem();
                    int i = 0;
                    for (HcsaSvcDocConfigDto oldHcsaSvcDocDto : oldHcsaSvcDocDtos) {
                        if (docTitle.equals(oldHcsaSvcDocDto.getDocTitle())) {
                            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = ApplicationHelper.getAppGrpprimaryDocDto(
                                    oldHcsaSvcDocDto.getId(), notEmptyDocList);
                            if (IaisCommonUtils.isEmpty(appGrpPrimaryDocDtoList)) {
                                AppGrpPrimaryDocDto appGrpPrimaryDocDto = ApplicationHelper.genEmptyPrimaryDocDto(
                                        hcsaSvcDocConfigDto.getId());
                                handlerDupPremDoc(dupPrem, appGrpPrimaryDocDto, appGrpPremisesDtos, newPrimaryDocList);
                            } else {
                                for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtoList) {
                                    appGrpPrimaryDocDto.setSvcComDocId(hcsaSvcDocConfigDto.getId());
                                    // handlerDupPremDoc(dupPrem,appGrpPrimaryDocDto,appGrpPremisesDtos,newPrimaryDocList);
                                    newPrimaryDocList.add(appGrpPrimaryDocDto);
                                }
                            }
                            break;
                        }
                        if (i == oldHcsaSvcDocDtos.size()) {
                            //add empty doc
                            AppGrpPrimaryDocDto appGrpPrimaryDocDto = ApplicationHelper.genEmptyPrimaryDocDto(
                                    hcsaSvcDocConfigDto.getId());
                            newPrimaryDocList.add(appGrpPrimaryDocDto);
                        }
                        i++;
                    }
                }
            } else {
                for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : docConfigDtos) {
                    AppGrpPrimaryDocDto appGrpPrimaryDocDto = ApplicationHelper.genEmptyPrimaryDocDto(hcsaSvcDocConfigDto.getId());
                    newPrimaryDocList.add(appGrpPrimaryDocDto);
                }
            }
        }
        log.info(StringUtil.changeForLog("newPrimaryDocList size:" + newPrimaryDocList.size()));
        log.debug(StringUtil.changeForLog("newPrimaryDocList:" + JsonUtil.parseToJson(newPrimaryDocList)));
        log.info(StringUtil.changeForLog("do handlerPrimaryDoc end ..."));
        return newPrimaryDocList;
    }

    private static void handlerDupPremDoc(String dupPrem, AppGrpPrimaryDocDto targetDto, List<AppGrpPremisesDto> appGrpPremisesDtos,
            List<AppGrpPrimaryDocDto> newPrimaryDocList) {
        if (AppConsts.NO.equals(dupPrem)) {
            newPrimaryDocList.add(targetDto);
        } else if (AppConsts.YES.equals(dupPrem)) {
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                targetDto.setPremisessName(appGrpPremisesDto.getPremisesIndexNo());
                targetDto.setPremisessType(appGrpPremisesDto.getPremisesType());
                newPrimaryDocList.add(targetDto);
            }
        }
    }

    public static List<AppGrpPrimaryDocDto> syncPrimaryDoc(String appType, boolean isRfi,
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos, List<HcsaSvcDocConfigDto> primaryDocConfig) {
        log.debug(StringUtil.changeForLog("do syncPrimaryDoc start ..."));
        log.debug(StringUtil.changeForLog("appType:" + appType));
        log.debug(StringUtil.changeForLog("isRfi:" + isRfi));
        List<AppGrpPrimaryDocDto> newGrpPrimaryDocList = IaisCommonUtils.genNewArrayList();
        //rfc/renew for primary doc
        boolean rfcOrRenwOrNew = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(
                appType) || ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType);
        if (!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos) && !IaisCommonUtils.isEmpty(primaryDocConfig)) {
            List<String> docConfigIds = IaisCommonUtils.genNewArrayList();
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos) {
                docConfigIds.add(appGrpPrimaryDocDto.getSvcComDocId());
            }
            log.debug(StringUtil.changeForLog("docConfigIds size:" + docConfigIds.size()));
            List<HcsaSvcDocConfigDto> oldHcsaSvcDocConfigDtos = getConfigCommService().getPrimaryDocConfigByIds(docConfigIds);
            if (!IaisCommonUtils.isEmpty(oldHcsaSvcDocConfigDtos)) {
                if (rfcOrRenwOrNew && !isRfi) {
                    for (HcsaSvcDocConfigDto oldDocConfig : oldHcsaSvcDocConfigDtos) {
                        for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos) {
                            if (oldDocConfig.getId().equals(appGrpPrimaryDocDto.getSvcComDocId())) {
                                String oldDocTitle = oldDocConfig.getDocTitle();
                                log.debug(StringUtil.changeForLog("old doc title:" + oldDocTitle));
                                for (HcsaSvcDocConfigDto docConfig : primaryDocConfig) {
                                    String newConfigDocTitle = docConfig.getDocTitle();
                                    log.debug(StringUtil.changeForLog("new config doc title:" + newConfigDocTitle));
                                    if (newConfigDocTitle.equals(oldDocTitle)) {
                                        AppGrpPrimaryDocDto newGrpPrimaryDoc = (AppGrpPrimaryDocDto) CopyUtil.copyMutableObject(
                                                appGrpPrimaryDocDto);
                                        newGrpPrimaryDoc.setSvcComDocId(docConfig.getId());
                                        newGrpPrimaryDoc.setSvcComDocName(docConfig.getDocTitle());
                                        //newGrpPrimaryDoc.setDocConfigVersion(docConfig.getVersion());
                                        newGrpPrimaryDocList.add(newGrpPrimaryDoc);
                                        //break;
                                    }
                                }
                                //break;
                            }
                        }
                    }
                } else if (isRfi) {
                    log.debug(StringUtil.changeForLog("set doc title for rfi ..."));
                    //set title name
                    for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos) {
                        for (HcsaSvcDocConfigDto oldDocConfig : oldHcsaSvcDocConfigDtos) {
                            if (oldDocConfig.getId().equals(appGrpPrimaryDocDto.getSvcComDocId())) {
                                AppGrpPrimaryDocDto newGrpPrimaryDoc = (AppGrpPrimaryDocDto) CopyUtil.copyMutableObject(
                                        appGrpPrimaryDocDto);
                                newGrpPrimaryDoc.setSvcComDocName(oldDocConfig.getDocTitle());
                                //newGrpPrimaryDoc.setDocConfigVersion(oldDocConfig.getVersion());
                                newGrpPrimaryDocList.add(newGrpPrimaryDoc);
                                break;
                            }
                        }
                    }
                }
            }
        }
        log.debug(StringUtil.changeForLog("do syncPrimaryDoc end ..."));
        return newGrpPrimaryDocList;
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

}
