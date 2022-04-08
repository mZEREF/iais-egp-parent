package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodQueryDto;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.PrefDateRangePeriodService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: yichen
 * @date time:12/24/2019 9:03 PM
 * @description:
 */

@Service
@Slf4j
public class PrefDateRangePeriodServiceImpl implements PrefDateRangePeriodService {
    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Override
    @SearchTrack(catalog = "hcsaconfig", key = "getPrefInspPeriodList")
    public SearchResult<HcsaServicePrefInspPeriodQueryDto> getHcsaServicePrefInspPeriodList(SearchParam searchParam) {
        return hcsaConfigClient.getHcsaServicePrefInspPeriodList(searchParam).getEntity();
    }

    @Override
    public Boolean savePrefInspPeriod(HcsaServicePrefInspPeriodDto period) {
        period.setTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        period.setPeriodAfterApp(transformToDay(period.getPeriodAfterApp()));
        period.setPeriodBeforeExp(transformToDay(period.getPeriodBeforeExp()));

        FeignResponseEntity<HcsaServicePrefInspPeriodDto> result =  hcsaConfigClient.savePrefInspPeriod(period);
        int status = result.getStatusCode();
        if (status == HttpStatus.SC_OK){
            HcsaServicePrefInspPeriodDto periodDto = result.getEntity();
            periodDto.setEicCall(true);
            callFeInspPeriod(periodDto);
            return Boolean.TRUE;
        }else {
            return Boolean.FALSE;
        }
    }

    public void callFeInspPeriod(HcsaServicePrefInspPeriodDto periodDto) {
        beEicGatewayClient.callEicWithTrack(periodDto, beEicGatewayClient::syncInspPeriodToFe, beEicGatewayClient.getClass(),
                "syncInspPeriodToFe", EicClientConstant.HCSA_CONFIG);
    }

    private Integer transformToDay(Integer week) {
        if (week == null) {
            return null;
        }

        return week * 7;
    }
}
