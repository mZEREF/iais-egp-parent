package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.AuditSystemResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.AuditSystemRiskAccpetDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaLastInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditFillterDto;
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
        auditTaskDataFillterDto.setLicenseeId(auditTaskDataDto.getLicenseeId());
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
        if( !StringUtil.isEmpty(dto.getTypeOfRisk())){
            if(ApplicationConsts. RISK_TYPE_FINANCIAL_KEY.equalsIgnoreCase(dto.getTypeOfRisk()) ||  ApplicationConsts.RISK_TYPE_LEADERSHIP_KEY.equalsIgnoreCase(dto.getTypeOfRisk()) ||
            ApplicationConsts.RISK_TYPE_LEGISLATIVE_BREACHES_KEY.equalsIgnoreCase(dto.getTypeOfRisk()) ||  ApplicationConsts.RISK_TYPE_OVERALL_KEY.equalsIgnoreCase(dto.getTypeOfRisk())){
                return  null;
            }
        }
        List<AuditTaskDataFillterDto> dtoList  = new ArrayList<>(auditTaskDtos.size());
            Boolean isHaveSAndEndDate = StringUtil.isEmpty(dto.getLastInspectionStart()) && StringUtil.isEmpty(dto.getLastInspectionEnd());
            for (AuditTaskDataDto temp : searchResult.getRows()) {
                AuditFillterDto auditFillterDto =  fillUpCheckListGetAppClient. getAuditTaskDataDtoByAuditTaskDataDto(temp).getEntity();
                AuditTaskDataFillterDto auditTaskDataFillterDto = getAuditTaskDataFillterDto(temp,Boolean.TRUE,Boolean.FALSE,Boolean.FALSE);
                auditTaskDataFillterDto.setLastInspEnd(Formatter.formatDate(auditFillterDto.getLastEndDate()));
                auditTaskDataFillterDto.setLastInspStart(Formatter.formatDate(auditFillterDto.getLastStartDate()));
                auditTaskDataFillterDto.setResultComplicance(auditFillterDto.getCompliance());
                auditTaskDataFillterDto.setRiskType(auditFillterDto.getRiskType());
                if(isHaveSAndEndDate && StringUtil.isEmpty(dto.getResultLastCompliance())){
                    dtoList.add(auditTaskDataFillterDto);
                }else if(isHaveSAndEndDate && !StringUtil.isEmpty(dto.getResultLastCompliance()) && doNcFitter(auditTaskDataFillterDto,dto.getResultLastCompliance())){
                    dtoList.add(auditTaskDataFillterDto);
                } else if(!isHaveSAndEndDate){
                    if(getAduitTaskDtoByInspecitonDate(auditFillterDto.getLastEndDate(), auditFillterDto.getLastStartDate(), dto)){
                        if(StringUtil.isEmpty(dto.getResultLastCompliance())){
                            dtoList.add(auditTaskDataFillterDto);
                        }else if(doNcFitter(auditTaskDataFillterDto,dto.getResultLastCompliance())){
                            dtoList.add(auditTaskDataFillterDto);
                        }
                    }
                }
            }

          if(dto.getGenerateNum() != null && dto.getGenerateNum() >0)  {
              Map<String,String> map = getAllServiceByAuditTaskDataFillterDtoList(auditTaskDtos);
              getRiskFillter(dto, auditTaskDtos,map);
          }

        return removeDuplicates(dtoList);
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




    private boolean doNcFitter(AuditTaskDataFillterDto temp, String resultLastCompliance) {
        if (HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_FULL_CODE.equals(resultLastCompliance) && HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_FULL_NAME.equalsIgnoreCase(temp.getResultComplicance())) {
            return Boolean.TRUE;
        } else if (HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_PARTIAL_CODE.equals(resultLastCompliance) && HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_PARTIAL_NAME.equalsIgnoreCase(temp.getResultComplicance())){
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
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
              // dtoList = sortByScore(dto, auditTaskDtos, HcsaLicenceBeConstant.RISK_TYPE_FINANCIAL,map);
            } else if (ApplicationConsts.RISK_TYPE_LEADERSHIP_KEY.equals(dto.getTypeOfRisk())) {
              //  dtoList = sortByScore(dto, auditTaskDtos, HcsaLicenceBeConstant.RISK_TYPE_LEADERSHIP,map);
            } else if (ApplicationConsts.RISK_TYPE_LEGISLATIVE_BREACHES_KEY.equals(dto.getTypeOfRisk())) {
            //    dtoList = sortByScore(dto, auditTaskDtos, HcsaLicenceBeConstant.RISK_TYPE_LEGISLATIVE_BREACHES,map);
            } else if (ApplicationConsts.RISK_TYPE_OVERALL_KEY.equals(dto.getTypeOfRisk())) {
                //sortByRiskScore();
             //   dtoList = sortByOverallRisk(dto, auditTaskDtos,map);
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


    private Boolean getAduitTaskDtoByInspecitonDate(Date endDate, Date startDate, AuditSystemPotentialDto dto) {
        Date dtostartDate = null;
        Date dtoEndDate = null;
        if (!StringUtil.isEmpty(dto.getLastInspectionStart())) {
            try {
                dtostartDate = Formatter.parseDate(dto.getLastInspectionStart());
            } catch (Exception e) {
                Log.debug(e.toString());
                return Boolean.FALSE;
            }
        }
        if (!StringUtil.isEmpty(dto.getLastInspectionEnd())) {
            try {
                dtostartDate = Formatter.parseDate(dto.getLastInspectionEnd());
            } catch (Exception e) {
                Log.debug(e.toString());
                return Boolean.FALSE;
            }
        }
        if (dtostartDate != null && dtoEndDate == null) {
            if (startDate != null && dtostartDate.getTime() > startDate.getTime()) {
                return Boolean.TRUE;
            }
        } else if (dtostartDate == null && dtoEndDate != null) {
            if (endDate != null && dtoEndDate.getTime() < endDate.getTime()) {
                return Boolean.TRUE;
            }
        } else if ( dtoEndDate != null) {
            if (startDate != null && endDate != null && dtoEndDate.getTime() < endDate.getTime() && dtostartDate.getTime() > startDate.getTime()) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
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
