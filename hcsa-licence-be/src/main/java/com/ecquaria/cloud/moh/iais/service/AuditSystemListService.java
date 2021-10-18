package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.role.Role;
import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/2/19 10:54
 */
public interface AuditSystemListService {
    List<AuditTaskDataFillterDto> getInspectors(List<AuditTaskDataFillterDto> auditTaskDataDtos);

    List<SelectOption> getAuditOp();

    void doSubmit(List<AuditTaskDataFillterDto> auditTaskDataDtos);

    List<AuditTaskDataFillterDto> doRemove(List<AuditTaskDataFillterDto> auditTaskDataDtos);

    void doCancel(List<AuditTaskDataFillterDto> auditTaskDataDtos);

    List<HcsaServiceDto> getActiveHCIServices();

    List<SelectOption> getActiveHCIServicesByNameOrCode(List<HcsaServiceDto> hcsaServiceDtos, String type);
    List<SelectOption> getActiveHCICode();
    void doRejectCancelTask(List<AuditTaskDataFillterDto> auditTaskDataDtos);
    void doCanceledTask(List<AuditTaskDataFillterDto> auditTaskDataDtos);

    void sendEmailToIns(String emailKey,String appGroupNo,AuditTaskDataFillterDto auditTaskDataFillterDto);

    void sendMailForAuditPlaner(String emailKey);

    void sendMailForAuditPlanerForSms(String emailKey);

    void sendEmailToInsForSms(String emailKey,String appGroupNo);

    List<SelectOption> getCanViewAuditRoles(List<String> roleIds);

    List<Role> getRolesByDomain(String domain);

    SelectOption getRoleSelectOption(List<Role> roles,String roleId);

    boolean  rightControlForRole(List<SelectOption> roleSels,String roleId);

    void setTcuAuditFlag(List<AuditTaskDataFillterDto> auditTaskDataDtos);

    void filetDoc(AppSubmissionDto appSubmissionDto);

    void setRiskToDto(AppSubmissionDto appSubmissionDto);
}
