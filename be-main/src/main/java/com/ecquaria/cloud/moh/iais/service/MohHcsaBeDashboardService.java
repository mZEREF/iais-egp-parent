package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.PoolRoleCheckDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashAllActionAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashAllGrpAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashAssignMeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashComPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashKpiPoolQuery;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashRenewQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashReplyQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashStageCircleKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashWaitApproveQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashWorkTeamQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import java.util.Date;
import java.util.List;

/**
 * @author Shicheng
 * @date 2021/4/14 14:37
 **/
public interface MohHcsaBeDashboardService {
    AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus, String decision,
                                                                 TaskDto taskDto, String userId, String remarks, String subStage);

    void updateInspectionStatus(String appPremisesCorrelationId, String status);

    AppPremisesRoutingHistoryDto getAppPremisesRoutingHistory(String appNo, String appStatus,
                                                              String stageId,String subStageId,String wrkGrpId, String internalRemarks,String externalRemarks,String processDecision,
                                                              String roleId);

    /**
      * @author: shicheng
      * @Date 2021/4/15
      * @Param: SearchParam, loginContext, actionValue
      * @return: List<String>
      * @Descripation: setPoolScopeByCurRoleId
      */
    List<String> setPoolScopeByCurRoleId(SearchParam searchParam, LoginContext loginContext, String switchAction, List<String> workGroupIds);

    SearchResult<DashComPoolQueryDto> getDashComPoolResult(SearchParam searchParam);

    SearchResult<DashComPoolQueryDto> getDashComPoolOtherData(SearchResult<DashComPoolQueryDto> searchResult);

    SearchResult<DashKpiPoolQuery> getDashKpiPoolResult(SearchParam searchParam);

    SearchResult<DashKpiPoolQuery> getDashKpiPoolOtherData(SearchResult<DashKpiPoolQuery> searchResult);

    PoolRoleCheckDto getDashRoleOption(LoginContext loginContext, PoolRoleCheckDto poolRoleCheckDto);

    SearchResult<DashAssignMeQueryDto> getDashAssignMeResult(SearchParam searchParam);

    SearchResult<DashAssignMeQueryDto> getDashAssignMeOtherData(SearchResult<DashAssignMeQueryDto> searchResult);

    SearchResult<DashWorkTeamQueryDto> getDashWorkTeamResult(SearchParam searchParam);

    SearchResult<DashWorkTeamQueryDto> getDashWorkTeamOtherData(SearchResult<DashWorkTeamQueryDto> searchResult);

    SearchResult<DashReplyQueryDto> getDashReplyResult(SearchParam searchParam);

    SearchResult<DashReplyQueryDto> getDashReplyOtherData(SearchResult<DashReplyQueryDto> searchResult);

    SearchResult<DashWaitApproveQueryDto> getDashWaitApproveResult(SearchParam searchParam);

    SearchResult<DashWaitApproveQueryDto> getDashWaitApproveOtherData(SearchResult<DashWaitApproveQueryDto> searchResult);

    SearchResult<DashRenewQueryDto> getDashRenewResult(SearchParam searchParam);

    SearchResult<DashRenewQueryDto> getDashRenewOtherData(SearchResult<DashRenewQueryDto> searchResult);

    /**
      * @author: shicheng
      * @Date 2021/5/10
      * @Param: curRoleId, dashSwitchActionValue
      * @return: List<SelectOption>
      * @Descripation: get Application status option By Role and Switch
      */
    List<SelectOption> getAppStatusOptionByRoleAndSwitch(String curRoleId, String dashSwitchActionValue);

    /**
      * @author: shicheng
      * @Date 2021/5/14
      * @Param: appGroupId
      * @return: Date
      * @Descripation: getMaxUpdateByAppGroup
      */
    Date getMaxUpdateByAppGroup(String appGroupId);

    /**
      * @author: shicheng
      * @Date 2021/5/17
      * @Param: application_status, appStatusOption
      * @return:
      * @Descripation:
      */
    boolean containsAppStatus(List<SelectOption> appStatusOption, String application_status);

    /**
      * @author: shicheng
      * @Date 2021/5/20
      * @Param: application_status
      * @return: List<String>
      * @Descripation: getSearchAppStatus
      */
    List<String> getSearchAppStatus(String application_status);

    /**
      * @author: shicheng
      * @Date 2021/5/24
      * @Param: null
      * @return: List<SelectOption>
      * @Descripation: getHashServiceOption
      */
    List<SelectOption> getHashServiceOption();

    /**
      * @author: shicheng
      * @Date 2021/5/24
      * @Param: searchResult
      * @return: List<DashStageCircleKpiDto>
      * @Descripation: getDashStageCircleKpiShow
      */
    List<DashStageCircleKpiDto> getDashStageCircleKpiShow(SearchResult<DashAllActionAppQueryDto> searchResult);

    /**
      * @author: shicheng
      * @Date 2021/5/24
      * @Param: searchParam
      * @return: SearchResult<DashAllActionAppQueryDto>
      * @Descripation: getDashAllActionResult
      */
    SearchResult<DashAllActionAppQueryDto> getDashAllActionResult(SearchParam searchParam);

    /**
      * @author: shicheng
      * @Date 2021/5/25
      * @Param: searchParam, services, appTypes
      * @return: SearchParam
      * @Descripation: setSysDashFilter
      */
    SearchParam setSysDashFilter(SearchParam searchParam, String[] services, String[] appTypes);

    /**
      * @author: shicheng
      * @Date 2021/5/27
      * @Param: dashSysStageVal
      * @return: String
      * @Descripation: getStageIdByJspClickVal
      */
    String getStageIdByJspClickVal(String dashSysStageVal);

    /**
      * @author: shicheng
      * @Date 2021/5/27
      * @Param: searchCountParam
      * @return: SearchResult<DashAllGrpAppQueryDto>
      * @Descripation: getDashSysGrpDetailQueryResult
      */
    SearchResult<DashAllGrpAppQueryDto> getDashSysGrpDetailQueryResult(SearchParam searchCountParam);

    /**
      * @author: shicheng
      * @Date 2021/5/27
      * @Param: searchResult
      * @return: SearchResult<DashAllGrpAppQueryDto>
      * @Descripation: getDashSysGrpDetailOtherData
      */
    SearchResult<DashAllGrpAppQueryDto> getDashSysGrpDetailOtherData(SearchResult<DashAllGrpAppQueryDto> searchResult);

    /**
      * @author: shicheng
      * @Date 2021/5/27
      * @Param: searchCountResult, serviceOption
      * @return: List<DashStageCircleKpiDto>
      * @Descripation: getDashStageSvcKpiShow
      */
    List<DashStageCircleKpiDto> getDashStageSvcKpiShow(SearchResult<DashAllActionAppQueryDto> searchCountResult, List<SelectOption> serviceOption);

    /**
      * @author: shicheng
      * @Date 2021/7/5
      * @Param: searchParam, services, appTypes, applicationNo
      * @return: SearchParam
      * @Descripation: setStatisticsDashFilter
      */
    SearchParam setStatisticsDashFilter(SearchParam searchParam, String[] services, String[] appTypes, String applicationNo);

    /**
      * @author: shicheng
      * @Date 2021/7/5
      * @Param: loginContext
      * @return: String
      * @Descripation: getPrivilegeFlagByRole
      */
    String getPrivilegeFlagByRole(LoginContext loginContext);

    void createReportResult(TaskDto taskDto, ApplicationViewDto applicationViewDto,String userId);

    List<String> getAssignMeAppGrpIdByResult(SearchResult<DashAssignMeQueryDto> searchResult);

    List<String> getComPoolAppGrpIdByResult(SearchResult<DashComPoolQueryDto> searchResult);

    List<String> getKpiPoolAppGrpIdByResult(SearchResult<DashKpiPoolQuery> searchResult);

    List<String> getSuperPoolAppGrpIdByResult(SearchResult<DashWorkTeamQueryDto> searchResult);

    List<String> getReplyAppGrpIdByResult(SearchResult<DashReplyQueryDto> searchResult);

    List<String> getWaitApproveAppGrpIdByResult(SearchResult<DashWaitApproveQueryDto> searchResult);

    List<String> getRenewAppGrpIdByResult(SearchResult<DashRenewQueryDto> searchResult);

    HcsaTaskAssignDto getHcsaTaskAssignDtoByAppGrp(List<String> appGroupIds);

    SearchParam setAppGrpIdsByUnitNos(SearchParam searchParam, String hci_address, HcsaTaskAssignDto hcsaTaskAssignDto, String appGroupIdFieldName, String appGroup_list);

    SearchParam setAppPremisesIdsByUnitNos(SearchParam searchParam, String hci_address, HcsaTaskAssignDto hcsaTaskAssignDto, String fieldName, String filterName);
}
