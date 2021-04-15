package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

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
    List<String> setPoolScopeByCurRoleId(SearchParam searchParam, LoginContext loginContext, String actionValue, List<String> workGroupIds);
}
