package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.WithOutRenewalDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
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
    public Boolean isChange(List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) throws Exception {
        {
            List<AppSvcRelatedInfoDto> n = (List<AppSvcRelatedInfoDto>) CopyUtil.copyMutableObject(newAppSvcRelatedInfoDtoList);
            List<AppSvcRelatedInfoDto> o = (List<AppSvcRelatedInfoDto>) CopyUtil.copyMutableObject(oldAppSvcRelatedInfoDtoList);
            n.get(0).setScore(null);
            n.get(0).setDoRiskDate(null);
            o.get(0).setScore(null);
            o.get(0).setDoRiskDate(null);
            List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = n.get(0).getAppSvcDisciplineAllocationDtoList();
            List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList1 = o.get(0).getAppSvcDisciplineAllocationDtoList();
            List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = n.get(0).getHcsaServiceStepSchemeDtos();
            String deputyPoFlag = n.get(0).getDeputyPoFlag();
            o.get(0).setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);
            o.get(0).setDeputyPoFlag(deputyPoFlag);
            if (appSvcDisciplineAllocationDtoList != null && appSvcDisciplineAllocationDtoList1 != null) {
                for (AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : appSvcDisciplineAllocationDtoList) {
                    String idNo = appSvcDisciplineAllocationDto.getIdNo();
                    String premiseVal = appSvcDisciplineAllocationDto.getPremiseVal();
                    String chkLstConfId = appSvcDisciplineAllocationDto.getChkLstConfId();
                    String cgoSelName = appSvcDisciplineAllocationDto.getCgoSelName();
                    String chkLstName = appSvcDisciplineAllocationDto.getChkLstName();
                    for (AppSvcDisciplineAllocationDto allocationDto : appSvcDisciplineAllocationDtoList1) {
                        String idNo1 = allocationDto.getIdNo();
                        String premiseVal1 = allocationDto.getPremiseVal();
                        String chkLstConfId1 = allocationDto.getChkLstConfId();
                        try {
                            if (idNo.equals(idNo1) && premiseVal.equals(premiseVal1) && chkLstConfId.equals(chkLstConfId1)) {
                                allocationDto.setCgoSelName(cgoSelName);
                                allocationDto.setChkLstName(chkLstName);
                            }
                        } catch (NullPointerException e) {
                            log.error("error ", e);
                        }
                    }
                }
            }
            List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = n.get(0).getAppSvcPrincipalOfficersDtoList();
            List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtoList = o.get(0).getAppSvcPrincipalOfficersDtoList();
            if (appSvcPrincipalOfficersDtoList != null && oldAppSvcPrincipalOfficersDtoList != null) {
                List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = copyAppSvcPo(appSvcPrincipalOfficersDtoList);
                List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtos = copyAppSvcPo(oldAppSvcPrincipalOfficersDtoList);
                n.get(0).setAppSvcPrincipalOfficersDtoList(appSvcPrincipalOfficersDtos);
                o.get(0).setAppSvcPrincipalOfficersDtoList(oldAppSvcPrincipalOfficersDtos);
            }
            List<AppSvcCgoDto> appSvcCgoDtoList = n.get(0).getAppSvcCgoDtoList();
            List<AppSvcCgoDto> oldAppSvcCgoDtoList = o.get(0).getAppSvcCgoDtoList();
            if (appSvcCgoDtoList != null && oldAppSvcCgoDtoList != null) {
                List<AppSvcCgoDto> appSvcCgoDtos = copyAppSvcCgo(appSvcCgoDtoList);
                List<AppSvcCgoDto> oldAppSvcCgoDtos = copyAppSvcCgo(oldAppSvcCgoDtoList);
                n.get(0).setAppSvcCgoDtoList(appSvcCgoDtos);
                o.get(0).setAppSvcCgoDtoList(oldAppSvcCgoDtos);
            }
            List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = n.get(0).getAppSvcMedAlertPersonList();
            List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList = o.get(0).getAppSvcMedAlertPersonList();
            if (appSvcMedAlertPersonList != null && oldAppSvcMedAlertPersonList != null) {
                List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonDtos = copyAppSvcPo(appSvcMedAlertPersonList);
                List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonDtos = copyAppSvcPo(oldAppSvcMedAlertPersonList);
                n.get(0).setAppSvcMedAlertPersonList(appSvcMedAlertPersonDtos);
                o.get(0).setAppSvcMedAlertPersonList(oldAppSvcMedAlertPersonDtos);
            }
            if (!o.equals(n)) {
                return true;
            }
            return false;
        }
    }

    private List<AppSvcCgoDto> copyAppSvcCgo(List<AppSvcCgoDto> appSvcCgoDtoList) throws Exception {
        List<AppSvcCgoDto> n = (List<AppSvcCgoDto>) CopyUtil.copyMutableObject(appSvcCgoDtoList);
        for (AppSvcCgoDto appSvcCgoDto : n) {
            appSvcCgoDto.setCgoPsn(false);
            appSvcCgoDto.setPoPsn(false);
            appSvcCgoDto.setDpoPsn(false);
            appSvcCgoDto.setMapPsn(false);
            appSvcCgoDto.setLicPerson(false);
            appSvcCgoDto.setSelectDropDown(false);
            appSvcCgoDto.setNeedSpcOptList(false);
            appSvcCgoDto.setPreferredMode(null);
            appSvcCgoDto.setSpcOptList(null);
            appSvcCgoDto.setSpecialityHtml(null);
            appSvcCgoDto.setCgoIndexNo(null);
            appSvcCgoDto.setAssignSelect(null);
        }
        return n;
    }

    private List<AppSvcPrincipalOfficersDto> copyAppSvcPo(List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList) throws Exception {
        List<AppSvcPrincipalOfficersDto> n = (List<AppSvcPrincipalOfficersDto>) CopyUtil.copyMutableObject(appSvcPrincipalOfficersDtoList);
        for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : n) {
            appSvcPrincipalOfficersDto.setCgoPsn(false);
            appSvcPrincipalOfficersDto.setPoPsn(false);
            appSvcPrincipalOfficersDto.setDpoPsn(false);
            appSvcPrincipalOfficersDto.setMapPsn(false);
            appSvcPrincipalOfficersDto.setLicPerson(false);
            appSvcPrincipalOfficersDto.setSelectDropDown(false);
            appSvcPrincipalOfficersDto.setNeedSpcOptList(false);
            appSvcPrincipalOfficersDto.setSpecialityHtml(null);
            appSvcPrincipalOfficersDto.setSpcOptList(null);
            appSvcPrincipalOfficersDto.setCgoIndexNo(null);
            appSvcPrincipalOfficersDto.setAssignSelect(null);
        }
        return n;
    }
}
