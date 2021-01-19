package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;

import java.util.Date;
import java.util.List;

public interface SubmitInspectionDate {
    ApplicationGroupDto getApplicationGroupByGroupId(String groupId);

    List<ApplicationDto> listApplicationByGroupId(String groupId);

    List<HcsaServicePrefInspPeriodDto> getPrefInspPeriodList(List<ApplicationDto> appList);

    Date getBlockPeriodByAfterApp(Date submitDate, List<HcsaServicePrefInspPeriodDto> blockPeriodList);

    Date getBlockPeriodByBeforeLicenceExpire(Date submitDate, List<HcsaServicePrefInspPeriodDto> blockPeriodList);

    void submitInspStartDateAndEndDate(String groupId, Date sDate, Date eDate);
}
