package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.AuditSystemResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.AuditSystemRiskAccpetDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaLastInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.InspectionInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditSystemPotentialDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.AuditSystemPotitalListService;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskSupportBeService;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.esotericsoftware.minlog.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/2/10 13:18
 */
@Slf4j
@Service
public class AuditSystemPotitalListServiceImpl implements AuditSystemPotitalListService {
    @Autowired
    HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    HcsaConfigClient hcsaConfigClient;
    @Autowired
    HcsaRiskSupportBeService hcsaRiskSupportBeService;
    @Autowired
    FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    HcsaRiskSupportBeServiceImpl hcsaRiskSupportBeServiceImpl;
    @Override
    public List<AuditTaskDataFillterDto> getSystemPotentailAdultList(AuditSystemPotentialDto dto) {
        List<AuditTaskDataDto> dtos = null;
        List<AuditTaskDataFillterDto> rdtoList = null;
        if(dto!=null){
            List<String> svcNameList = getSvcName(dto.getSvcNameList(),dto.getHcsaServiceCodeList());
            if(!IaisCommonUtils.isEmpty(svcNameList)){
                dto.setTotalServiceNameList(svcNameList);
            }
            StringBuilder sb = new StringBuilder("(");
            for(int i = 0; i < svcNameList.size(); i++){
                sb.append(":svcName" + i).append(",");
            }
            String inSql = sb.substring(0, sb.length() - 1) + ")";
            SearchParam searchParam  = getSearchParamFrom(dto,inSql);
            SearchResult<AuditTaskDataDto> searchResult = getAuditSysParam(searchParam);
            rdtoList = inspectionFitter(searchResult,dto);
        }
        return rdtoList;
    }

    private List<AuditTaskDataFillterDto> inspectionFitter(SearchResult<AuditTaskDataDto> searchResult, AuditSystemPotentialDto dto) {
        List<AuditTaskDataDto> auditTaskDtos = new ArrayList<>();
        if(StringUtil.isEmpty(dto.getLastInspectionStart())&&StringUtil.isEmpty(dto.getLastInspectionEnd())) {
            for (AuditTaskDataDto temp : searchResult.getRows()) {
                auditTaskDtos.add(temp);
            }
        }else{
            for (AuditTaskDataDto temp : searchResult.getRows()) {
                String licId = temp.getLicId();
                String svcCode = hcsaConfigClient.getServiceCodeByName(temp.getSvcName()).getEntity();
                Date startDate = getInspectionStartDate(licId, svcCode, true);
                Date endDate = getInspectionStartDate(licId, svcCode, false);
                getAduitTaskDtoByInspecitonDate(endDate, startDate, dto, auditTaskDtos, temp);
            }
        }
        List<AuditTaskDataFillterDto> dtoList = getRiskFillter(dto,auditTaskDtos);
        List<AuditTaskDataFillterDto> ncFdtoList = getNcFitter(dtoList,dto);
        //get Last inspDate info
        getLastInspDateInfo(ncFdtoList,dto);
        return ncFdtoList;
    }

    private List<AuditTaskDataFillterDto> getNcFitter(List<AuditTaskDataFillterDto> dtoList, AuditSystemPotentialDto dto) {
        List<AuditTaskDataFillterDto> fitterDtoList = new ArrayList<>();
        if(!IaisCommonUtils.isEmpty(dtoList)){
            for(AuditTaskDataFillterDto temp:dtoList){
                if(!StringUtil.isEmpty(dto.getResultLastCompliance())){
                    if(doNcFitter(temp,dto.getResultLastCompliance())){
                        fitterDtoList.add(temp);
                    }
                }else{
                    fitterDtoList.add(temp);
                }
            }
        }
        return fitterDtoList;
    }

    private boolean doNcFitter(AuditTaskDataFillterDto temp, String resultLastCompliance) {
        List<LicAppCorrelationDto> licCorrDtoList = hcsaLicenceClient.getLicCorrBylicId(temp.getLicId()).getEntity();
        List<AppPremPreInspectionNcDto> ncDtoList = new ArrayList<>();
        if (licCorrDtoList != null && !licCorrDtoList.isEmpty()) {
            for (LicAppCorrelationDto licAppCorr : licCorrDtoList) {
                String appId = licAppCorr.getApplicationId();
                List<AppPremisesCorrelationDto> appPremCorrList = fillUpCheckListGetAppClient.getAppPremiseseCorrDto(appId).getEntity();
                if (appPremCorrList != null && !appPremCorrList.isEmpty()) {
                    for (AppPremisesCorrelationDto appprem : appPremCorrList) {
                        AppPremisesRecommendationDto appPremCorrDto = null;
                        AppPremPreInspectionNcDto ncDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appprem.getId()).getEntity();
                        ncDtoList.add(ncDto);
                    }
                }
            }
        }
        if("full".equals(resultLastCompliance)&&IaisCommonUtils.isEmpty(ncDtoList)){
            return true;
        } else if("part".equals(resultLastCompliance)&&!IaisCommonUtils.isEmpty(ncDtoList)){
            return true;
        }else{
            return false;
        }
    }

    private void getLastInspDateInfo(List<AuditTaskDataFillterDto> dtoList,AuditSystemPotentialDto dto) {
        if(!IaisCommonUtils.isEmpty(dtoList)){
            for (AuditTaskDataFillterDto temp : dtoList) {
                String licId = temp.getLicId();
                String svcCode = hcsaConfigClient.getServiceCodeByName(temp.getSvcName()).getEntity();
                Date startDate = getInspectionStartDate(licId, svcCode, true);
                Date endDate = getInspectionStartDate(licId, svcCode, false);
                temp.setLastInspEnd(Formatter.formatDate(endDate));
                temp.setLastInspStart(Formatter.formatDate(startDate));
                temp.setRiskType(dto.getTypeOfRisk());
                temp.setSvcCode(svcCode);
            }
        }
    }

    private List<AuditTaskDataDto> transferBackToDataDtoList(List<AuditTaskDataFillterDto> dtoList) {
        List<AuditTaskDataDto> dataDtoList = new ArrayList<>();
        if(!IaisCommonUtils.isEmpty(dtoList)){
            for(AuditTaskDataFillterDto temp:dtoList){
                AuditTaskDataDto dto = new AuditTaskDataDto();
                dto.setAddress(temp.getAddress());
                dto.setId(temp.getId());
                dto.setLicId(temp.getLicId());
                dto.setPostalCode(temp.getPostalCode());
                dto.setSvcName(temp.getSvcName());
                dto.setHclCode(temp.getHclCode());
                dto.setHclName(temp.getHclName());
                dataDtoList.add(dto);
            }
        }
        return dataDtoList;
    }

    private List<AuditTaskDataFillterDto> getRiskFillter( AuditSystemPotentialDto dto, List<AuditTaskDataDto> auditTaskDtos) {
        List<AuditTaskDataFillterDto> dtoList = null;
        if(!StringUtil.isEmpty(dto.getTypeOfRisk())){
            if("lastInsp".equals(dto.getTypeOfRisk())){
                dtoList = sortByLastInsp(dto,auditTaskDtos);
            }else if("secLastInsp".equals(dto.getTypeOfRisk())){
                dtoList = sortBySecLastInsp(dto,auditTaskDtos);
            }else if("finance"          .equals(dto.getTypeOfRisk())){
                dtoList =  sortByScore(dto,auditTaskDtos,"finance");
            }else if("ledership".equals(dto.getTypeOfRisk())){
                dtoList = sortByScore(dto,auditTaskDtos,"leaderShip");
            }else if("leg".equals(dto.getTypeOfRisk())){
                dtoList = sortByScore(dto,auditTaskDtos,"legistive");
            }else if("overall".equals(dto.getTypeOfRisk())){
                //sortByRiskScore();
                dtoList = sortByOverallRisk(dto,auditTaskDtos);
            }
            dtoList = getNumberList(dto,dtoList);
        }else{
            dtoList = new ArrayList<>();
            for(AuditTaskDataDto temp:auditTaskDtos){
                AuditTaskDataFillterDto fDto = transferDtoToFiltterDto(temp,0d);
                dtoList.add(fDto);
            }
        }
        return dtoList;
    }

    private List<AuditTaskDataFillterDto> getNumberList(AuditSystemPotentialDto dto, List<AuditTaskDataFillterDto> dtoList) {
        List<AuditTaskDataFillterDto> reuturnList = new ArrayList<>();
        if(dto.getGenerateNum()>0){
            int num = dto.getGenerateNum();
            if(!IaisCommonUtils.isEmpty(dtoList)){
                if(num<dtoList.size()){
                    for(int i=0;i<num;i++){
                        reuturnList.add(dtoList.get(i));
                    }
                    return reuturnList;
                }
            }
        }
        return dtoList;
    }

    private List<AuditTaskDataFillterDto> sortByOverallRisk(AuditSystemPotentialDto dto, List<AuditTaskDataDto> auditTaskDtos) {
        List<AuditTaskDataFillterDto> fillterDtos = new ArrayList<>();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = new ArrayList<>();
        RiskAcceptiionDto acceptiiondto = new RiskAcceptiionDto();
        if(!IaisCommonUtils.isEmpty(auditTaskDtos)){
            for(AuditTaskDataDto temp:auditTaskDtos){
                String svcCode = hcsaConfigClient.getServiceCodeByName(temp.getSvcName()).getEntity();
                acceptiiondto.setScvCode(svcCode);
                acceptiiondto.setLicenceId(temp.getLicId());
                riskAcceptiionDtoList.add(acceptiiondto);
            }
            List<RiskResultDto> resultDtos = hcsaRiskSupportBeService.getRiskResult(riskAcceptiionDtoList);
            if(!IaisCommonUtils.isEmpty(resultDtos)){
                for(int i=0;i<resultDtos.size();i++){
                    Double score = resultDtos.get(i).getScore();
                    AuditTaskDataFillterDto fdto = transferDtoToFiltterDto(auditTaskDtos.get(i),score);
                    fillterDtos.add(fdto);
                }
                fillterDtos.sort((AuditTaskDataFillterDto a1,AuditTaskDataFillterDto a2)->a1.getScore().compareTo(a2.getScore()));
            }
        }
        return fillterDtos;

    }


    private List<AuditTaskDataFillterDto> sortByScore(AuditSystemPotentialDto dto, List<AuditTaskDataDto> auditTaskDtos,String type) {
        List<AuditTaskDataFillterDto> fillterDtos = new ArrayList<>();
        List<AuditSystemRiskAccpetDto> acceptDtoList = new ArrayList<>();
        AuditTaskDataFillterDto fdto = null;
        if(!IaisCommonUtils.isEmpty(auditTaskDtos)){
            for(AuditTaskDataDto temp:auditTaskDtos){
                AuditSystemRiskAccpetDto accDto = new AuditSystemRiskAccpetDto();
                accDto.setRiskType(type);
                String svcCode = hcsaConfigClient.getServiceCodeByName(temp.getSvcName()).getEntity();
                //todo :hardcode waiting for rim
                accDto.setSource("SOURCE001");
                accDto.setCases(2);
                accDto.setSvcCode(svcCode);
                acceptDtoList.add(accDto);
            }
            List<AuditSystemResultDto> auditSystemResultDtos = hcsaConfigClient.getAuditSystemRiskResult(acceptDtoList).getEntity();
            if(!IaisCommonUtils.isEmpty(auditSystemResultDtos)){
                for(int i=0;i<auditSystemResultDtos.size();i++){
                    Double score = auditSystemResultDtos.get(i).getScore();
                    fdto = transferDtoToFiltterDto(auditTaskDtos.get(i),score);
                    fillterDtos.add(fdto);
                }
                fillterDtos.sort((AuditTaskDataFillterDto a1,AuditTaskDataFillterDto a2)->a1.getScore().compareTo(a2.getScore()));
            }
        }
        return fillterDtos;
    }

    private List<AuditTaskDataFillterDto> sortBySecLastInsp(AuditSystemPotentialDto dto, List<AuditTaskDataDto> auditTaskDtos) {
        List<AuditTaskDataFillterDto> dtoList = new ArrayList<>();
        Double score = 0d;
        if(!IaisCommonUtils.isEmpty(auditTaskDtos)){
            for(AuditTaskDataDto temp:auditTaskDtos){
                String svcCode = hcsaConfigClient.getServiceCodeByName(temp.getSvcName()).getEntity();
                HcsaLastInspectionDto inspDto =hcsaRiskSupportBeServiceImpl.getLastSecRiskSocre(temp.getLicId(),svcCode);
                score = inspDto.getSecLastScore();
                AuditTaskDataFillterDto fDto = transferDtoToFiltterDto(temp,score);
                dtoList.add(fDto);
            }
            if(score!=0d){
                dtoList.sort((AuditTaskDataFillterDto d1,AuditTaskDataFillterDto d2)->d1.getScore().compareTo(d2.getScore()));
            }
        }
        return dtoList;
    }

    private List<AuditTaskDataFillterDto> sortByLastInsp(AuditSystemPotentialDto dto, List<AuditTaskDataDto> auditTaskDtos) {
        List<AuditTaskDataFillterDto> dtoList = new ArrayList<>();
        if(!IaisCommonUtils.isEmpty(auditTaskDtos)){
            for(AuditTaskDataDto temp:auditTaskDtos){
                String svcCode = hcsaConfigClient.getServiceCodeByName(temp.getSvcName()).getEntity();
                HcsaLastInspectionDto inspDto =hcsaRiskSupportBeServiceImpl.getLastSecRiskSocre(temp.getLicId(),svcCode);
                Double score = inspDto.getLastScore();
                AuditTaskDataFillterDto fDto = transferDtoToFiltterDto(temp,score);
                dtoList.add(fDto);
            }
            dtoList.sort((AuditTaskDataFillterDto d1,AuditTaskDataFillterDto d2)->d1.getScore().compareTo(d2.getScore()));
        }
        return dtoList;
    }
    public AuditTaskDataFillterDto transferDtoToFiltterDto(AuditTaskDataDto dto,Double score){
        AuditTaskDataFillterDto fDto = new AuditTaskDataFillterDto();
        if(score!=null){
            fDto.setScore(score);
        }
        fDto.setAddress(dto.getAddress());
        fDto.setId(dto.getId());
        fDto.setLicId(dto.getLicId());
        fDto.setPostalCode(dto.getPostalCode());
        fDto.setSvcName(dto.getSvcName());
        fDto.setHclCode(dto.getHclCode());
        fDto.setHclName(dto.getHclName());
        fDto.setPremisesType(dto.getPremisesType());
        fDto.setSvcCode(hcsaConfigClient.getServiceCodeByName(dto.getSvcName()).getEntity());
        return fDto;
    }


    private void getAduitTaskDtoByInspecitonDate(Date endDate, Date startDate, AuditSystemPotentialDto dto, List<AuditTaskDataDto> auditTaskDtos,AuditTaskDataDto temp) {
        Date dtostartDate = null;
        Date dtoEndDate = null;
        if(!StringUtil.isEmpty(dto.getLastInspectionStart())){
            try {
                dtostartDate = Formatter.parseDate(dto.getLastInspectionStart());
            }catch (Exception e){
                Log.debug(e.toString());
            }
        }else if(!StringUtil.isEmpty(dto.getLastInspectionEnd())){
            try {
                dtostartDate = Formatter.parseDate(dto.getLastInspectionEnd());
            }catch (Exception e){
                Log.debug(e.toString());
            }
        }
        if(dtostartDate!=null&&dtoEndDate==null){
            if(dtostartDate.getTime()>startDate.getTime()){
                auditTaskDtos.add(temp);
            }
        }else if(dtostartDate==null&&dtoEndDate!=null){
            if(dtoEndDate.getTime()<endDate.getTime()){
                auditTaskDtos.add(temp);
            }
        }else if(dtostartDate!=null&&dtoEndDate!=null){
            if(dtoEndDate.getTime()<endDate.getTime()&&dtostartDate.getTime()>startDate.getTime()){
                auditTaskDtos.add(temp);
            }
        }
    }

    private Date getInspectionStartDate(String licId, String svcCode, boolean isStartDate) {
        Date inspectionDate = null;
        List<AppPremisesRecommendationDto> appPremisesRecommendationDtoList = new ArrayList<>();
        List<InspectionInfoDto> inspInfoList = new ArrayList<InspectionInfoDto>();
        InspectionInfoDto info = new InspectionInfoDto();
        List<LicAppCorrelationDto> licCorrDtoList = hcsaLicenceClient.getLicCorrBylicId(licId).getEntity();
        if (licCorrDtoList != null && !licCorrDtoList.isEmpty()) {
            for (LicAppCorrelationDto licAppCorr : licCorrDtoList) {
                String appId = licAppCorr.getApplicationId();
                List<AppPremisesCorrelationDto> appPremCorrList = fillUpCheckListGetAppClient.getAppPremiseseCorrDto(appId).getEntity();
                if (appPremCorrList != null && !appPremCorrList.isEmpty()) {
                    for (AppPremisesCorrelationDto appprem : appPremCorrList) {
                        AppPremisesRecommendationDto appPremCorrDto = null;
                        if(isStartDate){
                            appPremCorrDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appprem.getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                        }else{
                            appPremCorrDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appprem.getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
                        }
                        if(appPremCorrDto!=null){
                            appPremisesRecommendationDtoList.add(appPremCorrDto);
                            info.setAppId(appId);
                            info.setAppPremId(appprem.getId());
                            info.setCreateDate(appPremCorrDto.getRecomInDate());
                            inspInfoList.add(info);
                        }
                    }
                }
            }
            inspectionDate = getInpectionDate(inspInfoList);
        }
        return inspectionDate;
    }
    public Date getInpectionDate( List<InspectionInfoDto> infoList) {//use
        HcsaLastInspectionDto lastInspection = new HcsaLastInspectionDto();
        if(infoList!=null && !infoList.isEmpty()){
            infoList.sort((InspectionInfoDto i1,InspectionInfoDto i2)->i2.getCreateDate().compareTo(i1.getCreateDate()));
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
        return lastInspection.getLastInspectionDate();
    }

    private SearchResult<AuditTaskDataDto> getAuditSysParam(SearchParam searchParam) {
        return hcsaLicenceClient.searchSysAduit(searchParam).getEntity();
    }

    public SearchParam getSearchParamFrom( AuditSystemPotentialDto dto,String insql) {
        SearchParam searchParam = new SearchParam(AuditTaskDataDto.class.getName());
        if(!IaisCommonUtils.isEmpty(dto.getTotalServiceNameList())){
            searchParam.addParam("serviceNameList", insql);
            for(int i = 0;i<dto.getTotalServiceNameList().size();i++){
                searchParam.addFilter("svcName"+i,dto.getTotalServiceNameList().get(i));
            }
        }
        if(!StringUtil.isEmpty(dto.getPostalCode())){
            searchParam.addFilter("postalCode", dto.getPostalCode(), true);
        }
        if(!StringUtil.isEmpty(dto.getHclCode())){
            searchParam.addFilter("hclCode",dto.getHclCode(),true);
        }
        if(!StringUtil.isEmpty(dto.getPremisesType())){
            searchParam.addFilter("premType",dto.getPremisesType(),true);
        }
        QueryHelp.setMainSql("inspectionQuery", "aduitSystemList", searchParam);
        String jsointest = JsonUtil.parseToJson(searchParam);
        return searchParam;
    }

    private List<String> getSvcName(List<String> svcNameList, List<String> hcsaServiceCodeList) {
        List<String> serviceNameList = new ArrayList<>();
        if(!IaisCommonUtils.isEmpty(svcNameList)){
            for(String temp:svcNameList){
                serviceNameList.add(temp);
            }
            if(!IaisCommonUtils.isEmpty(serviceNameList)&&!IaisCommonUtils.isEmpty(hcsaServiceCodeList)){
                List<HcsaServiceDto> dtos = hcsaConfigClient.getHcsaServiceDtoByCode(hcsaServiceCodeList).getEntity();
                for(HcsaServiceDto temp:dtos){
                    if(!serviceNameList.contains(temp.getSvcName())){
                        serviceNameList.add(temp.getSvcName());
                    }
                }
            }
        }
        return serviceNameList;
    }
}
