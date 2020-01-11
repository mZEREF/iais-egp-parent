package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaLastInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskGlobalDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskGolbalExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.InspectionInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RecommendInspectionDto;
import com.ecquaria.cloud.moh.iais.service.RiskSupportService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/1/9 11:01
 */
public class RiskSupportServiceImpl implements RiskSupportService {
    @Autowired
    LicenceClient licenceClient;
    @Autowired
    AppConfigClient appConfigClient;
    @Autowired
    ApplicationClient applicationClient;
    public List<LicAppCorrelationDto> getLicDtoByLicId(String licId){
        return licenceClient.getLicCorrBylicId(licId).getEntity();
    }

    @Override
    public List<PreOrPostInspectionResultDto> preOrPostInspection(List<RecommendInspectionDto> recommendInspectionDtoList){
        List<PreOrPostInspectionResultDto> preOrPostInspectionResultDtoList = new ArrayList<>();
        if(recommendInspectionDtoList!=null&&!recommendInspectionDtoList.isEmpty()){
            for(RecommendInspectionDto temp:recommendInspectionDtoList){
                if(temp.isRenew()){
                    preOrPostInspectionResultDtoList.add(renewRecommendInspection(temp));
                }else{
                    preOrPostInspectionResultDtoList.add(newRecommendInspection(temp));
                }
            }
            return preOrPostInspectionResultDtoList;
        }else{
            return Collections.emptyList();
        }
    }

    private PreOrPostInspectionResultDto newRecommendInspection(RecommendInspectionDto temp) {
        PreOrPostInspectionResultDto resultDto = new PreOrPostInspectionResultDto();
        String svcCode = temp.getSvcCode();
        resultDto.setSvcCode(svcCode);
        resultDto.setRequirement(true);
        HcsaRiskGlobalDto riskGlobalDto = appConfigClient.getRiskGolbalRiskMatraixBySvcCode(temp.getSvcCode()).getEntity();
        String golId = riskGlobalDto.getId();
        List<HcsaRiskGolbalExtDto> golExtList = appConfigClient.getRiskGolbalextDtoById(golId).getEntity();
        if(golExtList!=null &&!golExtList.isEmpty()){
            for(HcsaRiskGolbalExtDto golExt:golExtList){
                if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(golExt.getAppType())){
                   if(golExt.isPreInspect()){
                       resultDto.setPreInspection(true);
                   }else{
                       resultDto.setPreInspection(false);
                   }
                   resultDto.setInspectType(golExt.getInspectType());
                }
            }
        }
        return resultDto;
    }

    private PreOrPostInspectionResultDto renewRecommendInspection(RecommendInspectionDto temp) {
        PreOrPostInspectionResultDto resultDto = new PreOrPostInspectionResultDto();
        String licId = temp.getLicId();
        resultDto.setPremiseId(temp.getPremiseId());
        List<AppPremisesRecommendationDto> appPremisesRecommendationDtoList = new ArrayList<>();
        List<InspectionInfoDto> inspInfoList = new ArrayList<InspectionInfoDto>();
        InspectionInfoDto info = new InspectionInfoDto();
        List<LicAppCorrelationDto> licCorrDtoList = getLicDtoByLicId(licId);
        if(licCorrDtoList!=null&&licCorrDtoList.isEmpty()){
            for(LicAppCorrelationDto licAppCorr:licCorrDtoList){
                String appId = licAppCorr.getApplicationId();
                List<AppPremisesCorrelationDto> appPremCorrList = applicationClient.listAppPremisesCorrelation(appId).getEntity();
                if(appPremCorrList!=null &&!appPremCorrList.isEmpty()){
                    for(AppPremisesCorrelationDto appprem:appPremCorrList){
                        AppPremisesRecommendationDto appPremCorrDto = applicationClient.getAppPremRecordByIdAndType(appprem.getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
                        appPremisesRecommendationDtoList.add(appPremCorrDto);
                        info.setAppId(appId);
                        info.setAppPremId(appprem.getId());
                        info.setRiskScore(appprem.getRiskScore());
                        info.setCreateDate(appPremCorrDto.getRecomInDate());
                        inspInfoList.add(info);
                    }
                }
            }
            //last and second last Inspection
            HcsaLastInspectionDto lstInpDto = getLastAndSecLastInpection(inspInfoList);
            resultDto = doRenewCalPostOrPre(lstInpDto,temp);

        }

        return resultDto;
    }

    private PreOrPostInspectionResultDto doRenewCalPostOrPre(HcsaLastInspectionDto lstInpDto,RecommendInspectionDto reco) {
        PreOrPostInspectionResultDto resultDto = doGolbalRiskCal(reco);
        resultDto.setSvcCode(reco.getSvcCode());
        String svcCode = reco.getSvcCode();
        if(resultDto!=null){
            if(resultDto.isPreInspection()){
                doPreInspectionNextSep(resultDto,lstInpDto);
            }else{
                doPostInspectionNextStep(resultDto,lstInpDto);
            }
        }
        return resultDto;
    }

    private void doPostInspectionNextStep(PreOrPostInspectionResultDto resultDto, HcsaLastInspectionDto lstInpDto) {
        //cal risk
    }

    private void doPreInspectionNextSep(PreOrPostInspectionResultDto resultDto, HcsaLastInspectionDto lstInpDto) {
        Date LastInspectionDate = lstInpDto.getLastInspectionDate();
        Calendar c = Calendar.getInstance();
        c.setTime(LastInspectionDate);
        c.add(Calendar.MONTH,-5);
        Date sixBeforeDate = c.getTime();
        if(sixBeforeDate.getTime()<LastInspectionDate.getTime()){
            resultDto.setRequirement(false);
        }else{
            resultDto.setRequirement(true);
        }

    }

    private PreOrPostInspectionResultDto doGolbalRiskCal(RecommendInspectionDto reco) {
        PreOrPostInspectionResultDto resultDto = new PreOrPostInspectionResultDto();
        String svcCode = reco.getSvcCode();
        resultDto.setSvcCode(svcCode);
        resultDto.setRequirement(true);
        HcsaRiskGlobalDto riskGlobalDto = appConfigClient.getRiskGolbalRiskMatraixBySvcCode(reco.getSvcCode()).getEntity();
        String golId = riskGlobalDto.getId();
        List<HcsaRiskGolbalExtDto> golExtList = appConfigClient.getRiskGolbalextDtoById(golId).getEntity();
        if(golExtList!=null &&!golExtList.isEmpty()){
            for(HcsaRiskGolbalExtDto golExt:golExtList){
                if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(golExt.getAppType())){
                    if(golExt.isPreInspect()){
                        resultDto.setPreInspection(true);
                    }else{
                        resultDto.setPreInspection(false);
                    }
                    resultDto.setInspectType(golExt.getInspectType());
                }
            }
        }
        return resultDto;
    }

    private HcsaLastInspectionDto getLastAndSecLastInpection( List<InspectionInfoDto> infoList) {
        if(infoList!=null && !infoList.isEmpty()){
            infoList.sort((InspectionInfoDto i1,InspectionInfoDto i2)->i2.getCreateDate().compareTo(i1.getCreateDate()));
        }
        HcsaLastInspectionDto lastInspection = new HcsaLastInspectionDto();
        if(infoList.size()>=2){
            lastInspection.setLastInspectionDate(infoList.get(0).getCreateDate());
            lastInspection.setSecondLastInspectionDate(infoList.get(1).getCreateDate());
            //todo:cal risk score
        }else if(infoList.size()==1){
            lastInspection.setLastInspectionDate(infoList.get(0).getCreateDate());
            lastInspection.setSecondLastInspectionDate(null);
        }
        return lastInspection;
    }


}
