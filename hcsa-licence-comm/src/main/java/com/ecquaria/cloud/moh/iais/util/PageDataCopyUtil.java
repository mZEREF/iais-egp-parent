package com.ecquaria.cloud.moh.iais.util;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremNonLicRelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
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

    public static List<AppSvcPersonnelDto> copySectionLeaderDetail(List<AppSvcPersonnelDto> appSvcPersonnelDtoList) {
        if (appSvcPersonnelDtoList == null || appSvcPersonnelDtoList.isEmpty()) {
            return appSvcPersonnelDtoList;
        }
        List<AppSvcPersonnelDto>  dtoList = IaisCommonUtils.genNewArrayList(appSvcPersonnelDtoList.size());
        for (AppSvcPersonnelDto dto : appSvcPersonnelDtoList) {
            AppSvcPersonnelDto personnelDto = dto;
            personnelDto.setName(null);
            personnelDto.setSalutation(null);
            personnelDto.setIndexNo(null);
            dtoList.add(personnelDto);
        }
        return dtoList;
    }

    public static List<AppSvcPersonnelDto> copySectionLeader(List<AppSvcPersonnelDto> appSvcPersonnelDtoList) {
        if (appSvcPersonnelDtoList == null || appSvcPersonnelDtoList.isEmpty()) {
            return appSvcPersonnelDtoList;
        }
        List<AppSvcPersonnelDto>  dtoList = IaisCommonUtils.genNewArrayList(appSvcPersonnelDtoList.size());
        for (AppSvcPersonnelDto dto : appSvcPersonnelDtoList) {
            AppSvcPersonnelDto personnelDto = new AppSvcPersonnelDto();
            personnelDto.setName(dto.getName());
            personnelDto.setSalutation(dto.getSalutation());
            personnelDto.setIndexNo(dto.getIndexNo());
            dtoList.add(personnelDto);
        }
        return dtoList;
    }


    public static List<AppSvcPersonnelDto> copySvcArPersonnel(List<AppSvcPersonnelDto> appSvcPersonnelDtoList) {
        if (appSvcPersonnelDtoList == null || appSvcPersonnelDtoList.isEmpty()) {
            return appSvcPersonnelDtoList;
        }
        List<AppSvcPersonnelDto>  dtoList = IaisCommonUtils.genNewArrayList(appSvcPersonnelDtoList.size());
        for (AppSvcPersonnelDto dto : appSvcPersonnelDtoList) {
            AppSvcPersonnelDto personnelDto = new AppSvcPersonnelDto();
            personnelDto.setName(dto.getName());
            personnelDto.setSalutation(dto.getSalutation());
            personnelDto.setIndexNo(dto.getIndexNo());
            personnelDto.setPersonnelType(dto.getPersonnelType());
            dtoList.add(personnelDto);
        }
        return dtoList;
    }

    public static List<AppSvcPersonnelDto> copySvcDetailPersonnel(List<AppSvcPersonnelDto> appSvcPersonnelDtoList) {
        if (appSvcPersonnelDtoList == null || appSvcPersonnelDtoList.isEmpty()) {
            return appSvcPersonnelDtoList;
        }
        List<AppSvcPersonnelDto>  dtoList = IaisCommonUtils.genNewArrayList(appSvcPersonnelDtoList.size());
        for (AppSvcPersonnelDto dto : appSvcPersonnelDtoList) {
            AppSvcPersonnelDto personnelDto = dto;
            personnelDto.setName(null);
            personnelDto.setSalutation(null);
            personnelDto.setIndexNo(null);
            personnelDto.setPersonnelType(null);
            dtoList.add(personnelDto);
        }
        return dtoList;
    }

    public static AppSvcPrincipalOfficersDto copyAppSvcPrincipalOfficersDto(AppSvcPrincipalOfficersDto dto) {
        if (StringUtil.isEmpty(dto)){
            return dto;
        }
        AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
        appSvcPrincipalOfficersDto.setSalutation(dto.getSalutation());
        appSvcPrincipalOfficersDto.setName(dto.getName());
        appSvcPrincipalOfficersDto.setIdNo(dto.getIdNo());
        appSvcPrincipalOfficersDto.setIndexNo(dto.getIndexNo());
        return appSvcPrincipalOfficersDto;
    }

    public static List<AppSvcPrincipalOfficersDto> copyAppSvcPrincipalOfficersDtoDetail(List<AppSvcPrincipalOfficersDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return dtoList;
        }
        List<AppSvcPrincipalOfficersDto> officersDtoList = IaisCommonUtils.genNewArrayList(dtoList.size());
        for (AppSvcPrincipalOfficersDto dto : officersDtoList) {
            AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = dto;
            appSvcPrincipalOfficersDto.setSalutation(null);
            appSvcPrincipalOfficersDto.setName(null);
            appSvcPrincipalOfficersDto.setIdNo(null);
            appSvcPrincipalOfficersDto.setIndexNo(null);
            officersDtoList.add(appSvcPrincipalOfficersDto);
        }
        return officersDtoList;
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
            svcDocDto.setFileRepoId(appSvcDocDto.getFileRepoId());
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
