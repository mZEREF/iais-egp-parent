package com.ecquaria.cloud.moh.iais.rfcutil;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import sop.util.CopyUtil;

import java.util.ArrayList;
import java.util.List;

public class EqRequestForChangeSubmitResultChange {

    public static boolean eqHciNameChange(AppGrpPremisesDto appGrpPremisesDto ,AppGrpPremisesDto oldAppGrpPremisesDto){
        String hciName = appGrpPremisesDto.getHciName();
        String oldHciName = oldAppGrpPremisesDto.getHciName();
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){

        }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(appGrpPremisesDto.getPremisesType())){
            hciName=appGrpPremisesDto.getOffSiteHciName();
            oldHciName=oldAppGrpPremisesDto.getOffSiteHciName();
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            hciName=appGrpPremisesDto.getConveyanceHciName();
            oldHciName=oldAppGrpPremisesDto.getConveyanceHciName();
        }

        if(hciName==null&&oldHciName==null){
            return false;
        }else if(hciName==null&&oldHciName!=null){
            return true;
        }else if(hciName!=null&&oldHciName==null){
            return true;
        }
        if(!hciName.equals(oldHciName)){
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
    public static boolean eqGrpPremises(List<AppGrpPremisesDto> appGrpPremisesDtoList, List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) {
        List<AppGrpPremisesDto> appGrpPremisesDtos = PageDataCopyUtil.copyAppGrpPremises(appGrpPremisesDtoList);
        List<AppGrpPremisesDto> oldAppGrpPremisesDtos = PageDataCopyUtil.copyAppGrpPremises(oldAppGrpPremisesDtoList);
        if (!appGrpPremisesDtos.equals(oldAppGrpPremisesDtos)) {
            return true;
        }
        return false;
    }
    public static  boolean eqServiceChange(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) throws Exception {
        List<AppSvcRelatedInfoDto> n = (List<AppSvcRelatedInfoDto>) CopyUtil.copyMutableObject(appSvcRelatedInfoDtoList);
        List<AppSvcRelatedInfoDto> o = (List<AppSvcRelatedInfoDto>) CopyUtil.copyMutableObject(oldAppSvcRelatedInfoDtoList);
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
        List<AppSvcPersonnelDto> appSvcPersonnelDtoList = n.get(0).getAppSvcPersonnelDtoList();
        List<AppSvcPersonnelDto> oldAppSvcPersonnelDtoList = o.get(0).getAppSvcPersonnelDtoList();
        boolean eqServicePseronnel = eqServicePseronnel(appSvcPersonnelDtoList, oldAppSvcPersonnelDtoList);
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = n.get(0).getAppSvcPrincipalOfficersDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtoList = o.get(0).getAppSvcPrincipalOfficersDtoList();
        boolean eqSvcPrincipalOfficers = eqSvcPrincipalOfficers(appSvcPrincipalOfficersDtoList, oldAppSvcPrincipalOfficersDtoList);

        List<AppSvcCgoDto> appSvcCgoDtoList = n.get(0).getAppSvcCgoDtoList();
        List<AppSvcCgoDto> oldAppSvcCgoDtoList = o.get(0).getAppSvcCgoDtoList();
        boolean eqCgo = eqCgo(appSvcCgoDtoList, oldAppSvcCgoDtoList);

        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = n.get(0).getAppSvcMedAlertPersonList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList = o.get(0).getAppSvcMedAlertPersonList();
        boolean eqMeadrter = eqMeadrter(appSvcMedAlertPersonList, oldAppSvcMedAlertPersonList);
        List<AppSvcDocDto> appSvcDocDtoLit = n.get(0).getAppSvcDocDtoLit();
        List<AppSvcDocDto> oldAppSvcDocDtoLit = o.get(0).getAppSvcDocDtoLit();
        boolean eqSvcDoc = eqSvcDoc(appSvcDocDtoLit, oldAppSvcDocDtoLit);
        boolean eqAppSvcVehicle = eqAppSvcVehicleDto(n.get(0).getAppSvcVehicleDtoList(), o.get(0).getAppSvcVehicleDtoList());
        boolean eqAppSvcClinicalDirector = eqAppSvcClinicalDirector(n.get(0).getAppSvcClinicalDirectorDtoList(), o.get(0).getAppSvcClinicalDirectorDtoList());
        boolean eqAppSvcChargesPageDto = eqAppSvcChargesPageDto(n.get(0).getAppSvcChargesPageDto(), o.get(0).getAppSvcChargesPageDto());
        if (!flag || !flag1 || eqSvcPrincipalOfficers || eqCgo || eqMeadrter || eqServicePseronnel || eqSvcDoc || eqAppSvcVehicle || eqAppSvcClinicalDirector ||eqAppSvcChargesPageDto) {
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
    private static boolean eqCgo(List<AppSvcCgoDto> appSvcCgoDtoList, List<AppSvcCgoDto> oldAppSvcCgoDtoList)  {
        if (appSvcCgoDtoList != null && oldAppSvcCgoDtoList != null) {
            List<AppSvcCgoDto> n = PageDataCopyUtil.copyAppSvcCgo(appSvcCgoDtoList);
            List<AppSvcCgoDto> o = PageDataCopyUtil.copyAppSvcCgo(oldAppSvcCgoDtoList);
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
                boolean b = EqRequestForChangeSubmitResultChange.eqAppSvcDisciplineAllocationDto(appSvcDisciplineAllocationDtoList, oldAppSvcDisciplineAllocationDtoList);
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
        List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
        List<AppSvcCgoDto> oldAppSvcCgoDtoList = oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList();
        if (oldAppSvcCgoDtoList != null) {
            if (oldAppSvcCgoDtoList.size() != appSvcCgoDtoList.size()) {
                return true;
            } else if (oldAppSvcCgoDtoList.size() == appSvcCgoDtoList.size()) {
                int i = 0;
                for (AppSvcCgoDto appSvcCgoDto : oldAppSvcCgoDtoList) {
                    for (AppSvcCgoDto appSvcCgoDto1 : appSvcCgoDtoList) {
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
        AppSvcChargesPageDto appSvcChargesPageDto = appSvcRelatedInfoDto.getAppSvcChargesPageDto();
        AppSvcChargesPageDto oldAppSvcChargesPageDto = oldAppSvcRelatedInfoDto.getAppSvcChargesPageDto();
        boolean compareAppSvcChargesPage = compareAppSvcChargesPage(appSvcChargesPageDto, oldAppSvcChargesPageDto);
        if(compareAppSvcChargesPage){
           return compareAppSvcChargesPage;
        }
        List<AppSvcClinicalDirectorDto> appSvcClinicalDirectorDtoList = appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();
        List<AppSvcClinicalDirectorDto> oldAppSvcClinicalDirectorDtoList = oldAppSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();
        if(oldAppSvcClinicalDirectorDtoList!=null){
            if(appSvcClinicalDirectorDtoList.size()!=oldAppSvcClinicalDirectorDtoList.size()){
                return true;
            }else if(appSvcClinicalDirectorDtoList.size()==oldAppSvcClinicalDirectorDtoList.size()){
                int i=0;
                for (AppSvcClinicalDirectorDto v : appSvcClinicalDirectorDtoList) {
                    for (AppSvcClinicalDirectorDto v1 : oldAppSvcClinicalDirectorDtoList) {
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

        String newHciName = "";
        String oldHciName = "";
        String newVehicleNo="";
        String oldVehicleNo="";
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesListQueryDto.getPremisesType())) {
            oldHciName = premisesListQueryDto.getHciName();
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesListQueryDto.getPremisesType())) {
            oldHciName = premisesListQueryDto.getConveyanceHciName();
            oldVehicleNo=premisesListQueryDto.getConveyanceVehicleNo();
        }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(appGrpPremisesDto.getPremisesType())){
            oldHciName = premisesListQueryDto.getOffSiteHciName();
        }else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            oldHciName=premisesListQueryDto.getEasMtsHciName();

        }
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())) {
            newHciName = appGrpPremisesDto.getHciName();
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())) {
            newHciName = appGrpPremisesDto.getConveyanceHciName();
            newVehicleNo=appGrpPremisesDto.getConveyanceVehicleNo();
        }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(appGrpPremisesDto.getPremisesType())){
            newHciName = appGrpPremisesDto.getOffSiteHciName();
        }else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            newHciName=appGrpPremisesDto.getEasMtsHciName();
        }

        if (!newHciName.equals(oldHciName) || !newVehicleNo.equals(oldVehicleNo)) {
            return false;
        }

        return true;
    }

    public static boolean eqAppSvcVehicleDto(List<AppSvcVehicleDto> appSvcVehicleDtoList ,List<AppSvcVehicleDto> oldAppSvcVehicleDtoList){
        List<AppSvcVehicleDto> n = PageDataCopyUtil.copyAppSvcVehicleDto(appSvcVehicleDtoList);
        List<AppSvcVehicleDto> o = PageDataCopyUtil.copyAppSvcVehicleDto(oldAppSvcVehicleDtoList);
        if(!n.equals(o)){
        return true;
        }
        return false;
    }
    public static boolean eqAppSvcChargesPageDto(AppSvcChargesPageDto appSvcChargesPageDto,AppSvcChargesPageDto oldAppSvcChargesPageDto){
        AppSvcChargesPageDto n = PageDataCopyUtil.copyAppSvcClinicalDirector(appSvcChargesPageDto);
        AppSvcChargesPageDto o = PageDataCopyUtil.copyAppSvcClinicalDirector(oldAppSvcChargesPageDto);
        if(!n.equals(o)){
            return true;
        }
        return false;
    }

    public static boolean eqAppSvcClinicalDirector(List<AppSvcClinicalDirectorDto> appSvcClinicalDirectorDtos,List<AppSvcClinicalDirectorDto> oldAppSvcClinicalDirectorDtos){
        List<AppSvcClinicalDirectorDto> n = PageDataCopyUtil.copyAppSvcClinicalDirector(appSvcClinicalDirectorDtos);
        List<AppSvcClinicalDirectorDto> o = PageDataCopyUtil.copyAppSvcClinicalDirector(oldAppSvcClinicalDirectorDtos);
        if(!n.equals(o)){
            return true;
        }
        return false;
    }
}
