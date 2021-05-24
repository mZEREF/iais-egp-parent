package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.PoolRoleCheckDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashAssignMeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashComPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashKpiPoolQuery;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashRenewQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashReplyQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashStageCircleKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashWaitApproveQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashWorkTeamQueryDto;
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
     * @Param:
     * @return: List<DashStageCircleKpiDto>
     * @Descripation: getDashStageCircleKpiShow
     */
    List<DashStageCircleKpiDto> getDashStageCircleKpiShow();
}
