package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditSystemPotentialDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicInspectionGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremInspGrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.AuditSystemPotitalListService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2020/2/10 13:18
 */
@Slf4j
@Service
public class AuditSystemPotitalListServiceImpl implements AuditSystemPotitalListService {
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private ApplicationViewService applicationViewService;
    @Autowired
    private SystemParamConfig systemParamConfig;
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
        if (HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_FULL_CODE.equals(auditTaskDataDto.getResultComplicance())){
            auditTaskDataDto.setResultComplicance(HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_FULL_NAME);
        }else if(HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_PARTIAL_CODE.equalsIgnoreCase(auditTaskDataDto.getResultComplicance())){
            auditTaskDataDto.setResultComplicance(HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_PARTIAL_NAME );
        }
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
        auditTaskDataFillterDto.setLicenceDueDate(auditTaskDataDto.getLicenceDueDate());
        auditTaskDataFillterDto.setLicenceNo(auditTaskDataDto.getLicenceNo());

        auditTaskDataFillterDto.setSvcCode(auditTaskDataDto.getSvcCode());
        auditTaskDataFillterDto.setScore(auditTaskDataDto.getRiskScore());
        auditTaskDataFillterDto.setRiskType(auditTaskDataDto.getRiskType());
        auditTaskDataFillterDto.setLastInspEnd(Formatter.formatDateTime(auditTaskDataDto.getLastInspEndDate(),"dd/MM/yyyy"));
        auditTaskDataFillterDto.setLastInspStart(Formatter.formatDateTime(auditTaskDataDto.getLastInspStartDate(),"dd/MM/yyyy"));

        if(isNeedInsp &&!StringUtil.isEmpty(auditTaskDataFillterDto.getInspectorId()) && !StringUtil.isEmpty(auditTaskDataFillterDto.getAuditId())){
            OrgUserDto user = applicationViewService.getUserById(auditTaskDataFillterDto.getInspectorId());
            auditTaskDataFillterDto.setInspector(user.getDisplayName());
            Map<String,String> userIdToEmails = new HashMap<>(1);
            userIdToEmails.put(user.getDisplayName(),user.getEmail());
            auditTaskDataFillterDto.setUserIdToEmails(userIdToEmails);
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

    @Override
    public List<AuditTaskDataFillterDto> getCanInactiveAudit() {
        SearchParam searchParam = getCanInactiveSearchParamFrom();
        SearchResult<AuditTaskDataDto> searchResult = getAuditSysParam(searchParam);
        if(searchResult != null && searchResult.getRows() != null){
            List<AuditTaskDataDto> auditTaskDataDtos = searchResult.getRows();
            List<AuditTaskDataFillterDto> auditTaskDataFillterDtos = new ArrayList<>(auditTaskDataDtos.size());
            for(AuditTaskDataDto auditTaskDataDto : auditTaskDataDtos){
                auditTaskDataFillterDtos.add(getAuditTaskDataFillterDto(auditTaskDataDto,Boolean.FALSE,Boolean.TRUE,Boolean.FALSE));
            }
            return removeDuplicates(auditTaskDataFillterDtos);
        }
        return null;
    }

    public SearchParam getCanInactiveSearchParamFrom(){
        SearchParam searchParam = new SearchParam(AuditTaskDataDto.class.getName());
        QueryHelp.setMainSql("inspectionQuery", "aduitChangeStatusList", searchParam);
        return searchParam;
    }

    private List<AuditTaskDataFillterDto> inspectionFitter(SearchResult<AuditTaskDataDto> searchResult, AuditSystemPotentialDto dto) {
        List<AuditTaskDataDto> auditTaskDtos = searchResult.getRows();
        if(IaisCommonUtils.isEmpty(auditTaskDtos)) return  null;
        List<AuditTaskDataFillterDto> dtoList  = new ArrayList<>(auditTaskDtos.size());
        for (AuditTaskDataDto temp : searchResult.getRows()) {
            dtoList.add(getAuditTaskDataFillterDto(temp,Boolean.TRUE,Boolean.FALSE));
        }
        return removeDuplicates(dtoList);
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
            searchParam.addFilter("isTcuNeeded", dto.getIsTcuNeeded(), true);
            searchParam.addFilter("aduitInspectionMonthBeforeTcu",getDayByAduitInspectionMonthBeforeTcu(systemParamConfig.getAuditInspectionMonthBeforeTcu()), true);
        }
        if (!IaisCommonUtils.isEmpty(dto.getTotalServiceNameList()) && !StringUtil.isEmpty(insql)) {
            searchParam.addParam("serviceNameList", insql);
            for (int i = 0; i < dto.getTotalServiceNameList().size(); i++) {
                searchParam.addFilter("svcName" + i, dto.getTotalServiceNameList().get(i));
            }
        }
        if (!StringUtil.isEmpty(dto.getPostalCode())) {
            searchParam.addFilter("postalCode", dto.getPostalCode(), true);
        }
        if (!StringUtil.isEmpty(dto.getHclCode())) {
            searchParam.addFilter("hclCode", dto.getHclCode(), true);
        }
        if (!StringUtil.isEmpty(dto.getPremisesType())) {
            searchParam.addFilter("premType", dto.getPremisesType(), true);
        }
        if (!StringUtil.isEmpty(dto.getTypeOfRisk())){
            searchParam.addFilter("riskType", dto.getTypeOfRisk(), true);
        }

        if(dto.getGenerateNum() != null){
            searchParam.addFilter("number_generate", dto.getGenerateNum(), true);
        }

        if(!StringUtil.isEmpty(dto.getLastInspectionStart())){
            try {
                searchParam.addFilter("last_insp_start",Formatter.formatDateTime( Formatter.parseDate(dto.getLastInspectionStart()),"yyyy-MM-dd HH:mm:ss"), true);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }

        }
        if(!StringUtil.isEmpty(dto.getLastInspectionEnd())){
            try {
            searchParam.addFilter("last_insp_end", Formatter.formatDateTime(Formatter.parseDate(dto.getLastInspectionEnd()),"yyyy-MM-dd HH:mm:ss"), true);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }
        if(!StringUtil.isEmpty(dto.getResultLastCompliance())){
            searchParam.addFilter("resultComplicance", dto.getResultLastCompliance(), true);
        }
        QueryHelp.setMainSql("inspectionQuery", "aduitSystemList", searchParam);
        String sql = searchParam.getMainSql();
     if (!StringUtil.isEmpty(dto.getTypeOfRisk())){
      if("RT001".equalsIgnoreCase(dto.getTypeOfRisk())){
          sql=  sql.replace("replaceSql","t8.risk_type_fir_ins_socre");
      }else if("RT002".equalsIgnoreCase(dto.getTypeOfRisk())){
          sql=sql.replace("replaceSql"," t8.risk_type_sec_ins_socre");
      }else if("RT003".equalsIgnoreCase(dto.getTypeOfRisk())){
          sql= sql.replace("replaceSql"," t8.risk_type_financial_score");
      } else if("RT004".equalsIgnoreCase(dto.getTypeOfRisk())){
         sql= sql.replace("replaceSql","t8.risk_type_leadership_score");
      }else if("RT005".equalsIgnoreCase(dto.getTypeOfRisk())){
         sql= sql.replace("replaceSql","t8.risk_type_legislative_breaches_score");
      }else if("RT006".equalsIgnoreCase(dto.getTypeOfRisk())){
        sql=  sql.replace("replaceSql","t8.risk_score");
      }
     }else {
        sql= sql.replace("replaceSql","t8.risk_score");
     }
        searchParam.setMainSql(sql);
        return searchParam;
    }

    private int getDayByAduitInspectionMonthBeforeTcu( int aduitInspectionMonthBeforeTcu){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,aduitInspectionMonthBeforeTcu);
        return IaisEGPHelper.getCompareDate(new Date(), calendar.getTime());
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

    @Override
    public void inActiveAudit(AuditTaskDataFillterDto auditTaskDataFillterDto,AuditTrailDto intranet) {
        LicPremisesAuditDto licPremisesAuditDto = hcsaLicenceClient.getLicPremAuditByGuid(auditTaskDataFillterDto.getAuditId()).getEntity();
        licPremisesAuditDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
        licPremisesAuditDto.setAuditTrailDto(intranet);
        hcsaLicenceClient.createLicPremAudit(licPremisesAuditDto);
        LicInspectionGroupDto licInspectionGroupDto =  new LicInspectionGroupDto();
        licInspectionGroupDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
        licInspectionGroupDto.setAuditTrailDto(intranet);
        if(StringUtil.isEmpty(auditTaskDataFillterDto.getInsGrpId()) || StringUtil.isEmpty(auditTaskDataFillterDto.getLicPremGrpCorId())){
            log.info(StringUtil.changeForLog("----dirty data aduit id is "+ auditTaskDataFillterDto.getAuditId() +"----------"));
            return;
        }
        licInspectionGroupDto.setId(auditTaskDataFillterDto.getInsGrpId());
        hcsaLicenceClient.createLicInspectionGroup(licInspectionGroupDto);
        LicPremInspGrpCorrelationDto licPremInspGrpCorrelationDto = new LicPremInspGrpCorrelationDto();
        licPremInspGrpCorrelationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
        licPremInspGrpCorrelationDto.setInsGrpId(auditTaskDataFillterDto.getInsGrpId());
        licPremInspGrpCorrelationDto.setId(auditTaskDataFillterDto.getLicPremGrpCorId());
        licPremInspGrpCorrelationDto.setLicPremId(auditTaskDataFillterDto.getId());
        licPremInspGrpCorrelationDto.setAuditTrailDto(intranet);
        hcsaLicenceClient.createLicInspectionGroupCorre(licPremInspGrpCorrelationDto);
    }
}
