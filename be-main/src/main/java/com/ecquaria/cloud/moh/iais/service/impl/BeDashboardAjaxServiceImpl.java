package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.BeDashboardConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppStageSlaTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashAppDetailsQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashAssignMeAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashComPoolAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashKpiPoolAjaxQuery;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashRenewAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashReplyAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashWaitApproveAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashWorkTeamAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.BeDashboardAjaxService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.MohHcsaBeDashboardService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryMainClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationMainClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskMainClient;
import com.ecquaria.cloud.moh.iais.service.client.IntraDashboardClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2021/4/19 10:26
 **/
@Service
@Slf4j
public class BeDashboardAjaxServiceImpl implements BeDashboardAjaxService {

    @Autowired
    private MohHcsaBeDashboardService mohHcsaBeDashboardService;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private InspectionMainAssignTaskService inspectionMainAssignTaskService;

    @Autowired
    private ApplicationMainClient applicationMainClient;

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private HcsaConfigMainClient hcsaConfigMainClient;

    @Autowired
    private InspectionTaskMainClient inspectionTaskMainClient;

    @Autowired
    private IntraDashboardClient intraDashboardClient;

    @Autowired
    private OrganizationMainClient organizationMainClient;

    @Autowired
    private AppPremisesRoutingHistoryMainClient appPremisesRoutingHistoryMainClient;

    @Override
    public Map<String, Object> getCommonDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                       String actionValue, String dashFilterAppNo, HcsaTaskAssignDto hcsaTaskAssignDto, String hci_address) {
        SearchParam searchParam = new SearchParam(DashComPoolAjaxQueryDto.class.getName());
        searchParam.setPageNo(1);
        searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(groupNo)){
            searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
            //filter appGroup NO.
            searchParam.addFilter("groupNo", groupNo, true);
            ApplicationGroupDto applicationGroupDto = applicationMainClient.getAppGrpByNo(groupNo).getEntity();
            //filter app Premises Correlation
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationMainClient.getPremCorrDtoByAppGroupId(applicationGroupDto.getId()).getEntity();
            List<String> appCorrId_list = getAppPremCorrIdsByDto(appPremisesCorrelationDtos);
            String appPremCorrId = SqlHelper.constructInCondition("T2.ID", appCorrId_list.size());
            searchParam.addParam("appCorrId_list", appPremCorrId);
            for(int i = 0; i < appCorrId_list.size(); i++){
                searchParam.addFilter("T2.ID" + i, appCorrId_list.get(i));
            }
            //filter appNo
            if(!StringUtil.isEmpty(dashFilterAppNo)){
                searchParam.addFilter("dashFilterAppNo", dashFilterAppNo,true);
            }
            //filter page conditions
            searchParam = filterPageConditions(searchParam, searchParamGroup, null, null, hcsaTaskAssignDto,
                    "T5.APP_PREM_ID", hci_address);
            //filter work groups
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, actionValue, workGroupIds);
            //search
            QueryHelp.setMainSql("intraDashboardQuery", "dashCommonTaskAjax", searchParam);
            SearchResult<DashComPoolAjaxQueryDto> ajaxResult = getCommonAjaxResultByParam(searchParam);
            //set other data
            setComPoolAjaxDataToShow(ajaxResult.getRows(), hcsaTaskAssignDto);
            map.put("result", "Success");
            map.put("ajaxResult", ajaxResult);
        } else {
            searchParam.setPageSize(1);
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, actionValue, workGroupIds);
            QueryHelp.setMainSql("intraDashboardQuery", "dashCommonTaskAjax", searchParam);
            SearchResult<DashComPoolAjaxQueryDto> ajaxResult = getCommonAjaxResultByParam(searchParam);
            map.put("totalNumber", ajaxResult.getRowCount());
        }
        return map;
    }

    @Override
    public Map<String, Object> getKpiDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                    String switchAction, String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto,
                                                    String hci_address) {
        SearchParam searchParam = new SearchParam(DashKpiPoolAjaxQuery.class.getName());
        searchParam.setPageNo(1);
        searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(groupNo)){
            searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
            //filter appGroup NO.
            searchParam.addFilter("groupNo", groupNo, true);
            ApplicationGroupDto applicationGroupDto = applicationMainClient.getAppGrpByNo(groupNo).getEntity();
            //filter app Premises Correlation
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationMainClient.getPremCorrDtoByAppGroupId(applicationGroupDto.getId()).getEntity();
            List<String> appCorrId_list = getAppPremCorrIdsByDto(appPremisesCorrelationDtos);
            String appPremCorrId = SqlHelper.constructInCondition("T7.REF_NO", appCorrId_list.size());
            searchParam.addParam("appCorrId_list", appPremCorrId);
            for(int i = 0; i < appCorrId_list.size(); i++){
                searchParam.addFilter("T7.REF_NO" + i, appCorrId_list.get(i));
            }
            //filter appNo
            if(!StringUtil.isEmpty(dashFilterAppNo)){
                searchParam.addFilter("dashFilterAppNo", dashFilterAppNo,true);
            }
            //filter page conditions
            searchParam = filterPageConditions(searchParam, searchParamGroup, "T1.STATUS", dashAppStatus, hcsaTaskAssignDto,
                    "APP_PREM_ID", hci_address);
            //filter work groups
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            //search
            QueryHelp.setMainSql("intraDashboardQuery", "dashKpiTaskAjax", searchParam);
            SearchResult<DashKpiPoolAjaxQuery> ajaxResult = getKpiAjaxResultByParam(searchParam);
            //set other data
            setKpiPoolAjaxDataToShow(ajaxResult.getRows(), hcsaTaskAssignDto);
            map.put("result", "Success");
            map.put("ajaxResult", ajaxResult);
        } else {
            searchParam.setPageSize(1);
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            QueryHelp.setMainSql("intraDashboardQuery", "dashKpiTaskAjax", searchParam);
            SearchResult<DashKpiPoolAjaxQuery> ajaxResult = getKpiAjaxResultByParam(searchParam);
            map.put("totalNumber", ajaxResult.getRowCount());
        }
        return map;
    }

    @Override
    public Map<String, Object> getAssignMeDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                         String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto, String hci_address) {
        SearchParam searchParam = new SearchParam(DashAssignMeAjaxQueryDto.class.getName());
        searchParam.setPageNo(1);
        searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
        if(!StringUtil.isEmpty(groupNo)){
            searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
            //filter appGroup NO.
            searchParam.addFilter("groupNo", groupNo, true);
            ApplicationGroupDto applicationGroupDto = applicationMainClient.getAppGrpByNo(groupNo).getEntity();
            //filter app Premises Correlation
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationMainClient.getPremCorrDtoByAppGroupId(applicationGroupDto.getId()).getEntity();
            List<String> appCorrId_list = getAppPremCorrIdsByDto(appPremisesCorrelationDtos);
            String appPremCorrId = SqlHelper.constructInCondition("T2.ID", appCorrId_list.size());
            searchParam.addParam("appCorrId_list", appPremCorrId);
            for(int i = 0; i < appCorrId_list.size(); i++){
                searchParam.addFilter("T2.ID" + i, appCorrId_list.get(i));
            }
            //filter appNo
            if(!StringUtil.isEmpty(dashFilterAppNo)){
                searchParam.addFilter("dashFilterAppNo", dashFilterAppNo,true);
            }
            if(loginContext != null) {
                //role
                String curRoleId = loginContext.getCurRoleId();
                if (!StringUtil.isEmpty(curRoleId)) {
                    searchParam.addFilter("dashRoleId", curRoleId, true);
                }
                //user uuid
                String userId = loginContext.getUserId();
                if (!StringUtil.isEmpty(userId)) {
                    searchParam.addFilter("dashUserId", userId, true);
                }
            }
            //filter page conditions
            searchParam = filterPageConditions(searchParam, searchParamGroup, "T5.STATUS", dashAppStatus, hcsaTaskAssignDto,
                    "T5.APP_PREM_ID", hci_address);
            //search
            QueryHelp.setMainSql("intraDashboardQuery", "dashAssignMeAjax", searchParam);
            SearchResult<DashAssignMeAjaxQueryDto> ajaxResult = getAssignMeAjaxResultByParam(searchParam);
            //set other data
            setAssignMeAjaxDataToShow(ajaxResult.getRows(), hcsaTaskAssignDto);
            map.put("result", "Success");
            map.put("ajaxResult", ajaxResult);
        } else {
            searchParam.setPageSize(1);
            QueryHelp.setMainSql("intraDashboardQuery", "dashAssignMeAjax", searchParam);
            SearchResult<DashAssignMeAjaxQueryDto> ajaxResult = getAssignMeAjaxResultByParam(searchParam);
            map.put("totalNumber", ajaxResult.getRowCount());
        }
        return map;
    }

    @Override
    public Map<String, Object> getWorkTeamDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                         String switchAction, String dashFilterAppNo, String dashCommonPoolStatus, String dashAppStatus,
                                                         HcsaTaskAssignDto hcsaTaskAssignDto, String hci_address) {
        SearchParam searchParam = new SearchParam(DashWorkTeamAjaxQueryDto.class.getName());
        searchParam.setPageNo(1);
        searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(groupNo)){
            searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
            //filter appGroup NO.
            searchParam.addFilter("groupNo", groupNo, true);
            ApplicationGroupDto applicationGroupDto = applicationMainClient.getAppGrpByNo(groupNo).getEntity();
            //filter app Premises Correlation
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationMainClient.getPremCorrDtoByAppGroupId(applicationGroupDto.getId()).getEntity();
            List<String> appCorrId_list = getAppPremCorrIdsByDto(appPremisesCorrelationDtos);
            String appPremCorrId = SqlHelper.constructInCondition("T7.REF_NO", appCorrId_list.size());
            searchParam.addParam("appCorrId_list", appPremCorrId);
            for(int i = 0; i < appCorrId_list.size(); i++){
                searchParam.addFilter("T7.REF_NO" + i, appCorrId_list.get(i));
            }
            //filter appNo
            if(!StringUtil.isEmpty(dashFilterAppNo)){
                searchParam.addFilter("dashFilterAppNo", dashFilterAppNo,true);
            }
            //filter Common pool
            if(!StringUtil.isEmpty(dashCommonPoolStatus)){
                searchParam.addFilter("dashCommonPoolStatus", dashCommonPoolStatus,true);
            }
            //filter page conditions
            searchParam = filterPageConditions(searchParam, searchParamGroup, "T5.STATUS", dashAppStatus, hcsaTaskAssignDto,
                    "APP_PREM_ID", hci_address);
            //filter work groups
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            //search
            QueryHelp.setMainSql("intraDashboardQuery", "dashSupervisorAjax", searchParam);
            SearchResult<DashWorkTeamAjaxQueryDto> ajaxResult = getWorkTeamAjaxResultByParam(searchParam);
            //set other data
            setWorkTeamAjaxDataToShow(ajaxResult.getRows(), hcsaTaskAssignDto);
            map.put("result", "Success");
            map.put("ajaxResult", ajaxResult);
        } else {
            searchParam.setPageSize(1);
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            QueryHelp.setMainSql("intraDashboardQuery", "dashSupervisorAjax", searchParam);
            SearchResult<DashWorkTeamAjaxQueryDto> ajaxResult = getWorkTeamAjaxResultByParam(searchParam);
            map.put("totalNumber", ajaxResult.getRowCount());
        }
        return map;
    }

    @Override
    public Map<String, Object> getRenewDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                      String switchAction, String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto,
                                                      String hci_address) {
        SearchParam searchParam = new SearchParam(DashRenewAjaxQueryDto.class.getName());
        searchParam.setPageNo(1);
        searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        //licence expire days
        if(BeDashboardConstant.SWITCH_ACTION_RE_RENEW.equals(switchAction)) {
            searchParam.addFilter("lic_renew_exp", systemParamConfig.getDashRenewDate(), true);
        }
        if(!StringUtil.isEmpty(groupNo)){
            searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
            //filter appGroup NO.
            searchParam.addFilter("groupNo", groupNo, true);
            ApplicationGroupDto applicationGroupDto = applicationMainClient.getAppGrpByNo(groupNo).getEntity();
            //filter app Premises Correlation
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationMainClient.getPremCorrDtoByAppGroupId(applicationGroupDto.getId()).getEntity();
            List<String> appCorrId_list = getAppPremCorrIdsByDto(appPremisesCorrelationDtos);
            String appPremCorrId = SqlHelper.constructInCondition("T7.REF_NO", appCorrId_list.size());
            searchParam.addParam("appCorrId_list", appPremCorrId);
            for(int i = 0; i < appCorrId_list.size(); i++){
                searchParam.addFilter("T7.REF_NO" + i, appCorrId_list.get(i));
            }
            //filter appNo
            if(!StringUtil.isEmpty(dashFilterAppNo)){
                searchParam.addFilter("dashFilterAppNo", dashFilterAppNo,true);
            }

            //filter page conditions
            searchParam = filterPageConditions(searchParam, searchParamGroup, "T1.STATUS", dashAppStatus, hcsaTaskAssignDto,
                    "APP_PREM_ID", hci_address);
            //filter work groups
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            //search
            QueryHelp.setMainSql("intraDashboardQuery", "dashAppRenewAjax", searchParam);
            SearchResult<DashRenewAjaxQueryDto> ajaxResult = getRenewAjaxResultByParam(searchParam);
            //set other data
            setRenewAjaxDataToShow(ajaxResult.getRows(), hcsaTaskAssignDto);
            map.put("result", "Success");
            map.put("ajaxResult", ajaxResult);
        } else {
            searchParam.setPageSize(1);
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            QueryHelp.setMainSql("intraDashboardQuery", "dashAppRenewAjax", searchParam);
            SearchResult<DashRenewAjaxQueryDto> ajaxResult = getRenewAjaxResultByParam(searchParam);
            map.put("totalNumber", ajaxResult.getRowCount());
        }
        return map;
    }

    @Override
    public Map<String, Object> getReplyDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                      String switchAction, String dashFilterAppNo, HcsaTaskAssignDto hcsaTaskAssignDto, String hci_address) {
        SearchParam searchParam = new SearchParam(DashReplyAjaxQueryDto.class.getName());
        searchParam.setPageNo(1);
        searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(groupNo)){
            searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
            //filter appGroup NO.
            searchParam.addFilter("groupNo", groupNo, true);
            ApplicationGroupDto applicationGroupDto = applicationMainClient.getAppGrpByNo(groupNo).getEntity();
            //filter app Premises Correlation
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationMainClient.getPremCorrDtoByAppGroupId(applicationGroupDto.getId()).getEntity();
            List<String> appCorrId_list = getAppPremCorrIdsByDto(appPremisesCorrelationDtos);
            String appPremCorrId = SqlHelper.constructInCondition("T7.ID", appCorrId_list.size());
            searchParam.addParam("appCorrId_list", appPremCorrId);
            for(int i = 0; i < appCorrId_list.size(); i++){
                searchParam.addFilter("T7.ID" + i, appCorrId_list.get(i));
            }
            //filter appNo
            if(!StringUtil.isEmpty(dashFilterAppNo)){
                searchParam.addFilter("dashFilterAppNo", dashFilterAppNo,true);
            }
            //filter page conditions
            searchParam = filterPageConditions(searchParam, searchParamGroup, null, null, hcsaTaskAssignDto,
                    "APP_PREM_ID", hci_address);
            //filter work groups
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            //search
            QueryHelp.setMainSql("intraDashboardQuery", "dashAppReplyAjax", searchParam);
            SearchResult<DashReplyAjaxQueryDto> ajaxResult = getReplyAjaxResultByParam(searchParam);
            //set other data
            setReplyAjaxDataToShow(ajaxResult.getRows(), hcsaTaskAssignDto);
            map.put("result", "Success");
            map.put("ajaxResult", ajaxResult);
        } else {
            searchParam.setPageSize(1);
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            QueryHelp.setMainSql("intraDashboardQuery", "dashAppReplyAjax", searchParam);
            SearchResult<DashReplyAjaxQueryDto> ajaxResult = getReplyAjaxResultByParam(searchParam);
            map.put("totalNumber", ajaxResult.getRowCount());
        }
        return map;
    }

    @Override
    public Map<String, Object> getWaitApproveDropResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                        String switchAction, String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto,
                                                        String hci_address) {
        SearchParam searchParam = new SearchParam(DashWaitApproveAjaxQueryDto.class.getName());
        int pageSize=SystemParamUtil.getDefaultPageSize();
        searchParam.setPageSize(pageSize);
        searchParam.setPageNo(1);
        searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(groupNo)){
            searchParam.setPageSize(pageSize);
            //filter appGroup NO.
            searchParam.addFilter("groupNo", groupNo, true);
            ApplicationGroupDto applicationGroupDto = applicationMainClient.getAppGrpByNo(groupNo).getEntity();
            //filter app Premises Correlation
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationMainClient.getPremCorrDtoByAppGroupId(applicationGroupDto.getId()).getEntity();
            List<String> appCorrId_list = getAppPremCorrIdsByDto(appPremisesCorrelationDtos);
            String appPremCorrId = SqlHelper.constructInCondition("T1.REF_NO", appCorrId_list.size());
            searchParam.addParam("appCorrId_list", appPremCorrId);
            for(int i = 0; i < appCorrId_list.size(); i++){
                searchParam.addFilter("T1.REF_NO" + i, appCorrId_list.get(i));
            }
            //filter appNo
            if(!StringUtil.isEmpty(dashFilterAppNo)){
                searchParam.addFilter("dashFilterAppNo", dashFilterAppNo,true);
            }
            //filter page conditions
            searchParam = filterPageConditions(searchParam, searchParamGroup, "T7.STATUS", dashAppStatus, hcsaTaskAssignDto,
                    "APP_PREM_ID", hci_address);
            //filter work groups
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            //search
            QueryHelp.setMainSql("intraDashboardQuery", "dashWaitApproveAjax", searchParam);
            SearchResult<DashWaitApproveAjaxQueryDto> ajaxResult = getWaitAjaxResultByParam(searchParam);
            //set other data
            setWaitApproveAjaxDataToShow(ajaxResult.getRows(), hcsaTaskAssignDto);
            map.put("result", "Success");
            map.put("ajaxResult", ajaxResult);
        } else {
            searchParam.setPageSize(1);
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            QueryHelp.setMainSql("intraDashboardQuery", "dashWaitApproveAjax", searchParam);
            SearchResult<DashWaitApproveAjaxQueryDto> ajaxResult = getWaitAjaxResultByParam(searchParam);
            map.put("totalNumber", ajaxResult.getRowCount());
        }
        return map;
    }

    private List<DashWaitApproveAjaxQueryDto> setWaitApproveAjaxDataToShow(List<DashWaitApproveAjaxQueryDto> dashWaitApproveAjaxQueryDtos, HcsaTaskAssignDto hcsaTaskAssignDto) {
        if(!IaisCommonUtils.isEmpty(dashWaitApproveAjaxQueryDtos)){
            for(DashWaitApproveAjaxQueryDto dashWaitApproveAjaxQueryDto : dashWaitApproveAjaxQueryDtos){
                //get hciName / address
                AppGrpPremisesDto appGrpPremisesDto = inspectionMainAssignTaskService.getAppGrpPremisesDtoByAppCorrId(dashWaitApproveAjaxQueryDto.getId());
                String address = inspectionMainAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
                if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                    dashWaitApproveAjaxQueryDto.setHciAddress(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
                } else {
                    dashWaitApproveAjaxQueryDto.setHciAddress(StringUtil.viewHtml(address));
                }
                //app status
                dashWaitApproveAjaxQueryDto.setAppStatusStrShow(MasterCodeUtil.getCodeDesc(dashWaitApproveAjaxQueryDto.getAppStatus()));
                //service
                HcsaServiceDto hcsaServiceDto = hcsaConfigMainClient.getHcsaServiceDtoByServiceId(dashWaitApproveAjaxQueryDto.getServiceId()).getEntity();;
                dashWaitApproveAjaxQueryDto.setServiceName(hcsaServiceDto.getSvcName());
                dashWaitApproveAjaxQueryDto.setHciCode(StringUtil.viewHtml(appGrpPremisesDto.getHciCode()));
                //application
                ApplicationDto applicationDto = applicationMainClient.getAppByNo(dashWaitApproveAjaxQueryDto.getApplicationNo()).getEntity();
                //get license date
                if(StringUtil.isEmpty(applicationDto.getOriginLicenceId())){
                    dashWaitApproveAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                } else {
                    LicenceDto licenceDto = licenceClient.getLicDtoById(applicationDto.getOriginLicenceId()).getEntity();
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    if(licExpiryDate != null) {
                        dashWaitApproveAjaxQueryDto.setLicenceExpiryDate(licExpiryDate);
                        String licExpiryDateStr = Formatter.formatDateTime(licExpiryDate, AppConsts.DEFAULT_DATE_FORMAT);
                        dashWaitApproveAjaxQueryDto.setLicenceExpiryDateStr(licExpiryDateStr);
                    } else {
                        dashWaitApproveAjaxQueryDto.setLicenceExpiryDate(null);
                        dashWaitApproveAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                    }
                }
            }
        }
        return dashWaitApproveAjaxQueryDtos;
    }

    private List<DashReplyAjaxQueryDto> setReplyAjaxDataToShow(List<DashReplyAjaxQueryDto> dashReplyAjaxQueryDtos, HcsaTaskAssignDto hcsaTaskAssignDto) {
        if(!IaisCommonUtils.isEmpty(dashReplyAjaxQueryDtos)){
            for(DashReplyAjaxQueryDto dashReplyAjaxQueryDto : dashReplyAjaxQueryDtos){
                //get hciName / address
                AppGrpPremisesDto appGrpPremisesDto = inspectionMainAssignTaskService.getAppGrpPremisesDtoByAppCorrId(dashReplyAjaxQueryDto.getId());
                String address = inspectionMainAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
                if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                    dashReplyAjaxQueryDto.setHciAddress(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
                } else {
                    dashReplyAjaxQueryDto.setHciAddress(StringUtil.viewHtml(address));
                }
                //app status
                dashReplyAjaxQueryDto.setAppStatusStrShow(MasterCodeUtil.getCodeDesc(dashReplyAjaxQueryDto.getAppStatus()));
                //service
                HcsaServiceDto hcsaServiceDto = hcsaConfigMainClient.getHcsaServiceDtoByServiceId(dashReplyAjaxQueryDto.getServiceId()).getEntity();;
                dashReplyAjaxQueryDto.setServiceName(hcsaServiceDto.getSvcName());
                dashReplyAjaxQueryDto.setHciCode(StringUtil.viewHtml(appGrpPremisesDto.getHciCode()));
                //application
                ApplicationDto applicationDto = applicationMainClient.getAppByNo(dashReplyAjaxQueryDto.getApplicationNo()).getEntity();
                //get license date
                if(StringUtil.isEmpty(applicationDto.getOriginLicenceId())){
                    dashReplyAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                } else {
                    LicenceDto licenceDto = licenceClient.getLicDtoById(applicationDto.getOriginLicenceId()).getEntity();
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    if(licExpiryDate != null) {
                        dashReplyAjaxQueryDto.setLicenceExpiryDate(licExpiryDate);
                        String licExpiryDateStr = Formatter.formatDateTime(licExpiryDate, AppConsts.DEFAULT_DATE_FORMAT);
                        dashReplyAjaxQueryDto.setLicenceExpiryDateStr(licExpiryDateStr);
                    } else {
                        dashReplyAjaxQueryDto.setLicenceExpiryDate(null);
                        dashReplyAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                    }
                }
            }
        }
        return dashReplyAjaxQueryDtos;
    }

    private List<DashRenewAjaxQueryDto> setRenewAjaxDataToShow(List<DashRenewAjaxQueryDto> dashRenewAjaxQueryDtos, HcsaTaskAssignDto hcsaTaskAssignDto) {
        if(!IaisCommonUtils.isEmpty(dashRenewAjaxQueryDtos)){
            for(DashRenewAjaxQueryDto dashRenewAjaxQueryDto : dashRenewAjaxQueryDtos){
                //get hciName / address
                AppGrpPremisesDto appGrpPremisesDto = inspectionMainAssignTaskService.getAppGrpPremisesDtoByAppCorrId(dashRenewAjaxQueryDto.getId());
                String address = inspectionMainAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
                if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                    dashRenewAjaxQueryDto.setHciAddress(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
                } else {
                    dashRenewAjaxQueryDto.setHciAddress(StringUtil.viewHtml(address));
                }
                //app status
                dashRenewAjaxQueryDto.setAppStatusStrShow(MasterCodeUtil.getCodeDesc(dashRenewAjaxQueryDto.getAppStatus()));
                //service
                HcsaServiceDto hcsaServiceDto = hcsaConfigMainClient.getHcsaServiceDtoByServiceId(dashRenewAjaxQueryDto.getServiceId()).getEntity();;
                dashRenewAjaxQueryDto.setServiceName(hcsaServiceDto.getSvcName());
                dashRenewAjaxQueryDto.setHciCode(StringUtil.viewHtml(appGrpPremisesDto.getHciCode()));
                //application
                ApplicationDto applicationDto = applicationMainClient.getAppByNo(dashRenewAjaxQueryDto.getApplicationNo()).getEntity();
                //get license date
                if(StringUtil.isEmpty(applicationDto.getOriginLicenceId())){
                    dashRenewAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                } else {
                    LicenceDto licenceDto = licenceClient.getLicDtoById(applicationDto.getOriginLicenceId()).getEntity();
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    if(licExpiryDate != null) {
                        dashRenewAjaxQueryDto.setLicenceExpiryDate(licExpiryDate);
                        String licExpiryDateStr = Formatter.formatDateTime(licExpiryDate, AppConsts.DEFAULT_DATE_FORMAT);
                        dashRenewAjaxQueryDto.setLicenceExpiryDateStr(licExpiryDateStr);
                    } else {
                        dashRenewAjaxQueryDto.setLicenceExpiryDate(null);
                        dashRenewAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                    }
                }
            }
        }
        return dashRenewAjaxQueryDtos;
    }

    private List<DashWorkTeamAjaxQueryDto> setWorkTeamAjaxDataToShow(List<DashWorkTeamAjaxQueryDto> dashWorkTeamAjaxQueryDtos, HcsaTaskAssignDto hcsaTaskAssignDto) {
        if(!IaisCommonUtils.isEmpty(dashWorkTeamAjaxQueryDtos)){
            for(DashWorkTeamAjaxQueryDto dashWorkTeamAjaxQueryDto : dashWorkTeamAjaxQueryDtos){
                //get hciName / address
                AppGrpPremisesDto appGrpPremisesDto = inspectionMainAssignTaskService.getAppGrpPremisesDtoByAppCorrId(dashWorkTeamAjaxQueryDto.getId());
                String address = inspectionMainAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
                if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                    dashWorkTeamAjaxQueryDto.setHciAddress(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
                } else {
                    dashWorkTeamAjaxQueryDto.setHciAddress(StringUtil.viewHtml(address));
                }
                //app status
                dashWorkTeamAjaxQueryDto.setAppStatusStrShow(MasterCodeUtil.getCodeDesc(dashWorkTeamAjaxQueryDto.getAppStatus()));
                //service
                HcsaServiceDto hcsaServiceDto = hcsaConfigMainClient.getHcsaServiceDtoByServiceId(dashWorkTeamAjaxQueryDto.getServiceId()).getEntity();;
                dashWorkTeamAjaxQueryDto.setServiceName(hcsaServiceDto.getSvcName());
                dashWorkTeamAjaxQueryDto.setHciCode(StringUtil.viewHtml(appGrpPremisesDto.getHciCode()));
                //application
                ApplicationDto applicationDto = applicationMainClient.getAppByNo(dashWorkTeamAjaxQueryDto.getApplicationNo()).getEntity();
                //get license date
                if(StringUtil.isEmpty(applicationDto.getOriginLicenceId())){
                    dashWorkTeamAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                } else {
                    LicenceDto licenceDto = licenceClient.getLicDtoById(applicationDto.getOriginLicenceId()).getEntity();
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    if(licExpiryDate != null) {
                        dashWorkTeamAjaxQueryDto.setLicenceExpiryDate(licExpiryDate);
                        String licExpiryDateStr = Formatter.formatDateTime(licExpiryDate, AppConsts.DEFAULT_DATE_FORMAT);
                        dashWorkTeamAjaxQueryDto.setLicenceExpiryDateStr(licExpiryDateStr);
                    } else {
                        dashWorkTeamAjaxQueryDto.setLicenceExpiryDate(null);
                        dashWorkTeamAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                    }
                }
            }
        }
        return dashWorkTeamAjaxQueryDtos;
    }

    private List<DashAssignMeAjaxQueryDto> setAssignMeAjaxDataToShow(List<DashAssignMeAjaxQueryDto> dashAssignMeAjaxQueryDtos, HcsaTaskAssignDto hcsaTaskAssignDto) {
        if(!IaisCommonUtils.isEmpty(dashAssignMeAjaxQueryDtos)){
            for(DashAssignMeAjaxQueryDto dashAssignMeAjaxQueryDto : dashAssignMeAjaxQueryDtos){
                //get hciName / address
                AppGrpPremisesDto appGrpPremisesDto = inspectionMainAssignTaskService.getAppGrpPremisesDtoByAppCorrId(dashAssignMeAjaxQueryDto.getId());
                String address = inspectionMainAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
                if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                    dashAssignMeAjaxQueryDto.setHciAddress(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
                } else {
                    dashAssignMeAjaxQueryDto.setHciAddress(StringUtil.viewHtml(address));
                }
                //app status
                dashAssignMeAjaxQueryDto.setAppStatusStrShow(MasterCodeUtil.getCodeDesc(dashAssignMeAjaxQueryDto.getAppStatus()));
                //service
                HcsaServiceDto hcsaServiceDto = hcsaConfigMainClient.getHcsaServiceDtoByServiceId(dashAssignMeAjaxQueryDto.getServiceId()).getEntity();;
                dashAssignMeAjaxQueryDto.setServiceName(hcsaServiceDto.getSvcName());
                dashAssignMeAjaxQueryDto.setHciCode(StringUtil.viewHtml(appGrpPremisesDto.getHciCode()));
                //application
                ApplicationDto applicationDto = applicationMainClient.getAppByNo(dashAssignMeAjaxQueryDto.getApplicationNo()).getEntity();
                //get license date
                if(StringUtil.isEmpty(applicationDto.getOriginLicenceId())){
                    dashAssignMeAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                } else {
                    LicenceDto licenceDto = licenceClient.getLicDtoById(applicationDto.getOriginLicenceId()).getEntity();
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    if(licExpiryDate != null) {
                        dashAssignMeAjaxQueryDto.setLicenceExpiryDate(licExpiryDate);
                        String licExpiryDateStr = Formatter.formatDateTime(licExpiryDate, AppConsts.DEFAULT_DATE_FORMAT);
                        dashAssignMeAjaxQueryDto.setLicenceExpiryDateStr(licExpiryDateStr);
                    } else {
                        dashAssignMeAjaxQueryDto.setLicenceExpiryDate(null);
                        dashAssignMeAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                    }
                }
            }
        }
        return dashAssignMeAjaxQueryDtos;
    }

    private SearchParam filterPageConditions(SearchParam searchParam, SearchParam searchParamGroup, String appStatusKey, String application_status,
                                             HcsaTaskAssignDto hcsaTaskAssignDto, String fieldName, String hci_address) {
        if(searchParamGroup != null) {
            Map<String, Object> filters = searchParamGroup.getFilters();
            if(filters != null) {
                String application_type = (String)filters.get("application_type");
                String hci_code = (String)filters.get("hci_code");
                String hci_name = (String)filters.get("hci_name");
                if(!StringUtil.isEmpty(application_type)) {
                    searchParam.addFilter("application_type", application_type, true);
                }
                if(!StringUtil.isEmpty(application_status)) {
                    List<String> appStatus = mohHcsaBeDashboardService.getSearchAppStatus(application_status);
                    String appStatusStr = SqlHelper.constructInCondition(appStatusKey, appStatus.size());
                    searchParam.addParam("application_status", appStatusStr);
                    for(int i = 0; i < appStatus.size(); i++) {
                        searchParam.addFilter(appStatusKey + i, appStatus.get(i));
                    }
                }
                if(!StringUtil.isEmpty(hci_code)) {
                    searchParam.addFilter("hci_code", hci_code, true);
                }
                if(!StringUtil.isEmpty(hci_name)) {
                    searchParam.addFilter("hci_name", hci_name, true);
                }
                if(!StringUtil.isEmpty(hci_address)) {
                    searchParam = mohHcsaBeDashboardService.setAppPremisesIdsByUnitNos(searchParam, hci_address, hcsaTaskAssignDto, fieldName, "appPremId_list");
                }
            }
        }
        return searchParam;
    }

    private List<DashKpiPoolAjaxQuery> setKpiPoolAjaxDataToShow(List<DashKpiPoolAjaxQuery> dashKpiPoolAjaxQueryList, HcsaTaskAssignDto hcsaTaskAssignDto) {
        if(!IaisCommonUtils.isEmpty(dashKpiPoolAjaxQueryList)){
            for(DashKpiPoolAjaxQuery dashKpiPoolAjaxQuery : dashKpiPoolAjaxQueryList){
                //get hciName / address
                AppGrpPremisesDto appGrpPremisesDto = inspectionMainAssignTaskService.getAppGrpPremisesDtoByAppCorrId(dashKpiPoolAjaxQuery.getId());
                String address = inspectionMainAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
                if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                    dashKpiPoolAjaxQuery.setHciAddress(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
                } else {
                    dashKpiPoolAjaxQuery.setHciAddress(StringUtil.viewHtml(address));
                }
                //app status
                dashKpiPoolAjaxQuery.setAppStatusStrShow(MasterCodeUtil.getCodeDesc(dashKpiPoolAjaxQuery.getAppStatus()));
                //service
                HcsaServiceDto hcsaServiceDto = hcsaConfigMainClient.getHcsaServiceDtoByServiceId(dashKpiPoolAjaxQuery.getServiceId()).getEntity();;
                dashKpiPoolAjaxQuery.setServiceName(hcsaServiceDto.getSvcName());
                dashKpiPoolAjaxQuery.setHciCode(StringUtil.viewHtml(appGrpPremisesDto.getHciCode()));
                //application
                ApplicationDto applicationDto = applicationMainClient.getAppByNo(dashKpiPoolAjaxQuery.getApplicationNo()).getEntity();
                //get license date
                if(StringUtil.isEmpty(applicationDto.getOriginLicenceId())){
                    dashKpiPoolAjaxQuery.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                } else {
                    LicenceDto licenceDto = licenceClient.getLicDtoById(applicationDto.getOriginLicenceId()).getEntity();
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    if(licExpiryDate != null) {
                        dashKpiPoolAjaxQuery.setLicenceExpiryDate(licExpiryDate);
                        String licExpiryDateStr = Formatter.formatDateTime(licExpiryDate, AppConsts.DEFAULT_DATE_FORMAT);
                        dashKpiPoolAjaxQuery.setLicenceExpiryDateStr(licExpiryDateStr);
                    } else {
                        dashKpiPoolAjaxQuery.setLicenceExpiryDate(null);
                        dashKpiPoolAjaxQuery.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                    }
                }
            }
        }
        return dashKpiPoolAjaxQueryList;
    }

    private List<DashComPoolAjaxQueryDto> setComPoolAjaxDataToShow(List<DashComPoolAjaxQueryDto> dashComPoolAjaxQueryDtos, HcsaTaskAssignDto hcsaTaskAssignDto) {
        if(!IaisCommonUtils.isEmpty(dashComPoolAjaxQueryDtos)){
            for(DashComPoolAjaxQueryDto dashComPoolAjaxQueryDto : dashComPoolAjaxQueryDtos){
                //get hciName / address
                AppGrpPremisesDto appGrpPremisesDto = inspectionMainAssignTaskService.getAppGrpPremisesDtoByAppCorrId(dashComPoolAjaxQueryDto.getId());
                String address = inspectionMainAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
                if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                    dashComPoolAjaxQueryDto.setHciAddress(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
                } else {
                    dashComPoolAjaxQueryDto.setHciAddress(StringUtil.viewHtml(address));
                }
                //app status
                dashComPoolAjaxQueryDto.setAppStatusStrShow(MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT));
                //service
                HcsaServiceDto hcsaServiceDto = hcsaConfigMainClient.getHcsaServiceDtoByServiceId(dashComPoolAjaxQueryDto.getServiceId()).getEntity();;
                dashComPoolAjaxQueryDto.setServiceName(hcsaServiceDto.getSvcName());
                dashComPoolAjaxQueryDto.setHciCode(StringUtil.viewHtml(appGrpPremisesDto.getHciCode()));
                //application
                ApplicationDto applicationDto = applicationMainClient.getAppByNo(dashComPoolAjaxQueryDto.getApplicationNo()).getEntity();
                //get license date
                if(StringUtil.isEmpty(applicationDto.getOriginLicenceId())){
                    dashComPoolAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                } else {
                    LicenceDto licenceDto = licenceClient.getLicDtoById(applicationDto.getOriginLicenceId()).getEntity();
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    if(licExpiryDate != null) {
                        dashComPoolAjaxQueryDto.setLicenceExpiryDate(licExpiryDate);
                        String licExpiryDateStr = Formatter.formatDateTime(licExpiryDate, AppConsts.DEFAULT_DATE_FORMAT);
                        dashComPoolAjaxQueryDto.setLicenceExpiryDateStr(licExpiryDateStr);
                    } else {
                        dashComPoolAjaxQueryDto.setLicenceExpiryDate(null);
                        dashComPoolAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                    }
                }
            }
        }
        return dashComPoolAjaxQueryDtos;
    }

    @Override
    public String getKpiColorByTask(TaskDto taskDto) {
        String colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK;
        ApplicationDto applicationDto = inspectionTaskMainClient.getApplicationByCorreId(taskDto.getRefNo()).getEntity();
        if(applicationDto != null) {
            String stage;
            if (HcsaConsts.ROUTING_STAGE_INS.equals(taskDto.getTaskKey())) {
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto =
                        appPremisesRoutingHistoryMainClient.getAppPremisesRoutingHistorySubStage(taskDto.getRefNo(), taskDto.getTaskKey()).getEntity();
                stage = appPremisesRoutingHistoryDto.getSubStage();
            } else {
                stage = taskDto.getTaskKey();
            }
            HcsaServiceDto hcsaServiceDto = inspectionMainAssignTaskService.getHcsaServiceDtoByServiceId(applicationDto.getServiceId());
            HcsaSvcKpiDto hcsaSvcKpiDto = hcsaConfigMainClient.searchKpiResult(hcsaServiceDto.getSvcCode(), applicationDto.getApplicationType()).getEntity();
            if (hcsaSvcKpiDto != null) {
                //get current stage worked days
                int days = 0;
                if (!StringUtil.isEmpty(stage)) {
                    AppStageSlaTrackingDto appStageSlaTrackingDto = inspectionTaskMainClient.getSlaTrackByAppNoStageId(applicationDto.getApplicationNo(), stage).getEntity();
                    if (appStageSlaTrackingDto != null) {
                        days = appStageSlaTrackingDto.getKpiSlaDays();
                    }
                }
                //get warning value
                Map<String, Integer> kpiMap = hcsaSvcKpiDto.getStageIdKpi();
                int kpi = 0;
                if (!StringUtil.isEmpty(stage)) {
                    if (kpiMap != null && kpiMap.get(stage) != null) {
                        kpi = kpiMap.get(stage);
                    }
                }
                //get threshold value
                int remThreshold = 0;
                if (hcsaSvcKpiDto.getRemThreshold() != null) {
                    remThreshold = hcsaSvcKpiDto.getRemThreshold();
                }
                //get color
                colour = getColorByWorkAndKpiDay(kpi, days, remThreshold);
            }
        }
        return colour;
    }

    private String getColorByWorkAndKpiDay(int kpi, int days, int remThreshold) {
        String colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK;
        if(remThreshold != 0) {
            if (days < remThreshold) {
                colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK;
            }
            if (kpi != 0) {
                if (remThreshold <= days && days <= kpi) {
                    colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_AMBER;
                } else if (days > kpi) {
                    colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_RED;
                }
            }
        }
        return colour;
    }

    @SearchTrack(catalog = "intraDashboardQuery", key = "dashCommonTaskAjax")
    private SearchResult<DashComPoolAjaxQueryDto> getCommonAjaxResultByParam(SearchParam searchParam) {
        return inspectionTaskMainClient.searchDashComPoolDropResult(searchParam).getEntity();
    }

    @SearchTrack(catalog = "intraDashboardQuery", key = "dashKpiTaskAjax")
    private SearchResult<DashKpiPoolAjaxQuery> getKpiAjaxResultByParam(SearchParam searchParam) {
        return intraDashboardClient.searchDashKpiPoolDropResult(searchParam).getEntity();
    }

    @SearchTrack(catalog = "intraDashboardQuery", key = "dashAppRenewAjax")
    private SearchResult<DashRenewAjaxQueryDto> getRenewAjaxResultByParam(SearchParam searchParam) {
        return intraDashboardClient.searchDashRenewDropResult(searchParam).getEntity();
    }

    @SearchTrack(catalog = "intraDashboardQuery", key = "dashWaitApproveAjax")
    private SearchResult<DashWaitApproveAjaxQueryDto> getWaitAjaxResultByParam(SearchParam searchParam) {
        return intraDashboardClient.searchDashWaitApproveDropResult(searchParam).getEntity();
    }

    @SearchTrack(catalog = "intraDashboardQuery", key = "dashAppReplyAjax")
    private SearchResult<DashReplyAjaxQueryDto> getReplyAjaxResultByParam(SearchParam searchParam) {
        return intraDashboardClient.searchDashReplyPoolDropResult(searchParam).getEntity();
    }

    @Override
    @SearchTrack(catalog = "intraDashboardQuery", key = "dashSystemDetailAjax")
    public SearchResult<DashAppDetailsQueryDto> getDashAllActionResult(SearchParam searchParam) {
        return intraDashboardClient.searchDashAppDetailsResult(searchParam).getEntity();
    }

    @Override
    public SearchResult<DashAppDetailsQueryDto> setDashSysDetailsDropOtherData(SearchResult<DashAppDetailsQueryDto> searchResult) {
        if(searchResult != null) {
            List<DashAppDetailsQueryDto> dashAppDetailsQueryDtos = searchResult.getRows();
            if(!IaisCommonUtils.isEmpty(dashAppDetailsQueryDtos)) {
                for(DashAppDetailsQueryDto dashAppDetailsQueryDto : dashAppDetailsQueryDtos) {
                    if(dashAppDetailsQueryDto != null) {
                        //set app appType
                        String appType = dashAppDetailsQueryDto.getAppType();
                        dashAppDetailsQueryDto.setAppTypeStrShow(MasterCodeUtil.getCodeDesc(appType));
                        //set service name
                        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(dashAppDetailsQueryDto.getSvcCode());
                        if(hcsaServiceDto != null) {
                            dashAppDetailsQueryDto.setServiceName(hcsaServiceDto.getSvcName());
                            //set all stage kpi
                            HcsaSvcKpiDto hcsaSvcKpiDto = hcsaConfigMainClient.searchKpiResult(hcsaServiceDto.getSvcCode(), appType).getEntity();
                            dashAppDetailsQueryDto = setSumKpiByHcsaSvcKpiDto(hcsaSvcKpiDto, dashAppDetailsQueryDto);
                        }
                        //set kpi color
                        String color = HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK;
                        List<TaskDto> taskDtos = organizationMainClient.getCurrTaskByRefNo(dashAppDetailsQueryDto.getId()).getEntity();
                        if(!IaisCommonUtils.isEmpty(taskDtos)) {
                            TaskDto taskDto = taskDtos.get(0);
                            color = getKpiColorByTask(taskDto);
                        }
                        dashAppDetailsQueryDto.setKpiColor(color);
                        //set officer's name
                        List<String> appOwnerList = IaisCommonUtils.genNewArrayList();
                        if(IaisCommonUtils.isEmpty(taskDtos)) {
                            appOwnerList.add("Pending Assignment");
                        } else {
                            for(TaskDto taskDto : taskDtos) {
                                if(taskDto != null && !StringUtil.isEmpty(taskDto.getUserId())) {
                                    OrgUserDto orgUserDto = organizationMainClient.retrieveOrgUserAccountById(taskDto.getUserId()).getEntity();
                                    if(orgUserDto != null) {
                                        appOwnerList.add(orgUserDto.getDisplayName());
                                    }
                                }
                            }
                            if(IaisCommonUtils.isEmpty(appOwnerList)) {
                                appOwnerList.add("Pending Assignment");
                            }
                        }
                        Collections.sort(appOwnerList);
                        dashAppDetailsQueryDto.setAppOwnerList(appOwnerList);
                    }
                }
            }
        }
        return searchResult;
    }

    private DashAppDetailsQueryDto setSumKpiByHcsaSvcKpiDto(HcsaSvcKpiDto hcsaSvcKpiDto, DashAppDetailsQueryDto dashAppDetailsQueryDto) {
        dashAppDetailsQueryDto.setAllStageSumKpi(0 + "");
        if(hcsaSvcKpiDto != null) {
            Map<String, Integer> stageIdKpi = hcsaSvcKpiDto.getStageIdKpi();
            if(stageIdKpi != null) {
                int sumKpi = 0;
                for(Map.Entry<String, Integer> map : stageIdKpi.entrySet()) {
                    Integer kpi = map.getValue();
                    if(kpi != null) {
                        sumKpi = sumKpi + kpi;
                    }
                }
                dashAppDetailsQueryDto.setAllStageSumKpi(sumKpi + "");
            }
        }
        return dashAppDetailsQueryDto;
    }

    @SearchTrack(catalog = "intraDashboardQuery", key = "dashAssignMeAjax")
    private SearchResult<DashAssignMeAjaxQueryDto> getAssignMeAjaxResultByParam(SearchParam searchParam) {
        return inspectionTaskMainClient.searchDashAssignMeAjaxResult(searchParam).getEntity();
    }

    @SearchTrack(catalog = "intraDashboardQuery", key = "dashSupervisorAjax")
    private SearchResult<DashWorkTeamAjaxQueryDto> getWorkTeamAjaxResultByParam(SearchParam searchParam) {
        return inspectionTaskMainClient.searchDashWorkTeamDropResult(searchParam).getEntity();
    }

    private List<String> getAppPremCorrIdsByDto(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos) {
        List<String> appPremCorrIds = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appPremisesCorrelationDtos)) {
            for (AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos) {
                if(appPremisesCorrelationDto != null) {
                    appPremCorrIds.add(appPremisesCorrelationDto.getId());
                }
            }
        }
        return appPremCorrIds;
    }
}