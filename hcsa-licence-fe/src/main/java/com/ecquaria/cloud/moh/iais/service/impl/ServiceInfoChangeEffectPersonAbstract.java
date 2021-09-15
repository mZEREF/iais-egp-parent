package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.action.RequestForChangeMenuDelegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.rfcutil.PageDataCopyUtil;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceInfoChangeEffectPerson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Wenkang
 * @date 2021/4/27 15:48
 */
@Slf4j
@Component
public class ServiceInfoChangeEffectPersonAbstract implements ServiceInfoChangeEffectPerson {
    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private RequestForChangeService requestForChangeService;
    @Override
    public List<AppSubmissionDto> personContact(String licenseeId, AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto) throws Exception {
        List<AppSubmissionDto> appSubmissionDtoList = IaisCommonUtils.genNewArrayList();
        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        if (appSvcRelatedInfoDto == null || oldAppSvcRelatedInfoDto == null) {
            return appSubmissionDtoList;
        }
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtoList = appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();

        List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList = oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList = oldAppSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtoList = oldAppSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcClinicalDirectorDtoList = oldAppSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();

        List<AppSvcPrincipalOfficersDto> kahList = appSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList();
        List<AppSvcPrincipalOfficersDto> oldKahList = oldAppSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList();

        Set<String> set = IaisCommonUtils.genNewHashSet();
        List<String> list = IaisCommonUtils.genNewArrayList();
        List<String> list1 = changeCgo(appSvcCgoDtoList, oldAppSvcCgoDtoList);
        List<String> list2 = changeMeadrter(appSvcMedAlertPersonList, oldAppSvcMedAlertPersonList);
        List<String> list3 = changePo(appSvcPrincipalOfficersDtoList, oldAppSvcPrincipalOfficersDtoList);
        List<String> list4 = changeClinicalDirector(appSvcClinicalDirectorDtoList, oldAppSvcClinicalDirectorDtoList);
        List<String> list5 = changeKeyAppointmentHolder(kahList, oldKahList);

        set.addAll(list1);
        set.addAll(list2);
        set.addAll(list3);
        set.addAll(list4);
        set.addAll(list5);
        list.addAll(set);
        List<LicKeyPersonnelDto> licKeyPersonnelDtos = IaisCommonUtils.genNewArrayList();
        for (String string : list) {
            List<String> personnelDtoByIdNo = requestForChangeService.getPersonnelIdsByIdNo(string);
            List<LicKeyPersonnelDto> licKeyPersonnelDtoByPerId = requestForChangeService.getLicKeyPersonnelDtoByPerId(personnelDtoByIdNo);
            licKeyPersonnelDtos.addAll(licKeyPersonnelDtoByPerId);
        }

        Set<String> licenceId = IaisCommonUtils.genNewHashSet();
        List<String> licenceIdList = IaisCommonUtils.genNewArrayList();

        for (LicKeyPersonnelDto licKeyPersonnelDto : licKeyPersonnelDtos) {
            if (licenseeId.equals(licKeyPersonnelDto.getLicenseeId())) {
                licenceId.add(licKeyPersonnelDto.getLicenceId());
            }
        }
        licenceIdList.addAll(licenceId);
        licenceIdList.remove(appSubmissionDto.getLicenceId());
        for (String string : licenceIdList) {
            AppSubmissionDto appSubmissionDtoByLicenceId = requestForChangeService.getAppSubmissionDtoByLicenceId(string);
            if (appSubmissionDtoByLicenceId == null || appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList() == null) {
                continue;
            }
            log.info(StringUtil.changeForLog("The affected licence: " + appSubmissionDtoByLicenceId.getLicenceNo()));
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            appEditSelectDto.setServiceEdit(true);
            List<String> personnelEditList = IaisCommonUtils.genNewArrayList();
            AppSvcRelatedInfoDto appSvcRelatedInfoDto2 = appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0);
            if (!list1.isEmpty()) {
                reSetPersonnels(appSvcRelatedInfoDto, appSvcRelatedInfoDto2,
                        ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, personnelEditList);
            }
            if (!list2.isEmpty()) {
                reSetPersonnels(appSvcRelatedInfoDto, appSvcRelatedInfoDto2,
                        ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, personnelEditList);
            }
            if (!list3.isEmpty()) {
                reSetPersonnels(appSvcRelatedInfoDto, appSvcRelatedInfoDto2,
                        ApplicationConsts.PERSONNEL_PSN_TYPE_PO, personnelEditList);
            }
            if (!list4.isEmpty()) {
                reSetPersonnels(appSvcRelatedInfoDto, appSvcRelatedInfoDto2,
                        ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, personnelEditList);
            }
            // KAH
            if (!list5.isEmpty()) {
                reSetPersonnels(appSvcRelatedInfoDto, appSvcRelatedInfoDto2,
                        ApplicationConsts.PERSONNEL_PSN_KAH, personnelEditList);
            }
            appEditSelectDto.setPersonnelEditList(personnelEditList);
            appSubmissionDtoByLicenceId.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDtoByLicenceId.setChangeSelectDto(appEditSelectDto);
            appSubmissionDtoByLicenceId.setPartPremise(false);
            appSubmissionDtoByLicenceId.setGetAppInfoFromDto(true);
            RequestForChangeMenuDelegator.oldPremiseToNewPremise(appSubmissionDtoByLicenceId);
            appSubmissionDtoByLicenceId.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
            appSubmissionService.transform(appSubmissionDtoByLicenceId, appSubmissionDto.getLicenseeId());
            requestForChangeService.premisesDocToSvcDoc(appSubmissionDtoByLicenceId);
            appSubmissionDtoByLicenceId.setAutoRfc(true);
            appSubmissionDtoByLicenceId.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);
            appSubmissionDtoByLicenceId.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
            NewApplicationHelper.reSetAdditionalFields(appSubmissionDtoByLicenceId, appEditSelectDto);
            appSubmissionDtoList.add(appSubmissionDtoByLicenceId);
        }

        return appSubmissionDtoList;
    }

    private void reSetPersonnels(AppSvcRelatedInfoDto sourceReletedInfo, AppSvcRelatedInfoDto targetReletedInfo, String psnType,
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

    private void reSetPersonnels(List<AppSvcPrincipalOfficersDto> sourceList, List<AppSvcPrincipalOfficersDto> targetList,
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
                        NewApplicationHelper.syncPsnDto(source, target);
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

    private List<String> changeKeyAppointmentHolder(List<AppSvcPrincipalOfficersDto> kahList,
            List<AppSvcPrincipalOfficersDto> oldKahList) {
        List<String> ids = new ArrayList<>(10);
        if (kahList != null && oldKahList != null) {
            if (kahList.equals(oldKahList)) {
                return ids;
            }
            kahList.forEach(kah -> {
                if (oldKahList.stream().noneMatch(dto -> Objects.equals(dto.getIdNo(), kah.getIdNo())
                        && Objects.equals(dto.getIdType(), kah.getIdType())
                        && Objects.equals(dto.getName(), kah.getName())
                        && Objects.equals(dto.getSalutation(), kah.getSalutation()))) {
                    ids.add(kah.getIdNo());
                }
            });
        }
        return ids;
    }

    protected List<String> changePo(List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList, List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtoList) {
        List<String> ids=IaisCommonUtils.genNewArrayList();
        if (appSvcPrincipalOfficersDtoList != null && oldAppSvcPrincipalOfficersDtoList != null) {
            List<AppSvcPrincipalOfficersDto> n = PageDataCopyUtil.copyAppSvcPo(appSvcPrincipalOfficersDtoList);
            List<AppSvcPrincipalOfficersDto> o = PageDataCopyUtil.copyAppSvcPo(oldAppSvcPrincipalOfficersDtoList);
            if(n.equals(o)){
                return ids;
            }
            for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : n){
                for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto1 : o){
                    if(appSvcPrincipalOfficersDto.getIdNo().equals(appSvcPrincipalOfficersDto1.getIdNo())){
                        boolean b = appSvcPrincipalOfficersDto.getName().equals(appSvcPrincipalOfficersDto1.getName())
                                &&appSvcPrincipalOfficersDto.getSalutation().equals(appSvcPrincipalOfficersDto1.getSalutation())
                                &&appSvcPrincipalOfficersDto.getDesignation().equals(appSvcPrincipalOfficersDto1.getDesignation())
                                &&appSvcPrincipalOfficersDto.getOfficeTelNo().equals(appSvcPrincipalOfficersDto1.getOfficeTelNo())
                                &&appSvcPrincipalOfficersDto.getMobileNo().equals(appSvcPrincipalOfficersDto1.getMobileNo())
                                &&appSvcPrincipalOfficersDto.getEmailAddr().equals(appSvcPrincipalOfficersDto1.getEmailAddr());
                        if(!b){
                            ids.add(appSvcPrincipalOfficersDto.getIdNo());
                        }
                    }
                }
            }
        }
        return ids;
    }
    protected List<String> changeMeadrter(List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList, List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList1) {
        List<String> ids=IaisCommonUtils.genNewArrayList();
        if (appSvcMedAlertPersonList != null && oldAppSvcMedAlertPersonList1 != null) {
            List<AppSvcPrincipalOfficersDto> n = PageDataCopyUtil.copyMedaler(appSvcMedAlertPersonList);
            List<AppSvcPrincipalOfficersDto> o = PageDataCopyUtil.copyMedaler(oldAppSvcMedAlertPersonList1);
            if(n.equals(o)){
                return ids;
            }
            for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : n){
                for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto1 : o){
                    if(appSvcPrincipalOfficersDto.getIdNo().equals(appSvcPrincipalOfficersDto1.getIdNo())){
                        boolean b=appSvcPrincipalOfficersDto.getSalutation().equals(appSvcPrincipalOfficersDto1.getSalutation())
                                &&appSvcPrincipalOfficersDto.getName().equals(appSvcPrincipalOfficersDto1.getName())
                                &&appSvcPrincipalOfficersDto.getMobileNo().equals(appSvcPrincipalOfficersDto1.getMobileNo())
                                &&appSvcPrincipalOfficersDto.getEmailAddr().equals(appSvcPrincipalOfficersDto1.getEmailAddr());
                        if(!b){
                            ids.add(appSvcPrincipalOfficersDto.getIdNo());
                        }
                    }
                }
            }

        }
        return ids;
    }
    protected List<String> changeCgo(List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList, List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList) {
        List<String> ids=IaisCommonUtils.genNewArrayList();
        if (appSvcCgoDtoList != null && oldAppSvcCgoDtoList != null) {
            List<AppSvcPrincipalOfficersDto> n = PageDataCopyUtil.copyAppSvcCgo(appSvcCgoDtoList);
            List<AppSvcPrincipalOfficersDto> o = PageDataCopyUtil.copyAppSvcCgo(oldAppSvcCgoDtoList);
            if(n.equals(o)){
                return ids;
            }
            for(AppSvcPrincipalOfficersDto appSvcCgoDto : n){
                for(AppSvcPrincipalOfficersDto appSvcCgoDto1 : o){
                    if(appSvcCgoDto.getIdNo().equals(appSvcCgoDto1.getIdNo())){
                        boolean b=   appSvcCgoDto.getName().equals(appSvcCgoDto1.getName())&&appSvcCgoDto.getDesignation().equals(appSvcCgoDto1.getDesignation())
                                &&appSvcCgoDto.getEmailAddr().equals(appSvcCgoDto1.getEmailAddr())&&appSvcCgoDto.getMobileNo().equals(appSvcCgoDto1.getMobileNo());
                        if(!b){
                            ids.add(appSvcCgoDto.getIdNo()) ;
                        }
                    }
                }
            }
        }
        return ids;
    }
    protected List<String> changeClinicalDirector(List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos,List<AppSvcPrincipalOfficersDto> oldAppSvcClinicalDirectorDtos){
        List<String> ids=new ArrayList<>(10);
        if(appSvcClinicalDirectorDtos!=null&&oldAppSvcClinicalDirectorDtos!=null){
            List<AppSvcPrincipalOfficersDto> n = PageDataCopyUtil.copyAppSvcClinicalDirector(appSvcClinicalDirectorDtos);
            List<AppSvcPrincipalOfficersDto> o = PageDataCopyUtil.copyAppSvcClinicalDirector(oldAppSvcClinicalDirectorDtos);
            if(n.equals(o)){
                return ids;
            }
            for (AppSvcPrincipalOfficersDto v : n) {
                for (AppSvcPrincipalOfficersDto v1 : o) {
                    if (v.getIdNo().equals(v1.getIdNo())) {
                        boolean b = Objects.equals(v.getProfessionBoard(), v1.getProfessionBoard())
                                && Objects.equals(v.getProfRegNo(), v1.getProfRegNo())
                                && Objects.equals(v.getSalutation(), v1.getSalutation())
                                && Objects.equals(v.getName(), v1.getName())
                                && Objects.equals(v.getIdType(), v1.getIdType())
                                && Objects.equals(v.getDesignation(), v1.getDesignation())
                                && Objects.equals(v.getSpeciality(), v1.getSpeciality())
                                && Objects.equals(v.getSpecialtyGetDate(), v1.getSpecialtyGetDate())
                                && Objects.equals(v.getTypeOfCurrRegi(), v1.getTypeOfCurrRegi())
                                && Objects.equals(v.getCurrRegiDate(), v1.getCurrRegiDate())
                                && Objects.equals(v.getPraCerEndDate(), v1.getPraCerEndDate())
                                && Objects.equals(v.getTypeOfRegister(), v1.getTypeOfRegister())
                                && Objects.equals(v.getRelevantExperience(), v1.getRelevantExperience())
                                && Objects.equals(v.getHoldCerByEMS(), v1.getHoldCerByEMS())
                                && Objects.equals(v.getAclsExpiryDate(), v1.getAclsExpiryDate())
                                && Objects.equals(v.getMobileNo(), v1.getMobileNo())
                                && Objects.equals(v.getEmailAddr(), v1.getEmailAddr());
                        if (!b) {
                            ids.add(v.getIdNo());
                        }
                    }
                }
            }
        }
        return ids;
    }
}
