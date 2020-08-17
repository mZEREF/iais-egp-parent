package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;

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

    void sendEmailToInsForSms(String emailKey);
}
