package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.RoutingStageSearchDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditSystemPotentialDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.AuditSystemPotitalListService;
import com.ecquaria.cloud.moh.iais.service.client.ComSystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
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
    @Autowired
    private ComSystemAdminClient comSystemAdminClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Override
    public AuditSystemPotentialDto initDtoForSearch() {
        AuditSystemPotentialDto dto = new AuditSystemPotentialDto();
        SearchParam searchParam = new SearchParam(AuditTaskDataDto.class.getName());
        dto.setPageNo(1);
        dto.setPageSize(SystemParamUtil.getDefaultPageSize());
        searchParam.setSort("risk_score", SearchParam.DESCENDING);
        dto.setSearchParam(searchParam);
        return dto;
    }

    @Override
    public List<AuditTaskDataFillterDto> getSystemPotentailAdultList() {
        AuditSystemPotentialDto dto = initDtoForSearch();
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
    public List<AuditTaskDataFillterDto> getSystemPotentailAdultListForAuditTcu( AuditSystemPotentialDto dto) {
        dto.setIsTcuNeeded(1);
        SearchParam searchParam = getSearchParamFrom(dto, null);
        SearchResult<AuditTaskDataDto> searchResult = getAuditSysParam(searchParam);
        dto.setSearchResult(searchResult);
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
        List<String> svcCodes = isLimitedCancel();
        if( IaisCommonUtils.isEmpty(svcCodes)){
            return null;
        }
        SearchParam searchParam = getAduitCancelSearchParamFrom(svcCodes);
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
    @Override
    public List<String> isLimitedCancel(){
        String userGuid = IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid();
        List<String> workGrps = comSystemAdminClient.getWorkGrpsByUserId(userGuid).getEntity();
        if(IaisCommonUtils.isEmpty(workGrps)){
            return IaisCommonUtils.genNewArrayList();
        }
        List<String> workGroupsActives = hcsaConfigClient. getWorkGroupIdsByStageId(HcsaConsts.ROUTING_STAGE_INS).getEntity();
        if(IaisCommonUtils.isEmpty(workGroupsActives)){
            return IaisCommonUtils.genNewArrayList();
        }
        List<String> compathyWorkGroups = IaisCommonUtils.genNewArrayList();
         for(String workGrp : workGrps){
            for(String  workGroupsActive: workGroupsActives){
                if(workGrp.equalsIgnoreCase(workGroupsActive) && !compathyWorkGroups.contains( workGroupsActive)){
                    compathyWorkGroups.add(workGroupsActive);
                    break;
                }
            }
         }
         if(!IaisCommonUtils.isEmpty( compathyWorkGroups)){
             RoutingStageSearchDto routingStageSearchDto = new RoutingStageSearchDto();
             routingStageSearchDto.setAppType(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK);
             routingStageSearchDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
             routingStageSearchDto.setSubOrder(2);
             routingStageSearchDto.setWrokGroupIds(compathyWorkGroups);
             List<String> svcIds =  hcsaConfigClient.getSvcIdsByStageIdAndWorkgroupIdsAndAppType(routingStageSearchDto).getEntity();
             if(IaisCommonUtils.isEmpty(svcIds)){
                 return IaisCommonUtils.genNewArrayList();
             }
             return svcIds;
         }
        return IaisCommonUtils.genNewArrayList();
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
            if(user!=null){
                auditTaskDataFillterDto.setInspector(user.getDisplayName());
                Map<String,String> userIdToEmails = new HashMap<>(1);
                userIdToEmails.put(user.getDisplayName(),user.getEmail());
                auditTaskDataFillterDto.setUserIdToEmails(userIdToEmails);
            }
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
        dto.setSearchResult(searchResult);
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

    public SearchParam getAduitCancelSearchParamFrom( List<String> svcCodes) {
        SearchParam searchParam = new SearchParam(AuditTaskDataDto.class.getName());
        StringBuilder sb = new StringBuilder("(");
        for(int i=0;i < svcCodes.size();i++) {
            sb.append(":svcCode")
                    .append(i)
                    .append(',');
         }
        String inSql = sb.substring(0, sb.length() - 1) + ")";
            searchParam.addParam("svcCodes", inSql);
            int i = 0;
            for(String svcCode : svcCodes){
                searchParam.addFilter("svcCode" + i, svcCode);
                i++;
            }
        QueryHelp.setMainSql("inspectionQuery", "aduitCancelTaskList", searchParam);
        return searchParam;
    }
    public SearchParam getSearchParamFrom(AuditSystemPotentialDto dto, String insql) {
        SearchParam searchParam = dto.getSearchParam();
        searchParam.setPageNo(dto.getPageNo());
        searchParam.setPageSize(dto.getPageSize());
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

        /*if(RoleConsts.USER_ROLE_INSPECTIOR.equalsIgnoreCase(dto.getSelectRole())){
            searchParam.addFilter("ins_id", IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid(), true);
        }*/
        if(RoleConsts.USER_ROLE_INSPECTION_LEAD.equalsIgnoreCase(dto.getSelectRole())){
            List<String>  inspectioners = getInspectionorsByUseId(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
            if(IaisCommonUtils.isEmpty( inspectioners)){
                inspectioners.add(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
            }
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i <  inspectioners.size(); i++) {
                sb.append(":ins_id")
                        .append(i)
                        .append(',');
            }
            String inSql = sb.substring(0, sb.length() - 1) + ")";
            searchParam.addParam("ins_id",inSql);
            for (int i = 0; i <  inspectioners.size(); i++) {
                searchParam.addFilter("ins_id" + i, inspectioners.get(i));
            }
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
        String sort = "risk_score";
        if(!StringUtil.isEmpty(dto.getPostalCode())){
           sql = sql.replace("repalcePostalCodeSearch"," '"+dto.getPostalCode()+"%'");
        }
     if (!StringUtil.isEmpty(dto.getTypeOfRisk())){
      if("RT001".equalsIgnoreCase(dto.getTypeOfRisk())){
          sql=  sql.replace("replaceSql","t8.risk_type_fir_ins_socre");
          sort = "fir_ins_socre";
      }else if("RT002".equalsIgnoreCase(dto.getTypeOfRisk())){
          sql=sql.replace("replaceSql"," t8.risk_type_sec_ins_socre");
          sort="sec_ins_socre";
      }else if("RT003".equalsIgnoreCase(dto.getTypeOfRisk())){
          sql= sql.replace("replaceSql"," t8.risk_type_financial_score");
          sort="financial_score";
      } else if("RT004".equalsIgnoreCase(dto.getTypeOfRisk())){
         sql= sql.replace("replaceSql","t8.risk_type_leadership_score");
          sort="leadership_score";
      }else if("RT005".equalsIgnoreCase(dto.getTypeOfRisk())){
         sql= sql.replace("replaceSql","t8.risk_type_legislative_breaches_score");
          sort="legislative_breaches_score";
      }else if("RT006".equalsIgnoreCase(dto.getTypeOfRisk())){
        sql=  sql.replace("replaceSql","t8.risk_score");
          sort="risk_score";
      }
     }else {
        sql= sql.replace("replaceSql","t8.risk_score");
         sort="risk_score";
     }
        searchParam.setSort(sort,SearchParam.DESCENDING);
        searchParam.setMainSql(sql);
        if(!StringUtil.isEmpty(dto.getPostalCode())){
            searchParam.removeFilter("postalCode");
        }
        return searchParam;
    }


    private List<String>  getInspectionorsByUseId(String userId){
        List<UserGroupCorrelationDto> userGroupCorrelationDtos = organizationClient.getUserGroupLeadByUserId(userId).getEntity();
        List<String> inspectioners = IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isNotEmpty(userGroupCorrelationDtos)){
            for(UserGroupCorrelationDto userGroupCorrelationDto : userGroupCorrelationDtos){
                if(RoleConsts.USER_ROLE_INSPECTION_LEAD.equalsIgnoreCase(userGroupCorrelationDto.getUserRoleName())){
                    String workGroupId = userGroupCorrelationDto.getGroupId();
                    List<OrgUserDto> orgUserDtos = organizationClient. getUsersByWorkGroupNameNotAvailable(workGroupId,AppConsts.COMMON_STATUS_ACTIVE).getEntity();
                    for(OrgUserDto orgUserDto : orgUserDtos){
                        if(!inspectioners.contains(orgUserDto.getId())){
                            inspectioners.add(orgUserDto.getId());
                        }
                    }
                }
            }
        }
        return inspectioners;
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
    }
}
