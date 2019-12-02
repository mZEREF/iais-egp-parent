package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RecommendInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * AppSubmisionServiceImpl
 *
 * @author suocheng
 * @date 11/6/2019
 */
@Service
@Slf4j
public class AppSubmissionServiceImpl implements AppSubmissionService {
    String draftUrl =  RestApiUrlConsts.HCSA_APP + RestApiUrlConsts.HCSA_APP_SUBMISSION_DRAFT;
    String submission = RestApiUrlConsts.HCSA_APP + RestApiUrlConsts.HCSA_APP_SUBMISSION;

    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private AppConfigClient appConfigClient;
    @Autowired
    private SystemAdminClient systemAdminClient;
    @Override
    public AppSubmissionDto submit(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationClient.saveSubmision(appSubmissionDto).getEntity();
    }

    @Override
    public AppSubmissionDto doSaveDraft(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        return  applicationClient.saveDraft(appSubmissionDto).getEntity();
    }

    @Override
    public String getDraftNo(String appType) {
        return   systemAdminClient.draftNumber(appType).getEntity();
    }

    @Override
    public String getGroupNo(String appType) {

        return   systemAdminClient.applicationNumber(appType).getEntity();
    }

    @Override
    public Double getGroupAmount(AppSubmissionDto appSubmissionDto) {
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount start ...."));
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        AppGrpPremisesDto appGrpPremisesDto = appSubmissionDto.getAppGrpPremisesDto();
        List<LicenceFeeQueryDto> linenceFeeQuaryDtos = new ArrayList();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            List<String> premisessTypes =  new ArrayList();
            premisessTypes.add(appGrpPremisesDto.getPremisesType());
            LicenceFeeQueryDto licenceFeeQueryDto = new LicenceFeeQueryDto();
            licenceFeeQueryDto.setServiceCode(appSvcRelatedInfoDto.getServiceCode());
            licenceFeeQueryDto.setPremises(premisessTypes);
            linenceFeeQuaryDtos.add(licenceFeeQueryDto);
        }
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl linenceFeeQuaryDtos.size() is -->:"+linenceFeeQuaryDtos.size()));
        FeeDto entity = appConfigClient.newFee(linenceFeeQuaryDtos).getEntity();
        Double amount = entity.getTotal();
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl amount is -->:"+amount));
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount end ...."));
        return  amount;
    }

    @Override
    public PreOrPostInspectionResultDto judgeIsPreInspection(AppSubmissionDto appSubmissionDto) {
        RecommendInspectionDto recommendInspectionDto = new RecommendInspectionDto();
        List<RiskAcceptiionDto> riskAcceptiionDtos = new ArrayList();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setSvcType(appSvcRelatedInfoDto.getServiceType());
            riskAcceptiionDto.setBaseServiceCodeList(appSvcRelatedInfoDto.getBaseServiceCodeList());
            riskAcceptiionDtos.add(riskAcceptiionDto);
        }

        return     appConfigClient.recommendIsPreInspection(recommendInspectionDto).getEntity();
    }

    @Override
    public void setRiskToDto(AppSubmissionDto appSubmissionDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = new ArrayList();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(riskAcceptiionDto.getScvCode());
            riskAcceptiionDto.setApptype(appSubmissionDto.getAppType());
            riskAcceptiionDtoList.add(riskAcceptiionDto);
        }

        List<RiskResultDto> riskResultDtoList = appConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();

        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            String serviceCode = appSvcRelatedInfoDto.getServiceCode();
            RiskResultDto riskResultDto = getRiskResultDtoByServiceCode(riskResultDtoList,serviceCode);
            if(riskResultDto!= null){
                appSvcRelatedInfoDto.setScore(riskResultDto.getScore());
                appSvcRelatedInfoDto.setDoRiskDate(riskResultDto.getDoRiskDate());
            }
        }
    }
   private RiskResultDto getRiskResultDtoByServiceCode(List<RiskResultDto> riskResultDtoList,String serviceCode){
       RiskResultDto result = null;
       if(riskResultDtoList == null || StringUtil.isEmpty(serviceCode)){
        return result;
       }
       for(RiskResultDto riskResultDto : riskResultDtoList){
           if(serviceCode.equals(riskResultDto.getSvcCode())){
               result = riskResultDto ;
           }
       }
       return result;
   }

}
