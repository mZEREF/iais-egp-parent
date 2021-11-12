package com.ecquaria.cloud.moh.iais.rfcutil;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.OperationHoursReloadDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        AppGrpPremisesDto copy = new AppGrpPremisesDto();
        copy.setPremisesType(appGrpPremisesDto.getPremisesType());
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())) {
            copy.setPostalCode(appGrpPremisesDto.getPostalCode());
            copy.setAddrType(appGrpPremisesDto.getAddrType());
            copy.setBlkNo(appGrpPremisesDto.getBlkNo());
            copy.setFloorNo(appGrpPremisesDto.getFloorNo());
            copy.setUnitNo(appGrpPremisesDto.getUnitNo());
            copy.setStreetName(appGrpPremisesDto.getStreetName());
            copy.setBuildingName(appGrpPremisesDto.getBuildingName());
            copy.setEasMtsPubEmail(appGrpPremisesDto.getEasMtsPubEmail());
            copy.setOnsiteStartMM(appGrpPremisesDto.getOnsiteStartMM());
            copy.setOnsiteEndMM(appGrpPremisesDto.getOnsiteEndMM());
            copy.setOnsiteStartHH(appGrpPremisesDto.getOnsiteStartHH());
            copy.setOnsiteEndHH(appGrpPremisesDto.getOnsiteEndHH());

            copy.setHciName(appGrpPremisesDto.getHciName());
            copy.setOffTelNo(appGrpPremisesDto.getOffTelNo());
            copy.setLocateWithOthers(appGrpPremisesDto.getLocateWithOthers());
            copy.setScdfRefNo(appGrpPremisesDto.getScdfRefNo());
            copy.setCertIssuedDt(appGrpPremisesDto.getCertIssuedDt());
            if (StringUtil.isEmpty(appGrpPremisesDto.getCertIssuedDtStr())) {
                copy.setCertIssuedDtStr(null);
            } else {
                copy.setCertIssuedDtStr(appGrpPremisesDto.getCertIssuedDtStr());
            }
        } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(appGrpPremisesDto.getPremisesType())) {
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

            copy.setOffSiteHciName(appGrpPremisesDto.getOffSiteHciName());

            copy.setOffSiteEmail(appGrpPremisesDto.getOffSiteEmail());
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
            copy.setEasMtsUseOnly(appGrpPremisesDto.getEasMtsUseOnly());
            copy.setEasMtsPubEmail(appGrpPremisesDto.getEasMtsPubEmail());
            copy.setEasMtsPubHotline(appGrpPremisesDto.getEasMtsPubHotline());
            copy.setEasMtsCoLocation(appGrpPremisesDto.getEasMtsCoLocation());
        } else {
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

            copy.setConveyanceVehicleNo(appGrpPremisesDto.getConveyanceVehicleNo());
            copy.setConveyanceHciName(appGrpPremisesDto.getConveyanceHciName());

            copy.setConveyanceEmail(appGrpPremisesDto.getConveyanceEmail());
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
            //o.setStartDate(o.getStartDate());
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
            if(StringUtil.isEmpty(appGrpPrimaryDocDto.getMd5Code())){
                continue;
            }
            AppGrpPrimaryDocDto primaryDocDto=new AppGrpPrimaryDocDto();
            primaryDocDto.setDocName(appGrpPrimaryDocDto.getDocName());
            primaryDocDto.setDocSize(appGrpPrimaryDocDto.getDocSize());
            primaryDocDto.setSvcComDocName(appGrpPrimaryDocDto.getSvcComDocName());
            primaryDocDto.setMd5Code(appGrpPrimaryDocDto.getMd5Code());

            list.add(primaryDocDto);
        }
        Collections.sort(list, Comparator.comparing(AppGrpPrimaryDocDto::getMd5Code));
        return list;
    }

    public static List<AppSvcPersonnelDto> copySvcPersonnel (List<AppSvcPersonnelDto> appSvcPersonnelDtoList) {
        if (appSvcPersonnelDtoList == null || appSvcPersonnelDtoList.isEmpty()) {
            return appSvcPersonnelDtoList;
        }
        List<AppSvcPersonnelDto> result = IaisCommonUtils.genNewArrayList(appSvcPersonnelDtoList.size());
        for (AppSvcPersonnelDto svcPersonnelDto : appSvcPersonnelDtoList) {
            AppSvcPersonnelDto dto = new AppSvcPersonnelDto();
            dto.setSalutation(StringUtil.getNonNull(svcPersonnelDto.getSalutation()));
            dto.setPersonnelType(svcPersonnelDto.getPersonnelType());
            if (StringUtil.isEmpty(dto.getPersonnelType())) {
                dto.setPersonnelType(ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL);
            }
            dto.setName(svcPersonnelDto.getName());
            dto.setDesignation(svcPersonnelDto.getDesignation());
            dto.setOtherDesignation(svcPersonnelDto.getOtherDesignation());
            dto.setProfRegNo(svcPersonnelDto.getProfRegNo());
            dto.setWrkExpYear(StringUtil.getNonNull(svcPersonnelDto.getWrkExpYear()));
            dto.setQualification(StringUtil.getNonNull(svcPersonnelDto.getQualification()));
            result.add(dto);
        }
        return result.stream()
                .sorted(Comparator.comparing(AppSvcPersonnelDto::getName)
                        .thenComparing(AppSvcPersonnelDto::getSalutation)
                        .thenComparing(AppSvcPersonnelDto::getPersonnelType)
                        .thenComparing(AppSvcPersonnelDto::getQualification)
                        .thenComparing(AppSvcPersonnelDto::getWrkExpYear))
                .collect(Collectors.toList());
    }

    public static List<AppSvcDocDto> copySvcDoc(List<AppSvcDocDto> appSvcDocDtoLit){
        List<AppSvcDocDto> appSvcDocDtos=new ArrayList<>(appSvcDocDtoLit.size());
        for(AppSvcDocDto appSvcDocDto : appSvcDocDtoLit){
            if(StringUtil.isEmpty(appSvcDocDto.getMd5Code())){
                continue;
            }
            AppSvcDocDto svcDocDto=new AppSvcDocDto();
            svcDocDto.setSvcDocId(appSvcDocDto.getSvcDocId());
            svcDocDto.setDocName(appSvcDocDto.getDocName());
            svcDocDto.setDocSize(appSvcDocDto.getDocSize());
            svcDocDto.setFileRepoId(appSvcDocDto.getFileRepoId());
            //premiseVal May be ""
            svcDocDto.setMd5Code(appSvcDocDto.getMd5Code());
            appSvcDocDtos.add(svcDocDto);
        }
        Collections.sort(appSvcDocDtos, Comparator.comparing(AppSvcDocDto::getMd5Code));
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
        list.sort(Comparator.comparing(AppSvcPrincipalOfficersDto::getIdNo));
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
        list.sort((s1,s2)->(s1.getIdNo().compareTo(s2.getIdNo())));
        return list;
    }
    public static List<AppSvcPrincipalOfficersDto> copyAppSvcCgo(List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList) {
        List<AppSvcPrincipalOfficersDto> list = IaisCommonUtils.genNewArrayList();
        if (appSvcCgoDtoList == null || appSvcCgoDtoList.isEmpty()) {
            return list;
        }
        for (AppSvcPrincipalOfficersDto appSvcCgoDto : appSvcCgoDtoList) {
            AppSvcPrincipalOfficersDto cgoDto=new AppSvcPrincipalOfficersDto();
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
            cgoDto.setOtherQualification(appSvcCgoDto.getOtherQualification());
            cgoDto.setQualification(appSvcCgoDto.getQualification());
            cgoDto.setEmailAddr(appSvcCgoDto.getEmailAddr());
            list.add(cgoDto);
        }
        list.sort(Comparator.comparing(AppSvcPrincipalOfficersDto::getIdNo));
        return list;
    }

    public static List<AppSvcPrincipalOfficersDto> copyAppSvcKah(List<AppSvcPrincipalOfficersDto> appSvcKahDtoList) {
        List<AppSvcPrincipalOfficersDto> list = IaisCommonUtils.genNewArrayList();
        if (appSvcKahDtoList == null || appSvcKahDtoList.isEmpty()) {
            return list;
        }
        for (AppSvcPrincipalOfficersDto appSvcKahDto : appSvcKahDtoList) {
            AppSvcPrincipalOfficersDto kahDto = new AppSvcPrincipalOfficersDto();
            kahDto.setSalutation(appSvcKahDto.getSalutation());
            kahDto.setName(appSvcKahDto.getName());
            kahDto.setIdNo(appSvcKahDto.getIdNo());
            kahDto.setIdType(appSvcKahDto.getIdType());
            list.add(kahDto);
        }
        list.sort(Comparator.comparing(AppSvcPrincipalOfficersDto::getIdNo));
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
            AppSvcDisciplineAllocationDto o=new AppSvcDisciplineAllocationDto();
            o.setPremiseVal(appSvcDisciplineAllocationDto.getPremiseVal());
            o.setChkLstConfId(appSvcDisciplineAllocationDto.getChkLstConfId());
            o.setIdNo(appSvcDisciplineAllocationDto.getIdNo());
            list.add(o);
        }
        return list;
    };

    public static List<AppSvcVehicleDto> copyAppSvcVehicleDto(List<AppSvcVehicleDto> appSvcVehicleDtoList) {
        return copyAppSvcVehicleDto(appSvcVehicleDtoList, true);
    }

    public static List<AppSvcVehicleDto> copyAppSvcVehicleDto(List<AppSvcVehicleDto> appSvcVehicleDtoList, boolean toLower) {
        if (appSvcVehicleDtoList == null || appSvcVehicleDtoList.isEmpty()) {
            return new ArrayList<>(1);
        }
        List<AppSvcVehicleDto> list = new ArrayList<>(appSvcVehicleDtoList.size());
        appSvcVehicleDtoList.forEach((v) -> {
            AppSvcVehicleDto o = new AppSvcVehicleDto();
            o.setVehicleName(Optional.ofNullable(v.getVehicleName())
                    .map(f -> toLower ? f.toLowerCase(AppConsts.DFT_LOCALE) : f)
                    .orElseGet(() -> ""));
            o.setChassisNum(Optional.ofNullable(v.getChassisNum())
                    .map(f -> toLower ? f.toLowerCase(AppConsts.DFT_LOCALE) : f)
                    .orElseGet(() -> ""));
            o.setEngineNum(Optional.ofNullable(v.getEngineNum())
                    .map(f -> toLower ? f.toLowerCase(AppConsts.DFT_LOCALE) : f)
                    .orElseGet(() -> ""));
            o.setVehicleNum(Optional.ofNullable(v.getVehicleNum())
                    .map(f -> toLower ? f.toLowerCase(AppConsts.DFT_LOCALE) : f)
                    .orElseGet(() -> ""));
            list.add(o);
        });
        list.sort(Comparator.comparing(AppSvcVehicleDto::getVehicleName));
        return list;
    }

    public static AppSvcChargesPageDto copyAppSvcClinicalDirector(AppSvcChargesPageDto appSvcChargesPageDto){
        if(appSvcChargesPageDto==null){
            return new AppSvcChargesPageDto();
        }
        AppSvcChargesPageDto o=new AppSvcChargesPageDto();
        List<AppSvcChargesDto> generalChargesDtos = appSvcChargesPageDto.getGeneralChargesDtos();
        List<AppSvcChargesDto> otherChargesDtos = appSvcChargesPageDto.getOtherChargesDtos();
        if(generalChargesDtos==null||generalChargesDtos.isEmpty()){
            o.setGeneralChargesDtos(new ArrayList<>(1));
        }else {
            List<AppSvcChargesDto> list=new ArrayList<>(generalChargesDtos.size());
            generalChargesDtos.forEach((v)->{
                AppSvcChargesDto appSvcChargesDto=new AppSvcChargesDto();
                appSvcChargesDto.setChargesType(v.getChargesType());
                appSvcChargesDto.setMaxAmount(v.getMaxAmount());
                appSvcChargesDto.setMinAmount(v.getMinAmount());
                appSvcChargesDto.setRemarks(v.getRemarks());
                list.add(appSvcChargesDto);
            });
            o.setGeneralChargesDtos(list);
        }
        if(otherChargesDtos==null||otherChargesDtos.isEmpty()){
            o.setOtherChargesDtos(new ArrayList<>(1));
        }else {
            List<AppSvcChargesDto> list=new ArrayList<>(otherChargesDtos.size());
            otherChargesDtos.forEach((v)->{
                AppSvcChargesDto appSvcChargesDto=new AppSvcChargesDto();
                appSvcChargesDto.setChargesType(v.getChargesType());
                appSvcChargesDto.setRemarks(v.getRemarks());
                appSvcChargesDto.setMinAmount(v.getMinAmount());
                appSvcChargesDto.setMaxAmount(v.getMaxAmount());
                list.add(appSvcChargesDto);
            });
            o.setOtherChargesDtos(list);
        }
        return o;
    }

    public static List<AppSvcPrincipalOfficersDto> copyAppSvcClinicalDirector(List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos){

        if(appSvcClinicalDirectorDtos==null || appSvcClinicalDirectorDtos.isEmpty()){
            return new ArrayList<>(1);
        }
        List<AppSvcPrincipalOfficersDto> list=new ArrayList<>(appSvcClinicalDirectorDtos.size());
        appSvcClinicalDirectorDtos.forEach((v)->{
            AppSvcPrincipalOfficersDto o=new AppSvcPrincipalOfficersDto();
            o.setProfessionBoard(v.getProfessionBoard());
            o.setProfRegNo(v.getProfRegNo());
            o.setSalutation(v.getSalutation());
            o.setName(v.getName());
            o.setIdType(v.getIdType());
            o.setIdNo(v.getIdNo());
            o.setDesignation(v.getDesignation());
            o.setSpeciality(v.getSpeciality());
            o.setSpecialtyGetDate(v.getSpecialtyGetDate());
            o.setTypeOfCurrRegi(v.getTypeOfCurrRegi());
            o.setCurrRegiDate(v.getCurrRegiDate());
            o.setPraCerEndDate(v.getPraCerEndDate());
            o.setTypeOfRegister(v.getTypeOfRegister());
            o.setRelevantExperience(v.getRelevantExperience());
            o.setHoldCerByEMS(v.getHoldCerByEMS());
            o.setAclsExpiryDate(v.getAclsExpiryDate());
            o.setMobileNo(v.getMobileNo());
            o.setEmailAddr(v.getEmailAddr());
            list.add(o);
        });
        list.sort((s1,s2)->(s1.getIdNo().compareTo(s2.getIdNo())));
        return list;
    }

}
