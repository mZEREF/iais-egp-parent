package com.ecquaria.cloud.moh.iais.util;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremGroupOutsourcedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremNonLicRelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoAbortDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoMedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoNurseDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoTopDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoTopPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.OperationHoursReloadDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PageDataCopyUtil {

    public static AppGrpPremisesDto copyAppGrpPremisesDtoForAutoField(AppGrpPremisesDto appGrpPremisesDto) {
        AppGrpPremisesDto copy = new AppGrpPremisesDto();
        if (appGrpPremisesDto == null) {
            return copy;
        }
        copy.setPremisesType(appGrpPremisesDto.getPremisesType());
        copy.setScdfRefNo(appGrpPremisesDto.getScdfRefNo());
        copy.setCertIssuedDt(appGrpPremisesDto.getCertIssuedDt());
        if (StringUtil.isEmpty(appGrpPremisesDto.getCertIssuedDtStr())) {
            copy.setCertIssuedDtStr(null);
        } else {
            copy.setCertIssuedDtStr(appGrpPremisesDto.getCertIssuedDtStr());
        }
        //copy.setAddrType(appGrpPremisesDto.getAddrType());
        //copy.setBuildingName(appGrpPremisesDto.getBuildingName());
        copy.setLocateWtihHcsa(appGrpPremisesDto.getLocateWtihHcsa());
        return copy;
    }

    public static AppGrpPremisesDto copyInLocationFields(AppGrpPremisesDto appGrpPremisesDto) {
        AppGrpPremisesDto copy = new AppGrpPremisesDto();
        if (appGrpPremisesDto == null) {
            return copy;
        }
        copy.setPremisesType(appGrpPremisesDto.getPremisesType());
        copy.setVehicleNo(appGrpPremisesDto.getVehicleNo());
        copy.setPostalCode(appGrpPremisesDto.getPostalCode());
        copy.setAddrType(appGrpPremisesDto.getAddrType());
        copy.setBlkNo(appGrpPremisesDto.getBlkNo());
        copy.setStreetName(appGrpPremisesDto.getStreetName());
        copy.setBuildingName(appGrpPremisesDto.getBuildingName());

        copy.setEasMtsUseOnly(appGrpPremisesDto.getEasMtsUseOnly());
        copy.setEasMtsPubEmail(appGrpPremisesDto.getEasMtsPubEmail());
        copy.setEasMtsPubHotline(appGrpPremisesDto.getEasMtsPubHotline());
        return copy;
    }

    public static AppGrpPremisesDto copyCoLocationFields(AppGrpPremisesDto appGrpPremisesDto) {
        AppGrpPremisesDto copy = new AppGrpPremisesDto();
        if (appGrpPremisesDto == null) {
            return copy;
        }
        //copy.setLocateWtihHcsa(appGrpPremisesDto.getLocateWtihHcsa());
        copy.setLocateWtihNonHcsa(appGrpPremisesDto.getLocateWtihNonHcsa());

        List<AppPremNonLicRelationDto> appPremNonLicRelationDtos = appGrpPremisesDto.getAppPremNonLicRelationDtos();
        if (IaisCommonUtils.isNotEmpty(appPremNonLicRelationDtos)) {
            copy.setAppPremNonLicRelationDtos(appPremNonLicRelationDtos.stream()
                    .map(dto -> {
                        AppPremNonLicRelationDto relDto = new AppPremNonLicRelationDto();
                        relDto.setProvidedService(dto.getProvidedService());
                        relDto.setBusinessName(dto.getBusinessName());
                        return relDto;
                    })
                    .sorted(Comparator.comparing(AppPremNonLicRelationDto::getBusinessName)
                            .thenComparing(AppPremNonLicRelationDto::getProvidedService))
                    .collect(Collectors.toList()));
        }
        return copy;
    }

    public static List<OperationHoursReloadDto> copyOperationHoursReloadDto(List<OperationHoursReloadDto> list) {
        if (IaisCommonUtils.isEmpty(list)) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<OperationHoursReloadDto> operationHoursReloadDtoList = new ArrayList<>(list.size());
        for (OperationHoursReloadDto operationHoursReloadDto : list) {
            OperationHoursReloadDto o = new OperationHoursReloadDto();
            o.setSelectValList(operationHoursReloadDto.getSelectValList());
            o.setSelectAllDay(operationHoursReloadDto.isSelectAllDay());
            o.setStartFrom(operationHoursReloadDto.getStartFrom());
            o.setEndTo(operationHoursReloadDto.getEndTo());
            operationHoursReloadDtoList.add(o);
        }
        return operationHoursReloadDtoList;
    }

    public static List<AppPremEventPeriodDto> copyEvent(List<AppPremEventPeriodDto> list) {
        if (IaisCommonUtils.isEmpty(list)) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<AppPremEventPeriodDto> appPremEventPeriodDtoList = new ArrayList<>(list.size());
        for (AppPremEventPeriodDto appPremEventPeriodDto : list) {
            AppPremEventPeriodDto o = new AppPremEventPeriodDto();
            o.setEventName(appPremEventPeriodDto.getEventName());
            o.setEndDate(appPremEventPeriodDto.getEndDate());
            //o.setStartDate(o.getStartDate());
            appPremEventPeriodDtoList.add(o);
        }
        return appPremEventPeriodDtoList;
    }

    public static List<AppPremisesOperationalUnitDto> copyAppPremisesOperationalUnitDto(
            List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos) {
        if (IaisCommonUtils.isEmpty(appPremisesOperationalUnitDtos)) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<AppPremisesOperationalUnitDto> list = IaisCommonUtils.genNewArrayList();
        for (AppPremisesOperationalUnitDto appPremisesOperationalUnitDto : appPremisesOperationalUnitDtos) {
            AppPremisesOperationalUnitDto operationalUnitDto = new AppPremisesOperationalUnitDto();
            operationalUnitDto.setFloorNo(appPremisesOperationalUnitDto.getFloorNo());
            operationalUnitDto.setUnitNo(appPremisesOperationalUnitDto.getUnitNo());
            list.add(operationalUnitDto);
        }
        return list;
    }

    public static List<AppSvcPersonnelDto> copySvcPersonnels(List<AppSvcPersonnelDto> appSvcPersonnelDtoList) {
        if (appSvcPersonnelDtoList == null || appSvcPersonnelDtoList.isEmpty()) {
            return appSvcPersonnelDtoList;
        }
        return StreamSupport.stream(appSvcPersonnelDtoList.spliterator(),
                appSvcPersonnelDtoList.size() >= RfcConst.DFT_MIN_PARALLEL_SIZE)
                .map(svcPersonnelDto -> {
                    AppSvcPersonnelDto dto = new AppSvcPersonnelDto();
                    dto.setSalutation(StringUtil.getNonNull(svcPersonnelDto.getSalutation()));
                    dto.setPersonnelType(svcPersonnelDto.getPersonnelType());
                    dto.setIndexNo(svcPersonnelDto.getIndexNo());
                    if (StringUtil.isEmpty(dto.getPersonnelType())) {
                        dto.setPersonnelType(ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL);
                    }
                    dto.setName(svcPersonnelDto.getName());
                    return dto;
                })
                .sorted(Comparator.comparing(AppSvcPersonnelDto::getSalutation)
                        .thenComparing(AppSvcPersonnelDto::getName)
                        .thenComparing(AppSvcPersonnelDto::getPersonnelType))
                .collect(Collectors.toList());
    }


    public static List<AppSvcPersonnelDto> copySvcPersonnel(List<AppSvcPersonnelDto> appSvcPersonnelDtoList) {
        if (appSvcPersonnelDtoList == null || appSvcPersonnelDtoList.isEmpty()) {
            return appSvcPersonnelDtoList;
        }
        List<AppSvcPersonnelDto>  dtoList = IaisCommonUtils.genNewArrayList(appSvcPersonnelDtoList.size());
        for (AppSvcPersonnelDto dto : appSvcPersonnelDtoList) {
            AppSvcPersonnelDto personnelDto = new AppSvcPersonnelDto();
            personnelDto.setIndexNo(dto.getIndexNo());
            personnelDto.setName(dto.getName());
            personnelDto.setSalutation(dto.getSalutation());
            personnelDto.setPersonnelType(dto.getPersonnelType());
            dtoList.add(personnelDto);
        }
        return dtoList;
    }

    public static List<AppSvcPrincipalOfficersDto> copyAppSvcCd(List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList) {
        List<AppSvcPrincipalOfficersDto> list = IaisCommonUtils.genNewArrayList();
        if (appSvcCgoDtoList == null || appSvcCgoDtoList.isEmpty()) {
            return list;
        }
        for (AppSvcPrincipalOfficersDto appSvcCgoDto : appSvcCgoDtoList) {
            list.add(copyKeyPersonnelCd(appSvcCgoDto));
        }
        list.sort(Comparator.comparing(AppSvcPrincipalOfficersDto::getAssignSelect));
        return list;
    }


    public static List<AppSvcDocDto> copySvcDocs(List<AppSvcDocDto> appSvcDocDtoLit) {
        if (IaisCommonUtils.isEmpty(appSvcDocDtoLit)) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<AppSvcDocDto> appSvcDocDtos = new ArrayList<>(appSvcDocDtoLit.size());
        for (AppSvcDocDto appSvcDocDto : appSvcDocDtoLit) {
            if (StringUtil.isEmpty(appSvcDocDto.getMd5Code())) {
                continue;
            }
            AppSvcDocDto svcDocDto = new AppSvcDocDto();
            svcDocDto.setSvcDocId(appSvcDocDto.getSvcDocId());
            svcDocDto.setDocName(appSvcDocDto.getDocName());
            svcDocDto.setDocSize(appSvcDocDto.getDocSize());
            //svcDocDto.setFileRepoId(appSvcDocDto.getFileRepoId());
            svcDocDto.setSvcId(appSvcDocDto.getSvcId());
            //premiseVal May be ""
            svcDocDto.setMd5Code(appSvcDocDto.getMd5Code());
            appSvcDocDtos.add(svcDocDto);
        }
        appSvcDocDtos.sort(Comparator.comparing(AppSvcDocDto::getMd5Code));
        return appSvcDocDtos;
    }

    public static List<AppSvcPrincipalOfficersDto> copyMedaler(List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList) {
        if (IaisCommonUtils.isEmpty(appSvcMedAlertPersonList)) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<AppSvcPrincipalOfficersDto> list = IaisCommonUtils.genNewArrayList();
        for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcMedAlertPersonList) {
            list.add(copyKeyPersonnel(appSvcPrincipalOfficersDto));
        }
        list.sort(Comparator.comparing(AppSvcPrincipalOfficersDto::getAssignSelect));
        return list;
    }

    public static List<AppSvcPrincipalOfficersDto> copyAppSvcPo(List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList) {
        if (IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtoList)) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<AppSvcPrincipalOfficersDto> list = IaisCommonUtils.genNewArrayList();
        for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtoList) {
            list.add(copyKeyPersonnel(appSvcPrincipalOfficersDto));
        }
        list.sort(Comparator.comparing(AppSvcPrincipalOfficersDto::getAssignSelect));
        return list;
    }

    public static List<AppSvcPrincipalOfficersDto> copyAppSvcCgo(List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList) {
        List<AppSvcPrincipalOfficersDto> list = IaisCommonUtils.genNewArrayList();
        if (appSvcCgoDtoList == null || appSvcCgoDtoList.isEmpty()) {
            return list;
        }
        for (AppSvcPrincipalOfficersDto appSvcCgoDto : appSvcCgoDtoList) {
            list.add(copyKeyPersonnel(appSvcCgoDto));
        }
        list.sort(Comparator.comparing(AppSvcPrincipalOfficersDto::getAssignSelect));
        return list;
    }

    public static List<AppSvcPrincipalOfficersDto> copyAppSvcKah(List<AppSvcPrincipalOfficersDto> appSvcKahDtoList) {
        List<AppSvcPrincipalOfficersDto> list = IaisCommonUtils.genNewArrayList();
        if (appSvcKahDtoList == null || appSvcKahDtoList.isEmpty()) {
            return list;
        }
        for (AppSvcPrincipalOfficersDto appSvcKahDto : appSvcKahDtoList) {
            list.add(copyKeyPersonnel(appSvcKahDto));
        }
        list.sort(Comparator.comparing(AppSvcPrincipalOfficersDto::getAssignSelect));
        return list;
    }

    public static List<AppSvcChckListDto> copyAppSvcChckListDto(List<AppSvcChckListDto> appSvcChckListDtos) {
        List<AppSvcChckListDto> list = IaisCommonUtils.genNewArrayList();
        if (appSvcChckListDtos != null) {
            for (AppSvcChckListDto appSvcChckListDto : appSvcChckListDtos) {
                AppSvcChckListDto svcChckListDto = new AppSvcChckListDto();
                svcChckListDto.setChkLstConfId(appSvcChckListDto.getChkLstConfId());
                svcChckListDto.setChkName(appSvcChckListDto.getChkName());
                list.add(svcChckListDto);
            }
        }
        return list;
    }

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
                    .orElse(""));
            o.setChassisNum(Optional.ofNullable(v.getChassisNum())
                    .map(f -> toLower ? f.toLowerCase(AppConsts.DFT_LOCALE) : f)
                    .orElse(""));
            o.setEngineNum(Optional.ofNullable(v.getEngineNum())
                    .map(f -> toLower ? f.toLowerCase(AppConsts.DFT_LOCALE) : f)
                    .orElse(""));
            o.setVehicleNum(Optional.ofNullable(v.getVehicleNum())
                    .map(f -> toLower ? f.toLowerCase(AppConsts.DFT_LOCALE) : f)
                    .orElse(""));
            list.add(o);
        });
        list.sort(Comparator.comparing(AppSvcVehicleDto::getVehicleName));
        return list;
    }

    public static AppSvcChargesPageDto copyAppSvcClinicalDirector(AppSvcChargesPageDto appSvcChargesPageDto) {
        if (appSvcChargesPageDto == null) {
            return new AppSvcChargesPageDto();
        }
        AppSvcChargesPageDto o = new AppSvcChargesPageDto();
        List<AppSvcChargesDto> generalChargesDtos = appSvcChargesPageDto.getGeneralChargesDtos();
        List<AppSvcChargesDto> otherChargesDtos = appSvcChargesPageDto.getOtherChargesDtos();
        if (generalChargesDtos == null || generalChargesDtos.isEmpty()) {
            o.setGeneralChargesDtos(new ArrayList<>(1));
        } else {
            List<AppSvcChargesDto> list = new ArrayList<>(generalChargesDtos.size());
            generalChargesDtos.forEach((v) -> {
                AppSvcChargesDto appSvcChargesDto = new AppSvcChargesDto();
                appSvcChargesDto.setChargesType(v.getChargesType());
                appSvcChargesDto.setMaxAmount(v.getMaxAmount());
                appSvcChargesDto.setMinAmount(v.getMinAmount());
                appSvcChargesDto.setRemarks(v.getRemarks());
                list.add(appSvcChargesDto);
            });
            o.setGeneralChargesDtos(list);
        }
        if (otherChargesDtos == null || otherChargesDtos.isEmpty()) {
            o.setOtherChargesDtos(new ArrayList<>(1));
        } else {
            List<AppSvcChargesDto> list = new ArrayList<>(otherChargesDtos.size());
            otherChargesDtos.forEach((v) -> {
                AppSvcChargesDto appSvcChargesDto = new AppSvcChargesDto();
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

    public static List<AppSvcOtherInfoDto> copyAppSvcOtherInfoList(List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList){
        if (IaisCommonUtils.isEmpty(appSvcOtherInfoDtoList)){
            return appSvcOtherInfoDtoList;
        }
        List<AppSvcOtherInfoDto> appSvcOtherInfoDtos = IaisCommonUtils.genNewArrayList();
        for (AppSvcOtherInfoDto appSvcOtherInfoDto : appSvcOtherInfoDtoList) {
            AppSvcOtherInfoDto svcOtherInfoDto = new AppSvcOtherInfoDto();
            //med
            AppSvcOtherInfoMedDto dsMedDto = copyAppSvcOtherInfoMed(appSvcOtherInfoDto.getAppSvcOtherInfoMedDto());
            AppSvcOtherInfoMedDto rscMedDto = copyAppSvcOtherInfoMed(appSvcOtherInfoDto.getOtherInfoMedAmbulatorySurgicalCentre());
            //nurse
            AppSvcOtherInfoNurseDto appSvcOtherInfoNurseDto = copyAppSvcOtherInfoNurse(appSvcOtherInfoDto.getAppSvcOtherInfoNurseDto());
            svcOtherInfoDto.setAppSvcOtherInfoNurseDto(appSvcOtherInfoNurseDto);
            svcOtherInfoDto.setAppSvcOtherInfoMedDto(dsMedDto);
            svcOtherInfoDto.setOtherInfoMedAmbulatorySurgicalCentre(rscMedDto);
            svcOtherInfoDto.setAscsDeclaration(appSvcOtherInfoDto.getAscsDeclaration());
            svcOtherInfoDto.setDsDeclaration(appSvcOtherInfoDto.getDsDeclaration());
            svcOtherInfoDto.setPremisesVal(appSvcOtherInfoDto.getPremisesVal());
            svcOtherInfoDto.setInit(appSvcOtherInfoDto.isInit());
            appSvcOtherInfoDtos.add(svcOtherInfoDto);
        }
        return appSvcOtherInfoDtos;
    }


    public static List<AppSvcOtherInfoDto> copyAppSvcOtherInfoPersonList(List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList){
        if (IaisCommonUtils.isEmpty(appSvcOtherInfoDtoList)){
            return IaisCommonUtils.genNewArrayList();
        }
        List<AppSvcOtherInfoDto> appSvcOtherInfoDtos = IaisCommonUtils.genNewArrayList();
        for (AppSvcOtherInfoDto appSvcOtherInfoDto : appSvcOtherInfoDtoList) {
            AppSvcOtherInfoDto svcOtherInfoDto = new AppSvcOtherInfoDto();
            svcOtherInfoDto.setProvideTop(appSvcOtherInfoDto.getProvideTop());
            svcOtherInfoDto.setInit(appSvcOtherInfoDto.isInit());
            svcOtherInfoDto.setProvideYfVs(appSvcOtherInfoDto.getProvideYfVs());
            svcOtherInfoDto.setDeclaration(appSvcOtherInfoDto.getDeclaration());
            svcOtherInfoDto.setYfCommencementDateStr(appSvcOtherInfoDto.getYfCommencementDateStr());
            svcOtherInfoDto.setYfCommencementDate(appSvcOtherInfoDto.getYfCommencementDate());
            svcOtherInfoDto.setOrgUserDto(appSvcOtherInfoDto.getOrgUserDto());
            appSvcOtherInfoDtos.add(svcOtherInfoDto);
        }
        return appSvcOtherInfoDtos;
    }


    private static AppSvcOtherInfoMedDto copyAppSvcOtherInfoMed(AppSvcOtherInfoMedDto appSvcOtherInfoMedDto){
        if (appSvcOtherInfoMedDto == null){
            return new AppSvcOtherInfoMedDto();
        }
        AppSvcOtherInfoMedDto svcOtherInfoMedDto = new AppSvcOtherInfoMedDto();
        svcOtherInfoMedDto.setId(appSvcOtherInfoMedDto.getId());
        svcOtherInfoMedDto.setAppPremId(appSvcOtherInfoMedDto.getAppPremId());
        svcOtherInfoMedDto.setSystemOption(appSvcOtherInfoMedDto.getSystemOption());
        svcOtherInfoMedDto.setIsMedicalTypeIt(appSvcOtherInfoMedDto.getIsMedicalTypeIt());
        svcOtherInfoMedDto.setIsMedicalTypePaper(appSvcOtherInfoMedDto.getIsMedicalTypePaper());
        svcOtherInfoMedDto.setIsOpenToPublic(appSvcOtherInfoMedDto.getIsOpenToPublic());
        svcOtherInfoMedDto.setOtherSystemOption(appSvcOtherInfoMedDto.getOtherSystemOption());

        return svcOtherInfoMedDto;
    }

    private static AppSvcOtherInfoNurseDto copyAppSvcOtherInfoNurse(AppSvcOtherInfoNurseDto appSvcOtherInfoNurseDto){
        if (appSvcOtherInfoNurseDto == null){
            return new AppSvcOtherInfoNurseDto();
        }
        AppSvcOtherInfoNurseDto svcOtherInfoNurseDto = new AppSvcOtherInfoNurseDto();
        svcOtherInfoNurseDto.setDialysisStationsNum(appSvcOtherInfoNurseDto.getDialysisStationsNum());
        svcOtherInfoNurseDto.setPerShiftNum(appSvcOtherInfoNurseDto.getPerShiftNum());
        svcOtherInfoNurseDto.setAppPremId(appSvcOtherInfoNurseDto.getAppPremId());
        svcOtherInfoNurseDto.setHelpBStationNum(appSvcOtherInfoNurseDto.getHelpBStationNum());
        svcOtherInfoNurseDto.setIsOpenToPublic(appSvcOtherInfoNurseDto.getIsOpenToPublic());
        return svcOtherInfoNurseDto;
    }


    public static List<AppSvcOtherInfoTopPersonDto> copyAppSvcOtherInfoPerson(List<AppSvcOtherInfoTopPersonDto> appSvcOtherInfoTopPersonDtoList, List<String> autoList) {
        if (IaisCommonUtils.isEmpty(appSvcOtherInfoTopPersonDtoList)) {
            return IaisCommonUtils.genNewArrayList();
        }
        appSvcOtherInfoTopPersonDtoList.stream()
                .forEach((item) -> autoList.add(item.getPsnType()));

        List<AppSvcOtherInfoTopPersonDto> appSvcOtherInfoTopPersonDtos = appSvcOtherInfoTopPersonDtoList.stream()
                .map(dto -> MiscUtil.transferEntityDto(dto, AppSvcOtherInfoTopPersonDto.class))
                .collect(Collectors.toList());
        return appSvcOtherInfoTopPersonDtos;
    }

    public static AppSvcOtherInfoTopDto copyAppSvcOtherInfoTopDto(AppSvcOtherInfoTopDto appSvcOtherInfoTopDto){
        if (appSvcOtherInfoTopDto == null){
            return new AppSvcOtherInfoTopDto();
        }
        AppSvcOtherInfoTopDto svcOtherInfoTopDto = new AppSvcOtherInfoTopDto();
        svcOtherInfoTopDto.setId(appSvcOtherInfoTopDto.getId());
        svcOtherInfoTopDto.setTopType(appSvcOtherInfoTopDto.getTopType());
        svcOtherInfoTopDto.setAppPremId(appSvcOtherInfoTopDto.getAppPremId());
        svcOtherInfoTopDto.setIsProvideHpb(appSvcOtherInfoTopDto.getIsProvideHpb());
        svcOtherInfoTopDto.setHasConsuAttendCourse(appSvcOtherInfoTopDto.getHasConsuAttendCourse());
        svcOtherInfoTopDto.setCompCaseNum(appSvcOtherInfoTopDto.getCompCaseNum());
        svcOtherInfoTopDto.setIsOutcomeProcRecord(appSvcOtherInfoTopDto.getIsOutcomeProcRecord());
        return svcOtherInfoTopDto;
    }
    public static List<AppSvcOtherInfoAbortDto> copyAppSvcOtherInfoAbortDto(List<AppSvcOtherInfoAbortDto> abortDtoList){
        if (IaisCommonUtils.isEmpty(abortDtoList)){
            return IaisCommonUtils.genNewArrayList();
        }
        List<AppSvcOtherInfoAbortDto> appSvcOtherInfoAbortDtoList = abortDtoList.stream()
                .map(dto -> MiscUtil.transferEntityDto(dto, AppSvcOtherInfoAbortDto.class))
                .collect(Collectors.toList());
        return appSvcOtherInfoAbortDtoList;
    }

    public static List<AppPremGroupOutsourcedDto> copyAppPremGroupSoutsourcedList(List<AppPremGroupOutsourcedDto> appPremGroupOutsourcedDtoList){
        if (IaisCommonUtils.isEmpty(appPremGroupOutsourcedDtoList)){
            return IaisCommonUtils.genNewArrayList();
        }
        List<AppPremGroupOutsourcedDto> premGroupOutsourcedDtoList = IaisCommonUtils.genNewArrayList();
        appPremGroupOutsourcedDtoList.forEach((item) -> {
                    AppPremGroupOutsourcedDto appPremGroupOutsourcedDto = new AppPremGroupOutsourcedDto();
                    appPremGroupOutsourcedDto.setBusinessName(item.getBusinessName());
                    appPremGroupOutsourcedDto.setAddress(item.getAddress());
                    appPremGroupOutsourcedDto.setExpiryDate(item.getExpiryDate());
                    appPremGroupOutsourcedDto.setAppPremOutSourceLicenceDto(item.getAppPremOutSourceLicenceDto());
                    appPremGroupOutsourcedDto.setStartDateStr(item.getStartDateStr());
                    appPremGroupOutsourcedDto.setEndDateStr(item.getEndDateStr());
                    premGroupOutsourcedDtoList.add(appPremGroupOutsourcedDto);
                });
        return premGroupOutsourcedDtoList;
    }



    public static List<AppSvcPrincipalOfficersDto> copyAppSvcClinicalDirector(
            List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos) {
        if (appSvcClinicalDirectorDtos == null || appSvcClinicalDirectorDtos.isEmpty()) {
            return new ArrayList<>(1);
        }
        List<AppSvcPrincipalOfficersDto> list = new ArrayList<>(appSvcClinicalDirectorDtos.size());
        appSvcClinicalDirectorDtos.forEach((v) -> list.add(copyKeyPersonnel(v)));
        list.sort(Comparator.comparing(AppSvcPrincipalOfficersDto::getAssignSelect));
        return list;
    }
    public static AppSvcPrincipalOfficersDto copyKeyPersonnelCd(AppSvcPrincipalOfficersDto person) {
        return copyKeyPersonnel(person, 1);
    }
    public static AppSvcPrincipalOfficersDto copyKeyPersonnel(AppSvcPrincipalOfficersDto person) {
        return copyKeyPersonnel(person, 0);
    }
    /**
     * Copy Key Personnel
     *
     * @param personList target person
     * @param status 0: all fields; 1: key fields; 2: normal fields for affected person
     * @return new person dto
     */
    public static List<AppSvcPrincipalOfficersDto> copyKeyPersonnel(List<AppSvcPrincipalOfficersDto> personList, int status) {
        if (IaisCommonUtils.isEmpty(personList)) {
            return personList;
        }
        return personList.stream()
                .map(dto -> copyKeyPersonnel(dto, status))
                .sorted(Comparator.comparing(AppSvcPrincipalOfficersDto::getAssignSelect))
                .collect(Collectors.toList());
    }

    /**
     * Copy Key Personnel
     *
     * @param person target person
     * @param status 0: all fields; 1: key fields; 2: normal fields for affected person
     * @return new person dto
     */
    public static AppSvcPrincipalOfficersDto copyKeyPersonnel(AppSvcPrincipalOfficersDto person, int status) {
        if (person == null) {
            return null;
        }
        AppSvcPrincipalOfficersDto newPerson = new AppSvcPrincipalOfficersDto();
        if (2 != status) {
            newPerson.setIdNo(person.getIdNo());
            newPerson.setIdType(person.getIdType());
            newPerson.setNationality(StringUtil.getNonNull(person.getNationality()));
        }
        if (1 != status) {
            newPerson.setSalutation(person.getSalutation());
            newPerson.setName(person.getName());
            newPerson.setDesignation(person.getDesignation());
            newPerson.setOtherDesignation(StringUtil.getNonNull(person.getOtherDesignation()));
            newPerson.setMobileNo(person.getMobileNo());
            newPerson.setOfficeTelNo(StringUtil.getNonNull(person.getOfficeTelNo()));
            newPerson.setEmailAddr(person.getEmailAddr());
            newPerson.setProfessionType(person.getProfessionType());
            newPerson.setProfessionBoard(person.getProfessionBoard());
            newPerson.setProfRegNo(person.getProfRegNo());
            newPerson.setSpeciality(person.getSpeciality());
            newPerson.setSpecialtyGetDate(person.getSpecialtyGetDate());
            newPerson.setSubSpeciality(person.getSubSpeciality());
            newPerson.setQualification(person.getQualification());
            newPerson.setOtherQualification(person.getOtherQualification());
            newPerson.setTypeOfCurrRegi(person.getTypeOfCurrRegi());
            newPerson.setCurrRegiDate(person.getCurrRegiDate());
            newPerson.setPraCerEndDate(person.getPraCerEndDate());
            newPerson.setTypeOfRegister(person.getTypeOfRegister());
            newPerson.setRelevantExperience(person.getRelevantExperience());
            newPerson.setHoldCerByEMS(person.getHoldCerByEMS());
            newPerson.setAclsExpiryDate(person.getAclsExpiryDate());
            newPerson.setBclsExpiryDate(person.getBclsExpiryDate());
        }
        newPerson.setAssignSelect(ApplicationHelper.getPersonKey(newPerson));
        return newPerson;
    }

    public static <T extends Serializable> List<T> copyMutableObjectList(List<T> sourceList) {
        if (IaisCommonUtils.isEmpty(sourceList)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return sourceList.stream()
                .map(CopyUtil::copyMutableObject)
                .collect(Collectors.toList());
    }

}
