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
        //copy.setLocateWtihHcsa(appGrpPremisesDto.getLocateWtihHcsa());
        copy.setLocateWtihNonHcsa(appGrpPremisesDto.getLocateWtihNonHcsa());
        copy.setAppPremNonLicRelationDtos(MiscUtil.transferEntityDtos(appGrpPremisesDto.getAppPremNonLicRelationDtos(),
                AppPremNonLicRelationDto.class));
        return copy;
    }

    public static List<OperationHoursReloadDto> copyOperationHoursReloadDto(List<OperationHoursReloadDto> list) {
        if (list == null) {
            return new ArrayList<>();
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
        if (list == null) {
            return new ArrayList<>();
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
                    if (StringUtil.isEmpty(dto.getPersonnelType())) {
                        dto.setPersonnelType(ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL);
                    }
                    dto.setName(svcPersonnelDto.getName());
                    dto.setDesignation(svcPersonnelDto.getDesignation());
                    dto.setOtherDesignation(svcPersonnelDto.getOtherDesignation());
                    dto.setProfRegNo(svcPersonnelDto.getProfRegNo());
                    dto.setWrkExpYear(StringUtil.getNonNull(svcPersonnelDto.getWrkExpYear()));
                    dto.setQualification(StringUtil.getNonNull(svcPersonnelDto.getQualification()));
                    return dto;
                })
                .sorted(Comparator.comparing(AppSvcPersonnelDto::getSalutation)
                        .thenComparing(AppSvcPersonnelDto::getName)
                        .thenComparing(AppSvcPersonnelDto::getPersonnelType)
                        .thenComparing(AppSvcPersonnelDto::getQualification)
                        .thenComparing(AppSvcPersonnelDto::getWrkExpYear))
                .collect(Collectors.toList());
    }

    public static List<AppSvcDocDto> copySvcDocs(List<AppSvcDocDto> appSvcDocDtoLit) {
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
        List<AppSvcPrincipalOfficersDto> list = IaisCommonUtils.genNewArrayList();
        for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcMedAlertPersonList) {
            list.add(copyKeyPersonnel(appSvcPrincipalOfficersDto));
        }
        list.sort(Comparator.comparing(AppSvcPrincipalOfficersDto::getAssignSelect));
        return list;
    }

    public static List<AppSvcPrincipalOfficersDto> copyAppSvcPo(List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList) {
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
     * @param person target person
     * @param status 0: all fields; 1: key fields; 2: special fields for affected person
     * @return new person dto
     */
    public static AppSvcPrincipalOfficersDto copyKeyPersonnel(AppSvcPrincipalOfficersDto person, int status) {
        if (person == null) {
            return null;
        }
        String psnType = person.getPsnType();
        AppSvcPrincipalOfficersDto newPerson = new AppSvcPrincipalOfficersDto();
        newPerson.setSalutation(person.getSalutation());
        newPerson.setName(person.getName());
        newPerson.setIdNo(person.getIdNo());
        newPerson.setIdType(person.getIdType());
        newPerson.setNationality(StringUtil.getNonNull(person.getNationality()));
        if (!ApplicationConsts.PERSONNEL_PSN_KAH.equals(psnType)) {
            newPerson.setDesignation(person.getDesignation());
            newPerson.setOtherDesignation(StringUtil.getNonNull(person.getOtherDesignation()));
            newPerson.setMobileNo(person.getMobileNo());
            newPerson.setOfficeTelNo(StringUtil.getNonNull(person.getOfficeTelNo()));
            newPerson.setEmailAddr(person.getEmailAddr());
        }

        if (status == 0) {
            if (ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR.equals(psnType)) {
                newPerson.setProfessionBoard(person.getProfessionBoard());
                newPerson.setProfRegNo(person.getProfRegNo());
                newPerson.setSpeciality(person.getSpeciality());
                newPerson.setSpecialtyGetDate(person.getSpecialtyGetDate());
                newPerson.setTypeOfCurrRegi(person.getTypeOfCurrRegi());
                newPerson.setCurrRegiDate(person.getCurrRegiDate());
                newPerson.setPraCerEndDate(person.getPraCerEndDate());
                newPerson.setTypeOfRegister(person.getTypeOfRegister());
                newPerson.setRelevantExperience(person.getRelevantExperience());
                newPerson.setHoldCerByEMS(person.getHoldCerByEMS());
                newPerson.setAclsExpiryDate(person.getAclsExpiryDate());
            } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)) {
                newPerson.setProfessionType(person.getProfessionType());
                newPerson.setProfRegNo(person.getProfRegNo());
                newPerson.setSpeciality(person.getSpeciality());
                newPerson.setSubSpeciality(person.getSubSpeciality());
                newPerson.setQualification(person.getQualification());
                newPerson.setOtherQualification(person.getOtherQualification());
            }
        }
        newPerson.setAssignSelect(ApplicationHelper.getPersonKey(newPerson));
        return newPerson;
    }

    public static <T extends Serializable> List<T> copyMutableObjectList(List<T> sourceList) {
        if (IaisCommonUtils.isEmpty(sourceList)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return sourceList.stream()
                .map(t -> CopyUtil.copyMutableObject(t))
                .collect(Collectors.toList());
    }

}
