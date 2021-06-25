package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.action.RequestForChangeMenuDelegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicSvcClinicalDirectorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.rfcutil.PageDataCopyUtil;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceInfoChangeEffectPerson;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Wenkang
 * @date 2021/4/27 15:48
 */
@Component
public class ServiceInfoChangeEffectPersonAbstract implements ServiceInfoChangeEffectPerson {
    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private RequestForChangeService requestForChangeService;
    @Override
    public List<AppSubmissionDto> personContact(String licenseeId, AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto) throws Exception {
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setServiceEdit(true);
        appEditSelectDto.setPremisesEdit(false);
        appEditSelectDto.setDocEdit(false);
        appEditSelectDto.setPoEdit(false);
        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        if (appSvcRelatedInfoDto == null || oldAppSvcRelatedInfoDto == null) {
            return null;
        }
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtoList = appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();

        List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList = oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList = oldAppSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtoList = oldAppSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcClinicalDirectorDtoList = oldAppSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();

        Set<String> set = IaisCommonUtils.genNewHashSet();
        List<String> list = IaisCommonUtils.genNewArrayList();
        List<String> list1 = changeCgo(appSvcCgoDtoList, oldAppSvcCgoDtoList);
        List<String> list2 = changeMeadrter(appSvcMedAlertPersonList, oldAppSvcMedAlertPersonList);
        List<String> list3 = changePo(appSvcPrincipalOfficersDtoList, oldAppSvcPrincipalOfficersDtoList);
        List<String> list4 = changeClinicalDirector(appSvcClinicalDirectorDtoList, oldAppSvcClinicalDirectorDtoList);

        set.addAll(list1);
        set.addAll(list2);
        set.addAll(list3);
        list.addAll(set);
        Set<String> set1=new HashSet<>(list4);
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
        List<AppSubmissionDto> appSubmissionDtoList = IaisCommonUtils.genNewArrayList();
        for (String string : licenceIdList) {
            AppSubmissionDto appSubmissionDtoByLicenceId = requestForChangeService.getAppSubmissionDtoByLicenceId(string);
            if (appSubmissionDtoByLicenceId == null || appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList() == null) {
                continue;
            }
            AppSvcRelatedInfoDto appSvcRelatedInfoDto2 = appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0);

            List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList2 = appSvcRelatedInfoDto2.getAppSvcCgoDtoList();
            if(!list1.isEmpty()){
                if (appSvcCgoDtoList2 != null && appSvcCgoDtoList != null) {
                    for(AppSvcPrincipalOfficersDto var1 : appSvcCgoDtoList2){
                        for(AppSvcPrincipalOfficersDto var2 : appSvcCgoDtoList){
                            if(var1.getIdNo().equals(var2.getIdNo())){
                                appSvcCgoDtoList2.set(appSvcCgoDtoList2.indexOf(var1),var2);
                            }
                        }
                    }
                    appSvcRelatedInfoDto2.setAppSvcCgoDtoList(appSvcCgoDtoList2);
                }
            }
            List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList2 = appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0).getAppSvcMedAlertPersonList();
            if(!list2.isEmpty()){
                if (appSvcMedAlertPersonList2 != null && appSvcMedAlertPersonList != null) {
                    for(AppSvcPrincipalOfficersDto var1 : appSvcMedAlertPersonList2){
                        for(AppSvcPrincipalOfficersDto var2 : appSvcMedAlertPersonList){
                            if(var1.getIdNo().equals(var2.getIdNo())){
                                appSvcMedAlertPersonList2.set(appSvcMedAlertPersonList2.indexOf(var1),var2);
                            }
                        }
                    }
                    appSvcRelatedInfoDto2.setAppSvcMedAlertPersonList(appSvcMedAlertPersonList2);
                }
            }
            List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList2 = appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0).getAppSvcPrincipalOfficersDtoList();
            if(!list3.isEmpty()){
                if (appSvcPrincipalOfficersDtoList2 != null && appSvcPrincipalOfficersDtoList != null) {
                    for(AppSvcPrincipalOfficersDto var1 : appSvcPrincipalOfficersDtoList2){
                        for(AppSvcPrincipalOfficersDto var2 : appSvcPrincipalOfficersDtoList){
                            if(var1.getIdNo().equals(var2.getIdNo())){
                                appSvcPrincipalOfficersDtoList2.set(appSvcPrincipalOfficersDtoList2.indexOf(var1),var2);
                            }
                        }
                    }
                    appSvcRelatedInfoDto2.setAppSvcPrincipalOfficersDtoList(appSvcPrincipalOfficersDtoList2);
                }
            }
            List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtoList1 = appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0).getAppSvcClinicalDirectorDtoList();
            if(!list4.isEmpty()){
                if(appSvcClinicalDirectorDtoList!=null && appSvcClinicalDirectorDtoList1!=null){
                    for (AppSvcPrincipalOfficersDto var1 : appSvcClinicalDirectorDtoList1) {
                        for (AppSvcPrincipalOfficersDto var2 : appSvcClinicalDirectorDtoList) {
                            if(var1.getIdNo().equals(var2.getIdNo())){
                                appSvcClinicalDirectorDtoList1.set(appSvcClinicalDirectorDtoList1.indexOf(var1),var2);
                            }
                        }
                    }
                    appSvcRelatedInfoDto2.setAppSvcClinicalDirectorDtoList(appSvcClinicalDirectorDtoList1);
                }
            }
            appSubmissionDtoByLicenceId.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDtoByLicenceId.setPartPremise(false);
            appSubmissionDtoByLicenceId.setGetAppInfoFromDto(true);
            RequestForChangeMenuDelegator.oldPremiseToNewPremise(appSubmissionDtoByLicenceId);
            appSubmissionDtoByLicenceId.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
            appSubmissionService.transform(appSubmissionDtoByLicenceId, appSubmissionDto.getLicenseeId());
            requestForChangeService.premisesDocToSvcDoc(appSubmissionDtoByLicenceId);
            appSubmissionDtoByLicenceId.setAutoRfc(true);
            appSubmissionDtoByLicenceId.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);
            appSubmissionDtoByLicenceId.setIsNeedNewLicNo(AppConsts.NO);
            for(AppGrpPremisesDto appGrpPremisesDto : appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList()){
                appGrpPremisesDto.setNeedNewLicNo(Boolean.FALSE);
                appGrpPremisesDto.setSelfAssMtFlag(4);
            }
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);
            appSubmissionDtoList.add(appSubmissionDtoByLicenceId);
        }

        return appSubmissionDtoList;
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
                    if(v.getIdNo().equals(v1.getIdNo())){
                        boolean b=    v.getProfessionBoard().equals(v1.getProfessionBoard())
                                &&v.getProfRegNo().equals(v1.getProfRegNo())
                                &&v.getSalutation().equals(v1.getSalutation())
                                &&v.getName().equals(v1.getName())
                                &&v.getIdType().equals(v1.getIdType())
                                &&v.getDesignation().equals(v1.getDesignation())
                                &&v.getSpeciality().equals(v1.getSpeciality())
                                &&v.getSpecialtyGetDate().equals(v1.getSpecialtyGetDate())
                                &&v.getTypeOfCurrRegi().equals(v1.getTypeOfCurrRegi())
                                &&v.getCurrRegiDate().equals(v1.getCurrRegiDate())
                                &&v.getPraCerEndDate().equals(v1.getPraCerEndDate())
                                &&v.getTypeOfRegister().equals(v1.getTypeOfRegister())
                                &&v.getRelevantExperience().equals(v1.getRelevantExperience())
                                &&v.getHoldCerByEMS().equals(v1.getHoldCerByEMS())
                                &&v.getAclsExpiryDate().equals(v1.getAclsExpiryDate())
                                &&v.getMobileNo().equals(v1.getMobileNo())
                                &&v.getEmailAddr().equals(v1.getEmailAddr());
                        if(!b){
                            ids.add(v.getIdNo());
                        }
                    }
                }
            }
        }
        return ids;
    }
}
