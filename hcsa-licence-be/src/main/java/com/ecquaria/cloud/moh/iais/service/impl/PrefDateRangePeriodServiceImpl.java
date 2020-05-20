package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
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

import java.util.Date;

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

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Value("${spring.application.name}")
    private String currentApp;
    @Value("${iais.current.domain}")
    private String currentDomain;

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
            periodDto.setEicCall(true);
            EicRequestTrackingDto postSaveTrack = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.HCSA_CONFIG,
                    PrefDateRangePeriodServiceImpl.class.getName(),
                    "callFeInspPeriod", currentApp + "-" + currentDomain,
                    HcsaServicePrefInspPeriodDto.class.getName(), JsonUtil.parseToJson(periodDto));

            try {
                FeignResponseEntity<EicRequestTrackingDto> fetchResult =  eicRequestTrackingHelper.getHcsaConfigClient().getPendingRecordByReferenceNumber(postSaveTrack.getRefNo());
                if (HttpStatus.SC_OK == fetchResult.getStatusCode()){
                    EicRequestTrackingDto preEicRequest = fetchResult.getEntity();
                    if (AppConsts.EIC_STATUS_PENDING_PROCESSING.equals(preEicRequest.getStatus())){
                        callFeInspPeriod(periodDto);
                        preEicRequest.setProcessNum(1);
                        Date now = new Date();
                        preEicRequest.setFirstActionAt(now);
                        preEicRequest.setLastActionAt(now);
                        preEicRequest.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                        eicRequestTrackingHelper.getHcsaConfigClient().saveEicTrack(preEicRequest);
                    }
                }
            }catch (Exception e){
                log.error(StringUtil.changeForLog("encounter failure when sync inspection period date to fe" + e.getMessage()));
            }

            return Boolean.TRUE;
        }else {
            return Boolean.FALSE;
        }
    }

    private void callFeInspPeriod(HcsaServicePrefInspPeriodDto periodDto){
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

        beEicGatewayClient.syncInspPeriodToFe(periodDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }

    private Integer transformToDay(Integer week) {
        if (week == null) {
            return null;
        }

        return week * 7;
    }
}
