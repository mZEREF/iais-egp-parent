package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.service.WithdrawalService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {


    @Autowired
    private SystemAdminClient systemAdminClient;

    @Autowired
    private CessationClient cessationClient;

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private AppConfigClient appConfigClient;

    @Override
    public void saveWithdrawn(List<WithdrawnDto> withdrawnDtoList) {
        withdrawnDtoList.stream().forEach(h -> {
            String licenseeId = h.getLicenseeId();
            AppSubmissionDto appSubmissionDto = applicationClient.getAppSubmissionDto(h.getApplicationNo()).getEntity();
            transform(appSubmissionDto,licenseeId);
            AppSubmissionDto newAppSubmissionDto = applicationClient.saveSubmision(appSubmissionDto).getEntity();
            AppSubmissionDto appSubmissionDtoSave = applicationClient.saveApps(newAppSubmissionDto).getEntity();
            List<ApplicationDto> applicationDtoList = appSubmissionDtoSave.getApplicationDtos();
            String appId = applicationDtoList.get(0).getId();
            h.setApplicationId(appId);
        });
        cessationClient.saveWithdrawn(withdrawnDtoList);
    }

    private void transform(AppSubmissionDto appSubmissionDto,String licenseeId){
        AuditTrailDto auditTrailDto = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTERNET);
        String grpNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL).getEntity();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        String serviceId = appSvcRelatedInfoDtoList.get(0).getServiceId();
        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        String svcId = hcsaServiceDto.getId();
        String svcCode = hcsaServiceDto.getSvcCode();
        appSvcRelatedInfoDtoList.get(0).setServiceId(svcId);
        appSvcRelatedInfoDtoList.get(0).setServiceCode(svcCode);
        appSubmissionDto.setAppGrpNo(grpNo);
        appSubmissionDto.setFromBe(false);
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL);
        appSubmissionDto.setAmount(0.0);
        appSubmissionDto.setAuditTrailDto(auditTrailDto);
        appSubmissionDto.setPreInspection(true);
        appSubmissionDto.setRequirement(true);
        appSubmissionDto.setLicenseeId(licenseeId);
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
        setRiskToDto(appSubmissionDto);
    }

    private void setRiskToDto(AppSubmissionDto appSubmissionDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = IaisCommonUtils.genNewArrayList();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setApptype(appSubmissionDto.getAppType());
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDtoList.add(riskAcceptiionDto);
        }
        List<RiskResultDto> riskResultDtoList = appConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            String serviceCode = appSvcRelatedInfoDto.getServiceCode();
            RiskResultDto riskResultDto = getRiskResultDtoByServiceCode(riskResultDtoList, serviceCode);
            if (riskResultDto != null) {
                appSvcRelatedInfoDto.setDoRiskDate(riskResultDto.getDoRiskDate());
                appSvcRelatedInfoDto.setScore(riskResultDto.getScore());
            }
        }
    }

    private RiskResultDto getRiskResultDtoByServiceCode(List<RiskResultDto> riskResultDtoList, String serviceCode) {
        RiskResultDto result = null;
        if (riskResultDtoList == null || StringUtil.isEmpty(serviceCode)) {
            return result;
        }
        for (RiskResultDto riskResultDto : riskResultDtoList) {
            if (serviceCode.equals(riskResultDto.getSvcCode())) {
                result = riskResultDto;
            }
        }
        return result;
    }
}