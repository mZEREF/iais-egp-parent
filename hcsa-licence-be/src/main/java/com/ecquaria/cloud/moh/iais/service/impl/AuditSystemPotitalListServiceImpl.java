package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
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
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditSystemPotentialDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.AuditSystemPotitalListService;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskSupportBeService;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.esotericsoftware.minlog.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Autowired
    ApplicationViewService applicationViewService;
    @Override
    public List<AuditTaskDataFillterDto> getSystemPotentailAdultList() {
        AuditSystemPotentialDto dto = new AuditSystemPotentialDto();
        dto.setIsTcuNeeded(1);
        SearchParam searchParam = getSearchParamFrom(dto, null);
        SearchResult<AuditTaskDataDto> searchResult = getAuditSysParam(searchParam);
        if(searchResult != null && searchResult.getRows() != null){
            List<AuditTaskDataDto> auditTaskDataDtos = searchResult.getRows();
            List<AuditTaskDataFillterDto> auditTaskDataFillterDtos = new ArrayList<>(auditTaskDataDtos.size());
            for(AuditTaskDataDto auditTaskDataDto : auditTaskDataDtos){
                auditTaskDataFillterDtos.add(getAuditTaskDataFillterDto(auditTaskDataDto,Boolean.FALSE,Boolean.FALSE));
            }
            return removeDuplicates(auditTaskDataFillterDtos);
        }
        return  null;
    }

    @Override
    public List<AuditTaskDataFillterDto> getSystemPotentailAdultCancelList() {
        SearchParam searchParam = getAduitCancelSearchParamFrom();
        SearchResult<AuditTaskDataDto> searchResult = getAuditSysParam(searchParam);
        if(searchResult != null && searchResult.getRows() != null){
            List<AuditTaskDataDto> auditTaskDataDtos = searchResult.getRows();
            List<AuditTaskDataFillterDto> auditTaskDataFillterDtos = new ArrayList<>(auditTaskDataDtos.size());
            for(AuditTaskDataDto auditTaskDataDto : auditTaskDataDtos){
                auditTaskDataFillterDtos.add(getAuditTaskDataFillterDto(auditTaskDataDto,Boolean.TRUE,Boolean.TRUE));
            }
            return removeDuplicates(auditTaskDataFillterDtos);
        }
        return  null;
    }

    public  AuditTaskDataFillterDto getAuditTaskDataFillterDto(AuditTaskDataDto auditTaskDataDto,Boolean isCancelTask,Boolean needCancelReason){
     return getAuditTaskDataFillterDto(auditTaskDataDto,isCancelTask,needCancelReason,Boolean.TRUE);
    }
    public  AuditTaskDataFillterDto getAuditTaskDataFillterDto(AuditTaskDataDto auditTaskDataDto,Boolean isCancelTask,Boolean needCancelReason,Boolean isNeedInsp){
        AuditTaskDataFillterDto auditTaskDataFillterDto = MiscUtil.transferEntityDto(auditTaskDataDto,AuditTaskDataFillterDto.class);
        if(!isCancelTask)
            auditTaskDataFillterDto.setIsTcuNeeded(1);
        if(needCancelReason){
            auditTaskDataFillterDto.setCancelReason(auditTaskDataDto.getRemark());
            auditTaskDataFillterDto.setInsGrpId(auditTaskDataDto.getInsGrpId());
            auditTaskDataFillterDto.setLicPremGrpCorId(auditTaskDataDto.getLicPremGrpCorId());
        }
        auditTaskDataFillterDto.setAuditType(auditTaskDataDto.getAuditType());
        auditTaskDataFillterDto.setInspectorId(auditTaskDataDto.getInspectorId());
        auditTaskDataFillterDto.setAddress(auditTaskDataDto.getAddress());
        auditTaskDataFillterDto.setTcuDate(auditTaskDataDto.getTcuDate());
        auditTaskDataFillterDto.setId(auditTaskDataDto.getId());
        auditTaskDataFillterDto.setHclCode(auditTaskDataDto.getHclCode());
        auditTaskDataFillterDto.setHclName(auditTaskDataDto.getHclName());
        auditTaskDataFillterDto.setPostalCode(auditTaskDataDto.getPostalCode());
        auditTaskDataFillterDto.setSvcName(auditTaskDataDto.getSvcName());
        auditTaskDataFillterDto.setPremisesType(auditTaskDataDto.getPremisesType());
        auditTaskDataFillterDto.setAuditId(auditTaskDataDto.getAuditId());
        auditTaskDataFillterDto.setLicId(auditTaskDataDto.getLicId());
        auditTaskDataFillterDto.setAuditRiskType(auditTaskDataDto.getAuditRiskType());
        if(isNeedInsp &&!StringUtil.isEmpty(auditTaskDataFillterDto.getInspectorId()) && !StringUtil.isEmpty(auditTaskDataFillterDto.getAuditId())){
            OrgUserDto user = applicationViewService.getUserById(auditTaskDataFillterDto.getInspectorId());
            auditTaskDataFillterDto.setInspector(user.getDisplayName());
            auditTaskDataFillterDto.setAudited(Boolean.TRUE);
        }
        return auditTaskDataFillterDto;
    }

    @Override
    public List<AuditTaskDataFillterDto> getSystemPotentailAdultList(AuditSystemPotentialDto dto) {
        List<AuditTaskDataFillterDto> rdtoList = null;
        if (dto != null) {
            List<String> svcNameList = getSvcName(dto.getSvcNameList(), dto.getHcsaServiceCodeList());
            if (!IaisCommonUtils.isEmpty(svcNameList)) {
                dto.setTotalServiceNameList(svcNameList);
            }
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < svcNameList.size(); i++) {
                sb.append(":svcName")
                        .append(i)
                        .append(',');
            }
            String inSql = sb.substring(0, sb.length() - 1) + ")";
            SearchParam searchParam = getSearchParamFrom(dto, inSql);
            SearchResult<AuditTaskDataDto> searchResult = getAuditSysParam(searchParam);
            rdtoList = inspectionFitter(searchResult, dto);
        }
        return rdtoList;
    }


    private List<AuditTaskDataFillterDto> inspectionFitter(SearchResult<AuditTaskDataDto> searchResult, AuditSystemPotentialDto dto) {
        List<AuditTaskDataDto> auditTaskDtos = searchResult.getRows();
        if(IaisCommonUtils.isEmpty(auditTaskDtos)) return  null;
        Map<String,AuditTaskDataFillterDto> auditTaskDataFillterDtoMap = new HashMap<>(auditTaskDtos.size());
        if (StringUtil.isEmpty(dto.getLastInspectionStart()) && StringUtil.isEmpty(dto.getLastInspectionEnd())) {
        } else {
            auditTaskDtos = new ArrayList<>(auditTaskDtos.size());
            for (AuditTaskDataDto temp : searchResult.getRows()) {
                String licId = temp.getLicId();
                Date startDate = getInspectionStartDate(licId,  Boolean.TRUE);
                Date endDate = getInspectionStartDate(licId,  Boolean.FALSE);
                getAduitTaskDtoByInspecitonDate(endDate, startDate, dto, auditTaskDtos, temp);
                AuditTaskDataFillterDto auditTaskDataFillterDto = getAuditTaskDataFillterDto(temp,Boolean.TRUE,Boolean.FALSE,Boolean.FALSE);
                auditTaskDataFillterDto.setLastInspEnd(Formatter.formatDate(endDate));
                auditTaskDataFillterDto.setLastInspStart(Formatter.formatDate(startDate));
                auditTaskDataFillterDtoMap.put(temp.getId(),auditTaskDataFillterDto);
            }
        }
        Map<String,String> map = getAllServiceByAuditTaskDataFillterDtoList(auditTaskDtos);
        List<AuditTaskDataFillterDto> dtoList = getRiskFillter(dto, auditTaskDtos,map);
        List<AuditTaskDataFillterDto> ncFdtoList = getNcFitter(dtoList, dto);
        //get Last inspDate info
        getLastInspDateInfo(ncFdtoList, dto,map, auditTaskDataFillterDtoMap);
        return removeDuplicates(ncFdtoList);
    }

    private List<AuditTaskDataFillterDto> removeDuplicates(List<AuditTaskDataFillterDto> ncFdtoList){
        if(ncFdtoList != null){
            List<AuditTaskDataFillterDto> list = new ArrayList<>(ncFdtoList.size());
            for(AuditTaskDataFillterDto auditTaskDataFillterDto : ncFdtoList){
                boolean exist = Boolean.FALSE;
                for(AuditTaskDataFillterDto taskDataFillterDto :  list){
                    if(auditTaskDataFillterDto.getLicId().equalsIgnoreCase(taskDataFillterDto.getLicId())
                            && auditTaskDataFillterDto.getId().equalsIgnoreCase(taskDataFillterDto.getId())){
                        exist = Boolean.TRUE;
                        break;
                    }
                }
                if(!exist)
                    list.add(auditTaskDataFillterDto);
            }
            return list;
        }
        return ncFdtoList;
    }

    private List<AuditTaskDataFillterDto> getNcFitter(List<AuditTaskDataFillterDto> dtoList, AuditSystemPotentialDto dto) {
        List<AuditTaskDataFillterDto> fitterDtoList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(dtoList)) {
            for (AuditTaskDataFillterDto temp : dtoList) {
                setLastCompliance(temp);
                if (!StringUtil.isEmpty(dto.getResultLastCompliance())) {
                    if (doNcFitter(temp, dto.getResultLastCompliance())) {
                        fitterDtoList.add(temp);
                    }
                } else {
                    fitterDtoList.add(temp);
                }
            }
        }
        return fitterDtoList;
    }

    private boolean doNcFitter(AuditTaskDataFillterDto temp, String resultLastCompliance) {
        if (HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_FULL_CODE.equals(resultLastCompliance) && HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_FULL_NAME.equalsIgnoreCase(temp.getResultComplicance())) {
            return Boolean.TRUE;
        } else if (HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_PARTIAL_CODE.equals(resultLastCompliance) && HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_PARTIAL_NAME.equalsIgnoreCase(temp.getResultComplicance())){
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    private  void setLastCompliance(AuditTaskDataFillterDto temp){
        List<LicAppCorrelationDto> licCorrDtoList = hcsaLicenceClient.getLicCorrBylicId(temp.getLicId()).getEntity();
        List<AppPremPreInspectionNcDto> ncDtoList = IaisCommonUtils.genNewArrayList();
        if (licCorrDtoList != null && !licCorrDtoList.isEmpty()) {
            for (LicAppCorrelationDto licAppCorr : licCorrDtoList) {
                String appId = licAppCorr.getApplicationId();
                List<AppPremisesCorrelationDto> appPremCorrList = fillUpCheckListGetAppClient.getAppPremiseseCorrDto(appId).getEntity();
                if (appPremCorrList != null && !appPremCorrList.isEmpty()) {
                    for (AppPremisesCorrelationDto appprem : appPremCorrList) {
                        AppPremPreInspectionNcDto ncDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appprem.getId()).getEntity();
                        if(ncDto != null){
                            ncDtoList.add(ncDto);
                            break;
                        }
                    }
                }
            }
        }
        if(IaisCommonUtils.isEmpty(ncDtoList)){
            temp.setResultComplicance( HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_FULL_NAME);
        }else {
            temp.setResultComplicance(HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_PARTIAL_NAME);
        }
    }

    private void getLastInspDateInfo(List<AuditTaskDataFillterDto> dtoList, AuditSystemPotentialDto dto, Map<String,String> map, Map<String,AuditTaskDataFillterDto> auditTaskDataFillterDtoMap) {
        if (!IaisCommonUtils.isEmpty(dtoList)) {
            for (AuditTaskDataFillterDto temp : dtoList) {
                AuditTaskDataFillterDto auditTaskDataFillterDto = auditTaskDataFillterDtoMap.get(temp.getId());
                if( auditTaskDataFillterDto == null){
                    String licId = temp.getLicId();
                    Date startDate = getInspectionStartDate(licId, Boolean.TRUE);
                    Date endDate = getInspectionStartDate(licId, Boolean.FALSE);
                    temp.setLastInspEnd(Formatter.formatDate(endDate));
                    temp.setLastInspStart(Formatter.formatDate(startDate));
                }else {
                    temp.setLastInspStart(auditTaskDataFillterDto.getLastInspStart());
                    temp.setLastInspEnd(auditTaskDataFillterDto.getLastInspEnd());
                }
               if(!StringUtil.isEmpty(dto.getTypeOfRisk())){
                   temp.setRiskType(dto.getTypeOfRisk());
               }
                temp.setSvcCode(map.get(temp.getSvcName()));
            }
        }
    }

    private Map<String,String> getAllServiceByAuditTaskDataFillterDtoList( List<AuditTaskDataDto> auditTaskDtos ){
        Map<String,String> map = new HashMap<>(auditTaskDtos.size());
        if( IaisCommonUtils.isEmpty(auditTaskDtos)) return map;
        for(AuditTaskDataDto temp : auditTaskDtos ){
            if(StringUtil.isEmpty(map.get(temp.getSvcName()))){
                String svcCode = hcsaConfigClient.getServiceCodeByName(temp.getSvcName()).getEntity();
                map.put(temp.getSvcName(),svcCode);
            }
        }
        return map;
    }
    private List<AuditTaskDataDto> transferBackToDataDtoList(List<AuditTaskDataFillterDto> dtoList) {
        List<AuditTaskDataDto> dataDtoList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(dtoList)) {
            for (AuditTaskDataFillterDto temp : dtoList) {
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

    private List<AuditTaskDataFillterDto> getRiskFillter(AuditSystemPotentialDto dto, List<AuditTaskDataDto> auditTaskDtos,Map<String,String> map) {
        List<AuditTaskDataFillterDto> dtoList = null;
        if (!StringUtil.isEmpty(dto.getTypeOfRisk())) {
            if (ApplicationConsts.RISK_TYPE_LAST_INSPECTION_KEY.equals(dto.getTypeOfRisk())) {
                dtoList = sortByLastInsp(dto, auditTaskDtos,map);
            } else if (ApplicationConsts.RISK_TYPE_SECOND_INSPECTION_KEY.equals(dto.getTypeOfRisk())) {
                dtoList = sortBySecLastInsp(dto, auditTaskDtos,map);
            } else if (ApplicationConsts.RISK_TYPE_FINANCIAL_KEY.equals(dto.getTypeOfRisk())) {
                dtoList = sortByScore(dto, auditTaskDtos, HcsaLicenceBeConstant.RISK_TYPE_FINANCIAL,map);
            } else if (ApplicationConsts.RISK_TYPE_LEADERSHIP_KEY.equals(dto.getTypeOfRisk())) {
                dtoList = sortByScore(dto, auditTaskDtos, HcsaLicenceBeConstant.RISK_TYPE_LEADERSHIP,map);
            } else if (ApplicationConsts.RISK_TYPE_LEGISLATIVE_BREACHES_KEY.equals(dto.getTypeOfRisk())) {
                dtoList = sortByScore(dto, auditTaskDtos, HcsaLicenceBeConstant.RISK_TYPE_LEGISLATIVE_BREACHES,map);
            } else if (ApplicationConsts.RISK_TYPE_OVERALL_KEY.equals(dto.getTypeOfRisk())) {
                //sortByRiskScore();
                dtoList = sortByOverallRisk(dto, auditTaskDtos,map);
            }
        } else {
            dtoList = IaisCommonUtils.genNewArrayList();
            for (AuditTaskDataDto temp : auditTaskDtos) {
                AuditTaskDataFillterDto fDto = transferDtoToFiltterDto(temp, 0d, map);
                dtoList.add(fDto);
            }
        }
        dtoList = getNumberList(dto, dtoList);
        return dtoList;
    }

    private List<AuditTaskDataFillterDto> getNumberList(AuditSystemPotentialDto dto, List<AuditTaskDataFillterDto> dtoList) {
        List<AuditTaskDataFillterDto> reuturnList = IaisCommonUtils.genNewArrayList();
        if (dto.getGenerateNum() !=  null && dto.getGenerateNum() > 0) {
            int num = dto.getGenerateNum();
            if (!IaisCommonUtils.isEmpty(dtoList)) {
                if (num < dtoList.size()) {
                    for (int i = 0; i < num; i++) {
                        reuturnList.add(dtoList.get(i));
                    }
                    return reuturnList;
                }
            }
        }
        return dtoList;
    }

    private List<AuditTaskDataFillterDto> sortByOverallRisk(AuditSystemPotentialDto dto, List<AuditTaskDataDto> auditTaskDtos,Map<String,String> map) {
        List<AuditTaskDataFillterDto> fillterDtos = IaisCommonUtils.genNewArrayList();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(auditTaskDtos)) {
            for (AuditTaskDataDto temp : auditTaskDtos) {
                RiskAcceptiionDto acceptiiondto = new RiskAcceptiionDto();
                String svcCode = map.get(temp.getSvcName());
                acceptiiondto.setScvCode(svcCode);
                acceptiiondto.setLicenceId(temp.getLicId());
                riskAcceptiionDtoList.add(acceptiiondto);
            }
            List<RiskResultDto> resultDtos = hcsaRiskSupportBeService.getRiskResult(riskAcceptiionDtoList);
            if (!IaisCommonUtils.isEmpty(resultDtos)) {
                int i = 0 ;
                for (AuditTaskDataDto temp : auditTaskDtos) {
                    Double score = resultDtos.get(i).getScore();
                    AuditTaskDataFillterDto fdto = transferDtoToFiltterDto(temp, score,map);
                    fdto.setRiskType(dto.getTypeOfRisk());
                    fillterDtos.add(fdto);
                    i++;
                }
                fillterDtos.sort((AuditTaskDataFillterDto a1, AuditTaskDataFillterDto a2) -> a1.getScore().compareTo(a2.getScore()));
            }
        }
        return fillterDtos;

    }


    private List<AuditTaskDataFillterDto> sortByScore(AuditSystemPotentialDto dto, List<AuditTaskDataDto> auditTaskDtos, String type,Map<String,String> map) {
        List<AuditTaskDataFillterDto> fillterDtos = IaisCommonUtils.genNewArrayList();
        List<AuditSystemRiskAccpetDto> acceptDtoList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(auditTaskDtos)) {
            for (AuditTaskDataDto temp : auditTaskDtos) {
                AuditSystemRiskAccpetDto accDto = new AuditSystemRiskAccpetDto();
                accDto.setRiskType(type);
                String svcCode = map.get(temp.getSvcName());
                //todo :hardcode waiting for rim
                accDto.setSource("SOURCE001");
                accDto.setCases(2);
                accDto.setSvcCode(svcCode);
                acceptDtoList.add(accDto);
            }
            List<AuditSystemResultDto> auditSystemResultDtos = hcsaConfigClient.getAuditSystemRiskResult(acceptDtoList).getEntity();
            if (!IaisCommonUtils.isEmpty(auditSystemResultDtos)) {
                for (int i = 0; i < auditSystemResultDtos.size(); i++) {
                    Double score = auditSystemResultDtos.get(i).getScore();
                    AuditTaskDataFillterDto fdto = transferDtoToFiltterDto(auditTaskDtos.get(i), score,map);
                    fdto.setRiskType(dto.getTypeOfRisk());
                    fillterDtos.add(fdto);
                }
                fillterDtos.sort((AuditTaskDataFillterDto a1, AuditTaskDataFillterDto a2) -> a1.getScore().compareTo(a2.getScore()));
            }
        }
        return fillterDtos;
    }

    private List<AuditTaskDataFillterDto> sortBySecLastInsp(AuditSystemPotentialDto dto, List<AuditTaskDataDto> auditTaskDtos,Map<String,String> map) {
        List<AuditTaskDataFillterDto> dtoList = IaisCommonUtils.genNewArrayList();
        Double score = 0d;
        if (!IaisCommonUtils.isEmpty(auditTaskDtos)) {
            for (AuditTaskDataDto temp : auditTaskDtos) {
                String svcCode = map.get(temp.getSvcName());
                HcsaLastInspectionDto inspDto = hcsaRiskSupportBeServiceImpl.getLastSecRiskSocre(temp.getLicId(), svcCode);
                score = inspDto.getSecLastScore();
                AuditTaskDataFillterDto fDto = transferDtoToFiltterDto(temp, score,map);
                fDto.setRiskType(dto.getTypeOfRisk());
                dtoList.add(fDto);
            }
            if (score != null && score.equals(0d)) {
                dtoList.sort((AuditTaskDataFillterDto d1, AuditTaskDataFillterDto d2) -> d1.getScore().compareTo(d2.getScore()));
            }
        }
        return dtoList;
    }

    private List<AuditTaskDataFillterDto> sortByLastInsp(AuditSystemPotentialDto dto, List<AuditTaskDataDto> auditTaskDtos,Map<String,String> map) {
        List<AuditTaskDataFillterDto> dtoList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(auditTaskDtos)) {
            for (AuditTaskDataDto temp : auditTaskDtos) {
                String svcCode = map.get(temp.getSvcName());
                HcsaLastInspectionDto inspDto = hcsaRiskSupportBeServiceImpl.getLastSecRiskSocre(temp.getLicId(), svcCode);
                Double score = inspDto.getLastScore();
                AuditTaskDataFillterDto fDto = transferDtoToFiltterDto(temp, score,map);
                fDto.setRiskType(dto.getTypeOfRisk());
                dtoList.add(fDto);
            }
            try {
                dtoList.sort((AuditTaskDataFillterDto d1, AuditTaskDataFillterDto d2) -> d1.getScore().compareTo(d2.getScore()));
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }
        return dtoList;
    }

    public AuditTaskDataFillterDto transferDtoToFiltterDto(AuditTaskDataDto dto, Double score,Map<String,String> map) {
        AuditTaskDataFillterDto fDto = getAuditTaskDataFillterDto(dto,Boolean.TRUE,Boolean.FALSE);
        if (score != null) {
            fDto.setScore(score);
        }else {
            fDto.setScore(0d);
        }
        fDto.setAddress(dto.getAddress());
        fDto.setId(dto.getId());
        fDto.setLicId(dto.getLicId());
        fDto.setPostalCode(dto.getPostalCode());
        fDto.setSvcName(dto.getSvcName());
        fDto.setHclCode(dto.getHclCode());
        fDto.setHclName(dto.getHclName());
        fDto.setPremisesType(dto.getPremisesType());
        fDto.setSvcCode(map.get(dto.getSvcName()));
        return fDto;
    }


    private void getAduitTaskDtoByInspecitonDate(Date endDate, Date startDate, AuditSystemPotentialDto dto, List<AuditTaskDataDto> auditTaskDtos, AuditTaskDataDto temp) {
        Date dtostartDate = null;
        Date dtoEndDate = null;
        if (!StringUtil.isEmpty(dto.getLastInspectionStart())) {
            try {
                dtostartDate = Formatter.parseDate(dto.getLastInspectionStart());
            } catch (Exception e) {
                Log.debug(e.toString());
            }
        }
        if (!StringUtil.isEmpty(dto.getLastInspectionEnd())) {
            try {
                dtostartDate = Formatter.parseDate(dto.getLastInspectionEnd());
            } catch (Exception e) {
                Log.debug(e.toString());
            }
        }
        if (dtostartDate != null && dtoEndDate == null) {
            if (startDate != null && dtostartDate.getTime() > startDate.getTime()) {
                auditTaskDtos.add(temp);
            }
        } else if (dtostartDate == null && dtoEndDate != null) {
            if (endDate != null && dtoEndDate.getTime() < endDate.getTime()) {
                auditTaskDtos.add(temp);
            }
        } else if ( dtoEndDate != null) {
            if (startDate != null && endDate != null && dtoEndDate.getTime() < endDate.getTime() && dtostartDate.getTime() > startDate.getTime()) {
                auditTaskDtos.add(temp);
            }
        }
    }

    private Date getInspectionStartDate(String licId, boolean isStartDate) {
        Date inspectionDate = null;
        List<AppPremisesRecommendationDto> appPremisesRecommendationDtoList = IaisCommonUtils.genNewArrayList();
        List<InspectionInfoDto> inspInfoList = new ArrayList<InspectionInfoDto>(10);
        InspectionInfoDto info = new InspectionInfoDto();
        List<LicAppCorrelationDto> licCorrDtoList = hcsaLicenceClient.getLicCorrBylicId(licId).getEntity();
        if (licCorrDtoList != null && !licCorrDtoList.isEmpty()) {
            for (LicAppCorrelationDto licAppCorr : licCorrDtoList) {
                String appId = licAppCorr.getApplicationId();
                List<AppPremisesCorrelationDto> appPremCorrList = fillUpCheckListGetAppClient.getAppPremiseseCorrDto(appId).getEntity();
                if (appPremCorrList != null && !appPremCorrList.isEmpty()) {
                    for (AppPremisesCorrelationDto appprem : appPremCorrList) {
                        AppPremisesRecommendationDto appPremCorrDto = null;
                        if (isStartDate) {
                            appPremCorrDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appprem.getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                        } else {
                            try {
                                appPremCorrDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appprem.getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
                            }catch (Exception e){
                                log.info(StringUtil.changeForLog("------- data error appcId : " + appprem.getId()+ "-------------------------"));
                            }

                        }
                        if (appPremCorrDto != null) {
                            appPremisesRecommendationDtoList.add(appPremCorrDto);
                            info.setAppId(appId);
                            info.setAppPremId(appprem.getId());
                            info.setCreateDate(appPremCorrDto.getRecomInDate() == null ? new Date(): appPremCorrDto.getRecomInDate() );
                            inspInfoList.add(info);
                        }
                    }
                }
            }
            inspectionDate = getInpectionDate(inspInfoList);
        }
        return inspectionDate;
    }
    public Date getInpectionDate(List<InspectionInfoDto> infoList) {//use
        HcsaLastInspectionDto lastInspection = new HcsaLastInspectionDto();
        if (infoList != null && !infoList.isEmpty()) {
            infoList.sort((InspectionInfoDto i1, InspectionInfoDto i2) -> i2.getCreateDate().compareTo(i1.getCreateDate()));
            if (infoList.size() >= 2) {
                lastInspection.setLastInspectionDate(infoList.get(0).getCreateDate());
                lastInspection.setSecondLastInspectionDate(infoList.get(1).getCreateDate());
                lastInspection.setLastInspectionAppremId(infoList.get(0).getAppPremId());
                lastInspection.setSecondLastInspectionAppremId(infoList.get(1).getAppPremId());
            } else if (infoList.size() == 1) {
                lastInspection.setLastInspectionDate(infoList.get(0).getCreateDate());
                lastInspection.setLastInspectionAppremId(infoList.get(0).getAppPremId());
                lastInspection.setSecondLastInspectionDate(null);
            }
        } else {
            return null;
        }
        return lastInspection.getLastInspectionDate();
    }

    private SearchResult<AuditTaskDataDto> getAuditSysParam(SearchParam searchParam) {
        return hcsaLicenceClient.searchSysAduit(searchParam).getEntity();
    }

    public SearchParam getAduitCancelSearchParamFrom() {
        SearchParam searchParam = new SearchParam(AuditTaskDataDto.class.getName());
        QueryHelp.setMainSql("inspectionQuery", "aduitCancelTaskList", searchParam);
        return searchParam;
    }
    public SearchParam getSearchParamFrom(AuditSystemPotentialDto dto, String insql) {
        SearchParam searchParam = new SearchParam(AuditTaskDataDto.class.getName());
        if(dto.getIsTcuNeeded() != null){
            searchParam.addFilter("isTcuNeeded", dto.getIsTcuNeeded(), Boolean.TRUE);
        }
        if (!IaisCommonUtils.isEmpty(dto.getTotalServiceNameList()) && !StringUtil.isEmpty(insql)) {
            searchParam.addParam("serviceNameList", insql);
            for (int i = 0; i < dto.getTotalServiceNameList().size(); i++) {
                searchParam.addFilter("svcName" + i, dto.getTotalServiceNameList().get(i));
            }
        }
        if (!StringUtil.isEmpty(dto.getPostalCode())) {
            searchParam.addFilter("postalCode", dto.getPostalCode(), Boolean.TRUE);
        }
        if (!StringUtil.isEmpty(dto.getHclCode())) {
            searchParam.addFilter("hclCode", dto.getHclCode(), Boolean.TRUE);
        }
        if (!StringUtil.isEmpty(dto.getPremisesType())) {
            searchParam.addFilter("premType", dto.getPremisesType(), Boolean.TRUE);
        }
        QueryHelp.setMainSql("inspectionQuery", "aduitSystemList", searchParam);
        return searchParam;
    }

    private List<String> getSvcName(List<String> svcNameList, List<String> hcsaServiceCodeList) {
        List<String> serviceNameList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(svcNameList)) {
            for (String temp : svcNameList) {
                serviceNameList.add(temp);
            }
        }
        if (!IaisCommonUtils.isEmpty(hcsaServiceCodeList)) {
            for (String svcName : hcsaServiceCodeList) {
                if (!serviceNameList.contains(svcName)) {
                    serviceNameList.add(svcName);
                }
            }
        }
           /* if(!IaisCommonUtils.isEmpty(serviceNameList)&&!IaisCommonUtils.isEmpty(hcsaServiceCodeList)){
                List<HcsaServiceDto> dtos = hcsaConfigClient.getHcsaServiceDtoByCode(hcsaServiceCodeList).getEntity();
                for(HcsaServiceDto temp:dtos){
                    if(!serviceNameList.contains(temp.getSvcName())){
                        serviceNameList.add(temp.getSvcName());
                    }
                }
            }
        }*/
        return serviceNameList;
    }
}
