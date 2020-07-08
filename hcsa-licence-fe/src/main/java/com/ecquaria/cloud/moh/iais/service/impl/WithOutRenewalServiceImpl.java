package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.WithOutRenewalDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.google.inject.internal.cglib.reflect.$FastConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sop.util.CopyUtil;

import java.util.List;

@Service
@Slf4j
public class WithOutRenewalServiceImpl implements WithOutRenewalService {

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private SystemAdminClient systemAdminClient;

    @Override
    public WithOutRenewalDto getRenewalViewByLicNo(String licenceNo) {
        return null;
    }

    @Override
    public List<AppSubmissionDto> getAppSubmissionDtos(List<String> licenceIds) {
        List<AppSubmissionDto> entity = licenceClient.getAppSubmissionDtos(licenceIds).getEntity();
        if (!IaisCommonUtils.isEmpty(entity)) {
            for (AppSubmissionDto appSubmissionDto : entity) {
                appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            }
        }
        return entity;
    }

    @Override
    public String getAppGrpNoByAppType(String appType) {
        return systemAdminClient.applicationNumber(appType).getEntity();
    }

    @Override
    public String getLicenceNumberByLicenceId(String licenceId) {
        return licenceClient.getLicBylicId(licenceId).getEntity().getLicenceNo();
    }

    @Override
    public Boolean isReplace(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        for (int i = 0; i < newAppSvcRelatedInfoDtoList.size(); i++) {
            List<AppSvcCgoDto> newCgoList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcCgoDtoList();
            List<AppSvcCgoDto> oldCgoList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcCgoDtoList();
            if (newCgoList.size() == oldCgoList.size()) {
                for (int j = 0; j < newCgoList.size(); j++) {
                    String newIdNo = newCgoList.get(i).getIdNo();
                    String oldIdNo = oldCgoList.get(i).getIdNo();
                    if (!newIdNo.equals(oldIdNo)) {
                        return true;
                    }
                }
            } else {
                return true;
            }

            List<AppSvcPrincipalOfficersDto> newPoList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcPrincipalOfficersDtoList();
            List<AppSvcPrincipalOfficersDto> oldPoList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcPrincipalOfficersDtoList();
            if (newPoList.size() == oldPoList.size()) {
                for (int j = 0; j < newPoList.size(); j++) {
                    String newIdNo = newPoList.get(i).getIdNo();
                    String oldIdNo = oldPoList.get(i).getIdNo();
                    if (!newIdNo.equals(oldIdNo)) {
                        return true;
                    }
                }
            } else {
                return true;
            }

            List<AppSvcPrincipalOfficersDto> newMatList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcMedAlertPersonList();
            List<AppSvcPrincipalOfficersDto> oldMatList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcMedAlertPersonList();
            if (newMatList.size() == oldMatList.size()) {
                for (int j = 0; j < newMatList.size(); j++) {
                    String newIdNo = newMatList.get(i).getIdNo();
                    String oldIdNo = oldMatList.get(i).getIdNo();
                    if (!newIdNo.equals(oldIdNo)) {
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean isUpdate(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        boolean flag = false ;
        for (int i = 0; i < newAppSvcRelatedInfoDtoList.size(); i++) {
            List<AppSvcCgoDto> newCgoList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcCgoDtoList();
            List<AppSvcCgoDto> oldCgoList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcCgoDtoList();
            if (newCgoList.size() == oldCgoList.size()) {
                for (int j = 0; j < newCgoList.size(); j++) {
                    AppSvcCgoDto newAppSvcCgoDto = newCgoList.get(i);
                    AppSvcCgoDto oldAppSvcCgoDto = oldCgoList.get(i);
                    AppSvcCgoDto newAppSvcCgoDtoE = new AppSvcCgoDto();
                    AppSvcCgoDto oldAppSvcCgoDtoE = new AppSvcCgoDto();
                    newAppSvcCgoDtoE.setName(newAppSvcCgoDto.getName());
                    newAppSvcCgoDtoE.setDesignation(newAppSvcCgoDto.getDesignation());
                    newAppSvcCgoDtoE.setProfessionType(newAppSvcCgoDto.getProfessionType());
                    newAppSvcCgoDtoE.setProfRegNo(newAppSvcCgoDto.getProfRegNo());
                    newAppSvcCgoDtoE.setSpeciality(newAppSvcCgoDto.getSpeciality());
                    newAppSvcCgoDtoE.setSpecialityOther(newAppSvcCgoDto.getSpecialityOther());
                    newAppSvcCgoDtoE.setSubSpeciality(newAppSvcCgoDto.getSubSpeciality());
                    newAppSvcCgoDtoE.setMobileNo(newAppSvcCgoDto.getMobileNo());
                    newAppSvcCgoDtoE.setEmailAddr(newAppSvcCgoDto.getEmailAddr());

                    oldAppSvcCgoDtoE.setName(oldAppSvcCgoDto.getName());
                    oldAppSvcCgoDtoE.setDesignation(oldAppSvcCgoDto.getDesignation());
                    oldAppSvcCgoDtoE.setProfessionType(oldAppSvcCgoDto.getProfessionType());
                    oldAppSvcCgoDtoE.setProfRegNo(oldAppSvcCgoDto.getProfRegNo());
                    oldAppSvcCgoDtoE.setSpeciality(oldAppSvcCgoDto.getSpeciality());
                    oldAppSvcCgoDtoE.setSpecialityOther(oldAppSvcCgoDto.getSpecialityOther());
                    oldAppSvcCgoDtoE.setSubSpeciality(oldAppSvcCgoDto.getSubSpeciality());
                    oldAppSvcCgoDtoE.setMobileNo(oldAppSvcCgoDto.getMobileNo());
                    oldAppSvcCgoDtoE.setEmailAddr(oldAppSvcCgoDto.getEmailAddr());

                    String idNo = newAppSvcCgoDto.getIdNo();
                    String idNo1 = oldAppSvcCgoDto.getIdNo();
                    if (idNo.equals(idNo1)) {
                        if (oldAppSvcCgoDtoE.equals(newAppSvcCgoDtoE)) {
                            flag =  false;
                        } else {
                            flag =  true;
                        }
                    }
                }
            }

            List<AppSvcPrincipalOfficersDto> newPoList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcPrincipalOfficersDtoList();
            List<AppSvcPrincipalOfficersDto> oldPoList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcPrincipalOfficersDtoList();
            if (newPoList.size() == oldPoList.size()) {
                for (int j = 0; j < newPoList.size(); j++) {
                    AppSvcPrincipalOfficersDto newPoDto = newPoList.get(i);
                    AppSvcPrincipalOfficersDto oldPoDto = oldPoList.get(i);
                    String idNo = newPoDto.getIdNo();
                    String idNo1 = oldPoDto.getIdNo();
                    if (newPoDto.isPoPsn() && oldPoDto.isPoPsn() && idNo.equals(idNo1)) {
                        AppSvcPrincipalOfficersDto newPoE = new AppSvcPrincipalOfficersDto();
                        AppSvcPrincipalOfficersDto oldPoE = new AppSvcPrincipalOfficersDto();
                        newPoE.setName(newPoDto.getName());
                        newPoE.setName(newPoDto.getDesignation());
                        newPoE.setName(newPoDto.getMobileNo());
                        newPoE.setName(newPoDto.getOfficeTelNo());
                        newPoE.setName(newPoDto.getEmailAddr());
                        oldPoE.setName(oldPoDto.getName());
                        oldPoE.setName(oldPoDto.getDesignation());
                        oldPoE.setName(oldPoDto.getMobileNo());
                        oldPoE.setName(oldPoDto.getOfficeTelNo());
                        oldPoE.setName(oldPoDto.getEmailAddr());
                        if (newPoE.equals(oldPoE)) {
                            return false;
                        } else {
                            return true;
                        }

                    }
                    if (newPoDto.isDpoPsn() && oldPoDto.isDpoPsn() && idNo.equals(idNo1)) {
                        AppSvcPrincipalOfficersDto newDpoE = new AppSvcPrincipalOfficersDto();
                        AppSvcPrincipalOfficersDto oldDpoE = new AppSvcPrincipalOfficersDto();
                        newDpoE.setName(newPoDto.getName());
                        newDpoE.setName(newPoDto.getDesignation());
                        newDpoE.setName(newPoDto.getMobileNo());
                        newDpoE.setName(newPoDto.getOfficeTelNo());
                        newDpoE.setName(newPoDto.getEmailAddr());
                        oldDpoE.setName(oldPoDto.getName());
                        oldDpoE.setName(oldPoDto.getDesignation());
                        oldDpoE.setName(oldPoDto.getMobileNo());
                        oldDpoE.setName(oldPoDto.getOfficeTelNo());
                        oldDpoE.setName(oldPoDto.getEmailAddr());
                        if (newDpoE.equals(oldDpoE)) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
            }

            List<AppSvcPrincipalOfficersDto> newMatList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcMedAlertPersonList();
            List<AppSvcPrincipalOfficersDto> oldMatList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcMedAlertPersonList();
            if (newMatList.size() == oldMatList.size()) {
                for (int j = 0; j < newMatList.size(); j++) {
                    AppSvcPrincipalOfficersDto newMatDto = newMatList.get(i);
                    AppSvcPrincipalOfficersDto oldMatDto = oldMatList.get(i);
                    AppSvcPrincipalOfficersDto newMatE = new AppSvcPrincipalOfficersDto();
                    AppSvcPrincipalOfficersDto oldMatE = new AppSvcPrincipalOfficersDto();
                    newMatE.setName(newMatDto.getName());
                    newMatE.setMobileNo(newMatDto.getMobileNo());
                    newMatE.setEmailAddr(newMatDto.getEmailAddr());
                    newMatE.setEmailAddr(newMatDto.getPreferredMode());
                    oldMatE.setName(oldMatDto.getName());
                    oldMatE.setMobileNo(oldMatDto.getMobileNo());
                    oldMatE.setEmailAddr(oldMatDto.getEmailAddr());
                    oldMatE.setEmailAddr(oldMatDto.getPreferredMode());
                    if (newMatE.equals(oldMatE)) {
                        return false;
                    } else {
                        return true;
                    }

                }
            }
        }
        return false;
    }

    @Override
    public List<String> isUpdateCgo(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        boolean flag = false;
        List<String> idNos = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < newAppSvcRelatedInfoDtoList.size(); i++) {
            List<AppSvcCgoDto> newCgoList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcCgoDtoList();
            List<AppSvcCgoDto> oldCgoList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcCgoDtoList();
            if (newCgoList.size() == oldCgoList.size()) {
                for (int j = 0; j < newCgoList.size(); j++) {
                    AppSvcCgoDto newAppSvcCgoDto = newCgoList.get(i);
                    AppSvcCgoDto oldAppSvcCgoDto = oldCgoList.get(i);
                    AppSvcCgoDto newAppSvcCgoDtoE = new AppSvcCgoDto();
                    AppSvcCgoDto oldAppSvcCgoDtoE = new AppSvcCgoDto();
                    newAppSvcCgoDtoE.setName(newAppSvcCgoDto.getName());
                    newAppSvcCgoDtoE.setDesignation(newAppSvcCgoDto.getDesignation());
                    newAppSvcCgoDtoE.setProfessionType(newAppSvcCgoDto.getProfessionType());
                    newAppSvcCgoDtoE.setProfRegNo(newAppSvcCgoDto.getProfRegNo());
                    newAppSvcCgoDtoE.setSpeciality(newAppSvcCgoDto.getSpeciality());
                    newAppSvcCgoDtoE.setSpecialityOther(newAppSvcCgoDto.getSpecialityOther());
                    newAppSvcCgoDtoE.setSubSpeciality(newAppSvcCgoDto.getSubSpeciality());
                    newAppSvcCgoDtoE.setMobileNo(newAppSvcCgoDto.getMobileNo());
                    newAppSvcCgoDtoE.setEmailAddr(newAppSvcCgoDto.getEmailAddr());

                    oldAppSvcCgoDtoE.setName(oldAppSvcCgoDto.getName());
                    oldAppSvcCgoDtoE.setDesignation(oldAppSvcCgoDto.getDesignation());
                    oldAppSvcCgoDtoE.setProfessionType(oldAppSvcCgoDto.getProfessionType());
                    oldAppSvcCgoDtoE.setProfRegNo(oldAppSvcCgoDto.getProfRegNo());
                    oldAppSvcCgoDtoE.setSpeciality(oldAppSvcCgoDto.getSpeciality());
                    oldAppSvcCgoDtoE.setSpecialityOther(oldAppSvcCgoDto.getSpecialityOther());
                    oldAppSvcCgoDtoE.setSubSpeciality(oldAppSvcCgoDto.getSubSpeciality());
                    oldAppSvcCgoDtoE.setMobileNo(oldAppSvcCgoDto.getMobileNo());
                    oldAppSvcCgoDtoE.setEmailAddr(oldAppSvcCgoDto.getEmailAddr());

                    String idNo = newAppSvcCgoDto.getIdNo();
                    String idNo1 = oldAppSvcCgoDto.getIdNo();
                    if (idNo.equals(idNo1)) {
                        if (oldAppSvcCgoDtoE.equals(newAppSvcCgoDtoE)) {
                            flag = false;
                        } else {
                            flag = true;
                            idNos.add(idNo);
                        }
                    }
                }
            }
        }
        if(flag){
            return idNos;
        }else {
            return null;
        }

    }

    @Override
    public List<String> isUpdatePo(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        boolean flag = false;
        List<String> idNos = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < newAppSvcRelatedInfoDtoList.size(); i++) {
            List<AppSvcPrincipalOfficersDto> newPoList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcPrincipalOfficersDtoList();
            List<AppSvcPrincipalOfficersDto> oldPoList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcPrincipalOfficersDtoList();
            if (newPoList.size() == oldPoList.size()) {
                for (int j = 0; j < newPoList.size(); j++) {
                    AppSvcPrincipalOfficersDto newPoDto = newPoList.get(i);
                    AppSvcPrincipalOfficersDto oldPoDto = oldPoList.get(i);
                    String idNo = newPoDto.getIdNo();
                    String idNo1 = oldPoDto.getIdNo();
                    if (newPoDto.isPoPsn() && oldPoDto.isPoPsn() && idNo.equals(idNo1)) {
                        AppSvcPrincipalOfficersDto newPoE = new AppSvcPrincipalOfficersDto();
                        AppSvcPrincipalOfficersDto oldPoE = new AppSvcPrincipalOfficersDto();
                        newPoE.setName(newPoDto.getName());
                        newPoE.setName(newPoDto.getDesignation());
                        newPoE.setName(newPoDto.getMobileNo());
                        newPoE.setName(newPoDto.getOfficeTelNo());
                        newPoE.setName(newPoDto.getEmailAddr());
                        oldPoE.setName(oldPoDto.getName());
                        oldPoE.setName(oldPoDto.getDesignation());
                        oldPoE.setName(oldPoDto.getMobileNo());
                        oldPoE.setName(oldPoDto.getOfficeTelNo());
                        oldPoE.setName(oldPoDto.getEmailAddr());
                        if (newPoE.equals(oldPoE)) {
                            flag = false;
                        } else {
                            flag = true;
                            idNos.add(idNo);
                        }
                    }
                }
            }
        }
        if(flag){
            return idNos;
        }else {
            return null;
        }
    }

    @Override
    public List<String> isUpdateDpo(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        boolean flag = false;
        List<String> idNos = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < newAppSvcRelatedInfoDtoList.size(); i++) {
            List<AppSvcPrincipalOfficersDto> newPoList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcPrincipalOfficersDtoList();
            List<AppSvcPrincipalOfficersDto> oldPoList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcPrincipalOfficersDtoList();
            if (newPoList.size() == oldPoList.size()) {
                for (int j = 0; j < newPoList.size(); j++) {
                    AppSvcPrincipalOfficersDto newPoDto = newPoList.get(i);
                    AppSvcPrincipalOfficersDto oldPoDto = oldPoList.get(i);
                    String idNo = newPoDto.getIdNo();
                    String idNo1 = oldPoDto.getIdNo();
                    if (newPoDto.isDpoPsn() && oldPoDto.isDpoPsn() && idNo.equals(idNo1)) {
                        AppSvcPrincipalOfficersDto newDpoE = new AppSvcPrincipalOfficersDto();
                        AppSvcPrincipalOfficersDto oldDpoE = new AppSvcPrincipalOfficersDto();
                        newDpoE.setName(newPoDto.getName());
                        newDpoE.setName(newPoDto.getDesignation());
                        newDpoE.setName(newPoDto.getMobileNo());
                        newDpoE.setName(newPoDto.getOfficeTelNo());
                        newDpoE.setName(newPoDto.getEmailAddr());
                        oldDpoE.setName(oldPoDto.getName());
                        oldDpoE.setName(oldPoDto.getDesignation());
                        oldDpoE.setName(oldPoDto.getMobileNo());
                        oldDpoE.setName(oldPoDto.getOfficeTelNo());
                        oldDpoE.setName(oldPoDto.getEmailAddr());
                        if (newDpoE.equals(oldDpoE)) {
                            flag = false;
                        } else {
                            flag = true;
                            idNos.add(idNo);
                        }
                    }
                }
            }
        }
        if(flag){
            return idNos;
        }else {
            return null;
        }
    }

    @Override
    public List<String> isUpdateMat(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        boolean flag = false;
        List<String> idNos = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < newAppSvcRelatedInfoDtoList.size(); i++) {
            List<AppSvcPrincipalOfficersDto> newMatList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcMedAlertPersonList();
            List<AppSvcPrincipalOfficersDto> oldMatList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcMedAlertPersonList();
            if (newMatList.size() == oldMatList.size()) {
                for (int j = 0; j < newMatList.size(); j++) {
                    AppSvcPrincipalOfficersDto newMatDto = newMatList.get(i);
                    AppSvcPrincipalOfficersDto oldMatDto = oldMatList.get(i);
                    AppSvcPrincipalOfficersDto newMatE = new AppSvcPrincipalOfficersDto();
                    AppSvcPrincipalOfficersDto oldMatE = new AppSvcPrincipalOfficersDto();
                    String idNo = newMatDto.getIdNo();
                    newMatE.setName(newMatDto.getName());
                    newMatE.setMobileNo(newMatDto.getMobileNo());
                    newMatE.setEmailAddr(newMatDto.getEmailAddr());
                    newMatE.setEmailAddr(newMatDto.getPreferredMode());
                    oldMatE.setName(oldMatDto.getName());
                    oldMatE.setMobileNo(oldMatDto.getMobileNo());
                    oldMatE.setEmailAddr(oldMatDto.getEmailAddr());
                    oldMatE.setEmailAddr(oldMatDto.getPreferredMode());
                    if (newMatE.equals(oldMatE)) {
                        flag =  false;
                    } else {
                        flag =  true;
                        idNos.add(idNo);
                    }
                }
            }
        }
        if(flag){
            return idNos;
        }else {
            return null;
        }
    }
}
