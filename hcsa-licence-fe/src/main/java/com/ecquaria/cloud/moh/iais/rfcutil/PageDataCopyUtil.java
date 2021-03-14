package com.ecquaria.cloud.moh.iais.rfcutil;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class PageDataCopyUtil {

    public static List<AppGrpPremisesDto> copyAppGrpPremises(List<AppGrpPremisesDto> appGrpPremisesDtoList) {
        List<AppGrpPremisesDto> cpoyList=new ArrayList<>(appGrpPremisesDtoList.size());
        for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
            AppGrpPremisesDto copy = copyAppGrpPremisesDto(appGrpPremisesDto);
            cpoyList.add( copy);
        }

        return cpoyList;
    }
    public static AppGrpPremisesDto copyAppGrpPremisesDto(AppGrpPremisesDto appGrpPremisesDto){
        AppGrpPremisesDto copy=new AppGrpPremisesDto();
        copy.setPremisesType(appGrpPremisesDto.getPremisesType());
        copy.setPostalCode(appGrpPremisesDto.getPostalCode());
        copy.setAddrType(appGrpPremisesDto.getAddrType());
        copy.setBlkNo(appGrpPremisesDto.getBlkNo());
        copy.setFloorNo(appGrpPremisesDto.getFloorNo());
        copy.setUnitNo(appGrpPremisesDto.getUnitNo());
        copy.setStreetName(appGrpPremisesDto.getStreetName());
        copy.setBuildingName(appGrpPremisesDto.getBuildingName());
        copy.setOnsiteStartMM(appGrpPremisesDto.getOnsiteStartMM());
        copy.setOnsiteEndMM(appGrpPremisesDto.getOnsiteEndMM());
        copy.setOnsiteStartHH(appGrpPremisesDto.getOnsiteStartHH());
        copy.setOnsiteEndHH(appGrpPremisesDto.getOnsiteEndHH());

        copy.setOffSitePostalCode(appGrpPremisesDto.getOffSitePostalCode());
        copy.setOffSiteAddressType(appGrpPremisesDto.getOffSiteAddressType());
        copy.setOffSiteBlockNo(appGrpPremisesDto.getOffSiteBlockNo());
        copy.setOffSiteFloorNo(appGrpPremisesDto.getOffSiteFloorNo());
        copy.setOffSiteUnitNo(appGrpPremisesDto.getOffSiteUnitNo());
        copy.setOffSiteStreetName(appGrpPremisesDto.getOffSiteStreetName());
        copy.setOffSiteBuildingName(appGrpPremisesDto.getOffSiteBuildingName());
        copy.setOffSiteStartHH(appGrpPremisesDto.getOffSiteStartHH());
        copy.setOffSiteStartMM(appGrpPremisesDto.getOffSiteStartMM());
        copy.setOffSiteEndHH(appGrpPremisesDto.getOffSiteEndHH());
        copy.setOffSiteEndMM(appGrpPremisesDto.getOffSiteEndMM());

        copy.setConveyancePostalCode(appGrpPremisesDto.getConveyancePostalCode());
        copy.setConveyanceAddressType(appGrpPremisesDto.getConveyanceAddressType());
        copy.setConveyanceBlockNo(appGrpPremisesDto.getConveyanceBlockNo());
        copy.setConveyanceFloorNo(appGrpPremisesDto.getConveyanceFloorNo());
        copy.setConveyanceUnitNo(appGrpPremisesDto.getConveyanceUnitNo());
        copy.setConveyanceStreetName(appGrpPremisesDto.getConveyanceStreetName());
        copy.setConveyanceBuildingName(appGrpPremisesDto.getConveyanceBuildingName());
        copy.setConStartHH(appGrpPremisesDto.getConStartHH());
        copy.setConStartMM(appGrpPremisesDto.getConStartMM());
        copy.setConEndHH(appGrpPremisesDto.getConEndHH());
        copy.setConEndMM(appGrpPremisesDto.getConEndMM());
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
            copy.setHciName(appGrpPremisesDto.getHciName());
            copy.setOffTelNo(appGrpPremisesDto.getOffTelNo());
            copy.setLocateWithOthers(appGrpPremisesDto.getLocateWithOthers());
            copy.setScdfRefNo(appGrpPremisesDto.getScdfRefNo());
            copy.setCertIssuedDt(appGrpPremisesDto.getCertIssuedDt());
            if(StringUtil.isEmpty(appGrpPremisesDto.getCertIssuedDtStr())){
                copy.setCertIssuedDtStr(null);
            }else {
                copy.setCertIssuedDtStr(appGrpPremisesDto.getCertIssuedDtStr());
            }
        }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(appGrpPremisesDto.getPremisesType())){
            copy.setOffSiteHciName(appGrpPremisesDto.getOffSiteHciName());
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            copy.setConveyanceVehicleNo(appGrpPremisesDto.getConveyanceVehicleNo());
            copy.setConveyanceHciName(appGrpPremisesDto.getConveyanceHciName());
        }
        List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtoList = copyAppPremisesOperationalUnitDto(appGrpPremisesDto.getAppPremisesOperationalUnitDtos());
        copy.setAppPremisesOperationalUnitDtos(appPremisesOperationalUnitDtoList);
        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDto.getAppPremPhOpenPeriodList();
        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos=new ArrayList<>();
        if(appPremPhOpenPeriodList!=null){
            for(AppPremPhOpenPeriodDto appPremPhOpenPeriodDto : appPremPhOpenPeriodList){
                AppPremPhOpenPeriodDto premPhOpenPeriodDto=new AppPremPhOpenPeriodDto();
                premPhOpenPeriodDto.setConvEndToMM(appPremPhOpenPeriodDto.getConvEndToMM());
                premPhOpenPeriodDto.setConvEndToHH(appPremPhOpenPeriodDto.getConvEndToHH());
                premPhOpenPeriodDto.setConvStartFromHH(appPremPhOpenPeriodDto.getConvStartFromHH());
                premPhOpenPeriodDto.setConvStartFromMM(appPremPhOpenPeriodDto.getConvStartFromMM());
                premPhOpenPeriodDto.setOnsiteStartFromMM(appPremPhOpenPeriodDto.getOnsiteStartFromMM());
                premPhOpenPeriodDto.setOnsiteStartFromHH(appPremPhOpenPeriodDto.getOnsiteStartFromHH());
                premPhOpenPeriodDto.setOnsiteEndToHH(appPremPhOpenPeriodDto.getOnsiteEndToHH());
                premPhOpenPeriodDto.setOnsiteEndToMM(appPremPhOpenPeriodDto.getOnsiteEndToMM());
                premPhOpenPeriodDto.setPhDate(appPremPhOpenPeriodDto.getPhDate());
                premPhOpenPeriodDto.setPhDateStr(appPremPhOpenPeriodDto.getPhDateStr());
                premPhOpenPeriodDto.setStartFrom(appPremPhOpenPeriodDto.getStartFrom());
                premPhOpenPeriodDto.setEndTo(appPremPhOpenPeriodDto.getEndTo());
                appPremPhOpenPeriodDtos.add(premPhOpenPeriodDto);
            }

        }
        copy.setEventDtoList(copyEvent(appGrpPremisesDto.getEventDtoList()));
        copy.setWeeklyDtoList(copyOperationHoursReloadDto(appGrpPremisesDto.getWeeklyDtoList()));
        copy.setPhDtoList(copyOperationHoursReloadDto(appGrpPremisesDto.getPhDtoList()));
        copy.setAppPremPhOpenPeriodList(appPremPhOpenPeriodDtos);
        return copy;
    }
    public static List<OperationHoursReloadDto> copyOperationHoursReloadDto(List<OperationHoursReloadDto> list){
        if(list==null){
            return new ArrayList<>();
        }
        List<OperationHoursReloadDto> operationHoursReloadDtoList=new ArrayList<>(list.size());
        for(OperationHoursReloadDto operationHoursReloadDto : list){
            OperationHoursReloadDto o=new OperationHoursReloadDto();
            o.setSelectValList(operationHoursReloadDto.getSelectValList());
            o.setSelectAllDay(operationHoursReloadDto.isSelectAllDay());
            o.setStartFrom(operationHoursReloadDto.getStartFrom());
            o.setEndTo(operationHoursReloadDto.getEndTo());
            operationHoursReloadDtoList.add(o);
        }
        return operationHoursReloadDtoList;
    }
    public static List<AppPremEventPeriodDto> copyEvent(List<AppPremEventPeriodDto> list){
        if(list==null){
            return new ArrayList<>();
        }
        List<AppPremEventPeriodDto> appPremEventPeriodDtoList=new ArrayList<>(list.size());
        for(AppPremEventPeriodDto appPremEventPeriodDto : list){
            AppPremEventPeriodDto o=new AppPremEventPeriodDto();
            o.setEventName(appPremEventPeriodDto.getEventName());
            o.setEndDate(appPremEventPeriodDto.getEndDate());
            o.setStartDate(o.getStartDate());
            appPremEventPeriodDtoList.add(o);
        }
        return appPremEventPeriodDtoList;
    }
    public static List<AppPremisesOperationalUnitDto> copyAppPremisesOperationalUnitDto(List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos){

        List<AppPremisesOperationalUnitDto> list= IaisCommonUtils.genNewArrayList();
        for(AppPremisesOperationalUnitDto appPremisesOperationalUnitDto : appPremisesOperationalUnitDtos){
            AppPremisesOperationalUnitDto operationalUnitDto=new AppPremisesOperationalUnitDto();
            operationalUnitDto.setFloorNo(appPremisesOperationalUnitDto.getFloorNo());
            operationalUnitDto.setUnitNo(appPremisesOperationalUnitDto.getUnitNo());
            list.add(operationalUnitDto);
        }
        return list;
    }
    public static List<AppGrpPrimaryDocDto> copyGrpPrimaryDoc(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos){

        if(appGrpPrimaryDocDtos==null){
            return new ArrayList<>();
        }
        List<AppGrpPrimaryDocDto> list=new ArrayList<>(appGrpPrimaryDocDtos.size());
        for(AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos){
            AppGrpPrimaryDocDto primaryDocDto=new AppGrpPrimaryDocDto();
            primaryDocDto.setDocName(appGrpPrimaryDocDto.getDocName());
            primaryDocDto.setDocSize(appGrpPrimaryDocDto.getDocSize());
            primaryDocDto.setSvcComDocName(appGrpPrimaryDocDto.getSvcComDocName());
            primaryDocDto.setMd5Code(appGrpPrimaryDocDto.getMd5Code());
            list.add(primaryDocDto);
        }
        return list;
    }
    public static List<AppSvcDocDto> copySvcDoc(List<AppSvcDocDto> appSvcDocDtoLit){
        List<AppSvcDocDto> appSvcDocDtos=new ArrayList<>(appSvcDocDtoLit.size());
        for(AppSvcDocDto appSvcDocDto : appSvcDocDtoLit){
            AppSvcDocDto svcDocDto=new AppSvcDocDto();
            svcDocDto.setSvcDocId(appSvcDocDto.getSvcDocId());
            svcDocDto.setDocName(appSvcDocDto.getDocName());
            svcDocDto.setDocSize(appSvcDocDto.getDocSize());
            svcDocDto.setFileRepoId(appSvcDocDto.getFileRepoId());
            //premiseVal May be ""
            svcDocDto.setMd5Code(appSvcDocDto.getMd5Code());
            appSvcDocDtos.add(svcDocDto);
        }
        return appSvcDocDtos;
    }
    public static List<AppSvcPrincipalOfficersDto> copyMedaler(List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList) {
        List<AppSvcPrincipalOfficersDto> list=IaisCommonUtils.genNewArrayList();
        for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcMedAlertPersonList) {
            AppSvcPrincipalOfficersDto svcPrincipalOfficersDto=new AppSvcPrincipalOfficersDto();
            svcPrincipalOfficersDto.setSalutation(appSvcPrincipalOfficersDto.getSalutation());
            svcPrincipalOfficersDto.setName(appSvcPrincipalOfficersDto.getName());
            svcPrincipalOfficersDto.setIdType(appSvcPrincipalOfficersDto.getIdType());
            svcPrincipalOfficersDto.setIdNo(appSvcPrincipalOfficersDto.getIdNo());
            svcPrincipalOfficersDto.setMobileNo(appSvcPrincipalOfficersDto.getMobileNo());
            svcPrincipalOfficersDto.setEmailAddr(appSvcPrincipalOfficersDto.getEmailAddr());
            svcPrincipalOfficersDto.setDescription(appSvcPrincipalOfficersDto.getDescription());
            list.add(svcPrincipalOfficersDto);
        }
        return list;
    }
    public static List<AppSvcPrincipalOfficersDto> copyAppSvcPo(List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList)  {
        List<AppSvcPrincipalOfficersDto> list=IaisCommonUtils.genNewArrayList();
        for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtoList) {
            AppSvcPrincipalOfficersDto svcPrincipalOfficersDto=new AppSvcPrincipalOfficersDto();
            svcPrincipalOfficersDto.setSalutation(appSvcPrincipalOfficersDto.getSalutation());
            svcPrincipalOfficersDto.setName(appSvcPrincipalOfficersDto.getName());
            svcPrincipalOfficersDto.setIdType(appSvcPrincipalOfficersDto.getIdType());
            svcPrincipalOfficersDto.setIdNo(appSvcPrincipalOfficersDto.getIdNo());
            svcPrincipalOfficersDto.setDesignation(appSvcPrincipalOfficersDto.getDesignation());
            svcPrincipalOfficersDto.setOfficeTelNo(appSvcPrincipalOfficersDto.getOfficeTelNo());
            svcPrincipalOfficersDto.setEmailAddr(appSvcPrincipalOfficersDto.getEmailAddr());
            svcPrincipalOfficersDto.setMobileNo(appSvcPrincipalOfficersDto.getMobileNo());
            list.add(svcPrincipalOfficersDto);
        }
        return list;
    }
    public static List<AppSvcCgoDto> copyAppSvcCgo(List<AppSvcCgoDto> appSvcCgoDtoList) {
        List<AppSvcCgoDto> list=IaisCommonUtils.genNewArrayList();
        for (AppSvcCgoDto appSvcCgoDto : appSvcCgoDtoList) {
            AppSvcCgoDto cgoDto=new AppSvcCgoDto();
            cgoDto.setSalutation(appSvcCgoDto.getSalutation());
            cgoDto.setName(appSvcCgoDto.getName());
            cgoDto.setIdNo(appSvcCgoDto.getIdNo());
            cgoDto.setIdType(appSvcCgoDto.getIdType());
            cgoDto.setDesignation(appSvcCgoDto.getDesignation());
            cgoDto.setProfessionType(appSvcCgoDto.getProfessionType());
            cgoDto.setProfRegNo(appSvcCgoDto.getProfRegNo());
            cgoDto.setSpeciality(appSvcCgoDto.getSpeciality());
            cgoDto.setSubSpeciality(appSvcCgoDto.getSubSpeciality());
            cgoDto.setMobileNo(appSvcCgoDto.getMobileNo());
            cgoDto.setEmailAddr(appSvcCgoDto.getEmailAddr());
            list.add(cgoDto);
        }
        return list;
    }
    public static List<AppSvcChckListDto> copyAppSvcChckListDto(List<AppSvcChckListDto> appSvcChckListDtos){
        List<AppSvcChckListDto> list=IaisCommonUtils.genNewArrayList();
        if(appSvcChckListDtos!=null){
            for(AppSvcChckListDto appSvcChckListDto : appSvcChckListDtos){
                AppSvcChckListDto svcChckListDto=new AppSvcChckListDto();
                svcChckListDto.setChkLstConfId(appSvcChckListDto.getChkLstConfId());
                svcChckListDto.setChkName(appSvcChckListDto.getChkName());
                list.add(svcChckListDto) ;
            }
        }
        return list;
    }
    public static List<AppSvcDisciplineAllocationDto>  copyAppSvcDisciplineAllocationDto(List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList){
        if(appSvcDisciplineAllocationDtoList==null){
            return new ArrayList<>();
        }
        List<AppSvcDisciplineAllocationDto> list=new ArrayList<>(appSvcDisciplineAllocationDtoList.size());
        for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : appSvcDisciplineAllocationDtoList){

        }
        return list;
    };
}
