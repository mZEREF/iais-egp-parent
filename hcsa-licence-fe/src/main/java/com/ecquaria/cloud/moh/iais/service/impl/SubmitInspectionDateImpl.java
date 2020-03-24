package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.service.SubmitInspectionDate;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: yichen
 * @description:
 * @date:2020/3/23
 **/

@Service
public class SubmitInspectionDateImpl implements SubmitInspectionDate {

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private AppConfigClient appConfigClient;

    @Override
    public ApplicationGroupDto getApplicationGroupByGroupId(String groupId) {
        return applicationClient.getApplicationGroup(groupId).getEntity();
    }

    @Override
    public List<ApplicationDto> listApplicationByGroupId(String groupId) {
        return applicationClient.listApplicationByGroupId(groupId).getEntity();
    }

    @Override
    public List<HcsaServicePrefInspPeriodDto> getPrefInspPeriodList(List<ApplicationDto> appList) {
        List<HcsaServicePrefInspPeriodDto> periodList = appConfigClient.getPrefInspPeriodList().getEntity();

        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        appList.forEach(i -> {
            String svcId = i.getServiceId();
            String svcCode = HcsaServiceCacheHelper.getServiceById(svcId).getSvcCode();
            svcCodeList.add(svcCode);
        });

        periodList = periodList.stream().filter(i -> svcCodeList.contains(i.getSvcCode())).collect(Collectors.toList());

        return periodList;
    }

    @Override
    public Date getBlockPeriodByAfterApp(Date submitDate, List<HcsaServicePrefInspPeriodDto> blockPeriodList) {
        if (IaisCommonUtils.isEmpty(blockPeriodList)){
            return null;
        }

        int maxAfter = Integer.MIN_VALUE;
        for (HcsaServicePrefInspPeriodDto period : blockPeriodList){
            maxAfter = Math.max(maxAfter, period.getPeriodAfterApp());
        }

        Calendar c = Calendar.getInstance();
        c.setTime(submitDate);
        c.add(Calendar.DAY_OF_WEEK, maxAfter);
        return c.getTime();
    }

    @Override
    public Date getBlockPeriodByBeforeLicenceExpire(Date submitDate, List<HcsaServicePrefInspPeriodDto> blockPeriodList) {
        if (IaisCommonUtils.isEmpty(blockPeriodList)){
            return null;
        }

        int maxBefore = Integer.MIN_VALUE;
        for (HcsaServicePrefInspPeriodDto period : blockPeriodList){
            maxBefore = Math.max(maxBefore, period.getPeriodBeforeExp());
        }

        Calendar c = Calendar.getInstance();
        c.setTime(submitDate);
        c.add(Calendar.DAY_OF_WEEK, maxBefore);
        return c.getTime();
    }

    @Override
    public void submitInspStartDateAndEndDate(String groupId, Date sDate, Date eDate) {
        ApplicationGroupDto applicationGroupDto = new ApplicationGroupDto();
        applicationGroupDto.setId(groupId);
        applicationGroupDto.setPrefInspStartDate(sDate);
        applicationGroupDto.setPrefInspEndDate(eDate);
        applicationClient.doUpDate(applicationGroupDto);
    }
}
