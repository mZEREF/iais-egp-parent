package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashAppDetailsQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import java.util.Map;

/**
 * @author Shicheng
 * @date 2021/4/19 10:26
 **/
public interface BeDashboardAjaxService {

    /**
      * @author: shicheng
      * @Date 2021/4/28
      * @Param: taskDto
      * @return: String
      * @Descripation: search kpi color
      */
    String getKpiColorByTask(TaskDto taskDto);

    /**
      * @author: shicheng
      * @Date 2021/4/19
      * @Param: groupNo, loginContext, map, actionValue, dashFilterAppNo
      * @return: map
      * @Descripation: Common Pool Dropdown
      */
    Map<String, Object> getCommonDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                String actionValue, String dashFilterAppNo, HcsaTaskAssignDto hcsaTaskAssignDto, String hci_address);

    /**
      * @author: shicheng
      * @Date 2021/4/28
      * @Param: groupNo, loginContext, map, switchAction, dashFilterAppNo
      * @return: map
      * @Descripation: Kpi Pool Dropdown
      */
    Map<String, Object> getKpiDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                             String switchAction, String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto,
                                             String hci_address);

    /**
      * @author: shicheng
      * @Date 2021/4/30
      * @Param: groupNo, loginContext, map, searchParam, dashFilterAppNo
      * @return: Map<String, Object>
      * @Descripation: Task Assign To Me Drop down Result
      */
    Map<String, Object> getAssignMeDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                  String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto, String hci_address);

    /**
      * @author: shicheng
      * @Date 2021/5/6
      * @Param: groupNo, loginContext, map, switchAction, dashFilterAppNo
      * @return: Map<String, Object>
      * @Descripation: getWorkTeamDropdownResult
      */
    Map<String, Object> getWorkTeamDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                  String switchAction, String dashFilterAppNo, String dashCommonPoolStatus, String dashAppStatus,
                                                  HcsaTaskAssignDto hcsaTaskAssignDto, String hci_address);

    /**
      * @author: shicheng
      * @Date 2021/5/7
      * @Param: groupNo, loginContext, map, switchAction, dashFilterAppNo
      * @return: Map<String, Object>
      * @Descripation: getRenewDropdownResult
      */
    Map<String, Object> getRenewDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                               String switchAction, String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto,
                                               String hci_address);

    /**
      * @author: shicheng
      * @Date 2021/5/8
      * @Param: groupNo, loginContext, map, switchAction, dashFilterAppNo
      * @return: Map<String, Object>
      * @Descripation: getReplyDropdownResult
      */
    Map<String, Object> getReplyDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                               String switchAction, String dashFilterAppNo, HcsaTaskAssignDto hcsaTaskAssignDto, String hci_address);

    /**
      * @author: shicheng
      * @Date 2021/5/8
      * @Param: groupNo, loginContext, map, switchAction, dashFilterAppNo
      * @return: Map<String, Object>
      * @Descripation: getWaitApproveDropResult
      */
    Map<String, Object> getWaitApproveDropResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                 String switchAction, String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto,
                                                 String hci_address);

    /**
      * @author: shicheng
      * @Date 2021/5/27
      * @Param: searchParam
      * @return: SearchResult<DashAllActionAppQueryDto>
      * @Descripation: getDashAllActionResult
      */
    SearchResult<DashAppDetailsQueryDto> getDashAllActionResult(SearchParam searchParam);

    /**
      * @author: shicheng
      * @Date 2021/5/27
      * @Param: searchResult
      * @return: SearchResult<DashAppDetailsQueryDto>
      * @Descripation: setDashSysDetailsDropOtherData
      */
    SearchResult<DashAppDetailsQueryDto> setDashSysDetailsDropOtherData(SearchResult<DashAppDetailsQueryDto> searchResult);
}
