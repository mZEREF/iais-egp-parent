package com.ecquaria.cloud.moh.iais.rfcutil;

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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class EqRequestForChangeSubmitResultChange {

    public static boolean eqHciNameChange(AppGrpPremisesDto appGrpPremisesDto ,AppGrpPremisesDto oldAppGrpPremisesDto) {
        String hciName = NewApplicationHelper.getHciName(appGrpPremisesDto);
        String oldHciName = NewApplicationHelper.getHciName(oldAppGrpPremisesDto);
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

    public static boolean eqDocChange(List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos, List<AppGrpPrimaryDocDto> oldAppGrpPrimaryDocDtos){
        if (dtoAppGrpPrimaryDocDtos != null && oldAppGrpPrimaryDocDtos == null || dtoAppGrpPrimaryDocDtos == null && oldAppGrpPrimaryDocDtos != null) {
            return true;
        } else if (dtoAppGrpPrimaryDocDtos != null && oldAppGrpPrimaryDocDtos != null) {
            List<AppGrpPrimaryDocDto> n=PageDataCopyUtil.copyGrpPrimaryDoc(dtoAppGrpPrimaryDocDtos);
            List<AppGrpPrimaryDocDto> o=PageDataCopyUtil.copyGrpPrimaryDoc(oldAppGrpPrimaryDocDtos);
            if (!n.equals(o)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isChangeGrpPremises(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) {
        if (appGrpPremisesDtoList == null || oldAppGrpPremisesDtoList == null) {
            return false;
        }
         if( IaisCommonUtils.listChange(appGrpPremisesDtoList,oldAppGrpPremisesDtoList)){
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
        if(IaisCommonUtils.listChange(appGrpPremisesDtoList,oldAppGrpPremisesDtoList)){
            return true;
        }
        for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
            AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtoList.get(i);
            AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtoList.get(i);
            if(isChangeGrpPremisesAutoFields(appGrpPremisesDto, oldAppGrpPremisesDto)){
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

    public static boolean eqServiceChange(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) throws Exception {
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

        List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        List<AppSvcDisciplineAllocationDto> oldAppSvcDisciplineAllocationDtoList = oldAppSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        // allocation
        List<String> cgoList = null;
        if (oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList() != null) {
            cgoList = oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList().stream()
                    .map(dto -> NewApplicationHelper.getPersonKey(dto))
                    .collect(Collectors.toList());
        }
        List<String> slList = null;
        if (oldAppSvcRelatedInfoDto.getAppSvcSectionLeaderList() != null) {
            slList = oldAppSvcRelatedInfoDto.getAppSvcSectionLeaderList().stream()
                    .map(AppSvcPersonnelDto::getName)
                    .collect(Collectors.toList());
        }
        int flag = eqAppSvcDisciplineAllocation(appSvcDisciplineAllocationDtoList, oldAppSvcDisciplineAllocationDtoList,
                IaisCommonUtils.getList(cgoList), IaisCommonUtils.getList(slList));
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
        if (flag1 && flag == 0) {
            changeList.add(HcsaConsts.STEP_DISCIPLINE_ALLOCATION);
        }
        if (eqSvcDoc) {
            changeList.add(HcsaConsts.STEP_DOCUMENTS);
        }
        if (eqAppSvcChargesPageDto) {
            changeList.add(HcsaConsts.STEP_CHARGES);
        }
        if (appEditSelectDto != null) {
            List<String> personnelEditList = NewApplicationHelper.getList(appEditSelectDto.getPersonnelEditList());
            personnelEditList.addAll(changeList);
            appEditSelectDto.setPersonnelEditList(personnelEditList);
        }
        if (flag != 1 || !flag1 || eqSvcDoc || eqAppSvcVehicle || eqAppSvcChargesPageDto || changePersonnel || eqAppSvcBusiness) {
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

    private static boolean eqAppSvcLaboratoryDisciplines(List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList,List<AppSvcLaboratoryDisciplinesDto> oldAppSvcLaboratoryDisciplinesDtoList){
        if(appSvcLaboratoryDisciplinesDtoList!=null&&oldAppSvcLaboratoryDisciplinesDtoList!=null){
            List<AppSvcChckListDto> list= IaisCommonUtils.genNewArrayList();
            List<AppSvcChckListDto> list1=IaisCommonUtils.genNewArrayList();
            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList){
                List<AppSvcChckListDto> appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                list.addAll(PageDataCopyUtil.copyAppSvcChckListDto(appSvcChckListDtoList));
            }
            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : oldAppSvcLaboratoryDisciplinesDtoList){
                List<AppSvcChckListDto> appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                list1.addAll(PageDataCopyUtil.copyAppSvcChckListDto(appSvcChckListDtoList));
            }
            for(AppSvcChckListDto appSvcChckListDto: list){
                String chkLstConfId = appSvcChckListDto.getChkLstConfId();
                String chkName = appSvcChckListDto.getChkName();
                for(AppSvcChckListDto appSvcChckListDto1 : list1){
                    String chkLstConfId1 = appSvcChckListDto1.getChkLstConfId();
                    if(chkLstConfId==null&&chkLstConfId1==null){
                        appSvcChckListDto.setChkName(null);
                        appSvcChckListDto1.setChkName(null);
                    }
                    if(chkLstConfId!=null&&chkLstConfId.equals(chkLstConfId1)){
                        appSvcChckListDto1.setChkName(chkName);
                    }
                    if(chkLstConfId1!=null&&chkLstConfId1.equals(chkLstConfId)){
                        appSvcChckListDto1.setChkName(chkName);
                    }
                }
            }
            return list.equals(list1);
        }else {
            return true;
        }
    }

    /**
     * 0: changed with related cgo and sl no changed
     * 1: not changed
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
    private static int eqAppSvcDisciplineAllocation(List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList,
            List<AppSvcDisciplineAllocationDto> oldAppSvcDisciplineAllocationDtoList, List<String> cgoList, List<String> slList) {
        int flag = 1;
        if (appSvcDisciplineAllocationDtoList != null && oldAppSvcDisciplineAllocationDtoList != null) {
            List<AppSvcDisciplineAllocationDto> list1 = MiscUtil.transferEntityDtos(appSvcDisciplineAllocationDtoList,
                    AppSvcDisciplineAllocationDto.class);
            List<AppSvcDisciplineAllocationDto> list2 = MiscUtil.transferEntityDtos(oldAppSvcDisciplineAllocationDtoList,
                    AppSvcDisciplineAllocationDto.class);

            for (AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : list1) {
                String cgoPerson = appSvcDisciplineAllocationDto.getCgoPerson();
                String premiseVal = appSvcDisciplineAllocationDto.getPremiseVal();
                String chkLstConfId = appSvcDisciplineAllocationDto.getChkLstConfId();
                String cgoSelName = appSvcDisciplineAllocationDto.getCgoSelName();
                String chkLstName = appSvcDisciplineAllocationDto.getChkLstName();
                for (AppSvcDisciplineAllocationDto allocationDto : list2) {
                    String cgoPerson1 = allocationDto.getCgoPerson();
                    String premiseVal1 = allocationDto.getPremiseVal();
                    String chkLstConfId1 = allocationDto.getChkLstConfId();
                    if (Objects.equals(cgoPerson, cgoPerson1)
                            && Objects.equals(premiseVal, premiseVal1)
                            && Objects.equals(chkLstConfId, chkLstConfId1)) {
                        allocationDto.setCgoSelName(cgoSelName);
                        allocationDto.setChkLstName(chkLstName);
                        allocationDto.setSectionLeaderName(appSvcDisciplineAllocationDto.getSectionLeaderName());
                    }
                }
            }
            flag = list1.equals(list2) ? 1 : 0;
        } else if (appSvcDisciplineAllocationDtoList != null ^ oldAppSvcDisciplineAllocationDtoList != null) {
            flag = 0;
        }
        if (appSvcDisciplineAllocationDtoList != null && flag != 1) {
            boolean newCgo = appSvcDisciplineAllocationDtoList.stream()
                    .anyMatch(dto -> !cgoList.contains(dto.getCgoPerson()));
            boolean newSL = appSvcDisciplineAllocationDtoList.stream()
                    .anyMatch(dto -> !slList.contains(dto.getSectionLeaderName()));
            if (newCgo && newSL) {
                flag = 4;
            } else if (newCgo) {
                flag = 3;
            } else if (newSL) {
                flag = 2;
            }
        }
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

    private static boolean eqSvcPrincipalOfficers(List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList, List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtoList) {
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
    private static boolean eqMeadrter(List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList, List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList1)  {
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

    private static boolean eqKeyAppointmentHolder(List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderDtoList, List<AppSvcPrincipalOfficersDto> oldAppSvcKeyAppointmentHolderDtoList)  {
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

    private static boolean eqSvcDoc( List<AppSvcDocDto> appSvcDocDtoLit, List<AppSvcDocDto> oldAppSvcDocDtoLit){
        if(appSvcDocDtoLit==null){
            appSvcDocDtoLit=new ArrayList<>();
        }
        if(oldAppSvcDocDtoLit==null){
            oldAppSvcDocDtoLit=new ArrayList<>();
        }
        List<AppSvcDocDto> n = PageDataCopyUtil.copySvcDoc(appSvcDocDtoLit);
        List<AppSvcDocDto> o = PageDataCopyUtil.copySvcDoc(oldAppSvcDocDtoLit);
        if(!o.equals(n)){
            return true;
        }

        return false;
    }
    private static boolean eqCgo(List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList, List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList)  {
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

    public static boolean eqHciCode(AppGrpPremisesDto appGrpPremisesDto,AppGrpPremisesDto oldAppGrpPremisesDto){
        String hciCode = appGrpPremisesDto.getHciCode();
        String oldHciCode = oldAppGrpPremisesDto.getHciCode();
        if(!StringUtil.isEmpty(hciCode)){
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

    public static boolean isChangeFloorUnit(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto){
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        return isChangeFloorUnit(appGrpPremisesDtoList,oldAppGrpPremisesDtoList);
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

    public static boolean isChangeFloorUnit(List<AppGrpPremisesDto> appGrpPremisesDtoList , List<AppGrpPremisesDto> oldAppGrpPremisesDtoList){
        List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos=new ArrayList<>(10);
        appGrpPremisesDtoList.forEach((v)->{
            appPremisesOperationalUnitDtos.add(getFirstOpeUnitDto(v));
            appPremisesOperationalUnitDtos.addAll(v.getAppPremisesOperationalUnitDtos());
        });
        List<AppPremisesOperationalUnitDto> oldAppPremisesOperationalUnitDtos=new ArrayList<>(10);
        oldAppGrpPremisesDtoList.forEach((v)->{
            oldAppPremisesOperationalUnitDtos.add(getFirstOpeUnitDto(v));
            oldAppPremisesOperationalUnitDtos.addAll(v.getAppPremisesOperationalUnitDtos());
        });
        return !eqOperationalUnitDtoList(appPremisesOperationalUnitDtos,oldAppPremisesOperationalUnitDtos);
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
        String newHciName = NewApplicationHelper.getHciName(appGrpPremisesDto);
        String oldHciName = NewApplicationHelper.getHciName(premisesListQueryDto);
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
                if (!compareHciName(appGrpPremisesDto,oldAppGrpPremisesDto)) {
                    isChanged = true;
                    break;
                }
            }
        }
        return isChanged;
    }

    /**
     *
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

    public static boolean isChangeAppSvcVehicleDto(List<AppSvcVehicleDto> appSvcVehicleDtoList ,List<AppSvcVehicleDto> oldAppSvcVehicleDtoList) {
        List<AppSvcVehicleDto> n = PageDataCopyUtil.copyAppSvcVehicleDto(appSvcVehicleDtoList);
        List<AppSvcVehicleDto> o = PageDataCopyUtil.copyAppSvcVehicleDto(oldAppSvcVehicleDtoList);
        if (!n.equals(o)) {
            return true;
        }
        return false;
    }

    public static boolean isChangeAppSvcBusinessDtos(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList){
        if (appSvcRelatedInfoDtoList == null || oldAppSvcRelatedInfoDtoList == null) {
            return true;
        }
        if (appSvcRelatedInfoDtoList.size() != oldAppSvcRelatedInfoDtoList.size()) {
            return true;
        }
        List<AppSvcBusinessDto> appSvcBusinessDtoList = IaisCommonUtils.genNewArrayList();
        appSvcRelatedInfoDtoList.forEach((item)->{
            appSvcBusinessDtoList.addAll(item.getAppSvcBusinessDtoList());
        });
        List<AppSvcBusinessDto> oldAppSvcBusinessDtoList = IaisCommonUtils.genNewArrayList();
        oldAppSvcRelatedInfoDtoList.forEach((item)->{
            oldAppSvcBusinessDtoList.addAll(item.getAppSvcBusinessDtoList());
        });
        return isChangeAppSvcBusinessDto(appSvcBusinessDtoList, oldAppSvcBusinessDtoList);
    }

    public static boolean isChangeAppSvcBusinessDto(List<AppSvcBusinessDto> appSvcBusinessDtoList, List<AppSvcBusinessDto> oldAppSvcBusinessDtoList){
        List<String> appSvcBusinessNameList = IaisCommonUtils.genNewArrayList();
        appSvcBusinessDtoList.forEach((v)->{
            appSvcBusinessNameList.add(v.getBusinessName());
        });
        List<String> oldAppSvcBusinessNameList = IaisCommonUtils.genNewArrayList();
        oldAppSvcBusinessDtoList.forEach((v)->{
            oldAppSvcBusinessNameList.add(v.getBusinessName());
        });
        return ! appSvcBusinessNameList.equals(oldAppSvcBusinessNameList);
    }

    public static boolean eqAppSvcChargesPageDto(AppSvcChargesPageDto appSvcChargesPageDto,AppSvcChargesPageDto oldAppSvcChargesPageDto){
        AppSvcChargesPageDto n = PageDataCopyUtil.copyAppSvcClinicalDirector(appSvcChargesPageDto);
        AppSvcChargesPageDto o = PageDataCopyUtil.copyAppSvcClinicalDirector(oldAppSvcChargesPageDto);
        if(!n.equals(o)){
            return true;
        }
        return false;
    }

    public static boolean eqAppSvcClinicalDirector(List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos,List<AppSvcPrincipalOfficersDto> oldAppSvcClinicalDirectorDtos){
        List<AppSvcPrincipalOfficersDto> n = PageDataCopyUtil.copyAppSvcClinicalDirector(appSvcClinicalDirectorDtos);
        List<AppSvcPrincipalOfficersDto> o = PageDataCopyUtil.copyAppSvcClinicalDirector(oldAppSvcClinicalDirectorDtos);
        if(!n.equals(o)){
            return true;
        }
        return false;
    }

    public static boolean isChangeSubLicensee(SubLicenseeDto subLicenseeDto, SubLicenseeDto oldSbLicenseeDto) {
        return !Objects.equals(subLicenseeDto, oldSbLicenseeDto);
    }

    public static AppEditSelectDto rfcChangeModuleEvaluationDto(AppSubmissionDto appSubmissionDto,AppSubmissionDto oldAppSubmissionDto) throws Exception {
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        boolean hciNameChange = false;
        if(IaisCommonUtils.isNotEmpty(oldAppGrpPremisesDtoList)&& IaisCommonUtils.isNotEmpty(appGrpPremisesDtoList)){
            for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
                hciNameChange = eqHciNameChange(appGrpPremisesDtoList.get(i), oldAppGrpPremisesDtoList.get(i));
                if(hciNameChange){
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
        boolean changeBusiness = isChangeAppSvcBusinessDtos(appSvcRelatedInfoDtos,oldAppSvcRelatedInfoDtos);
        boolean changeSectionLeader = isChangeAppSvcSectionLeadersViaSvcInfo(appSvcRelatedInfoDtos, oldAppSvcRelatedInfoDtos);
        // for splitting the submission
        AppEditSelectDto showDto = appSubmissionDto.getAppEditSelectDto();
        List<String> stepList = NewApplicationHelper.getList(showDto.getPersonnelEditList());
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
                getList(appSvcRelatedInfoDtoList.getAppSvcPrincipalOfficersDtoList());
        List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDto =
                getList(oldAppSvcRelatedInfoDtoList.getAppSvcPrincipalOfficersDtoList());
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
                newPoIdNos.add(NewApplicationHelper.getPersonKey(item));
            } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(item.getPsnType())) {
                newDpoIdNos.add(NewApplicationHelper.getPersonKey(item));
            }
        }
        for (AppSvcPrincipalOfficersDto item : oldAppSvcPrincipalOfficersDto) {
            if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(item.getPsnType())) {
                oldPoIdNos.add(NewApplicationHelper.getPersonKey(item));
            } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(item.getPsnType())) {
                olddDpoIdNos.add(NewApplicationHelper.getPersonKey(item));
            }
        }
        if (!newPoIdNos.equals(oldPoIdNos)) {
            isAuto = false;
            personnelEditList.add(HcsaConsts.STEP_PRINCIPAL_OFFICERS);
        }
        if (!newDpoIdNos.equals(olddDpoIdNos)) {
            isAuto = false;
            NewApplicationHelper.addToList(HcsaConsts.STEP_PRINCIPAL_OFFICERS, personnelEditList);
            personnelEditList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
        }
        // CGO
        List<AppSvcPrincipalOfficersDto> newAppSvcCgoDto = getList(appSvcRelatedInfoDtoList.getAppSvcCgoDtoList());
        List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDto = getList(oldAppSvcRelatedInfoDtoList.getAppSvcCgoDtoList());
        List<String> newIdNos = IaisCommonUtils.genNewArrayList();
        List<String> oldIdNos = IaisCommonUtils.genNewArrayList();
        for (AppSvcPrincipalOfficersDto item : newAppSvcCgoDto) {
            newIdNos.add(NewApplicationHelper.getPersonKey(item));
        }
        for (AppSvcPrincipalOfficersDto item : oldAppSvcCgoDto) {
            oldIdNos.add(NewApplicationHelper.getPersonKey(item));
        }
        if (!newIdNos.equals(oldIdNos)) {
            isAuto = false;
            personnelEditList.add(HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS);
        }
        // CD
        List<AppSvcPrincipalOfficersDto> newAppSvcCdDto = getList(appSvcRelatedInfoDtoList.getAppSvcClinicalDirectorDtoList());
        List<AppSvcPrincipalOfficersDto> oldAppSvcCdDto = getList(oldAppSvcRelatedInfoDtoList.getAppSvcClinicalDirectorDtoList());
        newIdNos = IaisCommonUtils.genNewArrayList();
        oldIdNos = IaisCommonUtils.genNewArrayList();
        for (AppSvcPrincipalOfficersDto item : newAppSvcCdDto) {
            newIdNos.add(NewApplicationHelper.getPersonKey(item));
        }
        for (AppSvcPrincipalOfficersDto item : oldAppSvcCdDto) {
            oldIdNos.add(NewApplicationHelper.getPersonKey(item));
        }
        if (!newIdNos.equals(oldIdNos)) {
            isAuto = false;
            personnelEditList.add(HcsaConsts.STEP_CLINICAL_DIRECTOR);
        }
        // MAP
        List<AppSvcPrincipalOfficersDto> newAppSvcMapDto = getList(appSvcRelatedInfoDtoList.getAppSvcMedAlertPersonList());
        List<AppSvcPrincipalOfficersDto> oldAppSvcMapDto = getList(oldAppSvcRelatedInfoDtoList.getAppSvcMedAlertPersonList());
        newIdNos = IaisCommonUtils.genNewArrayList();
        oldIdNos = IaisCommonUtils.genNewArrayList();
        for (AppSvcPrincipalOfficersDto item : newAppSvcMapDto) {
            newIdNos.add(NewApplicationHelper.getPersonKey(item));
        }
        for (AppSvcPrincipalOfficersDto item : oldAppSvcMapDto) {
            oldIdNos.add(NewApplicationHelper.getPersonKey(item));
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
                            NewApplicationHelper.addToList(HcsaConsts.STEP_DISCIPLINE_ALLOCATION, personnelEditList);
                            break;
                        }
                    }
                } else if (!IaisCommonUtils.isEmpty(newCheckList) || !IaisCommonUtils.isEmpty(oldCheckList)) {
                    isAuto = false;
                    personnelEditList.add(HcsaConsts.STEP_LABORATORY_DISCIPLINES);
                    NewApplicationHelper.addToList(HcsaConsts.STEP_DISCIPLINE_ALLOCATION, personnelEditList);
                }
            }
        } else if (!IaisCommonUtils.isEmpty(newDisciplinesDto) || !IaisCommonUtils.isEmpty(oldDisciplinesDto)) {
            isAuto = false;
            personnelEditList.add(HcsaConsts.STEP_LABORATORY_DISCIPLINES);
            NewApplicationHelper.addToList(HcsaConsts.STEP_DISCIPLINE_ALLOCATION, personnelEditList);
        }
        // KAH
        List<AppSvcPrincipalOfficersDto> kahList = getList(appSvcRelatedInfoDtoList.getAppSvcKeyAppointmentHolderDtoList());
        List<AppSvcPrincipalOfficersDto> oldKahList = getList(oldAppSvcRelatedInfoDtoList.getAppSvcKeyAppointmentHolderDtoList());
        List<String> newKahIdNos = IaisCommonUtils.genNewArrayList();
        List<String> oldKahIdNos = IaisCommonUtils.genNewArrayList();
        for (AppSvcPrincipalOfficersDto item : kahList) {
            newKahIdNos.add(NewApplicationHelper.getPersonKey(item));
        }
        for (AppSvcPrincipalOfficersDto item : oldKahList) {
            oldKahIdNos.add(NewApplicationHelper.getPersonKey(item));
        }
        if (!newKahIdNos.equals(oldKahIdNos)) {
            isAuto = false;
            personnelEditList.add(HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER);
        }
        appEditSelectDto.setPersonnelEditList(personnelEditList);
        appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
        return isAuto;
    }

    private static <T> List<T> getList(List<T> sourceList) {
        if (sourceList == null) {
            return IaisCommonUtils.genNewArrayList(0);
        }
        return sourceList;
    }

    private static AppSvcLaboratoryDisciplinesDto getDisciplinesDto(List<AppSvcLaboratoryDisciplinesDto> disciplinesDto, String hciName) {
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
        autoDto.setAppGrpPremisesDtoList(NewApplicationHelper.updatePremisesIndex(
                (List<AppGrpPremisesDto>) CopyUtil.copyMutableObjectList(autoPremisesDtos),
                autoDto.getAppGrpPremisesDtoList()));
        autoDto.setChangeSelectDto(newChangeSelectDto);
        autoDto.setAppGrpStatus(null);
        autoDto.setAmount(0.0);
        NewApplicationHelper.reSetAdditionalFields(autoDto, newChangeSelectDto, autoGroupNo);
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
            List<AppSvcDisciplineAllocationDto> allocationDtoList = appSvcRelatedInfoDtos
                    .get(i).getAppSvcDisciplineAllocationDtoList();
            List<AppSvcDisciplineAllocationDto> oldAllocationDtoList = oldAppSvcRelatedInfoDtos
                    .get(i).getAppSvcDisciplineAllocationDtoList();
            Map<String, String> cgoMap = new HashMap<>();
            Map<String, String> slMap = new HashMap<>();
            if (oldAllocationDtoList != null) {
                oldAllocationDtoList.forEach(dto -> {
                    cgoMap.put(dto.getIdNo(), dto.getIdNo());
                    slMap.put(dto.getSlIndex(), dto.getSectionLeaderName());
                });
            }
            if (allocationDtoList != null) {
                boolean changed = allocationDtoList.stream().anyMatch(dto -> cgoMap.get(dto.getIdNo()) == null
                        || slMap.get(dto.getSlIndex()) == null);
                if (changed) {
                    return true;
                }
            }
        }
        return false;
    }
}
