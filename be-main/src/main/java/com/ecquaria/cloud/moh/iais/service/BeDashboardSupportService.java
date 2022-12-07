package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloudfeign.FeignException;

import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2021/4/14 14:55
 **/
public interface BeDashboardSupportService {
    void saveRejectReturnFee(List<ApplicationDto> applicationDtos, BroadcastApplicationDto broadcastApplicationDto);

    void doRefunds(List<AppReturnFeeDto> saveReturnFeeDtos);

    void inspectorAo1(LoginContext loginContext, ApplicationViewDto applicationViewDto, TaskDto taskDto);

    List<ApplicationDto> getStatusAppList(List<ApplicationDto> applicationDtos, String status);

    List<ApplicationDto> removeFastTracking(List<ApplicationDto> applicationDtos);

    void updateCurrentApplicationStatus(List<ApplicationDto> applicationDtos, String applicationId, String status);

    void updateCurAppStatusByLicensee(Map<String, String> returnFee, List<ApplicationDto> applicationDtos, String licenseeId);

    List<ApplicationDto> removeCurrentApplicationDto(List<ApplicationDto> applicationDtoList, String currentId);

    TaskDto completedTask(TaskDto taskDto);

    WorkingGroupDto changeStatusWrokGroup(WorkingGroupDto workingGroupDto, String status);

    List<UserGroupCorrelationDto> changeStatusUserGroupCorrelationDtos(List<UserGroupCorrelationDto> userGroupCorrelationDtos, String status);

    List<ApplicationDto> removeFastTrackingAndTransfer(List<ApplicationDto> applicationDtos);

    void changePostInsForTodoAudit(ApplicationViewDto applicationViewDto);

    String checkAllStatus(List<ApplicationDto> applicationDtoList,List<String> appList);

    void doAo1Ao2Approve(BroadcastOrganizationDto broadcastOrganizationDto, BroadcastApplicationDto broadcastApplicationDto, ApplicationDto applicationDto,
                         List<String> appNo, TaskDto taskDto, String newCorrelationId) throws FeignException;

    void setRiskScore(ApplicationDto applicationDto,String newCorrelationId);

    void rejectSendNotification(ApplicationDto applicationDto);

    void rfcSendRejectNotification(String applicationTypeShow, String applicationNo, String appDate, String mohName, ApplicationDto applicationDto,
                                           List<String> svcCodeList);
    void newAppSendNotification(String applicationTypeShow,String applicationNo,String appDate,String mohName,ApplicationDto applicationDto,List<String> svcCodeList);

    void renewalSendNotification(String applicationTypeShow, String applicationNo, String appDate, String mohName, ApplicationDto applicationDto,
                                        List<String> svcCodeList);
    }
