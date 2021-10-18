package com.ecquaria.cloud.moh.iais.rfcutil;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if (!appGrpPremisesDtoList.equals(oldAppGrpPremisesDtoList)) {
            return true;
        }
        return false;
    }

   /* public static boolean isChangeAutoFields(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) {
        List<AppGrpPremisesDto> appGrpPremisesDtos = generateDtosForAutoFields(appGrpPremisesDtoList, oldAppGrpPremisesDtoList);
        List<AppGrpPremisesDto> oldAppGrpPremisesDtos = PageDataCopyUtil.copyAppGrpPremises(oldAppGrpPremisesDtoList);
        if (!appGrpPremisesDtos.equals(oldAppGrpPremisesDtos)) {
            return true;
        }
        return false;
    }*/

    public static List<AppGrpPremisesDto> generateDtosForAutoFields(List<AppGrpPremisesDto> appGrpPremisesDtoList,
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
            if (appEditSelectDto.isChangeHciName() || appEditSelectDto.isChangeInLocation()) {
                if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())) {
                    copy.setPostalCode(appGrpPremisesDto.getPostalCode());
                    copy.setAddrType(appGrpPremisesDto.getAddrType());
                    copy.setBlkNo(appGrpPremisesDto.getBlkNo());
                    copy.setFloorNo(appGrpPremisesDto.getFloorNo());
                    copy.setUnitNo(appGrpPremisesDto.getUnitNo());
                    copy.setStreetName(appGrpPremisesDto.getStreetName());
                    copy.setBuildingName(appGrpPremisesDto.getBuildingName());

                    copy.setHciName(appGrpPremisesDto.getHciName());
                } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(appGrpPremisesDto.getPremisesType())) {
                    copy.setOffSitePostalCode(appGrpPremisesDto.getOffSitePostalCode());
                    copy.setOffSiteAddressType(appGrpPremisesDto.getOffSiteAddressType());
                    copy.setOffSiteBlockNo(appGrpPremisesDto.getOffSiteBlockNo());
                    copy.setOffSiteFloorNo(appGrpPremisesDto.getOffSiteFloorNo());
                    copy.setOffSiteUnitNo(appGrpPremisesDto.getOffSiteUnitNo());
                    copy.setOffSiteStreetName(appGrpPremisesDto.getOffSiteStreetName());
                    copy.setOffSiteBuildingName(appGrpPremisesDto.getOffSiteBuildingName());

                    copy.setOffSiteHciName(appGrpPremisesDto.getOffSiteHciName());
                } else if (ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())) {
                    //EASMTS
                    copy.setEasMtsHciName(appGrpPremisesDto.getEasMtsHciName());
                    copy.setEasMtsPostalCode(appGrpPremisesDto.getEasMtsPostalCode());
                    copy.setEasMtsAddressType(appGrpPremisesDto.getEasMtsAddressType());
                    copy.setEasMtsBlockNo(appGrpPremisesDto.getEasMtsBlockNo());
                    copy.setEasMtsFloorNo(appGrpPremisesDto.getEasMtsFloorNo());
                    copy.setEasMtsUnitNo(appGrpPremisesDto.getEasMtsUnitNo());
                    copy.setEasMtsStreetName(appGrpPremisesDto.getEasMtsStreetName());
                    copy.setEasMtsBuildingName(appGrpPremisesDto.getEasMtsBuildingName());
                } else {
                    copy.setConveyancePostalCode(appGrpPremisesDto.getConveyancePostalCode());
                    copy.setConveyanceAddressType(appGrpPremisesDto.getConveyanceAddressType());
                    copy.setConveyanceBlockNo(appGrpPremisesDto.getConveyanceBlockNo());
                    copy.setConveyanceFloorNo(appGrpPremisesDto.getConveyanceFloorNo());
                    copy.setConveyanceUnitNo(appGrpPremisesDto.getConveyanceUnitNo());
                    copy.setConveyanceStreetName(appGrpPremisesDto.getConveyanceStreetName());
                    copy.setConveyanceBuildingName(appGrpPremisesDto.getConveyanceBuildingName());
                    copy.setConveyanceHciName(appGrpPremisesDto.getConveyanceHciName());
                }
            }
            if (appEditSelectDto.isChangeAddFloorUnit()) {
                List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtoList =
                        PageDataCopyUtil.copyAppPremisesOperationalUnitDto(appGrpPremisesDto.getAppPremisesOperationalUnitDtos());
                copy.setAppPremisesOperationalUnitDtos(appPremisesOperationalUnitDtoList);
            }
            result.add(copy);
        }
        return result;
    }

    public static boolean eqServiceChange(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) throws Exception {
        List<AppSvcRelatedInfoDto> n = (List<AppSvcRelatedInfoDto>) CopyUtil.copyMutableObjectList(appSvcRelatedInfoDtoList);
        List<AppSvcRelatedInfoDto> o = (List<AppSvcRelatedInfoDto>) CopyUtil.copyMutableObjectList(oldAppSvcRelatedInfoDtoList);
        List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = n.get(0).getAppSvcDisciplineAllocationDtoList();
        List<AppSvcDisciplineAllocationDto> oldAppSvcDisciplineAllocationDtoList = o.get(0).getAppSvcDisciplineAllocationDtoList();
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = n.get(0).getHcsaServiceStepSchemeDtos();
        String deputyPoFlag = n.get(0).getDeputyPoFlag();
        o.get(0).setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);
        o.get(0).setDeputyPoFlag(deputyPoFlag);
        boolean flag=eqAppSvcDisciplineAllocation(appSvcDisciplineAllocationDtoList,oldAppSvcDisciplineAllocationDtoList);
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = n.get(0).getAppSvcLaboratoryDisciplinesDtoList();
        List<AppSvcLaboratoryDisciplinesDto> oldAppSvcLaboratoryDisciplinesDtoList = o.get(0).getAppSvcLaboratoryDisciplinesDtoList();
        boolean flag1= eqAppSvcLaboratoryDisciplines(appSvcLaboratoryDisciplinesDtoList,oldAppSvcLaboratoryDisciplinesDtoList);

        List<AppSvcDocDto> appSvcDocDtoLit = n.get(0).getAppSvcDocDtoLit();
        List<AppSvcDocDto> oldAppSvcDocDtoLit = o.get(0).getAppSvcDocDtoLit();
        boolean eqSvcDoc = eqSvcDoc(appSvcDocDtoLit, oldAppSvcDocDtoLit);
        boolean eqAppSvcVehicle = isChangeAppSvcVehicleDto(n.get(0).getAppSvcVehicleDtoList(), o.get(0).getAppSvcVehicleDtoList());
        boolean eqAppSvcChargesPageDto = eqAppSvcChargesPageDto(n.get(0).getAppSvcChargesPageDto(), o.get(0).getAppSvcChargesPageDto());
        boolean changePersonnel = changePersonnel(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        boolean eqAppSvcBusiness = isChangeAppSvcBusinessDto(n.get(0).getAppSvcBusinessDtoList(), o.get(0).getAppSvcBusinessDtoList());
        if (!flag || !flag1 || eqSvcDoc || eqAppSvcVehicle ||eqAppSvcChargesPageDto || changePersonnel || eqAppSvcBusiness) {
            return true;
        }
        return false;
    }

    public static boolean changePersonnel(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList,
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        List<AppSvcRelatedInfoDto> n = null;
        List<AppSvcRelatedInfoDto> o = null;
        n = (List<AppSvcRelatedInfoDto>) CopyUtil.copyMutableObjectList(appSvcRelatedInfoDtoList);
        o = (List<AppSvcRelatedInfoDto>) CopyUtil.copyMutableObjectList(oldAppSvcRelatedInfoDtoList);
        if (n == null || o == null) {
            return true;
        }
        boolean eqAppSvcClinicalDirector = eqAppSvcClinicalDirector(n.get(0).getAppSvcClinicalDirectorDtoList(), o.get(0).getAppSvcClinicalDirectorDtoList());
        if (eqAppSvcClinicalDirector) {
            return true;
        }
        List<AppSvcPersonnelDto> appSvcPersonnelDtoList = n.get(0).getAppSvcPersonnelDtoList();
        List<AppSvcPersonnelDto> oldAppSvcPersonnelDtoList = o.get(0).getAppSvcPersonnelDtoList();
        boolean eqServicePseronnel = eqServicePseronnel(appSvcPersonnelDtoList, oldAppSvcPersonnelDtoList);
        if (eqServicePseronnel) {
            return true;
        }
        // section leader
        List<AppSvcPersonnelDto> appSvcSectionLeaderList = n.get(0).getAppSvcSectionLeaderList();
        List<AppSvcPersonnelDto> oldAppSvcSectionLeaderList = o.get(0).getAppSvcSectionLeaderList();
        boolean eqSectionLeader = eqServicePseronnel(appSvcSectionLeaderList, oldAppSvcSectionLeaderList);
        if (eqSectionLeader) {
            return true;
        }
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = n.get(0).getAppSvcPrincipalOfficersDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtoList = o.get(0).getAppSvcPrincipalOfficersDtoList();
        boolean eqSvcPrincipalOfficers = eqSvcPrincipalOfficers(appSvcPrincipalOfficersDtoList, oldAppSvcPrincipalOfficersDtoList);
        if (eqSvcPrincipalOfficers) {
            return true;
        }
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = n.get(0).getAppSvcCgoDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList = o.get(0).getAppSvcCgoDtoList();
        boolean eqCgo = eqCgo(appSvcCgoDtoList, oldAppSvcCgoDtoList);
        if (eqCgo) {
            return true;
        }
        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = n.get(0).getAppSvcMedAlertPersonList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList = o.get(0).getAppSvcMedAlertPersonList();
        boolean eqMeadrter = eqMeadrter(appSvcMedAlertPersonList, oldAppSvcMedAlertPersonList);
        if (eqMeadrter) {
            return true;
        }
        // kah
        List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderDtoList = n.get(0).getAppSvcKeyAppointmentHolderDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcKeyAppointmentHolderDtoList = o.get(0).getAppSvcKeyAppointmentHolderDtoList();
        boolean eqKeyAppointmentHolder = eqKeyAppointmentHolder(appSvcKeyAppointmentHolderDtoList, oldAppSvcKeyAppointmentHolderDtoList);
        if (eqKeyAppointmentHolder) {
            return true;
        }
        return false;
    }

    private static boolean eqAppSvcLaboratoryDisciplines(List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList,List<AppSvcLaboratoryDisciplinesDto> oldAppSvcLaboratoryDisciplinesDtoList){
        boolean flag1;
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
            flag1=list.equals(list1);
        }else {
            flag1=true;
        }
        return flag1;
    }
    private static boolean eqAppSvcDisciplineAllocation(List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList , List<AppSvcDisciplineAllocationDto> oldAppSvcDisciplineAllocationDtoList){
        boolean flag;
        if (appSvcDisciplineAllocationDtoList != null && oldAppSvcDisciplineAllocationDtoList != null) {
            for (AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : appSvcDisciplineAllocationDtoList) {
                String idNo = appSvcDisciplineAllocationDto.getIdNo();
                String premiseVal = appSvcDisciplineAllocationDto.getPremiseVal();
                String chkLstConfId = appSvcDisciplineAllocationDto.getChkLstConfId();
                String cgoSelName = appSvcDisciplineAllocationDto.getCgoSelName();
                String chkLstName = appSvcDisciplineAllocationDto.getChkLstName();
                for (AppSvcDisciplineAllocationDto allocationDto : oldAppSvcDisciplineAllocationDtoList) {
                    String idNo1 = allocationDto.getIdNo();
                    String premiseVal1 = allocationDto.getPremiseVal();
                    String chkLstConfId1 = allocationDto.getChkLstConfId();
                    try {
                        if (idNo.equals(idNo1) && premiseVal.equals(premiseVal1) && chkLstConfId.equals(chkLstConfId1)) {
                            allocationDto.setCgoSelName(cgoSelName);
                            allocationDto.setChkLstName(chkLstName);
                        }
                    } catch (NullPointerException e) {

                    }
                }
            }
            flag = appSvcDisciplineAllocationDtoList.equals(oldAppSvcDisciplineAllocationDtoList);
        }else {
            flag=true;
        }
        return flag;
    }
    private static boolean eqServicePseronnel(List<AppSvcPersonnelDto> appSvcPersonnelDtoList , List<AppSvcPersonnelDto> oldAppSvcPersonnelDtoList){
        if(appSvcPersonnelDtoList==null){
            appSvcPersonnelDtoList=new ArrayList<>();
        }
        if(oldAppSvcPersonnelDtoList==null){
            oldAppSvcPersonnelDtoList=new ArrayList<>();
        }
        if(!appSvcPersonnelDtoList.equals(oldAppSvcPersonnelDtoList)){
            return true;
        }
        return false;
    }
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
            List<AppSvcPrincipalOfficersDto> n = PageDataCopyUtil.copyMedaler(appSvcKeyAppointmentHolderDtoList);
            List<AppSvcPrincipalOfficersDto> o = PageDataCopyUtil.copyMedaler(oldAppSvcKeyAppointmentHolderDtoList);
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
    public static boolean eqOperationalUnitDtoList( List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtoList ,List<AppPremisesOperationalUnitDto> oldAppSubmissionDtoAppGrpPremisesDtoList){
        if(appPremisesOperationalUnitDtoList==null && oldAppSubmissionDtoAppGrpPremisesDtoList==null){
            return false;
        }
        if(appPremisesOperationalUnitDtoList==null && oldAppSubmissionDtoAppGrpPremisesDtoList!=null){
            return true;
        }
        if(appPremisesOperationalUnitDtoList!=null && oldAppSubmissionDtoAppGrpPremisesDtoList==null){
            return true;
        }
        List<AppPremisesOperationalUnitDto> n = PageDataCopyUtil.copyAppPremisesOperationalUnitDto(appPremisesOperationalUnitDtoList);
        List<AppPremisesOperationalUnitDto> o = PageDataCopyUtil.copyAppPremisesOperationalUnitDto(oldAppSubmissionDtoAppGrpPremisesDtoList);
        if(!n.equals(o)){
            return true;
        }

        return false;
    }

    public static boolean isChangeFloorUnit(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto){
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPremisesDto> oldAppSubmissionDtoAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        return eqAddFloorNo(appGrpPremisesDtoList,oldAppSubmissionDtoAppGrpPremisesDtoList);
    }

    public static boolean eqAddFloorNo( List<AppGrpPremisesDto> appGrpPremisesDtoList , List<AppGrpPremisesDto> oldAppGrpPremisesDtoList){
        List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos=new ArrayList<>(10);
        appGrpPremisesDtoList.forEach((v)->{
            appPremisesOperationalUnitDtos.addAll(v.getAppPremisesOperationalUnitDtos());
        });
        List<AppPremisesOperationalUnitDto> oldAppPremisesOperationalUnitDtos=new ArrayList<>(10);
        oldAppGrpPremisesDtoList.forEach((v)->{
            oldAppPremisesOperationalUnitDtos.addAll(v.getAppPremisesOperationalUnitDtos());
        });
        return eqOperationalUnitDtoList(appPremisesOperationalUnitDtos,oldAppPremisesOperationalUnitDtos);

    }

    public static boolean eqAppSvcDisciplineAllocationDto(List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList,List<AppSvcDisciplineAllocationDto> oldAppSvcDisciplineAllocationDtoList){
        List<AppSvcDisciplineAllocationDto> list = PageDataCopyUtil.copyAppSvcDisciplineAllocationDto(appSvcDisciplineAllocationDtoList);
        List<AppSvcDisciplineAllocationDto> list1 = PageDataCopyUtil.copyAppSvcDisciplineAllocationDto(oldAppSvcDisciplineAllocationDtoList);
        if(!list.equals(list1)){
            return true;
        }
        return false;
    }
    public static boolean changePersonAuto(AppSubmissionDto oldAppSubmissionDto, AppSubmissionDto appSubmissionDto) {
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        List<AppSvcDisciplineAllocationDto> oldAppSvcDisciplineAllocationDtoList = oldAppSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        if(appSvcDisciplineAllocationDtoList!=null && oldAppSvcDisciplineAllocationDtoList!=null){
            if(appSvcDisciplineAllocationDtoList.size()==oldAppSvcDisciplineAllocationDtoList.size()){
                boolean b = eqAppSvcDisciplineAllocationDto(appSvcDisciplineAllocationDtoList, oldAppSvcDisciplineAllocationDtoList);
                if(b){
                    return true;
                }
            }else if(appSvcDisciplineAllocationDtoList.size()<oldAppSvcDisciplineAllocationDtoList.size()){
                for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : appSvcDisciplineAllocationDtoList){
                    for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto1 : oldAppSvcDisciplineAllocationDtoList){
                        if(appSvcDisciplineAllocationDto.getChkLstConfId().equals(appSvcDisciplineAllocationDto1.getChkLstConfId())){
                            if(!appSvcDisciplineAllocationDto.getIdNo().equals(appSvcDisciplineAllocationDto1.getIdNo())){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        List<AppSvcLaboratoryDisciplinesDto> oldAppSvcLaboratoryDisciplinesDtoList = oldAppSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        if(appSvcLaboratoryDisciplinesDtoList!=null&&oldAppSvcLaboratoryDisciplinesDtoList!=null){
            if(appSvcLaboratoryDisciplinesDtoList.size() > oldAppSvcLaboratoryDisciplinesDtoList.size()){
                return true;
            }else {
                List<AppSvcChckListDto> newAppSvcChckListDto=IaisCommonUtils.genNewArrayList();
                List<AppSvcChckListDto> oldAppSvcChckListDto=IaisCommonUtils.genNewArrayList();
                for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList){
                    List<AppSvcChckListDto> appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                    newAppSvcChckListDto.addAll(appSvcChckListDtoList);
                }
                for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : oldAppSvcLaboratoryDisciplinesDtoList){
                    List<AppSvcChckListDto> appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                    oldAppSvcChckListDto.addAll(appSvcChckListDtoList);
                }
                if(newAppSvcChckListDto.size() > oldAppSvcChckListDto.size()){
                    return true;
                }
            }
        }
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList = oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList();
        if (oldAppSvcCgoDtoList != null) {
            if (oldAppSvcCgoDtoList.size() != appSvcCgoDtoList.size()) {
                return true;
            } else if (oldAppSvcCgoDtoList.size() == appSvcCgoDtoList.size()) {
                int i = 0;
                for (AppSvcPrincipalOfficersDto appSvcCgoDto : oldAppSvcCgoDtoList) {
                    for (AppSvcPrincipalOfficersDto appSvcCgoDto1 : appSvcCgoDtoList) {
                        if (appSvcCgoDto.getIdNo().equals(appSvcCgoDto1.getIdNo())) {
                            i++;
                            break;
                        }
                    }
                }
                if (i != appSvcCgoDtoList.size()) {
                    return true;
                }
            }
        }
        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList = oldAppSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
        if (oldAppSvcMedAlertPersonList != null) {
            if (oldAppSvcMedAlertPersonList.size() != appSvcMedAlertPersonList.size()) {
                return true;
            } else if (oldAppSvcMedAlertPersonList.size() == appSvcMedAlertPersonList.size()) {
                int i = 0;
                for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcMedAlertPersonList) {
                    for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto1 : oldAppSvcMedAlertPersonList) {
                        if (appSvcPrincipalOfficersDto.getIdNo().equals(appSvcPrincipalOfficersDto1.getIdNo())) {
                            i++;
                            break;
                        }
                    }
                }
                if (i != oldAppSvcMedAlertPersonList.size()) {
                    return true;
                }
            }
        }
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtoList = oldAppSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        if (oldAppSvcPrincipalOfficersDtoList != null) {
            if (oldAppSvcPrincipalOfficersDtoList.size() != appSvcPrincipalOfficersDtoList.size()) {
                return true;
            } else if (oldAppSvcPrincipalOfficersDtoList.size() == appSvcPrincipalOfficersDtoList.size()) {
                int i = 0;
                for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtoList) {
                    for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto1 : oldAppSvcPrincipalOfficersDtoList) {
                        if (appSvcPrincipalOfficersDto.getIdNo().equals(appSvcPrincipalOfficersDto1.getIdNo())) {
                            i++;
                            break;
                        }
                    }
                }
                if (i != oldAppSvcPrincipalOfficersDtoList.size()) {
                    return true;
                }
            }
        }
        List<AppSvcVehicleDto> appSvcVehicleDtoList = appSvcRelatedInfoDto.getAppSvcVehicleDtoList();
        List<AppSvcVehicleDto> oldAppSvcVehicleDtoList = oldAppSvcRelatedInfoDto.getAppSvcVehicleDtoList();
        if(oldAppSvcVehicleDtoList!=null){
            if(oldAppSvcVehicleDtoList.size()!=appSvcVehicleDtoList.size()){
                return true;
            }else if(appSvcVehicleDtoList.size() == oldAppSvcVehicleDtoList .size()){
                int i=0;
                for (AppSvcVehicleDto v : appSvcVehicleDtoList) {
                    for (AppSvcVehicleDto v1 : oldAppSvcVehicleDtoList) {
                        if(v.getVehicleName().equals(v1.getVehicleName())){
                            i++;
                            break;
                        }
                    }
                }
                if(i != oldAppSvcVehicleDtoList.size()){
                    return true;
                }
            }
        }
        /*
        AppSvcChargesPageDto appSvcChargesPageDto = appSvcRelatedInfoDto.getAppSvcChargesPageDto();
        AppSvcChargesPageDto oldAppSvcChargesPageDto = oldAppSvcRelatedInfoDto.getAppSvcChargesPageDto();
        boolean compareAppSvcChargesPage = compareAppSvcChargesPage(appSvcChargesPageDto, oldAppSvcChargesPageDto);
        if(compareAppSvcChargesPage){
           return compareAppSvcChargesPage;
        }
        */
        List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtoList = appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcClinicalDirectorDtoList = oldAppSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();
        if(oldAppSvcClinicalDirectorDtoList!=null){
            if(appSvcClinicalDirectorDtoList.size()!=oldAppSvcClinicalDirectorDtoList.size()){
                return true;
            }else if(appSvcClinicalDirectorDtoList.size()==oldAppSvcClinicalDirectorDtoList.size()){
                int i=0;
                for (AppSvcPrincipalOfficersDto v : appSvcClinicalDirectorDtoList) {
                    for (AppSvcPrincipalOfficersDto v1 : oldAppSvcClinicalDirectorDtoList) {
                        if(v.getIdNo().equals(v1.getIdNo())){
                            i++;
                        }
                    }
                }
                if(i!=oldAppSvcClinicalDirectorDtoList.size()){
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean compareAppSvcChargesPage(AppSvcChargesPageDto appSvcChargesPageDto ,AppSvcChargesPageDto oldAppSvcChargesPageDto){
        if(oldAppSvcChargesPageDto!=null){
            List<AppSvcChargesDto> oldGeneralChargesDtos = oldAppSvcChargesPageDto.getGeneralChargesDtos();
            List<AppSvcChargesDto> oldOtherChargesDtos = oldAppSvcChargesPageDto.getOtherChargesDtos();
            List<AppSvcChargesDto> generalChargesDtos = appSvcChargesPageDto.getGeneralChargesDtos();
            List<AppSvcChargesDto> otherChargesDtos = appSvcChargesPageDto.getOtherChargesDtos();
            if(oldGeneralChargesDtos ==null && generalChargesDtos!=null){
                return true;
            }else if(oldGeneralChargesDtos!=null && generalChargesDtos==null){
                return true;
            }else if(oldGeneralChargesDtos!=null && generalChargesDtos!= null){
                if(oldGeneralChargesDtos.size()!=generalChargesDtos.size()){
                    return true;
                }else if(oldGeneralChargesDtos.size()== generalChargesDtos.size()){
                    int i=0;
                    for (AppSvcChargesDto v : generalChargesDtos) {
                        for (AppSvcChargesDto v1 : oldGeneralChargesDtos) {
                            if(v.getMaxAmount().equals(v1.getMaxAmount())&&v.getMinAmount().equals(v1.getMinAmount())){
                                i++;
                            }
                        }
                    }
                    if(i!=oldGeneralChargesDtos.size()){
                        return true;
                    }
                }
            }
            if(oldOtherChargesDtos==null&&otherChargesDtos!=null){
                return true;
            }else if(oldOtherChargesDtos!=null&&otherChargesDtos==null){
                return true;
            }else if(oldOtherChargesDtos!=null && otherChargesDtos!=null){
                if(oldOtherChargesDtos.size()!=otherChargesDtos.size()){
                    return true;
                }else if(oldOtherChargesDtos.size()== otherChargesDtos.size()){
                    int i=0;
                    for (AppSvcChargesDto v : otherChargesDtos) {
                        for (AppSvcChargesDto v1 : oldOtherChargesDtos) {
                            if(v.getMinAmount().equals(v1.getMinAmount())&&v.getMaxAmount().equals(v1.getMaxAmount())){
                                i++;
                            }
                        }
                    }
                    if(i!=oldOtherChargesDtos.size()){
                        return true;
                    }
                }
            }
        }
        return false;
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
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(0);
                AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(0);
                if (!appGrpPremisesDto.getAddress().equals(oldAppGrpPremisesDto.getAddress())) {
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
        if(IaisCommonUtils.isNotEmpty(oldAppGrpPremisesDtoList)&& IaisCommonUtils.isNotEmpty(appGrpPremisesDtoList)){
            for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
                boolean eqHciNameChange = eqHciNameChange(appGrpPremisesDtoList.get(i), oldAppGrpPremisesDtoList.get(i));
                if(eqHciNameChange){
                    appEditSelectDto.setChangeHciName(eqHciNameChange);
                    break;
                }
            }
        }

        boolean changeInLocation = !compareLocation(appSubmissionDto.getAppGrpPremisesDtoList(),
                oldAppSubmissionDto.getAppGrpPremisesDtoList());
        boolean eqAddFloorNo = isChangeFloorUnit(appSubmissionDto, oldAppSubmissionDto);
        boolean changeVehicles = isChangeAppSvcVehicleDtos(appSubmissionDto.getAppSvcRelatedInfoDtoList(), oldAppSubmissionDto.getAppSvcRelatedInfoDtoList());
        boolean changeBusiness = isChangeAppSvcBusinessDtos(appSubmissionDto.getAppSvcRelatedInfoDtoList(),
                oldAppSubmissionDto.getAppSvcRelatedInfoDtoList());
        boolean notChangePersonnel = compareNotChangePersonnel(appSubmissionDto, oldAppSubmissionDto);
        appEditSelectDto.setChangeInLocation(changeInLocation);
        appEditSelectDto.setChangeAddFloorUnit(eqAddFloorNo);
        appEditSelectDto.setChangeVehicle(changeVehicles);
        appEditSelectDto.setChangeBusinessName(changeBusiness);
        appEditSelectDto.setChangePersonnel(!notChangePersonnel);

        boolean licenseeChange = isChangeSubLicensee(appSubmissionDto.getSubLicenseeDto(), oldAppSubmissionDto.getSubLicenseeDto());
        boolean grpPremiseIsChange = changeInLocation || eqAddFloorNo || appEditSelectDto.isChangeHciName();
        boolean serviceIsChange;
        boolean docIsChange;
        List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        List<AppGrpPrimaryDocDto> oldAppGrpPrimaryDocDtos = oldAppSubmissionDto.getAppGrpPrimaryDocDtos();
        docIsChange = eqDocChange(dtoAppGrpPrimaryDocDtos, oldAppGrpPrimaryDocDtos);
        /*
         * 1.add if migrated -> hci name if no change to 2  ,if change to 0 (premises) first rfc or renew
         * 2. migrated ->premises -> is 0 all to -> 0 . can 2-> 0. but cannot 0->2
         *
         * */
        if (appGrpPremisesDtoList != null && !grpPremiseIsChange) {
            grpPremiseIsChange = isChangeGrpPremises(appGrpPremisesDtoList,oldAppGrpPremisesDtoList);
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
        serviceIsChange = eqServiceChange(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        appEditSelectDto.setDocEdit(docIsChange);
        appEditSelectDto.setLicenseeEdit(licenseeChange);
        appEditSelectDto.setServiceEdit(serviceIsChange);
        appEditSelectDto.setPremisesEdit(grpPremiseIsChange);
        appSubmissionDto.setChangeSelectDto(appEditSelectDto);
        // for splitting the submission
        if (changeVehicles) {
            AppEditSelectDto showEdit = appSubmissionDto.getAppEditSelectDto();
            List<String> personnelEditList = showEdit.getPersonnelEditList();
            if (personnelEditList == null) {
                personnelEditList = IaisCommonUtils.genNewArrayList();
            }
            personnelEditList.add(HcsaConsts.STEP_VEHICLES);
            showEdit.setPersonnelEditList(personnelEditList);
            appSubmissionDto.setAppEditSelectDto(showEdit);
        }
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
        List<AppSvcPrincipalOfficersDto> newAppSvcPrincipalOfficersDto = appSvcRelatedInfoDtoList.getAppSvcPrincipalOfficersDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDto = oldAppSvcRelatedInfoDtoList.getAppSvcPrincipalOfficersDtoList();
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
                newPoIdNos.add(item.getIdNo());
            } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(item.getPsnType())) {
                newDpoIdNos.add(item.getIdNo());
            }
        }
        for (AppSvcPrincipalOfficersDto item : oldAppSvcPrincipalOfficersDto) {
            if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(item.getPsnType())) {
                oldPoIdNos.add(item.getIdNo());
            } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(item.getPsnType())) {
                olddDpoIdNos.add(item.getIdNo());
            }
        }
        if (!newPoIdNos.equals(oldPoIdNos)) {
            isAuto = false;
            personnelEditList.add(HcsaConsts.STEP_PRINCIPAL_OFFICERS);
        }
        if (!newDpoIdNos.equals(olddDpoIdNos)) {
            isAuto = false;
            if (!personnelEditList.contains(HcsaConsts.STEP_PRINCIPAL_OFFICERS)) {
                personnelEditList.add(HcsaConsts.STEP_PRINCIPAL_OFFICERS);
            }
            personnelEditList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
        }
        // CGO
        List<AppSvcPrincipalOfficersDto> newAppSvcCgoDto = appSvcRelatedInfoDtoList.getAppSvcCgoDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDto = oldAppSvcRelatedInfoDtoList.getAppSvcCgoDtoList();
        List<String> newIdNos = IaisCommonUtils.genNewArrayList();
        List<String> oldIdNos = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(newAppSvcCgoDto) && !IaisCommonUtils.isEmpty(oldAppSvcCgoDto)) {
            for (AppSvcPrincipalOfficersDto item : newAppSvcCgoDto) {
                newIdNos.add(item.getIdNo());
            }
            for (AppSvcPrincipalOfficersDto item : oldAppSvcCgoDto) {
                oldIdNos.add(item.getIdNo());
            }
            if (!newIdNos.equals(oldIdNos)) {
                isAuto = false;
                personnelEditList.add(HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS);
            }
        }
        // CD
        List<AppSvcPrincipalOfficersDto> newAppSvcCdDto = appSvcRelatedInfoDtoList.getAppSvcClinicalDirectorDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcCdDto = oldAppSvcRelatedInfoDtoList.getAppSvcClinicalDirectorDtoList();
        newIdNos = IaisCommonUtils.genNewArrayList();
        oldIdNos = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(newAppSvcCdDto) && !IaisCommonUtils.isEmpty(oldAppSvcCdDto)) {
            for (AppSvcPrincipalOfficersDto item : newAppSvcCdDto) {
                newIdNos.add(item.getIdNo());
            }
            for (AppSvcPrincipalOfficersDto item : oldAppSvcCdDto) {
                oldIdNos.add(item.getIdNo());
            }
            if (!newIdNos.equals(oldIdNos)) {
                isAuto = false;
                personnelEditList.add(HcsaConsts.STEP_CLINICAL_DIRECTOR);
            }
        }
        // MAP
        List<AppSvcPrincipalOfficersDto> newAppSvcMapDto = appSvcRelatedInfoDtoList.getAppSvcMedAlertPersonList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcMapDto = oldAppSvcRelatedInfoDtoList.getAppSvcMedAlertPersonList();
        newIdNos = IaisCommonUtils.genNewArrayList();
        oldIdNos = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(newAppSvcMapDto) && !IaisCommonUtils.isEmpty(oldAppSvcMapDto)) {
            for (AppSvcPrincipalOfficersDto item : newAppSvcMapDto) {
                newIdNos.add(item.getIdNo());
            }
            for (AppSvcPrincipalOfficersDto item : oldAppSvcMapDto) {
                oldIdNos.add(item.getIdNo());
            }
            if (!newIdNos.equals(oldIdNos)) {
                isAuto = false;
                personnelEditList.add(HcsaConsts.STEP_MEDALERT_PERSON);
            }
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
                            if (!IaisCommonUtils.isEmpty(oldAppSvcRelatedInfoDtoList.getAppSvcDisciplineAllocationDtoList())) {
                                personnelEditList.add(HcsaConsts.STEP_DISCIPLINE_ALLOCATION);
                            }
                            break;
                        }
                    }
                }
            }
            if (!newDisciplinesDto.equals(oldDisciplinesDto)) {

            }
        }
        // KAH
        List<AppSvcPrincipalOfficersDto> kahList = appSvcRelatedInfoDtoList.getAppSvcKeyAppointmentHolderDtoList();
        List<AppSvcPrincipalOfficersDto> oldKahList = oldAppSvcRelatedInfoDtoList.getAppSvcKeyAppointmentHolderDtoList();
        List<String> newKahIdNos = IaisCommonUtils.genNewArrayList();
        List<String> oldKahIdNos = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(kahList) && !IaisCommonUtils.isEmpty(oldKahList)) {
            for (AppSvcPrincipalOfficersDto item : kahList) {
                newKahIdNos.add(item.getIdNo());
            }
            for (AppSvcPrincipalOfficersDto item : oldKahList) {
                oldKahIdNos.add(item.getIdNo());
            }
            if (!newKahIdNos.equals(oldKahIdNos)) {
                isAuto = false;
                personnelEditList.add(HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER);
            }
        }
        appEditSelectDto.setPersonnelEditList(personnelEditList);
        appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
        checkAppSvcInfoOtherSteps(appSubmissionDto, oldAppSubmissionDto);
        return isAuto;
    }

    public static void checkAppSvcInfoOtherSteps(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto) {
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
        if (appEditSelectDto == null || !appEditSelectDto.isServiceEdit()) {
            return;
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        List<String> personnelEditList = appEditSelectDto.getPersonnelEditList();
        if (personnelEditList == null) {
            personnelEditList = IaisCommonUtils.genNewArrayList();
        }
        // check allocation step
        if (!personnelEditList.contains(HcsaConsts.STEP_DISCIPLINE_ALLOCATION)) {
            List<AppSvcDisciplineAllocationDto> allocationDtoList = appSvcRelatedInfoDtoList.getAppSvcDisciplineAllocationDtoList();
            List<AppSvcDisciplineAllocationDto> oldAllocationDtoList =
                    oldAppSvcRelatedInfoDtoList.getAppSvcDisciplineAllocationDtoList();
            if (oldAllocationDtoList != null && !Objects.equals(allocationDtoList, oldAllocationDtoList)) {
                personnelEditList.add(HcsaConsts.STEP_DISCIPLINE_ALLOCATION);
            }
        }

        List<AppSvcBusinessDto> appSvcBusinessDtoList = appSvcRelatedInfoDtoList.getAppSvcBusinessDtoList();
        List<AppSvcBusinessDto> oldBusinessDtoList = oldAppSvcRelatedInfoDtoList.getAppSvcBusinessDtoList();
        if (!Objects.equals(appSvcBusinessDtoList, oldBusinessDtoList)) {
            personnelEditList.add(HcsaConsts.STEP_BUSINESS_NAME);
        }
    }

    private static AppSvcLaboratoryDisciplinesDto getDisciplinesDto(List<AppSvcLaboratoryDisciplinesDto> disciplinesDto, String hciName) {
        for (AppSvcLaboratoryDisciplinesDto iten : disciplinesDto) {
            if (hciName.equals(iten.getPremiseVal())) {
                return iten;
            }
        }
        return new AppSvcLaboratoryDisciplinesDto();
    }

    public static AppSubmissionDto generateDtosForAutoPremesis(AppSubmissionDto srcDto, List<AppGrpPremisesDto> autoPremisesDtos,
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
    }

}
