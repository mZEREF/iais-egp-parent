package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.WithOutRenewalDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.submission.client.App;
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
    public boolean isReplace(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
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
    public boolean isUpdate(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        for (int i = 0; i < newAppSvcRelatedInfoDtoList.size(); i++) {
            List<AppSvcCgoDto> newCgoList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcCgoDtoList();
            List<AppSvcCgoDto> oldCgoList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcCgoDtoList();
            if (newCgoList.size() == oldCgoList.size()) {
                for (int j = 0; j < newCgoList.size(); j++) {
                    AppSvcCgoDto newAppSvcCgoDto = newCgoList.get(j);
                    AppSvcCgoDto oldAppSvcCgoDto = oldCgoList.get(j);
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

                    if (!oldAppSvcCgoDtoE.equals(newAppSvcCgoDtoE)) {
                        return true;
                    }
                }
            }
            List<AppSvcPrincipalOfficersDto> newPoList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcPrincipalOfficersDtoList();
            List<AppSvcPrincipalOfficersDto> oldPoList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcPrincipalOfficersDtoList();
            if (newPoList.size() == oldPoList.size()) {
                for (int j = 0; j < newPoList.size(); j++) {
                    AppSvcPrincipalOfficersDto newPoDto = newPoList.get(j);
                    AppSvcPrincipalOfficersDto oldPoDto = oldPoList.get(j);
                    AppSvcPrincipalOfficersDto newPoE = new AppSvcPrincipalOfficersDto();
                    AppSvcPrincipalOfficersDto oldPoE = new AppSvcPrincipalOfficersDto();
                    String psnTypeNew = newPoDto.getPsnType();
                    String psnTypeOld = oldPoDto.getPsnType();
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnTypeNew) && ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnTypeOld)) {
                        newPoE.setName(newPoDto.getName());
                        newPoE.setDesignation(newPoDto.getDesignation());
                        newPoE.setMobileNo(newPoDto.getMobileNo());
                        newPoE.setOfficeTelNo(newPoDto.getOfficeTelNo());
                        newPoE.setEmailAddr(newPoDto.getEmailAddr());
                        oldPoE.setName(oldPoDto.getName());
                        oldPoE.setDesignation(oldPoDto.getDesignation());
                        oldPoE.setMobileNo(oldPoDto.getMobileNo());
                        oldPoE.setOfficeTelNo(oldPoDto.getOfficeTelNo());
                        oldPoE.setEmailAddr(oldPoDto.getEmailAddr());
                        if (!newPoE.equals(oldPoE)) {
                            return true;
                        }
                    }
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnTypeNew) && ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnTypeOld)) {
                        AppSvcPrincipalOfficersDto newDpoE = new AppSvcPrincipalOfficersDto();
                        AppSvcPrincipalOfficersDto oldDpoE = new AppSvcPrincipalOfficersDto();
                        newDpoE.setName(newPoDto.getName());
                        newDpoE.setDesignation(newPoDto.getDesignation());
                        newDpoE.setMobileNo(newPoDto.getMobileNo());
                        newDpoE.setOfficeTelNo(newPoDto.getOfficeTelNo());
                        newDpoE.setEmailAddr(newPoDto.getEmailAddr());
                        oldDpoE.setName(oldPoDto.getName());
                        oldDpoE.setDesignation(oldPoDto.getDesignation());
                        oldDpoE.setMobileNo(oldPoDto.getMobileNo());
                        oldDpoE.setOfficeTelNo(oldPoDto.getOfficeTelNo());
                        oldDpoE.setEmailAddr(oldPoDto.getEmailAddr());
                        if (!newDpoE.equals(oldDpoE)) {
                            return true;
                        }
                    }
                }
            }
            List<AppSvcPrincipalOfficersDto> newMatList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcMedAlertPersonList();
            List<AppSvcPrincipalOfficersDto> oldMatList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcMedAlertPersonList();
            if (newMatList.size() == oldMatList.size()) {
                for (int j = 0; j < newMatList.size(); j++) {
                    AppSvcPrincipalOfficersDto newMatDto = newMatList.get(j);
                    AppSvcPrincipalOfficersDto oldMatDto = oldMatList.get(j);
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
                    if (!newMatE.equals(oldMatE)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isEditDoc(AppSubmissionDto newAppSubmissionDto, AppSubmissionDto oldAppSubmissionDto) {

        List<String> newFileReportIds = IaisCommonUtils.genNewArrayList();
        List<String> oldFileReportIds = IaisCommonUtils.genNewArrayList();
        List<AppGrpPrimaryDocDto> newAppGrpPrimaryDocDtos = newAppSubmissionDto.getAppGrpPrimaryDocDtos();
        List<AppSvcDocDto> NewAppSvcDocDtoLit = newAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getAppSvcDocDtoLit();

        List<AppGrpPrimaryDocDto> oldAppGrpPrimaryDocDtos = oldAppSubmissionDto.getAppGrpPrimaryDocDtos();
        List<AppSvcDocDto> oldAppSvcDocDtoLit = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getAppSvcDocDtoLit();
        if(!IaisCommonUtils.isEmpty(newAppGrpPrimaryDocDtos)){
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto :newAppGrpPrimaryDocDtos){
                String fileRepoId = appGrpPrimaryDocDto.getFileRepoId();
                newFileReportIds.add(fileRepoId);
            }
        }
        if(!IaisCommonUtils.isEmpty(oldAppGrpPrimaryDocDtos)){
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto :oldAppGrpPrimaryDocDtos){
                String fileRepoId = appGrpPrimaryDocDto.getFileRepoId();
                oldFileReportIds.add(fileRepoId);
            }
        }
        if(!IaisCommonUtils.isEmpty(NewAppSvcDocDtoLit)){
            for(AppSvcDocDto appSvcDocDto :NewAppSvcDocDtoLit){
                String fileRepoId = appSvcDocDto.getFileRepoId();
                newFileReportIds.add(fileRepoId);
            }
        }
        if(!IaisCommonUtils.isEmpty(oldAppSvcDocDtoLit)){
            for(AppSvcDocDto appSvcDocDto :oldAppSvcDocDtoLit){
                String fileRepoId = appSvcDocDto.getFileRepoId();
                oldFileReportIds.add(fileRepoId);
            }
        }

        if (!IaisCommonUtils.isEmpty(newAppGrpPrimaryDocDtos) && !IaisCommonUtils.isEmpty(oldAppGrpPrimaryDocDtos)) {
            if (newAppGrpPrimaryDocDtos.size() == oldAppGrpPrimaryDocDtos.size()) {
                List<String> newFileIds = IaisCommonUtils.genNewArrayList();
                List<String> oldFileIds = IaisCommonUtils.genNewArrayList();
                for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : newAppGrpPrimaryDocDtos) {
                    String fileRepoId = appGrpPrimaryDocDto.getFileRepoId();
                    newFileIds.add(fileRepoId);
                }
                for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : oldAppGrpPrimaryDocDtos) {
                    String fileRepoId = appGrpPrimaryDocDto.getFileRepoId();
                    oldFileIds.add(fileRepoId);
                }
                newFileIds.retainAll(oldFileIds);
                if (newFileIds.size() != oldFileIds.size()) {
                    return true;
                }
            } else {
                return true;
            }
        }else {
            if (IaisCommonUtils.isEmpty(newAppGrpPrimaryDocDtos) && !IaisCommonUtils.isEmpty(oldAppGrpPrimaryDocDtos)){
                return true;
            }else if(!IaisCommonUtils.isEmpty(newAppGrpPrimaryDocDtos) && IaisCommonUtils.isEmpty(oldAppGrpPrimaryDocDtos)) {
                return true;
            }
        }
        if (!IaisCommonUtils.isEmpty(NewAppSvcDocDtoLit) && !IaisCommonUtils.isEmpty(oldAppSvcDocDtoLit)) {
            if (NewAppSvcDocDtoLit.size() == oldAppSvcDocDtoLit.size()) {
                List<String> newFileIds = IaisCommonUtils.genNewArrayList();
                List<String> oldFileIds = IaisCommonUtils.genNewArrayList();
                for (AppSvcDocDto appSvcDocDto : NewAppSvcDocDtoLit) {
                    String fileRepoId = appSvcDocDto.getFileRepoId();
                    newFileIds.add(fileRepoId);
                }
                for (AppSvcDocDto appSvcDocDto : oldAppSvcDocDtoLit) {
                    String fileRepoId = appSvcDocDto.getFileRepoId();
                    oldFileIds.add(fileRepoId);
                }
                newFileIds.retainAll(oldFileIds);
                if (newFileIds.size() != oldFileIds.size()) {
                    return true;
                }
            } else {
                return true;
            }
        }else {
            if (IaisCommonUtils.isEmpty(NewAppSvcDocDtoLit) && !IaisCommonUtils.isEmpty(oldAppSvcDocDtoLit)){
                return true;
            }else if(!IaisCommonUtils.isEmpty(NewAppSvcDocDtoLit) && IaisCommonUtils.isEmpty(oldAppSvcDocDtoLit)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> isUpdateCgo(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        List<String> idNos = IaisCommonUtils.genNewArrayList();
        AppSvcCgoDto newAppSvcCgoDtoE = new AppSvcCgoDto();
        AppSvcCgoDto oldAppSvcCgoDtoE = new AppSvcCgoDto();
        for (int i = 0; i < newAppSvcRelatedInfoDtoList.size(); i++) {
            List<AppSvcCgoDto> newCgoList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcCgoDtoList();
            List<AppSvcCgoDto> oldCgoList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcCgoDtoList();
            if (newCgoList.size() == oldCgoList.size()) {
                for (int j = 0; j < newCgoList.size(); j++) {
                    AppSvcCgoDto newAppSvcCgoDto = newCgoList.get(j);
                    AppSvcCgoDto oldAppSvcCgoDto = oldCgoList.get(j);
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
                    if (!oldAppSvcCgoDtoE.equals(newAppSvcCgoDtoE) && idNo.equals(idNo1)) {
                        idNos.add(idNo);
                    }
                }
            }
        }
        return idNos;
    }

    @Override
    public List<String> isUpdatePo(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        List<String> idNos = IaisCommonUtils.genNewArrayList();
        AppSvcPrincipalOfficersDto newPoE = new AppSvcPrincipalOfficersDto();
        AppSvcPrincipalOfficersDto oldPoE = new AppSvcPrincipalOfficersDto();
        for (int i = 0; i < newAppSvcRelatedInfoDtoList.size(); i++) {
            List<AppSvcPrincipalOfficersDto> newPoList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcPrincipalOfficersDtoList();
            List<AppSvcPrincipalOfficersDto> oldPoList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcPrincipalOfficersDtoList();
            if (newPoList.size() == oldPoList.size()) {
                for (int j = 0; j < newPoList.size(); j++) {
                    AppSvcPrincipalOfficersDto newPoDto = newPoList.get(j);
                    AppSvcPrincipalOfficersDto oldPoDto = oldPoList.get(j);
                    String psnType = newPoDto.getPsnType();
                    String psnType1 = oldPoDto.getPsnType();
                    String idNo = newPoDto.getIdNo();
                    String idNo1 = oldPoDto.getIdNo();
                    if (psnType.equals(psnType1)) {
                        newPoE.setName(newPoDto.getName());
                        newPoE.setDesignation(newPoDto.getDesignation());
                        newPoE.setMobileNo(newPoDto.getMobileNo());
                        newPoE.setOfficeTelNo(newPoDto.getOfficeTelNo());
                        newPoE.setEmailAddr(newPoDto.getEmailAddr());
                        oldPoE.setName(oldPoDto.getName());
                        oldPoE.setDesignation(oldPoDto.getDesignation());
                        oldPoE.setMobileNo(oldPoDto.getMobileNo());
                        oldPoE.setOfficeTelNo(oldPoDto.getOfficeTelNo());
                        oldPoE.setEmailAddr(oldPoDto.getEmailAddr());
                        if (!newPoE.equals(oldPoE) && idNo.equals(idNo1)) {
                            idNos.add(idNo);
                        }
                    }
                }
            }
        }
        return idNos;
    }

    @Override
    public List<String> isUpdateDpo(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        List<String> idNos = IaisCommonUtils.genNewArrayList();
        AppSvcPrincipalOfficersDto newDpoE = new AppSvcPrincipalOfficersDto();
        AppSvcPrincipalOfficersDto oldDpoE = new AppSvcPrincipalOfficersDto();
        for (int i = 0; i < newAppSvcRelatedInfoDtoList.size(); i++) {
            List<AppSvcPrincipalOfficersDto> newPoList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcPrincipalOfficersDtoList();
            List<AppSvcPrincipalOfficersDto> oldPoList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcPrincipalOfficersDtoList();
            if (newPoList.size() == oldPoList.size()) {
                for (int j = 0; j < newPoList.size(); j++) {
                    AppSvcPrincipalOfficersDto newPoDto = newPoList.get(j);
                    AppSvcPrincipalOfficersDto oldPoDto = oldPoList.get(j);
                    String idNo = newPoDto.getIdNo();
                    String idNo1 = oldPoDto.getIdNo();
                    String psnType = newPoDto.getPsnType();
                    String psnType1 = oldPoDto.getPsnType();
                    if (psnType.equals(psnType1)) {
                        newDpoE.setName(newPoDto.getName());
                        newDpoE.setDesignation(newPoDto.getDesignation());
                        newDpoE.setMobileNo(newPoDto.getMobileNo());
                        newDpoE.setOfficeTelNo(newPoDto.getOfficeTelNo());
                        newDpoE.setEmailAddr(newPoDto.getEmailAddr());
                        oldDpoE.setName(oldPoDto.getName());
                        oldDpoE.setDesignation(oldPoDto.getDesignation());
                        oldDpoE.setMobileNo(oldPoDto.getMobileNo());
                        oldDpoE.setOfficeTelNo(oldPoDto.getOfficeTelNo());
                        oldDpoE.setEmailAddr(oldPoDto.getEmailAddr());
                        if (!newDpoE.equals(oldDpoE) && idNo.equals(idNo1)) {
                            idNos.add(idNo);
                        }
                    }
                }
            }
        }
        return idNos;
    }

    @Override
    public List<String> isUpdateMat(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        List<String> idNos = IaisCommonUtils.genNewArrayList();
        AppSvcPrincipalOfficersDto newMatE = new AppSvcPrincipalOfficersDto();
        AppSvcPrincipalOfficersDto oldMatE = new AppSvcPrincipalOfficersDto();
        for (int i = 0; i < newAppSvcRelatedInfoDtoList.size(); i++) {
            List<AppSvcPrincipalOfficersDto> newMatList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcMedAlertPersonList();
            List<AppSvcPrincipalOfficersDto> oldMatList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcMedAlertPersonList();
            if (newMatList.size() == oldMatList.size()) {
                for (int j = 0; j < newMatList.size(); j++) {
                    AppSvcPrincipalOfficersDto newMatDto = newMatList.get(j);
                    AppSvcPrincipalOfficersDto oldMatDto = oldMatList.get(j);
                    String idNo = newMatDto.getIdNo();
                    String idNo1 = oldMatDto.getIdNo();
                    newMatE.setName(newMatDto.getName());
                    newMatE.setMobileNo(newMatDto.getMobileNo());
                    newMatE.setEmailAddr(newMatDto.getEmailAddr());
                    newMatE.setPreferredMode(newMatDto.getPreferredMode());
                    oldMatE.setName(oldMatDto.getName());
                    oldMatE.setMobileNo(oldMatDto.getMobileNo());
                    oldMatE.setEmailAddr(oldMatDto.getEmailAddr());
                    oldMatE.setPreferredMode(oldMatDto.getPreferredMode());
                    if (!newMatE.equals(oldMatE) && idNo.equals(idNo1)) {
                        idNos.add(idNo);
                    }
                }
            }
        }
        return idNos;
    }
}
