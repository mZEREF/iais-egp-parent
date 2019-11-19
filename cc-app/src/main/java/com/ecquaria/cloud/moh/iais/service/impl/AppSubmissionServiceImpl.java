package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RecommendInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public AppSubmissionDto submit(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return RestApiUtil.save(submission,appSubmissionDto,AppSubmissionDto.class);
    }

    @Override
    public AppSubmissionDto doSaveDraft(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return RestApiUtil.save(draftUrl,appSubmissionDto,AppSubmissionDto.class);
    }

    @Override
    public String getDraftNo(String appType) {
        return  RestApiUtil.getByPathParam(RestApiUrlConsts.SUBMISISON_DRAFT_NO, appType, String.class);
    }

    @Override
    public String getGroupNo(String appType) {
        return RestApiUtil.getByPathParam(RestApiUrlConsts.APPLICAITON_NUMBER, appType, String.class);
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
        Double amount = RestApiUtil.getByList(RestApiUrlConsts.NEW_FEE,linenceFeeQuaryDtos,Double.class);
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
        return RestApiUtil.save(RestApiUrlConsts.HCSA_CONFIG_PREORPOSTINSPECTION,recommendInspectionDto,PreOrPostInspectionResultDto.class);
    }
}
