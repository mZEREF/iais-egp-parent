package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.WithOutRenewalDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.RfcHelper;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WithOutRenewalServiceImpl implements WithOutRenewalService {

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private SystemAdminClient systemAdminClient;

    @Autowired
    private LicCommService commService;

    @Override
    public WithOutRenewalDto getRenewalViewByLicNo(String licenceNo) {
        return null;
    }

    @Override
    public List<AppSubmissionDto> getAppSubmissionDtos(List<String> licenceIds) {
        List<AppSubmissionDto> entity = commService.getAppSubmissionDtosByLicenceIds(licenceIds);
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
            List<AppSvcPrincipalOfficersDto> newCgoList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcCgoDtoList();
            List<AppSvcPrincipalOfficersDto> oldCgoList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcCgoDtoList();
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
        AppSvcPrincipalOfficersDto newPoE = new AppSvcPrincipalOfficersDto();
        AppSvcPrincipalOfficersDto oldPoE = new AppSvcPrincipalOfficersDto();
        AppSvcPrincipalOfficersDto newMatE = new AppSvcPrincipalOfficersDto();
        AppSvcPrincipalOfficersDto oldMatE = new AppSvcPrincipalOfficersDto();
        AppSvcPrincipalOfficersDto newDpoE = new AppSvcPrincipalOfficersDto();
        AppSvcPrincipalOfficersDto oldDpoE = new AppSvcPrincipalOfficersDto();
        for (int i = 0; i < newAppSvcRelatedInfoDtoList.size(); i++) {
            List<AppSvcPrincipalOfficersDto> newCgoList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcCgoDtoList();
            List<AppSvcPrincipalOfficersDto> oldCgoList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcCgoDtoList();
            if (newCgoList.size() == oldCgoList.size()) {
                for (int j = 0; j < newCgoList.size(); j++) {
                    AppSvcPrincipalOfficersDto newAppSvcCgoDto = newCgoList.get(j);
                    AppSvcPrincipalOfficersDto oldAppSvcCgoDto = oldCgoList.get(j);
                    AppSvcPrincipalOfficersDto newAppSvcCgoDtoE = new AppSvcPrincipalOfficersDto();
                    AppSvcPrincipalOfficersDto oldAppSvcCgoDtoE = new AppSvcPrincipalOfficersDto();
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
                    clearAppSvcPrincipalOfficersDto(newPoE);
                    clearAppSvcPrincipalOfficersDto(oldPoE);
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
                        clearAppSvcPrincipalOfficersDto(newPoE);
                        clearAppSvcPrincipalOfficersDto(oldDpoE);
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
                    clearAppSvcPrincipalOfficersDto(newMatE);
                    clearAppSvcPrincipalOfficersDto(oldMatE);
                    newMatE.setName(newMatDto.getName());
                    newMatE.setMobileNo(newMatDto.getMobileNo());
                    newMatE.setEmailAddr(newMatDto.getEmailAddr());
                    newMatE.setDescription(newMatDto.getDescription());
                    oldMatE.setName(oldMatDto.getName());
                    oldMatE.setMobileNo(oldMatDto.getMobileNo());
                    oldMatE.setEmailAddr(oldMatDto.getEmailAddr());
                    oldMatE.setDescription(oldMatDto.getDescription());
                    if (!newMatE.equals(oldMatE)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void clearAppSvcPrincipalOfficersDto(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto) {
        appSvcPrincipalOfficersDto.setName(null);
        appSvcPrincipalOfficersDto.setDesignation(null);
        appSvcPrincipalOfficersDto.setMobileNo(null);
        appSvcPrincipalOfficersDto.setOfficeTelNo(null);
        appSvcPrincipalOfficersDto.setEmailAddr(null);
    }

    @Override
    public boolean isEditDoc(AppSubmissionDto newAppSubmissionDto, AppSubmissionDto oldAppSubmissionDto) {
        List<String> newFileReportIds = IaisCommonUtils.genNewArrayList();
        List<String> oldFileReportIds = IaisCommonUtils.genNewArrayList();
        RfcHelper.svcDocToPresmise(oldAppSubmissionDto);

        List<AppGrpPrimaryDocDto> newAppGrpPrimaryDocDtos = newAppSubmissionDto.getAppGrpPrimaryDocDtos();
        List<AppSvcDocDto> NewAppSvcDocDtoLit = newAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getAppSvcDocDtoLit();

        List<AppGrpPrimaryDocDto> oldAppGrpPrimaryDocDtos = oldAppSubmissionDto.getAppGrpPrimaryDocDtos();
        List<AppSvcDocDto> oldAppSvcDocDtoLit = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getAppSvcDocDtoLit();

        if (!IaisCommonUtils.isEmpty(newAppGrpPrimaryDocDtos)) {
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : newAppGrpPrimaryDocDtos) {
                String fileRepoId = appGrpPrimaryDocDto.getFileRepoId();
                newFileReportIds.add(fileRepoId);
            }
        }
        if (!IaisCommonUtils.isEmpty(oldAppGrpPrimaryDocDtos)) {
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : oldAppGrpPrimaryDocDtos) {
                String fileRepoId = appGrpPrimaryDocDto.getFileRepoId();
                oldFileReportIds.add(fileRepoId);
            }
        }
        if (!IaisCommonUtils.isEmpty(NewAppSvcDocDtoLit)) {
            for (AppSvcDocDto appSvcDocDto : NewAppSvcDocDtoLit) {
                String fileRepoId = appSvcDocDto.getFileRepoId();
                newFileReportIds.add(fileRepoId);
            }
        }
        if (!IaisCommonUtils.isEmpty(oldAppSvcDocDtoLit)) {
            for (AppSvcDocDto appSvcDocDto : oldAppSvcDocDtoLit) {
                String fileRepoId = appSvcDocDto.getFileRepoId();
                oldFileReportIds.add(fileRepoId);
            }
        }
        if (newFileReportIds.size() == oldFileReportIds.size()) {
            //qu bing ji
            newFileReportIds.removeAll(oldFileReportIds);
            newFileReportIds.addAll(oldFileReportIds);
            if (newFileReportIds.size() != oldFileReportIds.size()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public List<String> isUpdateCgo(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) {
        List<String> idNos = IaisCommonUtils.genNewArrayList();
        AppSvcPrincipalOfficersDto newAppSvcCgoDtoE = new AppSvcPrincipalOfficersDto();
        AppSvcPrincipalOfficersDto oldAppSvcCgoDtoE = new AppSvcPrincipalOfficersDto();
        for (int i = 0; i < newAppSvcRelatedInfoDtoList.size(); i++) {
            List<AppSvcPrincipalOfficersDto> newCgoList = newAppSvcRelatedInfoDtoList.get(i).getAppSvcCgoDtoList();
            List<AppSvcPrincipalOfficersDto> oldCgoList = oldAppSvcRelatedInfoDtoList.get(i).getAppSvcCgoDtoList();
            if (newCgoList.size() == oldCgoList.size()) {
                for (int j = 0; j < newCgoList.size(); j++) {
                    AppSvcPrincipalOfficersDto newAppSvcCgoDto = newCgoList.get(j);
                    AppSvcPrincipalOfficersDto oldAppSvcCgoDto = oldCgoList.get(j);
                    newAppSvcCgoDtoE.setName(newAppSvcCgoDto.getName());
                    newAppSvcCgoDtoE.setDesignation(newAppSvcCgoDto.getDesignation());
//                    newAppSvcCgoDtoE.setProfessionType(newAppSvcCgoDto.getProfessionType());
//                    newAppSvcCgoDtoE.setProfRegNo(newAppSvcCgoDto.getProfRegNo());
//                    newAppSvcCgoDtoE.setSpeciality(newAppSvcCgoDto.getSpeciality());
//                    newAppSvcCgoDtoE.setSpecialityOther(newAppSvcCgoDto.getSpecialityOther());
//                    newAppSvcCgoDtoE.setSubSpeciality(newAppSvcCgoDto.getSubSpeciality());
                    newAppSvcCgoDtoE.setMobileNo(newAppSvcCgoDto.getMobileNo());
                    newAppSvcCgoDtoE.setEmailAddr(newAppSvcCgoDto.getEmailAddr());

                    oldAppSvcCgoDtoE.setName(oldAppSvcCgoDto.getName());
                    oldAppSvcCgoDtoE.setDesignation(oldAppSvcCgoDto.getDesignation());
//                    oldAppSvcCgoDtoE.setProfessionType(oldAppSvcCgoDto.getProfessionType());
//                    oldAppSvcCgoDtoE.setProfRegNo(oldAppSvcCgoDto.getProfRegNo());
//                    oldAppSvcCgoDtoE.setSpeciality(oldAppSvcCgoDto.getSpeciality());
//                    oldAppSvcCgoDtoE.setSpecialityOther(oldAppSvcCgoDto.getSpecialityOther());
//                    oldAppSvcCgoDtoE.setSubSpeciality(oldAppSvcCgoDto.getSubSpeciality());
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
                    newMatE.setDescription(newMatDto.getDescription());
                    oldMatE.setName(oldMatDto.getName());
                    oldMatE.setMobileNo(oldMatDto.getMobileNo());
                    oldMatE.setEmailAddr(oldMatDto.getEmailAddr());
                    oldMatE.setDescription(oldMatDto.getDescription());
                    if (!newMatE.equals(oldMatE) && idNo.equals(idNo1)) {
                        idNos.add(idNo);
                    }
                }
            }
        }
        return idNos;
    }
}
