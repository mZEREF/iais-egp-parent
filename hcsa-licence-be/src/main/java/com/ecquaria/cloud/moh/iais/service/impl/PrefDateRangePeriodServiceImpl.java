package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.PrefDateRangePeriodService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;


    @Override
    public SearchResult<HcsaServicePrefInspPeriodQueryDto> getHcsaServicePrefInspPeriodList(SearchParam searchParam) {
        return hcsaConfigClient.getHcsaServicePrefInspPeriodList(searchParam).getEntity();
    }

    @Override
    public Boolean savePrefInspPeriod(HcsaServicePrefInspPeriodDto period) {
        period.setPeriodAfterApp(transformToDay(period.getPeriodAfterApp()));
        period.setPeriodBeforeExp(transformToDay(period.getPeriodBeforeExp()));

        FeignResponseEntity<HcsaServicePrefInspPeriodDto> result =  hcsaConfigClient.savePrefInspPeriod(period);
        int status = result.getStatusCode();
        if (status == HttpStatus.SC_OK){
            HcsaServicePrefInspPeriodDto periodDto = result.getEntity();
            try {
                HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

                beEicGatewayClient.syncInspPeriodToFe(periodDto, signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization());
            }catch (Exception e){
                log.error(StringUtil.changeForLog("encounter failure when sync inspection period date to fe" + e.getMessage()));
            }

            return Boolean.TRUE;
        }else {
            return Boolean.FALSE;
        }
    }

    private Integer transformToDay(Integer week) {
        if (week == null) {
            return null;
        }

        return week * 7;
    }
}
