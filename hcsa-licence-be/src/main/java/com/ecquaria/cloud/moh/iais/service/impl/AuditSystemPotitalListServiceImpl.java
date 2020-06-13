package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
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
            auditTaskDataFillterDto.setAudited(true);
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
                AuditFillterDto auditFillterDto =  fillUpCheckListGetAppClient. getAuditTaskDataDtoByAuditTaskDataDto(temp.getLicId()).getEntity();
                AuditTaskDataFillterDto auditTaskDataFillterDto = getAuditTaskDataFillterDto(temp,Boolean.TRUE,Boolean.FALSE);
                auditTaskDataFillterDto.setLastInspEnd(Formatter.formatDate(auditFillterDto.getLastEndDate()));
                auditTaskDataFillterDto.setLastInspStart(Formatter.formatDate(auditFillterDto.getLastStartDate()));
                auditTaskDataFillterDto.setResultComplicance(auditFillterDto.getCompliance());
                auditTaskDataFillterDto.setRiskType(auditFillterDto.getRiskType());
                auditTaskDataFillterDto.setScore(auditFillterDto.getRiskScore());
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
          Map<String,String> map = getAllServiceByAuditTaskDataFillterDtoList(auditTaskDtos);
            for(AuditTaskDataFillterDto auditTaskDataFillterDto : dtoList){
                auditTaskDataFillterDto.setSvcCode(map.get(auditTaskDataFillterDto.getSvcName()));
            }
        return getRiskFillter(dto,  removeDuplicates(dtoList));
    }

    private List<AuditTaskDataFillterDto> removeDuplicates(List<AuditTaskDataFillterDto> ncFdtoList){
        if(ncFdtoList != null){
            List<AuditTaskDataFillterDto> list = new ArrayList<>(ncFdtoList.size());
            for(AuditTaskDataFillterDto auditTaskDataFillterDto : ncFdtoList){
                boolean exist = false;
                for(AuditTaskDataFillterDto taskDataFillterDto :  list){
                    if(auditTaskDataFillterDto.getLicId().equalsIgnoreCase(taskDataFillterDto.getLicId())
                            && auditTaskDataFillterDto.getId().equalsIgnoreCase(taskDataFillterDto.getId())){
                        exist = true;
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
            return true;
        } else if (HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_PARTIAL_CODE.equals(resultLastCompliance) && HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_PARTIAL_NAME.equalsIgnoreCase(temp.getResultComplicance())){
            return true;
        } else {
            return false;
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

    private List<AuditTaskDataFillterDto> getRiskFillter(AuditSystemPotentialDto dto,  List<AuditTaskDataFillterDto> AuditTaskDataFillterDtos) {
        if (!StringUtil.isEmpty(dto.getTypeOfRisk())) {
            AuditTaskDataFillterDtos = sortByScore(AuditTaskDataFillterDtos,dto.getTypeOfRisk());
        }else {
            AuditTaskDataFillterDtos.sort((AuditTaskDataFillterDto d1, AuditTaskDataFillterDto d2) -> d2.getScore().compareTo(d1.getScore()));
        }
        if(dto.getGenerateNum() != null && dto.getGenerateNum()>0)  {
            return  getNumberList(dto,AuditTaskDataFillterDtos);
        }
        return  AuditTaskDataFillterDtos;
    }

    private List<AuditTaskDataFillterDto> getNumberList(AuditSystemPotentialDto dto, List<AuditTaskDataFillterDto> dtoList) {
        List<AuditTaskDataFillterDto> reuturnList = IaisCommonUtils.genNewArrayList();
            int num = dto.getGenerateNum();
            if (!IaisCommonUtils.isEmpty(dtoList)) {
                if (num < dtoList.size()) {
                    for (int i = 0; i < num; i++) {
                        reuturnList.add(dtoList.get(i));
                    }
                    return reuturnList;
                }
            }
        return dtoList;
    }

    private List<AuditTaskDataFillterDto> sortByScore(List< AuditTaskDataFillterDto> auditTaskDtos, String type) {
        List<AuditTaskDataFillterDto> dtoList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(auditTaskDtos)) {
            for (AuditTaskDataFillterDto auditTaskDataFillterDto : auditTaskDtos) {
                if (type.equalsIgnoreCase( auditTaskDataFillterDto.getRiskType())){
                    dtoList.add(auditTaskDataFillterDto);
                }
            }
            if(!IaisCommonUtils.isEmpty(dtoList )){
                dtoList.sort((AuditTaskDataFillterDto d1, AuditTaskDataFillterDto d2) -> d1.getScore().compareTo(d2.getScore()));
            }
            return dtoList;
        }
        return null;
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
        return serviceNameList;
    }
}
