package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.AutoRenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaLastInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFeSupportDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskGlobalDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskGolbalExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.InspectionInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RecommendInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskSupportBeService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: jiahao
 * @Date: 2020/1/14 16:45
 */
@Slf4j
@Service
public class HcsaRiskSupportBeServiceImpl implements HcsaRiskSupportBeService {
    @Autowired
    HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    HcsaConfigClient hcsaConfigClient;
    @Autowired
    FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Value("${spring.application.name}")
    private String currentApp;
    @Value("${iais.current.domain}")
    private String currentDomain;
    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    public static final Double DEFAULTSCORE = 1.25;
    public List<LicAppCorrelationDto> getLicDtoByLicId(String licId){
        return hcsaLicenceClient.getLicCorrBylicId(licId).getEntity();
    }

    public HcsaLastInspectionDto getLastAndSecLastInpection( List<InspectionInfoDto> infoList) {//use
        HcsaLastInspectionDto lastInspection = new HcsaLastInspectionDto();
        if(infoList!=null && !infoList.isEmpty()){
            try {
                infoList.sort((InspectionInfoDto i1,InspectionInfoDto i2)->i2.getCreateDate().compareTo(i1.getCreateDate()));
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
            if(infoList.size()>=2){
                lastInspection.setLastInspectionDate(infoList.get(0).getCreateDate());
                lastInspection.setSecondLastInspectionDate(infoList.get(1).getCreateDate());
                lastInspection.setLastInspectionAppremId(infoList.get(0).getAppPremId());
                lastInspection.setSecondLastInspectionAppremId(infoList.get(1).getAppPremId());
            }else if(infoList.size()==1){
                lastInspection.setLastInspectionDate(infoList.get(0).getCreateDate());
                lastInspection.setLastInspectionAppremId(infoList.get(0).getAppPremId());
                lastInspection.setSecondLastInspectionDate(null);
            }
        }else{
            return null;
        }
        return lastInspection;
    }
    @Override
    public HcsaLastInspectionDto getLastSecRiskSocre(String licId,String svcCode) {//use
         List<AppPremisesRecommendationDto> appPremisesRecommendationDtoList = IaisCommonUtils.genNewArrayList();
        HcsaLastInspectionDto lstInpDto = null;
        List<InspectionInfoDto> inspInfoList = new ArrayList<InspectionInfoDto>();
        InspectionInfoDto info = new InspectionInfoDto();
        List<LicAppCorrelationDto> licCorrDtoList = getLicDtoByLicId(licId);
        if (licCorrDtoList != null && !licCorrDtoList.isEmpty()) {
            for (LicAppCorrelationDto licAppCorr : licCorrDtoList) {
                String appId = licAppCorr.getApplicationId();
                List<AppPremisesCorrelationDto> appPremCorrList = fillUpCheckListGetAppClient.getAppPremiseseCorrDto(appId).getEntity();
                if (appPremCorrList != null && !appPremCorrList.isEmpty()) {
                    for (AppPremisesCorrelationDto appprem : appPremCorrList) {
                        AppPremisesRecommendationDto appPremCorrDto = null;
                        try {
                            appPremCorrDto =  fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appprem.getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
                        }catch (Exception e){
                            log.error(e.getMessage(),e);
                            }
                        if(appPremCorrDto!=null){
                            appPremisesRecommendationDtoList.add(appPremCorrDto);
                            info.setAppId(appId);
                            info.setAppPremId(appprem.getId());
                            info.setRiskScore(appprem.getRiskScore());
                            info.setCreateDate(appPremCorrDto.getRecomInDate());
                            inspInfoList.add(info);
                        }
                    }
                }
            }
            lstInpDto = getLastAndSecLastInpection(inspInfoList);
            //callApi
            if(lstInpDto!=null){
                lstInpDto.setSvcCode(svcCode);
                lstInpDto = hcsaConfigClient.getLastAndSecRiskScore(lstInpDto).getEntity();
            }else{
                lstInpDto = new HcsaLastInspectionDto();
                lstInpDto.setSvcCode(svcCode);
                lstInpDto.setSecLastScore(1.25);
                lstInpDto.setLastScore(1.25);
            }
        }else{
            lstInpDto = new HcsaLastInspectionDto();
            lstInpDto.setSvcCode(svcCode);
            lstInpDto.setSecLastScore(1.25);
            lstInpDto.setLastScore(1.25);
        }
        return lstInpDto;

    }

    @Override
    public List<PreOrPostInspectionResultDto> preOrPostInspection(List<RecommendInspectionDto> recommendInspectionDtoList){
        List<PreOrPostInspectionResultDto> preOrPostInspectionResultDtoList = IaisCommonUtils.genNewArrayList();
        if(recommendInspectionDtoList!=null&&!recommendInspectionDtoList.isEmpty()){
            for(RecommendInspectionDto temp:recommendInspectionDtoList){
                if(temp.isRenew()){
                    preOrPostInspectionResultDtoList.add(renewRecommendInspection(temp));
                }else{
                    preOrPostInspectionResultDtoList.add(newRecommendInspection(temp));
                }
            }
            doAllCheckPreOrPost(preOrPostInspectionResultDtoList);
            return preOrPostInspectionResultDtoList;
        }else{
            return Collections.emptyList();
        }
    }

    private void doAllCheckPreOrPost(List<PreOrPostInspectionResultDto> preOrPostInspectionResultDtoList) {
        boolean postFlag = true;
        if(!IaisCommonUtils.isEmpty(preOrPostInspectionResultDtoList)){
            for(PreOrPostInspectionResultDto temp:preOrPostInspectionResultDtoList){
                if(!temp.isPreInspection()){
                    postFlag = false;
                }
            }
            for(PreOrPostInspectionResultDto temp:preOrPostInspectionResultDtoList){
                if(!temp.isPreInspection()){
                   temp.setPreInspection(true);
                }
            }
        }
    }

    private PreOrPostInspectionResultDto renewRecommendInspection(RecommendInspectionDto temp) {//use
        PreOrPostInspectionResultDto resultDto = new PreOrPostInspectionResultDto();
        String svcCode = temp.getSvcCode();
        resultDto.setSvcCode(svcCode);
        resultDto.setRequirement(true);
        HcsaRiskGlobalDto riskGlobalDto = hcsaConfigClient.getRiskGolbalRiskMatraixBySvcCode(temp.getSvcCode()).getEntity();
        String golId = riskGlobalDto.getId();
        List<HcsaRiskGolbalExtDto> golExtList = hcsaConfigClient.getRiskGolbalextDtoById(golId).getEntity();
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
        if(resultDto.isPreInspection()){
            HcsaLastInspectionDto dto = getLastSecRiskSocre(temp.getLicId(),temp.getSvcCode());
            doRenewInspectionLogic(dto,svcCode,resultDto);
        }else{
            HcsaLastInspectionDto dto = getLastSecRiskSocre(temp.getLicId(),temp.getSvcCode());
            lastSixMonthlogic(resultDto,dto,svcCode);
        }

        return resultDto;
    }

    public void doRenewInspectionLogic(HcsaLastInspectionDto dto,String svcCode,PreOrPostInspectionResultDto resultDto){//use
        RiskResultDto riskResultDto = getRiskResult(dto,svcCode);
        if(riskResultDto!=null){
            if(riskResultDto.getScore()>1){
                HcsaRiskGlobalDto riskGlobalDto = hcsaConfigClient.getRiskGolbalRiskMatraixBySvcCode(svcCode).getEntity();
                if(RiskConsts.YEAR.equals(riskResultDto.getDateType())){
                    if(riskGlobalDto.getLastInpectTh()>riskResultDto.getTimeCount()){
                        resultDto.setPreInspection(false);
                        resultDto.setRequirement(true);
                    }else{
                        resultDto.setPreInspection(true);
                        resultDto.setRequirement(true);
                    }
                }else{
                    resultDto.setPreInspection(false);
                    resultDto.setRequirement(true);
                }

            }
        }
    }

    private void lastSixMonthlogic(PreOrPostInspectionResultDto resultDto, HcsaLastInspectionDto dto,String svcCode) {//use
        if(dto!=null&&dto.getLastInspectionDate()!=null){
            Date LastInspectionDate = dto.getLastInspectionDate();
            Calendar c = Calendar.getInstance();
            c.setTime(LastInspectionDate);
            c.add(Calendar.MONTH,-5);
            Date sixBeforeDate = c.getTime();
            if(sixBeforeDate.getTime()<LastInspectionDate.getTime()){
                resultDto.setRequirement(false);
                RiskResultDto risksultDto = getRiskResult(dto,svcCode);
                resultDto.setPreInspectionResult(risksultDto);
            }else{
                resultDto.setRequirement(true);
            }
        }else{
            resultDto.setRequirement(true);
            resultDto.setPreInspection(true);
        }
    }

    public RiskResultDto getRiskResult(HcsaLastInspectionDto dto,String svcCode){//use
        List<RiskAcceptiionDto> riskAcceptiionDtoList = IaisCommonUtils.genNewArrayList();
        RiskAcceptiionDto accDto = new RiskAcceptiionDto();
        accDto.setScvCode(svcCode);
        accDto.setLastInspectionScore(dto.getLastScore());
        accDto.setSecondLastInspectionScore(dto.getSecLastScore());
        accDto.setApptype(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
        riskAcceptiionDtoList.add(accDto);
        List<RiskResultDto> riskResult = hcsaConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();
        if(!IaisCommonUtils.isEmpty(riskResult)){
            return riskResult.get(0);
        }
        return null;
    }

    private PreOrPostInspectionResultDto newRecommendInspection(RecommendInspectionDto temp) {//use
        PreOrPostInspectionResultDto resultDto = new PreOrPostInspectionResultDto();
        String svcCode = temp.getSvcCode();
        resultDto.setSvcCode(svcCode);
        resultDto.setRequirement(true);
        HcsaRiskGlobalDto riskGlobalDto = hcsaConfigClient.getRiskGolbalRiskMatraixBySvcCode(temp.getSvcCode()).getEntity();
        String golId = riskGlobalDto.getId();
        List<HcsaRiskGolbalExtDto> golExtList = hcsaConfigClient.getRiskGolbalextDtoById(golId).getEntity();
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
    @Override
    public List<RiskResultDto> getRiskResult(List<RiskAcceptiionDto> riskAcceptiionDtoList){
        List<RiskResultDto> riskResult = null;
        if(!IaisCommonUtils.isEmpty(riskAcceptiionDtoList)){
            for(RiskAcceptiionDto temp:riskAcceptiionDtoList){
                if(temp.getLicenceId()!=null){
                    HcsaLastInspectionDto lastInspectionDto = getLastSecRiskSocre(temp.getLicenceId(),temp.getScvCode());
                    temp.setSecondLastInspectionScore(lastInspectionDto.getSecLastScore());
                    temp.setLastInspectionScore(lastInspectionDto.getSecLastScore());
                }else{
                    temp.setLastInspectionScore(DEFAULTSCORE);
                    temp.setSecondLastInspectionScore(DEFAULTSCORE);
                }

            }
            riskResult = hcsaConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();
        }
        return riskResult;
    }

    @Override
    public List<AutoRenewDto> isAutoRenew(List<String> licNo,boolean isRenew) {
        List<LicenceDto> licDtoList = hcsaLicenceClient.getLicDtosByLicNos(licNo).getEntity();
        List<AutoRenewDto> autoRenewDtoList = IaisCommonUtils.genNewArrayList();
        AutoRenewDto dto = null;
        if(IaisCommonUtils.isEmpty(licDtoList)){
            for(LicenceDto temp:licDtoList){
                dto = doCalAuto(temp,isRenew);
                autoRenewDtoList.add(dto);
            }
        }
        return autoRenewDtoList;
    }

    private AutoRenewDto doCalAuto(LicenceDto temp,boolean isRenew) {
        String licId = temp.getId();
        String svcName = temp.getSvcName();
        AutoRenewDto adto = null;
        String svcCode = hcsaConfigClient.getServiceCodeByName(svcName).getEntity();
        List<RecommendInspectionDto> recommendInspectionDtoList = IaisCommonUtils.genNewArrayList();
        RecommendInspectionDto dto = new RecommendInspectionDto();
        dto.setLicId(licId);
        dto.setSvcCode(svcCode);
        dto.setRenewal(isRenew);
        recommendInspectionDtoList.add(dto);
        List<PreOrPostInspectionResultDto> preOrPostList = preOrPostInspection(recommendInspectionDtoList);
        PreOrPostInspectionResultDto preOrPostDto = null;
        if(IaisCommonUtils.isEmpty(preOrPostList)){
            preOrPostDto = preOrPostList.get(0);
            if(preOrPostDto.isPreInspection()){
                adto = getAutoDto(svcCode);
                adto.setLicId(licId);
                adto.setRenew(isRenew);
                adto.setLicNo(temp.getLicenceNo());
            }
        }
        return adto;
    }

    private AutoRenewDto getAutoDto(String svcCode) {
        AutoRenewDto autoRenewDto = new AutoRenewDto();
        HcsaRiskGlobalDto riskGlobalDto = hcsaConfigClient.getRiskGolbalRiskMatraixBySvcCode(svcCode).getEntity();
        if(riskGlobalDto.isAutoRenewal()){
            autoRenewDto.setRenew(true);
        }else{
            autoRenewDto.setRenew(false);
        }
        return autoRenewDto;
    }


    @Override
    public void feCreateRiskData(HcsaRiskFeSupportDto supportDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        beEicGatewayClient.feCreateRiskData(supportDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }

    @Override
    public void sysnRiskSaveEic(int httpStatus, HcsaRiskFeSupportDto supportDto) {
        if (httpStatus == HttpStatus.SC_CREATED){
            EicRequestTrackingDto postSaveTrack = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.HCSA_CONFIG, HcsaRiskSupportBeServiceImpl.class.getName(),
                    "feCreateRiskData", currentApp + "-" + currentDomain,
                    HcsaRiskFeSupportDto.class.getName(), JsonUtil.parseToJson(supportDto));
            FeignResponseEntity<EicRequestTrackingDto> fetchResult = eicRequestTrackingHelper.getHcsaConfigClient().getPendingRecordByReferenceNumber(postSaveTrack.getRefNo());
            try{
                if (HttpStatus.SC_OK == fetchResult.getStatusCode()) {
                    EicRequestTrackingDto entity = fetchResult.getEntity();
                    if (AppConsts.EIC_STATUS_PENDING_PROCESSING.equals(entity.getStatus())){
                        feCreateRiskData(supportDto);
                        entity.setProcessNum(1);
                        Date now = new Date();
                        entity.setFirstActionAt(now);
                        entity.setLastActionAt(now);
                        entity.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                        eicRequestTrackingHelper.getHcsaConfigClient().saveEicTrack(entity);
                    }
                }
            }catch (Exception e){
                log.error(StringUtil.changeForLog(e.getMessage()));
            }
        }
    }

    @Override
    public boolean versionSameForRisk(String version,String dbVersion) {
        if((version == null && dbVersion != null) ||(version != null && !version.equals(dbVersion))){
            return false;
        }
        return true;
    }

    @Override
    public boolean versionSameForRisk(Integer version, Integer dbVersion) {
         if((version ==null && dbVersion != null) || !versionSameForRisk(String.valueOf(version),String.valueOf(dbVersion))){
             return false;
         }
         return true;
    }

    @Override
    public List<HcsaServiceDto> getNameSortHcsaServiceDtos() {
        List<HcsaServiceDto> serviceDtoList = hcsaConfigClient.getActiveServices().getEntity();
        if(IaisCommonUtils.isNotEmpty(serviceDtoList) && serviceDtoList.size() >1){
            serviceDtoList.sort(Comparator.comparing(HcsaServiceDto::getSvcName));
        }
        return serviceDtoList;
    }
}

