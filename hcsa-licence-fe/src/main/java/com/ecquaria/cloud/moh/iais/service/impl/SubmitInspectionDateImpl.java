package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.service.SubmitInspectionDate;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
public class SubmitInspectionDateImpl implements SubmitInspectionDate {
    @Value("${spring.application.name}")
    private String currentApp;

    @Value("${iais.current.domain}")
    private String currentDomain;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private ApplicationFeClient applicationFeClient;

    @Autowired
    private AppConfigClient appConfigClient;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    @Override
    public ApplicationGroupDto getApplicationGroupByGroupId(String groupId) {
        return applicationFeClient.getApplicationGroup(groupId).getEntity();
    }

    @Override
    public List<ApplicationDto> listApplicationByGroupId(String groupId) {
        return applicationFeClient.listApplicationByGroupId(groupId).getEntity();
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
        c.add(Calendar.DAY_OF_MONTH, maxAfter);
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
        c.add(Calendar.DAY_OF_MONTH, maxBefore * -1);
        return c.getTime();
    }

    public void savePrefStatDateAndEndDateToBe(ApplicationGroupDto applicationGroupDto){
        feEicGatewayClient.saveAppGroupSysnEic(applicationGroupDto);
    }

    @Override
    public void submitInspStartDateAndEndDate(String groupId, Date sDate, Date eDate) {
        ApplicationGroupDto applicationGroupDto = new ApplicationGroupDto();
        applicationGroupDto.setId(groupId);
        applicationGroupDto.setPrefInspStartDate(sDate);
        applicationGroupDto.setPrefInspEndDate(eDate);
        applicationGroupDto.setSubmittedPrefDateFlag(true);
        //save to fe
        applicationFeClient.doUpDate(applicationGroupDto);

        try {
            savePrefStatDateAndEndDateToBe(applicationGroupDto);
            eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT,
                    SubmitInspectionDateImpl.class.getName(),
                    "savePrefStatDateAndEndDateToBe", currentApp + "-" + currentDomain,
                    ApplicationGroupDto.class.getName(), JsonUtil.parseToJson(applicationGroupDto));
        }catch (Exception e){
            log.error(StringUtil.changeForLog("encounter failure when savePrefStatDateAndEndDateToBe"), e);
        }

    }
}
